package apsupportapp.aperotechnologies.com.designapp.CustomerLoyalty;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.easing.linear.Linear;

import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by pamrutkar on 23/06/17.
 */
public class CustDetailBarChart extends AppCompatActivity
{
    TextView txt_cdb_name,txt_cdb_mobile,txt_cdb_email;
    RelativeLayout rel_back_button;
    private LinearLayout phn_call,mail_call;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cd_barchart);
        getSupportActionBar().hide();
        initialise_UI();
    }

    private void initialise_UI()
    {
        txt_cdb_name = (TextView)findViewById(R.id.txt_cdb_name);
        txt_cdb_mobile = (TextView)findViewById(R.id.txt_cdb_mobileNo);
        txt_cdb_email = (TextView)findViewById(R.id.txt_cdb_email);
        rel_back_button = (RelativeLayout) findViewById(R.id.rel_back);
        phn_call = (LinearLayout) findViewById(R.id.lin_one);
        mail_call = (LinearLayout) findViewById(R.id.lin_two);
        txt_cdb_name.setText(CustomerDetailActivity.customerDetailsarray.get(0).getFullName());
        txt_cdb_mobile.setText(CustomerDetailActivity.customerDetailsarray.get(0).getMobileNumber());
        txt_cdb_email.setText(CustomerDetailActivity.customerDetailsarray.get(0).getEmailAddress());

        rel_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        phn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt_cdb_mobile.getTextSize()!=0){

                    Intent callIntent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+txt_cdb_mobile.getText().toString()));
                    startActivity(callIntent);
                }

            }
        });
        mail_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt_cdb_email.getTextSize()!=0){

                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto:"+txt_cdb_email.getText().toString()));
                    startActivity(Intent.createChooser(emailIntent, "Send feedback"));
                }

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
