package apsupportapp.aperotechnologies.com.designapp.SkewedSize;

/**
 * Created by csuthar on 29/11/16.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;
import apsupportapp.aperotechnologies.com.designapp.model.SkewedSizeListDisplay;


/**
 * Created by csuthar on 18/11/16.
 */


public class SkewedSizeAdapter extends BaseAdapter {

    private ArrayList<SkewedSizeListDisplay> arrayList;

    //private List mStringFilterList;

    private LayoutInflater mInflater;
    Context context;
    private int Position;
    String TAG="SkewedSizesActivity";

    //private ValueFilter valueFilter;

    public SkewedSizeAdapter(ArrayList<SkewedSizeListDisplay> arrayList, Context context) {

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

        Position = position;
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = mInflater.inflate(R.layout.skewedsize_child, null);
            holder.skewed_SOHU = (TextView) convertView.findViewById(R.id.skewed_SOHU);
            holder.ProgressPicaso = (ProgressBar) convertView.findViewById(R.id.progressPicaso);
            holder.ProgressPicaso.setVisibility(View.VISIBLE);
            holder.skewed_fwc = (TextView) convertView.findViewById(R.id.skewed_fwc);
            holder.Skewed_ProdAttribute = (TextView) convertView.findViewById(R.id.skewed_ProdAttribute);
            holder.Skewed_SOH = (TextView) convertView.findViewById(R.id.skewed_SOH);
            holder.skewed_option = (TextView) convertView.findViewById(R.id.skewed_option);
            holder.skewed_image_child = (ImageView) convertView.findViewById(R.id.skewed_image_child);
            holder.toggle_skewed_fav = (ToggleButton) convertView.findViewById(R.id.toggle_skewed_fav);


            convertView.setTag(holder);

        } else {
            holder = (Holder) convertView.getTag();
            holder.ProgressPicaso.setVisibility(View.VISIBLE);


        }
        holder.skewed_option.setText(arrayList.get(position).getOption());
       // holder.skewed_SOHU.setText(calculation(arrayList.get(position).getStkOnhandQty()));
        holder.skewed_fwc.setText(arrayList.get(position).getFwdWeekCover());
       // holder.Skewed_ProdAttribute.setText(arrayList.get(position).getProdAttribute4());
       // holder.Skewed_SOH.setText((arrayList.get(position).getStkOnhandQty()));
        Log.e(TAG, "getView: "+calculation(arrayList.get(position).getStkOnhandQty()));


        if(!arrayList.get(position).getProdImageURL().equals("")) {
            Picasso.with(this.context).

                    load(arrayList.get(position).getProdImageURL()).
                    into(holder.skewed_image_child, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.ProgressPicaso.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            holder.ProgressPicaso.setVisibility(View.GONE);

                        }
                    });
        }else {
            holder.ProgressPicaso.setVisibility(View.GONE);

            Picasso.with(this.context).
                    load(R.mipmap.placeholder).
                    into(holder.skewed_image_child);

        }


        // ---------------------click listener -------------------------


        return convertView;
    }


    private int calculation(String value) {
        List<String> items = Arrays.asList(value.split("\\s*,\\s*"));
        int stkOnhandQty []=new int[items.size()];
        for (int i = 0; i <items.size(); i++) {
            stkOnhandQty[i]=Integer.parseInt(items.get(i));
        }
        return sumof(stkOnhandQty);


    }

    private int sumof(int[] stkOnhandQty )
    {
        int sum=0;
        for(int y : stkOnhandQty)
        {
            sum += y;
        }
        return sum;
       // Log.e(TAG, "sumof: "+sum );
    }


    private class Holder {

        TextView skewed_SOHU, skewed_fwc, skewed_option, Skewed_ProdAttribute, Skewed_SOH;
        ImageView skewed_image_child;
        ToggleButton toggle_skewed_fav;
        public ProgressBar ProgressPicaso;
    }
}
