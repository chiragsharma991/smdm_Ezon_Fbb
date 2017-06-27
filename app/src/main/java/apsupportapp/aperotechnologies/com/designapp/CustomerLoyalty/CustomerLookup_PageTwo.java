package apsupportapp.aperotechnologies.com.designapp.CustomerLoyalty;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.EditText;
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
import apsupportapp.aperotechnologies.com.designapp.RecyclerItemClickListener;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;

/**
 * Created by pamrutkar on 14/06/17.
 */
public class CustomerLookup_PageTwo extends Fragment {
    String e_bandnm;
    OnEngagemntBandClick onEngagemntBandClick;
    TextView txt_engagementnm_Val, txt_pending_Val,txt_color_engagemnt_nm;
    static EditText edt_cust_Search;
    RecyclerView lv_cust_details;
    CustomerDetail customerDetail;
    CustomerDetailAdapter customerDetailAdapter;
    ArrayList<CustomerDetail> detailArrayList,dublicateCustDetailList;
    ViewGroup root;
    Context context;
    SharedPreferences sharedPreferences;
    String userId, bearertoken, geoLeveLDesc, engagementFor = "EVENT";
    RequestQueue queue;
    Gson gson;
    private boolean checkNetworkFalse=false;
    private String recache = "true";
    String updated_userId;
    int offset = 0, count = 0, limit = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = (ViewGroup) inflater.inflate(R.layout.fragment_custlookup_pagetwo, null);
        context = root.getContext();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = sharedPreferences.getString("userId", "");
        updated_userId = userId.substring(0, userId.length() - 5);
        Log.e("update_userId", "" + updated_userId);
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
            offset = 0;
            limit = 10;
            count = 0;
            txt_color_engagemnt_nm.setBackground(null);
            txt_engagementnm_Val.setText("");
            requestCustomerDetail();
        }
        else
        {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
        dublicateCustDetailList = new ArrayList<CustomerDetail>();
        dublicateCustDetailList.addAll(detailArrayList);
        edt_cust_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String searchData = edt_cust_Search.getText().toString();
                customerDetailAdapter.getFilter().filter(searchData);

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });




//            @Override
      lv_cust_details.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
          @Override
          public void onItemClick(View view, int position)
          {
               Intent intent = new Intent(context,CustomerDetailActivity.class);
               intent.putExtra("uniqueCustomer",detailArrayList.get(position).getUniqueCustomer());
               Log.e("Customer Id ",""+detailArrayList.get(position).getUniqueCustomer());
               startActivity(intent);
          }
      }));
        return root;
    }

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

    private void initialise()
    {
        detailArrayList = new ArrayList<CustomerDetail>();
        edt_cust_Search = (EditText)root.findViewById(R.id.edt_cust_Search);
        txt_color_engagemnt_nm = (TextView)root.findViewById(R.id.txt_color_engagemnt_nm);
        txt_engagementnm_Val = (TextView) root.findViewById(R.id.txt_engagementnm_Val);
        txt_pending_Val = (TextView) root.findViewById(R.id.txt_pending_Val);
        lv_cust_details = (RecyclerView) root.findViewById(R.id.lv_cust_details);

    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        this.context = context;
        try
        {
            onEngagemntBandClick = (OnEngagemntBandClick) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement IFragmentToActivity");
        }
    }
//
    public void fragmentCommunication(String enagagemntband)
    {
        e_bandnm = enagagemntband;
        Log.e("engagement band :", "" + e_bandnm);

//        if (Reusable_Functions.chkStatus(context)) {
//            Reusable_Functions.progressDialog = new ProgressDialog(context);
//            Reusable_Functions.progressDialog.setCancelable(false);
//            if(!Reusable_Functions.progressDialog.isShowing())
//            {
//                Reusable_Functions.progressDialog.show();
//            }
//            Reusable_Functions.progressDialog.setMessage("Loading...");
            detailArrayList = new ArrayList<CustomerDetail>();
            offset = 0;
            limit = 10;
            count = 0;
            requestEngagementBandDetail();
//        } else {
//            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
//        }

    }

    private void requestEngagementBandDetail()
    {
        String url = ConstsCore.web_url + "/v1/display/customerdetails/" + updated_userId + "?engagementFor=" + engagementFor +"&engagementBrand="+ e_bandnm;//+ "&offset=" + offset + "&limit=" + limit;
        Log.e("detail url 1:",""+url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
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
                                    detailArrayList.add(customerDetail);
                                }
                            // }
                            lv_cust_details.setLayoutManager(new LinearLayoutManager(lv_cust_details.getContext(), 48 == Gravity.CENTER_HORIZONTAL ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                            lv_cust_details.setOnFlingListener(null);
                            new GravitySnapHelper(48).attachToRecyclerView(lv_cust_details);
                            customerDetailAdapter = new CustomerDetailAdapter(detailArrayList, context);
                            lv_cust_details.setAdapter(customerDetailAdapter);
                          //  txt_color_engagemnt_nm.setBackground(Color.parseColor(R.color.smdm_green));
                            txt_engagementnm_Val.setText(e_bandnm);
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

    private void requestCustomerDetail() {
        String url = ConstsCore.web_url + "/v1/display/customerdetails/" + updated_userId + "?engagementFor=" + engagementFor +"&recache=" + recache;//+ "&offset=" + offset + "&limit=" + limit;
        Log.e("detail url :",""+url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("response :",""+response);
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
                                    detailArrayList.add(customerDetail);
                                }
                           // }

                            lv_cust_details.setLayoutManager(new LinearLayoutManager(lv_cust_details.getContext(), 48 == Gravity.CENTER_HORIZONTAL ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                            lv_cust_details.setOnFlingListener(null);
                            new GravitySnapHelper(48).attachToRecyclerView(lv_cust_details);
                            customerDetailAdapter = new CustomerDetailAdapter(detailArrayList, context);
                            lv_cust_details.setAdapter(customerDetailAdapter);

                            Reusable_Functions.hDialog();
                        } catch (Exception e)
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
}

