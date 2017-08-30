package com.debitum.assets.port.adapter.watcher.contracts;


import com.debitum.assets.application.investment.InvestmentApplication;
import com.debitum.assets.domain.model.investment.Investment;
import com.debitum.assets.domain.model.watcher.InvestmentPublishingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Service responsible for publishing investment
 * contract info to ethereum blockchain Smart Contract.
 */
@Component
class EthereumInvestmentPublishingService implements InvestmentPublishingService {

    private final InvestmentApplication investmentApplication;
    private final DebtCoverageCollector debtCoverageCollector;


    public EthereumInvestmentPublishingService(InvestmentApplication investmentApplication,
                                               DebtCoverageCollector debtCoverageCollector) {
        this.investmentApplication = investmentApplication;
        this.debtCoverageCollector = debtCoverageCollector;
    }

    @Override
    public ContractAddInvestmentRequest publishInvestment(UUID investmentId){
        Investment investment = investmentApplication.get(investmentId);
        ContractAddInvestmentRequest addInvestmentRequest = null;
        try {
            addInvestmentRequest = new ContractAddInvestmentRequest(investment);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        debtCoverageCollector.addContract(
                addInvestmentRequest.getContractToken(),
                addInvestmentRequest.getInvestmentMeta(),
                addInvestmentRequest.getValue()
        );
        return addInvestmentRequest;
    }
}
