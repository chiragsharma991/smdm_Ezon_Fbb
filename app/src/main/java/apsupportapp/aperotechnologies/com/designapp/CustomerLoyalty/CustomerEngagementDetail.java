package apsupportapp.aperotechnologies.com.designapp.CustomerLoyalty;

/**
 * Created by pamrutkar on 17/06/17.
 */

public class CustomerEngagementDetail
{
    String engagementBand;

    public double getCustAch() {
        return custAch;
    }

    public void setCustAch(double custAch) {
        this.custAch = custAch;
    }

    double custAch;


    public double getCustCount() {
        return custCount;
    }

    public void setCustCount(double custCount) {
        this.custCount = custCount;
    }

    public String getEngagementBand() {
        return engagementBand;
    }

    public void setEngagementBand(String engagementBand) {
        this.engagementBand = engagementBand;
    }

    public double getSpend() {
        return spend;
    }

    public void setSpend(double spend) {
        this.spend = spend;
    }

    public double getSpc() {
        return spc;
    }

    public void setSpc(double spc) {
        this.spc = spc;
    }

    double custCount;
    double spend;
    double spc;

    public double getSalesAch() {
        return salesAch;
    }

    public void setSalesAch(double salesAch) {
        this.salesAch = salesAch;
    }

    double salesAch;
    double planSpend;

    public double getPlanCust() {
        return planCust;
    }

    public void setPlanCust(double planCust) {
        this.planCust = planCust;
    }

    public double getPlanSpend() {
        return planSpend;
    }

    public void setPlanSpend(double planSpend) {
        this.planSpend = planSpend;
    }

    public double getPlanSpc() {
        return planSpc;
    }

    public void setPlanSpc(double planSpc) {
        this.planSpc = planSpc;
    }

    double planCust;
   double planSpc;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    String level;


}
