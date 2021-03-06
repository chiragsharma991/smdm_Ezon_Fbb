package apsupportapp.aperotechnologies.com.designapp.Ezone;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.RunningPromo.RecyclerViewPositionHelper;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesAnalysisFilter;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesAnalysisSnapAdapter;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesPagerAdapter;
import apsupportapp.aperotechnologies.com.designapp.model.OptionEfficiencyDetails;
import apsupportapp.aperotechnologies.com.designapp.model.RecyclerItemClickListener;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisListDisplay;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisViewPagerValue;
import info.hoang8f.android.segmented.SegmentedGroup;


public class EzoneSalesAnalysisActivity1 extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, TabLayout.OnTabSelectedListener {
    JsonArrayRequest postRequest, ez_postRequest;
    ArrayList<SalesAnalysisViewPagerValue> analysisArrayList, salesList, ez_sales_header_array;
    ArrayList<SalesAnalysisListDisplay> salesAnalysisClassArrayList, ez_sales_detalis_array;
    SalesAnalysisListDisplay salesAnalysisClass, ez_sales_detail_model;
    SalesAnalysisSnapAdapter salesadapter;
    EzoneSalesAdapter ez_sales_adapter;
    SalesAnalysisViewPagerValue salesAnalysis, ez_sales_header_model;
    SalesPagerAdapter pageradapter;
    EzoneSalesPagerAdapter ez_sales_pager_adapter;
    Gson gson;
    public static Activity Ezone_SalesAnalysisActivity;
    Context context;
    String planDept, planCategory, planClass;
    ViewPager vwpagersales;
    LinearLayout lldots, llhierarchy, llayoutSalesAnalysis;
    RecyclerView listView_SalesAnalysis;
    SharedPreferences sharedPreferences;
    String userId, bearertoken, geoLeveLDesc, storeDescription, geoLevel2Code, lobId, selectedString;
    String saleFirstVisibleItem, fromWhere = "Department", val, txtSalesClickedValue, all_from_val;
    TextView txtheaderplanclass, txthDeptName;
    public static int level = 1;
    RequestQueue queue;
    int selFirstPositionValue = 0, currentVmPos, totalItemCount, firstVisibleItem, offsetvalue = 0, limit = 100, count = 0, currentState = RecyclerView.SCROLL_STATE_IDLE, prevState = RecyclerView.SCROLL_STATE_IDLE;
    boolean onClickFlag = false, filter_toggleClick = false;
    ProgressBar progressBar1;
    TabLayout Tabview;
    private String planDeptNm, planCategoryNm, planBrandNm, planClassNm;
    // Ezone Elements Declaration
    public static String ez_segment_val = "LD";
    SegmentedGroup ez_segmentgrp;
    RelativeLayout rel_ez_back, rel_ez_sort, rel_ez_filter, rel_ez_next, rel_ez_prev, rel_ez_viewBy, relStoreLayout, ezonesales_btnReset;
    RadioButton btn_ez_LD, btn_ez_WTD, btn_ez_MTD, btn_ez_YTD, rb_ez_viewBy_ProductChk, rb_ez_viewBy_LocatnChk;
    TextView txt_ez_header, ez_txt_hierarchy_nm;
    RecyclerView recyclevw_ez_sales;
    String ez_sale_first_item, ez_fromWhere = "Department", val_hierarchy, ez_sclickedVal;
    boolean ezone_onClickflg = false, ez_filter_toggleClick = false;
    LinearLayout ez_linear_dots, ez_linear_hierarchy, lin_ez_Product, lin_ez_Location;
    ViewPager ez_viewpager;
    public static int ezone_level = 1;
    int ez_firstVisible_no, ez_currentVmPos, ez_sFirstPosVal = 0, ez_totalItemCount;
    ProgressBar ez_progessBar;
    private String filterSelectedString = "", isMultiStore, value;
    private int filter_level;
    TabLayout ez_tabView;
    private String header_value;
    private String TAG="EzoneSalesAnalysisActivity1";
    private String[] hierarchyList;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ezone_sales);
        getSupportActionBar().hide();
        context = this;
        Ezone_SalesAnalysisActivity = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLeveLDesc = sharedPreferences.getString("geoLeveLDesc", "");
        storeDescription = sharedPreferences.getString("storeDescription", "");
        geoLevel2Code = sharedPreferences.getString("concept", "");
        lobId = sharedPreferences.getString("lobid", "");
        isMultiStore = sharedPreferences.getString("isMultiStore", "");
        value = sharedPreferences.getString("value", "");
        final String hierarchyLevels = sharedPreferences.getString("hierarchyLevels", "");
        // replace all labels using hierarchyList
        hierarchyList = hierarchyLevels.split(",");
        for (int i = 0; i <hierarchyList.length ; i++) {
            hierarchyList[i]=hierarchyList[i].trim();
        }
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getString("selectedStringVal") != null) {
                header_value = getIntent().getExtras().getString("selectedStringVal");
            } else {
                header_value = "";
            }
        } else {
            header_value = "";
        }
        //when geoLevelDesc is Ezone
        initialize_ez_ui();
        filterSelectedString = getIntent().getStringExtra("selectedStringVal");
        filter_level = getIntent().getIntExtra("selectedlevelVal", 0);


