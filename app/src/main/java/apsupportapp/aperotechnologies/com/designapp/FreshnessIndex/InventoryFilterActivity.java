package apsupportapp.aperotechnologies.com.designapp.FreshnessIndex;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesFilterActivity;
import apsupportapp.aperotechnologies.com.designapp.WorstPerformersInventory.WorstPerformerInventory;
import apsupportapp.aperotechnologies.com.designapp.model.ListBrand;
import apsupportapp.aperotechnologies.com.designapp.model.ListBrandClass;
import apsupportapp.aperotechnologies.com.designapp.model.ListCategory;
import apsupportapp.aperotechnologies.com.designapp.model.ListPlanClass;
import apsupportapp.aperotechnologies.com.designapp.model.ListProdLevel6;


public class InventoryFilterActivity extends Activity {

    RelativeLayout btnS_Filterback, btnS_Done;
    InventoryFilterExpandableList listAdapter;
    public static ExpandableListView pfilter_list;
    ArrayList<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    int offsetvalue = 0, limit = 100, count = 0, level;
    String userId, bearertoken;
    SharedPreferences sharedPreferences;
    RequestQueue queue;
    EditText editTextSearch;
   List<String> subdept, subCategory, subPlanClass, subBrandnm, subBrandPlanClass, subMC;
     JsonArrayRequest postRequest;


    public static List<Integer> groupImages;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_filter);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Log.e("came here", "");
        level = 1;
        subdept = new ArrayList<String>();
        subCategory = new ArrayList<String>();
        subBrandnm = new ArrayList<String>();
        subPlanClass = new ArrayList<String>();
        subBrandPlanClass = new ArrayList<String>();
        subMC = new ArrayList<String>();
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();


        subdept = new ArrayList<String>();

        btnS_Filterback = (RelativeLayout) findViewById(R.id.imageBtnInventoryFilterBack);
        btnS_Done = (RelativeLayout) findViewById(R.id.imageBtnInventoryFilterDone);

        pfilter_list = (ExpandableListView) findViewById(R.id.expandableListView_subdept);
        //noinspection deprecation,deprecation
        pfilter_list.setDivider(getResources().getDrawable(R.color.grey));
        pfilter_list.setDividerHeight(2);
        prepareListData();

        listAdapter = new InventoryFilterExpandableList(this, listDataHeader, listDataChild, pfilter_list, listAdapter);

        // setting list adapter
        pfilter_list.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
        //  Edit Text Search
        editTextSearch = (EditText) findViewById(R.id.editSearchSales);


        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchData = editTextSearch.getText().toString();
                // editTextSearch.clearFocus();
                InputMethodManager inputManager = (InputMethodManager) InventoryFilterActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(editTextSearch.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                listAdapter.filterData(editTextSearch.getText().toString());
                // listAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        btnS_Filterback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getIntent().getStringExtra("checkfrom").equals("freshnessIndex")) {
                    //Intent intent = new Intent(InventoryFilterActivity.this, FreshnessIndexActivity.class);
                    // startActivity(intent);
                    finish();
                } else if (getIntent().getStringExtra("checkfrom").equals("optionEfficiency")) {
                    // Intent intent = new Intent(InventoryFilterActivity.this, OptionEfficiencyActivity.class);
                    // startActivity(intent);
                    finish();
                } else if (getIntent().getStringExtra("checkfrom").equals("skewedSize")) {
                    //  Intent intent = new Intent(InventoryFilterActivity.this, SkewedSizesActivity.class);
                    //startActivity(intent);
                    finish();
                } else if (getIntent().getStringExtra("checkfrom").equals("stockAgeing")) {
                    //Intent intent = new Intent(InventoryFilterActivity.this, StockAgeingActivity.class);
                    //startActivity(intent);
                    finish();
                } else if (getIntent().getStringExtra("checkfrom").equals("bestPerformers")) {
                    // Intent intent = new Intent(InventoryFilterActivity.this, BestPerformerInventory.class);
                    //startActivity(intent);
                    finish();
                } else if (getIntent().getStringExtra("checkfrom").equals("floorAvailability")) {
                    //Intent intent = new Intent(InventoryFilterActivity.this, FloorAvailabilityActivity.class);
                    // startActivity(intent);
                    finish();
                } else if (getIntent().getStringExtra("checkfrom").equals("worstPerformers")) {
                    //  Intent intent = new Intent(InventoryFilterActivity.this, WorstPerformerInventory.class);
                    //  startActivity(intent);
                    finish();
                } else if (getIntent().getStringExtra("checkfrom").equals("targetStockException")) {
                    //Intent intent = new Intent(InventoryFilterActivity.this, TargetStockExceptionActivity.class);
                    // startActivity(intent);
                    finish();
                } else if (getIntent().getStringExtra("checkfrom").equals("sellThruExceptions")) {
                    // Intent intent = new Intent(InventoryFilterActivity.this, SaleThruInventory.class);
                    // startActivity(intent);
                    finish();
                } else if (getIntent().getStringExtra("checkfrom").equals("TopFullCut")) {
                    // Intent intent = new Intent(InventoryFilterActivity.this, TopFullCut.class);
                    //  startActivity(intent);
                    finish();
                }
            }
        });

    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        pfilter_list.setIndicatorBounds(pfilter_list.getRight() - 40, pfilter_list.getWidth());
