package apsupportapp.aperotechnologies.com.designapp.model;

/**
 * Created by pamrutkar on 07/12/16.
 */

public class FloorAvailabilityDetails {


     String storeCode;
    String storeDescription;
    String option;
    String prodImageURL;
    double stkOnhandQty ;
    String noDaysPassed;
    String firstReceiptDate;

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
