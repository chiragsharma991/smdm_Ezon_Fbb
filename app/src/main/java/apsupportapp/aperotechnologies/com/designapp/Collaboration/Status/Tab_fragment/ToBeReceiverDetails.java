package apsupportapp.aperotechnologies.com.designapp.Collaboration.Status.Tab_fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment.OnPress;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;

/**
 * Created by pamrutkar on 15/03/17.
 */
public class ToBeReceiverDetails extends AppCompatActivity implements View.OnClickListener, OnPress {
    private Context context;
    private RecyclerView rec_detail_recycleView;
    private Gson gson;
    private ArrayList<StatusModel> Rec_Status_dtlList, StatusDetailChild;
    private SharedPreferences sharedPreferences;
    private String userId;
    private String bearertoken;
    private RequestQueue queue;
    private int count = 0;
    private int limit = 100;
    private int offsetvalue = 0;
    private int option_level = 1;
    private RelativeLayout status_receiverdetails_imageBtnBack;
    private int caseNo = 0;
    private StatusModel rec_statusModel;
    private ReceiverStatusDetailsAdapter rec_details_Adapter;
    private TextView rec_storeCode, rec_storeCase, rec_storeDesc;
    public static HashMap<Integer, ArrayList<StatusModel>> Rec_StatusHashmapChildList;
    private String option = "";
    private String senderStoreCode = "";
    private String recache;
    private ProgressBar ReceiverDetailProcess;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_be_received_details);
        getSupportActionBar().hide();
        context = this;
        initalise();
        gson = new Gson();
        recache = "true";
        Rec_Status_dtlList = new ArrayList<StatusModel>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();

        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            ReceiverDetailProcess.setVisibility(View.VISIBLE);
            Reusable_Functions.sDialog(context, "Loading data...");
            requestReceiverStatusDetails();
        } else {
            Toast.makeText(context, "Please check network connection...", Toast.LENGTH_SHORT).show();
        }
    }

    private void initalise() {
        rec_detail_recycleView = (RecyclerView) findViewById(R.id.rec_statusDetail_list);
        rec_storeCase = (TextView) findViewById(R.id.rec_status_detailStoreCase);
        rec_storeCode = (TextView) findViewById(R.id.rec_status_detailStoreCode);
        rec_storeDesc = (TextView) findViewById(R.id.rec_status_detailStoreDesc);
        ReceiverDetailProcess = (ProgressBar) findViewById(R.id.receiverDetailProcess);
//        ReceiverDetailProcess.setVisibility(View.VISIBLE);
        status_receiverdetails_imageBtnBack = (RelativeLayout) findViewById(R.id.status_receiverdetails_imageBtnBack);
        status_receiverdetails_imageBtnBack.setOnClickListener(this);
        int data1 = getIntent().getExtras().getInt("CASE");
        senderStoreCode = getIntent().getExtras().getString("CODE");
        String storeDesc = getIntent().getExtras().getString("storeDesc");
        rec_storeCase.setText("Case#" + data1);
        rec_storeCode.setText(senderStoreCode);
        rec_storeDesc.setText(storeDesc);
        caseNo = data1;
    }

    private void requestReceiverStatusDetails() {

        String url = ConstsCore.web_url + "/v1/display/stocktransfer/receivercasestatus/detail/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&level=" + option_level + "&senderStoreCode=" + senderStoreCode + "&caseNo=" + caseNo + "&recache=" + recache;

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                ReceiverDetailProcess.setVisibility(View.GONE);
                                Toast.makeText(ToBeReceiverDetails.this, "no data found", Toast.LENGTH_SHORT).show();
                                return;

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    rec_statusModel = gson.fromJson(response.get(i).toString(), StatusModel.class);
                                    Rec_Status_dtlList.add(rec_statusModel);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;

                                requestReceiverStatusDetails();

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    rec_statusModel = gson.fromJson(response.get(i).toString(), StatusModel.class);
                                    Rec_Status_dtlList.add(rec_statusModel);
                                }
                            }
                            rec_detail_recycleView.setLayoutManager(new LinearLayoutManager(rec_detail_recycleView.getContext(), 48 == Gravity.CENTER_HORIZONTAL ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                            rec_detail_recycleView.setOnFlingListener(null);
                            rec_details_Adapter = new ReceiverStatusDetailsAdapter(Rec_Status_dtlList, context, ReceiverDetailProcess);
                            MakeHashMap(Rec_Status_dtlList);
                            rec_detail_recycleView.setAdapter(rec_details_Adapter);
                            ReceiverDetailProcess.setVisibility(View.GONE);
                            Reusable_Functions.hDialog();

                        } catch (Exception e) {
                            ReceiverDetailProcess.setVisibility(View.GONE);
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "data failed...." + e.toString(), Toast.LENGTH_SHORT).show();
                            Reusable_Functions.hDialog();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ReceiverDetailProcess.setVisibility(View.GONE);
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

    private void MakeHashMap(ArrayList<StatusModel> Rec_Status_dtlList) {

        Rec_StatusHashmapChildList = new HashMap<Integer, ArrayList<StatusModel>>();

        for (int i = 0; i < Rec_Status_dtlList.size(); i++) {
            ArrayList<StatusModel> listData = new ArrayList<StatusModel>();
            Rec_StatusHashmapChildList.put(i, listData);
        }
    }

    public void StartActivity(Context context, int caseNo, String senderStoreCode, String senderStoreDesc) {

        Intent intent = new Intent(context, ToBeReceiverDetails.class);
        intent.putExtra("CASE", caseNo);
        intent.putExtra("CODE", senderStoreCode);
        intent.putExtra("storeDesc", senderStoreDesc);
        context.startActivity(intent);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.status_receiverdetails_imageBtnBack:
                onBackPressed();
                break;
        }
    }


    public void onPress(int position) {
        option_level = 2;
        StatusDetailChild = new ArrayList<StatusModel>();
        option = Rec_Status_dtlList.get(position).getLevel();

        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.sDialog(ToBeReceiverDetails.this, "Loading...");
            ReceiverDetailProcess.setVisibility(View.VISIBLE);
            requestReceiverStatusSubDetails(position);

        } else {
            Toast.makeText(context, "Please check network connection...", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestReceiverStatusSubDetails(final int position) {
        String url = ConstsCore.web_url + "/v1/display/stocktransfer/receivercasestatus/detail/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&level=" + option_level + "&senderStoreCode=" + senderStoreCode + "&caseNo=" + caseNo + "&option=" + option.replaceAll(" ", "%20") + "&recache=" + recache;

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                ReceiverDetailProcess.setVisibility(View.GONE);
                                Toast.makeText(ToBeReceiverDetails.this, "no data found", Toast.LENGTH_SHORT).show();
                                return;

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    rec_statusModel = gson.fromJson(response.get(i).toString(), StatusModel.class);
                                    StatusDetailChild.add(rec_statusModel);

                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestReceiverStatusSubDetails(position);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    rec_statusModel = gson.fromJson(response.get(i).toString(), StatusModel.class);
                                    StatusDetailChild.add(rec_statusModel);
                                }
                            }
                            Rec_StatusHashmapChildList.put(position, StatusDetailChild);
                            rec_details_Adapter.notifyDataSetChanged();
                            Reusable_Functions.hDialog();
                            ReceiverDetailProcess.setVisibility(View.GONE);
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "data failed..." + e.toString(), Toast.LENGTH_SHORT).show();
                            Reusable_Functions.hDialog();
                            ReceiverDetailProcess.setVisibility(View.GONE);
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
                        ReceiverDetailProcess.setVisibility(View.GONE);
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
