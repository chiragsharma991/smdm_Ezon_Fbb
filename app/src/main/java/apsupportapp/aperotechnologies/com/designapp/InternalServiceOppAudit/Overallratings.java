package apsupportapp.aperotechnologies.com.designapp.InternalServiceOppAudit;

import org.json.JSONObject;

/**
 * Created by hasai on 21/09/17.
 */

public class Overallratings extends JSONObject{

    String header;
    String code;
    String smiley;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getSmiley() {
        return smiley;
    }

    public void setSmiley(String smiley) {
        this.smiley = smiley;
    }
}
