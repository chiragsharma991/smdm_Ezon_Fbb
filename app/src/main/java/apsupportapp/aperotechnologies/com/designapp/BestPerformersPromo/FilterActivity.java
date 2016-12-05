package apsupportapp.aperotechnologies.com.designapp.BestPerformersPromo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import apsupportapp.aperotechnologies.com.designapp.ExpiringPromo.ExpiringPromoActivity;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.RunningPromo.RunningPromoActivity;
import apsupportapp.aperotechnologies.com.designapp.UpcomingPromo.UpcomingPromo;
import apsupportapp.aperotechnologies.com.designapp.WorstPerformersPromo.WorstPerformerActivity;

public class FilterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView PromostartDate,PromoEndDate,Mc;
    private LinearLayout linear_PromostartDate,linear_PromoEndDate,linear_Mc;
    String Start="OFF";
    String End="OFF";
    String MC="OFF";
    private ListView McListView;
    private ArrayList<String> McList;
    private static TextView EdtPromoEndDate;
    private static TextView EdtPromoStartDate;
    private RelativeLayout Filter_imageBtnBack;
    private RelativeLayout FilterOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        getSupportActionBar().hide();
        intialize();
        main();



    }

    private void main()
    {
        McList=new ArrayList<String>();
        McList.add("L123-Lace Party Top Black");
        McList.add("L123-Lace Party Top Yello");
        McList.add("L123-Lace Party Top Green");
        McList.add("L123-Lace Party Top White");
        McList.add("L123-Lace Party Top Orange");
        McList.add("L123-Lace Party Top Red");
        McList.add("L123-Lace Party Top Grey");
        FilterAdapter filterAdapter=new FilterAdapter(McList,this);
        McListView.setAdapter(filterAdapter);



    }

    private void intialize()
    {
        EdtPromoEndDate=(TextView)findViewById(R.id.edtPromoEndDate);
        EdtPromoStartDate=(TextView)findViewById(R.id.edtPromoStartDate);

        PromostartDate=(TextView)findViewById(R.id.promostartDate);
        PromoEndDate=(TextView)findViewById(R.id.promoEndDate);
        Mc=(TextView)findViewById(R.id.mc);

        linear_PromostartDate=(LinearLayout)findViewById(R.id.linear_PromostartDate);
        linear_PromoEndDate=(LinearLayout)findViewById(R.id.linear_PromoEndDate);
        linear_Mc=(LinearLayout)findViewById(R.id.linear_Mc);

        Filter_imageBtnBack=(RelativeLayout)findViewById(R.id.filter_imageBtnBack);
        FilterOk=(RelativeLayout)findViewById(R.id.filterOk);

        McListView=(ListView)findViewById(R.id.mcList);

        PromostartDate.setOnClickListener(this);
        PromoEndDate.setOnClickListener(this);
        Filter_imageBtnBack.setOnClickListener(this);
        FilterOk.setOnClickListener(this);
        Mc.setOnClickListener(this);
        EdtPromoStartDate.setOnClickListener(this);
        EdtPromoEndDate.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.promostartDate:
                StartDate();
                break;
            case R.id.promoEndDate:
                EndDate();
                break;
            case R.id.mc:
                MC();
                break;
            case R.id.edtPromoStartDate:
                StartDatePicker(v);
                break;
            case R.id.edtPromoEndDate:
                EndDatePicker(v);
                break;

            case R.id.filter_imageBtnBack:
                filterBack();
                break;
            case R.id.filterOk:
                Toast.makeText(this,"Activity is still in process",Toast.LENGTH_LONG).show();
                break;




        }


    }

    private void filterBack() {

        if(getIntent().getStringExtra("from").equals("bestPromo"))
        {
            Intent intent=new Intent(this, BestPerformerActivity.class);
            startActivity(intent);
            finish();
        }
        else if(getIntent().getStringExtra("from").equals("worstPromo"))
        {
            Intent intent=new Intent(this, WorstPerformerActivity.class);
            startActivity(intent);
            finish();

        }
        else if(getIntent().getStringExtra("from").equals("upComingPromo"))
        {
            Intent intent=new Intent(this, UpcomingPromo.class);
            startActivity(intent);
            finish();
        }
        else if(getIntent().getStringExtra("from").equals("runningPromo"))
        {
            Intent intent=new Intent(this, RunningPromoActivity.class);
            startActivity(intent);
            finish();
        }
        else if(getIntent().getStringExtra("from").equals("expiringPromo"))
        {
            Intent intent=new Intent(this, ExpiringPromoActivity.class);
            startActivity(intent);
            finish();
        }
   }

    private void EndDatePicker(View v) {
        DialogFragment diaogFragment=new EndDatePickerFragment();
        diaogFragment.show(getSupportFragmentManager(),"DatePicker");
    }

    private void StartDatePicker(View v) {
        DialogFragment diaogFragment=new StartDatePickerFragment();
        diaogFragment.show(getSupportFragmentManager(),"DatePicker");

    }

    private void StartDate() {

        if(Start.equals("OFF")){
            linear_PromostartDate.setVisibility(View.VISIBLE);
            PromostartDate.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.uplist,0);
            Start="ON";
        }else
        {
            linear_PromostartDate.setVisibility(View.GONE);
            PromostartDate.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
            Start="OFF";
        }

    }

    private void EndDate() {

        if(End.equals("OFF")){
            linear_PromoEndDate.setVisibility(View.VISIBLE);
            PromoEndDate.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.uplist,0);
            End="ON";
        }else
        {
            linear_PromoEndDate.setVisibility(View.GONE);
            PromoEndDate.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
            End="OFF";
        }
    }

    private void MC() {

        if(MC.equals("OFF")){
            linear_Mc.setVisibility(View.VISIBLE);
            Mc.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.uplist,0);
            MC="ON";
        }else
        {
            linear_Mc.setVisibility(View.GONE);
            Mc.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
            MC="OFF";
        }
    }

    public static class StartDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
    {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }


        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            EdtPromoStartDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

        }
    }

    public static class EndDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
    {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }


        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            EdtPromoEndDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        filterBack();
    }
}
