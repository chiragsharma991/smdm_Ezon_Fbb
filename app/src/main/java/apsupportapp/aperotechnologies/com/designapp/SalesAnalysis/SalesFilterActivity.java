package apsupportapp.aperotechnologies.com.designapp.SalesAnalysis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
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
import apsupportapp.aperotechnologies.com.designapp.TopOptionCutSize.TopFullCut;
import apsupportapp.aperotechnologies.com.designapp.VisualAssortmentSwipe.VisualAssortmentActivity;


public class SalesFilterActivity extends Activity {

    RelativeLayout btnS_Filterback, btnS_Done;
    static SalesFilterExpandableList listAdapter;
    public static ExpandableListView pfilter_list;
    ArrayList<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    int offsetvalue = 0, limit = 100, count = 0;
    public static int level_filter = 1;
    String userId, bearertoken,geoLevel2Code;
    SharedPreferences sharedPreferences;
    RequestQueue queue;
    Context context;
    private boolean process_flag_dept = false, process_flag_cat = false, process_flag_class = false, process_flag_brand = false, process_flag_mc = false;
    String TAG = "SalesFilterActivity";
    static List<String> subdept, subCategory, subPlanClass, subBrandnm, subBrandPlanClass;
    public static String plandeptName;
    EditText editTextSearch;
    public static List<Integer> groupImages;
    public static RelativeLayout processbar;
    private Intent intent;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salespva_filter);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        context = this;
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLevel2Code = sharedPreferences.getString("geoLevel2Code","");
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        SalesFilterExpandableList.text1 = "";
        SalesFilterExpandableList.text2 = "";
        SalesFilterExpandableList.text3 = "";
        SalesFilterExpandableList.text4 = "";
        SalesFilterExpandableList.text5 = "";
        subdept = new ArrayList<String>();
        subCategory = new ArrayList<String>();
        subBrandnm = new ArrayList<String>();
        subPlanClass = new ArrayList<String>();
        subBrandPlanClass = new ArrayList<String>();

        btnS_Filterback = (RelativeLayout) findViewById(R.id.imageBtnSFilterBack);
        btnS_Done = (RelativeLayout) findViewById(R.id.imageBtnSalesFilterDone);
        processbar = (RelativeLayout) findViewById(R.id.process_filter);
        pfilter_list = (ExpandableListView) findViewById(R.id.expandableListView_subdept);
        pfilter_list.setTextFilterEnabled(true);
        pfilter_list.setDivider(getResources().getDrawable(R.color.grey));
        pfilter_list.setDividerHeight(2);
        prepareListData();

        listAdapter = new SalesFilterExpandableList(this, listDataHeader, listDataChild, pfilter_list, listAdapter);

        // setting list adapter
        pfilter_list.setAdapter(listAdapter);

        //  Edit Text Search
        editTextSearch = (EditText) findViewById(R.id.editSearchSales);
        editTextSearch.setSingleLine(true);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchData = editTextSearch.getText().toString();

                InputMethodManager inputManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(editTextSearch.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                listAdapter.filterData(editTextSearch.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });


        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                Boolean handled = false;
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_NEXT) || (actionId == EditorInfo.IME_ACTION_NONE) || (actionId == EditorInfo.IME_ACTION_SEARCH)) {
                    editTextSearch.clearFocus();
                    InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputManager != null) {
                        inputManager.hideSoftInputFromWindow(editTextSearch.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    handled = true;
                }
                return handled;
            }
        });


        btnS_Filterback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });


        btnS_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                StringBuilder build = new StringBuilder();
                //Send selected hierarchy level sales activity
                if (SalesFilterExpandableList.text1.length() != 0)
                {
                    String deptmnt = SalesFilterExpandableList.text1.replace("%", "%25");
                    String updateDept = deptmnt.replace(" ", "%20").replace("&", "%26");
                    String Department;

                    if (getIntent().getStringExtra("checkfrom").equals("SalesAnalysis")) {
                        Department = "department=" + updateDept;
                    } else if (getIntent().getStringExtra("checkfrom").equals("pvaAnalysis")) {
                        Department = "department=" + updateDept;
                    } else {
                        Department = "dept=" + updateDept;
                    }
                    build.append("&");
                    level_filter = 2;
                    build.append(Department.replace(",$", ""));

                }

                if (SalesFilterExpandableList.text2.length() != 0) {
                    String categry = SalesFilterExpandableList.text2.replace("%", "%25");
                    String updateCategory = categry.replace(" ", "%20").replace("&", "%26");
                    String Categary = "category=" + updateCategory;
                    build.append("&");
                    level_filter = 3;
                    build.append(Categary.replace(",$", ""));

                }

                if (SalesFilterExpandableList.text3.length() != 0)
                {
                    String plancls = SalesFilterExpandableList.text3.replace("%", "%25");
                    String updatePlanClass = plancls.replace(" ", "%20").replace("&", "%26");
                    String planclass = "class=" + updatePlanClass;
                    build.append("&");
                    level_filter = 4;
                    build.append(planclass.replace(",$", ""));

                }

                if (SalesFilterExpandableList.text4.length() != 0) {
                    String brand = SalesFilterExpandableList.text4.replace("%", "%25");
                    String updateBrand = brand.replace(" ", "%20").replace("&", "%26");
                    String Brand = "brand=" + updateBrand;
                    build.append("&");
                    level_filter = 5;
                    build.append(Brand.replace(",$", ""));

                }

                if (SalesFilterExpandableList.text5.length() != 0)
                {
                    String brandcls = SalesFilterExpandableList.text5.replace("%", "%25");
                    String updateBrandCls = brandcls.replace(" ", "%20").replace("&", "%26");
                    String Brandclass = "brandclass=" + updateBrandCls;
                    build.append("&");
                    if (getIntent().getStringExtra("checkfrom").equals("freshnessIndex") || getIntent().getStringExtra("checkfrom").equals("optionEfficiency")) {
                        level_filter = 5;
                    }
                    else
                    {
                        level_filter = 6;
                    }
                    build.append(Brandclass.replace(",$", ""));

                }
                if (getIntent().getStringExtra("checkfrom").equals("SalesAnalysis"))
                {
                    intent = new Intent(SalesFilterActivity.this, SalesAnalysisActivity1.class);
                    if (build.length() != 0)
                    {
                        SalesAnalysisActivity1.SalesAnalysisActivity.finish();
                    }
                    callback(build);


                } else if (getIntent().getStringExtra("checkfrom").equals("pvaAnalysis"))
                {
                    intent = new Intent(SalesFilterActivity.this, SalesPvAActivity.class);
                    if (build.length() != 0) {
                        SalesPvAActivity.Sales_Pva_Activity.finish();
                    }
                    callback(build);


                } else if (getIntent().getStringExtra("checkfrom").equals("TopFullCut")) {
                    intent = new Intent(SalesFilterActivity.this, TopFullCut.class);
                    if (build.length() != 0) {
                        TopFullCut.topFullcut.finish();
                    }
                    callback(build);

                } else if (getIntent().getStringExtra("checkfrom").equals("visualAssort")) {
                    intent = new Intent(SalesFilterActivity.this, VisualAssortmentActivity.class);

                    if (build.length() != 0) {
                        VisualAssortmentActivity.Visual_Assortment_Activity.finish();
                    }
                    callback(build);

                } else if (getIntent().getStringExtra("checkfrom").equals("skewedSize")) {
                    intent = new Intent(SalesFilterActivity.this, SkewedSizesActivity.class);
                    if (build.length() != 0)
                    {
                        SkewedSizesActivity.SkewedSizes.finish();
                    }
                    callback(build);
                } else if (getIntent().getStringExtra("checkfrom").equals("freshnessIndex")) {
                    intent = new Intent(SalesFilterActivity.this, FreshnessIndexActivity.class);
                    if (build.length() != 0) {
                        FreshnessIndexActivity.freshness_Index.finish();
                    }
                    callback(build);
                } else if (getIntent().getStringExtra("checkfrom").equals("optionEfficiency")) {
                    intent = new Intent(SalesFilterActivity.this, OptionEfficiencyActivity.class);
                    if (build.length() != 0) {
                        OptionEfficiencyActivity.option_Efficiency.finish();
                    }
                    callback(build);
                } else if (getIntent().getStringExtra("checkfrom").equals("bestPerformers")) {
                    intent = new Intent(SalesFilterActivity.this, BestPerformerInventory.class);
                    if (build.length() != 0) {
                        BestPerformerInventory.bestperoformer.finish();
                    }
                    callback(build);


                } else if (getIntent().getStringExtra("checkfrom").equals("stockAgeing")) {
                    intent = new Intent(SalesFilterActivity.this, StockAgeingActivity.class);
                    if (build.length() != 0) {
                        StockAgeingActivity.stockAgeing.finish();
                    }
                    callback(build);


                } else if (getIntent().getStringExtra("checkfrom").equals("floorAvailability")) {
                    intent = new Intent(SalesFilterActivity.this, FloorAvailabilityActivity.class);
                    if (build.length() != 0) {
                        FloorAvailabilityActivity.floorAvailability.finish();
                    }
                    callback(build);


                } else if (getIntent().getStringExtra("checkfrom").equals("sellThruExceptions")) {
                    intent = new Intent(SalesFilterActivity.this, SaleThruInventory.class);
                    if (build.length() != 0) {
                        SaleThruInventory.saleThru.finish();
                    }
                    callback(build);


                }
            }
        });
    }

    private void callback(StringBuilder build)
    {

        if (build.length() == 0) {
            Toast.makeText(context, "Please select value...", Toast.LENGTH_SHORT).show();
            return;
        } else {
            intent.putExtra("selectedDept", build.toString());
        }
        startActivity(intent);
        SalesFilterExpandableList.text1 = "";
        SalesFilterExpandableList.text2 = "";
        SalesFilterExpandableList.text3 = "";
        SalesFilterExpandableList.text4 = "";
        SalesFilterExpandableList.text5 = "";
        finish();
    }


    public void prepareListData() {

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding group name data
        listDataHeader.add("Department");
        listDataHeader.add("Category");
        listDataHeader.add("Class");
        listDataHeader.add("Brand");
        listDataHeader.add("Brand Class");
        if (Reusable_Functions.chkStatus(SalesFilterActivity.this))
        {
            if (listDataHeader.get(0).equals("Department"))
            {
                processbar.setVisibility(View.VISIBLE);
                offsetvalue = 0;
                limit = 100;
                count = 0;
                process_flag_dept = false;
                level_filter = 1;
                requestDeptAPI(offsetvalue, limit);
            }
        }
        else
        {
            Toast.makeText(SalesFilterActivity.this, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
        listDataChild.put(listDataHeader.get(0), subdept);
        listDataChild.put(listDataHeader.get(1), subCategory);
        listDataChild.put(listDataHeader.get(2), subPlanClass);
        listDataChild.put(listDataHeader.get(3), subBrandnm);
        listDataChild.put(listDataHeader.get(4), subBrandPlanClass);
        Reusable_Functions.hDialog();
    }

    // Department List
    public void requestDeptAPI(int offsetvalue1, int limit1)
    {
        String url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + level_filter +"&geoLevel2Code="+geoLevel2Code;
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                processbar.setVisibility(View.GONE);
                                Toast.makeText(SalesFilterActivity.this, "no data found in department", Toast.LENGTH_SHORT).show();
                            } else if (response.length() == limit) {
                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String plandept = productName1.getString("planDept");
                                    subdept.add(plandept);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestDeptAPI(offsetvalue, limit);
                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String planDept = productName1.getString("planDept");
                                    subdept.add(planDept);
                                }
                                process_flag_dept = true;
                                processbar.setVisibility(View.GONE);
                                if (listDataHeader.get(1).equals("Category")) {
                                    processbar.setVisibility(View.VISIBLE);
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    process_flag_cat = false;
                                    level_filter = 2;
                                    requestCategoryAPI(offsetvalue, limit);
                                }
                            }
                        } catch (Exception e)
                        {

                            Log.e(TAG, "onResponse1: "+e );
                            processbar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                        finally
                        {
                            if (listDataHeader.get(1).equals("Category")) {
                                processbar.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                process_flag_cat = false;
                                level_filter = 2;
                                requestCategoryAPI(offsetvalue, limit);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
    public void requestCategoryAPI(int offsetvalue1, int limit1)
    {
        String url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + level_filter +"&geoLevel2Code="+geoLevel2Code;
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(SalesFilterActivity.this, "no data found in Category", Toast.LENGTH_SHORT).show();
                                processbar.setVisibility(View.GONE);

                            } else if (response.length() == limit) {

                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String planCategory = productName1.getString("planCategory");
                                    subCategory.add(planCategory);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestCategoryAPI(offsetvalue, limit);
                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);

                                    String planCategory = productName1.getString("planCategory");
                                    subCategory.add(planCategory);

                                }
                                process_flag_cat = true;
                                processbar.setVisibility(View.GONE);
                                if (listDataHeader.get(2).equals("Class")) {
                                    processbar.setVisibility(View.VISIBLE);
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    process_flag_class = false;
                                    level_filter = 3;
                                    requestPlanClassAPI(offsetvalue, limit);
                                }
                            }
                        } catch (Exception e)
                        {
                            Log.e(TAG, "onResponse2: "+e );
                            processbar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                        finally
                        {
                            if (listDataHeader.get(2).equals("Class")) {
                                processbar.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                process_flag_class = false;
                                level_filter = 3;
                                requestPlanClassAPI(offsetvalue, limit);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
    public void requestPlanClassAPI(int offsetvalue1, int limit1)
    {

        String url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + level_filter +"&geoLevel2Code="+geoLevel2Code;
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(SalesFilterActivity.this, "no data found in class", Toast.LENGTH_SHORT).show();
                                processbar.setVisibility(View.GONE);

                            } else if (response.length() == limit) {
                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++)
                                {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String planClass = productName1.getString("planClass");
                                    subPlanClass.add(planClass);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestPlanClassAPI(offsetvalue, limit);
                            }
                            else if (response.length() < limit)
                            {
                                for (int i = 0; i < response.length(); i++)
                                {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String planClass = productName1.getString("planClass");
                                    subPlanClass.add(planClass);
                                }
                                process_flag_class = true;
                                processbar.setVisibility(View.GONE);
                                if (listDataHeader.get(3).equals("Brand")) {
                                    processbar.setVisibility(View.VISIBLE);
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    process_flag_brand = false;
                                    level_filter = 4;
                                    requestBrandNameAPI(offsetvalue, limit);
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            Log.e(TAG, "onResponse3: "+e);
                            processbar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                        finally
                        {
                            if (listDataHeader.get(3).equals("Brand")) {
                                processbar.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                process_flag_brand = false;
                                level_filter = 4;
                                requestBrandNameAPI(offsetvalue, limit);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
    public void requestBrandNameAPI(int offsetvalue1, int limit1)
    {
        String url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + level_filter +"&geoLevel2Code="+geoLevel2Code;
        Log.e(TAG, "requestBrandNameAPI: "+url );
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, "onResponse: "+response );

                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(SalesFilterActivity.this, "no data found in Brand", Toast.LENGTH_SHORT).show();
                                processbar.setVisibility(View.GONE);

                            } else if (response.length() == limit) {

                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);

                                    String brandName = productName1.getString("brandName");
                                    subBrandnm.add(brandName);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestBrandNameAPI(offsetvalue, limit);

                            } else if (response.length() < limit)
                            {
                                for (int i = 0; i < response.length(); i++)
                                {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String brandName = productName1.getString("brandName");
                                    subBrandnm.add(brandName);
                                }
                                process_flag_brand = true;
                                processbar.setVisibility(View.GONE);

                                if (listDataHeader.get(4).equals("Brand Class")) {
                                    processbar.setVisibility(View.VISIBLE);
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    process_flag_mc = false;
                                    level_filter = 5;
                                    requestBrandPlanClassAPI(offsetvalue, limit);
                                }

                            }
                        } catch (Exception e)
                        {
                            Log.e(TAG, "onResponse4----: " +e);
                            processbar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                        finally
                        {
                            if (listDataHeader.get(4).equals("Brand Class")) {
                                processbar.setVisibility(View.VISIBLE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                process_flag_mc = false;
                                level_filter = 5;
                                requestBrandPlanClassAPI(offsetvalue, limit);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        processbar.setVisibility(View.GONE);
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
    public void requestBrandPlanClassAPI(int offsetvalue1, int limit1) {

        String url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + level_filter +"&geoLevel2Code="+geoLevel2Code;

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(SalesFilterActivity.this, "no data found in Brand Class", Toast.LENGTH_SHORT).show();
                                processbar.setVisibility(View.GONE);

                            } else if (response.length() == limit) {
                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++)
                                {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String brandClass = productName1.getString("brandPlanClass");
                                    subBrandPlanClass.add(brandClass);
                                }

                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestBrandPlanClassAPI(offsetvalue, limit);

                            }
                            else if (response.length() < limit)
                            {
                                for (int i = 0; i < response.length(); i++)
                                {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String brandClass = productName1.getString("brandPlanClass");
                                    subBrandPlanClass.add(brandClass);
                                }
                                process_flag_mc = true;
                                processbar.setVisibility(View.GONE);
                            }
                        } catch (Exception e)
                        {
                            Log.e(TAG, "onResponse5: "+e );
                            processbar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        processbar.setVisibility(View.GONE);
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

    @Override
    public void onBackPressed()
    {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        finish();
    }
}
