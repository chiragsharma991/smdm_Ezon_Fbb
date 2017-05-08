package apsupportapp.aperotechnologies.com.designapp.RunningPromo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import apsupportapp.aperotechnologies.com.designapp.MySingleton;
import apsupportapp.aperotechnologies.com.designapp.OnRowPressListener;
import apsupportapp.aperotechnologies.com.designapp.HorlyAnalysis.ProductNameBean;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;

public class RunningPromoDetail extends AppCompatActivity {


    ViewGroup view;
    ArrayList<ProductNameBean> productNameBeanArrayList;
    RequestQueue queue;
    //passData data;
    Context context;

    RelativeLayout relativeLayout;
    public static RelativeLayout relProd_Frag;
    String userId, bearertoken;
    String storeDesc, storeCode;
    MySingleton m_config;
    int offsetvalue = 0, limit = 100;
    int count = 0;
    PopupWindow popupWindow;
    OnRowPressListener rowPressListener;
    Spinner subDeptList;
    SharedPreferences sharedPreferences;
    private String NetPercent;
    String productSubDeptItem;
    String f_productName;
    TextView txt_subdepName;
    String TAG = "RunningPromoDetails";


    RunningPromoListDisplay runningPromoListDisplay;
    ArrayList<RunningPromoListDisplay> promoList;
    Gson gson;
    String data;
    RelativeLayout ImageBtnBack;
    private TextView txtHeader;
    private TableLayout tableLayout;
    private TableRow tablerow;
    private LayoutInflater layoutInflater;
    private RelativeLayout backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_promo_detail);
        getSupportActionBar().hide();
        // getSupportActionBar().setTitle("Running Promo Details");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;


        //Intent i = getActivity().getIntent();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RunningPromoDetail.this.getBaseContext());
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        m_config = MySingleton.getInstance(RunningPromoDetail.this);
        gson = new Gson();
        promoList = new ArrayList<>();
        findview();

        Cache cache = new DiskBasedCache(RunningPromoDetail.this.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();


        //relProd_Frag = (RelativeLayout) findViewById(R.id.rel);
        // relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        //  relativeLayout.setBackgroundColor(Color.WHITE);
        // relProd_Frag.setVisibility(View.VISIBLE);


        if (Reusable_Functions.chkStatus(context)) {

            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            offsetvalue = 0;
            limit = 100;
            count = 0;

            requestProductAPI(offsetvalue, limit);

        } else {
            Toast.makeText(RunningPromoDetail.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
        }


        tableRow();
    }
    private void tableRow() {
    }

    private void findview()
    {
        backButton=(RelativeLayout)findViewById(R.id.rpd_ImageBtnBack);
        TextView textView=(TextView)findViewById(R.id.promoCat);
        data=getIntent().getExtras().getString("VM");
        textView.setText(data);
        //tableLayout=(TableLayout)findViewById(R.id.tableRunningPromo);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    private void requestProductAPI(int offsetvalue1, int limit1) {


        String url = ConstsCore.web_url + "/v1/display/runningpromodetails/" + userId + "?offset=" + offsetvalue + "&limit=" + limit;
        Log.i(TAG,"URL   "+ url + " " + bearertoken);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, "Running promo : " + " " + response);
                        Log.i(TAG, "Sales View Pager response" + "" + response.length());

                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(RunningPromoDetail.this, "no product data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {
                                //Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    runningPromoListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                    promoList.add(runningPromoListDisplay);
                                    Log.e(TAG,"Promolist in limit");

                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestProductAPI(offsetvalue, limit);
                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    runningPromoListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                    promoList.add(runningPromoListDisplay);
                                    Log.e(TAG,"Promolist size"+promoList.size());

                                }

                                //   txtHeader.setText(data.getString("VM"));

//                                Collections.sort(productNameBeanArrayList, new Comparator<ProductNameBean>() {
//                                    public int compare(ProductNameBean one, ProductNameBean other) {
//                                        return  new Integer(one.getWtdNetSales()).compareTo(new Integer(other.getWtdNetSales()));
//                                    }
//                                });
//                                Collections.reverse(productNameBeanArrayList);

                                //txtStoreCode.setText(promoList.get(0).getStoreCode());
                                //txtStoreDesc.setText(promoList.get(0).getStoreDesc());
                                addTexttoTable();
                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            e.printStackTrace();
                            Log.e(TAG, "catch...Error" +e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        // Toast.makeText(LoginActivity.this,"Invalid User",Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + bearertoken);

                Log.e("params ", " " + params);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void addTexttoTable()
    {
        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.linearTable);
        for (int i = 0; i <promoList.size() ; i++) {

            layoutInflater = (LayoutInflater) getApplicationContext()
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            view = (ViewGroup) layoutInflater.inflate(R.layout.activity_runningpromotable_child, null);
            TextView Mc=(TextView)view.findViewById(R.id.mccc);
            TextView Prodsale=(TextView)view.findViewById(R.id.prodSale);
            TextView ProdSaleU=(TextView)view.findViewById(R.id.prodSaleU);
            TextView SoH=(TextView)view.findViewById(R.id.soH);
            Mc.setText(promoList.get(i).getProdLevel6Desc());
            Prodsale.setText("\u20B9\t"+(int)promoList.get(i).getDurSaleNetVal());
            ProdSaleU.setText(""+promoList.get(i).getDurSaleTotQty());
            SoH.setText(""+promoList.get(i).getStkOnhandQty());
            Reusable_Functions.hDialog();
            linearLayout.addView(view);

        }





    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}










