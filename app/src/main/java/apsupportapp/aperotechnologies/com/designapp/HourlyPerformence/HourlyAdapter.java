package apsupportapp.aperotechnologies.com.designapp.HourlyPerformence;

/**
 * Created by csuthar on 20/06/17.
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.ArrayList;
import apsupportapp.aperotechnologies.com.designapp.MPM.mpm_model;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.EzoneSalesAdapter;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisListDisplay;


public class HourlyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>   {

    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;
    private final ArrayList<mpm_model> store_list;
    private final NumberFormat thousandSaperator;
    private LayoutInflater mInflater;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 2;
    Context context;


    // Disable touch detection for parent recyclerView if we use vertical nested recyclerViews


    public HourlyAdapter(ArrayList<mpm_model> store_list, Context context, NumberFormat thousandSaperator) {

        this.context = context;
        this.store_list = store_list;
        this.thousandSaperator = thousandSaperator;
        mInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getItemViewType(int position) {


            return VIEW_ITEM;

    }



    @Override
    public int getItemCount() {
        return store_list.size();
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_hourly_root_performer, parent, false);
            return new HourlyViewHolder(v);
        }

        return null;

    }


    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {

        if (viewHolder instanceof HourlyViewHolder) {
            if (position < store_list.size()) {

                mpm_model data = store_list.get(position);

                ((HourlyViewHolder) viewHolder).Hrl_root_Name.setText(data.getLevel());
                ((HourlyViewHolder) viewHolder).Hrl_root_netsales.setText("₹" + thousandSaperator.format((int) data.getSaleNetVal()));
                ((HourlyViewHolder) viewHolder).Hrl_root_plansales.setText("" + (int) data.getPlanSales());
                double singlePercVal = 0.5;//50/100;// width divide by 100 perc
                int    planVal = 100;                                    // planned value from API
                double achieveVal = 70;                              //data.getSalesAch();// Achieved value from API
                double calplanVal = planVal * singlePercVal;        // planned value multiplied by single perc value
                double calachieveVal = achieveVal * singlePercVal; // Achieved value multiplied by single perc value

                float density = context.getResources().getDisplayMetrics().density;
                int finalCalplanVal = (int) (density * calplanVal); //converting value from px to dp
                int finalCalachieveVal = (int) (density * calachieveVal); //converting value from px to dp
                Log.e("TAG", "onBindViewHolder: " + achieveVal + " " + calachieveVal);

                ((HourlyViewHolder) viewHolder).Hrl_root_sPlan.setWidth(finalCalachieveVal);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(3, 24);
                params.setMargins(finalCalplanVal, 0, 0, 0);
                ((HourlyViewHolder) viewHolder).Hrl_root_sAchieve.setLayoutParams(params);


            }
        }


        }



    public class HourlyViewHolder extends RecyclerView.ViewHolder {


        TextView Hrl_root_netsales,Hrl_root_plansales, Hrl_root_Name,Hrl_root_sAchieve,Hrl_root_sPlan;

        public HourlyViewHolder(View itemView) {
            super(itemView);

            Hrl_root_netsales = (TextView) itemView.findViewById(R.id.hrl_root_netsales);
            Hrl_root_plansales = (TextView) itemView.findViewById(R.id.hrl_root_plansales);
            Hrl_root_Name = (TextView) itemView.findViewById(R.id.hrl_root_Name);
            Hrl_root_sAchieve = (TextView) itemView.findViewById(R.id.hrl_root_sAchieve);
            Hrl_root_sPlan = (TextView) itemView.findViewById(R.id.hrl_root_sPlan);

        }
    }



}

