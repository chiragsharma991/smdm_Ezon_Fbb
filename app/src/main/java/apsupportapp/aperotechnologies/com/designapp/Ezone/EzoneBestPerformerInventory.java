package apsupportapp.aperotechnologies.com.designapp.Ezone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.BestPerformersInventory.BestPerformerInventoryAdapter;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesAnalysisFilter;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesFilterActivity;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;
import info.hoang8f.android.segmented.SegmentedGroup;


public class EzoneBestPerformerInventory extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener,TabLayout.OnTabSelectedListener {

    public  TextView BestInvent_txtStoreCode, BestInvent_txtStoreName;
    private RelativeLayout BestInvent_BtnBack, BestInvent_imgfilter, BestInvent_quickFilter, quickFilterPopup,
            BestQfDoneLayout, BestQuickFilterBorder,rel_store_layout;
    private RunningPromoListDisplay BestInventSizeListDisplay;
    private SharedPreferences sharedPreferences;
    private RadioButton CheckWTD, CheckL4W, CheckSTD;
    private String userId, bearertoken, geoLeveLDesc,storeDescription;
    private TextView Toolbar_title,toggle_txt;
    private String TAG = "";
    private int count = 0;
    private int limit = 10;
    private int offsetvalue = 0;
    private int top = 10;
    private int popPromo = 0;
    private Context context;
    private static String title = "Best";
    private RequestQueue queue;
    private Gson gson;
    private ListView BestInventListview;
    private ArrayList<RunningPromoListDisplay> BestInventList;
    private BestPerformerInventoryAdapter bestPerformerInventoryAdapter;
    private View footer;
    private String lazyScroll = "OFF";
    private static String seasonGroup = "Current";
    private SegmentedGroup BestInvent_segmented;
    private RadioButton BestInvent_core, BestInvent_fashion;
    private ToggleButton Toggle_bestInvent_fav;
    private static String corefashion = "Fashion";
    private static String orderbycol = "sellThruUnits";
    private int level;
    private RelativeLayout Bst_sortInventory;
    private LinearLayout BstInventory_salesU, BstInventory_salesThru, BstInventory_Fwd, BstInventory_coverNsell;
    private RadioButton BstInventory_salesU_chk, BstInventory_salesThru_chk, BstInventory_Fwd_chk, BstInventory_coverNsell_chk;
    private RelativeLayout BaseLayoutInventory;
    private static String checkValueIs = null, checkTimeValueIs = null;
    private static String view = "STD";
    private Switch BestAndWorst;
    private static String orderby = "DESC";
    private CheckBox BestCheckCurrent, BestCheckPrevious, BestCheckOld, BestCheckUpcoming;
    private boolean coreSelection = false;
    public static Activity ezone_bestperoformer;
    private boolean from_filter = false;
    private String selectedString = "", geoLevel2Code, lobId, isMultiStore, value;
    private boolean toggleClick = true;
    private boolean worstToggle = false;
    private RelativeLayout FreshnessIndex_Ez_moreVertical;
    private PopupWindow popupWindow;
    private RadioButton product_radiobtn, location_radiobtn;
    private static int preValue = 1, postValue;  //this is for radio button
    private JsonArrayRequest postRequest;
    private int selectedlevel,filter_level; //select level from filter
    private TabLayout Tabview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLeveLDesc = sharedPreferences.getString("geoLeveLDesc", "");
        geoLevel2Code = sharedPreferences.getString("concept","");
        lobId = sharedPreferences.getString("lobid","");
        isMultiStore = sharedPreferences.getString("isMultiStore","");
        value = sharedPreferences.getString("value","");
//        storeDescription = sharedPreferences.getString("storeDescription","");

