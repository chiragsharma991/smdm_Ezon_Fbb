package apsupportapp.aperotechnologies.com.designapp.CustomerLoyalty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by pamrutkar on 13/06/17.
 */
public class CustomerLookupActivity extends AppCompatActivity implements View.OnClickListener
{
    RelativeLayout rel_cust_btnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_lookup);
        getSupportActionBar().hide();
        initialiseUi();
    }

    private void initialiseUi()
    {
        rel_cust_btnBack = (RelativeLayout)findViewById(R.id.rel_cust_lookup_btnBack);
        rel_cust_btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.rel_cust_lookup_btnBack:
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
