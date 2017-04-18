package apsupportapp.aperotechnologies.com.designapp.StoreInspection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by pamrutkar on 18/04/17.
 */
public class InspectionDetailsActivity extends AppCompatActivity implements View.OnClickListener{
    private RelativeLayout insp_detls_btnback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insp_details);
        getSupportActionBar().hide();
        initialise();
    }

    private void initialise() {
        insp_detls_btnback = (RelativeLayout)findViewById(R.id.insp_detls_btnback);
        insp_detls_btnback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.insp_detls_btnback:
                onBackPressed();break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
