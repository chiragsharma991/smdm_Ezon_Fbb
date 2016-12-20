package apsupportapp.aperotechnologies.com.designapp.OptionEfficiency;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.DashBoardActivity;
import apsupportapp.aperotechnologies.com.designapp.FreshnessIndex.FreshnessIndexActivity;
import apsupportapp.aperotechnologies.com.designapp.FreshnessIndex.FreshnessIndexAdapter;
import apsupportapp.aperotechnologies.com.designapp.FreshnessIndex.InventoryFilterActivity;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.model.FreshnessIndexDetails;
import apsupportapp.aperotechnologies.com.designapp.model.OptionEfficiencyDetails;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by pamrutkar on 29/11/16.
 */
public class OptionEfficiencyActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    RadioButton oe_btnCore, oe_btnFashion;
    public String OEfficiency_SegmentClick;
    ArrayList<OptionEfficiencyDetails> optionEfficiencyDetailsArrayList, optionArrayList;
    TextView txtStoreCode, txtStoreDesc, oe_txtHeaderClass, oe_txtDeptName;
    String userId, bearertoken;
    SharedPreferences sharedPreferences;
    int offsetvalue = 0, limit = 100;
    int count = 0;
    RequestQueue queue;
    String OptionefficiencyValue, seasonGroup;
    Context context;
    String fromWhere, oe_FirstVisibleItem, oe_ClickedVal, oe_PlanDept, oe_Category, oe_PlanClass;
    PieChart oe_pieChart;
    ListView oe_listView;
    int focusposition, oe_FirstPositionValue;
    LinearLayout llayoutOEfficiency, oe_llayouthierarchy;
    SegmentedGroup optionEfficiency_segmentedGrp;
    int level;
    OptionEfficiencyDetails optionEfficiencyDetails;
    OptionEfficiencyAdapter oe_Adapter;
    RelativeLayout optionEfficiency_imageBtnBack, optionEfficiency_imgfilter, oe_quickFilter, quickFilterPopup, quickFilter_baseLayout;
    RelativeLayout oe_btnPrev, oe_btnNext, qfDoneLayout,quickFilter_BorderLayout;
    Gson gson;
    PieData pieData;
    float fullSizeCount = 0.0f, fullCutCount = 0.0f, partCutCount = 0.0f;
    PieDataSet dataset;
    ArrayList<PieEntry> entries;
   // RadioButton checkCurrent, checkPrevious, checkOld, checkUpcoming;
    private String checkSeasonGpVal = null;
    boolean flag = false;
    private String qfButton="OFF";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_efficiency);
        getSupportActionBar().hide();
        fromWhere = "Department";
        OEfficiency_SegmentClick = "Core";
        oe_FirstVisibleItem = "";
        oe_ClickedVal = "";
        context = this;
        level = 1;
        OptionefficiencyValue = "";
        seasonGroup = "Current";
        focusposition = 0;
        oe_FirstPositionValue = 0;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();
        initializeUI();

        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            offsetvalue = 0;
            limit = 100;
            count = 0;
            level = 1;
            seasonGroup = "All";
            oe_llayouthierarchy.setVisibility(View.GONE);
            requestOptionEfficiencyDetails();

        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }

        //onClick of Back Button
        optionEfficiency_imageBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        optionEfficiency_imgfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionEfficiencyActivity.this, InventoryFilterActivity.class);
                intent.putExtra("checkfrom", "optionEfficiency");
                startActivity(intent);
                finish();
            }
        });

        //quick Filter
//        oe_quickFilter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                filterFunction();
//            }
//        });
//        quickFilter_baseLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {

