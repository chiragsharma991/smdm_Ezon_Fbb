package apsupportapp.aperotechnologies.com.designapp.KeyProductPlan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import android.widget.EditText;

import android.widget.ExpandableListView;
import android.widget.ListView;

import android.widget.RelativeLayout;
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

import org.json.JSONArray;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Arrays;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.HourlyPerformence.HourlyLocationAdapter;
import apsupportapp.aperotechnologies.com.designapp.HourlyPerformence.HourlyStoreFilterActivity;
import apsupportapp.aperotechnologies.com.designapp.MySingleton;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesAnalysisProductAdapter;

import static apsupportapp.aperotechnologies.com.designapp.KeyProductPlan.KeyProductPlanActivity.keyproductPlanActivity;

/**
 * Created by pamrutkar on 03/03/17.
 */
public class KeyProductFilterActivity extends AppCompatActivity implements View.OnClickListener{

    RelativeLayout btnBack,btnProd_Done;
    EditText keyprod_editSearch;
    MySingleton m_config;
    ListView keyprod_listView;
    RequestQueue queue;
    ArrayList<String> keyPlanProductList;
    KeyProdFilterAdapter adapter;
    Context context;
    String userId, bearertoken;
    SharedPreferences sharedPreferences;

    String search_data;
    int offsetvalue = 0, limit = 100;
    int count = 0;
    ArrayList<String> loc_listDataHeader;
    HashMap<String, List<String>> loc_listDataChild;
    public static ExpandableListView explv_key_locatn;
    static HourlyLocationAdapter hourlyLocationAdapter;
    public int store_level_filter = 3;
    private StringBuilder build = new StringBuilder();
    static List<String> key_storeList;
    String geoLevel2Code, lobId;
    RelativeLayout rel_process_filter;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_product_filter);
        getSupportActionBar().hide();
        m_config = MySingleton.getInstance(context);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLevel2Code = sharedPreferences.getString("concept", "");
        lobId = sharedPreferences.getString("lobid", "");
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        context = this;
        rel_process_filter = (RelativeLayout) findViewById(R.id.rel_process_filter);
        rel_process_filter.setVisibility(View.VISIBLE);

        key_storeList = new ArrayList<String>();
        initialize();
        prepareData();
        hourlyLocationAdapter = new HourlyLocationAdapter(this, loc_listDataHeader, loc_listDataChild, explv_key_locatn, hourlyLocationAdapter);
        explv_key_locatn.setAdapter(hourlyLocationAdapter);
        explv_key_locatn.setNestedScrollingEnabled(true);
        keyprod_listView.setNestedScrollingEnabled(true);

//        setListViewHeight1(keyprod_listView);

        explv_key_locatn.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                setListViewHeight1(parent, groupPosition);
                return false;
            }
        });

