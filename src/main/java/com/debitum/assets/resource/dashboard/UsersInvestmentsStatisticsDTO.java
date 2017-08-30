package com.debitum.assets.resource.dashboard;


import com.debitum.assets.domain.model.dashboard.UsersInvestmentsStatistics;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
        value = "UsersInvestmentsStatisticsDTO",
        description = "Users investments statistics"
)
public class UsersInvestmentsStatisticsDTO {

    @ApiModelProperty(value = "1-15 days")
    private double days1_15;

    @ApiModelProperty(value = "16-30 days")
    private double days16_30;

    @ApiModelProperty(value = "31-60 days")
    private double days31_60;

    @ApiModelProperty(value = "60+ days")
    private double days60Plus;

    @ApiModelProperty(value = "Total")
    private double total;


    public double getDays1_15() {
        return days1_15;
    }

    public void setDays1_15(double days1_15) {
        this.days1_15 = days1_15;
    }

    public double getDays16_30() {
        return days16_30;
    }

    public void setDays16_30(double days16_30) {
        this.days16_30 = days16_30;
    }

    public double getDays31_60() {
        return days31_60;
    }

    public void setDays31_60(double days31_60) {
        this.days31_60 = days31_60;
    }

    public double getDays60Plus() {
        return days60Plus;
    }

    public void setDays60Plus(double days60Plus) {
        this.days60Plus = days60Plus;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public static UsersInvestmentsStatisticsDTO from(UsersInvestmentsStatistics domain) {
        UsersInvestmentsStatisticsDTO dto = new UsersInvestmentsStatisticsDTO();
        dto.setDays1_15(domain.getDays1_15());
        dto.setDays16_30(domain.getDays16_30());
        dto.setDays31_60(domain.getDays31_60());
        dto.setDays60Plus(domain.getDays60Plus());
        dto.setTotal(domain.getTotal());
        return dto;

    }
}
