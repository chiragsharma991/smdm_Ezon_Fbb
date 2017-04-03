package apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do;

/**
 * Created by csuthar on 03/03/17.
 */

public class ToDo_Modal {

    String storeCode;
    String storeDesc;
    String transferStatus;
    double stkOnhandQtyRequested;
    double stkQtyAvl;
    double stkOnhandQty;
    double stkGitQty;
    int noOfOptions;
    double fwdWeekCover;
    String mccodeDesc;
    String level;



    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    String option;

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getStoreDesc() {
        return storeDesc;
    }

    public void setStoreDesc(String storeDesc) {
        this.storeDesc = storeDesc;
    }

    public double getStkOnhandQtyRequested() {
        return stkOnhandQtyRequested;
    }

    public void setStkOnhandQtyRequested(double stkOnhandQtyRequested) {
        this.stkOnhandQtyRequested = stkOnhandQtyRequested;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public double getStkQtyAvl() {
        return stkQtyAvl;
    }

    public void setStkQtyAvl(double stkQtyAvl) {
        this.stkQtyAvl = stkQtyAvl;
    }

    public int getNoOfOptions() {
        return noOfOptions;
    }

    public void setNoOfOptions(int noOfOptions) {
        this.noOfOptions = noOfOptions;
    }

    public double getStkOnhandQty() {
        return stkOnhandQty;
    }

    public void setStkOnhandQty(double stkOnhandQty) {
        this.stkOnhandQty = stkOnhandQty;
    }

    public double getStkGitQty() {
        return stkGitQty;
    }

    public void setStkGitQty(double stkGitQty) {
        this.stkGitQty = stkGitQty;
    }

    public double getFwdWeekCover() {
        return fwdWeekCover;
    }

    public void setFwdWeekCover(double fwdWeekCover) {
        this.fwdWeekCover = fwdWeekCover;
    }

    public String getMccodeDesc() {
        return mccodeDesc;
    }

    public void setMccodeDesc(String mccodeDesc) {
        this.mccodeDesc = mccodeDesc;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
