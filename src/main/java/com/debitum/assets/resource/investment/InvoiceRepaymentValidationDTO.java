package com.debitum.assets.resource.investment;


import com.debitum.assets.domain.model.investment.InvoiceRepaymentValidation;
import com.debitum.assets.domain.model.investment.RepaymentValidationStatus;

public class InvoiceRepaymentValidationDTO {

    private double balanceEth;
    private double invoiceRepaymentNeededEth;

    private RepaymentValidationStatus status;


    public double getBalanceEth() {
        return balanceEth;
    }

    public void setBalanceEth(double balanceEth) {
        this.balanceEth = balanceEth;
    }

    public double getInvoiceRepaymentNeededEth() {
        return invoiceRepaymentNeededEth;
    }

    public void setInvoiceRepaymentNeededEth(double invoiceRepaymentNeededEth) {
        this.invoiceRepaymentNeededEth = invoiceRepaymentNeededEth;
    }

    public RepaymentValidationStatus getStatus() {
        return status;
    }

    public void setStatus(RepaymentValidationStatus status) {
        this.status = status;
    }

    public static InvoiceRepaymentValidationDTO from(InvoiceRepaymentValidation domain) {
        InvoiceRepaymentValidationDTO dto = new InvoiceRepaymentValidationDTO();
        dto.setBalanceEth(domain.getBalanceEth());
        dto.setInvoiceRepaymentNeededEth(domain.getInvoiceRepaymentNeededEth());
        dto.setStatus(domain.getStatus());
        return dto;
    }
}
