package apsupportapp.aperotechnologies.com.designapp.HourlyPerformence;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.communication.IOnItemFocusChangedListener;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.Httpcall.ApiRequest;
import apsupportapp.aperotechnologies.com.designapp.Httpcall.HttpResponse;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesAnalysisActivity1;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesAnalysisFilter;
import apsupportapp.aperotechnologies.com.designapp.SeasonCatalogue.mpm_model;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import info.hoang8f.android.segmented.SegmentedGroup;

public class HourlyPerformence extends AppCompatActivity implements HttpResponse, RadioGroup.OnCheckedChangeListener,TabLayout.OnTabSelectedListener {

    private SegmentedGroup segmentButton;
    public static PieChart pieChart;
    private CombinedChart barChart;
    private RecyclerView listView;
    private TextView netSales, archPercent, spend, units;
    private ArrayList<String> mobileArray;
    private View footer;
    private String TAG = "HourlyPerformence";
    private Context context;
    private SharedPreferences sharedPreferences;
    private String userId, bearertoken, geoLeveLDesc;
    private RequestQueue queue;
    private Gson gson;
    private int level, salesNetValue;
    private ArrayList<mpm_model> piechart_list, barchart_list, store_list;
    private HourlyAdapter hourlyAdapter;
    private NumberFormat thousandSaperator;
    private SegmentedGroup Hrl_segmented;
    private RadioButton Hrl_zonePerformance, Hrl_conceptPerformance;
    private RelativeLayout hrl_btnBack;
    private boolean concept_toggle = true, focusOnPie = false;  //concept toggle true and rotate pie focus position
    public static ProgressBar hrl_pi_Process;
    private String leveLDesc, geoLevel2Code, isMultiStore, value;  // for GeoLevel2Desc / GeoLevel3Desc.
    private int focusPosition, dupfocusPosition = 0;
    private LinearLayout addleggend;
    private TabLayout Tabview;
    public static Activity hourlyPerformance;
    private String from = null, storeCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_performence);
        getSupportActionBar().hide();
        context = this;
        hourlyPerformance = this;
        TAG = "HourlyPerformence";
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLeveLDesc = sharedPreferences.getString("geoLeveLDesc", "");
        geoLevel2Code = sharedPreferences.getString("concept","");
        isMultiStore = sharedPreferences.getString("isMultiStore","");
        value = sharedPreferences.getString("value","");
//        if (geoLeveLDesc.equals("E ZONE")) {
//            userId = sharedPreferences.getString("userId", "");       //E zone userid =username
//        } else {
            userId = sharedPreferences.getString("userId", "");       //FBB userid = username+store code
            // userId = userId.substring(0, userId.length() - 5);     // Hourly works only userid = username;
