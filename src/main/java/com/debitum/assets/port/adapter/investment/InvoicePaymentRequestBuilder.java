package com.debitum.assets.port.adapter.investment;


import com.debitum.assets.application.investment.InvestmentApplication;
import com.debitum.assets.domain.model.investment.Investment;
import com.debitum.assets.domain.model.investment.InvestmentEntry;
import com.debitum.assets.domain.model.investment.Invoice;
import org.springframework.stereotype.Component;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Component
class InvoicePaymentRequestBuilder {

    private final InvestmentApplication investmentApplication;

    public InvoicePaymentRequestBuilder(InvestmentApplication investmentApplication) {
        this.investmentApplication = investmentApplication;
    }

    public ContractInvoicePaymentRequest constructInvoicePaymentRequestFor(Invoice invoice) {
        List<InvestmentEntry> investmentEntries = investmentApplication.repayInvestmentEntriesOfInvoice(invoice);
        List<UUID> investmentIds = investmentEntries.stream().map(entry -> entry.getInvestmentId()).collect(toList());
        Map<UUID, Investment> investments = investmentApplication.investmentsMap(investmentIds);

        String invoiceEntries = investmentEntries.stream().map(entry ->
                entry.getId().toString()
        ).collect(Collectors.joining(";"));

        List<Uint256> pays = investmentEntries.stream().map(entry ->
                new Uint256(entry.getAmountEth())
        ).collect(toList());

        String tokens = investmentEntries.stream().map(entry -> {
            Investment investment = investments.get(entry.getInvestmentId());
            return investment.getContractToken();
        }).collect(Collectors.joining(";"));

        return new ContractInvoicePaymentRequest(
                tokens,
                invoiceEntries,
                pays,
                investmentIds,
                investmentEntries.stream().map(InvestmentEntry::getId).collect(toList())
        );
    }


    public static final class ContractInvoicePaymentRequest {
        private final Utf8String tokens;

        private final Utf8String invoiceEntries;

        private final DynamicArray<Uint256> pays;

        private final List<UUID> investmentIds;
        
        private final List<UUID> investmentEntriesIds;

        private ContractInvoicePaymentRequest(String tokens,
                                              String invoiceEntries,
                                              List<Uint256> pays,
                                              List<UUID> investmentIds,
                                              List<UUID> investmentEntriesIds) {
            this.tokens = new Utf8String(tokens);
            this.invoiceEntries = new Utf8String(invoiceEntries);
            this.pays = new DynamicArray<>(pays);
            this.investmentIds = investmentIds;
            this.investmentEntriesIds = investmentEntriesIds;
        }

        public Utf8String getTokens() {
            return tokens;
        }

        public Utf8String getInvoiceEntries() {
            return invoiceEntries;
        }

        public DynamicArray<Uint256> getPays() {
            return pays;
        }

        public List<UUID> getInvestmentIds() {
            return investmentIds;
        }

        public List<UUID> getInvestmentEntriesIds() {
            return investmentEntriesIds;
        }
    }
}
