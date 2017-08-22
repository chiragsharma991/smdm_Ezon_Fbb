package apsupportapp.aperotechnologies.com.designapp.BORIS;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import apsupportapp.aperotechnologies.com.designapp.R;

public class ReturnDetailActivity extends AppCompatActivity {

    private Context context;
    private String store;
    private SharedPreferences sharedPreferences;
    private TextView storedescription;
    private ImageView img_product, img_promo_product;
    private Button btn_accept, btn_reject;
    String radio_value;
    private RelativeLayout imageBtnBack1, img_free_product_click, rel_promo_product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_detail);
        getSupportActionBar().hide();
        getSupportActionBar().setElevation(0);
        context = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        initialiseUI();
    }

    private void initialiseUI() {

        storedescription = (TextView) findViewById(R.id.txtStoreCode);
        rel_promo_product = (RelativeLayout) findViewById(R.id.rel_promo_product);
        img_product = (ImageView) findViewById(R.id.img_product);
        img_free_product_click = (RelativeLayout) findViewById(R.id.img_free_product_click);
        img_promo_product = (ImageView) findViewById(R.id.img_promo_product);
        btn_accept = (Button) findViewById(R.id.btn_accept);
        btn_reject = (Button) findViewById(R.id.btn_reject);

        store = sharedPreferences.getString("storeDescription", "");
        storedescription.setText(store);

        imageBtnBack1 = (RelativeLayout) findViewById(R.id.imageBtnBack1);

        Glide.with(context).load(R.mipmap.noimageavailable).placeholder(R.mipmap.noimageavailable).centerCrop().into(img_product);
        Glide.with(context).load(R.mipmap.noimageavailable).placeholder(R.mipmap.noimageavailable).centerCrop().into(img_promo_product);


        imageBtnBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        img_free_product_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rel_promo_product.getVisibility()==View.VISIBLE)
                {
                    rel_promo_product.setVisibility(View.GONE);
                }
                else
                {
                    rel_promo_product.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                // ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater =  getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_accept_return, null);
                dialogBuilder.setView(dialogView);

                final Button btn_ok = (Button) dialogView.findViewById(R.id.btn_ok);

                final AlertDialog alertDialog = dialogBuilder.create();


                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();
                    }
                });
                alertDialog.setCancelable(true);
                alertDialog.show();
            }
        });

        btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                // ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater =  getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_reject_return, null);
                dialogBuilder.setView(dialogView);

                final Button btn_ok = (Button) dialogView.findViewById(R.id.btn_ok);
                final RadioButton radioSKU = (RadioButton) dialogView.findViewById(R.id.radioSKU);
                final RadioButton radioDefective = (RadioButton) dialogView.findViewById(R.id.radioDefective);
                final RadioButton radioTags = (RadioButton) dialogView.findViewById(R.id.radioTags);

                final AlertDialog alertDialog = dialogBuilder.create();

                radioSKU.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            radioSKU.setChecked(true);
                            radioDefective.setChecked(false);
                            radioTags.setChecked(false);
                        }
                        else
                        {
                            radioSKU.setChecked(false);
                        }
                    }
                });

                radioTags.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            radioTags.setChecked(true);
                            radioSKU.setChecked(false);
                            radioDefective.setChecked(false);
                        }
                        else
                        {
                            radioTags.setChecked(false);
                        }
                    }
                });

                radioDefective.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            radioDefective.setChecked(true);
                            radioSKU.setChecked(false);
                            radioTags.setChecked(false);
                        }
                        else
                        {
                            radioDefective.setChecked(false);
                        }
                    }
                });

                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(radioSKU.isChecked())
                        {

                            radio_value = "Incorrect SKU";
                        }
                        else if(radioDefective.isChecked())
                        {
                            radio_value = "Defective Return";

                        }
                        else if(radioTags.isChecked())
                        {
                            radio_value = "Tags Missing";
                        }
                        Log.e("radio_value "," "+radio_value);
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setCancelable(true);
                alertDialog.show();
            }
        });

    }
}
