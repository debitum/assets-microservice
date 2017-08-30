package com.debitum.assets.resource.investment;


import com.debitum.assets.domain.model.investment.CoinPrice;
import com.debitum.assets.domain.model.investment.Investment;
import com.debitum.assets.domain.model.investment.InvestmentStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@ApiModel(
        value = "InvestmentDTO",
        description = "Investment resource"
)
public class InvestmentDTO {

    @ApiModelProperty(value = "Investment identifier")
    private UUID id;

    @ApiModelProperty(value = "Investments user identifier")
    private UUID userId;

    @ApiModelProperty(value = "User fullname")
    private String userFullName;

    @ApiModelProperty(value = "Investment total amount")
    private Double totalAmount;

    @ApiModelProperty(value = "Investment contract token")
    private String contractToken;

    @ApiModelProperty(value = "Investment status")
    private InvestmentStatus status;

    @ApiModelProperty(value = "Investment total amount in ether")
    private Double totalAmountEth;

    @ApiModelProperty(value = "Last edit timestamp", readOnly = true)
    private Instant updatedOn;

    @ApiModelProperty(value = "Creation date", readOnly = true)
    private Instant createdOn;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getContractToken() {
        return contractToken;
    }

    public void setContractToken(String contractToken) {
        this.contractToken = contractToken;
    }

    public InvestmentStatus getStatus() {
        return status;
    }

    public void setStatus(InvestmentStatus status) {
        this.status = status;
    }

    public Double getTotalAmountEth() {
        return totalAmountEth;
    }

    public void setTotalAmountEth(Double totalAmountEth) {
        this.totalAmountEth = totalAmountEth;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Instant getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    public static InvestmentDTO from(String userFullName, Investment investment) {
        InvestmentDTO dto = new InvestmentDTO();
        dto.setId(investment.getId());
        dto.setTotalAmount(investment.getTotalAmount());
        dto.setUserId(investment.getUserId());
        dto.setUserFullName(userFullName);
        dto.setContractToken(investment.getContractToken());
        dto.setTotalAmountEth(CoinPrice.convertEthAmountToRepresentative(investment.getTotalAmountEth()));
        dto.setUpdatedOn(investment.getUpdatedOn());
        dto.setCreatedOn(investment.getCreatedOn());
        dto.setStatus(investment.getStatus());
        return dto;
    }

    public static InvestmentDTO from(Map<Long, String> usersNames, Investment investment) {
        InvestmentDTO dto = new InvestmentDTO();
        dto.setId(investment.getId());
        dto.setTotalAmount(investment.getTotalAmount());
        dto.setContractToken(investment.getContractToken());
        dto.setTotalAmountEth(CoinPrice.convertEthAmountToRepresentative(investment.getTotalAmountEth()));
        dto.setUserFullName(usersNames.getOrDefault(investment.getUserId(), "unknown"));
        dto.setUpdatedOn(investment.getUpdatedOn());
        dto.setCreatedOn(investment.getCreatedOn());
        dto.setUserId(investment.getUserId());
        dto.setStatus(investment.getStatus());
        return dto;
    }
}
