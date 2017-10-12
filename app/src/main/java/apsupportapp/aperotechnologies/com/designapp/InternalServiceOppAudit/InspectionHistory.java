package apsupportapp.aperotechnologies.com.designapp.InternalServiceOppAudit;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import apsupportapp.aperotechnologies.com.designapp.R;

public class InspectionHistory extends AppCompatActivity {

    private Context context;
    TabLayout tab_inspection;
//    ListView list_inspectionHistory;
//    InspectionHistoryAdapter adp_inspectionhistory;
//    List<InspectionHistoryZonalRatings> arr_zonalratings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspectionhistory);
        context = this;

        tab_inspection = (TabLayout) findViewById(R.id.tab_inspection);
        tab_inspection.addTab(tab_inspection.newTab().setText("Last Week"));
        tab_inspection.addTab(tab_inspection.newTab().setText("Last Month"));
        tab_inspection.addTab(tab_inspection.newTab().setText("Last Year"));
//        tab_inspection.setupWithViewPager(viewPager);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        final InspectionFragAdpter adapter = new InspectionFragAdpter
                (getSupportFragmentManager(), tab_inspection.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_inspection));

        tab_inspection.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
