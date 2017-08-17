package apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.PolicyExchangeHO;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.PolicyExchangeHO.Adapter.PolicyExchange_ReportAdapter;
import apsupportapp.aperotechnologies.com.designapp.Httpcall.ApiRequest;
import apsupportapp.aperotechnologies.com.designapp.Httpcall.HttpResponse;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.SeasonCatalogue.mpm_model;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by rkanawade on 24/07/17.
 */

public class PolicyExchange_Reports extends Fragment implements TabLayout.OnTabSelectedListener, HttpResponse, OnChartValueSelectedListener,PolicyExchange_ReportAdapter.RecyclerViewclick
{

    private Context context;
    private ReportInterface mCallback;
    private View v;
    private RecyclerView listview;
    private TextView storedesc,title;
    private PieChart pieChart=null;
    private TabLayout Tabview;
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private String userId, store, bearertoken, geoLeveLDesc;
    private String TAG = "PolicyExchange_Notify";
    private String view_params = "LD";
    private PolicyExchange_ReportAdapter adapter=null;
    private ArrayList<mpm_model> callbacklist=null, piechartList=null;
    private String attribute14 = "YES";
    private String feedbackKey = "3";
    private boolean ActivityCreated = false;
    private CardView card;
    private ProgressBar processbar_view;
    private LinearLayout addleggend;
    private float totalFeedbackCount, callbackFeedbackCount, nocallbackFeedbackCount;
    private int runningId;
    private String callback_header="Callback Required from CSD";


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        try {
            // mCallback = (ReportInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(this.toString()
                    + " must implement Interface");
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e(TAG, "setUserVisibleHint: " + isVisibleToUser);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView: ");
        context = getContext();
        View view = inflater.inflate(R.layout.fragment_policyexchange_reports, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG, "onActivityCreated: ");
        ActivityCreated = true;
        v = getView();
        initialiseUI();
    }

    private void initialiseUI() {
        listview = (RecyclerView) v.findViewById(R.id.listView);
        card = (CardView) v.findViewById(R.id.cf_cardView);
        storedesc = (TextView) v.findViewById(R.id.txtStoreCode);
        processbar_view = (ProgressBar) v.findViewById(R.id.processbar);
        processbar_view.setVisibility(View.GONE);
        title =(TextView) v.findViewById(R.id.cf_text);
        pieChart = (PieChart) v.findViewById(R.id.cf_pieChart);
        addleggend = (LinearLayout) v.findViewById(R.id.addleggend);
        pieChart.setOnChartValueSelectedListener(this);
        Tabview = (TabLayout) v.findViewById(R.id.tabview);
        Tabview.addTab(Tabview.newTab().setText("Yesterday"));
        Tabview.addTab(Tabview.newTab().setText("Last Week"));
        Tabview.addTab(Tabview.newTab().setText("Last Month"));
        Tabview.setOnTabSelectedListener(this);
        MainMethod();
        Apicallback(0, true, "Feedback with callback");



    }

    private void Apicallback(int id, boolean loader, String s) {
        runningId=id;
        if (Reusable_Functions.chkStatus(context)) {
            if (loader) {
                Reusable_Functions.sDialog(context, "Loading...");
            }
            mpm_model model = new mpm_model();
            requestcallback(model, id, s);            //this id is select for url.

        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }

    }


    private synchronized void requestcallback(mpm_model model, int id, String data) {

        String url = "";
        ApiRequest api_request;
        switch (id) {
            // case 0 and 1 will follow like first update list and after pie chart
            case 0:
                url = ConstsCore.web_url + "/v1/display/feedbackdisplaysummary/" + userId + "?feedbackKey="+feedbackKey + "&view=" + view_params + "&recache=true"; //Pie chart Api
                api_request = new ApiRequest(context, bearertoken, url, TAG, queue, model, 1, this, data);  // 1 is id for new api response
                break;
            case 1:
                url = ConstsCore.web_url + "/v1/display/feedbackdisplaysummarydetail/" + userId + "?feedbackKey="+feedbackKey + "&view=" + view_params + "&recache=true" + "&attribute14=" + attribute14; //Details list Api
                api_request = new ApiRequest(context, bearertoken, url, TAG, queue, model, 0, this, data);  // 0 is id for call finish response.

                break;
            case 2:  // this is for only change list
                url = ConstsCore.web_url + "/v1/display/feedbackdisplaysummarydetail/" + userId + "?feedbackKey="+feedbackKey + "&view=" + view_params + "&recache=true" + "&attribute14=" + attribute14; //Details list Api
                api_request = new ApiRequest(context, bearertoken, url, TAG, queue, model, 2, this, data);  // 1 is id for call another api after response
                break;
            default:
                break;


        }
    }

    @Override
    public void response(ArrayList<mpm_model> list, int id) {
        Log.e(TAG, "response: sucess"+id );
        switch (id) {
            // case 0 and 1 will follow like first api call and set view in case 0;
            case 0:
                Log.e(TAG, "callback list log: " );
                callbacklist = new ArrayList<>();
                callbacklist.addAll(list);
                setlistView(callbacklist);
                Reusable_Functions.hDialog();
                processbar_view.setVisibility(View.GONE);
                break;
            case 1:
                Log.e(TAG, "Pie chart list log: " );
                piechartList = new ArrayList<>();
                piechartList.addAll(list);
                setPiechart(piechartList);
                mpm_model model = new mpm_model();
                requestcallback(model, 1, "Feedback with callback"); //id 1 for call another api
                break;

            case 2:  // only for update listview
                callbacklist = new ArrayList<>();
                callbacklist.addAll(list);
                setlistView(callbacklist);
                processbar_view.setVisibility(View.GONE);
                break;

            default:
                break;

        }
    }

    private void setPiechart(ArrayList<mpm_model> piechartList)
    {
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        totalFeedbackCount = (float) piechartList.get(0).getTotalFeedbackCount();
        callbackFeedbackCount = (float) piechartList.get(0).getCallbackFeedbackCount();
        nocallbackFeedbackCount = (float) piechartList.get(0).getNoCallbackFeedbackCount();
        Log.e(TAG, "Piechart: "+totalFeedbackCount+" "+callbackFeedbackCount+" "+nocallbackFeedbackCount );
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#e8112d"));    //#20b5d3
        colors.add(Color.parseColor("#c6c4c4"));    //#21d24c    //#f8f6f6
        entries.add(new PieEntry(callbackFeedbackCount, "Feedback with Callback"));
        entries.add(new PieEntry(nocallbackFeedbackCount, "Feedback"));
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);
        dataSet.setValueLineWidth(0.5f);
        dataSet.setValueTextColor(Color.BLACK);
        PieData pieData = new PieData(dataSet);
        pieChart.setDrawMarkers(false);
        pieData.setValueTextSize(11f);
        dataSet.setXValuePosition(null);
        dataSet.setYValuePosition(null);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setHoleRadius(70);
        pieChart.setCenterText("Total Interactions" + "\n" + Math.round(totalFeedbackCount));
        pieChart.setCenterTextColor(Color.parseColor("#000000"));
        pieChart.setCenterTextSize(15f);
        pieChart.setTransparentCircleRadius(0);
        pieChart.setData(pieData);
        pieChart.invalidate();
        pieChart.animateXY(4000, 4000);
        pieChart.setDescription(null);
        pieChart.setTouchEnabled(false);
        Legend l = pieChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setFormSize(11f);
        l.setEnabled(false);
        addViewLayout();
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

        PieEntry pe = (PieEntry) e;
        Log.e("-----", "" + pe.getLabel());
        switch (pe.getLabel()) {
            case "Feedback with Callback":
                if (attribute14.equals("NO"))
                    attribute14 = "YES";
                Apicallback(0, false, "Feedback with callback");
                processbar_view.setVisibility(View.VISIBLE);
                break;

            case "Feedback":
                if (attribute14.equals("YES"))
                    attribute14 = "NO";
                Apicallback(0, false, "Feedback");
                processbar_view.setVisibility(View.VISIBLE);

                break;

            default:
                break;

        }
    }

    @Override
    public void onNothingSelected()
    {

    }

    @Override
    public void onclickList(int position) {
        Log.e(TAG, "onclickList: "+position );

        Callback_PolicyExchange.startScreen(context,view_params,attribute14,feedbackKey,
                callbacklist.get(position).getAttribute1(),callbacklist.get(position).getArcDate(),callback_header);

    }


    private void setlistView(ArrayList<mpm_model> callbacklist)
    {

        listview.setLayoutManager(new LinearLayoutManager(context));
        listview.setLayoutManager(new LinearLayoutManager(context, 48 == Gravity.CENTER_HORIZONTAL ?
                LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
        adapter = new PolicyExchange_ReportAdapter(callbacklist, context,this);
        listview.setAdapter(adapter);
        //  listview.setNestedScrollingEnabled(true);

    }

    @Override
    public void nodatafound() {
        Log.e(TAG, "response: null");
        Reusable_Functions.hDialog();
        processbar_view.setVisibility(View.GONE);
        try {

            if (piechartList == null) {
                pieChart.clearValues();
                pieChart.clearAnimation();
                pieChart.clearFocus();
                pieChart.clear();
                pieChart.invalidate();
//                piechartList=null;

            }
            if (callbacklist != null) {
                callbacklist.clear();
                adapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            Log.e(TAG, "nodatafound: catch error " + e.getMessage());
        }
    }


    private void MainMethod()
    {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = sharedPreferences.getString("userId", "");
     //   userId = userId.substring(0, userId.length() - 5);    // Hourly works only userid=username;
        store = sharedPreferences.getString("storeDescription", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLeveLDesc = sharedPreferences.getString("geoLeveLDesc", "");
        storedesc.setText(store);
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab)
    {
        int checkedId = Tabview.getSelectedTabPosition();

        Log.e(TAG, "onTabSelected: " + checkedId);
        switch (checkedId) {
            case 0: // Yesterday
                view_params = "LD";
                Apicallback(0, true, "Feedback with callback");
                break;
            case 1: // Last Week
                view_params = "LW";
                Apicallback(0, true, "Feedback with callback");
                break;
            case 2: // Last Month
                view_params = "LM";
                Apicallback(0, true, "Feedback with callback");
                break;
            default:
                break;

        }

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void addViewLayout() {
        int[] colors = {Color.parseColor("#e8112d"), Color.parseColor("#c6c4c4"), Color.parseColor("#f5204c"), Color.parseColor("#f89a20"), Color.parseColor("#78bc2c"), Color.parseColor("#db5a81"), Color.parseColor("#955eb9"), Color.parseColor("#dcd52d"), Color.parseColor("#6666FF"), Color.parseColor("#008040")};
        addleggend.removeAllViewsInLayout();
        LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        int k = 0;
        while (k < 2)
        {
            Log.e(TAG, "addViewLayout: " + k);
            ViewGroup view = (ViewGroup) layoutInflater.inflate(R.layout.activity_band_hrl_legend, null);
            TextView txt_legend_color = (TextView) view.findViewById(R.id.txt_legend_color);
            TextView txt_legend_name = (TextView) view.findViewById(R.id.txt_legend);
            TextView txt_legend_val = (TextView) view.findViewById(R.id.txt_legend_val);
            txt_legend_name.setTag(k);
            txt_legend_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (int) view.getTag();
                    Log.e(TAG, "onClick: " + position);
                    switch (position) {
                        case 0:
                            if (attribute14.equals("NO")){
                                attribute14 = "YES";
                                Apicallback(2, false, "Feedback with callback");
                                title.setText("Callback Required from CSD");
                                callback_header = title.getText().toString();
                                processbar_view.setVisibility(View.VISIBLE);
                            }
                            break;

                        case 1:
                            if (attribute14.equals("YES")){
                                attribute14 = "NO";
                                Apicallback(2, false, "Feedback");
                                title.setText("No Callback Required");
                                callback_header = title.getText().toString();
                                processbar_view.setVisibility(View.VISIBLE);
                            }
                            break;

                        default:
                            break;
                    }
                }
            });
            txt_legend_color.setBackgroundColor(colors[k]);
            switch (k) {
                case 0:
                    txt_legend_name.setText("Feedback with Callback");
                    txt_legend_val.setText("" + (int)callbackFeedbackCount);
                    addleggend.addView(view);
                    break;
                case 1:
                    txt_legend_name.setText("Feedback");
                    txt_legend_val.setText("" +(int)nocallbackFeedbackCount);
                    addleggend.addView(view);
                    break;
                default:
                    break;
            }
            k++;
        }
    }


    public interface ReportInterface {

        void onTrigger(int position);

    }
}
