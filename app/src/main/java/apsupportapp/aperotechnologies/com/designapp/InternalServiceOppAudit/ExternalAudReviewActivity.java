package apsupportapp.aperotechnologies.com.designapp.InternalServiceOppAudit;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by hasai on 10/10/17.
 */

public class ExternalAudReviewActivity  extends AppCompatActivity {

    private Context context;
    TabLayout tab_fg_competitor_store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_externalaudrev);
        context = this;

        tab_fg_competitor_store = (TabLayout) findViewById(R.id.tab_inspection);
        tab_fg_competitor_store.addTab(tab_fg_competitor_store.newTab().setText("FG Store"));
        tab_fg_competitor_store.addTab(tab_fg_competitor_store.newTab().setText("Competitor Store"));

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        final ExternalAudRevFragmentAdapter adapter = new ExternalAudRevFragmentAdapter
                (getSupportFragmentManager(), tab_fg_competitor_store.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_fg_competitor_store));

        tab_fg_competitor_store.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int check_pos = tab.getPosition();
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
