package com.debitum.assets.domain.model.investment;


public interface DebtCollectorContractWallet {

    Double getContractBalanceEth();

    Double getWalletBalanceEth();

    void transferFromOwnerAccountToSmartContract(Double amount);

    void sendCoinForInvestment(String token, double amountEth, String walletSource, String password);
}
