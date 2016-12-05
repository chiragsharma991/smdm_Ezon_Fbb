//package apsupportapp.aperotechnologies.com.designapp.PvaSalesAnalysis;
//
///**
// * Created by pamrutkar on 18/11/16.
// */
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.preference.PreferenceManager;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AbsListView;
//import android.widget.HeaderViewListAdapter;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Cache;
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.RetryPolicy;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.BasicNetwork;
//import com.android.volley.toolbox.DiskBasedCache;
//import com.android.volley.toolbox.HurlStack;
//import com.android.volley.toolbox.JsonArrayRequest;
//import com.github.mikephil.charting.charts.BarChart;
//import com.github.mikephil.charting.components.Legend;
//import com.github.mikephil.charting.components.XAxis;
//import com.github.mikephil.charting.components.YAxis;
//import com.github.mikephil.charting.data.BarData;
//import com.github.mikephil.charting.data.BarDataSet;
//import com.github.mikephil.charting.data.BarEntry;
//import com.github.mikephil.charting.data.CombinedData;
//import com.github.mikephil.charting.data.Entry;
//import com.github.mikephil.charting.data.LineDataSet;
//import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
//import com.github.mikephil.charting.utils.ViewPortHandler;
//import com.google.gson.Gson;
//
//import org.json.JSONArray;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
//import apsupportapp.aperotechnologies.com.designapp.DashBoardActivity;
//import apsupportapp.aperotechnologies.com.designapp.R;
//import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
//import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesFilterActivity;
//import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisListDisplay;
//import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisViewPagerValue;
//import info.hoang8f.android.segmented.SegmentedGroup;
//
//
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.preference.PreferenceManager;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AbsListView;
//import android.widget.HeaderViewListAdapter;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Cache;
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.RetryPolicy;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.BasicNetwork;
//import com.android.volley.toolbox.DiskBasedCache;
//import com.android.volley.toolbox.HurlStack;
//import com.android.volley.toolbox.JsonArrayRequest;
//import com.github.mikephil.charting.charts.BarChart;
//
//import com.github.mikephil.charting.components.Legend;
//import com.github.mikephil.charting.components.XAxis;
//import com.github.mikephil.charting.components.YAxis;
//import com.github.mikephil.charting.data.BarData;
//import com.github.mikephil.charting.data.BarDataSet;
//import com.github.mikephil.charting.data.BarEntry;
//import com.github.mikephil.charting.data.CombinedData;
//import com.github.mikephil.charting.data.Entry;
//import com.github.mikephil.charting.data.LineData;
//import com.github.mikephil.charting.data.LineDataSet;
//import com.github.mikephil.charting.utils.ColorTemplate;
//import com.github.mikephil.charting.utils.ViewPortHandler;
//import com.google.gson.Gson;
//
//import org.json.JSONArray;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
//import apsupportapp.aperotechnologies.com.designapp.DashBoardActivity;
//import apsupportapp.aperotechnologies.com.designapp.R;
//import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
//import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesFilterActivity;
//import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisListDisplay;
//import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisViewPagerValue;
//import info.hoang8f.android.segmented.SegmentedGroup;
//
//
//public class SalesPvAActivity1 extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
//
//
//    RadioButton radioButton;
//    public static String salesPvA_SegmentClick;
//    ArrayList<SalesAnalysisListDisplay> salesAnalysisClassArrayList;
//    RelativeLayout tableRelLayout,relChartLayout;
//    static LinearLayout llayoutSalesPvA;
//
//    TextView txtStoreCode, txtStoreDesc;
//    String userId, bearertoken;
//    SharedPreferences sharedPreferences;
//    int offsetvalue = 0, limit = 100;
//    int count = 0;
//    RequestQueue queue;
//    SalesPvAAdapter salesPvAAdapter;
//    ViewPortHandler handler;
//    Context context;
//    CombinedData pvaChartData;
//    BarChart barChart;
//    ArrayList<BarEntry> BARENTRY ;
//    ArrayList<String> BarEntryLabels ;
//    BarDataSet Bardataset ;
//    ListView listViewSalesPvA;
//    int focusposition = 0;
//    //CombinedData data;
//    BarData bar_Data;
//    int level;
//    SalesAnalysisListDisplay salesAnalysisListDisplay;
//    static String fromWhere = "Department";
//    static TextView txtheaderplanclass;
//    RelativeLayout btnBack,btnFilter;
//    RelativeLayout btnSalesPrev,btnSalesNext;
//    Gson gson;
//
//    String pvaFirstVisibleItem;
//
//    ArrayList<SalesAnalysisViewPagerValue> pvaChartArrayList;
//    SalesAnalysisViewPagerValue salesAnalysisViewPagerValue;
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_salespva);
//        fromWhere = "Department";
//        context = this;
//        DisplayMetrics metrics = getResources().getDisplayMetrics();
//        int densityDpi = (int)(metrics.density * 160f);
//
//        Log.e("densityDPi---",""+densityDpi);
//        // Display device width and height in pixels
//        int screenHeightpx = metrics.heightPixels;
//        int screenWidthpx  = metrics.widthPixels;
//
//        Log.e("screen height--",""+screenHeightpx + "screen width"+screenWidthpx);
//
//        getSupportActionBar().hide();
//        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        userId = sharedPreferences.getString("userId", "");
//        bearertoken = sharedPreferences.getString("bearerToken", "");
//        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
//        BasicNetwork network = new BasicNetwork(new HurlStack());
//        queue = new RequestQueue(cache, network);
//        queue.start();
//        gson = new Gson();
//        txtStoreCode = (TextView) findViewById(R.id.txtStoreCode);
//        txtStoreDesc = (TextView) findViewById(R.id.txtStoreName);
//        level = 1;
//        salesPvA_SegmentClick = "WTD";
//        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
//        radioButton = (RadioButton) findViewById(R.id.btn_wtd);
//        radioButton.toggle();
//        llayoutSalesPvA = (LinearLayout)findViewById(R.id.llayoutSalesPvA);
//        //llayoutSalesPvA.setVisibility(View.GONE);
//
//        tableRelLayout=(RelativeLayout)findViewById(R.id.relTablelayout);
//        relChartLayout=(RelativeLayout)findViewById(R.id.relChartlayout);
//        barChart = (BarChart) findViewById(R.id.barline_chart);
//        handler = barChart.getViewPortHandler();
//        handler.getScaleX();
//        // handler.getScaleY();
//
//        Log.e("scale x---", "" + handler.getScaleX());
//        SegmentedGroup segmented3 = (SegmentedGroup) findViewById(R.id.segmented3);
//        segmented3.setOnCheckedChangeListener(SalesPvAActivity1.this);
//        initializeValues(context);
//        txtheaderplanclass=(TextView)findViewById(R.id.txtPlanClass);
//        Log.e("text value",""+txtheaderplanclass.getText().toString());
//
//        listViewSalesPvA = (ListView)findViewById(R.id.list);
//        listViewSalesPvA.addFooterView(getLayoutInflater().inflate(R.layout.list_footer,null));
//
//        if (Reusable_Functions.chkStatus(context)) {
//            Reusable_Functions.hDialog();
//            Reusable_Functions.sDialog(context, "Loading data...");
//            offsetvalue = 0;
//            limit = 100;
//            count = 0;
//            level = 1;
//            requestSalesListDisplayAPI();
//
//        } else {
//            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
//        }
//
//        btnSalesPrev=(RelativeLayout)findViewById(R.id.btnSalesBack);
//        btnSalesPrev.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                switch (txtheaderplanclass.getText().toString()) {
//
//                    case "Brand Class":
//                        txtheaderplanclass.setText("Brand");
//                        fromWhere = "Brand";
//                        level = 4;
//                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
//                        llayoutSalesPvA.setVisibility(View.GONE);
//                        if (Reusable_Functions.chkStatus(context)) {
//
//                            Reusable_Functions.hDialog();
//                            Reusable_Functions.sDialog(context, "Loading data...");
//                            offsetvalue = 0;
//                            limit = 100;
//                            count = 0;
//                            Log.e("Brand Class Prev-- ","  ");
//                            requestSalesListDisplayAPI();
//                            Log.e("prev 1",""+salesAnalysisListDisplay.getBrandName());
//
//                        } else {
//                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
//                        }
//
//                        break;
//
//                    case "Brand":
//
//
//                        txtheaderplanclass.setText("Plan Class");
//                        fromWhere = "Plan Class";
//                        level = 3;
//                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
//
//                        llayoutSalesPvA.setVisibility(View.GONE);
//
//                        if (Reusable_Functions.chkStatus(context)) {
//
//                            Reusable_Functions.hDialog();
//                            Reusable_Functions.sDialog(context, "Loading data...");
//                            offsetvalue = 0;
//                            limit = 100;
//                            count = 0;
//                            Log.e("Brand name prev","--");
//                            requestSalesListDisplayAPI();
//                            Log.e("prev 2",""+salesAnalysisListDisplay.getPlanClass());
//
//                        } else {
//                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
//                        }
//                        Log.e("---2---", " ");
//
//                        break;
//
//
//                    case "Plan Class":
//
//                        txtheaderplanclass.setText("Category");
//                        fromWhere = "Category";
//                        level = 2;
//                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
//
//                        llayoutSalesPvA.setVisibility(View.GONE);
//                        if (Reusable_Functions.chkStatus(context)) {
//
//                            Reusable_Functions.hDialog();
//                            Reusable_Functions.sDialog(context, "Loading data...");
//                            offsetvalue = 0;
//                            limit = 100;
//                            count = 0;
//                            Log.e("Plan class prev","");
//                            requestSalesListDisplayAPI();
//
//
//                        } else {
//                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
//                        }
//                        Log.e("---3---", " ");
//
//                        break;
//
//                    case "Category":
//
//                        txtheaderplanclass.setText("Department");
//                        fromWhere = "Department";
//                        level = 1;
//                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
//
//                        llayoutSalesPvA.setVisibility(View.GONE);
//                        if (Reusable_Functions.chkStatus(context)) {
//
//                            Reusable_Functions.hDialog();
//                            Reusable_Functions.sDialog(context, "Loading data...");
//                            offsetvalue = 0;
//                            limit = 100;
//                            count = 0;
//                            Log.e("Category prev","");
//                            requestSalesListDisplayAPI();
//                            Log.e("prev 4",""+salesAnalysisListDisplay.getPlanDept());
//
//                        } else {
//                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
//                        }
//                        Log.e("---4---", " ");
//
//                        break;
//                    default:
//                }
//
//            }
//
//        });
//
//        btnSalesNext=(RelativeLayout)findViewById(R.id.btnSalesNext);
//        btnSalesNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                switch (txtheaderplanclass.getText().toString()) {
//
//                    case "Department":
//
//                        // btnSalesPrev.setVisibility(View.VISIBLE);
//
//                        txtheaderplanclass.setText("Category");
//
//                        fromWhere = "Category";
//                        level = 2;
//                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
//                        llayoutSalesPvA.setVisibility(View.GONE);
//                        if (Reusable_Functions.chkStatus(context)) {
//
//                            Reusable_Functions.hDialog();
//                            Reusable_Functions.sDialog(context, "Loading data...");
//                            offsetvalue = 0;
//                            limit = 100;
//                            count = 0;
//                            Log.i("dept next","-----");
//                            requestSalesListDisplayAPI();
//                        } else {
//                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
//                        }
//
//                        break;
//
//                    case "Category":
//                        fromWhere = "Plan Class";
//                        txtheaderplanclass.setText("Plan Class");
//                        level = 3;
//
//                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
//                        llayoutSalesPvA.setVisibility(View.GONE);
//
//                        if (Reusable_Functions.chkStatus(context)) {
//
//                            Reusable_Functions.hDialog();
//                            Reusable_Functions.sDialog(context, "Loading data...");
//                            offsetvalue = 0;
//                            limit = 100;
//                            count = 0;
//                            Log.e("category next --","");
//                            requestSalesListDisplayAPI();
//                            Log.e("next 2",""+salesAnalysisListDisplay.getPlanClass());
//
//                        } else {
//                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
//                        }
//                        Log.e("---2---", " ");
//
//                        break;
//
//
//                    case "Plan Class":
//                        txtheaderplanclass.setText("Brand");
//                        fromWhere = "Brand";
//                        level = 4;
//
//                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
//                        llayoutSalesPvA.setVisibility(View.GONE);
//
//                        if (Reusable_Functions.chkStatus(context)) {
//
//                            Reusable_Functions.hDialog();
//                            Reusable_Functions.sDialog(context, "Loading data...");
//                            offsetvalue = 0;
//                            limit = 100;
//                            count = 0;
//                            requestSalesListDisplayAPI();
//
//                        } else {
//                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
//                        }
//                        Log.e("---3--", " ");
//
//                        break;
//
//                    case "Brand":
//                        txtheaderplanclass.setText("Brand Class");
//
//                        fromWhere = "Brand Plan Class";
//                        level = 5;
//                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
//                        llayoutSalesPvA.setVisibility(View.GONE);
//                        if (Reusable_Functions.chkStatus(context)) {
//                            Reusable_Functions.hDialog();
//                            Reusable_Functions.sDialog(context, "Loading data...");
//                            offsetvalue = 0;
//                            limit = 100;
//                            count = 0;
//                            requestSalesListDisplayAPI();
//                        } else {
//                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
//                        }
//                        Log.e("---4---", " ");
//
//                        break;
//                    default:
//                }
//            }
//        });
//
//        listViewSalesPvA.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                if (scrollState == SCROLL_STATE_IDLE) {
//
//                    if (salesAnalysisClassArrayList.size() != 0) {
//                        //listView_SalesAnalysis.smoothScrollToPosition(firstVisibleItem);
//                        if (view.getFirstVisiblePosition() <= salesAnalysisClassArrayList.size() - 1) {
//                            focusposition = view.getFirstVisiblePosition();
//                            listViewSalesPvA.setSelection(view.getFirstVisiblePosition());
////                            //Log.e("focusposition", " " + firstVisibleItem + " " + productNameBeanArrayList.get(firstVisibleItem).getProductName());
////                            if (txtheaderplanclass.getText().toString().equals("Department")) {
////                                pvaFirstVisibleItem = salesAnalysisClassArrayList.get(listViewSalesPvA.getFirstVisiblePosition()).getPlanDept().toString();
////                            } else if (txtheaderplanclass.getText().toString().equals("Category")) {
////                                pvaFirstVisibleItem = salesAnalysisClassArrayList.get(listViewSalesPvA.getFirstVisiblePosition()).getPlanCategory().toString();
////                            } else if (txtheaderplanclass.getText().toString().equals("Plan Class")) {
////                                pvaFirstVisibleItem = salesAnalysisClassArrayList.get(listViewSalesPvA.getFirstVisiblePosition()).getPlanClass().toString();
////                            } else if (txtheaderplanclass.getText().toString().equals("Brand")) {
////                                pvaFirstVisibleItem = salesAnalysisClassArrayList.get(listViewSalesPvA.getFirstVisiblePosition()).getBrandName().toString();
////                            } else if (txtheaderplanclass.getText().toString().equals("Brand Class")) {
////                                pvaFirstVisibleItem = salesAnalysisClassArrayList.get(listViewSalesPvA.getFirstVisiblePosition()).getBrandplanClass().toString();
////                            }
////                            if (focusposition != selFirstPositionValue) {
////                                if (Reusable_Functions.chkStatus(context)) {
////                                    Reusable_Functions.hDialog();
////                                    Reusable_Functions.sDialog(context, "Loading data...");
////                                    offsetvalue = 0;
////                                    limit = 100;
////                                    count = 0;
////                                    pvaChartArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
////                                    requestPvAChartAPI();
////
////                                } else {
////                                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
////                                }
////                            }
////                            selFirstPositionValue = focusposition;
//
//
//                        } else {
//                            focusposition = salesAnalysisClassArrayList.size() - 1;
//                            listViewSalesPvA.setSelection(focusposition);
////                            data = new CombinedData(getXAxisValues());
////                            BarData bardata = new BarData(getXAxisValues(), getDataSet());
////                            data.setData(bardata);
////                            LineData lineData = new LineData(getXAxisValues(),getLineDataSet());
////                            data.setData(lineData);
////                            barChart.animateXY(2000, 2000);
////                            barChart.setData(data);
////                            barChart.setDescription("");
////                            selFirstPositionValue = focusposition;
////                          barChart.notifyDataSetChanged();
//
//                        }
//                    }
//
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//
//            }
//        });
//
//
//        btnBack=(RelativeLayout)findViewById(R.id.imageBtnBack);
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SalesPvAActivity1.this,DashBoardActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//
//        btnFilter=(RelativeLayout)findViewById(R.id.imgfilter);
//        btnFilter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent filterIntent= new Intent (SalesPvAActivity1.this,SalesFilterActivity.class);
//                filterIntent.putExtra("checkfrom", "pvaAnalysis");
//                startActivity(filterIntent);
//            }
//        });
//    }
//
//
//    public void initializeValues(Context context) {
//
//        Log.e("initializeValues---", "");
//
//        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
//
////        data = new CombinedData();
////        pvaChartData = new CombinedData();
//        XAxis xAxis = barChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.TOP);
//        xAxis.setDrawGridLines(false);
//
//        YAxis leftAxis = barChart.getAxisLeft();
//        //leftAxis.setTextColor(ColorTemplate.getHoloBlue());
//        leftAxis.setDrawGridLines(true);
//
//        YAxis rightAxis = barChart.getAxisRight();
//        rightAxis.setEnabled(false);
//        rightAxis.setDrawAxisLine(false);
//        rightAxis.setTextColor(Color.WHITE);
//        rightAxis.setDrawGridLines(true);
//
//       // barChart.setHighlightEnabled(true);
//        barChart.setTouchEnabled(true);
//        barChart.setDragEnabled(true);
//        barChart.setScaleEnabled(true);
//        barChart.setDrawGridBackground(false);
//        barChart.setBackgroundColor(Color.WHITE);
//        Legend l = barChart.getLegend();
//        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
//        l.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
//        l.setEnabled(true);
//
//    }
//
//
//    public static void setListViewHeightBasedOnChildren(ListView listViewSales) {
//        SalesPvAAdapter salesPvAAdapter =  ((SalesPvAAdapter)((HeaderViewListAdapter)listViewSales.getAdapter()).getWrappedAdapter());
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
//
//    @Override
//    public void onCheckedChanged(RadioGroup group, int checkedId) {
//        switch (checkedId) {
//
//            case R.id.btn_wtd:
//                //Toast.makeText(SalesPvAActivity.this, "WTD", Toast.LENGTH_SHORT).show();
//                if (salesPvA_SegmentClick.equals("WTD"))
//                    break;
//                salesPvA_SegmentClick = "WTD";
//                llayoutSalesPvA.setVisibility(View.GONE);
//                salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
//                if (Reusable_Functions.chkStatus(context)) {
//                    Reusable_Functions.hDialog();
//                    Reusable_Functions.sDialog(context, "Loading data...");
//                    offsetvalue = 0;
//                    limit = 100;
//                    count = 0;
//                    requestSalesListDisplayAPI();
//
//                } else {
//                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
//                }
//                Log.e("-----WTD-----"," ");
//                break;
//
//            case R.id.btn_lw:
//                //Toast.makeText(SalesPvAActivity.this, "LW", Toast.LENGTH_SHORT).show();
//                if (salesPvA_SegmentClick.equals("LW"))
//                    break;
//
//                salesPvA_SegmentClick = "LW";
//                llayoutSalesPvA.setVisibility(View.GONE);
//                salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
//                if (Reusable_Functions.chkStatus(context)) {
//                    Reusable_Functions.hDialog();
//                    Reusable_Functions.sDialog(context, "Loading data...");
//                    offsetvalue = 0;
//                    limit = 100;
//                    count = 0;
//                    requestSalesListDisplayAPI();
//
//                } else {
//                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
//                }
//
//                Log.e("-----lw-----"," ");
//
//
//                break;
////
//            default:
//                break;
//
//        }
//    }
//
//    private void requestSalesListDisplayAPI() {
//        String salespva_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + salesPvA_SegmentClick  + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit;
//        Log.e("url", " " + salespva_listurl);
//
//        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, salespva_listurl,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.i("Sales Analysis Class: ", " " + response);
//                        Log.i("response length", "" + response.length());
//
//                        try {
//                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
//                                Reusable_Functions.hDialog();
//                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
//                            } else if (response.length() == limit) {
//                                for (int i = 0; i < response.length(); i++) {
//                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
//                                    salesAnalysisClassArrayList.add(salesAnalysisListDisplay);
//                                }
//                                offsetvalue = (limit * count) + limit;
//                                count++;
//                                requestSalesListDisplayAPI();
//
//                            } else if (response.length() < limit) {
//                                for (int i = 0; i < response.length(); i++) {
//                                    salesAnalysisListDisplay = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
//                                    salesAnalysisClassArrayList.add(salesAnalysisListDisplay);
//                                }
//
//                                salesPvAAdapter = new SalesPvAAdapter(salesAnalysisClassArrayList, context, fromWhere, listViewSalesPvA);
//                                listViewSalesPvA.setAdapter(salesPvAAdapter);
//                                txtStoreCode.setText(salesAnalysisListDisplay.getStoreCode());
//                                txtStoreDesc.setText(salesAnalysisListDisplay.getStoreDesc());
////                                data = new CombinedData(getXAxisValues());
//                                bar_Data = new BarData(getXAxisValues(), (IBarDataSet) getDataSet());
//
////                                LineData lineData = new LineData(getXAxisValues(),getLineDataSet());
////                                data.setData(lineData);
//
//                                barChart.setData(bar_Data);
//                                barChart.setDescription("");
//                                //barChart.notifyDataSetChanged();
//                                llayoutSalesPvA.setVisibility(View.VISIBLE);
//
//                                Reusable_Functions.hDialog();
//                            }
//
//                        } catch (Exception e) {
//                            Reusable_Functions.hDialog();
//                            Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Reusable_Functions.hDialog();
//                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
//                        error.printStackTrace();
//                    }
//                }
//
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("Content-Type", "application/json");
//                params.put("Authorization", "Bearer " + bearertoken);
//                return params;
//            }
//        };
//        int socketTimeout = 60000;//5 seconds
//
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        postRequest.setRetryPolicy(policy);
//        queue.add(postRequest);
//
//    }
//
//    private ArrayList<String> getXAxisValues() {
//
//        ArrayList<String> xAxis = new ArrayList<>();
//        for (int j = 0; j < salesAnalysisClassArrayList.size(); j++) {
//            if (txtheaderplanclass.getText().toString().equals("Department")) {
//                xAxis.add(String.valueOf(salesAnalysisClassArrayList.get(j).getPlanDept()));
//            } else if (txtheaderplanclass.getText().toString().equals("Category")) {
//                xAxis.add(String.valueOf(salesAnalysisClassArrayList.get(j).getPlanCategory()));
//            } else if (txtheaderplanclass.getText().toString().equals("Plan Class")) {
//                xAxis.add(String.valueOf(salesAnalysisClassArrayList.get(j).getPlanClass()));
//            } else if (txtheaderplanclass.getText().toString().equals("Brand")) {
//                xAxis.add(String.valueOf(salesAnalysisClassArrayList.get(j).getBrandName()));
//            } else if (txtheaderplanclass.getText().toString().equals("Brand Plan Class")) {
//                xAxis.add(String.valueOf(salesAnalysisClassArrayList.get(j).getBrandplanClass()));
//            }
//        }
//        return xAxis;
//    }
//    private ArrayList<String> getpvaXAxis()
//    {
//        ArrayList<String> pvaxAxis = new ArrayList<>();
//        for (int j = 0; j < pvaChartArrayList.size(); j++) {
//            pvaxAxis.add(String.valueOf(j));
//            Log.e("pvaxAxis size", "" + pvaxAxis.add(String.valueOf(j)));
//        }
//        return pvaxAxis;
//    }
//
//    public LineDataSet getLineDataSet()
//    {
//        ArrayList<Entry> entry = new ArrayList<>();
//        for (int i = 0; i < salesAnalysisClassArrayList.size(); i++) {
//            Log.e("line data", "" + salesAnalysisClassArrayList.get(i).getPvaAchieved());
//            float value = (float) salesAnalysisClassArrayList.get(i).getPvaAchieved();
//            Entry lineValues = new Entry(value, i);
//            entry.add(lineValues);
//
//        }
//        LineDataSet linedataSet = new LineDataSet(entry,"PvAAchieved");
//        linedataSet.setDrawValues(false);
//        linedataSet.setColor(Color.parseColor("#ed5752"));
//        return linedataSet;
//    }
//
//    public LineDataSet getpvaLineDataSet()
//    {
//        ArrayList<Entry> pvaentry = new ArrayList<>();
//        for (int i = 0; i < pvaChartArrayList.size(); i++) {
//            Log.e("line data", "" + pvaChartArrayList.get(i).getPvaAchieved());
//            float value = (float) pvaChartArrayList.get(i).getPvaAchieved();
//            Entry pvalineValues = new Entry(value, i);
//            pvaentry.add(pvalineValues);
//
//        }
//        LineDataSet pvalinedataSet = new LineDataSet(pvaentry,"PvAAchieved");
//        pvalinedataSet.setDrawValues(false);
//        pvalinedataSet.setColor(Color.MAGENTA);
//        return pvalinedataSet;
//    }
//    // this method is used to create data for Bar graph<br />
//    public ArrayList<BarDataSet> getDataSet() {
//
//        ArrayList<BarDataSet> dataSets = null;
//        ArrayList<BarEntry> group1 = new ArrayList<>();
//
//        for (int i = 0; i < salesAnalysisClassArrayList.size(); i++) {
//            Log.e("getSaleNetVal--", "" + salesAnalysisClassArrayList.get(i).getPlanSaleNetVal());
//            float value = (float) salesAnalysisClassArrayList.get(i).getPlanSaleNetVal();
//            BarEntry barchartValues1 = new BarEntry(value, i);
//            group1.add(barchartValues1);
//        }
//
//        ArrayList<BarEntry> group2 = new ArrayList();
//        for (int i = 0; i < salesAnalysisClassArrayList.size(); i++)
//
//        {
//            Log.e("getPlanSaleNetVal--", "" + salesAnalysisClassArrayList.get(i).getSaleNetVal());
//            float value2 = (float) salesAnalysisClassArrayList.get(i).getSaleNetVal();
//            BarEntry barchartValues2 = new BarEntry(value2, i);
//            group2.add(barchartValues2);
//        }
//
//        BarDataSet barDataSet = new BarDataSet(group1, "Plan Sales");
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
//
//
//    // this method is used to create data for Bar graph<br for on scroll />
//    public ArrayList<BarDataSet> getpvaBarDataSet() {
//
//        ArrayList<BarDataSet> pvadataSets = null;
//        ArrayList<BarEntry> pvachartnetSale = new ArrayList<>();
//
//        for (int i = 0; i < pvaChartArrayList.size(); i++) {
//            Log.e("getSaleNetVal--", "" + pvaChartArrayList.get(i).getSaleNetVal());
//            float value = (float) pvaChartArrayList.get(i).getSaleNetVal();
//            BarEntry pvaBarchart1= new BarEntry(value, i);
//            pvachartnetSale.add(pvaBarchart1);
//        }
//
//        ArrayList<BarEntry> pvachartplanSale = new ArrayList();
//        for (int i = 0; i < pvaChartArrayList.size(); i++)
//
//        {
//            Log.e("getPlanSaleNetVal--", "" + pvaChartArrayList.get(i).getPlanSaleNetVal());
//            float value2 = (float) pvaChartArrayList.get(i).getPlanSaleNetVal();
//            BarEntry pvaBarchart2 = new BarEntry(value2, i);
//            pvachartplanSale.add(pvaBarchart2);
//        }
//
//        BarDataSet pvabarDataSet = new BarDataSet(pvachartnetSale, "Net Sales");
//        pvabarDataSet.setColor(Color.GREEN);
//        pvabarDataSet.setDrawValues(false);
//        BarDataSet pvabarDataSet1 = new BarDataSet(pvachartplanSale, "Plan Sales");
//        pvabarDataSet1.setDrawValues(false);
//        pvabarDataSet1.setColor(Color.RED);
//
//        pvadataSets = new ArrayList<>();
//        pvadataSets.add(pvabarDataSet);
//        pvadataSets.add(pvabarDataSet1);
//        return pvadataSets;
//    }
//
//
//    private void requestPvAChartAPI() {
//        Log.e("Department onsroll api", "" + pvaFirstVisibleItem);
//
//        String url = "";
//
//        if (txtheaderplanclass.getText().toString().equals("Department")) {
//
//            url = ConstsCore.web_url + "/v1/display/salesanalysisbytime/" + userId + "?view=" + salesPvA_SegmentClick + "&department=" + pvaFirstVisibleItem.toUpperCase().replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
//        }
//        else if (txtheaderplanclass.getText().toString().equals("Category")) {
//
//            url = ConstsCore.web_url + "/v1/display/salesanalysisbytime/" + userId + "?view=" + salesPvA_SegmentClick + "&category=" + pvaFirstVisibleItem.toUpperCase().replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
//        }
//        else if (txtheaderplanclass.getText().toString().equals("Plan Class")) {
//
//            url = ConstsCore.web_url + "/v1/display/salesanalysisbytime/" + userId + "?view=" + salesPvA_SegmentClick + "&class=" + pvaFirstVisibleItem.toUpperCase().replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
//        }
//        else if (txtheaderplanclass.getText().toString().equals("Brand")) {
//
//            url = ConstsCore.web_url + "/v1/display/salesanalysisbytime/" + userId + "?view=" + salesPvA_SegmentClick + "&brand=" + pvaFirstVisibleItem.toUpperCase().replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
//        }
//        else if (txtheaderplanclass.getText().toString().equals("Brand Class")) {
//
//            url = ConstsCore.web_url + "/v1/display/salesanalysisbytime/" + userId + "?view=" + salesPvA_SegmentClick + "&brandclass=" + pvaFirstVisibleItem.toUpperCase().replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
//        }
//        Log.e("Url", "" + url);
//
//        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
////                        Log.i("Sales Pva Chart on Scroll  : ", " " + response);
////                        Log.i("Sales Pva Chart response", "" + response.length());
//                        try {
//
//                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
//                                Reusable_Functions.hDialog();
//                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
//
//                            } else if (response.length() == limit) {
//                                for (int i = 0; i < response.length(); i++) {
//
//                                    salesAnalysisViewPagerValue = gson.fromJson(response.get(i).toString(), SalesAnalysisViewPagerValue.class);
//                                    pvaChartArrayList.add(salesAnalysisViewPagerValue);
//                                }
//                                offsetvalue = (limit * count) + limit;
//                                count++;
//                                requestPvAChartAPI();
//
//
//                            } else if (response.length() < limit) {
//                                for (int i = 0; i < response.length(); i++) {
//
//                                    salesAnalysisViewPagerValue = gson.fromJson(response.get(i).toString(), SalesAnalysisViewPagerValue.class);
//                                    pvaChartArrayList.add(salesAnalysisViewPagerValue);
//                                }
//                            }
//
//                            Log.e("analysisArrayList", " --- " + pvaChartArrayList.size());
////                            pvaChartData = new CombinedData();
////                            BarData pvabardata = new BarData(getpvaXAxis(), getpvaBarDataSet());
////                            pvaChartData.setData(pvabardata);
//////                            LineData pvalineData = new LineData(getpvaXAxis(),getpvaLineDataSet());
//////                            pvaChartData.setData(pvalineData);
////                            barChart.animateXY(2000, 2000);
////                            barChart.setData(pvaChartData);
////                            barChart.setDescription("");
//                            llayoutSalesPvA.setVisibility(View.VISIBLE);
//                            Reusable_Functions.hDialog();
//                        } catch (Exception e) {
//                            Reusable_Functions.hDialog();
//                            Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Reusable_Functions.hDialog();
//                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
//                        error.printStackTrace();
//                    }
//                }
//
//
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("Content-Type", "application/json");
//                params.put("Authorization", "Bearer " + bearertoken);
//                return params;
//            }
//        };
//        int socketTimeout = 60000;//5 seconds
//
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        postRequest.setRetryPolicy(policy);
//        queue.add(postRequest);
//
//    }
//
//    @Override
//    public void onBackPressed() {
//
//        Intent intent = new Intent(SalesPvAActivity1.this,DashBoardActivity.class);
//        startActivity(intent);
//        finish();
//    }
//}
