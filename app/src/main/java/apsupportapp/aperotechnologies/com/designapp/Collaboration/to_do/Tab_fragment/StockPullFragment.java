package apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointD;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.ToDo_Modal;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;

import apsupportapp.aperotechnologies.com.designapp.MyMarkerView;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.RecyclerItemClickListener;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.model.SalesPvAAnalysisWeek;


public class StockPullFragment extends Fragment implements OnChartGestureListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Gson gson;
    private SharedPreferences sharedPreferences;
    private String userId;
    private String bearertoken;
    private ToDo_Modal toDo_Modal;
    private int count = 0;
    private int limit = 100;
    private int offsetvalue = 0;
    private int level = 1;
    private boolean checkNetworkFalse = false;
    private RequestQueue queue;
    private ArrayList<ToDo_Modal> ReceiverSummaryList,subcategoryList;
    private String recache;
    private String mParam1;
    private String mParam2;
    private String mc_name , subcategory_name;
    private OnFragmentInteractionListener mListener;
    private Context context;
    private ViewGroup view;
    private RecyclerView recyclerView;
    private BarChart barChart;
    private String selectprodLevel3Desc="";
    private HashMap<Integer, String> mapValues;
    private String dublicateSelectprodLevel3Desc="";
    private RelativeLayout progressBar;

    public StockPullFragment() {
        // Required empty public constructor
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (checkNetworkFalse) {
                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();

            }
        }
    }


    // TODO: Rename and change types and number of parameters
    public static StockPullFragment newInstance(String param1, String param2) {
        StockPullFragment fragment = new StockPullFragment();
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
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = (ViewGroup) inflater.inflate(R.layout.fragment_stock_pull, container, false);
        ReceiverSummaryList = new ArrayList<>();
        recache = "true";
        initialise();
        MainMethod();
        return view;
    }

    private void initialise() {
        barChart = (BarChart) view.findViewById(R.id.bar_chart);
        recyclerView = (RecyclerView) view.findViewById(R.id.stockPull_list);
        progressBar = (RelativeLayout) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position)
            {
                subcategory_name = ReceiverSummaryList.get(position).getLevel();
                mc_name = subcategoryList.get(position).getLevel();

                new Details().StartActivity(context, subcategory_name,mc_name, ReceiverSummaryList.get(position).getStkQtyAvl());
            }
        }));

        barChart.setOnChartGestureListener(this);
    }



     /*   barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

               // Log.e("TAG", "dataXtoPx: "+dataXtoPx+" xValue"+xValue+" dataIndex"+datadrawX );
                final String item = barChart.getXAxis().getValueFormatter().getFormattedValue(e.getX(), barChart.getXAxis());
                if(!item.equals("")){
                    selectprodLevel3Desc=item;
                }
                Log.e("VAL SELECTED",
                        "Value: " + e.getY() + ", xIndex: " + e.getX()
                                + ", DataSet index: " + h.getDataSetIndex());
              //  Log.e("TAG", "onValueSelected: " + selectprodLevel3Desc);
               // Reusable_Functions.sDialog(context, "Loading.......");
               // requestTransferRequestSubcategory(selectprodLevel3Desc);

            }

            @Override
            public void onNothingSelected() {
                Log.e("TAG", "onNothingSelected: ");


            }
        });*/


    private void MainMethod() {
        NetworkProcess();
        Reusable_Functions.sDialog(context, "Loading.......");
        requestTransferRequestsummary();

    }



    private void requestTransferRequestsummary() {
        if (Reusable_Functions.chkStatus(context)) {
            //https://smdm.manthan.com/v1/display/stocktransfer/receiverdetail/69-4795?level=1
            String url = ConstsCore.web_url + "/v1/display/stocktransfer/receiverdetail/" + userId + "?level=" + level + "&offset=" + offsetvalue + "&limit=" + limit + "&recache=" + recache;
            Log.e("TAG", "requestTransferRequestsummary: " + url);
            final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,

                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d("TAG", "onResponse: " + response);
                            try {
                                if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                    Reusable_Functions.hDialog();
                                    checkNetworkFalse = true;
                                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                    return;

                                } else if (response.length() == limit) {
                                    for (int i = 0; i < response.length(); i++) {

                                        toDo_Modal = gson.fromJson(response.get(i).toString(), ToDo_Modal.class);
                                        ReceiverSummaryList.add(toDo_Modal);

                                    }
                                    offsetvalue = (limit * count) + limit;
                                    count++;
                                    requestTransferRequestsummary();

                                } else if (response.length() < limit) {
                                    for (int i = 0; i < response.length(); i++) {
                                        toDo_Modal = gson.fromJson(response.get(i).toString(), ToDo_Modal.class);
                                        ReceiverSummaryList.add(toDo_Modal);
                                    }
                                    count = 0;
                                    limit = 100;
                                    offsetvalue = 0;

                                }

                                multidatasetBarGraph(ReceiverSummaryList);
                                // recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), 48 == Gravity.CENTER_HORIZONTAL ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                // recyclerView.setOnFlingListener(null);
                                // StockPullAdapter stockPullAdapter;
                                // stockPullAdapter = new StockPullAdapter(ReceiverSummaryList,getActivity());
                                // recyclerView.setAdapter(stockPullAdapter);
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
        } else {
            Toast.makeText(context, "Please check network connection...", Toast.LENGTH_SHORT).show();

            Reusable_Functions.hDialog();
        }

    }


    private void requestTransferRequestSubcategory(String prodLevel3Desc) {
        if (Reusable_Functions.chkStatus(context)) {
            level=2;  // 2 for MC
            subcategoryList=new ArrayList<>();
            //https://smdm.manthan.com/v1/display/stocktransfer/receiverdetail/69-4795?level=2&prodLevel3Desc=BF011C-BF - Ladies ethnicwear
            String url = ConstsCore.web_url + "/v1/display/stocktransfer/receiverdetail/" + userId + "?level=" + level
                    +"&prodLevel3Desc="+prodLevel3Desc.replace("%", "%25").replace(" ", "%20").replace("&", "%26")+"&offset=" + offsetvalue + "&limit=" + limit + "&recache=" + recache;

            Log.e("TAG", "requestTransferRequestsummary: " + url);
            final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,

                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d("TAG", "onResponse: " + response);
                            try
                            {
                                if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                    Reusable_Functions.hDialog();
                                    checkNetworkFalse = true;
                                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    return;

                                } else if (response.length() == limit) {
                                    for (int i = 0; i < response.length(); i++) {

                                        toDo_Modal = gson.fromJson(response.get(i).toString(), ToDo_Modal.class);
                                        subcategoryList.add(toDo_Modal);

                                    }
                                    offsetvalue = (limit * count) + limit;
                                    count++;
                                    requestTransferRequestsummary();

                                }
                                else if (response.length() < limit) {
                                    for (int i = 0; i < response.length(); i++) {
                                        toDo_Modal = gson.fromJson(response.get(i).toString(), ToDo_Modal.class);
                                        subcategoryList.add(toDo_Modal);
                                    }
                                    count = 0;
                                    limit = 100;
                                    offsetvalue = 0;

                                }
                                 recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), 48 == Gravity.CENTER_HORIZONTAL ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                 recyclerView.setOnFlingListener(null);
                                 StockPullAdapter stockPullAdapter;
                                 stockPullAdapter = new StockPullAdapter(subcategoryList,getActivity());
                                 recyclerView.setAdapter(stockPullAdapter);
                                 Reusable_Functions.hDialog();
                                 progressBar.setVisibility(View.GONE);


                            } catch (Exception e) {
                                Reusable_Functions.hDialog();
                                progressBar.setVisibility(View.GONE);
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
                            progressBar.setVisibility(View.GONE);
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
        } else {
            Toast.makeText(context, "Please check network connection...", Toast.LENGTH_SHORT).show();

            Reusable_Functions.hDialog();
        }

    }

    public void multidatasetBarGraph(ArrayList<ToDo_Modal> receiverSummaryList) {

        try {
            if (receiverSummaryList != null & receiverSummaryList.size() > 0) {
                mapValues=new HashMap<>();
                barChart.setDrawBarShadow(false);
                barChart.setDrawValueAboveBar(true);
                barChart.setMaxVisibleValueCount(50);
                barChart.setPinchZoom(false);
                barChart.setDrawGridBackground(false);
                barChart.getDescription().setEnabled(false);

                XAxis xl = barChart.getXAxis();
                xl.setGranularity(1f);
                xl.setCenterAxisLabels(true);
                xl.setPosition(XAxis.XAxisPosition.BOTTOM);

                YAxis leftAxis = barChart.getAxisLeft();
                leftAxis.setGranularity(1f);
                leftAxis.setDrawGridLines(false);
                leftAxis.setSpaceTop(30f);
                leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true
                barChart.getAxisRight().setEnabled(false);

                MyMarkerView mv = new MyMarkerView(context, R.layout.custom_marker_view);
                mv.setChartView(barChart); // For bounds control
                barChart.setMarker(mv); // Set the marker to the chart

                //data
                float groupSpace = 0.04f;
                float barSpace = 0.02f; // x2 dataset
                float barWidth = 0.46f; // x2 dataset
                // (0.46 + 0.02) * 2 + 0.04 = 1.00 -> interval per "group"


                List<BarEntry> yVals1 = new ArrayList<BarEntry>();
                List<BarEntry> yVals2 = new ArrayList<BarEntry>();
                String[] labels = new String[receiverSummaryList.size()];




                for (int i = 0; i <receiverSummaryList.size(); i++) {
                    yVals1.add(new BarEntry(i, (float) receiverSummaryList.get(i).getStkOnhandQtyRequested()));
                    yVals2.add(new BarEntry(i, (float) receiverSummaryList.get(i).getStkQtyAvl()));
                    labels[i] = receiverSummaryList.get(i).getLevel();
                    mapValues.put(i,labels[i]);
                    Log.e("TAG", "labels: " + labels[i]);

                }


                BarDataSet set1, set2;

                if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
                    set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
                    set2 = (BarDataSet) barChart.getData().getDataSetByIndex(1);
                    set1.setValues(yVals1);
                    set2.setValues(yVals2);
                    barChart.getData().notifyDataChanged();
                    barChart.notifyDataSetChanged();
                } else {
                    // create 2 datasets with different types
                    set1 = new BarDataSet(yVals1, "Short Qty");
                    set1.setColor(Color.parseColor("#20b5d3"));
                    set2 = new BarDataSet(yVals2, "Avl Qty");
                    set2.setColor(Color.parseColor("#21d24c"));

                    ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                    dataSets.add(set1);
                    dataSets.add(set2);

                    BarData data = new BarData(dataSets);
                    barChart.setData(data);
                }

                barChart.getXAxis().setDrawLabels(true);
                barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
                barChart.getBarData().setBarWidth(barWidth);
                barChart.getXAxis().setAxisMinValue(0);
                barChart.getXAxis().setAxisMaximum(receiverSummaryList.size());
                barChart.groupBars(0, groupSpace, barSpace);
                barChart.setFitBars(true);
                barChart.invalidate();
                barChart.animateXY(3000, 3000);

            } else {
                barChart.clear();

            }

        } catch (Exception e) {
            barChart.clear();
            Log.e("TAG", "multidatasetBarGraph: catch error" + e.getMessage());

        }

    }

    private void NetworkProcess() {
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
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
    }
    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
    }
    @Override
    public void onChartLongPressed(MotionEvent me) {
    }
    @Override
    public void onChartDoubleTapped(MotionEvent me) {
    }
    @Override
    public void onChartSingleTapped(MotionEvent me) {
        float tappedX = me.getX();
        float tappedY = me.getY();
        MPPointD point = barChart.getTransformer(YAxis.AxisDependency.LEFT).getValuesByTouchPoint(tappedX, tappedY);
        Log.e("onChartSingleTapped", "tapped at: " + (int)point.x + "," + (int)point.y);
        String selectprodLevel3Desc=mapValues.get((int)point.x);
        Log.e("TAG", "onChartSingleTapped: Values "+selectprodLevel3Desc );
        // @parms: dublicateSelectprodLevel3Desc is stands for cancel recall Api.
        if(!dublicateSelectprodLevel3Desc.equals(selectprodLevel3Desc)) {
            progressBar.setVisibility(View.VISIBLE);
            requestTransferRequestSubcategory(selectprodLevel3Desc);
            dublicateSelectprodLevel3Desc=selectprodLevel3Desc;
        }

    }
    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
    }
    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
    }
    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}








