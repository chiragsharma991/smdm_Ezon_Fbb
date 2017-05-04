package apsupportapp.aperotechnologies.com.designapp.PvaSalesAnalysis;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.data.CombinedData;
import com.google.gson.Gson;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import apsupportapp.aperotechnologies.com.designapp.R;

import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisListDisplay;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisViewPagerValue;


public class SalesPvAAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    String fromWhere;
    int level;
    int offsetvalue = 0, count = 0, limit = 100;
    ArrayList<SalesAnalysisListDisplay> salesAnalysisListDisplayArrayList;
    SalesAnalysisListDisplay salesAnalysisListDisplay;
    SalesAnalysisViewPagerValue salesAnalysisViewPagerValue;
    SalesPvAAdapter salesPvAAdapter;
    ListView listViewSalesPvA;
    Gson gson;
    CombinedData pvaData;


    public SalesPvAAdapter(ArrayList<SalesAnalysisListDisplay> salesAnalysisListDisplayArrayList, Context context, int currentIndex, String fromWhere, ListView listViewSalesPvA) {
        this.context = context;
        this.salesAnalysisListDisplayArrayList = salesAnalysisListDisplayArrayList;
        this.fromWhere = fromWhere;
        this.listViewSalesPvA = listViewSalesPvA;
        level = 1;
        gson = new Gson();
    }

    @Override
    public int getCount() {

        return salesAnalysisListDisplayArrayList.size();
    }

    //Get the data item associated with the specified position in the data set.
    @Override
    public Object getItem(int position) {

        return salesAnalysisListDisplayArrayList.get(position);
    }

    //Get the row id associated with the specified position in the list.
    @Override
    public long getItemId(int position) {

        return position;
    }

    static class ViewHolderItem {
        TextView txtPlanClass, txtPlanSales, txtNetSales, txtPvASales, txtPlan, txtAchieve;
        RelativeLayout rel;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolderItem viewHolder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.salespva_list_row, parent,
                    false);
            viewHolder = new ViewHolderItem();
            viewHolder.txtPlanClass = (TextView) convertView.findViewById(R.id.txtPlanClass);
            viewHolder.txtPlanSales = (TextView) convertView.findViewById(R.id.txtPlanSales);
            viewHolder.txtNetSales = (TextView) convertView.findViewById(R.id.txtNetSales);

            viewHolder.rel = (RelativeLayout) convertView.findViewById(R.id.rel);
            viewHolder.txtPlan = (TextView) convertView.findViewById(R.id.txtPlan);
            viewHolder.txtAchieve = (TextView) convertView.findViewById(R.id.txtAchieve);
            convertView.setTag(viewHolder);
            convertView.setTag(R.id.txtPlanClass, viewHolder.txtPlanClass);
            convertView.setTag(R.id.txtPlanSales, viewHolder.txtPlanSales);
            convertView.setTag(R.id.txtNetSales, viewHolder.txtNetSales);

        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        SalesAnalysisListDisplay productNameBean = (SalesAnalysisListDisplay) salesAnalysisListDisplayArrayList.get(position);

        viewHolder.txtPlanClass.setTag(position);
        viewHolder.txtPlanSales.setTag(position);
        viewHolder.txtNetSales.setTag(position);

        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("","in"));


        if (fromWhere.equals("Department")) {

                viewHolder.txtPlanClass.setText(productNameBean.getPlanDept());


                viewHolder.txtPlanSales.setText("\u20B9 " + formatter.format(Math.round(productNameBean.getPlanSaleNetVal())));
                viewHolder.txtNetSales.setText("\u20B9 " + formatter.format(Math.round(productNameBean.getSaleNetVal())));





        } else if (fromWhere.equals("Category")) {

            viewHolder.txtPlanClass.setText(productNameBean.getPlanCategory());
            viewHolder.txtPlanSales.setText("\u20B9 " +formatter.format(Math.round(productNameBean.getPlanSaleNetVal())));
            viewHolder.txtNetSales.setText("\u20B9 " +formatter.format(Math.round(productNameBean.getSaleNetVal())));


//
//            viewHolder.txtValue.setText(productNameBean.getPlanCategory().toLowerCase());

        } else if (fromWhere.equals("Plan Class")) {
            viewHolder.txtPlanClass.setText(productNameBean.getPlanClass());
            viewHolder.txtPlanSales.setText("\u20B9 " +formatter.format(Math.round(productNameBean.getPlanSaleNetVal())));
            viewHolder.txtNetSales.setText("\u20B9 " +formatter.format(Math.round(productNameBean.getSaleNetVal())));


        } else if (fromWhere.equals("Brand"))
        {
            viewHolder.txtPlanClass.setText(productNameBean.getBrandName());
            viewHolder.txtPlanSales.setText("\u20B9 " +formatter.format(Math.round(productNameBean.getPlanSaleNetVal())));
            viewHolder.txtNetSales.setText("\u20B9 " +formatter.format(Math.round(productNameBean.getSaleNetVal())));

        } else if (fromWhere.equals("Brand Plan Class"))
        {
            viewHolder.txtPlanClass.setText(productNameBean.getBrandplanClass());
            viewHolder.txtPlanSales.setText("\u20B9 " +formatter.format(Math.round(productNameBean.getPlanSaleNetVal())));
            viewHolder.txtNetSales.setText("\u20B9 " +formatter.format(Math.round(productNameBean.getSaleNetVal())));


        }



        // update listview

