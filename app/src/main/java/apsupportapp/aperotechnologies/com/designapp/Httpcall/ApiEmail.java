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

public class ApiEmail {

    public static RequestQueue queue;
    public static String url;


    public static void req_email_API(String userId, Context context, final String bearertoken, String from, final String storecode) {

        final JSONObject[] jObj = {null};
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        final Gson gson = new Gson();
        if (from.equals("ourstoreservices"))
        {
           // String ourstoreservices = "Our Store Services";
           // ourstoreservices = ourstoreservices.replace(" ", "%20").replace("&", "%26");
            int ourstoreservices = 8;
            url = "https://smdm.manthan.com/v1/notification/email/" + userId + "?storecode=" + storecode + "&templateId=" + ourstoreservices;

        } else if (from.equals("productavailability"))
        {
//            String productavailability = "Product Availability & Notify Me";
//            productavailability = productavailability.replace(" ", "%20").replace("&", "%26");
              int productavailability = 3;
            url = "https://smdm.manthan.com/v1/notification/email/" + userId + "?storecode=" + storecode + "&templateId=" + productavailability;

        } else if (from.equals("policyexchange"))
        {
//            String policyexchange = "Policy- Exchange, Refund";
//            policyexchange = policyexchange.replace(" ", "%20");
            int policyexchange = 5;
            url = "https://smdm.manthan.com/v1/notification/email/" + userId + "?storecode=" + storecode + "&templateId=" + policyexchange;

        }
        else if (from.equals("pricepromotion")) {
//            String pricepromotion = "Price & Promotions";
//            pricepromotion = pricepromotion.replace(" ", "%20").replace("&", "%26");
            int pricepromotion = 6;
            url = "https://smdm.manthan.com/v1/notification/email/" + userId + "?storecode=" + storecode + "&templateId=" + pricepromotion;


        } else if (from.equals("productquality"))
        {
//            String productquality = "ProductQualityRange";
            int productquality = 4;
            url = "https://smdm.manthan.com/v1/notification/email/" + userId + "?storecode=" + storecode + "&templateId=" + productquality;

        } else if (from.equals("supervisorstaff"))
        {
//            String supervisorstaff = "Supervisor & Staff";
//            supervisorstaff = supervisorstaff.replace(" ", "%20").replace("&", "%26");
            int supervisorstaff = 7;
            url = "https://smdm.manthan.com/v1/notification/email/" + userId + "?storecode=" + storecode + "&templateId=" + supervisorstaff;

        }
        else
        {
            url = "https://smdm.manthan.com/v1/notification/email/" + userId + "?storecode="+storecode+ "&emailtype=EMAIL_TYPE_1";
        }
        // String url = "https://smdm.manthan.com/v1/notification/email/"+userId+"?storecode=2663&emailtype=EMAIL_TYPE_1";
        StringRequest emailrequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        //  Reusable_Functions.displayToast(context, response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


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
