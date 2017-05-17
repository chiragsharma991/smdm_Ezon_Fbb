package apsupportapp.aperotechnologies.com.designapp.ExpiringPromo;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import apsupportapp.aperotechnologies.com.designapp.BestPerformersPromo.FilterActivity;
import apsupportapp.aperotechnologies.com.designapp.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.google.gson.Gson;
import org.json.JSONArray;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;

import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.RunningPromo.RecyclerViewPositionHelper;


import apsupportapp.aperotechnologies.com.designapp.RunningPromo.RunningPromoSummaryAdapter;
import apsupportapp.aperotechnologies.com.designapp.RunningPromo.clickChild;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;

public class ExpiringPromoActivity extends AppCompatActivity implements View.OnClickListener,clickChild {

    TextView storecode, storedesc, promoval1, promoval2;
    RelativeLayout imageback, imagefilter;
    RunningPromoListDisplay ExpiringPromoListDisplay;
    private SharedPreferences sharedPreferences;
    String userId, bearertoken;
    String TAG = "ExpiringPromoActivity";
    private int count = 0;
    private int limit = 100;
    private int offsetvalue = 0;
    Context context = this;
    private RequestQueue queue;
    private Gson gson;
    RecyclerView ExpireListView;
    ArrayList<RunningPromoListDisplay> ExpireList,summary_list;
    private RunningPromoSummaryAdapter runningPromoSummaryAdapter;
    private int focusposition = 0;
    private int totalItemCount=0;
    int prevState = RecyclerView.SCROLL_STATE_IDLE;
    int currentState = RecyclerView.SCROLL_STATE_IDLE;
    private ExpiringPromoSnapAdapter expiringPromoSnapAdapter;
    private LinearLayout Expiring_promo,Expiring_summary;
    private RecyclerView PromoList_summary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expiring_promo);
        getSupportActionBar().hide();
        initalise();
        gson = new Gson();
        ExpireList = new ArrayList<RunningPromoListDisplay>();
        summary_list = new ArrayList<RunningPromoListDisplay>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        Expiring_promo.setVisibility(View.GONE);
        Expiring_summary.setVisibility(View.VISIBLE);
        Reusable_Functions.sDialog(this, "Loading.......");
        requestExpiryPromosummary();
    }

    private void requestExpiryPromosummary() {
        if (Reusable_Functions.chkStatus(context)) {
            String url = ConstsCore.web_url + "/v1/display/expiringpromosummary/" + userId + "?offset=" + offsetvalue + "&limit=" + limit;

            final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            try {
                                if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                    Reusable_Functions.hDialog();
                                    Toast.makeText(ExpiringPromoActivity.this, "no data found", Toast.LENGTH_SHORT).show();
                                    return;

                                } else if (response.length() == limit) {

                                    for (int i = 0; i < response.length(); i++) {

                                        ExpiringPromoListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                        summary_list.add(ExpiringPromoListDisplay);
                                    }
                                    offsetvalue = (limit * count) + limit;
                                    count++;

                                    requestExpiryPromosummary();

                                } else if (response.length() < limit) {

                                    for (int i = 0; i < response.length(); i++)
                                    {
                                        ExpiringPromoListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                        summary_list.add(ExpiringPromoListDisplay);

                                    }
                                    count = 0;
                                    limit = 100;
                                    offsetvalue = 0;


                                }

                                PromoList_summary.setLayoutManager(new LinearLayoutManager(PromoList_summary.getContext(), 48 == Gravity.CENTER_HORIZONTAL ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                PromoList_summary.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(PromoList_summary);
                                runningPromoSummaryAdapter = new RunningPromoSummaryAdapter(summary_list, ExpiringPromoActivity.this);
                                PromoList_summary.setAdapter(runningPromoSummaryAdapter);

                                Reusable_Functions.hDialog();
                            } catch (Exception e) {
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
    }

    private void requestRunningPromoApi() {

        if (Reusable_Functions.chkStatus(context)) {

            String url = ConstsCore.web_url + "/v1/display/expiringpromoheader/" + userId + "?offset=" + offsetvalue + "&limit=" + limit;
            final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            try {
                                if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                    Reusable_Functions.hDialog();
                                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                    return;

                                } else if (response.length() == limit) {

                                    for (int i = 0; i < response.length(); i++) {

                                        ExpiringPromoListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                        ExpireList.add(ExpiringPromoListDisplay);

                                    }
                                    offsetvalue = (limit * count) + limit;
                                    count++;

                                    requestRunningPromoApi();

                                } else if (response.length() < limit) {

                                    for (int i = 0; i < response.length(); i++) {

                                        ExpiringPromoListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                        ExpireList.add(ExpiringPromoListDisplay);
                                    }

                                    NumberFormat format = NumberFormat.getNumberInstance(new Locale("", "in"));

                                    promoval1.setText("\u20B9\t" +  format.format(Math.round(ExpireList.get(0).getDurSaleNetVal())));
                                    promoval2.setText("" + ExpireList.get(0).getDurSaleTotQty());
                                    storecode.setText(ExpireList.get(0).getStoreCode());
                                    storedesc.setText(ExpireList.get(0).getStoreDesc());
                                }

                                ExpireListView.setLayoutManager(new LinearLayoutManager(ExpireListView.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                ExpireListView.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(ExpireListView);
                                expiringPromoSnapAdapter = new ExpiringPromoSnapAdapter(ExpireList, context);
                                ExpireListView.setAdapter(expiringPromoSnapAdapter);
                                Reusable_Functions.hDialog();

                            } catch (Exception e) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "data failed..." + e.toString(), Toast.LENGTH_SHORT).show();
                                Reusable_Functions.hDialog();
                                e.printStackTrace();
                           }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "Server not found...", Toast.LENGTH_SHORT).show();
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

          ExpireListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    RecyclerViewPositionHelper mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
                    totalItemCount = mRecyclerViewHelper.getItemCount();
                    focusposition = mRecyclerViewHelper.findFirstVisibleItemPosition();
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, final int newState) {
                    super.onScrollStateChanged(recyclerView, newState);


                    currentState = newState;
                    if (prevState != RecyclerView.SCROLL_STATE_IDLE && currentState == RecyclerView.SCROLL_STATE_IDLE) {

                        Handler h = new Handler();
                        h.postDelayed(new Runnable() {
                            public void run() {
                                TimeUP();
                            }
                        }, 400);
                    }
                    prevState = currentState;
                }
            });
        }
    }

    private void TimeUP()
    {
        NumberFormat format = NumberFormat.getNumberInstance(new Locale("", "in"));
        if (focusposition < expiringPromoSnapAdapter.getItemCount() - 1) {
            promoval1.setText("\u20B9\t" + format.format(Math.round(ExpireList.get(focusposition).getDurSaleNetVal())));
            promoval2.setText("" + ExpireList.get(focusposition).getDurSaleTotQty());
            storecode.setText(ExpireList.get(focusposition).getStoreCode());
            storedesc.setText(ExpireList.get(focusposition).getStoreDesc());
        }
        else {
            focusposition = ExpireList.size() - 1;
            LinearLayoutManager llm = (LinearLayoutManager)ExpireListView.getLayoutManager();
            llm.scrollToPosition(focusposition);
            promoval1.setText("\u20B9\t" + format.format(Math.round(ExpireList.get(focusposition).getDurSaleNetVal())));
            promoval2.setText("" + ExpireList.get(focusposition).getDurSaleTotQty());
            storecode.setText(ExpireList.get(focusposition).getStoreCode());
            storedesc.setText(ExpireList.get(focusposition).getStoreDesc());
        }
    }


    private void initalise() {
        storecode = (TextView) findViewById(R.id.txtStoreCode);
        storedesc = (TextView) findViewById(R.id.txtStoreName);
        promoval1 = (TextView) findViewById(R.id.txt_exp_PromoVal1);
        promoval2 = (TextView) findViewById(R.id.txt_exp_PromoVal2);
        imageback = (RelativeLayout) findViewById(R.id.exp_imageBtnBack);
        imagefilter = (RelativeLayout) findViewById(R.id.exp_imgfilter);
        Expiring_summary = (LinearLayout) findViewById(R.id.expiring_summary);
        Expiring_promo = (LinearLayout) findViewById(R.id.expiring_promo);
        ExpireListView = (RecyclerView) findViewById(R.id.expirePromoListview);
        PromoList_summary = (RecyclerView) findViewById(R.id.promoList_summary);
        imageback.setOnClickListener(this);
        imagefilter.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.exp_imageBtnBack:
                onBackPressed();
                break;
            case R.id.exp_imgfilter:
                Intent intent = new Intent(ExpiringPromoActivity.this, FilterActivity.class);
                intent.putExtra("from","expiringPromo");
                startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(String value) {
        Reusable_Functions.sDialog(this, "Loading.......");
        requestRunningPromoApi();
        Expiring_promo.setVisibility(View.VISIBLE);
        Expiring_summary.setVisibility(View.GONE);
    }
}
