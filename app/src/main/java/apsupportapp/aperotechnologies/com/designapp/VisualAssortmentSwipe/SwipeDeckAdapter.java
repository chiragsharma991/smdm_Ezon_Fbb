package apsupportapp.aperotechnologies.com.designapp.VisualAssortmentSwipe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
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
import apsupportapp.aperotechnologies.com.designapp.ProductInformation.StyleDetailsBean;
import apsupportapp.aperotechnologies.com.designapp.ProductInformation.SwitchingTabActivity;
import apsupportapp.aperotechnologies.com.designapp.model.VisualAssort;


public class SwipeDeckAdapter extends BaseAdapter {

    private Context context;
    SwipeDeck cardStack;
    View layoutView;
    static RelativeLayout rellike, reldislike,


    relbuy, relcomment;
    static ImageButton btnlike, btndislike, btnbuy, btncomment;
    ArrayList<VisualAssort> visualassortmentlist;
    ImageView img_VisualAssortment;
    TextView txtName, txtSeason, txtColor, txtFabric, txtFit, txtCollection, txtSizeRatio, txtAmount;
    SharedPreferences sharedPreferences;
    String userId, bearertoken,geoLevel2Code, storeCode, body_geoLevel2Code;
    ProgressBar visualprogressPicaso;
    int pos;
    static LinearLayout fragmentLayout;
    String TAG = "VisualAssortmentActivity";
    Gson gson;
    ArrayList<StyleDetailsBean> optionList;
    StyleDetailsBean styleDetailsBean;
    int offset , limit;

    public SwipeDeckAdapter(ArrayList<VisualAssort> visualassortmentlist, Context context, SwipeDeck cardStack, String storeCode, String body_geoLevel2Code)
    {
        this.visualassortmentlist = visualassortmentlist;
        this.context = context;
        this.cardStack = cardStack;
        this.storeCode = storeCode;
        this.body_geoLevel2Code = body_geoLevel2Code;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLevel2Code = sharedPreferences.getString("concept","");
        pos = 0;
        optionList = new ArrayList<StyleDetailsBean>();
        gson = new Gson();
        offset = 0;
        limit = 10;

    }

    @Override
    public int getCount() {
        return visualassortmentlist.size();
    }

