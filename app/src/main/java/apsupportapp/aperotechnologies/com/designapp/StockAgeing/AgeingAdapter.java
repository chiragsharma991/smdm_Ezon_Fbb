package apsupportapp.aperotechnologies.com.designapp.StockAgeing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.KeyProductPlan.KeyProdFilterAdapter;
import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by rkanawade on 04/10/17.
 */

public class AgeingAdapter extends BaseAdapter {
    private Context context;
    ArrayList<String> ageing_val;
    boolean[] itemChecked;
    public static ArrayList <String> checkedValue_StockAgeing;
    LayoutInflater mInflater;



    public AgeingAdapter(ArrayList<String> ageing_val, Context context) {
        this.ageing_val = ageing_val;
        this.context = context;
        mInflater = LayoutInflater.from(context);
        itemChecked = new boolean[ageing_val.size()];
        checkedValue_StockAgeing = new ArrayList<String>();

    }

    @Override
    public int getCount() {
        return ageing_val.size();
    }

    //Get the data item associated with the specified position in the data set.
    @Override
    public Object getItem(int position) {

        return ageing_val.get(position);
    }

    //Get the row id associated with the specified position in the list.
    @Override
    public long getItemId(int position) {

        return position;
    }

    //Get a View that displays the data at the specified position in the data set.
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Holder viewHolder;

        if (convertView == null) {

            viewHolder = new Holder();
            convertView = mInflater.inflate(R.layout.stock_ageing_adapter, null);
            viewHolder.txtAgeing = (TextView) convertView.findViewById(R.id.txtAgeing);
            viewHolder.checkAgeing = (CheckBox)convertView.findViewById(R.id.checkAgeing);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (Holder) convertView.getTag();
        }
        viewHolder.txtAgeing.setText(ageing_val.get(position));

        viewHolder.checkAgeing.setChecked(false);

        if (itemChecked[position])
            viewHolder.checkAgeing.setChecked(true);
        else
            viewHolder.checkAgeing .setChecked(false);

        viewHolder.checkAgeing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (viewHolder.checkAgeing.isChecked()) {
                    itemChecked[position] = true;
                    checkedValue_StockAgeing.add(viewHolder.txtAgeing.getText().toString());
                }
                else
                {
                    itemChecked[position] = false;
                    checkedValue_StockAgeing.remove(viewHolder.txtAgeing.getText().toString());
                }
            }
        });



        return convertView;
    }



    private class Holder {

        TextView txtAgeing;
        CheckBox checkAgeing;
    }



}
