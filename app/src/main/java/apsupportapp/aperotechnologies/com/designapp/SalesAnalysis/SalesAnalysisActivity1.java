package apsupportapp.aperotechnologies.com.designapp.SalesAnalysis;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.util.Log;
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
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import apsupportapp.aperotechnologies.com.designapp.ConstsCore;

import apsupportapp.aperotechnologies.com.designapp.Ezone.EzoneSalesAdapter;
import apsupportapp.aperotechnologies.com.designapp.Ezone.EzoneSalesFilter;
import apsupportapp.aperotechnologies.com.designapp.Ezone.EzoneSalesPagerAdapter;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.RunningPromo.RecyclerViewPositionHelper;
import apsupportapp.aperotechnologies.com.designapp.model.RecyclerItemClickListener;

import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisListDisplay;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisViewPagerValue;
import info.hoang8f.android.segmented.SegmentedGroup;




public class SalesAnalysisActivity1 extends AppCompatActivity implements View.OnClickListener, TabLayout.OnTabSelectedListener
{
    JsonArrayRequest postRequest;
    ArrayList<SalesAnalysisViewPagerValue> analysisArrayList, salesList;
    ArrayList<SalesAnalysisListDisplay> salesAnalysisClassArrayList;
    SalesAnalysisListDisplay salesAnalysisClass;
    SalesAnalysisSnapAdapter salesadapter;

    public SalesAnalysisViewPagerValue salesAnalysis;
    SalesPagerAdapter pageradapter;

    Gson gson;
    public static Activity SalesAnalysisActivity;
    Context context;
    String planDept, planCategory, planClass;
    // Fashion At BB Elements Declaration
    SegmentedGroup segmentedGroupSales;
    ViewPager vwpagersales;
    LinearLayout lldots, llhierarchy, llayoutSalesAnalysis;
    RecyclerView listView_SalesAnalysis;
    SharedPreferences sharedPreferences;
    String userId, bearertoken, geoLeveLDesc,storeDescription,geoLevel2Code, lobId,selectedString,isMultiStore,value;
    EditText etListText;
    RadioButton btnWTD, btnL4W, btnLW, btnYTD;
    public static String selectedsegValue = "WTD";
    String saleFirstVisibleItem, fromWhere = "Department", val, txtSalesClickedValue,all_from_val;
    TextView txtStoreCode, txtStoreDesc, txtheaderplanclass, txthDeptName;

    public static int level = 1;
    RequestQueue queue;
    RelativeLayout relimgrank, relimgfilter, relprevbtn, relnextbtn, relLayoutSales, btnBack,relReset;
    int selFirstPositionValue = 0, currentVmPos, totalItemCount, firstVisibleItem, offsetvalue = 0, limit = 100, count = 0, currentState = RecyclerView.SCROLL_STATE_IDLE, prevState = RecyclerView.SCROLL_STATE_IDLE;
    private boolean onClickFlag = false, filter_toggleClick = false,from_filter = false;
    ProgressBar progressBar1;
    TabLayout Tabview;
    private String planDeptNm,planCategoryNm,planBrandNm,planClassNm;
    RelativeLayout relStoreLayout;

