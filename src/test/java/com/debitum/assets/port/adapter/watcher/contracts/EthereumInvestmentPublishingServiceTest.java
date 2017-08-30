package com.debitum.assets.port.adapter.watcher.contracts;


import com.debitum.assets.UnitTestBase;
import com.debitum.assets.application.investment.InvestmentApplication;
import com.debitum.assets.domain.model.investment.CoinPrice;
import com.debitum.assets.domain.model.investment.Investment;
import com.debitum.assets.domain.model.investment.InvestmentEntry;
import com.debitum.assets.domain.model.watcher.InvestmentPublishingService;
import com.debitum.assets.resource.investment.InvestmentEntryFixture;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.web3j.abi.datatypes.generated.Uint256;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class EthereumInvestmentPublishingServiceTest extends UnitTestBase {

    private EthereumInvestmentPublishingService publishingService;
    private final InvestmentApplication investmentApplication = mock(InvestmentApplication.class);
    private final DebtCoverageCollector debtCoverageCollector = mock(DebtCoverageCollector.class);
    private final UUID EXISTING_INVESTMENT_ID = fromString("0dc6ced3-2f9c-440e-9ad5-012bbb49cb5a");
    private final Investment investment = new Investment(UUID.fromString("0dc6ced3-2f9c-440e-9ad5-012bbb49cb5a"));


    @Before
    public void setUp() throws Exception {
        List<InvestmentEntry> entries = InvestmentEntryFixture.domainFixture(
                EXISTING_INVESTMENT_ID,
                fromString("0dc6ced3-2f9c-440e-9ad5-012bbb49cb5a"),
                fromString("e163b535-ff79-4f20-ad9b-747b5bf3c6cc")
        );
        investment.addEntry(entries.get(0));
        investment.addEntry(entries.get(1));

        investment.declareAmountInEth(new CoinPrice("eth", 300d, 301d));

        when(investmentApplication.get(EXISTING_INVESTMENT_ID)).thenReturn(investment);

        publishingService = new EthereumInvestmentPublishingService(
                investmentApplication,
                debtCoverageCollector
        );
    }

    @Test
    public void givenExistingInvestment_whenPublishingInvestmentToBlockchain_thenInvestmentDataSaveToBlockchain() throws IOException {
        //when
        InvestmentPublishingService.ContractAddInvestmentRequest addInvestmentRequest = publishingService.publishInvestment(EXISTING_INVESTMENT_ID);

        //then
        assertThat(addInvestmentRequest.getContractToken().toString()).isEqualTo(investment.getContractToken());
        assertThat(addInvestmentRequest.getValue()).isEqualTo(new Uint256((investment.getTotalAmountEth())));
        assertThat(addInvestmentRequest.getInvestmentMeta().toString()).isEqualTo(new ObjectMapper().writeValueAsString(investment));
        verify(
                debtCoverageCollector,
                times(1)
        ).addContract(
                eq(addInvestmentRequest.getContractToken()),
                eq(addInvestmentRequest.getInvestmentMeta()),
                eq(addInvestmentRequest.getValue())
        );
    }
}
