package com.debitum.assets.domain.model.investment;


import com.debitum.assets.UnitTestBase;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CoinPriceTest extends UnitTestBase{

    @Test
    public void givenEtherPrice_whenConvertingEther_thenPriceConvertedToLongDelimeter(){
        //given
        CoinPrice coinPrice = new CoinPrice("eth", 300d, 300d);
        double ether = 1.2132885456;

        //when
        long ethAmountForContract = coinPrice.declareEthAmountForContract(ether * 300);

        //then
        assertThat(CoinPrice.convertEthAmountToRepresentative(ethAmountForContract)).isEqualTo(1.213);
    }
}
