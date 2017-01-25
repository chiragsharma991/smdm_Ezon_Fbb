package apsupportapp.aperotechnologies.com.designapp.RunningPromo;


import android.content.Context;
import android.content.Intent;
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

import apsupportapp.aperotechnologies.com.designapp.FreshnessIndex.FreshnessIndexSnapAdapter;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;

public class RunningPromoSnapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements GravitySnapHelper.SnapListener {

    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;
    private final ArrayList<RunningPromoListDisplay> promoList;
    private final RunningPromoActivity Context;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 2;

    // Disable touch detection for parent recyclerView if we use vertical nested recyclerViews
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        }
    };

    public RunningPromoSnapAdapter(ArrayList<RunningPromoListDisplay> promoList, RunningPromoActivity runningPromoActivity) {

        this.promoList=promoList;
        this.Context=runningPromoActivity;
    }


    @Override
    public int getItemViewType(int position) {

        if (isPositionItem(position)){
            return VIEW_ITEM;

        }
        else {
            return VIEW_PROG;
        }
    }
    private boolean isPositionItem(int position) {
        // return position == 0;
        return position != promoList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_ITEM) {
            View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_running_promo_child, parent, false);
            return new Holder(v);
        } else if (viewType == VIEW_PROG){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_footer, parent, false);
            return new FooterView(v);
        }

        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof Holder) {
            if(position < promoList.size()) {

                RunningPromoListDisplay snap = promoList.get(position);
            // holder.snapTextView.setText(snap.getText());

            ((Holder)holder).PromotionName.setText(snap.getPromoDesc());
                ((Holder) holder).PromotionName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Context, RunningPromoDetails.class);
                                i.putExtra("VM", promoList.get(position).getPromoDesc());
                                Context.startActivity(i);
                    }
                });
            ((Holder)holder).StartDate.setText(snap.getPromoStartDate());

                ((Holder) holder).StartDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Context, RunningPromoDetails.class);
                        i.putExtra("VM", promoList.get(position).getPromoDesc());
                        Context.startActivity(i);
                    }
                });


             ((Holder)holder).EndDate.setText(snap.getPromoEndDate());
                ((Holder) holder).EndDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Context, RunningPromoDetails.class);
                        i.putExtra("VM", promoList.get(position).getPromoDesc());
                        Context.startActivity(i);
                    }
                });
                ((Holder)holder).Days.setText("" + snap.getPromoDays());
                ((Holder) holder).Days.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Context, RunningPromoDetails.class);
                        i.putExtra("VM", promoList.get(position).getPromoDesc());
                        Context.startActivity(i);
                    }
                });
                ((Holder)holder).Vm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(Context,VM.class);
                    intent.putExtra("VM",promoList.get(position).getPromoDesc());
                    intent.putExtra("FROM","RunningPromo");
                    Context.startActivity(intent);

                }
            });
        }
        }
        else
        {

        }

    }




    @Override
    public int getItemCount() {
       return promoList.size()+1;
    }

    @Override
    public void onSnap(int position) {
        Log.d("Snapped: ", position + "");
    }

    public static class Holder extends RecyclerView.ViewHolder {

     //   public TextView snapTextView;
     //   public RecyclerView recyclerView;

        TextView PromotionName,StartDate,EndDate,Days;
        ImageView Vm;

        public Holder(View itemView) {
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


    public static class FooterView extends RecyclerView.ViewHolder {
        //        Button loadButton;
//        ProgressBar progressBar;
        TextView txtView;

        public FooterView(View footerView){
            super(footerView);
//            loadButton = (Button) footerView.findViewById(R.id.reload_button);
//            progressBar = (ProgressBar) footerView.findViewById(R.id.progress_load);
            txtView = (TextView)footerView.findViewById(R.id.txtView);
        }
    }
}