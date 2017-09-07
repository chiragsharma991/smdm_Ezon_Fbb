package apsupportapp.aperotechnologies.com.designapp.BORIS;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import apsupportapp.aperotechnologies.com.designapp.R;

public class MobileScreenActivity extends AppCompatActivity {

    private Context context;
    private EditText edt_mobno;
    private Button btn_search;
    private RelativeLayout imageBtnBack1;
    private TextView txt_incorrect_phone;
    String mob_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_screen);
        getSupportActionBar().hide();
        getSupportActionBar().setElevation(0);
        context = this;

        initialiseUi();
    }

    private void initialiseUi() {

        edt_mobno = (EditText) findViewById(R.id.edt_mobno);
        btn_search = (Button) findViewById(R.id.btn_search);
        imageBtnBack1 = (RelativeLayout) findViewById(R.id.imageBtnBack1);
        txt_incorrect_phone = (TextView) findViewById(R.id.txt_incorrect_phone);

        imageBtnBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edt_mobno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edt_mobno.getText().length() == 10)
                {
                    txt_incorrect_phone.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mob_no = edt_mobno.getText().toString().trim();
                if(mob_no.equals("") || mob_no == null)
                {
                    txt_incorrect_phone.setVisibility(View.VISIBLE);
                    txt_incorrect_phone.setText(context.getResources().getString(R.string.customer_feedback_number));
                }
                else if(mob_no.length() != 10)
                {
                    txt_incorrect_phone.setVisibility(View.VISIBLE);
                    txt_incorrect_phone.setText(getResources().getString(R.string.customer_feedback_digit));
                }
                else
                {
                    txt_incorrect_phone.setVisibility(View.GONE);
                    Intent int_catalogue = new Intent(MobileScreenActivity.this, ReturnCatalogueActivity.class);
                    int_catalogue.putExtra("from","mobile_screen");
                    int_catalogue.putExtra("status"," ");
                    startActivity(int_catalogue);
                }

            }
        });
    }
}
