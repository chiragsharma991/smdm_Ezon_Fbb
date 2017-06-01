package apsupportapp.aperotechnologies.com.designapp.TopOptionCutSize;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
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
import apsupportapp.aperotechnologies.com.designapp.LocalNotificationReceiver;

import apsupportapp.aperotechnologies.com.designapp.LoginActivity1;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesFilterActivity;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;
import info.hoang8f.android.segmented.SegmentedGroup;


public class TopFullCut extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    TextView Top_txtStoreCode, Top_txtStoreName;
    RelativeLayout TopBest_BtnBack, TopOption_imgfilter, TopBest_qfDoneLayout, TopBest_quickFilter_baseLayout, TopBest_sk_quickFilter;
    RunningPromoListDisplay TopOptionListDisplay;
    private SharedPreferences sharedPreferences;
    String userId, bearertoken;
    private int count = 0;
    private int limit = 10;
    private int offsetvalue = 0;
    private int top = 10;
    private int popPromo = 0;
    Context context = this;
    private RequestQueue queue;
    private Gson gson;
    ListView TopOptionListView;
    ArrayList<RunningPromoListDisplay> TopOptionList;
    private TopOptionAdapter topOptionAdapter;
    private View footer;
    private String lazyScroll = "OFF";
    private SegmentedGroup Top_segmented;
    private RadioButton Top_fashion, Top_core, Topfull_checkWTD, Topfull_checkL4W, Topfull_checkSTD;
    private ToggleButton Toggle_top_fav;
    private static String corefashion="Fashion";
    private static String checkTimeValueIs = null;
    private static String view="STD";
    private boolean from_filter=false,filter_toggleClick = false;
    private String selectedString="";
    public static Activity topFullcut;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_full_cut);
        getSupportActionBar().hide();
        initalise();
        gson = new Gson();
        topFullcut=this;
        TopOptionListView.setVisibility(View.VISIBLE);
        TopOptionList = new ArrayList<RunningPromoListDisplay>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        TopOptionListView.setTag("FOOTER");


        if (Reusable_Functions.chkStatus(context)) {

            dissmiss_progress();
            Reusable_Functions.sDialog(context, "Loading data...");
            offsetvalue = 0;
            limit = 10;
            count = 0;
            top = 10;

            if (getIntent().getStringExtra("selectedDept") == null) {
                from_filter=false;
                filter_toggleClick = false;
            }
            else if(getIntent().getStringExtra("selectedDept") != null) {
                selectedString  = getIntent().getStringExtra("selectedDept");
                from_filter=true;
                filter_toggleClick = true;

            }
            retainSegmentValue();
            requestRunningPromoApi(selectedString);
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }


        footer = getLayoutInflater().inflate(R.layout.bestpromo_footer, null);

        TopOptionListView.addFooterView(footer);


    }

    private void dissmiss_progress()
    {
        Thread mThread = new Thread() {
            @Override
            public void run() {
                Reusable_Functions.hDialog();
            }
        };

        mThread.start();

    }

    private void retainSegmentValue() {
        filter_toggleClick = true;
        if(corefashion.equals("Fashion"))
        {
           Top_core.toggle();
        }
        else {
           Top_fashion.toggle();
        }
        maintainquickFilterVal();
    }

    private void requestRunningPromoApi(final String selectedString) {

        if (Reusable_Functions.chkStatus(context)) {

            String url;
            if(from_filter)
            {
                 url = ConstsCore.web_url + "/v1/display/topoptionsbyfullcut/" + userId + "?view=" + view + "&corefashion=" + corefashion + "&level=" + SalesFilterActivity.level_filter +  selectedString + "&top=" + top + "&offset=" + offsetvalue + "&limit=" + limit;
            }else
            {
                url = ConstsCore.web_url + "/v1/display/topoptionsbyfullcut/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&top=" + top + "&corefashion=" + corefashion + "&view=" + view;
            }

            final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            TopOptionListView.setVisibility(View.VISIBLE);


                            try {
                                if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                   // Reusable_Functions.hDialog();
                                    dissmiss_progress();
                                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                    TopOptionListView.removeFooterView(footer);
                                    TopOptionListView.setTag("FOOTER_REMOVE");

                                    if (TopOptionList.size() == 0) {
                                        TopOptionListView.setVisibility(View.GONE);
                                    }
                                    return;

                                } else if (response.length() == limit) {


                                    for (int i = 0; i < response.length(); i++) {

                                        TopOptionListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                        TopOptionList.add(TopOptionListDisplay);

                                    }
                                    offsetvalue = offsetvalue + 10;
                                    top = top + 10;
                                    //  count++;


                                } else if (response.length() < limit) {
                                    for (int i = 0; i < response.length(); i++) {

                                        TopOptionListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                        TopOptionList.add(TopOptionListDisplay);
                                    }
                                    offsetvalue = offsetvalue + response.length();
                                    top = top + response.length();
                                }




                                if (lazyScroll.equals("ON")) {
                                    topOptionAdapter.notifyDataSetChanged();
                                    lazyScroll = "OFF";
                                    footer.setVisibility(View.GONE);

                                } else {
                                    topOptionAdapter = new TopOptionAdapter(TopOptionList, context);
                                    TopOptionListView.setAdapter(topOptionAdapter);


                                }

                                Top_txtStoreCode.setText(TopOptionList.get(0).getStoreCode());
                                Top_txtStoreName.setText(TopOptionList.get(0).getStoreDesc());


                                dissmiss_progress();



                            } catch (Exception e) {
                                //Reusable_Functions.hDialog();
                                dissmiss_progress();
                                TopOptionListView.removeFooterView(footer);
                                TopOptionListView.setTag("FOOTER_REMOVE");
                                Toast.makeText(context, "Data failed...", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Reusable_Functions.hDialog();
                            dissmiss_progress();
                            Toast.makeText(context, "Server not found...", Toast.LENGTH_SHORT).show();
                            TopOptionListView.removeFooterView(footer);
                            TopOptionListView.setTag("FOOTER_REMOVE");
                            TopOptionListView.setVisibility(View.GONE);

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

            TopOptionListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                public int VisibleItemCount, TotalItemCount, FirstVisibleItem;

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                    if (FirstVisibleItem + VisibleItemCount == TotalItemCount && scrollState == SCROLL_STATE_IDLE && lazyScroll.equals("OFF")) {


                        if (TopOptionListView.getTag().equals("FOOTER_REMOVE")) {
                            TopOptionListView.addFooterView(footer);
                            TopOptionListView.setTag("FOOTER_ADDED");

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
           // Reusable_Functions.hDialog();
            dissmiss_progress();
            TopOptionListView.removeFooterView(footer);
            TopOptionListView.setTag("FOOTER_REMOVE");
        }
    }


    private void initalise() {
        Top_txtStoreCode = (TextView) findViewById(R.id.top_txtStoreCode);
        Top_txtStoreName = (TextView) findViewById(R.id.top_txtStoreName);

        TopBest_BtnBack = (RelativeLayout) findViewById(R.id.topBest_BtnBack);
        TopBest_sk_quickFilter = (RelativeLayout) findViewById(R.id.sk_quickFilter);
        TopBest_qfDoneLayout = (RelativeLayout) findViewById(R.id.qfDoneLayout);
        TopBest_quickFilter_baseLayout = (RelativeLayout) findViewById(R.id.quickFilterPopup);
        TopBest_quickFilter_baseLayout.setVisibility(View.GONE);

        TopOption_imgfilter = (RelativeLayout) findViewById(R.id.topOption_imgfilter);

        TopOptionListView = (ListView) findViewById(R.id.topOptionListView);
        Top_segmented = (SegmentedGroup) findViewById(R.id.top_segmented);

        Top_core = (RadioButton) findViewById(R.id.top_core);


        Top_fashion = (RadioButton) findViewById(R.id.top_fashion);

        Topfull_checkWTD = (RadioButton) findViewById(R.id.topfull_checkWTD);
        Topfull_checkL4W = (RadioButton) findViewById(R.id.topfull_checkL4W);
        Topfull_checkSTD = (RadioButton) findViewById(R.id.topfull_checkSTD);

        Toggle_top_fav = (ToggleButton) findViewById(R.id.toggle_top_fav);


        Top_segmented.setOnCheckedChangeListener(TopFullCut.this);
        TopBest_BtnBack.setOnClickListener(TopFullCut.this);
        TopBest_sk_quickFilter.setOnClickListener(TopFullCut.this);
        Topfull_checkWTD.setOnClickListener(TopFullCut.this);
        TopBest_quickFilter_baseLayout.setOnClickListener(TopFullCut.this);
        Topfull_checkL4W.setOnClickListener(TopFullCut.this);
        Topfull_checkSTD.setOnClickListener(TopFullCut.this);
        TopOption_imgfilter.setOnClickListener(TopFullCut.this);
        TopBest_qfDoneLayout.setOnClickListener(TopFullCut.this);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.topBest_BtnBack:
                onBackPressed();
                break;
            case R.id.sk_quickFilter:
                filterFunction();
                break;
            case R.id.topOption_imgfilter:
                Intent intent = new Intent(this, SalesFilterActivity.class);
                intent.putExtra("checkfrom", "TopFullCut");
                startActivity(intent);
                break;
            case R.id.toggle_top_fav:
                if (Toggle_top_fav.isChecked()) {
                    Toggle_top_fav.setChecked(true);
                } else {
                    Toggle_top_fav.setChecked(false);
                }

                break;


            case R.id.topfull_checkWTD:
                Topfull_checkWTD.setChecked(true);
                Topfull_checkL4W.setChecked(false);
                Topfull_checkSTD.setChecked(false);
                break;
            case R.id.topfull_checkL4W:
                Topfull_checkWTD.setChecked(false);
                Topfull_checkL4W.setChecked(true);
                Topfull_checkSTD.setChecked(false);
                break;
            case R.id.topfull_checkSTD:
                Topfull_checkWTD.setChecked(false);
                Topfull_checkL4W.setChecked(false);
                Topfull_checkSTD.setChecked(true);
                break;

            case R.id.qfDoneLayout:

                if (Reusable_Functions.chkStatus(context)) {

                    if (Topfull_checkWTD.isChecked()) {
                        checkTimeValueIs = "CheckWTD";
                        view = "WTD";
                        DoneTime();
                    } else if (Topfull_checkL4W.isChecked()) {
                        checkTimeValueIs = "CheckL4W";
                        view = "L4W";
                        DoneTime();

                    } else if (Topfull_checkSTD.isChecked()) {
                        checkTimeValueIs = "CheckSTD";
                        view = "STD";
                        DoneTime();

                    }


                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();

                }

                TopBest_quickFilter_baseLayout.setVisibility(View.GONE);

                break;


            //base layout clicking>>>

            case R.id.quickFilterPopup:
                maintainquickFilterVal();

                break;


        }
    }

    private void maintainquickFilterVal() {
        if (checkTimeValueIs == null) {
            Topfull_checkWTD.setChecked(false);
            Topfull_checkL4W.setChecked(false);
            Topfull_checkSTD.setChecked(true);
        } else {

            switch (checkTimeValueIs) {
                case "CheckWTD":
                    Topfull_checkWTD.setChecked(true);
                    Topfull_checkL4W.setChecked(false);
                    Topfull_checkSTD.setChecked(false);
                    break;
                case "CheckL4W":
                    Topfull_checkWTD.setChecked(false);
                    Topfull_checkL4W.setChecked(true);
                    Topfull_checkSTD.setChecked(false);
                    break;
                case "CheckSTD":
                    Topfull_checkWTD.setChecked(false);
                    Topfull_checkL4W.setChecked(false);
                    Topfull_checkSTD.setChecked(true);
                    break;
                default:
                    break;


            }
        }
        TopBest_quickFilter_baseLayout.setVisibility(View.GONE);
    }

    private void filterFunction() {
        TopBest_quickFilter_baseLayout.setVisibility(View.VISIBLE);
    }

    private void DoneTime() {
        limit = 10;
        offsetvalue = 0;
        top = 10;
        TopOptionList.clear();
        Reusable_Functions.sDialog(this, "Loading.......");
        requestRunningPromoApi(selectedString);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        view = null;
        corefashion = null;
        checkTimeValueIs = null;
        view = "STD";
        corefashion = "Fashion";
        this.finish();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        // Id is changed so that we have change core in fashion
        if(!filter_toggleClick ) {
            switch (checkedId) {
                case R.id.top_core:
                    if (Top_core.isChecked()) {
                        if (Reusable_Functions.chkStatus(context)) {
                            //Reusable_Functions.hDialog();
                            dissmiss_progress();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            limit = 10;
                            offsetvalue = 0;
                            top = 10;
                            corefashion = "Fashion";
                            lazyScroll = "OFF";
                            TopOptionList.clear();
                            TopOptionListView.setVisibility(View.GONE);
                            requestRunningPromoApi(selectedString);
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            TopOptionListView.setVisibility(View.GONE);

                        }
                    }
                    break;
                case R.id.top_fashion:
                    if (Top_fashion.isChecked()) {
                        if (Reusable_Functions.chkStatus(context)) {

                            limit = 10;
                            offsetvalue = 0;
                            top = 10;
                            corefashion = "Core";
                            lazyScroll = "OFF";
                            TopOptionList.clear();
                            TopOptionListView.setVisibility(View.GONE);
                            Reusable_Functions.sDialog(this, "Loading.......");
                            requestRunningPromoApi(selectedString);
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            TopOptionListView.setVisibility(View.GONE);

                        }
                    }


                    break;


            }
        }
        else
        {
            filter_toggleClick = false;
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (LocalNotificationReceiver.logoutAlarm) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            finish();
            Intent i = new Intent(context, LoginActivity1.class);
            // set the new task and clear flags
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }


    }
}
