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

import apsupportapp.aperotechnologies.com.designapp.PvaSalesAnalysis.SalesPvAActivity;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesAnalysisActivity;
import apsupportapp.aperotechnologies.com.designapp.VisualAssortmentSwipe.VisualAssortmentActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class DashBoardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageButton imageBtnStyle, imageBtnKeyProducts, imgBtnSales, imgBtnVisualAssortment , imgBtnPvaAnalysis;
    //ExpandableHeightGridView style_grid;
    EventAdapter eventAdapter;
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
        imageBtnStyle=(ImageButton)findViewById(R.id.imageBtnStyle);
        imageBtnKeyProducts=(ImageButton)findViewById(R.id.imageBtnKeyProducts);
        imgBtnSales = (ImageButton) findViewById(R.id.btnSales);
        imgBtnVisualAssortment = (ImageButton) findViewById(R.id.btnVisualAssortment);
        imgBtnPvaAnalysis = (ImageButton) findViewById(R.id.btnPVA);
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
