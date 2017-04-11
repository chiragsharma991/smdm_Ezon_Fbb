package apsupportapp.aperotechnologies.com.designapp.PvaSalesAnalysis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
//import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
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
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesAnalysisSnapAdapter;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesFilterActivity;
import apsupportapp.aperotechnologies.com.designapp.model.RecyclerItemClickListener;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisListDisplay;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisViewPagerValue;
import apsupportapp.aperotechnologies.com.designapp.model.SalesPvAAnalysisWeek;
import info.hoang8f.android.segmented.SegmentedGroup;


public class SalesPvAActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {


    RadioButton btn_WTD,btn_LW;
    private static String salesPvA_SegmentClick = "WTD";
    ArrayList<SalesAnalysisListDisplay> salesAnalysisClassArrayList;
    RelativeLayout tableRelLayout;
    LinearLayout llayoutSalesPvA, llpvahierarchy;
    TextView txtStoreCode, txtStoreDesc;
    String userId, bearertoken;
    SharedPreferences sharedPreferences;
    int offsetvalue = 0, limit = 100;
    int count = 0;
    RequestQueue queue;
    PvASnapAdapter salesPvAAdapter;
    ViewPortHandler handler;
    Context context;
    LineChart lineChart;
    RecyclerView listViewSalesPvA;
    int focusposition, selFirstPositionValue = 0, totalItemCount;
    private static int level = 1;
    SalesAnalysisListDisplay salesAnalysisListDisplay;
    SalesPvAAnalysisWeek salesPvAAnalysisWeek;
    ArrayList<SalesPvAAnalysisWeek> salesPvAAnalysisWeekArrayList;
    String fromWhere, txtPvAClickedValue;
    TextView txtheaderplanclass;
    RelativeLayout btnBack, btnFilter;
    RelativeLayout btnSalesPrev, btnSalesNext;
    Gson gson;
    String pvaFirstVisibleItem;
    JsonArrayRequest postRequest;
    ArrayList<SalesAnalysisViewPagerValue> arrayList;
    static SalesAnalysisViewPagerValue salesAnalysisViewPagerValue;
    static String planDept, planCategory, planClass;
    TextView txtpvahDeptName,txt_pva_noChart;
    String pvaVal, TAG = "SalesPvAActivity";
    int currentIndex;
    int prevState = RecyclerView.SCROLL_STATE_IDLE;
    int currentState = RecyclerView.SCROLL_STATE_IDLE;
    boolean flag = false, onItemClickFlag = false,filter_toggleClick = false;
    ProgressBar pva_progressBar;
    public static Activity Sales_Pva_Activity;
    float planSaleNetVal1,saleNetVal1;
    boolean plansalenetflag = false,salenetflag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salespva);
        fromWhere = "Department";
        focusposition = 0;
        context = this;
        Sales_Pva_Activity = this;

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int densityDpi = (int) (metrics.density * 160f);

        Log.e("densityDPi---", "" + densityDpi);
        // Display device width and height in pixels
        int screenHeightpx = metrics.heightPixels;
        int screenWidthpx = metrics.widthPixels;

        Log.e("screen height--", "" + screenHeightpx + "screen width" + screenWidthpx);

        getSupportActionBar().hide();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();
        txtStoreCode = (TextView) findViewById(R.id.txtStoreCode);
        txtStoreDesc = (TextView) findViewById(R.id.txtStoreName);
        txt_pva_noChart = (TextView)findViewById(R.id.pva_noChart);
        //hierarchy header
        txtpvahDeptName = (TextView) findViewById(R.id.txtpvahDeptName);
        pvaVal = " ";
        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
        salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
        arrayList = new ArrayList<SalesAnalysisViewPagerValue>();
        btn_WTD = (RadioButton) findViewById(R.id.btn_wtd);
        btn_LW = (RadioButton) findViewById(R.id.btn_lw);
       // radioButton.toggle();
        llayoutSalesPvA = (LinearLayout) findViewById(R.id.llayoutSalesPvA);
        //llayoutSalesPvA.setVisibility(View.GONE);
        llpvahierarchy = (LinearLayout) findViewById(R.id.llpvahierarchy);
        llpvahierarchy.setOrientation(LinearLayout.HORIZONTAL);
        tableRelLayout = (RelativeLayout) findViewById(R.id.relTablelayout);
        //  relChartLayout = (RelativeLayout) findViewById(R.id.relChartlayout);
        pva_progressBar = (ProgressBar) findViewById(R.id.pva_progressBar);
        lineChart = (LineChart) findViewById(R.id.linechart);
        LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setDrawZeroLine(true);
        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(false);
        lineChart.getAxisRight().setEnabled(false);
        Legend l = lineChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        l.setEnabled(true);
        handler = lineChart.getViewPortHandler();
        Log.e("scale x---", "" + handler.getScaleX());
        SegmentedGroup segmented3 = (SegmentedGroup) findViewById(R.id.segmented3);
        segmented3.setOnCheckedChangeListener(SalesPvAActivity.this);
        //  initializeValues(context);
        txtheaderplanclass = (TextView) findViewById(R.id.txtPlanClass);
        Log.e("text value", "" + txtheaderplanclass.getText().toString());

        listViewSalesPvA = (RecyclerView) findViewById(R.id.list);
