package apsupportapp.aperotechnologies.com.designapp.FreshnessIndex;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

//import com.github.mikephil.charting.formatter.LargeValueFormatter;
//import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.DashBoardActivity;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.model.FreshnessIndexDetails;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by pamrutkar on 22/11/16.
 */

public class FreshnessIndexActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    RadioButton radioButton;
    public String FIndex_SegmentClick;
    ArrayList<FreshnessIndexDetails> freshnessIndexDetailsArrayList;
    TextView txtStoreCode, txtStoreDesc, txtFIndexClass, txtfIndexDeptName;
    String userId, bearertoken;
    SharedPreferences sharedPreferences;
    int offsetvalue = 0, limit = 100;
    int count = 0;
    RequestQueue queue;
    String FreshnessIndexValue;
    Context context;
    String fromWhere, fIndexFirstVisibleItem, freshnessIndex_ClickedVal, fIndexPlanDept, fIndexCategory, fIndexPlanClass, fIndexBrand;
    PieChart pieChart;
    ListView listViewFIndex;
    int focusposition, selFirstPositionValue;
    LinearLayout llfreshnessIndex, llfIndexhierarchy;
    SegmentedGroup segmented3;
    int level;
    FreshnessIndexDetails freshnessIndexDetails;
    RelativeLayout freshnessIndex_imageBtnBack, freshnessIndex_imgfilter;
    RelativeLayout btnFIndexPrev, btnFIndexNext;
    Gson gson;
    FreshnessIndexAdapter fIndexAdapter;
    PieData pieData;
    float upcoming = 0.0f, oldgroup = 0.0f, previousgroup = 0.0f, currentgroup = 0.0f;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freshness_index);
        getSupportActionBar().hide();

        fromWhere = "Department";
        FIndex_SegmentClick = "Core";
        fIndexFirstVisibleItem = "";
        freshnessIndex_ClickedVal = "";
        FreshnessIndexValue = "";
        context = this;
        focusposition = 0;
        selFirstPositionValue = 0;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();
        initializeUI();
        freshnessIndex_imageBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FreshnessIndexActivity.this, DashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            offsetvalue = 0;
            limit = 100;
            count = 0;
            level = 1;
            llfIndexhierarchy.setVisibility(View.GONE);
            requestFreshnessIndexDetails();

        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }


        // previous
        btnFIndexPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (txtFIndexClass.getText().toString()) {

                    case "Brand Plan Class":
                        txtFIndexClass.setText("Brand");
                        fromWhere = "Brand";
                        level = 4;
                        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
                        llfIndexhierarchy.setVisibility(View.GONE);
                        llfreshnessIndex.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.e("Brand Plan Class Prev-- ", "  ");
                            requestFreshnessIndexDetails();
                            Log.e("prev 1", "" + freshnessIndexDetails.getBrandName());

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case "Brand":
                        txtFIndexClass.setText("Plan Class");
                        fromWhere = "Plan Class";
                        level = 3;
                        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
                        llfIndexhierarchy.setVisibility(View.GONE);
                        llfreshnessIndex.setVisibility(View.GONE);

                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.e("Brand name prev", "--");
                            requestFreshnessIndexDetails();
                            Log.e("prev 2", "" + freshnessIndexDetails.getPlanClass());

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("---2---", " ");

                        break;


                    case "Plan Class":
                        txtFIndexClass.setText("Category");
                        fromWhere = "Category";
                        level = 2;
                        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
                        llfIndexhierarchy.setVisibility(View.GONE);
                        llfreshnessIndex.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.e("Plan class prev", "");
                            requestFreshnessIndexDetails();
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("---3---", " ");

                        break;

                    case "Category":
                        txtFIndexClass.setText("Department");
                        fromWhere = "Department";
                        level = 1;
                        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
                        llfIndexhierarchy.setVisibility(View.GONE);
                        llfreshnessIndex.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.e("Category prev", "");
                            requestFreshnessIndexDetails();
                            Log.e("prev 4", "" + freshnessIndexDetails.getPlanDept());

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("---4---", " ");

                        break;
                    default:
                }
            }

        });

        // next-----
        btnFIndexNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (txtFIndexClass.getText().toString()) {

                    case "Department":
                        txtFIndexClass.setText("Category");
                        fromWhere = "Category";
                        level = 2;
                        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
                        llfIndexhierarchy.setVisibility(View.GONE);
                        llfreshnessIndex.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.i("dept next", "-----");
                            requestFreshnessIndexDetails();
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case "Category":
                        fromWhere = "Plan Class";
                        txtFIndexClass.setText("Plan Class");
                        level = 3;
                        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
                        llfIndexhierarchy.setVisibility(View.GONE);
                        llfreshnessIndex.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.e("category next --", "");
                            requestFreshnessIndexDetails();
                            Log.e("next 2", "" + freshnessIndexDetails.getPlanClass());

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("---2---", " ");

                        break;
                    case "Plan Class":
                        txtFIndexClass.setText("Brand");
                        fromWhere = "Brand";
                        level = 4;
                        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
                        //llpvahierarchy.setVisibility(View.GONE);
                        llfreshnessIndex.setVisibility(View.GONE);

                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            requestFreshnessIndexDetails();

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("---3--", " ");

                        break;

                    case "Brand":
                        txtFIndexClass.setText("Brand Plan Class");

                        fromWhere = "Brand Plan Class";
                        level = 5;
                        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
                        llfIndexhierarchy.setVisibility(View.GONE);
                        llfreshnessIndex.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            requestFreshnessIndexDetails();
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("---4---", " ");

                        break;
                    default:
                }
            }
        });
        // hierarchy level drill down on selected item click
        listViewFIndex.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (txtFIndexClass.getText().toString()) {

                    case "Department":
                        txtFIndexClass.setText("Category");
                        freshnessIndex_ClickedVal = freshnessIndexDetailsArrayList.get(position).getPlanDept();
                        Log.e("txtClicked department--", "" + freshnessIndex_ClickedVal);
                        llfreshnessIndex.setVisibility(View.GONE);
                        fromWhere = "Category";
                        level = 2;
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            freshnessIndexDetailsArrayList.clear();
                            Log.i("dept next", "-----");
                            request_FreshnessIndex_CategoryList(freshnessIndex_ClickedVal);
                            fIndexPlanDept = freshnessIndex_ClickedVal;

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case "Category":
                        txtFIndexClass.setText("Plan Class");
                        llfreshnessIndex.setVisibility(View.GONE);
                        freshnessIndex_ClickedVal = freshnessIndexDetailsArrayList.get(position).getPlanCategory();
                        Log.e("txtClicked category --", "" + freshnessIndex_ClickedVal);
                        fromWhere = "Plan Class";
                        level = 3;
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            freshnessIndexDetailsArrayList.clear();
                            Log.i("category next", "-----");
                            Log.i("come", "----" + fIndexPlanDept);
                            request_FreshnessIndex_PlanClassList(fIndexPlanDept, freshnessIndex_ClickedVal);
                            fIndexCategory = freshnessIndex_ClickedVal;
                            Log.e("fIndexCategory--", "" + fIndexCategory);
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "Plan Class":
                        txtFIndexClass.setText("Brand");
                        llfreshnessIndex.setVisibility(View.GONE);
                        freshnessIndex_ClickedVal = freshnessIndexDetailsArrayList.get(position).getPlanClass();
                        Log.e("txtClicked plan class---", "" + freshnessIndex_ClickedVal);
                        fromWhere = "Brand";
                        level = 4;
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            freshnessIndexDetailsArrayList.clear();
                            Log.i("Plan Class next", "-----");
                            request_FreshnessIndex_BrandList(fIndexPlanDept, fIndexCategory, freshnessIndex_ClickedVal);
                            fIndexPlanClass = freshnessIndex_ClickedVal;
                            Log.e("fIndexPlanClass---", "" + fIndexPlanClass);
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case "Brand":
                        txtFIndexClass.setText("Brand Plan Class");
                        llfreshnessIndex.setVisibility(View.GONE);
                        freshnessIndex_ClickedVal = freshnessIndexDetailsArrayList.get(position).getBrandName();
                        Log.e("txtSalesClickedValue3---", "" + freshnessIndex_ClickedVal);
                        fromWhere = "Brand Plan Class";
                        level = 5;
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            freshnessIndexDetailsArrayList.clear();
                            Log.i("brand next", "-----");
                            request_FreshnessIndex_BrandPlanList(fIndexPlanDept, fIndexCategory, fIndexPlanClass, freshnessIndex_ClickedVal);

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                }

            }

        });

        //list view on Scroll event

        listViewFIndex.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {

                    if (freshnessIndexDetailsArrayList.size() != 0) {
                        //listView_SalesAnalysis.smoothScrollToPosition(firstVisibleItem);
                        if (view.getFirstVisiblePosition() <= freshnessIndexDetailsArrayList.size() - 1) {
                            focusposition = view.getFirstVisiblePosition();
                            listViewFIndex.setSelection(view.getFirstVisiblePosition());
                            //Log.e("focusposition", " " + firstVisibleItem + " " + productNameBeanArrayList.get(firstVisibleItem).getProductName());
                            if (txtFIndexClass.getText().toString().equals("Department")) {
                                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(listViewFIndex.getFirstVisiblePosition()).getPlanDept().toString();
                            } else if (txtFIndexClass.getText().toString().equals("Category")) {
                                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(listViewFIndex.getFirstVisiblePosition()).getPlanCategory().toString();
                            } else if (txtFIndexClass.getText().toString().equals("Plan Class")) {
                                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(listViewFIndex.getFirstVisiblePosition()).getPlanClass().toString();
                            } else if (txtFIndexClass.getText().toString().equals("Brand")) {
                                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(listViewFIndex.getFirstVisiblePosition()).getBrandName().toString();
                            } else if (txtFIndexClass.getText().toString().equals("Brand Plan Class")) {
                                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(listViewFIndex.getFirstVisiblePosition()).getBrandplanClass().toString();
                            }
                            if (focusposition != selFirstPositionValue) {
                                if (Reusable_Functions.chkStatus(context)) {
                                    Reusable_Functions.hDialog();
                                    Reusable_Functions.sDialog(context, "Loading data...");
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    //freshnessIndexDetailsArrayList.clear();
                                    //pieData = new PieData();
                                    requestFIndexPieChart();
                                } else {
                                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                }
                            }
                            selFirstPositionValue = focusposition;


                        } else {
                            focusposition = freshnessIndexDetailsArrayList.size() - 1;
                            listViewFIndex.setSelection(focusposition);
                            selFirstPositionValue = focusposition;

                        }
                    }

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


    }

    private void initializeUI() {
        txtStoreCode = (TextView) findViewById(R.id.txtStoreCode);
        txtStoreDesc = (TextView) findViewById(R.id.txtStoreName);
        txtFIndexClass = (TextView) findViewById(R.id.txtFIndexClass);
        txtfIndexDeptName = (TextView) findViewById(R.id.txtfIndexDeptName);
        freshnessIndex_imageBtnBack = (RelativeLayout) findViewById(R.id.freshnessIndex_imageBtnBack);
        freshnessIndex_imgfilter = (RelativeLayout) findViewById(R.id.freshnessIndex_imgfilter);
        pieChart = (PieChart) findViewById(R.id.fIndex_pieChart);
        listViewFIndex = (ListView) findViewById(R.id.fIndex_list);
        listViewFIndex.addFooterView(getLayoutInflater().inflate(R.layout.list_footer, null));
        llfreshnessIndex = (LinearLayout) findViewById(R.id.llfreshnessIndex);
        llfIndexhierarchy = (LinearLayout) findViewById(R.id.llfIndexhierarchy);
        btnFIndexPrev = (RelativeLayout) findViewById(R.id.btnFIndexPrev);
        btnFIndexNext = (RelativeLayout) findViewById(R.id.btnFIndexNext);
        segmented3 = (SegmentedGroup) findViewById(R.id.freshnessIndex_segmentedGrp);
        segmented3.setOnCheckedChangeListener(FreshnessIndexActivity.this);
        radioButton = (RadioButton) findViewById(R.id.btnCore);
        radioButton.toggle();
        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {

            case R.id.btnCore:
                //Toast.makeText(SalesPvAActivity.this, "WTD", Toast.LENGTH_SHORT).show();
                if (FIndex_SegmentClick.equals("Core"))
                    break;
                FIndex_SegmentClick = "Core";
                llfIndexhierarchy.setVisibility(View.GONE);
                llfreshnessIndex.setVisibility(View.GONE);
                freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading data...");
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    requestFreshnessIndexDetails();

                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
                Log.e("-----core-----", " ");
                break;

            case R.id.btnFashion:
                //Toast.makeText(SalesPvAActivity.this, "LW", Toast.LENGTH_SHORT).show();
                if (FIndex_SegmentClick.equals("Fashion"))
                    break;

                FIndex_SegmentClick = "Fashion";
                llfIndexhierarchy.setVisibility(View.GONE);
                llfreshnessIndex.setVisibility(View.GONE);
                freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading data...");
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    requestFreshnessIndexDetails();

                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }

                Log.e("-----fashion-----", " ");


                break;

            default:
                break;

        }
    }
