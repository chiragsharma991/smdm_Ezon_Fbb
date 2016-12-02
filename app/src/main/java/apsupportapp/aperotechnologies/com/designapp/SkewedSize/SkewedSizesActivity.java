package apsupportapp.aperotechnologies.com.designapp.SkewedSize;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import apsupportapp.aperotechnologies.com.designapp.DashBoardActivity;
import apsupportapp.aperotechnologies.com.designapp.FreshnessIndex.InventoryFilterActivity;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;

import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;
import info.hoang8f.android.segmented.SegmentedGroup;




public class SkewedSizesActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    TextView Skewed_txtStoreCode,Skewed_txtStoreName;
    RelativeLayout Skewed_BtnBack,sk_imgfilter,sk_quickFilter,quickFilterPopup, quickFilter_baseLayout,qfDoneLayout;
    RunningPromoListDisplay SkewedSizeListDisplay;
    private SharedPreferences sharedPreferences;
    CheckBox checkCurrent,checkPrevious,checkOld,checkUpcoming;
    String userId, bearertoken;
    String TAG = "SkewedSizesActivity";
    private int count = 0;
    private int limit = 10;
    private int offsetvalue = 0;
    private int top = 10;
    private int popPromo=0;
    Context context = this;
    private RequestQueue queue;
    private Gson gson;
    ListView SkewedSizeListview;
    ArrayList<RunningPromoListDisplay>SkewedSizeList;
    private int focusposition = 0;
    private boolean userScrolled;
    private SkewedSizeAdapter SkewedSizeAdapter;
    private View footer;
    private String lazyScroll="OFF",seasonGroup = "All";
    private SegmentedGroup Skewed_segmented;
    private RadioButton Skewed_core,Skewed_fashion;
    private ToggleButton Toggle_skewed_fav;
    private String corefashion="Core";
    private ImageView Skewed_quickFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skewed_sizes);
        getSupportActionBar().hide();
        initalise();
        seasonGroup = "All";
        gson = new Gson();
        SkewedSizeList = new ArrayList<RunningPromoListDisplay>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Log.e(TAG, "userID and token" + userId + "and this is" + bearertoken);
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();

        requestRunningPromoApi();
        Reusable_Functions.sDialog(this, "Loading.......");
        // bestPromoAdapter = new BestPromoAdapter(BestpromoList,context);
        footer = getLayoutInflater().inflate(R.layout.bestpromo_footer,null);

        SkewedSizeListview.addFooterView(footer);
        // footer.setVisibility(View.GONE);
        // BestPerformanceListView.setAdapter(bestPromoAdapter);




    }

    private void requestRunningPromoApi() {

        String url = ConstsCore.web_url + "/v1/display/skewedsizes/" +userId + "?offset=" +offsetvalue + "&limit=" +limit+"&top=" +top+"&corefashion=" +corefashion +"&seasongroup="+seasonGroup;

        Log.e(TAG,"URL"+url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, "Skewed Option : " + " " + response);
                        Log.i(TAG, "response" + "" + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                footer.setVisibility(View.GONE);

                            } else if (response.length() == limit) {


                                Log.e(TAG, "Top eql limit");
                                for (int i = 0; i < response.length(); i++) {

                                    SkewedSizeListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                    SkewedSizeList.add(SkewedSizeListDisplay);

                                }
                                offsetvalue =offsetvalue+10;
                                top =top+10;
                                //  count++;

                                // requestRunningPromoApi();

                            }

                            else if (response.length() < limit) {
                                Log.e(TAG, "promo /= limit");
                                for (int i = 0; i < response.length(); i++) {

                                    SkewedSizeListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                    SkewedSizeList.add(SkewedSizeListDisplay);
                                    offsetvalue =offsetvalue+response.length();
                                    top =top+response.length();

                                }
                            }



                            footer.setVisibility(View.GONE);
                           /* if(popPromo==10)
                            {
                                topOptionAdapter = new TopOptionAdapter(TopOptionList,context);
                                TopOptionListView.setAdapter(topOptionAdapter);
                                popPromo=0;

                            }*/

                            if(lazyScroll.equals("ON")){
                               SkewedSizeAdapter.notifyDataSetChanged();
                                lazyScroll="OFF";
                            }else
                            {
                                SkewedSizeAdapter = new SkewedSizeAdapter(SkewedSizeList,context);
                                SkewedSizeListview.setAdapter(SkewedSizeAdapter);


                            }

                            Skewed_txtStoreCode.setText(SkewedSizeList.get(0).getStoreCode());
                            Skewed_txtStoreName.setText(SkewedSizeList.get(0).getStoreDesc());

                            Reusable_Functions.hDialog();






                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            footer.setVisibility(View.GONE);
                            Toast.makeText(context, "no data found in catch"+e.toString(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            Log.e(TAG, "catch...Error" +e.toString());
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


//---------------seton Click list listener------------------//

        SkewedSizeListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            public int VisibleItemCount,TotalItemCount,FirstVisibleItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if ( FirstVisibleItem + VisibleItemCount == TotalItemCount && scrollState==SCROLL_STATE_IDLE) {


                    footer.setVisibility(View.VISIBLE);

                    lazyScroll="ON";
                    requestRunningPromoApi();
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                this.FirstVisibleItem=firstVisibleItem;
                this.VisibleItemCount=visibleItemCount;
                this.TotalItemCount=totalItemCount;
            }
        });
    }


    private void initalise() {
        Skewed_txtStoreCode = (TextView) findViewById(R.id.txtStoreCode);
        Skewed_txtStoreName = (TextView) findViewById(R.id.txtStoreName);

        Skewed_BtnBack = (RelativeLayout) findViewById(R.id.skewed_BtnBack);
        sk_imgfilter = (RelativeLayout)findViewById(R.id.sk_imgfilter);
        SkewedSizeListview = (ListView) findViewById(R.id.skewedListView);
        Skewed_segmented=(SegmentedGroup)findViewById(R.id.skewed_segmented);
        sk_quickFilter=(RelativeLayout) findViewById(R.id.sk_quickFilter);
        Skewed_core=(RadioButton)findViewById(R.id.skewed_core);
        Skewed_core.toggle();
        Skewed_fashion=(RadioButton)findViewById(R.id.skewed_fashion);
        quickFilterPopup= (RelativeLayout)findViewById(R.id.quickFilterPopup);
        quickFilter_baseLayout = (RelativeLayout)findViewById(R.id.quickFilter_baseLayout);
        qfDoneLayout =(RelativeLayout)findViewById(R.id.qfDoneLayout);
        quickFilterPopup.setVisibility(View.GONE);
        Toggle_skewed_fav=(ToggleButton)findViewById(R.id.toggle_skewed_fav);
        checkCurrent =(CheckBox)findViewById(R.id.checkCurrent);
        checkPrevious = (CheckBox)findViewById(R.id.checkPrevious);
        checkOld = (CheckBox)findViewById(R.id.checkOld);
        checkUpcoming = (CheckBox)findViewById(R.id.checkUpcoming);

        Skewed_segmented.setOnCheckedChangeListener(this);
        Skewed_BtnBack.setOnClickListener(this);
        sk_imgfilter.setOnClickListener(this);
        sk_quickFilter.setOnClickListener(this);
        quickFilter_baseLayout.setOnClickListener(this);
        qfDoneLayout.setOnClickListener(this);
        checkCurrent.setOnClickListener(this);
        checkPrevious.setOnClickListener(this);
        checkOld.setOnClickListener(this);
        checkUpcoming.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.skewed_BtnBack:
                onBackPressed();
                break;
            case R.id.toggle_skewed_fav:
                if(Toggle_skewed_fav.isChecked())
                {
                    Toggle_skewed_fav.setChecked(true);
                }else
                {
                    Toggle_skewed_fav.setChecked(false);
                }

                break;
            case R.id.sk_imgfilter:
                Intent intent = new Intent(SkewedSizesActivity.this, InventoryFilterActivity.class);
                intent.putExtra("checkfrom","skewedSize");
                startActivity(intent);
                finish();
                break;
            case R.id.sk_quickFilter :
                filterFunction();
                break;
            case R.id.quickFilter_baseLayout :
                quickFilterPopup.setVisibility(View.GONE);
                break;
            case R.id.qfDoneLayout:
                if (checkCurrent.isChecked()) {
                    popupCurrent();
                    checkPrevious.setChecked(false);
                    checkOld.setChecked(false);
                    checkUpcoming.setChecked(false);
                    quickFilterPopup.setVisibility(View.GONE);

                } else if (checkPrevious.isChecked()) {
                    popupPrevious();
                    checkCurrent.setChecked(false);
                    checkOld.setChecked(false);
                    checkUpcoming.setChecked(false);
                    quickFilterPopup.setVisibility(View.GONE);

                } else if (checkOld.isChecked()) {
                    popupOld();
                    checkPrevious.setChecked(false);
                    checkCurrent.setChecked(false);
                    checkUpcoming.setChecked(false);
                    quickFilterPopup.setVisibility(View.GONE);

                } else if (checkUpcoming.isChecked()) {
                    popupUpcoming();
                    checkCurrent.setChecked(false);
                    checkPrevious.setChecked(false);
                    checkOld.setChecked(false);
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

    private void popupCurrent() {
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            limit = 10;
            offsetvalue = 0;
            top = 10;
            corefashion="Core";
            seasonGroup = "Current";
            SkewedSizeList.clear();
            Reusable_Functions.sDialog(this, "Loading.......");
            requestRunningPromoApi();

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
            corefashion="Core";
            seasonGroup = "Previous";
            SkewedSizeList.clear();
            Reusable_Functions.sDialog(this, "Loading.......");
            requestRunningPromoApi();

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
            corefashion="Core";
            seasonGroup = "Old";
            SkewedSizeList.clear();
            Reusable_Functions.sDialog(this, "Loading.......");
            requestRunningPromoApi();
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
            corefashion="Core";
            SkewedSizeList.clear();
            Reusable_Functions.sDialog(this, "Loading.......");
            requestRunningPromoApi();
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
        Intent intent=new Intent(context, DashBoardActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch(checkedId)
        {
            case R.id.skewed_core:
                if(Skewed_core.isChecked())
                {
                    limit = 10;
                    offsetvalue = 0;
                    top = 10;
                    corefashion="Core";
                    SkewedSizeList.clear();
                    Reusable_Functions.sDialog(this, "Loading.......");
                    requestRunningPromoApi();
                }
                break;
            case R.id.skewed_fashion:
                if(Skewed_fashion.isChecked())
                {
                    limit = 10;
                    offsetvalue = 0;
                    top = 10;
                    corefashion="Fashion";
                    SkewedSizeList.clear();
                    Reusable_Functions.sDialog(this, "Loading.......");
                    requestRunningPromoApi();
                }
                break;



        }

    }
}
