package com.debitum.assets.application.investment;

import com.debitum.assets.domain.model.dashboard.Dashboard;
import com.debitum.assets.domain.model.dashboard.DashboardService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@Transactional(readOnly = true)
public class DashboardApplication {

    private final DashboardService dashboardService;

    public DashboardApplication(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    public Dashboard overviewForUser(UUID userid) {
        return dashboardService.overview(userid);
    }
}


