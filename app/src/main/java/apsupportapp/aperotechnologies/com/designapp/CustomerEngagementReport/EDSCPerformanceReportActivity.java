package apsupportapp.aperotechnologies.com.designapp.CustomerEngagementReport;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import apsupportapp.aperotechnologies.com.designapp.CustomerEngagementReport.Adapter.EdscReportAdapter;
import apsupportapp.aperotechnologies.com.designapp.InfantApp.Adapter.HomeAdapter;
import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by pamrutkar on 07/11/17.
 */

public class EDSCPerformanceReportActivity extends AppCompatActivity implements View.OnClickListener
{
    private Context context;
    private RelativeLayout rel_back;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edsc_performance);
        getSupportActionBar().hide();
        context = this;
        intialise_ui();


    }

    private void intialise_ui()
    {
        rel_back = (RelativeLayout)findViewById(R.id.rel_back);
        tabLayout = (TabLayout) findViewById(R.id.edsc_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Registration Report"));
        tabLayout.addTab(tabLayout.newTab().setText("Membership Renewal"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#e8112d"));
        viewPager = (ViewPager) findViewById(R.id.edsc_pager);
        final EdscReportAdapter adapter = new EdscReportAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {


                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {


            }
        });



        rel_back.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.rel_back:
                onBackPressed();
                break;
        }

    }
}
