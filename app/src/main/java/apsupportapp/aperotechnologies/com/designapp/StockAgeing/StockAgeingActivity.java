package apsupportapp.aperotechnologies.com.designapp.StockAgeing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesAnalysisFilter;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;
import info.hoang8f.android.segmented.SegmentedGroup;

import static apsupportapp.aperotechnologies.com.designapp.StockAgeing.AgeingAdapter.checkedValue_StockAgeing;

/**
 * Created by pamrutkar on 05/12/16.
 */

public class StockAgeingActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener ,TabLayout.OnTabSelectedListener {

    TextView stock_txtStoreCode, stock_txtStoreName;
    RelativeLayout stock_BtnBack, stock_BtnFilter, stock_quickFilter, quickFilterPopup, quickFilter_baseLayout, quickFilter_BorderLayout;
    RunningPromoListDisplay StockAgeingListDisplay;
    private SharedPreferences sharedPreferences;
    private LinearLayout qfDoneLayout;
    String userId, bearertoken,storeDescription;
    private static String seasongroup = "Current", stockageband; //, stockageband="100-200";
    private int count = 0;
    private int limit = 10;
    private int offsetvalue = 0;
    private int top = 10;
    CheckBox checkAgeing1, checkAgeing2, checkAgeing3;
    RadioButton checkCurrent, checkPrevious, checkOld, checkUpcoming;
    Context context = this;
    private RequestQueue queue;
    private Gson gson;
    ListView StockAgListView;
    ArrayList<RunningPromoListDisplay> StockAgeingList;
    StockAgeingAdapter stockAgeingAdapter;
    ExpandableListView expandableListView_ageing;
    private View footer;
    private String lazyScroll = "OFF";
    private SegmentedGroup stock_segmented;
    private RadioButton stock_fashion, stock_core;
    private ToggleButton Toggle_stock_fav;
    private static String corefashion = "Fashion";
    private static String checkSeasonGpVal = null;
    private static String checkAgeingVal = null;
    private boolean coreSelection = false;
    private boolean from_filter = false;
    private String selectedString = "", geoLevel2Code, lobId, isMultiStore, value, stockageband1,stockageband2,stockageband3;
    public static Activity stockAgeing;
    private boolean toggleClick = false;
    private TabLayout Tabview;
    private int filter_level;
    private ArrayList<String> ageing;
    private ArrayList<String> ageing_val;
    private AgeingAdapter listAdapter ;

    private ListView listAgeing;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_ageing);
        getSupportActionBar().hide();

        gson = new Gson();
        stockAgeing = this;
        ageing = new ArrayList<String>();
        ageing_val = new ArrayList<String>();

        StockAgeingList = new ArrayList<RunningPromoListDisplay>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLevel2Code = sharedPreferences.getString("concept","");
        lobId = sharedPreferences.getString("lobid","");
        isMultiStore = sharedPreferences.getString("isMultiStore","");
        value = sharedPreferences.getString("value","");
