package apsupportapp.aperotechnologies.com.designapp.SeasonCatalogue;

import apsupportapp.aperotechnologies.com.designapp.model.FreshnessIndex_Ez_Model;

/**
 * Created by csuthar on 19/04/17.
 */

public class mpm_model extends FreshnessIndex_Ez_Model{

    String productName;
    String mpmPath;


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getMpmPath() {
        return mpmPath;
    }

    public void setMpmPath(String mpmPath) {
        this.mpmPath = mpmPath;
    }
}
