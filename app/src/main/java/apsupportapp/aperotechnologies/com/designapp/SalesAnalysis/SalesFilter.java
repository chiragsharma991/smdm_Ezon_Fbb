package apsupportapp.aperotechnologies.com.designapp.SalesAnalysis;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;

/**
 * Created by pamrutkar on 29/12/16.
 */

public class SalesFilter extends AppCompatActivity implements View.OnClickListener {

    TextView txtdepartment, txtcategory, txtplanclass, txtbrand, txtbrandclass;
    LinearLayout linear_dept, linear_category, linear_planclass, linear_brandnm, linear_brandclass;
    static String dept = "OFF", category = "OFF", planCls = "OFF", brand = "OFF", brandCls = "OFF";
    ListView deptListView, catListView, planClsListView, brandListView, brandClsListView;
    ArrayList<String> depmentList, catryList, planClsList, brndList, brndClsList;
    RelativeLayout Filter_imageBtnBack;
    RelativeLayout FilterOk;
    String userId, bearertoken;
    Context context;
    SharedPreferences sharedPreferences;
    RequestQueue queue;
    //git testing 05/01/2017

    static SalesFilterAdapter salesFilterAdapter, categoryAdapter, planClassAdapter, branAdapter, brandClsAdapter;
    EditText editSearch;
    ImageButton btnSearch;
    String searchData;
    int offsetvalue = 0, limit = 100, count = 0, level = 1;
    public  String searchDept = "", searchCategory = "", searchPlanClass = "", searchBrand = "", searchBrandPlanClass = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_filter);
        getSupportActionBar().hide();
        context = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Log.e("came here", "");

        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();

        depmentList = new ArrayList<String>();
        catryList = new ArrayList<String>();
        planClsList = new ArrayList<String>();
        brndList = new ArrayList<String>();
        brndClsList = new ArrayList<String>();
        intialize();
        main();

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Log.e("Size",""+salesFilterAdapter.getCount());
                // salesFilterAdapter.getFilter().filter(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchData = editSearch.getText().toString();
                Log.e("list", searchData);
                //  s = searchData;
                //editSearch.clearFocus();
                InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);
                Log.e("Search Data in Dept", searchData);
                salesFilterAdapter.getFilter().filter(searchData);
                categoryAdapter.getFilter().filter(searchData);
                Log.e("Search Data in Plan Class", "" + searchData);
                planClassAdapter.getFilter().filter(searchData);
                branAdapter.getFilter().filter(searchData);
                brandClsAdapter.getFilter().filter(searchData);
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });


    }

    private void main() {
        //Department List
        depmentList = new ArrayList<String>();
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading  data...");
            offsetvalue = 0;
            count = 0;
            limit = 100;
            level = 1;
            requestDeptAPI(offsetvalue, limit);
        } else {

            Toast.makeText(SalesFilter.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
        }
        //Category List
        catryList = new ArrayList<String>();
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading  data...");
            offsetvalue = 0;
            count = 0;
            limit = 100;
            level = 2;
            requestCategoryAPI(offsetvalue, limit);
        } else {

            Toast.makeText(SalesFilter.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
        }
        //Plan Class
        planClsList = new ArrayList<String>();
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading  data...");
            offsetvalue = 0;
            limit = 100;
            count = 0;
            level = 3;
            requestPlanClassAPI(offsetvalue, limit);
        } else {

            Toast.makeText(SalesFilter.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
        }

        //Brand Name
        brndList = new ArrayList<String>();
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading  data...");
            offsetvalue = 0;
            limit = 100;
            count = 0;
            level = 4;
            requestBrandNameAPI(offsetvalue, limit);
        } else {

            Toast.makeText(SalesFilter.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
        }
        //Brand Plan Class
        brndClsList = new ArrayList<String>();
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading  data...");
            offsetvalue = 0;
            limit = 100;
            count = 0;
            level = 5;
            requestBrandPlanClassAPI(offsetvalue, limit);
        } else {

            Toast.makeText(SalesFilter.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
        }
    }

    private void intialize() {

        txtdepartment = (TextView) findViewById(R.id.txtdepartment);
        txtcategory = (TextView) findViewById(R.id.txtcategory);
        txtplanclass = (TextView) findViewById(R.id.txtplanclass);
        txtbrand = (TextView) findViewById(R.id.txtbrand);
        txtbrandclass = (TextView) findViewById(R.id.txtbrandplanclass);
        linear_dept = (LinearLayout) findViewById(R.id.linear_dept);
        linear_category = (LinearLayout) findViewById(R.id.linear_category);
        linear_planclass = (LinearLayout) findViewById(R.id.linear_planClass);
        linear_brandnm = (LinearLayout) findViewById(R.id.linear_brand);
        linear_brandclass = (LinearLayout) findViewById(R.id.linear_brandclass);
        Filter_imageBtnBack = (RelativeLayout) findViewById(R.id.filter_imageBtnBack);
        FilterOk = (RelativeLayout) findViewById(R.id.filterOk);
        editSearch = (EditText) findViewById(R.id.editSearchSales);
        // btnSearch = (ImageButton) findViewById(R.id.btnSeatchList);

        deptListView = (ListView) findViewById(R.id.deptList);
        setListViewHeightBasedOnChildren(deptListView);
        catListView = (ListView) findViewById(R.id.categoryList);
        setListViewHeightBasedOnChildren(catListView);
        planClsListView = (ListView) findViewById(R.id.planClassList);
        setListViewHeightBasedOnChildren(planClsListView);
        brandListView = (ListView) findViewById(R.id.brandList);
        setListViewHeightBasedOnChildren(brandListView);
        brandClsListView = (ListView) findViewById(R.id.brandclassList);
        setListViewHeightBasedOnChildren(brandClsListView);

        txtdepartment.setOnClickListener(this);
        txtcategory.setOnClickListener(this);
        txtplanclass.setOnClickListener(this);
        txtbrand.setOnClickListener(this);
        txtbrandclass.setOnClickListener(this);
        Filter_imageBtnBack.setOnClickListener(this);
        FilterOk.setOnClickListener(this);
        //   btnSearch.setOnClickListener(this);


    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        android.widget.ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtdepartment:
                loadDepartmentData();
                break;
            case R.id.txtcategory:
                loadCategoryData();
                break;
            case R.id.txtplanclass:
                loadPlanClassData();
                break;
            case R.id.txtbrand:
                loadBrandData();
                break;
            case R.id.txtbrandplanclass:
                loadBrandClassData();
                break;
            case R.id.filter_imageBtnBack:
                filterBack();
                break;
            case R.id.filterOk:
                Toast.makeText(this, "Selected Dept : "+searchDept, Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(SalesFilter.this,SalesAnalysisActivity.class);
                //  intent.putExtra("selectedDept",searchDept);
                //  startActivity(intent);
                finish();
                break;
//            case R.id.btnSeatchList:
//                break;

        }
    }

    private void filterBack() {
        if (getIntent().getStringExtra("checkfrom").equals("SalesAnalysis")) {
            // Intent intent = new Intent(SalesFilter.this, SalesAnalysisActivity.class);
            // startActivity(intent);
            finish();
        }
//        else if (getIntent().getStringExtra("checkfrom").equals("pvaAnalysis")) {
//            Intent intent = new Intent(lesFilterActivity.this, SalesPvAActivity.class);
//            startActivity(intent);
//            finish();
//        }

    }


    public void loadDepartmentData() {

        if (dept.equals("OFF")) {
            linear_dept.setVisibility(View.VISIBLE);
            txtdepartment.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.uplist, 0);
            dept = "ON";
        } else {
            linear_dept.setVisibility(View.GONE);
            txtdepartment.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
            dept = "OFF";
        }
    }

    public void loadCategoryData() {

        if (category.equals("OFF")) {
            linear_category.setVisibility(View.VISIBLE);
            txtcategory.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.uplist, 0);
            category = "ON";
        } else {
            linear_category.setVisibility(View.GONE);
            txtcategory.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
            category = "OFF";
        }
    }

    public void loadPlanClassData() {

        if (planCls.equals("OFF")) {
            linear_planclass.setVisibility(View.VISIBLE);
            txtplanclass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.uplist, 0);

            planCls = "ON";
        } else {
            linear_planclass.setVisibility(View.GONE);
            txtplanclass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
            planCls = "OFF";
        }
    }

    public void loadBrandData() {

        if (brand.equals("OFF")) {
            linear_brandnm.setVisibility(View.VISIBLE);
            txtbrand.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.uplist, 0);

            brand = "ON";
        } else {
            linear_brandnm.setVisibility(View.GONE);
            txtbrand.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
            brand = "OFF";
        }
    }

    public void loadBrandClassData() {

        if (brandCls.equals("OFF")) {
            linear_brandclass.setVisibility(View.VISIBLE);
            txtbrandclass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.uplist, 0);
            brandCls = "ON";
        } else {
            linear_brandclass.setVisibility(View.GONE);
            txtbrandclass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
            brandCls = "OFF";
        }
    }


    // Department List
    public void requestDeptAPI(int offsetvalue1, int limit1) {
        String url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + level;
        Log.i("URL   ", url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Department Response", response.toString());
                        Log.e("Department response size", "" + response.length());
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(SalesFilter.this, "no data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {

                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String plandept = productName1.getString("planDept");
                                    depmentList.add(plandept);
                                }

                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestDeptAPI(offsetvalue, limit);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String planDept = productName1.getString("planDept");
                                    depmentList.add(planDept);

                                }

                            }
                            salesFilterAdapter = new SalesFilterAdapter(depmentList, getApplicationContext());
                            deptListView.setAdapter(salesFilterAdapter);
                            deptListView.setTextFilterEnabled(true);
                            deptListView.setOnTouchListener(new View.OnTouchListener() {
                                // Setting on Touch Listener for handling the touch inside ScrollView
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    // Disallow the touch request for parent scroll on touch of child view
                                    v.getParent().requestDisallowInterceptTouchEvent(true);
                                    return false;
                                }
                            });
                            deptListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
                            deptListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String productSubDeptItem = (String) parent.getItemAtPosition(position);

                                    searchDept = productSubDeptItem.replaceAll(" ", "%20").replaceAll("&", "%26");
                                    view.setBackgroundColor(Color.GRAY);
                                }
                            });





                            Reusable_Functions.hDialog();
                        } catch (Exception e) {
                            Log.e("Exception e", e.toString() + "");
                            e.printStackTrace();
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
    public void requestCategoryAPI(int offsetvalue1, int limit1) {
        String url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + level;
        Log.i("URL   ", url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Category Response", response.toString());
                        Log.e("Category response size", "" + response.length());
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {

                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);

                                    String planCategory = productName1.getString("planCategory");

                                    catryList.add(planCategory);
                                }

                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestCategoryAPI(offsetvalue, limit);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);

                                    String planCategory = productName1.getString("planCategory");
                                    catryList.add(planCategory);

                                }

                            }
                            categoryAdapter = new SalesFilterAdapter(catryList, getApplicationContext());
                            catListView.setAdapter(categoryAdapter);
                            catListView.setTextFilterEnabled(true);

                            catListView.setOnTouchListener(new View.OnTouchListener() {
                                // Setting on Touch Listener for handling the touch inside ScrollView
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    // Disallow the touch request for parent scroll on touch of child view
                                    v.getParent().requestDisallowInterceptTouchEvent(true);
                                    return false;
                                }
                            });

                            Reusable_Functions.hDialog();
                        } catch (Exception e) {
                            Log.e("Exception e", e.toString() + "");
                            e.printStackTrace();
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
    public void requestPlanClassAPI(int offsetvalue1, int limit1) {
        String url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + level;
        Log.i("URL   ", url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("PlanClass Response", response.toString());
                        Log.e("Plan Class response size", "" + response.length());
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {

                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);

                                    String planClass = productName1.getString("planClass");
                                    planClsList.add(planClass);
                                }

                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestPlanClassAPI(offsetvalue, limit);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);

                                    String planClass = productName1.getString("planClass");
                                    planClsList.add(planClass);

                                }
                            }
                            planClassAdapter = new SalesFilterAdapter(planClsList, getApplicationContext());
                            planClsListView.setAdapter(planClassAdapter);
                            planClsListView.setTextFilterEnabled(true);
                            planClsListView.setOnTouchListener(new View.OnTouchListener() {
                                // Setting on Touch Listener for handling the touch inside ScrollView
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    // Disallow the touch request for parent scroll on touch of child view
                                    v.getParent().requestDisallowInterceptTouchEvent(true);
                                    return false;
                                }
                            });

                            //Collections.sort(subdept);
                            //  listDataChild.put(listDataHeader.get(2), subPlanClass);
                            //  pfilter_list.expandGroup(2);
                            Reusable_Functions.hDialog();

                        } catch (Exception e) {
                            Log.e("Exception e", e.toString() + "");
                            e.printStackTrace();
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
    public void requestBrandNameAPI(int offsetvalue1, int limit1) {
        String url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + level;
        Log.i("URL   ", url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Brand Name Response", response.toString());
                        Log.e("Brand response size", "" + response.length());
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {

                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);

                                    String brandName = productName1.getString("brandName");
                                    brndList.add(brandName);
                                }

                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestBrandNameAPI(offsetvalue, limit);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);

                                    String brandName = productName1.getString("brandName");
                                    brndList.add(brandName);
                                }
                            }
                            branAdapter = new SalesFilterAdapter(brndList, getApplicationContext());
                            brandListView.setAdapter(branAdapter);
                            brandListView.setTextFilterEnabled(true);
                            brandListView.setOnTouchListener(new View.OnTouchListener() {
                                // Setting on Touch Listener for handling the touch inside ScrollView
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    // Disallow the touch request for parent scroll on touch of child view
                                    v.getParent().requestDisallowInterceptTouchEvent(true);
                                    return false;
                                }
                            });

                            //Collections.sort(subdept);
                            // listDataChild.put(listDataHeader.get(3), subBrandnm);
                            // pfilter_list.expandGroup(3);
                            Reusable_Functions.hDialog();
                        } catch (Exception e) {
                            Log.e("Exception e", e.toString() + "");
                            e.printStackTrace();
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


    // Brand Plan Class List
    public void requestBrandPlanClassAPI(int offsetvalue1, int limit1) {

        String url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + level;
        Log.i("URL   ", url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Brand Class Response", response.toString());
                        Log.e("Brand Class response size", "" + response.length());
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {

                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);

                                    String brandClass = productName1.getString("brandPlanClass");
                                    brndClsList.add(brandClass);
                                }

                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestBrandPlanClassAPI(offsetvalue, limit);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);

                                    String brandClass = productName1.getString("brandPlanClass");
                                    brndClsList.add(brandClass);
                                }

                            }
                            brandClsAdapter = new SalesFilterAdapter(brndClsList, getApplicationContext());
                            brandClsListView.setAdapter(brandClsAdapter);
                            brandClsListView.setTextFilterEnabled(true);

                            brandClsListView.setOnTouchListener(new View.OnTouchListener() {
                                // Setting on Touch Listener for handling the touch inside ScrollView
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    // Disallow the touch request for parent scroll on touch of child view
                                    v.getParent().requestDisallowInterceptTouchEvent(true);
                                    return false;
                                }
                            });

                            //Collections.sort(subdept);
                            //listDataChild.put(listDataHeader.get(4), subBrandPlanClass);
                            //pfilter_list.expandGroup(4);
                            Reusable_Functions.hDialog();
                        } catch (Exception e) {
                            Log.e("Exception e", e.toString() + "");
                            e.printStackTrace();
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        filterBack();
    }


}
