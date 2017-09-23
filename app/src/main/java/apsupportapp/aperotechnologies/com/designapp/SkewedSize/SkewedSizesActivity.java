package apsupportapp.aperotechnologies.com.designapp.SkewedSize;


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
import android.widget.ImageView;
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
import apsupportapp.aperotechnologies.com.designapp.model.SkewedSizeListDisplay;
import info.hoang8f.android.segmented.SegmentedGroup;



public class SkewedSizesActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener,TabLayout.OnTabSelectedListener {

    TextView Skewed_txtStoreCode, Skewed_txtStoreName;
    RelativeLayout Skewed_BtnBack, sk_imgfilter, sk_quickFilter, quickFilterPopup, quickFilter_baseLayout, qfDoneLayout;
    SkewedSizeListDisplay skewedSizeListDisplay;
    private SharedPreferences sharedPreferences;
    CheckBox checkCurrent, checkPrevious, checkOld, checkUpcoming;
    RadioButton Skewed_checkWTD, Skewed_checkL4W, Skewed_checkSTD;
    String userId, bearertoken,storeDescription,geoLevel2Code,lobId;
    private int count = 0;
    private int limit = 10;
    private int offsetvalue = 0;
    private int top = 10;
    private int popPromo = 0;
    Context context = this;
    private RequestQueue queue;
    private Gson gson;
    ListView SkewedSizeListview;
    ArrayList<SkewedSizeListDisplay> SkewedSizeList;
    private int focusposition = 0;
    private boolean userScrolled;
    private SkewedSizeAdapter SkewedSizeAdapter;
    private View footer;
    private String lazyScroll = "OFF";
    private static String seasonGroup = "Current";
    private SegmentedGroup Skewed_segmented;
    private RadioButton Skewed_core, Skewed_fashion;
    private ToggleButton Toggle_skewed_fav;
    private static String corefashion = "Fashion";
    private ImageView Skewed_quickFilter;
    private static String qfButton = "OFF";
    private boolean coreSelection = false;
    private static String checkTimeValueIs = null;
    private static String view = "STD";
    public static Activity SkewedSizes;
    private boolean from_filter = false, filter_toggleClick = false;
    private String selectedString = "", isMultiStore, value;
    private TabLayout Tabview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skewed_sizes);
        getSupportActionBar().hide();

        gson = new Gson();
        SkewedSizes = this;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLevel2Code = sharedPreferences.getString("concept","");
        lobId = sharedPreferences.getString("lobid","");
        isMultiStore = sharedPreferences.getString("isMultiStore","");
        value = sharedPreferences.getString("value","");
