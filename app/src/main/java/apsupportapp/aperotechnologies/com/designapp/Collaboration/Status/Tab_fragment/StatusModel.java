package apsupportapp.aperotechnologies.com.designapp.Collaboration.Status.Tab_fragment;

/**
 * Created by csuthar on 09/03/17.
 */

public class StatusModel
{
    int  caseNo;
    double  stkOnhandQtyRequested;
    double  stkOnhandQtyAcpt;
    String reqStoreCode;
    String statusInitiated;
    String statusAccept;
    String statusSto;
    String statusGrn;
    String level;

    public double getStkOnhandQtyRequested() {
        return stkOnhandQtyRequested;
    }

    public void setStkOnhandQtyRequested(double stkOnhandQtyRequested) {
        this.stkOnhandQtyRequested = stkOnhandQtyRequested;
    }

    public double getStkOnhandQtyAcpt() {
        return stkOnhandQtyAcpt;
    }

    public void setStkOnhandQtyAcpt(double stkOnhandQtyAcpt) {
        this.stkOnhandQtyAcpt = stkOnhandQtyAcpt;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }





    public int getCaseNo() {
        return caseNo;
    }

    public void setCaseNo(int caseNo) {
        this.caseNo = caseNo;
    }

    public String getReqStoreCode() {
        return reqStoreCode;
    }

    public void setReqStoreCode(String reqStoreCode) {
        this.reqStoreCode = reqStoreCode;
    }

    public String getStatusAccept() {
        return statusAccept;
    }

    public void setStatusAccept(String statusAccept) {
        this.statusAccept = statusAccept;
    }

    public String getStatusInitiated() {
        return statusInitiated;
    }

    public void setStatusInitiated(String statusInitiated) {
        this.statusInitiated = statusInitiated;
    }

    public String getStatusSto() {
        return statusSto;
    }

    public void setStatusSto(String statusSto) {
        this.statusSto = statusSto;
    }

    public String getStatusGrn() {
        return statusGrn;
    }

    public void setStatusGrn(String statusGrn) {
        this.statusGrn = statusGrn;
    }
}
