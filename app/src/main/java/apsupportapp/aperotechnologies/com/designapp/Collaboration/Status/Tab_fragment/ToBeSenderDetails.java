package apsupportapp.aperotechnologies.com.designapp.Collaboration.Status.Tab_fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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

public class ToBeSenderDetails extends AppCompatActivity implements View.OnClickListener,OnPress {

    private Context context;
    private RecyclerView recyclerView;
    private Gson gson;
    private ArrayList<StatusModel> StatusDetailsList,StatusDetailChild;
    private String TAG="StatusSender_Fragment";
    private SharedPreferences sharedPreferences;
    private String userId;
    private String bearertoken;
    private RequestQueue queue;
    private int count = 0;
    private int limit = 100;
    private int offsetvalue = 0;
    private int levelOfOption=1;
    private RelativeLayout status_senderdetails_imageBtnBack;
    private int caseNo=0;
    private StatusModel statusModel;
    private StatusSenderDetailsAdapter statusSenderDetails;
    private TextView storeCode,storeCase,storedesc;
    public static HashMap<Integer, ArrayList<StatusModel>> StatusHashmapChildList;
    private String option="";
    private String recache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_be_sender_details);
        getSupportActionBar().hide();
        context = this;
        initalise();

        //  PromoListView.addFooterView(getLayoutInflater().inflate(R.layout.list_footer, null));
        gson = new Gson();
        recache ="true";
        StatusDetailsList = new ArrayList<StatusModel>();
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
            requestStatusReceiversDetails();
        } else {
            Toast.makeText(context, "Please check network connection...", Toast.LENGTH_SHORT).show();
        }

    }

    private void requestStatusReceiversDetails()
    {
        String url = ConstsCore.web_url + "/v1/display/stocktransfer/sendercasestatus/detail/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&level=" + levelOfOption+"&senderStoreCode="+userId+"&caseNo="+caseNo+"&recache="+recache;

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
                                Toast.makeText(ToBeSenderDetails.this, "no data found", Toast.LENGTH_SHORT).show();
                                return;

                            } else if (response.length() == limit) {
                                Log.e(TAG, "promo eql limit");
                                for (int i = 0; i < response.length(); i++) {

                                    statusModel = gson.fromJson(response.get(i).toString(), StatusModel.class);
                                    StatusDetailsList.add(statusModel);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;

                                requestStatusReceiversDetails();

                            } else if (response.length() < limit) {
                                Log.e(TAG, "promo /= limit");
                                for (int i = 0; i < response.length(); i++)
                                {
                                    statusModel = gson.fromJson(response.get(i).toString(), StatusModel.class);
                                    StatusDetailsList.add(statusModel);
                                }
                                count = 0;
                                limit = 100;
                                offsetvalue = 0;


                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), 48 == Gravity.CENTER_HORIZONTAL ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                            recyclerView.setOnFlingListener(null);
                            Log.e(TAG, "data  "+StatusDetailsList.get(0).getLevel() );
                            // new GravitySnapHelper(48).attachToRecyclerView(recyclerView);
                            statusSenderDetails = new StatusSenderDetailsAdapter(StatusDetailsList, context);
                            MakeHashMap(StatusDetailsList);
                            recyclerView.setAdapter(statusSenderDetails);

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

    private void MakeHashMap(ArrayList<StatusModel> statusDetailsList) {

        StatusHashmapChildList = new HashMap<Integer, ArrayList<StatusModel>>();

        for (int i = 0; i < statusDetailsList.size(); i++) {
            ArrayList<StatusModel> listData = new ArrayList<StatusModel>();
            StatusHashmapChildList.put(i, listData);
        }
    }

    private void initalise()
    {
        recyclerView = (RecyclerView) findViewById(R.id.statusDetail_list);
        storeCase = (TextView) findViewById(R.id.status_detailStoreCase);
        storeCode = (TextView) findViewById(R.id.status_detailStoreCode);
        storedesc = (TextView) findViewById(R.id.detailStoreDesc);
        status_senderdetails_imageBtnBack = (RelativeLayout)findViewById(R.id.status_senderdetails_imageBtnBack);
        status_senderdetails_imageBtnBack.setOnClickListener(this);
        int data1 = getIntent().getExtras().getInt("CASE");
        String data2 = getIntent().getExtras().getString("CODE");
        String data3 = getIntent().getExtras().getString("DESC");
        storeCase.setText(" " +"Case#"+data1);
        storeCode.setText(data2);
        storedesc.setText(" "+data3);
        caseNo=data1;

    }

    public void StartActivity(Context context) {
        context.startActivity(new Intent(context, ToBeSenderDetails.class));
    }


    @Override
    public void OnPress(int position) {

        levelOfOption=2;
        StatusDetailChild = new ArrayList<StatusModel>();
        option=StatusDetailsList.get(position).getLevel();

        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.sDialog(ToBeSenderDetails.this, "Loading....");
            requestStatusReceiversSubDetails(position);

        } else {
            Toast.makeText(context, "Please check network connection...", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestStatusReceiversSubDetails(final int position)
    {
        String url = ConstsCore.web_url + "/v1/display/stocktransfer/sendercasestatus/detail/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&level=" + levelOfOption+"&senderStoreCode="+userId+"&caseNo="+caseNo+"&option="+option.replaceAll(" ", "%20")+"&recache="+recache;

        Log.e(TAG, "SubDetails Url" + "" + url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, "SubDetails api response : " + " " + response);
                        Log.i(TAG, "SubDetails api total length" + "" + response.length());


                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(ToBeSenderDetails.this, "no data found", Toast.LENGTH_SHORT).show();
                                return;

                            } else if (response.length() == limit) {
                                Log.e(TAG, "promo eql limit");
                                for (int i = 0; i < response.length(); i++) {

                                    statusModel = gson.fromJson(response.get(i).toString(), StatusModel.class);
                                    StatusDetailChild.add(statusModel);


                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                //

                                requestStatusReceiversSubDetails(position);

                            } else if (response.length() < limit) {
                                Log.e(TAG, "promo /= limit");
                                for (int i = 0; i < response.length(); i++) {
                                    statusModel = gson.fromJson(response.get(i).toString(), StatusModel.class);
                                    StatusDetailChild.add(statusModel);
                                }
                                count = 0;
                                limit = 100;
                                offsetvalue = 0;
                            }

                            StatusHashmapChildList.put(position, StatusDetailChild);
                            statusSenderDetails.notifyDataSetChanged();
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

    public void StartActivity(Context context, int data1 ,String data2,String data3) {
        Intent intent = new Intent(context, ToBeSenderDetails.class);
        intent.putExtra("CASE",data1);
        intent.putExtra("CODE",data2);
        intent.putExtra("DESC",data3);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.status_senderdetails_imageBtnBack :
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
