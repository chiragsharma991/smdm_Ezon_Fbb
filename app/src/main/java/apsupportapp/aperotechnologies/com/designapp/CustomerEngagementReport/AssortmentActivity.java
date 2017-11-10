package apsupportapp.aperotechnologies.com.designapp.CustomerEngagementReport;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import apsupportapp.aperotechnologies.com.designapp.R;

import static apsupportapp.aperotechnologies.com.designapp.ProductInformation.SwitchingTabActivity.tabLayout;

/**
 * Created by pamrutkar on 07/11/17.
 */

public class AssortmentActivity extends AppCompatActivity
{
    private AssortmentActivity context;
    private RelativeLayout imageBtnBack1,filter;
    private LinearLayout parentview;
    private String[]listdata={"Title one","Title Two","Title three","Title four"};
    private String TAG=this.getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cer_assortment);
        getSupportActionBar().hide();
        intialiseUI();
    }

    private void intialiseUI() {
        //getSupportActionBar().hide();
        context = this;
        imageBtnBack1 = (RelativeLayout) findViewById(R.id.imageBtnBack1);
        filter = (RelativeLayout) findViewById(R.id.filter);
        parentview = (LinearLayout) findViewById(R.id.parentview);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("LD"));
        tabLayout.addTab(tabLayout.newTab().setText("WTD"));
        tabLayout.addTab(tabLayout.newTab().setText("LW"));
        tabLayout.addTab(tabLayout.newTab().setText("MTD"));
        tabLayout.addTab(tabLayout.newTab().setText("LM"));
        tabLayout.addTab(tabLayout.newTab().setText("YTD"));
       // tabLayout.setTabGravity(TabLayout.MODE_SCROLLABLE|TabLayout.GRAVITY_FILL);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#e8112d"));
        imageBtnBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {


            }
        });
        makeTableView();
    }

    private void makeTableView() {
        Log.e(TAG, "makeTableView: " );
        parentview.removeAllViewsInLayout();
        for (int i = 0; i <4 ; i++) {
            View view= 0==i ? getLayoutInflater().inflate(R.layout.table_cer_assortment_header,null) : getLayoutInflater().inflate(R.layout.table_cer_assortment_subview,null);
                    if(i !=0){
                        TextView txt_one=(TextView)view.findViewById(R.id.txt_one);
                        TextView txt_two=(TextView)view.findViewById(R.id.txt_two);
                        TextView txt_three=(TextView)view.findViewById(R.id.txt_three);

                        txt_one.setText(listdata[0]);
                        txt_two.setText(listdata[1]);
                        txt_three.setText(listdata[2]);
                        parentview.addView(view);

                    }else { parentview.addView(view);}
        }


    }

    public void showMoving(View view){


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
