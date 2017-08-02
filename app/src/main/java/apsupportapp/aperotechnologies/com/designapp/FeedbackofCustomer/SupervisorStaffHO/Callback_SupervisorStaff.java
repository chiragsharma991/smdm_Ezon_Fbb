package apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.SupervisorStaffHO;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import apsupportapp.aperotechnologies.com.designapp.R;

public class Callback_SupervisorStaff extends AppCompatActivity {

    private TextView txt_mobile_number, txt_remarks, txt_cust_name, txt_employee_name, txt_store_name, txt_callback, txt_feedback_date, txt_email, txt_sms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback_supervisor_staff);

        initialiseUI();
    }

    private void initialiseUI() {

        txt_cust_name = (TextView) findViewById(R.id.txt_cust_name);
        txt_mobile_number = (TextView) findViewById(R.id.txt_mobile_number);
        txt_employee_name = (TextView) findViewById(R.id.txt_employee_name);
        txt_store_name = (TextView) findViewById(R.id.txt_store_name);
        txt_callback = (TextView) findViewById(R.id.txt_callback);
        txt_remarks = (TextView) findViewById(R.id.txt_remarks);
        txt_feedback_date = (TextView) findViewById(R.id.txt_feedback_date);
        txt_email = (TextView) findViewById(R.id.txt_email);
        txt_sms = (TextView) findViewById(R.id.txt_sms);

    }
}
