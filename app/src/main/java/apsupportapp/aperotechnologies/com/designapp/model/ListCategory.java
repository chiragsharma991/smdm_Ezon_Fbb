package apsupportapp.aperotechnologies.com.designapp.model;

import java.util.List;

/**
 * Created by pamrutkar on 03/10/16.
 */
public class ListCategory {


    String subdept;
    List category;
    List PlanClass;

    public void setSubdept(String subdept) {
        this.subdept = subdept;
    }

    public String getSubdept() {
        return subdept;
    }

    public void setCategory(List category) {
        this.category = category;
    }

    public List getCategory() {
        return category;
    }

    public void setPlanClass(List planClass) {
        PlanClass = planClass;
    }

    public List getPlanClass() {
        return PlanClass;
    }
}
