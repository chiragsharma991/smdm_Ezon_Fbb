package apsupportapp.aperotechnologies.com.designapp;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import java.util.HashMap;

/**
 * Created by hasai on 28/06/16.
 */

public class BaseLifeCycleCallbacks implements Application.ActivityLifecycleCallbacks {


    static HashMap<String, Integer> activities;
    SharedPreferences sharedPreferences;
    Activity activity;


    BaseLifeCycleCallbacks() {
        activities = new HashMap<String, Integer>();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        //Log.e("sharedpre"," "+sharedpreferences);
        this.activity = activity;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        //map Activity unique class name with 1 on foreground
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
        if (isBackGround()) {
            //Do something if the application is in background
            return false;
        }
        else
        {

//            Long timeout = sharedPreferences.getLong("timeout",0);
//
//            Log.e("timeout"," "+timeout+ "===="+ System.currentTimeMillis());
//            if(timeout != 0)
//            {
//
//                if(System.currentTimeMillis() >= timeout)
//                {

//                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
//                    alertDialogBuilder
//                            .setTitle("Exit")
//                            .setMessage("Your session is expired.")
//                            .setCancelable(false)
//                            .setNegativeButton("No", null)
//                            .setPositiveButton("Yes",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            //finish();
//                                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                                            editor.clear();
//                                            editor.commit();
//                                            Intent intent = new Intent(activity, LoginActivity.class);
//                                            activity.startActivity(intent);
//                                            activity.finish();
//
//                                        }
//                                    });
//
//                    alertDialogBuilder.show();
//                }
//
//            }

//
            return true;
        }
    }
}
