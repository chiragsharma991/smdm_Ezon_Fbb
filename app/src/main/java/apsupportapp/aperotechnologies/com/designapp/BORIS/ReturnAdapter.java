package apsupportapp.aperotechnologies.com.designapp.BORIS;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by rkanawade on 21/08/17.
 */

public class ReturnAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    RecyclerView recycler_view_return_catalogue;
    String status;

    public ReturnAdapter(Context context, RecyclerView recycler_view_return_catalogue, String status) {
        this.context = context;
        this.recycler_view_return_catalogue = recycler_view_return_catalogue;
        this.status = status;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View v0 = inflater.inflate(R.layout.adapter_return_catalogue, parent, false);
                viewHolder = new ReturnCatHolder(v0, context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
         final ReturnCatHolder returnCatHolder = (ReturnCatHolder) holder;
        returnCatHolder.text_order_no.setText("473434");

        Glide.with(context).load(R.mipmap.noimageavailable).placeholder(R.mipmap.noimageavailable).centerCrop().into(returnCatHolder.img_product);

        Glide.with(context).load(R.mipmap.noimageavailable).placeholder(R.mipmap.noimageavailable).centerCrop().into(returnCatHolder.img_promo_product);

        if(status.equals(" "))
        {
            returnCatHolder.text_status_value.setVisibility(View.GONE);
            returnCatHolder.text_status.setVisibility(View.GONE);
        }
        else if(status.equals("accepted"))
        {
            returnCatHolder.text_status_value.setVisibility(View.VISIBLE);
            returnCatHolder.text_status_value.setText("Return Accepted");
            returnCatHolder.text_status.setVisibility(View.VISIBLE);
            returnCatHolder.text_status_value.setTextColor(context.getResources().getColor(R.color.green));

        }
        else if(status.equals("rejected"))
        {
            returnCatHolder.text_status_value.setVisibility(View.VISIBLE);
            returnCatHolder.text_status_value.setText("Return Rejected");
            returnCatHolder.text_status.setVisibility(View.VISIBLE);
            returnCatHolder.text_status_value.setTextColor(context.getResources().getColor(R.color.ezfbb_red));

        }

        returnCatHolder.img_free_product_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(returnCatHolder.rel_promo_product.getVisibility()==View.VISIBLE)
                {
                    returnCatHolder.rel_promo_product.setVisibility(View.GONE);
                }
                else
                {
                    returnCatHolder.rel_promo_product.setVisibility(View.VISIBLE);
                }
            }
        });

        returnCatHolder.card_catalogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent int_detail = new Intent(context, ReturnDetailActivity.class);
                context.startActivity(int_detail);
                ((Activity)context).finish();
            }
        });
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }


    @Override
    public int getItemCount() {
        return 1;
    }

    class ReturnCatHolder extends RecyclerView.ViewHolder {
        RelativeLayout rel_promo_product, img_free_product_click;
        CardView card_catalogue;
        ImageView img_product, img_promo_product;
        TextView text_order_no, txt_title, txt_description, txt_size_number, txt_qty_number, txt_sku_id_no, txt_price, txt_promo_title, txt_promo_description,
                txt_promo_size_number, txt_promo_qty_number, txt_promo_price, text_status_value, text_status;

        public ReturnCatHolder(View v, Context context) {
            super(v);
            rel_promo_product = (RelativeLayout) v.findViewById(R.id.rel_promo_product);
            img_product = (ImageView) v.findViewById(R.id.img_product);
            img_free_product_click = (RelativeLayout) v.findViewById(R.id.img_free_product_click);
            img_promo_product = (ImageView) v.findViewById(R.id.img_promo_product);
            card_catalogue = (CardView) v.findViewById(R.id.card_catalogue);
            text_order_no = (TextView) v.findViewById(R.id.text_order_no);
            txt_title = (TextView) v.findViewById(R.id.txt_title);
            txt_description = (TextView) v.findViewById(R.id.txt_description);
            txt_size_number = (TextView) v.findViewById(R.id.txt_size_number);
            txt_qty_number = (TextView) v.findViewById(R.id.txt_qty_number);
            txt_sku_id_no = (TextView) v.findViewById(R.id.txt_sku_id_no);
            txt_price = (TextView) v.findViewById(R.id.txt_price);
            txt_promo_title = (TextView) v.findViewById(R.id.txt_promo_title);
            txt_promo_description = (TextView) v.findViewById(R.id.txt_promo_description);
            txt_promo_size_number = (TextView) v.findViewById(R.id.txt_promo_size_number);
            txt_promo_qty_number = (TextView) v.findViewById(R.id.txt_promo_qty_number);
            txt_promo_price = (TextView) v.findViewById(R.id.txt_promo_price);
            text_status_value = (TextView) v.findViewById(R.id.text_status_value);
            text_status = (TextView) v.findViewById(R.id.text_status);

        }


    }


}
