package apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment;

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
import android.widget.LinearLayout;
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

import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.ToDo_Modal;
import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Transfer_Request_Model;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;

/**
 * Created by pamrutkar on 08/03/17.
 */

public class TransferRequest_Details extends AppCompatActivity implements OnPress,View.OnClickListener {

    //Git activity_status_track_one
    private Gson gson;
    private SharedPreferences sharedPreferences;
    private String userId;
    private String bearertoken;
    private Transfer_Request_Model transfer_request_model;
    Context context ;
    private int count = 0;
    private int limit = 100;
    private int offsetvalue = 0;
    private RequestQueue queue;
    private String TAG = "TransferRequest_Details";
    private ArrayList<Transfer_Request_Model> Sender_DetailsList, ChildDetailList;
    RelativeLayout tr_imageBtnBack;
    private RecyclerView tr_recyclerView;
    private int levelOfOption = 1;  //  1 is for option and 2 is for size
    private String MCCodeDesc = "";    // code and description
    private String option = "";    // code and description
    private TransferDetailsAdapter transferDetailsAdapter;
    private LinearLayout detailsLinear;
    public static HashMap<Integer, ArrayList<ToDo_Modal>> HashmapList;
    private TextView txt_caseNo,txt_valtotalreqty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferreq_details);
        getSupportActionBar().hide();
        context = this;
        initalise();

        gson = new Gson();
        Sender_DetailsList = new ArrayList<Transfer_Request_Model>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Log.e(TAG, "userID and token" + userId + "and this is" + bearertoken);
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();

        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            requestSenderDetails();
        } else {
            Toast.makeText(context, "Please check network connection...", Toast.LENGTH_SHORT).show();
        }

    }

