package apsupportapp.aperotechnologies.com.designapp.Ezone;

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
import android.widget.AbsListView;
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

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.MyMarkerView;

import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.RunningPromo.RecyclerViewPositionHelper;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesAnalysisFilter;
import apsupportapp.aperotechnologies.com.designapp.model.RecyclerItemClickListener;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisListDisplay;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisViewPagerValue;
import apsupportapp.aperotechnologies.com.designapp.model.SalesPvAAnalysisWeek;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_FLING;
import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;


public class EzoneSalesPvAActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {

    SharedPreferences sharedPreferences;
    int offsetvalue = 0, limit = 100;
    int count = 0;
    RequestQueue queue;//
    RadioButton btn_WTD, btn_LW;
    private static String salesPvA_SegmentClick = "WTD", ez_tabClick = "WTD";
    ArrayList<SalesAnalysisListDisplay> salesAnalysisClassArrayList;
    LinearLayout llpvahierarchy;
    String userId, bearertoken, storeDescription, geoLeveLDesc, geoLevel2Code, lobId;
    EzonePvASnapAdapter salesPvAAdapter;
    ViewPortHandler handler;
    Context context;
    RelativeLayout btn_reset;
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
    SalesAnalysisViewPagerValue salesAnalysisViewPagerValue;
    static String planDept, planCategory, planClass;
    String pvaVal, TAG = "SalesPvAActivity";
    int currentIndex, prevState = RecyclerView.SCROLL_STATE_IDLE, currentState = RecyclerView.SCROLL_STATE_IDLE;
    boolean onItemClickFlag = false, filter_toggleClick = false;
    ProgressBar pva_progressBar, ez_progressBar;
    public static Activity Ezone_Sales_Pva_Activity;
    private TabLayout tabLayout, ez_tabLayout;
    private PopupWindow popupWindow;
    private RadioButton product_radiobtn, location_radiobtn;
    private int preValue = 1, postValue, sales_filter_level;
    private boolean from_filter;
    private String filterSelectedString, isMultiStore, value;
    private String dept_clickVal, categry_clickVal, class_clickVal, brand_clickVal,region_clickVal;
    public String all_from_val,selectedString;
    private String header_value;
    private String[] hierarchyList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        Ezone_Sales_Pva_Activity = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        storeDescription = sharedPreferences.getString("storeDescription", "");
        geoLeveLDesc = sharedPreferences.getString("geoLeveLDesc", "");
        geoLevel2Code = sharedPreferences.getString("concept", "");
        lobId = sharedPreferences.getString("lobid", "");
        isMultiStore = sharedPreferences.getString("isMultiStore", "");
        value = sharedPreferences.getString("value", "");
        String hierarchyLevels = sharedPreferences.getString("hierarchyLevels", "");
        // replace all labels using hierarchyList
        hierarchyList = hierarchyLevels.split(",");
        for (int i = 0; i <hierarchyList.length ; i++) {
            hierarchyList[i]=hierarchyList[i].trim();
        }
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();
        header_value = "";
        setContentView(R.layout.activity_ezone_pva);
        getSupportActionBar().hide();
        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
        commanInitialize();
        initializeEzoneUI();
        commanListView();

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                arrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                barChart.invalidate();
                header_value = "";
                // drill_down_val = "";
                selFirstPositionValue = 0;
                focusposition = 0;
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.sDialog(context, "Loading data...");
                    pva_progressBar.setVisibility(View.GONE);
                    btnSalesNext.setVisibility(View.VISIBLE);
                    btnSalesPrev.setVisibility(View.GONE);
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    level = 1;
                    salesPvA_SegmentClick = "WTD";
                    tabLayout.getTabAt(0).select();
                    fromWhere = hierarchyList[0];
                    txtheaderplanclass.setText(hierarchyList[0]);
                    llpvahierarchy.setVisibility(View.GONE);
                    filter_toggleClick = false;
                    retainSegmentValuesFilter();
                    requestSalesViewPagerValueAPI();
                }
                else
                {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void commanListView() {

        filterSelectedString = getIntent().getStringExtra("selectedStringVal");
        int filter_level = getIntent().getIntExtra("selectedlevelVal", 0);
        fromWhere = hierarchyList[0];
        txtheaderplanclass.setText(hierarchyList[0]);
        focusposition = 0;

        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.sDialog(context, "Loading data...");
            pva_progressBar.setVisibility(View.GONE);
            offsetvalue = 0;
            limit = 100;
            count = 0;
            level = 1;
            llpvahierarchy.setVisibility(View.GONE);

            if (getIntent().getStringExtra("selectedStringVal") == null) {
                filter_toggleClick = false;
                header_value = "";
                retainSegmentValuesFilter();
                requestSalesViewPagerValueAPI();
            } else if (getIntent().getStringExtra("selectedStringVal") != null) {
                selectedString = getIntent().getStringExtra("selectedStringVal");
                header_value=selectedString;
                sales_filter_level = getIntent().getIntExtra("selectedlevelVal", 0);
                filter_toggleClick = true;
                retainSegmentValuesFilter();
                requestHeaderAPI("filter");
            }
//            else {
//
//                Log.e("filter back calling", "=======");
//                retainSegmentValuesFilter();
//                level = filter_level;
//                from_filter = true;
//                if (filter_level == 9) {
//                    preValue = 2;
//                    location_radiobtn.setChecked(true);
//                    product_radiobtn.setChecked(false);
//                }
//                requestfromPvaFilter(filterSelectedString);
//
//            }


        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }




        listViewSalesPvA.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                RecyclerViewPositionHelper mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
                totalItemCount = mRecyclerViewHelper.getItemCount();
                focusposition = mRecyclerViewHelper.findFirstVisibleItemPosition();
                listViewSalesPvA.invalidate();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                listViewSalesPvA.invalidate();

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
                                selFirstPositionValue =0;
                                focusposition=0;

                                if(txtheaderplanclass.getText().toString().equals(hierarchyList[0])){
                                    btnSalesPrev.setVisibility(View.VISIBLE);
                                    fromWhere = hierarchyList[1];
                                    txtheaderplanclass.setText(hierarchyList[1]);
                                    txtPvAClickedValue = salesAnalysisClassArrayList.get(position).getLevel();

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
                                        arrayList.clear();
                                        dept_clickVal = txtPvAClickedValue;
                                        requestHeaderAPI(hierarchyList[1]);
                                        //  planDept = txtPvAClickedValue;


                                    } else {
                                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else if(txtheaderplanclass.getText().toString().equals(hierarchyList[1])){
                                    txtheaderplanclass.setText(hierarchyList[2]);
                                    txtPvAClickedValue = salesAnalysisClassArrayList.get(position).getLevel();

                                    fromWhere = hierarchyList[2];
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
                                        arrayList.clear();
                                        //* requestHeaderAPI("");
                                        // requestSalesPvAPlanClassListAPI(txtPvAClickedValue);//*
                                        categry_clickVal = txtPvAClickedValue;
                                        requestHeaderAPI(hierarchyList[2]);
                                        // planCategory = txtPvAClickedValue;
                                    } else {
                                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else if(txtheaderplanclass.getText().toString().equals(hierarchyList[2])){

                                    txtheaderplanclass.setText(hierarchyList[3]);
                                    txtPvAClickedValue = salesAnalysisClassArrayList.get(position).getLevel();
                                    btnSalesNext.setVisibility(View.INVISIBLE);
                                    fromWhere = hierarchyList[3];
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
                                        arrayList.clear();
                                        class_clickVal = txtPvAClickedValue;
                                        requestHeaderAPI(hierarchyList[3]);
                                        // requestSalesPvABrandListAPI(txtPvAClickedValue);
                                        //  planClass = txtPvAClickedValue;
                                    } else {
                                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else if(txtheaderplanclass.getText().toString().equals("Region")){

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
                                        region_clickVal = txtPvAClickedValue;
                                        requestHeaderAPI("store");
                                    } else {
                                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{

                                    Reusable_Functions.hDialog();
                                    Toast.makeText(context, " You are at the last level of hierarchy", Toast.LENGTH_SHORT).show();
                                    onItemClickFlag = false;
                                }


                                    /*switch (txtheaderplanclass.getText().toString()) {
                                        case "Department":
                                            btnSalesPrev.setVisibility(View.VISIBLE);

                                            fromWhere = "Subdept";
                                            txtheaderplanclass.setText("Subdept");
                                            txtPvAClickedValue = salesAnalysisClassArrayList.get(position).getLevel();

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
                                                arrayList.clear();
                                                Log.e(TAG, "click on: " + txtPvAClickedValue);
                                                dept_clickVal = txtPvAClickedValue;
                                                requestHeaderAPI("category");
                                              //  planDept = txtPvAClickedValue;


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
                                                arrayList.clear();
                                               *//* requestHeaderAPI("");
                                                requestSalesPvAPlanClassListAPI(txtPvAClickedValue);*//*
                                                categry_clickVal = txtPvAClickedValue;
                                                requestHeaderAPI("class");
                                               // planCategory = txtPvAClickedValue;
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
                                                arrayList.clear();
                                                class_clickVal = txtPvAClickedValue;
                                                requestHeaderAPI("brand");
                                               // requestSalesPvABrandListAPI(txtPvAClickedValue);
                                              //  planClass = txtPvAClickedValue;
                                            } else {
                                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                            }

                                            break;


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
                                                region_clickVal = txtPvAClickedValue;
                                                requestHeaderAPI("store");
                                            } else {
                                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                            }
                                            break;

                                        default:
                                            Reusable_Functions.hDialog();
                                            Toast.makeText(context, " You are at the last level of hierarchy", Toast.LENGTH_SHORT).show();
                                            onItemClickFlag = false;
                                            break;

                                    }*/

                            }
                        }
                    }, 700);
                }
            }
        }));
    }


    private void initializeEzoneUI() {
        btnFilter = (RelativeLayout) findViewById(R.id.imgfilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent filterIntent = new Intent(EzoneSalesPvAActivity.this, EzoneSalesFilter.class);
                filterIntent.putExtra("checkfrom", "ezonepvaAnalysis");
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
        Ezone_Sales_Pva_Activity = this;
        pva_progressBar = (ProgressBar) findViewById(R.id.pva_progressBar);
        tabLayout = (TabLayout) findViewById(R.id.tabview_salespva);
        tabLayout.addTab(tabLayout.newTab().setText("WTD"));
        tabLayout.addTab(tabLayout.newTab().setText("LW"));
        tabLayout.setOnTabSelectedListener(this);
        listViewSalesPvA = (RecyclerView) findViewById(R.id.list);
        llpvahierarchy = (LinearLayout) findViewById(R.id.llpvahierarchy);
        llpvahierarchy.setOrientation(LinearLayout.HORIZONTAL);
        barChart = (BarChart) findViewById(R.id.bar_chart);
        btn_reset = (RelativeLayout) findViewById(R.id.btn_reset);
        rel_store_layout = (RelativeLayout) findViewById(R.id.rel_store_layout);
        // rel_store_layout.setVisibility(View.VISIBLE);
        txtStoreCode = (TextView) findViewById(R.id.txtStoreCode);
        txtStoreDesc = (TextView) findViewById(R.id.txtStoreName);
        if (isMultiStore.equals("Yes")) {
            txtStoreCode.setText("Concept : ");
            txtStoreDesc.setText(value);

        } else {
            txtStoreCode.setText("Store : ");
            txtStoreDesc.setText(value);
        }
//        if (geoLeveLDesc.equals("E ZONE"))
//        {
//          //  rel_store_layout.setVisibility(View.GONE);
//        }
//        else
//        {
//            rel_store_layout.setVisibility(View.VISIBLE);
////            txtStoreCode.setText(storeDescription.trim().substring(0, 4));
////            txtStoreDesc.setText(storeDescription.substring(5));
//        }

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

        if (!(postValue == preValue)) {
// for product...
            if (postValue == 1) {

                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.sDialog(context, "Loading data...");
                    selFirstPositionValue =0;
                    focusposition=0;
                    llpvahierarchy.setVisibility(View.GONE);
                    preValue = postValue;
                    txtheaderplanclass.setText(hierarchyList[0]);
                    level = 1;
                    btnSalesPrev.setVisibility(View.INVISIBLE);
                    btnSalesNext.setVisibility(View.VISIBLE);
                    salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                    salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                    arrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                    requestSalesViewPagerValueAPI();
                } else {
                    product_radiobtn.setChecked(false);
                    location_radiobtn.setChecked(true);
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }

            } else if (postValue == 2) {
// for location...
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.sDialog(context, "Loading data...");
                    selFirstPositionValue =0;
                    focusposition=0;
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
                } else {
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
        if (salesPvA_SegmentClick.equals("WTD"))
        {
            //   btn_WTD.toggle();
            tabLayout.getTabAt(0).select();

        }
        else if (salesPvA_SegmentClick.equals("LW"))
        {
            //  btn_LW.toggle();
            tabLayout.getTabAt(1).select();

        }
        if(getIntent().getStringExtra("selectedStringVal") != null){
            if(sales_filter_level==7 || sales_filter_level==9){
                product_radiobtn.setChecked(false);
                location_radiobtn.setChecked(true);
                preValue=2;
            }
        }
    }

    private void TimeUP()
    {
//        if (geoLeveLDesc.equals("E ZONE")) {
        if (salesAnalysisClassArrayList.size() != 0) {
            if (focusposition < salesAnalysisClassArrayList.size() - 1 && !onItemClickFlag) {

                if (txtheaderplanclass.getText().toString().equals(hierarchyList[0])) {
                    level = 1;
                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();


                } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[1])) {
                    level = 2;
                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();


                } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[2])) {
                    level = 3;
                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();

                } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[3])) {
                    level = 4;
                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();
                } else if (txtheaderplanclass.getText().toString().equals("Region")) {
                    level = 7;
                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();

                } else if (txtheaderplanclass.getText().toString().equals("Store")) {
                    level = 9;
                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();
                }

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
                            requestSalesWeekChart(header_value);

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

            } else {
                focusposition = salesAnalysisClassArrayList.size() - 1;
                LinearLayoutManager llm = (LinearLayoutManager) listViewSalesPvA.getLayoutManager();
                llm.scrollToPosition(focusposition);
                //  listViewSalesPvA.getLayoutManager().scrollToPosition(focusposition);

                if (txtheaderplanclass.getText().toString().equals(hierarchyList[0])) {
                    level = 1;
                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();


                } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[1])) {
                    level = 2;
                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();


                } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[2])) {
                    level = 3;
                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();


                } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[3])) {
                    level = 4;

                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();


                } else if (txtheaderplanclass.getText().toString().equals("Region")) {
                    level = 7;
                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();

                } else if (txtheaderplanclass.getText().toString().equals("Store")) {
                    level = 9;
                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();
                }
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
                            requestSalesWeekChart(header_value);

                        } else {
                            salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                            barChart.invalidate();
                            barChart.setScaleEnabled(false);
                            requestPvAChartAPI();

                        }
                        selFirstPositionValue = focusposition;
                    }
                }
                else
                {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }

            }
        }
