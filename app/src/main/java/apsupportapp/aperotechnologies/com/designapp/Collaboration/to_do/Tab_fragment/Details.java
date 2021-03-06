package apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.ToDo_Modal;
import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.To_Do;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;

import static apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.To_Do.activity;


public class Details extends AppCompatActivity implements OnPress, View.OnClickListener {

    private Gson gson;
    private SharedPreferences sharedPreferences;
    private String userId;
    private String bearertoken,deviceId;
    Context context;
    private int count = 0;
    private int limit = 100;
    private int offsetvalue = 0;
    private RequestQueue queue;
    private ArrayList<ToDo_Modal> DetailsList, ChildDetailList;
    RelativeLayout details_imageBtnBack;
    private ToDo_Modal toDo_modal;
    private RecyclerView recyclerView;
    private int levelOfOption = 3;  //  3 is for option and 4 is for size
    private String MCCodeDesc = "",prodLevel3Desc = "",MC_name_code = "";    // code and description
    private double MCCode ;    // code and description
    private String option = "", store_code;    // code and description
    private StockDetailsAdapter stockDetailsAdapter;
    Button btn_selectAll,btn_reset;


    public HashMap<Integer, ArrayList<ToDo_Modal>> HashmapList;
    private TextView Todo_detailStoreCode;
    private TextView Todo_detailStoreAvlQty;
    private ProgressBar DetailProcess;
    private Button btn_receiver_submit;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getSupportActionBar().hide();
        context = this;
        initalise();
        gson = new Gson();
        DetailsList = new ArrayList<ToDo_Modal>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        deviceId = sharedPreferences.getString("device_id","");
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();

