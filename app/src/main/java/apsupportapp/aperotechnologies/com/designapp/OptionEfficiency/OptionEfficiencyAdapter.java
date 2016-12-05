package apsupportapp.aperotechnologies.com.designapp.OptionEfficiency;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.data.CombinedData;
import com.google.gson.Gson;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.FreshnessIndex.FreshnessIndexAdapter;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.model.FreshnessIndexDetails;
import apsupportapp.aperotechnologies.com.designapp.model.OptionEfficiencyDetails;

/**
 * Created by pamrutkar on 29/11/16.
 */
public class OptionEfficiencyAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    String fromWhere;
    int level;

    ArrayList<OptionEfficiencyDetails> optionEfficiencyDetailsArrayList;

    ListView oe_listView;
    Gson gson;



    public OptionEfficiencyAdapter(ArrayList<OptionEfficiencyDetails> optionEfficiencyDetailsArrayList, Context context, String fromWhere, ListView oe_listView) {
        this.context = context;
        this.optionEfficiencyDetailsArrayList = optionEfficiencyDetailsArrayList;
        this.fromWhere = fromWhere;
        this.oe_listView = oe_listView;
        level = 1;
        gson = new Gson();
    }

    @Override
    public int getCount() {

        return optionEfficiencyDetailsArrayList.size();
    }

    //Get the data item associated with the specified position in the data set.
    @Override
    public Object getItem(int position) {

        return optionEfficiencyDetailsArrayList.get(position);
    }

    //Get the row id associated with the specified position in the list.
    @Override
    public long getItemId(int position) {

        return position;
    }

    static class ViewHolderItem {
        TextView oe_txtPlanClass,oe_txtSOH_U,oe_txtSOH_Prec,oe_txtOption,oe_txtOption_Perc;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final OptionEfficiencyAdapter.ViewHolderItem viewHolder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.oefficiency_list_row, parent,
                    false);
            viewHolder = new OptionEfficiencyAdapter.ViewHolderItem();
            viewHolder.oe_txtPlanClass = (TextView) convertView.findViewById(R.id.oe_txtPlanClass);
            viewHolder.oe_txtSOH_U = (TextView) convertView.findViewById(R.id.oe_txtSOH_U);
            viewHolder.oe_txtSOH_Prec = (TextView) convertView.findViewById(R.id.oe_txtSOH_Prec);
            viewHolder.oe_txtOption = (TextView) convertView.findViewById(R.id.oe_txtOption);
            viewHolder.oe_txtOption_Perc = (TextView) convertView.findViewById(R.id.oe_txtOption_Perc);

            convertView.setTag(viewHolder);
//            convertView.setTag(R.id.oe_txtPlanClass, viewHolder.oe_txtPlanClass);
//            convertView.setTag(R.id.oe_txtSOH_U, viewHolder.oe_txtSOH_U);
//            convertView.setTag(R.id.oe_txtSOH_Prec, viewHolder.oe_txtSOH_Prec);
//            convertView.setTag(R.id.oe_txtOption, viewHolder.oe_txtOption);
//            convertView.setTag(R.id.oe_txtOption_Perc,viewHolder.oe_txtOption_Perc);

        } else {
            viewHolder = (OptionEfficiencyAdapter.ViewHolderItem) convertView.getTag();
        }

        OptionEfficiencyDetails optionEfficiencyDetails = (OptionEfficiencyDetails) optionEfficiencyDetailsArrayList.get(position);

//        viewHolder.oe_txtPlanClass.setTag(position);
//        viewHolder.oe_txtSOH_U.setTag(position);
//        viewHolder.oe_txtSOH_Prec.setTag(position);
//        viewHolder.oe_txtOption.setTag(position);
//        viewHolder.oe_txtOption_Perc.setTag(position);
        if (fromWhere.equals("Department")) {

            viewHolder.oe_txtPlanClass.setText(optionEfficiencyDetails.getPlanDept());
            viewHolder.oe_txtSOH_U.setText(""+optionEfficiencyDetails.getStkOnhandQty());
            viewHolder.oe_txtSOH_Prec.setText(" "+optionEfficiencyDetails.getStkOnhandQtyCount());
            viewHolder.oe_txtOption.setText(""+optionEfficiencyDetails.getOptionCount());
            viewHolder.oe_txtOption_Perc.setText(""+optionEfficiencyDetails.getOptionCountByStore());

        } else if (fromWhere.equals("Category")) {


            viewHolder.oe_txtPlanClass.setText(optionEfficiencyDetails.getPlanCategory());
            viewHolder.oe_txtSOH_U.setText(""+optionEfficiencyDetails.getStkOnhandQty());
            viewHolder.oe_txtSOH_Prec.setText(" "+optionEfficiencyDetails.getStkOnhandQtyCount());
            viewHolder.oe_txtOption.setText(""+optionEfficiencyDetails.getOptionCount());
            viewHolder.oe_txtOption_Perc.setText(""+optionEfficiencyDetails.getOptionCountByStore());




        } else if (fromWhere.equals("Plan Class")) {

            viewHolder.oe_txtPlanClass.setText(optionEfficiencyDetails.getPlanClass());
            viewHolder.oe_txtSOH_U.setText(""+optionEfficiencyDetails.getStkOnhandQty());
            viewHolder.oe_txtSOH_Prec.setText(" "+optionEfficiencyDetails.getStkOnhandQtyCount());
            viewHolder.oe_txtOption.setText(""+optionEfficiencyDetails.getOptionCount());
            viewHolder.oe_txtOption_Perc.setText(""+optionEfficiencyDetails.getOptionCountByStore());


        } else if (fromWhere.equals("Brand")) {


            viewHolder.oe_txtPlanClass.setText(optionEfficiencyDetails.getBrandName());
            viewHolder.oe_txtSOH_U.setText(""+optionEfficiencyDetails.getStkOnhandQty());
            viewHolder.oe_txtSOH_Prec.setText(" "+optionEfficiencyDetails.getStkOnhandQtyCount());
            viewHolder.oe_txtOption.setText(""+optionEfficiencyDetails.getOptionCount());
            viewHolder.oe_txtOption_Perc.setText(""+optionEfficiencyDetails.getOptionCountByStore());

        } else if (fromWhere.equals("Brand Plan Class")) {


            viewHolder.oe_txtPlanClass.setText(optionEfficiencyDetails.getBrandplanClass());
            viewHolder.oe_txtSOH_U.setText(""+optionEfficiencyDetails.getStkOnhandQty());
            viewHolder.oe_txtSOH_Prec.setText(" "+optionEfficiencyDetails.getStkOnhandQtyCount());
            viewHolder.oe_txtOption.setText(""+optionEfficiencyDetails.getOptionCount());
            viewHolder.oe_txtOption_Perc.setText(""+optionEfficiencyDetails.getOptionCountByStore());

        }



        return convertView;
    }


    public static int convertSpToPixels(double sp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, (float) sp, context.getResources().getDisplayMetrics());
        return px;
    }
}
