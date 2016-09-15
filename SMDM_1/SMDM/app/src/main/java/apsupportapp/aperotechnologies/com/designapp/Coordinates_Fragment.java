package apsupportapp.aperotechnologies.com.designapp;

/**
 * Created by ifattehkhan on 22/08/16.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import apsupportapp.aperotechnologies.com.designapp.R;

import java.util.ArrayList;

/**
 * Created by hp1 on 21-01-2015.
 */
public class Coordinates_Fragment extends Fragment {
  GridView gridView;
    ArrayList arrayList;
    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
