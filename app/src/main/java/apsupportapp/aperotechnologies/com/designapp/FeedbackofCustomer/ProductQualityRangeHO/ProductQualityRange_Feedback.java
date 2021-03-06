package apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.ProductQualityRangeHO;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import apsupportapp.aperotechnologies.com.designapp.DashboardSnap.SnapDashboardActivity;
import apsupportapp.aperotechnologies.com.designapp.Httpcall.ApiPostRequest;
import apsupportapp.aperotechnologies.com.designapp.Httpcall.HttpPostResponse;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.SeasonCatalogue.mpm_model;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static apsupportapp.aperotechnologies.com.designapp.DashboardSnap.SnapDashboardActivity.nestedScrollview;
import static apsupportapp.aperotechnologies.com.designapp.Httpcall.ApiSMS.req_sms_API;

/**
 * Created by rkanawade on 25/07/17.
 */

public class ProductQualityRange_Feedback extends Fragment implements View.OnClickListener, HttpPostResponse {
    private Context context;
    private RelativeLayout imageBtnBack1;
    private EditText edt_customer_mobile_number, edt_remarks, edt_first_name, edt_last_name, edt_article_id, edt_brand_name, edt_product_name, edt_size;
    private EditText edt_color_option1, edt_color_option2, edt_fit, edt_style;
    private RadioGroup radioCallbacks;
    private RadioButton radioYes, radioNo;
    private Button btn_submit, btn_cancel;
    private LinearLayout linear_toolbar;
    SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private ScrollView scrollView;
    private String remark, SelectedStoreCode, storeCode, store_code;
    private TextInputLayout input_remarks;
    private String remarks_text;
    private String TAG = "ProductQualityRange";
    private TextView incorrect_phone, incorrect_remark, storedescription, txt_incorrect_lastname,txt_incorrect_name;
    private String userId, bearertoken, geoLeveLDesc, store;
    private String customerFeedback, customerNumber, customerRemarks, customerName, customerLastname, customerArticleId,
            customerBrand, customerProduct, customerSize, customerColorOption1, customerColorOption2,
            customerFit, customerStyle, customerCallBack, customerArcDate;
    private View v;

