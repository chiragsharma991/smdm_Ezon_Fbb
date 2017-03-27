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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.ToDo_Modal;
import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.To_Do;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.RecyclerItemClickListener;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.model.VisualAssort;


public class Details extends AppCompatActivity implements OnPress,View.OnClickListener,OnSelectedItem {

    private Gson gson;
    private SharedPreferences sharedPreferences;
    private String userId;
    private String bearertoken;
    private ToDo_Modal toDo_Modal;
    Context context ;
    private int count = 0;
    private int limit = 100;
    private int offsetvalue = 0;
    private RequestQueue queue;
    private String TAG = "ToDo_Fregment";
    private ArrayList<ToDo_Modal> DetailsList, ChildDetailList;
    RelativeLayout details_imageBtnBack;
    private ToDo_Modal toDo_modal;
    private RecyclerView recyclerView;
    private int levelOfOption = 1;  //  1 is for option and 2 is for size
    private String MCCodeDesc = "";    // code and description
    private String MCCode = "";    // code and description
    private String option = "";    // code and description
    private StockDetailsAdapter stockPullAdapter;
    private LinearLayout detailsLinear;
    public  HashMap<Integer, ArrayList<ToDo_Modal>> HashmapList;
    private TextView Todo_detailStoreCode;
    private TextView Todo_detailStoreAvlQty;
    private ProgressBar DetailProcess;
    private Button btn_receiver_submit;
    String option_header,mcCodeDesc_header,prodAttr_header;
    private int selectedPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getSupportActionBar().hide();
        context = this;
        initalise();

        //  PromoListView.addFooterView(getLayoutInflater().inflate(R.layout.list_footer, null));
        gson = new Gson();
        DetailsList = new ArrayList<ToDo_Modal>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Log.e(TAG, "userID and token" + userId + "and this is" + bearertoken);
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();

        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();

