package apsupportapp.aperotechnologies.com.designapp.SalesAnalysis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
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
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisListDisplay;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisViewPagerValue;
import info.hoang8f.android.segmented.SegmentedGroup;


public class SalesAnalysisActivity1 extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, OnItemClickListener {

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
    String TAG = "SalesAnalysisActivity";
    SalesPagerAdapter pageradapter;
    SharedPreferences sharedPreferences;
    String userId, bearertoken;
    EditText etListText;

    RelativeLayout btnBack;
    RadioButton btnWTD;
    public static String selectedsegValue;
    String saleFirstVisibleItem;
    String fromWhere = "Department";
    TextView txtStoreCode, txtStoreDesc;
    int offsetvalue = 0, limit = 100;
    int count = 0, level;
    RequestQueue queue;
    static String planDept, planCategory, planClass, brandnm;
    Gson gson;
    static int focusposition = 0;
    RelativeLayout relimgrank, relimgfilter;
    RelativeLayout Style_loadingBar;
    RelativeLayout relprevbtn, relnextbtn, relimgclose;
    TextView txtheaderplanclass;
    TextView txtZonalSales, txtNationalSales;
    TextView txtZonalYOY, txtNationalYOY;
    TextView txthDeptName;
    int selFirstPositionValue = 500;
    String txtSalesClickedValue;
    String val;
    boolean flag = false,onClickFlag=false;
    int currentVmPos;
    static int currentIndex = 0;
    int value = 0;
    ProgressBar progressBar1;
    JsonArrayRequest postRequest;
    int firstVisiblePosition;
    static public int firstVisibleItem = 0;
    public int totalItemCount;
    int prevState = RecyclerView.SCROLL_STATE_IDLE;
    int currentState = RecyclerView.SCROLL_STATE_IDLE;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_analysis1);
        getSupportActionBar().hide();
        fromWhere = "Department";
        txtSalesClickedValue = " ";
        val = "";
        context = this;
        //   SalesFilter.searchDept = " ";
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
        selectedsegValue = "WTD";
        btnWTD = (RadioButton) findViewById(R.id.btnWTD);
        btnWTD.toggle();

        llayoutSalesAnalysis = (LinearLayout) findViewById(R.id.llayoutSalesAnalysis);
        //llayoutSalesAnalysis.setVisibility(View.GONE);


        relimgfilter = (RelativeLayout) findViewById(R.id.imgfilter);
        relimgrank = (RelativeLayout) findViewById(R.id.imgrank);
        relprevbtn = (RelativeLayout) findViewById(R.id.prevplanclass);
        relprevbtn.setVisibility(View.INVISIBLE);
        relnextbtn = (RelativeLayout) findViewById(R.id.nextplanclass);
        txtheaderplanclass = (TextView) findViewById(R.id.headerplanclass);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        //Rank UI Components
        // rankLayout = (LinearLayout) findViewById(R.id.rankLayout);
        txtZonalSales = (TextView) findViewById(R.id.txtZonalSales);
        txtNationalSales = (TextView) findViewById(R.id.txtNationalSales);
        txtZonalYOY = (TextView) findViewById(R.id.txtZonalYOY);
        txtNationalYOY = (TextView) findViewById(R.id.txtNationalYOY);

        //WOW & YOY UI Components

        txtheaderplanclass.setText("Department");

        relimgclose = (RelativeLayout) findViewById(R.id.relimgclose);

//        RelativeLayout relpopuplayout = (RelativeLayout) findViewById(R.id.relpopuplayout);
//        relpopuplayout.setOnClickListener(null);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
               /* Intent i = new Intent(SalesAnalysisActivity.this, DashBoardActivity.class);
                startActivity(i);*/
                //finish();
            }
        });


        relimgfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SalesAnalysisActivity1.this, SalesFilterActivity.class);
                intent.putExtra("checkfrom", "SalesAnalysis");
                startActivity(intent);
                //postRequest_hierarchy.cancel();

            }
        });

        focusposition = 0;
        level = 1;

        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
        analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
        salesList = new ArrayList<SalesAnalysisViewPagerValue>();

        vwpagersales = (ViewPager) findViewById(R.id.viewpager);
        // vwpagersales.setCurrentItem(0);
        pageradapter = new SalesPagerAdapter();
        pageradapter.notifyDataSetChanged();
        lldots = (LinearLayout) findViewById(R.id.lldots);
        lldots.setOrientation(LinearLayout.HORIZONTAL);
        llhierarchy = (LinearLayout) findViewById(R.id.llhierarchy);
        llhierarchy.setOrientation(LinearLayout.HORIZONTAL);
        relLayoutSales = (RelativeLayout) findViewById(R.id.relTablelayout);

        listView_SalesAnalysis = (RecyclerView) findViewById(R.id.listView_SalesAnalysis);

        salesadapter = new SalesAnalysisSnapAdapter();
        // listView_SalesAnalysis.addFooterView(getLayoutInflater().inflate(R.layout.list_footer, null));

        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();

        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            //Reusable_Functions.sDialog(context,"Loading..");
            progressBar1.setVisibility(View.VISIBLE);
            llhierarchy.setVisibility(View.GONE);
            //llayoutSalesAnalysis.setVisibility(View.GONE);
            offsetvalue = 0;
            limit = 100;
            count = 0;
            level = 1;
            // if (getIntent().getStringExtra("selectedDept") == null) {
            requestSalesListDisplayAPI();

