package apsupportapp.aperotechnologies.com.designapp.InternalServiceOppAudit;

import android.content.Context;
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

/**
 * Created by hasai on 26/09/17.
 */

public class ExternalAuditReviewAdapter extends BaseAdapter
{
    List<InspectionHistoryZonalRatings> arr_zonalratings;
    Context context;


    public ExternalAuditReviewAdapter(List<InspectionHistoryZonalRatings> arr_zonalratings, Context context, ListView list_externalauditorreview)
    {
        this.arr_zonalratings = arr_zonalratings;
        this.context = context;

    }

    @Override
    public int getCount() {
        return arr_zonalratings.size();
    }

    @Override
    public Object getItem(int i) {
        if(arr_zonalratings != null && arr_zonalratings.size() > 0)
        {
            return arr_zonalratings.get(i);
        }
        else
        {
            return 0;
        }

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        final  Holder viewHolder;
        LayoutInflater mInflater;

        if (view == null) {

            viewHolder = new Holder();
            mInflater = LayoutInflater.from(context);
            view = mInflater.inflate(R.layout.adapter_externalaudrev, null);

            viewHolder.txt_1 = (TextView) view.findViewById(R.id.txt_1);
            viewHolder.txt_2 = (TextView) view.findViewById(R.id.txt_2);
            viewHolder.txt_3 = (TextView) view.findViewById(R.id.txt_3);
            viewHolder.txt_4 = (TextView) view.findViewById(R.id.txt_4);

            Log.e("i "," "+i);

            viewHolder.txt_1.setText(arr_zonalratings.get(i).getZone_name());
            viewHolder.txt_2.setText(arr_zonalratings.get(i).getAvg_rating());
            viewHolder.txt_3.setText(arr_zonalratings.get(i).getAudit_done());
            viewHolder.txt_4.setText(arr_zonalratings.get(i).getCount_fg_audit());
            view.setTag(viewHolder);

        } else
        {
            viewHolder = (Holder) view.getTag();
        }

        return view;
    }

    private class Holder
    {
        TextView txt_1, txt_2, txt_3, txt_4;

    }
}
