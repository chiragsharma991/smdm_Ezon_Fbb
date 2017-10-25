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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by rkanawade on 10/10/17.
 */

public class CompetitorStoreFragment extends Fragment {

    private Spinner spinner_storeformat, spinner_city, spinner_mallname;
    private Context context;
    private ListView list_audit;
    FGStoreAdapter fgStoreAdapter;
    private TableLayout table_reports;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View v = inflater.inflate(R.layout.fragment_competitorstore, container, false);
        context = v.getContext();

        spinner_storeformat = (Spinner) v.findViewById(R.id.spinner_storeformat);
        spinner_city = (Spinner) v.findViewById(R.id.spinner_city);
        spinner_mallname = (Spinner) v.findViewById(R.id.spinner_mallname);

//        list_audit = (ListView) v.findViewById(R.id.list_audit1);
//
//        fgStoreAdapter = new FGStoreAdapter(context, list_audit);
//        list_audit.setAdapter(fgStoreAdapter);

        table_reports = (TableLayout) v.findViewById(R.id.table_reports);


        String[] headingTitle = new String[] { "         ", "Auditor Name", "Overall Audit Score", "Fashion Quotient(Display looks fashionable" };

        TableRow tableRow = new TableRow(context);
        tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                260, TableLayout.LayoutParams.WRAP_CONTENT);
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

//        addNumber();
//        addName();
//        addLocation();

        addRowA();
        addRowB();
        addRowC();

        return v;
    }

    private void addRowA() {

        String[] headingTitle = new String[] {"Audit 1", "Ketan Mokal", "2.1", "2" };

        TableRow tableRow = new TableRow(context);
        tableRow.setBackgroundColor(Color.parseColor("#a8a8a8"));
        tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                260, 120);
        // tableRow.setBackgroundColor(getResources().getColor(android.R.color.black));

        for (int i = 0; i < headingTitle.length; i++) {
            TextView textView = new TextView(context);
            textView.setText(headingTitle[i]);
            textView.setLayoutParams(params);
            textView.setBackground(getResources().getDrawable(R.drawable.cell_shape));
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
                260, 120);
        // tableRow.setBackgroundColor(getResources().getColor(android.R.color.black));

        for (int i = 0; i < headingTitle.length; i++) {
            TextView textView = new TextView(context);
            textView.setText(headingTitle[i]);
            textView.setLayoutParams(params);
            textView.setBackground(getResources().getDrawable(R.drawable.cell_shape));
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
        tableRow.setBackgroundColor(Color.parseColor("#a8a8a8"));
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                260, 120);
        // tableRow.setBackgroundColor(getResources().getColor(android.R.color.black));

        for (int i = 0; i < headingTitle.length; i++) {
            TextView textView = new TextView(context);
            textView.setText(headingTitle[i]);
            textView.setLayoutParams(params);
            textView.setBackground(getResources().getDrawable(R.drawable.cell_shape));
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setTextColor(getResources().getColor(android.R.color.black));
            tableRow.addView(textView);
        }
        table_reports.addView(tableRow);
    }

}
