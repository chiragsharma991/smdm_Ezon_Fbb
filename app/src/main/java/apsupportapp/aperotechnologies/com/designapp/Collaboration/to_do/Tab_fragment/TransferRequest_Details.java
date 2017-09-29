package apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.util.Pair;
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

    private Gson gson;
    private SharedPreferences sharedPreferences;
    private String userId;
    private String bearertoken;
    private String TAG="TransferRequest_Details";
    private Transfer_Request_Model transfer_request_model;
    Context context;
    private int count = 0;
    private int limit = 100;
    private int offsetvalue = 0;
    private RequestQueue queue;
    private ArrayList<Transfer_Request_Model> Sender_DetailsList, SenderChildDetailList,ScanList;
    RelativeLayout tr_imageBtnBack;
    private RecyclerView tr_recyclerView;
    private int levelOfOption = 1;  //  1 is for option and 2 is for size
    private String option = "";    // code and description
    private ArrayList<Integer> totalScanQty;
    private TransferDetailsAdapter transferDetailsAdapter;
    private HashMap<Integer,ArrayList<Integer>> TrchildScanQty;
    private TextView txt_caseNo, txt_valtotalreqty;
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
    private SharedPreferences permissionStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferreq_details);
        getSupportActionBar().hide();
        context = this;
        initalise();
        recache = "true";
        gson = new Gson();
        Sender_DetailsList = new ArrayList<Transfer_Request_Model>();
        ScanList = new ArrayList<Transfer_Request_Model>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();

        if (Reusable_Functions.chkStatus(context))
        {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            TransferDetailProcess.setVisibility(View.VISIBLE);

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
        Log.e(TAG, "requestReceiversChildDetails: "+url );
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        Log.e(TAG, "requestReceiversChildDetails: onResponse "+response );

                        try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                TransferDetailProcess.setVisibility(View.GONE);
                                Toast.makeText(TransferRequest_Details.this, "no data found", Toast.LENGTH_SHORT).show();
                                return;

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    transfer_request_model = gson.fromJson(response.get(i).toString(), Transfer_Request_Model.class);
                                    SenderChildDetailList.add(transfer_request_model);

                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestReceiversChildDetails(position);

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    transfer_request_model = gson.fromJson(response.get(i).toString(), Transfer_Request_Model.class);
                                    SenderChildDetailList.add(transfer_request_model);
                                }
                            }

                            addScanCount(SenderChildDetailList,position);
                            subchildqty.put(position,SenderChildDetailList);
                            transferDetailsAdapter.notifyDataSetChanged();
                            Reusable_Functions.hDialog();
                            TransferDetailProcess.setVisibility(View.GONE);

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "data failed...." + e.toString(), Toast.LENGTH_SHORT).show();
                            TransferDetailProcess.setVisibility(View.GONE);
                            Reusable_Functions.hDialog();
                            e.printStackTrace();
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

    private void requestSenderDetails() {

        String url = ConstsCore.web_url + "/v1/display/stocktransfer/senderdetail/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&level=" + levelOfOption +"&caseNo="+detail_CaseNo+"&reqStoreCode="+detl_reqStoreCode+"&recache=" + recache;
        Log.e(TAG, "requestSenderDetails--: "+url );
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, "requestSenderDetails: "+response );
                        try
                        {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                TransferDetailProcess.setVisibility(View.GONE);
                                Reusable_Functions.hDialog();
                                TransferDetailProcess.setVisibility(View.GONE);
                                Toast.makeText(TransferRequest_Details.this, "no data found", Toast.LENGTH_SHORT).show();
                                return;

                            } else if (response.length() == limit)
                            {
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
                                for (int i = 0; i < response.length(); i++)
                                {
                                    transfer_request_model = gson.fromJson(response.get(i).toString(), Transfer_Request_Model.class);
                                    Sender_DetailsList.add(transfer_request_model);
                                }
                            }
                            tr_recyclerView.setLayoutManager(new LinearLayoutManager(tr_recyclerView.getContext(), 48 == Gravity.CENTER_HORIZONTAL ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                            tr_recyclerView.setOnFlingListener(null);
                            MakeChildScanList(Sender_DetailsList);   //child all list
                            MakeSubChildScanCount(Sender_DetailsList);  //only child scan count
                            MakeHeaderScanCount(Sender_DetailsList);  // Top Header scan count
                            MakeCheckPair();  // Top Header scan count
                            transferDetailsAdapter = new TransferDetailsAdapter(Sender_DetailsList, context,subchildqty,subchildcount,TransferDetailProcess,headerScancount,TransferRequest_Details.this,CheckedItems);
                            tr_recyclerView.setAdapter(transferDetailsAdapter);
                            TransferDetailProcess.setVisibility(View.GONE);

                            TransferDetailProcess.setVisibility(View.GONE);
                            Reusable_Functions.hDialog();

                        } catch (Exception e) {
                            TransferDetailProcess.setVisibility(View.GONE);
                            Toast.makeText(context, "data failed...." + e.toString(), Toast.LENGTH_SHORT).show();
                            Reusable_Functions.hDialog();

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        TransferDetailProcess.setVisibility(View.GONE);
                        Reusable_Functions.hDialog();
                        TransferDetailProcess.setVisibility(View.GONE);
                        Toast.makeText(context, "server not responding..", Toast.LENGTH_SHORT).show();
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
        tr_recyclerView = (RecyclerView) findViewById(R.id.trasnsferreq_detail_list);
        tr_imageBtnBack = (RelativeLayout) findViewById(R.id.tr_details_imageBtnBack);
        txt_caseNo = (TextView) findViewById(R.id.txt_caseNo);
        txt_valtotalreqty = (TextView) findViewById(R.id.txt_valtotalreqty);
        btn_Submit = (Button)findViewById(R.id.btn_trdetailSubmit);
        TransferDetailProcess = (ProgressBar)findViewById(R.id.transferDetailProcess);
        TransferDetailProcess.setVisibility(View.GONE);
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

    public void onPress(int position) {

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
    public void onClick(View v)
    {
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
            if(!subchildqty.get(i).isEmpty())   //fst start with subchild if no one select in subchild then it will go header selection.
            {
                for (int j = 0; j < subchildqty.get(i).size(); j++)  // all sizes of one option
                {
                    Pair<Integer, Integer> Tag = new Pair<Integer, Integer>(i, j);  //check if any check box is check another wise it will not proceed.
                    if (CheckedItems.contains(Tag)) {
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("option", Sender_DetailsList.get(i).getLevel());
                            obj.put("prodAttribute4", subchildqty.get(i).get(j).getLevel());
                            obj.put("caseNo", detail_CaseNo);
                            jsonarray.put(count, obj);
                            count++;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
                else
                {
                    if(transferDetailsAdapter.TransferDetails_HeadercheckList[i])
                    {
                        JSONObject obj = new JSONObject();
                        try
                        {
                            obj.put("option",Sender_DetailsList.get(i).getLevel());
//                            obj.put("prodAttribute4","");
                            obj.put("caseNo",detail_CaseNo);
                            jsonarray.put(count,obj);
                            count++;

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }

        }
        if(jsonarray.length()==0)
        {
            Toast.makeText(context,"Please select at least one size.",Toast.LENGTH_SHORT).show();

        }else
        {
            requestSenderSubmitAPI(context,jsonarray,null,0);
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
    public void passData(String barcode,Context context)
    {
        Log.e(TAG, "passData: "+barcode);

        if (Reusable_Functions.chkStatus(context))
        {
            Reusable_Functions.sDialog(context, "Loading data...");
            TransferDetailProcess.setVisibility(View.VISIBLE);
            requestScanDetailsAPI(barcode,context);
        }
        else
        {
            Toast.makeText(context, "Please check network connection...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null)
        {
            if (result.getContents() == null)
            {
            }
            else
            {
                Toast.makeText(this, "Barcode Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.hDialog();
                        Reusable_Functions.sDialog(context, "Loading  data...");
                        TransferDetailProcess.setVisibility(View.VISIBLE);
                        requestScanDetailsAPI(result.getContents(),context);

                    } else {
                        Toast.makeText(TransferRequest_Details.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
                    }

            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


     public void requestScanDetailsAPI(String contents,final Context context) {

         String url = ConstsCore.web_url + "/v1/display/stocktransfer/senderscan/scan/" + userId + "?eanNumber="+contents +"&recache=true" ;
         Log.e(TAG, "requestScanDetailsAPI: "+url );
         final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, "onResponse: "+response);
                        try
                        {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0)
                            {
                                Reusable_Functions.hDialog();
                                TransferDetailProcess.setVisibility(View.GONE);
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                return;

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {

                                    transfer_request_model = gson.fromJson(response.get(i).toString(), Transfer_Request_Model.class);
                                    ScanList.add(transfer_request_model);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;

                            }
                            else if (response.length() < limit)
                            {
                                for (int i = 0; i < response.length(); i++) {
                                    transfer_request_model = gson.fromJson(response.get(i).toString(), Transfer_Request_Model.class);
                                    ScanList.add(transfer_request_model);
                                }

                                String size= ScanList.get(0).getProdAttribute4();
                                int size_pos;
                                ArrayList<Integer>addQty=new ArrayList<>();

                                for(int i = 0;i < SenderChildDetailList.size();i++) //find sizeitem  position from child list
                                {
                                    if(SenderChildDetailList.get(i).getLevel().equals(size))//add size instead of 4-5 value
                                    {
                                        scanDone=true;
                                        if(subchildqty.get(0).get(i).getStkOnhandQtyRequested() > subchildcount.get(0).get(i))   //"0"is for Pre position
                                        {
                                            size_pos=i;
                                            int count=subchildcount.get(0).get(i)+1;  //pre count + next count  && "0"is pre position you have to change
                                            addQty.add(count);  //total sub count

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

                                if(!scanDone){
                                    Toast.makeText(context,"Barcode does not match sizes...",Toast.LENGTH_SHORT).show();
                                }else {
                                    scanDone=false;
                                }
                            }
                            Reusable_Functions.hDialog();
                            TransferDetailProcess.setVisibility(View.GONE);


                        } catch (Exception e) {

                            Toast.makeText(context, "data failed...." + e.toString(), Toast.LENGTH_SHORT).show();
                            Reusable_Functions.hDialog();
                            TransferDetailProcess.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "server not responding..", Toast.LENGTH_SHORT).show();
                        Reusable_Functions.hDialog();
                        TransferDetailProcess.setVisibility(View.GONE);
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

    private void requestforSap() {

        String url = ConstsCore.web_url + "/v1/display/pulltransfersapsubmit/" + userId+"?caseNo="+detail_CaseNo +"&recache=true";
        Log.e(TAG, "requestforSap: "+url );
        final JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "onResponse: "+response);
                        try
                        {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0)
                            {
                                Reusable_Functions.hDialog();
                                Toast.makeText(TransferRequest_Details.this, "no data found", Toast.LENGTH_SHORT).show();
                                return;

                            } else{
                                //Reusable_Functions.hDialog();
                                requestSenderSubmitAPI(context,null,response,1);
                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Log.e(TAG, "onResponse: error"+e.getMessage() );
                            Toast.makeText(context, "data failed...." + e.toString(), Toast.LENGTH_SHORT).show();
                            Reusable_Functions.hDialog();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Log.e(TAG, "onErrorResponse: "+ error.getMessage() );
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


    /**
     *
     * @param  id is for api call number.
     */

    private void requestSenderSubmitAPI(final Context mcontext, JSONArray jsonarray ,JSONObject jsonobject, final int id )  // Sender Submit Api call
    {

        if (Reusable_Functions.chkStatus(mcontext)) {
            String url = null;
            String postofData= null;

            if(id ==0) {
                Reusable_Functions.sDialog(mcontext, "Submitting data…");
                url = ConstsCore.web_url + "/v1/save/stocktransfer/sendersubmit/" + userId;//+"?recache="+recache
                postofData=jsonarray.toString();
                Log.e(TAG, "requestSenderSubmitAPI: " + postofData);

            }
//            else if (id==1){
//                url = "https://mapi.futuregroup.in/fgapis/sap/STOCreateSRP/1.0?apikey=46a94bd1-04ea-466b-bcf0-59cb74abbd1b" ;
//                postofData=jsonobject.toString();
//
//            }
            Log.e(TAG, "requestSenderSubmitAPI: "+url );

            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.PUT, url,postofData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e(TAG, "requestSenderSubmitAPI onResponse: "+response);
                            Reusable_Functions.hDialog();

                            try {
                                if (response == null || response.equals("")) {
                                    Toast.makeText(mcontext,"Sending data failed...", Toast.LENGTH_LONG).show();

                                } else
                                {
                                    transferDetailsAdapter.notifyDataSetChanged();
                                    requestSenderDetails();
//                                    switch (id){
//                                        case 0:
//                                            requestforSap();
//                                            break;
//                                        case 1:
//                                            // String result=response.getString("status");
//                                            Reusable_Functions.hDialog();
//                                            Toast.makeText(mcontext,"Submission success", Toast.LENGTH_LONG).show();
//                                         //   String result=response.getString("status");
//                                         //   Toast.makeText(mcontext,""+result, Toast.LENGTH_LONG).show();
//                                            Intent intent = new Intent(TransferRequest_Details.this,To_Do.class);
//                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                            startActivity(intent);
//                                            break;
//                                    }

                                }
                            } catch (Exception e)
                            {
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
                            Toast.makeText(context, "server not responding...", Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError
                {
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

        } else
        {
            Toast.makeText(context, "Please check network connection...", Toast.LENGTH_SHORT).show();

        }
    }
}
