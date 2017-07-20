package apsupportapp.aperotechnologies.com.designapp.model;

/**
 * Created by csuthar on 10/01/17.
 */

public class EtlStatus
{
    String typeOfETL;
    String lastETLDate;


    public void setTypeOfETL(String typeOfETL) {
        this.typeOfETL = typeOfETL;
    }

    public String getTypeOfETL() {
        return typeOfETL;
    }

    public void setLastETLDate(String lastETLDate) {
        this.lastETLDate = lastETLDate;
    }

    public String getLastETLDate() {
        return lastETLDate;
    }
}
