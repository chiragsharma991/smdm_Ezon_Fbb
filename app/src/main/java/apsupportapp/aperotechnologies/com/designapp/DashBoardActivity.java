package apsupportapp.aperotechnologies.com.designapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DashBoardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageButton imageBtnStyle,imageBtnKeyProducts;
    ExpandableHeightGridView style_grid;
    Integer listItem[] = new Integer[]{R.drawable.futuregroup, R.drawable.futuregroup, R.drawable.futuregroup};
    EventAdapter eventAdapter;
    RequestQueue queue;
    String userId;
   ArrayList<String> arrayList;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context=this;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();

        arrayList=new ArrayList<>();


        //to call Collection API
        //at merging
//        Bundle bundle=getIntent().getExtras();
//        userId=bundle.getString("userId");
//        Log.d("userId","  "+userId);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initializeUI();

      eventAdapter=new EventAdapter(DashBoardActivity.this,listItem);
      style_grid.setAdapter(eventAdapter);


        imageBtnStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//                requestCollectionAPI();
//                requestArticleOptionsAPI();

                Intent intent=new Intent(DashBoardActivity.this,StyleActivity.class);
                intent.putExtra("userId",userId);
                startActivity(intent);

            }
        });


        imageBtnKeyProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (Reusable_Functions.chkStatus(context))
                {
                   // Reusable_Functions.sDialog(context,"fetching data");
                    requestProductAPI();
                }else {

                }

//               } requestArticleOptionsAPI();

                Intent intent=new Intent(DashBoardActivity.this,KeyProductActivity.class);
               // intent.putExtra("userId",userId);
                startActivity(intent);

            }
        });


    }



    private void initializeUI() {
        imageBtnStyle=(ImageButton)findViewById(R.id.imageBtnStyle);
        imageBtnKeyProducts=(ImageButton)findViewById(R.id.imageBtnKeyProducts);

       // imageBtnStyle.setOnClickListener(this);
        style_grid = (ExpandableHeightGridView) findViewById(R.id.spotsView);
//        style_grid.setOnClickListener(this);
        style_grid.setExpanded(true);

        final  ScrollView scrollview=(ScrollView)findViewById(R.id.scrollView);

        scrollview.post(new Runnable() {

            public void run() {
                scrollview.fullScroll(View.FOCUS_UP);
                //scrollview.pageScroll(View.FOCUS_UP);
            }
        });

    }









    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void requestProductAPI()
    {
        String url="https://ra.manthan.com/v1/display/hourlyproductdetails/9";
        Log.i("URL   ", url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        Log.i("ProductName Response   ", response.toString());
                        try
                        {
                            // JSONObject mainObject = new JSONObject(response);
                            for(int i=0;i<response.length();i++)
                            {
                                JSONObject productName = response.getJSONObject(i);
                                String ProductName = productName.getString("productName");
                                String L2Hrs_Net_Sales= productName.getString("last2HourSaleTotQty");
                                String Day_Net_Sales=productName.getString("fordaySaleTotQty");
                                String WTD_Net_Sales=productName.getString("wtdSaleTotQty");
                                String Day_Net_Sales_Percent=productName.getString("fordayPvaSalesUnitsPercent");
                                String WTD_Net_Sales_Percent=productName.getString("wtdPvaSalesUnitsPercent");
                                String SOH=productName.getString("stkOnhandQty");
                                String GIT= productName.getString("stkGitQty");
                                String option=productName.getString("articleOption");

                                ProductNameBean productNameBean= new ProductNameBean();
                                productNameBean.setProductName(ProductName);
                                productNameBean.setL2hrsNetSales(L2Hrs_Net_Sales);
                                productNameBean.setDayNetSales(Day_Net_Sales);
                                productNameBean.setDayNetSalesPercent(Day_Net_Sales_Percent);
                                productNameBean.setWtdNetSales(WTD_Net_Sales);
                                productNameBean.setWtdNetSalesPercent(WTD_Net_Sales_Percent);
                                productNameBean.setSoh(SOH);
                                productNameBean.setGit(GIT);
                                productNameBean.setArticleOption(option);

                                //Reusable_Functions.hDialog();

                                Intent productIntent= new Intent(DashBoardActivity.this,KeyProductActivity.class) ;
                                productIntent.putExtra("productNameBean",productNameBean);
                                startActivity(productIntent);


                                Log.e("Product Name",ProductName);Log.e("L2Hrs_Net_Sales:",L2Hrs_Net_Sales);


                            }
//                               Intent intent = new Intent(DashBoardActivity.this, StyleActivity.class);
//                               intent.putExtra("arrayList",arrayList);
//                                startActivity(intent);


                        }
                        catch(Exception e)
                        {
                            Log.e("Exception e",e.toString() +"");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Reusable_Functions.hDialog();
                        // Toast.makeText(LoginActivity.this,"Invalid User",Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }

        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }




}
