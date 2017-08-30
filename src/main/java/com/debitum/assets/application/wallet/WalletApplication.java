package com.debitum.assets.application.wallet;

import com.debitum.assets.domain.model.investment.DebtCollectorContractWallet;
import org.springframework.stereotype.Component;

@Component
public class WalletApplication {

    private final DebtCollectorContractWallet debtCollectorContractWallet;

    public WalletApplication(DebtCollectorContractWallet debtCollectorContractWallet) {
        this.debtCollectorContractWallet = debtCollectorContractWallet;
    }

    public Double getContractBalance() {
        return debtCollectorContractWallet.getContractBalanceEth();
    }

    public Double getWalletBalance() {
        return debtCollectorContractWallet.getWalletBalanceEth();
    }

    public void transferFromOwnerAcountToSmartContract(Double amount){
        debtCollectorContractWallet.transferFromOwnerAccountToSmartContract(amount);
    }
}
