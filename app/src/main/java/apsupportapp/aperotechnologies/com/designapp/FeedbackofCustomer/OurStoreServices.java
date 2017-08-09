package apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.Httpcall.ApiPostRequest;
import apsupportapp.aperotechnologies.com.designapp.Httpcall.HttpPostResponse;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.SeasonCatalogue.mpm_model;

/**
 * Created by pamrutkar on 17/07/17.
 */

public class OurStoreServices extends AppCompatActivity implements View.OnClickListener, HttpPostResponse {

    private Context context;
    private RelativeLayout imageBtnBack1;
    private EditText edt_customer_mobile_number, edt_remarks, edt_first_name, edt_last_name, edt_store_name;
    private RadioGroup radioCallbacks;
    private RadioButton radioYes, radioNo;
    private Button btn_submit, btn_cancel;
    private LinearLayout linear_toolbar;
    SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private ScrollView scrollView;
    private String TAG = "OurStoreServices";
    private TextView incorrect_phone, incorrect_remark, storedescription;
    private String userId, bearertoken, geoLeveLDesc, store;
    private String customerFeedback, customerNumber, customerRemarks, customerName, customerLastname,
                   customerCallBack, customerArcDate, customerStoreName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_service);
        getSupportActionBar().hide();
        getSupportActionBar().setElevation(0);
        context = this;
        statusbar();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        initializeUI();

    }

    private void initializeUI() {

        imageBtnBack1 = (RelativeLayout) findViewById(R.id.imageBtnBack1);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        edt_customer_mobile_number = (EditText) findViewById(R.id.edt_customer_mobile_number);
        edt_remarks = (EditText) findViewById(R.id.edt_remarks);
        edt_first_name = (EditText) findViewById(R.id.edt_first_name);
        edt_last_name = (EditText) findViewById(R.id.edt_last_name);
        edt_store_name = (EditText) findViewById(R.id.edt_store_name);
        radioCallbacks = (RadioGroup) findViewById(R.id.radioCallbacks);
        radioYes = (RadioButton) findViewById(R.id.radioYes);
        radioNo = (RadioButton) findViewById(R.id.radioNo);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        linear_toolbar = (LinearLayout) findViewById(R.id.linear_toolbar);
        linear_toolbar.setVisibility(View.VISIBLE);
        incorrect_phone = (TextView) findViewById(R.id.txt_incorrect_phone);
        incorrect_remark = (TextView) findViewById(R.id.txt_incorrect_remark);
        storedescription = (TextView) findViewById(R.id.txtStoreCode);

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

        edt_remarks.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    customerNumber = edt_customer_mobile_number.getText().toString().replaceAll("\\s+", "").trim();
                    if(!customerNumber.equals(""))
                    {
                        if (customerNumber.length() < 10) {
                            incorrect_phone.setText(getResources().getString(R.string.customer_feedback_digit));
                            incorrect_phone.setVisibility(View.VISIBLE);
                            edt_customer_mobile_number.setBackgroundResource(R.drawable.edittext_red_border);
                        } else {

                            incorrect_phone.setVisibility(View.GONE);
                            edt_customer_mobile_number.setBackgroundResource(R.drawable.edittext_border);
                        }
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

        MainMethod();

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

    public void getDetails() {

        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        String currentDateandTime = time.format(new Date());

        customerFeedback = "6";  // fixed for notified feedback
        customerNumber = edt_customer_mobile_number.getText().toString().replaceAll("\\s+", "").trim();
        customerRemarks = edt_remarks.getText().toString().replaceAll("\\s+", "").trim();
        customerName = edt_first_name.getText().toString().replaceAll("\\s+", "").trim();
        customerLastname = edt_last_name.getText().toString().replaceAll("\\s+", "").trim();
        customerStoreName = edt_store_name.getText().toString().replaceAll("\\s+", "").trim();
        customerCallBack = radioYes.isChecked() ? "YES" : "NO";
        customerArcDate = currentDateandTime;  //this will up to real time.
    }

    private void MainMethod() {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = sharedPreferences.getString("userId", "");
        Log.e("userId"," "+userId);
        store = sharedPreferences.getString("storeDescription", "");
        Log.e("store"," "+store);
        storedescription.setText(store);
        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLeveLDesc = sharedPreferences.getString("geoLeveLDesc", "");

        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
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

    private void submitData() {
        scrollView.setFocusableInTouchMode(true);
        scrollView.fullScroll(View.FOCUS_UP);
        getDetails();
        incorrect_remark.setVisibility(View.GONE);
        incorrect_phone.setVisibility(View.GONE);

        if ((customerNumber.equals("") || customerNumber == null) || (customerRemarks.equals("") || customerRemarks == null))
        {

            if(customerNumber.equals("") || customerNumber == null){
                incorrect_phone.setText(context.getResources().getString(R.string.customer_feedback_number));
                incorrect_phone.setVisibility(View.VISIBLE);
                edt_customer_mobile_number.setBackgroundResource(R.drawable.edittext_red_border);
            }

            if(customerRemarks.equals("") || customerRemarks == null){
                incorrect_remark.setText(context.getResources().getString(R.string.customer_feedback_remarks));
                incorrect_remark.setVisibility(View.VISIBLE);
                edt_remarks.setBackgroundResource(R.drawable.edittext_red_border);
            }

            if(!customerNumber.equals("")) {

                if (customerNumber.length() < 10) {

                    incorrect_phone.setText(getResources().getString(R.string.customer_feedback_digit));
                    incorrect_phone.setVisibility(View.VISIBLE);
                    edt_customer_mobile_number.setBackgroundResource(R.drawable.edittext_red_border);

                }
            }
        }
        else if(customerNumber.length() < 10)
        {

            incorrect_phone.setText(getResources().getString(R.string.customer_feedback_digit));
            incorrect_phone.setVisibility(View.VISIBLE);
            edt_customer_mobile_number.setBackgroundResource(R.drawable.edittext_red_border);

        }
        else
        {
            incorrect_remark.setVisibility(View.GONE);
            incorrect_phone.setVisibility(View.GONE);
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
            jsonObject.put("storeCode", "2663");
            jsonObject.put("attribute1", customerNumber);
            jsonObject.put("attribute2", customerRemarks);
            jsonObject.put("attribute3", customerName);
            jsonObject.put("attribute4", customerLastname);
            jsonObject.put("attribute18", customerStoreName);
            jsonObject.put("attribute14", customerCallBack);
            jsonObject.put("arcDate", customerArcDate);

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
            Toast.makeText(context, "" + result, Toast.LENGTH_LONG).show();
            cancelData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void PostDataNotFound() {

    }

    private void cancelData()
    {
        edt_customer_mobile_number.getText().clear();
        edt_remarks.getText().clear();
        edt_first_name.getText().clear();
        edt_last_name.getText().clear();
        edt_store_name.getText().clear();
        radioYes.setChecked(true);
        edt_customer_mobile_number.requestFocus();
    }


}
