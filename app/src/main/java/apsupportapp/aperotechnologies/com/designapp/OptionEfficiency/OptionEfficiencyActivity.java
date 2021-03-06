package apsupportapp.aperotechnologies.com.designapp.OptionEfficiency;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.google.gson.Gson;
import org.json.JSONArray;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.RecyclerItemClickListener;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.RunningPromo.RecyclerViewPositionHelper;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesAnalysisFilter;
import apsupportapp.aperotechnologies.com.designapp.model.OptionEfficiencyDetails;
import apsupportapp.aperotechnologies.com.designapp.model.OptionEfficiencyHeader;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisListDisplay;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by pamrutkar on 29/11/16.
 */
public class OptionEfficiencyActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, TabLayout.OnTabSelectedListener {

    private static String OEfficiency_SegmentClick = "Fashion";
    ArrayList<OptionEfficiencyDetails> optionEfficiencyDetailsArrayList, optionArrayList, headerList;
    ArrayList<OptionEfficiencyHeader> oeHeaderList;
    TextView txtStoreCode, txtStoreDesc, oe_txtHeaderClass, oe_txtDeptName, txtNoChart;
    String userId, bearertoken, storeDescription,geoLevel2Code,lobId;
    SharedPreferences sharedPreferences;
    int offsetvalue = 0, limit = 100;
    int count = 0;
    RequestQueue queue;
    String OptionefficiencyValue;
    private static String seasonGroup = null;
    Context context;
    String fromWhere, oe_FirstVisibleItem, oe_ClickedVal, oe_PlanDept, oe_Category, oe_PlanClass;
    PieChart oe_pieChart;
    RecyclerView oe_listView;
    private static String checkValueIs = null;
    int focusposition, oe_FirstPositionValue;
    LinearLayout llayoutOEfficiency, oe_llayouthierarchy;
    private static int level;
    int prevState = RecyclerView.SCROLL_STATE_IDLE;
    int currentState = RecyclerView.SCROLL_STATE_IDLE;
    OptionEfficiencyDetails optionEfficiencyDetails, optionEfficiencyDetail;
    OptionEfficiencyHeader optionEfficiencyHeader;
    OptionIndexSnapAdapter optionIndexSnapAdapter;
    RelativeLayout optionEfficiency_imageBtnBack, optionEfficiency_imgfilter, quickFilterPopup, quickFilter_baseLayout;
    RelativeLayout oe_btnPrev, oe_btnNext, qfDoneLayout, quickFilter_BorderLayout,optione_btnReset;
    Gson gson;
    RelativeLayout oe_quickFilter;
    PieData pieData;
    float fullSizeCount = 0.0f, fullCutCount = 0.0f, partCutCount = 0.0f;
    ArrayList<PieEntry> entries;
    private CheckBox checkCurrent, checkPrevious, checkOld, checkUpcoming;
    boolean flag = false;
    private String qfButton = "OFF", isMultiStore, value;
    private boolean CutCount = false, fullSize = false, fullCut = false;
    private boolean coreSelection = false, filter_toggleClick = false;
    private ProgressBar processBar;
    private int totalItemCount = 0;
    private int OveridePositionValue = 0;
    private JsonArrayRequest postRequest;
    private boolean OnItemClick = false;
    public static Activity option_Efficiency;
    private TabLayout Tabview;
    private int filter_level;
    private String header_value;
    private String[] hierarchyList;
    private String TAG="OptionEfficiencyActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_efficiency);
        getSupportActionBar().hide();
        fromWhere = "Department";
        oe_FirstVisibleItem = "All";
        oe_ClickedVal = "";
        context = this;
        header_value = "";
        if(getIntent().getExtras() != null)
        {
            if(getIntent().getExtras().getString("selectedStringVal") != null)
            {
                header_value = getIntent().getExtras().getString("selectedStringVal");
            }
            else
            {
                header_value = "";
            }
        }
        else
        {
            header_value = "";
        }
        option_Efficiency = this;
        OptionefficiencyValue = "";
        focusposition = 0;
        oe_FirstPositionValue = 0;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLevel2Code = sharedPreferences.getString("concept","");
        lobId = sharedPreferences.getString("lobid","");
        isMultiStore = sharedPreferences.getString("isMultiStore","");
        String hierarchyLevels = sharedPreferences.getString("hierarchyLevels", "");
        value = sharedPreferences.getString("value","");
        // replace all labels using hierarchyList
        hierarchyList = hierarchyLevels.split(",");
        for (int i = 0; i <hierarchyList.length ; i++) {
            hierarchyList[i]=hierarchyList[i].trim();
        }
//        storeDescription = sharedPreferences.getString("storeDescription", "");
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();
        initializeUI();
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            processBar.setVisibility(View.GONE);
            offsetvalue = 0;
            limit = 100;
            count = 0;
            level = 1;

            oe_llayouthierarchy.setVisibility(View.GONE);
            if (getIntent().getStringExtra("selectedStringVal") == null)
            {
                // filter_toggleClick = false;
                seasonGroup = "Current";
                retainValuesFilter();
                requestHearderAPI();
            }
            else if (getIntent().getStringExtra("selectedStringVal") != null)
            {
                header_value = getIntent().getStringExtra("selectedStringVal");
                filter_level=getIntent().getIntExtra("selectedlevelVal",0);

                // filter_toggleClick = true;
                coreSelection = true;
                retainValuesFilter();
                optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
                requestOptionEfficiencyFilterVal(header_value,filter_level);
            }
        }
        else
        {
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
                Intent intent = new Intent(OptionEfficiencyActivity.this, SalesAnalysisFilter.class);
                intent.putExtra("checkfrom", "optionEfficiency");
                startActivity(intent);
            }
        });

        //  quick Filter
        oe_quickFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OEfficiency_SegmentClick.equals("Fashion")) {
                    filterFunction();
                } else {
                    View view = findViewById(android.R.id.content);
                    Snackbar.make(view, "Quick filter is not available on core selection", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        quickFilter_baseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maintainQuickFilterValues();
            }
        });

        // previous
        oe_btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OveridePositionValue =0;
                focusposition=0;
                if (postRequest != null) {
                    postRequest.cancel();
                }
                if (processBar.getVisibility() == View.VISIBLE) {
                    return;
                }
                OptionefficiencyValue = "";
//                header_value = "";

                if(oe_txtHeaderClass.getText().toString().equals(hierarchyList[4])){

                    oe_btnNext.setVisibility(View.VISIBLE);
                    oe_txtHeaderClass.setText(hierarchyList[3]);
                    fromWhere = hierarchyList[3];
                    level = 4;
                    flag = false;
                    optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.hDialog();
                        Reusable_Functions.sDialog(context, "Loading data...");
                        processBar.setVisibility(View.GONE);
                        oe_FirstVisibleItem = "All";
                        offsetvalue = 0;
                        limit = 100;
                        count = 0;
                        requestHearderAPI();

                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }

                }
                else if(oe_txtHeaderClass.getText().toString().equals(hierarchyList[3])){

                    oe_txtHeaderClass.setText(hierarchyList[2]);
                    fromWhere = hierarchyList[2];
                    level = 3;
                    flag = false;
                    optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.hDialog();
                        Reusable_Functions.sDialog(context, "Loading data...");
                        processBar.setVisibility(View.GONE);
                        oe_FirstVisibleItem = "All";
                        offsetvalue = 0;
                        limit = 100;
                        count = 0;
                        requestHearderAPI();

                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }

                }
                else if(oe_txtHeaderClass.getText().toString().equals(hierarchyList[2])){

                    oe_txtHeaderClass.setText(hierarchyList[1]);
                    fromWhere = hierarchyList[1];
                    level = 2;
                    flag = false;
                    optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
                    oe_llayouthierarchy.setVisibility(View.GONE);
                    llayoutOEfficiency.setVisibility(View.GONE);
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.hDialog();
                        Reusable_Functions.sDialog(context, "Loading data...");
                        processBar.setVisibility(View.GONE);
                        oe_FirstVisibleItem = "All";
                        offsetvalue = 0;
                        limit = 100;
                        count = 0;
                        seasonGroup = "Current";
                        requestHearderAPI();

                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }

                }
                else if(oe_txtHeaderClass.getText().toString().equals(hierarchyList[1])){

                    oe_btnPrev.setVisibility(View.INVISIBLE);
                    oe_txtHeaderClass.setText(hierarchyList[0]);
                    fromWhere = hierarchyList[0];
                    level = 1;
                    flag = false;
                    optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
                    oe_llayouthierarchy.setVisibility(View.GONE);
                    llayoutOEfficiency.setVisibility(View.GONE);
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.hDialog();
                        Reusable_Functions.sDialog(context, "Loading data...");
                        processBar.setVisibility(View.GONE);
                        oe_FirstVisibleItem = "All";
                        offsetvalue = 0;
                        limit = 100;
                        count = 0;
                        seasonGroup = "Current";
                        requestHearderAPI();

                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }

                }
            }

        });

        // next-----
        oe_btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                OveridePositionValue =0;
                focusposition=0;
                if (postRequest != null) {
                    postRequest.cancel();
                }
                if (processBar.getVisibility() == View.VISIBLE) {
                    return;
                }
                OptionefficiencyValue = "";
