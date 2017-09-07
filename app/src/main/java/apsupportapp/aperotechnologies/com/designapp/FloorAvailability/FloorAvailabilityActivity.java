package apsupportapp.aperotechnologies.com.designapp.FloorAvailability;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesFilterActivity;
import apsupportapp.aperotechnologies.com.designapp.model.FloorAvailabilityDetails;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by pamrutkar on 07/12/16.
 */

public class FloorAvailabilityActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener,TabLayout.OnTabSelectedListener {

    TextView floor_txtStoreCode, floor_txtStoreName;
    RelativeLayout floor_BtnBack, floor_BtnFilter, floor_quickFilter, quickFilterPopup;
    RelativeLayout quickFilter_baseLayout, qfDoneLayout, quickFilter_BorderLayout;
    FloorAvailabilityDetails floorAvailabilityDetails;
    private SharedPreferences sharedPreferences;
    String userId, bearertoken,storeDescription;
    private static String seasongroup = "Current";
    private int count = 0;
    private boolean coreSelection = false;
    private int limit = 10;
    private int offsetvalue = 0;
    private int top = 10;
    CheckBox checkCurrent, checkPrevious, checkOld, checkUpcoming;
    Context context = this;
    private RequestQueue queue;
    private Gson gson;
    ListView floorListView;
    ArrayList<FloorAvailabilityDetails> FloorList;
    FloorAvailabilityAdapter floorAvailabilityAdapter;
    private View footer;
    private String lazyScroll = "OFF";
    private SegmentedGroup floor_segmented;
    private RadioButton floor_fashion, floor_core;
    private ToggleButton Toggle_floor_fav;
    private static String corefashion = "Fashion";
    private static String floorcheckSeasonGpVal = null;
    public static Activity floorAvailability;
    private boolean from_filter = false;
    private String selectedString = "";
    private boolean toggleClick = false;
    private TabLayout Tabview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_availability);
        getSupportActionBar().hide();
        initalise();
        gson = new Gson();
        floorAvailability = this;
        FloorList = new ArrayList<FloorAvailabilityDetails>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        storeDescription = sharedPreferences.getString("storeDescription","");
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        floor_txtStoreCode.setText(storeDescription.trim().substring(0,4));
        floor_txtStoreName.setText(storeDescription.substring(5));
        floorListView.setTag("FOOTER");
        floorListView.setVisibility(View.VISIBLE);

        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            offsetvalue = 0;
            limit = 10;
            count = 0;
            top = 10;
            if (getIntent().getStringExtra("selectedDept") == null) {
                from_filter = false;
            } else if (getIntent().getStringExtra("selectedDept") != null) {
                selectedString = getIntent().getStringExtra("selectedDept");
                from_filter = true;
            }
            RetainFromMain_filter();
            requestFloorAvailabilityApi(selectedString);
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
        footer = getLayoutInflater().inflate(R.layout.bestpromo_footer, null);
        floorListView.addFooterView(footer);
    }

    private void requestFloorAvailabilityApi(final String selectedString) {
    if (Reusable_Functions.chkStatus(context)) {

            String url;
            if (from_filter) {
                if (coreSelection) {
                    //core selection without season params
                    url = ConstsCore.web_url + "/v1/display/flooravailability/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&level=" + SalesFilterActivity.level_filter + selectedString + "&top=" + top + "&corefashion=" + corefashion;
                } else {
                    //fashion select with season params
                    url = ConstsCore.web_url + "/v1/display/flooravailability/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&level=" + SalesFilterActivity.level_filter + selectedString + "&top=" + top + "&corefashion=" + corefashion + "&seasongroup=" + seasongroup;
                }
            } else {
                if (coreSelection) {
                    //core selection without season params
                    url = ConstsCore.web_url + "/v1/display/flooravailability/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&top=" + top + "&corefashion=" + corefashion;
                } else {
                    // fashion select with season params
                    url = ConstsCore.web_url + "/v1/display/flooravailability/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&top=" + top + "&corefashion=" + corefashion + "&seasongroup=" + seasongroup;
                }
            }
            Log.e("TAG", "requestFloorAvailabilityApi: "+url );
            final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            floorListView.setVisibility(View.VISIBLE);
                            try {
                                if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                    Reusable_Functions.hDialog();
                                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                    floorListView.removeFooterView(footer);
                                    floorListView.setTag("FOOTER_REMOVE");
                                    if (FloorList.size() == 0) {
                                        floorListView.setVisibility(View.GONE);
                                    }
                                    return;

                                } else if (response.length() == limit) {
                                    for (int i = 0; i < response.length(); i++) {
                                        floorAvailabilityDetails = gson.fromJson(response.get(i).toString(), FloorAvailabilityDetails.class);
                                        FloorList.add(floorAvailabilityDetails);
                                    }
                                    offsetvalue = offsetvalue + 10;
                                    top = top + 10;
                                } else if (response.length() < limit) {
                                    for (int i = 0; i < response.length(); i++) {
                                        floorAvailabilityDetails = gson.fromJson(response.get(i).toString(), FloorAvailabilityDetails.class);
                                        FloorList.add(floorAvailabilityDetails);
                                    }
                                    offsetvalue = offsetvalue + 10;
                                    top = top + 10;
                                }
                                if (lazyScroll.equals("ON")) {
                                    floorAvailabilityAdapter.notifyDataSetChanged();
                                    lazyScroll = "OFF";
                                    footer.setVisibility(View.GONE);
                                } else {
                                    floorAvailabilityAdapter = new FloorAvailabilityAdapter(FloorList, context);
                                    floorListView.setAdapter(floorAvailabilityAdapter);
                                    floor_txtStoreCode.setText(FloorList.get(0).getStoreCode());
                                    floor_txtStoreName.setText(FloorList.get(0).getStoreDescription());
                                }
                                Reusable_Functions.hDialog();
                            } catch (Exception e) {

                                FloorList.clear();
                                floorAvailabilityAdapter.notifyDataSetChanged();
                                floorListView.setVisibility(View.GONE);
                                Reusable_Functions.hDialog();
                                footer.setVisibility(View.GONE);
                                Toast.makeText(context, "Data failed...", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "Server not found...", Toast.LENGTH_SHORT).show();
                            floorListView.removeFooterView(footer);
                            floorListView.setTag("FOOTER_REMOVE");
                            floorListView.setVisibility(View.GONE);
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

//---------------seton Click list listener------------------//

            floorListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                public int VisibleItemCount, TotalItemCount, FirstVisibleItem;

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                    if (FirstVisibleItem + VisibleItemCount == TotalItemCount && scrollState == SCROLL_STATE_IDLE && lazyScroll.equals("OFF")) {

                        if (floorListView.getTag().equals("FOOTER_REMOVE")) {
                            floorListView.addFooterView(footer);
                            floorListView.setTag("FOOTER_ADDED");

                        }
                        footer.setVisibility(View.VISIBLE);
                        lazyScroll = "ON";
                        requestFloorAvailabilityApi(selectedString);
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                    this.FirstVisibleItem = firstVisibleItem;
                    this.VisibleItemCount = visibleItemCount;
                    this.TotalItemCount = totalItemCount;

                }
            });
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
            Reusable_Functions.hDialog();
            floorListView.removeFooterView(footer);
            floorListView.setTag("FOOTER_REMOVE");
        }
    }

    private void initalise() {
        floor_txtStoreCode = (TextView) findViewById(R.id.txtStoreCode);
        floor_txtStoreName = (TextView) findViewById(R.id.txtStoreName);
        floor_BtnBack = (RelativeLayout) findViewById(R.id.fa_imageBtnBack);
        floor_BtnFilter = (RelativeLayout) findViewById(R.id.fa_imgfilter);
        floor_quickFilter = (RelativeLayout) findViewById(R.id.floor_quickFilter);
        quickFilterPopup = (RelativeLayout) findViewById(R.id.quickFilterPopup);
        quickFilter_baseLayout = (RelativeLayout) findViewById(R.id.quickFilter_baseLayout);
        qfDoneLayout = (RelativeLayout) findViewById(R.id.qfDoneLayout);
        quickFilter_BorderLayout = (RelativeLayout) findViewById(R.id.quickFilter_BorderLayout);
        floorListView = (ListView) findViewById(R.id.floorListView);

     //   floor_segmented = (SegmentedGroup) findViewById(R.id.floor_segmented);
      //  floor_core = (RadioButton) findViewById(R.id.floor_core);
      //  floor_fashion = (RadioButton) findViewById(R.id.floor_fashion);

        Toggle_floor_fav = (ToggleButton) findViewById(R.id.toggle_floor_fav);

        checkCurrent = (CheckBox) findViewById(R.id.checkCurrent);
        checkPrevious = (CheckBox) findViewById(R.id.checkPrevious);
        checkOld = (CheckBox) findViewById(R.id.checkOld);
        checkUpcoming = (CheckBox) findViewById(R.id.checkUpcoming);
        Tabview = (TabLayout) findViewById(R.id.tabview);
        Tabview.addTab(Tabview.newTab().setText("Fashion"));
        Tabview.addTab(Tabview.newTab().setText("Core"));

        Tabview.setOnTabSelectedListener(this);
        checkCurrent.setOnClickListener(this);
        checkPrevious.setOnClickListener(this);
        checkOld.setOnClickListener(this);
        checkUpcoming.setOnClickListener(this);

        qfDoneLayout.setOnClickListener(this);
     //   floor_segmented.setOnCheckedChangeListener(this);
        floor_BtnBack.setOnClickListener(this);
        floor_BtnFilter.setOnClickListener(this);
        floor_quickFilter.setOnClickListener(this);
        quickFilter_baseLayout.setOnClickListener(this);
        quickFilter_BorderLayout.setOnClickListener(this);
    }

    private void RetainFromMain_filter() {

        if (corefashion.equals("Fashion")) {
            //floor_fashion.toggle();
            coreSelection = false;
            Tabview.getTabAt(0).select();


        } else {
          // floor_core.toggle();
            toggleClick = true;   //toggle will apply whenever you change position that time it will forcefully call anotherwise it will not.
            coreSelection = true;
            Tabview.getTabAt(1).select();

        }
        baseclick();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fa_imageBtnBack:
                onBackPressed();
                break;
            case R.id.toggle_stock_fav:
                if (Toggle_floor_fav.isChecked()) {
                    Toggle_floor_fav.setChecked(true);
                } else {
                    Toggle_floor_fav.setChecked(false);
                }

                break;
            case R.id.fa_imgfilter:
                Intent intent = new Intent(FloorAvailabilityActivity.this, SalesFilterActivity.class);
                intent.putExtra("checkfrom", "floorAvailability");
                startActivity(intent);
                break;
            case R.id.floor_quickFilter:

                quickFilterPopup.setVisibility(View.VISIBLE);
                break;
            case R.id.quickFilter_baseLayout:
                baseclick();
                quickFilterPopup.setVisibility(View.GONE);
                break;
            case R.id.qfDoneLayout:
                if (checkCurrent.isChecked()) {
                    popupCurrent();
                    floorcheckSeasonGpVal = "Current";

                    quickFilterPopup.setVisibility(View.GONE);

                } else if (checkPrevious.isChecked()) {
                    popupPrevious();
                    floorcheckSeasonGpVal = "Previous";

                    quickFilterPopup.setVisibility(View.GONE);

                } else if (checkOld.isChecked()) {
                    popupOld();
                    floorcheckSeasonGpVal = "Old";

                    quickFilterPopup.setVisibility(View.GONE);

                } else if (checkUpcoming.isChecked()) {
                    popupUpcoming();
                    floorcheckSeasonGpVal = "Upcoming";

                    quickFilterPopup.setVisibility(View.GONE);
                } else {
                    Toast.makeText(this, "Uncheck", Toast.LENGTH_SHORT).show();

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
        }
    }

    private void baseclick() {
        if (floorcheckSeasonGpVal == null) {
            checkCurrent.setChecked(true);
            checkPrevious.setChecked(false);
            checkOld.setChecked(false);
            checkUpcoming.setChecked(false);
        } else {
            switch (floorcheckSeasonGpVal) {
                case "Current":
                    checkCurrent.setChecked(true);
                    checkPrevious.setChecked(false);
                    checkOld.setChecked(false);
                    checkUpcoming.setChecked(false);
                    break;
                case "Previous":
                    checkPrevious.setChecked(true);
                    checkCurrent.setChecked(false);
                    checkOld.setChecked(false);
                    checkUpcoming.setChecked(false);
                    break;
                case "Old":
                    checkOld.setChecked(true);
                    checkCurrent.setChecked(false);
                    checkPrevious.setChecked(false);
                    checkUpcoming.setChecked(false);
                    break;
                case "Upcoming":
                    checkUpcoming.setChecked(true);
                    checkCurrent.setChecked(false);
                    checkOld.setChecked(false);
                    checkPrevious.setChecked(false);
                    break;
            }
        }
    }

    private void popupCurrent() {
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            offsetvalue = 0;
            limit = 10;
            count = 0;
            top = 10;
            seasongroup = "Current";
            FloorList.clear();
            requestFloorAvailabilityApi(selectedString);
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private void popupPrevious() {
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            offsetvalue = 0;
            limit = 10;
            count = 0;
            top = 10;
            seasongroup = "Previous";
            FloorList.clear();
            requestFloorAvailabilityApi(selectedString);
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private void popupOld() {
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            offsetvalue = 0;
            limit = 10;
            count = 0;
            top = 10;
            seasongroup = "Old";
            FloorList.clear();
            requestFloorAvailabilityApi(selectedString);
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private void popupUpcoming() {
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            offsetvalue = 0;
            limit = 10;
            count = 0;
            top = 10;
            seasongroup = "Upcoming";
            FloorList.clear();
            requestFloorAvailabilityApi(selectedString);
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        seasongroup = null;
        corefashion = null;
        floorcheckSeasonGpVal = null;
        seasongroup = "Current";
        corefashion = "Fashion";
        finish();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        int checkedId= Tabview.getSelectedTabPosition();
        Log.e("TAB", "onTabSelected: "+toggleClick );

        if (toggleClick == false) {
            switch (checkedId) {
                case 1 :   //core selection
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            limit = 10;
                            offsetvalue = 0;
                            top = 10;
                            corefashion = "Core";
                            lazyScroll = "OFF";
                            FloorList.clear();
                            floorListView.setVisibility(View.GONE);
                            coreSelection = true;
                            requestFloorAvailabilityApi(selectedString);
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            floorListView.setVisibility(View.GONE);

                        }

                    break;
                case 0 :  // fashion selection
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.hDialog();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            limit = 10;
                            offsetvalue = 0;
                            top = 10;
                            corefashion = "Fashion";
                            lazyScroll = "OFF";
                            FloorList.clear();
                            floorListView.setVisibility(View.GONE);
                            coreSelection = false;
                            requestFloorAvailabilityApi(selectedString);
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            floorListView.setVisibility(View.GONE);

                        }

                    break;
            }
        } else {
            toggleClick = false;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
