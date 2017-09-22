package apsupportapp.aperotechnologies.com.designapp.CustomerEngagement;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.gson.Gson;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;



import apsupportapp.aperotechnologies.com.designapp.R;


import static apsupportapp.aperotechnologies.com.designapp.CustomerEngagement.CustomerLookup_PageOne.customerDetailsList;



/**
 * Created by pamrutkar on 20/06/17.
 */
public class CustomerDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;
    private  ArrayList<CustomerDetail> detailArrayList;
    private ArrayList<CustomerDetail> detailFilterList;
    CustomerDetailAdapter customerDetailAdapter;
    ValueFilter valueFilter;
    RecyclerView listViewFIndex;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 2;
    Context context;
    Gson gson;

    // Disable touch detection for parent recyclerView if we use vertical nested recyclerViews


    public CustomerDetailAdapter(ArrayList customerDetailsList, Context context)
    {
        this.context = context;
        this.detailArrayList = customerDetailsList;
        this.detailFilterList = detailArrayList;
        getFilter();
        gson = new Gson();
    }

    @Override
    public int getItemViewType(int position) {

//        if (isPositionItem(position)) {
//            return VIEW_ITEM;
//
//        } else {
//            return VIEW_PROG;
//        }


        if(customerDetailsList.get(position)  == null)
        {
            Log.e( "getItemViewType: ", ""+position);
            return VIEW_PROG;

        }
        else
        {
            return VIEW_ITEM;
        }
    }

    private boolean isPositionItem(int position) {


        return position != customerDetailsList.size();
    }
    @Override
    public int getItemCount()
    {
        if(customerDetailsList != null)
        {
            return customerDetailsList.size();
        }
        else
        {
            return 0;
        }

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        if (viewType == VIEW_ITEM)
        {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cust_detail_row, parent, false);
        return new CustomerDetailAdapter.CustDetailHolder(v);
        }
        else if (viewType == VIEW_PROG)
        {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.bestpromo_footer, parent, false);
            return new CustomerDetailAdapter.ProgressViewHolder(v);
        }

        return null;

    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position)
    {
        if (viewHolder instanceof CustDetailHolder)
        {
            if (position < customerDetailsList.size())
            {
                CustomerDetail customerDetail = customerDetailsList.get(position);
                NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("", "in"));
                ((CustomerDetailAdapter.CustDetailHolder) viewHolder).txt_cust_name_Val.setText(customerDetail.getFullName());
                ((CustomerDetailAdapter.CustDetailHolder) viewHolder).txt_cust_lastVisit_Val.setText(customerDetail.getLastPurchaseDate());
                ((CustomerDetailAdapter.CustDetailHolder) viewHolder).txt_cust_planSales_Val.setText("" + formatter.format(Math.round(customerDetail.getMbrPlanSaleNetVal())));
                ((CustomerDetailAdapter.CustDetailHolder) viewHolder).txt_cust_sales_Val.setText("" + formatter.format(Math.round(customerDetail.getSpend())));
                ((CustomerDetailAdapter.CustDetailHolder) viewHolder).txt_cust_ach_Val.setText(" " + Math.round(customerDetail.getSalesAch()) + "%");

              if(Math.round(customerDetail.getSalesAch()) > 90)
                {
                  //  ((CustomerDetailAdapter.CustDetailHolder) viewHolder).txt_cust_ach_Val.setBackgroundColor(Color.parseColor("#70e503"));
                    ((CustomerDetailAdapter.CustDetailHolder) viewHolder).txt_cust_ach_Val.setTextColor(Color.parseColor("#70e503"));
                }
                else if((Math.round(customerDetail.getSalesAch()) >= 80) && (Math.round(customerDetail.getSalesAch()) <= 90))
              {
                 // ((CustomerDetailAdapter.CustDetailHolder) viewHolder).txt_cust_ach_Val.setBackgroundColor(Color.parseColor("#ff7e00"));
                  ((CustomerDetailAdapter.CustDetailHolder) viewHolder).txt_cust_ach_Val.setTextColor(Color.parseColor("#ff7e00"));

              }
              else if(Math.round(customerDetail.getSalesAch())  < 80)
              {
                //  ((CustomerDetailAdapter.CustDetailHolder) viewHolder).txt_cust_ach_Val.setBackgroundColor(Color.parseColor("#fe0000"));
                  ((CustomerDetailAdapter.CustDetailHolder) viewHolder).txt_cust_ach_Val.setTextColor(Color.parseColor("#fe0000"));

              }
            }
        }
    }

    public static class CustDetailHolder extends RecyclerView.ViewHolder
    {
        TextView txt_cust_name_Val, txt_cust_lastVisit_Val, txt_cust_planSales_Val, txt_cust_sales_Val, txt_cust_ach_Val;

        public CustDetailHolder(View itemView)
        {
            super(itemView);
            txt_cust_name_Val = (TextView) itemView.findViewById(R.id.txt_cust_name_Val);
            txt_cust_lastVisit_Val = (TextView) itemView.findViewById(R.id.txt_cust_lastVisit_Val);
            txt_cust_planSales_Val = (TextView) itemView.findViewById(R.id.txt_cust_planSales_Val);
            txt_cust_ach_Val = (TextView) itemView.findViewById(R.id.txt_cust_ach_Val);
            txt_cust_sales_Val = (TextView) itemView.findViewById(R.id.txt_cust_sales_Val);
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtView;
        public static ProgressBar cust_progressBar;
        public ProgressViewHolder(View footerView)
        {
            super(footerView);
            txtView = (TextView) footerView.findViewById(R.id.txtView);
            cust_progressBar = (ProgressBar) footerView.findViewById(R.id.cust_progressBar);
        }
    }

    @Override
    public Filter getFilter()
    {
        if (valueFilter == null)
        {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter
    {
        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            FilterResults results = new FilterResults();
            Log.e("char :",""+constraint);
            if (constraint != null && constraint.length() > 0)
            {
                ArrayList<CustomerDetail> filterList = new ArrayList<CustomerDetail>();
                for(int i = 0; i < detailFilterList.size(); i++)
                {
                    if(detailFilterList.get(i).getFullName().toString().toLowerCase().contains(constraint.toString().toLowerCase()))
                    {
                        filterList.add(detailFilterList.get(i));
                        Log.e("filter list size :",""+filterList.size());
                    }
                }
                results.count = filterList.size() ;
                results.values = filterList;
            }
            else
            {
                results.count = detailFilterList.size();
                results.values = detailFilterList;
            }
            return results;
        }

        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")

        @Override
        protected void publishResults(CharSequence constraint,FilterResults results)
        {
            customerDetailsList = (ArrayList<CustomerDetail>) results.values ;
            notifyDataSetChanged();
        }
    }
}