//        storeDescription = sharedPreferences.getString("storeDescription","");
        Log.e("isMultiStore in skewed sizes "," "+ isMultiStore);
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
//        Skewed_txtStoreCode.setText(storeDescription.trim().substring(0,4));
//        Skewed_txtStoreName.setText(storeDescription.substring(5));
        initalise();
        SkewedSizeListview.setVisibility(View.VISIBLE);
        SkewedSizeList = new ArrayList<SkewedSizeListDisplay>();
        SkewedSizeListview.setTag("FOOTER");

        Reusable_Functions.sDialog(this, "Loading.......");
        if (getIntent().getStringExtra("selectedDept") == null) {
            from_filter = false;
            filter_toggleClick = false;

        } else if (getIntent().getStringExtra("selectedDept") != null) {
            selectedString = getIntent().getStringExtra("selectedDept");
            from_filter = true;
            filter_toggleClick = true;
        }
        Log.e("TAG", "Selected string: "+selectedString+" "+from_filter );
        retainValuesFilter();
        requestRunningPromoApi(selectedString);
        footer = getLayoutInflater().inflate(R.layout.bestpromo_footer, null);
        SkewedSizeListview.addFooterView(footer);


    }

    public void
    retainValuesFilter() {
      //  filter_toggleClick = true;
        if (corefashion.equals("Fashion")) {
          //  Skewed_core.toggle();
            coreSelection = false;
            Tabview.getTabAt(0).select();


        } else {
          //  Skewed_fashion.toggle();
            coreSelection = true;
            Tabview.getTabAt(1).select();


        }
        quickFilterValCheckClick();
    }


    private void requestRunningPromoApi(final String selectedString) {

        if (Reusable_Functions.chkStatus(context)) {

            Log.e("TAG", "From filter: "+from_filter );
            String url;
            if (from_filter) {
                if (coreSelection) {

                    //core selection without season params

                    url = ConstsCore.web_url + "/v1/display/skewedsizesNew/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&level=" + SalesFilterActivity.level_filter + selectedString + "&top=" + top + "&corefashion=" + corefashion + "&view=" + view+"&geoLevel2Code=" +geoLevel2Code + "&lobId="+ lobId;
                } else {

                    // fashion select with season params

                    url = ConstsCore.web_url + "/v1/display/skewedsizesNew/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&level=" + SalesFilterActivity.level_filter + selectedString + "&top=" + top + "&corefashion=" + corefashion + "&seasongroup=" + seasonGroup + "&view=" + view+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
                }
            } else {
                if (coreSelection) {

                    //core selection without season params
                    url = ConstsCore.web_url + "/v1/display/skewedsizesNew/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&top=" + top + "&corefashion=" + corefashion + "&view=" + view+"&geoLevel2Code=" +geoLevel2Code + "&lobId="+ lobId;
                } else {

                    // fashion select with season params
                    url = ConstsCore.web_url + "/v1/display/skewedsizesNew/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&top=" + top + "&corefashion=" + corefashion + "&seasongroup=" + seasonGroup + "&view=" + view+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;
                }
            }
            Log.e("url:",""+url);
            final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            SkewedSizeListview.setVisibility(View.VISIBLE);
                            try {
                                if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                    Reusable_Functions.hDialog();
                                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                    SkewedSizeListview.removeFooterView(footer);
                                    SkewedSizeListview.setTag("FOOTER_REMOVE");
                                    footer.setVisibility(View.GONE);
                                    if (SkewedSizeList.size() == 0) {
                                        SkewedSizeListview.setVisibility(View.GONE);
                                    }
                                    return;

                                } else if (response.length() == limit) {
                                    for (int i = 0; i < response.length(); i++) {
                                        skewedSizeListDisplay = gson.fromJson(response.get(i).toString(), SkewedSizeListDisplay.class);
                                        SkewedSizeList.add(skewedSizeListDisplay);
                                    }
                                    offsetvalue = offsetvalue + 10;
                                    top = top + 10;

                                } else if (response.length() < limit) {
                                    for (int i = 0; i < response.length(); i++) {
                                        skewedSizeListDisplay = gson.fromJson(response.get(i).toString(), SkewedSizeListDisplay.class);
                                        SkewedSizeList.add(skewedSizeListDisplay);
                                    }
                                    offsetvalue = offsetvalue + response.length();
                                    top = top + response.length();
                                }
                                footer.setVisibility(View.GONE);
                                if (lazyScroll.equals("ON")) {
                                    SkewedSizeAdapter.notifyDataSetChanged();
                                    lazyScroll = "OFF";
                                } else {
                                    SkewedSizeAdapter = new SkewedSizeAdapter(SkewedSizeList, context, getResources());
                                    SkewedSizeListview.setAdapter(SkewedSizeAdapter);
                                }

                                Reusable_Functions.hDialog();
                            } catch (Exception e) {
                                footer.setVisibility(View.GONE);
                                SkewedSizeListview.setVisibility(View.GONE);
                                SkewedSizeListview.setVisibility(View.GONE);
                                Toast.makeText(context, "Data failed...", Toast.LENGTH_SHORT).show();
                                Reusable_Functions.hDialog();
                                SkewedSizeListview.removeFooterView(footer);
                                SkewedSizeListview.setTag("FOOTER_REMOVE");
                                e.printStackTrace();

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "Server not found...", Toast.LENGTH_SHORT).show();
                            SkewedSizeListview.removeFooterView(footer);
                            SkewedSizeListview.setTag("FOOTER_REMOVE");
                            SkewedSizeListview.setVisibility(View.GONE);
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

            SkewedSizeListview.setOnScrollListener(new AbsListView.OnScrollListener() {

                public int VisibleItemCount, TotalItemCount, FirstVisibleItem;

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                    if (FirstVisibleItem + VisibleItemCount == TotalItemCount && scrollState == SCROLL_STATE_IDLE && lazyScroll.equals("OFF")) {

                        if (SkewedSizeListview.getTag().equals("FOOTER_REMOVE")) {
                            SkewedSizeListview.addFooterView(footer);
                            SkewedSizeListview.setTag("FOOTER_ADDED");
                        }
                        footer.setVisibility(View.VISIBLE);
                        lazyScroll = "ON";
                        requestRunningPromoApi(selectedString);
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
            SkewedSizeListview.removeFooterView(footer);
            SkewedSizeListview.setTag("FOOTER_REMOVE");
        }
    }


    private void initalise() {

        Skewed_txtStoreCode = (TextView) findViewById(R.id.txtStoreCode);
        Skewed_txtStoreName = (TextView) findViewById(R.id.txtStoreName);
        if(isMultiStore.equals("Yes"))
        {
            Skewed_txtStoreCode.setText("Concept : ");
            Skewed_txtStoreName.setText(value);

        }
        else
        {
            Skewed_txtStoreCode.setText("Store : ");
            Skewed_txtStoreName.setText(value);
        }
        Skewed_BtnBack = (RelativeLayout) findViewById(R.id.skewed_BtnBack);
        sk_imgfilter = (RelativeLayout) findViewById(R.id.sk_imgfilter);
        SkewedSizeListview = (ListView) findViewById(R.id.skewedListView);
        //Skewed_segmented = (SegmentedGroup) findViewById(R.id.skewed_segmented);
        sk_quickFilter = (RelativeLayout) findViewById(R.id.sk_quickFilter);
       // Skewed_core = (RadioButton) findViewById(R.id.skewed_core);
       // Skewed_fashion = (RadioButton) findViewById(R.id.skewed_fashion);
        quickFilterPopup = (RelativeLayout) findViewById(R.id.quickFilterPopup);
        quickFilter_baseLayout = (RelativeLayout) findViewById(R.id.quickFilter_baseLayout);
        qfDoneLayout = (RelativeLayout) findViewById(R.id.qfDoneLayout);
        quickFilterPopup.setVisibility(View.GONE);
        Toggle_skewed_fav = (ToggleButton) findViewById(R.id.toggle_skewed_fav);
        checkCurrent = (CheckBox) findViewById(R.id.checkCurrent);
        checkPrevious = (CheckBox) findViewById(R.id.checkPrevious);
        checkOld = (CheckBox) findViewById(R.id.checkOld);
        checkUpcoming = (CheckBox) findViewById(R.id.checkUpcoming);
        Skewed_checkWTD = (RadioButton) findViewById(R.id.skewed_checkWTD);
        Skewed_checkL4W = (RadioButton) findViewById(R.id.skewed_checkL4W);
        Skewed_checkSTD = (RadioButton) findViewById(R.id.skewed_checkSTD);
        Tabview = (TabLayout) findViewById(R.id.tabview);
        Tabview.addTab(Tabview.newTab().setText("Fashion"));
        Tabview.addTab(Tabview.newTab().setText("Core"));

        Tabview.setOnTabSelectedListener(this);
     //   Skewed_segmented.setOnCheckedChangeListener(this);
        Skewed_BtnBack.setOnClickListener(this);
        sk_imgfilter.setOnClickListener(this);
        sk_quickFilter.setOnClickListener(this);
        quickFilter_baseLayout.setOnClickListener(this);
        qfDoneLayout.setOnClickListener(this);
        checkCurrent.setOnClickListener(this);
        checkPrevious.setOnClickListener(this);
        checkOld.setOnClickListener(this);
        checkUpcoming.setOnClickListener(this);
        Skewed_checkWTD.setOnClickListener(this);
        Skewed_checkL4W.setOnClickListener(this);
        Skewed_checkSTD.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.skewed_BtnBack:
                onBackPressed();
                break;
            case R.id.toggle_skewed_fav:
                if (Toggle_skewed_fav.isChecked()) {
                    Toggle_skewed_fav.setChecked(true);
                } else {
                    Toggle_skewed_fav.setChecked(false);
                }
                break;
            case R.id.sk_imgfilter:
                Intent intent = new Intent(SkewedSizesActivity.this, SalesFilterActivity.class);
                intent.putExtra("checkfrom", "skewedSize");
                startActivity(intent);
                break;
            case R.id.sk_quickFilter:
                filterFunction();
                break;

            // base layout selections>>
            case R.id.quickFilter_baseLayout:
                quickFilterValCheckClick();
                break;

            case R.id.qfDoneLayout:

                from_filter = false;

                if (Reusable_Functions.chkStatus(context)) {
                    if (Skewed_checkWTD.isChecked()) {
                        checkTimeValueIs = "CheckWTD";
                        view = "WTD";
                    } else if (Skewed_checkL4W.isChecked()) {
                        checkTimeValueIs = "CheckL4W";
                        view = "L4W";
                    } else if (Skewed_checkSTD.isChecked()) {
                        checkTimeValueIs = "CheckSTD";
                        view = "STD";
                    }
                    if (checkCurrent.isChecked()) {
                        popupCurrent();
                        checkPrevious.setChecked(false);
                        checkOld.setChecked(false);
                        qfButton = "checkCurrent";
                        checkUpcoming.setChecked(false);
                        quickFilterPopup.setVisibility(View.GONE);

                    } else if (checkPrevious.isChecked()) {
                        popupPrevious();
                        checkCurrent.setChecked(false);
                        checkOld.setChecked(false);
                        qfButton = "checkPrevious";
                        checkUpcoming.setChecked(false);
                        quickFilterPopup.setVisibility(View.GONE);

                    } else if (checkOld.isChecked()) {
                        popupOld();
                        checkPrevious.setChecked(false);
                        checkCurrent.setChecked(false);
                        qfButton = "checkOld";
                        checkUpcoming.setChecked(false);
                        quickFilterPopup.setVisibility(View.GONE);

                    } else if (checkUpcoming.isChecked()) {
                        popupUpcoming();
                        checkCurrent.setChecked(false);
                        checkPrevious.setChecked(false);
                        qfButton = "checkUpcoming";
                        checkOld.setChecked(false);
                        quickFilterPopup.setVisibility(View.GONE);

                        //Time check>>>>>>>


                    } else {
                        Toast.makeText(this, "Uncheck", Toast.LENGTH_SHORT).show();

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

            case R.id.skewed_checkWTD:
                Skewed_checkWTD.setChecked(true);
                Skewed_checkL4W.setChecked(false);
                Skewed_checkSTD.setChecked(false);
                break;
            case R.id.skewed_checkL4W:
                Skewed_checkWTD.setChecked(false);
                Skewed_checkL4W.setChecked(true);
                Skewed_checkSTD.setChecked(false);
                break;
            case R.id.skewed_checkSTD:
                Skewed_checkWTD.setChecked(false);
                Skewed_checkL4W.setChecked(false);
                Skewed_checkSTD.setChecked(true);
                break;
        }
    }

    private void quickFilterValCheckClick() {


        if (qfButton.equals("OFF") && checkTimeValueIs == null) {
            checkCurrent.setChecked(true);
            checkUpcoming.setChecked(false);
            checkOld.setChecked(false);
            checkPrevious.setChecked(false);
            Skewed_checkWTD.setChecked(false);
            Skewed_checkL4W.setChecked(false);
            Skewed_checkSTD.setChecked(true);
        } else {
            switch (qfButton) {

                case "checkCurrent":
                    checkCurrent.setChecked(true);
                    checkPrevious.setChecked(false);
                    checkOld.setChecked(false);
                    checkUpcoming.setChecked(false);
                    break;
                case "checkPrevious":
                    checkPrevious.setChecked(true);
                    checkCurrent.setChecked(false);
                    checkOld.setChecked(false);
                    checkUpcoming.setChecked(false);
                    break;
                case "checkOld":
                    checkOld.setChecked(true);
                    checkPrevious.setChecked(false);
                    checkCurrent.setChecked(false);
                    checkUpcoming.setChecked(false);
                    break;
                case "checkUpcoming":
                    checkUpcoming.setChecked(true);
                    checkCurrent.setChecked(false);
                    checkPrevious.setChecked(false);
                    checkOld.setChecked(false);
                    break;

            }

            switch (checkTimeValueIs) {
                case "CheckWTD":
                    Skewed_checkWTD.setChecked(true);
                    Skewed_checkL4W.setChecked(false);
                    Skewed_checkSTD.setChecked(false);
                    break;
                case "CheckL4W":
                    Skewed_checkWTD.setChecked(false);
                    Skewed_checkL4W.setChecked(true);
                    Skewed_checkSTD.setChecked(false);
                    break;
                case "CheckSTD":
                    Skewed_checkWTD.setChecked(false);
                    Skewed_checkL4W.setChecked(false);
                    Skewed_checkSTD.setChecked(true);
                    break;
                default:
                    break;


            }
        }
        quickFilterPopup.setVisibility(View.GONE);

    }

    private void popupCurrent() {
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            limit = 10;
            offsetvalue = 0;
            top = 10;
            seasonGroup = "Current";
            SkewedSizeList.clear();
            Reusable_Functions.sDialog(this, "Loading.......");
            requestRunningPromoApi(selectedString);

        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private void popupPrevious() {
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            limit = 10;
            offsetvalue = 0;
            top = 10;
            seasonGroup = "Previous";
            SkewedSizeList.clear();
            Reusable_Functions.sDialog(this, "Loading.......");
            requestRunningPromoApi(selectedString);

        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private void popupOld() {
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            limit = 10;
            offsetvalue = 0;
            top = 10;
            seasonGroup = "Old";
            SkewedSizeList.clear();
            Reusable_Functions.sDialog(this, "Loading.......");
            requestRunningPromoApi(selectedString);
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private void popupUpcoming() {
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            limit = 10;
            offsetvalue = 0;
            top = 10;
            seasonGroup = "Upcoming";
            SkewedSizeList.clear();
            Reusable_Functions.sDialog(this, "Loading.......");
            requestRunningPromoApi(selectedString);
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private void filterFunction() {
        quickFilterPopup.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        corefashion = null;
        seasonGroup = null;
        view = null;
        checkTimeValueIs = null;
        qfButton = null;
        qfButton = "OFF";
        corefashion = "Fashion";
        seasonGroup = "Current";
        view = "STD";
        finish();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        //change core fashion according to chnage in xml layout
/*
        if (!filter_toggleClick) {
            switch (checkedId) {
                case R.id.skewed_core:
                    if (Skewed_core.isChecked()) {
                        limit = 10;
                        offsetvalue = 0;
                        top = 10;
                        corefashion = "Fashion";
                        SkewedSizeList.clear();
                        SkewedSizeListview.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.sDialog(this, "Loading.......");
                            coreSelection = false;
                            requestRunningPromoApi(selectedString);
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            Reusable_Functions.hDialog();
                            SkewedSizeListview.setVisibility(View.GONE);
                        }
                    }
                    break;
                case R.id.skewed_fashion:
                    if (Skewed_fashion.isChecked()) {
                        limit = 10;
                        offsetvalue = 0;
                        top = 10;
                        corefashion = "Core";
                        SkewedSizeList.clear();
                        SkewedSizeListview.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.sDialog(this, "Loading.......");
                            coreSelection = true;
                            requestRunningPromoApi(selectedString);
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            Reusable_Functions.hDialog();
                            SkewedSizeListview.setVisibility(View.GONE);

                        }
                    }
                    break;


            }
        } else {
            filter_toggleClick = false;
        }*/

    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int checkedId= Tabview.getSelectedTabPosition();
        Log.e("TAB", "onTabSelected: "+checkedId );

        if (!filter_toggleClick)   // from filter is use when you retain tab button that time it will call .
        {
            from_filter = false;

            switch (checkedId) {

            case 1 :   //core selection
                limit = 10;
                offsetvalue = 0;
                top = 10;
                corefashion = "Core";
                SkewedSizeList.clear();
                SkewedSizeListview.setVisibility(View.GONE);
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.sDialog(this, "Loading.......");
                    coreSelection = true;
                    requestRunningPromoApi(selectedString);
                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    Reusable_Functions.hDialog();
                    SkewedSizeListview.setVisibility(View.GONE);

                }
                break;

            case 0 :  // fashion selection
                limit = 10;
                offsetvalue = 0;
                top = 10;
                corefashion = "Fashion";
                SkewedSizeList.clear();
                SkewedSizeListview.setVisibility(View.GONE);
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.sDialog(this, "Loading.......");
                    coreSelection = false;
                    requestRunningPromoApi(selectedString);
                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    Reusable_Functions.hDialog();
                    SkewedSizeListview.setVisibility(View.GONE);
                }
                break;


        }
        }else{  filter_toggleClick = false;
        }

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
