package tna.aperotechnologies.com.tna;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* This function is used for Login purpose*/

public class LoginActivity extends Activity {
    EditText txt_user_name, txt_password;//EditText for username and password
    Button btn_login;// Button for login
    String uname, password;
    String URL; // url for API
    Configuration_Parameter m_config; //Call to singleton pattern function
    CheckBox chk_logged_in; // Checkbox for remain logged in functionality
    Boolean log_flag = false; // flag set for remain logged in functionality
    SharedPreferences sharedpreferences;
    RequestQueue queue;
    ReUsableFunctions reuse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        URL = "http://114.143.218.114:91/";

        //Initialize Configuration Parameter
        m_config = Configuration_Parameter.getInstance();

        //Initialize ReUsableFunctions
        reuse = new ReUsableFunctions();

        //Request Queue for WS Call
        queue = Volley.newRequestQueue(LoginActivity.this);

        //Enable Strict mode for webservice call
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Initialize preferences
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);

        txt_user_name = (EditText) findViewById(R.id.user_name);
        txt_password = (EditText) findViewById(R.id.user_password);
        chk_logged_in = (CheckBox) findViewById(R.id.chk_logged_in);
        btn_login = (Button) findViewById(R.id.btn_login);

        txt_user_name.setText(sharedpreferences.getString("Username",""));
        txt_password.setText(sharedpreferences.getString("Password", ""));


        //Code to clear Focus from editText
        txt_user_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_NEXT) || (actionId == EditorInfo.IME_ACTION_NONE)) {
                    txt_user_name.clearFocus();
                    txt_password.performClick();

                }
                return false;
            }
        });


        //Code to clear Focus from editText
        txt_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_NEXT) || (actionId == EditorInfo.IME_ACTION_NONE)) {
                    txt_password.clearFocus();
                    LoginClick();

                }
                return false;
            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LoginClick();
            }
        });

        //Event executed in case of keep me logged in check status is changed
        chk_logged_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    //In italize shared preference
                    log_flag = true;
                } else {
                    log_flag = false;
                    //Initalize shared preference
                }
            }
        });
    }

    public void LoginClick(){
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        uname = txt_user_name.getText().toString().trim();
        password = txt_password.getText().toString().trim();

        //Log.e("uname"," "+uname+" "+"password"+" "+password);
        if ((uname.equals("") || uname.length() == 0 || uname.equals(null)) || (password.equals("") || password.length() == 0 || password.equals(null))) {
            if (uname.equals("") || uname.length() == 0 || uname.equals(null)) {
                Toast.makeText(LoginActivity.this, "Please Enter UserName", Toast.LENGTH_LONG).show();
            } else if (password.equals("") || password.length() == 0 || password.equals(null)) {
                Toast.makeText(LoginActivity.this, "Please Enter Password", Toast.LENGTH_LONG).show();
            }

        } else {

            if (chkStatus()) {
                requestLogin(uname, password);
            } else {
                Toast.makeText(LoginActivity.this, "Please check your network connectivity", Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }).create().show();

    }


    //Volley WS call for User Authentication
    public void requestLogin(final String uname, final String password) {


        m_config.pDialog = new ProgressDialog(LoginActivity.this);
        m_config.pDialog.setMessage("Please Wait...");
        m_config.pDialog.setCancelable(false);
        if (!m_config.pDialog.isShowing()) {
            m_config.pDialog.show();
        }

        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        String url = URL + "api/tna/user/authenticate";
        JSONObject obj = new JSONObject();

        try {
            obj.put("Login", uname);
            obj.put("Password", password);
            //Log.e("obj", "" + obj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String desc = response.getString("Description");
                            //checks whether user is valid user or not
                            if (desc.equals("Valid User")) {
                                new ProcessLogin().execute(response);

                            } else {
                                Toast.makeText(LoginActivity.this, "Invalid Login Credentials", Toast.LENGTH_LONG).show();
                                if(m_config.pDialog != null){
                                    if (m_config.pDialog.isShowing()){
                                        m_config.pDialog.dismiss();

                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(m_config.pDialog != null){
                            if (m_config.pDialog.isShowing())
                                m_config.pDialog.dismiss();
                        }

                        error.printStackTrace();

                        // TODO Auto-generated method stub
                        if (error instanceof TimeoutError) {
                            Toast.makeText(LoginActivity.this, "Server Not Available.", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NoConnectionError || error instanceof NetworkError) {
                            //TODO
                            if (error.getMessage().toString().contains("No authentication challenges found")) {

                                Toast.makeText(LoginActivity.this, "Authorization Error", Toast.LENGTH_LONG).show();
                            } else if (error.getMessage().toString().contains("UnknownHostException")) {

                                Toast.makeText(LoginActivity.this, "Invalid Web API", Toast.LENGTH_LONG).show();
                            } else if (error.getMessage().toString().contains("ConnectException") || error.getMessage().toString().contains("SocketException")) {
                                if (error.getMessage().toString().contains("Connection refused")) {

                                    Toast.makeText(LoginActivity.this, "Invalid Web API", Toast.LENGTH_LONG).show();
                                } else if (error.getMessage().toString().contains("isConnected failed")) {

                                    Toast.makeText(LoginActivity.this, "Check Your Network Connection", Toast.LENGTH_LONG).show();
                                } else if (error.getMessage().toString().contains("ConnectException")) {

                                    Toast.makeText(LoginActivity.this, "Check Your Network Connection", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Check Your Network Connection", Toast.LENGTH_LONG).show();
                            }
                        } else if (error instanceof AuthFailureError) {
                            //TODO

                            Toast.makeText(LoginActivity.this, "Invalid Login Credentials", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            //TODO
                            Toast.makeText(LoginActivity.this, "Server Not Available", Toast.LENGTH_LONG).show();
                        } else if (error instanceof VolleyError) {
                            if (error.getMessage().toString().contains("Bad URL")) {
                                Toast.makeText(LoginActivity.this, "Invalid Web API", Toast.LENGTH_LONG).show();
                            } else {

                                Toast.makeText(LoginActivity.this, "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                            }
                        }


                    }
                }
        );
        int socketTimeout = 5000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        req.setRetryPolicy(policy);
        queue.add(req);
    }


    private class ProcessLogin extends AsyncTask<JSONObject, Void, Void> {
        @Override
        protected Void doInBackground(JSONObject... params) {
            JSONObject response = params[0];

            try {
                //Parse Login WS Response
                int UserId = response.getInt("UserId");

                //Initialoze shared preference
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putInt("UserId", UserId);
                editor.putString("Username", uname);
                editor.putString("Password", password);

                // checks flag is set/not set for remain logged in
                if (log_flag.equals(true)) {
                    Log.e("true", "");
                    editor.putBoolean("LoginFlag", true);
                } else {
                    Log.e("false", " ");
                    editor.putBoolean("LoginFlag", false);
                }
                editor.commit();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            //gives call to TNA Settings and Filters Screen
            reuse.requesttnas("http://114.143.218.114:91", LoginActivity.this);
        }
    }
    //Check Network availability
    public boolean chkStatus()
    {
        final ConnectivityManager connMgr = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnectedOrConnecting ()  || mobile.isConnectedOrConnecting ())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}


