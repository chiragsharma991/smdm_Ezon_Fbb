package apsupportapp.aperotechnologies.com.designapp.Ezone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.BestPerformersInventory.BestPerformerInventory;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.FreshnessIndex.FreshnessIndexActivity;
import apsupportapp.aperotechnologies.com.designapp.PvaSalesAnalysis.SalesPvAActivity;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;


import static apsupportapp.aperotechnologies.com.designapp.Ezone.EzoneSalesAnalysisActivity1.Ezone_SalesAnalysisActivity;

import static apsupportapp.aperotechnologies.com.designapp.Ezone.EzoneSalesAnalysisActivity1.Ezone_SalesAnalysisActivity;

/**
 * Created by pamrutkar on 05/06/17.
 */
public class EzoneSalesFilter extends AppCompatActivity implements View.OnClickListener {

    private TextView txt_ez_location, txt_ez_prod;
    private RelativeLayout rel_ez_sfilter_back, rel_ez_sfilter_done;
    static LinearLayout lin_ez_locatn, lin_ez_prod;
    ArrayList<String> loc_listDataHeader, prod_listDataHeader;
    HashMap<String, List<String>> loc_listDataChild, prod_listDataChild;
    private EditText et_ez_search;
    public static ExpandableListView explv_ez_locatn, explv_ez_prod;
    Context context = this;
    public static RelativeLayout rel_ez_process_filter;
    private String str_filter_location = "NO", str_filter_prod = "NO";
    static EzoneFilterProductAdapter prod_list_adapter;
    static EzoneFilterLocationAdapter locatn_list_adapter;
    int offset = 0, limit = 100, count = 0;
    public int ez_level_filter = 1, ez_prod_level = 1;
    String userId, bearertoken, geoLevel2Code, lobId;
    public int ezone_filter_level = 1;
    private Intent intent;
    SharedPreferences sharedPreferences;
    RequestQueue queue;
    static String str_checkFrom;
    private int filter_level = 0;
    private StringBuilder build = new StringBuilder();


