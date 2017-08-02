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
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.AvailabilityAndNotifyHO.ProductAvailability_Feedback;
import apsupportapp.aperotechnologies.com.designapp.FreshnessIndex.FreshnessIndexActivity;
import apsupportapp.aperotechnologies.com.designapp.HourlyPerformence.HourlyPerformence;

import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;

/**
 * Created by csuthar on 10/01/17.
 */

public class ApiPostRequest {

    private final RequestQueue queue;
    private final int id;
    private final JSONObject object;
    private HttpPostResponse ResposeInterface;
    private Context context;
    private String bearertoken;
    private String URL;
    private String TAG;
    public static JsonObjectRequest postRequest;

    public ApiPostRequest(Context context, String bearertoken, String Url, String TAG, RequestQueue queue, int id, JSONObject object,HttpPostResponse ResposeInterface) {
        this.ResposeInterface =ResposeInterface;
        this.context = context;
        this.URL = Url;
        this.TAG = TAG;
        this.queue = queue;
        this.bearertoken = bearertoken;
        this.id = id;
        this.object = object;
        setApi(context);


    }


    private void setApi(final Context context) {



        Log.e(TAG, "final_setApi: URL " + URL);
        Reusable_Functions.sDialog(context,"Submitting data…");
        postRequest = new JsonObjectRequest(Request.Method.POST, URL, object.toString(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {

                            if (response.equals("") || response == null || response.length() == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                ResposeInterface.PostDataNotFound();
                                return;

                            } else  {

                                Reusable_Functions.hDialog();
                                ResposeInterface.PostResponse(response);

                            }


                        } catch (Exception e) {
                            Toast.makeText(context, "data failed...", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "onResponse catch: " + e.getMessage());
                            Reusable_Functions.hDialog();
                            ResposeInterface.PostDataNotFound();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "Server not found...", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Server not found...: " + error.getMessage());
                        ResposeInterface.PostDataNotFound();

                        error.printStackTrace();
                    }


                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", bearertoken);
                //  params.put("Content-Type", "application/json");
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        int socketTimeout = 30000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);


    }


}




