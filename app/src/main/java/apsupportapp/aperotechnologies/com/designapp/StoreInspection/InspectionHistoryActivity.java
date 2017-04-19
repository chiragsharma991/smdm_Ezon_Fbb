package apsupportapp.aperotechnologies.com.designapp.StoreInspection;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by pamrutkar on 17/04/17.
 */
public class InspectionHistoryActivity extends AppCompatActivity implements View.OnClickListener
{

    RelativeLayout rel_insp_history_btnBack;
    RecyclerView rv_insp_history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_history);
        getSupportActionBar().hide();
        initialise();

    }

    private void initialise() {
        rel_insp_history_btnBack = (RelativeLayout)findViewById(R.id.insp_history_btnback);
        rv_insp_history = (RecyclerView)findViewById(R.id.rv_insp_history);
        rel_insp_history_btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.insp_history_btnback :
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
