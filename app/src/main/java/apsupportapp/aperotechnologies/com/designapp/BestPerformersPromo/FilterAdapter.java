package apsupportapp.aperotechnologies.com.designapp.BestPerformersPromo;

/**
 * Created by csuthar on 23/11/16.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;


public class FilterAdapter extends BaseAdapter {

    private ArrayList<String> arrayList;

    //private List mStringFilterList;

    private LayoutInflater mInflater;
    Context context;
    private int Position;

    //private ValueFilter valueFilter;

    public FilterAdapter(ArrayList<String> arrayList, Context context) {

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
            convertView = mInflater.inflate(R.layout.activity_filter_child, null);

            holder.FilterMc=(TextView)convertView.findViewById(R.id.filterMc);



            convertView.setTag(holder);

        } else {
            holder=(Holder)convertView.getTag();

        }
        holder.FilterMc.setText(arrayList.get(position));



        // ---------------------click listener -------------------------


        return convertView;
    }



    private class Holder {

        TextView FilterMc;


    }

}
