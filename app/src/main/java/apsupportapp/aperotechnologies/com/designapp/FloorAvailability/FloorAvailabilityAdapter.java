package apsupportapp.aperotechnologies.com.designapp.FloorAvailability;

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
import apsupportapp.aperotechnologies.com.designapp.StockAgeing.StockAgeingAdapter;
import apsupportapp.aperotechnologies.com.designapp.model.FloorAvailabilityDetails;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;

/**
 * Created by pamrutkar on 07/12/16.
 */
public class FloorAvailabilityAdapter extends BaseAdapter {

    private ArrayList<FloorAvailabilityDetails> arrayList;

    //private List mStringFilterList;

    private LayoutInflater mInflater;
    Context context;
    int Position;
    ProgressBar progressView = null;

    //private ValueFilter valueFiAlter;

    public FloorAvailabilityAdapter(ArrayList<FloorAvailabilityDetails> arrayList, Context context) {

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

        final FloorAvailabilityAdapter.Holder holder;
        if (convertView == null) {
            holder=new FloorAvailabilityAdapter.Holder();
            convertView = mInflater.inflate(R.layout.activity_floor_availability_child, null);
            holder.floor_option = (TextView) convertView.findViewById(R.id.floor_option);
            holder.floor_SOH_U=(TextView)convertView.findViewById(R.id.floor_SOH_U);
            holder.floor_NoofDays = (TextView) convertView.findViewById(R.id.floor_NoofDays);
            holder.floor_ReceiptDate = (TextView) convertView.findViewById(R.id.floor_ReceiptDate);
            holder.ProgressPicaso = (ProgressBar) convertView.findViewById(R.id.imageLoader_floor);
            holder.ProgressPicaso.setVisibility(View.VISIBLE);
            holder.floor_image_child = (ImageView) convertView.findViewById(R.id.floor_image_child);
            holder.floor_fav = (RelativeLayout) convertView.findViewById(R.id.floor_fav);
            convertView.setTag(holder);

        } else {

            holder=(FloorAvailabilityAdapter.Holder)convertView.getTag();
            holder.ProgressPicaso.setVisibility(View.VISIBLE);

        }

        holder.floor_option.setText(arrayList.get(position).getOption());
        holder.floor_SOH_U.setText(""+Math.round(arrayList.get(position).getStkOnhandQty()));
        holder.floor_NoofDays.setText(arrayList.get(position).getNoDaysPassed());
        holder.floor_ReceiptDate.setText(arrayList.get(position).getFirstReceiptDate());
//        StockAgeingActivity.stock_txtStoreCode.setText(arrayList.get(position).getStoreCode());
//        StockAgeingActivity.stock_txtStoreName.setText(arrayList.get(position).getStoreDescription());

        if(!arrayList.get(position).getProdImageURL().equals(""))
        {
            Picasso.with(this.context).

                    load(arrayList.get(position).getProdImageURL()).
                    into(holder.floor_image_child, new Callback() {
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
                    into(holder.floor_image_child);

        }

        // ---------------------click listener -------------------------
        return convertView;
    }



    private class Holder {

        TextView floor_option, floor_NoofDays, floor_ReceiptDate, floor_SOH_U;
        ImageView floor_image_child;
        RelativeLayout floor_fav;
        ProgressBar ProgressPicaso;
    }


    }

