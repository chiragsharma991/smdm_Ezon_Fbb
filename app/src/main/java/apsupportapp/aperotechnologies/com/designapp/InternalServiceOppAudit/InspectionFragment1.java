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
public class InspectionFragment1 extends Fragment {

    ListView list_inspectionHistory;
    InspectionHistoryAdapter adp_inspectionhistory;
    List<InspectionHistoryZonalRatings> arr_zonalratings;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View v = inflater.inflate(R.layout.inspection_tab, container, false);
        arr_zonalratings = new ArrayList<>();

        setLastMonth();

        list_inspectionHistory = (ListView) v.findViewById(R.id.list_inspectionHistory);
        adp_inspectionhistory = new InspectionHistoryAdapter(arr_zonalratings, getContext(), list_inspectionHistory);
        list_inspectionHistory.setAdapter(adp_inspectionhistory);


        return v;
    }



    private void setLastMonth()
    {
        InspectionHistoryZonalRatings inspectionHistoryZonalRatings = new InspectionHistoryZonalRatings();
        inspectionHistoryZonalRatings.setZone_name("North");
        inspectionHistoryZonalRatings.setAvg_rating("4");
        inspectionHistoryZonalRatings.setAudit_done("10/30");
        inspectionHistoryZonalRatings.setCount_fg_audit("1");
        inspectionHistoryZonalRatings.setCount_external_audit("5");

        arr_zonalratings.add(inspectionHistoryZonalRatings);
    }


}