//                header_value = "";

                if(oe_txtHeaderClass.getText().toString().equals(hierarchyList[0])){

                    oe_btnPrev.setVisibility(View.VISIBLE);
                    oe_txtHeaderClass.setText(hierarchyList[1]);
                    fromWhere = hierarchyList[1];
                    level = 2;
                    flag = false;
                    optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.hDialog();
                        Reusable_Functions.sDialog(context, "Loading data...");
                        processBar.setVisibility(View.GONE);
                        oe_FirstVisibleItem = "All";
                        offsetvalue = 0;
                        limit = 100;
                        count = 0;
                        requestHearderAPI();
                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }

                }
                else if(oe_txtHeaderClass.getText().toString().equals(hierarchyList[1])){

                    fromWhere = hierarchyList[2];
                    oe_txtHeaderClass.setText(hierarchyList[2]);
                    level = 3;
                    flag = false;
                    optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.hDialog();
                        Reusable_Functions.sDialog(context, "Loading data...");
                        processBar.setVisibility(View.GONE);
                        oe_FirstVisibleItem = "All";
                        offsetvalue = 0;
                        limit = 100;
                        count = 0;
                        requestHearderAPI();

                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }

                }
                else if(oe_txtHeaderClass.getText().toString().equals(hierarchyList[2])){

                    oe_txtHeaderClass.setText(hierarchyList[3]);
                    fromWhere = hierarchyList[3];
                    level = 4;
                    flag = false;
                    optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.hDialog();
                        Reusable_Functions.sDialog(context, "Loading data...");
                        processBar.setVisibility(View.GONE);
                        oe_FirstVisibleItem = "All";
                        offsetvalue = 0;
                        limit = 100;
                        count = 0;
                        requestHearderAPI();

                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }

                }
                else if(oe_txtHeaderClass.getText().toString().equals(hierarchyList[3])){

                    oe_btnNext.setVisibility(View.INVISIBLE);
                    oe_txtHeaderClass.setText(hierarchyList[4]);
                    fromWhere = hierarchyList[4];
                    level = 5;
                    flag = false;
                    optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.hDialog();
                        Reusable_Functions.sDialog(context, "Loading data...");
                        processBar.setVisibility(View.GONE);
                        oe_FirstVisibleItem = "All";
                        offsetvalue = 0;
                        limit = 100;
                        count = 0;
                        requestHearderAPI();
                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });


        oe_listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                currentState = newState;
                if (prevState != RecyclerView.SCROLL_STATE_IDLE && currentState == RecyclerView.SCROLL_STATE_IDLE) {
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        public void run() {
                            if (!OnItemClick) {
                                TimeUP();
                            }
                        }
                    }, 700);
                }
                prevState = currentState;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerViewPositionHelper mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
                int visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mRecyclerViewHelper.getItemCount();
                focusposition = mRecyclerViewHelper.findFirstVisibleItemPosition();
            }
        });

        // hierarchy level drill down on selected item click
        oe_listView.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (processBar.getVisibility() == View.VISIBLE) {
                    return;
                } else {

                    OnItemClick = true;

                    if (position < optionEfficiencyDetailsArrayList.size()) {
                        OveridePositionValue =0;
                        focusposition=0;

                        if(oe_txtHeaderClass.getText().toString().equals(hierarchyList[0])){

                            oe_btnPrev.setVisibility(View.VISIBLE);
                            oe_txtHeaderClass.setText(hierarchyList[1]);
                            oe_ClickedVal = optionEfficiencyDetailsArrayList.get(position).getPlanDept();
                            fromWhere = hierarchyList[1];
                            level = 2;
                            seasonGroup = "Current";
                            if(!oe_ClickedVal.equals("All")) {
                                oe_ClickedVal = oe_ClickedVal.replace("%", "%25");
                                oe_ClickedVal = oe_ClickedVal.replace(" ", "%20").replace("&", "%26");
                                if(!header_value.contains("&department=" + oe_ClickedVal))
                                {
                                    header_value += "&department=" + oe_ClickedVal;
                                }
                            }
                            else
                            {
                                header_value = "";
                            }
                            if (Reusable_Functions.chkStatus(context)) {
                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                processBar.setVisibility(View.GONE);
                                if (postRequest != null) {
                                    postRequest.cancel();
                                }
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
                                request_OE_CategoryList(oe_ClickedVal);
                                oe_PlanDept = oe_ClickedVal;

                            } else {

                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();

                            }

                        }
                        else if(oe_txtHeaderClass.getText().toString().equals(hierarchyList[1])){

                            oe_txtHeaderClass.setText(hierarchyList[2]);
                            oe_ClickedVal = optionEfficiencyDetailsArrayList.get(position).getPlanCategory();
                            fromWhere = hierarchyList[2];
                            level = 3;
                            seasonGroup = "Current";
                            if(!oe_ClickedVal.equals("All")) {
                                oe_ClickedVal = oe_ClickedVal.replace("%", "%25");
                                oe_ClickedVal = oe_ClickedVal.replace(" ", "%20").replace("&", "%26");
                                if(!header_value.contains("&category=" + oe_ClickedVal))
                                {
                                    header_value += "&category=" + oe_ClickedVal;
                                }
                            }
                            else
                            {
                                header_value = "";
                            }
                            if (Reusable_Functions.chkStatus(context)) {
                                if (postRequest != null) {
                                    postRequest.cancel();
                                }
                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                processBar.setVisibility(View.GONE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
                                request_OE_PlanClassList(oe_PlanDept, oe_ClickedVal);
                                oe_Category = oe_ClickedVal;

                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else if(oe_txtHeaderClass.getText().toString().equals(hierarchyList[2])){

                            oe_txtHeaderClass.setText(hierarchyList[3]);
                            oe_ClickedVal = optionEfficiencyDetailsArrayList.get(position).getPlanClass();
                            fromWhere = hierarchyList[3];
                            seasonGroup = "Current";
                            level = 4;
                            if(!oe_ClickedVal.equals("All"))
                            {
                                oe_ClickedVal = oe_ClickedVal.replace("%", "%25");
                                oe_ClickedVal = oe_ClickedVal.replace(" ", "%20").replace("&", "%26");
                                if(!header_value.contains("&class=" + oe_ClickedVal))
                                {
                                    header_value += "&class=" + oe_ClickedVal;
                                }
                            }
                            else
                            {
                                header_value = "";
                            }
                            if (Reusable_Functions.chkStatus(context)) {
                                if (postRequest != null) {
                                    postRequest.cancel();
                                }
                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                processBar.setVisibility(View.GONE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
                                request_OE_BrandList(oe_PlanDept, oe_Category, oe_ClickedVal);
                                oe_PlanClass = oe_ClickedVal;
                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else if(oe_txtHeaderClass.getText().toString().equals(hierarchyList[3])){

                            oe_btnNext.setVisibility(View.INVISIBLE);
                            oe_txtHeaderClass.setText(hierarchyList[4]);
                            oe_ClickedVal = optionEfficiencyDetailsArrayList.get(position).getBrandName();
                            fromWhere = hierarchyList[4];
                            seasonGroup = "Current";
                            level = 5;
                            if(!oe_ClickedVal.equals("All"))
                            {
                                oe_ClickedVal = oe_ClickedVal.replace("%", "%25");
                                oe_ClickedVal = oe_ClickedVal.replace(" ", "%20").replace("&", "%26");
                                if(!header_value.contains("&brand=" + oe_ClickedVal))
                                {
                                    header_value += "&brand=" + oe_ClickedVal;
                                }
                            }
                            else
                            {
                                header_value = "";
                            }
                            if (Reusable_Functions.chkStatus(context)) {
                                if (postRequest != null) {
                                    postRequest.cancel();
                                }
                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                processBar.setVisibility(View.GONE);
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
                                request_OE_BrandPlanList(oe_PlanDept, oe_Category, oe_PlanClass, oe_ClickedVal);

                            } else {
                                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            }

                        }

                        else {

                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "You are at the last level of hierarchy", Toast.LENGTH_SHORT).show();
                            processBar.setVisibility(View.GONE);
                            OnItemClick = false;

                        }

                    }
                }


            }
        }));
    }

    private void maintainQuickFilterValues() {
        if (checkValueIs == null) {
            checkCurrent.setChecked(true);
            checkPrevious.setChecked(false);
            checkOld.setChecked(false);
            checkUpcoming.setChecked(false);

        } else {

            //in this checkvalueIs  save the previous done condition params and call to true or false
            switch (checkValueIs.toString()) {
                case "BestCheckCurrent":
                    checkCurrent.setChecked(true);
                    checkPrevious.setChecked(false);
                    checkOld.setChecked(false);
                    checkUpcoming.setChecked(false);
                    break;
                case "BestCheckPrevious":
                    checkCurrent.setChecked(false);
                    checkPrevious.setChecked(true);
                    checkOld.setChecked(false);
                    checkUpcoming.setChecked(false);
                    break;
                case "BestCheckOld":
                    checkCurrent.setChecked(false);
                    checkPrevious.setChecked(false);
                    checkOld.setChecked(true);
                    checkUpcoming.setChecked(false);
                    break;
                case "BestCheckUpcoming":
                    checkCurrent.setChecked(false);
                    checkPrevious.setChecked(false);
                    checkOld.setChecked(false);
                    checkUpcoming.setChecked(true);
                    break;
                default:
                    break;

            }
        }
        quickFilterPopup.setVisibility(View.GONE);
    }

    private void TimeUP() {

        if (optionEfficiencyDetailsArrayList.size() != 0) {

            if (focusposition < optionEfficiencyDetailsArrayList.size() && !OnItemClick) {

                if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[0])) {
                    level = 1;
                    oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(focusposition).getPlanDept().toString();
                } else if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[1])) {
                    level = 2;
                    oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(focusposition).getPlanCategory().toString();
                } else if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[2])) {
                    level = 3;
                    oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(focusposition).getPlanClass().toString();
                } else if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[3])) {
                    level = 4;
                    oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(focusposition).getBrandName().toString();
                } else if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[4])) {
                    level = 5;
                    oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(focusposition).getBrandplanClass().toString();
                }
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.hDialog();
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;

                    if (focusposition != OveridePositionValue) {

                        if (postRequest != null) {
                            postRequest.cancel();
                        }
                        processBar.setVisibility(View.VISIBLE);
                        if (oe_FirstVisibleItem.equals("All")) {
                            oeHeaderList = new ArrayList<OptionEfficiencyHeader>();
                            requestHeaderPieChart();
                        } else {
                            optionArrayList = new ArrayList<OptionEfficiencyDetails>();
                            requestOEPieChart();
                        }
                        OveridePositionValue = focusposition;
                    }


                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
            } else {

                focusposition = optionEfficiencyDetailsArrayList.size() - 1;
                LinearLayoutManager llm = (LinearLayoutManager) oe_listView.getLayoutManager();
                llm.scrollToPosition(focusposition);
                if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[0])) {
                    level = 1;
                    oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(focusposition).getPlanDept().toString();
                } else if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[1])) {
                    level = 2;
                    oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(focusposition).getPlanCategory().toString();
                } else if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[2])) {
                    level = 3;
                    oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(focusposition).getPlanClass().toString();
                } else if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[3])) {
                    level = 4;
                    oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(focusposition).getBrandName().toString();
                } else if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[4])) {
                    level = 5;
                    oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(focusposition).getBrandplanClass().toString();
                }
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.hDialog();
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    if (focusposition != OveridePositionValue) {
                        if (postRequest != null) {
                            postRequest.cancel();
                        }
                        processBar.setVisibility(View.VISIBLE);
                        if (oe_FirstVisibleItem.equals("All")) {
                            oeHeaderList = new ArrayList<OptionEfficiencyHeader>();
                            requestHeaderPieChart();
                        } else {
                            optionArrayList = new ArrayList<OptionEfficiencyDetails>();
                            requestOEPieChart();
                        }
                        OveridePositionValue = focusposition;
                    }
                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void initializeUI() {
        txtStoreCode = (TextView) findViewById(R.id.txtStoreCode);
        txtStoreDesc = (TextView) findViewById(R.id.txtStoreName);
        if(isMultiStore.equals("Yes"))
        {
            txtStoreCode.setText("Concept : ");
            txtStoreDesc.setText(value);

        }
        else
        {
            txtStoreCode.setText("Store : ");
            txtStoreDesc.setText(value);
        }
        processBar = (ProgressBar) findViewById(R.id.progressBar);
        txtNoChart = (TextView) findViewById(R.id.noChartOption);
        oe_txtHeaderClass = (TextView) findViewById(R.id.oe_txtHeaderClass);
        oe_txtDeptName = (TextView) findViewById(R.id.oe_txtDeptName);
        optionEfficiency_imageBtnBack = (RelativeLayout) findViewById(R.id.optionEfficiency_imageBtnBack);
        optionEfficiency_imgfilter = (RelativeLayout) findViewById(R.id.optionEfficiency_imgfilter);
        oe_quickFilter = (RelativeLayout) findViewById(R.id.oe_quickFilter);
        quickFilterPopup = (RelativeLayout) findViewById(R.id.quickFilterPopup);
        quickFilter_baseLayout = (RelativeLayout) findViewById(R.id.quickFilter_baseLayout);
        qfDoneLayout = (RelativeLayout) findViewById(R.id.qfDoneLayout);
        oe_pieChart = (PieChart) findViewById(R.id.oe_pieChart);
        oe_listView = (RecyclerView) findViewById(R.id.oe_list);
        llayoutOEfficiency = (LinearLayout) findViewById(R.id.llayoutOEfficiency);
        oe_llayouthierarchy = (LinearLayout) findViewById(R.id.oe_llayouthierarchy);
        oe_btnPrev = (RelativeLayout) findViewById(R.id.oe_btnPrev);
        oe_btnPrev.setVisibility(View.INVISIBLE);
        oe_btnNext = (RelativeLayout) findViewById(R.id.oe_btnNext);
        Tabview = (TabLayout) findViewById(R.id.tabview);
        Tabview.addTab(Tabview.newTab().setText("Fashion"));
        Tabview.addTab(Tabview.newTab().setText("Core"));
        quickFilter_BorderLayout = (RelativeLayout) findViewById(R.id.quickFilter_BorderLayout);
        optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
        optionArrayList = new ArrayList<OptionEfficiencyDetails>();
        headerList = new ArrayList<OptionEfficiencyDetails>();
        oeHeaderList = new ArrayList<OptionEfficiencyHeader>();
        checkCurrent = (CheckBox) findViewById(R.id.checkCurrent);
        checkPrevious = (CheckBox) findViewById(R.id.checkPrevious);
        checkOld = (CheckBox) findViewById(R.id.checkOld);
        checkUpcoming = (CheckBox) findViewById(R.id.checkUpcoming);
        optione_btnReset = (RelativeLayout)findViewById(R.id.optione_btnReset);
        oe_txtHeaderClass.setText(hierarchyList[0]);
        fromWhere = hierarchyList[0];
        Tabview.setOnTabSelectedListener(this);
        checkCurrent.setOnClickListener(this);
        checkPrevious.setOnClickListener(this);
        checkOld.setOnClickListener(this);
        checkUpcoming.setOnClickListener(this);
        qfDoneLayout.setOnClickListener(this);
        optione_btnReset.setOnClickListener(this);
        quickFilter_BorderLayout.setOnClickListener(this);
    }

    private void retainValuesFilter() {
        if (OEfficiency_SegmentClick.equals("Fashion")) {
            Tabview.getTabAt(0).select();
            coreSelection = false;
        } else {
            filter_toggleClick = true;    //toggle will apply whenever you change position that time it will forcefully call anotherwise it will not.
            Tabview.getTabAt(1).select();
            coreSelection = true;
        }
        maintainQuickFilterValues();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qfDoneLayout:
                if (Reusable_Functions.chkStatus(context)) {
                    OveridePositionValue =0;
                    focusposition=0;
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
            case R.id.optione_btnReset:
                optionArrayList = new ArrayList<OptionEfficiencyDetails>();
                optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
                header_value = "";
                focusposition = 0;OveridePositionValue = 0;
                if (Reusable_Functions.chkStatus(context))
                {
                    Reusable_Functions.sDialog(context, "Loading...");
                    processBar.setVisibility(View.GONE);
                    oe_llayouthierarchy.setVisibility(View.GONE);
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    level = 1;
                    fromWhere = hierarchyList[0];
                    oe_btnPrev.setVisibility(View.INVISIBLE);
                    oe_btnNext.setVisibility(View.VISIBLE);
                    oe_txtHeaderClass.setText(hierarchyList[0]);
                    OEfficiency_SegmentClick = "Fashion";
                    Tabview.getTabAt(0).select();
                    requestOptionEfficiencyDetails();
                }
                else
                {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    processBar.setVisibility(View.GONE);
                    Reusable_Functions.hDialog();
                }
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
            processBar.setVisibility(View.GONE);
            offsetvalue = 0;
            limit = 100;
            count = 0;
            seasonGroup = "Current";
            requestHearderAPI();

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
            processBar.setVisibility(View.GONE);
            offsetvalue = 0;
            limit = 100;
            count = 0;
            seasonGroup = "Previous";
            requestHearderAPI();
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
            processBar.setVisibility(View.GONE);
            offsetvalue = 0;
            limit = 100;
            count = 0;
            seasonGroup = "Old";
            requestHearderAPI();


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
            processBar.setVisibility(View.GONE);
            offsetvalue = 0;
            limit = 100;
            count = 0;
            seasonGroup = "Upcoming";
            requestHearderAPI();
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }
    //----------------------------API Declaration---------------------------//


    private void requestHearderAPI() {

        String url = "";
        if (coreSelection) {
            //core selection without season params
            url = ConstsCore.web_url + "/v1/display/optionefficiencyheaderNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;

        }
        else {
            //  fashion select with season params
            url = ConstsCore.web_url + "/v1/display/optionefficiencyheaderNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&seasongroup=" + seasonGroup + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;

        }


        postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                processBar.setVisibility(View.GONE);

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
                                    oeHeaderList.add(optionEfficiencyHeader);
                                }
                            }
                            optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
                            requestOptionEfficiencyDetails();
                        }
                        catch (Exception e)
                        {
                            Reusable_Functions.hDialog();
                            processBar.setVisibility(View.GONE);
                            Toast.makeText(context, " no data found  ", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        processBar.setVisibility(View.GONE);
                        Reusable_Functions.hDialog();

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
        String oedetails;
        if (coreSelection)
        {
            //core selection without season params
          if(!header_value.equals(""))
          {
              oedetails = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId + header_value;
          }
            else
          {
              oedetails = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
          }
         } else
        {
         // fashion select with season params
            if(!header_value.equals(""))
            {
                oedetails = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&seasongroup=" + seasonGroup + "&offset=" + offsetvalue + "&limit=" + limit+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId + header_value;
            }
            else
            {
                oedetails = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&seasongroup=" + seasonGroup + "&offset=" + offsetvalue + "&limit=" + limit+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
            }
        }
//        Log.e(TAG, "requestOptionEfficiencyDetails: "+oedetails);
        postRequest = new JsonArrayRequest(Request.Method.GET, oedetails,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        int i;

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                OnItemClick = false;
                                Reusable_Functions.hDialog();
                                llayoutOEfficiency.setVisibility(View.GONE);
                                processBar.setVisibility(View.GONE);
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

                                if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[0])) {
                                    optionEfficiencyDetails.setPlanDept("All");


                                } else if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[1])) {
                                    optionEfficiencyDetails.setPlanCategory("All");


                                } else if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[2])) {
                                    optionEfficiencyDetails.setPlanClass("All");

                                } else if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[3])) {
                                    optionEfficiencyDetails.setBrandName("All");

                                } else if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[4])) {
                                    optionEfficiencyDetails.setBrandplanClass("All");

                                }
                                optionEfficiencyDetails.setOptionCount(optionEfficiencyHeader.getOptionCount());
                                optionEfficiencyDetails.setFullSizeCount(optionEfficiencyHeader.getFullSizeCount());
                                optionEfficiencyDetails.setStkOnhandQty(optionEfficiencyHeader.getStkOnhandQty());
                                optionEfficiencyDetails.setSohCountFullSize(optionEfficiencyHeader.getSohCountFullSize());
                                optionEfficiencyDetailsArrayList.add(0, optionEfficiencyDetails);
                                oe_listView.setLayoutManager(new LinearLayoutManager(context));
                                oe_listView.setLayoutManager(new LinearLayoutManager(
                                        oe_listView.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                oe_listView.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(oe_listView);
                                optionIndexSnapAdapter = new OptionIndexSnapAdapter(optionEfficiencyDetailsArrayList, context, fromWhere, oe_listView,hierarchyList);
                                oe_listView.setAdapter(optionIndexSnapAdapter);

                                if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[0])) {
                                    level = 1;
                                    oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(focusposition).getPlanDept().toString();
                                    Boolean ContainEqual = false;
                                    for (int j = 0; j < optionEfficiencyDetailsArrayList.size(); j++) {
                                        if (optionEfficiencyDetailsArrayList.get(j).getPlanDept().equals(oe_FirstVisibleItem)) {
                                            LinearLayoutManager llm = (LinearLayoutManager) oe_listView.getLayoutManager();
                                            llm.scrollToPosition(focusposition);
                                            ContainEqual = true;
                                            if (oe_FirstVisibleItem.equals("All")) {
                                                oe_llayouthierarchy.setVisibility(View.GONE);
                                                requestHeaderPieChart();

                                            } else {

                                                flag = false;
                                                oe_llayouthierarchy.setVisibility(View.GONE);
                                                requestOEPieChart();

                                            }
                                        }
                                    }
                                    if (!ContainEqual) {
                                        flag = false;
                                        oe_llayouthierarchy.setVisibility(View.GONE);
                                        requestHeaderPieChart();
                                        OveridePositionValue = 0;
                                        focusposition = 0;
                                        Toast.makeText(context, "Selected item is not available in Core", Toast.LENGTH_SHORT).show();
                                    }


                                } else if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[1])) {
                                    level = 2;
                                    oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(focusposition).getPlanCategory().toString();
                                    Boolean ContainEqual = false;
                                    for (int j = 0; j < optionEfficiencyDetailsArrayList.size(); j++) {
                                        if (optionEfficiencyDetailsArrayList.get(j).getPlanCategory().contentEquals(oe_FirstVisibleItem)) {
                                            LinearLayoutManager llm = (LinearLayoutManager) oe_listView.getLayoutManager();
                                            llm.scrollToPosition(focusposition);
                                            ContainEqual = true;
                                            if (oe_FirstVisibleItem.equals("All")) {
                                                flag = false;
                                                oe_llayouthierarchy.setVisibility(View.GONE);
                                                requestHeaderPieChart();

                                            } else {
                                                flag = false;
                                                oe_llayouthierarchy.setVisibility(View.GONE);
                                                requestOEPieChart();

                                            }
                                        }


                                    }
                                    if (!ContainEqual) {
                                        flag = false;
                                        oe_llayouthierarchy.setVisibility(View.GONE);
                                        requestHeaderPieChart();
                                        OveridePositionValue = 0;
                                        focusposition = 0;
                                        Toast.makeText(context, "Selected item is not available in Core", Toast.LENGTH_SHORT).show();
                                    }

                                } else if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[2])) {
                                    level = 3;
                                    oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(focusposition).getPlanClass().toString();
                                    Boolean ContainEqual = false;
                                    for (int j = 0; j < optionEfficiencyDetailsArrayList.size(); j++) {
                                        if (optionEfficiencyDetailsArrayList.get(j).getPlanClass().contentEquals(oe_FirstVisibleItem)) {
                                            LinearLayoutManager llm = (LinearLayoutManager) oe_listView.getLayoutManager();
                                            llm.scrollToPosition(focusposition);
                                            ContainEqual = true;
                                            if (oe_FirstVisibleItem.equals("All")) {
                                                flag = false;
                                                oe_llayouthierarchy.setVisibility(View.GONE);
                                                requestHeaderPieChart();

                                            } else {
                                                flag = false;
                                                oe_llayouthierarchy.setVisibility(View.GONE);
                                                requestOEPieChart();

                                            }
                                        }
                                    }
                                    if (!ContainEqual) {
                                        flag = false;
                                        oe_llayouthierarchy.setVisibility(View.GONE);
                                        requestHeaderPieChart();
                                        OveridePositionValue = 0;
                                        focusposition = 0;
                                        Toast.makeText(context, "Selected item is not available in Core", Toast.LENGTH_SHORT).show();
                                    }

                                } else if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[3])) {
                                    level = 4;
                                    oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(focusposition).getBrandName().toString();
                                    Boolean ContainEqual = false;
                                    for (int j = 0; j < optionEfficiencyDetailsArrayList.size(); j++) {
                                        if (optionEfficiencyDetailsArrayList.get(j).getBrandName().contentEquals(oe_FirstVisibleItem)) {
                                            LinearLayoutManager llm = (LinearLayoutManager) oe_listView.getLayoutManager();
                                            llm.scrollToPosition(focusposition);
                                            ContainEqual = true;
                                            if (oe_FirstVisibleItem.equals("All")) {
                                                flag = false;
                                                oe_llayouthierarchy.setVisibility(View.GONE);
                                                requestHeaderPieChart();
                                            } else {
                                                flag = false;
                                                oe_llayouthierarchy.setVisibility(View.GONE);
                                                requestOEPieChart();

                                            }
                                        }
                                    }
                                    if (!ContainEqual) {
                                        flag = false;
                                        oe_llayouthierarchy.setVisibility(View.GONE);
                                        requestHeaderPieChart();
                                        OveridePositionValue = 0;
                                        focusposition = 0;
                                        Toast.makeText(context, "Selected item is not available in Core", Toast.LENGTH_SHORT).show();
                                    }

                                } else if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[4])) {
                                    level = 5;
                                    oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(focusposition).getBrandplanClass().toString();
                                    Boolean ContainEqual = false;
                                    for (int j = 0; j < optionEfficiencyDetailsArrayList.size(); j++) {
                                        if (optionEfficiencyDetailsArrayList.get(j).getBrandplanClass().contentEquals(oe_FirstVisibleItem)) {
                                            LinearLayoutManager llm = (LinearLayoutManager) oe_listView.getLayoutManager();
                                            llm.scrollToPosition(focusposition);
                                            ContainEqual = true;
                                            if (oe_FirstVisibleItem.equals("All"))
                                            {
                                                flag = false;
                                                oe_llayouthierarchy.setVisibility(View.GONE);
                                                requestHeaderPieChart();
                                            }
                                            else
                                            {
                                                flag = false;
                                                oe_llayouthierarchy.setVisibility(View.GONE);
                                                requestOEPieChart();
                                            }
                                        }
                                    }
                                    if (!ContainEqual) {
                                        flag = false;
                                        oe_llayouthierarchy.setVisibility(View.GONE);
                                        requestHeaderPieChart();
                                        OveridePositionValue = 0;
                                        focusposition = 0;
                                        Toast.makeText(context, "Selected item is not available in Core", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                oeHeaderList = new ArrayList<OptionEfficiencyHeader>();
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                            }


                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no data found in catch ", Toast.LENGTH_SHORT).show();
                            llayoutOEfficiency.setVisibility(View.GONE);
                            processBar.setVisibility(View.GONE);
                            OnItemClick = false;
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
                        processBar.setVisibility(View.GONE);
                        OnItemClick = false;
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

    // Api for filter selected value
    private void requestOptionEfficiencyFilterVal(final String selectedString,final int filter_level) {
        String oe_filterval_url = "";
        if (coreSelection)
        {
            //core selection without season params


            if(filter_level != 0)
            {
                oe_filterval_url = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + filter_level + selectedString + "&offset=" + offsetvalue + "&limit=" + limit+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;

            }
            else
            {
                oe_filterval_url = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick  + selectedString + "&offset=" + offsetvalue + "&limit=" + limit+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;

            }

        } else {
            //fashion selection with season params
            if(filter_level != 0) {
                oe_filterval_url = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + filter_level + selectedString + "&offset=" + offsetvalue + "&limit=" + limit + "&seasongroup=" + seasonGroup + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;
            }
            else
            {
                oe_filterval_url = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick  + selectedString + "&offset=" + offsetvalue + "&limit=" + limit + "&seasongroup=" + seasonGroup + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;

            }
        }
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, oe_filterval_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if (filter_level == 2)
                        {
                            level=2;
                            oe_txtHeaderClass.setText(hierarchyList[1]);
                            fromWhere = hierarchyList[1];
                            oe_btnPrev.setVisibility(View.VISIBLE);

                        }
                        else if (filter_level == 3)
                        {
                            level=3;
                            oe_txtHeaderClass.setText(hierarchyList[2]);
                            fromWhere = hierarchyList[2];
                            oe_btnPrev.setVisibility(View.VISIBLE);

                        } else if (filter_level == 4) {

                            level=4;
                            oe_txtHeaderClass.setText(hierarchyList[3]);
                            fromWhere = hierarchyList[3];
                            oe_btnPrev.setVisibility(View.VISIBLE);

                        } else if (filter_level == 5) {

                            level=5;
                            oe_txtHeaderClass.setText(hierarchyList[4]);
                            fromWhere = hierarchyList[4];
                            oe_btnPrev.setVisibility(View.VISIBLE);
                            oe_btnNext.setVisibility(View.INVISIBLE);


                        } else if (filter_level == 5) {

                            level=5;
                            oe_txtHeaderClass.setText(hierarchyList[4]);
                            fromWhere = hierarchyList[4];
                            oe_btnPrev.setVisibility(View.VISIBLE);
                            oe_btnNext.setVisibility(View.INVISIBLE);

                        }
                        int i;
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                oe_pieChart.setData(null);
                                oe_pieChart.notifyDataSetChanged();
                                oe_pieChart.invalidate();
                                oe_listView.setAdapter(null);
                                processBar.setVisibility(View.GONE);
                                OnItemClick = false;
                                return;

                            } else if (response.length() == limit) {
                                for (i = 0; i < response.length(); i++) {
                                    optionEfficiencyDetails = gson.fromJson(response.get(i).toString(), OptionEfficiencyDetails.class);
                                    optionEfficiencyDetailsArrayList.add(optionEfficiencyDetails);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestOptionEfficiencyFilterVal(selectedString, filter_level);

                            } else if (response.length() < limit) {
                                for (i = 0; i < response.length(); i++) {

                                    optionEfficiencyDetails = gson.fromJson(response.get(i).toString(), OptionEfficiencyDetails.class);
                                    optionEfficiencyDetailsArrayList.add(optionEfficiencyDetails);
                                }
                            }

                            oeHeaderList = new ArrayList<OptionEfficiencyHeader>();
                            setFilterHeaderValue();



                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no data found"+e.getMessage(), Toast.LENGTH_SHORT).show();
                            processBar.setVisibility(View.GONE);
                            OnItemClick = false;
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        OnItemClick = false;
                        processBar.setVisibility(View.GONE);
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

    private void setFilterHeaderValue()
    {
        String url = "";
        if (coreSelection) {
            //core selection without season params
            if(!header_value.equals(""))
            {
                url = ConstsCore.web_url + "/v1/display/optionefficiencyheaderNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId+""+header_value;
            }
            else
            {
                url = ConstsCore.web_url + "/v1/display/optionefficiencyheaderNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
            }

        } else {
            //  fashion select with season params
            if(!header_value.equals(""))
            {
                url = ConstsCore.web_url + "/v1/display/optionefficiencyheaderNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&seasongroup=" + seasonGroup+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId+""+header_value;
            }
            else
            {
                url = ConstsCore.web_url + "/v1/display/optionefficiencyheaderNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&seasongroup=" + seasonGroup+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
            }

        }
        postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                return;

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    optionEfficiencyHeader = gson.fromJson(response.get(i).toString(), OptionEfficiencyHeader.class);
                                    oeHeaderList.add(optionEfficiencyHeader);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;

                                setFilterHeaderValue();

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    optionEfficiencyHeader = gson.fromJson(response.get(i).toString(), OptionEfficiencyHeader.class);
                                    oeHeaderList.add(optionEfficiencyHeader);
                                }
                            }
                            optionEfficiencyDetails = new OptionEfficiencyDetails();
                            if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[0]))
                            {
                                optionEfficiencyDetails.setPlanDept("All");
                            }
                            else if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[1]))
                            {
                                optionEfficiencyDetails.setPlanCategory("All");
                            }
                            if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[2]))
                            {
                                optionEfficiencyDetails.setPlanClass("All");
                            }
                            if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[3]))
                            {
                                optionEfficiencyDetails.setBrandName("All");
                            }
                            if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[4]))
                            {
                                optionEfficiencyDetails.setBrandplanClass("All");
                            }

                            optionEfficiencyDetails.setOptionCount(optionEfficiencyHeader.getOptionCount());
                            optionEfficiencyDetails.setFullSizeCount(optionEfficiencyHeader.getFullSizeCount());
                            optionEfficiencyDetails.setStkOnhandQty(optionEfficiencyHeader.getStkOnhandQty());
                            optionEfficiencyDetails.setSohCountFullSize(optionEfficiencyHeader.getSohCountFullSize());
                            optionEfficiencyDetailsArrayList.add(0, optionEfficiencyDetails);
                            oe_listView.setLayoutManager(new LinearLayoutManager(context));

                            oe_listView.setLayoutManager(new LinearLayoutManager(
                                    oe_listView.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                    LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                            oe_listView.setOnFlingListener(null);
                            new GravitySnapHelper(48).attachToRecyclerView(oe_listView);
                            optionIndexSnapAdapter = new OptionIndexSnapAdapter(optionEfficiencyDetailsArrayList, context, fromWhere, oe_listView,hierarchyList);
                            oe_listView.setAdapter(optionIndexSnapAdapter);
                            optionIndexSnapAdapter.notifyDataSetChanged();

                            offsetvalue = 0;
                            limit = 100;
                            count = 0;
                            optionArrayList.clear();
                            if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[0])) {
                                level = 1;
                                oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(0).getPlanDept().toString();
                            } else if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[1])) {
                                level = 2;
                                oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(0).getPlanCategory().toString();
                            } else if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[2])) {
                                level = 3;
                                oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(0).getPlanClass().toString();
                            } else if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[3])) {
                                level = 4;
                                oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(0).getBrandName().toString();
                            } else if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[4])) {
                                level = 5;
                                oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(0).getBrandplanClass().toString();
                            }
                            requestOEPieChart();


                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, " no data found  ", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
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
        String url = "";
        offsetvalue = 0;
        limit = 100;
        count = 0;
        oeHeaderList = new ArrayList<OptionEfficiencyHeader>();
        if (coreSelection) {
            //core selection without season group
            if(!header_value.equals(""))
            {
                url = ConstsCore.web_url + "/v1/display/optionefficiencyheaderNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId+""+header_value;
            }
            else
            {
                url = ConstsCore.web_url + "/v1/display/optionefficiencyheaderNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;
            }
        } else {
            //fashion selection with season group
            if(!header_value.equals(""))
            {
                url = ConstsCore.web_url + "/v1/display/optionefficiencyheaderNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit + "&seasongroup=" + seasonGroup + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId+""+header_value;
            }
            else
            {
                url = ConstsCore.web_url + "/v1/display/optionefficiencyheaderNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit + "&seasongroup=" + seasonGroup + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;
            }
        }
