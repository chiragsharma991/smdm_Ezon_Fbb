package apsupportapp.aperotechnologies.com.designapp.CustomerEngagement;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import org.json.JSONObject;

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
    private TextView txt_cust_name, txt_cust_mobileNo, txt_cust_email,txt_pie_ach;
    private TextView txt_cd_last_visit_Val, txt_cd_tot_visit_Val, txt_cd_last_spent_Val, txt_cd_tot_spent_Val;
    String unique_Customer;
    private Button btn_more, btn_reset;
    private Context context;
    JsonArrayRequest postRequest;
    RequestQueue queue;
    CustomerDetail customer_Details;
    CustomerRecomdtn customerRecomdtn;
    SharedPreferences sharedPreferences;
    private RelativeLayout rel_cust_detl_back;
    private String userId, bearertoken, geoLeveLDesc, engagementFor = "EVENT", update_userId, recache = "true", businessCcb,geoLevel2Code,lobId;
    Gson gson;
    private int offset = 0, limit = 10, count = 0;
    static ArrayList<CustomerDetail> customerDetailsarray;
    static ArrayList<CustomerRecomdtn> customerDetailArrayList;
    private ArrayList<CustomerDetail> detailList;
    private PieChart pieChart;
    private ViewPager cd_viewPager;
    private TabLayout cd_tab;
    private LinearLayout phn_call, mail_call, linear_legend;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private NestedScrollView nestedScrollView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);
        getSupportActionBar().hide();
        context = this;
        unique_Customer = getIntent().getStringExtra("uniqueCustomer");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = sharedPreferences.getString("userId", "");

        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLeveLDesc = sharedPreferences.getString("geoLeveLDesc", "");
        geoLevel2Code = sharedPreferences.getString("concept","");
        lobId = sharedPreferences.getString("lobid","");
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();
        initialise_UI();

        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.sDialog(context, "Loading...");
            customerDetailArrayList = new ArrayList<CustomerRecomdtn>();
            requestCustomerRecomdtn();

        } else {
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
                Intent intent = new Intent(CustomerDetailActivity.this, CustDetailBarChart.class);
                startActivity(intent);
            }
        });
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_cd_last_spent_Val.setText("");
                txt_cd_last_visit_Val.setText("");
                txt_cd_tot_spent_Val.setText("");
                txt_cd_tot_visit_Val.setText("");
              //  pieChart.setCurrentItem(0);
                if (Reusable_Functions.chkStatus(context))
                {
                    Reusable_Functions.sDialog(context, "Loading...");
                    requestResetCustomerDetailAPI();
                }
                else
                {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addTabs(ViewPager viewPager)
    {
        ToDoViewPagerAdapter adapter = new ToDoViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OffersOnly(), "Recommendations");
        adapter.addFragment(new LastShop(), "Last Shopped");
        viewPager.setAdapter(adapter);
    }

    private void initialise_UI() {
        customerDetailArrayList = new ArrayList<CustomerRecomdtn>();
        customerDetailsarray = new ArrayList<CustomerDetail>();
        detailList = new ArrayList<CustomerDetail>();
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
        cd_viewPager = (ViewPager) findViewById(R.id.viewpager_custDetails);
        cd_tab = (TabLayout) findViewById(R.id.tabs_custDetails);
        rel_cust_detl_back = (RelativeLayout) findViewById(R.id.rel_cust_detl_back);
        btn_more = (Button) findViewById(R.id.btn_cd_more);
        btn_reset = (Button) findViewById(R.id.btn_cd_reset);
        txt_pie_ach  = (TextView)findViewById(R.id.txt_pie_ach);
//        nestedScrollView = (NestedScrollView)findViewById(R.id.nested_scrollView);
//        nestedScrollView.setFillViewport(true);
        linear_legend = (LinearLayout) findViewById(R.id.linear_legend);

        phn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_cust_mobileNo.getTextSize() != 0) {
                    makePhoneCall(v);
                }

            }
        });
        mail_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_cust_email.getTextSize() == 0 || txt_cust_email.getText().equals("N/A")) {
                    return;
                } else {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto:" + txt_cust_email.getText().toString()));
                    startActivity(Intent.createChooser(emailIntent, "Send feedback"));
                }

            }
        });
    }

    private void makePhoneCall(View v) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            callPhone();
        }
    }

    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + txt_cust_mobileNo.getText().toString()));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callPhone();
                }
            }
        }
    }

    private void requestCustomerDetailsAPI() {
        String url = ConstsCore.web_url + "/v1/display/customerdetails/" + userId + "?engagementFor=" + engagementFor + "&uniqueCustomer=" + unique_Customer + "&recache=" + recache+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;

        postRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    int i = 0;
                    if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else
                    {
                        for (i = 0; i < response.length(); i++) {
                            customer_Details = gson.fromJson(response.get(i).toString(), CustomerDetail.class);
                            customerDetailsarray.add(customer_Details);
                        }
                    }
                    txt_cust_name.setText(customerDetailsarray.get(0).getFullName());
                    txt_cust_email.setText(customerDetailsarray.get(0).getEmailAddress());
                    txt_cust_mobileNo.setText(customerDetailsarray.get(0).getMobileNumber());
                    fillTopLayout();
                    createPieChart();
                    addTabs(cd_viewPager);
                    cd_viewPager.setOffscreenPageLimit(2);
                    cd_tab.setupWithViewPager(cd_viewPager);
                    Reusable_Functions.hDialog();

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

    private void requestResetCustomerDetailAPI()
    {
        String url = ConstsCore.web_url + "/v1/display/customerdetails/" + userId + "?engagementFor=" + engagementFor + "&uniqueCustomer=" + unique_Customer + "&recache=" + recache+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;

        postRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    int i = 0;
                    if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        for (i = 0; i < response.length(); i++)
                        {
                            JSONObject resp_obj = response.getJSONObject(i);
                            String lastPurchaseDate = resp_obj.getString("lastPurchaseDate");
                            double last12MthVisit = resp_obj.getDouble("last12MthVisit");
                            double lastSpend = resp_obj.getDouble("lastSpend");
                            double last12MthSpend = resp_obj.getDouble("last12MthSpend");
                            NumberFormat format = NumberFormat.getNumberInstance(new Locale("", "in"));
                            txt_cd_last_spent_Val.setText("₹ " + Math.round(lastSpend));
                            txt_cd_last_spent_Val.setTextColor(Color.parseColor("#404040"));
                            txt_cd_last_visit_Val.setText(lastPurchaseDate);
                            txt_cd_last_visit_Val.setTextColor(Color.parseColor("#404040"));
                            txt_cd_tot_spent_Val.setText("₹ " + format.format(Math.round( last12MthSpend)));
                            txt_cd_tot_spent_Val.setTextColor(Color.parseColor("#404040"));
                            txt_cd_tot_visit_Val.setText("" + Math.round(last12MthVisit));
                            txt_cd_tot_visit_Val.setTextColor(Color.parseColor("#404040"));
                        }
                    }
                   Reusable_Functions.hDialog();

                } catch (Exception e)
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

    private void fillTopLayout()
    {
        NumberFormat format = NumberFormat.getNumberInstance(new Locale("", "in"));
        txt_cd_last_spent_Val.setText("₹ " + Math.round(customerDetailsarray.get(0).getLastSpend()));
        txt_cd_last_spent_Val.setTextColor(Color.parseColor("#404040"));
        txt_cd_last_visit_Val.setText(customerDetailsarray.get(0).getLastPurchaseDate());
        txt_cd_last_visit_Val.setTextColor(Color.parseColor("#404040"));
        txt_cd_tot_spent_Val.setText("₹ " + format.format(Math.round(customerDetailsarray.get(0).getLast12MthSpend())));
        txt_cd_tot_spent_Val.setTextColor(Color.parseColor("#404040"));
        txt_cd_tot_visit_Val.setText("" + Math.round(customerDetailsarray.get(0).getLast12MthVisit()));
        txt_cd_tot_visit_Val.setTextColor(Color.parseColor("#404040"));
    }

    private void requestCustomerRecomdtn() {
        String url = ConstsCore.web_url + "/v1/display/customerreco/" + userId + "?engagementFor=" + engagementFor + "&uniqueCustomer=" + unique_Customer + "&recache=" + recache+"&geoLevel2Code="+ geoLevel2Code + "&lobId="+ lobId;

        postRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    int i = 0;
                    if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                    } else {
                        for (i = 0; i < 3; i++) {
                            customerRecomdtn = gson.fromJson(response.get(i).toString(), CustomerRecomdtn.class);
                            customerDetailArrayList.add(customerRecomdtn);

                        }
                    }
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.sDialog(context, "Loading...");
                        customerDetailsarray = new ArrayList<CustomerDetail>();
                        requestCustomerDetailsAPI();
                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
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
        pieChart.addPieSlice(new PieModel("Food", (int) customerDetailsarray.get(0).getFoodContr(), Color.parseColor("#20b5d3")));
        pieChart.addPieSlice(new PieModel("Fashion", (int) customerDetailsarray.get(0).getFashionContr(), Color.parseColor("#21d24c"))); //CDA67F
        pieChart.addPieSlice(new PieModel("Home", (int) customerDetailsarray.get(0).getHomeContr(), Color.parseColor("#f5204c"))); //CDA67F
        pieChart.addPieSlice(new PieModel("Electronics", (int) customerDetailsarray.get(0).getElectronicsContr(), Color.parseColor("#f89a20"))); //CDA67F
        pieChart.animate();
        txt_pie_ach.setVisibility(View.VISIBLE);
        addLegendLayout();

        pieChart.setDrawValueInPie(false);
        pieChart.setOnItemFocusChangedListener(new IOnItemFocusChangedListener() {
            @Override
            public void onItemFocusChanged(int _Position) {

                switch (_Position) {
                    case 0:
                        //  pieChart.setInnerValueString("" + Math.round(customerDetailsarray.get(0).getFoodContr()) + "%");
                        businessCcb = "Food";
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.sDialog(context, "Loading...");
                        requestPieChartOnFocus();
                        txt_cd_last_spent_Val.setTextColor(Color.parseColor("#20b5d3"));
                        txt_cd_last_visit_Val.setTextColor(Color.parseColor("#20b5d3"));
                        txt_cd_tot_spent_Val.setTextColor(Color.parseColor("#20b5d3"));
                        txt_cd_tot_visit_Val.setTextColor(Color.parseColor("#20b5d3"));
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 1:
                        // pieChart.setInnerValueString("" + Math.round(customerDetailsarray.get(0).getFashionContr()) + "%");
                        businessCcb = "Fashion";
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.sDialog(context, "Loading...");
                            requestPieChartOnFocus();
                        txt_cd_last_spent_Val.setTextColor(Color.parseColor("#21d24c"));
                        txt_cd_last_visit_Val.setTextColor(Color.parseColor("#21d24c"));
                        txt_cd_tot_spent_Val.setTextColor(Color.parseColor("#21d24c"));
                        txt_cd_tot_visit_Val.setTextColor(Color.parseColor("#21d24c"));
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:
                        // pieChart.setInnerValueString("" + Math.round(customerDetailsarray.get(0).getHomeContr()) + "%");
                        businessCcb = "Home";
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.sDialog(context, "Loading...");
                            requestPieChartOnFocus();
                        txt_cd_last_spent_Val.setTextColor(Color.parseColor("#f5204c"));
                        txt_cd_last_visit_Val.setTextColor(Color.parseColor("#f5204c"));
                        txt_cd_tot_spent_Val.setTextColor(Color.parseColor("#f5204c"));
                        txt_cd_tot_visit_Val.setTextColor(Color.parseColor("#f5204c"));
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 3:
                        // pieChart.setInnerValueString("" + Math.round(customerDetailsarray.get(0).getElectronicsContr()) + "%");
                        businessCcb = "Electronics";
                        if (Reusable_Functions.chkStatus(context)) {
                            Reusable_Functions.sDialog(context, "Loading...");
                            requestPieChartOnFocus();
                        txt_cd_last_spent_Val.setTextColor(Color.parseColor("#f89a20"));
                        txt_cd_last_visit_Val.setTextColor(Color.parseColor("#f89a20"));
                        txt_cd_tot_spent_Val.setTextColor(Color.parseColor("#f89a20"));
                        txt_cd_tot_visit_Val.setTextColor(Color.parseColor("#f89a20"));
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });
    }

    private void addLegendLayout()
    {
        LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup view = (ViewGroup) layoutInflater.inflate(R.layout.activity_pie_legend, null);
        //Legend 1
        TextView txt_legend_color1 = (TextView) view.findViewById(R.id.txt_legend_color1);
        TextView txt_legend_name1 = (TextView) view.findViewById(R.id.txt_legend1);
        TextView txt_legend_val1 = (TextView) view.findViewById(R.id.txt_legend_val1);
        txt_legend_color1.setBackgroundColor(Color.parseColor("#20b5d3"));
        txt_legend_name1.setText("Food");
        txt_legend_val1.setText("" + Math.round(customerDetailsarray.get(0).getFoodContr()));
        //Legend 2
        TextView txt_legend_color2 = (TextView) view.findViewById(R.id.txt_legend_color2);
        TextView txt_legend_name2 = (TextView) view.findViewById(R.id.txt_legend2);
        TextView txt_legend_val2 = (TextView) view.findViewById(R.id.txt_legend_val2);
        txt_legend_color2.setBackgroundColor(Color.parseColor("#21d24c"));
        txt_legend_name2.setText("Fashion");
        txt_legend_val2.setText("" + Math.round(customerDetailsarray.get(0).getFashionContr()));
        //Legend 3
        TextView txt_legend_color3 = (TextView) view.findViewById(R.id.txt_legend_color3);
        TextView txt_legend_name3 = (TextView) view.findViewById(R.id.txt_legend3);
        TextView txt_legend_val3 = (TextView) view.findViewById(R.id.txt_legend_val3);
        txt_legend_color3.setBackgroundColor(Color.parseColor("#f5204c"));
        txt_legend_name3.setText("Home");
        txt_legend_val3.setText("" + Math.round(customerDetailsarray.get(0).getHomeContr()));
        //Legend 4
        TextView txt_legend_color4 = (TextView) view.findViewById(R.id.txt_legend_color4);
        TextView txt_legend_name4 = (TextView) view.findViewById(R.id.txt_legend4);
        TextView txt_legend_val4 = (TextView) view.findViewById(R.id.txt_legend_val4);
        txt_legend_color4.setBackgroundColor(Color.parseColor("#f89a20"));
        txt_legend_name4.setText("Electronics");
        txt_legend_val4.setText("" + Math.round(customerDetailsarray.get(0).getElectronicsContr()));
        linear_legend.addView(view);
    }

    private void requestPieChartOnFocus()
    {
        String url = "";
        if (businessCcb.equals("Food")) {
            url = ConstsCore.web_url + "/v1/display/customercontribution/" + userId + "?engagementFor=" + engagementFor + "&uniqueCustomer=" + unique_Customer + "&businessCcb=" + businessCcb+"&geoLevel2Code="+ geoLevel2Code;
        } else if (businessCcb.equals("Fashion")) {
            url = ConstsCore.web_url + "/v1/display/customercontribution/" + userId + "?engagementFor=" + engagementFor + "&uniqueCustomer=" + unique_Customer + "&businessCcb=" + businessCcb+"&geoLevel2Code="+ geoLevel2Code;
        } else if (businessCcb.equals("Home")) {
            url = ConstsCore.web_url + "/v1/display/customercontribution/" + userId + "?engagementFor=" + engagementFor + "&uniqueCustomer=" + unique_Customer + "&businessCcb=" + businessCcb+"&geoLevel2Code="+ geoLevel2Code;
        } else if (businessCcb.equals("Electronics")) {
            url = ConstsCore.web_url + "/v1/display/customercontribution/" + userId + "?engagementFor=" + engagementFor + "&uniqueCustomer=" + unique_Customer + "&businessCcb=" + businessCcb+"&geoLevel2Code="+ geoLevel2Code;
        }


        postRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    int i = 0;
                    if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        for (i = 0; i < response.length(); i++) {
                            JSONObject resp_obj = response.getJSONObject(i);
                            String businessCcb = resp_obj.getString("businessCcb");
                            String lastPurchaseDate = resp_obj.getString("lastPurchaseDate");
                            double last12MthSpend = resp_obj.getDouble("last12MthSpend");
                            double last12MthVisit = resp_obj.getDouble("last12MthVisit");
                            double lastSpend = resp_obj.getDouble("lastSpend");
                            txt_cd_last_spent_Val.setText("₹ " + Math.round(lastSpend));
                            txt_cd_last_visit_Val.setText(lastPurchaseDate);
                            txt_cd_tot_spent_Val.setText("₹ " + Math.round(last12MthSpend));
                            txt_cd_tot_visit_Val.setText("" + Math.round(last12MthVisit));
                        }
                    }
                    Reusable_Functions.hDialog();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
