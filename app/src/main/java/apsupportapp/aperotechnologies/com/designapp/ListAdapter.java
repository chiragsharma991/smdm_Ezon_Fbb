package apsupportapp.aperotechnologies.com.designapp;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import apsupportapp.aperotechnologies.com.designapp.CustomerEngagement.CustomerDetail;
import apsupportapp.aperotechnologies.com.designapp.ProductInformation.StyleModel;

import static apsupportapp.aperotechnologies.com.designapp.ProductInformation.StyleActivity.arrayList;


public class ListAdapter extends BaseAdapter implements Filterable
{
    private ArrayList<StyleModel> collectionList;
    ArrayList<StyleModel> mStringFilterList;
    private LayoutInflater mInflater;
    private ValueFilter valueFilter;
    Context context;

    public ListAdapter(ArrayList<StyleModel> mStringList, Context context) {

        this.context = context;
        this.collectionList = mStringList;
        this.mStringFilterList = collectionList;
//        mInflater = LayoutInflater.from(context);
        getFilter();
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
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Holder viewHolder;
        if (convertView == null)
        {
            viewHolder = new Holder();
            convertView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_subdept_listview, null);
            viewHolder.nameTv = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (Holder) convertView.getTag();
        }
        StyleModel styleModel = arrayList.get(position);
//        Log.e("getView: ", ""+position);
        viewHolder.nameTv.setText(styleModel.getCollectionName());
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
                ArrayList<StyleModel> filterList = new ArrayList<StyleModel>();

                for (int i = 0; i < mStringFilterList.size(); i++)
                {
                    if (mStringFilterList.get(i).getCollectionName().toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filterList.add(mStringFilterList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
//                Log.e("performFiltering: ",""+filterList.size());

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
            arrayList = (ArrayList<StyleModel>) results.values;
            notifyDataSetChanged();
        }
    }
}