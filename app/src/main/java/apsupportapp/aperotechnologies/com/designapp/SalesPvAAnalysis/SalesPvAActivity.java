package apsupportapp.aperotechnologies.com.designapp.SalesPvAAnalysis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
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
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.ProductNameBean;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.SalesPvAAnalysis.SalesFilterActivity;
import apsupportapp.aperotechnologies.com.designapp.SalesPvAAnalysis.SalesPvAAdapter;
import info.hoang8f.android.segmented.SegmentedGroup;

public class SalesPvAActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {


    RadioButton radioButton;
    static String salesPvA_SegmentClick="WTD";
    ArrayList<ProductNameBean> productNameBeanArrayList;
    RelativeLayout relativeLayout,tableRelLayout,relChartLayout;
    ProductNameBean productNameBean;
    TextView txtStoreCode, txtStoreDesc;
    String userId, bearertoken;
    SharedPreferences sharedPreferences;
    int offsetvalue = 0, limit = 100;
    int count = 0;
    RequestQueue queue;
    SalesPvAAdapter adapter;
    CombinedChart combinedChart;
    ViewPortHandler handler;
    Context context;
    public  ListView listViewSales;
    EditText etFirstPosition;
    int focusposition = 0;
    CombinedData data;
    String prodName = "KNIT CHURIDAR";
    static TextView txtPlanClass;
    RelativeLayout btnBack,btnFilter;
    RelativeLayout btnSalesPrev,btnSalesNext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salespva);
        context = this;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int densityDpi = (int)(metrics.density * 160f);

        Log.e("densityDPi---",""+densityDpi);
        // Display device width and height in pixels
        int screenHeightpx = metrics.heightPixels;
        int screenWidthpx  = metrics.widthPixels;

        Log.e("screen height--",""+screenHeightpx + "screen width"+screenWidthpx);

        getSupportActionBar().hide();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        txtStoreCode = (TextView) findViewById(R.id.txtStoreCode);
        txtStoreDesc = (TextView) findViewById(R.id.txtStoreName);
      //  relativeLayout = (RelativeLayout) findViewById(R.id.relLayout1);
        radioButton = (RadioButton) findViewById(R.id.btn_wtd);
        radioButton.toggle();
        tableRelLayout=(RelativeLayout)findViewById(R.id.relTablelayout);
        tableRelLayout.setVisibility(View.GONE);
        relChartLayout=(RelativeLayout)findViewById(R.id.relChartlayout);
        relChartLayout.setVisibility(View.GONE);
        combinedChart = (CombinedChart) findViewById(R.id.barline_chart);


        handler = combinedChart.getViewPortHandler();
        handler.getScaleX();
        Log.e("scale x---", "" + handler.getScaleX());
        SegmentedGroup segmented3 = (SegmentedGroup) findViewById(R.id.segmented3);
        segmented3.setOnCheckedChangeListener(SalesPvAActivity.this);
        initializeValues(context);
        txtPlanClass=(TextView)findViewById(R.id.txtPlanClass);
        Log.e("text value",""+txtPlanClass.getText().toString());

        listViewSales = (ListView)findViewById(R.id.list);
        listViewSales.addFooterView(getLayoutInflater().inflate(R.layout.salespva_list_footer_view,null));




        if (Reusable_Functions.chkStatus(SalesPvAActivity.this)) {
            Log.e("WTD click ", "");
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(SalesPvAActivity.this, "Loading data...");
            offsetvalue = 0;
            limit = 100;
            count = 0;
            requestProductAPI(context);
        } else
        {

            Toast.makeText(SalesPvAActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
        }
        btnSalesPrev=(RelativeLayout)findViewById(R.id.btnSalesBack);
        btnSalesPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Toast.makeText(context,"txtValue on Back Click"+txtPlanClass.getText().toString(),Toast.LENGTH_SHORT).show();
//                switch(salesPvA_SegmentClick)
//                {
//                    case "WTD":
//                        if(txtPlanClass.getText().toString().equals("Brand Plan Class"))
//                        {
//
//                            Toast.makeText(context,"click1"+txtPlanClass.getText().toString(),Toast.LENGTH_SHORT).show();
//                            txtPlanClass.setText("Brand");
//                        }
//                        else if(txtPlanClass.getText().toString().equals("Brand"))
//                        {
//
//                            Toast.makeText(context,"click2"+txtPlanClass.getText().toString(),Toast.LENGTH_SHORT).show();
//                            txtPlanClass.setText("Plan Class");
//                        }
//                        else if(txtPlanClass.getText().toString().equals("Plan Class"))
//                        {
//
//                            Toast.makeText(context,"click3"+txtPlanClass.getText().toString(),Toast.LENGTH_SHORT).show();
//                            txtPlanClass.setText("Category");
//                        }
//                        else if(txtPlanClass.getText().toString().equals("Category"))
//                        {
//
//                            Toast.makeText(context,"click4"+txtPlanClass.getText().toString(),Toast.LENGTH_SHORT).show();
//                            txtPlanClass.setText("Department");
//                        }
//                        else if(txtPlanClass.getText().toString().equals("Department"))
//                        {
//
//                            Toast.makeText(context,"click5"+txtPlanClass.getText().toString(),Toast.LENGTH_SHORT).show();
//
//                        }
//                    case "LW":
//                        if(txtPlanClass.getText().toString().equals("Brand Plan Class"))
//
//                        {
//
//                            Toast.makeText(context,"click1"+txtPlanClass.getText().toString(),Toast.LENGTH_SHORT).show();
//                            txtPlanClass.setText("Brand");
//                        }
//                        else if(txtPlanClass.getText().toString().equals("Brand"))
//                        {
//
//                            Toast.makeText(context,"click2"+txtPlanClass.getText().toString(),Toast.LENGTH_SHORT).show();
//                            txtPlanClass.setText("Plan Class");
//                        }
//                        else if(txtPlanClass.getText().toString().equals("Plan Class"))
//                        {
//
//                            Toast.makeText(context,"click3"+txtPlanClass.getText().toString(),Toast.LENGTH_SHORT).show();
//                            txtPlanClass.setText("Category");
//                        }
//                        else if(txtPlanClass.getText().toString().equals("Category"))
//                        {
//
//                            Toast.makeText(context,"click4"+txtPlanClass.getText().toString(),Toast.LENGTH_SHORT).show();
//                            txtPlanClass.setText("Department");
//                        }
//                        else if(txtPlanClass.getText().toString().equals("Department"))
//                        {
//
//                            Toast.makeText(context,"click5"+txtPlanClass.getText().toString(),Toast.LENGTH_SHORT).show();
//
//
//                        }
//
//                }
//
                switch (txtPlanClass.getText().toString()) {

                    case "Brand Class":
                        btnSalesNext.setVisibility(View.VISIBLE);
                        txtPlanClass.setText("Brand");

                        prodName = "GRAPHIC TEES";
                        productNameBeanArrayList = new ArrayList<ProductNameBean>();
                        tableRelLayout.setVisibility(View.GONE);
                        relChartLayout.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            requestProductAPI(context);

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
                        }

                        break;

                    case "Brand":
                        txtPlanClass.setText("Plan Class");

                        prodName = "KNIT CHURIDAR";
                        productNameBeanArrayList = new ArrayList<ProductNameBean>();
                        tableRelLayout.setVisibility(View.GONE);
                        relChartLayout.setVisibility(View.GONE);

                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            requestProductAPI(context);
                            //llayoutSalesAnalysis.setVisibility(View.VISIBLE);

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
                        }
                        Log.e("---2---", " ");

                        break;


                    case "Plan Class":
                        txtPlanClass.setText("Category");

                        prodName = "NEON POLO";
                        productNameBeanArrayList=new ArrayList<ProductNameBean>();
                        tableRelLayout.setVisibility(View.GONE);
                        relChartLayout.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            requestProductAPI(context);

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
                        }
                        Log.e("---4---", " ");

                        break;

                    case "Category":
                        txtPlanClass.setText("Department");
                        btnSalesPrev.setVisibility(View.GONE);
                        btnSalesNext.setVisibility(View.VISIBLE);

                        prodName = "CORE DENIM";
                        productNameBeanArrayList = new ArrayList<ProductNameBean>();
                        tableRelLayout.setVisibility(View.GONE);
                        relChartLayout.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            requestProductAPI(context);

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
                        }
                        Log.e("---4---", " ");

                        break;
                    default:
                }

            }

        });

        btnSalesNext=(RelativeLayout)findViewById(R.id.btnSalesNext);
        btnSalesNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//
