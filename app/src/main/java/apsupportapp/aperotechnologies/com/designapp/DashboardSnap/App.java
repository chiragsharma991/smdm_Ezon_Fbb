package apsupportapp.aperotechnologies.com.designapp.DashboardSnap;

/**
 * Created by csuthar on 07/07/17.
 */
public class App {

    private int mDrawable;
    private String kpiId;
    private String mName;




    public App(String name, int drawable, String kpiId) {
        mName = name;
        mDrawable = drawable;

        this.kpiId = kpiId;

    }

    public String getKpiId() {
        return kpiId;
    }


    public int getDrawable() {
        return mDrawable;
    }

    public String getName() {
        return mName;
    }
}
