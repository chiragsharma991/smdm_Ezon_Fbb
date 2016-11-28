package apsupportapp.aperotechnologies.com.designapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.android.volley.toolbox.JsonObjectRequest;


import apsupportapp.aperotechnologies.com.designapp.BestPerformersPromo.BestPerformerActivity;
import apsupportapp.aperotechnologies.com.designapp.ExpiringPromo.ExpiringPromoActivity;
import apsupportapp.aperotechnologies.com.designapp.FreshnessIndex.FreshnessIndexActivity;
import apsupportapp.aperotechnologies.com.designapp.PvaSalesAnalysis.SalesPvAActivity;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesAnalysisActivity;
import apsupportapp.aperotechnologies.com.designapp.RunningPromo.RunningPromoActivity;
import apsupportapp.aperotechnologies.com.designapp.UpcomingPromo.UpcomingPromo;
import apsupportapp.aperotechnologies.com.designapp.VisualAssortmentSwipe.VisualAssortmentActivity;
import apsupportapp.aperotechnologies.com.designapp.WorstPerformersPromo.WorstPerformerActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class DashBoardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {

    ImageButton imageBtnStyle, imageBtnKeyProducts, imgBtnSales, imgBtnVisualAssortment , imgBtnPvaAnalysis,imgBtnRunningPromo,BtnUpcomingpromo,BtnExpiringpromo,BtnBestWorstpromo,btnFeshnessindex,BtnOnlyWorstpromo;
    LinearLayout hourlyFlash,productInfo,visualAssort,sales,promoAnalysis,inventory;
    TextView hourlyFlashTxt,productInfoTxt,visualAssortTxt,salesTxt,promoAnalysisTxt,inventoryTxt;

    //ExpandableHeightGridView style_grid;
    EventAdapter eventAdapter;
    String hrflash="NO";
    String pdInfo="NO";
    String vsAssort="NO";
    String sAles="NO";
    String pmAnalysis="NO";
    String inVENtory="NO";
    RequestQueue queue;
    boolean flag=true;
    String userId, bearertoken;
    //private Boolean exit = false;
    ArrayList<String> arrayList,eventUrlList;
    Context context;
    MySingleton m_config;


    ArrayList<ProductNameBean> productNameBeanArrayList;
    SharedPreferences sharedPreferences;

    //Event ViewPager

    ViewPager pager;
    PagerAdapter adapter;
    ImageView imgdot;
    LinearLayout li;
    Timer timer;
    int page = 0;


    //variable for storing collection list in style activity in searchable spinner
    public static List _collectionitems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        _collectionitems = new ArrayList();

        double val=0.3663;
        Log.e("val",String.format("%.1f", val));

        context = this;
        m_config= MySingleton.getInstance(context);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        userId = sharedPreferences.getString("userId","");
        bearertoken = sharedPreferences.getString("bearerToken","");

        Log.e("userId"," "+userId);

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();

        arrayList = new ArrayList<>();
        eventUrlList = new ArrayList<>();
        productNameBeanArrayList=new ArrayList<>();


        //to call Collection API
//        at merging
//        Bundle bundle = getIntent().getExtras();
//        userId = bundle.getString("userId");
//        Log.d("userId", "  " + userId);





//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);


        initializeUI();

        //Marketing events API
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading events...");
            requestMarketingEventsAPI();
        } else {
            // Reusable_Functions.hDialog();
            Toast.makeText(DashBoardActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
        }

        BtnOnlyWorstpromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this, WorstPerformerActivity.class);
                startActivity(intent);
                if(timer != null)
                {
                    timer.cancel();
                }
                finish();
            }
        });




        BtnBestWorstpromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DashBoardActivity.this, BestPerformerActivity.class);
                startActivity(intent);
                if(timer != null)
                {
                    timer.cancel();
                }
                finish();
            }
        });

        BtnExpiringpromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DashBoardActivity.this, ExpiringPromoActivity.class);
                startActivity(intent);
                if(timer != null)
                {
                    timer.cancel();
                }
                finish();
            }
        });

        BtnUpcomingpromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DashBoardActivity.this, UpcomingPromo.class);
                startActivity(intent);
                if(timer != null)
                {
                    timer.cancel();
                }
                finish();
            }
        });


        imgBtnRunningPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DashBoardActivity.this, RunningPromoActivity.class);
                startActivity(intent);
                if(timer != null)
                {
                    timer.cancel();
                }
                finish();
            }
        });





        imageBtnStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                requestArticleOptionsAPI();
                Intent intent = new Intent(DashBoardActivity.this, StyleActivity.class);
                startActivity(intent);
                if(timer != null)
                {
                    timer.cancel();
                }
                finish();

            }
        });

        imageBtnKeyProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this,KeyProductActivity.class);
                startActivity(intent);
                if(timer != null)
                {
                    timer.cancel();
                }
                finish();

            }

        });


        imgBtnSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this,SalesAnalysisActivity.class);
                startActivity(intent);
                if(timer != null)
                {
                    timer.cancel();
                }
                finish();
            }
        });

        imgBtnPvaAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this,SalesPvAActivity.class);
                startActivity(intent);
                if(timer != null)
                {
                    timer.cancel();
                }
                finish();
            }
        });

        imgBtnVisualAssortment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this,VisualAssortmentActivity.class);
                startActivity(intent);
                if(timer != null)
                {
                    timer.cancel();
                }

                finish();
            }
        });
        btnFeshnessindex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this,FreshnessIndexActivity.class);
                startActivity(intent);
                if(timer != null)
                {
                    timer.cancel();
                }

                finish();
            }
        });


    }

    private void requestMarketingEventsAPI() {

        String url = ConstsCore.web_url + "/v1/display/dashboard/" + userId;

        //String url = "https://ra.manthan.com/v1/display/dashboard/270389" ;
        Log.i("URL   ", url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("MarketingEvent Response", response.toString());
                        try {


                            if (response.equals(null) || response == null) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(DashBoardActivity.this, "No data found", Toast.LENGTH_LONG).show();
                            } else {
                                for (int i = 0; i < response.length(); i++)
                                {
                                    JSONObject jsonOject = response.getJSONObject(i);
                                    String imageURL = jsonOject.getString("imageName");
                                    // Log.e("imageURL", "\"+""+imageURL+""+\"");
                                    eventUrlList.add(imageURL);

                                }

                                //eventAdapter = new EventAdapter(DashBoardActivity.this, eventUrlList);
                                //style_grid.setAdapter(eventAdapter);
                                EventScroller();




                            }

                        } catch (Exception e) {
                            Log.e("Exception e", e.toString() + "");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        error.printStackTrace();
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer "+bearertoken);

                Log.e("params "," "+params);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);



    }


    private void initializeUI() {


        hourlyFlashTxt=(TextView)findViewById(R.id.headersmdm);
        productInfoTxt=(TextView)findViewById(R.id.productinfo);
        visualAssortTxt=(TextView)findViewById(R.id.visualAssort);
        salesTxt=(TextView)findViewById(R.id.headersales);
        promoAnalysisTxt=(TextView)findViewById(R.id.headerpromo);
        inventoryTxt=(TextView)findViewById(R.id.headerinvent);

        hourlyFlashTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
        productInfoTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
        visualAssortTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
        salesTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
        promoAnalysisTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
        inventoryTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);

        hourlyFlashTxt.setOnClickListener(this);
        productInfoTxt.setOnClickListener(this);
        visualAssortTxt.setOnClickListener(this);
        salesTxt.setOnClickListener(this);
        promoAnalysisTxt.setOnClickListener(this);
        inventoryTxt.setOnClickListener(this);



        hourlyFlash=(LinearLayout)findViewById(R.id.lin1);
        productInfo=(LinearLayout)findViewById(R.id.lineartwo);
        visualAssort=(LinearLayout)findViewById(R.id.linearthree);
        sales=(LinearLayout)findViewById(R.id.lin2);
        promoAnalysis=(LinearLayout)findViewById(R.id.lin3);
        inventory=(LinearLayout)findViewById(R.id.lin4);

        imageBtnStyle=(ImageButton)findViewById(R.id.imageBtnStyle);
        imageBtnKeyProducts=(ImageButton)findViewById(R.id.imageBtnKeyProducts);
        imgBtnSales = (ImageButton) findViewById(R.id.btnSales);
        imgBtnVisualAssortment = (ImageButton) findViewById(R.id.btnVisualAssortment);
        imgBtnPvaAnalysis = (ImageButton) findViewById(R.id.btnPVA);
        imgBtnRunningPromo=(ImageButton)findViewById(R.id.btnRunningpromo);
        BtnUpcomingpromo=(ImageButton)findViewById(R.id.btnUpcomingpromo);
        BtnExpiringpromo=(ImageButton)findViewById(R.id.btnExpiringpromo);
        BtnBestWorstpromo=(ImageButton)findViewById(R.id.btnBestWorstpromo);
        BtnOnlyWorstpromo=(ImageButton)findViewById(R.id.btnOnlyWorstpromo);
        btnFeshnessindex=(ImageButton)findViewById(R.id.btnFeshnessindex);
