package apsupportapp.aperotechnologies.com.designapp.Collaboration.Status.Tab_fragment;

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

import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment.Details;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.RecyclerItemClickListener;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;


public class ToBeSender extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String TAG="StatusSender_Fragment";
    private OnFragmentInteractionListener mListener;
    private ViewGroup view;
    private Context context;
    private Gson gson;
    private int count = 0;
    private int limit = 100;
    private int offsetvalue = 0;
    private SharedPreferences sharedPreferences;
    private String userId;
    private String bearertoken;
    private RequestQueue queue;
    private StatusModel statusModel;
    private ArrayList<StatusModel>SenderSummaryList;
    private RecyclerView recyclerView;


    public ToBeSender() {
        // Required empty public constructor
    }


    public static ToBeSender newInstance(String param1, String param2) {
        ToBeSender fragment = new ToBeSender();
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
        view = (ViewGroup) inflater.inflate(R.layout.fragment_to_be_received, container, false);
        SenderSummaryList = new ArrayList<>();
        initialise();
        MainMethod();


        return view;


    }

    private void MainMethod() {
        NetworkProcess();

        if(Reusable_Functions.chkStatus(context))
        {
            Reusable_Functions.sDialog(context, "Loading.......");

            requestSenderCaseStatusSummary();

        }else
        {
            Toast.makeText(context, "Please check network connection...", Toast.LENGTH_SHORT).show();

            Reusable_Functions.hDialog();
        }
    }

    private void requestSenderCaseStatusSummary()
    {

        String url = ConstsCore.web_url + "/v1/display/stocktransfer/sendercasestatus/summary/"+ userId + "?offset=" + offsetvalue + "&limit=" +limit;
        Log.e(TAG, "Status Sender Summary Url" + "" + url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        Log.i(TAG, "Status Sender response : " + " " + response);
                        Log.i(TAG, "Status Sender length" + "" + response.length());

                        try
                        {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                return;

                            } else if (response.length() == limit) {
                                Log.e(TAG, "promo eql limit");
                                for (int i = 0; i < response.length(); i++) {

                                    statusModel = gson.fromJson(response.get(i).toString(), StatusModel.class);
                                    SenderSummaryList.add(statusModel);

                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                //

                                requestSenderCaseStatusSummary();

                            } else if (response.length() < limit) {
                                Log.e(TAG, "promo /= limit");
                                for (int i = 0; i < response.length(); i++)
                                {
                                    statusModel = gson.fromJson(response.get(i).toString(), StatusModel.class);
                                    SenderSummaryList.add(statusModel);
                                }
                                count = 0;
                                limit = 100;
                                offsetvalue = 0;

                            }

                            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), 48 == Gravity.CENTER_HORIZONTAL ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                            recyclerView.setOnFlingListener(null);
                            // new GravitySnapHelper(48).attachToRecyclerView(recyclerView);
                            ToBeSenderAdapter Adapter = new ToBeSenderAdapter(SenderSummaryList,getActivity());
                            recyclerView.setAdapter(Adapter);
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

    }

    private void NetworkProcess()
    {
        gson = new Gson();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Log.e(TAG, "userID and token" + userId + "and this is" + bearertoken);
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
    }

    private void initialise() {

        recyclerView=(RecyclerView)view.findViewById(R.id.to_be_received_list);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                new ToBeSenderDetails().StartActivity(context,SenderSummaryList.get(position).getCaseNo(),SenderSummaryList.get(position).getReqStoreCode());
            }
        }));
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
        this.context=context;


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}