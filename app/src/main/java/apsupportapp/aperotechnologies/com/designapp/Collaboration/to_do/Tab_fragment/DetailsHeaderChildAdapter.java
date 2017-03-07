package apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.ToDo_Modal;
import apsupportapp.aperotechnologies.com.designapp.R;



public class DetailsHeaderChildAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final Context context;
    private final int PrePosition;
    private final HashMap<Integer, ArrayList<ToDo_Modal>> list;



    public DetailsHeaderChildAdapter(HashMap<Integer, ArrayList<ToDo_Modal>> list, Context context, int position) {
        this.list=list;
        this.context=context;//
        PrePosition=position;

    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.details_header_child, parent, false);
        return new DetailsHeaderChildAdapter.Holder(v);    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        Log.e("TAG", "On Detail child: "+position );

        ((DetailsHeaderChildAdapter.Holder)holder).DetailChild_size.setText(list.get(PrePosition).get(position).getLevel());
        ((DetailsHeaderChildAdapter.Holder)holder).DetailChild_requiredQty.setText(""+list.get(PrePosition).get(position).getStkOnhandQtyRequested());
        ((DetailsHeaderChildAdapter.Holder)holder).DetailChild_aviQty.setText(""+list.get(PrePosition).get(position).getStkQtyAvl());


    }


    @Override
    public int getItemCount() {
        return list.get(PrePosition).size();
    }

    private static class Holder extends RecyclerView.ViewHolder {


        private final TextView DetailChild_size,DetailChild_requiredQty,DetailChild_aviQty;
        private CheckBox DetailChild_checkBox;

        public Holder(View itemView) {
            super(itemView);
            DetailChild_size=(TextView)itemView.findViewById(R.id.detailChild_size);
            DetailChild_requiredQty=(TextView)itemView.findViewById(R.id.detailChild_requiredQty);
            DetailChild_aviQty=(TextView)itemView.findViewById(R.id.detailChild_aviQty);
            DetailChild_checkBox=(CheckBox) itemView.findViewById(R.id.detailChild_checkBox);

        }

    }


}
