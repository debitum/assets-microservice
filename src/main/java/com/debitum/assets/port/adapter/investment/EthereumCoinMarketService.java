package com.debitum.assets.port.adapter.investment;

import com.debitum.assets.domain.model.investment.CoinMarketService;
import com.debitum.assets.domain.model.investment.CoinPrice;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Component;

@Component
class EthereumCoinMarketService implements CoinMarketService {

    private final CoinMarketRestClient coinMarketRestClient;

    public EthereumCoinMarketService(CoinMarketRestClient coinMarketRestClient) {
        this.coinMarketRestClient = coinMarketRestClient;
    }

    @Override
    @HystrixCommand(
            groupKey = "EthereumCoinMarketService",
            commandKey = "EthereumCoinMarketService.etherPriceInEuro")
    public CoinPrice etherPriceInEuro() {
        CoinMarketRestClient.CoinInfo prices = coinMarketRestClient.getPrices();

        return new CoinPrice(
                prices.symbol,
                prices.price.eur,
                prices.price.usd
        );
    }
}
