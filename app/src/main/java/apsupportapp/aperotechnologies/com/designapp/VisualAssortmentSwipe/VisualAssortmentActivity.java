package apsupportapp.aperotechnologies.com.designapp.VisualAssortmentSwipe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesFilterActivity;
import apsupportapp.aperotechnologies.com.designapp.model.VisualAssort;

import static apsupportapp.aperotechnologies.com.designapp.VisualAssortmentSwipe.SwipeDeckAdapter.relbuy;

public class VisualAssortmentActivity extends AppCompatActivity {

    private static final String TAG = "VisualAssortmentActivity";
    RelativeLayout reloverlay;
    private SwipeDeck cardStack;
    private Context context ;
    ArrayList<VisualAssort> visualassortmentlist;
    SwipeDeckAdapter adapter;
    static String likeDislikeFlg ;
    SharedPreferences sharedPreferences;
    String userId, bearertoken,storeCode,geoLevel2Code, lobId;
    RadioButton visualAssort_PendingChk,visualAssort_CompletedChk;
    LinearLayout visualAssort_Pending,visualAssort_Completed;
    RequestQueue queue;
    Gson gson;
    VisualAssort visualAssort;
    int offsetvalue=0,limit=100;
    int count=0;
    public boolean vassort_from_filter=false;
    RelativeLayout imgBtnBack,visualsort,visualAssortSortLayout,vassort_imgFilter;
    public static RelativeLayout layoutBuy, layoutComment;
    static Button btnBuyDone;
    static Button btnCommentDone;
    public static EditText edtTextSets, edtTextComment;
    static TextView txtSize;
    private TextView txtStoreCode,txtStoreName;
    private LinearLayout SwipeLayout;
    public String selectedString="";
    boolean flag = false;
   JsonArrayRequest postRequest;
    public static Activity Visual_Assortment_Activity;
    String recache = "";
    int maxCharactes ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_deck);
        getSupportActionBar().hide();

        context = this;
        Visual_Assortment_Activity = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = sharedPreferences.getString("userId","");
        geoLevel2Code = sharedPreferences.getString("concept","");
        lobId = sharedPreferences.getString("lobid","");

//        userId = userId.substring(0,userId.length()-5);
//        Log.e("userId",""+userId);
        bearertoken = sharedPreferences.getString("bearerToken","");
       // storeCode = sharedPreferences.getString("storeDescription","");
        storeCode = getIntent().getExtras().getString("storeCode");
        reloverlay = (RelativeLayout) findViewById(R.id.reloverlay);
        cardStack = (SwipeDeck) findViewById(R.id.swipe_deck);
        cardStack.setHardwareAccelerationEnabled(true);

        visualassortmentlist = new ArrayList<VisualAssort>();
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();


        reloverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloverlay.setVisibility(View.GONE);
            }
        });

        txtStoreCode = (TextView)findViewById(R.id.txtStoreCode);
