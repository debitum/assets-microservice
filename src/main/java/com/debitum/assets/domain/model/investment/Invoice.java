package com.debitum.assets.domain.model.investment;


import com.debitum.assets.domain.model.AuditedEntity;
import com.debitum.assets.domain.model.event.DomainEventPublisher;
import com.debitum.assets.domain.model.investment.events.InvoiceRepaid;
import com.debitum.assets.domain.model.investment.exception.InvoiceAlreadyRepaidException;
import com.debitum.assets.domain.model.investment.exception.InvoiceAvailableForInvestmentAmountExceededException;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static com.debitum.assets.domain.model.investment.InvoiceStatus.CURRENT;
import static com.debitum.assets.domain.model.investment.InvoiceStatus.REPAID;

@Entity
@Table(name = "INVOICE")
public class Invoice extends AuditedEntity {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private UUID id;

    @Column(name = "LOAN_TYPE")
    @Enumerated(EnumType.STRING)
    private LoanType loanType;

    @Column(name = "ISSUE_DATE_UTC")
    private Instant issueDate;

    @Column(name = "LOAN_AMOUNT")
    private Double loanAmount;

    @Column(name = "ADVANCE_RATE")
    private Double advanceRate;

    @Column(name = "INTEREST_RATE")
    private Double interestRate;

    @Column(name = "AVAILABLE_FOR_INVESTMENT")
    private Double availableForInvestment;

    @Column(name = "ORIGINATOR")
    private String originator;

    @Column(name = "LOAN_BALANCE")
    private Double loanBalance;

    @Column(name = "TERM")
    private Date term;

    @Column(name = "LIST_DATE")
    private Date listDate;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    @Column(name = "BUSINESS_SECTOR")
    private String businessSector;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "INVOICE_TRANSACTION")
    private String invoiceTransaction;

    @Column(name = "CREDIT_RANK")
    private String creditRank;

    Invoice() {
    }

    public Invoice(LoanType loanType,
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
        this.loanType = loanType;
        this.issueDate = issueDate;
        this.loanAmount = loanAmount;
        this.advanceRate = advanceRate;
        this.interestRate = interestRate;
        //this.availableForInvestment = availableForInvestment;
        this.availableForInvestment = loanAmount;
        this.originator = originator;
        this.loanBalance = loanBalance;
        this.term = term;
        this.listDate = listDate;
        this.businessSector = businessSector;
        this.description = description;
        this.invoiceTransaction = invoiceTransaction;
        this.creditRank = creditRank;
        this.status = CURRENT;
    }

    public UUID getId() {
        return id;
    }

    public LoanType getLoanType() {
        return loanType;
    }

    public Instant getIssueDate() {
        return issueDate;
    }


    public Double getLoanAmount() {
        return loanAmount;
    }

    public Double getAdvanceRate() {
        return advanceRate;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public Double getAvailableForInvestment() {
        return availableForInvestment;
    }

    public String getOriginator() {
        return originator;
    }

    public Double getLoanBalance() {
        return loanBalance;
    }

    public Date getTerm() {
        return term;
    }

    public Date getListDate() {
        return listDate;
    }

    public String getBusinessSector() {
        return businessSector;
    }

    public String getDescription() {
        return description;
    }

    public String getInvoiceTransaction() {
        return invoiceTransaction;
    }

    public String getCreditRank() {
        return creditRank;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public Invoice repay() {
        if (this.status == REPAID) {
            throw new InvoiceAlreadyRepaidException(this);
        }
        this.status = REPAID;
        DomainEventPublisher.publish(new InvoiceRepaid(this));
        return this;
    }

    public Invoice addInvestmentAmount(double investmentAmount) {
        if (this.availableForInvestment == null || this.availableForInvestment == 0d) {
            this.availableForInvestment = loanAmount;
        }
        if (investmentAmount > this.availableForInvestment) {
            throw new InvoiceAvailableForInvestmentAmountExceededException(this.getId());
        }
        this.availableForInvestment -= investmentAmount;
        return this;
    }
}
