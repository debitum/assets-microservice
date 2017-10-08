package com.debitum.assets.resource.dashboard;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.time.DateUtils;

import java.time.Instant;
import java.util.*;


@ApiModel(
        value = "InvestmentStatisticsDTO",
        description = "All user investment statistics resource"
)
public class PaymentStatisticsDTO {

    @ApiModelProperty(value = "Amount in ether")
    private List<Double> amountEthValues = new ArrayList<>();

    @ApiModelProperty(value = "Creation date", readOnly = true)
    private List<Date> createdOnKeys = new ArrayList<>();

    @JsonIgnoreProperties
    private Map<Date, Double> investmentGraph = new HashMap<>();

    public PaymentStatisticsDTO() {
    }

    PaymentStatisticsDTO add(Double amountEth, Instant createdOn) {
        Date date = DateUtils.truncate(Date.from(createdOn), Calendar.DATE);;
        double totalAmountEth;

        if (!investmentGraph.containsKey(date)) {
            investmentGraph.put(date, 0d);
            totalAmountEth = amountEth;
        } else {
            totalAmountEth = investmentGraph.get(date) + amountEth;
        }

        investmentGraph.put(date, totalAmountEth);
        return this;
    }

    public PaymentStatisticsDTO build() {
        createdOnKeys = new ArrayList<>();
        amountEthValues = new ArrayList<>();
        investmentGraph
                .keySet()
                .stream()
                .forEach(key -> {
                    createdOnKeys.add(key);
                    amountEthValues.add(investmentGraph.get(key));
                });
        return this;
    }

    public List<Double> getAmountEthValues() {
        return amountEthValues;
    }

    public void setAmountEthValues(List<Double> amountEthValues) {
        this.amountEthValues = amountEthValues;
    }

    public List<Date> getCreatedOnKeys() {
        return createdOnKeys;
    }

    public void setCreatedOnKeys(List<Date> createdOnKeys) {
        this.createdOnKeys = createdOnKeys;
    }
}
