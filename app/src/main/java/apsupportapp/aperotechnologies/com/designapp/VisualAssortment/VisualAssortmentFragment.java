package apsupportapp.aperotechnologies.com.designapp.VisualAssortment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.model.VisualAssort;


/**
 * Created by mpatil on 19/07/16.
 */
public class VisualAssortmentFragment extends Fragment
{
    //Overriden method onCreateView

    View layoutView;
    int position;

    RelativeLayout btnlike, btndislike, btnbuy, btncomment;
    LinearLayout layoutBuy, layoutComment;
    Button btnBuyDone, btnCommentDone;
    EditText edtTextSets, edtTextComment;

    ArrayList<VisualAssort> visualassortmentlist;
    ImageView img_VisualAssortment;
    TextView txtName, txtFabric, txtFit, txtCollection, txtSizeRatio, txtAmount;
    Context cont;

    public VisualAssortmentFragment()
    {
    }

    @SuppressLint("ValidFragment")
    public VisualAssortmentFragment(Context cont, int position, ArrayList<VisualAssort> visualassortmentlist)
    {

        this.visualassortmentlist = visualassortmentlist;
        this.position = position;
        this.cont = cont;

    }



    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        //Returning the layout file after inflating
        layoutView = inflater.inflate(R.layout.fragment_visualassortment, container, false);


        btnlike = (RelativeLayout) layoutView.findViewById(R.id.imgbtnlike);
        btndislike = (RelativeLayout) layoutView.findViewById(R.id.imgbtndislike);
        btnbuy = (RelativeLayout) layoutView.findViewById(R.id.imgbtnbuy);
        btncomment = (RelativeLayout) layoutView.findViewById(R.id.imgbtncomment);
        layoutBuy = (LinearLayout) layoutView.findViewById(R.id.layoutBuy);
        layoutComment = (LinearLayout) layoutView.findViewById(R.id.layoutComment);
        btnBuyDone = (Button) layoutView.findViewById(R.id.btnBuyDone);
        btnCommentDone = (Button) layoutView.findViewById(R.id.btnCommentDone);
        edtTextSets = (EditText) layoutView.findViewById(R.id.edtTextSets);
        edtTextComment = (EditText) layoutView.findViewById(R.id.edtTextComment);

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
        txtFabric.setText(visualAssort.getProductFabricDesc());
        txtFit.setText(visualAssort.getProductFitDesc());
        txtCollection.setText(visualAssort.getCollectionName());
        txtAmount.setText(visualAssort.getUnitGrossPrice());
        txtSizeRatio.setText(visualAssort.getSize());


        edtTextSets.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            Boolean handled = false;
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_NEXT) || (actionId == EditorInfo.IME_ACTION_NONE)) {
                    edtTextSets.clearFocus();
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
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

                ImageButton btn = (ImageButton) btnlike.getChildAt(0);
                btn.setBackgroundResource(R.mipmap.like_selected);
                btnlike.setEnabled(false);
            }
        });

        btndislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click of dislike button","");
                ImageButton btn = (ImageButton) btndislike.getChildAt(0);
                btn.setBackgroundResource(R.mipmap.dislike_selected);
                btndislike.setEnabled(false);
            }
        });


        btnbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click of buy button","");
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
                btnbuy.setEnabled(false);
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if(inputManager != null){
                    inputManager.hideSoftInputFromWindow(edtTextSets.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }

        });


        btncomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if(inputManager != null){
                    inputManager.hideSoftInputFromWindow(edtTextComment.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }



        });


        return layoutView;
    }


}



