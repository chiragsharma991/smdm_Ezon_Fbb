package apsupportapp.aperotechnologies.com.designapp.Httpcall;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.FreshnessIndex.FreshnessIndexActivity;
import apsupportapp.aperotechnologies.com.designapp.HourlyPerformence.HourlyPerformence;
import apsupportapp.aperotechnologies.com.designapp.MPM.mpm_model;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;

/**
 * Created by csuthar on 10/01/17.
 */

public class ApiPostRequest {

    private final RequestQueue queue;
    private final int id;
    private final JSONObject object;
    private mpm_model mpm_modelClass;
    private HttpPostResponse ResposeInterface;
    private Context context;
    private String bearertoken;
    private String Url;
    private String TAG;
    private Gson gson;
    public static JsonArrayRequest postRequest;


    public ApiPostRequest(Context context,  String Url, String TAG, RequestQueue queue, int id ,JSONObject object) {
        ResposeInterface = (HttpPostResponse) context;
        this.context = context;
        this.Url = Url;
        this.TAG = TAG;
        this.queue = queue;
        this.id = id;
        this.object = object;
        gson = new Gson();
        setApi(context);


    }


    private void setApi(final Context context) {


        String URL = "";
        if (TAG.equals("FreshnessIndex_Ez_Activity")) {

            URL = Url ;

        }
        Log.e(TAG, "final_setApi: URL " + URL);
        postRequest = new JsonArrayRequest(Request.Method.POST, URL, object.toString(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        try {

                            if (response.equals("") || response == null || response.length() == 0) {
                                Reusable_Functions.hDialog();
                                if (TAG.equals("FreshnessIndex_Ez_Activity")) {
                                    FreshnessIndexActivity.listViewFIndex.setVisibility(View.GONE);
                                } else if (TAG.equals("HourlyPerformence")) {
                                    HourlyPerformence.hrl_pi_Process.setVisibility(View.GONE);
                                    ResposeInterface.PostDataNotFound();
                                }
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                return;

                            } else  {

                              //  String result = response.getString("status");
                             //   Toast.makeText(context, "" + result, Toast.LENGTH_LONG).show();
                                Reusable_Functions.hDialog();

                            }


                        } catch (Exception e) {
                            if (TAG.equals("FreshnessIndex_Ez_Activity")) {
                                FreshnessIndexActivity.listViewFIndex.setVisibility(View.GONE);
                            }
                            Log.e(TAG, "onResponse catch: " + e.getMessage());
                            Reusable_Functions.hDialog();


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse : " + error.getMessage());

                        if (TAG.equals("FreshnessIndex_Ez_Activity")) {
                            FreshnessIndexActivity.listViewFIndex.setVisibility(View.GONE);
                        }
                        if (TAG.equals("HourlyPerformence")) {
                            HourlyPerformence.hrl_pi_Process.setVisibility(View.GONE);
                            ResposeInterface.PostDataNotFound();
                        }
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "Server not found...", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Server not found...: " + error.getMessage());
                        error.printStackTrace();
                    }


                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + bearertoken);
                return params;
            }
        };
        int socketTimeout = 30000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);


    }


}




