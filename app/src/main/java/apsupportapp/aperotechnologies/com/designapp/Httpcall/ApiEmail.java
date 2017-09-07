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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rkanawade on 14/08/17.
 */

public class ApiEmail {

    public static RequestQueue queue;
    public static String url;


    public static void req_email_API(String userId, Context context, final String bearertoken, String from) {

        final JSONObject[] jObj = {null};
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        final Gson gson = new Gson();
        if(from.equals("ourstoreservices"))
        {
             String sapmle_url = "https://smdm.manthan.com/v1/notification/email/"+userId+"?storecode=2663&emailtype=Our Store Services";
             url = sapmle_url.replaceAll(" ", "%20");
        }
        else if(from.equals("productavailability"))
        {
            String sapmle_url = "https://smdm.manthan.com/v1/notification/email/"+userId+"?storecode=2663&emailtype=Product Availability & Notify Me";
            url = sapmle_url.replaceAll(" ", "%20");
         //   url = sample.replaceAll("&", "%26");

        }
        else if(from.equals("policyexchange"))
        {
            String sapmle_url = "https://smdm.manthan.com/v1/notification/email/"+userId+"?storecode=2663&emailtype=Policy- Exchange, Refund";
            url = sapmle_url.replaceAll(" ", "%20");
//            String sample2 = sapme1.replaceAll(" ", "%2D");
//            url = sample2.replaceAll(" ", "%2C");

        }
        else if(from.equals("pricepromotion"))
        {
            String sapmle_url = "https://smdm.manthan.com/v1/notification/email/"+userId+"?storecode=2663&emailtype=Price & Promotions";
            String sample = sapmle_url.replaceAll(" ", "%20");
            url = sample.replaceAll("&", "%26");


        }
        else if(from.equals("productquality"))
        {
            url = "https://smdm.manthan.com/v1/notification/email/"+userId+"?storecode=2663&emailtype=ProductQualityRange";
            //url = sapmle_url.replaceAll(" ", "%20");
        }
        else if(from.equals("supervisorstaff"))
        {
            String sapmle_url = "https://smdm.manthan.com/v1/notification/email/"+userId+"?storecode=2663&emailtype=Supervisor & Staff";
            String sample = sapmle_url.replaceAll(" ", "%20");
            url = sample.replaceAll("&", "%26");

        }
        else
        {
            url = "https://smdm.manthan.com/v1/notification/email/"+userId+"?storecode=2663&emailtype=EMAIL_TYPE_1";
        }
       // String url = "https://smdm.manthan.com/v1/notification/email/"+userId+"?storecode=2663&emailtype=EMAIL_TYPE_1";
        Log.e("email url ",""+url);

        StringRequest emailrequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("email api "," "+response.toString());
                        //  Reusable_Functions.displayToast(context, response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("sms api "," "+error.toString());

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
        emailrequest.setRetryPolicy(policy);
        queue.add(emailrequest);


    }

}
