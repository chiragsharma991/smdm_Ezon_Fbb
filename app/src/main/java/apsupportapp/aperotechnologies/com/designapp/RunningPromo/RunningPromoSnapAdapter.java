package apsupportapp.aperotechnologies.com.designapp.RunningPromo;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;

public class RunningPromoSnapAdapter extends RecyclerView.Adapter<RunningPromoSnapAdapter.ViewHolder> implements GravitySnapHelper.SnapListener {

    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;

    private ArrayList<RunningPromoListDisplay> mSnaps;
    // Disable touch detection for parent recyclerView if we use vertical nested recyclerViews
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        }
    };

    public RunningPromoSnapAdapter() {
        mSnaps = new ArrayList<>();
    }

    public void addSnap(RunningPromoListDisplay snap) {
        mSnaps.add(snap);
    }

    @Override
    public int getItemViewType(int position) {

        return VERTICAL;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_running_promo_child, parent, false);


//        if (viewType == VERTICAL) {
//            view.findViewById(R.id.recyclerView).setOnTouchListener(mTouchListener);
//        }

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RunningPromoListDisplay snap = mSnaps.get(position);
       // holder.snapTextView.setText(snap.getText());

        holder.PromotionName.setText(snap.getPromoDesc());
        holder.StartDate.setText(snap.getPromoStartDate());
        holder.EndDate.setText(snap.getPromoEndDate());
        holder.Days.setText(""+snap.getPromoDays());

//        if (snap.getGravity() == Gravity.START || snap.getGravity() == Gravity.END) {
//            holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder
//                    .recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
//            holder.recyclerView.setOnFlingListener(null);
//            new GravitySnapHelper(snap.getGravity(), false, this).attachToRecyclerView(holder.recyclerView);
//        } else if (snap.getGravity() == Gravity.CENTER_HORIZONTAL) {
//            holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder
//                    .recyclerView.getContext(), snap.getGravity() == Gravity.CENTER_HORIZONTAL ?
//                    LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
//            holder.recyclerView.setOnFlingListener(null);
//            new LinearSnapHelper().attachToRecyclerView(holder.recyclerView);
//        } else if (snap.getGravity() == Gravity.CENTER) { // Pager snap
//            holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder
//                    .recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
//            holder.recyclerView.setOnFlingListener(null);
//            new PagerSnapHelper().attachToRecyclerView(holder.recyclerView);
//        } else { // Top / Bottom
//            holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder
//                    .recyclerView.getContext()));
//            holder.recyclerView.setOnFlingListener(null);
//            new GravitySnapHelper(snap.getGravity()).attachToRecyclerView(holder.recyclerView);
//        }
//
//
//        holder.recyclerView.setAdapter(new Adapter(snap.getGravity() == Gravity.START
//                || snap.getGravity() == Gravity.END
//                || snap.getGravity() == Gravity.CENTER_HORIZONTAL,
//                snap.getGravity() == Gravity.CENTER, snap.getApps()));
    }

    @Override
    public int getItemCount() {
        return mSnaps.size();
    }

    @Override
    public void onSnap(int position) {
        Log.d("Snapped: ", position + "");
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

     //   public TextView snapTextView;
     //   public RecyclerView recyclerView;

        TextView PromotionName,StartDate,EndDate,Days;
        ImageView Vm;

        public ViewHolder(View itemView) {
            super(itemView);
           // snapTextView = (TextView) itemView.findViewById(R.id.snapTextView);
            // recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView);

            PromotionName = (TextView) itemView.findViewById(R.id.txtPromoName);
            StartDate = (TextView) itemView.findViewById(R.id.txtstartDate);
            EndDate = (TextView) itemView.findViewById(R.id.txtEndDate);
            Days = (TextView) itemView.findViewById(R.id.txtDays);
            Vm = (ImageView) itemView.findViewById(R.id.imgVm);
        }

    }
}