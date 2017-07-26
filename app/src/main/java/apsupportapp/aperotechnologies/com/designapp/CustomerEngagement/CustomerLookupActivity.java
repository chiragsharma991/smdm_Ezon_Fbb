package apsupportapp.aperotechnologies.com.designapp.CustomerEngagement;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.google.gson.Gson;

import apsupportapp.aperotechnologies.com.designapp.MySingleton;
import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by pamrutkar on 13/06/17.
 */
public class CustomerLookupActivity extends AppCompatActivity implements View.OnClickListener,OnEngagemntBandClick {
    RelativeLayout rel_cust_btnBack;
    static ViewPager mViewPager;
    private CustomerViewPagerAdapter adapter;
    static LinearLayout ez_linear_dots;
    Context context;
    RequestQueue queue;
    String userId, bearertoken,geoLeveLDesc;
    SharedPreferences sharedPreferences;
    Gson gson;
    MySingleton m_config;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_lookup);
        getSupportActionBar().hide();
        context = this;
        m_config = MySingleton.getInstance(context);
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        userId = sharedPreferences.getString("userId","");
        bearertoken = sharedPreferences.getString("bearerToken","");
        geoLeveLDesc = sharedPreferences.getString("geoLeveLDesc", "");
        if (geoLeveLDesc.equals("E ZONE")) {
            userId = sharedPreferences.getString("userId", "");  //E zone userid =username
        } else {
            userId = sharedPreferences.getString("userId", "");   //FBB userid =username+store code
            userId = userId.substring(0, userId.length() - 5);    // Hourly works only userid=username;
        }
        gson = new Gson();
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
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void communicatefrag1(String enagagemntband)
    {
        CustomerLookup_PageTwo fragment = (CustomerLookup_PageTwo) adapter.getItem(1);
        if (fragment != null)
        {
            fragment.fragmentCommunication(enagagemntband);
        }
    }
}
