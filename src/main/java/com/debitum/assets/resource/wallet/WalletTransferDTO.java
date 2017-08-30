package com.debitum.assets.resource.wallet;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
        value = "WalletTransferDTO",
        description = "Wallet transfer resource"
)
public class WalletTransferDTO {

    @ApiModelProperty(value = "Amount of ehter to transfer from owner account to smart contract")
    private Double amount;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }


}
