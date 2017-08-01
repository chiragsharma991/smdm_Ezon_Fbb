package apsupportapp.aperotechnologies.com.designapp.FeedbackofCustomer.AvailabilityAndNotifyHO.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.HourlyPerformence.HourlyAdapter;
import apsupportapp.aperotechnologies.com.designapp.MPM.mpm_model;
import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by csuthar on 01/08/17.
 */

public class ReportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;
    private final ArrayList<mpm_model> list;
    private LayoutInflater mInflater;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 2;
    Context context;


    // Disable touch detection for parent recyclerView if we use vertical nested recyclerViews


    public ReportAdapter(ArrayList<mpm_model> list, Context context) {

        this.context = context;
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

                ((ReportViewHolder) viewHolder).mobileNumber.setText(data.getAttribute1());
                ((ReportViewHolder) viewHolder).remark.setText(data.getAttribute2());
                ((ReportViewHolder) viewHolder).date.setText(data.getArcDate());
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
}
