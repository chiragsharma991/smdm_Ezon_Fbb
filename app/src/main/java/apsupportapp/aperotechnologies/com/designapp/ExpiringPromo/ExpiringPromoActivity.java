package apsupportapp.aperotechnologies.com.designapp.ExpiringPromo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import apsupportapp.aperotechnologies.com.designapp.BestPerformersPromo.FilterActivity;
import apsupportapp.aperotechnologies.com.designapp.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
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

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.DashBoardActivity;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;

public class ExpiringPromoActivity extends AppCompatActivity implements View.OnClickListener{

    TextView storecode, storedesc, promoval1, promoval2;
    RelativeLayout imageback, imagefilter;
    RunningPromoListDisplay ExpiringPromoListDisplay;
    private SharedPreferences sharedPreferences;
    String userId, bearertoken;
    String TAG = "ExpiringPromoActivity";
    private int count = 0;
    private int limit = 100;
    private int offsetvalue = 0;
    Context context = this;
    private RequestQueue queue;
    private Gson gson;
    ListView ExpireListView;
    ArrayList<RunningPromoListDisplay> ExpireList;
    private int focusposition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expiring_promo);
        getSupportActionBar().hide();
        initalise();
        ExpireListView.addFooterView(getLayoutInflater().inflate(R.layout.list_footer, null));
        gson = new Gson();
        ExpireList = new ArrayList<RunningPromoListDisplay>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Log.e(TAG, "userID and token" + userId + "and this is" + bearertoken);
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        Reusable_Functions.sDialog(this, "Loading.......");
        requestRunningPromoApi();
    }

    private void requestRunningPromoApi() {

        if (Reusable_Functions.chkStatus(context)) {

            //String url = ConstsCore.web_url + "/v1/display/runningpromoheader/" + userId + "?view=" + selectedsegValue + "&offset=" + offsetvalue + "&limit=" + limit;
            String url = ConstsCore.web_url + "/v1/display/expiringpromoheader/" + userId + "?offset=" + offsetvalue + "&limit=" + limit;

            Log.e(TAG, "Url" + "" + url);

            final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.i(TAG, "Expire promo : " + " " + response);
                            Log.i(TAG, " response" + "" + response.length());

                            try {
                                if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                    Reusable_Functions.hDialog();
                                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                } else if (response.length() == limit) {
                                    Log.e(TAG, "promo eql limit");
                                    for (int i = 0; i < response.length(); i++) {

                                        ExpiringPromoListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                        ExpireList.add(ExpiringPromoListDisplay);

                                    }
                                    offsetvalue = (limit * count) + limit;
                                    count++;

                                    requestRunningPromoApi();

                                } else if (response.length() < limit) {
                                    Log.e(TAG, "promo /= limit");
                                    for (int i = 0; i < response.length(); i++) {

                                        ExpiringPromoListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                        ExpireList.add(ExpiringPromoListDisplay);


                                    }
                                    Log.e(TAG, "promolistSize" + ExpireList.size());
                                    promoval1.setText("\u20B9\t" + (int) ExpireList.get(0).getDurSaleNetVal());
                                    promoval2.setText("" + ExpireList.get(0).getDurSaleTotQty());
                                    storecode.setText(ExpireList.get(0).getStoreCode());
                                    storedesc.setText(ExpireList.get(0).getStoreDesc());
                                }


                                ExpiringPromoAdapter runningPromoAdapter = new ExpiringPromoAdapter(ExpireList, context);
                                ExpireListView.setAdapter(runningPromoAdapter);
                                Reusable_Functions.hDialog();

                                // txtNetSalesVal.setText("\u20B9 "+(int) salesAnalysis.getSaleNetVal());


                            } catch (Exception e) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "data failed..." + e.toString(), Toast.LENGTH_SHORT).show();
                                Reusable_Functions.hDialog();
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


            //-----------------------------ON CLICK LISTENER-----------------------------//


       /* ExpireListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG,"listview position"+position);
                Intent i =  new Intent(context,RunningPromoDetail.class);
                i.putExtra("VM",ExpireList.get(position).getPromoDesc());
                context.startActivity(i);

            }
        });
*/


            ExpireListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {


                    if (ExpireList.size() != 0) {

                        if (view.getFirstVisiblePosition() <= ExpireList.size() - 1) {

                            focusposition = view.getFirstVisiblePosition();

                            ExpireListView.setSelection(view.getFirstVisiblePosition());
                            Log.e(TAG, "firstVisibleItem" + " " + focusposition);
                            //promoval1.setText(""+String.format("%.1f",promoList.get(focusposition).getDurSaleNetVal()));
                            promoval1.setText("\u20B9\t" + (int) ExpireList.get(focusposition).getDurSaleNetVal());
                            promoval2.setText("" + ExpireList.get(focusposition).getDurSaleTotQty());
                            storecode.setText(ExpireList.get(focusposition).getStoreCode());
                            storedesc.setText(ExpireList.get(focusposition).getStoreDesc());


                        } else {
                            focusposition = ExpireList.size() - 1;
                            ExpireListView.setSelection(ExpireList.size() - 1);

                        }
                    }


                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });

        }
        else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();

        }
    }


    private void initalise() {
        storecode = (TextView) findViewById(R.id.txtStoreCode);
        storedesc = (TextView) findViewById(R.id.txtStoreName);
        promoval1 = (TextView) findViewById(R.id.txt_exp_PromoVal1);
        promoval2 = (TextView) findViewById(R.id.txt_exp_PromoVal2);
        imageback = (RelativeLayout) findViewById(R.id.exp_imageBtnBack);
        imagefilter = (RelativeLayout) findViewById(R.id.exp_imgfilter);
        ExpireListView = (ListView) findViewById(R.id.expireListview);

        imageback.setOnClickListener(this);
        imagefilter.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.exp_imageBtnBack:
                onBackPressed();
                break;
            case R.id.exp_imgfilter:
                Intent intent = new Intent(ExpiringPromoActivity.this, FilterActivity.class);
                intent.putExtra("from","expiringPromo");
                startActivity(intent);
                //finish();

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    /*    Intent intent=new Intent(context, DashBoardActivity.class);
        startActivity(intent);*/
        finish();
    }
}
