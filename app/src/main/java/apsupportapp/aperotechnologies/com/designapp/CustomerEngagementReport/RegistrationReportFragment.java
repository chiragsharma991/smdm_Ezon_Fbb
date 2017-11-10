package apsupportapp.aperotechnologies.com.designapp.CustomerEngagementReport;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import apsupportapp.aperotechnologies.com.designapp.CustomerEngagementReport.Adapter.CustomerDetailsAdapter;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.model.VisualReport;

/**
 * Created by pamrutkar on 09/11/17.
 */
public class RegistrationReportFragment extends Fragment implements TabLayout.OnTabSelectedListener
{
    private Context context;
    private View view;
    private TabLayout Tabview;
    private RecyclerView recycler_regreport;
    private ArrayList<String> listCustomer;
    PieChart regreport_pieChart;
    CustomerDetailsAdapter cusomerDetailsAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getContext();
         view = inflater.inflate(R.layout.fragment_registration_report, container, false);
        initialiseUI();
        createPieChart();
        return view;
    }


    private void initialiseUI()
    {
        listCustomer = new ArrayList<String>();
        Tabview = (TabLayout) view.findViewById(R.id.tabview_regReport);
        Tabview.addTab(Tabview.newTab().setText("Daily"), 0);
        Tabview.addTab(Tabview.newTab().setText("Weekly"), 1);
        Tabview.addTab(Tabview.newTab().setText("Monthly"), 2);
        Tabview.setOnTabSelectedListener(this);
        regreport_pieChart = (PieChart)view.findViewById(R.id.regreport_pieChart);
        recycler_regreport = (RecyclerView)view.findViewById(R.id.recycler_regreport);
        recycler_regreport.setLayoutManager(new LinearLayoutManager(context));
        cusomerDetailsAdapter = new CustomerDetailsAdapter(context,  recycler_regreport, listCustomer);
        recycler_regreport.setAdapter(cusomerDetailsAdapter);
    }


    private void createPieChart()
    {
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

          float likedOptions =60f;

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#20b5d3"));
        colors.add(Color.parseColor("#21d24c"));
        colors.add(Color.parseColor("#f5204c"));

        if (likedOptions > 0.0f) {

            entries.add(new PieEntry(likedOptions,"Likes"));

        }

        PieDataSet dataSet = new PieDataSet(entries,"");
        dataSet.setColors(colors);
        dataSet.setValueTextColor(Color.BLACK);
        PieData pieData = new PieData(dataSet);
        regreport_pieChart.setDrawMarkers(false);
        pieData.setValueTextSize(12f);
        dataSet.setXValuePosition(null);
        dataSet.setYValuePosition(null);
        regreport_pieChart.setEntryLabelColor(Color.WHITE);
        regreport_pieChart.setHoleRadius(65);
        regreport_pieChart.setHoleColor(Color.parseColor("#ffffff"));

        regreport_pieChart.setCenterText("90 \n Out of \n 150");
        regreport_pieChart.setCenterTextSize(10f);
        regreport_pieChart.setExtraTopOffset(10f);
        regreport_pieChart.setCenterTextColor(Color.BLACK);
        regreport_pieChart.setTransparentCircleRadius(0);
        regreport_pieChart.setData(pieData);
        regreport_pieChart.setNoDataText("");
        regreport_pieChart.setDescription(null);
        regreport_pieChart.invalidate();
        regreport_pieChart.setRotationAngle(270);
        regreport_pieChart.animateXY(1000,1000);
        regreport_pieChart.setTouchEnabled(true);
        Legend l = regreport_pieChart.getLegend();
        l.setEnabled(false);
   }



    @Override
    public void onTabSelected(TabLayout.Tab tab)
    {
        int checkedId = tab.getPosition();
        switch (checkedId)
        {
            case 0:
                Toast.makeText(context,"Position : "+tab.getPosition(),Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(context,"Position : "+tab.getPosition(),Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(context,"Position : "+tab.getPosition(),Toast.LENGTH_SHORT).show();
                break;

        }

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
