package apsupportapp.aperotechnologies.com.designapp.CustomerLoyalty;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.easing.linear.Linear;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;


import apsupportapp.aperotechnologies.com.designapp.R;

import static apsupportapp.aperotechnologies.com.designapp.CustomerLoyalty.CustomerDetailActivity.customerDetailsarray;

/**
 * Created by pamrutkar on 23/06/17.
 */
public class CustDetailBarChart extends AppCompatActivity
{
    private LinearLayout phn_call,mail_call;
    ViewPortHandler handler;
    private TextView txt_cdb_name,txt_cdb_mobile,txt_cdb_email;
    private RelativeLayout rel_cdb_back;
    private HorizontalBarChart barChart_Category,barChart_Brand,barChart_Preference1,barChart_Preference2;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cd_barchart);
        getSupportActionBar().hide();
        initialise_UI();
        callCategoryBarChart();
        callBrandBarChart();
        callMonthBarchart();
        callWeekBarchart();
        rel_cdb_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }



    private void initialise_UI()
    {
        rel_cdb_back = (RelativeLayout)findViewById(R.id.rel_back);
        txt_cdb_name = (TextView)findViewById(R.id.txt_cdb_name);
        txt_cdb_mobile = (TextView)findViewById(R.id.txt_cdb_mobileNo);
        txt_cdb_email = (TextView)findViewById(R.id.txt_cdb_email);
        phn_call = (LinearLayout) findViewById(R.id.lin_one);
        mail_call = (LinearLayout) findViewById(R.id.lin_two);
        barChart_Category = (HorizontalBarChart) findViewById(R.id.hbarchart_category);
        barChart_Brand = (HorizontalBarChart)findViewById(R.id.hbarchart_brands);
        barChart_Preference1 = (HorizontalBarChart)findViewById(R.id.hbarchart_month);
        barChart_Preference2 = (HorizontalBarChart)findViewById(R.id.hbarchart_week);
        txt_cdb_name.setText(customerDetailsarray.get(0).getFullName());
        txt_cdb_mobile.setText(customerDetailsarray.get(0).getMobileNumber());
        txt_cdb_email.setText(customerDetailsarray.get(0).getEmailAddress());

        phn_call.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(txt_cdb_mobile.getTextSize()!=0){

                   makePhoneCall(v);
                }

            }
        });
        mail_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(txt_cdb_email.getTextSize()!=0)
                {

                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto:"+txt_cdb_email.getText().toString()));
                    startActivity(Intent.createChooser(emailIntent, "Send feedback"));
                }

            }
        });
    }

    private void makePhoneCall(View v) 
    {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else
        {
            callPhone();
        }
    }

    private void callPhone()
    {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + txt_cdb_mobile.getText().toString()));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callPhone();
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    // Horizontal Bar chart for Category
    private void callCategoryBarChart()
    {
        barChart_Category.setDescription(null);
        barChart_Category.setDrawGridBackground(false); //Do not display grid background

        //Disable all interaction with the chart
        barChart_Category.setHighlightPerTapEnabled(false);
        barChart_Category.setHighlightPerDragEnabled(false);
        barChart_Category.setDoubleTapToZoomEnabled(false);
        handler = barChart_Category.getViewPortHandler();

        //Disable surroundings graphs (axis and legends)\

        XAxis xAxis = barChart_Category.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setEnabled(true);
        YAxis leftAxis = barChart_Category.getAxisLeft();
        YAxis rightAxis = barChart_Category.getAxisRight();
        leftAxis.setEnabled(true);
        rightAxis.setEnabled(false);

        BarDataSet set1;
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        valueSet1.add(new BarEntry((float)customerDetailsarray.get(0).getPreferredCcbSales(), 0));
        valueSet1.add(new BarEntry((float)customerDetailsarray.get(0).getPreferredCcb2Sales(), 1));
        valueSet1.add(new BarEntry((float)customerDetailsarray.get(0).getPreferredCcb3Sales(), 2));
        ArrayList<IBarDataSet> dataSet ;
        set1 = new BarDataSet(valueSet1, "Sales");
        dataSet = new ArrayList<>();
        dataSet.add(set1);

        BarData data = new BarData(dataSet);
        barChart_Category.animateXY(2000, 2000);
        barChart_Category.setData(data);
        Legend l = barChart_Category.getLegend();
        // modify the legend ... by default it is on the left
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setForm(Legend.LegendForm.SQUARE);
    }

    // Horizontal Bar chart for brand
    private void callBrandBarChart()
    {
        barChart_Brand.setDescription(null);
        barChart_Brand.setDrawGridBackground(false); //Do not display grid background

        //Disable all interaction with the chart
        barChart_Brand.setHighlightPerTapEnabled(false);
        barChart_Brand.setHighlightPerDragEnabled(false);
        barChart_Brand.setDoubleTapToZoomEnabled(false);
        handler = barChart_Brand.getViewPortHandler();

        //Disable surroundings graphs (axis and legends)
        XAxis xAxis = barChart_Brand.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setEnabled(true);
        YAxis leftAxis = barChart_Brand.getAxisLeft();
        YAxis rightAxis = barChart_Brand.getAxisRight();
        leftAxis.setEnabled(true);
        rightAxis.setEnabled(false);

        BarDataSet set1;
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        valueSet1.add(new BarEntry((float)customerDetailsarray.get(0).getPreferredBrandSales(), 0));
        valueSet1.add(new BarEntry((float)customerDetailsarray.get(0).getPreferredBrand2Sales(), 1));
        valueSet1.add(new BarEntry((float)customerDetailsarray.get(0).getPreferredBrand3Sales(), 2));
        ArrayList<IBarDataSet> dataSet ;
        set1 = new BarDataSet(valueSet1, "Sales");
        dataSet = new ArrayList<>();
        dataSet.add(set1);

        BarData data = new BarData(dataSet);
        barChart_Brand.animateXY(2000, 2000);
        barChart_Brand.setData(data);
        Legend l = barChart_Brand.getLegend();
        // modify the legend ... by default it is on the left
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setForm(Legend.LegendForm.SQUARE);
    }

    private void callMonthBarchart()
    {
        barChart_Preference1.setDescription(null);
        barChart_Preference1.setDrawGridBackground(false); //Do not display grid background

        //Disable all interaction with the chart
        barChart_Preference1.setHighlightPerTapEnabled(false);
        barChart_Preference1.setHighlightPerDragEnabled(false);
        barChart_Preference1.setDoubleTapToZoomEnabled(false);
        handler = barChart_Preference1.getViewPortHandler();

        //Disable surroundings graphs (axis and legends)
        XAxis xAxis = barChart_Preference1.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setEnabled(true);
        YAxis leftAxis = barChart_Preference1.getAxisLeft();
        YAxis rightAxis = barChart_Preference1.getAxisRight();
        leftAxis.setEnabled(true);
        rightAxis.setEnabled(false);

        BarDataSet set1;
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        valueSet1.add(new BarEntry((float)customerDetailsarray.get(0).getTxnCntMonthStart(), 0));
        valueSet1.add(new BarEntry((float)customerDetailsarray.get(0).getTxnCntMonthEnd(), 1));
        ArrayList<IBarDataSet> dataSet ;
        set1 = new BarDataSet(valueSet1, "");
        dataSet = new ArrayList<>();
        dataSet.add(set1);

        BarData data = new BarData(dataSet);
        barChart_Preference1.animateXY(2000, 2000);
        barChart_Preference1.setData(data);
        Legend l = barChart_Preference1.getLegend();
        // modify the legend ... by default it is on the left
       l.setEnabled(false);
    }

    private void callWeekBarchart()
    {
        barChart_Preference2.setDescription(null);
        barChart_Preference2.setDrawGridBackground(false); //Do not display grid background

        //Disable all interaction with the chart
        barChart_Preference2.setHighlightPerTapEnabled(false);
        barChart_Preference2.setHighlightPerDragEnabled(false);
        barChart_Preference2.setDoubleTapToZoomEnabled(false);
        handler = barChart_Preference2.getViewPortHandler();

        //Disable surroundings graphs (axis and legends)
        XAxis xAxis = barChart_Preference2.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setEnabled(true);
        YAxis leftAxis = barChart_Preference2.getAxisLeft();
        YAxis rightAxis = barChart_Preference2.getAxisRight();
        leftAxis.setEnabled(true);
        rightAxis.setEnabled(false);

        BarDataSet set1;
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        valueSet1.add(new BarEntry((float)customerDetailsarray.get(0).getTxnCntWeekend(), 0));
        valueSet1.add(new BarEntry((float)customerDetailsarray.get(0).getTxnCntWeekday(), 1));
        valueSet1.add(new BarEntry((float)customerDetailsarray.get(0).getTxnCntWed(), 2));
        ArrayList<IBarDataSet> dataSet ;
        set1 = new BarDataSet(valueSet1, "Visits");
        dataSet = new ArrayList<>();
        dataSet.add(set1);

        BarData data = new BarData(dataSet);
        barChart_Preference2.animateXY(2000, 2000);
        barChart_Preference2.setData(data);
        Legend l = barChart_Preference2.getLegend();
        // modify the legend ... by default it is on the left
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setForm(Legend.LegendForm.SQUARE);
    }

}
