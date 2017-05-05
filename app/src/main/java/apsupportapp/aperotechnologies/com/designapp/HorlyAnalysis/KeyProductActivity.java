package apsupportapp.aperotechnologies.com.designapp.HorlyAnalysis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.CustomViewPager;
import apsupportapp.aperotechnologies.com.designapp.MySingleton;
import apsupportapp.aperotechnologies.com.designapp.OnRowPressListener;
import apsupportapp.aperotechnologies.com.designapp.ProductInformation.SearchActivity1;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.model.EtlStatus;


public class KeyProductActivity extends AppCompatActivity implements View.OnClickListener,OnRowPressListener {
    RelativeLayout btnBack;
    RelativeLayout btnSearch;
    public static String prodName = "";
    KeyProductAdapter adapter;
    TabLayout tabLayout;
    MySingleton m_config;
    Context context;
    RequestQueue queue;
    public static CustomViewPager viewPager;
    private TextView RefreshTime;
    String userId, bearertoken;
    private EtlStatus etlStatus;
    SharedPreferences sharedPreferences;
    private ArrayList<EtlStatus> etlStatusList;
    private Gson gson;
    public static Activity key_product_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_product);
        getSupportActionBar().hide();
        context = this;
        key_product_activity = this;
        m_config = MySingleton.getInstance(context);
        intialize();
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        userId = sharedPreferences.getString("userId","");
        bearertoken = sharedPreferences.getString("bearerToken","");
        gson = new Gson();
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Product Name "));
        tabLayout.addTab(tabLayout.newTab().setText("Option"));
        tabLayout.addTab(tabLayout.newTab().setText("SKU"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (CustomViewPager) findViewById(R.id.pager);
        adapter = new KeyProductAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
                if (viewPager.getCurrentItem() == 1 && KeyProductActivity.prodName.equals("")) {
                    Toast.makeText(context, "Please select product to view options", Toast.LENGTH_LONG).show();
                }
                if (viewPager.getCurrentItem() == 0 && !KeyProductActivity.prodName.equals("")) {
                    ProductName_Fragment.relProd_Frag.setVisibility(View.VISIBLE);
                    Option_Fragment.tableAOpt_Frag.removeAllViews();
                    Option_Fragment.tableBOpt_Frag.removeAllViews();
                    Option_Fragment.tableCOpt_Frag.removeAllViews();
                    Option_Fragment.tableDOpt_Frag.removeAllViews();
                    Option_Fragment.view.removeView(Option_Fragment.rel);
                    KeyProductActivity.prodName = "";
                    LinearLayout layout = (LinearLayout)viewPager.getParent();
                    TabLayout tab1 = (TabLayout) layout.getChildAt(1);
                    if(tab1.getTabCount() == 2)
                    {
                        tab1.removeTabAt(1);
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {

            }
        });

        RefreshTimeAPI();
    }

    private void RefreshTimeAPI()
    {
        String url = ConstsCore.web_url + "/v1/display/etlstatus/" + userId;
        etlStatusList = new ArrayList<EtlStatus>();
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response == null || response.equals(null)) {
                                RefreshTime.setText("N/A");
                            } else {

                                for (int i = 0; i < response.length(); i++)
                                {

                                    etlStatus = gson.fromJson(response.get(i).toString(), EtlStatus.class);
                                    etlStatusList.add(etlStatus);

                                }
                                RefreshTime.setText(etlStatusList.get(1).getLastETLDate());
                            }

                        } catch (Exception e) {
                            RefreshTime.setText("N/A");

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)

                    {
                        RefreshTime.setText("N/A");
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


    private void intialize() {
        RefreshTime = (TextView) findViewById(R.id.refreshTime);
        btnBack = (RelativeLayout) findViewById(R.id.imageBtnBack);
        btnBack.setOnClickListener(this);
        btnSearch = (RelativeLayout) findViewById(R.id.imageBtnSearch);
        btnSearch.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.imageBtnBack:
                Reusable_Functions.hDialog();
                SearchActivity1.searchSubDept = "";
                SearchActivity1.searchProductName = "";
                SearchActivity1.searchArticleOption = "";
                finish();
                break;
            case R.id.imageBtnSearch:
                Intent intent1 = new Intent(KeyProductActivity.this, SearchActivity1.class);
                startActivity(intent1);
                finish();
                break;
        }
    }

    // interface to communicate from product fragment to option fragment
    public void communicateToFragment2(String productName) {

        KeyProductActivity.prodName = productName;
        Option_Fragment fragment = (Option_Fragment) adapter.getFragment(1);
        if (fragment != null) {
            fragment.fragmentCommunication(productName);
        } else
        {

        }
    }

    // interface to communicate from option fragment to sku fragment
    public void communicateToFragment(String productName, String articleOption) {

        Sku_Fragment sku_fragment = (Sku_Fragment) adapter.getFragment(2);
        if (sku_fragment != null) {
            sku_fragment.fragmentCommunication1(productName, articleOption);
        } else
        {

        }
    }

    @Override
    public void onBackPressed() {

        Reusable_Functions.hDialog();
        SearchActivity1.searchSubDept = "";
        SearchActivity1.searchProductName = "";
        SearchActivity1.searchArticleOption = "";
        finish();
    }
}

