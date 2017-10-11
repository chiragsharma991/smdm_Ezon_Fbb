package apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.MPPointD;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.ToDo_Modal;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.MyMarkerView;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;



public class StockPullFragment extends Fragment implements OnChartGestureListener, View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Gson gson;
    private SharedPreferences sharedPreferences;
    private String userId;
    private String bearertoken,device_Id;
    private ToDo_Modal toDo_Modal;
    private int count = 0;
    private int limit = 100;
    private int offsetvalue = 0;
    private int level = 1;
    private boolean checkNetworkFalse = false;
    private RequestQueue queue;
    private ArrayList<ToDo_Modal> ReceiverSummaryList,subcategoryList;
    private String recache;
    public static String value = "", val ="";
    private String mParam1;
    private String mParam2;
    private String mc_name, mc_name_code, subcategory_name,selected_subCategory;
    private OnFragmentInteractionListener mListener;
    private Context context;
    private ViewGroup view;
    private RecyclerView recyclerView;
    private BarChart barChart;
    private String selectprodLevel3Desc="";
    private HashMap<Integer, String> mapValues;
    private String dublicateSelectprodLevel3Desc="";
    public static String store_Code;
    private ProgressBar progressBar;
    private Button stock_fragmentSubmit;
    StockPullAdapter stockPullAdapter;
    private boolean[] selectMc;
    private String TAG="StockPullFragment", storeCode;
    private LinearLayout spinner;
    private TextView spinner_text;
    private AlertDialog dialog;
    private ImageButton dropdkown;
    private ArrayList<String> selectedMcList;


    public StockPullFragment(String store_Code) {
        // Required empty public constructor
        this.store_Code = store_Code;

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (checkNetworkFalse) {
                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                Log.e("TAG", "checkNetworkFalse: " );

            }
        }
    }


    // TODO: Rename and change types and number of parameters
    public static StockPullFragment newInstance(String param1, String param2) {
        StockPullFragment fragment = new StockPullFragment(store_Code);
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
        gson = new Gson();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        device_Id = sharedPreferences.getString("device_id","");
        Log.e( "initialise: ", ""+device_Id);
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        storeCode = store_Code.substring(0,4);
        selectedMcList = new ArrayList<String>();
        //barChart = (BarChart) view.findViewById(R.id.bar_chart);
        spinner=(LinearLayout)view.findViewById(R.id.spinner);
        spinner_text=(TextView)view.findViewById(R.id.spinner_text);
        dropdkown=(ImageButton)view.findViewById(R.id.dropdkown);
        progressBar=(ProgressBar)view.findViewById(R.id.progressBar);
        spinner_text=(TextView)view.findViewById(R.id.spinner_text);
        recyclerView = (RecyclerView) view.findViewById(R.id.stockPull_list);
        stock_fragmentSubmit = (Button)view.findViewById(R.id.stock_fragmentSubmit);
        dropdkown.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        spinner_text.setOnClickListener(this);
        dropdkown.setOnClickListener(this);
        spinner_text.setText("Select Subcategory");
        subcategoryList=new ArrayList<>();


        stock_fragmentSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(subcategoryList.size() == 0)
                {
                    Toast.makeText(context,"No data for submission",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Log.e(TAG, "onClick: "+"clicked event");
                    onSubmit();

                }
            }
        });
        //progressBar = (RelativeLayout) view.findViewById(R.id.progressBar);
        //progressBar.setVisibility(View.GONE);
        //barChart.setOnChartGestureListener(this);
    }

    private void onSubmit()
    {
        if (!(subcategoryList.size() == 0))
        {
            // Log.e( "onSubmit: ", ""+selected_subCategory + "\n"+To_Do.deviceId);
            JSONArray jsonArray = new JSONArray();
            try {

                for(int i = 0 ; i <selectMc.length; i++)
                {
                    if(selectMc[i])
                    {
                        selectedMcList.add(subcategoryList.get(i).getLevelCode());
                    }
                }
                String[] array = (String[]) selectedMcList.toArray(new String[0]);
                String subMC = Arrays.toString(array);
                subMC = subMC.replace("[", "");
                subMC = subMC.replace("]", "");
                subMC = subMC.replace(", ", ",");
                JSONObject obj = new JSONObject();
//                        obj.put("option","");
//                        obj.put("prodAttribute4","");
                obj.put("prodLevel6Code",subMC);//MCCodeDesc
                obj.put("prodLevel3Code",selected_subCategory);//prodLevel3Desc
                //obj.put("deviceId",device_Id);
                obj.put("storeCode",storeCode);

                jsonArray.put(obj);
                if(jsonArray.length() != 0){
                    requestReceiverSubmitAPI(context, jsonArray);
                }
                else{
                    Toast.makeText(context, "Please select at least one option.", Toast.LENGTH_SHORT).show();
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    private  boolean allfalse (boolean[] values) {
        for (boolean value : values) {
            if (!value)
                return false;
        }
        return true;
    }


    private void MainMethod()
    {
        Reusable_Functions.sDialog(context, "Loading...");
        requestTransferRequestsummary();

    }



    private void requestTransferRequestsummary()
    {
        if (Reusable_Functions.chkStatus(context))
        {
            //https://smdm.manthan.com/v1/display/stocktransfer/receiverdetail/69-4795?level=1
            String url = ConstsCore.web_url + "/v1/display/stocktransfer/receiverdetail/" + userId + "?storeCode=" +storeCode + "&level=" + level + "&offset=" + offsetvalue + "&limit=" + limit + "&recache=" + recache ;
            Log.e("TAG", "requestTransferRequestsummary: " + url);
            final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,

                    new Response.Listener<JSONArray>()
                    {
                        @Override
                        public void onResponse(JSONArray response)
                        {
                            Log.d("TAG", "onResponse: receiverdetail" + response);
                            try {
                                if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                    Reusable_Functions.hDialog();
                                    checkNetworkFalse = true;
                                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                    Log.e("TAG", "requestTransferRequestsummary: " );

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
                                        Log.e(TAG, "onResponse: "+ ReceiverSummaryList.get(i).getStkQtyAvl());

                                    }
                                    count = 0;
                                    limit = 100;
                                    offsetvalue = 0;

                                }

                                // multidatasetBarGraph(ReceiverSummaryList);
                                // recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), 48 == Gravity.CENTER_HORIZONTAL ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                // recyclerView.setOnFlingListener(null);
                                // StockPullAdapter stockPullAdapter;
                                // stockPullAdapter = new StockPullAdapter(ReceiverSummaryList,getActivity());
                                // recyclerView.setAdapter(stockPullAdapter);
                                Reusable_Functions.hDialog();
                                if(!value.equals(""))
                                    spinner_text.setText(val);
                                requestTransferRequestSubcategory(value, "");
                            }
                            catch (Exception e)
                            {
                                Reusable_Functions.hDialog();
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

    private void requestTransferRequestSubcategory(String prodLevel3Desc, final String nodata)
    {
        if (Reusable_Functions.chkStatus(context)) {
            dropdkown.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            subcategoryList=new ArrayList<>();
            level=2;  // 2 for MC
            prodLevel3Desc = prodLevel3Desc.replace(" ", "%20").replace("&", "%26");

            //https://smdm.manthan.com/v1/display/stocktransfer/receiverdetail/69-4795?level=2&prodLevel3Desc=BF011C-BF - Ladies ethnicwear
            String url = ConstsCore.web_url + "/v1/display/stocktransfer/receiverdetail/" + userId + "?storeCode=" +storeCode + "&level=" + level
                    +"&prodLevel3Desc="+prodLevel3Desc+"&offset=" + offsetvalue + "&limit=" + limit + "&recache=" + recache;

            Log.e("TAG", "requestTransferRequestsummary: " + url);
            final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,

                    new Response.Listener<JSONArray>()
                    {
                        @Override
                        public void onResponse(JSONArray response)
                        {
                            Log.d("TAG", "onResponse: subcategory " + response);
                            try
                            {
                                if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                    if(nodata.equals("nodata")){
                                        spinner_text.setText("Select Subcategory");
                                        subcategoryList=new ArrayList<>();
                                        requestTransferRequestsummary();
                                        return;
                                    }else{
                                        checkNetworkFalse = true;
                                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                        subcategoryList.clear();
                                        //  stockPullAdapter.notifyDataSetChanged();
                                        stockPullAdapter = new StockPullAdapter(subcategoryList,selectMc, getActivity(), null);
                                        recyclerView.setAdapter(stockPullAdapter);

                                        Log.e("TAG", "requestTransferRequestSubcategory: " ); //
                                        dropdkown.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                        return;
                                    }


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
                                    dropdkown.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);

                                }

                                selectMc=new boolean[subcategoryList.size()];
                                for (int i = 0; i <subcategoryList.size() ; i++) {
                                    selectMc[i]=false;
                                    selectedMcList.clear();
                                    Log.e("select Mc :",""+selectMc[i] + selectedMcList);
                                }
                                recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), 48 == Gravity.CENTER_HORIZONTAL ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                                recyclerView.setOnFlingListener(null);
                                stockPullAdapter = new StockPullAdapter(subcategoryList,selectMc, getActivity(), new StockPullAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        Log.e(TAG, "onItemClick: ----"+position );
//                                        subcategory_name = ReceiverSummaryList.get(position).getLevel();
                                        mc_name = subcategoryList.get(position).getLevel();
                                        mc_name_code = subcategoryList.get(position).getLevelCode();
//                                        Log.e( "onItemClick: ",""+subcategory_name + "and mc name is"+mc_name +"\t"+subcategoryList.get(position).getStkQtyAvl());
                                        new Details().StartActivity(context, selected_subCategory,mc_name, mc_name_code,subcategoryList.get(position).getStkQtyAvl(), storeCode);

                                    }
                                });
                                recyclerView.setAdapter(stockPullAdapter);

                            } catch (Exception e) {
                                Toast.makeText(context, "data failed...." + e.toString(), Toast.LENGTH_SHORT).show();
                                dropdkown.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "server not responding..", Toast.LENGTH_SHORT).show();
                            dropdkown.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            error.printStackTrace();
                        }
                    }

            )
            {
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
        }

    }


    private void requestReceiverSubmitAPI (final Context mcontext, JSONArray object)
    {
        if (Reusable_Functions.chkStatus(mcontext)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(mcontext, "Submitting data…");

            String url = ConstsCore.web_url + "/v1/save/stocktransfer/receiversubmitdetail/"  + userId;//+"?recache="+recache  // +"?storeCode=" + storeCode
            Log.e("requestReceiverSubmitAPI: ",""+url + "\t" + object.toString() );
            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, object.toString(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("responseReceiverSubmitAPI "," "+response);
                            try {
                                if (response == null || response.equals("")) {
                                    Reusable_Functions.hDialog();
                                    Toast.makeText(mcontext, "Sending data failed...", Toast.LENGTH_LONG).show();

                                } else {
                                    Reusable_Functions.hDialog();
                                    String result = response.getString("status");
                                    for (int i = 0; i <subcategoryList.size() ; i++) {
                                        selectMc[i]=false;
                                    }
                                    Log.e("subcategoryList.size() ", " "+subcategoryList.size());
                                    Log.e("selectedMcList.size() ", " "+selectedMcList.size());

                                    stockPullAdapter.notifyDataSetChanged();
                                    Toast.makeText(mcontext, "" + result, Toast.LENGTH_LONG).show();
//                                    if(subcategoryList.size() == 0){
//                                        //  initialise();
//
//                                        requestTransferRequestsummary("no data");
//                                    }else{
                                        requestTransferRequestSubcategory(value, "nodata");

//                                    }

//                                    initialise();
//                                    MainMethod();

                                 /*   Intent intent = new Intent(context, SnapDashboardActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(intent);*/
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Reusable_Functions.hDialog();

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Reusable_Functions.hDialog();
                            Log.e(TAG, "onErrorResponse: " + error);
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

                for (int i = 0; i <receiverSummaryList.size(); i++)
                {
                    yVals1.add(new BarEntry(i, (float) receiverSummaryList.get(i).getStkOnhandQtyRequested()));
                    yVals2.add(new BarEntry(i, (float) receiverSummaryList.get(i).getStkQtyAvl()));
                    labels[i] = receiverSummaryList.get(i).getLevel();
                    mapValues.put(i,labels[i]);
                    Log.e("TAG", "labels: " + labels[i]);

                }
                BarDataSet set1, set2;

                if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0)
                {
                    set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
                    set2 = (BarDataSet) barChart.getData().getDataSetByIndex(1);
                    set1.setValues(yVals1);
                    set2.setValues(yVals2);
                    barChart.getData().notifyDataChanged();
                    barChart.notifyDataSetChanged();
                }
                else
                {
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
                barChart.setVisibleXRange(0,2);
                xl.setAxisMaximum(receiverSummaryList.size());
                barChart.moveViewToX(10);
                //barChart.getXAxis().setAxisMaximum(receiverSummaryList.size());
                barChart.groupBars(0, groupSpace, barSpace);
                barChart.setTouchEnabled(true);
                barChart.setPinchZoom(true);
                barChart.setScaleEnabled(true);
                barChart.setFitBars(true);
                barChart.invalidate();
                barChart.animateXY(3000, 3000);
            }
            else
            {

                barChart.clear();

            }

        }
        catch (Exception e)
        {
            barChart.clear();
            Log.e("TAG", "multidatasetBarGraph: catch error" + e.getMessage());

        }

    }

    private void NetworkProcess()
    {

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }
    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
    }
    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture)
    {
    }
    @Override
    public void onChartLongPressed(MotionEvent me) {
    }
    @Override
    public void onChartDoubleTapped(MotionEvent me) {
    }
    @Override
    public void onChartSingleTapped(MotionEvent me)
    {
        float tappedX = me.getX();
        float tappedY = me.getY();
        MPPointD point = barChart.getTransformer(YAxis.AxisDependency.LEFT).getValuesByTouchPoint(tappedX, tappedY);
        Log.e("onChartSingleTapped", "tapped at: " + (int)point.x + "," + (int)point.y);
        String selectprodLevel3Desc=mapValues.get((int)point.x);
        selected_subCategory = selectprodLevel3Desc;
        Log.e("onChartSingleTapped: ",""+selected_subCategory );

        Log.e("TAG", "onChartSingleTapped: Values "+selectprodLevel3Desc );
        // @parms: dublicateSelectprodLevel3Desc is stands for cancel recall Api.
        if(!dublicateSelectprodLevel3Desc.equals(selectprodLevel3Desc))
        {
           // progressBar.setVisibility(View.VISIBLE);
            requestTransferRequestSubcategory(selectprodLevel3Desc, "");
            dublicateSelectprodLevel3Desc=selectprodLevel3Desc;
        }

    }
    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY)
    {
    }
    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY)
    {
    }
    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY)
    {
    }

    @Override
    public void onClick(View view) {

        Log.e(TAG, "onClick: " );
        if(ReceiverSummaryList.size()>0)
            commentDialog();

    }


    private void commentDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        LayoutInflater inflater =getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.subcategorylist, null);
        ListView categoryList = (ListView) v.findViewById(R.id.categoryList);
        final ArrayList<String> subCategoryList = new ArrayList<>();
        CategoryAdapter adapter = new CategoryAdapter(context,ReceiverSummaryList);
        categoryList.setAdapter(adapter);
        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                Log.e("TAG", "onItemClick: "+position );
                 val = ReceiverSummaryList.get(position).getLevel();
                value =  ReceiverSummaryList.get(position).getLevelCode();
                spinner_text.setText(val);
                selected_subCategory=value;
                dialog.dismiss();
                requestTransferRequestSubcategory(value, "");

            }
        });
        builder.setView(v);
        dialog = builder.create();
        dialog.show();
    }


    // --------- Adapter-----------

    private class CategoryAdapter extends BaseAdapter
    {
        private final Context context;
        private final ArrayList<ToDo_Modal> receiverSummaryList;

        public CategoryAdapter(Context context, ArrayList<ToDo_Modal> receiverSummaryList)
        {
            this.context=context;
            this.receiverSummaryList=receiverSummaryList;
        }

        @Override
        public int getCount() {
            return receiverSummaryList.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View row;
            row = inflater.inflate(R.layout.simple_list_item, null, false);
            TextView title, detail;
            title = (TextView) row.findViewById(R.id.storeList);
            title.setText(receiverSummaryList.get(i).getLevel());
            return (row);
        }
    }

    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}








