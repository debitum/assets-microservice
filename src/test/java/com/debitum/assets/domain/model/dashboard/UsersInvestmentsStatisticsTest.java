package com.debitum.assets.domain.model.dashboard;


import com.debitum.assets.UnitTestBase;
import com.debitum.assets.domain.model.dashboard.UsersInvestmentsStatistics.UsersInvestmentsStatisticsBuilder;
import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.debitum.assets.domain.model.dashboard.UsersInvestmentsStatistics.UsersInvestmentsStatisticsBuilder.instance;
import static org.assertj.core.api.Assertions.assertThat;

public class UsersInvestmentsStatisticsTest extends UnitTestBase{


    @Test
    public void givenUsersInvestments_whenConstructingUsersInvestmentDashboard_thenInvestmentsDividedToPeriods(){
        //given
        UsersInvestmentsStatisticsBuilder builder = instance();

        //when
        builder.add(Instant.now().minus(15, ChronoUnit.DAYS), 10);
        builder.add(Instant.now().minus(30, ChronoUnit.DAYS), 20);
        builder.add(Instant.now().minus(60, ChronoUnit.DAYS), 50);
        builder.add(Instant.now().minus(61, ChronoUnit.DAYS), 20);
        UsersInvestmentsStatistics statistics = builder.build();

        //then
        assertThat(statistics.getDays1_15()).isEqualTo(10);
        assertThat(statistics.getDays16_30()).isEqualTo(20);
        assertThat(statistics.getDays31_60()).isEqualTo(50);
        assertThat(statistics.getDays60Plus()).isEqualTo(20);
        assertThat(statistics.getTotal()).isEqualTo(100);
    }
}
