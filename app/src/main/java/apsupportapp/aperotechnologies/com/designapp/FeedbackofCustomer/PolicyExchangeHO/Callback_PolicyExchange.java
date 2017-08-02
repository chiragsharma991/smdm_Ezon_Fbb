package apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.PolicyExchangeHO;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import apsupportapp.aperotechnologies.com.designapp.R;

public class Callback_PolicyExchange extends AppCompatActivity {

    private TextView txt_mobile_number, txt_exchange, txt_product_verified, txt_remarks, txt_cust_name, txt_callback, txt_feedback_date, txt_email, txt_sms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback_policy_exchange_ho);
        
        initializeUI();
    }

    private void initializeUI() {

        txt_mobile_number = (TextView) findViewById(R.id.txt_mobile_number);
        txt_exchange = (TextView) findViewById(R.id.txt_exchange);
        txt_product_verified = (TextView) findViewById(R.id.txt_product_verified);
        txt_remarks = (TextView) findViewById(R.id.txt_remarks);
        txt_cust_name = (TextView) findViewById(R.id.txt_cust_name);
        txt_callback = (TextView) findViewById(R.id.txt_callback);
        txt_feedback_date = (TextView) findViewById(R.id.txt_feedback_date);
        txt_email = (TextView) findViewById(R.id.txt_email);
        txt_sms = (TextView) findViewById(R.id.txt_sms);

    }
}
