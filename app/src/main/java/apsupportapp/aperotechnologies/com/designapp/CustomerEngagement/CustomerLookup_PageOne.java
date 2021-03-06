

package apsupportapp.aperotechnologies.com.designapp.CustomerEngagement;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.support.v4.app.Fragment;


import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.MySingleton;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;


import static android.content.Context.LAYOUT_INFLATER_SERVICE;




/**
 * Created by pamrutkar on 14/06/17.
 */
public class CustomerLookup_PageOne extends Fragment implements CompoundButton.OnCheckedChangeListener{
    private TextView txt_cust_NetSalesVal, txt_cust_PlanSalesVal, txt_cust_ActualCustVal, txt_cust_PlanCustVal, txt_cust_ActualCustName,
            txt_cust_ActualCustPerc, txt_cust_PlanCustName, txt_cust_PlanCustPerc, txt_progress_custVal, txt_progress_salesVal;
    private TextView txt_cust_pengagementType_Val, txt_cust_pCustomer_Val, txt_cust_psales_Val, txt_cust_pspc_Val, txt_cust_psalesAch_Val, txt_cust_NetSalesName,
            txt_cust_NetSalesPerc, txt_switch_name,txt_band_ach,txt_lifestage_ach;
    private ImageView txt_cust_NetSalesImage;
    private PieChart pieChart_band, pieChart_lifestage;
    private ProgressBar progressbar_customer, progressbar_sales;
    private int offsetval = 0, limit = 100, count = 0;
    Context context;
    ViewGroup root;
    CustomerLoyaltySummary customerLoyaltySummary;
    CustomerEngagementDetail customerEngagementDetail;
    CustomerDetail customerDetail;
    private ArrayList<CustomerEngagementDetail> planengagementArrayList, actualengagementArrayList;
    private ArrayList<CustomerLoyaltySummary> array_custLoyaltySummaries;
    public static ArrayList<CustomerDetail> customerDetailsList;
    private LinearLayout linearLayout,linearLayout1;
    JsonArrayRequest postRequest;
    RequestQueue queue;
    SharedPreferences sharedPreferences;
    String userId, bearertoken, geoLeveLDesc, engagementFor = "EVENT", e_bandnm,geoLevel2Code,lobId;
    Gson gson;
    String update_userId;
    private boolean checkNetworkFalse = false;
    private String recache = "true";
    OnEngagemntBandClick engagemntBandClick;
    private ArrayList<PieEntry> entries;
    private PieData pieData;
    private Switch sales_cust_switch;
    CustomerLookup_PageTwo customerLookup_pageTwo;
    private boolean bandcustToggle = false,bandClick = false;
    private String lazyScroll = "OFF";
    private int totalItemCount = 0;  // this is total item present in listview
    int firstVisibleItem = 0;
    MySingleton m_config;
    private String from, storeCode;
    RelativeLayout imgfilter;
    @SuppressLint("ValidFragment")
    public CustomerLookup_PageOne()
    {

    }
    public CustomerLookup_PageOne(String from, String storeCode, RelativeLayout imgfilter)
    {
        this.from = from;
        this.storeCode = storeCode;
        this.imgfilter = imgfilter;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (checkNetworkFalse) {
                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();

            }
            imgfilter.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        userId = sharedPreferences.getString("userId", "");
       // update_userId = userId.substring(0, userId.length() - 5);

        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLevel2Code = sharedPreferences.getString("concept","");
        lobId = sharedPreferences.getString("lobid","");
        m_config = MySingleton.getInstance(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = (ViewGroup) inflater.inflate(R.layout.fragment_custlookup_pageone, null);
        context = root.getContext();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = sharedPreferences.getString("userId", "");
        update_userId = userId.substring(0, userId.length() - 5);

        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLeveLDesc = sharedPreferences.getString("geoLeveLDesc", "");
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();

        initialise();
        if (Reusable_Functions.chkStatus(context))
        {
            Reusable_Functions.sDialog(context, "Loading...");
            offsetval = 0;
            limit = 10;
            count = 0;
            array_custLoyaltySummaries = new ArrayList<CustomerLoyaltySummary>();
            requestCustomerLoyaltySummary();
        }
        else
        {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
       return root;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        this.context = context;
        try {
            engagemntBandClick = (OnEngagemntBandClick) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onButtonPressed");
        }
    }

    private void initialise()
    {
        customerDetailsList = new ArrayList<CustomerDetail>();
        planengagementArrayList = new ArrayList<CustomerEngagementDetail>();
        actualengagementArrayList = new ArrayList<CustomerEngagementDetail>();
        txt_cust_ActualCustName = (TextView) root.findViewById(R.id.txt_cust_ActualCustName);
        txt_cust_ActualCustPerc = (TextView) root.findViewById(R.id.txt_cust_ActualCustPerc);
        txt_cust_ActualCustVal = (TextView) root.findViewById(R.id.txt_cust_ActualCustVal);
        txt_cust_NetSalesVal = (TextView) root.findViewById(R.id.txt_cust_NetSalesVal);
        txt_cust_PlanSalesVal = (TextView) root.findViewById(R.id.txt_cust_PlanSalesVal);
        txt_cust_PlanCustVal = (TextView) root.findViewById(R.id.txt_cust_PlanCustVal);
        txt_cust_PlanCustName = (TextView) root.findViewById(R.id.txt_cust_PlanCustName);
        txt_cust_PlanCustPerc = (TextView) root.findViewById(R.id.txt_cust_PlanCustPerc);
        txt_progress_custVal = (TextView) root.findViewById(R.id.txt_progress_custVal);
        txt_progress_salesVal = (TextView) root.findViewById(R.id.txt_progress_salesVal);
        txt_switch_name = (TextView) root.findViewById(R.id.txt_switch_name);
        sales_cust_switch = (Switch) root.findViewById(R.id.salescustswitch);
        sales_cust_switch.setOnCheckedChangeListener(this);
        pieChart_band = (PieChart) root.findViewById(R.id.pieChart_band);
        pieChart_lifestage = (PieChart) root.findViewById(R.id.pieChart_lifestage);
        linearLayout = (LinearLayout) root.findViewById(R.id.linear_band_legend);
        linearLayout1 = (LinearLayout)root.findViewById(R.id.linear_lifestage_legend);
        txt_band_ach = (TextView)root.findViewById(R.id.txt_band_ach);
        txt_lifestage_ach = (TextView)root.findViewById(R.id.txt_lifestage_ach);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        switch (buttonView.getId())
        {
            case R.id.salescustswitch:
                SalesCustToggle();
                break;
        }
    }

    private void requestCustomerLoyaltySummary()
    {
        String url;
        if(from != null)
        {
            url = ConstsCore.web_url + "/v1/display/customerloyaltysummary/" + userId + "?engagementFor=" + engagementFor + "&recache=" + recache+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId+""+storeCode;
        }
        else
        {
            url = ConstsCore.web_url + "/v1/display/customerloyaltysummary/" + userId + "?engagementFor=" + engagementFor + "&recache=" + recache+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
        }



        postRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    int i = 0;
                    if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();

                    } else {
                        for (i = 0; i < response.length(); i++) {
                            customerLoyaltySummary = gson.fromJson(response.get(i).toString(), CustomerLoyaltySummary.class);
                            array_custLoyaltySummaries.add(customerLoyaltySummary);
                        }
                    }
                    NumberFormat format = NumberFormat.getNumberInstance(new Locale("", "in"));
                    double netSalesVal = array_custLoyaltySummaries.get(0).getSpend() / 100000;
                    String netSalesVal1 = String.format("%.1f", netSalesVal);
                    double updated_nestSaleVal = Double.parseDouble(netSalesVal1);
                    txt_cust_NetSalesVal.setText("₹ " + format.format(updated_nestSaleVal));
//                    txt_cust_NetSalesName.setText("Sales Ach%");
//                    txt_cust_NetSalesPerc.setText("" + String.format("%.1f",array_custLoyaltySummaries.get(0).getSalesAch()) + "%");
//                    colorconditionForSales();
                    double planSalesVal = array_custLoyaltySummaries.get(0).getPlanSaleNetVal() / 100000;
                    String planSalesVal1 = String.format("%.1f", planSalesVal);
                    double updated_planSaleVal = Double.parseDouble(planSalesVal1);
                    txt_cust_PlanSalesVal.setText("₹ " + format.format(updated_planSaleVal));
                    txt_cust_ActualCustVal.setText("" + format.format(Math.round(array_custLoyaltySummaries.get(0).getCustCount())));
                    txt_cust_ActualCustName.setText("SPC");
                    txt_cust_ActualCustPerc.setText("₹ " + format.format(Math.round(array_custLoyaltySummaries.get(0).getSpc())));
                    txt_cust_PlanCustVal.setText("" + format.format(Math.round(array_custLoyaltySummaries.get(0).getPlanCustCount())));
                    txt_cust_PlanCustName.setText("SPC");
                    txt_cust_PlanCustPerc.setText("₹ " + format.format(Math.round(array_custLoyaltySummaries.get(0).getPlanSpc())));
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.sDialog(context, "Loading...");
                        offsetval = 0;
                        limit = 10;
                        count = 0;
                        planengagementArrayList = new ArrayList<CustomerEngagementDetail>();
                        requestCustomerPlanEngagement();
                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Reusable_Functions.hDialog();
                    Toast.makeText(context, "no data found" + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void colorconditionForSales()
    {
        if (array_custLoyaltySummaries.get(0).getSalesAch() < 80) {
            txt_cust_NetSalesPerc.setTextColor(Color.parseColor("#fe0000"));

        } else if (array_custLoyaltySummaries.get(0).getSalesAch() > 80 || array_custLoyaltySummaries.get(0).getSalesAch() < 90) {
            txt_cust_NetSalesPerc.setTextColor(Color.parseColor("#ff7e00"));
        } else if (array_custLoyaltySummaries.get(0).getSalesAch() > 90) {
            txt_cust_NetSalesPerc.setTextColor(Color.parseColor("#70e503"));
        }

    }

    private void requestCustomerPlanEngagement()
    {
        int level = 1;
        String url;
        if(from != null)
        {
            url = ConstsCore.web_url + "/v1/display/customerplanengagement/" + userId + "?engagementFor=" + engagementFor + "&recache=" + recache + "&level=" + level + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId+""+storeCode;//+ "&offset=" + offsetval + "&limit=" + limit;
        }
        else
        {
            url = ConstsCore.web_url + "/v1/display/customerplanengagement/" + userId + "?engagementFor=" + engagementFor + "&recache=" + recache + "&level=" + level+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;//+ "&offset=" + offsetval + "&limit=" + limit;
        }

        postRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    int i = 0;
                    if (response.equals("") || response == null || response.length() == 0 && count == 0)
                    {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();

                    }
                    for (i = 0; i < response.length(); i++)
                    {
                        customerEngagementDetail = gson.fromJson(response.get(i).toString(), CustomerEngagementDetail.class);
                        planengagementArrayList.add(customerEngagementDetail);
                    }
                    callBandSalesPieChart();

                    if (Reusable_Functions.chkStatus(context))
                    {
                        Reusable_Functions.sDialog(context, "Loading...");
                        offsetval = 0;
                        limit = 10;
                        count = 0;
                        actualengagementArrayList = new ArrayList<CustomerEngagementDetail>();
                        requestLifeStage();
                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Reusable_Functions.hDialog();
                    Toast.makeText(context, "no data found" + e.getMessage(), Toast.LENGTH_SHORT).show();

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

    private void SalesCustToggle() {
        if (!bandcustToggle)
        {
            if (sales_cust_switch.isChecked()) {
                linearLayout.removeAllViewsInLayout();
                pieChart_band.invalidate();
                pieChart_band.notifyDataSetChanged();
                linearLayout1.removeAllViewsInLayout();
                pieChart_lifestage.invalidate();
                pieChart_lifestage.notifyDataSetChanged();
                txt_switch_name.setText("Customer");
                callBandCustPieChart();
                callLSCustPieChart();

            }
            else
            {
                linearLayout.removeAllViewsInLayout();
                pieChart_band.invalidate();
                pieChart_band.notifyDataSetChanged();
                txt_switch_name.setText("Sales");
                linearLayout1.removeAllViewsInLayout();
                pieChart_lifestage.invalidate();
                pieChart_lifestage.notifyDataSetChanged();
                callBandSalesPieChart();
                callLSalesPieChart();
            }
        }
        else
        {
            bandcustToggle = false;
        }
    }


    private void requestLifeStage()
    {
        int level = 2;
        String url;
        if(from != null)
        {
            url = ConstsCore.web_url + "/v1/display/customerplanengagement/" + userId + "?engagementFor=" + engagementFor + "&recache=" + recache + "&level=" + level + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId+""+storeCode;//+ "&offset=" + offsetval + "&limit=" + limit;
        }
        else
        {
            url = ConstsCore.web_url + "/v1/display/customerplanengagement/" + userId + "?engagementFor=" + engagementFor + "&recache=" + recache + "&level=" + level+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;//+ "&offset=" + offsetval + "&limit=" + limit;
        }


        postRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    int i = 0;
                    if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                    }
                    for (i = 0; i < response.length(); i++) {
                        customerEngagementDetail = gson.fromJson(response.get(i).toString(), CustomerEngagementDetail.class);
                        actualengagementArrayList.add(customerEngagementDetail);
                    }
                    callLSalesPieChart();

                } catch (Exception e) {
                    Reusable_Functions.hDialog();
                    Toast.makeText(context, "no data found" + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    // Pie chart for Band Sales
    private void callBandSalesPieChart()
    {
        entries = new ArrayList<PieEntry>();
        for (int i = 0; i < planengagementArrayList.size(); i++) {
            entries.add(new PieEntry((float) planengagementArrayList.get(i).getSalesAch(), planengagementArrayList.get(i).getLevel()));
        }

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#20b5d3"));
        colors.add(Color.parseColor("#21d24c"));
        colors.add(Color.parseColor("#f5204c"));
        colors.add(Color.parseColor("#f89a20"));
        PieDataSet dataset = new PieDataSet(entries, "");
        dataset.setColors(colors);
        dataset.setValueLineWidth(0.5f);
        dataset.setValueTextColor(Color.BLACK);
        pieData = new PieData(dataset);
        pieChart_band.setDrawMarkers(false);
        dataset.setXValuePosition(null);
        dataset.setYValuePosition(null);
        pieChart_band.setEntryLabelColor(Color.BLACK);
        pieChart_band.setCenterText(String.format("%.1f",array_custLoyaltySummaries.get(0).getSalesAch())+"%");
        pieChart_band.setCenterTextSize(25f);
        pieChart_band.setCenterTextColor(Color.parseColor("#404040"));
        pieChart_band.setRotationEnabled(false);
        pieChart_band.setExtraOffsets(5, 10, 5, 5);
        pieChart_band.setHoleRadius(70);
        pieChart_band.setHoleColor(Color.WHITE);
        pieChart_band.setTransparentCircleRadius(0);
        pieChart_band.setData(pieData);
        pieChart_band.animateXY(4000, 4000);
        pieChart_band.setDescription(null);
        pieChart_band.setTouchEnabled(false);
        pieChart_band.invalidate();
        pieChart_band.notifyDataSetChanged();

        Legend legend = pieChart_band.getLegend();
        for (int i = 0; i < planengagementArrayList.size(); i++)
        {

            LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext()
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            if(i==0){
                ViewGroup view = (ViewGroup) layoutInflater.inflate(R.layout.activity_band_hrl_header_legend, null);
                linearLayout.addView(view);
            }
            ViewGroup view = (ViewGroup) layoutInflater.inflate(R.layout.activity_band_legend, null);
            TextView txt_legend_color = (TextView) view.findViewById(R.id.txt_legend_color);
            TextView txt_legend_name = (TextView) view.findViewById(R.id.txt_legend);
            TextView txt_legend_val = (TextView) view.findViewById(R.id.txt_legend_val);
            txt_legend_color.setBackgroundColor(colors.get(i));
            txt_legend_name.setText(planengagementArrayList.get(i).getLevel());
            txt_legend_val.setText("" + String.format("%.1f",planengagementArrayList.get(i).getSalesAch()));

            txt_legend_name.setTag(i);
            txt_legend_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    e_bandnm = planengagementArrayList.get(position).getLevel();

                    bandClick = true;
                    engagemntBandClick.communicatefrag1(e_bandnm,bandClick,context,userId,bearertoken);
                    CustomerLookupActivity.mViewPager.setCurrentItem(1);

//                            if (Reusable_Functions.chkStatus(getActivity()))
//                            {
//                                Reusable_Functions.sDialog(getActivity(), "Loading...");
//                                offsetval = 0;
//                                limit = 100;
//                                count = 0;
//                                bandClick = true;
//                                requestEngagementBandDetail();
//                            }
//                            else
//                            {
//                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
//                            }

                }
            });
            linearLayout.addView(view);


        }
        legend.setEnabled(false);
        Reusable_Functions.hDialog();
    }

    // Pie Chart for Band Customer
    private void callBandCustPieChart() {
        entries = new ArrayList<PieEntry>();
        for (int i = 0; i < planengagementArrayList.size(); i++) {
            entries.add(new PieEntry((float) planengagementArrayList.get(i).getCustAch(), planengagementArrayList.get(i).getLevel()));
        }
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#20b5d3"));
        colors.add(Color.parseColor("#21d24c"));
        colors.add(Color.parseColor("#f5204c"));
        colors.add(Color.parseColor("#f89a20"));
        PieDataSet dataset = new PieDataSet(entries, "");
        dataset.setColors(colors);
        dataset.setValueLineWidth(0.5f);
        dataset.setValueTextColor(Color.BLACK);
        pieData = new PieData(dataset);
        pieChart_band.setDrawMarkers(false);
        dataset.setXValuePosition(null);
        dataset.setYValuePosition(null);
        pieChart_band.setEntryLabelColor(Color.BLACK);
        pieChart_band.setCenterText(String.format("%.1f",array_custLoyaltySummaries.get(0).getCustAch())+"%");
        pieChart_band.setCenterTextSize(25f);
        pieChart_band.setCenterTextColor(Color.parseColor("#404040"));
        pieChart_band.setRotationEnabled(false);
        pieChart_band.setExtraOffsets(5, 10, 5, 5);
        pieChart_band.setHoleRadius(70);
        pieChart_band.setHoleColor(Color.WHITE);
        pieChart_band.setTransparentCircleRadius(0);
        pieChart_band.setData(pieData);
        pieChart_band.invalidate();
        pieChart_band.notifyDataSetChanged();
        pieChart_band.animateXY(4000, 4000);
        pieChart_band.setDescription(null);
        pieChart_band.setTouchEnabled(false);

        Legend legend = pieChart_band.getLegend();
        linearLayout.removeAllViewsInLayout();
        for (int i = 0; i < planengagementArrayList.size(); i++)
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext()
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            if(i==0)
            {
                ViewGroup view = (ViewGroup) layoutInflater.inflate(R.layout.activity_band_hrl_header_legend, null);
                linearLayout.addView(view);
            }
            ViewGroup view = (ViewGroup) layoutInflater.inflate(R.layout.activity_band_cust_legend, null);
            TextView txt_legend_color = (TextView) view.findViewById(R.id.txt_legend_color);
            TextView txt_legend_name = (TextView) view.findViewById(R.id.txt_legend);
            TextView txt_legend_val = (TextView) view.findViewById(R.id.txt_legend_val);
            txt_legend_color.setBackgroundColor(colors.get(i));
            txt_legend_name.setText(planengagementArrayList.get(i).getLevel());
            txt_legend_val.setText("" + String.format("%.1f", planengagementArrayList.get(i).getCustAch()));
            txt_legend_name.setTag(i);
            txt_legend_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();


                    e_bandnm = planengagementArrayList.get(position).getLevel();

                    bandClick = true;
                    engagemntBandClick.communicatefrag1(e_bandnm,bandClick,context,userId,bearertoken);
                    CustomerLookupActivity.mViewPager.setCurrentItem(1);

//                    if (Reusable_Functions.chkStatus(getActivity())) {
//                        Reusable_Functions.sDialog(getActivity(), "Loading...");
//                        offsetval = 0;
//                        limit = 100;
//                        count = 0;
//                        bandClick = true;
//                        requestEngagementBandDetail();
//                    }
//                    else
//                    {
//                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
//                    }

                }
            });
            linearLayout.addView(view);
        }
        legend.setEnabled(false);
        Reusable_Functions.hDialog();
    }

    //Pie Chart for LifeStage Sales
    private void callLSalesPieChart() {
        entries = new ArrayList<PieEntry>();
        for (int i = 0; i < actualengagementArrayList.size(); i++) {
            entries.add(new PieEntry((float) actualengagementArrayList.get(i).getSalesAch(), actualengagementArrayList.get(i).getLevel()));
        }

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#20b5d3"));
        colors.add(Color.parseColor("#21d24c"));
        colors.add(Color.parseColor("#f5204c"));
        colors.add(Color.parseColor("#f89a20"));
        colors.add(Color.parseColor("#78bc2c"));
        PieDataSet dataset = new PieDataSet(entries, "");
        dataset.setColors(colors);
        dataset.setValueLineWidth(0.5f);
        dataset.setValueTextColor(Color.BLACK);
        pieData = new PieData(dataset);
        pieChart_lifestage.setDrawMarkers(false);
        dataset.setXValuePosition(null);
        dataset.setYValuePosition(null);
        pieChart_lifestage.setEntryLabelColor(Color.BLACK);
        pieChart_lifestage.setCenterText(String.format("%.1f",array_custLoyaltySummaries.get(0).getSalesAch())+"%");
        pieChart_lifestage.setCenterTextSize(25f);
        pieChart_lifestage.setCenterTextColor(Color.parseColor("#404040"));
        pieChart_lifestage.setRotationEnabled(false);
        pieChart_lifestage.setExtraOffsets(5, 10, 5, 5);
        pieChart_lifestage.setHoleRadius(70);
        pieChart_lifestage.setHoleColor(Color.WHITE);
        pieChart_lifestage.setTransparentCircleRadius(0);
        pieChart_lifestage.setData(pieData);
        pieChart_lifestage.animateXY(4000, 4000);
        pieChart_lifestage.setDescription(null);
        pieChart_lifestage.setTouchEnabled(false);

        Legend legend = pieChart_lifestage.getLegend();
        for (int i = 0; i < actualengagementArrayList.size(); i++) {


            LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext()
                    .getSystemService(LAYOUT_INFLATER_SERVICE);

            if(i==0){
                ViewGroup view = (ViewGroup) layoutInflater.inflate(R.layout.activity_band_hrl_header_legend, null);
                linearLayout1.addView(view);
            }
            ViewGroup view = (ViewGroup) layoutInflater.inflate(R.layout.activity_band_legend, null);
            TextView txt_legend_color = (TextView) view.findViewById(R.id.txt_legend_color);
            TextView txt_legend_name = (TextView) view.findViewById(R.id.txt_legend);
            TextView txt_legend_val = (TextView) view.findViewById(R.id.txt_legend_val);
            txt_legend_color.setBackgroundColor(colors.get(i));
            txt_legend_name.setText(actualengagementArrayList.get(i).getLevel());
            txt_legend_val.setText("" + String.format("%.1f",actualengagementArrayList.get(i).getSalesAch()));
            txt_legend_name.setTag(i);
            txt_legend_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();


                    e_bandnm = actualengagementArrayList.get(position).getLevel();

                    bandClick = false;
                    engagemntBandClick.communicatefrag1(e_bandnm,bandClick,context,userId,bearertoken);
                    CustomerLookupActivity.mViewPager.setCurrentItem(1);


//                    if (Reusable_Functions.chkStatus(getActivity())) {
//                        Reusable_Functions.sDialog(getActivity(), "Loading...");
//                        offsetval = 0;
//                        limit = 100;
//                        count = 0;
//                        bandClick = false;
//                        requestEngagementBandDetail();
//                    }
//                    else
//                    {
//                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
//                    }

                }
            });
            linearLayout1.addView(view);
        }
        legend.setEnabled(false);
        Reusable_Functions.hDialog();

    }

    //Pie Chart for LifeStage Customer
    private void callLSCustPieChart() {
        entries = new ArrayList<PieEntry>();
        for (int i = 0; i < actualengagementArrayList.size(); i++)
        {
            entries.add(new PieEntry((float) actualengagementArrayList.get(i).getCustAch(), actualengagementArrayList.get(i).getLevel()));
        }
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#20b5d3"));
        colors.add(Color.parseColor("#21d24c"));
        colors.add(Color.parseColor("#f5204c"));
        colors.add(Color.parseColor("#f89a20"));
        colors.add(Color.parseColor("#78bc2c"));
        PieDataSet dataset = new PieDataSet(entries, "");
        dataset.setColors(colors);
        dataset.setValueLineWidth(0.5f);
        dataset.setValueTextColor(Color.BLACK);
        pieData = new PieData(dataset);
        pieChart_lifestage.setDrawMarkers(false);
        dataset.setXValuePosition(null);
        dataset.setYValuePosition(null);
        pieChart_lifestage.setRotationEnabled(false);
        pieChart_lifestage.setCenterText(String.format("%.1f",array_custLoyaltySummaries.get(0).getCustAch())+"%");
        pieChart_lifestage.setCenterTextSize(25f);
        pieChart_lifestage.setCenterTextColor(Color.parseColor("#404040"));
        pieChart_lifestage.setEntryLabelColor(Color.BLACK);
        pieChart_lifestage.setExtraOffsets(5, 10, 5, 5);
        pieChart_lifestage.setHoleRadius(70);
        pieChart_lifestage.setHoleColor(Color.WHITE);
        pieChart_lifestage.setTransparentCircleRadius(0);
        pieChart_lifestage.setData(pieData);
        pieChart_lifestage.invalidate();
        pieChart_lifestage.notifyDataSetChanged();
        pieChart_lifestage.animateXY(4000, 4000);
        pieChart_lifestage.setDescription(null);
        pieChart_lifestage.setTouchEnabled(false);
        txt_lifestage_ach.setVisibility(View.VISIBLE);

        Legend legend = pieChart_lifestage.getLegend();
        linearLayout1.removeAllViewsInLayout();
        for (int i = 0; i < actualengagementArrayList.size(); i++)
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext()
                    .getSystemService(LAYOUT_INFLATER_SERVICE);

            if(i==0){
                ViewGroup view = (ViewGroup) layoutInflater.inflate(R.layout.activity_band_hrl_header_legend, null);
                linearLayout1.addView(view);
            }
            ViewGroup view = (ViewGroup) layoutInflater.inflate(R.layout.activity_band_cust_legend, null);
            TextView txt_legend_color = (TextView) view.findViewById(R.id.txt_legend_color);
            TextView txt_legend_name = (TextView) view.findViewById(R.id.txt_legend);
            TextView txt_legend_val = (TextView) view.findViewById(R.id.txt_legend_val);
            txt_legend_color.setBackgroundColor(colors.get(i));
            txt_legend_name.setText(actualengagementArrayList.get(i).getLevel());
            txt_legend_val.setText("" + String.format("%.1f", actualengagementArrayList.get(i).getCustAch()));
            txt_legend_name.setTag(i);
            txt_legend_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();


                    e_bandnm = actualengagementArrayList.get(position).getLevel();

                    bandClick = false;
                    engagemntBandClick.communicatefrag1(e_bandnm,bandClick,context,userId,bearertoken);
                    CustomerLookupActivity.mViewPager.setCurrentItem(1);


//                    if (Reusable_Functions.chkStatus(getActivity())) {
//                        Reusable_Functions.sDialog(getActivity(), "Loading...");
//                        offsetval = 0;
//                        limit = 100;
//                        count = 0;
//                        bandClick = false;
//                        requestEngagementBandDetail();
//                    }
//                    else
//                    {
//                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
//                    }

                }
            });
            linearLayout1.addView(view);
        }
        legend.setEnabled(false);
    }


    private void requestEngagementBandDetail()
    {
        String url = "";
        if(bandClick)
        {
            url = ConstsCore.web_url + "/v1/display/customerdetails/" + userId + "?engagementFor=" + engagementFor + "&engagementBrand=" + e_bandnm.replace(" ", "%20") + "&recache=" + recache + "&offset=" + offsetval + "&limit=" + limit+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;

        }
        else
        {
            url = ConstsCore.web_url + "/v1/display/customerdetails/" + userId + "?engagementFor=" + engagementFor + "&lifeStage=" + e_bandnm.replace(" ", "%20") + "&recache=" + recache + "&offset=" + offsetval + "&limit=" + limit+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;

        }
        postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                return;

                            } else

                            if (response.length() == limit)
                            {

                                for (int i = 0; i < response.length(); i++) {
                                   // customerDetailsList.clear();
                                    customerDetail = gson.fromJson(response.get(i).toString(), CustomerDetail.class);
                                    customerDetailsList.add(customerDetail);


                                }


                                CustomerLookupActivity.mViewPager.setCurrentItem(1);

                               Reusable_Functions.hDialog();
                            }
                    }
                        catch (Exception e)
                        {
                            Reusable_Functions.hDialog();

                            Toast.makeText(context, "data failed...." + e.toString(), Toast.LENGTH_SHORT).show();
                            Reusable_Functions.hDialog();
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
                        Toast.makeText(context, "server not responding..", Toast.LENGTH_SHORT).show();
                        Reusable_Functions.hDialog();
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

}
