package apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.SupervisorStaffHO;

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

public class SupervisorStaff_Feedback extends Fragment implements View.OnClickListener, HttpPostResponse {

    private Context context;
    private RelativeLayout imageBtnBack1;
    private EditText edt_customer_mobile_number, edt_remarks, edt_first_name, edt_last_name, edt_emp_name, edt_store_name;
    private RadioGroup radioCallbacks;
    private RadioButton radioYes, radioNo;
    private Button btn_submit, btn_cancel;
    private LinearLayout linear_toolbar;
    SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private TextInputLayout input_remarks;
    private ScrollView scrollView;
    private View v;
    private String remark, remarks_text, SelectedStoreCode, storeCode, store_code;
    private String TAG = "SupervisiorStaff";
    private TextView incorrect_phone, incorrect_remark, storedescription;
    private String userId, bearertoken, geoLeveLDesc, store;
    private String customerFeedback, customerNumber, customerRemarks, customerName, customerLastname,
            customerEmpname, customerStorename, customerCallBack, customerArcDate;

    public SupervisorStaff_Feedback(String storeCode) {

        this.storeCode = storeCode;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getContext();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        return inflater.inflate(R.layout.activity_supervisor_staff, container, false);
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
        edt_emp_name = (EditText) v.findViewById(R.id.edt_emp_name);
        edt_store_name = (EditText) v.findViewById(R.id.edt_store_name);
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


        MainMethod();


    }

    public void getDetails() {

        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        String currentDateandTime = time.format(new Date());

        customerFeedback = "5";  // fixed for notified feedback
        customerNumber = edt_customer_mobile_number.getText().toString();
        customerRemarks = edt_remarks.getText().toString();
        customerName = edt_first_name.getText().toString();
        customerLastname = edt_last_name.getText().toString();
        customerEmpname = edt_emp_name.getText().toString();
        customerStorename = edt_store_name.getText().toString();
        customerCallBack = radioYes.isChecked() ? "YES" : "NO";
        customerArcDate = currentDateandTime;  //this will up to real time.
    }

    private void MainMethod() {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = sharedPreferences.getString("userId", "");

        store = sharedPreferences.getString("storeDescription", "");
//        SelectedStoreCode = store.trim().substring(0, 4);
//
//         .e("store"," "+store);
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

    private void cancelData()
    {
        edt_customer_mobile_number.getText().clear();
        edt_remarks.getText().clear();
        edt_first_name.getText().clear();
        edt_last_name.getText().clear();
        edt_emp_name.getText().clear();
        edt_store_name.getText().clear();
        radioYes.setChecked(true);
        edt_customer_mobile_number.requestFocus();
    }

    private void submitData() {
        scrollView.setFocusableInTouchMode(true);
        scrollView.fullScroll(View.FOCUS_UP);
        getDetails();
        // prefocus = true;
        incorrect_remark.setVisibility(View.GONE);
        incorrect_phone.setVisibility(View.GONE);

        if ((customerNumber.equals("") || customerNumber == null) || (customerRemarks.equals("") || customerRemarks == null)) {

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
            jsonObject.put("attribute17", customerEmpname);
            jsonObject.put("attribute18", customerStorename);
            jsonObject.put("attribute14", customerCallBack);
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

        String result = null;
        try {
            result = response.getString("status");
            Reusable_Functions.displayToast(context, result);
            req_sms_API(userId, customerNumber, bearertoken, customerCallBack, context, "supervisorstaff",store_code);

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



}
