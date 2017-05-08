package apsupportapp.aperotechnologies.com.designapp;



import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Created by hp1 on 21-01-2015.
 */
public class Coordinates_Fragment extends Fragment {
    private GridView gridView;
    private ArrayList arrayList;
    private Context context;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.coordinate_fragment,container,false);
        context=getActivity();
        arrayList=new ArrayList();
        for (int i=0;i<10;i++)
            arrayList.add(i++);
        Log.e("list size",""+arrayList.size());
        gridView=(GridView)v.findViewById(R.id.gridview);
        GridAdapter gridAdapter=new GridAdapter(getActivity(),R.layout.coordinate_gridview,arrayList);
        gridView.setAdapter(gridAdapter);
        return v;
    }
}
