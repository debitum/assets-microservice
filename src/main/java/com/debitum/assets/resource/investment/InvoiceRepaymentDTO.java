package com.debitum.assets.resource.investment;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.UUID;


@ApiModel(
        value = "InvoiceRepaymentDTO",
        description = "Invoice repayment resource"
)
public class InvoiceRepaymentDTO {

    @ApiModelProperty(value = "Invoice identifiers")
    List<UUID> invoices;

    public List<UUID> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<UUID> invoices) {
        this.invoices = invoices;
    }
}
