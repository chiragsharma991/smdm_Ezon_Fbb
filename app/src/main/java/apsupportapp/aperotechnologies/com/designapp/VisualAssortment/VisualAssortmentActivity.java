package apsupportapp.aperotechnologies.com.designapp.VisualAssortment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.DashBoardActivity;
import apsupportapp.aperotechnologies.com.designapp.ProductNameBean;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.SearchActivity1;
import apsupportapp.aperotechnologies.com.designapp.model.VisualAssort;

/**
 * Created by hasai on 22/09/16.
 */
public class VisualAssortmentActivity  extends AppCompatActivity {


    public static ViewPager viewPager_VAssortment;
    SharedPreferences sharedpreferences;
    ArrayList<VisualAssort> visualassortmentlist;
    public static Context cont;

    RelativeLayout btnlike, btndislike, btnbuy, btncomment;
    LinearLayout layoutBuy, layoutComment;
    Button btnBuyDone, btnCommentDone;
    EditText edtTextSets, edtTextComment;

    SharedPreferences sharedPreferences;
    String userId, bearertoken;


    RequestQueue queue;
    Gson gson;
    VisualAssort visualAssort;
    int offsetvalue=0,limit=100;
    int count=0;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualassortment);
        getSupportActionBar().hide();

        cont = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(cont);
        userId = sharedPreferences.getString("userId","");
        bearertoken = sharedPreferences.getString("bearerToken","");

        viewPager_VAssortment = (ViewPager) findViewById(R.id.viewPager_VAssortment);


        visualassortmentlist = new ArrayList<VisualAssort>();
        Cache cache = new DiskBasedCache(cont.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();

        if (Reusable_Functions.chkStatus(cont)) {

            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(cont, "Loading data...");
            offsetvalue = 0;
            limit = 100;
            count = 0;
            requestdisplayVisualAssortment();

        } else
        {
            Toast.makeText(cont, "Check your network connectivity", Toast.LENGTH_LONG).show();
        }


//        btnlike = (RelativeLayout) findViewById(R.id.imgbtnlike);
//        btndislike = (RelativeLayout) findViewById(R.id.imgbtndislike);
//        btnbuy = (RelativeLayout) findViewById(R.id.imgbtnbuy);
//        btncomment = (RelativeLayout) findViewById(R.id.imgbtncomment);
//        layoutBuy = (LinearLayout) findViewById(R.id.layoutBuy);
//        layoutComment = (LinearLayout) findViewById(R.id.layoutComment);
//        btnBuyDone = (Button) findViewById(R.id.btnBuyDone);
//        btnCommentDone = (Button) findViewById(R.id.btnCommentDone);
//        edtTextSets = (EditText) findViewById(R.id.edtTextSets);
//        edtTextComment = (EditText) findViewById(R.id.edtTextComment);




//        edtTextSets.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//            Boolean handled = false;
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_NEXT) || (actionId == EditorInfo.IME_ACTION_NONE)) {
//                    edtTextSets.clearFocus();
//                    InputMethodManager inputManager = (InputMethodManager) cont.getSystemService(Context.INPUT_METHOD_SERVICE);
//                    if(inputManager != null){
//                        inputManager.hideSoftInputFromWindow(edtTextSets.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                    }
//                    handled = true;
//                }
//                return handled;
//            }
//
//        });
//
//
//        edtTextComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//            Boolean handled = false;
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_NEXT) || (actionId == EditorInfo.IME_ACTION_NONE)) {
//                    edtTextComment.clearFocus();
//                    handled = true;
//                }
//                return handled;
//            }
//
//        });
//
//        btnlike.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("click of like button","");
//
//                ImageButton btn = (ImageButton) btnlike.getChildAt(0);
//                btn.setBackgroundResource(R.mipmap.like_selected);
//                btnlike.setEnabled(false);
//            }
//        });
//
//        btndislike.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("click of dislike button","");
//                ImageButton btn = (ImageButton) btndislike.getChildAt(0);
//                btn.setBackgroundResource(R.mipmap.dislike_selected);
//                btndislike.setEnabled(false);
//            }
//        });
//
//
//        btnbuy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("click of buy button","");
//                edtTextSets.setText("0");
//
//                if(layoutComment.getVisibility() == View.VISIBLE)
//                {
//                    layoutComment.setVisibility(View.GONE);
//                }
//
//                if(layoutBuy.getVisibility() == View.GONE)
//                {
//                    layoutBuy.setVisibility(View.VISIBLE);
//                }
//                else if(layoutBuy.getVisibility() == View.VISIBLE)
//                {
//                    layoutBuy.setVisibility(View.GONE);
//                }
//
//
//            }
//        });
//
//        btnBuyDone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Log.e("click of buy done button","");
//
//                layoutBuy.setVisibility(View.GONE);
//                edtTextSets.setText("0");
//                btnbuy.setEnabled(false);
//                InputMethodManager inputManager = (InputMethodManager) cont.getSystemService(Context.INPUT_METHOD_SERVICE);
//                if(inputManager != null){
//                    inputManager.hideSoftInputFromWindow(edtTextSets.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                }
//            }
//
//        });
//
//
//        btncomment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                edtTextComment.setText("");
//                Log.e("click of comment button","");
//
//                if(layoutBuy.getVisibility() == View.VISIBLE)
//                {
//                    layoutBuy.setVisibility(View.GONE);
//                }
//
//                if(layoutComment.getVisibility() == View.GONE)
//                {
//                    layoutComment.setVisibility(View.VISIBLE);
//                }
//                else if(layoutComment.getVisibility() == View.VISIBLE)
//                {
//                    layoutComment.setVisibility(View.GONE);
//                }
//            }
//        });
//
//        btnCommentDone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Log.e("click of comment done button","");
//
//                layoutComment.setVisibility(View.GONE);
//                edtTextComment.setText("");
//                btncomment.setEnabled(false);
//                InputMethodManager inputManager = (InputMethodManager) cont.getSystemService(Context.INPUT_METHOD_SERVICE);
//                if(inputManager != null){
//                    inputManager.hideSoftInputFromWindow(edtTextComment.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                }
//            }
//
//
//
//        });
    }



    @Override
    public void onBackPressed() {

//        if(layoutComment.getVisibility() == View.VISIBLE)
//        {
//            layoutComment.setVisibility(View.GONE);
//        }
//        else if(layoutBuy.getVisibility() == View.VISIBLE)
//        {
//            layoutBuy.setVisibility(View.GONE);
//        }
//        else
//        {
            Intent i = new Intent(VisualAssortmentActivity.this, DashBoardActivity.class);
            startActivity(i);
            finish();
  //      }


    }


    private void requestdisplayVisualAssortment() {

        //String url = ConstsCore.web_url + "/v1/display/visualassortments/" + userId+"?offset="+offsetvalue+"&limit="+ limit;
        String url = ConstsCore.web_url + "/v1/display/visualassortments/" + userId+"?offset=0&limit=100";
        Log.e("url", " URL VASSORT "+url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("response visualassortment: ", " "+response.length());

                        try
                        {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0)
                            {
                                Reusable_Functions.hDialog();
                                Toast.makeText(cont, "no data found", Toast.LENGTH_LONG).show();
                            }

//                            else if (response.length() == limit )
//                            {
//                                for (int i = 0; i < response.length(); i++)
//                                {
//                                    visualAssort = gson.fromJson(response.get(i).toString(), VisualAssort.class);
//                                    visualassortmentlist.add(visualAssort);
//                                }
//                                offsetvalue = (limit * count) + limit;
//                                count++;
//                                requestdisplayVisualAssortment();
//
//                            } else if (response.length() < limit)
//                            {

                             else {

                                for (int i = 0; i < response.length(); i++)
                                {
                                    visualAssort = gson.fromJson(response.get(i).toString(), VisualAssort.class);
                                    visualassortmentlist.add(visualAssort);
                                }

                                VisualAssortmentPagerAdapter adapter = new VisualAssortmentPagerAdapter(cont,getSupportFragmentManager(), visualassortmentlist);
                                viewPager_VAssortment.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                viewPager_VAssortment.setOnPageChangeListener(new CircularViewPagerHandler(viewPager_VAssortment));
                                Reusable_Functions.hDialog();
                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(cont, "no data found", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(cont, "no data found", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization","Bearer "+bearertoken);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);


    }


}




