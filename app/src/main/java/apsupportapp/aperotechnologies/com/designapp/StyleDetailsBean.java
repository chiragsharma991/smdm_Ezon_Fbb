package apsupportapp.aperotechnologies.com.designapp;

import java.io.Serializable;

/**
 * Created by ifattehkhan on 24/08/16.
 */
public class StyleDetailsBean implements Serializable
{
    String productName,collectionName,productFabricDesc,productFitDesc,productFinishDesc,seasonName,
            firstReceiptDate,lastReceiptDate;
  int fwdWeekCover,stkOnhandQty,stkGitQty,targetStock,twSaleTotQty,lwSaleTotQty,ytdSaleTotQty,unitGrossPrice,ros,usp;
    double sellThruUnitsRcpt;
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getProductFabricDesc() {
        return productFabricDesc;
    }

    public void setProductFabricDesc(String productFabricDesc) {
        this.productFabricDesc = productFabricDesc;
    }

    public String getProductFitDesc() {
        return productFitDesc;
    }

    public void setProductFitDesc(String productFitDesc) {
        this.productFitDesc = productFitDesc;
    }

    public String getProductFinishDesc() {
        return productFinishDesc;
    }

    public void setProductFinishDesc(String productFinishDesc) {
        this.productFinishDesc = productFinishDesc;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public String getFirstReceiptDate() {
        return firstReceiptDate;
    }

    public void setFirstReceiptDate(String firstReceiptDate) {
        this.firstReceiptDate = firstReceiptDate;
    }

    public String getLastReceiptDate() {
        return lastReceiptDate;
    }

    public void setLastReceiptDate(String lastReceiptDate) {
        this.lastReceiptDate = lastReceiptDate;
    }

    public int getFwdWeekCover() {
        return fwdWeekCover;
    }

    public void setFwdWeekCover(int fwdWeekCover) {
        this.fwdWeekCover = fwdWeekCover;
    }

    public int getTwSaleTotQty() {
        return twSaleTotQty;
    }

    public void setTwSaleTotQty(int twSaleTotQty) {
        this.twSaleTotQty = twSaleTotQty;
    }

    public int getLwSaleTotQty() {
        return lwSaleTotQty;
    }

    public void setLwSaleTotQty(int lwSaleTotQty) {
        this.lwSaleTotQty = lwSaleTotQty;
    }

    public int getYtdSaleTotQty() {
        return ytdSaleTotQty;
    }

    public void setYtdSaleTotQty(int ytdSaleTotQty) {
        this.ytdSaleTotQty = ytdSaleTotQty;
    }

    public int getStkOnhandQty() {
        return stkOnhandQty;
    }

    public void setStkOnhandQty(int stkOnhandQty) {
        this.stkOnhandQty = stkOnhandQty;
    }

    public int getStkGitQty() {
        return stkGitQty;
    }

    public void setStkGitQty(int stkGitQty) {
        this.stkGitQty = stkGitQty;
    }

    public int getTargetStock() {
        return targetStock;
    }

    public void setTargetStock(int targetStock) {
        this.targetStock = targetStock;
    }

    public int getUnitGrossPrice() {
        return unitGrossPrice;
    }

    public void setUnitGrossPrice(int unitGrossPrice) {
        this.unitGrossPrice = unitGrossPrice;
    }

    public double getSellThruUnitsRcpt() {
        return sellThruUnitsRcpt;
    }

    public void setSellThruUnitsRcpt(double sellThruUnitsRcpt) {
        this.sellThruUnitsRcpt = sellThruUnitsRcpt;
    }

    public int getRos() {
        return ros;
    }

    public void setRos(int ros) {
        this.ros = ros;
    }

    public int getUsp() {
        return usp;
    }

    public void setUsp(int usp) {
        this.usp = usp;
    }
}
