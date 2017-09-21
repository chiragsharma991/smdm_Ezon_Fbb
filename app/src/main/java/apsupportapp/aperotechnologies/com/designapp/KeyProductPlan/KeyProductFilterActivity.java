package apsupportapp.aperotechnologies.com.designapp.KeyProductPlan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import android.widget.EditText;

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


import java.util.ArrayList;
import java.util.Arrays;

import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.MySingleton;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;

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


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_product_filter);
        getSupportActionBar().hide();
        m_config = MySingleton.getInstance(context);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        context = this;
        initialize();

        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading  data...");
            offsetvalue = 0;
            count = 0;
            requestProductListAPI(offsetvalue, limit);
        } else {

            Toast.makeText(KeyProductFilterActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
        }

        keyprod_editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                search_data =  keyprod_editSearch.getText().toString();

                keyprod_editSearch.clearFocus();
                InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow( keyprod_editSearch.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                adapter.getFilter().filter(search_data);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search_data =  keyprod_editSearch.getText().toString();

                keyprod_editSearch.clearFocus();
                InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow( keyprod_editSearch.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                adapter.getFilter().filter(search_data);
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                search_data =  keyprod_editSearch.getText().toString();
                keyprod_editSearch.clearFocus();
                InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow( keyprod_editSearch.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                adapter.getFilter().filter(search_data);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.keyprodfilter_btnBack:
                onBackPressed();
                break;
            case R.id.keyproduct_btnfilterdone:
                Intent intent = new Intent(KeyProductFilterActivity.this,KeyProductPlanActivity.class);
                String filtervalue =  Arrays.toString(KeyProdFilterAdapter.checkedValue.toArray());
                filtervalue = filtervalue.replace("[","");
                String updatedfilterValue = filtervalue.replace("]","");
                updatedfilterValue = updatedfilterValue.replace(", ",",");
                intent.putExtra("productfilterValue",updatedfilterValue);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void requestProductListAPI(int offsetvalue2, final int limit2) {

        String url = ConstsCore.web_url + "/v1/display/keyproducts/" + userId + "?offset=" + offsetvalue + "&limit=" + limit;
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(KeyProductFilterActivity.this,KeyProductPlanActivity.class);
        startActivity(intent);
        finish();
    }
}
