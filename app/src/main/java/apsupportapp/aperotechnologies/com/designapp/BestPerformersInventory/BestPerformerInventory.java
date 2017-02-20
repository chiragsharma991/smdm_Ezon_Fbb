package apsupportapp.aperotechnologies.com.designapp.BestPerformersInventory;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import apsupportapp.aperotechnologies.com.designapp.R;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.DashBoardActivity;
import apsupportapp.aperotechnologies.com.designapp.FreshnessIndex.InventoryFilterActivity;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;

import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesFilterActivity;
import apsupportapp.aperotechnologies.com.designapp.SkewedSize.SkewedSizeAdapter;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;
import info.hoang8f.android.segmented.SegmentedGroup;


public class BestPerformerInventory extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener {

    static TextView BestInvent_txtStoreCode, BestInvent_txtStoreName;
    RelativeLayout BestInvent_BtnBack, BestInvent_imgfilter, BestInvent_quickFilter, quickFilterPopup,
            quickFilter_baseLayout, BestQfDoneLayout, BestQuickFilterBorder;
    RunningPromoListDisplay BestInventSizeListDisplay;
    private SharedPreferences sharedPreferences;
    RadioButton CheckWTD, CheckL4W, CheckSTD;
    String userId, bearertoken;
    private TextView Toolbar_title;
    String TAG = "BestPerformerInventory";
    private int count = 0;
    private int limit = 10;
    private int offsetvalue = 0;
    private int top = 10;
    private int popPromo = 0;
    Context context = this;
    private static String title="Best";
    private RequestQueue queue;
    private Gson gson;
    ListView BestInventListview;
    ArrayList<RunningPromoListDisplay> BestInventList;
    private int focusposition = 0;
    private boolean userScrolled;
    private BestPerformerInventoryAdapter bestPerformerInventoryAdapter;
    private View footer;
    private String lazyScroll = "OFF";
    private static String seasonGroup = "Current";
    private SegmentedGroup BestInvent_segmented;
    private RadioButton BestInvent_core, BestInvent_fashion;
    private ToggleButton Toggle_bestInvent_fav;
    private static String corefashion = "Fashion";
    private ImageView Skewed_quickFilter;
    private static String orderbycol = "9";
    private RelativeLayout Bst_sortInventory;
    private LinearLayout BstInventory_salesU, BstInventory_salesThru, BstInventory_Fwd, BstInventory_coverNsell;
    private RadioButton BstInventory_salesU_chk, BstInventory_salesThru_chk, BstInventory_Fwd_chk, BstInventory_coverNsell_chk;
    private RelativeLayout BaseLayoutInventory;
    private static String checkValueIs = null, checkTimeValueIs = null;
    private static String view = "STD";
    private Switch BestAndWorst;
    private static String orderby = "DESC";
    private CheckBox BestCheckCurrent, BestCheckPrevious, BestCheckOld, BestCheckUpcoming;
    private boolean coreSelection = false;
    public static Activity bestperoformer;
    private boolean from_filter=false;
    private String selectedString="";
    private boolean toggleClick=false;
    private boolean worstToggle=false;