//                Toast.makeText(context,"txtvalue :"+txtPlanClass.getText().toString(),Toast.LENGTH_SHORT).show();
//
//                //condition applied  when segment click - WTD and class level - department
//
//                if(salesPvA_SegmentClick.equals("WTD") && txtPlanClass.getText().toString().equals("Department")){
//
//                    Toast.makeText(context,"department click",Toast.LENGTH_SHORT).show();
//                    Log.e("btnSalesNext Click","");
//                    txtPlanClass.setText("Category");
//
//                    prodName = "DRESS";
//                    productNameBeanArrayList = new ArrayList<ProductNameBean>();
//                    tableRelLayout.setVisibility(View.GONE);
//                    //relChartLayout.setVisibility(View.GONE);
//                    if (Reusable_Functions.chkStatus(context)) {
//
//                        Reusable_Functions.hDialog();
//                        Reusable_Functions.sDialog(context, "Loading data...");
//                        offsetvalue = 0;
//                        limit = 100;
//                        count = 0;
//                        requestProductAPI(context);
//
//                    } else {
//                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
//                    }
//                    data = new CombinedData(getXAxisValues1());
//                    BarData bardata = new BarData(getXAxisValues(), getDataSet());
//                    data.setData(bardata);
//                    data.setData(lineData());
//                    combinedChart.animateXY(2000, 2000);
//                    combinedChart.setData(data);
//                    combinedChart.setDescription("");
//                    combinedChart.notifyDataSetChanged();
//                }
//                //condition applied when segment click - WTD and class level - category
//
//                 else if(salesPvA_SegmentClick.equals("WTD") && txtPlanClass.getText().toString().equals("Category"))
//                {
//                    Toast.makeText(context,"category click",Toast.LENGTH_SHORT).show();
//                    txtPlanClass.setText("Plan Class");
//                    prodName = "DEAL JEANS";
//                    productNameBeanArrayList = new ArrayList<ProductNameBean>();
//                    tableRelLayout.setVisibility(View.GONE);
//                    //relChartLayout.setVisibility(View.GONE);
//                    if (Reusable_Functions.chkStatus(context)) {
//
//                        Reusable_Functions.hDialog();
//                        Reusable_Functions.sDialog(context, "Loading data...");
//                        offsetvalue = 0;
//                        limit = 100;
//                        count = 0;
//                        requestProductAPI(context);
//
//                    } else {
//                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
//                    }
//                    data = new CombinedData(getXAxisValues1());
//                    BarData bardata = new BarData(getXAxisValues(), getDataSet());
//                    data.setData(bardata);
//                    data.setData(lineData());
//                    combinedChart.animateXY(2000, 2000);
//                    combinedChart.setData(data);
//                    combinedChart.setDescription("");
//                    combinedChart.notifyDataSetChanged();
//
//                }
//                //condition applied when segment click - WTD and class level - Plan Class
//                else if(salesPvA_SegmentClick.equals("WTD") && txtPlanClass.getText().toString().equals("Plan Class"))
//               {
//                   Toast.makeText(context,"plan class click ",Toast.LENGTH_SHORT).show();
//                   txtPlanClass.setText("Brand");
//               }
//
//               // condition applied when segment click - WTD and class level - Brand
//                else if(salesPvA_SegmentClick.equals("WTD") && txtPlanClass.getText().toString().equals("Brand"))
//                {
//                    Toast.makeText(context,"brand click ",Toast.LENGTH_SHORT).show();
//                    txtPlanClass.setText("Brand Plan Class");
//                }
                switch (txtPlanClass.getText().toString()) {

                    case "Department":
                        btnSalesPrev.setVisibility(View.VISIBLE);
                        txtPlanClass.setText("Category");

                        prodName = "NEON POLO";
                        productNameBeanArrayList = new ArrayList<ProductNameBean>();
                        tableRelLayout.setVisibility(View.GONE);
                        relChartLayout.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            requestProductAPI(context);

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
                        }
                        break;

                    case "Category":
                        txtPlanClass.setText("Plan Class");

                        prodName = "KNIT CHURIDAR";
                        productNameBeanArrayList = new ArrayList<ProductNameBean>();
                        tableRelLayout.setVisibility(View.GONE);
                        relChartLayout.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            requestProductAPI(context);
                            //llayoutSalesAnalysis.setVisibility(View.VISIBLE);

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
                        }
                        Log.e("---2---", " ");

                        break;


                    case "Plan Class":
                        txtPlanClass.setText("Brand");

                        prodName = "GRAPHIC TEES";
                        productNameBeanArrayList = new ArrayList<ProductNameBean>();
                        tableRelLayout.setVisibility(View.GONE);
                        relChartLayout.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            requestProductAPI(context);

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
                        }
                        Log.e("---4---", " ");

                        break;

                    case "Brand":
                        txtPlanClass.setText("Brand Class");
                        btnSalesNext.setVisibility(View.VISIBLE);
                        btnSalesPrev.setVisibility(View.GONE);

                        prodName = "DJ&C SHIRTS";
                        productNameBeanArrayList = new ArrayList<ProductNameBean>();
                        tableRelLayout.setVisibility(View.GONE);
                        relChartLayout.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            requestProductAPI(context);

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
                        }
                        Log.e("---4---", " ");

                        break;
                    default:
                }


            }
        });


        listViewSales.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
                if (scrollState == SCROLL_STATE_IDLE)
                {

                    if(productNameBeanArrayList.size() != 0)
                    {
                        //listView_SalesAnalysis.smoothScrollToPosition(firstVisibleItem);
                        if(view.getFirstVisiblePosition() <=  productNameBeanArrayList.size() - 1 )
                        {
                            focusposition = view.getFirstVisiblePosition();
                            listViewSales.setSelection(focusposition);
                            //Log.e("focusposition", " " + firstVisibleItem + " " + productNameBeanArrayList.get(firstVisibleItem).getProductName());
                            Log.e("list view scroll",""+ productNameBeanArrayList.size());
                            data = new CombinedData(getXAxisValues1());
                            BarData bardata = new BarData(getXAxisValues(), getDataSet());
                            data.setData(bardata);
                            data.setData(lineData());
                            combinedChart.animateXY(2000, 2000);
                            combinedChart.setData(data);
                            combinedChart.setDescription("");
                            combinedChart.notifyDataSetChanged();
                        }
                        else{
                            focusposition = productNameBeanArrayList.size() - 1;
                            listViewSales.setSelection(focusposition);
                            data = new CombinedData(getXAxisValues1());
                            BarData bardata = new BarData(getXAxisValues(), getDataSet());
                            data.setData(bardata);
                            data.setData(lineData());
                            combinedChart.animateXY(2000, 2000);
                            combinedChart.setData(data);
                            combinedChart.setDescription("");
                            combinedChart.notifyDataSetChanged();

                        }
                    }
//
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {



            }
        });
        btnBack=(RelativeLayout)findViewById(R.id.imageBtnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,DashBoardActivity.class);
