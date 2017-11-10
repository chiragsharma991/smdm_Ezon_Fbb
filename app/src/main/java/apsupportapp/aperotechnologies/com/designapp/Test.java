package apsupportapp.aperotechnologies.com.designapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;

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
       /* Picasso.with(this)
                .load(R.mipmap.koryo_speaker_21_mb_app)
                .into(image);*/

        Animation anim = new ScaleAnimation(
                0f, 1f, // Start and end values for the X axis scaling
                0f, 1f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(2000);
        image.startAnimation(anim);
    }
}
