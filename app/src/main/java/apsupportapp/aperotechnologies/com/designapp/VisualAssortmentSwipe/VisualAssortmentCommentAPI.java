package apsupportapp.aperotechnologies.com.designapp.VisualAssortmentSwipe;

import android.content.Context;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.model.VisualAssortComment;

/**
 * Created by hasai on 27/09/16.
 */
public class VisualAssortmentCommentAPI {

    static RequestQueue queue;
    static Gson gson;


    public static void requestSaveComment(String userId, final String bearertoken, JSONObject obj, final Context context) {

        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();

        String url =  ConstsCore.web_url + "/v1/save/visualassortmentcomment/"+userId;

        Log.e("url", " post VASSORT "+url+" ==== "+obj.toString());

        final JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, obj.toString(),
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.i("Login   Response   ", response.toString());
                        try
                        {

                            if(response == null || response.equals(null))
                            {

                                Reusable_Functions.hDialog();

                            }
                            else
                            {
                                Toast.makeText(context, "Data is saved successfully", Toast.LENGTH_SHORT).show();
                                Reusable_Functions.hDialog();

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
                        error.printStackTrace();
                    }
                }

        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {

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

    public static void requestUpdateSaveComment(String userId, final String bearertoken, JSONObject obj, final Context context) {

        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();

        String url =  ConstsCore.web_url + "/v1/save/visualassortmentcomment/"+userId;

        Log.e("url", " put VASSORT "+url+" ==== "+obj.toString());


        final JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.PUT, url, obj.toString(),
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.i("Login   Response   ", response.toString());
                        try
                        {

                            if(response == null || response.equals(null))
                            {
                                Reusable_Functions.hDialog();

                            }
                            else
                            {
                                Toast.makeText(context, "Data is updated successfully", Toast.LENGTH_SHORT).show();
                                Reusable_Functions.hDialog();
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
                        error.printStackTrace();
                    }
                }

        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {

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

    public static String getDate()
    {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        //System.out.println(cal.getTime());


        String formatted = format1.format(cal.getTime());
        System.out.println(formatted);

        return formatted;
    }
}
