package com.debitum.assets.resource.investment;

import io.swagger.annotations.ApiModel;

@ApiModel(
        value = "InvoiceRepaymentDTO",
        description = "Invoice repayment resource"
)
public class WalletPaymentDTO {

    private String walletSource;

    private String password;

    public String getWalletSource() {
        return walletSource;
    }

    public void setWalletSource(String walletSource) {
        this.walletSource = walletSource;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