//        footer = getLayoutInflater().inflate(R.layout.list_footer, null);
//        listViewSalesPvA.addFooterView(footer);
        if (Reusable_Functions.chkStatus(context)) {
            // Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            pva_progressBar.setVisibility(View.GONE);
            offsetvalue = 0;
            limit = 100;
            count = 0;
            level = 1;
            llpvahierarchy.setVisibility(View.GONE);
            //llayoutSalesPvA.setVisibility(View.GONE);
            if (getIntent().getStringExtra("selectedDept") == null) {
                filter_toggleClick = false;
                retainSegmentValuesFilter();
                requestSalesViewPagerValueAPI();
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    public void run() {
                        requestSalesListDisplayAPI();
                    }
                }, 700);

            }
            else if(getIntent().getStringExtra("selectedDept") != null) {
                String  selectedString  = getIntent().getStringExtra("selectedDept");
                filter_toggleClick = true;
                retainSegmentValuesFilter();
                requestSalesSelectedFilterVal(selectedString);
            }




        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }

        btnSalesPrev = (RelativeLayout) findViewById(R.id.btnSalesBack);
        btnSalesPrev.setVisibility(View.INVISIBLE);
        btnSalesPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(postRequest!=null)
                {
                    postRequest.cancel();
                }
                switch (txtheaderplanclass.getText().toString()) {

                    case "MC":
                        btnSalesNext.setVisibility(View.VISIBLE);
                        txtheaderplanclass.setText("Subclass");
                        fromWhere = "Subclass";
                      //  flag = false;
                        level = 4;
                        pvaVal = " ";
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        listViewSalesPvA.removeAllViews();
                        llpvahierarchy.setVisibility(View.GONE);
                        // llayoutSalesPvA.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            pva_progressBar.setVisibility(View.GONE);
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.e("Brand Plan Class Prev-- ", "  ");
                            requestSalesViewPagerValueAPI();
                            Handler h = new Handler();
                            h.postDelayed(new Runnable() {
                                public void run() {
                                    requestSalesListDisplayAPI();
                                }
                            }, 700);
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case "Subclass":
                        txtheaderplanclass.setText("Class");
                        fromWhere = "Class";
                        level = 3;
                        pvaVal = " ";
                        // flag = false;
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        listViewSalesPvA.removeAllViews();
                        llpvahierarchy.setVisibility(View.GONE);
                        // llayoutSalesPvA.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            pva_progressBar.setVisibility(View.GONE);
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.e("Brand name prev", "--");
                            requestSalesViewPagerValueAPI();
                            Handler h = new Handler();
                            h.postDelayed(new Runnable() {
                                public void run() {
                                    requestSalesListDisplayAPI();
                                }
                            }, 700);
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("---2---", " ");
                        break;

                    case "Class":
                        txtheaderplanclass.setText("Subdept");
                        fromWhere = "Subdept";
                        level = 2;
                        pvaVal = " ";
                        // flag = false;
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        listViewSalesPvA.removeAllViews();
                        llpvahierarchy.setVisibility(View.GONE);
                        //  llayoutSalesPvA.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            pva_progressBar.setVisibility(View.GONE);
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.e("Plan class prev", "");
                            requestSalesViewPagerValueAPI();
                            Handler h = new Handler();
                            h.postDelayed(new Runnable() {
                                public void run() {
                                    requestSalesListDisplayAPI();
                                }
                            }, 700);

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("---3---", " ");

                        break;

                    case "Subdept":
                        btnSalesPrev.setVisibility(View.INVISIBLE);
                        txtheaderplanclass.setText("Department");
                        fromWhere = "Department";
                        level = 1;
                        pvaVal = " ";
                        //  flag = false;
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        listViewSalesPvA.removeAllViews();
                        llpvahierarchy.setVisibility(View.GONE);
                        //  llayoutSalesPvA.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            pva_progressBar.setVisibility(View.GONE);
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.e("Category prev", "");
                            requestSalesViewPagerValueAPI();
                            Handler h = new Handler();
                            h.postDelayed(new Runnable() {
                                public void run() {
                                    requestSalesListDisplayAPI();
                                }
                            }, 700);

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("---4---", " ");

                        break;
                    default:
                }

            }

        });

        btnSalesNext = (RelativeLayout) findViewById(R.id.btnSalesNext);
        btnSalesNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(postRequest!=null)
                {
                    postRequest.cancel();
                }
                switch (txtheaderplanclass.getText().toString()) {

                    case "Department":
                        btnSalesPrev.setVisibility(View.VISIBLE);
                        txtheaderplanclass.setText("Subdept");
                        fromWhere = "Subdept";
                        level = 2;
                        pvaVal = " ";
                        //  flag = false;
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        listViewSalesPvA.removeAllViews();
                        // relChartLayout.setVisibility(View.GONE);
                        llpvahierarchy.setVisibility(View.GONE);
                        //     llayoutSalesPvA.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            pva_progressBar.setVisibility(View.GONE);
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.i("dept next", "-----");
                            requestSalesViewPagerValueAPI();
                            Handler h = new Handler();
                            h.postDelayed(new Runnable() {
                                public void run() {
                                    requestSalesListDisplayAPI();
                                }
                            }, 700);
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case "Subdept":
                        fromWhere = "Class";
                        txtheaderplanclass.setText("Class");
                      //  flag = false;
                        level = 3;
                        pvaVal = " ";
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        listViewSalesPvA.removeAllViews();
                        llpvahierarchy.setVisibility(View.GONE);
                        // relChartLayout.setVisibility(View.GONE);
                        //   llayoutSalesPvA.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            pva_progressBar.setVisibility(View.GONE);
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.e("category next --", "");
                            requestSalesViewPagerValueAPI();
                            Handler h = new Handler();
                            h.postDelayed(new Runnable() {
                                public void run() {
                                    requestSalesListDisplayAPI();
                                }
                            }, 700);
                            Log.e("next 2", "" + salesAnalysisListDisplay.getPlanClass());

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("---2---", " ");

                        break;
                    case "Class":
                        txtheaderplanclass.setText("Subclass");
                        fromWhere = "Subclass";
                      //  flag = false;
                        level = 4;
                        pvaVal = " ";
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        listViewSalesPvA.removeAllViews();
                        llpvahierarchy.setVisibility(View.GONE);
                        // relChartLayout.setVisibility(View.GONE);
                        //  llayoutSalesPvA.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            pva_progressBar.setVisibility(View.GONE);
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            requestSalesViewPagerValueAPI();
                            Handler h = new Handler();
                            h.postDelayed(new Runnable() {
                                public void run() {
                                    requestSalesListDisplayAPI();
                                }
                            }, 700);

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("---3--", " ");

                        break;

                    case "Subclass":
                        btnSalesNext.setVisibility(View.INVISIBLE);

                        txtheaderplanclass.setText("MC");
                        fromWhere = "MC";
                      //  flag = false;
                        level = 5;
                        pvaVal = " ";
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        listViewSalesPvA.removeAllViews();
                        llpvahierarchy.setVisibility(View.GONE);
                        // relChartLayout.setVisibility(View.GONE);
                        // llayoutSalesPvA.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            pva_progressBar.setVisibility(View.GONE);
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            requestSalesViewPagerValueAPI();
                            Handler h = new Handler();
                            h.postDelayed(new Runnable() {
                                public void run() {
                                    requestSalesListDisplayAPI();
                                }
                            }, 700);
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("---4---", " ");

                        break;
                    default:
                }
            }
        });


        listViewSalesPvA.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                RecyclerViewPositionHelper mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
                int visibleItemCount = recyclerView.getChildCount();
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
                            if(onItemClickFlag == false) {
                                TimeUP();
                            }
                        }
                    }, 700);
                }
                prevState = currentState;

            }
        });

        //Drill Down
        listViewSalesPvA.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        onItemClickFlag = true;
                       // Reusable_Functions.sDialog(context, "Loading data...");
                        Handler h = new Handler();
                        h.postDelayed(new Runnable() {
                            public void run() {
                                if (position < salesAnalysisClassArrayList.size()) {


                                    switch (txtheaderplanclass.getText().toString()) {
                                        case "Department":
                                            btnSalesPrev.setVisibility(View.VISIBLE);
                                            txtheaderplanclass.setText("Subdept");
                                            txtPvAClickedValue = salesAnalysisClassArrayList.get(position).getPlanDept();
                                            Log.e("txtClicked department--", "" + txtPvAClickedValue);
                                            //  llayoutSalesPvA.setVisibility(View.GONE);
                                            fromWhere = "Subdept";
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
                                                Log.i("dept next", "-----");
                                                requestSalesPvACategoryList(txtPvAClickedValue);
                                                planDept = txtPvAClickedValue;

                                            } else {
                                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                            }
                                            break;

                                        case "Subdept":
//                                            if (flag == true) {
                                                txtheaderplanclass.setText("Class");
                                                //     llayoutSalesPvA.setVisibility(View.GONE);
                                                txtPvAClickedValue = salesAnalysisClassArrayList.get(position).getPlanCategory();
                                                Log.e("txtClicked category --", "" + txtPvAClickedValue);
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
                                                    Log.i("category next", "-----");
                                                    Log.i("come", "----" + planDept);
                                                    requestSalesPvAPlanClassListAPI(txtPvAClickedValue);
                                                    planCategory = txtPvAClickedValue;
                                                    Log.e("planCategory--", "" + planCategory);
                                                } else {
                                                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                                }
//                                            } else {
//                                                Reusable_Functions.hDialog();
//                                                Toast.makeText(context, "Please Select dept name..", Toast.LENGTH_SHORT).show();
//                                            }

                                            break;
                                        case "Class":
                                         //   if (flag == true) {
                                                txtheaderplanclass.setText("Subclass");
                                                //llayoutSalesPvA.setVisibility(View.GONE);
                                                txtPvAClickedValue = salesAnalysisClassArrayList.get(position).getPlanClass();
                                                Log.e("txtClicked plan class---", "" + txtPvAClickedValue);
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
                                                    Log.i("Plan Class next", "-----");
                                                    requestSalesPvABrandListAPI(txtPvAClickedValue);
                                                    planClass = txtPvAClickedValue;
                                                    Log.e("planClass---", "" + planClass);
                                                } else {
                                                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                                }
//                                            } else {
//                                                Reusable_Functions.hDialog();
//                                                Toast.makeText(context, "Please Select dept name..", Toast.LENGTH_SHORT).show();
//                                            }
                                            break;
                                        case "Subclass":
                                            Log.e("in sales pva brand ", "-----" + planDept);

                                           // if (flag == true) {
                                                btnSalesNext.setVisibility(View.INVISIBLE);
                                                txtheaderplanclass.setText("MC");
                                                //llayoutSalesPvA.setVisibility(View.GONE);
                                                txtPvAClickedValue = salesAnalysisClassArrayList.get(position).getBrandName();
                                                Log.e("txtSalesClickedValue3---", "" + txtPvAClickedValue);
                                                fromWhere = "MC";
                                                level = 5;
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
                                                    Log.i("brand next", "-----");
                                                    requestSalesPvABrandPlanListAPI(txtPvAClickedValue);

                                                } else {
                                                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                                }
//                                            } else {
//                                                Reusable_Functions.hDialog();
//                                                Toast.makeText(context, "Please Select dept name..", Toast.LENGTH_SHORT).show();
//                                            }

                                            break;

                                        default:
                                            Reusable_Functions.hDialog();
                                            Toast.makeText(context, " You are at the last level of hierarchy", Toast.LENGTH_SHORT).show();
                                            onItemClickFlag = false;
                                            break;

                                    }
                                }
                            }
                        }, 700);
                    }
                }));

        btnBack = (RelativeLayout) findViewById(R.id.imageBtnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(SalesPvAActivity.this, DashBoardActivity.class);
                startActivity(intent);*/
             onBackPressed();
            }
        });

        btnFilter = (RelativeLayout) findViewById(R.id.imgfilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent filterIntent = new Intent(SalesPvAActivity.this, SalesFilterActivity.class);
                filterIntent.putExtra("checkfrom", "pvaAnalysis");
                startActivity(filterIntent);
            }
        });
    }

    private void retainSegmentValuesFilter() {
        filter_toggleClick = true;
        if (salesPvA_SegmentClick.equals("WTD")) {
            btn_WTD.toggle();
            Log.e(TAG, "WTD toggle: ");


        } else if (salesPvA_SegmentClick.equals("LW")) {
            btn_LW.toggle();
            Log.e(TAG, "LW toggle: ");

        }

    }

    private void TimeUP() {


        if (focusposition < salesAnalysisClassArrayList.size() && onItemClickFlag == false) {

            if (txtheaderplanclass.getText().toString().equals("Department")) {
                level = 1;
                pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanDept().toString();
            } else if (txtheaderplanclass.getText().toString().equals("Subdept")) {
                level = 2;
                pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanCategory().toString();
            } else if (txtheaderplanclass.getText().toString().equals("Class")) {
                level = 3;
                pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanClass().toString();
            } else if (txtheaderplanclass.getText().toString().equals("Subclass")) {
                level = 4;
                pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getBrandName().toString();
            } else if (txtheaderplanclass.getText().toString().equals("MC")) {
                level = 5;
                pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getBrandplanClass().toString();
            }

            if (Reusable_Functions.chkStatus(context)) {
                Reusable_Functions.hDialog();
                //  Reusable_Functions.sDialog(context, "Loading data...");
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
                        lineChart.invalidate();
                        lineChart.setScaleEnabled(false);
                        requestSalesWeekChart();

                    } else {
                        salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                        lineChart.invalidate();
                        lineChart.setScaleEnabled(false);
                        requestPvAChartAPI();
                    }
                    selFirstPositionValue = focusposition;
                }

            } else {
                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
            }

        } else {
            focusposition = salesAnalysisClassArrayList.size()-1 ;
            LinearLayoutManager llm = (LinearLayoutManager) listViewSalesPvA.getLayoutManager();
            llm.scrollToPosition(focusposition);

            if (txtheaderplanclass.getText().toString().equals("Department")) {
                level = 1;
                pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanDept().toString();
            } else if (txtheaderplanclass.getText().toString().equals("Subdept")) {
                level = 2;
                pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanCategory().toString();
            } else if (txtheaderplanclass.getText().toString().equals("Class")) {
                level = 3;
                pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanClass().toString();
            } else if (txtheaderplanclass.getText().toString().equals("Subclass")) {
                level = 4;
                pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getBrandName().toString();
            } else if (txtheaderplanclass.getText().toString().equals("MC")) {
                level = 5;
                pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getBrandplanClass().toString();
            }
            if (Reusable_Functions.chkStatus(context)) {
                Reusable_Functions.hDialog();
                //Reusable_Functions.sDialog(context, "Loading data...");

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
                        lineChart.invalidate();
                        lineChart.setScaleEnabled(false);
                        requestSalesWeekChart();

                    } else {
                        salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                        lineChart.invalidate();
                        lineChart.setScaleEnabled(false);
                        requestPvAChartAPI();

                    }
                    selFirstPositionValue = focusposition;

                }

            } else {
                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private ArrayList<Entry> setYAxisValues() {
        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < salesPvAAnalysisWeekArrayList.size(); i++) {

            float val = (float) salesPvAAnalysisWeekArrayList.get(i).getPvaAchieved();
            Log.e("val", "" + val);
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


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
       if(filter_toggleClick == false) {
           switch (checkedId) {

               case R.id.btn_wtd:
                   if (salesPvA_SegmentClick.equals("WTD"))
                       break;
                   salesPvA_SegmentClick = "WTD";
                   pvaVal = " ";
                   llpvahierarchy.setVisibility(View.GONE);
                   // llayoutSalesPvA.setVisibility(View.GONE);
                   salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                   if (Reusable_Functions.chkStatus(context)) {
                       Reusable_Functions.hDialog();
                       Reusable_Functions.sDialog(context, "Loading Data...");
                       pva_progressBar.setVisibility(View.GONE);
                       offsetvalue = 0;
                       limit = 100;
                       count = 0;
                       Log.e("focuspos in WTD",""+focusposition);
                      if (getIntent().getStringExtra("selectedDept") == null) {
                           requestSalesViewPagerValueAPI();
                           Handler h = new Handler();
                           h.postDelayed(new Runnable() {
                               public void run() {
                                   requestSalesListDisplayAPI();
                               }
                           }, 700);

                       } else if (getIntent().getStringExtra("selectedDept") != null) {
                           String selectedString = getIntent().getStringExtra("selectedDept");
                           requestSalesSelectedFilterVal(selectedString);
                       }
                   } else {
                       Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                   }
                   Log.e("-----WTD-----", " ");
                   break;

               case R.id.btn_lw:

                   if (salesPvA_SegmentClick.equals("LW"))
                       break;

                   salesPvA_SegmentClick = "LW";
                   pvaVal = " ";
                   llpvahierarchy.setVisibility(View.GONE);
                   // llayoutSalesPvA.setVisibility(View.GONE);
                   salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                   if (Reusable_Functions.chkStatus(context)) {
                       Reusable_Functions.hDialog();
                       Reusable_Functions.sDialog(context, "Loading Data...");
                       pva_progressBar.setVisibility(View.GONE);
                       offsetvalue = 0;
                       limit = 100;
                       count = 0;
                       Log.e("focuspos in LW",""+focusposition);

                       if (getIntent().getStringExtra("selectedDept") == null) {
                           requestSalesViewPagerValueAPI();
                           Handler h = new Handler();
                           h.postDelayed(new Runnable() {
                               public void run() {
                                   requestSalesListDisplayAPI();
                               }
                           }, 700);

                       } else if (getIntent().getStringExtra("selectedDept") != null) {
                           String selectedString = getIntent().getStringExtra("selectedDept");
                           requestSalesSelectedFilterVal(selectedString);
                       }

                   } else {
                       Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                   }
                   Log.e("-----lw-----", " ");
                   break;

               default:
                   break;
           }
       }
        else
       {
           filter_toggleClick = false;
       }
    }


    // API 1.20
    private void requestSalesListDisplayAPI() {
        String salespva_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + salespva_listurl);

        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Sales Analysis Class: ", " " + response);
                        Log.i("response length", "" + response.length());

                        try {
                            int i;
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                pva_progressBar.setVisibility(View.GONE);
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
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

                                if (txtheaderplanclass.getText().toString().equals("Department")) {
                                    salesAnalysisListDisplay.setPlanDept("All");
                                    salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                    salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                    salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());

                                } else if (txtheaderplanclass.getText().toString().equals("Subdept")) {
                                    salesAnalysisListDisplay.setPlanCategory("All");
                                    salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                    salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                    salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());

                                } else if (txtheaderplanclass.getText().toString().equals("Class")) {
                                    salesAnalysisListDisplay.setPlanClass("All");
                                    salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                    salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                    salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());

                                } else if (txtheaderplanclass.getText().toString().equals("Subclass")) {
                                    salesAnalysisListDisplay.setBrandName("All");
                                    salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                    salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                    salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());

                                } else if (txtheaderplanclass.getText().toString().equals("MC")) {
                                    salesAnalysisListDisplay.setBrandplanClass("All");
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

                                salesPvAAdapter = new PvASnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listViewSalesPvA);
                                listViewSalesPvA.setAdapter(salesPvAAdapter);
                                txtStoreCode.setText(salesAnalysisClassArrayList.get(i).getStoreCode());
                                txtStoreDesc.setText(salesAnalysisClassArrayList.get(i).getStoreDesc());

                                // Retain values....
                                if (txtheaderplanclass.getText().toString().equals("Department")) {
                                    for (int j = 0; j < salesAnalysisClassArrayList.size(); j++) {
                                        Log.e("In for loop","----");
                                        level = 1;
                                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanDept().toString();
                                        if (salesAnalysisClassArrayList.get(focusposition).getPlanDept().equals(pvaFirstVisibleItem)) {
                                            Log.e("j position :", "" + j + "firstVisibleItem pos :" + focusposition);
                                            listViewSalesPvA.getLayoutManager().scrollToPosition(focusposition);
                                        }
                                    }
                                } else if (txtheaderplanclass.getText().toString().equals("Subdept")) {
                                    for (int j = 0; j < salesAnalysisClassArrayList.size(); j++) {
                                        level = 2;
                                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanCategory().toString();
                                        if (salesAnalysisClassArrayList.get(j).getPlanCategory().equals(pvaFirstVisibleItem)) {
                                            listViewSalesPvA.getLayoutManager().scrollToPosition(focusposition);
                                        }
                                    }
                                } else if (txtheaderplanclass.getText().toString().equals("Class")) {
                                    for (int j = 0; j < salesAnalysisClassArrayList.size(); j++) {
                                        level = 3;
                                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanClass().toString();
                                        if (salesAnalysisClassArrayList.get(j).getPlanClass().equals(pvaFirstVisibleItem)) {
                                            listViewSalesPvA.getLayoutManager().scrollToPosition(focusposition);

                                        }
                                    }
                                } else if (txtheaderplanclass.getText().toString().equals("Subclass")) {
                                    for (int j = 0; j < salesAnalysisClassArrayList.size(); j++) {

                                        level = 4;
                                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getBrandName().toString();
                                        if (salesAnalysisClassArrayList.get(j).getBrandName().equals(pvaFirstVisibleItem)) {
                                            listViewSalesPvA.getLayoutManager().scrollToPosition(focusposition);

                                        }
                                    }
                                } else if (txtheaderplanclass.getText().toString().equals("MC")) {
                                    for (int j = 0; j < salesAnalysisClassArrayList.size(); j++) {

                                        level = 5;
                                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getBrandplanClass().toString();
                                        if (salesAnalysisClassArrayList.get(j).getBrandplanClass().equals(pvaFirstVisibleItem)) {
                                            listViewSalesPvA.getLayoutManager().scrollToPosition(focusposition);

                                        }
                                    }
                                }
                                if (pvaFirstVisibleItem.equals("All")) {
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;

                                   // flag = false;
                                    llpvahierarchy.setVisibility(View.GONE);
                                    salesPvAAnalysisWeekArrayList.clear();
                                    requestSalesWeekChart();

                                } else {
                                   // flag = false;
                                    llpvahierarchy.setVisibility(View.GONE);
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    salesPvAAnalysisWeekArrayList.clear();
                                    requestPvAChartAPI();
                                }

