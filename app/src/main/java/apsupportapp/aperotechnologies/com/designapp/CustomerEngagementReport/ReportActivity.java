package apsupportapp.aperotechnologies.com.designapp.CustomerEngagementReport;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import apsupportapp.aperotechnologies.com.designapp.InfantApp.Adapter.HomeAdapter;
import apsupportapp.aperotechnologies.com.designapp.InfantApp.FragmentCategories;
import apsupportapp.aperotechnologies.com.designapp.InfantApp.FragmentHome;
import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by csuthar on 07/11/17.
 */

public class ReportActivity extends AppCompatActivity {

    private Context context;
    private RelativeLayout imageBtnBack1;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cer_report);
        intialiseUI();


    }

    private void intialiseUI() {
        getSupportActionBar().hide();
        context = this;
        imageBtnBack1 = (RelativeLayout) findViewById(R.id.imageBtnBack1);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Recency Report"));
        tabLayout.addTab(tabLayout.newTab().setText("Frequency Report"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#e8112d"));
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        final ReportAdapter adapter = new ReportAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        imageBtnBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                View focus = getCurrentFocus();
                if (focus != null) {
                    hiddenKeyboard(focus);
                }
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

    private void hiddenKeyboard(View v) {
        InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public class ReportAdapter extends FragmentStatePagerAdapter {

        int mNumOfTabs;

        public ReportAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    RecencyFrequencyFragment tab1 = new RecencyFrequencyFragment();
                    return tab1;
                case 1:
                    FrequencyFragment tab2 = new FrequencyFragment();
                    return tab2;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}
