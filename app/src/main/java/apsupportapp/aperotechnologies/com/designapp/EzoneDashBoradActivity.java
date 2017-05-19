package apsupportapp.aperotechnologies.com.designapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by pamrutkar on 18/05/17.
 */
public class EzoneDashBoradActivity extends AppCompatActivity implements View.OnClickListener
{
    private ImageButton btn_ezone_sales,btn_ezone_inventory;
    private LinearLayout linear_sales,linear_inventory;
    private TextView txt_headersales,txt_headerinventory;
    String ezone_sales="NO";
    String ezone_inventory="NO";
    private boolean ezone_sales_flag=false;
    private boolean ezone_inventry_flg=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ezone_dashboard);
        getSupportActionBar().hide();
        initialise();
    }

    private void initialise() {
        btn_ezone_sales = (ImageButton)findViewById(R.id.btn_ezone_sales);
        btn_ezone_inventory = (ImageButton)findViewById(R.id.btn_ezone_inventory);
        txt_headerinventory =(TextView)findViewById(R.id.headerinventory);
        txt_headersales = (TextView)findViewById(R.id.headersales);
        linear_inventory = (LinearLayout)findViewById(R.id.linear_inventory);
        linear_sales = (LinearLayout)findViewById(R.id.linear_sales);

        txt_headersales.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
        txt_headerinventory.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
        txt_headerinventory.setOnClickListener(this);
        txt_headersales.setOnClickListener(this);
        btn_ezone_inventory.setOnClickListener(this);
        btn_ezone_sales.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.headersales :
               if(ezone_sales.equals("NO"))
               {
                   linear_sales.setVisibility(View.VISIBLE);
                   linear_inventory.setVisibility(View.GONE);
                   txt_headersales.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.uplist,0);
                   txt_headerinventory.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                   ezone_sales = "YES";
                   ezone_inventory = "NO";
                   ezone_sales_flag = true;
                   ezone_inventry_flg=false;
               }
                else
               {
                   ezone_sales = "NO";
                   linear_sales.setVisibility(View.GONE);
                   txt_headersales.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                   ezone_sales_flag = false;
               }
                break;
            case R.id.headerinventory :
                if(ezone_inventory.equals("NO"))
                {
                    linear_sales.setVisibility(View.GONE);
                    linear_inventory.setVisibility(View.VISIBLE);
                    txt_headersales.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    txt_headerinventory.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.uplist,0);
                    ezone_sales = "NO";
                    ezone_inventory = "YES";
                    ezone_sales_flag = false;
                    ezone_inventry_flg=true;
                }
                else
                {
                    ezone_inventory = "NO";
                    linear_inventory.setVisibility(View.GONE);
                    txt_headerinventory.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    ezone_inventry_flg = false;
                }
                break;
            case R.id.btn_ezone_sales:
                new Ezone_Sales_Activity().StartIntent(EzoneDashBoradActivity.this);
                break;
            case R.id.btn_ezone_inventory:
                new Ezone_Freshness_Activity().StartIntent(EzoneDashBoradActivity.this);
                break;
        }
    }
}
