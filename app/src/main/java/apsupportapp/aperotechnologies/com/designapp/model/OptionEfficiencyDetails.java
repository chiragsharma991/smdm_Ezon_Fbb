package apsupportapp.aperotechnologies.com.designapp.model;

/**
 * Created by pamrutkar on 29/11/16.
 */

public class OptionEfficiencyDetails {

    String storeCode;
    String storeDescription;
    String planDept;
    int optionCount;
    double optionCountByStore;
    double stkOnhandQty;
    double stkOnhandQtyCount;
    double fullSizeCount;
    double fullCutCount;
    double partCutCount;
    String planCategory;
    String planClass;
    String brandName;
    String brandplanClass;
    String seasongroup;

    public double getSohCountFullSize() {
        return sohCountFullSize;
    }

    public void setSohCountFullSize(double sohCountFullSize) {
        this.sohCountFullSize = sohCountFullSize;
    }

    double sohCountFullSize;

    public String getSeasongroup() {
        return seasongroup;
    }

    public void setSeasongroup(String seasongroup) {
        this.seasongroup = seasongroup;
    }




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

    public String getPlanDept() {
        return planDept;
    }

    public void setPlanDept(String planDept) {
        this.planDept = planDept;
    }

    public int getOptionCount() {
        return optionCount;
    }

    public void setOptionCount(int optionCount) {
        this.optionCount = optionCount;
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

    public double getOptionCountByStore() {
        return optionCountByStore;
    }

    public void setOptionCountByStore(double optionCountByStore) {
        this.optionCountByStore = optionCountByStore;
    }

    public double getStkOnhandQty() {
        return stkOnhandQty;
    }

    public void setStkOnhandQty(double stkOnhandQty) {
        this.stkOnhandQty = stkOnhandQty;
    }

    public double getStkOnhandQtyCount() {
        return stkOnhandQtyCount;
    }

    public void setStkOnhandQtyCount(double stkOnhandQtyCount) {
        this.stkOnhandQtyCount = stkOnhandQtyCount;
    }

    public double getFullSizeCount() {
        return fullSizeCount;
    }

    public void setFullSizeCount(double fullSizeCount) {
        this.fullSizeCount = fullSizeCount;
    }

    public double getFullCutCount() {
        return fullCutCount;
    }

    public void setFullCutCount(double fullCutCount) {
        this.fullCutCount = fullCutCount;
    }

    public double getPartCutCount() {
        return partCutCount;
    }

    public void setPartCutCount(double partCutCount) {
        this.partCutCount = partCutCount;
    }



}
