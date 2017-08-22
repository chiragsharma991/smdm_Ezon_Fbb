package apsupportapp.aperotechnologies.com.designapp.BORIS;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import apsupportapp.aperotechnologies.com.designapp.R;

public class MobileScreenActivity extends AppCompatActivity {

    private Context context;
    private EditText edt_mobno;
    private Button btn_search;
    private RelativeLayout imageBtnBack1;
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

        imageBtnBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mob_no = edt_mobno.getText().toString().trim();
                if(mob_no.equals("") || mob_no == null)
                {
                    Toast.makeText(context, "Please enter mobile no.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent int_catalogue = new Intent(MobileScreenActivity.this, ReturnCatalogueActivity.class);
                    startActivity(int_catalogue);
                }

            }
        });
    }
}
