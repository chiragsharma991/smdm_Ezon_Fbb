package apsupportapp.aperotechnologies.com.designapp.SellThruExceptions;

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
import android.widget.LinearLayout;
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
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;
import info.hoang8f.android.segmented.SegmentedGroup;


public class SaleThruInventory extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener,TabLayout.OnTabSelectedListener {

    TextView BestInvent_txtStoreCode, BestInvent_txtStoreName;
    RelativeLayout BestInvent_BtnBack, BestInvent_imgfilter, BestInvent_quickFilter, quickFilterPopup,
            quickFilter_baseLayout, BestQfDoneLayout, BestQuickFilterBorder, SwitchRelay;
    RunningPromoListDisplay BestInventSizeListDisplay;
    private SharedPreferences sharedPreferences;
    CheckBox BestCheckCurrent, BestCheckPrevious, BestCheckOld, BestCheckUpcoming;
    RadioButton CheckWTD, CheckL4W, CheckSTD;
    String userId, bearertoken,storeDescription;
    private int count = 0;
    private int limit = 10;
    private int offsetvalue = 0;
    private int top = 10;
    private int popPromo = 0;
    private RequestQueue queue;
    private Gson gson;
    ListView BestInventListview;
    ArrayList<RunningPromoListDisplay> BestInventList;
    private SaleThruInventoryAdapter bestPerformerInventoryAdapter;
    private View footer;
    private String lazyScroll = "OFF";
    private static String seasonGroup = "Current";
    private SegmentedGroup BestInvent_segmented;
    private RadioButton BestInvent_core, BestInvent_fashion;
    private ToggleButton Toggle_bestInvent_fav;
    private static String corefashion = "Fashion";
    private int orderbycol = 1;
    Context context;
    private RelativeLayout Bst_sortInventory;
    private LinearLayout BstInventory_salesU, BstInventory_salesThru, BstInventory_Fwd, BstInventory_coverNsell;
    private RadioButton BstInventory_salesU_chk, BstInventory_salesThru_chk, BstInventory_Fwd_chk, BstInventory_coverNsell_chk;
    private RelativeLayout BaseLayoutInventory;
    private static String checkValueIs = null, checkTimeValueIs = null;
    private static String view = "STD";
    private TextView Toolbar_title;
    private boolean coreSelection = false, filter_toggleClick = false;
    public static Activity saleThru;
    private boolean from_filter = false;
    private String selectedString = "";
    private TabLayout Tabview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_performer_inventory);
        getSupportActionBar().hide();
        initalise();
        BstInventory_salesU_chk.setChecked(true);
        BaseLayoutInventory.setVisibility(View.GONE);
        BestInventListview.setVisibility(View.VISIBLE);
        Bst_sortInventory.setVisibility(View.GONE);
        gson = new Gson();
        context = this;
        saleThru = this;
        BestInventList = new ArrayList<RunningPromoListDisplay>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        storeDescription = sharedPreferences.getString("storeDescription","");
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        BestInvent_txtStoreCode.setText(storeDescription.trim().substring(0,4));
        BestInvent_txtStoreName.setText(storeDescription.substring(5));
        BestInventListview.setTag("FOOTER");

        Reusable_Functions.hDialog();
        Reusable_Functions.sDialog(context, "Loading.......");

        if (getIntent().getStringExtra("selectedDept") == null) {
            from_filter = false;
            filter_toggleClick = false;
        } else if (getIntent().getStringExtra("selectedDept") != null) {
            selectedString = getIntent().getStringExtra("selectedDept");
            from_filter = true;
            filter_toggleClick = true;

        }
        retainSegmentVal();
        requestRunningPromoApi(selectedString);
        footer = getLayoutInflater().inflate(R.layout.bestpromo_footer, null);
        BestInventListview.addFooterView(footer);
    }


    private void requestRunningPromoApi(final String selectedString) {


        if (Reusable_Functions.chkStatus(context)) {

            String url;

            if (from_filter) {
                if (coreSelection) {

                    //core selection without season params

                    url = ConstsCore.web_url + "/v1/display/sellthruexceptions/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&level=" + SalesFilterActivity.level_filter + selectedString + "&top=" + top + "&corefashion=" + corefashion + "&view=" + view;
                } else {

                    // fashion select with season params

                    url = ConstsCore.web_url + "/v1/display/sellthruexceptions/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&level=" + SalesFilterActivity.level_filter + selectedString + "&top=" + top + "&corefashion=" + corefashion + "&seasongroup=" + seasonGroup + "&view=" + view;
                }
            } else {
                if (coreSelection) {

                    //core selection without season params

                    url = ConstsCore.web_url + "/v1/display/sellthruexceptions/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&top=" + top + "&corefashion=" + corefashion + "&view=" + view;
                } else {

                    // fashion select with season params

                    url = ConstsCore.web_url + "/v1/display/sellthruexceptions/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&top=" + top + "&corefashion=" + corefashion + "&seasongroup=" + seasonGroup + "&view=" + view;
                }
            }
            Log.e("TAG", "requestRunningPromoApi: "+url );

            final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            BestInventListview.setVisibility(View.VISIBLE);


                            try {
                                if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                    Reusable_Functions.hDialog();
                                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                    BestInventListview.removeFooterView(footer);
                                    BestInventListview.setTag("FOOTER_REMOVE");
                                    if (BestInventList.size() == 0) {
                                        BestInventListview.setVisibility(View.GONE);
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
                                    offsetvalue = offsetvalue + response.length();
                                    top = top + response.length();
                                    BestInventListview.removeFooterView(footer);
                                    BestInventListview.setTag("FOOTER_REMOVE");
                                }


                                footer.setVisibility(View.GONE);


                                if (lazyScroll.equals("ON")) {
                                    bestPerformerInventoryAdapter.notifyDataSetChanged();
                                    lazyScroll = "OFF";
                                } else {
                                    bestPerformerInventoryAdapter = new SaleThruInventoryAdapter(BestInventList, context);
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
                                footer.setVisibility(View.GONE);
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
            int socketTimeout = 30000;//30 seconds

            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            postRequest.setRetryPolicy(policy);
            queue.add(postRequest);


//---------------seton Click list listener------------------//

            BestInventListview.setOnScrollListener(new AbsListView.OnScrollListener() {
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
            Reusable_Functions.hDialog();
            BestInventListview.removeFooterView(footer);
            BestInventListview.setTag("FOOTER_REMOVE");

        }
    }


    private void initalise() {

        BestInvent_txtStoreCode = (TextView) findViewById(R.id.bestInvent_txtStoreCode);
        BestInvent_txtStoreName = (TextView) findViewById(R.id.bestInvent_txtStoreName);

        BestInvent_BtnBack = (RelativeLayout) findViewById(R.id.bestInvent_BtnBack);
        BestInvent_imgfilter = (RelativeLayout) findViewById(R.id.bestInvent_imgfilter);
        BestQuickFilterBorder = (RelativeLayout) findViewById(R.id.bestQuickFilterBorder);
        SwitchRelay = (RelativeLayout) findViewById(R.id.switchRelay);
        SwitchRelay.setVisibility(View.GONE);

        Toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        Toolbar_title.setText("Sell Thru Exceptions");


        BestInventListview = (ListView) findViewById(R.id.bestInvent_ListView);

       // BestInvent_segmented = (SegmentedGroup) findViewById(R.id.bestInvent_segmented);
        BestInvent_quickFilter = (RelativeLayout) findViewById(R.id.bestInvent_quickFilter);

      //  BestInvent_core = (RadioButton) findViewById(R.id.bestInvent_core);
      //  BestInvent_fashion = (RadioButton) findViewById(R.id.bestInvent_fashion);
        // BestInvent_fashion.toggle();
        Bst_sortInventory = (RelativeLayout) findViewById(R.id.bst_sortInventory);
        BaseLayoutInventory = (RelativeLayout) findViewById(R.id.baseLayoutInventory);
        BstInventory_salesU = (LinearLayout) findViewById(R.id.bstInventory_salesU);
        BstInventory_salesThru = (LinearLayout) findViewById(R.id.bstInventory_salesThru);
        BstInventory_Fwd = (LinearLayout) findViewById(R.id.bstInventory_Fwd);
        BstInventory_coverNsell = (LinearLayout) findViewById(R.id.bstInventory_coverNsell);

        BstInventory_salesU_chk = (RadioButton) findViewById(R.id.bstInventory_salesUchk);
        BstInventory_salesThru_chk = (RadioButton) findViewById(R.id.bstInventory_salesThruchk);
        BstInventory_Fwd_chk = (RadioButton) findViewById(R.id.bstInventory_Fwdchk);
        BstInventory_coverNsell_chk = (RadioButton) findViewById(R.id.bstInventory_coverNsellchk);


        quickFilterPopup = (RelativeLayout) findViewById(R.id.baseQuickFilterPopup);
        BestQfDoneLayout = (RelativeLayout) findViewById(R.id.bestQfDoneLayout);
        quickFilterPopup.setVisibility(View.GONE);
        Toggle_bestInvent_fav = (ToggleButton) findViewById(R.id.toggle_bestInvent_fav);

        BestCheckCurrent = (CheckBox) findViewById(R.id.bestCheckCurrent);
        BestCheckPrevious = (CheckBox) findViewById(R.id.bestCheckPrevious);
        BestCheckOld = (CheckBox) findViewById(R.id.bestCheckOld);
        BestCheckUpcoming = (CheckBox) findViewById(R.id.bestCheckUpcoming);
        CheckWTD = (RadioButton) findViewById(R.id.checkWTD);
        CheckL4W = (RadioButton) findViewById(R.id.checkL4W);
        CheckSTD = (RadioButton) findViewById(R.id.checkSTD);
        Tabview = (TabLayout) findViewById(R.id.tabview);
        Tabview.addTab(Tabview.newTab().setText("Fashion"));
        Tabview.addTab(Tabview.newTab().setText("Core"));

        Tabview.setOnTabSelectedListener(this);
        // BestInvent_segmented.setOnCheckedChangeListener(this);
        BestInvent_BtnBack.setOnClickListener(this);
        BestInvent_imgfilter.setOnClickListener(this);
        BestInvent_quickFilter.setOnClickListener(this);
        Bst_sortInventory.setOnClickListener(this);
        BaseLayoutInventory.setOnClickListener(this);
        BestQfDoneLayout.setOnClickListener(this);
        BestCheckCurrent.setOnClickListener(this);
        BestCheckPrevious.setOnClickListener(this);
        BestCheckOld.setOnClickListener(this);
        BestCheckUpcoming.setOnClickListener(this);
        BestQuickFilterBorder.setOnClickListener(this);
        quickFilterPopup.setOnClickListener(this);
        CheckWTD.setOnClickListener(this);
        CheckL4W.setOnClickListener(this);
        CheckSTD.setOnClickListener(this);

        BstInventory_salesU.setOnClickListener(this);
        BstInventory_salesThru.setOnClickListener(this);
        BstInventory_Fwd.setOnClickListener(this);
        BstInventory_coverNsell.setOnClickListener(this);
        BestInvent_imgfilter.setOnClickListener(this);
    }

    public void retainSegmentVal() {

        if (corefashion.equals("Fashion")) {
          //  BestInvent_fashion.toggle();
            coreSelection = false;
            Tabview.getTabAt(0).select();

        } else {
          //  BestInvent_core.toggle();
            filter_toggleClick = true;   //toggle will apply whenever you change position that time it will forcefully call anotherwise it will not.
            coreSelection = true;
            Tabview.getTabAt(1).select();
        }
        maintainquickFilterVal();
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
                salesUPopUp();
                break;
            case R.id.bstInventory_salesThru:
                salesThruPopUp();
                break;
            case R.id.bstInventory_Fwd:
                FwdPopUp();
                break;
            case R.id.bstInventory_coverNsell:
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
                Intent intent = new Intent(this, SalesFilterActivity.class);
                intent.putExtra("checkfrom", "sellThruExceptions");
                startActivity(intent);
                break;


            //Quick filter>>>

            case R.id.bestInvent_quickFilter:
                filterFunction();
                break;

            // Base layout>>>>
            case R.id.baseQuickFilterPopup:
                maintainquickFilterVal();

                break;

            case R.id.bestQfDoneLayout:

                if (Reusable_Functions.chkStatus(context)) {

                    //Time >>>

                    if (CheckWTD.isChecked()) {
                        checkTimeValueIs = "CheckWTD";
                        view = "WTD";
                    } else if (CheckL4W.isChecked()) {
                        checkTimeValueIs = "CheckL4W";
                        view = "L4W";


                    } else if (CheckSTD.isChecked()) {
                        checkTimeValueIs = "CheckSTD";
                        view = "STD";
                    }


                    //season group

                    if (BestCheckCurrent.isChecked()) {
                        checkValueIs = "BestCheckCurrent";
                        popupCurrent();
                        quickFilterPopup.setVisibility(View.GONE);

                    } else if (BestCheckPrevious.isChecked()) {
                        checkValueIs = "BestCheckPrevious";
                        popupPrevious();

                        quickFilterPopup.setVisibility(View.GONE);

                    } else if (BestCheckOld.isChecked()) {
                        checkValueIs = "BestCheckOld";
                        popupOld();

                        quickFilterPopup.setVisibility(View.GONE);

                    } else if (BestCheckUpcoming.isChecked()) {
                        checkValueIs = "BestCheckUpcoming";
                        popupUpcoming();

                        quickFilterPopup.setVisibility(View.GONE);

                    } else {

                        CheckTimeDone();
                        quickFilterPopup.setVisibility(View.GONE);

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

    public void maintainquickFilterVal() {
        if (checkTimeValueIs == null && checkValueIs == null) {
            BestCheckCurrent.setChecked(true);
            BestCheckPrevious.setChecked(false);
            BestCheckOld.setChecked(false);
            BestCheckUpcoming.setChecked(false);
            CheckWTD.setChecked(false);
            CheckL4W.setChecked(false);
            CheckSTD.setChecked(true);

        } else {
            switch (checkValueIs) {
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
            switch (checkTimeValueIs) {
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


        quickFilterPopup.setVisibility(View.GONE);

    }

    private void FwdPopUp() {

        if (BstInventory_Fwd_chk.isChecked()) {
            BstInventory_Fwd_chk.setChecked(false);
            BaseLayoutInventory.setVisibility(View.GONE);
        } else {
            BstInventory_salesU_chk.setChecked(false);
            BstInventory_salesThru_chk.setChecked(false);
            BstInventory_Fwd_chk.setChecked(true);
            BstInventory_coverNsell_chk.setChecked(false);
            orderbycol = 3;
            BestInventList.clear();
            Reusable_Functions.sDialog(this, "Loading.......");
            popPromo = 10;
            limit = 10;
            offsetvalue = 0;
            top = 10;
            requestRunningPromoApi(selectedString);
            BaseLayoutInventory.setVisibility(View.GONE);
        }
    }

    private void coverNsellPopUp() {
        if (BstInventory_coverNsell_chk.isChecked()) {
            BstInventory_coverNsell_chk.setChecked(false);
            BaseLayoutInventory.setVisibility(View.GONE);
        } else {
            BstInventory_salesU_chk.setChecked(false);
            BstInventory_salesThru_chk.setChecked(false);
            BstInventory_Fwd_chk.setChecked(false);
            BstInventory_coverNsell_chk.setChecked(true);
            orderbycol = 4;
            BestInventList.clear();
            Reusable_Functions.sDialog(this, "Loading.......");
            popPromo = 10;
            limit = 10;
            offsetvalue = 0;
            top = 10;
            requestRunningPromoApi(selectedString);
            BaseLayoutInventory.setVisibility(View.GONE);

        }

    }

    private void salesThruPopUp() {
        if (BstInventory_salesThru_chk.isChecked()) {
            BstInventory_salesThru_chk.setChecked(false);
            BaseLayoutInventory.setVisibility(View.GONE);
        } else {
            BstInventory_salesU_chk.setChecked(false);
            BstInventory_salesThru_chk.setChecked(true);
            BstInventory_Fwd_chk.setChecked(false);
            BstInventory_coverNsell_chk.setChecked(false);
            orderbycol = 2;
            BestInventList.clear();
            Reusable_Functions.sDialog(this, "Loading.......");
            popPromo = 10;
            limit = 10;
            offsetvalue = 0;
            top = 10;
            requestRunningPromoApi(selectedString);
            BaseLayoutInventory.setVisibility(View.GONE);

        }
    }

    private void salesUPopUp() {
        if (BstInventory_salesU_chk.isChecked()) {

            BstInventory_salesU_chk.setChecked(false);
            BaseLayoutInventory.setVisibility(View.GONE);
        } else {

            BstInventory_salesU_chk.setChecked(true);
            BstInventory_salesThru_chk.setChecked(false);
            BstInventory_Fwd_chk.setChecked(false);
            BstInventory_coverNsell_chk.setChecked(false);
            orderbycol = 1;
            BestInventList.clear();
            Reusable_Functions.sDialog(this, "Loading.......");
            popPromo = 10;
            limit = 10;
            offsetvalue = 0;
            top = 10;
            requestRunningPromoApi(selectedString);
            BaseLayoutInventory.setVisibility(View.GONE);

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
            BestInventList.clear();
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
            corefashion = "Core";
            seasonGroup = "Old";
            BestInventList.clear();
            Reusable_Functions.sDialog(this, "Loading.......");
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
            corefashion = "Core";
            seasonGroup = "Upcoming";
            BestInventList.clear();
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
        view = null;
        seasonGroup = null;
        checkTimeValueIs = null;
        checkValueIs = null;
        corefashion = "Fashion";
        view = "STD";
        seasonGroup = "Current";
        finish();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {



    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        int checkedId= Tabview.getSelectedTabPosition();
        Log.e("TAB", "onTabSelected: "+filter_toggleClick );

        if (filter_toggleClick == false) {
            switch (checkedId) {
                case 1 :   //core selection
                        limit = 10;
                        offsetvalue = 0;
                        top = 10;
                        corefashion = "Core";
                        if (Reusable_Functions.chkStatus(context)) {
                            BestInventList.clear();
                            BestInventListview.setVisibility(View.GONE);
                            Reusable_Functions.sDialog(this, "Loading.......");
                            coreSelection = true;
                            requestRunningPromoApi(selectedString);
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }

                    break;
                case 0 :  // fashion selection

                        limit = 10;
                        offsetvalue = 0;
                        top = 10;
                        corefashion = "Fashion";
                        if (Reusable_Functions.chkStatus(context)) {
                            BestInventList.clear();
                            BestInventListview.setVisibility(View.GONE);
                            Reusable_Functions.sDialog(this, "Loading.......");
                            coreSelection = false;
                            requestRunningPromoApi(selectedString);
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();

                        }

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
}
