package apsupportapp.aperotechnologies.com.designapp.PvaSalesAnalysis;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.data.CombinedData;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.google.gson.Gson;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisListDisplay;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisViewPagerValue;


/**
 * Created by pamrutkar on 23/01/17.
 */

public class PvASnapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements GravitySnapHelper.SnapListener {

    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 2;
    int curerntindex;
    private Context context;
    String fromWhere;
    int level;
    ArrayList<SalesAnalysisListDisplay> mSnaps;
    RecyclerView listViewSalesPvA;
    Gson gson;




    public PvASnapAdapter(ArrayList<SalesAnalysisListDisplay> salesAnalysisListDisplayArrayList, Context context, int currentIndex, String fromWhere, RecyclerView listViewSalesPvA) {
        this.context = context;
        this.mSnaps = salesAnalysisListDisplayArrayList;
        this.fromWhere = fromWhere;
        this.listViewSalesPvA = listViewSalesPvA;
        this.curerntindex = currentIndex;
        level = 1;
        gson = new Gson();
    }


    public void addSnap(SalesAnalysisListDisplay snap) {
        mSnaps.add(snap);
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
        return position !=mSnaps.size();
    }

    @Override
    public int getItemCount() {
        return mSnaps.size()+1;
    }

    @Override
    public void onSnap(int position) {
        Log.d("Snapped: ", position + "");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.salespva_list_row, parent, false);
            return new PvAViewHolder(v);
        } else if (viewType == VIEW_PROG){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_footer, parent, false);
            return new ProgressViewHolder(v);
        }

        return null;

    }


    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {

        if (viewHolder instanceof PvAViewHolder) {

            SalesAnalysisListDisplay productNameBean = mSnaps.get(position);
            NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("","in"));


            if (fromWhere.equals("Department")) {

                ((PvAViewHolder)viewHolder).txtPlanClass.setText(productNameBean.getPlanDept());
                ((PvAViewHolder)viewHolder).txtPlanSales.setText("\u20B9 " + formatter.format(Math.round(productNameBean.getPlanSaleNetVal())));
                ((PvAViewHolder)viewHolder).txtNetSales.setText("\u20B9 " + formatter.format(Math.round(productNameBean.getSaleNetVal())));

            } else if (fromWhere.equals("Category")) {

                ((PvAViewHolder)viewHolder).txtPlanClass.setText(productNameBean.getPlanCategory());
                ((PvAViewHolder)viewHolder).txtPlanSales.setText("\u20B9 " +formatter.format(Math.round(productNameBean.getPlanSaleNetVal())));
                ((PvAViewHolder)viewHolder).txtNetSales.setText("\u20B9 " +formatter.format(Math.round(productNameBean.getSaleNetVal())));

            } else if (fromWhere.equals("Plan Class")) {

                ((PvAViewHolder)viewHolder).txtPlanClass.setText(productNameBean.getPlanClass());
                ((PvAViewHolder)viewHolder).txtPlanSales.setText("\u20B9 " +formatter.format(Math.round(productNameBean.getPlanSaleNetVal())));
                ((PvAViewHolder)viewHolder).txtNetSales.setText("\u20B9 " +formatter.format(Math.round(productNameBean.getSaleNetVal())));


            } else if (fromWhere.equals("Brand"))
            {
                ((PvAViewHolder)viewHolder).txtPlanClass.setText(productNameBean.getBrandName());
                ((PvAViewHolder)viewHolder).txtPlanSales.setText("\u20B9 " +formatter.format(Math.round(productNameBean.getPlanSaleNetVal())));
                ((PvAViewHolder)viewHolder).txtNetSales.setText("\u20B9 " +formatter.format(Math.round(productNameBean.getSaleNetVal())));

            } else if (fromWhere.equals("Brand Plan Class"))
            {
                ((PvAViewHolder)viewHolder).txtPlanClass.setText(productNameBean.getBrandplanClass());
                ((PvAViewHolder)viewHolder).txtPlanSales.setText("\u20B9 " +formatter.format(Math.round(productNameBean.getPlanSaleNetVal())));
                ((PvAViewHolder)viewHolder).txtNetSales.setText("\u20B9 " +formatter.format(Math.round(productNameBean.getSaleNetVal())));

            }

            double singlePercVal = 0.5;//50/100;// width divide by 100 perc

            int planVal = 100; // planned value from API
            double achieveVal = productNameBean.getPvaAchieved();// Achieved value from API

            double calplanVal = planVal * singlePercVal; // planned value multiplied by single perc value
            double calachieveVal = achieveVal * singlePercVal; // Achieved value multiplied by single perc value

            float density = context.getResources().getDisplayMetrics().density;

            int finalCalplanVal = (int) (density * calplanVal); //converting value from px to dp
            //Log.e("", "==finalCalplanVal= " + finalCalplanVal);
            int finalCalachieveVal = (int) (density * calachieveVal); //converting value from px to dp
            // Log.e("", "==finalCalachieveVal= " + finalCalachieveVal);


            ((PvAViewHolder)viewHolder).txtPlan.setWidth(finalCalachieveVal);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(3, 24);
            params.setMargins(finalCalplanVal, 0, 0, 0);
            ((PvAViewHolder)viewHolder).txtAchieve.setLayoutParams(params);


            if (achieveVal < 70) {
                ((PvAViewHolder)viewHolder).txtPlan.setBackgroundColor(Color.RED);
            } else if (achieveVal > 90) {
                ((PvAViewHolder)viewHolder).txtPlan.setBackgroundColor(Color.GREEN);//yellow
            } else {
                ((PvAViewHolder)viewHolder).txtPlan.setBackgroundColor(Color.parseColor("#ff7e00"));
            }

            } else {
                // ((ProgressViewHolder) viewHolder).txtView.setText(" ");
            }

        }

      public  class PvAViewHolder extends RecyclerView.ViewHolder {


            TextView txtPlanClass, txtPlanSales, txtNetSales, txtPvASales, txtPlan, txtAchieve;
            RelativeLayout rel;


            public PvAViewHolder(View itemView) {
                super(itemView);

                txtPlanClass = (TextView) itemView.findViewById(R.id.txtPlanClass);
                txtPlanSales = (TextView) itemView.findViewById(R.id.txtPlanSales);
                txtNetSales = (TextView) itemView.findViewById(R.id.txtNetSales);

                rel = (RelativeLayout) itemView.findViewById(R.id.rel);
                txtPlan = (TextView) itemView.findViewById(R.id.txtPlan);
                txtAchieve = (TextView) itemView.findViewById(R.id.txtAchieve);

            }

        }

       public class ProgressViewHolder extends RecyclerView.ViewHolder {

            TextView txtView;

            public ProgressViewHolder(View footerView) {
                super(footerView);

                //  txtView = (TextView)footerView.findViewById(R.id.txtView);
            }
        }

 }