//                startActivity(intent);
//                finish();
            }
        });
        btnFilter=(RelativeLayout)findViewById(R.id.imageBtnFilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent filterIntent= new Intent (SalesPvAActivity.this,SalesFilterActivity.class);
                startActivity(filterIntent);
                finish();
            }
        });


    }



    public void initializeValues(Context context) {

        Log.e("initializeValues---","");

        productNameBeanArrayList = new ArrayList<ProductNameBean>();

        data = new CombinedData(getXAxisValues());
        XAxis xAxis = combinedChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = combinedChart.getAxisLeft();
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = combinedChart.getAxisRight();
        rightAxis.setEnabled(false);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setTextColor(Color.WHITE);
        rightAxis.setDrawGridLines(true);
        // enable value highlighting
        combinedChart.setHighlightEnabled(true);
        // enable touch gestures

        combinedChart.setTouchEnabled(true);

        combinedChart.setDragEnabled(true);
        combinedChart.setScaleEnabled(true);
        combinedChart.setDrawGridBackground(false);

        combinedChart.setBackgroundColor(Color.WHITE);
        BarData bardata = new BarData(getXAxisValues(), getDataSet());
        data.setData(bardata);
        data.setData(lineData());
        combinedChart.animateXY(2000, 2000);
        combinedChart.setData(data);
        combinedChart.setDescription("");
        Legend l = combinedChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        l.setEnabled(true);

    }


    public static void setListViewHeightBasedOnChildren(ListView listViewSales) {
        SalesPvAAdapter salesPvAAdapter =  ((SalesPvAAdapter)((HeaderViewListAdapter)listViewSales.getAdapter()).getWrappedAdapter());
        salesPvAAdapter.notifyDataSetChanged();
        if (salesPvAAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < salesPvAAdapter.getCount() - 1; i++) {
            View listItem = salesPvAAdapter.getView(i, null, listViewSales);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listViewSales.getLayoutParams();
        params.height = totalHeight + (listViewSales.getDividerHeight() * (salesPvAAdapter.getCount() - 1));
        listViewSales.setLayoutParams(params);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {

            case R.id.btn_wtd:
                Toast.makeText(SalesPvAActivity.this, "WTD", Toast.LENGTH_SHORT).show();
                if (salesPvA_SegmentClick.equals("WTD"))
                    break;
                salesPvA_SegmentClick = "WTD";
                prodName = "KNIT CHURIDAR";
                productNameBeanArrayList = new ArrayList<ProductNameBean>();
                tableRelLayout.setVisibility(View.GONE);

                relChartLayout.setVisibility(View.GONE);
                if (Reusable_Functions.chkStatus(context)) {

                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading data...");
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    requestProductAPI(context);

                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
                }

                Log.e("-----WTD-----"," ");
                break;

            case R.id.btn_lw:
                Toast.makeText(SalesPvAActivity.this, "LW", Toast.LENGTH_SHORT).show();
                if (salesPvA_SegmentClick.equals("LW"))
                    break;

                salesPvA_SegmentClick = "LW";
                prodName = "DEAL JEANS";
                productNameBeanArrayList = new ArrayList<ProductNameBean>();
                tableRelLayout.setVisibility(View.GONE);
                relChartLayout.setVisibility(View.GONE);
                if (Reusable_Functions.chkStatus(context)) {

                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading data...");
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    requestProductAPI(context);

                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
                }

                Log.e("-----lw-----"," ");


                break;
//
            default:
                break;

        }
    }

    private void requestProductAPI(final Context context)
    {
        String url = ConstsCore.web_url + "/v1/display/hourlytransproducts/"+userId+"?view=articleOption&productName=" + prodName.replaceAll(" ", "%20").replaceAll("&","%26")+"&offset="+offsetvalue+"&limit="+limit;//ConstsCore.web_url + "/v1/display/hourlytransproducts/"+userId+"?offset="+offsetvalue+"&limit="+ limit;
        Log.i("URL   ", url + " "+bearertoken);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            if (response.equals(null) || response == null|| response.length()==0 && count==0)
                            {
                                Reusable_Functions.hDialog();
                                Toast.makeText(SalesPvAActivity.this.context, "no product data found", Toast.LENGTH_LONG).show();
                            }
                            else if(response.length()==limit)
                            {
                                for (int i = 0; i < response.length(); i++)
                                {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String ProductName = productName1.getString("productName");
                                    //Log.e("Product Name:", ProductName);
                                    String articleOption = productName1.getString("articleOption");
                                    int L2Hrs_Net_Sales = productName1.getInt("last2HourSaleTotQty");
                                    int Day_Net_Sales = productName1.getInt("fordaySaleTotQty");
                                    int WTD_Net_Sales = productName1.getInt("wtdSaleTotQty");
                                    double Day_Net_Sales_Percent = productName1.getDouble("fordayPvaSalesUnitsPercent");
                                    double WTD_Net_Sales_Percent = productName1.getDouble("wtdPvaSalesUnitsPercent");
                                    int SOH = productName1.getInt("stkOnhandQty");
                                    int GIT = productName1.getInt("stkGitQty");
                                    String Storecode = productName1.getString("storeCode");
                                    String storeDesc = productName1.getString("storeDesc");

                                    ProductNameBean productNameBean = new ProductNameBean();
                                    productNameBean.setProductName(ProductName);
                                    productNameBean.setArticleOption(articleOption);
                                    productNameBean.setL2hrsNetSales(L2Hrs_Net_Sales);
                                    productNameBean.setDayNetSales(Day_Net_Sales);
                                    productNameBean.setWtdNetSales(WTD_Net_Sales);
                                    productNameBean.setDayNetSalesPercent(Day_Net_Sales_Percent);
                                    productNameBean.setWtdNetSalesPercent(WTD_Net_Sales_Percent);
                                    productNameBean.setSoh(SOH);
                                    productNameBean.setGit(GIT);
                                    productNameBean.setStoreCode(Storecode);
                                    productNameBean.setStoreDesc(storeDesc);
                                    productNameBeanArrayList.add(productNameBean);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestProductAPI(context);
                            }
                            else if(response.length()< limit)
                            {
                                for (int i = 0; i < response.length(); i++)
                                {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String ProductName = productName1.getString("productName");
                                    //Log.e("Product Name:", ProductName);
                                    String articleOption = productName1.getString("articleOption");
                                    int L2Hrs_Net_Sales = productName1.getInt("last2HourSaleTotQty");
                                    int Day_Net_Sales = productName1.getInt("fordaySaleTotQty");
                                    int WTD_Net_Sales = productName1.getInt("wtdSaleTotQty");
                                    double Day_Net_Sales_Percent = productName1.getDouble("fordayPvaSalesUnitsPercent");
                                    double WTD_Net_Sales_Percent = productName1.getDouble("wtdPvaSalesUnitsPercent");
                                    int SOH = productName1.getInt("stkOnhandQty");
                                    int GIT = productName1.getInt("stkGitQty");
                                    String Storecode = productName1.getString("storeCode");
                                    String storeDesc = productName1.getString("storeDesc");
                                    ProductNameBean productNameBean = new ProductNameBean();
                                    productNameBean.setProductName(ProductName);
                                    productNameBean.setArticleOption(articleOption);
                                    productNameBean.setL2hrsNetSales(L2Hrs_Net_Sales);
                                    productNameBean.setDayNetSales(Day_Net_Sales);
                                    productNameBean.setWtdNetSales(WTD_Net_Sales);
                                    productNameBean.setDayNetSalesPercent(Day_Net_Sales_Percent);
                                    productNameBean.setWtdNetSalesPercent(WTD_Net_Sales_Percent);
                                    productNameBean.setSoh(SOH);
                                    productNameBean.setGit(GIT);
                                    productNameBean.setStoreCode(Storecode);
                                    productNameBean.setStoreDesc(storeDesc);
                                    productNameBeanArrayList.add(productNameBean);

                                }

                                Collections.sort(productNameBeanArrayList, new Comparator<ProductNameBean>() {
                                    public int compare(ProductNameBean one, ProductNameBean other) {
                                        return  new Integer(one.getWtdNetSales()).compareTo(new Integer(other.getWtdNetSales()));
                                    }
                                });
                                Collections.reverse(productNameBeanArrayList);

                                Log.e("arrayList"," "+productNameBeanArrayList.size());





                                adapter = new SalesPvAAdapter(productNameBeanArrayList, context);
                                listViewSales.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                //((SalesPvAAdapter)((HeaderViewListAdapter)listViewSales.getAdapter()).getWrappedAdapter()).notifyDataSetChanged();
                                //setListViewHeightBasedOnChildren(listViewSales);

                                tableRelLayout.setVisibility(View.VISIBLE);
                                relChartLayout.setVisibility(View.VISIBLE);
                                Reusable_Functions.hDialog();


                            }

                        }
                        catch (Exception e) {
                            //Log.e("Exception e", e.toString() + "");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        // Toast.makeText(LoginActivity.this,"Invalid User",Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer "+bearertoken);

                Log.e("params "," "+params);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }


    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("1");
        xAxis.add("2");
        xAxis.add("3");
        xAxis.add("4");
        xAxis.add("5");
        xAxis.add("6");
        xAxis.add("7");
        xAxis.add("8");
        xAxis.add("9");
        xAxis.add("10");
        xAxis.add("11");
        xAxis.add("12");
        xAxis.add("13");
        xAxis.add("14");
        xAxis.add("15");
        xAxis.add("16");
        xAxis.add("17");
        xAxis.add("18");
        xAxis.add("19");
        xAxis.add("20");
        xAxis.add("21");
        xAxis.add("22");
        xAxis.add("23");
        xAxis.add("24");
        return xAxis;
    }
    private ArrayList<String> getXAxisValues1() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("11");
        xAxis.add("12");
        xAxis.add("13");
        xAxis.add("14");
        xAxis.add("15");
        xAxis.add("16");
        xAxis.add("17");
        xAxis.add("18");
        xAxis.add("19");
        xAxis.add("20");
        xAxis.add("21");
        xAxis.add("22");
        xAxis.add("23");
        xAxis.add("24");
        xAxis.add("25");
        xAxis.add("26");
        xAxis.add("27");
        xAxis.add("28");
        xAxis.add("29");
        xAxis.add("30");
        xAxis.add("31");
        xAxis.add("32");
        xAxis.add("33");
        xAxis.add("34");
        return xAxis;
    }

    // this method is used to create data for line graph<br />
    public LineData lineData() {
        ArrayList<Entry> line = new ArrayList();
//        for(int i=0;i<line.size();i++  ) {
        line.add(new Entry(2f, 0));
        line.add(new Entry(4f, 1));
        line.add(new Entry(3f, 2));
        line.add(new Entry(6f, 3));
        line.add(new Entry(9f, 4));
        line.add(new Entry(4f, 5));
        line.add(new Entry(5f, 6));
        line.add(new Entry(2f, 7));
        line.add(new Entry(2f, 8));
        line.add(new Entry(2f, 9));
        line.add(new Entry(2f, 10));
        line.add(new Entry(2f, 11));
        line.add(new Entry(2f, 12));
        line.add(new Entry(4f, 13));
        line.add(new Entry(3f, 14));
        line.add(new Entry(6f, 15));
        line.add(new Entry(9f, 16));
        line.add(new Entry(4f, 17));
        line.add(new Entry(5f, 18));
        line.add(new Entry(2f, 19));
        line.add(new Entry(2f, 20));
        line.add(new Entry(2f, 21));
        line.add(new Entry(2f, 22));
        line.add(new Entry(2f, 23));

        LineDataSet lineDataSet = new LineDataSet(line, "Value 3");
        lineDataSet.setDrawValues(false);
        lineDataSet.setColor(Color.MAGENTA);
        LineData lineData = new LineData(getXAxisValues(), lineDataSet);
        return lineData;
    }

    // this method is used to create data for Bar graph<br />
    public ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;
        ArrayList<BarEntry> group1 = new ArrayList();
        BarEntry v1e1 = new BarEntry(1f, 0); // Jan
        group1.add(v1e1);
        BarEntry v1e2 = new BarEntry(4f, 1); // Feb
        group1.add(v1e2);
        BarEntry v1e3 = new BarEntry(6f, 2); // Mar
        group1.add(v1e3);
        BarEntry v1e4 = new BarEntry(8f, 3); // Apr
        group1.add(v1e4);
        BarEntry v1e5 = new BarEntry(10f, 4); // May
        group1.add(v1e5);
        BarEntry v1e6 = new BarEntry(11f, 5); // Jun
        group1.add(v1e6);
        BarEntry v1e7 = new BarEntry(12f, 6); // Jan
        group1.add(v1e7);
        BarEntry v1e8 = new BarEntry(13f, 7); // Feb
        group1.add(v1e8);
        BarEntry v1e9 = new BarEntry(14f, 8); // Mar
        group1.add(v1e9);
        BarEntry v1e10 = new BarEntry(15f, 9); // Apr
        group1.add(v1e10);
        BarEntry v1e11 = new BarEntry(16f, 10); // May
        group1.add(v1e11);
        BarEntry v1e12 = new BarEntry(17f, 11); // Jun
        group1.add(v1e12);
        BarEntry v1e13 = new BarEntry(18f, 12); // Jan
        group1.add(v1e13);
        BarEntry v1e14 = new BarEntry(19f, 13); // Feb
        group1.add(v1e14);
        BarEntry v1e15 = new BarEntry(20f, 14); // Mar
        group1.add(v1e15);
        BarEntry v1e16 = new BarEntry(21f, 15); // Apr
        group1.add(v1e16);
        BarEntry v1e17 = new BarEntry(22f, 16); // May
        group1.add(v1e17);
        BarEntry v1e18 = new BarEntry(25f, 17); // Jun
        group1.add(v1e18);
        BarEntry v1e19 = new BarEntry(10f, 18); // Jan
        group1.add(v1e19);
        BarEntry v1e20 = new BarEntry(14f, 19); // Feb
        group1.add(v1e20);
        BarEntry v1e21 = new BarEntry(22f, 20); // Mar
        group1.add(v1e21);
        BarEntry v1e22 = new BarEntry(23f, 21); // Apr
        group1.add(v1e22);
        BarEntry v1e23 = new BarEntry(9f, 22); // May
        group1.add(v1e23);
        BarEntry v1e24 = new BarEntry(10f, 23); // Jun
        group1.add(v1e24);


        ArrayList<BarEntry> group2 = new ArrayList();
        BarEntry v2e1 = new BarEntry(1f, 0); // Jan
        group2.add(v2e1);
        BarEntry v2e2 = new BarEntry(4f, 1); // Feb
        group2.add(v2e2);
        BarEntry v2e3 = new BarEntry(6f, 2); // Mar
        group2.add(v2e3);
        BarEntry v2e4 = new BarEntry(8f, 3); // Apr
        group2.add(v2e4);
        BarEntry v2e5 = new BarEntry(10f, 4); // May
        group2.add(v2e5);
        BarEntry v2e6 = new BarEntry(11f, 5); // Jun
        group2.add(v2e6);
        BarEntry v2e7 = new BarEntry(12f, 6); // Jan
        group2.add(v2e7);
        BarEntry v2e8 = new BarEntry(13f, 7); // Feb
        group2.add(v2e8);
        BarEntry v2e9 = new BarEntry(14f, 8); // Mar
        group2.add(v2e9);
        BarEntry v2e10 = new BarEntry(15f, 9); // Apr
        group2.add(v2e10);
        BarEntry v2e11 = new BarEntry(16f, 10); // May
        group2.add(v2e11);
        BarEntry v2e12 = new BarEntry(17f, 11); // Jun
        group2.add(v2e12);
        BarEntry v2e13 = new BarEntry(18f, 12); // Jan
        group2.add(v2e13);
        BarEntry v2e14 = new BarEntry(19f, 13); // Feb
        group2.add(v2e14);
        BarEntry v2e15 = new BarEntry(20f, 14); // Mar
        group2.add(v2e15);
        BarEntry v2e16 = new BarEntry(21f, 15); // Apr
        group2.add(v2e16);
        BarEntry v2e17 = new BarEntry(22f, 16); // May
        group2.add(v2e17);
        BarEntry v2e18 = new BarEntry(25f, 17); // Jun
        group2.add(v2e18);
        BarEntry v2e19 = new BarEntry(10f, 18); // Jan
        group2.add(v2e19);
        BarEntry v2e20 = new BarEntry(14f, 19); // Feb
        group2.add(v2e20);
        BarEntry v2e21 = new BarEntry(22f, 20); // Mar
        group2.add(v2e21);
        BarEntry v2e22 = new BarEntry(23f, 21); // Apr
        group2.add(v2e22);
        BarEntry v2e23 = new BarEntry(9f, 22); // May
        group2.add(v2e23);
        BarEntry v2e24 = new BarEntry(10f, 23); // Jun
        group2.add(v2e24);

        BarDataSet barDataSet = new BarDataSet(group1, "Net Sales");
        barDataSet.setColor(Color.GREEN);
        barDataSet.setDrawValues(false);
        BarDataSet barDataSet1 = new BarDataSet(group2, "Plan Sales");
        barDataSet.setColor(Color.RED);

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet);
        dataSets.add(barDataSet1);
        return dataSets;
    }

}