package apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.ToDo_Modal;
import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Transfer_Request_Model;
import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by pamrutkar on 09/03/17.
 */
public class TransferDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private ArrayList<Transfer_Request_Model> list;
    private static boolean check=false;
    public static boolean[] Toggle;
    public static boolean[] HeadercheckList;
    public OnPress onPressInterface;



    public TransferDetailsAdapter(ArrayList<Transfer_Request_Model> list, Context context) {
        this.list=list;
        this.context=context;//
        Toggle= new boolean[list.size()];
        HeadercheckList= new boolean[list.size()];
        onPressInterface=(OnPress)context;

    }

   @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_transferreq_details_child, parent, false);
        return new TransferDetailsAdapter.Holder(v);    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        Log.e("TAG", "Stock detail: "+position );

        if(holder instanceof TransferDetailsAdapter.Holder) {
            if(position < list.size()) {



                ((TransferDetailsAdapter.Holder)holder).txt_optionval.setText(list.get(position).getLevel());
                ((TransferDetailsAdapter.Holder)holder).txt_reqtyval.setText(""+Math.round(list.get(position).getStkOnhandQtyRequested()));
                ((TransferDetailsAdapter.Holder)holder).txt_avlqtyval.setText(""+Math.round(list.get(position).getStkQtyAvl()));
                ((TransferDetailsAdapter.Holder)holder).txt_sohval.setText(""+Math.round(list.get(position).getStkOnhandQty()));
                ((TransferDetailsAdapter.Holder)holder).txt_gitval.setText(""+Math.round(list.get(position).getStkGitQty()));
      //          ((TransferDetailsAdapter.Holder)holder).txt_scanqtyVal.setText(0);

//                DetailsHeaderChildAdapter detailsHeaderChildAdapter=new DetailsHeaderChildAdapter(Details.HashmapList,context,position,StockDetailsAdapter.this);
//                ((StockDetailsAdapter.Holder)holder).detailsLinear.setAdapter(detailsHeaderChildAdapter);

            }
        }
    }




    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        private TextView txt_caseNo,txt_optionval,txt_reqtyval,txt_avlqtyval,txt_sohval,txt_gitval,txt_scanqtyVal;


        public Holder(View itemView) {
            super(itemView);
            txt_caseNo = (TextView)itemView.findViewById(R.id.txt_caseNo);
            txt_optionval = (TextView)itemView.findViewById(R.id.txt_optionVal);
            txt_reqtyval = (TextView)itemView.findViewById(R.id.txt_reqtyVal);
            txt_avlqtyval = (TextView)itemView.findViewById(R.id.txt_avlqtyVal);
            txt_sohval = (TextView)itemView.findViewById(R.id.txt_sohVal);
            txt_gitval = (TextView)itemView.findViewById(R.id.txt_gitVal);
            txt_scanqtyVal= (TextView)itemView.findViewById(R.id.txt_scanqtyVal);






        }

    }


}
