package apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Transfer_Request_Model;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;


import static apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment.StockPullFragment.store_Code;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TransferRequestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TransferRequestFragment#} factory method to
 * create an instance of this fragment.
 */
public class TransferRequestFragment extends Fragment implements View.OnClickListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Gson gson;
    private SharedPreferences sharedPreferences;
    private String userId;
    private String bearertoken;
    private Transfer_Request_Model transfer_request_model;
    private int count = 0;
    private int limit = 100;
    private int offsetvalue = 0;
    private boolean checkNetworkFalse=false;
    private RequestQueue queue;
    private ArrayList<Transfer_Request_Model> SenderSummaryList;
    private String recache;
    private String mParam1;
    private String mParam2;
    private StockPullFragment.OnFragmentInteractionListener mListener;
    private Context context;
    private ViewGroup view;
    private RecyclerView senderSummary_recyclerView;
    private CheckBox mcCheck;
    private boolean[] selectMc;
    private String TAG="TransferRequestFragment";
    private Button submit;
    TransferRequestAdapter transferRequestAdapter;
    private ArrayList<String> selectedCaseNo;
    private boolean submitFlag = false;

    public TransferRequestFragment(String storeCode) {
        // Required empty public constructor
       // this.store_Code = store_Code;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
        {
            if(checkNetworkFalse)
            {
                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //TODO: Rename and change types and number of parameters
//    public static StockPullFragment newInstance(String param1, String param2) {
//        StockPullFragment fragment = new StockPullFragment(store_Code, from);
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view = (ViewGroup) inflater.inflate(R.layout.fragment_transfer_request, container, false);
        context = view.getContext();
        SenderSummaryList=new ArrayList<Transfer_Request_Model>();
        recache = "true";
        submitFlag = false;
        initialise();
        MainMethod();
        return view;
    }

    private void initialise()
    {
        selectedCaseNo=  new ArrayList<String>();
        senderSummary_recyclerView=(RecyclerView)view.findViewById(R.id.transferRequest_list);
        submit=(Button)view.findViewById(R.id.btn_submit);
        submit.setOnClickListener(this);
    }

    private void MainMethod()
    {
        NetworkProcess();
        Reusable_Functions.sDialog(context, "Loading...");
        requestTransferRequestsummary();

    }

    private void requestTransferRequestsummary()
    {
        if (Reusable_Functions.chkStatus(context)) {

            String url = ConstsCore.web_url + "/v1/display/stocktransfer/sendersummary/"+ userId + "?offset=" + offsetvalue + "&limit=" +limit +"&recache="+recache + "&senderStoreCode=" + store_Code;

            final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response)
                        {
                            try
                            {
                                if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                    Reusable_Functions.hDialog();
                                    if(submitFlag) {
                                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                    }
                                    checkNetworkFalse=true;
                                    return;
                                }
                                else if (response.length() == limit)
                                {
                                    for (int i = 0; i < response.length(); i++)
                                    {
                                        transfer_request_model = gson.fromJson(response.get(i).toString(), Transfer_Request_Model.class);
                                        SenderSummaryList.add(transfer_request_model);
                                    }
                                    offsetvalue = (limit * count) + limit;
                                    count++;
                                    requestTransferRequestsummary();

                                }
                                else if (response.length() < limit)
                                {
                                    for (int i = 0; i < response.length(); i++)
                                    {
                                        transfer_request_model = gson.fromJson(response.get(i).toString(), Transfer_Request_Model.class);
                                        SenderSummaryList.add(transfer_request_model);
                                    }
                                }
                                selectMc=new boolean[SenderSummaryList.size()];
                                for (int i = 0; i <SenderSummaryList.size() ; i++) {
                                    selectMc[i]=false;
                                    selectedCaseNo.clear();
                                }
                                senderSummary_recyclerView.setLayoutManager(new LinearLayoutManager(senderSummary_recyclerView.getContext(), 48 == Gravity.CENTER_HORIZONTAL ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                senderSummary_recyclerView.setOnFlingListener(null);

                                transferRequestAdapter = new TransferRequestAdapter(SenderSummaryList,selectMc,getActivity(), new TransferRequestAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position)
                                    {
                                        new TransferRequest_Details().StartActivity(SenderSummaryList.get(position).getCaseNo(),SenderSummaryList.get(position).getStkOnhandQtyRequested(),SenderSummaryList.get(position).getReqStoreCode(),context, store_Code);
                                    }
                                });
                                senderSummary_recyclerView.setAdapter(transferRequestAdapter );
                                submitFlag = false;
                                Reusable_Functions.hDialog();

                            } catch (Exception e)
                            {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "data failed...." + e.toString(), Toast.LENGTH_SHORT).show();
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
        else
        {
            Toast.makeText(context, "Please check network connection...", Toast.LENGTH_SHORT).show();
            Reusable_Functions.hDialog();
        }

    }

    private void NetworkProcess()
    {
        gson = new Gson();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    public void onSubmit(){

        if (!(SenderSummaryList.size() == 0))
        {
            JSONArray jsonArray = new JSONArray();
            try {

                for(int i = 0 ; i <selectMc.length; i++)
                {
                    if(selectMc[i]) {
                        selectedCaseNo.add(SenderSummaryList.get(i).getCaseNo());
                    }
                }

                String[] array = (String[]) selectedCaseNo.toArray(new String[0]);
                String caseNo = Arrays.toString(array);
                caseNo = caseNo.replace("[", "");
                caseNo = caseNo.replace("]", "");
                caseNo = caseNo.replace(", ", ",");

                JSONObject obj = new JSONObject();
                //obj.put("option",SenderSummaryList.get(i).getLevel());
                obj.put("caseNo",caseNo);
               // obj.put("storeCode",store_Code);

                jsonArray.put(obj);

                if(jsonArray.length() != 0){
                    if(jsonArray.length() > 1) Toast.makeText(context, "Please select single case for submission", Toast.LENGTH_SHORT).show();
                    else requestSenderSubmitAPI(context, jsonArray,null,0);
                }
                else{
                    Toast.makeText(context, "Please select at least one option.", Toast.LENGTH_SHORT).show();
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     *
     * @param  id is for api call number.
     */

    private void requestSenderSubmitAPI(final Context mcontext, final JSONArray jsonarray , JSONObject jsonobject, final int id )  // Sender Submit Api call
    {

        if (Reusable_Functions.chkStatus(mcontext)) {
            //Reusable_Functions.hDialog();
            String url = null;
            String postofData= null;

            if(id ==0)
            {
                Reusable_Functions.sDialog(mcontext, "Submitting data…");
                url = ConstsCore.web_url + "/v1/save/stocktransfer/sendersubmit/" + userId;//+"?recache="+recache
                postofData=jsonarray.toString();
            }
//            else if (id==1){
//                url = "https://mapi.futuregroup.in/fgapis/sap/STOCreateSRP/1.0?apikey=46a94bd1-04ea-466b-bcf0-59cb74abbd1b" ;
//                postofData=jsonobject.toString();
//
//            }

            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.PUT, url,postofData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Reusable_Functions.hDialog();

                            try {
                                if (response == null || response.equals("")) {
                                    Toast.makeText(mcontext,"Sending data failed...", Toast.LENGTH_LONG).show();

                                } else
                                {
                                    SenderSummaryList.clear();
                                    transferRequestAdapter = new TransferRequestAdapter(SenderSummaryList,selectMc,getActivity(), null);
                                    senderSummary_recyclerView.setAdapter(transferRequestAdapter);
                                    transferRequestAdapter.notifyDataSetChanged();
                                    Reusable_Functions.sDialog(context, "Loading...");
                                    submitFlag = true;
                                    requestTransferRequestsummary();



//                                    switch (id){
//                                        case 0:
//                                            String selectCase=jsonarray.getJSONObject(0).get("caseNo").toString();
//                                            requestforSap(selectCase);
//                                            break;
//                                        case 1:
//                                            // String result=response.getString("status");
//                                            Reusable_Functions.hDialog();
//                                            for (int i = 0; i <SenderSummaryList.size() ; i++) {
//                                                selectMc[i]=false;
//                                            }
//                                            transferRequestAdapter.notifyDataSetChanged();
//                                            Toast.makeText(mcontext,"Submission success", Toast.LENGTH_LONG).show();
//                                            break;
//                                    }
                                    Reusable_Functions.hDialog();
                                }
                            } catch (Exception e)
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

        } else
        {
            Toast.makeText(context, "Please check network connection...", Toast.LENGTH_SHORT).show();

        }
    }

    private void requestforSap(String selectCase) {

        String url = ConstsCore.web_url + "/v1/display/pulltransfersapsubmit/" + userId+"?caseNo="+selectCase +"&recache=true";
        final JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0)
                            {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                return;

                            } else{
                                //Reusable_Functions.hDialog();
                                requestSenderSubmitAPI(context,null,response,1);
                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "data failed...." + e.toString(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
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

    }



    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        onSubmit();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
