package apsupportapp.aperotechnologies.com.designapp.Collaboration.Status.Tab_fragment;

/**
 * Created by csuthar on 09/03/17.
 */

public class StatusModel
{
    int  caseNo;
    int  docNo;
    double  stoQty;
    double  grnQty;
    double  stkOnhandQtyRequested;
    double  stkOnhandQtyAcpt;
    String reqStoreCode;
    String statusInitiated;
    String statusAccept;
    String statusSto;
    String statusGrn;
    String level;
    String receiver_requested_date;
    String stoDate;
    String grnDate;

    public String getSenderStoreCode() {
        return senderStoreCode;
    }

    public void setSenderStoreCode(String senderStoreCode) {
        this.senderStoreCode = senderStoreCode;
    }

    public String getSenderStoreDesc() {
        return senderStoreDesc;
    }

    public void setSenderStoreDesc(String senderStoreDesc) {
        this.senderStoreDesc = senderStoreDesc;
    }

    String senderStoreCode;
    String senderStoreDesc;



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

    public int getDocNo() {
        return docNo;
    }

    public void setDocNo(int docNo) {
        this.docNo = docNo;
    }

    public String getReceiver_requested_date() {
        return receiver_requested_date;
    }

    public void setReceiver_requested_date(String receiver_requested_date) {
        this.receiver_requested_date = receiver_requested_date;

     }

    

    public double getStoQty() {
        return stoQty;
    }

    public void setStoQty(double stoQty) {
        this.stoQty = stoQty;
    }

    public double getGrnQty() {
        return grnQty;
    }

    public void setGrnQty(double grnQty) {
        this.grnQty = grnQty;
    }

    public String getStoDate() {
        return stoDate;
    }

    public void setStoDate(String stoDate) {
        this.stoDate = stoDate;
    }

    public String getGrnDate() {
        return grnDate;
    }

    public void setGrnDate(String grnDate) {
        this.grnDate = grnDate;
    }
}
