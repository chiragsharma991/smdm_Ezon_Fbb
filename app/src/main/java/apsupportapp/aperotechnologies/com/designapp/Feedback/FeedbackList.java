package apsupportapp.aperotechnologies.com.designapp.Feedback;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;

public class FeedbackList extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout Feedback_BtnBack;
    Context context = this;
    private Gson gson;
    private SharedPreferences sharedPreferences;
    private String userId;
    private String bearertoken,storeDescription;
    private String TAG = "FeedbackList";
    private RequestQueue queue;
    private int count = 0;
    private int limit = 10;
    private int offsetvalue = 0;
    private int top = 10;
    private int nextCount = 0;  // when you press next then
    private Feedback_model feedback_model;
    ArrayList<Feedback_model> feedbackListData;
    private ImageView Feedback_image;
    private ProgressBar ImageLoader_feedback;
    private Button Pricing, Fitting, Colours, Prints, Styling, Fabric_quality, Garment_quality;
    private TextView Feedback_option,txtStoreCode,txtStoreName;
    private AlertDialog dialog;
    private LinearLayout firstView;
    private RelativeLayout secondView,FeedbackPre_layout,FeedbackNext_layout;
    private Button FeedbackNext, FeedbackPre;
    private RelativeLayout Fitting_relative, Pricing_relative, colours_relative, prints_relative, styling_relative, fabric_relative, garment_relative;
    private ListView FeedbackDetailList;
    private ArrayList<String> optionList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_list);
        getSupportActionBar().hide();//
        gson = new Gson();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        storeDescription = sharedPreferences.getString("storeDescription","");
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        initalise();
        feedbackListData = new ArrayList<>();

        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            requestFeedbackApi();
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
            FeedbackNext.setEnabled(false);
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
        Feedback_BtnBack = (RelativeLayout) findViewById(R.id.feedbackList_BtnBack);
        Feedback_image = (ImageView) findViewById(R.id.feedbackList_image);
        Feedback_option = (TextView) findViewById(R.id.feedbackList_option);
        txtStoreCode = (TextView) findViewById(R.id.txtStoreCode);
        txtStoreName = (TextView) findViewById(R.id.txtStoreName);
        ImageLoader_feedback = (ProgressBar) findViewById(R.id.imageLoader_feedbackList);
        FeedbackNext = (Button) findViewById(R.id.feedbackList_next);
        FeedbackPre = (Button) findViewById(R.id.feedbackList_pre);

        FeedbackPre_layout = (RelativeLayout) findViewById(R.id.feedbackList_pre_layout);
        FeedbackNext_layout = (RelativeLayout) findViewById(R.id.feedbackList_next_layout);
        FeedbackPre_layout.setVisibility(View.GONE);

        //all relative layout for add views
        Fitting_relative = (RelativeLayout) findViewById(R.id.fitting_relative);
        Pricing_relative = (RelativeLayout) findViewById(R.id.pricing_relative);
        colours_relative = (RelativeLayout) findViewById(R.id.colours_relative);
        prints_relative = (RelativeLayout) findViewById(R.id.prints_relative);
        styling_relative = (RelativeLayout) findViewById(R.id.styling_relative);
        fabric_relative = (RelativeLayout) findViewById(R.id.fabric_relative);
        garment_relative = (RelativeLayout) findViewById(R.id.garment_relative);
        ImageLoader_feedback.setVisibility(View.GONE);
        Feedback_BtnBack.setOnClickListener(this);
        FeedbackNext.setOnClickListener(this);
        FeedbackPre.setOnClickListener(this);
    }

    private void requestFeedbackApi() {


        if (Reusable_Functions.chkStatus(context)) {

            //https://smdm.manthan.com/v1/display/worstperformerfeedback/displayreports/4813
            String url = ConstsCore.web_url + "/v1/display/worstperformerfeedback/displayreports/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&recache=true";

            Log.e(TAG, "requestFeedbackApi: "+url);

            final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d(TAG, "onResponse: "+response );
                            try {
                                if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                    Reusable_Functions.hDialog();
                                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                    return;

                                } else if (response.length() == limit) {

                                    for (int i = 0; i < response.length(); i++) {
                                        feedback_model = gson.fromJson(response.get(i).toString(), Feedback_model.class);
                                        feedbackListData.add(feedback_model);
                                    }
                                    offsetvalue = (limit * count) + limit;
                                    count++;

                                    //  count++ ;

                                    requestFeedbackApi();

                                } else if (response.length() < limit) {
                                    for (int i = 0; i < response.length(); i++) {

                                        feedback_model = gson.fromJson(response.get(i).toString(), Feedback_model.class);
                                        feedbackListData.add(feedback_model);

                                    }
                                    count = 0;
                                    limit = 10;
                                    offsetvalue = 0;
                                    Reusable_Functions.hDialog();
                                    for (int i = 0; i < optionList.size(); i++) {

                                        feedbackReport(i, 0);
                                    }
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
        c.startActivity(new Intent(c, FeedbackList.class));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.feedbackList_BtnBack:
                finish();
                break;
            case R.id.feedbackList_next:

                nextCount++;
                if (nextCount != feedbackListData.size() - 1) {
                    for (int i = 0; i < optionList.size(); i++) {

                        feedbackReport(i, nextCount);
                    }
                    FeedbackPre_layout.setVisibility(View.VISIBLE);

                } else {
                    for (int i = 0; i < optionList.size(); i++) {

                        feedbackReport(i, nextCount);
                    }
                    FeedbackNext_layout.setVisibility(View.GONE);
                }
                break;
            case R.id.feedbackList_pre:
                nextCount--;
                if (nextCount != 0) {
                    for (int i = 0; i < optionList.size(); i++) {

                        feedbackReport(i, nextCount);
                    }
                    FeedbackNext_layout.setVisibility(View.VISIBLE);

                } else {
                    for (int i = 0; i < optionList.size(); i++) {

                        feedbackReport(i, nextCount);
                    }
                    FeedbackPre_layout.setVisibility(View.GONE);
                }

                break;


            default:
                break;


        }
    }


    private void feedbackReport(final int position, final int Listposition) {

        Fitting_relative.removeAllViewsInLayout();
        Pricing_relative.removeAllViewsInLayout();
        colours_relative.removeAllViewsInLayout();
        prints_relative.removeAllViewsInLayout();
        styling_relative.removeAllViewsInLayout();
        fabric_relative.removeAllViewsInLayout();
        garment_relative.removeAllViewsInLayout();
        // set image per list

        Feedback_option.setText(feedbackListData.get(Listposition).getOption());
        txtStoreCode.setText(storeDescription.trim().substring(0,4));
        txtStoreName.setText(storeDescription.substring(5));
        ImageLoader_feedback.setVisibility(View.VISIBLE);
        if (!feedbackListData.get(Listposition).getProdImageUrl().equals("")) {
            Glide.
                    with(context)
                    .load(feedbackListData.get(Listposition).getProdImageUrl())
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
                    load(R.mipmap.noimageavailable).
                    into(Feedback_image);
        }

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

                double x = 0;
                if (position == 0)
                //get 0 is depend on next and pre button
                {
                    x = ((double) feedbackListData.get(Listposition).getFittingCntPer() / 100) * width;
                } else if (position == 1) {
                    x = ((double) feedbackListData.get(Listposition).getPricingCntPer() / 100) * width;
                } else if (position == 2) {
                    x = ((double) feedbackListData.get(Listposition).getColorsCntPer() / 100) * width;
                } else if (position == 3) {
                    x = ((double) feedbackListData.get(Listposition).getPrintCntPer() / 100) * width;
                } else if (position == 4) {
                    x = ((double) feedbackListData.get(Listposition).getStylingCntPer() / 100) * width;
                } else if (position == 5) {
                    x = ((double) feedbackListData.get(Listposition).getFabricQualityCntPer() / 100) * width;
                } else if (position == 6) {
                    x = ((double) feedbackListData.get(Listposition).getGarmentQualityCntPer() / 100) * width;
                }

                int percentage = (int) x;
                Log.e("TAG", "view width:................ " + width + "and percentage is " + feedbackListData.get(0).getFittingCntPer() + "and values are" + percentage);
                View lp = new View(context);
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
        textView1.setTextColor(Color.parseColor("#000000"));
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
            textView2.setText("" + String.format("%.1f", +feedbackListData.get(Listposition).getFittingCntPer()) + "%");
        } else if (position == 1) {
            textView2.setText("" + String.format("%.1f", +feedbackListData.get(Listposition).getPricingCntPer()) + "%");
        } else if (position == 2) {
            textView2.setText("" + String.format("%.1f", +feedbackListData.get(Listposition).getColorsCntPer()) + "%");
        } else if (position == 3) {
            textView2.setText("" + String.format("%.1f", +feedbackListData.get(Listposition).getPrintCntPer()) + "%");
        } else if (position == 4) {
            textView2.setText("" + String.format("%.1f", +feedbackListData.get(Listposition).getStylingCntPer()) + "%");
        } else if (position == 5) {
            textView2.setText("" + String.format("%.1f", +feedbackListData.get(Listposition).getFabricQualityCntPer()) + "%");
        } else if (position == 6) {
            textView2.setText("" + String.format("%.1f", +feedbackListData.get(Listposition).getGarmentQualityCntPer()) + "%");
        }
        textView2.setTextColor(Color.parseColor("#000000"));
        textView2.setTypeface(Typeface.DEFAULT_BOLD);
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
}

