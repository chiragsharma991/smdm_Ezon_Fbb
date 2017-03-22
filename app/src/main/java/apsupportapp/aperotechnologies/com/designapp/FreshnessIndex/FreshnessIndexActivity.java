package apsupportapp.aperotechnologies.com.designapp.FreshnessIndex;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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
import apsupportapp.aperotechnologies.com.designapp.RecyclerItemClickListener;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.RunningPromo.RecyclerViewPositionHelper;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesFilterActivity;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by pamrutkar on 22/11/16.
 */

public class FreshnessIndexActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    RadioButton btnCore, btnFashion;
    private static String FIndex_SegmentClick = "Fashion";
    ArrayList<FreshnessIndexDetails> freshnessIndexDetailsArrayList, fIndexArrayList;
    TextView txtStoreCode, txtStoreDesc, txtFIndexClass, txtfIndexDeptName, txtNoChart;
    String userId, bearertoken;
    SharedPreferences sharedPreferences;
    int offsetvalue = 0, limit = 100;
    int count = 0;
    RequestQueue queue;
    String FreshnessIndexValue;
    //activity_status_track_one git 6/1/17
    Context context;
    String fromWhere, freshnessIndex_ClickedVal, fIndexPlanDept, fIndexCategory, fIndexPlanClass, fIndexBrand;
    PieChart pieChart;
    RecyclerView listViewFIndex;
    int selFirstPositionValue;
    LinearLayout llfreshnessIndex, llfIndexhierarchy;
    SegmentedGroup segmented3;
    private static int level = 1;
    FreshnessIndexDetails freshnessIndexDetails, freshnessIndexDetail;
    RelativeLayout freshnessIndex_imageBtnBack, freshnessIndex_imgfilter;
    RelativeLayout btnFIndexPrev, btnFIndexNext;
    Gson gson;
    FreshnessIndexSnapAdapter freshnessIndexSnapAdapter;
    PieData pieData;
    float upcoming = 0.0f, oldgroup = 0.0f, previousgroup = 0.0f, currentgroup = 0.0f,coreGroupCount=0.0f;
    PieDataSet dataSet;
    private String TAG = "FreshnessIndexActivity";
    private boolean current = false, previous = false, old = false, upcome = false,coregroup=false;
    private int totalItemCount = 0;
    int firstVisibleItem = 0;
    JsonArrayRequest postRequest;
    private ProgressBar processBar;
    int prevState = RecyclerView.SCROLL_STATE_IDLE;
    int currentState = RecyclerView.SCROLL_STATE_IDLE;
    private String fIndexFirstVisibleItem ;
    private boolean OnItemClick = false,filter_toggleClick = false;
    private int OveridePositionValue = 0;
    public static Activity freshness_Index;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freshness_index);
        getSupportActionBar().hide();

        fromWhere = "Department";
        fIndexFirstVisibleItem = "";
        freshnessIndex_ClickedVal = "";
        FreshnessIndexValue = "";
        context = this;
        freshness_Index = this;
        level = 1;
       
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
              /*  Intent intent = new Intent(FreshnessIndexActivity.this, DashBoardActivity.class);
                intent.putExtra("BACKTO","inventory");
                startActivity(intent);*/
                onBackPressed();
            }
        });
        freshnessIndex_imgfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FreshnessIndexActivity.this, SalesFilterActivity.class);
                intent.putExtra("checkfrom", "freshnessIndex");
                startActivity(intent);
                // finish();
            }
        });
        if (Reusable_Functions.chkStatus(context)) {
            // Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            processBar.setVisibility(View.GONE);

            offsetvalue = 0;
            limit = 100;
            count = 0;
            level = 1;
            llfIndexhierarchy.setVisibility(View.GONE);

            if (getIntent().getStringExtra("selectedDept") == null)
            {
                filter_toggleClick = false;
                retainValuesFilter();
                requestFreshnessIndexDetails();
            }
            else if(getIntent().getStringExtra("selectedDept") != null)
            {
                String  selectedString  = getIntent().getStringExtra("selectedDept");
                filter_toggleClick = true;
                retainValuesFilter();
                requestFreshnessIndexFilterVal(selectedString);

            }

        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
            llfreshnessIndex.setVisibility(View.GONE);

        }

        // previous
        btnFIndexPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstVisibleItem = 0;

                if (postRequest != null) {
                    postRequest.cancel();
                }
                FreshnessIndexValue="";

                switch (txtFIndexClass.getText().toString()) {

                    case "MC":
                        btnFIndexNext.setVisibility(View.VISIBLE);
                        txtFIndexClass.setText("Subclass");
                        fromWhere = "Subclass";
                        level = 4;
                        //  flag = false;
                        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
                        // llfIndexhierarchy.setVisibility(View.GONE);
                        // llfreshnessIndex.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            processBar.setVisibility(View.GONE);

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

                        break;

                    case "Subclass":
                        txtFIndexClass.setText("Class");
                        fromWhere = "Class";
                        level = 3;
                        //flag = false;
                        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
                        //     llfIndexhierarchy.setVisibility(View.GONE);
                        //llfreshnessIndex.setVisibility(View.GONE);

                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");                                                processBar.setVisibility(View.GONE);
                            processBar.setVisibility(View.GONE);
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


                        break;


                    case "Class":
                        txtFIndexClass.setText("Subdept");
                        fromWhere = "Subdept";
                        level = 2;
                        // flag = false;
                        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
                        // llfIndexhierarchy.setVisibility(View.GONE);
                        // llfreshnessIndex.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            processBar.setVisibility(View.GONE);
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


                        break;

                    case "Subdept":
                        btnFIndexPrev.setVisibility(View.INVISIBLE);
                        txtFIndexClass.setText("Department");
                        fromWhere = "Department";
                        level = 1;
                        //  flag = false;
                        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
                        //  llfIndexhierarchy.setVisibility(View.GONE);
                        //  llfreshnessIndex.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            processBar.setVisibility(View.GONE);
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


                        break;
                    default:
                }
            }

        });

        // next-----
        btnFIndexNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstVisibleItem = 0;
                if (postRequest != null) {
                    postRequest.cancel();
                }
                FreshnessIndexValue="";
                switch (txtFIndexClass.getText().toString()) {

                    case "Department":
                        btnFIndexPrev.setVisibility(View.VISIBLE);
                        txtFIndexClass.setText("Subdept");
                        fromWhere = "Subdept";
                        level = 2;
                        //flag = false;
                        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
                        //  llfIndexhierarchy.setVisibility(View.GONE);
                        // llfreshnessIndex.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            processBar.setVisibility(View.GONE);
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
                        break;

                    case "Subdept":
                        fromWhere = "Class";
                        txtFIndexClass.setText("Class");
                        level = 3;
                        // flag = false;
                        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
                        //    llfIndexhierarchy.setVisibility(View.GONE);
                        //  llfreshnessIndex.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            processBar.setVisibility(View.GONE);
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

                        break;
                    case "Class":
                        txtFIndexClass.setText("Subclass");
                        fromWhere = "Subclass";
                        level = 4;
                        // flag = false;
                        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
                        //  llfIndexhierarchy.setVisibility(View.GONE);
                        //  llfreshnessIndex.setVisibility(View.GONE);

                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            processBar.setVisibility(View.GONE);
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

                        break;

                    case "Subclass":
                        btnFIndexNext.setVisibility(View.INVISIBLE);
                        txtFIndexClass.setText("MC");
                        //  flag = false;
                        fromWhere = "MC";
                        level = 5;
                        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
                        //   llfIndexhierarchy.setVisibility(View.GONE);
                        //   llfreshnessIndex.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            processBar.setVisibility(View.GONE);
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

                        break;
                    default:
                }
            }
        });


  /*      listViewFIndex.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });*/


        listViewFIndex.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {


                    @Override
                    public void onItemClick(View v, final int position) {

                        OnItemClick = true;
                        Reusable_Functions.sDialog(context, "Loading data...");
                        processBar.setVisibility(View.GONE);


                        Handler h = new Handler();
                        h.postDelayed(new Runnable() {
                            public void run() {


                                if (position < freshnessIndexDetailsArrayList.size()) {
                                    // TestItem();
                                    switch (txtFIndexClass.getText().toString()) {


                                        case "Department":
                                            btnFIndexPrev.setVisibility(View.VISIBLE);
                                            txtFIndexClass.setText("Subdept");
                                            freshnessIndex_ClickedVal = freshnessIndexDetailsArrayList.get(position).getPlanDept();
                                            Log.e(TAG, "onItemClick: " + position + "and item is " + freshnessIndex_ClickedVal);
                                            //llfIndexhierarchy.setVisibility(View.GONE);
                                            //llfreshnessIndex.setVisibility(View.GONE);
                                            fromWhere = "Subdept";
                                            level = 2;
                                            if (Reusable_Functions.chkStatus(context)) {
                                                if (postRequest != null) {
                                                    postRequest.cancel();
                                                }
                                                //  Reusable_Functions.hDialog();
                                                Reusable_Functions.sDialog(context, "Loading data...");
                                                processBar.setVisibility(View.GONE);
                                                offsetvalue = 0;
                                                limit = 100;
                                                count = 0;
                                                freshnessIndexDetailsArrayList.clear();
                                                request_FreshnessIndex_CategoryList(freshnessIndex_ClickedVal);

                                                fIndexPlanDept = freshnessIndex_ClickedVal;

                                            } else {
                                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                                Reusable_Functions.hDialog();

                                            }

                                            break;

                                        case "Subdept":
                                            txtFIndexClass.setText("Class");
                                            // llfIndexhierarchy.setVisibility(View.GONE);
                                            // llfreshnessIndex.setVisibility(View.GONE);
                                            freshnessIndex_ClickedVal = freshnessIndexDetailsArrayList.get(position).getPlanCategory();
                                            fromWhere = "Class";
                                            level = 3;
                                            if (Reusable_Functions.chkStatus(context)) {
                                                if (postRequest != null) {
                                                    postRequest.cancel();
                                                }
                                                //   Reusable_Functions.hDialog();
                                                Reusable_Functions.sDialog(context, "Loading data...");
                                                processBar.setVisibility(View.GONE);

                                                offsetvalue = 0;
                                                limit = 100;
                                                count = 0;
                                                freshnessIndexDetailsArrayList.clear();
                                                fIndexCategory = freshnessIndex_ClickedVal;
                                                request_FreshnessIndex_PlanClassList(fIndexPlanDept, fIndexCategory);
                                            } else {
                                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                                Reusable_Functions.hDialog();

                                            }


                                            break;
                                        case "Class":
                                            txtFIndexClass.setText("Subclass");
                                            //  llfIndexhierarchy.setVisibility(View.GONE);
                                            //  llfreshnessIndex.setVisibility(View.GONE);
                                            freshnessIndex_ClickedVal = freshnessIndexDetailsArrayList.get(position).getPlanClass();
                                            fromWhere = "Subclass";
                                            level = 4;
                                            if (Reusable_Functions.chkStatus(context)) {
                                                if (postRequest != null) {
                                                    postRequest.cancel();
                                                }
                                                //  Reusable_Functions.hDialog();
                                                Reusable_Functions.sDialog(context, "Loading data...");
                                                processBar.setVisibility(View.GONE);

                                                offsetvalue = 0;
                                                limit = 100;
                                                count = 0;
                                                freshnessIndexDetailsArrayList.clear();
                                                fIndexPlanClass = freshnessIndex_ClickedVal;
                                                request_FreshnessIndex_BrandList(fIndexPlanDept, fIndexCategory, fIndexPlanClass);

                                            } else {
                                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                                Reusable_Functions.hDialog();
                                            }
                                            break;

                                        case "Subclass":
                                            btnFIndexNext.setVisibility(View.INVISIBLE);
                                            txtFIndexClass.setText("MC");
                                            //    llfIndexhierarchy.setVisibility(View.GONE);
                                            //    llfreshnessIndex.setVisibility(View.GONE);
                                            freshnessIndex_ClickedVal = freshnessIndexDetailsArrayList.get(position).getBrandName();
                                            fromWhere = "MC";
                                            level = 5;
                                            if (Reusable_Functions.chkStatus(context)) {
                                                if (postRequest != null) {
                                                    postRequest.cancel();
                                                }
                                                //  Reusable_Functions.hDialog();
                                                Reusable_Functions.sDialog(context, "Loading data...");
                                                processBar.setVisibility(View.GONE);

                                                offsetvalue = 0;
                                                limit = 100;
                                                count = 0;
                                                freshnessIndexDetailsArrayList.clear();
                                                fIndexBrand = freshnessIndex_ClickedVal;
                                                request_FreshnessIndex_BrandPlanList(fIndexPlanDept, fIndexCategory, fIndexPlanClass, fIndexBrand);
                                            } else {
                                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                                Reusable_Functions.hDialog();
                                            }
                                            break;

                                        default:
                                            Reusable_Functions.hDialog();
                                            Toast.makeText(context, " You are at the last level of hierarchy", Toast.LENGTH_SHORT).show();
                                            OnItemClick = false;
                                            break;

                                    }
                                } else {
                                    Reusable_Functions.hDialog();
                                    Log.e(TAG, "position problem is: " + position + "size is " + freshnessIndexDetailsArrayList.size());
                                }
                            }
                        }, 700);
                    }
                })
        );

        // hierarchy level drill down on selected item click


        //list view on Scroll event

        listViewFIndex.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                RecyclerViewPositionHelper mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
                int visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mRecyclerViewHelper.getItemCount();
                firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();

            }


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, final int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                currentState = newState;
                // if (prevState != RecyclerView.SCROLL_STATE_IDLE && currentState == RecyclerView.SCROLL_STATE_IDLE && OnItemClick==false) {
                if (prevState != RecyclerView.SCROLL_STATE_IDLE && currentState == RecyclerView.SCROLL_STATE_IDLE) {

                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        public void run() {
                            if (OnItemClick == false) {
                                TimeUP();

                            }
                        }
                    }, 700);


                }
                prevState = currentState;


            }


        });


    }



    private void TestItem() {
        if (txtFIndexClass.getText().toString().equals("Department")) {
            level = 1;
            fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(0).getPlanDept().toString();
        } else if (txtFIndexClass.getText().toString().equals("Subdept")) {
            level = 2;
            fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(0).getPlanCategory().toString();

        } else if (txtFIndexClass.getText().toString().equals("Class")) {
            level = 3;
            fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(0).getPlanClass().toString();

        } else if (txtFIndexClass.getText().toString().equals("Subclass")) {
            level = 4;
            fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(0).getBrandName().toString();

        } else if (txtFIndexClass.getText().toString().equals("MC")) {
            level = 5;
            fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(0).getBrandplanClass().toString();

        }
    }

    private void TimeUP() {

        Log.e(TAG, "select scroll item position: " + firstVisibleItem);


        if (firstVisibleItem < freshnessIndexSnapAdapter.getItemCount() - 1 && OnItemClick == false) {


            //10<10 where footer is call then it goes else condition
            if (txtFIndexClass.getText().toString().equals("Department")) {
                level = 1;
                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getPlanDept().toString();
            } else if (txtFIndexClass.getText().toString().equals("Subdept")) {
                level = 2;
                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getPlanCategory().toString();
            } else if (txtFIndexClass.getText().toString().equals("Class")) {
                level = 3;
                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getPlanClass().toString();
            } else if (txtFIndexClass.getText().toString().equals("Subclass")) {
                level = 4;
                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getBrandName().toString();
            } else if (txtFIndexClass.getText().toString().equals("MC")) {
                level = 5;
                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getBrandplanClass().toString();
            }
            //  if (firstVisibleItem != selFirstPositionValue) {
            if (Reusable_Functions.chkStatus(context)) {
                Reusable_Functions.hDialog();
                offsetvalue = 0;
                limit = 100;
                count = 0;
                fIndexArrayList = new ArrayList<FreshnessIndexDetails>();

                if (firstVisibleItem != OveridePositionValue) {
                    if (postRequest != null) {
                        postRequest.cancel();
                    }
                    processBar.setVisibility(View.VISIBLE);
                    requestFIndexPieChart();
                    OveridePositionValue = firstVisibleItem;
                }


            } else {
                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
            }
        } else {

            firstVisibleItem = freshnessIndexDetailsArrayList.size() - 1;
            LinearLayoutManager llm = (LinearLayoutManager) listViewFIndex.getLayoutManager();
            llm.scrollToPosition(firstVisibleItem);


            if (txtFIndexClass.getText().toString().equals("Department")) {
                level = 1;
                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getPlanDept().toString();
            } else if (txtFIndexClass.getText().toString().equals("Subdept")) {
                level = 2;
                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getPlanCategory().toString();
            } else if (txtFIndexClass.getText().toString().equals("Class")) {
                level = 3;
                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getPlanClass().toString();
            } else if (txtFIndexClass.getText().toString().equals("Subclass")) {
                level = 4;
                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getBrandName().toString();
            } else if (txtFIndexClass.getText().toString().equals("MC")) {
                level = 5;
                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getBrandplanClass().toString();
            }
            //  if (firstVisibleItem != selFirstPositionValue) {
            if (Reusable_Functions.chkStatus(context)) {
                Reusable_Functions.hDialog();
                offsetvalue = 0;
                limit = 100;
                count = 0;
                fIndexArrayList = new ArrayList<FreshnessIndexDetails>();

                if (firstVisibleItem != OveridePositionValue) {
                    if (postRequest != null) {
                        postRequest.cancel();
                    }
                    processBar.setVisibility(View.VISIBLE);
                    requestFIndexPieChart();
                    OveridePositionValue = firstVisibleItem;
                }


            } else {
                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void initializeUI() {
        txtStoreCode = (TextView) findViewById(R.id.txtStoreCode);
        txtStoreDesc = (TextView) findViewById(R.id.txtStoreName);
        processBar = (ProgressBar) findViewById(R.id.progressBar);
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
        // btnFashion.toggle();
        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
        fIndexArrayList = new ArrayList<FreshnessIndexDetails>();
    }
    public void retainValuesFilter() {
        filter_toggleClick = true;
        if (FIndex_SegmentClick.equals("All")) {
            btnCore.toggle();
            Log.e(TAG, "Fashion toggle: " );


        } else {
            btnFashion.toggle();
            Log.e(TAG, "Core toggle: " );
        }

    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        OnItemClick = true;
        FreshnessIndexValue="";
        if(filter_toggleClick == false) {
            switch (checkedId) {

                case R.id.btnCore:
                    //Toast.makeText(SalesPvAActivity.this, "WTD", Toast.LENGTH_SHORT).show();
                    if (FIndex_SegmentClick.equals("All"))
                        break;
                    FIndex_SegmentClick = "All";
                    //   llfIndexhierarchy.setVisibility(View.GONE);
                    //  llfreshnessIndex.setVisibility(View.GONE);
                    freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
                    if (Reusable_Functions.chkStatus(context)) {
                        // Reusable_Functions.hDialog();
                        // Reusable_Functions.sDialog(context, "Loading data...");
                        if (postRequest != null) {
                            postRequest.cancel();
                        }
                        processBar.setVisibility(View.VISIBLE);

                        offsetvalue = 0;
                        limit = 100;
                        count = 0;
                        if (getIntent().getStringExtra("selectedDept") == null) {
                            requestFreshnessIndexDetails();
                        } else if (getIntent().getStringExtra("selectedDept") != null) {
                            String selectedString = getIntent().getStringExtra("selectedDept");
                            requestFreshnessIndexFilterVal(selectedString);

                        }

                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        processBar.setVisibility(View.GONE);

                    }
                    break;

                case R.id.btnFashion:
                    //Toast.makeText(SalesPvAActivity.this, "LW", Toast.LENGTH_SHORT).show();
                    if (FIndex_SegmentClick.equals("Fashion"))
                        break;

                    FIndex_SegmentClick = "Fashion";
                    //  llfIndexhierarchy.setVisibility(View.GONE);
                    // llfreshnessIndex.setVisibility(View.GONE);
                    freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
                    if (Reusable_Functions.chkStatus(context)) {
                        //    Reusable_Functions.hDialog();
                        //  Reusable_Functions.sDialog(context, "Loading data...");
                        if (postRequest != null) {
                            postRequest.cancel();

                        }
                        processBar.setVisibility(View.VISIBLE);
                        offsetvalue = 0;
                        limit = 100;
                        count = 0;
                        if (getIntent().getStringExtra("selectedDept") == null) {
                            requestFreshnessIndexDetails();
                        } else if (getIntent().getStringExtra("selectedDept") != null) {
                            String selectedString = getIntent().getStringExtra("selectedDept");
                            requestFreshnessIndexFilterVal(selectedString);

                        }
                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        processBar.setVisibility(View.GONE);

                    }


                    break;

                default:
                    break;

            }
        }else
        {
            filter_toggleClick = false;
        }
    }


    //----------------------------API Declaration---------------------------//
    // API 1.31
    private void requestFreshnessIndexDetails() {

        String fIdetails = "";
        fIdetails = ConstsCore.web_url + "/v1/display/freshnessindexdetail/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e(TAG, "requestFreshnessIndexDetails: " + fIdetails);

        postRequest = new JsonArrayRequest(Request.Method.GET, fIdetails,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, "onResponse:"+response);
                        int i;
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                OnItemClick = false;

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
                                //this>>
                                requestAll();
                            }

                        } catch (Exception e) {
                            OnItemClick = false;

                            // Reusable_Functions.hDialog();
                            //Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        OnItemClick = false;

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

        Log.e(TAG, "request_FreshnessIndex_CategoryList: " + freshnessindex_category_listurl);
        postRequest = new JsonArrayRequest(Request.Method.GET, freshnessindex_category_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int i;
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "No Subdept data found", Toast.LENGTH_SHORT).show();
                                processBar.setVisibility(View.GONE);
                                OnItemClick = false;


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
                                //fIndexAdapter = new FreshnessIndexAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex);
                                listViewFIndex.setAdapter(freshnessIndexSnapAdapter);
                                TestItem();
                                //0 fIndexAdapter = new FreshnessIndexAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex);
                                //listViewFIndex.setAdapter(fIndexAdapter);
                                //   flag = true;
                                //   freshnessIndexSnapAdapter.notifyDataSetChanged();
                                txtStoreCode.setText(freshnessIndexDetailsArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(freshnessIndexDetailsArrayList.get(0).getStoreDescription());
                                FreshnessIndexValue = " ";
                                FreshnessIndexValue =" > "+deptName;
                                txtfIndexDeptName.setText(FreshnessIndexValue);
                                llfIndexhierarchy.setVisibility(View.VISIBLE);
                                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(0).getPlanCategory().toString();
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                level = 2;
                                //  processBar.setVisibility(View.VISIBLE);
                                requestFIndexPieChart();

                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "No Subdept data found", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "in catch: " + e.getMessage());
                            processBar.setVisibility(View.GONE);
                            OnItemClick = false;

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "No Subdept data found", Toast.LENGTH_SHORT).show();
                        processBar.setVisibility(View.GONE);
                        OnItemClick = false;

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

        String freshnessIndex_planclass_listurl = null;
        freshnessIndex_planclass_listurl = ConstsCore.web_url + "/v1/display/freshnessindexdetail/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&category=" + category.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;

        Log.e(TAG, "request_FreshnessIndex_PlanClassList: " + freshnessIndex_planclass_listurl);
        postRequest = new JsonArrayRequest(Request.Method.GET, freshnessIndex_planclass_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "No Class data found", Toast.LENGTH_SHORT).show();
                                processBar.setVisibility(View.GONE);
                                OnItemClick = false;


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
                                TestItem();

                                //  fIndexAdapter = new FreshnessIndexAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex);
                                // listViewFIndex.setAdapter(fIndexAdapter);
                                //flag = true;
                                freshnessIndexSnapAdapter.notifyDataSetChanged();
                                txtStoreCode.setText(freshnessIndexDetailsArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(freshnessIndexDetailsArrayList.get(0).getStoreDescription());
                                FreshnessIndexValue +=" > "+category;
                                txtfIndexDeptName.setText(FreshnessIndexValue);
                                llfIndexhierarchy.setVisibility(View.VISIBLE);
                                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(0).getPlanClass().toString();
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                level = 3;
                                // processBar.setVisibility(View.VISIBLE);
                                requestFIndexPieChart();

                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "No Class data found", Toast.LENGTH_SHORT).show();
                            processBar.setVisibility(View.GONE);
                            OnItemClick = false;

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "No Class data found", Toast.LENGTH_SHORT).show();
                        processBar.setVisibility(View.GONE);
                        OnItemClick = false;

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

        String freshnessIndex_brand_listurl;

        freshnessIndex_brand_listurl = ConstsCore.web_url + "/v1/display/freshnessindexdetail/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level+"&class="+planclass.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;


        Log.e(TAG, "request_FreshnessIndex_BrandList: " + freshnessIndex_brand_listurl);


        postRequest = new JsonArrayRequest(Request.Method.GET, freshnessIndex_brand_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "No Subclass data found", Toast.LENGTH_SHORT).show();
                                processBar.setVisibility(View.GONE);
                                OnItemClick = false;

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
                                TestItem();


                                // fIndexAdapter = new FreshnessIndexAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex);
                                //listViewFIndex.setAdapter(fIndexAdapter);
                                //  flag = true;
                                freshnessIndexSnapAdapter.notifyDataSetChanged();
                                txtStoreCode.setText(freshnessIndexDetailsArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(freshnessIndexDetailsArrayList.get(0).getStoreDescription());

                                FreshnessIndexValue+=" > "+planclass;
                                txtfIndexDeptName.setText(FreshnessIndexValue);
                                llfIndexhierarchy.setVisibility(View.VISIBLE);
                                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(0).getBrandName().toString();
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                level = 4;
                                //   processBar.setVisibility(View.VISIBLE);
                                requestFIndexPieChart();

                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "No Subclass data found", Toast.LENGTH_SHORT).show();
                            OnItemClick = false;
                            Log.e(TAG, "catch : " + e.getMessage());
                            processBar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "No Subclass data found", Toast.LENGTH_SHORT).show();
                        OnItemClick = false;
                        processBar.setVisibility(View.GONE);
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

        String freshnessIndex_brandplan_listurl = null;
        freshnessIndex_brandplan_listurl = ConstsCore.web_url + "/v1/display/freshnessindexdetail/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level+"&brand="+brandnm.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;


        Log.e(TAG, "request_FreshnessIndex_BrandPlanList: " + freshnessIndex_brandplan_listurl);

        postRequest = new JsonArrayRequest(Request.Method.GET, freshnessIndex_brandplan_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {

                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "No MC data found", Toast.LENGTH_SHORT).show();
                                processBar.setVisibility(View.GONE);
                                OnItemClick = false;


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
                                TestItem();


                                // fIndexAdapter = new FreshnessIndexAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex);
                                // listViewFIndex.setAdapter(fIndexAdapter);
                                freshnessIndexSnapAdapter.notifyDataSetChanged();
                                txtStoreCode.setText(freshnessIndexDetailsArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(freshnessIndexDetailsArrayList.get(0).getStoreDescription());
                                FreshnessIndexValue+=" > "+brandnm;
                                txtfIndexDeptName.setText(FreshnessIndexValue);
                                llfIndexhierarchy.setVisibility(View.VISIBLE);

                                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(0).getBrandplanClass().toString();
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                level = 5;
                                //  processBar.setVisibility(View.VISIBLE);
                                requestFIndexPieChart();
                            }


                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "No MC data found", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "catch : " + e.getMessage());
                            OnItemClick = false;
                            processBar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "No MC data found", Toast.LENGTH_SHORT).show();
                        OnItemClick = false;
                        processBar.setVisibility(View.GONE);
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

        Log.e(TAG, "requestFIndexPieChart selected item: " + fIndexFirstVisibleItem);

        offsetvalue = 0;
        limit = 100;
        count = 0;
        String url = " ";
        txtNoChart.setVisibility(View.GONE);
        fIndexArrayList = new ArrayList<FreshnessIndexDetails>();

        if (fIndexFirstVisibleItem.equals("All")) {
            ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

            upcoming = (float) freshnessIndexDetail.getUpcomingGroupCount();
            oldgroup = (float) freshnessIndexDetail.getOldGroupCount();
            previousgroup = (float) freshnessIndexDetail.getPreviousGroupCount();
            currentgroup = (float) freshnessIndexDetail.getSohCurrentGrpCount();
            coreGroupCount = (float) freshnessIndexDetail.getCoreGrpCount();
            //coreGroupCount = (float) 22.0;
       /*    upcoming = (float)6.34;
            oldgroup = (float) 5.5;
            previousgroup = (float) 66.35;
            currentgroup = (float) 20.8;*/

            Log.e(TAG, "requestFIndexPieChart: " + upcoming + " ," + oldgroup + " ," + previousgroup + " ," + currentgroup+ " ," + coreGroupCount);

            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(Color.parseColor("#31d6c5"));
            colors.add(Color.parseColor("#aea9fd"));
            colors.add(Color.parseColor("#ffc65b"));
            colors.add(Color.parseColor("#fe8081"));
            colors.add(Color.parseColor("#e8c0bb"));
            ArrayList<String> labels = new ArrayList<>();
            if (currentgroup > 0.0f)
            {
                entries.add(new PieEntry(currentgroup, "Current"));
            } else
            {
                current = true;
            }

            if (previousgroup > 0.0f)
            {
                entries.add(new PieEntry(previousgroup, "Previous"));
            } else {
                previous = true;
            }

            if (oldgroup > 0.0f) {
                entries.add(new PieEntry(oldgroup, "Old"));
            } else {
                old = true;
            }
            if (upcoming > 0.0f) {
                entries.add(new PieEntry(upcoming, "Upcoming"));
            } else {
                upcome = true;
            }  if (coreGroupCount > 0.0f) {
                entries.add(new PieEntry(coreGroupCount, "Core"));
            } else {
                coregroup = true;
            }
            if (current && previous && old && upcome && coregroup) {
                txtNoChart.setVisibility(View.VISIBLE);
            }
            current = false;
            previous = false;
            old = false;
            upcome = false;
            coregroup=false;

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
            processBar.setVisibility(View.GONE);
            Reusable_Functions.hDialog();
            OnItemClick = false;
            return;
        }

        fIndexFirstVisibleItem = fIndexFirstVisibleItem.replace("%","%25");
        fIndexFirstVisibleItem = fIndexFirstVisibleItem.replace(" ","%20").replace("&","%26");

        if (txtFIndexClass.getText().toString().equals("Department")) {
            url = ConstsCore.web_url + "/v1/display/freshnessindexdetail/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&dept=" + fIndexFirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit;
        }
        else if (txtFIndexClass.getText().toString().equals("Subdept")) {
            url = ConstsCore.web_url + "/v1/display/freshnessindexdetail/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&category=" + fIndexFirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit;
        }
        else if (txtFIndexClass.getText().toString().equals("Class")) {
            url = ConstsCore.web_url + "/v1/display/freshnessindexdetail/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&class=" + fIndexFirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit;
        }
        else if (txtFIndexClass.getText().toString().equals("Subclass")) {
            url = ConstsCore.web_url + "/v1/display/freshnessindexdetail/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&brand=" + fIndexFirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit;
        }
        else if (txtFIndexClass.getText().toString().equals("MC")) {
            url = ConstsCore.web_url + "/v1/display/freshnessindexdetail/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&brandclass=" + fIndexFirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit;
        }
        Log.e(TAG, "requestFIndexPieChart: " + url);

        postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {

                            int i;

                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                llfreshnessIndex.setVisibility(View.VISIBLE);
                                OnItemClick = false;


                            } else if (response.length() == limit) {

                                for (i = 0; i < response.length(); i++) {

                                    freshnessIndexDetails = gson.fromJson(response.get(i).toString(), FreshnessIndexDetails.class);
                                    fIndexArrayList.add(freshnessIndexDetails);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestFIndexPieChart();


                            } else if (response.length() < limit) {
                                Log.e(TAG, "onResponse: "+response );

                                for (i = 0; i < response.length(); i++) {

                                    freshnessIndexDetails = gson.fromJson(response.get(i).toString(), FreshnessIndexDetails.class);
                                    fIndexArrayList.add(freshnessIndexDetails);
                                }

                                ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
                                fIndexFirstVisibleItem = fIndexFirstVisibleItem.replace("%25","%");
                                fIndexFirstVisibleItem = fIndexFirstVisibleItem.replace("%20"," ").replace("%26","&");
                                for (FreshnessIndexDetails fresh : fIndexArrayList) {
                                    Log.e(TAG, "fresh: "+fresh.getPlanCategory()+fIndexFirstVisibleItem );
                                    if (fIndexFirstVisibleItem.equals("All")) {
                                        upcoming = (float) fresh.getUpcomingGroupCount();
                                        oldgroup = (float) fresh.getOldGroupCount();
                                        previousgroup = (float) fresh.getPreviousGroupCount();
                                        currentgroup = (float) fresh.getCurrentGroupCount();
                                        coreGroupCount = (float) fresh.getCoreGroupCount();
                                        Log.e(TAG, "requestFIndexPieChart: " + upcoming + " ," + oldgroup + " ," + previousgroup + " ," + currentgroup + " ," + coreGroupCount);


                                    } else if (fIndexFirstVisibleItem.equals(fresh.getPlanDept())) {

                                        upcoming = (float) fresh.getUpcomingGroupCount();
                                        oldgroup = (float) fresh.getOldGroupCount();
                                        previousgroup = (float) fresh.getPreviousGroupCount();
                                        currentgroup = (float) fresh.getCurrentGroupCount();
                                        coreGroupCount = (float) fresh.getCoreGroupCount();
                                        Log.e(TAG, "requestFIndexPieChart: " + upcoming + " ," + oldgroup + " ," + previousgroup + " ," + currentgroup + " ," + coreGroupCount);

                                    } else if (fIndexFirstVisibleItem.equals(fresh.getPlanCategory())) {

                                        Log.e(TAG, "in dept pie chart"+fresh.getPlanCategory());

                                        upcoming = (float) fresh.getUpcomingGroupCount();
                                        oldgroup = (float) fresh.getOldGroupCount();
                                        previousgroup = (float) fresh.getPreviousGroupCount();
                                        currentgroup = (float) fresh.getCurrentGroupCount();
                                        coreGroupCount = (float) fresh.getCoreGroupCount();
                                        Log.e(TAG, "requestFIndexPieChart: " + upcoming + " ," + oldgroup + " ," + previousgroup + " ," + currentgroup + " ," + coreGroupCount);

                                    } else if (fIndexFirstVisibleItem.equals(fresh.getPlanClass())) {
                                        upcoming = (float) fresh.getUpcomingGroupCount();
                                        oldgroup = (float) fresh.getOldGroupCount();
                                        previousgroup = (float) fresh.getPreviousGroupCount();
                                        currentgroup = (float) fresh.getCurrentGroupCount();
                                        coreGroupCount = (float) fresh.getCoreGroupCount();
                                        Log.e(TAG, "requestFIndexPieChart: " + upcoming + " ," + oldgroup + " ," + previousgroup + " ," + currentgroup + " ," + coreGroupCount);

                                    } else if (fIndexFirstVisibleItem.equals(fresh.getBrandName())) {
                                        upcoming = (float) fresh.getUpcomingGroupCount();
                                        oldgroup = (float) fresh.getOldGroupCount();
                                        previousgroup = (float) fresh.getPreviousGroupCount();
                                        currentgroup = (float) fresh.getCurrentGroupCount();
                                        coreGroupCount = (float) fresh.getCoreGroupCount();
                                        Log.e(TAG, "requestFIndexPieChart: " + upcoming + " ," + oldgroup + " ," + previousgroup + " ," + currentgroup + " ," + coreGroupCount);

                                    } else if (fIndexFirstVisibleItem.equals(fresh.getBrandplanClass())) {
                                        upcoming = (float) fresh.getUpcomingGroupCount();
                                        oldgroup = (float) fresh.getOldGroupCount();
                                        previousgroup = (float) fresh.getPreviousGroupCount();
                                        currentgroup = (float) fresh.getCurrentGroupCount();
                                        coreGroupCount = (float) fresh.getCoreGroupCount();
                                        Log.e(TAG, "requestFIndexPieChart: " + upcoming + " ," + oldgroup + " ," + previousgroup + " ," + currentgroup + " ," + coreGroupCount);
                                    }
                                }
                                ArrayList<Integer> colors = new ArrayList<>();
                                colors.add(Color.parseColor("#31d6c5"));
                                colors.add(Color.parseColor("#aea9fd"));
                                colors.add(Color.parseColor("#ffc65b"));
                                colors.add(Color.parseColor("#fe8081"));
                                colors.add(Color.parseColor("#e8c0bb"));

                                ArrayList<String> labels = new ArrayList<>();
                                if (currentgroup > 0.0f) {

                                    entries.add(new PieEntry(currentgroup, "Current"));
                                } else {
                                    current = true;
                                }

                                if (previousgroup > 0.0f) {

                                    entries.add(new PieEntry(previousgroup, "Previous"));

                                } else {
                                    previous = true;

                                }

                                if (oldgroup > 0.0f) {

                                    entries.add(new PieEntry(oldgroup, "Old"));

                                } else {
                                    old = true;

                                }
                                if (upcoming > 0.0f) {

                                    entries.add(new PieEntry(upcoming, "Upcoming"));

                                } else {
                                    upcome = true;


                                }
                                if (coreGroupCount > 0.0f) {

                                    entries.add(new PieEntry(coreGroupCount, "Core"));

                                } else {
                                    coregroup = true;


                                }
                                if (current && previous && old && upcome && coregroup) {

                                    txtNoChart.setVisibility(View.VISIBLE);
                                    Log.e(TAG, "chart not avaible: " + current + " ," + previous + " ," + old + " ," + upcome + " ," + coregroup);


                                }
                                current = false;
                                previous = false;
                                old = false;
                                upcome = false;
                                coregroup=false;

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
                                processBar.setVisibility(View.GONE);
                                OnItemClick = false;
                                llfreshnessIndex.setVisibility(View.VISIBLE);
                                Reusable_Functions.hDialog();
                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                            llfreshnessIndex.setVisibility(View.VISIBLE);
                            Log.e(TAG, "in catch: " + e.getMessage());
                            OnItemClick = false;

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
                        OnItemClick = false;

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

        String fIdetails = ConstsCore.web_url + "/v1/display/freshnessindexheader/" + userId + "?corefashion=" + FIndex_SegmentClick ;

        Log.e(TAG, "freshnessindexheader: "+fIdetails );

        postRequest = new JsonArrayRequest(Request.Method.GET, fIdetails,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, "onResponse:"+response);

                        int i;
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                OnItemClick = false;

                                llfreshnessIndex.setVisibility(View.GONE);

                                return;

                            } else {
                                freshnessIndexDetail = new FreshnessIndexDetails();
                                for (i = 0; i < response.length(); i++)
                                {
                                    freshnessIndexDetail = gson.fromJson(response.get(i).toString(), FreshnessIndexDetails.class);
                                }
                                if (txtFIndexClass.getText().toString().equals("Department")) {
                                    freshnessIndexDetail.setPlanDept("All");
                                } else if (txtFIndexClass.getText().toString().equals("Subdept")) {
                                    freshnessIndexDetail.setPlanCategory("All");
                                } else if (txtFIndexClass.getText().toString().equals("Class")) {
                                    freshnessIndexDetail.setPlanClass("All");
                                } else if (txtFIndexClass.getText().toString().equals("Subclass")) {
                                    freshnessIndexDetail.setBrandName("All");
                                } else if (txtFIndexClass.getText().toString().equals("MC")) {
                                    freshnessIndexDetail.setBrandplanClass("All");
                                }

                                freshnessIndexDetail.setStkOnhandQty(freshnessIndexDetail.getStkOnhandQty());
                                freshnessIndexDetail.setStkOnhandQtyCount(100);
                                freshnessIndexDetail.setStkGitQty(freshnessIndexDetail.getStkGitQty());
                                freshnessIndexDetail.setCoreGrpCount(freshnessIndexDetail.getCoreGrpCount());
                                Log.e(TAG, "getCoreGroupCount: "+freshnessIndexDetail.getCoreGrpCount() );

                                //getCurrentGroupCount is not avaible in Api

                                freshnessIndexDetail.setUpcomingGroupCount(freshnessIndexDetail.getUpcomingGrpCount());
                                freshnessIndexDetail.setOldGroupCount(freshnessIndexDetail.getOldGrpCount());
                                freshnessIndexDetail.setPreviousGroupCount(freshnessIndexDetail.getPreviousGrpCount());
                                freshnessIndexDetail.setSohCurrentGrpCount(freshnessIndexDetail.getSohCurrentGrpCount());

                            }

                            freshnessIndexDetailsArrayList.add(0, freshnessIndexDetail);

                            setAdapter();

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "Data failed...", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "onResponse: catch"+e.getMessage() );
                            llfreshnessIndex.setVisibility(View.GONE);
                            OnItemClick = false;


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
                        OnItemClick = false;

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

    private void setAdapter() {

        listViewFIndex.setLayoutManager(new LinearLayoutManager(context));

        listViewFIndex.setLayoutManager(new LinearLayoutManager(
                listViewFIndex.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
        listViewFIndex.setOnFlingListener(null);
        new GravitySnapHelper(48).attachToRecyclerView(listViewFIndex);

        freshnessIndexSnapAdapter = new FreshnessIndexSnapAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex);
        // fIndexAdapter = new FreshnessIndexAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex);
        listViewFIndex.setAdapter(freshnessIndexSnapAdapter);
        Log.e(TAG, " freshnessIndexDetailsArrayList size is " + freshnessIndexDetailsArrayList.size());
        //fIndexAdapter.notifyDataSetChanged();
        txtStoreCode.setText(freshnessIndexDetailsArrayList.get(0).getStoreCode());
        txtStoreDesc.setText(freshnessIndexDetailsArrayList.get(0).getStoreDescription());

        if (txtFIndexClass.getText().toString().equals("Department")) {
            level = 1;
            fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getPlanDept().toString();
            for (int j = 0; j < freshnessIndexDetailsArrayList.size(); j++) {
                if (freshnessIndexDetailsArrayList.get(j).getPlanDept().equals(fIndexFirstVisibleItem)) {

                    LinearLayoutManager llm = (LinearLayoutManager) listViewFIndex.getLayoutManager();
                    llm.scrollToPosition(firstVisibleItem);
                }
            }
        } else if (txtFIndexClass.getText().toString().equals("Subdept")) {
            level = 2;
            fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getPlanCategory().toString();
            for (int j = 0; j < freshnessIndexDetailsArrayList.size(); j++) {
                if (freshnessIndexDetailsArrayList.get(j).getPlanCategory().contentEquals(fIndexFirstVisibleItem)) {
                    LinearLayoutManager llm = (LinearLayoutManager) listViewFIndex.getLayoutManager();
                    llm.scrollToPosition(firstVisibleItem);
                 //   fIndexFirstVisibleItem = fIndexFirstVisibleItem;

                }
            }

        } else if (txtFIndexClass.getText().toString().equals("Class")) {
            level = 3;
            fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getPlanClass().toString();
            for (int j = 0; j < freshnessIndexDetailsArrayList.size(); j++) {
                if (freshnessIndexDetailsArrayList.get(j).getPlanClass().contentEquals(fIndexFirstVisibleItem)) {
                    LinearLayoutManager llm = (LinearLayoutManager) listViewFIndex.getLayoutManager();
                    llm.scrollToPosition(firstVisibleItem);
                 //   fIndexFirstVisibleItem = fIndexFirstVisibleItem;
                }
            }
        } else if (txtFIndexClass.getText().toString().equals("Subclass")) {
            level = 4;
            fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getBrandName().toString();
            for (int j = 0; j < freshnessIndexDetailsArrayList.size(); j++) {
                if (freshnessIndexDetailsArrayList.get(j).getBrandName().contentEquals(fIndexFirstVisibleItem)) {
                    LinearLayoutManager llm = (LinearLayoutManager) listViewFIndex.getLayoutManager();
                    llm.scrollToPosition(firstVisibleItem);
                   // fIndexFirstVisibleItem = fIndexFirstVisibleItem;

                }
            }

        } else if (txtFIndexClass.getText().toString().equals("MC")) {
            level = 5;
            fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getBrandplanClass().toString();
            for (int j = 0; j < freshnessIndexDetailsArrayList.size(); j++) {
                if (freshnessIndexDetailsArrayList.get(j).getBrandplanClass().contentEquals(fIndexFirstVisibleItem)) {
                    LinearLayoutManager llm = (LinearLayoutManager) listViewFIndex.getLayoutManager();
                    llm.scrollToPosition(firstVisibleItem);
                 //   fIndexFirstVisibleItem = fIndexFirstVisibleItem;

                }
            }
        }
        offsetvalue = 0;
        limit = 100;
        count = 0;
        llfIndexhierarchy.setVisibility(View.GONE);
        requestFIndexPieChart();
    }

    private void requestFreshnessIndexFilterVal(final String selectedString) {

        String freshnessindex_filterVal_listurl = ConstsCore.web_url + "/v1/display/freshnessindexdetail/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + SalesFilterActivity.level_filter + selectedString + "&offset=" + offsetvalue + "&limit=" + limit;

        Log.e(TAG, "request_FreshnessIndex_FilterVal: " + freshnessindex_filterVal_listurl);
        postRequest = new JsonArrayRequest(Request.Method.GET, freshnessindex_filterVal_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(SalesFilterActivity.level_filter == 2)
                        {
                            txtFIndexClass.setText("Subdept");
                            fromWhere = "Subdept";
                            btnFIndexPrev.setVisibility(View.VISIBLE);

                        }
                        else if(SalesFilterActivity.level_filter == 3)
                        {
                            txtFIndexClass.setText("Class");
                            fromWhere = "Class";
                            btnFIndexPrev.setVisibility(View.VISIBLE);


                        }
                        else if(SalesFilterActivity.level_filter == 4)
                        {
                            txtFIndexClass.setText("Subclass");
                            fromWhere = "Subclass";
                            btnFIndexPrev.setVisibility(View.VISIBLE);



                        }
                        else if(SalesFilterActivity.level_filter == 5)
                        {
                            txtFIndexClass.setText("MC");
                            fromWhere = "MC";
                            btnFIndexPrev.setVisibility(View.VISIBLE);
                            btnFIndexNext.setVisibility(View.INVISIBLE);


                        }
                        else if(SalesFilterActivity.level_filter == 6)
                        {
                            txtFIndexClass.setText("MC");
                            fromWhere = "MC";
                            btnFIndexPrev.setVisibility(View.VISIBLE);
                            btnFIndexNext.setVisibility(View.INVISIBLE);

                        }


                        int i;
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found...", Toast.LENGTH_SHORT).show();
                                processBar.setVisibility(View.GONE);
                                OnItemClick = false;


                            } else if (response.length() == limit) {
                                for (i = 0; i < response.length(); i++) {

                                    freshnessIndexDetails = gson.fromJson(response.get(i).toString(), FreshnessIndexDetails.class);
                                    freshnessIndexDetailsArrayList.add(freshnessIndexDetails);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestFreshnessIndexFilterVal(selectedString);

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
                                listViewFIndex.setAdapter(freshnessIndexSnapAdapter);


                                txtStoreCode.setText(freshnessIndexDetailsArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(freshnessIndexDetailsArrayList.get(0).getStoreDescription());
//                                offsetvalue = 0;
//                                limit = 100;
//                                count = 0;
//                                fIndexArrayList.clear();

                                if (txtFIndexClass.getText().toString().equals("Department")) {

                                    level =1 ;
                                    fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getPlanDept().toString();

                                } else if (txtFIndexClass.getText().toString().equals("Subdept")) {
                                    level = 2;
                                    fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getPlanCategory().toString();

                                } else if (txtFIndexClass.getText().toString().equals("Class")) {
                                    level = 3;
                                    fIndexFirstVisibleItem  = freshnessIndexDetailsArrayList.get(firstVisibleItem).getPlanClass().toString();

                                } else if (txtFIndexClass.getText().toString().equals("Subclass")) {
                                    level = 4;
                                    fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getBrandName().toString();

                                } else if (txtFIndexClass.getText().toString().equals("MC")) {
                                    level = 5;
                                    fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getBrandplanClass().toString();
                                }

                                requestFIndexPieChart();

                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, " data failed..." + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "in catch: " + e.getMessage());
                            processBar.setVisibility(View.GONE);
                            OnItemClick = false;

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "Server not found...", Toast.LENGTH_SHORT).show();
                        processBar.setVisibility(View.GONE);
                        OnItemClick = false;

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

        FIndex_SegmentClick = null;
        level = 0;
        FIndex_SegmentClick = "Fashion";
        level = 1;
        this.finish();
    }


}

