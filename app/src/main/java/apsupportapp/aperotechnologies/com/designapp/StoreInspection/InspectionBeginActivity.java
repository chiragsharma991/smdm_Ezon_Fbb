package apsupportapp.aperotechnologies.com.designapp.StoreInspection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by pamrutkar on 11/04/17.
 */
public class InspectionBeginActivity extends AppCompatActivity
{

    ImageView image_improvement_criteria_1,image_okay_criteria_1,image_good_criteria_1,image_excellent_criteria_1;
    ImageView image_improvement_criteria_2,image_okay_criteria_2,image_good_criteria_2,image_excellent_criteria_2;
    ImageView image_improvement_criteria_3,image_okay_criteria_3,image_good_criteria_3,image_excellent_criteria_3;
    ImageView image_improvement_criteria_4,image_okay_criteria_4,image_good_criteria_4,image_excellent_criteria_4;
    ImageView image_improvement_criteria_5,image_okay_criteria_5,image_good_criteria_5,image_excellent_criteria_5;
    ImageView image_improvement_criteria_6,image_okay_criteria_6,image_good_criteria_6,image_excellent_criteria_6;
    ImageView image_improvement_criteria_7,image_okay_criteria_7,image_good_criteria_7,image_excellent_criteria_7;
    ImageView image_improvement_criteria_8,image_okay_criteria_8,image_good_criteria_8,image_excellent_criteria_8;






    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_begin);
        getSupportActionBar().hide();
    }
}
