package apsupportapp.aperotechnologies.com.designapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rkanawade on 21/09/17.
 */

public class StoreListAdapter extends BaseAdapter{

    private final Context context;
    private final ArrayList<String> storelist_data;

    public StoreListAdapter(Context context, ArrayList<String> storelist_data)
    {
        this.context=context;
        this.storelist_data=storelist_data;
    }

    @Override
    public int getCount() {
        return storelist_data.size();
    }

    @Override
    public Object getItem(int i)
    {
        return i;
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        //LayoutInflater inflater = getLayoutInflater();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View row;
        row = inflater.inflate(R.layout.simple_list_item, null, false);
        TextView title, detail;
        title = (TextView) row.findViewById(R.id.storeList);
        title.setText(storelist_data.get(i));
        return (row);
    }

}