//        } else // FBB login
//        {

//        }
    }

    private ArrayList<Entry> setYAxisValues() {
        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < salesPvAAnalysisWeekArrayList.size(); i++) {

            float val = (float) salesPvAAnalysisWeekArrayList.get(i).getPvaAchieved();
            yVals.add(new Entry(i, val));
        }

        return yVals;
    }

    private ArrayList<String> setXAxisValues() {
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < salesPvAAnalysisWeekArrayList.size(); i++) {

            xVals.add(String.valueOf(salesPvAAnalysisWeekArrayList.get(i).getWeekNumber()));
        }

        return xVals;
    }


    // API 1.20
    private void requestSalesListDisplayAPI() {
        String salespva_listurl;

        if (from_filter) {
            salespva_listurl = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + filterSelectedString.replace(" ", "%20") + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&lobId=" + lobId;
        } else {
            salespva_listurl = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&lobId=" + lobId;
        }

        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
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

                                if (txtheaderplanclass.getText().toString().equals(hierarchyList[0])) {
                                    salesAnalysisListDisplay.setLevel("All");
                                    salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                    salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                    salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());

                                } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[1])) {
                                    salesAnalysisListDisplay.setLevel("All");
                                    salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                    salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                    salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());

                                } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[2])) {
                                    salesAnalysisListDisplay.setLevel("All");
                                    salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                    salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                    salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());

                                } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[3])) {
                                    salesAnalysisListDisplay.setLevel("All");
                                    salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                    salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                    salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());

                                } else if (txtheaderplanclass.getText().toString().equals("Region")) {
                                    salesAnalysisListDisplay.setLevel("All");
                                    salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                    salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                    salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());
                                } else if (txtheaderplanclass.getText().toString().equals("Store")) {
                                    salesAnalysisListDisplay.setLevel("All");
                                    salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                    salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                    salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());
                                }

                                salesAnalysisClassArrayList.add(0, salesAnalysisListDisplay);
                                listViewSalesPvA.setLayoutManager(new LinearLayoutManager(context));

                                listViewSalesPvA.setLayoutManager(new LinearLayoutManager(
                                        listViewSalesPvA.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                listViewSalesPvA.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(listViewSalesPvA);

                                salesPvAAdapter = new EzonePvASnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listViewSalesPvA, geoLeveLDesc,hierarchyList);
                                listViewSalesPvA.setAdapter(salesPvAAdapter);


                                // Retain values....
                                if (txtheaderplanclass.getText().toString().equals(hierarchyList[0])) {
                                    for (int j = 0; j < salesAnalysisClassArrayList.size(); j++) {
                                        level = 1;
                                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();
                                        if (salesAnalysisClassArrayList.get(focusposition).getLevel().equals(pvaFirstVisibleItem)) {
                                            listViewSalesPvA.getLayoutManager().scrollToPosition(focusposition);
                                        }
                                    }
                                } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[1])) {
                                    for (int j = 0; j < salesAnalysisClassArrayList.size(); j++) {
                                        level = 2;
                                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();
                                        if (salesAnalysisClassArrayList.get(focusposition).getLevel().equals(pvaFirstVisibleItem)) {
                                            listViewSalesPvA.getLayoutManager().scrollToPosition(focusposition);
                                        }
                                    }
                                } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[2])) {
                                    for (int j = 0; j < salesAnalysisClassArrayList.size(); j++) {
                                        level = 3;
                                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();
                                        if (salesAnalysisClassArrayList.get(focusposition).getLevel().equals(pvaFirstVisibleItem)) {
                                            listViewSalesPvA.getLayoutManager().scrollToPosition(focusposition);
                                        }
                                    }
                                } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[3])) {
                                    for (int j = 0; j < salesAnalysisClassArrayList.size(); j++) {
                                        level = 4;
                                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();
                                        if (salesAnalysisClassArrayList.get(focusposition).getLevel().equals(pvaFirstVisibleItem)) {
                                            listViewSalesPvA.getLayoutManager().scrollToPosition(focusposition);
                                        }
                                    }
                                } else if (txtheaderplanclass.getText().toString().equals("Region")) {
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
                                // end of else
//                                header_value = "";
                                if (pvaFirstVisibleItem.equals("All")) {
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    llpvahierarchy.setVisibility(View.GONE);
                                    salesPvAAnalysisWeekArrayList.clear();
                                    requestSalesWeekChart(header_value);

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

        String url = "";
        pvaFirstVisibleItem = pvaFirstVisibleItem.replace("%", "%25");
        pvaFirstVisibleItem = pvaFirstVisibleItem.replace(" ", "%20").replace("&", "%26");

        if(!header_value.equals(""))
        {
            if (txtheaderplanclass.getText().toString().equals(hierarchyList[0])) {
                url = ConstsCore.web_url + "/v1/display/salesanalysisPVA13WeekgraphEZNew/" + userId + "?department=" + pvaFirstVisibleItem + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&view=" + salesPvA_SegmentClick + "&lobId=" + lobId + header_value;

            } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[1])) {
                url = ConstsCore.web_url + "/v1/display/salesanalysisPVA13WeekgraphEZNew/" + userId + "?category=" + pvaFirstVisibleItem + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&view=" + salesPvA_SegmentClick + "&lobId=" + lobId + header_value;

            } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[2])) {
                url = ConstsCore.web_url + "/v1/display/salesanalysisPVA13WeekgraphEZNew/" + userId + "?class=" + pvaFirstVisibleItem + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&view=" + salesPvA_SegmentClick + "&lobId=" + lobId + header_value;

            } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[3])) {
                url = ConstsCore.web_url + "/v1/display/salesanalysisPVA13WeekgraphEZNew/" + userId + "?brand=" + pvaFirstVisibleItem + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&view=" + salesPvA_SegmentClick + "&lobId=" + lobId + header_value;

            } else if (txtheaderplanclass.getText().toString().equals("Region")) {

                url = ConstsCore.web_url + "/v1/display/salesanalysisPVA13WeekgraphEZNew/" + userId + "?regionDescription=" + pvaFirstVisibleItem + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&view=" + salesPvA_SegmentClick + "&lobId=" + lobId + header_value;

            } else if (txtheaderplanclass.getText().toString().equals("Store"))
            {
                url = ConstsCore.web_url + "/v1/display/salesanalysisPVA13WeekgraphEZNew/" + userId + "?storeCode=" + pvaFirstVisibleItem.substring(0,4) + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&view=" + salesPvA_SegmentClick + "&lobId=" + lobId + header_value;
            }
        }
        else
        {
            if (txtheaderplanclass.getText().toString().equals(hierarchyList[0])) {
                url = ConstsCore.web_url + "/v1/display/salesanalysisPVA13WeekgraphEZNew/" + userId + "?department=" + pvaFirstVisibleItem + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&view=" + salesPvA_SegmentClick + "&lobId=" + lobId;

            } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[1])) {
                url = ConstsCore.web_url + "/v1/display/salesanalysisPVA13WeekgraphEZNew/" + userId + "?category=" + pvaFirstVisibleItem + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&view=" + salesPvA_SegmentClick + "&lobId=" + lobId;

            } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[2])) {
                url = ConstsCore.web_url + "/v1/display/salesanalysisPVA13WeekgraphEZNew/" + userId + "?class=" + pvaFirstVisibleItem + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&view=" + salesPvA_SegmentClick + "&lobId=" + lobId;

            } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[3])) {
                url = ConstsCore.web_url + "/v1/display/salesanalysisPVA13WeekgraphEZNew/" + userId + "?brand=" + pvaFirstVisibleItem + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&view=" + salesPvA_SegmentClick + "&lobId=" + lobId;

            } else if (txtheaderplanclass.getText().toString().equals("Region")) {

                url = ConstsCore.web_url + "/v1/display/salesanalysisPVA13WeekgraphEZNew/" + userId + "?regionDescription=" + pvaFirstVisibleItem + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&view=" + salesPvA_SegmentClick + "&lobId=" + lobId;

            } else if (txtheaderplanclass.getText().toString().equals("Store"))
            {
                url = ConstsCore.web_url + "/v1/display/salesanalysisPVA13WeekgraphEZNew/" + userId + "?storeCode=" + pvaFirstVisibleItem.substring(0,4) + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&view=" + salesPvA_SegmentClick + "&lobId=" + lobId;
            }

        }

