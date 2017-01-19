/*
package apsupportapp.aperotechnologies.com.designapp.FreshnessIndex;

*/
/**
 * Created by csuthar on 19/01/17.
 *//*


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
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
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.RunningPromo.RecyclerViewPositionHelper;
import info.hoang8f.android.segmented.SegmentedGroup;

package apsupportapp.aperotechnologies.com.designapp.FreshnessIndex;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
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
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.RunningPromo.RecyclerViewPositionHelper;
import info.hoang8f.android.segmented.SegmentedGroup;

*/
/**
 * Created by pamrutkar on 22/11/16.
 *//*


public class FreshnessIndexActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    RadioButton btnCore, btnFashion;
    public String FIndex_SegmentClick;
    ArrayList<FreshnessIndexDetails> freshnessIndexDetailsArrayList, fIndexArrayList;
    TextView txtStoreCode, txtStoreDesc, txtFIndexClass, txtfIndexDeptName, txtNoChart;
    String userId, bearertoken;
    SharedPreferences sharedPreferences;
    int offsetvalue = 0, limit = 100;
    int count = 0;
    RequestQueue queue;
    String FreshnessIndexValue;
    //test git 6/1/17
    Context context;
    String fromWhere, fIndexFirstVisibleItem, freshnessIndex_ClickedVal, fIndexPlanDept, fIndexCategory, fIndexPlanClass, fIndexBrand;
    PieChart pieChart;
    RecyclerView listViewFIndex;
    int focusposition, selFirstPositionValue;
    LinearLayout llfreshnessIndex, llfIndexhierarchy;
    SegmentedGroup segmented3;
    int level;
    FreshnessIndexDetails freshnessIndexDetails, freshnessIndexDetail;
    RelativeLayout freshnessIndex_imageBtnBack, freshnessIndex_imgfilter;
    RelativeLayout btnFIndexPrev, btnFIndexNext;
    Gson gson;
    FreshnessIndexAdapter fIndexAdapter;
    FreshnessIndexSnapAdapter freshnessIndexSnapAdapter;
    PieData pieData;
    float upcoming = 0.0f, oldgroup = 0.0f, previousgroup = 0.0f, currentgroup = 0.0f;
    PieDataSet dataSet;
    boolean flag = false;
    private String TAG = "FreshnessIndexActivity";
    private boolean current = false, previous = false, old = false, upcome = false;
    private int totalItemCount = 0;
    int firstVisibleItem = 0;
    JsonArrayRequest postRequest;

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
              */
