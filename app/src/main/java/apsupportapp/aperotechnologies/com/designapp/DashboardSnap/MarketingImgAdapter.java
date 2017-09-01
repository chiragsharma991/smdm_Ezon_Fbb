package apsupportapp.aperotechnologies.com.designapp.DashboardSnap;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import apsupportapp.aperotechnologies.com.designapp.R;

import static apsupportapp.aperotechnologies.com.designapp.DashboardSnap.SnapDashboardActivity.nestedScrollview;

/**
 * Created by rkanawade on 01/09/17.
 */

public class MarketingImgAdapter extends PagerAdapter {

    Context context;
    public ImageView image_market;
    public int pos;
    private ImageView img_first, img_second, img_third;
    ViewPager pager;
    int[] mResources = {
            R.mipmap.mi_max2_buy_now_app,
            R.mipmap.moto_e4_app,
            R.mipmap.koryo_speaker_21_mb_app

    };



    public MarketingImgAdapter(Context context, ViewPager pager) {
        this.context = context;
        this.pos = pos;
        this.pager = pager;

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.marketing_image_adapter, container, false);
        image_market = (ImageView) view.findViewById(R.id.image_market);
        img_first = (ImageView) view.findViewById(R.id.img_first);
        img_second = (ImageView) view.findViewById(R.id.img_second);
        img_third = (ImageView) view.findViewById(R.id.img_third);

        image_market.setImageResource(mResources[position]);

        int i = mResources[position];

//        if(i == 1){
//            img_first.setBackgroundResource(R.mipmap.inactive_splash_screen_circle);
//                    img_second.setBackgroundResource(R.mipmap.active_splash_screen_circle);
//                    img_third.setBackgroundResource(R.mipmap.inactive_splash_screen_circle);
//        }
//        else if(i == 2)
//        {
//            img_first.setBackgroundResource(R.mipmap.inactive_splash_screen_circle);
//                    img_second.setBackgroundResource(R.mipmap.inactive_splash_screen_circle);
//                    img_third.setBackgroundResource(R.mipmap.active_splash_screen_circle);
//        }
//        else
//        {
//            img_first.setBackgroundResource(R.mipmap.active_splash_screen_circle);
//                    img_second.setBackgroundResource(R.mipmap.inactive_splash_screen_circle);
//                    img_third.setBackgroundResource(R.mipmap.inactive_splash_screen_circle);
//        }


//        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){
//
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                //    Toast.makeText(context, "onPageScrollStateChanged "+state, Toast.LENGTH_SHORT).show();
//                // Called when the scroll state changes (scroll started - ended)
//            }
//
//            @Override
//            public void onPageScrolled(int position,
//                                       float positionOffset, int positionOffsetPixels) {
//                // This is called a lot of times when the user is scrolling
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                Toast.makeText(context, " "+position,Toast.LENGTH_SHORT).show();
//
//                if(position == 1){
//
//                    img_first.setBackgroundResource(R.mipmap.inactive_splash_screen_circle);
//                    img_second.setBackgroundResource(R.mipmap.active_splash_screen_circle);
//                    img_third.setBackgroundResource(R.mipmap.inactive_splash_screen_circle);
//                }
//                else if(position == 2){
//
//                    img_first.setBackgroundResource(R.mipmap.inactive_splash_screen_circle);
//                    img_second.setBackgroundResource(R.mipmap.inactive_splash_screen_circle);
//                    img_third.setBackgroundResource(R.mipmap.active_splash_screen_circle);
//                }
//                else{
//                   // nestedScrollview.setFocusableInTouchMode(true);
//
//                    img_first.setBackgroundResource(R.mipmap.active_splash_screen_circle);
//                    img_second.setBackgroundResource(R.mipmap.inactive_splash_screen_circle);
//                    img_third.setBackgroundResource(R.mipmap.inactive_splash_screen_circle);
//                }
//
//                // Check position here to see which page was selected
//            }
//        });


        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }
}
