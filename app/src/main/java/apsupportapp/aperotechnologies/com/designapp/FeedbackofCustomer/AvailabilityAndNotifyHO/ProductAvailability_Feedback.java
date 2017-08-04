package apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.AvailabilityAndNotifyHO;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
 * Created by rkanawade on 24/07/17.
 */

public class ProductAvailability_Feedback extends Fragment implements View.OnClickListener, HttpPostResponse {
    private Context context;
    private EditText edt_customer_mobile_number, edt_remarks, edt_first_name, edt_last_name, edt_ean_number, edt_brand_name, edt_product_name, edt_size;
    private EditText edt_quantity, edt_color_option1, edt_color_option2, edt_fit, edt_style;
    private RadioGroup radioCallbacks;
    private RadioButton radioYes, radioNo;
    private Button btn_submit, btn_cancel;
    private LinearLayout linear_toolbar;
    private View v;
    private String TAG = "ProductAvailability";
    private TextInputLayout layout_customer_mobile_number, layout_remarks;
    private SharedPreferences sharedPreferences;
    private String userId, bearertoken, geoLeveLDesc, store;
    private RequestQueue queue;
    private TextView incorrect_phone, incorrect_remark, storedescription;
    private feedbackInterface mCallback;
    private ScrollView scrollView;
    private String customerFeedback, customerNumber, customerRemarks, customerName, customerLastname, customerEAN,
            customerBrand, customerProduct, customerSize, customerQty, customerColorOption1, customerColorOption2,
            customerFit, customerStyle, customerCallBack, customerArcDate;
    private boolean prefocus = false;  // enable focus automatically when submit button click at first time


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_product_availability, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        try {
            mCallback = (feedbackInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(this.toString()
                    + " must implement Interface");
        }
    }

    private void MainMethod() {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = sharedPreferences.getString("userId", "");
        store = sharedPreferences.getString("storeDescription", "");
        Log.e(TAG, "Storedesc: " + store);
        storedescription.setText(store);
        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLeveLDesc = sharedPreferences.getString("geoLeveLDesc", "");
        //  editor.putString("storeDescription",storeDescription);

        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        v = getView();
        initialiseUI();
    }

    private void initialiseUI() {

        scrollView = (ScrollView) v.findViewById(R.id.scrollView);
        edt_customer_mobile_number = (EditText) v.findViewById(R.id.edt_customer_mobile_number);
        edt_remarks = (EditText) v.findViewById(R.id.edt_remarks);
        layout_customer_mobile_number = (TextInputLayout) v.findViewById(R.id.input_customer_mobile_number);
        layout_remarks = (TextInputLayout) v.findViewById(R.id.input_remarks);
        edt_first_name = (EditText) v.findViewById(R.id.edt_first_name);
        edt_last_name = (EditText) v.findViewById(R.id.edt_last_name);
        edt_ean_number = (EditText) v.findViewById(R.id.edt_ean_number);
        edt_brand_name = (EditText) v.findViewById(R.id.edt_brand_name);
        edt_product_name = (EditText) v.findViewById(R.id.edt_product_name);
        edt_size = (EditText) v.findViewById(R.id.edt_size);
        edt_quantity = (EditText) v.findViewById(R.id.edt_quantity);
        edt_color_option1 = (EditText) v.findViewById(R.id.edt_color_option1);
        edt_color_option2 = (EditText) v.findViewById(R.id.edt_color_option2);
        edt_fit = (EditText) v.findViewById(R.id.edt_fit);
        edt_style = (EditText) v.findViewById(R.id.edt_style);
        radioCallbacks = (RadioGroup) v.findViewById(R.id.radioCallbacks);
        radioYes = (RadioButton) v.findViewById(R.id.radioYes);
        radioNo = (RadioButton) v.findViewById(R.id.radioNo);

        btn_submit = (Button) v.findViewById(R.id.btn_submit);
        btn_cancel = (Button) v.findViewById(R.id.btn_cancel);
        incorrect_phone = (TextView) v.findViewById(R.id.txt_incorrect_phone);
        incorrect_remark = (TextView) v.findViewById(R.id.txt_incorrect_remark);
        storedescription = (TextView) v.findViewById(R.id.txtStoreCode);
        incorrect_phone.setVisibility(View.GONE);
        incorrect_remark.setVisibility(View.GONE);
        linear_toolbar = (LinearLayout) v.findViewById(R.id.linear_toolbar);
        linear_toolbar.setVisibility(View.GONE);

        btn_submit.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);


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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_submit:
                submitData();
                break;

