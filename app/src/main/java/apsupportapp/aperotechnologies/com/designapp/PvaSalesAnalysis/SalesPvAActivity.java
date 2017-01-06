package apsupportapp.aperotechnologies.com.designapp.PvaSalesAnalysis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesFilterActivity;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisListDisplay;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisViewPagerValue;
import apsupportapp.aperotechnologies.com.designapp.model.SalesPvAAnalysisWeek;
import info.hoang8f.android.segmented.SegmentedGroup;


public class SalesPvAActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {


    RadioButton radioButton;
    public String salesPvA_SegmentClick;
    ArrayList<SalesAnalysisListDisplay> salesAnalysisClassArrayList;
    RelativeLayout tableRelLayout, relChartLayout;
    LinearLayout llayoutSalesPvA, llpvahierarchy;
    TextView txtStoreCode, txtStoreDesc;
    String userId, bearertoken;
    SharedPreferences sharedPreferences;
    int offsetvalue = 0, limit = 100;
    int count = 0;
    RequestQueue queue;
    SalesPvAAdapter salesPvAAdapter;
    ViewPortHandler handler;
    Context context;
    //CombinedData pvaChartData;
    // BarChart barChart;
    LineChart lineChart;
    ListView listViewSalesPvA;
    int focusposition, selFirstPositionValue;
    //CombinedData data;
    int level;
    SalesAnalysisListDisplay salesAnalysisListDisplay;
    SalesPvAAnalysisWeek salesPvAAnalysisWeek;
    ArrayList<SalesPvAAnalysisWeek> salesPvAAnalysisWeekArrayList;
    String fromWhere, txtPvAClickedValue;
    TextView txtheaderplanclass;
    RelativeLayout btnBack, btnFilter;
    RelativeLayout btnSalesPrev, btnSalesNext;
    Gson gson;
    String pvaFirstVisibleItem;
    ArrayList<SalesAnalysisViewPagerValue> arrayList;
    static SalesAnalysisViewPagerValue salesAnalysisViewPagerValue;
    static String planDept, planCategory, planClass;
    TextView txtpvahDeptName, txtpvahCategory, txtpvahPlanClass, txtpvahBrand;
    String pvaVal;
    int currentIndex;
    //git testing 05/01/2017
    boolean flag = false;
    View footer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salespva);
        fromWhere = "Department";
        focusposition = 0;
        selFirstPositionValue = 0;
        context = this;
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
        //hierarchy header
        txtpvahDeptName = (TextView) findViewById(R.id.txtpvahDeptName);
        pvaVal = " ";
        level = 1;
        salesPvA_SegmentClick = "WTD";
        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
        salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
        arrayList = new ArrayList<SalesAnalysisViewPagerValue>();
        radioButton = (RadioButton) findViewById(R.id.btn_wtd);
        radioButton.toggle();
        llayoutSalesPvA = (LinearLayout) findViewById(R.id.llayoutSalesPvA);
        //llayoutSalesPvA.setVisibility(View.GONE);
        llpvahierarchy = (LinearLayout) findViewById(R.id.llpvahierarchy);
        llpvahierarchy.setOrientation(LinearLayout.HORIZONTAL);
        tableRelLayout = (RelativeLayout) findViewById(R.id.relTablelayout);
        //  relChartLayout = (RelativeLayout) findViewById(R.id.relChartlayout);

        lineChart = (LineChart) findViewById(R.id.linechart);
        //  lineChart.setOnChartGestureListener(this);
        // lineChart.setOnChartValueSelectedListener(this);

        LimitLine llXAxis = new LimitLine(10f, "Index 10");
        // llXAxis.setLineWidth(4f);
        // llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);

        XAxis xAxis = lineChart.getXAxis();
        //  xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        // leftAxis.addLimitLine(ll1);
        // leftAxis.addLimitLine(ll2);
        // leftAxis.setAxisMaximum(200f);
        // leftAxis.setAxisMinimum(-50f);
        //leftAxis.setYOffset(20f);
        //   leftAxis.enableGridDashedLine(10f, 10f, 0f);
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

        listViewSalesPvA = (ListView) findViewById(R.id.list);
        footer = getLayoutInflater().inflate(R.layout.list_footer, null);
        listViewSalesPvA.addFooterView(footer);
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            offsetvalue = 0;
            limit = 100;
            count = 0;
            level = 1;
            llpvahierarchy.setVisibility(View.GONE);
            llayoutSalesPvA.setVisibility(View.GONE);
            //   relChartLayout.setVisibility(View.GONE);
            requestSalesViewPagerValueAPI();
            requestSalesListDisplayAPI();

        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }

        btnSalesPrev = (RelativeLayout) findViewById(R.id.btnSalesBack);
        btnSalesPrev.setVisibility(View.INVISIBLE);
        btnSalesPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (txtheaderplanclass.getText().toString()) {

                    case "Brand Plan Class":
                        btnSalesNext.setVisibility(View.VISIBLE);
                        txtheaderplanclass.setText("Brand");
                        fromWhere = "Brand";
                        flag = false;
                        level = 4;
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        //   relChartLayout.setVisibility(View.GONE);
                        llpvahierarchy.setVisibility(View.GONE);
                        llayoutSalesPvA.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.e("Brand Plan Class Prev-- ", "  ");
                            requestSalesViewPagerValueAPI();
                            requestSalesListDisplayAPI();
                            Log.e("prev 1", "" + salesAnalysisListDisplay.getBrandName());

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case "Brand":
                        txtheaderplanclass.setText("Plan Class");
                        fromWhere = "Plan Class";
                        level = 3;
                        flag = false;
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        // relChartLayout.setVisibility(View.GONE);
                        llpvahierarchy.setVisibility(View.GONE);
                        llayoutSalesPvA.setVisibility(View.GONE);

                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.e("Brand name prev", "--");
                            requestSalesViewPagerValueAPI();
                            requestSalesListDisplayAPI();
                            Log.e("prev 2", "" + salesAnalysisListDisplay.getPlanClass());

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("---2---", " ");

                        break;


                    case "Plan Class":

                        txtheaderplanclass.setText("Category");
                        fromWhere = "Category";
                        level = 2;
                        flag = false;
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        // relChartLayout.setVisibility(View.GONE);
                        llpvahierarchy.setVisibility(View.GONE);
                        llayoutSalesPvA.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.e("Plan class prev", "");
                            requestSalesViewPagerValueAPI();
                            requestSalesListDisplayAPI();

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("---3---", " ");

                        break;

                    case "Category":
                        btnSalesPrev.setVisibility(View.INVISIBLE);
                        txtheaderplanclass.setText("Department");
                        fromWhere = "Department";
                        level = 1;
                        flag = false;
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        // relChartLayout.setVisibility(View.GONE);
                        llpvahierarchy.setVisibility(View.GONE);
                        llayoutSalesPvA.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.e("Category prev", "");
                            requestSalesViewPagerValueAPI();
                            requestSalesListDisplayAPI();
                            Log.e("prev 4", "" + salesAnalysisListDisplay.getPlanDept());

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
                switch (txtheaderplanclass.getText().toString()) {

                    case "Department":
                        btnSalesPrev.setVisibility(View.VISIBLE);
                        txtheaderplanclass.setText("Category");
                        fromWhere = "Category";
                        level = 2;
                        flag = false;
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        // relChartLayout.setVisibility(View.GONE);
                        llpvahierarchy.setVisibility(View.GONE);
                        llayoutSalesPvA.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.i("dept next", "-----");
                            requestSalesViewPagerValueAPI();
                            requestSalesListDisplayAPI();
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "Category":
                        fromWhere = "Plan Class";
                        txtheaderplanclass.setText("Plan Class");
                        flag = false;
                        level = 3;
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        llpvahierarchy.setVisibility(View.GONE);
                        // relChartLayout.setVisibility(View.GONE);
                        llayoutSalesPvA.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.e("category next --", "");
                            requestSalesViewPagerValueAPI();
                            requestSalesListDisplayAPI();
                            Log.e("next 2", "" + salesAnalysisListDisplay.getPlanClass());

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("---2---", " ");

                        break;
                    case "Plan Class":
                        txtheaderplanclass.setText("Brand");
                        fromWhere = "Brand";
                        flag = false;
                        level = 4;
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        llpvahierarchy.setVisibility(View.GONE);
                        // relChartLayout.setVisibility(View.GONE);
                        llayoutSalesPvA.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            requestSalesViewPagerValueAPI();
                            requestSalesListDisplayAPI();

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("---3--", " ");

                        break;

                    case "Brand":
                        btnSalesNext.setVisibility(View.INVISIBLE);

                        txtheaderplanclass.setText("Brand Plan Class");
                        fromWhere = "Brand Plan Class";
                        flag = false;
                        level = 5;
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        llpvahierarchy.setVisibility(View.GONE);
                        // relChartLayout.setVisibility(View.GONE);
                        llayoutSalesPvA.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            requestSalesViewPagerValueAPI();
                            requestSalesListDisplayAPI();
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("---4---", " ");

                        break;
                    default:
                }
            }
        });

        // level drill down on item click
        listViewSalesPvA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                  if(position < salesAnalysisClassArrayList.size()){
                    switch (txtheaderplanclass.getText().toString()) {

                        case "Department":

                            btnSalesPrev.setVisibility(View.VISIBLE);
                            txtheaderplanclass.setText("Category");
                            txtPvAClickedValue = salesAnalysisClassArrayList.get(position).getPlanDept();
//                        relChartLayout.setVisibility(View.GONE);
                            Log.e("txtClicked department--", "" + txtPvAClickedValue);
                            footer.setVisibility(View.GONE);
                            llayoutSalesPvA.setVisibility(View.GONE);
                            fromWhere = "Category";
                            level = 2;
                            if (Reusable_Functions.chkStatus(context)) {
                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
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

                        case "Category":
                            Log.e("in pva category", "-----" + planDept);

                            if (flag == true) {

                                txtheaderplanclass.setText("Plan Class");
//                            relChartLayout.setVisibility(View.GONE);
                                llayoutSalesPvA.setVisibility(View.GONE);
                                txtPvAClickedValue = salesAnalysisClassArrayList.get(position).getPlanCategory();
                                Log.e("txtClicked category --", "" + txtPvAClickedValue);
                                fromWhere = "Plan Class";
                                level = 3;
                                if (Reusable_Functions.chkStatus(context)) {
                                    Reusable_Functions.hDialog();
                                    Reusable_Functions.sDialog(context, "Loading data...");
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    salesAnalysisClassArrayList.clear();
                                    Log.i("category next", "-----");
                                    Log.i("come", "----" + planDept);
                                    requestSalesPvAPlanClassListAPI(planDept, txtPvAClickedValue);
                                    planCategory = txtPvAClickedValue;
                                    Log.e("planCategory--", "" + planCategory);
                                } else {
                                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.e("Please Select dept name", "------1");
                            }

                            break;
                        case "Plan Class":
                            Log.e("in sales pva plan class", "-----" + planDept);

                            if (flag == true) {

                                txtheaderplanclass.setText("Brand");
                                //                          relChartLayout.setVisibility(View.GONE);
                                llayoutSalesPvA.setVisibility(View.GONE);
                                txtPvAClickedValue = salesAnalysisClassArrayList.get(position).getPlanClass();
                                Log.e("txtClicked plan class---", "" + txtPvAClickedValue);
                                fromWhere = "Brand";
                                level = 4;
                                if (Reusable_Functions.chkStatus(context)) {
                                    Reusable_Functions.hDialog();
                                    Reusable_Functions.sDialog(context, "Loading data...");
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    salesAnalysisClassArrayList.clear();
                                    Log.i("Plan Class next", "-----");
                                    requestSalesPvABrandListAPI(planDept, planCategory, txtPvAClickedValue);
                                    planClass = txtPvAClickedValue;
                                    Log.e("planClass---", "" + planClass);
                                } else {
                                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.e("Please select dept name", "------2");
                            }


                            break;
                        case "Brand":
                            Log.e("in sales pva brand ", "-----" + planDept);

                            if (flag == true) {
                                btnSalesNext.setVisibility(View.INVISIBLE);
                                txtheaderplanclass.setText("Brand Plan Class");
                                //                        relChartLayout.setVisibility(View.GONE);
                                llayoutSalesPvA.setVisibility(View.GONE);
                                txtPvAClickedValue = salesAnalysisClassArrayList.get(position).getBrandName();
                                Log.e("txtSalesClickedValue3---", "" + txtPvAClickedValue);
                                fromWhere = "Brand Plan Class";
                                level = 5;
                                if (Reusable_Functions.chkStatus(context)) {
                                    Reusable_Functions.hDialog();
                                    Reusable_Functions.sDialog(context, "Loading data...");
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    salesAnalysisClassArrayList.clear();
                                    Log.i("brand next", "-----");
                                    requestSalesPvABrandPlanListAPI(planDept, planCategory, planClass, txtPvAClickedValue);

                                } else {
                                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.e("Please select dept name", "---------3");
                            }

                            break;
                        default:
                            break;
                    }
                }

            }

        });





        // list view on Scroll
        listViewSalesPvA.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                if (scrollState == SCROLL_STATE_IDLE) {

                if (salesAnalysisClassArrayList.size() != 0) {
                    //listView_SalesAnalysis.smoothScrollToPosition(firstVisibleItem);
                    if (view.getFirstVisiblePosition() <= salesAnalysisClassArrayList.size() - 1) {
                        focusposition = view.getFirstVisiblePosition();
                        listViewSalesPvA.setSelection(view.getFirstVisiblePosition());
                        currentIndex = listViewSalesPvA.getFirstVisiblePosition();

                        //Log.e("focusposition", " " + firstVisibleItem + " " + productNameBeanArrayList.get(firstVisibleItem).getProductName());
                        if (txtheaderplanclass.getText().toString().equals("Department")) {
                            pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanDept().toString();
                        } else if (txtheaderplanclass.getText().toString().equals("Category")) {
                            pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanCategory().toString();
                        } else if (txtheaderplanclass.getText().toString().equals("Plan Class")) {
                            pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanClass().toString();
                        } else if (txtheaderplanclass.getText().toString().equals("Brand")) {
                            pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getBrandName().toString();
                        } else if (txtheaderplanclass.getText().toString().equals("Brand Plan Class")) {
                            pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getBrandplanClass().toString();
                        }
                        if (focusposition != selFirstPositionValue) {
                            if (Reusable_Functions.chkStatus(context)) {
                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                                if (pvaFirstVisibleItem.equals("All")) {

                                    lineChart.setPinchZoom(false);
                                    lineChart.setScaleEnabled(false);
                                    requestSalesWeekChart();

                                } else {
                                    //relChartLayout.setVisibility(View.GONE);
                                    salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                                    //lineChart.invalidate();
                                    lineChart.setPinchZoom(false);
                                    lineChart.setTouchEnabled(false);
lineChart.setScaleEnabled(false);
                                    requestPvAChartAPI();

                                }

                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }
                        }
                        selFirstPositionValue = focusposition;


                    } else {
                        focusposition = salesAnalysisClassArrayList.size() - 1;
                        listViewSalesPvA.setSelection(focusposition);
                        selFirstPositionValue = focusposition;

                    }
                }

//                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


            }
        });


        btnBack = (RelativeLayout) findViewById(R.id.imageBtnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(SalesPvAActivity.this, DashBoardActivity.class);
                startActivity(intent);*/
                finish();
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


//    public static void setListViewHeightBasedOnChildren(ListView listViewSales) {
//        SalesPvAAdapter salesPvAAdapter = ((SalesPvAAdapter) ((HeaderViewListAdapter) listViewSales.getAdapter()).getWrappedAdapter());
//        salesPvAAdapter.notifyDataSetChanged();
//        if (salesPvAAdapter == null) {
//            // pre-condition
//            return;
//        }
//
//        int totalHeight = 0;
//        for (int i = 0; i < salesPvAAdapter.getCount() - 1; i++) {
//            View listItem = salesPvAAdapter.getView(i, null, listViewSales);
//            listItem.measure(0, 0);
//            totalHeight += listItem.getMeasuredHeight();
//        }
//
//        ViewGroup.LayoutParams params = listViewSales.getLayoutParams();
//        params.height = totalHeight + (listViewSales.getDividerHeight() * (salesPvAAdapter.getCount() - 1));
//        listViewSales.setLayoutParams(params);
//
//    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {

            case R.id.btn_wtd:
                //Toast.makeText(SalesPvAActivity.this, "WTD", Toast.LENGTH_SHORT).show();
                if (salesPvA_SegmentClick.equals("WTD"))
                    break;
                salesPvA_SegmentClick = "WTD";
                //          relChartLayout.setVisibility(View.GONE);
                llpvahierarchy.setVisibility(View.GONE);
                llayoutSalesPvA.setVisibility(View.GONE);
                currentIndex = listViewSalesPvA.getFirstVisiblePosition();
                salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading data...");
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    requestSalesViewPagerValueAPI();
                    requestSalesListDisplayAPI();

                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
                Log.e("-----WTD-----", " ");
                break;

            case R.id.btn_lw:
                //Toast.makeText(SalesPvAActivity.this, "LW", Toast.LENGTH_SHORT).show();
                if (salesPvA_SegmentClick.equals("LW"))
                    break;

                salesPvA_SegmentClick = "LW";
                //        relChartLayout.setVisibility(View.GONE);
                llpvahierarchy.setVisibility(View.GONE);
                llayoutSalesPvA.setVisibility(View.GONE);
                currentIndex = listViewSalesPvA.getFirstVisiblePosition();
                salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading data...");
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    requestSalesViewPagerValueAPI();
                    requestSalesListDisplayAPI();

                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }

                Log.e("-----lw-----", " ");


                break;

            default:
                break;

        }
    }


