package apsupportapp.aperotechnologies.com.designapp.InternalServiceOppAudit;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import apsupportapp.aperotechnologies.com.designapp.R;

public class ViewDetailsTableActivity extends AppCompatActivity {

    private TableLayout table_reports;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details_table);
        context = this;

        table_reports = (TableLayout) findViewById(R.id.table_reports);

        String[] headingTitle = new String[] {"   ","Fashion Quotient(Display looks fashionable)", "Merchandise display(by size/full size set)", "Merchandise presentation standards", "Staff available standards", };

        TableRow tableRow = new TableRow(context);
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                320, TableLayout.LayoutParams.WRAP_CONTENT);
        // tableRow.setBackgroundColor(getResources().getColor(android.R.color.black));

        for (int i = 0; i < headingTitle.length; i++) {
            TextView textView = new TextView(context);
            textView.setText(headingTitle[i]);
            textView.setLayoutParams(params);
            textView.setTextColor(getResources().getColor(android.R.color.black));
            tableRow.addView(textView);
        }
        table_reports.addView(tableRow);

        addRowA();

    }

    private void addRowA() {

        String[] headingTitle = new String[] {"Audit 1", "3", "5", "3", "4" };

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
