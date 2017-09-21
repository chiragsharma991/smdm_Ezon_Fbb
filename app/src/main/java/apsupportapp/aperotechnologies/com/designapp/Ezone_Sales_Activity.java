package apsupportapp.aperotechnologies.com.designapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by pamrutkar on 18/05/17.
 */
public class Ezone_Sales_Activity extends AppCompatActivity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ezone_sales);
    }

    public void StartIntent(Context c) {
        c.startActivity(new Intent(c,Ezone_Sales_Activity.class));
    }
}
