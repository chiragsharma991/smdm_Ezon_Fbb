package apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do;

/**
 * Created by csuthar on 03/03/17.
 */

public class ToDo_Modal {



    String storeCode;
    String storeDesc;
    String level;

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    String levelCode;
    double stkOnhandQtyRequested;
    double stkQtyAvl;
    double stkOnhandQty;
    double stkGitQty;
    double sellThruUnits;
    double fwdWeekCover;

    public double getStkOnhandQtyRequested() {
        return stkOnhandQtyRequested;
    }

    public void setStkOnhandQtyRequested(double stkOnhandQtyRequested) {
        this.stkOnhandQtyRequested = stkOnhandQtyRequested;
    }

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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public double getStkQtyAvl() {
        return stkQtyAvl;
    }

    public void setStkQtyAvl(double stkQtyAvl) {
        this.stkQtyAvl = stkQtyAvl;
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

    public double getSellThruUnits() {
        return sellThruUnits;
    }

    public void setSellThruUnits(double sellThruUnits) {
        this.sellThruUnits = sellThruUnits;
    }

    public double getFwdWeekCover() {
        return fwdWeekCover;
    }

    public void setFwdWeekCover(double fwdWeekCover) {
        this.fwdWeekCover = fwdWeekCover;
    }

}




