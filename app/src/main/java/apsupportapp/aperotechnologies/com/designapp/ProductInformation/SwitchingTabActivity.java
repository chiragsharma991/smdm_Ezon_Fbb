package apsupportapp.aperotechnologies.com.designapp.ProductInformation;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import android.widget.RelativeLayout;

import apsupportapp.aperotechnologies.com.designapp.BestPerformersInventory.BestPerformerInventory;


import apsupportapp.aperotechnologies.com.designapp.DashboardSnap.SnapDashboardActivity;
import apsupportapp.aperotechnologies.com.designapp.FloorAvailability.FloorAvailabilityActivity;
import apsupportapp.aperotechnologies.com.designapp.HorlyAnalysis.KeyProductActivity;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.SellThruExceptions.SaleThruInventory;
import apsupportapp.aperotechnologies.com.designapp.SkewedSize.SkewedSizesActivity;
import apsupportapp.aperotechnologies.com.designapp.StockAgeing.StockAgeingActivity;
import apsupportapp.aperotechnologies.com.designapp.VisualAssortmentSwipe.VisualAssortmentActivity;

import static apsupportapp.aperotechnologies.com.designapp.ProductInformation.StyleActivity.selStoreName;
import static apsupportapp.aperotechnologies.com.designapp.ProductInformation.StyleActivity.selcollectionName;
import static apsupportapp.aperotechnologies.com.designapp.ProductInformation.StyleActivity.seloptionName;
import static apsupportapp.aperotechnologies.com.designapp.ProductInformation.StyleActivity.styleactivity;

public class SwitchingTabActivity extends AppCompatActivity {

    RelativeLayout backButton, imageBtnHomePage;
    public static Activity switchingTabActivity;
    public static ViewPager viewPager;
    public static TabLayout tabLayout;
    SharedPreferences sharedPreferences;
    String kpi_id, storeCode, articleOptionCode, check, content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_tab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar().hide();
        getSupportActionBar().setElevation(0);
        switchingTabActivity = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if(getIntent().getExtras() != null)
        {
            if(getIntent().getExtras().getString("storeCode") != null)
            {
                storeCode = getIntent().getExtras().getString("storeCode");
                articleOptionCode = getIntent().getExtras().getString("articleOptionCode");
                check = getIntent().getExtras().getString("check");
                content = getIntent().getExtras().getString("content");


            }

        }
        kpi_id = sharedPreferences.getString("kpi_id", "");

        backButton = (RelativeLayout) findViewById(R.id.imageBtnBack1);
        imageBtnHomePage = (RelativeLayout) findViewById(R.id.imageBtnHomePage);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             onBackPressed();
            }
        });

        imageBtnHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                selStoreName = null;
                selcollectionName = null;
                seloptionName = null;
                styleactivity.finish();

//                Intent intent = new Intent(SwitchingTabActivity.this, SnapDashboardActivity.class);
//                String[] kpiIdArray = kpi_id.split(",");
//                intent.putExtra("kpiId", kpiIdArray);
//                startActivity(intent);
//                finish();
//                if(getIntent().getStringExtra("checkFrom").equals("bestInventory"))
//                {
//                    BestPerformerInventory.bestperoformer.finish();
//
//                }

            }
        });
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText("Option"));
        tabLayout.addTab(tabLayout.newTab().setText("Style Size"));
        tabLayout.setTabTextColors(Color.parseColor("#e8112d"),Color.parseColor("#e8112d"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) findViewById(R.id.pager);
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), storeCode, articleOptionCode, check, content, getIntent().getExtras().getString("selStoreName"));
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(getIntent().getStringExtra("checkFrom").equals("SkewedActivity"))
        {
            Intent intent = new Intent(SwitchingTabActivity.this, SkewedSizesActivity.class);
            startActivity(intent);
            finish();
        }
        else if(getIntent().getStringExtra("checkFrom").equals("floor_availability"))
        {
            Intent intent = new Intent(SwitchingTabActivity.this, FloorAvailabilityActivity.class);
            startActivity(intent);
            finish();
        }
        else if(getIntent().getStringExtra("checkFrom").equals("stockAgeing"))
        {
            Intent intent = new Intent(SwitchingTabActivity.this, StockAgeingActivity.class);
            startActivity(intent);
            finish();
        }
        else if(getIntent().getStringExtra("checkFrom").equals("sell_thru_exception"))
        {
            Intent intent = new Intent(SwitchingTabActivity.this, SaleThruInventory.class);
            startActivity(intent);
            finish();
        }
        else if(getIntent().getStringExtra("checkFrom").equals("topCut"))
        {
            Intent intent = new Intent(SwitchingTabActivity.this, SaleThruInventory.class);
            startActivity(intent);
            finish();
        }
        else if(getIntent().getStringExtra("checkFrom").equals("option_fragment"))
        {
            Intent intent = new Intent(SwitchingTabActivity.this, KeyProductActivity.class);
            startActivity(intent);
            finish();
        }
        else if(getIntent().getStringExtra("checkFrom").equals("visualAssortment"))
        {
            Intent intent = new Intent(SwitchingTabActivity.this, VisualAssortmentActivity.class);
            startActivity(intent);
            finish();
        }
        else if(getIntent().getStringExtra("checkFrom").equals("bestInventory"))
        {
          finish();
        }
        else if(getIntent().getStringExtra("checkFrom").equals("styleActivity"))
        {
            Intent intent = new Intent(SwitchingTabActivity.this, StyleActivity.class);
            intent.putExtra("from", "");
            intent.putExtra("selStoreName",getIntent().getExtras().getString("selStoreName"));
            intent.putExtra("selCollectionname", getIntent().getExtras().getString("selCollectionname"));
            intent.putExtra("selOptionName", getIntent().getExtras().getString("selOptionName"));



            startActivity(intent);
            finish();
        }
    }
}