//        Log.e(TAG, "requestHeaderPieChart: "+url);
        postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Reusable_Functions.hDialog();

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Toast.makeText(context, "no chart data found", Toast.LENGTH_SHORT).show();
                                llayoutOEfficiency.setVisibility(View.VISIBLE);
                                oe_pieChart.clear();
                                oe_pieChart.clearValues();
                                oe_pieChart.clearFocus();
                                oe_pieChart.invalidate();
                                Reusable_Functions.hDialog();
                                OnItemClick = false;
                                processBar.setVisibility(View.GONE);

                            } else
                            {
                                for (int i = 0; i < response.length(); i++) {
                                    optionEfficiencyHeader = gson.fromJson(response.get(i).toString(), OptionEfficiencyHeader.class);
                                    oeHeaderList.add(optionEfficiencyHeader);

                                }


                                entries = new ArrayList<PieEntry>();
                                for (OptionEfficiencyHeader optionEfficiency : oeHeaderList) {

                                    fullSizeCount = (float) optionEfficiency.getFullSizeCount();
                                    partCutCount = (float) optionEfficiency.getPartCutCount();
                                    fullCutCount = (float) optionEfficiency.getFullCutCount();


                                }
                                ArrayList<Integer> colors = new ArrayList<>();
                                colors.add(Color.parseColor("#20b5d3"));
                                colors.add(Color.parseColor("#21d24c"));
                                colors.add(Color.parseColor("#f5204c"));
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

                                PieDataSet dataset = new PieDataSet(entries, "");
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
                                processBar.setVisibility(View.GONE);
                                OnItemClick = false;
                                processBar.setVisibility(View.GONE);
                                Reusable_Functions.hDialog();


                            }


                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, " no data found ", Toast.LENGTH_SHORT).show();
                            llayoutOEfficiency.setVisibility(View.VISIBLE);
                            OnItemClick = false;
                            processBar.setVisibility(View.GONE);
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
                        OnItemClick = false;
                        processBar.setVisibility(View.GONE);
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
        if (coreSelection) {
            //core selection without seasongroup
            oe_category_listurl = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&department=" + deptName.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;

        } else {
            //fashion selection with seasiongroup
            oe_category_listurl = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&department=" + deptName.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit + "&seasongroup=" + seasonGroup+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;

        }
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, oe_category_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int i;

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                OnItemClick = false;
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "No Category data found", Toast.LENGTH_SHORT).show();
                                return;

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
                                oeHeaderList = new ArrayList<OptionEfficiencyHeader>();
                                optionEfficiencyDetails = new OptionEfficiencyDetails();
                                if(oe_txtHeaderClass.getText().toString().equals(hierarchyList[1])) {
                                    optionEfficiencyDetails.setPlanCategory("All");

                                }

                                setHeaderValue(deptName,optionEfficiencyDetails, 2);
                            }




                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            OnItemClick = false;
                            Toast.makeText(context, "No Category data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        OnItemClick = false;
                        Toast.makeText(context, "No Category data found", Toast.LENGTH_SHORT).show();
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

    private void setHeaderValue(final String Name, final OptionEfficiencyDetails optionEfficiencyDetails, final int level) {

        String url = "";
        if (coreSelection) {
            //core selection without season params
            if(!header_value.equals(""))
            {
                url = ConstsCore.web_url + "/v1/display/optionefficiencyheaderNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + OptionEfficiencyActivity.level + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId+""+header_value;
            }
            else
            {
                url = ConstsCore.web_url + "/v1/display/optionefficiencyheaderNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + OptionEfficiencyActivity.level + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;
            }
        }
        else {
            //  fashion select with season params
            if(!header_value.equals("")) {
                url = ConstsCore.web_url + "/v1/display/optionefficiencyheaderNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + OptionEfficiencyActivity.level + "&seasongroup=" + seasonGroup + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId+""+header_value;
            }
            else
            {
                url = ConstsCore.web_url + "/v1/display/optionefficiencyheaderNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + OptionEfficiencyActivity.level + "&seasongroup=" + seasonGroup + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;
            }
        }

        postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, " no data found  ", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else
                            {
                                for (int i = 0; i < response.length(); i++) {

                                    optionEfficiencyHeader = gson.fromJson(response.get(i).toString(), OptionEfficiencyHeader.class);
                                    oeHeaderList.add(optionEfficiencyHeader);
                                }


                            }


                            updheader(Name, optionEfficiencyDetails, level);

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, " no data found  ", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
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

    public String hierarchy(String freshnessIndex_ClickedVal) {

        if (OptionefficiencyValue == null || OptionefficiencyValue.equals("")) {

            OptionefficiencyValue = freshnessIndex_ClickedVal;

        } else {

            OptionefficiencyValue += " > " + freshnessIndex_ClickedVal;
        }


        return OptionefficiencyValue;

    }

    // For Plan Class on click of Category Val
    private void request_OE_PlanClassList(final String deptName, final String category) {
        String oe_planclass_listurl = " ";
        if (coreSelection) {

            oe_planclass_listurl = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&category=" + category.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
        } else {
            oe_planclass_listurl = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&category=" + category.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit + "&seasongroup=" + seasonGroup+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
        }


        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, oe_planclass_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                OnItemClick = false;
                                Toast.makeText(context, "No Class data found", Toast.LENGTH_SHORT).show();
                                return;

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
                                oeHeaderList = new ArrayList<OptionEfficiencyHeader>();

                                optionEfficiencyDetails = new OptionEfficiencyDetails();
                                if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[2])) {
                                    optionEfficiencyDetails.setPlanClass("All");

                                }
                                setHeaderValue(category, optionEfficiencyDetails, 3);

                            }



                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            OnItemClick = false;
                            Toast.makeText(context, "No Class data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        OnItemClick = false;
                        Toast.makeText(context, "No Class data found", Toast.LENGTH_SHORT).show();
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
        String oe_brand_listurl = "";
        if (coreSelection) {
            oe_brand_listurl = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&class=" + planclass.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;

        } else {
            oe_brand_listurl = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&class=" + planclass.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit + "&seasongroup=" + seasonGroup+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
        }

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, oe_brand_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                OnItemClick = false;
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "No Brand data found", Toast.LENGTH_SHORT).show();
                                return;

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    optionEfficiencyDetails = gson.fromJson(response.get(i).toString(), OptionEfficiencyDetails.class);
                                    optionEfficiencyDetailsArrayList.add(optionEfficiencyDetails);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                request_OE_BrandList(oe_PlanDept, oe_Category, planclass);

                            } else if (response.length() < limit)
                            {
                                for (int i = 0; i < response.length(); i++) {

                                    optionEfficiencyDetails = gson.fromJson(response.get(i).toString(), OptionEfficiencyDetails.class);
                                    optionEfficiencyDetailsArrayList.add(optionEfficiencyDetails);
                                }
                                oeHeaderList = new ArrayList<OptionEfficiencyHeader>();
//                              setHeaderValue();
                                optionEfficiencyDetails = new OptionEfficiencyDetails();
                                if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[3])) {
                                    optionEfficiencyDetails.setBrandName("All");
                                }
                                setHeaderValue(planclass, optionEfficiencyDetails, 4);

                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            OnItemClick = false;
                            Toast.makeText(context, "No Brand data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        OnItemClick = false;
                        Toast.makeText(context, "No Brand data found", Toast.LENGTH_SHORT).show();
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
    private void request_OE_BrandPlanList(String deptName, String category, String plan_class, final String brandnm)
    {
        String oe_brandplan_listurl = "";
        if (coreSelection)
        {
            oe_brandplan_listurl = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&brand=" + brandnm.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;

        }
        else
        {
            oe_brandplan_listurl = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&brand=" + brandnm.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit + "&seasongroup=" + seasonGroup+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
        }

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, oe_brandplan_listurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {

                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                OnItemClick = false;
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "No Brand Class data found", Toast.LENGTH_SHORT).show();
                                return;

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
                                oeHeaderList = new ArrayList<OptionEfficiencyHeader>();
                                optionEfficiencyDetails = new OptionEfficiencyDetails();
                                if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[4])) {
                                    optionEfficiencyDetails.setBrandplanClass("All");
                                }
                                setHeaderValue(brandnm, optionEfficiencyDetails, 5);
                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            OnItemClick = false;
                            Toast.makeText(context, "No Brand Class data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        OnItemClick = false;
                        Toast.makeText(context, "No Brand Class data found", Toast.LENGTH_SHORT).show();
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
//        if(oe_FirstVisibleItem.equals("All"))
//        {
//            requestHeaderPieChart();
//            return;
//        }
        String url = "";
        txtNoChart.setVisibility(View.GONE);
        oe_FirstVisibleItem = oe_FirstVisibleItem.replace("%", "%25");
        oe_FirstVisibleItem = oe_FirstVisibleItem.replace(" ", "%20").replace("&", "%26");
        if(!header_value.equals(""))
        {
            if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[0])) {
                if (coreSelection) {
                    //core selection without seasongroup
                    url = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&department=" + oe_FirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId + header_value;
                } else {
                    url = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&department=" + oe_FirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit + "&seasongroup=" + seasonGroup+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId +header_value;
                }
            } else if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[1])) {
                if (coreSelection) {
                    url = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&category=" + oe_FirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId + header_value;
                } else {
                    url = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&category=" + oe_FirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit + "&seasongroup=" + seasonGroup+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId + header_value;
                }
            } else if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[2])) {
                if (coreSelection) {
                    url = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&class=" + oe_FirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId + header_value;
                } else {
                    url = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&class=" + oe_FirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit + "&seasongroup=" + seasonGroup+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId + header_value;
                }
            } else if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[3])) {
                if (coreSelection) {
                    url = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&brand=" + oe_FirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId + header_value;
                } else {
                    url = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&brand=" + oe_FirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit + "&seasongroup=" + seasonGroup+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId + header_value;
                }
            } else if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[4])) {
                if (coreSelection) {
                    url = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&brandclass=" + oe_FirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId + header_value;
                } else {
                    url = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&brandclass=" + oe_FirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit + "&seasongroup=" + seasonGroup+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId + header_value;
                }
            }
        }
        else
        {
            if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[0])) {
                if (coreSelection) {
                    //core selection without seasongroup
                    url = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&department=" + oe_FirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
                } else {
                    url = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&department=" + oe_FirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit + "&seasongroup=" + seasonGroup+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
                }
            } else if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[1])) {
                if (coreSelection) {
                    url = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&category=" + oe_FirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
                } else {
                    url = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&category=" + oe_FirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit + "&seasongroup=" + seasonGroup+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
                }
            } else if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[2])) {
                if (coreSelection) {
                    url = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&class=" + oe_FirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
                } else {
                    url = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&class=" + oe_FirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit + "&seasongroup=" + seasonGroup+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
                }
            } else if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[3])) {
                if (coreSelection) {
                    url = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&brand=" + oe_FirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
                } else {
                    url = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&brand=" + oe_FirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit + "&seasongroup=" + seasonGroup+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
                }
            } else if (oe_txtHeaderClass.getText().toString().equals(hierarchyList[4])) {
                if (coreSelection) {
                    url = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&brandclass=" + oe_FirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
                } else {
                    url = ConstsCore.web_url + "/v1/display/optionefficiencydetailNew/" + userId + "?corefashion=" + OEfficiency_SegmentClick + "&level=" + level + "&brandclass=" + oe_FirstVisibleItem + "&offset=" + offsetvalue + "&limit=" + limit + "&seasongroup=" + seasonGroup+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
                }
            }
        }


