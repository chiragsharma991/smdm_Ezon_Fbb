package apsupportapp.aperotechnologies.com.designapp.RunningPromo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.Gravity;
import android.view.View;

import android.widget.AdapterView;

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
import apsupportapp.aperotechnologies.com.designapp.BestPerformersPromo.FilterActivity;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;

public class RunningPromoActivity extends AppCompatActivity implements View.OnClickListener, GravitySnapHelper.SnapListener,clickChild {

    TextView storecode, storedesc, promoval1, promoval2;
    RelativeLayout imageback, imagefilter;
    LinearLayout Running_promo, Running_summary;
    RunningPromoListDisplay runningPromoListDisplay;
    private SharedPreferences sharedPreferences;
    String userId, bearertoken;
    private int count = 0;
    private int limit = 100;
    private int offsetvalue = 0;
    Context context = RunningPromoActivity.this;
    private RequestQueue queue;
    private Gson gson;
    RecyclerView PromoListView;
    ArrayList<RunningPromoListDisplay> promoList,summary_list;
    private int focusposition = 0;
    private int itemCount = 0;
    private int totalItemCount;
    private boolean scrolling = false;
    private RunningPromoSnapAdapter runningPromoSnapAdapter;
    private RunningPromoSummaryAdapter runningPromoSummaryAdapter;
    private int firstVisibleItem = 0;
    int prevState = RecyclerView.SCROLL_STATE_IDLE;
    int currentState = RecyclerView.SCROLL_STATE_IDLE;
    private RecyclerView PromoList_summary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_promo);
        getSupportActionBar().hide();
        initalise();
        gson = new Gson();
        promoList = new ArrayList<RunningPromoListDisplay>();
        summary_list = new ArrayList<RunningPromoListDisplay>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        Running_promo.setVisibility(View.GONE);
        Running_summary.setVisibility(View.VISIBLE);
        Reusable_Functions.sDialog(this, "Loading.......");
        requestRunningPromosummary();
     }


    private void requestRunningPromosummary() {


        if (Reusable_Functions.chkStatus(context)) {
            String url = ConstsCore.web_url + "/v1/display/runningpromosummary/" + userId + "?offset=" + offsetvalue + "&limit=" + limit;
            final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                           try {
                                if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                    Reusable_Functions.hDialog();
                                    Toast.makeText(RunningPromoActivity.this, "no data found", Toast.LENGTH_SHORT).show();
                                    return;

                                } else if (response.length() == limit) {
                                    for (int i = 0; i < response.length(); i++) {

                                        runningPromoListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                        summary_list.add(runningPromoListDisplay);

                                    }
                                    offsetvalue = (limit * count) + limit;
                                    count++;
                                    requestRunningPromosummary();

                                } else if (response.length() < limit) {
                                    for (int i = 0; i < response.length(); i++)
                                    {
                                        runningPromoListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                        summary_list.add(runningPromoListDisplay);
                                    }
                                    count = 0;
                                    limit = 100;
                                    offsetvalue = 0;
                                }

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

            String url = ConstsCore.web_url + "/v1/display/runningpromoheader/" + userId + "?offset=" + offsetvalue + "&limit=" + limit;

            final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            try {
                                if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                    Reusable_Functions.hDialog();
                                    Toast.makeText(RunningPromoActivity.this, "no data found", Toast.LENGTH_SHORT).show();
                                    return;

                                } else if (response.length() == limit) {
                                    for (int i = 0; i < response.length(); i++) {

                                        runningPromoListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                        promoList.add(runningPromoListDisplay);

                                    }
                                    offsetvalue = (limit * count) + limit;
                                    count++;
                                    requestRunningPromoApi();

                                } else if (response.length() < limit) {
                                    for (int i = 0; i < response.length(); i++)
                                    {
                                        runningPromoListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                        promoList.add(runningPromoListDisplay);
                                    }

                                    NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("","in"));
                                    promoval1.setText("\u20B9\t" + formatter.format(Math.round(promoList.get(0).getDurSaleNetVal())));
                                    promoval2.setText("" + promoList.get(0).getDurSaleTotQty());
                                    storecode.setText(promoList.get(0).getStoreCode());
                                    storedesc.setText(promoList.get(0).getStoreDesc());
                                }
                                PromoListView.setLayoutManager(new LinearLayoutManager(
                                        PromoListView.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                PromoListView.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(PromoListView);
                                runningPromoSnapAdapter = new RunningPromoSnapAdapter(promoList, RunningPromoActivity.this);
                                PromoListView.setAdapter(runningPromoSnapAdapter);

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


            //-----------------------------ON CLICK LISTENER-----------------------------//


            PromoListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    RecyclerViewPositionHelper mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
                    totalItemCount = mRecyclerViewHelper.getItemCount();
                    firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();
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


    private void TimeUP() {

        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("","in"));
        if (firstVisibleItem < runningPromoSnapAdapter.getItemCount() - 1) {

            promoval1.setText("\u20B9\t" + formatter.format(Math.round(promoList.get(firstVisibleItem).getDurSaleNetVal())));
            promoval2.setText("" + promoList.get(firstVisibleItem).getDurSaleTotQty());
            storecode.setText(promoList.get(firstVisibleItem).getStoreCode());
            storedesc.setText(promoList.get(firstVisibleItem).getStoreDesc());
        } else {
            firstVisibleItem = promoList.size() - 1;
            LinearLayoutManager llm = (LinearLayoutManager) PromoListView.getLayoutManager();
            llm.scrollToPosition(firstVisibleItem);
            promoval1.setText("\u20B9\t" + formatter.format(Math.round(promoList.get(firstVisibleItem).getDurSaleNetVal())));
            promoval2.setText("" + promoList.get(firstVisibleItem).getDurSaleTotQty());
            storecode.setText(promoList.get(firstVisibleItem).getStoreCode());
            storedesc.setText(promoList.get(firstVisibleItem).getStoreDesc());
        }
    }

    private void initalise() {
        storecode = (TextView) findViewById(R.id.txtStoreCode);
        storedesc = (TextView) findViewById(R.id.txtStoreName);
        promoval1 = (TextView) findViewById(R.id.txtPromoVal1);
        promoval2 = (TextView) findViewById(R.id.txtPromoVal2);
        imageback = (RelativeLayout) findViewById(R.id.rp_imageBtnBack);
        imagefilter = (RelativeLayout) findViewById(R.id.rp_imgfilter);
        Running_promo = (LinearLayout) findViewById(R.id.running_promo);
        Running_summary = (LinearLayout) findViewById(R.id.running_summary);
        PromoListView = (RecyclerView) findViewById(R.id.promoListview);
        PromoList_summary = (RecyclerView) findViewById(R.id.promoList_summary);
        PromoListView.setLayoutManager(new LinearLayoutManager(this));
        PromoListView.setHasFixedSize(true);
        imageback.setOnClickListener(this);
        imagefilter.setOnClickListener(this);

    }

    public static View getChildAtPosition(final AdapterView view, final int position) {
        final int index = position - view.getFirstVisiblePosition();
        if ((index >= 0) && (index < view.getChildCount())) {
            return view.getChildAt(index);
        } else {
            return null;
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rp_imageBtnBack:
                onBackPressed();
                break;
            case R.id.rp_imgfilter:
                Intent intent = new Intent(context, FilterActivity.class);
                intent.putExtra("from", "runningPromo");
                startActivity(intent);
                // finish();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onSnap(int position) {

    }

    @Override
    public void onClick(String value) {
          Reusable_Functions.sDialog(this, "Loading...");
          requestRunningPromoApi();
          Running_promo.setVisibility(View.VISIBLE);
          Running_summary.setVisibility(View.GONE);

    }


}
