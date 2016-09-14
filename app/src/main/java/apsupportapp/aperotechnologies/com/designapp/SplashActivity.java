package apsupportapp.aperotechnologies.com.designapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import apsupportapp.aperotechnologies.com.designapp.R;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity
{
    SharedPreferences sharedPreferences;
    ProgressBar progressbar;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        context = this;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        DisplayMetrics metrics = getResources().getDisplayMetrics();

// return 0.75 if it's LDPI
// return 1.0 if it's MDPI
// return 1.5 if it's HDPI
// return 2.0 if it's XHDPI
// return 3.0 if it's XXHDPI
// return 4.0 if it's XXXHDPI


        if(sharedPreferences.getBoolean("log_flag",false) == true) {
            if (Reusable_Functions.chkStatus(context))
            {
                if (progressbar != null) {
                    progressbar.setVisibility(View.VISIBLE);
                    progressbar.setIndeterminate(true);
                    progressbar.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.MULTIPLY);
                }
            }
            else
            {
                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
            }

        }

        Thread background = new Thread()
        {
            public void run()
            {
                try
                {
                    // Thread will sleep for 5 seconds
                    sleep(3*1000);

                    Log.e("chk"," "+(sharedPreferences.getBoolean("log_flag",false) == true));
                    if(sharedPreferences.getBoolean("log_flag",false) == true)
                    {

                        requestLoginAPI();

                    }
                    else if(sharedPreferences.getBoolean("log_flag",false) == false)
                    {
                        Intent i = new Intent(SplashActivity.this,LoginActivity.class);
                        startActivity(i);
                        //progressbar.setVisibility(View.GONE);
                        finish();
                    }


                }
                catch (Exception e)
                {
                }
            }
        };
        // start thread
        background.start();


    }

    private void requestLoginAPI()
    {
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();

        String url = ConstsCore.web_url+"/v1/login";
        final String username = sharedPreferences.getString("username","");
        final String password = sharedPreferences.getString("password","");
        final String auth_code = sharedPreferences.getString("authcode","");

        Log.e("auth---code "," --- "+username+" "+password+" "+auth_code);

        final JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.i("Login   Response   ", response.toString());
                        try
                        {
                            if(response == null || response.equals(null))
                            {
                                Reusable_Functions.hDialog();

                            }

                            String bearerToken = response.getString("bearerToken");
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("bearerToken",bearerToken);
                            editor.apply();
                            Log.i("bearerToken   ", bearerToken);

                            Intent i = new Intent(SplashActivity.this,DashBoardActivity.class);
                            i.putExtra("from","splash");
                            startActivity(i);
                            progressbar.setVisibility(View.GONE);
                            finish();



                        }
                        catch(Exception e)
                        {
                            Log.e("Exception e",e.toString() +"");
                            e.printStackTrace();
                            Reusable_Functions.hDialog();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Reusable_Functions.hDialog();
                        error.printStackTrace();
                    }
                }

        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                //String auth_code = "Basic " + Base64.encodeToString((uname+":"+password).getBytes(), Base64.NO_WRAP); //Base64.NO_WRAP flag
                Log.i("Auth Code", auth_code);
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", auth_code);
                return params;


            }
        };
        int socketTimeout = 60000;//5 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}
