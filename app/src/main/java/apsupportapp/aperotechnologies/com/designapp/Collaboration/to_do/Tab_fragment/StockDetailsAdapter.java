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
 * Created by csuthar on 06/03/17.
 */

public class StockDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final Context context;
    private final ArrayList<ToDo_Modal> list;
    private static boolean check=false;


    public StockDetailsAdapter(ArrayList<ToDo_Modal> list, Context context) {
        this.list=list;
        this.context=context;//



    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_stock_details_child, parent, false);
        return new StockDetailsAdapter.Holder(v);    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof StockDetailsAdapter.Holder) {
            if(position < list.size()) {

                // holder.snapTextView.setText(snap.getText());

                ((StockDetailsAdapter.Holder)holder).Detail_Soh.setText(""+(int)list.get(position).getStkOnhandQty());
                ((StockDetailsAdapter.Holder)holder).Detail_optionLevel.setText(list.get(position).getLevel());
                ((StockDetailsAdapter.Holder)holder).Detail_reqQty.setText(""+list.get(position).getStkOnhandQtyRequested());
                ((StockDetailsAdapter.Holder)holder).Detail_Git.setText(""+(int)list.get(position).getStkGitQty());
                ((StockDetailsAdapter.Holder)holder).Detail_AviQty.setText(""+list.get(position).getStkQtyAvl());




            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private static class Holder extends RecyclerView.ViewHolder {


        private final TextView Detail_Soh,Detail_optionLevel,Detail_reqQty,Detail_Git,Detail_AviQty;
        TextView TransferStatus,SOH_Requested,QTY_Avi,NumberOfOption,SOH,GIT_Qty,FWD,McCodeDescribtion;
        public Holder(View itemView) {
            super(itemView);
            Detail_optionLevel=(TextView)itemView.findViewById(R.id.detail_optionLevel);
            Detail_reqQty=(TextView)itemView.findViewById(R.id.detail_reqQty);
            Detail_Soh=(TextView)itemView.findViewById(R.id.detail_Soh);
            Detail_Git=(TextView)itemView.findViewById(R.id.detail_Git);
            Detail_AviQty=(TextView)itemView.findViewById(R.id.detail_AviQty);





        }

    }


}