//        }
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();

        if(getIntent().getExtras() != null)
        {
            if (getIntent().getExtras().getString("selectedStringVal") != null) {
                from = "filter";
                storeCode = getIntent().getExtras().getString("selectedStringVal");
            }
        }

        intialise();
        functionality();
    }

    private void functionality()
    {
        if (Reusable_Functions.chkStatus(context)) {
            mpm_model model = new mpm_model();
            ApiCallBack(model, 0);
            //requestRunningPromoApi(selectedString);
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private synchronized void ApiCallBack(mpm_model model, int id) {
        String url;
        ApiRequest api_request;

        switch (id)
        {
            case 0:   //total values
                level = 5;
                if (focusOnPie) {
                    if(from != null)
                    {
                        url = ConstsCore.web_url + "/v1/display/hourlyplanactual/" + userId + "?level=" + level + "&" + leveLDesc + "&recache=true" + "&geoLevel2Code=" + geoLevel2Code+""+storeCode; //Detail Api
                    }
                    else
                    {
                        url = ConstsCore.web_url + "/v1/display/hourlyplanactual/" + userId + "?level=" + level + "&" + leveLDesc + "&recache=true" + "&geoLevel2Code=" + geoLevel2Code; //Detail Api
                    }

                    //hrl_pi_Process.setVisibility(View.VISIBLE);
                    Reusable_Functions.animateScaleOut(hrl_pi_Process);
                    api_request = new ApiRequest(context, bearertoken, url, TAG, queue, model, 0);  // 0 is id for identification

                } else {
                    if(from != null)
                    {
                        url = ConstsCore.web_url + "/v1/display/hourlyplanactual/" + userId + "?level=" + level + "&recache=true" + "&geoLevel2Code=" + geoLevel2Code+""+storeCode;
                    }
                    else
                    {
                        url = ConstsCore.web_url + "/v1/display/hourlyplanactual/" + userId + "?level=" + level + "&recache=true" + "&geoLevel2Code=" + geoLevel2Code; //Detail Api

                    }
                    Reusable_Functions.sDialog(context, "Loading...");
                    api_request = new ApiRequest(context, bearertoken, url, TAG, queue, model, 0);  // 0 is id for identification
                }

                break;

            case 1:  // pie chart values
                level = concept_toggle == true ? 2 : 3;
                if (focusOnPie) {
                    ApiCallBack(model, 2);  //calling for pie chart
                } else {
                    if(from != null)
                    {
                        url = ConstsCore.web_url + "/v1/display/hourlyplanactual/" + userId + "?level=" + level + "&recache=true" + "&geoLevel2Code=" + geoLevel2Code+""+storeCode; //Detail Api
                    }
                    else
                    {
                        url = ConstsCore.web_url + "/v1/display/hourlyplanactual/" + userId + "?level=" + level + "&recache=true" + "&geoLevel2Code=" + geoLevel2Code; //Detail Api
                    }
                        api_request = new ApiRequest(context, bearertoken, url, TAG, queue, model, 1);  // 1 is id for identification
                }
                break;
            case 2:   //Bar values
                level = 1;
                if (focusOnPie) {
                    if(from != null)
                    {
                        url = ConstsCore.web_url + "/v1/display/hourlyplanactual/" + userId + "?level=" + level + "&" + leveLDesc + "&recache=true" +"&geoLevel2Code="+ geoLevel2Code+""+storeCode; //Detail Api
                    }
                    else
                    {
                        url = ConstsCore.web_url + "/v1/display/hourlyplanactual/" + userId + "?level=" + level + "&" + leveLDesc + "&recache=true" + "&geoLevel2Code=" + geoLevel2Code; //Detail Api
                    }
                    api_request = new ApiRequest(context, bearertoken, url, TAG, queue, model, 2);  // 2 is id for identification
                } else {
                    if(from != null) {
                        url = ConstsCore.web_url + "/v1/display/hourlyplanactual/" + userId + "?level=" + level + "&recache=true" + "&geoLevel2Code=" + geoLevel2Code+""+storeCode; //Detail Api
                    }
                    else
                    {
                        url = ConstsCore.web_url + "/v1/display/hourlyplanactual/" + userId + "?level=" + level + "&recache=true" + "&geoLevel2Code=" + geoLevel2Code; //Detail Api
                    }
                        api_request = new ApiRequest(context, bearertoken, url, TAG, queue, model, 2);  // 2 is id for identification
                }

                break;

            case 3:   //Store values
                level = 4;
                if (focusOnPie) {
                    if(from != null) {
                        url = ConstsCore.web_url + "/v1/display/hourlyplanactual/" + userId + "?level=" + level + "&" + leveLDesc + "&recache=true" +"&geoLevel2Code="+ geoLevel2Code+""+storeCode; //Detail Api
                    }
                    else
                    {
                        url = ConstsCore.web_url + "/v1/display/hourlyplanactual/" + userId + "?level=" + level + "&" + leveLDesc + "&recache=true" +"&geoLevel2Code="+ geoLevel2Code; //Detail Api
                    }

                    api_request = new ApiRequest(context, bearertoken, url, TAG, queue, model, 3);  // 3 is id for identification
                } else {
                    if(from != null) {
                        url = ConstsCore.web_url + "/v1/display/hourlyplanactual/" + userId + "?level=" + level + "&recache=true" +"&geoLevel2Code="+ geoLevel2Code+""+storeCode; //Detail Api
                    }
                    else
                    {
                        url = ConstsCore.web_url + "/v1/display/hourlyplanactual/" + userId + "?level=" + level + "&recache=true" +"&geoLevel2Code="+ geoLevel2Code; //Detail Api
                    }

                    api_request = new ApiRequest(context, bearertoken, url, TAG, queue, model, 3);  // 3 is id for identification
                }


                break;

            default:
                break;
        }
    }

    private void callBarchart() {
        barChart.clear();
        ViewPortHandler handler = barChart.getViewPortHandler();
        handler.getScaleX();
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.parseColor("#2277b1"));
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int positon = (int) value;
                String vaslue = barchart_list.get(positon).getLevel();
                return vaslue;
            }
        });

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setTextColor(Color.parseColor("#2277b1"));
        leftAxis.setDrawGridLines(false);
        //  leftAxis.setEnabled(false);


        //  leftAxis.setValueFormatter(new MyvalueFormatter(1));
        // leftAxis.setAxisMaxValue(4f);
        //  leftAxis.setAxisMinValue(0f);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setTextColor(Color.parseColor("#2277b1"));
        rightAxis.setDrawGridLines(true);
        rightAxis.setEnabled(false);
        rightAxis.setValueFormatter(new MyvalueFormatter(1));
        //   rightAxis.setAxisMaxValue(800f);
        // rightAxis.setAxisMinValue(0f);

        // enable touch gestures
        barChart.setTouchEnabled(true);

        // enable scaling and dragging
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
        barChart.setDrawGridBackground(false);
        barChart.setGridBackgroundColor(android.R.color.black);
        //combinedChart.setHighlightPerDragEnabled(true);
        barChart.setBackgroundColor(Color.WHITE);
        barChart.setDescription(null);


        Legend l = barChart.getLegend();
        // modify the legend ... by default it is on the left
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        ArrayList<LegendEntry> label = new ArrayList<>();
        label.add(0, new LegendEntry("Net Sales", Legend.LegendForm.SQUARE, 10, 100, null, Color.parseColor("#2277b1")));
        label.add(1, new LegendEntry("PvA%", Legend.LegendForm.SQUARE, 10, 100, null, Color.parseColor("#48c430")));
        l.setCustom(label);
        l.setForm(Legend.LegendForm.SQUARE);

        CombinedData data = new CombinedData();
        data.setData(Bardata());
        data.setData(LineData());
        barChart.animateXY(2000, 2000);
        barChart.setData(data);
        barChart.invalidate();
        // combinedChart.setDescription("activity_hourly_nested_sc1");
    }

    public int getRandomColor()
    {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    private void callPiechart() {

        pieChart.clearChart();//pieChart.clearAnimation();//pieChart.clearFocus();pieChart.invalidate();
        int[] colors = {Color.parseColor("#20b5d3"), Color.parseColor("#21d24c"), Color.parseColor("#f5204c"), Color.parseColor("#f89a20"), Color.parseColor("#78bc2c"), Color.parseColor("#db5a81"), Color.parseColor("#955eb9"), Color.parseColor("#dcd52d"), Color.parseColor("#6666FF"), Color.parseColor("#008040")};

        addleggend.removeAllViewsInLayout();
        for (int i = 0; i < piechart_list.size(); i++) {

            if (piechart_list.size() <= 10) {
                int usecolor = colors[i];
                pieChart.addPieSlice(new PieModel(piechart_list.get(i).getLevel(), (int) piechart_list.get(i).getSalesContr(), usecolor));
                addViewLayout(usecolor, i);

            } else {

                int usecolor = getRandomColor();
                pieChart.addPieSlice(new PieModel(piechart_list.get(i).getLevel(), (int) piechart_list.get(i).getSalesContr(), usecolor));
                addViewLayout(usecolor, i);


            }
        }
        // pieChart.addPieSlice(new PieModel());
        pieChart.setInnerValueString(String.format("%.1f", piechart_list.get(0).getSalesContr()));
        pieChart.animate();
        // pieChart.setInnerPadding(54);
        pieChart.setInnerPaddingColor(Color.WHITE);
        pieChart.setDrawValueInPie(true);
        pieChart.setOnItemFocusChangedListener(new IOnItemFocusChangedListener() {
            @Override
            public void onItemFocusChanged(int _Position) {
                pieChart.setInnerValueString(String.format("%.1f", piechart_list.get(_Position).getSalesContr()));
                focusPosition = _Position;

            }
        });

        pieChart.invalidate();

    }

    private void addViewLayout(int usecolor, int j) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        if(j==0){
            ViewGroup view = (ViewGroup) layoutInflater.inflate(R.layout.activity_band_hrl_header_legend, null);
            addleggend.addView(view);
        }
        ViewGroup view = (ViewGroup) layoutInflater.inflate(R.layout.activity_band_hrl_legend, null);
        TextView txt_legend_color = (TextView) view.findViewById(R.id.txt_legend_color);
        TextView txt_legend_name = (TextView) view.findViewById(R.id.txt_legend);
        TextView txt_legend_val = (TextView) view.findViewById(R.id.txt_legend_val);
        txt_legend_color.setBackgroundColor(usecolor);
        txt_legend_name.setText(piechart_list.get(j).getLevel());
        txt_legend_val.setText("" + String.format("%.1f", piechart_list.get(j).getSalesContr()));
        addleggend.addView(view);
    }

    private void changefocuscall(int _Position) {
        focusOnPie = true;
        leveLDesc = concept_toggle == true ? "geoLevel2Desc=" + piechart_list.get(_Position).getLevel().replace(" ", "%20").replace("&", "%26").replace("%", "%25") : "geoLevel3Desc=" + piechart_list.get(_Position).getLevel().replace(" ", "%20").replace("&", "%26").replace("%", "%25");
        if (Reusable_Functions.chkStatus(context)) {
            if (ApiRequest.getRequest != null) {
                ApiRequest.getRequest.cancel();
            }
            mpm_model model = new mpm_model();
            ApiCallBack(model, 0);

        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private void callList()
    {
        listView.setLayoutManager(new LinearLayoutManager(context));
        listView.setLayoutManager(new LinearLayoutManager(listView.getContext(), 48 == Gravity.CENTER_HORIZONTAL ?
                LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
        hourlyAdapter = new HourlyAdapter(store_list, context, thousandSaperator);
        listView.setAdapter(hourlyAdapter);
    }

    private void intialise()
    {
        thousandSaperator = NumberFormat.getNumberInstance(new Locale("", "in"));
        //segmentButton = (SegmentedGroup) findViewById(R.id.hrl_segmented);
        pieChart = (PieChart) findViewById(R.id.hrl_piechart);
        barChart = (CombinedChart) findViewById(R.id.hrl_barchart);
        listView = (RecyclerView) findViewById(R.id.hrl_geoPerformance_listview);
        netSales = (TextView) findViewById(R.id.hrl_netSales);
        addleggend = (LinearLayout) findViewById(R.id.hrl_addleggend);
        hrl_btnBack = (RelativeLayout) findViewById(R.id.hrl_BtnBack);
        hrl_pi_Process = (ProgressBar) findViewById(R.id.hrl_pi_process);
        hrl_pi_Process.setVisibility(View.GONE);
        archPercent = (TextView) findViewById(R.id.hrl_arh);
        spend = (TextView) findViewById(R.id.hrl_spend);
        units = (TextView) findViewById(R.id.hrl_units);
       // Hrl_zonePerformance = (RadioButton) findViewById(R.id.hrl_zonePerformance);
       // Hrl_conceptPerformance = (RadioButton) findViewById(R.id.hrl_conceptPerformance);
       // Hrl_segmented = (SegmentedGroup) findViewById(R.id.hrl_segmented);
        Tabview = (TabLayout) findViewById(R.id.tabview);
        Tabview.addTab(Tabview.newTab().setText("Concept Performance"));
        Tabview.addTab(Tabview.newTab().setText("Zone Performance"));

        Tabview.setOnTabSelectedListener(this);
      //  Hrl_segmented.setOnCheckedChangeListener(this);
        footer = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_listview, null, false);
        hrl_btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RelativeLayout imgfilter = (RelativeLayout) findViewById(R.id.imgfilter);
        imgfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HourlyPerformence.this, HourlyStoreFilterActivity.class);
                intent.putExtra("checkfrom", "HourlyPerformance");
                startActivity(intent);
            }
        });

    }

    // this method is used to create data for line graph<br />
    public LineData LineData() {

        LineData linedata = new LineData();
        ArrayList<Entry> line = new ArrayList();
        for (int i = 0; i < barchart_list.size(); i++) {
            float x = i;
            float y = (float) barchart_list.get(i).getSalesAch();
            line.add(new Entry(x, y));
        }

        LineDataSet set = new LineDataSet(line, "PvA%");
        set.setColor(Color.parseColor("#48c430"));
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.parseColor("#48c430"));
        set.setCircleRadius(5f);
        set.setFillColor(Color.parseColor("#48c430"));
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setDrawValues(false);
        //  set.setValueTextColor(Color.rgb(240, 238, 70));
        set.setAxisDependency(YAxis.AxisDependency.RIGHT);
        linedata.addDataSet(set);
        return linedata;
    }

    // this method is used to create data for Bar graph<br />
    public BarData Bardata() {
        ArrayList<BarEntry> entries1 = new ArrayList<BarEntry>();
        for (int i = 0; i < barchart_list.size(); i++) {
            float x = i;
            float y = (float) barchart_list.get(i).getSaleNetVal();
            entries1.add(new BarEntry(x, y));
        }
        BarDataSet set1 = new BarDataSet(entries1, "Net Sales");
        set1.setColor(Color.parseColor("#2277b1"));
        set1.setValueTextColor(Color.rgb(60, 220, 78));
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setDrawValues(false);
        BarData bardata = new BarData(set1);
        return bardata;
    }


    @Override
    public void response(ArrayList<mpm_model> list, int id) {
        switch (id) {

            case 0:
                netSales.setText("₹" + thousandSaperator.format((int) list.get(0).getSaleNetVal()));
                archPercent.setText(""+String.format("%.1f", list.get(0).getSalesAch()) + "%");
                units.setText(String.format("%.1f", list.get(0).getUnitsBill()));
                spend.setText(String.format("%.1f", list.get(0).getSpendBill()));
                salesNetValue = (int) list.get(0).getSaleNetVal();
                mpm_model model = new mpm_model();
                ApiCallBack(model, 1);  //calling for pie chart
                //   Reusable_Functions.hDialog();
                break;

            case 1:
                piechart_list = new ArrayList<mpm_model>();
                for (mpm_model data : list)
                    piechart_list.add(data);

                model = new mpm_model();
                ApiCallBack(model, 2);  //calling for line chart
                // Reusable_Functions.hDialog();
                break;

            case 2:
                barchart_list = new ArrayList<mpm_model>();
                for (mpm_model data : list)
                    barchart_list.add(data);

                model = new mpm_model();
                ApiCallBack(model, 3);  //calling for line chart
                // Reusable_Functions.hDialog();
                break;

            case 3:
                store_list = new ArrayList<mpm_model>();
                for (mpm_model data : list)
                    store_list.add(data);
                // hrl_pi_Process.setVisibility(View.GONE);
                Reusable_Functions.animateScaleIn(hrl_pi_Process);
                setPerform();
                Reusable_Functions.hDialog();
                break;

            case 4:
                // zone perform
                piechart_list = new ArrayList<mpm_model>();
                for (mpm_model data : list)
                    piechart_list.add(data);
                callPiechart();
                Reusable_Functions.hDialog();
                break;

            case 5:
                // Concept perform
                piechart_list = new ArrayList<mpm_model>();
                for (mpm_model data : list)
                    piechart_list.add(data);
                callPiechart();
                Reusable_Functions.hDialog();
                break;

            default:
                Reusable_Functions.hDialog();
                break;

        }
    }

    @Override
    public void nodatafound()
    {
        try
        {
            Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
            Reusable_Functions.animateScaleIn(hrl_pi_Process);
            netSales.setText("₹" + "0");
            archPercent.setText("" + "0" + "%");
            units.setText(String.format("%.1f", 0.0));
            spend.setText(String.format("%.1f", 0.0));

            if (piechart_list.size() != 0 && focusOnPie == false) {
                pieChart.clearChart();
                pieChart.clearAnimation();
                pieChart.clearFocus();
                pieChart.invalidate();
            }
            if (barchart_list.size() != 0) {
                barChart.clear();
                barChart.clearAnimation();
                barChart.clearFocus();
                barChart.invalidate();

            }
            if (store_list.size() != 0) {
                store_list.clear();
                hourlyAdapter.notifyDataSetChanged();
            }
            if( focusOnPie == false){

                addleggend.removeAllViewsInLayout();
            }


        }
        catch (NullPointerException e)
        {

        }
    }

    private void setPerform() {
        callList();
        if (!focusOnPie) {
            callPiechart();
        }
        callBarchart();
        pieChart.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, final MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        public void run() {
                            if (event.getAction() == MotionEvent.ACTION_UP && focusPosition != dupfocusPosition) {
                                dupfocusPosition = focusPosition;
                                changefocuscall(focusPosition);

                            }
                        }
                    }, 1200);
                }
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int id) {
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        int checkedId= Tabview.getSelectedTabPosition();
        String url;
        ApiRequest api_request;
        focusOnPie = false;

        switch (checkedId) {
            case 0 : // Concept Performance
                concept_toggle = true;
                dupfocusPosition = 0;
                if (Reusable_Functions.chkStatus(context))
                {
                    if (ApiRequest.getRequest != null) {
                        ApiRequest.getRequest.cancel();
                    }
                    Reusable_Functions.animateScaleIn(hrl_pi_Process);
                    mpm_model model = new mpm_model();
                    ApiCallBack(model, 0);
                    //requestRunningPromoApi(selectedString);

                }
                else
                {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
                break;

            case 1 : // Zone performance
                concept_toggle = false;
                dupfocusPosition = 0;
                if (Reusable_Functions.chkStatus(context))
                {
                    if (ApiRequest.getRequest != null) {
                        ApiRequest.getRequest.cancel();
                    }
                    Reusable_Functions.animateScaleIn(hrl_pi_Process);
                    mpm_model model = new mpm_model();
                    ApiCallBack(model, 0);            //requestRunningPromoApi(selectedString);

                }
                else
                {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
                break;


        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }



    private class MyvalueFormatter implements IAxisValueFormatter {

        private final int id;

        public MyvalueFormatter(int id) {
            this.id = id;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            String format = id == 1 ? value + " %" : value + " Cr";
            return format;
        }
    }
}
