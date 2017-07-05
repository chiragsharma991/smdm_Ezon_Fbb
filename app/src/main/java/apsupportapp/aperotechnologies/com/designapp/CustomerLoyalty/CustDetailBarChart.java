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
import android.text.Html;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;


import apsupportapp.aperotechnologies.com.designapp.FreshnessIndex.FreshnessIndexActivity;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;

import static apsupportapp.aperotechnologies.com.designapp.CustomerLoyalty.CustomerDetailActivity.customerDetailsarray;

/**
 * Created by pamrutkar on 23/06/17.
 */
public class CustDetailBarChart extends AppCompatActivity
{
    private LinearLayout phn_call, mail_call;
    ViewPortHandler handler;
    private TextView txt_cdb_name, txt_cdb_mobile, txt_cdb_email;
    private RelativeLayout rel_cdb_back;
    private HorizontalBarChart barChart_Category, barChart_Brand, barChart_Preference1, barChart_Preference2;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        rel_cdb_back = (RelativeLayout) findViewById(R.id.rel_back);
        txt_cdb_name = (TextView) findViewById(R.id.txt_cdb_name);
        txt_cdb_mobile = (TextView) findViewById(R.id.txt_cdb_mobileNo);
        txt_cdb_email = (TextView) findViewById(R.id.txt_cdb_email);
        phn_call = (LinearLayout) findViewById(R.id.lin_one);
        mail_call = (LinearLayout) findViewById(R.id.lin_two);
        barChart_Category = (HorizontalBarChart) findViewById(R.id.hbarchart_category);
        barChart_Brand = (HorizontalBarChart) findViewById(R.id.hbarchart_brands);
        barChart_Preference1 = (HorizontalBarChart) findViewById(R.id.hbarchart_month);
        barChart_Preference2 = (HorizontalBarChart) findViewById(R.id.hbarchart_week);
        txt_cdb_name.setText(customerDetailsarray.get(0).getFullName());
        txt_cdb_mobile.setText(customerDetailsarray.get(0).getMobileNumber());
        txt_cdb_email.setText(customerDetailsarray.get(0).getEmailAddress());

