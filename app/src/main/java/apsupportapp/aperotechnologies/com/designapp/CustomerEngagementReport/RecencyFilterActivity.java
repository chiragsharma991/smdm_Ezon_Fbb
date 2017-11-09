package apsupportapp.aperotechnologies.com.designapp.CustomerEngagementReport;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import apsupportapp.aperotechnologies.com.designapp.CustomerEngagement.CustomerDetailActivity;
import apsupportapp.aperotechnologies.com.designapp.R;

public class RecencyFilterActivity extends AppCompatActivity {

    private Context context;
    private Button btn_cust;
    private TextView txt_day1, txt_day2, txt_day3, txt_day4, txt_day5, txt_freq_day1, txt_freq_day2, txt_freq_day3;
    Boolean day1 = false, day2 = false, day3 = false, day4 = false, day5 = false, reportday1 = false, reportday2 = false, reportday3 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recency_filter);
        getSupportActionBar().hide();
        context = this;
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

        txt_day1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(day1 == false) {
                    txt_day1.setBackground(getResources().getDrawable(R.drawable.edt_round_filter));
                    txt_day1.setTextColor(getResources().getColor(R.color.white));
                    day1 = true;
                }
                else
                {
                    txt_day1.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_day1.setTextColor(R.color.ezfb_black);
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
                    day2 = true;
                }
                else
                {
                    txt_day2.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_day2.setTextColor(R.color.ezfb_black);
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
                    day3 = true;
                }
                else
                {
                    txt_day3.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_day3.setTextColor(R.color.ezfb_black);
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
                    day4 = true;
                }
                else
                {
                    txt_day4.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_day4.setTextColor(R.color.ezfb_black);
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
                    day5 = true;
                }
                else
                {
                    txt_day5.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_day5.setTextColor(R.color.ezfb_black);
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
                    reportday1 = true;
                }
                else
                {
                    txt_freq_day1.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_freq_day1.setTextColor(R.color.ezfb_black);
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
                    reportday2 = true;
                }
                else
                {
                    txt_freq_day2.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_freq_day2.setTextColor(R.color.ezfb_black);
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
                    reportday3 = true;
                }
                else
                {
                    txt_freq_day3.setBackgroundColor(getResources().getColor(R.color.white));
                    txt_freq_day3.setTextColor(R.color.ezfb_black);
                    reportday3 = false;

                }
            }
        });

    }
}