    //orderby  view  orderbycol  corefashion  seasonGroup  BestInvent_core  BestInvent_fashion  BestAndWorst  checkValueIs  checkTimeValueIs
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_performer_inventory);
        getSupportActionBar().hide();//
        initalise();
        BstInventory_salesThru_chk.setChecked(true);
        BaseLayoutInventory.setVisibility(View.GONE);
        BestInventListview.setVisibility(View.VISIBLE);
        gson = new Gson();
        bestperoformer=this;
        BestInventList = new ArrayList<RunningPromoListDisplay>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Log.e(TAG, "userID and token" + userId + "and this is" + bearertoken);
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        BestInventListview.setTag("FOOTER");

        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            offsetvalue = 0;
            limit = 10;
            count = 0;
            top = 10;
            if (getIntent().getStringExtra("selectedDept") == null) {
                from_filter = false;
            } else if (getIntent().getStringExtra("selectedDept") != null) {
                selectedString = getIntent().getStringExtra("selectedDept");
                //   selectedString = selectedString.replace(" ","%20");
                from_filter = true;

            }

            RetainFromMain_filter();
            requestRunningPromoApi(selectedString);
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }

        footer = getLayoutInflater().inflate(R.layout.bestpromo_footer, null);
        BestInventListview.addFooterView(footer);

        // bestPromoAdapter = new BestPromoAdapter(BestpromoList,context);

        // footer.setVisibility(View.GONE);
        // BestPerformanceListView.setAdapter(bestPromoAdapter);


    }

    private void RetainFromMain_filter()
    {
        toggleClick=true;


        if(corefashion.equals("Fashion"))
        {
            coreSelection=false;
            BestInvent_fashion.toggle();

        }else
        {
            coreSelection=true;
            BestInvent_core.toggle();
        }
        if(title.equals("Worst"))
        {
            worstToggle=true;
            Toolbar_title.setText("Worst Performers");
            BestAndWorst.setChecked(true);
        }

        baseclick();
        sortRetain();
    }

    private void sortRetain()
    {
        switch (orderbycol.toString())
        {
            case "6":
                BstInventory_salesU_chk.setChecked(true);
                BstInventory_salesThru_chk.setChecked(false);
                BstInventory_Fwd_chk.setChecked(false);
                BstInventory_coverNsell_chk.setChecked(false);
                break;
            case "9":
                BstInventory_salesU_chk.setChecked(false);
                BstInventory_salesThru_chk.setChecked(true);
                BstInventory_Fwd_chk.setChecked(false);
                BstInventory_coverNsell_chk.setChecked(false);
                break;
            case "10":
                BstInventory_salesU_chk.setChecked(false);
                BstInventory_salesThru_chk.setChecked(false);
                BstInventory_Fwd_chk.setChecked(true);
                BstInventory_coverNsell_chk.setChecked(false);
                break;
            case "10,9":
                BstInventory_salesU_chk.setChecked(false);
                BstInventory_salesThru_chk.setChecked(false);
                BstInventory_Fwd_chk.setChecked(false);
                BstInventory_coverNsell_chk.setChecked(true);
                break;
        }
    }

    private void baseclick()
    {
        if (checkTimeValueIs == null && checkValueIs == null) {
            BestCheckCurrent.setChecked(true);
            BestCheckPrevious.setChecked(false);
            BestCheckOld.setChecked(false);
            BestCheckUpcoming.setChecked(false);
            CheckWTD.setChecked(false);
            CheckL4W.setChecked(false);
            CheckSTD.setChecked(true);


        } else {

//in this checkvalueIs  save the previous done condition params and call to true or false


            switch (checkValueIs.toString()) {
                case "BestCheckCurrent":
                    BestCheckCurrent.setChecked(true);
                    BestCheckPrevious.setChecked(false);
                    BestCheckOld.setChecked(false);
                    BestCheckUpcoming.setChecked(false);
                    Log.i(TAG, "BestCheckCurrent is checked");
                    break;
                case "BestCheckPrevious":
                    BestCheckCurrent.setChecked(false);
                    BestCheckPrevious.setChecked(true);
                    BestCheckOld.setChecked(false);
                    BestCheckUpcoming.setChecked(false);
                    Log.i(TAG, "BestCheckPrevious is checked");
                    break;
                case "BestCheckOld":
                    BestCheckCurrent.setChecked(false);
                    BestCheckPrevious.setChecked(false);
                    BestCheckOld.setChecked(true);
                    BestCheckUpcoming.setChecked(false);
                    Log.i(TAG, "BestCheckOld is checked");
                    break;
                case "BestCheckUpcoming":
                    BestCheckCurrent.setChecked(false);
                    BestCheckPrevious.setChecked(false);
                    BestCheckOld.setChecked(false);
                    BestCheckUpcoming.setChecked(true);
                    Log.i(TAG, "BestCheckUpcoming is checked");
                    break;
                default:
                    break;

            }
            switch (checkTimeValueIs.toString()) {
                case "CheckWTD":
                    CheckWTD.setChecked(true);
                    CheckL4W.setChecked(false);
                    CheckSTD.setChecked(false);
                    Log.i(TAG, "CheckWTD is checked");
                    break;
                case "CheckL4W":
                    CheckWTD.setChecked(false);
                    CheckL4W.setChecked(true);
                    CheckSTD.setChecked(false);
                    Log.i(TAG, "CheckL4W is checked");
                    break;
                case "CheckSTD":
                    CheckWTD.setChecked(false);
                    CheckL4W.setChecked(false);
                    CheckSTD.setChecked(true);
                    Log.i(TAG, "CheckSTD is checked");
                    break;
                default:
                    break;


            }
        }


    }

    private void requestRunningPromoApi(final String selectedString) {


        if (Reusable_Functions.chkStatus(context)) {

            String url;
            if (from_filter) {
                if (coreSelection) {

                    //core selection without season params

                    url = ConstsCore.web_url + "/v1/display/inventorybestworstperformers/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&orderby=" + orderby + "&orderbycol=" + orderbycol +"&level=" + SalesFilterActivity.level_filter + selectedString + "&top=" + top + "&corefashion=" + corefashion + "&view=" + view;
                } else {

                    // fashion select with season params

                    url = ConstsCore.web_url + "/v1/display/inventorybestworstperformers/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&orderby=" + orderby + "&orderbycol=" + orderbycol + "&level=" + SalesFilterActivity.level_filter + selectedString + "&top=" + top + "&corefashion=" + corefashion + "&seasongroup=" + seasonGroup + "&view=" + view;
                }
            } else {
                if (coreSelection) {

                    //core selection without season params

                    url = ConstsCore.web_url + "/v1/display/inventorybestworstperformers/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&orderby=" + orderby + "&orderbycol=" + orderbycol + "&top=" + top + "&corefashion=" + corefashion + "&view=" + view;
                } else {

                    // fashion select with season params

                    url = ConstsCore.web_url + "/v1/display/inventorybestworstperformers/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&orderby=" + orderby + "&orderbycol=" + orderbycol + "&top=" + top + "&corefashion=" + corefashion + "&seasongroup=" + seasonGroup + "&view=" + view;
                }
            }


            Log.e(TAG, "URL" + url);

            final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.i(TAG, "inventorybestworstperformers Option : " + " " + response);
                            Log.i(TAG, "response" + "" + response.length());
                            BestInventListview.setVisibility(View.VISIBLE);


                            try {

                                if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                    Reusable_Functions.hDialog();
                                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                    BestInventListview.removeFooterView(footer);
                                    BestInventListview.setTag("FOOTER_REMOVE");
                                    if (BestInventList.size() == 0) {
                                        BestInventListview.setVisibility(View.GONE);
                                        return;


                                    }
                                    return;


                                } else if (response.length() == limit) {


                                    Log.e(TAG, "Top eql limit");
                                    for (int i = 0; i < response.length(); i++) {

                                        BestInventSizeListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                        BestInventList.add(BestInventSizeListDisplay);

                                    }
                                    offsetvalue = offsetvalue + 10;
                                    top = top + 10;
                                    Log.e(TAG, "list size is" + BestInventList.size());

                                    //  count++;

                                    // requestRunningPromoApi();

                                } else if (response.length() < limit) {
                                    Log.e(TAG, "promo /= limit");
                                    for (int i = 0; i < response.length(); i++) {

                                        BestInventSizeListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                        BestInventList.add(BestInventSizeListDisplay);

                                    }
                                    offsetvalue = offsetvalue + 10;
                                    top = top + 10;
                                }


                           /* if(popPromo==10)
                            {
                                topOptionAdapter = new TopOptionAdapter(TopOptionList,context);
                                TopOptionListView.setAdapter(topOptionAdapter);
                                popPromo=0;

                            }*/

                                Log.e(TAG, "set adapter start");

                                if (lazyScroll.equals("ON")) {
                                    bestPerformerInventoryAdapter.notifyDataSetChanged();
                                    lazyScroll = "OFF";
                                    footer.setVisibility(View.GONE);

                                } else {
                                    bestPerformerInventoryAdapter = new BestPerformerInventoryAdapter(BestInventList, context);
                                    BestInventListview.setAdapter(bestPerformerInventoryAdapter);


                                }


                                Reusable_Functions.hDialog();


                            } catch (Exception e) {
                                BestInventList.clear();
                                bestPerformerInventoryAdapter.notifyDataSetChanged();
                                BestInventListview.setVisibility(View.GONE);
                                //    BestInvent_fashion.setEnabled(true);
                                //   BestInvent_core.setEnabled(true);
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "Data failed...", Toast.LENGTH_SHORT).show();
                                BestInventListview.removeFooterView(footer);
                                BestInventListview.setTag("FOOTER_REMOVE");
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
                            BestInventListview.removeFooterView(footer);
                            BestInventListview.setTag("FOOTER_REMOVE");
                            BestInventListview.setVisibility(View.GONE);
                     //       Log.e(TAG, "onErrorResponse  " + error.getMessage().toString());


                            String json = null;

                            NetworkResponse response = error.networkResponse;
                            if(response != null && response.data != null){
                                switch(response.statusCode){
                                    case 400:
                                        json = new String(response.data);
                                        json = trimMessage(json, "message");
                                        if(json != null) displayMessage(json);
                                        break;
                                }
                                //Additional cases
                            }



                            error.printStackTrace();
                        }

                        public String trimMessage(String json, String key){
                            String trimmedString = null;

                            try{
                                JSONObject obj = new JSONObject(json);
                                trimmedString = obj.getString(key);
                            } catch(JSONException e){
                                e.printStackTrace();
                                return null;
                            }

                            return trimmedString;
                        }

                        //Somewhere that has access to a context
                        public void displayMessage(String toastString){
                            Toast.makeText(context, toastString, Toast.LENGTH_LONG).show();
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

            BestInventListview.setOnScrollListener(new AbsListView.OnScrollListener() {
                public int VisibleItemCount, TotalItemCount, FirstVisibleItem;

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                    if (FirstVisibleItem + VisibleItemCount == TotalItemCount && scrollState == SCROLL_STATE_IDLE && lazyScroll.equals("OFF")) {

                        if (BestInventListview.getTag().equals("FOOTER_REMOVE")) {
                            BestInventListview.addFooterView(footer);
                            BestInventListview.setTag("FOOTER_ADDED");

                        }
                        footer.setVisibility(View.VISIBLE);
                        lazyScroll = "ON";
                        //BestInvent_fashion.setEnabled(false);
                        //BestInvent_core.setEnabled(false);
                        requestRunningPromoApi(selectedString);
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
            BestInventListview.removeFooterView(footer);
            BestInventListview.setTag("FOOTER_REMOVE");
            Reusable_Functions.hDialog();

        }
    }


    private void initalise() {
        BestInvent_txtStoreCode = (TextView) findViewById(R.id.bestInvent_txtStoreCode);
        BestInvent_txtStoreName = (TextView) findViewById(R.id.bestInvent_txtStoreName);
        Toolbar_title = (TextView) findViewById(R.id.toolbar_title);

        BestInvent_BtnBack = (RelativeLayout) findViewById(R.id.bestInvent_BtnBack);
        BestInvent_imgfilter = (RelativeLayout) findViewById(R.id.bestInvent_imgfilter);
        BestQuickFilterBorder = (RelativeLayout) findViewById(R.id.bestQuickFilterBorder);

        BestInventListview = (ListView) findViewById(R.id.bestInvent_ListView);

        BestAndWorst = (Switch) findViewById(R.id.bestNworstswitch);

        BestInvent_segmented = (SegmentedGroup) findViewById(R.id.bestInvent_segmented);
        BestInvent_quickFilter = (RelativeLayout) findViewById(R.id.bestInvent_quickFilter);

        BestInvent_core = (RadioButton) findViewById(R.id.bestInvent_core);
        BestInvent_fashion = (RadioButton) findViewById(R.id.bestInvent_fashion);
      //  BestInvent_fashion.toggle();
        Bst_sortInventory = (RelativeLayout) findViewById(R.id.bst_sortInventory);
        BaseLayoutInventory = (RelativeLayout) findViewById(R.id.baseLayoutInventory);

        BstInventory_salesU = (LinearLayout) findViewById(R.id.bstInventory_salesU);
        BstInventory_salesThru = (LinearLayout) findViewById(R.id.bstInventory_salesThru);
        BstInventory_Fwd = (LinearLayout) findViewById(R.id.bstInventory_Fwd);
        BstInventory_coverNsell = (LinearLayout) findViewById(R.id.bstInventory_coverNsell);

        BstInventory_salesU_chk = (RadioButton) findViewById(R.id.bstInventory_salesUchk);
        BstInventory_salesThru_chk = (RadioButton) findViewById(R.id.bstInventory_salesThruchk);
        BstInventory_Fwd_chk = (RadioButton) findViewById(R.id.bstInventory_Fwdchk);
        BstInventory_coverNsell_chk = (RadioButton) findViewById(R.id.bstInventory_coverNsellchk);


        quickFilterPopup = (RelativeLayout) findViewById(R.id.baseQuickFilterPopup);
        //quickFilter_baseLayout = (RelativeLayout)findViewById(R.id.bestQuickFilterPopup);
        BestQfDoneLayout = (RelativeLayout) findViewById(R.id.bestQfDoneLayout);
        quickFilterPopup.setVisibility(View.GONE);
        Toggle_bestInvent_fav = (ToggleButton) findViewById(R.id.toggle_bestInvent_fav);

        BestCheckCurrent = (CheckBox) findViewById(R.id.bestCheckCurrent);
        BestCheckPrevious = (CheckBox) findViewById(R.id.bestCheckPrevious);
        BestCheckOld = (CheckBox) findViewById(R.id.bestCheckOld);
        BestCheckUpcoming = (CheckBox) findViewById(R.id.bestCheckUpcoming);
        CheckWTD = (RadioButton) findViewById(R.id.checkWTD);
        CheckL4W = (RadioButton) findViewById(R.id.checkL4W);
        CheckSTD = (RadioButton) findViewById(R.id.checkSTD);

        BestInvent_segmented.setOnCheckedChangeListener(this);
        BestInvent_BtnBack.setOnClickListener(this);
        BestInvent_imgfilter.setOnClickListener(this);
        BestInvent_quickFilter.setOnClickListener(this);
        //  quickFilter_baseLayout.setOnClickListener(this);
        Bst_sortInventory.setOnClickListener(this);
        BaseLayoutInventory.setOnClickListener(this);
        BestQfDoneLayout.setOnClickListener(this);
        BestCheckCurrent.setOnClickListener(this);
        BestCheckPrevious.setOnClickListener(this);
        BestCheckOld.setOnClickListener(this);
        BestCheckUpcoming.setOnClickListener(this);
        BestQuickFilterBorder.setOnClickListener(this);
        quickFilterPopup.setOnClickListener(this);
        CheckWTD.setOnClickListener(this);
        CheckL4W.setOnClickListener(this);
        CheckSTD.setOnClickListener(this);
        BestAndWorst.setOnCheckedChangeListener(this);

        BstInventory_salesU.setOnClickListener(this);
        BstInventory_salesThru.setOnClickListener(this);
        BstInventory_Fwd.setOnClickListener(this);
        BstInventory_coverNsell.setOnClickListener(this);
        BestInvent_imgfilter.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bestInvent_BtnBack:
                onBackPressed();
                break;

         /*   case R.id.bestNworstswitch:
                BestAndWorstToggle();
                break;*/


            //pop up >>>
            case R.id.bst_sortInventory:
                sortFunction();
                break;
            case R.id.baseLayoutInventory:
                BaseLayoutInventory.setVisibility(View.GONE);
                break;
            case R.id.bstInventory_salesU:
                salesUPopUp();
                break;
            case R.id.bstInventory_salesThru:
                salesThruPopUp();
                break;
            case R.id.bstInventory_Fwd:
                FwdPopUp();
                break;
            case R.id.bstInventory_coverNsell:
                coverNsellPopUp();
                break;

            //pop up<<<<<

            case R.id.toggle_bestInvent_fav:
                if (Toggle_bestInvent_fav.isChecked()) {
                    Toggle_bestInvent_fav.setChecked(true);
                } else {
                    Toggle_bestInvent_fav.setChecked(false);
                }

                break;
            case R.id.bestInvent_imgfilter:
                Intent intent = new Intent(this, SalesFilterActivity.class);
                intent.putExtra("checkfrom", "bestPerformers");
                startActivity(intent);
                //   finish();
                break;


            //Quick filter>>>

            case R.id.bestInvent_quickFilter:
                filterFunction();
                break;

            // base layout and sorting fuction>>>>>>

            case R.id.baseQuickFilterPopup:

                baseclick();
                //overridePendingTransition(R.anim.startingfrom_right,R.anim.startingfrom_left);
              /*  TranslateAnimation animation = new TranslateAnimation(0,600,0,0); // new TranslateAnimation (float fromXDelta,float toXDelta, float fromYDelta, float toYDelta)

                animation.setDuration(2000); // animation duration
                animation.setRepeatCount(0); // animation repeat count
                animation.setRepeatMode(0); // repeat animation (left to right, right to left)

                animation.setFillAfter(true);
                quickFilterPopup.startAnimation(animation);
                quickFilterPopup.setVisibility(View.GONE);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        Log.e(TAG, "onAnimationStart: " );
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Log.e(TAG, "onAnimationEnd: " );
                        animation.cancel();


                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        Log.e(TAG, "onAnimationRepeat: " );

                    }
                });*/

                quickFilterPopup.setVisibility(View.GONE);

                break;

            case R.id.bestQfDoneLayout:

                //Time >>>if you press done then you pass checkTimeValueIs and checkValueIs params
                if (Reusable_Functions.chkStatus(context)) {


                    if (CheckWTD.isChecked()) {
                        checkTimeValueIs = "CheckWTD";
                        view = "WTD";
             /*       BestCheckCurrent.setChecked(false);
                    BestCheckPrevious.setChecked(false);
                    BestCheckOld.setChecked(false);*/

                    } else if (CheckL4W.isChecked()) {
                        checkTimeValueIs = "CheckL4W";
                        view = "L4W";

             /*       BestCheckCurrent.setChecked(false);
                    BestCheckPrevious.setChecked(false);
                    BestCheckOld.setChecked(false);*/

                    } else if (CheckSTD.isChecked()) {
                        checkTimeValueIs = "CheckSTD";
                        view = "STD";
             /*       BestCheckCurrent.setChecked(false);
                    BestCheckPrevious.setChecked(false);
                    BestCheckOld.setChecked(false);*/

                    }


                    //season group

                    if (BestCheckCurrent.isChecked()) {
                        checkValueIs = "BestCheckCurrent";
                        popupCurrent();
                    /*popupCurrent();
                    BestCheckPrevious.setChecked(false);
                    BestCheckOld.setChecked(false);
                    BestCheckUpcoming.setChecked(false);*/
                        quickFilterPopup.setVisibility(View.GONE);

                    } else if (BestCheckPrevious.isChecked()) {
                        checkValueIs = "BestCheckPrevious";
                        popupPrevious();
                   /* BestCheckOld.setChecked(false);
                    BestCheckUpcoming.setChecked(false);
                    BestCheckCurrent.setChecked(false);*/
                        quickFilterPopup.setVisibility(View.GONE);

                    } else if (BestCheckOld.isChecked()) {
                        checkValueIs = "BestCheckOld";
                        popupOld();
                /*    BestCheckUpcoming.setChecked(false);
                    BestCheckCurrent.setChecked(false);
                    BestCheckPrevious.setChecked(false);*/
                        quickFilterPopup.setVisibility(View.GONE);

                    } else if (BestCheckUpcoming.isChecked()) {
                        checkValueIs = "BestCheckUpcoming";
                        popupUpcoming();
             /*       BestCheckCurrent.setChecked(false);
                    BestCheckPrevious.setChecked(false);
                    BestCheckOld.setChecked(false);*/
                        quickFilterPopup.setVisibility(View.GONE);

                    } else {

                        CheckTimeDone();
                        quickFilterPopup.setVisibility(View.GONE);

                    }
                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();

                }


                break;
            case R.id.bestCheckCurrent:
                BestCheckCurrent.setChecked(true);
                BestCheckPrevious.setChecked(false);
                BestCheckOld.setChecked(false);
                BestCheckUpcoming.setChecked(false);
                break;
            case R.id.bestCheckPrevious:
                BestCheckPrevious.setChecked(true);
                BestCheckCurrent.setChecked(false);
                BestCheckOld.setChecked(false);
                BestCheckUpcoming.setChecked(false);
                break;
            case R.id.bestCheckOld:
                BestCheckOld.setChecked(true);
                BestCheckCurrent.setChecked(false);
                BestCheckPrevious.setChecked(false);
                BestCheckUpcoming.setChecked(false);
                break;
            case R.id.bestCheckUpcoming:
                BestCheckUpcoming.setChecked(true);
                BestCheckCurrent.setChecked(false);
                BestCheckOld.setChecked(false);
                BestCheckPrevious.setChecked(false);
                break;
            case R.id.checkWTD:
                CheckWTD.setChecked(true);
                CheckL4W.setChecked(false);
                CheckSTD.setChecked(false);
                break;
            case R.id.checkL4W:
                CheckL4W.setChecked(true);
                CheckSTD.setChecked(false);
                CheckWTD.setChecked(false);
                break;
            case R.id.checkSTD:
                CheckSTD.setChecked(true);
                CheckL4W.setChecked(false);
                CheckWTD.setChecked(false);
                break;


            //Quick filter<<<<<<<

        }

    }


    //Toggle switch>>>>>>>>

    private void BestAndWorstToggle() {

        if(worstToggle==false) {
            if (BestAndWorst.isChecked()) {
                limit = 10;
                offsetvalue = 0;
                top = 10;
                orderby = "ASC";
                title="Worst";
                Toolbar_title.setText("Worst Performers");
                Log.e(TAG, "BestAndWorstToggle:is checked log");
                BestInventList.clear();
                BestInventListview.setVisibility(View.GONE);
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.sDialog(this, "Loading.......");
                    requestRunningPromoApi(selectedString);
                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    BestInventListview.setVisibility(View.GONE);
                    Toolbar_title.setText("Worst Performers");


                }
            } else {
                limit = 10;
                offsetvalue = 0;
                top = 10;
                orderby = "DESC";
                title="Best";
                Toolbar_title.setText("Best Performers");
                Log.e(TAG, "BestAndWorstToggle:is unchecked log");
                BestInventList.clear();
                BestInventListview.setVisibility(View.GONE);
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.sDialog(this, "Loading.......");
                    requestRunningPromoApi(selectedString);
                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    BestInventListview.setVisibility(View.GONE);
                    Toolbar_title.setText("Best Performers");


                }
            }

        }else
        {
            worstToggle=false;
        }
    }

    private void FwdPopUp() {

        if (Reusable_Functions.chkStatus(context)) {
            if (BstInventory_Fwd_chk.isChecked()) {

                BstInventory_salesU_chk.setChecked(false);
                BstInventory_salesThru_chk.setChecked(false);
                BstInventory_Fwd_chk.setChecked(true);
                BstInventory_coverNsell_chk.setChecked(false);
                BaseLayoutInventory.setVisibility(View.GONE);
                Log.e(TAG, "FWD pop up if");


            } else if (!BstInventory_Fwd_chk.isChecked()) {
                BstInventory_salesU_chk.setChecked(false);
                BstInventory_salesThru_chk.setChecked(false);
                BstInventory_Fwd_chk.setChecked(true);
                BstInventory_coverNsell_chk.setChecked(false);
                orderbycol = "10";
                BestInventList.clear();
                Log.e(TAG, "FWD pop up else");
                Reusable_Functions.sDialog(this, "Loading.......");
                popPromo = 10;
                limit = 10;
                offsetvalue = 0;
                top = 10;
                requestRunningPromoApi(selectedString);
                BaseLayoutInventory.setVisibility(View.GONE);


            }


        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private void coverNsellPopUp() {
        if (Reusable_Functions.chkStatus(context)) {

            if (BstInventory_coverNsell_chk.isChecked()) {
                BstInventory_salesU_chk.setChecked(false);
                BstInventory_salesThru_chk.setChecked(false);
                BstInventory_Fwd_chk.setChecked(false);
                BstInventory_coverNsell_chk.setChecked(true);
                BaseLayoutInventory.setVisibility(View.GONE);
                Log.e(TAG, "coverNsell pop up if");


            } else if (!BstInventory_coverNsell_chk.isChecked()) {
                BstInventory_salesU_chk.setChecked(false);
                BstInventory_salesThru_chk.setChecked(false);
                BstInventory_Fwd_chk.setChecked(false);
                BstInventory_coverNsell_chk.setChecked(true);
                orderbycol = "10,9";
                BestInventList.clear();
                Log.e(TAG, "coverNsell pop up else");
                Reusable_Functions.sDialog(this, "Loading.......");
                popPromo = 10;
                limit = 10;
                offsetvalue = 0;
                top = 10;
                requestRunningPromoApi(selectedString);
                BaseLayoutInventory.setVisibility(View.GONE);

            }

        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private void salesThruPopUp() {
        if (Reusable_Functions.chkStatus(context)) {

            if (BstInventory_salesThru_chk.isChecked()) {

                BstInventory_salesU_chk.setChecked(false);
                BstInventory_salesThru_chk.setChecked(true);
                BstInventory_Fwd_chk.setChecked(false);
                BstInventory_coverNsell_chk.setChecked(false);

                BaseLayoutInventory.setVisibility(View.GONE);
                Log.e(TAG, "salesThru pop up if");

            } else if (!BstInventory_salesThru_chk.isChecked()) {

                BstInventory_salesU_chk.setChecked(false);
                BstInventory_salesThru_chk.setChecked(true);
                BstInventory_Fwd_chk.setChecked(false);
                BstInventory_coverNsell_chk.setChecked(false);
                orderbycol = "9";
                BestInventList.clear();
                Log.e(TAG, "salesThru pop up else");
                Reusable_Functions.sDialog(this, "Loading.......");
                popPromo = 10;
                limit = 10;
                offsetvalue = 0;
                top = 10;
                requestRunningPromoApi(selectedString);
                BaseLayoutInventory.setVisibility(View.GONE);

            }
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private void salesUPopUp() {

        if (Reusable_Functions.chkStatus(context)) {

            if (BstInventory_salesU_chk.isChecked()) {

                BstInventory_salesU_chk.setChecked(true);
                BstInventory_salesThru_chk.setChecked(false);
                BstInventory_Fwd_chk.setChecked(false);
                BstInventory_coverNsell_chk.setChecked(false);

                BaseLayoutInventory.setVisibility(View.GONE);
                Log.e(TAG, "salesUpopUp pop up if");
            } else if (!BstInventory_salesU_chk.isChecked()) {

                BstInventory_salesU_chk.setChecked(true);
                BstInventory_salesThru_chk.setChecked(false);
                BstInventory_Fwd_chk.setChecked(false);
                BstInventory_coverNsell_chk.setChecked(false);
                orderbycol = "6";
                BestInventList.clear();
                Log.e(TAG, "salesUpopUp pop up else");
                Reusable_Functions.sDialog(this, "Loading.......");
                popPromo = 10;
                limit = 10;
                offsetvalue = 0;
                top = 10;
                requestRunningPromoApi(selectedString);
                BaseLayoutInventory.setVisibility(View.GONE);

            }
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();

        }
    }

    private void sortFunction() {
        BaseLayoutInventory.setVisibility(View.VISIBLE);
    }

    private void popupCurrent() {
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            limit = 10;
            offsetvalue = 0;
            top = 10;
            seasonGroup = "Current";
            BestInventList.clear();
            Reusable_Functions.sDialog(this, "Loading.......");
            requestRunningPromoApi(selectedString);

        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private void popupPrevious() {
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            limit = 10;
            offsetvalue = 0;
            top = 10;
            seasonGroup = "Previous";
            BestInventList.clear();
            Reusable_Functions.sDialog(this, "Loading.......");
            requestRunningPromoApi(selectedString);

        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private void popupOld() {
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            limit = 10;
            offsetvalue = 0;
            top = 10;
            corefashion = "Core";
            seasonGroup = "Old";
            BestInventList.clear();
            Reusable_Functions.sDialog(this, "Loading.......");
            requestRunningPromoApi(selectedString);
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private void CheckTimeDone() {
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            limit = 10;
            offsetvalue = 0;
            top = 10;
            seasonGroup = "All";
            BestInventList.clear();
            Reusable_Functions.sDialog(this, "Loading.......");
            requestRunningPromoApi(selectedString);

        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }


    private void popupUpcoming() {
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            limit = 10;
            offsetvalue = 0;
            top = 10;
            corefashion = "Core";
            seasonGroup = "Upcoming";
            BestInventList.clear();
            Reusable_Functions.sDialog(this, "Loading.......");
            requestRunningPromoApi(selectedString);
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private void filterFunction() {

        quickFilterPopup.setVisibility(View.VISIBLE);

        //overridePendingTransition(R.anim.startingfrom_right,R.anim.startingfrom_left);
      /*  TranslateAnimation animation = new TranslateAnimation(100,0,0,0); // new TranslateAnimation (float fromXDelta,float toXDelta, float fromYDelta, float toYDelta)

        animation.setDuration(2000); // animation duration
        animation.setRepeatCount(0); // animation repeat count
        animation.setRepeatMode(0); // repeat animation (left to right, right to left)

        animation.setFillAfter(true);
        quickFilterPopup.startAnimation(animation);
        quickFilterPopup.setVisibility(View.VISIBLE);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.e(TAG, "onAnimationStart: " );
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.e(TAG, "onAnimationEnd: " );
                animation.cancel();


            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.e(TAG, "onAnimationRepeat: " );

            }
        });*/


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
/*        Intent intent = new Intent(context, DashBoardActivity.class);
        intent.putExtra("BACKTO","inventory");
        startActivity(intent);*/
        //orderby  view  orderbycol  corefashion  seasonGroup  BestInvent_core  BestInvent_fashion  BestAndWorst  checkValueIs  checkTimeValueIs

        orderby=null;view=null;orderbycol=null;corefashion=null;seasonGroup=null;checkValueIs=null;checkTimeValueIs=null;title=null;
        orderby="DESC";view="STD";orderbycol="9";corefashion="Fashion";seasonGroup="Current";checkValueIs=null;checkTimeValueIs=null;title="Best";

        finish();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if(toggleClick==false) {

            switch (checkedId) {
                case R.id.bestInvent_core:
                    if (BestInvent_core.isChecked()) {
                        limit = 10;
                        offsetvalue = 0;
                        top = 10;
                        corefashion = "Core";
                        lazyScroll = "OFF";
                        BestInventList.clear();
                        BestInventListview.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.sDialog(context, "Loading data...");
                            coreSelection = true;
                            requestRunningPromoApi(selectedString);
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            BestInventListview.setVisibility(View.GONE);

                        }
                    }
                    break;
                case R.id.bestInvent_fashion:
                    if (BestInvent_fashion.isChecked()) {
                        limit = 10;
                        offsetvalue = 0;
                        top = 10;
                        corefashion = "Fashion";
                        lazyScroll = "OFF";
                        BestInventListview.setVisibility(View.GONE);

                        BestInventList.clear();

                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.sDialog(this, "Loading.......");
                            coreSelection = false;
                            requestRunningPromoApi(selectedString);
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            BestInventListview.setVisibility(View.GONE);

                        }
                    }

                    break;


            }

        }else
        {
            toggleClick=false;

        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.bestNworstswitch:
                BestAndWorstToggle();
                break;

        }

    }
}
