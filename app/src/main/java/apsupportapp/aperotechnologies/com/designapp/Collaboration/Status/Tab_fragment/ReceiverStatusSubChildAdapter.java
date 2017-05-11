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

/**
 * Created by pamrutkar on 16/03/17.
 */
public class ReceiverStatusSubChildAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private  Context context;
    private  int Rec_PrePosition;
    private HashMap<Integer, ArrayList<StatusModel>> subchild_list;
    private  ReceiverStatusDetailsAdapter receiverStatusDetailsAdapter;

    public ReceiverStatusSubChildAdapter(HashMap<Integer, ArrayList<StatusModel>> statusHashmapChildList, Context context, int position, ReceiverStatusDetailsAdapter receiverStatusDetailsAdapter) {

        this.subchild_list=statusHashmapChildList;
        this.context=context;//
        Rec_PrePosition=position;
        this.receiverStatusDetailsAdapter=receiverStatusDetailsAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_status_receiver_details_subchild, parent, false);
        return new ReceiverStatusSubChildAdapter.Holder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        ((ReceiverStatusSubChildAdapter.Holder)holder).Rec_StatusSubchild_size.setText(subchild_list.get(Rec_PrePosition).get(position).getLevel());
        ((ReceiverStatusSubChildAdapter.Holder)holder).Rec_StatusSubchild_shortQty.setText(""+Math.round(subchild_list.get(Rec_PrePosition).get(position).getStkOnhandQtyRequested()));
        ((ReceiverStatusSubChildAdapter.Holder)holder).Rec_StatusSubchild_avlQty.setText(""+Math.round(subchild_list.get(Rec_PrePosition).get(position).getStkOnhandQtyAcpt()));
    }

    @Override
    public int getItemCount() {
        return subchild_list.get(Rec_PrePosition).size();
    }

    private static class Holder extends RecyclerView.ViewHolder {

       private TextView Rec_StatusSubchild_size,Rec_StatusSubchild_shortQty,Rec_StatusSubchild_avlQty;
        public Holder(View itemView)
        {
            super(itemView);
            Rec_StatusSubchild_size=(TextView)itemView.findViewById(R.id.status_receiver_subdetail_size);
            Rec_StatusSubchild_shortQty=(TextView)itemView.findViewById(R.id.status_receiver_subdetail_shortQtyVal);
            Rec_StatusSubchild_avlQty=(TextView)itemView.findViewById(R.id.status_receiver_subdetail_avlQtyVal);
        }
    }
}
