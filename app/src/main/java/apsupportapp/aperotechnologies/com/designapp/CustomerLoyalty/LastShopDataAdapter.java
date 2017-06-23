package apsupportapp.aperotechnologies.com.designapp.CustomerLoyalty;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by pamrutkar on 23/06/17.
 */
public class LastShopDataAdapter extends BaseAdapter {
    private ArrayList<CustomerDetail> detailArrayList;
    private LayoutInflater mInflater;


    public LastShopDataAdapter(Context context, ArrayList<CustomerDetail> customerDetailArrayList) {
        this.detailArrayList = customerDetailArrayList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return detailArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return detailArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LastShopDataAdapter.Holder viewHolder;
        if (convertView == null) {
            viewHolder = new LastShopDataAdapter.Holder();
            convertView = mInflater.inflate((R.layout.activity_item_listview), null);
            viewHolder.txt_cd_product_one = (TextView)convertView.findViewById(R.id.txt_cd_product_one);
            viewHolder.txt_cd_product_two = (TextView)convertView.findViewById(R.id.txt_cd_product_two);
            viewHolder.txt_cd_product_three = (TextView)convertView.findViewById(R.id.txt_cd_product_three);
            viewHolder.txt_cd_product_one_val = (TextView)convertView.findViewById(R.id.txt_cd_product_one_val);
            viewHolder.txt_cd_product_two_val = (TextView)convertView.findViewById(R.id.txt_cd_product_two_val);
            viewHolder.txt_cd_product_three_val = (TextView)convertView.findViewById(R.id.txt_cd_product_three_val);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (LastShopDataAdapter.Holder) convertView.getTag();
        }
        viewHolder.txt_cd_product_one.setText(detailArrayList.get(0).getLastShoppedProduct1());
        viewHolder.txt_cd_product_two.setText(detailArrayList.get(0).getLastShoppedProduct2());
        viewHolder.txt_cd_product_three.setText(detailArrayList.get(0).getLastShoppedProduct3());
        viewHolder.txt_cd_product_one_val.setText("₹ "+Math.round(detailArrayList.get(0).getLastShoppedProduct1Sales()));
        viewHolder.txt_cd_product_two_val.setText("₹ "+Math.round(detailArrayList.get(0).getLastShoppedProduct2Sales()));
        viewHolder.txt_cd_product_three_val.setText("₹ "+Math.round(detailArrayList.get(0).getLastShoppedProduct3Sales()));
        return convertView;
    }

    private class Holder {
        TextView txt_cd_product_one,txt_cd_product_two,txt_cd_product_three;
        TextView txt_cd_product_one_val,txt_cd_product_two_val,txt_cd_product_three_val;
    }
}