    @Override
    public Object getItem(int position) {
        return visualassortmentlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = LayoutInflater.from(context);
        layoutView = inflater.inflate(R.layout.fragment_visual_assort, parent, false);
        rellike = (RelativeLayout) layoutView.findViewById(R.id.imgrellike);
        reldislike = (RelativeLayout) layoutView.findViewById(R.id.imgreldislike);
        relbuy = (RelativeLayout) layoutView.findViewById(R.id.imgrelbuy);
        relcomment = (RelativeLayout) layoutView.findViewById(R.id.imgrelcomment);
        btnlike = (ImageButton) layoutView.findViewById(R.id.imgbtnlike);
        btndislike = (ImageButton) layoutView.findViewById(R.id.imgbtndislike);
        btnbuy = (ImageButton) layoutView.findViewById(R.id.imgbtnbuy);
        btncomment = (ImageButton) layoutView.findViewById(R.id.imgbtncomment);

        img_VisualAssortment = (ImageView) layoutView.findViewById(R.id.card_image);
        visualprogressPicaso= (ProgressBar)layoutView.findViewById(R.id.visualprogressPicaso);
        visualprogressPicaso.setVisibility(View.VISIBLE);
        txtName = (TextView) layoutView.findViewById(R.id.txtname);
        txtSeason = (TextView) layoutView.findViewById(R.id.txtSeason);
        txtColor = (TextView) layoutView.findViewById(R.id.txtColor);
        txtFabric = (TextView) layoutView.findViewById(R.id.txtFabric);
        txtFit = (TextView) layoutView.findViewById(R.id.txtFit);
        txtCollection = (TextView) layoutView.findViewById(R.id.txtCollection);
        txtSizeRatio = (TextView) layoutView.findViewById(R.id.txtSizeRatio);
        txtAmount = (TextView) layoutView.findViewById(R.id.txtAmount);

        relbuy.setTag(position);
        relcomment.setTag(position);
        final VisualAssort visualAssort = visualassortmentlist.get(position);
        txtName.setText(visualAssort.getArticleOption());
        //Option Click event to get detail information
//        txtName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                if (Reusable_Functions.chkStatus(context))
//                {
//                    Reusable_Functions.hDialog();
//                    Reusable_Functions.sDialog(context, "Loading  data...");
//                    requestOptionDetailsAPI(visualAssort.getArticleOption());
//                } else
//                {
//                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
        txtSeason.setText("" + visualAssort.getSeasonName());
        txtColor.setText(" " + visualAssort.getColor());
        txtFabric.setText("" + visualAssort.getProductFabricDesc());
        txtFit.setText("" + visualAssort.getProductFitDesc());
        txtCollection.setText("" + visualAssort.getCollectionName());
        txtAmount.setText("\u20B9 " + visualAssort.getUnitGrossPrice());
        txtSizeRatio.setText(" " + visualAssort.getSize());

        if(!visualAssort.getProdImageURL().equals(""))
        {
            Glide.with(this.context)
                    .load(visualAssort.getProdImageURL())
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            visualprogressPicaso.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            visualprogressPicaso.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(img_VisualAssortment)
            ;

        }else {
            visualprogressPicaso.setVisibility(View.GONE);

            Glide.with(this.context).
                    load(R.mipmap.img_placeholder).
                    into(img_VisualAssortment);



        }

        if (visualAssort.getLikeDislikeFlg() == null) {
            btnlike.setBackgroundResource(R.mipmap.likeunselected);
            btndislike.setBackgroundResource(R.mipmap.dislikeunselected);
        } else if (visualAssort.getLikeDislikeFlg().equals("1")) {
            btnlike.setBackgroundResource(R.mipmap.likeunselected);
            rellike.setBackgroundColor(Color.parseColor("#ffffff"));
            btndislike.setBackgroundResource(R.mipmap.dislikeselected);
        } else if (visualAssort.getLikeDislikeFlg().equals("0")) {
            btnlike.setBackgroundResource(R.mipmap.likeselected);
            btndislike.setBackgroundResource(R.mipmap.dislikeunselected);
            reldislike.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            btnlike.setBackgroundResource(R.mipmap.likeunselected);
            btndislike.setBackgroundResource(R.mipmap.dislikeunselected);
        }


        rellike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(VisualAssortmentActivity.likeDislikeFlg.equals("Pending")) {

                    if (VisualAssortmentActivity.layoutComment.getVisibility() == View.VISIBLE) {
                        VisualAssortmentActivity.layoutComment.setVisibility(View.GONE);
                    }

                    if (VisualAssortmentActivity.layoutBuy.getVisibility() == View.VISIBLE) {
                        VisualAssortmentActivity.layoutBuy.setVisibility(View.GONE);
                    }
                    InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputManager != null) {
                        inputManager.hideSoftInputFromWindow(VisualAssortmentActivity.edtTextComment.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        inputManager.hideSoftInputFromWindow(VisualAssortmentActivity.edtTextSets.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }

                    RelativeLayout rel1 = (RelativeLayout) v;
                    LinearLayout parent = (LinearLayout) rel1.getParent();
                    RelativeLayout rel2 = (RelativeLayout) parent.getChildAt(1);

                    ImageButton btn1 = (ImageButton) rel1.getChildAt(0);
                    btn1.setBackgroundResource(R.mipmap.likeunselected);
                    rel1.setBackgroundColor(Color.parseColor("#ffffff"));
                    ImageButton btn2 = (ImageButton) rel2.getChildAt(0);
                    btn2.setBackgroundResource(R.mipmap.dislikeselected);
                    rel2.setBackgroundColor(Color.parseColor("#ffffff"));
                    cardStack.swipeTopCardRight(180);
                }
                else
                {
                    Log.e("like click disabled","-------");
                }
            }
        });

        reldislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(VisualAssortmentActivity.likeDislikeFlg.equals("Pending"))
                 {
                     if (VisualAssortmentActivity.layoutComment.getVisibility() == View.VISIBLE)
                     {
                         VisualAssortmentActivity.layoutComment.setVisibility(View.GONE);
                     }

                     if (VisualAssortmentActivity.layoutBuy.getVisibility() == View.VISIBLE)
                     {
                         VisualAssortmentActivity.layoutBuy.setVisibility(View.GONE);
                     }
                     InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                     if (inputManager != null)
                     {
                         inputManager.hideSoftInputFromWindow(VisualAssortmentActivity.edtTextComment.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                         inputManager.hideSoftInputFromWindow(VisualAssortmentActivity.edtTextSets.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                     }

                     RelativeLayout rel2 = (RelativeLayout) v;
                     LinearLayout parent = (LinearLayout) rel2.getParent();
                     RelativeLayout rel1 = (RelativeLayout) parent.getChildAt(0);

                     ImageButton btn1 = (ImageButton) rel1.getChildAt(0);
                     btn1.setBackgroundResource(R.mipmap.likeselected);
                     rel1.setBackgroundColor(Color.parseColor("#ffffff"));
                     ImageButton btn2 = (ImageButton) rel2.getChildAt(0);
                     btn2.setBackgroundResource(R.mipmap.dislikeunselected);
                     rel2.setBackgroundColor(Color.parseColor("#ffffff"));
                     cardStack.swipeTopCardLeft(180);
                 }
                else
                 {
                     Log.e("Click disabled","----");
                 }

            }
        });

        relbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.setBackgroundResource(R.drawable.button_click);
                int position = (int) v.getTag();

                VisualAssort visualAssort1 = visualassortmentlist.get(position);
                VisualAssortmentActivity.txtSize.setText(visualAssort1.getSize());
                VisualAssortmentActivity.edtTextSets.setText("");
                pos = position;

                if (VisualAssortmentActivity.layoutComment.getVisibility() == View.VISIBLE)
                {
                    VisualAssortmentActivity.layoutComment.setVisibility(View.GONE);
                }

                if (VisualAssortmentActivity.layoutBuy.getVisibility() == View.GONE) {
                    VisualAssortmentActivity.layoutBuy.setVisibility(View.VISIBLE);
                } else if (VisualAssortmentActivity.layoutBuy.getVisibility() == View.VISIBLE) {
                    VisualAssortmentActivity.layoutBuy.setVisibility(View.GONE);
                }
            }
        });

        VisualAssortmentActivity.btnBuyDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                int position = pos;
