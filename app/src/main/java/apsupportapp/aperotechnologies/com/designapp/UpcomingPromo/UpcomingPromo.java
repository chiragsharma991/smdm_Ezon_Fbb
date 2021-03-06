package apsupportapp.aperotechnologies.com.designapp.UpcomingPromo;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import apsupportapp.aperotechnologies.com.designapp.BestPerformersPromo.FilterActivity;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;

public class UpcomingPromo extends AppCompatActivity implements View.OnClickListener{

    TextView Up_storecode, Up_storedesc;
    RelativeLayout imageback, imagefilter;
    RunningPromoListDisplay UpcomingPromoListDisplay;
    private SharedPreferences sharedPreferences;
    String userId, bearertoken;
    private int count = 0;
    private int limit = 100;
    private int offsetvalue = 0;
    Context context = this;
    private RequestQueue queue;
    private Gson gson;
    ListView UP_PromoListView;
    ArrayList<RunningPromoListDisplay> Up_promoList;
    private int focusposition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_promo);
        getSupportActionBar().hide();
        initalise();
        gson = new Gson();
        Up_promoList = new ArrayList<RunningPromoListDisplay>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        Reusable_Functions.sDialog(this, "Loading.......");
        requestRunningPromoApi();
    }

    private void requestRunningPromoApi() {

        if (Reusable_Functions.chkStatus(context)) {
            String url = ConstsCore.web_url + "/v1/display/futurepromodetails/" + userId + "?offset=" + offsetvalue + "&limit=" + limit;

            final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                                  try {
                                if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                    Reusable_Functions.hDialog();
                                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                } else if (response.length() == limit) {
                                    for (int i = 0; i < response.length(); i++) {

                                        UpcomingPromoListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                        Up_promoList.add(UpcomingPromoListDisplay);

                                    }
                                    offsetvalue = (limit * count) + limit;
                                    count++;
                                    requestRunningPromoApi();

                                } else if (response.length() < limit) {
                                    for (int i = 0; i < response.length(); i++) {
                                        UpcomingPromoListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                        Up_promoList.add(UpcomingPromoListDisplay);
                                    }
                                    UpcomingPromoAdapter runningPromoAdapter = new UpcomingPromoAdapter(Up_promoList, context);
                                    UP_PromoListView.setAdapter(runningPromoAdapter);
                                    UP_PromoListView.getParent().requestDisallowInterceptTouchEvent(false);
                                    Reusable_Functions.hDialog();
                                    Up_storecode.setText(Up_promoList.get(0).getStoreCode());
                                    Up_storedesc.setText(Up_promoList.get(0).getStoreDesc());
                                }

                            } catch (Exception e) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "data failed..." + e.toString(), Toast.LENGTH_SHORT).show();
                                Reusable_Functions.hDialog();
                                e.printStackTrace();
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

            UP_PromoListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {


                    if (Up_promoList.size() != 0) {

                        if (view.getFirstVisiblePosition() <= Up_promoList.size() - 1) {

                            focusposition = view.getFirstVisiblePosition();

                            UP_PromoListView.setSelection(view.getFirstVisiblePosition());
                            Up_storecode.setText(Up_promoList.get(focusposition).getStoreCode());
                            Up_storedesc.setText(Up_promoList.get(focusposition).getStoreDesc());

                        } else {
                            focusposition = Up_promoList.size() - 1;
                            UP_PromoListView.setSelection(Up_promoList.size() - 1);
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

        imageback = (RelativeLayout) findViewById(R.id.up_imageBtnBack);
        imagefilter = (RelativeLayout) findViewById(R.id.up_imgfilter);
        UP_PromoListView = (ListView) findViewById(R.id.up_promoListview);
        Up_storecode = (TextView) findViewById(R.id.txtStoreCode);
        Up_storedesc = (TextView) findViewById(R.id.txtStoreName);
        imageback.setOnClickListener(this);
        imagefilter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.up_imageBtnBack:
                finish();
                break;
            case R.id.up_imgfilter:
                Intent intent1= new Intent(context, FilterActivity.class);
                intent1.putExtra("from","upComingPromo");
                startActivity(intent1);
                break;

        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
}