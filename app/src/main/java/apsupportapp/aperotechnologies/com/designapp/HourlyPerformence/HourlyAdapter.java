package apsupportapp.aperotechnologies.com.designapp.HourlyPerformence;

/**
 * Created by csuthar on 20/06/17.
 */

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.ArrayList;
import apsupportapp.aperotechnologies.com.designapp.MPM.mpm_model;
import apsupportapp.aperotechnologies.com.designapp.PvaSalesAnalysis.PvASnapAdapter;
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

                final mpm_model data = store_list.get(position);

                ((HourlyViewHolder) viewHolder).Hrl_root_Name.setText(data.getLevel());
                ((HourlyViewHolder) viewHolder).Hrl_root_netsales.setText("₹" + thousandSaperator.format((int) data.getSaleNetVal()));
                ((HourlyViewHolder) viewHolder).Hrl_root_plansales.setText("₹" +thousandSaperator.format((int) data.getPlanSales()));

                //calculate screen view size and add line bar process.

                ViewTreeObserver vto = ((HourlyViewHolder) viewHolder).Hrl_root_innerview.getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            ((HourlyViewHolder) viewHolder).Hrl_root_innerview.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            ((HourlyViewHolder) viewHolder).Hrl_root_innerview.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                        int width = ((HourlyViewHolder) viewHolder).Hrl_root_innerview.getMeasuredWidth();
                        int height = ((HourlyViewHolder) viewHolder).Hrl_root_innerview.getMeasuredHeight();

                        // Calculation width acording to size of phone
                        double x = 0;  // x is result of calculation.
                        x = ((double) data.getSalesAch() / 100) * width/2;   // divide by 2 to show process under the threshold.


                        int percentage =(int)x;
                        ((HourlyViewHolder) viewHolder).Hrl_root_innerview.removeAllViewsInLayout();


                        View lp = new View(context);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(percentage, LinearLayout.LayoutParams.MATCH_PARENT);
                        lp.setLayoutParams(layoutParams);
                        if (percentage < 70) {
                            lp.setBackgroundColor(Color.RED);
                        } else if (percentage > 90) {
                            lp.setBackgroundColor(Color.GREEN);
                        }
                        ((HourlyViewHolder) viewHolder).Hrl_root_innerview.addView(lp);


                        View threshold =new View (context);
                        RelativeLayout.LayoutParams layoutParams1=new RelativeLayout.LayoutParams(3,RelativeLayout.LayoutParams.MATCH_PARENT);
                        layoutParams1.addRule(RelativeLayout.CENTER_IN_PARENT);
                        threshold.setLayoutParams(layoutParams1);
                        threshold.setBackgroundColor(Color.BLACK);
                        ((HourlyViewHolder) viewHolder).Hrl_root_innerview.addView(threshold);






                    }
                });


            }}}



    public class HourlyViewHolder extends RecyclerView.ViewHolder {


        TextView Hrl_root_netsales,Hrl_root_plansales, Hrl_root_Name,Hrl_root_sAchieve,Hrl_root_sPlan;
        RelativeLayout Hrl_root_innerview;

        public HourlyViewHolder(View itemView) {
            super(itemView);

            Hrl_root_netsales = (TextView) itemView.findViewById(R.id.hrl_root_netsales);
            Hrl_root_plansales = (TextView) itemView.findViewById(R.id.hrl_root_plansales);
            Hrl_root_Name = (TextView) itemView.findViewById(R.id.hrl_root_Name);
          //  Hrl_root_sAchieve = (TextView) itemView.findViewById(R.id.hrl_root_sAchieve);
          //  Hrl_root_sPlan = (TextView) itemView.findViewById(R.id.hrl_root_sPlan);
            Hrl_root_innerview = (RelativeLayout) itemView.findViewById(R.id.rel_ez_inner);

        }
    }



}

