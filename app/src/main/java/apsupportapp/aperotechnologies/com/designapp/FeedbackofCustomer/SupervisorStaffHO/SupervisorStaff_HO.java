package apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.SupervisorStaffHO;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.SupervisorStaffHO.Adapter.SupervisorStaff_ViewPagerAdapter;
import apsupportapp.aperotechnologies.com.designapp.R;

public class SupervisorStaff_HO extends AppCompatActivity {
    TabLayout tabLayout;
    private RelativeLayout imageBtnBack1;
    private Context context;
    private String storeCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_staff_ho);

        getSupportActionBar().hide();
        getSupportActionBar().setElevation(0);
        context = this;
        imageBtnBack1 = (RelativeLayout) findViewById(R.id.imageBtnBack1);
        if(getIntent().getExtras().getString("storeCode") != null )
        {
            storeCode = getIntent().getExtras().getString("storeCode");
        }
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Reports"));
        tabLayout.addTab(tabLayout.newTab().setText("New Feedback"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#e8112d"));

        imageBtnBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final SupervisorStaff_ViewPagerAdapter adapter = new SupervisorStaff_ViewPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(), storeCode);
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

    }
}
