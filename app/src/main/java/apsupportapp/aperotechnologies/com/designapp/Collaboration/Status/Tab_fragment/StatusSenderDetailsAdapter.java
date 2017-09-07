package apsupportapp.aperotechnologies.com.designapp.Collaboration.Status.Tab_fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment.OnPress;
import apsupportapp.aperotechnologies.com.designapp.R;


/**
 * Created by csuthar on 10/03/17.
 */

public class StatusSenderDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ProgressBar senderDetailProcess;
    private Context context;
    private ArrayList<StatusModel> list;
    private static boolean[] Toggle;
    public OnPress onPressInterface;

    public StatusSenderDetailsAdapter(ArrayList<StatusModel> list, Context context, ProgressBar senderDetailProcess) {
        this.list = list;
        this.context = context;
        Toggle = new boolean[list.size()];
        onPressInterface = (OnPress) context;
        this.senderDetailProcess = senderDetailProcess;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_status_sender_details_child1, parent, false);
        return new StatusSenderDetailsAdapter.Holder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof StatusSenderDetailsAdapter.Holder) {
            if (position < list.size()) {
              //  senderDetailProcess.setVisibility(View.GONE);

                HandlePositionOnSet(holder, position);
                ((StatusSenderDetailsAdapter.Holder) holder).OptionLevel.setText(list.get(position).getLevel());
                ((StatusSenderDetailsAdapter.Holder) holder).ReqQty.setText("" + Math.round(list.get(position).getStkOnhandQtyRequested()));
                ((StatusSenderDetailsAdapter.Holder) holder).ScanQty.setText("" + Math.round(list.get(position).getStkOnhandQtyAcpt()));
                ((StatusSenderDetailsAdapter.Holder) holder).OptionLevel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (Toggle[position] == true) {
                            Toggle[position] = false;
                            notifyDataSetChanged();

                        } else {
                            Toggle[position] = true;

                            if (ToBeSenderDetails.StatusHashmapChildList.get(position).isEmpty()) {
                                if (senderDetailProcess.getVisibility() == View.GONE) {
                                    senderDetailProcess.setVisibility(View.VISIBLE);
                                    onPressInterface.onPress(position);
                                }
                            } else {
                                notifyDataSetChanged();
                            }
                        }
                    }
                });

                StatusSenderSubChildAdapterDetails detailsHeaderChildAdapter = new StatusSenderSubChildAdapterDetails(ToBeSenderDetails.StatusHashmapChildList, context, position, StatusSenderDetailsAdapter.this);
                ((StatusSenderDetailsAdapter.Holder) holder).StatusChildListView.setAdapter(detailsHeaderChildAdapter);
            }
        }
    }

    private void HandlePositionOnSet(RecyclerView.ViewHolder holder, int position) {
        if (Toggle[position]) {
            ((StatusSenderDetailsAdapter.Holder) holder).StatusDetailChild_Layout.setVisibility(View.VISIBLE);

        } else {
            ((StatusSenderDetailsAdapter.Holder) holder).StatusDetailChild_Layout.setVisibility(View.GONE);

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        private TextView OptionLevel, ReqQty, ScanQty;
        private LinearLayout StatusDetailChild_Layout;
        private RecyclerView StatusChildListView;

        public Holder(View itemView) {
            super(itemView);
            OptionLevel = (TextView) itemView.findViewById(R.id.statusSender_detail_optionLevel);
            ReqQty = (TextView) itemView.findViewById(R.id.status_sender_detail_reqQty);
            ScanQty = (TextView) itemView.findViewById(R.id.status_sender_detail_ScanedQty);
            StatusDetailChild_Layout = (LinearLayout) itemView.findViewById(R.id.StatusDetailChild_size);
            StatusChildListView = (RecyclerView) itemView.findViewById(R.id.StatusSenderdetails_SubChild);
        }

    }
}
