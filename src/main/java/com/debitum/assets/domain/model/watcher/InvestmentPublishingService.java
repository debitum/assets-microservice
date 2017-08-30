package com.debitum.assets.domain.model.watcher;


import com.debitum.assets.domain.model.investment.Investment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;

import java.util.UUID;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public interface InvestmentPublishingService {

    ContractAddInvestmentRequest publishInvestment(UUID investmentId);

    class ContractAddInvestmentRequest {
        private Utf8String contractToken;
        private Utf8String investmentMeta;
        private Uint256 value;
        private static final ObjectMapper objectMapper = new ObjectMapper();

        public ContractAddInvestmentRequest(Investment investment) throws JsonProcessingException {
            this.contractToken = new Utf8String(investment.getContractToken());
            this.investmentMeta = new Utf8String(objectMapper.writeValueAsString(investment));
            this.value = new Uint256(investment.getTotalAmountEth());
        }

        public Utf8String getContractToken() {
            return contractToken;
        }

        public Utf8String getInvestmentMeta() {
            return investmentMeta;
        }

        public Uint256 getValue() {
            return value;
        }
    }
}
