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

public class RunningPromoSummaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements GravitySnapHelper.SnapListener {

    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;
    private final ArrayList<RunningPromoListDisplay> promoList;
    private final Context Context;
    private clickChild Interface_clickListner;


    // Disable touch detection for parent recyclerView if we use vertical nested recyclerViews
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        }
    };

    public RunningPromoSummaryAdapter(ArrayList<RunningPromoListDisplay> promoList, Context context) {

        this.promoList=promoList;
        this.Context=context;
        this.Interface_clickListner=(clickChild)context;

    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_running_promo_summary_child, parent, false);
            return new Holder(v);




    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof Holder) {
            if(position < promoList.size()) {

                RunningPromoListDisplay snap = promoList.get(position);
                // holder.snapTextView.setText(snap.getText());

                ((Holder)holder).Department.setText(snap.getPlanDepartment());
                ((Holder)holder).number_of_promo.setText(""+snap.getNoOfPromotions());

                ((Holder)holder).Department.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Interface_clickListner.onClick("DEPARTMENT");
                    }
                });

                ((Holder)holder).number_of_promo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Interface_clickListner.onClick("PROMO");
                    }
                });


            }
        }


    }




    @Override
    public int getItemCount() {
        return promoList.size();
    }

    @Override
    public void onSnap(int position) {

    }


    private static class Holder extends RecyclerView.ViewHolder {

        //   public TextView snapTextView;
        //   public RecyclerView recyclerView;

        TextView Department,number_of_promo;

        public Holder(View itemView) {
            super(itemView);
            // snapTextView = (TextView) itemView.findViewById(R.id.snapTextView);
            // recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView);

            Department = (TextView) itemView.findViewById(R.id.summary_department);
            number_of_promo = (TextView) itemView.findViewById(R.id.summary_numberOfPromo);


        }

    }



}