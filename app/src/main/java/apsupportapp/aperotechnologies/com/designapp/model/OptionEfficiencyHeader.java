package apsupportapp.aperotechnologies.com.designapp.model;

/**
 * Created by pamrutkar on 03/01/17.
 */

public class OptionEfficiencyHeader {
    String storeCode;
    String storeDescription;
    double optionCount;
    double fullSizeOption;

    public double getFullSizeCount() {
        return fullSizeCount;
    }

    public void setFullSizeCount(double fullSizeCount) {
        this.fullSizeCount = fullSizeCount;
    }

    public double getStkOnhandQtyCount() {
        return stkOnhandQtyCount;
    }

    public void setStkOnhandQtyCount(double stkOnhandQtyCount) {
        this.stkOnhandQtyCount = stkOnhandQtyCount;
    }

    public double getStkOnhandQty() {
        return stkOnhandQty;
    }

    public void setStkOnhandQty(double stkOnhandQty) {
        this.stkOnhandQty = stkOnhandQty;
    }

    public double getFullCutCount() {
        return fullCutCount;
    }

    public void setFullCutCount(double fullCutCount) {
        this.fullCutCount = fullCutCount;
    }

    public double getFullCutOption() {
        return fullCutOption;
    }

    public void setFullCutOption(double fullCutOption) {
        this.fullCutOption = fullCutOption;
    }

    public double getPartCutCount() {
        return partCutCount;
    }

    public void setPartCutCount(double partCutCount) {
        this.partCutCount = partCutCount;
    }

    public double getPartCutOption() {
        return partCutOption;
    }

    public void setPartCutOption(double partCutOption) {
        this.partCutOption = partCutOption;
    }

    public double getFullSizeOption() {
        return fullSizeOption;
    }

    public void setFullSizeOption(double fullSizeOption) {
        this.fullSizeOption = fullSizeOption;
    }

    public double getOptionCount() {
        return optionCount;
    }

    public void setOptionCount(double optionCount) {
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

    double fullSizeCount;
    double partCutOption;
    double partCutCount;
    double fullCutOption;
    double fullCutCount;
    double stkOnhandQty;
    double stkOnhandQtyCount;

}
