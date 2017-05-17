package apsupportapp.aperotechnologies.com.designapp.Collaboration.Status.Tab_fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import apsupportapp.aperotechnologies.com.designapp.R;


public class StatusSenderSubChildAdapterDetails extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final int PrePosition;
    private final HashMap<Integer, ArrayList<StatusModel>> list;
    private final StatusSenderDetailsAdapter statusSenderDetailsAdapter;


    public StatusSenderSubChildAdapterDetails(HashMap<Integer, ArrayList<StatusModel>> list, Context context, int position, StatusSenderDetailsAdapter statusSenderDetailsAdapter) {
        this.list = list;
        this.context = context;//
        PrePosition = position;
        this.statusSenderDetailsAdapter = statusSenderDetailsAdapter;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_status_sender_details_subchild1, parent, false);
        return new StatusSenderSubChildAdapterDetails.Holder(v);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        ((StatusSenderSubChildAdapterDetails.Holder) holder).StatusSubchild_size.setText(list.get(PrePosition).get(position).getLevel());
        ((StatusSenderSubChildAdapterDetails.Holder) holder).StatusSubchild_reqQty.setText("" + Math.round(list.get(PrePosition).get(position).getStkOnhandQtyRequested()));
        ((StatusSenderSubChildAdapterDetails.Holder) holder).StatusSubchild_scanQty.setText("" + Math.round(list.get(PrePosition).get(position).getStkOnhandQtyAcpt()));

    }

    @Override
    public int getItemCount() {
        return list.get(PrePosition).size();
    }

    private static class Holder extends RecyclerView.ViewHolder {


        private final TextView StatusSubchild_size, StatusSubchild_reqQty, StatusSubchild_scanQty;

        public Holder(View itemView) {
            super(itemView);
            StatusSubchild_size = (TextView) itemView.findViewById(R.id.status_receiver_subdetail_size);
            StatusSubchild_reqQty = (TextView) itemView.findViewById(R.id.status_receiver_subdetail_shortQtyVal);
            StatusSubchild_scanQty = (TextView) itemView.findViewById(R.id.status_receiver_subdetail_avlQtyVal);

        }
    }
}
