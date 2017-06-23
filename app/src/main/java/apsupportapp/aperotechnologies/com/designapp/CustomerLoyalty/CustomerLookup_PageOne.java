

package apsupportapp.aperotechnologies.com.designapp.CustomerLoyalty;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;


import static android.content.Context.LAYOUT_INFLATER_SERVICE;


/**
 * Created by pamrutkar on 14/06/17.
 */
public class CustomerLookup_PageOne extends Fragment  {
    private TextView txt_cust_NetSalesVal, txt_cust_PlanSalesVal, txt_cust_ActualCustVal, txt_cust_PlanCustVal, txt_cust_ActualCustName,
            txt_cust_ActualCustPerc, txt_cust_PlanCustName, txt_cust_PlanCustPerc, txt_progress_custVal, txt_progress_salesVal;
    private TextView txt_cust_pengagementType_Val, txt_cust_pCustomer_Val, txt_cust_psales_Val, txt_cust_pspc_Val, txt_cust_psalesAch_Val;

    private ProgressBar progressbar_customer, progressbar_sales;
    private int offsetval = 0, limit = 100, count = 0;
    Context context;
    ViewGroup root;
    CustomerLoyaltySummary customerLoyaltySummary;
    CustomerEngagementDetail customerEngagementDetail;
    private ArrayList<CustomerEngagementDetail> planengagementArrayList,actualengagementArrayList;
    private ArrayList<CustomerLoyaltySummary> array_custLoyaltySummaries;
    JsonArrayRequest postRequest;
    RequestQueue queue;
    SharedPreferences sharedPreferences;
    String userId, bearertoken, geoLeveLDesc, engagementFor = "EVENT";
    Gson gson;
    private LayoutInflater layoutInflater;
    ScrollView scroll_custOne;
    String update_userId;
    private boolean checkNetworkFalse=false;
    private String recache = "true";
    OnEngagemntBandClick engagemntBandClick;
    String engagemnt_band;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
        {
            if(checkNetworkFalse)
            {
                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.fragment_custlookup_pageone, null);
        context = root.getContext();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = sharedPreferences.getString("userId", "");
        update_userId = userId.substring(0,userId.length()-5);
        Log.e("update_userId",""+update_userId);
        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLeveLDesc = sharedPreferences.getString("geoLeveLDesc", "");
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();

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
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        try {
            engagemntBandClick = (OnEngagemntBandClick) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getContext().toString() + " must implement onButtonPressed");
        }
    }

    private void initialise()
    {
        planengagementArrayList = new ArrayList<CustomerEngagementDetail>();
        actualengagementArrayList = new ArrayList<CustomerEngagementDetail>();
        scroll_custOne = (ScrollView)root.findViewById(R.id.scrollv_custOne);
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
        progressbar_customer = (ProgressBar) root.findViewById(R.id.circularProgressbar_customer);
        progressbar_sales = (ProgressBar) root.findViewById(R.id.circularProgressbar_sales);
    }

    private void requestCustomerLoyaltySummary()
    {
       String url = ConstsCore.web_url + "/v1/display/customerloyaltysummary/" + update_userId + "?engagementFor=" + engagementFor +"&recache="+ recache;
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

                    } else
                    {
                        for (i = 0; i < response.length(); i++) {
                            customerLoyaltySummary = gson.fromJson(response.get(i).toString(), CustomerLoyaltySummary.class);
                            array_custLoyaltySummaries.add(customerLoyaltySummary);
                        }
                    }
                    NumberFormat format = NumberFormat.getNumberInstance(new Locale("", "in"));
                    double netSalesVal = array_custLoyaltySummaries.get(0).getSpend()/100000;
                    txt_cust_NetSalesVal.setText("₹ " + format.format(Math.round(netSalesVal))+"\tLac");
                    double planSalesVal = array_custLoyaltySummaries.get(0).getPlanSaleNetVal()/100000;
                    txt_cust_PlanSalesVal.setText("₹ " + format.format(Math.round(planSalesVal))+"\tLac");
                    txt_cust_ActualCustVal.setText("" + format.format(Math.round(array_custLoyaltySummaries.get(0).getCustCount())));
                    txt_cust_ActualCustName.setText("SPC");
                    txt_cust_ActualCustPerc.setText("₹ " + format.format(Math.round(array_custLoyaltySummaries.get(0).getSpc())));
                    txt_cust_PlanCustVal.setText("" + format.format(Math.round(array_custLoyaltySummaries.get(0).getPlanCustCount())));
                    txt_cust_PlanCustName.setText("SPC");
                    txt_cust_PlanCustPerc.setText("₹ " + format.format(Math.round(array_custLoyaltySummaries.get(0).getPlanSpc())));
                    int custAch = (int) array_custLoyaltySummaries.get(0).getCustAch();
                    progressbar_customer.setProgress(custAch);
                    txt_progress_custVal.setText("" + Math.round(array_custLoyaltySummaries.get(0).getCustAch()) + "%");
                    int salesAch = (int) array_custLoyaltySummaries.get(0).getSalesAch();
                    progressbar_sales.setProgress(salesAch);
                    txt_progress_salesVal.setText("" + Math.round(array_custLoyaltySummaries.get(0).getSalesAch()) + "%");
                   // Reusable_Functions.hDialog();
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

    private void requestCustomerPlanEngagement()
    {
        String url = ConstsCore.web_url + "/v1/display/customerplanengagement/" + update_userId + "?engagementFor=" + engagementFor +"&recache="+recache;//+ "&offset=" + offsetval + "&limit=" + limit;

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
                   // }
                    addPlanEngagementDatatoTable(i);
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.sDialog(context, "Loading...");
                        offsetval = 0;
                        limit = 10;
                        count = 0;
                        actualengagementArrayList = new ArrayList<CustomerEngagementDetail>();
                        requestCustomerActualEngagement();
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


    private void requestCustomerActualEngagement()
    {
        String url = ConstsCore.web_url + "/v1/display/customeractualengagement/" + update_userId + "?engagementFor=" + engagementFor +"&recache="+recache;//+ "&offset=" + offsetval + "&limit=" + limit;
        Log.e("cust summary url 1", "" + url);
        postRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                Log.e("response 1 :", "" + response);
                try {
                    int i = 0;
                    if (response.equals("") || response == null || response.length() == 0 && count == 0)
                    {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                    }
                    //  else if (response.length() == limit) {
                    for (i = 0; i < response.length(); i++)
                    {
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
                    addActualEngagementDatatoTable(i);

                } catch (Exception e)
                {
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

    private void addActualEngagementDatatoTable(int position)
    {
        LinearLayout linearLayout = (LinearLayout) root.findViewById(R.id.linear_actual_enagement_table);
        for (int i = 0; i < actualengagementArrayList.size(); i++) {
            NumberFormat format = NumberFormat.getNumberInstance(new Locale("", "in"));
            layoutInflater = (LayoutInflater) context.getApplicationContext()
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup view = (ViewGroup) layoutInflater.inflate(R.layout.activity_cust_actaul_engagement_band, null);
            txt_cust_pengagementType_Val = (TextView) view.findViewById(R.id.txt_cust_aengagementType_Val);
            txt_cust_pCustomer_Val = (TextView) view.findViewById(R.id.txt_cust_aCustomer_Val);
            txt_cust_psales_Val = (TextView) view.findViewById(R.id.txt_cust_asales_Val);
            txt_cust_pspc_Val = (TextView) view.findViewById(R.id.txt_cust_aspc_Val);

            txt_cust_pengagementType_Val.setText(actualengagementArrayList.get(i).getEngagementBand());
            txt_cust_pCustomer_Val.setText("" + format.format(Math.round(actualengagementArrayList.get(i).getCustCount())));
            double plan_spendVal = actualengagementArrayList.get(i).getSpend()/100000;
            txt_cust_psales_Val.setText("₹ " + format.format(Math.round(plan_spendVal)));
            txt_cust_pspc_Val.setText("" + format.format(Math.round(actualengagementArrayList.get(i).getSpc())));
            txt_cust_pengagementType_Val.setTag(position);
            txt_cust_pCustomer_Val.setTag(position);
            txt_cust_psales_Val.setTag(position);
            txt_cust_pspc_Val.setTag(position);
//            final int  k = i;
//            txt_cust_pengagementType_Val.setOnClickListener(new ViewGroup.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    engagemnt_band = actualengagementArrayList.get(k).getEngagementBand().toString();
//                    Log.e("band name :",""+engagemnt_band);
//                    engagemntBandClick.communicatefrag1(engagemnt_band);
//                    CustomerLookupActivity.mViewPager.setCurrentItem(1);
//
//                }
//            });
//            txt_cust_pCustomer_Val.setOnClickListener(new ViewGroup.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    engagemnt_band = actualengagementArrayList.get(k).getEngagementBand().toString();
//                    Log.e("band name :",""+engagemnt_band);
//                    engagemntBandClick.communicatefrag1(engagemnt_band);
//                    CustomerLookupActivity.mViewPager.setCurrentItem(1);
//
//                }
//            });
//            txt_cust_psales_Val.setOnClickListener(new ViewGroup.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    engagemnt_band = actualengagementArrayList.get(k).getEngagementBand().toString();
//                    Log.e("band name :",""+engagemnt_band);
//                    engagemntBandClick.communicatefrag1(engagemnt_band);
//                    CustomerLookupActivity.mViewPager.setCurrentItem(1);
//
//                }
//            });
//            txt_cust_pspc_Val.setOnClickListener(new ViewGroup.OnClickListener() {
//                @Override
//                public void onClick(View v)
//                {
//                    engagemnt_band = actualengagementArrayList.get(k).getEngagementBand().toString();
//                    Log.e("band name :",""+engagemnt_band);
//                    engagemntBandClick.communicatefrag1(engagemnt_band);
//                    CustomerLookupActivity.mViewPager.setCurrentItem(1);
//                }
//            });
            Reusable_Functions.hDialog();
            linearLayout.addView(view);
        }
    }

    private void addPlanEngagementDatatoTable(int position)
    {
        LinearLayout linearLayout = (LinearLayout) root.findViewById(R.id.linear_plan_enagement_table);
        for ( int i = 0; i < planengagementArrayList.size(); i++) {
            NumberFormat format = NumberFormat.getNumberInstance(new Locale("", "in"));
            layoutInflater = (LayoutInflater) getActivity().getApplicationContext()
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup view = (ViewGroup) layoutInflater.inflate(R.layout.activity_cust_engagement_band, null);
            txt_cust_pengagementType_Val = (TextView) view.findViewById(R.id.txt_cust_pengagementType_Val);
            txt_cust_pCustomer_Val = (TextView) view.findViewById(R.id.txt_cust_pCustomer_Val);
            txt_cust_psales_Val = (TextView) view.findViewById(R.id.txt_cust_psales_Val);
            txt_cust_pspc_Val = (TextView) view.findViewById(R.id.txt_cust_pspc_Val);
            txt_cust_psalesAch_Val = (TextView) view.findViewById(R.id.txt_cust_psalesAch_Val);
            txt_cust_pengagementType_Val.setText("");
            txt_cust_pengagementType_Val.setText(planengagementArrayList.get(i).getLevel());
            txt_cust_pCustomer_Val.setText("" + format.format(Math.round(planengagementArrayList.get(i).getPlanCust())));
            double salesVal = planengagementArrayList.get(i).getPlanSpend()/100000;
            txt_cust_psales_Val.setText("₹ " + format.format(Math.round(salesVal)));
            txt_cust_pspc_Val.setText("₹ " + format.format(Math.round(planengagementArrayList.get(i).getPlanSpc())));
            txt_cust_psalesAch_Val.setText(""+format.format(Math.round(planengagementArrayList.get(i).getSalesAch())));
            txt_cust_pengagementType_Val.setTag(i);
            txt_cust_pCustomer_Val.setTag(i);
            txt_cust_psales_Val.setTag(i);
            txt_cust_pspc_Val.setTag(i);
            final int k = i;
//            Log.e("k :",""+k);
//            txt_cust_pengagementType_Val.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Log.e("onClick: ",""+txt_cust_pengagementType_Val.getTag()+planengagementArrayList.size());
//                    engagemnt_band = planengagementArrayList.get(k).getEngagementBand().toString();
//                    Log.e("band name :",""+engagemnt_band);
//                    engagemntBandClick.communicatefrag1(engagemnt_band);
//                    CustomerLookupActivity.mViewPager.setCurrentItem(1);
//
//                }
//            });
//            txt_cust_pCustomer_Val.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    engagemnt_band = planengagementArrayList.get(k).getEngagementBand().toString();
//                    Log.e("band name :",""+engagemnt_band);
//                    engagemntBandClick.communicatefrag1(engagemnt_band);
//                    CustomerLookupActivity.mViewPager.setCurrentItem(1);
//
//                }
//            });
//            txt_cust_psales_Val.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    engagemnt_band = planengagementArrayList.get(k).getEngagementBand().toString();
//                    Log.e("band name :",""+engagemnt_band);
//                    engagemntBandClick.communicatefrag1(engagemnt_band);
//                    CustomerLookupActivity.mViewPager.setCurrentItem(1);
//
//                }
//            });
//            txt_cust_pspc_Val.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    engagemnt_band = planengagementArrayList.get(k).getEngagementBand().toString();
//                    Log.e("band name :",""+engagemnt_band);
//                    engagemntBandClick.communicatefrag1(engagemnt_band);
//                    CustomerLookupActivity.mViewPager.setCurrentItem(1);
//
//                }
//            });
            Reusable_Functions.hDialog();
            linearLayout.addView(view);
        }
    }
}