//    //------------------------Pie Chart DataSets--------------------------------//
//
//    public List<PieDataSet> getPieDataSet() {
//
//
//        // current group count
//        ArrayList<Entry> group1 = new ArrayList<Entry>();
//        for (int i = 0; i < freshnessIndexDetailsArrayList.size(); i++) {
//
//            float value = (float) freshnessIndexDetailsArrayList.get(i).getCurrentGroupCount();
//            Entry currentgroup = new Entry(value,i);
//            group1.add(currentgroup);
//        }
//        //previous group count
//        ArrayList<Entry> group2 = new ArrayList<Entry>();
//        for (int i = 0; i < freshnessIndexDetailsArrayList.size(); i++) {
//            float value2 = (float) freshnessIndexDetailsArrayList.get(i).getPreviousGroupCount();
//            Entry previousgroup = new Entry(value2, i);
//            group2.add(previousgroup);
//        }
//        //old group count
//        ArrayList<Entry> group3 = new ArrayList<Entry>();
//        for (int i = 0; i < freshnessIndexDetailsArrayList.size(); i++) {
//            float value3 = (float) freshnessIndexDetailsArrayList.get(i).getOldGroupCount();
//            Entry oldgroup = new Entry(value3, i);
//            group3.add(oldgroup);
//        }
//        //upcoming group count
//        ArrayList<Entry> group4 = new ArrayList<Entry>();
//        for (int i = 0; i < freshnessIndexDetailsArrayList.size(); i++) {
//            float value4 = (float) freshnessIndexDetailsArrayList.get(i).getUpcomingGroupCount();
//            Entry upcominggroup = new Entry(value4, i);
//            group4.add(upcominggroup);
//        }
//        ArrayList<PieDataSet> dataSets = new ArrayList<PieDataSet>();
//        PieDataSet pieCurrentDataSet = new PieDataSet(group1, "Current");
//        pieCurrentDataSet.setColor(Color.parseColor("#8857a6"));
//
//        PieDataSet piePreviousDataSet = new PieDataSet(group2, "Previous");
//        piePreviousDataSet.setColor(Color.parseColor("#b33d2f"));
//
//        PieDataSet pieOldDataSet = new PieDataSet(group3, "Old");
//        pieCurrentDataSet.setColor(Color.parseColor("#386e34"));
//
//        PieDataSet pieUpcomingDataSet = new PieDataSet(group4, "Upcoming");
//        pieCurrentDataSet.setColor(Color.parseColor("#308fab"));
//
//
//        dataSets.set(0,pieCurrentDataSet);
//        dataSets.set(1,piePreviousDataSet);
//        dataSets.set(2,pieOldDataSet);
//        dataSets.set(3,pieUpcomingDataSet);
//        return dataSets;
//    }
//
//    private ArrayList<String> getXAxisValues() {
//
//        ArrayList<String> xAxis = new ArrayList<>();
//
//        for (int j = 0; j < freshnessIndexDetailsArrayList.size(); j++) {
//            if (txtFIndexClass.getText().toString().equals("Department")) {
//                xAxis.add(String.valueOf(freshnessIndexDetailsArrayList.get(j).getPlanDept()));
//            } else if (txtFIndexClass.getText().toString().equals("Category")) {
//                xAxis.add(String.valueOf(freshnessIndexDetailsArrayList.get(j).getPlanCategory()));
//            } else if (txtFIndexClass.getText().toString().equals("Plan Class")) {
//                xAxis.add(String.valueOf(freshnessIndexDetailsArrayList.get(j).getPlanClass()));
//            } else if (txtFIndexClass.getText().toString().equals("Brand")) {
//                xAxis.add(String.valueOf(freshnessIndexDetailsArrayList.get(j).getBrandName()));
//            } else if (txtFIndexClass.getText().toString().equals("Brand Plan Class")) {
//                xAxis.add(String.valueOf(freshnessIndexDetailsArrayList.get(j).getBrandplanClass()));
//            }
//            Log.e("X axis values",xAxis.get(j).toString());
//        }
//
//        return xAxis;
//    }
//


    //----------------------------API Declaration---------------------------//
    // API 1.31
    private void requestFreshnessIndexDetails() {

        String fIdetails = ConstsCore.web_url + "/v1/display/freshnessindexdetail/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + fIdetails);


        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, fIdetails,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Freshness Index Details Class: ", " " + response);
                        Log.i("response length", "" + response.length());
                        int i;
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                            } else if (response.length() == limit) {
                                for (i = 0; i < response.length(); i++) {

                                    freshnessIndexDetails = gson.fromJson(response.get(i).toString(), FreshnessIndexDetails.class);
                                    freshnessIndexDetailsArrayList.add(freshnessIndexDetails);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestFreshnessIndexDetails();

                            } else if (response.length() < limit) {
                                for (i = 0; i < response.length(); i++) {

                                    freshnessIndexDetails = gson.fromJson(response.get(i).toString(), FreshnessIndexDetails.class);
                                    freshnessIndexDetailsArrayList.add(freshnessIndexDetails);
                                }
                                freshnessIndexDetails = new FreshnessIndexDetails();

                                if (txtFIndexClass.getText().toString().equals("Department")) {
                                    freshnessIndexDetails.setPlanDept("All");
                                } else if (txtFIndexClass.getText().toString().equals("Category")) {
                                    freshnessIndexDetails.setPlanCategory("All");
                                } else if (txtFIndexClass.getText().toString().equals("Plan Class")) {
                                    freshnessIndexDetails.setPlanClass("All");
                                } else if (txtFIndexClass.getText().toString().equals("Brand")) {
                                    freshnessIndexDetails.setBrandName("All");
                                } else if (txtFIndexClass.getText().toString().equals("Brand Plan Class")) {
                                    freshnessIndexDetails.setBrandplanClass("All");
                                }

                                freshnessIndexDetailsArrayList.add(0, freshnessIndexDetails);


                                fIndexAdapter = new FreshnessIndexAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex);
                                listViewFIndex.setAdapter(fIndexAdapter);
                                fIndexAdapter.notifyDataSetChanged();
                                txtStoreCode.setText(freshnessIndexDetailsArrayList.get(1).getStoreCode());
                                txtStoreDesc.setText(freshnessIndexDetailsArrayList.get(1).getStoreDescription());

                                ArrayList<Entry> entries = new ArrayList<>();
                                for (FreshnessIndexDetails fresh : freshnessIndexDetailsArrayList) {

                                    upcoming = (float) fresh.getUpcomingGroupCount();
                                    oldgroup = (float) fresh.getOldGroupCount();
                                    previousgroup = (float) fresh.getPreviousGroupCount();
                                    currentgroup = (float) fresh.getCurrentGroupCount();
                                    Log.e("Values", "" + upcoming + "\t" + oldgroup + "\t" + previousgroup + "\t" + currentgroup);
                                }
                                ArrayList<Integer> colors = new ArrayList<>();
                                colors.add(Color.parseColor("#8857a6"));
                                colors.add(Color.parseColor("#b33d2f"));
                                colors.add(Color.parseColor("#386e34"));
                                colors.add(Color.parseColor("#308fab"));
                                ArrayList<String> labels = new ArrayList<>();
                           if(currentgroup > -1)
                           {
                                labels.add("Current");
                                entries.add(new Entry(currentgroup, i));
                            }
                           if(previousgroup > -1)
                            {
                                labels.add("Previous");
                                entries.add(new Entry(previousgroup, i));
                                 }
                            if(oldgroup > -1)
                            {
                                labels.add("Old");
                                entries.add(new Entry(oldgroup, i));
                                }
                            if(upcoming > -1)
                            {
                                labels.add("Upcoming");
                                entries.add(new Entry(upcoming, i));
                                }

                                PieDataSet dataset = new PieDataSet(entries, "");
                                dataset.setColors(colors);
                                //dataset.setSliceSpace(3);
                                //dataset.setSelectionShift(5);
                                pieData = new PieData(labels, dataset);
                                //pieData.setValueFormatter(new MyValueFormatter());
                                pieData.setValueTextSize(10f);
                                pieData.setValueTextColor(Color.WHITE);
                                pieChart.setData(pieData);
                                pieChart.animateXY(4000, 4000);
                                pieChart.setDescription("");
                                //pieChart.invalidate();
                                Legend l = pieChart.getLegend();
                                l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
                                l.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
                                l.setEnabled(true);
                                llfreshnessIndex.setVisibility(View.VISIBLE);
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

    // For Category List on click of Dept Value
    private void request_FreshnessIndex_CategoryList(final String deptName) {

        String freshnessindex_category_listurl = ConstsCore.web_url + "/v1/display/freshnessindexdetail/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&dept=" + deptName.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + freshnessindex_category_listurl);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, freshnessindex_category_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("FreshNess Index Category List: ", " " + response);
                        Log.i("FreshNess Index Category List response length", "" + response.length());
                        int i;
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no category data found", Toast.LENGTH_SHORT).show();
                            } else if (response.length() == limit) {
                                for (i = 0; i < response.length(); i++) {

                                    freshnessIndexDetails = gson.fromJson(response.get(i).toString(), FreshnessIndexDetails.class);
                                    freshnessIndexDetailsArrayList.add(freshnessIndexDetails);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                request_FreshnessIndex_CategoryList(deptName);

                            } else if (response.length() < limit) {
                                for (i = 0; i < response.length(); i++) {

                                    freshnessIndexDetails = gson.fromJson(response.get(i).toString(), FreshnessIndexDetails.class);
                                    freshnessIndexDetailsArrayList.add(freshnessIndexDetails);
                                }

                                fIndexAdapter = new FreshnessIndexAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex);
                                listViewFIndex.setAdapter(fIndexAdapter);
                                fIndexAdapter.notifyDataSetChanged();
                                txtStoreCode.setText(freshnessIndexDetailsArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(freshnessIndexDetailsArrayList.get(0).getStoreDescription());
                                FreshnessIndexValue = " ";
                                FreshnessIndexValue = deptName;
                                txtfIndexDeptName.setText(FreshnessIndexValue);
                                llfIndexhierarchy.setVisibility(View.VISIBLE);

                                float upcoming = 0.0f;
                                float oldgroup = 0.0f;
                                float previousgroup = 0.0f;
                                float currentgroup = 0.0f;
                                ArrayList<Entry> entries = new ArrayList<>();
                                for (FreshnessIndexDetails fresh : freshnessIndexDetailsArrayList) {
                                    if (fIndexFirstVisibleItem.equalsIgnoreCase(fresh.getPlanCategory())) {

                                        upcoming = (float) fresh.getUpcomingGroupCount();
                                        oldgroup = (float) fresh.getOldGroupCount();
                                        previousgroup = (float) fresh.getPreviousGroupCount();
                                        currentgroup = (float) fresh.getCurrentGroupCount();

                                    }
                                    Log.e("Values", "" + fresh.getUpcomingGroupCount() + "\t" + fresh.getOldGroupCount() + "\t" + fresh.getPreviousGroupCount() + "\t" + fresh.getCurrentGroupCount());

                                }
                                ArrayList<Integer> colors = new ArrayList<>();
                                colors.add(Color.parseColor("#8857a6"));
                                colors.add(Color.parseColor("#b33d2f"));
                                colors.add(Color.parseColor("#386e34"));
                                colors.add(Color.parseColor("#308fab"));
                                ArrayList<String> labels = new ArrayList<>();
//                            if(currentgroup > 0.0f)
//                            {
                                labels.add("Current");
                                entries.add(new Entry(currentgroup, 0));
//                            }
//                            if(previousgroup > 0.0f)
//                            {
                                labels.add("Previous");
                                entries.add(new Entry(previousgroup, 1));
                                // }
//                            if(oldgroup > 0.0f)
//                            {
                                labels.add("Old");
                                entries.add(new Entry(oldgroup, 2));
                                //}
//                            if(upcoming > 0.0f)
//                            {
                                labels.add("Upcoming");
                                entries.add(new Entry(upcoming, 3));
                                //}
                                PieDataSet dataset = new PieDataSet(entries, "");
                                dataset.setColors(colors);
                                dataset.setSliceSpace(3);
                                dataset.setSelectionShift(5);
                                pieData = new PieData(labels, dataset);
                                //pieData.setValueFormatter(new MyValueFormatter());
                                pieData.setValueTextSize(10f);
                                pieData.setValueTextColor(Color.WHITE);
                                pieChart.setData(pieData);
                                pieChart.animateXY(4000, 4000);
                                pieChart.setDescription("");
                                pieChart.invalidate();
                                Legend l = pieChart.getLegend();
                                l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
                                l.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
                                l.setEnabled(true);
                                llfreshnessIndex.setVisibility(View.VISIBLE);
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

    // For Plan Class on click of Category Val
    private void request_FreshnessIndex_PlanClassList(final String deptName, final String category) {

        String freshnessIndex_planclass_listurl = ConstsCore.web_url + "/v1/display/freshnessindexdetail/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&dept=" + fIndexPlanDept.replaceAll(" ", "%20").replaceAll("&", "%26") + "&category=" + category.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + freshnessIndex_planclass_listurl);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, freshnessIndex_planclass_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("FreshNess Index Plan Class List : ", " " + response);
                        Log.i("FreshNess Index Plan Class List response length", "" + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no plan class data found", Toast.LENGTH_SHORT).show();
                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    freshnessIndexDetails = gson.fromJson(response.get(i).toString(), FreshnessIndexDetails.class);
                                    freshnessIndexDetailsArrayList.add(freshnessIndexDetails);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                request_FreshnessIndex_PlanClassList(deptName, category);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    freshnessIndexDetails = gson.fromJson(response.get(i).toString(), FreshnessIndexDetails.class);
                                    freshnessIndexDetailsArrayList.add(freshnessIndexDetails);
                                }

                                fIndexAdapter = new FreshnessIndexAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex);
                                listViewFIndex.setAdapter(fIndexAdapter);
                                fIndexAdapter.notifyDataSetChanged();
                                txtStoreCode.setText(freshnessIndexDetailsArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(freshnessIndexDetailsArrayList.get(0).getStoreDescription());
                                FreshnessIndexValue += " > " + category;
                                txtfIndexDeptName.setText(FreshnessIndexValue);
                                llfIndexhierarchy.setVisibility(View.VISIBLE);

                                ArrayList<Entry> entries = new ArrayList<>();
                                for (FreshnessIndexDetails fresh : freshnessIndexDetailsArrayList) {

                                        upcoming = (float) fresh.getUpcomingGroupCount();
                                        oldgroup = (float) fresh.getOldGroupCount();
                                        previousgroup = (float) fresh.getPreviousGroupCount();
                                        currentgroup = (float) fresh.getCurrentGroupCount();
                                        Log.e("Values", "" + fresh.getUpcomingGroupCount() + "\t" + fresh.getOldGroupCount() + "\t" + fresh.getPreviousGroupCount() + "\t" + fresh.getCurrentGroupCount());
                                        break;


                                }
                                ArrayList<Integer> colors = new ArrayList<>();
                                colors.add(Color.parseColor("#8857a6"));
                                colors.add(Color.parseColor("#b33d2f"));
                                colors.add(Color.parseColor("#386e34"));
                                colors.add(Color.parseColor("#308fab"));
                                ArrayList<String> labels = new ArrayList<>();
//                            if(currentgroup > 0.0f)
//                            {
                                labels.add("Current");
                                entries.add(new Entry(currentgroup, 0));
//                            }
//                            if(previousgroup > 0.0f)
//                            {
                                labels.add("Previous");
                                entries.add(new Entry(previousgroup, 1));
                                // }
//                            if(oldgroup > 0.0f)
//                            {
                                labels.add("Old");
                                entries.add(new Entry(oldgroup, 2));
                                //}
//                            if(upcoming > 0.0f)
//                            {
                                labels.add("Upcoming");
                                entries.add(new Entry(upcoming, 3));
                                //}

                                PieDataSet dataset = new PieDataSet(entries, "");
                                dataset.setColors(colors);
                                dataset.setSliceSpace(3);
                                dataset.setSelectionShift(5);
                                pieData = new PieData(labels, dataset);
                                //pieData.setValueFormatter(new MyValueFormatter());
                                pieData.setValueTextSize(10f);
                                pieData.setValueTextColor(Color.WHITE);
                                pieChart.setData(pieData);
                                pieChart.animateXY(4000, 4000);
                                pieChart.setDescription("");
                                pieChart.invalidate();
                                Legend l = pieChart.getLegend();
                                l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
                                l.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
                                l.setEnabled(true);
                                llfreshnessIndex.setVisibility(View.VISIBLE);
                                Reusable_Functions.hDialog();

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

    // For Brand on click of Plan Class Val
    private void request_FreshnessIndex_BrandList(String deptName, String category, final String planclass) {

        String freshnessIndex_brand_listurl = ConstsCore.web_url + "/v1/display/freshnessindexdetail/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&dept=" + fIndexPlanDept.replaceAll(" ", "%20").replaceAll("&", "%26") + "&category=" + fIndexCategory.replaceAll(" ", "%20").replaceAll("&", "%26") + "&class=" + planclass.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + freshnessIndex_brand_listurl);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, freshnessIndex_brand_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Freshness Index brand List : ", " " + response);
                        Log.i("Freshness Index brand List response length", "" + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no brand name data found", Toast.LENGTH_SHORT).show();
                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    freshnessIndexDetails = gson.fromJson(response.get(i).toString(), FreshnessIndexDetails.class);
                                    freshnessIndexDetailsArrayList.add(freshnessIndexDetails);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                request_FreshnessIndex_BrandList(fIndexPlanDept, fIndexCategory, planclass);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    freshnessIndexDetails = gson.fromJson(response.get(i).toString(), FreshnessIndexDetails.class);
                                    freshnessIndexDetailsArrayList.add(freshnessIndexDetails);
                                }

                                fIndexAdapter = new FreshnessIndexAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex);
                                listViewFIndex.setAdapter(fIndexAdapter);
                                //fIndexAdapter.notifyDataSetChanged();
                                txtStoreCode.setText(freshnessIndexDetailsArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(freshnessIndexDetailsArrayList.get(0).getStoreDescription());

                                FreshnessIndexValue += " > " + planclass;
                                txtfIndexDeptName.setText(FreshnessIndexValue);
                                llfIndexhierarchy.setVisibility(View.VISIBLE);


                                ArrayList<Entry> entries = new ArrayList<>();
                                for (FreshnessIndexDetails fresh : freshnessIndexDetailsArrayList) {

                                        upcoming = (float) fresh.getUpcomingGroupCount();
                                        oldgroup = (float) fresh.getOldGroupCount();
                                        previousgroup = (float) fresh.getPreviousGroupCount();
                                        currentgroup = (float) fresh.getCurrentGroupCount();


                                    Log.e("Values", "" + fresh.getUpcomingGroupCount() + "\t" + fresh.getOldGroupCount() + "\t" + fresh.getPreviousGroupCount() + "\t" + fresh.getCurrentGroupCount());
                                }
                                ArrayList<Integer> colors = new ArrayList<>();
                                colors.add(Color.parseColor("#8857a6"));
                                colors.add(Color.parseColor("#b33d2f"));
                                colors.add(Color.parseColor("#386e34"));
                                colors.add(Color.parseColor("#308fab"));
                                ArrayList<String> labels = new ArrayList<>();
//                            if(currentgroup > 0.0f)
//                            {
                                labels.add("Current");
                                entries.add(new Entry(currentgroup, 0));
//                            }
//                            if(previousgroup > 0.0f)
//                            {
                                labels.add("Previous");
                                entries.add(new Entry(previousgroup, 1));
                                // }
//                            if(oldgroup > 0.0f)
//                            {
                                labels.add("Old");
                                entries.add(new Entry(oldgroup, 2));
                                //}
//                            if(upcoming > 0.0f)
//                            {
                                labels.add("Upcoming");
                                entries.add(new Entry(upcoming, 3));
                                //}
                                PieDataSet dataset = new PieDataSet(entries, "");
                                dataset.setColors(colors);
                                dataset.setSliceSpace(3);
                                dataset.setSelectionShift(5);
                                pieData = new PieData(labels, dataset);
                                //pieData.setValueFormatter(new MyValueFormatter());
                                pieData.setValueTextSize(10f);
                                pieData.setValueTextColor(Color.WHITE);
                                pieChart.setData(pieData);
                                pieChart.animateXY(4000, 4000);
                                pieChart.setDescription("");
                                pieChart.invalidate();
                                Legend l = pieChart.getLegend();
                                l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
                                l.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
                                l.setEnabled(true);
                                llfreshnessIndex.setVisibility(View.VISIBLE);
                                Reusable_Functions.hDialog();

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

    // For BrandPlanCLass on click of Brand Val
    private void request_FreshnessIndex_BrandPlanList(String deptName, String category, String plan_class, final String brandnm) {

        String freshnessIndex_brandplan_listurl = ConstsCore.web_url + "/v1/display/freshnessindexdetail/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&dept=" + fIndexPlanDept.replaceAll(" ", "%20").replaceAll("&", "%26") + "&category=" + fIndexCategory.replaceAll(" ", "%20").replaceAll("&", "%26") + "&class=" + fIndexPlanClass.replaceAll(" ", "%20").replaceAll("&", "%26") + "&brand=" + brandnm.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + freshnessIndex_brandplan_listurl);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, freshnessIndex_brandplan_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Freshness Index Brand Plan Class List : ", " " + response);
                        Log.i("Freshness Index Plan Class List response length", "" + response.length());

                        try {

                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no brand plan class data found", Toast.LENGTH_SHORT).show();

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    freshnessIndexDetails = gson.fromJson(response.get(i).toString(), FreshnessIndexDetails.class);
                                    freshnessIndexDetailsArrayList.add(freshnessIndexDetails);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                request_FreshnessIndex_BrandPlanList(fIndexPlanDept, fIndexCategory, fIndexPlanClass, brandnm);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    freshnessIndexDetails = gson.fromJson(response.get(i).toString(), FreshnessIndexDetails.class);
                                    freshnessIndexDetailsArrayList.add(freshnessIndexDetails);
                                }

                                fIndexAdapter = new FreshnessIndexAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex);
                                listViewFIndex.setAdapter(fIndexAdapter);
                                fIndexAdapter.notifyDataSetChanged();
                                txtStoreCode.setText(freshnessIndexDetailsArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(freshnessIndexDetailsArrayList.get(0).getStoreDescription());
                                FreshnessIndexValue += " > " + brandnm;
                                txtfIndexDeptName.setText(FreshnessIndexValue);
                                llfIndexhierarchy.setVisibility(View.VISIBLE);


                                ArrayList<Entry> entries = new ArrayList<>();
                                for (FreshnessIndexDetails fresh : freshnessIndexDetailsArrayList) {

                                        upcoming = (float) fresh.getUpcomingGroupCount();
                                        oldgroup = (float) fresh.getOldGroupCount();
                                        previousgroup = (float) fresh.getPreviousGroupCount();
                                        currentgroup = (float) fresh.getCurrentGroupCount();


                                    Log.e("Values", "" + fresh.getUpcomingGroupCount() + "\t" + fresh.getOldGroupCount() + "\t" + fresh.getPreviousGroupCount() + "\t" + fresh.getCurrentGroupCount());

                                }
                                ArrayList<Integer> colors = new ArrayList<>();
                                colors.add(Color.parseColor("#8857a6"));
                                colors.add(Color.parseColor("#b33d2f"));
                                colors.add(Color.parseColor("#386e34"));
                                colors.add(Color.parseColor("#308fab"));
                                ArrayList<String> labels = new ArrayList<>();
//                            if(currentgroup > 0.0f)
//                            {
                                labels.add("Current");
                                entries.add(new Entry(currentgroup, 0));
//                            }
//                            if(previousgroup > 0.0f)
//                            {
                                labels.add("Previous");
                                entries.add(new Entry(previousgroup, 1));
                                // }
//                            if(oldgroup > 0.0f)
//                            {
                                labels.add("Old");
                                entries.add(new Entry(oldgroup, 2));
                                //}
//                            if(upcoming > 0.0f)
//                            {
                                labels.add("Upcoming");
                                entries.add(new Entry(upcoming, 3));
                                //}

                                PieDataSet dataset = new PieDataSet(entries, "");
                                dataset.setColors(colors);
                                dataset.setSliceSpace(3);

                                dataset.setSelectionShift(5);
                                pieData = new PieData(labels, dataset);
                                //pieData.setValueFormatter(new MyValueFormatter());
                                pieData.setValueTextSize(10f);
                                pieData.setValueTextColor(Color.WHITE);
                                pieChart.setData(pieData);
                                pieChart.animateXY(4000, 4000);
                                pieChart.setDescription("");

                                Legend l = pieChart.getLegend();
                                l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
                                l.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
                                l.setEnabled(true);
                                llfreshnessIndex.setVisibility(View.VISIBLE);
                                Reusable_Functions.hDialog();
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

    // Pie Chart val changed on Scroll of list view
    private void requestFIndexPieChart() {
        Log.e("Department onsroll api", "" + fIndexFirstVisibleItem);
        Log.e("Brand", txtFIndexClass.getText().toString());
        String url = "";

        if (txtFIndexClass.getText().toString().equals("Department")) {
            url = ConstsCore.web_url + "/v1/display/freshnessindexdetail/" + userId + "?corefashion=" + FIndex_SegmentClick + "&dept=" + fIndexFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        } else if (txtFIndexClass.getText().toString().equals("Category")) {
            url = ConstsCore.web_url + "/v1/display/freshnessindexdetail/" + userId + "?corefashion=" + FIndex_SegmentClick + "&category=" + fIndexFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        } else if (txtFIndexClass.getText().toString().equals("Plan Class")) {
            url = ConstsCore.web_url + "/v1/display/freshnessindexdetail/" + userId + "?corefashion=" + FIndex_SegmentClick + "&class=" + fIndexFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        } else if (txtFIndexClass.getText().toString().equals("Brand")) {

            url = ConstsCore.web_url + "/v1/display/freshnessindexdetail/" + userId + "?corefashion=" + FIndex_SegmentClick + "&brand=" + fIndexFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        } else if (txtFIndexClass.getText().toString().equals("Brand Plan Class")) {

            url = ConstsCore.web_url + "/v1/display/freshnessindexdetail/" + userId + "?corefashion=" + FIndex_SegmentClick + "&brandclass=" + fIndexFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        }
        Log.e("Url", "" + url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Sales Pva Chart on Scroll  : ", " " + response);
                        Log.e("Sales Pva Chart response", "" + response.length());
                        try {

                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    freshnessIndexDetails = gson.fromJson(response.get(i).toString(), FreshnessIndexDetails.class);
                                    freshnessIndexDetailsArrayList.add(freshnessIndexDetails);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestFIndexPieChart();


                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    freshnessIndexDetails = gson.fromJson(response.get(i).toString(), FreshnessIndexDetails.class);
                                    freshnessIndexDetailsArrayList.add(freshnessIndexDetails);
                                }
                                fIndexAdapter.notifyDataSetChanged();

                                ArrayList<Entry> entries = new ArrayList<Entry>();
                                for (FreshnessIndexDetails fresh : freshnessIndexDetailsArrayList) {
                                    if (fIndexFirstVisibleItem.equals(fresh.getPlanDept())) {
                                        upcoming = (float) fresh.getUpcomingGroupCount();
                                        oldgroup = (float) fresh.getOldGroupCount();
                                        previousgroup = (float) fresh.getPreviousGroupCount();
                                        currentgroup = (float) fresh.getCurrentGroupCount();
                                        Log.e("Values-------", "" + upcoming + "\t" + oldgroup + "\t" + previousgroup + "\t" + currentgroup);

                                    } else if (fIndexFirstVisibleItem.equals(fresh.getPlanCategory())) {
                                        upcoming = (float) fresh.getUpcomingGroupCount();
                                        oldgroup = (float) fresh.getOldGroupCount();
                                        previousgroup = (float) fresh.getPreviousGroupCount();
                                        currentgroup = (float) fresh.getCurrentGroupCount();
                                        Log.e("Values-------", "" + upcoming + "\t" + oldgroup + "\t" + previousgroup + "\t" + currentgroup);

                                    } else if (fIndexFirstVisibleItem.equals(fresh.getPlanClass())) {
                                        upcoming = (float) fresh.getUpcomingGroupCount();
                                        oldgroup = (float) fresh.getOldGroupCount();
                                        previousgroup = (float) fresh.getPreviousGroupCount();
                                        currentgroup = (float) fresh.getCurrentGroupCount();
                                        Log.e("Values-------", "" + upcoming + "\t" + oldgroup + "\t" + previousgroup + "\t" + currentgroup);

                                    } else if (fIndexFirstVisibleItem.equals(fresh.getBrandName())) {
                                        upcoming = (float) fresh.getUpcomingGroupCount();
                                        oldgroup = (float) fresh.getOldGroupCount();
                                        previousgroup = (float) fresh.getPreviousGroupCount();
                                        currentgroup = (float) fresh.getCurrentGroupCount();
                                        Log.e("Values-------", "" + upcoming + "\t" + oldgroup + "\t" + previousgroup + "\t" + currentgroup);

                                    } else if (fIndexFirstVisibleItem.equals(fresh.getBrandplanClass())) {
                                        upcoming = (float) fresh.getUpcomingGroupCount();
                                        oldgroup = (float) fresh.getOldGroupCount();
                                        previousgroup = (float) fresh.getPreviousGroupCount();
                                        currentgroup = (float) fresh.getCurrentGroupCount();
                                        Log.e("Values-------", "" + upcoming + "\t" + oldgroup + "\t" + previousgroup + "\t" + currentgroup);

                                    }

                                }
                                ArrayList<Integer> colors = new ArrayList<>();
                                colors.add(Color.parseColor("#8857a6"));
                                colors.add(Color.parseColor("#b33d2f"));
                                colors.add(Color.parseColor("#386e34"));
                                colors.add(Color.parseColor("#308fab"));
                                ArrayList<String> labels = new ArrayList<>();

                                labels.add("Current");
                                entries.add(new Entry(currentgroup, 0));

                                labels.add("Previous");
                                entries.add(new Entry(previousgroup, 1));

                                labels.add("Old");
                                entries.add(new Entry(oldgroup, 2));

                                labels.add("Upcoming");
                                entries.add(new Entry(upcoming, 3));
                                PieDataSet dataset = new PieDataSet(entries, "");
                                dataset.setColors(colors);
//                                dataset.setSliceSpace(3);
//                                dataset.setSelectionShift(5);
                                pieData = new PieData(labels, dataset);
                                //pieData.setValueFormatter(new MyValueFormatter());
                                pieData.setValueTextSize(10f);
                                pieData.setValueTextColor(Color.WHITE);
                                pieChart.setData(pieData);
                                pieChart.animateXY(4000, 4000);
                                pieChart.setDescription("");
                                pieChart.invalidate();
                                Legend l = pieChart.getLegend();
                                l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
                                l.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
                                l.setEnabled(true);
                                llfreshnessIndex.setVisibility(View.VISIBLE);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FreshnessIndexActivity.this, FreshnessIndexActivity.class);
        startActivity(intent);
        finish();
    }

//    public class MyValueFormatter implements ValueFormatter {
//
//        private DecimalFormat mFormat;
//
//        public MyValueFormatter() {
//            mFormat = new DecimalFormat("###,###,##0"); // use one decimal if needed
//        }
//
//
//        @Override
//        public String getFormattedValue(float value,Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//            return mFormat.format(value) + ""; // e.g. append a dollar-sign
//        }
//    }

}


