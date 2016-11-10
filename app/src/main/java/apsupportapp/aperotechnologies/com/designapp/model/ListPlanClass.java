package apsupportapp.aperotechnologies.com.designapp.model;


import java.util.List;

public class ListPlanClass {

    String dept;
    String category;
    List planclass;

    public void setSubdept(String subdept) {
        this.dept = subdept;
    }

    public String getSubdept() {
        return dept;
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