//        Log.e(TAG, "requestPvAChartAPI: " + url);
        postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Log.e(TAG, "onResponse: "+response);
//                        Log.e("requestPvAChartAPI ", " " + response);
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

    private void requestSalesWeekChart(final String fromWhere) {
        String salespvaweekChart_url = "";

        if (fromWhere.equals("filter"))
        {
            salespvaweekChart_url = ConstsCore.web_url + "/v1/display/salesanalysisPVA13WeekgraphEZNew/" + userId + "?view=" + salesPvA_SegmentClick + selectedString + "&geoLevel2Code=" + geoLevel2Code  + "&offset=" + offsetvalue + "&limit=" + limit + "&lobId=" + lobId;
        }
        if(!header_value.equals(""))
        {
            salespvaweekChart_url = ConstsCore.web_url + "/v1/display/salesanalysisPVA13WeekgraphEZNew/" + userId + "?view=" + salesPvA_SegmentClick + "&geoLevel2Code=" + geoLevel2Code   + "&offset=" + offsetvalue + "&limit=" + limit + "&lobId=" + lobId + header_value;

        }
        else{
            salespvaweekChart_url = ConstsCore.web_url + "/v1/display/salesanalysisPVA13WeekgraphEZNew/" + userId + "?view=" + salesPvA_SegmentClick + "&geoLevel2Code=" + geoLevel2Code  + "&offset=" + offsetvalue + "&limit=" + limit + "&lobId=" + lobId;

        }
//        Log.e(TAG, "requestSalesWeekChart: "+salespvaweekChart_url);

//        if (geoLeveLDesc.equals("E ZONE")) {
//            //https://smdm.manthan.com/v1/display/salesanalysisPVA13WeekgraphEZ/1234
//        } else {
//        if (fromWhere.equals("category"))
//        {
//            dept_clickVal = dept_clickVal.replace("%", "%25");
//            dept_clickVal = dept_clickVal.replace(" ", "%20").replace("&", "%26");
//            salespvaweekChart_url = ConstsCore.web_url + "/v1/display/salesvisualpvaanalysisbyweekNew/" + userId + "?view=" + salesPvA_SegmentClick + "&dept="+dept_clickVal +"&geoLevel2Code=" + geoLevel2Code + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit + "&lobId=" + lobId;
//
//        } else if (fromWhere.equals("class"))
//        {
//            categry_clickVal = categry_clickVal.replace("%", "%25");
//            categry_clickVal = categry_clickVal.replace(" ", "%20").replace("&", "%26");
//            salespvaweekChart_url = ConstsCore.web_url + "/v1/display/salesvisualpvaanalysisbyweekNew/" + userId + "?view=" + salesPvA_SegmentClick +"&category="+categry_clickVal + "&geoLevel2Code=" + geoLevel2Code + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit + "&lobId=" + lobId;
//
//        } else if (fromWhere.equals("brand"))
//        {
//            class_clickVal = class_clickVal.replace("%", "%25");
//            class_clickVal = class_clickVal.replace(" ", "%20").replace("&", "%26");
//            salespvaweekChart_url = ConstsCore.web_url + "/v1/display/salesvisualpvaanalysisbyweekNew/" + userId + "?view=" + salesPvA_SegmentClick+"&class="+class_clickVal + "&geoLevel2Code=" + geoLevel2Code + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit + "&lobId=" + lobId;
//
//        } else if (fromWhere.equals("brandClass"))
//        {
//            brand_clickVal = brand_clickVal.replace("%", "%25");
//            brand_clickVal = brand_clickVal.replace(" ", "%20").replace("&", "%26");
//            salespvaweekChart_url = ConstsCore.web_url + "/v1/display/salesvisualpvaanalysisbyweekNew/" + userId + "?view=" + salesPvA_SegmentClick+"&brand="+brand_clickVal + "&geoLevel2Code=" + geoLevel2Code + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit + "&lobId=" + lobId;
//        }
//        else if (fromWhere.equals("filter"))
//        {
//            salespvaweekChart_url = ConstsCore.web_url + "/v1/display/salesvisualpvaanalysisbyweekNew/" + userId + "?view=" + salesPvA_SegmentClick + selectedString + "&geoLevel2Code=" + geoLevel2Code + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit + "&lobId=" + lobId;
//        }
//        else
//        {
//            salespvaweekChart_url = ConstsCore.web_url + "/v1/display/salesvisualpvaanalysisbyweekNew/" + userId + "?view=" + salesPvA_SegmentClick + "&geoLevel2Code=" + geoLevel2Code + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit + "&lobId=" + lobId;
//        }
        postRequest = new JsonArrayRequest(Request.Method.GET, salespvaweekChart_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        Log.e(TAG, "requestSalesWeekChart: " + response);
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
                                requestSalesWeekChart(fromWhere);
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

                if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0)
                {
                    set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
                    set2 = (BarDataSet) barChart.getData().getDataSetByIndex(1);
                    set1.setValues(yVals1);
                    set2.setValues(yVals2);
                    barChart.getData().notifyDataChanged();
                    barChart.notifyDataSetChanged();
                }
                else
                {
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
            }
            else
            {
                barChart.clear();
            }
        } catch (Exception e) {
            barChart.clear();

        }

    }


    // drill down level API
    private void requestSalesPvACategoryList(final String deptName) {

        String salespvacategory_listurl;
//        if (geoLeveLDesc.equals("E ZONE")) {
        salespvacategory_listurl = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&department=" + deptName.replaceAll(" ", "%20").replaceAll("&", "%26") +"&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit+ "&lobId="+ lobId;
//
//        } else {
//        salespvacategory_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytimeNew/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&department=" + deptName.replaceAll(" ", "%20").replaceAll("&", "%26") + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&lobId=" + lobId;
//        }

        postRequest = new JsonArrayRequest(Request.Method.GET, salespvacategory_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
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
//                                if(geoLeveLDesc.equals("E ZONE"))
//                                {
//                                    if (txtheaderplanclass.getText().toString().equals("Subdept")) {
//
//                                        salesAnalysisListDisplay.setLevel("All");
//
//                                    }
//                                }
//                                else
//                                {
                                salesAnalysisListDisplay = new SalesAnalysisListDisplay();
                                if (txtheaderplanclass.getText().toString().equals(hierarchyList[1]))
                                {
                                    salesAnalysisListDisplay.setLevel("All");
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
                                salesPvAAdapter = new EzonePvASnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listViewSalesPvA, geoLeveLDesc,hierarchyList);
                                listViewSalesPvA.setAdapter(salesPvAAdapter);
//                                    salesPvAAdapter.notifyDataSetChanged();
                                pvaVal = " ";
                                pvaVal = deptName.replaceAll("%20", " ").replaceAll("%26", "&");
                                txtpvahDeptName.setText(pvaVal);
                                llpvahierarchy.setVisibility(View.VISIBLE);
                                salesPvAAnalysisWeekArrayList.clear();
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
//                                    if (geoLeveLDesc.equals("E ZONE")) {
//                                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(0).getLevel();
//
//                                    } else {
                                pvaFirstVisibleItem = salesAnalysisClassArrayList.get(0).getLevel();
                                if(!header_value.contains("&department=" + deptName.replaceAll(" ", "%20").replaceAll("&", "%26"))) {

                                    header_value += "&department=" + deptName.replaceAll(" ", "%20").replaceAll("&", "%26");
                                }
                                salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                                if (pvaFirstVisibleItem.equals("All")) {
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    all_from_val = "category";
                                    salesPvAAnalysisWeekArrayList.clear();
                                    requestSalesWeekChart(header_value);
                                } else {
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    salesPvAAnalysisWeekArrayList.clear();
                                    requestPvAChartAPI();
                                }
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

    private void requestSalesPvAPlanClassListAPI(final String category)
    {
        String salespva_planclass_listurl;
        salespva_planclass_listurl = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&category=" + category.replaceAll(" ", "%20").replaceAll("&", "%26") + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&lobId=" + lobId;
        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_planclass_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        Log.e(TAG, "requestSalesPvAPlanClassListAPI: " + response);

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
                                if (txtheaderplanclass.getText().toString().equals(hierarchyList[2]))
                                {
                                    salesAnalysisListDisplay.setLevel("All");
                                }
                                salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());
                                salesAnalysisClassArrayList.add(0, salesAnalysisListDisplay);
                                salesPvAAdapter = new EzonePvASnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listViewSalesPvA, geoLeveLDesc,hierarchyList);
                                listViewSalesPvA.setAdapter(salesPvAAdapter);
                                if(pvaVal.equals("") || pvaVal.equals(" ")) pvaVal=categry_clickVal.replaceAll("%20", " ").replaceAll("%26", "&");
                                else pvaVal += " > " + categry_clickVal.replaceAll("%20", " ").replaceAll("%26", "&");
                                txtpvahDeptName.setText(pvaVal);
                                llpvahierarchy.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                salesPvAAnalysisWeekArrayList.clear();
//                                    if (geoLeveLDesc.equals("E ZONE")) {
                                pvaFirstVisibleItem = salesAnalysisClassArrayList.get(0).getLevel();
//
//                                    } else {
//                                pvaFirstVisibleItem = salesAnalysisClassArrayList.get(0).getPlanClass();
//                                    }
                                if(!header_value.contains("&category=" + categry_clickVal.replaceAll(" ", "%20").replaceAll("&", "%26")))
                                {
                                    header_value += "&category=" + categry_clickVal.replaceAll(" ", "%20").replaceAll("&", "%26");
                                }
                                salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                                if (pvaFirstVisibleItem.equals("All")) {
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    all_from_val = "class";
                                    salesPvAAnalysisWeekArrayList.clear();
                                    requestSalesWeekChart(header_value);
                                } else {
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    salesPvAAnalysisWeekArrayList.clear();
                                    requestPvAChartAPI();
                                }
                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            onItemClickFlag = false;
                            pva_progressBar.setVisibility(View.GONE);
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

    private void requestProductAndLocation(final String region)
    {
        String salespva_planclass_listurl;
        salespva_planclass_listurl = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&regionDescription=" + region.replaceAll(" ", "%20").replaceAll("&", "%26") + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&lobId=" + lobId;
        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_planclass_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                pva_progressBar.setVisibility(View.GONE);
                                onItemClickFlag = false;
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
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
                                salesPvAAdapter = new EzonePvASnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listViewSalesPvA, geoLeveLDesc,hierarchyList);
                                listViewSalesPvA.setAdapter(salesPvAAdapter);
                                salesPvAAdapter.notifyDataSetChanged();
                                pvaVal = " ";
                                pvaVal = region.replaceAll("%20", " ").replaceAll("%26", "&");
                                txtpvahDeptName.setText(pvaVal);
                                llpvahierarchy.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                salesPvAAnalysisWeekArrayList.clear();
                                pvaFirstVisibleItem = salesAnalysisClassArrayList.get(0).getLevel();
                                salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                                if(!header_value.contains("&regionDescription=" + region.replaceAll(" ", "%20").replaceAll("&", "%26")))
                                {
                                    header_value += "&regionDescription=" + region.replaceAll(" ", "%20").replaceAll("&", "%26");
                                }
                                if (pvaFirstVisibleItem.equals("All")) {
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    salesPvAAnalysisWeekArrayList.clear();
                                    requestSalesWeekChart(header_value);
                                } else {
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    salesPvAAnalysisWeekArrayList.clear();
                                    requestPvAChartAPI();
                                }
                            }

                        }
                        catch (Exception e)
                        {
                            Reusable_Functions.hDialog();
                            onItemClickFlag = false;
                            pva_progressBar.setVisibility(View.GONE);
//                            Log.e(TAG, "onResponse in store: "+e.getMessage());
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

    private void requestSalesPvABrandListAPI(final String planclass) {

        String salespva_brand_listurl;
        salespva_brand_listurl = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&class=" + planclass.replaceAll(" ", "%20").replaceAll("&", "%26") + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&lobId=" + lobId;
        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_brand_listurl,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
//                        Log.e(TAG, "requestSalesPvAPlanClassListAPI: " + response);

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

                                if (txtheaderplanclass.getText().toString().equals(hierarchyList[3])) {
                                    salesAnalysisListDisplay.setLevel("All");

                                }
                                salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());

                                salesAnalysisClassArrayList.add(0, salesAnalysisListDisplay);
                                salesPvAAdapter = new EzonePvASnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listViewSalesPvA, geoLeveLDesc,hierarchyList);
                                listViewSalesPvA.setAdapter(salesPvAAdapter);
                                if(pvaVal.equals("") || pvaVal.equals(" ")) pvaVal=class_clickVal.replaceAll("%20", " ").replaceAll("%26", "&");
                                else pvaVal += " > " + class_clickVal.replaceAll("%20", " ").replaceAll("%26", "&");
                                txtpvahDeptName.setText(pvaVal);
                                llpvahierarchy.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                salesPvAAnalysisWeekArrayList.clear();
                                if (geoLeveLDesc.equals("E ZONE"))
                                {
                                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(0).getLevel();

                                }
                                salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                                if(!header_value.contains("&class=" + planclass.replaceAll(" ", "%20").replaceAll("&", "%26")))
                                {
                                    header_value += "&class=" + planclass.replaceAll(" ", "%20").replaceAll("&", "%26");
                                }
                                if (pvaFirstVisibleItem.equals("All")) {
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    all_from_val = "brand";
                                    salesPvAAnalysisWeekArrayList.clear();
                                    requestSalesWeekChart(header_value);
                                } else {
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    salesPvAAnalysisWeekArrayList.clear();
                                    requestPvAChartAPI();
                                }
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
        salespva_brandplan_listurl = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&brand=" + brandnm.replaceAll(" ", "%20").replaceAll("&", "%26") + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&lobId=" + lobId;



        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_brandplan_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        Log.e(TAG, "requestSalesPvAPlanClassListAPI: " + response);

                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                pva_progressBar.setVisibility(View.GONE);
                                onItemClickFlag = false;
                                Toast.makeText(context, "No Brand Class data found", Toast.LENGTH_SHORT).show();

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++)
                                {

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
                                if (txtheaderplanclass.getText().toString().equals(hierarchyList[4])) {
//                                        if (geoLeveLDesc.equals("E ZONE")) {
//                                            salesAnalysisListDisplay.setLevel("All");
//                                        } else {
                                    salesAnalysisListDisplay.setBrandplanClass("All");
//                                        }
                                }
                                salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());
                                salesAnalysisClassArrayList.add(0, salesAnalysisListDisplay);
                                salesPvAAdapter = new EzonePvASnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listViewSalesPvA, geoLeveLDesc,hierarchyList);
                                listViewSalesPvA.setAdapter(salesPvAAdapter);
                                salesPvAAdapter.notifyDataSetChanged();
                                categry_clickVal = categry_clickVal.replaceAll("%20", " ").replaceAll("%26", "&");
                                if(pvaVal.equals("") || pvaVal.equals(" ")) pvaVal=brand_clickVal.replaceAll("%20", " ").replaceAll("%26", "&");
                                else pvaVal += " > " + brand_clickVal.replaceAll("%20", " ").replaceAll("%26", "&");
                                txtpvahDeptName.setText(pvaVal);
                                llpvahierarchy.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                salesPvAAnalysisWeekArrayList.clear();
