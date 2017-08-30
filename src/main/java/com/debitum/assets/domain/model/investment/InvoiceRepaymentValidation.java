package com.debitum.assets.domain.model.investment;


import org.apache.commons.lang.ObjectUtils;

import static com.debitum.assets.domain.model.investment.RepaymentValidationStatus.BALANCE_NOT_ENOUGH;
import static com.debitum.assets.domain.model.investment.RepaymentValidationStatus.VALID;

public class InvoiceRepaymentValidation {

    private double balanceEth;
    private double invoiceRepaymentNeededEth;

    public InvoiceRepaymentValidation(Double balanceEth,
                                      Double invoiceRepaymentNeededEth) {

        this.balanceEth = (double) ObjectUtils.defaultIfNull(balanceEth, 0d);
        this.invoiceRepaymentNeededEth = (double) ObjectUtils.defaultIfNull(invoiceRepaymentNeededEth, 0d);
    }

    public RepaymentValidationStatus getStatus() {
        return balanceEth >= invoiceRepaymentNeededEth ? VALID : BALANCE_NOT_ENOUGH;
    }

    public Double getBalanceEth() {
        return balanceEth;
    }

    public Double getInvoiceRepaymentNeededEth() {
        return invoiceRepaymentNeededEth;
    }
}
