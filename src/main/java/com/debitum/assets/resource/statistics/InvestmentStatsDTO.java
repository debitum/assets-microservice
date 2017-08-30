package com.debitum.assets.resource.statistics;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
        value = "InvestmentStatsDTO",
        description = "Invoice investment statistics resource"
)
public class InvestmentStatsDTO {
    @ApiModelProperty(value = "Investor")
    private String investor;

    @ApiModelProperty(value = "Investment amount")
    private Double amount;

    public String getInvestor() {
        return investor;
    }

    public void setInvestor(String investor) {
        this.investor = investor;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public static InvestmentStatsDTO from(String investor, double amount) {
        InvestmentStatsDTO dto = new InvestmentStatsDTO();
        dto.setInvestor(investor);
        dto.setAmount(amount);
        return dto;
    }
}
