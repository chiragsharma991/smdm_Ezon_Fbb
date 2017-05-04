package apsupportapp.aperotechnologies.com.designapp.HorlyAnalysis;

import java.io.Serializable;


public class ProductNameBean implements Serializable {
    String productName;
    int l2hrsNetSales, dayNetSales, wtdNetSales, soh, git;
    double dayNetSalesPercent;
    double wtdNetSalesPercent;

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    String articleName;
    String articleOption;
    String storeDesc;
    String storeCode;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    String size;

    public String getProdDesc() {
        return prodDesc;
    }

    public void setProdDesc(String prodDesc) {
        this.prodDesc = prodDesc;
    }

    String prodDesc;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    String color;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    String productCode;

    public String getArtileCode() {
        return artileCode;
    }

    public void setArtileCode(String artileCode) {
        this.artileCode = artileCode;
    }

    String artileCode;

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

    public int getWtdNetSales() {
        return wtdNetSales;
    }

    public void setWtdNetSales(int wtdNetSales) {
        this.wtdNetSales = wtdNetSales;

    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getL2hrsNetSales() {
        return l2hrsNetSales;
    }

    public void setL2hrsNetSales(int l2hrsNetSales) {
        this.l2hrsNetSales = l2hrsNetSales;

    }

    public int getDayNetSales() {
        return dayNetSales;
    }

    public void setDayNetSales(int dayNetSales) {
        this.dayNetSales = dayNetSales;

    }

    public double getDayNetSalesPercent() {
        return dayNetSalesPercent;
    }

    public void setDayNetSalesPercent(double dayNetSalesPercent) {
        this.dayNetSalesPercent = dayNetSalesPercent;

    }

    public double getWtdNetSalesPercent() {
        return wtdNetSalesPercent;
    }

    public void setWtdNetSalesPercent(double wtdNetSalesPercent) {
        this.wtdNetSalesPercent = wtdNetSalesPercent;

    }

    public int getSoh() {
        return soh;
    }

    public void setSoh(int soh) {
        this.soh = soh;

    }

    public int getGit() {
        return git;
    }

    public void setGit(int git) {
        this.git = git;

    }

    public String getArticleOption() {
        return articleOption;
    }

    public void setArticleOption(String articleOption) {
        this.articleOption = articleOption;
    }

}
