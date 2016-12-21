package apsupportapp.aperotechnologies.com.designapp.FreshnessIndex;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.PvaSalesAnalysis.SalesPvAAdapter;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.model.FreshnessIndexDetails;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisListDisplay;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisViewPagerValue;

/**
 * Created by pamrutkar on 22/11/16.
 */
public class FreshnessIndexAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    String fromWhere;
    int level;
    int offsetvalue = 0, count = 0, limit = 100;
    ArrayList<FreshnessIndexDetails> freshnessIndexDetailsArrayList;

    ListView listViewFIndex;
    Gson gson;
    CombinedData pvaData;


    public FreshnessIndexAdapter(ArrayList<FreshnessIndexDetails> freshnessIndexDetailsArrayList, Context context, String fromWhere, ListView listViewFIndex) {
        this.context = context;
        this.freshnessIndexDetailsArrayList = freshnessIndexDetailsArrayList;
        this.fromWhere = fromWhere;
        this.listViewFIndex = listViewFIndex;

        gson = new Gson();
    }

    @Override
    public int getCount() {

        return freshnessIndexDetailsArrayList.size();
    }

    //Get the data item associated with the specified position in the data set.
    @Override
    public Object getItem(int position) {

        return freshnessIndexDetailsArrayList.get(position);
    }

    //Get the row id associated with the specified position in the list.
    @Override
    public long getItemId(int position) {

        return position;
    }

    static class ViewHolderItem {
        TextView txtfindexClass, txtfindexSOH, txtfindexSOH_U, txtfindexGIT;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final FreshnessIndexAdapter.ViewHolderItem viewHolder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.findex_list_row, parent,
                    false);
            viewHolder = new FreshnessIndexAdapter.ViewHolderItem();
            viewHolder.txtfindexClass = (TextView) convertView.findViewById(R.id.txtfindexClass);
            viewHolder.txtfindexSOH = (TextView) convertView.findViewById(R.id.txtfindexSOH);
            viewHolder.txtfindexSOH_U = (TextView) convertView.findViewById(R.id.txtfindexSOH_U);
            viewHolder.txtfindexGIT = (TextView) convertView.findViewById(R.id.txtfindexGIT);


            convertView.setTag(viewHolder);
//            convertView.setTag(R.id.txtfindexClass, viewHolder.txtfindexClass);
//            convertView.setTag(R.id.txtfindexSOH, viewHolder.txtfindexSOH);
//            convertView.setTag(R.id.txtfindexSOH_U, viewHolder.txtfindexSOH_U);
//            convertView.setTag(R.id.txtfindexGIT, viewHolder.txtfindexGIT);

        } else {
            viewHolder = (FreshnessIndexAdapter.ViewHolderItem) convertView.getTag();
        }

        FreshnessIndexDetails freshnessIndexDetails = (FreshnessIndexDetails) freshnessIndexDetailsArrayList.get(position);

