package apsupportapp.aperotechnologies.com.designapp.BestPerformersPromo;

/**
 * Created by csuthar on 18/11/16.
 */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.PromoAnalysis.VM;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;

public class BestPromoAdapter extends BaseAdapter {

    private ArrayList<RunningPromoListDisplay> arrayList;

    //private List mStringFilterList;

    private LayoutInflater mInflater;
    Context context;
    private int Position;

    //private ValueFilter valueFilter;

    public BestPromoAdapter(ArrayList<RunningPromoListDisplay> arrayList, Context context) {

        // Log.e("in sales analysis adapter"," ");
        this.arrayList = arrayList;
        this.context = context;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        //Log.e("in ","getview");

        Position=position;
        final Holder holder;
        if (convertView == null) {
            holder=new Holder();
            convertView = mInflater.inflate(R.layout.activity_best_performer_child, null);
            holder.PromotionName = (TextView) convertView.findViewById(R.id.bst_head_child);
            holder.Bst_PromoValues_child = (TextView) convertView.findViewById(R.id.bst_PromoValues_child);
            holder.Bst_PromoValuesU_child = (TextView) convertView.findViewById(R.id.bst_PromoValuesU_child);
            holder.Bst_image_child = (ImageView) convertView.findViewById(R.id.bst_image_child);



            convertView.setTag(holder);

        } else {
            holder=(Holder)convertView.getTag();

        }
        holder.PromotionName.setText(arrayList.get(position).getPromoDesc());
        holder.Bst_PromoValues_child.setText("₹"+arrayList.get(position).getDurSaleNetVal());
        holder.Bst_PromoValuesU_child.setText(""+arrayList.get(position).getDurSaleTotQty());


        // ---------------------click listener -------------------------



     /*   holder.PromotionName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(context,RunningPromoDetail.class);

                context.startActivity(i);

            }
        });*/


   /*     holder.StartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(context,RunningPromoDetail.class);
                context.startActivity(i);

            }
        });

        holder.EndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(context,RunningPromoDetail.class);
                context.startActivity(i);

            }
        });

        holder.Days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(context,RunningPromoDetail.class);
                context.startActivity(i);

            }
        });



        holder.Vm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,VM.class);
                context.startActivity(intent);
            }
        });*/











        return convertView;
    }



    private class Holder {

        TextView PromotionName,Bst_PromoValues_child,Bst_PromoValuesU_child;
        ImageView Bst_image_child;

    }




}
