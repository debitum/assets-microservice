package com.debitum.assets.resource.investment;


import com.debitum.assets.domain.model.investment.CoinPrice;
import com.debitum.assets.domain.model.investment.InvestmentEntry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.Instant;
import java.util.UUID;


@ApiModel(
        value = "InvestmentEntryDTO",
        description = "Investment entry resource resource"
)
public class InvestmentEntryDTO {

    @ApiModelProperty(value = "Investment entry identifier")
    private UUID id;

    @ApiModelProperty(value = "Invoice identifier")
    private UUID invoiceId;

    @ApiModelProperty(value = "Investment identifier")
    private UUID investmentId;

    @ApiModelProperty(value = "Investment entry amount")
    private Double amount;

    @ApiModelProperty(value = "Investment entry amount in ether")
    private Double amountEth;

    @ApiModelProperty(value = "Investment entry amount in ether")
    private Double repaidAmountEth;

    @ApiModelProperty(value = "Last edit timestamp", readOnly = true)
    private Instant updatedOn;

    @ApiModelProperty(value = "Creation date", readOnly = true)
    private Instant createdOn;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(UUID invoiceId) {
        this.invoiceId = invoiceId;
    }

    public UUID getInvestmentId() {
        return investmentId;
    }

    public void setInvestmentId(UUID investmentId) {
        this.investmentId = investmentId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAmountEth() {
        return amountEth;
    }

    public void setAmountEth(Double amountEth) {
        this.amountEth = amountEth;
    }

    public Double getRepaidAmountEth() {
        return repaidAmountEth;
    }

    public void setRepaidAmountEth(Double repaidAmountEth) {
        this.repaidAmountEth = repaidAmountEth;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Instant getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    public static InvestmentEntryDTO from(InvestmentEntry investmentEntry) {
        InvestmentEntryDTO dto = new InvestmentEntryDTO();
        dto.setId(investmentEntry.getId());
        dto.setAmount(investmentEntry.getAmount());
        dto.setInvestmentId(investmentEntry.getInvestmentId());
        dto.setInvoiceId(investmentEntry.getInvoiceId());
        dto.setCreatedOn(investmentEntry.getCreatedOn());
        dto.setUpdatedOn(investmentEntry.getUpdatedOn());
        dto.setAmountEth(CoinPrice.convertEthAmountToRepresentative(investmentEntry.getAmountEth()));
        dto.setRepaidAmountEth(CoinPrice.convertEthAmountToRepresentative(investmentEntry.getRepaidAmountEth()));
        return dto;
    }
}