    public int sales_filter_level;
    private String header_value,drill_down_val;
    private String[] hierarchyList;
    private String TAG="SalesAnalysisActivity1";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_analysis1);
        context = this;
        SalesAnalysisActivity = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLeveLDesc = sharedPreferences.getString("geoLeveLDesc", "");
        storeDescription = sharedPreferences.getString("storeDescription","");
        geoLevel2Code = sharedPreferences.getString("concept","");
        lobId = sharedPreferences.getString("lobid","");
        isMultiStore = sharedPreferences.getString("isMultiStore","");
        value = sharedPreferences.getString("value","");
        final String hierarchyLevels = sharedPreferences.getString("hierarchyLevels", "");
        // replace all labels using hierarchyList
        hierarchyList = hierarchyLevels.split(",");
        for (int i = 0; i <hierarchyList.length ; i++) {
            hierarchyList[i]=hierarchyList[i].trim();
            Log.i(TAG, "hierarchyList: "+hierarchyList[i]);
        }
        Log.e("lobId "," "+lobId);
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();
        header_value = "";
        drill_down_val = "";
        from_filter = false;
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
          // when geoLevelDesc is FBB
            getSupportActionBar().hide();
            initialize_fbb_ui();
        if (Reusable_Functions.chkStatus(context))
        {
            Reusable_Functions.sDialog(context, "Loading...");
            progressBar1.setVisibility(View.GONE);
            llhierarchy.setVisibility(View.GONE);
            offsetvalue = 0;
            limit = 100;
            count = 0;
            level = 1;
            if (getIntent().getStringExtra("selectedStringVal") == null)
            {
                from_filter = false;
                filter_toggleClick = false;
                retainSegmentValuesFilter();
                requestSalesListDisplayAPI();
            }
            else if (getIntent().getStringExtra("selectedStringVal") != null)
            {
                header_value  = getIntent().getStringExtra("selectedStringVal");
               sales_filter_level = getIntent().getIntExtra("selectedlevelVal",0);
                filter_toggleClick = true;
                retainSegmentValuesFilter();
                onClickFlag = false;
                from_filter = true;
                drill_down_val = "";
                drill_down_val = getIntent().getStringExtra("selectedStringVal");
                requestSalesSelectedFilterVal(header_value,sales_filter_level);
            }
        }
        else
        {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
            progressBar1.setVisibility(View.GONE);
        }
            // scroll event on reccycle view (Fashion At BB)
            listView_SalesAnalysis.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    RecyclerViewPositionHelper mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
                    totalItemCount = mRecyclerViewHelper.getItemCount();
                    firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState)
                {
                    super.onScrollStateChanged(recyclerView, newState);
                    currentState = newState;
                    if (prevState != RecyclerView.SCROLL_STATE_IDLE && currentState == RecyclerView.SCROLL_STATE_IDLE && !onClickFlag) {
                        Handler h = new Handler();
                        h.postDelayed(new Runnable()
                        {
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
                        public void onItemClick(View view, final int position)
                        {
                            selFirstPositionValue = 0;
                            firstVisibleItem = 0;
                            if (progressBar1.getVisibility() == View.VISIBLE) {
                                return;
                            }//002201520115--icic0000022
                            else
                            {
//                                drill_down_val = "";
                                onClickFlag = true;
                                Handler h = new Handler();
                                h.postDelayed(new Runnable() {
                                    public void run() {

                                        if (position < salesAnalysisClassArrayList.size()) {


                                            if(txtheaderplanclass.getText().toString().equals(hierarchyList[0])){

                                                relprevbtn.setVisibility(View.VISIBLE);
                                                txtheaderplanclass.setText(hierarchyList[1]);
                                                txtSalesClickedValue = salesAnalysisClassArrayList.get(position).getPlanDept();
                                                fromWhere = hierarchyList[1];
                                                if(!txtSalesClickedValue.equals("All")) {
                                                    txtSalesClickedValue = txtSalesClickedValue.replace("%", "%25");
                                                    txtSalesClickedValue = txtSalesClickedValue.replace(" ", "%20").replace("&", "%26");
                                                    header_value = "&department=" + txtSalesClickedValue;
                                                }
                                                else
                                                {
                                                    header_value = "";
                                                }
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

                                            }
                                            else if(txtheaderplanclass.getText().toString().equals(hierarchyList[1])){

                                                txtheaderplanclass.setText(hierarchyList[2]);
                                                txtSalesClickedValue = salesAnalysisClassArrayList.get(position).getPlanCategory();
                                                if(!txtSalesClickedValue.equals("All")) {
                                                    txtSalesClickedValue = txtSalesClickedValue.replace("%", "%25");
                                                    txtSalesClickedValue = txtSalesClickedValue.replace(" ", "%20").replace("&", "%26");
                                                    header_value = "&category=" + txtSalesClickedValue;
                                                }
                                                else
                                                {
                                                    header_value = "";
                                                }
                                                fromWhere = hierarchyList[2];
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

                                            }
                                            else if(txtheaderplanclass.getText().toString().equals(hierarchyList[2])){

                                                txtheaderplanclass.setText(hierarchyList[3]);
                                                txtSalesClickedValue = salesAnalysisClassArrayList.get(position).getPlanClass();
                                                if(!txtSalesClickedValue.equals("All")) {
                                                    txtSalesClickedValue = txtSalesClickedValue.replace("%", "%25");
                                                    txtSalesClickedValue = txtSalesClickedValue.replace(" ", "%20").replace("&", "%26");
                                                    header_value = "&class=" + txtSalesClickedValue;
                                                }
                                                else
                                                {
                                                    header_value = "";
                                                }
                                                fromWhere = hierarchyList[3];
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

                                            }

                                            else if(txtheaderplanclass.getText().toString().equals(hierarchyList[3])){

                                                relnextbtn.setVisibility(View.INVISIBLE);
                                                txtheaderplanclass.setText(hierarchyList[4]);
                                                txtSalesClickedValue = salesAnalysisClassArrayList.get(position).getBrandName();
                                                if(!txtSalesClickedValue.equals("All")) {
                                                    txtSalesClickedValue = txtSalesClickedValue.replace("%", "%25");
                                                    txtSalesClickedValue = txtSalesClickedValue.replace(" ", "%20").replace("&", "%26");
                                                    header_value = "&brand=" + txtSalesClickedValue;
                                                }
                                                else
                                                {
                                                    header_value = "";
                                                }
                                                fromWhere = hierarchyList[4];
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

                                            }
                                            else{

                                                Reusable_Functions.hDialog();
                                                Toast.makeText(context, " You are at the last level of hierarchy", Toast.LENGTH_SHORT).show();
                                                onClickFlag = false;
                                            }


                                           /* switch (txtheaderplanclass.getText().toString()) {
                                                case "Department":
                                                    relprevbtn.setVisibility(View.VISIBLE);
                                                    txtheaderplanclass.setText("Category");
                                                    txtSalesClickedValue = salesAnalysisClassArrayList.get(position).getPlanDept();
                                                    fromWhere = "Category";
                                                    if(!txtSalesClickedValue.equals("All")) {
                                                        txtSalesClickedValue = txtSalesClickedValue.replace("%", "%25");
                                                        txtSalesClickedValue = txtSalesClickedValue.replace(" ", "%20").replace("&", "%26");
                                                        header_value = "&department=" + txtSalesClickedValue;
                                                    }
                                                    else
                                                    {
                                                        header_value = "";
                                                    }
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

                                                case "Category":
                                                    txtheaderplanclass.setText("Class");
                                                    txtSalesClickedValue = salesAnalysisClassArrayList.get(position).getPlanCategory();
                                                    if(!txtSalesClickedValue.equals("All")) {
                                                        txtSalesClickedValue = txtSalesClickedValue.replace("%", "%25");
                                                        txtSalesClickedValue = txtSalesClickedValue.replace(" ", "%20").replace("&", "%26");
                                                        header_value = "&category=" + txtSalesClickedValue;
                                                    }
                                                    else
                                                    {
                                                        header_value = "";
                                                    }
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
                                                    txtheaderplanclass.setText("Brand");
                                                    txtSalesClickedValue = salesAnalysisClassArrayList.get(position).getPlanClass();
                                                    if(!txtSalesClickedValue.equals("All")) {
                                                        txtSalesClickedValue = txtSalesClickedValue.replace("%", "%25");
                                                        txtSalesClickedValue = txtSalesClickedValue.replace(" ", "%20").replace("&", "%26");
                                                        header_value = "&class=" + txtSalesClickedValue;
                                                    }
                                                    else
                                                    {
                                                        header_value = "";
                                                    }
                                                    fromWhere = "Brand";
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

                                                case "Brand":
                                                    relnextbtn.setVisibility(View.INVISIBLE);
                                                    txtheaderplanclass.setText("Brand Class");
                                                    txtSalesClickedValue = salesAnalysisClassArrayList.get(position).getBrandName();
                                                    if(!txtSalesClickedValue.equals("All")) {
                                                        txtSalesClickedValue = txtSalesClickedValue.replace("%", "%25");
                                                        txtSalesClickedValue = txtSalesClickedValue.replace(" ", "%20").replace("&", "%26");
                                                        header_value = "&brand=" + txtSalesClickedValue;
                                                    }
                                                    else
                                                    {
                                                        header_value = "";
                                                    }
                                                    fromWhere = "Brand Class";
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
                                            }*/
                                        }
                                    }
                                }, 700);
                            }
                        }
                    }));
//        }
    }



    private void initialize_fbb_ui() {
        fromWhere = hierarchyList[0];
        txtSalesClickedValue = " ";
        val = "";
        firstVisibleItem = 0;
//        etListText = (EditText) findViewById(R.id.etListText);
        relStoreLayout = (RelativeLayout)findViewById(R.id.relStoreLayout);
        relStoreLayout.setVisibility(View.GONE);
        txtStoreCode = (TextView) findViewById(R.id.txtStoreCode);
        txtStoreDesc = (TextView) findViewById(R.id.txtStoreName);
//        if(isMultiStore.equals("Yes"))
//        {
//            txtStoreCode.setText("Concept : ");
//            txtStoreDesc.setText(value);
//
//        }
//        else
//        {
//            txtStoreCode.setText("Store : ");
//            txtStoreDesc.setText(value);
//        }
//        Log.e( "initialize_fbb_ui: ", ""+storeDescription.trim().substring(0,4));
//        txtStoreCode.setText(storeDescription.trim().substring(0,4));
//        txtStoreDesc.setText(storeDescription.substring(5));
        //hierarchy header
        txthDeptName = (TextView) findViewById(R.id.txthDeptName);
        btnBack = (RelativeLayout) findViewById(R.id.imageBtnBack);
        btnBack.setOnClickListener(this);
        segmentedGroupSales = (SegmentedGroup) findViewById(R.id.segmentedGrp);
        btnWTD = (RadioButton) findViewById(R.id.btnWTD);
        btnLW = (RadioButton) findViewById(R.id.btnLW);
        btnL4W = (RadioButton) findViewById(R.id.btnL4W);
        btnYTD = (RadioButton) findViewById(R.id.btnYTD);
        Tabview = (TabLayout) findViewById(R.id.tabview_sales);
        Tabview.addTab(Tabview.newTab().setText("WTD"), 0);
        Tabview.addTab(Tabview.newTab().setText("LW"), 1);
        Tabview.addTab(Tabview.newTab().setText("L4W"), 2);
        Tabview.addTab(Tabview.newTab().setText("STD"), 3);
        Tabview.setOnTabSelectedListener(this);
        llayoutSalesAnalysis = (LinearLayout) findViewById(R.id.llayoutSalesAnalysis);
        relimgfilter = (RelativeLayout) findViewById(R.id.imgfilter);
        relimgfilter.setOnClickListener(this);
        //   relimgrank = (RelativeLayout) findViewById(R.id.imgrank);
        relprevbtn = (RelativeLayout) findViewById(R.id.prevplanclass);
        relprevbtn.setVisibility(View.INVISIBLE);
        relprevbtn.setOnClickListener(this);
        relnextbtn = (RelativeLayout) findViewById(R.id.nextplanclass);
        relnextbtn.setOnClickListener(this);
        txtheaderplanclass = (TextView) findViewById(R.id.headerplanclass);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        txtheaderplanclass.setText(hierarchyList[0]);
        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
        analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
        salesList = new ArrayList<SalesAnalysisViewPagerValue>();
        vwpagersales = (ViewPager) findViewById(R.id.viewpager);
        pageradapter = new SalesPagerAdapter();
        pageradapter.notifyDataSetChanged();
        lldots = (LinearLayout) findViewById(R.id.lldots);
        lldots.setOrientation(LinearLayout.HORIZONTAL);

        TabLayout tab = (TabLayout) findViewById(R.id.dotTab);
        tab.setupWithViewPager(vwpagersales, true);
        llhierarchy = (LinearLayout) findViewById(R.id.llhierarchy);
        llhierarchy.setOrientation(LinearLayout.HORIZONTAL);
        relLayoutSales = (RelativeLayout) findViewById(R.id.relTablelayout);
        listView_SalesAnalysis = (RecyclerView) findViewById(R.id.listView_SalesAnalysis);
        salesadapter = new SalesAnalysisSnapAdapter();
        relReset = (RelativeLayout)findViewById(R.id.imgReset);
        relReset.setOnClickListener(this);
        final RelativeLayout rel_overlay = (RelativeLayout) findViewById(R.id.rel_overlay);
        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        rel_overlay.setOnClickListener(null);
        menuMultipleActions.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                rel_overlay.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMenuCollapsed() {
                rel_overlay.setVisibility(View.GONE);
            }
        });

        final FloatingActionButton action_department = (FloatingActionButton) findViewById(R.id.action_department);
        action_department.setSize(FloatingActionButton.SIZE_MINI);
        action_department.setColorNormalResId(R.color.pink);
        action_department.setColorPressedResId(R.color.ezfb_Red);
        action_department.setIcon(R.drawable.fabicon_department);
        action_department.setStrokeVisible(false);

        action_department.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selFirstPositionValue = 0; firstVisibleItem = 0;
                if (Reusable_Functions.chkStatus(context))
                {
                    Reusable_Functions.sDialog(context, "Loading...");
                    progressBar1.setVisibility(View.GONE);
                    if(drill_down_val.equals(getIntent().getStringExtra("selectedStringVal")) || drill_down_val.equals(""))
                    {
                        llhierarchy.setVisibility(View.GONE);

                    }
                    else
                    {
                        llhierarchy.setVisibility(View.VISIBLE);
                    }
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    level = 1;
                    relprevbtn.setVisibility(View.INVISIBLE);
                    relnextbtn.setVisibility(View.VISIBLE);
                    txtheaderplanclass.setText(hierarchyList[0]);
                    salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                    fromWhere = hierarchyList[0];
                    retainSegmentValuesFilter();
                    if(!drill_down_val.equals(""))
                    {

                        requestViewByDisplay(drill_down_val);
                    }


                    else
                    {
                        Log.e("onClick: ", "in view dept");
                        requestSalesListDisplayAPI();
                    }

                }
                else
                {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    progressBar1.setVisibility(View.GONE);
                    Reusable_Functions.hDialog();
                }
                menuMultipleActions.collapse();
            }
        });

        final FloatingActionButton action_category = (FloatingActionButton) findViewById(R.id.action_category);
        action_category.setSize(FloatingActionButton.SIZE_MINI);
        action_category.setColorNormalResId(R.color.pink);
        action_category.setColorPressedResId(R.color.ezfb_Red);
        action_category.setIcon(R.drawable.fabicon_category);
        action_category.setStrokeVisible(false);

        action_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                fromWhere = hierarchyList[1];
                selFirstPositionValue = 0; firstVisibleItem = 0;

                if (Reusable_Functions.chkStatus(context))
                {
                    Reusable_Functions.sDialog(context, "Loading...");
                    progressBar1.setVisibility(View.GONE);
                    if(drill_down_val.equals(getIntent().getStringExtra("selectedStringVal")) || drill_down_val.equals(""))
                    {
                        llhierarchy.setVisibility(View.GONE);

                    }
                    else
                    {
                        llhierarchy.setVisibility(View.VISIBLE);
                    }
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    level = 2;
                    relprevbtn.setVisibility(View.VISIBLE);
                    relnextbtn.setVisibility(View.VISIBLE);
                    txtheaderplanclass.setText(hierarchyList[1]);
                    retainSegmentValuesFilter();
                    if(!drill_down_val.equals(""))
                    {

                        requestViewByDisplay(drill_down_val);
                    }
                    else
                    {
                        Log.e("onClick: ", "in view category");
                        requestSalesListDisplayAPI();
                    }
                }
                else
                {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    progressBar1.setVisibility(View.GONE);
                    Reusable_Functions.hDialog();
                }
                menuMultipleActions.collapse();

            }
        });

        final FloatingActionButton action_class = (FloatingActionButton) findViewById(R.id.action_class);
        action_class.setSize(FloatingActionButton.SIZE_MINI);
        action_class.setColorNormalResId(R.color.pink);
        action_class.setColorPressedResId(R.color.ezfb_Red);
        action_class.setIcon(R.drawable.fabicon_class);
        action_class.setStrokeVisible(false);

        action_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                fromWhere = hierarchyList[2];
                selFirstPositionValue = 0; firstVisibleItem = 0;

                if (Reusable_Functions.chkStatus(context))
                {
                    Reusable_Functions.sDialog(context, "Loading...");
                    progressBar1.setVisibility(View.GONE);
                    if(drill_down_val.equals(getIntent().getStringExtra("selectedStringVal")) || drill_down_val.equals(""))
                    {
                        llhierarchy.setVisibility(View.GONE);

                    }
                    else
                    {
                        llhierarchy.setVisibility(View.VISIBLE);
                    }
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    level = 3;
                    relprevbtn.setVisibility(View.VISIBLE);
                    relnextbtn.setVisibility(View.VISIBLE);
                    txtheaderplanclass.setText(hierarchyList[2]);
                    retainSegmentValuesFilter();
                    if(!drill_down_val.equals(""))
                    {

                        requestViewByDisplay(drill_down_val);
                    }
                    else
                    {
                        Log.e("onClick: ", "in view class");
                        requestSalesListDisplayAPI();
                    }

                }
                else
                {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    progressBar1.setVisibility(View.GONE);
                    Reusable_Functions.hDialog();
                }
                menuMultipleActions.collapse();
            }
        });

        final FloatingActionButton action_brand = (FloatingActionButton) findViewById(R.id.action_brand);
        action_brand.setSize(FloatingActionButton.SIZE_MINI);
        action_brand.setColorNormalResId(R.color.pink);
        action_brand.setColorPressedResId(R.color.ezfb_Red);
        action_brand.setIcon(R.drawable.fabicon_brand);
        action_brand.setStrokeVisible(false);

        action_brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Reusable_Functions.chkStatus(context))
                {
                    salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                    fromWhere = hierarchyList[3];
                    selFirstPositionValue = 0; firstVisibleItem = 0;

                    Reusable_Functions.sDialog(context, "Loading...");
                    progressBar1.setVisibility(View.GONE);
                    if(drill_down_val.equals(getIntent().getStringExtra("selectedStringVal")) || drill_down_val.equals(""))
                    {
                        llhierarchy.setVisibility(View.GONE);

                    }
                    else
                    {
                        llhierarchy.setVisibility(View.VISIBLE);
                    }
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    level = 4;
                    relprevbtn.setVisibility(View.VISIBLE);
                    relnextbtn.setVisibility(View.VISIBLE);
                    txtheaderplanclass.setText(hierarchyList[3]);
                    retainSegmentValuesFilter();
                    if(!drill_down_val.equals(""))
                    {

                        requestViewByDisplay(drill_down_val);
                    }
                    else
                    {
                        Log.e("onClick: ", "in view brand");
                        requestSalesListDisplayAPI();
                    }

                }
                else
                {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    progressBar1.setVisibility(View.GONE);
                    Reusable_Functions.hDialog();
                }
                menuMultipleActions.collapse();
            }
        });

        final FloatingActionButton action_brandclass = (FloatingActionButton) findViewById(R.id.action_brandclass);
        action_brandclass.setSize(FloatingActionButton.SIZE_MINI);
        action_brandclass.setColorNormalResId(R.color.pink);
        action_brandclass.setColorPressedResId(R.color.ezfb_Red);
        action_brandclass.setIcon(R.drawable.fabicon_brand_class);
        action_brandclass.setStrokeVisible(false);

        action_brandclass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                fromWhere = hierarchyList[4];
                selFirstPositionValue = 0; firstVisibleItem = 0;

                if (Reusable_Functions.chkStatus(context))
                {
                    Reusable_Functions.sDialog(context, "Loading...");
                    progressBar1.setVisibility(View.GONE);
                    if(drill_down_val.equals(getIntent().getStringExtra("selectedStringVal")) || drill_down_val.equals(""))
                    {
                        llhierarchy.setVisibility(View.GONE);

                    }
                    else
                    {
                        llhierarchy.setVisibility(View.VISIBLE);
                    }
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    level = 5;
                    relprevbtn.setVisibility(View.VISIBLE);
                    relnextbtn.setVisibility(View.INVISIBLE);
                    txtheaderplanclass.setText(hierarchyList[4]);
                    retainSegmentValuesFilter();
                    if(!drill_down_val.equals(""))
                    {

                        requestViewByDisplay(drill_down_val);
                    }
                    else
                    {
                        Log.e("onClick: ", "in view brand class");
                        requestSalesListDisplayAPI();
                    }

                }
                else
                {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    progressBar1.setVisibility(View.GONE);
                    Reusable_Functions.hDialog();
                }
                menuMultipleActions.collapse();
            }
        });

        final FloatingActionButton action_store = (FloatingActionButton) findViewById(R.id.action_store);
        action_store.setSize(FloatingActionButton.SIZE_MINI);
        action_store.setColorNormalResId(R.color.pink);
        action_store.setColorPressedResId(R.color.ezfb_Red);
        action_store.setIcon(R.drawable.fabicon_store);
        action_store.setStrokeVisible(false);

        action_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                fromWhere = "Store";
                selFirstPositionValue = 0; firstVisibleItem = 0;

                if (Reusable_Functions.chkStatus(context))
                {
                    Reusable_Functions.sDialog(context, "Loading...");
                    progressBar1.setVisibility(View.GONE);
                    if(drill_down_val.equals(getIntent().getStringExtra("selectedStringVal")) || drill_down_val.equals(""))
                    {
                        llhierarchy.setVisibility(View.GONE);
                    }
                    else
                    {
                        llhierarchy.setVisibility(View.VISIBLE);
                    }
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    level = 6;
                    relprevbtn.setVisibility(View.INVISIBLE);
                    relnextbtn.setVisibility(View.INVISIBLE);
                    txtheaderplanclass.setText("Store");
                    retainSegmentValuesFilter();
                    if(!drill_down_val.equals(""))
                    {
                        requestViewByDisplay(drill_down_val);
                    }
                    else
                    {
                        Log.e("onClick: ", "in view store");
                        requestSalesListDisplayAPI();
                    }
                }
                else
                {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    progressBar1.setVisibility(View.GONE);
                    Reusable_Functions.hDialog();
                }
                menuMultipleActions.collapse();
            }
        });
    }

    // Retain values for segment click
    private void retainSegmentValuesFilter()
    {
        filter_toggleClick = true;
        switch (selectedsegValue)
        {
            case "WTD":
                Tabview.getTabAt(0).select();
                break;
            case "LW":
                Tabview.getTabAt(1).select();
                break;
            case "L4W":
                Tabview.getTabAt(2).select();
                break;
            case "STD":
                Tabview.getTabAt(3).select();
                break;
        }
    }

    private void TimeUP()
    {
            if (salesAnalysisClassArrayList.size() != 0)
            {
                if (firstVisibleItem < salesAnalysisClassArrayList.size() && !onClickFlag) {

                    if (txtheaderplanclass.getText().toString().equals(hierarchyList[0])) {
                        level = 1;
                        saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getPlanDept();
                    } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[1])) {
                        level = 2;
                        saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getPlanCategory();
                    } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[2])) {
                        level = 3;
                        saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getPlanClass();
                    } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[3])) {
                        level = 4;
                        saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getBrandName();
                    } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[4])) {
                        level = 5;
                        saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getBrandplanClass();
                    }
                    else if (txtheaderplanclass.getText().toString().equals("Store")) {
                        level = 6;
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
                            if (saleFirstVisibleItem.equals("All"))
                            {
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
                    if (txtheaderplanclass.getText().toString().equals(hierarchyList[0])) {
                        level = 1;
                        saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getPlanDept();
                    } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[1])) {
                        level = 2;
                        saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getPlanCategory();
                    } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[2])) {
                        level = 3;
                        saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getPlanClass();
                    } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[3])) {
                        level = 4;
                        saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getBrandName();
                    } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[4])) {
                        level = 5;
                        saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getBrandplanClass();
                    }
                    else if (txtheaderplanclass.getText().toString().equals("Store")) {
                        level = 6;
                        saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getBrandplanClass();
                    }
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.hDialog();
                        currentVmPos = vwpagersales.getCurrentItem();
                        offsetvalue = 0;
                        limit = 100;
                        count = 0;

                        if (firstVisibleItem != selFirstPositionValue) {
                            if (postRequest != null) {
                                postRequest.cancel();
                            }
                            analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                            progressBar1.setVisibility(View.VISIBLE);
                            if (saleFirstVisibleItem.equals("All"))
                            {
                                requestSalesViewPagerValueAPI();
                            }
                            else
                            {
                                requestSalesPagerOnScrollAPI();
                            }
                            selFirstPositionValue = firstVisibleItem;
                        }
                    }
                    else
                    {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }
                }

        }// end of else loop
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.prevplanclass:
                selFirstPositionValue = 0;
                firstVisibleItem = 0;

                if (postRequest != null) {
                    postRequest.cancel();
                }
                if (progressBar1.getVisibility() == View.VISIBLE)
                {
                    return;
                } else {
                    drill_down_val = "";

                   if(txtheaderplanclass.getText().toString().equals(hierarchyList[4])){

                       relnextbtn.setVisibility(View.VISIBLE);
                       if (lldots != null) {
                           lldots.removeAllViews();
                       }
                       currentVmPos = vwpagersales.getCurrentItem();
                       llhierarchy.setVisibility(View.GONE);
                       txtheaderplanclass.setText(hierarchyList[3]);
                       fromWhere = hierarchyList[3];
                       level = 4;
                       val = "";
//                            header_value = "";
                       salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                       if (Reusable_Functions.chkStatus(context))
                       {

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

                   }
                   else if(txtheaderplanclass.getText().toString().equals(hierarchyList[3])){

                       if (lldots != null) {
                           lldots.removeAllViews();
                       }
                       currentVmPos = vwpagersales.getCurrentItem();
                       llhierarchy.setVisibility(View.GONE);
                       txtheaderplanclass.setText(hierarchyList[2]);
                       fromWhere = hierarchyList[2];
                       level = 3;
                       val ="";
//                            header_value = "";
                       salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                       listView_SalesAnalysis.removeAllViews();
                       val = " ";
                       if (Reusable_Functions.chkStatus(context))
                       {
                           Reusable_Functions.hDialog();
                           Reusable_Functions.sDialog(context, "Loading data...");
                           progressBar1.setVisibility(View.GONE);
                           offsetvalue = 0;
                           limit = 100;
                           count = 0;
                           requestSalesListDisplayAPI();
                       }
                       else
                       {
                           Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                       }

                   }
                   else if(txtheaderplanclass.getText().toString().equals(hierarchyList[2])){

                       if (lldots != null)
                       {
                           lldots.removeAllViews();
                       }
                       currentVmPos = vwpagersales.getCurrentItem();
                       llhierarchy.setVisibility(View.GONE);
                       txtheaderplanclass.setText(hierarchyList[1]);
                       fromWhere = hierarchyList[1];
                       level = 2;
//                            header_value = "";
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

                   }
                   else if(txtheaderplanclass.getText().toString().equals(hierarchyList[1])){

                       relprevbtn.setVisibility(View.INVISIBLE);
                       if (lldots != null)
                       {
                           lldots.removeAllViews();
                       }
                       currentVmPos = vwpagersales.getCurrentItem();
                       llhierarchy.setVisibility(View.GONE);
                       txtheaderplanclass.setText(hierarchyList[0]);
                       fromWhere = hierarchyList[0];
                       level = 1;
                       val = " ";
//                            header_value = "";
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
                       }
                       else
                       {
                           Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                       }

                   }



                 /*   switch (txtheaderplanclass.getText().toString())
                    {
                        case "Brand Class":
                            relnextbtn.setVisibility(View.VISIBLE);
                            if (lldots != null) {
                                lldots.removeAllViews();
                            }
                            currentVmPos = vwpagersales.getCurrentItem();
                            llhierarchy.setVisibility(View.GONE);
                            txtheaderplanclass.setText("Brand");
                            fromWhere = "Brand";
                            level = 4;
                            val = "";
//                            header_value = "";
                            salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                            if (Reusable_Functions.chkStatus(context))
                            {

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

                        case "Brand":

                            if (lldots != null) {
                                lldots.removeAllViews();
                            }
                            currentVmPos = vwpagersales.getCurrentItem();
                            llhierarchy.setVisibility(View.GONE);
                            txtheaderplanclass.setText("Class");
                            fromWhere = "Class";
                            level = 3;
                            val ="";
//                            header_value = "";
                            salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                            listView_SalesAnalysis.removeAllViews();
                            val = " ";
                            if (Reusable_Functions.chkStatus(context))
                            {
                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                progressBar1.setVisibility(View.GONE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                requestSalesListDisplayAPI();
                            }
                            else
                            {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }
                            break;

                        case "Class":
                            if (lldots != null)
                            {
                                lldots.removeAllViews();
                            }
                            currentVmPos = vwpagersales.getCurrentItem();
                            llhierarchy.setVisibility(View.GONE);
                            txtheaderplanclass.setText("Category");
                            fromWhere = "Category";
                            level = 2;
//                            header_value = "";
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

                        case "Category":
                            relprevbtn.setVisibility(View.INVISIBLE);
                            if (lldots != null)
                            {
                                lldots.removeAllViews();
                            }
                            currentVmPos = vwpagersales.getCurrentItem();
                            llhierarchy.setVisibility(View.GONE);
                            txtheaderplanclass.setText("Department");
                            fromWhere = "Department";
                            level = 1;
                            val = " ";
//                            header_value = "";
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
                            }
                            else
                            {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        default:
                            break;
                    }*/
                }
                break;
            case R.id.nextplanclass:
                selFirstPositionValue = 0;
                firstVisibleItem = 0;

                if (postRequest != null)
                {
                    postRequest.cancel();
                }
                if (progressBar1.getVisibility() == View.VISIBLE)
                {
                    return;
                }
                else
                {
                    drill_down_val = "";


                    if(txtheaderplanclass.getText().toString().equals(hierarchyList[0])){
                        relprevbtn.setVisibility(View.VISIBLE);
                        txtheaderplanclass.setText(hierarchyList[1]);
                        if (lldots != null) {
                            lldots.removeAllViews();
                        }
                        currentVmPos = vwpagersales.getCurrentItem();
                        llhierarchy.setVisibility(View.GONE);
                        fromWhere = hierarchyList[1];
                        level = 2;
//                            header_value = "";
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
                    }
                    else if(txtheaderplanclass.getText().toString().equals(hierarchyList[1])){
                        fromWhere = hierarchyList[2];
                        txtheaderplanclass.setText(hierarchyList[2]);
                        level = 3;
//                            header_value = "";
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
                    }
                    else if(txtheaderplanclass.getText().toString().equals(hierarchyList[2])){
                        txtheaderplanclass.setText(hierarchyList[3]);
                        fromWhere = hierarchyList[3];
                        level = 4;
                        val = " ";
//                            header_value = "";
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
                    }
                    else if(txtheaderplanclass.getText().toString().equals(hierarchyList[3])){
                        txtheaderplanclass.setText(hierarchyList[4]);

                        relnextbtn.setVisibility(View.INVISIBLE);
                        if (lldots != null) {
                            lldots.removeAllViews();
                        }
//                            header_value = "";
                        currentVmPos = vwpagersales.getCurrentItem();
                        llhierarchy.setVisibility(View.GONE);
                        fromWhere = hierarchyList[4];
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
                    }


                    /*switch (txtheaderplanclass.getText().toString())
                    {
                        case "Department":
                            relprevbtn.setVisibility(View.VISIBLE);
                            txtheaderplanclass.setText("Category");
                            if (lldots != null) {
                                lldots.removeAllViews();
                            }
                            currentVmPos = vwpagersales.getCurrentItem();
                            llhierarchy.setVisibility(View.GONE);
                            fromWhere = "Category";
                            level = 2;
//                            header_value = "";
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

                        case "Category":
                            fromWhere = "Class";
                            txtheaderplanclass.setText("Class");
                            level = 3;
//                            header_value = "";
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
                            txtheaderplanclass.setText("Brand");
                            fromWhere = "Brand";
                            level = 4;
                            val = " ";
//                            header_value = "";
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

                        case "Brand":
                            txtheaderplanclass.setText("Brand Class");

                            relnextbtn.setVisibility(View.INVISIBLE);
                            if (lldots != null) {
                                lldots.removeAllViews();
                            }
//                            header_value = "";
                            currentVmPos = vwpagersales.getCurrentItem();
                            llhierarchy.setVisibility(View.GONE);
                            fromWhere = "Brand Class";
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
                    }*/
                }
                break;
            case R.id.imgfilter:
