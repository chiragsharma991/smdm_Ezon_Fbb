package apsupportapp.aperotechnologies.com.designapp.SalesAnalysis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
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
import apsupportapp.aperotechnologies.com.designapp.model.RecyclerItemClickListener;

import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisListDisplay;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisViewPagerValue;
import info.hoang8f.android.segmented.SegmentedGroup;


public class SalesAnalysisActivity1 extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    SegmentedGroup segmentedGroupSales;
    LinearLayout llayoutSalesAnalysis;
    SalesAnalysisListDisplay salesAnalysisClass;
    public static SalesAnalysisViewPagerValue salesAnalysis;
    ArrayList<SalesAnalysisViewPagerValue> analysisArrayList, salesList;
    ArrayList<SalesAnalysisListDisplay> salesAnalysisClassArrayList;
    ViewPager vwpagersales;
    LinearLayout lldots, llhierarchy;
    RelativeLayout relLayoutSales;
    RecyclerView listView_SalesAnalysis;
    SalesAnalysisSnapAdapter salesadapter;
    Context context;
    SalesPagerAdapter pageradapter;
    SharedPreferences sharedPreferences;
    String userId, bearertoken;
    EditText etListText;
    RelativeLayout btnBack;
    RadioButton btnWTD, btnL4W, btnLW, btnYTD;
    public static String selectedsegValue ;
    String saleFirstVisibleItem;
    String fromWhere = "Department";
    TextView txtStoreCode, txtStoreDesc;
    int offsetvalue = 0, limit = 100;
    int count = 0;
    public static int level = 1;
    RequestQueue queue;
    static String planDept, planCategory, planClass;
    Gson gson;
    RelativeLayout relimgrank, relimgfilter;
    RelativeLayout Style_loadingBar;
    RelativeLayout relprevbtn, relnextbtn, relimgclose;
    TextView txtheaderplanclass;
    TextView txtZonalSales, txtNationalSales;
    TextView txtZonalYOY, txtNationalYOY;
    TextView txthDeptName;
    int selFirstPositionValue = 0;
    String txtSalesClickedValue;
    String val;
    boolean flag = false, onClickFlag = false, filter_toggleClick = false;
    int currentVmPos;
    ProgressBar progressBar1;
    JsonArrayRequest postRequest;
    int firstVisibleItem ;
    public int totalItemCount;
    int prevState = RecyclerView.SCROLL_STATE_IDLE;
    int currentState = RecyclerView.SCROLL_STATE_IDLE;
    public static Activity SalesAnalysisActivity;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_analysis1);
        getSupportActionBar().hide();
        fromWhere = "Department";
        txtSalesClickedValue = " ";
        selectedsegValue = "WTD";
        val = "";
        context = this;
        SalesAnalysisActivity = this;
        firstVisibleItem = 0;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        etListText = (EditText) findViewById(R.id.etListText);
        Style_loadingBar = (RelativeLayout) findViewById(R.id.style_loadingBar);
        txtStoreCode = (TextView) findViewById(R.id.txtStoreCode);
        txtStoreDesc = (TextView) findViewById(R.id.txtStoreName);
        //hierarchy header
        txthDeptName = (TextView) findViewById(R.id.txthDeptName);
        btnBack = (RelativeLayout) findViewById(R.id.imageBtnBack);
        segmentedGroupSales = (SegmentedGroup) findViewById(R.id.segmentedGrp);
        segmentedGroupSales.setOnCheckedChangeListener(this);
        btnWTD = (RadioButton) findViewById(R.id.btnWTD);
        btnLW = (RadioButton) findViewById(R.id.btnLW);
        btnL4W = (RadioButton) findViewById(R.id.btnL4W);
        btnYTD = (RadioButton) findViewById(R.id.btnYTD);
        llayoutSalesAnalysis = (LinearLayout) findViewById(R.id.llayoutSalesAnalysis);
        relimgfilter = (RelativeLayout) findViewById(R.id.imgfilter);
        relimgrank = (RelativeLayout) findViewById(R.id.imgrank);
        relprevbtn = (RelativeLayout) findViewById(R.id.prevplanclass);
        relprevbtn.setVisibility(View.INVISIBLE);
        relnextbtn = (RelativeLayout) findViewById(R.id.nextplanclass);
        txtheaderplanclass = (TextView) findViewById(R.id.headerplanclass);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        txtZonalSales = (TextView) findViewById(R.id.txtZonalSales);
        txtNationalSales = (TextView) findViewById(R.id.txtNationalSales);

        txtheaderplanclass.setText("Department");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        relimgfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SalesAnalysisActivity1.this, SalesFilterActivity.class);
                intent.putExtra("checkfrom", "SalesAnalysis");
                startActivity(intent);

            }
        });
        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
        analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
        salesList = new ArrayList<SalesAnalysisViewPagerValue>();
        vwpagersales = (ViewPager) findViewById(R.id.viewpager);
        pageradapter = new SalesPagerAdapter();
        pageradapter.notifyDataSetChanged();
        lldots = (LinearLayout) findViewById(R.id.lldots);
        lldots.setOrientation(LinearLayout.HORIZONTAL);

        TabLayout tab=(TabLayout)findViewById(R.id.dotTab);
        tab.setupWithViewPager(vwpagersales, true);

        llhierarchy = (LinearLayout) findViewById(R.id.llhierarchy);
        llhierarchy.setOrientation(LinearLayout.HORIZONTAL);
        relLayoutSales = (RelativeLayout) findViewById(R.id.relTablelayout);

        listView_SalesAnalysis = (RecyclerView) findViewById(R.id.listView_SalesAnalysis);

        salesadapter = new SalesAnalysisSnapAdapter();

        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();

        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading...");
            progressBar1.setVisibility(View.GONE);
            llhierarchy.setVisibility(View.GONE);
            offsetvalue = 0;
            limit = 100;
            count = 0;
            level = 1;
            if (getIntent().getStringExtra("selectedDept") == null)
            {
                filter_toggleClick = false;
                retainSegmentValuesFilter();
                requestSalesListDisplayAPI();
            }
            else if (getIntent().getStringExtra("selectedDept") != null)
            {
                String selectedString = getIntent().getStringExtra("selectedDept");
                filter_toggleClick = true;
                retainSegmentValuesFilter();
                requestSalesSelectedFilterVal(selectedString);
            }
        } else
        {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
            progressBar1.setVisibility(View.GONE);
        }

        // Previous button click
        relprevbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postRequest != null)
                {
                    postRequest.cancel();
                }
                if(progressBar1.getVisibility() == View.VISIBLE)
                {
                    return;
                }
                else {
                    switch (txtheaderplanclass.getText().toString()) {

                        case "MC":
                            relnextbtn.setVisibility(View.VISIBLE);
                            if (lldots != null) {
                                lldots.removeAllViews();
                            }
                            currentVmPos = vwpagersales.getCurrentItem();
                            llhierarchy.setVisibility(View.GONE);
                            txtheaderplanclass.setText("Subclass");
                            fromWhere = "Subclass";
                            level = 4;
                            val = "";
                            salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                            if (Reusable_Functions.chkStatus(context)) {

                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                progressBar1.setVisibility(View.GONE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                requestSalesListDisplayAPI();
                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }
                            break;

                        case "Subclass":

                            if (lldots != null) {
                                lldots.removeAllViews();
                            }
                            currentVmPos = vwpagersales.getCurrentItem();
                            llhierarchy.setVisibility(View.GONE);
                            txtheaderplanclass.setText("Class");
                            fromWhere = "Class";
                            level = 3;
                            salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                            listView_SalesAnalysis.removeAllViews();
                            val = " ";
                            if (Reusable_Functions.chkStatus(context)) {
                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                progressBar1.setVisibility(View.GONE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                requestSalesListDisplayAPI();
                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }
                            break;

                        case "Class":
                            if (lldots != null) {
                                lldots.removeAllViews();
                            }
                            currentVmPos = vwpagersales.getCurrentItem();
                            llhierarchy.setVisibility(View.GONE);
                            txtheaderplanclass.setText("Subdept");
                            fromWhere = "Subdept";
                            level = 2;
                            val = " ";
                            salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                            listView_SalesAnalysis.removeAllViews();
                            if (Reusable_Functions.chkStatus(context)) {
                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                progressBar1.setVisibility(View.GONE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                requestSalesListDisplayAPI();
                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }
                            break;

                        case "Subdept":
                            relprevbtn.setVisibility(View.INVISIBLE);
                            if (lldots != null) {
                                lldots.removeAllViews();
                            }
                            currentVmPos = vwpagersales.getCurrentItem();
                            llhierarchy.setVisibility(View.GONE);
                            txtheaderplanclass.setText("Department");
                            fromWhere = "Department";
                            level = 1;
                            val = " ";
                            salesAnalysisClassArrayList.clear();
                            listView_SalesAnalysis.removeAllViews();
                            if (Reusable_Functions.chkStatus(context)) {
                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                progressBar1.setVisibility(View.GONE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                requestSalesListDisplayAPI();
                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        });

        // Next Button click
        relnextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postRequest != null) {
                    postRequest.cancel();
                }
                if(progressBar1.getVisibility() == View.VISIBLE)
                {
                    return;
                }
                else {
                    switch (txtheaderplanclass.getText().toString()) {
                        case "Department":
                            relprevbtn.setVisibility(View.VISIBLE);
                            txtheaderplanclass.setText("Subdept");
                            if (lldots != null) {
                                lldots.removeAllViews();
                            }
                            currentVmPos = vwpagersales.getCurrentItem();
                            llhierarchy.setVisibility(View.GONE);
                            fromWhere = "Subdept";
                            level = 2;
                            val = " ";
                            salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                            listView_SalesAnalysis.removeAllViews();
                            if (Reusable_Functions.chkStatus(context)) {
                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                progressBar1.setVisibility(View.GONE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                requestSalesListDisplayAPI();
                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }
                            break;

                        case "Subdept":
                            fromWhere = "Class";
                            txtheaderplanclass.setText("Class");
                            level = 3;
                            val = " ";
                            if (lldots != null) {
                                lldots.removeAllViews();
                            }
                            currentVmPos = vwpagersales.getCurrentItem();
                            llhierarchy.setVisibility(View.GONE);
                            salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                            listView_SalesAnalysis.removeAllViews();
                            if (Reusable_Functions.chkStatus(context)) {
                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                progressBar1.setVisibility(View.GONE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                requestSalesListDisplayAPI();
                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }
                            break;

                        case "Class":
                            txtheaderplanclass.setText("Subclass");
                            fromWhere = "Subclass";
                            level = 4;
                            val = " ";
                            if (lldots != null) {
                                lldots.removeAllViews();
                            }
                            currentVmPos = vwpagersales.getCurrentItem();
                            llhierarchy.setVisibility(View.GONE);
                            salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                            listView_SalesAnalysis.removeAllViews();
                            if (Reusable_Functions.chkStatus(context)) {
                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                progressBar1.setVisibility(View.GONE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                requestSalesListDisplayAPI();

                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }
                            break;

                        case "Subclass":
                            txtheaderplanclass.setText("MC");

                            relnextbtn.setVisibility(View.INVISIBLE);
                            if (lldots != null) {
                                lldots.removeAllViews();
                            }
                            currentVmPos = vwpagersales.getCurrentItem();
                            llhierarchy.setVisibility(View.GONE);
                            fromWhere = "MC";
                            level = 5;
                            val = " ";
                            salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                            listView_SalesAnalysis.removeAllViews();
                            if (Reusable_Functions.chkStatus(context)) {
                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                progressBar1.setVisibility(View.GONE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                requestSalesListDisplayAPI();
                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        });


        listView_SalesAnalysis.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                RecyclerViewPositionHelper mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
                totalItemCount = mRecyclerViewHelper.getItemCount();
                firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                currentState = newState;
                if (prevState != RecyclerView.SCROLL_STATE_IDLE && currentState == RecyclerView.SCROLL_STATE_IDLE && !onClickFlag) {

                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        public void run() {
                            if (!onClickFlag) {
                                TimeUP();
                            }
                        }
                    }, 700);
                }
                prevState = currentState;

            }
        });

        //Drill Down hierarchy
        listView_SalesAnalysis.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        if (progressBar1.getVisibility() == View.VISIBLE) {
                            return;
                        } else {
                            onClickFlag = true;
                            Handler h = new Handler();
                            h.postDelayed(new Runnable() {
                                public void run() {

                                    if (position < salesAnalysisClassArrayList.size()) {
                                        switch (txtheaderplanclass.getText().toString()) {

                                            case "Department":
                                                relprevbtn.setVisibility(View.VISIBLE);
                                                txtheaderplanclass.setText("Subdept");
                                                txtSalesClickedValue = salesAnalysisClassArrayList.get(position).getPlanDept();

                                                fromWhere = "Subdept";
                                                if (lldots != null) {
                                                    lldots.removeAllViews();
                                                }
                                                currentVmPos = vwpagersales.getCurrentItem();
                                                level = 2;
                                                if (Reusable_Functions.chkStatus(context)) {
                                                    if (postRequest != null) {
                                                        postRequest.cancel();
                                                    }
                                                    Reusable_Functions.hDialog();
                                                    Reusable_Functions.sDialog(context, "Loading data...");
                                                    progressBar1.setVisibility(View.GONE);
                                                    offsetvalue = 0;
                                                    limit = 100;
                                                    count = 0;
                                                    salesAnalysisClassArrayList.clear();
                                                    requestSalesCategoryList(txtSalesClickedValue);
                                                    planDept = txtSalesClickedValue;

                                                } else {
                                                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                                }
                                                break;

                                            case "Subdept":
                                                txtheaderplanclass.setText("Class");
                                                txtSalesClickedValue = salesAnalysisClassArrayList.get(position).getPlanCategory();
                                                fromWhere = "Class";
                                                if (lldots != null) {
                                                    lldots.removeAllViews();
                                                }
                                                currentVmPos = vwpagersales.getCurrentItem();
                                                level = 3;
                                                if (Reusable_Functions.chkStatus(context)) {
                                                    if (postRequest != null) {
                                                        postRequest.cancel();
                                                    }
                                                    Reusable_Functions.hDialog();
                                                    Reusable_Functions.sDialog(context, "Loading data...");
                                                    progressBar1.setVisibility(View.GONE);
                                                    offsetvalue = 0;
                                                    limit = 100;
                                                    count = 0;
                                                    salesAnalysisClassArrayList.clear();
                                                    requestSalesPlanClassListAPI(txtSalesClickedValue);
                                                    planCategory = txtSalesClickedValue;
                                                } else {
                                                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                                }

                                                break;

                                            case "Class":

                                                txtheaderplanclass.setText("Subclass");
                                                txtSalesClickedValue = salesAnalysisClassArrayList.get(position).getPlanClass();
                                                fromWhere = "Subclass";
                                                if (lldots != null) {
                                                    lldots.removeAllViews();
                                                }
                                                currentVmPos = vwpagersales.getCurrentItem();
                                                level = 4;
                                                if (Reusable_Functions.chkStatus(context)) {
                                                    if (postRequest != null) {
                                                        postRequest.cancel();
                                                    }
                                                    Reusable_Functions.hDialog();
                                                    Reusable_Functions.sDialog(context, "Loading data...");
                                                    progressBar1.setVisibility(View.GONE);
                                                    offsetvalue = 0;
                                                    limit = 100;
                                                    count = 0;
                                                    salesAnalysisClassArrayList.clear();
                                                    requestSalesBrandListAPI(txtSalesClickedValue);
                                                    planClass = txtSalesClickedValue;
                                                } else {
                                                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                                }

                                                break;

                                            case "Subclass":

                                                relnextbtn.setVisibility(View.INVISIBLE);
                                                txtheaderplanclass.setText("MC");
                                                txtSalesClickedValue = salesAnalysisClassArrayList.get(position).getBrandName();
                                                fromWhere = "MC";
                                                if (lldots != null) {
                                                    lldots.removeAllViews();
                                                }
                                                currentVmPos = vwpagersales.getCurrentItem();
                                                level = 5;
                                                if (Reusable_Functions.chkStatus(context)) {
                                                    if (postRequest != null) {
                                                        postRequest.cancel();
                                                    }
                                                    Reusable_Functions.hDialog();
                                                    Reusable_Functions.sDialog(context, "Loading data...");
                                                    progressBar1.setVisibility(View.GONE);
                                                    offsetvalue = 0;
                                                    limit = 100;
                                                    count = 0;
                                                    salesAnalysisClassArrayList.clear();
                                                    requestSalesBrandPlanListAPI(txtSalesClickedValue);
                                                } else {
                                                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                                }

                                                break;


                                            default:
                                                Reusable_Functions.hDialog();
                                                Toast.makeText(context, " You are at the last level of hierarchy", Toast.LENGTH_SHORT).show();
                                                onClickFlag = false;
                                                break;

                                        }

                                    }
                                }
                            }, 700);
                        }
                    }
                }));
    }
    // Retain values for segment click
    private void retainSegmentValuesFilter() {
        filter_toggleClick = true;
        switch (selectedsegValue) {
            case "WTD":
                btnWTD.toggle();


                break;
            case "LW":
                btnLW.toggle();

                break;
            case "L4W":
                btnL4W.toggle();

                break;
            case "STD":
                btnYTD.toggle();

                break;
        }

    }


    private void TimeUP()
    {
        if(salesAnalysisClassArrayList.size() != 0) {

            if (firstVisibleItem < salesAnalysisClassArrayList.size() && !onClickFlag) {

                if (txtheaderplanclass.getText().toString().equals("Department")) {
                    level = 1;
                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getPlanDept();
                } else if (txtheaderplanclass.getText().toString().equals("Subdept")) {
                    level = 2;
                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getPlanCategory();
                } else if (txtheaderplanclass.getText().toString().equals("Class")) {
                    level = 3;
                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getPlanClass();
                } else if (txtheaderplanclass.getText().toString().equals("Subclass")) {
                    level = 4;
                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getBrandName();
                } else if (txtheaderplanclass.getText().toString().equals("MC")) {
                    level = 5;
                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getBrandplanClass();
                }
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.hDialog();
                    currentVmPos = vwpagersales.getCurrentItem();
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();

                    if (firstVisibleItem != selFirstPositionValue) {
                        if (postRequest != null) {
                            postRequest.cancel();
                        }
                        progressBar1.setVisibility(View.VISIBLE);
                        if (saleFirstVisibleItem.equals("All")) {
                            requestSalesViewPagerValueAPI();
                        } else {
                            requestSalesPagerOnScrollAPI();
                        }
                        selFirstPositionValue = firstVisibleItem;
                    }
                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }

            }
            else
            {
                firstVisibleItem = salesAnalysisClassArrayList.size() - 1;
                LinearLayoutManager llm = (LinearLayoutManager) listView_SalesAnalysis.getLayoutManager();
                llm.scrollToPosition(firstVisibleItem);
                if (txtheaderplanclass.getText().toString().equals("Department")) {
                    level = 1;
                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getPlanDept();
                } else if (txtheaderplanclass.getText().toString().equals("Subdept")) {
                    level = 2;
                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getPlanCategory();
                } else if (txtheaderplanclass.getText().toString().equals("Class")) {
                    level = 3;
                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getPlanClass();
                } else if (txtheaderplanclass.getText().toString().equals("Subclass")) {
                    level = 4;
                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getBrandName();
                } else if (txtheaderplanclass.getText().toString().equals("MC")) {
                    level = 5;
                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getBrandplanClass();
                }
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.hDialog();
                    currentVmPos = vwpagersales.getCurrentItem();
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();

                    if (firstVisibleItem != selFirstPositionValue) {

                        if (postRequest != null) {
                            postRequest.cancel();
                        }
                        progressBar1.setVisibility(View.VISIBLE);
                        if (saleFirstVisibleItem.equals("All")) {
                            requestSalesViewPagerValueAPI();

                        } else {
                            requestSalesPagerOnScrollAPI();
                        }
                        selFirstPositionValue = firstVisibleItem;
                    }
                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    // on Check change listener on Segment Listener(WTD, YTD,LW and L4W)
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (!filter_toggleClick ) {
            switch (checkedId) {

                case R.id.btnWTD:
                    if (selectedsegValue.equals("WTD"))
                        break;
                    selectedsegValue = "WTD";
                    if (lldots != null)
                    {
                        lldots.removeAllViews();
                    }
                    llhierarchy.setVisibility(View.GONE);
                    currentVmPos = vwpagersales.getCurrentItem();
                    salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                    analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.hDialog();
                        Reusable_Functions.sDialog(context, "Loading data...");
                        progressBar1.setVisibility(View.GONE);
                        offsetvalue = 0;
                        limit = 100;
                        count = 0;
                        val = "";
                        if (getIntent().getStringExtra("selectedDept") == null)
                        {
                            requestSalesListDisplayAPI();
                        }
                        else
                        {
                            String str = getIntent().getStringExtra("selectedDept");
                            requestSalesSelectedFilterVal(str);
                        }
                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.btnLW:
                    if (selectedsegValue.equals("LW"))
                        break;
                    selectedsegValue = "LW";
                    if (lldots != null) {
                        lldots.removeAllViews();
                    }
                    currentVmPos = vwpagersales.getCurrentItem();
                    llhierarchy.setVisibility(View.GONE);
                   salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                    analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.hDialog();
                        Reusable_Functions.sDialog(context, "Loading data...");
                        progressBar1.setVisibility(View.GONE);
                        offsetvalue = 0;
                        limit = 100;
                        count = 0;
                        val = "";
                        if (getIntent().getStringExtra("selectedDept") == null) {
                            requestSalesListDisplayAPI();

                        } else {
                            String str = getIntent().getStringExtra("selectedDept");
                            requestSalesSelectedFilterVal(str);
                        }

                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.btnL4W:
                    if (selectedsegValue.equals("L4W"))
                        break;
                    selectedsegValue = "L4W";
                    if (lldots != null)
                    {
                        lldots.removeAllViews();
                    }

                    currentVmPos = vwpagersales.getCurrentItem();
                    llhierarchy.setVisibility(View.GONE);
                    salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                    analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.hDialog();
                        Reusable_Functions.sDialog(context, "Loading data...");
                        progressBar1.setVisibility(View.GONE);
                        offsetvalue = 0;
                        limit = 100;
                        count = 0;
                        val = "";
                        if (getIntent().getStringExtra("selectedDept") == null)
                        {
                            requestSalesListDisplayAPI();
                        }
                        else
                        {
                            String str = getIntent().getStringExtra("selectedDept");
                            requestSalesSelectedFilterVal(str);
                        }
                    } else
                    {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.btnYTD:
                    if (selectedsegValue.equals("STD"))
                        break;
                    selectedsegValue = "STD";
                    if (lldots != null) {
                        lldots.removeAllViews();
                    }
                    currentVmPos = vwpagersales.getCurrentItem();
                    llhierarchy.setVisibility(View.GONE);
                    salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                    analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();

                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.hDialog();
                        Reusable_Functions.sDialog(context, "Loading data...");
                        progressBar1.setVisibility(View.GONE);
                        offsetvalue = 0;
                        limit = 100;
                        count = 0;
                        val = "";

                        if (getIntent().getStringExtra("selectedDept") == null)
                        {
                            requestSalesListDisplayAPI();
                        }
                        else
                        {
                            String str = getIntent().getStringExtra("selectedDept");
                            requestSalesSelectedFilterVal(str);
                        }
                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }

                    break;
                default:
                    break;

            }
        } else {
            filter_toggleClick = false;
        }
    }

    //Api to display class level values(Api 1.20)
    private void requestSalesListDisplayAPI() {
        String url = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit;
        postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            int i;
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                onClickFlag = false;
                                progressBar1.setVisibility(View.GONE);
                            } else if (response.length() == limit)
                            {
                                for (i = 0; i < response.length(); i++)
                                {
                                    salesAnalysisClass = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisClass);
                                    salesadapter.addSnap(salesAnalysisClass);

                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesListDisplayAPI();

                            } else if (response.length() < limit) {
                                for (i = 0; i < response.length(); i++)
                                {
                                    salesAnalysisClass = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisClass);
                                    salesadapter.addSnap(salesAnalysisClass);
                                }

                                for (i = 0; i < 3; i++) {
                                    ImageView imgdot = new ImageView(context);
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
                                    layoutParams.setMargins(3, 3, 3, 3);
                                    imgdot.setLayoutParams(layoutParams);
                                    imgdot.setImageResource(R.mipmap.dots_unselected);
                                    lldots.addView(imgdot);

                                }
                                final int currentItem = vwpagersales.getCurrentItem();
                                ImageView img = (ImageView) lldots.getChildAt(currentItem);
                                img.setImageResource(R.mipmap.dots_selected);
                                txtStoreCode.setText("" + salesAnalysisClassArrayList.get(i).getStoreCode());
                                txtStoreDesc.setText("" + salesAnalysisClassArrayList.get(i).getStoreDesc());

                                // For Add "All"
                                salesAnalysisClass = new SalesAnalysisListDisplay();
                                if (txtheaderplanclass.getText().toString().equals("Department"))
                                {
                                    salesAnalysisClass.setPlanDept("All");

                                } else if (txtheaderplanclass.getText().toString().equals("Subdept"))
                                {
                                    salesAnalysisClass.setPlanCategory("All");

                                } else if (txtheaderplanclass.getText().toString().equals("Class"))
                                {
                                    salesAnalysisClass.setPlanClass("All");

                                } else if (txtheaderplanclass.getText().toString().equals("Subclass"))
                                {
                                    salesAnalysisClass.setBrandName("All");

                                } else if (txtheaderplanclass.getText().toString().equals("MC"))
                                {
                                    salesAnalysisClass.setBrandplanClass("All");

                                }
                                salesAnalysisClassArrayList.add(0, salesAnalysisClass);
                                listView_SalesAnalysis.setLayoutManager(new LinearLayoutManager(context));

                                listView_SalesAnalysis.setLayoutManager(new LinearLayoutManager(listView_SalesAnalysis.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                listView_SalesAnalysis.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(listView_SalesAnalysis);

                                salesadapter = new SalesAnalysisSnapAdapter(salesAnalysisClassArrayList, context, firstVisibleItem, fromWhere, listView_SalesAnalysis);
                                listView_SalesAnalysis.setAdapter(salesadapter);

                                //Retain Values.....
                                for (int j = 0; j < salesAnalysisClassArrayList.size(); j++) {

                                    if (txtheaderplanclass.getText().toString().equals("Department")) {
                                        level = 1;
                                        saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getPlanDept();
                                        if (salesAnalysisClassArrayList.get(firstVisibleItem).getPlanDept().equals(saleFirstVisibleItem)) {
                                            listView_SalesAnalysis.getLayoutManager().scrollToPosition(firstVisibleItem);
                                        }

                                    } else if (txtheaderplanclass.getText().toString().equals("Subdept")) {
                                        level = 2;
                                        saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getPlanCategory();
                                        if (salesAnalysisClassArrayList.get(firstVisibleItem).getPlanCategory().equals(saleFirstVisibleItem)) {
                                            listView_SalesAnalysis.getLayoutManager().scrollToPosition(firstVisibleItem);
                                        }

                                    } else if (txtheaderplanclass.getText().toString().equals("Class")) {

                                        level = 3;
                                        saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getPlanClass();
                                        if (salesAnalysisClassArrayList.get(firstVisibleItem).getPlanClass().equals(saleFirstVisibleItem)) {
                                            listView_SalesAnalysis.getLayoutManager().scrollToPosition(firstVisibleItem);

                                        }

                                    } else if (txtheaderplanclass.getText().toString().equals("Subclass")) {

                                        level = 4;
                                        saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getBrandName();
                                        if (salesAnalysisClassArrayList.get(firstVisibleItem).getBrandName().equals(saleFirstVisibleItem)) {
                                            listView_SalesAnalysis.getLayoutManager().scrollToPosition(firstVisibleItem);
                                        }

                                    } else if (txtheaderplanclass.getText().toString().equals("MC")) {

                                        level = 5;
                                        saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getBrandplanClass();
                                        if (salesAnalysisClassArrayList.get(firstVisibleItem).getBrandplanClass().equals(saleFirstVisibleItem)) {
                                            listView_SalesAnalysis.getLayoutManager().scrollToPosition(firstVisibleItem);
                                        }
                                    }
                                }

                                if (saleFirstVisibleItem.equals("All")) {
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    analysisArrayList.clear();
                                    llhierarchy.setVisibility(View.GONE);
                                    requestSalesViewPagerValueAPI();

                                } else {
                                    llhierarchy.setVisibility(View.GONE);
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    analysisArrayList.clear();
                                    requestSalesPagerOnScrollAPI();
                                }
                            }

                        } catch (Exception e)
                        {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                            progressBar1.setVisibility(View.GONE);
                            onClickFlag = false;
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                        progressBar1.setVisibility(View.GONE);
                        onClickFlag = false;

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

    //Api 1.19 for view pager values on store level like wtd , lw
    private void requestSalesViewPagerValueAPI() {

        String url = ConstsCore.web_url + "/v1/display/salesanalysisbytime/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit;
        postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                onClickFlag = false;
                                progressBar1.setVisibility(View.GONE);
                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysis = gson.fromJson(response.get(i).toString(), SalesAnalysisViewPagerValue.class);
                                    analysisArrayList.add(salesAnalysis);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;

                                requestSalesViewPagerValueAPI();

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysis = gson.fromJson(response.get(i).toString(), SalesAnalysisViewPagerValue.class);
                                    analysisArrayList.add(salesAnalysis);
                                }
                            }
                            pageradapter = new SalesPagerAdapter(context, analysisArrayList, firstVisibleItem, vwpagersales, lldots, salesadapter, listView_SalesAnalysis, salesAnalysisClassArrayList, fromWhere, pageradapter);
                            vwpagersales.setAdapter(pageradapter);
                            vwpagersales.setCurrentItem(currentVmPos);
                            pageradapter.notifyDataSetChanged();
                            onClickFlag = false;
                            Reusable_Functions.hDialog();
                            progressBar1.setVisibility(View.GONE);

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            onClickFlag = false;
                            progressBar1.setVisibility(View.GONE);
                            Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        onClickFlag = false;
                        progressBar1.setVisibility(View.GONE);
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

    // APi to display view pager value on scroll
    private void requestSalesPagerOnScrollAPI() {

        String url = " ";
        saleFirstVisibleItem = saleFirstVisibleItem.replace("%", "%25");
        saleFirstVisibleItem = saleFirstVisibleItem.replace(" ", "%20").replace("&", "%26");

        if (txtheaderplanclass.getText().toString().equals("Department")) {
            url = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + selectedsegValue +"&level="+level +"&department=" + saleFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        } else if (txtheaderplanclass.getText().toString().equals("Subdept")) {
            url = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + selectedsegValue +"&level="+level+ "&category=" + saleFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        } else if (txtheaderplanclass.getText().toString().equals("Class")) {
            url = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + selectedsegValue +"&level="+level+  "&class=" + saleFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        } else if (txtheaderplanclass.getText().toString().equals("Subclass")) {
            url = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + selectedsegValue +"&level="+level+ "&brand=" + saleFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        } else if (txtheaderplanclass.getText().toString().equals("MC")) {
            url = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + selectedsegValue +"&level="+level + "&brandclass=" + saleFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        }
        postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            int valuePos = firstVisibleItem;
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                progressBar1.setVisibility(View.GONE);
                                onClickFlag = false;
                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysis = gson.fromJson(response.get(i).toString(), SalesAnalysisViewPagerValue.class);
                                    analysisArrayList.add(salesAnalysis);

                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesPagerOnScrollAPI();


                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysis = gson.fromJson(response.get(i).toString(), SalesAnalysisViewPagerValue.class);
                                    analysisArrayList.add(salesAnalysis);
                                }

                                pageradapter = new SalesPagerAdapter(context, analysisArrayList, firstVisibleItem, vwpagersales, lldots, salesadapter, listView_SalesAnalysis, salesAnalysisClassArrayList, fromWhere, pageradapter);

                                vwpagersales.setAdapter(pageradapter);
                                vwpagersales.setCurrentItem(currentVmPos);
                                pageradapter.notifyDataSetChanged();
                                progressBar1.setVisibility(View.GONE);
                                onClickFlag = false;
                                Reusable_Functions.hDialog();
                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                            onClickFlag = false;
                            progressBar1.setVisibility(View.GONE);


                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                        onClickFlag = false;
                        progressBar1.setVisibility(View.GONE);
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

    private void requestSalesCategoryList(final String deptName) {
        String salespvacategory_listurl ;
        salespvacategory_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&department=" + deptName.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;

        postRequest = new JsonArrayRequest(Request.Method.GET, salespvacategory_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                       try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "No Subdept data found", Toast.LENGTH_SHORT).show();
                                progressBar1.setVisibility(View.GONE);
                                onClickFlag = false;

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    salesAnalysisClass = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisClass);
                                }

                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesCategoryList(deptName);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisClass = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisClass);
                                }
                                for (int i = 0; i < 3; i++) {
                                    ImageView imgdot = new ImageView(context);
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
                                    layoutParams.setMargins(3, 3, 3, 3);
                                    imgdot.setLayoutParams(layoutParams);
                                    imgdot.setImageResource(R.mipmap.dots_unselected);
                                    lldots.addView(imgdot);

                                }
                                int currentItem = vwpagersales.getCurrentItem();
                                ImageView img = (ImageView) lldots.getChildAt(currentItem);
                                img.setImageResource(R.mipmap.dots_selected);

                                listView_SalesAnalysis.setLayoutManager(new LinearLayoutManager(
                                        listView_SalesAnalysis.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                listView_SalesAnalysis.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(listView_SalesAnalysis);

                                salesadapter = new SalesAnalysisSnapAdapter(salesAnalysisClassArrayList, context, firstVisibleItem, fromWhere, listView_SalesAnalysis);
                                listView_SalesAnalysis.setAdapter(salesadapter);
                                txtStoreCode.setText(salesAnalysisClassArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(salesAnalysisClassArrayList.get(0).getStoreDesc());
                                val = "";
                                val = deptName;
                                txthDeptName.setText(val);
                                llhierarchy.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                                saleFirstVisibleItem = salesAnalysisClassArrayList.get(0).getPlanCategory();
                                requestSalesPagerOnScrollAPI();
                            }


                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "No Subdept data found", Toast.LENGTH_SHORT).show();
                            progressBar1.setVisibility(View.GONE);
                            onClickFlag = false;
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "No Subdept data found", Toast.LENGTH_SHORT).show();
                        progressBar1.setVisibility(View.GONE);
                        onClickFlag = false;
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

    private void requestSalesPlanClassListAPI(final String category) {

        String salespva_planclass_listurl = "";
        salespva_planclass_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&category=" + category.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_planclass_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "No Class data found", Toast.LENGTH_SHORT).show();
                                progressBar1.setVisibility(View.GONE);
                                onClickFlag = false;

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisClass = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisClass);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesPlanClassListAPI(category);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisClass = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisClass);
                                }

                                for (int i = 0; i < 3; i++) {
                                    ImageView imgdot = new ImageView(context);
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
                                    layoutParams.setMargins(3, 3, 3, 3);
                                    imgdot.setLayoutParams(layoutParams);
                                    imgdot.setImageResource(R.mipmap.dots_unselected);
                                    lldots.addView(imgdot);

                                }
                                int currentItem = vwpagersales.getCurrentItem();
                                ImageView img = (ImageView) lldots.getChildAt(currentItem);
                                img.setImageResource(R.mipmap.dots_selected);

                                listView_SalesAnalysis.setLayoutManager(new LinearLayoutManager(
                                        listView_SalesAnalysis.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                listView_SalesAnalysis.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(listView_SalesAnalysis);

                                salesadapter = new SalesAnalysisSnapAdapter(salesAnalysisClassArrayList, context, firstVisibleItem, fromWhere, listView_SalesAnalysis);
                                listView_SalesAnalysis.setAdapter(salesadapter);
                                txtStoreCode.setText(salesAnalysisClassArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(salesAnalysisClassArrayList.get(0).getStoreDesc());

                                val += " > " + category;

                                txthDeptName.setText(val);
                                llhierarchy.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                                saleFirstVisibleItem = salesAnalysisClassArrayList.get(0).getPlanClass();
                                requestSalesPagerOnScrollAPI();
                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "No Class data found", Toast.LENGTH_SHORT).show();
                            progressBar1.setVisibility(View.GONE);
                            onClickFlag = false;
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "No Class data found", Toast.LENGTH_SHORT).show();
                        progressBar1.setVisibility(View.GONE);
                        onClickFlag = false;
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

    private void requestSalesBrandListAPI(final String planclass) {
        String salespva_brand_listurl ;

        salespva_brand_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&class=" + planclass.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_brand_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "No Subclass data found", Toast.LENGTH_SHORT).show();
                                progressBar1.setVisibility(View.GONE);
                                onClickFlag = false;
                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisClass = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisClass);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesBrandListAPI(planclass);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisClass = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisClass);
                                }

                                for (int i = 0; i < 3; i++) {
                                    ImageView imgdot = new ImageView(context);
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
                                    layoutParams.setMargins(3, 3, 3, 3);
                                    imgdot.setLayoutParams(layoutParams);
                                    imgdot.setImageResource(R.mipmap.dots_unselected);
                                    lldots.addView(imgdot);

                                }
                                int currentItem = vwpagersales.getCurrentItem();
                                ImageView img = (ImageView) lldots.getChildAt(currentItem);
                                img.setImageResource(R.mipmap.dots_selected);
                                listView_SalesAnalysis.setLayoutManager(new LinearLayoutManager(
                                        listView_SalesAnalysis.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                listView_SalesAnalysis.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(listView_SalesAnalysis);

                                salesadapter = new SalesAnalysisSnapAdapter(salesAnalysisClassArrayList, context, firstVisibleItem, fromWhere, listView_SalesAnalysis);
                                listView_SalesAnalysis.setAdapter(salesadapter);
                                salesadapter.notifyDataSetChanged();
                                txtStoreCode.setText(salesAnalysisClassArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(salesAnalysisClassArrayList.get(0).getStoreDesc());
                                val += " > " + planclass;
                                txthDeptName.setText(val);
                                llhierarchy.setVisibility(View.VISIBLE);

                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                                saleFirstVisibleItem = salesAnalysisClassArrayList.get(0).getBrandName();
                                requestSalesPagerOnScrollAPI();
                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "No Subclass data found", Toast.LENGTH_SHORT).show();
                            progressBar1.setVisibility(View.GONE);
                            onClickFlag = false;
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "No Subclass data found", Toast.LENGTH_SHORT).show();
                        progressBar1.setVisibility(View.GONE);
                        onClickFlag = false;
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

    private void requestSalesBrandPlanListAPI(final String brandnm) {

        String salespva_brandplan_listurl ;

        salespva_brandplan_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&brand=" + brandnm.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_brandplan_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                      try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "No MC data found", Toast.LENGTH_SHORT).show();
                                progressBar1.setVisibility(View.GONE);
                                onClickFlag = false;

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisClass = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisClass);

                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesBrandPlanListAPI(brandnm);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisClass = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisClass);
                                }

                                for (int i = 0; i < 3; i++) {
                                    ImageView imgdot = new ImageView(context);
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
                                    layoutParams.setMargins(3, 3, 3, 3);
                                    imgdot.setLayoutParams(layoutParams);
                                    imgdot.setImageResource(R.mipmap.dots_unselected);
                                    lldots.addView(imgdot);

                                }
                                int currentItem = vwpagersales.getCurrentItem();
                                ImageView img = (ImageView) lldots.getChildAt(currentItem);
                                img.setImageResource(R.mipmap.dots_selected);
                                listView_SalesAnalysis.setLayoutManager(new LinearLayoutManager(
                                        listView_SalesAnalysis.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                listView_SalesAnalysis.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(listView_SalesAnalysis);

                                salesadapter = new SalesAnalysisSnapAdapter(salesAnalysisClassArrayList, context, firstVisibleItem, fromWhere, listView_SalesAnalysis);
                                listView_SalesAnalysis.setAdapter(salesadapter);
                                salesadapter.notifyDataSetChanged();
                                txtStoreCode.setText(salesAnalysisClassArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(salesAnalysisClassArrayList.get(0).getStoreDesc());
                                val += " > " + brandnm;
                                txthDeptName.setText(val);
                                llhierarchy.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                                saleFirstVisibleItem = salesAnalysisClassArrayList.get(0).getBrandplanClass();
                                requestSalesPagerOnScrollAPI();
                            }

                        } catch (Exception e)

                        {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "No MC data found", Toast.LENGTH_SHORT).show();
                            progressBar1.setVisibility(View.GONE);
                            onClickFlag = false;
                            e.printStackTrace();
                        }
                    }
                }

                ,
                new Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "No MC data found", Toast.LENGTH_SHORT).show();
                        progressBar1.setVisibility(View.GONE);
                        onClickFlag = false;
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
        }

        ;
        int socketTimeout = 60000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }


    private void requestSalesSelectedFilterVal(final String str) {

        String salespva_brandplan_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + selectedsegValue + "&level=" + SalesFilterActivity.level_filter + str + "&offset=" + offsetvalue + "&limit=" + limit;
        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_brandplan_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if (SalesFilterActivity.level_filter == 2) {
                            txtheaderplanclass.setText("Subdept");
                            fromWhere = "Subdept";
                            relprevbtn.setVisibility(View.VISIBLE);

                        } else if (SalesFilterActivity.level_filter == 3) {
                            txtheaderplanclass.setText("Class");
                            fromWhere = "Class";
                            relprevbtn.setVisibility(View.VISIBLE);


                        } else if (SalesFilterActivity.level_filter == 4) {
                            txtheaderplanclass.setText("Subclass");
                            fromWhere = "Subclass";
                            relprevbtn.setVisibility(View.VISIBLE);


                        } else if (SalesFilterActivity.level_filter == 5) {
                            txtheaderplanclass.setText("MC");
                            fromWhere = "MC";
                            relprevbtn.setVisibility(View.VISIBLE);
                            relnextbtn.setVisibility(View.INVISIBLE);


                        } else if (SalesFilterActivity.level_filter == 6) {
                            txtheaderplanclass.setText("MC");
                            fromWhere = "MC";
                            relprevbtn.setVisibility(View.VISIBLE);
                            relnextbtn.setVisibility(View.INVISIBLE);

                        }

                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {

                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                progressBar1.setVisibility(View.GONE);
                                onClickFlag = false;

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisClass = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisClass);

                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesSelectedFilterVal(str);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisClass = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisClass);
                                }

                                for (int i = 0; i < 3; i++) {
                                    ImageView imgdot = new ImageView(context);
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
                                    layoutParams.setMargins(3, 3, 3, 3);
                                    imgdot.setLayoutParams(layoutParams);
                                    imgdot.setImageResource(R.mipmap.dots_unselected);
                                    lldots.addView(imgdot);

                                }
                                int currentItem = vwpagersales.getCurrentItem();
                                ImageView img = (ImageView) lldots.getChildAt(currentItem);
                                img.setImageResource(R.mipmap.dots_selected);
                                listView_SalesAnalysis.setLayoutManager(new LinearLayoutManager(
                                        listView_SalesAnalysis.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                listView_SalesAnalysis.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(listView_SalesAnalysis);
                                salesadapter = new SalesAnalysisSnapAdapter(salesAnalysisClassArrayList, context, firstVisibleItem, fromWhere, listView_SalesAnalysis);
                                listView_SalesAnalysis.setAdapter(salesadapter);
                                salesadapter.notifyDataSetChanged();
                                txtStoreCode.setText(salesAnalysisClassArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(salesAnalysisClassArrayList.get(0).getStoreDesc());
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();

                                if (txtheaderplanclass.getText().toString().equals("Department")) {
                                    level = 1;
                                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getPlanDept();
                                } else if (txtheaderplanclass.getText().toString().equals("Subdept")) {
                                    level = 2;
                                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getPlanCategory();
                                } else if (txtheaderplanclass.getText().toString().equals("Class")) {
                                    level = 3;
                                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getPlanClass();
                                } else if (txtheaderplanclass.getText().toString().equals("Subclass")) {
                                    level = 4;
                                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getBrandName();
                                } else if (txtheaderplanclass.getText().toString().equals("MC")) {
                                    level = 5;
                                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getBrandplanClass();
                                }
                                requestSalesPagerOnScrollAPI();
                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                            progressBar1.setVisibility(View.GONE);
                            onClickFlag = false;
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                        progressBar1.setVisibility(View.GONE);
                        onClickFlag = false;
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

        SalesFilterExpandableList.text1 = "";
        SalesFilterExpandableList.text2 = "";
        SalesFilterExpandableList.text3 = "";
        SalesFilterExpandableList.text4 = "";
        SalesFilterExpandableList.text5 = "";
        selectedsegValue = "";
        level = 0;
        selectedsegValue = "WTD";
        level = 1;
        this.finish();

    }
}
