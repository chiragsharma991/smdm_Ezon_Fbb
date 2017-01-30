package apsupportapp.aperotechnologies.com.designapp.RunningPromo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.crashlytics.android.answers.LoginEvent;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.BestPerformersPromo.FilterActivity;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.DashBoardActivity;
import apsupportapp.aperotechnologies.com.designapp.FreshnessIndex.FreshnessIndexDetails;
import apsupportapp.aperotechnologies.com.designapp.LoginActivity;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.RecyclerItemClickListener;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.SplashActivity;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;

public class RunningPromoActivity extends AppCompatActivity implements View.OnClickListener, GravitySnapHelper.SnapListener {

    TextView storecode, storedesc, promoval1, promoval2;
    RelativeLayout imageback, imagefilter;
    RunningPromoListDisplay runningPromoListDisplay;
    private SharedPreferences sharedPreferences;
    String userId, bearertoken;
    String TAG = "RunningPromoactivity";
    private int count = 0;
    private int limit = 100;
    private int offsetvalue = 0;
    Context context = RunningPromoActivity.this;
    private RequestQueue queue;
    private Gson gson;
    RecyclerView PromoListView;
    ArrayList<RunningPromoListDisplay> promoList;
    private int focusposition = 0;
    private int itemCount = 0;
    private int totalItemCount;
    private boolean scrolling = false;
    private RunningPromoSnapAdapter runningPromoSnapAdapter;
    private int firstVisibleItem = 0;
    int prevState = RecyclerView.SCROLL_STATE_IDLE;
    int currentState = RecyclerView.SCROLL_STATE_IDLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_promo);
        getSupportActionBar().hide();
        initalise();
        //  PromoListView.addFooterView(getLayoutInflater().inflate(R.layout.list_footer, null));
        gson = new Gson();
        promoList = new ArrayList<RunningPromoListDisplay>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Log.e(TAG, "userID and token" + userId + "and this is" + bearertoken);
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        //Running testing
        Reusable_Functions.sDialog(this, "Loading.......");
        requestRunningPromoApi();

    }

    private void requestRunningPromoApi() {

        if (Reusable_Functions.chkStatus(context)) {

            //String url = ConstsCore.web_url + "/v1/display/runningpromoheader/" + userId + "?view=" + selectedsegValue + "&offset=" + offsetvalue + "&limit=" + limit;
            String url = ConstsCore.web_url + "/v1/display/runningpromoheader/" + userId + "?offset=" + offsetvalue + "&limit=" + limit;

            Log.e(TAG, "Url" + "" + url);
            final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.i(TAG, "Running promo : " + " " + response);
                            Log.i(TAG, "Sales View Pager response" + "" + response.length());


                            try {
                                if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                    Reusable_Functions.hDialog();
                                    Toast.makeText(RunningPromoActivity.this, "no data found", Toast.LENGTH_SHORT).show();
                                } else if (response.length() == limit) {
                                    Log.e(TAG, "promo eql limit");
                                    for (int i = 0; i < response.length(); i++) {

                                        runningPromoListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                        promoList.add(runningPromoListDisplay);


                                    }
                                    offsetvalue = (limit * count) + limit;
                                    count++;
                                    //

                                    requestRunningPromoApi();

                                } else if (response.length() < limit) {
                                    Log.e(TAG, "promo /= limit");
                                    for (int i = 0; i < response.length(); i++) {

                                        runningPromoListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                        promoList.add(runningPromoListDisplay);
                                    }

                                    NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("","in"));

                                    Log.e(TAG, "promolistSize" + promoList.size());
                                    promoval1.setText("\u20B9\t" + formatter.format(Math.round(promoList.get(0).getDurSaleNetVal())));
                                    promoval2.setText("" + promoList.get(0).getDurSaleTotQty());
                                    storecode.setText(promoList.get(0).getStoreCode());
                                    storedesc.setText(promoList.get(0).getStoreDesc());
                                }


                                //  RunningPromoAdapter runningPromoAdapter = new RunningPromoAdapter(promoList, RunningPromoActivity.this);
                                // PromoListView.setAdapter(runningPromoAdapter);
                                PromoListView.setLayoutManager(new LinearLayoutManager(
                                        PromoListView.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                PromoListView.setOnFlingListener(null);
                                new GravitySnapHelper(48).attachToRecyclerView(PromoListView);
                                runningPromoSnapAdapter = new RunningPromoSnapAdapter(promoList, RunningPromoActivity.this);
                                PromoListView.setAdapter(runningPromoSnapAdapter);

                                // PromoListView.setSelectionFromTop(3,0);
                                Reusable_Functions.hDialog();

                                // txtNetSalesVal.setText("\u20B9 "+(int) salesAnalysis.getSaleNetVal());


                            } catch (Exception e) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "data failed...." + e.toString(), Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(context, "server not responding..", Toast.LENGTH_SHORT).show();
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


            PromoListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    RecyclerViewPositionHelper mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
                    int visibleItemCount = recyclerView.getChildCount();
                    totalItemCount = mRecyclerViewHelper.getItemCount();
                    firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();


                }


                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, final int newState) {
                    super.onScrollStateChanged(recyclerView, newState);


                    currentState = newState;
                    if (prevState != RecyclerView.SCROLL_STATE_IDLE && currentState == RecyclerView.SCROLL_STATE_IDLE) {

                        Log.i(TAG, "" + "scroll state" + newState);
                        Handler h = new Handler();
                        h.postDelayed(new Runnable() {
                            public void run() {
                                Log.e(TAG, "run: time out");
                                TimeUP();
                            }
                        }, 400);


                    }
                    prevState = currentState;


                }


            });


