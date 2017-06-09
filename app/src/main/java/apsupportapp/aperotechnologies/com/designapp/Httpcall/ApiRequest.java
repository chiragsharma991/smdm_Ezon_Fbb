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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.FreshnessIndex.FreshnessIndexActivity;
import apsupportapp.aperotechnologies.com.designapp.MPM.mpm_model;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;

/**
 * Created by csuthar on 10/01/17.
 */

public class ApiRequest  {

    private final RequestQueue queue;
    private final int id;
    private int limit=99;
    private int offsetvalue=0;
    private final ArrayList<mpm_model> list;
    private mpm_model mpm_modelClass;
    private HttpResponse ResposeInterface;
    private Context context;
    private String bearertoken;
    private String Url;
    private String TAG;
    private int count = 0;
    private Gson gson;


    public ApiRequest(Context context, String token, String Url, String TAG, RequestQueue queue, mpm_model mpm_modelClass,int id)
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



    private void setApi(final Context context) {


        Reusable_Functions.progressDialog = new ProgressDialog(context);
        if(!Reusable_Functions.progressDialog.isShowing())
        {
            Reusable_Functions.progressDialog.show();
            Reusable_Functions.progressDialog.setCancelable(false);
            Reusable_Functions.progressDialog.setMessage("Loading...");


        }

        String URL = "";
        if(TAG.equals("FreshnessIndex_Ez_Activity")){

            URL=Url+ "&offset=" + offsetvalue + "&limit=" +limit;

        }else{

            URL=Url+ "?offset=" + offsetvalue + "&limit=" +limit;

        }
        Log.e(TAG, "final_setApi: URL "+URL );
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        try {

                            if (response.equals("") || response == null || response.length() == 0 ) {
                                Reusable_Functions.hDialog();
                                if(TAG.equals("FreshnessIndex_Ez_Activity")) {
                                    FreshnessIndexActivity.listViewFIndex.setVisibility(View.GONE);
                                }
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                return;

                            }
                            else if (response.length() == limit) {
                                Log.e(TAG, "promo eql limit");
                                for (int i = 0; i < response.length(); i++) {

                                    mpm_modelClass = gson.fromJson(response.get(i).toString(), mpm_model.class);
                                    list.add(mpm_modelClass);

                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                //

                                setApi(context);

                            } else if (response.length() < limit) {
                                Log.e(TAG, "promo /= limit");
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
                            }
                            Log.e(TAG, "onResponse: "+e.getMessage() );
                            Reusable_Functions.hDialog();



                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(TAG.equals("FreshnessIndex_Ez_Activity")) {
                            FreshnessIndexActivity.listViewFIndex.setVisibility(View.GONE);
                        }
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "Server not found...", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Server not found...: " );
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
        int socketTimeout = 60000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);



    }
}