//                Intent intent = new Intent(SalesAnalysisActivity1.this, SalesFilterActivity.class);
//                intent.putExtra("checkfrom", "SalesAnalysis");
//                startActivity(intent);
                Intent intent = new Intent(SalesAnalysisActivity1.this, SalesAnalysisFilter.class);
                intent.putExtra("checkfrom", "SalesAnalysis");
                startActivity(intent);
                break;
            case R.id.imgReset :
                salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                header_value = "";
                drill_down_val = "";
                selFirstPositionValue = 0;firstVisibleItem = 0;
                if (Reusable_Functions.chkStatus(context))
                {
                    Reusable_Functions.sDialog(context, "Loading...");
                    progressBar1.setVisibility(View.GONE);
                    llhierarchy.setVisibility(View.GONE);
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    level = 1;
                    fromWhere = hierarchyList[0];
                    relprevbtn.setVisibility(View.INVISIBLE);
                    relnextbtn.setVisibility(View.VISIBLE);
                    txtheaderplanclass.setText(hierarchyList[0]);
                    selectedsegValue = "WTD";
                    Tabview.getTabAt(0).select();
                    requestSalesListDisplayAPI();
                }
                else
                {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    progressBar1.setVisibility(View.GONE);
                    Reusable_Functions.hDialog();
                }
                break;
            case R.id.imageBtnBack:
                onBackPressed();
                break;
        }
    }
    //------------------------------------- API Declaration -------------------------------------------//

    //Api Calling for fashion at bb part.......
    //Api to display class level values(Api 1.20)
    private void requestSalesListDisplayAPI()
    {
        String url = "";
        if(!header_value.equals(""))
        {
          url  = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytimeNew/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit +"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId + header_value;
        }
        else
        {
         url  = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytimeNew/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit +"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
        }
        Log.e("url sales in fbb:", "" + url);
        postRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    int i;
                    if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                        onClickFlag = false;
                        progressBar1.setVisibility(View.GONE);
                        return;
                    } else if (response.length() == limit)
                    {
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
                        ImageView img = (ImageView) lldots.getChildAt(currentItem);
                        img.setImageResource(R.mipmap.dots_selected);


                        // For Add "All"
                        salesAnalysisClass = new SalesAnalysisListDisplay();
                        if (txtheaderplanclass.getText().toString().equals(hierarchyList[0])) {
                            salesAnalysisClass.setPlanDept("All");

                        } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[1])) {
                            salesAnalysisClass.setPlanCategory("All");

                        } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[2])) {
                            salesAnalysisClass.setPlanClass("All");

                        } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[3])) {
                            salesAnalysisClass.setBrandName("All");

                        }
                        else if (txtheaderplanclass.getText().toString().equals(hierarchyList[4])) {
                            salesAnalysisClass.setBrandplanClass("All");

                        }
                        else if (txtheaderplanclass.getText().toString().equals("Store")) {
                            salesAnalysisClass.setBrandplanClass("All");

                        }
                        salesAnalysisClassArrayList.add(0, salesAnalysisClass);
                        listView_SalesAnalysis.setLayoutManager(new LinearLayoutManager(context));

                        listView_SalesAnalysis.setLayoutManager(new LinearLayoutManager(listView_SalesAnalysis.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                        listView_SalesAnalysis.setOnFlingListener(null);
                        new GravitySnapHelper(48).attachToRecyclerView(listView_SalesAnalysis);

                        salesadapter = new SalesAnalysisSnapAdapter(salesAnalysisClassArrayList, context, firstVisibleItem, fromWhere, listView_SalesAnalysis,hierarchyList);
                        listView_SalesAnalysis.setAdapter(salesadapter);

                        //Retain Values.....
                        for (int j = 0; j < salesAnalysisClassArrayList.size(); j++) {

                            if (txtheaderplanclass.getText().toString().equals(hierarchyList[0])) {
                                level = 1;
                                saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getPlanDept();
                                if (salesAnalysisClassArrayList.get(firstVisibleItem).getPlanDept().equals(saleFirstVisibleItem)) {
                                    listView_SalesAnalysis.getLayoutManager().scrollToPosition(firstVisibleItem);
                                }

                            } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[1])) {
                                level = 2;
                                saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getPlanCategory();
                                if (salesAnalysisClassArrayList.get(firstVisibleItem).getPlanCategory().equals(saleFirstVisibleItem)) {
                                    listView_SalesAnalysis.getLayoutManager().scrollToPosition(firstVisibleItem);
                                }

                            } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[2])) {

                                level = 3;
                                saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getPlanClass();
                                if (salesAnalysisClassArrayList.get(firstVisibleItem).getPlanClass().equals(saleFirstVisibleItem)) {
                                    listView_SalesAnalysis.getLayoutManager().scrollToPosition(firstVisibleItem);
                                }

                            } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[3])) {

                                level = 4;
                                saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getBrandName();
                                if (salesAnalysisClassArrayList.get(firstVisibleItem).getBrandName().equals(saleFirstVisibleItem)) {
                                    listView_SalesAnalysis.getLayoutManager().scrollToPosition(firstVisibleItem);
                                }

                            } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[4])) {

                                level = 5;
                                saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getBrandplanClass();
                                if (salesAnalysisClassArrayList.get(firstVisibleItem).getBrandplanClass().equals(saleFirstVisibleItem)) {
                                    listView_SalesAnalysis.getLayoutManager().scrollToPosition(firstVisibleItem);
                                }
                            }
                            else if (txtheaderplanclass.getText().toString().equals("Store")) {

                                level = 6;
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
                            from_filter = false;
                            llhierarchy.setVisibility(View.GONE);
                            requestSalesViewPagerValueAPI();
                        }
                        else
                        {
                            llhierarchy.setVisibility(View.GONE);
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            from_filter = false;
                            analysisArrayList.clear();
                            requestSalesPagerOnScrollAPI();
                        }
                    }
                }
                catch (Exception e)
                {
                    Reusable_Functions.hDialog();
                    Log.e("", "onResponse: " +e.getMessage());
                    Toast.makeText(context, "no data found"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar1.setVisibility(View.GONE);
                    onClickFlag = false;
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
                        onClickFlag = false;
                        error.printStackTrace();
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
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
    private void requestSalesViewPagerValueAPI()
    {
        String url = " ";
        if(!header_value.equals(""))
        {
            url = ConstsCore.web_url + "/v1/display/salesanalysisbytimeNew/" + userId + "?view=" + selectedsegValue + "&offset=" + offsetvalue + "&limit=" + limit+"&geoLevel2Code="+geoLevel2Code + "&lobId="+ lobId + header_value;

        }
        else
        {
            url = ConstsCore.web_url + "/v1/display/salesanalysisbytimeNew/" + userId + "?view=" + selectedsegValue + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;
        }
        Log.e("Sales Analysis", "requestSalesViewPagerValueAPI: "+url);
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
                                return;
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
//                            pageradapter.notifyDataSetChanged();
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
        if(!header_value.equals(""))
        {
            if (txtheaderplanclass.getText().toString().equals(hierarchyList[0]))
            {
                url = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytimeNew/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&department=" + saleFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code="+geoLevel2Code + "&lobId="+ lobId + header_value;
            }
            else if (txtheaderplanclass.getText().toString().equals(hierarchyList[1]))
            {
                url = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytimeNew/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&category=" + saleFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit+ "&geoLevel2Code="+geoLevel2Code + "&lobId="+ lobId + header_value;
            }
            else if (txtheaderplanclass.getText().toString().equals(hierarchyList[2]))
            {
                url = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytimeNew/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&class=" + saleFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit+ "&geoLevel2Code="+geoLevel2Code + "&lobId="+ lobId + header_value;
            }
            else if (txtheaderplanclass.getText().toString().equals(hierarchyList[3]))
            {
                url = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytimeNew/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&brand=" + saleFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit+ "&geoLevel2Code="+geoLevel2Code + "&lobId="+ lobId + header_value;
            }
            else if (txtheaderplanclass.getText().toString().equals(hierarchyList[4]))
            {
                url = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytimeNew/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&brandclass=" + saleFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit+ "&geoLevel2Code="+geoLevel2Code + "&lobId="+ lobId + header_value;
            }
            else if (txtheaderplanclass.getText().toString().equals("Store"))
            {
                url = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytimeNew/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&storeCode=" + saleFirstVisibleItem.substring(0,4) + "&offset=" + offsetvalue + "&limit=" + limit+ "&geoLevel2Code="+geoLevel2Code + "&lobId="+ lobId +
                        header_value;
            }
        }
        else
        {
            if (txtheaderplanclass.getText().toString().equals(hierarchyList[0]))
            {
                url = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytimeNew/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&department=" + saleFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;
            }
            else if (txtheaderplanclass.getText().toString().equals(hierarchyList[1]))
            {
                url = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytimeNew/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&category=" + saleFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;
            }
            else if (txtheaderplanclass.getText().toString().equals(hierarchyList[2]))
            {
                url = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytimeNew/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&class=" + saleFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;
            }
            else if (txtheaderplanclass.getText().toString().equals(hierarchyList[3]))
            {
                url = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytimeNew/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&brand=" + saleFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;
            }
            else if (txtheaderplanclass.getText().toString().equals(hierarchyList[4]))
            {
                url = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytimeNew/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&brandclass=" + saleFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;
            }
            else if (txtheaderplanclass.getText().toString().equals("Store"))
            {
                url = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytimeNew/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&storeCode=" + saleFirstVisibleItem.substring(0, 4) + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;
            }
        }
        Log.e("Sales Analysis", "requestSalesPagerOnScrollAPI: "+url );
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
                                return;
                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++)
                                {

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
                                from_filter = false;
                                Reusable_Functions.hDialog();
                            }
                        } catch (Exception e)

                        {
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

    // hierarchy api calling for Category , class ,Brand , Brand Class
    private void requestSalesCategoryList(final String deptName) {
        String salespvacategory_listurl;
        salespvacategory_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytimeNew/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&department=" + deptName.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code="+geoLevel2Code + "&lobId="+ lobId;
        Log.e("Sales Anlysis", "requestSalesCategoryList: "+salespvacategory_listurl);
        postRequest = new JsonArrayRequest(Request.Method.GET, salespvacategory_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                progressBar1.setVisibility(View.GONE);
                                onClickFlag = false;
                                return;

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

                                // For Add "All"
                                salesAnalysisClass = new SalesAnalysisListDisplay();
                                if (txtheaderplanclass.getText().toString().equals(hierarchyList[0]))
                                {
                                    salesAnalysisClass.setPlanDept("All");

                                }
                                else if (txtheaderplanclass.getText().toString().equals(hierarchyList[1]))
                                {
                                    salesAnalysisClass.setPlanCategory("All");

                                }
                                else if (txtheaderplanclass.getText().toString().equals(hierarchyList[2]))
                                {
                                    salesAnalysisClass.setPlanClass("All");

                                } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[3]))
                                {
                                    salesAnalysisClass.setBrandName("All");

                                } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[4]))
                                {
                                    salesAnalysisClass.setBrandplanClass("All");
                                }
                                salesAnalysisClassArrayList.add(0, salesAnalysisClass);
                                listView_SalesAnalysis.setLayoutManager(new LinearLayoutManager(listView_SalesAnalysis.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                listView_SalesAnalysis.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(listView_SalesAnalysis);

                                salesadapter = new SalesAnalysisSnapAdapter(salesAnalysisClassArrayList, context, firstVisibleItem, fromWhere, listView_SalesAnalysis,hierarchyList);
                                listView_SalesAnalysis.setAdapter(salesadapter);

                                val = "";
                                val =  deptName.replaceAll("%20", " ").replaceAll("%26","&");
                                drill_down_val = "";
                                drill_down_val = "&department="+ deptName.replaceAll(" ", "%20").replaceAll("&","%26");
                                txthDeptName.setText(val);
                                llhierarchy.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                                saleFirstVisibleItem = salesAnalysisClassArrayList.get(0).getPlanCategory();

                                if (saleFirstVisibleItem.equals("All")) {
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    analysisArrayList.clear();
                                    requestSalesViewPagerValueAPI();
                                }
                                else
                                {
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    analysisArrayList.clear();
                                    requestSalesPagerOnScrollAPI();
                                }
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

    private void requestSalesPlanClassListAPI(final String category)
    {
        String salespva_planclass_listurl = "";
        salespva_planclass_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytimeNew/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&category=" + category.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit+ "&geoLevel2Code="+geoLevel2Code + "&lobId="+ lobId;
//        Log.e("salespva_planclass_listurl "," "+salespva_planclass_listurl);
        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_planclass_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        Log.e(TAG, "onResponse: ", );
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                progressBar1.setVisibility(View.GONE);
                                onClickFlag = false;
                                return;

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

                                for (int i = 0; i < 3; i++)
                                {
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

                                // For Add "All"
                                salesAnalysisClass = new SalesAnalysisListDisplay();
                                if (txtheaderplanclass.getText().toString().equals(hierarchyList[0])) {
                                    salesAnalysisClass.setPlanDept("All");

                                } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[1])) {
                                    salesAnalysisClass.setPlanCategory("All");

                                } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[2])) {
                                    salesAnalysisClass.setPlanClass("All");

                                } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[3])) {
                                    salesAnalysisClass.setBrandName("All");

                                } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[4])) {
                                    salesAnalysisClass.setBrandplanClass("All");
                                }
                                salesAnalysisClassArrayList.add(0, salesAnalysisClass);
                                listView_SalesAnalysis.setLayoutManager(new LinearLayoutManager(
                                        listView_SalesAnalysis.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                listView_SalesAnalysis.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(listView_SalesAnalysis);

                                salesadapter = new SalesAnalysisSnapAdapter(salesAnalysisClassArrayList, context, firstVisibleItem, fromWhere, listView_SalesAnalysis,hierarchyList);
                                listView_SalesAnalysis.setAdapter(salesadapter);

                                val += " > " + category.replaceAll("%20"," ").replaceAll("%26","&");
                                drill_down_val = drill_down_val + "&category="+category.replaceAll(" ","%20").replaceAll("&","%26");
                                txthDeptName.setText(val);
                                llhierarchy.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                                saleFirstVisibleItem = salesAnalysisClassArrayList.get(0).getPlanClass();

                                if (saleFirstVisibleItem.equals("All"))
                                {
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    planCategoryNm = category;
                                    all_from_val = "class";
                                    analysisArrayList.clear();
                                    requestSalesViewPagerValueAPI();
                                }
                                else
                                {
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    analysisArrayList.clear();
                                    requestSalesPagerOnScrollAPI();
                                }
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

    private void requestSalesBrandListAPI(final String planclass) {
        String salespva_brand_listurl;
        salespva_brand_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytimeNew/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&class=" + planclass.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit+ "&geoLevel2Code="+geoLevel2Code + "&lobId="+ lobId;
        Log.e("salespva_brand_listurl "," "+salespva_brand_listurl);

        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_brand_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                progressBar1.setVisibility(View.GONE);
                                onClickFlag = false;
                                return;
                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    salesAnalysisClass = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisClass);

                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesBrandListAPI(planclass);

                            }
                            else if (response.length() < limit)
                            {
                                for (int i = 0; i < response.length(); i++)
                                {

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

                                // For Add "All"
                                salesAnalysisClass = new SalesAnalysisListDisplay();
                                if (txtheaderplanclass.getText().toString().equals(hierarchyList[0])) {
                                    salesAnalysisClass.setPlanDept("All");

                                } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[1])) {
                                    salesAnalysisClass.setPlanCategory("All");

                                } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[2])) {
                                    salesAnalysisClass.setPlanClass("All");

                                } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[3])) {
                                    salesAnalysisClass.setBrandName("All");

                                } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[4])) {
                                    salesAnalysisClass.setBrandplanClass("All");
                                }
                                salesAnalysisClassArrayList.add(0, salesAnalysisClass);
                                listView_SalesAnalysis.setLayoutManager(new LinearLayoutManager(
                                        listView_SalesAnalysis.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                listView_SalesAnalysis.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(listView_SalesAnalysis);

                                salesadapter = new SalesAnalysisSnapAdapter(salesAnalysisClassArrayList, context, firstVisibleItem, fromWhere, listView_SalesAnalysis,hierarchyList);
                                listView_SalesAnalysis.setAdapter(salesadapter);
                                salesadapter.notifyDataSetChanged();
                                val += " > " +  planclass.replaceAll("%20"," ").replaceAll("%26","&");
                                drill_down_val = drill_down_val +"&class="+planclass.replaceAll(" ","%20").replaceAll("&","%26");
                                txthDeptName.setText(val);
                                llhierarchy.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                                saleFirstVisibleItem = salesAnalysisClassArrayList.get(0).getBrandName();

                                if (saleFirstVisibleItem.equals("All"))
                                {
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    planClassNm = planclass;
                                    all_from_val = "brand";
                                    analysisArrayList.clear();
                                    requestSalesViewPagerValueAPI();
                                }
                                else
                                {
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    analysisArrayList.clear();
                                    requestSalesPagerOnScrollAPI();
                                }
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

    private void requestSalesBrandPlanListAPI(final String brandnm) {

        String salespva_brandplan_listurl;

        salespva_brandplan_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytimeNew/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&brand=" + brandnm.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit+ "&geoLevel2Code="+geoLevel2Code + "&lobId="+ lobId;
        Log.e("salespva_brandplan_listurl "," "+salespva_brandplan_listurl);
        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_brandplan_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                progressBar1.setVisibility(View.GONE);
                                onClickFlag = false;
                                return;

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
                                // For Add "All"
                                salesAnalysisClass = new SalesAnalysisListDisplay();
                                if (txtheaderplanclass.getText().toString().equals(hierarchyList[0])) {
                                    salesAnalysisClass.setPlanDept("All");

                                } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[1])) {
                                    salesAnalysisClass.setPlanCategory("All");

                                } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[2])) {
                                    salesAnalysisClass.setPlanClass("All");

                                } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[3])) {
                                    salesAnalysisClass.setBrandName("All");

                                } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[4])) {
                                    salesAnalysisClass.setBrandplanClass("All");
                                }
                                salesAnalysisClassArrayList.add(0, salesAnalysisClass);
                                listView_SalesAnalysis.setLayoutManager(new LinearLayoutManager(
                                        listView_SalesAnalysis.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                listView_SalesAnalysis.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(listView_SalesAnalysis);

                                salesadapter = new SalesAnalysisSnapAdapter(salesAnalysisClassArrayList, context, firstVisibleItem, fromWhere, listView_SalesAnalysis,hierarchyList);
                                listView_SalesAnalysis.setAdapter(salesadapter);
                                salesadapter.notifyDataSetChanged();
                                val += " > " + brandnm.replaceAll("%20"," ").replaceAll("%26","&");
                                drill_down_val = drill_down_val +"&brand="+brandnm.replaceAll(" ","%20").replaceAll("&","%26");
                                txthDeptName.setText(val);
                                llhierarchy.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                                saleFirstVisibleItem = salesAnalysisClassArrayList.get(0).getBrandplanClass();
                                if(saleFirstVisibleItem.equals("All"))
                                {
                                    Log.e("in brand class api: ","header");
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    analysisArrayList.clear();
                                    requestSalesViewPagerValueAPI();
                                }
                                else
                                {
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
                }
                ,
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


    private void requestSalesSelectedFilterVal(final String str, final int sales_filter_level)
    {
        String salespva_brandplan_listurl = "";
        if(sales_filter_level != 0)
        {
            salespva_brandplan_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytimeNew/" + userId + "?view=" + selectedsegValue + "&level=" + sales_filter_level + str + "&offset=" + offsetvalue + "&limit=" + limit+ "&geoLevel2Code="+geoLevel2Code + "&lobId="+ lobId;
        }

        else
        {
            salespva_brandplan_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytimeNew/" + userId + "?view=" + selectedsegValue + str + "&offset=" + offsetvalue + "&limit=" + limit+ "&geoLevel2Code="+geoLevel2Code + "&lobId="+ lobId;
        }
        Log.e("requestSalesSelectedFilterVal: ",""+salespva_brandplan_listurl);
        postRequest = new JsonArrayRequest(Request.Method.GET, salespva_brandplan_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if (sales_filter_level == 2)
                        {
                            txtheaderplanclass.setText(hierarchyList[1]);
                            fromWhere = hierarchyList[1];
                            relprevbtn.setVisibility(View.VISIBLE);
                        }
                        else if (sales_filter_level == 3)
                        {
                            txtheaderplanclass.setText(hierarchyList[2]);
                            fromWhere = hierarchyList[2];
                            relprevbtn.setVisibility(View.VISIBLE);

                        }
                        else if (sales_filter_level == 4)
                        {
                            txtheaderplanclass.setText(hierarchyList[3]);
                            fromWhere = hierarchyList[3];
                            relprevbtn.setVisibility(View.VISIBLE);

                        }
                        else if (sales_filter_level == 5)
                        {
                            txtheaderplanclass.setText(hierarchyList[4]);
                            fromWhere = hierarchyList[4];
                            relprevbtn.setVisibility(View.VISIBLE);
                            relnextbtn.setVisibility(View.INVISIBLE);
                        }
                        else if (sales_filter_level == 5)
                        {
                            txtheaderplanclass.setText(hierarchyList[4]);
                            fromWhere = hierarchyList[4];
                            relprevbtn.setVisibility(View.VISIBLE);
                            relnextbtn.setVisibility(View.INVISIBLE);
                        }

                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                progressBar1.setVisibility(View.GONE);
                                onClickFlag = false;
                                return;

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    salesAnalysisClass = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisClass);

                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesSelectedFilterVal(str, sales_filter_level);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++)
                                {
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

                                // For Add "All"
                                salesAnalysisClass = new SalesAnalysisListDisplay();
                                if (txtheaderplanclass.getText().toString().equals(hierarchyList[0]))
                                {
                                    salesAnalysisClass.setPlanDept("All");
                                }
                                else if (txtheaderplanclass.getText().toString().equals(hierarchyList[1]))
                                {
                                    salesAnalysisClass.setPlanCategory("All");

                                }
                                else if (txtheaderplanclass.getText().toString().equals(hierarchyList[2]))
                                {
                                    salesAnalysisClass.setPlanClass("All");

                                }
                                else if (txtheaderplanclass.getText().toString().equals(hierarchyList[3]))
                                {
                                    salesAnalysisClass.setBrandName("All");
                                }
                                else if (txtheaderplanclass.getText().toString().equals(hierarchyList[4]))
                                {
                                    salesAnalysisClass.setBrandplanClass("All");

                                }
//                                else if (txtheaderplanclass.getText().toString().equals("Store"))
//                                {
//                                    salesAnalysisClass.setBrandplanClass("All");
//
//                                }
                                salesAnalysisClassArrayList.add(0, salesAnalysisClass);
                                listView_SalesAnalysis.setLayoutManager(new LinearLayoutManager(
                                        listView_SalesAnalysis.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                listView_SalesAnalysis.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(listView_SalesAnalysis);
                                salesadapter = new SalesAnalysisSnapAdapter(salesAnalysisClassArrayList, context, firstVisibleItem, fromWhere, listView_SalesAnalysis,hierarchyList);
                                listView_SalesAnalysis.setAdapter(salesadapter);
                                salesadapter.notifyDataSetChanged();
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                from_filter = true;
                                analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();

                                if (txtheaderplanclass.getText().toString().equals(hierarchyList[0]))
                                {
                                    level = 1;
                                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getPlanDept();
                                }
                                else if (txtheaderplanclass.getText().toString().equals(hierarchyList[1]))
                                {
                                    level = 2;
                                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getPlanCategory();
                                }
                                else if (txtheaderplanclass.getText().toString().equals(hierarchyList[2])) {
                                    level = 3;
                                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getPlanClass();
                                }
                                else if (txtheaderplanclass.getText().toString().equals(hierarchyList[3])) {
                                    level = 4;
                                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getBrandName();
                                }
                                else if (txtheaderplanclass.getText().toString().equals(hierarchyList[4])) {
                                    level = 5;
                                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(firstVisibleItem).getBrandplanClass();
                                }

                                if(saleFirstVisibleItem.equals("All"))
                                {
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    analysisArrayList.clear();
                                    all_from_val = "filter";
                                    llhierarchy.setVisibility(View.GONE);
                                    requestSalesViewPagerValueAPI();
                                }
                                else
                                {
                                    Log.e("onResponse===: ",""+from_filter);
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    analysisArrayList.clear();
                                    llhierarchy.setVisibility(View.GONE);
                                    requestSalesPagerOnScrollAPI();

                                }
                               from_filter = false;
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

    private void requestViewByDisplay(final String drill_down_val)
    {
        String viewby_url;
        viewby_url = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytimeNew/" + userId + "?view=" + selectedsegValue + "&level=" + level + drill_down_val + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code="+geoLevel2Code + "&lobId="+ lobId;
        Log.e("Sales Anlysis", "requestViewByDisplay: "+viewby_url);
        postRequest = new JsonArrayRequest(Request.Method.GET, viewby_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                progressBar1.setVisibility(View.GONE);
                                onClickFlag = false;
                                return;

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    salesAnalysisClass = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisClass);

                                }

                                offsetvalue = (limit * count) + limit;
                                count++;
                               requestViewByDisplay(drill_down_val);

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

                                // For Add "All"
                                salesAnalysisClass = new SalesAnalysisListDisplay();
                                if (txtheaderplanclass.getText().toString().equals(hierarchyList[0]))
                                {
                                    salesAnalysisClass.setPlanDept("All");

                                }
                                else if (txtheaderplanclass.getText().toString().equals(hierarchyList[1]))
                                {
                                    salesAnalysisClass.setPlanCategory("All");
                                }
                                else if (txtheaderplanclass.getText().toString().equals(hierarchyList[2]))
                                {
                                    salesAnalysisClass.setPlanClass("All");

                                } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[3]))
                                {
                                    salesAnalysisClass.setBrandName("All");

                                } else if (txtheaderplanclass.getText().toString().equals(hierarchyList[4]))
                                {
                                    salesAnalysisClass.setBrandplanClass("All");
                                }
                                else if (txtheaderplanclass.getText().toString().equals("Store"))
                                {
                                    salesAnalysisClass.setBrandplanClass("All");
                                }
                                salesAnalysisClassArrayList.add(0, salesAnalysisClass);
                                listView_SalesAnalysis.setLayoutManager(new LinearLayoutManager(listView_SalesAnalysis.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                listView_SalesAnalysis.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(listView_SalesAnalysis);

                                salesadapter = new SalesAnalysisSnapAdapter(salesAnalysisClassArrayList, context, firstVisibleItem, fromWhere, listView_SalesAnalysis,hierarchyList);
                                listView_SalesAnalysis.setAdapter(salesadapter);

                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                                if (txtheaderplanclass.getText().toString().equals(hierarchyList[0]))
                                {
                                    level = 1;
                                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(0).getPlanDept();
                                }
                                else if (txtheaderplanclass.getText().toString().equals(hierarchyList[1]))
                                {
                                    level = 2;
                                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(0).getPlanCategory();
                                }
                                else if (txtheaderplanclass.getText().toString().equals(hierarchyList[2])) {
                                    level = 3;
                                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(0).getPlanClass();
                                }
                                else if (txtheaderplanclass.getText().toString().equals(hierarchyList[3]))
                                {
                                    level = 4;
                                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(0).getBrandName();
                                }
                                else if (txtheaderplanclass.getText().toString().equals(hierarchyList[4]))
                                {
                                    level = 5;
                                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(0).getBrandplanClass();
                                }
                                else if (txtheaderplanclass.getText().toString().equals("Store"))
                                {
                                    level = 6;
                                    saleFirstVisibleItem = salesAnalysisClassArrayList.get(0).getBrandplanClass();
                                }
                                header_value = drill_down_val;

                                if (saleFirstVisibleItem.equals("All")) {
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    requestSalesViewPagerValueAPI();
                                }
                                else
                                {
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    requestSalesPagerOnScrollAPI();
                                }
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
    public void onBackPressed()
    {
        selectedsegValue = "";
        level = 0;
        selectedsegValue = "WTD";
        level = 1;
        drill_down_val = "";
        this.finish();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Log.e("TAG", "onTabSelected: " + tab.getPosition() + filter_toggleClick);
        int checkedId = tab.getPosition();
        //  if (!filter_toggleClick) {
            switch (checkedId) {
                case 0:
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
                        header_value="";
//                        if (getIntent().getStringExtra("selectedStringVal") == null)
//                        {
//                            filter_toggleClick = false;
//                            retainSegmentValuesFilter();
                            requestSalesListDisplayAPI();
//                        }
//                        else if (getIntent().getStringExtra("selectedStringVal") != null)
//                        {
//                            header_value  = getIntent().getStringExtra("selectedStringVal");
////                            sales_filter_level = getIntent().getIntExtra("selectedlevelVal",0);
//
//                            filter_toggleClick = true;
//                            retainSegmentValuesFilter();
//                            requestSalesSelectedFilterVal(header_value,filter_level);
//                        }
                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case 1:
                    Log.e("LW Selected", "");
                    if (selectedsegValue.equals("LW"))
                        break;
                    selectedsegValue = "LW";
                    if (lldots != null)
                    {
                        lldots.removeAllViews();
                    }
                    currentVmPos = vwpagersales.getCurrentItem();
                    llhierarchy.setVisibility(View.GONE);
                    salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                    analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                    if (Reusable_Functions.chkStatus(context))
                    {
                        Reusable_Functions.hDialog();
                        Reusable_Functions.sDialog(context, "Loading data...");
                        progressBar1.setVisibility(View.GONE);
                        offsetvalue = 0;
                        limit = 100;
                        count = 0;
                        val = "";
//                        header_value="";
                        Log.e("onTabSelected: LW", "" + selectedsegValue);
//                        if (getIntent().getStringExtra("selectedStringVal") == null)
//                        {
//                            filter_toggleClick = false;
//                            retainSegmentValuesFilter();
                            requestSalesListDisplayAPI();
//                        }
//                        else if (getIntent().getStringExtra("selectedStringVal") != null)
//                        {
//                            header_value  = getIntent().getStringExtra("selectedStringVal");
////                            sales_filter_level = getIntent().getIntExtra("selectedlevelVal",0);
//
//                            filter_toggleClick = true;
//                            retainSegmentValuesFilter();
//                            requestSalesSelectedFilterVal(header_value,filter_level);
//                        }
                   } else
                    {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case 2:
                    if (selectedsegValue.equals("L4W"))
                        break;
                    selectedsegValue = "L4W";
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
//                        header_value="";
//                      if (getIntent().getStringExtra("selectedStringVal") == null)
//                        {
//                            filter_toggleClick = false;
//                            retainSegmentValuesFilter();
                            requestSalesListDisplayAPI();
//                        }
//                        else if (getIntent().getStringExtra("selectedStringVal") != null)
//                        {
//                            header_value  = getIntent().getStringExtra("selectedStringVal");
//                            sales_filter_level = getIntent().getIntExtra("selectedlevelVal",0);
//
//                            filter_toggleClick = true;
//                            retainSegmentValuesFilter();
//                            requestSalesSelectedFilterVal(header_value,sales_filter_level);

                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case 3:
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
//                        header_value="";
//                       if (getIntent().getStringExtra("selectedStringVal") == null)
//                        {
//                            filter_toggleClick = false;
//                            retainSegmentValuesFilter();
                            requestSalesListDisplayAPI();
//                        }
//                        else if (getIntent().getStringExtra("selectedStringVal") != null)
//                        {
//                            header_value  = getIntent().getStringExtra("selectedStringVal");
//                            sales_filter_level = getIntent().getIntExtra("selectedlevelVal",0);
//
//                            filter_toggleClick = true;
//                            retainSegmentValuesFilter();
//                            requestSalesSelectedFilterVal(header_value,sales_filter_level);
//                        }
                    }
                    else
                    {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
//            } else {
//                filter_toggleClick = false;
//            }
         }

    @Override
    public void onTabUnselected(TabLayout.Tab tab)
    {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
