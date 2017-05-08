package apsupportapp.aperotechnologies.com.designapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class GridAdapter extends BaseAdapter {
    Context context;
    int coordinate_gridview;
    ArrayList arrayList;

    public GridAdapter(Context context, int coordinate_gridview, ArrayList arrayList) {
        this.context = context;
        this.coordinate_gridview = coordinate_gridview;
        this.arrayList = arrayList;
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

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater lf = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = lf.inflate(R.layout.coordinate_gridview, null);

        return convertView;
    }

    public static class ViewHolder {
        ImageView imgView;
        TextView text1, text2, text3;
    }
}
