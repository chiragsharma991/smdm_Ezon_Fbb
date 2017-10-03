package apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.DashboardSnap.SnapDashboardActivity;

import apsupportapp.aperotechnologies.com.designapp.Httpcall.ApiPostRequest;
import apsupportapp.aperotechnologies.com.designapp.Httpcall.HttpPostResponse;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.SeasonCatalogue.mpm_model;

import static apsupportapp.aperotechnologies.com.designapp.DashboardSnap.SnapDashboardActivity.nestedScrollview;
import static apsupportapp.aperotechnologies.com.designapp.Httpcall.ApiSMS.req_sms_API;

/**
 * Created by pamrutkar on 17/07/17.
 */

public class PolicyExchangeRefund extends AppCompatActivity implements View.OnClickListener, HttpPostResponse {

    private Context context;
    private RelativeLayout imageBtnBack1;
    private TextView txt_empty_product, txt_empty_exchange;
    private EditText edt_customer_mobile_number, edt_remarks, edt_first_name, edt_last_name;
    private RadioGroup radioCallbacks, radioExchange, radioProduct;
    private RadioButton radioYes, radioNo, radioExchangeYes, radioExchangeNo, radioProductYes, radioProductNo;
    private Button btn_submit, btn_cancel;
    private LinearLayout linear_toolbar;
    private ScrollView scrollView;
    private TextInputLayout input_remarks;
    private String remark, remarks_text, SelectedStoreCode, storeCode, store_Code;
    private Spinner spinner_reasons;
    private String TAG = "PolicyExchangeRefund";
    private RequestQueue queue;
    ArrayAdapter<String> adp3;
    private String userId, bearertoken, geoLeveLDesc, store, user_trim;
    private String customerFeedback, customerNumber, customerRemarks, customerName, customerLastname,
                   customerCallBack, customerExchangeDone, customerProductVerified, exchangeReason, customerArcDate;
    private TextView incorrect_phone, incorrect_remark, storedescription;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy_exchange);
        getSupportActionBar().hide();
        getSupportActionBar().setElevation(0);
        context = this;
        statusbar();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        initializeUI();

    }

    private void initializeUI() {

        if(getIntent().getExtras().getString("storeCode") != null )
        {
            storeCode = getIntent().getExtras().getString("storeCode");
            store_Code = storeCode.substring(0,4);
            Log.i(TAG, "storeCode: "+storeCode );
        }
        imageBtnBack1 = (RelativeLayout) findViewById(R.id.imageBtnBack1);
        input_remarks = (TextInputLayout) findViewById(R.id.input_remarks);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        edt_customer_mobile_number = (EditText) findViewById(R.id.edt_customer_mobile_number);
        edt_remarks = (EditText) findViewById(R.id.edt_remarks);
        edt_first_name = (EditText) findViewById(R.id.edt_first_name);
        edt_last_name = (EditText) findViewById(R.id.edt_last_name);
        radioCallbacks = (RadioGroup) findViewById(R.id.radioCallbacks);
        radioExchange = (RadioGroup) findViewById(R.id.radioExchange);
        radioProduct = (RadioGroup) findViewById(R.id.radioProduct);
        radioYes = (RadioButton) findViewById(R.id.radioYes);
        radioNo = (RadioButton) findViewById(R.id.radioNo);
        radioExchangeYes = (RadioButton) findViewById(R.id.radioExchangeYes);
        radioExchangeNo = (RadioButton) findViewById(R.id.radioExchangeNo);
        radioProductYes = (RadioButton) findViewById(R.id.radioProductYes);
        radioProductNo = (RadioButton) findViewById(R.id.radioProductNo);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        incorrect_phone = (TextView) findViewById(R.id.txt_incorrect_phone);
        incorrect_remark = (TextView) findViewById(R.id.txt_incorrect_remark);
        txt_empty_product = (TextView) findViewById(R.id.txt_empty_product);
        txt_empty_exchange = (TextView) findViewById(R.id.txt_empty_exchange);
        spinner_reasons = (Spinner) findViewById(R.id.spinner_reasons);
        storedescription = (TextView) findViewById(R.id.txtStoreCode);
     //   storedescription.setText(store);
        linear_toolbar = (LinearLayout) findViewById(R.id.linear_toolbar);
        linear_toolbar.setVisibility(View.VISIBLE);

        imageBtnBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        incorrect_phone.setVisibility(View.GONE);
        incorrect_remark.setVisibility(View.GONE);

        btn_submit.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        edt_remarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager =  (InputMethodManager)context.getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(edt_remarks.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                // ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater =  getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_remark, null);
                dialogBuilder.setView(dialogView);

                final EditText edt_remark_dialog = (EditText) dialogView.findViewById(R.id.edt_remark_dialog);
                edt_remark_dialog.setFilters(new InputFilter[] { new InputFilter.LengthFilter(500)});

                final Button btn_submit = (Button) dialogView.findViewById(R.id.btn_submit);
                final RelativeLayout rel_edt = (RelativeLayout) dialogView.findViewById(R.id.rel_edt);
                remarks_text = edt_remarks.getText().toString().trim();
                Log.e("===","remarks_text "+remarks_text);
                if(!remarks_text.equals("")){
                    edt_remark_dialog.setText(remarks_text);
                }
                edt_remark_dialog.setSelection(edt_remark_dialog.getText().length());

                final AlertDialog alertDialog = dialogBuilder.create();

                rel_edt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    }
                });

                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        remark = edt_remark_dialog.getText().toString().trim();
                        Log.e("remark ",""+remark);
                        edt_remarks.setText(remark);
                        edt_remarks.setSelection(edt_remarks.getText().length());
                        edt_first_name.requestFocus();

                        alertDialog.dismiss();
                    }
                });
                alertDialog.setCancelable(true);
                alertDialog.show();
            }
        });

        input_remarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager =  (InputMethodManager)context.getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(edt_remarks.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                // ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater =  getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_remark, null);
                dialogBuilder.setView(dialogView);

                final EditText edt_remark_dialog = (EditText) dialogView.findViewById(R.id.edt_remark_dialog);
                edt_remark_dialog.setFilters(new InputFilter[] { new InputFilter.LengthFilter(500)});

                final Button btn_submit = (Button) dialogView.findViewById(R.id.btn_submit);
                final RelativeLayout rel_edt = (RelativeLayout) dialogView.findViewById(R.id.rel_edt);

                remarks_text = edt_remarks.getText().toString().trim();
                Log.e("===","remarks_text "+remarks_text);

                if(!remarks_text.equals("")){
                    edt_remark_dialog.setText(remarks_text);
                }
                edt_remark_dialog.setSelection(edt_remark_dialog.getText().length());

                final AlertDialog alertDialog = dialogBuilder.create();

                rel_edt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    }
                });

                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        remark = edt_remark_dialog.getText().toString().trim();
                        Log.e("remark ",""+remark);
                        edt_remarks.setText(remark);
                        edt_remarks.setSelection(edt_remarks.getText().length());
                        edt_first_name.requestFocus();

                        alertDialog.dismiss();
                    }
                });
                alertDialog.setCancelable(true);
                alertDialog.show();
            }
        });


        edt_remarks.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    customerNumber = edt_customer_mobile_number.getText().toString().replaceAll("\\s+", "").trim();
                    if (!customerNumber.equals("")) {
                        if (customerNumber.length() < 10) {

                            incorrect_phone.setText(getResources().getString(R.string.customer_feedback_digit));
                            incorrect_phone.setVisibility(View.VISIBLE);
                            edt_customer_mobile_number.setBackgroundResource(R.drawable.edittext_red_border);
                        } else {
                            remarks_text = edt_remarks.getText().toString().trim();
                            Log.e("===","remarks_text "+remarks_text);
                            incorrect_phone.setVisibility(View.GONE);
                            edt_customer_mobile_number.setBackgroundResource(R.drawable.edittext_border);
                            InputMethodManager inputMethodManager =  (InputMethodManager)context.getSystemService(INPUT_METHOD_SERVICE);
                            inputMethodManager.toggleSoftInputFromWindow(edt_remarks.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater =  getLayoutInflater();
                            final View dialogView = inflater.inflate(R.layout.dialog_remark, null);
                            dialogBuilder.setView(dialogView);

                            final EditText edt_remark_dialog = (EditText) dialogView.findViewById(R.id.edt_remark_dialog);
                            edt_remark_dialog.setFilters(new InputFilter[] { new InputFilter.LengthFilter(500)});

                            final Button btn_submit = (Button) dialogView.findViewById(R.id.btn_submit);
                            final RelativeLayout rel_edt = (RelativeLayout) dialogView.findViewById(R.id.rel_edt);
                            if(!remarks_text.equals("")){
                                edt_remark_dialog.setText(remarks_text);
                            }
                            edt_remark_dialog.setSelection(edt_remark_dialog.getText().length());

                            final AlertDialog alertDialog = dialogBuilder.create();

                            rel_edt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                                }
                            });

                            btn_submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                                    remark = edt_remark_dialog.getText().toString().trim();
                                    Log.e("remark ",""+remark);
                                    edt_remarks.setText(remark);
                                    edt_remarks.setSelection(edt_remarks.getText().length());
                                    edt_first_name.requestFocus();

                                    alertDialog.dismiss();
                                }
                            });
                            alertDialog.setCancelable(true);
                            alertDialog.show();
                        }
                    }
                    else{
                        remarks_text = edt_remarks.getText().toString().trim();
                        Log.e("===","remarks_text "+remarks_text);
                        incorrect_phone.setVisibility(View.GONE);
                        edt_customer_mobile_number.setBackgroundResource(R.drawable.edittext_border);
                        InputMethodManager inputMethodManager =  (InputMethodManager)context.getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.toggleSoftInputFromWindow(edt_remarks.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                        // ...Irrelevant code for customizing the buttons and title
                        LayoutInflater inflater =  getLayoutInflater();
                        final View dialogView = inflater.inflate(R.layout.dialog_remark, null);
                        dialogBuilder.setView(dialogView);

                        final EditText edt_remark_dialog = (EditText) dialogView.findViewById(R.id.edt_remark_dialog);
                        edt_remark_dialog.setFilters(new InputFilter[] { new InputFilter.LengthFilter(500)});

                        final Button btn_submit = (Button) dialogView.findViewById(R.id.btn_submit);
                        final RelativeLayout rel_edt = (RelativeLayout) dialogView.findViewById(R.id.rel_edt);
                        if(!remarks_text.equals("")){
                            edt_remark_dialog.setText(remarks_text);
                        }
                        edt_remark_dialog.setSelection(edt_remark_dialog.getText().length());

                        final AlertDialog alertDialog = dialogBuilder.create();

                        rel_edt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                            }
                        });

                        btn_submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                                remark = edt_remark_dialog.getText().toString().trim();
                                Log.e("remark ",""+remark);
                                edt_remarks.setText(remark);
                                edt_remarks.setSelection(edt_remarks.getText().length());
                                edt_first_name.requestFocus();

                                alertDialog.dismiss();
                            }
                        });
                        alertDialog.setCancelable(true);
                        alertDialog.show();
                    }

                }

            }
        });

        edt_customer_mobile_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                incorrect_phone.setVisibility(View.GONE);
                edt_customer_mobile_number.setBackgroundResource(R.drawable.edittext_border);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//
        edt_remarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                incorrect_remark.setVisibility(View.GONE);
                edt_remarks.setBackgroundResource(R.drawable.edittext_border);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        radioExchangeYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    txt_empty_exchange.setVisibility(View.GONE);
                    spinner_reasons.setVisibility(View.VISIBLE);
                    req_fetch_spinnerdata_YES_API();

                }

            }
        });

        radioExchangeNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    txt_empty_exchange.setVisibility(View.GONE);
                    spinner_reasons.setVisibility(View.VISIBLE);
                    req_fetch_spinnerdata_NO_API();

                }

            }
        });


        radioProductYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    txt_empty_product.setVisibility(View.GONE);
                }

            }
        });

        radioProductNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    txt_empty_product.setVisibility(View.GONE);
                }

            }
        });

        MainMethod();

    }

    public void getDetails() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        String currentDateandTime = sdf.format(new Date());

        customerFeedback = "3";  // fixed for notified feedback
        customerNumber = edt_customer_mobile_number.getText().toString();
        customerRemarks = edt_remarks.getText().toString();
        customerName = edt_first_name.getText().toString();
        customerLastname = edt_last_name.getText().toString();
        customerExchangeDone = radioExchangeYes.isChecked() ? "YES" : "NO";
        customerProductVerified =  radioProductYes.isChecked() ? "YES" : "NO";
        customerCallBack = radioYes.isChecked() ? "YES" : "NO";

        // customerArcDate = currentDateandTime;  //this will up to real time.
    }

    private void MainMethod() {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = sharedPreferences.getString("userId", "");
        user_trim = userId.substring(0,2);
        store = sharedPreferences.getString("storeDescription", "");
      //  SelectedStoreCode = store.trim().substring(0, 4);

        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLeveLDesc = sharedPreferences.getString("geoLeveLDesc", "");
        //  editor.putString("storeDescription",storeDescription);

        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
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

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_submit:
                submitData();
                break;

            case R.id.btn_cancel:
                cancelData();
                break;

        }

    }

    private void cancelData()
    {
        edt_customer_mobile_number.getText().clear();
        edt_remarks.getText().clear();
        edt_first_name.getText().clear();
        edt_last_name.getText().clear();
        radioExchangeYes.setChecked(true);
        radioProductYes.setChecked(true);
        radioYes.setChecked(true);
        edt_customer_mobile_number.requestFocus();
    }

    private void submitData() {
        scrollView.setFocusableInTouchMode(false);
        scrollView.fullScroll(View.FOCUS_UP);
        getDetails();
        incorrect_remark.setVisibility(View.GONE);
        incorrect_phone.setVisibility(View.GONE);

        if ((customerNumber.equals("") || customerNumber == null) || (customerRemarks.equals("") || customerRemarks == null))
        {

            if(customerNumber.equals("") || customerNumber == null)
            {
                incorrect_phone.setText(context.getResources().getString(R.string.customer_feedback_number));
                incorrect_phone.setVisibility(View.VISIBLE);
                edt_customer_mobile_number.setBackgroundResource(R.drawable.edittext_red_border);
            }

            if(customerRemarks.equals("") || customerRemarks == null)
            {
                incorrect_remark.setText(context.getResources().getString(R.string.customer_feedback_remarks));
                incorrect_remark.setVisibility(View.VISIBLE);
                edt_remarks.setBackgroundResource(R.drawable.edittext_red_border);
            }

            if(!customerNumber.equals(""))
            {

                if (customerNumber.length() < 10)
                {

                    incorrect_phone.setText(getResources().getString(R.string.customer_feedback_digit));
                    incorrect_phone.setVisibility(View.VISIBLE);
                    edt_customer_mobile_number.setBackgroundResource(R.drawable.edittext_red_border);

                }
            }

            if(!radioExchangeYes.isChecked() && !radioExchangeNo.isChecked()){

                txt_empty_exchange.setVisibility(View.VISIBLE);
            }

            if(!radioProductYes.isChecked() && !radioProductNo.isChecked()){

                txt_empty_product.setVisibility(View.VISIBLE);

            }

            if(radioExchangeYes.isChecked() && spinner_reasons.getSelectedItem().equals("Reason for Exchange"))
            {

                txt_empty_exchange.setVisibility(View.VISIBLE);
                txt_empty_exchange.setText("Please select reason for exchange");

            }
            if(radioExchangeNo.isChecked() && spinner_reasons.getSelectedItem().equals("Reason for store refusal")){

                txt_empty_exchange.setVisibility(View.VISIBLE);
                txt_empty_exchange.setText("Please select reason for store refusal");

            }

        }
        else if(customerNumber.length() < 10)
        {

            incorrect_phone.setText(getResources().getString(R.string.customer_feedback_digit));
            incorrect_phone.setVisibility(View.VISIBLE);
            edt_customer_mobile_number.setBackgroundResource(R.drawable.edittext_red_border);

        }
        else if(!radioExchangeYes.isChecked() && !radioExchangeNo.isChecked()){

            txt_empty_exchange.setVisibility(View.VISIBLE);
        }

        else if(!radioProductYes.isChecked() && !radioProductNo.isChecked()){

            txt_empty_product.setVisibility(View.VISIBLE);

        }

        else if(radioExchangeYes.isChecked() && spinner_reasons.getSelectedItem().equals("Reason for Exchange"))
        {

            txt_empty_exchange.setVisibility(View.VISIBLE);
            txt_empty_exchange.setText("Please select reason for exchange");

        }
        else if(radioExchangeNo.isChecked() && spinner_reasons.getSelectedItem().equals("Reason for store refusal")){

            txt_empty_exchange.setVisibility(View.VISIBLE);
            txt_empty_exchange.setText("Please select reason for store refusal");

        }
        else
        {
            exchangeReason = spinner_reasons.getSelectedItem().toString();
            incorrect_remark.setVisibility(View.GONE);
            incorrect_phone.setVisibility(View.GONE);
            txt_empty_exchange.setVisibility(View.GONE);
            txt_empty_product.setVisibility(View.GONE);
            edt_customer_mobile_number.setBackgroundResource(R.drawable.edittext_border);
            edt_remarks.setBackgroundResource(R.drawable.edittext_border);
            Log.e("submitData: json is "," " + getObject().toString());
            if (Reusable_Functions.chkStatus(context))
            {
                mpm_model model = new mpm_model();
                ApiCallBack(getObject(), 0);// id is zero.

            } else
            {
                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public JSONObject getObject() {

        // totoal is 14 contain and 3 extra like : feedback id,storecode,arcDate

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("feedbackKey", customerFeedback);
            jsonObject.put("storeCode", store_Code);
            jsonObject.put("attribute1", customerNumber);
            jsonObject.put("attribute2", customerRemarks);
            jsonObject.put("attribute3", customerName);
            jsonObject.put("attribute4", customerLastname);
            jsonObject.put("attribute14", customerCallBack);
            jsonObject.put("attribute15", customerExchangeDone);
            jsonObject.put("attribute16", customerProductVerified);
            jsonObject.put("attribute17", exchangeReason);

          //  jsonObject.put("arcDate", customerArcDate);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    private synchronized void ApiCallBack(JSONObject object, int id) {

        switch (id) {

            case 0:   //total values

                String url = ConstsCore.web_url + "/v1/save/feedback/" + userId;
                ApiPostRequest api_request = new ApiPostRequest(context, bearertoken, url, TAG, queue, id, object, this);
                break;

            default:
                break;

        }
    }

    @Override
    public void PostResponse(JSONObject response) {
        Log.e(TAG, "PostResponse: success");
        String result = null;
        try {
            result = response.getString("status");
            Reusable_Functions.displayToast(context, result);
           // SelectedStoreCode = "2663";
            req_sms_API(userId, customerNumber, bearertoken, customerCallBack, context, "policyexchange",store_Code);

            cancelData();
            ((Activity) context).finish();
            nestedScrollview.fullScroll(View.FOCUS_DOWN);
//            Intent dashboard = new Intent(context, SnapDashboardActivity.class);
//            dashboard.putExtra("from","feedback");
//            startActivity(dashboard);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void PostDataNotFound() {

    }

    public void req_fetch_spinnerdata_YES_API()
    {
        final JSONObject[] jObj = {null};
        final Gson gson = new Gson();
        String url = "https://smdm.manthan.com/v1/display/returnreason/"+userId+"?feedbackKey=3&exchangeRequired=YES";
        Log.e("sms url ",""+url);

        JsonArrayRequest smsrequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.e("sms api "," "+response.toString());
                        List<String> listist = new ArrayList<String>();
                        listist.add(0, "Reason for Exchange");


                        for (int i = 0; i < response.length(); i++) {
                            try {
                                listist.add("" + response.get(i));
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        adp3 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listist);
                        adp3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_reasons.setAdapter(adp3);
                        spinner_reasons.setSelection(0);
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("sms api "," "+error.toString());

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", bearertoken);
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        int socketTimeout = 30000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        smsrequest.setRetryPolicy(policy);
        queue.add(smsrequest);

    }



    public void req_fetch_spinnerdata_NO_API()
    {
        final JSONObject[] jObj = {null};
        final Gson gson = new Gson();
        String url = "https://smdm.manthan.com/v1/display/returnreason/"+userId+"?feedbackKey=3&exchangeRequired=NO";
        Log.e("sms url ",""+url);

        JsonArrayRequest smsrequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.e("sms api "," "+response.toString());
                        List<String> listist = new ArrayList<String>();
                        listist.add(0, "Reason for store refusal");


                        for (int i = 0; i < response.length(); i++) {
                            try {
                                listist.add("" + response.get(i));
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        adp3 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listist);
                        adp3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_reasons.setAdapter(adp3);
                        spinner_reasons.setSelection(0);
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("sms api "," "+error.toString());

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", bearertoken);
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        int socketTimeout = 30000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        smsrequest.setRetryPolicy(policy);
        queue.add(smsrequest);

    }


}
