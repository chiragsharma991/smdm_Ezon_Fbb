package apsupportapp.aperotechnologies.com.designapp.SalesAnalysis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
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
import apsupportapp.aperotechnologies.com.designapp.DashBoardActivity;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisListDisplay;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisViewPagerValue;
import info.hoang8f.android.segmented.SegmentedGroup;

import static android.view.View.FOCUS_UP;


public class SalesAnalysisActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    SegmentedGroup segmentedGroupSales;
    static  LinearLayout llayoutSalesAnalysis;
    SalesAnalysisListDisplay salesAnalysisClass;
    SalesAnalysisViewPagerValue salesAnalysis;
    ArrayList<SalesAnalysisViewPagerValue> analysisArrayList, salesList;
    ArrayList<SalesAnalysisListDisplay> salesAnalysisClassArrayList;
    static ViewPager vwpagersales;
    static LinearLayout lldots;
    RelativeLayout relLayoutSales;
    static ListView listView_SalesAnalysis;
    SalesAnalysisAdapter salesadapter;
    Context context;
    SalesPagerAdapter pageradapter;
    SharedPreferences sharedPreferences;
    String userId, bearertoken;

    RelativeLayout btnBack;
    RadioButton btnWTD;
    public static String selectedsegValue = "WTD";
    String saleFirstVisibleItem;
    static String fromWhere = "Department";
    String prodName = "KNIT CHURIDAR";
    static TextView txtStoreCode, txtStoreDesc;
    int offsetvalue = 0, limit = 100;
    int count = 0 ,level;
    RequestQueue queue;

    Gson gson;
    int focusposition = 0;
    View view;


    LinearLayout rankLayout;
    RelativeLayout relimgrank, relimgfilter;
    RelativeLayout relprevbtn, relnextbtn, relimgclose;
    static TextView txtheaderplanclass;
    static TextView txtZonalSales,txtNationalSales;
    static TextView txtZonalYOY,txtNationalYOY;
    TextView txtNetSalesPerc,txtPlanSalesPerc,txtNetSalesUPerc;

    int selFirstPositionValue = 0;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesanalysis);
        getSupportActionBar().hide();
        fromWhere = "Department";
        context = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        selFirstPositionValue = 0;
        txtStoreCode = (TextView) findViewById(R.id.txtStoreCode);
        txtStoreDesc = (TextView) findViewById(R.id.txtStoreName);

        btnBack = (RelativeLayout) findViewById(R.id.imageBtnBack);
        segmentedGroupSales = (SegmentedGroup) findViewById(R.id.segmentedGrp);
        segmentedGroupSales.setOnCheckedChangeListener(this);

        btnWTD = (RadioButton) findViewById(R.id.btnWTD);
        btnWTD.toggle();

        llayoutSalesAnalysis = (LinearLayout) findViewById(R.id.llayoutSalesAnalysis);
        llayoutSalesAnalysis.setVisibility(View.GONE);


        relimgfilter = (RelativeLayout) findViewById(R.id.imgfilter);
        relimgrank = (RelativeLayout) findViewById(R.id.imgrank);
        relprevbtn = (RelativeLayout) findViewById(R.id.prevplanclass);
        relnextbtn = (RelativeLayout) findViewById(R.id.nextplanclass);
        txtheaderplanclass = (TextView) findViewById(R.id.headerplanclass);

        //Rank UI Components
        rankLayout = (LinearLayout) findViewById(R.id.rankLayout);
        txtZonalSales =(TextView) findViewById(R.id.txtZonalSales);
        txtNationalSales =(TextView)findViewById(R.id.txtNationalSales);
        txtZonalYOY = (TextView) findViewById(R.id.txtZonalYOY);
        txtNationalYOY =(TextView)findViewById(R.id.txtNationalYOY);

        //WOW & YOY UI Components



        txtheaderplanclass.setText("Department");

        relimgclose = (RelativeLayout) findViewById(R.id.relimgclose);

        RelativeLayout relpopuplayout = (RelativeLayout) findViewById(R.id.relpopuplayout);
        relpopuplayout.setOnClickListener(null);

        RelativeLayout relrankLayout = (RelativeLayout) findViewById(R.id.relrankLayout);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Intent i = new Intent (SalesAnalysisActivity.this, DashBoardActivity.class);
                startActivity(i);
            }
        });

        relrankLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rankLayout.setVisibility(View.GONE);
            }
        });

        relimgclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rankLayout.setVisibility(View.GONE);
            }
        });

        relimgrank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rankLayout.getVisibility() == View.VISIBLE) {
                    rankLayout.setVisibility(View.GONE);
                } else if (rankLayout.getVisibility() == View.GONE) {
                    rankLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        relimgfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SalesAnalysisActivity.this,SalesFilterActivity.class);
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
        vwpagersales.setCurrentItem(0);
        lldots = (LinearLayout) findViewById(R.id.lldots);
        lldots.setOrientation(LinearLayout.HORIZONTAL);
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
            offsetvalue = 0;
            limit = 100;
            count = 0;
            level = 1;
            requestSalesListDisplayAPI();

        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }

        listView_SalesAnalysis.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (salesAnalysisClassArrayList.size() != 0) {

                    if (view.getFirstVisiblePosition() <= salesAnalysisClassArrayList.size() - 1) {

                        focusposition = view.getFirstVisiblePosition();

                        listView_SalesAnalysis.setSelection(view.getFirstVisiblePosition());
                        //Log.e("firstVisibleItem", " " + view.getFirstVisiblePosition() + " " + salesAnalysisClassArrayList.get(view.getFirstVisiblePosition()).getPlanDept());

                        if(txtheaderplanclass.getText().toString().equals("Department"))
                        {
                            saleFirstVisibleItem = salesAnalysisClassArrayList.get(listView_SalesAnalysis.getFirstVisiblePosition()).getPlanDept().toString();

                        }
                        else if(txtheaderplanclass.getText().toString().equals("Category"))

                        {
                            Log.e("saleFirstVisibleItem","-----"+salesAnalysisClassArrayList.size());
                            saleFirstVisibleItem = salesAnalysisClassArrayList.get(listView_SalesAnalysis.getFirstVisiblePosition()).getPlanCategory().toString();
                        }
                        else if(txtheaderplanclass.getText().toString().equals("Plan Class"))
                        {
                            saleFirstVisibleItem = salesAnalysisClassArrayList.get(listView_SalesAnalysis.getFirstVisiblePosition()).getPlanClass().toString();
                        }
                        else if(txtheaderplanclass.getText().toString().equals("Brand"))
                        {
                            saleFirstVisibleItem = salesAnalysisClassArrayList.get(listView_SalesAnalysis.getFirstVisiblePosition()).getBrandName().toString();
                        }
                        else if(txtheaderplanclass.getText().toString().equals("Brand Plan Class"))
                        {
                            saleFirstVisibleItem = salesAnalysisClassArrayList.get(listView_SalesAnalysis.getFirstVisiblePosition()).getBrandplanClass().toString();
                        }
                        Log.e("analysisArrayList", " " + analysisArrayList.size());

                        if(focusposition != selFirstPositionValue) {
                            if (Reusable_Functions.chkStatus(context)) {
                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                SalesPagerAdapter.currentPage = 0;
                                if (lldots != null) {
                                    lldots.removeAllViews();
                                }
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
                                requestSalesPagerOnScrollAPI();

                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }
                        }
                        selFirstPositionValue = focusposition;

                    } else {
                        focusposition = salesAnalysisClassArrayList.size() - 1;
                        listView_SalesAnalysis.setSelection(salesAnalysisClassArrayList.size() - 1);
                        if (lldots != null) {
                            lldots.removeAllViews();
                        }
//                        reqy
                        selFirstPositionValue = focusposition;

                    }
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


//        listView_SalesAnalysis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//               Log.e("position","-----"+position);
//                listView_SalesAnalysis.smoothScrollToPosition(position);
//                Log.e("here","----"+position);
//
//            }
//        });

        relprevbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (txtheaderplanclass.getText().toString()) {

                    case "Brand Plan Class":
                        //relnextbtn.setVisibility(View.VISIBLE);
                        SalesPagerAdapter.currentPage = 0;
                        if (lldots != null) {
                            lldots.removeAllViews();
                        }
                        txtheaderplanclass.setText("Brand");
                        fromWhere = "Brand";
                        level = 4;
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        llayoutSalesAnalysis.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.e("Brand Class Prev-- ","  ");
                            requestSalesListDisplayAPI();
                            Log.e("prev 1",""+salesAnalysisClass.getBrandName());

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case "Brand":
                        SalesPagerAdapter.currentPage = 0;
                        if (lldots != null) {
                            lldots.removeAllViews();
                        }
                        txtheaderplanclass.setText("Plan Class");
                        fromWhere = "Plan Class";
                        level = 3;
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        llayoutSalesAnalysis.setVisibility(View.GONE);

                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.e("Brand name prev","--");
                            requestSalesListDisplayAPI();
                            Log.e("prev 2",""+salesAnalysisClass.getPlanClass());

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("---2---", " ");

                        break;


                    case "Plan Class":
                        SalesPagerAdapter.currentPage = 0;
                        if (lldots != null) {
                            lldots.removeAllViews();
                        }

                        txtheaderplanclass.setText("Category");
                        fromWhere = "Category";
                        level = 2;
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        llayoutSalesAnalysis.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.e("Plan class prev","");
                            requestSalesListDisplayAPI();


                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("---3---", " ");

                        break;

                    case "Category":

                        //relprevbtn.setVisibility(View.GONE);
                        //relnextbtn.setVisibility(View.VISIBLE);
                        SalesPagerAdapter.currentPage = 0;
                        if (lldots != null) {
                            lldots.removeAllViews();
                        }

                        txtheaderplanclass.setText("Department");
                        fromWhere = "Department";
                        level = 1;
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        llayoutSalesAnalysis.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.e("Category prev","");
                            requestSalesListDisplayAPI();
                            Log.e("prev 4",""+salesAnalysisClass.getPlanDept());

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
                        //relprevbtn.setVisibility(View.VISIBLE);

                        txtheaderplanclass.setText("Category");
                        SalesPagerAdapter.currentPage = 0;
                        if (lldots != null) {
                            lldots.removeAllViews();
                        }
                        fromWhere = "Category";
                        level = 2;
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        llayoutSalesAnalysis.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.i("dept next","-----");
                            requestSalesListDisplayAPI();
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case "Category":
                        fromWhere = "Plan Class";
                        txtheaderplanclass.setText("Plan Class");
                        level = 3;
                        SalesPagerAdapter.currentPage = 0;
                        if (lldots != null) {
                            lldots.removeAllViews();
                        }
                        salesAnalysisClassArrayList = new ArrayList<SalesAnalysisListDisplay>();
                        llayoutSalesAnalysis.setVisibility(View.GONE);

                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.e("category next --","");
                            requestSalesListDisplayAPI();
                           Log.e("next 2",""+salesAnalysisClass.getPlanClass());

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("---2---", " ");

                        break;


                    case "Plan Class":
                        txtheaderplanclass.setText("Brand");
                        fromWhere = "Brand";
                        level = 4;
                        SalesPagerAdapter.currentPage = 0;
                        if (lldots != null) {
                            lldots.removeAllViews();
                        }
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
                        //relnextbtn.setVisibility(View.GONE);
                        SalesPagerAdapter.currentPage = 0;
                        if (lldots != null) {
                            lldots.removeAllViews();
                        }
                        fromWhere = "Brand Plan Class";
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

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {

            case R.id.btnWTD:
                if (selectedsegValue.equals("WTD"))
                    break;

                selectedsegValue = "WTD";
                SalesPagerAdapter.currentPage = 0;
                if (lldots != null) {
                    lldots.removeAllViews();
                }

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

                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
                Log.e("---1---", " ");

                break;

            case R.id.btnLW:
                if (selectedsegValue.equals("LW"))
                    break;
                selectedsegValue = "LW";
                SalesPagerAdapter.currentPage = 0;
                if (lldots != null) {
                    lldots.removeAllViews();
                }
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

                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
                Log.e("---2---", " ");

                break;

            case R.id.btnL4W:
                if (selectedsegValue.equals("L4W"))
                    break;
                selectedsegValue = "L4W";
                SalesPagerAdapter.currentPage = 0;
                if (lldots != null) {
                    lldots.removeAllViews();
                }
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

                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
                Log.e("---3---", " ");

                break;

            case R.id.btnYTD:
                if (selectedsegValue.equals("YTD"))
                    break;
                selectedsegValue = "YTD";
                SalesPagerAdapter.currentPage = 0;
                if (lldots != null) {
                    lldots.removeAllViews();
                }

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

        String url = ConstsCore.web_url + "/v1/display/salesanalysisoptedbytime/" + userId + "?view=" + selectedsegValue  + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Sales Analysis Class: ", " " + response);
                        Log.i("response length", "" + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    salesAnalysisClass = gson.fromJson(response.get(i).toString(), SalesAnalysisListDisplay.class);
                                    salesAnalysisClassArrayList.add(salesAnalysisClass);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSalesListDisplayAPI();

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
                                    ImageView img = (ImageView) lldots.getChildAt(0);
                                    img.setImageResource(R.mipmap.dots_selected);
                                }

                                salesadapter = new SalesAnalysisAdapter(salesAnalysisClassArrayList, context, fromWhere);
                                listView_SalesAnalysis.setAdapter(salesadapter);
                                txtStoreCode.setText(salesAnalysisClass.getStoreCode());
                                txtStoreDesc.setText(salesAnalysisClass.getStoreDesc());
                                llayoutSalesAnalysis.setVisibility(View.VISIBLE);
                                //Reusable_Functions.hDialog();

                                //if (Reusable_Functions.chkStatus(context)) {

//                                    Reusable_Functions.hDialog();
//                                    Reusable_Functions.sDialog(context, "Loading data...");
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

        String url = ConstsCore.web_url + "/v1/display/salesanalysisbytime/" + userId + "?view=" + selectedsegValue + "&offset=" + offsetvalue + "&limit=" + limit;

        Log.e("Url", "" + url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("view pager response",""+response);

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
                            pageradapter = new SalesPagerAdapter(context, analysisArrayList, focusposition, vwpagersales, lldots, salesadapter, listView_SalesAnalysis);
                            //Log.i("-------",""+analysisArrayList.size());
                            vwpagersales.setAdapter(pageradapter);
                            txtZonalSales.setText("" + salesAnalysis.getZonalSalesRank());
                            txtNationalSales.setText("" + salesAnalysis.getNationalSalesRank());
                            txtZonalYOY.setText("" + salesAnalysis.getZonalYOYGrowthRank());
                            txtNationalYOY.setText("" + salesAnalysis.getNationalYOYGrowthRank());

                            // Log.i("Sales Zonal Rank",""+salesAnalysis.getZonalSalesRank());

                            //pageradapter.notifyDataSetChanged();
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

        if(txtheaderplanclass.getText().toString().equals("Department"))
        {
            url = ConstsCore.web_url + "/v1/display/salesanalysisbytime/" + userId + "?view=" + selectedsegValue + "&department=" + saleFirstVisibleItem.toUpperCase().replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        }
        else if(txtheaderplanclass.getText().toString().equals("Category"))
        {
            url = ConstsCore.web_url + "/v1/display/salesanalysisbytime/" + userId + "?view=" + selectedsegValue + "&category=" + saleFirstVisibleItem.toUpperCase().replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        }
        else if(txtheaderplanclass.getText().toString().equals("Plan Class"))
        {
            url = ConstsCore.web_url + "/v1/display/salesanalysisbytime/" + userId + "?view=" + selectedsegValue + "&class=" + saleFirstVisibleItem.toUpperCase().replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        }
        else if(txtheaderplanclass.getText().toString().equals("Brand"))
        {
            url = ConstsCore.web_url + "/v1/display/salesanalysisbytime/" + userId + "?view=" + selectedsegValue + "&brand=" + saleFirstVisibleItem.toUpperCase().replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        }
        else if(txtheaderplanclass.getText().toString().equals("Brand Plan Class"))
        {
            url = ConstsCore.web_url + "/v1/display/salesanalysisbytime/" + userId + "?view=" + selectedsegValue + "&brandclass=" + saleFirstVisibleItem.toUpperCase().replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        }
        Log.e("Url", "" + url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Sales Analysis by time : ", " " + response);
                        Log.i("Sales View Pager response", "" + response.length());
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

                                for (int i = 0; i < 3; i++) {
                                    ImageView imgdot = new ImageView(context);
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
                                    layoutParams.setMargins(3, 3, 3, 3);
                                    imgdot.setLayoutParams(layoutParams);
                                    imgdot.setImageResource(R.mipmap.dots_unselected);
                                    lldots.addView(imgdot);
                                    ImageView img = (ImageView) lldots.getChildAt(0);
                                    img.setImageResource(R.mipmap.dots_selected);
                                }
                                pageradapter = new SalesPagerAdapter(context, analysisArrayList, focusposition, vwpagersales, lldots, salesadapter, listView_SalesAnalysis);
                                //Log.i("-------",""+analysisArrayList.size());
                                vwpagersales.setAdapter(pageradapter);
                                txtZonalSales.setText("" + salesAnalysis.getZonalSalesRank());
                                txtNationalSales.setText("" + salesAnalysis.getNationalSalesRank());
                                txtZonalYOY.setText("" + salesAnalysis.getZonalYOYGrowthRank());
                                txtNationalYOY.setText("" + salesAnalysis.getNationalYOYGrowthRank());

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


    @Override
    public void onBackPressed() {

        if (rankLayout.getVisibility() == View.VISIBLE) {
            rankLayout.setVisibility(View.GONE);
        } else {
            SalesPagerAdapter.currentPage = 0;
            Intent i = new Intent(SalesAnalysisActivity.this, DashBoardActivity.class);
            startActivity(i);
            finish();
        }

    }


}
