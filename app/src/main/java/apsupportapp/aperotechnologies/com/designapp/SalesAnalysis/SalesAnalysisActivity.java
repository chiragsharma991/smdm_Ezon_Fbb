package apsupportapp.aperotechnologies.com.designapp.SalesAnalysis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisListDisplay;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisViewPagerValue;
import info.hoang8f.android.segmented.SegmentedGroup;


public class SalesAnalysisActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    SegmentedGroup segmentedGroupSales;
    LinearLayout llayoutSalesAnalysis;
    SalesAnalysisListDisplay salesAnalysisClass;
   public static SalesAnalysisViewPagerValue salesAnalysis;
    ArrayList<SalesAnalysisViewPagerValue> analysisArrayList, salesList;
    ArrayList<SalesAnalysisListDisplay> salesAnalysisClassArrayList;
    static ViewPager vwpagersales;
    LinearLayout lldots, llhierarchy;
    RelativeLayout relLayoutSales;
    ListView listView_SalesAnalysis;
    SalesAnalysisAdapter salesadapter;
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
    static String planDept, planCategory, planClass;
    Gson gson;
    int focusposition = 0;
    LinearLayout rankLayout;
    RelativeLayout relimgrank, relimgfilter;
    RelativeLayout relprevbtn, relnextbtn, relimgclose;
    TextView txtheaderplanclass;
    TextView txtZonalSales, txtNationalSales;
    TextView txtZonalYOY, txtNationalYOY;
    TextView txthDeptName, txthCategory, txthPlanClass, txthBrand;
    View view;
    int selFirstPositionValue = 0;
    String txtSalesClickedValue;
    String val;
    boolean flag = false;
    int currentVmPos;
    int currentIndex ,top;
    Parcelable state;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesanalysis);
        getSupportActionBar().hide();
        fromWhere = "Department";
        txtSalesClickedValue = " ";
        val = "";
        context = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        selFirstPositionValue = 0;
        etListText = (EditText) findViewById(R.id.etListText);
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

        //Rank UI Components
       // rankLayout = (LinearLayout) findViewById(R.id.rankLayout);
        txtZonalSales = (TextView) findViewById(R.id.txtZonalSales);
        txtNationalSales = (TextView) findViewById(R.id.txtNationalSales);
        txtZonalYOY = (TextView) findViewById(R.id.txtZonalYOY);
        txtNationalYOY = (TextView) findViewById(R.id.txtNationalYOY);

        //WOW & YOY UI Components


        txtheaderplanclass.setText("Department");

        relimgclose = (RelativeLayout) findViewById(R.id.relimgclose);

        RelativeLayout relpopuplayout = (RelativeLayout) findViewById(R.id.relpopuplayout);
        relpopuplayout.setOnClickListener(null);

      //  RelativeLayout relrankLayout = (RelativeLayout) findViewById(R.id.relrankLayout);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
               /* Intent i = new Intent(SalesAnalysisActivity.this, DashBoardActivity.class);
                startActivity(i);*/
                finish();
            }
        });

//        relrankLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rankLayout.setVisibility(View.GONE);
//            }
//        });

//        relimgclose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rankLayout.setVisibility(View.GONE);
//            }
//        });
//
//        relimgrank.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (rankLayout.getVisibility() == View.VISIBLE) {
//                    rankLayout.setVisibility(View.GONE);
//                } else if (rankLayout.getVisibility() == View.GONE) {
//                    rankLayout.setVisibility(View.VISIBLE);
//                }
//            }
//        });

        relimgfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SalesAnalysisActivity.this, SalesFilterActivity.class);
                intent.putExtra("checkfrom", "SalesAnalysis");
                startActivity(intent);

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

        listView_SalesAnalysis = (ListView) findViewById(R.id.listView_SalesAnalysis);
        listView_SalesAnalysis.addFooterView(getLayoutInflater().inflate(R.layout.list_footer, null));

        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();

        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            llhierarchy.setVisibility(View.GONE);
            llayoutSalesAnalysis.setVisibility(View.GONE);
            offsetvalue = 0;
            limit = 100;
            count = 0;
            level = 1;
            currentIndex = listView_SalesAnalysis.getFirstVisiblePosition();
            View v = listView_SalesAnalysis.getChildAt(0);
            top = (v == null) ? 0 : (v.getTop() - listView_SalesAnalysis.getPaddingTop());
            requestSalesListDisplayAPI();

            Log.e("state on create",""+state);

        } else {

            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }


        relprevbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        llayoutSalesAnalysis.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
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
                        flag= false;
                        level = 3;
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        llayoutSalesAnalysis.setVisibility(View.GONE);

                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
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
                        llayoutSalesAnalysis.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
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
//
                        relprevbtn.setVisibility(View.INVISIBLE);
                        //relnextbtn.setVisibility(View.VISIBLE);
