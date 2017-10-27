package apsupportapp.aperotechnologies.com.designapp.InternalServiceOppAudit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.model.InspectionHistoryZonalRatings;
import apsupportapp.aperotechnologies.com.designapp.model.ViewDetails;

/**
 * Created by rkanawade on 10/10/17.
 */

public class ViewDetailsAdapter extends BaseAdapter {

    Context context;
    List<ViewDetails> arr_viewDetails;
    ListView list_viewDetails;


    public ViewDetailsAdapter(List<ViewDetails> arr_viewDetails, Context context, ListView list_viewDetails) {

        this.context = context;
        this.list_viewDetails = list_viewDetails;
        this.arr_viewDetails = arr_viewDetails;

    }

    @Override
    public int getCount() {
        return arr_viewDetails.size();
    }

    @Override
    public Object getItem(int position) {
        if(arr_viewDetails != null && arr_viewDetails.size() > 0)
        {
            return arr_viewDetails.get(position);
        }
        else
        {
            return 0;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        final Holder viewHolder;
        LayoutInflater mInflater;

        if (view == null)
        {

            viewHolder = new Holder();
            mInflater = LayoutInflater.from(context);
            view = mInflater.inflate(R.layout.adapter_view_details, null);

            viewHolder.card_viewdetails = (CardView) view.findViewById(R.id.card_viewdetails);
            viewHolder.txt_mallName = (TextView) view.findViewById(R.id.txt_mallName);
            viewHolder.txt_mallName1 =  (TextView) view.findViewById(R.id.txt_mallName1);
            viewHolder.txt_location = (TextView) view.findViewById(R.id.txt_location);
            viewHolder.txt_location1 = (TextView) view.findViewById(R.id.txt_location1);
            viewHolder.txt_rating = (TextView) view.findViewById(R.id.txt_rating);
            viewHolder.txt_rating1 = (TextView) view.findViewById(R.id.txt_rating1);
            viewHolder.txt_noOfAudit = (TextView) view.findViewById(R.id.txt_noOfAudit);
            viewHolder.txt_noOfAudit1 = (TextView) view.findViewById(R.id.txt_noOfAudit1);
            viewHolder.txt_title = (TextView) view.findViewById(R.id.txt_title);

            viewHolder.txt_mallName.setText(arr_viewDetails.get(i).getMall_name());
            viewHolder.txt_location.setText(arr_viewDetails.get(i).getLocation());
            viewHolder.txt_rating.setText("Average Rating : "+arr_viewDetails.get(i).getRating());
            viewHolder.txt_noOfAudit.setText("No. of Audit : "+arr_viewDetails.get(i).getCount_of_audit());
            viewHolder.txt_mallName1.setText(arr_viewDetails.get(i).getMall_name());
            viewHolder.txt_location1.setText(arr_viewDetails.get(i).getLocation());
            viewHolder.txt_rating1.setText("Average Rating : "+arr_viewDetails.get(i).getRating());
            viewHolder.txt_noOfAudit1.setText("No. of Audit : "+arr_viewDetails.get(i).getCount_of_audit());

            viewHolder.card_viewdetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent view_tables = new Intent(context, ViewDetailsTableActivity.class);
                    context.startActivity(view_tables);
                }
            });

            view.setTag(viewHolder);

        }
        else
        {
            viewHolder = (Holder) view.getTag();
        }

        return view;
    }

    private class Holder
    {
        TextView txt_mallName, txt_mallName1, txt_location, txt_location1, txt_rating, txt_rating1, txt_noOfAudit, txt_noOfAudit1, txt_title;
        CardView card_viewdetails;
    }
}
