package apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by rkanawade on 25/07/17.
 */

public class SupervisorStaff_Reports extends Fragment {

    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getContext();

        return inflater.inflate(R.layout.fragment_supervisorstaff_reports, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        v = getView();
//        initialiseUI();
    }
}
