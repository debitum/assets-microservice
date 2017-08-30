package com.debitum.assets.resource.dashboard;


import com.debitum.assets.domain.model.dashboard.Repayments;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
        value = "DashboardDTO",
        description = "Overview of clients investments"
)
public class RepaymentsDTO {

    @ApiModelProperty(value = "Total investments amount in eth")
    private double totalInvestmentsEth;
    @ApiModelProperty(value = "Repaid investments amount in eth")
    private double repaidInvestmentsEth;

    public double getTotalInvestmentsEth() {
        return totalInvestmentsEth;
    }

    public void setTotalInvestmentsEth(double totalInvestmentsEth) {
        this.totalInvestmentsEth = totalInvestmentsEth;
    }

    public double getRepaidInvestmentsEth() {
        return repaidInvestmentsEth;
    }

    public void setRepaidInvestmentsEth(double repaidInvestmentsEth) {
        this.repaidInvestmentsEth = repaidInvestmentsEth;
    }

    public static RepaymentsDTO from(Repayments repayments) {
        RepaymentsDTO dto = new RepaymentsDTO();
        dto.setTotalInvestmentsEth(repayments.getTotalInvestmentsEth());
        dto.setRepaidInvestmentsEth(repayments.getRepaidInvestmentsEth());
        return dto;
    }
}
