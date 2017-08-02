package apsupportapp.aperotechnologies.com.designapp.model;

import apsupportapp.aperotechnologies.com.designapp.HourlyPerformence.HourlyPerformence_Model;

/**
 * Created by csuthar on 01/06/17.
 */

public class FreshnessIndex_Ez_Model extends HourlyPerformence_Model {

    String level;
    int stkOnhandQty;
    double stkOnhandQtyCont;
    double sohAssortmentGrp;
    double assortmentGrpCont;
    double sohNonAssortmentGrp;
    double nonAssortmentGrpCont;
    int stkGitQty;



    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getStkOnhandQty() {
        return stkOnhandQty;
    }

    public void setStkOnhandQty(int stkOnhandQty) {
        this.stkOnhandQty = stkOnhandQty;
    }

    public double getStkOnhandQtyCont() {
        return stkOnhandQtyCont;
    }

    public void setStkOnhandQtyCont(double stkOnhandQtyCont) {
        this.stkOnhandQtyCont = stkOnhandQtyCont;
    }

    public double getAssortmentGrpCont() {
        return assortmentGrpCont;
    }

    public void setAssortmentGrpCont(double assortmentGrpCont) {
        this.assortmentGrpCont = assortmentGrpCont;
    }

    public double getSohAssortmentGrp() {
        return sohAssortmentGrp;
    }

    public void setSohAssortmentGrp(double sohAssortmentGrp) {
        this.sohAssortmentGrp = sohAssortmentGrp;
    }

    public double getSohNonAssortmentGrp() {
        return sohNonAssortmentGrp;
    }

    public void setSohNonAssortmentGrp(double sohNonAssortmentGrp) {
        this.sohNonAssortmentGrp = sohNonAssortmentGrp;
    }

    public double getNonAssortmentGrpCont() {
        return nonAssortmentGrpCont;
    }

    public void setNonAssortmentGrpCont(double nonAssortmentGrpCont) {
        this.nonAssortmentGrpCont = nonAssortmentGrpCont;
    }

    public int getStkGitQty() {
        return stkGitQty;
    }

    public void setStkGitQty(int stkGitQty) {
        this.stkGitQty = stkGitQty;
    }
}
