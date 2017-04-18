package apsupportapp.aperotechnologies.com.designapp.StoreInspection;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
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
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.json.JSONArray;
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


import apsupportapp.aperotechnologies.com.designapp.DashBoardActivity;

import apsupportapp.aperotechnologies.com.designapp.LoginActivity;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;

import static apsupportapp.aperotechnologies.com.designapp.HorlyAnalysis.Option_Fragment.view;

/**
 * Created by pamrutkar on 11/04/17.
 */
public class InspectionBeginActivity extends AppCompatActivity implements View.OnClickListener
{

    public static final int MY_PERMISSIONS_REQUEST_CAMERA=15; //1
    public static final int MY_PERMISSIONS_REQUEST_R=30; //2
    public static final int MY_PERMISSIONS_REQUEST_RWFRMCAM=60; //3
    Context context;
    // Emoji Declaration
    ImageView image_improvement_criteria_1,image_okay_criteria_1,image_good_criteria_1,image_excellent_criteria_1;
    ImageView image_improvement_criteria_2,image_okay_criteria_2,image_good_criteria_2,image_excellent_criteria_2;
    ImageView image_improvement_criteria_3,image_okay_criteria_3,image_good_criteria_3,image_excellent_criteria_3;
    ImageView image_improvement_criteria_4,image_okay_criteria_4,image_good_criteria_4,image_excellent_criteria_4;
    ImageView image_improvement_criteria_5,image_okay_criteria_5,image_good_criteria_5,image_excellent_criteria_5;
    ImageView image_improvement_criteria_6,image_okay_criteria_6,image_good_criteria_6,image_excellent_criteria_6;
    ImageView image_improvement_criteria_7,image_okay_criteria_7,image_good_criteria_7,image_excellent_criteria_7;
    ImageView image_improvement_criteria_8,image_okay_criteria_8,image_good_criteria_8,image_excellent_criteria_8;
    ImageView image_camera , image_upload;

    // Emoji Text Declaration
    TextView txt_improvement_criteria_1,txt_okay_criteria_1,txt_good_criteria_1,txt_excellent_criteria_1;
    TextView txt_improvement_criteria_2,txt_okay_criteria_2,txt_good_criteria_2,txt_excellent_criteria_2;
    TextView txt_improvement_criteria_3,txt_okay_criteria_3,txt_good_criteria_3,txt_excellent_criteria_3;
    TextView txt_improvement_criteria_4,txt_okay_criteria_4,txt_good_criteria_4,txt_excellent_criteria_4;
    TextView txt_improvement_criteria_5,txt_okay_criteria_5,txt_good_criteria_5,txt_excellent_criteria_5;
    TextView txt_improvement_criteria_6,txt_okay_criteria_6,txt_good_criteria_6,txt_excellent_criteria_6;
    TextView txt_improvement_criteria_7,txt_okay_criteria_7,txt_good_criteria_7,txt_excellent_criteria_7;
    TextView txt_improvement_criteria_8,txt_okay_criteria_8,txt_good_criteria_8,txt_excellent_criteria_8;

    EditText et_inspected_by,et_comment;
    Button btn_insp_submit;
    private RelativeLayout rel_cam_image;
    Uri selectedImage;
    private TextView txt_insp_date_Val;
    private String picturePath;
    private RelativeLayout inspection_btnback;
    private RequestQueue queue;
    private String bearertoken;
    private SharedPreferences sharedPreferences;
    private int fashionQuot,merchDisplay,merchPresentationStd,suggSellingByStaff,overallCleanliness,signage,winClusterMannequinsDisp;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_begin);
        getSupportActionBar().hide();
        context = this;
        initialise();
        getCurrentDate(view);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();


