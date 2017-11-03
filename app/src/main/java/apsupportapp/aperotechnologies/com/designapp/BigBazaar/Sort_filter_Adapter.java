package apsupportapp.aperotechnologies.com.designapp.BigBazaar;

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

import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.StoreAdapter;

/**
 * Created by pamrutkar on 02/11/17.
 */
public class Sort_filter_Adapter extends BaseAdapter
{
    private List mStringList;
    private List mStringFilterList;
    private LayoutInflater mInflater;

    public Sort_filter_Adapter(List mStringList, Context context) {

        this.mStringList = mStringList;
        this.mStringFilterList = mStringList;
        mInflater = LayoutInflater.from(context);

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
        Sort_filter_Adapter.Holder viewHolder;
        if (convertView == null)
        {
            viewHolder = new Sort_filter_Adapter.Holder();
            convertView = mInflater.inflate(R.layout.activity_subdept_listview, null);
            viewHolder.nameTv = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (Sort_filter_Adapter.Holder) convertView.getTag();
        }

        viewHolder.nameTv.setText(mStringList.get(position).toString());
        return convertView;
    }

    private class Holder {
        TextView nameTv;
    }

    //Returns a filter that can be used to constrain data with a filtering pattern.

    }
