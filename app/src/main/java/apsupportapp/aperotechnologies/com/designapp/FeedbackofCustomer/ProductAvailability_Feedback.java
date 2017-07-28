package apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer;

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
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.Httpcall.ApiPostRequest;
import apsupportapp.aperotechnologies.com.designapp.Httpcall.ApiRequest;
import apsupportapp.aperotechnologies.com.designapp.MPM.mpm_model;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;

/**
 * Created by rkanawade on 24/07/17.
 */

public class ProductAvailability_Feedback extends Fragment implements View.OnClickListener {
    private Context context;
    private EditText edt_customer_mobile_number, edt_remarks, edt_first_name, edt_last_name, edt_ean_number, edt_brand_name, edt_product_name, edt_size;
    private EditText edt_quantity, edt_color_option1, edt_color_option2, edt_fit, edt_style;
    private RadioGroup radioCallbacks;
    private RadioButton radioYes, radioNo;
    private Button btn_submit, btn_cancel;
    private LinearLayout linear_toolbar;
    private View v;
    private String TAG = "ProductAvailability_Feedback";
    private TextInputLayout layout_customer_mobile_number, layout_remarks;
    private SharedPreferences sharedPreferences;
    private String userId, bearertoken, geoLeveLDesc;
    private RequestQueue queue;
    private TextView incorrect_phone,incorrect_remark;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getContext();
        MainMethod();
        return inflater.inflate(R.layout.activity_product_availability, container, false);
    }

    private void MainMethod() {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLeveLDesc = sharedPreferences.getString("geoLeveLDesc", "");
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
        incorrect_phone.setVisibility(View.GONE);
        incorrect_remark.setVisibility(View.GONE);
        linear_toolbar = (LinearLayout) v.findViewById(R.id.linear_toolbar);
        linear_toolbar.setVisibility(View.GONE);

        btn_submit.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);


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
        incorrect_remark.setVisibility(View.GONE);
        incorrect_phone.setVisibility(View.GONE);
        if (edt_customer_mobile_number.length() == 0 || edt_customer_mobile_number.equals("") || edt_customer_mobile_number == null) {

            Log.e(TAG, "submitData: null");
            incorrect_phone.setVisibility(View.VISIBLE);
           // Reusable_Functions.MakeToast(context, "Please Enter your mobile number");

        } else if (edt_remarks.length() == 0 || edt_remarks.equals("") || edt_remarks == null) {

            Log.e(TAG, "edt_remarks: null");
            incorrect_remark.setVisibility(View.VISIBLE);
           // Reusable_Functions.MakeToast(context, "Please Enter Remarks");

        } else {
            incorrect_remark.setVisibility(View.GONE);
            incorrect_phone.setVisibility(View.GONE);
            Log.e(TAG, "submitData: json is " + getObject().toString());
            if (Reusable_Functions.chkStatus(context)) {
                mpm_model model = new mpm_model();
                ApiCallBack(getObject(), 0);            // id is zero.
                //String url = ConstsCore.web_url + "/v1/save/feedback/" + userId;

                //requestReceiverSubmitAPI(context,getObject(),url);

            } else {
                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
            }
        }


    }

    public JSONObject getObject() {

        // totoal is 14 contain and 3 extra like : feedback id,storecode,arcDate

        String customerFeedback = "1";  // fixed for notified feedback
        String customerNumber = edt_customer_mobile_number.getText().toString();
        String customerRemarks = edt_remarks.getText().toString();
        String customerName = edt_first_name.getText().toString();
        String customerLastname = edt_last_name.getText().toString();
        String customerEAN = edt_ean_number.getText().toString();
        String customerBrand = edt_brand_name.getText().toString();
        String customerProduct = edt_product_name.getText().toString();
        String customerSize = edt_size.getText().toString();
        String customerQty = edt_quantity.getText().toString();
        String customerColorOption1 = edt_color_option1.getText().toString();
        String customerColorOption2 = edt_color_option2.getText().toString();
        String customerFit = edt_fit.getText().toString();
        String customerStyle = edt_style.getText().toString();
        String customerCallBack = radioYes.isChecked() ? "YES" : "NO";
        String customerArcDate = "2017-07-05 06:52:22";  //this will up to real time.


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("feedbackKey", customerFeedback);
            jsonObject.put("storeCode", "2663");
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
                ApiPostRequest api_request = new ApiPostRequest(context, bearertoken, url, TAG, queue, id, object);  // 0 is id for identification

                break;

            default:
                break;


        }
    }





}
