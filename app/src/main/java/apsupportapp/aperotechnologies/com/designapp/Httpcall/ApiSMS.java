package apsupportapp.aperotechnologies.com.designapp.Httpcall;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rkanawade on 14/08/17.
 */

public class ApiSMS {
    public static RequestQueue queue;


    public static void req_sms_API(String userId, String customerNumber, final String bearertoken, Context context) {

        final JSONObject[] jObj = {null};
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        final Gson gson = new Gson();
        String url = "https://smdm.manthan.com/v1/notification/sms/" + userId + "?smstype=SMS_TYPE_1&mobilenumber=" + customerNumber;
        Log.e("sms url ", "" + url);

        StringRequest smsrequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("sms api ", " " + response.toString());
                        //req_email_API();
                        // Reusable_Functions.displayToast(context, response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("sms api ", " " + error.toString());

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", bearertoken);
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        int socketTimeout = 30000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        smsrequest.setRetryPolicy(policy);
        queue.add(smsrequest);

    }

}
