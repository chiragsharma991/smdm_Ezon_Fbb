package apsupportapp.aperotechnologies.com.designapp.InternalServiceOppAudit;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.model.InspectionHistoryZonalRatings;
import apsupportapp.aperotechnologies.com.designapp.model.ViewDetails;

public class ViewDetailsActivity extends AppCompatActivity {

    private Spinner spinner_location;
    private ListView list_viewDetails;
    private RelativeLayout imageBtnBack1;
    private Context context;
    ViewDetailsAdapter viewdetailsAdapter;
    List<ViewDetails> arr_viewDetails;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);
        context = this;
        getSupportActionBar().hide();
        initialiseUI();
    }

    private void initialiseUI() {

        spinner_location = (Spinner) findViewById(R.id.spinner_location);
        list_viewDetails = (ListView) findViewById(R.id.list_viewDetails);
        imageBtnBack1 = (RelativeLayout) findViewById(R.id.imageBtnBack1);
        arr_viewDetails = new ArrayList<>();

        setValues();

        viewdetailsAdapter = new ViewDetailsAdapter(arr_viewDetails, context, list_viewDetails);
        list_viewDetails.setAdapter(viewdetailsAdapter);

        imageBtnBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setValues() {

        ViewDetails viewDetails = new ViewDetails();
        viewDetails.setTitle("FG Store Audit");
        viewDetails.setMall_name("FBB - ABC Mall");
        viewDetails.setLocation("Kandivali, Mumbai 400008");
        viewDetails.setRating("4.5");
        viewDetails.setCount_of_audit("2");

        arr_viewDetails.add(viewDetails);
    }
}