//        SalesAnalysisListDisplay salesAnalysisClass = salesAnalysisListDisplayArrayList.get(0);
//      //  Log.e("listv", "" + listViewSalesPvA.getFirstVisiblePosition());
//
//
//        if (listViewSalesPvA.getFirstVisiblePosition() == 0) {
//
//            if (salesAnalysisClass.getPlanDept() != null) {
//                if (salesAnalysisClass.getPlanDept().equals("All")) {
//                    Log.e("-----", "All");
//                   // salesAnalysisClass.setPvaAchieved(SalesAnalysisActivity.salesAnalysis.getPvaAchieved());
//                  // viewHolder.txtPlanSales.setText("₹" +formatter.format(Math.round(SalesPvAActivity.salesAnalysisViewPagerValue.getPlanSaleNetVal())));
//                   salesAnalysisClass.setPlanSaleNetVal(Math.round(SalesPvAActivity.salesAnalysisViewPagerValue.getPlanSaleNetVal()));
////                    salesAnalysisClass.setPlanSaleNetVal(Math.round(SalesPvAActivity.salesAnalysisViewPagerValue.getPlanSaleNetVal()));
////                    Log.e("ghdhfgd", "" + SalesPvAActivity.salesAnalysisViewPagerValue.getPlanSaleNetVal());
////                    salesAnalysisClass.setSaleNetVal(Math.round(SalesPvAActivity.salesAnalysisViewPagerValue.getSaleNetVal()));
//                   salesAnalysisListDisplayArrayList.set(0, salesAnalysisClass);
//                    salesPvAAdapter = new SalesPvAAdapter(salesAnalysisListDisplayArrayList, context, fromWhere, listViewSalesPvA);
//                    listViewSalesPvA.setAdapter(salesPvAAdapter);
//                //   salesPvAAdapter.notifyDataSetChanged();
//
//                }
//
//            }
//        }

