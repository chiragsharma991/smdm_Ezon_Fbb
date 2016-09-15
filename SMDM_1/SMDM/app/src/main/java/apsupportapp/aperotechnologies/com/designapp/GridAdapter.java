package apsupportapp.aperotechnologies.com.designapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import apsupportapp.aperotechnologies.com.designapp.R;

import java.util.ArrayList;

/**
 * Created by ifattehkhan on 22/08/16.
 */
public class GridAdapter extends BaseAdapter{
    Context context;
    int coordinate_gridview;
    ArrayList arrayList;
    public GridAdapter(Context context, int coordinate_gridview, ArrayList arrayList) {
        this.context=context;
        this.coordinate_gridview=coordinate_gridview;
        this.arrayList=arrayList;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final  ViewHolder view=null;
        LayoutInflater lf=(LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = lf.inflate(R.layout.coordinate_gridview, null);

//        view.imgView = (ImageView) convertView.findViewById(R.id.imageview);
//        view.text1 = (TextView) convertView.findViewById(R.id.textView);
//        view.text2 = (TextView) convertView.findViewById(R.id.textView1);
//        view.text3 = (TextView) convertView.findViewById(R.id.textView2);


        return convertView;
    }
    public static class ViewHolder
    {
        ImageView imgView;
        TextView text1,text2,text3;

    }
}
