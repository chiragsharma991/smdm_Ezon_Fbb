package apsupportapp.aperotechnologies.com.designapp.FreshnessIndex;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
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
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
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
import apsupportapp.aperotechnologies.com.designapp.OptionEfficiency.OptionEfficiencyActivity;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.model.FreshnessIndexDetails;
import apsupportapp.aperotechnologies.com.designapp.model.OptionEfficiencyDetails;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by pamrutkar on 22/11/16.
 */

public class FreshnessIndexActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    RadioButton btnCore,btnFashion;
    public String FIndex_SegmentClick;
    ArrayList<FreshnessIndexDetails> freshnessIndexDetailsArrayList, fIndexArrayList;
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
    PieDataSet dataSet;
    boolean flag = false;
    private String TAG = "FreshnessIndexActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freshness_index);
        getSupportActionBar().hide();

        fromWhere = "Department";
        FIndex_SegmentClick = "Fashion";
        fIndexFirstVisibleItem = "";
        freshnessIndex_ClickedVal = "";
        FreshnessIndexValue = "";
        context = this;
        level = 1;
        focusposition = 0;
        selFirstPositionValue = 0;
        fIndexPlanDept = " ";
        fIndexCategory = "";
        fIndexPlanClass = " ";
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
              /*  Intent intent = new Intent(FreshnessIndexActivity.this, DashBoardActivity.class);
                intent.putExtra("BACKTO","inventory");
                startActivity(intent);*/
                finish();
            }
        });
        freshnessIndex_imgfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FreshnessIndexActivity.this, InventoryFilterActivity.class);
                intent.putExtra("checkfrom", "freshnessIndex");
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
                        flag = false;
                        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
                        llfIndexhierarchy.setVisibility(View.GONE);
                        llfreshnessIndex.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            fIndexPlanDept = " ";
                            fIndexCategory = " ";
                            fIndexPlanClass = " ";
                            fIndexBrand = " ";
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
                        flag = false;
                        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
                        llfIndexhierarchy.setVisibility(View.GONE);
                        llfreshnessIndex.setVisibility(View.GONE);

                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            fIndexPlanDept = " ";
                            fIndexCategory = " ";
                            fIndexPlanClass = " ";
                            fIndexBrand = " ";
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
                        flag = false;
                        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
                        llfIndexhierarchy.setVisibility(View.GONE);
                        llfreshnessIndex.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            fIndexPlanDept = " ";
                            fIndexCategory = " ";
                            fIndexPlanClass = " ";
                            fIndexBrand = " ";
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
                        flag = false;
                        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
                        llfIndexhierarchy.setVisibility(View.GONE);
                        llfreshnessIndex.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            fIndexPlanDept = " ";
                            fIndexCategory = " ";
                            fIndexPlanClass = " ";
                            fIndexBrand = " ";
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
                        flag = false;
                        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
                        llfIndexhierarchy.setVisibility(View.GONE);
                        llfreshnessIndex.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            fIndexPlanDept = " ";
                            fIndexCategory = " ";
                            fIndexPlanClass = " ";
                            fIndexBrand = " ";
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
                        flag = false;
                        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
                        llfIndexhierarchy.setVisibility(View.GONE);
                        llfreshnessIndex.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            fIndexPlanDept = " ";
                            fIndexCategory = " ";
                            fIndexPlanClass = " ";
                            fIndexBrand = " ";
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
                        flag = false;
                        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
                        llfIndexhierarchy.setVisibility(View.GONE);
                        llfreshnessIndex.setVisibility(View.GONE);

                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            fIndexPlanDept = " ";
                            fIndexCategory = " ";
                            fIndexPlanClass = " ";
                            fIndexBrand = " ";
                            requestFreshnessIndexDetails();

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("---3--", " ");

                        break;

                    case "Brand":
                        txtFIndexClass.setText("Brand Plan Class");
                        flag = false;
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
                            fIndexPlanDept = " ";
                            fIndexCategory = " ";
                            fIndexPlanClass = " ";
                            fIndexBrand = " ";
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
                        llfIndexhierarchy.setVisibility(View.GONE);
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
                        Log.e("in category", "---" + fIndexPlanDept);
                        if (flag == true) {
                            txtFIndexClass.setText("Plan Class");
                            llfIndexhierarchy.setVisibility(View.GONE);
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
                                fIndexCategory = freshnessIndex_ClickedVal;
                                request_FreshnessIndex_PlanClassList(fIndexPlanDept, fIndexCategory);


                                Log.e("fIndexCategory--", "" + fIndexCategory);
                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Please select dept name", Toast.LENGTH_SHORT);
                        }


                        break;
                    case "Plan Class":
                        Log.e("In plan class", "-------" + fIndexPlanDept);
                        if (flag == true) {
                            Log.e("fIndexPlanDept in ELSE ", fIndexPlanDept);
                            txtFIndexClass.setText("Brand");
                            llfIndexhierarchy.setVisibility(View.GONE);
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
                                fIndexPlanClass = freshnessIndex_ClickedVal;
                                request_FreshnessIndex_BrandList(fIndexPlanDept, fIndexCategory, fIndexPlanClass);


                                Log.e("fIndexPlanClass---", "" + fIndexPlanClass);
                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Please select dept name", Toast.LENGTH_SHORT);
                        }

                        break;

                    case "Brand":
                        if (flag == true) {
                            Log.e("in brand", "----" + fIndexPlanDept);
                            txtFIndexClass.setText("Brand Plan Class");
                            llfIndexhierarchy.setVisibility(View.GONE);
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
                                fIndexBrand = freshnessIndex_ClickedVal;

                                request_FreshnessIndex_BrandPlanList(fIndexPlanDept, fIndexCategory, fIndexPlanClass, fIndexBrand);

                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Please select dept name", Toast.LENGTH_SHORT);
                        }

                        break;

                }

            }

        });

        //list view on Scroll event

        listViewFIndex.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                if (scrollState == SCROLL_STATE_IDLE) {

                if (freshnessIndexDetailsArrayList.size() != 0) {
                    //listView_SalesAnalysis.smoothScrollToPosition(firstVisibleItem);
                    if (view.getFirstVisiblePosition() <= freshnessIndexDetailsArrayList.size() - 1) {
                        focusposition = view.getFirstVisiblePosition();
                        listViewFIndex.setSelection(view.getFirstVisiblePosition());
                        //Log.e("focusposition", " " + firstVisibleItem + " " + productNameBeanArrayList.get(firstVisibleItem).getProductName());
                        if (txtFIndexClass.getText().toString().equals("Department")) {
                            level = 1;
                            fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(listViewFIndex.getFirstVisiblePosition()).getPlanDept().toString();
                        } else if (txtFIndexClass.getText().toString().equals("Category")) {
                            level = 2;
                            fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(listViewFIndex.getFirstVisiblePosition()).getPlanCategory().toString();
                        } else if (txtFIndexClass.getText().toString().equals("Plan Class")) {
                            level = 3;
                            fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(listViewFIndex.getFirstVisiblePosition()).getPlanClass().toString();
                        } else if (txtFIndexClass.getText().toString().equals("Brand")) {
                            level = 4;
                            fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(listViewFIndex.getFirstVisiblePosition()).getBrandName().toString();
                        } else if (txtFIndexClass.getText().toString().equals("Brand Plan Class")) {
                            level = 5;
                            fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(listViewFIndex.getFirstVisiblePosition()).getBrandplanClass().toString();
                        }
                        if (focusposition != selFirstPositionValue) {
                            if (Reusable_Functions.chkStatus(context)) {
                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                requestFIndexPieChart();
                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }
                        }
                        selFirstPositionValue = focusposition;


                    } else {
                        focusposition = freshnessIndexDetailsArrayList.size() - 1;
                        listViewFIndex.setSelection(freshnessIndexDetailsArrayList.size() - 1);
                        selFirstPositionValue = focusposition;

                    }
                }

//                }
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
        btnCore = (RadioButton) findViewById(R.id.btnCore);
        btnFashion = (RadioButton)findViewById(R.id.btnFashion);
        btnFashion.toggle();
        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
        fIndexArrayList = new ArrayList<FreshnessIndexDetails>();
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
//                                freshnessIndexDetails = new FreshnessIndexDetails();
//
//                                if (txtFIndexClass.getText().toString().equals("Department")) {
//                                    freshnessIndexDetails.setPlanDept("All");
//                                } else if (txtFIndexClass.getText().toString().equals("Category")) {
//                                    freshnessIndexDetails.setPlanCategory("All");
//                                } else if (txtFIndexClass.getText().toString().equals("Plan Class")) {
//                                    freshnessIndexDetails.setPlanClass("All");
//                                } else if (txtFIndexClass.getText().toString().equals("Brand")) {
//                                    freshnessIndexDetails.setBrandName("All");
//                                } else if (txtFIndexClass.getText().toString().equals("Brand Plan Class")) {
//                                    freshnessIndexDetails.setBrandplanClass("All");
//                                }
//
//                                freshnessIndexDetailsArrayList.add(0, freshnessIndexDetails);
//

                                fIndexAdapter = new FreshnessIndexAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex);
                                listViewFIndex.setAdapter(fIndexAdapter);
                                fIndexAdapter.notifyDataSetChanged();
                                txtStoreCode.setText(freshnessIndexDetailsArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(freshnessIndexDetailsArrayList.get(0).getStoreDescription());


                                if (txtFIndexClass.getText().toString().equals("Department")) {
                                    level = 1;
                                    fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(focusposition).getPlanDept().toString();
                                } else if (txtFIndexClass.getText().toString().equals("Category")) {
                                    level = 2;
                                    fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(focusposition).getPlanCategory().toString();
                                } else if (txtFIndexClass.getText().toString().equals("Plan Class")) {
                                    level = 3;
                                    fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(focusposition).getPlanClass().toString();
                                } else if (txtFIndexClass.getText().toString().equals("Brand")) {
                                    level = 4;
                                    fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(focusposition).getBrandName().toString();
                                } else if (txtFIndexClass.getText().toString().equals("Brand Plan Class")) {
                                    level = 5;
                                    fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(focusposition).getBrandplanClass().toString();
                                }
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                requestFIndexPieChart();

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
                                flag = true;
                                fIndexAdapter.notifyDataSetChanged();
                                txtStoreCode.setText(freshnessIndexDetailsArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(freshnessIndexDetailsArrayList.get(0).getStoreDescription());
                                FreshnessIndexValue = " ";
                                FreshnessIndexValue = deptName;
                                txtfIndexDeptName.setText(FreshnessIndexValue);
                                llfIndexhierarchy.setVisibility(View.VISIBLE);

                                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(listViewFIndex.getFirstVisiblePosition()).getPlanCategory().toString();
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                level = 2;
                                requestFIndexPieChart();


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
                                flag = true;
                                fIndexAdapter.notifyDataSetChanged();
                                txtStoreCode.setText(freshnessIndexDetailsArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(freshnessIndexDetailsArrayList.get(0).getStoreDescription());
                                FreshnessIndexValue += " > " + category;
                                txtfIndexDeptName.setText(FreshnessIndexValue);
                                llfIndexhierarchy.setVisibility(View.VISIBLE);
                                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(listViewFIndex.getFirstVisiblePosition()).getPlanClass().toString();
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                level = 3;
                                requestFIndexPieChart();

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
                                flag = true;
                                fIndexAdapter.notifyDataSetChanged();
                                txtStoreCode.setText(freshnessIndexDetailsArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(freshnessIndexDetailsArrayList.get(0).getStoreDescription());

                                FreshnessIndexValue += " > " + planclass;
                                txtfIndexDeptName.setText(FreshnessIndexValue);
                                llfIndexhierarchy.setVisibility(View.VISIBLE);
                                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(listViewFIndex.getFirstVisiblePosition()).getBrandName().toString();
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                level = 4;
                                requestFIndexPieChart();

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

//
//                                ArrayList<Entry> entries = new ArrayList<>();
//                                for (FreshnessIndexDetails fresh : freshnessIndexDetailsArrayList) {
//
//                                        upcoming = (float) fresh.getUpcomingGroupCount();
//                                        oldgroup = (float) fresh.getOldGroupCount();
//                                        previousgroup = (float) fresh.getPreviousGroupCount();
//                                        currentgroup = (float) fresh.getCurrentGroupCount();
//
//
//                                    Log.e("Values", "" + fresh.getUpcomingGroupCount() + "\t" + fresh.getOldGroupCount() + "\t" + fresh.getPreviousGroupCount() + "\t" + fresh.getCurrentGroupCount());
//
//                                }
//                                ArrayList<Integer> colors = new ArrayList<>();
//                                colors.add(Color.parseColor("#8857a6"));
//                                colors.add(Color.parseColor("#b33d2f"));
//                                colors.add(Color.parseColor("#386e34"));
//                                colors.add(Color.parseColor("#308fab"));
//                                ArrayList<String> labels = new ArrayList<>();
////                            if(currentgroup > 0.0f)
////                            {
//                                labels.add("Current");
//                                entries.add(new Entry(currentgroup, 0));
////                            }
////                            if(previousgroup > 0.0f)
////                            {
//                                labels.add("Previous");
//                                entries.add(new Entry(previousgroup, 1));
//                                // }
////                            if(oldgroup > 0.0f)
////                            {
//                                labels.add("Old");
//                                entries.add(new Entry(oldgroup, 2));
//                                //}
////                            if(upcoming > 0.0f)
////                            {
//                                labels.add("Upcoming");
//                                entries.add(new Entry(upcoming, 3));
//                                //}
//
//                                PieDataSet dataset = new PieDataSet(entries, "");
//                                dataset.setColors(colors);
//                                dataset.setSliceSpace(3);
//
//                                dataset.setSelectionShift(5);
//                                pieData = new PieData(labels, dataset);
//                                //pieData.setValueFormatter(new MyValueFormatter());
//                                pieData.setValueTextSize(10f);
//                                pieData.setValueTextColor(Color.WHITE);
//                                pieChart.setData(pieData);
//                                pieChart.animateXY(4000, 4000);
//                                pieChart.setDescription("");
//
//                                Legend l = pieChart.getLegend();
//                                l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
//                                l.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
//                                l.setEnabled(true);
//                                llfreshnessIndex.setVisibility(View.VISIBLE);
//                                Reusable_Functions.hDialog();
                                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(listViewFIndex.getFirstVisiblePosition()).getBrandplanClass().toString();
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                level = 5;
                                requestFIndexPieChart();

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
        Log.e("Header Class", txtFIndexClass.getText().toString());
        String url = "";

        if (txtFIndexClass.getText().toString().equals("Department")) {
            url = ConstsCore.web_url + "/v1/display/freshnessindexdetail/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&dept=" + fIndexFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        } else if (txtFIndexClass.getText().toString().equals("Category")) {
            url = ConstsCore.web_url + "/v1/display/freshnessindexdetail/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&category=" + fIndexFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        } else if (txtFIndexClass.getText().toString().equals("Plan Class")) {
            url = ConstsCore.web_url + "/v1/display/freshnessindexdetail/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&class=" + fIndexFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        } else if (txtFIndexClass.getText().toString().equals("Brand")) {
            url = ConstsCore.web_url + "/v1/display/freshnessindexdetail/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&brand=" + fIndexFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        } else if (txtFIndexClass.getText().toString().equals("Brand Plan Class")) {

            url = ConstsCore.web_url + "/v1/display/freshnessindexdetail/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&brandclass=" + fIndexFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        }
        Log.e("Url", "" + url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Sales Pva Chart on Scroll  : ", " " + response);

                        Log.e("Sales Pva Chart response", "" + response.length());
                        try {
                            int i;
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();

                            } else if (response.length() == limit) {
                                for (i = 0; i < response.length(); i++) {

                                    freshnessIndexDetails = gson.fromJson(response.get(i).toString(), FreshnessIndexDetails.class);
                                    fIndexArrayList.add(freshnessIndexDetails);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestFIndexPieChart();


                            } else if (response.length() < limit) {
                                for (i = 0; i < response.length(); i++) {

                                    freshnessIndexDetails = gson.fromJson(response.get(i).toString(), FreshnessIndexDetails.class);
                                    fIndexArrayList.add(freshnessIndexDetails);
                                }
                                //fIndexAdapter.notifyDataSetChanged();

                                ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
                                for (FreshnessIndexDetails fresh : fIndexArrayList) {
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
                                if (currentgroup > 0.0f) {

                                    entries.add(new PieEntry(currentgroup, "Current"));
                                }
                                if (previousgroup > 0.0f) {

                                    entries.add(new PieEntry(previousgroup, "Previous"));
                                }
                                if (oldgroup > 0.0f) {

                                    entries.add(new PieEntry(oldgroup, "Old"));
                                }
                                if (upcoming > 0.0f) {

                                    entries.add(new PieEntry(upcoming, "Upcoming"));
                                }
                                dataSet = new PieDataSet(entries, "");
                                dataSet.setColors(colors);
                                dataSet.setValueLineWidth(0.5f);
                                dataSet.setValueTextColor(Color.BLACK);
                                pieData = new PieData(dataSet);
                                pieData.setValueFormatter(new MyValueFormatter());
                                dataSet.setValueLinePart1Length(1.5f);
                                dataSet.setValueLinePart2Length(1.8f);
                                pieChart.setDrawMarkers(false);
                                pieData.setValueTextSize(8.5f);

                                dataSet.setXValuePosition(null);
                                dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                                pieChart.setEntryLabelColor(Color.BLACK);
                                pieChart.setExtraOffsets(5, 10, 5, 5);
                                pieChart.setHoleRadius(0);
                                pieChart.setHoleColor(Color.WHITE);
                                pieChart.setTransparentCircleRadius(0);
                                pieChart.setData(pieData);
                                pieChart.animateXY(4000, 4000);
                                pieChart.setDescription(null);

                                pieChart.setTouchEnabled(false);
                                Legend l = pieChart.getLegend();


                                l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);


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

    public class MyValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,###,##0.0"); // use two decimal if needed
        }


        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//           return mFormat.format(value) + ""; // e.g. append a dollar-sign

            if (value < 0.0) return "";
            else return mFormat.format(value) + " %";
        }
    }

    @Override
    public void onBackPressed() {

 /*       Intent intent = new Intent(FreshnessIndexActivity.this, DashBoardActivity.class);
        intent.putExtra("BACKTO","inventory");
        startActivity(intent);*/
        finish();
    }
}


