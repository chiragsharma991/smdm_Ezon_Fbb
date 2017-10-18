package apsupportapp.aperotechnologies.com.designapp.InternalServiceOppAudit;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import apsupportapp.aperotechnologies.com.designapp.R;

public class ReportActivity extends AppCompatActivity {

    private Context context;
    TabLayout tab_reports;
    private RelativeLayout imageBtnBack1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        getSupportActionBar().hide();

        context = this;

        tab_reports = (TabLayout) findViewById(R.id.tab_reports);
        tab_reports.addTab(tab_reports.newTab().setText("FG store"));
        tab_reports.addTab(tab_reports.newTab().setText("Competitor store"));

        imageBtnBack1 = (RelativeLayout) findViewById(R.id.imageBtnBack1);

        imageBtnBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        final ReportsAdapter adapter = new ReportsAdapter
                (getSupportFragmentManager(), tab_reports.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_reports));

        tab_reports.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int check_pos = tab.getPosition();
//                Log.e("check_pos "," "+check_pos);
                viewPager.setCurrentItem(check_pos);

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
