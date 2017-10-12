package apsupportapp.aperotechnologies.com.designapp.InternalServiceOppAudit;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Spinner;

import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by rkanawade on 10/10/17.
 */

public class CompetitorStoreFragment extends Fragment {

    private Spinner spinner_storeformat, spinner_storename;
    private Context context;
    private ListView list_audit;
    FGStoreAdapter fgStoreAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View v = inflater.inflate(R.layout.fragment_fgstore, container, false);
        context = v.getContext();

        spinner_storeformat = (Spinner) v.findViewById(R.id.spinner_storeformat);
        spinner_storename = (Spinner) v.findViewById(R.id.spinner_storename);

//        list_audit = (ListView) v.findViewById(R.id.list_audit1);
//
//        fgStoreAdapter = new FGStoreAdapter(context, list_audit);
//        list_audit.setAdapter(fgStoreAdapter);

        return v;
    }
}
