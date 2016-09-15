package apsupportapp.aperotechnologies.com.designapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import apsupportapp.aperotechnologies.com.designapp.R;

import java.util.ArrayList;

/**
 * Created by pamrutkar on 23/08/16.
 */
public class KeyProductActivityWork extends AppCompatActivity implements View.OnClickListener, OnRowPressListener {
    Button btnBack, btnSearch;
    public static String prodName = "";
    private final String LOG_TAG = "MainActivity";
    KeyProductAdapter adapter;
    TabLayout tabLayout;
    MySingleton m_config;
    int offsetvalue=0,limit=100;
    int count=0;
    Context context;
    ProductNameBean productNameBean;
    ArrayList<ProductNameBean> productNameBeanArrayList;
    RequestQueue queue;
    public static CustomViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        // getSupportActionBar().hide();
        setContentView(R.layout.activity_key_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar().hide();
        context = this;
        m_config= MySingleton.getInstance(context);

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

        // final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager = (CustomViewPager) findViewById(R.id.pager);


        adapter = new KeyProductAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

//        if(!SearchActivity1.searchArticleOption.equals(""))
//        {
//            tabLayout.getTabAt(1).select();
//            viewPager.setCurrentItem(1);
//        }


//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
                Log.e("currentItem "," "+viewPager.getCurrentItem() +" "+ KeyProductActivityWork.prodName);

                if(viewPager.getCurrentItem() == 1 && KeyProductActivityWork.prodName.equals(""))
                {
                    //option tab click
                    Fragment fragment = new Option_Fragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.child_fragment_container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                }

                if(viewPager.getCurrentItem() == 0 && KeyProductActivityWork.prodName.equals(""))
                {
                    //option productname click for click of productname to option
                    Fragment fragment = new ProductName_Fragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.child_fragment_container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        btnBack = (Button) findViewById(R.id.imageBtnBack);
        btnBack.setOnClickListener(this);
        btnSearch = (Button) findViewById(R.id.imageBtnSearch);
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
                SearchActivity1.searchSubDept = ""; SearchActivity1.searchProductName = "";  SearchActivity1.searchArticleOption = "";
                Intent intent = new Intent(KeyProductActivityWork.this, DashBoardActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.imageBtnSearch:
                Intent intent1 = new Intent(KeyProductActivityWork.this, SearchActivity1.class);
                startActivity(intent1);
                finish();
                break;
        }
    }


    public void communicateToFragment2(String productName) {

        Log.e("product ==== ", " " + productName);
        Log.e("adapter.getFragment(1) ", " " + adapter);

        KeyProductActivityWork.prodName = productName;


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

        //KeyProductActivity.prodName = productName;


        Sku_Fragment sku_fragment = (Sku_Fragment) adapter.getFragment(2);
        if (sku_fragment != null) {
            sku_fragment.fragmentCommunication1(productName, articleOption);
        } else {
            Log.i(LOG_TAG, "Fragment 2 is not initialized");

        }

    }

    @Override
    public void onBackPressed() {

        Log.e("currentItem---"," "+viewPager.getCurrentItem());

//        if(viewPager.getCurrentItem() == 2)
//        {
//            Sku_Fragment.skurel.setVisibility(View.GONE);
//            Option_Fragment.relopt.setVisibility(View.VISIBLE);
//        }
//        else if(viewPager.getCurrentItem() == 1)
//        {
//            Option_Fragment.relopt.setVisibility(View.GONE);
//        }
//        else
//        {
            Reusable_Functions.hDialog();
            SearchActivity1.searchSubDept = ""; SearchActivity1.searchProductName = "";  SearchActivity1.searchArticleOption = "";
            Intent intent = new Intent(KeyProductActivityWork.this, DashBoardActivity.class);
            startActivity(intent);
            finish();
            //super.onBackPressed();
        //}


//        Reusable_Functions.hDialog();
//        SearchActivity1.searchSubDept = ""; SearchActivity1.searchProductName = "";  SearchActivity1.searchArticleOption = "";
//        Intent intent = new Intent(KeyProductActivity.this, DashBoardActivity.class);
//        startActivity(intent);
//        super.onBackPressed();
    }
}

