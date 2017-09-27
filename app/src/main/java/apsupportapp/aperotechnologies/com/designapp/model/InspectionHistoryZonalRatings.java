package apsupportapp.aperotechnologies.com.designapp.model;

/**
 * Created by hasai on 26/09/17.
 */

public class InspectionHistoryZonalRatings {


    String zone_name;
    String avg_rating;
    String audit_done;
    String count_fg_audit;
    String count_external_audit;

    public String getZone_name() {
        return zone_name;
    }

    public void setZone_name(String zone_name) {
        this.zone_name = zone_name;
    }

    public String getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(String avg_rating) {
        this.avg_rating = avg_rating;
    }

    public String getAudit_done() {
        return audit_done;
    }

    public void setAudit_done(String audit_done) {
        this.audit_done = audit_done;
    }

    public String getCount_fg_audit() {
        return count_fg_audit;
    }

    public void setCount_fg_audit(String count_fg_audit) {
        this.count_fg_audit = count_fg_audit;
    }

    public String getCount_external_audit() {
        return count_external_audit;
    }

    public void setCount_external_audit(String count_external_audit) {
        this.count_external_audit = count_external_audit;
    }
}
