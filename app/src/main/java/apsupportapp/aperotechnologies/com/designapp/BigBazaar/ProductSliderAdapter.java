package apsupportapp.aperotechnologies.com.designapp.BigBazaar;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.RunningPromo.VM;

/**
 * Created by csuthar on 03/11/17.
 */



class ProductSliderAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

    private final LinearLayout lldots;
    private final ViewPager mViewPager;
    Context mContext;
    LayoutInflater mLayoutInflater;
    private int currentPage=0;

    public ProductSliderAdapter(Context context,  LinearLayout lldots, ViewPager mViewPager) {
        mContext = context;
        this.lldots=lldots;
        this.mViewPager=mViewPager;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        mViewPager.setOnPageChangeListener(this);
        View itemView = mLayoutInflater.inflate(R.layout.activity_vm_pageritem, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.pagerImageView);
        Glide.with(mContext)
                .load(R.mipmap.placeholder)
                .error(R.mipmap.placeholder)
                .into(imageView);

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        for (int i = 0; i <3 ; i++) {
            ImageView img = (ImageView) lldots.getChildAt(i);
            img.setImageResource(R.mipmap.dots_unselected);
        }
        ImageView img1 = (ImageView) lldots.getChildAt(position);
        img1.setImageResource(R.mipmap.dots_selected);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
