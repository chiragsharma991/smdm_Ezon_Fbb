package apsupportapp.aperotechnologies.com.designapp.model;

/**
 * Created by pamrutkar on 07/12/16.
 */

public class FloorAvailabilityDetails {
    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandplanClass() {
        return brandplanClass;
    }

    public void setBrandplanClass(String brandplanClass) {
        this.brandplanClass = brandplanClass;
    }

    String storeCode;
    String storeDescription;
    String option;
    String prodImageURL;

    String noDaysPassed;
    String brandName;
    String brandplanClass;
    String firstReceiptDate;
    double ros;
    double availabilityPct;
    String planDept;
    double stkOnhandQty;
    double targetROS;


    public String getPlanCategory() {
        return planCategory;
    }

    public void setPlanCategory(String planCategory) {
        this.planCategory = planCategory;
    }

    public String getPlanClass() {
        return planClass;
    }

    public void setPlanClass(String planClass) {
        this.planClass = planClass;
    }

    String planCategory;
    String planClass;

    public String getPlanDept() {
        return planDept;
    }

    public void setPlanDept(String planDept) {
        this.planDept = planDept;
    }



    public double getTargetROS() {
        return targetROS;
    }

    public void setTargetROS(double targetROS) {
        this.targetROS = targetROS;
    }



    public double getRos() {
        return ros;
    }

    public void setRos(double ros) {
        this.ros = ros;
    }

    public double getAvailabilityPct() {
        return availabilityPct;
    }

    public void setAvailabilityPct(double availabilityPct) {
        this.availabilityPct = availabilityPct;
    }




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

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getProdImageURL() {
        return prodImageURL;
    }

    public void setProdImageURL(String prodImageURL) {
        this.prodImageURL = prodImageURL;
    }

    public double getStkOnhandQty() {
        return stkOnhandQty;
    }

    public void setStkOnhandQty(double stkOnhandQty) {
        this.stkOnhandQty = stkOnhandQty;
    }

    public String getNoDaysPassed() {
        return noDaysPassed;
    }

    public void setNoDaysPassed(String noDaysPassed) {
        this.noDaysPassed = noDaysPassed;
    }

    public String getFirstReceiptDate() {
        return firstReceiptDate;
    }

    public void setFirstReceiptDate(String firstReceiptDate) {
        this.firstReceiptDate = firstReceiptDate;
    }


}
