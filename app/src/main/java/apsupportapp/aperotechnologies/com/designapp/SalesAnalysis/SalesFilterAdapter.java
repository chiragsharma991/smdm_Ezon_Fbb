package apsupportapp.aperotechnologies.com.designapp.SalesAnalysis;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by pamrutkar on 29/12/16.
 */
public class SalesFilterAdapter extends BaseAdapter implements Filterable {

    private ArrayList<String> arrayList;
    private ArrayList<String> mFilterList;

    //private List mStringFilterList;

    private LayoutInflater mInflater;
    Context context;
    private int Position;
    SFilter sFilter;
    List<String> selectedItemsPositions;//to store all selected items position


    //private ValueFilter valueFilter;

    public SalesFilterAdapter(ArrayList<String> arrayList, Context context) {

        // Log.e("in sales analysis adapter"," ");
        this.arrayList = arrayList;
        this.mFilterList = arrayList;
        this.context = context;
        mInflater = LayoutInflater.from(context);
        selectedItemsPositions = new ArrayList<>();
        getFilter();

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
            convertView = mInflater.inflate(R.layout.activity_filter_child, null);
            holder.FilterMc = (TextView) convertView.findViewById(R.id.filterMc);
            holder.checkBox=(CheckBox)convertView.findViewById(R.id.checkFilterMc);
            holder.checkBox.setVisibility(View.INVISIBLE);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();

        }
        holder.FilterMc.setText(arrayList.get(position));
//        if(selectedItemsPositions.contains(holder.FilterMc.getTag()))
//        {
//            convertView.setBackgroundColor();
//        }
//        else
//        {
//            holder.checkBox.setChecked(false);
//        }
//
//       convertView.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View v) {
//
//               RelativeLayout rel = (RelativeLayout) v;
//               CheckBox cb = (CheckBox) rel.getChildAt(1);
//               TextView txtView = (TextView) rel.getChildAt(0);
//               String txtClickedVal = txtView.getText().toString();
//
//
//               if(cb.isChecked() == false)
//               {
//                   selectedItemsPositions.add(arrayList.get(position));
//                   cb.setChecked(true);
//               }
//               else
//               {
//                   selectedItemsPositions.remove(holder.FilterMc);
//                   cb.setChecked(false);
//               }
//           }
//       });

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (sFilter == null) {
            sFilter = new SFilter();
        }
        return sFilter;
    }

    public class SFilter extends Filter {

        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<String> filterList = new ArrayList<String>();
                Log.e("Filter Array Size", "" + mFilterList.size());
                for (int i = 0; i < mFilterList.size(); i++) {
                    if (mFilterList.get(i).toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filterList.add(mFilterList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mFilterList.size();
                results.values = mFilterList;
            }
            return results;
        }

        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            arrayList = (ArrayList<String>) results.values;

            SalesFilter.salesFilterAdapter.notifyDataSetChanged();


            SalesFilter.categoryAdapter.notifyDataSetChanged();


            SalesFilter.planClassAdapter.notifyDataSetChanged();


            SalesFilter.branAdapter.notifyDataSetChanged();


            SalesFilter.brandClsAdapter.notifyDataSetChanged();


        }
    }


    private class Holder {

        TextView FilterMc;
        CheckBox checkBox;


    }

}