//        storeDescription = sharedPreferences.getString("storeDescription","");
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        initalise();
//        stock_txtStoreCode.setText(storeDescription.trim().substring(0,4));
//        stock_txtStoreName.setText(storeDescription.substring(5));
        StockAgListView.setTag("FOOTER");
        StockAgListView.setVisibility(View.VISIBLE);
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            offsetvalue = 0;
            limit = 10;
            count = 0;
            top = 10;
            if (getIntent().getStringExtra("selectedStringVal") == null) {
                from_filter = false;
                toggleClick=false;

            } else if (getIntent().getStringExtra("selectedStringVal") != null) {
                selectedString = getIntent().getStringExtra("selectedStringVal");
                filter_level  = getIntent().getIntExtra("selectedlevelVal",0);
                from_filter = true;
                toggleClick=true;
            }
            RetainFromMain_filter();
            if(Tabview.getTabAt(0).isSelected())
            {
                requestStockAgeingApi(selectedString);
            }

        }
        else
        {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
        footer = getLayoutInflater().inflate(R.layout.bestpromo_footer, null);
        StockAgListView.addFooterView(footer);

    }

    private void RetainFromMain_filter() {
      //  toggleClick = true;
        if (corefashion.equals("Fashion")) {
         //   stock_fashion.toggle();
            coreSelection = false;
            Tabview.getTabAt(0).select();

        } else {
          //  stock_core.toggle();
            coreSelection = true;
            Tabview.getTabAt(1).select();

        }
        baseclick();
    }

    private void baseclick()
    {
        if (checkSeasonGpVal == null && checkAgeingVal == null)
        {
            checkCurrent.setChecked(true);
            checkPrevious.setChecked(false);
            checkOld.setChecked(false);
            checkUpcoming.setChecked(false);
//            checkAgeing1.setChecked(false);
//            checkAgeing2.setChecked(false);
//            checkAgeing3.setChecked(false);
        }
        else
        {
            if(checkAgeingVal!=null)
            {
                switch (checkAgeingVal)
                {
                   /* case "CheckAgeing1":
                        Log.e("CheckAgeing1 ","");
//                        checkAgeing1.setChecked(true);
//                        checkAgeing2.setChecked(false);
//                        checkAgeing3.setChecked(false);
                        if(checkAgeing1.isChecked())
                        {
                            checkAgeing1.setChecked(false);
                        }
                        else
                        {
                            checkAgeing1.setChecked(true);

                        }
                        break;

                    case "CheckAgeing2":
                        Log.e("CheckAgeing2 ","");

                        // checkAgeing1.setChecked(false);
                      //  checkAgeing2.setChecked(true);
                       // checkAgeing3.setChecked(false);
                        if(checkAgeing2.isChecked())
                        {
                            checkAgeing2.setChecked(false);
                        }
                        else
                        {
                            checkAgeing2.setChecked(true);

                        }
                        break;
                    case "CheckAgeing3":
                        Log.e("CheckAgeing3 ","");

                        //  checkAgeing1.setChecked(false);
                      //  checkAgeing2.setChecked(false);
                     //   checkAgeing3.setChecked(true);
                        if(checkAgeing3.isChecked())
                        {
                            checkAgeing3.setChecked(false);
                        }
                        else
                        {
                            checkAgeing3.setChecked(true);

                        }
                        break;  */
                }
            }else{
//                checkAgeing1.setChecked(false);
//                checkAgeing2.setChecked(false);
//                checkAgeing3.setChecked(false);
            }

            switch (checkSeasonGpVal)
            {
                case "Current":
                    checkCurrent.setChecked(true);
                    checkPrevious.setChecked(false);
                    checkOld.setChecked(false);
                    checkUpcoming.setChecked(false);
                    break;

                case "Previous":
                    checkPrevious.setChecked(true);
                    checkCurrent.setChecked(false);
                    checkOld.setChecked(false);
                    checkUpcoming.setChecked(false);
                    break;

                case "Old":
                    checkOld.setChecked(true);
                    checkCurrent.setChecked(false);
                    checkPrevious.setChecked(false);
                    checkUpcoming.setChecked(false);
                    break;

                case "Upcoming":
                    checkUpcoming.setChecked(true);
                    checkCurrent.setChecked(false);
                    checkOld.setChecked(false);
                    checkPrevious.setChecked(false);
                    break;
            }


        }
    }

    private void requestStockAgeingApi(final String selectedString) {

        if (Reusable_Functions.chkStatus(context)) {
            String url;
            if (from_filter)
            {
                Log.e("from_filter","" );

                if (coreSelection)
                {
                    //core selection without season params
                    if(filter_level != 0)
                    {
                        url = ConstsCore.web_url + "/v1/display/stockageingNew/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&level=" + filter_level + selectedString + "&top=" + top + "&corefashion=" + corefashion + "&geoLevel2Code=" + geoLevel2Code + "&lobId="+ lobId; //+"&stockageband="+stockageband

                    }
                    else
                    {
                        url = ConstsCore.web_url + "/v1/display/stockageingNew/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + selectedString + "&top=" + top + "&corefashion=" + corefashion +"&geoLevel2Code=" + geoLevel2Code + "&lobId="+ lobId;
                    }
                }
                else
                {
                    // fashion select with season params
                    if(filter_level != 0)
                    {
                        url = ConstsCore.web_url + "/v1/display/stockageingNew/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&level=" + filter_level + selectedString + "&top=" + top + "&corefashion=" + corefashion + "&seasongroup=" + seasongroup + "&geoLevel2Code=" + geoLevel2Code + "&lobId="+ lobId;
                    }
                    else
                    {
                        url = ConstsCore.web_url + "/v1/display/stockageingNew/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + selectedString + "&top=" + top + "&corefashion=" + corefashion + "&seasongroup=" + seasongroup + "&geoLevel2Code=" + geoLevel2Code + "&lobId="+ lobId;
                    }
                }
            }
            else {
                if (coreSelection)
                {
                    if(stockageband1 == null || stockageband1.equals(""))
                    {
                        url = ConstsCore.web_url + "/v1/display/stockageingNew/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&top=" + top + "&corefashion=" + corefashion + "&geoLevel2Code=" + geoLevel2Code + "&lobId="+ lobId;

                    }
                    else
                    {
                        url = ConstsCore.web_url + "/v1/display/stockageingNew/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&top=" + top + "&corefashion=" + corefashion + "&geoLevel2Code=" + geoLevel2Code + "&lobId="+ lobId +"&stockageband="+stockageband1;

                    }

                    //core selection without season params
                }
                else {
                    if (stockageband1 == null || stockageband1.equals("")) {

                        // fashion select with season params
                        url = ConstsCore.web_url + "/v1/display/stockageingNew/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&top=" + top + "&corefashion=" + corefashion + "&seasongroup=" + seasongroup + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;
                    }
                    else
                    {
                        url = ConstsCore.web_url + "/v1/display/stockageingNew/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&top=" + top + "&corefashion=" + corefashion + "&seasongroup=" + seasongroup +"&stockageband=" + stockageband1 + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;

                    }
                }
            }
            Log.e("TAG", "requestStockAgeingApi: "+url );
            final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            StockAgListView.setVisibility(View.VISIBLE);
                            ageing.clear();
                            try {
                                if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                    Reusable_Functions.hDialog();
                                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                    StockAgListView.removeFooterView(footer);
                                    StockAgListView.setTag("FOOTER_REMOVE");
                                    if (StockAgeingList.size() == 0)
                                    {
                                        StockAgListView.setVisibility(View.GONE);
                                    }
                                    return;

                                } else if (response.length() == limit) {
                                    for (int i = 0; i < response.length(); i++) {

                                        StockAgeingListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                        StockAgeingList.add(StockAgeingListDisplay);

                                    }
                                    offsetvalue = offsetvalue + 10;
                                    top = top + 10;

                                } else if (response.length() < limit) {
                                    for (int i = 0; i < response.length(); i++) {
                                        StockAgeingListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                        StockAgeingList.add(StockAgeingListDisplay);
                                    }
                                    offsetvalue = offsetvalue + 10;
                                    top = top + 10;
                                }
                                if (lazyScroll.equals("ON")) {
                                    stockAgeingAdapter.notifyDataSetChanged();
                                    lazyScroll = "OFF";
                                    footer.setVisibility(View.GONE);
                                } else {
                                    stockAgeingAdapter = new StockAgeingAdapter(StockAgeingList, context);
                                    StockAgListView.setAdapter(stockAgeingAdapter);

                                }
                                Reusable_Functions.hDialog();
                            } catch (Exception e) {
                                StockAgeingList.clear();
                                stockAgeingAdapter.notifyDataSetChanged();
                                StockAgListView.setVisibility(View.GONE);
                                Reusable_Functions.hDialog();
                                StockAgListView.removeFooterView(footer);
                                StockAgListView.setTag("FOOTER_REMOVE");
                                Toast.makeText(context, "Data failed...", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "Server not found...", Toast.LENGTH_SHORT).show();
                            StockAgListView.removeFooterView(footer);
                            StockAgListView.setTag("FOOTER_REMOVE");
                            StockAgListView.setVisibility(View.GONE);
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

                    if (FirstVisibleItem + VisibleItemCount == TotalItemCount && scrollState == SCROLL_STATE_IDLE && lazyScroll.equals("OFF")) {
                        if (StockAgListView.getTag().equals("FOOTER_REMOVE")) {
                            StockAgListView.addFooterView(footer);
                            StockAgListView.setTag("FOOTER_ADDED");

                        }
                        footer.setVisibility(View.VISIBLE);
                        lazyScroll = "ON";
                        requestStockAgeingApi(selectedString);
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
            StockAgListView.removeFooterView(footer);
            StockAgListView.setTag("FOOTER_REMOVE");
        }
    }

    private void initalise() {
        stock_txtStoreCode = (TextView) findViewById(R.id.txtStoreCode);
        stock_txtStoreName = (TextView) findViewById(R.id.txtStoreName);
        listAgeing = (ListView) findViewById(R.id.listAgeing);


        if(isMultiStore.equals("Yes"))
        {
            stock_txtStoreCode.setText("Concept : ");
            stock_txtStoreName.setText(value);

        }
        else
        {
            stock_txtStoreCode.setText("Store : ");
            stock_txtStoreName.setText(value);
        }
        stock_BtnBack = (RelativeLayout) findViewById(R.id.stockAgeing_imageBtnBack);
        stock_BtnFilter = (RelativeLayout) findViewById(R.id.stockAgeing_imgfilter);
        stock_quickFilter = (RelativeLayout) findViewById(R.id.sa_quickFilter);
        quickFilterPopup = (RelativeLayout) findViewById(R.id.quickFilterPopup);
        quickFilterPopup.setVisibility(View.GONE);
        quickFilter_baseLayout = (RelativeLayout) findViewById(R.id.quickFilter_baseLayout);
        qfDoneLayout = (LinearLayout) findViewById(R.id.qfDoneLayout);
        quickFilter_BorderLayout = (RelativeLayout) findViewById(R.id.quickFilter_BorderLayout);
        StockAgListView = (ListView) findViewById(R.id.stockListView);
        stock_segmented = (SegmentedGroup) findViewById(R.id.stock_segmented);
        stock_core = (RadioButton) findViewById(R.id.stock_core);
        stock_fashion = (RadioButton) findViewById(R.id.stock_fashion);
        Toggle_stock_fav = (ToggleButton) findViewById(R.id.toggle_top_fav);
        checkCurrent = (RadioButton) findViewById(R.id.checkCurrent);
        checkPrevious = (RadioButton) findViewById(R.id.checkPrevious);
        checkOld = (RadioButton) findViewById(R.id.checkOld);
        checkUpcoming = (RadioButton) findViewById(R.id.checkUpcoming);
//        checkAgeing1 = (CheckBox) findViewById(R.id.checkAgeing1);
//        checkAgeing2 = (CheckBox) findViewById(R.id.checkAgeing2);
//        checkAgeing3 = (CheckBox) findViewById(R.id.checkAgeing3);
        Tabview = (TabLayout) findViewById(R.id.tabview);
        Tabview.addTab(Tabview.newTab().setText("Fashion"));
        Tabview.addTab(Tabview.newTab().setText("Core"));

        Tabview.setOnTabSelectedListener(this);
        checkCurrent.setOnClickListener(this);
        checkPrevious.setOnClickListener(this);
        checkOld.setOnClickListener(this);
        checkUpcoming.setOnClickListener(this);
//        checkAgeing3.setOnClickListener(this);
//        checkAgeing2.setOnClickListener(this);
//        checkAgeing1.setOnClickListener(this);
        qfDoneLayout.setOnClickListener(this);
        stock_segmented.setOnCheckedChangeListener(StockAgeingActivity.this);
        stock_BtnBack.setOnClickListener(StockAgeingActivity.this);
        stock_BtnFilter.setOnClickListener(this);
        stock_quickFilter.setOnClickListener(this);
        quickFilter_baseLayout.setOnClickListener(this);
        quickFilter_BorderLayout.setOnClickListener(this);

        stockageband_api();

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.stockAgeing_imageBtnBack:
                onBackPressed();
                break;
            case R.id.toggle_stock_fav:
                if (Toggle_stock_fav.isChecked()) {
                    Toggle_stock_fav.setChecked(true);
                } else {
                    Toggle_stock_fav.setChecked(false);
                }

                break;
            case R.id.stockAgeing_imgfilter:
                Intent intent1 = new Intent(StockAgeingActivity.this, SalesAnalysisFilter.class);
                intent1.putExtra("checkfrom", "stockAgeing");
                startActivity(intent1);
                break;
            case R.id.sa_quickFilter:
                quickFilterPopup.setVisibility(View.VISIBLE);
                break;
            case R.id.quickFilter_baseLayout:
                baseclick();
                quickFilterPopup.setVisibility(View.GONE);
                break;
            case R.id.qfDoneLayout:
                from_filter=false;

                if (Reusable_Functions.chkStatus(context))
                {

                    Log.e("checkedValue_StockAgeing"," "+checkedValue_StockAgeing);


                    String[] array = (String[]) checkedValue_StockAgeing.toArray(new String[0]);
                    stockageband1 = Arrays.toString(array);
                    stockageband1 = stockageband1.replace("[", "");
                    stockageband1 = stockageband1.replace("]", "");
                    stockageband1 = stockageband1.replace(", ", ",");
                    stockageband1 = stockageband1.replaceAll(" ", "%20");
                    stockageband1 = stockageband1.replaceAll("&", "%26");

//                    if (checkAgeing1.isChecked()) {
//                        checkAgeingVal = "CheckAgeing1";
//                        stockageband1="100-200";
//                        ageing.add(stockageband1);
//
//                    }
//                    if (checkAgeing2.isChecked()) {
//                        checkAgeingVal = "CheckAgeing2";
//                        stockageband2="150-250";
//                        ageing.add(stockageband2);
//
//
//                    }
//                    if (checkAgeing3.isChecked()) {
//                        checkAgeingVal = "CheckAgeing3";
//                        stockageband3="250-350";
//                        ageing.add(stockageband3);
//
//                    }
//
//                    String[] array = (String[]) ageing.toArray(new String[0]);
//                    subMC = Arrays.toString(array);
//                    subMC = subMC.replace("[", "");
//                    subMC = subMC.replace("]", "");
//                    subMC = subMC.replace(", ", ",");
//

//                    else{
//                        stockageband=" ";
//                        ageing.clear();
//
//                    }



                    if (checkCurrent.isChecked()) {
                        checkSeasonGpVal = "Current";
                        quickFilterPopup.setVisibility(View.GONE);
                        popupCurrent();
                        break;
                    } else if (checkPrevious.isChecked()) {
                        checkSeasonGpVal = "Previous";
                        quickFilterPopup.setVisibility(View.GONE);
                        popupPrevious();
                        break;

                    } else if (checkOld.isChecked()) {
                        checkSeasonGpVal = "Old";
                        quickFilterPopup.setVisibility(View.GONE);
                        popupOld();
                        break;

                    } else if (checkUpcoming.isChecked()) {
                        checkSeasonGpVal = "Upcoming";
                        quickFilterPopup.setVisibility(View.GONE);
                        popupUpcoming();
                        break;

                    }


                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();

                }

                break;
            case R.id.checkCurrent:
                Toast.makeText(context, "ageing"+ageing.toString(),Toast.LENGTH_SHORT).show();
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
          /*  case R.id.checkAgeing1:

//                checkAgeing1.setChecked(true);
//                checkAgeing2.setChecked(false);
//                checkAgeing3.setChecked(false);
                if(!checkAgeing1.isChecked())
                {
                    checkAgeing1.setChecked(false);
                }
                else
                {
                    checkAgeing1.setChecked(true);

                }
                break;
            case R.id.checkAgeing2:

//                checkAgeing2.setChecked(true);
//                checkAgeing1.setChecked(false);
//                checkAgeing3.setChecked(false);
                if(!checkAgeing2.isChecked())
                {
                    checkAgeing2.setChecked(false);
                }
                else
                {
                    checkAgeing2.setChecked(true);

                }
                break;
            case R.id.checkAgeing3:

//                checkAgeing3.setChecked(true);
//                checkAgeing1.setChecked(false);
//                checkAgeing2.setChecked(false);
                if(!checkAgeing3.isChecked())
                {
                    checkAgeing3.setChecked(false);
                }
                else
                {
                    checkAgeing3.setChecked(true);

                }
                break;  */
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
            requestStockAgeingApi(selectedString);
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
            requestStockAgeingApi(selectedString);
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
            requestStockAgeingApi(selectedString);
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
            requestStockAgeingApi(selectedString);
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (toggleClick == false) {

            switch (checkedId) {
                case R.id.stock_core:
                    if (stock_core.isChecked()) {
                        limit = 10;
                        offsetvalue = 0;
                        top = 10;
                        corefashion = "Core";
                        lazyScroll = "OFF";
                        if (Reusable_Functions.chkStatus(context)) {
                            StockAgeingList.clear();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            StockAgListView.setVisibility(View.GONE);
                            coreSelection = true;
                            requestStockAgeingApi(selectedString);
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            StockAgListView.setVisibility(View.GONE);

                        }
                    }
                    break;
                case R.id.stock_fashion:
                    if (stock_fashion.isChecked()) {
                        limit = 10;
                        offsetvalue = 0;
                        top = 10;
                        corefashion = "Fashion";
                        lazyScroll = "OFF";
                        if (Reusable_Functions.chkStatus(context)) {
                            StockAgeingList.clear();
                            StockAgListView.setVisibility(View.GONE);
                            Reusable_Functions.sDialog(context, "Loading data...");
                            coreSelection = false;
                            requestStockAgeingApi(selectedString);
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            StockAgListView.setVisibility(View.GONE);

                        }
                    }
                    break;

            }
        } else {
            toggleClick = false;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        corefashion = null;
        checkSeasonGpVal = null;
        checkAgeingVal = null;
        seasongroup = null;
        corefashion = "Fashion";
        seasongroup = "Current";
        finish();
    }


    //------ Tab functionality...

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        int checkedId= Tabview.getSelectedTabPosition();
        Log.e("TAG", "onTabSelected: " );

//        if (toggleClick == false) {  // toggleClick is use when you retain tab button that time it will call .
//            from_filter=false;
            switch (checkedId) {
                case 1 :   //core selection
                        limit = 10;
                        offsetvalue = 0;
                        top = 10;
                        corefashion = "Core";
                        lazyScroll = "OFF";
                        if (Reusable_Functions.chkStatus(context)) {
                            StockAgeingList.clear();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            StockAgListView.setVisibility(View.GONE);
                            coreSelection = true;
                            requestStockAgeingApi(selectedString);
                        }
                        else
                        {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            StockAgListView.setVisibility(View.GONE);
                        }

                    break;
                case 0 :  // fashion selection
                        limit = 10;
                        offsetvalue = 0;
                        top = 10;
                        corefashion = "Fashion";
                        lazyScroll = "OFF";
                        if (Reusable_Functions.chkStatus(context)) {
                            StockAgeingList.clear();
                            StockAgListView.setVisibility(View.GONE);
                            Reusable_Functions.sDialog(context, "Loading data...");
                            coreSelection = false;
                            requestStockAgeingApi(selectedString);
                        }
                        else
                        {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            StockAgListView.setVisibility(View.GONE);
                        }
                    break;

            }
//        }
//
//    else {
//            toggleClick = false;
//
//        }


    }

    private void stockageband_api(){

        String url = ConstsCore.web_url + "/v1/display/stockageband/" + userId + "?lobId=" + lobId ; //+"&stockageband="+stockageband

        Log.e("TAG", "stockageband: "+url );
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Reusable_Functions.hDialog();
                        Log.e("stockageband response "," "+response);
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();

                              //  return;

                            } else{
                                for (int i = 0; i < response.length(); i++) {

                                    String planDept = response.get(i).toString();
                                    ageing_val.add(planDept);
                                }
                                listAdapter = new AgeingAdapter(ageing_val, context);
                                listAgeing.setAdapter( listAdapter );
                            }
                            Log.e("ageing_val "," "+ageing_val);
                        } catch (Exception e) {

                            Toast.makeText(context, "Data failed...", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "Server not found...", Toast.LENGTH_SHORT).show();
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
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
