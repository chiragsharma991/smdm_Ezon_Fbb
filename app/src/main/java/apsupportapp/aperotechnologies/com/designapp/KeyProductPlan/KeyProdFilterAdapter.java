package apsupportapp.aperotechnologies.com.designapp.KeyProductPlan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by pamrutkar on 03/03/17.
 */
public class KeyProdFilterAdapter extends BaseAdapter implements Filterable {

    private ArrayList<String> mProdStringList;
    private ArrayList<String> mProdStringFilterList;
    private LayoutInflater mInflater;

    boolean[] itemChecked;
    public static ArrayList <String> checkedValue;
    private PlanKeyProdFilter keyProdFilter;


    public KeyProdFilterAdapter(ArrayList<String> mStringList, Context context) {
        this.mProdStringList = mStringList;
        this.mProdStringFilterList = mProdStringList;
        mInflater = LayoutInflater.from(context);
        itemChecked = new boolean[mProdStringList.size()];
        checkedValue = new ArrayList<String>();
        getFilter();
    }

    @Override
    public int getCount() {
        return mProdStringList.size();
    }

    //Get the data item associated with the specified position in the data set.
    @Override
    public Object getItem(int position) {

        return mProdStringList.get(position);
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
            convertView = mInflater.inflate(R.layout.activity_keyprodfilter_listview, null);
            viewHolder.txt_product = (TextView) convertView.findViewById(R.id.txtplanProduct);
            viewHolder.check_product = (CheckBox)convertView.findViewById(R.id.checkProductName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (Holder) convertView.getTag();
        }
        viewHolder.txt_product.setText(mProdStringList.get(position));

        viewHolder.check_product.setChecked(false);

        if (itemChecked[position])
            viewHolder.check_product.setChecked(true);
        else
            viewHolder.check_product .setChecked(false);

        viewHolder.check_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (viewHolder.check_product.isChecked()) {
                    itemChecked[position] = true;
                    checkedValue.add(viewHolder.txt_product.getText().toString());
                }
                else
                {
                    itemChecked[position] = false;
                    checkedValue.remove(viewHolder.txt_product.getText().toString());
                }
            }
        });



        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (keyProdFilter == null) {
            keyProdFilter = new PlanKeyProdFilter();
        }
        return keyProdFilter;
    }

    private class Holder {

        TextView txt_product;
        CheckBox check_product;
    }

    private class PlanKeyProdFilter extends Filter {

        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<String> filterList = new ArrayList<String>();
                for (int i = 0; i < mProdStringFilterList.size(); i++) {
                    if (mProdStringFilterList.get(i).toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filterList.add(mProdStringFilterList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mProdStringFilterList.size();
                results.values = mProdStringFilterList;
            }
            return results;
        }

        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            mProdStringList = (ArrayList<String>) results.values;
            notifyDataSetChanged();
        }
    }

}