//            PromoListView.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
//                @Override
//                public void onItemClick(View view,final int position) {
//                    if (position < promoList.size()) {
//
////                        LinearLayout rel = (LinearLayout) view;
////                        TextView txtview = (TextView)rel.getChildAt(0);
////                        txtview.setOnClickListener(new View.OnClickListener() {
////                            @Override
////                            public void onClick(View view) {
////                                Intent i = new Intent(context, RunningPromoDetails.class);
////                                i.putExtra("VM", promoList.get(position).getPromoDesc());
////                                context.startActivity(i);
////                            }
////                        });
//
////                        switch (view.getId()) {
////                            case R.id.txtPromoName:
////                                Intent i = new Intent(context, RunningPromoDetails.class);
////                                i.putExtra("VM", promoList.get(position).getPromoDesc());
////                                context.startActivity(i);
////                                break;
////                            case R.id.txtstartDate:
////                                Intent j = new Intent(context, RunningPromoDetails.class);
////                                j.putExtra("VM", promoList.get(position).getPromoDesc());
////                                context.startActivity(j);
////                                break;
////                            case R.id.txtEndDate:
////                                Intent k = new Intent(context, RunningPromoDetails.class);
////                                k.putExtra("VM", promoList.get(position).getPromoDesc());
////                                context.startActivity(k);
////                                break;
////                            case R.id.txtDays:
////                                Intent l = new Intent(context, RunningPromoDetails.class);
////                                l.putExtra("VM", promoList.get(position).getPromoDesc());
////                                context.startActivity(l);
////                                break;
////
////                        }
//
//
//
//                            Log.e(TAG, "onItemClick: "+view.getId() );
//                            //Intent i = new Intent(context, RunningPromoDetails.class);
//                            //i.putExtra("VM", promoList.get(position).getPromoDesc());
//                           // context.startActivity(i);
//
//
//
//                    }
//
//                }
//            }));

       /*     PromoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.e(TAG, "listview position" + position + "and list is" + itemCount);
                    if (itemCount >= position) {
                        Intent i = new Intent(context, RunningPromoDetails.class);
                        i.putExtra("VM", promoList.get(position).getPromoDesc());
                        context.startActivity(i);
                    }

                }
            });*/


         /*   PromoListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                    //Log.e(TAG, "scroll on" + " " + scrollState);


                    switch (scrollState) {
                        case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                            if (scrolling){
                                // get first visible item
                                View itemView = view.getChildAt(0);
                                int top = Math.abs(itemView.getTop()); // top is a negative value
                                int bottom = Math.abs(itemView.getBottom());
                                if (top >= bottom){
                                    ((ListView)view).setSelectionFromTop(view.getFirstVisiblePosition()+1, 0);
                                } else {
                                    ((ListView)view).setSelectionFromTop(view.getFirstVisiblePosition(), 0);
                                }
                            }
                            scrolling = false;
                            break;
                        case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                            Log.i(TAG,"TEST"+"SCROLLING");
                            scrolling = true;
                            break;
                    }
*//*
                    if (promoList.size() != 0) {

                        if (view.getFirstVisiblePosition() <= promoList.size() - 1) {
                            Log.e(TAG, "scroll on" + " " + scrollState);


                            focusposition = view.getFirstVisiblePosition();

//                           // smoothScrollToPositionFromTop(view,2);
//                            int h1 = PromoListView.getHeight();
//                            int h2 = PromoListView.getHeight();
//
//                            PromoListView.smoothScrollToPositionFromTop(view.getFirstVisiblePosition(), h1/2 - h2/2);
//                            PromoListView.smoothScrollToPositionFromTop(view.getFirstVisiblePosition(),0);
                            PromoListView.setSelection(view.getFirstVisiblePosition());

                            // PromoListView.setSelection(view.getFirstVisiblePosition());
                            Log.e(TAG, "firstVisibleItem" + " " + focusposition);
                            //promoval1.setText(""+String.format("%.1f",promoList.get(focusposition).getDurSaleNetVal()));
                            promoval1.setText("\u20B9\t" + Math.round(promoList.get(focusposition).getDurSaleNetVal()));
                            promoval2.setText("" + promoList.get(focusposition).getDurSaleTotQty());
                            storecode.setText(promoList.get(focusposition).getStoreCode());
                            storedesc.setText(promoList.get(focusposition).getStoreDesc());


                        } else {
                            focusposition = promoList.size() - 1;
                            PromoListView.setSelection(promoList.size() - 1);

                        }
                    }*//*


                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });

        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
            Reusable_Functions.hDialog();


        }*/


        }
    }

    private void callActivity()
    {

    }

    private void TimeUP() {
        // if (promoList.size() != 0 && newState== RecyclerView.SCROLL_STATE_IDLE) {
        //check ideal condition then call .....
        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("","in"));
        if (firstVisibleItem < runningPromoSnapAdapter.getItemCount() - 1) {
            Log.i(TAG, "onScrollStateChanged: item " + firstVisibleItem + "getitem Count" + runningPromoSnapAdapter.getItemCount());
            //10<10 where footer is call then it goes else condition


            promoval1.setText("\u20B9\t" + formatter.format(Math.round(promoList.get(firstVisibleItem).getDurSaleNetVal())));
            promoval2.setText("" + promoList.get(firstVisibleItem).getDurSaleTotQty());
            storecode.setText(promoList.get(firstVisibleItem).getStoreCode());
            storedesc.setText(promoList.get(firstVisibleItem).getStoreDesc());
        } else {
            Log.e(TAG, "onScrollStateChanged else conition: ");
            firstVisibleItem = promoList.size() - 1;
            LinearLayoutManager llm = (LinearLayoutManager) PromoListView.getLayoutManager();
            llm.scrollToPosition(firstVisibleItem);

            promoval1.setText("\u20B9\t" + formatter.format(Math.round(promoList.get(firstVisibleItem).getDurSaleNetVal())));
            promoval2.setText("" + promoList.get(firstVisibleItem).getDurSaleTotQty());
            storecode.setText(promoList.get(firstVisibleItem).getStoreCode());
            storedesc.setText(promoList.get(firstVisibleItem).getStoreDesc());
        }


    }


    private void initalise() {
        storecode = (TextView) findViewById(R.id.txtStoreCode);
        storedesc = (TextView) findViewById(R.id.txtStoreName);
        promoval1 = (TextView) findViewById(R.id.txtPromoVal1);
        promoval2 = (TextView) findViewById(R.id.txtPromoVal2);
        imageback = (RelativeLayout) findViewById(R.id.rp_imageBtnBack);
        imagefilter = (RelativeLayout) findViewById(R.id.rp_imgfilter);

        PromoListView = (RecyclerView) findViewById(R.id.promoListview);
        PromoListView.setLayoutManager(new LinearLayoutManager(this));
        PromoListView.setHasFixedSize(true);

        imageback.setOnClickListener(this);
        imagefilter.setOnClickListener(this);


    }

    public static void smoothScrollToPositionFromTop(final AbsListView view, final int position) {
        View child = getChildAtPosition(view, position);
        // There's no need to scroll if child is already at top or view is already scrolled to its end
        if ((child != null) && ((child.getTop() == 0) || ((child.getTop() > 0) && !view.canScrollVertically(1)))) {
            Log.e("if condition Scroll", "-----");

            return;
        }

        view.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final AbsListView view, final int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    view.setOnScrollListener(null);

                    // Fix for scrolling bug
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            view.setSelection(position);
                            Log.e("View Scroll", "-----");
                        }
                    });
                }
            }

            @Override
            public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount,
                                 final int totalItemCount) {
            }
        });

        // Perform scrolling to position
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                view.smoothScrollToPositionFromTop(position, 0);
                Log.e(" smoothScrollToPositionFromTop Scroll", "-----");

            }
        });
    }


    public static View getChildAtPosition(final AdapterView view, final int position) {
        final int index = position - view.getFirstVisiblePosition();
        if ((index >= 0) && (index < view.getChildCount())) {
            return view.getChildAt(index);
        } else {
            return null;
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rp_imageBtnBack:
                onBackPressed();
                break;
            case R.id.rp_imgfilter:
                Intent intent = new Intent(context, FilterActivity.class);
                intent.putExtra("from", "runningPromo");
                startActivity(intent);
                // finish();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* Intent intent=new Intent(context, DashBoardActivity.class);
        startActivity(intent);*/
        finish();
    }

    @Override
    public void onSnap(int position) {

    }
}
