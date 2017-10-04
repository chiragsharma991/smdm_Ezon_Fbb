package apsupportapp.aperotechnologies.com.designapp.FreshnessIndex;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
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
import apsupportapp.aperotechnologies.com.designapp.Httpcall.ApiRequest;
import apsupportapp.aperotechnologies.com.designapp.Httpcall.HttpResponse;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesAnalysisFilter;
import apsupportapp.aperotechnologies.com.designapp.SeasonCatalogue.mpm_model;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.RecyclerItemClickListener;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.RunningPromo.RecyclerViewPositionHelper;
import apsupportapp.aperotechnologies.com.designapp.model.FreshnessIndex_Ez_Model;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by pamrutkar on 22/11/16.
 */

public class FreshnessIndexActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, HttpResponse, TabLayout.OnTabSelectedListener {

    RadioButton btnCore, btnFashion;
    private static String FIndex_SegmentClick = "Fashion";
    ArrayList<FreshnessIndexDetails> freshnessIndexDetailsArrayList, fIndexArrayList;
    ArrayList<mpm_model> freshnessIndexDetails_Ez_ArrayList;
    TextView txtStoreCode, txtStoreDesc, txtFIndexClass, txtfIndexDeptName, txtNoChart;
    String userId, bearertoken, geoLeveLDesc,storeDescription;
    SharedPreferences sharedPreferences;
    int offsetvalue = 0, limit = 100;
    int count = 0;
    RequestQueue queue;
    String FreshnessIndexValue="";   //set value in hierarchy
    Context context;
    String fromWhere, freshnessIndex_ClickedVal, fIndexPlanDept, fIndexCategory, fIndexPlanClass, fIndexBrand;   // selected value from list
    PieChart pieChart;
    public static RecyclerView listViewFIndex;
    int selFirstPositionValue;
    LinearLayout llfreshnessIndex, llfIndexhierarchy;
    SegmentedGroup segmented3;
    private static int level = 1;
    FreshnessIndexDetails freshnessIndexDetails;
    public static FreshnessIndexDetails freshnessIndexDetail;
    public FreshnessIndex_Ez_Model freshnessIndex_Ez_Model;
    RelativeLayout freshnessIndex_imageBtnBack, freshnessIndex_imgfilter, FreshnessIndex_Ez_moreVertical;
    RelativeLayout btnFIndexPrev, btnFIndexNext;  //small arrow key to change department.
    Gson gson;
    FreshnessIndexSnapAdapter freshnessIndexSnapAdapter;
    PieData pieData;
    float upcoming = 0.0f, oldgroup = 0.0f, previousgroup = 0.0f, currentgroup = 0.0f, coreGroupCount = 0.0f, non_assortment_count = 0.0f, assortment_count = 0.0f;
    PieDataSet dataSet;
    private String TAG;
    private boolean current = false, previous = false, old = false, upcome = false, coregroup = false;
    private int totalItemCount = 0;  // this is total item present in listview
    int firstVisibleItem = 0;  // this is highlight position in listview.
    JsonArrayRequest postRequest;
    private ProgressBar processBar;
    int prevState = RecyclerView.SCROLL_STATE_IDLE;
    int currentState = RecyclerView.SCROLL_STATE_IDLE;
    private String fIndexFirstVisibleItem="All";    // This is for list name from listview
    private boolean OnItemClick = false, filter_toggleClick = false;
    private int OveridePositionValue = 0;
    public static Activity freshness_Index;
    private PopupWindow popupWindow;
    private static int preValue=1, postValue;  //this is for radio button
    private RadioButton product_radiobtn, location_radiobtn;
    private boolean from_filter;
    private String selectedString, geoLevel2Code, lobId, isMultiStore, value;
    private int selectedlevel;
    private TabLayout Tabview;
    private int filter_level;
    private String header_value;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLeveLDesc = sharedPreferences.getString("geoLeveLDesc", "");
        context = this;
        header_value = "";
        if(getIntent().getExtras() != null)
        {
            if(getIntent().getExtras().getString("selectedStringVal") != null)
            {
                header_value = getIntent().getExtras().getString("selectedStringVal");
            }
            else
            {
                header_value = "";
            }
        }
        else
        {
            header_value = "";
        }


//        storeDescription = sharedPreferences.getString("storeDescription","");
        geoLevel2Code = sharedPreferences.getString("concept","");
        lobId = sharedPreferences.getString("lobid","");
        isMultiStore = sharedPreferences.getString("isMultiStore","");
        value = sharedPreferences.getString("value","");
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();

        // login define: ezone/ Fbb..

        if (geoLeveLDesc.equals("E ZONE")) {
            setContentView(R.layout.activity_ezone_freshness_index);
            getSupportActionBar().hide();
            TAG = "FreshnessIndex_Ez_Activity";
            context = this;
            common_intializeUI();
            intializeUIofEzon();
            Ezon_collection();  // start method for ezon collection

        } else {
            setContentView(R.layout.activity_freshness_index);
            getSupportActionBar().hide();
            TAG = "FreshnessIndexActivity";
            common_intializeUI();
            Fbb_collection();  // start Fbb collection.

        }


