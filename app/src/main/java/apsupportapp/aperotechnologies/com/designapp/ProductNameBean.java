package apsupportapp.aperotechnologies.com.designapp;

import java.io.Serializable;

/**
 * Created by pamrutkar on 24/08/16.
 */
public class ProductNameBean implements Serializable {
    String productName;
    String l2hrsNetSales;
    String dayNetSales;
    String wtdNetSales;
    String dayNetSalesPercent;
    String wtdNetSalesPercent;
    String soh;
    String git;
    String articleOption;



    public String getWtdNetSales() {
        return wtdNetSales;
    }

    public void setWtdNetSales(String wtdNetSales) {
        this.wtdNetSales = wtdNetSales;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getL2hrsNetSales() {
        return l2hrsNetSales;
    }

    public void setL2hrsNetSales(String l2hrsNetSales) {
        this.l2hrsNetSales = l2hrsNetSales;
    }

    public String getDayNetSales() {
        return dayNetSales;
    }

    public void setDayNetSales(String dayNetSales) {
        this.dayNetSales = dayNetSales;
    }

    public String getDayNetSalesPercent() {
        return dayNetSalesPercent;
    }

    public void setDayNetSalesPercent(String dayNetSalesPercent) {
        this.dayNetSalesPercent = dayNetSalesPercent;
    }

    public String getWtdNetSalesPercent() {
        return wtdNetSalesPercent;
    }

    public void setWtdNetSalesPercent(String wtdNetSalesPercent) {
        this.wtdNetSalesPercent = wtdNetSalesPercent;
    }

    public String getSoh() {
        return soh;
    }

    public void setSoh(String soh) {
        this.soh = soh;
    }

    public String getGit() {
        return git;
    }

    public void setGit(String git) {
        this.git = git;
    }

    public String getArticleOption() {
        return articleOption;
    }

    public void setArticleOption(String articleOption) {
        this.articleOption = articleOption;
    }



}