//            } else {
//                saleFirstVisibleItem = getIntent().getStringExtra("selectedDept");
//                requestSalesSelectedFilterVal();
//            }


        } else {

            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
            progressBar1.setVisibility(View.GONE);
        }


        relprevbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postRequest != null) {
                    postRequest.cancel();
                }
                switch (txtheaderplanclass.getText().toString()) {

                    case "Brand Plan Class":

                        relnextbtn.setVisibility(View.VISIBLE);
                        //SalesPagerAdapter.currentPage = 0;
                        if (lldots != null) {
                            lldots.removeAllViews();
                        }
                        currentVmPos = vwpagersales.getCurrentItem();
                        llhierarchy.setVisibility(View.GONE);
                        txtheaderplanclass.setText("Brand");
                        fromWhere = "Brand";
                        flag = false;
                        level = 4;
                        salesAnalysisClassArrayList.clear();
                        listView_SalesAnalysis.removeAllViews();
                        // llayoutSalesAnalysis.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                           Reusable_Functions.hDialog();
                           Reusable_Functions.sDialog(context, "Loading data...");
                           progressBar1.setVisibility(View.VISIBLE);
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.e("txt Prev-- ", "  ");

                            requestSalesListDisplayAPI();
                            Log.e("prev 1", "" + salesAnalysisClass.getBrandName());

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case "Brand":
                        //SalesPagerAdapter.currentPage = 0;
                        if (lldots != null) {
                            lldots.removeAllViews();
                        }
                        currentVmPos = vwpagersales.getCurrentItem();
                        llhierarchy.setVisibility(View.GONE);
                        txtheaderplanclass.setText("Plan Class");
                        fromWhere = "Plan Class";
                        flag = false;
                        level = 3;
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        listView_SalesAnalysis.removeAllViews();
                       //  llayoutSalesAnalysis.setVisibility(View.GONE);

                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            progressBar1.setVisibility(View.VISIBLE);
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.e("Brand name prev", "--");
                            requestSalesListDisplayAPI();
                            Log.e("prev 2", "" + salesAnalysisClass.getPlanClass());
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("---2---", " ");
                        break;

                    case "Plan Class":
                        //SalesPagerAdapter.currentPage = 0;
                        if (lldots != null) {
                            lldots.removeAllViews();
                        }
                        currentVmPos = vwpagersales.getCurrentItem();
                        llhierarchy.setVisibility(View.GONE);
                        txtheaderplanclass.setText("Category");
                        fromWhere = "Category";
                        flag = false;
                        level = 2;
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        listView_SalesAnalysis.removeAllViews();
                        //    llayoutSalesAnalysis.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.hDialog();
                           progressBar1.setVisibility(View.VISIBLE);
                           Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.e("Plan class prev", "");
                            requestSalesListDisplayAPI();
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("---3---", " ");
                        break;

                    case "Category":
                        relprevbtn.setVisibility(View.INVISIBLE);
                        //relnextbtn.setVisibility(View.VISIBLE);
                        //SalesPagerAdapter.currentPage = 0;
                        if (lldots != null) {
                            lldots.removeAllViews();
                        }
                        currentVmPos = vwpagersales.getCurrentItem();
                        llhierarchy.setVisibility(View.GONE);
                        txtheaderplanclass.setText("Department");
                        fromWhere = "Department";
                        flag = false;
                        level = 1;
                        salesAnalysisClassArrayList.clear();
                        listView_SalesAnalysis.removeAllViews();
                        // llayoutSalesAnalysis.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.hDialog();
                            progressBar1.setVisibility(View.VISIBLE);
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.e("Category prev", "");
                            requestSalesListDisplayAPI();
                            Log.e("prev 4", "" + salesAnalysisClass.getPlanDept());
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("---4---", " ");
                        break;
                    default:
                }
            }
        });

        relnextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postRequest != null) {
                    postRequest.cancel();
                }
                switch (txtheaderplanclass.getText().toString()) {
                    case "Department":
                        relprevbtn.setVisibility(View.VISIBLE);
                        txtheaderplanclass.setText("Category");
                        //SalesPagerAdapter.currentPage = 0;
                        if (lldots != null) {
                            lldots.removeAllViews();
                        }
                        currentVmPos = vwpagersales.getCurrentItem();
                        llhierarchy.setVisibility(View.GONE);
                        fromWhere = "Category";
                        flag = false;
                        level = 2;

                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        listView_SalesAnalysis.removeAllViews();

                        //   llayoutSalesAnalysis.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            progressBar1.setVisibility(View.VISIBLE);
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.i("dept next", "-----");
                            requestSalesListDisplayAPI();
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case "Category":
                        fromWhere = "Plan Class";
                        txtheaderplanclass.setText("Plan Class");
                        level = 3;
                        //SalesPagerAdapter.currentPage = 0;
                        if (lldots != null) {
                            lldots.removeAllViews();
                        }
                        currentVmPos = vwpagersales.getCurrentItem();
                        flag = false;
                        llhierarchy.setVisibility(View.GONE);
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        listView_SalesAnalysis.removeAllViews();
                         //   llayoutSalesAnalysis.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.hDialog();
                            progressBar1.setVisibility(View.VISIBLE);
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.e("category next --", "");
                            requestSalesListDisplayAPI();
                            Log.e("next 2", "" + salesAnalysisClass.getPlanClass());
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("---2---", " ");
                        break;

                    case "Plan Class":
                        txtheaderplanclass.setText("Brand");
                        fromWhere = "Brand";
                        level = 4;
                        // SalesPagerAdapter.currentPage = 0;
                        if (lldots != null) {
                            lldots.removeAllViews();
                        }
                        currentVmPos = vwpagersales.getCurrentItem();
                        flag = false;
                        llhierarchy.setVisibility(View.GONE);
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        listView_SalesAnalysis.removeAllViews();
                        // llayoutSalesAnalysis.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            progressBar1.setVisibility(View.VISIBLE);
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            requestSalesListDisplayAPI();

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("---3--", " ");

                        break;

                    case "Brand":
                        txtheaderplanclass.setText("Brand Plan Class");

                        //relprevbtn.setVisibility(View.VISIBLE);
                        relnextbtn.setVisibility(View.INVISIBLE);
                        //SalesPagerAdapter.currentPage = 0;
                        if (lldots != null) {
                            lldots.removeAllViews();
                        }
                        currentVmPos = vwpagersales.getCurrentItem();
                        llhierarchy.setVisibility(View.GONE);
                        fromWhere = "Brand Plan Class";
                        flag = false;
                        level = 5;
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        listView_SalesAnalysis.removeAllViews();
                        // llayoutSalesAnalysis.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            progressBar1.setVisibility(View.VISIBLE);
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
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


        listView_SalesAnalysis.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                RecyclerViewPositionHelper mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
                int visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mRecyclerViewHelper.getItemCount();
                firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();
                Log.e("OnScroll","-----------");


            }


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                currentState = newState;
                if(prevState != RecyclerView.SCROLL_STATE_IDLE && currentState == RecyclerView.SCROLL_STATE_IDLE && onClickFlag == false ){

                    Log.i(TAG, ""+"scroll state"+newState );
                    Handler h=new Handler();
                    h.postDelayed(new Runnable(){
                        public void run(){
                            Log.e(TAG, "run: time out" );
                            TimeUP();
                        }
                    }, 700);
                }
                prevState = currentState;

            }
        });

        //Drill Down
        listView_SalesAnalysis.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        onClickFlag = true;
                        Reusable_Functions.sDialog(context, "Loading data...");


                        Handler h = new Handler();
                        h.postDelayed(new Runnable() {
                            public void run() {


                                if (position < salesAnalysisClassArrayList.size()) {
                            switch (txtheaderplanclass.getText().toString()) {

                                case "Department":
                                    relprevbtn.setVisibility(View.VISIBLE);
                                    txtheaderplanclass.setText("Category");
                                    //llayoutSalesAnalysis.setVisibility(View.GONE);
                                    //String plandept= salesAnalysisClassArrayList.get(position).getPlanDept().substring(0,1).toUpperCase()+salesAnalysisClassArrayList.get(position).getPlanDept().substring(1).toLowerCase();
                                    txtSalesClickedValue = salesAnalysisClassArrayList.get(position).getPlanDept();
                                    Log.e("txtClicked department--", "" + txtSalesClickedValue);
                                    fromWhere = "Category";
                                    //SalesPagerAdapter.currentPage = 0;
                                    if (lldots != null) {
                                        lldots.removeAllViews();
                                    }
                                    currentVmPos = vwpagersales.getCurrentItem();
                                    level = 2;
                                    if (Reusable_Functions.chkStatus(context)) {
                                        Reusable_Functions.hDialog();
                                        progressBar1.setVisibility(View.VISIBLE);
                                        Reusable_Functions.sDialog(context, "Loading data...");
                                        offsetvalue = 0;
                                        limit = 100;
                                        count = 0;
                                        salesAnalysisClassArrayList.clear();
                                        Log.i("dept next", "-----");
                                        requestSalesCategoryList(txtSalesClickedValue);
                                        planDept = txtSalesClickedValue;

                                    } else {
                                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                    }
                                    break;

                                case "Category":

                                    Log.e("in sales analysis category", "-----" + planDept);
                                    if (flag == true) {
                                        txtheaderplanclass.setText("Plan Class");
                                        //  llayoutSalesAnalysis.setVisibility(View.GONE);
                                        //String planCategry= salesAnalysisClassArrayList.get(position).getPlanCategory().substring(0,1).toUpperCase()+salesAnalysisClassArrayList.get(position).getPlanCategory().substring(1).toLowerCase();
                                        txtSalesClickedValue = salesAnalysisClassArrayList.get(position).getPlanCategory();
                                        Log.e("txtClicked category --", "" + txtSalesClickedValue);
                                        fromWhere = "Plan Class";
                                        //SalesPagerAdapter.currentPage = 0;
                                        if (lldots != null) {
                                            lldots.removeAllViews();
                                        }
                                        currentVmPos = vwpagersales.getCurrentItem();
                                        level = 3;
                                        if (Reusable_Functions.chkStatus(context)) {
                                            Reusable_Functions.hDialog();
                                            progressBar1.setVisibility(View.VISIBLE);

                                            Reusable_Functions.sDialog(context, "Loading data...");
                                            offsetvalue = 0;
                                            limit = 100;
                                            count = 0;
                                            salesAnalysisClassArrayList.clear();
                                            Log.i("category next", "-----");
                                            Log.i("come", "----" + planDept);
                                            requestSalesPlanClassListAPI(planDept, txtSalesClickedValue);
                                            planCategory = txtSalesClickedValue;
                                            Log.e("planCategory--", "" + planCategory);
                                        } else {
                                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(context, "Please select dept name", Toast.LENGTH_SHORT).show();
                                        Log.e("Please select dept name", "");
                                    }
                                    break;

                                case "Plan Class":
                                    Log.e("in sales analysis plan class", "-----" + planDept);
                                    if (flag == true) {
                                        txtheaderplanclass.setText("Brand");
                                        //   llayoutSalesAnalysis.setVisibility(View.GONE);
                                        //String planCls= salesAnalysisClassArrayList.get(position).getPlanClass().substring(0,1).toUpperCase()+salesAnalysisClassArrayList.get(position).getPlanClass().substring(1).toLowerCase();
                                        txtSalesClickedValue = salesAnalysisClassArrayList.get(position).getPlanClass();
                                        Log.e("txtClicked plan class---", "" + txtSalesClickedValue);
                                        fromWhere = "Brand";
                                        //SalesPagerAdapter.currentPage = 0;
                                        if (lldots != null) {
                                            lldots.removeAllViews();
                                        }
                                        currentVmPos = vwpagersales.getCurrentItem();
                                        level = 4;
                                        if (Reusable_Functions.chkStatus(context)) {
                                            Reusable_Functions.hDialog();
                                            progressBar1.setVisibility(View.VISIBLE);
                                            Reusable_Functions.sDialog(context, "Loading data...");
                                            offsetvalue = 0;
                                            limit = 100;
                                            count = 0;
                                            salesAnalysisClassArrayList.clear();
                                            Log.i("Plan Class next", "-----");
                                            requestSalesBrandListAPI(planDept, planCategory, txtSalesClickedValue);
                                            planClass = txtSalesClickedValue;
                                            Log.e("planClass---", "" + planClass);
                                        } else {
                                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(context, "Please select dept name", Toast.LENGTH_SHORT).show();
                                        Log.e("Please Select Dept name", "-------");
                                    }
                                    break;

                                case "Brand":
                                    Log.e("in sales analysis brand", "-----" + planDept);
                                    if (flag == true) {
                                        relnextbtn.setVisibility(View.INVISIBLE);
                                        txtheaderplanclass.setText("Brand Plan Class");
                                        //   llayoutSalesAnalysis.setVisibility(View.GONE);
                                        //String brnd = salesAnalysisClassArrayList.get(position).getBrandName().substring(0,1).toUpperCase()+salesAnalysisClassArrayList.get(position).getBrandName().substring(1).toLowerCase();
                                        txtSalesClickedValue = salesAnalysisClassArrayList.get(position).getBrandName();
                                        Log.e("txtSalesClickedValue3---", "" + txtSalesClickedValue);
                                        fromWhere = "Brand Plan Class";
                                        //SalesPagerAdapter.currentPage = 0;
                                        if (lldots != null) {
                                            lldots.removeAllViews();
                                        }
                                        currentVmPos = vwpagersales.getCurrentItem();
                                        level = 5;
                                        if (Reusable_Functions.chkStatus(context)) {
                                            Reusable_Functions.hDialog();
                                            progressBar1.setVisibility(View.VISIBLE);
                                            Reusable_Functions.sDialog(context, "Loading data...");
                                            offsetvalue = 0;
                                            limit = 100;
                                            count = 0;
                                            salesAnalysisClassArrayList.clear();
                                            Log.i("brand next", "-----");
                                            requestSalesBrandPlanListAPI(planDept, planCategory, planClass, txtSalesClickedValue);
                                        } else {
                                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(context, "Please select dept name", Toast.LENGTH_SHORT).show();
                                        Log.e("Please select dept name", "------1");
                                    }
                                    break;
                            }

                        }
                            }
                        }, 700);
                    }
                }));
    }

    private void TimeUP() {

        if (salesAnalysisClassArrayList.size() != 0) {
            if (firstVisibleItem < salesadapter.getItemCount() - 1) {

                Log.e(TAG, "onScrollStateChanged: " + firstVisibleItem);
//                if (firstVisibleItem >= salesAnalysisClassArrayList.size()) {
//                    firstVisibleItem = salesAnalysisClassArrayList.size() - 1;
//                    LinearLayoutManager llm = (LinearLayoutManager) listView_SalesAnalysis.getLayoutManager();
//                    llm.scrollToPosition(firstVisibleItem);
//                    return;
//                }

                if (txtheaderplanclass.getText().toString().equals("Department")) {
                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getPlanDept().toString();
                } else if (txtheaderplanclass.getText().toString().equals("Category")) {
                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getPlanCategory().toString();
                } else if (txtheaderplanclass.getText().toString().equals("Plan Class")) {
                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getPlanClass().toString();
                } else if (txtheaderplanclass.getText().toString().equals("Brand")) {
                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getBrandName().toString();
                } else if (txtheaderplanclass.getText().toString().equals("Brand Plan Class")) {
                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getBrandplanClass().toString();
                }
                //  if (firstVisibleItem != selFirstPositionValue) {
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.hDialog();
                    currentVmPos = vwpagersales.getCurrentItem();
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();

                    if(firstVisibleItem != selFirstPositionValue) {
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

                // }
                //  selFirstPositionValue = firstVisibleItem;
            } else {
                firstVisibleItem = salesAnalysisClassArrayList.size() - 1;
                LinearLayoutManager llm = (LinearLayoutManager) listView_SalesAnalysis.getLayoutManager();
                llm.scrollToPosition(firstVisibleItem);

                if (txtheaderplanclass.getText().toString().equals("Department")) {
                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getPlanDept().toString();
                } else if (txtheaderplanclass.getText().toString().equals("Category")) {
                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getPlanCategory().toString();
                } else if (txtheaderplanclass.getText().toString().equals("Plan Class")) {
                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getPlanClass().toString();
                } else if (txtheaderplanclass.getText().toString().equals("Brand")) {
                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getBrandName().toString();
                } else if (txtheaderplanclass.getText().toString().equals("Brand Plan Class")) {
                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getBrandplanClass().toString();
                }
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.hDialog();
                    currentVmPos = vwpagersales.getCurrentItem();
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();

                    if(firstVisibleItem != selFirstPositionValue) {

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


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {

            case R.id.btnWTD:
                if (selectedsegValue.equals("WTD"))
                    break;
                selectedsegValue = "WTD";
                //SalesPagerAdapter.currentPage = 0;
                if (lldots != null) {
                    lldots.removeAllViews();
                }
                // listView_SalesAnalysis.invalidateViews();
                llhierarchy.setVisibility(View.GONE);
                currentVmPos = vwpagersales.getCurrentItem();
                Log.e(TAG, "currentVmPos: " + currentVmPos);
                // currentIndex = listView_SalesAnalysis.getFirstVisiblePosition();
                Log.e(TAG, " in WTD foucpos" + firstVisibleItem);
                // save position here, and set position on API call's onPostexecute that scroll pos will get on Scroll method??
                //saleFirstVisibleItem = " ";
                salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                //  llayoutSalesAnalysis.setVisibility(View.GONE);
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.hDialog();
                   // Reusable_Functions.sDialog(context, "Loading data...");
                    progressBar1.setVisibility(View.VISIBLE);
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    requestSalesListDisplayAPI();
                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
                Log.e("---1---", " ");
                break;

            case R.id.btnLW:
                if (selectedsegValue.equals("LW"))
                    break;
                selectedsegValue = "LW";
                //SalesPagerAdapter.currentPage = 0;
                if (lldots != null) {
                    lldots.removeAllViews();
                }
                currentVmPos = vwpagersales.getCurrentItem();
                llhierarchy.setVisibility(View.GONE);
                //   currentIndex = listView_SalesAnalysis.getFirstVisiblePosition();
                Log.e(TAG, " in LW foucpos" + firstVisibleItem);
                //saleFirstVisibleItem = " ";
                // listView_SalesAnalysis.invalidateViews();
                salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                // llayoutSalesAnalysis.setVisibility(View.GONE);
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.hDialog();
                 //   Reusable_Functions.sDialog(context, "Loading data...");
                    progressBar1.setVisibility(View.VISIBLE);
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    Log.e("array list size", "" + salesAnalysisClassArrayList.size());
                    value = 2;
                    requestSalesListDisplayAPI();
                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
                Log.e("---2---", " ");
                break;

            case R.id.btnL4W:
                if (selectedsegValue.equals("L4W"))
                    break;
                selectedsegValue = "L4W";
                //SalesPagerAdapter.currentPage = 0;
                if (lldots != null) {
                    lldots.removeAllViews();
                }
                // listView_SalesAnalysis.invalidateViews();
                currentVmPos = vwpagersales.getCurrentItem();
                llhierarchy.setVisibility(View.GONE);
                //    currentIndex = listView_SalesAnalysis.getFirstVisiblePosition();
                Log.e(TAG, " in L4W foucpos" + firstVisibleItem);
                //saleFirstVisibleItem = " ";
                salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                // llayoutSalesAnalysis.setVisibility(View.GONE);
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.hDialog();
                 //   Reusable_Functions.sDialog(context, "Loading data...");
                    progressBar1.setVisibility(View.VISIBLE);
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    Log.e("array list size in L4W", "----" + salesAnalysisClassArrayList.size());
                    requestSalesListDisplayAPI();

                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
                Log.e("---3---", " ");

                break;

            case R.id.btnYTD:
                if (selectedsegValue.equals("YTD"))
                    break;
                selectedsegValue = "YTD";
                //SalesPagerAdapter.currentPage = 0;
                if (lldots != null) {
                    lldots.removeAllViews();
                }
                // listView_SalesAnalysis.invalidateViews();
                currentVmPos = vwpagersales.getCurrentItem();
                llhierarchy.setVisibility(View.GONE);
                // currentIndex = listView_SalesAnalysis.getFirstVisiblePosition();
                Log.e(TAG, " in YTD foucpos" + firstVisibleItem);
                //saleFirstVisibleItem = " ";
                salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                // llayoutSalesAnalysis.setVisibility(View.GONE);
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.hDialog();
                    progressBar1.setVisibility(View.VISIBLE);
                  //  Reusable_Functions.sDialog(context, "Loading data...");
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    Log.e("array list size in YTD", "" + salesAnalysisClassArrayList.size());

                    requestSalesListDisplayAPI();
                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
                Log.e("---4---", " ");

                break;
            default:

        }

    }

    //Api to display class level values(Api 1.20)

    private void requestSalesListDisplayAPI() {
        String url = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + url);
       postRequest = new JsonArrayRequest(Request.Method.GET, url,
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
                                Style_loadingBar.setVisibility(View.GONE);
                            } else if (response.length() == limit) {
                                for (i = 0; i < response.length(); i++) {
                                    salesAnalysisClass = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisClass);
                                    salesadapter.addSnap(salesAnalysisClass);

                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesListDisplayAPI();

                            } else if (response.length() < limit) {
                                for (i = 0; i < response.length(); i++) {
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
                                Log.e("----", " " + vwpagersales.getCurrentItem());
                                ImageView img = (ImageView) lldots.getChildAt(currentItem);
                                img.setImageResource(R.mipmap.dots_selected);
                                txtStoreCode.setText("" + salesAnalysisClassArrayList.get(i).getStoreCode());
                                txtStoreDesc.setText("" + salesAnalysisClassArrayList.get(i).getStoreDesc());

                                // For Add "All"
                                salesAnalysisClass = new SalesAnalysisListDisplay();
                                if (txtheaderplanclass.getText().toString().equals("Department")) {
                                    salesAnalysisClass.setPlanDept("All");

                                } else if (txtheaderplanclass.getText().toString().equals("Category")) {
                                    salesAnalysisClass.setPlanCategory("All");

                                } else if (txtheaderplanclass.getText().toString().equals("Plan Class")) {
                                    salesAnalysisClass.setPlanClass("All");

                                } else if (txtheaderplanclass.getText().toString().equals("Brand")) {
                                    salesAnalysisClass.setBrandName("All");

                                } else if (txtheaderplanclass.getText().toString().equals("Brand Plan Class")) {
                                    salesAnalysisClass.setBrandplanClass("All");

                                }
                                salesAnalysisClassArrayList.add(0, salesAnalysisClass);
                                //Log.e(TAG, "focusPosition in API----" + currentIndex);
                                listView_SalesAnalysis.setLayoutManager(new LinearLayoutManager(context));

                                listView_SalesAnalysis.setLayoutManager(new LinearLayoutManager(
                                        listView_SalesAnalysis.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                listView_SalesAnalysis.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(listView_SalesAnalysis);

                                salesadapter = new SalesAnalysisSnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listView_SalesAnalysis);


                                Log.e("--- ", " salesAnalysisClassArrayList" + salesAnalysisClassArrayList.size());
//
//                                if (listView_SalesAnalysis.getAdapter() == null) {
//                                    listView_SalesAnalysis.setAdapter(salesadapter);
//                                    flag = false;
//                                    llhierarchy.setVisibility(View.GONE);
//                                    offsetvalue = 0;
//                                    limit = 100;
//                                    count = 0;
//                                    requestSalesViewPagerValueAPI();
//
//                                } else if (listView_SalesAnalysis.getAdapter() != null)
//
//                                {
                                    salesadapter = new SalesAnalysisSnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listView_SalesAnalysis);
                                    listView_SalesAnalysis.setAdapter(salesadapter);

                                    for (int j = 0; j < salesAnalysisClassArrayList.size(); j++) {
                                        if (txtheaderplanclass.getText().toString().equals("Department")) {
                                            if (salesAnalysisClassArrayList.get(j).getPlanDept().equals(saleFirstVisibleItem)) {
                                                firstVisibleItem = j;
                                                listView_SalesAnalysis.scrollToPosition(firstVisibleItem);
                                            }
                                        } else if (txtheaderplanclass.getText().toString().equals("Category")) {
                                            if (salesAnalysisClassArrayList.get(j).getPlanCategory().equals(saleFirstVisibleItem)) {
                                                firstVisibleItem = j;
                                                listView_SalesAnalysis.scrollToPosition(firstVisibleItem);
                                            }
                                        } else if (txtheaderplanclass.getText().toString().equals("Plan Class")) {
                                            if (salesAnalysisClassArrayList.get(j).getPlanClass().equals(saleFirstVisibleItem)) {
                                                firstVisibleItem = j;
                                                listView_SalesAnalysis.scrollToPosition(firstVisibleItem);
                                            }
                                        } else if (txtheaderplanclass.getText().toString().equals("Brand")) {
                                            if (salesAnalysisClassArrayList.get(j).getBrandName().equals(saleFirstVisibleItem)) {
                                                firstVisibleItem = j;
                                                listView_SalesAnalysis.scrollToPosition(firstVisibleItem);
                                            }
                                        } else if (txtheaderplanclass.getText().toString().equals("Brand Plan Class")) {
                                            if (salesAnalysisClassArrayList.get(j).getBrandplanClass().equals(saleFirstVisibleItem)) {
                                                firstVisibleItem = j;
                                                listView_SalesAnalysis.scrollToPosition(firstVisibleItem);
                                            }
                                        }
                                    }
                                    if (txtheaderplanclass.getText().toString().equals("Department")) {
                                        saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getPlanDept().toString();
                                    } else if (txtheaderplanclass.getText().toString().equals("Category")) {
                                        saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getPlanCategory().toString();
                                    } else if (txtheaderplanclass.getText().toString().equals("Plan Class")) {
                                        saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getPlanClass().toString();
                                    } else if (txtheaderplanclass.getText().toString().equals("Brand")) {
                                        saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getBrandName().toString();
                                    } else if (txtheaderplanclass.getText().toString().equals("Brand Plan Class")) {
                                        saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getBrandplanClass().toString();
                                    }
                                    if (saleFirstVisibleItem.equals("All")) {
                                        offsetvalue = 0;
                                        limit = 100;
                                        count = 0;
                                        analysisArrayList.clear();
                                        flag = false;
                                        llhierarchy.setVisibility(View.GONE);
                                        requestSalesViewPagerValueAPI();

                                    } else {
                                        flag = false;
                                        llhierarchy.setVisibility(View.GONE);
                                        offsetvalue = 0;
                                        limit = 100;
                                        count = 0;
                                        analysisArrayList .clear();

                                        requestSalesPagerOnScrollAPI();
                                    }




                                }
                           // }
                        } catch (Exception e)

                        {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                            progressBar1.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                        progressBar1.setVisibility(View.GONE);

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
        //Log.e("saleFirstVisibleItem in Api",""+saleFirstVisibleItem);


        String url = ConstsCore.web_url + "/v1/display/salesanalysisbytime/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit;

        Log.e("Url", "" + url);
        postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("view pager response", "" + response);

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                               // Style_loadingBar.setVisibility(View.GONE);
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

//                            for (int i = 0; i < 3; i++) {
////
//                                ImageView imgdot = new ImageView(context);
//                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
//                                layoutParams.setMargins(3, 3, 3, 3);
//                                imgdot.setLayoutParams(layoutParams);
//                                imgdot.setImageResource(R.mipmap.dots_unselected);
//                                lldots.addView(imgdot);
//                                ImageView img = (ImageView) lldots.getChildAt(0);
//                                img.setImageResource(R.mipmap.dots_selected);
//                            }

                            //Log.i("-------",""+analysisArrayList.size());


                            Log.i("----getCurrentItem---", "" + vwpagersales.getCurrentItem());
                            pageradapter = new SalesPagerAdapter(context, analysisArrayList, focusposition, vwpagersales, lldots, salesadapter, listView_SalesAnalysis, salesAnalysisClassArrayList, fromWhere, pageradapter);
                            vwpagersales.setAdapter(pageradapter);
                            vwpagersales.setCurrentItem(currentVmPos);
                            pageradapter.notifyDataSetChanged();

                            // llayoutSalesAnalysis.setVisibility(View.VISIBLE);
                            // Reusable_Functions.hDialog();
                         //   Style_loadingBar.setVisibility(View.GONE);
                            progressBar1.setVisibility(View.GONE);


                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                          //  Style_loadingBar.setVisibility(View.GONE);
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
                       // Style_loadingBar.setVisibility(View.GONE);
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
        //  scrolling = false;
        Log.e("Department onsroll api", "" + saleFirstVisibleItem);

        String url = "";

        if (txtheaderplanclass.getText().toString().equals("Department")) {
            url = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + selectedsegValue + "&department=" + saleFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        } else if (txtheaderplanclass.getText().toString().equals("Category")) {
            url = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + selectedsegValue + "&category=" + saleFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        } else if (txtheaderplanclass.getText().toString().equals("Plan Class")) {
            url = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + selectedsegValue + "&class=" + saleFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        } else if (txtheaderplanclass.getText().toString().equals("Brand")) {
            url = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + selectedsegValue + "&brand=" + saleFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        } else if (txtheaderplanclass.getText().toString().equals("Brand Plan Class")) {
            url = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + selectedsegValue + "&brandclass=" + saleFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        }
        Log.e("Url", "" + url);

        postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Sales Analysis by time : ", " " + response);
                        Log.e("Sales View Pager response", "" + response.length());
                        try {
                            int valuePos = focusposition;
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                progressBar1.setVisibility(View.GONE);
                               // Style_loadingBar.setVisibility(View.GONE);

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
                                Log.e("analysisArrayList", " --- " + analysisArrayList.size());

//                                for (int i = 0; i < 3; i++) {
//                                    ImageView imgdot = new ImageView(context);
//                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
//                                    layoutParams.setMargins(3, 3, 3, 3);
//                                    imgdot.setLayoutParams(layoutParams);
//                                    imgdot.setImageResource(R.mipmap.dots_unselected);
//                                    lldots.addView(imgdot);
//                                    ImageView img = (ImageView) lldots.getChildAt(0);
//                                    img.setImageResource(R.mipmap.dots_selected);
//                                }
                                pageradapter = new SalesPagerAdapter(context, analysisArrayList, focusposition, vwpagersales, lldots, salesadapter, listView_SalesAnalysis, salesAnalysisClassArrayList, fromWhere, pageradapter);
                                //Log.i("-------",""+analysisArrayList.size());
                                //   if(valuePos == focusposition) {
                                vwpagersales.setAdapter(pageradapter);
                                vwpagersales.setCurrentItem(currentVmPos);
                                pageradapter.notifyDataSetChanged();
                                Log.e("Focus position on scroll ", "" + focusposition);
                                progressBar1.setVisibility(View.GONE);
                                Reusable_Functions.hDialog();
                             //   Style_loadingBar.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                            progressBar1.setVisibility(View.GONE);
                         //   Style_loadingBar.setVisibility(View.GONE);

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
                      //  Style_loadingBar.setVisibility(View.GONE);

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
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }

    private void requestSalesCategoryList(final String deptName) {

        String salespvacategory_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&department=" + deptName.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + salespvacategory_listurl);

     postRequest = new JsonArrayRequest(Request.Method.GET, salespvacategory_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Sales Category List: ", " " + response);
                        Log.i("Sales Category List response length", "" + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no category data found", Toast.LENGTH_SHORT).show();
                             //   Style_loadingBar.setVisibility(View.GONE);
                                progressBar1.setVisibility(View.GONE);

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
                                Log.e("----", " " + vwpagersales.getCurrentItem());
                                ImageView img = (ImageView) lldots.getChildAt(currentItem);
                                img.setImageResource(R.mipmap.dots_selected);


                                listView_SalesAnalysis.setLayoutManager(new LinearLayoutManager(
                                        listView_SalesAnalysis.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                listView_SalesAnalysis.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(listView_SalesAnalysis);

                                salesadapter = new SalesAnalysisSnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listView_SalesAnalysis);
                                listView_SalesAnalysis.setAdapter(salesadapter);
                                flag = true;
                                salesadapter.notifyDataSetChanged();
                                onClickFlag = false;
                                txtStoreCode.setText(salesAnalysisClassArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(salesAnalysisClassArrayList.get(0).getStoreDesc());
                                val = "";
                                val = deptName;
                                txthDeptName.setText(val);
                                llhierarchy.setVisibility(View.VISIBLE);
                                // llayoutSalesAnalysis.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                                saleFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanCategory();
                                Log.e("saleFirstVisibleItem in category list", "-----" + saleFirstVisibleItem);
                                requestSalesPagerOnScrollAPI();
                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no category data found", Toast.LENGTH_SHORT).show();
                            progressBar1.setVisibility(View.GONE);

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no category data found", Toast.LENGTH_SHORT).show();
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

    private void requestSalesPlanClassListAPI(String deptName, final String category) {

        Log.e("planDeptin plan class", "----" + planDept);

        String salespva_planclass_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&department=" + planDept.replaceAll(" ", "%20").replaceAll("&", "%26") + "&category=" + category.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + salespva_planclass_listurl);

        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_planclass_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Sales Plan Class List : ", " " + response);
                        Log.i("Sales Plan Class List response length", "" + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no plan class data found", Toast.LENGTH_SHORT).show();
                                progressBar1.setVisibility(View.GONE);

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisClass = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisClass);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesPlanClassListAPI(planDept, category);

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
                                Log.e("----", " " + vwpagersales.getCurrentItem());
                                ImageView img = (ImageView) lldots.getChildAt(currentItem);
                                img.setImageResource(R.mipmap.dots_selected);


                                listView_SalesAnalysis.setLayoutManager(new LinearLayoutManager(
                                        listView_SalesAnalysis.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                listView_SalesAnalysis.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(listView_SalesAnalysis);

                                salesadapter = new SalesAnalysisSnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listView_SalesAnalysis);
                                listView_SalesAnalysis.setAdapter(salesadapter);
                                flag = true;
                                salesadapter.notifyDataSetChanged();
                                onClickFlag = false;
                                txtStoreCode.setText(salesAnalysisClassArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(salesAnalysisClassArrayList.get(0).getStoreDesc());
                                // String categry = category.substring(0,1).toUpperCase()+category.substring(1).toLowerCase();
                                val += " > " + category;
                                txthDeptName.setText(val);
                                //txthCategory.setText(category);
                                llhierarchy.setVisibility(View.VISIBLE);
                                //llayoutSalesAnalysis.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                                saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisiblePosition).getPlanClass();
                                Log.e("saleFirstVisibleItem in plan class list", "-----" + saleFirstVisibleItem);
                                requestSalesPagerOnScrollAPI();
                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no plan class data found", Toast.LENGTH_SHORT).show();
                            progressBar1.setVisibility(View.GONE);

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no  plan class data found", Toast.LENGTH_SHORT).show();
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

    private void requestSalesBrandListAPI(String deptName, String category, final String planclass) {

        String salespva_brand_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&department=" + planDept.replaceAll(" ", "%20").replaceAll("&", "%26") + "&category=" + planCategory.replaceAll(" ", "%20").replaceAll("&", "%26") + "&class=" + planclass.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + salespva_brand_listurl);

        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_brand_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Sales brand List : ", " " + response);
                        Log.i("Sales brand List response length", "" + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no brand name data found", Toast.LENGTH_SHORT).show();
                                progressBar1.setVisibility(View.GONE);

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisClass = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisClass);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesBrandListAPI(planDept, planCategory, planclass);

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
                                Log.e("----", " " + vwpagersales.getCurrentItem());
                                ImageView img = (ImageView) lldots.getChildAt(currentItem);
                                img.setImageResource(R.mipmap.dots_selected);


                                listView_SalesAnalysis.setLayoutManager(new LinearLayoutManager(
                                        listView_SalesAnalysis.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                listView_SalesAnalysis.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(listView_SalesAnalysis);

                                salesadapter = new SalesAnalysisSnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listView_SalesAnalysis);
                                listView_SalesAnalysis.setAdapter(salesadapter);
                                salesadapter.notifyDataSetChanged();
                                flag = true;
                                txtStoreCode.setText(salesAnalysisClassArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(salesAnalysisClassArrayList.get(0).getStoreDesc());
                                onClickFlag = false;
                                // String plnCls = planclass.substring(0,1).toUpperCase()+planclass.substring(1).toLowerCase();
                                val += " > " + planclass;
                                txthDeptName.setText(val);
                                llhierarchy.setVisibility(View.VISIBLE);
//                                llayoutSalesAnalysis.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                                saleFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getBrandName();
                                Log.e("saleFirstVisibleItem in brand list", "-----" + saleFirstVisibleItem);
                                requestSalesPagerOnScrollAPI();
                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no brand name data found", Toast.LENGTH_SHORT).show();
                            progressBar1.setVisibility(View.GONE);

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no brand name data found", Toast.LENGTH_SHORT).show();
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

    private void requestSalesBrandPlanListAPI(String deptName, String category, final String plan_class, final String brandnm) {

        String salespva_brandplan_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&department=" + planDept.replaceAll(" ", "%20").replaceAll("&", "%26") + "&category=" + planCategory.replaceAll(" ", "%20").replaceAll("&", "%26") + "&class=" + planClass.replaceAll(" ", "%20").replaceAll("&", "%26") + "&brand=" + brandnm.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + salespva_brandplan_listurl);

        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_brandplan_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Sales Brand Plan Class List : ", " " + response);
                        Log.i("Sales Brand Plan Class List response length", "" + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no brand plan class data found", Toast.LENGTH_SHORT).show();
                                progressBar1.setVisibility(View.GONE);


                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisClass = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisClass);

                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesBrandPlanListAPI(planDept, planCategory, planClass, brandnm);

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
                                Log.e("----", " " + vwpagersales.getCurrentItem());
                                ImageView img = (ImageView) lldots.getChildAt(currentItem);
                                img.setImageResource(R.mipmap.dots_selected);


                                listView_SalesAnalysis.setLayoutManager(new LinearLayoutManager(
                                        listView_SalesAnalysis.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                listView_SalesAnalysis.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(listView_SalesAnalysis);

                                salesadapter = new SalesAnalysisSnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listView_SalesAnalysis);
                                listView_SalesAnalysis.setAdapter(salesadapter);
                                salesadapter.notifyDataSetChanged();
                                onClickFlag = false;

                                txtStoreCode.setText(salesAnalysisClassArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(salesAnalysisClassArrayList.get(0).getStoreDesc());
                                String brnd = brandnm.substring(0, 1).toUpperCase() + brandnm.substring(1).toLowerCase();
                                val += " > " + brandnm;
                                txthDeptName.setText(val);
                                llhierarchy.setVisibility(View.VISIBLE);
                                // llayoutSalesAnalysis.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                                saleFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getBrandplanClass();
                                Log.e("saleFirstVisibleItem in brandplanclass list", "-----" + saleFirstVisibleItem);
                                requestSalesPagerOnScrollAPI();
                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no brand plan class data found", Toast.LENGTH_SHORT).show();
                            progressBar1.setVisibility(View.GONE);

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no brand plan class data found", Toast.LENGTH_SHORT).show();
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

    private void requestSalesSelectedFilterVal() {
        String salespva_brandplan_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&department=" + saleFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + salespva_brandplan_listurl);

        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_brandplan_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Sales List : ", " " + response);
                        Log.i("Sales List response length", "" + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                progressBar1.setVisibility(View.GONE);


                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisClass = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisClass);

                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesSelectedFilterVal();

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
                                Log.e("----", " " + vwpagersales.getCurrentItem());
                                ImageView img = (ImageView) lldots.getChildAt(currentItem);
                                img.setImageResource(R.mipmap.dots_selected);

                                salesadapter = new SalesAnalysisSnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listView_SalesAnalysis);
                                listView_SalesAnalysis.setAdapter(salesadapter);
                                salesadapter.notifyDataSetChanged();

                                txtStoreCode.setText(salesAnalysisClassArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(salesAnalysisClassArrayList.get(0).getStoreDesc());

                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                                saleFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanDept();
                                Log.e("saleFirstVisibleItem in brandplanclass list", "-----" + saleFirstVisibleItem);
                                requestSalesPagerOnScrollAPI();
                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
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


    @Override
    public void onBackPressed() {

//        if (rankLayout.getVisibility() == View.VISIBLE) {
//            rankLayout.setVisibility(View.GONE);
//        } else {
//            SalesPagerAdapter.currentPage = 0;
            /*Intent i = new Intent(SalesAnalysisActivity.this, DashBoardActivity.class);
            startActivity(i);*/
        finish();
        // }

    }

    public void updatelistView(double pvaachieved, Context context) {
        SalesAnalysisListDisplay salesAnalysisClass = salesAnalysisClassArrayList.get(0);
        salesAnalysisClass.setPvaAchieved(pvaachieved);
        salesAnalysisClassArrayList.add(0, salesAnalysisClass);
        salesadapter = new SalesAnalysisSnapAdapter(salesAnalysisClassArrayList, context, currentIndex, fromWhere, listView_SalesAnalysis);
        listView_SalesAnalysis.setAdapter(salesadapter);


    }

    @Override
    public void onClick(View view, int position) {
        SalesAnalysisListDisplay salesAnalysisListDisplay = salesAnalysisClassArrayList.get(position);
        Log.e(TAG, "onClick: " + salesAnalysisListDisplay);

    }
}
