package com.debitum.assets.application.settings;


import com.debitum.assets.domain.model.settings.Settings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class SettingsApplication {
    private final String investmentContractAddress;
    private final String investmentContractAbi;

    public SettingsApplication(@Value("${debitum.contract.investments.address}") String investmentContractAddress,
                               @Value("${debitum.contract.investments.abi}") String investmentContractAbi) {
        this.investmentContractAddress = investmentContractAddress;
        this.investmentContractAbi = investmentContractAbi;
    }

    public Settings getSettings() {
        return new Settings(investmentContractAddress, investmentContractAbi);
    }
}