        setContentView(R.layout.activity_best_performer_ez_inventory);
        getSupportActionBar().hide();
        context = this;
        TAG = "BestPerformer_Ez_Inventory";
        common_intializeUI();
        intializeUIofEzon();
        Ezon_collection();  // start method for ezon collection

    }


    private void checkfromFilter() {

        if (getIntent().getStringExtra("selectedStringVal") == null)
        {
            from_filter = false;
            level = 7;
            preValue = 1;
            //  BestInvent_fashion.toggle();

        } else if (getIntent().getStringExtra("selectedStringVal") != null)
        {
            selectedString = getIntent().getStringExtra("selectedStringVal");
            selectedlevel = getIntent().getIntExtra("selectedlevelVal", 0);
            from_filter = true;
            setChangeViewBy(selectedlevel);
        }

        show_popup();
        RetainFromMain_filter();
        requestRunningPromoApi(selectedString);
    }


    private void common_intializeUI() {

        ezone_bestperoformer = EzoneBestPerformerInventory.this;

        // toggleClick = true;  // you set toggle on segment button so you have to handle this flag.
        BestInventListview = (ListView) findViewById(R.id.bestInvent_ListView);
        Toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toggle_txt = (TextView) findViewById(R.id.toggle_txt);
        Bst_sortInventory = (RelativeLayout) findViewById(R.id.bst_sortInventory);
        BestInvent_imgfilter = (RelativeLayout) findViewById(R.id.bestInvent_imgfilter);
        BestInvent_BtnBack = (RelativeLayout) findViewById(R.id.bestInvent_BtnBack);
        BestInvent_quickFilter = (RelativeLayout) findViewById(R.id.bestInvent_quickFilter);
        BaseLayoutInventory = (RelativeLayout) findViewById(R.id.baseLayoutInventory);
        BstInventory_salesU = (LinearLayout) findViewById(R.id.bstInventory_salesU);
        BstInventory_salesU_chk = (RadioButton) findViewById(R.id.bstInventory_salesUchk);
        BstInventory_salesThru_chk = (RadioButton) findViewById(R.id.bstInventory_salesThruchk);
        BstInventory_salesThru = (LinearLayout) findViewById(R.id.bstInventory_salesThru);
        BstInventory_Fwd = (LinearLayout) findViewById(R.id.bstInventory_Fwd);
        BstInventory_Fwd_chk = (RadioButton) findViewById(R.id.bstInventory_Fwdchk);
        BstInventory_coverNsell = (LinearLayout) findViewById(R.id.bstInventory_coverNsell);
        BstInventory_coverNsell_chk = (RadioButton) findViewById(R.id.bstInventory_coverNsellchk);
        quickFilterPopup = (RelativeLayout) findViewById(R.id.baseQuickFilterPopup);
        BestQfDoneLayout = (RelativeLayout) findViewById(R.id.bestQfDoneLayout);
        //  BestInvent_core = (RadioButton) findViewById(R.id.bestInvent_core);
        //    BestInvent_fashion = (RadioButton) findViewById(R.id.bestInvent_fashion);
        //   BestInvent_segmented = (SegmentedGroup) findViewById(R.id.bestInvent_segmented);
        BestQuickFilterBorder = (RelativeLayout) findViewById(R.id.bestQuickFilterBorder);
        CheckWTD = (RadioButton) findViewById(R.id.checkWTD);
        CheckL4W = (RadioButton) findViewById(R.id.checkL4W);
        CheckSTD = (RadioButton) findViewById(R.id.checkSTD);
        Tabview = (TabLayout) findViewById(R.id.tabview);
        Tabview.addTab(Tabview.newTab().setText("Fashion"));
        Tabview.addTab(Tabview.newTab().setText("Core"));


        BaseLayoutInventory.setVisibility(View.GONE);
        quickFilterPopup.setVisibility(View.GONE);


        BestAndWorst = (Switch) findViewById(R.id.bestNworstswitch);
        BestInventList = new ArrayList<RunningPromoListDisplay>();
        BestInventListview.setTag("FOOTER");
        footer = getLayoutInflater().inflate(R.layout.bestpromo_footer, null);
        BestInventListview.addFooterView(footer);
        Tabview.setOnTabSelectedListener(this);
        BstInventory_coverNsell.setOnClickListener(this);
        BstInventory_Fwd.setOnClickListener(this);
        BestInvent_quickFilter.setOnClickListener(this);
        BestQfDoneLayout.setOnClickListener(this);
        //  BestInvent_segmented.setOnCheckedChangeListener(this);
        Bst_sortInventory.setOnClickListener(this);
        BaseLayoutInventory.setOnClickListener(this);
        BstInventory_salesU.setOnClickListener(this);
        BstInventory_salesThru.setOnClickListener(this);
        quickFilterPopup.setOnClickListener(this);
        CheckWTD.setOnClickListener(this);
        CheckL4W.setOnClickListener(this);
        BestInvent_BtnBack.setOnClickListener(this);
        CheckSTD.setOnClickListener(this);
        BestQuickFilterBorder.setOnClickListener(this);
        BstInventory_salesThru_chk.setChecked(true);
        BestInvent_imgfilter.setOnClickListener(this);
        BestAndWorst.setOnCheckedChangeListener(this);

        toggle_txt.setText("Best");


        gson = new Gson();
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();


    }


    private void RetainFromMain_filter() {


        if (corefashion.equals("Fashion")) {
            coreSelection = false;
            //BestInvent_fashion.toggle();
            Tabview.getTabAt(0).select();


        } else {
            coreSelection = true;
            // BestInvent_core.toggle();
            Tabview.getTabAt(1).select();

        }

        if (TAG.equals("BestPerformerInventory")) {

            if (title.equals("Worst")) {
                worstToggle = true;
                Toolbar_title.setText("Worst Performers");
                BestAndWorst.setChecked(true);
            }
            baseclick();


        } else {

            baseclickEz_QF();
        }

        sortRetain();
    }

    private void sortRetain() {
        switch (orderbycol.toString()) {
            case "saleTotQty":
                BstInventory_salesU_chk.setChecked(true);
                BstInventory_salesThru_chk.setChecked(false);
                BstInventory_Fwd_chk.setChecked(false);
                BstInventory_coverNsell_chk.setChecked(false);
                break;
            case "sellThruUnits":
                BstInventory_salesU_chk.setChecked(false);
                BstInventory_salesThru_chk.setChecked(true);
                BstInventory_Fwd_chk.setChecked(false);
                BstInventory_coverNsell_chk.setChecked(false);
                break;
            case "fwdWeekCover":
                BstInventory_salesU_chk.setChecked(false);
                BstInventory_salesThru_chk.setChecked(false);
                BstInventory_Fwd_chk.setChecked(true);
                BstInventory_coverNsell_chk.setChecked(false);
                break;
            case "fwdWeekCover,sellThruUnits":
                BstInventory_salesU_chk.setChecked(false);
                BstInventory_salesThru_chk.setChecked(false);
                BstInventory_Fwd_chk.setChecked(false);
                BstInventory_coverNsell_chk.setChecked(true);
                break;
        }
    }

    private void baseclick() {
        if (checkTimeValueIs == null && checkValueIs == null) {
            BestCheckCurrent.setChecked(true);
            BestCheckPrevious.setChecked(false);
            BestCheckOld.setChecked(false);
            BestCheckUpcoming.setChecked(false);
            CheckWTD.setChecked(false);
            CheckL4W.setChecked(false);
            CheckSTD.setChecked(true);


        } else {


//in this checkvalueIs  save the previous done condition params and call to true or false

            switch (checkValueIs.toString()) {
                case "BestCheckCurrent":
                    BestCheckCurrent.setChecked(true);
                    BestCheckPrevious.setChecked(false);
                    BestCheckOld.setChecked(false);
                    BestCheckUpcoming.setChecked(false);
                    break;
                case "BestCheckPrevious":
                    BestCheckCurrent.setChecked(false);
                    BestCheckPrevious.setChecked(true);
                    BestCheckOld.setChecked(false);
                    BestCheckUpcoming.setChecked(false);
                    break;
                case "BestCheckOld":
                    BestCheckCurrent.setChecked(false);
                    BestCheckPrevious.setChecked(false);
                    BestCheckOld.setChecked(true);
                    BestCheckUpcoming.setChecked(false);
                    break;
                case "BestCheckUpcoming":
                    BestCheckCurrent.setChecked(false);
                    BestCheckPrevious.setChecked(false);
                    BestCheckOld.setChecked(false);
                    BestCheckUpcoming.setChecked(true);
                    break;
                default:
                    break;

            }
            switch (checkTimeValueIs.toString()) {
                case "CheckWTD":
                    CheckWTD.setChecked(true);
                    CheckL4W.setChecked(false);
                    CheckSTD.setChecked(false);
                    break;
                case "CheckL4W":
                    CheckWTD.setChecked(false);
                    CheckL4W.setChecked(true);
                    CheckSTD.setChecked(false);
                    break;
                case "CheckSTD":
                    CheckWTD.setChecked(false);
                    CheckL4W.setChecked(false);
                    CheckSTD.setChecked(true);
                    break;
                default:
                    break;


            }
        }


    }

    private void requestRunningPromoApi(final String selectedString) {


        if (Reusable_Functions.chkStatus(context)) {
            String url;
            if (postRequest != null) {
                postRequest.cancel();
                //     bestPerformerInventoryAdapter.notifyDataSetChanged();
            }


            url = get_ez_url();


            postRequest = new JsonArrayRequest(Request.Method.GET, url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            BestInventListview.setVisibility(View.VISIBLE);
                            try {
                                if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                    Reusable_Functions.hDialog();
                                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                    BestInventListview.removeFooterView(footer);
                                    BestInventListview.setTag("FOOTER_REMOVE");
                                    if (BestInventList.size() == 0) {
                                        BestInventListview.setVisibility(View.GONE);
                                        return;
                                    }
                                    return;


                                } else if (response.length() == limit) {

                                    for (int i = 0; i < response.length(); i++) {

                                        BestInventSizeListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                        BestInventList.add(BestInventSizeListDisplay);
                                    }
                                    offsetvalue = offsetvalue + 10;
                                    top = top + 10;

                                } else if (response.length() < limit) {
                                    for (int i = 0; i < response.length(); i++) {

                                        BestInventSizeListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                        BestInventList.add(BestInventSizeListDisplay);

                                    }
                                    offsetvalue = offsetvalue + 10;
                                    top = top + 10;
                                }

                                if (lazyScroll.equals("ON")) {
                                    bestPerformerInventoryAdapter.notifyDataSetChanged();
                                    lazyScroll = "OFF";
                                    footer.setVisibility(View.GONE);
                                }
                                else
                                {
                                    bestPerformerInventoryAdapter = new BestPerformerInventoryAdapter(BestInventList, context, TAG);
                                    BestInventListview.setAdapter(bestPerformerInventoryAdapter);


                                }
                                Reusable_Functions.hDialog();
                            } catch (Exception e) {
                                BestInventList.clear();
                                bestPerformerInventoryAdapter.notifyDataSetChanged();
                                BestInventListview.setVisibility(View.GONE);
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "Data failed...", Toast.LENGTH_SHORT).show();
                                BestInventListview.removeFooterView(footer);
                                BestInventListview.setTag("FOOTER_REMOVE");
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "Server not found...", Toast.LENGTH_SHORT).show();
                            BestInventListview.removeFooterView(footer);
                            BestInventListview.setTag("FOOTER_REMOVE");
                            BestInventListview.setVisibility(View.GONE);
                            String json = null;
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                switch (response.statusCode) {
                                    case 400:
                                        json = new String(response.data);
                                        json = trimMessage(json, "message");
                                        if (json != null) displayMessage(json);
                                        break;
                                }

                                //Additional cases
                            }
                            error.printStackTrace();
                        }

                        public String trimMessage(String json, String key) {
                            String trimmedString = null;
                            try {
                                JSONObject obj = new JSONObject(json);
                                trimmedString = obj.getString(key);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                return null;
                            }
                            return trimmedString;
                        }

                        //Somewhere that has access to a context
                        public void displayMessage(String toastString) {
                            Toast.makeText(context, toastString, Toast.LENGTH_LONG).show();
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

            BestInventListview.setOnScrollListener(new AbsListView.OnScrollListener()
            {
                public int VisibleItemCount, TotalItemCount, FirstVisibleItem;

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                    if (FirstVisibleItem + VisibleItemCount == TotalItemCount && scrollState == SCROLL_STATE_IDLE && lazyScroll.equals("OFF")) {

                        if (BestInventListview.getTag().equals("FOOTER_REMOVE")) {
                            BestInventListview.addFooterView(footer);
                            BestInventListview.setTag("FOOTER_ADDED");

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
            BestInventListview.removeFooterView(footer);
            BestInventListview.setTag("FOOTER_REMOVE");
            Reusable_Functions.hDialog();
        }
    }

    private String getUrl() {

        String url = "";

        if (from_filter) {
            if (coreSelection) {

                //core selection without season params
                if(filter_level != 0)
                {
                    url = ConstsCore.web_url + "/v1/display/inventorybestworstperformersNew/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&orderby=" + orderby + "&orderbycol=" + orderbycol + "&level=" + filter_level + selectedString + "&top=" + top + "&corefashion=" + corefashion + "&view=" + view + "&geoLevel2Code=" + geoLevel2Code + "&lobId="+ lobId;

                }
                else
                {
                    url = ConstsCore.web_url + "/v1/display/inventorybestworstperformersNew/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&orderby=" + orderby + "&orderbycol=" + orderbycol  + selectedString + "&top=" + top + "&corefashion=" + corefashion + "&view=" + view + "&geoLevel2Code=" + geoLevel2Code + "&lobId="+ lobId;

                }
            } else {

                // fashion select with season params
                if(filter_level != 0)
                {
                    url = ConstsCore.web_url + "/v1/display/inventorybestworstperformersNew/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&orderby=" + orderby + "&orderbycol=" + orderbycol + "&level=" + filter_level + selectedString + "&top=" + top + "&corefashion=" + corefashion + "&seasongroup=" + seasonGroup + "&view=" + view + "&geoLevel2Code=" + geoLevel2Code + "&lobId="+ lobId;
                }
                else
                {
                    url = ConstsCore.web_url + "/v1/display/inventorybestworstperformersNew/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&orderby=" + orderby + "&orderbycol=" + orderbycol + selectedString + "&top=" + top + "&corefashion=" + corefashion + "&seasongroup=" + seasonGroup + "&view=" + view + "&geoLevel2Code=" + geoLevel2Code + "&lobId="+ lobId;
                }
            }
        } else {
            if (coreSelection) {

                //core selection without season params

                url = ConstsCore.web_url + "/v1/display/inventorybestworstperformersNew/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&orderby=" + orderby + "&orderbycol=" + orderbycol + "&top=" + top + "&corefashion=" + corefashion + "&view=" + view + "&geoLevel2Code=" + geoLevel2Code + "&lobId="+ lobId;
            } else {

                // fashion select with season params

                url = ConstsCore.web_url + "/v1/display/inventorybestworstperformersNew/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&orderby=" + orderby + "&orderbycol=" + orderbycol + "&top=" + top + "&corefashion=" + corefashion + "&seasongroup=" + seasonGroup + "&view=" + view + "&geoLevel2Code=" + geoLevel2Code + "&lobId="+ lobId;
            }
        }
        return url;
    }


    private void initalise()
    {
        rel_store_layout = (RelativeLayout)findViewById(R.id.rel_store_layout);
        BestInvent_txtStoreCode = (TextView) findViewById(R.id.bestInvent_txtStoreCode);
        BestInvent_txtStoreName = (TextView) findViewById(R.id.bestInvent_txtStoreName);
//        BestInvent_txtStoreCode.setText(storeDescription.trim().substring(0,4));
//        BestInvent_txtStoreName.setText(storeDescription.substring(5));
        if(isMultiStore.equals("Yes"))
        {
            BestInvent_txtStoreCode.setText("Concept : ");
            BestInvent_txtStoreName.setText(value);

        }
        else
        {
            BestInvent_txtStoreCode.setText("Store : ");
            BestInvent_txtStoreName.setText(value);
        }
        //    rel_store_layout.setVisibility(View.VISIBLE);
        Toggle_bestInvent_fav = (ToggleButton) findViewById(R.id.toggle_bestInvent_fav);
        BestCheckCurrent = (CheckBox) findViewById(R.id.bestCheckCurrent);
        BestCheckPrevious = (CheckBox) findViewById(R.id.bestCheckPrevious);
        BestCheckOld = (CheckBox) findViewById(R.id.bestCheckOld);
        BestCheckUpcoming = (CheckBox) findViewById(R.id.bestCheckUpcoming);
        BestCheckCurrent.setOnClickListener(this);
        BestCheckPrevious.setOnClickListener(this);
        BestCheckOld.setOnClickListener(this);
        BestCheckUpcoming.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bestInvent_BtnBack:
                onBackPressed();
                break;
            //pop up >>>
            case R.id.bst_sortInventory:
                sortFunction();
                break;
            case R.id.baseLayoutInventory:
                BaseLayoutInventory.setVisibility(View.GONE);
                break;
            case R.id.bstInventory_salesU:
                from_filter = false;
                salesUPopUp();
                break;
            case R.id.bstInventory_salesThru:
                from_filter = false;
                salesThruPopUp();
                break;
            case R.id.bstInventory_Fwd:
                from_filter = false;
                FwdPopUp();
                break;
            case R.id.bstInventory_coverNsell:
                from_filter = false;
                coverNsellPopUp();
                break;

            //pop up<<<<<

            case R.id.toggle_bestInvent_fav:
                if (Toggle_bestInvent_fav.isChecked()) {
                    Toggle_bestInvent_fav.setChecked(true);
                } else {
                    Toggle_bestInvent_fav.setChecked(false);
                }

                break;
            case R.id.bestInvent_imgfilter:

                Intent intent = new Intent(this, EzoneSalesFilter.class);
                intent.putExtra("checkfrom", "ezonebestPerformers");
                startActivity(intent);
                break;

//
//                }
            //Quick filter>>>
            case R.id.bestInvent_quickFilter:
                filterFunction();
                break;

            // base layout and sorting fuction>>>>>>

            case R.id.baseQuickFilterPopup:

                baseclickEz_QF();
                quickFilterPopup.setVisibility(View.GONE);

                break;

            case R.id.bestQfDoneLayout:
                from_filter = false;

                //Time >>>if you press done then you pass checkTimeValueIs and checkValueIs params
                if (Reusable_Functions.chkStatus(context)) {



                        if (CheckWTD.isChecked())
                        {
                            checkTimeValueIs = "CheckWTD";
                            view = "WTD";
                            popupDoneEz();

                        }
                        else if (CheckL4W.isChecked())
                        {
                            checkTimeValueIs = "CheckL4W";
                            view = "L4W";
                            popupDoneEz();


                        }
                        else if (CheckSTD.isChecked())
                        {
                            checkTimeValueIs = "CheckYTD";
                            view = "YTD";
                            popupDoneEz();
                        }





                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.bestCheckCurrent:
                BestCheckCurrent.setChecked(true);
                BestCheckPrevious.setChecked(false);
                BestCheckOld.setChecked(false);
                BestCheckUpcoming.setChecked(false);
                break;
            case R.id.bestCheckPrevious:
                BestCheckPrevious.setChecked(true);
                BestCheckCurrent.setChecked(false);
                BestCheckOld.setChecked(false);
                BestCheckUpcoming.setChecked(false);
                break;
            case R.id.bestCheckOld:
                BestCheckOld.setChecked(true);
                BestCheckCurrent.setChecked(false);
                BestCheckPrevious.setChecked(false);
                BestCheckUpcoming.setChecked(false);
                break;
            case R.id.bestCheckUpcoming:
                BestCheckUpcoming.setChecked(true);
                BestCheckCurrent.setChecked(false);
                BestCheckOld.setChecked(false);
                BestCheckPrevious.setChecked(false);
                break;
            case R.id.checkWTD:
                CheckWTD.setChecked(true);
                CheckL4W.setChecked(false);
                CheckSTD.setChecked(false);
                break;
            case R.id.checkL4W:
                CheckL4W.setChecked(true);
                CheckSTD.setChecked(false);
                CheckWTD.setChecked(false);
                break;
            case R.id.checkSTD:
                CheckSTD.setChecked(true);
                CheckL4W.setChecked(false);
                CheckWTD.setChecked(false);
                break;


            //Quick filter<<<<<<<

        }

    }


    //Toggle switch>>>>>>>>

    private void BestAndWorstToggle() {

        if (!worstToggle) {
            if (BestAndWorst.isChecked()) {
                limit = 10;
                offsetvalue = 0;
                top = 10;
                orderby = "ASC";
                title = "Worst";
                toggle_txt.setText("Worst");
                Toolbar_title.setText("Worst Performers");
                BestInventList.clear();
                BestInventListview.setVisibility(View.GONE);
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.sDialog(this, "Loading...");
                    requestRunningPromoApi(selectedString);
                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    BestInventListview.setVisibility(View.GONE);
                    Toolbar_title.setText("Worst Performers");


                }
            } else {
                limit = 10;
                offsetvalue = 0;
                top = 10;
                orderby = "DESC";
                title = "Best";
                toggle_txt.setText("Best");
                Toolbar_title.setText("Best Performers");
                BestInventList.clear();
                BestInventListview.setVisibility(View.GONE);
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.sDialog(this, "Loading...");
                    requestRunningPromoApi(selectedString);
                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    BestInventListview.setVisibility(View.GONE);
                    Toolbar_title.setText("Best Performers");
                }
            }

        } else {
            worstToggle = false;
        }
    }

    private void FwdPopUp() {

        if (Reusable_Functions.chkStatus(context)) {
            if (BstInventory_Fwd_chk.isChecked()) {
                BstInventory_salesU_chk.setChecked(false);
                BstInventory_salesThru_chk.setChecked(false);
                BstInventory_Fwd_chk.setChecked(true);
                BstInventory_coverNsell_chk.setChecked(false);
                BaseLayoutInventory.setVisibility(View.GONE);


            } else if (!BstInventory_Fwd_chk.isChecked()) {
                BstInventory_salesU_chk.setChecked(false);
                BstInventory_salesThru_chk.setChecked(false);
                BstInventory_Fwd_chk.setChecked(true);
                BstInventory_coverNsell_chk.setChecked(false);
                orderbycol = "fwdWeekCover";
                BestInventList.clear();
                Reusable_Functions.sDialog(this, "Loading...");
                popPromo = 10;
                limit = 10;
                offsetvalue = 0;
                top = 10;
                requestRunningPromoApi(selectedString);
                BaseLayoutInventory.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private void coverNsellPopUp() {
        if (Reusable_Functions.chkStatus(context)) {

            if (BstInventory_coverNsell_chk.isChecked()) {
                BstInventory_salesU_chk.setChecked(false);
                BstInventory_salesThru_chk.setChecked(false);
                BstInventory_Fwd_chk.setChecked(false);
                BstInventory_coverNsell_chk.setChecked(true);
                BaseLayoutInventory.setVisibility(View.GONE);


            } else if (!BstInventory_coverNsell_chk.isChecked()) {
                BstInventory_salesU_chk.setChecked(false);
                BstInventory_salesThru_chk.setChecked(false);
                BstInventory_Fwd_chk.setChecked(false);
                BstInventory_coverNsell_chk.setChecked(true);
                try {
                    orderbycol = "fwdWeekCover,sellThruUnits";
                    orderbycol =  URLEncoder.encode(orderbycol, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                BestInventList.clear();
                Reusable_Functions.sDialog(this, "Loading...");
                popPromo = 10;
                limit = 10;
                offsetvalue = 0;
                top = 10;
                requestRunningPromoApi(selectedString);
                BaseLayoutInventory.setVisibility(View.GONE);

            }

        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private void salesThruPopUp() {
        if (Reusable_Functions.chkStatus(context)) {

            if (BstInventory_salesThru_chk.isChecked()) {
                BstInventory_salesU_chk.setChecked(false);
                BstInventory_salesThru_chk.setChecked(true);
                BstInventory_Fwd_chk.setChecked(false);
                BstInventory_coverNsell_chk.setChecked(false);
                BaseLayoutInventory.setVisibility(View.GONE);

            } else if (!BstInventory_salesThru_chk.isChecked()) {
                BstInventory_salesU_chk.setChecked(false);
                BstInventory_salesThru_chk.setChecked(true);
                BstInventory_Fwd_chk.setChecked(false);
                BstInventory_coverNsell_chk.setChecked(false);
                orderbycol = "sellThruUnits";
                BestInventList.clear();
                Reusable_Functions.sDialog(this, "Loading...");
                popPromo = 10;
                limit = 10;
                offsetvalue = 0;
                top = 10;
                requestRunningPromoApi(selectedString);
                BaseLayoutInventory.setVisibility(View.GONE);

            }
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private void salesUPopUp() {

        if (Reusable_Functions.chkStatus(context)) {

            if (BstInventory_salesU_chk.isChecked()) {
                BstInventory_salesU_chk.setChecked(true);
                BstInventory_salesThru_chk.setChecked(false);
                BstInventory_Fwd_chk.setChecked(false);
                BstInventory_coverNsell_chk.setChecked(false);
                BaseLayoutInventory.setVisibility(View.GONE);
            } else if (!BstInventory_salesU_chk.isChecked()) {
                BstInventory_salesU_chk.setChecked(true);
                BstInventory_salesThru_chk.setChecked(false);
                BstInventory_Fwd_chk.setChecked(false);
                BstInventory_coverNsell_chk.setChecked(false);
                orderbycol = "saleTotQty";
                BestInventList.clear();
                Reusable_Functions.sDialog(this, "Loading...");
                popPromo = 10;
                limit = 10;
                offsetvalue = 0;
                top = 10;
                requestRunningPromoApi(selectedString);
                BaseLayoutInventory.setVisibility(View.GONE);

            }
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();

        }
    }

    private void sortFunction() {
        BaseLayoutInventory.setVisibility(View.VISIBLE);
    }


    private void popupCurrent() {
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            limit = 10;
            offsetvalue = 0;
            top = 10;
            seasonGroup = "Current";
            BestInventList.clear();
            Reusable_Functions.sDialog(this, "Loading...");
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
            BestInventList.clear();
            Reusable_Functions.sDialog(this, "Loading...");
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
            corefashion = "Core";
            seasonGroup = "Old";
            BestInventList.clear();
            Reusable_Functions.sDialog(this, "Loading...");
            requestRunningPromoApi(selectedString);
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private void CheckTimeDone() {
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            limit = 10;
            offsetvalue = 0;
            top = 10;
            seasonGroup = "All";
            BestInventList.clear();
            Reusable_Functions.sDialog(this, "Loading...");
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
            corefashion = "Core";
            seasonGroup = "Upcoming";
            BestInventList.clear();
            Reusable_Functions.sDialog(this, "Loading...");
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
        orderby = null;
        view = null;
        orderbycol = null;
        corefashion = null;
        seasonGroup = null;
        checkValueIs = null;
        checkTimeValueIs = null;
        title = null;
        orderby = "DESC";
        view = "STD";
        orderbycol = "sellThruUnits";
        preValue = 1;
        corefashion = "Fashion";
        seasonGroup = "Current";
        checkValueIs = null;
        checkTimeValueIs = null;
        title = "Best";
        this.finish();


    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (!toggleClick) {


            switch (checkedId) {
                case R.id.bestInvent_core:
                    if (BestInvent_core.isChecked()) {
                        limit = 10;
                        offsetvalue = 0;
                        top = 10;
                        orderby = "ASC";
                        corefashion = "Core";
                        lazyScroll = "OFF";
                        BestInventList.clear();
                        BestInventListview.setVisibility(View.GONE);
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.sDialog(context, "Loading data...");
                            coreSelection = true;
                            requestRunningPromoApi(selectedString);
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            BestInventListview.setVisibility(View.GONE);

                        }
                    }
                    break;
                case R.id.bestInvent_fashion:
                    if (BestInvent_fashion.isChecked()) {
                        limit = 10;
                        offsetvalue = 0;
                        top = 10;
                        corefashion = "Fashion";
                        orderby = "DESC";
                        lazyScroll = "OFF";
                        BestInventListview.setVisibility(View.GONE);
                        BestInventList.clear();

                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.sDialog(this, "Loading...");
                            coreSelection = false;
                            requestRunningPromoApi(selectedString);
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            BestInventListview.setVisibility(View.GONE);

                        }
                    }

                    break;


            }

        } else {
            toggleClick = false;

        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.bestNworstswitch:
                from_filter = false;
                BestAndWorstToggle();
                break;

        }

    }

    /**
     * start for Ezon>>>
     */


    private void popupDoneEz() {

        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(this, "Loading...");
            quickFilterPopup.setVisibility(View.GONE);
            limit = 10;
            offsetvalue = 0;
            top = 10;
            BestInventList.clear();
            requestRunningPromoApi(selectedString);

        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }


    }

    private void intializeUIofEzon() {

        BestInvent_txtStoreCode = (TextView) findViewById(R.id.bestInvent_txtStoreCode);
        BestInvent_txtStoreName = (TextView) findViewById(R.id.bestInvent_txtStoreName);
//        BestInvent_txtStoreCode.setText(storeDescription.trim().substring(0,4));
//        BestInvent_txtStoreName.setText(storeDescription.substring(5));
        if(isMultiStore.equals("No"))
        {
            BestInvent_txtStoreCode.setText("Store : ");
            BestInvent_txtStoreName.setText(value);
        }
        else
        {
            BestInvent_txtStoreCode.setText("Concept : ");
            BestInvent_txtStoreName.setText(value);
        }
        rel_store_layout = (RelativeLayout)findViewById(R.id.rel_store_layout);
        //  rel_store_layout.setVisibility(View.INVISIBLE);
        FreshnessIndex_Ez_moreVertical = (RelativeLayout) findViewById(R.id.freshnessIndex_Ez_moreVertical);
        FreshnessIndex_Ez_moreVertical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.showAsDropDown(view);
            }
        });
    }


    private void baseclickEz_QF() {

        if (checkTimeValueIs == null) {

            CheckWTD.setChecked(true);
            CheckL4W.setChecked(false);
            CheckSTD.setChecked(false);
            view = "WTD";


        } else {
            switch (checkTimeValueIs.toString()) {

                case "CheckWTD":
                    CheckWTD.setChecked(true);
                    CheckL4W.setChecked(false);
                    CheckSTD.setChecked(false);
                    view = "WTD";

                    break;
                case "CheckL4W":
                    CheckWTD.setChecked(false);
                    CheckL4W.setChecked(true);
                    CheckSTD.setChecked(false);
                    view = "L4W";

                    break;
                case "CheckYTD":
                    CheckWTD.setChecked(false);
                    CheckL4W.setChecked(false);
                    CheckSTD.setChecked(true);
                    view = "YTD";

                    break;
                default:
                    break;


            }
        }
    }

    private void setChangeViewBy(int filter_level) {

        switch (filter_level) {

            case 9:
                preValue = 2;
                break;

            default:
                preValue = 1;
                break;

        }


    }

    private void Ezon_collection() {
        BestInventListview.setVisibility(View.VISIBLE);


        if (Reusable_Functions.chkStatus(context)) {
            limit = 10;
            offsetvalue = 0;
            top = 10;
            Reusable_Functions.sDialog(context, "Loading data...");
            checkfromFilter();
            //requestRunningPromoApi(selectedString);

        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }


    }

    public String get_ez_url() {

        String url;


   /*     if (from_filter) {
            url = ConstsCore.web_url + "/v1/display/inventorybestworstperformersEZ/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&orderby=" + orderby + "&orderbycol=" + orderbycol + "&top=" + top + "&view=" + view + "&level=" + selectedlevel + selectedString;

        } else {
            url = ConstsCore.web_url + "/v1/display/inventorybestworstperformersEZ/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&orderby=" + orderby + "&orderbycol=" + orderbycol + "&top=" + top + "&view=" + view + "&level=" + level;

        }*/

        if (from_filter) {
            if (coreSelection) {

                //core selection without season params

                url = ConstsCore.web_url + "/v1/display/inventorybestworstperformersEZNew/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&orderby=" + orderby + "&orderbycol=" + orderbycol  + selectedString + "&top=" + top + "&view=" + view + "&geoLevel2Code=" + geoLevel2Code + "&lobId="+ lobId;
            } else {

                // fashion select with season params

                url = ConstsCore.web_url + "/v1/display/inventorybestworstperformersEZNew/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&orderby=" + orderby + "&orderbycol=" + orderbycol + selectedString + "&top=" + top + "&view=" + view + "&geoLevel2Code=" + geoLevel2Code + "&lobId="+ lobId;
            }
        } else {
            if (coreSelection) {

                //core selection without season params

                url = ConstsCore.web_url + "/v1/display/inventorybestworstperformersEZNew/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&orderby=" + orderby + "&orderbycol=" + orderbycol + "&top=" + top + "&view=" + view + "&geoLevel2Code=" + geoLevel2Code + "&lobId="+ lobId;
            } else {

                // fashion select with season params

                url = ConstsCore.web_url + "/v1/display/inventorybestworstperformersEZNew/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&orderby=" + orderby + "&orderbycol=" + orderbycol + "&top=" + top + "&view=" + view + "&geoLevel2Code=" + geoLevel2Code + "&lobId="+ lobId;
            }
        }
        return url;
    }

    public void show_popup() {

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.activity_ezon_sorting, null);

        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout location = (LinearLayout) popupView.findViewById(R.id.lin_ez_location);
        LinearLayout product = (LinearLayout) popupView.findViewById(R.id.lin_ez_Product);
        product_radiobtn = (RadioButton) popupView.findViewById(R.id.rb_ez_viewBy_ProductChk);
        location_radiobtn = (RadioButton) popupView.findViewById(R.id.rb_ez_viewBy_LocatnChk);
        if (preValue == 1) {
            product_radiobtn.setChecked(true);
        } else {
            location_radiobtn.setChecked(true);
        }


        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Reusable_Functions.chkStatus(context)) {
                    postValue = 2;
                    level = 9;
                    from_filter = false;
                    location_radiobtn.setChecked(true);
                    product_radiobtn.setChecked(false);
                    popupWindow.dismiss();
                    ViewByFunction();

                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }

            }
        });

        product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Reusable_Functions.chkStatus(context)) {
                    postValue = 1;
                    level = 7;
                    from_filter = false;
                    product_radiobtn.setChecked(true);
                    location_radiobtn.setChecked(false);
                    popupWindow.dismiss();
                    ViewByFunction();

                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
            }
        });


        popupWindow.setOutsideTouchable(true);
        //   popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);  //focus as a side background

        // Removes default black background
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //TODO do sth here on dismiss
                popupWindow.dismiss();


            }
        });


    }

    private void ViewByFunction() {

        if (postValue != preValue) {
            preValue = postValue;

            if (Reusable_Functions.chkStatus(context)) {
                Reusable_Functions.hDialog();
                limit = 10;
                offsetvalue = 0;
                top = 10;
                BestInventList.clear();
                Reusable_Functions.sDialog(this, "Loading...");
                requestRunningPromoApi(selectedString);

            } else {
                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
            }


        }
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        int checkedId= Tabview.getSelectedTabPosition();


        switch (checkedId) {
            case 1 :   //core selection
                from_filter = false;
                limit = 10;
                offsetvalue = 0;
                top = 10;
                orderby = "ASC";
                corefashion = "Core";
                lazyScroll = "OFF";
                BestInventList.clear();
                BestInventListview.setVisibility(View.GONE);
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.sDialog(context, "Loading data...");
                    coreSelection = true;
                    requestRunningPromoApi(selectedString);
                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    BestInventListview.setVisibility(View.GONE);

                }

                break;
            case 0 :  // fashion selection

                from_filter = false;
                limit = 10;
                offsetvalue = 0;
                top = 10;
                corefashion = "Fashion";
                orderby = "DESC";
                lazyScroll = "OFF";
                BestInventListview.setVisibility(View.GONE);
                BestInventList.clear();

                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.sDialog(this, "Loading...");
                    coreSelection = false;
                    requestRunningPromoApi(selectedString);
                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    BestInventListview.setVisibility(View.GONE);

                }


                break;


        }


    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}