            Reusable_Functions.sDialog(context, "Loading data...");
            requestReceiversDetails();
        } else {
            Toast.makeText(context, "Please check network connection...", Toast.LENGTH_SHORT).show();
        }

    }

    private void requestReceiversChildDetails(final int position) {

        String url = ConstsCore.web_url + "/v1/display/stocktransfer/receiverdetail/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&level=" + levelOfOption + "&MCCodeDesc=" + MCCodeDesc.replaceAll(" ", "%20") + "&option=" + option.replaceAll(" ", "%20");

        Log.e(TAG, "Details Url" + "" + url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, "Detail api response : " + " " + response);
                        Log.i(TAG, "Detail api total length" + "" + response.length());


                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(Details.this, "no data found", Toast.LENGTH_SHORT).show();
                                DetailProcess.setVisibility(View.GONE);
                                return;

                            } else if (response.length() == limit) {
                                Log.e(TAG, "promo eql limit");
                                for (int i = 0; i < response.length(); i++) {

                                    toDo_modal = gson.fromJson(response.get(i).toString(), ToDo_Modal.class);
                                    ChildDetailList.add(toDo_modal);


                                }
                                offsetvalue = (limit * count) + limit;
                                count++;


                                requestReceiversChildDetails(position);

                            } else if (response.length() < limit) {
                                Log.e(TAG, "promo /= limit");
                                for (int i = 0; i < response.length(); i++) {
                                    toDo_modal = gson.fromJson(response.get(i).toString(), ToDo_Modal.class);
                                    ChildDetailList.add(toDo_modal);
                                }
                                count = 0;
                                limit = 100;
                                offsetvalue = 0;
                            }

                            HashmapList.put(position, ChildDetailList);
                            stockPullAdapter.notifyDataSetChanged();
                            Reusable_Functions.hDialog();
                            DetailProcess.setVisibility(View.GONE);

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "data failed...." + e.toString(), Toast.LENGTH_SHORT).show();
                            Reusable_Functions.hDialog();
                            DetailProcess.setVisibility(View.GONE);
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


    private void requestReceiversDetails() {

        String url = ConstsCore.web_url + "/v1/display/stocktransfer/receiverdetail/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&level=" + levelOfOption + "&MCCodeDesc=" + MCCodeDesc.replaceAll(" ", "%20");
        Log.e(TAG, "Details Url" + "" + url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, "Detail api response : " + " " + response);
                        Log.i(TAG, "Detail api total length" + "" + response.length());


                        try {
                            int i;
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(Details.this, "no data found", Toast.LENGTH_SHORT).show();
                                return;

                            } else if (response.length() == limit) {
                                Log.e(TAG, "promo eql limit");
                                for (i = 0; i < response.length(); i++) {

                                    toDo_modal = gson.fromJson(response.get(i).toString(), ToDo_Modal.class);
                                    DetailsList.add(toDo_modal);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;


                                requestReceiversDetails();

                            } else if (response.length() < limit) {
                                Log.e(TAG, "promo /= limit");
                                for ( i = 0; i < response.length(); i++) {
                                    toDo_modal = gson.fromJson(response.get(i).toString(), ToDo_Modal.class);
                                    DetailsList.add(toDo_modal);


                                }

                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), 48 == Gravity.CENTER_HORIZONTAL ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                            recyclerView.setOnFlingListener(null);
                            // new GravitySnapHelper(48).attachToRecyclerView(recyclerView);
                            MakeHashMap(DetailsList);
                            stockPullAdapter = new StockDetailsAdapter(DetailsList,HashmapList, context,DetailProcess);
                            recyclerView.setAdapter(stockPullAdapter);

                            Reusable_Functions.hDialog();
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
        Reusable_Functions.hDialog();


    }

    private void MakeHashMap(ArrayList<ToDo_Modal> detailsList) {

        HashmapList = new HashMap<Integer, ArrayList<ToDo_Modal>>();

        for (int i = 0; i < detailsList.size(); i++) {
            ArrayList<ToDo_Modal> listData = new ArrayList<ToDo_Modal>();
            HashmapList.put(i, listData);
        }

    }

    private void initalise() {

        String data1 = getIntent().getExtras().getString("MCCodeDesc");
        Double data2 = getIntent().getExtras().getDouble("AvlQty");
        MCCodeDesc = data1;
        MCCode=String.valueOf(Math.round(data2));
        recyclerView = (RecyclerView) findViewById(R.id.stockDetail_list);
        details_imageBtnBack = (RelativeLayout)findViewById(R.id.details_imageBtnBack);
        Todo_detailStoreCode = (TextView)findViewById(R.id.todo_detailStoreCode);
        Todo_detailStoreAvlQty = (TextView)findViewById(R.id.todo_detailStoreAvlQty);
        btn_receiver_submit = (Button)findViewById(R.id.stock_detailSubmit);
        DetailProcess = (ProgressBar)findViewById(R.id.detailProcess);
        Todo_detailStoreCode.setText(MCCodeDesc);
        Todo_detailStoreAvlQty.setText(MCCode);
        details_imageBtnBack.setOnClickListener(this);
        btn_receiver_submit.setOnClickListener(this);
//        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//                if (StockDetailsAdapter.Toggle[position] == true) {
//                    StockDetailsAdapter.Toggle[position] = false;
//                    stockPullAdapter.notifyDataSetChanged();
//                } else {
//                    StockDetailsAdapter.Toggle[position] = true;
//                    MCCodeDesc = DetailsList.get(position).getMccodeDesc();
//                    option = DetailsList.get(position).getLevel();
//                    levelOfOption = 2;
//                    ChildDetailList = new ArrayList<ToDo_Modal>();
//                    if (Reusable_Functions.chkStatus(context)) {
//                        Reusable_Functions.sDialog(context, "Loading.......");
//                        requestReceiversChildDetails(position);
//                    } else {
//                        Toast.makeText(context, "Please check network connection...", Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//
//            }
//        }));

    }


    public void StartActivity(Context context) {
        context.startActivity(new Intent(context, Details.class));
    }

    public void StartActivity(Context context, String data1,Double data2) {
        Intent intent = new Intent(context, Details.class);
        intent.putExtra("MCCodeDesc", data1);
        intent.putExtra("AvlQty", data2);
        context.startActivity(intent);
    }

    @Override
    public void OnPress(int position) {
        MCCodeDesc = DetailsList.get(position).getMccodeDesc();
        option = DetailsList.get(position).getLevel();
        levelOfOption = 2;
        ChildDetailList = new ArrayList<ToDo_Modal>();
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.sDialog(Details.this, "Loading....");
            DetailProcess.setVisibility(View.VISIBLE);
            requestReceiversChildDetails(position);
        } else {
            Toast.makeText(context, "Please check network connection...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.details_imageBtnBack:
                onBackPressed();
                break;

            case R.id.stock_detailSubmit:

                onSelected(selectedPosition);

//                JSONArray jsonarray =  new JSONArray();
//                try {
////            for (int i = position; i < DetailsList.size(); i++) {
//                    JSONObject obj = new JSONObject();
//
//                    obj.put("option", DetailsList.get(selectedPosition).getOption());
//                    obj.put("prodAttribute4", "");
//                    obj.put("prodLevel6Code",  DetailsList.get(selectedPosition).getMccodeDesc());
//                    jsonarray.put(selectedPosition, obj);
//
////            }
//                    if (Reusable_Functions.chkStatus(context))
//                    {
//                        Reusable_Functions.sDialog(Details.this, "Loading...");
//                        requestReceiverSubmitAPI(context, jsonarray);
//                    }
//                    else
//                    {
//                        Toast.makeText(context, "Please check network connection...", Toast.LENGTH_SHORT).show();
//                    }
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                    break;
        }

    }


    private void requestReceiverSubmitAPI(final Context mcontext, JSONArray object)
    {
        String url = ConstsCore.web_url + "/v1/save/stocktransfer/receiversubmitdetail/" + userId;
        Log.e("url", " post Request " + url + " ==== " + object.toString());
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, object.toString(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Submit Click Response :", response.toString());
                        try {
                            if (response == null || response.equals(null)) {
                                Reusable_Functions.hDialog();
                            } else {
                                  Toast.makeText(mcontext, "Data is saved successfully", Toast.LENGTH_SHORT).show();
                                Reusable_Functions.hDialog();
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
                params.put("Authorization", bearertoken);
              //  params.put("Content-Type", "application/json");
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    public void onSelected(int position) {
        selectedPosition = position;
        Log.e("Selected Position :",""+selectedPosition);

    }
}
