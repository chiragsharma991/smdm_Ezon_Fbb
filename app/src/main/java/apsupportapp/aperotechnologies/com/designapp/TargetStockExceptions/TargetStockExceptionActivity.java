package apsupportapp.aperotechnologies.com.designapp.TargetStockExceptions;

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
import android.widget.SeekBar;
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
import apsupportapp.aperotechnologies.com.designapp.StockAgeing.StockAgeingAdapter;
import apsupportapp.aperotechnologies.com.designapp.model.FloorAvailabilityDetails;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by pamrutkar on 08/12/16.
 */

public class TargetStockExceptionActivity extends AppCompatActivity implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {

    TextView target_txtStoreCode, target_txtStoreName;
    RelativeLayout target_BtnBack, target_BtnFilter, target_quickFilter, quickFilterPopup, quickFilter_baseLayout, qfDoneLayout, quickFilter_BorderLayout;
    FloorAvailabilityDetails targetStockDetails;
    private SharedPreferences sharedPreferences;
    String userId, bearertoken, seasongroup = "Current";
    String TAG = "TargetStockExceptionActivity";
    int count = 0;
    int limit = 10;
    int offsetvalue = 0;
    int top = 10;
    static int level = 1;
    CheckBox checkCurrent, checkPrevious, checkOld, checkUpcoming;
    CheckBox checktwo, checkthree, checkfour, checkfive;
    CheckBox checksix, checkseven, checkeight, checknine, checkten;
    RadioButton checkDept, checkCategory, checkPlanClass, checkWTD, checkL4W, checkYTD;
    Context context = this;
    private RequestQueue queue;
    private Gson gson;
    ListView targetListView;
    ArrayList<FloorAvailabilityDetails> targetStockList;
    private int focusposition = 0;
    private boolean userScrolled;
    TargetStockExcepAdapter targetAgeingAdapter;
    private View footer;
    private String lazyScroll = "OFF";
    private SegmentedGroup target_segmented;
    private RadioButton target_fashion, target_core;
    private ToggleButton Toggle_target_fav;
    private String corefashion = "Fashion", view = "STD";
    String checkSeasonGpVal = null, checkTimeVal = null, checkTitleVal = null;
    int checkTargetROSVal = 7;
    private SeekBar TargetSeek;
    private TextView targetMax;
    private int setValue;
    private boolean coreSelection = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target_exception);
        getSupportActionBar().hide();
        initalise();
        gson = new Gson();
        targetStockList = new ArrayList<FloorAvailabilityDetails>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Log.e(TAG, "userID and token" + userId + "and this is" + bearertoken);
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        targetListView.setTag("FOOTER");
        targetListView.setVisibility(View.VISIBLE);
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            offsetvalue = 0;
            limit = 10;
            count = 0;
            top = 10;
            level = 1;
            view = "STD";
            requestTargetStockExcepApi();
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
        // bestPromoAdapter = new BestPromoAdapter(BestpromoList,context);
        footer = getLayoutInflater().inflate(R.layout.target_exception_footer, null);

        targetListView.addFooterView(footer);

    }

    private void requestTargetStockExcepApi() {

        if (Reusable_Functions.chkStatus(context)) {


            String url;
            if (coreSelection) {

                //core selection without season params

                url = ConstsCore.web_url + "/v1/display/targetstockexceptions/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&top=" + top + "&corefashion=" + corefashion + "&level=" + level + "&view=" + view + "&targetros=" + checkTargetROSVal;
            } else {

                // fashion select with season params

                url = ConstsCore.web_url + "/v1/display/targetstockexceptions/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&top=" + top + "&corefashion=" + corefashion + "&seasongroup=" + seasongroup + "&level=" + level + "&view=" + view + "&targetros=" + checkTargetROSVal;
            }


            Log.e(TAG, "URL" + url);
            final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.i(TAG, "Target Stock Exception : " + " " + response);
                            Log.i(TAG, "response" + "" + response.length());
                            targetListView.setVisibility(View.VISIBLE);
                            try {
                                if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                    Reusable_Functions.hDialog();
                                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                    targetListView.removeFooterView(footer);
                                    targetListView.setTag("FOOTER_REMOVE");
                                    if (targetStockList.size() == 0) {
                                        targetListView.setVisibility(View.GONE);
                                        return;
                                    }

                                } else if (response.length() == limit) {
                                    Log.e(TAG, "Top eql limit");
                                    for (int i = 0; i < response.length(); i++) {

                                        targetStockDetails = gson.fromJson(response.get(i).toString(), FloorAvailabilityDetails.class);
                                        targetStockList.add(targetStockDetails);

                                    }
                                    offsetvalue = offsetvalue + 10;
                                    top = top + 10;
                                    //  count++;

                                    // requestTargetStockExcepApi();

                                } else if (response.length() < limit) {
                                    Log.e(TAG, "promo /= limit");
                                    for (int i = 0; i < response.length(); i++) {

                                        targetStockDetails = gson.fromJson(response.get(i).toString(), FloorAvailabilityDetails.class);
                                        targetStockList.add(targetStockDetails);

                                        offsetvalue = offsetvalue + response.length();
                                        top = top + response.length();

                                    }
                                    targetListView.removeFooterView(footer);
                                    targetListView.setTag("FOOTER_REMOVE");
                                }


                                footer.setVisibility(View.GONE);
                           /* if(popPromo==10)
                            {
                                topOptionAdapter = new TopOptionAdapter(TopOptionList,context);
                                TopOptionListView.setAdapter(topOptionAdapter);
                                popPromo=0;

                            }*/

                                if (lazyScroll.equals("ON")) {
                                    targetAgeingAdapter.notifyDataSetChanged();
                                    lazyScroll = "OFF";
                                } else {
                                    targetAgeingAdapter = new TargetStockExcepAdapter(targetStockList, context);
                                    targetListView.setAdapter(targetAgeingAdapter);
                                    target_txtStoreCode.setText(targetStockList.get(0).getStoreCode());
                                    target_txtStoreName.setText(targetStockList.get(0).getStoreDescription());

                                }


                                Reusable_Functions.hDialog();
                            } catch (Exception e) {


                                targetStockList.clear();
                                targetAgeingAdapter.notifyDataSetChanged();
                                targetListView.setVisibility(View.GONE);
                                Reusable_Functions.hDialog();
                                footer.setVisibility(View.GONE);
                                Toast.makeText(context, "Data failed...", Toast.LENGTH_SHORT).show();
                                targetListView.removeFooterView(footer);
                                targetListView.setTag("FOOTER_REMOVE");
                                e.printStackTrace();
                                Log.e(TAG, "catch...Error" + e.toString());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "Server not found...", Toast.LENGTH_SHORT).show();
                            targetListView.removeFooterView(footer);
                            targetListView.setTag("FOOTER_REMOVE");
                            targetListView.setVisibility(View.GONE);

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

            targetListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                public int VisibleItemCount, TotalItemCount, FirstVisibleItem;

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                    if (FirstVisibleItem + VisibleItemCount == TotalItemCount && scrollState == SCROLL_STATE_IDLE && lazyScroll.equals("OFF")) {
                        if (targetListView.getTag().equals("FOOTER_REMOVE")) {
                            targetListView.addFooterView(footer);
                            targetListView.setTag("FOOTER_ADDED");

                        }


                        Log.e(TAG, "onScrollStateChanged:  log");
                        footer.setVisibility(View.VISIBLE);
                        lazyScroll = "ON";
                        requestTargetStockExcepApi();
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
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
            Reusable_Functions.hDialog();
            targetListView.removeFooterView(footer);
            targetListView.setTag("FOOTER_REMOVE");
        }
    }


    private void initalise() {
        target_txtStoreCode = (TextView) findViewById(R.id.txtStoreCode);
        target_txtStoreName = (TextView) findViewById(R.id.txtStoreName);
        target_BtnBack = (RelativeLayout) findViewById(R.id.target_imageBtnBack);
        target_BtnFilter = (RelativeLayout) findViewById(R.id.target_imgfilter);
        target_quickFilter = (RelativeLayout) findViewById(R.id.target_quickFilter);
        quickFilterPopup = (RelativeLayout) findViewById(R.id.quickFilterPopup);
        quickFilterPopup.setVisibility(View.GONE);
        quickFilter_baseLayout = (RelativeLayout) findViewById(R.id.quickFilter_baseLayout);
        qfDoneLayout = (RelativeLayout) findViewById(R.id.qfDoneLayout);
        quickFilter_BorderLayout = (RelativeLayout) findViewById(R.id.quickFilter_BorderLayout);
        targetListView = (ListView) findViewById(R.id.targetListView);
        target_segmented = (SegmentedGroup) findViewById(R.id.target_segmented);
        target_core = (RadioButton) findViewById(R.id.target_core);
        target_fashion = (RadioButton) findViewById(R.id.target_fashion);
        target_fashion.toggle();
        Toggle_target_fav = (ToggleButton) findViewById(R.id.toggle_top_fav);
        //seasonGroup
        checkCurrent = (CheckBox) findViewById(R.id.checkCurrent);
        checkPrevious = (CheckBox) findViewById(R.id.checkPrevious);
        checkOld = (CheckBox) findViewById(R.id.checkOld);
        checkUpcoming = (CheckBox) findViewById(R.id.checkUpcoming);
        //Time
        checkWTD = (RadioButton) findViewById(R.id.checkWTD);
        checkL4W = (RadioButton) findViewById(R.id.checkL4W);
        checkYTD = (RadioButton) findViewById(R.id.checkYTD);
        //Title
        checkDept = (RadioButton) findViewById(R.id.checkDept);
        checkCategory = (RadioButton) findViewById(R.id.checkCategory);
        checkPlanClass = (RadioButton) findViewById(R.id.checkPlanClass);

        //Seek Bar
        TargetSeek = (SeekBar) findViewById(R.id.targetSeek);
        targetMax = (TextView) findViewById(R.id.targetMax);


        //Target ROS
        /*checktwo = (CheckBox) findViewById(R.id.checktwo);
        checkthree = (CheckBox) findViewById(R.id.checkthree);
        checkfour = (CheckBox) findViewById(R.id.checkfour);
        checkfive = (CheckBox) findViewById(R.id.checkfive);
        checksix = (CheckBox) findViewById(R.id.checksix);
        checkseven = (CheckBox) findViewById(R.id.checkseven);
        checkeight = (CheckBox) findViewById(R.id.checkeight);
        checknine = (CheckBox) findViewById(R.id.checknine);
        checkten = (CheckBox) findViewById(R.id.checkten);*/

        checkCurrent.setOnClickListener(this);
        checkPrevious.setOnClickListener(this);
        checkOld.setOnClickListener(this);
        checkUpcoming.setOnClickListener(this);

        checkWTD.setOnClickListener(this);
        checkL4W.setOnClickListener(this);
        checkYTD.setOnClickListener(this);
        TargetSeek.setOnSeekBarChangeListener(this);

        checkDept.setOnClickListener(this);
        checkCategory.setOnClickListener(this);
        checkPlanClass.setOnClickListener(this);
/*
        checktwo.setOnClickListener(this);
        checkthree.setOnClickListener(this);
        checkfour.setOnClickListener(this);
        checkfive.setOnClickListener(this);
        checksix.setOnClickListener(this);
        checkseven.setOnClickListener(this);
        checkeight.setOnClickListener(this);
        checknine.setOnClickListener(this);
        checkten.setOnClickListener(this);*/

        qfDoneLayout.setOnClickListener(this);
        target_segmented.setOnCheckedChangeListener(TargetStockExceptionActivity.this);
        target_BtnBack.setOnClickListener(TargetStockExceptionActivity.this);
        target_BtnFilter.setOnClickListener(this);
        target_quickFilter.setOnClickListener(this);
        quickFilter_baseLayout.setOnClickListener(this);
        quickFilter_BorderLayout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.target_imageBtnBack:
               /* Intent intent = new Intent(TargetStockExceptionActivity.this, DashBoardActivity.class);
                intent.putExtra("BACKTO","inventory");
                startActivity(intent);*/
                finish();
                break;
            case R.id.toggle_target_fav:
                if (Toggle_target_fav.isChecked()) {
                    Toggle_target_fav.setChecked(true);
                } else {
                    Toggle_target_fav.setChecked(false);
                }

                break;
            case R.id.target_imgfilter:
                Intent intent1 = new Intent(TargetStockExceptionActivity.this, InventoryFilterActivity.class);
                intent1.putExtra("checkfrom", "targetStockException");
                startActivity(intent1);
                //finish();
                break;
            case R.id.target_quickFilter:
                quickFilterPopup.setVisibility(View.VISIBLE);
                break;
            case R.id.quickFilter_baseLayout:
                if (checkSeasonGpVal == null && checkTimeVal == null && checkTitleVal == null) {
                    checkCurrent.setChecked(true);
                    checkPrevious.setChecked(false);
                    checkOld.setChecked(false);
                    checkUpcoming.setChecked(false);

                    checkWTD.setChecked(false);
                    checkL4W.setChecked(false);
                    checkYTD.setChecked(true);

                    checkDept.setChecked(true);
                    checkCategory.setChecked(false);
                    checkPlanClass.setChecked(false);


                    TargetSeek.setProgress(70);




                 /*   checktwo.setChecked(false);
                    checkthree.setChecked(false);
                    checkfour.setChecked(false);
                    checkfive.setChecked(false);
                    checksix.setChecked(false);
                    checkseven.setChecked(true);
                    checkeight.setChecked(false);
                    checknine.setChecked(false);
                    checkten.setChecked(false);*/

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

                    switch (checkTimeVal.toString()) {
                        case "CheckWTD":
                            checkWTD.setChecked(true);
                            checkL4W.setChecked(false);
                            checkYTD.setChecked(false);
                            break;
                        case "CheckL4W":
                            checkL4W.setChecked(true);
                            checkWTD.setChecked(false);
                            checkYTD.setChecked(false);
                            break;
                        case "CheckYTD":
                            checkYTD.setChecked(true);
                            checkWTD.setChecked(false);
                            checkL4W.setChecked(false);
                            break;
                    }
                    switch (checkTitleVal.toString()) {
                        case "CheckDept":
                            checkDept.setChecked(true);
                            checkPlanClass.setChecked(false);
                            checkCategory.setChecked(false);
                            break;
                        case "CheckCategory":
                            checkCategory.setChecked(true);
                            checkDept.setChecked(false);
                            checkPlanClass.setChecked(false);
                            break;
                        case "CheckPlanClass":
                            checkPlanClass.setChecked(true);
                            checkDept.setChecked(false);
                            checkCategory.setChecked(false);
                            break;
                    }
                   /* switch (checkTargetROSVal) {
                        case 2:
                            checktwo.setChecked(true);
                            checkthree.setChecked(false);
                            checkfour.setChecked(false);
                            checkfive.setChecked(false);
                            checksix.setChecked(false);
                            checkseven.setChecked(false);
                            checkeight.setChecked(false);
                            checknine.setChecked(false);
                            checkten.setChecked(false);

                            break;
                        case 3:
                            checktwo.setChecked(false);
                            checkthree.setChecked(true);
                            checkfour.setChecked(false);
                            checkfive.setChecked(false);
                            checksix.setChecked(false);
                            checkseven.setChecked(false);
                            checkeight.setChecked(false);
                            checknine.setChecked(false);
                            checkten.setChecked(false);

                            break;
                        case 4:
                            checktwo.setChecked(false);
                            checkthree.setChecked(false);
                            checkfour.setChecked(true);
                            checkfive.setChecked(false);
                            checksix.setChecked(false);
                            checkseven.setChecked(false);
                            checkeight.setChecked(false);
                            checknine.setChecked(false);
                            checkten.setChecked(false);

                            break;
                        case 5:
                            checktwo.setChecked(false);
                            checkthree.setChecked(false);
                            checkfour.setChecked(false);
                            checkfive.setChecked(true);
                            checksix.setChecked(false);
                            checkseven.setChecked(false);
                            checkeight.setChecked(false);
                            checknine.setChecked(false);
                            checkten.setChecked(false);
                            break;
                        case 6:
                            checktwo.setChecked(false);
                            checkthree.setChecked(false);
                            checkfour.setChecked(false);
                            checkfive.setChecked(false);
                            checksix.setChecked(true);
                            checkseven.setChecked(false);
                            checkeight.setChecked(false);
                            checknine.setChecked(false);
                            checkten.setChecked(false);
                            break;
                        case 7:
                            checktwo.setChecked(false);
                            checkthree.setChecked(false);
                            checkfour.setChecked(false);
                            checkfive.setChecked(false);
                            checksix.setChecked(false);
                            checkseven.setChecked(true);
                            checkeight.setChecked(false);
                            checknine.setChecked(false);
                            checkten.setChecked(false);
                            break;
                        case 8:
                            checktwo.setChecked(false);
                            checkthree.setChecked(false);
                            checkfour.setChecked(false);
                            checkfive.setChecked(false);
                            checksix.setChecked(false);
                            checkseven.setChecked(false);
                            checkeight.setChecked(true);
                            checknine.setChecked(false);
                            checkten.setChecked(false);
                            break;
                        case 9:
                            checktwo.setChecked(false);
                            checkthree.setChecked(false);
                            checkfour.setChecked(false);
                            checkfive.setChecked(false);
                            checksix.setChecked(false);
                            checkseven.setChecked(false);
                            checkeight.setChecked(false);
                            checknine.setChecked(true);
                            checkten.setChecked(false);
                            break;
                        case 10:
                            checktwo.setChecked(false);
                            checkthree.setChecked(false);
                            checkfour.setChecked(false);
                            checkfive.setChecked(false);
                            checksix.setChecked(false);
                            checkseven.setChecked(false);
                            checkeight.setChecked(false);
                            checknine.setChecked(false);
                            checkten.setChecked(true);
                            break;
*/


                }
                quickFilterPopup.setVisibility(View.GONE);
                break;


            case R.id.qfDoneLayout:

                if (Reusable_Functions.chkStatus(context)) {

                    checkTargetROSVal = setValue;
                    //seasongroup
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
                        Log.e("Uncheck1", "----" + checkSeasonGpVal);
                    }
                    //Time
                    if (checkWTD.isChecked()) {
                        popupWTD();
                        checkTimeVal = "CheckWTD";
                        quickFilterPopup.setVisibility(View.GONE);
                    } else if (checkL4W.isChecked()) {
                        popupL4W();
                        checkTimeVal = "CheckL4W";
                        quickFilterPopup.setVisibility(View.GONE);

                    } else if (checkYTD.isChecked()) {
                        popupYTD();
                        checkTimeVal = "CheckYTD";
                        quickFilterPopup.setVisibility(View.GONE);
                    } else {
                        Log.e("Uncheck2", "----" + checkTimeVal);
                    }
                    //Title
                    if (checkDept.isChecked()) {
                        popupDept();
                        checkTitleVal = "CheckDept";
                        quickFilterPopup.setVisibility(View.GONE);
                    } else if (checkCategory.isChecked()) {
                        popupCategory();
                        checkTitleVal = "CheckCategory";
                        quickFilterPopup.setVisibility(View.GONE);

                    } else if (checkPlanClass.isChecked()) {
                        popupPlanClass();
                        checkTimeVal = "CheckPlanClass";
                        quickFilterPopup.setVisibility(View.GONE);
                    } else {
                        Log.e("Uncheck3", "----" + checkTitleVal);
                    }
                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();

                }
                break;

            //Targer ROS
            /*    if (checktwo.isChecked()) {
                    //popuptwo();
                    checkTargetROSVal = 2;
                    quickFilterPopup.setVisibility(View.GONE);
                } else if (checkthree.isChecked()) {
                    //popupthree();
                     checkTargetROSVal = 3;
                    quickFilterPopup.setVisibility(View.GONE);

                } else if (checkfour.isChecked()) {
                    //popupfour();
                    checkTargetROSVal = 4;
                    quickFilterPopup.setVisibility(View.GONE);
                }
                else if (checkfive.isChecked()) {
                    //popupfive();
                    checkTargetROSVal = 5;
                    quickFilterPopup.setVisibility(View.GONE);
                }
                else if (checksix.isChecked()) {
                    //popupsix();
                    checkTargetROSVal = 6;
                    quickFilterPopup.setVisibility(View.GONE);
                }
                else if (checkseven.isChecked()) {
                    //popupseven();
                    checkTargetROSVal =7;
                    quickFilterPopup.setVisibility(View.GONE);
                }
                else if (checkeight.isChecked()) {
                    //popupeight();
                    checkTargetROSVal = 8;
                    quickFilterPopup.setVisibility(View.GONE);
                }
                else if (checknine.isChecked()) {
                    //popupfour();
                    checkTargetROSVal = 9;
                    quickFilterPopup.setVisibility(View.GONE);
                }
                else if (checkten.isChecked()) {
                    //popupfour();
                    checkTargetROSVal = 10;
                    quickFilterPopup.setVisibility(View.GONE);
                }


                else {
                    Log.e("Uncheck4","----"+checkTargetROSVal);
                }*/


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
            case R.id.checkWTD:
                checkWTD.setChecked(true);
                checkL4W.setChecked(false);
                checkYTD.setChecked(false);
                break;
            case R.id.checkL4W:
                checkL4W.setChecked(true);
                checkWTD.setChecked(false);
                checkYTD.setChecked(false);
                break;
            case R.id.checkYTD:
                checkYTD.setChecked(true);
                checkWTD.setChecked(false);
                checkL4W.setChecked(false);
                break;
            case R.id.checkDept:
                checkDept.setChecked(true);
                checkPlanClass.setChecked(false);
                checkCategory.setChecked(false);
                break;
            case R.id.checkCategory:
                checkDept.setChecked(false);
                checkPlanClass.setChecked(false);
                checkCategory.setChecked(true);
                break;
            case R.id.checkPlanClass:
                checkDept.setChecked(false);
                checkPlanClass.setChecked(true);
                checkCategory.setChecked(false);
                break;
     /*       case R.id.checktwo:
                checktwo.setChecked(true);
                checkthree.setChecked(false);
                checkfour.setChecked(false);
                checkfive.setChecked(false);
                checksix.setChecked(false);
                checkseven.setChecked(false);
                checkeight.setChecked(false);
                checknine.setChecked(false);
                checkten.setChecked(false);
                break;
            case R.id.checkthree:
                checktwo.setChecked(false);
                checkthree.setChecked(true);
                checkfour.setChecked(false);
                checkfive.setChecked(false);
                checksix.setChecked(false);
                checkseven.setChecked(false);
                checkeight.setChecked(false);
                checknine.setChecked(false);
                checkten.setChecked(false);
                break;
            case R.id.checkfour:
                checktwo.setChecked(false);
                checkthree.setChecked(false);
                checkfour.setChecked(true);
                checkfive.setChecked(false);
                checksix.setChecked(false);
                checkseven.setChecked(false);
                checkeight.setChecked(false);
                checknine.setChecked(false);
                checkten.setChecked(false);
                break;
            case R.id.checkfive:
                checktwo.setChecked(false);
                checkthree.setChecked(false);
                checkfour.setChecked(false);
                checkfive.setChecked(true);
                checksix.setChecked(false);
                checkseven.setChecked(false);
                checkeight.setChecked(false);
                checknine.setChecked(false);
                checkten.setChecked(false);
                break;
            case R.id.checksix:
                checktwo.setChecked(false);
                checkthree.setChecked(false);
                checkfour.setChecked(false);
                checkfive.setChecked(false);
                checksix.setChecked(true);
                checkseven.setChecked(false);
                checkeight.setChecked(false);
                checknine.setChecked(false);
                checkten.setChecked(false);
                break;
            case R.id.checkseven:
                checktwo.setChecked(false);
                checkthree.setChecked(false);
                checkfour.setChecked(false);
                checkfive.setChecked(false);
                checksix.setChecked(false);
                checkseven.setChecked(true);
                checkeight.setChecked(false);
                checknine.setChecked(false);
                checkten.setChecked(false);
                break;
            case R.id.checkeight:
                checktwo.setChecked(false);
                checkthree.setChecked(false);
                checkfour.setChecked(false);
                checkfive.setChecked(false);
                checksix.setChecked(false);
                checkseven.setChecked(false);
                checkeight.setChecked(true);
                checknine.setChecked(false);
                checkten.setChecked(false);
                break;
            case R.id.checknine:
                checktwo.setChecked(false);
                checkthree.setChecked(false);
                checkfour.setChecked(false);
                checkfive.setChecked(false);
                checksix.setChecked(false);
                checkseven.setChecked(false);
                checkeight.setChecked(false);
                checknine.setChecked(true);
                checkten.setChecked(false);
                break;
            case R.id.checkten:
                checktwo.setChecked(false);
                checkthree.setChecked(false);
                checkfour.setChecked(false);
                checkfive.setChecked(false);
                checksix.setChecked(false);
                checkseven.setChecked(false);
                checkeight.setChecked(false);
                checknine.setChecked(false);
                checkten.setChecked(true);
                break;*/
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
            targetStockList.clear();
            requestTargetStockExcepApi();
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
            targetStockList.clear();
            requestTargetStockExcepApi();
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
            targetStockList.clear();
            requestTargetStockExcepApi();
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
            targetStockList.clear();
            requestTargetStockExcepApi();
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }

    }

    private void popupWTD() {
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            limit = 10;
            offsetvalue = 0;
            top = 10;
            view = "WTD";
            targetStockList.clear();
            requestTargetStockExcepApi();
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }

    }

    private void popupL4W() {
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            limit = 10;
            offsetvalue = 0;
            top = 10;
            view = "L4W";
            targetStockList.clear();
            requestTargetStockExcepApi();
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }

    }

    private void popupYTD() {
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            limit = 10;
            offsetvalue = 0;
            top = 10;
            view = "STD";
            targetStockList.clear();
            requestTargetStockExcepApi();
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }

    }

    private void popupDept() {
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            limit = 10;
            offsetvalue = 0;
            top = 10;
            level = 1;
            targetStockList.clear();
            requestTargetStockExcepApi();
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }

    }

    private void popupCategory() {
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            limit = 10;
            offsetvalue = 0;
            top = 10;
            level = 2;
            targetStockList.clear();
            requestTargetStockExcepApi();
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }

    }

    private void popupPlanClass() {
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            limit = 10;
            offsetvalue = 0;
            top = 10;
            level = 3;
            targetStockList.clear();
            requestTargetStockExcepApi();
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.target_core:
                if (target_core.isChecked()) {
                    limit = 10;
                    offsetvalue = 0;
                    top = 10;
                    corefashion = "Core";
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.hDialog();
                        Reusable_Functions.sDialog(context, "Loading data...");
                        targetStockList.clear();
                        targetListView.setVisibility(View.GONE);
                        coreSelection = true;
                        requestTargetStockExcepApi();
                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.target_fashion:
                if (target_fashion.isChecked()) {
                    limit = 10;
                    offsetvalue = 0;
                    top = 10;
                    corefashion = "Fashion";
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.hDialog();
                        Reusable_Functions.sDialog(context, "Loading data...");
                        targetStockList.clear();
                        targetListView.setVisibility(View.GONE);
                        coreSelection = false;
                        requestTargetStockExcepApi();
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
   /*     Intent intent = new Intent(TargetStockExceptionActivity.this, DashBoardActivity.class);
        intent.putExtra("BACKTO","inventory");
        startActivity(intent);*/
        finish();
    }

    private void scalingValue(int progress) {
        int Result = progress / 10;
        if (2 >= Result) {
            targetMax.setText("2");
            setValue = 2;
        } else if (2 < Result) {
            targetMax.setText(String.valueOf(Result));
            setValue = Result;
        }

    }


    //>>>>>>>Seek Bar for Quick filter

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        scalingValue(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.e(TAG, "onStopTrackingTouch: " + seekBar.getMax());

    }
}
