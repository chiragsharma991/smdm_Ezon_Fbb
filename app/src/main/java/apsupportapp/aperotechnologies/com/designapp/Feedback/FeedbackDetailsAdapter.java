package apsupportapp.aperotechnologies.com.designapp.Feedback;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.FloorAvailability.FloorAvailabilityAdapter;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.RunningPromo.RunningPromoSnapAdapter;

/**
 * Created by csuthar on 10/04/17.
 */

public class FeedbackDetailsAdapter extends BaseAdapter{


    private final Context context;
    private final LayoutInflater inflator;
    private ArrayList<String>optionList;
    private ArrayList<Integer>optionPercentageList;
    FeedbackDetailsAdapter.Holder holder;

    public FeedbackDetailsAdapter(ArrayList<String> optionList, ArrayList<Integer> optionPercentageList, Context context) {
        this.context=context;
        this.optionList=optionList;
        this.optionPercentageList=optionPercentageList;
        inflator= LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int i) {
        return optionList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {


        if(convertView==null)
        {
            holder=new Holder();
            convertView= inflator.inflate(R.layout.feedback_details_child,null);
            holder.Addviewchild=(RelativeLayout)convertView.findViewById(R.id.addViewChild);
            LineBar(position);

            convertView.setTag(holder);
        }else
        {
            holder=(FeedbackDetailsAdapter.Holder)convertView.getTag();
        }



        return convertView;
    }

    private void LineBar(final int position)
    {
        holder.Addviewchild.removeAllViewsInLayout();

        ViewTreeObserver vto =  holder.Addviewchild.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    holder.Addviewchild.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    holder.Addviewchild.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int width  = holder.Addviewchild.getMeasuredWidth();
                int height = holder.Addviewchild.getMeasuredHeight();

                // Calculation width acording to size of phone

                double x= ((double) optionPercentageList.get(position)/100)*width;  //20 is from api
                int percentage=(int)x;


                Log.e("TAG", "view width:................ "+width+"and percentage is "+optionPercentageList.get(position)+"and values are"+percentage+"option list size is"+optionList.size()+"position"+position);

                View lp = new View(context) ;
                // LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(400,LinearLayout.LayoutParams.MATCH_PARENT);
                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(percentage,LinearLayout.LayoutParams.MATCH_PARENT);
                lp.setLayoutParams(layoutParams);


//for setting the background color  // input your color
                lp.setBackgroundColor(Color.parseColor("#e3e2e3"));
                holder.Addviewchild.addView(lp);

                AddText(position,percentage);

            }
        });
    }

    private void AddText(int position, int percentage) {

        // starting title text

        final TextView textView1 = new TextView(context);
        textView1.setText(optionList.get(position));
        textView1.setTextColor(Color.parseColor("#404040"));

        final RelativeLayout.LayoutParams params1 =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);

        params1.addRule(RelativeLayout.CENTER_VERTICAL);
        params1.setMargins(5,0,0,0);
        textView1.setLayoutParams(params1);

        holder.Addviewchild.addView(textView1, params1);


        // another text

        final TextView textView2 = new TextView(context);
        textView2.setText("%"+optionPercentageList.get(position));
        textView2.setTextColor(Color.parseColor("#404040"));

        final RelativeLayout.LayoutParams params2 =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);

        params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params2.addRule(RelativeLayout.CENTER_VERTICAL);
        textView2.setLayoutParams(params2);

        holder.Addviewchild.addView(textView2, params2);
    }


    public class Holder
    {
        RelativeLayout Addviewchild;



    }



}


