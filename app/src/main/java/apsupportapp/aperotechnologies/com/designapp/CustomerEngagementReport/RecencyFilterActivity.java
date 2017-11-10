package apsupportapp.aperotechnologies.com.designapp.CustomerEngagementReport;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.CustomerEngagement.CustomerDetailActivity;
import apsupportapp.aperotechnologies.com.designapp.R;

public class RecencyFilterActivity extends AppCompatActivity {

    private Context context;
    private Button btn_apply;
    private RelativeLayout rel_visits;
    String report_days, report_visits;
    //String[] recency_days = " ";
    ArrayList<String> values;
    private TextView txt_day1, txt_day2, txt_day3, txt_day4, txt_day5, txt_freq_day1, txt_freq_day2, txt_freq_day3, txt_visit1, txt_visit2, txt_visit3, txt_visit4, txt_visit5, txt_visits;
    Boolean day1 = false, day2 = false, day3 = false, day4 = false, day5 = false, reportday1 = false, reportday2 = false, reportday3 = false, visit1 = false, visit2 = false, visit3 = false, visit4 = false, visit5 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recency_filter);
        getSupportActionBar().hide();
//        getSupportActionBar().setElevation(0);
        context = this;
        values = new ArrayList<>();
        initialiseUI();
    }

    private void initialiseUI() {

        txt_day1 = (TextView) findViewById(R.id.txt_day1);
        txt_day2 = (TextView) findViewById(R.id.txt_day2);
        txt_day3 = (TextView) findViewById(R.id.txt_day3);
        txt_day4 = (TextView) findViewById(R.id.txt_day4);
        txt_day5 = (TextView) findViewById(R.id.txt_day5);
        txt_freq_day1 = (TextView) findViewById(R.id.txt_freq_day1);
        txt_freq_day2 = (TextView) findViewById(R.id.txt_freq_day2);
        txt_freq_day3 = (TextView) findViewById(R.id.txt_freq_day3);
        txt_visit1 = (TextView) findViewById(R.id.txt_visit1);
        txt_visit2 = (TextView) findViewById(R.id.txt_visit2);
        txt_visit3 = (TextView) findViewById(R.id.txt_visit3);
        txt_visit4 = (TextView) findViewById(R.id.txt_visit4);
        txt_visit5 = (TextView) findViewById(R.id.txt_visit5);
        txt_visits = (TextView) findViewById(R.id.txt_visits);
        btn_apply = (Button) findViewById(R.id.btn_apply);
        rel_visits = (RelativeLayout) findViewById(R.id.rel_visits);

        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Uri uri = Uri.parse("market://details?id=" + getPackageName());
//                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
//                try {
//                    startActivity(myAppLinkToMarket);
//                } catch (ActivityNotFoundException e) {
//                    Toast.makeText(context, " unable to find market app", Toast.LENGTH_LONG).show();
//                }
                Toast.makeText(context, "report days "+report_days, Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "recency days "+values.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        txt_day1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(day1 == false) {
                    txt_day1.setBackground(getResources().getDrawable(R.drawable.edt_round_filter));
                    txt_day1.setTextColor(getResources().getColor(R.color.white));
                    String strday1 = txt_day1.getText().toString();
                    values.add(strday1);
                    day1 = true;
                }
                else
                {
                    txt_day1.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_day1.setTextColor(R.color.ezfb_black);
                    values.remove("0 - 7");
                    day1 = false;

                }
            }
        });

        txt_day2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(day2 == false) {
                    txt_day2.setBackground(getResources().getDrawable(R.drawable.edt_round_filter));
                    txt_day2.setTextColor(getResources().getColor(R.color.white));
                    String strday2 = txt_day2.getText().toString();
                    values.add(strday2);
                    day2 = true;
                }
                else
                {
                    txt_day2.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_day2.setTextColor(R.color.ezfb_black);
                    values.remove("8 - 14");
                    day2 = false;

                }
            }
        });

        txt_day3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(day3 == false) {
                    txt_day3.setBackground(getResources().getDrawable(R.drawable.edt_round_filter));
                    txt_day3.setTextColor(getResources().getColor(R.color.white));
                    String strday3 = txt_day3.getText().toString();
                    values.add(strday3);
                    day3 = true;
                }
                else
                {
                    txt_day3.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_day3.setTextColor(R.color.ezfb_black);
                    values.remove("15 - 30");
                    day3 = false;

                }
            }
        });

        txt_day4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(day4 == false) {
                    txt_day4.setBackground(getResources().getDrawable(R.drawable.edt_round_filter));
                    txt_day4.setTextColor(getResources().getColor(R.color.white));
                    String strday4 = txt_day4.getText().toString();
                    values.add(strday4);
                    day4 = true;
                }
                else
                {
                    txt_day4.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_day4.setTextColor(R.color.ezfb_black);
                    values.remove("31 - 60");
                    day4 = false;

                }
            }
        });

        txt_day5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(day5 == false) {
                    txt_day5.setBackground(getResources().getDrawable(R.drawable.edt_round_filter));
                    txt_day5.setTextColor(getResources().getColor(R.color.white));
                    String strday5 = txt_day5.getText().toString();
                    values.add(strday5);
                    day5 = true;
                }
                else
                {
                    txt_day5.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_day5.setTextColor(R.color.ezfb_black);
                    values.remove("61 - 90");
                    day5 = false;

                }
            }
        });

        txt_freq_day1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reportday1 == false) {
                    txt_freq_day1.setBackground(getResources().getDrawable(R.drawable.edt_round_filter));
                    txt_freq_day1.setTextColor(getResources().getColor(R.color.white));
                    txt_freq_day2.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_freq_day2.setTextColor(R.color.ezfb_black);
                    txt_freq_day3.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_freq_day3.setTextColor(R.color.ezfb_black);
                    rel_visits.setVisibility(View.VISIBLE);
                    txt_visits.setVisibility(View.VISIBLE);
                    report_days = txt_freq_day1.getText().toString();
                    reportday1 = true;
                    reportday2 = false;
                    reportday3 = false;

                }
                else
                {
                    txt_freq_day1.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_freq_day1.setTextColor(R.color.ezfb_black);
                    report_days = "";
                    rel_visits.setVisibility(View.GONE);
                    txt_visits.setVisibility(View.GONE);
                    reportday1 = false;

                }
            }
        });

        txt_freq_day2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reportday2 == false) {
                    txt_freq_day2.setBackground(getResources().getDrawable(R.drawable.edt_round_filter));
                    txt_freq_day2.setTextColor(getResources().getColor(R.color.white));
                    txt_freq_day1.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_freq_day1.setTextColor(R.color.ezfb_black);
                    txt_freq_day3.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_freq_day3.setTextColor(R.color.ezfb_black);
                    rel_visits.setVisibility(View.VISIBLE);
                    txt_visits.setVisibility(View.VISIBLE);
                    report_days = txt_freq_day2.getText().toString();
                    reportday2 = true;
                    reportday1 = false;
                    reportday3 = false;
                }
                else
                {
                    txt_freq_day2.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_freq_day2.setTextColor(R.color.ezfb_black);
                    report_days = "";
                    rel_visits.setVisibility(View.GONE);
                    txt_visits.setVisibility(View.GONE);
                    reportday2 = false;

                }
            }
        });

        txt_freq_day3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reportday3 == false) {
                    txt_freq_day3.setBackground(getResources().getDrawable(R.drawable.edt_round_filter));
                    txt_freq_day3.setTextColor(getResources().getColor(R.color.white));
                    txt_freq_day2.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_freq_day2.setTextColor(R.color.ezfb_black);
                    txt_freq_day1.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_freq_day1.setTextColor(R.color.ezfb_black);
                    report_days = txt_freq_day3.getText().toString();
                    rel_visits.setVisibility(View.VISIBLE);
                    txt_visits.setVisibility(View.VISIBLE);
                    reportday3 = true;
                    reportday1 = false;
                    reportday2 = false;
                }
                else
                {
                    txt_freq_day3.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_freq_day3.setTextColor(R.color.ezfb_black);
                    report_days = "";
                    rel_visits.setVisibility(View.GONE);
                    txt_visits.setVisibility(View.GONE);
                    reportday3 = false;

                }
            }
        });

        txt_visit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(visit1 == false) {
                    txt_visit1.setBackground(getResources().getDrawable(R.drawable.edt_round_filter));
                    txt_visit1.setTextColor(getResources().getColor(R.color.white));
                    txt_visit2.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_visit2.setTextColor(R.color.ezfb_black);
                    txt_visit3.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_visit3.setTextColor(R.color.ezfb_black);
                    txt_visit4.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_visit4.setTextColor(R.color.ezfb_black);
                    txt_visit5.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_visit5.setTextColor(R.color.ezfb_black);
                    visit1 = true;
                    visit2 = false;
                    visit3 = false;
                    visit4 = false;
                    visit5 = false;

                }
                else
                {
                    txt_visit1.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_visit1.setTextColor(R.color.ezfb_black);
                    visit1 = false;

                }
            }
        });

        txt_visit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(visit2 == false) {
                    txt_visit2.setBackground(getResources().getDrawable(R.drawable.edt_round_filter));
                    txt_visit2.setTextColor(getResources().getColor(R.color.white));
                    txt_visit1.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_visit1.setTextColor(R.color.ezfb_black);
                    txt_visit3.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_visit3.setTextColor(R.color.ezfb_black);
                    txt_visit4.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_visit4.setTextColor(R.color.ezfb_black);
                    txt_visit5.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_visit5.setTextColor(R.color.ezfb_black);
                    visit2 = true;
                    visit1 = false;
                    visit3 = false;
                    visit4 = false;
                    visit5 = false;
                }
                else
                {
                    txt_visit2.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_visit2.setTextColor(R.color.ezfb_black);
                    visit2 = false;

                }
            }
        });

        txt_visit3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(visit3 == false) {
                    txt_visit3.setBackground(getResources().getDrawable(R.drawable.edt_round_filter));
                    txt_visit3.setTextColor(getResources().getColor(R.color.white));
                    txt_visit2.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_visit2.setTextColor(R.color.ezfb_black);
                    txt_visit1.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_visit1.setTextColor(R.color.ezfb_black);
                    txt_visit4.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_visit4.setTextColor(R.color.ezfb_black);
                    txt_visit5.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_visit5.setTextColor(R.color.ezfb_black);
                    visit3 = true;
                    visit1 = false;
                    visit2 = false;
                    visit4 = false;
                    visit5 = false;
                }
                else
                {
                    txt_visit3.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_visit3.setTextColor(R.color.ezfb_black);
                    visit3 = false;

                }
            }
        });

        txt_visit4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(visit4 == false) {
                    txt_visit4.setBackground(getResources().getDrawable(R.drawable.edt_round_filter));
                    txt_visit4.setTextColor(getResources().getColor(R.color.white));
                    txt_visit5.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_visit5.setTextColor(R.color.ezfb_black);
                    txt_visit3.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_visit3.setTextColor(R.color.ezfb_black);
                    txt_visit2.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_visit2.setTextColor(R.color.ezfb_black);
                    txt_visit1.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_visit1.setTextColor(R.color.ezfb_black);
                    visit4 = true;
                    visit1 = false;
                    visit2 = false;
                    visit3 = false;
                    visit5 = false;
                }
                else
                {
                    txt_visit4.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_visit4.setTextColor(R.color.ezfb_black);
                    visit4 = false;

                }
            }
        });

        txt_visit5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(visit5 == false) {
                    txt_visit5.setBackground(getResources().getDrawable(R.drawable.edt_round_filter));
                    txt_visit5.setTextColor(getResources().getColor(R.color.white));
                    txt_visit4.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_visit4.setTextColor(R.color.ezfb_black);
                    txt_visit3.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_visit3.setTextColor(R.color.ezfb_black);
                    txt_visit2.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_visit2.setTextColor(R.color.ezfb_black);
                    txt_visit1.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_visit1.setTextColor(R.color.ezfb_black);
                    visit5 = true;
                    visit1 = false;
                    visit2 = false;
                    visit3 = false;
                    visit4 = false;
                }
                else
                {
                    txt_visit5.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_visit5.setTextColor(R.color.ezfb_black);
                    visit5 = false;

                }
            }
        });


    }
}
