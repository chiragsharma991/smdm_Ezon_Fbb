package apsupportapp.aperotechnologies.com.designapp;

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

import apsupportapp.aperotechnologies.com.designapp.HorlyAnalysis.KeyProductActivity;

/**
 * Created by pamrutkar on 07/09/16.
 */
@SuppressWarnings("ALL")
public class Prod_FilterActivity extends Activity {


    RelativeLayout btnP_Filterback;
    ExpandableListAdapter listAdapter;
    public static ExpandableListView pfilter_list;
    ArrayList<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    int offsetvalue = 0, limit = 100, count = 0;
    String userId, bearertoken;
    SharedPreferences sharedPreferences;
    RequestQueue queue;
    List<String> subdept;
    String pf_prodName = " ";
    public static Activity filterActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prod_filter);
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
        subdept = new ArrayList<String>();
        btnP_Filterback = (RelativeLayout) findViewById(R.id.imageBtnBack);

        pfilter_list = (ExpandableListView) findViewById(R.id.expandableListView_subdept);
        //noinspection deprecation,deprecation
        pfilter_list.setDivider(getResources().getDrawable(R.color.grey));
        pfilter_list.setDividerHeight(2);
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild, pfilter_list);

        pfilter_list.setChoiceMode(ExpandableListView.CHOICE_MODE_MULTIPLE);
        // setting list adapter
        pfilter_list.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();

        // Listview Group click listener
        pfilter_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return false;
            }
        });

        btnP_Filterback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Prod_FilterActivity.this, KeyProductActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding group name data
        listDataHeader.add("Sub Dept");
        listDataHeader.add("Product");

        if (Reusable_Functions.chkStatus(Prod_FilterActivity.this)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(Prod_FilterActivity.this, "Loading  data...");
            offsetvalue = 0;
            count = 0;
            requestSubDeptAPI(offsetvalue, limit);
        } else {

            Toast.makeText(Prod_FilterActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
        }
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
        Intent i = new Intent(Prod_FilterActivity.this, KeyProductActivity.class);
        startActivity(i);
        finish();
    }
}
