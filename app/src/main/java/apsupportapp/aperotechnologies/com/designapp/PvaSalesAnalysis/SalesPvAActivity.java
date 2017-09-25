package apsupportapp.aperotechnologies.com.designapp.PvaSalesAnalysis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;

import apsupportapp.aperotechnologies.com.designapp.MyMarkerView;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.RunningPromo.RecyclerViewPositionHelper;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.EzoneSalesFilter;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesAnalysisFilter;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesFilterActivity;
import apsupportapp.aperotechnologies.com.designapp.model.RecyclerItemClickListener;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisListDisplay;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisViewPagerValue;
import apsupportapp.aperotechnologies.com.designapp.model.SalesPvAAnalysisWeek;


public class SalesPvAActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {

    SharedPreferences sharedPreferences;
    int offsetvalue = 0, limit = 100;
    int count = 0;
    RequestQueue queue;
    RadioButton btn_WTD, btn_LW;
    private static String salesPvA_SegmentClick = "WTD", ez_tabClick = "WTD";
    ArrayList<SalesAnalysisListDisplay> salesAnalysisClassArrayList;
    LinearLayout llpvahierarchy;
    String userId, bearertoken, storeDescription, geoLeveLDesc,geoLevel2Code, lobId;
    PvASnapAdapter salesPvAAdapter;
    ViewPortHandler handler;
    Context context;
    public static Activity SalesPvAActivity;
    BarChart barChart, ez_barChart;
    RecyclerView listViewSalesPvA, ez_listView;
    int focusposition, selFirstPositionValue = 0, totalItemCount;
    private static int level = 1, ez_level = 1;
    SalesAnalysisListDisplay salesAnalysisListDisplay;
    SalesPvAAnalysisWeek salesPvAAnalysisWeek;
    ArrayList<SalesPvAAnalysisWeek> salesPvAAnalysisWeekArrayList;
    String fromWhere, txtPvAClickedValue, ez_fromWhere, ez_txtClickedVal;
    TextView txtStoreCode, txtStoreDesc, txtheaderplanclass, txtpvahDeptName, txt_pva_noChart, ez_txtheaderclass, ez_txtpvahDeptName;
    RelativeLayout tableRelLayout, btnBack, btnFilter, llayoutSalesPvA, btnSalesPrev, btnSalesNext, rel_store_layout, btn_ez_back, btn_ez_filter, btn_ez_prev, btn_ez_next, btn_ez_sort;
    Gson gson;
    String pvaFirstVisibleItem, ez_firstVisibleItem;
    JsonArrayRequest postRequest, ez_postRequest;
    ArrayList<SalesAnalysisViewPagerValue> arrayList;
    static SalesAnalysisViewPagerValue salesAnalysisViewPagerValue;
    static String planDept, planCategory, planClass;
    String pvaVal, TAG = "SalesPvAActivity";
    int currentIndex, prevState = RecyclerView.SCROLL_STATE_IDLE, currentState = RecyclerView.SCROLL_STATE_IDLE;
    boolean onItemClickFlag = false, filter_toggleClick = false;
    ProgressBar pva_progressBar, ez_progressBar;
    public static Activity Sales_Pva_Activity;
    private TabLayout tabLayout, ez_tabLayout;
    private PopupWindow popupWindow;
    private RadioButton product_radiobtn, location_radiobtn;
    private int preValue = 1, postValue;
    private boolean from_filter;
    private String filterSelectedString, isMultiStore, value;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        Sales_Pva_Activity = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        storeDescription = sharedPreferences.getString("storeDescription", "");
        geoLeveLDesc = sharedPreferences.getString("geoLeveLDesc", "");
        geoLevel2Code = sharedPreferences.getString("concept", "");
        lobId = sharedPreferences.getString("lobid","");
        isMultiStore = sharedPreferences.getString("isMultiStore","");
        value = sharedPreferences.getString("value","");
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();
        if (geoLeveLDesc.equals("E ZONE"))
        {
            setContentView(R.layout.activity_ezone_pva);
            Log.e(TAG, "----Wellcome in Ezone----");
            getSupportActionBar().hide();
            salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
            commanInitialize();
            initializeEzoneUI();
            commanListView();

        } else {
            setContentView(R.layout.activity_sales_pva);
            Log.e(TAG, "----Wellcome in FBB----");
            getSupportActionBar().hide();
            salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
            commanInitialize();
            initializeFBBUI();
            commanListView();


        }
    }


    private void commanListView() {

        filterSelectedString = getIntent().getStringExtra("selectedStringVal");
        int filter_level = getIntent().getIntExtra("selectedlevelVal", 0);
        fromWhere = "Department";
        focusposition = 0;
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.sDialog(context, "Loading data...");
            pva_progressBar.setVisibility(View.GONE);
            offsetvalue = 0;
            limit = 100;
            count = 0;
            level = 1;
            llpvahierarchy.setVisibility(View.GONE);

            if (filterSelectedString == null) {

                if (getIntent().getStringExtra("selectedDept") == null) {
                    filter_toggleClick = false;
                    retainSegmentValuesFilter();
                    requestSalesViewPagerValueAPI();
                } else if (getIntent().getStringExtra("selectedDept") != null) {
                    String selectedString = getIntent().getStringExtra("selectedDept");
                    filter_toggleClick = true;
                    retainSegmentValuesFilter();
                    requestSalesSelectedFilterVal(selectedString);
                }

            } else {

                Log.e("filter back calling", "=======");
                retainSegmentValuesFilter();
                level = filter_level;
                from_filter = true;
                if (filter_level == 9) {
                    preValue = 2;
                    location_radiobtn.setChecked(true);
                    product_radiobtn.setChecked(false);
                }
                requestfromPvaFilter(filterSelectedString);

            }


        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }

        listViewSalesPvA.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                RecyclerViewPositionHelper mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
                totalItemCount = mRecyclerViewHelper.getItemCount();
                focusposition = mRecyclerViewHelper.findFirstVisibleItemPosition();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                currentState = newState;
                if (prevState != RecyclerView.SCROLL_STATE_IDLE && currentState == RecyclerView.SCROLL_STATE_IDLE) {
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        public void run() {
                            if (!onItemClickFlag) {
                                TimeUP();
                            }
                        }
                    }, 700);
                }
                prevState = currentState;
            }
        });

        //Drill Down
        listViewSalesPvA.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                if (pva_progressBar.getVisibility() == View.VISIBLE) {
                    return;
                } else {
                    onItemClickFlag = true;
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        public void run() {
                            if (position < salesAnalysisClassArrayList.size()) {
                                if (geoLeveLDesc.equals("E ZONE")) {
                                    switch (txtheaderplanclass.getText().toString())
                                    {
                                        case "Department":
                                            btnSalesPrev.setVisibility(View.VISIBLE);

                                            fromWhere = "Subdept";
                                            txtheaderplanclass.setText("Subdept");
                                            txtPvAClickedValue = salesAnalysisClassArrayList.get(position).getLevel();

                                            level = 2;
                                            if (Reusable_Functions.chkStatus(context))
                                            {
                                                if (postRequest != null) {
                                                    postRequest.cancel();
                                                }
                                                Reusable_Functions.hDialog();
                                                Reusable_Functions.sDialog(context, "Loading data...");
                                                pva_progressBar.setVisibility(View.GONE);
                                                offsetvalue = 0;
                                                limit = 100;
                                                count = 0;
                                                salesAnalysisClassArrayList.clear();
                                                Log.e(TAG, "click on: " + txtPvAClickedValue);
                                                requestSalesPvACategoryList(txtPvAClickedValue);
                                                planDept = txtPvAClickedValue;

                                            } else {
                                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                            }
                                            break;

                                        case "Subdept":
                                            txtheaderplanclass.setText("Class");
                                            txtPvAClickedValue = salesAnalysisClassArrayList.get(position).getLevel();

                                            fromWhere = "Class";
                                            level = 3;
                                            if (Reusable_Functions.chkStatus(context)) {
                                                if (postRequest != null) {
                                                    postRequest.cancel();
                                                }
                                                Reusable_Functions.hDialog();
                                                Reusable_Functions.sDialog(context, "Loading data...");
                                                pva_progressBar.setVisibility(View.GONE);
                                                offsetvalue = 0;
                                                limit = 100;
                                                count = 0;
                                                salesAnalysisClassArrayList.clear();

                                                requestSalesPvAPlanClassListAPI(txtPvAClickedValue);
                                                planCategory = txtPvAClickedValue;
                                            } else {
                                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                            }


                                            break;
                                        case "Class":

                                            txtheaderplanclass.setText("Subclass");
                                            txtPvAClickedValue = salesAnalysisClassArrayList.get(position).getLevel();
                                            btnSalesNext.setVisibility(View.INVISIBLE);

                                            fromWhere = "Subclass";
                                            level = 4;
                                            if (Reusable_Functions.chkStatus(context))
                                            {
                                                if (postRequest != null) {
                                                    postRequest.cancel();
                                                }
                                                Reusable_Functions.hDialog();
                                                Reusable_Functions.sDialog(context, "Loading data...");
                                                pva_progressBar.setVisibility(View.GONE);
                                                offsetvalue = 0;
                                                limit = 100;
                                                count = 0;
                                                salesAnalysisClassArrayList.clear();
                                                requestSalesPvABrandListAPI(txtPvAClickedValue);
                                                planClass = txtPvAClickedValue;
                                            }
                                            else
                                            {
                                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                            }

                                            break;
//                                        case "Subclass":
//                                                Reusable_Functions.hDialog();
//                                                Toast.makeText(context, " You are at the last level of hierarchy", Toast.LENGTH_SHORT).show();
//                                                onItemClickFlag = false;
//                                                break;
//                                            btnSalesNext.setVisibility(View.INVISIBLE);
//                                            txtheaderplanclass.setText("Brand Class");
//                                            if (geoLeveLDesc.equals("E ZONE")) {
//                                                txtPvAClickedValue = salesAnalysisClassArrayList.get(position).getLevel();
//                                            } else {
//                                                txtPvAClickedValue = salesAnalysisClassArrayList.get(position).getBrandName();
//                                            }
//                                            fromWhere = "Brand Class";
//                                            level = 5;
//                                            if (Reusable_Functions.chkStatus(context)) {
//                                                if (postRequest != null) {
//                                                    postRequest.cancel();
//                                                }
//                                                Reusable_Functions.hDialog();
//                                                Reusable_Functions.sDialog(context, "Loading data...");
//                                                pva_progressBar.setVisibility(View.GONE);
//                                                offsetvalue = 0;
//                                                limit = 100;
//                                                count = 0;
//                                                salesAnalysisClassArrayList.clear();
//                                                requestSalesPvABrandPlanListAPI(txtPvAClickedValue);
//                                            } else {
//                                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
//                                            }
//                                            break;

                                        case "Region":
                                            btnSalesNext.setVisibility(View.INVISIBLE);
                                            btnSalesPrev.setVisibility(View.VISIBLE);
                                            txtheaderplanclass.setText("Store");
                                            txtPvAClickedValue = salesAnalysisClassArrayList.get(position).getLevel();
                                            fromWhere = "Store";
                                            level = 9;
                                            if (Reusable_Functions.chkStatus(context)) {
                                                if (postRequest != null) {
                                                    postRequest.cancel();
                                                }
                                                Reusable_Functions.hDialog();
                                                Reusable_Functions.sDialog(context, "Loading data...");
                                                pva_progressBar.setVisibility(View.GONE);
                                                offsetvalue = 0;
                                                limit = 100;
                                                count = 0;
                                                salesAnalysisClassArrayList.clear();
                                                requestProductAndLocation(txtPvAClickedValue);
                                            } else {
                                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                            }
                                            break;
                                        default:
                                            Reusable_Functions.hDialog();
                                            Toast.makeText(context, " You are at the last level of hierarchy", Toast.LENGTH_SHORT).show();
                                            onItemClickFlag = false;
                                            break;

                                    }
                                } else // FBB login
                                {
                                    switch (txtheaderplanclass.getText().toString()) {
                                        case "Department":
                                            btnSalesPrev.setVisibility(View.VISIBLE);

                                            fromWhere = "Category";
                                            txtheaderplanclass.setText("Category");
                                            txtPvAClickedValue = salesAnalysisClassArrayList.get(position).getPlanDept();

                                            level = 2;
                                            if (Reusable_Functions.chkStatus(context)) {
                                                if (postRequest != null) {
                                                    postRequest.cancel();
                                                }
                                                Reusable_Functions.hDialog();
                                                Reusable_Functions.sDialog(context, "Loading data...");
                                                pva_progressBar.setVisibility(View.GONE);
                                                offsetvalue = 0;
                                                limit = 100;
                                                count = 0;
                                                salesAnalysisClassArrayList.clear();
                                                Log.e(TAG, "click on: " + txtPvAClickedValue);
                                                requestSalesPvACategoryList(txtPvAClickedValue);
                                                planDept = txtPvAClickedValue;

                                            } else {
                                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                            }
                                            break;

                                        case "Category":

                                            txtheaderplanclass.setText("Class");

                                            txtPvAClickedValue = salesAnalysisClassArrayList.get(position).getPlanCategory();

                                            fromWhere = "Class";
                                            level = 3;
                                            if (Reusable_Functions.chkStatus(context)) {
                                                if (postRequest != null) {
                                                    postRequest.cancel();
                                                }
                                                Reusable_Functions.hDialog();
                                                Reusable_Functions.sDialog(context, "Loading data...");
                                                pva_progressBar.setVisibility(View.GONE);
                                                offsetvalue = 0;
                                                limit = 100;
                                                count = 0;
                                                salesAnalysisClassArrayList.clear();

                                                requestSalesPvAPlanClassListAPI(txtPvAClickedValue);
                                                planCategory = txtPvAClickedValue;
                                            } else {
                                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                            }


                                            break;
                                        case "Class":
                                            txtheaderplanclass.setText("Brand");
                                            txtPvAClickedValue = salesAnalysisClassArrayList.get(position).getPlanClass();
                                            fromWhere = "Brand";
                                            level = 4;
                                            if (Reusable_Functions.chkStatus(context)) {
                                                if (postRequest != null) {
                                                    postRequest.cancel();
                                                }
                                                Reusable_Functions.hDialog();
                                                Reusable_Functions.sDialog(context, "Loading data...");
                                                pva_progressBar.setVisibility(View.GONE);
                                                offsetvalue = 0;
                                                limit = 100;
                                                count = 0;
                                                salesAnalysisClassArrayList.clear();
                                                requestSalesPvABrandListAPI(txtPvAClickedValue);
                                                planClass = txtPvAClickedValue;
                                            } else {
                                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                            }

                                            break;
                                        case "Brand":

                                            btnSalesNext.setVisibility(View.INVISIBLE);
                                            txtheaderplanclass.setText("Brand Class");
                                            txtPvAClickedValue = salesAnalysisClassArrayList.get(position).getBrandName();
                                            fromWhere = "Brand Class";
                                            level = 5;
                                            if (Reusable_Functions.chkStatus(context))
                                            {
                                                if (postRequest != null) {
                                                    postRequest.cancel();
                                                }
                                                Reusable_Functions.hDialog();
                                                Reusable_Functions.sDialog(context, "Loading data...");
                                                pva_progressBar.setVisibility(View.GONE);
                                                offsetvalue = 0;
                                                limit = 100;
                                                count = 0;
                                                salesAnalysisClassArrayList.clear();
                                                requestSalesPvABrandPlanListAPI(txtPvAClickedValue);
                                            }
                                            else
                                            {
                                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                            }
                                            break;

                                        default:
                                            Reusable_Functions.hDialog();
                                            Toast.makeText(context, " You are at the last level of hierarchy", Toast.LENGTH_SHORT).show();
                                            onItemClickFlag = false;
                                            break;

                                    }
                                }
                            }
                        }
                    }, 700);
                }
            }
        }));
    }

    private void requestfromPvaFilter(String filterSelectedString) {

        if(geoLeveLDesc.equals("E ZONE"))
        {
            if (level == 2) {
                txtheaderplanclass.setText("Subdept");
                fromWhere = "Subdept";
                btnSalesPrev.setVisibility(View.VISIBLE);
            } else if (level == 3) {
                txtheaderplanclass.setText("Class");
                fromWhere = "Class";
                btnSalesPrev.setVisibility(View.VISIBLE);
            } else if (level == 4) {
                txtheaderplanclass.setText("Subclass");
                fromWhere = "Subclass";
                btnSalesPrev.setVisibility(View.VISIBLE);
            }
//            else if (level == 5) {
//                txtheaderplanclass.setText("Subclass");
//                fromWhere = "Subclass";
//                btnSalesPrev.setVisibility(View.VISIBLE);
//                btnSalesNext.setVisibility(View.INVISIBLE);
//            }
//            else if (level == 6)
//            {
//                txtheaderplanclass.setText("Brand Class");
//                fromWhere = "Brand Class";
//                btnSalesPrev.setVisibility(View.VISIBLE);
//                btnSalesNext.setVisibility(View.INVISIBLE);
//            }
            else if (level == 9) {
                txtheaderplanclass.setText("Store");
                fromWhere = "Store";
                btnSalesPrev.setVisibility(View.VISIBLE);
                btnSalesNext.setVisibility(View.INVISIBLE);
            }
        }
        else
        {
            if (level == 2) {
                txtheaderplanclass.setText("Category");
                fromWhere = "Category";
                btnSalesPrev.setVisibility(View.VISIBLE);
            } else if (level == 3) {
                txtheaderplanclass.setText("Class");
                fromWhere = "Class";
                btnSalesPrev.setVisibility(View.VISIBLE);
            } else if (level == 4) {
                txtheaderplanclass.setText("Brand");
                fromWhere = "Brand";
                btnSalesPrev.setVisibility(View.VISIBLE);
            } else if (level == 5) {
                txtheaderplanclass.setText("Brand Class");
                fromWhere = "Brand Class";
                btnSalesPrev.setVisibility(View.VISIBLE);
                btnSalesNext.setVisibility(View.INVISIBLE);
            } else if (level == 6) {
                txtheaderplanclass.setText("Brand Class");
                fromWhere = "Brand Class";
                btnSalesPrev.setVisibility(View.VISIBLE);
                btnSalesNext.setVisibility(View.INVISIBLE);
            }
        }

        requestSalesViewPagerValueAPI();


    }

    private void initializeEzoneUI() {
        btnFilter = (RelativeLayout) findViewById(R.id.imgfilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent filterIntent = new Intent(SalesPvAActivity.this, EzoneSalesFilter.class);
                filterIntent.putExtra("checkfrom", "pvaAnalysis");
                startActivity(filterIntent);
            }
        });
        show_popup();
        RelativeLayout sortPopup = (RelativeLayout) findViewById(R.id.sortPopup);
        sortPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.showAsDropDown(view);
            }
        });

    }

    private void commanInitialize() {
        SalesPvAActivity = this;
        pva_progressBar = (ProgressBar) findViewById(R.id.pva_progressBar);
        tabLayout = (TabLayout) findViewById(R.id.tabview_salespva);
        tabLayout.addTab(tabLayout.newTab().setText("WTD"));
        tabLayout.addTab(tabLayout.newTab().setText("LW"));
        tabLayout.setOnTabSelectedListener(this);
        listViewSalesPvA = (RecyclerView) findViewById(R.id.list);
        llpvahierarchy = (LinearLayout) findViewById(R.id.llpvahierarchy);
        llpvahierarchy.setOrientation(LinearLayout.HORIZONTAL);
        barChart = (BarChart) findViewById(R.id.bar_chart);

        rel_store_layout = (RelativeLayout) findViewById(R.id.rel_store_layout);
        rel_store_layout.setVisibility(View.VISIBLE);
        txtStoreCode = (TextView) findViewById(R.id.txtStoreCode);
        txtStoreDesc = (TextView) findViewById(R.id.txtStoreName);
        if(isMultiStore.equals("Yes"))
        {
            txtStoreCode.setText("Concept : ");
            txtStoreDesc.setText(value);

        }
        else
        {
            txtStoreCode.setText("Store : ");
            txtStoreDesc.setText(value);
        }
        Log.e(TAG, "store desc: " + storeDescription);
        if (geoLeveLDesc.equals("E ZONE"))
        {
            rel_store_layout.setVisibility(View.GONE);
        }
        else
        {
            rel_store_layout.setVisibility(View.VISIBLE);
//            txtStoreCode.setText(storeDescription.trim().substring(0, 4));
//            txtStoreDesc.setText(storeDescription.substring(5));
        }

        txt_pva_noChart = (TextView) findViewById(R.id.pva_noChart);
        //hierarchy header
        txtpvahDeptName = (TextView) findViewById(R.id.txtpvahDeptName);
        pvaVal = " ";
        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
        salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
        arrayList = new ArrayList<SalesAnalysisViewPagerValue>();

        llayoutSalesPvA = (RelativeLayout) findViewById(R.id.llayoutSalesPvA);

        tableRelLayout = (RelativeLayout) findViewById(R.id.relTablelayout);

        handler = barChart.getViewPortHandler();
        txtheaderplanclass = (TextView) findViewById(R.id.txtPlanClass);
        btnSalesPrev = (RelativeLayout) findViewById(R.id.btnSalesBack);
        btnSalesPrev.setVisibility(View.INVISIBLE);
        btnSalesPrev.setOnClickListener(this);
        btnSalesNext = (RelativeLayout) findViewById(R.id.btnSalesNext);
        btnSalesNext.setOnClickListener(this);
        btnBack = (RelativeLayout) findViewById(R.id.imageBtnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void initializeFBBUI() {
        btnFilter = (RelativeLayout) findViewById(R.id.imgfilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent filterIntent = new Intent(SalesPvAActivity.this, SalesAnalysisFilter.class);
                filterIntent.putExtra("checkfrom", "pvaAnalysis");
                startActivity(filterIntent);
            }
        });

        final RelativeLayout rel_overlay = (RelativeLayout) findViewById(R.id.rel_overlay);
        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);

        rel_overlay.setOnClickListener(null);

        menuMultipleActions.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                rel_overlay.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMenuCollapsed() {
                rel_overlay.setVisibility(View.GONE);
            }
        });

        final FloatingActionButton action_department = (FloatingActionButton) findViewById(R.id.action_department);
        action_department.setSize(FloatingActionButton.SIZE_MINI);
        action_department.setColorNormalResId(R.color.pink);
        action_department.setColorPressedResId(R.color.ezfb_Red);
        action_department.setIcon(R.drawable.ic_fab_star);
        action_department.setStrokeVisible(false);


        action_department.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                menuMultipleActions.collapse();
            }
        });

        final FloatingActionButton action_category = (FloatingActionButton) findViewById(R.id.action_category);
        action_category.setSize(FloatingActionButton.SIZE_MINI);
        action_category.setColorNormalResId(R.color.pink);
        action_category.setColorPressedResId(R.color.ezfb_Red);
        action_category.setIcon(R.drawable.ic_fab_star);
        action_category.setStrokeVisible(false);

        action_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                menuMultipleActions.collapse();

            }
        });

        final FloatingActionButton action_class = (FloatingActionButton) findViewById(R.id.action_class);
        action_class.setSize(FloatingActionButton.SIZE_MINI);
        action_class.setColorNormalResId(R.color.pink);
        action_class.setColorPressedResId(R.color.ezfb_Red);
        action_class.setIcon(R.drawable.ic_fab_star);
        action_class.setStrokeVisible(false);

        action_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                menuMultipleActions.collapse();
            }
        });

        final FloatingActionButton action_brand = (FloatingActionButton) findViewById(R.id.action_brand);
        action_brand.setSize(FloatingActionButton.SIZE_MINI);
        action_brand.setColorNormalResId(R.color.pink);
        action_brand.setColorPressedResId(R.color.ezfb_Red);
        action_brand.setIcon(R.drawable.ic_fab_star);
        action_brand.setStrokeVisible(false);

        action_brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                menuMultipleActions.collapse();
            }
        });

        final FloatingActionButton action_brandclass = (FloatingActionButton) findViewById(R.id.action_brandclass);
        action_brandclass.setSize(FloatingActionButton.SIZE_MINI);
        action_brandclass.setColorNormalResId(R.color.pink);
        action_brandclass.setColorPressedResId(R.color.ezfb_Red);
        action_brandclass.setIcon(R.drawable.ic_fab_star);
        action_brandclass.setStrokeVisible(false);

        action_brandclass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                menuMultipleActions.collapse();
            }
        });

        final FloatingActionButton action_location = (FloatingActionButton) findViewById(R.id.action_location);
        action_location.setSize(FloatingActionButton.SIZE_MINI);
        action_location.setColorNormalResId(R.color.pink);
        action_location.setColorPressedResId(R.color.ezfb_Red);
        action_location.setIcon(R.drawable.ic_fab_star);
        action_location.setStrokeVisible(false);

        action_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                menuMultipleActions.collapse();
            }
        });

    }

    public void show_popup() {

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.activity_ezon_sorting, null);

        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout location = (LinearLayout) popupView.findViewById(R.id.lin_ez_location);
        LinearLayout product = (LinearLayout) popupView.findViewById(R.id.lin_ez_Product);
        product_radiobtn = (RadioButton) popupView.findViewById(R.id.rb_ez_viewBy_ProductChk);
        location_radiobtn = (RadioButton) popupView.findViewById(R.id.rb_ez_viewBy_LocatnChk);
        if (preValue == 1) {
            product_radiobtn.setChecked(true);
        } else {
            location_radiobtn.setChecked(true);
        }

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postValue = 2;
                from_filter = false;
                location_radiobtn.setChecked(true);
                product_radiobtn.setChecked(false);
                popupWindow.dismiss();
                sortFunction();
                // popupWindow.dismiss();

            }
        });

        product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postValue = 1;
                from_filter = false;
                product_radiobtn.setChecked(true);
                location_radiobtn.setChecked(false);
                popupWindow.dismiss();
                sortFunction();

                // popupWindow.dismiss();


            }
        });


        popupWindow.setOutsideTouchable(true);
        //   popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);  //focus as a side background

        // Removes default black background
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //TODO do sth here on dismiss
                popupWindow.dismiss();


            }
        });


    }

    private void sortFunction() {

        if (!(postValue == preValue))
        {
            Log.e(TAG, "sortFunction: post value is" + postValue + " and prevalue" + preValue);
// for product...
            if (postValue == 1) {

                Log.e(TAG, "sortFunction: true...");
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.sDialog(context, "Loading data...");
                    llpvahierarchy.setVisibility(View.GONE);
                    preValue = postValue;
                    txtheaderplanclass.setText("Department");
                    level = 1;
                    btnSalesPrev.setVisibility(View.INVISIBLE);
                    btnSalesNext.setVisibility(View.VISIBLE);
                    salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                    salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                    arrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                    requestSalesViewPagerValueAPI();
                }
                else
                {
                    product_radiobtn.setChecked(false);
                    location_radiobtn.setChecked(true);
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }

            } else if (postValue == 2) {
// for location...
                if (Reusable_Functions.chkStatus(context))
                {
                    Reusable_Functions.sDialog(context, "Loading data...");
                    llpvahierarchy.setVisibility(View.GONE);
                    txtheaderplanclass.setText("Region");
                    btnSalesPrev.setVisibility(View.INVISIBLE);
                    btnSalesNext.setVisibility(View.VISIBLE);
                    preValue = postValue;
                    level = 7;
                    salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                    salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                    arrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                    requestSalesViewPagerValueAPI();
                }
                else
                {
                    product_radiobtn.setChecked(true);
                    location_radiobtn.setChecked(false);
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void retainSegmentValuesFilter()
    {
        filter_toggleClick = true;
        if (salesPvA_SegmentClick.equals("WTD")) {
            //   btn_WTD.toggle();
            tabLayout.getTabAt(0).select();

        } else if (salesPvA_SegmentClick.equals("LW")) {
            //  btn_LW.toggle();
            tabLayout.getTabAt(1).select();

        }

    }

    private void TimeUP()
    {
        if(geoLeveLDesc.equals("E ZONE")) {
            if (salesAnalysisClassArrayList.size() != 0) {
                if (focusposition < salesAnalysisClassArrayList.size() - 1 && !onItemClickFlag) {

                    if (txtheaderplanclass.getText().toString().equals("Department")) {
                        level = 1;
                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();


                    } else if (txtheaderplanclass.getText().toString().equals("Subdept")) {
                        level = 2;
                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();


                    } else if (txtheaderplanclass.getText().toString().equals("Class")) {
                        level = 3;
                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();


                    } else if (txtheaderplanclass.getText().toString().equals("Subclass")) {
                        level = 4;

                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();


                    } else if (txtheaderplanclass.getText().toString().equals("Region"))
                    {
                        level = 7;
                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();

                    } else if (txtheaderplanclass.getText().toString().equals("Store"))
                    {
                        level = 9;
                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();
                    }
                    Log.e(TAG, "pvaFirstVisibleItem: " + pvaFirstVisibleItem + "\t" + focusposition);

                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.hDialog();
                        offsetvalue = 0;
                        limit = 100;
                        count = 0;
                        if (focusposition != selFirstPositionValue) {
                            if (postRequest != null) {
                                postRequest.cancel();
                            }
                            pva_progressBar.setVisibility(View.VISIBLE);
                            if (pvaFirstVisibleItem.equals("All")) {
                                salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                                barChart.invalidate();
                                barChart.setScaleEnabled(false);
                                Log.e(TAG, "TimeUP: " + pvaFirstVisibleItem);
                                requestSalesWeekChart();

                            } else {
                                salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                                barChart.invalidate();
                                barChart.setScaleEnabled(false);
                                requestPvAChartAPI();
                                Log.e(TAG, "TimeUP: " + pvaFirstVisibleItem);
                            }
                            selFirstPositionValue = focusposition;
                        }

                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }

                } else {


                    focusposition = salesAnalysisClassArrayList.size() - 1;
                    LinearLayoutManager llm = (LinearLayoutManager) listViewSalesPvA.getLayoutManager();
                    llm.scrollToPosition(focusposition);
                    //  listViewSalesPvA.getLayoutManager().scrollToPosition(focusposition);

                    if (txtheaderplanclass.getText().toString().equals("Department")) {
                        level = 1;
                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();


                    } else if (txtheaderplanclass.getText().toString().equals("Subdept")) {
                        level = 2;
                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();


                    } else if (txtheaderplanclass.getText().toString().equals("Class")) {
                        level = 3;
                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();


                    } else if (txtheaderplanclass.getText().toString().equals("Subclass")) {
                        level = 4;

                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();


                    } else if (txtheaderplanclass.getText().toString().equals("Region"))
                    {
                        level = 7;
                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();

                    } else if (txtheaderplanclass.getText().toString().equals("Store"))
                    {
                        level = 9;
                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();
                    }
                    Log.e(TAG, "scroll up: " + focusposition + " visible item is " + pvaFirstVisibleItem);
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.hDialog();
                        offsetvalue = 0;
                        limit = 100;
                        count = 0;
                        if (focusposition != selFirstPositionValue) {
                            if (postRequest != null) {
                                postRequest.cancel();
                            }
                            pva_progressBar.setVisibility(View.VISIBLE);
                            if (pvaFirstVisibleItem.equals("All")) {

                                salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                                barChart.invalidate();
                                barChart.setScaleEnabled(false);
                                requestSalesWeekChart();

                            } else {
                                salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                                barChart.invalidate();
                                barChart.setScaleEnabled(false);
                                requestPvAChartAPI();

                            }
                            selFirstPositionValue = focusposition;

                        }

                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }
        else // FBB login
        {
            if (salesAnalysisClassArrayList.size() != 0) {
                if (focusposition < salesAnalysisClassArrayList.size() - 1 && !onItemClickFlag) {

                    if (txtheaderplanclass.getText().toString().equals("Department")) {
                        level = 1;

                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanDept();

                    } else if (txtheaderplanclass.getText().toString().equals("Category")) {
                        level = 2;

                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanCategory();

                    } else if (txtheaderplanclass.getText().toString().equals("Class")) {
                        level = 3;

                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanClass();

                    } else if (txtheaderplanclass.getText().toString().equals("Brand")) {

                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getBrandName();

                    } else if (txtheaderplanclass.getText().toString().equals("Brand Class")) {
                        level = 5;

                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getBrandplanClass();


                    }
                    Log.e(TAG, "pvaFirstVisibleItem: " + pvaFirstVisibleItem + "\t" + focusposition);

                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.hDialog();
                        offsetvalue = 0;
                        limit = 100;
                        count = 0;
                        if (focusposition != selFirstPositionValue) {
                            if (postRequest != null) {
                                postRequest.cancel();
                            }
                            pva_progressBar.setVisibility(View.VISIBLE);
                            if (pvaFirstVisibleItem.equals("All")) {
                                salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                                barChart.invalidate();
                                barChart.setScaleEnabled(false);
                                Log.e(TAG, "TimeUP: " + pvaFirstVisibleItem);
                                requestSalesWeekChart();

                            } else {
                                salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                                barChart.invalidate();
                                barChart.setScaleEnabled(false);
                                requestPvAChartAPI();
                                Log.e(TAG, "TimeUP: " + pvaFirstVisibleItem);
                            }
                            selFirstPositionValue = focusposition;
                        }

                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }

                } else {


                    focusposition = salesAnalysisClassArrayList.size() - 1;
                    LinearLayoutManager llm = (LinearLayoutManager) listViewSalesPvA.getLayoutManager();
                    llm.scrollToPosition(focusposition);
                    //  listViewSalesPvA.getLayoutManager().scrollToPosition(focusposition);


                    if (txtheaderplanclass.getText().toString().equals("Department")) {
                        level = 1;

                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanDept();

                    } else if (txtheaderplanclass.getText().toString().equals("Category")) {
                        level = 2;

                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanCategory();

                    } else if (txtheaderplanclass.getText().toString().equals("Class")) {
                        level = 3;

                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanClass();

                    } else if (txtheaderplanclass.getText().toString().equals("Brand")) {

                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getBrandName();

                    } else if (txtheaderplanclass.getText().toString().equals("Brand Class")) {
                        level = 5;

                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getBrandplanClass();


                    }
                    Log.e(TAG, "scroll up: " + focusposition + " visible item is " + pvaFirstVisibleItem);
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.hDialog();
                        offsetvalue = 0;
                        limit = 100;
                        count = 0;
                        if (focusposition != selFirstPositionValue) {
                            if (postRequest != null) {
                                postRequest.cancel();
                            }
                            pva_progressBar.setVisibility(View.VISIBLE);
                            if (pvaFirstVisibleItem.equals("All")) {

                                salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                                barChart.invalidate();
                                barChart.setScaleEnabled(false);
                                requestSalesWeekChart();

                            } else {
                                salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                                barChart.invalidate();
                                barChart.setScaleEnabled(false);
                                requestPvAChartAPI();

                            }
                            selFirstPositionValue = focusposition;

                        }

                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }
    }

    private ArrayList<Entry> setYAxisValues()
    {
        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < salesPvAAnalysisWeekArrayList.size(); i++) {

            float val = (float) salesPvAAnalysisWeekArrayList.get(i).getPvaAchieved();
            yVals.add(new Entry(i, val));
        }

        return yVals;
    }

    private ArrayList<String> setXAxisValues()
    {
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < salesPvAAnalysisWeekArrayList.size(); i++) {

            xVals.add(String.valueOf(salesPvAAnalysisWeekArrayList.get(i).getWeekNumber()));
        }

        return xVals;
    }


    // API 1.20
    private void requestSalesListDisplayAPI() {
        String salespva_listurl;
        if (geoLeveLDesc.equals("E ZONE"))
        {
            if (from_filter)
            {
                salespva_listurl = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + filterSelectedString.replace(" ", "%20") + "&geoLevel2Code="+geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit+ "&lobId="+ lobId;
            }
            else
            {
                salespva_listurl = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level +"&geoLevel2Code="+geoLevel2Code +"&offset=" + offsetvalue + "&limit=" + limit+ "&lobId="+ lobId;
            }
        }
        else
        {
            salespva_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytimeNew/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level+"&geoLevel2Code="+geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit+ "&lobId="+ lobId;
        }
        Log.e(TAG, "requestSalesListDisplayAPI: " + salespva_listurl);
        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                           Log.e("requestSalesListDisplayAPI "," "+response);
                        try {
                            int i;
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                pva_progressBar.setVisibility(View.GONE);
                                from_filter = false;
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                onItemClickFlag = false;
                            } else if (response.length() == limit) {
                                for (i = 0; i < response.length(); i++) {

                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisListDisplay);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesListDisplayAPI();

                            } else if (response.length() < limit) {
                                for (i = 0; i < response.length(); i++) {

                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisListDisplay);
                                }

                                salesAnalysisListDisplay = new SalesAnalysisListDisplay();
                                if(geoLeveLDesc.equals("E ZONE"))
                                {
                                    if (txtheaderplanclass.getText().toString().equals("Department")) {
                                        salesAnalysisListDisplay.setLevel("All");
                                        salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                        salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                        salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());

                                    } else if (txtheaderplanclass.getText().toString().equals("Subdept")) {
                                        salesAnalysisListDisplay.setLevel("All");
                                        salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                        salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                        salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());

                                    } else if (txtheaderplanclass.getText().toString().equals("Class")) {
                                        salesAnalysisListDisplay.setLevel("All");
                                        salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                        salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                        salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());

                                    } else if (txtheaderplanclass.getText().toString().equals("Subclass")) {
                                        salesAnalysisListDisplay.setLevel("All");
                                        salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                        salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                        salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());

                                    } else if (txtheaderplanclass.getText().toString().equals("Region"))
                                    {
                                        salesAnalysisListDisplay.setLevel("All");
                                        salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                        salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                        salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());
                                    }
                                    else if (txtheaderplanclass.getText().toString().equals("Store"))
                                    {
                                        salesAnalysisListDisplay.setLevel("All");
                                        salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                        salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                        salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());
                                    }
                                }
                                else // FBB login
                                {
                                    if (txtheaderplanclass.getText().toString().equals("Department")) {
                                        salesAnalysisListDisplay.setPlanDept("All");
                                        salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                        salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                        salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());

                                    } else if (txtheaderplanclass.getText().toString().equals("Category")) {
                                        salesAnalysisListDisplay.setPlanCategory("All");
                                        salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                        salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                        salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());

                                    } else if (txtheaderplanclass.getText().toString().equals("Class")) {
                                        salesAnalysisListDisplay.setPlanClass("All");
                                        salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                        salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                        salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());

                                    } else if (txtheaderplanclass.getText().toString().equals("Brand")) {
                                        salesAnalysisListDisplay.setBrandName("All");
                                        salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                        salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                        salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());

                                    } else if (txtheaderplanclass.getText().toString().equals("Brand Class")) {
                                        salesAnalysisListDisplay.setBrandplanClass("All");
                                        salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                        salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                        salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());
                                    }
                                }
                                salesAnalysisClassArrayList.add(0, salesAnalysisListDisplay);
                                listViewSalesPvA.setLayoutManager(new LinearLayoutManager(context));

                                listViewSalesPvA.setLayoutManager(new LinearLayoutManager(
                                        listViewSalesPvA.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                listViewSalesPvA.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(listViewSalesPvA);

                                salesPvAAdapter = new PvASnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listViewSalesPvA, geoLeveLDesc);
                                listViewSalesPvA.setAdapter(salesPvAAdapter);


                                // Retain values....
                                if(geoLeveLDesc.equals("E ZONE"))
                                {
                                    if (txtheaderplanclass.getText().toString().equals("Department")) {
                                        for (int j = 0; j < salesAnalysisClassArrayList.size(); j++) {
                                            level = 1;
                                            pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();
                                            if (salesAnalysisClassArrayList.get(focusposition).getLevel().equals(pvaFirstVisibleItem)) {
                                                listViewSalesPvA.getLayoutManager().scrollToPosition(focusposition);
                                            }

                                        }
                                    } else if (txtheaderplanclass.getText().toString().equals("Subdept")) {
                                        for (int j = 0; j < salesAnalysisClassArrayList.size(); j++) {
                                            level = 2;
                                            pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();
                                            if (salesAnalysisClassArrayList.get(focusposition).getLevel().equals(pvaFirstVisibleItem)) {
                                                listViewSalesPvA.getLayoutManager().scrollToPosition(focusposition);
                                            }

                                        }
                                    } else if (txtheaderplanclass.getText().toString().equals("Class")) {
                                        for (int j = 0; j < salesAnalysisClassArrayList.size(); j++) {
                                            level = 3;
                                            pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();
                                            if (salesAnalysisClassArrayList.get(focusposition).getLevel().equals(pvaFirstVisibleItem)) {
                                                listViewSalesPvA.getLayoutManager().scrollToPosition(focusposition);
                                            }

                                        }
                                    } else if (txtheaderplanclass.getText().toString().equals("Subclass")) {
                                        for (int j = 0; j < salesAnalysisClassArrayList.size(); j++) {
                                            level = 4;
                                            pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();
                                            if (salesAnalysisClassArrayList.get(focusposition).getLevel().equals(pvaFirstVisibleItem)) {
                                                listViewSalesPvA.getLayoutManager().scrollToPosition(focusposition);
                                            }
                                        }
                                    }
                                    else if (txtheaderplanclass.getText().toString().equals("Region")) {
                                        for (int j = 0; j < salesAnalysisClassArrayList.size(); j++) {
                                            level = 7;
                                            pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();
                                            if (salesAnalysisClassArrayList.get(focusposition).getLevel().equals(pvaFirstVisibleItem)) {
                                                listViewSalesPvA.getLayoutManager().scrollToPosition(focusposition);

                                            }
                                        }
                                    } else if (txtheaderplanclass.getText().toString().equals("Store")) {
                                        for (int j = 0; j < salesAnalysisClassArrayList.size(); j++) {
                                            level = 9;
                                            pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();
                                            if (salesAnalysisClassArrayList.get(focusposition).getLevel().equals(pvaFirstVisibleItem)) {
                                                listViewSalesPvA.getLayoutManager().scrollToPosition(focusposition);

                                            }
                                        }
                                    }
                                }
                                // FBB login
                                else {
                                    if (txtheaderplanclass.getText().toString().equals("Department")) {
                                        for (int j = 0; j < salesAnalysisClassArrayList.size(); j++) {
                                            level = 1;

                                            pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanDept();
                                            if (salesAnalysisClassArrayList.get(focusposition).getPlanDept().equals(pvaFirstVisibleItem)) {
                                                listViewSalesPvA.getLayoutManager().scrollToPosition(focusposition);
                                            }
                                        }

                                    } else if (txtheaderplanclass.getText().toString().equals("Category")) {
                                        for (int j = 0; j < salesAnalysisClassArrayList.size(); j++) {
                                            level = 2;

                                            pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanCategory();
                                            if (salesAnalysisClassArrayList.get(focusposition).getPlanCategory().equals(pvaFirstVisibleItem)) {
                                                listViewSalesPvA.getLayoutManager().scrollToPosition(focusposition);
                                            }

                                        }
                                    } else if (txtheaderplanclass.getText().toString().equals("Class")) {
                                        for (int j = 0; j < salesAnalysisClassArrayList.size(); j++) {
                                            level = 3;

                                            pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanClass();
                                            if (salesAnalysisClassArrayList.get(focusposition).getPlanClass().equals(pvaFirstVisibleItem)) {
                                                listViewSalesPvA.getLayoutManager().scrollToPosition(focusposition);
                                            }
                                        }

                                    } else if (txtheaderplanclass.getText().toString().equals("Brand")) {
                                        for (int j = 0; j < salesAnalysisClassArrayList.size(); j++) {
                                            level = 4;

                                            pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getBrandName();
                                            if (salesAnalysisClassArrayList.get(focusposition).getBrandName().equals(pvaFirstVisibleItem)) {
                                                listViewSalesPvA.getLayoutManager().scrollToPosition(focusposition);
                                            }

                                        }
                                    } else if (txtheaderplanclass.getText().toString().equals("Brand Class")) {
                                        for (int j = 0; j < salesAnalysisClassArrayList.size(); j++) {
                                            level = 5;

                                            pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getBrandplanClass();
                                            if (salesAnalysisClassArrayList.get(focusposition).getBrandplanClass().equals(pvaFirstVisibleItem)) {
                                                listViewSalesPvA.getLayoutManager().scrollToPosition(focusposition);
                                            }
                                        }
                                    }
                                }// end of else

                                if (pvaFirstVisibleItem.equals("All")) {
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    llpvahierarchy.setVisibility(View.GONE);
                                    salesPvAAnalysisWeekArrayList.clear();
                                    requestSalesWeekChart();

                                } else {
                                    llpvahierarchy.setVisibility(View.GONE);
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    salesPvAAnalysisWeekArrayList.clear();
                                    requestPvAChartAPI();
                                }
                                from_filter = false;

                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            pva_progressBar.setVisibility(View.GONE);
                            onItemClickFlag = false;
                            from_filter = false;
                            Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        pva_progressBar.setVisibility(View.GONE);
                        onItemClickFlag = false;
                        from_filter = false;
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
    }


    // this method is used to create data for Bar graph<br for on scroll />
    private void requestPvAChartAPI() {
        if (pvaFirstVisibleItem.equals("All")) {
            requestSalesWeekChart();
            return;
        }
        String url = "";
        pvaFirstVisibleItem = pvaFirstVisibleItem.replace("%", "%25");
        pvaFirstVisibleItem = pvaFirstVisibleItem.replace(" ", "%20").replace("&", "%26");

        if(geoLeveLDesc.equals("E ZONE"))
        {
            if (txtheaderplanclass.getText().toString().equals("Department"))
            {
                url = ConstsCore.web_url + "/v1/display/salesanalysisPVA13WeekgraphEZNew/" + userId + "?department=" + pvaFirstVisibleItem + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&view=" + salesPvA_SegmentClick+ "&lobId="+ lobId;

            } else if (txtheaderplanclass.getText().toString().equals("Subdept"))
            {
                url = ConstsCore.web_url + "/v1/display/salesanalysisPVA13WeekgraphEZNew/" + userId + "?category=" + pvaFirstVisibleItem + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&view=" + salesPvA_SegmentClick+ "&lobId="+ lobId;

            }
            else if (txtheaderplanclass.getText().toString().equals("Class"))
            {
                url = ConstsCore.web_url + "/v1/display/salesanalysisPVA13WeekgraphEZNew/" + userId + "?class=" + pvaFirstVisibleItem + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&view=" + salesPvA_SegmentClick+ "&lobId="+ lobId;

            } else if (txtheaderplanclass.getText().toString().equals("Subclass"))
            {
                url = ConstsCore.web_url + "/v1/display/salesanalysisPVA13WeekgraphEZNew/" + userId + "?brand=" + pvaFirstVisibleItem + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&view=" + salesPvA_SegmentClick+ "&lobId="+ lobId;

            } else if (txtheaderplanclass.getText().toString().equals("Region")) {

                url = ConstsCore.web_url + "/v1/display/salesanalysisPVA13WeekgraphEZNew/" + userId + "?region=" + pvaFirstVisibleItem + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&view=" + salesPvA_SegmentClick+ "&lobId="+ lobId;

            } else if (txtheaderplanclass.getText().toString().equals("Store")) {

                url = ConstsCore.web_url + "/v1/display/salesanalysisPVA13WeekgraphEZNew/" + userId + "?store=" + pvaFirstVisibleItem + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&view=" + salesPvA_SegmentClick+ "&lobId="+ lobId;

            }
        }
        else  // FBB login
        {
            if (txtheaderplanclass.getText().toString().equals("Department")) {

                url = ConstsCore.web_url + "/v1/display/salesvisualpvaanalysisbyweekNew/" + userId + "?department=" + pvaFirstVisibleItem +"&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&view=" + salesPvA_SegmentClick+ "&lobId="+ lobId;

            } else if (txtheaderplanclass.getText().toString().equals("Category")) {

                url = ConstsCore.web_url + "/v1/display/salesvisualpvaanalysisbyweekNew/" + userId + "?category=" + pvaFirstVisibleItem +"&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&view=" + salesPvA_SegmentClick+ "&lobId="+ lobId;

            } else if (txtheaderplanclass.getText().toString().equals("Class")) {

                url = ConstsCore.web_url + "/v1/display/salesvisualpvaanalysisbyweekNew/" + userId + "?class=" + pvaFirstVisibleItem +"&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&view=" + salesPvA_SegmentClick+ "&lobId="+ lobId;

            } else if (txtheaderplanclass.getText().toString().equals("Brand")) {

                url = ConstsCore.web_url + "/v1/display/salesvisualpvaanalysisbyweekNew/" + userId + "?brand=" + pvaFirstVisibleItem +"&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&view=" + salesPvA_SegmentClick+ "&lobId="+ lobId;

            } else if (txtheaderplanclass.getText().toString().equals("Brand Class")) {

                url = ConstsCore.web_url + "/v1/display/salesvisualpvaanalysisbyweekNew/" + userId + "?brandclass=" + pvaFirstVisibleItem +"&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&view=" + salesPvA_SegmentClick+ "&lobId="+ lobId;

            }
        }
        Log.e(TAG, "requestPvAChartAPI: " + url);
        postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Log.e(TAG, "onResponse: "+response);
                        Log.e("requestPvAChartAPI "," "+response);
                        try {

                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                barChart.setData(null);
                                barChart.notifyDataSetChanged();
                                barChart.invalidate();
                                pva_progressBar.setVisibility(View.GONE);
                                onItemClickFlag = false;
                                // Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    salesPvAAnalysisWeek = gson.fromJson(response.get(i).toString(), SalesPvAAnalysisWeek.class);
                                    salesPvAAnalysisWeekArrayList.add(salesPvAAnalysisWeek);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestPvAChartAPI();
                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    salesPvAAnalysisWeek = gson.fromJson(response.get(i).toString(), SalesPvAAnalysisWeek.class);
                                    salesPvAAnalysisWeekArrayList.add(salesPvAAnalysisWeek);
                                }
                                multidatasetBarGraph(salesPvAAnalysisWeekArrayList);
                                onItemClickFlag = false;
                                pva_progressBar.setVisibility(View.GONE);
                                Reusable_Functions.hDialog();
                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            onItemClickFlag = false;
                            pva_progressBar.setVisibility(View.GONE);
                            Log.e(TAG, "catch: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        onItemClickFlag = false;
                        pva_progressBar.setVisibility(View.GONE);
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
    }

    private void requestSalesWeekChart() {
        String salespvaweekChart_url = "";
        if (geoLeveLDesc.equals("E ZONE")) {
            //https://smdm.manthan.com/v1/display/salesanalysisPVA13WeekgraphEZ/1234
            salespvaweekChart_url = ConstsCore.web_url + "/v1/display/salesanalysisPVA13WeekgraphEZNew/" + userId + "?view=" + salesPvA_SegmentClick + "&geoLevel2Code=" + geoLevel2Code + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit+ "&lobId="+ lobId;
        } else {
            salespvaweekChart_url = ConstsCore.web_url + "/v1/display/salesvisualpvaanalysisbyweekNew/" + userId + "?view=" + salesPvA_SegmentClick + "&geoLevel2Code=" + geoLevel2Code + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit+ "&lobId="+ lobId;
        }

        Log.e(TAG, "requestSalesWeekChart: " + salespvaweekChart_url);
        postRequest = new JsonArrayRequest(Request.Method.GET, salespvaweekChart_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, "requestSalesWeekChart: " + response);
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                barChart.setData(null);
                                barChart.notifyDataSetChanged();
                                barChart.invalidate();

                                pva_progressBar.setVisibility(View.GONE);
                                onItemClickFlag = false;
                                //  Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesPvAAnalysisWeek = gson.fromJson(response.get(i).toString(), SalesPvAAnalysisWeek.class);
                                    salesPvAAnalysisWeekArrayList.add(salesPvAAnalysisWeek);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesWeekChart();
                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    salesPvAAnalysisWeek = gson.fromJson(response.get(i).toString(), SalesPvAAnalysisWeek.class);
                                    salesPvAAnalysisWeekArrayList.add(salesPvAAnalysisWeek);
                                }

                                multidatasetBarGraph(salesPvAAnalysisWeekArrayList);
                                onItemClickFlag = false;
                                Reusable_Functions.hDialog();
                                pva_progressBar.setVisibility(View.GONE);

                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            onItemClickFlag = false;
                            pva_progressBar.setVisibility(View.GONE);
                            Log.e(TAG, "catch error: " + e.getMessage());
                            Toast.makeText(context, "data failed...", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        onItemClickFlag = false;
                        pva_progressBar.setVisibility(View.GONE);
                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }
        )

        {
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

    public void multidatasetBarGraph(ArrayList<SalesPvAAnalysisWeek> salesPvAAnalysisWeekArrayList) {

        try {
            if (salesPvAAnalysisWeekArrayList != null & salesPvAAnalysisWeekArrayList.size() > 0) {
                barChart.setDrawBarShadow(false);
                barChart.setDrawValueAboveBar(true);
                barChart.setMaxVisibleValueCount(50);
                barChart.setPinchZoom(false);
                barChart.setDrawGridBackground(false);
                barChart.getDescription().setEnabled(false);

                MyMarkerView mv = new MyMarkerView(context, R.layout.custom_marker_view);
                mv.setChartView(barChart); // For bounds control
                barChart.setMarker(mv); // Set the marker to the chart

                XAxis xl = barChart.getXAxis();
                xl.setGranularity(1f);
                xl.setCenterAxisLabels(true);

                YAxis leftAxis = barChart.getAxisLeft();
                leftAxis.setGranularity(1f);
                leftAxis.setDrawGridLines(false);
                leftAxis.setSpaceTop(30f);
                leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true
                barChart.getAxisRight().setEnabled(false);

                //data
                float groupSpace = 0.04f;
                float barSpace = 0.02f; // x2 dataset
                float barWidth = 0.46f; // x2 dataset
                // (0.46 + 0.02) * 2 + 0.04 = 1.00 -> interval per "group"

                List<BarEntry> yVals1 = new ArrayList<BarEntry>();
                List<BarEntry> yVals2 = new ArrayList<BarEntry>();
                String[] labels = new String[salesPvAAnalysisWeekArrayList.size()];

                for (int i = 0; i < salesPvAAnalysisWeekArrayList.size(); i++) {
                    yVals1.add(new BarEntry(i, (float) salesPvAAnalysisWeekArrayList.get(i).getPlanSaleNetVal()));
                    yVals2.add(new BarEntry(i, (float) salesPvAAnalysisWeekArrayList.get(i).getSaleNetVal()));
                    labels[i] = salesPvAAnalysisWeekArrayList.get(i).getWeekNumber();

                }

                BarDataSet set1, set2;

                if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
                    set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
                    set2 = (BarDataSet) barChart.getData().getDataSetByIndex(1);
                    set1.setValues(yVals1);
                    set2.setValues(yVals2);
                    barChart.getData().notifyDataChanged();
                    barChart.notifyDataSetChanged();
                } else {
                    // create 2 datasets with different types
                    set1 = new BarDataSet(yVals1, "Plan Sales");
                    set1.setColor(Color.parseColor("#20b5d3"));
                    set2 = new BarDataSet(yVals2, "Net Sales");
                    set2.setColor(Color.parseColor("#21d24c"));

                    ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                    dataSets.add(set1);
                    dataSets.add(set2);

                    BarData data = new BarData(dataSets);
                    data.setDrawValues(false);  //remove for number on bar
                    barChart.setData(data);
                }

                barChart.getXAxis().setDrawLabels(true);
                barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
                barChart.getBarData().setBarWidth(barWidth);
                barChart.getXAxis().setAxisMinValue(0);
                barChart.getXAxis().setAxisMaximum(salesPvAAnalysisWeekArrayList.size());
                barChart.groupBars(0, groupSpace, barSpace);
                //  Legend l = barChart.getLegend();
                //  l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

                barChart.setFitBars(true);
                barChart.invalidate();
                barChart.animateXY(3000, 3000);
            } else {
                barChart.clear();

            }
        } catch (Exception e) {
            barChart.clear();

        }

    }


    // drill down level API
    private void requestSalesPvACategoryList(final String deptName) {

        String salespvacategory_listurl;
        if (geoLeveLDesc.equals("E ZONE")) {
            salespvacategory_listurl = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&department=" + deptName.replaceAll(" ", "%20").replaceAll("&", "%26") +"&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit+ "&lobId="+ lobId;

        } else {
            salespvacategory_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytimeNew/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&department=" + deptName.replaceAll(" ", "%20").replaceAll("&", "%26") + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit+ "&lobId="+ lobId;
        }
        Log.e(TAG, "requestSalesPvACategoryList: " + salespvacategory_listurl);

        postRequest = new JsonArrayRequest(Request.Method.GET, salespvacategory_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, "requestSalesPvACategoryList: " + response);
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                pva_progressBar.setVisibility(View.GONE);
                                onItemClickFlag = false;
                                Toast.makeText(context, "No Category found", Toast.LENGTH_SHORT).show();

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisListDisplay);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesPvACategoryList(deptName);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisListDisplay);
                                }
                                salesAnalysisListDisplay = new SalesAnalysisListDisplay();
                                if(geoLeveLDesc.equals("E ZONE"))
                                {
                                    if (txtheaderplanclass.getText().toString().equals("Subdept")) {

                                        salesAnalysisListDisplay.setLevel("All");

                                    }
                                }
                                else
                                {
                                    if (txtheaderplanclass.getText().toString().equals("Category")) {

                                        salesAnalysisListDisplay.setPlanCategory("All");

                                    }
                                }

                                salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());

                                salesAnalysisClassArrayList.add(0, salesAnalysisListDisplay);

                                salesPvAAdapter = new PvASnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listViewSalesPvA, geoLeveLDesc);
                                listViewSalesPvA.setAdapter(salesPvAAdapter);
                                salesPvAAdapter.notifyDataSetChanged();

                                pvaVal = " ";
                                pvaVal = deptName;
                                txtpvahDeptName.setText(pvaVal);
                                llpvahierarchy.setVisibility(View.VISIBLE);
                                salesPvAAnalysisWeekArrayList.clear();
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                if (geoLeveLDesc.equals("E ZONE")) {
                                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(0).getLevel();

                                } else {
                                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(0).getPlanCategory();
                                }
                                salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                                requestPvAChartAPI();
                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            onItemClickFlag = false;
                            pva_progressBar.setVisibility(View.GONE);
                            Toast.makeText(context, "No Category found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        onItemClickFlag = false;
                        pva_progressBar.setVisibility(View.GONE);
                        Toast.makeText(context, "No Category found", Toast.LENGTH_SHORT).show();
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

    private void requestSalesPvAPlanClassListAPI(final String category) {
        String salespva_planclass_listurl;
        if (geoLeveLDesc.equals("E ZONE")) {
            salespva_planclass_listurl = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&category=" + category.replaceAll(" ", "%20").replaceAll("&", "%26") + "&geoLevel2Code=" + geoLevel2Code + "&offset=" +  offsetvalue + "&limit=" + limit+ "&lobId="+ lobId;

        } else {
            salespva_planclass_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytimeNew/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&category=" + category.replaceAll(" ", "%20").replaceAll("&", "%26") + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit+ "&lobId="+ lobId;
        }
        Log.e(TAG, "requestSalesPvAPlanClassListAPI: " + salespva_planclass_listurl);
        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_planclass_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, "requestSalesPvAPlanClassListAPI: " + response);

                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                pva_progressBar.setVisibility(View.GONE);
                                onItemClickFlag = false;
                                Toast.makeText(context, "No Class data found", Toast.LENGTH_SHORT).show();
                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisListDisplay);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesPvAPlanClassListAPI(category);
                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisListDisplay);
                                }

                                salesAnalysisListDisplay = new SalesAnalysisListDisplay();
                                if (txtheaderplanclass.getText().toString().equals("Class")) {
                                    if (geoLeveLDesc.equals("E ZONE")) {
                                        salesAnalysisListDisplay.setLevel("All");
                                    } else {
                                        salesAnalysisListDisplay.setPlanClass("All");
                                    }
                                }
                                salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());

                                salesAnalysisClassArrayList.add(0, salesAnalysisListDisplay);
                                salesPvAAdapter = new PvASnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listViewSalesPvA, geoLeveLDesc);
                                listViewSalesPvA.setAdapter(salesPvAAdapter);
                                salesPvAAdapter.notifyDataSetChanged();
                                pvaVal += " > " + category;
                                txtpvahDeptName.setText(pvaVal);
                                llpvahierarchy.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                salesPvAAnalysisWeekArrayList.clear();
                                if (geoLeveLDesc.equals("E ZONE")) {
                                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(0).getLevel();

                                } else {
                                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(0).getPlanClass();
                                }
                                salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();

                                requestPvAChartAPI();
                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            onItemClickFlag = false;
                            pva_progressBar.setVisibility(View.GONE);
                            Toast.makeText(context, "No Class data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        pva_progressBar.setVisibility(View.GONE);
                        onItemClickFlag = false;
                        Toast.makeText(context, "No Class data found", Toast.LENGTH_SHORT).show();
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


    private void requestProductAndLocation(final String region) {
        String salespva_planclass_listurl;
        salespva_planclass_listurl = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&region=" + region.replaceAll(" ", "%20").replaceAll("&", "%26") + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit+ "&lobId="+ lobId;
        Log.e(TAG, "requestSalesPvAPlanClassListAPI: " + salespva_planclass_listurl);
        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_planclass_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, "requestSalesPvAPlanClassListAPI: " + response);

                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                pva_progressBar.setVisibility(View.GONE);
                                onItemClickFlag = false;
                                Toast.makeText(context, "No Class data found", Toast.LENGTH_SHORT).show();
                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisListDisplay);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesPvAPlanClassListAPI(region);
                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisListDisplay);
                                }
                                salesAnalysisListDisplay = new SalesAnalysisListDisplay();
                                if (txtheaderplanclass.getText().toString().equals("Store")) {
                                    salesAnalysisListDisplay.setLevel("All");

                                }
                                salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());

                                salesAnalysisClassArrayList.add(0, salesAnalysisListDisplay);
                                salesPvAAdapter = new PvASnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listViewSalesPvA, geoLeveLDesc);
                                listViewSalesPvA.setAdapter(salesPvAAdapter);
                                salesPvAAdapter.notifyDataSetChanged();
                                pvaVal = " ";
                                pvaVal = region;
                                txtpvahDeptName.setText(pvaVal);
                                llpvahierarchy.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                salesPvAAnalysisWeekArrayList.clear();
                                pvaFirstVisibleItem = salesAnalysisClassArrayList.get(0).getLevel();
                                salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                                requestPvAChartAPI();
                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            onItemClickFlag = false;
                            pva_progressBar.setVisibility(View.GONE);
                            Toast.makeText(context, "No Class data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        pva_progressBar.setVisibility(View.GONE);
                        onItemClickFlag = false;
                        Toast.makeText(context, "No Class data found", Toast.LENGTH_SHORT).show();
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

    private void requestSalesPvABrandListAPI(final String planclass) {
        String salespva_brand_listurl;
        if (geoLeveLDesc.equals("E ZONE")) {
            salespva_brand_listurl = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&class=" + planclass.replaceAll(" ", "%20").replaceAll("&", "%26") + "&geoLevel2Code=" + geoLevel2Code +  "&offset=" + offsetvalue + "&limit=" + limit+ "&lobId="+ lobId;

        } else {
            salespva_brand_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytimeNew/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&class=" + planclass.replaceAll(" ", "%20").replaceAll("&", "%26") + "&geoLevel2Code=" + geoLevel2Code +  "&offset=" + offsetvalue + "&limit=" + limit+ "&lobId="+ lobId;
        }
        Log.e(TAG, "requestSalesPvAPlanClassListAPI: " + salespva_brand_listurl);

        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_brand_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, "requestSalesPvAPlanClassListAPI: " + response);

                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                pva_progressBar.setVisibility(View.GONE);
                                onItemClickFlag = false;
                                Toast.makeText(context, "No Brand data found", Toast.LENGTH_SHORT).show();

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisListDisplay);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesPvABrandListAPI(planclass);
                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisListDisplay);
                                }
                                salesAnalysisListDisplay = new SalesAnalysisListDisplay();
                                if(geoLeveLDesc.equals("E ZONE"))
                                {
                                    if (txtheaderplanclass.getText().toString().equals("Subclass")) {
                                        salesAnalysisListDisplay.setLevel("All");

                                    }
                                }
                                else
                                {
                                    if (txtheaderplanclass.getText().toString().equals("Brand")) {

                                        salesAnalysisListDisplay.setBrandName("All");

                                    }
                                }

                                salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());

                                salesAnalysisClassArrayList.add(0, salesAnalysisListDisplay);
                                salesPvAAdapter = new PvASnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listViewSalesPvA, geoLeveLDesc);
                                listViewSalesPvA.setAdapter(salesPvAAdapter);
                                salesPvAAdapter.notifyDataSetChanged();
                                pvaVal += " > " + planclass;
                                txtpvahDeptName.setText(pvaVal);
                                llpvahierarchy.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                salesPvAAnalysisWeekArrayList.clear();
                                if (geoLeveLDesc.equals("E ZONE")) {
                                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(0).getLevel();

                                } else {
                                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(0).getBrandName();
                                }
                                salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                                requestPvAChartAPI();
                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            pva_progressBar.setVisibility(View.GONE);
                            onItemClickFlag = false;
                            Toast.makeText(context, "No Brand data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        pva_progressBar.setVisibility(View.GONE);
                        onItemClickFlag = false;
                        Toast.makeText(context, "No Brand data found", Toast.LENGTH_SHORT).show();
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

    private void requestSalesPvABrandPlanListAPI(final String brandnm) {

        String salespva_brandplan_listurl;
        if (geoLeveLDesc.equals("E ZONE")) {
            salespva_brandplan_listurl = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&brand=" + brandnm.replaceAll(" ", "%20").replaceAll("&", "%26") + "&geoLevel2Code=" + geoLevel2Code +  "&offset=" + offsetvalue + "&limit=" + limit+ "&lobId="+ lobId;

        } else {
            salespva_brandplan_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytimeNew/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&brand=" + brandnm.replaceAll(" ", "%20").replaceAll("&", "%26") + "&geoLevel2Code=" + geoLevel2Code +  "&offset=" + offsetvalue + "&limit=" + limit+ "&lobId="+ lobId;
        }
        Log.e(TAG, "requestSalesPvAPlanClassListAPI: " + salespva_brandplan_listurl);

        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_brandplan_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, "requestSalesPvAPlanClassListAPI: " + response);

                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                pva_progressBar.setVisibility(View.GONE);
                                onItemClickFlag = false;
                                Toast.makeText(context, "No Brand Class data found", Toast.LENGTH_SHORT).show();

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisListDisplay);

                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesPvABrandPlanListAPI(brandnm);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisListDisplay);
                                }

                                salesAnalysisListDisplay = new SalesAnalysisListDisplay();
                                if (txtheaderplanclass.getText().toString().equals("Brand Class")) {
                                    if (geoLeveLDesc.equals("E ZONE")) {
                                        salesAnalysisListDisplay.setLevel("All");
                                    } else {
                                        salesAnalysisListDisplay.setBrandplanClass("All");
                                    }
                                }
                                salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());

                                salesAnalysisClassArrayList.add(0, salesAnalysisListDisplay);

                                salesPvAAdapter = new PvASnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listViewSalesPvA, geoLeveLDesc);
                                listViewSalesPvA.setAdapter(salesPvAAdapter);
                                salesPvAAdapter.notifyDataSetChanged();
                                pvaVal += " > " + brandnm;
                                txtpvahDeptName.setText(pvaVal);
                                llpvahierarchy.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                salesPvAAnalysisWeekArrayList.clear();
                                if (geoLeveLDesc.equals("E ZONE")) {
                                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(0).getLevel();

                                } else {
                                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(0).getBrandplanClass();
                                }
                                salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                                requestPvAChartAPI();
                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            pva_progressBar.setVisibility(View.GONE);
                            onItemClickFlag = false;
                            Toast.makeText(context, "No Brand Class data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        pva_progressBar.setVisibility(View.GONE);
                        onItemClickFlag = false;
                        Toast.makeText(context, "No Brand Class data found", Toast.LENGTH_SHORT).show();
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

    // API 1.19 for add values for All
    private void requestSalesViewPagerValueAPI() {

        String url;
        if (geoLeveLDesc.equals("E ZONE")) {
            url = ConstsCore.web_url + "/v1/display/salesheaderEZNew/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&geoLevel2Code=" + geoLevel2Code +  "&offset=" + offsetvalue + "&limit=" + limit+ "&lobId="+ lobId;
        } else {

            url = ConstsCore.web_url + "/v1/display/salesanalysisbytimeNew/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&geoLevel2Code=" + geoLevel2Code +  "&offset=" + offsetvalue + "&limit=" + limit+ "&lobId="+ lobId;
        }
        Log.e(TAG, "requestSalesViewPagerValueAPI: " + url);
        postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, "requestSalesViewPagerValueAPI: " + response);

                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                pva_progressBar.setVisibility(View.GONE);
                                onItemClickFlag = false;
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisViewPagerValue = gson.fromJson(response.get(i).toString(), SalesAnalysisViewPagerValue.class);
                                    arrayList.add(salesAnalysisViewPagerValue);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesViewPagerValueAPI();
                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisViewPagerValue = gson.fromJson(response.get(i).toString(), SalesAnalysisViewPagerValue.class);
                                arrayList.add(salesAnalysisViewPagerValue);
                            }
                            onItemClickFlag = false;
                            }
                            requestSalesListDisplayAPI();

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            pva_progressBar.setVisibility(View.GONE);
                            onItemClickFlag = false;
                            Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        pva_progressBar.setVisibility(View.GONE);
                        onItemClickFlag = false;
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

    }

    private void requestSalesSelectedFilterVal(final String selectedString) {
        String salespva_brandplan_listurl;

        level = SalesFilterActivity.level_filter;
        salespva_brandplan_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytimeNew/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + SalesFilterActivity.level_filter + selectedString.replace(" ", "%20") + "&geoLevel2Code=" + geoLevel2Code +  "&offset=" + offsetvalue + "&limit=" + limit+ "&lobId="+ lobId;

        Log.e(TAG, "requestSalesSelectedFilterVal: " + salespva_brandplan_listurl);
        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_brandplan_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        Log.e(TAG, "requestSalesSelectedFilterVal: " + response);

                        if(geoLeveLDesc.equals("E ZONE"))
                        {
                            if (level == 2) {
                                txtheaderplanclass.setText("Subdept");
                                fromWhere = "Subdept";
                                btnSalesPrev.setVisibility(View.VISIBLE);

                            } else if (level == 3) {
                                txtheaderplanclass.setText("Class");
                                fromWhere = "Class";
                                btnSalesPrev.setVisibility(View.VISIBLE);
                            } else if (level == 4) {
                                txtheaderplanclass.setText("Subclass");
                                fromWhere = "Subclass";
                                btnSalesPrev.setVisibility(View.VISIBLE);
                                btnSalesNext.setVisibility(View.INVISIBLE);

                            } else if (level == 9) {
                                txtheaderplanclass.setText("Store");
                                fromWhere = "Store";
                                btnSalesPrev.setVisibility(View.VISIBLE);
                                btnSalesNext.setVisibility(View.INVISIBLE);

                            }
                        }
                        else // FBB login
                        {
                            if (level == 2)
                            {
                                txtheaderplanclass.setText("Category");
                                fromWhere = "Category";
                                btnSalesPrev.setVisibility(View.VISIBLE);

                            } else if (level == 3) {
                                txtheaderplanclass.setText("Class");
                                fromWhere = "Class";
                                btnSalesPrev.setVisibility(View.VISIBLE);
                            } else if (level == 4) {
                                txtheaderplanclass.setText("Brand");
                                fromWhere = "Brand";
                                btnSalesPrev.setVisibility(View.VISIBLE);
                            } else if (level == 5) {
                                txtheaderplanclass.setText("Brand Class");
                                fromWhere = "Brand Class";
                                btnSalesPrev.setVisibility(View.VISIBLE);
                                btnSalesNext.setVisibility(View.INVISIBLE);
                            } else if (level == 6) {
                                txtheaderplanclass.setText("Brand Class");
                                fromWhere = "Brand Class";
                                btnSalesPrev.setVisibility(View.VISIBLE);
                                btnSalesNext.setVisibility(View.INVISIBLE);
                            }
                        }


                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {

                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                pva_progressBar.setVisibility(View.GONE);
                                onItemClickFlag = false;

                            } else if (response.length() == limit) {
                                Log.e(TAG, "onResponse: " + response);
                                for (int i = 0; i < response.length(); i++) {
                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisListDisplay);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesSelectedFilterVal(selectedString);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisListDisplay);
                                }

                                // For Add "All"
                                salesAnalysisListDisplay = new SalesAnalysisListDisplay();
                                if(geoLeveLDesc.equals("E ZONE"))
                                {
                                    if (txtheaderplanclass.getText().toString().equals("Department")) {
                                        salesAnalysisListDisplay.setLevel("All");

                                    } else if (txtheaderplanclass.getText().toString().equals("Subdept"))
                                    {
                                        salesAnalysisListDisplay.setLevel("All");

                                    } else if (txtheaderplanclass.getText().toString().equals("Class")) {
                                        salesAnalysisListDisplay.setLevel("All");


                                    } else if (txtheaderplanclass.getText().toString().equals("Subclass"))
                                    {
                                        salesAnalysisListDisplay.setLevel("All");

                                    } else if (txtheaderplanclass.getText().toString().equals("Region"))
                                    {
                                        salesAnalysisListDisplay.setLevel("All");
                                    }
                                    else if (txtheaderplanclass.getText().toString().equals("Store")) {
                                        salesAnalysisListDisplay.setLevel("All");
                                    }
                                }
                                else // FBB login
                                {
                                    if (txtheaderplanclass.getText().toString().equals("Department")) {

                                        salesAnalysisListDisplay.setPlanDept("All");

                                    } else if (txtheaderplanclass.getText().toString().equals("Category")) {

                                        salesAnalysisListDisplay.setPlanCategory("All");

                                    } else if (txtheaderplanclass.getText().toString().equals("Class")) {

                                        salesAnalysisListDisplay.setPlanClass("All");


                                    } else if (txtheaderplanclass.getText().toString().equals("Brand")) {

                                        salesAnalysisListDisplay.setBrandName("All");

                                    } else if (txtheaderplanclass.getText().toString().equals("Brand Class")) {

                                        salesAnalysisListDisplay.setBrandplanClass("All");

                                    }
                                }

                                salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());

                                salesAnalysisClassArrayList.add(0, salesAnalysisListDisplay);
                                listViewSalesPvA.setLayoutManager(new LinearLayoutManager(
                                        listViewSalesPvA.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                listViewSalesPvA.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(listViewSalesPvA);

                                salesPvAAdapter = new PvASnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listViewSalesPvA, geoLeveLDesc);
                                listViewSalesPvA.setAdapter(salesPvAAdapter);
                                salesPvAAdapter.notifyDataSetChanged();
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                if(geoLeveLDesc.equals("E ZONE"))
                                {
                                    if (txtheaderplanclass.getText().toString().equals("Department")) {
                                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();

                                    } else if (txtheaderplanclass.getText().toString().equals("Subdept")) {
                                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();

                                    } else if (txtheaderplanclass.getText().toString().equals("Class")) {
                                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();

                                    } else if (txtheaderplanclass.getText().toString().equals("Subclass")) {

                                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();

                                    }  else if (txtheaderplanclass.getText().toString().equals("Store")) {
                                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();

                                    }
                                    else // FBB login
                                    {
                                        if (txtheaderplanclass.getText().toString().equals("Department")) {

                                            pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanDept();

                                        } else if (txtheaderplanclass.getText().toString().equals("Category")) {

                                            pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanCategory();

                                        } else if (txtheaderplanclass.getText().toString().equals("Class")) {

                                            pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanClass();

                                        } else if (txtheaderplanclass.getText().toString().equals("Brand")) {

                                            pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getBrandName();

                                        } else if (txtheaderplanclass.getText().toString().equals("Brand Class")) {

                                            pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getBrandplanClass();

                                        }
                                    }

                                }

                                salesPvAAnalysisWeekArrayList.clear();
                                requestPvAChartAPI();
                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                            pva_progressBar.setVisibility(View.GONE);
                            onItemClickFlag = false;
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                        pva_progressBar.setVisibility(View.GONE);
                        onItemClickFlag = false;
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
    public void onBackPressed() {
        salesPvA_SegmentClick = null;
        level = 0;
        salesPvA_SegmentClick = "WTD";
        level = 1;
        this.finish();
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Log.e("TAG", "onTabSelected: " + tab.getPosition() + filter_toggleClick);
        int checkedId = tab.getPosition();

        switch (checkedId) {

            case 0:
                if (salesPvA_SegmentClick.equals("WTD"))
                    break;
                salesPvA_SegmentClick = "WTD";
                pvaVal = " ";
                llpvahierarchy.setVisibility(View.GONE);
                salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading Data...");
                    pva_progressBar.setVisibility(View.GONE);
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    // if you come from filter then tab always be maintain.
                    if (filterSelectedString == null) {
                        if (getIntent().getStringExtra("selectedDept") == null) {
                            requestSalesViewPagerValueAPI();

                        } else if (getIntent().getStringExtra("selectedDept") != null) {
                            String selectedString = getIntent().getStringExtra("selectedDept");
                            requestSalesSelectedFilterVal(selectedString);
                        }
                    } else {
                        from_filter = true;
                        requestSalesViewPagerValueAPI();
                    }


                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
                break;

            case 1:

                if (salesPvA_SegmentClick.equals("LW"))
                    break;

                salesPvA_SegmentClick = "LW";
                pvaVal = " ";
                llpvahierarchy.setVisibility(View.GONE);
                salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading Data...");
                    pva_progressBar.setVisibility(View.GONE);
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    // if you come from filter then tab always be maintain.
                    if (filterSelectedString == null) {
                        if (getIntent().getStringExtra("selectedDept") == null) {
                            requestSalesViewPagerValueAPI();

                        } else if (getIntent().getStringExtra("selectedDept") != null) {
                            String selectedString = getIntent().getStringExtra("selectedDept");
                            requestSalesSelectedFilterVal(selectedString);
                        }
                    } else {
                        from_filter = true;
                        requestSalesViewPagerValueAPI();
                    }


                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }


    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSalesNext:
                if (postRequest != null) {
                    postRequest.cancel();
                }
                if (pva_progressBar.getVisibility() == View.VISIBLE) {
                    return;
                }
                if (geoLeveLDesc.equals("E ZONE"))
                {
                    switch (txtheaderplanclass.getText().toString())
                    {
                        case "Department":
                            btnSalesPrev.setVisibility(View.VISIBLE);
                            txtheaderplanclass.setText("Subdept");
                            fromWhere = "Subdept";
                            level = 2;
                            pvaVal = " ";
                            salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                            listViewSalesPvA.removeAllViews();
                            llpvahierarchy.setVisibility(View.GONE);
                            if (Reusable_Functions.chkStatus(context)) {

                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                pva_progressBar.setVisibility(View.GONE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                requestSalesViewPagerValueAPI();

                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }
                            break;

                        case "Subdept":
                            fromWhere = "Class";
                            txtheaderplanclass.setText("Class");
                            level = 3;
                            pvaVal = " ";
                            salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                            listViewSalesPvA.removeAllViews();
                            llpvahierarchy.setVisibility(View.GONE);
                            if (Reusable_Functions.chkStatus(context)) {
                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                pva_progressBar.setVisibility(View.GONE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                requestSalesViewPagerValueAPI();

                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }
                            break;

                        case "Class":
                            btnSalesNext.setVisibility(View.INVISIBLE);
                            txtheaderplanclass.setText("Subclass");
                            fromWhere = "Subclass";
                            level = 4;
                            pvaVal = " ";
                            salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                            listViewSalesPvA.removeAllViews();
                            llpvahierarchy.setVisibility(View.GONE);

                            if (Reusable_Functions.chkStatus(context)) {
                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                pva_progressBar.setVisibility(View.GONE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                requestSalesViewPagerValueAPI();
                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }
                            break;

//                        case "Brand":
//                            btnSalesNext.setVisibility(View.INVISIBLE);
//                            txtheaderplanclass.setText("Brand Class");
//                            fromWhere = "Brand Class";
//                            level = 5;
//                            pvaVal = " ";
//                            salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
//                            listViewSalesPvA.removeAllViews();
//                            llpvahierarchy.setVisibility(View.GONE);
//
//                            if (Reusable_Functions.chkStatus(context)) {
//                                Reusable_Functions.hDialog();
//                                Reusable_Functions.sDialog(context, "Loading data...");
//                                pva_progressBar.setVisibility(View.GONE);
//                                offsetvalue = 0;
//                                limit = 100;
//                                count = 0;
//                                requestSalesViewPagerValueAPI();
//
//                            } else {
//                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
//                            }
//                            break;

                        case "Region":
                            btnSalesNext.setVisibility(View.INVISIBLE);
                            btnSalesPrev.setVisibility(View.VISIBLE);
                            txtheaderplanclass.setText("Store");
                            fromWhere = "Store";
                            level = 9;
                            pvaVal = " ";
                            salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                            listViewSalesPvA.removeAllViews();
                            llpvahierarchy.setVisibility(View.GONE);

                            if (Reusable_Functions.chkStatus(context)) {
                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                pva_progressBar.setVisibility(View.GONE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                requestSalesViewPagerValueAPI();

                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        default:
                    }
                    break;
                }
                else // FBB login
                {
                    switch (txtheaderplanclass.getText().toString())
                    {
                        case "Department":
                            btnSalesPrev.setVisibility(View.VISIBLE);
                            txtheaderplanclass.setText("Category");
                            fromWhere = "Category";
                            level = 2;
                            pvaVal = " ";
                            salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                            listViewSalesPvA.removeAllViews();
                            llpvahierarchy.setVisibility(View.GONE);
                            if (Reusable_Functions.chkStatus(context)) {

                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                pva_progressBar.setVisibility(View.GONE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                requestSalesViewPagerValueAPI();

                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }
                            break;

                        case "Category":
                            fromWhere = "Class";
                            txtheaderplanclass.setText("Class");
                            level = 3;
                            pvaVal = " ";
                            salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                            listViewSalesPvA.removeAllViews();
                            llpvahierarchy.setVisibility(View.GONE);
                            if (Reusable_Functions.chkStatus(context)) {
                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                pva_progressBar.setVisibility(View.GONE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                requestSalesViewPagerValueAPI();

                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }
                            break;

                        case "Class":

                            txtheaderplanclass.setText("Brand");
                            fromWhere = "Brand";
                            level = 4;
                            pvaVal = " ";
                            salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                            listViewSalesPvA.removeAllViews();
                            llpvahierarchy.setVisibility(View.GONE);

                            if (Reusable_Functions.chkStatus(context)) {
                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                pva_progressBar.setVisibility(View.GONE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                requestSalesViewPagerValueAPI();
                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }
                            break;

                        case "Brand":
                            btnSalesNext.setVisibility(View.INVISIBLE);
                            txtheaderplanclass.setText("Brand Class");
                            fromWhere = "Brand Class";
                            level = 5;
                            pvaVal = " ";
                            salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                            listViewSalesPvA.removeAllViews();
                            llpvahierarchy.setVisibility(View.GONE);

                            if (Reusable_Functions.chkStatus(context)) {
                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                pva_progressBar.setVisibility(View.GONE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                requestSalesViewPagerValueAPI();

                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }
                            break;

                        default:
                    }
                    break;
                }

            case R.id.btnSalesBack:
                if (postRequest != null) {
                    postRequest.cancel();
                }
                if (pva_progressBar.getVisibility() == View.VISIBLE) {
                    return;
                }
                if(geoLeveLDesc.equals("E ZONE"))
                {
                    switch (txtheaderplanclass.getText().toString())
                    {
                        case "Subclass":
                            btnSalesNext.setVisibility(View.VISIBLE);
                            btnSalesPrev.setVisibility(View.VISIBLE);
                            txtheaderplanclass.setText("Class");
                            fromWhere = "Class";
                            level = 3;
                            pvaVal = " ";
                            salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                            listViewSalesPvA.removeAllViews();
                            llpvahierarchy.setVisibility(View.GONE);
                            if (Reusable_Functions.chkStatus(context)) {
                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                pva_progressBar.setVisibility(View.GONE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                requestSalesViewPagerValueAPI();

                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }
                            break;

                        case "Class":
                            btnSalesNext.setVisibility(View.VISIBLE);
                            txtheaderplanclass.setText("Subdept");
                            fromWhere = "Subdept";
                            level = 2;
                            pvaVal = " ";
                            salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                            listViewSalesPvA.removeAllViews();
                            llpvahierarchy.setVisibility(View.GONE);
                            if (Reusable_Functions.chkStatus(context)) {

                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                pva_progressBar.setVisibility(View.GONE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                requestSalesViewPagerValueAPI();

                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }

                            break;

                        case "Subdept":
                            btnSalesPrev.setVisibility(View.INVISIBLE);
                            btnSalesNext.setVisibility(View.VISIBLE);
                            txtheaderplanclass.setText("Department");
                            fromWhere = "Department";
                            level = 1;
                            pvaVal = " ";
                            salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                            listViewSalesPvA.removeAllViews();
                            llpvahierarchy.setVisibility(View.GONE);
                            if (Reusable_Functions.chkStatus(context)) {

                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                pva_progressBar.setVisibility(View.GONE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                requestSalesViewPagerValueAPI();
                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }
                            break;

                        case "Store":
                            btnSalesPrev.setVisibility(View.INVISIBLE);
                            btnSalesNext.setVisibility(View.VISIBLE);
                            txtheaderplanclass.setText("Region");
                            fromWhere = "Region";
                            level = 7;
                            pvaVal = " ";
                            salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                            listViewSalesPvA.removeAllViews();
                            llpvahierarchy.setVisibility(View.GONE);
                            if (Reusable_Functions.chkStatus(context)) {

                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                pva_progressBar.setVisibility(View.GONE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                requestSalesViewPagerValueAPI();
                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }

                            break;

                        default:
                    }
                    break;
                }
                else  // FBB login
                {
                    switch (txtheaderplanclass.getText().toString()) {

                        case "Brand Class":
                            btnSalesNext.setVisibility(View.VISIBLE);
                            btnSalesPrev.setVisibility(View.VISIBLE);
                            txtheaderplanclass.setText("Brand");
                            fromWhere = "Brand";
                            level = 4;
                            pvaVal = " ";
                            salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                            listViewSalesPvA.removeAllViews();
                            llpvahierarchy.setVisibility(View.GONE);
                            if (Reusable_Functions.chkStatus(context)) {
                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                pva_progressBar.setVisibility(View.GONE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                requestSalesViewPagerValueAPI();
                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }

                            break;

                        case "Brand":
                            btnSalesNext.setVisibility(View.VISIBLE);
                            txtheaderplanclass.setText("Class");
                            fromWhere = "Class";
                            level = 3;
                            pvaVal = " ";
                            salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                            listViewSalesPvA.removeAllViews();
                            llpvahierarchy.setVisibility(View.GONE);
                            if (Reusable_Functions.chkStatus(context)) {
                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                pva_progressBar.setVisibility(View.GONE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                requestSalesViewPagerValueAPI();

                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }
                            break;

                        case "Class":
                            txtheaderplanclass.setText("Category");
                            fromWhere = "Category";
                            level = 2;
                            pvaVal = " ";
                            salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                            listViewSalesPvA.removeAllViews();
                            llpvahierarchy.setVisibility(View.GONE);
                            if (Reusable_Functions.chkStatus(context)) {

                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                pva_progressBar.setVisibility(View.GONE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                requestSalesViewPagerValueAPI();

                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }

                            break;

                        case "Category":
                            btnSalesNext.setVisibility(View.VISIBLE);
                            btnSalesPrev.setVisibility(View.INVISIBLE);
                            txtheaderplanclass.setText("Department");
                            fromWhere = "Department";
                            level = 1;
                            pvaVal = " ";
                            salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                            listViewSalesPvA.removeAllViews();
                            llpvahierarchy.setVisibility(View.GONE);
                            if (Reusable_Functions.chkStatus(context)) {

                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                pva_progressBar.setVisibility(View.GONE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                requestSalesViewPagerValueAPI();
                            }
                            else
                            {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }

                            break;
                        default:
                    }
                    break;
                }
        }
    }
}

