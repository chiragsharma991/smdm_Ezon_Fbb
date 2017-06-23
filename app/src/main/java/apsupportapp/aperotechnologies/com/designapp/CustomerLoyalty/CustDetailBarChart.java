package apsupportapp.aperotechnologies.com.designapp.CustomerLoyalty;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by pamrutkar on 23/06/17.
 */
public class CustDetailBarChart extends AppCompatActivity
{
    TextView txt_cdb_name,txt_cdb_mobile,txt_cdb_email;
    RelativeLayout rel_cdb_back;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cd_barchart);
        getSupportActionBar().hide();
        initialise_UI();
    }

    private void initialise_UI()
    {
        txt_cdb_name = (TextView)findViewById(R.id.txt_cdb_name);
        txt_cdb_mobile = (TextView)findViewById(R.id.txt_cdb_mobileNo);
        txt_cdb_email = (TextView)findViewById(R.id.txt_cdb_email);
        txt_cdb_name.setText(CustomerDetailActivity.customerDetailsarray.get(0).getFullName());
        txt_cdb_mobile.setText(CustomerDetailActivity.customerDetailsarray.get(0).getMobileNumber());
        txt_cdb_email.setText(CustomerDetailActivity.customerDetailsarray.get(0).getEmailAddress());
    }
}
