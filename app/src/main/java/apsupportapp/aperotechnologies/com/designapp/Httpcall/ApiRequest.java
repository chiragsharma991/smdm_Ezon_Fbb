package apsupportapp.aperotechnologies.com.designapp.Httpcall;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.AvailabilityAndNotifyHO.ProductAvailability_Reports;
import apsupportapp.aperotechnologies.com.designapp.FreshnessIndex.FreshnessIndexActivity;
import apsupportapp.aperotechnologies.com.designapp.HourlyPerformence.HourlyPerformence;
import apsupportapp.aperotechnologies.com.designapp.SeasonCatalogue.mpm_model;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;

/**
 * Created by csuthar on 10/01/17.
 */

public class ApiRequest  {

    private final RequestQueue queue;
    private final int id;
    private int limit=100;
    private int offsetvalue=0;
    private final ArrayList<mpm_model> list;
    private mpm_model mpm_modelClass;
    private HttpResponse ResposeInterface;
    private Context context;
    private String bearertoken;
    private String Url;
    private String TAG;
    private String data;
    private int count = 0;
    private Gson gson;
    public static JsonArrayRequest getRequest;

    // this constructer is useful for Activity
    public ApiRequest(Context context, String token, String Url, String TAG, RequestQueue queue, mpm_model mpm_modelClass, int id)
    {
        ResposeInterface= (HttpResponse)context;
        this.context=context;
        bearertoken=token;
        this.Url=Url;
        this.TAG=TAG;
        this.queue=queue;
        this.id=id;
        this.list=new ArrayList<>();
        this.mpm_modelClass=mpm_modelClass;
        gson=new Gson();
        setApi(context);
    }

    // this constructer is useful for fragment
    public ApiRequest(Context context, String token, String Url, String TAG, RequestQueue queue, mpm_model mpm_modelClass, int id, HttpResponse httpResponse, String data) {
        ResposeInterface= (HttpResponse)httpResponse;
        this.context=context;
        bearertoken=token;
        this.Url=Url;
        this.TAG=TAG;
        this.queue=queue;
        this.id=id;
        this.data = data;
        this.list=new ArrayList<>();
        this.mpm_modelClass=mpm_modelClass;
        gson=new Gson();
        setApi(context);
    }


    private  void setApi(final Context context) {

        /*    Reusable_Functions.progressDialog = new ProgressDialog(context);
        if(!Reusable_Functions.progressDialog.isShowing())
        {
            Reusable_Functions.progressDialog.show();
            Reusable_Functions.progressDialog.setCancelable(false);
            Reusable_Functions.progressDialog.setMessage("Loading...");


        }*/

        String URL = "";
        if(TAG.equals("FreshnessIndex_Ez_Activity")){

            URL=Url+ "&offset=" + offsetvalue + "&limit=" +limit;

        }
        else if(TAG.equals("HourlyPerformence")){

            URL=Url+ "&offset=" + offsetvalue + "&limit=" +limit;

        }
        else if(TAG.equals("customerFeedbackReport")){

            URL=Url+ "&offset=" + offsetvalue + "&limit=" +limit;

        }
        else{

            URL=Url+ "&offset=" + offsetvalue + "&limit=" +limit;

        }
        getRequest = new JsonArrayRequest(Request.Method.GET, URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public  void onResponse(JSONArray response) {
                        Reusable_Functions.hDialog();

                        try {

                            if (response.equals("") || response == null || response.length() == 0 ) {
                                if(TAG.equals("FreshnessIndex_Ez_Activity")) {
                                    FreshnessIndexActivity.listViewFIndex.setVisibility(View.GONE);
                                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                }
                                else if(TAG.equals("HourlyPerformence")) {
                                    HourlyPerformence.hrl_pi_Process.setVisibility(View.GONE);
                                    ResposeInterface.nodatafound();
                                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                }else{
                                //    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                    Reusable_Functions.hDialog();

                                    ResposeInterface.nodatafound();
                                    return;
//                                    if(data.equals("Feedback with callback")) {
//                                        Toast.makeText(context, "no data found for Feedback with Callback", Toast.LENGTH_SHORT).show();
//                                    }
//                                    else if(data.equals("Feedback")){
//                                        Toast.makeText(context, "no data found for Feedback", Toast.LENGTH_SHORT).show();
//
//                                    }
//                                    else{
//                                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
//
//                                    }


                                }


                            }
                            else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    mpm_modelClass = gson.fromJson(response.get(i).toString(), mpm_model.class);
                                    list.add(mpm_modelClass);

                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                //

                                setApi(context);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++)
                                {
                                    mpm_modelClass = gson.fromJson(response.get(i).toString(), mpm_model.class);
                                    list.add(mpm_modelClass);
                                }
                                ResposeInterface.response(list,id);
                                count = 0;
                                limit = 100;
                                offsetvalue = 0;
                              //  Reusable_Functions.hDialog();
                            }
                        } catch (Exception e) {
                            if(TAG.equals("FreshnessIndex_Ez_Activity")) {
                                FreshnessIndexActivity.listViewFIndex.setVisibility(View.GONE);
                            }else{
                                ResposeInterface.nodatafound();
                            }
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "data failed...", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if(TAG.equals("FreshnessIndex_Ez_Activity")) {
                            FreshnessIndexActivity.listViewFIndex.setVisibility(View.GONE);
                        }
                        if(TAG.equals("HourlyPerformence")) {
                            HourlyPerformence.hrl_pi_Process.setVisibility(View.GONE);
                            ResposeInterface.nodatafound();
                        }else{
                            ResposeInterface.nodatafound();
                        }
                        Reusable_Functions.hDialog();
                     //   Toast.makeText(context, "Server not found...", Toast.LENGTH_SHORT).show();
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
        getRequest.setRetryPolicy(policy);
        queue.add(getRequest);
    }
}




