package apsupportapp.aperotechnologies.com.designapp.StoreInspection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView;

import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by pamrutkar on 11/04/17.
 */
public class InspectionBeginActivity extends AppCompatActivity implements View.OnClickListener
{

    // Emoji Declaration
    ImageView image_improvement_criteria_1,image_okay_criteria_1,image_good_criteria_1,image_excellent_criteria_1;
    ImageView image_improvement_criteria_2,image_okay_criteria_2,image_good_criteria_2,image_excellent_criteria_2;
    ImageView image_improvement_criteria_3,image_okay_criteria_3,image_good_criteria_3,image_excellent_criteria_3;
    ImageView image_improvement_criteria_4,image_okay_criteria_4,image_good_criteria_4,image_excellent_criteria_4;
    ImageView image_improvement_criteria_5,image_okay_criteria_5,image_good_criteria_5,image_excellent_criteria_5;
    ImageView image_improvement_criteria_6,image_okay_criteria_6,image_good_criteria_6,image_excellent_criteria_6;
    ImageView image_improvement_criteria_7,image_okay_criteria_7,image_good_criteria_7,image_excellent_criteria_7;
    ImageView image_improvement_criteria_8,image_okay_criteria_8,image_good_criteria_8,image_excellent_criteria_8;

    // Emoji Text Declaration
    TextView txt_improvement_criteria_1,txt_okay_criteria_1,txt_good_criteria_1,txt_excellent_criteria_1;
    TextView txt_improvement_criteria_2,txt_okay_criteria_2,txt_good_criteria_2,txt_excellent_criteria_2;
    TextView txt_improvement_criteria_3,txt_okay_criteria_3,txt_good_criteria_3,txt_excellent_criteria_3;
    TextView txt_improvement_criteria_4,txt_okay_criteria_4,txt_good_criteria_4,txt_excellent_criteria_4;
    TextView txt_improvement_criteria_5,txt_okay_criteria_5,txt_good_criteria_5,txt_excellent_criteria_5;
    TextView txt_improvement_criteria_6,txt_okay_criteria_6,txt_good_criteria_6,txt_excellent_criteria_6;
    TextView txt_improvement_criteria_7,txt_okay_criteria_7,txt_good_criteria_7,txt_excellent_criteria_7;
    TextView txt_improvement_criteria_8,txt_okay_criteria_8,txt_good_criteria_8,txt_excellent_criteria_8;