//
//                VisualAssort visualAssort1 = visualassortmentlist.get(position);
//
                if (VisualAssortmentActivity.edtTextSets.getText().toString().equals(""))
                {
                    Toast.makeText(context, "Enter some value", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    cardStack.swipeTopCardRight(180);
                    InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputManager != null) {
                    inputManager.hideSoftInputFromWindow(VisualAssortmentActivity.edtTextSets.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
//                    Reusable_Functions.sDialog(context, "Loading..");
//                    String articleOption = visualAssort1.getArticleOption();
//                    String checkLikedislike = visualAssort1.getLikeDislikeFlg();
//                    if(checkLikedislike == null)
//                    {
//                        checkLikedislike = "";
//                    }
//                    String checkFeedback = visualAssort1.getFeedback();
//                    if(checkFeedback == null)
//                    {
//                        checkFeedback = "";
//                    }
//                    int checkSizeSet = visualAssort1.getSizeSet();
//
//                    JSONObject obj = new JSONObject();
//                    try {
//                        obj.put("articleOption", articleOption);
//                        obj.put("likeDislikeFlg", checkLikedislike);
//                        obj.put("feedback", checkFeedback);
//                        obj.put("sizeSet", Integer.parseInt(VisualAssortmentActivity.edtTextSets.getText().toString()));
//                    }
//                    catch (JSONException e)
//                    {
//                        e.printStackTrace();
//                    }
//
//
//                    if(checkLikedislike.equals("") && checkSizeSet == 0 && (checkFeedback.equals("")))
//                    {
//
//                        //GO FOR POST METHOD
//                        VisualAssortmentCommentAPI.requestSaveComment(userId, bearertoken, obj, context);
//                        VisualAssortmentActivity.layoutBuy.setVisibility(View.GONE);
//                        relbuy.setEnabled(false);
//                        visualAssort1.setSizeSet(Integer.parseInt(VisualAssortmentActivity.edtTextSets.getText().toString()));
//                    }
//                    else
//                    {
//                        //GO FOR PUT METHOD
//                        VisualAssortmentCommentAPI.requestUpdateSaveComment(userId, bearertoken, obj, context);
//                        VisualAssortmentActivity.layoutBuy.setVisibility(View.GONE);
//                        relbuy.setEnabled(false);
//                        visualAssort1.setSizeSet(Integer.parseInt(VisualAssortmentActivity.edtTextSets.getText().toString()));
//                    }
//                }
//
//
            }
        });

        relcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.setBackgroundResource(R.drawable.button_click);

                int position = (int) v.getTag();

                VisualAssort visualAssort1 = visualassortmentlist.get(position);

                VisualAssortmentActivity.edtTextComment.setText(visualAssort1.getFeedback());

                pos = position;
                if (VisualAssortmentActivity.layoutBuy.getVisibility() == View.VISIBLE) {
                    VisualAssortmentActivity.layoutBuy.setVisibility(View.GONE);
                }

                if (VisualAssortmentActivity.layoutComment.getVisibility() == View.GONE) {
                    VisualAssortmentActivity.layoutComment.setVisibility(View.VISIBLE);
                } else if (VisualAssortmentActivity.layoutComment.getVisibility() == View.VISIBLE) {
                    VisualAssortmentActivity.layoutComment.setVisibility(View.GONE);
                }
            }
        });

        VisualAssortmentActivity.btnCommentDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int position = pos;
                VisualAssort visualAssort1 = visualassortmentlist.get(position);
                if (VisualAssortmentActivity.edtTextComment.getText().toString().trim().equals("")) {
                    Toast.makeText(context, "Enter some value", Toast.LENGTH_SHORT).show();
                } else {
                    String articleOption = visualAssort1.getArticleOption();
                    String checkLikedislike = visualAssort1.getLikeDislikeFlg();
                    if(checkLikedislike == null)
                    {
                        checkLikedislike = "";
                    }
                    String checkFeedback = visualAssort1.getFeedback();
                    if(checkFeedback == null)
                    {
                        checkFeedback = "";
                    }
                    int checkSizeSet = visualAssort1.getSizeSet();

                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("articleOption", articleOption);
                        obj.put("likeDislikeFlg", checkLikedislike);
                        obj.put("feedback", VisualAssortmentActivity.edtTextComment.getText().toString().trim());
                        obj.put("sizeSet", checkSizeSet);
                        obj.put("storecode", storeCode);
                        obj.put("geoLevel2Code", body_geoLevel2Code);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(checkLikedislike.equals("") && checkSizeSet == 0 && (checkFeedback.equals("")))
                    {
                        //GO FOR POST METHOD
                        VisualAssortmentCommentAPI.requestSaveComment(userId, bearertoken, obj, context, storeCode);
                        VisualAssortmentActivity.layoutComment.setVisibility(View.GONE);
                        relcomment.setEnabled(false);
                        visualAssort1.setFeedback(VisualAssortmentActivity.edtTextComment.getText().toString());
                    }
                    else
                    {
                        //GO FOR PUT METHOD
                        VisualAssortmentCommentAPI.requestUpdateSaveComment(userId, bearertoken, obj, context,storeCode);
                        VisualAssortmentActivity.layoutComment.setVisibility(View.GONE);
                        relcomment.setEnabled(false);
                        visualAssort1.setFeedback(VisualAssortmentActivity.edtTextComment.getText().toString());
                    }

                }

                InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputManager != null) {
                    inputManager.hideSoftInputFromWindow(VisualAssortmentActivity.edtTextComment.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }

            }

        });
        return layoutView;

    }

    private void requestOptionDetailsAPI(String option)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        String userId = sharedPreferences.getString("userId", "");
        final String bearertoken = sharedPreferences.getString("bearerToken", "");
        String geoLevel2Code = sharedPreferences.getString("concept", "");
        String lobId = sharedPreferences.getString("lobid", "");
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();

        String url;

        url = ConstsCore.web_url + "/v1/display/productdetailsNew/" + userId + "?articleOption=" + option.replaceAll(" ", "%20").replaceAll("&", "%26") +"&geoLevel2Code="+geoLevel2Code + "&lobId="+lobId;

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            int i;
                            if (response.equals("") || response == null || response.length() == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "No data found", Toast.LENGTH_LONG).show();
                            } else if(response.length() < limit){
                                Reusable_Functions.hDialog();
                                for ( i = 0; i < response.length(); i++) {

                                    styleDetailsBean = gson.fromJson(response.get(i).toString(), StyleDetailsBean.class);
                                    optionList.add(styleDetailsBean);

                                }
                                Intent intent = new Intent(context, SwitchingTabActivity.class);
                                intent.putExtra("checkFrom","visualAssortment");
                                intent.putExtra("articleCode",styleDetailsBean.getArticleCode());
                                intent.putExtra("articleOption",styleDetailsBean.getArticleOption());
                                intent.putExtra("styleDetailsBean", styleDetailsBean);
                                context.startActivity(intent);
                                VisualAssortmentActivity.Visual_Assortment_Activity.finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Toast.makeText(context, "Network connectivity fail", Toast.LENGTH_LONG).show();
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


}