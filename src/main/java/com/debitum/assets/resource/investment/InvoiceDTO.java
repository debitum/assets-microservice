package com.debitum.assets.resource.investment;


import com.debitum.assets.domain.model.investment.Invoice;
import com.debitum.assets.domain.model.investment.InvoiceStatus;
import com.debitum.assets.domain.model.investment.LoanType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;


@ApiModel(
        value = "InvoiceDTO",
        description = "Invoice resource resource"
)
public class InvoiceDTO {

    @ApiModelProperty(value = "Invoice identifier")
    private UUID id;

    @ApiModelProperty(value = "Loan type")
    private LoanType loanType;

    @ApiModelProperty(value = "Issue date")
    private Instant issueDate;

    @ApiModelProperty(value = "Loan amount")
    private Double loanAmount;

    @ApiModelProperty(value = "Advance interest rate")
    private Double advanceRate;

    @ApiModelProperty(value = "Interest rate")
    private Double interestRate;

    @ApiModelProperty(value = "Available for investment")
    private Double availableForInvestment;

    @ApiModelProperty(value = "Last edit timestamp", readOnly = true)
    private Instant updatedOn;

    @ApiModelProperty(value = "Creation date", readOnly = true)
    private Instant createdOn;

    @ApiModelProperty(value = "Originator")
    private String originator;

    @ApiModelProperty(value = "Loan balance")
    private Double loanBalance;

    @ApiModelProperty(value = "Term")
    private Date term;

    @ApiModelProperty(value = "List / registration date")
    private Date listDate;

    @ApiModelProperty(value = "Business sector")
    private String businessSector;

    @ApiModelProperty(value = "Description")
    private String description;

    @ApiModelProperty(value = "Invoice transaction")
    private String invoiceTransaction;

    @ApiModelProperty(value = "Credit rank")
    private String creditRank;

    @ApiModelProperty(value = "Invoice status")
    private InvoiceStatus status;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LoanType getLoanType() {
        return loanType;
    }

    public void setLoanType(LoanType loanType) {
        this.loanType = loanType;
    }

    public Instant getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Instant issueDate) {
        this.issueDate = issueDate;
    }

    public Double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Double getAdvanceRate() {
        return advanceRate;
    }

    public void setAdvanceRate(Double advanceRate) {
        this.advanceRate = advanceRate;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Double getAvailableForInvestment() {
        return availableForInvestment;
    }

    public void setAvailableForInvestment(Double availableForInvestment) {
        this.availableForInvestment = availableForInvestment;
    }

    public Instant getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public String getOriginator() {
        return originator;
    }

    public void setOriginator(String originator) {
        this.originator = originator;
    }

    public Double getLoanBalance() {
        return loanBalance;
    }

    public void setLoanBalance(Double loanBalance) {
        this.loanBalance = loanBalance;
    }

    public Date getTerm() {
        return term;
    }

    public void setTerm(Date term) {
        this.term = term;
    }

    public Date getListDate() {
        return listDate;
    }

    public void setListDate(Date listDate) {
        this.listDate = listDate;
    }

    public String getBusinessSector() {
        return businessSector;
    }

    public void setBusinessSector(String businessSector) {
        this.businessSector = businessSector;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInvoiceTransaction() {
        return invoiceTransaction;
    }

    public void setInvoiceTransaction(String invoiceTransaction) {
        this.invoiceTransaction = invoiceTransaction;
    }

    public String getCreditRank() {
        return creditRank;
    }

    public void setCreditRank(String creditRank) {
        this.creditRank = creditRank;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public static InvoiceDTO from(Invoice invoice) {
        InvoiceDTO dto = new InvoiceDTO();
        dto.setId(invoice.getId());
        dto.setAvailableForInvestment(invoice.getAvailableForInvestment());
        dto.setAdvanceRate(invoice.getAdvanceRate());
        dto.setInterestRate(invoice.getInterestRate());
        dto.setIssueDate(invoice.getIssueDate());
        dto.setLoanType(invoice.getLoanType());
        dto.setLoanAmount(invoice.getLoanAmount());
        dto.setCreatedOn(invoice.getCreatedOn());
        dto.setUpdatedOn(invoice.getUpdatedOn());
        dto.setOriginator(invoice.getOriginator());
        dto.setLoanBalance(invoice.getLoanBalance());
        dto.setTerm(invoice.getTerm());
        dto.setListDate(invoice.getListDate());
        dto.setBusinessSector(invoice.getBusinessSector());
        dto.setDescription(invoice.getDescription());
        dto.setInvoiceTransaction(invoice.getInvoiceTransaction());
        dto.setCreditRank(invoice.getCreditRank());
        dto.setStatus(invoice.getStatus());
        return dto;
    }
}
