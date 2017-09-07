package apsupportapp.aperotechnologies.com.designapp.DashboardSnap;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import apsupportapp.aperotechnologies.com.designapp.R;

import static apsupportapp.aperotechnologies.com.designapp.DashboardSnap.SnapDashboardActivity.nestedScrollview;

/**
 * Created by rkanawade on 01/09/17.
 */

public class MarketingImgAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener{

    Context context;
    public ImageView image_market;
    public int pos;
    LinearLayout lldots;
    public static int currentpage;
    ViewPager pager;

    int[] mResources = {
            R.mipmap.tkk_mobile_unit,
            R.mipmap.moto_e4_app,

    };

    public MarketingImgAdapter(Context context, ViewPager pager, LinearLayout lldots) {
        this.context = context;
        this.pos = pos;
        this.pager = pager;
        this.lldots = lldots;
        currentpage = pager.getCurrentItem();


    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.marketing_image_adapter, container, false);
        image_market = (ImageView) view.findViewById(R.id.image_market);

        image_market.setImageResource(mResources[position]);

        int i = mResources[position];
        pager.setOnPageChangeListener(this);

        pager.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


    }

    @Override
    public void onPageSelected(int position) {
        ImageView img = (ImageView) lldots.getChildAt(currentpage);
        img.setImageResource(R.mipmap.dots_unselected);
        currentpage = position;
        ImageView img1 = (ImageView) lldots.getChildAt(currentpage);
        img1.setImageResource(R.mipmap.dots_selected);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        // Remove viewpager_item.xml from ViewPager
        (container).removeView((LinearLayout) object);

    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
