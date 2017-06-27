package apsupportapp.aperotechnologies.com.designapp.CustomerLoyalty;

import android.content.Intent;
import android.net.Uri;
import android.graphics.Color;
import android.os.Bundle;
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

/**
 * Created by pamrutkar on 23/06/17.
 */
public class CustDetailBarChart extends AppCompatActivity
{
    private LinearLayout phn_call,mail_call;

    private TextView txt_cdb_name,txt_cdb_mobile,txt_cdb_email;
    CustomerDetailActivity detailActivity;
    private RelativeLayout rel_cdb_back;
    private HorizontalBarChart barChart_Category,barChart_Brand,barChart_Preference1,barChart_Preference2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cd_barchart);
        getSupportActionBar().hide();
        initialise_UI();
        callCategoryBarChart();
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
        txt_cdb_name.setText(CustomerDetailActivity.customerDetailsarray.get(0).getFullName());
        txt_cdb_mobile.setText(CustomerDetailActivity.customerDetailsarray.get(0).getMobileNumber());
        txt_cdb_email.setText(CustomerDetailActivity.customerDetailsarray.get(0).getEmailAddress());

        phn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt_cdb_mobile.getTextSize()!=0){

                    Intent callIntent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+txt_cdb_mobile.getText().toString()));
                    startActivity(callIntent);
                }

            }
        });
        mail_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt_cdb_email.getTextSize()!=0){

                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto:"+txt_cdb_email.getText().toString()));
                    startActivity(Intent.createChooser(emailIntent, "Send feedback"));
                }

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void callCategoryBarChart()
    {
        // enable touch gestures
        ViewPortHandler handler = barChart_Category.getViewPortHandler();
        barChart_Category.setDrawBarShadow(false);

        barChart_Category.setDrawValueAboveBar(true);

        barChart_Category.getDescription().setEnabled(false);
        XAxis xl = barChart_Category.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGranularity(10f);

        YAxis yl = barChart_Category.getAxisLeft();
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yl.setInverted(true);

//        YAxis yr = barChart_Category.getAxisRight();
//        yr.setDrawAxisLine(true);
//        yr.setDrawGridLines(false);
        barChart_Category.setTouchEnabled(true);
        // enable scaling and dragging
        barChart_Category.setDragEnabled(true);
        barChart_Category.setScaleEnabled(true);
        barChart_Category.setDrawGridBackground(false);
        barChart_Category.setGridBackgroundColor(android.R.color.black);
        barChart_Category.setBackgroundColor(Color.WHITE);
        barChart_Category.setDescription(null);
        BarDataSet set1;
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry((float) detailActivity.customerDetailsarray.get(0).getPreferredCcbSales(), 0); // Jan
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry((float) detailActivity.customerDetailsarray.get(0).getPreferredCcb2Sales(), 1); // Feb
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry((float) detailActivity.customerDetailsarray.get(0).getPreferredCcb3Sales(), 2); // Mar
        valueSet1.add(v1e3);
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



}