//    }


    // Prepare list data
    private void prepareListData() {

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding group name data
        listDataHeader.add("Department");
        listDataHeader.add("Category");
        listDataHeader.add("Plan Class");
        listDataHeader.add("Brand");
        listDataHeader.add("Brand Plan Class");
        listDataHeader.add("MC");

        if (Reusable_Functions.chkStatus(InventoryFilterActivity.this)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(InventoryFilterActivity.this, "Loading data...");


            if (listDataHeader.get(0).equals("Department")) {
                offsetvalue = 0;
                limit = 100;
                count = 0;
                level = 1;
                requestInventoryDeptAPI(offsetvalue, limit);
            }

            if (listDataHeader.get(1).equals("Category")) {
                offsetvalue = 0;
                limit = 100;
                count = 0;
                level = 2;
                requestInventoryCategoryAPI(offsetvalue, limit);
            }


            if (listDataHeader.get(2).equals("Plan Class")) {
                offsetvalue = 0;
                limit = 100;
                count = 0;
                level = 3;
                requestInventoryPlanClassAPI(offsetvalue, limit);
            }


            if (listDataHeader.get(3).equals("Brand")) {
                offsetvalue = 0;
                limit = 100;
                count = 0;
                level = 4;
                requestInventoryBrandNameAPI(offsetvalue, limit);
            }


            if (listDataHeader.get(4).equals("Brand Plan Class")) {
                offsetvalue = 0;
                limit = 100;
                count = 0;
                level = 5;
                requestInventoryBrandClassAPI(offsetvalue, limit);
            }


            if (listDataHeader.get(5).equals("MC")) {
                offsetvalue = 0;
                limit = 100;
                count = 0;
                level = 6;
                requestInventoryProdLevel6API(offsetvalue, limit);
            }

        } else {
            Toast.makeText(InventoryFilterActivity.this, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
        listDataChild.put(listDataHeader.get(0), subdept);
        listDataChild.put(listDataHeader.get(1), subCategory);
        listDataChild.put(listDataHeader.get(2), subPlanClass);
        listDataChild.put(listDataHeader.get(3), subBrandnm);
        listDataChild.put(listDataHeader.get(4), subBrandPlanClass);
        listDataChild.put(listDataHeader.get(5), subMC);

    }

    // API declaration used API 1.18
    public void requestInventoryDeptAPI(int offsetvalue1, int limit1) {

        String url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/" + userId + "?level=" + level + "&offset=" + offsetvalue1 + "&limit=" + limit1;
        Log.i("URL   ", url);

        postRequest= new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Inventory Department Response", response.toString());
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(InventoryFilterActivity.this, "no data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {

                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);

                                    String plandept = productName1.getString("planDept");

                                    subdept.add(plandept);
                                }

                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestInventoryDeptAPI(offsetvalue, limit);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);

                                    String planDept = productName1.getString("planDept");
                                    subdept.add(planDept);
                                }

