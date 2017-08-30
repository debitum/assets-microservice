package com.debitum.assets.resource.settings;


import com.debitum.assets.domain.model.settings.Settings;
import io.swagger.annotations.ApiModelProperty;

public class SettingsDTO {

    @ApiModelProperty(value = "Investment smart contract address")
    private String investmentContractAddress;
    @ApiModelProperty(value = "Investment smart contract address ABI")
    private String investmentContractAbi;

    public String getInvestmentContractAddress() {
        return investmentContractAddress;
    }

    public void setInvestmentContractAddress(String investmentContractAddress) {
        this.investmentContractAddress = investmentContractAddress;
    }

    public String getInvestmentContractAbi() {
        return investmentContractAbi;
    }

    public void setInvestmentContractAbi(String investmentContractAbi) {
        this.investmentContractAbi = investmentContractAbi;
    }

    public static SettingsDTO from(Settings settings) {
        SettingsDTO dto = new SettingsDTO();
        dto.setInvestmentContractAbi(settings.getInvestmentContractAbi());
        dto.setInvestmentContractAddress(settings.getInvestmentContractAddress());
        return dto;
    }
}
