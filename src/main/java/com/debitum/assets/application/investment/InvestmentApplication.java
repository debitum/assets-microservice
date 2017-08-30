package com.debitum.assets.application.investment;

import com.debitum.assets.domain.model.event.DomainEventPublisher;
import com.debitum.assets.domain.model.investment.*;
import com.debitum.assets.domain.model.investment.events.InvestmentCreated;
import com.debitum.assets.domain.model.investment.exception.InvestmentAlreadyPaidException;
import com.debitum.assets.port.adapter.helpers.HibernateHelper;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.debitum.assets.domain.model.investment.InvestmentStatus.PENDING;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

@Component
@Transactional
public class InvestmentApplication {

    private final InvestmentRepository investmentRepository;
    private final InvestmentEntryRepository investmentEntryRepository;
    private final InvoiceRepository invoiceRepository;
    private final CoinMarketService coinMarketService;
    private final DebtCollectorContractWallet debtCollectorContractWallet;

    public InvestmentApplication(InvestmentRepository investmentRepository,
                                 InvestmentEntryRepository investmentEntryRepository,
                                 InvoiceRepository invoiceRepository,
                                 CoinMarketService coinMarketService,
                                 DebtCollectorContractWallet debtCollectorContractWallet) {
        this.investmentRepository = investmentRepository;
        this.investmentEntryRepository = investmentEntryRepository;
        this.invoiceRepository = invoiceRepository;
        this.coinMarketService = coinMarketService;
        this.debtCollectorContractWallet = debtCollectorContractWallet;
    }

    public List<Investment> findAll() {
        return investmentRepository.findAll();
    }

    public List<Investment> findAll(UUID userId) {
        return investmentRepository.findAll(userId);
    }

    public List<Investment> findAll(List<UUID> investmentIds) {
        return investmentRepository.findAll(investmentIds);
    }


    public Map<UUID, Investment> investmentsMap(List<UUID> investmentIds) {
        List<Investment> investments = HibernateHelper.cast(investmentRepository.findAll(investmentIds));
        return investments.stream().collect(toMap(Investment::getId, identity()));
    }

    public List<InvestmentEntry> findInvestmentsEntries(UUID investmentId) {
        return HibernateHelper.cast(investmentEntryRepository.findAll(investmentId));
    }

    public Investment create(UUID userId) {
        Investment investment = new Investment(userId);
        return HibernateHelper.cast(investmentRepository.save(investment));
    }

    public int deleteNotPaidInvestments(Instant createdBefore) {
        return investmentRepository.deleteExpiredInvestments(createdBefore);
    }

    public void sendCoinForInvestment(Investment investment, String walletSource, String password) {
        if (investment.getStatus() != PENDING) {
            throw new InvestmentAlreadyPaidException();
        }
        debtCollectorContractWallet.sendCoinForInvestment(
                investment.getContractToken(),
                CoinPrice.convertEthAmountToRepresentative(investment.getTotalAmountEth()),
                walletSource,
                password
        );
    }

    public Investment markAsSentToBlockchain(String contractToken) {
        Investment investment = getByContractToken(contractToken);
        investment.markAsSentToBlockchain();
        return HibernateHelper.cast(investmentRepository.save(investment));
    }

    public Investment markAsPaid(String contractToken) {
        Investment investment = getByContractToken(contractToken);
        investment.markAsPaid();
        return HibernateHelper.cast(investmentRepository.save(investment));
    }

    public Investment get(UUID investmentId) {
        Investment investment = investmentRepository.get(investmentId);
        investment.getInvestments();
        return HibernateHelper.cast(investment);
    }

    public Investment getByContractToken(String contractToken) {
        Investment investment = investmentRepository.getByContractToken(contractToken);
        investment.getInvestments();
        return investment;
    }

    public List<InvestmentEntry> repayInvestmentEntriesOfInvoice(Invoice invoice) {
        List<InvestmentEntry> entries = investmentEntryRepository.getInvestmentEntriesByInvoiceId(invoice.getId());
        entries.stream().forEach(entry -> investmentEntryRepository.save(entry.repay(invoice)));
        return entries;
    }

    public List<InvestmentEntry> investmentEntriesOfInvoice(UUID invoiceId) {
        List<InvestmentEntry> entries = investmentEntryRepository.getInvestmentEntriesByInvoiceId(invoiceId);
        return entries;
    }

    public List<InvestmentEntry> investmentEntriesOfUser(UUID userId) {
        List<InvestmentEntry> entries = investmentEntryRepository.getInvestmentEntriesUfUser(userId);
        return entries;
    }

    public void execute(List<PersistInvestmentEntryCommand> commands) {
        if (isNotEmpty(commands)) {
            Investment investment = investmentRepository.get(commands.get(0).getInvestmentId());
            investment.resetAmount();

            CoinPrice coinPrice = coinMarketService.etherPriceInEuro();
            commands.forEach(command -> execute(command, investment, coinPrice));
            investment.declareAmountInEth(coinPrice);
            Investment updatedInvestment = HibernateHelper.cast(investmentRepository.save(investment));
            DomainEventPublisher.publish(new InvestmentCreated(updatedInvestment));
        }
    }

    private void execute(PersistInvestmentEntryCommand command, Investment investment, CoinPrice coinPrice) {
        InvestmentEntry entry;
        if (command.isNewEntry()) {
            entry = investment.makeInvestmentEntry(command.getInvoiceId(), command.getAmount(), coinPrice);
        } else {
            entry = HibernateHelper.cast(investmentEntryRepository.get(command.getEntryId()));
            entry.changeAmount(command.getAmount(), coinPrice);
        }
        Invoice invoice = HibernateHelper.cast(invoiceRepository.get(command.getInvoiceId()));
        invoice.addInvestmentAmount(command.getAmount());
        investment.addEntry(entry);
        invoiceRepository.save(invoice);
        investmentEntryRepository.save(entry);
    }


    public static class PersistInvestmentEntryCommand {
        private UUID entryId;

        private UUID invoiceId;

        private UUID investmentId;

        private Double amount;

        public PersistInvestmentEntryCommand(UUID entryId,
                                             UUID invoiceId,
                                             UUID investmentId,
                                             Double amount) {
            this.entryId = entryId;
            this.invoiceId = invoiceId;
            this.investmentId = investmentId;
            this.amount = amount;
        }

        public UUID getEntryId() {
            return entryId;
        }

        public UUID getInvoiceId() {
            return invoiceId;
        }

        public UUID getInvestmentId() {
            return investmentId;
        }

        public Double getAmount() {
            return amount;
        }

        public boolean isNewEntry() {
            return entryId == null;
        }
    }

}