//        Log.e(TAG, "requestOEPieChart: "+url);
        postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            int i;
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                OnItemClick = false;
                                oe_pieChart.clear();
                                oe_pieChart.clearValues();
                                oe_pieChart.clearFocus();
                                oe_pieChart.invalidate();
                                Reusable_Functions.hDialog();
                                processBar.setVisibility(View.GONE);
                                return;

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

                                entries = new ArrayList<PieEntry>();
                                oe_FirstVisibleItem = oe_FirstVisibleItem.replace("%25", "%");
                                oe_FirstVisibleItem = oe_FirstVisibleItem.replace("%20", " ").replace("%26", "&");
                                for (OptionEfficiencyDetails optionEfficiency : optionArrayList) {
                                    if (oe_FirstVisibleItem.equals("All")) {
                                        fullSizeCount = (float) optionEfficiency.getFullSizeCount();
                                        partCutCount = (float) optionEfficiency.getPartCutCount();
                                        fullCutCount = (float) optionEfficiency.getFullCutCount();
                                    } else if (oe_FirstVisibleItem.equals(optionEfficiency.getPlanDept())) {
                                        fullSizeCount = (float) optionEfficiency.getFullSizeCount();
                                        partCutCount = (float) optionEfficiency.getPartCutCount();
                                        fullCutCount = (float) optionEfficiency.getFullCutCount();
                                    } else if (oe_FirstVisibleItem.equals(optionEfficiency.getPlanCategory())) {
                                        fullSizeCount = (float) optionEfficiency.getFullSizeCount();
                                        partCutCount = (float) optionEfficiency.getPartCutCount();
                                        fullCutCount = (float) optionEfficiency.getFullCutCount();
                                    } else if (oe_FirstVisibleItem.equals(optionEfficiency.getPlanClass())) {
                                        fullSizeCount = (float) optionEfficiency.getFullSizeCount();
                                        partCutCount = (float) optionEfficiency.getPartCutCount();
                                        fullCutCount = (float) optionEfficiency.getFullCutCount();
                                    } else if (oe_FirstVisibleItem.equals(optionEfficiency.getBrandName())) {
                                        fullSizeCount = (float) optionEfficiency.getFullSizeCount();
                                        partCutCount = (float) optionEfficiency.getPartCutCount();
                                        fullCutCount = (float) optionEfficiency.getFullCutCount();
                                    } else if (oe_FirstVisibleItem.equals(optionEfficiency.getBrandplanClass())) {
                                        fullSizeCount = (float) optionEfficiency.getFullSizeCount();
                                        partCutCount = (float) optionEfficiency.getPartCutCount();
                                        fullCutCount = (float) optionEfficiency.getFullCutCount();
                                    }
                                }
                                ArrayList<Integer> colors = new ArrayList<>();
                                colors.add(Color.parseColor("#20b5d3"));
                                colors.add(Color.parseColor("#21d24c"));
                                colors.add(Color.parseColor("#f5204c"));
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
                                PieDataSet dataset = new PieDataSet(entries, "");
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
                                processBar.setVisibility(View.GONE);
                                OnItemClick = false;
                                processBar.setVisibility(View.GONE);
                                Reusable_Functions.hDialog();
                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, " no data found ", Toast.LENGTH_SHORT).show();
                            OnItemClick = false;
                            processBar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "Server not found...", Toast.LENGTH_SHORT).show();
                        OnItemClick = false;
                        processBar.setVisibility(View.GONE);
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

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int checkedId = Tabview.getSelectedTabPosition();


        OnItemClick = true;
        OptionefficiencyValue = "";
        if (!filter_toggleClick) {
            switch (checkedId) {

                case 1:   //core selection
                    if (OEfficiency_SegmentClick.equals("Core"))
                        break;
                    OEfficiency_SegmentClick = "Core";
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.hDialog();
                        processBar.setVisibility(View.VISIBLE);
                        offsetvalue = 0;
                        limit = 100;
                        count = 0;
                        coreSelection = true;
//                        if (getIntent().getStringExtra("selectedDept") == null) {
                            requestHearderAPI();
//                            Handler h = new Handler();
//                        } else if (getIntent().getStringExtra("selectedDept") != null) {
//                            String selectedString = getIntent().getStringExtra("selectedDept");
//                            optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
//                            requestOptionEfficiencyFilterVal(selectedString, filter_level);
//
//                        }
                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case 0:  // fashion selection

                    if (OEfficiency_SegmentClick.equals("Fashion"))
                        break;
                    OEfficiency_SegmentClick = "Fashion";
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.hDialog();
                        processBar.setVisibility(View.VISIBLE);
                        offsetvalue = 0;
                        limit = 100;
                        count = 0;
                        coreSelection = false;

//                        if (getIntent().getStringExtra("selectedDept") == null) {
                            requestHearderAPI();

//                        } else if (getIntent().getStringExtra("selectedDept") != null) {
//                            String selectedString = getIntent().getStringExtra("selectedDept");
//                            optionEfficiencyDetailsArrayList = new ArrayList<OptionEfficiencyDetails>();
//                            requestOptionEfficiencyFilterVal(selectedString, filter_level);
//                        }

                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }
                    break;

                default:
                    break;

            }
        } else {
            filter_toggleClick = false;
        }

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public class MyValueFormatter implements IValueFormatter {
        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,###,##0.0"); // use two decimal if needed
        }

        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            if (value < 0.0) {
                return "";
            } else {
                String val = String.valueOf(Html.fromHtml("\n 8,11,235"));
                return mFormat.format(value) + " %";
            }
        }
    }

    @Override
    public void onBackPressed() {
        OEfficiency_SegmentClick = null;
        seasonGroup = null;
        level = 0;
        checkValueIs = null;
        OEfficiency_SegmentClick = "Fashion";
        seasonGroup = "Current";
        level = 1;
        finish();
    }


    public void updheader(String name, OptionEfficiencyDetails optionEfficiencyDetails, int level)
    {
        optionEfficiencyDetails.setOptionCount(optionEfficiencyHeader.getOptionCount());
        optionEfficiencyDetails.setFullSizeCount(optionEfficiencyHeader.getFullSizeCount());
        optionEfficiencyDetails.setStkOnhandQty(optionEfficiencyHeader.getStkOnhandQty());
        optionEfficiencyDetails.setSohCountFullSize(optionEfficiencyHeader.getSohCountFullSize());
        optionEfficiencyDetails.setPartCutCount(optionEfficiencyHeader.getPartCutCount());
        optionEfficiencyDetails.setFullCutCount(optionEfficiencyHeader.getFullCutCount());
        optionEfficiencyDetailsArrayList.add(0, this.optionEfficiencyDetails);
        oe_listView.setLayoutManager(new LinearLayoutManager(context));
        this.optionEfficiencyDetails = optionEfficiencyDetailsArrayList.get(0);


        oe_listView.setLayoutManager(new LinearLayoutManager(
                oe_listView.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
        oe_listView.setOnFlingListener(null);
        new GravitySnapHelper(48).attachToRecyclerView(oe_listView);

        optionIndexSnapAdapter = new OptionIndexSnapAdapter(optionEfficiencyDetailsArrayList, context, fromWhere, oe_listView,hierarchyList);
        oe_listView.setAdapter(optionIndexSnapAdapter);

        oe_txtDeptName.setText(hierarchy(name.replaceAll("%20"," ").replaceAll("%26","&")));//deptName));
        oe_llayouthierarchy.setVisibility(View.VISIBLE);

        if(level == 2)
        {
            oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(0).getPlanCategory().toString();
        }
        else if(level == 3)
        {
            oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(0).getPlanClass().toString();
        }
        else if(level == 4)
        {
            oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(0).getBrandName().toString();
        }
        else if(level == 5)
        {
            oe_FirstVisibleItem = optionEfficiencyDetailsArrayList.get(0).getBrandplanClass().toString();
        }

        offsetvalue = 0;
        limit = 100;
        count = 0;
        OptionEfficiencyActivity.level = level;
//        requestHeaderPieChart();

        entries = new ArrayList<PieEntry>();
        for (OptionEfficiencyHeader optionEfficiency : oeHeaderList) {

            fullSizeCount = (float) optionEfficiency.getFullSizeCount();
            partCutCount = (float) optionEfficiency.getPartCutCount();
            fullCutCount = (float) optionEfficiency.getFullCutCount();


        }
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#20b5d3"));
        colors.add(Color.parseColor("#21d24c"));
        colors.add(Color.parseColor("#f5204c"));
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

        PieDataSet dataset = new PieDataSet(entries, "");
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
        processBar.setVisibility(View.GONE);
        OnItemClick = false;
        processBar.setVisibility(View.GONE);
        Reusable_Functions.hDialog();

    }
}
