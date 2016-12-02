package apsupportapp.aperotechnologies.com.designapp.model;

import java.util.List;


public class ListProdLevel6 {

    String subdept;
    String category;
    String planclass;
    String Brand;
    String BrandClass;
    List prdlevel6;

    public String getBrandClass() {
        return BrandClass;
    }

    public void setBrandClass(String brandClass) {
        BrandClass = brandClass;
    }

    public List getPrdlevel6() {
        return prdlevel6;
    }

    public void setPrdlevel6(List prdlevel6) {
        this.prdlevel6 = prdlevel6;
    }

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

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getBrand() {
        return Brand;
    }


}
