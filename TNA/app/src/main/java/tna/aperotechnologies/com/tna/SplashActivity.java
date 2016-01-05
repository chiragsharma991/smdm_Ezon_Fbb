package tna.aperotechnologies.com.tna;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by hasai on 21/12/15.
 */

/*this function gives call to main launcher function (i.e. Splash Activity)*/

public class SplashActivity extends Activity
{
    SharedPreferences sharedpreferences;
    Configuration_Parameter m_config;
    SQLiteDatabase sqldb;
    DBHelper helper;
    ReUsableFunctions reuse = new ReUsableFunctions();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //setContentView(R.layout.activity_splash_screen);
        //Initialize preferences
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        m_config=Configuration_Parameter.getInstance();

        /****** Create Thread that will sleep for 5 seconds *************/
        Thread background = new Thread()
        {
            public void run() {
                try {

                    sleep(3 * 1000);

                    //checks whether user has set remain loggedin
                    if (sharedpreferences.getBoolean("LoginFlag", false) == true)
                    {
                        Log.e("loginflag is true", "");
                        // when user have selected remain login
                        //reuse.requesttnas("http://114.143.218.114:91", SplashActivity.this);
                        reuse.createTNAElements(SplashActivity.this);

                    }
                    else if (sharedpreferences.getBoolean("LoginFlag", false) == false)
                    {
                        Log.e("loginflag is false","");
                        // when user have not selected remain login
                        Intent i = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(i);
                    }


                    finish();

                }
                catch(Exception e)
                {

                }
            }
        };
        // start thread
        background.start();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}