//        if (Reusable_Functions.chkStatus(context)) {
//            Reusable_Functions.hDialog();
//            Reusable_Functions.sDialog(context, "Loading  data...");
//            offsetvalue = 0;
//            count = 0;
//            requestProductListAPI(offsetvalue, limit);
//        } else {
//
//            Toast.makeText(KeyProductFilterActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
//        }

        keyprod_editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                search_data =  keyprod_editSearch.getText().toString();

                keyprod_editSearch.clearFocus();
                InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow( keyprod_editSearch.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                adapter.getFilter().filter(search_data);
                hourlyLocationAdapter.filterData(search_data);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search_data =  keyprod_editSearch.getText().toString();

                keyprod_editSearch.clearFocus();
                InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow( keyprod_editSearch.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                adapter.getFilter().filter(search_data);
                hourlyLocationAdapter.filterData(search_data);
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                search_data =  keyprod_editSearch.getText().toString();
                keyprod_editSearch.clearFocus();
                InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow( keyprod_editSearch.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                adapter.getFilter().filter(search_data);
                hourlyLocationAdapter.filterData(search_data);
            }
        });

    }



    private void initialize() {

        btnBack = (RelativeLayout)findViewById(R.id.keyprodfilter_btnBack);
        btnProd_Done =(RelativeLayout)findViewById(R.id.keyproduct_btnfilterdone) ;
        keyprod_editSearch = (EditText)findViewById(R.id.keyprodfilter_editSearch);
        keyprod_listView = (ListView)findViewById(R.id.keyproduct_listview);
        keyPlanProductList = new ArrayList<String>();
        btnBack.setOnClickListener(this);
        btnProd_Done.setOnClickListener(this);
        explv_key_locatn = (ExpandableListView) findViewById(R.id.explv_key_locatn);
        explv_key_locatn.setTextFilterEnabled(true);
        explv_key_locatn.setDivider(getResources().getDrawable(R.color.grey));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.keyprodfilter_btnBack:
                onBackPressed();
                break;
            case R.id.keyproduct_btnfilterdone:
                selectbuild();

                String filtervalue =  Arrays.toString(KeyProdFilterAdapter.checkedValue.toArray());
                filtervalue = filtervalue.replace("[","");
                String updatedfilterValue = filtervalue.replace("]","");
                Intent intent = new Intent(KeyProductFilterActivity.this,KeyProductPlanActivity.class);
                updatedfilterValue = updatedfilterValue.replace(", ",",");
                intent.putExtra("productfilterValue",updatedfilterValue);
                if(build.length() != 0)
                {
                    intent.putExtra("selectedStringVal", build.toString());
                }
                startActivity(intent);
                keyproductPlanActivity.finish();
                finish();
                HourlyLocationAdapter.hr_store_str = "";
                break;
        }
    }


    private void selectbuild() {


        if (HourlyLocationAdapter.hr_store_str.length() != 0) {
            String store = HourlyLocationAdapter.hr_store_str;//.substring(0,4);
            String Store;
            Store = "storeCode=" + store;
            build.append("&");
            build.append(Store.replace(",$", ""));
        }


    }

    private void requestProductListAPI(int offsetvalue2, final int limit2)
    {

        String url = ConstsCore.web_url + "/v1/display/keyproducts/" + userId + "?offset=" + offsetvalue + "&limit=" + limit;
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
//                                Reusable_Functions.hDialog();
                                rel_process_filter.setVisibility(View.GONE);
                                Toast.makeText(KeyProductFilterActivity.this, "no data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {
                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                   String prodName= response.getString(i);
                                    keyPlanProductList.add(prodName);
                                }

                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestProductListAPI(offsetvalue, limit);

                            } else if (response.length() < limit) {
                                Reusable_Functions.hDialog();

                                for (int i = 0; i < response.length(); i++) {
                                    String prodName= response.getString(i);

                                    keyPlanProductList.add(prodName);
                                }
                                adapter = new KeyProdFilterAdapter(keyPlanProductList, getApplicationContext());
                                keyprod_listView.setAdapter(adapter);
//                                Reusable_Functions.hDialog();
                                rel_process_filter.setVisibility(View.GONE);
                                if (loc_listDataHeader.get(0).equals("Store"))
                                {
                                    rel_process_filter.setVisibility(View.VISIBLE);
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    requestStore(offsetvalue, limit);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            rel_process_filter.setVisibility(View.GONE);
                        }
                        finally {
                            if (response.equals("") || response == null || response.length() == 0) {
                                if (loc_listDataHeader.get(0).equals("Store"))
                                {
                                    rel_process_filter.setVisibility(View.VISIBLE);
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    requestStore(offsetvalue, limit);
                                }
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Reusable_Functions.hDialog();
                        rel_process_filter.setVisibility(View.GONE);
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
        super.onBackPressed();
        Intent intent = new Intent(KeyProductFilterActivity.this,KeyProductPlanActivity.class);
        startActivity(intent);
        finish();
    }



    private void setListViewHeight1(ExpandableListView listView, int group) {
        HourlyLocationAdapter listAdapter = (HourlyLocationAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void prepareData()
    {
        loc_listDataHeader = new ArrayList<String>();
        loc_listDataChild = new HashMap<String, List<String>>();
        loc_listDataHeader.add("Store");

        if (Reusable_Functions.chkStatus(context)) {
//            Reusable_Functions.hDialog();
//            Reusable_Functions.sDialog(context, "Loading  data...");
            rel_process_filter.setVisibility(View.VISIBLE);
            offsetvalue = 0;
            count = 0;
            requestProductListAPI(offsetvalue, limit);
        } else {

            Toast.makeText(KeyProductFilterActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
        }

        loc_listDataChild.put(loc_listDataHeader.get(0), key_storeList);
        rel_process_filter.setVisibility(View.GONE);

    }

    private void requestStore(int offsetval, int limitval) {
        //        store_url = ConstsCore.web_url + "/v1/display/storeselection/" + userId  + "?geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;

        String store_url = "";
        store_url = ConstsCore.web_url + "/v1/display/storeselection/" + userId  + "?geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, store_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0)
                            {
//                                Reusable_Functions.hDialog();
                                rel_process_filter.setVisibility(View.GONE);
                                Toast.makeText(KeyProductFilterActivity.this, "no data found in store ", Toast.LENGTH_LONG).show();
                            }
                            else{
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String store = productName1.getString("storeCode");
                                    key_storeList.add(store);
                                }
                                rel_process_filter.setVisibility(View.GONE);

                            }


                           /* else if (response.length() == limit)
                            {
                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String store = productName1.getString("descEz");
                                    key_storeList.add(store);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestStore(offsetvalue, limit);
                            }
                            else if (response.length() < limit)
                            {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String store = productName1.getString("descEz");
                                    key_storeList.add(store);
                                }
                                rel_process_filter.setVisibility(View.GONE);

                            }*/


                        }
                        catch (Exception e)
                        {
                            Reusable_Functions.hDialog();
                            rel_process_filter.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        rel_process_filter.setVisibility(View.GONE);
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