//                        relprevbtn.setBackgroundResource(R.color.bg_header);
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
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        llayoutSalesAnalysis.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
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
                        llayoutSalesAnalysis.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
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
                        llayoutSalesAnalysis.setVisibility(View.GONE);

                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
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
                        llayoutSalesAnalysis.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
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
                        llayoutSalesAnalysis.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
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
        listView_SalesAnalysis.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (salesAnalysisClassArrayList.size() != 0) {

                    if (view.getFirstVisiblePosition() <= salesAnalysisClassArrayList.size() - 1) {

                        focusposition = view.getFirstVisiblePosition();

                        listView_SalesAnalysis.setSelection(view.getFirstVisiblePosition());
                        currentIndex = listView_SalesAnalysis.getFirstVisiblePosition();
                        //Log.e("firstVisibleItem", " " + view.getFirstVisiblePosition() + " " + arrayList.get(view.getFirstVisiblePosition()).getPlanDept());
//                        currentIndex = focusposition;
                        Log.e(TAG,"focusPosition----"+currentIndex);
                        if (txtheaderplanclass.getText().toString().equals("Department")) {
                            saleFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanDept().toString();
                        } else if (txtheaderplanclass.getText().toString().equals("Category")) {
                            saleFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanCategory().toString();
                        } else if (txtheaderplanclass.getText().toString().equals("Plan Class")) {
                            saleFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanClass().toString();
                        } else if (txtheaderplanclass.getText().toString().equals("Brand")) {
                            saleFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getBrandName().toString();
                        } else if (txtheaderplanclass.getText().toString().equals("Brand Plan Class")) {
                            saleFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getBrandplanClass().toString();
                        }
                        //Log.e("analysisArrayList", " " + analysisArrayList.size());

                        if (focusposition != selFirstPositionValue) {
                            if (Reusable_Functions.chkStatus(context)) {
                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                               //SalesPagerAdapter.currentPage = 0;
//                                if (lldots != null) {
//                                    lldots.removeAllViews();
//                                }
                                currentVmPos = vwpagersales.getCurrentItem();
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                                if (saleFirstVisibleItem.equals("All")) {
                                    requestSalesViewPagerValueAPI();
                                } else {
                                    requestSalesPagerOnScrollAPI();
                                }

                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }
                        }
                        selFirstPositionValue = focusposition;

                    } else {
                        focusposition = salesAnalysisClassArrayList.size() - 1;
                        listView_SalesAnalysis.setSelection(salesAnalysisClassArrayList.size() - 1);
                        //SalesPagerAdapter.currentPage = 0;
//                        if (lldots != null) {
//                            lldots.removeAllViews();
//                        }
                        selFirstPositionValue = focusposition;
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


        listView_SalesAnalysis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "listview position" + position);
                //listView_SalesAnalysis.setSelector(android.R.color.darker_gray);
                if(position < salesAnalysisClassArrayList.size()) {
                    switch (txtheaderplanclass.getText().toString()) {

                        case "Department":
                            relprevbtn.setVisibility(View.VISIBLE);
                            txtheaderplanclass.setText("Category");
                            llayoutSalesAnalysis.setVisibility(View.GONE);
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
                                llayoutSalesAnalysis.setVisibility(View.GONE);
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
                                Log.e("Please select dept name", "");
                            }

                            break;

                        case "Plan Class":
                            Log.e("in sales analysis plan class", "-----" + planDept);

                            if (flag == true) {

                                txtheaderplanclass.setText("Brand");
                                llayoutSalesAnalysis.setVisibility(View.GONE);
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
                                Log.e("Please Select Dept name", "-------");
                            }


                            break;
                        case "Brand":
                            Log.e("in sales analysis brand", "-----" + planDept);

                            if (flag == true) {
                                relnextbtn.setVisibility(View.INVISIBLE);
                                txtheaderplanclass.setText("Brand Plan Class");
                                llayoutSalesAnalysis.setVisibility(View.GONE);
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
                                Log.e("Please select dept name", "------1");
                            }


                            break;

                    }
                }
            }
        });
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
                llhierarchy.setVisibility(View.GONE);
                currentVmPos= vwpagersales.getCurrentItem();
                Log.e(TAG, "currentVmPos: "+currentVmPos );
                currentIndex = listView_SalesAnalysis.getFirstVisiblePosition();
                Log.e(TAG," in WTD foucpos"+currentIndex);
                // save position here, and set position on API call's onPostexecute that scroll pos will get on Scroll method??
                saleFirstVisibleItem = " ";
                salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                llayoutSalesAnalysis.setVisibility(View.GONE);
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading data...");
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    requestSalesListDisplayAPI();
                }
                else
                {
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
                currentVmPos= vwpagersales.getCurrentItem();
                llhierarchy.setVisibility(View.GONE);
                currentIndex = listView_SalesAnalysis.getFirstVisiblePosition();
                Log.e(TAG," in LW foucpos"+currentIndex);
                saleFirstVisibleItem = " ";

                salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                llayoutSalesAnalysis.setVisibility(View.GONE);
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading data...");
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    requestSalesListDisplayAPI();
                }
                else
                {
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
                currentVmPos= vwpagersales.getCurrentItem();
                llhierarchy.setVisibility(View.GONE);
                currentIndex = listView_SalesAnalysis.getFirstVisiblePosition();
                Log.e(TAG," in L4W foucpos"+currentIndex);
                saleFirstVisibleItem = " ";
                salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                llayoutSalesAnalysis.setVisibility(View.GONE);
                if (Reusable_Functions.chkStatus(context))
                {
                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading data...");
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    requestSalesListDisplayAPI();

                } else
                {
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
                currentVmPos= vwpagersales.getCurrentItem();
                llhierarchy.setVisibility(View.GONE);
                currentIndex = listView_SalesAnalysis.getFirstVisiblePosition();
                Log.e(TAG," in YTD foucpos"+currentIndex);
                saleFirstVisibleItem = " ";
                salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                llayoutSalesAnalysis.setVisibility(View.GONE);
                if (Reusable_Functions.chkStatus(context))
                {
                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading data...");
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    requestSalesListDisplayAPI();
                }
                else
                {
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

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
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
                            } else if (response.length() == limit) {
                                for (i = 0; i < response.length(); i++) {
                                    salesAnalysisClass = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisClass);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesListDisplayAPI();

                            } else if (response.length() < limit) {
                                for (i = 0; i < response.length(); i++) {
                                    salesAnalysisClass = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisClass);
                                }

                                for (i = 0; i < 3; i++) {
                                    ImageView imgdot = new ImageView(context);
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
                                    layoutParams.setMargins(3, 3, 3, 3);
                                    imgdot.setLayoutParams(layoutParams);
                                    imgdot.setImageResource(R.mipmap.dots_unselected);
                                    lldots.addView(imgdot);

                                }
                                int currentItem = vwpagersales.getCurrentItem();
                                Log.e("----"," "+vwpagersales.getCurrentItem());
                                ImageView img = (ImageView) lldots.getChildAt(currentItem);
                                img.setImageResource(R.mipmap.dots_selected);


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
                                salesadapter = new SalesAnalysisAdapter(salesAnalysisClassArrayList, context, fromWhere, listView_SalesAnalysis);

                                listView_SalesAnalysis.setAdapter(salesadapter);
                                Log.e(TAG,"focusPosition in API----"+currentIndex);

                                listView_SalesAnalysis.setSelection(currentIndex);
                                listView_SalesAnalysis.smoothScrollToPosition(currentIndex);


                                Log.e("Current Index",""+currentIndex);

                                txtStoreCode.setText("" + salesAnalysisClassArrayList.get(i).getStoreCode());
                               // Log.e("storecode", "------" + salesAnalysisClassArrayList.get(1).getStoreCode());
                                txtStoreDesc.setText("" + salesAnalysisClassArrayList.get(i).getStoreDesc());
                                //llayoutSalesAnalysis.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                requestSalesViewPagerValueAPI();

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
    //Api 1.19 for view pager values on store level like wtd , lw

    private void requestSalesViewPagerValueAPI() {
        //Log.e("saleFirstVisibleItem in Api",""+saleFirstVisibleItem);

        String url = ConstsCore.web_url + "/v1/display/salesanalysisbytime/" + userId + "?view=" + selectedsegValue + "&level=" + level +"&offset=" + offsetvalue + "&limit=" + limit;

        Log.e("Url", "" + url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("view pager response", "" + response);

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
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

                            Log.i("----getCurrentItem---",""+vwpagersales.getCurrentItem());
                               pageradapter = new SalesPagerAdapter(context, analysisArrayList, focusposition, vwpagersales, lldots, salesadapter, listView_SalesAnalysis, salesAnalysisClassArrayList, fromWhere, pageradapter);
                               vwpagersales.setAdapter(pageradapter);
                               vwpagersales.setCurrentItem(currentVmPos);
                            pageradapter.notifyDataSetChanged();
                            //txtZonalSales.setText("" + salesAnalysis.getZonalSalesRank());
//                            txtNationalSales.setText("" + salesAnalysis.getNationalSalesRank());
//                            txtZonalYOY.setText("" + salesAnalysis.getZonalYOYGrowthRank());
//                            txtNationalYOY.setText("" + salesAnalysis.getNationalYOYGrowthRank());

                            // Log.i("Sales Zonal Rank",""+salesAnalysis.getZonalSalesRank());

                            llayoutSalesAnalysis.setVisibility(View.VISIBLE);
                            Reusable_Functions.hDialog();


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

    // APi to display view pager value on scroll
    private void requestSalesPagerOnScrollAPI() {

        Log.e("Department onsroll api", "" + saleFirstVisibleItem);

        String url = "";

        if (txtheaderplanclass.getText().toString().equals("Department"))
        {
            url = ConstsCore.web_url + "/v1/display/salesanalysisbytime/" + userId + "?view=" + selectedsegValue + "&department=" + saleFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        }
        else if (txtheaderplanclass.getText().toString().equals("Category"))
        {
            url = ConstsCore.web_url + "/v1/display/salesanalysisbytime/" + userId + "?view=" + selectedsegValue + "&category=" + saleFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        }
        else if (txtheaderplanclass.getText().toString().equals("Plan Class"))
        {
            url = ConstsCore.web_url + "/v1/display/salesanalysisbytime/" + userId + "?view=" + selectedsegValue + "&class=" + saleFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        }
        else if (txtheaderplanclass.getText().toString().equals("Brand"))
        {
            url = ConstsCore.web_url + "/v1/display/salesanalysisbytime/" + userId + "?view=" + selectedsegValue + "&brand=" + saleFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        }
        else if (txtheaderplanclass.getText().toString().equals("Brand Plan Class"))
        {
            url = ConstsCore.web_url + "/v1/display/salesanalysisbytime/" + userId + "?view=" + selectedsegValue + "&brandclass=" + saleFirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        }
        Log.e("Url", "" + url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Sales Analysis by time : ", " " + response);
                        Log.e("Sales View Pager response", "" + response.length());
                        try {

                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();

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
                                Log.e("salesAnalysisClassArrayList", "---" + salesAnalysisClassArrayList);
                                pageradapter = new SalesPagerAdapter(context, analysisArrayList, focusposition, vwpagersales, lldots, salesadapter, listView_SalesAnalysis, salesAnalysisClassArrayList, fromWhere, pageradapter);
                                //Log.i("-------",""+analysisArrayList.size());
                                vwpagersales.setAdapter(pageradapter);
                                vwpagersales.setCurrentItem(currentVmPos);
                                pageradapter.notifyDataSetChanged();
//                                txtZonalSales.setText("" + salesAnalysis.getZonalSalesRank());
//                                txtNationalSales.setText("" + salesAnalysis.getNationalSalesRank());
//                                txtZonalYOY.setText("" + salesAnalysis.getZonalYOYGrowthRank());
//                                txtNationalYOY.setText("" + salesAnalysis.getNationalYOYGrowthRank());

                                llayoutSalesAnalysis.setVisibility(View.VISIBLE);
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

    private void requestSalesCategoryList(final String deptName) {

        String salespvacategory_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&department=" + deptName.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + salespvacategory_listurl);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, salespvacategory_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Sales Category List: ", " " + response);
                        Log.i("Sales Category List response length", "" + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no category data found", Toast.LENGTH_SHORT).show();
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
                                Log.e("----"," "+vwpagersales.getCurrentItem());
                                ImageView img = (ImageView) lldots.getChildAt(currentItem);
                                img.setImageResource(R.mipmap.dots_selected);

                                salesadapter = new SalesAnalysisAdapter(salesAnalysisClassArrayList, context, fromWhere, listView_SalesAnalysis);
                                listView_SalesAnalysis.setAdapter(salesadapter);
                                flag = true;
                                salesadapter.notifyDataSetChanged();
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

    private void requestSalesPlanClassListAPI(String deptName, final String category) {

        Log.e("planDeptin plan class", "----" + planDept);

        String salespva_planclass_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&department=" + planDept.replaceAll(" ", "%20").replaceAll("&", "%26") + "&category=" + category.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + salespva_planclass_listurl);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, salespva_planclass_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Sales Plan Class List : ", " " + response);
                        Log.i("Sales Plan Class List response length", "" + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no plan class data found", Toast.LENGTH_SHORT).show();
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
                                Log.e("----"," "+vwpagersales.getCurrentItem());
                                ImageView img = (ImageView) lldots.getChildAt(currentItem);
                                img.setImageResource(R.mipmap.dots_selected);

                                salesadapter = new SalesAnalysisAdapter(salesAnalysisClassArrayList, context, fromWhere, listView_SalesAnalysis);
                                listView_SalesAnalysis.setAdapter(salesadapter);
                                flag = true;
                                salesadapter.notifyDataSetChanged();
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
                                saleFirstVisibleItem = salesAnalysisClassArrayList.get(focusposition).getPlanClass();
                                Log.e("saleFirstVisibleItem in plan class list", "-----" + saleFirstVisibleItem);
                                requestSalesPagerOnScrollAPI();
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

    private void requestSalesBrandListAPI(String deptName, String category, final String planclass) {

        String salespva_brand_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&department=" + planDept.replaceAll(" ", "%20").replaceAll("&", "%26") + "&category=" + planCategory.replaceAll(" ", "%20").replaceAll("&", "%26") + "&class=" + planclass.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + salespva_brand_listurl);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, salespva_brand_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Sales brand List : ", " " + response);
                        Log.i("Sales brand List response length", "" + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no brand name data found", Toast.LENGTH_SHORT).show();
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
                                Log.e("----"," "+vwpagersales.getCurrentItem());
                                ImageView img = (ImageView) lldots.getChildAt(currentItem);
                                img.setImageResource(R.mipmap.dots_selected);

                                salesadapter = new SalesAnalysisAdapter(salesAnalysisClassArrayList, context, fromWhere, listView_SalesAnalysis);
                                listView_SalesAnalysis.setAdapter(salesadapter);
                                salesadapter.notifyDataSetChanged();
                                flag = true;
                                txtStoreCode.setText(salesAnalysisClassArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(salesAnalysisClassArrayList.get(0).getStoreDesc());
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

    private void requestSalesBrandPlanListAPI(String deptName, String category, final String plan_class, final String brandnm) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        String userId = sharedPreferences.getString("userId", "");
        final String bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();

        String salespva_brandplan_listurl = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + selectedsegValue + "&level=" + level + "&department=" + planDept.replaceAll(" ", "%20").replaceAll("&", "%26") + "&category=" + planCategory.replaceAll(" ", "%20").replaceAll("&", "%26") + "&class=" + planClass.replaceAll(" ", "%20").replaceAll("&", "%26") + "&brand=" + brandnm.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + salespva_brandplan_listurl);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, salespva_brandplan_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Sales Brand Plan Class List : ", " " + response);
                        Log.i("Sales Brand Plan Class List response length", "" + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no brand plan class data found", Toast.LENGTH_SHORT).show();

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
                                Log.e("----"," "+vwpagersales.getCurrentItem());
                                ImageView img = (ImageView) lldots.getChildAt(currentItem);
                                img.setImageResource(R.mipmap.dots_selected);

                                salesadapter = new SalesAnalysisAdapter(salesAnalysisClassArrayList, context, fromWhere, listView_SalesAnalysis);
                                listView_SalesAnalysis.setAdapter(salesadapter);
                                salesadapter.notifyDataSetChanged();

                                txtStoreCode.setText(salesAnalysisClassArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(salesAnalysisClassArrayList.get(0).getStoreDesc());
                                 String brnd = brandnm.substring(0,1).toUpperCase()+brandnm.substring(1).toLowerCase();
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
        salesadapter = new SalesAnalysisAdapter(salesAnalysisClassArrayList, context, fromWhere, listView_SalesAnalysis);
        listView_SalesAnalysis.setAdapter(salesadapter);


    }
}
