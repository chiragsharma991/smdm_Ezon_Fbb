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

import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.ToDo_Modal;
import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by csuthar on 02/03/17.
 */

 public class StockPullAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>implements GravitySnapHelper.SnapListener
{

    private final Context context;
    private final ArrayList<ToDo_Modal> list;
    private static boolean check=false;


    public StockPullAdapter(ArrayList<ToDo_Modal> list, Context context) {
        this.list=list;
        this.context=context;//



    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_stockpullfragment_child, parent, false);
        return new StockPullAdapter.Holder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof StockPullAdapter.Holder) {
            if(position < list.size()) {

                // holder.snapTextView.setText(snap.getText());

                ((StockPullAdapter.Holder)holder).TransferStatus.setText(list.get(position).getTransferStatus());
                ((StockPullAdapter.Holder)holder).SOH_Requested.setText(""+Math.round(list.get(position).getStkOnhandQtyRequested()));
                ((StockPullAdapter.Holder)holder).QTY_Avi.setText(""+Math.round(list.get(position).getStkQtyAvl()));
                ((StockPullAdapter.Holder)holder).NumberOfOption.setText(""+list.get(position).getNoOfOptions());
                ((StockPullAdapter.Holder)holder).SOH.setText(""+list.get(position).getStkOnhandQty());
                ((StockPullAdapter.Holder)holder).GIT_Qty.setText(""+list.get(position).getStkGitQty());
                ((StockPullAdapter.Holder)holder).FWD.setText(""+String.format("%.1f",list.get(position).getFwdWeekCover()));
                ((StockPullAdapter.Holder)holder).McCodeDescribtion.setText(list.get(position).getMccodeDesc());



            }
        }



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onSnap(int position) {

    }



    private static class Holder extends RecyclerView.ViewHolder {


        TextView TransferStatus,SOH_Requested,QTY_Avi,NumberOfOption,SOH,GIT_Qty,FWD,McCodeDescribtion;
        CardView Stock_cardView;
        public Holder(View itemView) {
            super(itemView);
            TransferStatus=(TextView)itemView.findViewById(R.id.stock_transfer_status);
            SOH_Requested=(TextView)itemView.findViewById(R.id.stock_sohRequested);
            QTY_Avi=(TextView)itemView.findViewById(R.id.stock_avi);
            SOH=(TextView)itemView.findViewById(R.id.stock_soh);
            GIT_Qty=(TextView)itemView.findViewById(R.id.stock_git);
            FWD=(TextView)itemView.findViewById(R.id.stock_fwc);
            McCodeDescribtion=(TextView)itemView.findViewById(R.id.stock_McCodeDesc);
            NumberOfOption=(TextView)itemView.findViewById(R.id.stock_numberOfOption);
           // Stock_cardView=(CardView)itemView.findViewById(R.id.stockFragment_cardView);




        }

    }
}