//                                    if (geoLeveLDesc.equals("E ZONE")) {
//                                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(0).getLevel();
//
//                                    } else {
                                pvaFirstVisibleItem = salesAnalysisClassArrayList.get(0).getBrandplanClass();
//                                    }
                                salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                                if(!header_value.contains("&brand=" + brandnm.replaceAll(" ", "%20").replaceAll("&", "%26")))
                                {
                                    header_value += "&brand=" + brandnm.replaceAll(" ", "%20").replaceAll("&", "%26");
                                }
                                if (pvaFirstVisibleItem.equals("All")) {
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    all_from_val = "brandClass";
                                    salesPvAAnalysisWeekArrayList.clear();
                                    requestSalesWeekChart(header_value);
                                } else {
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    salesPvAAnalysisWeekArrayList.clear();
                                    requestPvAChartAPI();
                                }
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

    private void requestHeaderAPI(final String fromWhere) {
        String url = " ";
        if (fromWhere.equals(hierarchyList[1])) {
            dept_clickVal = dept_clickVal.replace("%", "%25");
            dept_clickVal = dept_clickVal.replace(" ", "%20").replace("&", "%26");

            url = ConstsCore.web_url + "/v1/display/salesheaderEZNew/" + userId + "?view=" + salesPvA_SegmentClick + "&department=" + dept_clickVal + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&lobId=" + lobId;
        } else if (fromWhere.equals(hierarchyList[2])) {
            categry_clickVal = categry_clickVal.replace("%", "%25");
            categry_clickVal = categry_clickVal.replace(" ", "%20").replace("&", "%26");
            url = ConstsCore.web_url + "/v1/display/salesheaderEZNew/" + userId + "?view=" + salesPvA_SegmentClick + "&category=" + categry_clickVal + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&lobId=" + lobId;
        } else if (fromWhere.equals(hierarchyList[3])) {
            class_clickVal = class_clickVal.replace("%", "%25");
            class_clickVal = class_clickVal.replace(" ", "%20").replace("&", "%26");
            url = ConstsCore.web_url + "/v1/display/salesheaderEZNew/" + userId + "?view=" + salesPvA_SegmentClick + "&class=" + class_clickVal + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&lobId=" + lobId;
        } else if (fromWhere.equals(hierarchyList[4])) {
            brand_clickVal = brand_clickVal.replace("%", "%25");
            brand_clickVal = brand_clickVal.replace(" ", "%20").replace("&", "%26");
            url = ConstsCore.web_url + "/v1/display/salesheaderEZNew/" + userId + "?view=" + salesPvA_SegmentClick + "&brand=" + brand_clickVal + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&lobId=" + lobId;
        } else if (fromWhere.equals("store"))
        {
            region_clickVal = region_clickVal.replace("%", "%25");
            region_clickVal = region_clickVal.replace(" ", "%20").replace("&", "%26");
            url = ConstsCore.web_url + "/v1/display/salesheaderEZNew/" + userId + "?view=" + salesPvA_SegmentClick + "&regionDescription=" + region_clickVal + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&lobId=" + lobId;

        } else if (fromWhere.equals("filter")) {
            url = ConstsCore.web_url + "/v1/display/salesheaderEZNew/" + userId + "?view=" + salesPvA_SegmentClick + selectedString + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&lobId=" + lobId;

        }
        postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        Log.e(TAG, "requestSalesViewPagerValueAPI: " + response);

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

                            if(fromWhere.toString().equals(hierarchyList[1])){
                                requestSalesPvACategoryList(dept_clickVal);
                            }
                            else if(fromWhere.toString().equals(hierarchyList[2])){
                                requestSalesPvAPlanClassListAPI(categry_clickVal);
                            }
                            else if(fromWhere.toString().equals(hierarchyList[3])){
                                requestSalesPvABrandListAPI(class_clickVal);
                            }
                            else if(fromWhere.toString().equals(hierarchyList[4])){
                                requestSalesPvABrandPlanListAPI(brand_clickVal);
                            }
                            else if(fromWhere.toString().equals("store")){
                                requestProductAndLocation(region_clickVal);
                            }
                            else if(fromWhere.toString().equals("filter")){
                                requestSalesSelectedFilterVal(selectedString, sales_filter_level);
                            }

                            /*switch (fromWhere)
                            {
                                case "category":
                                    requestSalesPvACategoryList(dept_clickVal);
                                    break;
                                case "class":
                                    requestSalesPvAPlanClassListAPI(categry_clickVal);
                                    break;
                                case "brand":
                                    requestSalesPvABrandListAPI(class_clickVal);
                                    break;
                                case "brandClass":
                                    requestSalesPvABrandPlanListAPI(brand_clickVal);
                                    break;
                                case "store":
                                    requestProductAndLocation(region_clickVal);
                                    break;
                                case "filter":
                                    requestSalesSelectedFilterVal(selectedString, sales_filter_level);
                                    break;
                            }*/

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


    // API 1.19 for add values for All
    private void requestSalesViewPagerValueAPI() {
        String url;

        url = ConstsCore.web_url + "/v1/display/salesheaderEZNew/" + userId + "?view=" + salesPvA_SegmentClick + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&lobId=" + lobId;

        postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

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
//                            header_value = "";
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

    private void requestSalesSelectedFilterVal(final String selectedString, final int sales_filter_level) {
        String salespva_brandplan_listurl = "";
        if (sales_filter_level != 0) {
            salespva_brandplan_listurl = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + sales_filter_level + selectedString + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&lobId=" + lobId;
        } else {
            salespva_brandplan_listurl = ConstsCore.web_url + "/v1/display/salesDetailEZNew/" + userId + "?view=" + salesPvA_SegmentClick + selectedString + "&geoLevel2Code=" + geoLevel2Code + "&offset=" + offsetvalue + "&limit=" + limit + "&lobId=" + lobId;

        }
        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_brandplan_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        if (sales_filter_level == 2) {
                            txtheaderplanclass.setText(hierarchyList[1]);
                            fromWhere = hierarchyList[1];
                            btnSalesPrev.setVisibility(View.VISIBLE);

                        } else if (sales_filter_level == 3) {
                            txtheaderplanclass.setText(hierarchyList[2]);
                            fromWhere = hierarchyList[2];
                            btnSalesPrev.setVisibility(View.VISIBLE);
                        } else if (sales_filter_level == 4) {
                            txtheaderplanclass.setText(hierarchyList[3]);
                            fromWhere = hierarchyList[3];
                            btnSalesPrev.setVisibility(View.VISIBLE);
                            btnSalesNext.setVisibility(View.INVISIBLE);

                        } else if (sales_filter_level == 9) {
                            txtheaderplanclass.setText("Store");
                            fromWhere = "Store";
                            btnSalesPrev.setVisibility(View.VISIBLE);
                            btnSalesNext.setVisibility(View.INVISIBLE);

                        }


                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {

                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                pva_progressBar.setVisibility(View.GONE);
                                onItemClickFlag = false;

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisListDisplay);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesSelectedFilterVal(selectedString, sales_filter_level);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisListDisplay);
                                }

                                // For Add "All"
                                salesAnalysisListDisplay = new SalesAnalysisListDisplay();


                                //  if(geoLeveLDesc.equals("E ZONE"))
                                {
                                    if (txtheaderplanclass.getText().toString().equals(hierarchyList[0])) {
                                        salesAnalysisListDisplay.setLevel("All");

                                    } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[1]))
                                    {
                                        salesAnalysisListDisplay.setLevel("All");

                                    } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[2])) {
                                        salesAnalysisListDisplay.setLevel("All");


                                    } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[3]))
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

                                salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());

                                salesAnalysisClassArrayList.add(0, salesAnalysisListDisplay);
                                listViewSalesPvA.setLayoutManager(new LinearLayoutManager(
                                        listViewSalesPvA.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                listViewSalesPvA.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(listViewSalesPvA);

                                salesPvAAdapter = new EzonePvASnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listViewSalesPvA, geoLeveLDesc,hierarchyList);
                                listViewSalesPvA.setAdapter(salesPvAAdapter);
                                salesPvAAdapter.notifyDataSetChanged();
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
//                                if(geoLeveLDesc.equals("E ZONE"))
//                                {
                                if (txtheaderplanclass.getText().toString().equals(hierarchyList[0])) {
                                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();

                                } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[1])) {
                                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();

                                } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[2])) {
                                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();

                                } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[3])) {

                                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();

                                }  else if (txtheaderplanclass.getText().toString().equals("Store")) {
                                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getLevel();

                                }


                                header_value = selectedString;

                                salesPvAAnalysisWeekArrayList.clear();
                                if (pvaFirstVisibleItem.equals("All")) {
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    all_from_val = "filter";
                                    salesPvAAnalysisWeekArrayList.clear();
                                    requestSalesWeekChart(header_value);
                                } else {
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    salesPvAAnalysisWeekArrayList.clear();
                                    requestPvAChartAPI();
                                }
                            }
                        }

                        catch (Exception e) {
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
        int checkedId = tab.getPosition();

        switch (checkedId) {

            case 0:
                if (salesPvA_SegmentClick.equals("WTD"))
                    break;
                salesPvA_SegmentClick = "WTD";
                pvaVal = " ";
                llpvahierarchy.setVisibility(View.GONE);
                salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                arrayList.clear();

                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading Data...");
                    pva_progressBar.setVisibility(View.GONE);
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
//                    header_value="";
                    // if you come from filter then tab always be maintain.
                    if (filterSelectedString == null) {
                        if (getIntent().getStringExtra("selectedDept") == null) {
                            requestSalesViewPagerValueAPI();

                        } else if (getIntent().getStringExtra("selectedDept") != null) {
                            String selectedString = getIntent().getStringExtra("selectedDept");
                            requestSalesSelectedFilterVal(selectedString, sales_filter_level);
                        }
                    } else {
                        from_filter = true;
                        //requestSalesViewPagerValueAPI();
                        requestHeaderAPI("filter");
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
                arrayList.clear();

                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading Data...");
                    pva_progressBar.setVisibility(View.GONE);
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
//                    header_value="";
                    // if you come from filter then tab always be maintain.
                    if (filterSelectedString == null) {
                        if (getIntent().getStringExtra("selectedDept") == null) {
                            requestSalesViewPagerValueAPI();

                        } else if (getIntent().getStringExtra("selectedDept") != null) {
                            String selectedString = getIntent().getStringExtra("selectedDept");
                            requestSalesSelectedFilterVal(selectedString, sales_filter_level);
                        }
                    } else {
                        from_filter = true;
                        //requestSalesViewPagerValueAPI();
                        requestHeaderAPI("filter");
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
                selFirstPositionValue =0;
                focusposition=0;

                if (postRequest != null) {
                    postRequest.cancel();
                }
                if (pva_progressBar.getVisibility() == View.VISIBLE) {
                    return;
                }

                if(txtheaderplanclass.getText().toString().equals(hierarchyList[0])){
                    btnSalesPrev.setVisibility(View.VISIBLE);
                    txtheaderplanclass.setText(hierarchyList[1]);
                    fromWhere = hierarchyList[1];
                    level = 2;
                    pvaVal = " ";
//                    header_value= "";
                    salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                    arrayList.clear();
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

                }
                else if(txtheaderplanclass.getText().toString().equals(hierarchyList[1])){
                    fromWhere = hierarchyList[2];
                    txtheaderplanclass.setText(hierarchyList[2]);
                    level = 3;
                    pvaVal = " ";
//                    header_value= "";
                    salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                    arrayList.clear();
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

                }
                else if(txtheaderplanclass.getText().toString().equals(hierarchyList[2])){
                    btnSalesNext.setVisibility(View.INVISIBLE);
                    txtheaderplanclass.setText(hierarchyList[3]);
                    fromWhere = hierarchyList[3];
                    level = 4;
                    pvaVal = " ";
//                    header_value= "";
                    salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                    arrayList.clear();
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

                }
                else if(txtheaderplanclass.getText().toString().equals("Region")){
                    btnSalesNext.setVisibility(View.INVISIBLE);
                    btnSalesPrev.setVisibility(View.VISIBLE);
                    txtheaderplanclass.setText("Store");
                    fromWhere = "Store";
                    level = 9;
                    pvaVal = " ";
//                    header_value= "";
                    salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                    arrayList.clear();
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

                }




                /*switch (txtheaderplanclass.getText().toString()) {
                    case "Department":
                        btnSalesPrev.setVisibility(View.VISIBLE);
                        txtheaderplanclass.setText("Subdept");
                        fromWhere = "Subdept";
                        level = 2;
                        pvaVal = " ";
                        header_value= "";
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        arrayList.clear();
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
                        header_value= "";
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        arrayList.clear();
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
                        header_value= "";
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        arrayList.clear();
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

                    case "Region":
                        btnSalesNext.setVisibility(View.INVISIBLE);
                        btnSalesPrev.setVisibility(View.VISIBLE);
                        txtheaderplanclass.setText("Store");
                        fromWhere = "Store";
                        level = 9;
                        pvaVal = " ";
                        header_value= "";
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        arrayList.clear();
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
                }*/
                break;


            case R.id.btnSalesBack:
                selFirstPositionValue =0;
                focusposition=0;

                if (postRequest != null) {
                    postRequest.cancel();
                }
                if (pva_progressBar.getVisibility() == View.VISIBLE) {
                    return;
                }

                if(txtheaderplanclass.getText().toString().equals(hierarchyList[3])){
                    btnSalesNext.setVisibility(View.VISIBLE);
                    btnSalesPrev.setVisibility(View.VISIBLE);
                    txtheaderplanclass.setText(hierarchyList[2]);
                    fromWhere = hierarchyList[2];
                    level = 3;
                    pvaVal = " ";
//                    header_value= "";
                    salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                    arrayList.clear();
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
                }
                else if(txtheaderplanclass.getText().toString().equals(hierarchyList[2])){
                    btnSalesNext.setVisibility(View.VISIBLE);
                    txtheaderplanclass.setText(hierarchyList[1]);
                    fromWhere = hierarchyList[1];
                    level = 2;
                    pvaVal = " ";
//                    header_value= "";
                    salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                    arrayList.clear();
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

                }

                else if(txtheaderplanclass.getText().toString().equals(hierarchyList[1])){
                    btnSalesPrev.setVisibility(View.INVISIBLE);
                    btnSalesNext.setVisibility(View.VISIBLE);
                    txtheaderplanclass.setText(hierarchyList[0]);
                    fromWhere = hierarchyList[0];
                    level = 1;
                    pvaVal = " ";
//                    header_value= "";
                    salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                    arrayList.clear();
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

                }
                else if(txtheaderplanclass.getText().toString().equals("Store")){
                    btnSalesPrev.setVisibility(View.INVISIBLE);
                    btnSalesNext.setVisibility(View.VISIBLE);
                    txtheaderplanclass.setText("Region");
                    fromWhere = "Region";
                    level = 7;
                    pvaVal = " ";
//                    header_value= "";
                    salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                    arrayList.clear();
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
                }






               /* switch (txtheaderplanclass.getText().toString()) {
                    case "Subclass":
                        btnSalesNext.setVisibility(View.VISIBLE);
                        btnSalesPrev.setVisibility(View.VISIBLE);
                        txtheaderplanclass.setText("Class");
                        fromWhere = "Class";
                        level = 3;
                        pvaVal = " ";
                        header_value= "";
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        arrayList.clear();
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
                        header_value= "";
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        arrayList.clear();
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
                        header_value= "";
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        arrayList.clear();
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
                        header_value= "";
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        arrayList.clear();
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
                }*/
                break;

        }
    }
}

