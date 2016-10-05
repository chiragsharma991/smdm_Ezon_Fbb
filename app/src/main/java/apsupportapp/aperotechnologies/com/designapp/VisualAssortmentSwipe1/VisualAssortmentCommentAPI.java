package apsupportapp.aperotechnologies.com.designapp.VisualAssortmentSwipe1;

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

    public static void requestFetchComment(final String userId, final String bearertoken, final String articleOption, final String check, final String value, final Context context) {

        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();

        gson = new Gson();

        String url =  ConstsCore.web_url + "/v1/display/visualassortmentcomment/" + userId+"?articleOption="+articleOption.replaceAll(" ", "%20").replaceAll("&","%26");
        Log.e("url", " URL VASSORT "+url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("response visualassortment: ", " "+response.length());

                        try
                        {
                            if (response.equals(null) || response == null || response.length() == 0)
                            {

                                    JSONObject obj = new JSONObject();

                                    try
                                    {
                                        obj.put("articleOption",articleOption);


                                        if(check.equals("like"))
                                        {
                                            obj.put("likeDislikeFlg",value);

                                        }
                                        else  if(check.equals("dislike"))
                                        {
                                            obj.put("likeDislikeFlg", value);

                                        }
                                        else if(check.equals("comment"))
                                        {

                                            obj.put("feedback", value);


                                        }
                                        else if(check.equals("order"))
                                        {
                                            obj.put("sizeSet",value);

                                        }


                                    }
                                    catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }


                                    VisualAssortmentCommentAPI.requestSaveComment(userId, bearertoken, obj, context);


                            }

                            else
                            {
                                VisualAssortComment visualAssortComment = null;
                                for (int i = 0; i < response.length(); i++)
                                {
                                    try
                                    {
                                        visualAssortComment = gson.fromJson(response.get(i).toString(), VisualAssortComment.class);
                                    }
                                    catch (JSONException e1)
                                    {
                                        e1.printStackTrace();
                                    }
                                }

                                    JSONObject obj = new JSONObject();

                                    try
                                    {
                                        obj.put("articleOption",visualAssortComment.getArticleOption());

                                        if(check.equals("like"))
                                        {
                                            obj.put("likeDislikeFlg", value);
                                            obj.put("feedback",visualAssortComment.getFeedback());
                                            obj.put("sizeSet",visualAssortComment.getSizeSet());

                                        }
                                        else  if(check.equals("dislike"))
                                        {
                                            obj.put("likeDislikeFlg", value);
                                            obj.put("feedback",visualAssortComment.getFeedback());
                                            obj.put("sizeSet",visualAssortComment.getSizeSet());
                                        }
                                        else if(check.equals("comment"))
                                        {
                                            obj.put("likeDislikeFlg", visualAssortComment.getLikeDislikeFlg());
                                            obj.put("feedback", value);
                                            obj.put("sizeSet",visualAssortComment.getSizeSet());
                                        }
                                        else if(check.equals("order"))
                                        {
                                            obj.put("likeDislikeFlg", visualAssortComment.getLikeDislikeFlg());
                                            obj.put("feedback",visualAssortComment.getFeedback());
                                            obj.put("sizeSet", value);
                                        }


                                    }
                                    catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }

                                    requestUpdateSaveComment(userId, bearertoken, obj, context);


                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            //Toast.makeText(context, "no data found", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no data found", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization","Bearer "+bearertoken);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }


    public static void requestSaveComment(String userId, final String bearertoken, JSONObject obj, final Context context) {

        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();

        String url =  ConstsCore.web_url + "/v1/save/visualassortmentcomment/"+userId;
        try {
            Log.e("url", " post VASSORT "+url+" ==== "+obj.getString("articleOption"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

        try {
            Log.e("url", " put VASSORT "+url+" ==== "+obj.getString("articleOption"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
