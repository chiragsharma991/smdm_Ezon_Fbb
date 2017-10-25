package apsupportapp.aperotechnologies.com.designapp.SalesAnalysis;

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
import apsupportapp.aperotechnologies.com.designapp.FloorAvailability.FloorAvailabilityActivity;
import apsupportapp.aperotechnologies.com.designapp.FreshnessIndex.FreshnessIndexActivity;
import apsupportapp.aperotechnologies.com.designapp.OptionEfficiency.OptionEfficiencyActivity;
import apsupportapp.aperotechnologies.com.designapp.PvaSalesAnalysis.SalesPvAActivity;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.SellThruExceptions.SaleThruInventory;
import apsupportapp.aperotechnologies.com.designapp.SkewedSize.SkewedSizesActivity;
import apsupportapp.aperotechnologies.com.designapp.StockAgeing.StockAgeingActivity;
import apsupportapp.aperotechnologies.com.designapp.TargetStockExceptions.TargetStockExceptionActivity;
import apsupportapp.aperotechnologies.com.designapp.TopOptionCutSize.TopFullCut;
import apsupportapp.aperotechnologies.com.designapp.VisualAssortmentSwipe.VisualAssortmentActivity;
import apsupportapp.aperotechnologies.com.designapp.model.FreshnessIndex_Ez_Model;

/**
 * Created by pamrutkar on 05/06/17.
 */
