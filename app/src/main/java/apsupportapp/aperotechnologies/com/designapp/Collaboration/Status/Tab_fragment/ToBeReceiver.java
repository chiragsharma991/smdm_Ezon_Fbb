package apsupportapp.aperotechnologies.com.designapp.Collaboration.Status.Tab_fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import apsupportapp.aperotechnologies.com.designapp.Collaboration.Status.StatusActivity;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;



public class ToBeReceiver extends Fragment  implements OnclickStatus{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private ViewGroup view;
    Context context;
    private Gson gson;
    private int count = 0;
    private int limit = 100;
    private int offsetval = 0;
    private SharedPreferences sharedPreferences;
    private String userId;
    private String bearertoken;
    private RequestQueue queue;
    private boolean Receiver_checkNetwkstatus = false;
    private StatusModel recstatusModel;
    private ArrayList<StatusModel> ReceiverSummaryList,ReceiverStatusList;
    private RecyclerView recyclerView_receiver;
    private HashMap<Integer,ArrayList<StatusModel>> rec_initiatedStatusList,rec_senderAcpStatusList,rec_stoStatusList,rec_grnStatusList;
    private ToBeReceiverAdapter ReceiverAdapter;
    private String recache;

    public ToBeReceiver() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ToBeReceiver newInstance(String param1, String param2) {
        ToBeReceiver fragment = new ToBeReceiver();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = (ViewGroup) inflater.inflate(R.layout.fragment_to_be_transfer, container, false);
        ReceiverSummaryList = new ArrayList<>();
        gson = new Gson();
        recache = "true";
        Receiver_checkNetwkstatus = false;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        initialise();
        MainMethod();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            if (Receiver_checkNetwkstatus) {
                Toast.makeText(context, "No data found ", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void MainMethod() {
        NetworkProcess();
        if(Reusable_Functions.chkStatus(context))
        {
            Reusable_Functions.sDialog(context, "Loading...");
            requestReceiverCaseStatusSummary();

        }else
        {
            Toast.makeText(context, "Please check network connection...", Toast.LENGTH_SHORT).show();
            Reusable_Functions.hDialog();
        }
    }

    private void initialise() {

        recyclerView_receiver=(RecyclerView)view.findViewById(R.id.to_be_sender_list);
    }

    private void NetworkProcess()
    {
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void MakeReceiver_StatusHashMap(ArrayList<StatusModel> senderSummaryList) {
        rec_initiatedStatusList=new HashMap<Integer, ArrayList<StatusModel>>();
        for (int i = 0; i <senderSummaryList.size() ; i++)
        {
            //noinspection MismatchedQueryAndUpdateOfCollection
            @SuppressWarnings("MismatchedQueryAndUpdateOfCollection") ArrayList<StatusModel>list=new ArrayList<>();
            rec_initiatedStatusList.put(i,list);
        }

        rec_senderAcpStatusList=new HashMap<Integer, ArrayList<StatusModel>>();
        for (int i = 0; i <senderSummaryList.size() ; i++) {
            ArrayList<StatusModel>list=new ArrayList<>();
            rec_senderAcpStatusList.put(i,list);
        }

        rec_stoStatusList=new HashMap<Integer, ArrayList<StatusModel>>();
        for (int i = 0; i <senderSummaryList.size() ; i++) {
            ArrayList<StatusModel>list=new ArrayList<>();
            rec_stoStatusList.put(i,list);
        }

        rec_grnStatusList=new HashMap<Integer, ArrayList<StatusModel>>();
        for (int i = 0; i <senderSummaryList.size() ; i++) {
            ArrayList<StatusModel>list=new ArrayList<>();
            rec_grnStatusList.put(i,list);
        }
    }

    @Override
    public void Onclick(int caseNo, String actionStatus, int dublicatePosition, int Case,String sender_storcode) {
        String rec_senderStoreCode= sender_storcode;
        ReceiverStatusList=new ArrayList<StatusModel>();
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.sDialog(context, "Loading....");

            // this case is for last two sto and grn

            if(Case == 1) // initiated status
            {
                requestReceiverCaseStatus(caseNo,actionStatus,rec_senderStoreCode,dublicatePosition,Case);

            }
            if(Case == 2) // Sender Acpt status
            {
                requestReceiverCaseStatus(caseNo,actionStatus,rec_senderStoreCode,dublicatePosition,Case);

            }else if (Case==3) // STO status
            {
                requestReceiverCaseStatus(caseNo,actionStatus,rec_senderStoreCode,dublicatePosition,Case);

            }else if(Case == 4) // GRN status
            {
                requestReceiverCaseStatus(caseNo,actionStatus,rec_senderStoreCode,dublicatePosition,Case);

            }

        } else {
            Toast.makeText(context, "Please check network connection...", Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //---------------------------- API Declaration --------------------------//
    private void requestReceiverCaseStatusSummary()
    {
        String receiver_case_url = ConstsCore.web_url + "/v1/display/stocktransfer/receivercasestatus/summary/"+ userId + "?offset=" + offsetval + "&limit=" +limit + "&recache="+recache;
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, receiver_case_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                    try
                        {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Receiver_checkNetwkstatus= true;
                                return;

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++)
                                {

                                   recstatusModel = gson.fromJson(response.get(i).toString(), StatusModel.class);
                                   ReceiverSummaryList.add(recstatusModel);

                                }
                                offsetval = (limit * count) + limit;
                                count++;
                                requestReceiverCaseStatusSummary();

                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++)
                                {
                                    recstatusModel = gson.fromJson(response.get(i).toString(), StatusModel.class);
                                    ReceiverSummaryList.add(recstatusModel);
                                }
                          }
                            recyclerView_receiver.setLayoutManager(new LinearLayoutManager(recyclerView_receiver.getContext(), 48 == Gravity.CENTER_HORIZONTAL ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                            recyclerView_receiver.setOnFlingListener(null);
                            MakeReceiver_StatusHashMap(ReceiverSummaryList);
                            ReceiverAdapter = new ToBeReceiverAdapter(rec_initiatedStatusList,rec_senderAcpStatusList,rec_stoStatusList,rec_grnStatusList,ReceiverSummaryList,context,ToBeReceiver.this);
                            recyclerView_receiver.setAdapter(ReceiverAdapter);
                            Reusable_Functions.hDialog();

                        } catch (Exception e) {
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
    private void requestReceiverCaseStatus(final int caseNo, final String actionStatus, final String senderStoreCode, final int position,final int Case)
    {
        String url="" ;
        if(Case==1) // initiated status
        {
            url = ConstsCore.web_url + "/v1/display/stocktransfer/receivercasestatus/initiated/" + userId + "?offset=" + offsetval+ "&limit=" + limit +"&caseNo="+caseNo+"&actionStatus="+actionStatus+"&senderStoreCode="+senderStoreCode +"&recache="+recache;


        }else if(Case == 2) // Sender Acpt status
        {
            url = ConstsCore.web_url + "/v1/display/stocktransfer/receivercasestatus/senderacpt/" + userId + "?offset=" + offsetval + "&limit=" + limit +"&caseNo="+caseNo+"&actionStatus="+actionStatus+"&senderStoreCode="+senderStoreCode+"&recache="+recache;

        }
        else if(Case == 3 ) // STO status
        {
            url = ConstsCore.web_url + "/v1/display/stocktransfer/receivercasestatus/sto/" + userId + "?offset=" + offsetval + "&limit=" + limit +"&caseNo="+caseNo+"&senderStoreCode="+senderStoreCode+"&recache="+recache;

        }
        else if(Case==4) // GRN status
        {
            url = ConstsCore.web_url + "/v1/display/stocktransfer/receivercasestatus/grn/" + userId + "?offset=" + offsetval + "&limit=" + limit +"&caseNo="+caseNo+"&senderStoreCode="+senderStoreCode+"&recache="+recache;
        }
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                    try {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                StatusActivity.StatusProcess.setVisibility(View.GONE);
                                ReceiverAdapter .notifyDataSetChanged();
                                return;

                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++)
                                {
                                    recstatusModel = gson.fromJson(response.get(i).toString(), StatusModel.class);
                                    ReceiverStatusList.add(recstatusModel);
                                }
                                offsetval = (limit * count) + limit;
                                count++;
                                requestReceiverCaseStatus(caseNo,actionStatus,senderStoreCode,position,Case);

                            } else if (response.length() < limit)
                            {
                                for (int i = 0; i < response.length(); i++)
                                {
                                    recstatusModel = gson.fromJson(response.get(i).toString(), StatusModel.class);
                                    ReceiverStatusList.add(recstatusModel);

                                }

                            }

                            if(Case==1)
                            {
                                rec_initiatedStatusList.put(position,ReceiverStatusList);

                            }else if(Case==2)
                            {
                                rec_senderAcpStatusList.put(position,ReceiverStatusList);
                            }
                            else if(Case==3)
                            {
                                rec_stoStatusList.put(position,ReceiverStatusList);
                            }
                            else if(Case==4)
                            {
                                rec_grnStatusList.put(position,ReceiverStatusList);
                            }

                            ReceiverAdapter.notifyDataSetChanged();
                            StatusActivity.StatusProcess.setVisibility(View.GONE);
                            Reusable_Functions.hDialog();
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "data failed...." + e.toString(), Toast.LENGTH_SHORT).show();
                            Reusable_Functions.hDialog();
                            StatusActivity.StatusProcess.setVisibility(View.GONE);
                            ReceiverAdapter .notifyDataSetChanged();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "server not responding..", Toast.LENGTH_SHORT).show();
                        ReceiverAdapter.notifyDataSetChanged();
                        StatusActivity.StatusProcess.setVisibility(View.GONE);
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
