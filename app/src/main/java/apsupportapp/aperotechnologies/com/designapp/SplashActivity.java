package apsupportapp.aperotechnologies.com.designapp;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

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

import com.crashlytics.android.Crashlytics;

import apsupportapp.aperotechnologies.com.designapp.StoreInspection.InspectionBeginActivity;
import io.fabric.sdk.android.Fabric;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    ProgressBar progressbar;
    Context context;
    private Snackbar snackbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        context = this;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Log.e("---", " " + getResources().getDisplayMetrics().density);

        if (sharedPreferences.getBoolean("log_flag", false) == true) {
            if (Reusable_Functions.chkStatus(context))
            {
                if (progressbar != null) {
                    progressbar.setVisibility(View.VISIBLE);
                    progressbar.setIndeterminate(true);
                    progressbar.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.MULTIPLY);
                }
            } else {
                progressbar.setVisibility(View.GONE);
                checkNetwork();
              //  Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
            }
        }
        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 5 seconds
                    sleep(3 * 1000);
                    Log.e("chk", " " + (sharedPreferences.getBoolean("log_flag", false) == true));
                    if (sharedPreferences.getBoolean("log_flag", false) == true) {
                        if (Reusable_Functions.chkStatus(context)) {
                            requestLoginAPI();
                        } else
                        {
                            //Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
                        }
                    } else if (sharedPreferences.getBoolean("log_flag", false) == false) {
                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                } catch (Exception e) {
                }
            }
        };
        // start thread
        background.start();
    }

    private void checkNetwork() {
        Log.e("TAG", "Check Network: ");
//        if(LocalNotificationReceiver.logoutAlarm)
//        {
            View v=findViewById(android.R.id.content);
        snackbar = Snackbar.make(v,"Check your network ", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(Reusable_Functions.chkStatus(context))
                    {
                        Log.e("", "onClick: "+"in dismiss condition" );
                        progressbar.setVisibility(View.VISIBLE);
                        requestLoginAPI();
                    }else
                    {
                        Log.e("", "onClick: "+"in if  condition" );
                        checkNetwork();

                    }
                }
            });
            snackbar.setActionTextColor(getResources().getColor(R.color.smdm_actionbar));
            snackbar.show();
//            LocalNotificationReceiver.logoutAlarm=false;
//            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.cancel(LocalNotificationReceiver.notId);


       // }
    }

    private void requestLoginAPI() {
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();

        String url = ConstsCore.web_url + "/v1/login";
        final String username = sharedPreferences.getString("username", "");
        final String password = sharedPreferences.getString("password", "");
        final String auth_code = sharedPreferences.getString("authcode", "");

        Log.e("auth---code ", " --- " + username + " " + password + " " + auth_code);

        final JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Login   Response   ", response.toString());
                        try
                        {
                            if (response == null || response.equals(null))
                            {
                                Reusable_Functions.hDialog();
                            }
                            String bearerToken = response.getString("bearerToken");
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("bearerToken", bearerToken);
                            editor.apply();
                            Log.i("bearerToken   ", bearerToken);
                            Intent i = new Intent(SplashActivity.this, DashBoardActivity.class);
                            i.putExtra("from", "splash");
                            startActivity(i);
                            progressbar.setVisibility(View.GONE);
                            finish();
                        }
                        catch (Exception e)
                        {
                            Log.e("Exception e", e.toString() + " ");
                            e.printStackTrace();
                            Reusable_Functions.hDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

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
    protected void onDestroy() {
        super.onDestroy();
    }
}
