package apsupportapp.aperotechnologies.com.designapp.FreshnessIndex;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import java.util.ArrayList;
import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by pamrutkar on 22/11/16.
 */
public class FreshnessIndexAdapter extends BaseAdapter {

    private Context context;
    String fromWhere;
    ArrayList<FreshnessIndexDetails> freshnessIndexDetailsArrayList;
    ListView listViewFIndex;
    Gson gson;


    public FreshnessIndexAdapter(ArrayList<FreshnessIndexDetails> freshnessIndexDetailsArrayList, Context context, String fromWhere, ListView listViewFIndex) {
        this.context = context;
        this.freshnessIndexDetailsArrayList = freshnessIndexDetailsArrayList;
        this.fromWhere = fromWhere;
        this.listViewFIndex = listViewFIndex;

        gson = new Gson();
    }

    @Override
    public int getCount() {

        return freshnessIndexDetailsArrayList.size();
    }

    //Get the data item associated with the specified position in the data set.
    @Override
    public Object getItem(int position) {

        return freshnessIndexDetailsArrayList.get(position);
    }

    //Get the row id associated with the specified position in the list.
    @Override
    public long getItemId(int position) {

        return position;
    }

    static class ViewHolderItem {
        TextView txtfindexClass, txtfindexSOH, txtfindexSOH_U, txtfindexGIT;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final FreshnessIndexAdapter.ViewHolderItem viewHolder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.findex_list_row, parent,
                    false);
            viewHolder = new FreshnessIndexAdapter.ViewHolderItem();
            viewHolder.txtfindexClass = (TextView) convertView.findViewById(R.id.txtfindexClass);
            viewHolder.txtfindexSOH = (TextView) convertView.findViewById(R.id.txtfindexSOH);
            viewHolder.txtfindexSOH_U = (TextView) convertView.findViewById(R.id.txtfindexSOH_U);
            viewHolder.txtfindexGIT = (TextView) convertView.findViewById(R.id.txtfindexGIT);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FreshnessIndexAdapter.ViewHolderItem) convertView.getTag();
        }

        FreshnessIndexDetails freshnessIndexDetails = (FreshnessIndexDetails) freshnessIndexDetailsArrayList.get(position);
        if (fromWhere.equals("Department")) {

            viewHolder.txtfindexClass.setText(freshnessIndexDetails.getPlanDept());
            viewHolder.txtfindexSOH.setText("" + freshnessIndexDetails.getStkOnhandQty());
            viewHolder.txtfindexSOH_U.setText(" " + String.format("%.1f", freshnessIndexDetails.getStkOnhandQtyCount()));
            viewHolder.txtfindexGIT.setText("" + Math.round(freshnessIndexDetails.getStkGitQty()));

        } else if (fromWhere.equals("Category")) {

            viewHolder.txtfindexClass.setText(freshnessIndexDetails.getPlanCategory());
            viewHolder.txtfindexSOH.setText("" + freshnessIndexDetails.getStkOnhandQty());
            viewHolder.txtfindexSOH_U.setText(" " + String.format("%.1f", freshnessIndexDetails.getStkOnhandQtyCount()));
            viewHolder.txtfindexGIT.setText("" + Math.round(freshnessIndexDetails.getStkGitQty()));


        } else if (fromWhere.equals("Plan Class")) {

            viewHolder.txtfindexClass.setText(freshnessIndexDetails.getPlanClass());
            viewHolder.txtfindexSOH.setText("" + freshnessIndexDetails.getStkOnhandQty());
            viewHolder.txtfindexSOH_U.setText(" " + String.format("%.1f", freshnessIndexDetails.getStkOnhandQtyCount()));
            viewHolder.txtfindexGIT.setText("" + Math.round(freshnessIndexDetails.getStkGitQty()));


        } else if (fromWhere.equals("Brand")) {

            viewHolder.txtfindexClass.setText(freshnessIndexDetails.getBrandName());
            viewHolder.txtfindexSOH.setText("" + (int) freshnessIndexDetails.getStkOnhandQty());
            viewHolder.txtfindexSOH_U.setText(" " + String.format("%.1f", freshnessIndexDetails.getStkOnhandQtyCount()));
            viewHolder.txtfindexGIT.setText("" + Math.round(freshnessIndexDetails.getStkGitQty()));

        } else if (fromWhere.equals("Brand Plan Class")) {

            viewHolder.txtfindexClass.setText(freshnessIndexDetails.getBrandplanClass());
            viewHolder.txtfindexSOH.setText("" + (int) freshnessIndexDetails.getStkOnhandQty());
            viewHolder.txtfindexSOH_U.setText(" " + String.format("%.1f", freshnessIndexDetails.getStkOnhandQtyCount()));
            viewHolder.txtfindexGIT.setText("" + Math.round(freshnessIndexDetails.getStkGitQty()));

        }
        return convertView;
    }


}


