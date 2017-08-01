package apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.ProductQualityRangeHO;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import apsupportapp.aperotechnologies.com.designapp.R;

public class Callback_ProductQualityRange extends AppCompatActivity {

    private TextView txt_mobile_number, txt_remarks, txt_article_id, txt_cust_name, txt_brand_name, txt_product_name, txt_size, txt_color;
    private TextView txt_fit, txt_style, txt_callback, txt_feedback_date, txt_email, txt_sms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback_product_quality_range);

        initialiseUI();
    }

    private void initialiseUI() {

        txt_mobile_number = (TextView) findViewById(R.id.txt_mobile_number);
        txt_remarks = (TextView) findViewById(R.id.txt_remarks);
        txt_article_id = (TextView) findViewById(R.id.txt_article_id);
        txt_cust_name = (TextView) findViewById(R.id.txt_cust_name);
        txt_brand_name = (TextView) findViewById(R.id.txt_brand_name);
        txt_product_name = (TextView) findViewById(R.id.txt_product_name);
        txt_size = (TextView) findViewById(R.id.txt_size);
        txt_color = (TextView) findViewById(R.id.txt_color);
        txt_fit = (TextView) findViewById(R.id.txt_fit);
        txt_style = (TextView) findViewById(R.id.txt_style);
        txt_feedback_date = (TextView) findViewById(R.id.txt_feedback_date);
        txt_callback = (TextView) findViewById(R.id.txt_callback);
        txt_email = (TextView) findViewById(R.id.txt_email);
        txt_sms = (TextView) findViewById(R.id.txt_sms);
    }
}
