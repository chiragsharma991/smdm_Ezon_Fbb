package apsupportapp.aperotechnologies.com.designapp;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import java.util.HashMap;


public class BaseLifeCycleCallbacks implements Application.ActivityLifecycleCallbacks {

    static HashMap<String, Integer> activities;
    SharedPreferences sharedPreferences;
    Activity activity;
    BaseLifeCycleCallbacks() {
        activities = new HashMap<String, Integer>();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        this.activity = activity;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

        activities.put(activity.getLocalClassName(), 1);
        applicationStatus();
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
        //map Activity unique class name with 0 on foreground
        activities.put(activity.getLocalClassName(), 0);
        applicationStatus();
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    /**
     * Check if any activity is in the foreground
     */
    private static boolean isBackGround() {
        for (String s : activities.keySet()) {
            if (activities.get(s) == 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Log application status.
     */
    public static boolean applicationStatus() {


        Log.d("ApplicationStatus", "Is application background " + isBackGround());
        return !isBackGround();
    }
}
