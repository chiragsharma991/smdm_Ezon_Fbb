package apsupportapp.aperotechnologies.com.designapp;

/**
 * Created by ifattehkhan on 27/08/16.
 */
public class StyleColorBean
{
    String color,size;
    int twSaleTotQty,stkOnhandQty;
    double fwdWeekCover;

    //harshada
    String articleCode,articleOption;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getTwSaleTotQty() {
        return twSaleTotQty;
    }

    public void setTwSaleTotQty(int twSaleTotQty) {
        this.twSaleTotQty = twSaleTotQty;
    }

    public int getStkOnhandQty() {
        return stkOnhandQty;
    }

    public void setStkOnhandQty(int stkOnhandQty) {
        this.stkOnhandQty = stkOnhandQty;
    }

    public double getFwdWeekCover() {
        return fwdWeekCover;
    }

    public void setFwdWeekCover(double fwdWeekCover) {
        this.fwdWeekCover = fwdWeekCover;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getArticleCode(){ return articleCode;}

    public void setArticleCode(String articleCode)
    {
        this.articleCode = articleCode;
    }

    public String getArticleOption(){return articleOption;}

    public void setArticleOption(String articleOption){
        this.articleOption = articleOption;
    }


}
