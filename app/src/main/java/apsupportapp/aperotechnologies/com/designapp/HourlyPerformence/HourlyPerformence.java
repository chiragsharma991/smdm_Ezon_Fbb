package apsupportapp.aperotechnologies.com.designapp.HourlyPerformence;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.R;
import info.hoang8f.android.segmented.SegmentedGroup;

public class HourlyPerformence extends AppCompatActivity {

    private SegmentedGroup segmentButton;
    private PieChart pieChart;
    private CombinedChart barChart;
    private ListView listView;
    private TextView netSales,archPercent,spend,units;
    private ArrayList<String>mobileArray;
    private View footer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_performence);
        intialise();
        functionality();
    }

    private void functionality() {
        mobileArray= new ArrayList<>();
        mobileArray.add("FBB");
        mobileArray.add("E zone");
        mobileArray.add("Brand Factory");
        mobileArray.add("Big Bazar");
        final ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, mobileArray);
        listView.addFooterView(footer);
        Log.e("TAG", "list size: "+mobileArray.size() );


        listView.setAdapter(adapter);

    }

    private void intialise() {
        segmentButton=(SegmentedGroup)findViewById(R.id.hrl_segmented);
        pieChart=(PieChart) findViewById(R.id.hrl_piechart);
        barChart=(CombinedChart) findViewById(R.id.hrl_barchart);
        listView=(ListView) findViewById(R.id.hrl_geoPerformance_listview);
        netSales=(TextView)findViewById(R.id.hrl_netSales);
        archPercent=(TextView)findViewById(R.id.hrl_arh);
        spend=(TextView)findViewById(R.id.hrl_spend);
        units=(TextView)findViewById(R.id.hrl_units);
        footer=((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_listview,null,false);
    }





}
