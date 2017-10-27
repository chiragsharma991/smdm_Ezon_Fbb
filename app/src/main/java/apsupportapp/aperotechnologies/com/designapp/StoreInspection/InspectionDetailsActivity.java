package apsupportapp.aperotechnologies.com.designapp.StoreInspection;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import org.json.JSONArray;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;

/**
 * Created by pamrutkar on 18/04/17.
 */
public class InspectionDetailsActivity extends AppCompatActivity implements View.OnClickListener{
    private RelativeLayout insp_detls_btnback;
    Context context = this;
    private String userId;
    private String bearertoken, storeCode;
    private RequestQueue queue;
    private SharedPreferences sharedPreferences;
    int inspectn_Id;
    private Gson gson;
    private InspectionBeanClass beanClass;
    ArrayList<InspectionBeanClass> inspdetls_ArrayList;

    //Image View
    ImageView image_improvement_inspdetls_cri_1,image_okay_inspdetls_cri_1,image_good_inspdetls_cri_1,image_excellent_inspdetls_cri_1;
    ImageView image_improvement_inspdetls_cri_2,image_okay_inspdetls_cri_2,image_good_inspdetls_cri_2,image_excellent_inspdetls_cri_2;
    ImageView image_improvement_inspdetls_cri_3,image_okay_inspdetls_cri_3,image_good_inspdetls_cri_3,image_excellent_inspdetls_cri_3;
    ImageView image_improvement_inspdetls_cri_4,image_okay_inspdetls_cri_4,image_good_inspdetls_cri_4,image_excellent_inspdetls_cri_4;
    ImageView image_improvement_inspdetls_cri_5,image_okay_inspdetls_cri_5,image_good_inspdetls_cri_5,image_excellent_inspdetls_cri_5;
    ImageView image_improvement_inspdetls_cri_6,image_okay_inspdetls_cri_6,image_good_inspdetls_cri_6,image_excellent_inspdetls_cri_6;
    ImageView image_improvement_inspdetls_cri_7,image_okay_inspdetls_cri_7,image_good_inspdetls_cri_7,image_excellent_inspdetls_cri_7;
    ImageView image_improvement_inspdetls_cri_8,image_okay_inspdetls_cri_8,image_good_inspdetls_cri_8,image_excellent_inspdetls_cri_8;
    //Image View
    ImageView image_improvement_inspdetls_criteria_1,image_okay_inspdetls_criteria_1,image_good_inspdetls_criteria_1,image_excellent_inspdetls_criteria_1;
    ImageView image_improvement_inspdetls_criteria_2,image_okay_inspdetls_criteria_2,image_good_inspdetls_criteria_2,image_excellent_inspdetls_criteria_2;
    ImageView image_improvement_inspdetls_criteria_3,image_okay_inspdetls_criteria_3,image_good_inspdetls_criteria_3,image_excellent_inspdetls_criteria_3;
    ImageView image_improvement_inspdetls_criteria_4,image_okay_inspdetls_criteria_4,image_good_inspdetls_criteria_4,image_excellent_inspdetls_criteria_4;
    ImageView image_improvement_inspdetls_criteria_5,image_okay_inspdetls_criteria_5,image_good_inspdetls_criteria_5,image_excellent_inspdetls_criteria_5;
    ImageView image_improvement_inspdetls_criteria_6,image_okay_inspdetls_criteria_6,image_good_inspdetls_criteria_6,image_excellent_inspdetls_criteria_6;
    ImageView image_improvement_inspdetls_criteria_7,image_okay_inspdetls_criteria_7,image_good_inspdetls_criteria_7,image_excellent_inspdetls_criteria_7;
    ImageView image_improvement_inspdetls_criteria_8,image_okay_inspdetls_criteria_8,image_good_inspdetls_criteria_8,image_excellent_inspdetls_criteria_8;


    ImageView camera_imageView1,emoji_image;
    // Emoji Text Declaration
    TextView txt_improvement_inspdetls_cri_1,txt_okay_inspdetls_cri_1,txt_good_inspdetls_cri_1,txt_excellent_inspdetls_cri_1;
    TextView txt_improvement_inspdetls_cri_2,txt_okay_inspdetls_cri_2,txt_good_inspdetls_cri_2,txt_excellent_inspdetls_cri_2;
    TextView txt_improvement_inspdetls_cri_3,txt_okay_inspdetls_cri_3,txt_good_inspdetls_cri_3,txt_excellent_inspdetls_cri_3;
    TextView txt_improvement_inspdetls_cri_4,txt_okay_inspdetls_cri_4,txt_good_inspdetls_cri_4,txt_excellent_inspdetls_cri_4;
    TextView txt_improvement_inspdetls_cri_5,txt_okay_inspdetls_cri_5,txt_good_inspdetls_cri_5,txt_excellent_inspdetls_cri_5;
    TextView txt_improvement_inspdetls_cri_6,txt_okay_inspdetls_cri_6,txt_good_inspdetls_cri_6,txt_excellent_inspdetls_cri_6;
    TextView txt_improvement_inspdetls_cri_7,txt_okay_inspdetls_cri_7,txt_good_inspdetls_cri_7,txt_excellent_inspdetls_cri_7;
    TextView txt_improvement_inspdetls_cri_8,txt_okay_inspdetls_cri_8,txt_good_inspdetls_cri_8,txt_excellent_inspdetls_cri_8;

