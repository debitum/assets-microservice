package com.debitum.assets.port.adapter.dashboard;


import com.debitum.assets.application.investment.InvestmentApplication;
import com.debitum.assets.domain.model.dashboard.Dashboard;
import com.debitum.assets.domain.model.dashboard.DashboardService;
import com.debitum.assets.domain.model.dashboard.Repayments.RepaymentsBuilder;
import com.debitum.assets.domain.model.dashboard.UsersInvestmentsStatistics.UsersInvestmentsStatisticsBuilder;
import com.debitum.assets.domain.model.investment.InvestmentEntry;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static com.debitum.assets.domain.model.dashboard.UsersInvestmentsStatistics.UsersInvestmentsStatisticsBuilder.instance;

@Component
class ClientDashboardService implements DashboardService {

    private final InvestmentApplication investmentApplication;

    public ClientDashboardService(InvestmentApplication investmentApplication) {
        this.investmentApplication = investmentApplication;
    }

    @Override
    public Dashboard overview(UUID userId) {
        List<InvestmentEntry> investmentEntries = investmentApplication.investmentEntriesOfUser(userId);
        UsersInvestmentsStatisticsBuilder statisticsBuilder = instance();
        RepaymentsBuilder repaymentsBuilder = RepaymentsBuilder.instance();

        if (CollectionUtils.isNotEmpty(investmentEntries)) {
            investmentEntries.stream().filter(entry -> entry != null).forEach(entry -> {
                statisticsBuilder.add(
                        entry.getCreatedOn(),
                        entry.getAmount()
                );
                repaymentsBuilder.add(
                        entry
                );
            });
        }

        return new Dashboard(
                statisticsBuilder.build(),
                repaymentsBuilder.build()
        );
    }
}
