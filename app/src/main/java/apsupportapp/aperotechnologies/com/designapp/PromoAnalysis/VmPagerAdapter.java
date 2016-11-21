package apsupportapp.aperotechnologies.com.designapp.PromoAnalysis;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by csuthar on 15/11/16.
 */
class VmPagerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

    private final ArrayList<String> list;
    private final LinearLayout lldots;
    Context mContext;
    LayoutInflater mLayoutInflater;
    private int currentPage=0;

    public VmPagerAdapter(Context context, ArrayList<String> list, LinearLayout lldots) {
        mContext = context;
        this.list=list;
        this.lldots=lldots;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.activity_vm_pageritem, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.pagerImageView);
       // imageView.setImageResource(mResources[position]);
        Picasso.with(mContext)
                .load(VM.list.get(position))
                .into(imageView);


        container.addView(itemView);

        return itemView;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        ImageView img = (ImageView) lldots.getChildAt(currentPage);
        img.setImageResource(R.mipmap.dots_unselected);
        currentPage = position;
        ImageView img1 = (ImageView) lldots.getChildAt(currentPage);
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