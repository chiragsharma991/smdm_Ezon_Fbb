/*
package apsupportapp.aperotechnologies.com.designapp.PvaSalesAnalysis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
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
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesFilterActivity;
import apsupportapp.aperotechnologies.com.designapp.model.RecyclerItemClickListener;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisListDisplay;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisViewPagerValue;
import apsupportapp.aperotechnologies.com.designapp.model.SalesPvAAnalysisWeek;

*/
/**
 * Created by pamrutkar on 10/08/17.
 *//*


public class Test {
}


package apsupportapp.aperotechnologies.com.designapp.PvaSalesAnalysis;

        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.os.Handler;
        import android.preference.PreferenceManager;

        import android.support.design.widget.TabLayout;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.util.DisplayMetrics;

        import android.util.Log;
        import android.view.Gravity;
        import android.view.View;
        import android.widget.LinearLayout;
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

        import com.github.mikephil.charting.charts.BarChart;
        import com.github.mikephil.charting.components.AxisBase;
        import com.github.mikephil.charting.components.Legend;
        import com.github.mikephil.charting.components.XAxis;
        import com.github.mikephil.charting.components.YAxis;
        import com.github.mikephil.charting.data.BarData;
        import com.github.mikephil.charting.data.BarDataSet;
        import com.github.mikephil.charting.data.BarEntry;
        import com.github.mikephil.charting.data.Entry;
        import com.github.mikephil.charting.formatter.IAxisValueFormatter;
        import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
        import com.github.mikephil.charting.utils.ViewPortHandler;
        import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
        import com.google.gson.Gson;
        import com.numetriclabz.numandroidcharts.GaugeChart;

        import org.json.JSONArray;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.Map;

        import apsupportapp.aperotechnologies.com.designapp.ConstsCore;

        import apsupportapp.aperotechnologies.com.designapp.R;
        import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
        import apsupportapp.aperotechnologies.com.designapp.RunningPromo.RecyclerViewPositionHelper;
        import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesFilterActivity;
        import apsupportapp.aperotechnologies.com.designapp.model.RecyclerItemClickListener;
        import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisListDisplay;
        import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisViewPagerValue;
        import apsupportapp.aperotechnologies.com.designapp.model.SalesPvAAnalysisWeek;


public class SalesPvAActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener,View.OnClickListener {

    SharedPreferences sharedPreferences;
    int offsetvalue = 0, limit = 100;
    int count = 0;
    RequestQueue queue;
    RadioButton btn_WTD, btn_LW;
    private static String salesPvA_SegmentClick = "WTD",ez_tabClick = "WTD";
    ArrayList<SalesAnalysisListDisplay> salesAnalysisClassArrayList;
    LinearLayout llpvahierarchy;
    String userId, bearertoken,storeDescription,geoLeveLDesc;
    PvASnapAdapter salesPvAAdapter;
    ViewPortHandler handler;
    Context context;
    BarChart barChart,ez_barChart;
    RecyclerView listViewSalesPvA,ez_listView;
    int focusposition, selFirstPositionValue = 0, totalItemCount;
    private static int level = 1,ez_level = 1;
    SalesAnalysisListDisplay salesAnalysisListDisplay;
    SalesPvAAnalysisWeek salesPvAAnalysisWeek;
    ArrayList<SalesPvAAnalysisWeek> salesPvAAnalysisWeekArrayList;
    String fromWhere, txtPvAClickedValue,ez_fromWhere,ez_txtClickedVal;
    TextView txtStoreCode, txtStoreDesc, txtheaderplanclass,txtpvahDeptName, txt_pva_noChart,ez_txtheaderclass,ez_txtpvahDeptName;
    RelativeLayout tableRelLayout,btnBack, btnFilter, llayoutSalesPvA, btnSalesPrev, btnSalesNext,btn_ez_back,btn_ez_filter,btn_ez_prev,btn_ez_next,btn_ez_sort;
    Gson gson;
    String pvaFirstVisibleItem,ez_firstVisibleItem;
    JsonArrayRequest postRequest,ez_postRequest;
    ArrayList<SalesAnalysisViewPagerValue> arrayList;
    static SalesAnalysisViewPagerValue salesAnalysisViewPagerValue;
    static String planDept, planCategory, planClass;
    String pvaVal, TAG = "SalesPvAActivity";
    int currentIndex,prevState = RecyclerView.SCROLL_STATE_IDLE,currentState = RecyclerView.SCROLL_STATE_IDLE;
    boolean onItemClickFlag = false, filter_toggleClick = false;
    ProgressBar pva_progressBar,ez_progressBar;
    public static Activity Sales_Pva_Activity;
    private TabLayout tabLayout,ez_tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        Sales_Pva_Activity = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        storeDescription = sharedPreferences.getString("storeDescription","");
        geoLeveLDesc = sharedPreferences.getString("geoLeveLDesc","");
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();
        if (geoLeveLDesc.equals("E ZONE")) {
            setContentView(R.layout.activity_ezone_pva);
            getSupportActionBar().hide();
            initializeEzoneUI();
        }
        else
        {
            setContentView(R.layout.activity_sales_pva);
            getSupportActionBar().hide();
            initializeFBBUI();
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
            }
            else
            {
                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
            }
            listViewSalesPvA.addOnScrollListener(new RecyclerView.OnScrollListener()
            {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy)
                {
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
                                    switch (txtheaderplanclass.getText().toString()) {
                                        case "Department":
                                            btnSalesPrev.setVisibility(View.VISIBLE);
                                            txtheaderplanclass.setText("Subdept");
                                            txtPvAClickedValue = salesAnalysisClassArrayList.get(position).getPlanDept();
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
                                                requestSalesPvACategoryList(txtPvAClickedValue);
                                                planDept = txtPvAClickedValue;

                                            } else {
                                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                            }
                                            break;

                                        case "Subdept":

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
                                            txtheaderplanclass.setText("Subclass");
                                            txtPvAClickedValue = salesAnalysisClassArrayList.get(position).getPlanClass();
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
                                                requestSalesPvABrandListAPI(txtPvAClickedValue);
                                                planClass = txtPvAClickedValue;
                                            } else {
                                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                            }

                                            break;
                                        case "Subclass":
                                            btnSalesNext.setVisibility(View.INVISIBLE);
                                            txtheaderplanclass.setText("MC");
                                            txtPvAClickedValue = salesAnalysisClassArrayList.get(position).getBrandName();
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
                                                requestSalesPvABrandPlanListAPI(txtPvAClickedValue);
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
                                }
                            }
                        }, 700);
                    }
                }
            }));


        }
    }

    private void initializeEzoneUI()
    {
        btn_ez_back = (RelativeLayout)findViewById(R.id.rel_ez_back);
        btn_ez_filter = (RelativeLayout)findViewById(R.id.rel_ez_filter);
        btn_ez_prev = (RelativeLayout)findViewById(R.id.btnEzoneBack);
        btn_ez_next = (RelativeLayout)findViewById(R.id.btnEzoneNext);
        btn_ez_sort = (RelativeLayout)findViewById(R.id.rel_ez_sort);
        ez_txtheaderclass = (TextView)findViewById(R.id.txtPlanClass);
        ez_txtpvahDeptName = (TextView)findViewById(R.id.txtpvahDeptName);
        ez_tabLayout = (TabLayout) findViewById(R.id.tabview_ez_salespva);
        ez_tabLayout.addTab(ez_tabLayout.newTab().setText("WTD"));
        ez_tabLayout.addTab(ez_tabLayout.newTab().setText("LW"));
        ez_tabLayout.setOnTabSelectedListener(this);

        ez_listView = (RecyclerView)findViewById(R.id.ez_list);
        ez_progressBar = (ProgressBar)findViewById(R.id.ez_progressBar);
        ez_barChart = (BarChart)findViewById(R.id.ez_bar_chart);

    }

    private void initializeFBBUI()
    {
        txtStoreCode = (TextView) findViewById(R.id.txtStoreCode);
        txtStoreDesc = (TextView) findViewById(R.id.txtStoreName);
        txtStoreCode.setText(storeDescription.trim().substring(0,4));
        txtStoreDesc.setText(storeDescription.substring(5));
        txt_pva_noChart = (TextView) findViewById(R.id.pva_noChart);
        //hierarchy header
        txtpvahDeptName = (TextView) findViewById(R.id.txtpvahDeptName);
        pvaVal = " ";
        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
        salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
        arrayList = new ArrayList<SalesAnalysisViewPagerValue>();
        tabLayout = (TabLayout) findViewById(R.id.tabview_salespva);
        tabLayout.addTab(tabLayout.newTab().setText("WTD"));
        tabLayout.addTab(tabLayout.newTab().setText("LW"));
        tabLayout.setOnTabSelectedListener(this);
        llayoutSalesPvA = (RelativeLayout) findViewById(R.id.llayoutSalesPvA);
        llpvahierarchy = (LinearLayout) findViewById(R.id.llpvahierarchy);
        llpvahierarchy.setOrientation(LinearLayout.HORIZONTAL);
        tableRelLayout = (RelativeLayout) findViewById(R.id.relTablelayout);
        pva_progressBar = (ProgressBar) findViewById(R.id.pva_progressBar);
        barChart = (BarChart) findViewById(R.id.bar_chart);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setDrawZeroLine(true);
        leftAxis.setDrawLimitLinesBehindData(false);
        barChart.getAxisRight().setEnabled(false);
        Legend l = barChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        l.setEnabled(true);
        handler = barChart.getViewPortHandler();
        txtheaderplanclass = (TextView) findViewById(R.id.txtPlanClass);
        listViewSalesPvA = (RecyclerView) findViewById(R.id.list);
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
            //   btn_WTD.toggle();
            tabLayout.getTabAt(0).select();

        } else if (salesPvA_SegmentClick.equals("LW")) {
            //  btn_LW.toggle();
            tabLayout.getTabAt(1).select();

        }

    }

    private void TimeUP() {

        if (salesAnalysisClassArrayList.size() != 0) {
            if (focusposition < salesAnalysisClassArrayList.size() && !onItemClickFlag) {

                if (txtheaderplanclass.getText().toString().equals("Department")) {
                    level = 1;
                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanDept();
                } else if (txtheaderplanclass.getText().toString().equals("Subdept")) {
                    level = 2;
                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanCategory();
                } else if (txtheaderplanclass.getText().toString().equals("Class")) {
                    level = 3;
                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanClass();
                } else if (txtheaderplanclass.getText().toString().equals("Subclass")) {
                    level = 4;
                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getBrandName();
                } else if (txtheaderplanclass.getText().toString().equals("MC")) {
                    level = 5;
                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getBrandplanClass();
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

            } else {
                focusposition = salesAnalysisClassArrayList.size() - 1;
                LinearLayoutManager llm = (LinearLayoutManager) listViewSalesPvA.getLayoutManager();
                llm.scrollToPosition(focusposition);

                if (txtheaderplanclass.getText().toString().equals("Department")) {
                    level = 1;
                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanDept();
                } else if (txtheaderplanclass.getText().toString().equals("Subdept")) {
                    level = 2;
                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanCategory();
                } else if (txtheaderplanclass.getText().toString().equals("Class")) {
                    level = 3;
                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanClass();
                } else if (txtheaderplanclass.getText().toString().equals("Subclass")) {
                    level = 4;
                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getBrandName();
                } else if (txtheaderplanclass.getText().toString().equals("MC")) {
                    level = 5;
                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getBrandplanClass();
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
        String salespva_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit;

        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            int i;
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                pva_progressBar.setVisibility(View.GONE);
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

                                // Retain values....
                                if (txtheaderplanclass.getText().toString().equals("Department")) {
                                    for (int j = 0; j < salesAnalysisClassArrayList.size(); j++) {
                                        level = 1;
                                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanDept();
                                        if (salesAnalysisClassArrayList.get(focusposition).getPlanDept().equals(pvaFirstVisibleItem)) {
                                            listViewSalesPvA.getLayoutManager().scrollToPosition(focusposition);
                                        }
                                    }
                                } else if (txtheaderplanclass.getText().toString().equals("Subdept")) {
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
                                } else if (txtheaderplanclass.getText().toString().equals("Subclass")) {
                                    for (int j = 0; j < salesAnalysisClassArrayList.size(); j++) {

                                        level = 4;
                                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getBrandName();
                                        if (salesAnalysisClassArrayList.get(focusposition).getBrandName().equals(pvaFirstVisibleItem)) {
                                            listViewSalesPvA.getLayoutManager().scrollToPosition(focusposition);

                                        }
                                    }
                                } else if (txtheaderplanclass.getText().toString().equals("MC")) {
                                    for (int j = 0; j < salesAnalysisClassArrayList.size(); j++) {

                                        level = 5;
                                        pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getBrandplanClass();
                                        if (salesAnalysisClassArrayList.get(focusposition).getBrandplanClass().equals(pvaFirstVisibleItem)) {
                                            listViewSalesPvA.getLayoutManager().scrollToPosition(focusposition);

                                        }
                                    }
                                }
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


                            }

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


    // this method is used to create data for Bar graph<br for on scroll />
    private void requestPvAChartAPI() {
        String url = "";
        pvaFirstVisibleItem = pvaFirstVisibleItem.replace("%", "%25");
        pvaFirstVisibleItem = pvaFirstVisibleItem.replace(" ", "%20").replace("&", "%26");

        if (txtheaderplanclass.getText().toString().equals("Department")) {

            url = ConstsCore.web_url + "/v1/display/salesvisualpvaanalysisbyweek/" + userId + "?department=" + pvaFirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit +"&view="+salesPvA_SegmentClick;
        } else if (txtheaderplanclass.getText().toString().equals("Subdept")) {

            url = ConstsCore.web_url + "/v1/display/salesvisualpvaanalysisbyweek/" + userId + "?category=" + pvaFirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit+"&view="+salesPvA_SegmentClick;
        } else if (txtheaderplanclass.getText().toString().equals("Class")) {

            url = ConstsCore.web_url + "/v1/display/salesvisualpvaanalysisbyweek/" + userId + "?class=" + pvaFirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit+"&view="+salesPvA_SegmentClick;
        } else if (txtheaderplanclass.getText().toString().equals("Subclass")) {

            url = ConstsCore.web_url + "/v1/display/salesvisualpvaanalysisbyweek/" + userId + "?brand=" + pvaFirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit+"&view="+salesPvA_SegmentClick;
        } else if (txtheaderplanclass.getText().toString().equals("MC")) {

            url = ConstsCore.web_url + "/v1/display/salesvisualpvaanalysisbyweek/" + userId + "?brandclass=" + pvaFirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit+"&view="+salesPvA_SegmentClick;
        }
        Log.e(TAG, "requestPvAChartAPI: "+url );
        postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, "onResponse: "+response);
                        try {

                            if (response.equals("") || response == null || response.length() == 0 && count == 0)
                            {
                                Reusable_Functions.hDialog();
                                barChart.setData(null);
                                barChart.notifyDataSetChanged();
                                barChart.invalidate();
                                pva_progressBar.setVisibility(View.GONE);
                                onItemClickFlag = false;
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();

                            }
                            else if (response.length() == limit)
                            {
                                for (int i = 0; i < response.length(); i++)
                                {
                                    salesPvAAnalysisWeek = gson.fromJson(response.get(i).toString(), SalesPvAAnalysisWeek.class);
                                    salesPvAAnalysisWeekArrayList.add(salesPvAAnalysisWeek);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestPvAChartAPI();
                            }
                            else if (response.length() < limit)
                            {
                                for (int i = 0; i < response.length(); i++)
                                {
                                    salesPvAAnalysisWeek = gson.fromJson(response.get(i).toString(), SalesPvAAnalysisWeek.class);
                                    salesPvAAnalysisWeekArrayList.add(salesPvAAnalysisWeek);
                                }
                                float groupSpace = 0.04f;
                                float barSpace = 0.02f; // x2 dataset
                                float barWidth = 0.46f; // x2 dataset
                                ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
                                ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();


                                for (int i = 0; i < salesPvAAnalysisWeekArrayList.size(); i++) {
                                    yVals1.add(new BarEntry(i,(float)salesPvAAnalysisWeekArrayList.get(i).getPlanSaleNetVal()));
                                }

                                for (int i = 0; i < salesPvAAnalysisWeekArrayList.size(); i++) {
                                    yVals2.add(new BarEntry(i,(float)salesPvAAnalysisWeekArrayList.get(i).getSaleNetVal()));
                                }
                                BarDataSet set1, set2;
                                if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
                                    set1 = (BarDataSet)barChart.getData().getDataSetByIndex(0);
                                    set2 = (BarDataSet)barChart.getData().getDataSetByIndex(1);
                                    set1.setValues(yVals1);
                                    set2.setValues(yVals2);
                                    barChart.getData().notifyDataChanged();
                                    barChart.notifyDataSetChanged();
                                } else {
                                    // create 2 datasets with different types
                                    set1 = new BarDataSet(yVals1, "Plan Sales");
                                    set1.setDrawValues(false);
                                    set1.setColor(Color.parseColor("#20b5d3"));

                                    set2 = new BarDataSet(yVals2, "Net Sales");
                                    set2.setColor(Color.parseColor("#21d24c"));
                                    set2.setDrawValues(false);

                                    ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                                    dataSets.add(set1);
                                    dataSets.add(set2);

                                    BarData data = new BarData(dataSets);
                                    barChart.setData(data);
                                }
                                barChart.getBarData().setBarWidth(barWidth);
                                barChart.getXAxis().setAxisMinValue(0);
                                barChart.groupBars(0, groupSpace, barSpace);
                                barChart.invalidate();

                                barChart.getXAxis().setDrawGridLines(false);
                                barChart.setDescription(null);
                                barChart.setTouchEnabled(false);
                                barChart.setScaleEnabled(false);
                                onItemClickFlag = false;
                                pva_progressBar.setVisibility(View.GONE);
                                Reusable_Functions.hDialog();
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

    private void requestSalesWeekChart()
    {
        String salespvaweekChart_url = ConstsCore.web_url + "/v1/display/salesvisualpvaanalysisbyweek/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e(TAG, "requestSalesWeekChart: "+salespvaweekChart_url );
        postRequest = new JsonArrayRequest(Request.Method.GET, salespvaweekChart_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, "onResponse: "+response);
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                barChart.setData(null);
                                barChart.notifyDataSetChanged();
                                barChart.invalidate();

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
                                requestSalesWeekChart();
                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++)
                                {
                                    salesPvAAnalysisWeek = gson.fromJson(response.get(i).toString(), SalesPvAAnalysisWeek.class);
                                    salesPvAAnalysisWeekArrayList.add(salesPvAAnalysisWeek);
                                }

                                float groupSpace = 0.04f;
                                float barSpace = 0.02f; // x2 dataset
                                float barWidth = 0.46f; // x2 dataset
                                ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
                                ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();


                                for (int i = 0; i < salesPvAAnalysisWeekArrayList.size(); i++) {
                                    yVals1.add(new BarEntry(i,(float)salesPvAAnalysisWeekArrayList.get(i).getPlanSaleNetVal()));
                                }

                                for (int i = 0; i < salesPvAAnalysisWeekArrayList.size(); i++) {
                                    yVals2.add(new BarEntry(i,(float)salesPvAAnalysisWeekArrayList.get(i).getSaleNetVal()));
                                }
                                BarDataSet set1, set2;
                                if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
                                    set1 = (BarDataSet)barChart.getData().getDataSetByIndex(0);
                                    set2 = (BarDataSet)barChart.getData().getDataSetByIndex(1);
                                    set1.setValues(yVals1);
                                    set2.setValues(yVals2);
                                    barChart.getData().notifyDataChanged();
                                    barChart.notifyDataSetChanged();
                                } else {
                                    // create 2 datasets with different types
                                    set1 = new BarDataSet(yVals1, "Plan Sales");
                                    set1.setDrawValues(false);
                                    set1.setColor(Color.parseColor("#20b5d3"));

                                    set2 = new BarDataSet(yVals2, "Net Sales");
                                    set2.setColor(Color.parseColor("#21d24c"));
                                    set2.setDrawValues(false);

                                    ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                                    dataSets.add(set1);
                                    dataSets.add(set2);

                                    BarData data = new BarData(dataSets);
                                    barChart.setData(data);
                                }
                                barChart.getBarData().setBarWidth(barWidth);
                                barChart.getXAxis().setAxisMinValue(0);
                                barChart.groupBars(0, groupSpace, barSpace);
                                barChart.invalidate();

                                barChart.getAxisLeft().setDrawGridLines(false);
                                barChart.getXAxis().setDrawGridLines(false);
                                barChart.setDescription(null);
                                barChart.setTouchEnabled(false);
                                barChart.setScaleEnabled(true);

                                onItemClickFlag = false;
                                Reusable_Functions.hDialog();
                                pva_progressBar.setVisibility(View.GONE);

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


    // drill down level API
    private void requestSalesPvACategoryList(final String deptName) {
        String salespvacategory_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&department=" + deptName.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        postRequest = new JsonArrayRequest(Request.Method.GET, salespvacategory_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
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

                                pvaVal = " ";
                                pvaVal = deptName;
                                txtpvahDeptName.setText(pvaVal);
                                llpvahierarchy.setVisibility(View.VISIBLE);
                                salesPvAAnalysisWeekArrayList.clear();
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                pvaFirstVisibleItem = salesAnalysisClassArrayList.get(0).getPlanCategory();
                                salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
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
        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_planclass_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

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
                                salesPvAAdapter = new PvASnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listViewSalesPvA);
                                listViewSalesPvA.setAdapter(salesPvAAdapter);
                                salesPvAAdapter.notifyDataSetChanged();
                                pvaVal += " > " + category;
                                txtpvahDeptName.setText(pvaVal);
                                llpvahierarchy.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                salesPvAAnalysisWeekArrayList.clear();
                                pvaFirstVisibleItem = salesAnalysisClassArrayList.get(0).getPlanClass();
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

        String salespva_brand_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&class=" + planclass.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_brand_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
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
                                pvaVal += " > " + planclass;
                                txtpvahDeptName.setText(pvaVal);
                                llpvahierarchy.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                salesPvAAnalysisWeekArrayList.clear();
                                pvaFirstVisibleItem = salesAnalysisClassArrayList.get(0).getBrandName();
                                salesPvAAnalysisWeekArrayList = new ArrayList<SalesPvAAnalysisWeek>();
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
        final String salespva_brandplan_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + level + "&brand=" + brandnm.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;

        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_brandplan_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
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
                                for (int i = 0; i < response.length(); i++) {
                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisListDisplay);
                                }
                                salesPvAAdapter = new PvASnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listViewSalesPvA);
                                listViewSalesPvA.setAdapter(salesPvAAdapter);
                                salesPvAAdapter.notifyDataSetChanged();
                                pvaVal += " > " + brandnm;
                                txtpvahDeptName.setText(pvaVal);
                                llpvahierarchy.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                salesPvAAnalysisWeekArrayList.clear();
                                pvaFirstVisibleItem = salesAnalysisClassArrayList.get(0).getBrandplanClass();
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
        String salespva_brandplan_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + salesPvA_SegmentClick + "&level=" + SalesFilterActivity.level_filter + selectedString.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_brandplan_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (SalesFilterActivity.level_filter == 2) {
                            txtheaderplanclass.setText("Subdept");
                            fromWhere = "Subdept";
                            btnSalesPrev.setVisibility(View.VISIBLE);

                        } else if (SalesFilterActivity.level_filter == 3) {
                            txtheaderplanclass.setText("Class");
                            fromWhere = "Class";
                            btnSalesPrev.setVisibility(View.VISIBLE);
                        } else if (SalesFilterActivity.level_filter == 4) {
                            txtheaderplanclass.setText("Subclass");
                            fromWhere = "Subclass";
                            btnSalesPrev.setVisibility(View.VISIBLE);
                        } else if (SalesFilterActivity.level_filter == 5) {
                            txtheaderplanclass.setText("MC");
                            fromWhere = "MC";
                            btnSalesPrev.setVisibility(View.VISIBLE);
                            btnSalesNext.setVisibility(View.INVISIBLE);
                        } else if (SalesFilterActivity.level_filter == 6) {
                            txtheaderplanclass.setText("MC");
                            fromWhere = "MC";
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

                                salesPvAAdapter = new PvASnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listViewSalesPvA);
                                listViewSalesPvA.setAdapter(salesPvAAdapter);
                                salesPvAAdapter.notifyDataSetChanged();
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                if (txtheaderplanclass.getText().toString().equals("Department")) {
                                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanDept();
                                } else if (txtheaderplanclass.getText().toString().equals("Subdept")) {
                                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanCategory();
                                } else if (txtheaderplanclass.getText().toString().equals("Class")) {
                                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanClass();
                                } else if (txtheaderplanclass.getText().toString().equals("Subclass")) {
                                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getBrandName();
                                } else if (txtheaderplanclass.getText().toString().equals("MC")) {
                                    pvaFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getBrandplanClass();
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
        if( geoLeveLDesc.equals("E ZONE"))
        {

        }

        else {
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
                        if (getIntent().getStringExtra("selectedDept") == null) {
                            requestSalesViewPagerValueAPI();

                        } else if (getIntent().getStringExtra("selectedDept") != null) {
                            String selectedString = getIntent().getStringExtra("selectedDept");
                            requestSalesSelectedFilterVal(selectedString);
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
                        if (getIntent().getStringExtra("selectedDept") == null) {
                            requestSalesViewPagerValueAPI();

                        } else if (getIntent().getStringExtra("selectedDept") != null) {
                            String selectedString = getIntent().getStringExtra("selectedDept");
                            requestSalesSelectedFilterVal(selectedString);
                        }

                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }
                    break;

                default:
                    break;
            }
        }

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnSalesNext :
                if (postRequest != null) {
                    postRequest.cancel();
                }
                if (pva_progressBar.getVisibility() == View.VISIBLE) {
                    return;
                }
                switch (txtheaderplanclass.getText().toString()) {

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

                    case "Subclass":
                        btnSalesNext.setVisibility(View.INVISIBLE);
                        txtheaderplanclass.setText("MC");
                        fromWhere = "MC";
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
            case R.id.btnSalesBack :
                if (postRequest != null) {
                    postRequest.cancel();
                }
                if (pva_progressBar.getVisibility() == View.VISIBLE) {
                    return;
                }
                switch (txtheaderplanclass.getText().toString()) {

                    case "MC":
                        btnSalesNext.setVisibility(View.VISIBLE);
                        txtheaderplanclass.setText("Subclass");
                        fromWhere = "Subclass";
                        level = 4;
                        pvaVal = " ";
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        listViewSalesPvA.removeAllViews();
                        llpvahierarchy.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context))
                        {
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
                    case "Subclass":
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
                    default:
                }
                break;


        }

    }


}


*/