public class SalesAnalysisFilter extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rel_an_sfilter_back, rel_an_sfilter_done;
    ArrayList<String> loc_listDataHeader, prod_listDataHeader;
    HashMap<String, List<String>> loc_listDataChild, prod_listDataChild;
    private EditText et_an_search;
    public static ExpandableListView explv_an_locatn, explv_an_prod;
    Context context = this;
    public static RelativeLayout rel_an_process_filter;
    private String str_filter_location = "NO", str_filter_prod = "NO";
    static SalesAnalysisProductAdapter sanalysis_prod_list_adapter;
    static SalesAnalysisLocationAdapter sanalysis_locatn_list_adapter;
    int offset = 0, limit = 100, count = 0;
    public int an_level_filter = 1, prod_level = 1;
    String userId, bearertoken, geoLevel2Code, lobId;

    private Intent intent;
    SharedPreferences sharedPreferences;
    RequestQueue queue;
    static String an_str_checkFrom;
    public int filter_level = 0;
    private StringBuilder build = new StringBuilder();


    static List<String> an_storeList, an_deptList, an_categryList, an_classList, an_brandList, an_mcList;
    private String[] hierarchyList;
    // git 09-06-17


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_sfilter);
        getSupportActionBar().hide();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        context = this;
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLevel2Code = sharedPreferences.getString("concept", "");
        lobId = sharedPreferences.getString("lobid", "");
        String hierarchyLevels = sharedPreferences.getString("hierarchyLevels", "");
        hierarchyList = hierarchyLevels.split(",");
        for (int i = 0; i <hierarchyList.length ; i++) {
            hierarchyList[i]=hierarchyList[i].trim();
        }
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
//        an_regionList = new ArrayList<String>();
        an_storeList = new ArrayList<String>();
        an_deptList = new ArrayList<String>();
        an_categryList = new ArrayList<String>();
        an_classList = new ArrayList<String>();
        an_brandList = new ArrayList<String>();
        an_mcList = new ArrayList<String>();
        an_str_checkFrom = getIntent().getStringExtra("checkfrom");
        initialise_ui();
        prepareData();
        sanalysis_locatn_list_adapter = new SalesAnalysisLocationAdapter(this, loc_listDataHeader, loc_listDataChild, explv_an_locatn, sanalysis_locatn_list_adapter);
        explv_an_locatn.setAdapter(sanalysis_locatn_list_adapter);
        sanalysis_prod_list_adapter = new SalesAnalysisProductAdapter(this, prod_listDataHeader, prod_listDataChild, explv_an_prod, sanalysis_prod_list_adapter);
        explv_an_prod.setAdapter(sanalysis_prod_list_adapter);

        explv_an_locatn.setNestedScrollingEnabled(true);
        explv_an_prod.setNestedScrollingEnabled(true);

        explv_an_locatn.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                setListViewHeight1(parent, groupPosition);
                return false;
            }
        });
        explv_an_prod.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                setListViewHeight(parent, groupPosition);
                return false;
            }
        });
        et_an_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                Boolean handled = false;
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_NEXT) || (actionId == EditorInfo.IME_ACTION_NONE) || (actionId == EditorInfo.IME_ACTION_SEARCH)) {
                    et_an_search.clearFocus();
                    InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputManager != null) {
                        inputManager.hideSoftInputFromWindow(et_an_search.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    handled = true;
                }
                return handled;
            }
        });

        et_an_search.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(et_an_search.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                s = et_an_search.getText().toString();
                Log.e("s :", "" + s);
                sanalysis_locatn_list_adapter.filterData(s.toString());
                sanalysis_prod_list_adapter.filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
    }

    private void setListViewHeight1(ExpandableListView listView, int group) {
        SalesAnalysisLocationAdapter listAdapter = (SalesAnalysisLocationAdapter) listView.getExpandableListAdapter();
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
        SalesAnalysisProductAdapter listAdapter = (SalesAnalysisProductAdapter) listView.getExpandableListAdapter();
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

        prod_listDataHeader.add(hierarchyList[0]);
        prod_listDataHeader.add(hierarchyList[1]);
        prod_listDataHeader.add(hierarchyList[2]);
        prod_listDataHeader.add(hierarchyList[3]);
        prod_listDataHeader.add(hierarchyList[4]);
        loc_listDataHeader.add("Store");

        if (Reusable_Functions.chkStatus(SalesAnalysisFilter.this))
        {
            if (prod_listDataHeader.get(0).equals(hierarchyList[0])) {
                rel_an_process_filter.setVisibility(View.VISIBLE);
                offset = 0;
                limit = 100;
                count = 0;
                an_level_filter = 1;
                requestDepartment(offset, limit);
            }
        }
        else
        {
            Toast.makeText(SalesAnalysisFilter.this, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
        prod_listDataChild.put(prod_listDataHeader.get(0), an_deptList); // Header, Child data
        prod_listDataChild.put(prod_listDataHeader.get(1), an_categryList);
        prod_listDataChild.put(prod_listDataHeader.get(2), an_classList); // Header, Child data
        prod_listDataChild.put(prod_listDataHeader.get(3), an_brandList);
        prod_listDataChild.put(prod_listDataHeader.get(4), an_mcList); // Header, Child data
        loc_listDataChild.put(loc_listDataHeader.get(0), an_storeList);
        rel_an_process_filter.setVisibility(View.GONE);
    }

    private void initialise_ui()
    {
        rel_an_sfilter_back = (RelativeLayout) findViewById(R.id.rel_an_sfilter_back);
        rel_an_sfilter_done = (RelativeLayout) findViewById(R.id.rel_an_sfilter_done);
        rel_an_process_filter = (RelativeLayout) findViewById(R.id.rel_an_process_filter);
        et_an_search = (EditText) findViewById(R.id.et_an_search);
        et_an_search.setSingleLine(true);
        explv_an_locatn = (ExpandableListView) findViewById(R.id.explv_an_location);
        explv_an_locatn.setTextFilterEnabled(true);
        explv_an_locatn.setDivider(getResources().getDrawable(R.color.grey));
        explv_an_locatn.setDividerHeight(2);
        explv_an_prod = (ExpandableListView) findViewById(R.id.explv_an_product);
        explv_an_prod.setTextFilterEnabled(true);
        explv_an_prod.setDivider(getResources().getDrawable(R.color.grey));
        explv_an_prod.setDividerHeight(2);
        rel_an_sfilter_done.setOnClickListener(this);
        rel_an_sfilter_back.setOnClickListener(this);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_an_sfilter_back:
                onBackPressed();
                break;
            case R.id.rel_an_sfilter_done:
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                //Send selected hierarchy level to selected activity
                selectbuild();
                if (getIntent().getStringExtra("checkfrom").equals("SalesAnalysis"))
                {
                    intent = new Intent(SalesAnalysisFilter.this, SalesAnalysisActivity1.class);
                    if (build.length() != 0)
                    {
                        SalesAnalysisActivity1.SalesAnalysisActivity.finish();
                    }
                    callback(build);

                }
                else if (getIntent().getStringExtra("checkfrom").equals("pvaAnalysis"))
                {
                    intent = new Intent(SalesAnalysisFilter.this, SalesPvAActivity.class);

                    if (build.length() != 0) {
                        SalesPvAActivity.Sales_Pva_Activity.finish();
                    }
                    callback(build);

                }
                else if (getIntent().getStringExtra("checkfrom").equals("freshnessIndex")) {
                    intent = new Intent(SalesAnalysisFilter.this, FreshnessIndexActivity.class);

                    if (build.length() != 0) {
                        FreshnessIndexActivity.freshness_Index.finish();
                    }
                    callback(build);

                }
                else if (getIntent().getStringExtra("checkfrom").equals("optionEfficiency"))
                {
                    intent = new Intent(SalesAnalysisFilter.this, OptionEfficiencyActivity.class);

                    if (build.length() != 0) {
                        OptionEfficiencyActivity.option_Efficiency.finish();
                    }
                    callback(build);

                }
                else if (getIntent().getStringExtra("checkfrom").equals("bestPerformers"))
                {
                    intent = new Intent(SalesAnalysisFilter.this, BestPerformerInventory.class);

                    if (build.length() != 0) {
                        BestPerformerInventory.bestperoformer.finish();
                    }
                    callback(build);

                }
                else if (getIntent().getStringExtra("checkfrom").equals("skewedSize"))
                {
                    intent = new Intent(SalesAnalysisFilter.this, SkewedSizesActivity.class);

                    if (build.length() != 0) {
                        SkewedSizesActivity.SkewedSizes.finish();
                    }
                    callback(build);
                }
                else if (getIntent().getStringExtra("checkfrom").equals("sellThruExceptions"))
                {
                    intent = new Intent(SalesAnalysisFilter.this, SaleThruInventory.class);

                    if (build.length() != 0) {
                        SaleThruInventory.saleThru.finish();
                    }
                    callback(build);
                }
                else if (getIntent().getStringExtra("checkfrom").equals("stockAgeing"))
                {
                    intent = new Intent(SalesAnalysisFilter.this, StockAgeingActivity.class);

                    if (build.length() != 0) {
                        StockAgeingActivity.stockAgeing.finish();
                    }
                    callback(build);
                }
                else if (getIntent().getStringExtra("checkfrom").equals("floorAvailability"))
                {
                    intent = new Intent(SalesAnalysisFilter.this, FloorAvailabilityActivity.class);

                    if (build.length() != 0) {
                        FloorAvailabilityActivity.floorAvailability.finish();
                    }
                    callback(build);
                }
                else if (getIntent().getStringExtra("checkfrom").equals("targetstockexception"))
                {
                    intent = new Intent(SalesAnalysisFilter.this, TargetStockExceptionActivity.class);

                    if (build.length() != 0) {
                        TargetStockExceptionActivity.targetStockException.finish();
                    }
                    callback(build);
                }
                else if (getIntent().getStringExtra("checkfrom").equals("visualAssort"))
                {
                    intent = new Intent(SalesAnalysisFilter.this, VisualAssortmentActivity.class);

                    if (build.length() != 0) {
                        VisualAssortmentActivity.Visual_Assortment_Activity.finish();
                    }
                    callback(build);
                }
                break;
        }
    }

    private void selectbuild() {


        if (SalesAnalysisProductAdapter.an_dept_text.length() != 0)

        {
            String deptmnt = SalesAnalysisProductAdapter.an_dept_text.replace("%", "%25");
            String updateDept = deptmnt.replace(" ", "%20").replace("&", "%26");
            String Department;
            Department = "department=" + updateDept;
            build.append("&");
            build.append(Department.replace(",$", ""));
        }
        if (SalesAnalysisProductAdapter.an_categry_text.length() != 0) {
            String categry = SalesAnalysisProductAdapter.an_categry_text.replace("%", "%25");
            String updateCategory = categry.replace(" ", "%20").replace("&", "%26");
            String Categary = "category=" + updateCategory;
            build.append("&");
            build.append(Categary.replace(",$", ""));
        }

        if (SalesAnalysisProductAdapter.an_class_text.length() != 0) {
            String plancls = SalesAnalysisProductAdapter.an_class_text.replace("%", "%25");
            String updatePlanClass = plancls.replace(" ", "%20").replace("&", "%26");
            String planclass = "class=" + updatePlanClass;
            build.append("&");
            build.append(planclass.replace(",$", ""));
        }
        if (SalesAnalysisProductAdapter.an_brand_text.length() != 0) {
            String brand = SalesAnalysisProductAdapter.an_brand_text.replace("%", "%25");
            String updateBrand = brand.replace(" ", "%20").replace("&", "%26");
            String Brand = "brand=" + updateBrand;
            build.append("&");
            build.append(Brand.replace(",$", ""));
        }
        if (SalesAnalysisProductAdapter.an_brandcls_text.length() != 0) {
            String brandcls = SalesAnalysisProductAdapter.an_brandcls_text.replace("%", "%25");
            String updateBrandCls = brandcls.replace(" ", "%20").replace("&", "%26");
            String Brandclass = "brandclass=" + updateBrandCls;
            build.append("&");
            build.append(Brandclass.replace(",$", ""));
        }
        if (SalesAnalysisLocationAdapter.an_store_str.length() != 0) {
            String store = SalesAnalysisLocationAdapter.an_store_str;
            //  String updateStore = store.replace(" ", "%20").replace("&", "%26");
            String Store;
            Store = "storeCode=" + store;
            build.append("&");
            build.append(Store.replace(",$", ""));
        }


    }

    private void callback(StringBuilder build) {
        if (build.length() == 0) {
            Toast.makeText(context, "Please select value...", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            callFilterLevelSales();
            intent.putExtra("selectedStringVal", build.toString());
            Log.e("TAG", "callback:  selectedStringVal" + build.toString());
            intent.putExtra("selectedlevelVal", filter_level);
            Log.e("TAG", "callback:  selectedlevelVal" + filter_level);
        }
        startActivity(intent);
        SalesAnalysisProductAdapter.an_dept_text = "";
        SalesAnalysisProductAdapter.an_categry_text = "";
        SalesAnalysisProductAdapter.an_class_text = "";
        SalesAnalysisProductAdapter.an_brand_text = "";
        SalesAnalysisProductAdapter.an_brandcls_text = "";
        SalesAnalysisLocationAdapter.an_store_str = "";
        an_str_checkFrom = "";
        finish();
    }

    private void callFilterLevelSales() {

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


        }
    }

    @Override
    public void onBackPressed() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        finish();
    }


    //------------------------------------API Declaration--------------------------------------//


    // Department List
    public void requestDepartment(int offsetvalue1, int limit1)
    {
        String dept_url = "";
        dept_url = ConstsCore.web_url + "/v1/display/salesanalysishierarchyNew/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + prod_level + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;

        Log.e("dept_url :", "" + dept_url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, dept_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        Log.e("dept response :", "" + response);
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                rel_an_process_filter.setVisibility(View.GONE);
                                Toast.makeText(SalesAnalysisFilter.this, "no data found in department", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {
                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String plandept = productName1.getString("planDept");
                                    an_deptList.add(plandept);
                                }
                                offset = (limit * count) + limit;
                                count++;
                                requestDepartment(offset, limit);
                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String plandept = productName1.getString("planDept");
                                    an_deptList.add(plandept);
                                }
                                rel_an_process_filter.setVisibility(View.GONE);
                                if (prod_listDataHeader.get(1).equals(hierarchyList[1])) {
                                    rel_an_process_filter.setVisibility(View.VISIBLE);
                                    offset = 0;
                                    limit = 100;
                                    count = 0;
                                    prod_level = 2;
                                    requestCategory(offset, limit);
                                }
                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            rel_an_process_filter.setVisibility(View.GONE);
                            e.printStackTrace();
                        } finally {
                            if (response.equals("") || response == null || response.length() == 0) {

                                if (prod_listDataHeader.get(1).equals(hierarchyList[1])) {
                                    rel_an_process_filter.setVisibility(View.VISIBLE);
                                    offset = 0;
                                    limit = 100;
                                    count = 0;
                                    prod_level = 2;
                                    requestCategory(offset, limit);
                                }
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        rel_an_process_filter.setVisibility(View.GONE);
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
    public void requestCategory(int offsetvalue1, int limit1) {
        String category_url = "";

        category_url = ConstsCore.web_url + "/v1/display/salesanalysishierarchyNew/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + prod_level + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;
        Log.e("categry_url :", "" + category_url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, category_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        Log.e("categry response :", "" + response);
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                rel_an_process_filter.setVisibility(View.GONE);
                                Toast.makeText(SalesAnalysisFilter.this, "no data found in category", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {

                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String planCategory = productName1.getString("planCategory");
                                    an_categryList.add(planCategory);
                                }
                                offset = (limit * count) + limit;
                                count++;
                                requestCategory(offset, limit);
                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String planCategory = productName1.getString("planCategory");
                                    an_categryList.add(planCategory);
                                }
                                rel_an_process_filter.setVisibility(View.GONE);
                                if (prod_listDataHeader.get(2).equals(hierarchyList[2])) {
                                    rel_an_process_filter.setVisibility(View.VISIBLE);
                                    offset = 0;
                                    limit = 100;
                                    count = 0;
                                    prod_level = 3;
                                    requestPlanClass(offset, limit);
                                }
                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            rel_an_process_filter.setVisibility(View.GONE);
                            e.printStackTrace();
                        } finally {
                            if (response.equals("") || response == null || response.length() == 0) {

                                if (prod_listDataHeader.get(2).equals(hierarchyList[2])) {
                                    rel_an_process_filter.setVisibility(View.VISIBLE);
                                    offset = 0;
                                    limit = 100;
                                    count = 0;
                                    prod_level = 3;
                                    requestPlanClass(offset, limit);
                                }
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        rel_an_process_filter.setVisibility(View.GONE);
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
    public void requestPlanClass(int offsetvalue1, int limit1) {
        String class_url = "";

        class_url = ConstsCore.web_url + "/v1/display/salesanalysishierarchyNew/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + prod_level + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;

        Log.e("class_url :", "" + class_url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, class_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        Log.e("class response :", "" + response);
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                rel_an_process_filter.setVisibility(View.GONE);
                                Toast.makeText(SalesAnalysisFilter.this, "no data found in class", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {
                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String planClass = productName1.getString("planClass");
                                    an_classList.add(planClass);
                                }
                                offset = (limit * count) + limit;
                                count++;
                                requestPlanClass(offset, limit);
                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String planClass = productName1.getString("planClass");
                                    an_classList.add(planClass);
                                }
                                rel_an_process_filter.setVisibility(View.GONE);

                                if (prod_listDataHeader.get(3).equals(hierarchyList[3])) {
                                    rel_an_process_filter.setVisibility(View.VISIBLE);
                                    offset = 0;
                                    limit = 100;
                                    count = 0;
                                    prod_level = 4;
                                    requestBrand(offset, limit);
                                }
                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            rel_an_process_filter.setVisibility(View.GONE);
                            e.printStackTrace();
                        } finally {
                            if (response.equals("") || response == null || response.length() == 0) {

                                if (prod_listDataHeader.get(3).equals(hierarchyList[3])) {
                                    rel_an_process_filter.setVisibility(View.VISIBLE);
                                    offset = 0;
                                    limit = 100;
                                    count = 0;
                                    prod_level = 4;
                                    requestBrand(offset, limit);
                                }
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        rel_an_process_filter.setVisibility(View.GONE);
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
    public void requestBrand(int offsetvalue1, int limit1) {
        String brand_url = "";

        brand_url = ConstsCore.web_url + "/v1/display/salesanalysishierarchyNew/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + prod_level + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;

        Log.e("brand_url :", "" + brand_url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, brand_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        Log.e("brand response :", "" + response);
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                rel_an_process_filter.setVisibility(View.GONE);
                                Toast.makeText(SalesAnalysisFilter.this, "no data found in brand", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {

                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String brandName = productName1.getString("brandName");
                                    an_brandList.add(brandName);
                                }
                                offset = (limit * count) + limit;
                                count++;
                                requestBrand(offset, limit);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String brandName = productName1.getString("brandName");
                                    an_brandList.add(brandName);
                                }
                                rel_an_process_filter.setVisibility(View.GONE);
                                if (prod_listDataHeader.get(4).equals(hierarchyList[4])) {
                                    rel_an_process_filter.setVisibility(View.VISIBLE);
                                    offset = 0;
                                    limit = 100;
                                    count = 0;
                                    prod_level = 5;
                                    requestBrandPlanClass(offset, limit);
                                }

                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            rel_an_process_filter.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                        finally {
                            if (response.equals("") || response == null || response.length() == 0) {

                                rel_an_process_filter.setVisibility(View.GONE);
                                if (prod_listDataHeader.get(4).equals(hierarchyList[4])) {
                                    rel_an_process_filter.setVisibility(View.VISIBLE);
                                    offset = 0;
                                    limit = 100;
                                    count = 0;
                                    prod_level = 5;
                                    requestBrandPlanClass(offset, limit);
                                }

                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        rel_an_process_filter.setVisibility(View.GONE);
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
    public void requestBrandPlanClass(int offsetvalue1, int limit1) {

        String mc_url = "";
        mc_url = ConstsCore.web_url + "/v1/display/salesanalysishierarchyNew/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + prod_level + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;
        Log.e("mc_url :", "" + mc_url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, mc_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.e("mc response :", "" + response);
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                rel_an_process_filter.setVisibility(View.GONE);
                                Toast.makeText(SalesAnalysisFilter.this, "no data found in brand class", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {
                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String brandClass = productName1.getString("brandPlanClass");
                                    an_mcList.add(brandClass);
                                }

                                offset = (limit * count) + limit;
                                count++;
                                requestBrandPlanClass(offset, limit);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String brandClass = productName1.getString("brandPlanClass");
                                    an_mcList.add(brandClass);
                                }

                                rel_an_process_filter.setVisibility(View.GONE);
                                if (loc_listDataHeader.get(0).equals("Store")) {
                                    rel_an_process_filter.setVisibility(View.VISIBLE);
                                    offset = 0;
                                    limit = 100;
                                    count = 0;
                                    an_level_filter = 3;
                                    requestStore(offset, limit);
                                }
                            }
                        } catch (Exception e) {
                            rel_an_process_filter.setVisibility(View.GONE);
                            Reusable_Functions.hDialog();
                            e.printStackTrace();
                        }
                        finally {
                            if (response.equals("") || response == null || response.length() == 0) {

                                rel_an_process_filter.setVisibility(View.GONE);
                                if (loc_listDataHeader.get(0).equals("Store")) {
                                    rel_an_process_filter.setVisibility(View.VISIBLE);
                                    offset = 0;
                                    limit = 100;
                                    count = 0;
                                    an_level_filter = 3;
                                    requestStore(offset, limit);
                                }

                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        rel_an_process_filter.setVisibility(View.GONE);
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
    private void requestStore(int offsetval, int limitval) {
        String store_url = "";
        store_url = ConstsCore.web_url + "/v1/display/storehierarchyEZNew/" + userId + "?offset=" + offset + "&limit=" + limit + "&level=" + an_level_filter + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;
        Log.e("store url :", "" + store_url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, store_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response)
                    {
//                        Log.e("store response :", "" + response);
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                rel_an_process_filter.setVisibility(View.GONE);
                                Toast.makeText(SalesAnalysisFilter.this, "no data found in store ", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {
                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String store = productName1.getString("descEz");
                                    an_storeList.add(store);
                                }
                                offset = (limit * count) + limit;
                                count++;
                                requestStore(offset, limit);
                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String store = productName1.getString("descEz");
                                    an_storeList.add(store);
                                }
                                rel_an_process_filter.setVisibility(View.GONE);

                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            rel_an_process_filter.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        rel_an_process_filter.setVisibility(View.GONE);
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