package apsupportapp.aperotechnologies.com.designapp;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import apsupportapp.aperotechnologies.com.designapp.R;

import java.util.ArrayList;

/**
 * Created by hasai on 15/03/16.
 */
@SuppressWarnings("ALL")
public class EventPagerAdapter extends PagerAdapter {
    // Declare Variables
    Context context;
    ArrayList<String> eventUrlList;
    LayoutInflater inflater;
    LinearLayout li;
    PageListener pageListener;
    View.OnClickListener onclick;
    ViewPager pager;
    private int currentPage = 0;

    public EventPagerAdapter(Context context, ArrayList<String> eventUrlList, LinearLayout li, ViewPager pager) {
        this.context = context;
        this.eventUrlList = eventUrlList;
        this.li = li;
        this.pager = pager;
        currentPage = pager.getCurrentItem();
    }

    @Override
    public int getCount() {
        return eventUrlList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {

        // Declare Variables
        ImageView imgEvent;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.fragment_screen_slide_page, container,
                false);


        pageListener = new PageListener();
        ((ViewPager) container).setOnPageChangeListener(pageListener);
        imgEvent = (ImageView) itemView.findViewById(R.id.imgEvent);

        Picasso.with(context).load(eventUrlList.get(position)).fit().centerInside()
                    .placeholder(R.color.grey)
                    .error(R.color.grey)
                    .into(imgEvent);

        itemView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //this will log the page number that was click
            }
        });

        // Add viewpager_item.xml to ViewPager
        ((ViewPager) container).addView(itemView);
        return itemView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);
    }

    private class PageListener extends ViewPager.SimpleOnPageChangeListener {
        public void onPageSelected(int pos) {
            ImageView img = (ImageView)  li.getChildAt(currentPage);
            img.setImageResource(R.mipmap.dots_unselected);
            currentPage = pos;
            ImageView img1 = (ImageView)  li.getChildAt(currentPage);
            img1.setImageResource(R.mipmap.dots_selected);
        }
    }
}