    EditText et_inspected_by,et_comment;
    Button btn_insp_submit;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_begin);
        getSupportActionBar().hide();
        initialise();
    }

    private void initialise() 
    {
        //Improvement emoji image
        image_improvement_criteria_1 = (ImageView)findViewById(R.id.image_improvement_criteria_1);
        image_improvement_criteria_2 = (ImageView)findViewById(R.id.image_improvement_criteria_2);
        image_improvement_criteria_3 = (ImageView)findViewById(R.id.image_improvement_criteria_3);
        image_improvement_criteria_4 = (ImageView)findViewById(R.id.image_improvement_criteria_4);
        image_improvement_criteria_5 = (ImageView)findViewById(R.id.image_improvement_criteria_5);
        image_improvement_criteria_6 = (ImageView)findViewById(R.id.image_improvement_criteria_6);
        image_improvement_criteria_7 = (ImageView)findViewById(R.id.image_improvement_criteria_7);
        image_improvement_criteria_8 = (ImageView)findViewById(R.id.image_improvement_criteria_8);

        //Okay emoji image
        image_okay_criteria_1 = (ImageView)findViewById(R.id.image_okay_criteria_1);
        image_okay_criteria_2 = (ImageView)findViewById(R.id.image_okay_criteria_2);
        image_okay_criteria_3 = (ImageView)findViewById(R.id.image_okay_criteria_3);
        image_okay_criteria_4 = (ImageView)findViewById(R.id.image_okay_criteria_4);
        image_okay_criteria_5 = (ImageView)findViewById(R.id.image_okay_criteria_5);
        image_okay_criteria_6 = (ImageView)findViewById(R.id.image_okay_criteria_6);
        image_okay_criteria_7 = (ImageView)findViewById(R.id.image_okay_criteria_7);
        image_okay_criteria_8 = (ImageView)findViewById(R.id.image_okay_criteria_8);

        //Good Emoji image
        image_good_criteria_1 = (ImageView)findViewById(R.id.image_good_criteria_1);
        image_good_criteria_2 = (ImageView)findViewById(R.id.image_good_criteria_2);
        image_good_criteria_3 = (ImageView)findViewById(R.id.image_good_criteria_3);
        image_good_criteria_4 = (ImageView)findViewById(R.id.image_good_criteria_4);
        image_good_criteria_5 = (ImageView)findViewById(R.id.image_good_criteria_5);
        image_good_criteria_6 = (ImageView)findViewById(R.id.image_good_criteria_6);
        image_good_criteria_7 = (ImageView)findViewById(R.id.image_good_criteria_7);
        image_good_criteria_8 = (ImageView)findViewById(R.id.image_good_criteria_8);

        //excellent emoji image
        image_excellent_criteria_1 = (ImageView)findViewById(R.id.image_excellent_criteria_1);
        image_excellent_criteria_2 = (ImageView)findViewById(R.id.image_excellent_criteria_2);
        image_excellent_criteria_3 = (ImageView)findViewById(R.id.image_excellent_criteria_3);
        image_excellent_criteria_4 = (ImageView)findViewById(R.id.image_excellent_criteria_4);
        image_excellent_criteria_5 = (ImageView)findViewById(R.id.image_excellent_criteria_5);
        image_excellent_criteria_6 = (ImageView)findViewById(R.id.image_excellent_criteria_6);
        image_excellent_criteria_7 = (ImageView)findViewById(R.id.image_excellent_criteria_7);
        image_excellent_criteria_8 = (ImageView)findViewById(R.id.image_excellent_criteria_8);

        //Improvement emoji text
        txt_improvement_criteria_1 = (TextView)findViewById(R.id.txt_improvement_criteria_1);
        txt_improvement_criteria_2 = (TextView)findViewById(R.id.txt_improvement_criteria_2);
        txt_improvement_criteria_3 = (TextView)findViewById(R.id.txt_improvement_criteria_3);
        txt_improvement_criteria_4 = (TextView)findViewById(R.id.txt_improvement_criteria_4);
        txt_improvement_criteria_5 = (TextView)findViewById(R.id.txt_improvement_criteria_5);
        txt_improvement_criteria_6 = (TextView)findViewById(R.id.txt_improvement_criteria_6);
        txt_improvement_criteria_7 = (TextView)findViewById(R.id.txt_improvement_criteria_7);
        txt_improvement_criteria_8 = (TextView)findViewById(R.id.txt_improvement_criteria_8);

        //Okay emoji text
        txt_okay_criteria_1 = (TextView)findViewById(R.id.txt_okay_criteria_1);
        txt_okay_criteria_2 = (TextView)findViewById(R.id.txt_okay_criteria_2);
        txt_okay_criteria_3 = (TextView)findViewById(R.id.txt_okay_criteria_3);
        txt_okay_criteria_4 = (TextView)findViewById(R.id.txt_okay_criteria_4);
        txt_okay_criteria_5 = (TextView)findViewById(R.id.txt_okay_criteria_5);
        txt_okay_criteria_6 = (TextView)findViewById(R.id.txt_okay_criteria_6);
        txt_okay_criteria_7 = (TextView)findViewById(R.id.txt_okay_criteria_7);
        txt_okay_criteria_8 = (TextView)findViewById(R.id.txt_okay_criteria_8);

        //Good Emoji text
        txt_good_criteria_1 = (TextView)findViewById(R.id.txt_good_criteria_1);
        txt_good_criteria_2 = (TextView)findViewById(R.id.txt_good_criteria_2);
        txt_good_criteria_3 = (TextView)findViewById(R.id.txt_good_criteria_3);
        txt_good_criteria_4 = (TextView)findViewById(R.id.txt_good_criteria_4);
        txt_good_criteria_5 = (TextView)findViewById(R.id.txt_good_criteria_5);
        txt_good_criteria_6 = (TextView)findViewById(R.id.txt_good_criteria_6);
        txt_good_criteria_7 = (TextView)findViewById(R.id.txt_good_criteria_7);
        txt_good_criteria_8 = (TextView)findViewById(R.id.txt_good_criteria_8);

        //excellent emoji text
        txt_excellent_criteria_1 = (TextView)findViewById(R.id.txt_excellent_criteria_1);
        txt_excellent_criteria_2 = (TextView)findViewById(R.id.txt_excellent_criteria_2);
        txt_excellent_criteria_3 = (TextView)findViewById(R.id.txt_excellent_criteria_3);
        txt_excellent_criteria_4 = (TextView)findViewById(R.id.txt_excellent_criteria_4);
        txt_excellent_criteria_5 = (TextView)findViewById(R.id.txt_excellent_criteria_5);
        txt_excellent_criteria_6 = (TextView)findViewById(R.id.txt_excellent_criteria_6);
        txt_excellent_criteria_7 = (TextView)findViewById(R.id.txt_excellent_criteria_7);
        txt_excellent_criteria_8 = (TextView)findViewById(R.id.txt_excellent_criteria_8);

        //Edittext
        et_inspected_by = (EditText)findViewById(R.id.et_inspected_by);
        et_comment = (EditText)findViewById(R.id.et_comment);

        btn_insp_submit = (Button)findViewById(R.id.btn_insp_submit);

        image_improvement_criteria_1.setOnClickListener(this);
        image_improvement_criteria_2.setOnClickListener(this);
        image_improvement_criteria_3.setOnClickListener(this);
        image_improvement_criteria_4.setOnClickListener(this);
        image_improvement_criteria_5.setOnClickListener(this);
        image_improvement_criteria_6.setOnClickListener(this);
        image_improvement_criteria_7.setOnClickListener(this);
        image_improvement_criteria_8.setOnClickListener(this);
        image_okay_criteria_1.setOnClickListener(this);
        image_okay_criteria_2.setOnClickListener(this);
        image_okay_criteria_3.setOnClickListener(this);
        image_okay_criteria_4.setOnClickListener(this);
        image_okay_criteria_5.setOnClickListener(this);
        image_okay_criteria_6.setOnClickListener(this);
        image_okay_criteria_7.setOnClickListener(this);
        image_okay_criteria_8.setOnClickListener(this);
        image_good_criteria_1.setOnClickListener(this);
        image_good_criteria_2.setOnClickListener(this);
        image_good_criteria_3.setOnClickListener(this);
        image_good_criteria_4.setOnClickListener(this);
        image_good_criteria_5.setOnClickListener(this);
        image_good_criteria_6.setOnClickListener(this);
        image_good_criteria_7.setOnClickListener(this);
        image_good_criteria_8.setOnClickListener(this);
        image_excellent_criteria_1.setOnClickListener(this);
        image_excellent_criteria_2.setOnClickListener(this);
        image_excellent_criteria_3.setOnClickListener(this);
        image_excellent_criteria_4.setOnClickListener(this);
        image_excellent_criteria_5.setOnClickListener(this);
        image_excellent_criteria_6.setOnClickListener(this);
        image_excellent_criteria_7.setOnClickListener(this);
        image_excellent_criteria_8.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.image_improvement_criteria_1:
                image_improvement_criteria_1.setBackgroundResource(R.mipmap.improvementemojiselected);
                txt_improvement_criteria_1.setText("Need Improvement");
                break;
            case R.id.image_improvement_criteria_2:
                image_improvement_criteria_2.setBackgroundResource(R.mipmap.improvementemojiselected);
                txt_improvement_criteria_2.setText("Need Improvement");
                break;
            case R.id.image_improvement_criteria_3:
                image_improvement_criteria_3.setBackgroundResource(R.mipmap.improvementemojiselected);
                txt_improvement_criteria_3.setText("Need Improvement");
                break;
            case R.id.image_improvement_criteria_4:
                image_improvement_criteria_4.setBackgroundResource(R.mipmap.improvementemojiselected);
                txt_improvement_criteria_4.setText("Need Improvement");
                break;
            case R.id.image_improvement_criteria_5:
                image_improvement_criteria_5.setBackgroundResource(R.mipmap.improvementemojiselected);
                txt_improvement_criteria_5.setText("Need Improvement");
                break;
            case R.id.image_improvement_criteria_6:
                image_improvement_criteria_1.setBackgroundResource(R.mipmap.improvementemojiselected);
                txt_improvement_criteria_1.setText("Need Improvement");
                break;
            case R.id.image_improvement_criteria_7:
                image_improvement_criteria_7.setBackgroundResource(R.mipmap.improvementemojiselected);
                txt_improvement_criteria_7.setText("Need Improvement");
                break;
            case R.id.image_improvement_criteria_8:
                image_improvement_criteria_8.setBackgroundResource(R.mipmap.improvementemojiselected);
                txt_improvement_criteria_8.setText("Need Improvement");
                break;

            case R.id.image_okay_criteria_1:
                image_okay_criteria_1.setBackgroundResource(R.mipmap.okayemojiselected);
                txt_okay_criteria_1.setText("Okay");
                break;
            case R.id.image_okay_criteria_2:
                image_okay_criteria_2.setBackgroundResource(R.mipmap.okayemojiselected);
                txt_okay_criteria_2.setText("Okay");
                break;
            case R.id.image_okay_criteria_3:
                image_okay_criteria_3.setBackgroundResource(R.mipmap.okayemojiselected);
                txt_okay_criteria_3.setText("Okay");
                break;
            case R.id.image_okay_criteria_4:
                image_okay_criteria_4.setBackgroundResource(R.mipmap.okayemojiselected);
                txt_okay_criteria_4.setText("Okay");
                break;
            case R.id.image_okay_criteria_5:
                image_okay_criteria_5.setBackgroundResource(R.mipmap.okayemojiselected);
                txt_okay_criteria_5.setText("Okay");
                break;
            case R.id.image_okay_criteria_6:
                image_okay_criteria_6.setBackgroundResource(R.mipmap.okayemojiselected);
                txt_okay_criteria_6.setText("Okay");
                break;
            case R.id.image_okay_criteria_7:
                image_okay_criteria_7.setBackgroundResource(R.mipmap.okayemojiselected);
                txt_okay_criteria_7.setText("Okay");
                break;
            case R.id.image_okay_criteria_8:
                image_okay_criteria_8.setBackgroundResource(R.mipmap.okayemojiselected);
                txt_okay_criteria_8.setText("Okay");
                break;

            case R.id.image_good_criteria_1:
                image_good_criteria_1.setBackgroundResource(R.mipmap.goodemojiselected);
                txt_good_criteria_1.setText("Good");
                break;
            case R.id.image_good_criteria_2:
                image_good_criteria_2.setBackgroundResource(R.mipmap.goodemojiselected);
                txt_good_criteria_2.setText("Good");
                break;
            case R.id.image_good_criteria_3:
                image_good_criteria_3.setBackgroundResource(R.mipmap.goodemojiselected);
                txt_good_criteria_3.setText("Good");
                break;
            case R.id.image_good_criteria_4:
                image_good_criteria_4.setBackgroundResource(R.mipmap.goodemojiselected);
                txt_good_criteria_4.setText("Good");
                break;
            case R.id.image_good_criteria_5:
                image_good_criteria_5.setBackgroundResource(R.mipmap.goodemojiselected);
                txt_good_criteria_5.setText("Good");
                break;
            case R.id.image_good_criteria_6:
                image_good_criteria_6.setBackgroundResource(R.mipmap.goodemojiselected);
                txt_good_criteria_6.setText("Good");
                break;
            case R.id.image_good_criteria_7:
                image_good_criteria_7.setBackgroundResource(R.mipmap.goodemojiselected);
                txt_good_criteria_7.setText("Good");
                break;
            case R.id.image_good_criteria_8:
                image_good_criteria_8.setBackgroundResource(R.mipmap.goodemojiselected);
                txt_good_criteria_8.setText("Good");
                break;

            case R.id.image_excellent_criteria_1:
                image_excellent_criteria_1.setBackgroundResource(R.mipmap.excellentemojiselected);
                txt_excellent_criteria_1.setText("Excellent");
                break;
            case R.id.image_excellent_criteria_2:
                image_excellent_criteria_2.setBackgroundResource(R.mipmap.excellentemojiselected);
                txt_excellent_criteria_2.setText("Excellent");
                break;
            case R.id.image_excellent_criteria_3:
                image_excellent_criteria_3.setBackgroundResource(R.mipmap.excellentemojiselected);
                txt_excellent_criteria_3.setText("Excellent");
                break;
            case R.id.image_excellent_criteria_4:
                image_excellent_criteria_4.setBackgroundResource(R.mipmap.excellentemojiselected);
                txt_excellent_criteria_4.setText("Excellent");
                break;
            case R.id.image_excellent_criteria_5:
                image_excellent_criteria_5.setBackgroundResource(R.mipmap.excellentemojiselected);
                txt_excellent_criteria_5.setText("Excellent");
                break;
            case R.id.image_excellent_criteria_6:
                image_excellent_criteria_6.setBackgroundResource(R.mipmap.excellentemojiselected);
                txt_excellent_criteria_6.setText("Excellent");
                break;
            case R.id.image_excellent_criteria_7:
                image_excellent_criteria_7.setBackgroundResource(R.mipmap.excellentemojiselected);
                txt_excellent_criteria_7.setText("Excellent");
                break;
            case R.id.image_excellent_criteria_8:
                image_excellent_criteria_8.setBackgroundResource(R.mipmap.excellentemojiselected);
                txt_excellent_criteria_8.setText("Excellent");
                break;
          }
    }
}
