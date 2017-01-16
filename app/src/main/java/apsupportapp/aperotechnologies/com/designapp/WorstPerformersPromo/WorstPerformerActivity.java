package apsupportapp.aperotechnologies.com.designapp.WorstPerformersPromo;

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
import android.widget.ListView;
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
import java.util.Map;


import apsupportapp.aperotechnologies.com.designapp.BestPerformersPromo.FilterActivity;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.DashBoardActivity;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;





public class WorstPerformerActivity extends AppCompatActivity implements View.OnClickListener{

    TextView Wst_txtStoreCode, Wst_txtStoreName,PopPromo,PopPromoU,PopSort;
    RelativeLayout Wst_imageBtnBack, Wst_sort,Wst_imgfilter,BaseLayout,SortPopup;
    RunningPromoListDisplay WorstPromoListDisplay;
    private SharedPreferences sharedPreferences;
    String userId, bearertoken;
    String TAG = "WorstPerformersActivity";
    private int count = 0;
    private int limit = 10;
    private int offsetvalue = 0;
    private int top = 10;
    Context context = this;
    private RequestQueue queue;
    private Gson gson;
    ListView WorstPerformanceListView;
    ArrayList<RunningPromoListDisplay> WorstpromoList;
    private int focusposition = 0;
    private boolean userScrolled;
    private WorstPromoAdapter worstPromoAdapter;
    private View footer;
    int index = 0;
    int iterations = 0;
    private int popPromo=0;
    private int orderbycol=1;
    private String lazyScroll="OFF";
    private CheckBox CheckWstSale,CheckWstSaleU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worst_performer);
        getSupportActionBar().hide();
        initalise();
        CheckWstSale.setChecked(true);
        SortPopup.setVisibility(View.GONE);

        gson = new Gson();
        WorstpromoList = new ArrayList<RunningPromoListDisplay>();
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
      //  worstPromoAdapter = new WorstPromoAdapter(WorstpromoList,context);
        footer = getLayoutInflater().inflate(R.layout.bestpromo_footer,null);

        WorstPerformanceListView.addFooterView(footer);
        //footer.setVisibility(View.GONE);
        //WorstPerformanceListView.setAdapter(worstPromoAdapter);




    }

   //String url = ConstsCore.web_url + "/v1/display/bestworstpromodetails/" +userId + "?offset=" +offsetvalue + "&limit=" +limit+"&top=" +top+"&orderby=" +"ASC"+"&orderbycol=" +"1";

    private void requestRunningPromoApi() {

        //String url = ConstsCore.web_url + "/v1/display/runningpromoheader/" + userId + "?view=" + selectedsegValue + "&offset=" + offsetvalue + "&limit=" + limit;
        String url = ConstsCore.web_url + "/v1/display/bestworstpromodetails/" +userId + "?offset=" +offsetvalue + "&limit=" +limit+"&top=" +top+"&orderby=" +"ASC"+"&orderbycol=" +orderbycol;
        //  https://smdm.manthan.com/v1/display/bestworstpromodetails/4813?offset=0&limit=10&top=2&orderby=DESC&ord erbycol=1        Log.e(TAG, "Url" + "" + url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, "Worst promo : " + " " + response);
                        Log.i(TAG, "response" + "" + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                footer.setVisibility(View.GONE);

                            } else if (response.length() == limit) {


                                //BestPerformanceListView.removeFooterView(footer);
                                //bestPromoAdapter.notifyDataSetChanged();
                                Log.e(TAG, "promo eql limit");
                                for (int i = 0; i < response.length(); i++) {

                                    WorstPromoListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                    WorstpromoList.add(WorstPromoListDisplay);

                                }
                                offsetvalue =offsetvalue+10;
                                top =top+10;
                                //  count++;

                                // requestRunningPromoApi();

                            }

                            else if (response.length() < limit) {
                                Log.e(TAG, "promo /= limit");
                                for (int i = 0; i < response.length(); i++) {

                                    WorstPromoListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                    WorstpromoList.add(WorstPromoListDisplay);
                                    offsetvalue =offsetvalue+response.length();
                                    top =top+response.length();

                                }
                            }
                            footer.setVisibility(View.GONE);

                            // BestPromo_footer.setVisibility(View.GONE);
                            if(popPromo==10)
                            {
                                worstPromoAdapter = new WorstPromoAdapter(WorstpromoList,context);
                                WorstPerformanceListView.setAdapter(worstPromoAdapter);
                                popPromo=0;

                            }
                            else if(lazyScroll.equals("ON")){
                                worstPromoAdapter.notifyDataSetChanged();
                            }
                            else
                            {
                                worstPromoAdapter = new WorstPromoAdapter(WorstpromoList,context);
                                WorstPerformanceListView.setAdapter(worstPromoAdapter);
                            }

                            Reusable_Functions.hDialog();

                            //  Bst_storecode.setText(BestpromoList.get(0).getStoreCode());
                            // Bst_txtStoreName.setText(BestpromoList.get(0).getStoreDesc());
                            Log.v(TAG,"set text on");

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

        WorstPerformanceListView.setOnScrollListener(new AbsListView.OnScrollListener() {
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
        Wst_txtStoreCode = (TextView) findViewById(R.id.txtStoreCode);
        Wst_txtStoreName = (TextView) findViewById(R.id.txtStoreName);
        Wst_imageBtnBack = (RelativeLayout) findViewById(R.id.wst_imageBtnBack);
        Wst_sort = (RelativeLayout) findViewById(R.id.wst_sort);
        Wst_imgfilter = (RelativeLayout) findViewById(R.id.wst_imgfilter);
        WorstPerformanceListView = (ListView) findViewById(R.id.worstPerformanceListView);
        PopPromo = (TextView) findViewById(R.id.popPromo);
        PopPromoU = (TextView) findViewById(R.id.popPromoU);
        PopSort = (TextView) findViewById(R.id.popSort);
        BaseLayout = (RelativeLayout) findViewById(R.id.baseLayout);
        SortPopup=(RelativeLayout)findViewById(R.id.sortPopup);
        CheckWstSale=(CheckBox)findViewById(R.id.checkPromoSale);
        CheckWstSaleU=(CheckBox)findViewById(R.id.checkPromoSaleU);


        Wst_imageBtnBack.setOnClickListener(this);

        Wst_imgfilter.setOnClickListener(this);
        Wst_sort.setOnClickListener(this);
        BaseLayout.setOnClickListener(this);
        PopPromo.setOnClickListener(this);
        PopPromoU.setOnClickListener(this);
        PopSort.setOnClickListener(this);
        CheckWstSale.setOnClickListener(this);
        CheckWstSaleU.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.wst_imageBtnBack:
                onBackPressed();
                break;
            case R.id.wst_sort:
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

            case R.id.checkPromoSale:
                if(CheckWstSale.isChecked())
                {
                    popupPromo();
                    CheckWstSaleU.setChecked(false);
                    SortPopup.setVisibility(View.GONE);


                }

                break;
            case R.id.checkPromoSaleU:
                if(CheckWstSaleU.isChecked())
                {
                    popupPromoU();
                    CheckWstSale.setChecked(false);
                    SortPopup.setVisibility(View.GONE);

                }
                break;
            case R.id.wst_imgfilter:
               Intent intent = new Intent(WorstPerformerActivity.this, FilterActivity.class);
               intent.putExtra("from","worstPromo");
                startActivity(intent);


        }
    }

    private void popupPromoU()
    {
        WorstpromoList.clear();
        Log.e(TAG,"bestPromolist size"+WorstpromoList.size());
        Reusable_Functions.sDialog(this, "Loading.......");
        popPromo=10;
        limit = 10;
        offsetvalue = 0;
        top = 10;
        orderbycol=2;
        requestRunningPromoApi();
    }

    private void popupPromo()
    {
        WorstpromoList.clear();
        Log.e(TAG,"bestPromolist size"+WorstpromoList.size());
        Reusable_Functions.sDialog(this, "Loading.......");
        popPromo=10;
        limit = 10;
        offsetvalue = 0;
        top = 10;
        orderbycol=1;
        requestRunningPromoApi();

    }

    private void sortFunction()
    {
        SortPopup.setVisibility(View.VISIBLE);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
  /*      Intent intent=new Intent(context, DashBoardActivity.class);
        startActivity(intent);*/
        finish();
    }
}
