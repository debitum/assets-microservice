package com.debitum.assets.domain.model.investment;


public class CoinPrice {

    private final static long FRACTIONAL_DELIMITERS = 10000;
    private final static long INTEGER_DELIMITERS = 1000000;

    private String symbol;
    private Double eur;
    private Double usd;

    public CoinPrice(String symbol, Double eur, Double usd) {
        this.symbol = symbol;
        this.eur = eur;
        this.usd = usd;
    }

    public String getSymbol() {
        return symbol;
    }

    public Double getEur() {
        return eur;
    }

    public Double getUsd() {
        return usd;
    }

    public long declareEthAmountForContract(double amountEur) {
        long amount = (long) ((amountEur / eur) * FRACTIONAL_DELIMITERS * INTEGER_DELIMITERS);
        return amount - (amount  % 10000000) ;
    }

    public static double convertEthAmountToRepresentative(long eth) {
        return (double) (eth / FRACTIONAL_DELIMITERS) / INTEGER_DELIMITERS;
    }
}
