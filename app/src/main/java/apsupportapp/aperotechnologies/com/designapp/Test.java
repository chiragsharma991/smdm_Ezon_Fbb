package apsupportapp.aperotechnologies.com.designapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import apsupportapp.aperotechnologies.com.designapp.Utils.RoundedTopImageView;

/**
 * Created by csuthar on 10/11/17.
 */

public class Test extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        RoundedTopImageView image = (RoundedTopImageView) findViewById(R.id.di_iv_image);
        Picasso.with(this)
                .load(R.mipmap.koryo_speaker_21_mb_app)
                .into(image);
    }
}
