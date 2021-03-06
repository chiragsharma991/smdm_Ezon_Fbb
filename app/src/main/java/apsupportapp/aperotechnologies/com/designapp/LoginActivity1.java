package apsupportapp.aperotechnologies.com.designapp;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.DashboardSnap.SnapDashboardActivity;


/**
 * Created by pamrutkar on 25/05/17.
 */

public class LoginActivity1 extends AppCompatActivity
{
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
    public static ArrayList<String> storelist_data;
    public StoreListAdapter spinnerArrayAdapter;
    private String SelectedStoreCode, userId,storeDescription,geoLevel2Code;
    private boolean firstLogin = false;
    private AlertDialog dialog;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        context = this;
        m_config = MySingleton.getInstance(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        edtUserName = (EditText) findViewById(R.id.edtUserName);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtUserName.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        edtPassword.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        chkKeepMeLogin = (CheckBox) findViewById(R.id.chkKeepMeLogin);
        checkToken();
        chkKeepMeLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                log_flag = ((CheckBox) v).isChecked();

            }
        });
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                uname = edtUserName.getText().toString().trim();
                password = edtPassword.getText().toString().trim();

                if ((uname.equals("") || uname.length() == 0) || (password.equals("") || password.length() == 0))
                {
                    if (uname.equals("") || uname.length() == 0) {
                        Toast.makeText(LoginActivity1.this, "Please enter username", Toast.LENGTH_LONG).show();
                    } else if (password.equals("") || password.length() == 0) {
                        Toast.makeText(LoginActivity1.this, "Please enter password", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if (Reusable_Functions.chkStatus(context))
                    {
                        Reusable_Functions.progressDialog = new ProgressDialog(context);
                        if(!Reusable_Functions.progressDialog.isShowing())
                        {
                            Reusable_Functions.progressDialog.show();
                            Reusable_Functions.progressDialog.setCancelable(false);
                        }
                        Reusable_Functions.progressDialog.setMessage("Authenticating User...");
                        requestLoginAPI();
                    }
                    else
                    {
                        Toast.makeText(LoginActivity1.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }//onCreate

    private void checkToken()
    {
        if (LocalNotificationReceiver.logoutAlarm) {
            View view = findViewById(android.R.id.content);
            snackbar = Snackbar.make(view, "Session has been Log out Please Retry", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    snackbar.dismiss();
                }
            });
            snackbar.setActionTextColor(getResources().getColor(R.color.smdm_actionbar));
            snackbar.show();
            LocalNotificationReceiver.logoutAlarm = false;
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(LocalNotificationReceiver.notId);
        }
    }

    private void requestLoginAPI()
    {
        String url = ConstsCore.web_url + "/v1/login"; //ConstsCore.web_url+ + "/v1/login/userId";
        final JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response == null || response.equals(null)) {
                                Reusable_Functions.hDialog();
                            }
                            // Login condition check for Ezone
                            if (response.getString("geoLeveLDesc").equals("E ZONE"))
                            {
                                Long notificationTime = System.currentTimeMillis() + 1800000; //30 minutes
                                setLocalnotification(context, notificationTime);
                                String username = response.getString("loginName");
                                String password = response.getString("password");
                                userId = response.getString("userId");
                                String bearerToken = response.getString("bearerToken");
                                String geoLeveLDesc = response.getString("geoLeveLDesc");
                                geoLevel2Code = response.getString("geoLevel2Code");
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("username", username);
                                editor.putString("password", password);
                                editor.putString("userId", userId);
                                editor.putString("bearerToken", bearerToken);
                                editor.putString("geoLeveLDesc", geoLeveLDesc);
                                editor.putString("geoLevel2Code",geoLevel2Code);
                                editor.putString("device_id", "");
                                //editor.putString("push_tokken", "");
                                editor.apply();
                                if (log_flag)
                                {
                                    editor.putBoolean("log_flag", true);
                                    editor.putString("authcode", auth_code);
                                    editor.apply();
                                }
                                Intent intent = new Intent(LoginActivity1.this, SnapDashboardActivity.class);
                                intent.putExtra("from", "login");
                                intent.putExtra("BACKTO", "login");
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                                finish();
                            }
                            // Login condition check for Fashion At BB
                            else
                            {
                                if (Reusable_Functions.chkStatus(context)) {
                                    Reusable_Functions.sDialog(context, "Fetching store code...");
                                    SelectedStoreCode = uname;
                                    firstLogin = false;
                                    requestLoginWithStoreAPI();
                                }
                                else
                                {
                                    Toast.makeText(LoginActivity1.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(LoginActivity1.this, "Invalid user" + error.getMessage(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                auth_code = "Basic " + Base64.encodeToString((uname + ":" + password).getBytes(), Base64.NO_WRAP); //Base64.NO_WRAP flag
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

    // Login with store code list
    private void requestLoginWithStoreAPI()
    {
        String url = ConstsCore.web_url + "/v1/login?storeCode=" + SelectedStoreCode.replace(" ", "%20"); //ConstsCore.web_url+ + "/v1/login/userId";
        final JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response == null || response.equals(""))
                            {
                                Reusable_Functions.hDialog();
                                Toast.makeText(LoginActivity1.this, "Invalid user", Toast.LENGTH_LONG).show();
                                return;
                            }
                            // when store code fetched it will go second condition.
                            if (!firstLogin)
                            {
                                String username = response.getString("loginName");
                                String password = response.getString("password");
                                String userId = response.getString("userId");
                                String bearerToken = response.getString("bearerToken");
                                String geoLeveLDesc = response.getString("geoLeveLDesc");
                                geoLevel2Code = response.getString("geoLevel2Code");
                                requestLoginFBBAPI(bearerToken, userId);
                            }
                            else
                            {

                                Long notificationTime = System.currentTimeMillis() + 18000000; //300 minutes
                                setLocalnotification(context, notificationTime);
                                String username = response.getString("loginName");
                                String password = response.getString("password");
                                userId = response.getString("userId");
                                String geoLeveLDesc = response.getString("geoLeveLDesc");
                                geoLevel2Code = response.getString("geoLevel2Code");
                                String bearerToken = response.getString("bearerToken");
                               // userId = userId + "-" + SelectedStoreCode;
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("username", username);
                                editor.putString("password", password);
                                editor.putString("userId", userId);
                                editor.putString("bearerToken", bearerToken);
                                editor.putString("geoLeveLDesc", geoLeveLDesc);
                                editor.putString("geoLevel2Code",geoLevel2Code);
                                editor.putString("storeDescription",storeDescription);
                                editor.putString("device_id", "");
                                //editor.putString("push_tokken", "");
                                editor.apply();
                                if (log_flag)
                                {
                                    editor.putBoolean("log_flag", true);
                                    editor.putString("authcode", auth_code);
                                    editor.apply();
                                }
                                Reusable_Functions.hDialog();
                                Intent intent = new Intent(LoginActivity1.this, SnapDashboardActivity.class);
                                intent.putExtra("from", "login");
                                intent.putExtra("BACKTO", "login");
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                                finish();
                            }
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(context, "data failed...." + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Reusable_Functions.hDialog();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(LoginActivity1.this, "Invalid user", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                auth_code = "Basic " + Base64.encodeToString((uname + ":" + password).getBytes(), Base64.NO_WRAP); //Base64.NO_WRAP flag
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

    // APi for store code list
    private void requestLoginFBBAPI(final String bearerToken, String userId)
    {
        String url = ConstsCore.web_url + "/v1/login/userstores/" + userId +"?geoLevel2Code="+geoLevel2Code; //ConstsCore.web_url+ + "/v1/login/userId";
        JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            if (response.equals("") || response == null)
                            {
                                Reusable_Functions.hDialog();
                                Toast.makeText(LoginActivity1.this, "Invalid user", Toast.LENGTH_LONG).show();
                                return;
                            }
                            storelist_data = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                storelist_data.add(response.getString(i));

                            }
                            Reusable_Functions.hDialog();
                            if (storelist_data.size() == 1) {
                                String value = storelist_data.get(0);
                                SelectedStoreCode = value.trim().substring(0, 4);
                                storeDescription = value.trim();
                                firstLogin = true;
                                requestLoginWithStoreAPI();
                            } else {
                                commentDialog();
                            }
                        }
                        catch (Exception e)
                        {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "data failed...." + e.toString(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(LoginActivity1.this, "Invalid user", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + bearerToken);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }


    @Override
    public void onBackPressed()
    {
        finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void setLocalnotification(Context cont, Long notificationTime)
    {
        AlarmManager alarmManager = (AlarmManager) cont.getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(cont, LocalNotificationReceiver.class);

        PendingIntent broadcast = PendingIntent.getBroadcast(cont, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= 19)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, notificationTime, broadcast);
        else if (Build.VERSION.SDK_INT >= 15)
            alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTime, broadcast);
    }

    private void commentDialog()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v = inflater.inflate(R.layout.storecode_list, null);
        ListView select_storeList = (ListView) v.findViewById(R.id.select_storeList);
        // search function for search store code from list.
        final EditText search = (EditText) v.findViewById(R.id.search_store);
        final ArrayList<String> dublicateStoreList = new ArrayList<>();
        dublicateStoreList.addAll(storelist_data);
        search.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                String searchData = search.getText().toString();
                filterData(searchData, storelist_data, dublicateStoreList);
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });
        spinnerArrayAdapter = new StoreListAdapter(context,storelist_data);
        select_storeList.setAdapter(spinnerArrayAdapter);
        select_storeList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                dialog.dismiss();
                if (Reusable_Functions.chkStatus(context))
                {
                    Reusable_Functions.sDialog(context, "Authenticating user...");
                    String value =  storelist_data.get(position);
                    SelectedStoreCode = value.trim().substring(0, 4);
                    storeDescription = value.trim();
                    firstLogin = true;
                    requestLoginWithStoreAPI();
                }
                else
                {
                    Toast.makeText(LoginActivity1.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setView(v);
        dialog = builder.create();
        dialog.show();
    }

    public void filterData(String query, ArrayList<String> storelist_data, ArrayList<String> dublicateStoreList)
    {
        storelist_data.clear();
        String charText = query.toLowerCase(Locale.getDefault());
        if (charText.length() == 0)
        {
            storelist_data.addAll(dublicateStoreList);
            spinnerArrayAdapter.notifyDataSetChanged();
        }
        else
        {
            for (int i = 0; i < dublicateStoreList.size(); i++)
            {
                if (dublicateStoreList.get(i).toLowerCase(Locale.getDefault()).replace(" ", "").contains(charText))
                {
                    storelist_data.add(dublicateStoreList.get(i));
                }
            }
            spinnerArrayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        Reusable_Functions.hDialog();
        super.onDestroy();

    }


}
