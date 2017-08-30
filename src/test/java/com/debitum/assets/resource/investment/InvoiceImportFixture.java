package com.debitum.assets.resource.investment;


import java.util.ArrayList;
import java.util.List;

public class InvoiceImportFixture {

    public static InvoiceImportDTO fixture(int invoiceSize) {
        InvoiceImportDTO invoiceImport = new InvoiceImportDTO();
        List<InvoiceDTO> invoices = new ArrayList<>();
        invoiceImport.setInvoices(invoices);
        for (int i = 0; i < invoiceSize; i++) {
            invoices.add(InvoiceFixture.fixture());
        }
        return invoiceImport;
    }
}