//                                offsetvalue = 0;
//                                limit = 100;
//                                count = 0;
//                                salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
//                                requestSalesWeekChart();
                                //llayoutSalesPvA.setVisibility(View.VISIBLE);
                                // Reusable_Functions.hDialog();
                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
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
        Log.e("Department onsroll api", "" + pvaFirstVisibleItem);
        String url = "";
        pvaFirstVisibleItem = pvaFirstVisibleItem.replace("%","%25");
        pvaFirstVisibleItem = pvaFirstVisibleItem.replace(" ","%20").replace("&","%26");

        if (txtheaderplanclass.getText().toString().equals("Department")) {

            url = ConstsCore.web_url + "/v1/display/salesvisualpvaanalysisbyweek/" + userId + "?department=" + pvaFirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit;
        } else if (txtheaderplanclass.getText().toString().equals("Subdept")) {

            url = ConstsCore.web_url + "/v1/display/salesvisualpvaanalysisbyweek/" + userId + "?category=" + pvaFirstVisibleItem+ "&offset=" + offsetvalue + "&limit=" + limit;
        } else if (txtheaderplanclass.getText().toString().equals("Class")) {

            url = ConstsCore.web_url + "/v1/display/salesvisualpvaanalysisbyweek/" + userId + "?class=" + pvaFirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit;
        } else if (txtheaderplanclass.getText().toString().equals("Subclass")) {

            url = ConstsCore.web_url + "/v1/display/salesvisualpvaanalysisbyweek/" + userId + "?brand=" + pvaFirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit;
        } else if (txtheaderplanclass.getText().toString().equals("MC")) {

            url = ConstsCore.web_url + "/v1/display/salesvisualpvaanalysisbyweek/" + userId + "?brandclass=" + pvaFirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit;
        }
        Log.e("Url", "" + url);

        postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Sales Pva Chart on Scroll  : ", " " + response);
                        Log.e("Sales Pva Chart response", "" + response.length());
                        try {

                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                               // txt_pva_noChart.setVisibility(View.VISIBLE);
                                 lineChart.setData(null);
                                lineChart.notifyDataSetChanged();
                                lineChart.invalidate();
                                pva_progressBar.setVisibility(View.GONE);
                                onItemClickFlag = false;
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();

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
                                Log.e("Array Size", "" + salesPvAAnalysisWeekArrayList.size());
                                ArrayList<String> xVals = setXAxisValues();
                                ArrayList<Entry> yVal_select_planSale = new ArrayList<Entry>();
                                for (int j = 0; j < salesPvAAnalysisWeekArrayList.size(); j++) {
                                     planSaleNetVal1 = (float) salesPvAAnalysisWeekArrayList.get(j).getPlanSaleNetVal();
                                    yVal_select_planSale.add(new Entry(j, planSaleNetVal1));
                                }
                                ArrayList<Entry> yVal_select_netSale = new ArrayList<Entry>();
                                for (int j = 0; j < salesPvAAnalysisWeekArrayList.size(); j++) {
                                    saleNetVal1 = (float) salesPvAAnalysisWeekArrayList.get(j).getSaleNetVal();
                                    yVal_select_netSale.add(new Entry(j, saleNetVal1));
                                }
                              //  txt_pva_noChart.setVisibility(View.GONE);
                                LineDataSet lineDataSet1, lineDataSet2;
                                lineDataSet1 = new LineDataSet(yVal_select_planSale, "Plan Sales");
                                lineDataSet1.setDrawValues(false);
                                lineDataSet2 = new LineDataSet(yVal_select_netSale, "Net Sales");
                                lineDataSet2.setDrawValues(false);
                                //set the line to be drawn like this "- - - - - -"
                                //set1.enableDashedLine(10f, 5f, 0f);
                                //set1.enableDashedHighlightLine(10f, 5f, 0f);
                                lineDataSet1.setColor(Color.parseColor("#198c19"));
                                lineDataSet1.setCircleColor(Color.parseColor("#99cc99"));
                                //set1.setCircleColor(Color.BLACK);
                                lineDataSet1.setLineWidth(1f);
                                lineDataSet1.setCircleRadius(3f);
                                lineDataSet1.setDrawCircleHole(false);
                                lineDataSet1.setValueTextSize(9f);
                                lineDataSet1.setDrawFilled(false);
                                lineDataSet1.setFormLineWidth(1f);
                                //set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                                lineDataSet1.setFormSize(15.f);
                                lineDataSet2.setColor(Color.MAGENTA);
                                lineDataSet2.setCircleColor(Color.parseColor("#ffccff"));
                                //set1.setCircleColor(Color.BLACK);
                                lineDataSet2.setLineWidth(1f);
                                lineDataSet2.setCircleRadius(3f);
                                lineDataSet2.setDrawCircleHole(false);
                                lineDataSet2.setValueTextSize(9f);
                                lineDataSet2.setDrawFilled(false);
                                lineDataSet2.setFormLineWidth(1f);
                                //set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                                lineDataSet2.setFormSize(15.f);
//                              LineDataSet dataSets = new LineDataSet(yVals,"");
//                              dataSets.add(set1); // add the datasets
                                //create a data object with the datasets
                                LineData data = new LineData(lineDataSet1, lineDataSet2);
                                //set data
                                //create a data object with the datasets
                                LineData lineChartdata = new LineData(lineDataSet1, lineDataSet2);
                                Log.e("lineDataSet", "" + lineDataSet1.getLabel());
                                //lineChartdata.addDataSet(lineDataSet);
                                //set data
                                //lineChart = (LineChart)findViewById(R.id.linechart);
                                lineChart.setData(lineChartdata);
                                lineChart.notifyDataSetChanged();
                                lineChart.invalidate();
                                lineChart.getAxisLeft().setDrawGridLines(false);
                                lineChart.getXAxis().setDrawGridLines(false);
                                lineChart.setDescription(null);
                                //lineChart.addView(lineChart);
                                Log.e("lineChartData", "" + lineChartdata.getDataSetCount());
                                lineChart.setTouchEnabled(false);
                                lineChart.setScaleEnabled(false);
                                onItemClickFlag=false;
                                pva_progressBar.setVisibility(View.GONE);
                                //llayoutSalesPvA.setVisibility(View.VISIBLE);
                                Reusable_Functions.hDialog();
                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            onItemClickFlag=false;
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
                        onItemClickFlag=false;
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
        String salespvaweekChart_url = ConstsCore.web_url + "/v1/display/salesvisualpvaanalysisbyweek/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + salespvaweekChart_url);
        postRequest = new JsonArrayRequest(Request.Method.GET, salespvaweekChart_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Sales Week Chart: ", " " + response);
                        Log.e("Sales PvA Week response length", "" + response.length());
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                lineChart.setData(null);
                                lineChart.notifyDataSetChanged();
                                lineChart.invalidate();
                               // txt_pva_noChart.setVisibility(View.VISIBLE);
                                pva_progressBar.setVisibility(View.GONE);
                                onItemClickFlag = false;
                                Toast.makeText(context, "no category data found", Toast.LENGTH_SHORT).show();
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
                                ArrayList<String> xVals = setXAxisValues();
                                ArrayList<Entry> yVals_PlanSale = new ArrayList<Entry>();
                                for (int j = 0; j < salesPvAAnalysisWeekArrayList.size(); j++) {
                                    float planSaleVal = (float) salesPvAAnalysisWeekArrayList.get(j).getPlanSaleNetVal();
                                    yVals_PlanSale.add(new Entry(j, planSaleVal));
                                }
                                ArrayList<Entry> yVals_SaleNet = new ArrayList<Entry>();
                                for (int j = 0; j < salesPvAAnalysisWeekArrayList.size(); j++) {
                                    float saleNetVal = (float) salesPvAAnalysisWeekArrayList.get(j).getSaleNetVal();
                                    yVals_SaleNet.add(new Entry(j, saleNetVal));
                                }
                              //  txt_pva_noChart.setVisibility(View.GONE);
                                LineDataSet set1, set2;
                                set1 = new LineDataSet(yVals_PlanSale, "Plan Sales");
                                set1.setDrawValues(false);
                                set2 = new LineDataSet(yVals_SaleNet, "Net Sales");
                                set2.setDrawValues(false);
                                //set the line to be drawn like this "- - - - - -"
                                //set1.enableDashedLine(10f, 5f, 0f);
                                //set1.enableDashedHighlightLine(10f, 5f, 0f);
                                set1.setColor(Color.parseColor("#198c19"));
                                set1.setCircleColor(Color.parseColor("#99cc99"));
                                //set1.setCircleColor(Color.BLACK);
                                set1.setLineWidth(1f);
                                set1.setCircleRadius(3f);
                                set1.setDrawCircleHole(false);
                                set1.setValueTextSize(9f);
                                set1.setDrawFilled(false);
                                set1.setFormLineWidth(1f);
                                //  set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                                set1.setFormSize(15.f);
                                set2.setColor(Color.MAGENTA);
                                set2.setCircleColor(Color.parseColor("#ffccff"));
                                //set1.setCircleColor(Color.BLACK);
                                set2.setLineWidth(1f);
                                set2.setCircleRadius(3f);
                                set2.setDrawCircleHole(false);
                                set2.setValueTextSize(9f);
                                set2.setDrawFilled(false);
                                set2.setFormLineWidth(1f);
                                set2.setFormSize(15.f);

                                LineData data = new LineData(set1, set2);
                                // set data
                                lineChart.setData(data);
                                lineChart.notifyDataSetChanged();
                                lineChart.invalidate();
                                lineChart.getAxisLeft().setDrawGridLines(false);
                                lineChart.getXAxis().setDrawGridLines(false);
                                lineChart.setDescription(null);
                                lineChart.setTouchEnabled(false);
                                lineChart.setScaleEnabled(false);
//                                lineChart.setScaleYEnabled(true);
//                                lineChart.setScaleXEnabled(true);
                                //  llayoutSalesPvA.setVisibility(View.VISIBLE);
                                onItemClickFlag=false;
                                pva_progressBar.setVisibility(View.GONE);
                                Reusable_Functions.hDialog();
                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            onItemClickFlag=false;
                            pva_progressBar.setVisibility(View.GONE);
                            Toast.makeText(context, "no category data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        onItemClickFlag=false;
                        pva_progressBar.setVisibility(View.GONE);
                        Toast.makeText(context, "no category data found", Toast.LENGTH_SHORT).show();
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

    // drill down level API
    private void requestSalesPvACategoryList(final String deptName) {
        String salespvacategory_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&department=" + deptName.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + salespvacategory_listurl);
        postRequest = new JsonArrayRequest(Request.Method.GET, salespvacategory_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Sales PvA Category List: ", " " + response);
                        Log.e("Sales PvA Category List response length", "" + response.length());
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                pva_progressBar.setVisibility(View.GONE);
                                onItemClickFlag = false;
                                Toast.makeText(context, "No Subdept found", Toast.LENGTH_SHORT).show();

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

                                salesPvAAdapter = new PvASnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listViewSalesPvA);
                                listViewSalesPvA.setAdapter(salesPvAAdapter);
                                salesPvAAdapter.notifyDataSetChanged();

                                txtStoreCode.setText(salesAnalysisClassArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(salesAnalysisClassArrayList.get(0).getStoreDesc());
                                pvaVal = " ";
                                pvaVal = deptName;
                                txtpvahDeptName.setText(pvaVal);
                                llpvahierarchy.setVisibility(View.VISIBLE);
                                salesPvAAnalysisWeekArrayList.clear();
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                //analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                                pvaFirstVisibleItem = salesAnalysisClassArrayList.get(0).getPlanCategory();
                                salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                               // flag = true;
                                requestPvAChartAPI();
                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            onItemClickFlag = false;
                            pva_progressBar.setVisibility(View.GONE);
                            Toast.makeText(context, "No Subdept found", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, "No Subdept found", Toast.LENGTH_SHORT).show();
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
        String salespva_planclass_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&category=" + category.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + salespva_planclass_listurl);
        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_planclass_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Sales Pva Plan Class List : ", " " + response);
                        Log.e("Sales Pva Plan Class List response length", "" + response.length());
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
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
                                salesPvAAdapter = new PvASnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listViewSalesPvA);
                                listViewSalesPvA.setAdapter(salesPvAAdapter);
                                salesPvAAdapter.notifyDataSetChanged();
                                txtStoreCode.setText(salesAnalysisClassArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(salesAnalysisClassArrayList.get(0).getStoreDesc());
                                pvaVal += " > " + category;
                                txtpvahDeptName.setText(pvaVal);
                                llpvahierarchy.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                salesPvAAnalysisWeekArrayList.clear();
                                //analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                                pvaFirstVisibleItem = salesAnalysisClassArrayList.get(0).getPlanClass();
                                Log.e("saleFirstVisibleItem in category list", "-----" + pvaFirstVisibleItem);
                                salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                             //   flag = true;
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

        String salespva_brand_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&class=" + planclass.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;

        Log.e("url", " " + salespva_brand_listurl);
        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_brand_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Sales Pva brand List : ", " " + response);
                        Log.e("Sales Pva brand List response length", "" + response.length());
                        try
                        {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                pva_progressBar.setVisibility(View.GONE);
                                onItemClickFlag = false;
                                Toast.makeText(context, "No Subclass data found", Toast.LENGTH_SHORT).show();

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
                                salesPvAAdapter = new PvASnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listViewSalesPvA);
                                listViewSalesPvA.setAdapter(salesPvAAdapter);
                                salesPvAAdapter.notifyDataSetChanged();
                                txtStoreCode.setText(salesAnalysisClassArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(salesAnalysisClassArrayList.get(0).getStoreDesc());
                                pvaVal += " > " + planclass;
                                txtpvahDeptName.setText(pvaVal);
                                llpvahierarchy.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                salesPvAAnalysisWeekArrayList.clear();
                                //analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                                pvaFirstVisibleItem = salesAnalysisClassArrayList.get(0).getBrandName();
                                Log.e("saleFirstVisibleItem in category list", "-----" + pvaFirstVisibleItem);
                                salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                               // flag = true;
                                requestPvAChartAPI();
                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            pva_progressBar.setVisibility(View.GONE);
                            onItemClickFlag = false;
                            Toast.makeText(context, "No Subclass data found", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, "No Subclass data found", Toast.LENGTH_SHORT).show();
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

        final String salespva_brandplan_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level  + "&brand=" + brandnm.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + salespva_brandplan_listurl);

        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_brandplan_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Sales Pva Brand Plan Class List : ", " " + response);
                        Log.i("Sales Pva Brand Plan Class List response length", "" + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                pva_progressBar.setVisibility(View.GONE);
                                onItemClickFlag = false;
                                Toast.makeText(context, "No MC data found", Toast.LENGTH_SHORT).show();

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisListDisplay);

                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesPvABrandPlanListAPI(brandnm);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++)
                                {
                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisListDisplay);
                                }
                                salesPvAAdapter = new PvASnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listViewSalesPvA);
                                listViewSalesPvA.setAdapter(salesPvAAdapter);
                                salesPvAAdapter.notifyDataSetChanged();
                                txtStoreCode.setText(salesAnalysisClassArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(salesAnalysisClassArrayList.get(0).getStoreDesc());
                                pvaVal += " > " + brandnm;
                                txtpvahDeptName.setText(pvaVal);
                                llpvahierarchy.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                salesPvAAnalysisWeekArrayList.clear();
                                pvaFirstVisibleItem = salesAnalysisClassArrayList.get(0).getBrandplanClass();
                                Log.e("saleFirstVisibleItem in category list", "-----" + pvaFirstVisibleItem);
                                salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                                requestPvAChartAPI();
                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            pva_progressBar.setVisibility(View.GONE);
                            onItemClickFlag = false;
                            Toast.makeText(context, "No MC data found", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, "No MC data found", Toast.LENGTH_SHORT).show();
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

        String url = ConstsCore.web_url + "/v1/display/salesanalysisbytime/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("Url", "" + url);
        postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("view pager response", "" + response);
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                pva_progressBar.setVisibility(View.GONE);
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
                                onItemClickFlag= false;
                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
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

    private void requestSalesSelectedFilterVal(final String selectedString)
    {
        String salespva_brandplan_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + SalesFilterActivity.level_filter + selectedString.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + salespva_brandplan_listurl);

        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_brandplan_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Sales List : ", " " + response);
                        Log.e("Sales List response length", "" + response.length());
                        if(SalesFilterActivity.level_filter == 2)
                        {
                            txtheaderplanclass.setText("Subdept");
                            fromWhere = "Subdept";
                            btnSalesPrev.setVisibility(View.VISIBLE);

                        }
                        else if(SalesFilterActivity.level_filter == 3)
                        {
                            txtheaderplanclass.setText("Class");
                            fromWhere = "Class";
                            btnSalesPrev.setVisibility(View.VISIBLE);
                        }
                        else if(SalesFilterActivity.level_filter == 4)
                        {
                            txtheaderplanclass.setText("Subclass");
                            fromWhere = "Subclass";
                            btnSalesPrev.setVisibility(View.VISIBLE);
                        }
                        else if(SalesFilterActivity.level_filter == 5)
                        {
                            txtheaderplanclass.setText("MC");
                            fromWhere = "MC";
                            btnSalesPrev.setVisibility(View.VISIBLE);
                            btnSalesNext.setVisibility(View.INVISIBLE);
                       }
                        else if(SalesFilterActivity.level_filter == 6)
                        {
                            txtheaderplanclass.setText("MC");
                            fromWhere = "MC";
                            btnSalesPrev.setVisibility(View.VISIBLE);
                            btnSalesNext.setVisibility(View.INVISIBLE);
                        }

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {

                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                pva_progressBar.setVisibility(View.GONE);
                                onItemClickFlag = false;

                            }
                            else if (response.length() == limit) {
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

                                listViewSalesPvA.setLayoutManager(new LinearLayoutManager(
                                        listViewSalesPvA.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                listViewSalesPvA.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(listViewSalesPvA);

                                Log.e(TAG, "onResponse: "+fromWhere );
                                salesPvAAdapter = new PvASnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listViewSalesPvA);
                                listViewSalesPvA.setAdapter(salesPvAAdapter);
                                salesPvAAdapter.notifyDataSetChanged();
                                txtStoreCode.setText(salesAnalysisClassArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(salesAnalysisClassArrayList.get(0).getStoreDesc());
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;

                                if (txtheaderplanclass.getText().toString().equals("Department"))
                                {
                                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanDept().toString();
                                }
                                else if (txtheaderplanclass.getText().toString().equals("Subdept"))
                                {

                                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanCategory().toString();

                                }
                                else if (txtheaderplanclass.getText().toString().equals("Class"))
                                {

                                    pvaFirstVisibleItem  = salesAnalysisClassArrayList.get(focusposition).getPlanClass().toString();
                                }
                                else if (txtheaderplanclass.getText().toString().equals("Subclass"))
                                {

                                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getBrandName().toString();
                                }
                                else if (txtheaderplanclass.getText().toString().equals("MC"))
                                {

                                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getBrandplanClass().toString();
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

     /*   Intent intent = new Intent(SalesPvAActivity.this, DashBoardActivity.class);
        startActivity(intent);*/
        salesPvA_SegmentClick = null;
        level = 0;
        salesPvA_SegmentClick = "WTD";
        level = 1;
        this.finish();
    }


}