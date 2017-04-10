package apsupportapp.aperotechnologies.com.designapp.Feedback;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.playlog.internal.LogEvent;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;

public class Feedback extends AppCompatActivity implements View.OnClickListener{

    private RelativeLayout Feedback_BtnBack;
    Context context = this;
    private Gson gson;
    private SharedPreferences sharedPreferences;
    private String userId;
    private String bearertoken;
    private String TAG="Feedback";
    private RequestQueue queue;
    private int count = 0;
    private int limit = 10;
    private int offsetvalue = 0;
    private int top = 10;
    private Feedback_model feedback_model;
    ArrayList<Feedback_model> feedbackList;
    private ImageView Feedback_image;
    private ProgressBar ImageLoader_feedback;
    private Button Pricing,Fitting,Colours,Prints,Styling,Fabric_quality,Garment_quality,FeedbackNext;
    private TextView Feedback_option;
    private AlertDialog dialog;
    private LinearLayout firstView;
    private RelativeLayout secondView;
    //  private Feedback_details feedbackAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        getSupportActionBar().hide();//
        initalise();
        gson = new Gson();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken","");
        Log.e(TAG, "userID and token" + userId + "and this is" + bearertoken);
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        feedbackList=new ArrayList<>();

        if (Reusable_Functions.chkStatus(context))
    {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            requestFeedbackApi();
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private void initalise() {
        Feedback_BtnBack=(RelativeLayout)findViewById(R.id.feedback_BtnBack);
        Feedback_image=(ImageView)findViewById(R.id.feedback_image);
        Feedback_option=(TextView)findViewById(R.id.feedback_option);
        ImageLoader_feedback=(ProgressBar)findViewById(R.id.imageLoader_feedback);

        firstView=(LinearLayout)findViewById(R.id.replaceView_first);
        secondView=(RelativeLayout)findViewById(R.id.replaceView_two);
        FeedbackNext=(Button)findViewById(R.id.feedbackNext);

        Pricing=(Button)findViewById(R.id.pricing);
        Fitting=(Button)findViewById(R.id.fitting);
        Colours=(Button)findViewById(R.id.colours);
        Prints=(Button)findViewById(R.id.prints);
        Styling=(Button)findViewById(R.id.styling);
        Fabric_quality=(Button)findViewById(R.id.fabric_quality);
        Garment_quality=(Button)findViewById(R.id.garment_quality);

        ImageLoader_feedback.setVisibility(View.GONE);
        Feedback_BtnBack.setOnClickListener(this);
        Pricing.setOnClickListener(this);
        Fitting.setOnClickListener(this);
        Colours.setOnClickListener(this);
        Prints.setOnClickListener(this);
        Styling.setOnClickListener(this);
        FeedbackNext.setOnClickListener(this);
        Fabric_quality.setOnClickListener(this);
        Garment_quality.setOnClickListener(this);
    }

    private void requestFeedbackApi( ) {


        if (Reusable_Functions.chkStatus(context)) {

            //https://smdm.manthan.com/v1/display/worstperformerfeedback/displayoptions/4813
            String url = ConstsCore.web_url + "/v1/display/worstperformerfeedback/displayoptions/" + userId + "?offset=" + offsetvalue + "&limit=" + limit;


            Log.e(TAG, "URL" + url);
            final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.i(TAG, "feedback response: " + " " + response);
                            Log.i(TAG, "feedback response length" + "" + response.length());
                            try {
                                if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                    Reusable_Functions.hDialog();
                                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                    return;


                                } else if (response.length() == limit) {

                                    Log.e(TAG, "Top eql limit");
                                    for (int i = 0; i < response.length(); i++) {
                                        feedback_model = gson.fromJson(response.get(i).toString(), Feedback_model.class);
                                        feedbackList.add(feedback_model);
                                    }
                                    offsetvalue = (limit * count) + limit;
                                    count++;

                                    //  count++ ;

                                    requestFeedbackApi();

                                } else if (response.length() < limit) {
                                    Log.e(TAG, "promo /= limit");
                                    for (int i = 0; i < response.length(); i++) {

                                        feedback_model = gson.fromJson(response.get(i).toString(), Feedback_model.class);
                                        feedbackList.add(feedback_model);

                                    }
                                    count = 0;
                                    limit = 10;
                                    offsetvalue = 0;
                                }


                                // new GravitySnapHelper(48).attachToRecyclerView(recyclerView);
                               // feedbackAdapter = new Feedback_details(feedbackList,Feedback.this);
                                Reusable_Functions.hDialog();
                                Feedback_option.setText(feedbackList.get(3).getOption());
                                Log.e(TAG, "array list size : "+feedbackList.size() );
                                ImageLoader_feedback.setVisibility(View.VISIBLE);

                                if(!feedbackList.get(3).getProdImageUrl().equals(""))
                                {
                                    Glide.
                                            with(context)
                                            .load(feedbackList.get(3).getProdImageUrl())
                                            .listener(new RequestListener<String, GlideDrawable>() {
                                                @Override
                                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                                    ImageLoader_feedback.setVisibility(View.GONE);
                                                    return false;
                                                }

                                                @Override
                                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                    ImageLoader_feedback.setVisibility(View.GONE);
                                                    return false;
                                                }
                                            })
                                            .into(Feedback_image);

                                }else {
                                    ImageLoader_feedback.setVisibility(View.GONE);

                                    Glide.with(context).
                                            load(R.mipmap.placeholder).
                                            into(Feedback_image);



                                }


                            } catch (Exception e) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "data failed...." + e.toString(), Toast.LENGTH_SHORT).show();
                                Reusable_Functions.hDialog();
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "server not responding..", Toast.LENGTH_SHORT).show();
                            Reusable_Functions.hDialog();
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


//---------------seton Click list listener------------------//


        }
    }



    public static void StartIntent(Context c) {
        c.startActivity(new Intent(c, Feedback.class));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.feedback_BtnBack:
                finish();
                break;
            case R.id.feedbackNext:
                firstView.setVisibility(View.VISIBLE);
                secondView.setVisibility(View.GONE);
                break;
            default:
                commentDialog();
                break;


        }
    }

    private void commentDialog() {

        Log.e(TAG, "commentDialog: true...." );
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v=inflater.inflate(R.layout.comment_dialog, null);
        LinearLayout skip =(LinearLayout) v.findViewById(R.id.comment_skip);
        LinearLayout ok =(LinearLayout)v.findViewById(R.id.comment_ok);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                firstView.setVisibility(View.GONE);
                secondView.setVisibility(View.VISIBLE);

            }
        });

        builder.setView(v);

        dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
