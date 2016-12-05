package apsupportapp.aperotechnologies.com.designapp;

import java.io.Serializable;


public class ProductLevelBean implements Serializable {

    String productLevel3Code;
    String productLevel3Desc;
    String productName;
    String prodLevel6Code, prodLevel6Desc;
    String articleCode, articleDesc;

    public String getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(String articleCode) {
        this.articleCode = articleCode;
    }

    public String getArticleDesc() {
        return articleDesc;
    }

    public void setArticleDesc(String articleDesc) {
        this.articleDesc = articleDesc;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


    public String getProdLevel6Code() {
        return prodLevel6Code;
    }

    public void setProdLevel6Code(String prodLevel6Code) {
        this.prodLevel6Code = prodLevel6Code;
    }

    public String getProdLevel6Desc() {
        return prodLevel6Desc;
    }

    public void setProdLevel6Desc(String prodLevel6Desc) {
        this.prodLevel6Desc = prodLevel6Desc;
    }


    public String getProductLevel3Code() {
        return productLevel3Code;
    }

    public void setProductLevel3Code(String productLevel3Code) {
        this.productLevel3Code = productLevel3Code;
    }

    public String getProductLevel3Desc() {
        return productLevel3Desc;
    }

    public void setProductLevel3Desc(String productLevel3Desc) {
        this.productLevel3Desc = productLevel3Desc;
    }


}
