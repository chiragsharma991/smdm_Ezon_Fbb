package apsupportapp.aperotechnologies.com.designapp.StoreInspection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by pamrutkar on 18/04/17.
 */
public class InspectionDetailsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insp_details);
        getSupportActionBar().hide();
    }
}
