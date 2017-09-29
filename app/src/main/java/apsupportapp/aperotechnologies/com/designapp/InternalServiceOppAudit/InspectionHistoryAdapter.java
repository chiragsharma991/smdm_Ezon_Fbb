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

public class InspectionHistoryAdapter extends BaseAdapter
{

    List<InspectionHistoryZonalRatings> arr_zonalratings;
    Context context;
    ListView list_inspectionHistory;
    int lastSelectedPos;



    public InspectionHistoryAdapter(List<InspectionHistoryZonalRatings> arr_zonalratings, Context context, ListView list_inspectionHistory)
    {
        this.arr_zonalratings = arr_zonalratings;
        this.context = context;
        this.list_inspectionHistory = list_inspectionHistory;
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
            view = mInflater.inflate(R.layout.adapter_inspection_history, null);

            viewHolder.rel_zonal = (RelativeLayout) view.findViewById(R.id.rel_zonal);
            viewHolder.rel_count =  (RelativeLayout) view.findViewById(R.id.rel_count);
            viewHolder.txt_zone_name = (TextView) view.findViewById(R.id.txt_zone_name);
            viewHolder.txt_avg_rating = (TextView) view.findViewById(R.id.txt_avg_rating);
            viewHolder.txt_audit_done = (TextView) view.findViewById(R.id.txt_audit_done);
            viewHolder.txt_count_fg_audit = (TextView) view.findViewById(R.id.txt_count_fg_audit);
            viewHolder.txt_count_external_audit = (TextView) view.findViewById(R.id.txt_count_external_audit);
            viewHolder.btn_view_details = (Button) view.findViewById(R.id.btn_view_details);

            viewHolder.btn_view_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });



            viewHolder.rel_zonal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.e("== "," "+lastSelectedPos +" "+list_inspectionHistory.getChildAt(lastSelectedPos));


                    if(i != lastSelectedPos)
                    {
                        RelativeLayout rel_main = (RelativeLayout) list_inspectionHistory.getChildAt(lastSelectedPos);
                        CardView cardview = (CardView) rel_main.getChildAt(0);
                        RelativeLayout rel = (RelativeLayout) cardview.getChildAt(0);
                        rel.getChildAt(2).setVisibility(View.GONE);
                    }

                    if(viewHolder.rel_count.getVisibility() == View.VISIBLE)
                    {
                        viewHolder.rel_count.setVisibility(View.GONE);
                    }
                    else
                    {
                        viewHolder.rel_count.setVisibility(View.VISIBLE);
                    }

                    lastSelectedPos = i;
                    Log.e("pos[0] "," "+lastSelectedPos);
                }
            });


            viewHolder.txt_zone_name.setText(arr_zonalratings.get(i).getZone_name());
            viewHolder.txt_avg_rating.setText(arr_zonalratings.get(i).getAvg_rating());
            viewHolder.txt_audit_done.setText("Audit done in : "+arr_zonalratings.get(i).getAudit_done());
            viewHolder.txt_count_fg_audit.setText("Count of FG Audit : "+arr_zonalratings.get(i).getCount_fg_audit());
            viewHolder.txt_count_external_audit.setText("Count of External Agency Audit : "+arr_zonalratings.get(i).getCount_external_audit());
            view.setTag(viewHolder);

        } else
        {
            viewHolder = (Holder) view.getTag();
        }

        return view;
    }

    private class Holder
    {
        RelativeLayout rel_zonal, rel_count;
        TextView txt_zone_name, txt_avg_rating, txt_audit_done, txt_count_fg_audit, txt_count_external_audit;
        Button btn_view_details;
    }
}
