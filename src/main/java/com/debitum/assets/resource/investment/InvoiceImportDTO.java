package com.debitum.assets.resource.investment;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


@ApiModel(
        value = "InvoiceImportDTO",
        description = "Invoice resource resource"
)
public class InvoiceImportDTO {

    @ApiModelProperty(value = "Invoices to import")
    private List<InvoiceDTO> invoices;

    public List<InvoiceDTO> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<InvoiceDTO> invoices) {
        this.invoices = invoices;
    }
}
