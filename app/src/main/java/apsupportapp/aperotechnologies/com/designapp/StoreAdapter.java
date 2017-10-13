package apsupportapp.aperotechnologies.com.designapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class StoreAdapter extends BaseAdapter implements Filterable
{
    private List mStringList;
    private List mStringFilterList;
    private LayoutInflater mInflater;
    private ValueFilter valueFilter;

    public StoreAdapter(List mStringList, Context context) {

        this.mStringList = mStringList;
        this.mStringFilterList = mStringList;
        mInflater = LayoutInflater.from(context);
        getFilter();
    }

    //How many items are in the data set represented by this Adapter.
    @Override
    public int getCount() {
        return mStringList.size();
    }

    //Get the data item associated with the specified position in the data set.
    @Override
    public Object getItem(int position) {
        return mStringList.get(position);
    }

    //Get the row id associated with the specified position in the list.
    @Override
    public long getItemId(int position) {
        return position;
    }

    //Get a View that displays the data at the specified position in the data set.
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Holder viewHolder;
        if (convertView == null)
        {
            viewHolder = new Holder();
            convertView = mInflater.inflate(R.layout.activity_subdept_listview, null);
            viewHolder.nameTv = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (Holder) convertView.getTag();
        }

        viewHolder.nameTv.setText(mStringList.get(position).toString());
        return convertView;
    }

    private class Holder {
        TextView nameTv;
    }

    //Returns a filter that can be used to constrain data with a filtering pattern.
    @Override
    public Filter getFilter() {

        if (valueFilter == null)
        {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0)
            {
                List filterList = new ArrayList<String>();
                for (int i = 0; i < mStringFilterList.size(); i++)
                {
                    if (mStringFilterList.get(i).toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filterList.add(mStringFilterList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;

            }
            else
            {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;
        }

        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results)
        {
            mStringList = (ArrayList<String>) results.values;
            notifyDataSetChanged();
        }
    }
}