        listViewFIndex.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
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
                if (prevState != RecyclerView.SCROLL_STATE_IDLE && currentState == RecyclerView.SCROLL_STATE_IDLE) {
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        public void run() {
                            if (!OnItemClick)
                            {

                                if (TAG.equals("FreshnessIndex_Ez_Activity"))
                                {
                                    TimeUpEzone();
                                }
                                else
                                {
                                    TimeUP();
                                }
                            }
                        }
                    }, 700);
                }
                prevState = currentState;
            }
        });


        freshnessIndex_imageBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    private void TestItem() {
        if (txtFIndexClass.getText().toString().equals("Department")) {
            level = 1;
            fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(0).getPlanDept().toString();
        } else if (txtFIndexClass.getText().toString().equals("Category")) {
            level = 2;
            fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(0).getPlanCategory().toString();

        } else if (txtFIndexClass.getText().toString().equals("Class")) {
            level = 3;
            fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(0).getPlanClass().toString();

        } else if (txtFIndexClass.getText().toString().equals("Brand")) {
            level = 4;
            fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(0).getBrandName().toString();

        } else if (txtFIndexClass.getText().toString().equals("Brand Class")) {
            level = 5;
            fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(0).getBrandplanClass().toString();

        }
    }

    private void TimeUP()
    {
        if (freshnessIndexDetailsArrayList.size() != 0)
        {
            if (firstVisibleItem < freshnessIndexDetailsArrayList.size() && !OnItemClick) {
                //10<10 where footer is call then it goes else condition
                if (txtFIndexClass.getText().toString().equals("Department")) {
                    level = 1;
                    fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getPlanDept().toString();
                } else if (txtFIndexClass.getText().toString().equals("Category")) {
                    level = 2;
                    fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getPlanCategory().toString();
                } else if (txtFIndexClass.getText().toString().equals("Class")) {
                    level = 3;
                    fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getPlanClass().toString();
                } else if (txtFIndexClass.getText().toString().equals("Brand")) {
                    level = 4;
                    fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getBrandName().toString();
                } else if (txtFIndexClass.getText().toString().equals("Brand Class")) {
                    level = 5;
                    fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getBrandplanClass().toString();
                }
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
                        requestFIndexPieChart("");
                        OveridePositionValue = firstVisibleItem;
                    }
                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {

                firstVisibleItem = freshnessIndexDetailsArrayList.size() - 1;
                LinearLayoutManager llm = (LinearLayoutManager) listViewFIndex.getLayoutManager();
                llm.scrollToPosition(firstVisibleItem);

                if (txtFIndexClass.getText().toString().equals("Department")) {
                    level = 1;
                    fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getPlanDept().toString();
                } else if (txtFIndexClass.getText().toString().equals("Category")) {
                    level = 2;
                    fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getPlanCategory().toString();
                } else if (txtFIndexClass.getText().toString().equals("Class")) {
                    level = 3;
                    fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getPlanClass().toString();
                } else if (txtFIndexClass.getText().toString().equals("Brand")) {
                    level = 4;
                    fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getBrandName().toString();
                } else if (txtFIndexClass.getText().toString().equals("Brand Class")) {
                    level = 5;
                    fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getBrandplanClass().toString();
                }
                if (Reusable_Functions.chkStatus(context))
                {
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
                        requestFIndexPieChart("");
                        OveridePositionValue = firstVisibleItem;
                    }
                }
                else
                {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void initializeUI()
    {
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
//        String code = storeDescription.substring(0,4);
//
//        txtStoreDesc.setText(storeDescription.substring(5));
        processBar = (ProgressBar) findViewById(R.id.progressBar);
        pieChart = (PieChart) findViewById(R.id.fIndex_pieChart);
        txtNoChart = (TextView) findViewById(R.id.noChart);
        llfreshnessIndex = (LinearLayout) findViewById(R.id.llfreshnessIndex);
        btnFIndexNext = (RelativeLayout) findViewById(R.id.btnFIndexNext);
        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
        fIndexArrayList = new ArrayList<FreshnessIndexDetails>();
        Tabview = (TabLayout) findViewById(R.id.tabview);
        Tabview.addTab(Tabview.newTab().setText("Fashion"));
        Tabview.addTab(Tabview.newTab().setText("All"));
        Tabview.setOnTabSelectedListener(FreshnessIndexActivity.this);
    }

    public void retainValuesFilter()
    {
        //   filter_toggleClick = true;
        if (FIndex_SegmentClick.equals("All")) {
            //btnCore.toggle();
            Tabview.getTabAt(1).select();

        } else {
            //btnFashion.toggle();
            Tabview.getTabAt(0).select();

        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {

    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Log.e(TAG,"toggle is "+filter_toggleClick);
        int checkedId= Tabview.getSelectedTabPosition();
        OnItemClick = true;
        FreshnessIndexValue = "";
        if (!filter_toggleClick) {
            switch (checkedId) {

                case 1 :   //core selection
                    if (FIndex_SegmentClick.equals("All"))
                        break;
                    FIndex_SegmentClick = "All";
                    freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
                    if (Reusable_Functions.chkStatus(context)) {
                        if (postRequest != null) {
                            postRequest.cancel();
                        }
                        processBar.setVisibility(View.VISIBLE);
                        offsetvalue = 0;
                        limit = 100;
                        count = 0;
                        //   if (getIntent().getStringExtra("selectedDept") == null) {
                        requestFreshnessIndexDetails();
                      /*  } else if (getIntent().getStringExtra("selectedDept") != null) {
                            String selectedString = getIntent().getStringExtra("selectedDept");
                            requestFreshnessIndexFilterVal(selectedString);

                        }*/

                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        processBar.setVisibility(View.GONE);

                    }
                    break;

                case 0 :  // fashion selection

                    if (FIndex_SegmentClick.equals("Fashion"))
                        break;

                    FIndex_SegmentClick = "Fashion";
                    freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
                    if (Reusable_Functions.chkStatus(context)) {
                        if (postRequest != null) {
                            postRequest.cancel();

                        }
                        processBar.setVisibility(View.VISIBLE);
                        offsetvalue = 0;
                        limit = 100;
                        count = 0;
                        //   if (getIntent().getStringExtra("selectedDept") == null) {
                        requestFreshnessIndexDetails();
                        /*   } else if (getIntent().getStringExtra("selectedDept") != null) {
                            String selectedString = getIntent().getStringExtra("selectedDept");
                            requestFreshnessIndexFilterVal(selectedString);

                        }
                        */
                    }
                    else
                    {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        processBar.setVisibility(View.GONE);

                    }

                    break;

                default:
                    break;

            }
        } else {
            filter_toggleClick = false;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    //----------------------------API Declaration---------------------------//
    // API 1.31
    private void requestFreshnessIndexDetails()
    {
        String fIdetails = "";
        fIdetails = ConstsCore.web_url + "/v1/display/freshnessindexdetailNew/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit +"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
        Log.e(TAG, "requestFreshnessIndexDetails: "+ fIdetails);
        postRequest = new JsonArrayRequest(Request.Method.GET, fIdetails,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Reusable_Functions.hDialog();
                        int i;
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                OnItemClick = false;
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
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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


    // For Category List on click of Dept Value
    private void request_FreshnessIndex_CategoryList(final String deptName)
    {
        String freshnessindex_category_listurl = ConstsCore.web_url + "/v1/display/freshnessindexdetailNew/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&department=" + deptName.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit +"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
        Log.e(TAG, "request_FreshnessIndex_CategoryList: "+ freshnessindex_category_listurl);

        postRequest = new JsonArrayRequest(Request.Method.GET, freshnessindex_category_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Reusable_Functions.hDialog();

                        int i;
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Toast.makeText(context, "No Category data found", Toast.LENGTH_SHORT).show();
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
//                                freshnessIndexDetails = new FreshnessIndexDetails();
//                                if(txtFIndexClass.getText().toString().equals("Category"))
//                                {
//                                    freshnessIndexDetails.setPlanCategory("All");
//                                }

                                requestHeader(deptName, 2);
                                /*freshnessIndexDetails.setStkOnhandQty(freshnessIndexDetail.getStkOnhandQty());
                                freshnessIndexDetails.setStkOnhandQtyCount(100);
                                freshnessIndexDetails.setStkGitQty(freshnessIndexDetail.getStkGitQty());

                                freshnessIndexDetailsArrayList.add(0,freshnessIndexDetails);
                                listViewFIndex.setLayoutManager(new LinearLayoutManager(context));
                                listViewFIndex.setLayoutManager(new LinearLayoutManager(
                                        listViewFIndex.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                listViewFIndex.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(listViewFIndex);
                                freshnessIndexSnapAdapter = new FreshnessIndexSnapAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex, TAG);
                                listViewFIndex.setAdapter(freshnessIndexSnapAdapter);
                                TestItem();


                                Log.e(TAG, "txtfIndexDeptName: "+deptName+" and FreshnessIndex"+FreshnessIndexValue);
                                txtfIndexDeptName.setText(hierarchy(deptName));
                                llfIndexhierarchy.setVisibility(View.VISIBLE);
                                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(0).getPlanCategory().toString();
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                level = 2;
                                requestFIndexPieChart();*/

                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "No Category data found", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, "No Category data found", Toast.LENGTH_SHORT).show();
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
        freshnessIndex_planclass_listurl = ConstsCore.web_url + "/v1/display/freshnessindexdetailNew/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&category=" + category.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit +"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
        Log.e(TAG, "request_FreshnessIndex_PlanClassList: "+ freshnessIndex_planclass_listurl);
        postRequest = new JsonArrayRequest(Request.Method.GET, freshnessIndex_planclass_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Reusable_Functions.hDialog();

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Toast.makeText(context, "No Class data found", Toast.LENGTH_SHORT).show();
                                processBar.setVisibility(View.GONE);
                                OnItemClick = false;


                            } else if (response.length() == limit)
                            {
                                for (int i = 0; i < response.length(); i++) {
                                    freshnessIndexDetails = gson.fromJson(response.get(i).toString(), FreshnessIndexDetails.class);
                                    freshnessIndexDetailsArrayList.add(freshnessIndexDetails);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                request_FreshnessIndex_PlanClassList(deptName, category);

                            }
                            else if (response.length() < limit)
                            {
                                for (int i = 0; i < response.length(); i++) {
                                    freshnessIndexDetails = gson.fromJson(response.get(i).toString(), FreshnessIndexDetails.class);
                                    freshnessIndexDetailsArrayList.add(freshnessIndexDetails);
                                }
                                requestHeader(category, 3);
                                /*freshnessIndexDetails = new FreshnessIndexDetails();
                                if(txtFIndexClass.getText().toString().equals("Class"))
                                {
                                    freshnessIndexDetails.setPlanClass("All");
                                }
                                freshnessIndexDetails.setStkOnhandQty(freshnessIndexDetail.getStkOnhandQty());
                                freshnessIndexDetails.setStkOnhandQtyCount(100);
                                freshnessIndexDetails.setStkGitQty(freshnessIndexDetail.getStkGitQty());

                                freshnessIndexDetailsArrayList.add(0,freshnessIndexDetails);

                                listViewFIndex.setLayoutManager(new LinearLayoutManager(context));
                                listViewFIndex.setLayoutManager(new LinearLayoutManager(
                                        listViewFIndex.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                listViewFIndex.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(listViewFIndex);
                                freshnessIndexSnapAdapter = new FreshnessIndexSnapAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex, TAG);
                                listViewFIndex.setAdapter(freshnessIndexSnapAdapter);

                                freshnessIndexSnapAdapter.notifyDataSetChanged();

                                // FreshnessIndexValue += " > " + category;
                                //  txtfIndexDeptName.setText(FreshnessIndexValue);
                                txtfIndexDeptName.setText(hierarchy(category));
                                llfIndexhierarchy.setVisibility(View.VISIBLE);
//                                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(0).getPlanClass().toString();
                                TestItem();
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                level = 3;
                                requestFIndexPieChart();*/
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
        freshnessIndex_brand_listurl = ConstsCore.web_url + "/v1/display/freshnessindexdetailNew/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&class=" + planclass.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit +"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
        Log.e(TAG, "request_FreshnessIndex_BrandList: "+ freshnessIndex_brand_listurl);
        postRequest = new JsonArrayRequest(Request.Method.GET, freshnessIndex_brand_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Reusable_Functions.hDialog();

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Toast.makeText(context, "No Brand data found", Toast.LENGTH_SHORT).show();
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
                                requestHeader(planclass, 4);
                                /*freshnessIndexDetails = new FreshnessIndexDetails();
                                if(txtFIndexClass.getText().toString().equals("Brand"))
                                {
                                    freshnessIndexDetails.setBrandName("All");
                                }
                                freshnessIndexDetails.setStkOnhandQty(freshnessIndexDetail.getStkOnhandQty());
                                freshnessIndexDetails.setStkOnhandQtyCount(100);
                                freshnessIndexDetails.setStkGitQty(freshnessIndexDetail.getStkGitQty());

                                freshnessIndexDetailsArrayList.add(0,freshnessIndexDetails);
                                listViewFIndex.setLayoutManager(new LinearLayoutManager(context));
                                listViewFIndex.setLayoutManager(new LinearLayoutManager(
                                        listViewFIndex.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                listViewFIndex.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(listViewFIndex);
                                freshnessIndexSnapAdapter = new FreshnessIndexSnapAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex, TAG);
                                listViewFIndex.setAdapter(freshnessIndexSnapAdapter);
                                TestItem();
                                freshnessIndexSnapAdapter.notifyDataSetChanged();

                                // FreshnessIndexValue += " > " + planclass;
                                // txtfIndexDeptName.setText(FreshnessIndexValue);
                                txtfIndexDeptName.setText(hierarchy(planclass));
                                llfIndexhierarchy.setVisibility(View.VISIBLE);
                                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(0).getBrandName().toString();
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                level = 4;
                                requestFIndexPieChart();*/

                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "No Brand data found", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, "No Brand data found", Toast.LENGTH_SHORT).show();
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
        freshnessIndex_brandplan_listurl = ConstsCore.web_url + "/v1/display/freshnessindexdetailNew/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&brand=" + brandnm.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit +"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
        Log.e(TAG, "request_FreshnessIndex_BrandPlanList: "+ freshnessIndex_brandplan_listurl);
        postRequest = new JsonArrayRequest(Request.Method.GET, freshnessIndex_brandplan_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Reusable_Functions.hDialog();

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Toast.makeText(context, "No Brand Class data found", Toast.LENGTH_SHORT).show();
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
                                requestHeader(brandnm, 4);
                                /*freshnessIndexDetails = new FreshnessIndexDetails();
                                if(txtFIndexClass.getText().toString().equals("Brand Class"))
                                {
                                    freshnessIndexDetails.setBrandplanClass("All");
                                }
                                freshnessIndexDetails.setStkOnhandQty(freshnessIndexDetail.getStkOnhandQty());
                                freshnessIndexDetails.setStkOnhandQtyCount(100);
                                freshnessIndexDetails.setStkGitQty(freshnessIndexDetail.getStkGitQty());

                                freshnessIndexDetailsArrayList.add(0,freshnessIndexDetails);
                                listViewFIndex.setLayoutManager(new LinearLayoutManager(context));
                                listViewFIndex.setLayoutManager(new LinearLayoutManager(
                                        listViewFIndex.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                listViewFIndex.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(listViewFIndex);

                                freshnessIndexSnapAdapter = new FreshnessIndexSnapAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex, TAG);
                                listViewFIndex.setAdapter(freshnessIndexSnapAdapter);
                                TestItem();
                                freshnessIndexSnapAdapter.notifyDataSetChanged();

                                // FreshnessIndexValue += " > " + brandnm;
                                //  txtfIndexDeptName.setText(FreshnessIndexValue);
                                txtfIndexDeptName.setText(hierarchy(brandnm));
                                llfIndexhierarchy.setVisibility(View.VISIBLE);
                                fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(0).getBrandplanClass().toString();
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                level = 5;
                                requestFIndexPieChart();*/
                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "No Brand Class data found", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, "No Brand Class data found", Toast.LENGTH_SHORT).show();
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
    private void requestFIndexPieChart(String header_value_filter) {
        offsetvalue = 0;
        limit = 100;
        count = 0;
        String url = " ";
        txtNoChart.setVisibility(View.GONE);
        fIndexArrayList = new ArrayList<FreshnessIndexDetails>();

        Log.e("header_value_filter "," "+header_value_filter+" "+fIndexFirstVisibleItem);

        if (fIndexFirstVisibleItem.equals("All"))
        {
            ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
            upcoming = (float) freshnessIndexDetail.getUpcomingGroupCount();
            oldgroup = (float) freshnessIndexDetail.getOldGroupCount();
            previousgroup = (float) freshnessIndexDetail.getPreviousGroupCount();
            currentgroup = (float) freshnessIndexDetail.getSohCurrentGrpCount();
            coreGroupCount = (float) freshnessIndexDetail.getCoreGrpCount();
            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(Color.parseColor("#20b5d3"));
            colors.add(Color.parseColor("#21d24c"));
            colors.add(Color.parseColor("#f5204c"));
            colors.add(Color.parseColor("#f89a20"));
            colors.add(Color.parseColor("#78bc2c"));
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
            }
            current = false;
            previous = false;
            old = false;
            upcome = false;
            coregroup = false;
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
        fIndexFirstVisibleItem = fIndexFirstVisibleItem.replace("%", "%25");
        fIndexFirstVisibleItem = fIndexFirstVisibleItem.replace(" ", "%20").replace("&", "%26");

        if(!header_value_filter.equals(""))
        {
            if (txtFIndexClass.getText().toString().equals("Department")) {
                url = ConstsCore.web_url + "/v1/display/freshnessindexdetailNew/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&department=" + fIndexFirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit +"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId+""+header_value_filter;
            } else if (txtFIndexClass.getText().toString().equals("Category")) {
                url = ConstsCore.web_url + "/v1/display/freshnessindexdetailNew/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&category=" + fIndexFirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit +"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId+""+header_value_filter;
            } else if (txtFIndexClass.getText().toString().equals("Class")) {
                url = ConstsCore.web_url + "/v1/display/freshnessindexdetailNew/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&class=" + fIndexFirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit +"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId+""+header_value_filter;
            } else if (txtFIndexClass.getText().toString().equals("Brand")) {
                url = ConstsCore.web_url + "/v1/display/freshnessindexdetailNew/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&brand=" + fIndexFirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit +"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId+""+header_value_filter;
            } else if (txtFIndexClass.getText().toString().equals("Brand Class")) {
                url = ConstsCore.web_url + "/v1/display/freshnessindexdetailNew/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&brandclass=" + fIndexFirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit +"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId+""+ header_value_filter;
            }
        }
        else
        {
            if (txtFIndexClass.getText().toString().equals("Department")) {
                url = ConstsCore.web_url + "/v1/display/freshnessindexdetailNew/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&department=" + fIndexFirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit +"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
            } else if (txtFIndexClass.getText().toString().equals("Category")) {
                url = ConstsCore.web_url + "/v1/display/freshnessindexdetailNew/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&category=" + fIndexFirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit +"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
            } else if (txtFIndexClass.getText().toString().equals("Class")) {
                url = ConstsCore.web_url + "/v1/display/freshnessindexdetailNew/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&class=" + fIndexFirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit +"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
            } else if (txtFIndexClass.getText().toString().equals("Brand")) {
                url = ConstsCore.web_url + "/v1/display/freshnessindexdetailNew/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&brand=" + fIndexFirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit +"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
            } else if (txtFIndexClass.getText().toString().equals("Brand Class")) {
                url = ConstsCore.web_url + "/v1/display/freshnessindexdetailNew/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&brandclass=" + fIndexFirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit +"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
            }
        }


        Log.e("requestFIndexPieChart ","  URL: "+ url);

        postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Reusable_Functions.hDialog();
                        Log.e("response requestFIndexPieChart "," "+response);

                        try {
                            int i;
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
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
                                requestFIndexPieChart("");
                            } else if (response.length() < limit) {
                                for (i = 0; i < response.length(); i++) {
                                    freshnessIndexDetails = gson.fromJson(response.get(i).toString(), FreshnessIndexDetails.class);
                                    fIndexArrayList.add(freshnessIndexDetails);
                                }
                                ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
                                fIndexFirstVisibleItem = fIndexFirstVisibleItem.replace("%25", "%");
                                fIndexFirstVisibleItem = fIndexFirstVisibleItem.replace("%20", " ").replace("%26", "&");
                                for (FreshnessIndexDetails fresh : fIndexArrayList) {
                                    if (fIndexFirstVisibleItem.equals("All")) {
                                        upcoming = (float) fresh.getUpcomingGroupCount();
                                        oldgroup = (float) fresh.getOldGroupCount();
                                        previousgroup = (float) fresh.getPreviousGroupCount();
                                        currentgroup = (float) fresh.getCurrentGroupCount();
                                        coreGroupCount = (float) fresh.getCoreGroupCount();
                                    } else if (fIndexFirstVisibleItem.equals(fresh.getPlanDept())) {
                                        upcoming = (float) fresh.getUpcomingGroupCount();
                                        oldgroup = (float) fresh.getOldGroupCount();
                                        previousgroup = (float) fresh.getPreviousGroupCount();
                                        currentgroup = (float) fresh.getCurrentGroupCount();
                                        coreGroupCount = (float) fresh.getCoreGroupCount();
                                    } else if (fIndexFirstVisibleItem.equals(fresh.getPlanCategory())) {
                                        upcoming = (float) fresh.getUpcomingGroupCount();
                                        oldgroup = (float) fresh.getOldGroupCount();
                                        previousgroup = (float) fresh.getPreviousGroupCount();
                                        currentgroup = (float) fresh.getCurrentGroupCount();
                                        coreGroupCount = (float) fresh.getCoreGroupCount();
                                    } else if (fIndexFirstVisibleItem.equals(fresh.getPlanClass())) {
                                        upcoming = (float) fresh.getUpcomingGroupCount();
                                        oldgroup = (float) fresh.getOldGroupCount();
                                        previousgroup = (float) fresh.getPreviousGroupCount();
                                        currentgroup = (float) fresh.getCurrentGroupCount();
                                        coreGroupCount = (float) fresh.getCoreGroupCount();
                                    } else if (fIndexFirstVisibleItem.equals(fresh.getBrandName())) {
                                        upcoming = (float) fresh.getUpcomingGroupCount();
                                        oldgroup = (float) fresh.getOldGroupCount();
                                        previousgroup = (float) fresh.getPreviousGroupCount();
                                        currentgroup = (float) fresh.getCurrentGroupCount();
                                        coreGroupCount = (float) fresh.getCoreGroupCount();
                                    } else if (fIndexFirstVisibleItem.equals(fresh.getBrandplanClass())) {
                                        upcoming = (float) fresh.getUpcomingGroupCount();
                                        oldgroup = (float) fresh.getOldGroupCount();
                                        previousgroup = (float) fresh.getPreviousGroupCount();
                                        currentgroup = (float) fresh.getCurrentGroupCount();
                                        coreGroupCount = (float) fresh.getCoreGroupCount();
                                    }
                                }
                                ArrayList<Integer> colors = new ArrayList<>();
                                colors.add(Color.parseColor("#20b5d3"));
                                colors.add(Color.parseColor("#21d24c"));
                                colors.add(Color.parseColor("#f5204c"));
                                colors.add(Color.parseColor("#f89a20"));
                                colors.add(Color.parseColor("#78bc2c"));
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
                                }
                                current = false;
                                previous = false;
                                old = false;
                                upcome = false;
                                coregroup = false;
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
            if (value < 0.0) return "";
            else return mFormat.format(value) + " %";
        }
    }


    private void requestAll() {
        String fIdetails;

        fIdetails = ConstsCore.web_url + "/v1/display/freshnessindexheaderNew/" + userId + "?corefashion=" + FIndex_SegmentClick + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;


        Log.e(TAG, "requestAll URL: "+ fIdetails);

        postRequest = new JsonArrayRequest(Request.Method.GET, fIdetails,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, "onResponse: All"+response );
                        Reusable_Functions.hDialog();


                        int i;
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                OnItemClick = false;
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
                                } else if (txtFIndexClass.getText().toString().equals("Class")) {
                                    freshnessIndexDetail.setPlanClass("All");
                                } else if (txtFIndexClass.getText().toString().equals("Brand")) {
                                    freshnessIndexDetail.setBrandName("All");
                                } else if (txtFIndexClass.getText().toString().equals("Brand Class")) {
                                    freshnessIndexDetail.setBrandplanClass("All");
                                }

                                freshnessIndexDetail.setStkOnhandQty(freshnessIndexDetail.getStkOnhandQty());
                                freshnessIndexDetail.setStkOnhandQtyCount(100);
                                freshnessIndexDetail.setStkGitQty(freshnessIndexDetail.getStkGitQty());
                                freshnessIndexDetail.setCoreGrpCount(freshnessIndexDetail.getCoreGrpCount());
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
                            Log.e(TAG,"Data failed."+e.getMessage());
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


    private void requestHeader(final String name, final int level) {
        String fIdetails;

        fIdetails = ConstsCore.web_url + "/v1/display/freshnessindexheaderNew/" + userId + "?corefashion=" + FIndex_SegmentClick + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId+""+header_value;


        Log.e(TAG, "requestHeader URL: "+ fIdetails);

        postRequest = new JsonArrayRequest(Request.Method.GET, fIdetails,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, "requestHeader onResponse: All"+response );
                        Reusable_Functions.hDialog();


                        int i;
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                OnItemClick = false;
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
                                } else if (txtFIndexClass.getText().toString().equals("Class")) {
                                    freshnessIndexDetail.setPlanClass("All");
                                } else if (txtFIndexClass.getText().toString().equals("Brand")) {
                                    freshnessIndexDetail.setBrandName("All");
                                } else if (txtFIndexClass.getText().toString().equals("Brand Class")) {
                                    freshnessIndexDetail.setBrandplanClass("All");
                                }

                                freshnessIndexDetail.setStkOnhandQty(freshnessIndexDetail.getStkOnhandQty());
                                freshnessIndexDetail.setStkOnhandQtyCount(100);
                                freshnessIndexDetail.setStkGitQty(freshnessIndexDetail.getStkGitQty());
                                freshnessIndexDetail.setCoreGrpCount(freshnessIndexDetail.getCoreGrpCount());
                                freshnessIndexDetail.setUpcomingGroupCount(freshnessIndexDetail.getUpcomingGrpCount());
                                freshnessIndexDetail.setOldGroupCount(freshnessIndexDetail.getOldGrpCount());
                                freshnessIndexDetail.setPreviousGroupCount(freshnessIndexDetail.getPreviousGrpCount());
                                freshnessIndexDetail.setSohCurrentGrpCount(freshnessIndexDetail.getSohCurrentGrpCount());
                            }

                            freshnessIndexDetailsArrayList.add(0, freshnessIndexDetail);
                            listViewFIndex.setLayoutManager(new LinearLayoutManager(context));
                            listViewFIndex.setLayoutManager(new LinearLayoutManager(
                                    listViewFIndex.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                    LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                            listViewFIndex.setOnFlingListener(null);
                            new GravitySnapHelper(48).attachToRecyclerView(listViewFIndex);
                            freshnessIndexSnapAdapter = new FreshnessIndexSnapAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex, TAG);
                            listViewFIndex.setAdapter(freshnessIndexSnapAdapter);

                            txtfIndexDeptName.setText(hierarchy(name.replaceAll("%20"," ").replaceAll("%26","&")));
                            llfIndexhierarchy.setVisibility(View.VISIBLE);
                            TestItem();
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            FreshnessIndexActivity.level = level;
                            requestFIndexPieChart("");

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "Data failed...", Toast.LENGTH_SHORT).show();
                            Log.e(TAG,"Data failed."+e.getMessage());
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

    private void requestFilterHeader() {
        String fIdetails = ConstsCore.web_url + "/v1/display/freshnessindexheaderNew/" + userId + "?corefashion=" + FIndex_SegmentClick + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId+""+header_value;

        Log.e(TAG, "requestFilterHeader URL: "+ fIdetails);

        postRequest = new JsonArrayRequest(Request.Method.GET, fIdetails,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, "requestHeader onResponse: All"+response );
                        Reusable_Functions.hDialog();


                        int i;
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                OnItemClick = false;
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
                                } else if (txtFIndexClass.getText().toString().equals("Class")) {
                                    freshnessIndexDetail.setPlanClass("All");
                                } else if (txtFIndexClass.getText().toString().equals("Brand")) {
                                    freshnessIndexDetail.setBrandName("All");
                                } else if (txtFIndexClass.getText().toString().equals("Brand Class")) {
                                    freshnessIndexDetail.setBrandplanClass("All");
                                }

                                freshnessIndexDetail.setStkOnhandQty(freshnessIndexDetail.getStkOnhandQty());
                                freshnessIndexDetail.setStkOnhandQtyCount(100);
                                freshnessIndexDetail.setStkGitQty(freshnessIndexDetail.getStkGitQty());
                                freshnessIndexDetail.setCoreGrpCount(freshnessIndexDetail.getCoreGrpCount());
                                freshnessIndexDetail.setUpcomingGroupCount(freshnessIndexDetail.getUpcomingGrpCount());
                                freshnessIndexDetail.setOldGroupCount(freshnessIndexDetail.getOldGrpCount());
                                freshnessIndexDetail.setPreviousGroupCount(freshnessIndexDetail.getPreviousGrpCount());
                                freshnessIndexDetail.setSohCurrentGrpCount(freshnessIndexDetail.getSohCurrentGrpCount());

                                freshnessIndexDetailsArrayList.add(0,freshnessIndexDetail);

                                listViewFIndex.setLayoutManager(new LinearLayoutManager(context));
                                listViewFIndex.setLayoutManager(new LinearLayoutManager(
                                        listViewFIndex.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                listViewFIndex.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(listViewFIndex);
                                freshnessIndexSnapAdapter = new FreshnessIndexSnapAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex, TAG);
                                listViewFIndex.setAdapter(freshnessIndexSnapAdapter);

                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                fIndexArrayList.clear();
                                TestItem();

                                requestFIndexPieChart(header_value);


                            }



                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "Data failed...", Toast.LENGTH_SHORT).show();
                            Log.e(TAG,"Data failed."+e.getMessage());
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
        freshnessIndexSnapAdapter = new FreshnessIndexSnapAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex, TAG);
        listViewFIndex.setAdapter(freshnessIndexSnapAdapter);

        if (txtFIndexClass.getText().toString().equals("Department")) {
            level = 1;
            fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getPlanDept().toString();
            for (int j = 0; j < freshnessIndexDetailsArrayList.size(); j++) {
                if (freshnessIndexDetailsArrayList.get(j).getPlanDept().contentEquals(fIndexFirstVisibleItem)) {
                    LinearLayoutManager llm = (LinearLayoutManager) listViewFIndex.getLayoutManager();
                    llm.scrollToPosition(firstVisibleItem);
                }
            }
        } else if (txtFIndexClass.getText().toString().equals("Category")) {
            level = 2;
            fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getPlanCategory().toString();
            for (int j = 0; j < freshnessIndexDetailsArrayList.size(); j++) {
                if (freshnessIndexDetailsArrayList.get(j).getPlanCategory().contentEquals(fIndexFirstVisibleItem)) {
                    LinearLayoutManager llm = (LinearLayoutManager) listViewFIndex.getLayoutManager();
                    llm.scrollToPosition(firstVisibleItem);
                    llm.scrollToPosition(firstVisibleItem);
                }
            }

        } else if (txtFIndexClass.getText().toString().equals("Class")) {
            level = 3;
            fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getPlanClass().toString();
            for (int j = 0; j < freshnessIndexDetailsArrayList.size(); j++) {
                if (freshnessIndexDetailsArrayList.get(j).getPlanClass().contentEquals(fIndexFirstVisibleItem)) {
                    LinearLayoutManager llm = (LinearLayoutManager) listViewFIndex.getLayoutManager();
                    llm.scrollToPosition(firstVisibleItem);
                    llm.scrollToPosition(firstVisibleItem);
                }
            }
        } else if (txtFIndexClass.getText().toString().equals("Brand")) {
            level = 4;
            fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getBrandName().toString();
            for (int j = 0; j < freshnessIndexDetailsArrayList.size(); j++) {
                if (freshnessIndexDetailsArrayList.get(j).getBrandName().contentEquals(fIndexFirstVisibleItem)) {
                    LinearLayoutManager llm = (LinearLayoutManager) listViewFIndex.getLayoutManager();
                    llm.scrollToPosition(firstVisibleItem);
                    llm.scrollToPosition(firstVisibleItem);
                }
            }

        } else if (txtFIndexClass.getText().toString().equals("Brand Class")) {
            level = 5;
            fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(firstVisibleItem).getBrandplanClass().toString();
            for (int j = 0; j < freshnessIndexDetailsArrayList.size(); j++) {
                if (freshnessIndexDetailsArrayList.get(j).getBrandplanClass().contentEquals(fIndexFirstVisibleItem)) {
                    LinearLayoutManager llm = (LinearLayoutManager) listViewFIndex.getLayoutManager();
                    llm.scrollToPosition(firstVisibleItem);
                    llm.scrollToPosition(firstVisibleItem);
                }
            }
        }
        offsetvalue = 0;
        limit = 100;
        count = 0;
        llfIndexhierarchy.setVisibility(View.GONE);
        requestFIndexPieChart("");
    }

    private void requestFreshnessIndexFilterVal(final String selectedString,final int inv_filter_level) {
        String freshnessindex_filterVal_listurl = "";
        if(inv_filter_level != 0)
        {
            freshnessindex_filterVal_listurl = ConstsCore.web_url + "/v1/display/freshnessindexdetailNew/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + inv_filter_level + selectedString + "&offset=" + offsetvalue + "&limit=" + limit +"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
        }
        else
        {
            freshnessindex_filterVal_listurl = ConstsCore.web_url + "/v1/display/freshnessindexdetailNew/" + userId + "?corefashion=" + FIndex_SegmentClick + selectedString + "&offset=" + offsetvalue + "&limit=" + limit +"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
        }
        Log.e(TAG, "requestFreshnessIndexFilterVal: "+freshnessindex_filterVal_listurl );
        postRequest = new JsonArrayRequest(Request.Method.GET, freshnessindex_filterVal_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Reusable_Functions.hDialog();
                        Log.e(TAG, "requestFreshnessIndexFilterVal: response "+response );

                        if (inv_filter_level == 2)
                        {
                            txtFIndexClass.setText("Category");
                            fromWhere = "Category";
                            level=2;
                            btnFIndexPrev.setVisibility(View.VISIBLE);

                        } else if (inv_filter_level == 3)
                        {
                            txtFIndexClass.setText("Class");
                            fromWhere = "Class";
                            level=3;
                            btnFIndexPrev.setVisibility(View.VISIBLE);
                        } else if (inv_filter_level == 4)
                        {
                            txtFIndexClass.setText("Brand");
                            fromWhere = "Brand";
                            level=4;
                            btnFIndexPrev.setVisibility(View.VISIBLE);
                        }
                        else if (inv_filter_level == 5)
                        {
                            txtFIndexClass.setText("Brand Class");
                            fromWhere = "Brand Class";
                            level=5;
                            btnFIndexPrev.setVisibility(View.VISIBLE);
                            btnFIndexNext.setVisibility(View.INVISIBLE);
                        }
                        else if (inv_filter_level== 5)
                        {
                            txtFIndexClass.setText("Brand Class");
                            fromWhere = "Brand Class";
                            level=6;
                            btnFIndexPrev.setVisibility(View.VISIBLE);
                            btnFIndexNext.setVisibility(View.INVISIBLE);
                        }
                        int i;
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                fIndexFirstVisibleItem="All";
                                processBar.setVisibility(View.GONE);
                                OnItemClick = false;

                            } else if (response.length() == limit) {
                                for (i = 0; i < response.length(); i++) {

                                    freshnessIndexDetails = gson.fromJson(response.get(i).toString(), FreshnessIndexDetails.class);
                                    freshnessIndexDetailsArrayList.add(freshnessIndexDetails);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestFreshnessIndexFilterVal(selectedString, inv_filter_level);

                            } else if (response.length() < limit) {
                                for (i = 0; i < response.length(); i++) {

                                    freshnessIndexDetails = gson.fromJson(response.get(i).toString(), FreshnessIndexDetails.class);
                                    freshnessIndexDetailsArrayList.add(freshnessIndexDetails);
                                }
                                requestFilterHeader();
                                /*freshnessIndexDetails = new FreshnessIndexDetails();
                                if (txtFIndexClass.getText().toString().equals("Department")) {
                                    freshnessIndexDetails.setPlanDept("All");
                                } else if (txtFIndexClass.getText().toString().equals("Category")) {
                                    freshnessIndexDetails.setPlanCategory("All");
                                } else if (txtFIndexClass.getText().toString().equals("Class")) {
                                    freshnessIndexDetails.setPlanClass("All");
                                } else if (txtFIndexClass.getText().toString().equals("Brand")) {
                                    freshnessIndexDetails.setBrandName("All");
                                } else if (txtFIndexClass.getText().toString().equals("Brand Class")) {
                                    freshnessIndexDetails.setBrandplanClass("All");
                                }
                                freshnessIndexDetails.setStkOnhandQty(freshnessIndexDetail.getStkOnhandQty());
                                freshnessIndexDetails.setStkOnhandQtyCount(100);
                                freshnessIndexDetails.setStkGitQty(freshnessIndexDetail.getStkGitQty());

                                freshnessIndexDetailsArrayList.add(0,freshnessIndexDetails);

                                listViewFIndex.setLayoutManager(new LinearLayoutManager(context));
                                listViewFIndex.setLayoutManager(new LinearLayoutManager(
                                        listViewFIndex.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                listViewFIndex.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(listViewFIndex);
                                freshnessIndexSnapAdapter = new FreshnessIndexSnapAdapter(freshnessIndexDetailsArrayList, context, fromWhere, listViewFIndex, TAG);
                                listViewFIndex.setAdapter(freshnessIndexSnapAdapter);

                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                fIndexArrayList.clear();
                                if (txtFIndexClass.getText().toString().equals("Department")) {
                                    level = 1;
                                    fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(0).getPlanDept().toString();
                                } else if (txtFIndexClass.getText().toString().equals("Category")) {
                                    level = 2;
                                    fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(0).getPlanCategory().toString();

                                } else if (txtFIndexClass.getText().toString().equals("Class")) {
                                    level = 3;
                                    fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(0).getPlanClass().toString();

                                } else if (txtFIndexClass.getText().toString().equals("Brand")) {
                                    level = 4;
                                    fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(0).getBrandName().toString();

                                } else if (txtFIndexClass.getText().toString().equals("Brand Class")) {
                                    level = 5;
                                    fIndexFirstVisibleItem = freshnessIndexDetailsArrayList.get(0).getBrandplanClass().toString();
                                }

                                requestFIndexPieChart();*/

                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, " data failed..." + e.getMessage(), Toast.LENGTH_SHORT).show();
                            processBar.setVisibility(View.GONE);
                            OnItemClick = false;

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
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


    // Ezon Area...>>>>>>>>

    private void intializeUIofEzon() {
        freshnessIndex_ClickedVal = "All";
        freshnessIndexDetails_Ez_ArrayList = new ArrayList<>();
        llfIndexhierarchy.setVisibility(View.GONE);
        FreshnessIndex_Ez_moreVertical = (RelativeLayout) findViewById(R.id.freshnessIndex_Ez_moreVertical);
        FreshnessIndex_Ez_moreVertical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.showAsDropDown(view);
            }
        });


    }

    @Override
    public void response(ArrayList<mpm_model> list, int id) {
        switch (id) {

            case 0:
                freshnessIndexDetails_Ez_ArrayList.addAll(list);
                String url;
                if(from_filter){ url = ConstsCore.web_url + "/v1/display/inventoryassortmentnonassortmentheaderEZNew/" + userId + "?level=" + selectedlevel +"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;   from_filter=false;  }
                else{ url = ConstsCore.web_url + "/v1/display/inventoryassortmentnonassortmentheaderEZNew/" + userId + "?level=" + level +"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;  }//header api
                //  Log.e(TAG, "Freshness_Ez: Header URL " + url);
                // Reusable_Functions.hDialog();
                mpm_model model = new mpm_model();
                ApiRequest api_request = new ApiRequest(context, bearertoken, url, TAG, queue, model, 1);
                break;

            case 1:
                freshnessIndexDetails_Ez_ArrayList.addAll(0, list);
                freshnessIndexDetails_Ez_ArrayList.get(0).setLevel("All");
                freshnessIndexDetails_Ez_ArrayList.get(0).setStkOnhandQtyCont(100);
                Log.e(TAG, "After add header size is: " + freshnessIndexDetails_Ez_ArrayList.size() + "and value" + freshnessIndexDetails_Ez_ArrayList.get(0).getLevel());
                setAdapterForEz(freshnessIndexDetails_Ez_ArrayList);
                break;

        }


    }

    @Override
    public void nodatafound() {
/**
 you can write here cause of no data found
 */
    }

    private void setAdapterForEz(ArrayList<mpm_model> freshnessIndexDetails_Ez_ArrayList)
    {
        listViewFIndex.setLayoutManager(new LinearLayoutManager(context));
        listViewFIndex.setLayoutManager(new LinearLayoutManager(
                listViewFIndex.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
        listViewFIndex.setOnFlingListener(null);
        new GravitySnapHelper(48).attachToRecyclerView(listViewFIndex);
        freshnessIndexSnapAdapter = new FreshnessIndexSnapAdapter(freshnessIndexDetails_Ez_ArrayList, context, listViewFIndex, TAG);
        listViewFIndex.setAdapter(freshnessIndexSnapAdapter);


        // Retail function when you drill down the list.
        for (int j = 0; j < freshnessIndexDetails_Ez_ArrayList.size(); j++) {
            if (freshnessIndexDetails_Ez_ArrayList.get(j).getLevel().contentEquals(freshnessIndex_ClickedVal)) {
                LinearLayoutManager llm = (LinearLayoutManager) listViewFIndex.getLayoutManager();
                llm.scrollToPosition(j);
                requestFIndex_Ez_PieChart(j);
                break;
            }
        }

        Reusable_Functions.hDialog();
    }


    private void TimeUpEzone() {

        if (freshnessIndexDetails_Ez_ArrayList.size() != 0) {
            if (firstVisibleItem < freshnessIndexDetails_Ez_ArrayList.size()) {

                //10<10 where footer is call then it goes else condition


                fIndexFirstVisibleItem = freshnessIndexDetails_Ez_ArrayList.get(firstVisibleItem).getLevel();
                Log.e(TAG, "TimeUpEzone: fIndexFirstVisibleItem" + fIndexFirstVisibleItem);

            } else {

                firstVisibleItem = freshnessIndexDetails_Ez_ArrayList.size() - 1;
                LinearLayoutManager llm = (LinearLayoutManager) listViewFIndex.getLayoutManager();
                llm.scrollToPosition(firstVisibleItem);

                fIndexFirstVisibleItem = freshnessIndexDetails_Ez_ArrayList.get(firstVisibleItem).getLevel();
                Log.e(TAG, "TimeUpEzone Else: fIndexFirstVisibleItem" + fIndexFirstVisibleItem);

            }
            requestFIndex_Ez_PieChart(firstVisibleItem);


        }

    }

    private void requestFIndex_Ez_PieChart(int position) {
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        non_assortment_count = (float) freshnessIndexDetails_Ez_ArrayList.get(position).getNonAssortmentGrpCont();
        assortment_count = (float) freshnessIndexDetails_Ez_ArrayList.get(position).getAssortmentGrpCont();
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#20b5d3"));
        colors.add(Color.parseColor("#21d24c"));
        entries.add(new PieEntry(non_assortment_count, "Non Asst"));
        entries.add(new PieEntry(assortment_count, "Asst"));
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
        if(preValue==1){   product_radiobtn.setChecked(true);} else{ location_radiobtn.setChecked(true);}

        location.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postValue = 2;
                from_filter=false;
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
                from_filter=false;
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

    public void sortFunction()
    {
        // post value changes according to click

        if (!(postValue == preValue)) {
            Log.e(TAG, "sortFunction: post value is" + postValue + " and prevalue" + preValue);

            if (postValue == 1)
            {
                Log.e(TAG, "sortFunction: true...");
                if (Reusable_Functions.chkStatus(context))
                {
                    listViewFIndex.setVisibility(View.VISIBLE);
                    llfIndexhierarchy.setVisibility(View.GONE);
                    FreshnessIndexValue="";
                    preValue = postValue;
                    txtFIndexClass.setText("Department");
                    freshnessIndex_ClickedVal = "All";
                    FreshnessIndexValue = "";
                    level = 1;
                    btnFIndexPrev.setVisibility(View.INVISIBLE);
                    btnFIndexNext.setVisibility(View.VISIBLE);
                    freshnessIndexDetails_Ez_ArrayList = new ArrayList<>();
                    mpm_model model = new mpm_model();
                    ApiCallBack(model, 0, "");

                }
                else
                {
                    product_radiobtn.setChecked(false);
                    location_radiobtn.setChecked(true);
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
            } else if (postValue == 2) {

                if (Reusable_Functions.chkStatus(context)) {
                    listViewFIndex.setVisibility(View.VISIBLE);
                    FreshnessIndexValue="";
                    llfIndexhierarchy.setVisibility(View.GONE);
                    preValue = postValue;
                    txtFIndexClass.setText("Region");
                    freshnessIndex_ClickedVal = "All";
                    FreshnessIndexValue = "";
                    level = 7;
                    btnFIndexPrev.setVisibility(View.INVISIBLE);
                    btnFIndexNext.setVisibility(View.VISIBLE);
                    freshnessIndexDetails_Ez_ArrayList = new ArrayList<>();
                    mpm_model model = new mpm_model();
                    ApiCallBack(model, 0, "");

                } else {
                    product_radiobtn.setChecked(true);
                    location_radiobtn.setChecked(false);
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }


    private void ApiCallBack(mpm_model model, int id, String deptName) {

        switch (id) {

            case 0:
                String url = ConstsCore.web_url + "/v1/display/inventoryassortmentnonassortmentlineEZNew/" + userId + "?level=" + level +"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;   //Detail Api
                Reusable_Functions.sDialog(context,"Loading...");
                // Log.e(TAG, "Freshness_Ez: Detail URL " + url);
                ApiRequest api_request = new ApiRequest(context, bearertoken, url, TAG, queue, model, 0);  // 0 is id for identification
                break;

            case 1:
                //String freshnessindex_category_listurl = ConstsCore.web_url + "/v1/display/freshnessindexdetail/" + userId + "?corefashion=" + FIndex_SegmentClick + "&level=" + level + "&department=" + deptName.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
                url = ConstsCore.web_url + "/v1/display/inventoryassortmentnonassortmentlineEZNew/" + userId + "?level=" + level + "&" + fromWhere + "=" + deptName.replaceAll(" ", "%20").replaceAll("&", "%26") +"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
                // Log.e(TAG, "Freshness_Ez: Detail URL " + url);
                Reusable_Functions.sDialog(context,"Loading...");
                api_request = new ApiRequest(context, bearertoken, url, TAG, queue, model, 0);
                break;

            case 2:
                url = ConstsCore.web_url + "/v1/display/inventoryassortmentnonassortmentlineEZNew/" + userId + "?level=" + selectedlevel + selectedString +"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
                // Log.e(TAG, "Freshness_Ez: Detail URL " + url);
                Reusable_Functions.sDialog(context,"Loading...");
                api_request = new ApiRequest(context, bearertoken, url, TAG, queue, model, 0);
                break;
        }
    }


    private void Ezon_collection() {
        Log.e(TAG, "Ezon_collection: log");


        if (Reusable_Functions.chkStatus(context)) {
            mpm_model model = new mpm_model();

            if (getIntent().getStringExtra("selectedStringVal") == null ) {
                from_filter = false;
                ApiCallBack(model, 0, "");
                Log.e(TAG, "checkfromFilter: null");

            } else if (getIntent().getStringExtra("selectedStringVal") != null) {
                selectedString = getIntent().getStringExtra("selectedStringVal");
                selectedlevel = getIntent().getIntExtra("selectedlevelVal",0);
                from_filter = true;
                setText(selectedlevel);
                ApiCallBack(model, 2, "");
                Log.e(TAG, "checkfromFilter: ok "+selectedlevel+" "+selectedString);
            }
            show_popup();

        }
        else
        {

            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }

        // Next arrow button.

        btnFIndexNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                freshnessIndex_ClickedVal = "All";
                llfIndexhierarchy.setVisibility(View.GONE);
                FreshnessIndexValue = "";
                listViewFIndex.setVisibility(View.VISIBLE);

                switch (txtFIndexClass.getText().toString()) {

                    case "Department":
                        level = 2;
                        btnFIndexPrev.setVisibility(View.VISIBLE);
                        txtFIndexClass.setText("Subdept");
                        fromWhere = "Subdept";
                        freshnessIndexDetails_Ez_ArrayList = new ArrayList<>();
                        if (Reusable_Functions.chkStatus(context)) {

                            mpm_model model = new mpm_model();
                            ApiCallBack(model, 0, "");

                        } else {
                            listViewFIndex.setVisibility(View.GONE);
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case "Subdept":
                        fromWhere = "Class";
                        txtFIndexClass.setText("Class");
                        level = 3;
                        freshnessIndexDetails_Ez_ArrayList = new ArrayList<>();
                        if (Reusable_Functions.chkStatus(context)) {

                            mpm_model model = new mpm_model();
                            ApiCallBack(model, 0, "");

                        } else {
                            listViewFIndex.setVisibility(View.GONE);
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case "Class":
                        txtFIndexClass.setText("Subclass");
                        fromWhere = "Subclass";
                        level = 4;
                        freshnessIndexDetails_Ez_ArrayList = new ArrayList<>();
                        if (Reusable_Functions.chkStatus(context)) {

                            mpm_model model = new mpm_model();
                            ApiCallBack(model, 0, "");

                        } else {
                            listViewFIndex.setVisibility(View.GONE);
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case "Subclass":
                        btnFIndexNext.setVisibility(View.INVISIBLE);
                        txtFIndexClass.setText("MC");
                        fromWhere = "MC";
                        level = 5;
                        freshnessIndexDetails_Ez_ArrayList = new ArrayList<>();
                        if (Reusable_Functions.chkStatus(context)) {

                            mpm_model model = new mpm_model();
                            ApiCallBack(model, 0, "");

                        } else {
                            listViewFIndex.setVisibility(View.GONE);
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case "Region":
                        level = 9;
                        btnFIndexPrev.setVisibility(View.VISIBLE);
                        btnFIndexNext.setVisibility(View.INVISIBLE);
                        txtFIndexClass.setText("Store");
                        freshnessIndexDetails_Ez_ArrayList = new ArrayList<>();
                        if (Reusable_Functions.chkStatus(context)) {

                            mpm_model model = new mpm_model();
                            ApiCallBack(model, 0, "");

                        } else {
                            listViewFIndex.setVisibility(View.GONE);
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });

        // previous arrow button

        btnFIndexPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                llfIndexhierarchy.setVisibility(View.GONE);
                FreshnessIndexValue = "";
                freshnessIndex_ClickedVal = "All";
                listViewFIndex.setVisibility(View.VISIBLE);
                String url = ConstsCore.web_url + "/v1/display/inventoryassortmentnonassortmentlineEZNew/" + userId + "?level=" + level +"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
                switch (txtFIndexClass.getText().toString())
                {
                    case "MC":
                        btnFIndexNext.setVisibility(View.VISIBLE);
                        txtFIndexClass.setText("Subclass");
                        fromWhere = "Subclass";
                        level = 4;
                        freshnessIndexDetails_Ez_ArrayList = new ArrayList<>();

                        if (Reusable_Functions.chkStatus(context)) {

                            mpm_model model = new mpm_model();
                            ApiCallBack(model, 0, "");

                        } else {
                            listViewFIndex.setVisibility(View.GONE);
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        break;


                    case "Subclass":
                        txtFIndexClass.setText("Class");
                        fromWhere = "Class";
                        level = 3;
                        freshnessIndexDetails_Ez_ArrayList = new ArrayList<>();
                        if (Reusable_Functions.chkStatus(context)) {

                            mpm_model model = new mpm_model();
                            ApiCallBack(model, 0, "");

                        } else {
                            listViewFIndex.setVisibility(View.GONE);
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }

                        break;


                    case "Class":
                        txtFIndexClass.setText("Subdept");
                        fromWhere = "Subdept";
                        level = 2;
                        freshnessIndexDetails_Ez_ArrayList = new ArrayList<>();
                        if (Reusable_Functions.chkStatus(context)) {

                            mpm_model model = new mpm_model();
                            ApiCallBack(model, 0, "");

                        } else {
                            listViewFIndex.setVisibility(View.GONE);
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case "Subdept":
                        btnFIndexPrev.setVisibility(View.INVISIBLE);
                        txtFIndexClass.setText("Department");
                        fromWhere = "Department";
                        level = 1;
                        freshnessIndexDetails_Ez_ArrayList = new ArrayList<>();
                        if (Reusable_Functions.chkStatus(context)) {

                            mpm_model model = new mpm_model();
                            ApiCallBack(model, 0, "");

                        } else {
                            listViewFIndex.setVisibility(View.GONE);
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case "Store":
                        btnFIndexNext.setVisibility(View.VISIBLE);
                        btnFIndexPrev.setVisibility(View.INVISIBLE);
                        txtFIndexClass.setText("Region");
                        level = 7;
                        freshnessIndexDetails_Ez_ArrayList = new ArrayList<>();

                        if (Reusable_Functions.chkStatus(context)) {

                            mpm_model model = new mpm_model();
                            ApiCallBack(model, 0, "");

                        } else {
                            listViewFIndex.setVisibility(View.GONE);
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        break;

                }
            }
        });

        // click listner on list view

        listViewFIndex.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.e(TAG, "addOnItemTouchListener: ");

                        // this position will block on footer click .
                        if (position < freshnessIndexDetails_Ez_ArrayList.size()) {
                            llfIndexhierarchy.setVisibility(View.VISIBLE);

                            switch (txtFIndexClass.getText().toString()) {

                                case "Department":
                                    btnFIndexPrev.setVisibility(View.VISIBLE);
                                    txtFIndexClass.setText("Subdept");
                                    fromWhere = "Subdept";
                                    freshnessIndex_ClickedVal = freshnessIndexDetails_Ez_ArrayList.get(position).getLevel();
                                    Log.e(TAG, "onItemClick: " + freshnessIndex_ClickedVal);
                                    level = 2;
                                    freshnessIndexDetails_Ez_ArrayList = new ArrayList<>();
                                    if (Reusable_Functions.chkStatus(context)) {

                                        txtfIndexDeptName.setText(hierarchy(freshnessIndex_ClickedVal));
                                        mpm_model model = new mpm_model();
                                        ApiCallBack(model, 1, freshnessIndex_ClickedVal);  //1 is for another id

                                    } else {

                                        listViewFIndex.setVisibility(View.GONE);
                                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                    }

                                    break;

                                case "Subdept":
                                    txtFIndexClass.setText("Class");
                                    freshnessIndex_ClickedVal = freshnessIndexDetails_Ez_ArrayList.get(position).getLevel();
                                    level = 3;
                                    fromWhere = "Class";

                                    freshnessIndexDetails_Ez_ArrayList = new ArrayList<>();
                                    if (Reusable_Functions.chkStatus(context)) {

                                        txtfIndexDeptName.setText(hierarchy(freshnessIndex_ClickedVal));
                                        mpm_model model = new mpm_model();
                                        ApiCallBack(model, 1, freshnessIndex_ClickedVal);  //1 is for another id

                                    }
                                    else
                                    {
                                        listViewFIndex.setVisibility(View.GONE);
                                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                    }

                                    break;

                                case "Class":
                                    txtFIndexClass.setText("Subclass");
                                    freshnessIndex_ClickedVal = freshnessIndexDetails_Ez_ArrayList.get(position).getLevel();
                                    level = 4;
                                    fromWhere = "Subclass";
                                    freshnessIndexDetails_Ez_ArrayList = new ArrayList<>();
                                    if (Reusable_Functions.chkStatus(context)) {

                                        txtfIndexDeptName.setText(hierarchy(freshnessIndex_ClickedVal));
                                        mpm_model model = new mpm_model();
                                        ApiCallBack(model, 1, freshnessIndex_ClickedVal);  //1 is for another id

                                    } else {
                                        listViewFIndex.setVisibility(View.GONE);
                                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                    }
                                    break;

                                case "Subclass":
                                    btnFIndexNext.setVisibility(View.INVISIBLE);
                                    txtFIndexClass.setText("MC");
                                    freshnessIndex_ClickedVal = freshnessIndexDetails_Ez_ArrayList.get(position).getLevel();
                                    level = 5;
                                    fromWhere = "MC";
                                    freshnessIndexDetails_Ez_ArrayList = new ArrayList<>();
                                    if (Reusable_Functions.chkStatus(context)) {

                                        txtfIndexDeptName.setText(hierarchy(freshnessIndex_ClickedVal));
                                        mpm_model model = new mpm_model();
                                        ApiCallBack(model, 1, freshnessIndex_ClickedVal);  //1 is for another id

                                    } else {
                                        listViewFIndex.setVisibility(View.GONE);
                                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                    }
                                    break;


                                case "Region":
                                    btnFIndexPrev.setVisibility(View.VISIBLE);
                                    btnFIndexNext.setVisibility(View.INVISIBLE);
                                    txtFIndexClass.setText("Store");
                                    freshnessIndex_ClickedVal = freshnessIndexDetails_Ez_ArrayList.get(position).getLevel();
                                    Log.e(TAG, "onItemClick: " + freshnessIndex_ClickedVal);
                                    level = 9;
                                    fromWhere = "Store";

                                    freshnessIndexDetails_Ez_ArrayList = new ArrayList<>();
                                    if (Reusable_Functions.chkStatus(context)) {

                                        txtfIndexDeptName.setText(hierarchy(freshnessIndex_ClickedVal));
                                        mpm_model model = new mpm_model();
                                        ApiCallBack(model, 1, freshnessIndex_ClickedVal);  //1 is for another id

                                    } else {
                                        listViewFIndex.setVisibility(View.GONE);
                                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                    }

                                    break;

                                default:
                                    Reusable_Functions.hDialog();
                                    Toast.makeText(context, " You are at the last level of hierarchy", Toast.LENGTH_SHORT).show();
                                    OnItemClick = false;
                                    break;

                            }}


                    }
                }));



    }

    public void setText(int filter_level){

        if (filter_level == 2)
        {
            txtFIndexClass.setText("Subdept");
            btnFIndexNext.setVisibility(View.VISIBLE);
            btnFIndexPrev.setVisibility(View.VISIBLE);
            preValue=1;
        }

        else if (filter_level == 3)
        {
            txtFIndexClass.setText("Class");
            btnFIndexNext.setVisibility(View.VISIBLE);
            btnFIndexPrev.setVisibility(View.VISIBLE);
            preValue=1;

        }
        else if (filter_level == 4)
        {
            txtFIndexClass.setText("Subclass");
            btnFIndexNext.setVisibility(View.VISIBLE);
            btnFIndexPrev.setVisibility(View.VISIBLE);
            preValue=1;

        }
        else if (filter_level == 5)
        {
            txtFIndexClass.setText("Brand Class");
            btnFIndexNext.setVisibility(View.INVISIBLE);
            btnFIndexPrev.setVisibility(View.VISIBLE);
            preValue=1;

        }
        else if (filter_level == 6)
        {
            txtFIndexClass.setText("MC");
            btnFIndexNext.setVisibility(View.INVISIBLE);
            btnFIndexPrev.setVisibility(View.VISIBLE);
            preValue=1;


        }
        else if(filter_level == 9)
        {
            txtFIndexClass.setText("Store");
            btnFIndexPrev.setVisibility(View.VISIBLE);
            btnFIndexNext.setVisibility(View.INVISIBLE);
            preValue=2;

        }

    }



    public String hierarchy(String freshnessIndex_ClickedVal){

        if(FreshnessIndexValue==null || FreshnessIndexValue.equals(""))
        {

            FreshnessIndexValue =  freshnessIndex_ClickedVal;

        }else{

            FreshnessIndexValue += " > " + freshnessIndex_ClickedVal;
        }


        return FreshnessIndexValue;

    }


    //Ezon area...<<<<<<<<<


    private void Fbb_collection() {

        Log.e(TAG, "Fbb_collection: log");
        fromWhere = "Department";
        fIndexFirstVisibleItem = "";
        freshnessIndex_ClickedVal = "";
        FreshnessIndexValue = "";
        level = 1;
        selFirstPositionValue = 0;
        initializeUI();

        if (Reusable_Functions.chkStatus(context))
        {
            Reusable_Functions.sDialog(context, "Loading data...");
            processBar.setVisibility(View.GONE);
            offsetvalue = 0;
            limit = 100;
            count = 0;
            level = 1;
            llfIndexhierarchy.setVisibility(View.GONE);

            if (getIntent().getStringExtra("selectedStringVal") == null) {
                filter_toggleClick = false;
                retainValuesFilter();
                requestFreshnessIndexDetails();
            } else if (getIntent().getStringExtra("selectedStringVal") != null) {
                String selectedString = getIntent().getStringExtra("selectedStringVal");
                filter_level = getIntent().getIntExtra("selectedlevelVal",0);

                filter_toggleClick = true;
                Log.e(TAG, "Selected values: "+selectedString );
                retainValuesFilter();
                requestFreshnessIndexFilterVal(selectedString,filter_level);

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
                if (processBar.getVisibility() == View.VISIBLE) {
                    return;
                }
                FreshnessIndexValue = "";
                header_value = "";
                switch (txtFIndexClass.getText().toString()) {

                    case "Brand Class":
                        btnFIndexNext.setVisibility(View.VISIBLE);
                        txtFIndexClass.setText("Brand");
                        fromWhere = "Brand";
                        level = 4;
                        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
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

                    case "Brand":
                        txtFIndexClass.setText("Class");
                        fromWhere = "Class";
                        level = 3;
                        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            processBar.setVisibility(View.GONE);
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
                        txtFIndexClass.setText("Category");
                        fromWhere = "Category";
                        level = 2;
                        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
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

                    case "Category":
                        btnFIndexPrev.setVisibility(View.INVISIBLE);
                        txtFIndexClass.setText("Department");
                        fromWhere = "Department";
                        level = 1;
                        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
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
                if (processBar.getVisibility() == View.VISIBLE) {
                    return;
                }
                FreshnessIndexValue = "";
                header_value = "";
                switch (txtFIndexClass.getText().toString()) {

                    case "Department":
                        btnFIndexPrev.setVisibility(View.VISIBLE);
                        txtFIndexClass.setText("Category");
                        fromWhere = "Category";
                        level = 2;
                        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
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

                    case "Category":
                        fromWhere = "Class";
                        txtFIndexClass.setText("Class");
                        level = 3;
                        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
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
                        txtFIndexClass.setText("Brand");
                        fromWhere = "Brand";
                        level = 4;
                        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
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

                    case "Brand":
                        btnFIndexNext.setVisibility(View.INVISIBLE);
                        txtFIndexClass.setText("Brand Class");
                        fromWhere = "Brand Class";
                        level = 5;
                        freshnessIndexDetailsArrayList = new ArrayList<FreshnessIndexDetails>();
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

        listViewFIndex.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {


                    @Override
                    public void onItemClick(View v, final int position) {
                        if (processBar.getVisibility() == View.VISIBLE) {
                            return;
                        } else {
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
                                                txtFIndexClass.setText("Category");
                                                freshnessIndex_ClickedVal = freshnessIndexDetailsArrayList.get(position).getPlanDept();
                                                fromWhere = "Category";
                                                level = 2;

                                                if(!freshnessIndex_ClickedVal.equals("All")) {
                                                    freshnessIndex_ClickedVal = freshnessIndex_ClickedVal.replace("%", "%25");
                                                    freshnessIndex_ClickedVal = freshnessIndex_ClickedVal.replace(" ", "%20").replace("&", "%26");
                                                    header_value = "&department=" + freshnessIndex_ClickedVal;
                                                }
                                                else
                                                {
                                                    header_value = "";
                                                }

                                                if (Reusable_Functions.chkStatus(context)) {
                                                    if (postRequest != null) {
                                                        postRequest.cancel();
                                                    }
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

                                            case "Category":
                                                txtFIndexClass.setText("Class");
                                                freshnessIndex_ClickedVal = freshnessIndexDetailsArrayList.get(position).getPlanCategory();
                                                fromWhere = "Class";
                                                level = 3;
                                                if(!freshnessIndex_ClickedVal.equals("All")) {
                                                    freshnessIndex_ClickedVal = freshnessIndex_ClickedVal.replace("%", "%25");
                                                    freshnessIndex_ClickedVal = freshnessIndex_ClickedVal.replace(" ", "%20").replace("&", "%26");
                                                    header_value = "&category=" + freshnessIndex_ClickedVal;
                                                }
                                                else
                                                {
                                                    header_value = "";
                                                }
                                                if (Reusable_Functions.chkStatus(context)) {
                                                    if (postRequest != null) {
                                                        postRequest.cancel();
                                                    }
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
                                                txtFIndexClass.setText("Brand");
                                                freshnessIndex_ClickedVal = freshnessIndexDetailsArrayList.get(position).getPlanClass();
                                                fromWhere = "Brand";
                                                level = 4;
                                                if(!freshnessIndex_ClickedVal.equals("All")) {
                                                    freshnessIndex_ClickedVal = freshnessIndex_ClickedVal.replace("%", "%25");
                                                    freshnessIndex_ClickedVal = freshnessIndex_ClickedVal.replace(" ", "%20").replace("&", "%26");
                                                    header_value = "&class=" + freshnessIndex_ClickedVal;
                                                }
                                                else
                                                {
                                                    header_value = "";
                                                }
                                                if (Reusable_Functions.chkStatus(context)) {
                                                    if (postRequest != null) {
                                                        postRequest.cancel();
                                                    }
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

                                            case "Brand":
                                                btnFIndexNext.setVisibility(View.INVISIBLE);
                                                txtFIndexClass.setText("Brand Class");
                                                freshnessIndex_ClickedVal = freshnessIndexDetailsArrayList.get(position).getBrandName();
                                                fromWhere = "Brand Class";
                                                level = 5;
                                                if(!freshnessIndex_ClickedVal.equals("All")) {
                                                    freshnessIndex_ClickedVal = freshnessIndex_ClickedVal.replace("%", "%25");
                                                    freshnessIndex_ClickedVal = freshnessIndex_ClickedVal.replace(" ", "%20").replace("&", "%26");
                                                    header_value = "&brand=" + freshnessIndex_ClickedVal;
                                                }
                                                else
                                                {
                                                    header_value = "";
                                                }
                                                if (Reusable_Functions.chkStatus(context)) {
                                                    if (postRequest != null) {
                                                        postRequest.cancel();
                                                    }
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
                                    }
                                }
                            }, 700);
                        }
                    }
                })
        );
    }

    // hierarchy level drill down on selected item click


    //list view on Scroll event


    private void common_intializeUI() {

        freshness_Index = this;
        processBar = (ProgressBar) findViewById(R.id.progressBar);
        txtfIndexDeptName = (TextView) findViewById(R.id.txtfIndexDeptName);
        llfIndexhierarchy = (LinearLayout) findViewById(R.id.llfIndexhierarchy);
        pieChart = (PieChart) findViewById(R.id.fIndex_pieChart);
        listViewFIndex = (RecyclerView) findViewById(R.id.listView_SalesAnalysis);
        btnFIndexPrev = (RelativeLayout) findViewById(R.id.btnFIndexPrev);
        btnFIndexPrev.setVisibility(View.INVISIBLE);
        btnFIndexNext = (RelativeLayout) findViewById(R.id.btnFIndexNext);
        txtFIndexClass = (TextView) findViewById(R.id.txtFIndexClass);
        freshnessIndex_imageBtnBack = (RelativeLayout) findViewById(R.id.freshnessIndex_imageBtnBack);
        freshnessIndex_imgfilter = (RelativeLayout) findViewById(R.id.freshnessIndex_imgfilter);


        freshnessIndex_imgfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(TAG.equals("FreshnessIndex_Ez_Activity")){
//                    Intent intent = new Intent(context, EzoneSalesFilter.class);
//                    intent.putExtra("checkfrom", "freshnessIndex");
//                    startActivity(intent);
//
//                }else{

                    Intent intent = new Intent(FreshnessIndexActivity.this, SalesAnalysisFilter.class);
                    intent.putExtra("checkfrom", "freshnessIndex");
                    startActivity(intent);
//                }

            }
        });
    }


    @Override
    public void onBackPressed() {

        if (TAG.equals("FreshnessIndex_Ez_Activity")) {

            level = 1;
            preValue=1;
            this.finish();

        } else {
            FIndex_SegmentClick = null;
            level = 0;
            FIndex_SegmentClick = "Fashion";
            level = 1;
            this.finish();
        }

    }
}
