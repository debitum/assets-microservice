package com.debitum.assets.port.adapter.investment;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "coin-market", url = "https://coinmarketcap-nexuist.rhcloud.com")
interface CoinMarketRestClient {

    @RequestMapping(
            value = "/api/eth",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    CoinInfo getPrices();


    @JsonIgnoreProperties(ignoreUnknown = true)
    class CoinInfo {
        public String symbol;
        public String name;
        public Price price;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class Price {
        public double eur;
        public double usd;
    }
}
