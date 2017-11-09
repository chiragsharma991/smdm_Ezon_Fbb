package apsupportapp.aperotechnologies.com.designapp.CustomerEngagementReport;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import apsupportapp.aperotechnologies.com.designapp.CustomerEngagementReport.Adapter.BasketAnalysisAdapter;
import apsupportapp.aperotechnologies.com.designapp.InfantApp.Adapter.FragmentCategoriesAdapter;
import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by pamrutkar on 07/11/17.
 */

public class BasketAnalysis extends AppCompatActivity implements View.OnClickListener, TabLayout.OnTabSelectedListener,CompoundButton.OnCheckedChangeListener
{
    private RelativeLayout rel_back;
    private TabLayout Tabview;
    private Switch mbrswitch,bandswitch;
    private Context context;
    private TextView txt_mbr,txt_nonmbr,txt_spendBand,txt_unitBand,txt_minRangeVal,txt_maxRangeVal;
    private TextView txt_br_NetSalesVal,txt_br_custVal,txt_br_atvVal,txt_br_uptVal,txt_br_spcVal,txt_br_upcVal;
    private LinearLayout lin_range;
    private RecyclerView br_recyclerView;
    BasketAnalysisAdapter basketAnalysisAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_analysis);
        getSupportActionBar().hide();
        context = this;
        intialise_ui();
        show_rangeBar();
    }


    private void intialise_ui()
    {
        rel_back = (RelativeLayout)findViewById(R.id.rel_back);
        rel_back.setOnClickListener(this);
        Tabview = (TabLayout) findViewById(R.id.tabview_basket);
        Tabview.addTab(Tabview.newTab().setText("LD"), 0);
        Tabview.addTab(Tabview.newTab().setText("WTD"), 1);
        Tabview.addTab(Tabview.newTab().setText("LW"), 2);
        Tabview.addTab(Tabview.newTab().setText("MTD"), 3);
        Tabview.addTab(Tabview.newTab().setText("LM"), 4);
        Tabview.addTab(Tabview.newTab().setText("YTD"), 5);
        Tabview.setOnTabSelectedListener(this);
        txt_br_NetSalesVal = (TextView)findViewById(R.id.txt_br_NetSalesVal);
        txt_br_custVal = (TextView)findViewById(R.id.txt_br_custVal);
        txt_br_atvVal = (TextView)findViewById(R.id.txt_br_atvVal);
        txt_br_uptVal = (TextView)findViewById(R.id.txt_br_uptVal);
        txt_br_spcVal = (TextView)findViewById(R.id.txt_br_spcVal);
        txt_br_upcVal = (TextView)findViewById(R.id.txt_br_upcVal);
        txt_mbr = (TextView)findViewById(R.id.txt_mbr);
        txt_nonmbr = (TextView)findViewById(R.id.txt_nonmbr);
        txt_spendBand = (TextView)findViewById(R.id.txt_spendBand);
        txt_unitBand = (TextView)findViewById(R.id.txt_unitBand);
        txt_maxRangeVal = (TextView)findViewById(R.id.txt_maxRangeVal);
        txt_minRangeVal = (TextView)findViewById(R.id.txt_minRangeVal);
        mbrswitch = (Switch)findViewById(R.id.mbrswitch);
        bandswitch = (Switch)findViewById(R.id.bandswitch);
        mbrswitch.setOnCheckedChangeListener(this);
        bandswitch.setOnCheckedChangeListener(this);
        lin_range = (LinearLayout)findViewById(R.id.lin_range);
//        br_recyclerView = (RecyclerView)findViewById(R.id.br_recyclerView);
//        br_recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
//        basketAnalysisAdapter = new BasketAnalysisAdapter(context,  br_recyclerView);
//        br_recyclerView.setAdapter(basketAnalysisAdapter);
    }
    private void show_rangeBar()
    {
        final ViewTreeObserver vto = lin_range.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    lin_range.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    lin_range.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int width = lin_range.getMeasuredWidth();
                int height = lin_range.getMeasuredHeight();
                Log.e( "onGlobalLayout: ","----"+width);
                int i  ;// This value depends on list size
//                lin_range.getLayoutParams().width = width/5;
//                lin_range.requestLayout();
                int[] colors = {Color.parseColor("#fe0000"), Color.parseColor("#2277b1"), Color.parseColor("#f3a314"), Color.parseColor("#ff7e00"), Color.parseColor("#70e503")};

                for( i = 0; i < 5; i++)
                {
                    TextView text = new TextView(context);
                    text.setText(""+i); // <-- does it rea
                    // lly compile without the + sign?
                    text.setTextSize(12);
                    text.setBackgroundColor(colors[i]);
                    text.setLayoutParams(new LinearLayout.LayoutParams(width/5, height));
                    lin_range.addView(text);
                }
            }
        });
    }



    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.rel_back:
                onBackPressed();
                break;
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab)
    {
        int checkedId = tab.getPosition();
        switch (checkedId)
        {
            case 0:
                Toast.makeText(context,"Position :"+tab.getPosition(),Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(context,"Position :"+tab.getPosition(),Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(context,"Position :"+tab.getPosition(),Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(context,"Position :"+tab.getPosition(),Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(context,"Position :"+tab.getPosition(),Toast.LENGTH_SHORT).show();
                break;
            case 5:
                Toast.makeText(context,"Position :"+tab.getPosition(),Toast.LENGTH_SHORT).show();
                break;


        }


    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId())
        {
            case R.id.mbrswitch:
                mbrSwitchFun();
                break;
            case R.id.bandswitch:
                bandSwitchFun();
        }
    }

    private void mbrSwitchFun()
    {
        if(mbrswitch.isChecked())
        {
            txt_nonmbr.setTextColor(Color.parseColor("#000000"));
            txt_mbr.setTextColor(Color.parseColor("#dfdedf"));

        }
        else
        {
            txt_mbr.setTextColor(Color.parseColor("#000000"));
            txt_nonmbr.setTextColor(Color.parseColor("#dfdedf"));
        }
    }
    private void bandSwitchFun()
    {
        if(bandswitch.isChecked())
        {
            txt_unitBand.setTextColor(Color.parseColor("#000000"));
            txt_spendBand.setTextColor(Color.parseColor("#dfdedf"));
        }
        else
        {
            txt_unitBand.setTextColor(Color.parseColor("#dfdedf"));
            txt_spendBand.setTextColor(Color.parseColor("#000000"));
        }
    }
}
