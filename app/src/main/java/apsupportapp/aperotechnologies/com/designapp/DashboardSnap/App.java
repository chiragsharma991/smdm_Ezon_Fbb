package apsupportapp.aperotechnologies.com.designapp.DashboardSnap;

/**
 * Created by csuthar on 07/07/17.
 */
public class App {

    private int mDrawable;
    private String mName;

    public App(String name, int drawable) {
        mName = name;
        mDrawable = drawable;
    }


    public int getDrawable() {
        return mDrawable;
    }

    public String getName() {
        return mName;
    }
}
