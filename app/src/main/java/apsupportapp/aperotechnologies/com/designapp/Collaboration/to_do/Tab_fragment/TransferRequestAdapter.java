package apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.ArrayList;


import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Transfer_Request_Model;
import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by pamrutkar on 08/03/17.
 */
public class TransferRequestAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>implements GravitySnapHelper.SnapListener {

    private final Context context;
    private final ArrayList<Transfer_Request_Model> sender_list;
    private static boolean check = false;


    public TransferRequestAdapter(ArrayList<Transfer_Request_Model> list, Context context) {
        this.sender_list = list;
        this.context = context;//


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_child_transferrequest, parent, false);
        return new TransferRequestAdapter.Holder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof TransferRequestAdapter.Holder) {
            if (position < sender_list.size()) {

                // holder.snapTextView.setText(snap.getText());
                ((TransferRequestAdapter.Holder) holder).transferRequest_stockCode.setText(sender_list.get(position).getReqStoreCode());
                ((TransferRequestAdapter.Holder) holder).transferRequest_stockdesc.setText(sender_list.get(position).getReqStoreDesc());
                ((TransferRequestAdapter.Holder) holder).transferRequest_case.setText(sender_list.get(position).getCaseNo());
                ((TransferRequestAdapter.Holder) holder).transferRequest_reqty.setText("" + Math.round(sender_list.get(position).getStkOnhandQtyRequested()));
                ((TransferRequestAdapter.Holder) holder).transferRequest_avlqty.setText("" + Math.round(sender_list.get(position).getStkQtyAvl()));
                ((TransferRequestAdapter.Holder) holder).transferRequest_optreq.setText("" + Math.round(sender_list.get(position).getOptionCount()));
                ((TransferRequestAdapter.Holder) holder).transferRequest_days.setText("No of Days : "+sender_list.get(position).getNoOfDays());
            }
        }
   }

    @Override
    public int getItemCount() {
        return sender_list.size();
    }

    @Override
    public void onSnap(int position) {

    }


    private static class Holder extends RecyclerView.ViewHolder {


        TextView transferRequest_stockCode, transferRequest_case, transferRequest_stockdesc, transferRequest_reqty,
                transferRequest_avlqty, transferRequest_optreq, transferRequest_days;
        CardView transferRequest_cardView;

        public Holder(View itemView) {
            super(itemView);
            transferRequest_stockCode = (TextView) itemView.findViewById(R.id.transferRequest_stockCode);
            transferRequest_case = (TextView) itemView.findViewById(R.id.transferRequest_case);
            transferRequest_stockdesc = (TextView) itemView.findViewById(R.id.transferRequest_stockDesc);
            transferRequest_avlqty = (TextView) itemView.findViewById(R.id.stock_avi);
            transferRequest_reqty = (TextView) itemView.findViewById(R.id.stock_sohRequested);
            transferRequest_optreq = (TextView) itemView.findViewById(R.id.stock_numberOfOption);
            transferRequest_days = (TextView) itemView.findViewById(R.id.transferRequest_days);
          //  transferRequest_cardView = (CardView) itemView.findViewById(R.id.transferRequest_cardView);


        }

    }
}