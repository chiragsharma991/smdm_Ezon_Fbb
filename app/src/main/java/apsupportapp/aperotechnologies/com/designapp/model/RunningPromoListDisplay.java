package apsupportapp.aperotechnologies.com.designapp.model;

/**
 * Created by pamrutkar on 16/11/16.
 */
public class RunningPromoListDisplay {
    String storeCode;
    String storeDesc;
    String alphanumericPromoCode;
    String promoCode;
    String promoDesc;
    String promoStartDate;
    String promoEndDate;
    String salesFlg;
    double durSaleNetVal;
    int    promoDays;
    int durSaleTotQty;
    String promoImage;

    String prodLevel6Desc;
    int stkOnhandQty;

    public int getStkOnhandQty() {
        return stkOnhandQty;
    }

    public void setStkOnhandQty(int stkOnhandQty) {
        this.stkOnhandQty = stkOnhandQty;
    }

    public String getProdLevel6Desc() {
        return prodLevel6Desc;
    }

    public void setProdLevel6Desc(String prodLevel6Desc) {
        this.prodLevel6Desc = prodLevel6Desc;
    }

    public String getPromoImage() {
        return promoImage;
    }

    public void setPromoImage(String promoImage) {
        this.promoImage = promoImage;
    }

    public int getDurSaleTotQty() {
        return durSaleTotQty;
    }

    public void setDurSaleTotQty(int durSaleTotQty) {
        this.durSaleTotQty = durSaleTotQty;
    }

    public int getPromoDays() {
        return promoDays;
    }

    public void setPromoDays(int promoDays) {
        this.promoDays = promoDays;
    }

    public double getDurSaleNetVal() {
        return durSaleNetVal;
    }

    public void setDurSaleNetVal(double durSaleNetVal) {
        this.durSaleNetVal = durSaleNetVal;
    }

    public String getSalesFlg() {
        return salesFlg;
    }

    public void setSalesFlg(String salesFlg) {
        this.salesFlg = salesFlg;
    }

    public String getPromoEndDate() {
        return promoEndDate;
    }

    public void setPromoEndDate(String promoEndDate) {
        this.promoEndDate = promoEndDate;
    }

    public String getPromoStartDate() {
        return promoStartDate;
    }

    public void setPromoStartDate(String promoStartDate) {
        this.promoStartDate = promoStartDate;
    }

    public String getPromoDesc() {
        return promoDesc;
    }

    public void setPromoDesc(String promoDesc) {
        this.promoDesc = promoDesc;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getAlphanumericPromoCode() {
        return alphanumericPromoCode;
    }

    public void setAlphanumericPromoCode(String alphanumericPromoCode) {
        this.alphanumericPromoCode = alphanumericPromoCode;
    }

    public String getStoreDesc() {
        return storeDesc;
    }

    public void setStoreDesc(String storeDesc) {
        this.storeDesc = storeDesc;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }



}
