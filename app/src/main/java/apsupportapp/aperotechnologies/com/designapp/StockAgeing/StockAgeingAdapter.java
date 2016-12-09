package apsupportapp.aperotechnologies.com.designapp.StockAgeing;

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
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.TopOptionCutSize.TopOptionAdapter;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;

/**
 * Created by pamrutkar on 05/12/16.
 */
public class StockAgeingAdapter extends BaseAdapter{

    private ArrayList<RunningPromoListDisplay> arrayList;

    //private List mStringFilterList;

    private LayoutInflater mInflater;
    Context context;
    int Position;
    ProgressBar progressView = null;

    //private ValueFilter valueFiAlter;

    public StockAgeingAdapter(ArrayList<RunningPromoListDisplay> arrayList, Context context) {

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

        final StockAgeingAdapter.Holder holder;
        if (convertView == null) {
            holder=new StockAgeingAdapter.Holder();
            convertView = mInflater.inflate(R.layout.activity_stock_ageing_child, null);
            holder.stock_ageing = (TextView) convertView.findViewById(R.id.stock_ageing);
            holder.stock_SOH_U = (TextView) convertView.findViewById(R.id.stock_SOH_U);
            holder.stock_option = (TextView) convertView.findViewById(R.id.stock_option);
//
            holder.stock_image_child = (ImageView) convertView.findViewById(R.id.stock_image_child);
            holder.stock_fav = (RelativeLayout) convertView.findViewById(R.id.stock_fav);
            convertView.setTag(holder);

        } else {

            holder=(StockAgeingAdapter.Holder)convertView.getTag();

        }
        holder.stock_option.setText(arrayList.get(position).getOption());
        holder.stock_ageing.setText(arrayList.get(position).getStockageBandDesc()+" Days" );
        holder.stock_SOH_U.setText(""+(int)arrayList.get(position).getStkOnhandQty());
//        StockAgeingActivity.stock_txtStoreCode.setText(arrayList.get(position).getStoreCode());
//        StockAgeingActivity.stock_txtStoreName.setText(arrayList.get(position).getStoreDescription());

        if(!arrayList.get(position).getProdImageURL().equals("")) {
            Picasso.with(this.context).load(arrayList.get(position).getProdImageURL()).into(holder.stock_image_child);
        }else {
            Picasso.with(this.context).load(R.mipmap.placeholder).into(holder.stock_image_child);
        }

       // ---------------------click listener -------------------------
        return convertView;
    }

    private class Holder {

        TextView stock_ageing,stock_SOH_U,stock_option;
        ImageView stock_image_child;
        RelativeLayout stock_fav;
        ProgressBar loader;

    }


}