        if (Reusable_Functions.chkStatus(context))
        {
            Reusable_Functions.sDialog(context, "Loading data...");
            DetailProcess.setVisibility(View.VISIBLE);
            requestReceiversDetails();
        }
        else
        {
            Toast.makeText(context, "Please check network connection...", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestReceiversChildDetails(final int position)
    {

        prodLevel3Desc = prodLevel3Desc.replace(" ", "%20").replace("&", "%26");
        MC_name_code = MC_name_code.replace(" ", "%20").replace("&", "%26");
        option = option.replace(" ", "%20").replace("&", "%26");

        String url = ConstsCore.web_url + "/v1/display/stocktransfer/receiverdetail/" + userId + "?storeCode=" +store_code+ "&offset=" + offsetvalue + "&limit=" + limit + "&level=" + levelOfOption + "&MCCodeDesc=" + MC_name_code +"&prodLevel3Desc=" + prodLevel3Desc.replaceAll(" ","%20")+"&option=" + option.replaceAll(" ", "%20") +"&recache=true";
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(Details.this, "no data found", Toast.LENGTH_SHORT).show();
                                StockDetailsAdapter.Holder.view_border.setVisibility(View.GONE);
                                DetailProcess.setVisibility(View.GONE);

                                return;

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    toDo_modal = gson.fromJson(response.get(i).toString(), ToDo_Modal.class);
                                    ChildDetailList.add(toDo_modal);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestReceiversChildDetails(position);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    toDo_modal = gson.fromJson(response.get(i).toString(), ToDo_Modal.class);
                                    ChildDetailList.add(toDo_modal);
                                }
                                count = 0;
                                limit = 100;
                                offsetvalue = 0;
                            }
                            StockDetailsAdapter.Holder.view_border.setVisibility(View.VISIBLE);

                            HashmapList.put(position, ChildDetailList);
                            stockDetailsAdapter.notifyDataSetChanged();
                            Reusable_Functions.hDialog();
                            DetailProcess.setVisibility(View.GONE);

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "data failed...." + e.toString(), Toast.LENGTH_SHORT).show();
                            Reusable_Functions.hDialog();
                            StockDetailsAdapter.Holder.view_border.setVisibility(View.GONE);
                            DetailProcess.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "server not responding..", Toast.LENGTH_SHORT).show();
                        Reusable_Functions.hDialog();
                        StockDetailsAdapter.Holder.view_border.setVisibility(View.GONE);
                        DetailProcess.setVisibility(View.GONE);
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
        Reusable_Functions.hDialog();
    }


    private void requestReceiversDetails()
    {

        prodLevel3Desc = prodLevel3Desc.replace(" ", "%20").replace("&", "%26");
        MC_name_code = MC_name_code.replace(" ", "%20").replace("&", "%26");
      //  MCCodeDesc = MCCodeDesc.replace(" ", "%20").replace("&", "%26");
        String url = ConstsCore.web_url + "/v1/display/stocktransfer/receiverdetail/" + userId +"?storeCode="+store_code+ "&offset=" + offsetvalue + "&limit=" + limit + "&level=" + levelOfOption + "&MCCodeDesc=" + MC_name_code+"&prodLevel3Desc=" + prodLevel3Desc.replaceAll(" ","%20") +"&recache=true";
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                DetailProcess.setVisibility(View.GONE);
                                Toast.makeText(Details.this, "no data found", Toast.LENGTH_SHORT).show();
                                return;

                            } else if (response.length() == limit)
                            {
                                for (int i = 0; i < response.length(); i++) {
                                    toDo_modal = gson.fromJson(response.get(i).toString(), ToDo_Modal.class);
                                    DetailsList.add(toDo_modal);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestReceiversDetails();
                            }
                            else if (response.length() < limit)
                            {
                                for (int i = 0; i < response.length(); i++)
                                {
                                    toDo_modal = gson.fromJson(response.get(i).toString(), ToDo_Modal.class);
                                    DetailsList.add(toDo_modal);
                                }
                                count = 0;
                                limit = 100;
                                offsetvalue = 0;
                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), 48 == Gravity.CENTER_HORIZONTAL ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                            recyclerView.setOnFlingListener(null);
                            MakeHashMap(DetailsList);
                            stockDetailsAdapter = new StockDetailsAdapter(DetailsList, HashmapList, context, DetailProcess);
                            recyclerView.setAdapter(stockDetailsAdapter);
                            DetailProcess.setVisibility(View.GONE);
                            Reusable_Functions.hDialog();
                        }
                        catch (Exception e)
                        {
                            Reusable_Functions.hDialog();
                            DetailProcess.setVisibility(View.GONE);
                            Toast.makeText(context, "data failed..." + e.toString(), Toast.LENGTH_SHORT).show();
                            Reusable_Functions.hDialog();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        DetailProcess.setVisibility(View.GONE);
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "server not responding...", Toast.LENGTH_SHORT).show();
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
        Reusable_Functions.hDialog();
    }

    private void MakeHashMap(ArrayList<ToDo_Modal> detailsList) {

        HashmapList = new HashMap<Integer, ArrayList<ToDo_Modal>>();

        for (int i = 0; i < detailsList.size(); i++) {
            ArrayList<ToDo_Modal> listData = new ArrayList<ToDo_Modal>();
            HashmapList.put(i, listData);
        }
    }

    private void initalise()
    {
        String subcategory = getIntent().getExtras().getString("prodLevel3Desc");
        String mc = getIntent().getExtras().getString("MCCodeDesc");
        Double data2 = getIntent().getExtras().getDouble("AvlQty");
        store_code = getIntent().getExtras().getString("storeCode");
        String mc_name_code = getIntent().getExtras().getString("MCCode");
        MC_name_code = mc_name_code;
        MCCodeDesc = mc; //MCCodeDesc = "2257-Ladies Ethnic Kurta" ; // this is used to disaply values //  - this is actual value
        prodLevel3Desc = subcategory; // prodLevel3Desc = "BF011C-BF - Ladies ethnicwear"; // - to display values  // - this is actual value
        MCCode = (Math.round(data2));
        btn_selectAll = (Button)findViewById(R.id.btn_stock_selectAll);
        btn_reset = (Button)findViewById(R.id.btn_stock_reset);
        recyclerView = (RecyclerView) findViewById(R.id.stockDetail_list);
        details_imageBtnBack = (RelativeLayout) findViewById(R.id.details_imageBtnBack);
        btn_receiver_submit = (Button) findViewById(R.id.stock_detailSubmit);
        Todo_detailStoreCode = (TextView) findViewById(R.id.todo_detailStoreCode);
        Todo_detailStoreAvlQty = (TextView) findViewById(R.id.todo_detailStoreAvlQty);
        DetailProcess = (ProgressBar) findViewById(R.id.detailProcess);
        Todo_detailStoreCode.setText(MCCodeDesc);
        Todo_detailStoreAvlQty.setText(" " + Math.round(MCCode));
        details_imageBtnBack.setOnClickListener(this);
        btn_receiver_submit.setOnClickListener(this);
        btn_selectAll.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                for (int i = 0; i < DetailsList.size();i++)
                {
                    stockDetailsAdapter.HeadercheckList[i]= true;
                    stockDetailsAdapter.visibleItems[i]=true;

                }
                stockDetailsAdapter.notifyDataSetChanged();


            }
        });
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i <DetailsList.size();i++)
                {
                    stockDetailsAdapter.HeadercheckList[i] = false;
                    stockDetailsAdapter.visibleItems[i] = true;
                    stockDetailsAdapter.selectedSizeList.clear();
                    stockDetailsAdapter.selectedOptionList.clear();
                }
                stockDetailsAdapter.notifyDataSetChanged();
            }
        });

    }


    public void StartActivity(Context context, String subcategory, String mc, String mc_name_code, Double data2, String storeCode)
    {
        Intent intent = new Intent(context, Details.class);
        intent.putExtra("prodLevel3Desc",subcategory);
        intent.putExtra("MCCodeDesc", mc);
        intent.putExtra("storeCode", storeCode);
        intent.putExtra("MCCode",mc_name_code);
        intent.putExtra("AvlQty", data2);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.details_imageBtnBack:
                onBackPressed();
                break;

            case R.id.stock_detailSubmit:
                if (!(DetailsList.size() == 0)) {
                    JSONArray jsonArray = stockDetailsAdapter.OnSubmit(MC_name_code, prodLevel3Desc, store_code);
                    if (jsonArray.length() == 0) {
                        Toast.makeText(Details.this, "Please select at least one option.", Toast.LENGTH_SHORT).show();
                    } else {
                        requestReceiverSubmitAPI(context, jsonArray);
                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }


    private void requestReceiverSubmitAPI(final Context mcontext, JSONArray object)
    {
        if (Reusable_Functions.chkStatus(mcontext))
        {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(mcontext, "Submitting data…");

            String url = ConstsCore.web_url + "/v1/save/stocktransfer/receiversubmitdetail/" + userId;//+"?recache="+recache
            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, object.toString(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                if (response == null || response.equals("")) {
                                    Reusable_Functions.hDialog();
                                    Toast.makeText(mcontext, "Sending data failed...", Toast.LENGTH_LONG).show();

                                }
                                else
                                {
                                    String result = response.getString("status");
                                    Toast.makeText(mcontext, "" + result, Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Details.this, To_Do.class);
                                    intent.putExtra("from",store_code);
                                    intent.putExtra("selectTab","");

                                    startActivity(intent);
                                    Reusable_Functions.hDialog();
                                    activity.finish();
                                    finish();
                                }
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                                Reusable_Functions.hDialog();

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "server not responding...", Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization", bearertoken);
                    return params;
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }

            };
            int socketTimeout = 60000;//5 seconds
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            postRequest.setRetryPolicy(policy);
            queue.add(postRequest);


        } else {
            Toast.makeText(context, "Please check network connection...", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onPress(int Position)
    {
        option = DetailsList.get(Position).getLevelCode();
        levelOfOption = 4; // 4 is for size
        ChildDetailList = new ArrayList<ToDo_Modal>();
        if (Reusable_Functions.chkStatus(context))
        {
            Reusable_Functions.sDialog(Details.this, "Loading...");
            DetailProcess.setVisibility(View.VISIBLE);
            requestReceiversChildDetails(Position);
        }
        else
        {
            Toast.makeText(context, "Please check network connection...", Toast.LENGTH_SHORT).show();
        }
    }
}