        phn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (txt_cdb_mobile.getTextSize() != 0) {

                    makePhoneCall(v);
                }

            }
        });
        mail_call.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (txt_cdb_email.getTextSize() == 0 || txt_cdb_email.getText().equals("N/A")) {
                      return ;

                }
                else
                {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto:" + txt_cdb_email.getText().toString()));
                    startActivity(Intent.createChooser(emailIntent, "Send feedback"));
                }

            }
        });
    }

    private void makePhoneCall(View v)
    {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callPhone();
                }
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }

    // Horizontal Bar chart for Category
    private void callCategoryBarChart() {
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
        xAxis.setValueFormatter(new MyValueFormatter());
        xAxis.setTextSize(10.5f);
        xAxis.setDrawGridLines(false);
        xAxis.setEnabled(true);
        YAxis leftAxis = barChart_Category.getAxisLeft();
        YAxis rightAxis = barChart_Category.getAxisRight();
        leftAxis.setLabelCount(2);
        leftAxis.setTextSize(10.5f);
        leftAxis.setEnabled(true);
        leftAxis.setStartAtZero(true);
        leftAxis.setDrawGridLines(false);
        rightAxis.setEnabled(false);
        BarDataSet set1;
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        valueSet1.add(new BarEntry(0, (float) customerDetailsarray.get(0).getPreferredCcbSales(), customerDetailsarray.get(0).getPreferredCcb()));
        valueSet1.add(new BarEntry(1, (float) customerDetailsarray.get(0).getPreferredCcb2Sales(), customerDetailsarray.get(0).getPreferredCcb2()));
        valueSet1.add(new BarEntry(2, (float) customerDetailsarray.get(0).getPreferredCcb3Sales(), customerDetailsarray.get(0).getPreferredCcb3()));
        ArrayList<IBarDataSet> dataSet;
        set1 = new BarDataSet(valueSet1, "Sales");
        set1.setColor(Color.parseColor("#20b5d3"));
        dataSet = new ArrayList<>();
        dataSet.add(set1);
        BarData data = new BarData(dataSet);
        data.setValueFormatter(new MyCategoryFormatter());
        data.setValueTextSize(10.5f);
        barChart_Category.animateXY(2000, 2000);
        barChart_Category.setData(data);
        barChart_Category.notifyDataSetChanged();
        Legend l = barChart_Category.getLegend();
        // modify the legend ... by default it is on the left
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setForm(Legend.LegendForm.SQUARE);
    }


    // Horizontal Bar chart for brand
    private void callBrandBarChart() {
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
        xAxis.setValueFormatter(new MyAxisFormatter());
        xAxis.isAvoidFirstLastClippingEnabled();
        xAxis.setTextSize(10.5f);
        xAxis.setDrawGridLines(false);
        xAxis.setEnabled(true);
        YAxis leftAxis = barChart_Brand.getAxisLeft();
        YAxis rightAxis = barChart_Brand.getAxisRight();
        leftAxis.setTextSize(10.5f);
        leftAxis.setLabelCount(2);
        leftAxis.setEnabled(true);
        leftAxis.setStartAtZero(true);
        leftAxis.setDrawGridLines(false);
        rightAxis.setEnabled(false);
        BarDataSet set1;
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        valueSet1.add(new BarEntry(0, (float) customerDetailsarray.get(0).getPreferredBrandSales(), customerDetailsarray.get(0).getPreferredBrand()));
        valueSet1.add(new BarEntry(1, (float) customerDetailsarray.get(0).getPreferredBrand2Sales(), customerDetailsarray.get(0).getPreferredBrand2()));
        valueSet1.add(new BarEntry(2, (float) customerDetailsarray.get(0).getPreferredBrand3Sales(), customerDetailsarray.get(0).getPreferredBrand3()));
        ArrayList<IBarDataSet> dataSet;
        set1 = new BarDataSet(valueSet1, "Sales");
        set1.setColor(Color.parseColor("#21d24c"));
        dataSet = new ArrayList<>();
        dataSet.add(set1);
        BarData data = new BarData(dataSet);
        data.setValueFormatter(new MyBrandFormatter());
        data.setValueTextSize(10.5f);
        barChart_Brand.animateXY(2000, 2000);
        barChart_Brand.setData(data);
        barChart_Brand.notifyDataSetChanged();
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
        xAxis.setValueFormatter(new MyMonthFormatter());
        xAxis.setTextSize(10.5f);
        xAxis.setDrawGridLines(false);
        xAxis.setEnabled(true);
        YAxis leftAxis = barChart_Preference1.getAxisLeft();
        YAxis rightAxis = barChart_Preference1.getAxisRight();
        leftAxis.setTextSize(10.5f);
        leftAxis.setLabelCount(2);
        leftAxis.setEnabled(true);
        leftAxis.setStartAtZero(true);
        leftAxis.setDrawGridLines(false);
        rightAxis.setEnabled(false);
        BarDataSet set1;
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        valueSet1.add(new BarEntry(0,(float) customerDetailsarray.get(0).getTxnCntMonthEnd(), "Month End"));
        valueSet1.add(new BarEntry(1,(float) customerDetailsarray.get(0).getTxnCntMonthStart(),"Month Start"));
        ArrayList<IBarDataSet> dataSet;
        set1 = new BarDataSet(valueSet1, "Visits");
        set1.setColor(Color.parseColor("#f5204c"));
        dataSet = new ArrayList<>();
        dataSet.add(set1);
        BarData data = new BarData(dataSet);
        data.setValueFormatter(new MyMonthValFormatter());
        data.setValueTextSize(10.5f);
        barChart_Preference1.animateXY(2000, 2000);
        barChart_Preference1.setData(data);
        barChart_Preference1.notifyDataSetChanged();
        Legend l = barChart_Preference1.getLegend();
        // modify the legend ... by default it is on the left
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setForm(Legend.LegendForm.SQUARE);
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
        xAxis.setValueFormatter(new MyWeekFormatter());
        xAxis.setTextSize(10.5f);
        xAxis.setEnabled(true);
        xAxis.setDrawGridLines(false);
        YAxis leftAxis = barChart_Preference2.getAxisLeft();
        YAxis rightAxis = barChart_Preference2.getAxisRight();
        leftAxis.setTextSize(10.5f);
        leftAxis.setLabelCount(2);
        leftAxis.setStartAtZero(true);
        leftAxis.setEnabled(true);
        leftAxis.setDrawGridLines(false);
        rightAxis.setEnabled(false);
        BarDataSet set1;
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        valueSet1.add(new BarEntry(2, (float) customerDetailsarray.get(0).getTxnCntWeekend(), "Weekend"));
        valueSet1.add(new BarEntry(1, (float) customerDetailsarray.get(0).getTxnCntWeekday(), "Weekday"));
        valueSet1.add(new BarEntry(0, (float) customerDetailsarray.get(0).getTxnCntWed(), "Wednesday"));
        ArrayList<IBarDataSet> dataSet;
        set1 = new BarDataSet(valueSet1, "Visits");
        set1.setColor(Color.parseColor("#f89a20"));
        dataSet = new ArrayList<>();
        dataSet.add(set1);
        BarData data = new BarData(dataSet);
        data.setValueTextSize(10.5f);
        data.setValueFormatter(new MyWeekValFormatter());
        barChart_Preference2.animateXY(2000, 2000);
        barChart_Preference2.setData(data);
        barChart_Preference2.notifyDataSetChanged();
        Legend l = barChart_Preference2.getLegend();
        // modify the legend ... by default it is on the left
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setForm(Legend.LegendForm.SQUARE);
    }



    public class MyValueFormatter implements IAxisValueFormatter
    {
        @Override
        public String getFormattedValue(float value, AxisBase axis)
        {
            if (value == 0.0f)
            {
                return customerDetailsarray.get(0).getPreferredCcb();
            }
            else if (value == 1.0f)
            {
                return customerDetailsarray.get(0).getPreferredCcb2();
            }
            else if (value == 2.0f)
            {
                return customerDetailsarray.get(0).getPreferredCcb3();
            }
            else
            {
                return " ";
            }
        }
    }

    private class MyAxisFormatter implements IAxisValueFormatter
    {
        @Override
        public String getFormattedValue(float value, AxisBase axis)
        {
            if (value == 0.0f)
            {
                return customerDetailsarray.get(0).getPreferredBrand();
            }
            else if (value == 1.0f)
            {
                return customerDetailsarray.get(0).getPreferredBrand2();
            }
            else if (value == 2.0f)
            {
                return customerDetailsarray.get(0).getPreferredBrand3();
            }
            else
            {
                return " ";
            }
        }
    }

    private class MyMonthFormatter implements IAxisValueFormatter
    {
        @Override
        public String getFormattedValue(float value, AxisBase axis)
        {
            Log.e("value",""+value);
           // return String.valueOf(value);
            if (value == 0.0f)
            {
                return "Month End";
            }
            else if (value == 0.90000004f)
            {
                return "Month Start";
            }
            else
            {
                return " ";
            }
        }
    }


    private class MyWeekFormatter implements IAxisValueFormatter
    {
        @Override
        public String getFormattedValue(float value, AxisBase axis)
        {
            Log.e("value",""+value);
           //  return String.valueOf(value);
            if (value == 0.0f)
            {
                return "Wednesday";
            }
            else if (value == 1.0f)
            {
                return "Weekday";
            }
            else if (value == 2.0f)
            {
                return "Weekend";
            }
            else
            {
                return " ";
            }
        }
    }

    private class MyCategoryFormatter implements IValueFormatter
    {
        private DecimalFormat mFormat;
        public MyCategoryFormatter()
        {
            mFormat = new DecimalFormat("###,###,###,##0.0"); // use two decimal if needed
        }

        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler)
        {
            return "₹ "+ Math.round(value);
        }
    }

    private class MyBrandFormatter implements IValueFormatter
    {
        private DecimalFormat mFormat;
        public MyBrandFormatter()
        {
            mFormat = new DecimalFormat("###,###,###,##0.0"); // use two decimal if needed
        }

        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler)
        {
            return "₹ "+ Math.round(value);
        }
    }

    private class MyMonthValFormatter implements IValueFormatter
    {
        private DecimalFormat mFormat;
        public MyMonthValFormatter()
        {
            mFormat = new DecimalFormat("###,###,###,##0.0"); // use two decimal if needed
        }

        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler)
        {
            return String.valueOf(Math.round(value));
        }
    }

    private class MyWeekValFormatter implements IValueFormatter
    {
        private DecimalFormat mFormat;
        public MyWeekValFormatter()
        {
            mFormat = new DecimalFormat("###,###,###,##0.0"); // use two decimal if needed
        }

        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler)
        {
            return String.valueOf(Math.round(value));
        }
    }
}
