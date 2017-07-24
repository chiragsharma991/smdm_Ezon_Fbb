package apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import apsupportapp.aperotechnologies.com.designapp.AboutUsActivity;
import apsupportapp.aperotechnologies.com.designapp.LoginActivity1;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesAnalysisActivity1;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesFilterActivity;

/**
 * Created by pamrutkar on 17/07/17.
 */

public class ProductAvailability_Notify extends AppCompatActivity{

    private Context context;
    private RelativeLayout imageBtnBack1;
    private EditText edt_customer_mobile_number, edt_remarks, edt_first_name, edt_last_name, edt_ean_number, edt_brand_name, edt_product_name, edt_size;
    private EditText edt_quantity, edt_color_option1, edt_color_option2, edt_fit, edt_style;
    private RadioGroup radioCallbacks;
    private RadioButton radioYes, radioNo;
    private Button btn_submit, btn_cancel;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_availability);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar().hide();
        getSupportActionBar().setElevation(0);
        context = this;
        statusbar();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        initializeUI();

    }

    private void initializeUI() {

        imageBtnBack1 = (RelativeLayout) findViewById(R.id.imageBtnBack1);
        edt_customer_mobile_number = (EditText) findViewById(R.id.edt_customer_mobile_number);
        edt_remarks = (EditText) findViewById(R.id.edt_remarks);
        edt_first_name = (EditText) findViewById(R.id.edt_first_name);
        edt_last_name = (EditText) findViewById(R.id.edt_last_name);
        edt_ean_number = (EditText) findViewById(R.id.edt_ean_number);
        edt_brand_name = (EditText) findViewById(R.id.edt_brand_name);
        edt_product_name = (EditText) findViewById(R.id.edt_product_name);
        edt_size = (EditText) findViewById(R.id.edt_size);
        edt_quantity = (EditText) findViewById(R.id.edt_quantity);
        edt_color_option1 = (EditText) findViewById(R.id.edt_color_option1);
        edt_color_option2 = (EditText) findViewById(R.id.edt_color_option2);
        edt_fit = (EditText) findViewById(R.id.edt_fit);
        edt_style = (EditText) findViewById(R.id.edt_style);
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

        edt_ean_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edt_ean_number.setBackgroundResource(R.drawable.edittext_red_border);
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

        edt_quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edt_quantity.setBackgroundResource(R.drawable.edittext_red_border);
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

    private void statusbar()
    {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.ezfbb_status_bar));
        }
    }


}
