package com.debitum.assets.resource.investment;


import com.debitum.assets.RandomValueHelper;
import com.debitum.assets.domain.model.investment.CoinPrice;
import com.debitum.assets.domain.model.investment.InvestmentEntry;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InvestmentEntryFixture {

    public static List<InvestmentEntryDTO> fixture(UUID investmentId, UUID... invoices) {
        List<InvestmentEntryDTO> entries = new ArrayList<>();
        for (int i = 0; i < invoices.length; i++) {
            InvestmentEntryDTO entry = new InvestmentEntryDTO();
            entry.setInvestmentId(investmentId);
            entry.setAmount(RandomValueHelper.randomInvestmentAmount());
            entry.setInvoiceId(invoices[i]);
            entries.add(entry);
        }
        return entries;
    }

    public static List<InvestmentEntry> domainFixture(UUID investmentId, UUID... invoices) {
        List<InvestmentEntry> entries = new ArrayList<>();
        for (int i = 0; i < invoices.length; i++) {
            InvestmentEntry entry = new InvestmentEntry(
                    invoices[i],
                    investmentId,
                    RandomValueHelper.randomDouble(),
                    new CoinPrice("eth", 300d, 3001d)
            );
            entries.add(entry);
        }
        return entries;
    }

    public static List<InvestmentEntry> domainFixture(Double amount, UUID investmentId, UUID... invoices) {
        List<InvestmentEntry> entries = new ArrayList<>();
        for (int i = 0; i < invoices.length; i++) {
            InvestmentEntry entry = new InvestmentEntry(
                    invoices[i],
                    investmentId,
                    amount,
                    new CoinPrice("eth", 300d, 3001d)
            );
            entries.add(entry);
        }
        return entries;
    }

    public static List<InvestmentEntry> domainFixture(Double amount, Instant _createdOn, UUID investmentId, UUID... invoices) {
        List<InvestmentEntry> entries = new ArrayList<>();
        for (int i = 0; i < invoices.length; i++) {
            InvestmentEntry entry = new InvestmentEntry(
                    invoices[i],
                    investmentId,
                    amount,
                    new CoinPrice("eth", 300d, 3001d)
            ){
                @Override
                public Instant getCreatedOn() {
                    return _createdOn;
                }
            };
            entries.add(entry);
        }
        return entries;
    }

}
