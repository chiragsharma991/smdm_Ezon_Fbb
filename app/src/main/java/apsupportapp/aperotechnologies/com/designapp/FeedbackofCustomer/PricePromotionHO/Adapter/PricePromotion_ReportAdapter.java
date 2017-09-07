package apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.PricePromotionHO.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.PolicyExchangeHO.Adapter.PolicyExchange_ReportAdapter;
import apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.PricePromotionHO.PricePromotion_Reports;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.SeasonCatalogue.mpm_model;

/**
 * Created by pamrutkar on 04/08/17.
 */
public class PricePromotion_ReportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;
    private final ArrayList<mpm_model> list;
    private final RecyclerViewclick recyclerViewclick;
    private LayoutInflater mInflater;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 2;
    Context context;


    // Disable touch detection for parent recyclerView if we use vertical nested recyclerViews


    public PricePromotion_ReportAdapter(ArrayList<mpm_model> list, Context context, PricePromotion_Reports pricePromotion_reports) {

        this.context = context;
        this.recyclerViewclick = (RecyclerViewclick)pricePromotion_reports;
        this.list = list;
        mInflater = LayoutInflater.from(this.context);
    }




    @Override
    public int getItemViewType(int position) {


        return VIEW_ITEM;

    }



    @Override
    public int getItemCount() {
        return list.size();
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_report_feedback_rowchild, parent, false);
            return new ReportViewHolder(v);
        }

        return null;

    }


    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {

        if (viewHolder instanceof ReportViewHolder) {
            if (position < list.size()) {

                final mpm_model data = list.get(position);

                ((PricePromotion_ReportAdapter.ReportViewHolder) viewHolder).mobileNumber.setText(data.getAttribute1());
                ((PricePromotion_ReportAdapter.ReportViewHolder) viewHolder).remark.setText(data.getAttribute2());
                ((PricePromotion_ReportAdapter.ReportViewHolder) viewHolder).date.setText(data.getArcDate().substring(0,data.getArcDate().length()-3));
                ((PricePromotion_ReportAdapter.ReportViewHolder) viewHolder).mobileNumber.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        recyclerViewclick.onclickList(position);
                    }
                });
                ((PricePromotion_ReportAdapter.ReportViewHolder) viewHolder).remark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        recyclerViewclick.onclickList(position);
                    }
                });
                ((PricePromotion_ReportAdapter.ReportViewHolder) viewHolder).date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        recyclerViewclick.onclickList(position);
                    }
                });
            }}
    }



    public class ReportViewHolder extends RecyclerView.ViewHolder {


        TextView mobileNumber,remark,date;

        public ReportViewHolder(View itemView) {
            super(itemView);

            mobileNumber = (TextView) itemView.findViewById(R.id.cf_mobilenumber);
            remark = (TextView) itemView.findViewById(R.id.cf_remark);
            date = (TextView) itemView.findViewById(R.id.cf_date);


        }
    }

    public interface RecyclerViewclick{
        void onclickList (int position);
    }

}