//                if (checkSeasonGpVal == null ) {
//                    checkCurrent.setChecked(false);
//                    checkPrevious.setChecked(false);
//                    checkOld.setChecked(false);
//                    checkUpcoming.setChecked(false);
//
//
//                } else {
//                    switch (checkSeasonGpVal.toString()) {
//                        case "Current":
//                            checkCurrent.setChecked(true);
//                            checkPrevious.setChecked(false);
//                            checkOld.setChecked(false);
//                            checkUpcoming.setChecked(false);
//
//                            Log.e("Current checked", "" + checkCurrent.isChecked());
//                            break;
//
//                        case "Previous":
//                            checkPrevious.setChecked(true);
//                            checkCurrent.setChecked(false);
//                            checkOld.setChecked(false);
//                            checkUpcoming.setChecked(false);
//                            Log.e("Previous checked", "" + checkPrevious.isChecked());
//                            break;
//                        case "Old":
//                            checkOld.setChecked(true);
//                            checkCurrent.setChecked(false);
//                            checkPrevious.setChecked(false);
//                            checkUpcoming.setChecked(false);
//                            Log.e("Old checked", "" + checkOld.isChecked());
//                            break;
//                        case "Upcoming":
//                            checkUpcoming.setChecked(true);
//                            checkCurrent.setChecked(false);
//                            checkOld.setChecked(false);
//                            checkPrevious.setChecked(false);
//                            Log.e("Upcoming checked", "" + checkUpcoming.isChecked());
//                            break;
//                    }
//
//                }
//                quickFilterPopup.setVisibility(View.GONE);
//                if(qfButton.equals("OFF")) {
//                    checkCurrent.setChecked(true);
//                    checkUpcoming.setChecked(false);
//                    checkOld.setChecked(false);
//                    checkPrevious.setChecked(false);
//                }
//                quickFilterPopup.setVisibility(View.GONE);
//
//            }
//        });

        // previous
        oe_btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (oe_txtHeaderClass.getText().toString()) {

                    case "Brand Plan Class":
                        oe_txtHeaderClass.setText("Brand");
                        fromWhere = "Brand";
                        level = 4;
                        flag = false;
                        optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
                        oe_llayouthierarchy.setVisibility(View.GONE);
                        llayoutOEfficiency.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.e("Brand Plan Class Prev-- ", "  ");
                            requestOptionEfficiencyDetails();

                            Log.e("prev 1", "" + optionEfficiencyDetails.getBrandName());

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case "Brand":
                        oe_txtHeaderClass.setText("Plan Class");
                        fromWhere = "Plan Class";
                        level = 3;
                        flag = false;
                        optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
                        oe_llayouthierarchy.setVisibility(View.GONE);
                        llayoutOEfficiency.setVisibility(View.GONE);

                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.e("Brand name prev", "--");
                            requestOptionEfficiencyDetails();
                            Log.e("prev 2", "" + optionEfficiencyDetails.getPlanClass());

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("---2---", " ");

                        break;


                    case "Plan Class":
                        oe_txtHeaderClass.setText("Category");
                        fromWhere = "Category";
                        level = 2;
                        flag = false;
                        optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
                        oe_llayouthierarchy.setVisibility(View.GONE);
                        llayoutOEfficiency.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            seasonGroup = "All";
                            Log.e("Plan class prev", "");
                            requestOptionEfficiencyDetails();
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("---3---", " ");

                        break;

                    case "Category":
                        oe_txtHeaderClass.setText("Department");
                        fromWhere = "Department";
                        level = 1;
                        flag = false;
                        optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
                        oe_llayouthierarchy.setVisibility(View.GONE);
                        llayoutOEfficiency.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            seasonGroup = "All";
                            Log.e("Category prev", "");
                            requestOptionEfficiencyDetails();
                            Log.e("prev 4", "" + optionEfficiencyDetails.getPlanDept());

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("---4---", " ");

                        break;
                    default:
                }
            }

        });

        // next-----
        oe_btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (oe_txtHeaderClass.getText().toString()) {

                    case "Department":

                        oe_txtHeaderClass.setText("Category");
                        fromWhere = "Category";
                        level = 2;
                        flag = false;
                        optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
                        oe_llayouthierarchy.setVisibility(View.GONE);
                        llayoutOEfficiency.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.i("dept next", "-----");
                            requestOptionEfficiencyDetails();
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }


                        break;

                    case "Category":


                        fromWhere = "Plan Class";
                        oe_txtHeaderClass.setText("Plan Class");
                        level = 3;
                        flag = false;
                        optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
                        oe_llayouthierarchy.setVisibility(View.GONE);
                        llayoutOEfficiency.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            Log.e("category next --", "");
                            requestOptionEfficiencyDetails();
                            Log.e("next 2", "" + optionEfficiencyDetails.getPlanClass());

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }


                        break;
                    case "Plan Class":
                        oe_txtHeaderClass.setText("Brand");
                        fromWhere = "Brand";
                        level = 4;
                        flag = false;
                        optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();

                        oe_llayouthierarchy.setVisibility(View.GONE);
                        llayoutOEfficiency.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {

                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            requestOptionEfficiencyDetails();

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("---3--", " ");

                        break;

                    case "Brand":
                        oe_txtHeaderClass.setText("Brand Plan Class");

                        fromWhere = "Brand Plan Class";
                        level = 5;
                        flag = false;
                        optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
                        oe_llayouthierarchy.setVisibility(View.GONE);
                        llayoutOEfficiency.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            requestOptionEfficiencyDetails();
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("---4---", " ");

                        break;
                    default:
                }
            }
        });

        oe_listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                if (scrollState == SCROLL_STATE_IDLE) {

                if (optionEfficiencyDetailsArrayList.size() != 0) {
                    //listView_SalesAnalysis.smoothScrollToPosition(firstVisibleItem);
                    if (view.getFirstVisiblePosition() <= optionEfficiencyDetailsArrayList.size() - 1) {
                        focusposition = view.getFirstVisiblePosition();
                        oe_listView.setSelection(view.getFirstVisiblePosition());

                        if (oe_txtHeaderClass.getText().toString().equals("Department")) {
                            level = 1;
                            oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(oe_listView.getFirstVisiblePosition()).getPlanDept().toString();
                        } else if (oe_txtHeaderClass.getText().toString().equals("Category")) {
                            level = 2;
                            oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(oe_listView.getFirstVisiblePosition()).getPlanCategory().toString();
                        } else if (oe_txtHeaderClass.getText().toString().equals("Plan Class")) {
                            level = 3;
                            oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(oe_listView.getFirstVisiblePosition()).getPlanClass().toString();
                        } else if (oe_txtHeaderClass.getText().toString().equals("Brand")) {
                            level = 4;
                            oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(oe_listView.getFirstVisiblePosition()).getBrandName().toString();
                        } else if (oe_txtHeaderClass.getText().toString().equals("Brand Plan Class")) {
                            level = 5;
                            oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(oe_listView.getFirstVisiblePosition()).getBrandplanClass().toString();
                        }
                        if (focusposition != oe_FirstPositionValue) {
                            if (Reusable_Functions.chkStatus(context)) {
                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                requestOEPieChart();
                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }

                        }
                        oe_FirstPositionValue = focusposition;

                    } else {
                        focusposition = optionEfficiencyDetailsArrayList.size() - 1;
                        oe_listView.setSelection(optionEfficiencyDetailsArrayList.size() - 1);

                        oe_FirstPositionValue = focusposition;
                    }
                }

//                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        // hierarchy level drill down on selected item click
        oe_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (oe_txtHeaderClass.getText().toString()) {

                    case "Department":
                        oe_txtHeaderClass.setText("Category");
                        oe_ClickedVal = optionEfficiencyDetailsArrayList.get(position).getPlanDept();
                        Log.e("txtClicked department--", "" + oe_ClickedVal);
                        oe_llayouthierarchy.setVisibility(View.GONE);
                        llayoutOEfficiency.setVisibility(View.GONE);
                        fromWhere = "Category";
                        level = 2;
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
                            Log.e("Size Before", "----" + optionEfficiencyDetailsArrayList.size());
                            Log.i("dept next", "-----");
                            request_OE_CategoryList(oe_ClickedVal);
//                           Log.e("Size After", "----" + optionEfficiencyDetailsArrayList.size());
                            oe_PlanDept = oe_ClickedVal;

                        } else {

                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case "Category":
                        Log.e("in oe category", "-----" + oe_PlanDept);
                        if (flag == true) {
                            oe_txtHeaderClass.setText("Plan Class");
                            oe_llayouthierarchy.setVisibility(View.GONE);
                            llayoutOEfficiency.setVisibility(View.GONE);
                            oe_ClickedVal = optionEfficiencyDetailsArrayList.get(position).getPlanCategory();
                            Log.e("txtClicked category --", "" + oe_ClickedVal);
                            fromWhere = "Plan Class";
                            level = 3;
                            if (Reusable_Functions.chkStatus(context)) {
                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
                                Log.i("category next", "-----");
                                Log.i("come", "----" + oe_PlanDept);
                                request_OE_PlanClassList(oe_PlanDept, oe_ClickedVal);
                                oe_Category = oe_ClickedVal;
                                Log.e("fIndexCategory--", "" + oe_Category);
                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Please select Dept name", Toast.LENGTH_SHORT);
                        }

                        break;
                    case "Plan Class":
                        Log.e("in oe plan class", "-----" + oe_PlanDept);
                        if (flag == true) {
                            oe_txtHeaderClass.setText("Brand");
                            oe_llayouthierarchy.setVisibility(View.GONE);
                            llayoutOEfficiency.setVisibility(View.GONE);
                            oe_ClickedVal = optionEfficiencyDetailsArrayList.get(position).getPlanClass();
                            Log.e("txtClicked plan class---", "" + oe_ClickedVal);
                            fromWhere = "Brand";
                            level = 4;
                            if (Reusable_Functions.chkStatus(context)) {
                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
                                Log.i("Plan Class next", "-----");

                                request_OE_BrandList(oe_PlanDept, oe_Category, oe_ClickedVal);

                                oe_PlanClass = oe_ClickedVal;
                                Log.e("fIndexPlanClass---", "" + oe_PlanClass);
                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Please select Dept name", Toast.LENGTH_SHORT);
                        }

                        break;

                    case "Brand":
                        Log.e("in oe brand", "-----" + oe_PlanDept);
                        if (flag == true) {
                            oe_txtHeaderClass.setText("Brand Plan Class");
                            oe_llayouthierarchy.setVisibility(View.GONE);
                            llayoutOEfficiency.setVisibility(View.GONE);
                            oe_ClickedVal = optionEfficiencyDetailsArrayList.get(position).getBrandName();
                            Log.e("txtSalesClickedValue3---", "" + oe_ClickedVal);
                            fromWhere = "Brand Plan Class";
                            level = 5;
                            if (Reusable_Functions.chkStatus(context)) {
                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
                                Log.i("brand next", "-----");

                                request_OE_BrandPlanList(oe_PlanDept, oe_Category, oe_PlanClass, oe_ClickedVal);

                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Please select Dept name", Toast.LENGTH_SHORT);
                        }
                        break;
                }

            }

        });
    }


    private void initializeUI() {
        txtStoreCode = (TextView) findViewById(R.id.txtStoreCode);
        txtStoreDesc = (TextView) findViewById(R.id.txtStoreName);
        oe_txtHeaderClass = (TextView) findViewById(R.id.oe_txtHeaderClass);
        oe_txtDeptName = (TextView) findViewById(R.id.oe_txtDeptName);
        optionEfficiency_imageBtnBack = (RelativeLayout) findViewById(R.id.optionEfficiency_imageBtnBack);
        optionEfficiency_imgfilter = (RelativeLayout) findViewById(R.id.optionEfficiency_imgfilter);
        oe_quickFilter = (RelativeLayout) findViewById(R.id.oe_quickFilter);
        quickFilterPopup= (RelativeLayout)findViewById(R.id.quickFilterPopup);
        quickFilter_baseLayout = (RelativeLayout)findViewById(R.id.quickFilter_baseLayout);
        qfDoneLayout =(RelativeLayout)findViewById(R.id.qfDoneLayout);
     //   quickFilterPopup.setVisibility(View.GONE);
        oe_pieChart = (PieChart) findViewById(R.id.oe_pieChart);
        oe_listView = (ListView) findViewById(R.id.oe_list);
        oe_listView.addFooterView(getLayoutInflater().inflate(R.layout.list_footer, null));
        llayoutOEfficiency = (LinearLayout) findViewById(R.id.llayoutOEfficiency);
        oe_llayouthierarchy = (LinearLayout) findViewById(R.id.oe_llayouthierarchy);
        oe_btnPrev = (RelativeLayout) findViewById(R.id.oe_btnPrev);
        oe_btnNext = (RelativeLayout) findViewById(R.id.oe_btnNext);

        optionEfficiency_segmentedGrp = (SegmentedGroup) findViewById(R.id.optionEfficiency_segmentedGrp);
        optionEfficiency_segmentedGrp.setOnCheckedChangeListener(OptionEfficiencyActivity.this);
        oe_btnCore = (RadioButton) findViewById(R.id.oe_btnCore);
        oe_btnFashion = (RadioButton) findViewById(R.id.oe_btnFashion);
        oe_btnFashion.toggle();
//        quickFilter_BorderLayout=(RelativeLayout)findViewById(R.id.quickFilter_BorderLayout);
        optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
        optionArrayList = new ArrayList<OptionEfficiencyDetails>();
//        checkCurrent = (RadioButton) findViewById(R.id.checkCurrent);
//        checkPrevious = (RadioButton) findViewById(R.id.checkPrevious);
//        checkOld = (RadioButton) findViewById(R.id.checkOld);
//        checkUpcoming = (RadioButton) findViewById(R.id.checkUpcoming);
//        checkCurrent.setOnClickListener(OptionEfficiencyActivity.this);
//        checkPrevious.setOnClickListener(this);
//        checkOld.setOnClickListener(this);
//        checkUpcoming.setOnClickListener(this);
//        qfDoneLayout.setOnClickListener(this);
//        quickFilter_BorderLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
      //      case R.id.qfDoneLayout:

//                if (checkCurrent.isChecked()) {
//                    popupCurrent();
//                    checkSeasonGpVal = "Current";
//
//                    quickFilterPopup.setVisibility(View.GONE);
//
//                } else if (checkPrevious.isChecked()) {
//                    popupPrevious();
//                    checkSeasonGpVal = "Previous";
//
//                    quickFilterPopup.setVisibility(View.GONE);
//
//                } else if (checkOld.isChecked()) {
//                    popupOld();
//                    checkSeasonGpVal = "Old";
//
//                    quickFilterPopup.setVisibility(View.GONE);
//
//                } else if (checkUpcoming.isChecked()) {
//                    popupUpcoming();
//                    checkSeasonGpVal = "Upcoming";
//
//                    quickFilterPopup.setVisibility(View.GONE);
//                } else {
//                    Toast.makeText(this, "Uncheck", Toast.LENGTH_SHORT).show();
//
//                }
//                qfButton="ON";
//                if (checkCurrent.isChecked()) {
//                    popupCurrent();
//                    checkPrevious.setChecked(false);
//                    checkOld.setChecked(false);
//                    checkUpcoming.setChecked(false);
//                    quickFilterPopup.setVisibility(View.GONE);
//
//                } else if (checkPrevious.isChecked()) {
//                    popupPrevious();
//                    checkCurrent.setChecked(false);
//                    checkOld.setChecked(false);
//                    checkUpcoming.setChecked(false);
//                    quickFilterPopup.setVisibility(View.GONE);
//
//                } else if (checkOld.isChecked()) {
//                    popupOld();
//                    checkPrevious.setChecked(false);
//                    checkCurrent.setChecked(false);
//                    checkUpcoming.setChecked(false);
//                    quickFilterPopup.setVisibility(View.GONE);
//
//                } else if (checkUpcoming.isChecked()) {
//                    popupUpcoming();
//                    checkCurrent.setChecked(false);
//                    checkPrevious.setChecked(false);
//                    checkOld.setChecked(false);
//                    quickFilterPopup.setVisibility(View.GONE);
//
//                } else {
//                    Toast.makeText(this, "Uncheck", Toast.LENGTH_SHORT).show();
//
//                }
//                break;
//            case R.id.checkCurrent:
//                checkCurrent.setChecked(true);
//                checkPrevious.setChecked(false);
//                checkOld.setChecked(false);
//                checkUpcoming.setChecked(false);
//                break;
//            case R.id.checkPrevious:
//                checkPrevious.setChecked(true);
//                checkCurrent.setChecked(false);
//                checkOld.setChecked(false);
//                checkUpcoming.setChecked(false);
//                break;
//            case R.id.checkOld:
//                checkOld.setChecked(true);
//                checkCurrent.setChecked(false);
//                checkPrevious.setChecked(false);
//                checkUpcoming.setChecked(false);
//                break;
//            case R.id.checkUpcoming:
//                checkUpcoming.setChecked(true);
//                checkCurrent.setChecked(false);
//                checkOld.setChecked(false);
//                checkPrevious.setChecked(false);
//                break;

        }
    }

    private void filterFunction() {
        quickFilterPopup.setVisibility(View.VISIBLE);
    }

    private void popupCurrent() {
        oe_llayouthierarchy.setVisibility(View.GONE);
        llayoutOEfficiency.setVisibility(View.GONE);
        optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            offsetvalue = 0;
            limit = 100;
            count = 0;
            level = 1;
            seasonGroup = "Current";
            requestOptionEfficiencyDetails();

        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private void popupPrevious() {
        oe_llayouthierarchy.setVisibility(View.GONE);
        llayoutOEfficiency.setVisibility(View.GONE);
        optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            offsetvalue = 0;
            limit = 100;
            count = 0;
            level = 1;
            seasonGroup = "Previous";
            requestOptionEfficiencyDetails();

        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }

    }

    private void popupOld() {
        oe_llayouthierarchy.setVisibility(View.GONE);
        llayoutOEfficiency.setVisibility(View.GONE);
        optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            offsetvalue = 0;
            limit = 100;
            count = 0;
            level = 1;
            seasonGroup = "Old";
            requestOptionEfficiencyDetails();

        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private void popupUpcoming() {
        oe_llayouthierarchy.setVisibility(View.GONE);
        llayoutOEfficiency.setVisibility(View.GONE);
        optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            offsetvalue = 0;
            limit = 100;
            count = 0;
            level = 1;
            seasonGroup = "Upcoming";
            requestOptionEfficiencyDetails();

        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {

            case R.id.oe_btnCore:

                if (OEfficiency_SegmentClick.equals("Core"))
                    break;
                OEfficiency_SegmentClick = "Core";
                oe_llayouthierarchy.setVisibility(View.GONE);
                llayoutOEfficiency.setVisibility(View.GONE);
                optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading data...");
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    requestOptionEfficiencyDetails();

                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
                Log.e("-----core-----", " ");
                break;

            case R.id.oe_btnFashion:

                if (OEfficiency_SegmentClick.equals("Fashion"))
                    break;

                OEfficiency_SegmentClick = "Fashion";
                oe_llayouthierarchy.setVisibility(View.GONE);
                llayoutOEfficiency.setVisibility(View.GONE);
                optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading data...");
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    requestOptionEfficiencyDetails();

                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
                Log.e("-----fashion-----", " ");
                break;

            default:
                break;

        }
    }

    //----------------------------API Declaration---------------------------//
    // API 1.33
    private void requestOptionEfficiencyDetails() {

        String fIdetails = ConstsCore.web_url + "/v1/display/optionefficiencydetail/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&seasongroup=" + seasonGroup + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + fIdetails);


        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, fIdetails,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Option Details Class: ", " " + response);
                        Log.i("response length", "" + response.length());
                        int i;
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                            } else if (response.length() == limit) {
                                for (i = 0; i < response.length(); i++) {

                                    optionEfficiencyDetails = gson.fromJson(response.get(i).toString(), OptionEfficiencyDetails.class);
                                    optionEfficiencyDetailsArrayList.add(optionEfficiencyDetails);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestOptionEfficiencyDetails();

                            } else if (response.length() < limit) {
                                for (i = 0; i < response.length(); i++) {

                                    optionEfficiencyDetails = gson.fromJson(response.get(i).toString(), OptionEfficiencyDetails.class);
                                    optionEfficiencyDetailsArrayList.add(optionEfficiencyDetails);
                                }
//                                freshnessIndexDetails = new FreshnessIndexDetails();
//
//                                if (txtFIndexClass.getText().toString().equals("Department")) {
//                                    freshnessIndexDetails.setPlanDept("All");
//                                } else if (txtFIndexClass.getText().toString().equals("Category")) {
//                                    freshnessIndexDetails.setPlanCategory("All");
//                                } else if (txtFIndexClass.getText().toString().equals("Plan Class")) {
//                                    freshnessIndexDetails.setPlanClass("All");
//                                } else if (txtFIndexClass.getText().toString().equals("Brand")) {
//                                    freshnessIndexDetails.setBrandName("All");
//                                } else if (txtFIndexClass.getText().toString().equals("Brand Plan Class")) {
//                                    freshnessIndexDetails.setBrandplanClass("All");
//                                }
//
//                                freshnessIndexDetailsArrayList.add(0, freshnessIndexDetails);
//

                                oe_Adapter = new OptionEfficiencyAdapter(optionEfficiencyDetailsArrayList, context, fromWhere, oe_listView);
                                oe_listView.setAdapter(oe_Adapter);
                                oe_Adapter.notifyDataSetChanged();
                                txtStoreCode.setText(optionEfficiencyDetailsArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(optionEfficiencyDetailsArrayList.get(0).getStoreDescription());

                                if (oe_txtHeaderClass.getText().toString().equals("Department")) {
                                    level = 1;
                                    oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(oe_listView.getFirstVisiblePosition()).getPlanDept().toString();
                                } else if (oe_txtHeaderClass.getText().toString().equals("Category")) {
                                    level = 2;
                                    oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(oe_listView.getFirstVisiblePosition()).getPlanCategory().toString();
                                } else if (oe_txtHeaderClass.getText().toString().equals("Plan Class")) {
                                    level = 3;
                                    oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(oe_listView.getFirstVisiblePosition()).getPlanClass().toString();
                                } else if (oe_txtHeaderClass.getText().toString().equals("Brand")) {
                                    level = 4;
                                    oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(oe_listView.getFirstVisiblePosition()).getBrandName().toString();
                                } else if (oe_txtHeaderClass.getText().toString().equals("Brand Plan Class")) {
                                    level = 5;
                                    oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(oe_listView.getFirstVisiblePosition()).getBrandplanClass().toString();
                                }
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                requestOEPieChart();

                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + bearertoken);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    // For Category List on click of Dept Value
    private void request_OE_CategoryList(final String deptName) {

        String oe_category_listurl = " ";

        oe_category_listurl = ConstsCore.web_url + "/v1/display/optionefficiencydetail/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&dept=" + deptName.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + oe_category_listurl);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, oe_category_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Option Category List: ", " " + response);
                        Log.i("Option Category List response length", "" + response.length());
                        int i;
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no category data found", Toast.LENGTH_SHORT).show();
                            } else if (response.length() == limit) {
                                for (i = 0; i < response.length(); i++) {

                                    optionEfficiencyDetails = gson.fromJson(response.get(i).toString(), OptionEfficiencyDetails.class);
                                    optionEfficiencyDetailsArrayList.add(optionEfficiencyDetails);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                request_OE_CategoryList(deptName);

                            } else if (response.length() < limit) {
                                for (i = 0; i < response.length(); i++) {

                                    optionEfficiencyDetails = gson.fromJson(response.get(i).toString(), OptionEfficiencyDetails.class);
                                    optionEfficiencyDetailsArrayList.add(optionEfficiencyDetails);
                                }

                                oe_Adapter = new OptionEfficiencyAdapter(optionEfficiencyDetailsArrayList, context, fromWhere, oe_listView);
                                oe_listView.setAdapter(oe_Adapter);
                                flag = true;
                                txtStoreCode.setText(optionEfficiencyDetailsArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(optionEfficiencyDetailsArrayList.get(0).getStoreDescription());
                                OptionefficiencyValue = " ";
                                OptionefficiencyValue = deptName;
                                oe_txtDeptName.setText(OptionefficiencyValue);
                                oe_llayouthierarchy.setVisibility(View.VISIBLE);

                                oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(oe_listView.getFirstVisiblePosition()).getPlanCategory().toString();
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                level = 2;
                                requestOEPieChart();

                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no category data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no category data found", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + bearertoken);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    // For Plan Class on click of Category Val
    private void request_OE_PlanClassList(final String deptName, final String category) {

        String oe_planclass_listurl = ConstsCore.web_url + "/v1/display/optionefficiencydetail/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&dept=" + oe_PlanDept.replaceAll(" ", "%20").replaceAll("&", "%26") + "&category=" + category.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + oe_planclass_listurl);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, oe_planclass_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(" Option Plan Class List : ", " " + response);
                        Log.i("Option Plan Class List response length", "" + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no plan class data found", Toast.LENGTH_SHORT).show();
                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    optionEfficiencyDetails = gson.fromJson(response.get(i).toString(), OptionEfficiencyDetails.class);
                                    optionEfficiencyDetailsArrayList.add(optionEfficiencyDetails);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                request_OE_PlanClassList(deptName, category);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    optionEfficiencyDetails = gson.fromJson(response.get(i).toString(), OptionEfficiencyDetails.class);
                                    optionEfficiencyDetailsArrayList.add(optionEfficiencyDetails);
                                }

                                oe_Adapter = new OptionEfficiencyAdapter(optionEfficiencyDetailsArrayList, context, fromWhere, oe_listView);
                                oe_listView.setAdapter(oe_Adapter);
                                flag = true;
                                txtStoreCode.setText(optionEfficiencyDetailsArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(optionEfficiencyDetailsArrayList.get(0).getStoreDescription());
                                OptionefficiencyValue += " > " + category;
                                oe_txtDeptName.setText(OptionefficiencyValue);
                                oe_llayouthierarchy.setVisibility(View.VISIBLE);
                                oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(oe_listView.getFirstVisiblePosition()).getPlanClass().toString();
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                level = 3;
                                requestOEPieChart();

                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no plan class data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no  plan class data found", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + bearertoken);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    // For Brand on click of Plan Class Val
    private void request_OE_BrandList(String deptName, String category, final String planclass) {

        String oe_brand_listurl = ConstsCore.web_url + "/v1/display/optionefficiencydetail/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&dept=" + oe_PlanDept.replaceAll(" ", "%20").replaceAll("&", "%26") + "&category=" + oe_Category.replaceAll(" ", "%20").replaceAll("&", "%26") + "&class=" + planclass.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + oe_brand_listurl);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, oe_brand_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Option brand List : ", " " + response);
                        Log.i("Option brand List response length", "" + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no brand name data found", Toast.LENGTH_SHORT).show();
                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    optionEfficiencyDetails = gson.fromJson(response.get(i).toString(), OptionEfficiencyDetails.class);
                                    optionEfficiencyDetailsArrayList.add(optionEfficiencyDetails);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                request_OE_BrandList(oe_PlanDept, oe_Category, planclass);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    optionEfficiencyDetails = gson.fromJson(response.get(i).toString(), OptionEfficiencyDetails.class);
                                    optionEfficiencyDetailsArrayList.add(optionEfficiencyDetails);
                                }

                                oe_Adapter = new OptionEfficiencyAdapter(optionEfficiencyDetailsArrayList, context, fromWhere, oe_listView);
                                oe_listView.setAdapter(oe_Adapter);
                                flag = true;
                                //fIndexAdapter.notifyDataSetChanged();
                                txtStoreCode.setText(optionEfficiencyDetailsArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(optionEfficiencyDetailsArrayList.get(0).getStoreDescription());

                                OptionefficiencyValue += " > " + planclass;
                                oe_txtDeptName.setText(OptionefficiencyValue);
                                oe_llayouthierarchy.setVisibility(View.VISIBLE);
//
                                oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(oe_listView.getFirstVisiblePosition()).getBrandName().toString();
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                level = 4;
                                requestOEPieChart();

                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no brand name data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no brand name data found", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + bearertoken);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    // For BrandPlanCLass on click of Brand Val
    private void request_OE_BrandPlanList(String deptName, String category, String plan_class, final String brandnm) {

        String oe_brandplan_listurl = ConstsCore.web_url + "/v1/display/optionefficiencydetail/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&dept=" + oe_PlanDept.replaceAll(" ", "%20").replaceAll("&", "%26") + "&category=" + oe_Category.replaceAll(" ", "%20").replaceAll("&", "%26") + "&class=" + oe_PlanClass.replaceAll(" ", "%20").replaceAll("&", "%26") + "&brand=" + brandnm.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("url", " " + oe_brandplan_listurl);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, oe_brandplan_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Option Brand Plan Class List : ", " " + response);
                        Log.i("Option Plan Class List response length", "" + response.length());

                        try {

                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no brand plan class data found", Toast.LENGTH_SHORT).show();

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    optionEfficiencyDetails = gson.fromJson(response.get(i).toString(), OptionEfficiencyDetails.class);
                                    optionEfficiencyDetailsArrayList.add(optionEfficiencyDetails);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                request_OE_BrandPlanList(oe_PlanDept, oe_Category, oe_PlanClass, brandnm);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    optionEfficiencyDetails = gson.fromJson(response.get(i).toString(), OptionEfficiencyDetails.class);
                                    optionEfficiencyDetailsArrayList.add(optionEfficiencyDetails);
                                }

                                oe_Adapter = new OptionEfficiencyAdapter(optionEfficiencyDetailsArrayList, context, fromWhere, oe_listView);
                                oe_listView.setAdapter(oe_Adapter);
                                flag = true;
                                txtStoreCode.setText(optionEfficiencyDetailsArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(optionEfficiencyDetailsArrayList.get(0).getStoreDescription());
                                OptionefficiencyValue += " > " + brandnm;
                                oe_txtDeptName.setText(OptionefficiencyValue);
                                oe_llayouthierarchy.setVisibility(View.VISIBLE);
                                oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(oe_listView.getFirstVisiblePosition()).getBrandplanClass().toString();
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                level = 5;
                                requestOEPieChart();

                            }


                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no brand plan class data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no brand plan class data found", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + bearertoken);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void requestOEPieChart() {
        Log.e("Department onsroll api", "" + oe_FirstVisibleItem);
        Log.e("Header Class", oe_txtHeaderClass.getText().toString());
        String url = "";

        if (oe_txtHeaderClass.getText().toString().equals("Department")) {
            url = ConstsCore.web_url + "/v1/display/optionefficiencydetail/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&dept=" + oe_FirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        } else if (oe_txtHeaderClass.getText().toString().equals("Category")) {
            url = ConstsCore.web_url + "/v1/display/optionefficiencydetail/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&category=" + oe_FirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        } else if (oe_txtHeaderClass.getText().toString().equals("Plan Class")) {
            url = ConstsCore.web_url + "/v1/display/optionefficiencydetail/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&class=" + oe_FirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        } else if (oe_txtHeaderClass.getText().toString().equals("Brand")) {
            url = ConstsCore.web_url + "/v1/display/optionefficiencydetail/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&brand=" + oe_FirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        } else if (oe_txtHeaderClass.getText().toString().equals("Brand Plan Class")) {
            url = ConstsCore.web_url + "/v1/display/optionefficiencydetail/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&brandclass=" + oe_FirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit;
        }
        Log.e("Url", "" + url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("OE Pie Chart on Scroll  : ", " " + response);
                        Log.e("OE Pie Chart response", "" + response.length());
                        try {
                            int i;
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();

                            } else if (response.length() == limit) {
                                for (i = 0; i < response.length(); i++) {

                                    optionEfficiencyDetails = gson.fromJson(response.get(i).toString(), OptionEfficiencyDetails.class);
                                    optionArrayList.add(optionEfficiencyDetails);
                                }
                            } else if (response.length() < limit) {
                                for (i = 0; i < response.length(); i++) {

                                    optionEfficiencyDetails = gson.fromJson(response.get(i).toString(), OptionEfficiencyDetails.class);
                                    optionArrayList.add(optionEfficiencyDetails);
                                }
//                                oe_Adapter = new OptionEfficiencyAdapter(optionEfficiencyDetailsArrayList, context, fromWhere, oe_listView);
//                                oe_listView.setAdapter(oe_Adapter);
                                //oe_Adapter.notifyDataSetChanged();

                                entries = new ArrayList<PieEntry>();
                                for (OptionEfficiencyDetails optionEfficiency : optionArrayList) {
                                    if (oe_FirstVisibleItem.equals(optionEfficiency.getPlanDept())) {
                                        fullSizeCount = (float) optionEfficiency.getFullSizeCount();
                                        partCutCount = (float) optionEfficiency.getPartCutCount();
                                        fullCutCount = (float) optionEfficiency.getFullCutCount();

                                        Log.e("Values-------", "" + fullSizeCount + "\t" + partCutCount + "\t" + fullCutCount);

                                    } else if (oe_FirstVisibleItem.equals(optionEfficiency.getPlanCategory())) {
                                        fullSizeCount = (float) optionEfficiency.getFullSizeCount();
                                        partCutCount = (float) optionEfficiency.getPartCutCount();
                                        fullCutCount = (float) optionEfficiency.getFullCutCount();

                                        Log.e("Values-------", "" + fullSizeCount + "\t" + partCutCount + "\t" + fullCutCount);

                                    } else if (oe_FirstVisibleItem.equals(optionEfficiency.getPlanClass())) {
                                        fullSizeCount = (float) optionEfficiency.getFullSizeCount();
                                        partCutCount = (float) optionEfficiency.getPartCutCount();
                                        fullCutCount = (float) optionEfficiency.getFullCutCount();

                                        Log.e("Values-------", "" + fullSizeCount + "\t" + partCutCount + "\t" + fullCutCount);

                                    } else if (oe_FirstVisibleItem.equals(optionEfficiency.getBrandName())) {
                                        fullSizeCount = (float) optionEfficiency.getFullSizeCount();
                                        partCutCount = (float) optionEfficiency.getPartCutCount();
                                        fullCutCount = (float) optionEfficiency.getFullCutCount();

                                        Log.e("Values-------", "" + fullSizeCount + "\t" + partCutCount + "\t" + fullCutCount);

                                    } else if (oe_FirstVisibleItem.equals(optionEfficiency.getBrandplanClass())) {
                                        fullSizeCount = (float) optionEfficiency.getFullSizeCount();
                                        partCutCount = (float) optionEfficiency.getPartCutCount();
                                        fullCutCount = (float) optionEfficiency.getFullCutCount();

                                        Log.e("Values-------", "" + fullSizeCount + "\t" + partCutCount + "\t" + fullCutCount);

                                    }

                                }
                                ArrayList<Integer> colors = new ArrayList<>();
                                colors.add(Color.parseColor("#8857a6"));
                                colors.add(Color.parseColor("#b33d2f"));
                                colors.add(Color.parseColor("#386e34"));

                                ArrayList<String> labels = new ArrayList<>();
                                if (fullSizeCount > 0.0f) {

                                    entries.add(new PieEntry(fullSizeCount, "Full Size"));
                                }
                                if (partCutCount > 0.0f) {

                                    entries.add(new PieEntry(partCutCount, "Part Cut"));
                                }
                                if (fullCutCount > 0.0f) {

                                    entries.add(new PieEntry(fullCutCount, "Full Cut"));
                                }

                                dataset = new PieDataSet(entries, "");
                                dataset.setColors(colors);
                                dataset.setValueLineWidth(0.5f);
                                dataset.setValueTextColor(Color.BLACK);
                                pieData = new PieData(dataset);
                                pieData.setValueFormatter(new MyValueFormatter());
                                dataset.setValueLinePart1Length(1.5f);
                                dataset.setValueLinePart2Length(1.8f);
                                oe_pieChart.setDrawMarkers(false);
                                pieData.setValueTextSize(8.5f);
                                dataset.setXValuePosition(null);
                                dataset.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                                oe_pieChart.setEntryLabelColor(Color.BLACK);
                                oe_pieChart.setExtraOffsets(5, 10, 5, 5);
                                oe_pieChart.setHoleRadius(0);
                                oe_pieChart.setHoleColor(Color.WHITE);
                                oe_pieChart.setTransparentCircleRadius(0);
                                oe_pieChart.setData(pieData);
                                oe_pieChart.animateXY(4000, 4000);
                                oe_pieChart.setDescription(null);
                                oe_pieChart.setTouchEnabled(false);
                                Legend l = oe_pieChart.getLegend();
                                l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
                                l.setEnabled(true);
                                llayoutOEfficiency.setVisibility(View.VISIBLE);
                                Reusable_Functions.hDialog();
                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }


        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + bearertoken);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }


    public class MyValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,###,##0.0"); // use two decimal if needed
        }


        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//           return mFormat.format(value) + ""; // e.g. append a dollar-sign

            if (value < 0.0) return "";
            else return mFormat.format(value) + " %";
        }
    }

    @Override
    public void onBackPressed() {
   /*     Intent intent = new Intent(OptionEfficiencyActivity.this, DashBoardActivity.class);
        intent.putExtra("BACKTO","inventory");
        startActivity(intent);*/
        finish();
    }


}