//        Log.e("filterSelectedString :", " " + filterSelectedString);
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.progressDialog = new ProgressDialog(context);
            Reusable_Functions.progressDialog.setCancelable(false);
            if (!Reusable_Functions.progressDialog.isShowing()) {
                Reusable_Functions.progressDialog.show();
            }
            Reusable_Functions.progressDialog.setMessage("Loading...");
            ez_progessBar.setVisibility(View.GONE);
            ez_linear_hierarchy.setVisibility(View.GONE);
            rel_ez_prev.setVisibility(View.INVISIBLE);
            rb_ez_viewBy_ProductChk.setChecked(true);
            offsetvalue = 0;
            limit = 100;
            count = 0;
            if (getIntent().getStringExtra("selectedStringVal") == null) {
                ez_filter_toggleClick = false;
                retainEzoneSegVal();
                ezone_level = 1;
                requestEzoneSalesDetailAPI();
            } else if (getIntent().getStringExtra("selectedStringVal") != null) {
                header_value = getIntent().getStringExtra("selectedStringVal");
                filter_level = getIntent().getIntExtra("selectedlevelVal", 0);
                ez_filter_toggleClick = true;
                retainEzoneSegVal();
                requestEzoneFilterSelectedVal(header_value, filter_level);
            }
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
            ez_progessBar.setVisibility(View.GONE);
        }
        // Scroll listener on Recycle View
        recyclevw_ez_sales.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                currentState = newState;
                if (prevState != RecyclerView.SCROLL_STATE_IDLE  && !ezone_onClickflg) {
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        public void run() {
                            if (!ezone_onClickflg) {
                                TimeUP();
                            }
                        }
                    }, 700);
                }
                prevState = currentState;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerViewPositionHelper mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
                ez_totalItemCount = mRecyclerViewHelper.getItemCount();
                ez_firstVisible_no = mRecyclerViewHelper.findFirstVisibleItemPosition();
            }
        });

        // Ezone Drill Down level hierarchy(Department to Brand Class level)
        recyclevw_ez_sales.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position)
                    {
                        ez_sFirstPosVal = 0;
                        ez_firstVisible_no = 0;
                        if (ez_progessBar.getVisibility() == View.VISIBLE)
                        {
                           return;
                        }
                        else
                        {
                            ezone_onClickflg = true;

                                    if (position < ez_sales_detalis_array.size()) {
                                        if(txt_ez_header.getText().toString().equals(hierarchyList[0])){
                                            rel_ez_prev.setVisibility(View.VISIBLE);
                                            txt_ez_header.setText(hierarchyList[1]);
                                            ez_sclickedVal = ez_sales_detalis_array.get(position).getLevel();
                                            if (!ez_sclickedVal.equals("All")) {
                                                ez_sclickedVal = ez_sclickedVal.replace("%", "%25");
                                                ez_sclickedVal = ez_sclickedVal.replace(" ", "%20").replace("&", "%26");
                                                if(!header_value.contains("&department=" + ez_sclickedVal))
                                                {
                                                    header_value += "&department=" + ez_sclickedVal;
                                                }
                                            } else {
//                                                    header_value = "";
                                            }
                                            ez_fromWhere = hierarchyList[1];
                                            if (ez_linear_dots != null) {
                                                ez_linear_dots.removeAllViews();
                                            }
                                            ez_currentVmPos = ez_viewpager.getCurrentItem();
                                            ezone_level = 2;
                                            if (Reusable_Functions.chkStatus(context)) {
                                                if (ez_postRequest != null) {
                                                    ez_postRequest.cancel();
                                                }
                                                Reusable_Functions.progressDialog = new ProgressDialog(context);
                                                if (!Reusable_Functions.progressDialog.isShowing()) {
                                                    Reusable_Functions.progressDialog.show();
                                                }
                                                Reusable_Functions.progressDialog.setMessage("Loading data...");
                                                ez_progessBar.setVisibility(View.GONE);
                                                offsetvalue = 0;
                                                limit = 100;
                                                count = 0;
                                                ez_sales_detalis_array = new ArrayList<SalesAnalysisListDisplay>();
                                                requestEzoneSalesCategoryList(ez_sclickedVal);
                                                planDept = ez_sclickedVal;

                                            } else {
                                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else if(txt_ez_header.getText().toString().equals(hierarchyList[1])){
                                            txt_ez_header.setText(hierarchyList[2]);
                                            ez_sclickedVal = ez_sales_detalis_array.get(position).getLevel();
                                            if (!ez_sclickedVal.equals("All")) {
                                                ez_sclickedVal = ez_sclickedVal.replace("%", "%25");
                                                ez_sclickedVal = ez_sclickedVal.replace(" ", "%20").replace("&", "%26");
                                                if(!header_value.contains("&category=" + ez_sclickedVal))
                                                {
                                                    header_value += "&category=" + ez_sclickedVal;
                                                }
                                            } else {
//                                                    header_value = "";
                                            }
                                            ez_fromWhere = hierarchyList[2];
                                            if (ez_linear_dots != null) {
                                                ez_linear_dots.removeAllViews();
                                            }
                                            ez_currentVmPos = ez_viewpager.getCurrentItem();
                                            ezone_level = 3;
                                            if (Reusable_Functions.chkStatus(context)) {
                                                if (ez_postRequest != null) {
                                                    ez_postRequest.cancel();
                                                }
                                                Reusable_Functions.progressDialog = new ProgressDialog(context);
                                                if (!Reusable_Functions.progressDialog.isShowing()) {
                                                    Reusable_Functions.progressDialog.show();
                                                }
                                                Reusable_Functions.progressDialog.setMessage("Loading data...");
                                                ez_progessBar.setVisibility(View.GONE);
                                                offsetvalue = 0;
                                                limit = 100;
                                                count = 0;
                                                ez_sales_detalis_array = new ArrayList<SalesAnalysisListDisplay>();
                                                requestEzoneSalesPlanClassList(ez_sclickedVal);
                                                planCategory = ez_sclickedVal;
                                            } else {
                                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else if(txt_ez_header.getText().toString().equals(hierarchyList[2])){

                                            rel_ez_next.setVisibility(View.INVISIBLE);
                                            txt_ez_header.setText(hierarchyList[3]);
                                            ez_sclickedVal = ez_sales_detalis_array.get(position).getLevel();
                                            if (!ez_sclickedVal.equals("All")) {
                                                ez_sclickedVal = ez_sclickedVal.replace("%", "%25");
                                                ez_sclickedVal = ez_sclickedVal.replace(" ", "%20").replace("&", "%26");
                                                if(!header_value.contains("&class=" + ez_sclickedVal))
                                                {
                                                    header_value += "&class=" + ez_sclickedVal;
                                                }
                                            } else {
//                                                    header_value = "";
                                            }
                                            ez_fromWhere = hierarchyList[3];
                                            if (ez_linear_dots != null) {
                                                ez_linear_dots.removeAllViews();
                                            }
                                            ez_currentVmPos = ez_viewpager.getCurrentItem();
                                            ezone_level = 4;
                                            if (Reusable_Functions.chkStatus(context)) {
                                                if (ez_postRequest != null) {
                                                    ez_postRequest.cancel();
                                                }
                                                Reusable_Functions.progressDialog = new ProgressDialog(context);
                                                if (!Reusable_Functions.progressDialog.isShowing()) {
                                                    Reusable_Functions.progressDialog.show();
                                                }
                                                Reusable_Functions.progressDialog.setMessage("Loading data...");
                                                ez_progessBar.setVisibility(View.GONE);
                                                offsetvalue = 0;
                                                limit = 100;
                                                count = 0;
                                                ez_sales_detalis_array = new ArrayList<SalesAnalysisListDisplay>();
                                                requestEzoneSalesBrandList(ez_sclickedVal);
                                                planClass = ez_sclickedVal;
                                            } else {
                                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else if(txt_ez_header.getText().toString().equals("Region")){
                                            rel_ez_prev.setVisibility(View.VISIBLE);
                                            rel_ez_next.setVisibility(View.INVISIBLE);
                                            txt_ez_header.setText("Store");
                                            ez_sclickedVal = ez_sales_detalis_array.get(position).getLevel();
                                            if (!ez_sclickedVal.equals("All")) {
                                                ez_sclickedVal = ez_sclickedVal.replace("%", "%25");
                                                ez_sclickedVal = ez_sclickedVal.replace(" ", "%20").replace("&", "%26");
                                                if(!header_value.contains("&regionDescription=" + ez_sclickedVal))
                                                {
                                                    header_value += "&regionDescription=" + ez_sclickedVal;
                                                }
                                            } else {
//                                                    header_value = "";
                                            }
                                            ez_fromWhere = "Store";
                                            if (ez_linear_dots != null) {
                                                ez_linear_dots.removeAllViews();
                                            }
                                            ez_currentVmPos = ez_viewpager.getCurrentItem();
                                            ezone_level = 9;
                                            if (Reusable_Functions.chkStatus(context)) {
                                                if (ez_postRequest != null) {
                                                    ez_postRequest.cancel();
                                                }
                                                Reusable_Functions.progressDialog = new ProgressDialog(context);
                                                if (!Reusable_Functions.progressDialog.isShowing()) {
                                                    Reusable_Functions.progressDialog.show();
                                                }
                                                Reusable_Functions.progressDialog.setMessage("Loading data...");
                                                ez_progessBar.setVisibility(View.GONE);
                                                offsetvalue = 0;
                                                limit = 100;
                                                count = 0;
                                                ez_sales_detalis_array = new ArrayList<SalesAnalysisListDisplay>();
                                                requestEzoneSalesStoreList(ez_sclickedVal);
                                            } else {
                                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else{

                                            Reusable_Functions.hDialog();
                                            Toast.makeText(context, " You are at the last level of hierarchy", Toast.LENGTH_SHORT).show();
                                            ezone_onClickflg = false;
                                        }
                                    }
                                }

                    }
                }));

    }


    private void initialize_ez_ui() {
        ez_fromWhere = hierarchyList[0];
        ez_firstVisible_no = 0;
        ez_sales_detalis_array = new ArrayList<SalesAnalysisListDisplay>();
        ez_sales_header_array = new ArrayList<SalesAnalysisViewPagerValue>();
        ez_progessBar = (ProgressBar) findViewById(R.id.ez_progressBar);
        ez_linear_dots = (LinearLayout) findViewById(R.id.ez_linear_dots);
        ez_linear_dots.setOrientation(LinearLayout.HORIZONTAL);
        ez_linear_hierarchy = (LinearLayout) findViewById(R.id.ez_linear_hierarchy);
        ez_viewpager = (ViewPager) findViewById(R.id.ez_viewpager);
        recyclevw_ez_sales = (RecyclerView) findViewById(R.id.recyclevw_ez_sales);
        txt_ez_header = (TextView) findViewById(R.id.txt_ez_header);
        txt_ez_header.setText(hierarchyList[0]);
        ez_txt_hierarchy_nm = (TextView) findViewById(R.id.ez_txt_hierarchy_nm);
        ez_segmentgrp = (SegmentedGroup) findViewById(R.id.ez_segmentedGrp);
        ez_segmentgrp.setOnCheckedChangeListener(this);
        rel_ez_viewBy = (RelativeLayout) findViewById(R.id.rel_ez_viewBy);
        rel_ez_viewBy.setVisibility(View.GONE);
        rel_ez_prev = (RelativeLayout) findViewById(R.id.rel_ez_prev);
        rel_ez_next = (RelativeLayout) findViewById(R.id.rel_ez_next);
        rel_ez_back = (RelativeLayout) findViewById(R.id.rel_ez_back);
        rel_ez_filter = (RelativeLayout) findViewById(R.id.rel_ez_filter);
        rel_ez_sort = (RelativeLayout) findViewById(R.id.rel_ez_sort);
        ezonesales_btnReset = (RelativeLayout) findViewById(R.id.ezonesales_btnReset);
        lin_ez_Product = (LinearLayout) findViewById(R.id.lin_ez_Product);
        lin_ez_Location = (LinearLayout) findViewById(R.id.lin_ez_location);
        ez_tabView = (TabLayout) findViewById(R.id.tabview_ezone_sales);
        ez_tabView.addTab(ez_tabView.newTab().setText("LD"), 0);
        ez_tabView.addTab(ez_tabView.newTab().setText("WTD"), 1);
        ez_tabView.addTab(ez_tabView.newTab().setText("MTD"), 2);
        ez_tabView.addTab(ez_tabView.newTab().setText("YTD"), 3);
        ez_tabView.setOnTabSelectedListener(this);
        rb_ez_viewBy_LocatnChk = (RadioButton) findViewById(R.id.rb_ez_viewBy_LocatnChk);
        rb_ez_viewBy_ProductChk = (RadioButton) findViewById(R.id.rb_ez_viewBy_ProductChk);
        TabLayout tab = (TabLayout) findViewById(R.id.ez_dotTab);
        tab.setupWithViewPager(ez_viewpager, true);
        ez_sales_pager_adapter = new EzoneSalesPagerAdapter();
        ez_sales_pager_adapter.notifyDataSetChanged();
        ez_sales_adapter = new EzoneSalesAdapter();
        rel_ez_sort.setOnClickListener(this);
        rel_ez_filter.setOnClickListener(this);
        rel_ez_back.setOnClickListener(this);
        rel_ez_next.setOnClickListener(this);
        rel_ez_prev.setOnClickListener(this);
        rel_ez_viewBy.setOnClickListener(this);
        lin_ez_Location.setOnClickListener(this);
        lin_ez_Product.setOnClickListener(this);

        ezonesales_btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ez_sales_detalis_array = new ArrayList<SalesAnalysisListDisplay>();
                salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                ez_sales_header_array = new ArrayList<SalesAnalysisViewPagerValue>();
                analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                header_value = "";
                ez_fromWhere = hierarchyList[0];
                ez_firstVisible_no = 0;
                ez_sFirstPosVal = 0;
                if (Reusable_Functions.chkStatus(context))
                {
                    Reusable_Functions.sDialog(context, "Loading...");
                    ez_progessBar.setVisibility(View.GONE);
                    ez_linear_hierarchy.setVisibility(View.GONE);
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    ezone_level = 1;
                    header_value = "";
                    ez_txt_hierarchy_nm.setText(" ");
                    rel_ez_prev.setVisibility(View.INVISIBLE);
                    rel_ez_next.setVisibility(View.VISIBLE);
                    txt_ez_header.setText(hierarchyList[0]);
                    ez_segment_val = "LD";
                    ez_tabView.getTabAt(0).select();
                    ez_filter_toggleClick = false;
                    requestEzoneSalesDetailAPI();
                }
                else
                {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    ez_progessBar.setVisibility(View.GONE);
                    Reusable_Functions.hDialog();
                }


            }
        });

    }


    private void retainEzoneSegVal() {
        ez_filter_toggleClick = true;
        switch (ez_segment_val) {
            case "LD":
                ez_tabView.getTabAt(0).select();
                break;
            case "WTD":
                ez_tabView.getTabAt(1).select();
                break;
            case "MTD":
                ez_tabView.getTabAt(2).select();
                break;
            case "YTD":
                ez_tabView.getTabAt(3).select();
                break;

        }
    }

    private void TimeUP() {
        if (ez_sales_detalis_array.size() != 0) {
            if (ez_firstVisible_no < ez_sales_detalis_array.size() && !ezone_onClickflg) {

                // product is checked in viewby
                if (txt_ez_header.getText().toString().equals(hierarchyList[0])) {
                    ezone_level = 1;
                    ez_sale_first_item = ez_sales_detalis_array.get(ez_firstVisible_no).getLevel();

                } else if (txt_ez_header.getText().toString().equals(hierarchyList[1])) {
                    ezone_level = 2;
                    ez_sale_first_item = ez_sales_detalis_array.get(ez_firstVisible_no).getLevel();
                } else if (txt_ez_header.getText().toString().equals(hierarchyList[2])) {
                    ezone_level = 3;
                    ez_sale_first_item = ez_sales_detalis_array.get(ez_firstVisible_no).getLevel();
                } else if (txt_ez_header.getText().toString().equals(hierarchyList[3])) {
                    ezone_level = 4;
                    ez_sale_first_item = ez_sales_detalis_array.get(ez_firstVisible_no).getLevel();
                }
                //location is checked in view by
                else if (txt_ez_header.getText().toString().equals("Region")) {
                    ezone_level = 7;
                    ez_sale_first_item = ez_sales_detalis_array.get(ez_firstVisible_no).getLevel();
                } else if (txt_ez_header.getText().toString().equals("Store")) {
                    ezone_level = 9;
                    ez_sale_first_item = ez_sales_detalis_array.get(ez_firstVisible_no).getLevel();
                }
                if (Reusable_Functions.chkStatus(context)) {
                    ez_currentVmPos = ez_viewpager.getCurrentItem();
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    ez_sales_header_array = new ArrayList<SalesAnalysisViewPagerValue>();

                    if (ez_firstVisible_no != ez_sFirstPosVal) {
                        if (ez_postRequest != null) {
                            ez_postRequest.cancel();
                        }
                        ez_progessBar.setVisibility(View.VISIBLE);
                        if (ez_sale_first_item.equals("All"))
                        {
                            requestEzoneSalesHeaderAPI();
                        }
                        else
                        {
                            requestEzoneSalesPagerOnScrollAPI();
                        }
                        ez_sFirstPosVal = ez_firstVisible_no;
                    }
                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                ez_firstVisible_no = ez_sales_detalis_array.size() - 1;

                LinearLayoutManager llm = (LinearLayoutManager) recyclevw_ez_sales.getLayoutManager();
                llm.scrollToPosition(ez_firstVisible_no);
                // product is checked in view by
                if (txt_ez_header.getText().toString().equals(hierarchyList[0])) {
                    ezone_level = 1;
                    ez_sale_first_item = ez_sales_detalis_array.get(ez_firstVisible_no).getLevel();

                } else if (txt_ez_header.getText().toString().equals(hierarchyList[1])) {
                    ezone_level = 2;
                    ez_sale_first_item = ez_sales_detalis_array.get(ez_firstVisible_no).getLevel();
                } else if (txt_ez_header.getText().toString().equals(hierarchyList[2])) {
                    ezone_level = 3;
                    ez_sale_first_item = ez_sales_detalis_array.get(ez_firstVisible_no).getLevel();
                } else if (txt_ez_header.getText().toString().equals(hierarchyList[3])) {
                    ezone_level = 4;
                    ez_sale_first_item = ez_sales_detalis_array.get(ez_firstVisible_no).getLevel();
                }
                // when location is checked in view by
                else if (txt_ez_header.getText().toString().equals("Region")) {
                    ezone_level = 7;
                    ez_sale_first_item = ez_sales_detalis_array.get(ez_firstVisible_no).getLevel();
                } else if (txt_ez_header.getText().toString().equals("Store")) {
                    ezone_level = 9;
                    ez_sale_first_item = ez_sales_detalis_array.get(ez_firstVisible_no).getLevel();
                }

                if (Reusable_Functions.chkStatus(context)) {
                    //  Reusable_Functions.hDialog();
                    ez_currentVmPos = ez_viewpager.getCurrentItem();
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    ez_sales_header_array = new ArrayList<SalesAnalysisViewPagerValue>();

                    if (ez_firstVisible_no != ez_sFirstPosVal) {
                        if (ez_postRequest != null) {
                            ez_postRequest.cancel();
                        }
                        ez_progessBar.setVisibility(View.VISIBLE);
                        if (ez_sale_first_item.equals("All"))
                        {
                            requestEzoneSalesHeaderAPI();
                        } else {
                            requestEzoneSalesPagerOnScrollAPI();
                        }
                        ez_sFirstPosVal = ez_firstVisible_no;
                    }
                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
            }
        }// end of if loop

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.rel_ez_back:
                onBackPressed();
                break;
            case R.id.imageBtnBack:
                onBackPressed();
                break;
            case R.id.rel_ez_prev:
                if (ez_postRequest != null) {
                    ez_postRequest.cancel();
                }
                if (ez_progessBar.getVisibility() == View.VISIBLE) {
                    return;
                } else {
                    ez_sFirstPosVal = 0;
                    ez_firstVisible_no = 0;
                    if(txt_ez_header.getText().toString().equals(hierarchyList[3])){

                        rel_ez_next.setVisibility(View.VISIBLE);
                        if (ez_linear_dots != null) {
                            ez_linear_dots.removeAllViews();
                        }
                        ez_currentVmPos = ez_viewpager.getCurrentItem();
                        ez_linear_hierarchy.setVisibility(View.GONE);
                        txt_ez_header.setText(hierarchyList[2]);
                        ez_fromWhere = hierarchyList[2];
                        ezone_level = 3;
                        ez_sales_detalis_array = new ArrayList<SalesAnalysisListDisplay>();
                        recyclevw_ez_sales.removeAllViews();
                        val_hierarchy = " ";
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.progressDialog = new ProgressDialog(context);
                            Reusable_Functions.progressDialog.setCancelable(false);
                            if (!Reusable_Functions.progressDialog.isShowing()) {
                                Reusable_Functions.progressDialog.show();
                            }
                            Reusable_Functions.progressDialog.setMessage("Loading data...");
                            ez_progessBar.setVisibility(View.GONE);
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            requestEzoneSalesDetailAPI();
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }


                    }
                   else if(txt_ez_header.getText().toString().equals(hierarchyList[2])){

                        if (ez_linear_dots != null) {
                            ez_linear_dots.removeAllViews();
                        }
                        ez_currentVmPos = ez_viewpager.getCurrentItem();
                        ez_linear_hierarchy.setVisibility(View.GONE);
                        txt_ez_header.setText(hierarchyList[1]);
                        ez_fromWhere = hierarchyList[1];
                        ezone_level = 2;
                        val_hierarchy = " ";
                        ez_sales_detalis_array = new ArrayList<SalesAnalysisListDisplay>();
                        recyclevw_ez_sales.removeAllViews();
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.progressDialog = new ProgressDialog(context);
                            Reusable_Functions.progressDialog.setCancelable(false);
                            if (!Reusable_Functions.progressDialog.isShowing()) {
                                Reusable_Functions.progressDialog.show();
                            }
                            Reusable_Functions.progressDialog.setMessage("Loading data...");
                            ez_progessBar.setVisibility(View.GONE);
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            requestEzoneSalesDetailAPI();
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }

                    }

                   else if(txt_ez_header.getText().toString().equals(hierarchyList[1])){

                        rel_ez_prev.setVisibility(View.INVISIBLE);
                        if (ez_linear_dots != null) {
                            ez_linear_dots.removeAllViews();
                        }
                        ez_currentVmPos = ez_viewpager.getCurrentItem();
                        ez_linear_hierarchy.setVisibility(View.GONE);
                        txt_ez_header.setText(hierarchyList[0]);
                        ez_fromWhere = hierarchyList[0];
                        ezone_level = 1;
                        val_hierarchy = " ";
                        ez_sales_detalis_array = new ArrayList<SalesAnalysisListDisplay>();
                        recyclevw_ez_sales.removeAllViews();
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.progressDialog = new ProgressDialog(context);
                            Reusable_Functions.progressDialog.setCancelable(false);
                            if (!Reusable_Functions.progressDialog.isShowing()) {
                                Reusable_Functions.progressDialog.show();
                            }
                            Reusable_Functions.progressDialog.setMessage("Loading data...");
                            ez_progessBar.setVisibility(View.GONE);
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            requestEzoneSalesDetailAPI();
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if(txt_ez_header.getText().toString().equals("Store")){

                        rel_ez_prev.setVisibility(View.INVISIBLE);
                        rel_ez_next.setVisibility(View.VISIBLE);
                        if (ez_linear_dots != null) {
                            ez_linear_dots.removeAllViews();
                        }
                        ez_currentVmPos = ez_viewpager.getCurrentItem();
                        ez_linear_hierarchy.setVisibility(View.GONE);
                        txt_ez_header.setText("Region");
                        ez_fromWhere = "Region";
                        ezone_level = 7;
                        val_hierarchy = " ";
                        ez_sales_detalis_array = new ArrayList<SalesAnalysisListDisplay>();
                        recyclevw_ez_sales.removeAllViews();
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.progressDialog = new ProgressDialog(context);
                            Reusable_Functions.progressDialog.setCancelable(false);
                            if (!Reusable_Functions.progressDialog.isShowing()) {
                                Reusable_Functions.progressDialog.show();
                            }
                            Reusable_Functions.progressDialog.setMessage("Loading data...");
                            ez_progessBar.setVisibility(View.GONE);
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            requestEzoneSalesDetailAPI();
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                break;

            case R.id.rel_ez_next:
                if (ez_postRequest != null) {
                    ez_postRequest.cancel();
                }
                if (ez_progessBar.getVisibility() == View.VISIBLE) {
                    return;
                } else {
                    ez_sFirstPosVal = 0;
                    ez_firstVisible_no = 0;

                    if(txt_ez_header.getText().toString().equals("Region")){

                        rel_ez_prev.setVisibility(View.VISIBLE);
                        rel_ez_next.setVisibility(View.INVISIBLE);
                        txt_ez_header.setText("Store");
                        if (ez_linear_dots != null) {
                            ez_linear_dots.removeAllViews();
                        }
                        ez_currentVmPos = ez_viewpager.getCurrentItem();
                        ez_linear_hierarchy.setVisibility(View.GONE);
                        ez_fromWhere = "Store";
                        ezone_level = 9;
                        val_hierarchy = " ";
                        ez_sales_detalis_array = new ArrayList<SalesAnalysisListDisplay>();
                        recyclevw_ez_sales.removeAllViews();
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.progressDialog = new ProgressDialog(context);
                            Reusable_Functions.progressDialog.setCancelable(false);
                            if (!Reusable_Functions.progressDialog.isShowing()) {
                                Reusable_Functions.progressDialog.show();
                            }
                            Reusable_Functions.progressDialog.setMessage("Loading data...");
                            ez_progessBar.setVisibility(View.GONE);
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            requestEzoneSalesDetailAPI();
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else if(txt_ez_header.getText().toString().equals(hierarchyList[0])){

                        rel_ez_prev.setVisibility(View.VISIBLE);
                        txt_ez_header.setText(hierarchyList[1]);
                        if (ez_linear_dots != null) {
                            ez_linear_dots.removeAllViews();
                        }
                        ez_currentVmPos = ez_viewpager.getCurrentItem();
                        ez_linear_hierarchy.setVisibility(View.GONE);
                        ez_fromWhere = hierarchyList[1];
                        ezone_level = 2;
                        val_hierarchy = " ";
                        ez_sales_detalis_array = new ArrayList<SalesAnalysisListDisplay>();
                        recyclevw_ez_sales.removeAllViews();
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.progressDialog = new ProgressDialog(context);
                            Reusable_Functions.progressDialog.setCancelable(false);
                            if (!Reusable_Functions.progressDialog.isShowing()) {
                                Reusable_Functions.progressDialog.show();
                            }
                            Reusable_Functions.progressDialog.setMessage("Loading data...");
                            ez_progessBar.setVisibility(View.GONE);
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            requestEzoneSalesDetailAPI();
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else if(txt_ez_header.getText().toString().equals(hierarchyList[1])){

                        ez_fromWhere = hierarchyList[2];
                        txt_ez_header.setText(hierarchyList[2]);
                        ezone_level = 3;
                        val_hierarchy = " ";
                        if (ez_linear_dots != null) {
                            ez_linear_dots.removeAllViews();
                        }
                        ez_currentVmPos = ez_viewpager.getCurrentItem();
                        ez_linear_hierarchy.setVisibility(View.GONE);
                        ez_sales_detalis_array = new ArrayList<SalesAnalysisListDisplay>();
                        recyclevw_ez_sales.removeAllViews();
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.progressDialog = new ProgressDialog(context);
                            Reusable_Functions.progressDialog.setCancelable(false);
                            if (!Reusable_Functions.progressDialog.isShowing()) {
                                Reusable_Functions.progressDialog.show();
                            }
                            Reusable_Functions.progressDialog.setMessage("Loading data...");
                            ez_progessBar.setVisibility(View.GONE);
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            requestEzoneSalesDetailAPI();
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if(txt_ez_header.getText().toString().equals(hierarchyList[2])){

                        rel_ez_next.setVisibility(View.INVISIBLE);
                        txt_ez_header.setText(hierarchyList[3]);
                        ez_fromWhere = hierarchyList[3];
                        ezone_level = 4;
                        val_hierarchy = " ";
                        if (ez_linear_dots != null) {
                            ez_linear_dots.removeAllViews();
                        }
                        ez_currentVmPos = ez_viewpager.getCurrentItem();
                        ez_linear_hierarchy.setVisibility(View.GONE);
                        ez_sales_detalis_array = new ArrayList<SalesAnalysisListDisplay>();
                        recyclevw_ez_sales.removeAllViews();
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.progressDialog = new ProgressDialog(context);
                            Reusable_Functions.progressDialog.setCancelable(false);
                            if (!Reusable_Functions.progressDialog.isShowing()) {
                                Reusable_Functions.progressDialog.show();
                            }
                            Reusable_Functions.progressDialog.setMessage("Loading data...");
                            ez_progessBar.setVisibility(View.GONE);
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            requestEzoneSalesDetailAPI();
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                break;
            case R.id.rel_ez_sort:
                rel_ez_viewBy.setVisibility(View.VISIBLE);
                break;
            case R.id.rel_ez_viewBy:
                rel_ez_viewBy.setVisibility(View.GONE);
                break;
            case R.id.lin_ez_location:
                viewBy_Location();
                break;
            case R.id.lin_ez_Product:
                viewBy_Product();
                break;
            case R.id.rel_ez_filter:
                Intent filter_intent = new Intent(EzoneSalesAnalysisActivity1.this, EzoneSalesFilter.class);
                filter_intent.putExtra("checkfrom", "ezoneSales");
                startActivity(filter_intent);
                break;

        }
    }

    private void viewBy_Product() {
        if (Reusable_Functions.chkStatus(context)) {
            if (rb_ez_viewBy_ProductChk.isChecked()) {
                rb_ez_viewBy_LocatnChk.setChecked(false);
                rb_ez_viewBy_ProductChk.setChecked(true);
                rel_ez_viewBy.setVisibility(View.GONE);
            } else if (!rb_ez_viewBy_ProductChk.isChecked()) {
                rb_ez_viewBy_LocatnChk.setChecked(false);
                rb_ez_viewBy_ProductChk.setChecked(true);
                rel_ez_next.setVisibility(View.VISIBLE);
                rel_ez_prev.setVisibility(View.INVISIBLE);
                ezone_level = 1;
                ez_segment_val = "LD";
                ez_fromWhere = hierarchyList[0];
                txt_ez_header.setText(hierarchyList[0]);
                ez_sales_detalis_array = new ArrayList<SalesAnalysisListDisplay>();
                Reusable_Functions.progressDialog = new ProgressDialog(context);
                Reusable_Functions.progressDialog.setCancelable(false);
                if (!Reusable_Functions.progressDialog.isShowing()) {
                    Reusable_Functions.progressDialog.show();
                }
                Reusable_Functions.progressDialog.setMessage("Loading data...");
                limit = 100;
                offsetvalue = 0;
                count = 0;
                ez_sFirstPosVal = 0;
                ez_firstVisible_no = 0;
                requestEzoneSalesDetailAPI();
                rel_ez_viewBy.setVisibility(View.GONE);

            }
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private void viewBy_Location() {
        if (Reusable_Functions.chkStatus(context)) {
            if (rb_ez_viewBy_LocatnChk.isChecked()) {
                rb_ez_viewBy_LocatnChk.setChecked(true);
                rb_ez_viewBy_ProductChk.setChecked(false);
                rel_ez_viewBy.setVisibility(View.GONE);

            } else if (!rb_ez_viewBy_LocatnChk.isChecked()) {
                rb_ez_viewBy_LocatnChk.setChecked(true);
                rb_ez_viewBy_ProductChk.setChecked(false);
                rel_ez_next.setVisibility(View.VISIBLE);
                rel_ez_prev.setVisibility(View.INVISIBLE);
                ezone_level = 7;
                ez_segment_val = "LD";
                ez_fromWhere = "Region";
                txt_ez_header.setText("Region");
                ez_sales_detalis_array = new ArrayList<SalesAnalysisListDisplay>();
                Reusable_Functions.progressDialog = new ProgressDialog(context);
                Reusable_Functions.progressDialog.setCancelable(false);

                if (!Reusable_Functions.progressDialog.isShowing()) {
                    Reusable_Functions.progressDialog.show();
                }
                Reusable_Functions.progressDialog.setMessage("Loading data...");
                limit = 100;
                offsetvalue = 0;
                count = 0;
                ez_sFirstPosVal = 0;
                ez_firstVisible_no = 0;
                requestEzoneSalesDetailAPI();
                rel_ez_viewBy.setVisibility(View.GONE);

            }
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    // on Check change listener on Segment Listener(WTD, YTD,LW and L4W)
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (!ez_filter_toggleClick) {
            switch (checkedId) {
                case R.id.btn_ez_Ld:

                    if (ez_segment_val.equals("LD"))
                        break;
                    ez_segment_val = "LD";
                    if (ez_linear_dots != null) {
                        ez_linear_dots.removeAllViews();
                    }
                    ez_linear_hierarchy.setVisibility(View.GONE);
                    ez_currentVmPos = ez_viewpager.getCurrentItem();
                    ez_sales_detalis_array = new ArrayList<SalesAnalysisListDisplay>();
                    ez_sales_header_array = new ArrayList<SalesAnalysisViewPagerValue>();
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.progressDialog = new ProgressDialog(context);
                        Reusable_Functions.progressDialog.setCancelable(false);
                        if (!Reusable_Functions.progressDialog.isShowing()) {
                            Reusable_Functions.progressDialog.show();
                        }
                        Reusable_Functions.progressDialog.setMessage("Loading data...");
                        ez_progessBar.setVisibility(View.GONE);
                        offsetvalue = 0;
                        limit = 100;
                        count = 0;
                        val_hierarchy = "";
                        if (filterSelectedString == null) {
                            requestEzoneSalesDetailAPI();
                        } else {

                            requestEzoneFilterSelectedVal(filterSelectedString, filter_level);
                        }
                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_ez_Wtd:
                    if (ez_segment_val.equals("WTD"))
                        break;
                    ez_segment_val = "WTD";
                    if (ez_linear_dots != null) {
                        ez_linear_dots.removeAllViews();
                    }
                    ez_linear_hierarchy.setVisibility(View.GONE);
                    ez_currentVmPos = ez_viewpager.getCurrentItem();
                    ez_sales_detalis_array = new ArrayList<SalesAnalysisListDisplay>();
                    ez_sales_header_array = new ArrayList<SalesAnalysisViewPagerValue>();
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.progressDialog = new ProgressDialog(context);
                        Reusable_Functions.progressDialog.setCancelable(false);
                        if (!Reusable_Functions.progressDialog.isShowing()) {
                            Reusable_Functions.progressDialog.show();
                        }
                        Reusable_Functions.progressDialog.setMessage("Loading data...");
                        ez_progessBar.setVisibility(View.GONE);
                        offsetvalue = 0;
                        limit = 100;
                        count = 0;
                        val_hierarchy = "";
                        if (filterSelectedString == null) {
                            requestEzoneSalesDetailAPI();
                        } else {

                            requestEzoneFilterSelectedVal(filterSelectedString, filter_level);
                        }
                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_ez_Mtd:
                    if (ez_segment_val.equals("MTD"))
                        break;
                    ez_segment_val = "MTD";
                    if (ez_linear_dots != null) {
                        ez_linear_dots.removeAllViews();
                    }
                    ez_linear_hierarchy.setVisibility(View.GONE);
                    ez_currentVmPos = ez_viewpager.getCurrentItem();
                    ez_sales_detalis_array = new ArrayList<SalesAnalysisListDisplay>();
                    ez_sales_header_array = new ArrayList<SalesAnalysisViewPagerValue>();
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.progressDialog = new ProgressDialog(context);
                        Reusable_Functions.progressDialog.setCancelable(false);

                        if (!Reusable_Functions.progressDialog.isShowing()) {
                            Reusable_Functions.progressDialog.show();
                        }
                        Reusable_Functions.progressDialog.setMessage("Loading data...");
                        ez_progessBar.setVisibility(View.GONE);
                        offsetvalue = 0;
                        limit = 100;
                        count = 0;
                        val_hierarchy = "";
                        if (filterSelectedString == null) {
                            requestEzoneSalesDetailAPI();
                        } else {
                            requestEzoneFilterSelectedVal(filterSelectedString, filter_level);
                        }
                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_ez_Ytd:
                    if (ez_segment_val.equals("YTD"))
                        break;
                    ez_segment_val = "YTD";
                    if (ez_linear_dots != null) {
                        ez_linear_dots.removeAllViews();
                    }
                    ez_linear_hierarchy.setVisibility(View.GONE);
                    ez_currentVmPos = ez_viewpager.getCurrentItem();
                    ez_sales_detalis_array = new ArrayList<SalesAnalysisListDisplay>();
                    ez_sales_header_array = new ArrayList<SalesAnalysisViewPagerValue>();
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.progressDialog = new ProgressDialog(context);
                        Reusable_Functions.progressDialog.setCancelable(false);

                        if (!Reusable_Functions.progressDialog.isShowing()) {
                            Reusable_Functions.progressDialog.show();
                        }
                        Reusable_Functions.progressDialog.setMessage("Loading data...");
                        ez_progessBar.setVisibility(View.GONE);
                        offsetvalue = 0;
                        limit = 100;
                        count = 0;
                        val_hierarchy = "";
                        if (filterSelectedString == null) {

                            requestEzoneSalesDetailAPI();
                        } else {

                            requestEzoneFilterSelectedVal(filterSelectedString, filter_level);
                        }
                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        } else {
            ez_filter_toggleClick = false;
        }
    }
   //---------------------------------------------------E ZONE---------------------------------------------------//
    // Api calling functionality for E-zone module...
    private void requestEzoneSalesDetailAPI()
    {
        String url = "";
        if(!header_value.equals(""))
        {
            url = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + ez_segment_val + "&level=" + ezone_level + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId + header_value;
        }
        else
        {
            url = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + ez_segment_val + "&level=" + ezone_level + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;
        }
        //  String url £= ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e(TAG, "requestEzoneSalesDetailAPI: " + url);
        ez_postRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    Log.i(TAG, "onResponse: "+response);
                    int i;

                    if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                        ezone_onClickflg = false;
                        ez_progessBar.setVisibility(View.GONE);
                        Reusable_Functions.hDialog();

                    } else if (response.length() == limit) {
                        for (i = 0; i < response.length(); i++) {
                            ez_sales_detail_model = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                            ez_sales_detalis_array.add(ez_sales_detail_model);
                            ez_sales_adapter.addSnap(ez_sales_detail_model);

                        }
                        offsetvalue = (limit * count) + limit;
                        count++;
                        requestEzoneSalesDetailAPI();

                    } else if (response.length() < limit) {
                        for (i = 0; i < response.length(); i++) {
                            ez_sales_detail_model = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                            ez_sales_detalis_array.add(ez_sales_detail_model);
                            ez_sales_adapter.addSnap(ez_sales_detail_model);
                        }
                        for (i = 0; i < 2; i++) {
                            ImageView imgdot = new ImageView(context);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
                            layoutParams.setMargins(3, 3, 3, 3);
                            imgdot.setLayoutParams(layoutParams);
                            imgdot.setImageResource(R.mipmap.dots_unselected);
                            ez_linear_dots.addView(imgdot);
                        }
                        final int currentItem = ez_viewpager.getCurrentItem();
                        ImageView img = (ImageView) ez_linear_dots.getChildAt(currentItem);
                        img.setImageResource(R.mipmap.dots_selected);

                        // For Add "All"
                        ez_sales_detail_model = new SalesAnalysisListDisplay();
                        if (txt_ez_header.getText().toString().equals(hierarchyList[0])) {
                            ez_sales_detail_model.setLevel("All");

                        } else if (txt_ez_header.getText().toString().equals(hierarchyList[1])) {
                            ez_sales_detail_model.setLevel("All");

                        } else if (txt_ez_header.getText().toString().equals(hierarchyList[2])) {
                            ez_sales_detail_model.setLevel("All");

                        } else if (txt_ez_header.getText().toString().equals(hierarchyList[3])) {
                            ez_sales_detail_model.setLevel("All");

                        }
                        else if (txt_ez_header.getText().toString().equals("Region")) {
                            ez_sales_detail_model.setLevel("All");
                        } else if (txt_ez_header.getText().toString().equals("Store")) {
                            ez_sales_detail_model.setLevel("All");
                        }

                        ez_sales_detalis_array.add(0, ez_sales_detail_model);
                        recyclevw_ez_sales.setLayoutManager(new LinearLayoutManager(context));

                        recyclevw_ez_sales.setLayoutManager(new LinearLayoutManager(recyclevw_ez_sales.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                        recyclevw_ez_sales.setOnFlingListener(null);
                        new GravitySnapHelper(48).attachToRecyclerView(recyclevw_ez_sales);
                        ez_sales_adapter = new EzoneSalesAdapter(ez_sales_detalis_array, context, ez_firstVisible_no, ez_fromWhere, recyclevw_ez_sales,hierarchyList);
                        recyclevw_ez_sales.setAdapter(ez_sales_adapter);

                        //Retain Values.....
                        for (int j = 0; j < ez_sales_detalis_array.size(); j++) {
                            if (txt_ez_header.getText().toString().equals(hierarchyList[0])) {
                                ezone_level = 1;
                                ez_sale_first_item = ez_sales_detalis_array.get(ez_firstVisible_no).getLevel();
                                if (ez_sales_detalis_array.get(ez_firstVisible_no).getLevel().equals(ez_sale_first_item)) {
                                    recyclevw_ez_sales.getLayoutManager().scrollToPosition(ez_firstVisible_no);
                                }
                            } else if (txt_ez_header.getText().toString().equals(hierarchyList[1])) {
                                ezone_level = 2;
                                ez_sale_first_item = ez_sales_detalis_array.get(ez_firstVisible_no).getLevel();
                                if (ez_sales_detalis_array.get(ez_firstVisible_no).getLevel().equals(ez_sale_first_item)) {
                                    recyclevw_ez_sales.getLayoutManager().scrollToPosition(ez_firstVisible_no);
                                }
                            } else if (txt_ez_header.getText().toString().equals(hierarchyList[2])) {
                                ezone_level = 3;
                                ez_sale_first_item = ez_sales_detalis_array.get(ez_firstVisible_no).getLevel();
                                if (ez_sales_detalis_array.get(ez_firstVisible_no).getLevel().equals(ez_sale_first_item)) {
                                    recyclevw_ez_sales.getLayoutManager().scrollToPosition(ez_firstVisible_no);
                                }
                            } else if (txt_ez_header.getText().toString().equals(hierarchyList[3])) {
                                ezone_level = 4;
                                ez_sale_first_item = ez_sales_detalis_array.get(ez_firstVisible_no).getLevel();
                                if (ez_sales_detalis_array.get(ez_firstVisible_no).getLevel().equals(ez_sale_first_item)) {
                                    recyclevw_ez_sales.getLayoutManager().scrollToPosition(ez_firstVisible_no);
                                }

                            }

                            else if (txt_ez_header.getText().toString().equals("Region")) {
                                ezone_level = 7;
                                ez_sale_first_item = ez_sales_detalis_array.get(ez_firstVisible_no).getLevel();
                                if (ez_sales_detalis_array.get(ez_firstVisible_no).getLevel().equals(ez_sale_first_item)) {
                                    recyclevw_ez_sales.getLayoutManager().scrollToPosition(ez_firstVisible_no);
                                }
                            } else if (txt_ez_header.getText().toString().equals("Store")) {
                                ezone_level = 9;
                                ez_sale_first_item = ez_sales_detalis_array.get(ez_firstVisible_no).getLevel();
                                if (ez_sales_detalis_array.get(ez_firstVisible_no).getLevel().equals(ez_sale_first_item)) {
                                    recyclevw_ez_sales.getLayoutManager().scrollToPosition(ez_firstVisible_no);
                                }
                            }
                        }// end of for loop

                        if (ez_sale_first_item.equals("All")) {
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            ez_sales_header_array.clear();
                            ez_linear_hierarchy.setVisibility(View.GONE);
                            requestEzoneSalesHeaderAPI();
                        } else {
                            ez_linear_hierarchy.setVisibility(View.GONE);
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            ez_sales_header_array.clear();
                            requestEzoneSalesPagerOnScrollAPI();
                        }
                    }


                } catch (Exception e) {
                    Reusable_Functions.hDialog();
                    Toast.makeText(context, "no data found" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    ez_progessBar.setVisibility(View.GONE);
                    ezone_onClickflg = false;
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                        ez_progessBar.setVisibility(View.GONE);
                        ezone_onClickflg = false;
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
        ez_postRequest.setRetryPolicy(policy);
        queue.add(ez_postRequest);
    }

    private void requestEzoneFilterSelectedVal(final String filterSelectedString, final int filter_level) {

        String ezone_filter_url = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + ez_segment_val + "&level=" + filter_level + filterSelectedString + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;
        Log.e(TAG, "requestEzoneFilterSelectedVal: " +ezone_filter_url);
        ez_postRequest = new JsonArrayRequest(Request.Method.GET, ezone_filter_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, "onResponse: "+response);
                        if (filter_level == 2) {
                            rb_ez_viewBy_ProductChk.setChecked(true);
                            rb_ez_viewBy_LocatnChk.setChecked(false);
                            txt_ez_header.setText(hierarchyList[1]);
                            ez_fromWhere = hierarchyList[1];
                            rel_ez_prev.setVisibility(View.VISIBLE);
                        } else if (filter_level == 3) {
                            rb_ez_viewBy_ProductChk.setChecked(true);
                            rb_ez_viewBy_LocatnChk.setChecked(false);
                            txt_ez_header.setText(hierarchyList[2]);
                            ez_fromWhere = hierarchyList[2];
                            rel_ez_prev.setVisibility(View.VISIBLE);

                        } else if (filter_level == 4) {
                            rb_ez_viewBy_ProductChk.setChecked(true);
                            rb_ez_viewBy_LocatnChk.setChecked(false);
                            txt_ez_header.setText(hierarchyList[3]);
                            ez_fromWhere = hierarchyList[3];
                            rel_ez_prev.setVisibility(View.VISIBLE);
                            rel_ez_next.setVisibility(View.INVISIBLE);

                        }

                        else if (filter_level == 9) {
                            rb_ez_viewBy_LocatnChk.setChecked(true);
                            rb_ez_viewBy_ProductChk.setChecked(false);
                            txt_ez_header.setText("Store");
                            ez_fromWhere = "Store";
                            rel_ez_prev.setVisibility(View.VISIBLE);
                            rel_ez_next.setVisibility(View.INVISIBLE);
                        }
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                ez_progessBar.setVisibility(View.GONE);
                                ezone_onClickflg = false;

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    ez_sales_detail_model = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    ez_sales_detalis_array.add(ez_sales_detail_model);
                                    ez_sales_adapter.addSnap(ez_sales_detail_model);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestEzoneFilterSelectedVal(filterSelectedString, filter_level);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    ez_sales_detail_model = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    ez_sales_detalis_array.add(ez_sales_detail_model);
                                    ez_sales_adapter.addSnap(ez_sales_detail_model);
                                }
                                for (int i = 0; i < 2; i++) {
                                    ImageView imgdot = new ImageView(context);
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
                                    layoutParams.setMargins(3, 3, 3, 3);
                                    imgdot.setLayoutParams(layoutParams);
                                    imgdot.setImageResource(R.mipmap.dots_unselected);
                                    ez_linear_dots.addView(imgdot);
                                }
                                final int currentItem = ez_viewpager.getCurrentItem();
                                ImageView img = (ImageView) ez_linear_dots.getChildAt(currentItem);
                                img.setImageResource(R.mipmap.dots_selected);

                                // For Add "All"
                                ez_sales_detail_model = new SalesAnalysisListDisplay();
                                if (txt_ez_header.getText().toString().equals(hierarchyList[0])) {
                                    ez_sales_detail_model.setLevel("All");

                                } else if (txt_ez_header.getText().toString().equals(hierarchyList[1])) {
                                    ez_sales_detail_model.setLevel("All");

                                } else if (txt_ez_header.getText().toString().equals(hierarchyList[2])) {
                                    ez_sales_detail_model.setLevel("All");

                                } else if (txt_ez_header.getText().toString().equals(hierarchyList[3])) {
                                    ez_sales_detail_model.setLevel("All");

                                }

                                else if (txt_ez_header.getText().toString().equals("Region")) {
                                    ez_sales_detail_model.setLevel("All");
                                } else if (txt_ez_header.getText().toString().equals("Store")) {
                                    ez_sales_detail_model.setLevel("All");
                                }

                                ez_sales_detalis_array.add(0, ez_sales_detail_model);
                                recyclevw_ez_sales.setLayoutManager(new LinearLayoutManager(context));

                                recyclevw_ez_sales.setLayoutManager(new LinearLayoutManager(recyclevw_ez_sales.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                recyclevw_ez_sales.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(recyclevw_ez_sales);

                                ez_sales_adapter = new EzoneSalesAdapter(ez_sales_detalis_array, context, ez_firstVisible_no, ez_fromWhere, recyclevw_ez_sales,hierarchyList);
                                recyclevw_ez_sales.setAdapter(ez_sales_adapter);

                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                ezone_onClickflg = false;
                                analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();

                                if (txt_ez_header.getText().toString().equals(hierarchyList[0])) {
                                    ezone_level = 1;
                                    ez_sale_first_item = ez_sales_detalis_array.get(ez_firstVisible_no).getLevel();
                                } else if (txt_ez_header.getText().toString().equals(hierarchyList[1])) {
                                    ezone_level = 2;
                                    ez_sale_first_item = ez_sales_detalis_array.get(ez_firstVisible_no).getLevel();
                                } else if (txt_ez_header.getText().toString().equals(hierarchyList[2])) {
                                    ezone_level = 3;
                                    ez_sale_first_item = ez_sales_detalis_array.get(ez_firstVisible_no).getLevel();
                                } else if (txt_ez_header.getText().toString().equals(hierarchyList[3])) {
                                    ezone_level = 4;
                                    ez_sale_first_item = ez_sales_detalis_array.get(ez_firstVisible_no).getLevel();
                                }
                                else if (txt_ez_header.getText().toString().equals("Region")) {
                                    ezone_level = 7;
                                    ez_sale_first_item = ez_sales_detalis_array.get(ez_firstVisible_no).getLevel();
                                } else if (txt_ez_header.getText().toString().equals("Store")) {
                                    ezone_level = 9;
                                    ez_sale_first_item = ez_sales_detalis_array.get(ez_firstVisible_no).getLevel();
                                }
                                if (ez_sale_first_item.equals("All")) {
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    ez_sales_header_array.clear();
                                    ez_linear_hierarchy.setVisibility(View.GONE);
                                    requestEzoneSalesHeaderAPI();
                                }
                                else
                                {
                                    ez_linear_hierarchy.setVisibility(View.GONE);
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    ez_sales_header_array.clear();
                                    requestEzoneSalesPagerOnScrollAPI();
                                }
                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                            ez_progessBar.setVisibility(View.GONE);
                            ezone_onClickflg = false;
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                        ez_progessBar.setVisibility(View.GONE);
                        ezone_onClickflg = false;
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
        ez_postRequest.setRetryPolicy(policy);
        queue.add(ez_postRequest);

    }

    //-------------------------- drill down hierarchy level api implementation-----------------------------------//
    //Api - Ezone Category List
    private void requestEzoneSalesCategoryList(final String ez_sclickedVal) {
        String ez_scategory_listurl;
        ez_scategory_listurl = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + ez_segment_val + "&level=" + ezone_level + "&department=" + ez_sclickedVal.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;
        Log.e(TAG, "requestEzoneSalesCategoryList: "+ez_scategory_listurl );
        ez_postRequest = new JsonArrayRequest(Request.Method.GET, ez_scategory_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.i(TAG, "onResponse: "+response);

                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
                                ez_progessBar.setVisibility(View.GONE);
                                ezone_onClickflg = false;

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    ez_sales_detail_model = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    ez_sales_detalis_array.add(ez_sales_detail_model);
                                }

                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestEzoneSalesCategoryList(ez_sclickedVal);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    ez_sales_detail_model = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    ez_sales_detalis_array.add(ez_sales_detail_model);
                                }
                                for (int i = 0; i < 2; i++) {
                                    ImageView imgdot = new ImageView(context);
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
                                    layoutParams.setMargins(3, 3, 3, 3);
                                    imgdot.setLayoutParams(layoutParams);
                                    imgdot.setImageResource(R.mipmap.dots_unselected);
                                    ez_linear_dots.addView(imgdot);

                                }
                                int currentItem = ez_viewpager.getCurrentItem();
                                ImageView img = (ImageView) ez_linear_dots.getChildAt(currentItem);
                                img.setImageResource(R.mipmap.dots_selected);
                                // For Add "All"
                                ez_sales_detail_model = new SalesAnalysisListDisplay();
                                if (txt_ez_header.getText().toString().equals(hierarchyList[1])) {
                                    ez_sales_detail_model.setLevel("All");

                                }
                                ez_sales_detalis_array.add(0, ez_sales_detail_model);

                                recyclevw_ez_sales.setLayoutManager(new LinearLayoutManager(
                                        recyclevw_ez_sales.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                recyclevw_ez_sales.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(recyclevw_ez_sales);

                                ez_sales_adapter = new EzoneSalesAdapter(ez_sales_detalis_array, context, ez_firstVisible_no, ez_fromWhere, recyclevw_ez_sales,hierarchyList);
                                recyclevw_ez_sales.setAdapter(ez_sales_adapter);

                                val_hierarchy = "";
                                val_hierarchy = ez_sclickedVal.replaceAll("%20"," ").replaceAll("%26","&");
                                ez_txt_hierarchy_nm.setText(val_hierarchy);
                                ez_linear_hierarchy.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                ez_sales_header_array = new ArrayList<SalesAnalysisViewPagerValue>();
                                ez_sale_first_item = ez_sales_detalis_array.get(0).getLevel();

                                if (ez_sale_first_item.equals("All")) {
                                    requestEzoneSalesHeaderAPI();

                                }
                                else
                                {
                                   requestEzoneSalesPagerOnScrollAPI();
                                }
                            }


                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
                            ez_progessBar.setVisibility(View.GONE);
                            ezone_onClickflg = false;
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
                        ez_progessBar.setVisibility(View.GONE);
                        ezone_onClickflg = false;
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
        ez_postRequest.setRetryPolicy(policy);
        queue.add(ez_postRequest);
    }

    // Api - Ezone Plan class
    private void requestEzoneSalesPlanClassList(final String ez_sclickedVal) {
        String ez_splanclass_listurl;
        ez_splanclass_listurl = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + ez_segment_val + "&level=" + ezone_level + "&category=" + ez_sclickedVal.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;
        Log.e(TAG, "requestEzoneSalesPlanClassList: "+ez_splanclass_listurl );
        ez_postRequest = new JsonArrayRequest(Request.Method.GET, ez_splanclass_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.i(TAG, "onResponse: "+response);
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
                                ez_progessBar.setVisibility(View.GONE);
                                ezone_onClickflg = false;

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    ez_sales_detail_model = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    ez_sales_detalis_array.add(ez_sales_detail_model);
                                }

                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestEzoneSalesPlanClassList(ez_sclickedVal);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    ez_sales_detail_model = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    ez_sales_detalis_array.add(ez_sales_detail_model);
                                }
                                for (int i = 0; i < 2; i++) {
                                    ImageView imgdot = new ImageView(context);
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
                                    layoutParams.setMargins(3, 3, 3, 3);
                                    imgdot.setLayoutParams(layoutParams);
                                    imgdot.setImageResource(R.mipmap.dots_unselected);
                                    ez_linear_dots.addView(imgdot);

                                }
                                int currentItem = ez_viewpager.getCurrentItem();
                                ImageView img = (ImageView) ez_linear_dots.getChildAt(currentItem);
                                img.setImageResource(R.mipmap.dots_selected);

                                // For Add "All"
                                ez_sales_detail_model = new SalesAnalysisListDisplay();
                                if (txt_ez_header.getText().toString().equals(hierarchyList[2])) {
                                    ez_sales_detail_model.setLevel("All");
                                }
                                ez_sales_detalis_array.add(0, ez_sales_detail_model);

                                recyclevw_ez_sales.setLayoutManager(new LinearLayoutManager(
                                        recyclevw_ez_sales.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                recyclevw_ez_sales.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(recyclevw_ez_sales);

                                ez_sales_adapter = new EzoneSalesAdapter(ez_sales_detalis_array, context, ez_firstVisible_no, ez_fromWhere, recyclevw_ez_sales,hierarchyList);
                                recyclevw_ez_sales.setAdapter(ez_sales_adapter);

                                val_hierarchy += " > " + ez_sclickedVal.replaceAll("%20"," ").replaceAll("%26","&");
                                ez_txt_hierarchy_nm.setText(val_hierarchy);
                                ez_linear_hierarchy.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                ez_sales_header_array = new ArrayList<SalesAnalysisViewPagerValue>();
                                ez_sale_first_item = ez_sales_detalis_array.get(0).getLevel();
                                if (ez_sale_first_item.equals("All"))
                                {
                                    requestEzoneSalesHeaderAPI();
                                }
                                else
                                {
                                    requestEzoneSalesPagerOnScrollAPI();
                                }
                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
                            ez_progessBar.setVisibility(View.GONE);
                            ezone_onClickflg = false;
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
                        ez_progessBar.setVisibility(View.GONE);
                        ezone_onClickflg = false;
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
        ez_postRequest.setRetryPolicy(policy);
        queue.add(ez_postRequest);
    }

    // Api - Ezone Sales Brand list
    private void requestEzoneSalesBrandList(final String ez_sclickedVal)
    {
        String ez_sbrand_listurl;
        ez_sbrand_listurl = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + ez_segment_val + "&level=" + ezone_level + "&class=" + ez_sclickedVal.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;
        Log.e(TAG, "requestEzoneSalesBrandList: "+ez_sbrand_listurl );
        ez_postRequest = new JsonArrayRequest(Request.Method.GET, ez_sbrand_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.i(TAG, "onResponse: "+response);
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
                                ez_progessBar.setVisibility(View.GONE);
                                ezone_onClickflg = false;

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    ez_sales_detail_model = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    ez_sales_detalis_array.add(ez_sales_detail_model);
                                }

                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestEzoneSalesBrandList(ez_sclickedVal);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    ez_sales_detail_model = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    ez_sales_detalis_array.add(ez_sales_detail_model);
                                }
                                for (int i = 0; i < 2; i++) {
                                    ImageView imgdot = new ImageView(context);
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
                                    layoutParams.setMargins(3, 3, 3, 3);
                                    imgdot.setLayoutParams(layoutParams);
                                    imgdot.setImageResource(R.mipmap.dots_unselected);
                                    ez_linear_dots.addView(imgdot);

                                }
                                int currentItem = ez_viewpager.getCurrentItem();
                                ImageView img = (ImageView) ez_linear_dots.getChildAt(currentItem);
                                img.setImageResource(R.mipmap.dots_selected);
                                // For Add "All"
                                ez_sales_detail_model = new SalesAnalysisListDisplay();
                                if (txt_ez_header.getText().toString().equals(hierarchyList[3])) {
                                    ez_sales_detail_model.setLevel("All");

                                }
                                ez_sales_detalis_array.add(0, ez_sales_detail_model);

                                recyclevw_ez_sales.setLayoutManager(new LinearLayoutManager(
                                        recyclevw_ez_sales.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                recyclevw_ez_sales.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(recyclevw_ez_sales);

                                ez_sales_adapter = new EzoneSalesAdapter(ez_sales_detalis_array, context, ez_firstVisible_no, ez_fromWhere, recyclevw_ez_sales,hierarchyList);
                                recyclevw_ez_sales.setAdapter(ez_sales_adapter);

                                val_hierarchy += " > " + ez_sclickedVal.replaceAll("%20"," ").replaceAll("%26","&");
                                ez_txt_hierarchy_nm.setText(val_hierarchy);
                                ez_linear_hierarchy.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                ez_sales_header_array = new ArrayList<SalesAnalysisViewPagerValue>();
                                ez_sale_first_item = ez_sales_detalis_array.get(0).getLevel();
                                if (ez_sale_first_item.equals("All"))
                                {
                                    requestEzoneSalesHeaderAPI();
                                }
                                else
                                {
                                    requestEzoneSalesPagerOnScrollAPI();
                                }
                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
                            ez_progessBar.setVisibility(View.GONE);
                            ezone_onClickflg = false;
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
                        ez_progessBar.setVisibility(View.GONE);
                        ezone_onClickflg = false;
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
        ez_postRequest.setRetryPolicy(policy);
        queue.add(ez_postRequest);
    }

    // Api - Ezone Sales Brand Plan List
    private void requestEzoneSalesBrandPlanList(final String ez_sclickedVal) {
        String ez_sbrandplan_listurl;
        ez_sbrandplan_listurl = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + ez_segment_val + "&level=" + ezone_level + "&brand=" + ez_sclickedVal.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;
        Log.e(TAG, "requestEzoneSalesBrandPlanList: "+ez_sbrandplan_listurl );
        ez_postRequest = new JsonArrayRequest(Request.Method.GET, ez_sbrandplan_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.i(TAG, "onResponse: "+response);
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
                                ez_progessBar.setVisibility(View.GONE);
                                ezone_onClickflg = false;

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    ez_sales_detail_model = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    ez_sales_detalis_array.add(ez_sales_detail_model);
                                }

                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestEzoneSalesBrandPlanList(ez_sclickedVal);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    ez_sales_detail_model = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    ez_sales_detalis_array.add(ez_sales_detail_model);
                                }
                                for (int i = 0; i < 2; i++) {
                                    ImageView imgdot = new ImageView(context);
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
                                    layoutParams.setMargins(3, 3, 3, 3);
                                    imgdot.setLayoutParams(layoutParams);
                                    imgdot.setImageResource(R.mipmap.dots_unselected);
                                    ez_linear_dots.addView(imgdot);

                                }
                                int currentItem = ez_viewpager.getCurrentItem();
                                ImageView img = (ImageView) ez_linear_dots.getChildAt(currentItem);
                                img.setImageResource(R.mipmap.dots_selected);

                                recyclevw_ez_sales.setLayoutManager(new LinearLayoutManager(
                                        recyclevw_ez_sales.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                recyclevw_ez_sales.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(recyclevw_ez_sales);

                                ez_sales_adapter = new EzoneSalesAdapter(ez_sales_detalis_array, context, ez_firstVisible_no, ez_fromWhere, recyclevw_ez_sales,hierarchyList);
                                recyclevw_ez_sales.setAdapter(ez_sales_adapter);

                                val_hierarchy += " > " + ez_sclickedVal.replaceAll("%20"," ").replaceAll("%26","&");
                                ez_txt_hierarchy_nm.setText(val_hierarchy);
                                ez_linear_hierarchy.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                ez_sales_header_array = new ArrayList<SalesAnalysisViewPagerValue>();
                                ez_sale_first_item = ez_sales_detalis_array.get(0).getLevel();
                                requestEzoneSalesPagerOnScrollAPI();
                            }


                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
                            ez_progessBar.setVisibility(View.GONE);
                            ezone_onClickflg = false;
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
                        ez_progessBar.setVisibility(View.GONE);
                        ezone_onClickflg = false;
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
        ez_postRequest.setRetryPolicy(policy);
        queue.add(ez_postRequest);
    }

    // Api - Ezone Sales Store List
    private void requestEzoneSalesStoreList(final String ez_sclickedVal) {
        String ez_sstore_listurl;
        ez_sstore_listurl = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + ez_segment_val + "&level=" + ezone_level + "&regionDescription=" + ez_sclickedVal.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;
        Log.e(TAG, "requestEzoneSalesStoreList: "+ez_sstore_listurl );
        ez_postRequest = new JsonArrayRequest(Request.Method.GET, ez_sstore_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.i(TAG, "onResponse: "+response);
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
                                ez_progessBar.setVisibility(View.GONE);
                                ezone_onClickflg = false;

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    ez_sales_detail_model = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    ez_sales_detalis_array.add(ez_sales_detail_model);
                                }

                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestEzoneSalesStoreList(ez_sclickedVal);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    ez_sales_detail_model = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    ez_sales_detalis_array.add(ez_sales_detail_model);
                                }
                                for (int i = 0; i < 2; i++) {
                                    ImageView imgdot = new ImageView(context);
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
                                    layoutParams.setMargins(3, 3, 3, 3);
                                    imgdot.setLayoutParams(layoutParams);
                                    imgdot.setImageResource(R.mipmap.dots_unselected);
                                    ez_linear_dots.addView(imgdot);

                                }
                                int currentItem = ez_viewpager.getCurrentItem();
                                ImageView img = (ImageView) ez_linear_dots.getChildAt(currentItem);
                                img.setImageResource(R.mipmap.dots_selected);
                                 // For Add "All"
                                ez_sales_detail_model = new SalesAnalysisListDisplay();
                                if (txt_ez_header.getText().toString().equals("Store")) {
                                    ez_sales_detail_model.setLevel("All");

                                }
                                ez_sales_detalis_array.add(0, ez_sales_detail_model);

                                recyclevw_ez_sales.setLayoutManager(new LinearLayoutManager(
                                        recyclevw_ez_sales.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                recyclevw_ez_sales.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(recyclevw_ez_sales);

                                ez_sales_adapter = new EzoneSalesAdapter(ez_sales_detalis_array, context, ez_firstVisible_no, ez_fromWhere, recyclevw_ez_sales,hierarchyList);
                                recyclevw_ez_sales.setAdapter(ez_sales_adapter);
                                val_hierarchy = " ";
                                val_hierarchy = ez_sclickedVal.replaceAll("%20"," ").replaceAll("%26","&");
                                ez_txt_hierarchy_nm.setText(val_hierarchy);
                                ez_linear_hierarchy.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                ez_sales_header_array = new ArrayList<SalesAnalysisViewPagerValue>();
                                ez_sale_first_item = ez_sales_detalis_array.get(0).getLevel();
                                if (ez_sale_first_item.equals("All"))
                                {
                                    requestEzoneSalesHeaderAPI();
                                }
                                else
                                {
                                    requestEzoneSalesPagerOnScrollAPI();
                                }
                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
                            ez_progessBar.setVisibility(View.GONE);
                            ezone_onClickflg = false;
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
                        ez_progessBar.setVisibility(View.GONE);
                        ezone_onClickflg = false;
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
        ez_postRequest.setRetryPolicy(policy);
        queue.add(ez_postRequest);
    }


    // Api for display All value - Api SalesheaderEz
    private void requestEzoneSalesHeaderAPI() {

        String url="";
        if (!header_value.equals(""))
        {

            url  = ConstsCore.web_url + "/v1/display/salesheaderEZNew/" + userId + "?view=" + ez_segment_val  + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId + header_value;

        }
        else
        {

            url  = ConstsCore.web_url + "/v1/display/salesheaderEZNew/" + userId + "?view=" + ez_segment_val  + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;
        }
        Log.e(TAG, "requestEzoneSalesHeaderAPI: "+url);
        ez_postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.i(TAG, "onResponse: "+response);
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                ezone_onClickflg = false;
                                ez_progessBar.setVisibility(View.GONE);
                            } else
                                for (int i = 0; i < response.length(); i++) {

                                    ez_sales_header_model = gson.fromJson(response.get(i).toString(), SalesAnalysisViewPagerValue.class);
                                    ez_sales_header_array.add(ez_sales_header_model);
                                }

                            ez_sales_pager_adapter = new EzoneSalesPagerAdapter(context, ez_sales_header_array, ez_firstVisible_no, ez_viewpager, ez_linear_dots, ez_sales_adapter, recyclevw_ez_sales, ez_sales_detalis_array, ez_fromWhere, ez_sales_pager_adapter);
                            ez_viewpager.setAdapter(ez_sales_pager_adapter);
                            ez_viewpager.setCurrentItem(ez_currentVmPos);
                            if(ez_sales_detalis_array.size()!=0){
                                SalesAnalysisListDisplay salesAnalysisListDisplay = ez_sales_detalis_array.get(0);

                                if (salesAnalysisListDisplay.getLevel() != null) {
                                    if (salesAnalysisListDisplay.getLevel().equals("All")) {
                                        salesAnalysisListDisplay.setPvaAchieved(ez_sales_header_model.getPvaAchieved());
                                        ez_sales_detalis_array.set(0, salesAnalysisListDisplay);
                                        ez_sales_adapter.notifyDataSetChanged();

                                    }
                                }

                            }
                            ezone_onClickflg = false;
                            Reusable_Functions.hDialog();
                            ez_progessBar.setVisibility(View.GONE);


                        } catch (Exception e) {
                            ezone_onClickflg = false;
                            Reusable_Functions.hDialog();
                            ez_progessBar.setVisibility(View.GONE);
                            Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ezone_onClickflg = false;
                        Reusable_Functions.hDialog();
                        ez_progessBar.setVisibility(View.GONE);
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
        ez_postRequest.setRetryPolicy(policy);
        queue.add(ez_postRequest);

    }

    // Api for change view pager value on scroll - Api SalesDetailEz
    private void requestEzoneSalesPagerOnScrollAPI() {
        String url = " ";
        ez_sale_first_item = ez_sale_first_item.replace("%", "%25");
        ez_sale_first_item = ez_sale_first_item.replace(" ", "%20").replace("&", "%26");
        if(!header_value.equals(""))
        {
            if (txt_ez_header.getText().toString().equals(hierarchyList[0])) {
                url = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + ez_segment_val + "&level=" + ezone_level + "&department=" + ez_sale_first_item.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId + header_value;
            } else if (txt_ez_header.getText().toString().equals(hierarchyList[1])) {
                url = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + ez_segment_val + "&level=" + ezone_level + "&category=" + ez_sale_first_item.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId + header_value;
            } else if (txt_ez_header.getText().toString().equals(hierarchyList[2])) {
                url = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + ez_segment_val + "&level=" + ezone_level + "&class=" + ez_sale_first_item.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId + header_value;
            } else if (txt_ez_header.getText().toString().equals(hierarchyList[3])) {
                url = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + ez_segment_val + "&level=" + ezone_level + "&brand=" + ez_sale_first_item.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId + header_value;
            }
            else if (txt_ez_header.getText().toString().equals("Region")) {
                url = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + ez_segment_val + "&level=" + ezone_level + "&regionDescription=" + ez_sale_first_item.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId + header_value;
            } else if (txt_ez_header.getText().toString().equals("Store")) {
                url = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + ez_segment_val + "&level=" + ezone_level + "&storeCode=" + ez_sale_first_item.substring(0,4) + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId + header_value;
            }
        }
        else
        {
            if (txt_ez_header.getText().toString().equals(hierarchyList[0])) {
                url = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + ez_segment_val + "&level=" + ezone_level + "&department=" + ez_sale_first_item.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;
            } else if (txt_ez_header.getText().toString().equals(hierarchyList[1])) {
                url = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + ez_segment_val + "&level=" + ezone_level + "&category=" + ez_sale_first_item.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;
            } else if (txt_ez_header.getText().toString().equals(hierarchyList[2])) {
                url = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + ez_segment_val + "&level=" + ezone_level + "&class=" + ez_sale_first_item.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;
            } else if (txt_ez_header.getText().toString().equals(hierarchyList[3])) {
                url = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + ez_segment_val + "&level=" + ezone_level + "&brand=" + ez_sale_first_item.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;
            }
            else if (txt_ez_header.getText().toString().equals("Region")) {
                url = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + ez_segment_val + "&level=" + ezone_level + "&regionDescription=" + ez_sale_first_item.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;
            } else if (txt_ez_header.getText().toString().equals("Store")) {
                url = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + ez_segment_val + "&level=" + ezone_level + "&storeCode=" + ez_sale_first_item.substring(0,4) + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;
            }
        }
        Log.e(TAG, "requestEzoneSalesPagerOnScrollAPI: "+url);
        ez_postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.i(TAG, "onResponse: "+response);
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                ez_progessBar.setVisibility(View.GONE);
                                ezone_onClickflg = false;
                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    ez_sales_header_model = gson.fromJson(response.get(i).toString(), SalesAnalysisViewPagerValue.class);
                                    ez_sales_header_array.add(ez_sales_header_model);

                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestEzoneSalesPagerOnScrollAPI();


                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    ez_sales_header_model = gson.fromJson(response.get(i).toString(), SalesAnalysisViewPagerValue.class);
                                    ez_sales_header_array.add(ez_sales_header_model);

                                }

                                ez_sales_pager_adapter = new EzoneSalesPagerAdapter(context, ez_sales_header_array, ez_firstVisible_no, ez_viewpager, ez_linear_dots, ez_sales_adapter, recyclevw_ez_sales, ez_sales_detalis_array, ez_fromWhere, ez_sales_pager_adapter);
                                ez_viewpager.setAdapter(ez_sales_pager_adapter);
                                ez_viewpager.setCurrentItem(ez_currentVmPos);
                                ezone_onClickflg = false;
                                Reusable_Functions.hDialog();
                                ez_progessBar.setVisibility(View.GONE);

                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                            ez_progessBar.setVisibility(View.GONE);
                            ezone_onClickflg = false;
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                        ez_progessBar.setVisibility(View.GONE);
                        ezone_onClickflg = false;
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
        ez_postRequest.setRetryPolicy(policy);
        queue.add(ez_postRequest);
    }

    @Override
    public void onBackPressed() {

        ezone_level = 0;
        ez_segment_val = "";
        ezone_level = 1;
        ez_segment_val = "LD";
        this.finish();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        int checkedId = tab.getPosition();

        switch (checkedId) {
            case 0:

                if (ez_segment_val.equals("LD"))
                    break;
                ez_segment_val = "LD";
                if (ez_linear_dots != null) {
                    ez_linear_dots.removeAllViews();
                }
                ez_linear_hierarchy.setVisibility(View.GONE);
                ez_currentVmPos = ez_viewpager.getCurrentItem();
                ez_sales_detalis_array = new ArrayList<SalesAnalysisListDisplay>();
                ez_sales_header_array = new ArrayList<SalesAnalysisViewPagerValue>();
                if (Reusable_Functions.chkStatus(context)) {
                    // Reusable_Functions.hDialog();
                    // Reusable_Functions.sDialog(context, "Loading data...");
                    Reusable_Functions.progressDialog = new ProgressDialog(context);
                    Reusable_Functions.progressDialog.setCancelable(false);
                    if (!Reusable_Functions.progressDialog.isShowing()) {
                        Reusable_Functions.progressDialog.show();
                    }
                    Reusable_Functions.progressDialog.setMessage("Loading data...");
                    ez_progessBar.setVisibility(View.GONE);
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    val_hierarchy = "";
                    requestEzoneSalesDetailAPI();
//                    requestEzoneFilterSelectedVal(filterSelectedString , filter_level);

                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
                break;

            case 1:
                if (ez_segment_val.equals("WTD"))
                    break;
                ez_segment_val = "WTD";
                if (ez_linear_dots != null) {
                    ez_linear_dots.removeAllViews();
                }
                ez_linear_hierarchy.setVisibility(View.GONE);
                ez_currentVmPos = ez_viewpager.getCurrentItem();
                ez_sales_detalis_array = new ArrayList<SalesAnalysisListDisplay>();
                ez_sales_header_array = new ArrayList<SalesAnalysisViewPagerValue>();
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.progressDialog = new ProgressDialog(context);
                    Reusable_Functions.progressDialog.setCancelable(false);

                    if (!Reusable_Functions.progressDialog.isShowing()) {
                        Reusable_Functions.progressDialog.show();
                    }
                    Reusable_Functions.progressDialog.setMessage("Loading data...");
                    ez_progessBar.setVisibility(View.GONE);
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    val_hierarchy = "";
                    requestEzoneSalesDetailAPI();
//                    requestEzoneFilterSelectedVal(filterSelectedString , filter_level);
                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
                break;

            case 2:
                if (ez_segment_val.equals("MTD"))
                    break;
                ez_segment_val = "MTD";
                if (ez_linear_dots != null) {
                    ez_linear_dots.removeAllViews();
                }
                ez_linear_hierarchy.setVisibility(View.GONE);
                ez_currentVmPos = ez_viewpager.getCurrentItem();
                ez_sales_detalis_array = new ArrayList<SalesAnalysisListDisplay>();
                ez_sales_header_array = new ArrayList<SalesAnalysisViewPagerValue>();
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.progressDialog = new ProgressDialog(context);
                    Reusable_Functions.progressDialog.setCancelable(false);

                    if (!Reusable_Functions.progressDialog.isShowing()) {
                        Reusable_Functions.progressDialog.show();
                    }
                    Reusable_Functions.progressDialog.setMessage("Loading data...");
                    ez_progessBar.setVisibility(View.GONE);
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    val_hierarchy = "";
                    requestEzoneSalesDetailAPI();

//                    requestEzoneFilterSelectedVal(filterSelectedString , filter_level);
                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
                break;

            case 3:
                if (ez_segment_val.equals("YTD"))
                    break;
                ez_segment_val = "YTD";
                if (ez_linear_dots != null) {
                    ez_linear_dots.removeAllViews();
                }
                ez_linear_hierarchy.setVisibility(View.GONE);
                ez_currentVmPos = ez_viewpager.getCurrentItem();
                ez_sales_detalis_array = new ArrayList<SalesAnalysisListDisplay>();
                ez_sales_header_array = new ArrayList<SalesAnalysisViewPagerValue>();
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.progressDialog = new ProgressDialog(context);
                    Reusable_Functions.progressDialog.setCancelable(false);

                    if (!Reusable_Functions.progressDialog.isShowing()) {
                        Reusable_Functions.progressDialog.show();
                    }
                    Reusable_Functions.progressDialog.setMessage("Loading data...");
                    ez_progessBar.setVisibility(View.GONE);
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    val_hierarchy = "";


                    requestEzoneSalesDetailAPI();

//                    requestEzoneFilterSelectedVal(filterSelectedString , filter_level);
                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
                break;
        }


    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
