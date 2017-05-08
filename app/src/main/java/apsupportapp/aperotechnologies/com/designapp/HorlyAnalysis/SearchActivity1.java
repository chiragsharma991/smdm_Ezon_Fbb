package apsupportapp.aperotechnologies.com.designapp.HorlyAnalysis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.Map;
import java.util.Objects;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.MySingleton;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import info.hoang8f.android.segmented.SegmentedGroup;


public class SearchActivity1 extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    RelativeLayout btnBack;
    EditText editSearch;
    ImageButton btnSearch;
    MySingleton m_config;
    String searchData;
    String activeId;
    ListView listView, listView1;
    RequestQueue queue;
    RadioButton radioButton;
    ArrayList<String> productsubdeptList, productNameList, productArticleList;
    ValueAdapter adapter;
    Context context;
    String userId, bearertoken;
    SharedPreferences sharedPreferences;
    int offsetvalue = 0, limit = 100, offsetvalue1 = 0, limit1 = 100;
    int count = 0, count1 = 0;
    public static String searchSubDept = "", searchProductName = "", searchArticleOption = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search1);
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
        searchSubDept = "";
        searchProductName = "";
        searchArticleOption = "";
        radioButton = (RadioButton) findViewById(R.id.button31);
        radioButton.toggle();
        SegmentedGroup segmented3 = (SegmentedGroup) findViewById(R.id.segmented3);
        segmented3.setOnCheckedChangeListener(this);
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading  data...");
            offsetvalue = 0;
            count = 0;
            requestSubDeptAPI(offsetvalue, limit);
        } else
        {
            Toast.makeText(SearchActivity1.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
        }

        activeId = "SubDept";
        context = this;
        productsubdeptList = new ArrayList<>();

        productNameList = new ArrayList<>();
        productArticleList = new ArrayList<>();

        listView = (ListView) findViewById(R.id.list);
        btnBack = (RelativeLayout) findViewById(R.id.imageBtnBack);
        btnBack.setOnClickListener(this);
        btnSearch = (ImageButton) findViewById(R.id.btnSeatchList);
        btnSearch.setOnClickListener(this);
        editSearch = (EditText) findViewById(R.id.editSearch);

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getFilter().filter(s);
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.imageBtnBack:
                Intent i = new Intent(SearchActivity1.this, KeyProductActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.btnSeatchList:

                searchData = editSearch.getText().toString();
                editSearch.clearFocus();
                InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);
                adapter.getFilter().filter(searchData);
                break;
            default:
                break;
        }
    }
    /*-------------------------API Declaration ----------------------------*/

    public void requestSubDeptAPI(int offsetvalue1, int limit1)
    {
        String url = ConstsCore.web_url + "/v1/display/hourlyfilterproducts/" + userId + "?offset=" + offsetvalue1 + "&limit=" + limit1;
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(SearchActivity1.this, "no data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {

                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String prodLevel3Desc = productName1.getString("prodLevel3Desc");
                                    productsubdeptList.add(prodLevel3Desc);

                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSubDeptAPI(offsetvalue, limit);
                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String prodLevel3Desc = productName1.getString("prodLevel3Desc");
                                    productsubdeptList.add(prodLevel3Desc);
                                }

                                Collections.sort(productsubdeptList);
                                adapter = new ValueAdapter(productsubdeptList, getApplicationContext());
                                listView.setAdapter(adapter);
                                listView.setTextFilterEnabled(true);
                                Reusable_Functions.hDialog();

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        String productSubDeptItem = (String) parent.getItemAtPosition(position);
                                        searchSubDept = productSubDeptItem.replaceAll(" ", "%20").replaceAll("&", "%26");
                                        Intent intent = new Intent(SearchActivity1.this, KeyProductActivity.class);
                                        startActivity(intent);
                                        finish();
                                     }
                                });
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

    private void requestProductListAPI(int offsetvalue2, final int limit2)
    {
        String url = ConstsCore.web_url + "/v1/display/hourlyfilterproducts/" + userId + "?view=PNF&offset=" + offsetvalue1 + "&limit=" + limit1;
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count1 == 0)
                            {
                                Reusable_Functions.hDialog();
                                Toast.makeText(SearchActivity1.this, "no data found", Toast.LENGTH_LONG).show();
                            }
                            else if (response.length() == limit1)
                            {
                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++)
                                {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String productName = productName1.getString("productName");
                                    productNameList.add(productName);
                                }
                                offsetvalue1 = (limit1 * count1) + limit1;
                                count1++;
                                requestProductListAPI(offsetvalue1, limit1);
                            }
                            else if (response.length() < limit1)
                            {
                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++)
                                {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String productName = productName1.getString("productName");
                                    productNameList.add(productName);
                                }
                                Collections.sort(productNameList);
                                adapter = new ValueAdapter(productNameList, getApplicationContext());
                                listView1.setAdapter(adapter);
                                listView1.setTextFilterEnabled(true);
                                Reusable_Functions.hDialog();
                                listView1.setOnItemClickListener(new AdapterView.OnItemClickListener()
                                {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                                    {
                                        String productNameItem = (String) parent.getItemAtPosition(position);
                                        searchProductName = productNameItem.replaceAll(" ", "%20").replaceAll("&", "%26");
                                        Intent intent = new Intent(SearchActivity1.this, KeyProductActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
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
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        switch (checkedId)
        {
            case R.id.button31:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                {
                    if (Objects.equals(activeId, "SubDept"))
                        break;
                }
                activeId = "SubDept";
                productsubdeptList.clear();
                listView = (ListView) findViewById(R.id.list);
                if (Reusable_Functions.chkStatus(context))
                {
                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading  data...");
                    offsetvalue = 0;
                    count = 0;
                    requestSubDeptAPI(offsetvalue, limit);
                }
                else
                {
                    Toast.makeText(SearchActivity1.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.button32:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                {
                    if (Objects.equals(activeId, "ProductName"))
                    break;
                }

                activeId = "ProductName";
                productNameList.clear();
                listView1 = (ListView) findViewById(R.id.list);
                if (Reusable_Functions.chkStatus(context))
                {
                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading  data...");
                    offsetvalue1 = 0;
                    count1 = 0;
                    requestProductListAPI(offsetvalue1, limit1);
                }
                else
                {
                    Toast.makeText(SearchActivity1.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
                }

                break;
            default:
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(SearchActivity1.this, KeyProductActivity.class);
        startActivity(i);
        finish();

    }

}