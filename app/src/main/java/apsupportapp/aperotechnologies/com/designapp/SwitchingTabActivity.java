package apsupportapp.aperotechnologies.com.designapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import apsupportapp.aperotechnologies.com.designapp.R;

public class SwitchingTabActivity extends AppCompatActivity {

    RelativeLayout backButton,imageBtnHomePage;
    public static  Activity switchingTabActivity;
    public static ViewPager viewPager;
    public static TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switching_tab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar().hide();

        switchingTabActivity = this;

        backButton = (RelativeLayout)findViewById(R.id.imageBtnBack1);
        imageBtnHomePage=(RelativeLayout)findViewById(R.id.imageBtnHomePage);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Intent intent=new Intent(SwitchingTabActivity.this,StyleActivity.class);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
                intent.putExtra("selCollectionname", getIntent().getExtras().getString("selCollectionname"));
                intent.putExtra("selOptionName", getIntent().getExtras().getString("selOptionName"));
                startActivity(intent);
                finish();
               // System.exit(0);

            }
        });

        imageBtnHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Intent intent=new Intent(SwitchingTabActivity.this,DashBoardActivity.class);
                startActivity(intent);
                finish();

            }
        });




         tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Details"));
        tabLayout.addTab(tabLayout.newTab().setText("Style Size"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


       // final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager = (ViewPager) findViewById(R.id.pager);

        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent=new Intent(SwitchingTabActivity.this,StyleActivity.class);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        intent.putExtra("selCollectionname", getIntent().getExtras().getString("selCollectionname"));
        intent.putExtra("selOptionName", getIntent().getExtras().getString("selOptionName"));
        startActivity(intent);
        finish();
        //
        // System.exit(0);
    }
}