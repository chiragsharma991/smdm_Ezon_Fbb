package apsupportapp.aperotechnologies.com.designapp.CustomerEngagement;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import apsupportapp.aperotechnologies.com.designapp.MySingleton;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.RecyclerItemClickListener;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.RunningPromo.RecyclerViewPositionHelper;

import static apsupportapp.aperotechnologies.com.designapp.CustomerEngagement.CustomerDetailAdapter.ProgressViewHolder.cust_progressBar;
import static apsupportapp.aperotechnologies.com.designapp.CustomerEngagement.CustomerLookup_PageOne.customerDetailsList;


/**
 * Created by pamrutkar on 14/06/17.
 */
public class CustomerLookup_PageTwo extends Fragment
{
    public static String e_bandnm;
    OnEngagemntBandClick onEngagemntBandClick;
    static public TextView txt_engagemnt_nm;
    static EditText edt_cust_Search;
    static public RecyclerView lv_cust_details;
    static public Button btn_reset;
    CustomerDetail customerDetail;
    static public CustomerDetailAdapter customerDetailAdapter;
    ArrayList<CustomerDetail> detailArrayList, customerDetailArrayList = new ArrayList<CustomerDetail>();
    static public LinearLayout linear_engagement_type_nm;
    ViewGroup root;
    Context context;
    SharedPreferences sharedPreferences;
    String userId, bearertoken, geoLeveLDesc, engagementFor = "EVENT";
    RequestQueue queue;
    Gson gson;
    private boolean checkNetworkFalse = false;
    JsonArrayRequest postRequest;
    private String recache = "true";
    public static String lazyScroll = "OFF";
    private int totalItemCount = 0;  // this is total item present in listview
    int firstVisibleItem = 0;
    MySingleton m_config;
    String updated_userId;
    public static boolean band_Click;
    public static boolean flag = false ;
    int offset = 0, count = 0, limit = 100;
    private int arr_count = 0;
    public int pos;

   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.fragment_custlookup_pagetwo, container, false);
        context = getContext();
        m_config = MySingleton.getInstance(getActivity());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        userId = sharedPreferences.getString("userId", "");
        updated_userId = userId.substring(0, userId.length() - 5);
        Log.e("update_userId", "" + updated_userId);
        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLeveLDesc = sharedPreferences.getString("geoLeveLDesc", "");
        arr_count = 0;
        Cache cache = new DiskBasedCache(getActivity().getApplicationContext().getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();
        initialise();

        if (Reusable_Functions.chkStatus(context))
        {
            Reusable_Functions.sDialog(context, "Loading...");
            offset = 0;
            limit = 100;
            count = 0;
            flag = false;
            pos = 0;
            customerDetailsList = new ArrayList<CustomerDetail>();
            linear_engagement_type_nm.setVisibility(View.GONE);
            requestCustomerDetail();
        }
        else
        {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
        Log.e("test", "onCreateView: page two");


        edt_cust_Search.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            Boolean handled = false;

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_NEXT) || (actionId == EditorInfo.IME_ACTION_NONE)) {
                    edt_cust_Search.clearFocus();
                    InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputManager != null) {
                        inputManager.hideSoftInputFromWindow(edt_cust_Search.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    handled = true;
                }
                return handled;
            }

        });

        edt_cust_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String searchData = edt_cust_Search.getText().toString();
                customerDetailAdapter.getFilter().filter(searchData);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        lv_cust_details.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerViewPositionHelper mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
                int visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mRecyclerViewHelper.getItemCount();
                firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();

                Log.e(" arr_count ", "" + arr_count);

                if (arr_count == 100 && lazyScroll.equals("OFF"))
                {
                    customerDetailsList.add(null);
                    customerDetailAdapter.notifyItemInserted(customerDetailsList.size() - 1);
                    pos = customerDetailsList.size() - 1;
                    Log.e( "onScrollStateChanged: ", ""+pos);
                    arr_count = 0;
                    lazyScroll = "ON";
                    android.os.Handler h = new android.os.Handler();
                    h.postDelayed(new Runnable()
                    {
                        public void run()
                        {
                            if (flag == false)
                            {
                                Log.e("run: ", "one");
                                linear_engagement_type_nm.setVisibility(View.GONE);
                                requestCustomerDetail();
                            }
                            else
                            {
                                Log.e("run: ", "two");
                                linear_engagement_type_nm.setVisibility(View.VISIBLE);
                                requestEngagementBandDetail(context,userId,bearertoken);

                            }
                        }
                    }, 5000);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        lv_cust_details.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position)
            {
                Intent intent = new Intent(context, CustomerDetailActivity.class);
                intent.putExtra("uniqueCustomer", customerDetailsList.get(position).getUniqueCustomer());
                Log.e("Customer Id ", "" + customerDetailsList.get(position).getUniqueCustomer());
                startActivity(intent);
            }
        }));
        return root;
    }

    public CustomerLookup_PageTwo()
    {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.e("setUserVisibleHint: ", "" + isVisibleToUser);
            if (checkNetworkFalse) {
                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void initialise()
    {
        customerDetailsList = new ArrayList<CustomerDetail>();
        detailArrayList = new ArrayList<CustomerDetail>();
        edt_cust_Search = (EditText) root.findViewById(R.id.edt_cust_Search);
        txt_engagemnt_nm = (TextView) root.findViewById(R.id.txt_engagemnt_nm);
        btn_reset = (Button)root.findViewById(R.id.btn_reset);
        linear_engagement_type_nm = (LinearLayout)root.findViewById(R.id.linear_engagement_type_nm);
        lv_cust_details = (RecyclerView) root.findViewById(R.id.lv_cust_details);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (Reusable_Functions.chkStatus(context))
                {
                    Reusable_Functions.sDialog(context, "Loading...");
                    customerDetailsList.clear();
                    offset = 0;
                    limit = 100;
                    count = 0;
                    flag = false;
                    linear_engagement_type_nm.setVisibility(View.GONE);
                    requestCustomerDetail();
                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        this.context = context;
        try
        {
            onEngagemntBandClick = (OnEngagemntBandClick) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()
                    + " must implement IFragmentToActivity");
        }
    }

    public void fragmentCommunication(String engagementBand, boolean bandClick, Context mcontext, String user_id, String bearer_token) {
        e_bandnm = engagementBand;
        band_Click = bandClick;
        context = mcontext;
        userId = user_id;
        bearertoken = bearer_token;
        Log.e("fragmentCommunication: ", " " + band_Click + e_bandnm + userId + bearertoken);
        linear_engagement_type_nm.setVisibility(View.VISIBLE);
        txt_engagemnt_nm.setText(e_bandnm);
        if (Reusable_Functions.chkStatus(mcontext)) {
            Reusable_Functions.sDialog(mcontext, "Loading...");
        flag = true;
        customerDetailsList = new ArrayList<CustomerDetail>();
        lv_cust_details.removeAllViews();
        offset = 0;
        limit = 100;
        count = 0;
        lazyScroll = "OFF";
        arr_count = 0;
        requestEngagementBandDetail(context, userId, bearertoken);
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }


    private void requestCustomerDetail()
    {
        String url = ConstsCore.web_url + "/v1/display/customerdetails/" + updated_userId + "?engagementFor=" + engagementFor + "&recache=" + recache + "&offset=" + offset + "&limit=" + limit;
        Log.e("detail url :", "" + url);
        postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("response two:", "" + response);
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                return;

                            } else if (response.length() == limit) {

                                for (int i = 0; i < response.length(); i++)
                                {
                                    customerDetail = gson.fromJson(response.get(i).toString(), CustomerDetail.class);
                                    customerDetailsList.add(customerDetail);
                                    Log.e("===two first", " " + customerDetailsList.get(i).getFullName() + "  " + customerDetailsList.get(i).getMbrPlanSaleNetVal());

                                }

                                arr_count = response.length();
                                flag = false;
                                offset = offset + limit;

                            } else if (response.length() < limit)
                            {

                                for (int i = 0; i < response.length(); i++) {
                                    customerDetail = gson.fromJson(response.get(i).toString(), CustomerDetail.class);
                                    customerDetailsList.add(customerDetail);
                                    Log.e("===two second", " " + customerDetailsList.get(i).getFullName() + "  " + customerDetailsList.get(i).getMbrPlanSaleNetVal());

                                }
                                arr_count = response.length();
                                flag = false;
                                offset = offset + limit;

                            }
                            if (lazyScroll.equals("ON"))
                            {
                                customerDetailAdapter.notifyDataSetChanged();
                                lazyScroll = "OFF";
                                customerDetailsList.remove(pos);


                            }
                            else
                            {
                                lv_cust_details.setLayoutManager(new LinearLayoutManager(lv_cust_details.getContext(), 48 == Gravity.CENTER_HORIZONTAL ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                lv_cust_details.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(lv_cust_details);
                                customerDetailAdapter = new CustomerDetailAdapter(customerDetailsList, context);
                                lv_cust_details.setAdapter(customerDetailAdapter);
                                customerDetailAdapter.notifyDataSetChanged();

                            }
                            Reusable_Functions.hDialog();

                        } catch (Exception e) {
                            customerDetailsList.remove(pos);
                            customerDetailAdapter.notifyDataSetChanged();

                             Reusable_Functions.hDialog();
                            Log.e("exception :", "" + e.getMessage());
                            Toast.makeText(context, "data failed...." + e.toString(), Toast.LENGTH_SHORT).show();
                            Reusable_Functions.hDialog();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        customerDetailsList.remove(pos);
                        customerDetailAdapter.notifyDataSetChanged();
                        cust_progressBar.setVisibility(View.GONE);
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


    private void requestEngagementBandDetail(final Context mcontext, String user_id, final String bearer_token)
    {
        Cache cache = new DiskBasedCache(mcontext.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        RequestQueue mqueue;
        mqueue = new RequestQueue(cache, network);
        mqueue.start();
        user_id = user_id.substring(0, user_id.length() - 5);
        String url = "";
        final Gson gson;
        gson = new Gson();
        if (band_Click) {

            url = ConstsCore.web_url + "/v1/display/customerdetails/" + user_id + "?engagementFor=" + engagementFor + "&engagementBrand=" + e_bandnm.replace(" ", "%20") + "&recache=" + recache + "&offset=" + offset + "&limit=" + limit;
        } else {
            url = ConstsCore.web_url + "/v1/display/customerdetails/" + user_id + "?engagementFor=" + engagementFor + "&lifeStage=" + e_bandnm.replace(" ", "%20") + "&recache=" + recache + "&offset=" + offset + "&limit=" + limit;
        }
        Log.e("detail url 1:", "" + url);
        JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("response page one:", "" + response + " size " + response.length());
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(mcontext, "no data found", Toast.LENGTH_SHORT).show();
                                return;

                            } else if (response.length() == limit) {

                                for (int i = 0; i < response.length(); i++) {
                                    customerDetail = gson.fromJson(response.get(i).toString(), CustomerDetail.class);
                                    customerDetailsList.add(customerDetail);

                                }
                                arr_count = response.length();
                                Log.e("array size: ", "" + customerDetailsList.size() + "\t"+ arr_count);
                                flag = true;
                                offset = offset + limit;

                            } else if (response.length() < limit)
                            {

                                for (int i = 0; i < response.length(); i++) {
                                    customerDetail = gson.fromJson(response.get(i).toString(), CustomerDetail.class);
                                    customerDetailsList.add(customerDetail);

                                }
                                arr_count = response.length();
                                Log.e("array size: ", "" + customerDetailsList.size() + "\t"+ arr_count);
                                flag = true;
                                offset = offset + limit;

                            }
                            if (lazyScroll.equals("ON"))
                            {
                                customerDetailAdapter.notifyDataSetChanged();
                                lazyScroll = "OFF";
                                customerDetailsList.remove(pos);
                            }
                            else
                            {
                                customerDetailAdapter = new CustomerDetailAdapter(customerDetailsList, mcontext);
                                lv_cust_details.setAdapter(customerDetailAdapter);
                                customerDetailAdapter.notifyDataSetChanged();

                            }
                            Reusable_Functions.hDialog();

                        } catch (Exception e)
                        {
                            customerDetailsList.remove(pos);
                            customerDetailAdapter.notifyDataSetChanged();
                            Reusable_Functions.hDialog();
                            Log.e("exception :", "" + e.getMessage());
                            Toast.makeText(mcontext, "data failed...." + e.toString(), Toast.LENGTH_SHORT).show();
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
                        customerDetailsList.remove(pos);
                        customerDetailAdapter.notifyDataSetChanged();
                        Reusable_Functions.hDialog();
                        Toast.makeText(mcontext, "server not responding..", Toast.LENGTH_SHORT).show();
                        Reusable_Functions.hDialog();
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + bearer_token);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        mqueue.add(postRequest);
    }
}