//                                listDataChild.put(listDataHeader.get(0), subdept);
//                                pfilter_list.expandGroup(0);
                                Reusable_Functions.hDialog();
                            }
                        } catch (Exception e) {
                            Log.e("Exception e", e.toString() + "");
                            Toast.makeText(InventoryFilterActivity.this, "no data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InventoryFilterActivity.this, "server not found", Toast.LENGTH_SHORT).show();
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
        queue.getSequenceNumber();
    }

    // API declration used API 1.18
    public void requestInventoryCategoryAPI(int offsetvalue1, int limit1) {


        String inv_category_url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/" + userId + "?level=" + level + "&offset=" + offsetvalue1 + "&limit=" + limit1;

        Log.i("URL   ", inv_category_url);

        postRequest  = new JsonArrayRequest(Request.Method.GET, inv_category_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Inventory Category Filter Response", response.toString());
                        Log.e("Inventory category list", "---" + response.length());
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {

                                Reusable_Functions.hDialog();
                                Toast.makeText(InventoryFilterActivity.this, "no category data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {
                                Reusable_Functions.hDialog();

                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);

                                    String category = productName1.getString("planCategory");
                                    subCategory.add(category);

                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestInventoryCategoryAPI(offsetvalue, limit);

                            } else {
                                if (response.length() < limit) {
                                    for (int i = 0; i < response.length(); i++) {

                                        JSONObject productName1 = response.getJSONObject(i);

                                        String category = productName1.getString("planCategory");
                                        subCategory.add(category);

                                    }
                                    Reusable_Functions.hDialog();

                                }
                            }

                        } catch (Exception e) {
                            Toast.makeText(InventoryFilterActivity.this, "no data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(InventoryFilterActivity.this, "server not found", Toast.LENGTH_SHORT).show();
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

    @SuppressLint("LongLogTag")

    // Plan class API
    public void requestInventoryPlanClassAPI(int offsetvalue1, int limit1) {

        String inv_planclass_url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/" + userId + "?level=" + level + "&offset=" + offsetvalue1 + "&limit=" + limit1;

        Log.e("requestPlanClassAPI URL   ", inv_planclass_url);

        postRequest = new JsonArrayRequest(Request.Method.GET, inv_planclass_url,
                new Response.Listener<JSONArray>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Inventory Plan Class Filter Response", response.toString());
                        Log.e("Inventory plan class list", " " + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {

                                Reusable_Functions.hDialog();
                                Toast.makeText(InventoryFilterActivity.this, "no plan class data found", Toast.LENGTH_LONG).show();

                            } else {
                                if (response.length() == limit) {
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject productName1 = response.getJSONObject(i);

                                        String planClass = productName1.getString("planClass");
                                        subPlanClass.add(planClass);

                                    }
                                    offsetvalue = (limit * count) + limit;
                                    count++;
                                    requestInventoryPlanClassAPI(offsetvalue, limit);

                                } else if (response.length() < limit) {
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject productName1 = response.getJSONObject(i);

                                        String planClass = productName1.getString("planClass");
                                        subPlanClass.add(planClass);
                                    }

                                    Reusable_Functions.hDialog();
                                }
                            }
                        } catch (Exception e) {
                            //Log.e("Exception e", e.toString() + "");
                            Toast.makeText(InventoryFilterActivity.this, "no data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(InventoryFilterActivity.this, "server not found", Toast.LENGTH_SHORT).show();
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

    //Brand Name Api
    public void requestInventoryBrandNameAPI(int offsetvalue1, int limit1) {

        String inv_brand_url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/" + userId + "?level=" + level + "&offset=" + offsetvalue1 + "&limit=" + limit1;

        Log.e("requestPlanClassAPI URL   ", inv_brand_url);

        postRequest = new JsonArrayRequest(Request.Method.GET, inv_brand_url,
                new Response.Listener<JSONArray>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Inventory Brand Filter Response", response.toString());
                        Log.e("Inventory brand list", " " + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(InventoryFilterActivity.this, "no brand name data found", Toast.LENGTH_LONG).show();

                            } else {
                                if (response.length() == limit) {
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject productName1 = response.getJSONObject(i);

                                        String brand_name = productName1.getString("brandName");
                                        subBrandnm.add(brand_name);

                                    }
                                    offsetvalue = (limit * count) + limit;
                                    count++;
                                    requestInventoryBrandNameAPI(offsetvalue, limit);

                                } else if (response.length() < limit) {
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject productName1 = response.getJSONObject(i);

                                        String brand_name = productName1.getString("brandName");
                                        subBrandnm.add(brand_name);

                                    }

                                    Reusable_Functions.hDialog();
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(InventoryFilterActivity.this, "no data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InventoryFilterActivity.this, "server not found", Toast.LENGTH_SHORT).show();
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

    //Brand Class Api
    public void requestInventoryBrandClassAPI(int offsetvalue1, int limit1) {
        String inv_brandplanclass_url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/" + userId + "?level=" + level + "&offset=" + offsetvalue1 + "&limit=" + limit1;
        Log.e("requestPlanClassAPI URL   ", inv_brandplanclass_url);

        postRequest = new JsonArrayRequest(Request.Method.GET, inv_brandplanclass_url,
                new Response.Listener<JSONArray>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Inv Brand Plan Class Filter Response", response.toString());
                        Log.e("Inv brand plan class list", " " + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {

                                Reusable_Functions.hDialog();
                                Toast.makeText(InventoryFilterActivity.this, "no brand plan class data found", Toast.LENGTH_LONG).show();

                            } else {
                                if (response.length() == limit) {
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject productName1 = response.getJSONObject(i);

                                        String brandPlanClass = productName1.getString("brandPlanClass");
                                        subBrandPlanClass.add(brandPlanClass);

                                    }
                                    offsetvalue = (limit * count) + limit;
                                    count++;
                                    requestInventoryBrandClassAPI(offsetvalue, limit);

                                } else if (response.length() < limit) {
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject productName1 = response.getJSONObject(i);

                                        String brandPlanClass = productName1.getString("brandPlanClass");
                                        subBrandPlanClass.add(brandPlanClass);

                                    }
                                    Reusable_Functions.hDialog();

                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(InventoryFilterActivity.this, "no data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InventoryFilterActivity.this, "server not found", Toast.LENGTH_SHORT).show();
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

    // Prod level 6 Api
    private void requestInventoryProdLevel6API(int offsetvalue1, int limit1) {


        String url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/" + userId + "?level=" + level + "&offset=" + offsetvalue + "&limit=" + limit;

        // String inv_prodlevel6_url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/"+ userId +"?level="+level+"&dept="+ planDepartmentName.replaceAll(" ", "%20").replaceAll("&", "%26") + "&category=" + planCategoryName.replaceAll(" ", "%20").replaceAll("&", "%26") + "&class=" + planClassName.replaceAll(" ", "%20").replaceAll("&", "%26")+"&brand= "+ brand_Name.replaceAll(" ", "%20").replaceAll("&", "%26")+"&brandclass=" + brandPlanClass.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("requestProd6Level URL   ", url);

        postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Inv Prod level 6 Filter Response", response.toString());
                        Log.e("Inv prod level 6 list", " " + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {

                                Reusable_Functions.hDialog();
                                Toast.makeText(InventoryFilterActivity.this, "no brand plan class data found", Toast.LENGTH_LONG).show();

                            } else {
                                if (response.length() == limit) {
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject productName1 = response.getJSONObject(i);

                                        //  String mcCode = productName1.getString("prodLevel6Code");
                                        String mc = productName1.getString("prodLevel6Desc");
                                        subMC.add(mc);

                                    }
                                    offsetvalue = (limit * count) + limit;
                                    count++;
                                    requestInventoryProdLevel6API(offsetvalue, limit);

                                } else if (response.length() < limit) {
                                    for (int i = 0; i < response.length(); i++) {

                                        JSONObject productName1 = response.getJSONObject(i);
                                        //   String mcCode = productName1.getString("prodLevel6Code");
                                        String mc = productName1.getString("prodLevel6Desc");
                                        subMC.add(mc);


                                    }
                                    Reusable_Functions.hDialog();

                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(InventoryFilterActivity.this, "no data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InventoryFilterActivity.this, "server not found", Toast.LENGTH_SHORT).show();
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

        if (getIntent().getStringExtra("checkfrom").equals("freshnessIndex")) {
            // Intent intent = new Intent(InventoryFilterActivity.this, FreshnessIndexActivity.class);
            // startActivity(intent);
            finish();
        } else if (getIntent().getStringExtra("checkfrom").equals("optionEfficiency")) {
            // Intent intent = new Intent(InventoryFilterActivity.this, OptionEfficiencyActivity.class);
            // startActivity(intent);
            finish();
        } else if (getIntent().getStringExtra("checkfrom").equals("skewedSize")) {
            // Intent intent = new Intent(InventoryFilterActivity.this, SkewedSizesActivity.class);
            //  startActivity(intent);
            finish();
        } else if (getIntent().getStringExtra("checkfrom").equals("stockAgeing")) {
            // Intent intent = new Intent(InventoryFilterActivity.this, StockAgeingActivity.class);
            // startActivity(intent);
            finish();
        } else if (getIntent().getStringExtra("checkfrom").equals("bestPerformers")) {
            //Intent intent = new Intent(InventoryFilterActivity.this, BestPerformerInventory.class);
            //startActivity(intent);
            finish();
        } else if (getIntent().getStringExtra("checkfrom").equals("floorAvailability")) {
            //Intent intent = new Intent(InventoryFilterActivity.this, FloorAvailabilityActivity.class);
            // startActivity(intent);
            finish();
        } else if (getIntent().getStringExtra("checkfrom").equals("worstPerformers")) {
            // Intent intent = new Intent(InventoryFilterActivity.this, WorstPerformerInventory.class);
            //  startActivity(intent);
            finish();
        } else if (getIntent().getStringExtra("checkfrom").equals("targetStockException")) {
            //Intent intent = new Intent(InventoryFilterActivity.this, TargetStockExceptionActivity.class);
            // startActivity(intent);
            finish();
        } else if (getIntent().getStringExtra("checkfrom").equals("sellThruExceptions")) {
            //Intent intent = new Intent(InventoryFilterActivity.this, SaleThruInventory.class);
            // startActivity(intent);
            finish();
        } else if (getIntent().getStringExtra("checkfrom").equals("TopFullCut")) {
            // Intent intent = new Intent(InventoryFilterActivity.this, TopFullCut.class);
            // startActivity(intent);
            finish();
        }
    }

}