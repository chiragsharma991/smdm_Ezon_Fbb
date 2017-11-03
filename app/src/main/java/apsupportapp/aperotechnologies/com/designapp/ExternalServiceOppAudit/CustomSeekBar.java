package apsupportapp.aperotechnologies.com.designapp.ExternalServiceOppAudit;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import apsupportapp.aperotechnologies.com.designapp.R;

import static apsupportapp.aperotechnologies.com.designapp.Constants.overall_progress;

public class CustomSeekBar {

    int maxCount, textColor;
    Context mContext;
    LinearLayout mSeekLin, mSeekLin1;
    SeekBar mSeekBar;

    public CustomSeekBar(Context context, int maxCount, int textColor) {
        this.mContext = context;
        this.maxCount = maxCount;
        this.textColor = textColor;
    }

    public void addSeekBar(LinearLayout parent) {

        if (parent instanceof LinearLayout) {

            parent.setOrientation(LinearLayout.VERTICAL);

            // Add LinearLayout for labels above SeekBar
            mSeekLin1 = new LinearLayout(mContext);
            mSeekLin1.setOrientation(LinearLayout.HORIZONTAL);
//            mSeekLin.setPadding(10, 0, 10, 0);
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            params1.setMargins(16, 0, 16, 10);
            mSeekLin1.setLayoutParams(params1);
            addLabelsAboveSeekBar(0);


            mSeekBar = new SeekBar(mContext);
            mSeekBar.setMax(maxCount - 1);
           // mSeekBar.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.seekbar));

            mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    mSeekLin1.removeAllViews();
                    addLabelsAboveSeekBar(seekBar.getProgress());
                    overall_progress = seekBar.getProgress();
                    Log.e("overall_progress "," "+ overall_progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });


            // Add LinearLayout for labels below SeekBar
            mSeekLin = new LinearLayout(mContext);
            mSeekLin.setOrientation(LinearLayout.HORIZONTAL);
//            mSeekLin.setPadding(10, 0, 10, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(16, 10, 16, 0);
            mSeekLin.setLayoutParams(params);
            addLabelsBelowSeekBar();

            parent.addView(mSeekLin1);
            parent.addView(mSeekBar);
            parent.addView(mSeekLin);

        }
        else
        {

            Log.e("CustomSeekBar", " Parent is not a LinearLayout");

        }

    }

    private void addLabelsBelowSeekBar()
    {
        for (int count = 0; count < maxCount; count++)
        {
            TextView textView = new TextView(mContext);
            textView.setText(String.valueOf(count + 1));
            textView.setTextColor(textColor);
            textView.setGravity(Gravity.LEFT);
            mSeekLin.addView(textView);
            textView.setLayoutParams((count == maxCount - 1) ? getLayoutParams(0.0f) : getLayoutParams(1.0f));
        }
    }

    private void addLabelsAboveSeekBar(int progress)
    {
        for (int count = 0; count < maxCount; count++)
        {
            TextView textView = new TextView(mContext);
            textView.setText(String.valueOf(count + 1));
            textView.setTextColor(textColor);
            textView.setGravity(Gravity.LEFT);
            textView.setVisibility(View.INVISIBLE);
            mSeekLin1.addView(textView);
            textView.setLayoutParams((count == maxCount - 1) ? getLayoutParams(0.0f) : getLayoutParams(1.0f));
        }

        TextView txt = (TextView) mSeekLin1.getChildAt(progress);
        txt.setVisibility(View.VISIBLE);


    }

    LinearLayout.LayoutParams getLayoutParams(float weight) {
        return new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, weight);
    }

}