package apsupportapp.aperotechnologies.com.designapp.CustomerLoyalty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.communication.IOnItemFocusChangedListener;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;



import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.ToDoViewPagerAdapter;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;

/**
 * Created by pamrutkar on 21/06/17.
 */
public class CustomerDetailActivity extends AppCompatActivity {
    private TextView txt_cust_name, txt_cust_mobileNo, txt_cust_email;
    private TextView txt_cd_last_visit_Val, txt_cd_tot_visit_Val, txt_cd_last_spent_Val, txt_cd_tot_spent_Val;
    static String unique_Customer;
    private Button btn_more;
    private Context context;
    JsonArrayRequest postRequest;
    RequestQueue queue;
    CustomerDetail customer_Details;
    CustomerRecomdtn customerRecomdtn;
    SharedPreferences sharedPreferences;
    private RelativeLayout rel_cust_detl_back;
    private String userId, bearertoken, geoLeveLDesc, engagementFor = "EVENT", update_userId, recache = "true";
    Gson gson;
    private int offset = 0, limit = 10, count = 0;
    static ArrayList<CustomerDetail> customerDetailsarray;
    static ArrayList<CustomerRecomdtn> customerDetailArrayList;
    private PieChart pieChart;
    private ViewPager cd_viewPager;
    private TabLayout cd_tab;
    private LinearLayout phn_call,mail_call;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);
        getSupportActionBar().hide();
        context = this;
        unique_Customer = getIntent().getStringExtra("uniqueCustomer");
        Log.e("unique_Customer", "" + unique_Customer);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = sharedPreferences.getString("userId", "");
        update_userId = userId.substring(0, userId.length() - 5);
        Log.e("update_userId", "" + update_userId);
        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLeveLDesc = sharedPreferences.getString("geoLeveLDesc", "");
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();
        initialise_UI();

        if (Reusable_Functions.chkStatus(context))
        {
            Reusable_Functions.sDialog(context, "Loading...");
            customerDetailsarray = new ArrayList<CustomerDetail>();
            requestCustomerDetailsAPI();
        }
        else
        {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
        rel_cust_detl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerDetailActivity.this,CustDetailBarChart.class);
                startActivity(intent);
            }
        });
    }

    private void addTabs(ViewPager viewPager)
    {
        ToDoViewPagerAdapter adapter = new ToDoViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OffersOnly(), "Offer Only For You");
        adapter.addFragment(new LastShop(), "Last Shopped");
        viewPager.setAdapter(adapter);
    }

    private void initialise_UI()
    {
        customerDetailArrayList = new ArrayList<CustomerRecomdtn>();
        customerDetailsarray = new ArrayList<CustomerDetail>();
        txt_cust_name = (TextView) findViewById(R.id.txt_cust_name);
        txt_cust_mobileNo = (TextView) findViewById(R.id.txt_cust_mobileNo);
        txt_cust_email = (TextView) findViewById(R.id.txt_cust_email);
        txt_cd_last_visit_Val = (TextView) findViewById(R.id.txt_cd_last_visit_Val);
        txt_cd_tot_visit_Val = (TextView) findViewById(R.id.txt_cd_tot_visit_Val);
        txt_cd_last_spent_Val = (TextView) findViewById(R.id.txt_cd_last_spent_Val);
        txt_cd_tot_spent_Val = (TextView) findViewById(R.id.txt_cd_tot_spent_Val);
        phn_call = (LinearLayout) findViewById(R.id.lin_one);
        mail_call = (LinearLayout) findViewById(R.id.lin_two);
        pieChart = (PieChart) findViewById(R.id.cust_piechart);
        cd_viewPager = (ViewPager)findViewById(R.id.viewpager_custDetails);
        cd_tab = (TabLayout)findViewById(R.id.tabs_custDetails);
        rel_cust_detl_back = (RelativeLayout)findViewById(R.id.rel_cust_detl_back);
        btn_more = (Button)findViewById(R.id.btn_cd_more);

        phn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt_cust_mobileNo.getTextSize()!=0){

                    Intent callIntent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+txt_cust_mobileNo.getText().toString()));
                    startActivity(callIntent);
                }

            }
        });
        mail_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt_cust_email.getTextSize()!=0){

                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto:"+txt_cust_email.getText().toString()));
                    startActivity(Intent.createChooser(emailIntent, "Send feedback"));
                }

            }
        });
    }

    private void requestCustomerDetailsAPI()
    {
        String url = ConstsCore.web_url + "/v1/display/customerdetails/" + update_userId + "?engagementFor=" + engagementFor + "&uniqueCustomer=" + unique_Customer + "&recache=" + recache;
        Log.e("cust details  url ", "" + url);
        postRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                Log.e("response details:", "" + response);
                try {
                    int i = 0;
                    if (response.equals("") || response == null || response.length() == 0 && count == 0)
                    {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        for (i = 0; i < response.length(); i++)
                        {
                            customer_Details = gson.fromJson(response.get(i).toString(), CustomerDetail.class);
                            customerDetailsarray.add(customer_Details);
                        }
                    }
                    txt_cust_name.setText(customerDetailsarray.get(0).getFullName());
                    txt_cust_email.setText(customerDetailsarray.get(0).getEmailAddress());
                    txt_cust_mobileNo.setText(customerDetailsarray.get(0).getMobileNumber());
                    txt_cd_last_spent_Val.setText("₹ " + Math.round(customerDetailsarray.get(0).getLastSpend()));
                    txt_cd_last_visit_Val.setText(customerDetailsarray.get(0).getLastPurchaseDate());
                    txt_cd_tot_spent_Val.setText("₹ " + Math.round(customerDetailsarray.get(0).getLast12MthSpend()));
                    txt_cd_tot_visit_Val.setText("" + Math.round(customerDetailsarray.get(0).getLast12MthVisit()));
                    createPieChart();
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.sDialog(context, "Loading...");
                        customerDetailArrayList = new ArrayList<CustomerRecomdtn>();
                        requestCustomerRecomdtn();
                        addTabs(cd_viewPager);
                        cd_viewPager.setOffscreenPageLimit(2);
                        cd_tab.setupWithViewPager(cd_viewPager);
                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }

                    Reusable_Functions.hDialog();
                }
                catch (Exception e)
                {
                    Reusable_Functions.hDialog();
                    Toast.makeText(context, "no data found" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
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
            public Map<String, String> getHeaders() throws AuthFailureError
            {
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

    private void requestCustomerRecomdtn()
    {
        String url = ConstsCore.web_url + "/v1/display/customerreco/" + update_userId + "?engagementFor=" + engagementFor + "&uniqueCustomer=" + unique_Customer +"&recache="+ recache;
        Log.e("cust summary url ", "" + url);
        postRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("response :", "" + response);
                try {
                    int i = 0;
                    if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();

                    } else
                    {
                        for (i = 0; i < 3; i++)
                        {
                            customerRecomdtn =gson.fromJson(response.get(i).toString(), CustomerRecomdtn.class);
                            customerDetailArrayList.add(customerRecomdtn);
                            Log.e("cust recomdtn :",""+customerDetailArrayList.size());
                        }
                    }



                } catch (Exception e) {
                    Reusable_Functions.hDialog();
                    Toast.makeText(context, "no data found" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
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
    }


    private void createPieChart()
    {
        pieChart.addPieSlice(new PieModel("Food", (int)customerDetailsarray.get(0).getFoodContr(), Color.parseColor("#4e7aa7")));
        pieChart.addPieSlice(new PieModel("Fashion", (int) customerDetailsarray.get(0).getFashionContr(), Color.parseColor("#f28e2c"))); //CDA67F
        pieChart.addPieSlice(new PieModel("Home", (int) customerDetailsarray.get(0).getHomeContr(), Color.parseColor("#a6a6a6"))); //CDA67F
        pieChart.addPieSlice(new PieModel("Electronics", (int) customerDetailsarray.get(0).getElectronicsContr(), Color.parseColor("#fbd043"))); //CDA67F
        pieChart.animate();
        pieChart.setDrawValueInPie(true);
        pieChart.setOnItemFocusChangedListener(new IOnItemFocusChangedListener()
        {
            @Override
            public void onItemFocusChanged(int _Position)
            {
                Log.e("onItemFocusChanged: ", "" + _Position);
                switch (_Position)
                {
                    case 0 :
                        pieChart.setInnerValueString(""+Math.round(customerDetailsarray.get(0).getFoodContr())+"%");
                        break;
                    case 1 :
                        pieChart.setInnerValueString(""+Math.round(customerDetailsarray.get(0).getFashionContr())+"%");
                        break;
                    case 2 :
                        pieChart.setInnerValueString(""+Math.round(customerDetailsarray.get(0).getHomeContr())+"%");
                        break;
                    case 3 :
                        pieChart.setInnerValueString(""+Math.round(customerDetailsarray.get(0).getElectronicsContr())+"%");
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        customerDetailsarray.clear();
        unique_Customer = "";
        finish();
    }
}
