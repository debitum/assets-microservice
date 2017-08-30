package com.debitum.assets.domain.model.dashboard;


import com.debitum.assets.IntegrationTestBase;
import org.junit.Test;

import javax.inject.Inject;
import java.util.UUID;

import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;

public class DashboardServiceTest extends IntegrationTestBase {

    @Inject
    private DashboardService dashboardService;

    @Test
    public void givenUsersInvestments_whenConstructingUsersInvestmentDashboard_thenInvestmentsDividedToPeriods() {
        //given
        UUID EXISTING_USER_ID = fromString("f954f7a8-7bfc-11e7-bb31-be2e44b06b34");

        //when
        Dashboard overview = dashboardService.overview(EXISTING_USER_ID);

        //then
        assertThat(overview.getMyInvestments().getTotal()).isGreaterThan(0);

    }
}
