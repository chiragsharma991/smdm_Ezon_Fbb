package apsupportapp.aperotechnologies.com.designapp.StockAgeing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckBox;
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

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.DashBoardActivity;
import apsupportapp.aperotechnologies.com.designapp.FreshnessIndex.InventoryFilterActivity;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.TopOptionCutSize.TopFullCut;
import apsupportapp.aperotechnologies.com.designapp.TopOptionCutSize.TopOptionAdapter;
import apsupportapp.aperotechnologies.com.designapp.model.OptionEfficiencyDetails;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by pamrutkar on 05/12/16.
 */

public class StockAgeingActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    TextView stock_txtStoreCode, stock_txtStoreName;
    RelativeLayout stock_BtnBack, stock_BtnFilter, stock_quickFilter, quickFilterPopup, quickFilter_baseLayout, qfDoneLayout, quickFilter_BorderLayout;
    RunningPromoListDisplay StockAgeingListDisplay;
    private SharedPreferences sharedPreferences;
    String userId, bearertoken, seasongroup = "All";
    String TAG = "StockAgeingActivity";
    private int count = 0;
    private int limit = 10;
    private int offsetvalue = 0;
    private int top = 10;
    CheckBox checkCurrent, checkPrevious, checkOld, checkUpcoming, checkAgeing1, checkAgeing2, checkAgeing3;
    Context context = this;
    private RequestQueue queue;
    private Gson gson;
    ListView StockAgListView;
    ArrayList<RunningPromoListDisplay> StockAgeingList;
    private int focusposition = 0;
    private boolean userScrolled;
    StockAgeingAdapter stockAgeingAdapter;
    private View footer;
    private String lazyScroll = "OFF";
    private SegmentedGroup stock_segmented;
    private RadioButton stock_fashion, stock_core;
    private ToggleButton Toggle_stock_fav;
    private String corefashion = "Fashion";
    String checkSeasonGpVal = null, checkAgeingVal = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_ageing);
        getSupportActionBar().hide();
        initalise();

        gson = new Gson();
        StockAgeingList = new ArrayList<RunningPromoListDisplay>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Log.e(TAG, "userID and token" + userId + "and this is" + bearertoken);
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        StockAgListView.setVisibility(View.VISIBLE);
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            offsetvalue = 0;
            limit = 10;
            count = 0;
            top = 10;
            requestStockAgeingApi();
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
        // bestPromoAdapter = new BestPromoAdapter(BestpromoList,context);
        footer = getLayoutInflater().inflate(R.layout.bestpromo_footer, null);

        StockAgListView.addFooterView(footer);

    }

    private void requestStockAgeingApi() {

        String url = ConstsCore.web_url + "/v1/display/stockageing/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&top=" + top + "&corefashion=" + corefashion + "&seasongroup=" + seasongroup;

        Log.e(TAG, "URL" + url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, "Stock Aging : " + " " + response);
                        Log.i(TAG, "response" + "" + response.length());
                        StockAgListView.setVisibility(View.VISIBLE);
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                footer.setVisibility(View.GONE);
                                if (StockAgeingList.size() == 0) {
                                    StockAgListView.setVisibility(View.GONE);
                                }

                            } else if (response.length() == limit) {
                                Log.e(TAG, "Top eql limit");
                                for (int i = 0; i < response.length(); i++) {

                                    StockAgeingListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                    StockAgeingList.add(StockAgeingListDisplay);

                                }
                                offsetvalue = offsetvalue + 10;
                                top = top + 10;
                                //  count++;

                                // requestStockAgeingApi();

                            } else if (response.length() < limit) {
                                Log.e(TAG, "promo /= limit");
                                for (int i = 0; i < response.length(); i++) {

                                    StockAgeingListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                    StockAgeingList.add(StockAgeingListDisplay);

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
                                stockAgeingAdapter.notifyDataSetChanged();
                                lazyScroll = "OFF";
                            } else {
                                stockAgeingAdapter = new StockAgeingAdapter(StockAgeingList, context);
                                StockAgListView.setAdapter(stockAgeingAdapter);
                                stock_txtStoreCode.setText(StockAgeingList.get(0).getStoreCode());
                                stock_txtStoreName.setText(StockAgeingList.get(0).getStoreDescription());

                            }


                            Reusable_Functions.hDialog();
                        } catch (Exception e) {


                            StockAgeingList.clear();
                            stockAgeingAdapter.notifyDataSetChanged();
                            StockAgListView.setVisibility(View.GONE);
                            Reusable_Functions.hDialog();
                            footer.setVisibility(View.GONE);
                            // Toast.makeText(context, "no data found in catch" + e.toString(), Toast.LENGTH_SHORT).show();
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

        StockAgListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            public int VisibleItemCount, TotalItemCount, FirstVisibleItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (FirstVisibleItem + VisibleItemCount == TotalItemCount && scrollState == SCROLL_STATE_IDLE) {
                    footer.setVisibility(View.VISIBLE);
                    lazyScroll = "ON";
                    requestStockAgeingApi();
                    //Reusable_Functions.sDialog(context, "Loading data...");
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
        stock_txtStoreCode = (TextView) findViewById(R.id.txtStoreCode);
        stock_txtStoreName = (TextView) findViewById(R.id.txtStoreName);
        stock_BtnBack = (RelativeLayout) findViewById(R.id.stockAgeing_imageBtnBack);
        stock_BtnFilter = (RelativeLayout) findViewById(R.id.stockAgeing_imgfilter);
        stock_quickFilter = (RelativeLayout) findViewById(R.id.sa_quickFilter);
        quickFilterPopup = (RelativeLayout) findViewById(R.id.quickFilterPopup);
        quickFilterPopup.setVisibility(View.GONE);
        quickFilter_baseLayout = (RelativeLayout) findViewById(R.id.quickFilter_baseLayout);
        qfDoneLayout = (RelativeLayout) findViewById(R.id.qfDoneLayout);
        quickFilter_BorderLayout = (RelativeLayout) findViewById(R.id.quickFilter_BorderLayout);
        StockAgListView = (ListView) findViewById(R.id.stockListView);
        stock_segmented = (SegmentedGroup) findViewById(R.id.stock_segmented);
        stock_core = (RadioButton) findViewById(R.id.stock_core);
        stock_fashion = (RadioButton) findViewById(R.id.stock_fashion);
        stock_fashion.toggle();
        Toggle_stock_fav = (ToggleButton) findViewById(R.id.toggle_top_fav);
        checkCurrent = (CheckBox) findViewById(R.id.checkCurrent);
        checkPrevious = (CheckBox) findViewById(R.id.checkPrevious);
        checkOld = (CheckBox) findViewById(R.id.checkOld);
        checkUpcoming = (CheckBox) findViewById(R.id.checkUpcoming);
        checkAgeing1 = (CheckBox) findViewById(R.id.checkAgeing1);
        checkAgeing2 = (CheckBox) findViewById(R.id.checkAgeing2);
        checkAgeing3 = (CheckBox) findViewById(R.id.checkAgeing3);
        checkCurrent.setOnClickListener(this);
        checkPrevious.setOnClickListener(this);
        checkOld.setOnClickListener(this);
        checkUpcoming.setOnClickListener(this);
        checkAgeing3.setOnClickListener(this);
        checkAgeing2.setOnClickListener(this);
        checkAgeing1.setOnClickListener(this);


        qfDoneLayout.setOnClickListener(this);
        stock_segmented.setOnCheckedChangeListener(StockAgeingActivity.this);
        stock_BtnBack.setOnClickListener(StockAgeingActivity.this);
        stock_BtnFilter.setOnClickListener(this);
        stock_quickFilter.setOnClickListener(this);
        quickFilter_baseLayout.setOnClickListener(this);
        quickFilter_BorderLayout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.stockAgeing_imageBtnBack:
            /*    Intent intent = new Intent(StockAgeingActivity.this, DashBoardActivity.class);
                intent.putExtra("BACKTO","inventory");
                startActivity(intent);*/
                finish();
                break;
            case R.id.toggle_stock_fav:
                if (Toggle_stock_fav.isChecked()) {
                    Toggle_stock_fav.setChecked(true);
                } else {
                    Toggle_stock_fav.setChecked(false);
                }

                break;
            case R.id.stockAgeing_imgfilter:
                Intent intent1 = new Intent(StockAgeingActivity.this, InventoryFilterActivity.class);
                intent1.putExtra("checkfrom", "stockAgeing");
                startActivity(intent1);
                finish();
                break;
            case R.id.sa_quickFilter:
                quickFilterPopup.setVisibility(View.VISIBLE);
                break;
            case R.id.quickFilter_baseLayout:
                if (checkSeasonGpVal == null && checkAgeingVal == null) {
                    checkCurrent.setChecked(false);
                    checkPrevious.setChecked(false);
                    checkOld.setChecked(false);
                    checkUpcoming.setChecked(false);
                    checkAgeing1.setChecked(false);
                    checkAgeing2.setChecked(false);
                    checkAgeing3.setChecked(false);

                } else {
                    switch (checkSeasonGpVal.toString()) {
                        case "Current":
                            checkCurrent.setChecked(true);
                            checkPrevious.setChecked(false);
                            checkOld.setChecked(false);
                            checkUpcoming.setChecked(false);

                            Log.e("Current checked", "" + checkCurrent.isChecked());
                            break;

                        case "Previous":
                            checkPrevious.setChecked(true);
                            checkCurrent.setChecked(false);
                            checkOld.setChecked(false);
                            checkUpcoming.setChecked(false);
                            Log.e("Previous checked", "" + checkPrevious.isChecked());
                            break;
                        case "Old":
                            checkOld.setChecked(true);
                            checkCurrent.setChecked(false);
                            checkPrevious.setChecked(false);
                            checkUpcoming.setChecked(false);
                            Log.e("Old checked", "" + checkOld.isChecked());
                            break;
                        case "Upcoming":
                            checkUpcoming.setChecked(true);
                            checkCurrent.setChecked(false);
                            checkOld.setChecked(false);
                            checkPrevious.setChecked(false);
                            Log.e("Upcoming checked", "" + checkUpcoming.isChecked());
                            break;
                    }

                    switch (checkAgeingVal.toString()) {
                        case "CheckAgeing1":
                            checkAgeing1.setChecked(true);
                            checkAgeing2.setChecked(false);
                            checkAgeing3.setChecked(false);
                            break;
                        case "CheckAgeing2":
                            checkAgeing2.setChecked(true);
                            checkAgeing1.setChecked(false);
                            checkAgeing3.setChecked(false);
                            break;
                        case "CheckAgeing3":
                            checkAgeing3.setChecked(true);
                            checkAgeing2.setChecked(false);
                            checkAgeing1.setChecked(false);
                            break;
                    }
                }
                quickFilterPopup.setVisibility(View.GONE);
                break;
            case R.id.qfDoneLayout:
                if (checkCurrent.isChecked()) {
                    popupCurrent();
                    checkSeasonGpVal = "Current";

                    quickFilterPopup.setVisibility(View.GONE);

                } else if (checkPrevious.isChecked()) {
                    popupPrevious();
                    checkSeasonGpVal = "Previous";

                    quickFilterPopup.setVisibility(View.GONE);

                } else if (checkOld.isChecked()) {
                    popupOld();
                    checkSeasonGpVal = "Old";

                    quickFilterPopup.setVisibility(View.GONE);

                } else if (checkUpcoming.isChecked()) {
                    popupUpcoming();
                    checkSeasonGpVal = "Upcoming";

                    quickFilterPopup.setVisibility(View.GONE);
                } else {
                    Log.e("Uncheck1","----"+checkSeasonGpVal);
                    //Toast.makeText(this, "Uncheck", Toast.LENGTH_SHORT).show();

                }
                if (checkAgeing1.isChecked()) {
                    //popupUpcoming();
                    checkAgeingVal = "CheckAgeing1";

                    quickFilterPopup.setVisibility(View.GONE);
                } else if (checkAgeing2.isChecked()) {
                    //popupUpcoming();
                    checkAgeingVal = "CheckAgeing2";

                    quickFilterPopup.setVisibility(View.GONE);
                } else if (checkAgeing3.isChecked()) {
                    //popupUpcoming();
                    checkAgeingVal = "CheckAgeing3";
                    quickFilterPopup.setVisibility(View.GONE);
                } else {
                    Log.e("Uncheck2","----"+checkAgeingVal);

                    //Toast.makeText(this, "Uncheck", Toast.LENGTH_SHORT).show();

                }

                break;
            case R.id.checkCurrent:
                checkCurrent.setChecked(true);
                checkPrevious.setChecked(false);
                checkOld.setChecked(false);
                checkUpcoming.setChecked(false);

                break;
            case R.id.checkPrevious:

                checkPrevious.setChecked(true);
                checkCurrent.setChecked(false);
                checkOld.setChecked(false);
                checkUpcoming.setChecked(false);

                break;
            case R.id.checkOld:

                checkOld.setChecked(true);
                checkCurrent.setChecked(false);
                checkPrevious.setChecked(false);
                checkUpcoming.setChecked(false);

                break;
            case R.id.checkUpcoming:

                checkUpcoming.setChecked(true);
                checkCurrent.setChecked(false);
                checkOld.setChecked(false);
                checkPrevious.setChecked(false);

                break;
            case R.id.checkAgeing1:

                checkAgeing1.setChecked(true);
                checkAgeing2.setChecked(false);
                checkAgeing3.setChecked(false);
                break;
            case R.id.checkAgeing2:

                checkAgeing2.setChecked(true);
                checkAgeing1.setChecked(false);
                checkAgeing3.setChecked(false);
                break;
            case R.id.checkAgeing3:

                checkAgeing3.setChecked(true);
                checkAgeing1.setChecked(false);
                checkAgeing2.setChecked(false);
                break;
        }

    }

    private void popupCurrent() {
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            limit = 10;
            offsetvalue = 0;
            top = 10;
            seasongroup = "Current";
            StockAgeingList.clear();
            requestStockAgeingApi();
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private void popupPrevious() {
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            limit = 10;
            offsetvalue = 0;
            top = 10;
            seasongroup = "Previous";
            StockAgeingList.clear();
            requestStockAgeingApi();
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private void popupOld() {
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            limit = 10;
            offsetvalue = 0;
            top = 10;
            seasongroup = "Old";
            StockAgeingList.clear();
            requestStockAgeingApi();
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private void popupUpcoming() {
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            limit = 10;
            offsetvalue = 0;
            top = 10;
            seasongroup = "Upcoming";
            StockAgeingList.clear();
            requestStockAgeingApi();
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.stock_core:
                if (stock_core.isChecked()) {
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.hDialog();
                        Reusable_Functions.sDialog(context, "Loading data...");
                        limit = 10;
                        offsetvalue = 0;
                        top = 10;
                        corefashion = "Core";
                        StockAgeingList.clear();
                        stockAgeingAdapter.notifyDataSetChanged();
                        StockAgListView.setVisibility(View.GONE);
                        requestStockAgeingApi();
                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.stock_fashion:
                if (stock_fashion.isChecked()) {
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.hDialog();
                        Reusable_Functions.sDialog(context, "Loading data...");
                        limit = 10;
                        offsetvalue = 0;
                        top = 10;
                        corefashion = "Fashion";
                        StockAgeingList.clear();
                        stockAgeingAdapter.notifyDataSetChanged();
                        StockAgListView.setVisibility(View.GONE);
                        requestStockAgeingApi();
                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* Intent intent = new Intent(StockAgeingActivity.this, DashBoardActivity.class);
        intent.putExtra("BACKTO","inventory");
        startActivity(intent);*/
        finish();
    }

}
