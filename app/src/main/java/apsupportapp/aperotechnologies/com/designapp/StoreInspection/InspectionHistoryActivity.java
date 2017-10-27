package apsupportapp.aperotechnologies.com.designapp.StoreInspection;


import android.content.Context;

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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;

/**
 * Created by pamrutkar on 17/04/17.
 */

public class InspectionHistoryActivity extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout rel_insp_history_btnBack;
    RecyclerView rv_insp_history;
    private RecyclerView.LayoutManager mLayoutManager;
    private String userId;
    private String bearertoken;
    private RequestQueue queue;
    private SharedPreferences sharedPreferences;
    Context context = this;
    private int offset = 0, limit = 50, count = 0;
    private InspectionBeanClass inspectionBeanClass;
    private ArrayList<InspectionBeanClass> inspectionArrayList;
    private Gson gson;
    private String recache, storeCode, store_Code;
    private Insp_History_Adapter insp_history_adapter;
    JsonArrayRequest postRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_history);

        recache = "true";
        getSupportActionBar().hide();
        initialise();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
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
        else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private void initialise()
    {
        if(getIntent().getExtras().getString("storeCode") != null )
        {
            storeCode = getIntent().getExtras().getString("storeCode");
            store_Code = storeCode.substring(0,4);
        }
        rel_insp_history_btnBack = (RelativeLayout) findViewById(R.id.insp_history_btnback);
        rv_insp_history = (RecyclerView) findViewById(R.id.rv_insp_history);
        mLayoutManager = new LinearLayoutManager(this);
        rv_insp_history.setLayoutManager(mLayoutManager);
        rel_insp_history_btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.insp_history_btnback:
                onBackPressed();
                break;
        }
    }

    private void requestInspectionSummary() {
        String url = ConstsCore.web_url + "/v1/display/storeinspectionsummary/" + userId + "?storeCode=" + store_Code + "&recache=" + recache;
        postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    inspectionBeanClass = gson.fromJson(response.get(i).toString(), InspectionBeanClass.class);
                                    inspectionArrayList.add(inspectionBeanClass);

                                }
                                offset = (limit * count) + limit;
                                count++;

                                requestInspectionSummary();

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    inspectionBeanClass = gson.fromJson(response.get(i).toString(), InspectionBeanClass.class);
                                    inspectionArrayList.add(inspectionBeanClass);

                                }
                            }
                            rv_insp_history.setLayoutManager(new LinearLayoutManager(rv_insp_history.getContext(), 48 == Gravity.CENTER_HORIZONTAL ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                            rv_insp_history.setOnFlingListener(null);
                            // new GravitySnapHelper(48).attachToRecyclerView(recyclerView);
                            insp_history_adapter = new Insp_History_Adapter(inspectionArrayList, context, store_Code);
                            rv_insp_history.setAdapter(insp_history_adapter);
                            Reusable_Functions.hDialog();

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "data failed...." + e.toString(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
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

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
