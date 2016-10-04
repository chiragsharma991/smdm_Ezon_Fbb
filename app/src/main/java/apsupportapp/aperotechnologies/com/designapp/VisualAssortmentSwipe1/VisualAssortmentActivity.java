package apsupportapp.aperotechnologies.com.designapp.VisualAssortmentSwipe1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.daprlabs.cardstack.SwipeDeck;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.DashBoardActivity;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.model.VisualAssort;

public class VisualAssortmentActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private SwipeDeck cardStack;
    private Context context = this;
    ArrayList<VisualAssort> visualassortmentlist;
    private SwipeDeckAdapter adapter;

    SharedPreferences sharedPreferences;
    String userId, bearertoken;

    RequestQueue queue;
    Gson gson;
    VisualAssort visualAssort;
    int offsetvalue=0,limit=100;
    int count=0;



    RelativeLayout btnlike, btndislike, btnbuy, btncomment;
    LinearLayout layoutBuy, layoutComment;
    Button btnBuyDone, btnCommentDone;
    EditText edtTextSets, edtTextComment;

    int pos = 0;
    String articleOption;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_deck);
        getSupportActionBar().hide();

        context = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = sharedPreferences.getString("userId","");
        bearertoken = sharedPreferences.getString("bearerToken","");


        cardStack = (SwipeDeck) findViewById(R.id.swipe_deck);
        cardStack.setHardwareAccelerationEnabled(true);

        visualassortmentlist = new ArrayList<VisualAssort>();
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();

        if (Reusable_Functions.chkStatus(context)) {

            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            offsetvalue = 0;
            limit = 100;
            count = 0;
            pos = 0;
            requestdisplayVisualAssortment();

        } else
        {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
        }


        btnlike = (RelativeLayout) findViewById(R.id.imgbtnlike);
        btndislike = (RelativeLayout) findViewById(R.id.imgbtndislike);
        btnbuy = (RelativeLayout) findViewById(R.id.imgbtnbuy);
        btncomment = (RelativeLayout) findViewById(R.id.imgbtncomment);
        layoutBuy = (LinearLayout) findViewById(R.id.layoutBuy);
        layoutComment = (LinearLayout) findViewById(R.id.layoutComment);
        btnBuyDone = (Button) findViewById(R.id.btnBuyDone);
        btnCommentDone = (Button) findViewById(R.id.btnCommentDone);
        edtTextSets = (EditText) findViewById(R.id.edtTextSets);
        edtTextComment = (EditText) findViewById(R.id.edtTextComment);



        edtTextSets.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            Boolean handled = false;
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_NEXT) || (actionId == EditorInfo.IME_ACTION_NONE)) {
                    edtTextSets.clearFocus();
                    InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(inputManager != null){
                        inputManager.hideSoftInputFromWindow(edtTextSets.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    handled = true;
                }
                return handled;
            }

        });


        edtTextComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            Boolean handled = false;
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_NEXT) || (actionId == EditorInfo.IME_ACTION_NONE)) {
                    edtTextComment.clearFocus();
                    handled = true;
                }
                return handled;
            }

        });

        btnlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click of like button","");
//                ImageButton btn = (ImageButton) btnlike.getChildAt(0);
//                btn.setBackgroundResource(R.mipmap.like_selected);
                cardStack.swipeTopCardRight(180);
            }
        });

        btndislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click of dislike button","");
