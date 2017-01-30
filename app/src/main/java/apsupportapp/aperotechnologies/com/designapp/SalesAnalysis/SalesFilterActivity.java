package apsupportapp.aperotechnologies.com.designapp.SalesAnalysis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
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
import apsupportapp.aperotechnologies.com.designapp.KeyProductActivity;
import apsupportapp.aperotechnologies.com.designapp.PvaSalesAnalysis.SalesPvAActivity;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.SearchActivity1;


public class SalesFilterActivity extends Activity {

    RelativeLayout btnS_Filterback, btnS_Done;
    static SalesFilterExpandableList listAdapter;
    public static ExpandableListView pfilter_list;
    ArrayList<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    int offsetvalue = 0, limit = 100, count = 0, level = 1;
    String userId, bearertoken;
    SharedPreferences sharedPreferences;
    RequestQueue queue;
    Context context;
    //git testing 05/01/2017

    String TAG = "SalesFilterActivity";
    static List<String> subdept, subCategory, subPlanClass, subBrandnm, subBrandPlanClass;
    public static String plandeptName;
    EditText editTextSearch;


    public static List<Integer> groupImages;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salespva_filter);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Log.e("came here", "");
        context = this;
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();

        subdept = new ArrayList<String>();
        subCategory = new ArrayList<String>();
        subBrandnm = new ArrayList<String>();
        subPlanClass = new ArrayList<String>();
        subBrandPlanClass = new ArrayList<String>();

        btnS_Filterback = (RelativeLayout) findViewById(R.id.imageBtnSFilterBack);
        btnS_Done = (RelativeLayout) findViewById(R.id.imageBtnSalesFilterDone);

        pfilter_list = (ExpandableListView) findViewById(R.id.expandableListView_subdept);
        pfilter_list.setTextFilterEnabled(true);
        //noinspection deprecation,deprecation
        pfilter_list.setDivider(getResources().getDrawable(R.color.grey));
        pfilter_list.setDividerHeight(2);
        prepareListData();

        listAdapter = new SalesFilterExpandableList(this, listDataHeader, listDataChild, pfilter_list, listAdapter);

        // pfilter_list.setChoiceMode(ExpandableListView.CHOICE_MODE_MULTIPLE);
        // setting list adapter
        pfilter_list.setAdapter(listAdapter);

        //listAdapter.notifyDataSetChanged();



        pfilter_list.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

//
            }


        });

        // Listview Group collasped listener
        pfilter_list.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                //pfilter_list.collapseGroup(groupPosition);
            }
        });

//        pfilter_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
//                parent.setItemChecked(index, true);
//
//
//                return false;
//            }
//        });

        //  Edit Text Search
        editTextSearch = (EditText)findViewById(R.id.editSearchSales);



        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchData = editTextSearch.getText().toString();
                // editTextSearch.clearFocus();
                InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(editTextSearch.getWindowToken(),InputMethodManager.HIDE_IMPLICIT_ONLY);
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

                if (getIntent().getStringExtra("checkfrom").equals("SalesAnalysis")) {
                    //  Intent intent = new Intent(SalesFilterActivity.this, SalesAnalysisActivity.class);
                    //  startActivity(intent);
                    finish();
                } else if (getIntent().getStringExtra("checkfrom").equals("pvaAnalysis")) {
                    // Intent intent = new Intent(SalesFilterActivity.this, SalesPvAActivity.class);
                    //  startActivity(intent);
                    finish();
                }
            }
        });



        btnS_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(SalesFilterActivity.this,SalesAnalysisActivity1.class);
//                intent.putExtra("selectedDept",SalesFilterExpandableList.txtClickedVal);
//                Log.e(TAG,"txtClickedVal"+SalesFilterExpandableList.txtClickedVal);
//                startActivity(intent);
            }
        });


    }


