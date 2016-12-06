package apsupportapp.aperotechnologies.com.designapp.SkewedSize;

/**
 * Created by csuthar on 29/11/16.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;




/**
 * Created by csuthar on 18/11/16.
 */


public class SkewedSizeAdapter extends BaseAdapter {

    private ArrayList<RunningPromoListDisplay> arrayList;

    //private List mStringFilterList;

    private LayoutInflater mInflater;
    Context context;
    private int Position;

    //private ValueFilter valueFilter;

    public SkewedSizeAdapter(ArrayList<RunningPromoListDisplay> arrayList, Context context) {

        // Log.e("in sales analysis adapter"," ");
        this.arrayList = arrayList;
        this.context = context;
        mInflater = LayoutInflater.from(context);

        //getFilter();
    }

    //How many items are in the data set represented by this Adapter.
    @Override
    public int getCount() {


        return arrayList.size();
    }

    //Get the data item associated with the specified position in the data set.
    @Override
    public Object getItem(int position) {

        return arrayList.get(position);
    }

    //Get the row id associated with the specified position in the list.
    @Override
    public long getItemId(int position) {

        return position;
    }

    //Get a View that displays the data at the specified position in the data set.
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //Log.e("in ","getview");

        Position=position;
        final Holder holder;
        if (convertView == null) {
            holder=new Holder();
            convertView = mInflater.inflate(R.layout.skewedsize_child, null);
            holder.skewed_SOHU = (TextView) convertView.findViewById(R.id.skewed_SOHU);
            holder.skewed_fwc = (TextView) convertView.findViewById(R.id.skewed_fwc);
            holder.Skewed_ProdAttribute = (TextView) convertView.findViewById(R.id.skewed_ProdAttribute);
            holder.Skewed_SOH = (TextView) convertView.findViewById(R.id.skewed_SOH);
            holder.skewed_option = (TextView) convertView.findViewById(R.id.skewed_option);
            holder.skewed_image_child = (ImageView) convertView.findViewById(R.id.skewed_image_child);
            holder.toggle_skewed_fav = (ToggleButton) convertView.findViewById(R.id.toggle_skewed_fav);



            convertView.setTag(holder);

        } else {
            holder=(Holder)convertView.getTag();

        }
        holder.skewed_option.setText(arrayList.get(position).getOption());
        holder.skewed_SOHU.setText(""+(int)arrayList.get(position).getStkOnhandQty());
        holder.skewed_fwc.setText(""+(int)arrayList.get(position).getFwdWeekCover());
        holder.Skewed_ProdAttribute.setText(arrayList.get(position).getProdAttribute4());
        holder.Skewed_SOH.setText(""+(int)arrayList.get(position).getStkOnhandQty());


        // ---------------------click listener -------------------------






        return convertView;
    }



    private class Holder {

        TextView skewed_SOHU,skewed_fwc,skewed_option,Skewed_ProdAttribute,Skewed_SOH;
        ImageView skewed_image_child;
        ToggleButton toggle_skewed_fav;




    }




}
