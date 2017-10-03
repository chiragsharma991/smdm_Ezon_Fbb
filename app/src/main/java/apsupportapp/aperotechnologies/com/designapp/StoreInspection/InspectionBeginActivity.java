package apsupportapp.aperotechnologies.com.designapp.StoreInspection;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;

import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;


import apsupportapp.aperotechnologies.com.designapp.DashboardSnap.SnapDashboardActivity;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;

import static apsupportapp.aperotechnologies.com.designapp.HorlyAnalysis.Option_Fragment.view;

/**
 * Created by pamrutkar on 11/04/17.
 */
public class InspectionBeginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 15; //1
    public static final int MY_PERMISSIONS_REQUEST_R = 30; //2
    public static final int MY_PERMISSIONS_REQUEST_RWFRMCAM = 60; //3
    private Bitmap bitmap;
    private int ratting_size[]=new int[8];

    // Emoji Declaration
    ImageView image_improvement_criteria_1, image_okay_criteria_1, image_good_criteria_1, image_excellent_criteria_1;
    ImageView image_improvement_criteria_2, image_okay_criteria_2, image_good_criteria_2, image_excellent_criteria_2;
    ImageView image_improvement_criteria_3, image_okay_criteria_3, image_good_criteria_3, image_excellent_criteria_3;
    ImageView image_improvement_criteria_4, image_okay_criteria_4, image_good_criteria_4, image_excellent_criteria_4;
    ImageView image_improvement_criteria_5, image_okay_criteria_5, image_good_criteria_5, image_excellent_criteria_5;
    ImageView image_improvement_criteria_6, image_okay_criteria_6, image_good_criteria_6, image_excellent_criteria_6;
    ImageView image_improvement_criteria_7, image_okay_criteria_7, image_good_criteria_7, image_excellent_criteria_7;
    ImageView image_improvement_criteria_8, image_okay_criteria_8, image_good_criteria_8, image_excellent_criteria_8;
    ImageView image_camera, image_upload ,emoji_image;

    ImageView image_improvement_cri_1,image_okay_cri_1,image_good_cri_1,image_excellent_cri_1;
    ImageView image_improvement_cri_2,image_okay_cri_2,image_good_cri_2,image_excellent_cri_2;
    ImageView image_improvement_cri_3,image_okay_cri_3,image_good_cri_3,image_excellent_cri_3;
    ImageView image_improvement_cri_4,image_okay_cri_4,image_good_cri_4,image_excellent_cri_4;
    ImageView image_improvement_cri_5,image_okay_cri_5,image_good_cri_5,image_excellent_cri_5;
    ImageView image_improvement_cri_6,image_okay_cri_6,image_good_cri_6,image_excellent_cri_6;
    ImageView image_improvement_cri_7,image_okay_cri_7,image_good_cri_7,image_excellent_cri_7;
    ImageView image_improvement_cri_8,image_okay_cri_8,image_good_cri_8,image_excellent_cri_8;

    // Emoji Text Declaration
    TextView txt_improvement_criteria_1, txt_okay_criteria_1, txt_good_criteria_1, txt_excellent_criteria_1;
    TextView txt_improvement_criteria_2, txt_okay_criteria_2, txt_good_criteria_2, txt_excellent_criteria_2;
    TextView txt_improvement_criteria_3, txt_okay_criteria_3, txt_good_criteria_3, txt_excellent_criteria_3;
    TextView txt_improvement_criteria_4, txt_okay_criteria_4, txt_good_criteria_4, txt_excellent_criteria_4;
    TextView txt_improvement_criteria_5, txt_okay_criteria_5, txt_good_criteria_5, txt_excellent_criteria_5;
    TextView txt_improvement_criteria_6, txt_okay_criteria_6, txt_good_criteria_6, txt_excellent_criteria_6;
    TextView txt_improvement_criteria_7, txt_okay_criteria_7, txt_good_criteria_7, txt_excellent_criteria_7;
    TextView txt_improvement_criteria_8, txt_okay_criteria_8, txt_good_criteria_8, txt_excellent_criteria_8;

    EditText et_inspected_by, et_comment;
    Button btn_insp_submit,btn_insp_reset;
    private RelativeLayout rel_cam_image;
    Uri selectedImage;
    private TextView txt_insp_date_Val,txt_overall_ratings;
    private String picturePath;
    private RelativeLayout inspection_btnback;
    private String userId;
    private String bearertoken;
    private RequestQueue queue;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private String insp_imagePath, storeCode, store_Code;
    String strDate;
    Context context;
    private double overallRating = 0.0;
    private int fashionQuot, merchDisplay, merchPresentationStd, suggSellingByStaff, overallCleanliness, signage, winClusterMannequinsDisp, mpmExecution;
    private double sumofRatting, cal_result;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_begin);
        getSupportActionBar().hide();
        context = this;
        initialise();
        getCurrentDate(view);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();

    }
    //field means -0,1,2..
    //inx means= selected value
    public double calculation(int x,int field){
        int div_by=0;
        sumofRatting=0;
        ratting_size[field]=x;

        //sum of all values
        for (int i = 0; i <8 ; i++) {
            if(ratting_size[i]!=0){
                div_by+=1;
            }
            sumofRatting+=ratting_size[i];
        }

        //sum divide by ..
        cal_result = sumofRatting/div_by;
        //emoji image set
        if(Math.round(cal_result) < 2)
        {
            emoji_image.setBackgroundResource(R.mipmap.improvementemojiselected);
        }
        else  if(Math.round(cal_result)< 3)
        {
            emoji_image.setBackgroundResource(R.mipmap.okayemojiselected);

        }
        else  if(Math.round(cal_result) < 4)
        {
            emoji_image.setBackgroundResource(R.mipmap.goodemojiselected);
        }
        else  if(Math.round(cal_result) >= 4)
        {
            emoji_image.setBackgroundResource(R.mipmap.excellentemojiselected);
        }

        return cal_result;
    }

    public void getCurrentDate(View view) {
        Calendar calendar = Calendar.getInstance(); //dd / MMM /yyyy
        SimpleDateFormat mdformat = new SimpleDateFormat("dd-MMM-yyyy");
        strDate = mdformat.format(calendar.getTime());
        txt_insp_date_Val.setText(strDate);
    }

    private void initialise()
    {
        if(getIntent().getExtras().getString("storeCode") != null )
        {
            storeCode = getIntent().getExtras().getString("storeCode");
            store_Code = storeCode.substring(0,4);
        }
        image_improvement_cri_1 = (ImageView)findViewById(R.id.image_improvement_cri_1);
        image_improvement_cri_1.setVisibility(View.GONE);
        image_okay_cri_1 = (ImageView)findViewById(R.id.image_okay_cri_1);
        image_okay_cri_1.setVisibility(View.GONE);
        image_good_cri_1 = (ImageView)findViewById(R.id.image_good_cri_1);
        image_good_cri_1.setVisibility(View.GONE);
        image_excellent_cri_1 = (ImageView)findViewById(R.id.image_excellent_cri_1);
        image_excellent_cri_1.setVisibility(View.GONE);
        //for insp 2
        image_improvement_cri_2 = (ImageView)findViewById(R.id.image_improvement_cri_2);
        image_improvement_cri_2.setVisibility(View.GONE);
        image_okay_cri_2 = (ImageView)findViewById(R.id.image_okay_cri_2);
        image_okay_cri_2.setVisibility(View.GONE);
        image_good_cri_2 = (ImageView)findViewById(R.id.image_good_cri_2);
        image_good_cri_2.setVisibility(View.GONE);
        image_excellent_cri_2 = (ImageView)findViewById(R.id.image_excellent_cri_2);
        image_excellent_cri_2.setVisibility(View.GONE);
        //for insp 3
        image_improvement_cri_3 = (ImageView)findViewById(R.id.image_improvement_cri_3);
        image_improvement_cri_3.setVisibility(View.GONE);
        image_okay_cri_3 = (ImageView)findViewById(R.id.image_okay_cri_3);
        image_okay_cri_3.setVisibility(View.GONE);
        image_good_cri_3 = (ImageView)findViewById(R.id.image_good_cri_3);
        image_good_cri_3.setVisibility(View.GONE);
        image_excellent_cri_3 = (ImageView)findViewById(R.id.image_excellent_cri_3);
        image_excellent_cri_3.setVisibility(View.GONE);
        //for insp 4
        image_improvement_cri_4 = (ImageView)findViewById(R.id.image_improvement_cri_4);
        image_improvement_cri_4.setVisibility(View.GONE);
        image_okay_cri_4 = (ImageView)findViewById(R.id.image_okay_cri_4);
        image_okay_cri_4.setVisibility(View.GONE);
        image_good_cri_4 = (ImageView)findViewById(R.id.image_good_cri_4);
        image_good_cri_4.setVisibility(View.GONE);
        image_excellent_cri_4 = (ImageView)findViewById(R.id.image_excellent_cri_4);
        image_excellent_cri_4.setVisibility(View.GONE);
        //for insp 5
        image_improvement_cri_5 = (ImageView)findViewById(R.id.image_improvement_cri_5);
        image_improvement_cri_5.setVisibility(View.GONE);
        image_okay_cri_5 = (ImageView)findViewById(R.id.image_okay_cri_5);
        image_okay_cri_5.setVisibility(View.GONE);
        image_good_cri_5 = (ImageView)findViewById(R.id.image_good_cri_5);
        image_good_cri_5.setVisibility(View.GONE);
        image_excellent_cri_5 = (ImageView)findViewById(R.id.image_excellent_cri_5);
        image_excellent_cri_5.setVisibility(View.GONE);
        //for insp 6
        image_improvement_cri_6 = (ImageView)findViewById(R.id.image_improvement_cri_6);
        image_improvement_cri_6.setVisibility(View.GONE);
        image_okay_cri_6 = (ImageView)findViewById(R.id.image_okay_cri_6);
        image_okay_cri_6.setVisibility(View.GONE);
        image_good_cri_6 = (ImageView)findViewById(R.id.image_good_cri_6);
        image_good_cri_6.setVisibility(View.GONE);
        image_excellent_cri_6 = (ImageView)findViewById(R.id.image_excellent_cri_6);
        image_excellent_cri_6.setVisibility(View.GONE);
        //for insp 7
        image_improvement_cri_7 = (ImageView)findViewById(R.id.image_improvement_cri_7);
        image_improvement_cri_7.setVisibility(View.GONE);
        image_okay_cri_7 = (ImageView)findViewById(R.id.image_okay_cri_7);
        image_okay_cri_7.setVisibility(View.GONE);
        image_good_cri_7 = (ImageView)findViewById(R.id.image_good_cri_7);
        image_good_cri_7.setVisibility(View.GONE);
        image_excellent_cri_7 = (ImageView)findViewById(R.id.image_excellent_cri_7);
        image_excellent_cri_7.setVisibility(View.GONE);
        //for insp 8
        image_improvement_cri_8 = (ImageView)findViewById(R.id.image_improvement_cri_8);
        image_improvement_cri_8.setVisibility(View.GONE);
        image_okay_cri_8 = (ImageView)findViewById(R.id.image_okay_cri_8);
        image_okay_cri_8.setVisibility(View.GONE);
        image_good_cri_8 = (ImageView)findViewById(R.id.image_good_cri_8);
        image_good_cri_8.setVisibility(View.GONE);
        image_excellent_cri_8 = (ImageView)findViewById(R.id.image_excellent_cri_8);
        image_excellent_cri_8.setVisibility(View.GONE);
        //Improvement emoji image
        image_improvement_criteria_1 = (ImageView) findViewById(R.id.image_improvement_criteria_1);
        image_improvement_criteria_2 = (ImageView) findViewById(R.id.image_improvement_criteria_2);
        image_improvement_criteria_3 = (ImageView) findViewById(R.id.image_improvement_criteria_3);
        image_improvement_criteria_4 = (ImageView) findViewById(R.id.image_improvement_criteria_4);
        image_improvement_criteria_5 = (ImageView) findViewById(R.id.image_improvement_criteria_5);
        image_improvement_criteria_6 = (ImageView) findViewById(R.id.image_improvement_criteria_6);
        image_improvement_criteria_7 = (ImageView) findViewById(R.id.image_improvement_criteria_7);
        image_improvement_criteria_8 = (ImageView) findViewById(R.id.image_improvement_criteria_8);

        //Okay emoji image
        image_okay_criteria_1 = (ImageView) findViewById(R.id.image_okay_criteria_1);
        image_okay_criteria_2 = (ImageView) findViewById(R.id.image_okay_criteria_2);
        image_okay_criteria_3 = (ImageView) findViewById(R.id.image_okay_criteria_3);
        image_okay_criteria_4 = (ImageView) findViewById(R.id.image_okay_criteria_4);
        image_okay_criteria_5 = (ImageView) findViewById(R.id.image_okay_criteria_5);
        image_okay_criteria_6 = (ImageView) findViewById(R.id.image_okay_criteria_6);
        image_okay_criteria_7 = (ImageView) findViewById(R.id.image_okay_criteria_7);
        image_okay_criteria_8 = (ImageView) findViewById(R.id.image_okay_criteria_8);

        //Good Emoji image
        image_good_criteria_1 = (ImageView) findViewById(R.id.image_good_criteria_1);
        image_good_criteria_2 = (ImageView) findViewById(R.id.image_good_criteria_2);
        image_good_criteria_3 = (ImageView) findViewById(R.id.image_good_criteria_3);
        image_good_criteria_4 = (ImageView) findViewById(R.id.image_good_criteria_4);
        image_good_criteria_5 = (ImageView) findViewById(R.id.image_good_criteria_5);
        image_good_criteria_6 = (ImageView) findViewById(R.id.image_good_criteria_6);
        image_good_criteria_7 = (ImageView) findViewById(R.id.image_good_criteria_7);
        image_good_criteria_8 = (ImageView) findViewById(R.id.image_good_criteria_8);

        //excellent emoji image
        image_excellent_criteria_1 = (ImageView) findViewById(R.id.image_excellent_criteria_1);
        image_excellent_criteria_2 = (ImageView) findViewById(R.id.image_excellent_criteria_2);
        image_excellent_criteria_3 = (ImageView) findViewById(R.id.image_excellent_criteria_3);
        image_excellent_criteria_4 = (ImageView) findViewById(R.id.image_excellent_criteria_4);
        image_excellent_criteria_5 = (ImageView) findViewById(R.id.image_excellent_criteria_5);
        image_excellent_criteria_6 = (ImageView) findViewById(R.id.image_excellent_criteria_6);
        image_excellent_criteria_7 = (ImageView) findViewById(R.id.image_excellent_criteria_7);
        image_excellent_criteria_8 = (ImageView) findViewById(R.id.image_excellent_criteria_8);

        //Improvement emoji text
        txt_improvement_criteria_1 = (TextView) findViewById(R.id.txt_improvement_criteria_1);
        txt_improvement_criteria_2 = (TextView) findViewById(R.id.txt_improvement_criteria_2);
        txt_improvement_criteria_3 = (TextView) findViewById(R.id.txt_improvement_criteria_3);
        txt_improvement_criteria_4 = (TextView) findViewById(R.id.txt_improvement_criteria_4);
        txt_improvement_criteria_5 = (TextView) findViewById(R.id.txt_improvement_criteria_5);
        txt_improvement_criteria_6 = (TextView) findViewById(R.id.txt_improvement_criteria_6);
        txt_improvement_criteria_7 = (TextView) findViewById(R.id.txt_improvement_criteria_7);
        txt_improvement_criteria_8 = (TextView) findViewById(R.id.txt_improvement_criteria_8);

        //Okay emoji text
        txt_okay_criteria_1 = (TextView) findViewById(R.id.txt_okay_criteria_1);
        txt_okay_criteria_2 = (TextView) findViewById(R.id.txt_okay_criteria_2);
        txt_okay_criteria_3 = (TextView) findViewById(R.id.txt_okay_criteria_3);
        txt_okay_criteria_4 = (TextView) findViewById(R.id.txt_okay_criteria_4);
        txt_okay_criteria_5 = (TextView) findViewById(R.id.txt_okay_criteria_5);
        txt_okay_criteria_6 = (TextView) findViewById(R.id.txt_okay_criteria_6);
        txt_okay_criteria_7 = (TextView) findViewById(R.id.txt_okay_criteria_7);
        txt_okay_criteria_8 = (TextView) findViewById(R.id.txt_okay_criteria_8);

        //Good Emoji text
        txt_good_criteria_1 = (TextView) findViewById(R.id.txt_good_criteria_1);
        txt_good_criteria_2 = (TextView) findViewById(R.id.txt_good_criteria_2);
        txt_good_criteria_3 = (TextView) findViewById(R.id.txt_good_criteria_3);
        txt_good_criteria_4 = (TextView) findViewById(R.id.txt_good_criteria_4);
        txt_good_criteria_5 = (TextView) findViewById(R.id.txt_good_criteria_5);
        txt_good_criteria_6 = (TextView) findViewById(R.id.txt_good_criteria_6);
        txt_good_criteria_7 = (TextView) findViewById(R.id.txt_good_criteria_7);
        txt_good_criteria_8 = (TextView) findViewById(R.id.txt_good_criteria_8);

        //excellent emoji text
        txt_excellent_criteria_1 = (TextView) findViewById(R.id.txt_excellent_criteria_1);
        txt_excellent_criteria_2 = (TextView) findViewById(R.id.txt_excellent_criteria_2);
        txt_excellent_criteria_3 = (TextView) findViewById(R.id.txt_excellent_criteria_3);
        txt_excellent_criteria_4 = (TextView) findViewById(R.id.txt_excellent_criteria_4);
        txt_excellent_criteria_5 = (TextView) findViewById(R.id.txt_excellent_criteria_5);
        txt_excellent_criteria_6 = (TextView) findViewById(R.id.txt_excellent_criteria_6);
        txt_excellent_criteria_7 = (TextView) findViewById(R.id.txt_excellent_criteria_7);
        txt_excellent_criteria_8 = (TextView) findViewById(R.id.txt_excellent_criteria_8);

        for (int i = 0; i < 8 ; i++) {
            ratting_size[i]=0;

        }

        //Edittext
        et_inspected_by = (EditText) findViewById(R.id.et_inspected_by);
        et_comment = (EditText) findViewById(R.id.et_comment);
        et_comment.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        rel_cam_image = (RelativeLayout) findViewById(R.id.rel_cam_image);
        image_camera = (ImageView) findViewById(R.id.camera_imageView);
        image_upload = (ImageView) findViewById(R.id.camera_imageView1);
        emoji_image = (ImageView)findViewById(R.id.emoji_image);
        image_upload.setVisibility(View.INVISIBLE);
        txt_insp_date_Val = (TextView) findViewById(R.id.txt_insp_date_Val);
        txt_overall_ratings = (TextView)findViewById(R.id.txt_overall_ratings);
        inspection_btnback = (RelativeLayout) findViewById(R.id.inspection_btnback);

        btn_insp_submit = (Button) findViewById(R.id.btn_insp_submit);
        btn_insp_reset = (Button)findViewById(R.id.btn_insp_reset);

        image_improvement_criteria_1.setOnClickListener(this);
        image_improvement_criteria_2.setOnClickListener(this);
        image_improvement_criteria_3.setOnClickListener(this);
        image_improvement_criteria_4.setOnClickListener(this);
        image_improvement_criteria_5.setOnClickListener(this);
        image_improvement_criteria_6.setOnClickListener(this);
        image_improvement_criteria_7.setOnClickListener(this);
        image_improvement_criteria_8.setOnClickListener(this);

        image_okay_criteria_1.setOnClickListener(this);
        image_okay_criteria_2.setOnClickListener(this);
        image_okay_criteria_3.setOnClickListener(this);
        image_okay_criteria_4.setOnClickListener(this);
        image_okay_criteria_5.setOnClickListener(this);
        image_okay_criteria_6.setOnClickListener(this);
        image_okay_criteria_7.setOnClickListener(this);
        image_okay_criteria_8.setOnClickListener(this);

        image_good_criteria_1.setOnClickListener(this);
        image_good_criteria_2.setOnClickListener(this);
        image_good_criteria_3.setOnClickListener(this);
        image_good_criteria_4.setOnClickListener(this);
        image_good_criteria_5.setOnClickListener(this);
        image_good_criteria_6.setOnClickListener(this);
        image_good_criteria_7.setOnClickListener(this);
        image_good_criteria_8.setOnClickListener(this);

        image_excellent_criteria_1.setOnClickListener(this);
        image_excellent_criteria_2.setOnClickListener(this);
        image_excellent_criteria_3.setOnClickListener(this);
        image_excellent_criteria_4.setOnClickListener(this);
        image_excellent_criteria_5.setOnClickListener(this);
        image_excellent_criteria_6.setOnClickListener(this);
        image_excellent_criteria_7.setOnClickListener(this);
        image_excellent_criteria_8.setOnClickListener(this);
        image_camera.setOnClickListener(this);
        inspection_btnback.setOnClickListener(this);
        btn_insp_submit.setOnClickListener(this);
        btn_insp_reset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_improvement_criteria_1:
                if (image_improvement_criteria_1.isClickable())
                {
                    image_good_cri_1.setVisibility(View.GONE);
                    image_excellent_cri_1.setVisibility(View.GONE);
                    image_okay_cri_1.setVisibility(View.GONE);
                    image_improvement_cri_1.setBackgroundResource(R.mipmap.improvementemojiselected);
                    image_improvement_cri_1.setVisibility(View.VISIBLE);
                    image_improvement_criteria_1.setVisibility(View.GONE);
                    txt_improvement_criteria_1.setText("Need Improvement");

                    image_excellent_criteria_1.setVisibility(View.VISIBLE);
                    image_good_criteria_1.setVisibility(View.VISIBLE);
                    image_okay_criteria_1.setVisibility(View.VISIBLE);
                    image_good_criteria_1.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_okay_criteria_1.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_excellent_criteria_1.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    txt_good_criteria_1.setText("");
                    txt_excellent_criteria_1.setText("");
                    txt_okay_criteria_1.setText("");
                    fashionQuot = 1;
                    calculation(fashionQuot,0);


                } else {

                    image_improvement_criteria_1.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_improvement_criteria_1.setVisibility(View.VISIBLE);
                    image_improvement_cri_1.setVisibility(View.GONE);
                    txt_improvement_criteria_1.setText("");
                }
                break;

            case R.id.image_improvement_criteria_2:
                if (image_improvement_criteria_2.isClickable()) {
                    image_good_cri_2.setVisibility(View.GONE);
                    image_excellent_cri_2.setVisibility(View.GONE);
                    image_okay_cri_2.setVisibility(View.GONE);
                    image_improvement_cri_2.setBackgroundResource(R.mipmap.improvementemojiselected);
                    image_improvement_cri_2.setVisibility(View.VISIBLE);
                    image_improvement_criteria_2.setVisibility(View.GONE);
                    txt_improvement_criteria_2.setText("Need Improvement");

                    image_excellent_criteria_2.setVisibility(View.VISIBLE);
                    image_good_criteria_2.setVisibility(View.VISIBLE);
                    image_okay_criteria_2.setVisibility(View.VISIBLE);
                    image_good_criteria_2.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_okay_criteria_2.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_excellent_criteria_2.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    txt_good_criteria_2.setText("");
                    txt_excellent_criteria_2.setText("");
                    txt_okay_criteria_2.setText("");
                    merchDisplay = 1;
                    calculation(merchDisplay,1);



                } else {
                    image_improvement_criteria_2.setVisibility(View.VISIBLE);
                    image_improvement_cri_2.setVisibility(View.GONE);
                    image_improvement_criteria_2.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    txt_improvement_criteria_2.setText("");
                }
                break;

            case R.id.image_improvement_criteria_3:
                if (image_improvement_criteria_3.isClickable())
                {
                    image_good_cri_3.setVisibility(View.GONE);
                    image_excellent_cri_3.setVisibility(View.GONE);
                    image_okay_cri_3.setVisibility(View.GONE);
                    image_improvement_cri_3.setBackgroundResource(R.mipmap.improvementemojiselected);
                    image_improvement_cri_3.setVisibility(View.VISIBLE);
                    image_improvement_criteria_3.setVisibility(View.GONE);
                    txt_improvement_criteria_3.setText("Need Improvement");
                    image_excellent_criteria_3.setVisibility(View.VISIBLE);
                    image_good_criteria_3.setVisibility(View.VISIBLE);
                    image_okay_criteria_3.setVisibility(View.VISIBLE);
                    image_good_criteria_3.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_okay_criteria_3.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_excellent_criteria_3.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    txt_good_criteria_3.setText("");
                    txt_excellent_criteria_3.setText("");
                    txt_okay_criteria_3.setText("");
                    merchPresentationStd = 1;
                    ratting_size[2]=merchPresentationStd;
                    calculation(merchPresentationStd,2);



                } else {
                    image_improvement_criteria_3.setVisibility(View.VISIBLE);
                    image_improvement_cri_3.setVisibility(View.GONE);
                    image_improvement_criteria_3.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    txt_improvement_criteria_3.setText("");
                }
                break;


            case R.id.image_improvement_criteria_4:

                if (image_improvement_criteria_4.isClickable()) {

                    image_good_cri_4.setVisibility(View.GONE);
                    image_excellent_cri_4.setVisibility(View.GONE);
                    image_okay_cri_4.setVisibility(View.GONE);
                    image_improvement_cri_4.setBackgroundResource(R.mipmap.improvementemojiselected);
                    image_improvement_cri_4.setVisibility(View.VISIBLE);
                    image_improvement_criteria_4.setVisibility(View.GONE);
                    txt_improvement_criteria_4.setText("Need Improvement");
                    image_excellent_criteria_4.setVisibility(View.VISIBLE);
                    image_good_criteria_4.setVisibility(View.VISIBLE);
                    image_okay_criteria_4.setVisibility(View.VISIBLE);
                    image_good_criteria_4.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_okay_criteria_4.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_excellent_criteria_4.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    txt_good_criteria_4.setText("");
                    txt_excellent_criteria_4.setText("");
                    txt_okay_criteria_4.setText("");

                    suggSellingByStaff = 1;
                    ratting_size[3] = suggSellingByStaff;
                    calculation(suggSellingByStaff,3);

                } else {
                      image_improvement_criteria_4.setVisibility(View.VISIBLE);
                    image_improvement_cri_4.setVisibility(View.GONE);
                    image_improvement_criteria_4.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    txt_improvement_criteria_4.setText("");
                }

                break;

            case R.id.image_improvement_criteria_5:

                if (image_improvement_criteria_5.isClickable()) {

                    image_good_cri_5.setVisibility(View.GONE);
                    image_excellent_cri_5.setVisibility(View.GONE);
                    image_okay_cri_5.setVisibility(View.GONE);
                    image_improvement_cri_5.setBackgroundResource(R.mipmap.improvementemojiselected);
                    image_improvement_cri_5.setVisibility(View.VISIBLE);
                    image_improvement_criteria_5.setVisibility(View.GONE);
                    txt_improvement_criteria_5.setText("Need Improvement");
                    image_excellent_criteria_5.setVisibility(View.VISIBLE);
                    image_good_criteria_5.setVisibility(View.VISIBLE);
                    image_okay_criteria_5.setVisibility(View.VISIBLE);
                    image_good_criteria_5.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_okay_criteria_5.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_excellent_criteria_5.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    txt_good_criteria_5.setText("");
                    txt_excellent_criteria_5.setText("");
                    txt_okay_criteria_5.setText("");
                    overallCleanliness = 1;
                    ratting_size[4]= overallCleanliness;
                    calculation(overallCleanliness,4);

                } else {
                    image_improvement_criteria_5.setVisibility(View.VISIBLE);
                    image_improvement_cri_5.setVisibility(View.GONE);
                    image_improvement_criteria_5.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    txt_improvement_criteria_5.setText("");
                }

                break;

            case R.id.image_improvement_criteria_6:

                if (image_improvement_criteria_6.isClickable()) {

                    image_good_cri_6.setVisibility(View.GONE);
                    image_excellent_cri_6.setVisibility(View.GONE);
                    image_okay_cri_6.setVisibility(View.GONE);
                    image_improvement_cri_6.setBackgroundResource(R.mipmap.improvementemojiselected);
                    image_improvement_cri_6.setVisibility(View.VISIBLE);
                    image_improvement_criteria_6.setVisibility(View.GONE);
                    txt_improvement_criteria_6.setText("Need Improvement");

                    image_excellent_criteria_6.setVisibility(View.VISIBLE);
                    image_good_criteria_6.setVisibility(View.VISIBLE);
                    image_okay_criteria_6.setVisibility(View.VISIBLE);
                    image_good_criteria_6.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_okay_criteria_6.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_excellent_criteria_6.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    txt_good_criteria_6.setText("");
                    txt_excellent_criteria_6.setText("");
                    txt_okay_criteria_6.setText("");
                    signage = 1;
                    ratting_size[5]= signage;
                    calculation(signage,5);

                } else {
                    image_improvement_criteria_6.setVisibility(View.VISIBLE);
                    image_improvement_cri_6.setVisibility(View.GONE);
                    image_improvement_criteria_6.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    txt_improvement_criteria_6.setText("");
                }

                break;

            case R.id.image_improvement_criteria_7:

                if (image_improvement_criteria_7.isClickable())
                {
                    image_good_cri_7.setVisibility(View.GONE);
                    image_excellent_cri_7.setVisibility(View.GONE);
                    image_okay_cri_7.setVisibility(View.GONE);
                    image_improvement_cri_7.setBackgroundResource(R.mipmap.improvementemojiselected);
                    image_improvement_cri_7.setVisibility(View.VISIBLE);
                    image_improvement_criteria_7.setVisibility(View.GONE);
                    txt_improvement_criteria_7.setText("Need Improvement");
                    image_excellent_criteria_7.setVisibility(View.VISIBLE);
                    image_good_criteria_7.setVisibility(View.VISIBLE);
                    image_okay_criteria_7.setVisibility(View.VISIBLE);
                    image_good_criteria_7.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_okay_criteria_7.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_excellent_criteria_7.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    txt_good_criteria_7.setText("");
                    txt_excellent_criteria_7.setText("");
                    txt_okay_criteria_7.setText("");

                    mpmExecution = 1;
                    ratting_size[6]=mpmExecution;
                    calculation(mpmExecution,6);

                } else
                {
                    image_improvement_criteria_7.setVisibility(View.VISIBLE);
                    image_improvement_cri_7.setVisibility(View.GONE);
                    image_improvement_criteria_7.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    txt_improvement_criteria_7.setText("");
                }


                break;

            case R.id.image_improvement_criteria_8:

                if (image_improvement_criteria_8.isClickable()) {

                    image_good_cri_8.setVisibility(View.GONE);
                    image_excellent_cri_8.setVisibility(View.GONE);
                    image_okay_cri_8.setVisibility(View.GONE);
                    image_improvement_cri_8.setBackgroundResource(R.mipmap.improvementemojiselected);
                    image_improvement_cri_8.setVisibility(View.VISIBLE);
                    image_improvement_criteria_8.setVisibility(View.GONE);
                    txt_improvement_criteria_8.setText("Need Improvement");
                    image_excellent_criteria_8.setVisibility(View.VISIBLE);
                    image_good_criteria_8.setVisibility(View.VISIBLE);
                    image_okay_criteria_8.setVisibility(View.VISIBLE);
                    image_good_criteria_8.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_okay_criteria_8.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_excellent_criteria_8.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    txt_good_criteria_8.setText("");
                    txt_excellent_criteria_8.setText("");
                    txt_okay_criteria_8.setText("");

                    winClusterMannequinsDisp = 1;
                    ratting_size[7]= winClusterMannequinsDisp;
                    calculation(winClusterMannequinsDisp,7);

                } else {
                    image_improvement_criteria_8.setVisibility(View.VISIBLE);
                    image_improvement_cri_8.setVisibility(View.GONE);
                    image_improvement_criteria_8.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    txt_improvement_criteria_8.setText("");
                }

                break;

            case R.id.image_okay_criteria_1:

                if (image_okay_criteria_1.isClickable()) {

                    image_improvement_cri_1.setVisibility(View.GONE);
                    image_excellent_cri_1.setVisibility(View.GONE);
                    image_good_cri_1.setVisibility(View.GONE);
                    image_improvement_criteria_1.setVisibility(View.VISIBLE);
                    image_improvement_criteria_1.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_good_criteria_1.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_excellent_criteria_1.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    image_okay_cri_1.setBackgroundResource(R.mipmap.okayemojiselected);
                    image_okay_cri_1.setVisibility(View.VISIBLE);
                    image_good_criteria_1.setVisibility(View.VISIBLE);
                    image_excellent_criteria_1.setVisibility(View.VISIBLE);
                    image_okay_criteria_1.setVisibility(View.GONE);
                    txt_okay_criteria_1.setText("Okay");
                    txt_good_criteria_1.setText("");
                    txt_excellent_criteria_1.setText("");
                    txt_improvement_criteria_1.setText("");

                    fashionQuot = 2;
                    ratting_size[0] = fashionQuot;
                    calculation(fashionQuot,0);

                } else {
                    image_okay_criteria_1.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_okay_criteria_1.setVisibility(View.VISIBLE);
                    image_okay_cri_1.setVisibility(View.GONE);
                    txt_okay_criteria_1.setText("");
                }

                break;

            case R.id.image_okay_criteria_2:
                if (image_okay_criteria_2.isClickable())
                {
                    image_improvement_cri_2.setVisibility(View.GONE);
                    image_excellent_cri_2.setVisibility(View.GONE);
                    image_good_cri_2.setVisibility(View.GONE);
                    image_improvement_criteria_2.setVisibility(View.VISIBLE);
                    image_improvement_criteria_2.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_good_criteria_2.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_excellent_criteria_2.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    image_okay_cri_2.setBackgroundResource(R.mipmap.okayemojiselected);
                    image_okay_cri_2.setVisibility(View.VISIBLE);
                    image_good_criteria_2.setVisibility(View.VISIBLE);
                    image_excellent_criteria_2.setVisibility(View.VISIBLE);
                    image_okay_criteria_2.setVisibility(View.GONE);
                    txt_okay_criteria_2.setText("Okay");

                    txt_good_criteria_2.setText("");
                    txt_excellent_criteria_2.setText("");
                    txt_improvement_criteria_2.setText("");

                    merchDisplay = 2;
                    ratting_size[1] = merchDisplay;
                    calculation(merchDisplay,1);

                } else {
                     image_okay_criteria_2.setVisibility(View.VISIBLE);
                    image_okay_cri_2.setVisibility(View.GONE);
                    image_okay_criteria_2.setBackgroundResource(R.mipmap.okayemojiunselected);
                    txt_okay_criteria_2.setText("");
                }


                break;

            case R.id.image_okay_criteria_3:
                if (image_okay_criteria_3.isClickable()) {

                    image_improvement_cri_3.setVisibility(View.GONE);
                    image_excellent_cri_3.setVisibility(View.GONE);
                    image_good_cri_3.setVisibility(View.GONE);
                    image_improvement_criteria_3.setVisibility(View.VISIBLE);
                    image_improvement_criteria_3.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_good_criteria_3.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_excellent_criteria_3.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    image_okay_cri_3.setBackgroundResource(R.mipmap.okayemojiselected);
                    image_okay_cri_3.setVisibility(View.VISIBLE);
                    image_good_criteria_3.setVisibility(View.VISIBLE);
                    image_excellent_criteria_3.setVisibility(View.VISIBLE);
                    image_okay_criteria_3.setVisibility(View.GONE);
                    txt_okay_criteria_3.setText("Okay");
                    txt_good_criteria_3.setText("");
                    txt_excellent_criteria_3.setText("");
                    txt_improvement_criteria_3.setText("");
                    merchPresentationStd = 2;
                    ratting_size[2]=merchPresentationStd;
                    calculation(merchPresentationStd,2);

                } else {
                    image_okay_cri_3.setVisibility(View.GONE);
                    image_okay_criteria_3.setVisibility(View.VISIBLE);
                    image_okay_criteria_3.setBackgroundResource(R.mipmap.okayemojiunselected);
                    txt_okay_criteria_3.setText("");
                }

                break;

            case R.id.image_okay_criteria_4:

                if (image_okay_criteria_4.isClickable()) {
                    image_improvement_cri_4.setVisibility(View.GONE);
                    image_excellent_cri_4.setVisibility(View.GONE);
                    image_good_cri_4.setVisibility(View.GONE);
                    image_improvement_criteria_4.setVisibility(View.VISIBLE);
                    image_improvement_criteria_4.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_good_criteria_4.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_excellent_criteria_4.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    image_okay_cri_4.setBackgroundResource(R.mipmap.okayemojiselected);
                    image_okay_cri_4.setVisibility(View.VISIBLE);
                    image_good_criteria_4.setVisibility(View.VISIBLE);
                    image_excellent_criteria_4.setVisibility(View.VISIBLE);
                    image_okay_criteria_4.setVisibility(View.GONE);
                    txt_okay_criteria_4.setText("Okay");
                    txt_good_criteria_4.setText("");
                    txt_excellent_criteria_4.setText("");
                    txt_improvement_criteria_4.setText("");

                    suggSellingByStaff = 2;
                    ratting_size[3]=suggSellingByStaff;
                    calculation(suggSellingByStaff,3);


                } else {
                    image_okay_criteria_4.setVisibility(View.VISIBLE);
                    image_okay_cri_4.setVisibility(View.GONE);
                    image_okay_criteria_4.setBackgroundResource(R.mipmap.okayemojiunselected);
                    txt_okay_criteria_4.setText("");
                }
                break;

            case R.id.image_okay_criteria_5:

                if (image_okay_criteria_5.isClickable()) {

                    image_improvement_cri_5.setVisibility(View.GONE);
                    image_excellent_cri_5.setVisibility(View.GONE);
                    image_good_cri_5.setVisibility(View.GONE);
                    image_improvement_criteria_5.setVisibility(View.VISIBLE);
                    image_improvement_criteria_5.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_good_criteria_5.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_excellent_criteria_5.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    image_okay_cri_5.setBackgroundResource(R.mipmap.okayemojiselected);
                    image_okay_cri_5.setVisibility(View.VISIBLE);
                    image_good_criteria_5.setVisibility(View.VISIBLE);
                    image_excellent_criteria_5.setVisibility(View.VISIBLE);
                    image_okay_criteria_5.setVisibility(View.GONE);
                    txt_okay_criteria_5.setText("Okay");
                    txt_good_criteria_5.setText("");
                    txt_excellent_criteria_5.setText("");
                    txt_improvement_criteria_5.setText("");

                    overallCleanliness = 2;
                    ratting_size[4]=overallCleanliness;
                    calculation(overallCleanliness,4);

                } else {
                    image_okay_cri_5.setVisibility(View.GONE);
                    image_okay_criteria_5.setVisibility(View.VISIBLE);
                    image_okay_criteria_5.setBackgroundResource(R.mipmap.okayemojiunselected);
                    txt_okay_criteria_5.setText("");
                }


                break;

            case R.id.image_okay_criteria_6:
                if (image_okay_criteria_6.isClickable()) {

                    image_improvement_cri_6.setVisibility(View.GONE);
                    image_excellent_cri_6.setVisibility(View.GONE);
                    image_good_cri_6.setVisibility(View.GONE);
                    image_improvement_criteria_6.setVisibility(View.VISIBLE);
                    image_improvement_criteria_6.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_good_criteria_6.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_excellent_criteria_6.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    image_okay_cri_6.setBackgroundResource(R.mipmap.okayemojiselected);
                    image_okay_cri_6.setVisibility(View.VISIBLE);
                    image_good_criteria_6.setVisibility(View.VISIBLE);
                    image_excellent_criteria_6.setVisibility(View.VISIBLE);
                    image_okay_criteria_6.setVisibility(View.GONE);
                    txt_okay_criteria_6.setText("Okay");
                    txt_good_criteria_6.setText("");
                    txt_excellent_criteria_6.setText("");
                    txt_improvement_criteria_6.setText("");

                    signage = 2;
                    ratting_size[5]=signage;
                    calculation(signage,5);


                } else {
                    image_okay_cri_6.setVisibility(View.GONE);
                    image_okay_criteria_6.setVisibility(View.VISIBLE);
                    image_okay_criteria_6.setBackgroundResource(R.mipmap.okayemojiunselected);
                    txt_okay_criteria_6.setText("");
                }


                break;

            case R.id.image_okay_criteria_7:
                if (image_okay_criteria_7.isClickable()) {

                    image_improvement_cri_7.setVisibility(View.GONE);
                    image_excellent_cri_7.setVisibility(View.GONE);
                    image_good_cri_7.setVisibility(View.GONE);
                    image_improvement_criteria_7.setVisibility(View.VISIBLE);
                    image_improvement_criteria_7.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_good_criteria_7.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_excellent_criteria_7.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    image_okay_cri_7.setBackgroundResource(R.mipmap.okayemojiselected);
                    image_okay_cri_7.setVisibility(View.VISIBLE);
                    image_good_criteria_7.setVisibility(View.VISIBLE);
                    image_excellent_criteria_7.setVisibility(View.VISIBLE);
                    image_okay_criteria_7.setVisibility(View.GONE);
                    txt_okay_criteria_7.setText("Okay");
                    txt_good_criteria_7.setText("");
                    txt_excellent_criteria_7.setText("");
                    txt_improvement_criteria_7.setText("");

                    mpmExecution = 2;
                    ratting_size[6]=mpmExecution;
                    calculation(mpmExecution,6);

                } else {
                    image_okay_cri_7.setVisibility(View.GONE);
                    image_okay_criteria_7.setVisibility(View.VISIBLE);
                    image_okay_criteria_7.setBackgroundResource(R.mipmap.okayemojiunselected);
                    txt_okay_criteria_7.setText("");
                }


                break;

            case R.id.image_okay_criteria_8:

                if (image_okay_criteria_8.isClickable()) {

                    image_improvement_cri_8.setVisibility(View.GONE);
                    image_excellent_cri_8.setVisibility(View.GONE);
                    image_good_cri_8.setVisibility(View.GONE);
                    image_improvement_criteria_8.setVisibility(View.VISIBLE);
                    image_improvement_criteria_8.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_good_criteria_8.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_excellent_criteria_8.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    image_okay_cri_8.setBackgroundResource(R.mipmap.okayemojiselected);
                    image_okay_cri_8.setVisibility(View.VISIBLE);
                    image_good_criteria_8.setVisibility(View.VISIBLE);
                    image_excellent_criteria_8.setVisibility(View.VISIBLE);
                    image_okay_criteria_8.setVisibility(View.GONE);
                    txt_okay_criteria_8.setText("Okay");
                    txt_good_criteria_8.setText("");
                    txt_excellent_criteria_8.setText("");
                    txt_improvement_criteria_8.setText("");

                    winClusterMannequinsDisp = 2;
                    ratting_size[7]= winClusterMannequinsDisp;
                    calculation(winClusterMannequinsDisp,7);

                } else {
                    image_okay_cri_8.setVisibility(View.GONE);
                    image_okay_criteria_8.setVisibility(View.VISIBLE);
                    image_okay_criteria_8.setBackgroundResource(R.mipmap.okayemojiunselected);
                    txt_okay_criteria_8.setText("");
                }


                break;

            case R.id.image_good_criteria_1:

                if (image_good_criteria_1.isClickable())
                {
                    image_improvement_cri_1.setVisibility(View.GONE);
                    image_excellent_cri_1.setVisibility(View.GONE);
                    image_okay_cri_1.setVisibility(View.GONE);
                    image_improvement_criteria_1.setVisibility(View.VISIBLE);
                    image_excellent_criteria_1.setVisibility(View.VISIBLE);
                    image_okay_criteria_1.setVisibility(View.VISIBLE);
                    image_improvement_criteria_1.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_okay_criteria_1.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_excellent_criteria_1.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    image_good_cri_1.setBackgroundResource(R.mipmap.goodemojiselected);
                    image_good_cri_1.setVisibility(View.VISIBLE);
                    image_good_criteria_1.setVisibility(View.GONE);
                    txt_good_criteria_1.setText("Good");
                    txt_okay_criteria_1.setText("");
                    txt_excellent_criteria_1.setText("");
                    txt_improvement_criteria_1.setText("");
                    fashionQuot = 3;
                    ratting_size[0]= fashionQuot;
                    calculation(fashionQuot,0);


                } else
                {
                    image_good_criteria_1.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_good_criteria_1.setVisibility(View.VISIBLE);
                    image_good_cri_1.setVisibility(View.GONE);
                    txt_good_criteria_1.setText("");
                }

                break;

            case R.id.image_good_criteria_2:
                if (image_good_criteria_2.isClickable())
                {
                    image_improvement_cri_2.setVisibility(View.GONE);
                    image_excellent_cri_2.setVisibility(View.GONE);
                    image_okay_cri_2.setVisibility(View.GONE);
                    image_improvement_criteria_2.setVisibility(View.VISIBLE);
                    image_excellent_criteria_2.setVisibility(View.VISIBLE);
                    image_okay_criteria_2.setVisibility(View.VISIBLE);
                    image_improvement_criteria_2.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_okay_criteria_2.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_excellent_criteria_2.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    image_good_cri_2.setBackgroundResource(R.mipmap.goodemojiselected);
                    image_good_cri_2.setVisibility(View.VISIBLE);
                    image_good_criteria_2.setVisibility(View.GONE);
                    txt_good_criteria_2.setText("Good");
                    txt_okay_criteria_2.setText("");
                    txt_excellent_criteria_2.setText("");
                    txt_improvement_criteria_2.setText("");
                    merchDisplay = 3;
                    ratting_size[1]= merchDisplay;
                    calculation(merchDisplay,1);

                } else
                {
                    image_good_criteria_2.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_good_criteria_2.setVisibility(View.VISIBLE);
                    image_good_cri_2.setVisibility(View.GONE);
                    txt_good_criteria_2.setText("");
                }


                break;
            case R.id.image_good_criteria_3:

                if (image_good_criteria_3.isClickable()) {
                    image_improvement_cri_3.setVisibility(View.GONE);
                    image_excellent_cri_3.setVisibility(View.GONE);
                    image_okay_cri_3.setVisibility(View.GONE);
                    image_improvement_criteria_3.setVisibility(View.VISIBLE);
                    image_excellent_criteria_3.setVisibility(View.VISIBLE);
                    image_okay_criteria_3.setVisibility(View.VISIBLE);
                    image_improvement_criteria_3.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_okay_criteria_3.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_excellent_criteria_3.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    image_good_cri_3.setBackgroundResource(R.mipmap.goodemojiselected);
                    image_good_cri_3.setVisibility(View.VISIBLE);
                    image_good_criteria_3.setVisibility(View.GONE);
                    txt_good_criteria_3.setText("Good");

                    txt_okay_criteria_3.setText("");
                    txt_excellent_criteria_3.setText("");
                    txt_improvement_criteria_3.setText("");

                    merchPresentationStd = 3;
                    ratting_size[2]=merchPresentationStd;
                    calculation(merchPresentationStd,2);

                } else {
                    image_good_criteria_3.setVisibility(View.VISIBLE);
                    image_good_cri_3.setVisibility(View.GONE);
                    image_good_criteria_3.setBackgroundResource(R.mipmap.goodemojiunselected);
                    txt_good_criteria_3.setText("");
                }

                break;

            case R.id.image_good_criteria_4:

                if (image_good_criteria_4.isClickable()) {

                    image_improvement_cri_4.setVisibility(View.GONE);
                    image_excellent_cri_4.setVisibility(View.GONE);
                    image_okay_cri_4.setVisibility(View.GONE);
                    image_improvement_criteria_4.setVisibility(View.VISIBLE);
                    image_excellent_criteria_4.setVisibility(View.VISIBLE);
                    image_okay_criteria_4.setVisibility(View.VISIBLE);
                    image_improvement_criteria_4.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_okay_criteria_4.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_excellent_criteria_4.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    image_good_cri_4.setBackgroundResource(R.mipmap.goodemojiselected);
                    image_good_cri_4.setVisibility(View.VISIBLE);
                    image_good_criteria_4.setVisibility(View.GONE);
                    txt_good_criteria_4.setText("Good");
                    txt_okay_criteria_4.setText("");
                    txt_excellent_criteria_4.setText("");
                    txt_improvement_criteria_4.setText("");
                    suggSellingByStaff = 3;
                    ratting_size[3]=suggSellingByStaff;
                    calculation(suggSellingByStaff,3);

                } else {
                    image_good_criteria_4.setVisibility(View.VISIBLE);
                    image_good_cri_4.setVisibility(View.GONE);
                    image_good_criteria_4.setBackgroundResource(R.mipmap.goodemojiunselected);
                    txt_good_criteria_4.setText("");
                }

                break;

            case R.id.image_good_criteria_5:

                if (image_good_criteria_5.isClickable()) {

                    image_improvement_cri_5.setVisibility(View.GONE);
                    image_excellent_cri_5.setVisibility(View.GONE);
                    image_okay_cri_5.setVisibility(View.GONE);
                    image_improvement_criteria_5.setVisibility(View.VISIBLE);
                    image_excellent_criteria_5.setVisibility(View.VISIBLE);
                    image_okay_criteria_5.setVisibility(View.VISIBLE);
                    image_improvement_criteria_5.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_okay_criteria_5.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_excellent_criteria_5.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    image_good_cri_5.setBackgroundResource(R.mipmap.goodemojiselected);
                    image_good_cri_5.setVisibility(View.VISIBLE);
                    image_good_criteria_5.setVisibility(View.GONE);
                    txt_good_criteria_5.setText("Good");
                    txt_okay_criteria_5.setText("");
                    txt_excellent_criteria_5.setText("");
                    txt_improvement_criteria_5.setText("");
                    overallCleanliness = 3;
                    ratting_size[4]=overallCleanliness;
                    calculation(overallCleanliness,4);


                } else {
                    image_good_cri_5.setVisibility(View.GONE);
                    image_good_criteria_5.setVisibility(View.VISIBLE);
                    image_good_criteria_5.setBackgroundResource(R.mipmap.goodemojiunselected);
                    txt_good_criteria_5.setText("");
                }

                break;

            case R.id.image_good_criteria_6:

                if (image_good_criteria_6.isClickable()) {

                    image_improvement_cri_6.setVisibility(View.GONE);
                    image_excellent_cri_6.setVisibility(View.GONE);
                    image_okay_cri_6.setVisibility(View.GONE);
                    image_improvement_criteria_6.setVisibility(View.VISIBLE);
                    image_excellent_criteria_6.setVisibility(View.VISIBLE);
                    image_okay_criteria_6.setVisibility(View.VISIBLE);
                    image_improvement_criteria_6.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_okay_criteria_6.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_excellent_criteria_6.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    image_good_cri_6.setBackgroundResource(R.mipmap.goodemojiselected);
                    image_good_cri_6.setVisibility(View.VISIBLE);
                    image_good_criteria_6.setVisibility(View.GONE);
                    txt_good_criteria_6.setText("Good");
                    txt_okay_criteria_6.setText("");
                    txt_excellent_criteria_6.setText("");
                    txt_improvement_criteria_6.setText("");
                    signage = 3;
                    ratting_size[5]=signage;
                    calculation(signage,5);

                } else {
                     image_good_criteria_6.setVisibility(View.VISIBLE);
                    image_good_cri_6.setVisibility(View.GONE);
                    image_good_criteria_6.setBackgroundResource(R.mipmap.goodemojiunselected);
                    txt_good_criteria_6.setText("");
                }

                break;

            case R.id.image_good_criteria_7:

                if (image_good_criteria_7.isClickable()) {

                    image_improvement_cri_7.setVisibility(View.GONE);
                    image_excellent_cri_7.setVisibility(View.GONE);
                    image_okay_cri_7.setVisibility(View.GONE);
                    image_improvement_criteria_7.setVisibility(View.VISIBLE);
                    image_excellent_criteria_7.setVisibility(View.VISIBLE);
                    image_okay_criteria_7.setVisibility(View.VISIBLE);
                    image_improvement_criteria_7.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_okay_criteria_7.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_excellent_criteria_7.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    image_good_cri_7.setBackgroundResource(R.mipmap.goodemojiselected);
                    image_good_cri_7.setVisibility(View.VISIBLE);
                    image_good_criteria_7.setVisibility(View.GONE);
                    txt_good_criteria_7.setText("Good");
                    txt_okay_criteria_7.setText("");
                    txt_excellent_criteria_7.setText("");
                    txt_improvement_criteria_7.setText("");
                    mpmExecution = 3;
                    ratting_size[6]=mpmExecution;
                    calculation(mpmExecution,6);


                } else {
                    image_good_criteria_7.setVisibility(View.VISIBLE);
                    image_good_cri_8.setVisibility(View.GONE);
                    image_good_criteria_7.setBackgroundResource(R.mipmap.goodemojiunselected);
                    txt_good_criteria_7.setText("");
                }

                break;

            case R.id.image_good_criteria_8:

                if (image_good_criteria_8.isClickable()) {
                    image_improvement_cri_8.setVisibility(View.GONE);
                    image_excellent_cri_8.setVisibility(View.GONE);
                    image_okay_cri_8.setVisibility(View.GONE);
                    image_improvement_criteria_8.setVisibility(View.VISIBLE);
                    image_excellent_criteria_8.setVisibility(View.VISIBLE);
                    image_okay_criteria_8.setVisibility(View.VISIBLE);
                    image_improvement_criteria_8.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_okay_criteria_8.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_excellent_criteria_8.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    image_good_cri_8.setBackgroundResource(R.mipmap.goodemojiselected);
                    image_good_cri_8.setVisibility(View.VISIBLE);
                    image_good_criteria_8.setVisibility(View.GONE);
                    txt_good_criteria_8.setText("Good");
                    txt_okay_criteria_8.setText("");
                    txt_excellent_criteria_8.setText("");
                    txt_improvement_criteria_8.setText("");
                    winClusterMannequinsDisp = 3;
                    ratting_size[7]=winClusterMannequinsDisp;
                    calculation(winClusterMannequinsDisp,7);



                } else {
                    image_good_cri_8.setVisibility(View.GONE);
                    image_good_criteria_8.setVisibility(View.VISIBLE);
                    image_good_criteria_8.setBackgroundResource(R.mipmap.goodemojiunselected);
                    txt_good_criteria_8.setText("");
                }


                break;

            case R.id.image_excellent_criteria_1:

                if (image_excellent_criteria_1.isClickable())
                {
                    image_good_cri_1.setVisibility(View.GONE);
                    image_okay_cri_1.setVisibility(View.GONE);
                    image_improvement_cri_1.setVisibility(View.GONE);
                    image_improvement_criteria_1.setVisibility(View.VISIBLE);
                    image_okay_criteria_1.setVisibility(View.VISIBLE);
                    image_good_criteria_1.setVisibility(View.VISIBLE);
                    image_improvement_criteria_1.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_okay_criteria_1.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_good_criteria_1.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_excellent_cri_1.setBackgroundResource(R.mipmap.excellentemojiselected);
                    image_excellent_cri_1.setVisibility(View.VISIBLE);
                    image_excellent_criteria_1.setVisibility(View.GONE);
                    txt_excellent_criteria_1.setText("Excellent");
                    txt_okay_criteria_1.setText("");
                    txt_good_criteria_1.setText("");
                    txt_improvement_criteria_1.setText("");
                    fashionQuot = 4;
                    ratting_size[0]=fashionQuot;
                    calculation(fashionQuot,0);


                } else {
                    image_excellent_criteria_1.setVisibility(View.VISIBLE);
                    image_excellent_cri_1.setVisibility(View.GONE);
                    image_excellent_criteria_1.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    txt_excellent_criteria_1.setText("");
                }
                break;

            case R.id.image_excellent_criteria_2:

                if (image_excellent_criteria_2.isClickable()) {

                    image_good_cri_2.setVisibility(View.GONE);
                    image_okay_cri_2.setVisibility(View.GONE);
                    image_improvement_cri_2.setVisibility(View.GONE);
                    image_improvement_criteria_2.setVisibility(View.VISIBLE);
                    image_okay_criteria_2.setVisibility(View.VISIBLE);
                    image_good_criteria_2.setVisibility(View.VISIBLE);
                    image_improvement_criteria_2.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_okay_criteria_2.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_good_criteria_2.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_excellent_cri_2.setBackgroundResource(R.mipmap.excellentemojiselected);
                    image_excellent_cri_2.setVisibility(View.VISIBLE);
                    image_excellent_criteria_2.setVisibility(View.GONE);
                    txt_excellent_criteria_2.setText("Excellent");
                    txt_okay_criteria_2.setText("");
                    txt_good_criteria_2.setText("");
                    txt_improvement_criteria_2.setText("");
                    merchDisplay = 4;
                    ratting_size[1]=merchDisplay;
                    calculation(merchDisplay,1);

                } else {
                    image_excellent_criteria_2.setVisibility(View.VISIBLE);
                    image_excellent_cri_2.setVisibility(View.GONE);
                    image_excellent_criteria_2.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    txt_excellent_criteria_2.setText("");
                }
                break;

            case R.id.image_excellent_criteria_3:

                if (image_excellent_criteria_3.isClickable()) {

                    image_good_cri_3.setVisibility(View.GONE);
                    image_okay_cri_3.setVisibility(View.GONE);
                    image_improvement_cri_3.setVisibility(View.GONE);
                    image_improvement_criteria_3.setVisibility(View.VISIBLE);
                    image_okay_criteria_3.setVisibility(View.VISIBLE);
                    image_good_criteria_3.setVisibility(View.VISIBLE);
                    image_improvement_criteria_3.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_okay_criteria_3.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_good_criteria_3.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_excellent_cri_3.setBackgroundResource(R.mipmap.excellentemojiselected);
                    image_excellent_cri_3.setVisibility(View.VISIBLE);
                    image_excellent_criteria_3.setVisibility(View.GONE);
                    txt_excellent_criteria_3.setText("Excellent");
                    txt_okay_criteria_3.setText("");
                    txt_good_criteria_3.setText("");
                    txt_improvement_criteria_3.setText("");
                    merchPresentationStd = 4;
                    ratting_size[2]=merchPresentationStd;
                    calculation(merchPresentationStd,2);

                } else {
                    image_excellent_criteria_3.setVisibility(View.VISIBLE);
                    image_excellent_cri_3.setVisibility(View.GONE);
                    image_excellent_criteria_3.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    txt_excellent_criteria_3.setText("");

                }
                break;

            case R.id.image_excellent_criteria_4:
                if (image_excellent_criteria_4.isClickable()) {


                    image_good_cri_4.setVisibility(View.GONE);
                    image_okay_cri_4.setVisibility(View.GONE);
                    image_improvement_cri_4.setVisibility(View.GONE);
                    image_improvement_criteria_4.setVisibility(View.VISIBLE);
                    image_okay_criteria_4.setVisibility(View.VISIBLE);
                    image_good_criteria_4.setVisibility(View.VISIBLE);
                    image_improvement_criteria_4.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_okay_criteria_4.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_good_criteria_4.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_excellent_cri_4.setBackgroundResource(R.mipmap.excellentemojiselected);
                    image_excellent_cri_4.setVisibility(View.VISIBLE);
                    image_excellent_criteria_4.setVisibility(View.GONE);
                    txt_excellent_criteria_4.setText("Excellent");
                    txt_okay_criteria_4.setText("");
                    txt_good_criteria_4.setText("");
                    txt_improvement_criteria_4.setText("");
                    suggSellingByStaff = 4;
                    ratting_size[3]=suggSellingByStaff;
                    calculation(suggSellingByStaff,3);


                } else {
                      image_excellent_criteria_4.setVisibility(View.VISIBLE);
                    image_excellent_cri_4.setVisibility(View.GONE);
                    image_excellent_criteria_4.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    txt_excellent_criteria_4.setText("");
                }
                break;

            case R.id.image_excellent_criteria_5:

                if (image_excellent_criteria_5.isClickable())
                {

                    image_good_cri_5.setVisibility(View.GONE);
                    image_okay_cri_5.setVisibility(View.GONE);
                    image_improvement_cri_5.setVisibility(View.GONE);
                    image_improvement_criteria_5.setVisibility(View.VISIBLE);
                    image_okay_criteria_5.setVisibility(View.VISIBLE);
                    image_good_criteria_5.setVisibility(View.VISIBLE);
                    image_improvement_criteria_5.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_okay_criteria_5.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_good_criteria_5.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_excellent_cri_5.setBackgroundResource(R.mipmap.excellentemojiselected);
                    image_excellent_cri_5.setVisibility(View.VISIBLE);
                    image_excellent_criteria_5.setVisibility(View.GONE);
                    txt_excellent_criteria_5.setText("Excellent");

                    txt_okay_criteria_5.setText("");
                    txt_good_criteria_5.setText("");
                    txt_improvement_criteria_5.setText("");
                    overallCleanliness = 4;
                    ratting_size[4]=overallCleanliness;
                    calculation(overallCleanliness,4);

                } else {
                     image_excellent_criteria_5.setVisibility(View.VISIBLE);
                    image_excellent_cri_5.setVisibility(View.GONE);
                    image_excellent_criteria_5.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    txt_excellent_criteria_5.setText("");
                }
                break;

            case R.id.image_excellent_criteria_6:

                if (image_excellent_criteria_6.isClickable()) {

                    image_good_cri_6.setVisibility(View.GONE);
                    image_okay_cri_6.setVisibility(View.GONE);
                    image_improvement_cri_6.setVisibility(View.GONE);
                    image_improvement_criteria_6.setVisibility(View.VISIBLE);
                    image_okay_criteria_6.setVisibility(View.VISIBLE);
                    image_good_criteria_6.setVisibility(View.VISIBLE);
                    image_improvement_criteria_6.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_okay_criteria_6.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_good_criteria_6.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_excellent_cri_6.setBackgroundResource(R.mipmap.excellentemojiselected);
                    image_excellent_cri_6.setVisibility(View.VISIBLE);
                    image_excellent_criteria_6.setVisibility(View.GONE);
                    txt_excellent_criteria_1.setText("Excellent");
                    txt_okay_criteria_6.setText("");
                    txt_good_criteria_6.setText("");
                    txt_improvement_criteria_6.setText("");
                    signage = 4;
                    ratting_size[5]=signage;
                    calculation(signage,5);

                } else {
                    image_excellent_cri_6.setVisibility(View.GONE);
                    image_excellent_criteria_6.setVisibility(View.VISIBLE);
                     image_excellent_criteria_6.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    txt_excellent_criteria_6.setText("");
                }

                break;

            case R.id.image_excellent_criteria_7:

                if (image_excellent_criteria_7.isClickable()) {

                    image_good_cri_7.setVisibility(View.GONE);
                    image_okay_cri_7.setVisibility(View.GONE);
                    image_improvement_cri_7.setVisibility(View.GONE);
                    image_improvement_criteria_7.setVisibility(View.VISIBLE);
                    image_okay_criteria_7.setVisibility(View.VISIBLE);
                    image_good_criteria_7.setVisibility(View.VISIBLE);
                    image_improvement_criteria_7.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_okay_criteria_7.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_good_criteria_7.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_excellent_cri_7.setBackgroundResource(R.mipmap.excellentemojiselected);
                    image_excellent_cri_7.setVisibility(View.VISIBLE);
                    image_excellent_criteria_7.setVisibility(View.GONE);
                    txt_excellent_criteria_7.setText("Excellent");

                    txt_okay_criteria_7.setText("");
                    txt_good_criteria_7.setText("");
                    txt_improvement_criteria_7.setText("");
                    mpmExecution = 4;
                    ratting_size[6]=mpmExecution;
                    calculation(mpmExecution,6);

                } else {
                      image_excellent_criteria_7.setVisibility(View.VISIBLE);
                    image_excellent_cri_7.setVisibility(View.GONE);
                    image_excellent_criteria_7.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    txt_excellent_criteria_7.setText("");
                }
                break;

            case R.id.image_excellent_criteria_8:

                if (image_excellent_criteria_8.isClickable()) {

                    image_good_cri_8.setVisibility(View.GONE);
                    image_okay_cri_8.setVisibility(View.GONE);
                    image_improvement_cri_8.setVisibility(View.GONE);
                    image_improvement_criteria_8.setVisibility(View.VISIBLE);
                    image_okay_criteria_8.setVisibility(View.VISIBLE);
                    image_good_criteria_8.setVisibility(View.VISIBLE);
                    image_improvement_criteria_8.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_okay_criteria_8.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_good_criteria_8.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_excellent_cri_8.setBackgroundResource(R.mipmap.excellentemojiselected);
                    image_excellent_cri_8.setVisibility(View.VISIBLE);
                    image_excellent_criteria_8.setVisibility(View.GONE);
                    txt_excellent_criteria_8.setText("Excellent");

                    txt_okay_criteria_8.setText("");
                    txt_good_criteria_8.setText("");
                    txt_improvement_criteria_8.setText("");
                    winClusterMannequinsDisp = 4;
                    ratting_size[7]=winClusterMannequinsDisp;
                    calculation(winClusterMannequinsDisp,7);

                } else {
                     image_excellent_criteria_8.setVisibility(View.VISIBLE);
                    image_excellent_cri_8.setVisibility(View.GONE);
                    image_excellent_criteria_8.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    txt_excellent_criteria_8.setText("");
                }
                break;
            case R.id.camera_imageView:
                selectImage();
                break;
            case R.id.inspection_btnback:
                onBackPressed();
                break;
            case R.id.btn_insp_submit:
                OnSubmit();
                break;
            case R.id.btn_insp_reset:
                OnReset();
                break;

        }
    }

    private void OnReset() {
        //default emoji
        emoji_image.setBackgroundResource(R.mipmap.defaultemojiunselected);
        image_improvement_criteria_1.setBackgroundResource(R.mipmap.improvementemojiunselected);
        image_improvement_criteria_2.setBackgroundResource(R.mipmap.improvementemojiunselected);
        image_improvement_criteria_3.setBackgroundResource(R.mipmap.improvementemojiunselected);
        image_improvement_criteria_4.setBackgroundResource(R.mipmap.improvementemojiunselected);
        image_improvement_criteria_5.setBackgroundResource(R.mipmap.improvementemojiunselected);
        image_improvement_criteria_6.setBackgroundResource(R.mipmap.improvementemojiunselected);
        image_improvement_criteria_7.setBackgroundResource(R.mipmap.improvementemojiunselected);
        image_improvement_criteria_8.setBackgroundResource(R.mipmap.improvementemojiunselected);
        //Okay Emoji
        image_okay_criteria_1.setBackgroundResource(R.mipmap.okayemojiunselected);
        image_okay_criteria_2.setBackgroundResource(R.mipmap.okayemojiunselected);
        image_okay_criteria_3.setBackgroundResource(R.mipmap.okayemojiunselected);
        image_okay_criteria_4.setBackgroundResource(R.mipmap.okayemojiunselected);
        image_okay_criteria_5.setBackgroundResource(R.mipmap.okayemojiunselected);
        image_okay_criteria_6.setBackgroundResource(R.mipmap.okayemojiunselected);
        image_okay_criteria_7.setBackgroundResource(R.mipmap.okayemojiunselected);
        image_okay_criteria_8.setBackgroundResource(R.mipmap.okayemojiunselected);
        //Good Emoji
        image_good_criteria_1.setBackgroundResource(R.mipmap.goodemojiunselected);
        image_good_criteria_2.setBackgroundResource(R.mipmap.goodemojiunselected);
        image_good_criteria_3.setBackgroundResource(R.mipmap.goodemojiunselected);
        image_good_criteria_4.setBackgroundResource(R.mipmap.goodemojiunselected);
        image_good_criteria_5.setBackgroundResource(R.mipmap.goodemojiunselected);
        image_good_criteria_6.setBackgroundResource(R.mipmap.goodemojiunselected);
        image_good_criteria_7.setBackgroundResource(R.mipmap.goodemojiunselected);
        image_good_criteria_8.setBackgroundResource(R.mipmap.goodemojiunselected);
        //Excellent Emoji
        image_excellent_criteria_1.setBackgroundResource(R.mipmap.excellentemojiunselected);
        image_excellent_criteria_2.setBackgroundResource(R.mipmap.excellentemojiunselected);
        image_excellent_criteria_3.setBackgroundResource(R.mipmap.excellentemojiunselected);
        image_excellent_criteria_4.setBackgroundResource(R.mipmap.excellentemojiunselected);
        image_excellent_criteria_5.setBackgroundResource(R.mipmap.excellentemojiunselected);
        image_excellent_criteria_6.setBackgroundResource(R.mipmap.excellentemojiunselected);
        image_excellent_criteria_7.setBackgroundResource(R.mipmap.excellentemojiunselected);
        image_excellent_criteria_8.setBackgroundResource(R.mipmap.excellentemojiunselected);
         //Text improvement
        txt_improvement_criteria_1.setText("");
        txt_improvement_criteria_2.setText("");
        txt_improvement_criteria_3.setText("");
        txt_improvement_criteria_4.setText("");
        txt_improvement_criteria_5.setText("");
        txt_improvement_criteria_6.setText("");
        txt_improvement_criteria_7.setText("");
        txt_improvement_criteria_8.setText("");
        //Text okay
        txt_okay_criteria_1.setText("");
        txt_okay_criteria_2.setText("");
        txt_okay_criteria_3.setText("");
        txt_okay_criteria_4.setText("");
        txt_okay_criteria_5.setText("");
        txt_okay_criteria_6.setText("");
        txt_okay_criteria_7.setText("");
        txt_okay_criteria_8.setText("");
        //Text good
        txt_good_criteria_1.setText("");
        txt_good_criteria_2.setText("");
        txt_good_criteria_3.setText("");
        txt_good_criteria_4.setText("");
        txt_good_criteria_5.setText("");
        txt_good_criteria_6.setText("");
        txt_good_criteria_7.setText("");
        txt_good_criteria_8.setText("");
        //Text excellent
        txt_excellent_criteria_1.setText("");
        txt_excellent_criteria_2.setText("");
        txt_excellent_criteria_3.setText("");
        txt_excellent_criteria_4.setText("");
        txt_excellent_criteria_5.setText("");
        txt_excellent_criteria_6.setText("");
        txt_excellent_criteria_7.setText("");
        txt_excellent_criteria_8.setText("");
        et_inspected_by.setText("");
        et_comment.setText("");
    }

    private void OnSubmit() {
        //  JSONArray jsonarray=new JSONArray();
        // int count=0;
        String insp_comment , inspected_name ;
        JSONObject obj = new JSONObject();
        try {

        if (et_inspected_by.equals("") || et_inspected_by.length() == 0 ||
                    fashionQuot == 0 || merchDisplay == 0 || merchPresentationStd == 0 || suggSellingByStaff == 0
                    || overallCleanliness == 0 || signage == 0 || mpmExecution == 0 || winClusterMannequinsDisp == 0)
            {

                // Inspection Criteria...
                if (fashionQuot == 0 )
                {

                    Toast.makeText(InspectionBeginActivity.this, "Please select value of Fashion Quotient", Toast.LENGTH_LONG).show();

                }
                else if( merchDisplay == 0 )
                {
                    Toast.makeText(InspectionBeginActivity.this, "Please select value of Merchant Display", Toast.LENGTH_LONG).show();

                }
                else if(merchPresentationStd == 0 )
                {
                    Toast.makeText(InspectionBeginActivity.this, "Please select value of Merchandise Presentation Standards", Toast.LENGTH_LONG).show();

                }
                else if(suggSellingByStaff == 0 )
                {
                    Toast.makeText(InspectionBeginActivity.this, "Please select value of Suggestive Selling by Staff", Toast.LENGTH_LONG).show();

                }
                else if( overallCleanliness == 0)
                {
                    Toast.makeText(InspectionBeginActivity.this, "Please select value of Overall Cleanliness", Toast.LENGTH_LONG).show();

                }
                else if( signage == 0)
                {
                    Toast.makeText(InspectionBeginActivity.this, "Please select value of Signage", Toast.LENGTH_LONG).show();

                }
                else if( mpmExecution == 0)
                {
                    Toast.makeText(InspectionBeginActivity.this, "Please select value of MPM Execution", Toast.LENGTH_LONG).show();

                }
                else if( winClusterMannequinsDisp == 0)
                {
                    Toast.makeText(InspectionBeginActivity.this, "Please select value of WinClusterMannequinsDisp", Toast.LENGTH_LONG).show();

                }
                //For Inspected By -- Inspector Name
                else if (et_inspected_by.equals("") || et_inspected_by.length() == 0) {
                    Toast.makeText(InspectionBeginActivity.this, "Please enter name", Toast.LENGTH_LONG).show();

                }
                //For Comment
                else if (et_comment.equals("") || et_comment.length() == 0) {
                    Toast.makeText(InspectionBeginActivity.this, "Please enter comment", Toast.LENGTH_LONG).show();

                }
            } else {
                obj.put("inspectionDate", txt_insp_date_Val.getText().toString());
                if (fashionQuot != 0) // Condition for Inspection Criteria 1
                {
                    if (fashionQuot == 1) {
                        obj.put("fashionQuot", 1); //Need Improvement is selected
                    } else if (fashionQuot == 2) {
                        obj.put("fashionQuot", 2); //Okay is selected
                    } else if (fashionQuot == 3) {
                        obj.put("fashionQuot", 3); //Good is selected
                    } else if (fashionQuot == 4) {
                        obj.put("fashionQuot", 4); //Excellent is selected
                    }
                }
                if (merchDisplay != 0) // Condition for Inspection Criteria 2
                {
                    if (merchDisplay == 1) {
                        obj.put("merchDisplay", 1); //Need Improvement is selected
                    } else if (merchDisplay == 2) {
                        obj.put("merchDisplay", 2); //Okay is selected
                    } else if (merchDisplay == 3) {
                        obj.put("merchDisplay", 3); //Good is selected
                    } else if (merchDisplay == 4) {
                        obj.put("merchDisplay", 4); //Excellent is selected
                    }
                }
                if (merchPresentationStd != 0) // Condition for Inspection Criteria 3
                {
                    if (merchPresentationStd == 1) {
                        obj.put("merchPresentationStd", 1); //Need Improvement is selected
                    } else if (merchPresentationStd == 2) {
                        obj.put("merchPresentationStd", 2); //Okay is selected
                    } else if (merchPresentationStd == 3) {
                        obj.put("merchPresentationStd", 3); //Good is selected
                    } else if (merchPresentationStd == 4) {
                        obj.put("merchPresentationStd", 4); //Excellent is selected
                    }
                }
                if (suggSellingByStaff != 0) //Condition for Inspection Criteria 4
                {
                    if (suggSellingByStaff == 1) {
                        obj.put("suggSellingByStaff", 1); //Need Improvement is selected
                    } else if (suggSellingByStaff == 2) {
                        obj.put("suggSellingByStaff", 2); //Okay is selected
                    } else if (suggSellingByStaff == 3) {
                        obj.put("merchPresentationStd", 3); //Good is selected
                    } else if (merchPresentationStd == 4) {
                        obj.put("suggSellingByStaff", 4); //Excellent is selected
                    }
                }
                if (overallCleanliness != 0) // Condition for Inspection Criteria 5
                {
                    if (overallCleanliness == 1) {
                        obj.put("overallCleanliness", 1); //Need Improvement is selected
                    } else if (merchPresentationStd == 2) {
                        obj.put("merchPresentationStd", 2); //Okay is selected
                    } else if (overallCleanliness == 3) {
                        obj.put("overallCleanliness", 3); //Good is selected
                    } else if (overallCleanliness == 4) {
                        obj.put("overallCleanliness", 4); //Excellent is selected
                    }
                }
                if (signage != 0) // Condition for Inspection Criteria 6
                {
                    if (signage == 1) {
                        obj.put("signage", 1); //Need Improvement is selected
                    } else if (signage == 2) {
                        obj.put("signage", 2); //Okay is selected
                    } else if (signage == 3) {
                        obj.put("signage", 3); //Good is selected
                    } else if (signage == 4) {
                        obj.put("signage", 4); //Excellent is selected
                    }
                }
                if (mpmExecution != 0) // Condition for Inspection Criteria 7
                {
                    if (mpmExecution == 1) {
                        obj.put("mpmExecution", 1); //Need Improvement is selected
                    } else if (mpmExecution == 2) {
                        obj.put("mpmExecution", 2); //Okay is selected
                    } else if (mpmExecution == 3) {
                        obj.put("mpmExecution", 3); //Good is selected
                    } else if (mpmExecution == 4) {
                        obj.put("mpmExecution", 4); //Excellent is selected
                    }
                }
                if (winClusterMannequinsDisp != 0) // Condition for Inspection Criteria 8
                {
                    if (winClusterMannequinsDisp == 1) {
                        obj.put("winClusterMannequinsDisp", 1); //Need Improvement is selected
                    } else if (winClusterMannequinsDisp == 2) {
                        obj.put("winClusterMannequinsDisp", 2); //Okay is selected
                    } else if (winClusterMannequinsDisp == 3) {
                        obj.put("winClusterMannequinsDisp", 3); //Good is selected
                    } else if (winClusterMannequinsDisp == 4) {
                        obj.put("winClusterMannequinsDisp", 4); //Excellent is selected
                    }
                }
                inspected_name = et_inspected_by.getText().toString();
                InputMethodManager imm = (InputMethodManager) et_inspected_by.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_inspected_by.getWindowToken(), 0);
                obj.put("inspectorName", inspected_name);
                insp_comment = et_comment.getText().toString();

                //Comment
                obj.put("comments", insp_comment);
                InputMethodManager imm1 = (InputMethodManager) et_comment.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(et_comment.getWindowToken(), 0);
                obj.put("storeCode",store_Code);
                //Rating
                if(Math.round(cal_result) == 1)
                {
                    obj.put("rating","Need Improvement");
                }
                else if(Math.round(cal_result) == 2)
                {
                    obj.put("rating","Okay");
                }
                else if(Math.round(cal_result) == 3)
                {
                    obj.put("rating","Good");
                }
                else if(Math.round(cal_result) == 4)
                {
                    obj.put("rating","Excellent");
                }
//               // For Image
//              //  insp_imagePath = getStringImage(bitmap);
//                insp_imagePath = picturePath;

//                obj.put("storeImg", insp_imagePath);
                requestInspectionSubmitAPI(context,obj);
            }

        } catch (JSONException e)
        {
            e.printStackTrace();
            Toast.makeText(InspectionBeginActivity.this,e.getMessage(), Toast.LENGTH_LONG).show();

        }
    }

    private String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    //Inspection Submit API
    private void requestInspectionSubmitAPI(Context context1, JSONObject jsonarray) {
    {

            if (Reusable_Functions.chkStatus(context)) {
                Reusable_Functions.hDialog();
                Reusable_Functions.sDialog(context, "Submitting data…");
                Log.e("jsonarray storeinspection "," "+jsonarray);

                String url;
                url = ConstsCore.web_url + "/v1/save/storeinspection/submit/" + userId; // + "&storeCode=" + store_Code;//+"?recache="+recache
                Log.e("url storeinspection "," "+url);
                //Log.e("url storeinspection "," "+url);

                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, jsonarray.toString(),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.e("response storeinspection "," "+response);

                                try {
                                    if (response == null || response.equals("")) {
                                        Reusable_Functions.hDialog();
                                        Toast.makeText(context, "Sending data failed...", Toast.LENGTH_LONG).show();

                                    } else {
                                        String result = response.getString("status");
                                        Toast.makeText(context, "" + result, Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(InspectionBeginActivity.this, SnapDashboardActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);
                                        //Details.this.finish();
                                        Reusable_Functions.hDialog();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Reusable_Functions.hDialog();

                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "server not responding...", Toast.LENGTH_SHORT).show();
                                error.printStackTrace();
                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("Authorization", bearertoken);
                        return params;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json";
                    }

                };
                int socketTimeout = 60000;//5 seconds
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                postRequest.setRetryPolicy(policy);
                queue.add(postRequest);

            }
            else
            {
                Toast.makeText(context, "Please check network connection...", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(InspectionBeginActivity.this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    startCamera();
                } else if (options[item].equals("Choose from Gallery")) {
                    openGallery();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void openGallery() {
        if (Build.VERSION.SDK_INT < 23) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
        } else {
            int permissionCheck = ContextCompat.checkSelfPermission(InspectionBeginActivity.this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            } else {
                ActivityCompat.requestPermissions(InspectionBeginActivity.this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_R);
            }
        }
    }

    public void startCamera() {
        if (Build.VERSION.SDK_INT < 23) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 1);
        } else {

            int permissionCheck = ContextCompat.checkSelfPermission(InspectionBeginActivity.this,
                    android.Manifest.permission.CAMERA);


            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

                permissionCheck = ContextCompat.checkSelfPermission(InspectionBeginActivity.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

                //here
                int permissioncheckRead = ContextCompat.checkSelfPermission(InspectionBeginActivity.this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE);


                if (permissionCheck == PackageManager.PERMISSION_GRANTED && permissioncheckRead == PackageManager.PERMISSION_GRANTED) {
                    //Open Camera Here
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else {
                    //Get Permission for read and write
                    ActivityCompat.requestPermissions(InspectionBeginActivity.this,
                            new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_RWFRMCAM);
                }
            } else {
                ActivityCompat.requestPermissions(InspectionBeginActivity.this,
                        new String[]{android.Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            onCaptureImageResult(data);
        } else if (requestCode == 2) {
            if (data != null) {
                selectedImage = data.getData();
                String[] filePath1 = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath1, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath1[0]);
                picturePath = c.getString(columnIndex);
                c.close();
               // Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                image_upload.setVisibility(View.VISIBLE);
                Glide.with(InspectionBeginActivity.this)
                        .load(selectedImage)
                        .fitCenter()
                        .into(image_upload);

            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        if (data != null) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");
            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                picturePath = destination.getAbsolutePath();
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            image_upload.setVisibility(View.VISIBLE);
            image_upload.setImageBitmap(thumbnail);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}

