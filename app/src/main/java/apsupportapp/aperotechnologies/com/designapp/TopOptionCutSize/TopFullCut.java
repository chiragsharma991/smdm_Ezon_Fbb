package apsupportapp.aperotechnologies.com.designapp.TopOptionCutSize;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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

import apsupportapp.aperotechnologies.com.designapp.BestPerformersPromo.BestPromoAdapter;
import apsupportapp.aperotechnologies.com.designapp.BestPerformersPromo.FilterActivity;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.DashBoardActivity;
import apsupportapp.aperotechnologies.com.designapp.FreshnessIndex.InventoryFilterActivity;
import apsupportapp.aperotechnologies.com.designapp.LocalNotificationReceiver;
import apsupportapp.aperotechnologies.com.designapp.LoginActivity;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.TransparentActivity;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;
import info.hoang8f.android.segmented.SegmentedGroup;


public class TopFullCut extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    TextView Top_txtStoreCode, Top_txtStoreName;
    RelativeLayout TopBest_BtnBack,TopOption_imgfilter;
    RunningPromoListDisplay TopOptionListDisplay;
    private SharedPreferences sharedPreferences;
    String userId, bearertoken;
    String TAG = "TopOptionByFullCut";
    private int count = 0;
    private int limit = 10;
    private int offsetvalue = 0;
    private int top = 10;
    private int popPromo = 0;
    Context context = this;
    private RequestQueue queue;
    private Gson gson;
    ListView TopOptionListView;
    ArrayList<RunningPromoListDisplay> TopOptionList;
    private int focusposition = 0;
    private boolean userScrolled;
    private TopOptionAdapter topOptionAdapter;
    private View footer;
    private String lazyScroll = "OFF";
    private SegmentedGroup Top_segmented;
    private RadioButton Top_fashion, Top_core;
    private ToggleButton Toggle_top_fav;
    private String corefashion = "Fashion";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_full_cut);
        getSupportActionBar().hide();
        initalise();
        gson = new Gson();
        TopOptionListView.setVisibility(View.VISIBLE);
        TopOptionList = new ArrayList<RunningPromoListDisplay>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Log.e(TAG, "userID and token" + userId + "and this is" + bearertoken);
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();

        requestRunningPromoApi();
        Reusable_Functions.sDialog(this, "Loading.......");
        // bestPromoAdapter = new BestPromoAdapter(BestpromoList,context);
        footer = getLayoutInflater().inflate(R.layout.bestpromo_footer, null);

        TopOptionListView.addFooterView(footer);
        // footer.setVisibility(View.GONE);
        // BestPerformanceListView.setAdapter(bestPromoAdapter);


    }

    private void requestRunningPromoApi() {

        String url = ConstsCore.web_url + "/v1/display/topoptionsbyfullcut/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&top=" + top + "&corefashion=" + corefashion;

        Log.e(TAG, "URL" + url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, "Top Option : " + " " + response);
                        Log.i(TAG, "response" + "" + response.length());
                        TopOptionListView.setVisibility(View.VISIBLE);


                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                footer.setVisibility(View.GONE);
                                if (TopOptionList.size() == 0) {
                                    TopOptionListView.setVisibility(View.GONE);

                                }
                            } else if (response.length() == limit) {


                                Log.e(TAG, "Top eql limit");
                                for (int i = 0; i < response.length(); i++) {

                                    TopOptionListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                    TopOptionList.add(TopOptionListDisplay);

                                }
                                offsetvalue = offsetvalue + 10;
                                top = top + 10;
                                //  count++;

                                // requestRunningPromoApi();

                            } else if (response.length() < limit) {
                                Log.e(TAG, "promo /= limit");
                                for (int i = 0; i < response.length(); i++) {

                                    TopOptionListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                    TopOptionList.add(TopOptionListDisplay);
                                    offsetvalue = offsetvalue + response.length();
                                    top = top + response.length();

                                }
                            }


                            footer.setVisibility(View.GONE);
                           /* if(popPromo==10)
                            {
                                topOptionAdapter = new TopOptionAdapter(TopOptionList,context);
                                TopOptionListView.setAdapter(topOptionAdapter);
                                popPromo=0;

                            }*/

                            if (lazyScroll.equals("ON")) {
                                topOptionAdapter.notifyDataSetChanged();
                                lazyScroll = "OFF";
                            } else {
                                topOptionAdapter = new TopOptionAdapter(TopOptionList, context);
                                TopOptionListView.setAdapter(topOptionAdapter);


                            }

                            Top_txtStoreCode.setText(TopOptionList.get(0).getStoreCode());
                            Top_txtStoreName.setText(TopOptionList.get(0).getStoreDesc());

                            Reusable_Functions.hDialog();
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            footer.setVisibility(View.GONE);
                            TopOptionListView.setVisibility(View.GONE);
                            e.printStackTrace();
                            Log.e(TAG, "catch...Error" + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
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


//---------------seton Click list listener------------------//

        TopOptionListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            public int VisibleItemCount, TotalItemCount, FirstVisibleItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (FirstVisibleItem + VisibleItemCount == TotalItemCount && scrollState == SCROLL_STATE_IDLE) {


                    footer.setVisibility(View.VISIBLE);

                    lazyScroll = "ON";
                    requestRunningPromoApi();
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                this.FirstVisibleItem = firstVisibleItem;
                this.VisibleItemCount = visibleItemCount;
                this.TotalItemCount = totalItemCount;

            }
        });
    }


    private void initalise() {
        Top_txtStoreCode = (TextView) findViewById(R.id.top_txtStoreCode);
        Top_txtStoreName = (TextView) findViewById(R.id.top_txtStoreName);

        TopBest_BtnBack = (RelativeLayout) findViewById(R.id.topBest_BtnBack);
        TopOption_imgfilter = (RelativeLayout) findViewById(R.id.topOption_imgfilter);

        TopOptionListView = (ListView) findViewById(R.id.topOptionListView);
        Top_segmented = (SegmentedGroup) findViewById(R.id.top_segmented);

        Top_core = (RadioButton) findViewById(R.id.top_core);
        Top_core.toggle();

        Top_fashion = (RadioButton) findViewById(R.id.top_fashion);

        Toggle_top_fav = (ToggleButton) findViewById(R.id.toggle_top_fav);


        Top_segmented.setOnCheckedChangeListener(TopFullCut.this);
        TopBest_BtnBack.setOnClickListener(TopFullCut.this);
        TopOption_imgfilter.setOnClickListener(TopFullCut.this);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.topBest_BtnBack:
                onBackPressed();
                break;
            case R.id.topOption_imgfilter:
                Intent intent = new Intent(this, InventoryFilterActivity.class);
                intent.putExtra("checkfrom", "TopFullCut");
                startActivity(intent);
                finish();
                break;
            case R.id.toggle_top_fav:
                if (Toggle_top_fav.isChecked()) {
                    Toggle_top_fav.setChecked(true);
                } else {
                    Toggle_top_fav.setChecked(false);
                }

                break;


        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* Intent intent=new Intent(context, DashBoardActivity.class);
        intent.putExtra("BACKTO","inventory");
        startActivity(intent);*/
        finish();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.top_core:
                if (Top_core.isChecked()) {
                    limit = 10;
                    offsetvalue = 0;
                    top = 10;
                    corefashion = "Core";
                    TopOptionList.clear();
                    topOptionAdapter.notifyDataSetChanged();
                    TopOptionListView.setVisibility(View.GONE);
                    Reusable_Functions.sDialog(this, "Loading.......");
                    requestRunningPromoApi();
                }
                break;
            case R.id.top_fashion:
                if (Top_fashion.isChecked()) {
                    limit = 10;
                    offsetvalue = 0;
                    top = 10;
                    corefashion = "Fashion";
                    TopOptionList.clear();
                    topOptionAdapter.notifyDataSetChanged();
                    TopOptionListView.setVisibility(View.GONE);
                    Reusable_Functions.sDialog(this, "Loading.......");
                    requestRunningPromoApi();
                }


                break;


        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart: ");
        if (LocalNotificationReceiver.logoutAlarm) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            finish();
            Intent i = new Intent(context, LoginActivity.class);
            // set the new task and clear flags
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }


    }
}
