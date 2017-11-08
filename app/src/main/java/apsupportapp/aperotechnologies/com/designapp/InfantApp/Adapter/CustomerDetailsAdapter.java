package apsupportapp.aperotechnologies.com.designapp.InfantApp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by rkanawade on 07/11/17.
 */

public class CustomerDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private Context context;
    private RecyclerView recycler_custdetails;
    ArrayList<String> listCustomer;

    public CustomerDetailsAdapter(Context context, RecyclerView recycler_custdetails, ArrayList<String> listCustomer) {

        this.context = context;
        this.recycler_custdetails = recycler_custdetails;
        this.listCustomer = listCustomer;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.adapter_customer_details, parent, false);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        CustDetailsHolder custDetailsHolder = (CustDetailsHolder) holder;
        custDetailsHolder.txt_noofSize.setText("₹\t"+"2506, 10 Units");


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class CustDetailsHolder extends RecyclerView.ViewHolder {

        TextView txt_date, txt_month, txt_year, txt_visit, txt_noofvisit, txt_name, txt_email, txt_avgSize, txt_noofSize, txt_phone, txt_avgVisits, txt_noofVisits;
        RecyclerView recycler_childView;
        View viewDivider;

        public CustDetailsHolder(View itemView) {
            super(itemView);

            txt_noofSize = (TextView)itemView.findViewById(R.id.txt_noofSize);
//            recycler_childView = (RecyclerView) itemView.findViewById(R.id.list);

        }
    }
}
