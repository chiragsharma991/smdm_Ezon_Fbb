package apsupportapp.aperotechnologies.com.designapp.DashboardSnap;

/**
 * Created by csuthar on 07/07/17.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.RunningPromo.RunningPromoSnapAdapter;

import static apsupportapp.aperotechnologies.com.designapp.R.id.pager;

public class SnapAdapter extends RecyclerView.Adapter<SnapAdapter.ViewHolder> implements GravitySnapHelper.SnapListener {

    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;
    private final Context context;
    private final ArrayList<String> eventUrlList;
    private int preposition;
    private final int VIEW_ITEM = 1;
    private final int VIEW_FOOT = 2;
    public static PagerAdapter adapter;
    String geoLeveLDesc;
    public int pos;
    SharedPreferences sharedPreferences;
   // private int icon[]={R.mipmap.placeholder,R.mipmap.placeholder};


    private ArrayList<Snap> mSnaps;
    // Disable touch detection for parent recyclerView if we use vertical nested recyclerViews
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        }
    };

    public SnapAdapter(Context context, ArrayList<String> eventUrlList) {

        this.context=context;
        this.eventUrlList=eventUrlList;
        mSnaps = new ArrayList<>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        geoLeveLDesc = sharedPreferences.getString("geoLeveLDesc", "");
    }

    public void addSnap(Snap snap) {
        mSnaps.add(snap);
    }

    @Override
    public int getItemViewType(int position) {
       // Snap snap = mSnaps.get(position);

        if (isPositionItem(position)) {
            return VIEW_ITEM;

        } else {
            return VIEW_FOOT;
        }
    }

    private boolean isPositionItem(int position) {
        return position != mSnaps.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_ITEM) {

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_snap_vertical, parent, false);

            return new ViewHolder(v);
        } else if (viewType == VIEW_FOOT) {

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_snap_vertical_footer, parent, false);

            return new ViewHolder(v);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        if (position < mSnaps.size())
        {
            preposition = position;
            Snap snap = mSnaps.get(position);
            holder.snap_parentTitle.setText(snap.getText());

            holder.Recycler_horizentalView.setLayoutManager(new LinearLayoutManager(holder
                    .Recycler_horizentalView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            holder.Recycler_horizentalView.setOnFlingListener(null);
            new GravitySnapHelper(snap.getGravity(), false, this).attachToRecyclerView(holder.Recycler_horizentalView);

            holder.Recycler_horizentalView.setAdapter(new SnapChildAdapter(snap.getGravity() == Gravity.START
                    || snap.getGravity() == Gravity.END
                    || snap.getGravity() == Gravity.CENTER_HORIZONTAL,
                    snap.getGravity() == Gravity.CENTER, snap.getApps(), context, preposition));
        }
        else
        {
            preposition = position;
            holder.snap_parentTitle.setText("Marketing Events");
            if (geoLeveLDesc.equals("E ZONE")) {
                adapter = new MarketingImgAdapter(context, holder.pager);
                holder.pager.setAdapter(adapter);

                //Necessary or the pager will only have one extra page to show
                // make this at least however many pages you can see
                holder.pager.setOffscreenPageLimit(adapter.getCount());
                //A little space between pages
                holder.pager.setPageMargin(15);

                //If hardware acceleration is enabled, you should also remove
                // clipping on the pager for its children.
                holder.pager.setClipChildren(false);
            }
            else {

                holder.Recycler_horizentalView.setLayoutManager(new LinearLayoutManager(holder
                        .Recycler_horizentalView.getContext(), LinearLayoutManager.HORIZONTAL, false));
                holder.Recycler_horizentalView.setOnFlingListener(null);
                new GravitySnapHelper(Gravity.START, false, this).attachToRecyclerView(holder.Recycler_horizentalView);
                holder.Recycler_horizentalView.setAdapter(new MarketEventAdapter(eventUrlList,context, preposition) );

            }





        }
    }

    @Override
    public int getItemCount() {
        return mSnaps.size()+1;
    }

    @Override
    public void onSnap(int position) {
        Log.d("Snapped: ", position + "");
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView snap_parentTitle;
        public RecyclerView Recycler_horizentalView;
        public static ViewPager pager;


        public ViewHolder(View itemView)
        {
            super(itemView);
            snap_parentTitle = (TextView) itemView.findViewById(R.id.snap_parentTitle);
            Recycler_horizentalView = (RecyclerView) itemView.findViewById(R.id.recycler_horizentalView);
            pager = (ViewPager) itemView.findViewById(R.id.view_pager);

        }
    }
}

