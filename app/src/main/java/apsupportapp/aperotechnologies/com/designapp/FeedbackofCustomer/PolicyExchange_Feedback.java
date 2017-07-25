package apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by rkanawade on 24/07/17.
 */

public class PolicyExchange_Feedback extends Fragment{
    private Context context;
    private EditText edt_customer_mobile_number, edt_remarks, edt_first_name, edt_last_name;
    private RadioGroup radioCallbacks, radioExchange, radioProduct;
    private RadioButton radioYes, radioNo, radioExchangeYes, radioExchangeNo, radioProductYes, radioProductNo;
    private Button btn_submit, btn_cancel;
    private LinearLayout linear_toolbar;
    SharedPreferences sharedPreferences;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getContext();

        return inflater.inflate(R.layout.activity_policy_exchange, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        v = getView();
        initializeUI();
    }

    private void initializeUI() {

        edt_customer_mobile_number = (EditText) v.findViewById(R.id.edt_customer_mobile_number);
        edt_remarks = (EditText) v.findViewById(R.id.edt_remarks);
        edt_first_name = (EditText) v.findViewById(R.id.edt_first_name);
        edt_last_name = (EditText) v.findViewById(R.id.edt_last_name);
        radioCallbacks = (RadioGroup) v.findViewById(R.id.radioCallbacks);
        radioExchange = (RadioGroup) v.findViewById(R.id.radioExchange);
        radioProduct = (RadioGroup) v.findViewById(R.id.radioProduct);
        radioYes = (RadioButton) v.findViewById(R.id.radioYes);
        radioNo = (RadioButton) v.findViewById(R.id.radioNo);
        radioExchangeYes = (RadioButton) v.findViewById(R.id.radioExchangeYes);
        radioExchangeNo = (RadioButton) v.findViewById(R.id.radioExchangeNo);
        radioProductYes = (RadioButton) v.findViewById(R.id.radioProductYes);
        radioProductNo = (RadioButton) v.findViewById(R.id.radioProductNo);
        btn_submit = (Button) v.findViewById(R.id.btn_submit);
        btn_cancel = (Button) v.findViewById(R.id.btn_cancel);
        linear_toolbar = (LinearLayout) v.findViewById(R.id.linear_toolbar);
        linear_toolbar.setVisibility(View.GONE);

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
