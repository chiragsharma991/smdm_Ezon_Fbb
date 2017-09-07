package apsupportapp.aperotechnologies.com.designapp.VisualAssortmentSwipe;

import android.content.Context;
import android.util.Log;
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
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;


public class VisualAssortmentCommentAPI {
    RequestQueue queue;
     Gson gson;

    // Api for post method
    public static void requestSaveComment(String userId, final String bearertoken, JSONObject obj, final Context context) {

        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        RequestQueue queue = null;
        if (queue != null) {
            queue.stop();
        } else if(queue == null)
        {
            queue = new RequestQueue(cache, network);
            queue.start();
        }
       // userId = userId.substring(0,userId.length()-5);
        String url = ConstsCore.web_url + "/v1/save/visualassortmentcomment/" + userId ;
        Log.e("post url :",""+url +"\n"+obj);
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, obj.toString(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("post response :",""+response);
                        try {
                            if (response == null || response.equals("")) {
                                Reusable_Functions.hDialog();
                            } else {
                                Reusable_Functions.hDialog();
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
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", bearertoken);

                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    //Api call for put method
    public static void requestUpdateSaveComment(String userId, final String bearertoken, JSONObject obj, final Context context) {

        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        RequestQueue queue = null;
        if (queue != null) {
            queue.stop();
        } else if(queue == null){
            queue = new RequestQueue(cache, network);
            queue.start();
        }
       // userId = userId.substring(0,userId.length()-5);
        String url = ConstsCore.web_url + "/v1/save/visualassortmentcomment/" + userId;
        Log.e("put request :",""+url + "\n"+obj);
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.PUT, url, obj.toString(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("put response :",""+response);
                        try {
                            if (response == null || response.equals("")) {
                                Reusable_Functions.hDialog();
                            } else {
                                Reusable_Functions.hDialog();
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
                        error.printStackTrace();
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", bearertoken);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    public static String getDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format1.format(cal.getTime());
        System.out.println(formatted);
       return formatted;
    }
}