    public ProductQualityRange_Feedback(String storeCode) {

        this.storeCode = storeCode;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getContext();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        return inflater.inflate(R.layout.activity_product_quality_range, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        v = getView();
        initializeUI();
    }

    private void initializeUI() {

        edt_customer_mobile_number = (EditText) v.findViewById(R.id.edt_customer_mobile_number);
        input_remarks = (TextInputLayout) v.findViewById(R.id.input_remarks);
        scrollView = (ScrollView) v.findViewById(R.id.scrollView);
        edt_remarks = (EditText) v.findViewById(R.id.edt_remarks);
        edt_first_name = (EditText) v.findViewById(R.id.edt_first_name);
        edt_last_name = (EditText) v.findViewById(R.id.edt_last_name);
        edt_article_id = (EditText) v.findViewById(R.id.edt_article_id);
        edt_brand_name = (EditText) v.findViewById(R.id.edt_brand_name);
        edt_product_name = (EditText) v.findViewById(R.id.edt_product_name);
        edt_size = (EditText) v.findViewById(R.id.edt_size);
        edt_color_option1 = (EditText) v.findViewById(R.id.edt_color_option1);
        edt_color_option2 = (EditText) v.findViewById(R.id.edt_color_option2);
        edt_fit = (EditText) v.findViewById(R.id.edt_fit);
        edt_style = (EditText) v.findViewById(R.id.edt_style);
        radioCallbacks = (RadioGroup) v.findViewById(R.id.radioCallbacks);
        radioYes = (RadioButton) v.findViewById(R.id.radioYes);
        radioNo = (RadioButton) v.findViewById(R.id.radioNo);
        btn_submit = (Button) v.findViewById(R.id.btn_submit);
        btn_cancel = (Button) v.findViewById(R.id.btn_cancel);
        linear_toolbar = (LinearLayout) v.findViewById(R.id.linear_toolbar);
        linear_toolbar.setVisibility(View.GONE);
        incorrect_phone = (TextView) v.findViewById(R.id.txt_incorrect_phone);
        incorrect_remark = (TextView) v.findViewById(R.id.txt_incorrect_remark);
        storedescription = (TextView) v.findViewById(R.id.txtStoreCode);
        txt_incorrect_lastname = (TextView) v.findViewById(R.id.txt_incorrect_lastname);
        txt_incorrect_name = (TextView) v.findViewById(R.id.txt_incorrect_name);

        incorrect_phone.setVisibility(View.GONE);
        incorrect_remark.setVisibility(View.GONE);
        txt_incorrect_lastname.setVisibility(View.GONE);
        txt_incorrect_name.setVisibility(View.GONE);
        btn_submit.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        edt_remarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager =  (InputMethodManager)context.getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(edt_remarks.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                // ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater =  getActivity().getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_remark, null);
                dialogBuilder.setView(dialogView);

                final EditText edt_remark_dialog = (EditText) dialogView.findViewById(R.id.edt_remark_dialog);
                edt_remark_dialog.setFilters(new InputFilter[] { new InputFilter.LengthFilter(500)});

                final Button btn_submit = (Button) dialogView.findViewById(R.id.btn_submit);
                final RelativeLayout rel_edt = (RelativeLayout) dialogView.findViewById(R.id.rel_edt);
                remarks_text = edt_remarks.getText().toString().trim();
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

                        edt_remarks.setText(remark);
                        edt_remarks.setSelection(edt_remarks.getText().length());
                        edt_article_id.requestFocus();
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
                LayoutInflater inflater =  getActivity().getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_remark, null);
                dialogBuilder.setView(dialogView);

                final EditText edt_remark_dialog = (EditText) dialogView.findViewById(R.id.edt_remark_dialog);
                edt_remark_dialog.setFilters(new InputFilter[] { new InputFilter.LengthFilter(500)});

                final Button btn_submit = (Button) dialogView.findViewById(R.id.btn_submit);
                final RelativeLayout rel_edt = (RelativeLayout) dialogView.findViewById(R.id.rel_edt);

                remarks_text = edt_remarks.getText().toString().trim();

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
                        edt_remarks.setText(remark);
                        edt_remarks.setSelection(edt_remarks.getText().length());
                        edt_article_id.requestFocus();

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
                            incorrect_phone.setVisibility(View.GONE);
                            edt_customer_mobile_number.setBackgroundResource(R.drawable.edittext_border);
                            InputMethodManager inputMethodManager =  (InputMethodManager)context.getSystemService(INPUT_METHOD_SERVICE);
                            inputMethodManager.toggleSoftInputFromWindow(edt_remarks.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater =  getActivity().getLayoutInflater();
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
                                    edt_remarks.setText(remark);
                                    edt_remarks.setSelection(edt_remarks.getText().length());
                                    edt_article_id.requestFocus();

                                    alertDialog.dismiss();
                                }
                            });
                            alertDialog.setCancelable(true);
                            alertDialog.show();
                        }
                    }
                    else{
                        remarks_text = edt_remarks.getText().toString().trim();
                        incorrect_phone.setVisibility(View.GONE);
                        edt_customer_mobile_number.setBackgroundResource(R.drawable.edittext_border);
                        InputMethodManager inputMethodManager =  (InputMethodManager)context.getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.toggleSoftInputFromWindow(edt_remarks.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                        // ...Irrelevant code for customizing the buttons and title
                        LayoutInflater inflater =  getActivity().getLayoutInflater();
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
                                edt_remarks.setText(remark);
                                edt_remarks.setSelection(edt_remarks.getText().length());
                                edt_article_id.requestFocus();

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

        edt_first_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txt_incorrect_name.setVisibility(View.GONE);
                edt_first_name.setBackgroundResource(R.drawable.edittext_border);
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
                txt_incorrect_lastname.setVisibility(View.GONE);
                edt_last_name.setBackgroundResource(R.drawable.edittext_border);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        MainMethod();
    }

    public void getDetails() {

        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        String currentDateandTime = time.format(new Date());

        customerFeedback = "2";  // fixed for notified feedback
        customerNumber = edt_customer_mobile_number.getText().toString();
        customerRemarks = edt_remarks.getText().toString();
        customerName = edt_first_name.getText().toString();
        customerLastname = edt_last_name.getText().toString();
        customerArticleId = edt_article_id.getText().toString();
        customerBrand = edt_brand_name.getText().toString();
        customerProduct = edt_product_name.getText().toString();
        customerSize = edt_size.getText().toString();
        customerColorOption1 = edt_color_option1.getText().toString();
        customerColorOption2 = edt_color_option2.getText().toString();
        customerFit = edt_fit.getText().toString();
        customerStyle = edt_style.getText().toString();
        customerCallBack = radioYes.isChecked() ? "YES" : "NO";
        customerArcDate = currentDateandTime;  //this will up to real time.
    }

    private void MainMethod() {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = sharedPreferences.getString("userId", "");
        store = sharedPreferences.getString("storeDescription", "");
        store_code = storeCode.substring(0, 4);

        storedescription.setText(storeCode);
        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLeveLDesc = sharedPreferences.getString("geoLeveLDesc", "");
        //  editor.putString("storeDescription",storeDescription);

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
        //  scrollView.setDescendantFocusability(ViewGroup.FOCUS_UP);
        getDetails();
        // prefocus = true;
        incorrect_remark.setVisibility(View.GONE);
        incorrect_phone.setVisibility(View.GONE);
        txt_incorrect_name.setVisibility(View.GONE);
        txt_incorrect_lastname.setVisibility(View.GONE);

        if ((customerNumber.equals("") || customerNumber == null) || (customerRemarks.equals("") || customerRemarks == null) || (customerLastname.equals("") || customerLastname == null) ||(customerName.equals("") || customerName == null)) {

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

            if(customerName.equals("") || customerName == null){
                txt_incorrect_name.setText(context.getResources().getString(R.string.customer_feedback_name));
                txt_incorrect_name.setVisibility(View.VISIBLE);
                edt_first_name.setBackgroundResource(R.drawable.edittext_red_border);
            }

            if(customerLastname.equals("") || customerLastname == null){
                txt_incorrect_lastname.setText(context.getResources().getString(R.string.customer_feedback_lastname));
                txt_incorrect_lastname.setVisibility(View.VISIBLE);
                edt_last_name.setBackgroundResource(R.drawable.edittext_red_border);
            }

            if(!customerNumber.equals("")) {

                if (customerNumber.length() < 10) {

                    incorrect_phone.setText(getResources().getString(R.string.customer_feedback_digit));
                    incorrect_phone.setVisibility(View.VISIBLE);
                    edt_customer_mobile_number.setBackgroundResource(R.drawable.edittext_red_border);

                }
            }


        }
        else if(customerNumber.length() < 10){

            incorrect_phone.setText(getResources().getString(R.string.customer_feedback_digit));
            incorrect_phone.setVisibility(View.VISIBLE);
            edt_customer_mobile_number.setBackgroundResource(R.drawable.edittext_red_border);

        }
        else {
            incorrect_remark.setVisibility(View.GONE);
            incorrect_phone.setVisibility(View.GONE);
            edt_customer_mobile_number.setBackgroundResource(R.drawable.edittext_border);
            edt_remarks.setBackgroundResource(R.drawable.edittext_border);
            if (Reusable_Functions.chkStatus(context)) {
                mpm_model model = new mpm_model();
                ApiCallBack(getObject(), 0);// id is zero.

            } else {
                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public JSONObject getObject() {

        // totoal is 14 contain and 3 extra like : feedback id,storecode,arcDate


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("feedbackKey", customerFeedback);
            jsonObject.put("storeCode", store_code);
            jsonObject.put("attribute1", customerNumber);
            jsonObject.put("attribute2", customerRemarks);
            jsonObject.put("attribute3", customerName);
            jsonObject.put("attribute4", customerLastname);
            jsonObject.put("attribute5", customerArticleId);
            jsonObject.put("attribute6", customerBrand);
            jsonObject.put("attribute7", customerProduct);
            jsonObject.put("attribute8", customerSize);
            jsonObject.put("attribute10", customerColorOption1);
            jsonObject.put("attribute11", customerColorOption2);
            jsonObject.put("attribute12", customerFit);
            jsonObject.put("attribute13", customerStyle);
            jsonObject.put("attribute14", customerCallBack);
           // jsonObject.put("arcDate", customerArcDate);
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
        String result = null;
        try {
            result = response.getString("status");
            Reusable_Functions.displayToast(context, result);
            req_sms_API(userId, customerNumber, bearertoken, customerCallBack, context, "productquality",store_code);

            cancelData();
            ((Activity) context).finish();
            nestedScrollview.fullScroll(View.FOCUS_DOWN);
//            Intent dashboard = new Intent(getActivity(), SnapDashboardActivity.class);
//            dashboard.putExtra("from","feedback");
//            getActivity().startActivity(dashboard);

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
        edt_article_id.getText().clear();
        edt_brand_name.getText().clear();
        edt_product_name.getText().clear();
        edt_size.getText().clear();
        edt_color_option1.getText().clear();
        edt_color_option2.getText().clear();
        edt_fit.getText().clear();
        edt_style.getText().clear();
        radioYes.setChecked(true);
        edt_customer_mobile_number.requestFocus();
    }



}