//        viewHolder.txtfindexClass.setTag(position);
//        viewHolder.txtfindexSOH.setTag(position);
//        viewHolder.txtfindexSOH_U.setTag(position);
//        viewHolder.txtfindexGIT.setTag(position);

        if (fromWhere.equals("Department")) {

            viewHolder.txtfindexClass.setText(freshnessIndexDetails.getPlanDept());
            viewHolder.txtfindexSOH.setText(""+freshnessIndexDetails.getStkOnhandQty());
            viewHolder.txtfindexSOH_U.setText(" "+ String.format("%.1f",freshnessIndexDetails.getStkOnhandQtyCount()));
            viewHolder.txtfindexGIT.setText(""+Math.round(freshnessIndexDetails.getStkGitQty()));

        } else if (fromWhere.equals("Category")) {

            viewHolder.txtfindexClass.setText(freshnessIndexDetails.getPlanCategory());
            viewHolder.txtfindexSOH.setText(""+(int)freshnessIndexDetails.getStkOnhandQty());
            viewHolder.txtfindexSOH_U.setText(" "+ String.format("%.1f",freshnessIndexDetails.getStkOnhandQtyCount()));
            viewHolder.txtfindexGIT.setText(""+Math.round(freshnessIndexDetails.getStkGitQty()));



        } else if (fromWhere.equals("Plan Class")) {

            viewHolder.txtfindexClass.setText(freshnessIndexDetails.getPlanClass());
            viewHolder.txtfindexSOH.setText(""+(int)freshnessIndexDetails.getStkOnhandQty());
            viewHolder.txtfindexSOH_U.setText(" "+ String.format("%.1f",freshnessIndexDetails.getStkOnhandQtyCount()));
            viewHolder.txtfindexGIT.setText(""+Math.round(freshnessIndexDetails.getStkGitQty()));



        } else if (fromWhere.equals("Brand")) {

            viewHolder.txtfindexClass.setText(freshnessIndexDetails.getBrandName());
            viewHolder.txtfindexSOH.setText(""+(int)freshnessIndexDetails.getStkOnhandQty());
            viewHolder.txtfindexSOH_U.setText(" "+ String.format("%.1f",freshnessIndexDetails.getStkOnhandQtyCount()));
            viewHolder.txtfindexGIT.setText(""+Math.round(freshnessIndexDetails.getStkGitQty()));

        } else if (fromWhere.equals("Brand Plan Class")) {

            viewHolder.txtfindexClass.setText(freshnessIndexDetails.getBrandplanClass());
            viewHolder.txtfindexSOH.setText(""+(int)freshnessIndexDetails.getStkOnhandQty());
            viewHolder.txtfindexSOH_U.setText(" "+ String.format("%.1f",freshnessIndexDetails.getStkOnhandQtyCount()));
            viewHolder.txtfindexGIT.setText(""+Math.round(freshnessIndexDetails.getStkGitQty()));

        }

        // update listview
//
//        SalesAnalysisListDisplay salesAnalysisClass = salesAnalysisListDisplayArrayList.get(0);
//        Log.e("listv", "" + listViewSalesPvA.getFirstVisiblePosition());

//        if (listViewSalesPvA.getFirstVisiblePosition() == 0) {
//
//            if (salesAnalysisClass.getPlanDept() != null) {
//                if (salesAnalysisClass.getPlanDept().equals("All")) {
//                    Log.e("-----", "All");
//                    salesAnalysisClass.setPvaAchieved(salesAnalysisViewPagerValue.getPvaAchieved());
//                    salesAnalysisClass.setPlanSaleNetVal((int) salesAnalysisViewPagerValue.getPlanSaleNetVal());
//                    salesAnalysisClass.setSaleNetVal((int) productNameBean.getSaleNetVal());
//                    salesAnalysisListDisplayArrayList.set(0, salesAnalysisClass);
//                    salesPvAAdapter = new SalesPvAAdapter(salesAnalysisListDisplayArrayList, context, fromWhere, listViewSalesPvA);
//                    listViewSalesPvA.setAdapter(salesPvAAdapter);
//                    salesPvAAdapter.notifyDataSetChanged();
//
//                }
//
//            }
//
//            if (salesAnalysisClass.getPlanCategory() != null) {
//                if (salesAnalysisClass.getPlanCategory().equals("All")) {
//                    Log.e("-----", "All");
//                    salesAnalysisClass.setPvaAchieved(productNameBean.getPvaAchieved());
//                    salesAnalysisClass.setPlanSaleNetVal((int) productNameBean.getPlanSaleNetVal());
//                    salesAnalysisClass.setSaleNetVal((int) productNameBean.getSaleNetVal());
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
//                        salesAnalysisClass.setPlanSaleNetVal((int) productNameBean.getPlanSaleNetVal());
//                        salesAnalysisClass.setSaleNetVal((int) productNameBean.getSaleNetVal());
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
//                        salesAnalysisClass.setPlanSaleNetVal((int) productNameBean.getPlanSaleNetVal());
//                        salesAnalysisClass.setSaleNetVal((int) productNameBean.getSaleNetVal());
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
//                        salesAnalysisClass.setPlanSaleNetVal((int) productNameBean.getPlanSaleNetVal());
//                        salesAnalysisClass.setSaleNetVal((int) productNameBean.getSaleNetVal());
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
//        }

        return convertView;
    }


    public static int convertSpToPixels(double sp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, (float) sp, context.getResources().getDisplayMetrics());
        return px;
    }
}


