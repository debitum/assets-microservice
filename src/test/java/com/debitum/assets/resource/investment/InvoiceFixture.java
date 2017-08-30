package com.debitum.assets.resource.investment;


import com.debitum.assets.RandomValueHelper;

import java.time.Instant;

import static com.debitum.assets.domain.model.investment.LoanType.INVOICE_FINANCING;

public class InvoiceFixture {

    public static InvoiceDTO fixture() {
        InvoiceDTO dto = new InvoiceDTO();
        dto.setLoanType(INVOICE_FINANCING);
        dto.setLoanAmount(RandomValueHelper.randomDouble());
        dto.setInterestRate(RandomValueHelper.randomDouble());
        dto.setIssueDate(Instant.now());
        dto.setAvailableForInvestment(RandomValueHelper.randomDouble());
        dto.setOriginator(RandomValueHelper.randomString());
        dto.setLoanBalance(RandomValueHelper.randomDouble());
        dto.setTerm(RandomValueHelper.randomDate());
        dto.setListDate(RandomValueHelper.randomDate());
        dto.setBusinessSector(RandomValueHelper.randomString());
        dto.setDescription(RandomValueHelper.randomText());
        dto.setInvoiceTransaction(RandomValueHelper.randomString());
        dto.setCreditRank(RandomValueHelper.randomString());
        return dto;
    }
}
