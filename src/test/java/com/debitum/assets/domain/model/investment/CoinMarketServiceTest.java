package com.debitum.assets.domain.model.investment;


import com.debitum.assets.IntegrationTestBase;
import org.junit.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

public class CoinMarketServiceTest extends IntegrationTestBase {

    @Inject
    private CoinMarketService coinMarketService;

    @Test
    public void givenNothing_whenGettingEthPrice_thenCoinMarketReturnsOneEthPrices() {
        //given nothing

        //when
        CoinPrice coinPrice = coinMarketService.etherPriceInEuro();

        //then
        assertThat(coinPrice.getSymbol()).isNotNull();
        assertThat(coinPrice.getEur()).isNotNull();
        assertThat(coinPrice.getUsd()).isNotNull();
    }
}
