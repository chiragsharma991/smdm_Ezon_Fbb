package apsupportapp.aperotechnologies.com.designapp.VisualAssortmentSwipe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.FreshnessIndex.FreshnessIndexActivity;
import apsupportapp.aperotechnologies.com.designapp.FreshnessIndex.FreshnessIndexDetails;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.model.VisualAssort;
import apsupportapp.aperotechnologies.com.designapp.model.VisualReport;

/**
 * Created by pamrutkar on 10/01/17.
 */
public class VisualReportActivity extends AppCompatActivity implements View.OnClickListener {


    RelativeLayout visualreport_imageBtnBack;
    PieChart pieChart;
    RequestQueue queue;
    Context context;
    String userId, bearertoken, TAG = "VisualReport";
    SharedPreferences sharedPreferences;
    Gson gson;
    int offset  = 0,limit = 10,count = 0;
    VisualReport visualReport;
    ProgressBar visual_report_progressBar;
    ArrayList<VisualReport> visualReportArrayList;
    float pendingOptions = 0.0f, likedOptions = 0.0f, dislikedOptions = 0.0f, totalOptions = 0.0f;
    TextView vr_likeVal,vr_dislikeVal,vr_pendingVal;
    PieDataSet dataSet;
    PieData pieData;
    String recache;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_report);
        getSupportActionBar().hide();
        context = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();
        visualreport_imageBtnBack = (RelativeLayout)findViewById(R.id.visualreport_imageBtnBack);
        vr_likeVal = (TextView)findViewById(R.id.vr_likesVal);
        vr_dislikeVal = (TextView)findViewById(R.id.vr_dislikesVal);
        vr_pendingVal = (TextView)findViewById(R.id.vr_PendingVal);
        pieChart = (PieChart)findViewById(R.id.vreport_pieChart);
        visual_report_progressBar = (ProgressBar)findViewById(R.id.visual_report_progressBar);
        visualreport_imageBtnBack.setOnClickListener(this);
        visualReportArrayList = new ArrayList<VisualReport>();
        if (Reusable_Functions.chkStatus(context)) {

            Reusable_Functions.hDialog();
            visual_report_progressBar.setVisibility(View.VISIBLE);
            offset = 0;
            limit = 10;
            count = 0;
            recache = "true";
            requestVisualReportAPI();

        } else
        {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
        }


    }

    private void requestVisualReportAPI() {

      String  url = ConstsCore.web_url + "/v1/display/visualassortmentoptiondetails/" + userId  + "?recache="+ recache +"&offset=" + offset + "&limit=" + limit ;
        Log.e(TAG,"requestVisualReportAPI Url  "+ url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG,"VReport Pie Chart on Scroll  : "+ response);

                        Log.e(" VReport Pie Chart response", "" + response.length());
                        try {

                            int i;

                            if (response.equals(null) || response == null || response.length() == 0 && count == 0 ) {
                                Reusable_Functions.hDialog();
                                visual_report_progressBar.setVisibility(View.GONE);
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();



                            } else if (response.length() == limit) {
                                for (i = 0; i < response.length(); i++) {

                                    visualReport = gson.fromJson(response.get(i).toString(), VisualReport.class);
                                    visualReportArrayList.add(visualReport);
                                }
                                offset= (limit * count) + limit;
                                count++;
                                requestVisualReportAPI();


                            } else if (response.length() < limit) {
                                for (i = 0; i < response.length(); i++) {

                                    visualReport = gson.fromJson(response.get(i).toString(), VisualReport.class);
                                    visualReportArrayList.add(visualReport);
                                }

                                ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
                                for (VisualReport v_report : visualReportArrayList) {

                                        totalOptions = (float) v_report.getTotalOptions();
                                        likedOptions = (float) v_report.getLikedOptions();
                                        dislikedOptions = (float) v_report.getDislikedOptions();
                                        pendingOptions =  (totalOptions -likedOptions - dislikedOptions);
                                        Log.e(TAG,"Values-------"+ totalOptions + "\t" + likedOptions + "\t" + dislikedOptions + "\t" + pendingOptions);


                                }
                                NumberFormat format = NumberFormat.getNumberInstance(new Locale("","in"));
                                vr_likeVal.setText(format.format(Math.round(likedOptions)));
                                vr_dislikeVal.setText(format.format(Math.round(dislikedOptions)));
                                vr_pendingVal.setText(format.format(Math.round(pendingOptions)));
                                ArrayList<Integer> colors = new ArrayList<>();
                                //colors.add(Color.parseColor("#ffc65b"));
                                colors.add(Color.parseColor("#31d6cf"));
                                colors.add(Color.parseColor("#fe8081"));
                                colors.add(Color.parseColor("#aea9fd"));
//                                ArrayList<String> labels = new ArrayList<>();
//                                labels.add(0,"Total");
//                                labels.add(1,"Likes");
//                                labels.add(2,"Dislike");
//                                labels.add(3,"Pending");
                                if (likedOptions > 0.0f) {

                                    entries.add(new PieEntry(likedOptions,"Likes"));
                                    Log.e(TAG, "likes: ");

                                }
                                if (dislikedOptions > 0.0f) {

                                    entries.add(new PieEntry(dislikedOptions,"Dislike"));
                                    Log.e(TAG, "dislike: ");

                                }
//                                if (totalOptions > 0.0f) {
//
//                                    //   entries.add(new PieEntry(totalOptions, "Total"));
//                                    Log.e(TAG, "total: ");
//
//                                }
                                if (pendingOptions > 0.0f) {

                                    entries.add(new PieEntry(pendingOptions,"Pending"));
                                    Log.e(TAG, "pending: ");
                                }
                                dataSet = new PieDataSet(entries,"");
                                dataSet.setColors(colors);
                                dataSet.setValueTextColor(Color.BLACK);
                                pieData = new PieData(dataSet);
                               // pieData.setValueFormatter(new MyValueFormatter());
                                pieChart.setDrawMarkers(false);
                                pieData.setValueTextSize(12f);
                                dataSet.setXValuePosition(null);
                                dataSet.setValueLineWidth(0.6f);
                                dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                                pieChart.setEntryLabelColor(Color.WHITE);
                                pieChart.setHoleRadius(65);
                                pieChart.setHoleColor(Color.parseColor("#ffc65b"));
                                pieChart.setCenterText(String.valueOf(format.format(Math.round(totalOptions)))+ "\nTotal");
                                pieChart.setCenterTextSize(35f);
                                pieChart.setExtraTopOffset(10f);
                                pieChart.setCenterTextColor(Color.WHITE);
                                pieChart.setTransparentCircleRadius(0);
                                pieChart.setData(pieData);
                                pieChart.setNoDataText("");
                                pieChart.setDescription(null);
                                pieChart.invalidate();
                                pieChart.animateXY(1000,1000);
                                pieChart.setTouchEnabled(false);
                                Legend l = pieChart.getLegend();
                                // l.setExtra(colors,labels);
                                l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
                                l.setFormSize(15f);
                                l.setEnabled(true);
                                Reusable_Functions.hDialog();
                                visual_report_progressBar.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            visual_report_progressBar.setVisibility(View.GONE);
                            Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
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

    public class MyValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,###,##0.0"); // use two decimal if needed
        }


        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//           return mFormat.format(value) + ""; // e.g. append a dollar-sign

            if (value < 0.0) return "";
            else return mFormat.format(value) + " %";
        }
    }


    @Override
    public void onClick(View v) {
      switch (v.getId())
      {
          case R.id.visualreport_imageBtnBack :
              onBackPressed();
              break;
          default:
              break;
      }

    }



    @Override
    public void onBackPressed() {
//        Intent intent = new Intent (VisualReportActivity.this,DashBoardActivity.class);
//        startActivity(intent);
        finish();
    }
}