    static List<String> ez_regionList, ez_storeList, ez_deptList, ez_categryList, ez_classList, ez_brandList, ez_mcList;
    // git 09-06-17


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ezone_sfilter);
        getSupportActionBar().hide();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        context = this;
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLevel2Code = sharedPreferences.getString("concept", "");
        lobId = sharedPreferences.getString("lobid","");
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        ez_regionList = new ArrayList<String>();
        ez_storeList = new ArrayList<String>();
        ez_deptList = new ArrayList<String>();
        ez_categryList = new ArrayList<String>();
        ez_classList = new ArrayList<String>();
        ez_brandList = new ArrayList<String>();
        ez_mcList = new ArrayList<String>();
        str_checkFrom = getIntent().getStringExtra("checkfrom");
        initialise_ui();
        prepareData();
        locatn_list_adapter = new EzoneFilterLocationAdapter(this, loc_listDataHeader, loc_listDataChild, explv_ez_locatn, locatn_list_adapter);
        explv_ez_locatn.setAdapter(locatn_list_adapter);
        prod_list_adapter = new EzoneFilterProductAdapter(this, prod_listDataHeader, prod_listDataChild, explv_ez_prod, prod_list_adapter);
        explv_ez_prod.setAdapter(prod_list_adapter);

        explv_ez_locatn.setNestedScrollingEnabled(true);
        explv_ez_prod.setNestedScrollingEnabled(true);

        explv_ez_locatn.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                setListViewHeight1(parent, groupPosition);
                return false;
            }
        });
        explv_ez_prod.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                setListViewHeight(parent, groupPosition);
                return false;
            }
        });
        et_ez_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                Boolean handled = false;
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_NEXT) || (actionId == EditorInfo.IME_ACTION_NONE) || (actionId == EditorInfo.IME_ACTION_SEARCH)) {
                    et_ez_search.clearFocus();
                    InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputManager != null) {
                        inputManager.hideSoftInputFromWindow(et_ez_search.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    handled = true;
                }
                return handled;
            }
        });

        et_ez_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(et_ez_search.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                s = et_ez_search.getText().toString();
                Log.e("s :", "" + s);
                locatn_list_adapter.filterData(s.toString());
                prod_list_adapter.filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setListViewHeight1(ExpandableListView listView, int group) {
        EzoneFilterLocationAdapter listAdapter = (EzoneFilterLocationAdapter) listView.getExpandableListAdapter();
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


    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        EzoneFilterProductAdapter listAdapter = (EzoneFilterProductAdapter) listView.getExpandableListAdapter();
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
        prod_listDataHeader = new ArrayList<String>();
        loc_listDataChild = new HashMap<String, List<String>>();
        prod_listDataChild = new HashMap<String, List<String>>();

        loc_listDataHeader.add("Region");
        loc_listDataHeader.add("Store");
        if (getIntent().getStringExtra("checkfrom").equals("ezoneSales") || getIntent().getStringExtra("checkfrom").equals("ezonepvaAnalysis")) {
            prod_listDataHeader.add("Department");
            prod_listDataHeader.add("Subdept");
            prod_listDataHeader.add("Class");
            prod_listDataHeader.add("Subclass");
        } else {
            prod_listDataHeader.add("Department");
            prod_listDataHeader.add("Subdept");
            prod_listDataHeader.add("Class");
            prod_listDataHeader.add("Subclass");
            prod_listDataHeader.add("MC");
        }

        if (Reusable_Functions.chkStatus(EzoneSalesFilter.this)) {
            if (loc_listDataHeader.get(0).equals("Region")) {
                rel_ez_process_filter.setVisibility(View.VISIBLE);
                offset = 0;
                limit = 100;
                count = 0;
                ez_level_filter = 1;
                requestEzoneRegion(offset, limit);
            }
        } else {
            Toast.makeText(EzoneSalesFilter.this, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }


        if (getIntent().getStringExtra("checkfrom").equals("ezoneSales") || getIntent().getStringExtra("checkfrom").equals("ezonepvaAnalysis")) {
            listdataFill();
        } else {
            listdataFill();
            prod_listDataChild.put(prod_listDataHeader.get(4), ez_mcList); // Header, Child data

        }

    }

    private void listdataFill()
    {
        loc_listDataChild.put(loc_listDataHeader.get(0), ez_regionList); // Header, Child data
        loc_listDataChild.put(loc_listDataHeader.get(1), ez_storeList);
        prod_listDataChild.put(prod_listDataHeader.get(0), ez_deptList); // Header, Child data
        prod_listDataChild.put(prod_listDataHeader.get(1), ez_categryList);
        prod_listDataChild.put(prod_listDataHeader.get(2), ez_classList); // Header, Child data
        prod_listDataChild.put(prod_listDataHeader.get(3), ez_brandList);
    }


    private void initialise_ui() {
        rel_ez_sfilter_back = (RelativeLayout) findViewById(R.id.rel_ez_sfilter_back);
        rel_ez_sfilter_done = (RelativeLayout) findViewById(R.id.rel_ez_sfilter_done);
        rel_ez_process_filter = (RelativeLayout) findViewById(R.id.rel_ez_process_filter);
        et_ez_search = (EditText) findViewById(R.id.et_ez_search);
        et_ez_search.setSingleLine(true);
        explv_ez_locatn = (ExpandableListView) findViewById(R.id.explv_ez_location);
        explv_ez_locatn.setTextFilterEnabled(true);
        explv_ez_locatn.setDivider(getResources().getDrawable(R.color.grey));
        explv_ez_locatn.setDividerHeight(2);
        explv_ez_prod = (ExpandableListView) findViewById(R.id.explv_ez_product);
        explv_ez_prod.setTextFilterEnabled(true);
        explv_ez_prod.setDivider(getResources().getDrawable(R.color.grey));
        explv_ez_prod.setDividerHeight(2);
        rel_ez_sfilter_done.setOnClickListener(this);
        rel_ez_sfilter_back.setOnClickListener(this);
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_ez_sfilter_back:
                onBackPressed();
                break;
            case R.id.rel_ez_sfilter_done:
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                //Send selected hierarchy level to selected activity
                if (getIntent().getStringExtra("checkfrom").equals("ezoneSales") || getIntent().getStringExtra("checkfrom").equals("ezonepvaAnalysis")) {
                    selectbuild();

                } else {
                    selectbuild();
                    if (EzoneFilterProductAdapter.brandcls_text.length() != 0) {
                        String brandcls = EzoneFilterProductAdapter.brandcls_text.replace("%", "%25");
                        String updateBrandCls = brandcls.replace(" ", "%20").replace("&", "%26");
                        String Brandclass = "brandclass=" + updateBrandCls;
                        build.append("&");
                        build.append(Brandclass.replace(",$", ""));

                    }
                }

                if (getIntent().getStringExtra("checkfrom").equals("ezoneSales")) {
                    intent = new Intent(EzoneSalesFilter.this, EzoneSalesAnalysisActivity1.class);
                    if (build.length() != 0) {
                        Ezone_SalesAnalysisActivity.finish();
                    }
                    callback(build);
                } else if (getIntent().getStringExtra("checkfrom").equals("ezonebestPerformers")) {
                    intent = new Intent(EzoneSalesFilter.this, EzoneBestPerformerInventory.class);
                    if (build.length() != 0) {
                        EzoneBestPerformerInventory.ezone_bestperoformer.finish();

                    }
                    callback(build);
                }
                else if (getIntent().getStringExtra("checkfrom").equals("ezonefreshnessIndex")) {
                    intent = new Intent(EzoneSalesFilter.this, EzoneFreshnessIndexActivity.class);
                    if (build.length() != 0) {
                        EzoneFreshnessIndexActivity.ezone_freshness_Index.finish();
                        Log.e("TAG", "freshnessIndex:  call finish ");


                    }
                    callback(build);
                }

                else if (getIntent().getStringExtra("checkfrom").equals("ezonepvaAnalysis")) {
                    intent = new Intent(EzoneSalesFilter.this, EzoneSalesPvAActivity.class);
                    if (build.length() != 0) {
                        EzoneSalesPvAActivity.Ezone_Sales_Pva_Activity.finish();
                        Log.e("TAG", "freshnessIndex:  call finish ");


                    }
                    callback(build);
                }
                break;


        }
    }

    private void selectbuild() {
        if (EzoneFilterLocationAdapter.region_str.length() != 0)

        {
            String region = EzoneFilterLocationAdapter.region_str.replace("%", "%25");
            String updateRegion = region.replace(" ", "%20").replace("&", "%26");
            String Region;
            Region = "region=" + updateRegion;
            build.append("&");
            build.append(Region.replace(",$", ""));
        }

        if (EzoneFilterLocationAdapter.store_str.length() != 0)

        {
            String store = EzoneFilterLocationAdapter.store_str.replace("%", "%25");
            String updateStore = store.replace(" ", "%20").replace("&", "%26");
            String Store;
            Store = "store=" + updateStore;
            build.append("&");
            build.append(Store.replace(",$", ""));

        }

        if (EzoneFilterProductAdapter.department_text.length() != 0)

        {
            String deptmnt = EzoneFilterProductAdapter.department_text.replace("%", "%25");
            String updateDept = deptmnt.replace(" ", "%20").replace("&", "%26");
            String Department;
            Department = "department=" + updateDept;
            build.append("&");
            build.append(Department.replace(",$", ""));

        }

        if (EzoneFilterProductAdapter.categry_text.length() != 0)

        {
            String categry = EzoneFilterProductAdapter.categry_text.replace("%", "%25");
            String updateCategory = categry.replace(" ", "%20").replace("&", "%26");
            String Categary = "category=" + updateCategory;
            build.append("&");
            build.append(Categary.replace(",$", ""));

        }

        if (EzoneFilterProductAdapter.class_text.length() != 0)

        {
            String plancls = EzoneFilterProductAdapter.class_text.replace("%", "%25");
            String updatePlanClass = plancls.replace(" ", "%20").replace("&", "%26");
            String planclass = "class=" + updatePlanClass;
            build.append("&");
            build.append(planclass.replace(",$", ""));

        }

        if (EzoneFilterProductAdapter.brand_text.length() != 0)

        {
            String brand = EzoneFilterProductAdapter.brand_text.replace("%", "%25");
            String updateBrand = brand.replace(" ", "%20").replace("&", "%26");
            String Brand = "brand=" + updateBrand;
            build.append("&");
            build.append(Brand.replace(",$", ""));

        }

    }

    private void callback(StringBuilder build) {
        if (build.length() == 0) {
            Toast.makeText(context, "Please select value...", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            if (getIntent().getStringExtra("checkfrom").equals("ezoneSales") || getIntent().getStringExtra("checkfrom").equals("ezonepvaAnalysis")) {
                callFilterLevelSales();


            } else {
                callFilterLevelInventory();

            }
            intent.putExtra("selectedStringVal", build.toString());
            Log.e("TAG", "callback:  selectedStringVal" + build.toString());
            intent.putExtra("selectedlevelVal", filter_level);
            Log.e("TAG", "callback:  selectedlevelVal" + filter_level);
        }
        startActivity(intent);
        EzoneFilterProductAdapter.department_text = "";
        EzoneFilterProductAdapter.categry_text = "";
        EzoneFilterProductAdapter.class_text = "";
        EzoneFilterProductAdapter.brand_text = "";
        EzoneFilterProductAdapter.brandcls_text = "";
        EzoneFilterLocationAdapter.region_str = "";
        EzoneFilterLocationAdapter.store_str = "";
        str_checkFrom = "";
        finish();
    }

    private void callFilterLevelSales()
    {
        if (build.toString().contains("region") || build.toString().contains("store")) {
            filter_level = 9;
        } else {
            if (build.toString().contains("department")) {
                filter_level = 2;
            }
            if (build.toString().contains("category")) {
                filter_level = 3;
            }
            if (build.toString().contains("class")) {
                filter_level = 4;
            }
            if (build.toString().contains("brand")) {
                filter_level = 4;
            }
        }
    }

    private void callFilterLevelInventory() {
        if (build.toString().contains("region") || build.toString().contains("store")) {
            filter_level = 9;
        } else {
            if (build.toString().contains("department")) {
                filter_level = 2;
            }
            if (build.toString().contains("category")) {
                filter_level = 3;
            }
            if (build.toString().contains("class")) {
                filter_level = 4;
            }
            if (build.toString().contains("brand")) {
                filter_level = 5;
            }
            if (build.toString().contains("brandclass")) {
                filter_level = 5;
            }

        }
    }

    @Override
    public void onBackPressed() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        finish();
    }


    //------------------------------------API Declaration--------------------------------------//
    private void requestEzoneRegion(int offset1, int limit1) {
        String region_url = "";
//        if (getIntent().getStringExtra("checkfrom").equals("ezoneSales") || getIntent().getStringExtra("checkfrom").equals("pvaAnalysis")) {
//            // with geolevel2code field
            region_url = ConstsCore.web_url + "/v1/display/storehierarchyEZNew/" + userId + "?offset=" + offset + "&limit=" + limit + "&level=" + ez_level_filter + "&geoLevel2code=" + geoLevel2Code;
//        } else {
//            //without geolevel2code field
//            region_url = ConstsCore.web_url + "/v1/display/storehierarchyEZ/" + userId + "?offset=" + offset + "&limit=" + limit + "&level=" + ez_level_filter;
//
//        }
        Log.e("region url :", "" + region_url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, region_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        Log.e("region response :", "" + response);
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                rel_ez_process_filter.setVisibility(View.GONE);
                                Toast.makeText(EzoneSalesFilter.this, "no data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit)
                            {
                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String region = productName1.getString("descEz");
                                    ez_regionList.add(region);
                                }
                                offset = (limit * count) + limit;
                                count++;
                                requestEzoneRegion(offset, limit);

                            } else if (response.length() < limit)
                            {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String region = productName1.getString("descEz");
                                    ez_regionList.add(region);
                                }
                                rel_ez_process_filter.setVisibility(View.GONE);

                                if (loc_listDataHeader.get(1).equals("Store"))
                                {
                                    rel_ez_process_filter.setVisibility(View.VISIBLE);
                                    offset = 0;
                                    limit = 100;
                                    count = 0;
                                    ez_level_filter = 3;
                                    requestEzoneStore(offset, limit);
                                }
                            }
                        } catch (Exception e)
                        {
                            Reusable_Functions.hDialog();
                            rel_ez_process_filter.setVisibility(View.GONE);
                            e.printStackTrace();

                        }
                        finally
                        {
                            if(response.equals("") || response == null || response.length() == 0)
                            {
                                if (loc_listDataHeader.get(1).equals("Store")) {
                                    rel_ez_process_filter.setVisibility(View.VISIBLE);
                                    offset = 0;
                                    limit = 100;
                                    count = 0;
                                    ez_level_filter = 3;
                                    requestEzoneStore(offset, limit);
                                }
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        rel_ez_process_filter.setVisibility(View.GONE);
                        error.printStackTrace();
                    }
                }
        )

        {
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

    private void requestEzoneStore(int offsetval, int limitval) {
        String store_url = "";
//        if (getIntent().getStringExtra("checkfrom").equals("ezoneSales") || getIntent().getStringExtra("checkfrom").equals("pvaAnalysis")) {
//            // with geolevel2code field
            store_url = ConstsCore.web_url + "/v1/display/storehierarchyEZNew/" + userId + "?offset=" + offset + "&limit=" + limit + "&level=" + ez_level_filter + "&geoLevel2Code=" + geoLevel2Code;

//        } else {
//            //without geolevel2code field
//            store_url = ConstsCore.web_url + "/v1/display/storehierarchyEZ/" + userId + "?offset=" + offset + "&limit=" + limit + "&level=" + ez_level_filter;
//
//        }
        Log.e("store url :", "" + store_url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, store_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        Log.e("store response :", "" + response);
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                rel_ez_process_filter.setVisibility(View.GONE);
                                Toast.makeText(EzoneSalesFilter.this, "no data found ", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {
                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String store = productName1.getString("descEz");
                                    ez_storeList.add(store);
                                }
                                offset = (limit * count) + limit;
                                count++;
                                requestEzoneStore(offset, limit);
                            } else if (response.length() < limit)
                            {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String store = productName1.getString("descEz");
                                    ez_storeList.add(store);
                                }
                                rel_ez_process_filter.setVisibility(View.GONE);
                                if (prod_listDataHeader.get(0).equals("Department")) {
                                    rel_ez_process_filter.setVisibility(View.VISIBLE);
                                    offset = 0;
                                    limit = 100;
                                    count = 0;
                                    ez_prod_level = 1;
                                    requestEzoneDepartment(offset, limit);
                                }
                            }
                        } catch (Exception e)
                        {
                            Reusable_Functions.hDialog();
                            rel_ez_process_filter.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                        finally
                        {
                            if(response.equals("") || response == null || response.length()==0) {

                                if (prod_listDataHeader.get(0).equals("Department")) {
                                    rel_ez_process_filter.setVisibility(View.VISIBLE);
                                    offset = 0;
                                    limit = 100;
                                    count = 0;
                                    ez_prod_level = 1;
                                    requestEzoneDepartment(offset, limit);
                                }
                            }

                        }

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        rel_ez_process_filter.setVisibility(View.GONE);
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

    // Department List
    public void requestEzoneDepartment(int offsetvalue1, int limit1)
    {
        String dept_url = "";
//        if (getIntent().getStringExtra("checkfrom").equals("ezoneSales") || getIntent().getStringExtra("checkfrom").equals("pvaAnalysis")) {
//            //with geolevel2code field
            dept_url = ConstsCore.web_url + "/v1/display/salesanalysishierarchyNew/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + ez_prod_level+ "&geoLevel2Code="+geoLevel2Code + "&lobId="+ lobId;
//        }
//        else
//        {
//            //without geolevel2code field
//            dept_url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + ez_prod_level;
//
//        }
        Log.e("dept_url :", "" + dept_url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, dept_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        Log.e("dept response :", "" + response);
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                rel_ez_process_filter.setVisibility(View.GONE);
                                Toast.makeText(EzoneSalesFilter.this, "no data found in department", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {
                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String plandept = productName1.getString("planDept");
                                    ez_deptList.add(plandept);
                                }
                                offset = (limit * count) + limit;
                                count++;
                                requestEzoneDepartment(offset, limit);
                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String plandept = productName1.getString("planDept");
                                    ez_deptList.add(plandept);
                                }
                                rel_ez_process_filter.setVisibility(View.GONE);
                                if (prod_listDataHeader.get(1).equals("Subdept")) {
                                    rel_ez_process_filter.setVisibility(View.VISIBLE);
                                    offset = 0;
                                    limit = 100;
                                    count = 0;
                                    ez_prod_level = 2;
                                    requestEzoneCategory(offset, limit);
                                }
                            }
                        } catch (Exception e)
                        {
                            Reusable_Functions.hDialog();
                            rel_ez_process_filter.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                        finally
                        {
                            if(response.equals("") || response == null || response.length()==0) {

                                if (prod_listDataHeader.get(1).equals("Subdept")) {
                                    rel_ez_process_filter.setVisibility(View.VISIBLE);
                                    offset = 0;
                                    limit = 100;
                                    count = 0;
                                    ez_prod_level = 2;
                                    requestEzoneCategory(offset, limit);
                                }
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        rel_ez_process_filter.setVisibility(View.GONE);
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

    //Category List
    public void requestEzoneCategory(int offsetvalue1, int limit1)
    {
        String category_url = "";
//        if (getIntent().getStringExtra("checkfrom").equals("ezoneSales") || getIntent().getStringExtra("checkfrom").equals("pvaAnalysis")) {
//            //with geolevel2code field
            category_url = ConstsCore.web_url + "/v1/display/salesanalysishierarchyNew/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + ez_prod_level + "&geoLevel2Code="+geoLevel2Code + "&lobId="+ lobId;
//        }
//        else
//        {
//            //without geolevel2code field
//            category_url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + ez_prod_level ;
//
//        }
        Log.e("categry_url :", "" + category_url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, category_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        Log.e("categry response :", "" + response);
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                rel_ez_process_filter.setVisibility(View.GONE);
                                Toast.makeText(EzoneSalesFilter.this, "no data found in subdept", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {

                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String planCategory = productName1.getString("planCategory");
                                    ez_categryList.add(planCategory);
                                }
                                offset = (limit * count) + limit;
                                count++;
                                requestEzoneCategory(offset, limit);
                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String planCategory = productName1.getString("planCategory");
                                    ez_categryList.add(planCategory);
                                }
                                rel_ez_process_filter.setVisibility(View.GONE);
                                if (prod_listDataHeader.get(2).equals("Class")) {
                                    rel_ez_process_filter.setVisibility(View.VISIBLE);
                                    offset = 0;
                                    limit = 100;
                                    count = 0;
                                    ez_prod_level = 3;
                                    requestEzonePlanClass(offset, limit);
                                }
                            }
                        } catch (Exception e)
                        {
                            Reusable_Functions.hDialog();
                            rel_ez_process_filter.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                        finally
                        {
                            if(response.equals("") || response == null || response.length()==0) {

                                if (prod_listDataHeader.get(2).equals("Class")) {
                                    rel_ez_process_filter.setVisibility(View.VISIBLE);
                                    offset = 0;
                                    limit = 100;
                                    count = 0;
                                    ez_prod_level = 3;
                                    requestEzonePlanClass(offset, limit);
                                }
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        rel_ez_process_filter.setVisibility(View.GONE);
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

    //Plan Class List
    public void requestEzonePlanClass(int offsetvalue1, int limit1)
    {
        String class_url = "";
//        if (getIntent().getStringExtra("checkfrom").equals("ezoneSales") || getIntent().getStringExtra("checkfrom").equals("pvaAnalysis")) {
//            //with geolevel2code field
            class_url = ConstsCore.web_url + "/v1/display/salesanalysishierarchyNew/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + ez_prod_level+ "&geoLevel2Code="+geoLevel2Code + "&lobId="+ lobId;
//        }
//        else
//        {
//            //without geolevel2code field
//            class_url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + ez_prod_level;
//
//        }
            Log.e("class_url :", "" + class_url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, class_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        Log.e("class response :", "" + response);
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                rel_ez_process_filter.setVisibility(View.GONE);
                                Toast.makeText(EzoneSalesFilter.this, "no data found in class", Toast.LENGTH_LONG).show();
                            }
                            else if (response.length() == limit) {
                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String planClass = productName1.getString("planClass");
                                    ez_classList.add(planClass);
                                }
                                offset = (limit * count) + limit;
                                count++;
                                requestEzonePlanClass(offset, limit);
                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String planClass = productName1.getString("planClass");
                                    ez_classList.add(planClass);
                                }
                                rel_ez_process_filter.setVisibility(View.GONE);

                                if (prod_listDataHeader.get(3).equals("Subclass")) {
                                    rel_ez_process_filter.setVisibility(View.VISIBLE);
                                    offset = 0;
                                    limit = 100;
                                    count = 0;
                                    ez_prod_level = 4;
                                    requestEzoneBrand(offset, limit);
                                }
                            }
                        } catch (Exception e)
                        {
                            Reusable_Functions.hDialog();
                            rel_ez_process_filter.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                        finally
                        {
                            if(response.equals("") || response == null || response.length()==0) {

                                if (prod_listDataHeader.get(3).equals("Subclass")) {
                                    rel_ez_process_filter.setVisibility(View.VISIBLE);
                                    offset = 0;
                                    limit = 100;
                                    count = 0;
                                    ez_prod_level = 4;
                                    requestEzoneBrand(offset, limit);
                                }
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        rel_ez_process_filter.setVisibility(View.GONE);
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


    //Brand Name List
    public void requestEzoneBrand(int offsetvalue1, int limit1)
    {
        String brand_url = "";
//        if (getIntent().getStringExtra("checkfrom").equals("ezoneSales") || getIntent().getStringExtra("checkfrom").equals("pvaAnalysis")) {
//            //with geolevel2code field
            brand_url = ConstsCore.web_url + "/v1/display/salesanalysishierarchyNew/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + ez_prod_level+"&geoLevel2Code="+geoLevel2Code + "&lobId="+ lobId;
//        }
//        else
//        {
//            //without geolevel2code field
//            brand_url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + ez_prod_level;
//
//        }
            Log.e("brand_url :", "" + brand_url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, brand_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        Log.e("brand response :", "" + response);
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                rel_ez_process_filter.setVisibility(View.GONE);
                                Toast.makeText(EzoneSalesFilter.this, "no data found in subclass", Toast.LENGTH_LONG).show();
                            }
                            else if (response.length() == limit) {

                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String brandName = productName1.getString("brandName");
                                    ez_brandList.add(brandName);
                                }
                                offset = (limit * count) + limit;
                                count++;
                                requestEzoneBrand(offset, limit);

                            } else if (response.length() < limit)
                            {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String brandName = productName1.getString("brandName");
                                    ez_brandList.add(brandName);
                                }
                                if (getIntent().getStringExtra("checkfrom").equals("ezoneSales") || getIntent().getStringExtra("checkfrom").equals("pvaAnalysis")) {
                                    rel_ez_process_filter.setVisibility(View.GONE);
                                } else {
                                    rel_ez_process_filter.setVisibility(View.GONE);
                                    if (prod_listDataHeader.get(4).equals("MC")) {
                                        rel_ez_process_filter.setVisibility(View.VISIBLE);
                                        offset = 0;
                                        limit = 100;
                                        count = 0;
                                        ez_prod_level = 5;
                                        requestEzoneBrandPlanClass(offset, limit);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            rel_ez_process_filter.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                        finally
                        {
                            if(response.equals("") || response == null || response.length()==0) {

                                if (getIntent().getStringExtra("checkfrom").equals("ezoneSales") || getIntent().getStringExtra("checkfrom").equals("ezonepvaAnalysis")) {
                                    rel_ez_process_filter.setVisibility(View.GONE);
                                } else {
                                    rel_ez_process_filter.setVisibility(View.GONE);
                                    if (prod_listDataHeader.get(4).equals("MC")) {
                                        rel_ez_process_filter.setVisibility(View.VISIBLE);
                                        offset = 0;
                                        limit = 100;
                                        count = 0;
                                        ez_prod_level = 5;
                                        requestEzoneBrandPlanClass(offset, limit);
                                    }
                                }
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        rel_ez_process_filter.setVisibility(View.GONE);
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


    //Brand Plan Class List
    public void requestEzoneBrandPlanClass(int offsetvalue1, int limit1) {

        String mc_url = "";
//        if (getIntent().getStringExtra("checkfrom").equals("ezoneSales") || getIntent().getStringExtra("checkfrom").equals("ezonepvaAnalysis")) {
//            //with geolevel2code field
            mc_url = ConstsCore.web_url + "/v1/display/salesanalysishierarchyNew/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + ez_prod_level+ "&geoLevel2Code="+geoLevel2Code + "&lobId="+ lobId;
//        }
//        else
//        {
//            //without geoLevel2code field
//            mc_url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + ez_prod_level;
//        }
            Log.e("mc_url :", "" + mc_url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, mc_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.e("mc response :", "" + response);
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                rel_ez_process_filter.setVisibility(View.GONE);
                                Toast.makeText(EzoneSalesFilter.this, "no data found in mc", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {
                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String brandClass = productName1.getString("brandPlanClass");
                                    ez_mcList.add(brandClass);
                                }

                                offset = (limit * count) + limit;
                                count++;
                                requestEzoneBrandPlanClass(offset, limit);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String brandClass = productName1.getString("brandPlanClass");
                                    ez_mcList.add(brandClass);
                                }
//                                txt_ez_location.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.uplist, 0);
//                                txt_ez_prod.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.uplist, 0);
//                                lin_ez_locatn.setVisibility(View.VISIBLE);
//                                lin_ez_prod.setVisibility(View.VISIBLE);
                                rel_ez_process_filter.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            rel_ez_process_filter.setVisibility(View.GONE);
                            Reusable_Functions.hDialog();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        rel_ez_process_filter.setVisibility(View.GONE);
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