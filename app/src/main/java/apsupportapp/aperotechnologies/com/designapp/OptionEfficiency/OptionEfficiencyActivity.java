package apsupportapp.aperotechnologies.com.designapp.OptionEfficiency;

import android.app.MediaRouteButton;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.FreshnessIndex.FreshnessIndexActivity;
import apsupportapp.aperotechnologies.com.designapp.FreshnessIndex.FreshnessIndexAdapter;
import apsupportapp.aperotechnologies.com.designapp.FreshnessIndex.FreshnessIndexDetails;
import apsupportapp.aperotechnologies.com.designapp.FreshnessIndex.InventoryFilterActivity;
import apsupportapp.aperotechnologies.com.designapp.PvaSalesAnalysis.SalesPvAAdapter;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.model.OptionEfficiencyDetails;
import apsupportapp.aperotechnologies.com.designapp.model.OptionEfficiencyHeader;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisListDisplay;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisViewPagerValue;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by pamrutkar on 29/11/16.
 */
public class OptionEfficiencyActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private RadioButton oe_btnCore, oe_btnFashion,Skewed_checkWTD,Skewed_checkL4W,Skewed_checkSTD;
    public String OEfficiency_SegmentClick;
    ArrayList<OptionEfficiencyDetails> optionEfficiencyDetailsArrayList, optionArrayList, headerList;
    ArrayList<OptionEfficiencyHeader> oeHeaderList;
    TextView txtStoreCode, txtStoreDesc, oe_txtHeaderClass, oe_txtDeptName, txtNoChart;
    String userId, bearertoken;
    SharedPreferences sharedPreferences;
    int offsetvalue = 0, limit = 100;
    int count = 0;
    RequestQueue queue;
    //test git 6/1/17
    String OptionefficiencyValue, seasonGroup;
    Context context;
    String fromWhere, oe_FirstVisibleItem, oe_ClickedVal, oe_PlanDept, oe_Category, oe_PlanClass;
    PieChart oe_pieChart;
    ListView oe_listView;
    private String checkValueIs = null, checkTimeValueIs = null;
    int focusposition, oe_FirstPositionValue;
    LinearLayout llayoutOEfficiency, oe_llayouthierarchy;
    SegmentedGroup optionEfficiency_segmentedGrp;
    int level;
    OptionEfficiencyDetails optionEfficiencyDetails, optionEfficiencyDetail;
    OptionEfficiencyHeader optionEfficiencyHeader;
    OptionEfficiencyAdapter oe_Adapter;
    RelativeLayout optionEfficiency_imageBtnBack, optionEfficiency_imgfilter, oe_quickFilter, quickFilterPopup, quickFilter_baseLayout;
    RelativeLayout oe_btnPrev, oe_btnNext, qfDoneLayout, quickFilter_BorderLayout;
    Gson gson;
    PieData pieData;
    float fullSizeCount = 0.0f, fullCutCount = 0.0f, partCutCount = 0.0f;

    ArrayList<PieEntry> entries;
    private CheckBox checkCurrent, checkPrevious, checkOld, checkUpcoming;
    private String checkSeasonGpVal = null;
    boolean flag = false;
    private String qfButton = "OFF";
    private String TAG = "OptionEfficiencyActivity";
    private boolean CutCount = false, fullSize = false, fullCut = false;
   // private String view = "STD";
    private boolean coreSelection=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_efficiency);
        getSupportActionBar().hide();


        fromWhere = "Department";
        OEfficiency_SegmentClick = "Fashion";
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
            seasonGroup = "Current";
            oe_llayouthierarchy.setVisibility(View.GONE);
            requestHearderAPI();
            requestOptionEfficiencyDetails();

        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
            llayoutOEfficiency.setVisibility(View.GONE);

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
               // finish();
            }
        });

        //  quick Filter
        oe_quickFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterFunction();
            }
        });
        quickFilter_baseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkTimeValueIs == null ) {
                    checkCurrent.setChecked(true);
                    checkPrevious.setChecked(false);
                    checkOld.setChecked(false);
                    checkUpcoming.setChecked(false);
//
//                    Skewed_checkWTD.setChecked(false);
//                    Skewed_checkL4W.setChecked(false);
//                    Skewed_checkSTD.setChecked(true);


                } else {

//in this checkvalueIs  save the previous done condition params and call to true or false


                    switch (checkValueIs.toString()) {
                        case "BestCheckCurrent":
                            checkCurrent.setChecked(true);
                            checkPrevious.setChecked(false);
                            checkOld.setChecked(false);
                            checkUpcoming.setChecked(false);
                            Log.i(TAG, "BestCheckCurrent is checked");
                            break;
                        case "BestCheckPrevious":
                            checkCurrent.setChecked(false);
                            checkPrevious.setChecked(true);
                            checkOld.setChecked(false);
                            checkUpcoming.setChecked(false);
                            Log.i(TAG, "BestCheckPrevious is checked");
                            break;
                        case "BestCheckOld":
                            checkCurrent.setChecked(false);
                            checkPrevious.setChecked(false);
                            checkOld.setChecked(true);
                            checkUpcoming.setChecked(false);
                            Log.i(TAG, "BestCheckOld is checked");
                            break;
                        case "BestCheckUpcoming":
                            checkCurrent.setChecked(false);
                            checkPrevious.setChecked(false);
                            checkOld.setChecked(false);
                            checkUpcoming.setChecked(true);
                            Log.i(TAG, "BestCheckUpcoming is checked");
                            break;
                        default:
                            break;

                    }
//                    switch (checkTimeValueIs.toString()) {
//                        case "CheckWTD":
//                            Skewed_checkWTD.setChecked(true);
//                            Skewed_checkL4W.setChecked(false);
//                            Skewed_checkSTD.setChecked(false);
//                            Log.i(TAG, "CheckWTD is checked");
//                            break;
//                        case "CheckL4W":
//                            Skewed_checkWTD.setChecked(false);
//                            Skewed_checkL4W.setChecked(true);
//                            Skewed_checkSTD.setChecked(false);
//                            Log.i(TAG, "CheckL4W is checked");
//                            break;
//                        case "CheckSTD":
//                            Skewed_checkWTD.setChecked(false);
//                            Skewed_checkL4W.setChecked(false);
//                            Skewed_checkSTD.setChecked(true);
//                            Log.i(TAG, "CheckSTD is checked");
//                            break;
//                        default:
//                            break;
//
//
//                    }
                }


                quickFilterPopup.setVisibility(View.GONE);

            }
        });

        // previous
        oe_btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (oe_txtHeaderClass.getText().toString()) {

                    case "Brand Plan Class":
                        oe_btnNext.setVisibility(View.VISIBLE);
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
                            requestHearderAPI();
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
                            requestHearderAPI();
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
                            seasonGroup = "Current";
                            Log.e("Plan class prev", "");
                            requestHearderAPI();
                            requestOptionEfficiencyDetails();
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("---3---", " ");

                        break;

                    case "Category":
                        oe_btnPrev.setVisibility(View.INVISIBLE);
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
                            seasonGroup = "Current";
                            Log.e("Category prev", "");
                            requestHearderAPI();
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
                        oe_btnPrev.setVisibility(View.VISIBLE);
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
                            requestHearderAPI();
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
                            requestHearderAPI();
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
                            requestHearderAPI();
                            requestOptionEfficiencyDetails();

                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("---3--", " ");

                        break;

                    case "Brand":
                        oe_btnNext.setVisibility(View.INVISIBLE);
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
                            requestHearderAPI();
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
                                if(oe_FirstVisibleItem.equals("All"))
                                {
                                    oeHeaderList = new ArrayList<OptionEfficiencyHeader>();
                                    requestHeaderPieChart();
                                }
                                else {
                                    optionArrayList = new ArrayList<OptionEfficiencyDetails>();
                                    requestOEPieChart();
                                }
                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }

                        }
                        oe_FirstPositionValue = focusposition;

                    } else {
                        focusposition = optionEfficiencyDetailsArrayList.size() - 1;
                        oe_listView.setSelection(focusposition);
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
                if(position < optionEfficiencyDetailsArrayList.size()) {
                    switch (oe_txtHeaderClass.getText().toString()) {

                        case "Department":
                            oe_btnPrev.setVisibility(View.VISIBLE);

                            oe_txtHeaderClass.setText("Category");
                            oe_ClickedVal = optionEfficiencyDetailsArrayList.get(position).getPlanDept();
                            Log.e("txtClicked department--", "" + oe_ClickedVal);
                            oe_llayouthierarchy.setVisibility(View.GONE);
                            llayoutOEfficiency.setVisibility(View.GONE);
                            fromWhere = "Category";
                            level = 2;
                            seasonGroup = "Current";
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
                                seasonGroup = "Current";
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
                                seasonGroup = "Current";
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
                                oe_btnNext.setVisibility(View.INVISIBLE);
                                oe_txtHeaderClass.setText("Brand Plan Class");
                                oe_llayouthierarchy.setVisibility(View.GONE);
                                llayoutOEfficiency.setVisibility(View.GONE);
                                oe_ClickedVal = optionEfficiencyDetailsArrayList.get(position).getBrandName();
                                Log.e("txtSalesClickedValue3---", "" + oe_ClickedVal);
                                fromWhere = "Brand Plan Class";
                                seasonGroup = "Current";
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
            }

        });
    }



    private void initializeUI() {
        txtStoreCode = (TextView) findViewById(R.id.txtStoreCode);
        txtStoreDesc = (TextView) findViewById(R.id.txtStoreName);
        txtNoChart = (TextView) findViewById(R.id.noChartOption);
        oe_txtHeaderClass = (TextView) findViewById(R.id.oe_txtHeaderClass);
        oe_txtDeptName = (TextView) findViewById(R.id.oe_txtDeptName);
        optionEfficiency_imageBtnBack = (RelativeLayout) findViewById(R.id.optionEfficiency_imageBtnBack);
        optionEfficiency_imgfilter = (RelativeLayout) findViewById(R.id.optionEfficiency_imgfilter);
        oe_quickFilter = (RelativeLayout) findViewById(R.id.oe_quickFilter);
        quickFilterPopup = (RelativeLayout) findViewById(R.id.quickFilterPopup);
        quickFilter_baseLayout = (RelativeLayout) findViewById(R.id.quickFilter_baseLayout);
        qfDoneLayout = (RelativeLayout) findViewById(R.id.qfDoneLayout);
        //   quickFilterPopup.setVisibility(View.GONE);
        oe_pieChart = (PieChart) findViewById(R.id.oe_pieChart);
        oe_listView = (ListView) findViewById(R.id.oe_list);
        oe_listView.addFooterView(getLayoutInflater().inflate(R.layout.list_footer, null));
        llayoutOEfficiency = (LinearLayout) findViewById(R.id.llayoutOEfficiency);
        oe_llayouthierarchy = (LinearLayout) findViewById(R.id.oe_llayouthierarchy);
        oe_btnPrev = (RelativeLayout) findViewById(R.id.oe_btnPrev);
        oe_btnPrev.setVisibility(View.INVISIBLE);
        oe_btnNext = (RelativeLayout) findViewById(R.id.oe_btnNext);

        optionEfficiency_segmentedGrp = (SegmentedGroup) findViewById(R.id.optionEfficiency_segmentedGrp);
        optionEfficiency_segmentedGrp.setOnCheckedChangeListener(this);
        oe_btnCore = (RadioButton) findViewById(R.id.oe_btnCore);
        oe_btnFashion = (RadioButton) findViewById(R.id.oe_btnFashion);
        oe_btnFashion.toggle();
        quickFilter_BorderLayout = (RelativeLayout) findViewById(R.id.quickFilter_BorderLayout);
        optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
        optionArrayList = new ArrayList<OptionEfficiencyDetails>();
        headerList = new ArrayList<OptionEfficiencyDetails>();
        oeHeaderList = new ArrayList<OptionEfficiencyHeader>();
        checkCurrent = (CheckBox) findViewById(R.id.checkCurrent);
        checkPrevious = (CheckBox) findViewById(R.id.checkPrevious);
        checkOld = (CheckBox) findViewById(R.id.checkOld);
        checkUpcoming = (CheckBox) findViewById(R.id.checkUpcoming);
//
//        Skewed_checkWTD = (RadioButton) findViewById(R.id.skewed_checkWTD);
//        Skewed_checkL4W = (RadioButton) findViewById(R.id.skewed_checkL4W);
//        Skewed_checkSTD = (RadioButton) findViewById(R.id.skewed_checkSTD);

        checkCurrent.setOnClickListener(this);
        checkPrevious.setOnClickListener(this);
        checkOld.setOnClickListener(this);
        checkUpcoming.setOnClickListener(this);
//
//        Skewed_checkWTD.setOnClickListener(this);
//        Skewed_checkL4W.setOnClickListener(this);
//        Skewed_checkSTD.setOnClickListener(this);

        qfDoneLayout.setOnClickListener(this);
        quickFilter_BorderLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qfDoneLayout:

                if (Reusable_Functions.chkStatus(context)) {

//
//                    if (Skewed_checkWTD.isChecked()) {
//                        checkTimeValueIs = "CheckWTD";
//                        view = "WTD";
//
//
//                    } else if (Skewed_checkL4W.isChecked()) {
//                        checkTimeValueIs = "CheckL4W";
//                        view = "L4W";
//
//
//                    } else if (Skewed_checkSTD.isChecked()) {
//                        checkTimeValueIs = "CheckSTD";
//                        view = "STD";
//
//
//                    }


                    //season group

                    if (checkCurrent.isChecked()) {
                        checkValueIs = "BestCheckCurrent";
                        popupCurrent();

                        quickFilterPopup.setVisibility(View.GONE);

                    } else if (checkPrevious.isChecked()) {
                        checkValueIs = "BestCheckPrevious";
                        popupPrevious();

                        quickFilterPopup.setVisibility(View.GONE);

                    } else if (checkOld.isChecked()) {
                        checkValueIs = "BestCheckOld";
                        popupOld();

                        quickFilterPopup.setVisibility(View.GONE);

                    } else if (checkUpcoming.isChecked()) {
                        checkValueIs = "BestCheckUpcoming";
                        popupUpcoming();

                        quickFilterPopup.setVisibility(View.GONE);

                    } else {

                        quickFilterPopup.setVisibility(View.GONE);

                    }
                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();

                }

                break;


            case R.id.checkCurrent:
                checkCurrent.setChecked(true);
                checkPrevious.setChecked(false);
                checkOld.setChecked(false);
                checkUpcoming.setChecked(false);
                break;
            case R.id.checkPrevious:
                checkPrevious.setChecked(true);
                checkCurrent.setChecked(false);
                checkOld.setChecked(false);
                checkUpcoming.setChecked(false);
                break;
            case R.id.checkOld:
                checkOld.setChecked(true);
                checkCurrent.setChecked(false);
                checkPrevious.setChecked(false);
                checkUpcoming.setChecked(false);
                break;
            case R.id.checkUpcoming:
                checkUpcoming.setChecked(true);
                checkCurrent.setChecked(false);
                checkOld.setChecked(false);
                checkPrevious.setChecked(false);
                break;


//            case R.id.skewed_checkWTD:
//                Skewed_checkWTD.setChecked(true);
//                Skewed_checkL4W.setChecked(false);
//                Skewed_checkSTD.setChecked(false);
//                break;
//            case R.id.skewed_checkL4W:
//                Skewed_checkL4W.setChecked(true);
//                Skewed_checkSTD.setChecked(false);
//                Skewed_checkWTD.setChecked(false);
//                break;
//            case R.id.skewed_checkSTD:
//                Skewed_checkSTD.setChecked(true);
//                Skewed_checkL4W.setChecked(false);
//                Skewed_checkWTD.setChecked(false);
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
           // level = 1;
            seasonGroup = "Current";
            requestHearderAPI();
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
           // level = 1;
            seasonGroup = "Previous";
            requestHearderAPI();
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
           // level = 1;
            seasonGroup = "Old";
            requestHearderAPI();
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
          //  level = 1;
            seasonGroup = "Upcoming";
            requestHearderAPI();
            requestOptionEfficiencyDetails();
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {

            case R.id.oe_btnCore:
//
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
                   // coreSelection = true;

                    requestHearderAPI();
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
                  //  coreSelection = false;

                    requestHearderAPI();
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


    private void requestHearderAPI() {

        String url = "";
//        if (coreSelection)
//        {
//            //core selection without season params
//            url = ConstsCore.web_url + "/v1/display/optionefficiencyheader/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level +"&view=" + view;
//        } else
//        {
            // fashion select with season params
            url = ConstsCore.web_url + "/v1/display/optionefficiencyheader/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level +"&seasongroup=" + seasonGroup;
       // }
        Log.e(TAG, "Url" + url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, "Header Response" + response);
                        Log.e("Length-----", "" + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                // Reusable_Functions.hDialog();

                                return;
                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    optionEfficiencyHeader = gson.fromJson(response.get(i).toString(), OptionEfficiencyHeader.class);
                                    oeHeaderList.add(optionEfficiencyHeader);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;

                                requestHearderAPI();

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    optionEfficiencyHeader = gson.fromJson(response.get(i).toString(), OptionEfficiencyHeader.class);
                                    oeHeaderList.add(optionEfficiencyHeader);                                }
                            }
                            // Log.e(TAG, "headerList : " + headerList.get(0).getFullCutCount());


                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            //  Toast.makeText(context, "catch no data found in header" , Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  Reusable_Functions.hDialog();
                        //  Toast.makeText(context, "Server not found...", Toast.LENGTH_SHORT).show();

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

    // API 1.33
    private void requestOptionEfficiencyDetails() {


        String fIdetails;
//        if (coreSelection) {
//
//            //core selection without season params
//            fIdetails = ConstsCore.web_url + "/v1/display/optionefficiencydetail/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit+ "&view=" + view;
//
//
//        } else {

            // fashion select with season params
             fIdetails = ConstsCore.web_url + "/v1/display/optionefficiencydetail/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&seasongroup=" + seasonGroup + "&offset=" + offsetvalue + "&limit=" + limit;

      //  }

        Log.e(TAG, "requestOptionEfficiencyDetails" + fIdetails);


        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, fIdetails,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, "Option Details Class: " + response);
                        Log.i("response length", "" + response.length());
                        int i;
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                llayoutOEfficiency.setVisibility(View.GONE);
                                return;
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
                                optionEfficiencyDetails = new OptionEfficiencyDetails();

                                if (oe_txtHeaderClass.getText().toString().equals("Department")) {
                                    optionEfficiencyDetails.setPlanDept("All");

                                    Log.e(TAG,"Full Size Count"+optionEfficiencyHeader.getSohCountFullSize());

                                } else if (oe_txtHeaderClass.getText().toString().equals("Category")) {
                                    optionEfficiencyDetails.setPlanCategory("All");


                                } else if (oe_txtHeaderClass.getText().toString().equals("Plan Class")) {
                                    optionEfficiencyDetails.setPlanClass("All");

                                } else if (oe_txtHeaderClass.getText().toString().equals("Brand")) {
                                    optionEfficiencyDetails.setBrandName("All");

                                } else if (oe_txtHeaderClass.getText().toString().equals("Brand Plan Class")) {
                                    optionEfficiencyDetails.setBrandplanClass("All");

                                }

                                optionEfficiencyDetails.setOptionCount(optionEfficiencyHeader.getOptionCount());
                                optionEfficiencyDetails.setFullSizeCount(optionEfficiencyHeader.getFullSizeCount());
                                optionEfficiencyDetails.setStkOnhandQty(optionEfficiencyHeader.getStkOnhandQty());
                                optionEfficiencyDetails.setSohCountFullSize(optionEfficiencyHeader.getSohCountFullSize());
                                optionEfficiencyDetailsArrayList.add(0, optionEfficiencyDetails);


                                oe_Adapter = new OptionEfficiencyAdapter(optionEfficiencyDetailsArrayList, context, fromWhere, oe_listView);
                                oe_listView.setAdapter(oe_Adapter);
                                oe_Adapter.notifyDataSetChanged();
                                txtStoreCode.setText(optionEfficiencyDetailsArrayList.get(i).getStoreCode());
                                txtStoreDesc.setText(optionEfficiencyDetailsArrayList.get(i).getStoreDescription());

//                                llayoutOEfficiency.setVisibility(View.VISIBLE);
//                                Reusable_Functions.hDialog();
                                oeHeaderList = new ArrayList<OptionEfficiencyHeader>();
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                requestHeaderPieChart();
                            }


                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "catch:no data found in Details ", Toast.LENGTH_SHORT).show();
                            llayoutOEfficiency.setVisibility(View.GONE);

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "Server not found...", Toast.LENGTH_SHORT).show();
                        llayoutOEfficiency.setVisibility(View.GONE);

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

    private void requestHeaderPieChart() {
        String url = ConstsCore.web_url + "/v1/display/optionefficiencyheader/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit+"&seasongroup="+seasonGroup;
        Log.e(TAG,"Url" + url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG,"Header Pie Chart Response"+ response);
                        Log.e(TAG,"Length Pie Chart-----"+ response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no chart data found", Toast.LENGTH_SHORT).show();
                                llayoutOEfficiency.setVisibility(View.VISIBLE);


                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    optionEfficiencyHeader = gson.fromJson(response.get(i).toString(), OptionEfficiencyHeader.class);
                                    oeHeaderList.add(optionEfficiencyHeader);

                                }
                                offsetvalue = (limit * count) + limit;
                                count++;

                                requestHeaderPieChart();

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    optionEfficiencyHeader = gson.fromJson(response.get(i).toString(), OptionEfficiencyHeader.class);
                                    oeHeaderList.add(optionEfficiencyHeader);
                                }
                                entries = new ArrayList<PieEntry>();
                                for (OptionEfficiencyHeader optionEfficiency : oeHeaderList) {

                                    fullSizeCount = (float) optionEfficiency.getFullSizeCount();
                                    partCutCount = (float) optionEfficiency.getPartCutCount();
                                    fullCutCount = (float) optionEfficiency.getFullCutCount();

                                    Log.e(TAG, "Values-------" + fullSizeCount + "\t" + partCutCount + "\t" + fullCutCount);

                                }
                                ArrayList<Integer> colors = new ArrayList<>();
                                colors.add(Color.parseColor("#31d6c5"));
                                colors.add(Color.parseColor("#aea9fd"));
                                colors.add(Color.parseColor("#fe8081"));

                                ArrayList<String> labels = new ArrayList<>();
                                if (fullSizeCount > 0.0f) {

                                    entries.add(new PieEntry(fullSizeCount, "Full Size"));
                                } else {
                                    fullSize = true;
                                }
                                if (partCutCount > 0.0f) {

                                    entries.add(new PieEntry(partCutCount, "Part Cut"));
                                } else {
                                    CutCount = true;

                                }
                                if (fullCutCount > 0.0f) {

                                    entries.add(new PieEntry(fullCutCount, "Full Cut"));
                                } else {
                                    fullCut = true;

                                }
                                if (fullSize && CutCount && fullCut) {
                                    txtNoChart.setVisibility(View.VISIBLE);
                                    fullSize = false;
                                    CutCount = false;
                                    fullCut = false;

                                }

                                PieDataSet   dataset = new PieDataSet(entries, "");
                                dataset.setColors(colors);
                                dataset.setValueLineWidth(0.7f);
                                dataset.setValueTextColor(Color.BLACK);
                                pieData = new PieData(dataset);
                                pieData.setValueFormatter(new MyValueFormatter());
                                dataset.setValueLinePart1Length(1.2f);
                                dataset.setValueLinePart2Length(1.8f);
                                oe_pieChart.setDrawMarkers(false);
                                pieData.setValueTextSize(10f);
                                dataset.setXValuePosition(null);
                                dataset.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                                oe_pieChart.setEntryLabelColor(Color.BLACK);
                                oe_pieChart.setExtraOffsets(5, 10, 5, 5);
                                oe_pieChart.setHoleRadius(0);
                                oe_pieChart.setHoleColor(Color.WHITE);
                                oe_pieChart.setTransparentCircleRadius(0);
                                oe_pieChart.setData(pieData);
                                oe_pieChart.notifyDataSetChanged();
                                oe_pieChart.invalidate();
                                oe_pieChart.animateXY(4000, 4000);
                                oe_pieChart.setDescription(null);
                                oe_pieChart.setTouchEnabled(false);
                                Legend l = oe_pieChart.getLegend();
                                l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
                                l.setEnabled(true);
                                l.setFormSize(11f);
                                llayoutOEfficiency.setVisibility(View.VISIBLE);
                                Reusable_Functions.hDialog();


                            }


                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "catch no data found in Graphheader" , Toast.LENGTH_SHORT).show();
                            llayoutOEfficiency.setVisibility(View.VISIBLE);

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "Server not found...", Toast.LENGTH_SHORT).show();
                        llayoutOEfficiency.setVisibility(View.VISIBLE);

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

        oe_category_listurl = ConstsCore.web_url + "/v1/display/optionefficiencydetail/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&dept=" + deptName.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit +"&seasongroup="+seasonGroup;
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

        String oe_planclass_listurl = ConstsCore.web_url + "/v1/display/optionefficiencydetail/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&dept=" + oe_PlanDept.replaceAll(" ", "%20").replaceAll("&", "%26") + "&category=" + category.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit +"&seasongroup="+seasonGroup;
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

        String oe_brand_listurl = ConstsCore.web_url + "/v1/display/optionefficiencydetail/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&dept=" + oe_PlanDept.replaceAll(" ", "%20").replaceAll("&", "%26") + "&category=" + oe_Category.replaceAll(" ", "%20").replaceAll("&", "%26") + "&class=" + planclass.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit +"&seasongroup="+seasonGroup;
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

        String oe_brandplan_listurl = ConstsCore.web_url + "/v1/display/optionefficiencydetail/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&dept=" + oe_PlanDept.replaceAll(" ", "%20").replaceAll("&", "%26") + "&category=" + oe_Category.replaceAll(" ", "%20").replaceAll("&", "%26") + "&class=" + oe_PlanClass.replaceAll(" ", "%20").replaceAll("&", "%26") + "&brand=" + brandnm.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit +"&seasongroup="+seasonGroup;
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
        txtNoChart.setVisibility(View.GONE);


        if (oe_txtHeaderClass.getText().toString().equals("Department")) {
            url = ConstsCore.web_url + "/v1/display/optionefficiencydetail/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&dept=" + oe_FirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit +"&seasongroup="+seasonGroup;
        } else if (oe_txtHeaderClass.getText().toString().equals("Category")) {
            url = ConstsCore.web_url + "/v1/display/optionefficiencydetail/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&category=" + oe_FirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit +"&seasongroup="+seasonGroup;
        } else if (oe_txtHeaderClass.getText().toString().equals("Plan Class")) {
            url = ConstsCore.web_url + "/v1/display/optionefficiencydetail/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&class=" + oe_FirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit +"&seasongroup="+seasonGroup;
        } else if (oe_txtHeaderClass.getText().toString().equals("Brand")) {
            url = ConstsCore.web_url + "/v1/display/optionefficiencydetail/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&brand=" + oe_FirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit+"&seasongroup="+seasonGroup;
        } else if (oe_txtHeaderClass.getText().toString().equals("Brand Plan Class")) {
            url = ConstsCore.web_url + "/v1/display/optionefficiencydetail/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&brandclass=" + oe_FirstVisibleItem.replace(" ", "%20") + "&offset=" + offsetvalue + "&limit=" + limit+"&seasongroup="+seasonGroup;
        }
        Log.e(TAG, "Url" + url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, "OE Pie Chart on Scroll  : " + response);
                        Log.e("OE Pie Chart response", "" + response.length());
                        try {
                            int i;
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found in DetailPieChart", Toast.LENGTH_SHORT).show();

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
                                    if (oe_FirstVisibleItem.equals("All")) {
                                        fullSizeCount = (float) optionEfficiency.getFullSizeCount();
                                        partCutCount = (float) optionEfficiency.getPartCutCount();
                                        fullCutCount = (float) optionEfficiency.getFullCutCount();

                                        Log.e(TAG, "Values-------" + fullSizeCount + "\t" + partCutCount + "\t" + fullCutCount);

                                    } else if (oe_FirstVisibleItem.equals(optionEfficiency.getPlanDept())) {
                                        fullSizeCount = (float) optionEfficiency.getFullSizeCount();
                                        partCutCount = (float) optionEfficiency.getPartCutCount();
                                        fullCutCount = (float) optionEfficiency.getFullCutCount();

                                        Log.e(TAG,"Values-------" + fullSizeCount + "\t" + partCutCount + "\t" + fullCutCount);

                                    } else if (oe_FirstVisibleItem.equals(optionEfficiency.getPlanCategory())) {
                                        fullSizeCount = (float) optionEfficiency.getFullSizeCount();
                                        partCutCount = (float) optionEfficiency.getPartCutCount();
                                        fullCutCount = (float) optionEfficiency.getFullCutCount();

                                        Log.e(TAG,"Values-------"+ fullSizeCount + "\t" + partCutCount + "\t" + fullCutCount);

                                    } else if (oe_FirstVisibleItem.equals(optionEfficiency.getPlanClass())) {
                                        fullSizeCount = (float) optionEfficiency.getFullSizeCount();
                                        partCutCount = (float) optionEfficiency.getPartCutCount();
                                        fullCutCount = (float) optionEfficiency.getFullCutCount();

                                        Log.e(TAG,"Values-------"+ fullSizeCount + "\t" + partCutCount + "\t" + fullCutCount);

                                    } else if (oe_FirstVisibleItem.equals(optionEfficiency.getBrandName())) {
                                        fullSizeCount = (float) optionEfficiency.getFullSizeCount();
                                        partCutCount = (float) optionEfficiency.getPartCutCount();
                                        fullCutCount = (float) optionEfficiency.getFullCutCount();

                                        Log.e(TAG,"Values-------" + fullSizeCount + "\t" + partCutCount + "\t" + fullCutCount);

                                    } else if (oe_FirstVisibleItem.equals(optionEfficiency.getBrandplanClass())) {
                                        fullSizeCount = (float) optionEfficiency.getFullSizeCount();
                                        partCutCount = (float) optionEfficiency.getPartCutCount();
                                        fullCutCount = (float) optionEfficiency.getFullCutCount();

                                        Log.e(TAG,"Values-------"+ fullSizeCount + "\t" + partCutCount + "\t" + fullCutCount);

                                    }

                                }
                                ArrayList<Integer> colors = new ArrayList<>();
                                colors.add(Color.parseColor("#31d6c5"));
                                colors.add(Color.parseColor("#aea9fd"));
                                colors.add(Color.parseColor("#fe8081"));

                                ArrayList<String> labels = new ArrayList<>();
                                if (fullSizeCount > 0.0f) {

                                    entries.add(new PieEntry(fullSizeCount, "Full Size"));
                                } else {
                                    fullSize = true;
                                }
                                if (partCutCount > 0.0f) {

                                    entries.add(new PieEntry(partCutCount, "Part Cut"));
                                } else {
                                    CutCount = true;

                                }
                                if (fullCutCount > 0.0f) {

                                    entries.add(new PieEntry(fullCutCount, "Full Cut"));
                                } else {
                                    fullCut = true;

                                }
                                if (fullSize && CutCount && fullCut) {
                                    txtNoChart.setVisibility(View.VISIBLE);
                                    fullSize = false;
                                    CutCount = false;
                                    fullCut = false;

                                }

                                PieDataSet   dataset = new PieDataSet(entries, "");
                                dataset.setColors(colors);
                                dataset.setValueLineWidth(0.7f);
                                dataset.setValueTextColor(Color.BLACK);
                                pieData = new PieData(dataset);
                                pieData.setValueFormatter(new MyValueFormatter());
                                dataset.setValueLinePart1Length(1.2f);
                                dataset.setValueLinePart2Length(1.8f);
                                oe_pieChart.setDrawMarkers(false);
                                pieData.setValueTextSize(10f);
                                dataset.setXValuePosition(null);
                                dataset.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                                oe_pieChart.setEntryLabelColor(Color.BLACK);
                                oe_pieChart.setExtraOffsets(5, 10, 5, 5);
                                oe_pieChart.setHoleRadius(0);
                                oe_pieChart.setHoleColor(Color.WHITE);
                                oe_pieChart.setTransparentCircleRadius(0);
                                oe_pieChart.setData(pieData);
                                oe_pieChart.notifyDataSetChanged();
                                oe_pieChart.invalidate();
                                oe_pieChart.animateXY(4000, 4000);
                                oe_pieChart.setDescription(null);
                                oe_pieChart.setTouchEnabled(false);
                                Legend l = oe_pieChart.getLegend();
                                l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
                                l.setEnabled(true);
                                l.setFormSize(11f);
                                llayoutOEfficiency.setVisibility(View.VISIBLE);
                                Reusable_Functions.hDialog();
                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "catch no data found in GraphDetail" , Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "Server not found...", Toast.LENGTH_SHORT).show();
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
