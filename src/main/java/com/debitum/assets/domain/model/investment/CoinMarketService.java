package com.debitum.assets.domain.model.investment;


/**
 * Coin market service for getting crypto coin rates.
 */
public interface CoinMarketService {

    /**
     * Ether prices.
     *
     * @return the ether price
     */
    CoinPrice etherPriceInEuro();
}