//        et_inspected_by.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//            Boolean handled = false;
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_NEXT) || (actionId == EditorInfo.IME_ACTION_NONE)) {
//                    et_inspected_by.clearFocus();
//                    InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//                    if(inputManager != null){
//                        inputManager.hideSoftInputFromWindow(et_inspected_by.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
//                    }
//                    handled = true;
//                }
//                return handled;
//            }
//
//        });
//        et_comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//            Boolean handled = false;
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_NEXT) || (actionId == EditorInfo.IME_ACTION_NONE) ) {
//                    et_comment.clearFocus();
//                    InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//                    if(inputManager != null){
//                        inputManager.hideSoftInputFromWindow(et_comment.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
//                    }
//                    handled = true;
//                }
//                return handled;
//            }
//
//        });

    }
    public void getCurrentDate(View view) {
        Calendar calendar = Calendar.getInstance(); //dd / MM /yyyy
        SimpleDateFormat mdformat = new SimpleDateFormat("dd / MM /yyyy ");
        String strDate = mdformat.format(calendar.getTime());
        txt_insp_date_Val.setText(strDate);
    }

    private void initialise() 
    {
        //Improvement emoji image
        image_improvement_criteria_1 = (ImageView)findViewById(R.id.image_improvement_criteria_1);
        image_improvement_criteria_2 = (ImageView)findViewById(R.id.image_improvement_criteria_2);
        image_improvement_criteria_3 = (ImageView)findViewById(R.id.image_improvement_criteria_3);
        image_improvement_criteria_4 = (ImageView)findViewById(R.id.image_improvement_criteria_4);
        image_improvement_criteria_5 = (ImageView)findViewById(R.id.image_improvement_criteria_5);
        image_improvement_criteria_6 = (ImageView)findViewById(R.id.image_improvement_criteria_6);
        image_improvement_criteria_7 = (ImageView)findViewById(R.id.image_improvement_criteria_7);
        image_improvement_criteria_8 = (ImageView)findViewById(R.id.image_improvement_criteria_8);

        //Okay emoji image
        image_okay_criteria_1 = (ImageView)findViewById(R.id.image_okay_criteria_1);
        image_okay_criteria_2 = (ImageView)findViewById(R.id.image_okay_criteria_2);
        image_okay_criteria_3 = (ImageView)findViewById(R.id.image_okay_criteria_3);
        image_okay_criteria_4 = (ImageView)findViewById(R.id.image_okay_criteria_4);
        image_okay_criteria_5 = (ImageView)findViewById(R.id.image_okay_criteria_5);
        image_okay_criteria_6 = (ImageView)findViewById(R.id.image_okay_criteria_6);
        image_okay_criteria_7 = (ImageView)findViewById(R.id.image_okay_criteria_7);
        image_okay_criteria_8 = (ImageView)findViewById(R.id.image_okay_criteria_8);

        //Good Emoji image
        image_good_criteria_1 = (ImageView)findViewById(R.id.image_good_criteria_1);
        image_good_criteria_2 = (ImageView)findViewById(R.id.image_good_criteria_2);
        image_good_criteria_3 = (ImageView)findViewById(R.id.image_good_criteria_3);
        image_good_criteria_4 = (ImageView)findViewById(R.id.image_good_criteria_4);
        image_good_criteria_5 = (ImageView)findViewById(R.id.image_good_criteria_5);
        image_good_criteria_6 = (ImageView)findViewById(R.id.image_good_criteria_6);
        image_good_criteria_7 = (ImageView)findViewById(R.id.image_good_criteria_7);
        image_good_criteria_8 = (ImageView)findViewById(R.id.image_good_criteria_8);

        //excellent emoji image
        image_excellent_criteria_1 = (ImageView)findViewById(R.id.image_excellent_criteria_1);
        image_excellent_criteria_2 = (ImageView)findViewById(R.id.image_excellent_criteria_2);
        image_excellent_criteria_3 = (ImageView)findViewById(R.id.image_excellent_criteria_3);
        image_excellent_criteria_4 = (ImageView)findViewById(R.id.image_excellent_criteria_4);
        image_excellent_criteria_5 = (ImageView)findViewById(R.id.image_excellent_criteria_5);
        image_excellent_criteria_6 = (ImageView)findViewById(R.id.image_excellent_criteria_6);
        image_excellent_criteria_7 = (ImageView)findViewById(R.id.image_excellent_criteria_7);
        image_excellent_criteria_8 = (ImageView)findViewById(R.id.image_excellent_criteria_8);

        //Improvement emoji text
        txt_improvement_criteria_1 = (TextView)findViewById(R.id.txt_improvement_criteria_1);
        txt_improvement_criteria_2 = (TextView)findViewById(R.id.txt_improvement_criteria_2);
        txt_improvement_criteria_3 = (TextView)findViewById(R.id.txt_improvement_criteria_3);
        txt_improvement_criteria_4 = (TextView)findViewById(R.id.txt_improvement_criteria_4);
        txt_improvement_criteria_5 = (TextView)findViewById(R.id.txt_improvement_criteria_5);
        txt_improvement_criteria_6 = (TextView)findViewById(R.id.txt_improvement_criteria_6);
        txt_improvement_criteria_7 = (TextView)findViewById(R.id.txt_improvement_criteria_7);
        txt_improvement_criteria_8 = (TextView)findViewById(R.id.txt_improvement_criteria_8);

        //Okay emoji text
        txt_okay_criteria_1 = (TextView)findViewById(R.id.txt_okay_criteria_1);
        txt_okay_criteria_2 = (TextView)findViewById(R.id.txt_okay_criteria_2);
        txt_okay_criteria_3 = (TextView)findViewById(R.id.txt_okay_criteria_3);
        txt_okay_criteria_4 = (TextView)findViewById(R.id.txt_okay_criteria_4);
        txt_okay_criteria_5 = (TextView)findViewById(R.id.txt_okay_criteria_5);
        txt_okay_criteria_6 = (TextView)findViewById(R.id.txt_okay_criteria_6);
        txt_okay_criteria_7 = (TextView)findViewById(R.id.txt_okay_criteria_7);
        txt_okay_criteria_8 = (TextView)findViewById(R.id.txt_okay_criteria_8);

        //Good Emoji text
        txt_good_criteria_1 = (TextView)findViewById(R.id.txt_good_criteria_1);
        txt_good_criteria_2 = (TextView)findViewById(R.id.txt_good_criteria_2);
        txt_good_criteria_3 = (TextView)findViewById(R.id.txt_good_criteria_3);
        txt_good_criteria_4 = (TextView)findViewById(R.id.txt_good_criteria_4);
        txt_good_criteria_5 = (TextView)findViewById(R.id.txt_good_criteria_5);
        txt_good_criteria_6 = (TextView)findViewById(R.id.txt_good_criteria_6);
        txt_good_criteria_7 = (TextView)findViewById(R.id.txt_good_criteria_7);
        txt_good_criteria_8 = (TextView)findViewById(R.id.txt_good_criteria_8);

        //excellent emoji text
        txt_excellent_criteria_1 = (TextView)findViewById(R.id.txt_excellent_criteria_1);
        txt_excellent_criteria_2 = (TextView)findViewById(R.id.txt_excellent_criteria_2);
        txt_excellent_criteria_3 = (TextView)findViewById(R.id.txt_excellent_criteria_3);
        txt_excellent_criteria_4 = (TextView)findViewById(R.id.txt_excellent_criteria_4);
        txt_excellent_criteria_5 = (TextView)findViewById(R.id.txt_excellent_criteria_5);
        txt_excellent_criteria_6 = (TextView)findViewById(R.id.txt_excellent_criteria_6);
        txt_excellent_criteria_7 = (TextView)findViewById(R.id.txt_excellent_criteria_7);
        txt_excellent_criteria_8 = (TextView)findViewById(R.id.txt_excellent_criteria_8);

        //Edittext
        et_inspected_by = (EditText)findViewById(R.id.et_inspected_by);
        et_comment = (EditText)findViewById(R.id.et_comment);
        rel_cam_image = (RelativeLayout)findViewById(R.id.rel_cam_image);
        image_camera = (ImageView)findViewById(R.id.camera_imageView);
        image_upload = (ImageView)findViewById(R.id.camera_imageView1);
        image_upload.setVisibility(View.INVISIBLE);
        txt_insp_date_Val = (TextView)findViewById(R.id.txt_insp_date_Val);
        inspection_btnback = (RelativeLayout)findViewById(R.id.inspection_btnback);

        btn_insp_submit = (Button)findViewById(R.id.btn_insp_submit);

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
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.image_improvement_criteria_1:
                if(image_improvement_criteria_1.isClickable())
                {
                    image_improvement_criteria_1.setBackgroundResource(R.mipmap.improvementemojiselected);
                    txt_improvement_criteria_1.setText("Need Improvement");
                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .playOn(findViewById(R.id.image_improvement_criteria_1));
//                    image_improvement_criteria_1.getLayoutParams().height = 80;
//                    image_improvement_criteria_1.getLayoutParams().width = 80;

//                    LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) image_improvement_criteria_1.getLayoutParams();
//                    params1.height = this.getResources().getDimensionPixelSize(R.dimen.item_height);
//                    params1.width = this.getResources().getDimensionPixelSize(R.dimen.item_width);
                    image_good_criteria_1.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_okay_criteria_1.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_excellent_criteria_1.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    txt_good_criteria_1.setText("");
                    txt_excellent_criteria_1.setText("");
                    txt_okay_criteria_1.setText("");
                    fashionQuot = 1;

                }
                else
                {
                    image_improvement_criteria_1.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    txt_improvement_criteria_1.setText("");
                }
                break;

            case R.id.image_improvement_criteria_2:
                if(image_improvement_criteria_2.isClickable())
                {
                    image_improvement_criteria_2.setBackgroundResource(R.mipmap.improvementemojiselected);
                    txt_improvement_criteria_2.setText("Need Improvement");
                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .playOn(findViewById(R.id.image_improvement_criteria_2));
//                    LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) image_improvement_criteria_1.getLayoutParams();
//                    params1.height = this.getResources().getDimensionPixelSize(R.dimen.item_height);
//                    params1.width = this.getResources().getDimensionPixelSize(R.dimen.item_width);
                    image_good_criteria_2.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_okay_criteria_2.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_excellent_criteria_2.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    txt_good_criteria_2.setText("");
                    txt_excellent_criteria_2.setText("");
                    txt_okay_criteria_2.setText("");
                    merchDisplay = 1;
                }
                else
                {
                    image_improvement_criteria_2.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    txt_improvement_criteria_2.setText("");
                }
                break;

            case R.id.image_improvement_criteria_3:
                if(image_improvement_criteria_3.isClickable())
                {
                    image_improvement_criteria_3.setBackgroundResource(R.mipmap.improvementemojiselected);
                    txt_improvement_criteria_3.setText("Need Improvement");
                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .playOn(findViewById(R.id.image_improvement_criteria_3));
//                    LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) image_improvement_criteria_1.getLayoutParams();
//                    params1.height = this.getResources().getDimensionPixelSize(R.dimen.item_height);
//                    params1.width = this.getResources().getDimensionPixelSize(R.dimen.item_width);
                    image_good_criteria_3.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_okay_criteria_3.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_excellent_criteria_3.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    txt_good_criteria_3.setText("");
                    txt_excellent_criteria_3.setText("");
                    txt_okay_criteria_3.setText("");
                    merchPresentationStd = 3;
                }
                else
                {
                    image_improvement_criteria_3.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    txt_improvement_criteria_3.setText("");
                }
                break;


            case R.id.image_improvement_criteria_4:

                if(image_improvement_criteria_4.isClickable())
                {

                    image_improvement_criteria_4.setBackgroundResource(R.mipmap.improvementemojiselected);
                    txt_improvement_criteria_4.setText("Need Improvement");
                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .playOn(findViewById(R.id.image_improvement_criteria_4));
//                  LinearLayout.LayoutParams params4 = (LinearLayout.LayoutParams) image_improvement_criteria_1.getLayoutParams();
//                  params4.height = this.getResources().getDimensionPixelSize(R.dimen.item_height);
//                  params4.width = this.getResources().getDimensionPixelSize(R.dimen.item_width);
                    image_good_criteria_4.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_okay_criteria_4.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_excellent_criteria_4.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    txt_good_criteria_4.setText("");
                    txt_excellent_criteria_4.setText("");
                    txt_okay_criteria_4.setText("");
                    suggSellingByStaff = 1;

                }
                else
                {
                  //  image_improvement_criteria_4.setClickable(false);
                    image_improvement_criteria_4.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    txt_improvement_criteria_4.setText("");
                }

                break;

            case R.id.image_improvement_criteria_5:

                if(image_improvement_criteria_5.isClickable())
                {

                    image_improvement_criteria_5.setBackgroundResource(R.mipmap.improvementemojiselected);
                    txt_improvement_criteria_5.setText("Need Improvement");
                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .playOn(findViewById(R.id.image_improvement_criteria_5));
//                  LinearLayout.LayoutParams params5 = (LinearLayout.LayoutParams) image_improvement_criteria_1.getLayoutParams();
//                  params5.height = this.getResources().getDimensionPixelSize(R.dimen.item_height);
//                  params5.width = this.getResources().getDimensionPixelSize(R.dimen.item_width);

                    image_good_criteria_5.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_okay_criteria_5.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_excellent_criteria_5.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    txt_good_criteria_5.setText("");
                    txt_excellent_criteria_5.setText("");
                    txt_okay_criteria_5.setText("");
                    overallCleanliness = 1;
                }
                else
                {
                  //  image_improvement_criteria_5.setClickable(false);
                    image_improvement_criteria_5.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    txt_improvement_criteria_5.setText("");
                }

                break;

            case R.id.image_improvement_criteria_6:

                if(image_improvement_criteria_6.isClickable())
                {

                    image_improvement_criteria_6.setBackgroundResource(R.mipmap.improvementemojiselected);
                    txt_improvement_criteria_6.setText("Need Improvement");
                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .playOn(findViewById(R.id.image_improvement_criteria_6));
//                LinearLayout.LayoutParams params6 = (LinearLayout.LayoutParams) image_improvement_criteria_1.getLayoutParams();
//                params6.height = this.getResources().getDimensionPixelSize(R.dimen.item_height);
//                params6.width = this.getResources().getDimensionPixelSize(R.dimen.item_width);

                    image_good_criteria_6.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_okay_criteria_6.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_excellent_criteria_6.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    txt_good_criteria_6.setText("");
                    txt_excellent_criteria_6.setText("");
                    txt_okay_criteria_6.setText("");
                    signage = 1;
                }
                else
                {
                   // image_improvement_criteria_6.setClickable(false);
                    image_improvement_criteria_6.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    txt_improvement_criteria_6.setText("");
                }

                break;

            case R.id.image_improvement_criteria_7:

                if(image_improvement_criteria_7.isClickable())
                {

                    image_improvement_criteria_7.setBackgroundResource(R.mipmap.improvementemojiselected);
                    txt_improvement_criteria_7.setText("Need Improvement");
                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .playOn(findViewById(R.id.image_improvement_criteria_7));
//                LinearLayout.LayoutParams params7 = (LinearLayout.LayoutParams) image_improvement_criteria_1.getLayoutParams();
//                params7.height = this.getResources().getDimensionPixelSize(R.dimen.item_height);
//                params7.width = this.getResources().getDimensionPixelSize(R.dimen.item_width);
                    image_good_criteria_7.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_okay_criteria_7.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_excellent_criteria_7.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    txt_good_criteria_7.setText("");
                    txt_excellent_criteria_7.setText("");
                    txt_okay_criteria_7.setText("");

                }
                else
                {
                   // image_improvement_criteria_7.setClickable(false);
                    image_improvement_criteria_7.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    txt_improvement_criteria_7.setText("");
                }


                break;

            case R.id.image_improvement_criteria_8:

                if(image_improvement_criteria_8.isClickable())
                {

                    image_improvement_criteria_8.setBackgroundResource(R.mipmap.improvementemojiselected);
                    txt_improvement_criteria_8.setText("Need Improvement");
                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .playOn(findViewById(R.id.image_improvement_criteria_8));
//                  LinearLayout.LayoutParams params8 = (LinearLayout.LayoutParams) image_improvement_criteria_1.getLayoutParams();
//                  params8.height = this.getResources().getDimensionPixelSize(R.dimen.item_height);
//                  params8.width = this.getResources().getDimensionPixelSize(R.dimen.item_width);
                    image_good_criteria_8.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_okay_criteria_8.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_excellent_criteria_8.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    txt_good_criteria_8.setText("");
                    txt_excellent_criteria_8.setText("");
                    txt_okay_criteria_8.setText("");
                    winClusterMannequinsDisp = 1;
                }
                else
                {
                  //  image_improvement_criteria_8.setClickable(false);
                    image_improvement_criteria_8.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    txt_improvement_criteria_8.setText("");
                }

                break;

            case R.id.image_okay_criteria_1:

                if(image_okay_criteria_1.isClickable())
                {

                    image_improvement_criteria_1.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_good_criteria_1.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_excellent_criteria_1.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    image_okay_criteria_1.setBackgroundResource(R.mipmap.okayemojiselected);
                    txt_okay_criteria_1.setText("Okay");
                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .playOn(findViewById(R.id.image_okay_criteria_1));
//                    LinearLayout.LayoutParams params_1 = (LinearLayout.LayoutParams) image_okay_criteria_1.getLayoutParams();
//                    params_1.height = this.getResources().getDimensionPixelSize(R.dimen.item_height);
//                    params_1.width = this.getResources().getDimensionPixelSize(R.dimen.item_width);
                    txt_good_criteria_1.setText("");
                    txt_excellent_criteria_1.setText("");
                    txt_improvement_criteria_1.setText("");
                    fashionQuot =2;

                }
                else
                {
                  //  image_okay_criteria_1.setClickable(false);
                    image_okay_criteria_1.setBackgroundResource(R.mipmap.okayemojiunselected);
                    txt_okay_criteria_1.setText("");
                }

               break;

            case R.id.image_okay_criteria_2:
                if(image_okay_criteria_2.isClickable())
                {

                    image_improvement_criteria_2.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_good_criteria_2.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_excellent_criteria_2.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    image_okay_criteria_2.setBackgroundResource(R.mipmap.okayemojiselected);
                    txt_okay_criteria_2.setText("Okay");
                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .playOn(findViewById(R.id.image_okay_criteria_2));
//                    LinearLayout.LayoutParams params_2 = (LinearLayout.LayoutParams) image_okay_criteria_2.getLayoutParams();
//                    params_2.height = this.getResources().getDimensionPixelSize(R.dimen.item_height);
//                    params_2.width = this.getResources().getDimensionPixelSize(R.dimen.item_width);
                    txt_good_criteria_2.setText("");
                    txt_excellent_criteria_2.setText("");
                    txt_improvement_criteria_2.setText("");
                     merchDisplay = 2;
                }
                else
                {
                   // image_okay_criteria_2.setClickable(false);
                    image_okay_criteria_2.setBackgroundResource(R.mipmap.okayemojiunselected);
                    txt_okay_criteria_2.setText("");
                }


                break;

            case R.id.image_okay_criteria_3:
                if(image_okay_criteria_3.isClickable())
                {

                    image_improvement_criteria_3.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_good_criteria_3.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_excellent_criteria_3.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    image_okay_criteria_3.setBackgroundResource(R.mipmap.okayemojiselected);
                    txt_okay_criteria_3.setText("Okay");
                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .playOn(findViewById(R.id.image_okay_criteria_3));
//                    LinearLayout.LayoutParams params_3 = (LinearLayout.LayoutParams) image_okay_criteria_3.getLayoutParams();
//                    params_3.height = this.getResources().getDimensionPixelSize(R.dimen.item_height);
//                    params_3.width = this.getResources().getDimensionPixelSize(R.dimen.item_width);
                    txt_good_criteria_3.setText("");
                    txt_excellent_criteria_3.setText("");
                    txt_improvement_criteria_3.setText("");
                    merchPresentationStd = 2;

                }
                else
                {
                  //  image_okay_criteria_3.setClickable(false);
                    image_okay_criteria_3.setBackgroundResource(R.mipmap.okayemojiunselected);
                    txt_okay_criteria_3.setText("");
                }

                break;

            case R.id.image_okay_criteria_4:

                if(image_okay_criteria_4.isClickable())
                {

                    image_improvement_criteria_4.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_good_criteria_4.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_excellent_criteria_4.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    image_okay_criteria_4.setBackgroundResource(R.mipmap.okayemojiselected);
                    txt_okay_criteria_4.setText("Okay");
                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .playOn(findViewById(R.id.image_okay_criteria_4));
//                    LinearLayout.LayoutParams params_4 = (LinearLayout.LayoutParams) image_okay_criteria_4.getLayoutParams();
//                    params_4.height = this.getResources().getDimensionPixelSize(R.dimen.item_height);
//                    params_4.width = this.getResources().getDimensionPixelSize(R.dimen.item_width);
                    txt_good_criteria_4.setText("");
                    txt_excellent_criteria_4.setText("");
                    txt_improvement_criteria_4.setText("");
                    suggSellingByStaff = 2;

                }
                else
                {
                   // image_okay_criteria_4.setClickable(false);
                    image_okay_criteria_4.setBackgroundResource(R.mipmap.okayemojiunselected);
                    txt_okay_criteria_4.setText("");
                }
                break;

            case R.id.image_okay_criteria_5:

                if(image_okay_criteria_5.isClickable())
                {

                    image_improvement_criteria_5.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_good_criteria_5.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_excellent_criteria_5.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    image_okay_criteria_5.setBackgroundResource(R.mipmap.okayemojiselected);
                    txt_okay_criteria_5.setText("Okay");
                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .playOn(findViewById(R.id.image_okay_criteria_5));
//                    LinearLayout.LayoutParams params_5 = (LinearLayout.LayoutParams) image_okay_criteria_5.getLayoutParams();
//                    params_5.height = this.getResources().getDimensionPixelSize(R.dimen.item_height);
//                    params_5.width = this.getResources().getDimensionPixelSize(R.dimen.item_width);
                    txt_good_criteria_5.setText("");
                    txt_excellent_criteria_5.setText("");
                    txt_improvement_criteria_5.setText("");
                    overallCleanliness = 2;
                }
                else
                {
                   // image_okay_criteria_5.setClickable(false);
                    image_okay_criteria_5.setBackgroundResource(R.mipmap.okayemojiunselected);
                    txt_okay_criteria_5.setText("");
                }


                break;

            case R.id.image_okay_criteria_6:
                if(image_okay_criteria_6.isClickable())
                {

                    image_improvement_criteria_6.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_good_criteria_6.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_excellent_criteria_6.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    image_okay_criteria_6.setBackgroundResource(R.mipmap.okayemojiselected);
                    txt_okay_criteria_6.setText("Okay");
                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .playOn(findViewById(R.id.image_okay_criteria_6));
//                    LinearLayout.LayoutParams params_6 = (LinearLayout.LayoutParams) image_okay_criteria_6.getLayoutParams();
//                    params_6.height = this.getResources().getDimensionPixelSize(R.dimen.item_height);
//                    params_6.width = this.getResources().getDimensionPixelSize(R.dimen.item_width);
                    txt_good_criteria_6.setText("");
                    txt_excellent_criteria_6.setText("");
                    txt_improvement_criteria_6.setText("");
                    signage = 2;

                }
                else {
                   // image_okay_criteria_6.setClickable(false);
                    image_okay_criteria_6.setBackgroundResource(R.mipmap.okayemojiunselected);
                    txt_okay_criteria_6.setText("");
                }



                break;

            case R.id.image_okay_criteria_7:
                if(image_okay_criteria_7.isClickable())
                {

                    image_improvement_criteria_7.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_good_criteria_7.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_excellent_criteria_7.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    image_okay_criteria_7.setBackgroundResource(R.mipmap.okayemojiselected);
                    txt_okay_criteria_7.setText("Okay");
                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .playOn(findViewById(R.id.image_okay_criteria_7));
//                    LinearLayout.LayoutParams params_7 = (LinearLayout.LayoutParams) image_okay_criteria_7.getLayoutParams();
//                    params_7.height = this.getResources().getDimensionPixelSize(R.dimen.item_height);
//                    params_7.width = this.getResources().getDimensionPixelSize(R.dimen.item_width);
                    txt_good_criteria_7.setText("");
                    txt_excellent_criteria_7.setText("");
                    txt_improvement_criteria_7.setText("");
                }
                else
                {
                   // image_okay_criteria_7.setClickable(false);
                    image_okay_criteria_7.setBackgroundResource(R.mipmap.okayemojiunselected);
                    txt_okay_criteria_7.setText("");
                }


                break;

            case R.id.image_okay_criteria_8:

                if(image_okay_criteria_8.isClickable())
                {

                    image_improvement_criteria_8.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_good_criteria_8.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_excellent_criteria_8.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    image_okay_criteria_8.setBackgroundResource(R.mipmap.okayemojiselected);
                    txt_okay_criteria_8.setText("Okay");
                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .playOn(findViewById(R.id.image_okay_criteria_8));
//                    LinearLayout.LayoutParams params_8 = (LinearLayout.LayoutParams) image_okay_criteria_8.getLayoutParams();
//                    params_8.height = this.getResources().getDimensionPixelSize(R.dimen.item_height);
//                    params_8.width = this.getResources().getDimensionPixelSize(R.dimen.item_width);
                    txt_good_criteria_8.setText("");
                    txt_excellent_criteria_8.setText("");
                    txt_improvement_criteria_8.setText("");
                    winClusterMannequinsDisp = 2;

                }
                else
                {
                   // image_okay_criteria_8.setClickable(false);
                    image_okay_criteria_8.setBackgroundResource(R.mipmap.okayemojiunselected);
                    txt_okay_criteria_8.setText("");
                }


                break;

            case R.id.image_good_criteria_1:

                if(image_good_criteria_1.isClickable())
                {

                    image_improvement_criteria_1.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_okay_criteria_1.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_excellent_criteria_1.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    image_good_criteria_1.setBackgroundResource(R.mipmap.goodemojiselected);
                    txt_good_criteria_1.setText("Good");
                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .playOn(findViewById(R.id.image_good_criteria_1));
//                    LinearLayout.LayoutParams param1 = (LinearLayout.LayoutParams) image_good_criteria_1.getLayoutParams();
//                    param1.height = this.getResources().getDimensionPixelSize(R.dimen.item_height);
//                    param1.width = this.getResources().getDimensionPixelSize(R.dimen.item_width);
                    txt_okay_criteria_1.setText("");
                    txt_excellent_criteria_1.setText("");
                    txt_improvement_criteria_1.setText("");
                    fashionQuot = 3;

                }
                else
                {
                   // image_good_criteria_1.setClickable(false);
                    image_good_criteria_1.setBackgroundResource(R.mipmap.goodemojiunselected);
                    txt_good_criteria_1.setText("");

                }

                break;

            case R.id.image_good_criteria_2:
                if(image_good_criteria_2.isClickable())
                {

                    image_improvement_criteria_2.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_okay_criteria_2.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_excellent_criteria_2.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    image_good_criteria_2.setBackgroundResource(R.mipmap.goodemojiselected);
                    txt_good_criteria_2.setText("Good");
                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .playOn(findViewById(R.id.image_good_criteria_2));
//                    LinearLayout.LayoutParams param2 = (LinearLayout.LayoutParams) image_good_criteria_2.getLayoutParams();
//                    param2.height = this.getResources().getDimensionPixelSize(R.dimen.item_height);
//                    param2.width = this.getResources().getDimensionPixelSize(R.dimen.item_width);
                    txt_okay_criteria_2.setText("");
                    txt_excellent_criteria_2.setText("");
                    txt_improvement_criteria_2.setText("");
                    merchDisplay = 3;
                }
                else
                {
                   // image_good_criteria_2.setClickable(false);
                    image_good_criteria_2.setBackgroundResource(R.mipmap.goodemojiunselected);
                    txt_good_criteria_2.setText("");
                }


                break;
            case R.id.image_good_criteria_3:

                if(image_good_criteria_3.isClickable())
                {

                    image_improvement_criteria_3.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_okay_criteria_3.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_excellent_criteria_3.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    image_good_criteria_3.setBackgroundResource(R.mipmap.goodemojiselected);
                    txt_good_criteria_3.setText("Good");
                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .playOn(findViewById(R.id.image_good_criteria_3));
//                    LinearLayout.LayoutParams param3 = (LinearLayout.LayoutParams) image_good_criteria_3.getLayoutParams();
//                    param3.height = this.getResources().getDimensionPixelSize(R.dimen.item_height);
//                    param3.width = this.getResources().getDimensionPixelSize(R.dimen.item_width);
                    txt_okay_criteria_3.setText("");
                    txt_excellent_criteria_3.setText("");
                    txt_improvement_criteria_3.setText("");
                    merchPresentationStd = 3;
                }
                else
                {
                   // image_good_criteria_3.setClickable(false);
                    image_good_criteria_3.setBackgroundResource(R.mipmap.goodemojiunselected);
                    txt_good_criteria_3.setText("");
                }

                break;

            case R.id.image_good_criteria_4:

                if(image_good_criteria_4.isClickable())
                {

                    image_improvement_criteria_4.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_okay_criteria_4.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_excellent_criteria_4.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    image_good_criteria_4.setBackgroundResource(R.mipmap.goodemojiselected);
                    txt_good_criteria_4.setText("Good");
                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .playOn(findViewById(R.id.image_good_criteria_4));
//                    LinearLayout.LayoutParams param4 = (LinearLayout.LayoutParams) image_good_criteria_4.getLayoutParams();
//                    param4.height = this.getResources().getDimensionPixelSize(R.dimen.item_height);
//                    param4.width = this.getResources().getDimensionPixelSize(R.dimen.item_width);
                    txt_okay_criteria_4.setText("");
                    txt_excellent_criteria_4.setText("");
                    txt_improvement_criteria_4.setText("");
                    suggSellingByStaff = 3;
                }
                else
                {
                   // image_good_criteria_4.setClickable(false);
                    image_good_criteria_4.setBackgroundResource(R.mipmap.goodemojiunselected);
                    txt_good_criteria_4.setText("");
                }

                break;

            case R.id.image_good_criteria_5:

                if(image_good_criteria_5.isClickable())
                {

                    image_improvement_criteria_5.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_okay_criteria_5.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_excellent_criteria_5.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    image_good_criteria_5.setBackgroundResource(R.mipmap.goodemojiselected);
                    txt_good_criteria_5.setText("Good");
                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .playOn(findViewById(R.id.image_good_criteria_5));
//                    LinearLayout.LayoutParams param5 = (LinearLayout.LayoutParams) image_good_criteria_5.getLayoutParams();
//                    param5.height = this.getResources().getDimensionPixelSize(R.dimen.item_height);
//                    param5.width = this.getResources().getDimensionPixelSize(R.dimen.item_width);
                    txt_okay_criteria_5.setText("");
                    txt_excellent_criteria_5.setText("");
                    txt_improvement_criteria_5.setText("");
                    overallCleanliness = 3;


                }
                else
                {
                  //  image_good_criteria_5.setClickable(false);
                    image_good_criteria_5.setBackgroundResource(R.mipmap.goodemojiunselected);
                    txt_good_criteria_5.setText("");
                }

                break;

            case R.id.image_good_criteria_6:

                if(image_good_criteria_6.isClickable())
                {

                    image_improvement_criteria_6.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_okay_criteria_6.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_excellent_criteria_6.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    image_good_criteria_6.setBackgroundResource(R.mipmap.goodemojiselected);
                    txt_good_criteria_6.setText("Good");
                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .playOn(findViewById(R.id.image_good_criteria_6));
//                    LinearLayout.LayoutParams param6 = (LinearLayout.LayoutParams) image_good_criteria_6.getLayoutParams();
//                    param6.height = this.getResources().getDimensionPixelSize(R.dimen.item_height);
//                    param6.width = this.getResources().getDimensionPixelSize(R.dimen.item_width);
                    txt_okay_criteria_6.setText("");
                    txt_excellent_criteria_6.setText("");
                    txt_improvement_criteria_6.setText("");
                    signage = 3;
                }
                else
                {
                   // image_good_criteria_6.setClickable(false);
                    image_good_criteria_6.setBackgroundResource(R.mipmap.goodemojiunselected);
                    txt_good_criteria_6.setText("");
                }

                break;

            case R.id.image_good_criteria_7:

                if(image_good_criteria_7.isClickable())
                {

                    image_improvement_criteria_7.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_okay_criteria_7.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_excellent_criteria_7.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    image_good_criteria_7.setBackgroundResource(R.mipmap.goodemojiselected);
                    txt_good_criteria_7.setText("Good");
                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .playOn(findViewById(R.id.image_good_criteria_7));
//                    LinearLayout.LayoutParams param7 = (LinearLayout.LayoutParams) image_good_criteria_7.getLayoutParams();
//                    param7.height = this.getResources().getDimensionPixelSize(R.dimen.item_height);
//                    param7.width = this.getResources().getDimensionPixelSize(R.dimen.item_width);
                    txt_okay_criteria_7.setText("");
                    txt_excellent_criteria_7.setText("");
                    txt_improvement_criteria_7.setText("");


                }
                else
                {
                   // image_good_criteria_7.setClickable(false);
                    image_good_criteria_7.setBackgroundResource(R.mipmap.goodemojiunselected);
                    txt_good_criteria_7.setText("");
                }

                break;

            case R.id.image_good_criteria_8:

                if(image_good_criteria_8.isClickable())
                {

                    image_improvement_criteria_8.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_okay_criteria_8.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_excellent_criteria_8.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    image_good_criteria_8.setBackgroundResource(R.mipmap.goodemojiselected);
                    txt_good_criteria_8.setText("Good");
                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .playOn(findViewById(R.id.image_good_criteria_8));
//                    LinearLayout.LayoutParams param8 = (LinearLayout.LayoutParams) image_good_criteria_8.getLayoutParams();
//                    param8.height = this.getResources().getDimensionPixelSize(R.dimen.item_height);
//                    param8.width = this.getResources().getDimensionPixelSize(R.dimen.item_width);
                    txt_okay_criteria_8.setText("");
                    txt_excellent_criteria_8.setText("");
                    txt_improvement_criteria_8.setText("");
                    winClusterMannequinsDisp = 3;


                }
                else
                {
                   // image_good_criteria_8.setClickable(false);
                    image_good_criteria_8.setBackgroundResource(R.mipmap.goodemojiunselected);
                    txt_good_criteria_8.setText("");
                }


                break;

            case R.id.image_excellent_criteria_1:

                if(image_excellent_criteria_1.isClickable())
                {

                    image_improvement_criteria_1.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_okay_criteria_1.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_good_criteria_1.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_excellent_criteria_1.setBackgroundResource(R.mipmap.excellentemojiselected);
                    txt_excellent_criteria_1.setText("Excellent");
                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .playOn(findViewById(R.id.image_excellent_criteria_1));
//                    LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) image_excellent_criteria_1.getLayoutParams();
//                    layoutParams1.height = this.getResources().getDimensionPixelSize(R.dimen.item_height);
//                    layoutParams1.width = this.getResources().getDimensionPixelSize(R.dimen.item_width);
                    txt_okay_criteria_1.setText("");
                    txt_good_criteria_1.setText("");
                    txt_improvement_criteria_1.setText("");
                }
                else
                {
                  //  image_excellent_criteria_1.setClickable(false);
                    image_excellent_criteria_1.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    txt_excellent_criteria_1.setText("");
                }
                break;

            case R.id.image_excellent_criteria_2:

                if(image_excellent_criteria_2.isClickable())
                {
                    image_improvement_criteria_2.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_okay_criteria_2.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_good_criteria_2.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_excellent_criteria_2.setBackgroundResource(R.mipmap.excellentemojiselected);
                    txt_excellent_criteria_2.setText("Excellent");
                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .playOn(findViewById(R.id.image_excellent_criteria_2));
//                    LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) image_excellent_criteria_2.getLayoutParams();
//                    layoutParams2.height = this.getResources().getDimensionPixelSize(R.dimen.item_height);
//                    layoutParams2.width = this.getResources().getDimensionPixelSize(R.dimen.item_width);
                    txt_okay_criteria_2.setText("");
                    txt_good_criteria_2.setText("");
                    txt_improvement_criteria_2.setText("");
                }
                else
                {
                  //  image_excellent_criteria_2.setClickable(false);
                    image_excellent_criteria_2.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    txt_excellent_criteria_2.setText("");
                }
                break;

            case R.id.image_excellent_criteria_3:

                if(image_excellent_criteria_3.isClickable())
                {
                    image_improvement_criteria_3.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_okay_criteria_3.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_good_criteria_3.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_excellent_criteria_3.setBackgroundResource(R.mipmap.excellentemojiselected);
                    txt_excellent_criteria_3.setText("Excellent");
                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .playOn(findViewById(R.id.image_excellent_criteria_3));
//                    LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) image_excellent_criteria_3.getLayoutParams();
//                    layoutParams3.height = this.getResources().getDimensionPixelSize(R.dimen.item_height);
//                    layoutParams3.width = this.getResources().getDimensionPixelSize(R.dimen.item_width);
                    txt_okay_criteria_3.setText("");
                    txt_good_criteria_3.setText("");
                    txt_improvement_criteria_3.setText("");
                }
                else
                {
                  //  image_excellent_criteria_3.setClickable(false);
                    image_excellent_criteria_3.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    txt_excellent_criteria_3.setText("");
                }
                break;

            case R.id.image_excellent_criteria_4:
                if(image_excellent_criteria_4.isClickable())
                {

                    image_improvement_criteria_4.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_okay_criteria_4.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_good_criteria_4.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_excellent_criteria_4.setBackgroundResource(R.mipmap.excellentemojiselected);
                    txt_excellent_criteria_4.setText("Excellent");
                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .playOn(findViewById(R.id.image_excellent_criteria_4));
//                    LinearLayout.LayoutParams layoutParams4 = (LinearLayout.LayoutParams) image_excellent_criteria_4.getLayoutParams();
//                    layoutParams4.height = this.getResources().getDimensionPixelSize(R.dimen.item_height);
//                    layoutParams4.width = this.getResources().getDimensionPixelSize(R.dimen.item_width);
                    txt_okay_criteria_4.setText("");
                    txt_good_criteria_4.setText("");
                    txt_improvement_criteria_4.setText("");

                }
                else
                {
                  //  image_excellent_criteria_4.setClickable(false);
                    image_excellent_criteria_4.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    txt_excellent_criteria_4.setText("");
                }
                break;

            case R.id.image_excellent_criteria_5:

                if(image_excellent_criteria_5.isClickable())
                {
                    image_improvement_criteria_5.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_okay_criteria_5.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_good_criteria_5.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_excellent_criteria_5.setBackgroundResource(R.mipmap.excellentemojiselected);
                    txt_excellent_criteria_5.setText("Excellent");
                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .playOn(findViewById(R.id.image_excellent_criteria_5));
//                    LinearLayout.LayoutParams layoutParams5 = (LinearLayout.LayoutParams) image_excellent_criteria_4.getLayoutParams();
//                    layoutParams5.height = this.getResources().getDimensionPixelSize(R.dimen.item_height);
//                    layoutParams5.width = this.getResources().getDimensionPixelSize(R.dimen.item_width);
                    txt_okay_criteria_5.setText("");
                    txt_good_criteria_5.setText("");
                    txt_improvement_criteria_5.setText("");
                }
                else
                {
                   // image_excellent_criteria_5.setClickable(false);
                    image_excellent_criteria_5.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    txt_excellent_criteria_5.setText("");
                }
                break;

            case R.id.image_excellent_criteria_6:

                if(image_excellent_criteria_6.isClickable())
                {
                    image_improvement_criteria_6.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_okay_criteria_6.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_good_criteria_6.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_excellent_criteria_6.setBackgroundResource(R.mipmap.excellentemojiselected);
                    txt_excellent_criteria_6.setText("Excellent");
                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .playOn(findViewById(R.id.image_excellent_criteria_6));
//                    LinearLayout.LayoutParams layoutParams6 = (LinearLayout.LayoutParams) image_excellent_criteria_6.getLayoutParams();
//                    layoutParams6.height = this.getResources().getDimensionPixelSize(R.dimen.item_height);
//                    layoutParams6.width = this.getResources().getDimensionPixelSize(R.dimen.item_width);
                    txt_okay_criteria_6.setText("");
                    txt_good_criteria_6.setText("");
                    txt_improvement_criteria_6.setText("");
                }
                else
                {
                  //  image_excellent_criteria_6.setClickable(false);
                    image_excellent_criteria_6.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    txt_excellent_criteria_6.setText("");
                }

                break;

            case R.id.image_excellent_criteria_7:

                if(image_excellent_criteria_7.isClickable())
                {
                    image_improvement_criteria_7.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_okay_criteria_7.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_good_criteria_7.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_excellent_criteria_7.setBackgroundResource(R.mipmap.excellentemojiselected);
                    txt_excellent_criteria_7.setText("Excellent");
                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .playOn(findViewById(R.id.image_excellent_criteria_7));
//                    LinearLayout.LayoutParams layoutParams7 = (LinearLayout.LayoutParams) image_excellent_criteria_7.getLayoutParams();
//                    layoutParams7.height = this.getResources().getDimensionPixelSize(R.dimen.item_height);
//                    layoutParams7.width = this.getResources().getDimensionPixelSize(R.dimen.item_width);
                    txt_okay_criteria_7.setText("");
                    txt_good_criteria_7.setText("");
                    txt_improvement_criteria_7.setText("");

                }
                else
                {
                  //  image_excellent_criteria_7.setClickable(false);
                    image_excellent_criteria_7.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    txt_excellent_criteria_7.setText("");
                }
                break;

            case R.id.image_excellent_criteria_8:

                if(image_excellent_criteria_8.isClickable())
                {
                    image_improvement_criteria_8.setBackgroundResource(R.mipmap.improvementemojiunselected);
                    image_okay_criteria_8.setBackgroundResource(R.mipmap.okayemojiunselected);
                    image_good_criteria_8.setBackgroundResource(R.mipmap.goodemojiunselected);
                    image_excellent_criteria_8.setBackgroundResource(R.mipmap.excellentemojiselected);
                    txt_excellent_criteria_8.setText("Excellent");
                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .playOn(findViewById(R.id.image_excellent_criteria_8));
//                    LinearLayout.LayoutParams layoutParams8 = (LinearLayout.LayoutParams) image_excellent_criteria_8.getLayoutParams();
//                    layoutParams8.height = this.getResources().getDimensionPixelSize(R.dimen.item_height);
//                    layoutParams8.width = this.getResources().getDimensionPixelSize(R.dimen.item_width);
                    txt_okay_criteria_8.setText("");
                    txt_good_criteria_8.setText("");
                    txt_improvement_criteria_8.setText("");
                }
                else
                {
                   // image_excellent_criteria_8.setClickable(false);
                    image_excellent_criteria_8.setBackgroundResource(R.mipmap.excellentemojiunselected);
                    txt_excellent_criteria_8.setText("");
                }
                break;
            case R.id.camera_imageView :
                selectImage();
                break;
            case R.id.inspection_btnback :
                onBackPressed();
                break;
            case R.id.btn_insp_submit :
                OnSubmit();
                break;

          }
    }

    private void OnSubmit() {
        JSONArray jsonarray=new JSONArray();
        int count=0;
        String insp_comment = "", inspected_name = "";
                    JSONObject obj = new JSONObject();
                    try {
                        if(et_inspected_by.equals("") || et_inspected_by.length() == 0) {
                            Toast.makeText(InspectionBeginActivity.this, "Please enter name", Toast.LENGTH_LONG).show();

                        }
                        else {
                          inspected_name = et_inspected_by.getText().toString();
                            InputMethodManager imm = (InputMethodManager) et_inspected_by.getContext()
                                    .getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(et_inspected_by.getWindowToken(), 0);
                        }
                        if(et_comment.equals("") || et_comment.length() == 0)
                        {
                            Toast.makeText(InspectionBeginActivity.this, "Please enter comment", Toast.LENGTH_LONG).show();

                        }
                        else {
                            insp_comment = et_comment.getText().toString();
                            InputMethodManager imm = (InputMethodManager) et_comment.getContext()
                                    .getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(et_comment.getWindowToken(), 0);
                        }
                        obj.put("inspectorName",inspected_name);
                        obj.put("comments",insp_comment);
                        obj.put("inspectionDate",txt_insp_date_Val.getText().toString());

                        jsonarray.put(count,obj);
                        count++;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

        Log.e("TAG", "onSubmit : Json Array is:"+jsonarray.toString() );
        if(jsonarray.length()==0)
        {
            Toast.makeText(context,"Please select at least one size.",Toast.LENGTH_SHORT).show();

        }else
        {
         //   requestInspectionSubmitAPI(context,jsonarray);
        // Toast.makeText(context,"Data submission successfully",Toast.LENGTH_SHORT).show();

        }
    }
    //Inspection Submit API
    private void requestInspectionSubmitAPI(Context context1, JSONArray jsonarray) {
        {

            if (Reusable_Functions.chkStatus(context)) {
                Reusable_Functions.hDialog();
                Reusable_Functions.sDialog(context, "Submitting data…");
                String url = "";
              //  String url = ConstsCore.web_url + "/v1/save/stocktransfer/sendersubmit/" + userId ;//+"?recache="+recache
            //    Log.e("url", " put Request " + url + " ==== " + object.toString());
                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonarray.toString(),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.e("Sender Submit Click Response :", response.toString());
                                try {
                                    if (response == null || response.equals(null)) {
                                        Reusable_Functions.hDialog();
                                        Toast.makeText(context,"Sending data failed...", Toast.LENGTH_LONG).show();

                                    } else
                                    {
                                        String result=response.getString("status");
                                        Toast.makeText(context,""+result, Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(InspectionBeginActivity.this,DashBoardActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);
                                        //Details.this.finish();
                                        Reusable_Functions.hDialog();
                                    }
                                } catch (Exception e)
                                {
                                    Log.e("Exception e", e.toString() + "");
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
                        //  params.put("Content-Type", "application/json");
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

            } else
            {
                Toast.makeText(context, "Please check network connection...", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(InspectionBeginActivity.this,R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    startCamera();
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    openGallery();
                }
                else if (options[item].equals("Cancel"))
                {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void openGallery(){
        if ((int) Build.VERSION.SDK_INT < 23)
        {
            Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
        }
        else
        {
            int permissionCheck = ContextCompat.checkSelfPermission(InspectionBeginActivity.this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE);
            Log.e("Read Permission Check"," "+permissionCheck);
            if(permissionCheck== PackageManager.PERMISSION_GRANTED)
            {
                Log.i("Read Permission granted","");
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
            else
            {
                Log.i("Read Permission not granted","");
                ActivityCompat.requestPermissions(InspectionBeginActivity.this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_R);
            }
        }
    }

    public void startCamera(){
        if ((int) Build.VERSION.SDK_INT < 23)
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 1);
        }
        else
        {
            int permissionCheck = ContextCompat.checkSelfPermission(InspectionBeginActivity.this,
                    android.Manifest.permission.CAMERA);
            Log.e("Camera permission Check"," "+permissionCheck);
            if(permissionCheck == PackageManager.PERMISSION_GRANTED)
            {
                Log.i("Have Camera Permission", "Yes");
                permissionCheck = ContextCompat.checkSelfPermission(InspectionBeginActivity.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                //here
                int permissioncheckRead = ContextCompat.checkSelfPermission(InspectionBeginActivity.this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE);
                Log.e("Read and Write permission check",permissionCheck +"   " +permissioncheckRead );
                if(permissionCheck == PackageManager.PERMISSION_GRANTED && permissioncheckRead == PackageManager.PERMISSION_GRANTED)
                {
                    Log.i("Have Camera, Read and Write Permission","Yes");
                    //Open Camera Here
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else
                {
                    //Get Permission for read and write
                    Log.e("Camera permission approved ","But No RW permision");
                    Log.e("Ask for camera Read,Write permission","");
                    ActivityCompat.requestPermissions(InspectionBeginActivity.this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_RWFRMCAM);
                }
            }
            else
            {
                Log.e("Dont have camera permission", "Else block");
                ActivityCompat.requestPermissions(InspectionBeginActivity.this,
                        new String[]{android.Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            onCaptureImageResult(data);
        }
        else if(requestCode == 2)
        {
            if(data != null){
                selectedImage = data.getData();
                String[] filePath1 = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath1, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath1[0]);
                picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                image_upload.setVisibility(View.VISIBLE);
                Glide.with(InspectionBeginActivity.this)
                        .load(selectedImage)
                        .fitCenter()
                        .into(image_upload);

            }
        }
    }

    private void onCaptureImageResult(Intent data)
    {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try
        {
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
//        image_camera.setImageBitmap(thumbnail);
//        image_camera.getLayoutParams().height = RelativeLayout.LayoutParams.MATCH_PARENT;
//        image_camera.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
//        image_camera.setAdjustViewBounds(true);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
}

