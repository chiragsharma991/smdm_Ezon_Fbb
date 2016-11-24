package apsupportapp.aperotechnologies.com.designapp.BestPerformersPromo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.R;

public class FilterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView PromostartDate,PromoEndDate,Mc;
    private LinearLayout linear_PromostartDate,linear_PromoEndDate,linear_Mc;
    String Start="OFF";
    String End="OFF";
    String MC="OFF";
    private ListView McListView;
    private ArrayList<String> McList;

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
        //promostartDate,promoEndDate,mc
        PromostartDate=(TextView)findViewById(R.id.promostartDate);
        PromoEndDate=(TextView)findViewById(R.id.promoEndDate);
        Mc=(TextView)findViewById(R.id.mc);

        linear_PromostartDate=(LinearLayout)findViewById(R.id.linear_PromostartDate);
        linear_PromoEndDate=(LinearLayout)findViewById(R.id.linear_PromoEndDate);
        linear_Mc=(LinearLayout)findViewById(R.id.linear_Mc);
        
        McListView=(ListView)findViewById(R.id.mcList);

        PromostartDate.setOnClickListener(this);
        PromoEndDate.setOnClickListener(this);
        Mc.setOnClickListener(this);


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
        }


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
}