//        txtStoreCode.setText(storeCode.trim().substring(0,4));
//        txtStoreName = (TextView)findViewById(R.id.txtStoreName);
//        txtStoreName.setText(storeCode.substring(5));
        imgBtnBack = (RelativeLayout) findViewById(R.id.imageBtnBack);
        visualsort = (RelativeLayout)findViewById(R.id.visualsort);
        SwipeLayout = (LinearLayout)findViewById(R.id.swipeLayout);
        vassort_imgFilter = (RelativeLayout)findViewById(R.id.vassort_imgfilter);
        visualAssortSortLayout = (RelativeLayout)findViewById(R.id.visualAssortSortLayout);
        visualAssortSortLayout.setVisibility(View.GONE);
        visualAssort_Pending = (LinearLayout)findViewById(R.id.visualAssort_Pending);
        visualAssort_Completed = (LinearLayout)findViewById(R.id.visualAssort_Completed);
        visualAssort_PendingChk = (RadioButton)findViewById(R.id.visualAssort_PendingChk);
        visualAssort_CompletedChk =(RadioButton)findViewById(R.id.visualAssort_CompletedChk);
        layoutBuy = (RelativeLayout) findViewById(R.id.layoutBuy);
        layoutComment = (RelativeLayout) findViewById(R.id.layoutComment);
        btnBuyDone = (Button) findViewById(R.id.btnBuyDone);
        btnCommentDone = (Button) findViewById(R.id.btnCommentDone);
        edtTextSets = (EditText) findViewById(R.id.edtTextSets);
        edtTextComment = (EditText) findViewById(R.id.edtTextComment);
        txtSize = (TextView) findViewById(R.id.txtSize);


        maxCharactes = 500;
        edtTextComment.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxCharactes)});


        edtTextSets.setFilters(new InputFilter[]{new InputFilterMinMax(0, 10000)});



            imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackClick();
            }
        });


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

        LinearLayout linlayoutComment = (LinearLayout) findViewById(R.id.linlayoutComment);
        linlayoutComment.setOnClickListener(null);
        LinearLayout linlayoutBuy = (LinearLayout) findViewById(R.id.linlayoutBuy);
        linlayoutBuy.setOnClickListener(null);

        layoutComment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                layoutComment.setVisibility(View.GONE);
                InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if(inputManager != null){
                    inputManager.hideSoftInputFromWindow(VisualAssortmentActivity.edtTextComment.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });


        layoutBuy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                layoutBuy.setVisibility(View.GONE);
                InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if(inputManager != null){
                    inputManager.hideSoftInputFromWindow(VisualAssortmentActivity.edtTextSets.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });



        btnBuyDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                layoutBuy.setVisibility(View.GONE);

                InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if(inputManager != null){
                    inputManager.hideSoftInputFromWindow(edtTextSets.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }

        });


        btnCommentDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                layoutComment.setVisibility(View.GONE);

                InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if(inputManager != null){
                    inputManager.hideSoftInputFromWindow(edtTextComment.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }

        });

        //Visual Sort
        visualAssort_PendingChk.setChecked(true);
        visualsort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visual_sort_function();
            }
        });
        visualAssort_Pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visualAssort_pendingFunction();
            }
        });
        visualAssort_Completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visualAssort_CompletedFunction();
            }
        });
        visualAssortSortLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visualAssortSortLayout.setVisibility(View.GONE);
            }
        });
        vassort_imgFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VisualAssortmentActivity.this, SalesFilterActivity.class);
                intent.putExtra("checkfrom", "visualAssort");

                startActivity(intent);
            }
        });

        if (Reusable_Functions.chkStatus(context)) {

            reloverlay.setVisibility(View.VISIBLE);
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            offsetvalue = 0;
            limit = 100;
            count = 0;
            recache = "true";
            likeDislikeFlg = "Pending";
            btnCommentDone.setVisibility(View.VISIBLE);
            btnBuyDone.setVisibility(View.VISIBLE);
            edtTextSets.setEnabled(true);
            edtTextComment.setEnabled(true);
            if (postRequest != null) {
                postRequest.cancel();
            }
           visualassortmentlist = new ArrayList<VisualAssort>();
            if (getIntent().getStringExtra("selectedDept") == null) {
                vassort_from_filter=false;
            }
            else if(getIntent().getStringExtra("selectedDept") != null) {
               selectedString  = getIntent().getStringExtra("selectedDept");
                vassort_from_filter=true;

            }
           requestdisplayVisualAssortment(selectedString);

        } else
        {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
        }



        cardStack.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position)
            {
                if(likeDislikeFlg.equals("Pending"))
                {
                    //dislike
                    VisualAssort visualAssort1 = visualassortmentlist.get(position);
                    String articleOption = visualAssort1.getArticleOption();
                    String checkLikedislike = visualAssort1.getLikeDislikeFlg();
                    if (checkLikedislike == null) {
                        checkLikedislike = "";
                    }
                    String checkFeedback = visualAssort1.getFeedback();
                    if (checkFeedback == null) {
                        checkFeedback = "";
                    }
                    int checkSizeSet = visualAssort1.getSizeSet();

                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("articleOption", articleOption);
                        obj.put("likeDislikeFlg", "0");
                        obj.put("feedback", checkFeedback);
                        obj.put("sizeSet", checkSizeSet);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (checkLikedislike.equals("") && checkSizeSet == 0 && (checkFeedback.equals("")))
                    {

                        //GO FOR POST METHOD
                        if (postRequest != null)
                        {
                            postRequest.cancel();
                        }
                        VisualAssortmentCommentAPI.requestSaveComment(userId, bearertoken, obj, context, geoLevel2Code);
                        visualAssort1.setLikeDislikeFlg("0");
                    }
                    else
                    {
                        //GO FOR PUT METHOD
                        if (postRequest != null)
                        {
                            postRequest.cancel();
                        }
                        VisualAssortmentCommentAPI.requestUpdateSaveComment(userId, bearertoken, obj, context, geoLevel2Code);
                        visualAssort1.setLikeDislikeFlg("0");
                    }
                }
                else
                {
                    Log.e("came ","here");
                }
            }

            @Override
            public void cardSwipedRight(int position)
            {
                if(likeDislikeFlg.equals("Pending"))
                {
                    //like
                    VisualAssort visualAssort1 = visualassortmentlist.get(position);
                    if(VisualAssortmentActivity.layoutBuy.getVisibility()==View.VISIBLE)
                    {
                        Log.e("edit Text visible","=====");

                            Reusable_Functions.sDialog(context, "Loading..");
                            String articleOption = visualAssort1.getArticleOption();
                            String checkLikedislike = visualAssort1.getLikeDislikeFlg();
                            if (checkLikedislike == null)
                            {
                                checkLikedislike = "";
                            }
                            String checkFeedback = visualAssort1.getFeedback();
                            if (checkFeedback == null)
                            {
                                checkFeedback = "";
                            }

                            JSONObject obj = new JSONObject();
                            try {
                                obj.put("articleOption", articleOption);
                                obj.put("likeDislikeFlg", "1");
                                obj.put("feedback", checkFeedback);
                                obj.put("sizeSet", Integer.parseInt(VisualAssortmentActivity.edtTextSets.getText().toString()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            VisualAssortmentCommentAPI.requestSaveComment(userId, bearertoken, obj, context, geoLevel2Code);
                            VisualAssortmentActivity.layoutBuy.setVisibility(View.GONE);
                            relbuy.setEnabled(false);
                            visualAssort1.setSizeSet(Integer.parseInt(VisualAssortmentActivity.edtTextSets.getText().toString()));
                            visualAssort1.setLikeDislikeFlg("1");
                            edtTextSets.setText("");

                    }
                    else
                    {
                        Log.e("edit Text invisible","=====");
                        relbuy.setEnabled(true);
                        String articleOption = visualAssort1.getArticleOption();
                        String checkLikedislike = visualAssort1.getLikeDislikeFlg();
                        if (checkLikedislike == null) {
                            checkLikedislike = "";
                        }
                        String checkFeedback = visualAssort1.getFeedback();
                        if (checkFeedback == null) {
                            checkFeedback = "";
                        }
                        int checkSizeSet = visualAssort1.getSizeSet();

                        JSONObject obj = new JSONObject();
                        try
                        {
                            obj.put("articleOption", articleOption);
                            obj.put("likeDislikeFlg", "1");
                            obj.put("feedback", checkFeedback);
                            obj.put("sizeSet", checkSizeSet);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        if (checkLikedislike.equals("") && checkSizeSet == 0 && (checkFeedback.equals(""))) {

                            //GO FOR POST METHOD
                            if (postRequest != null) {
                                postRequest.cancel();
                            }
                            VisualAssortmentCommentAPI.requestSaveComment(userId, bearertoken, obj, context,geoLevel2Code);
                            visualAssort1.setLikeDislikeFlg("1");
                        } else {
                            //GO FOR PUT METHOD
                            if (postRequest != null) {
                                postRequest.cancel();
                            }
                            VisualAssortmentCommentAPI.requestUpdateSaveComment(userId, bearertoken, obj, context,geoLevel2Code);
                            visualAssort1.setLikeDislikeFlg("1");
                        }
                    }
                }
                else
                {
                    Log.e("Completed","-------");
                }
            }

            @Override
            public void cardsDepleted()
            {
                Toast.makeText(context, "No more data", Toast.LENGTH_LONG).show();
            }

            @Override
            public void cardActionDown() {
            }

            @Override
            public void cardActionUp() {
            }

        });


 }

    private void visualAssort_CompletedFunction() {
        if (visualAssort_CompletedChk.isChecked())
        {
            visualAssort_CompletedChk.setChecked(true);
            visualAssort_PendingChk.setChecked(false);
            visualAssortSortLayout.setVisibility(View.GONE);
        }
        else if (!visualAssort_CompletedChk.isChecked())
        {
            visualAssort_CompletedChk.setChecked(true);
            visualAssort_PendingChk.setChecked(false);

            if (Reusable_Functions.chkStatus(context))
            {
                Reusable_Functions.hDialog();
                Reusable_Functions.sDialog(context, "Loading data...");
                offsetvalue = 0;
                limit = 100;
                count = 0;
                recache = "true";
                visualassortmentlist = new ArrayList<VisualAssort>();
                cardStack.setVisibility(View.GONE);
                likeDislikeFlg = "Completed";
                btnCommentDone.setVisibility(View.INVISIBLE);
                btnBuyDone.setVisibility(View.INVISIBLE);
                edtTextComment.setEnabled(false);
                edtTextSets.setEnabled(false);
                requestdisplayVisualAssortment(selectedString);
                visualAssortSortLayout.setVisibility(View.GONE);
            }
            else
            {
                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
                visualAssortSortLayout.setVisibility(View.GONE);
            }
        }
    }
    private void visualAssort_pendingFunction()
    {
        if (visualAssort_PendingChk.isChecked())
        {
            visualAssort_CompletedChk.setChecked(false);
            visualAssort_PendingChk.setChecked(true);
            visualAssortSortLayout.setVisibility(View.GONE);
        }
        else if (!visualAssort_PendingChk.isChecked())
        {
            visualAssort_CompletedChk.setChecked(false);
            visualAssort_PendingChk.setChecked(true);

            if(Reusable_Functions.chkStatus(context))
            {
                Reusable_Functions.hDialog();
                Reusable_Functions.sDialog(context, "Loading data...");
                offsetvalue = 0;
                limit = 100;
                count = 0;
                recache = "true";
                cardStack.setVisibility(View.GONE);
                likeDislikeFlg = "Pending";
                visualassortmentlist = new ArrayList<VisualAssort>();
                btnCommentDone.setVisibility(View.VISIBLE);
                btnBuyDone.setVisibility(View.VISIBLE);
                edtTextSets.setEnabled(true);
                edtTextComment.setEnabled(true);
                requestdisplayVisualAssortment(selectedString);
                visualAssortSortLayout.setVisibility(View.GONE);
            }
            else
            {
                Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
                visualAssortSortLayout.setVisibility(View.GONE);

            }
        }
    }

    private void visual_sort_function()
    {
        visualAssortSortLayout.setVisibility(View.VISIBLE);
    }


    private void requestdisplayVisualAssortment(String selectedString) {

        String url;
        if (vassort_from_filter)
        {
            url = ConstsCore.web_url + "/v1/display/visualassortments/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&likedislike=" + likeDislikeFlg + "&level=" + SalesFilterActivity.level_filter + selectedString +"&recache="+ recache + "&geoLevel2Code=" + geoLevel2Code + "&lobId="+ lobId;
        }
        else
        {
            url = ConstsCore.web_url + "/v1/display/visualassortments/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&likedislike=" + likeDislikeFlg +"&recache="+ recache + "&geoLevel2Code=" + geoLevel2Code + "&lobId="+ lobId;
        }

      Log.e("visual assort url :",""+url);
      postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("va response :",""+response);
                       try
                        {
                            int i;
                            if (response.equals("") || response == null || response.length() == 0 && count == 0)
                            {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no data found", Toast.LENGTH_LONG).show();
                                cardStack.setVisibility(View.GONE);
                                reloverlay.setVisibility(View.GONE);

                            }

                            else if (response.length() == limit)
                            {
                                for (i= 0; i < response.length(); i++)
                                {
                                    visualAssort = gson.fromJson(response.get(i).toString(), VisualAssort.class);
                                    visualassortmentlist.add(visualAssort);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                if(visualassortmentlist.size() == 0)
                                {
                                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                    cardStack.setVisibility(View.GONE);
                                    Reusable_Functions.hDialog();
                                }
                                else
                                {
                                    adapter = new SwipeDeckAdapter(visualassortmentlist, context, cardStack);
                                    cardStack.setAdapter(adapter);

                                    adapter.notifyDataSetChanged();
                                    cardStack.setVisibility(View.VISIBLE);
                                    Reusable_Functions.hDialog();
                                }
                            }
                            else if (response.length() < limit)
                            {
                                for ( i = 0; i < response.length(); i++)
                                {
                                    visualAssort = gson.fromJson(response.get(i).toString(), VisualAssort.class);
                                    visualassortmentlist.add(visualAssort);
                                }

                                if(visualassortmentlist.size() == 0)
                                {
                                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                    cardStack.setVisibility(View.GONE);
                                    Reusable_Functions.hDialog();
                                }
                                else
                                {
                                    adapter = new SwipeDeckAdapter(visualassortmentlist, context, cardStack);
                                    cardStack.setAdapter(adapter);

                                    adapter.notifyDataSetChanged();
                                    cardStack.setVisibility(View.VISIBLE);
                                    Reusable_Functions.hDialog();
                                }
                            }
                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                            cardStack.setVisibility(View.GONE);
                            reloverlay.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.getMessage();
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "  no data found", Toast.LENGTH_LONG).show();
                        reloverlay.setVisibility(View.GONE);
                        cardStack.setVisibility(View.GONE);
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
        int socketTimeout  = 60000 ;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    @Override
    public void onBackPressed() {
        onBackClick();
    }


    public void onBackClick()
    {
        likeDislikeFlg = "";
        likeDislikeFlg = "Pending";
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputManager != null)
        {
            inputManager.hideSoftInputFromWindow(VisualAssortmentActivity.edtTextComment.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            inputManager.hideSoftInputFromWindow(VisualAssortmentActivity.edtTextSets.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        if(reloverlay.getVisibility() == View.VISIBLE)
        {
            reloverlay.setVisibility(View.GONE);
        }
         if(VisualAssortmentActivity.layoutComment.getVisibility() == View.VISIBLE)
        {
            VisualAssortmentActivity.layoutComment.setVisibility(View.GONE);
        }
        if(VisualAssortmentActivity.layoutBuy.getVisibility() == View.VISIBLE)
        {
            VisualAssortmentActivity.layoutBuy.setVisibility(View.GONE);
        }
       finish();
    }


    public class InputFilterMinMax implements InputFilter {
        private int min;
        private int max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;

        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            //noinspection EmptyCatchBlock
            try {
                int input = Integer.parseInt(dest.subSequence(0, dstart).toString() + source + dest.subSequence(dend, dest.length()));
                if (isInRange(min, max, input))
                    return null;

                if(max > 10000)
                {
                    Toast.makeText(context,"Please Enter valid set",Toast.LENGTH_SHORT).show();
                }

            } catch (NumberFormatException nfe) { }
            return "";
        }

        private boolean isInRange(int a, int b, int c)
        {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }
}
