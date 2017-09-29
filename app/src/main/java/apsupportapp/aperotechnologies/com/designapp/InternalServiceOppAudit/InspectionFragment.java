package apsupportapp.aperotechnologies.com.designapp.InternalServiceOppAudit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.model.InspectionHistoryZonalRatings;

/**
 * Created by hasai on 26/09/17.
 */
public class InspectionFragment extends Fragment {

    ListView list_inspectionHistory;
    InspectionHistoryAdapter adp_inspectionhistory;
    List<InspectionHistoryZonalRatings> arr_zonalratings;
    int instance;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View v = inflater.inflate(R.layout.inspection_tab, container, false);

        arr_zonalratings = new ArrayList<>();
//        Log.e("instance "," "+instance);

        setLastWeek();

        list_inspectionHistory = (ListView) v.findViewById(R.id.list_inspectionHistory);
        adp_inspectionhistory = new InspectionHistoryAdapter(arr_zonalratings, getContext(), list_inspectionHistory);
        list_inspectionHistory.setAdapter(adp_inspectionhistory);

        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        Log.e("instance =="," "+instance);
        super.setUserVisibleHint(isVisibleToUser);
    }

    public void setLastWeek()
    {
        InspectionHistoryZonalRatings inspectionHistoryZonalRatings = new InspectionHistoryZonalRatings();
        inspectionHistoryZonalRatings.setZone_name("North");
        inspectionHistoryZonalRatings.setAvg_rating("4");
        inspectionHistoryZonalRatings.setAudit_done("10/30");
        inspectionHistoryZonalRatings.setCount_fg_audit("1");
        inspectionHistoryZonalRatings.setCount_external_audit("5");

        arr_zonalratings.add(inspectionHistoryZonalRatings);

        InspectionHistoryZonalRatings inspectionHistoryZonalRatings1 = new InspectionHistoryZonalRatings();
        inspectionHistoryZonalRatings1.setZone_name("East");
        inspectionHistoryZonalRatings1.setAvg_rating("4");
        inspectionHistoryZonalRatings1.setAudit_done("10/30");
        inspectionHistoryZonalRatings1.setCount_fg_audit("1");
        inspectionHistoryZonalRatings1.setCount_external_audit("5");

        arr_zonalratings.add(inspectionHistoryZonalRatings1);

        InspectionHistoryZonalRatings inspectionHistoryZonalRatings2 = new InspectionHistoryZonalRatings();
        inspectionHistoryZonalRatings2.setZone_name("West");
        inspectionHistoryZonalRatings2.setAvg_rating("4");
        inspectionHistoryZonalRatings2.setAudit_done("10/30");
        inspectionHistoryZonalRatings2.setCount_fg_audit("1");
        inspectionHistoryZonalRatings2.setCount_external_audit("5");

        arr_zonalratings.add(inspectionHistoryZonalRatings2);

        InspectionHistoryZonalRatings inspectionHistoryZonalRatings3 = new InspectionHistoryZonalRatings();
        inspectionHistoryZonalRatings3.setZone_name("South");
        inspectionHistoryZonalRatings3.setAvg_rating("4");
        inspectionHistoryZonalRatings3.setAudit_done("10/30");
        inspectionHistoryZonalRatings3.setCount_fg_audit("1");
        inspectionHistoryZonalRatings3.setCount_external_audit("5");

        arr_zonalratings.add(inspectionHistoryZonalRatings3);
    }

}
