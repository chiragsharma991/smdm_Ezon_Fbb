package apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
    private final boolean[] selectMc;
    private OnItemClickListener mListener;


    public TransferRequestAdapter(ArrayList<Transfer_Request_Model> list, boolean[] selectMc, Context context, OnItemClickListener mListener) {
        this.sender_list = list;
        this.context = context;//
        this.selectMc = selectMc;
        this.mListener = mListener;


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_child_transferrequest, parent, false);
        return new TransferRequestAdapter.Holder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof TransferRequestAdapter.Holder)
        {
            if (position < sender_list.size()) {

              //  ((TransferRequestAdapter.Holder) holder).transferRequest_stockCode.setText(sender_list.get(position).getReqStoreCode());
                ((TransferRequestAdapter.Holder) holder).transferRequest_stockdesc.setText(sender_list.get(position).getReqStoreCode()+" "+sender_list.get(position).getReqStoreDesc());
                ((TransferRequestAdapter.Holder) holder).transferRequest_case.setText(sender_list.get(position).getCaseNo());
                ((TransferRequestAdapter.Holder) holder).transferRequest_reqty.setText("" + Math.round(sender_list.get(position).getStkOnhandQtyRequested()));
                ((TransferRequestAdapter.Holder) holder).transferRequest_avlqty.setText("" + Math.round(sender_list.get(position).getStkQtyAvl()));
                ((TransferRequestAdapter.Holder) holder).transferRequest_optreq.setText("" + Math.round(sender_list.get(position).getOptionCount()));
                ((TransferRequestAdapter.Holder) holder).transferRequest_days.setText("No of Days : "+sender_list.get(position).getNoOfDays());

                ((TransferRequestAdapter.Holder) holder).transferRequest_stockdesc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.onItemClick(view,position);
                    }
                });
                ((TransferRequestAdapter.Holder)holder).mcCheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(((CheckBox)view).isChecked())
                        {
                            selectMc[position] = true;
                            notifyItemChanged(position);
                        }else
                        {
                            selectMc[position] = false;
                            notifyItemChanged(position);
                        }
                    }
                });
                ((TransferRequestAdapter.Holder)holder).mcCheck.setChecked(selectMc[position]==true ? true :false);
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
        CheckBox mcCheck;

        public Holder(View itemView) {
            super(itemView);
           // transferRequest_stockCode = (TextView) itemView.findViewById(R.id.transferRequest_stockCode);
            transferRequest_case = (TextView) itemView.findViewById(R.id.transferRequest_case);
            transferRequest_stockdesc = (TextView) itemView.findViewById(R.id.transferRequest_stockDesc);
            transferRequest_avlqty = (TextView) itemView.findViewById(R.id.stock_avi);
            transferRequest_reqty = (TextView) itemView.findViewById(R.id.stock_sohRequested);
            transferRequest_optreq = (TextView) itemView.findViewById(R.id.stock_numberOfOption);
            transferRequest_days = (TextView) itemView.findViewById(R.id.transferRequest_days);
            transferRequest_days = (TextView) itemView.findViewById(R.id.transferRequest_days);
            mcCheck=(CheckBox)itemView.findViewById(R.id.mcCheck);

        }
    }


    public interface OnItemClickListener {

        public void onItemClick(View view, int position);

    }
}