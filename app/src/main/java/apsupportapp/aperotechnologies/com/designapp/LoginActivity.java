package apsupportapp.aperotechnologies.com.designapp;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText edtUserName, edtPassword;
    String uname, password;
    String auth_code;
    CheckBox chkKeepMeLogin;
    Context context;

    RequestQueue queue;
    MySingleton m_config;
    boolean log_flag = false;
    SharedPreferences sharedPreferences;
    private LinearLayout LinearLogin;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        context = this;
        m_config = MySingleton.getInstance(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        edtUserName = (EditText) findViewById(R.id.edtUserName);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        chkKeepMeLogin = (CheckBox) findViewById(R.id.chkKeepMeLogin);
        LinearLogin = (LinearLayout) findViewById(R.id.linearLogin);

        checkToken();


        chkKeepMeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log_flag = ((CheckBox) v).isChecked();

            }
        });

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                uname = edtUserName.getText().toString().trim();
                password = edtPassword.getText().toString().trim();

                if ((uname.equals("") || uname.length() == 0) || (password.equals("") || password.length() == 0)) {
                    if (uname.equals("") || uname.length() == 0) {
                        Toast.makeText(LoginActivity.this, "Please enter username", Toast.LENGTH_LONG).show();
                    } else if (password.equals("") || password.length() == 0) {
                        Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_LONG).show();
                    }

                } else {
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.sDialog(context, "Authenticating user...");
                        requestLoginAPI();
                    } else {

                        Toast.makeText(LoginActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }//onCreate

    private void checkToken()
    {
        Log.e("TAG", "checkToken: ");
        if(LocalNotificationReceiver.logoutAlarm)
        {
            View view=findViewById(android.R.id.content);
             snackbar = Snackbar.make(view, "Session has been Log out Please Retry", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    snackbar.dismiss();

                }
            });
            snackbar.setActionTextColor(getResources().getColor(R.color.smdm_actionbar));
            snackbar.show();
            LocalNotificationReceiver.logoutAlarm=false;
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(LocalNotificationReceiver.notId);


        }
    }

    private void requestLoginAPI() {
        String url = ConstsCore.web_url + "/v1/login"; //ConstsCore.web_url+ + "/v1/login/userId";

        final JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Login   Response   ", response.toString());
                        try {
                            if (response == null || response.equals(null)) {
                                Reusable_Functions.hDialog();

                            }
                            Long notificationTime = System.currentTimeMillis() + 1800000; //30 minutes
                            Log.e("notificationTime", "onResponse: "+notificationTime );
                            setLocalnotification(context, notificationTime);
                            String username = response.getString("loginName");
                            String password = response.getString("password");
                            String userId = response.getString("userId");
                            String bearerToken = response.getString("bearerToken");

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("username", username);
                            editor.putString("password", password);
                            editor.putString("userId", userId);
                            editor.putString("bearerToken", bearerToken);
                            editor.apply();
                            if (log_flag) {
                                editor.putBoolean("log_flag", true);
                                editor.putString("authcode", auth_code);

                                Log.e("authcode ", " --- " + username + " " + password + " " + auth_code);
                                editor.apply();

                            }
                            Reusable_Functions.hDialog();
                            Intent intent = new Intent(LoginActivity.this, DashBoardActivity.class);
                            intent.putExtra("from", "login");
                            intent.putExtra("BACKTO", "login");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                        } catch (Exception e) {
                            Log.e("Exception e", e.toString() + "");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(LoginActivity.this, "Invalid user", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                auth_code = "Basic " + Base64.encodeToString((uname + ":" + password).getBytes(), Base64.NO_WRAP); //Base64.NO_WRAP flag
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
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void setLocalnotification(Context cont, Long notificationTime) {
        AlarmManager alarmManager = (AlarmManager) cont.getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(cont, LocalNotificationReceiver.class);

        PendingIntent broadcast = PendingIntent.getBroadcast(cont, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= 19)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, notificationTime, broadcast);
        else if (Build.VERSION.SDK_INT >= 15)
            alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTime, broadcast);
    }
}
