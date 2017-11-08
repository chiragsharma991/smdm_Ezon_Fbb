package apsupportapp.aperotechnologies.com.designapp.CustomerEngagementReport;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by pamrutkar on 07/11/17.
 */

public class BasketAnalysis extends AppCompatActivity implements View.OnClickListener
{
    private RelativeLayout rel_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_analysis);
        getSupportActionBar().hide();
        intialise_ui();
    }

    private void intialise_ui()
    {
        rel_back = (RelativeLayout)findViewById(R.id.rel_back);
        rel_back.setOnClickListener(this);
    }



    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.rel_back:
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
