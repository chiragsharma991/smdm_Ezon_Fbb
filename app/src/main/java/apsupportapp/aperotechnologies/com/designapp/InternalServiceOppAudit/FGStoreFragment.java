package apsupportapp.aperotechnologies.com.designapp.InternalServiceOppAudit;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.ProductInformation.Style_Fragment;
import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by rkanawade on 10/10/17.
 */

public class FGStoreFragment extends Fragment {

    private Spinner spinner_storeformat, spinner_storename;
    private Context context;
    private ListView list_audit;
    FGStoreAdapter fgStoreAdapter;
    private TableLayout table_reports;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View v = inflater.inflate(R.layout.fragment_fgstore, container, false);
        context = v.getContext();

        spinner_storeformat = (Spinner) v.findViewById(R.id.spinner_storeformat);
        spinner_storename = (Spinner) v.findViewById(R.id.spinner_storename);
//        list_audit = (ListView) v.findViewById(R.id.list_audit1);
//
//        fgStoreAdapter = new FGStoreAdapter(context, list_audit);
//        list_audit.setAdapter(fgStoreAdapter);

        table_reports = (TableLayout) v.findViewById(R.id.table_reports);


        String[] headingTitle = new String[] { "         ", "Auditor Name", "Overall Audit Score", "Fashion Quotient(Display looks fashionable" };

        TableRow tableRow = new TableRow(context);
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                300, TableLayout.LayoutParams.WRAP_CONTENT);
       // tableRow.setBackgroundColor(getResources().getColor(android.R.color.black));

        for (int i = 0; i < headingTitle.length; i++) {
            TextView textView = new TextView(context);
            textView.setText(headingTitle[i]);
            textView.setLayoutParams(params);
            textView.setTextColor(getResources().getColor(android.R.color.black));
            tableRow.addView(textView);
        }
        table_reports.addView(tableRow);

//        addNumber();
//        addName();
//        addLocation();

        addRowA();
        addRowB();
        addRowC();

        return v;
    }

    private void addLocation() {

        TableRow tableRow3 = new TableRow(context);
        TableRow.LayoutParams params3 = new TableRow.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        TextView textView3 = new TextView(context);
        textView3.setLayoutParams(params3);
        textView3.setText("Kandivali, Mumbai 400008");
        textView3.setTextSize(13);
        tableRow3.addView(textView3);
        table_reports.addView(tableRow3);
    }

    private void addNumber() {

        TableRow tableRow1 = new TableRow(context);
        TableRow.LayoutParams params1 = new TableRow.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(context);
        textView.setLayoutParams(params1);
        textView.setText("FBB 001");
        textView.setTextSize(13);
        tableRow1.addView(textView);
        table_reports.addView(tableRow1);
    }

    private void addName() {

        TableRow tableRow2 = new TableRow(context);
        TableRow.LayoutParams params2 = new TableRow.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        TextView textView2 = new TextView(context);
        textView2.setLayoutParams(params2);
        textView2.setText("FBB-ABC Mall");
        textView2.setTextSize(13);
        tableRow2.addView(textView2);
        table_reports.addView(tableRow2);


    }

    private void addRowA() {

        String[] headingTitle = new String[] {"Audit 1", "Ketan Mokal", "2.1", "2" };

        TableRow tableRow = new TableRow(context);
        tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                300, 120);
        // tableRow.setBackgroundColor(getResources().getColor(android.R.color.black));

        for (int i = 0; i < headingTitle.length; i++) {
            TextView textView = new TextView(context);
            textView.setText(headingTitle[i]);
            textView.setLayoutParams(params);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setTextColor(getResources().getColor(android.R.color.black));
            tableRow.addView(textView);
        }
        table_reports.addView(tableRow);
    }

    private void addRowB() {

        String[] headingTitle = new String[] {"Audit 2", "Vikey malhotra ", "3.2", "4" };

        TableRow tableRow = new TableRow(context);
        tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                300, 120);
        // tableRow.setBackgroundColor(getResources().getColor(android.R.color.black));

        for (int i = 0; i < headingTitle.length; i++) {
            TextView textView = new TextView(context);
            textView.setText(headingTitle[i]);
            textView.setLayoutParams(params);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setTextColor(getResources().getColor(android.R.color.black));
            tableRow.addView(textView);
        }
        table_reports.addView(tableRow);
    }

    private void addRowC() {

        String[] headingTitle = new String[] {"Audit 3", "Sameer Singh", "3.2", "4" };

        TableRow tableRow = new TableRow(context);
        tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                300, 120);
        // tableRow.setBackgroundColor(getResources().getColor(android.R.color.black));

        for (int i = 0; i < headingTitle.length; i++) {
            TextView textView = new TextView(context);
            textView.setText(headingTitle[i]);
            textView.setLayoutParams(params);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setTextColor(getResources().getColor(android.R.color.black));
            tableRow.addView(textView);
        }
        table_reports.addView(tableRow);
    }

}
