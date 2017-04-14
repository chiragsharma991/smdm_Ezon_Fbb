package apsupportapp.aperotechnologies.com.designapp.Feedback;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.playlog.internal.LogEvent;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment.Details;
import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.To_Do;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;

public class Feedback extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout Feedback_BtnBack;
    Context context = this;
    private Gson gson;
    private SharedPreferences sharedPreferences;
    private String userId;
    private String bearertoken;
    private String TAG = "Feedback";
    private RequestQueue queue;
    private int count = 0;
    private int limit = 10;
    private int offsetvalue = 0;
    private int listCount = 0;  //when you click on next then
    private int top = 10;
    private Feedback_model feedback_model;
    private ArrayList<Feedback_model> feedbackList, feedbackReportList;
    private ImageView Feedback_image;
    private ProgressBar ImageLoader_feedback;
    private Button Pricing, Fitting, Colours, Prints, Styling, Fabric_quality, Garment_quality;
    private TextView Feedback_option;
    private EditText feedback_comment;
    private AlertDialog dialog;
    private LinearLayout firstView;
    private RelativeLayout secondView, FeedbackNext;
    private RelativeLayout Fitting_relative, Pricing_relative, colours_relative, prints_relative, styling_relative, fabric_relative, garment_relative;
    private ListView FeedbackDetailList;
    private ArrayList<String> optionList;
    private boolean feedbackReport = false;
    private Feedback_model feedback_model_report;
    private String selectCategory;
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
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Log.e(TAG, "userID and token" + userId + "and this is" + bearertoken);
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        feedbackList = new ArrayList<>();
        feedbackReportList = new ArrayList<>();

        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            requestFeedbackApi();
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private void initalise() {


        optionList = new ArrayList<>();
        optionList.add("Fitting");
        optionList.add("Pricing");
        optionList.add("Colours");
        optionList.add("Prints");
        optionList.add("Styling");
        optionList.add("Fabric Quality");
        optionList.add("Garment Quality");

        Feedback_BtnBack = (RelativeLayout) findViewById(R.id.feedback_BtnBack);
        Feedback_image = (ImageView) findViewById(R.id.feedback_image);
        Feedback_option = (TextView) findViewById(R.id.feedback_option);
        ImageLoader_feedback = (ProgressBar) findViewById(R.id.imageLoader_feedback);
        // FeedbackDetailList=(ListView)findViewById(R.id.feedbackDetailList);


        firstView = (LinearLayout) findViewById(R.id.replaceView_first);
        secondView = (RelativeLayout) findViewById(R.id.replaceView_two);
        FeedbackNext = (RelativeLayout) findViewById(R.id.feedbackNext);

        Pricing = (Button) findViewById(R.id.pricing);
        Fitting = (Button) findViewById(R.id.fitting);
        Colours = (Button) findViewById(R.id.colours);
        Prints = (Button) findViewById(R.id.prints);
        Styling = (Button) findViewById(R.id.styling);
        Fabric_quality = (Button) findViewById(R.id.fabric_quality);
        Garment_quality = (Button) findViewById(R.id.garment_quality);


        Fitting_relative = (RelativeLayout) findViewById(R.id.fitting_relative);
        Pricing_relative = (RelativeLayout) findViewById(R.id.pricing_relative);
        colours_relative = (RelativeLayout) findViewById(R.id.colours_relative);
        prints_relative = (RelativeLayout) findViewById(R.id.prints_relative);
        styling_relative = (RelativeLayout) findViewById(R.id.styling_relative);
        fabric_relative = (RelativeLayout) findViewById(R.id.fabric_relative);
        garment_relative = (RelativeLayout) findViewById(R.id.garment_relative);


        ImageLoader_feedback.setVisibility(View.GONE);
        firstView.setVisibility(View.VISIBLE);
        secondView.setVisibility(View.GONE);

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

    private void requestFeedbackApi() {


        if (Reusable_Functions.chkStatus(context)) {



            String url;
            if (feedbackReport == false) {
                url = ConstsCore.web_url + "/v1/display/worstperformerfeedback/displayoptions/" + userId + "?offset=" + offsetvalue + "&limit=" + limit;

            } else {

                String option=Feedback_option.getText().toString().replace("%", "%25").replace(" ", "%20").replace("&", "%26");
                url = ConstsCore.web_url + "/v1/display/worstperformerfeedback/displayreports/" + userId + "?option=" +option + "&offset=" + offsetvalue + "&limit=" + limit;

            }
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
                                    feedbackReport = false;
                                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                    return;


                                } else if (response.length() == limit) {

                                    // if api call for report then it will true
                                    if (feedbackReport) {

                                        Log.e(TAG, "Top eql limit");
                                        for (int i = 0; i < response.length(); i++) {
                                            feedback_model_report = gson.fromJson(response.get(i).toString(), Feedback_model.class);
                                            feedbackReportList.add(feedback_model_report);
                                        }
                                        offsetvalue = (limit * count) + limit;
                                        count++;
                                        requestFeedbackApi();

                                    } else {
                                        Log.e(TAG, "Top eql limit");
                                        for (int i = 0; i < response.length(); i++) {
                                            feedback_model = gson.fromJson(response.get(i).toString(), Feedback_model.class);
                                            feedbackList.add(feedback_model);
                                        }
                                        offsetvalue = (limit * count) + limit;
                                        count++;
                                        requestFeedbackApi();

                                    }


                                    // if api call for last entry.

                                } else if (response.length() < limit) {

                                    // if api call for report then it will true

                                    if (feedbackReport) {

                                        Log.e(TAG, "promo /= limit");
                                        for (int i = 0; i < response.length(); i++) {

                                            feedback_model_report = gson.fromJson(response.get(i).toString(), Feedback_model.class);
                                            feedbackReportList.add(feedback_model_report);

                                        }
                                        count = 0;
                                        limit = 10;
                                        offsetvalue = 0;

                                        for (int i = 0; i < optionList.size(); i++) {

                                            feedbackDetails(i, 0);
                                        }
                                        feedbackReport = false;
                                        Reusable_Functions.hDialog();


                                    } else {
                                        Log.e(TAG, "promo /= limit");
                                        for (int i = 0; i < response.length(); i++) {

                                            feedback_model = gson.fromJson(response.get(i).toString(), Feedback_model.class);
                                            feedbackList.add(feedback_model);

                                        }
                                        count = 0;
                                        limit = 10;
                                        offsetvalue = 0;
                                        Reusable_Functions.hDialog();
                                        nextList(listCount);
                                    }
                                }


                                // new GravitySnapHelper(48).attachToRecyclerView(recyclerView);
                                // feedbackAdapter = new Feedback_details(feedbackList,Feedback.this);


                            } catch (Exception e) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "data failed...." + e.toString(), Toast.LENGTH_SHORT).show();
                                Reusable_Functions.hDialog();
                                feedbackReport = false;
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
                            feedbackReport = false;
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
        switch (view.getId()) {
            case R.id.feedback_BtnBack:
                finish();
                break;
            case R.id.feedbackNext:

                if (listCount + 1 < feedbackList.size()) {
                    Reusable_Functions.ViewVisible(firstView);
                    Reusable_Functions.ViewGone(secondView);
                    listCount++;
                    nextList(listCount);
                } else {
                    Toast.makeText(context, "Data is not available", Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                Button button = (Button) view;
                selectCategory = button.getText().toString();
                Log.e(TAG, "onClick:  selectCategory"+selectCategory);
                commentDialog();
                break;


        }
    }

    private void nextList(int position) {
        Feedback_option.setText(feedbackList.get(position).getOption());
        Log.e(TAG, "array list size : " + feedbackList.size());
        ImageLoader_feedback.setVisibility(View.VISIBLE);

        if (!feedbackList.get(position).getProdImageUrl().equals("")) {
            Glide.
                    with(context)
                    .load(feedbackList.get(position).getProdImageUrl())
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

        } else {
            ImageLoader_feedback.setVisibility(View.GONE);

            Glide.with(context).
                    load(R.mipmap.placeholder).
                    into(Feedback_image);


        }
    }

    private void commentDialog() {

        Log.e(TAG, "commentDialog: true....");
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v = inflater.inflate(R.layout.comment_dialog, null);
        LinearLayout skip = (LinearLayout) v.findViewById(R.id.comment_skip);
        LinearLayout ok = (LinearLayout) v.findViewById(R.id.comment_ok);
        feedback_comment = (EditText) v.findViewById(R.id.feedback_comment);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Submitting data…");
                    JSONObject jsonObject = OnSubmit();
                    Log.e(TAG, "jsonObject: "+jsonObject.toString() );
                    requestReceiverSubmitAPI(context, jsonObject);
                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();

            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!(feedback_comment.getText().length() == 0)) {


                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.hDialog();
                        Reusable_Functions.sDialog(context, "Submitting data…");
                        JSONObject jsonObject = OnSubmit();
                        Log.e(TAG, "jsonObject: "+jsonObject.toString() );
                        requestReceiverSubmitAPI(context, jsonObject);
                    } else {
                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();

                } else {
                    Toast.makeText(context, "Please enter your comment", Toast.LENGTH_SHORT).show();
                }


            }
        });

        builder.setView(v);

        dialog = builder.create();
        dialog.show();
    }

    private void feedbackDetails(final int position, final int Listposition) {

        // firstView.setVisibility(View.GONE);
        // secondView.setVisibility(View.VISIBLE);
        Reusable_Functions.ViewGone(firstView);
        Reusable_Functions.ViewVisible(secondView);
        Fitting_relative.removeAllViewsInLayout();
        Pricing_relative.removeAllViewsInLayout();
        colours_relative.removeAllViewsInLayout();
        prints_relative.removeAllViewsInLayout();
        styling_relative.removeAllViewsInLayout();
        fabric_relative.removeAllViewsInLayout();
        garment_relative.removeAllViewsInLayout();

        //calculate screen view size and add line bar process.

        ViewTreeObserver vto = Fitting_relative.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    Fitting_relative.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    Fitting_relative.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int width = Fitting_relative.getMeasuredWidth();
                int height = Fitting_relative.getMeasuredHeight();

                // Calculation width acording to size of phone

                double x = 0;  // x is result of calculation.
                if (position == 0)
                //get 0 is depend on next and pre button
                {
                    x = ((double) feedbackReportList.get(Listposition).getFittingCntPer() / 100) * width;
                } else if (position == 1) {
                    x = ((double) feedbackReportList.get(Listposition).getPricingCntPer() / 100) * width;
                } else if (position == 2) {
                    x = ((double) feedbackReportList.get(Listposition).getColorsCntPer() / 100) * width;
                } else if (position == 3) {
                    x = ((double) feedbackReportList.get(Listposition).getPrintCntPer() / 100) * width;
                } else if (position == 4) {
                    x = ((double) feedbackReportList.get(Listposition).getStylingCntPer() / 100) * width;
                } else if (position == 5) {
                    x = ((double) feedbackReportList.get(Listposition).getFabricQualityCntPer() / 100) * width;
                } else if (position == 6) {
                    x = ((double) feedbackReportList.get(Listposition).getGarmentQualityCntPer() / 100) * width;
                }

                int percentage = (int) x;
                Log.e("TAG", "view width:................ " + width + "and percentage is " + feedbackReportList.get(0).getFittingCntPer() + "and values are" + percentage);
                View lp = new View(context);
                // LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(400,LinearLayout.LayoutParams.MATCH_PARENT);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(percentage, LinearLayout.LayoutParams.MATCH_PARENT);
                lp.setLayoutParams(layoutParams);
                lp.setBackgroundColor(Color.parseColor("#e3e2e3"));
                if (position == 0) {
                    Fitting_relative.addView(lp);
                } else if (position == 1) {
                    Pricing_relative.addView(lp);
                } else if (position == 2) {
                    colours_relative.addView(lp);
                } else if (position == 3) {
                    prints_relative.addView(lp);
                } else if (position == 4) {
                    styling_relative.addView(lp);
                } else if (position == 5) {
                    fabric_relative.addView(lp);
                } else if (position == 6) {
                    garment_relative.addView(lp);
                }

                AddText(position, Listposition);

            }
        });


    }

    private void AddText(int position, int Listposition) {


        // starting title text


        final TextView textView1 = new TextView(context);
        textView1.setText("" + optionList.get(position));
        textView1.setTextColor(Color.parseColor("#404040"));

        final RelativeLayout.LayoutParams params1 =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);

        params1.addRule(RelativeLayout.CENTER_VERTICAL);
        params1.setMargins(5, 0, 0, 0);
        textView1.setLayoutParams(params1);

        // add text in list

        if (position == 0) {
            Fitting_relative.addView(textView1, params1);
        } else if (position == 1) {
            Pricing_relative.addView(textView1, params1);
        } else if (position == 2) {
            colours_relative.addView(textView1, params1);
        } else if (position == 3) {
            prints_relative.addView(textView1, params1);
        } else if (position == 4) {
            styling_relative.addView(textView1, params1);
        } else if (position == 5) {
            fabric_relative.addView(textView1, params1);
        } else if (position == 6) {
            garment_relative.addView(textView1, params1);
        }


        // another text

        final TextView textView2 = new TextView(context);

        // get percentage for all list & 0 will be change according to next pre button
        if (position == 0) {
            textView2.setText("" + String.format("%.1f", +feedbackReportList.get(Listposition).getFittingCntPer()) + "%");
        } else if (position == 1) {
            textView2.setText("" + String.format("%.1f", +feedbackReportList.get(Listposition).getPricingCntPer()) + "%");
        } else if (position == 2) {
            textView2.setText("" + String.format("%.1f", +feedbackReportList.get(Listposition).getColorsCntPer()) + "%");
        } else if (position == 3) {
            textView2.setText("" + String.format("%.1f", +feedbackReportList.get(Listposition).getPrintCntPer()) + "%");
        } else if (position == 4) {
            textView2.setText("" + String.format("%.1f", +feedbackReportList.get(Listposition).getStylingCntPer()) + "%");
        } else if (position == 5) {
            textView2.setText("" + String.format("%.1f", +feedbackReportList.get(Listposition).getFabricQualityCntPer()) + "%");
        } else if (position == 6) {
            textView2.setText("" + String.format("%.1f", +feedbackReportList.get(Listposition).getGarmentQualityCntPer()) + "%");
        }

        textView2.setTextColor(Color.parseColor("#404040"));

        final RelativeLayout.LayoutParams params2 =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);

        params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params2.addRule(RelativeLayout.CENTER_VERTICAL);
        textView2.setLayoutParams(params2);

        // add text in all list
        if (position == 0) {
            Fitting_relative.addView(textView2, params2);
        } else if (position == 1) {
            Pricing_relative.addView(textView2, params2);
        } else if (position == 2) {
            colours_relative.addView(textView2, params2);
        } else if (position == 3) {
            prints_relative.addView(textView2, params2);
        } else if (position == 4) {
            styling_relative.addView(textView2, params2);
        } else if (position == 5) {
            fabric_relative.addView(textView2, params2);
        } else if (position == 6) {
            garment_relative.addView(textView2, params2);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public JSONObject OnSubmit() {


        // do not change this section because this is only hardcoded and case sensitive.
        int fitting = selectCategory.equals("Fitting") ? 1 : 0;
        int pricing = selectCategory.equals("Pricing")  ? 1 : 0;
        int colours = selectCategory.equals("Colours")  ? 1 : 0;
        int prints = selectCategory.equals("Prints")  ? 1 : 0;
        int styling = selectCategory.equals("Styling")  ? 1 : 0;
        int fabric = selectCategory.equals("Fabric Quality")  ? 1 : 0;
        int fabricQuality = selectCategory.equals("Garment Quality")  ? 1 : 0;


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("option", Feedback_option.getText().toString());
            jsonObject.put("userId", userId);
            jsonObject.put("prodImageUrl", feedbackList.get(listCount).getProdImageUrl());
            jsonObject.put("comments", feedback_comment.getText().toString());
            jsonObject.put("fitting", fitting);
            jsonObject.put("pricing", pricing);
            jsonObject.put("colors", colours);
            jsonObject.put("print", prints);
            jsonObject.put("styling", styling);
            jsonObject.put("fabricQuality", fabric);
            jsonObject.put("garmentQuality", fabricQuality);




        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "OnSubmit: catch error " + e.getMessage());
        }


        return jsonObject;
    }

    private void requestReceiverSubmitAPI(final Context mcontext, JSONObject object) {

        if (Reusable_Functions.chkStatus(mcontext)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(mcontext, "Submitting data…");

            String url = ConstsCore.web_url + "/v1/save/worstperformerfeedbackdetails/" + userId;//+"?recache="+recache
            Log.e("url", " post Request " + url + " ==== " + object.toString());
            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, object.toString(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("Submit Click Response :", response.toString());
                            try {
                                if (response == null || response.equals(null)) {
                                    Reusable_Functions.hDialog();
                                    Toast.makeText(mcontext, "Sending data failed...", Toast.LENGTH_LONG).show();

                                } else {
                                    String result = response.getString("status");
                                    Toast.makeText(mcontext, "" + result, Toast.LENGTH_LONG).show();
                                    Reusable_Functions.hDialog();


                                    if (Reusable_Functions.chkStatus(context)) {
                                        feedbackReport = true;
                                        Reusable_Functions.hDialog();
                                        Reusable_Functions.sDialog(context, "Loading....");
                                        requestFeedbackApi();   //call api again for showing report acording to option.

                                    } else {
                                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            } catch (Exception e) {
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


        } else {
            Toast.makeText(context, "Please check network connection...", Toast.LENGTH_SHORT).show();

        }


    }

}
