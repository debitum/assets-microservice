package com.debitum.assets.port.adapter.investment;

import com.debitum.assets.domain.model.investment.CoinMarketService;
import com.debitum.assets.domain.model.investment.CoinPrice;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.stereotype.Component;

@Component
class EthereumCoinMarketService implements CoinMarketService {

    private final CoinMarketRestClient coinMarketRestClient;
    private CoinPrice lastCoinPrice;

    public EthereumCoinMarketService(CoinMarketRestClient coinMarketRestClient) {
        this.coinMarketRestClient = coinMarketRestClient;
    }

    @Override
    @HystrixCommand(
            groupKey = "EthereumCoinMarketService",
            commandKey = "EthereumCoinMarketService.etherPriceInEuro",
            fallbackMethod = "etherPriceInEuroFallback")
    public CoinPrice etherPriceInEuro() {
        CoinMarketRestClient.CoinInfo prices = coinMarketRestClient.getPrices();
        lastCoinPrice = new CoinPrice(
                "ETH",
                prices.ETH.EUR,
                prices.ETH.USD
        );
        return lastCoinPrice;
    }

    public CoinPrice etherPriceInEuroFallback() {
        return (CoinPrice) ObjectUtils.defaultIfNull(
                lastCoinPrice,
                new CoinPrice(
                        "ETH",
                        300d,
                        300d
                )
        );
    }
}