//    public LineDataSet getLineDataSet() {
//        ArrayList<Entry> entry = new ArrayList<>();
//        for (int i = 0; i < salesAnalysisClassArrayList.size(); i++) {
//            Log.e("line data", "" + salesAnalysisClassArrayList.get(i).getPvaAchieved());
//            float value = (float) salesAnalysisClassArrayList.get(i).getPvaAchieved();
//            Entry lineValues = new Entry(value, i);
//            entry.add(lineValues);
//        }
//        LineDataSet linedataSet = new LineDataSet(entry, "PvAAchieved");
//        linedataSet.setDrawValues(false);
//        linedataSet.setColor(Color.parseColor("#ed5752"));
//        return linedataSet;
//    }

//    public LineDataSet getpvaLineDataSet() {
//        ArrayList<Entry> pvaentry = new ArrayList<>();
//        for (int i = 0; i < pvaChartArrayList.size(); i++) {
//            Log.e("line data", "" + pvaChartArrayList.get(i).getPvaAchieved());
//            float value = (float) pvaChartArrayList.get(i).getPvaAchieved();
//            Entry pvalineValues = new Entry(value, i);
//            pvaentry.add(pvalineValues);
//
//        }
//        LineDataSet pvalinedataSet = new LineDataSet(pvaentry, "PvAAchieved");
//        pvalinedataSet.setDrawValues(false);
//        pvalinedataSet.setColor(Color.MAGENTA);
//        return pvalinedataSet;
//    }

    // this method is used to create data for Bar graph<br />
