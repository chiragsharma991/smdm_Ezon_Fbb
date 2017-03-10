package apsupportapp.aperotechnologies.com.designapp.BestPerformersPromo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.IllegalFormatCodePointException;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.DashBoardActivity;

import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;
import info.hoang8f.android.segmented.SegmentedGroup;


public class BestPerformerActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    TextView Bst_storecode, Bst_txtStoreName, PopPromo, PopPromoU, PopSort, Bst_txtStoreCode;
    RelativeLayout Bst_imageBtnBack, Bst_sort, Bst_imgfilter, BestPromo_footer, SortPopup, BaseLayout;
    RunningPromoListDisplay BestPromoListDisplay;
    private SharedPreferences sharedPreferences;
    String userId, bearertoken;
    String TAG = "BestPerformersActivity";
    private int count = 0;
    private int limit = 10;
    private int offsetvalue = 0;
    private int top = 10;
    private int popPromo = 0;
    int orderbycol = 1;
    Context context = this;
    private RequestQueue queue;
    private Gson gson;
    ListView BestPerformanceListView;
    ArrayList<RunningPromoListDisplay> BestpromoList;
    private int focusposition = 0;
    private boolean userScrolled;
    private BestPromoAdapter bestPromoAdapter;
    private View footer;
    int index = 0;
    int iterations = 0;
    private RadioButton CheckBstSale, CheckBstSaleU;
    private String lazyScroll = "OFF";
    private SegmentedGroup segmentedGroup;
    private RadioButton bestRadio, worstRadio;
    private String orderby = "DESC";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_performer);
        getSupportActionBar().hide();
        initalise();
        //activity_status_track_one git 27dec
        CheckBstSale.setChecked(true);
        SortPopup.setVisibility(View.GONE);
        BestPerformanceListView.setVisibility(View.VISIBLE);
        gson = new Gson();
        BestpromoList = new ArrayList<RunningPromoListDisplay>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Log.e(TAG, "userID and token" + userId + "and this is" + bearertoken);
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        BestPerformanceListView.setTag("FOOTER");
        // bestPromoAdapter = new BestPromoAdapter(BestpromoList,context);
        footer = getLayoutInflater().inflate(R.layout.bestpromo_footer, null);

        BestPerformanceListView.addFooterView(footer);
        // footer.setVisibility(View.GONE);
        // BestPerformanceListView.setAdapter(bestPromoAdapter);

        Reusable_Functions.sDialog(this, "Loading.......");
        requestRunningPromoApi();
    }

    private void requestRunningPromoApi() {

        if (Reusable_Functions.chkStatus(context)) {


            String url = ConstsCore.web_url + "/v1/display/bestworstpromodetails/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&top=" + top + "&orderby=" + orderby + "&orderbycol=" + orderbycol;
            Log.e(TAG, "requestRunningPromoApi: "+url );
            final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.i(TAG, "Best promo : " + " " + response);
                            Log.i(TAG, "response" + "" + response.length());
                            BestPerformanceListView.setVisibility(View.VISIBLE);


                            try {
                                if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                    Reusable_Functions.hDialog();
                                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                    BestPerformanceListView.removeFooterView(footer);
                                    BestPerformanceListView.setTag("FOOTER_REMOVE");
                                    if (BestpromoList.size() == 0) {
                                        BestPerformanceListView.setVisibility(View.GONE);
                                        return;

                                    }

                                } else if (response.length() == limit) {


                                    //BestPerformanceListView.removeFooterView(footer);
                                    //bestPromoAdapter.notifyDataSetChanged();
                                    Log.e(TAG, "promo eql limit");
                                    for (int i = 0; i < response.length(); i++) {

                                        BestPromoListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                        BestpromoList.add(BestPromoListDisplay);

                                    }
                                    offsetvalue = offsetvalue + 10;
                                    top = top + 10;
                                    //  count++;

                                    // requestRunningPromoApi();

                                } else if (response.length() < limit) {
                                    Log.e(TAG, "promo /= limit");
                                    for (int i = 0; i < response.length(); i++) {

                                        BestPromoListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                        BestpromoList.add(BestPromoListDisplay);
                                        offsetvalue = offsetvalue + response.length();
                                        top = top + response.length();

                                    }
                                    BestPerformanceListView.removeFooterView(footer);
                                    BestPerformanceListView.setTag("FOOTER_REMOVE");
                                }
                                footer.setVisibility(View.GONE);
                                if (popPromo == 10) {
                                    bestPromoAdapter = new BestPromoAdapter(BestpromoList, context);
                                    BestPerformanceListView.setAdapter(bestPromoAdapter);
                                    popPromo = 0;

                                } else if (lazyScroll.equals("ON")) {
                                    Log.i(TAG, "notifydatasetchanged" );
                                    bestPromoAdapter.notifyDataSetChanged();
                                    lazyScroll = "OFF";

                                } else {
                                    bestPromoAdapter = new BestPromoAdapter(BestpromoList, context);
                                    BestPerformanceListView.setAdapter(bestPromoAdapter);
                                    Bst_txtStoreCode.setText(BestpromoList.get(0).getStoreCode());
                                    Bst_txtStoreName.setText(BestpromoList.get(0).getStoreDesc());
                                    Log.i(TAG, "setAdapter" );

                                }
                                // BestPromo_footer.setVisibility(View.GONE);
                                Reusable_Functions.hDialog();
                                //  Bst_storecode.setText(BestpromoList.get(0).getStoreCode());
                                // Bst_txtStoreName.setText(BestpromoList.get(0).getStoreDesc());
                                Log.v(TAG, "set text on");
                            } catch (Exception e) {
                                Reusable_Functions.hDialog();
                                footer.setVisibility(View.GONE);
                                BestPerformanceListView.setVisibility(View.GONE);
                                Toast.makeText(context, "data failed...", Toast.LENGTH_SHORT).show();
                                Reusable_Functions.hDialog();
                                BestPerformanceListView.removeFooterView(footer);
                                BestPerformanceListView.setTag("FOOTER_REMOVE");
                                e.printStackTrace();
                                Log.e(TAG, "catch...Error" + e.toString());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "Server not found...", Toast.LENGTH_SHORT).show();
                            Reusable_Functions.hDialog();
                            BestPerformanceListView.removeFooterView(footer);
                            BestPerformanceListView.setTag("FOOTER_REMOVE");
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

            BestPerformanceListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                public int VisibleItemCount, TotalItemCount, FirstVisibleItem;

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                    if (FirstVisibleItem + VisibleItemCount == TotalItemCount && scrollState == SCROLL_STATE_IDLE&&lazyScroll.equals("OFF")) {

                        if (BestPerformanceListView.getTag().equals("FOOTER_REMOVE")) {
                            BestPerformanceListView.addFooterView(footer);
                            BestPerformanceListView.setTag("FOOTER_ADDED");
                            Log.e(TAG, "FOOTER_ADDED: ");

                        }

                        footer.setVisibility(View.VISIBLE);
                        lazyScroll = "ON";
                        requestRunningPromoApi();
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
            BestPerformanceListView.removeFooterView(footer);
            BestPerformanceListView.setTag("FOOTER_REMOVE");

        }
    }

    private void initalise() {
        Bst_storecode = (TextView) findViewById(R.id.txtStoreCode);
        PopPromo = (TextView) findViewById(R.id.popPromo);
        PopPromoU = (TextView) findViewById(R.id.popPromoU);
        Bst_txtStoreCode = (TextView) findViewById(R.id.bst_txtStoreCode);
        Bst_txtStoreName = (TextView) findViewById(R.id.bst_txtStoreName);

        PopSort = (TextView) findViewById(R.id.popSort);
        segmentedGroup = (SegmentedGroup) findViewById(R.id.bestPromo_segmented);
        bestRadio = (RadioButton) findViewById(R.id.bestPromo);
        worstRadio = (RadioButton) findViewById(R.id.worstPromo);
        Log.e(TAG, "---" + bestRadio);
        bestRadio.toggle();

        Bst_imageBtnBack = (RelativeLayout) findViewById(R.id.bst_imageBtnBack);
        Bst_sort = (RelativeLayout) findViewById(R.id.bst_sort);
        BaseLayout = (RelativeLayout) findViewById(R.id.baseLayout);
        Bst_imgfilter = (RelativeLayout) findViewById(R.id.bst_imgfilter);
        BestPerformanceListView = (ListView) findViewById(R.id.bestPerformanceListView);
        SortPopup = (RelativeLayout) findViewById(R.id.sortPopup);

        CheckBstSale = (RadioButton) findViewById(R.id.checkPromoSale);
        CheckBstSaleU = (RadioButton) findViewById(R.id.checkPromoSaleU);
        Bst_imageBtnBack.setOnClickListener(this);
        Bst_sort.setOnClickListener(this);
        Bst_imgfilter.setOnClickListener(this);
        BaseLayout.setOnClickListener(this);
        PopPromo.setOnClickListener(this);
        PopPromoU.setOnClickListener(this);
        PopSort.setOnClickListener(this);
        CheckBstSale.setOnClickListener(this);
        CheckBstSaleU.setOnClickListener(this);
        segmentedGroup.setOnCheckedChangeListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bst_imageBtnBack:
                onBackPressed();
                break;
            case R.id.bst_sort:
                sortFunction();
                break;
            case R.id.baseLayout:
                SortPopup.setVisibility(View.GONE);
                break;
            case R.id.popPromo:
                // popupPromo();
                //SortPopup.setVisibility(View.GONE);
                break;
            case R.id.popPromoU:
                //popupPromoU();
                //SortPopup.setVisibility(View.GONE);
                break;
            case R.id.bst_imgfilter:
                Intent intent = new Intent(this, FilterActivity.class);
                intent.putExtra("from", "bestPromo");
                startActivity(intent);
                break;
            case R.id.checkPromoSale:
                if (CheckBstSale.isChecked()) {
                    popupPromo();
                    CheckBstSaleU.setChecked(false);
                    SortPopup.setVisibility(View.GONE);

                } else {

                    Toast.makeText(this, "Uncheck", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.checkPromoSaleU:

                if (CheckBstSaleU.isChecked()) {
                    popupPromoU();
                    CheckBstSale.setChecked(false);
                    SortPopup.setVisibility(View.GONE);

                } else {
                    Toast.makeText(this, "Uncheck", Toast.LENGTH_SHORT).show();
                }

                break;

        }

    }

    private void popupPromo() {
        Log.e(TAG, "bestPromolist size" + BestpromoList.size());
        popPromo = 10;
        limit = 10;
        offsetvalue = 0;
        top = 10;
        orderbycol = 1;
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.sDialog(this, "Loading.......");
            BestpromoList.clear();
            requestRunningPromoApi();
        } else {
            Reusable_Functions.hDialog();
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
            CheckBstSale.setChecked(false);
            CheckBstSaleU.setChecked(true);
        }

    }

    private void popupPromoU() {
        Log.e(TAG, "bestPromolist size" + BestpromoList.size());
        popPromo = 10;
        limit = 10;
        offsetvalue = 0;
        top = 10;
        orderbycol = 2;
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.sDialog(this, "Loading.......");
            BestpromoList.clear();
            requestRunningPromoApi();
        } else {
            Reusable_Functions.hDialog();
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
            CheckBstSale.setChecked(true);
            CheckBstSaleU.setChecked(false);
        }
    }

    private void sortFunction() {
        SortPopup.setVisibility(View.VISIBLE);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Intent intent=new Intent(context, DashBoardActivity.class);
        //startActivity(intent);
        finish();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        Log.e(TAG, "onCheckedChanged:");

        switch (checkedId) {
            case R.id.bestPromo:
                if (bestRadio.isChecked()) {
                    limit = 10;
                    offsetvalue = 0;
                    top = 10;
                    orderby = "DESC";
                    lazyScroll = "OFF";
                    BestpromoList.clear();
                    // bestPromoAdapter.notifyDataSetChanged();
                    BestPerformanceListView.setVisibility(View.GONE);
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.sDialog(this, "Loading.......");
                        requestRunningPromoApi();
                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        Reusable_Functions.hDialog();
                        BestPerformanceListView.setVisibility(View.GONE);
                    }
                }

                break;
            case R.id.worstPromo:
                if (worstRadio.isChecked()) {
                    limit = 10;
                    offsetvalue = 0;
                    top = 10;
                    orderby = "ASC";
                    lazyScroll = "OFF";
                    BestpromoList.clear();
                    //bestPromoAdapter.notifyDataSetChanged();
                    BestPerformanceListView.setVisibility(View.GONE);
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.sDialog(this, "Loading.......");
                        requestRunningPromoApi();
                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        Reusable_Functions.hDialog();
                        BestPerformanceListView.setVisibility(View.GONE);
                    }
                }


                break;


        }


    }
}
