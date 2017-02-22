package apsupportapp.aperotechnologies.com.designapp.SellThruExceptions;

/**
 * Created by csuthar on 05/12/16.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;


/**
 * Created by csuthar on 29/11/16.
 */




public class SaleThruInventoryAdapter extends BaseAdapter {

    private ArrayList<RunningPromoListDisplay> arrayList;

    //private List mStringFilterList;

    private LayoutInflater mInflater;
    Context context;
    private int Position;

    //private ValueFilter valueFilter;

    public SaleThruInventoryAdapter(ArrayList<RunningPromoListDisplay> arrayList, Context context) {

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
            convertView = mInflater.inflate(R.layout.activity_salethru_inventory_child, null);
            holder.BestInvent_SOH = (TextView) convertView.findViewById(R.id.bestInvent_SOH);
            holder.BestInvent_sellThru = (TextView) convertView.findViewById(R.id.bestInvent_sellThru);
            holder.ProgressPicaso = (ProgressBar) convertView.findViewById(R.id.progressPicaso);
            holder.ProgressPicaso.setVisibility(View.VISIBLE);
            holder.BestInvent_Sale = (TextView) convertView.findViewById(R.id.bestInvent_Sale);
            holder.bestInvent_zonalSell = (TextView) convertView.findViewById(R.id.bestInvent_zonalSell);
            holder.bestInvent_wks = (TextView) convertView.findViewById(R.id.bestInvent_wks);
            holder.BestInvent_option = (TextView) convertView.findViewById(R.id.bestInvent_option);
           // holder.BestInventTable_SOH = (TextView) convertView.findViewById(R.id.bestInventTable_SOH);
          //  holder.BestInventTable_ProdAttribute = (TextView) convertView.findViewById(R.id.bestInventTable_ProdAttribute);
            holder.BestInvent_image_child = (ImageView) convertView.findViewById(R.id.bestInvent_image_child);




            convertView.setTag(holder);

        } else {
            holder=(Holder)convertView.getTag();
            holder.ProgressPicaso.setVisibility(View.VISIBLE);


        }
        holder.BestInvent_SOH.setText(""+Math.round(arrayList.get(position).getStkOnhandQty()));
        holder.BestInvent_sellThru.setText(String.format("%.1f",arrayList.get(position).getSellthruUnits()));

        holder.bestInvent_zonalSell.setText(String.format("%.1f",arrayList.get(position).getSellthruUnitsZonal()));
        holder.BestInvent_Sale.setText(""+(int)arrayList.get(position).getSaleTotQty());
       // holder.bestInvent_wks.setText(""+(int)arrayList.get(position).getFwdWeekCover());
        holder.BestInvent_option.setText(arrayList.get(position).getOption());
        //holder.BestInventTable_SOH.setText(""+(int)arrayList.get(position).getStkOnhandQty());
       // holder.BestInventTable_ProdAttribute.setText(arrayList.get(position).getProdAttribute2());
        SaleThruInventory.BestInvent_txtStoreCode.setText(arrayList.get(position).getStoreCode());
        SaleThruInventory.BestInvent_txtStoreName.setText(arrayList.get(position).getStoreDesc());

        if(!arrayList.get(position).getProdImageURL().equals("")) {

            Glide.with(this.context)
                    .load(arrayList.get(position).getProdImageURL())
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.ProgressPicaso.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.ProgressPicaso.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.BestInvent_image_child);

        }else {
            holder.ProgressPicaso.setVisibility(View.GONE);

            Glide.with(this.context).
                    load(R.mipmap.placeholder).
                    into(holder.BestInvent_image_child);


        }



        // ---------------------click listener -------------------------






        return convertView;
    }



    private class Holder {

        TextView BestInvent_SOH,BestInvent_sellThru,
                BestInvent_Sale,bestInvent_zonalSell,bestInvent_thru,bestInvent_wks,BestInvent_option;

        ImageView BestInvent_image_child;


        public ProgressBar ProgressPicaso;
    }




}