//    public ArrayList<IBarDataSet> getDataSet() {
//
//        ArrayList<IBarDataSet> dataSets = null;
//        ArrayList<BarEntry> group1 = new ArrayList<>();
//
//        for (int i = 0; i < salesAnalysisClassArrayList.size(); i++) {
//            // Log.e("getSaleNetVal--", "" + salesAnalysisClassArrayList.get(i).getPlanSaleNetVal());
//            float value = (float) salesAnalysisClassArrayList.get(i).getPlanSaleNetVal();
//            BarEntry barchartValues1 = new BarEntry(value, i);
//            group1.add(barchartValues1);
//        }
////            }
////        }
//
//
//        ArrayList<BarEntry> group2 = new ArrayList();
//        for (int i = 0; i < salesAnalysisClassArrayList.size(); i++) {
//            //Log.e("getPlanSaleNetVal--", "" + salesAnalysisClassArrayList.get(i).getSaleNetVal());
//            float value2 = (float) salesAnalysisClassArrayList.get(i).getSaleNetVal();
//            BarEntry barchartValues2 = new BarEntry(value2, i);
//            group2.add(barchartValues2);
//        }
//
//        BarDataSet barDataSet = new BarDataSet(group1, "Plan Sales");
//
//        barDataSet.setColor(Color.BLUE);
//        barDataSet.setDrawValues(false);
//        BarDataSet barDataSet1 = new BarDataSet(group2, "Net Sales");
//        barDataSet1.setDrawValues(false);
//        barDataSet1.setColor(Color.parseColor("#a45df1"));
//        dataSets = new ArrayList<>();
//        dataSets.add(barDataSet);
//        dataSets.add(barDataSet1);
//        return dataSets;
//    }


    // API 1.20
    private void requestSalesListDisplayAPI() {
        String salespva_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + salespva_listurl);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, salespva_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Sales Analysis Class: ", " " + response);
                        Log.i("response length", "" + response.length());

                        try {
                            int i;
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
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

                                if (txtheaderplanclass.getText().toString().equals("Department"))
                                {
                                    salesAnalysisListDisplay.setPlanDept("All");
                                    salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                    salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                    salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());

                                } else if (txtheaderplanclass.getText().toString().equals("Category"))
                                {
                                    salesAnalysisListDisplay.setPlanCategory("All");
                                    salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                    salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                    salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());

                                } else if (txtheaderplanclass.getText().toString().equals("Plan Class"))
                                {
                                    salesAnalysisListDisplay.setPlanClass("All");
                                    salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                    salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                    salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());

                                } else if (txtheaderplanclass.getText().toString().equals("Brand"))
                                {
                                    salesAnalysisListDisplay.setBrandName("All");
                                    salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                    salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                    salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());

                                } else if (txtheaderplanclass.getText().toString().equals("Brand Plan Class"))
                                {
                                    salesAnalysisListDisplay.setBrandplanClass("All");
                                    salesAnalysisListDisplay.setPlanSaleNetVal(Math.round(salesAnalysisViewPagerValue.getPlanSaleNetVal()));
                                    salesAnalysisListDisplay.setSaleNetVal(Math.round(salesAnalysisViewPagerValue.getSaleNetVal()));
                                    salesAnalysisListDisplay.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());
                                }

                                salesAnalysisClassArrayList.add(0, salesAnalysisListDisplay);
                                salesPvAAdapter = new SalesPvAAdapter(salesAnalysisClassArrayList, context,currentIndex, fromWhere, listViewSalesPvA);

                                Log.e("","focusPosition in API----"+currentIndex);

                                listViewSalesPvA.setSelection(currentIndex);
                                listViewSalesPvA.smoothScrollToPosition(currentIndex);
                                if(listViewSalesPvA.getAdapter() == null)
                                {
                                    listViewSalesPvA.setAdapter(salesPvAAdapter);
                                }
                                else
                                {
                                    salesPvAAdapter.notifyDataSetChanged();
                                }


                                txtStoreCode.setText(salesAnalysisClassArrayList.get(i).getStoreCode());
                                txtStoreDesc.setText(salesAnalysisClassArrayList.get(i).getStoreDesc());


                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                                requestSalesWeekChart();
                                //llayoutSalesPvA.setVisibility(View.VISIBLE);
                                // Reusable_Functions.hDialog();
                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
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
    }


    // this method is used to create data for Bar graph<br for on scroll />
    private void requestPvAChartAPI() {
        Log.e("Department onsroll api", "" + pvaFirstVisibleItem);
        String url = "";
        if (txtheaderplanclass.getText().toString().equals("Department")) {

            url = ConstsCore.web_url + "/v1/display/salesvisualpvaanalysisbyweek/" + userId + "?department=" + pvaFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        } else if (txtheaderplanclass.getText().toString().equals("Category")) {

            url = ConstsCore.web_url + "/v1/display/salesvisualpvaanalysisbyweek/" + userId + "?category=" + pvaFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        } else if (txtheaderplanclass.getText().toString().equals("Plan Class")) {

            url = ConstsCore.web_url + "/v1/display/salesvisualpvaanalysisbyweek/" + userId + "?class=" + pvaFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        } else if (txtheaderplanclass.getText().toString().equals("Brand")) {

            url = ConstsCore.web_url + "/v1/display/salesvisualpvaanalysisbyweek/" + userId + "?brand=" + pvaFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        } else if (txtheaderplanclass.getText().toString().equals("Brand Plan Class")) {

            url = ConstsCore.web_url + "/v1/display/salesvisualpvaanalysisbyweek/" + userId + "?brandclass=" + pvaFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        }
        Log.e("Url", "" + url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Sales Pva Chart on Scroll  : ", " " + response);
                        Log.i("Sales Pva Chart response", "" + response.length());
                        try {

                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
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
                                    float planSaleNetVal1 = (float) salesPvAAnalysisWeekArrayList.get(j).getPlanSaleNetVal();
                                    Log.e("Val Plan Sale----", "" + planSaleNetVal1);
                                    yVal_select_planSale.add(new Entry(j, planSaleNetVal1));

                                }
                                ArrayList<Entry> yVal_select_netSale = new ArrayList<Entry>();
                                for (int j = 0; j < salesPvAAnalysisWeekArrayList.size(); j++) {
                                    float saleNetVal1 = (float) salesPvAAnalysisWeekArrayList.get(j).getSaleNetVal();
                                    Log.e("Val Net Sale---", "" + saleNetVal1);
                                    yVal_select_netSale.add(new Entry(j, saleNetVal1));

                                }

                                LineDataSet lineDataSet1, lineDataSet2;
                                lineDataSet1 = new LineDataSet(yVal_select_planSale, "Plan Sales");
                                lineDataSet1.setDrawValues(false);


                                lineDataSet2 = new LineDataSet(yVal_select_netSale, "Net Sales");
                                lineDataSet2.setDrawValues(false);
                                //    set the line to be drawn like this "- - - - - -"
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
                                //  set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
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
                                //  set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                                lineDataSet2.setFormSize(15.f);

//                             LineDataSet dataSets = new LineDataSet(yVals,"");
//                             dataSets.add(set1); // add the datasets

                                // create a data object with the datasets
                                LineData data = new LineData(lineDataSet1, lineDataSet2);
                                // set data

                                // create a data object with the datasets
                                LineData lineChartdata = new LineData(lineDataSet1, lineDataSet2);
                                Log.e("lineDataSet", "" + lineDataSet1.getLabel());
                                // lineChartdata.addDataSet(lineDataSet);
                                // set data
                                // lineChart = (LineChart)findViewById(R.id.linechart);
                                lineChart.setData(lineChartdata);
                                lineChart.notifyDataSetChanged();
                                lineChart.invalidate();
                                lineChart.getAxisLeft().setDrawGridLines(false);
                                lineChart.getXAxis().setDrawGridLines(false);
                                lineChart.setDescription(null);
                                //  lineChart.addView(lineChart);

                                Log.e("lineChartData", "" + lineChartdata.getDataSetCount());
                                lineChart.setTouchEnabled(true);
                                lineChart.setScaleYEnabled(true);
                                lineChart.setScaleXEnabled(true);

                                //lineChart.setDoubleTapToZoomEnabled(false);


                                //  relChartLayout.setVisibility(View.VISIBLE);
                                llayoutSalesPvA.setVisibility(View.VISIBLE);
                                Reusable_Functions.hDialog();


                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
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

    }

    private void requestSalesWeekChart() {
        String salespvaweekChart_url = ConstsCore.web_url + "/v1/display/salesvisualpvaanalysisbyweek/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + salespvaweekChart_url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, salespvaweekChart_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Sales Week Chart: ", " " + response);
                        Log.i("Sales PvA Week response length", "" + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
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
                                    Log.e("planSaleVal", "" + planSaleVal);
                                    yVals_PlanSale.add(new Entry(j, planSaleVal));

                                }

                                ArrayList<Entry> yVals_SaleNet = new ArrayList<Entry>();
                                for (int j = 0; j < salesPvAAnalysisWeekArrayList.size(); j++) {
                                    float saleNetVal = (float) salesPvAAnalysisWeekArrayList.get(j).getSaleNetVal();
                                    Log.e("saleNetVal", "" + saleNetVal);
                                    yVals_SaleNet.add(new Entry(j, saleNetVal));

                                }

                                LineDataSet set1, set2;
                                set1 = new LineDataSet(yVals_PlanSale, "Plan Sales");
                                set1.setDrawValues(false);


                                set2 = new LineDataSet(yVals_SaleNet, "Net Sales");
                                set2.setDrawValues(false);
                                //    set the line to be drawn like this "- - - - - -"
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
                                //  set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                                set2.setFormSize(15.f);

//                             LineDataSet dataSets = new LineDataSet(yVals,"");
//                             dataSets.add(set1); // add the datasets

                                // create a data object with the datasets
                                LineData data = new LineData(set1, set2);
                                // set data

                                lineChart.setData(data);
                                lineChart.notifyDataSetChanged();
                                lineChart.invalidate();
                                lineChart.getAxisLeft().setDrawGridLines(false);
                                lineChart.getXAxis().setDrawGridLines(false);
                                lineChart.setDescription(null);
                                lineChart.setTouchEnabled(true);
                                lineChart.setScaleYEnabled(true);
                                lineChart.setScaleXEnabled(true);

                                // lineChart.setDoubleTapToZoomEnabled(false);
                                // relChartLayout.setVisibility(View.VISIBLE);
                                llayoutSalesPvA.setVisibility(View.VISIBLE);

                                Reusable_Functions.hDialog();
                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no category data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
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

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, salespvacategory_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Sales PvA Category List: ", " " + response);
                        Log.i("Sales PvA Category List response length", "" + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no category data found", Toast.LENGTH_SHORT).show();
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

                                salesPvAAdapter = new SalesPvAAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listViewSalesPvA);
                                listViewSalesPvA.setAdapter(salesPvAAdapter);
                                salesPvAAdapter.notifyDataSetChanged();
                                flag = true;
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
                                pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanCategory();
                                Log.e("saleFirstVisibleItem in category list", "-----" + pvaFirstVisibleItem);
                                salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                                requestPvAChartAPI();

                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no category data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
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

    private void requestSalesPvAPlanClassListAPI(final String deptName, final String category) {

        String salespva_planclass_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&department=" + planDept.replaceAll(" ", "%20").replaceAll("&", "%26") + "&category=" + category.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + salespva_planclass_listurl);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, salespva_planclass_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Sales Pva Plan Class List : ", " " + response);
                        Log.i("Sales Pva Plan Class List response length", "" + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no plan class data found", Toast.LENGTH_SHORT).show();
                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisListDisplay);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesPvAPlanClassListAPI(deptName, category);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisListDisplay);
                                }

                                salesPvAAdapter = new SalesPvAAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listViewSalesPvA);
                                listViewSalesPvA.setAdapter(salesPvAAdapter);
                                salesPvAAdapter.notifyDataSetChanged();
                                flag = true;
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
                                pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanClass();
                                Log.e("saleFirstVisibleItem in category list", "-----" + pvaFirstVisibleItem);
                                salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                                requestPvAChartAPI();

                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no plan class data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no  plan class data found", Toast.LENGTH_SHORT).show();
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

    private void requestSalesPvABrandListAPI(String deptName, String category, final String planclass) {

        String salespva_brand_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&department=" + planDept.replaceAll(" ", "%20").replaceAll("&", "%26") + "&category=" + planCategory.replaceAll(" ", "%20").replaceAll("&", "%26") + "&class=" + planclass.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + salespva_brand_listurl);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, salespva_brand_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Sales Pva brand List : ", " " + response);
                        Log.i("Sales Pva brand List response length", "" + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no brand name data found", Toast.LENGTH_SHORT).show();
                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisListDisplay);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesPvABrandListAPI(planDept, planCategory, planclass);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisListDisplay);
                                }

                                salesPvAAdapter = new SalesPvAAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listViewSalesPvA);
                                listViewSalesPvA.setAdapter(salesPvAAdapter);
                                salesPvAAdapter.notifyDataSetChanged();
                                flag = true;
                                txtStoreCode.setText(salesAnalysisClassArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(salesAnalysisClassArrayList.get(0).getStoreDesc());
//
                                pvaVal += " > " + planclass;
                                txtpvahDeptName.setText(pvaVal);

                                llpvahierarchy.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                salesPvAAnalysisWeekArrayList.clear();
                                //analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                                pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getBrandName();
                                Log.e("saleFirstVisibleItem in category list", "-----" + pvaFirstVisibleItem);
                                salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                                requestPvAChartAPI();

                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no brand name data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no brand name data found", Toast.LENGTH_SHORT).show();
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

    private void requestSalesPvABrandPlanListAPI(String deptName, String category, String plan_class, final String brandnm) {

        final String salespva_brandplan_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&department=" + planDept.replaceAll(" ", "%20").replaceAll("&", "%26") + "&category=" + planCategory.replaceAll(" ", "%20").replaceAll("&", "%26") + "&class=" + planClass.replaceAll(" ", "%20").replaceAll("&", "%26") + "&brand=" + brandnm.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + salespva_brandplan_listurl);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, salespva_brandplan_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Sales Pva Brand Plan Class List : ", " " + response);
                        Log.i("Sales Pva Brand Plan Class List response length", "" + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no brand plan class data found", Toast.LENGTH_SHORT).show();

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisListDisplay);

                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesPvABrandPlanListAPI(planDept, planCategory, planClass, brandnm);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisListDisplay);
                                }

                                salesPvAAdapter = new SalesPvAAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listViewSalesPvA);
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
                                //analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                                pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getBrandplanClass();
                                Log.e("saleFirstVisibleItem in category list", "-----" + pvaFirstVisibleItem);
                                salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
                                requestPvAChartAPI();

                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no brand plan class data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no brand plan class data found", Toast.LENGTH_SHORT).show();
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
        //Log.e("saleFirstVisibleItem in Api",""+saleFirstVisibleItem);

        String url = ConstsCore.web_url + "/v1/display/salesanalysisbytime/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit;

        Log.e("Url", "" + url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("view pager response", "" + response);

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
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
                            }


                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
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

    }


    @Override
    public void onBackPressed() {

     /*   Intent intent = new Intent(SalesPvAActivity.this, DashBoardActivity.class);
        startActivity(intent);*/
        finish();
    }


