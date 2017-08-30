package com.debitum.assets.domain.model.dashboard;


import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class UsersInvestmentsStatistics {

    private final double days1_15;
    private final double days16_30;
    private final double days31_60;
    private final double days60Plus;
    private final double total;

    private UsersInvestmentsStatistics(double days1_15,
                                       double days16_30,
                                       double days31_60,
                                       double days60Plus,
                                       double total) {
        this.days1_15 = days1_15;
        this.days16_30 = days16_30;
        this.days31_60 = days31_60;
        this.days60Plus = days60Plus;
        this.total = total;
    }

    public double getDays1_15() {
        return days1_15;
    }

    public double getDays16_30() {
        return days16_30;
    }

    public double getDays31_60() {
        return days31_60;
    }

    public double getDays60Plus() {
        return days60Plus;
    }

    public double getTotal() {
        return total;
    }

    public static class UsersInvestmentsStatisticsBuilder {
        private double days1_15;
        private double days16_30;
        private double days31_60;
        private double days60Plus;
        private double total;

        private UsersInvestmentsStatisticsBuilder() {

        }

        public static UsersInvestmentsStatisticsBuilder instance() {
            return new UsersInvestmentsStatisticsBuilder();
        }

        public UsersInvestmentsStatisticsBuilder add(Instant date, double amount) {
            if (date.isAfter(Instant.now().minus(16, ChronoUnit.DAYS))) {
                days1_15 += amount;
            } else if (date.isAfter(Instant.now().minus(31, ChronoUnit.DAYS))) {
                days16_30 += amount;
            } else if (date.isAfter(Instant.now().minus(61, ChronoUnit.DAYS))) {
                days31_60 += amount;
            } else {
                days60Plus += amount;
            }
            total += amount;
            return this;
        }

        public UsersInvestmentsStatistics build() {
            return new UsersInvestmentsStatistics(
                    days1_15,
                    days16_30,
                    days31_60,
                    days60Plus,
                    total);
        }


    }

}

