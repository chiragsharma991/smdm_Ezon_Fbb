package apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by pamrutkar on 17/07/17.
 */

public class SupervisiorStaff extends AppCompatActivity {
    private Context context;
    private RelativeLayout imageBtnBack1;
    private EditText edt_customer_mobile_number, edt_remarks, edt_first_name, edt_last_name, edt_emp_name, edt_store_name;
    private RadioGroup radioCallbacks;
    private RadioButton radioYes, radioNo;
    private Button btn_submit, btn_cancel;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_staff);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar().hide();
        getSupportActionBar().setElevation(0);
        context = this;
        //statusbar();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        initializeUI();

    }

    private void initializeUI() {

        imageBtnBack1 = (RelativeLayout) findViewById(R.id.imageBtnBack1);
        edt_customer_mobile_number = (EditText) findViewById(R.id.edt_customer_mobile_number);
        edt_remarks = (EditText) findViewById(R.id.edt_remarks);
        edt_first_name = (EditText) findViewById(R.id.edt_first_name);
        edt_last_name = (EditText) findViewById(R.id.edt_last_name);
        edt_emp_name = (EditText) findViewById(R.id.edt_emp_name);
        edt_store_name = (EditText) findViewById(R.id.edt_store_name);
        radioCallbacks = (RadioGroup) findViewById(R.id.radioCallbacks);
        radioYes = (RadioButton) findViewById(R.id.radioYes);
        radioNo = (RadioButton) findViewById(R.id.radioNo);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);


        imageBtnBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edt_customer_mobile_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edt_customer_mobile_number.setBackgroundResource(R.drawable.edittext_red_border);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_remarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edt_remarks.setBackgroundResource(R.drawable.edittext_red_border);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_first_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edt_first_name.setBackgroundResource(R.drawable.edittext_red_border);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_last_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edt_last_name.setBackgroundResource(R.drawable.edittext_red_border);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_emp_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edt_emp_name.setBackgroundResource(R.drawable.edittext_red_border);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_store_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edt_store_name.setBackgroundResource(R.drawable.edittext_red_border);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

}