//
//    @Override
//    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
//
//    }
//
//    @Override
//    public void onChartGestureEnd(MotionEvent me,
//                                  ChartTouchListener.ChartGesture
//                                          lastPerformedGesture) {
//
//        Log.i("Gesture", "END, lastGesture: " + lastPerformedGesture);
//
//        // un-highlight values after the gesture is finished and no single-tap
//        if(lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
//            // or highlightTouch(null) for callback to onNothingSelected(...)
//            lineChart.highlightValues(null);
//    }
//
//    @Override
//    public void onChartLongPressed(MotionEvent me) {
//        Log.i("LongPress", "Chart longpressed.");
//    }
//
//    @Override
//    public void onChartDoubleTapped(MotionEvent me) {
//        Log.i("DoubleTap", "Chart double-tapped.");
//    }
//
//    @Override
//    public void onChartSingleTapped(MotionEvent me) {
//        Log.i("SingleTap", "Chart single-tapped.");
//    }
//
//    @Override
//    public void onChartFling(MotionEvent me1, MotionEvent me2,
//                             float velocityX, float velocityY) {
//        Log.i("Fling", "Chart flinged. VeloX: "
//                + velocityX + ", VeloY: " + velocityY);
//    }
//
//    @Override
//    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
//        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
//    }
//
//    @Override
//    public void onChartTranslate(MotionEvent me, float dX, float dY) {
//        Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
//    }
//
//
//
//    @Override
//    public void onValueSelected(Entry e, Highlight h) {
//        Log.i("Entry selected", e.toString());
//        Log.i("LOWHIGH", "low: " + lineChart.getLowestVisibleX()
//                + ", high: " + lineChart.getHighestVisibleX());
//
//        Log.i("MIN MAX", "xmin: " + lineChart.getXChartMin()
//                + ", xmax: " + lineChart.getXChartMax()
//                + ", ymin: " + lineChart.getYChartMin()
//                + ", ymax: " + lineChart.getYChartMax());
//
//    }
//
//    @Override
//    public void onNothingSelected() {
//        Log.i("Nothing selected", "Nothing selected.");
//    }
//


}