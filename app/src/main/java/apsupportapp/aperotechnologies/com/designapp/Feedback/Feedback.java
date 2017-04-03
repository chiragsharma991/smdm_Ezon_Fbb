package apsupportapp.aperotechnologies.com.designapp.Feedback;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import apsupportapp.aperotechnologies.com.designapp.Collaboration.Status.StatusActivity;
import apsupportapp.aperotechnologies.com.designapp.R;

public class Feedback extends AppCompatActivity implements View.OnClickListener{

    private RelativeLayout Feedback_BtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        getSupportActionBar().hide();//
        initalise();
    }

    private void initalise() {
        Feedback_BtnBack=(RelativeLayout)findViewById(R.id.feedback_BtnBack);
        Feedback_BtnBack.setOnClickListener(this);
    }

    public static void StartIntent(Context c) {
        c.startActivity(new Intent(c, Feedback.class));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.feedback_BtnBack:
                finish();
                break;
            default:
                break;


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
