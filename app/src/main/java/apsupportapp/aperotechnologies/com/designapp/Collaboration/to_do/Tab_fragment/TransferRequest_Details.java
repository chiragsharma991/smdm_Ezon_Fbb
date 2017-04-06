package apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import apsupportapp.aperotechnologies.com.designapp.AnyOrientationCaptureActivity;
import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.To_Do;
import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Transfer_Request_Model;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;

import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;

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
    private ArrayList<Integer> totalScanQty;
    private TransferDetailsAdapter transferDetailsAdapter;
    private HashMap<Integer,ArrayList<Integer>> TrchildScanQty;
    private TextView txt_caseNo, txt_valtotalreqty;
//    private  int[] scanQty;
//    private int ScanCount;
    private boolean scanDone=false;
    private Button btn_Submit;
    private ProgressBar TransferDetailProcess;
    private String recache,detail_CaseNo,detl_reqStoreCode;
    private HashMap<Integer, ArrayList<Transfer_Request_Model>> subchildqty;    //for sub child list
    private HashMap<Integer, ArrayList<Integer>> subchildcount;    //for sub child count
    private HashMap<Integer, ArrayList<Integer>> headerScancount;    //for Header scan qty
    private HashSet<Pair<Integer, Integer>> CheckedItems;
    private static final int CAMERA_PERMISSION = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{Manifest.permission.CAMERA,
          };
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferreq_details);
        getSupportActionBar().hide();
        context = this;
        initalise();
       // ScanCount = 0;
        recache = "true";
        gson = new Gson();
        Sender_DetailsList = new ArrayList<Transfer_Request_Model>();
        ScanList = new ArrayList<Transfer_Request_Model>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);
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
                        Log.e(TAG, "Sender Details Child  api response : " + " " + response);
                        Log.e(TAG, "Sender Details Child  total length" + "" + response.length());

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

                            addScanCount(SenderChildDetailList,position);
                            subchildqty.put(position,SenderChildDetailList);
                          //  PutScanQty(SenderChildDetailList,position);
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



    private void addScanCount(ArrayList<Transfer_Request_Model> senderChildDetailList, int position)
    {
        ArrayList<Integer> scanCount = new ArrayList<Integer>();
        for (int i = 0; i <senderChildDetailList.size() ; i++) {
            scanCount.add(0);
        }
        subchildcount.put(position,scanCount);
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
                            MakeChildScanList(Sender_DetailsList);   //child all list
                            MakeSubChildScanCount(Sender_DetailsList);  //only child scan count
                            MakeHeaderScanCount(Sender_DetailsList);  // Top Header scan count
                            MakeCheckPair();  // Top Header scan count
                          //  MakeScanList(Sender_DetailsList);
                            transferDetailsAdapter = new TransferDetailsAdapter(Sender_DetailsList, context,subchildqty,subchildcount,TransferDetailProcess,headerScancount,TransferRequest_Details.this,CheckedItems);
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

    private void MakeCheckPair()
    {
        CheckedItems=new HashSet<Pair<Integer, Integer>>();

    }

    private void MakeScanList(ArrayList<Transfer_Request_Model> sender_detailsList) {

        // This is for child scan count
        TrchildScanQty = new HashMap<Integer, ArrayList<Integer>>();

        for (int i = 0; i <sender_detailsList.size() ; i++) {
            ArrayList<Integer> list=new ArrayList<Integer>();
            TrchildScanQty.put(i,list);
        }
        Log.e("sender list size",""+sender_detailsList.size());
    }

    private void MakeSubChildScanCount(ArrayList<Transfer_Request_Model> sender_detailsList)
    {
        subchildcount=new HashMap<>();
        for (int i = 0; i <sender_detailsList.size() ; i++)
        {
            ArrayList<Integer> list=new ArrayList<Integer>();
            subchildcount.put(i,list);

        }

    }
    private void MakeHeaderScanCount(ArrayList<Transfer_Request_Model> sender_detailsList)
    {
        headerScancount=new HashMap<>();
        for (int i = 0; i <sender_detailsList.size() ; i++)
        {
            ArrayList<Integer> list=new ArrayList<Integer>();
            list.add(0);
            headerScancount.put(i,list);

        }

    }

    private void MakeChildScanList(ArrayList<Transfer_Request_Model> sender_detailsList) {

     subchildqty=new HashMap<>();
        for (int i = 0; i <sender_detailsList.size() ; i++)
        {
            ArrayList<Transfer_Request_Model> list=new ArrayList<Transfer_Request_Model>();
            subchildqty.put(i,list);

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
                putMethod();
                break;

        }
    }

    private void putMethod()
    {
        JSONArray jsonarray=new JSONArray();
        int count=0;

        for (int i = 0; i <Sender_DetailsList.size(); i++)  //all list of details
        {
            for (int j = 0; j <subchildqty.get(i).size() ; j++)  // all sizes of one option
            {
                Pair<Integer, Integer> Tag = new Pair<Integer, Integer>(i,j);  //check if any check box is check another wise it will not proceed.
                if(CheckedItems.contains(Tag))
                {
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("option",Sender_DetailsList.get(i).getLevel());
                        obj.put("prodAttribute4",subchildqty.get(i).get(j).getLevel());
                        obj.put("caseNo",detail_CaseNo);
                        jsonarray.put(count,obj);
                        count++;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
        }
        }
        Log.e(TAG, "putMethod: Json Array is:"+jsonarray.toString() );
        if(jsonarray.length()==0)
        {
            Toast.makeText(context,"Please select at least one size.",Toast.LENGTH_SHORT).show();

        }else
        {
            requestSenderSubmitAPI(context,jsonarray);

            // Toast.makeText(context,"Data submission successfully",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onScan(View view, int position, TransferDetailsAdapter transferDetailsAdapter)
    {
        if (ActivityCompat.checkSelfPermission(TransferRequest_Details.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(TransferRequest_Details.this, Manifest.permission.CAMERA)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(TransferRequest_Details.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(TransferRequest_Details.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(Manifest.permission.CAMERA,false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(TransferRequest_Details.this,R.style.AppCompatAlertDialogStyle);
                builder.setTitle("Camera Permission");
                builder.setMessage("This app needs camera permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else
            {
                //just request the permission
                ActivityCompat.requestPermissions(TransferRequest_Details.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
            }
            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.CAMERA,true);
            editor.commit();

        } else
        {
            //You already have the permission, just go ahead.
        }
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
        if(requestCode == CAMERA_PERMISSION)
        {

        }
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null)
        {
            if (result.getContents() == null)
            {
               // Toast.makeText(this, "Barcode not scanned", Toast.LENGTH_LONG).show();
            } else
            {
                Toast.makeText(this, "Barcode Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.hDialog();
                        Reusable_Functions.sDialog(context, "Loading  data...");
                        requestScanDetailsAPI(result.getContents());

                    } else {
                        Toast.makeText(TransferRequest_Details.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
                    }

            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
               // proceedAfterPermission();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(TransferRequest_Details.this, Manifest.permission.CAMERA)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(TransferRequest_Details.this);
                    builder.setTitle("Need Camera Permission");
                    builder.setMessage("This app needs camera permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(TransferRequest_Details.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(getBaseContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
                }
            }
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
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0)
                            {
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

                                ArrayList<Integer>addQty=new ArrayList<>();

                                for(int i = 0;i < SenderChildDetailList.size();i++) //find sizeitem  position from child list
                                {
                                    if(SenderChildDetailList.get(i).getLevel().equals("15-16"))//add size instead of 4-5 value
                                    {
                                        //        maxScanQty = (int) Math.round(list.get(PrePosition).get(position).getStkOnhandQtyRequested());

                                        scanDone=true;
                                        if(subchildqty.get(0).get(i).getStkOnhandQtyRequested() > subchildcount.get(0).get(i))   //"0"is for Pre position
                                        {
                                            size_pos=i;
                                            Log.e("position :",""+i+"\tsize_pos :"+size_pos);
                                            int count=subchildcount.get(0).get(i)+1;  //pre count + next count  && "0"is pre position you have to change
                                            addQty.add(count);  //total sub count
                                            // subchildcount.put(i,total);
                                            Log.e("i position :",""+i);

                                        }else
                                        {
                                            int count=subchildcount.get(0).get(i);
                                            addQty.add(count);
                                            Toast.makeText(context,"Scan Qty should be less than Req.Qty",Toast.LENGTH_LONG).show();

                                        }
                                        break;
                                    }
                                    else
                                    {
                                        int count=subchildcount.get(0).get(i);
                                        addQty.add(count);
                                    }
                                }

                                subchildcount.put(0,addQty);//0 is pre position
                                addtotalIn_headerScanqty(0);
                                transferDetailsAdapter.notifyDataSetChanged();

                                if(scanDone==false){
                                    Toast.makeText(context,"Barcode does not match sizes...",Toast.LENGTH_SHORT).show();
                                }else {
                                    scanDone=false;
                                }


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

    public void addtotalIn_headerScanqty(int position) {
        ArrayList<Integer>count=new ArrayList<Integer>();
        int totalHeaderqty=0;
        for (int i = 0; i <subchildqty.get(position).size(); i++) {
             totalHeaderqty +=subchildcount.get(position).get(i);   // position is Preposition
        }
        count.add(totalHeaderqty);
        headerScancount.put(position,count);
    }

    private void requestSenderSubmitAPI(final Context mcontext, JSONArray object)  // Sender Submit Api call
    {

        if (Reusable_Functions.chkStatus(mcontext)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(mcontext, "Submitting data…");

            String url = ConstsCore.web_url + "/v1/save/stocktransfer/sendersubmit/" + userId ;//+"?recache="+recache
            Log.e("url", " put Request " + url + " ==== " + object.toString());
            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.PUT, url, object.toString(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("Sender Submit Click Response :", response.toString());
                            try {
                                if (response == null || response.equals(null)) {
                                    Reusable_Functions.hDialog();
                                    Toast.makeText(mcontext,"Sending data failed...", Toast.LENGTH_LONG).show();

                                } else
                                {
                                    String result=response.getString("status");
                                    Toast.makeText(mcontext,""+result, Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(TransferRequest_Details.this,To_Do.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(intent);
                                    //Details.this.finish();
                                    Reusable_Functions.hDialog();
                                }
                            } catch (Exception e)
                            {
                                Log.e("Exception e", e.toString() + "");
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

        } else
        {
            Toast.makeText(context, "Please check network connection...", Toast.LENGTH_SHORT).show();

        }
    }
}
