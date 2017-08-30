package com.debitum.assets.resource.wallet;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
        value = "WalletDTO",
        description = "Wallet resource"
)
public class WalletDTO {

    @ApiModelProperty(value = "Balance of smart contract")
    private Double contractBalanceEth;

    @ApiModelProperty(value = "Balance of owner wallet")
    private Double walletBalanceEth;

    public Double getContractBalanceEth() {
        return contractBalanceEth;
    }

    public void setContractBalanceEth(Double contractBalanceEth) {
        this.contractBalanceEth = contractBalanceEth;
    }

    public Double getWalletBalanceEth() {
        return walletBalanceEth;
    }

    public void setWalletBalanceEth(Double walletBalanceEth) {
        this.walletBalanceEth = walletBalanceEth;
    }

    public static WalletDTO from(Double contractBalance, Double walletBalance) {
        WalletDTO dto = new WalletDTO();
        dto.setContractBalanceEth(contractBalance);
        dto.setWalletBalanceEth(walletBalance);
        return dto;
    }
}
