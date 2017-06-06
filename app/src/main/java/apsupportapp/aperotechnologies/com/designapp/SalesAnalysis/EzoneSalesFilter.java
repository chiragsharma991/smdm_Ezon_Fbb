package apsupportapp.aperotechnologies.com.designapp.SalesAnalysis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by pamrutkar on 05/06/17.
 */
public class EzoneSalesFilter extends AppCompatActivity implements View.OnClickListener {

    private TextView txt_ez_location, txt_ez_prod;
    private RelativeLayout rel_ez_sfilter_back, rel_ez_sfilter_done;
    static LinearLayout lin_ez_locatn, lin_ez_prod;
    ArrayList<String> loc_listDataHeader, prod_listDataHeader;
    HashMap<String, List<String>> loc_listDataChild, prod_listDataChild;
    private EditText et_ez_search;
    public static ExpandableListView explv_ez_locatn, explv_ez_prod;
    Context context = this;
    String str_filter_location = "NO", str_filter_prod = "NO";
    private boolean loction_flag = false, prod_flag = false;
    static EzoneFilterAdapter locatn_list_adapter,prod_list_adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ezone_sfilter);
        getSupportActionBar().hide();
        initialise_ui();
        prepareData();
        locatn_list_adapter = new EzoneFilterAdapter(this, loc_listDataHeader, loc_listDataChild, explv_ez_locatn, locatn_list_adapter);
        explv_ez_locatn.setAdapter(locatn_list_adapter);
        prod_list_adapter = new EzoneFilterAdapter(this, prod_listDataHeader, prod_listDataChild, explv_ez_prod, prod_list_adapter);
        explv_ez_prod.setAdapter(prod_list_adapter);
        et_ez_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                Boolean handled = false;
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_NEXT) || (actionId == EditorInfo.IME_ACTION_NONE) || (actionId == EditorInfo.IME_ACTION_SEARCH)) {
                    et_ez_search.clearFocus();
                    InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputManager != null) {
                        inputManager.hideSoftInputFromWindow(et_ez_search.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    handled = true;
                }
                return handled;
            }
        });

        et_ez_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchData = et_ez_search.getText().toString();

                InputMethodManager inputManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(et_ez_search.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
              //  locatn_list_adapter.filterData(et_ez_search.getText().toString());
                prod_list_adapter.filterData(et_ez_search.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
    }

    private void prepareData() {
        loc_listDataHeader = new ArrayList<String>();
        prod_listDataHeader = new ArrayList<String>();
        loc_listDataChild = new HashMap<String, List<String>>();
        prod_listDataChild = new HashMap<String, List<String>>();

        loc_listDataHeader.add("Region");
        loc_listDataHeader.add("Store");

        List<String> top250 = new ArrayList<String>();
        top250.add("ARegion 1");
        top250.add("Region 2");
        top250.add("Region 3");
        top250.add("ARegion 4");


        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("Store 1");
        nowShowing.add("AStore 2");
        nowShowing.add("Store 3");
        nowShowing.add("Store 4");


        loc_listDataChild.put(loc_listDataHeader.get(0), top250); // Header, Child data
        loc_listDataChild.put(loc_listDataHeader.get(1), nowShowing);

        prod_listDataHeader.add("Department");
        prod_listDataHeader.add("Subdept");
        prod_listDataHeader.add("Class");
        prod_listDataHeader.add("Subclass");
        prod_listDataHeader.add("MC");

        List<String> dept = new ArrayList<String>();
        dept.add("ADept 1");
        dept.add("dept 2");
        dept.add("Dept 3");
        dept.add("Dept 4");


        List<String> cat = new ArrayList<String>();
        cat.add("ACategory 1");
        cat.add("Category 2");
        cat.add("Category 2");
        cat.add("Categorya 2");

        List<String> cat2 = new ArrayList<String>();
        cat2.add("aClass 1");
        cat2.add("Class 2");
        cat2.add("Class 2");
        cat2.add("Class 2");

        List<String> cat3 = new ArrayList<String>();
        cat3.add("SubClass 1");
        cat3.add("SubClass 2");
        cat3.add("aSubClass 2");
        cat3.add("SubClass 2");

        List<String> cat4 = new ArrayList<String>();
        cat4.add("MC 1");
        cat4.add("MC 2");
        cat4.add("aMC 2");
        cat4.add("MCa 2");

        prod_listDataChild.put(prod_listDataHeader.get(0), dept); // Header, Child data
        prod_listDataChild.put(prod_listDataHeader.get(1), cat);
        prod_listDataChild.put(prod_listDataHeader.get(2), cat2); // Header, Child data
        prod_listDataChild.put(prod_listDataHeader.get(3), cat3);
        prod_listDataChild.put(prod_listDataHeader.get(4), cat4); // Header, Child data
    }

    private void initialise_ui() {
        rel_ez_sfilter_back = (RelativeLayout) findViewById(R.id.rel_ez_sfilter_back);
        rel_ez_sfilter_done = (RelativeLayout) findViewById(R.id.rel_ez_sfilter_done);
        lin_ez_locatn = (LinearLayout) findViewById(R.id.lin_filter_locatn);
        lin_ez_prod = (LinearLayout) findViewById(R.id.lin_filter_product);
        txt_ez_location = (TextView) findViewById(R.id.txt_ez_location);
        txt_ez_prod = (TextView) findViewById(R.id.txt_ez_filter_product);
        et_ez_search = (EditText) findViewById(R.id.et_ez_search);
        et_ez_search.setSingleLine(true);
        explv_ez_locatn = (ExpandableListView) findViewById(R.id.explv_ez_location);
        explv_ez_locatn.setTextFilterEnabled(true);
        explv_ez_locatn.setDivider(getResources().getDrawable(R.color.grey));
        explv_ez_locatn.setDividerHeight(2);
        explv_ez_prod = (ExpandableListView) findViewById(R.id.explv_ez_product);
        explv_ez_prod.setTextFilterEnabled(true);
        explv_ez_prod.setDivider(getResources().getDrawable(R.color.grey));
        explv_ez_prod.setDividerHeight(2);
        txt_ez_location.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
        txt_ez_prod.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
        txt_ez_location.setOnClickListener(this);
        txt_ez_prod.setOnClickListener(this);
        rel_ez_sfilter_done.setOnClickListener(this);
        rel_ez_sfilter_back.setOnClickListener(this);
    }

    public static void StartIntent(Context c) {
        c.startActivity(new Intent(c, EzoneSalesFilter.class));
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (loction_flag)
        {
            lin_ez_locatn.setVisibility(View.VISIBLE);
        }
        else
        {
            lin_ez_locatn.setVisibility(View.GONE);
        }
        if (prod_flag)
        {
            lin_ez_prod.setVisibility(View.VISIBLE);
        }
        else
        {
            lin_ez_prod.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_ez_sfilter_back:
                onBackPressed();
                break;
            case R.id.rel_ez_sfilter_done:
                Toast.makeText(context, "Please select value...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.txt_ez_location:
                if (str_filter_location.equals("NO"))
                {
                    lin_ez_locatn.setVisibility(View.VISIBLE);
                    lin_ez_prod.setVisibility(View.GONE);
                    txt_ez_location.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.uplist, 0);
                    txt_ez_prod.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    str_filter_location = "YES";
                    str_filter_prod = "NO";
                    loction_flag = true;
                    prod_flag = false;
                }
                else
                {
                    lin_ez_locatn.setVisibility(View.GONE);
                    txt_ez_location.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    str_filter_location = "NO";
                    loction_flag = false;
                }
                break;
            case R.id.txt_ez_filter_product:
                if (str_filter_prod.equals("NO"))
                {
                    lin_ez_prod.setVisibility(View.VISIBLE);
                    lin_ez_locatn.setVisibility(View.GONE);
                    txt_ez_prod.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.uplist, 0);
                    txt_ez_location.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    str_filter_location = "NO";
                    str_filter_prod = "YES";
                    loction_flag = false;
                    prod_flag = true;
                }
                else
                {
                    lin_ez_prod.setVisibility(View.GONE);
                    txt_ez_prod.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    str_filter_prod = "NO";
                    prod_flag = false;
                }
                break;

        }
    }

    @Override
    public void onBackPressed()
    {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        finish();
    }
}
