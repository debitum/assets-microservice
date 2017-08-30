package com.debitum.assets.port.adapter.watcher.contracts;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ManagedTransaction;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

@Configuration
class EthereumMonitorConfiguration {
    private final static Logger LOG = LoggerFactory.getLogger(EthereumMonitorConfiguration.class);

    private final Web3j web3;
    private DebtCoverageCollector debtCoverageCollector;


    public EthereumMonitorConfiguration(@Value("${debitum.fullNodeURL}") String ethereumFullNodeUrl,
                                        @Value("${debitum.contract.debtCollector.address}") String debtCollectorContractAddress,
                                        @Value("${debitum.wallet.path}") String walletFile,
                                        @Value("${debitum.wallet.password}") String password,
                                        @Value("${debitum.wallet.useClassPath:false}") boolean useClasspath) {
        RequestConfig config = RequestConfig.custom()

                .setSocketTimeout(600000)
                .setConnectTimeout(600000)
                .build();
        web3 = Web3j.build(
                new HttpService(
                        ethereumFullNodeUrl,
                        HttpClients.custom().setConnectionManagerShared(true).setDefaultRequestConfig(config).build()
                )
        );


        //web3 = Web3j.build(new HttpService(ethereumFullNodeUrl));

        try {
            File wallet;
            if (useClasspath) {
                wallet = new ClassPathResource(walletFile).getFile();
            } else {
                wallet = new File(walletFile);
            }
            Credentials credentials = WalletUtils.loadCredentials(password, wallet);
            this.debtCoverageCollector = DebtCoverageCollector.load(
                    debtCollectorContractAddress,
                    web3, credentials,
                    ManagedTransaction.GAS_PRICE,
                    BigInteger.valueOf(4_700_000)
            );
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
        }
    }


    @Bean
    public DebtCoverageCollector DebtCoverageCollector() {
        return debtCoverageCollector;
    }

    @Bean
    public Web3j web3j(){
        return web3;
    }


}
