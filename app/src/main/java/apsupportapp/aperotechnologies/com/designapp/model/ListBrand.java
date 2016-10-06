package apsupportapp.aperotechnologies.com.designapp.model;

import java.util.List;

/**
 * Created by hasai on 04/10/16.
 */
public class ListBrand {


    String subdept;
    String category;
    String planclass;
    List Brand;

    public void setSubdept(String subdept) {
        this.subdept = subdept;
    }

    public String getSubdept() {
        return subdept;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setPlanclass(String planclass) {
        this.planclass = planclass;
    }

    public String getPlanclass() {
        return planclass;
    }

    public void setBrand(List brand) {
        Brand = brand;
    }

    public List getBrand() {
        return Brand;
    }
}
