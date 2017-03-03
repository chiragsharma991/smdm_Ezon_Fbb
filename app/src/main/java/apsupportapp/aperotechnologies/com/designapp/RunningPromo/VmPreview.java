package apsupportapp.aperotechnologies.com.designapp.RunningPromo;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import apsupportapp.aperotechnologies.com.designapp.R;

public class VmPreview extends AppCompatActivity {

    private ViewPager mViewPager;
    RelativeLayout vm_imageBtnBack;
    private LinearLayout lldots;
    Bundle data;
    String TAG="VmPreview";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vm_preview);
        getSupportActionBar().hide();

        findView();
        dotIntialize();

        VmPagerAdapter vmPagerAdapter = new VmPagerAdapter(this,VM.list,lldots,mViewPager);
    /*    for (int i = 0; i < VM.list.size(); i++) {

            ImageView imgdot = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
            layoutParams.setMargins(3, 3, 3, 3);
            imgdot.setLayoutParams(layoutParams);
            imgdot.setImageResource(R.mipmap.dots_unselected);
            lldots.addView(imgdot);
            ImageView img = (ImageView) lldots.getChildAt(0);
            img.setImageResource(R.mipmap.dots_selected);
        }*/

        mViewPager.setAdapter(vmPagerAdapter);
        //mViewPager.setCurrentItem(data.getInt("VM_ADP"));
        vm_imageBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: ");

                finish();
            }
        });

    }

    private void dotIntialize() {
        for (int i = 0; i <VM.list.size(); i++) {

            ImageView imgdot = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
            layoutParams.setMargins(3, 3, 3, 3);
            imgdot.setLayoutParams(layoutParams);
            imgdot.setImageResource(R.mipmap.dots_unselected);
            lldots.addView(imgdot);
        }
        ImageView img = (ImageView) lldots.getChildAt(0);
        img.setImageResource(R.mipmap.dots_selected);

    }

    private void findView() {

        mViewPager = (ViewPager) findViewById(R.id.pager);
        vm_imageBtnBack=(RelativeLayout)findViewById(R.id.vm_imageBtnBack);

        lldots = (LinearLayout) findViewById(R.id.dotIndicator);
        TabLayout tab=(TabLayout)findViewById(R.id.dotTab);
        tab.setupWithViewPager(mViewPager, true);

        //lldots.setOrientation(LinearLayout.HORIZONTAL);
        data=getIntent().getExtras();


    }

    @Override
    public void onBackPressed() {
        Log.e(TAG, "onBack: ");

        this.finish();
    }
}
