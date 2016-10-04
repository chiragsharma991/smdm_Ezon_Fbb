package apsupportapp.aperotechnologies.com.designapp.VisualAssortment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import apsupportapp.aperotechnologies.com.designapp.R;


/**
 * Created by hasai on 22/09/16.
 */
public class VisualAssortmentActivityWork extends Activity {


    RelativeLayout btnlike, btndislike, btnbuy, btncomment;
    LinearLayout layoutBuy, layoutComment;
    Button btnBuyDone, btnCommentDone;
    EditText edtTextSets, edtTextComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualassortment);


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
                        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if(inputManager != null){
                            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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


        btnbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                edtTextSets.setText(0);
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if(inputManager != null){
                    inputManager.hideSoftInputFromWindow(edtTextSets.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }

        });


        btncomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                layoutComment.setVisibility(View.GONE);
                edtTextComment.setText("");
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if(inputManager != null){
                    inputManager.hideSoftInputFromWindow(edtTextComment.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }



        });



    }
}
