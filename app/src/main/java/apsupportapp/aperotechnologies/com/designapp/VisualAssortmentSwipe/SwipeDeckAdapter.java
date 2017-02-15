package apsupportapp.aperotechnologies.com.designapp.VisualAssortmentSwipe;

import android.content.Context;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.daprlabs.cardstack.SwipeDeck;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.VisualAssortmentSwipe.*;
import apsupportapp.aperotechnologies.com.designapp.model.VisualAssort;


public class SwipeDeckAdapter extends BaseAdapter {

    private Context context;
    SwipeDeck cardStack;
    View layoutView;
    static RelativeLayout rellike, reldislike, relbuy, relcomment;
    static ImageButton btnlike, btndislike, btnbuy, btncomment;
    ArrayList<VisualAssort> visualassortmentlist;
    ImageView img_VisualAssortment;
    TextView txtName, txtSeason, txtColor, txtFabric, txtFit, txtCollection, txtSizeRatio, txtAmount;
    SharedPreferences sharedPreferences;
    String userId, bearertoken;
    ProgressBar visualprogressPicaso;
    int pos;
    static LinearLayout fragmentLayout;

    public SwipeDeckAdapter(ArrayList<VisualAssort> visualassortmentlist, Context context, SwipeDeck cardStack) {
        this.visualassortmentlist = visualassortmentlist;
        this.context = context;
        this.cardStack = cardStack;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        pos = 0;
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
        layoutView = inflater.inflate(R.layout.fragment_visualassortment, parent, false);
        fragmentLayout = (LinearLayout)layoutView.findViewById(R.id.fragmentLayout);
        rellike = (RelativeLayout) layoutView.findViewById(R.id.imgrellike);
        reldislike = (RelativeLayout) layoutView.findViewById(R.id.imgreldislike);
        relbuy = (RelativeLayout) layoutView.findViewById(R.id.imgrelbuy);
        relcomment = (RelativeLayout) layoutView.findViewById(R.id.imgrelcomment);
        btnlike = (ImageButton) layoutView.findViewById(R.id.imgbtnlike);
        btndislike = (ImageButton) layoutView.findViewById(R.id.imgbtndislike);
        btnbuy = (ImageButton) layoutView.findViewById(R.id.imgbtnbuy);
        btncomment = (ImageButton) layoutView.findViewById(R.id.imgbtncomment);
//        layoutBuy = (LinearLayout) layoutView.findViewById(R.id.layoutBuy);
//        layoutComment = (LinearLayout) layoutView.findViewById(R.id.layoutComment);
//        btnBuyDone = (Button) layoutView.findViewById(R.id.btnBuyDone);
//        btnCommentDone = (Button) layoutView.findViewById(R.id.btnCommentDone);
//        edtTextSets = (EditText) layoutView.findViewById(R.id.edtTextSets);
//        edtTextComment = (EditText) layoutView.findViewById(R.id.edtTextComment);

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

        //img_VisualAssortment.setImageResource(R.drawable.faces1);

        final VisualAssort visualAssort = visualassortmentlist.get(position);
        txtName.setText(visualAssort.getArticleOption());
        txtSeason.setText("Season : " + visualAssort.getSeasonName());
        txtColor.setText("Color : " + visualAssort.getColor());
        txtFabric.setText("Fabric : " + visualAssort.getProductFabricDesc());
        txtFit.setText("Fit : " + visualAssort.getProductFitDesc());
        txtCollection.setText("Collection : " + visualAssort.getCollectionName());
        txtAmount.setText("\u20B9 " + visualAssort.getUnitGrossPrice());
        txtSizeRatio.setText("Size Ratio : " + visualAssort.getSize());
//        if(!visualAssort.getProdImageURL().equals("")) {
//            Picasso.with(this.context).
//
//                    load(visualAssort.getProdImageURL()).
//                    into(img_VisualAssortment, new Callback() {
//                        @Override
//                        public void onSuccess() {
//                            visualprogressPicaso.setVisibility(View.GONE);
//                        }
//
//                        @Override
//                        public void onError() {
//                            visualprogressPicaso.setVisibility(View.GONE);
//
//                        }
//                    });
//        }else {
//            visualprogressPicaso.setVisibility(View.GONE);
//
//            Picasso.with(this.context).
//                    load(R.mipmap.placeholder).
//                    into(img_VisualAssortment);
//
//        }
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



//        if(visualAssort.getLikeDislikeFlg() == null)
//        {
//            btnlike.setBackgroundResource(R.mipmap.like_unselected);
//            btndislike.setBackgroundResource(R.mipmap.dislike_unselected);
//        }
//        else if(visualAssort.getLikeDislikeFlg().equals("1"))
//        {
//            btnlike.setBackgroundResource(R.mipmap.like_selected);
//            btndislike.setBackgroundResource(R.mipmap.dislike_unselected);
//        }
//        else if(visualAssort.getLikeDislikeFlg().equals("0"))
//        {
//            btnlike.setBackgroundResource(R.mipmap.like_unselected);
//            btndislike.setBackgroundResource(R.mipmap.dislike_selected);
//        }
//        else
//        {
//            btnlike.setBackgroundResource(R.mipmap.like_unselected);
//            btndislike.setBackgroundResource(R.mipmap.dislike_unselected);
//        }


        if (visualAssort.getLikeDislikeFlg() == null) {
            btnlike.setBackgroundResource(R.mipmap.like_selected);
            btndislike.setBackgroundResource(R.mipmap.dislike_selected);
        } else if (visualAssort.getLikeDislikeFlg().equals("1")) {
            btnlike.setBackgroundResource(R.mipmap.like_unselected);
            rellike.setBackgroundColor(Color.parseColor("#920609"));
            btndislike.setBackgroundResource(R.mipmap.dislike_selected);
        } else if (visualAssort.getLikeDislikeFlg().equals("0")) {
            btnlike.setBackgroundResource(R.mipmap.like_selected);
            btndislike.setBackgroundResource(R.mipmap.dislike_unselected);
            reldislike.setBackgroundColor(Color.parseColor("#920609"));
        } else {
            btnlike.setBackgroundResource(R.mipmap.like_selected);
            btndislike.setBackgroundResource(R.mipmap.dislike_selected);
        }


        rellike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                btn1.setBackgroundResource(R.mipmap.like_unselected);
                rel1.setBackgroundColor(Color.parseColor("#920609"));
                ImageButton btn2 = (ImageButton) rel2.getChildAt(0);
                btn2.setBackgroundResource(R.mipmap.dislike_selected);
                rel2.setBackgroundColor(Color.parseColor("#B73020"));
                cardStack.swipeTopCardRight(180);

            }
        });

        reldislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                RelativeLayout rel2 = (RelativeLayout) v;
                LinearLayout parent = (LinearLayout) rel2.getParent();
                RelativeLayout rel1 = (RelativeLayout) parent.getChildAt(0);

                ImageButton btn1 = (ImageButton) rel1.getChildAt(0);
                btn1.setBackgroundResource(R.mipmap.like_selected);
                rel1.setBackgroundColor(Color.parseColor("#B73020"));
                ImageButton btn2 = (ImageButton) rel2.getChildAt(0);
                btn2.setBackgroundResource(R.mipmap.dislike_unselected);
                rel2.setBackgroundColor(Color.parseColor("#920609"));
                cardStack.swipeTopCardLeft(180);
            }
        });

        relbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.setBackgroundResource(R.drawable.button_red_effect);
                int position = (int) v.getTag();

                VisualAssort visualAssort1 = visualassortmentlist.get(position);
                VisualAssortmentActivity.txtSize.setText(visualAssort1.getSize());
                VisualAssortmentActivity.edtTextSets.setText(String.valueOf(visualAssort1.getSizeSet()));
                pos = position;
                Log.e("articleOption", " " + visualAssort1.getArticleOption());

                if (VisualAssortmentActivity.layoutComment.getVisibility() == View.VISIBLE) {
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

                int position = pos;

                VisualAssort visualAssort1 = visualassortmentlist.get(position);

                if (VisualAssortmentActivity.edtTextSets.getText().toString().equals("0")) {
                    Toast.makeText(context, "Enter some value", Toast.LENGTH_SHORT).show();
                } else {
                    Reusable_Functions.sDialog(context, "Loading..");
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
                    Log.i("", "order set done click: position " + position + " articleOption " + articleOption + " checkLikedislike " + checkLikedislike + " checkSizeSet " + checkSizeSet);

                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("articleOption", articleOption);
                        obj.put("likeDislikeFlg", checkLikedislike);
                        obj.put("feedback", checkFeedback);
                        obj.put("sizeSet", Integer.parseInt(VisualAssortmentActivity.edtTextSets.getText().toString()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                    if (!checkLikedislike.equals("") || checkSizeSet != 0 || (!checkFeedback.equals(""))) {
//                        //GO FOR PUT METHOD
//                        VisualAssortmentCommentAPI.requestUpdateSaveComment(userId, bearertoken, obj, context);
//                        VisualAssortmentActivity.layoutBuy.setVisibility(View.GONE);
//                        relbuy.setEnabled(false);
//                        visualAssort1.setSizeSet(Integer.parseInt(VisualAssortmentActivity.edtTextSets.getText().toString()));
//                    } else {
//                        //GO FOR POST METHOD
//                        VisualAssortmentCommentAPI.requestSaveComment(userId, bearertoken, obj, context);
//                        VisualAssortmentActivity.layoutBuy.setVisibility(View.GONE);
//                        relbuy.setEnabled(false);
//                        visualAssort1.setSizeSet(Integer.parseInt(VisualAssortmentActivity.edtTextSets.getText().toString()));
//                    }

                    if(checkLikedislike.equals("") && checkSizeSet == 0 && (checkFeedback.equals("")))
                    {

                        //GO FOR POST METHOD
                        VisualAssortmentCommentAPI.requestSaveComment(userId, bearertoken, obj, context);
                        VisualAssortmentActivity.layoutBuy.setVisibility(View.GONE);
                        relbuy.setEnabled(false);
                        visualAssort1.setSizeSet(Integer.parseInt(VisualAssortmentActivity.edtTextSets.getText().toString()));
                    }
                    else
                    {
                        //GO FOR PUT METHOD
                        VisualAssortmentCommentAPI.requestUpdateSaveComment(userId, bearertoken, obj, context);
                        VisualAssortmentActivity.layoutBuy.setVisibility(View.GONE);
                        relbuy.setEnabled(false);
                        visualAssort1.setSizeSet(Integer.parseInt(VisualAssortmentActivity.edtTextSets.getText().toString()));

                    }

                }

                InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputManager != null) {
                    inputManager.hideSoftInputFromWindow(VisualAssortmentActivity.edtTextSets.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }

        });


        relcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.setBackgroundResource(R.drawable.button_red_effect);

                int position = (int) v.getTag();

                VisualAssort visualAssort1 = visualassortmentlist.get(position);

                VisualAssortmentActivity.edtTextComment.setText(visualAssort1.getFeedback());

                pos = position;

                Log.e("articleOption", " " + visualAssort1.getArticleOption());

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

                Log.e("click of comment done button", "");

                int position = pos;

                VisualAssort visualAssort1 = visualassortmentlist.get(position);

                if (VisualAssortmentActivity.edtTextComment.getText().toString().trim().equals("")) {
                    Toast.makeText(context, "Enter some value", Toast.LENGTH_SHORT).show();
                } else {
                    Reusable_Functions.sDialog(context, "Loading..");
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
                    Log.i("", "comment done click: position " + position + " articleOption " + articleOption + " checkLikedislike " + checkLikedislike + " checkSizeSet " + checkSizeSet + " checkFeedback " + checkFeedback);

                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("articleOption", articleOption);
                        obj.put("likeDislikeFlg", checkLikedislike);
                        obj.put("feedback", VisualAssortmentActivity.edtTextComment.getText().toString().trim());
                        obj.put("sizeSet", checkSizeSet);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                    if (!checkLikedislike.equals("") || checkSizeSet != 0 || (!checkFeedback.equals(""))) {
//                        //GO FOR PUT METHOD
//                        VisualAssortmentCommentAPI.requestUpdateSaveComment(userId, bearertoken, obj, context);
//                        VisualAssortmentActivity.layoutComment.setVisibility(View.GONE);
//                        relcomment.setEnabled(false);
//                        visualAssort1.setFeedback(VisualAssortmentActivity.edtTextComment.getText().toString());
//                    } else {
//                        //GO FOR POST METHOD
//                        VisualAssortmentCommentAPI.requestSaveComment(userId, bearertoken, obj, context);
//                        VisualAssortmentActivity.layoutComment.setVisibility(View.GONE);
//                        relcomment.setEnabled(false);
//                        visualAssort1.setFeedback(VisualAssortmentActivity.edtTextComment.getText().toString());
//                    }

                    if(checkLikedislike.equals("") && checkSizeSet == 0 && (checkFeedback.equals("")))
                    {

                        //GO FOR POST METHOD
                        VisualAssortmentCommentAPI.requestSaveComment(userId, bearertoken, obj, context);
                        VisualAssortmentActivity.layoutComment.setVisibility(View.GONE);
                        relcomment.setEnabled(false);
                        visualAssort1.setFeedback(VisualAssortmentActivity.edtTextComment.getText().toString());
                    }
                    else
                    {
                        //GO FOR PUT METHOD
                        VisualAssortmentCommentAPI.requestUpdateSaveComment(userId, bearertoken, obj, context);
                        VisualAssortmentActivity.layoutComment.setVisibility(View.GONE);
                        relcomment.setEnabled(false);
                        visualAssort1.setFeedback(VisualAssortmentActivity.edtTextComment.getText().toString());

                    }

                }

                //layoutComment.setVisibility(View.GONE);
                //edtTextComment.setText("");
                //btncomment.setEnabled(false);
                InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputManager != null) {
                    inputManager.hideSoftInputFromWindow(VisualAssortmentActivity.edtTextComment.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }

            }

        });
        return layoutView;

    }
}