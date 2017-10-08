package com.debitum.assets.resource.dashboard;

import com.debitum.assets.domain.model.dashboard.Dashboard;
import com.debitum.assets.domain.model.investment.InvestmentEntry;
import io.swagger.annotations.ApiModel;

import java.util.List;

@ApiModel(
        value = "DashboardDTO",
        description = "Overview of clients investments"
)
public class DashboardDTO {

    private UsersInvestmentsStatisticsDTO myInvestments;
    private RepaymentsDTO repayments;


    public UsersInvestmentsStatisticsDTO getMyInvestments() {
        return myInvestments;
    }

    public void setMyInvestments(UsersInvestmentsStatisticsDTO myInvestments) {
        this.myInvestments = myInvestments;
    }

    public RepaymentsDTO getRepayments() {
        return repayments;
    }

    public void setRepayments(RepaymentsDTO repayments) {
        this.repayments = repayments;
    }

    public static DashboardDTO from(Dashboard dashboard,
                                    List<InvestmentEntry> currentUsersInvestmentEntries,
                                    List<InvestmentEntry> investmentEntries) {
        DashboardDTO dto = new DashboardDTO();
        dto.setMyInvestments(
                UsersInvestmentsStatisticsDTO.from(
                        dashboard.getMyInvestments(),
                        currentUsersInvestmentEntries,
                        investmentEntries
                )
        );
        dto.setRepayments(RepaymentsDTO.from(dashboard.getRepayments()));
        return dto;
    }
}
