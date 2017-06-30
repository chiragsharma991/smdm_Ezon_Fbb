

package apsupportapp.aperotechnologies.com.designapp.CustomerLoyalty;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.OptionEfficiency.OptionEfficiencyActivity;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.model.OptionEfficiencyHeader;


import static android.content.Context.LAYOUT_INFLATER_SERVICE;


/**
 * Created by pamrutkar on 14/06/17.
 */
public class CustomerLookup_PageOne extends Fragment implements CompoundButton.OnCheckedChangeListener,OnChartValueSelectedListener{
    private TextView txt_cust_NetSalesVal, txt_cust_PlanSalesVal, txt_cust_ActualCustVal, txt_cust_PlanCustVal, txt_cust_ActualCustName,
            txt_cust_ActualCustPerc, txt_cust_PlanCustName, txt_cust_PlanCustPerc, txt_progress_custVal, txt_progress_salesVal;
    private TextView txt_cust_pengagementType_Val, txt_cust_pCustomer_Val, txt_cust_psales_Val, txt_cust_pspc_Val, txt_cust_psalesAch_Val, txt_cust_NetSalesName,
            txt_cust_NetSalesPerc, txt_switch_name,txt_ls_switch_name;
    private ImageView txt_cust_NetSalesImage;
    private PieChart pieChart_band,pieChart_lifestage;
    private ProgressBar progressbar_customer, progressbar_sales;
    private int offsetval = 0, limit = 100, count = 0;
    Context context;
    ViewGroup root;
    CustomerLoyaltySummary customerLoyaltySummary;
    CustomerEngagementDetail customerEngagementDetail;
    CustomerDetail customerDetail;
    private ArrayList<CustomerEngagementDetail> planengagementArrayList, actualengagementArrayList;
    private ArrayList<CustomerLoyaltySummary> array_custLoyaltySummaries;
    static ArrayList<CustomerDetail> customerDetailsList;
    JsonArrayRequest postRequest;
    RequestQueue queue;
    SharedPreferences sharedPreferences;
    String userId, bearertoken, geoLeveLDesc, engagementFor = "EVENT",e_bandnm;
    Gson gson;
    static boolean valSelectedFlag = false;
    String update_userId;
    private boolean checkNetworkFalse = false;
    private String recache = "true";
    OnEngagemntBandClick engagemntBandClick;
    private ArrayList<PieEntry> entries;
    private PieData pieData;
    private Switch sales_cust_switch,sales_cust_switch_lifestage;
    private boolean bandcustToggle = false,lifestageToggle = false;


