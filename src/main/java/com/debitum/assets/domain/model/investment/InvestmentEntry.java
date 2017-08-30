package com.debitum.assets.domain.model.investment;

import com.debitum.assets.domain.model.AuditedEntity;
import com.debitum.assets.domain.model.event.DomainEventPublisher;
import com.debitum.assets.domain.model.investment.events.InvestmentEntryCreated;
import com.debitum.assets.domain.model.investment.events.InvestmentEntryRepaid;
import org.apache.commons.lang.Validate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static com.debitum.assets.domain.model.investment.InvestmentEntryStatus.CURRENT;
import static com.debitum.assets.domain.model.investment.InvestmentEntryStatus.REPAID;
import static org.apache.commons.lang.ObjectUtils.defaultIfNull;

@Entity
@Table(name = "INVESTMENT_ENTRY")
public class InvestmentEntry extends AuditedEntity {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private UUID id;

    @Column(name = "INVOICE_ID")
    private UUID invoiceId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="INVOICE_ID", updatable = false, insertable = false)
    private Invoice invoice;

    @Column(name = "INVESTMENT_ID")
    private UUID investmentId;

    private Double amount;

    @Column(name = "AMOUNT_ETH")
    private Long amountEth;

    @Column(name = "REPAID_AMOUNT_ETH")
    private Long repaidAmountEth;

    @Enumerated(EnumType.STRING)
    private InvestmentEntryStatus status;

    InvestmentEntry() {
    }

    public InvestmentEntry(UUID invoiceId, UUID investmentId, Double amount, CoinPrice coinPrice) {
        this.invoiceId = invoiceId;
        this.investmentId = investmentId;
        this.amount = amount;
        declareAmountInEth(coinPrice);
        this.status = CURRENT;
        DomainEventPublisher.publish(new InvestmentEntryCreated(this));
    }

    public InvestmentEntry changeAmount(Double amount, CoinPrice coinPrice) {
        this.amount = amount;
        declareAmountInEth(coinPrice);
        return this;
    }

    private void declareAmountInEth(CoinPrice coinPrice) {
        this.amountEth = coinPrice.declareEthAmountForContract(amount);
    }

    public UUID getId() {
        return id;
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

    public Long getAmountEth() {
        return amountEth == null ? 0 : amountEth.longValue();
    }

    public Long getRepaidAmountEth() {
        return (long) defaultIfNull(repaidAmountEth, 0L);
    }

    public InvestmentEntryStatus getStatus() {
        return status;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public InvestmentEntry repay(Invoice invoice) {
        Validate.isTrue(this.status != REPAID, "Investment id=" + this.getId() + " is already repaid");
        this.status = REPAID;
        calculateRepay(invoice);
        DomainEventPublisher.publish(new InvestmentEntryRepaid(this));
        return this;
    }

    public Double neededEthForRepayment() {
        double advanceRate = invoice.getAdvanceRate() == null ? 0 : invoice.getAdvanceRate() / 100;
        double interestRate = invoice.getInterestRate() == null ? 0 : invoice.getInterestRate() / 100;
        long loanDurationInDays = Duration.between(getCreatedOn(), Instant.now()).toDays();
        return CoinPrice.convertEthAmountToRepresentative(getAmountEth() + (long) (getAmountEth() * loanDurationInDays * advanceRate * interestRate / 30));
    }

    private void calculateRepay(Invoice invoice) {
        double advanceRate = invoice.getAdvanceRate() == null ? 0 : invoice.getAdvanceRate() / 100;
        double interestRate = invoice.getInterestRate() == null ? 0 : invoice.getInterestRate() / 100;
        long loanDurationInDays = Duration.between(getCreatedOn(), Instant.now()).toDays();
        this.repaidAmountEth = getAmountEth() + (long) (getAmountEth() * loanDurationInDays * advanceRate * interestRate / 30);
    }
}
