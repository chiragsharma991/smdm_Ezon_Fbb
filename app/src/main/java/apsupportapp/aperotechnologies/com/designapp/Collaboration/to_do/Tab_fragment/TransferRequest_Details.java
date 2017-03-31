package apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
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
import com.google.gson.Gson;
import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.AnyOrientationCaptureActivity;
import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Transfer_Request_Model;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.StyleActivity;

/**
 * Created by pamrutkar on 08/03/17.
 */

public class TransferRequest_Details extends AppCompatActivity implements OnPress,View.OnClickListener,OnScanBarcode {

    //Git activity_status_track_one
    private Gson gson;
    private SharedPreferences sharedPreferences;
    private String userId;
    private String bearertoken;
    private String check_adapter_str;
    private int headerAdapterPos, childAdapterPos;
    private Transfer_Request_Model transfer_request_model;
    Context context;
    private int count = 0;
    private int limit = 100;
    private int offsetvalue = 0;
    private RequestQueue queue;
    private String TAG = "TransferRequest_Details";
    private ArrayList<Transfer_Request_Model> Sender_DetailsList, SenderChildDetailList,ScanList;
    RelativeLayout tr_imageBtnBack;
    private RecyclerView tr_recyclerView;
    private int levelOfOption = 1;  //  1 is for option and 2 is for size
    private String option = "";    // code and description
    private TransferDetailsAdapter transferDetailsAdapter;
    private HashMap<Integer,ArrayList<Integer>> TrchildScanQty;
    public static HashMap<Integer, ArrayList<Transfer_Request_Model>> TransferReqHashmapList;
    private TextView txt_caseNo, txt_valtotalreqty;
    private  int[] scanQty;
    private int ScanCount;
    private Button btn_Submit;
    private ProgressBar TransferDetailProcess;
    private String recache,detail_CaseNo,detl_reqStoreCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferreq_details);
        getSupportActionBar().hide();
        context = this;
        initalise();
        ScanCount = 0;
        recache = "true";
        gson = new Gson();
        Sender_DetailsList = new ArrayList<Transfer_Request_Model>();
        ScanList = new ArrayList<Transfer_Request_Model>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Log.e(TAG, "userID and token" + userId + "and this is" + bearertoken);
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();

        if (Reusable_Functions.chkStatus(context))
        {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            requestSenderDetails();
        }
        else
        {
            Toast.makeText(context, "Please check network connection...", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestReceiversChildDetails(final int position)
    {
        String url = ConstsCore.web_url + "/v1/display/stocktransfer/senderdetail/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&level=" + levelOfOption + "&option=" + option.replaceAll(" ", "%20") +"&caseNo="+detail_CaseNo+"&reqStoreCode="+detl_reqStoreCode+"&recache="+recache;
        Log.e(TAG, "Sender Details Child Url" + "" + url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        Log.i(TAG, "Sender Details Child  api response : " + " " + response);
                        Log.i(TAG, "Sender Details Child  total length" + "" + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                TransferDetailProcess.setVisibility(View.GONE);
                                Toast.makeText(TransferRequest_Details.this, "no data found", Toast.LENGTH_SHORT).show();
                                return;

                            } else if (response.length() == limit) {
                                Log.e(TAG, "promo eql limit");
                                for (int i = 0; i < response.length(); i++) {

                                    transfer_request_model = gson.fromJson(response.get(i).toString(), Transfer_Request_Model.class);
                                    SenderChildDetailList.add(transfer_request_model);

                                }
                                offsetvalue = (limit * count) + limit;
                                count++;

                                requestReceiversChildDetails(position);

                            } else if (response.length() < limit) {
                                Log.e(TAG, "promo /= limit");
                                for (int i = 0; i < response.length(); i++) {
                                    transfer_request_model = gson.fromJson(response.get(i).toString(), Transfer_Request_Model.class);
                                    SenderChildDetailList.add(transfer_request_model);
                                }
                            }

                            TransferReqHashmapList.put(position,SenderChildDetailList);
                            PutScanQty(SenderChildDetailList,position);
                            transferDetailsAdapter.notifyDataSetChanged();
                            Reusable_Functions.hDialog();
                            TransferDetailProcess.setVisibility(View.GONE);

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "data failed...." + e.toString(), Toast.LENGTH_SHORT).show();
                            TransferDetailProcess.setVisibility(View.GONE);
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
                        TransferDetailProcess.setVisibility(View.GONE);
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

    private void PutScanQty(ArrayList<Transfer_Request_Model> senderChildDetailList, int position)
    {
        Log.e("child array list size :",""+senderChildDetailList.size());
        ArrayList<Integer>list=new ArrayList<Integer>();

        for (int i = 0; i <senderChildDetailList.size() ; i++) {
            list.add(0);
        }
        TrchildScanQty.put(position,list);
    }

    private void requestSenderDetails() {

        String url = ConstsCore.web_url + "/v1/display/stocktransfer/senderdetail/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&level=" + levelOfOption +"&caseNo="+detail_CaseNo+"&reqStoreCode="+detl_reqStoreCode+"&recache=" + recache;
        Log.e(TAG, "Details Url" + "" + url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, "Detail api response : " + " " + response);
                        Log.i(TAG, "Detail api total length" + "" + response.length());
                        try
                        {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(TransferRequest_Details.this, "no data found", Toast.LENGTH_SHORT).show();
                                return;

                            } else if (response.length() == limit)
                            {
                                Log.e(TAG, "promo eql limit");
                                for (int i = 0; i < response.length(); i++)
                                {
                                    transfer_request_model = gson.fromJson(response.get(i).toString(), Transfer_Request_Model.class);
                                    Sender_DetailsList.add(transfer_request_model);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSenderDetails();

                            } else if (response.length() < limit)
                            {
                                Log.e(TAG, "promo /= limit");
                                for (int i = 0; i < response.length(); i++)
                                {
                                    transfer_request_model = gson.fromJson(response.get(i).toString(), Transfer_Request_Model.class);
                                    Sender_DetailsList.add(transfer_request_model);
                                }
                            }
                            tr_recyclerView.setLayoutManager(new LinearLayoutManager(tr_recyclerView.getContext(), 48 == Gravity.CENTER_HORIZONTAL ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                            tr_recyclerView.setOnFlingListener(null);
                            // new GravitySnapHelper(48).attachToRecyclerView(recyclerView);
                            MakeChildScanList(Sender_DetailsList);
                            MakeScanList(Sender_DetailsList);
                            transferDetailsAdapter = new TransferDetailsAdapter(Sender_DetailsList, context,scanQty,TrchildScanQty,TransferDetailProcess);
                            MakeHashMap(Sender_DetailsList);
                            //MakeSubChildScanQty(Sender_DetailsList);
                            tr_recyclerView.setAdapter(transferDetailsAdapter);
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
                new Response.ErrorListener()
                {
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

    private void MakeChildScanList(ArrayList<Transfer_Request_Model> sender_detailsList) {

        // This is for child scan count
        TrchildScanQty = new HashMap<Integer, ArrayList<Integer>>();

        for (int i = 0; i <sender_detailsList.size() ; i++) {
            ArrayList<Integer> list=new ArrayList<Integer>();
            TrchildScanQty.put(i,list);
        }
        Log.e("sender list size",""+sender_detailsList.size());
    }

//    private void MakeSubChildScanQty(ArrayList<Transfer_Request_Model> sender_detailsList)
//    {
//        childadapter_scanQty=new int[sender_detailsList.size()];
//        for (int i = 0; i <sender_detailsList.size() ; i++) {
//
//            childadapter_scanQty[i]=0;
//        }
//
//    }

    private void MakeScanList(ArrayList<Transfer_Request_Model> sender_detailsList) {

        //   TransReqTotalScanQty = new HashMap<Integer, ArrayList<Transfer_Request_Model>>();

        scanQty=new int[sender_detailsList.size()];
        for (int i = 0; i <sender_detailsList.size() ; i++) {
            scanQty[i]=0;
            //       TransReqTotalScanQty.put(i,sender_detailsList);
        }
    }

    private void MakeHashMap(ArrayList<Transfer_Request_Model> detailsList)
    {
        TransferReqHashmapList = new HashMap<Integer, ArrayList<Transfer_Request_Model>>();
        for (int i = 0; i < detailsList.size(); i++)
        {
            ArrayList<Transfer_Request_Model> listData = new ArrayList<Transfer_Request_Model>();
            TransferReqHashmapList.put(i, listData);
        }
    }

    private void initalise()
    {
        detail_CaseNo = getIntent().getExtras().getString("caseNo");
        double data2 = getIntent().getExtras().getDouble("reqQty");
        detl_reqStoreCode = getIntent().getExtras().getString("reqStoreCode");
        //  MCCodeDesc = data;
        tr_recyclerView = (RecyclerView) findViewById(R.id.trasnsferreq_detail_list);
        tr_imageBtnBack = (RelativeLayout) findViewById(R.id.tr_details_imageBtnBack);
        txt_caseNo = (TextView) findViewById(R.id.txt_caseNo);
        txt_valtotalreqty = (TextView) findViewById(R.id.txt_valtotalreqty);
        btn_Submit = (Button)findViewById(R.id.btn_trdetailSubmit);
        TransferDetailProcess = (ProgressBar)findViewById(R.id.transferDetailProcess);
        txt_caseNo.setText("Case#"+detail_CaseNo);
        txt_valtotalreqty.setText("" + Math.round(data2));
        tr_imageBtnBack.setOnClickListener(this);
        btn_Submit.setOnClickListener(this);
    }

    public void StartActivity(String CaseNo, double reqQty,String reqStoreCode, Context context)
    {
        Intent intent = new Intent(context, TransferRequest_Details.class);
        intent.putExtra("caseNo", CaseNo);
        intent.putExtra("reqQty", reqQty);
        intent.putExtra("reqStoreCode",reqStoreCode);
        context.startActivity(intent);
    }

    @Override
    public void OnPress(int position) {
//        MCCodeDesc = DetailsList.get(position).getMccodeDesc();
        option = Sender_DetailsList.get(position).getLevel();
        levelOfOption = 2;
        SenderChildDetailList = new ArrayList<Transfer_Request_Model>();
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.sDialog(TransferRequest_Details.this, "Loading....");
            TransferDetailProcess.setVisibility(View.VISIBLE);
            requestReceiversChildDetails(position);
        } else {
            Toast.makeText(context, "Please check network connection...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tr_details_imageBtnBack:
                onBackPressed();
                break;
            case R.id.btn_trdetailSubmit:
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



    @Override
    public void onScan(View view, TransferDetailsAdapter transferDetailsAdapter) {
//        if(check.equals("HeaderAdapter"))
////        {
////            check_adapter_str = check;
////            headerAdapterPos = position;
////        }
////        else
////        {
//            check_adapter_str = check;
//        //childAdapterPos = position;
//        // Log.e("String check :",""+check +"\tchildAdapterPos:"+position);
//
//        //  }
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Place a barcode inside the viewfinder rectangle to scan it");
        integrator.setOrientationLocked(true);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
        transferDetailsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null)
            {
                Toast.makeText(this, "Barcode not scanned", Toast.LENGTH_LONG).show();
            } else
            {
                Toast.makeText(this, "Barcode Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
//                if(check_adapter_str.equals("HeaderAdapter"))
//                {
//                    ScanCount++;
//                    TransferDetailsAdapter.headeradapter_scanQty[headerAdapterPos]=ScanCount;
//                    transferDetailsAdapter.notifyDataSetChanged();
//                    Log.e("Scan Count ",""+ ScanCount);
//                }
//                else if(check_adapter_str.equals("ChildAdapter"))
//                {

                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.hDialog();
                        Reusable_Functions.sDialog(context, "Loading  data...");
                        requestScanDetailsAPI(result.getContents());

                    } else {
                        Toast.makeText(TransferRequest_Details.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
                    }
              //  }
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void requestScanDetailsAPI(String contents) {

        String url = ConstsCore.web_url + "/v1/display/stocktransfer/senderscan/scan/" + userId + "?eanNumber="+contents +"&recache=" + recache;
        Log.e(TAG, "Details Url" + "" + url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG,"Scan api response : " + " " + response);
                        Log.e(TAG,"Scan api total length :" + "" + response.length());
                        try
                        {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(TransferRequest_Details.this, "no data found", Toast.LENGTH_SHORT).show();
                                return;

                            } else if (response.length() == limit) {
                                Log.e(TAG, "promo eql limit");
                                for (int i = 0; i < response.length(); i++) {

                                    transfer_request_model = gson.fromJson(response.get(i).toString(), Transfer_Request_Model.class);
                                    ScanList.add(transfer_request_model);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSenderDetails();

                            }
                            else if (response.length() < limit)
                            {
                                for (int i = 0; i < response.length(); i++) {
                                    transfer_request_model = gson.fromJson(response.get(i).toString(), Transfer_Request_Model.class);
                                    ScanList.add(transfer_request_model);
                                }
                                String size= ScanList.get(0).getProdAttribute4();
                                int size_pos = 0;

                                for(int i = 0;i < SenderChildDetailList.size();i++)
                                {
                                    if(SenderChildDetailList.get(i).getLevel().equals("4-5"))
                                    {

                                        size_pos=i;
                                        Log.e("position :",""+i+"\tsize_pos :"+size_pos);
                                        ScanCount++;
                                        scanQty[size_pos] = ScanCount;
                                        Log.e("Child Scan Count ---", "" + ScanCount);
                                        ArrayList<Integer> childlist = new ArrayList<Integer>();
                                        childlist.add(ScanCount);
                                        TrchildScanQty.put(size_pos, childlist);
                                        Log.e("childAdapterPos", "" + TrchildScanQty.get(size_pos));
                                        transferDetailsAdapter.notifyDataSetChanged();
                                        Log.e("list size -----",""+childlist.size());
                                        break;
                                    }
                                    else
                                    {
                                        Log.e("Came","here");
                                    }
                                }
//                                if(SenderChildDetailList.get(childAdapterPos).getLevel().equals(ScanList.get(childAdapterPos).getProdAttribute4()))
//                                {
//                                    Log.e("Level :",""+SenderChildDetailList.get(childAdapterPos).getLevel()+"\tProdAttribute4:"+ScanList.get(childAdapterPos).getProdAttribute4());
//
//                                }

                            }
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
                new Response.ErrorListener()
                {
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



}