    public CustomerLookup_PageOne()
    {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (checkNetworkFalse) {
                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = (ViewGroup) inflater.inflate(R.layout.fragment_custlookup_pageone, null);
        context = root.getContext();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = sharedPreferences.getString("userId", "");
        update_userId = userId.substring(0, userId.length() - 5);
        Log.e("update_userId", "" + update_userId);
        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLeveLDesc = sharedPreferences.getString("geoLeveLDesc", "");
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();
        Log.e("test", "onCreateView: page one" );


        initialise();
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.sDialog(context, "Loading...");

            offsetval = 0;
            limit = 10;
            count = 0;
            array_custLoyaltySummaries = new ArrayList<CustomerLoyaltySummary>();
            requestCustomerLoyaltySummary();
        } else {
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
        txt_ls_switch_name = (TextView)root.findViewById(R.id.txt_ls_switch_name);
        sales_cust_switch_lifestage = (Switch)root.findViewById(R.id.salescustswitch_lifestage); 
        sales_cust_switch = (Switch) root.findViewById(R.id.salescustswitch);
        sales_cust_switch.setOnCheckedChangeListener(this);
        sales_cust_switch_lifestage.setOnCheckedChangeListener(this);

//        progressbar_customer = (ProgressBar) root.findViewById(R.id.circularProgressbar_customer);
//        progressbar_sales = (ProgressBar) root.findViewById(R.id.circularProgressbar_sales);
//        txt_cust_NetSalesImage = (ImageView)root.findViewById(R.id.txt_cust_NetSalesImage);
        txt_cust_NetSalesName = (TextView) root.findViewById(R.id.txt_cust_NetSalesName);
        txt_cust_NetSalesPerc = (TextView) root.findViewById(R.id.txt_cust_NetSalesPerc);
        pieChart_band = (PieChart) root.findViewById(R.id.pieChart_band);
        pieChart_lifestage = (PieChart)root.findViewById(R.id.pieChart_lifestage);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        switch (buttonView.getId()) {
            case R.id.salescustswitch:
                SalesCustToggle();
                break;
            case R.id.salescustswitch_lifestage:
                ls_SalesCustToggle();
                break;

        }
    }

   
    private void requestCustomerLoyaltySummary()
    {
        String url = ConstsCore.web_url + "/v1/display/customerloyaltysummary/" + update_userId + "?engagementFor=" + engagementFor + "&recache=" + recache;
        Log.e("cust summary url ", "" + url);
        postRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("response :", "" + response);
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
                    String netSalesVal1 =  String.format("%.1f",netSalesVal);
                    double updated_nestSaleVal = Double.parseDouble(netSalesVal1);
                    txt_cust_NetSalesVal.setText("₹ " +format.format(updated_nestSaleVal));
                    txt_cust_NetSalesName.setText("Sales");
                    txt_cust_NetSalesPerc.setText("" + Math.round(array_custLoyaltySummaries.get(0).getSalesAch()) + "%");
                    colorconditionForSales();
                    double planSalesVal = array_custLoyaltySummaries.get(0).getPlanSaleNetVal() / 100000;
                    String planSalesVal1 = String.format("%.1f",planSalesVal);
                    double updated_planSaleVal = Double.parseDouble(planSalesVal1);
                    txt_cust_PlanSalesVal.setText("₹ " + format.format(updated_planSaleVal));
                    txt_cust_ActualCustVal.setText("" + format.format(Math.round(array_custLoyaltySummaries.get(0).getCustCount())));
                    txt_cust_ActualCustName.setText("SPC");
                    txt_cust_ActualCustPerc.setText("₹ " + format.format(Math.round(array_custLoyaltySummaries.get(0).getSpc())));
                    txt_cust_PlanCustVal.setText("" + format.format(Math.round(array_custLoyaltySummaries.get(0).getPlanCustCount())));
                    txt_cust_PlanCustName.setText("SPC");
                    txt_cust_PlanCustPerc.setText("₹ " + format.format(Math.round(array_custLoyaltySummaries.get(0).getPlanSpc())));
//                    int custAch = (int) array_custLoyaltySummaries.get(0).getCustAch();
//                    progressbar_customer.setProgress(custAch);
//                    txt_progress_custVal.setText("" + Math.round(array_custLoyaltySummaries.get(0).getCustAch()) + "%");
//                    int salesAch = (int) array_custLoyaltySummaries.get(0).getSalesAch();
//                    progressbar_sales.setProgress(salesAch);
//                    txt_progress_salesVal.setText("" + Math.round(array_custLoyaltySummaries.get(0).getSalesAch()) + "%");

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
            txt_cust_NetSalesName.setTextColor(Color.parseColor("#fe0000"));
            txt_cust_NetSalesPerc.setTextColor(Color.parseColor("#fe0000"));

        } else if (array_custLoyaltySummaries.get(0).getSalesAch() > 80 || array_custLoyaltySummaries.get(0).getSalesAch() < 90) 
        {
            txt_cust_NetSalesName.setTextColor(Color.parseColor("#ff7e00"));
            txt_cust_NetSalesPerc.setTextColor(Color.parseColor("#ff7e00"));
        }
        else if (array_custLoyaltySummaries.get(0).getSalesAch() > 90) 
        {
            txt_cust_NetSalesName.setTextColor(Color.parseColor("#70e503"));
            txt_cust_NetSalesPerc.setTextColor(Color.parseColor("#70e503"));
        }

    }

