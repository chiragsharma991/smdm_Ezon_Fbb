package apsupportapp.aperotechnologies.com.designapp.TargetStockExceptions;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.model.FloorAvailabilityDetails;


/**
 * Created by pamrutkar on 08/12/16.
 */
public class TargetStockExcepAdapter extends BaseAdapter {

    private ArrayList<FloorAvailabilityDetails> arrayList;
    private LayoutInflater mInflater;
    Context context;
    int Position,mlevel;
    ProgressBar progressView = null;
    TargetStockExceptionActivity targetStockExceptionActivity;

    public TargetStockExcepAdapter(ArrayList<FloorAvailabilityDetails> arrayList, int level, Context context) {

        this.arrayList = arrayList;
        this.context = context;
        this.mlevel = level;
        mInflater = LayoutInflater.from(context);
    }

    //How many items are in the data set represented by this Adapter.
    @Override
    public int getCount()
    {
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


        Position = position;

        final TargetStockExcepAdapter.Holder holder;
        if (convertView == null) {
            holder = new TargetStockExcepAdapter.Holder();
            convertView = mInflater.inflate(R.layout.activity_target_exception_child, null);
            holder.target_SOH_U = (TextView) convertView.findViewById(R.id.target_SOH_U);
            holder.target_ROS_U = (TextView) convertView.findViewById(R.id.target_ROS_U);
            holder.target_ROS = (TextView) convertView.findViewById(R.id.target_ROS);
            holder.target_Availability = (TextView) convertView.findViewById(R.id.target_Availability);
            holder.target_option = (TextView) convertView.findViewById(R.id.target_option);
           // holder.target_fav = (RelativeLayout) convertView.findViewById(R.id.target_fav);
            convertView.setTag(holder);

        }
        else
        {
            holder = (TargetStockExcepAdapter.Holder) convertView.getTag();

        }
        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("", "in"));

        if (mlevel == 1)
        {
           holder.target_option.setText(arrayList.get(position).getPlanDept());

        }
        else if (mlevel == 2)
        {
            holder.target_option.setText(arrayList.get(position).getPlanCategory());

        }
        else if (mlevel == 3)
        {
            holder.target_option.setText(arrayList.get(position).getPlanClass());

        }
        else if (mlevel == 4)
        {
            holder.target_option.setText(arrayList.get(position).getBrandName());
        }
        else if (mlevel == 5)
        {
            holder.target_option.setText(arrayList.get(position).getBrandplanClass());
        }
        holder.target_SOH_U.setText("" + formatter.format(Math.round(arrayList.get(position).getStkOnhandQty())));
        holder.target_ROS_U.setText("" + String.format("%.1f", arrayList.get(position).getTargetROS()));
        holder.target_ROS.setText("" + String.format("%.1f", arrayList.get(position).getRos()));
        holder.target_Availability.setText("" + Math.round(arrayList.get(position).getAvailabilityPct()) + "%");

        // ---------------------click listener -------------------------
        return convertView;
    }

    private class Holder {
        TextView target_SOH_U, target_ROS_U, target_ROS, target_Availability, target_option;
        RelativeLayout target_fav;
    }

}
