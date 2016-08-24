package apsupportapp.aperotechnologies.com.designapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
Button btnLogin;
    EditText edtUserName,edtPassword;
    String uname,password;
    String auth_code;
    Context context;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        context=this;
        setContentView(R.layout.activity_login);
        edtUserName=(EditText)findViewById(R.id.edtUserName);
        edtPassword=(EditText)findViewById(R.id.edtPassword);

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();


        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                uname = edtUserName.getText().toString().trim();
                password = edtPassword.getText().toString().trim();

                //web_api = txt_api.getText().toString().trim();


                if ((uname.equals("") || uname.length() == 0) || (password.equals("") || password.length() == 0))
                {
                    if (uname.equals("") || uname.length() == 0)
                    {
                        Toast.makeText(LoginActivity.this, "Please Enter User Name", Toast.LENGTH_LONG).show();
                    } else if (password.equals("") || password.length() == 0) {
                        Toast.makeText(LoginActivity.this, "Please Enter Password", Toast.LENGTH_LONG).show();
                    }

                } else {
                    if (Reusable_Functions.chkStatus(context))
                    {
                        Reusable_Functions.sDialog(context,"validating user");
                        requestLoginAPI();
                    } else {
                        Toast.makeText(LoginActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
                    }



                }
            }
        });
    }//onCreate


    private void requestLoginAPI()
    {
        String url="https://ra.manthan.com/v1/login/userId";

        JSONObject jsonobj = new JSONObject();
        try {
              jsonobj.put("loginName", uname);//"User_4974_DM1");
              jsonobj.put("password", password);//"123456");
            } catch (JSONException e)
              {
                e.printStackTrace();
              }

        Log.e("json"," "+jsonobj.toString());


        final JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, jsonobj.toString(),
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.i("Login   Response   ", response.toString());
                        try
                        {
                           // JSONObject mainObject = new JSONObject(response);
                            String username = response.getString("loginName");
                            String password = response.getString("password");
                            String userId=response.getString("userId");
                            Log.e(" id  url",""+userId+"     "+username+"    " +password);
                            Reusable_Functions.hDialog();

                            if(username != null || !username.equals(null))
                            {
                                Intent intent = new Intent(LoginActivity.this, DashBoardActivity.class);
                                intent.putExtra("userId",userId);
                                startActivity(intent);
                            }

                        }
                        catch(Exception e)
                        {
                            Log.e("Exception e",e.toString() +"");
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
                        Toast.makeText(LoginActivity.this,"Invalid User",Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }

        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                String auth_code = "Basic " + Base64.encodeToString((uname+":"+password).getBytes(), Base64.NO_WRAP); //Base64.NO_WRAP flag
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



}
