package com.debitum.assets.application.investment;


import com.debitum.assets.domain.model.investment.*;
import com.debitum.assets.port.adapter.helpers.HibernateHelper;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

@Component
@Transactional
public class InvoiceApplication {

    private final InvoiceRepository invoiceRepository;
    private final InvestmentEntryRepository investmentEntryRepository;
    private final DebtCollectorContractWallet debtCollectorContractWallet;

    public InvoiceApplication(InvoiceRepository invoiceRepository,
                              InvestmentEntryRepository investmentEntryRepository,
                              DebtCollectorContractWallet debtCollectorContractWallet) {
        this.invoiceRepository = invoiceRepository;
        this.investmentEntryRepository = investmentEntryRepository;
        this.debtCollectorContractWallet = debtCollectorContractWallet;
    }

    public List<Invoice> findAll() {
        return invoiceRepository.findAll();
    }

    public List<Invoice> findAvailableForInvestments() {
        return invoiceRepository.findAvailableForInvestments();
    }

    public Invoice get(UUID invoiceId) {
        return invoiceRepository.get(invoiceId);
    }

    public Invoice create(LoanType loanType,
                          Instant issueDate,
                          Double loanAmount,
                          Double advanceRate,
                          Double interestRate,
                          String originator,
                          Double loanBalance,
                          Date term,
                          Date listDate,
                          String businessSector,
                          String description,
                          String invoiceTransaction,
                          String creditRank) {
        Invoice invoice = new Invoice(
                loanType,
                issueDate,
                loanAmount,
                advanceRate,
                interestRate,
                originator,
                loanBalance,
                term,
                listDate,
                businessSector,
                description,
                invoiceTransaction,
                creditRank
        );

        return HibernateHelper.cast(invoiceRepository.save(invoice));
    }

    public void execute(List<PersistInvoiceCommand> commands) {
        if (isNotEmpty(commands)) {

            commands.forEach(command -> execute(command));
        }
    }

    public void repayInvoices(List<UUID> invoiceIds) {
        invoiceRepository.findAll(invoiceIds).stream()
                .forEach(invoice -> invoiceRepository.save(invoice.repay()));
    }

    public InvoiceRepaymentValidation invoiceRepaymentValidation(List<UUID> invoiceIds) {
        return invoiceRepaymentValidation(debtCollectorContractWallet.getContractBalanceEth(), invoiceIds);
    }

    public InvoiceRepaymentValidation invoiceRepaymentValidation(double contractBalance, List<UUID> invoiceIds) {
        double neededEth = investmentEntryRepository.getInvestmentEntriesByInvoiceIds(invoiceIds)
                .stream()
                .mapToDouble(InvestmentEntry::neededEthForRepayment).sum();

        return new InvoiceRepaymentValidation(contractBalance, neededEth);
    }

    private void execute(PersistInvoiceCommand command) {
        create(
                command.loanType,
                command.issueDate,
                command.loanAmount,
                command.advanceRate,
                command.interestRate,
                command.originator,
                command.loanBalance,
                command.term,
                command.listDate,
                command.businessSector,
                command.description,
                command.invoiceTransaction,
                command.creditRank
        );
    }

    public static class PersistInvoiceCommand {

        private LoanType loanType;

        private Instant issueDate;

        private Double loanAmount;

        private Double advanceRate;

        private Double interestRate;

        private Double availableForInvestment;

        private String originator;

        private Double loanBalance;

        private Date term;

        private Date listDate;

        private String businessSector;

        private String description;

        private String invoiceTransaction;

        private String creditRank;

        public PersistInvoiceCommand(LoanType loanType,
                                     Instant issueDate,
                                     Double loanAmount,
                                     Double advanceRate,
                                     Double interestRate,
                                     Double availableForInvestment,
                                     String originator,
                                     Double loanBalance,
                                     Date term,
                                     Date listDate,
                                     String businessSector,
                                     String description,
                                     String invoiceTransaction,
                                     String creditRank) {
            this.loanType = loanType;
            this.issueDate = issueDate;
            this.loanAmount = loanAmount;
            this.advanceRate = advanceRate;
            this.interestRate = interestRate;
            this.availableForInvestment = availableForInvestment;
            this.originator = originator;
            this.loanBalance = loanBalance;
            this.term = term;
            this.listDate = listDate;
            this.businessSector = businessSector;
            this.description = description;
            this.invoiceTransaction = invoiceTransaction;
            this.creditRank = creditRank;
        }
    }
}
