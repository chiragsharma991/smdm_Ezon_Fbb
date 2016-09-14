package apsupportapp.aperotechnologies.com.designapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
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
import apsupportapp.aperotechnologies.com.designapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pamrutkar on 07/09/16.
 */
public class Prod_FilterActivity extends Activity {

    Button btnP_Filterback;
    ImageButton btnP_SubFilter;
    ExpandableListAdapter listAdapter;
    ExpandableListView pfilter_list;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    int offsetvalue = 0, limit = 100, count = 0;
    String userId,subdeptName, bearertoken;
    SharedPreferences sharedPreferences;
    RequestQueue queue;
    ProductLevelBean productLevelBean;
    List<String> subdept = new ArrayList<String>(), productList = new ArrayList<String>();
    String pf_prodName = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prod_filter);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken","");
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        pf_prodName = " ";
        btnP_Filterback = (Button) findViewById(R.id.imageBtnBack);
        btnP_Filterback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Prod_FilterActivity.this, KeyProductActivity.class);
                startActivity(intent);
                finish();
            }
        });
//
            pfilter_list = (ExpandableListView) findViewById(R.id.expandableListView_subdept);
            //prepareListData();

        if (Reusable_Functions.chkStatus(Prod_FilterActivity.this)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(Prod_FilterActivity.this, "Loading  data...");
            offsetvalue = 0;
            count = 0;
            prepareListData();
            requestSubDeptAPI(offsetvalue, limit);
        } else {

            Toast.makeText(Prod_FilterActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
       }


            listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

            // setting list adapter
            pfilter_list.setAdapter(listAdapter);

            // Listview Group click listener
            pfilter_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                @Override
                public boolean onGroupClick(ExpandableListView parent, View v,
                                            int groupPosition, long id) {

                    return false;
                }
            });

            // Listview Group expanded listener
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
//
                }
            });

            // Listview on child click listener
            pfilter_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {
                    // TODO Auto-generated method stub

                    Log.e("Onclick Child-----","");

                    Toast.makeText(
                            getApplicationContext(),
                            listDataHeader.get(groupPosition)
                                    + " : "
                                    + listDataChild.get(
                                    listDataHeader.get(groupPosition)).get(
                                    childPosition), Toast.LENGTH_SHORT)
                            .show();
                    subdeptName =  listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                    Log.e("subDeptName"," "+subdeptName);


                    switch (groupPosition) {
                        case 0:

                            if (Reusable_Functions.chkStatus(Prod_FilterActivity.this)) {
                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(Prod_FilterActivity.this, "Loading  data...");
                                offsetvalue = 0;
                                count = 0;
                                productList= new ArrayList<String>();
                                requestSearchSubDeptAPI(offsetvalue, limit);
                                //listAdapter.notifyDataSetChanged();
                            } else {
                                // Reusable_Functions.hDialog();
                                Toast.makeText(Prod_FilterActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
                            }
                            break;
                        case 1:
                            pf_prodName=listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);

                            Intent intent= new Intent(Prod_FilterActivity.this,KeyProductActivity.class);
                            intent.putExtra("filterproductname",pf_prodName);
                            startActivity(intent);
                            finish();
                            break;
                    }
                    return false;
                }
            });
       // listAdapter.notifyDataSetChanged();
        }
    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Sub Dept");
        listDataHeader.add("Product");
       // Adding child data
       //subdeptlist = new ArrayList<String>();
      //  productlist = new ArrayList<String>();


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
                                Toast.makeText(Prod_FilterActivity.this, "no data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {

                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);

                                    String prodLevel3Code = productName1.getString("prodLevel3Code");
                                    String prodLevel3Desc = productName1.getString("prodLevel3Desc");
//
                                    subdept.add(prodLevel3Desc);

                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSubDeptAPI(offsetvalue, limit);
                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String prodLevel3Code = productName1.getString("prodLevel3Code");
                                    String prodLevel3Desc = productName1.getString("prodLevel3Desc");
                                    subdept.add(prodLevel3Desc);
                                    // Log.e("ArrayList", "" + productsubdeptList.size());
                                }

                            }
                            Collections.sort(subdept);
                            listDataChild.put(listDataHeader.get(0), subdept);

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
                params.put("Authorization","Bearer "+bearertoken);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void requestSearchSubDeptAPI(int offsetvalue1, int limit1) {

        //  Toast.makeText(context, userId,Toast.LENGTH_SHORT).show();
        String url = ConstsCore.web_url + "/v1/display/hourlytransproducts/" + userId + "?view=productName&prodLevel3Desc="+subdeptName.replace(" ", "%20") + "&offset" + offsetvalue + "&limit" + limit;
        Log.i("URL   ", url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Sub Dept Response", response.toString());
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(Prod_FilterActivity.this, "no data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {
                                Reusable_Functions.hDialog();

                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String ProductName = productName1.getString("productName");
                                    Log.e("Product Name:", ProductName);

                                    productList.add(ProductName);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSearchSubDeptAPI(offsetvalue, limit);
                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String ProductName = productName1.getString("productName");
                                    Log.e("Product Name:", ProductName);

                                    productList.add(ProductName);

                                }
                           }
                            Collections.sort(productList);
                            listDataChild.put(listDataHeader.get(1), productList);

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
                        // Toast.makeText(LoginActivity.this,"Invalid User",Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization","Bearer "+bearertoken);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);


    }

}
