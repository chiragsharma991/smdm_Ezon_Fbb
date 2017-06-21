package apsupportapp.aperotechnologies.com.designapp.CustomerLoyalty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import apsupportapp.aperotechnologies.com.designapp.KeyProductPlan.KeyProductPlanActivity;
import apsupportapp.aperotechnologies.com.designapp.KeyProductPlan.Plan_Option_Fragment;
import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by pamrutkar on 13/06/17.
 */
public class CustomerLookupActivity extends AppCompatActivity implements View.OnClickListener,OnEngagemntBandClick {
    RelativeLayout rel_cust_btnBack;
    static ViewPager mViewPager;
    private CustomerViewPagerAdapter adapter;
    static LinearLayout ez_linear_dots;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_lookup);
        getSupportActionBar().hide();
        initialiseUi();
        setTab();
    }


    private void initialiseUi() {
        rel_cust_btnBack = (RelativeLayout) findViewById(R.id.rel_cust_lookup_btnBack);
        ez_linear_dots = (LinearLayout) findViewById(R.id.linear_cust_dots);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tab = (TabLayout) findViewById(R.id.cust_dotTab);
        tab.setupWithViewPager(mViewPager, true);
        adapter = new CustomerViewPagerAdapter(getApplicationContext(), getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);
        rel_cust_btnBack.setOnClickListener(this);
    }

    private void setTab() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int position) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                for (int i = 0; i < 2; i++) {
                    ImageView imgdot = new ImageView(CustomerLookupActivity.this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
                    layoutParams.setMargins(3, 3, 3, 3);
                    imgdot.setLayoutParams(layoutParams);
                    imgdot.setImageResource(R.mipmap.dots_unselected);
                    ez_linear_dots.addView(imgdot);
                }
                ImageView img = (ImageView) ez_linear_dots.getChildAt(position);
                img.setImageResource(R.mipmap.dots_selected);
            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_cust_lookup_btnBack:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void communicatefrag1(String enagagemntband) {
        CustomerLookup_PageTwo fragment = (CustomerLookup_PageTwo) adapter.getItem(1);
        if (fragment != null)
        {
            fragment.fragmentCommunication(enagagemntband);
        }
    }
}
