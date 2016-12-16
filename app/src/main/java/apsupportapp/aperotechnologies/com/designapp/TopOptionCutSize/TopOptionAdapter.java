package apsupportapp.aperotechnologies.com.designapp.TopOptionCutSize;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;


/**
 * Created by csuthar on 18/11/16.
 */


public class TopOptionAdapter extends BaseAdapter {

    private ArrayList<RunningPromoListDisplay> arrayList;

    //private List mStringFilterList;

    private LayoutInflater mInflater;
    Context context;
    private int Position;

    //private ValueFilter valueFilter;

    public TopOptionAdapter(ArrayList<RunningPromoListDisplay> arrayList, Context context) {

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
            convertView = mInflater.inflate(R.layout.activity_topfullcut_child, null);
            holder.Top_SOHU = (TextView) convertView.findViewById(R.id.top_SOHU);
            holder.Top_bstStockU = (TextView) convertView.findViewById(R.id.top_bstStockU);
            holder.ProgressPicaso = (ProgressBar) convertView.findViewById(R.id.progressPicaso);
            holder.ProgressPicaso.setVisibility(View.VISIBLE);
            holder.Top_option = (TextView) convertView.findViewById(R.id.bst_option);
            holder.Top_RosU = (TextView) convertView.findViewById(R.id.top_RosU);
            holder.Top_image_child = (ImageView) convertView.findViewById(R.id.top_image_child);
            holder.Top_fav = (RelativeLayout) convertView.findViewById(R.id.top_fav);



            convertView.setTag(holder);

        } else {
            holder=(Holder)convertView.getTag();
            holder.ProgressPicaso.setVisibility(View.VISIBLE);


        }
        holder.Top_option.setText(arrayList.get(position).getOption());
        holder.Top_SOHU.setText(""+(int)arrayList.get(position).getStkOnhandQty());
        holder.Top_bstStockU.setText(""+(int)arrayList.get(position).getTargetStock());
        holder.Top_RosU.setText(""+(int)arrayList.get(position).getRos());

        if(!arrayList.get(position).getProdImageURL().equals("")) {
            Picasso.with(this.context).

                    load(arrayList.get(position).getProdImageURL()).
                    into(holder.Top_image_child, new Callback() {
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
                    into(holder.Top_image_child);

        }


        // ---------------------click listener -------------------------

     




        return convertView;
    }



    private class Holder {

        TextView Top_SOHU,Top_bstStockU,Top_RosU,Top_option;
        ImageView Top_image_child;
        RelativeLayout Top_fav;


        public ProgressBar ProgressPicaso;
    }




}
