package apsupportapp.aperotechnologies.com.designapp.SalesAnalysis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.DashBoardActivity;
import apsupportapp.aperotechnologies.com.designapp.ProductNameBean;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by hasai on 19/09/16.
 */
public class SalesAnalysisActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    SegmentedGroup segmentedGroupSales;
    LinearLayout llayoutSalesAnalysis;
    ViewPager vwpagersales;
    LinearLayout lldots;
    RelativeLayout relLayoutSales;
    ListView listView_SalesAnalysis;
    SalesAnalysisAdapter salesadapter;
    Context context;
    SalesPagerAdapter pageradapter;
    SharedPreferences sharedPreferences;
    String userId,bearertoken;
    ArrayList<ProductNameBean> arrayList;
    RelativeLayout btnBack;
    public static String selectedsegValue = "WTD";
    String prodName = "KNIT CHURIDAR";

    int offsetvalue=0,limit=100;
    int count=0;
    RequestQueue queue;
    int focusposition = 0;


    LinearLayout rankLayout;
    RelativeLayout relimgrank, relimgfilter;
    RelativeLayout relprevbtn, relnextbtn, relimgclose;
    TextView txtheaderplanclass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesanalysis);
        getSupportActionBar().hide();
        context = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = sharedPreferences.getString("userId","");
        bearertoken = sharedPreferences.getString("bearerToken","");

        btnBack = (RelativeLayout) findViewById(R.id.imageBtnBack);
        segmentedGroupSales= (SegmentedGroup) findViewById(R.id.segmentedGrp);
        segmentedGroupSales.setOnCheckedChangeListener(this);

        RadioButton btnWTD = (RadioButton) findViewById(R.id.btnWTD);
        btnWTD.toggle();

        llayoutSalesAnalysis = (LinearLayout) findViewById(R.id.llayoutSalesAnalysis);
        llayoutSalesAnalysis.setVisibility(View.GONE);

        rankLayout = (LinearLayout) findViewById(R.id.rankLayout);
        relimgfilter = (RelativeLayout) findViewById(R.id.imgfilter);
        relimgrank = (RelativeLayout) findViewById(R.id.imgrank);
        relprevbtn = (RelativeLayout) findViewById(R.id.prevplanclass);
        relnextbtn = (RelativeLayout) findViewById(R.id.nextplanclass);
        txtheaderplanclass = (TextView) findViewById(R.id.headerplanclass);
        relimgclose = (RelativeLayout) findViewById(R.id.relimgclose);

        RelativeLayout relpopuplayout = (RelativeLayout) findViewById(R.id.relpopuplayout);
        relpopuplayout.setOnClickListener(null);

        RelativeLayout relrankLayout = (RelativeLayout) findViewById(R.id.relrankLayout);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
            public void onClick(View v)
            {
                if(rankLayout.getVisibility() == View.VISIBLE)
                {
                    rankLayout.setVisibility(View.GONE);
                }
                else if(rankLayout.getVisibility() == View.GONE)
                {
                    rankLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        relimgfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        relprevbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                switch (txtheaderplanclass.getText().toString()) {

                    case "Brand Class":
                        relnextbtn.setVisibility(View.VISIBLE);
                        txtheaderplanclass.setText("Brand");
                        SalesPagerAdapter.currentPage = 0;
                        if (lldots != null) {
                            lldots.removeAllViews();
                        }


                        prodName = "GRAPHIC TEES";
                        arrayList = new ArrayList<ProductNameBean>();
                        llayoutSalesAnalysis.setVisibility(View.GONE);
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
                        txtheaderplanclass.setText("Plan Class");
                        SalesPagerAdapter.currentPage = 0;
                        if (lldots != null) {
                            lldots.removeAllViews();
                        }
                        prodName = "KNIT CHURIDAR";
                        arrayList = new ArrayList<ProductNameBean>();
                        llayoutSalesAnalysis.setVisibility(View.GONE);

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
                        txtheaderplanclass.setText("Category");
                        SalesPagerAdapter.currentPage = 0;
                        if (lldots != null) {
                            lldots.removeAllViews();
                        }
                        prodName = "NEON POLO";
                        arrayList = new ArrayList<ProductNameBean>();
                        llayoutSalesAnalysis.setVisibility(View.GONE);
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
                        txtheaderplanclass.setText("Department");
                        relprevbtn.setVisibility(View.GONE);
                        relnextbtn.setVisibility(View.VISIBLE);
                        SalesPagerAdapter.currentPage = 0;
                        if (lldots != null) {
                            lldots.removeAllViews();
                        }
                        prodName = "CORE DENIM";
                        arrayList = new ArrayList<ProductNameBean>();
                        llayoutSalesAnalysis.setVisibility(View.GONE);
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

        relnextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (txtheaderplanclass.getText().toString()) {

                    case "Department":
                        relprevbtn.setVisibility(View.VISIBLE);
                        txtheaderplanclass.setText("Category");
                        SalesPagerAdapter.currentPage = 0;
                        if (lldots != null) {
                            lldots.removeAllViews();
                        }


                        prodName = "NEON POLO";
                        arrayList = new ArrayList<ProductNameBean>();
                        llayoutSalesAnalysis.setVisibility(View.GONE);
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
                        txtheaderplanclass.setText("Plan Class");
                        SalesPagerAdapter.currentPage = 0;
                        if (lldots != null) {
                            lldots.removeAllViews();
                        }
                        prodName = "KNIT CHURIDAR";
                        arrayList = new ArrayList<ProductNameBean>();
                        llayoutSalesAnalysis.setVisibility(View.GONE);

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
                        txtheaderplanclass.setText("Brand");
                        SalesPagerAdapter.currentPage = 0;
                        if (lldots != null) {
                            lldots.removeAllViews();
                        }
                        prodName = "GRAPHIC TEES";
                        arrayList = new ArrayList<ProductNameBean>();
                        llayoutSalesAnalysis.setVisibility(View.GONE);
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
                        txtheaderplanclass.setText("Brand Class");
                        relprevbtn.setVisibility(View.VISIBLE);
                        relnextbtn.setVisibility(View.GONE);
                        SalesPagerAdapter.currentPage = 0;
                        if (lldots != null) {
                            lldots.removeAllViews();
                        }
                        prodName = "DJ&C SHIRTS";
                        arrayList = new ArrayList<ProductNameBean>();
                        llayoutSalesAnalysis.setVisibility(View.GONE);
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

//        RelativeLayout rel = (RelativeLayout) findViewById(R.id.rel);
//        TextView txtPlan = (TextView) findViewById(R.id.txtPlan);
//        TextView txtAchieve = (TextView) findViewById(R.id.txtAchieve);

//        rel.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//        Log.e("--- ", " "+rel.getMeasuredWidth() + " "+ (200/100));
//
//        int singlePercVal = 200/100;//rel.getMeasuredWidth()/100;
//
//        int planVal = 100;//30
//        int achieveVal = 50;
//
//        int calplanVal = planVal * singlePercVal;
//        int calachieveVal = achieveVal * singlePercVal;
//
//        txtPlan.setWidth(calplanVal);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(2,18);
//        params.setMargins(calachieveVal,0,0,0);
//        txtAchieve.setLayoutParams(params);
//
//        if(planVal < achieveVal)
//        {
//            txtPlan.setBackgroundColor(Color.RED);
//        }
//        else
//        {
//            txtPlan.setBackgroundColor(Color.GREEN);
//        }


        //initialiseUIElements();

        focusposition = 0;
        arrayList = new ArrayList<ProductNameBean>();
        vwpagersales = (ViewPager) findViewById(R.id.viewpager);
        vwpagersales.setCurrentItem(0);
        lldots = (LinearLayout) findViewById(R.id.lldots);
        lldots.setOrientation(LinearLayout.HORIZONTAL);
        relLayoutSales = (RelativeLayout) findViewById(R.id.relTablelayout);
        //edt = (EditText) findViewById(R.id.edtFirstPosition);
        listView_SalesAnalysis = (ListView) findViewById(R.id.listView_SalesAnalysis);
        listView_SalesAnalysis.addFooterView(getLayoutInflater().inflate(R.layout.list_footer, null));


        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();


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

        listView_SalesAnalysis.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
                if(arrayList.size() != 0)
                {

                    if(view.getFirstVisiblePosition() <= arrayList.size() - 1) {
                        //listView_SalesAnalysis.smoothScrollToPosition(firstVisibleItem);
                        focusposition = view.getFirstVisiblePosition();
                        listView_SalesAnalysis.setSelection(view.getFirstVisiblePosition());
                        Log.e("firstVisibleItem", " " + view.getFirstVisiblePosition() + " " + arrayList.get(view.getFirstVisiblePosition()).getProductName());
                        pageradapter = new SalesPagerAdapter(context, arrayList, focusposition, vwpagersales, lldots, salesadapter, listView_SalesAnalysis);
                        vwpagersales.setAdapter(pageradapter);
                        pageradapter.notifyDataSetChanged();
                        vwpagersales.setCurrentItem(SalesPagerAdapter.currentPage);

                    }else
                    {
                        //listView_SalesAnalysis.setSelection(arrayList.size() - 1);
                        focusposition = arrayList.size() - 1;
                        listView_SalesAnalysis.setSelection(arrayList.size() - 1);
                        pageradapter = new SalesPagerAdapter(context, arrayList, focusposition, vwpagersales, lldots, salesadapter, listView_SalesAnalysis);
                        vwpagersales.setAdapter(pageradapter);
                        pageradapter.notifyDataSetChanged();
                        vwpagersales.setCurrentItem(SalesPagerAdapter.currentPage);

                    }



                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

//                if(arrayList.size() != 0)
//                {
//
//                    if(firstVisibleItem <= arrayList.size() - 1) {
//                        //listView_SalesAnalysis.smoothScrollToPosition(firstVisibleItem);
//                        focusposition = firstVisibleItem;
//                        Log.e("firstVisibleItem", " " + firstVisibleItem + " " + arrayList.get(firstVisibleItem).getProductName());
//                        pageradapter = new SalesPagerAdapter(context, arrayList, focusposition, vwpagersales, lldots);
//                        vwpagersales.setAdapter(pageradapter);
//                        pageradapter.notifyDataSetChanged();
//                        vwpagersales.setCurrentItem(SalesPagerAdapter.currentPage);
//
//                    }else
//                    {
//                        listView_SalesAnalysis.setSelection(arrayList.size() - 1);
//
//                    }

//                }

            }
        });






    }

//    private void initialiseUIElements(Context context)
//    {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.activity_salesanalysis,null);
//
//        focusposition = 0;
//        arrayList = new ArrayList<ProductNameBean>();
////        llayoutSalesAnalysis = (LinearLayout) view.findViewById(R.id.llayoutSalesAnalysis);
////        vwpagersales = (ViewPager) view.findViewById(R.id.viewpager);
////        vwpagersales.setCurrentItem(0);
////        relLayoutSales = (RelativeLayout) view.findViewById(R.id.relTablelayout);
////        edt = (EditText) view.findViewById(R.id.edtFirstPosition);
////        listView_SalesAnalysis = (ListView) view.findViewById(R.id.listView_SalesAnalysis);
//
//        view.addView(llayoutSalesAnalysis);
//        llayoutSalesAnalysis.addView(vwpagersales);
//        llayoutSalesAnalysis.addView(relLayoutSales);
//        relLayoutSales.addView(edt);
//        relLayoutSales.addView(listView_SalesAnalysis);
//
//
//        Log.e("--- "," "+llayoutSalesAnalysis.getChildAt(0)+ " "+ llayoutSalesAnalysis.getChildAt(1));
//
//    }




    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {

            case R.id.btnWTD:
                if(selectedsegValue.equals("WTD"))
                    break;

                selectedsegValue = "WTD";
                SalesPagerAdapter.currentPage = 0;
                if(lldots != null)
                {
                    lldots.removeAllViews();
                }

                prodName = "KNIT CHURIDAR";
                arrayList = new ArrayList<ProductNameBean>();
                llayoutSalesAnalysis.setVisibility(View.GONE);
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
                Log.e("---1---"," ");

                break;

            case R.id.btnLW:
                if(selectedsegValue.equals("LW"))
                    break;
                selectedsegValue = "LW";
                SalesPagerAdapter.currentPage = 0;
                if(lldots != null)
                {
                    lldots.removeAllViews();
                }
                prodName = "DEAL JEANS";
                arrayList = new ArrayList<ProductNameBean>();
                llayoutSalesAnalysis.setVisibility(View.GONE);
//                relLayoutSales.removeView(edt);
//                relLayoutSales.removeView(listView_SalesAnalysis);
//                llayoutSalesAnalysis.removeView(vwpagersales);
//                llayoutSalesAnalysis.removeView(relLayoutSales);
//                ((ViewGroup)llayoutSalesAnalysis.getParent()).removeView(llayoutSalesAnalysis);
//                initialiseUIElements(context);

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
                Log.e("---2---"," ");

                break;

            case R.id.btnL4W:
                if(selectedsegValue.equals("L4W"))
                    break;
                selectedsegValue = "L4W";
                SalesPagerAdapter.currentPage = 0;
                if(lldots != null)
                {
                    lldots.removeAllViews();
                }
                prodName = "DJ&C CR CHINOS";
                arrayList = new ArrayList<ProductNameBean>();
                llayoutSalesAnalysis.setVisibility(View.GONE);

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
                Log.e("---3---"," ");

                break;

            case R.id.btnYTD:
                if(selectedsegValue.equals("YTD"))
                    break;
                selectedsegValue = "YTD";
                SalesPagerAdapter.currentPage = 0;
                if(lldots != null)
                {
                    lldots.removeAllViews();
                }
                prodName = "BUFFALO SHIRTS";
                arrayList = new ArrayList<ProductNameBean>();
                llayoutSalesAnalysis.setVisibility(View.GONE);
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
                Log.e("---4---"," ");

                break;
            default:

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
                                Toast.makeText(SalesAnalysisActivity.this.context, "no product data found", Toast.LENGTH_LONG).show();
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
                                    arrayList.add(productNameBean);
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
                                    arrayList.add(productNameBean);

                                }

                                Collections.sort(arrayList, new Comparator<ProductNameBean>() {
                                    public int compare(ProductNameBean one, ProductNameBean other) {
                                        return  new Integer(one.getWtdNetSales()).compareTo(new Integer(other.getWtdNetSales()));
                                    }
                                });
                                Collections.reverse(arrayList);

                                Log.e("arrayList"," "+arrayList.size());

                                for (int i = 0; i < 3; i++) {

                                    ImageView imgdot = new ImageView(context);
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
                                    layoutParams.setMargins(3, 3, 3, 3);
                                    imgdot.setLayoutParams(layoutParams);
                                    imgdot.setImageResource(R.mipmap.dots_unselected);
                                    lldots.addView(imgdot);
                                    ImageView img = (ImageView)  lldots.getChildAt(0);
                                    img.setImageResource(R.mipmap.dots_selected);


                                }

                                pageradapter = new SalesPagerAdapter(context, arrayList, focusposition, vwpagersales, lldots, salesadapter, listView_SalesAnalysis);
                                vwpagersales.setAdapter(pageradapter);
                                pageradapter.notifyDataSetChanged();


                                salesadapter = new SalesAnalysisAdapter(arrayList, context, "PVA Sales");
                                listView_SalesAnalysis.setAdapter(salesadapter);
                                salesadapter.notifyDataSetChanged();


//                                pageradapter = new SalesPagerAdapter(context, arrayList, focusposition, vwpagersales, lldots);
//                                vwpagersales.setAdapter(pageradapter);
//                                pageradapter.notifyDataSetChanged();

//                                salesadapter = new SalesAnalysisAdapter(arrayList, context);
//                                listView_SalesAnalysis.setAdapter(salesadapter);
//                                salesadapter.notifyDataSetChanged();

                                llayoutSalesAnalysis.setVisibility(View.VISIBLE);
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


    @Override
    public void onBackPressed() {

        if(rankLayout.getVisibility() == View.VISIBLE)
        {
            rankLayout.setVisibility(View.GONE);
        }
        else
        {
            SalesPagerAdapter.currentPage = 0;
            Intent i = new Intent(SalesAnalysisActivity.this, DashBoardActivity.class);
            startActivity(i);
            finish();
        }

    }



}
