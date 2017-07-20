package apsupportapp.aperotechnologies.com.designapp.DashboardSnap;

/**
 * Created by csuthar on 07/07/17.
 */

import android.content.Context;
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
import android.widget.TextView;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.RunningPromo.RunningPromoSnapAdapter;

public class SnapAdapter extends RecyclerView.Adapter<SnapAdapter.ViewHolder> implements GravitySnapHelper.SnapListener {

    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;
    private final Context context;
    private final ArrayList<String> eventUrlList;
    private int preposition;
    private final int VIEW_ITEM = 1;
    private final int VIEW_FOOT = 2;
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
    public void onBindViewHolder(ViewHolder holder, int position)
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
            holder.Recycler_horizentalView.setLayoutManager(new LinearLayoutManager(holder
                    .Recycler_horizentalView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            holder.Recycler_horizentalView.setOnFlingListener(null);
            new GravitySnapHelper(Gravity.START, false, this).attachToRecyclerView(holder.Recycler_horizentalView);
            holder.Recycler_horizentalView.setAdapter(new MarketEventAdapter(eventUrlList,context, preposition) );
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

        public ViewHolder(View itemView)
        {
            super(itemView);
            snap_parentTitle = (TextView) itemView.findViewById(R.id.snap_parentTitle);
            Recycler_horizentalView = (RecyclerView) itemView.findViewById(R.id.recycler_horizentalView);
        }
    }
}

