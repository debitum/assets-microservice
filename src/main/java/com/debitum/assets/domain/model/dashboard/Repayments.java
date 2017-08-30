package com.debitum.assets.domain.model.dashboard;


import com.debitum.assets.domain.model.investment.InvestmentEntry;

import static com.debitum.assets.domain.model.investment.CoinPrice.convertEthAmountToRepresentative;
import static com.debitum.assets.domain.model.investment.InvestmentEntryStatus.REPAID;

public class Repayments {

    private double totalInvestmentsEth;
    private double repaidInvestmentsEth;

    public Repayments(double totalInvestmentsEth,
                      double repaidInvestmentsEth) {
        this.totalInvestmentsEth = totalInvestmentsEth;
        this.repaidInvestmentsEth = repaidInvestmentsEth;
    }

    public double getTotalInvestmentsEth() {
        return totalInvestmentsEth;
    }

    public double getRepaidInvestmentsEth() {
        return repaidInvestmentsEth;
    }

    public static class RepaymentsBuilder {
        private double totalInvestmentsEth;
        private double repaidInvestmentsEth;

        private RepaymentsBuilder() {
        }

        public static RepaymentsBuilder instance() {
            return new RepaymentsBuilder();
        }

        public RepaymentsBuilder add(InvestmentEntry entry) {
            totalInvestmentsEth += convertEthAmountToRepresentative(entry.getAmountEth());
            if (entry.getStatus() == REPAID) {
                repaidInvestmentsEth += convertEthAmountToRepresentative(entry.getRepaidAmountEth());
            }
            return this;
        }

        public Repayments build() {
            return new Repayments(totalInvestmentsEth, repaidInvestmentsEth);
        }
    }
}
