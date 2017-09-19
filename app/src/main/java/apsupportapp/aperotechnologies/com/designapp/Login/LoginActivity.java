package apsupportapp.aperotechnologies.com.designapp.Login;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.DB_operation.DatabaseHandler;
import apsupportapp.aperotechnologies.com.designapp.DashboardSnap.SnapDashboardActivity;
import apsupportapp.aperotechnologies.com.designapp.LocalNotificationReceiver;
import apsupportapp.aperotechnologies.com.designapp.MySingleton;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.model.Login_StoreList;


/**
 * Created by pamrutkar on 15/09/17.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnLogin;
    EditText edtUserName, edtPassword;
    String uname, password,recache,bearToken ;
    String auth_code;
    CheckBox chkKeepMeLogin;
    Context context;
    RequestQueue queue;
    MySingleton m_config;
    boolean log_flag = false;
    SharedPreferences sharedPreferences;
    private String TAG = "LoginActivity";
    private ArrayList<Login_StoreList> loginStoreArray;
    private String SelectedStoreCode, userId, storeDescription, geoLevel2Code;
    private boolean firstLogin = false;
    private Login_StoreList login_storeList;
    private Snackbar snackbar;
    Gson gson;
    private DatabaseHandler db;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        context = this;
        recache = "true";
        loginStoreArray = new ArrayList<Login_StoreList>();
        NetworkAccess();
        initialise();
        checkToken();

    }

    private void NetworkAccess()
    {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        m_config = MySingleton.getInstance(this);
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();
    }

    private void initialise()
    {
        db = new DatabaseHandler(context);
        edtUserName = (EditText) findViewById(R.id.edtUserName);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtUserName.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        edtPassword.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        chkKeepMeLogin = (CheckBox) findViewById(R.id.chkKeepMeLogin);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        chkKeepMeLogin.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

    }

    private void checkToken()
    {
        if (LocalNotificationReceiver.logoutAlarm)
        {
            View view = findViewById(android.R.id.content);
            snackbar = Snackbar.make(view, "Session has been Log out Please Retry", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.setActionTextColor(getResources().getColor(R.color.smdm_actionbar));
            snackbar.show();
            LocalNotificationReceiver.logoutAlarm = false;
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(LocalNotificationReceiver.notId);
            Log.e("TAG", "notification id:-- " + LocalNotificationReceiver.notId);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.chkKeepMeLogin:
                log_flag = ((CheckBox) v).isChecked();
                break;
            case R.id.btnLogin:
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                uname = edtUserName.getText().toString().trim();
                password = edtPassword.getText().toString().trim();

                if ((uname.equals("") || uname.length() == 0) || (password.equals("") || password.length() == 0))
                {
                    if (uname.equals("") || uname.length() == 0) {
                        Toast.makeText(context, "Please enter username", Toast.LENGTH_LONG).show();
                    } else if (password.equals("") || password.length() == 0) {
                        Toast.makeText(context, "Please enter password", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.progressDialog = new ProgressDialog(context);
                        if (!Reusable_Functions.progressDialog.isShowing()) {
                            Reusable_Functions.progressDialog.show();
                            Reusable_Functions.progressDialog.setCancelable(false);
                        }
                        Reusable_Functions.progressDialog.setMessage("Authenticating User...");
                        requestLoginAPI();
                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
                    }
                }
        }
    }

    private void requestLoginAPI()
    {
        String url = ConstsCore.web_url + "/v1/login"; //ConstsCore.web_url+ + "/v1/login/userId";
        Log.e(TAG, "requestLoginAPI: "+url);
        final JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Login Response   ", response.toString());
                        try {
                            if (response == null || response.equals(null)) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_LONG).show();

                            } else
                            {
                                login_storeList = gson.fromJson(response.toString(), Login_StoreList.class);

                            }
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("username", login_storeList.getLoginName());
                            editor.putString("password", login_storeList.getPassword());
                            editor.putString("userId", login_storeList.getUserId());
                            userId = login_storeList.getUserId();
                            editor.putString("bearerToken", login_storeList.getBearerToken());
                            bearToken =  login_storeList.getBearerToken();
                            editor.putString("geoLeveLDesc", login_storeList.getGeoLeveLDesc());
                            editor.putString("geoLevel2Code",login_storeList.getGeoLevel2Code());
                            editor.putString("device_id", "");
                            editor.apply();
                            if (Reusable_Functions.chkStatus(context))
                            {
                                Reusable_Functions.sDialog(context, "Loading...");
                                requestUserStore();
                            }
                            else
                            {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
                            }

                        }
                        catch (Exception e)
                        {
                            Reusable_Functions.hDialog();
                            Log.e("Exception e", e.toString() + "");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "Invalid user" + error.getMessage(), Toast.LENGTH_LONG).show();
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

    private void requestUserStore()
    {
        String url = ConstsCore.web_url + "/v1/login/userstoresNew/" + userId +"?geoLevel2Code="+login_storeList.getGeoLevel2Code()+"&recache="+recache; //ConstsCore.web_url+ + "/v1/login/userId";
        Log.e("Login", "requestUserStore: " + url);
        JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        Log.e(TAG, "requestUserStore -***- onResponse: "+response);
                        try
                        {
                            if (response.equals("") || response == null)
                            {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "Invalid user", Toast.LENGTH_LONG).show();
                                return;
                            }
                            else
                            {
                                for (int i=0 ; i<response.length();i++)
                                {
                                    login_storeList = gson.fromJson(response.get(i).toString(),Login_StoreList.class);
                                    loginStoreArray.add(login_storeList);
                                }
                                //database storeage
                                db.db_AddData(loginStoreArray);
                                Log.e(TAG, "onResponse: "+login_storeList.getIsMultiStore().equals("NO"));
                                if(response.length() == 1 && login_storeList.getIsMultiStore().equals("NO")) // for single store save storecode
                                {
                                    if (Reusable_Functions.chkStatus(context))
                                    {
                                        Reusable_Functions.hDialog();
                                        Reusable_Functions.sDialog(context, "Fetching store code and concept...");
                                        requestUserStoreConcept();
                                    }
                                    else
                                    {
                                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
                                    }
                                }
                                else // for multiple store save concept (geoLevelDesc)
                                {
                                    Log.e(TAG, "requestUserStore - in else: " );
                                    if (Reusable_Functions.chkStatus(context))
                                    {
                                        Reusable_Functions.hDialog();
                                        Reusable_Functions.sDialog(context, "Fetching store code and concept...");
                                        requestUserStoreConcept();
                                    }
                                    else
                                    {
                                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
                                    }
                                }
                                Reusable_Functions.hDialog();



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
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "Invalid user", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + bearToken);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void requestUserStoreConcept()
    {

        String url = ConstsCore.web_url + "/v1/login/userstoreorconcept/" + userId +"?geoLevel2Code="+login_storeList.getGeoLevel2Code()+"&lobId="+login_storeList.getLobId(); //ConstsCore.web_url+ + "/v1/login/userId";
        Log.e("Login", "requestUserStoreConcept: " + url);
        JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        Log.e(TAG, "requestUserStoreConcept - onResponse: "+response);
                        try {
                            if (response.equals("") || response == null)
                            {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_LONG).show();
                                return;
                            }
                            else
                            {
                                String value = "";
                                for (int i=0 ; i<response.length();i++)
                                {
                                 value  = response.get(i).toString();
                                }
                                Log.e(TAG, "requestUserStoreConcept - onResponse: "+value);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("value", value);
                                editor.apply();

                            }
                            Reusable_Functions.hDialog();
                            Intent intent = new Intent(context, SnapDashboardActivity.class);
                            intent.putExtra("from", "login");
                            intent.putExtra("BACKTO", "login");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            finish();
                        }
                        catch (Exception e)
                        {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "data failed...." + e.toString(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Reusable_Functions.hDialog();
                        Log.e(TAG, "onErrorResponse: "+error);
                        Toast.makeText(context, "Invalid user", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + bearToken);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }
}
