package apsupportapp.aperotechnologies.com.designapp.SalesPvAAnalysis;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.ProductNameBean;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.SalesPvAAnalysis.SalesPvAActivity;


public class SalesPvAAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<ProductNameBean> productNameBeanArrayList;
    SalesPvAActivity mainActivity;

    public SalesPvAAdapter(ArrayList<ProductNameBean> productNameBeanArrayList, Context context) {
        this.context = context;
        this.productNameBeanArrayList = productNameBeanArrayList;
    }

    @Override
    public int getCount() {
        return productNameBeanArrayList.size();
    }

    @Override
    public Object getItem(int location) {
        return productNameBeanArrayList.get(location);
    }

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

        viewHolder.rel.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Log.e("--- ", " " + viewHolder.rel.getMeasuredWidth() + " " + (200 / 100));

        int singlePercVal = 200 / 100;//rel.getMeasuredWidth()/100;

        int planVal = 100;//30
        int achieveVal = productNameBeanArrayList.get(position).getDayNetSales();

        int calplanVal = planVal * singlePercVal;
        int calachieveVal = achieveVal * singlePercVal;

        viewHolder.txtPlan.setWidth(calplanVal);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(2, 18);
        params.setMargins(calachieveVal, 0, 0, 0);
        viewHolder.txtAchieve.setLayoutParams(params);

        if (planVal < achieveVal) {
            viewHolder.txtPlan.setBackgroundColor(Color.RED);
        } else {
            viewHolder.txtPlan.setBackgroundColor(Color.GREEN);
        }

        viewHolder.txtPlanClass.setTag(position);
        viewHolder.txtPlanSales.setTag(position);
        viewHolder.txtNetSales.setTag(position);

        viewHolder.txtPlanClass.setText(productNameBeanArrayList.get(position).getArticleOption());
        viewHolder.txtPlanSales.setText("452546.154");
        viewHolder.txtNetSales.setText("145690.359");

        viewHolder.txtPlanClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "name click" + viewHolder.txtPlanClass.getText().toString(), Toast.LENGTH_SHORT).show();
                Log.e("segmented click", "" + SalesPvAActivity.salesPvA_SegmentClick);
                switch (SalesPvAActivity.salesPvA_SegmentClick) {
                    case "WTD":
                        if (SalesPvAActivity.txtPlanClass.getText().toString().equals("Department")) {
                            Toast.makeText(context, "on adapter click 1" + viewHolder.txtPlanClass.getText().toString() + "" + SalesPvAActivity.txtPlanClass.getText().toString(), Toast.LENGTH_SHORT).show();
                            SalesPvAActivity.txtPlanClass.setText("Category");
                        } else if (SalesPvAActivity.txtPlanClass.getText().toString().equals("Category")) {
                            Toast.makeText(context, "on adapter click 2" + viewHolder.txtPlanClass.getText().toString() + "" + SalesPvAActivity.txtPlanClass.getText().toString(), Toast.LENGTH_SHORT).show();
                            SalesPvAActivity.txtPlanClass.setText("Plan Class");
                        } else if (SalesPvAActivity.txtPlanClass.getText().toString().equals("Plan Class")) {
                            Toast.makeText(context, "on adapter click 3" + viewHolder.txtPlanClass.getText().toString() + "" + SalesPvAActivity.txtPlanClass.getText().toString(), Toast.LENGTH_SHORT).show();
                            SalesPvAActivity.txtPlanClass.setText("Brand");
                        } else if (SalesPvAActivity.txtPlanClass.getText().toString().equals("Brand")) {
                            Toast.makeText(context, "on adapter click 4" + viewHolder.txtPlanClass.getText().toString() + "" + SalesPvAActivity.txtPlanClass.getText().toString(), Toast.LENGTH_SHORT).show();
                            SalesPvAActivity.txtPlanClass.setText("Brand Plan Class");
                        }
                        break;
                    case "LW":
                        if (SalesPvAActivity.txtPlanClass.getText().toString().equals("Department")) {
                            Toast.makeText(context, "on adapter click 1 -" + viewHolder.txtPlanClass.getText().toString() + "" + SalesPvAActivity.txtPlanClass.getText().toString(), Toast.LENGTH_SHORT).show();
                            SalesPvAActivity.txtPlanClass.setText("Category");
                        } else if (SalesPvAActivity.txtPlanClass.getText().toString().equals("Category")) {
                            Toast.makeText(context, "on adapter click 2-" + viewHolder.txtPlanClass.getText().toString() + "" + SalesPvAActivity.txtPlanClass.getText().toString(), Toast.LENGTH_SHORT).show();
                            SalesPvAActivity.txtPlanClass.setText("Plan Class");
                        } else if (SalesPvAActivity.txtPlanClass.getText().toString().equals("Plan Class")) {
                            Toast.makeText(context, "on adapter click 3-" + viewHolder.txtPlanClass.getText().toString() + "" + SalesPvAActivity.txtPlanClass.getText().toString(), Toast.LENGTH_SHORT).show();
                            SalesPvAActivity.txtPlanClass.setText("Brand");
                        } else if (SalesPvAActivity.txtPlanClass.getText().toString().equals("Brand")) {
                            Toast.makeText(context, "on adapter click 4-" + viewHolder.txtPlanClass.getText().toString() + "" + SalesPvAActivity.txtPlanClass.getText().toString(), Toast.LENGTH_SHORT).show();
                            SalesPvAActivity.txtPlanClass.setText("Brand Plan Class");
                        }
                }
            }
        });
        return convertView;
    }
}
