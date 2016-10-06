package apsupportapp.aperotechnologies.com.designapp.SalesPvAAnalysis;

import java.util.ArrayList;

/**
 * Created by pamrutkar on 09/09/16.
 */
public class FilterArray {

    String subdept = "";
    String prodname = "";
    public boolean Checked = false;

    ArrayList prodarray, articlearray;

    public ArrayList getArticlearray() {
        return articlearray;
    }

    public void setArticlearray(ArrayList articlearray) {
        this.articlearray = articlearray;
    }



    public void setSubdept(String subdept)
    {
        this.subdept = subdept;
    }

    public String getSubdept() {
        return subdept;
    }


    public void setprodname(String prodname)
    {
        this.prodname = prodname;
    }

    public String getprodname() {
        return prodname;
    }


    public void setprodArray(ArrayList prodarray)
    {
        this.prodarray = prodarray;
    }

    public ArrayList getprodarray() {
        return prodarray;
    }


    public boolean getSelected()
    {
        return Checked;
    }
    public void setSelected(boolean Checked)
    {
        this.Checked = Checked;
    }
}
