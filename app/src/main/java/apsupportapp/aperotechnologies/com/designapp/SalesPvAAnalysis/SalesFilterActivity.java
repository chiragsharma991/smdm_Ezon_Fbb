package apsupportapp.aperotechnologies.com.designapp.SalesPvAAnalysis;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;


public class SalesFilterActivity extends Activity {

    RelativeLayout btnS_Filterback, btnS_Done;
    ExpandableList listAdapter;
    public static ExpandableListView pfilter_list;
    ArrayList<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    int offsetvalue = 0, limit = 100, count = 0;
    String userId, bearertoken;
    SharedPreferences sharedPreferences;
    RequestQueue queue;
    List<String> subdept;
    String pf_prodName = " ", subdeptName;
    public static Activity filterActivity;
    public static List<Integer> groupImages;
    List<String> productList, articleList;
    ArrayList productnamelist, articleOptionList;
    String ProductName;
    ArrayList<FilterArray> arrfilter;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salespva_filter);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        filterActivity = this;

        Log.e("came here", "");

        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        pf_prodName = " ";
        articleList = new ArrayList<String>();
        articleOptionList = new ArrayList();
        subdept = new ArrayList<String>();
        arrfilter = new ArrayList<FilterArray>();
        btnS_Filterback = (RelativeLayout) findViewById(R.id.imageBtnSFilterBack);
        btnS_Done = (RelativeLayout) findViewById(R.id.imageBtnSalesFilterDone);

        pfilter_list = (ExpandableListView) findViewById(R.id.expandableListView_subdept);
        //noinspection deprecation,deprecation
        pfilter_list.setDivider(getResources().getDrawable(R.color.grey));
        pfilter_list.setDividerHeight(2);
        prepareListData();

        listAdapter = new ExpandableList(this, listDataHeader, listDataChild, pfilter_list, listAdapter);

        pfilter_list.setChoiceMode(ExpandableListView.CHOICE_MODE_MULTIPLE);
        // setting list adapter
        pfilter_list.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();

        btnS_Filterback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SalesFilterActivity.this, SalesPvAActivity.class);
                startActivity(intent);
                finish();
            }
        });

        pfilter_list.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        // Listview Group collasped listener
        pfilter_list.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                //pfilter_list.collapseGroup(groupPosition);
            }
        });
        // Listview on child click listener
        pfilter_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                subdeptName = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                Log.e("subDeptName", " " + subdeptName);
                return false;

            }
        });

    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        pfilter_list.setIndicatorBounds(pfilter_list.getRight() - 40, pfilter_list.getWidth());
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        groupImages = new ArrayList<Integer>();
        groupImages.add(R.mipmap.filter_department_icon);
        groupImages.add(R.mipmap.filter_category_icon);
        groupImages.add(R.mipmap.filter_planclass_icon);
        groupImages.add(R.mipmap.filter_brand_icon);
        groupImages.add(R.mipmap.filter_brandplanclass_icon);
        // Adding group name data
        listDataHeader.add("Department");
        listDataHeader.add("Category");
        listDataHeader.add("Plan Class");
        listDataHeader.add("Brand");
        listDataHeader.add("Brand Plan Class");

        if (Reusable_Functions.chkStatus(SalesFilterActivity.this)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(SalesFilterActivity.this, "Loading  data...");
            offsetvalue = 0;
            count = 0;
            requestSubDeptAPI(offsetvalue, limit);
        } else {

            Toast.makeText(SalesFilterActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();

        }
    }

    public void requestFilterProductAPI(int offsetvalue1, int limit1, final String subdeptName) {

//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SalesFilterActivity.this);
//        String userId = sharedPreferences.getString("userId", "");
//        final String bearertoken = sharedPreferences.getString("bearerToken", "");
//        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
//        BasicNetwork network = new BasicNetwork(new HurlStack());
//        RequestQueue queue = new RequestQueue(cache, network);
//        queue.start();
        String url = ConstsCore.web_url + "/v1/display/hourlytransproducts/" + userId + "?view=productName&prodLevel3Desc=" + subdeptName.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset" + offsetvalue + "&limit" + limit;
        Log.i("URL   ", url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Sub Dept Response", response.toString());
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(SalesFilterActivity.this, "no data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {
                                Reusable_Functions.hDialog();

                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    ProductName = productName1.getString("productName");
                                    //Log.e("Product Name:", ProductName);

                                    productList.add(ProductName);
                                    productnamelist.add(ProductName);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestFilterProductAPI(offsetvalue, limit, subdeptName);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    ProductName = productName1.getString("productName");
                                    //Log.e("Product Name:", ProductName);
                                    productList.add(ProductName);
                                    productnamelist.add(ProductName);
                                }
                                Collections.sort(productnamelist);
                                listDataChild.put(listDataHeader.get(1), productnamelist);
                                FilterArray filterArray = new FilterArray();
                                filterArray.setSubdept(subdeptName);
                                filterArray.setprodArray((ArrayList) productList);
                                arrfilter.add(filterArray);
                                SalesFilterActivity.pfilter_list.expandGroup(1);
                                Reusable_Functions.hDialog();
                            }

                        } catch (Exception e) {
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


    public void requestSubDeptAPI(int offsetvalue1, int limit1) {

        String url = ConstsCore.web_url + "/v1/display/hourlyfilterproducts/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1;

        Log.i("URL   ", url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("SubDept Response", response.toString());
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(SalesFilterActivity.this, "no data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {

                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);

                                    //String prodLevel3Code = productName1.getString("prodLevel3Code");
                                    String prodLevel3Desc = productName1.getString("prodLevel3Desc");

                                    subdept.add(prodLevel3Desc);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSubDeptAPI(offsetvalue, limit);
                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    //String prodLevel3Code = productName1.getString("prodLevel3Code");
                                    String prodLevel3Desc = productName1.getString("prodLevel3Desc");
                                    subdept.add(prodLevel3Desc);
                                    // Log.e("ArrayList", "" + productsubdeptList.size());
                                }
                                Collections.sort(subdept);
                                listDataChild.put(listDataHeader.get(0), subdept);
                                Reusable_Functions.hDialog();
                            }
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
        Intent i = new Intent(SalesFilterActivity.this, SalesPvAActivity.class);
        startActivity(i);
        finish();
    }
}
