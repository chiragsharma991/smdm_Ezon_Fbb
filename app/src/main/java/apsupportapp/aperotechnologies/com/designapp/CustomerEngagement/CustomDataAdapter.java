package apsupportapp.aperotechnologies.com.designapp.CustomerEngagement;

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
public class CustomDataAdapter extends BaseAdapter {
    private ArrayList<CustomerRecomdtn> detailArrayList;
    private LayoutInflater mInflater;


    public CustomDataAdapter(Context context, ArrayList<CustomerRecomdtn> customerDetailArrayList) {
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
        Holder viewHolder;
        if (convertView == null) {
            viewHolder = new Holder();
            convertView = mInflater.inflate((R.layout.activity_offer_listview), null);
            viewHolder.txt_cd_product_one = (TextView)convertView.findViewById(R.id.txt_cd_product_one);

            viewHolder.txt_cd_product_one_val = (TextView)convertView.findViewById(R.id.txt_cd_product_one_val);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (Holder) convertView.getTag();
        }

            viewHolder.txt_cd_product_one.setText(detailArrayList.get(position).getProductCodeTarget());



            return convertView;
    }

    private class Holder
    {
        TextView txt_cd_product_one,txt_cd_product_two,txt_cd_product_three;
        TextView txt_cd_product_one_val,txt_cd_product_two_val,txt_cd_product_three_val;
    }
}

