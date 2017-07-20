package apsupportapp.aperotechnologies.com.designapp.KeyProductPlan;

/**
 * Created by pamrutkar on 27/02/17.
 */

public class KeyPlanProductBean {
    String storeCode;
    String storeDesc;
    String productCode;
    String productDesc;
    String productName;
            String option;
         String size;
    double pvaSales;

    public String getAchColor() {
        return achColor;
    }

    public void setAchColor(String achColor) {
        this.achColor = achColor;
    }

    String achColor;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    String level;

    public double getPvaStock() {
        return pvaStock;
    }

    public void setPvaStock(double pvaStock) {
        this.pvaStock = pvaStock;
    }

    public double getPvaSales() {
        return pvaSales;
    }

    public void setPvaSales(double pvaSales) {
        this.pvaSales = pvaSales;
    }

    double pvaStock;

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

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getPlanSaleTotQty() {
        return planSaleTotQty;
    }

    public void setPlanSaleTotQty(double planSaleTotQty) {
        this.planSaleTotQty = planSaleTotQty;
    }

    public double getPlanTargetStockQty() {
        return planTargetStockQty;
    }

    public void setPlanTargetStockQty(double planTargetStockQty) {
        this.planTargetStockQty = planTargetStockQty;
    }

    public double getPlanSaleNetVal() {
        return planSaleNetVal;
    }

    public void setPlanSaleNetVal(double planSaleNetVal) {
        this.planSaleNetVal = planSaleNetVal;
    }

    public double getSaleNetVal() {
        return saleNetVal;
    }

    public void setSaleNetVal(double saleNetVal) {
        this.saleNetVal = saleNetVal;
    }

    public double getSaleTotQty() {
        return saleTotQty;
    }

    public void setSaleTotQty(double saleTotQty) {
        this.saleTotQty = saleTotQty;
    }

    public double getInvClosingQty() {
        return invClosingQty;
    }

    public void setInvClosingQty(double invClosingQty) {
        this.invClosingQty = invClosingQty;
    }

    double planSaleTotQty;
         double planSaleNetVal;
        double planTargetStockQty;
        double  saleNetVal;
        double saleTotQty;
        double invClosingQty;

}
