package apsupportapp.aperotechnologies.com.designapp.RunningPromo;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import apsupportapp.aperotechnologies.com.designapp.R;

public class VmPreview extends AppCompatActivity {

    private ViewPager mViewPager;
    RelativeLayout vm_imageBtnBack;
    private LinearLayout lldots;
    Bundle data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vm_preview);
        getSupportActionBar().hide();

        findView();

        VmPagerAdapter vmPagerAdapter = new VmPagerAdapter(this,VM.list,lldots);
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

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(vmPagerAdapter);
        mViewPager.setCurrentItem(data.getInt("VM_ADP"));
        vm_imageBtnBack=(RelativeLayout)findViewById(R.id.vm_imageBtnBack);
        vm_imageBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

    }

    private void findView() {

        //lldots = (LinearLayout) findViewById(R.id.vmlldots);
        //lldots.setOrientation(LinearLayout.HORIZONTAL);
        data=getIntent().getExtras();


    }

    @Override
    public void onBackPressed() {

        this.finish();
    }
}