            case R.id.btn_cancel:
                cancelData();
                break;

        }
    }


    private void cancelData() {

    }


    private void submitData() {
        scrollView.setFocusableInTouchMode(true);
        scrollView.fullScroll(View.FOCUS_UP);
        //  scrollView.setDescendantFocusability(ViewGroup.FOCUS_UP);
        getDetails();
        // prefocus = true;
        incorrect_remark.setVisibility(View.GONE);
        incorrect_phone.setVisibility(View.GONE);

        if ((customerNumber.equals("") || customerNumber == null) || (customerRemarks.equals("") || customerRemarks == null)) {

            if (customerNumber.equals("") || customerNumber == null) {
                incorrect_phone.setText(context.getResources().getString(R.string.customer_feedback_number));
                incorrect_phone.setVisibility(View.VISIBLE);
                edt_customer_mobile_number.setBackgroundResource(R.drawable.edittext_red_border);
            }

            if (customerRemarks.equals("") || customerRemarks == null) {
                incorrect_remark.setText(context.getResources().getString(R.string.customer_feedback_remarks));
                incorrect_remark.setVisibility(View.VISIBLE);
                edt_remarks.setBackgroundResource(R.drawable.edittext_red_border);
            }

            if (!customerNumber.equals("")) {

                if (customerNumber.length() < 10) {

                    incorrect_phone.setText(getResources().getString(R.string.customer_feedback_digit));
                    incorrect_phone.setVisibility(View.VISIBLE);
                    edt_customer_mobile_number.setBackgroundResource(R.drawable.edittext_red_border);

                }
            }


        } else if (customerNumber.length() < 10) {

            incorrect_phone.setText(getResources().getString(R.string.customer_feedback_digit));
            incorrect_phone.setVisibility(View.VISIBLE);
            edt_customer_mobile_number.setBackgroundResource(R.drawable.edittext_red_border);

        } else {
            incorrect_remark.setVisibility(View.GONE);
            incorrect_phone.setVisibility(View.GONE);
            edt_customer_mobile_number.setBackgroundResource(R.drawable.edittext_border);
            edt_remarks.setBackgroundResource(R.drawable.edittext_border);
            Log.e("submitData: json is ", " " + getObject().toString());
            if (Reusable_Functions.chkStatus(context)) {
                mpm_model model = new mpm_model();
                ApiCallBack(getObject(), 0);// id is zero.

            } else {
                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void getDetails() {

        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss"); // kk for 24 hours & hh for 12 hours
        String currentDateandTime = time.format(new Date());

        customerFeedback = "1";  // fixed for notified feedback
        customerNumber = edt_customer_mobile_number.getText().toString().replaceAll("\\s+", "").trim();
        customerRemarks = edt_remarks.getText().toString().replaceAll("\\s+", "").trim();
        customerName = edt_first_name.getText().toString().replaceAll("\\s+", "").trim();
        customerLastname = edt_last_name.getText().toString().replaceAll("\\s+", "").trim();
        customerEAN = edt_ean_number.getText().toString().replaceAll("\\s+", "").trim();
        customerBrand = edt_brand_name.getText().toString().replaceAll("\\s+", "").trim();
        customerProduct = edt_product_name.getText().toString().replaceAll("\\s+", "").trim();
        customerSize = edt_size.getText().toString().replaceAll("\\s+", "").trim();
        customerQty = edt_quantity.getText().toString().replaceAll("\\s+", "").trim();
        customerColorOption1 = edt_color_option1.getText().toString().replaceAll("\\s+", "").trim();
        customerColorOption2 = edt_color_option2.getText().toString().replaceAll("\\s+", "").trim();
        customerFit = edt_fit.getText().toString().replaceAll("\\s+", "").trim();
        customerStyle = edt_style.getText().toString().replaceAll("\\s+", "").trim();
        customerCallBack = radioYes.isChecked() ? "YES" : "NO";
        customerArcDate = "2017-07-15 10:06:55";  //this will up to real time.
    }

    public JSONObject getObject() {

        // totoal is 14 contain and 3 extra like : feedback id,storecode,arcDate


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("feedbackKey", customerFeedback);
            jsonObject.put("storeCode", store.trim().substring(0,4));
            jsonObject.put("attribute1", customerNumber);
            jsonObject.put("attribute2", customerRemarks);
            jsonObject.put("attribute3", customerName);
            jsonObject.put("attribute4", customerLastname);
            jsonObject.put("attribute5", customerEAN);
            jsonObject.put("attribute6", customerBrand);
            jsonObject.put("attribute7", customerProduct);
            jsonObject.put("attribute8", customerSize);
            jsonObject.put("attribute9", customerQty);
            jsonObject.put("attribute10", customerColorOption1);
            jsonObject.put("attribute11", customerColorOption2);
            jsonObject.put("attribute12", customerFit);
            jsonObject.put("attribute13", customerStyle);
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
            clearData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void PostDataNotFound() {
        Log.e(TAG, "PostDataNotFound");
    }


    public interface feedbackInterface {

        void onTrigger(int position);

    }


    public void clearData() {

        edt_customer_mobile_number.getText().clear();
        edt_remarks.getText().clear();
        edt_first_name.getText().clear();
        edt_last_name.getText().clear();
        edt_ean_number.getText().clear();
        edt_brand_name.getText().clear();
        edt_product_name.getText().clear();
        edt_size.getText().clear();
        edt_quantity.getText().clear();
        edt_color_option1.getText().clear();
        edt_color_option2.getText().clear();
        edt_fit.getText().clear();
        edt_style.getText().clear();
        radioYes.setChecked(true);
        edt_customer_mobile_number.requestFocus();

    }


}