//    private void requestReceiversChildDetails(final int position) {
//
//        String url = ConstsCore.web_url + "/v1/display/stocktransfer/receiverdetail/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&level=" + levelOfOption + "&MCCodeDesc=" + MCCodeDesc.replaceAll(" ", "%20") + "&option=" + option.replaceAll(" ", "%20");
//
//        Log.e(TAG, "Details Url" + "" + url);
//        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.i(TAG, "Detail api response : " + " " + response);
//                        Log.i(TAG, "Detail api total length" + "" + response.length());
//
//
//                        try {
//                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
//                                Reusable_Functions.hDialog();
//                                Toast.makeText(TransferRequest_Details.this, "no data found", Toast.LENGTH_SHORT).show();
//                                return;
//
//                            } else if (response.length() == limit) {
//                                Log.e(TAG, "promo eql limit");
//                                for (int i = 0; i < response.length(); i++) {
//
//                                    toDo_modal = gson.fromJson(response.get(i).toString(), ToDo_Modal.class);
//                                    ChildDetailList.add(toDo_modal);
//
//
//                                }
//                                offsetvalue = (limit * count) + limit;
//                                count++;
//                                //
//
//                                requestReceiversChildDetails(position);
//
//                            } else if (response.length() < limit) {
//                                Log.e(TAG, "promo /= limit");
//                                for (int i = 0; i < response.length(); i++) {
//                                    toDo_modal = gson.fromJson(response.get(i).toString(), ToDo_Modal.class);
//                                    ChildDetailList.add(toDo_modal);
//                                }
//                                count = 0;
//                                limit = 100;
//                                offsetvalue = 0;
//
//
//                            }
//
//                            HashmapList.put(position, ChildDetailList);
//                            stockPullAdapter.notifyDataSetChanged();
//                            Reusable_Functions.hDialog();
//
//
//                        } catch (Exception e) {
//                            Reusable_Functions.hDialog();
//                            Toast.makeText(context, "data failed...." + e.toString(), Toast.LENGTH_SHORT).show();
//                            Reusable_Functions.hDialog();
//
//                            e.printStackTrace();
//                            Log.e(TAG, "catch...Error" + e.toString());
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Reusable_Functions.hDialog();
//                        Toast.makeText(context, "server not responding..", Toast.LENGTH_SHORT).show();
//                        Reusable_Functions.hDialog();
//                        error.printStackTrace();
//                    }
//                }
//
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("Content-Type", "application/json");
//                params.put("Authorization", "Bearer " + bearertoken);
//                return params;
//            }
//        };
//        int socketTimeout = 60000;//5 seconds
//
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        postRequest.setRetryPolicy(policy);
//        queue.add(postRequest);
//        Reusable_Functions.hDialog();
//
//
//    }


    private void requestSenderDetails() {

        String url = ConstsCore.web_url + "/v1/display/stocktransfer/senderdetail/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&level=" + levelOfOption ;


        Log.e(TAG, "Details Url" + "" + url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, "Detail api response : " + " " + response);
                        Log.i(TAG, "Detail api total length" + "" + response.length());


                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(TransferRequest_Details.this, "no data found", Toast.LENGTH_SHORT).show();
                                return;

                            } else if (response.length() == limit) {
                                Log.e(TAG, "promo eql limit");
                                for (int i = 0; i < response.length(); i++) {

                                    transfer_request_model = gson.fromJson(response.get(i).toString(), Transfer_Request_Model.class);
                                    Sender_DetailsList.add(transfer_request_model);


                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                //

                                requestSenderDetails();

                            } else if (response.length() < limit) {
                                Log.e(TAG, "promo /= limit");
                                for (int i = 0; i < response.length(); i++) {
                                    transfer_request_model = gson.fromJson(response.get(i).toString(), Transfer_Request_Model.class);
                                    Sender_DetailsList.add(transfer_request_model);

                                }


                           }

                            tr_recyclerView.setLayoutManager(new LinearLayoutManager(tr_recyclerView.getContext(), 48 == Gravity.CENTER_HORIZONTAL ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                            tr_recyclerView.setOnFlingListener(null);
                            // new GravitySnapHelper(48).attachToRecyclerView(recyclerView);
                            transferDetailsAdapter = new TransferDetailsAdapter(Sender_DetailsList, context);
                           // MakeHashMap(Sender_DetailsList);
                            tr_recyclerView.setAdapter(transferDetailsAdapter);

                            Reusable_Functions.hDialog();


                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "data failed...." + e.toString(), Toast.LENGTH_SHORT).show();
                            Reusable_Functions.hDialog();

                            e.printStackTrace();
                            Log.e(TAG, "catch...Error" + e.toString());
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

    private void MakeHashMap(ArrayList<ToDo_Modal> detailsList) {

        HashmapList = new HashMap<Integer, ArrayList<ToDo_Modal>>();

        for (int i = 0; i < detailsList.size(); i++) {
            ArrayList<ToDo_Modal> listData = new ArrayList<ToDo_Modal>();
            HashmapList.put(i, listData);
        }

    }

    private void initalise() {

//        String data = getIntent().getExtras().getString("MCCodeDesc");
      //  MCCodeDesc = data;
        tr_recyclerView = (RecyclerView) findViewById(R.id.trasnsferreq_detail_list);
        tr_imageBtnBack = (RelativeLayout)findViewById(R.id.tr_details_imageBtnBack);
        txt_caseNo = (TextView)findViewById(R.id.txt_caseNo);
        txt_valtotalreqty = (TextView)findViewById(R.id.txt_valtotalreqty);
        txt_caseNo.setText("");
        tr_imageBtnBack.setOnClickListener(this);
    }

    public void StartActivity(String CaseNo,double reqQty,Context context) {
        Intent intent = new Intent(context, TransferRequest_Details.class);
        intent.putExtra("caseNo", CaseNo);
        intent.putExtra("reqQty", reqQty);
        context.startActivity(intent);
    }

    @Override
    public void OnPress(int position) {
//        MCCodeDesc = DetailsList.get(position).getMccodeDesc();
//        option = DetailsList.get(position).getLevel();
//        levelOfOption = 2;
//        ChildDetailList = new ArrayList<ToDo_Modal>();
//        if (Reusable_Functions.chkStatus(context)) {
//            Reusable_Functions.sDialog(TransferRequest_Details.this, "Loading....");
//            requestReceiversChildDetails(position);
//        } else {
//            Toast.makeText(context, "Please check network connection...", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tr_details_imageBtnBack :
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}