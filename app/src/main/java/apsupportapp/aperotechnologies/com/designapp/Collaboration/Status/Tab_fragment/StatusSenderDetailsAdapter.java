package apsupportapp.aperotechnologies.com.designapp.Collaboration.Status.Tab_fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment.StockDetailsAdapter;
import apsupportapp.aperotechnologies.com.designapp.R;


/**
 * Created by csuthar on 10/03/17.
 */

public class StatusSenderDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<StatusModel> list;
    private static boolean check=false;
    private String TAG="StatusSender_Fragment";





    public StatusSenderDetailsAdapter(ArrayList<StatusModel> list, Context context) {
        this.list=list;
        this.context=context;//

    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_status_sender_details_child, parent, false);
        return new StatusSenderDetailsAdapter.Holder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {

        if(holder instanceof StatusSenderDetailsAdapter.Holder) {
            if(position < list.size()) {

                Log.e(TAG, "Stock detail>>>>>>>: "+position+list.get(0).getLevel() );


                ((StatusSenderDetailsAdapter.Holder)holder).OptionLevel.setText(list.get(position).getLevel());
                ((StatusSenderDetailsAdapter.Holder)holder).ReqQty.setText(""+Math.round(list.get(position).getStkOnhandQtyRequested()));
                ((StatusSenderDetailsAdapter.Holder)holder).ScanQty.setText(""+Math.round(list.get(position).getStkOnhandQtyAcpt()));


           /*     StatusSenderChildDetails detailsHeaderChildAdapter=new StatusSenderChildDetails(Details.HashmapList,context,position,StockDetailsAdapter.this);
                ((StockDetailsAdapter.Holder)holder).detailsLinear.setAdapter(detailsHeaderChildAdapter);*/

            }
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        private  TextView OptionLevel,ReqQty,ScanQty  ;

        public Holder(View itemView) {
            super(itemView);
            OptionLevel=(TextView)itemView.findViewById(R.id.statusSender_detail_optionLevel);
            ReqQty=(TextView)itemView.findViewById(R.id.status_sender_detail_reqQty);
            ScanQty=(TextView)itemView.findViewById(R.id.status_sender_detail_ScanedQty);







        }

    }
}
