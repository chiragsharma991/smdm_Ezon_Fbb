package apsupportapp.aperotechnologies.com.designapp.KeyProductPlan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.google.gson.Gson;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.CustomViewPager;
import apsupportapp.aperotechnologies.com.designapp.MySingleton;
import apsupportapp.aperotechnologies.com.designapp.R;


public class KeyProductPlanActivity extends AppCompatActivity implements View.OnClickListener,OnRowPressListener1 {
    RelativeLayout btnBack,btnFilter;
    KeyProductPlanAdapter adapter;
    TabLayout tabLayout;
    MySingleton m_config;
    Context context;
    static String productName = "";
    RequestQueue queue;
    public static CustomViewPager plan_pager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_actual_product);
        context = this;
        m_config = MySingleton.getInstance(context);
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        btnBack = (RelativeLayout) findViewById(R.id.planactual_BtnBack);
        btnBack.setOnClickListener(this);

        btnFilter = (RelativeLayout) findViewById(R.id.planactual_BtnFilter);
        btnFilter.setOnClickListener(this);

        tabLayout = (TabLayout) findViewById(R.id.planactual_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Product Name "));
        tabLayout.addTab(tabLayout.newTab().setText("Option"));
        tabLayout.addTab(tabLayout.newTab().setText("SKU"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        plan_pager = (CustomViewPager) findViewById(R.id.planactual_pager);
        adapter = new KeyProductPlanAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        plan_pager.setAdapter(adapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                plan_pager.setCurrentItem(tab.getPosition());

                if (plan_pager.getCurrentItem() == 1 && KeyProductPlanActivity.productName.equals("")) {
                    View view=findViewById(android.R.id.content);
                    Snackbar.make(view, "Please select product to view options", Snackbar.LENGTH_LONG).show();
                }

                if (plan_pager.getCurrentItem() == 0 && !KeyProductPlanActivity.productName.equals(""))
                {
                   Plan_Product.relPlanProd_Frag.setVisibility(View.VISIBLE);
                   Plan_Option_Fragment.tableAPlanOpt_Frag.removeAllViews();
                   Plan_Option_Fragment.tableBPlanOpt_Frag.removeAllViews();
                   Plan_Option_Fragment.tableCPlanOpt_Frag.removeAllViews();
                   Plan_Option_Fragment.tableDPlanOpt_Frag.removeAllViews();
                   Plan_Option_Fragment.optionview.removeView(Plan_Option_Fragment.optrel);
                   KeyProductPlanActivity.productName = "";
                   LinearLayout layout = (LinearLayout)plan_pager.getParent();
                   TabLayout tab1 = (TabLayout) layout.getChildAt(1);
                    if(tab1.getTabCount() == 2)
                    {
                        tab1.removeTabAt(1);
                    }
               }
           }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });



    }

    public void communicateToFragment2(String productName, String segClick) {


        KeyProductPlanActivity.productName = productName;
        Plan_Option_Fragment fragment = (Plan_Option_Fragment) adapter.getFragment(1);
        if (fragment != null) {
            fragment.fragmentCommunication(productName,segClick);
        }
    }



    @Override
    public void communicateToFragment3(String level,String optsegmentclick) {

        Plan_SKU_Fragment fragment = ( Plan_SKU_Fragment) adapter.getFragment(2);
        if (fragment != null) {
            fragment.fragment_Communication(level,optsegmentclick);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.planactual_BtnBack:



               onBackPressed();
                break;
            case R.id.planactual_BtnFilter:
                Intent intent1 = new Intent(KeyProductPlanActivity.this,KeyProductFilterActivity.class);
                startActivity(intent1);
                finish();
                break;
        }
    }




    @Override
    public void onBackPressed() {
       KeyProductPlanActivity.productName = "";

       KeyProdFilterAdapter.checkedValue = new ArrayList<String>();
        Plan_Product.prodsegClick = "";
        Plan_Product.prodsegClick = "WTD";
        finish();
    }
}

