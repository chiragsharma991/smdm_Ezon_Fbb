package apsupportapp.aperotechnologies.com.designapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

import java.util.ArrayList;


public class KeyProductActivity extends AppCompatActivity implements View.OnClickListener, OnRowPressListener {
    RelativeLayout btnBack;
    RelativeLayout btnSearch;
    public static String prodName = "";
    private final String LOG_TAG = "MainActivity";
    KeyProductAdapter adapter;
    TabLayout tabLayout;
    MySingleton m_config;
    int offsetvalue = 0, limit = 100;
    int count = 0;
    Context context;
    ProductNameBean productNameBean;
    ArrayList<ProductNameBean> productNameBeanArrayList;
    RequestQueue queue;
    public static CustomViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_product);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar().hide();
        context = this;
        m_config = MySingleton.getInstance(context);

        Log.e("here ", "onCreate");

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Product Name"));
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

                Log.e("currentItem ", " " + viewPager.getCurrentItem() + " " + KeyProductActivity.prodName + " " + (viewPager.getCurrentItem() == 0 && !KeyProductActivity.prodName.equals("")));


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
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageBtnBack:
                Reusable_Functions.hDialog();
                SearchActivity1.searchSubDept = "";
                SearchActivity1.searchProductName = "";
                SearchActivity1.searchArticleOption = "";
                Intent intent = new Intent(KeyProductActivity.this, DashBoardActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.imageBtnSearch:
                Intent intent1 = new Intent(KeyProductActivity.this, SearchActivity1.class);
                startActivity(intent1);
                finish();
                break;
        }
    }


    public void communicateToFragment2(String productName) {

        Log.e("product ==== ", " " + productName);
        Log.e("adapter.getFragment(1) ", " " + adapter);
        KeyProductActivity.prodName = productName;

        Option_Fragment fragment = (Option_Fragment) adapter.getFragment(1);
        if (fragment != null) {
            fragment.fragmentCommunication(productName);
        } else {
            Log.i(LOG_TAG, "Fragment 2 is not initialized");

        }
    }

    public void communicateToFragment(String productName, String articleOption) {

        Log.e("product ==== ", " " + productName);
        Log.e("adapter.getFragment(1) ", " " + adapter);

        Sku_Fragment sku_fragment = (Sku_Fragment) adapter.getFragment(2);
        if (sku_fragment != null) {
            sku_fragment.fragmentCommunication1(productName, articleOption);
        } else {
            Log.i(LOG_TAG, "Fragment 2 is not initialized");
        }
    }

    @Override
    public void onBackPressed() {

        Log.e("currentItem---", " " + viewPager.getCurrentItem());
        Reusable_Functions.hDialog();
        SearchActivity1.searchSubDept = "";
        SearchActivity1.searchProductName = "";
        SearchActivity1.searchArticleOption = "";
        Intent intent = new Intent(KeyProductActivity.this, DashBoardActivity.class);
        startActivity(intent);
        finish();
    }
}

