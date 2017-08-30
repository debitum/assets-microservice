package com.debitum.assets.domain.model.investment;


import com.debitum.assets.UnitTestBase;
import com.debitum.assets.resource.investment.InvestmentEntryFixture;
import org.junit.Test;

import java.util.List;

import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;

public class InvestmentTest extends UnitTestBase{

    @Test
    public void givenExistingInvestment_whenDeclareAmounInEth_thenTotalAmountInEthIsSet(){
        //given
        CoinPrice eth = new CoinPrice("eth", 300d, 300d);
        Investment investment = new Investment(fromString("0dc6ced3-2f9c-440e-9ad5-012bbb49cb5a"));
        List<InvestmentEntry> investmentEntries = InvestmentEntryFixture.domainFixture(3003d, fromString("0dc6ced3-2f9c-440e-9ad5-012bbb49cb5a"), fromString("0dc6ced3-2f9c-440e-9ad5-012bbb49cb5a"));
        investment.addEntry(investmentEntries.get(0));

        //when
        investment.declareAmountInEth(eth);

        //then
        assertThat(investment.getTotalAmountEth()).isEqualTo(100100000000L);
        assertThat(CoinPrice.convertEthAmountToRepresentative(investment.getTotalAmountEth())).isEqualTo(10.01);
    }
}