/*  Intent intent = new Intent(FreshnessIndexActivity.this, DashBoardActivity.class);
                intent.putExtra("BACKTO","inventory");
                startActivity(intent);*//*

                finish();
            }
        });
        freshnessIndex_imgfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FreshnessIndexActivity.this, InventoryFilterActivity.class);
                intent.putExtra("checkfrom", "freshnessIndex");
                startActivity(intent);
                // finish();
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
            llfreshnessIndex.setVisibility(View.GONE);

        }

        // previous
        btnFIndexPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (txtFIndexClass.getText().toString()) {

                    case "Brand Plan Class":
                        btnFIndexNext.setVisibility(View.VISIBLE);
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
                        btnFIndexPrev.setVisibility(View.INVISIBLE);
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
                        btnFIndexPrev.setVisibility(View.VISIBLE);
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
                        btnFIndexNext.setVisibility(View.INVISIBLE);
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
*/
/*        listViewFIndex.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position < freshnessIndexDetailsArrayList.size()) {
                    switch (txtFIndexClass.getText().toString()) {


                        case "Department":
                            btnFIndexPrev.setVisibility(View.VISIBLE);
                            txtFIndexClass.setText("Category");
                            Log.e("position", "" + listViewFIndex.getCheckedItemPosition() + "position" + position + "posotion1" + focusposition);
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
                                btnFIndexNext.setVisibility(View.INVISIBLE);
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

            }

        });*//*


        //list view on Scroll event

        listViewFIndex.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (freshnessIndexDetailsArrayList.size() != 0) {
                    if (firstVisibleItem < freshnessIndexSnapAdapter.getItemCount() - 1) {



                        RecyclerViewPositionHelper mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
                        int visibleItemCount = recyclerView.getChildCount();
                        totalItemCount = mRecyclerViewHelper.getItemCount();
                        firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();
                        Log.e(TAG, "onScrollStateChanged: " + firstVisibleItem);
                        if(firstVisibleItem >= freshnessIndexDetailsArrayList.size())
                        {
                            firstVisibleItem = freshnessIndexDetailsArrayList.size() - 1;
                            LinearLayoutManager llm = (LinearLayoutManager)listViewFIndex .getLayoutManager();
                            llm.scrollToPosition(firstVisibleItem);
                            return;
                        }

                        if (txtFIndexClass.getText().toString().equals("Department")) {
                            level = 1;
                            fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getPlanDept().toString();
                        } else if (txtFIndexClass.getText().toString().equals("Category")) {
                            level = 2;
                            fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getPlanCategory().toString();
                        } else if (txtFIndexClass.getText().toString().equals("Plan Class")) {
                            level = 3;
                            fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getPlanClass().toString();
                        } else if (txtFIndexClass.getText().toString().equals("Brand")) {
                            level = 4;
                            fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getBrandName().toString();
                        } else if (txtFIndexClass.getText().toString().equals("Brand Plan Class")) {
                            level = 5;
                            fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getBrandplanClass().toString();
                        }
                        //  if (firstVisibleItem != selFirstPositionValue) {
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.hDialog();
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            fIndexArrayList= new ArrayList<FreshnessIndexDetails>();
                            if(postRequest!=null)
                            {
                                postRequest.cancel();
                            }
                            requestFIndexPieChart();


                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        // }
                        //  selFirstPositionValue = firstVisibleItem;
                    } else {
                        Log.e(TAG, "onScrollStateChanged: ");
                        firstVisibleItem = freshnessIndexDetailsArrayList.size() - 1;
                        //selFirstPositionValue = firstVisibleItem;
                    }


//                    if (firstVisibleItem >= freshnessIndexSnapAdapter.getItemCount() - 2) {
//                        Log.e(TAG, "STATE_IDLE>>>>>>>>" + "firstvisible=" + firstVisibleItem + "and total=" + totalItemCount);
//
//                        LinearLayoutManager llm = (LinearLayoutManager) listViewFIndex.getLayoutManager();
//                        Log.e("Array Size", "" + freshnessIndexDetailsArrayList.size() + " totalItemCount :" + totalItemCount);
//                        // llm.scrollToPositionWithOffset(totalItemCount-14, totalItemCount);
//                        //  llm.scrollToPosition(11);
//                    }
                }
            }
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {


            }

        });
    }



*/
/*            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                if (scrollState == SCROLL_STATE_IDLE) {

                if (freshnessIndexDetailsArrayList.size() != 0) {
                    RecyclerViewPositionHelper mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(listViewFIndex);

                    totalItemCount = mRecyclerViewHelper.getItemCount();
                    firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();
                    //listView_SalesAnalysis.smoothScrollToPosition(firstVisibleItem);
                    if (view.getFirstVisiblePosition() <= freshnessIndexDetailsArrayList.size() - 1) {
                        focusposition = view.getFirstVisiblePosition();
                       // listViewFIndex.setSelection(view.getFirstVisiblePosition());
                        //Log.e("focusposition", " " + firstVisibleItem + " " + productNameBeanArrayList.get(firstVisibleItem).getProductName());
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








//                }


            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


    }*//*


    private void initializeUI() {
        txtStoreCode = (TextView) findViewById(R.id.txtStoreCode);
        txtStoreDesc = (TextView) findViewById(R.id.txtStoreName);
        txtNoChart = (TextView) findViewById(R.id.noChart);
        txtFIndexClass = (TextView) findViewById(R.id.txtFIndexClass);
        txtfIndexDeptName = (TextView) findViewById(R.id.txtfIndexDeptName);
        freshnessIndex_imageBtnBack = (RelativeLayout) findViewById(R.id.freshnessIndex_imageBtnBack);
        freshnessIndex_imgfilter = (RelativeLayout) findViewById(R.id.freshnessIndex_imgfilter);
        pieChart = (PieChart) findViewById(R.id.fIndex_pieChart);
        listViewFIndex = (RecyclerView) findViewById(R.id.listView_SalesAnalysis);
        // listViewFIndex.addFooterView(getLayoutInflater().inflate(R.layout.list_footer, null));
        llfreshnessIndex = (LinearLayout) findViewById(R.id.llfreshnessIndex);
        llfIndexhierarchy = (LinearLayout) findViewById(R.id.llfIndexhierarchy);
        btnFIndexPrev = (RelativeLayout) findViewById(R.id.btnFIndexPrev);
        btnFIndexPrev.setVisibility(View.INVISIBLE);
        btnFIndexNext = (RelativeLayout) findViewById(R.id.btnFIndexNext);
        segmented3 = (SegmentedGroup) findViewById(R.id.freshnessIndex_segmentedGrp);
        segmented3.setOnCheckedChangeListener(FreshnessIndexActivity.this);
        btnCore = (RadioButton) findViewById(R.id.btnCore);
        btnFashion = (RadioButton) findViewById(R.id.btnFashion);
        btnFashion.toggle();
        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
        fIndexArrayList = new ArrayList<FreshnessIndexDetails>();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {

            case R.id.btnCore:
                //Toast.makeText(SalesPvAActivity.this, "WTD", Toast.LENGTH_SHORT).show();
                if (FIndex_SegmentClick.equals("All"))
                    break;
                FIndex_SegmentClick = "All";
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
                Log.e("-----All-----", " ");
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
        Log.e(TAG, "url" + fIdetails);


        postRequest = new JsonArrayRequest(Request.Method.GET, fIdetails,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, "Freshness Index Details Class: " + response);
                        Log.i("response length", "" + response.length());
                        int i;
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                //Reusable_Functions.hDialog();
                                //Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();

                                return;
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
                                Log.e(TAG, "freshnessIndexDetailsArrayList: " + freshnessIndexDetailsArrayList.size());

                                //this>>

                                requestAll();


                            }

                        } catch (Exception e) {
                            // Reusable_Functions.hDialog();
                            //Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Reusable_Functions.hDialog();
                        //Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                        //llfreshnessIndex.setVisibility(View.GONE);

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

        postRequest = new JsonArrayRequest(Request.Method.GET, freshnessindex_category_listurl,
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

                                listViewFIndex.setLayoutManager(new LinearLayoutManager(context));

                                listViewFIndex.setLayoutManager(new LinearLayoutManager(
                                        listViewFIndex.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                listViewFIndex.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(listViewFIndex);

                                freshnessIndexSnapAdapter = new FreshnessIndexSnapAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex);
                                // fIndexAdapter = new FreshnessIndexAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex);
                                listViewFIndex.setAdapter(freshnessIndexSnapAdapter);


                                //0 fIndexAdapter = new FreshnessIndexAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex);
                                //listViewFIndex.setAdapter(fIndexAdapter);
                                flag = true;
                                freshnessIndexSnapAdapter.notifyDataSetChanged();
                                txtStoreCode.setText(freshnessIndexDetailsArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(freshnessIndexDetailsArrayList.get(0).getStoreDescription());
                                FreshnessIndexValue = " ";
                                FreshnessIndexValue = deptName;
                                txtfIndexDeptName.setText(FreshnessIndexValue);
                                llfIndexhierarchy.setVisibility(View.VISIBLE);
                                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getPlanCategory().toString();
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

        postRequest = new JsonArrayRequest(Request.Method.GET, freshnessIndex_planclass_listurl,
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
                                listViewFIndex.setLayoutManager(new LinearLayoutManager(context));

                                listViewFIndex.setLayoutManager(new LinearLayoutManager(
                                        listViewFIndex.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                listViewFIndex.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(listViewFIndex);

                                freshnessIndexSnapAdapter = new FreshnessIndexSnapAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex);
                                // fIndexAdapter = new FreshnessIndexAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex);
                                listViewFIndex.setAdapter(freshnessIndexSnapAdapter);

                                //  fIndexAdapter = new FreshnessIndexAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex);
                                // listViewFIndex.setAdapter(fIndexAdapter);
                                flag = true;
                                freshnessIndexSnapAdapter.notifyDataSetChanged();
                                txtStoreCode.setText(freshnessIndexDetailsArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(freshnessIndexDetailsArrayList.get(0).getStoreDescription());
                                FreshnessIndexValue += " > " + category;
                                txtfIndexDeptName.setText(FreshnessIndexValue);
                                llfIndexhierarchy.setVisibility(View.VISIBLE);
                                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getPlanClass().toString();
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

    private void request_FreshnessIndex_BrandList(String deptName, String category, final String planclass) {

        String freshnessIndex_brand_listurl = ConstsCore.web_url + "/v1/display/freshnessindexdetail/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&dept=" + fIndexPlanDept.replaceAll(" ", "%20").replaceAll("&", "%26") + "&category=" + fIndexCategory.replaceAll(" ", "%20").replaceAll("&", "%26") + "&class=" + planclass.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + freshnessIndex_brand_listurl);

        postRequest = new JsonArrayRequest(Request.Method.GET, freshnessIndex_brand_listurl,
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

                                listViewFIndex.setLayoutManager(new LinearLayoutManager(context));

                                listViewFIndex.setLayoutManager(new LinearLayoutManager(
                                        listViewFIndex.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                listViewFIndex.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(listViewFIndex);

                                freshnessIndexSnapAdapter = new FreshnessIndexSnapAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex);
                                // fIndexAdapter = new FreshnessIndexAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex);
                                listViewFIndex.setAdapter(freshnessIndexSnapAdapter);

                                // fIndexAdapter = new FreshnessIndexAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex);
                                //listViewFIndex.setAdapter(fIndexAdapter);
                                flag = true;
                                freshnessIndexSnapAdapter.notifyDataSetChanged();
                                txtStoreCode.setText(freshnessIndexDetailsArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(freshnessIndexDetailsArrayList.get(0).getStoreDescription());

                                FreshnessIndexValue += " > " + planclass;
                                txtfIndexDeptName.setText(FreshnessIndexValue);
                                llfIndexhierarchy.setVisibility(View.VISIBLE);
                                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getBrandName().toString();
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

        postRequest = new JsonArrayRequest(Request.Method.GET, freshnessIndex_brandplan_listurl,
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

                                listViewFIndex.setLayoutManager(new LinearLayoutManager(context));

                                listViewFIndex.setLayoutManager(new LinearLayoutManager(
                                        listViewFIndex.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                listViewFIndex.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(listViewFIndex);

                                freshnessIndexSnapAdapter = new FreshnessIndexSnapAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex);
                                // fIndexAdapter = new FreshnessIndexAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex);
                                listViewFIndex.setAdapter(freshnessIndexSnapAdapter);

                                // fIndexAdapter = new FreshnessIndexAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex);
                                // listViewFIndex.setAdapter(fIndexAdapter);
                                freshnessIndexSnapAdapter.notifyDataSetChanged();
                                txtStoreCode.setText(freshnessIndexDetailsArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(freshnessIndexDetailsArrayList.get(0).getStoreDescription());
                                FreshnessIndexValue += " > " + brandnm;
                                txtfIndexDeptName.setText(FreshnessIndexValue);
                                llfIndexhierarchy.setVisibility(View.VISIBLE);

                                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getBrandplanClass().toString();
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
        Log.e(TAG, "Department onsroll api" + fIndexFirstVisibleItem);
        //  Log.e("Header Class", txtFIndexClass.getText().toString());
        String url = " ";
        txtNoChart.setVisibility(View.GONE);

        if (fIndexFirstVisibleItem.equals("All")) {
            Log.e(TAG, "fIndexFirstVisibleItem.equals: ALL");
            ArrayList<PieEntry> entries = new ArrayList<PieEntry>();


            upcoming = (float) freshnessIndexDetail.getUpcomingGroupCount();
            oldgroup = (float) freshnessIndexDetail.getOldGroupCount();
            previousgroup = (float) freshnessIndexDetail.getPreviousGroupCount();
            currentgroup = (float) freshnessIndexDetail.getSohCurrentGrpCount();
            Log.e(TAG, "Values-------" + upcoming + "\t" + oldgroup + "\t" + previousgroup + "\t" + currentgroup);


            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(Color.parseColor("#31d6c5"));
            colors.add(Color.parseColor("#aea9fd"));
            colors.add(Color.parseColor("#ffc65b"));
            colors.add(Color.parseColor("#fe8081"));
            ArrayList<String> labels = new ArrayList<>();
            if (currentgroup > 0.0f) {

                entries.add(new PieEntry(currentgroup, "Current"));
                Log.e(TAG, "currentgroup: ");
            } else {
                current = true;
            }

            if (previousgroup > 0.0f) {

                entries.add(new PieEntry(previousgroup, "Previous"));
                Log.e(TAG, "previousgroup: ");

            } else {
                previous = true;

            }

            if (oldgroup > 0.0f) {

                entries.add(new PieEntry(oldgroup, "Old"));
                Log.e(TAG, "oldgroup: ");

            } else {
                old = true;

            }
            if (upcoming > 0.0f) {

                entries.add(new PieEntry(upcoming, "Upcoming"));
                Log.e(TAG, "upcoming: ");

            } else {
                upcome = true;


            }
            if (current && previous && old && upcome) {
                txtNoChart.setVisibility(View.VISIBLE);
                current = false;
                previous = false;
                old = false;
                upcome = false;

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
            pieData.setValueTextSize(11f);
            dataSet.setXValuePosition(null);
            dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            pieChart.setEntryLabelColor(Color.BLACK);
            pieChart.setExtraOffsets(5, 10, 5, 5);
            pieChart.setHoleRadius(0);
            //pieChart.setHoleColor(Color.WHITE);
            pieChart.setTransparentCircleRadius(0);
            pieChart.setData(pieData);
            pieChart.invalidate();
            pieChart.animateXY(4000, 4000);
            pieChart.setDescription(null);
            pieChart.setTouchEnabled(false);
            Legend l = pieChart.getLegend();
            l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
            l.setFormSize(11f);
            l.setEnabled(true);
            llfreshnessIndex.setVisibility(View.VISIBLE);
            Reusable_Functions.hDialog();
            return;
        }

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
        Log.e(TAG, "requestFIndexPieChart Url  " + url);

        postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, "Pie Chart on Scroll  : " + response);

                        Log.e("Pie Chart response", "" + response.length());
                        try {

                            int i;

                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                llfreshnessIndex.setVisibility(View.VISIBLE);


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
                                    if (fIndexFirstVisibleItem.equals("All")) {
                                        upcoming = (float) fresh.getUpcomingGroupCount();
                                        oldgroup = (float) fresh.getOldGroupCount();
                                        previousgroup = (float) fresh.getPreviousGroupCount();
                                        currentgroup = (float) fresh.getCurrentGroupCount();
                                        Log.e(TAG, "Values-------" + upcoming + "\t" + oldgroup + "\t" + previousgroup + "\t" + currentgroup);

                                    } else if (fIndexFirstVisibleItem.equals(fresh.getPlanDept())) {
                                        upcoming = (float) fresh.getUpcomingGroupCount();
                                        oldgroup = (float) fresh.getOldGroupCount();
                                        previousgroup = (float) fresh.getPreviousGroupCount();
                                        currentgroup = (float) fresh.getCurrentGroupCount();
                                        Log.e(TAG, "Values-------" + upcoming + "\t" + oldgroup + "\t" + previousgroup + "\t" + currentgroup);
                                    } else if (fIndexFirstVisibleItem.equals(fresh.getPlanCategory())) {
                                        upcoming = (float) fresh.getUpcomingGroupCount();
                                        oldgroup = (float) fresh.getOldGroupCount();
                                        previousgroup = (float) fresh.getPreviousGroupCount();
                                        currentgroup = (float) fresh.getCurrentGroupCount();
                                        Log.e(TAG, "Values-------" + upcoming + "\t" + oldgroup + "\t" + previousgroup + "\t" + currentgroup);

                                    } else if (fIndexFirstVisibleItem.equals(fresh.getPlanClass())) {
                                        upcoming = (float) fresh.getUpcomingGroupCount();
                                        oldgroup = (float) fresh.getOldGroupCount();
                                        previousgroup = (float) fresh.getPreviousGroupCount();
                                        currentgroup = (float) fresh.getCurrentGroupCount();
                                        Log.e(TAG, "Values-------" + upcoming + "\t" + oldgroup + "\t" + previousgroup + "\t" + currentgroup);

                                    } else if (fIndexFirstVisibleItem.equals(fresh.getBrandName())) {
                                        upcoming = (float) fresh.getUpcomingGroupCount();
                                        oldgroup = (float) fresh.getOldGroupCount();
                                        previousgroup = (float) fresh.getPreviousGroupCount();
                                        currentgroup = (float) fresh.getCurrentGroupCount();
                                        Log.e(TAG, "Values-------" + upcoming + "\t" + oldgroup + "\t" + previousgroup + "\t" + currentgroup);

                                    } else if (fIndexFirstVisibleItem.equals(fresh.getBrandplanClass())) {
                                        upcoming = (float) fresh.getUpcomingGroupCount();
                                        oldgroup = (float) fresh.getOldGroupCount();
                                        previousgroup = (float) fresh.getPreviousGroupCount();
                                        currentgroup = (float) fresh.getCurrentGroupCount();
                                        Log.e(TAG, "Values-------" + upcoming + "\t" + oldgroup + "\t" + previousgroup + "\t" + currentgroup);

                                    }
                                }
                                ArrayList<Integer> colors = new ArrayList<>();
                                colors.add(Color.parseColor("#31d6c5"));
                                colors.add(Color.parseColor("#aea9fd"));
                                colors.add(Color.parseColor("#ffc65b"));
                                colors.add(Color.parseColor("#fe8081"));
                                ArrayList<String> labels = new ArrayList<>();
                                if (currentgroup > 0.0f) {

                                    entries.add(new PieEntry(currentgroup, "Current"));
                                    Log.e(TAG, "currentgroup: ");
                                } else {
                                    current = true;
                                }

                                if (previousgroup > 0.0f) {

                                    entries.add(new PieEntry(previousgroup, "Previous"));
                                    Log.e(TAG, "previousgroup: ");

                                } else {
                                    previous = true;

                                }

                                if (oldgroup > 0.0f) {

                                    entries.add(new PieEntry(oldgroup, "Old"));
                                    Log.e(TAG, "oldgroup: ");

                                } else {
                                    old = true;

                                }
                                if (upcoming > 0.0f) {

                                    entries.add(new PieEntry(upcoming, "Upcoming"));
                                    Log.e(TAG, "upcoming: ");

                                } else {
                                    upcome = true;


                                }
                                if (current && previous && old && upcome) {
                                    txtNoChart.setVisibility(View.VISIBLE);
                                    current = false;
                                    previous = false;
                                    old = false;
                                    upcome = false;

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
                                pieData.setValueTextSize(11f);
                                dataSet.setXValuePosition(null);
                                dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                                pieChart.setEntryLabelColor(Color.BLACK);
                                pieChart.setExtraOffsets(5, 10, 5, 5);
                                pieChart.setHoleRadius(0);
                                //pieChart.setHoleColor(Color.WHITE);
                                pieChart.setTransparentCircleRadius(0);
                                pieChart.setData(pieData);
                                pieChart.invalidate();
                                pieChart.animateXY(4000, 4000);
                                pieChart.setDescription(null);
                                pieChart.setTouchEnabled(false);
                                Legend l = pieChart.getLegend();
                                l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
                                l.setFormSize(11f);
                                l.setEnabled(true);
                                llfreshnessIndex.setVisibility(View.VISIBLE);
                                Reusable_Functions.hDialog();
                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                            llfreshnessIndex.setVisibility(View.VISIBLE);

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                        llfreshnessIndex.setVisibility(View.VISIBLE);

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


    private void requestAll() {

        String fIdetails = ConstsCore.web_url + "/v1/display/freshnessindexheader/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level;
        Log.e(TAG, "requestAll" + fIdetails);


        postRequest = new JsonArrayRequest(Request.Method.GET, fIdetails,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Log.i(TAG,"Freshness Index Details Class: " + response);
                        Log.i(TAG, "response length and" + response.length() + "\n" + response);
                        int i;
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                llfreshnessIndex.setVisibility(View.GONE);

                                return;

                            } else {
                                freshnessIndexDetail = new FreshnessIndexDetails();
                                for (i = 0; i < response.length(); i++) {

                                    freshnessIndexDetail = gson.fromJson(response.get(i).toString(), FreshnessIndexDetails.class);
                                }


                                if (txtFIndexClass.getText().toString().equals("Department")) {
                                    freshnessIndexDetail.setPlanDept("All");
                                } else if (txtFIndexClass.getText().toString().equals("Category")) {
                                    freshnessIndexDetail.setPlanCategory("All");
                                } else if (txtFIndexClass.getText().toString().equals("Plan Class")) {
                                    freshnessIndexDetail.setPlanClass("All");
                                } else if (txtFIndexClass.getText().toString().equals("Brand")) {
                                    freshnessIndexDetail.setBrandName("All");
                                } else if (txtFIndexClass.getText().toString().equals("Brand Plan Class")) {
                                    freshnessIndexDetail.setBrandplanClass("All");
                                }

                                freshnessIndexDetail.setStkOnhandQty(freshnessIndexDetail.getStkOnhandQty());
                                freshnessIndexDetail.setStkOnhandQtyCount(100);
                                freshnessIndexDetail.setStkGitQty(freshnessIndexDetail.getStkGitQty());

                                //getCurrentGroupCount is not avaible in Api

                                freshnessIndexDetail.setUpcomingGroupCount(freshnessIndexDetail.getUpcomingGrpCount());
                                freshnessIndexDetail.setOldGroupCount(freshnessIndexDetail.getOldGrpCount());
                                freshnessIndexDetail.setPreviousGroupCount(freshnessIndexDetail.getPreviousGrpCount());
                                freshnessIndexDetail.setSohCurrentGrpCount(freshnessIndexDetail.getSohCurrentGrpCount());

                                Log.e(TAG, "Graph values: " + freshnessIndexDetail.getUpcomingGrpCount() + " " + freshnessIndexDetail.getOldGrpCount() + " " + freshnessIndexDetail.getPreviousGrpCount() + freshnessIndexDetail.getSohCurrentGrpCount());

                            }

                            freshnessIndexDetailsArrayList.add(0, freshnessIndexDetail);
                            Log.e(TAG, "After add 0 freshnessIndexDetailsArrayList: " + freshnessIndexDetailsArrayList.size());


                            listViewFIndex.setLayoutManager(new LinearLayoutManager(context));

                            listViewFIndex.setLayoutManager(new LinearLayoutManager(
                                    listViewFIndex.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                    LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                            listViewFIndex.setOnFlingListener(null);
                            new GravitySnapHelper(48).attachToRecyclerView(listViewFIndex);

                            freshnessIndexSnapAdapter = new FreshnessIndexSnapAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex);
                            Log.e(TAG, "onResponse: "+fromWhere );
                            // fIndexAdapter = new FreshnessIndexAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex);
                            listViewFIndex.setAdapter(freshnessIndexSnapAdapter);
                            //fIndexAdapter.notifyDataSetChanged();
                            txtStoreCode.setText(freshnessIndexDetailsArrayList.get(0).getStoreCode());
                            txtStoreDesc.setText(freshnessIndexDetailsArrayList.get(0).getStoreDescription());


                            if (txtFIndexClass.getText().toString().equals("Department")) {
                                level = 1;
                                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getPlanDept().toString();
                                //fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(focusposition).getPlanDept().toString();
                                Log.e(TAG, "fIndexFirstVisibleItem: " + fIndexFirstVisibleItem + "focus position is" + focusposition);
                            } else if (txtFIndexClass.getText().toString().equals("Category")) {
                                level = 2;
                                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getPlanCategory().toString();
                                // fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(focusposition).getPlanCategory().toString();
                                Log.e(TAG, "fIndexFirstVisibleItem: " + fIndexFirstVisibleItem + "focus position is" + focusposition);

                            } else if (txtFIndexClass.getText().toString().equals("Plan Class")) {
                                level = 3;
                                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getPlanClass().toString();
                                // fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(focusposition).getPlanClass().toString();
                                Log.e(TAG, "fIndexFirstVisibleItem: " + fIndexFirstVisibleItem + "focus position is" + focusposition);

                            } else if (txtFIndexClass.getText().toString().equals("Brand")) {
                                level = 4;
                                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getBrandName().toString();
                                //fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(focusposition).getBrandName().toString();
                                Log.e(TAG, "fIndexFirstVisibleItem: " + fIndexFirstVisibleItem + "focus position is" + focusposition);

                            } else if (txtFIndexClass.getText().toString().equals("Brand Plan Class")) {
                                level = 5;
                                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getBrandplanClass().toString();
                                // fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(focusposition).getBrandplanClass().toString();
                                Log.e(TAG, "fIndexFirstVisibleItem: " + fIndexFirstVisibleItem + "focus position is" + focusposition);

                            }
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            requestFIndexPieChart();

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "Data failed...", Toast.LENGTH_SHORT).show();
                            llfreshnessIndex.setVisibility(View.GONE);

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "Server not found", Toast.LENGTH_SHORT).show();
                        llfreshnessIndex.setVisibility(View.GONE);
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

 */
/*     Intent intent = new Intent(FreshnessIndexActivity.this, DashBoardActivity.class);
        intent.putExtra("BACKTO","inventory");
        startActivity(intent);*//*

        finish();
    }

}

*/
