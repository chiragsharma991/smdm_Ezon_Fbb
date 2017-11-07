package apsupportapp.aperotechnologies.com.designapp.InfantApp;

import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import apsupportapp.aperotechnologies.com.designapp.R;

public class ProductDetails extends AppCompatActivity {

    private ViewPager view_pager;
    private RelativeLayout btnBack;
    private LinearLayout lldots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setupWindowAnimations();
        setContentView(R.layout.activity_bb_product_details);
        getSupportActionBar().hide();
        intialiseUI();
        dotIntialize();
        ProductSliderAdapter vmPagerAdapter = new ProductSliderAdapter(this,lldots,view_pager);
        view_pager.setAdapter(vmPagerAdapter);


    }

    private void setupWindowAnimations() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
          /*  Fade fade = new Fade();
            fade.setDuration(1000);
            getWindow().setEnterTransition(fade);*/

            Fade slide = new Fade();
            slide.setDuration(1000);
            getWindow().setReturnTransition(slide);
        }


    }

    private void intialiseUI() {

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        btnBack=(RelativeLayout)findViewById(R.id.btnBack);
        lldots = (LinearLayout) findViewById(R.id.dotIndicator);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void dotIntialize() {
        for (int i = 0; i < 3; i++) {

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }else{
            finish();
        }
    }
}
