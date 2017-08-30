package com.debitum.assets.domain.model.dashboard;


public class Dashboard {

    private UsersInvestmentsStatistics myInvestments;
    private Repayments repayments;

    public Dashboard(UsersInvestmentsStatistics myInvestments,
                     Repayments repayments) {
        this.myInvestments = myInvestments;
        this.repayments = repayments;
    }

    public UsersInvestmentsStatistics getMyInvestments() {
        return myInvestments;
    }

    public Repayments getRepayments() {
        return repayments;
    }
}
