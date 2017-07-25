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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import apsupportapp.aperotechnologies.com.designapp.DashboardSnap.App;
import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by pamrutkar on 17/07/17.
 */

public class Price_Promotion extends AppCompatActivity {
    private Context context;
    private RelativeLayout imageBtnBack1;
    private EditText edt_customer_mobile_number, edt_remarks, edt_first_name, edt_last_name, edt_brand_name, edt_product_name, edt_size;
    private EditText edt_color_option1, edt_color_option2, edt_fit, edt_style;
    private RadioGroup radioCallbacks;
    private RadioButton radioYes, radioNo;
    private Button btn_submit, btn_cancel;
    private LinearLayout linear_toolbar;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_promotion);
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
        edt_brand_name = (EditText) findViewById(R.id.edt_brand_name);
        edt_product_name = (EditText) findViewById(R.id.edt_product_name);
        edt_size = (EditText) findViewById(R.id.edt_size);
        edt_color_option1 = (EditText) findViewById(R.id.edt_color_option1);
        edt_color_option2 = (EditText) findViewById(R.id.edt_color_option2);
        edt_fit = (EditText) findViewById(R.id.edt_fit);
        edt_style = (EditText) findViewById(R.id.edt_style);
        radioCallbacks = (RadioGroup) findViewById(R.id.radioCallbacks);
        radioYes = (RadioButton) findViewById(R.id.radioYes);
        radioNo = (RadioButton) findViewById(R.id.radioNo);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        linear_toolbar = (LinearLayout) findViewById(R.id.linear_toolbar);
        linear_toolbar.setVisibility(View.VISIBLE);


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

        edt_brand_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edt_brand_name.setBackgroundResource(R.drawable.edittext_red_border);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_product_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edt_product_name.setBackgroundResource(R.drawable.edittext_red_border);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_size.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edt_size.setBackgroundResource(R.drawable.edittext_red_border);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_color_option1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edt_color_option1.setBackgroundResource(R.drawable.edittext_red_border);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_color_option2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edt_color_option2.setBackgroundResource(R.drawable.edittext_red_border);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_fit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edt_fit.setBackgroundResource(R.drawable.edittext_red_border);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_style.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edt_style.setBackgroundResource(R.drawable.edittext_red_border);
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
