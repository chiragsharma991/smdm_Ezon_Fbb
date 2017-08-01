package apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.OurStoreServicesHO;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import apsupportapp.aperotechnologies.com.designapp.R;

public class Callback_OurStoreServices extends AppCompatActivity {

    private TextView txt_mobile_number, txt_remarks, txt_cust_name, txt_store_number, txt_callback, txt_feedback_date, txt_email, txt_sms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback_our_store_services);

        initialiseUI();
    }

    private void initialiseUI() {

        txt_mobile_number = (TextView) findViewById(R.id.txt_mobile_number);
        txt_remarks = (TextView) findViewById(R.id.txt_remarks);
        txt_cust_name = (TextView) findViewById(R.id.txt_cust_name);
        txt_store_number = (TextView) findViewById(R.id.txt_store_number);
        txt_callback = (TextView) findViewById(R.id.txt_callback);
        txt_feedback_date = (TextView) findViewById(R.id.txt_feedback_date);
        txt_email = (TextView) findViewById(R.id.txt_email);
        txt_sms = (TextView) findViewById(R.id.txt_sms);

    }
}