//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        pfilter_list.setIndicatorBounds(pfilter_list.getRight() - 40, pfilter_list.getWidth());
//    }


    /*
     * Preparing the list data
     */

    private void prepareListData() {

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding group name data
        listDataHeader.add("Department");
        listDataHeader.add("Category");
        listDataHeader.add("Plan Class");
        listDataHeader.add("Brand");
        listDataHeader.add("Brand Plan Class");
        if (Reusable_Functions.chkStatus(SalesFilterActivity.this)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(SalesFilterActivity.this, "Loading data...");

            if (listDataHeader.get(0).equals("Department"))
            {
                offsetvalue = 0;
                limit = 100;
                count = 0;
                level = 1;
                requestDeptAPI(offsetvalue, limit);
            }
            if (listDataHeader.get(1).equals("Category"))
            {
                offsetvalue = 0;
                limit = 100;
                count = 0;
                level = 2;
                requestCategoryAPI(offsetvalue, limit);
            }
            if (listDataHeader.get(2).equals("Plan Class"))
            {
                offsetvalue = 0;
                limit = 100;
                count = 0;
                level = 3;
                requestPlanClassAPI(offsetvalue, limit);
            }
            if (listDataHeader.get(3).equals("Brand"))
            {
                offsetvalue = 0;
                limit = 100;
                count = 0;
                level = 4;
                requestBrandNameAPI(offsetvalue, limit);
            }
            if (listDataHeader.get(4).equals("Brand Plan Class"))
            {
                offsetvalue = 0;
                limit = 100;
                count = 0;
                level = 5;
                requestBrandPlanClassAPI(offsetvalue, limit);
            }
        } else {
            Toast.makeText(SalesFilterActivity.this, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }

        listDataChild.put(listDataHeader.get(0),subdept);
        listDataChild.put(listDataHeader.get(1),subCategory);
        listDataChild.put(listDataHeader.get(2),subPlanClass);
        listDataChild.put(listDataHeader.get(3),subBrandnm);
        listDataChild.put(listDataHeader.get(4),subBrandPlanClass);
    }

    // Department List
    public void requestDeptAPI(int offsetvalue1, int limit1) {
        String url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + level;
        Log.i("URL   ", url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Department Response", response.toString());
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(SalesFilterActivity.this, "no data found", Toast.LENGTH_LONG).show();
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
                                //Collections.sort(subdept);
                                //   listDataChild.put(listDataHeader.get(0), subdept);

                                // pfilter_list.expandGroup(0);
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

    //Category List
    public void requestCategoryAPI(int offsetvalue1, int limit1) {

        String url =  ConstsCore.web_url + "/v1/display/salesanalysishierarchy/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + level;
        Log.i("URL   ", url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Category Response", response.toString());
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(SalesFilterActivity.this, "no data found", Toast.LENGTH_LONG).show();
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
                                //Collections.sort(subdept);
                                //   listDataChild.put(listDataHeader.get(1), subCategory);
                                // pfilter_list.expandGroup(1);
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

    //Plan Class List
    public void requestPlanClassAPI(int offsetvalue1, int limit1) {
        String url =  ConstsCore.web_url + "/v1/display/salesanalysishierarchy/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + level;
        Log.i("URL   ", url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("PlanClass Response", response.toString() + " Size" + response.length());
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(SalesFilterActivity.this, "no data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {

                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);

                                    String planClass = productName1.getString("planClass");
                                    subPlanClass.add(planClass);
                                }

                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestPlanClassAPI(offsetvalue, limit);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);

                                    String planClass = productName1.getString("planClass");
                                    subPlanClass.add(planClass);

                                }
                                //Collections.sort(subdept);
                                //   listDataChild.put(listDataHeader.get(2), subPlanClass);
                                //  pfilter_list.expandGroup(2);
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


    //Brand Name List
    public void requestBrandNameAPI(int offsetvalue1, int limit1) {
        String url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + level;
        Log.i("URL   ", url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Brand Name Response", response.toString() + "Size---" + response.length());
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(SalesFilterActivity.this, "no data found", Toast.LENGTH_LONG).show();
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

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);

                                    String brandName = productName1.getString("brandName");
                                    subBrandnm.add(brandName);
                                }
                                //Collections.sort(subdept);
                                //  listDataChild.put(listDataHeader.get(3), subBrandnm);
                                // pfilter_list.expandGroup(3);
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


    //Brand Plan Class List
    public void requestBrandPlanClassAPI(int offsetvalue1, int limit1) {

        String url = ConstsCore.web_url + "/v1/display/salesanalysishierarchy/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1 + "&level=" + level;
        Log.i("URL   ", url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Brand Class Response", response.toString() + "Size ---" + response.length());
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(SalesFilterActivity.this, "no data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {

                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);

                                    String brandClass = productName1.getString("brandPlanClass");
                                    subBrandPlanClass.add(brandClass);
                                }

                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestBrandPlanClassAPI(offsetvalue, limit);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);

                                    String brandClass = productName1.getString("brandPlanClass");
                                    subBrandPlanClass.add(brandClass);
                                }
                                //Collections.sort(subdept);
                                //     listDataChild.put(listDataHeader.get(4), subBrandPlanClass);
                                //   pfilter_list.expandGroup(4);
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
        if (getIntent().getStringExtra("checkfrom").equals("SalesAnalysis")) {
            //Intent intent = new Intent(SalesFilterActivity.this, SalesAnalysisActivity.class);
            // startActivity(intent);
            finish();
        } else if (getIntent().getStringExtra("checkfrom").equals("pvaAnalysis")) {
            // Intent intent = new Intent(SalesFilterActivity.this, SalesPvAActivity.class);
            // startActivity(intent);
            finish();
        }
    }
}
