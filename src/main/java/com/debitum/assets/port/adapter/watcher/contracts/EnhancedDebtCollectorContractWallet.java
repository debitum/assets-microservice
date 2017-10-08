package com.debitum.assets.port.adapter.watcher.contracts;


import com.debitum.assets.domain.model.investment.DebtCollectorContractWallet;
import com.debitum.assets.domain.model.investment.exception.InvalidWalletCredentialsException;
import com.debitum.assets.domain.model.investment.exception.WalletBalanceNotEnoughException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.tx.ManagedTransaction;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

@Component
class EnhancedDebtCollectorContractWallet implements DebtCollectorContractWallet {
    private final static Logger LOG = LoggerFactory.getLogger(EnhancedDebtCollectorContractWallet.class);

    private final DebtCoverageCollector debtCoverageCollector;
    private final Web3j web3;
    private final String walletAddress;
    private final String debtCollectorContractAddress;

    public EnhancedDebtCollectorContractWallet(DebtCoverageCollector debtCoverageCollector,
                                               @Value("${debitum.wallet.address}") String walletAddress,
                                               @Value("${debitum.contract.debtCollector.address}") String debtCollectorContractAddress,
                                               Web3j web3) {
        this.debtCoverageCollector = debtCoverageCollector;
        this.web3 = web3;
        this.walletAddress = walletAddress;
        this.debtCollectorContractAddress = debtCollectorContractAddress;
    }

    @Override
    public Double getContractBalanceEth() {
        double balance = 0;
        try {
            balance = (debtCoverageCollector.getBalance().get().getValue().doubleValue() / 1000000000000000000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return balance;
    }

    @Override
    public Double getWalletBalanceEth() {
        try {
            EthGetBalance ethBalance = web3
                    .ethGetBalance(walletAddress, DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();
            return ethBalance.getBalance().doubleValue() / 1000000000000000000L;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    @Override
    public void transferFromOwnerAccountToSmartContract(Double amount) {
        Validate.isTrue(amount > 0, "Amount must to be greater than 0.");
        new Thread(() -> {
            try {
                debtCoverageCollector.AddEth(BigInteger.valueOf((long) (amount * 1000000000000000000L))).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        ).start();
    }

    private double getBalanceOfClient(WalletFile walletFile){
        EthGetBalance ethBalance = null;
        try {
            ethBalance = web3
                    .ethGetBalance(Numeric.prependHexPrefix(walletFile.getAddress()), DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();
            return ethBalance.getBalance().doubleValue() / 1000000000000000000L;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return 0.0;
    }

    @Override
    public void sendCoinForInvestment(String token, double amountEth, String walletSource, String password) {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
            WalletFile walletFile = null;
            try {
                walletFile = objectMapper.readValue(walletSource, WalletFile.class);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Credentials credentials = Credentials.create(Wallet.decrypt(password, walletFile));

            double balanceOfClient = getBalanceOfClient(walletFile);
            if(balanceOfClient < amountEth){
                throw new WalletBalanceNotEnoughException();
            }

            DebtCoverageCollector newDebtCoverageCollector = DebtCoverageCollector.load(
                    debtCollectorContractAddress,
                    web3, credentials,
                    ManagedTransaction.GAS_PRICE,
                    BigInteger.valueOf(4_700_000)
            );
            final Address clientAddress = new Address(Numeric.prependHexPrefix(walletFile.getAddress()));
            new Thread(() -> {
                try {
                    newDebtCoverageCollector.sendCoin(new Utf8String(token), clientAddress, BigInteger.valueOf(((long) (amountEth * 1000000000000000000L)))).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            ).start();
        } catch (CipherException e) {
            LOG.error(e.getMessage());
            throw new InvalidWalletCredentialsException();
        }
    }
}
