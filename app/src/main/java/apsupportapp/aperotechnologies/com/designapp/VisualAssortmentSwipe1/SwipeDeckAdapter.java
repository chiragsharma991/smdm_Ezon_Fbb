package apsupportapp.aperotechnologies.com.designapp.VisualAssortmentSwipe1;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daprlabs.cardstack.SwipeDeck;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.model.VisualAssort;

/**
 * Created by hasai on 27/09/16.
 */
public class SwipeDeckAdapter extends BaseAdapter {

    private Context context;
    SwipeDeck cardStack;

    View layoutView;

    RelativeLayout btnlike, btndislike, btnbuy, btncomment;
    LinearLayout layoutBuy, layoutComment;
    Button btnBuyDone, btnCommentDone;
    EditText edtTextSets, edtTextComment;

    ArrayList<VisualAssort> visualassortmentlist;
    ImageView img_VisualAssortment;
    TextView txtName, txtFabric, txtFit, txtCollection, txtSizeRatio, txtAmount;


    SharedPreferences sharedPreferences;
    String userId, bearertoken;


    public SwipeDeckAdapter(ArrayList<VisualAssort> visualassortmentlist, Context context, SwipeDeck cardStack) {
        this.visualassortmentlist = visualassortmentlist;
        this.context = context;
        this.cardStack = cardStack;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = sharedPreferences.getString("userId","");
        bearertoken = sharedPreferences.getString("bearerToken","");


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


       /* btnlike = (RelativeLayout) layoutView.findViewById(R.id.imgbtnlike);
        btndislike = (RelativeLayout) layoutView.findViewById(R.id.imgbtndislike);
        btnbuy = (RelativeLayout) layoutView.findViewById(R.id.imgbtnbuy);
        btncomment = (RelativeLayout) layoutView.findViewById(R.id.imgbtncomment);
        layoutBuy = (LinearLayout) layoutView.findViewById(R.id.layoutBuy);
        layoutComment = (LinearLayout) layoutView.findViewById(R.id.layoutComment);
        btnBuyDone = (Button) layoutView.findViewById(R.id.btnBuyDone);
        btnCommentDone = (Button) layoutView.findViewById(R.id.btnCommentDone);
        edtTextSets = (EditText) layoutView.findViewById(R.id.edtTextSets);
        edtTextComment = (EditText) layoutView.findViewById(R.id.edtTextComment);*/

        img_VisualAssortment = (ImageView) layoutView.findViewById(R.id.card_image);
        txtName = (TextView) layoutView.findViewById(R.id.txtname);
        txtFabric = (TextView) layoutView.findViewById(R.id.txtFabric);
        txtFit = (TextView) layoutView.findViewById(R.id.txtFit);
        txtCollection = (TextView) layoutView.findViewById(R.id.txtCollection);
        txtSizeRatio = (TextView) layoutView.findViewById(R.id.txtSizeRatio);
        txtAmount = (TextView) layoutView.findViewById(R.id.txtAmount);

        //img_VisualAssortment.setImageResource(R.drawable.faces1);

        VisualAssort visualAssort = visualassortmentlist.get(position);


        txtName.setText(visualAssort.getArticleOption());
        txtFabric.setText("Fabric : "+visualAssort.getProductFabricDesc());
        txtFit.setText("Fit : "+visualAssort.getProductFitDesc());
        txtCollection.setText("Collection : "+visualAssort.getCollectionName());
        txtAmount.setText("\u20B9 "+visualAssort.getUnitGrossPrice());
        txtSizeRatio.setText("Size Ratio : " +visualAssort.getSize());


        /*edtTextSets.setOnEditorActionListener(new TextView.OnEditorActionListener() {

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

                RelativeLayout rel = (RelativeLayout) v;
                ImageButton btn = (ImageButton) rel.getChildAt(0);
                btn.setBackgroundResource(R.mipmap.like_selected);

                cardStack.swipeTopCardRight(180);

            }
        });

        btndislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RelativeLayout rel = (RelativeLayout) v;
                ImageButton btn = (ImageButton) rel.getChildAt(0);
                btn.setBackgroundResource(R.mipmap.dislike_selected);


                cardStack.swipeTopCardLeft(180);

            }
        });

        btnbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.setBackgroundResource(R.drawable.button_red_effect);

//                LinearLayout footelin = (LinearLayout) btnbuy.getParent();
//                RelativeLayout footer = (RelativeLayout) footelin.getParent();
//                FrameLayout frameLayout = (FrameLayout) footer.getParent();
//                Log.e("-- "," "+frameLayout.getChildAt(2)+" "+frameLayout.getChildAt(3)+" "+layoutComment.getVisibility());

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


                layoutBuy.setVisibility(View.GONE);
                edtTextSets.setText("0");
                btnbuy.setEnabled(false);
                InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if(inputManager != null){
                    inputManager.hideSoftInputFromWindow(edtTextSets.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }

        });


        btncomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.setBackgroundResource(R.drawable.button_red_effect);

                edtTextComment.setText("");
                Log.e("click of comment button","");

                if(layoutBuy.getVisibility() == View.VISIBLE)
                {
                    layoutBuy.setVisibility(View.GONE);
                }

                if(layoutComment.getVisibility() == View.GONE)
                {
                    layoutComment.setVisibility(View.VISIBLE);
                }
                else if(layoutComment.getVisibility() == View.VISIBLE)
                {
                    layoutComment.setVisibility(View.GONE);
                }
            }
        });

        btnCommentDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("click of comment done button","");

                layoutComment.setVisibility(View.GONE);
                edtTextComment.setText("");
                btncomment.setEnabled(false);
                InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if(inputManager != null){
                    inputManager.hideSoftInputFromWindow(edtTextComment.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }



        });*/




        return layoutView;

    }
}