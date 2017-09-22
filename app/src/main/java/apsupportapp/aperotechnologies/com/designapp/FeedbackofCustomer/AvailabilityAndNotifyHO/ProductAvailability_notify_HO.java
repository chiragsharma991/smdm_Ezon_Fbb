package apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.AvailabilityAndNotifyHO;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.AvailabilityAndNotifyHO.Adapter.ProductAvailability_ViewPagerAdapter;
import apsupportapp.aperotechnologies.com.designapp.R;

public class ProductAvailability_notify_HO extends AppCompatActivity implements ProductAvailability_Feedback.feedbackInterface{

    TabLayout tabLayout;
    private RelativeLayout imageBtnBack1;
    private Context context;
    private String TAG="ProductAvlHO";
    private ViewPager viewPager;
    private String storeCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_availability_notify_ho);
        Log.e(TAG, "onCreate:>> " );
        getSupportActionBar().hide();
        getSupportActionBar().setElevation(0);
        context = this;
        imageBtnBack1 = (RelativeLayout) findViewById(R.id.imageBtnBack1);
        if(getIntent().getExtras().getString("from") != null )
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

        viewPager = (ViewPager) findViewById(R.id.pager);
        final ProductAvailability_ViewPagerAdapter adapter = new ProductAvailability_ViewPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(), storeCode);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e(TAG, "onTabSelected: " );
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
    public void onTrigger(int position) {
        viewPager.setCurrentItem(position);
        //tabLayout.getTabAt(position).select();

    }
}
