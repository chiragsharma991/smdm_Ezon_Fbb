package apsupportapp.aperotechnologies.com.designapp.BigBazaar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import apsupportapp.aperotechnologies.com.designapp.BigBazaar.Adapter.FragmentHomeAdapter;
import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by rkanawade on 02/11/17.
 */

public class FragmentHome extends Fragment{
    
    private Context context;
    private View v;
    private RecyclerView listHome;
    FragmentHomeAdapter fragmenthomeadapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getContext();
        View view = inflater.inflate(R.layout.fragment_home_bigbazar, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        v = getView();
        initialiseUI();
    }

    private void initialiseUI() {

        listHome = (RecyclerView) v.findViewById(R.id.listHome);
        listHome.setLayoutManager(new LinearLayoutManager(context));
        fragmenthomeadapter = new FragmentHomeAdapter(context,  listHome);
        listHome.setAdapter(fragmenthomeadapter);
    }

}
