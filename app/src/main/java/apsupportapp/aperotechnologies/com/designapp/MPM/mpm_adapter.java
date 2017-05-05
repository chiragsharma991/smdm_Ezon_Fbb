package apsupportapp.aperotechnologies.com.designapp.MPM;

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

import apsupportapp.aperotechnologies.com.designapp.Feedback.FeedbackDetailsAdapter;
import apsupportapp.aperotechnologies.com.designapp.R;







public class mpm_adapter extends BaseAdapter {


    private final ArrayList<mpm_model> list;
    private final Context context;
    private final LayoutInflater inflator;
    private Holder holder;


    public mpm_adapter(Context context, ArrayList<mpm_model> list ) {
        this.context=context;
        this.list=list;
        inflator=LayoutInflater.from(this.context);

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
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
            convertView= inflator.inflate(R.layout.mpm_details_child,null);
            holder.department=(TextView) convertView.findViewById(R.id.mpm_department);
            convertView.setTag(holder);
        }else
        {
            holder=(Holder)convertView.getTag();
        }
        holder.department.setText(list.get(position).getProductName());
        convertView.setBackgroundColor((position == mpm_activity.clickPosition) ?
                Color.parseColor("#e8e8e8") : Color.parseColor("#f8f6f6"));






        return convertView;
    }









    public class Holder
    {
        TextView department;


    }






}


