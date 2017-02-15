package apsupportapp.aperotechnologies.com.designapp;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.FreshnessIndex.FreshnessIndexDetails;

/**
 * Created by csuthar on 01/02/17.
 */

public class CashingData {

    public   ArrayList<FreshnessIndexDetails> freshnessIndexDetailsArrayList;

    public CashingData(ArrayList<FreshnessIndexDetails> freshnessIndexDetailsArrayList) {
        this.freshnessIndexDetailsArrayList=freshnessIndexDetailsArrayList;
    }

    public CashingData() {

    }

    public ArrayList<FreshnessIndexDetails> getFreshnessIndexDetailsArrayList() {
        return freshnessIndexDetailsArrayList;
    }
}
