package apsupportapp.aperotechnologies.com.designapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import apsupportapp.aperotechnologies.com.designapp.ProductInformation.Details_Fragment;

import java.util.ArrayList;


public class Coordinate_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    GridView gridView;
    ArrayList arrayList;
    Context context;

    public Coordinate_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.coordinate_fragment, container, false);
        context = getActivity();
        arrayList = new ArrayList();
        for (int i = 0; i < 10; i++)
            arrayList.add(i++);
        Log.e("list size", "" + arrayList.size());
        gridView = (GridView) v.findViewById(R.id.gridview);
        GridAdapter gridAdapter = new GridAdapter(getActivity(), R.layout.coordinate_gridview, arrayList);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), Details_Fragment.class);
                startActivity(intent);
            }
        });
        return v;
    }
}
