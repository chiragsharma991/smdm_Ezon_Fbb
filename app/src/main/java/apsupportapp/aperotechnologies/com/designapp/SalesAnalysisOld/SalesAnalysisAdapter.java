package apsupportapp.aperotechnologies.com.designapp.SalesAnalysisOld;

/**
 * Created by hasai on 12/09/16.
 */

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import apsupportapp.aperotechnologies.com.designapp.ProductNameBean;
import apsupportapp.aperotechnologies.com.designapp.R;


public class SalesAnalysisAdapter extends BaseAdapter{

    private List arrayList;

    //private List mStringFilterList;

    private LayoutInflater mInflater;
    Context context;
    String fromwhere;

    //private ValueFilter valueFilter;

    public SalesAnalysisAdapter(ArrayList<ProductNameBean> arrayList, Context context, String fromwhere) {

        Log.e("in sales analysis adapter"," ");

        this.arrayList = arrayList;
        this.context = context;
        this.fromwhere = fromwhere;
        mInflater = LayoutInflater.from(context);

        //getFilter();
    }

    //How many items are in the data set represented by this Adapter.
    @Override
    public int getCount() {

        return arrayList.size();
    }

    //Get the data item associated with the specified position in the data set.
    @Override
    public Object getItem(int position) {

        return arrayList.get(position);
    }

    //Get the row id associated with the specified position in the list.
    @Override
    public long getItemId(int position) {

        return position;
    }

    //Get a View that displays the data at the specified position in the data set.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.e("in ","getview");
        Holder viewHolder;

        if (convertView == null) {

            viewHolder = new Holder();
            convertView = mInflater.inflate(R.layout.child_sales_listview, null);
            viewHolder.nameTv = (TextView) convertView.findViewById(R.id.txtVal);
            viewHolder.rel = (RelativeLayout) convertView.findViewById(R.id.rel);
            viewHolder.innerrel = (RelativeLayout) convertView.findViewById(R.id.innerrellay);
            viewHolder.relValue = (RelativeLayout) convertView.findViewById(R.id.relValue);
            viewHolder.txtPlan = (TextView) convertView.findViewById(R.id.txtPlan);
            viewHolder.txtValue = (TextView) convertView.findViewById(R.id.txtValue);
            viewHolder.txtAchieve = (TextView) convertView.findViewById(R.id.txtAchieve);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (Holder) convertView.getTag();
        }

        ProductNameBean productNameBean = (ProductNameBean) arrayList.get(position);
        viewHolder.nameTv.setText(productNameBean.getArticleOption().toLowerCase());


        Log.e("--- ", " "+viewHolder.rel.getMeasuredWidth() + " "+ (200/100));

        if(fromwhere.equals("PVA Sales")) {

            double singlePercVal = 0.5;//50/100;// width divide by 100 perc

            int planVal = 100; // planned value from API
            int achieveVal = productNameBean.getWtdNetSales();// Achieved value from API

            double calplanVal = planVal * singlePercVal; // planned value multiplied by single perc value
            double calachieveVal = achieveVal * singlePercVal; // Achieved value multiplied by single perc value

            int planvalueinpx = convertSpToPixels(calplanVal, context); //converting value from sp to px
            int achievevalueinpx = convertSpToPixels(calachieveVal, context); //converting value from sp to px

            float density = context.getResources().getDisplayMetrics().density;

            int finalCalplanVal = (int) (density * planvalueinpx); //converting value from px to dp
            Log.e("", "==finalCalplanVal= " + finalCalplanVal);
            int finalCalachieveVal = (int) (density * achievevalueinpx); //converting value from px to dp
            Log.e("", "==finalCalachieveVal= " + finalCalachieveVal);


            viewHolder.txtPlan.setWidth(finalCalplanVal);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(2, 24);
            params.setMargins(finalCalachieveVal, 0, 0, 0);
            viewHolder.txtAchieve.setLayoutParams(params);


//        viewHolder.txtPlan.setWidth(calplanVal);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(2,18);
//        params.setMargins(calachieveVal,0,0,0);
//        viewHolder.txtAchieve.setLayoutParams(params);

            if (planVal < achieveVal) {
                viewHolder.txtPlan.setBackgroundColor(Color.RED);
            } else {
                viewHolder.txtPlan.setBackgroundColor(Color.GREEN);
            }
        }
        else if(fromwhere.equals("Net Sales"))
        {

            viewHolder.innerrel.setVisibility(View.GONE);
            viewHolder.relValue.setVisibility(View.VISIBLE);
            viewHolder.txtValue.setText(productNameBean.getArticleOption().toLowerCase());

        }
        else if(fromwhere.equals("Plan Sales"))
        {

            viewHolder.innerrel.setVisibility(View.GONE);
            viewHolder.relValue.setVisibility(View.VISIBLE);
            viewHolder.txtValue.setText(productNameBean.getStoreCode());

        }

        return convertView;
    }

    private class Holder {

        TextView nameTv;
        RelativeLayout rel;
        RelativeLayout innerrel, relValue;
        TextView txtPlan, txtValue;
        TextView txtAchieve;
    }


    public static int convertSpToPixels(double sp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, (float) sp, context.getResources().getDisplayMetrics());
        return px;
    }


}