//            if (salesAnalysisClass.getPlanCategory() != null) {
//                if (salesAnalysisClass.getPlanCategory().equals("All")) {
//                    Log.e("-----", "All");
//                    salesAnalysisClass.setPvaAchieved(productNameBean.getPvaAchieved());
//                    salesAnalysisClass.setPlanSaleNetVal(Math.round( productNameBean.getPlanSaleNetVal()));
//                    salesAnalysisClass.setSaleNetVal(Math.round( productNameBean.getSaleNetVal()));
//                    salesAnalysisListDisplayArrayList.set(0, salesAnalysisClass);
//                    salesPvAAdapter = new SalesPvAAdapter(salesAnalysisListDisplayArrayList, context, fromWhere, listViewSalesPvA);
//                    listViewSalesPvA.setAdapter(salesPvAAdapter);
//                    salesPvAAdapter.notifyDataSetChanged();
//
//
//                }
//
//                if (salesAnalysisClass.getPlanClass() != null) {
//                    if (salesAnalysisClass.getPlanClass().equals("All")) {
//                        Log.e("-----", "All");
//                        salesAnalysisClass.setPvaAchieved(productNameBean.getPvaAchieved());
//                        salesAnalysisClass.setPlanSaleNetVal(Math.round( productNameBean.getPlanSaleNetVal()));
//                        salesAnalysisClass.setSaleNetVal(Math.round( productNameBean.getSaleNetVal()));
//                        salesAnalysisListDisplayArrayList.set(0, salesAnalysisClass);
//                        salesPvAAdapter = new SalesPvAAdapter(salesAnalysisListDisplayArrayList, context, fromWhere, listViewSalesPvA);
//                        listViewSalesPvA.setAdapter(salesPvAAdapter);
//                        salesPvAAdapter.notifyDataSetChanged();
//
//                    }
//
//                }
//
//                if (salesAnalysisClass.getBrandName() != null) {
//                    if (salesAnalysisClass.getBrandName().equals("All")) {
//                        Log.e("-----", "All");
//                        salesAnalysisClass.setPvaAchieved(productNameBean.getPvaAchieved());
//                        salesAnalysisClass.setPlanSaleNetVal(Math.round( productNameBean.getPlanSaleNetVal()));
//                        salesAnalysisClass.setSaleNetVal(Math.round( productNameBean.getSaleNetVal()));
//                        salesAnalysisListDisplayArrayList.set(0, salesAnalysisClass);
//                        salesPvAAdapter = new SalesPvAAdapter(salesAnalysisListDisplayArrayList, context, fromWhere, listViewSalesPvA);
//                        listViewSalesPvA.setAdapter(salesPvAAdapter);
//                        salesPvAAdapter.notifyDataSetChanged();
//
//                    }
//
//                }
//
//                if (salesAnalysisClass.getBrandplanClass() != null) {
//                    if (salesAnalysisClass.getBrandplanClass().equals("All")) {
//                        Log.e("-----", "All");
//                        salesAnalysisClass.setPvaAchieved(productNameBean.getPvaAchieved());
//                        salesAnalysisClass.setPlanSaleNetVal(Math.round( productNameBean.getPlanSaleNetVal()));
//                        salesAnalysisClass.setSaleNetVal(Math.round( productNameBean.getSaleNetVal()));
//                        salesAnalysisListDisplayArrayList.set(0, salesAnalysisClass);
//                        salesPvAAdapter = new SalesPvAAdapter(salesAnalysisListDisplayArrayList, context, fromWhere, listViewSalesPvA);
//                        listViewSalesPvA.setAdapter(salesPvAAdapter);
//                        salesPvAAdapter.notifyDataSetChanged();
//
//                    }
//
//                }
//
//            }
   //     }


        double singlePercVal = 0.5;//50/100;// width divide by 100 perc

        int planVal = 100; // planned value from API
        double achieveVal = productNameBean.getPvaAchieved();// Achieved value from API

        double calplanVal = planVal * singlePercVal; // planned value multiplied by single perc value
        double calachieveVal = achieveVal * singlePercVal; // Achieved value multiplied by single perc value

//        int planvalueinpx = convertSpToPixels((int)calplanVal, context); //converting value from sp to px
//        int achievevalueinpx = convertSpToPixels((int)calachieveVal, context); //converting value from sp to px

        float density = context.getResources().getDisplayMetrics().density;

        int finalCalplanVal = (int) (density * calplanVal); //converting value from px to dp
        //Log.e("", "==finalCalplanVal= " + finalCalplanVal);
        int finalCalachieveVal = (int) (density * calachieveVal); //converting value from px to dp
        // Log.e("", "==finalCalachieveVal= " + finalCalachieveVal);


        viewHolder.txtPlan.setWidth(finalCalachieveVal);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(3, 24);
        params.setMargins(finalCalplanVal, 0, 0, 0);
        viewHolder.txtAchieve.setLayoutParams(params);


        if (achieveVal < 70) {
            viewHolder.txtPlan.setBackgroundColor(Color.RED);
        } else if (achieveVal > 90) {
            viewHolder.txtPlan.setBackgroundColor(Color.GREEN);//yellow
        } else {
            viewHolder.txtPlan.setBackgroundColor(Color.parseColor("#ff7e00"));
        }
        return convertView;
    }



    public static int convertSpToPixels(double sp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, (float) sp, context.getResources().getDisplayMetrics());
        return px;
    }


}