   TextView txt_inspdetls_id_Val,txt_inspdetls_date_Val,txt_inspdetls_name_Val,txt_comment_val,inspection_txtStoreCode,inspection_txtStoreName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insp_details);
        getSupportActionBar().hide();
        initialise();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();
        inspectn_Id = getIntent().getIntExtra("inspectionId",0);
        inspdetls_ArrayList = new ArrayList<InspectionBeanClass>();

        if (Reusable_Functions.chkStatus(InspectionDetailsActivity.this))
        {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(InspectionDetailsActivity.this, "Loading data...");
            requestInspectionDetails(inspectn_Id);
        }
        else
        {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }


    private void initialise() {

        if(getIntent().getExtras().getString("store_Code") != null )
        {
            storeCode = getIntent().getExtras().getString("store_Code");
        }
        insp_detls_btnback = (RelativeLayout)findViewById(R.id.insp_detls_btnback);
        txt_inspdetls_date_Val = (TextView)findViewById(R.id.txt_inspdetls_date_Val);
        txt_inspdetls_id_Val = (TextView)findViewById(R.id.txt_inspdetls_id_Val);
        txt_inspdetls_name_Val = (TextView)findViewById(R.id.txt_inspdetls_name_Val);
        inspection_txtStoreName = (TextView)findViewById(R.id.inspection_txtStoreName);
        inspection_txtStoreCode = (TextView)findViewById(R.id.inspection_txtStoreCode);
        txt_comment_val = (TextView)findViewById(R.id.txt_comment_val);
        camera_imageView1 = (ImageView)findViewById(R.id.camera_imageView1);
        emoji_image = (ImageView)findViewById(R.id.emoji_image);
        insp_detls_btnback.setOnClickListener(this);
        //Improvement emoji image
        image_improvement_inspdetls_cri_1 = (ImageView)findViewById(R.id.image_improvement_cri_1);
        image_improvement_inspdetls_cri_2 = (ImageView)findViewById(R.id.image_improvement_cri_2);
        image_improvement_inspdetls_cri_3 = (ImageView)findViewById(R.id.image_improvement_cri_3);
        image_improvement_inspdetls_cri_4 = (ImageView)findViewById(R.id.image_improvement_cri_4);
        image_improvement_inspdetls_cri_5 = (ImageView)findViewById(R.id.image_improvement_cri_5);
        image_improvement_inspdetls_cri_6 = (ImageView)findViewById(R.id.image_improvement_cri_6);
        image_improvement_inspdetls_cri_7 = (ImageView)findViewById(R.id.image_improvement_cri_7);
        image_improvement_inspdetls_cri_8 = (ImageView)findViewById(R.id.image_improvement_cri_8);
        //Okay emoji image
        image_okay_inspdetls_cri_1 = (ImageView)findViewById(R.id.image_okay_cri_1);
        image_okay_inspdetls_cri_2 = (ImageView)findViewById(R.id.image_okay_cri_2);
        image_okay_inspdetls_cri_3 = (ImageView)findViewById(R.id.image_okay_cri_3);
        image_okay_inspdetls_cri_4 = (ImageView)findViewById(R.id.image_okay_cri_4);
        image_okay_inspdetls_cri_5 = (ImageView)findViewById(R.id.image_okay_cri_5);
        image_okay_inspdetls_cri_6 = (ImageView)findViewById(R.id.image_okay_cri_6);
        image_okay_inspdetls_cri_7 = (ImageView)findViewById(R.id.image_okay_cri_7);
        image_okay_inspdetls_cri_8 = (ImageView)findViewById(R.id.image_okay_cri_8);
        //Good Emoji image
        image_good_inspdetls_cri_1 = (ImageView)findViewById(R.id.image_good_cri_1);
        image_good_inspdetls_cri_2 = (ImageView)findViewById(R.id.image_good_cri_2);
        image_good_inspdetls_cri_3 = (ImageView)findViewById(R.id.image_good_cri_3);
        image_good_inspdetls_cri_4 = (ImageView)findViewById(R.id.image_good_cri_4);
        image_good_inspdetls_cri_5 = (ImageView)findViewById(R.id.image_good_cri_5);
        image_good_inspdetls_cri_6 = (ImageView)findViewById(R.id.image_good_cri_6);
        image_good_inspdetls_cri_7 = (ImageView)findViewById(R.id.image_good_cri_7);
        image_good_inspdetls_cri_8 = (ImageView)findViewById(R.id.image_good_cri_8);
        //excellent emoji image
        image_excellent_inspdetls_cri_1 = (ImageView)findViewById(R.id.image_excellent_cri_1);
        image_excellent_inspdetls_cri_2 = (ImageView)findViewById(R.id.image_excellent_cri_2);
        image_excellent_inspdetls_cri_3 = (ImageView)findViewById(R.id.image_excellent_cri_3);
        image_excellent_inspdetls_cri_4 = (ImageView)findViewById(R.id.image_excellent_cri_4);
        image_excellent_inspdetls_cri_5 = (ImageView)findViewById(R.id.image_excellent_cri_5);
        image_excellent_inspdetls_cri_6 = (ImageView)findViewById(R.id.image_excellent_cri_6);
        image_excellent_inspdetls_cri_7 = (ImageView)findViewById(R.id.image_excellent_cri_7);
        image_excellent_inspdetls_cri_8 = (ImageView)findViewById(R.id.image_excellent_cri_8);
        //Improvement Emoji default
        image_improvement_inspdetls_criteria_1 = (ImageView)findViewById(R.id.image_improvement_criteria_1);
        image_improvement_inspdetls_criteria_2 = (ImageView)findViewById(R.id.image_improvement_criteria_2);
        image_improvement_inspdetls_criteria_3 = (ImageView)findViewById(R.id.image_improvement_criteria_3);
        image_improvement_inspdetls_criteria_4 = (ImageView)findViewById(R.id.image_improvement_criteria_4);
        image_improvement_inspdetls_criteria_5 = (ImageView)findViewById(R.id.image_improvement_criteria_5);
        image_improvement_inspdetls_criteria_6 = (ImageView)findViewById(R.id.image_improvement_criteria_6);
        image_improvement_inspdetls_criteria_7 = (ImageView)findViewById(R.id.image_improvement_criteria_7);
        image_improvement_inspdetls_criteria_8 = (ImageView)findViewById(R.id.image_improvement_criteria_8);
        //Okay emoji image default
        image_okay_inspdetls_criteria_1 = (ImageView)findViewById(R.id.image_okay_criteria_1);
        image_okay_inspdetls_criteria_2 = (ImageView)findViewById(R.id.image_okay_criteria_2);
        image_okay_inspdetls_criteria_3 = (ImageView)findViewById(R.id.image_okay_criteria_3);
        image_okay_inspdetls_criteria_4 = (ImageView)findViewById(R.id.image_okay_criteria_4);
        image_okay_inspdetls_criteria_5 = (ImageView)findViewById(R.id.image_okay_criteria_5);
        image_okay_inspdetls_criteria_6 = (ImageView)findViewById(R.id.image_okay_criteria_6);
        image_okay_inspdetls_criteria_7 = (ImageView)findViewById(R.id.image_okay_criteria_7);
        image_okay_inspdetls_criteria_8 = (ImageView)findViewById(R.id.image_okay_criteria_8);
        //Good Emoji image default
        image_good_inspdetls_criteria_1 = (ImageView)findViewById(R.id.image_good_criteria_1);
        image_good_inspdetls_criteria_2 = (ImageView)findViewById(R.id.image_good_criteria_2);
        image_good_inspdetls_criteria_3 = (ImageView)findViewById(R.id.image_good_criteria_3);
        image_good_inspdetls_criteria_4 = (ImageView)findViewById(R.id.image_good_criteria_4);
        image_good_inspdetls_criteria_5 = (ImageView)findViewById(R.id.image_good_criteria_5);
        image_good_inspdetls_criteria_6 = (ImageView)findViewById(R.id.image_good_criteria_6);
        image_good_inspdetls_criteria_7 = (ImageView)findViewById(R.id.image_good_criteria_7);
        image_good_inspdetls_criteria_8 = (ImageView)findViewById(R.id.image_good_criteria_8);
        //excellent emoji image default
        image_excellent_inspdetls_criteria_1 = (ImageView)findViewById(R.id.image_excellent_criteria_1);
        image_excellent_inspdetls_criteria_2 = (ImageView)findViewById(R.id.image_excellent_criteria_2);
        image_excellent_inspdetls_criteria_3 = (ImageView)findViewById(R.id.image_excellent_criteria_3);
        image_excellent_inspdetls_criteria_4 = (ImageView)findViewById(R.id.image_excellent_criteria_4);
        image_excellent_inspdetls_criteria_5 = (ImageView)findViewById(R.id.image_excellent_criteria_5);
        image_excellent_inspdetls_criteria_6 = (ImageView)findViewById(R.id.image_excellent_criteria_6);
        image_excellent_inspdetls_criteria_7 = (ImageView)findViewById(R.id.image_excellent_criteria_7);
        image_excellent_inspdetls_criteria_8 = (ImageView)findViewById(R.id.image_excellent_criteria_8);
        //Improvement emoji text
        txt_improvement_inspdetls_cri_1 = (TextView)findViewById(R.id.txt_improvement_criteria_1);
        txt_improvement_inspdetls_cri_2 = (TextView)findViewById(R.id.txt_improvement_criteria_2);
        txt_improvement_inspdetls_cri_3 = (TextView)findViewById(R.id.txt_improvement_criteria_3);
        txt_improvement_inspdetls_cri_4 = (TextView)findViewById(R.id.txt_improvement_criteria_4);
        txt_improvement_inspdetls_cri_5 = (TextView)findViewById(R.id.txt_improvement_criteria_5);
        txt_improvement_inspdetls_cri_6 = (TextView)findViewById(R.id.txt_improvement_criteria_6);
        txt_improvement_inspdetls_cri_7 = (TextView)findViewById(R.id.txt_improvement_criteria_7);
        txt_improvement_inspdetls_cri_8 = (TextView)findViewById(R.id.txt_improvement_criteria_8);
        //Okay emoji text
        txt_okay_inspdetls_cri_1 = (TextView)findViewById(R.id.txt_okay_criteria_1);
        txt_okay_inspdetls_cri_2 = (TextView)findViewById(R.id.txt_okay_criteria_2);
        txt_okay_inspdetls_cri_3 = (TextView)findViewById(R.id.txt_okay_criteria_3);
        txt_okay_inspdetls_cri_4 = (TextView)findViewById(R.id.txt_okay_criteria_4);
        txt_okay_inspdetls_cri_5 = (TextView)findViewById(R.id.txt_okay_criteria_5);
        txt_okay_inspdetls_cri_6 = (TextView)findViewById(R.id.txt_okay_criteria_6);
        txt_okay_inspdetls_cri_7 = (TextView)findViewById(R.id.txt_okay_criteria_7);
        txt_okay_inspdetls_cri_8 = (TextView)findViewById(R.id.txt_okay_criteria_8);
        //Good Emoji text
        txt_good_inspdetls_cri_1 = (TextView)findViewById(R.id.txt_good_criteria_1);
        txt_good_inspdetls_cri_2 = (TextView)findViewById(R.id.txt_good_criteria_2);
        txt_good_inspdetls_cri_3 = (TextView)findViewById(R.id.txt_good_criteria_3);
        txt_good_inspdetls_cri_4 = (TextView)findViewById(R.id.txt_good_criteria_4);
        txt_good_inspdetls_cri_5 = (TextView)findViewById(R.id.txt_good_criteria_5);
        txt_good_inspdetls_cri_6 = (TextView)findViewById(R.id.txt_good_criteria_6);
        txt_good_inspdetls_cri_7 = (TextView)findViewById(R.id.txt_good_criteria_7);
        txt_good_inspdetls_cri_8 = (TextView)findViewById(R.id.txt_good_criteria_8);
        //excellent emoji text
        txt_excellent_inspdetls_cri_1 = (TextView)findViewById(R.id.txt_excellent_criteria_1);
        txt_excellent_inspdetls_cri_2 = (TextView)findViewById(R.id.txt_excellent_criteria_2);
        txt_excellent_inspdetls_cri_3 = (TextView)findViewById(R.id.txt_excellent_criteria_3);
        txt_excellent_inspdetls_cri_4 = (TextView)findViewById(R.id.txt_excellent_criteria_4);
        txt_excellent_inspdetls_cri_5 = (TextView)findViewById(R.id.txt_excellent_criteria_5);
        txt_excellent_inspdetls_cri_6 = (TextView)findViewById(R.id.txt_excellent_criteria_6);
        txt_excellent_inspdetls_cri_7 = (TextView)findViewById(R.id.txt_excellent_criteria_7);
        txt_excellent_inspdetls_cri_8 = (TextView)findViewById(R.id.txt_excellent_criteria_8);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.insp_detls_btnback:
                onBackPressed();
                break;
        }
    }

    //-------------------------- API Declaration --------------------------//
    private void requestInspectionDetails(final int inspectn_id)
    {
            String url = ConstsCore.web_url + "/v1/display/storeinspection/" + userId+"?inspectionId="+inspectn_id + "&storeCode=" + storeCode;
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response)
                        {
                            try
                            {

                                if (response.equals("") || response == null || response.length() == 0 ) {
                                    Reusable_Functions.hDialog();
                                    Toast.makeText(InspectionDetailsActivity.this, "no data found", Toast.LENGTH_SHORT).show();
                                    // return;

                                } else {

                                    for (int i = 0; i < response.length(); i++)
                                    {
                                        beanClass = gson.fromJson(response.get(i).toString(), InspectionBeanClass.class);
                                        inspdetls_ArrayList.add(beanClass);

                                        int id = inspdetls_ArrayList.get(i).getInspectionId();
                                        String name = inspdetls_ArrayList.get(i).getInspectorName();
                                        String date = inspdetls_ArrayList.get(i).getInspectionDate();
                                        String storeImg = inspdetls_ArrayList.get(i).getStoreImg();
                                        String rating = inspdetls_ArrayList.get(i).getRating();

                                        txt_inspdetls_name_Val.setText(name);
                                        txt_inspdetls_id_Val.setText(""+id);
                                        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                                        DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
                                        Date update_date = null;
                                        try {
                                            update_date = inputFormat.parse(date);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        String outputDateStr = outputFormat.format(update_date);
                                        txt_inspdetls_date_Val.setText(outputDateStr);
                                        //Rating
                                        switch (rating) {
                                            case "Need Improvement":
                                                emoji_image.setBackgroundResource(R.mipmap.improvementemojiselected);
                                                break;
                                            case "Okay":
                                                emoji_image.setBackgroundResource(R.mipmap.okayemojiselected);
                                                break;
                                            case "Good":
                                                emoji_image.setBackgroundResource(R.mipmap.goodemojiselected);
                                                break;
                                            case "Excellent":
                                                emoji_image.setBackgroundResource(R.mipmap.excellentemojiselected);
                                                break;
                                        }

                                        Glide.with(context)
                                                .load(storeImg)
                                                .fitCenter()
                                                .into(camera_imageView1);


                                        inspection_txtStoreCode.setText(inspdetls_ArrayList.get(i).getStoreCode());
                                        inspection_txtStoreName.setText(inspdetls_ArrayList.get(i).getStoreDesc());
                                        txt_comment_val.setText(inspdetls_ArrayList.get(i).getComments());

                                        // Fashion Quient
                                        if(inspdetls_ArrayList.get(i).getFashionQuot() == 1)
                                        {
                                            image_improvement_inspdetls_criteria_1.setVisibility(View.GONE);
                                            image_improvement_inspdetls_cri_1.setVisibility(View.VISIBLE);
                                            image_improvement_inspdetls_cri_1.setBackgroundResource(R.mipmap.improvementemojiselected);
                                            txt_improvement_inspdetls_cri_1.setText("Need Improvement");
                                            txt_improvement_inspdetls_cri_1.setTextSize(11f);
                                        }
                                        else if(inspdetls_ArrayList.get(i).getFashionQuot() == 2)
                                        {
                                            image_okay_inspdetls_criteria_1.setVisibility(View.GONE);
                                            image_okay_inspdetls_cri_1.setVisibility(View.VISIBLE);
                                            image_okay_inspdetls_cri_1.setBackgroundResource(R.mipmap.okayemojiselected);
                                            txt_okay_inspdetls_cri_1.setText("Okay");
                                            txt_okay_inspdetls_cri_1.setTextSize(11f);
                                        }
                                        else if(inspdetls_ArrayList.get(i).getFashionQuot() == 3)
                                        {
                                            image_good_inspdetls_criteria_1.setVisibility(View.GONE);
                                            image_good_inspdetls_cri_1.setVisibility(View.VISIBLE);
                                            image_good_inspdetls_cri_1.setBackgroundResource(R.mipmap.goodemojiselected);
                                            txt_good_inspdetls_cri_1.setText("Good");
                                            txt_good_inspdetls_cri_1.setTextSize(11f);
                                        }
                                        else if(inspdetls_ArrayList.get(i).getFashionQuot() == 4)
                                        {
                                            image_excellent_inspdetls_criteria_1.setVisibility(View.GONE);
                                            image_excellent_inspdetls_cri_1.setVisibility(View.VISIBLE);
                                            image_excellent_inspdetls_cri_1.setBackgroundResource(R.mipmap.excellentemojiselected);
                                            txt_excellent_inspdetls_cri_1.setText("Excellent");
                                            txt_excellent_inspdetls_cri_1.setTextSize(11f);
                                        }
                                        //merchDisplay
                                        if(inspdetls_ArrayList.get(i).getMerchDisplay() == 1)
                                        {
                                            image_improvement_inspdetls_criteria_2.setVisibility(View.GONE);
                                            image_improvement_inspdetls_cri_2.setVisibility(View.VISIBLE);
                                            image_improvement_inspdetls_cri_2.setBackgroundResource(R.mipmap.improvementemojiselected);
                                            txt_improvement_inspdetls_cri_2.setText("Need Improvement");
                                            txt_improvement_inspdetls_cri_2.setTextSize(11f);
                                        }
                                        else if(inspdetls_ArrayList.get(i).getMerchDisplay() == 2)
                                        {
                                            image_okay_inspdetls_criteria_2.setVisibility(View.GONE);
                                            image_okay_inspdetls_cri_2.setVisibility(View.VISIBLE);
                                            image_okay_inspdetls_cri_2.setBackgroundResource(R.mipmap.okayemojiselected);
                                            txt_okay_inspdetls_cri_2.setText("Okay");
                                            txt_okay_inspdetls_cri_2.setTextSize(11f);
                                        }
                                        else if(inspdetls_ArrayList.get(i).getMerchDisplay() == 3)
                                        {
                                            image_good_inspdetls_criteria_2.setVisibility(View.GONE);
                                            image_good_inspdetls_cri_2.setVisibility(View.VISIBLE);
                                            image_good_inspdetls_cri_2.setBackgroundResource(R.mipmap.goodemojiselected);
                                            txt_good_inspdetls_cri_2.setText("Good");
                                            txt_good_inspdetls_cri_2.setTextSize(11f);
                                        }
                                        else if(inspdetls_ArrayList.get(i).getMerchDisplay() == 4)
                                        {
                                            image_excellent_inspdetls_criteria_2.setVisibility(View.GONE);
                                            image_excellent_inspdetls_cri_2.setVisibility(View.VISIBLE);
                                            image_excellent_inspdetls_cri_2.setBackgroundResource(R.mipmap.excellentemojiselected);
                                            txt_excellent_inspdetls_cri_2.setText("Excellent");
                                            txt_excellent_inspdetls_cri_2.setTextSize(11f);
                                        }
                                        //Merchandise Presentation Display
                                        if(inspdetls_ArrayList.get(i).getMerchPresentationStd() == 1)
                                        {
                                            image_improvement_inspdetls_criteria_3.setVisibility(View.GONE);
                                            image_improvement_inspdetls_cri_3.setVisibility(View.VISIBLE);
                                            image_improvement_inspdetls_cri_3.setBackgroundResource(R.mipmap.improvementemojiselected);
                                            txt_improvement_inspdetls_cri_3.setText("Need Improvement");
                                            txt_improvement_inspdetls_cri_3.setTextSize(11f);
                                        }
                                        else if(inspdetls_ArrayList.get(i).getMerchPresentationStd() == 2)
                                        {
                                            image_okay_inspdetls_criteria_3.setVisibility(View.GONE);
                                            image_okay_inspdetls_cri_3.setVisibility(View.VISIBLE);
                                            image_okay_inspdetls_cri_3.setBackgroundResource(R.mipmap.okayemojiselected);
                                            txt_okay_inspdetls_cri_3.setText("Okay");
                                            txt_okay_inspdetls_cri_3.setTextSize(11f);
                                        }
                                        else if(inspdetls_ArrayList.get(i).getMerchPresentationStd() == 3)
                                        {
                                            image_good_inspdetls_criteria_3.setVisibility(View.GONE);
                                            image_good_inspdetls_cri_3.setVisibility(View.VISIBLE);
                                            image_good_inspdetls_cri_3.setBackgroundResource(R.mipmap.goodemojiselected);
                                            txt_good_inspdetls_cri_3.setText("Good");
                                            txt_good_inspdetls_cri_3.setTextSize(11f);
                                        }
                                        else if(inspdetls_ArrayList.get(i).getMerchPresentationStd() == 4)
                                        {
                                            image_excellent_inspdetls_criteria_3.setVisibility(View.GONE);
                                            image_excellent_inspdetls_cri_3.setVisibility(View.VISIBLE);
                                            image_excellent_inspdetls_cri_3.setBackgroundResource(R.mipmap.excellentemojiselected);
                                            txt_excellent_inspdetls_cri_3.setText("Excellent");
                                            txt_excellent_inspdetls_cri_3.setTextSize(11f);
                                        }
                                        //Suggestive Selling By Staff
                                        if(inspdetls_ArrayList.get(i).getSuggSellingByStaff() == 1)
                                        {
                                            image_improvement_inspdetls_criteria_4.setVisibility(View.GONE);
                                            image_improvement_inspdetls_cri_4.setVisibility(View.VISIBLE);
                                            image_improvement_inspdetls_cri_4.setBackgroundResource(R.mipmap.improvementemojiselected);
                                            txt_improvement_inspdetls_cri_4.setText("Need Improvement");
                                            txt_improvement_inspdetls_cri_4.setTextSize(11f);
                                        }
                                        else if(inspdetls_ArrayList.get(i).getSuggSellingByStaff() == 2)
                                        {
                                            image_okay_inspdetls_criteria_4.setVisibility(View.GONE);
                                            image_okay_inspdetls_cri_4.setVisibility(View.VISIBLE);
                                            image_okay_inspdetls_cri_4.setBackgroundResource(R.mipmap.okayemojiselected);
                                            txt_okay_inspdetls_cri_4.setText("Okay");
                                            txt_okay_inspdetls_cri_4.setTextSize(11f);
                                        }
                                        else if(inspdetls_ArrayList.get(i).getSuggSellingByStaff() == 3)
                                        {
                                            image_good_inspdetls_criteria_4.setVisibility(View.GONE);
                                            image_good_inspdetls_cri_4.setVisibility(View.VISIBLE);
                                            image_good_inspdetls_cri_4.setBackgroundResource(R.mipmap.goodemojiselected);
                                            txt_good_inspdetls_cri_4.setText("Good");
                                            txt_good_inspdetls_cri_4.setTextSize(11f);
                                        }
                                        else if(inspdetls_ArrayList.get(i).getSuggSellingByStaff() == 4)
                                        {
                                            image_excellent_inspdetls_criteria_4.setVisibility(View.GONE);
                                            image_excellent_inspdetls_cri_4.setVisibility(View.VISIBLE);
                                            image_excellent_inspdetls_cri_4.setBackgroundResource(R.mipmap.excellentemojiselected);
                                            txt_excellent_inspdetls_cri_4.setText("Excellent");
                                            txt_excellent_inspdetls_cri_4.setTextSize(11f);
                                        }
                                        //Overall Cleanliness
                                        if(inspdetls_ArrayList.get(i).getOverallCleanliness() == 1)
                                        {
                                            image_improvement_inspdetls_criteria_5.setVisibility(View.GONE);
                                            image_improvement_inspdetls_cri_5.setVisibility(View.VISIBLE);
                                            image_improvement_inspdetls_cri_5.setBackgroundResource(R.mipmap.improvementemojiselected);
                                            txt_improvement_inspdetls_cri_5.setText("Need Improvement");
                                            txt_improvement_inspdetls_cri_5.setTextSize(11f);
                                        }
                                        else if(inspdetls_ArrayList.get(i).getOverallCleanliness() == 2)
                                        {
                                            image_okay_inspdetls_criteria_5.setVisibility(View.GONE);
                                            image_okay_inspdetls_cri_5.setVisibility(View.VISIBLE);
                                            image_okay_inspdetls_cri_5.setBackgroundResource(R.mipmap.okayemojiselected);
                                            txt_okay_inspdetls_cri_5.setText("Okay");
                                            txt_okay_inspdetls_cri_5.setTextSize(11f);
                                        }
                                        else if(inspdetls_ArrayList.get(i).getOverallCleanliness() == 3)
                                        {
                                            image_good_inspdetls_criteria_5.setVisibility(View.GONE);
                                            image_good_inspdetls_cri_5.setVisibility(View.VISIBLE);
                                            image_good_inspdetls_cri_5.setBackgroundResource(R.mipmap.goodemojiselected);
                                            txt_good_inspdetls_cri_5.setText("Good");
                                            txt_good_inspdetls_cri_5.setTextSize(11f);
                                        }
                                        else if(inspdetls_ArrayList.get(i).getOverallCleanliness() == 4)
                                        {
                                            image_excellent_inspdetls_criteria_5.setVisibility(View.GONE);
                                            image_excellent_inspdetls_cri_5.setVisibility(View.VISIBLE);
                                            image_excellent_inspdetls_cri_5.setBackgroundResource(R.mipmap.excellentemojiselected);
                                            txt_excellent_inspdetls_cri_5.setText("Excellent");
                                            txt_excellent_inspdetls_cri_5.setTextSize(11f);
                                        }
                                        //Signage
                                        if(inspdetls_ArrayList.get(i).getSignage() == 1)
                                        {
                                            image_improvement_inspdetls_criteria_6.setVisibility(View.GONE);
                                            image_improvement_inspdetls_cri_6.setVisibility(View.VISIBLE);
                                            image_improvement_inspdetls_cri_6.setBackgroundResource(R.mipmap.improvementemojiselected);
                                            txt_improvement_inspdetls_cri_6.setText("Need Improvement");
                                            txt_improvement_inspdetls_cri_6.setTextSize(11f);
                                        }
                                        else if(inspdetls_ArrayList.get(i).getSignage() == 2)
                                        {
                                            image_okay_inspdetls_criteria_6.setVisibility(View.GONE);
                                            image_okay_inspdetls_cri_6.setVisibility(View.VISIBLE);
                                            image_okay_inspdetls_cri_6.setBackgroundResource(R.mipmap.okayemojiselected);
                                            txt_okay_inspdetls_cri_6.setText("Okay");
                                            txt_okay_inspdetls_cri_6.setTextSize(11f);
                                        }
                                        else if(inspdetls_ArrayList.get(i).getSignage() == 3)
                                        {
                                            image_good_inspdetls_criteria_6.setVisibility(View.GONE);
                                            image_good_inspdetls_cri_6.setVisibility(View.VISIBLE);
                                            image_good_inspdetls_cri_6.setBackgroundResource(R.mipmap.goodemojiselected);
                                            txt_good_inspdetls_cri_6.setText("Good");
                                            txt_good_inspdetls_cri_6.setTextSize(11f);
                                        }
                                        else if(inspdetls_ArrayList.get(i).getSignage() == 4)
                                        {
                                            image_excellent_inspdetls_criteria_6.setVisibility(View.GONE);
                                            image_excellent_inspdetls_cri_6.setVisibility(View.VISIBLE);
                                            image_excellent_inspdetls_cri_6.setBackgroundResource(R.mipmap.excellentemojiselected);
                                            txt_excellent_inspdetls_cri_6.setText("Excellent");
                                            txt_excellent_inspdetls_cri_6.setTextSize(11f);
                                        }
                                        //MPM Execution
                                        if(inspdetls_ArrayList.get(i).getMpmExecution() == 1)
                                        {
                                            image_improvement_inspdetls_criteria_7.setVisibility(View.GONE);
                                            image_improvement_inspdetls_cri_7.setVisibility(View.VISIBLE);
                                            image_improvement_inspdetls_cri_7.setBackgroundResource(R.mipmap.improvementemojiselected);
                                            txt_improvement_inspdetls_cri_7.setText("Need Improvement");
                                            txt_improvement_inspdetls_cri_7.setTextSize(11f);
                                        }
                                        else if(inspdetls_ArrayList.get(i).getMpmExecution() == 2)
                                        {
                                            image_okay_inspdetls_criteria_7.setVisibility(View.GONE);
                                            image_okay_inspdetls_cri_7.setVisibility(View.VISIBLE);
                                            image_okay_inspdetls_cri_7.setBackgroundResource(R.mipmap.okayemojiselected);
                                            txt_okay_inspdetls_cri_7.setText("Okay");
                                            txt_okay_inspdetls_cri_7.setTextSize(11f);
                                        }
                                        else if(inspdetls_ArrayList.get(i).getMpmExecution() == 3)
                                        {
                                            image_good_inspdetls_criteria_7.setVisibility(View.GONE);
                                            image_good_inspdetls_cri_7.setVisibility(View.VISIBLE);
                                            image_good_inspdetls_cri_7.setBackgroundResource(R.mipmap.goodemojiselected);
                                            txt_good_inspdetls_cri_7.setText("Good");
                                            txt_good_inspdetls_cri_7.setTextSize(11f);
                                        }
                                        else if(inspdetls_ArrayList.get(i).getMpmExecution() == 4)
                                        {
                                            image_excellent_inspdetls_criteria_7.setVisibility(View.GONE);
                                            image_excellent_inspdetls_cri_7.setVisibility(View.VISIBLE);
                                            image_excellent_inspdetls_cri_7.setBackgroundResource(R.mipmap.excellentemojiselected);
                                            txt_excellent_inspdetls_cri_7.setText("Excellent");
                                            txt_excellent_inspdetls_cri_7.setTextSize(11f);
                                        }
                                        //Window and cluster Mannequins Displays
                                        if(inspdetls_ArrayList.get(i).getWinClusterMannequinsDisp() == 1)
                                        {
                                            image_improvement_inspdetls_criteria_8.setVisibility(View.GONE);
                                            image_improvement_inspdetls_cri_8.setVisibility(View.VISIBLE);
                                            image_improvement_inspdetls_cri_8.setBackgroundResource(R.mipmap.improvementemojiselected);
                                            txt_improvement_inspdetls_cri_8.setText("Need Improvement");
                                            txt_improvement_inspdetls_cri_8.setTextSize(11f);
                                        }
                                        else if(inspdetls_ArrayList.get(i).getWinClusterMannequinsDisp() == 2)
                                        {
                                            image_okay_inspdetls_criteria_8.setVisibility(View.GONE);
                                            image_okay_inspdetls_cri_8.setVisibility(View.VISIBLE);
                                            image_okay_inspdetls_cri_8.setBackgroundResource(R.mipmap.okayemojiselected);
                                            txt_okay_inspdetls_cri_8.setText("Okay");
                                            txt_okay_inspdetls_cri_8.setTextSize(11f);
                                        }
                                        else if(inspdetls_ArrayList.get(i).getWinClusterMannequinsDisp() == 3)
                                        {
                                            image_good_inspdetls_criteria_8.setVisibility(View.GONE);
                                            image_good_inspdetls_cri_8.setVisibility(View.VISIBLE);
                                            image_good_inspdetls_cri_8.setBackgroundResource(R.mipmap.goodemojiselected);
                                            txt_good_inspdetls_cri_8.setText("Good");
                                            txt_good_inspdetls_cri_8.setTextSize(11f);
                                        }
                                        else if(inspdetls_ArrayList.get(i).getWinClusterMannequinsDisp() == 4)
                                        {
                                            image_excellent_inspdetls_criteria_8.setVisibility(View.GONE);
                                            image_excellent_inspdetls_cri_8.setVisibility(View.VISIBLE);
                                            image_excellent_inspdetls_cri_8.setBackgroundResource(R.mipmap.excellentemojiselected);
                                            txt_excellent_inspdetls_cri_8.setText("Excellent");
                                            txt_excellent_inspdetls_cri_8.setTextSize(11f);
                                        }
                                    }
                                    Reusable_Functions.hDialog();
                                }
                            }
                            catch (Exception e)
                            {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "data failed...." + e.toString(), Toast.LENGTH_SHORT).show();
                                Reusable_Functions.hDialog();
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "server not responding..", Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json");
                    params.put("Authorization", "Bearer " + bearertoken);
                    return params;
                }
            };
            int socketTimeout = 60000;//5 seconds
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            postRequest.setRetryPolicy(policy);
            queue.add(postRequest);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
