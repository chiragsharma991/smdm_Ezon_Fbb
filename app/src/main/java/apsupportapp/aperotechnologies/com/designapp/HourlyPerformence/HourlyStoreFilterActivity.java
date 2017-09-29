package apsupportapp.aperotechnologies.com.designapp.HourlyPerformence;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.CustomerEngagement.CustomerLookupActivity;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;

import static apsupportapp.aperotechnologies.com.designapp.CustomerEngagement.CustomerLookupActivity.customerLookUpActivity;
import static apsupportapp.aperotechnologies.com.designapp.HourlyPerformence.HourlyPerformence.hourlyPerformance;

public class HourlyStoreFilterActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rel_sfilter_back, rel_sfilter_done;
    ArrayList<String> loc_listDataHeader;
    HashMap<String, List<String>> loc_listDataChild;
    private EditText et_search;
    public static ExpandableListView explv_hr_locatn;
    Context context = this;
    public static RelativeLayout rel_process_filter;
    static HourlyLocationAdapter hourlyLocationAdapter;
    int offset = 0, limit = 100, count = 0;
    public int store_level_filter = 3;
    String userId, bearertoken, geoLevel2Code, lobId;
    private Intent intent;
    SharedPreferences sharedPreferences;
    RequestQueue queue;
    static String hr_str_checkFrom;
    private int filter_level = 0;
    private StringBuilder build = new StringBuilder();
    static List<String> hr_storeList;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_filter);
        getSupportActionBar().hide();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        context = this;
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLevel2Code = sharedPreferences.getString("concept", "");
        lobId = sharedPreferences.getString("lobid", "");
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();

        hr_storeList = new ArrayList<String>();
        hr_str_checkFrom = getIntent().getStringExtra("checkfrom");
        initialise_ui();
        prepareData();
        hourlyLocationAdapter = new HourlyLocationAdapter(this, loc_listDataHeader, loc_listDataChild, explv_hr_locatn, hourlyLocationAdapter);
        explv_hr_locatn.setAdapter(hourlyLocationAdapter);
//        9


        explv_hr_locatn.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                setListViewHeight1(parent, groupPosition);
                return false;
            }
        });

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                Boolean handled = false;
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_NEXT) || (actionId == EditorInfo.IME_ACTION_NONE) || (actionId == EditorInfo.IME_ACTION_SEARCH)) {
                    et_search.clearFocus();
                    InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputManager != null) {
                        inputManager.hideSoftInputFromWindow(et_search.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    handled = true;
                }
                return handled;
            }
        });

        et_search.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(et_search.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                s = et_search.getText().toString();
                Log.e("s :", "" + s);
                hourlyLocationAdapter.filterData(s.toString());

            }
            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
    }

    private void setListViewHeight1(ExpandableListView listView, int group) {
        HourlyLocationAdapter listAdapter = (HourlyLocationAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }




    private void prepareData()
    {
        loc_listDataHeader = new ArrayList<String>();
        loc_listDataChild = new HashMap<String, List<String>>();
        loc_listDataHeader.add("Store");

        if (Reusable_Functions.chkStatus(HourlyStoreFilterActivity.this))
        {

                rel_process_filter.setVisibility(View.VISIBLE);
                offset = 0;
                limit = 100;
                count = 0;
                store_level_filter = 3;
                requestStore(offset, limit);
        }
        else
        {
            Toast.makeText(HourlyStoreFilterActivity.this, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }

        loc_listDataChild.put(loc_listDataHeader.get(0), hr_storeList);

    }

    private void initialise_ui()
    {
        rel_sfilter_back = (RelativeLayout) findViewById(R.id.rel_sfilter_back);
        rel_sfilter_done = (RelativeLayout) findViewById(R.id.rel_sfilter_done);
        rel_process_filter = (RelativeLayout) findViewById(R.id.rel_process_filter);
        et_search = (EditText) findViewById(R.id.et_search);
        et_search.setSingleLine(true);
        explv_hr_locatn = (ExpandableListView) findViewById(R.id.explv_hr_locatn);
        explv_hr_locatn.setTextFilterEnabled(true);
        explv_hr_locatn.setDivider(getResources().getDrawable(R.color.grey));
        rel_sfilter_done.setOnClickListener(this);
        rel_sfilter_back.setOnClickListener(this);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_sfilter_back:
                onBackPressed();
                break;
            case R.id.rel_sfilter_done:
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                //Send selected hierarchy level to selected activity
                selectbuild();
                Log.e("came here == "," " +getIntent().getStringExtra("checkfrom"));
                if (getIntent().getStringExtra("checkfrom").equals("HourlyPerformance"))
                {
                    intent = new Intent(HourlyStoreFilterActivity.this, HourlyPerformence.class);

                    if (build.length() != 0)
                    {
                        hourlyPerformance.finish();
                    }
                    callback(build);
                }

                if (getIntent().getStringExtra("checkfrom").equals("CustomerEngagement"))
                {
                    intent = new Intent(HourlyStoreFilterActivity.this, CustomerLookupActivity.class);

                    if (build.length() != 0)
                    {
                        customerLookUpActivity.finish();
                    }
                    callback(build);
                }

                break;
        }
    }

    private void selectbuild() {


        if (HourlyLocationAdapter.hr_store_str.length() != 0) {
            Log.e("came here 1","");
            String store = HourlyLocationAdapter.hr_store_str;//.substring(0,4);
            String Store;
            Store = "storeCode=" + store;
            build.append("&");
            build.append(Store.replace(",$", ""));
        }


    }

    private void callback(StringBuilder build) {
        if (build.length() == 0) {
            Toast.makeText(context, "Please select value...", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            intent.putExtra("selectedStringVal", build.toString());
            Log.e("TAG", "callback:  selectedStringVal" + build.toString());

        }
        startActivity(intent);
        HourlyLocationAdapter.hr_store_str = "";
        hr_str_checkFrom = "";
        finish();
    }


    @Override
    public void onBackPressed() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        finish();
    }


//------------------------------------API Declaration--------------------------------------//



    private void requestStore(int offsetval, int limitval) {
        String store_url = "";
        store_url = ConstsCore.web_url + "/v1/display/storehierarchyEZNew/" + userId + "?offset=" + offset + "&limit=" + limit + "&level=" + store_level_filter + "&geoLevel2Code=" + geoLevel2Code + "&lobId=" + lobId;
        Log.e("store url :", "" + store_url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, store_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        Log.e("store response :", "" + response);
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                rel_process_filter.setVisibility(View.GONE);
                                Toast.makeText(HourlyStoreFilterActivity.this, "no data found in store ", Toast.LENGTH_LONG).show();
                            }
                            else if (response.length() == limit)
                            {
                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String store = productName1.getString("descEz");
                                    hr_storeList.add(store);
                                }
                                offset = (limit * count) + limit;
                                count++;
                                requestStore(offset, limit);
                            }
                            else if (response.length() < limit)
                            {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String store = productName1.getString("descEz");
                                    hr_storeList.add(store);
                                }
                                rel_process_filter.setVisibility(View.GONE);

                            }


                        }
                        catch (Exception e)
                        {
                            Reusable_Functions.hDialog();
                            rel_process_filter.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        rel_process_filter.setVisibility(View.GONE);
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


}