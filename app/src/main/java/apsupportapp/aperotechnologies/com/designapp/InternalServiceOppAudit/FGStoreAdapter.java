package apsupportapp.aperotechnologies.com.designapp.InternalServiceOppAudit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by rkanawade on 10/10/17.
 */

public class FGStoreAdapter extends BaseAdapter {

    private Context context;
    private ListView list_audit;


    public FGStoreAdapter(Context context, ListView list_audit) {

        this.context = context;
        this.list_audit = list_audit;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        return null;
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
            view = mInflater.inflate(R.layout.adapter_fgstore, null);


            viewHolder.txt_fbbstorecode = (TextView) view.findViewById(R.id.txt_fbbstorecode);
            viewHolder.txt_fbbstorename =  (TextView) view.findViewById(R.id.txt_fbbstorename);
            viewHolder.txt_fbbstoreloc = (TextView) view.findViewById(R.id.txt_fbbstoreloc);
            viewHolder.txt_date = (TextView) view.findViewById(R.id.txt_date);
            viewHolder.txt_name = (TextView) view.findViewById(R.id.txt_name);
            viewHolder.txt_place = (TextView) view.findViewById(R.id.txt_place);
            viewHolder.txt_score = (TextView) view.findViewById(R.id.txt_score);
            viewHolder.txt_fashion = (TextView) view.findViewById(R.id.txt_fashion);
            viewHolder.txt_display = (TextView) view.findViewById(R.id.txt_display);
            viewHolder.lin_table = (LinearLayout) view.findViewById(R.id.lin_table);


//            viewHolder.txt_mallName.setText(arr_viewDetails.get(i).getMall_name());
//            viewHolder.txt_location.setText(arr_viewDetails.get(i).getLocation());
//            viewHolder.txt_rating.setText("Average Rating : "+arr_viewDetails.get(i).getRating());
//            viewHolder.txt_noOfAudit.setText("No. of Audit : "+arr_viewDetails.get(i).getCount_of_audit());
//            viewHolder.txt_mallName1.setText(arr_viewDetails.get(i).getMall_name());
//            viewHolder.txt_location1.setText(arr_viewDetails.get(i).getLocation());
//            viewHolder.txt_rating1.setText("Average Rating : "+arr_viewDetails.get(i).getRating());
//            viewHolder.txt_noOfAudit1.setText("No. of Audit : "+arr_viewDetails.get(i).getCount_of_audit());
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
        TextView txt_fbbstorecode, txt_fbbstorename, txt_fbbstoreloc, txt_date, txt_name, txt_place, txt_score, txt_fashion, txt_display;
        LinearLayout lin_table;
    }
}
