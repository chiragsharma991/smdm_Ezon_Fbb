package apsupportapp.aperotechnologies.com.designapp.DashboardSnap;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import apsupportapp.aperotechnologies.com.designapp.R;

public class MarketEventAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

    private final Context context;
    private final int preposition;
    private final ArrayList<String> eventUrlList;
    private final ViewPager pager;
    private final LinearLayout lldots;
    private onclickView clickView;
    private ImageView image_market;
    public  int currentpage;



    public MarketEventAdapter(ArrayList<String> eventUrlList, Context context, int preposition, ViewPager pager, LinearLayout lldots) {

        this.context = context;
        this.preposition = preposition;
        this.eventUrlList = eventUrlList;
        clickView = (onclickView) this.context;

        this.pager = pager;
        this.lldots = lldots;
        currentpage = pager.getCurrentItem();


    }

    @Override
    public int getCount() {
        return eventUrlList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.marketing_image_adapter, container, false);
        image_market = (ImageView) view.findViewById(R.id.image_market);

        Picasso.with(context).load(eventUrlList.get(position)).fit().centerInside()
                .placeholder(R.color.grey)
                .error(R.color.grey)
                .into(image_market);
        pager.setOnPageChangeListener(this);
        pager.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object)  ;
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





   /* @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_snap_child_footer_hrl, parent, false));

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {

        Picasso.with(context).load(eventUrlList.get(position)).fit().centerInside()
                .placeholder(R.color.grey)
                .error(R.color.grey)
                .into(holder.Snap_child_imageView);

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount()
    {
        return eventUrlList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        public ImageView Snap_child_imageView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            Snap_child_imageView = (ImageView) itemView.findViewById(R.id.snap_child_imageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Log.e("TAG", "click" + getAdapterPosition());
            clickView.onclickView(preposition, getAdapterPosition());
        }
    }*/

}