    private void requestCustomerPlanEngagement() 
    {
        int level = 1;
        String url = ConstsCore.web_url + "/v1/display/customerplanengagement/" + update_userId + "?engagementFor=" + engagementFor + "&recache=" + recache + "&level=" + level;//+ "&offset=" + offsetval + "&limit=" + limit;

        Log.e("cust summary url ", "" + url);
        postRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("response :", "" + response);
                try {
                    int i = 0;
                    if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();

                    }
//                    else if (response.length() == limit) {
//                        for (i = 0; i < response.length(); i++) {
//                            customerEngagementDetail = gson.fromJson(response.get(i).toString(), CustomerEngagementDetail.class);
//                            engagementDetailArrayList.add(customerEngagementDetail);
//                        }
//
//                        offsetval = (limit * count) + limit;
//                        count++;
//                        requestCustomerPlanEngagement();
                    // }
//                else if (response.length() < limit) {
                    for (i = 0; i < response.length(); i++) {
                        customerEngagementDetail = gson.fromJson(response.get(i).toString(), CustomerEngagementDetail.class);
                        planengagementArrayList.add(customerEngagementDetail);
                    }
                    callBandSalesPieChart();
                    // }
                    // addPlanEngagementDatatoTable(i);
                    if (Reusable_Functions.chkStatus(context)) {
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

    private void SalesCustToggle()
    {
        if (!bandcustToggle)
        {
            if (sales_cust_switch.isChecked())
            {
                pieChart_band.invalidate();
                pieChart_band.notifyDataSetChanged();
                txt_switch_name.setText("Customer");
                callBandCustPieChart();

            } else {
                pieChart_band.invalidate();
                pieChart_band.notifyDataSetChanged();
                txt_switch_name.setText("Sales");
                callBandSalesPieChart();
            }
        } else {
            bandcustToggle = false;
        }
    }


    private void ls_SalesCustToggle()
    {
        if (!lifestageToggle)
        {
            if (sales_cust_switch_lifestage.isChecked())
            {
                pieChart_lifestage.invalidate();
                pieChart_lifestage.notifyDataSetChanged();
                txt_ls_switch_name.setText("Customer");
                callLSCustPieChart();

            }
            else
            {
                pieChart_lifestage.invalidate();
                pieChart_lifestage.notifyDataSetChanged();
                txt_ls_switch_name.setText("Sales");
                callLSalesPieChart();
            }
        } else {
            lifestageToggle = false;
        }
    }


    private void requestLifeStage()
    {
        int level = 2;
        String url = ConstsCore.web_url + "/v1/display/customerplanengagement/" + update_userId + "?engagementFor=" + engagementFor + "&recache=" + recache + "&level=" + level;//+ "&offset=" + offsetval + "&limit=" + limit;

        Log.e("cust summary url 1", "" + url);
        postRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("response 1 :", "" + response);
                try {
                    int i = 0;
                    if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                    }
                    //  else if (response.length() == limit) {
                    for (i = 0; i < response.length(); i++) {
                        customerEngagementDetail = gson.fromJson(response.get(i).toString(), CustomerEngagementDetail.class);
                        actualengagementArrayList.add(customerEngagementDetail);
                    }

//                        offsetval = (limit * count) + limit;
//                        count++;
//                        requestCustomerPlanEngagement();
//                    } else if (response.length() < limit) {
//                        for (i = 0; i < response.length(); i++) {
//                            customerEngagementDetail = gson.fromJson(response.get(i).toString(), CustomerEngagementDetail.class);
//                            engagementDetailArrayList.add(customerEngagementDetail);
//                        }
//                    }
//                    addActualEngagementDatatoTable(i);
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
        for (int i = 0; i < planengagementArrayList.size(); i++)
        {
            entries.add(new PieEntry((float) planengagementArrayList.get(i).getSalesAch(), planengagementArrayList.get(i).getLevel()));
        }

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#ffcb00"));
        colors.add(Color.parseColor("#66ff66"));
        colors.add(Color.parseColor("#66ffff"));
        colors.add(Color.parseColor("#6666ff"));
        PieDataSet dataset = new PieDataSet(entries, "");
//        for (int i = 0 ;i<planengagementArrayList.size();i++)
//        {
//            dataset.setColors(getRandomColor());
//        }
        dataset.setColors(colors);
        dataset.setValueLineWidth(0.5f);
        dataset.setValueTextColor(Color.BLACK);
        pieData = new PieData(dataset);
        dataset.setValueLinePart1Length(0.8f);
        dataset.setValueLinePart2Length(0.5f);
        pieChart_band.setDrawMarkers(false);
        pieData.setValueTextSize(10f);
        dataset.setXValuePosition(null);
        dataset.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieChart_band.setEntryLabelColor(Color.BLACK);
        pieChart_band.setExtraOffsets(5, 10, 5, 5);
        pieChart_band.setHoleRadius(40);
        pieChart_band.setHoleColor(Color.WHITE);
        pieChart_band.setTransparentCircleRadius(0);
        pieChart_band.setData(pieData);
        pieChart_band.animateXY(4000, 4000);
        pieChart_band.setDescription(null);
        pieChart_band.setTouchEnabled(true);
        pieChart_band.invalidate();
      //  pieChart_band.setOnChartValueSelectedListener(this);
        Legend l = pieChart_band.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setWordWrapEnabled(true);
        l.setEnabled(true);
        l.setFormSize(11f);
        Reusable_Functions.hDialog();
    }

    // Pie Chart for Band Customer
    private void callBandCustPieChart()
    {
        entries = new ArrayList<PieEntry>();
        for (int i = 0; i < planengagementArrayList.size(); i++)
        {
            entries.add(new PieEntry((float)planengagementArrayList.get(i).getCustAch() , planengagementArrayList.get(i).getLevel()));
        }
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#ffcb00"));
        colors.add(Color.parseColor("#66ff66"));
        colors.add(Color.parseColor("#66ffff"));
        colors.add(Color.parseColor("#6666ff"));
        PieDataSet dataset = new PieDataSet(entries, "");
//        for (int i = 0; i < planengagementArrayList.size();i++)
//        {
            dataset.setColors(colors);

//        }
        dataset.setValueLineWidth(0.5f);
        dataset.setValueTextColor(Color.BLACK);
        pieData = new PieData(dataset);
        dataset.setValueLinePart1Length(0.8f);
        dataset.setValueLinePart2Length(0.5f);
        pieChart_band.setDrawMarkers(false);
        pieData.setValueTextSize(10f);
        dataset.setXValuePosition(null);
        dataset.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieChart_band.setEntryLabelColor(Color.BLACK);
        pieChart_band.setExtraOffsets(5, 10, 5, 5);
        pieChart_band.setHoleRadius(40);
        pieChart_band.setHoleColor(Color.WHITE);
        pieChart_band.setTransparentCircleRadius(0);
        pieChart_band.setData(pieData);
        pieChart_band.notifyDataSetChanged();
        pieChart_band.invalidate();
        pieChart_band.animateXY(4000, 4000);
        pieChart_band.setDescription(null);
        pieChart_band.setTouchEnabled(true);
        Legend l = pieChart_band.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setWordWrapEnabled(true);
        l.setEnabled(true);
        l.setFormSize(11f);
    }

    //Pie Chart for LifeStage Sales
    private void callLSalesPieChart()
    {
        entries = new ArrayList<PieEntry>();
        for (int i = 0; i < actualengagementArrayList.size(); i++)
        {
            entries.add(new PieEntry((float) actualengagementArrayList.get(i).getSalesAch(), actualengagementArrayList.get(i).getLevel()));
        }

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#ffcb00"));
        colors.add(Color.parseColor("#66ff66"));
        colors.add(Color.parseColor("#66ffff"));
        colors.add(Color.parseColor("#6666ff"));
        colors.add(Color.parseColor("#8000ff"));
        PieDataSet dataset = new PieDataSet(entries, "");
//        for (int i = 0 ; i < actualengagementArrayList.size();i++)
//        {
            dataset.setColors(colors);
//        }
        dataset.setValueLineWidth(0.5f);
        dataset.setValueTextColor(Color.BLACK);
        pieData = new PieData(dataset);
        dataset.setValueLinePart1Length(0.8f);
        dataset.setValueLinePart2Length(0.5f);
        pieChart_lifestage.setDrawMarkers(false);
        pieData.setValueTextSize(10f);
        dataset.setXValuePosition(null);
        dataset.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieChart_lifestage.setEntryLabelColor(Color.BLACK);
        pieChart_lifestage.setExtraOffsets(5, 10, 5, 5);
        pieChart_lifestage.setHoleRadius(40);
        pieChart_lifestage.setHoleColor(Color.WHITE);
        pieChart_lifestage.setTransparentCircleRadius(0);
        pieChart_lifestage.setData(pieData);
        pieChart_lifestage.animateXY(4000, 4000);
        pieChart_lifestage.setDescription(null);
        pieChart_lifestage.setTouchEnabled(true);
        Legend l = pieChart_lifestage.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setWordWrapEnabled(true);
        l.setEnabled(true);
        l.setFormSize(11f);
        Reusable_Functions.hDialog();

    }

    //Pie Chart for LifeStage Customer
    private void callLSCustPieChart()
    {
        entries = new ArrayList<PieEntry>();
        for (int i = 0; i < actualengagementArrayList.size(); i++)
        {
            entries.add(new PieEntry((float)actualengagementArrayList.get(i).getCustAch() , actualengagementArrayList.get(i).getLevel()));
        }

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#ffcb00"));
        colors.add(Color.parseColor("#66ff66"));
        colors.add(Color.parseColor("#66ffff"));
        colors.add(Color.parseColor("#6666ff"));
        colors.add(Color.parseColor("#8000ff"));
        PieDataSet dataset = new PieDataSet(entries, "");
//        for (int i = 0; i < actualengagementArrayList.size();i++)
//        {
            dataset.setColors(colors);

//        }
        dataset.setValueLineWidth(0.5f);
        dataset.setValueTextColor(Color.BLACK);
        pieData = new PieData(dataset);
        dataset.setValueLinePart1Length(0.8f);
        dataset.setValueLinePart2Length(0.5f);
        pieChart_lifestage.setDrawMarkers(false);
        pieData.setValueTextSize(10f);
        dataset.setXValuePosition(null);
        dataset.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieChart_lifestage.setEntryLabelColor(Color.BLACK);
        pieChart_lifestage.setExtraOffsets(5, 10, 5, 5);
        pieChart_lifestage.setHoleRadius(40);
        pieChart_lifestage.setHoleColor(Color.WHITE);
        pieChart_lifestage.setTransparentCircleRadius(0);
        pieChart_lifestage.setData(pieData);
        pieChart_lifestage.notifyDataSetChanged();
        pieChart_lifestage.invalidate();
        pieChart_lifestage.animateXY(4000, 4000);
        pieChart_lifestage.setDescription(null);
        pieChart_lifestage.setTouchEnabled(true);
        Legend l = pieChart_lifestage.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setWordWrapEnabled(true);
        l.setEnabled(true);
        l.setFormSize(11f);
    }

    public int getRandomColor()
    {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }


    @Override
    public void onValueSelected(Entry e, Highlight h)
    {

        PieEntry pe = (PieEntry)e;
        Log.e("-----",""+  pe.getLabel());
         e_bandnm = pe.getLabel();

        //  engagemntBandClick.communicatefrag1(e_bandnm,customerDetailsList);
               if (Reusable_Functions.chkStatus(getActivity())) {
            Reusable_Functions.sDialog(getActivity(), "Loading...");
        offsetval = 0;
        limit = 10;
        count = 0;
        requestEngagementBandDetail();
           }
        else
        {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
//
    }

    @Override
    public void onNothingSelected()
    {

    }

    private void requestEngagementBandDetail()
    {
        String url = ConstsCore.web_url + "/v1/display/customerdetails/" + update_userId + "?engagementFor=" + engagementFor +"&engagementBrand="+ e_bandnm.replace(" ","%20") + "&recache=" + recache;//+ "&offset=" + offset + "&limit=" + limit;
        Log.e("detail url 1:",""+url);
        postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("response 1:",""+response);
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                return;

                            } else
//                            if (response.length() == limit) {
//
//                                for (int i = 0; i < response.length(); i++) {
//
//                                    customerDetail = gson.fromJson(response.get(i).toString(), CustomerDetail.class);
//                                    detailArrayList.add(customerDetail);
//                                }
//                                offset = (limit * count) + limit;
//                                count++;
//
//                                requestCustomerDetail();
//
//                            } else if (response.length() < limit) {

                                for (int i = 0; i < response.length(); i++) {
                                    customerDetail = gson.fromJson(response.get(i).toString(), CustomerDetail.class);
                                    customerDetailsList.add(customerDetail);
                                }
                            // }

                            CustomerLookup_PageOne.valSelectedFlag = false;
                            engagemntBandClick.communicatefrag1(e_bandnm,customerDetailsList);
                            CustomerLookupActivity.mViewPager.setCurrentItem(1);
                            Reusable_Functions.hDialog();
                        }
                        catch (Exception e)
                        {
                            Reusable_Functions.hDialog();
                            Log.e("exception :",""+e.getMessage());
                            Toast.makeText(context, "data failed...." + e.toString(), Toast.LENGTH_SHORT).show();
                            Reusable_Functions.hDialog();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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



//    private void addActualEngagementDatatoTable(int position)
//    {
//        LinearLayout linearLayout = (LinearLayout) root.findViewById(R.id.linear_actual_enagement_table);
//        for (int i = 0; i < actualengagementArrayList.size(); i++) {
//            NumberFormat format = NumberFormat.getNumberInstance(new Locale("", "in"));
//            layoutInflater = (LayoutInflater) context.getApplicationContext()
//                    .getSystemService(LAYOUT_INFLATER_SERVICE);
//            ViewGroup view = (ViewGroup) layoutInflater.inflate(R.layout.activity_cust_actaul_engagement_band, null);
//            txt_cust_pengagementType_Val = (TextView) view.findViewById(R.id.txt_cust_aengagementType_Val);
//            txt_cust_pCustomer_Val = (TextView) view.findViewById(R.id.txt_cust_aCustomer_Val);
//            txt_cust_psales_Val = (TextView) view.findViewById(R.id.txt_cust_asales_Val);
//            txt_cust_pspc_Val = (TextView) view.findViewById(R.id.txt_cust_aspc_Val);
//
//            txt_cust_pengagementType_Val.setText(actualengagementArrayList.get(i).getEngagementBand());
//            txt_cust_pCustomer_Val.setText("" + format.format(Math.round(actualengagementArrayList.get(i).getCustCount())));
//            double plan_spendVal = actualengagementArrayList.get(i).getSpend()/100000;
//            txt_cust_psales_Val.setText("₹ " + format.format(Math.round(plan_spendVal)));
//            txt_cust_pspc_Val.setText("" + format.format(Math.round(actualengagementArrayList.get(i).getSpc())));
//            txt_cust_pengagementType_Val.setTag(position);
//            txt_cust_pCustomer_Val.setTag(position);
//            txt_cust_psales_Val.setTag(position);
//            txt_cust_pspc_Val.setTag(position);
////            final int  k = i;
////            txt_cust_pengagementType_Val.setOnClickListener(new ViewGroup.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    engagemnt_band = actualengagementArrayList.get(k).getEngagementBand().toString();
////                    Log.e("band name :",""+engagemnt_band);
////                    engagemntBandClick.communicatefrag1(engagemnt_band);
////                    CustomerLookupActivity.mViewPager.setCurrentItem(1);
////
////                }
////            });
////            txt_cust_pCustomer_Val.setOnClickListener(new ViewGroup.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    engagemnt_band = actualengagementArrayList.get(k).getEngagementBand().toString();
////                    Log.e("band name :",""+engagemnt_band);
////                    engagemntBandClick.communicatefrag1(engagemnt_band);
////                    CustomerLookupActivity.mViewPager.setCurrentItem(1);
////
////                }
////            });
////            txt_cust_psales_Val.setOnClickListener(new ViewGroup.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    engagemnt_band = actualengagementArrayList.get(k).getEngagementBand().toString();
////                    Log.e("band name :",""+engagemnt_band);
////                    engagemntBandClick.communicatefrag1(engagemnt_band);
////                    CustomerLookupActivity.mViewPager.setCurrentItem(1);
////
////                }
////            });
////            txt_cust_pspc_Val.setOnClickListener(new ViewGroup.OnClickListener() {
////                @Override
////                public void onClick(View v)
////                {
////                    engagemnt_band = actualengagementArrayList.get(k).getEngagementBand().toString();
////                    Log.e("band name :",""+engagemnt_band);
////                    engagemntBandClick.communicatefrag1(engagemnt_band);
////                    CustomerLookupActivity.mViewPager.setCurrentItem(1);
////                }
////            });
//            Reusable_Functions.hDialog();
//            linearLayout.addView(view);
//        }
//    }

//    private void addPlanEngagementDatatoTable(int position)
//    {
//        LinearLayout linearLayout = (LinearLayout) root.findViewById(R.id.linear_plan_enagement_table);
//        for ( int i = 0; i < planengagementArrayList.size(); i++) {
//            NumberFormat format = NumberFormat.getNumberInstance(new Locale("", "in"));
//            layoutInflater = (LayoutInflater) getActivity().getApplicationContext()
//                    .getSystemService(LAYOUT_INFLATER_SERVICE);
//            ViewGroup view = (ViewGroup) layoutInflater.inflate(R.layout.activity_cust_engagement_band, null);
//            txt_cust_pengagementType_Val = (TextView) view.findViewById(R.id.txt_cust_pengagementType_Val);
//            txt_cust_pCustomer_Val = (TextView) view.findViewById(R.id.txt_cust_pCustomer_Val);
//            txt_cust_psales_Val = (TextView) view.findViewById(R.id.txt_cust_psales_Val);
//            txt_cust_pspc_Val = (TextView) view.findViewById(R.id.txt_cust_pspc_Val);
//            txt_cust_psalesAch_Val = (TextView) view.findViewById(R.id.txt_cust_psalesAch_Val);
//            txt_cust_pengagementType_Val.setText("");
//            txt_cust_pengagementType_Val.setText(planengagementArrayList.get(i).getLevel());
//            txt_cust_pCustomer_Val.setText("" + format.format(Math.round(planengagementArrayList.get(i).getPlanCust())));
//            double salesVal = planengagementArrayList.get(i).getPlanSpend()/100000;
//            txt_cust_psales_Val.setText("₹ " + format.format(Math.round(salesVal)));
//            txt_cust_pspc_Val.setText("₹ " + format.format(Math.round(planengagementArrayList.get(i).getPlanSpc())));
//            txt_cust_psalesAch_Val.setText(""+format.format(Math.round(planengagementArrayList.get(i).getSalesAch())));
//            txt_cust_pengagementType_Val.setTag(i);
//            txt_cust_pCustomer_Val.setTag(i);
//            txt_cust_psales_Val.setTag(i);
//            txt_cust_pspc_Val.setTag(i);
//            final int k = i;
//            Log.e("k :",""+k);
////            txt_cust_pengagementType_Val.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////
////                    Log.e("onClick: ",""+txt_cust_pengagementType_Val.getTag()+planengagementArrayList.size());
////                    engagemnt_band = planengagementArrayList.get(k).getLevel().toString();
////                    Log.e("band name :",""+engagemnt_band);
////                    engagemntBandClick.communicatefrag1(engagemnt_band);
////                    CustomerLookupActivity.mViewPager.setCurrentItem(1);
////
////                }
////            });
////            txt_cust_pCustomer_Val.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    engagemnt_band = planengagementArrayList.get(k).getLevel().toString();
////                    Log.e("band name :",""+engagemnt_band);
////                    engagemntBandClick.communicatefrag1(engagemnt_band);
////                    CustomerLookupActivity.mViewPager.setCurrentItem(1);
////
////                }
////            });
////            txt_cust_psales_Val.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    engagemnt_band = planengagementArrayList.get(k).getLevel().toString();
////                    Log.e("band name :",""+engagemnt_band);
////                    engagemntBandClick.communicatefrag1(engagemnt_band);
////                    CustomerLookupActivity.mViewPager.setCurrentItem(1);
////
////                }
////            });
////            txt_cust_pspc_Val.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    engagemnt_band = planengagementArrayList.get(k).getLevel().toString();
////                    Log.e("band name :",""+engagemnt_band);
////                    engagemntBandClick.communicatefrag1(engagemnt_band);
////                    CustomerLookupActivity.mViewPager.setCurrentItem(1);
////
////                }
////            });
//            Reusable_Functions.hDialog();
//            linearLayout.addView(view);
//        }
//    }
}