//        style_grid = (ExpandableHeightGridView) findViewById(R.id.spotsView);
//        style_grid.setExpanded(true);

        pager = (ViewPager) findViewById(R.id.viewpager);


        li = (LinearLayout) findViewById(R.id.lill);
        li.setOrientation(LinearLayout.HORIZONTAL);

        final  ScrollView scrollview=(ScrollView)findViewById(R.id.scrollView);

        scrollview.post(new Runnable() {

            public void run() {
                scrollview.fullScroll(View.FOCUS_UP);
                //scrollview.pageScroll(View.FOCUS_UP);
            }
        });



    }


    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(DashBoardActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        else if(id == R.id.aboutus)
        {
            Intent intent = new Intent(DashBoardActivity.this,AboutUsActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void requestLoginAPI()
    {
        String url = ConstsCore.web_url+"/v1/login";

        final String password = sharedPreferences.getString("password","");
        final String auth_code = sharedPreferences.getString("authcode","");

        Log.e("authcode"," "+auth_code);

        final JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.i("Login   Response   ", response.toString());
                        try
                        {
                            if(response == null || response.equals(null))
                            {
                                Reusable_Functions.hDialog();

                            }

                            String bearerToken = response.getString("bearerToken");
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("bearerToken",bearerToken);
                            editor.apply();

                            //Marketing events API
                            requestMarketingEventsAPI();



                        }
                        catch(Exception e)
                        {
                            Log.e("Exception e",e.toString() +"");
                            e.printStackTrace();
                            Reusable_Functions.hDialog();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Reusable_Functions.hDialog();
                        error.printStackTrace();
                    }
                }

        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                //String auth_code = "Basic " + Base64.encodeToString((uname+":"+password).getBytes(), Base64.NO_WRAP); //Base64.NO_WRAP flag
                Log.i("Auth Code", auth_code);
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", auth_code);
                return params;


            }
        };
        int socketTimeout = 60000;//5 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }


    private void EventScroller()
    {

        Log.e("eventURLLIST"," "+eventUrlList);
        for (int i = 0; i < eventUrlList.size(); i++) {

            imgdot = new ImageView(this);//new View(DashBoardActivity.this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
            layoutParams.setMargins(3, 3, 3, 3);
            imgdot.setLayoutParams(layoutParams);
            //imgdot.setBackgroundColor(Color.parseColor("#666666"));
            imgdot.setImageResource(R.mipmap.dots_unselected);
            li.addView(imgdot);

        }

        // Pass results to ViewPagerAdapter Class
        adapter = new EventPagerAdapter(DashBoardActivity.this, eventUrlList, li, pager);
        // Binds the Adapter to the ViewPager
        pager.setAdapter(adapter);
        Reusable_Functions.hDialog();
        pageSwitcher(10);
    }

    public void pageSwitcher(int seconds) {
        timer = new Timer(); // At this line a new Thread will be created
        timer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 1000); // delay

    }

    @Override
    public void onClick(View v)
    {
        Log.e("on click on ","log");

        switch (v.getId())
        {
          /*  hourlyFlash=(LinearLayout)findViewById(R.id.lin1);
            productInfo=(LinearLayout)findViewById(R.id.lineartwo);
            visualAssort=(LinearLayout)findViewById(R.id.linearthree);
            sales=(LinearLayout)findViewById(R.id.lin2);
            promoAnalysis=(LinearLayout)findViewById(R.id.lin3);
            inventory=(LinearLayout)findViewById(R.id.lin4);*/

          /*  hourlyFlashTxt=(TextView)findViewById(R.id.headersmdm);
            productInfoTxt=(TextView)findViewById(R.id.productinfo);
            visualAssortTxt=(TextView)findViewById(R.id.visualAssort);
            salesTxt=(TextView)findViewById(R.id.headersales);
            promoAnalysisTxt=(TextView)findViewById(R.id.headerpromo);
            inventoryTxt=(TextView)findViewById(R.id.headerinvent);
*/

            case R.id.headersmdm:
                if(hrflash.equals("NO")){
                    hourlyFlash.setVisibility(View.VISIBLE);
                    hourlyFlashTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.uplist,0);


                    hrflash="YES";
                }else
                {
                    hourlyFlash.setVisibility(View.GONE);
                    hourlyFlashTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);

                    hrflash="NO";

                }
                break;

            case R.id.productinfo:
                if(pdInfo.equals("NO")){
                    productInfo.setVisibility(View.VISIBLE);
                    productInfoTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.uplist,0);

                    pdInfo="YES";

                }else
                {
                    productInfo.setVisibility(View.GONE);
                    productInfoTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    pdInfo="NO";


                }
                break;

            case R.id.visualAssort:
                if(vsAssort.equals("NO")){
                    visualAssort.setVisibility(View.VISIBLE);
                    visualAssortTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.uplist,0);
                    vsAssort="YES";

                }else
                {
                    visualAssort.setVisibility(View.GONE);
                    visualAssortTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    vsAssort="NO";

                } break;

            case R.id.headersales:
                if(sAles.equals("NO")){
                    sales.setVisibility(View.VISIBLE);
                    salesTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.uplist,0);
                    sAles="YES";
                }else
                {
                    sales.setVisibility(View.GONE);
                    salesTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    sAles="NO";

                } break;

            case R.id.headerpromo:
                if(pmAnalysis.equals("NO")){
                    promoAnalysis.setVisibility(View.VISIBLE);
                    promoAnalysisTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.uplist,0);
                    pmAnalysis="YES";
                }else
                {
                    promoAnalysis.setVisibility(View.GONE);
                    promoAnalysisTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    pmAnalysis="NO";

                } break;

            case R.id.headerinvent:
                if(inVENtory.equals("NO")){
                    inventory.setVisibility(View.VISIBLE);
                    inventoryTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.uplist,0);
                    inVENtory="YES";
                }else
                {
                    inventory.setVisibility(View.GONE);
                    inventoryTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    inVENtory="NO";


                } break;
        }


    }

    // this is an inner class...
    class RemindTask extends TimerTask {

        @Override
        public void run() {

            // As the TimerTask run on a seprate thread from UI thread we have
            // to call runOnUiThread to do work on UI thread.
            runOnUiThread(new Runnable() {
                public void run() {

                    if (page == eventUrlList.size() - 1) { // In my case the number of pages are 5
                        page = 0;
                        pager.setCurrentItem(0);
                    } else
                    {
                        page = page + 1;
                        pager.setCurrentItem(page);
                    }
                }
            });

        }
    }

}
