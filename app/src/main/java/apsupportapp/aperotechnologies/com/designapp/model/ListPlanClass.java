package apsupportapp.aperotechnologies.com.designapp.model;


import java.util.List;

public class ListPlanClass {

    String subdept;
    String category;
    List planclass;

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

    public void setPlanclass(List planclass) {
        this.planclass = planclass;
    }

    public List getPlanclass() {
        return planclass;
    }

}
