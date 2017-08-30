package com.debitum.assets.domain.model.settings;


public class Settings {

    private final String investmentContractAddress;
    private final String investmentContractAbi;

    public Settings(String investmentContractAddress,
                    String investmentContractAbi) {
        this.investmentContractAddress = investmentContractAddress;
        this.investmentContractAbi = investmentContractAbi;
    }

    public String getInvestmentContractAddress() {
        return investmentContractAddress;
    }

    public String getInvestmentContractAbi() {
        return investmentContractAbi;
    }
}
