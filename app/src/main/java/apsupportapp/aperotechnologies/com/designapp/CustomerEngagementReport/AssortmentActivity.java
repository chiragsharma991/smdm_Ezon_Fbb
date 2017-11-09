package apsupportapp.aperotechnologies.com.designapp.CustomerEngagementReport;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by pamrutkar on 07/11/17.
 */

public class AssortmentActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assortment);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
