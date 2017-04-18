package apsupportapp.aperotechnologies.com.designapp.StoreInspection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;

/**
 * Created by pamrutkar on 17/04/17.
 */

public class InspectionHistoryActivity extends AppCompatActivity implements View.OnClickListener
{
    RelativeLayout rel_insp_history_btnBack;
    RecyclerView rv_insp_history;
    private RecyclerView.LayoutManager mLayoutManager;
    private String userId;
    private String bearertoken;
    private RequestQueue queue;
    private SharedPreferences sharedPreferences;
    private Context context;
    private int offset =0, limit = 50, count = 0;
    private InspectionBeanClass inspectionBeanClass;
    private ArrayList<InspectionBeanClass> inspectionArrayList;
    private Gson gson;
    private Insp_History_Adapter insp_history_adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_history);
        context = this;
        getSupportActionBar().hide();
        initialise();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Log.e("" ,"userID and token" + userId + "and this is" + bearertoken);
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();
        inspectionArrayList = new ArrayList<InspectionBeanClass>();
        if (Reusable_Functions.chkStatus(context))
        {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            requestInspectionSummary();
        }
        else
        {
            Toast.makeText(context, "Please check network connection...", Toast.LENGTH_SHORT).show();
        }
    }

    private void initialise()
    {
        rel_insp_history_btnBack = (RelativeLayout)findViewById(R.id.insp_history_btnback);
        rv_insp_history = (RecyclerView)findViewById(R.id.rv_insp_history);
        mLayoutManager = new LinearLayoutManager(this);
        rv_insp_history.setLayoutManager(mLayoutManager);
        rel_insp_history_btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.insp_history_btnback :
                onBackPressed();
                break;
        }
    }

    private void requestInspectionSummary()
    {
        String url = ConstsCore.web_url + "/v1/display/storeinspectionsummary/" + userId  ;
        Log.e("Inspection History Url" ,"" + url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        Log.e("Inspection History api response : " , "" +response);
                        Log.e("Inspection History total length :" , "" +response.length());
                        try
                        {
//                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
//                                Reusable_Functions.hDialog();
//                                Toast.makeText(InspectionHistoryActivity.this, "no data found", Toast.LENGTH_SHORT).show();
//                               // return;
//
//                            } else if (response.length() == limit) {
//                                Log.e("", "promo eql limit");
//                                for (int i = 0; i < 1; i++) {

//                                    inspectionBeanClass = gson.fromJson(response.get(i).toString(), InspectionBeanClass.class);
//                                    inspectionArrayList.add(inspectionBeanClass);
//                                    JSONObject productName1 = response.getJSONObject(i);
//                                    String inspectorName = productName1.getString("inspectorName");
//                                    String comment = productName1.getString("comments");
//                                    String storeCode = productName1.getString("storeCode");
//                                    int inspectionId = productName1.getInt("inspectionId");

                                    inspectionBeanClass = new InspectionBeanClass();
                                    inspectionBeanClass.setInspectorName("Rohit");
                                    inspectionBeanClass.setStoreCode("4813");
                                    inspectionBeanClass.setInspectionId(14212);
                                    inspectionArrayList.add(inspectionBeanClass);

                           //     }
//                                offset = (limit * count) + limit;
//                                count++;
//
//                                requestInspectionSummary();
//
//                            } else if (response.length() < limit) {
//                                Log.e("", "promo /= limit");
//                                for (int i = 0; i < response.length(); i++) {
//                                    inspectionBeanClass = gson.fromJson(response.get(i).toString(), InspectionBeanClass.class);
//                                    inspectionArrayList.add(inspectionBeanClass);
//
//                                    JSONObject productName1 = response.getJSONObject(i);
//                                    String inspectorName = productName1.getString("inspectorName");
//                                    String comment = productName1.getString("comments");
//                                    String storeCode = productName1.getString("storeCode");
//                                    int inspectionId = productName1.getInt("inspectionId");
//
//                                    inspectionBeanClass = new InspectionBeanClass();
//                                    inspectionBeanClass.setInspectorName("Rohit");
//                                    inspectionBeanClass.setStoreCode("4813");
//                                    inspectionBeanClass.setInspectionId(14212);
//                                    inspectionArrayList.add(inspectionBeanClass);
//                                }
//                            }
                            rv_insp_history.setLayoutManager(new LinearLayoutManager(rv_insp_history.getContext(), 48 == Gravity.CENTER_HORIZONTAL ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                            rv_insp_history.setOnFlingListener(null);
                            // new GravitySnapHelper(48).attachToRecyclerView(recyclerView);
                            insp_history_adapter = new Insp_History_Adapter(inspectionArrayList, context);
                            rv_insp_history.setAdapter(insp_history_adapter);

                        }
                        catch (Exception e)
                        {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "data failed...." + e.toString(), Toast.LENGTH_SHORT).show();
                            Reusable_Functions.hDialog();
                            e.printStackTrace();
                            Log.e("", "catch...Error" + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "server not responding..", Toast.LENGTH_SHORT).show();
                        Reusable_Functions.hDialog();
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
        Reusable_Functions.hDialog();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }

//    public void StartActivity(Context context)
//    {
//        Intent intent = new Intent(context, InspectionDetailsActivity.class);
//        context.startActivity(intent);
//    }
}
