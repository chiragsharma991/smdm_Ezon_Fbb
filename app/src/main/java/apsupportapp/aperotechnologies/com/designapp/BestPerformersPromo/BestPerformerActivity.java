package apsupportapp.aperotechnologies.com.designapp.BestPerformersPromo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
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
import apsupportapp.aperotechnologies.com.designapp.PromoAnalysis.RunningPromoAdapter;
import apsupportapp.aperotechnologies.com.designapp.PromoAnalysis.RunningPromoDetail;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;


public class BestPerformerActivity extends AppCompatActivity implements View.OnClickListener{

    TextView Bst_storecode, Bst_txtStoreName;
    RelativeLayout Bst_imageBtnBack, Bst_sort,Bst_imgfilter;
    RunningPromoListDisplay BestPromoListDisplay;
    private SharedPreferences sharedPreferences;
    String userId, bearertoken;
    String TAG = "BestPerformersActivity";
    private int count = 0;
    private int limit = 10;
    private int offsetvalue = 0;
    Context context = this;
    private RequestQueue queue;
    private Gson gson;
    ListView BestPerformanceListView;
    ArrayList<RunningPromoListDisplay> BestpromoList;
    private int focusposition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_performer);
        getSupportActionBar().hide();
        initalise();
        //BestPerformanceListView.addFooterView(getLayoutInflater().inflate(R.layout.list_footer, null));
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
        requestRunningPromoApi();
        Reusable_Functions.sDialog(this, "Loading.......");
    }

    private void requestRunningPromoApi() {

        //String url = ConstsCore.web_url + "/v1/display/runningpromoheader/" + userId + "?view=" + selectedsegValue + "&offset=" + offsetvalue + "&limit=" + limit;
        String url = ConstsCore.web_url + "/v1/display/bestworstpromodetails/" +userId + "?offset=" +offsetvalue + "&limit=" +limit+"&top=" +"10"+"&orderby=" +"DESC"+"&orderbycol=" +"1";
      //  https://smdm.manthan.com/v1/display/bestworstpromodetails/4813?offset=0&limit=10&top=2&orderby=DESC&ord erbycol=1        Log.e(TAG, "Url" + "" + url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, "Best promo : " + " " + response);
                        Log.i(TAG, "response" + "" + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                            } else if (response.length() == limit) {
                                Log.e(TAG, "promo eql limit");
                                for (int i = 0; i < response.length(); i++) {

                                    BestPromoListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                    BestpromoList.add(BestPromoListDisplay);

                                }
                                offsetvalue = (limit * count) + limit;
                                count++;

                               // requestRunningPromoApi();

                            }
                       /*     else if (response.length() < limit) {
                                Log.e(TAG, "promo /= limit");
                                for (int i = 0; i < response.length(); i++) {

                                    runningPromoListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                    promoList.add(runningPromoListDisplay);

                                }
                                Log.e(TAG, "promolistSize" + promoList.size());
                            }*/


                            BestPromoAdapter bestPromoAdapter = new BestPromoAdapter(BestpromoList,context);
                            BestPerformanceListView.setAdapter(bestPromoAdapter);
                            Reusable_Functions.hDialog();
                          //  Bst_storecode.setText(BestpromoList.get(0).getStoreCode());
                           // Bst_txtStoreName.setText(BestpromoList.get(0).getStoreDesc());
                            Log.v(TAG,"set text on");




                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
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

        BestPerformanceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v(TAG,"set on item click  ");

               // Bst_storecode.setText(BestpromoList.get(position).getStoreCode());
               // Bst_txtStoreName.setText(BestpromoList.get(position).getStoreDesc());

            }
        });







    }


    private void initalise() {
        Bst_storecode = (TextView) findViewById(R.id.bst_txtStoreCode);
        Bst_txtStoreName = (TextView) findViewById(R.id.bst_txtStoreName);
        Bst_imageBtnBack = (RelativeLayout) findViewById(R.id.bst_imageBtnBack);
        Bst_sort = (RelativeLayout) findViewById(R.id.bst_sort);
        Bst_imgfilter = (RelativeLayout) findViewById(R.id.bst_imgfilter);
        BestPerformanceListView = (ListView) findViewById(R.id.bestPerformanceListView);

        Bst_imageBtnBack.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.bst_imageBtnBack:
                onBackPressed();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(context, DashBoardActivity.class);
        startActivity(intent);
        finish();
    }
}
