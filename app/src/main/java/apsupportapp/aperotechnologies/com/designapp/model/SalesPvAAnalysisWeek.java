package apsupportapp.aperotechnologies.com.designapp.model;

/**
 * Created by pamrutkar on 22/12/16.
 */

public class SalesPvAAnalysisWeek {

    String storeCode;
    String storeDescription;
    double saleNetVal;
    double planSaleNetVal;
    double pvaAchieved;
    String weekNumber;



    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getStoreDescription() {
        return storeDescription;
    }

    public void setStoreDescription(String storeDescription) {
        this.storeDescription = storeDescription;
    }

    public double getSaleNetVal() {
        return saleNetVal;
    }

    public void setSaleNetVal(double saleNetVal) {
        this.saleNetVal = saleNetVal;
    }

    public double getPlanSaleNetVal() {
        return planSaleNetVal;
    }

    public void setPlanSaleNetVal(double planSaleNetVal) {
        this.planSaleNetVal = planSaleNetVal;
    }

    public double getPvaAchieved() {
        return pvaAchieved;
    }

    public void setPvaAchieved(double pvaAchieved) {
        this.pvaAchieved = pvaAchieved;
    }

    public String getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(String weekNumber) {
        this.weekNumber = weekNumber;
    }




}
