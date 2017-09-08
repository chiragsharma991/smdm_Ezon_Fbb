package apsupportapp.aperotechnologies.com.designapp.VisualAssortmentSwipe;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
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
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.model.VisualReport;

/**
 * Created by pamrutkar on 10/01/17.
 */
public class VisualReportActivity extends AppCompatActivity implements View.OnClickListener {


    RelativeLayout visualreport_imageBtnBack;
    PieChart pieChart;
    RequestQueue queue;
    Context context;
    String userId, bearertoken, TAG = "VisualReport",storeDescription,geoLevel2Code;
    SharedPreferences sharedPreferences;
    Gson gson;
    int offset  = 0,limit = 10,count = 0;
    VisualReport visualReport;
    ProgressBar visual_report_progressBar;
    ArrayList<VisualReport> visualReportArrayList;
    float pendingOptions = 0.0f, likedOptions = 0.0f, dislikedOptions = 0.0f, totalOptions = 0.0f;
    TextView vr_likeVal,vr_dislikeVal,vr_pendingVal,txt_like_color,txt_dislike_color,txt_pending_color,txtStoreCode,txtStoreName;
    PieDataSet dataSet;
    PieData pieData;
    String recache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_report);
        getSupportActionBar().hide();
        context = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        storeDescription = sharedPreferences.getString("storeDescription","");
        geoLevel2Code = sharedPreferences.getString("geoLevel2Code","");

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();
        txtStoreCode = (TextView)findViewById(R.id.txtStoreCode);
        txtStoreName = (TextView)findViewById(R.id.txtStoreName);
        txtStoreCode.setText(storeDescription.trim().substring(0,4));
        txtStoreName.setText(storeDescription.substring(5));
        visualreport_imageBtnBack = (RelativeLayout)findViewById(R.id.visualreport_imageBtnBack);
        vr_likeVal = (TextView)findViewById(R.id.vr_likesVal);
        vr_dislikeVal = (TextView)findViewById(R.id.vr_dislikesVal);
        vr_pendingVal = (TextView)findViewById(R.id.vr_PendingVal);
        txt_like_color = (TextView)findViewById(R.id.txt_like_color);
        txt_dislike_color = (TextView)findViewById(R.id.txt_dislike_color);
        txt_pending_color = (TextView)findViewById(R.id.txt_pending_color);
        pieChart = (PieChart)findViewById(R.id.vreport_pieChart);
        visual_report_progressBar = (ProgressBar)findViewById(R.id.visual_report_progressBar);
        visualreport_imageBtnBack.setOnClickListener(this);
        visualReportArrayList = new ArrayList<VisualReport>();
        if (Reusable_Functions.chkStatus(context)) {

            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context,"Loading...");
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

      String  url = ConstsCore.web_url + "/v1/display/visualassortmentoptiondetails/" + userId  + "?recache="+ recache +"&geoLevel2Code="+geoLevel2Code;//+"&offset=" + offset + "&limit=" + limit ;
        Log.e(TAG, "requestVisualReportAPI: "+url );
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, "onResponse: "+response);
                        try {

                            int i;

                            if (response.equals("") || response == null || response.length() == 0 && count == 0 ) {
                                Reusable_Functions.hDialog();
                               // visual_report_progressBar.setVisibility(View.GONE);
                                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();

//                            } else if (response.length() == limit) {
//                                for (i = 0; i < response.length(); i++) {
//
//                                    visualReport = gson.fromJson(response.get(i).toString(), VisualReport.class);
//                                    visualReportArrayList.add(visualReport);
//                                }
//                                offset= (limit * count) + limit;
//                                count++;
//                                requestVisualReportAPI();


                            }
                                for (i = 0; i < response.length(); i++) {

                                    visualReport = gson.fromJson(response.get(i).toString(), VisualReport.class);
                                    visualReportArrayList.add(visualReport);
                                }

                                ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
                                for (VisualReport v_report : visualReportArrayList)
                                {
                                        totalOptions = (float) v_report.getTotalOptions();
                                        likedOptions = (float) v_report.getLikedOptions();
                                        dislikedOptions = (float) v_report.getDislikedOptions();
                                        pendingOptions =  (totalOptions -likedOptions - dislikedOptions);
                                }
                                NumberFormat format = NumberFormat.getNumberInstance(new Locale("","in"));

                                ArrayList<Integer> colors = new ArrayList<>();
                                colors.add(Color.parseColor("#20b5d3"));
                                colors.add(Color.parseColor("#21d24c"));
                                colors.add(Color.parseColor("#f5204c"));

                                if (likedOptions > 0.0f) {

                                    entries.add(new PieEntry(likedOptions,"Likes"));

                                }
                                if (dislikedOptions > 0.0f) {

                                    entries.add(new PieEntry(dislikedOptions,"Dislike"));

                                }
                                if (pendingOptions > 0.0f) {

                                    entries.add(new PieEntry(pendingOptions,"Pending"));
                                }
                                dataSet = new PieDataSet(entries,"");
                                dataSet.setColors(colors);
                                dataSet.setValueTextColor(Color.BLACK);
                                pieData = new PieData(dataSet);
                                pieChart.setDrawMarkers(false);
                                pieData.setValueTextSize(12f);
                                dataSet.setXValuePosition(null);
                                dataSet.setYValuePosition(null);
                                pieChart.setEntryLabelColor(Color.WHITE);
                                pieChart.setHoleRadius(65);
                                pieChart.setHoleColor(Color.parseColor("#ffffff"));
                                pieChart.setOnFocusChangeListener(new View.OnFocusChangeListener()
                                {
                                    @Override
                                    public void onFocusChange(View v, boolean hasFocus) {
                                        v = pieChart.focusSearch(v.focusSearch(View.FOCUS_LEFT),View.FOCUS_LEFT);
                                    }
                                });
                                pieChart.setCenterText(String.valueOf(format.format(Math.round(totalOptions)))+ "\nTotal");
                                pieChart.setCenterTextSize(35f);
                                pieChart.setExtraTopOffset(10f);
                                pieChart.setCenterTextColor(Color.BLACK);
                                pieChart.setTransparentCircleRadius(0);
                                pieChart.setData(pieData);
                                pieChart.setNoDataText("");
                                pieChart.setDescription(null);
                                pieChart.invalidate();
                                pieChart.setRotationAngle(270);
                                pieChart.animateXY(1000,1000);
                                pieChart.setTouchEnabled(true);
                                Legend l = pieChart.getLegend();
                                txt_like_color.setBackgroundColor(Color.parseColor("#20b5d3"));
                                txt_dislike_color.setBackgroundColor(Color.parseColor("#21d24c"));
                                txt_pending_color.setBackgroundColor(Color.parseColor("#f5204c"));
                                vr_likeVal.setText(format.format(Math.round(likedOptions)));
                                vr_dislikeVal.setText(format.format(Math.round(dislikedOptions)));
                                vr_pendingVal.setText(format.format(Math.round(pendingOptions)));
                             //   l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
                                l.setFormSize(15f);
                                l.setEnabled(false);
                                Reusable_Functions.hDialog();
                               // visual_report_progressBar.setVisibility(View.GONE);

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
        finish();
    }


}