//                ImageButton btn = (ImageButton) btndislike.getChildAt(0);
//                btn.setBackgroundResource(R.mipmap.dislike_selected);
                cardStack.swipeTopCardLeft(180);
            }
        });


        btnbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click of buy button",""+pos);

                articleOption = visualassortmentlist.get(pos).getArticleOption();


                edtTextSets.setText("0");

                if(layoutComment.getVisibility() == View.VISIBLE)
                {
                    layoutComment.setVisibility(View.GONE);
                }

                if(layoutBuy.getVisibility() == View.GONE)
                {
                    layoutBuy.setVisibility(View.VISIBLE);
                }
                else if(layoutBuy.getVisibility() == View.VISIBLE)
                {
                    layoutBuy.setVisibility(View.GONE);
                }


            }
        });

        btnBuyDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("click of buy done button","");
                layoutBuy.setVisibility(View.GONE);
                edtTextSets.setText("0");
                //btnbuy.setEnabled(false);
                InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if(inputManager != null){
                    inputManager.hideSoftInputFromWindow(edtTextSets.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }

        });


        btncomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("---"," "+cardStack);

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
//
//                    VisualAssortmentCommentAPI.requestFetchComment(userId, bearertoken, visualassortmentlist.get(position).getArticleOption(), "comment", "1", context);
//                }
//                else if(layoutComment.getVisibility() == View.VISIBLE)
//                {
//                    layoutComment.setVisibility(View.GONE);
//                }
            }
        });

        btnCommentDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("click of comment done button","");

                layoutComment.setVisibility(View.GONE);
                edtTextComment.setText("");
                //btncomment.setEnabled(false);
                InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if(inputManager != null){
                    inputManager.hideSoftInputFromWindow(edtTextComment.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }

        });

        cardStack.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                Log.i("MainActivity", "card was swiped left, position in adapter: " + position);
                //dislike
                pos++;
                VisualAssortmentCommentAPI.requestFetchComment(userId, bearertoken, visualassortmentlist.get(position).getArticleOption(), "like", "1", context);

            }

            @Override
            public void cardSwipedRight(int position) {
                Log.i("MainActivity", "card was swiped right, position in adapter: " + position);
                //like
                pos++;
                VisualAssortmentCommentAPI.requestFetchComment(userId, bearertoken, visualassortmentlist.get(position).getArticleOption(), "dislike", "0",context);

            }

            @Override
            public void cardsDepleted() {
                Log.i("MainActivity", "no more cards");
//                adapter = new SwipeDeckAdapter(visualassortmentlist, context);
//                cardStack.setAdapter(adapter);
//                adapter.notifyDataSetChanged();

                if (Reusable_Functions.chkStatus(context))
                {
                    visualassortmentlist = new ArrayList<VisualAssort>();
                    Reusable_Functions.sDialog(context, "Loading data...");
                    pos = 0;
                    requestdisplayVisualAssortment();

                } else
                {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void cardActionDown() {
                Log.i(TAG, "cardActionDown");
            }

            @Override
            public void cardActionUp() {
                Log.i(TAG, "cardActionUp");
            }

        });

//        cardStack.setLeftImage(R.id.left_image);
//        cardStack.setRightImage(R.id.right_image);


    }

    private void requestdisplayVisualAssortment() {

        String url =  ConstsCore.web_url + "/v1/display/visualassortments/" + userId+"?offset="+offsetvalue+"&limit="+ limit;
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
                                Toast.makeText(context, "no data found", Toast.LENGTH_LONG).show();
                            }
                            else if (response.length() == limit)
                            {
                                for (int i = 0; i < response.length(); i++)
                                {
                                    visualAssort = gson.fromJson(response.get(i).toString(), VisualAssort.class);
                                    visualassortmentlist.add(visualAssort);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                //requestdisplayVisualAssortment();

                                adapter = new SwipeDeckAdapter(visualassortmentlist, context, cardStack);
                                cardStack.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                Reusable_Functions.hDialog();

                            }
                            else if (response.length() < limit)
                            {

                                for (int i = 0; i < response.length(); i++)
                                {
                                    visualAssort = gson.fromJson(response.get(i).toString(), VisualAssort.class);
                                    visualassortmentlist.add(visualAssort);
                                }
                                offsetvalue = 0;
                                count = 0;

                                adapter = new SwipeDeckAdapter(visualassortmentlist, context, cardStack);
                                cardStack.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                Reusable_Functions.hDialog();
                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no data found", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "no data found", Toast.LENGTH_LONG).show();
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


    @Override
    public void onBackPressed() {

        Intent i = new Intent(VisualAssortmentActivity.this, DashBoardActivity.class);
        startActivity(i);
        finish();

    }



}
