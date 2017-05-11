package apsupportapp.aperotechnologies.com.designapp.UpcomingPromo;

/**
 * Created by csuthar on 17/11/16.
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

import apsupportapp.aperotechnologies.com.designapp.RunningPromo.VM;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;

/**
 * Created by hasai on 12/09/16.
 */

public class UpcomingPromoAdapter extends BaseAdapter {

    private ArrayList<RunningPromoListDisplay> arrayList;
    RunningPromoListDisplay runningPromoListDisplay;
    private LayoutInflater mInflater;
    Context context;
    private int Position;

    public UpcomingPromoAdapter(ArrayList<RunningPromoListDisplay> arrayList, Context context) {

        this.arrayList = arrayList;
        this.context = context;
        mInflater = LayoutInflater.from(context);
        runningPromoListDisplay=new RunningPromoListDisplay();

    }

    //How many items are in the data set represented by this Adapter.
    @Override
    public int getCount() {

        return arrayList.size();
    }

    //Get the data item associated with the specified position in the data set.
    @Override
    public Object getItem(int position)
    {
        return arrayList.get(position);
    }

    //Get the row id associated with the specified position in the list.
    @Override
    public long getItemId(int position)
    {
        return position;
    }

    //Get a View that displays the data at the specified position in the data set.
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Position=position;
        Holder holder;
        if (convertView == null) {
            holder=new Holder();
            convertView = mInflater.inflate(R.layout.activity_upcoming_promo_child, null);
            holder.PromotionName = (TextView) convertView.findViewById(R.id.txt_up_PromoName);
            holder.StartDate = (TextView) convertView.findViewById(R.id.txt_up_startDate);
            holder.EndDate = (TextView) convertView.findViewById(R.id.txt_up_EndDate);
            holder.Vm = (ImageView) convertView.findViewById(R.id.up_imgVm);
            convertView.setTag(holder);

        } else {
            holder=(Holder)convertView.getTag();
        }

        holder.PromotionName.setText(arrayList.get(position).getPromoDesc());
        holder.StartDate.setText(arrayList.get(position).getPromoStartDate());
        holder.EndDate.setText(arrayList.get(position).getPromoEndDate());


        // ---------------------click listener -------------------------

            holder.Vm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent=new Intent(context,VM.class);
            intent.putExtra("VM",arrayList.get(position).getPromoDesc());
            intent.putExtra("FROM","UpcomingPromo");
            context.startActivity(intent);
            }
       });

        return convertView;
    }

    private class Holder {

        TextView PromotionName,StartDate,EndDate;
        ImageView Vm;

    }
}
