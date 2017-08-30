package com.debitum.assets.port.adapter.watcher.contracts;

import com.debitum.assets.application.investment.InvestmentApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.DefaultBlockParameterName;

@Component
@Profile(value = "!" + AssetsApplication.TEST_PROFILE)
class EthereumContractEventListener {
    private final static Logger LOG = LoggerFactory.getLogger(EthereumContractEventListener.class);

    private final DebtCoverageCollector debtCoverageCollector;
    private final InvestmentApplication investmentApplication;

    public EthereumContractEventListener(DebtCoverageCollector debtCoverageCollector,
                                         InvestmentApplication investmentApplication) {
        this.debtCoverageCollector = debtCoverageCollector;
        this.investmentApplication = investmentApplication;
        onContractAdded();
        onContractPaid();
    }


    public void onContractAdded() {
        new Thread(() ->
                debtCoverageCollector.contractAddedEventObservable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST).subscribe(
                        contractAddedEventResponse -> {
                            LOG.info("Contract added token = " + contractAddedEventResponse.token.toString() + " value = " + contractAddedEventResponse.value.getValue().toString());
                            investmentApplication.markAsSentToBlockchain(contractAddedEventResponse.token.toString());
                        })
        ).start();
    }


    public void onContractPaid() {
        new Thread(() ->
                debtCoverageCollector.contractPaidEventObservable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST).subscribe(
                        contractPaidEventResponse -> {
                            LOG.info("Contract paid token = " + contractPaidEventResponse.token.toString() + " amount = " + contractPaidEventResponse.ammount.getValue().toString() + " sender = " + contractPaidEventResponse.sender.toString());
                            investmentApplication.markAsPaid(contractPaidEventResponse.token.toString());
                        })
        ).start();
    }
}
