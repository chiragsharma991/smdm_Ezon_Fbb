package apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.ToDo_Modal;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.RecyclerItemClickListener;

/**
 * Created by csuthar on 06/03/17.
 */

public class StockDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private  Context context;
    private  ArrayList<ToDo_Modal> list;
    private static boolean check=false;
    public static boolean[] Toggle;
    public static boolean[] HeadercheckList;
    public OnPress onPressInterface;



    public StockDetailsAdapter(ArrayList<ToDo_Modal> list, Context context) {
        this.list=list;
        this.context=context;//
        Toggle= new boolean[list.size()];
        HeadercheckList= new boolean[list.size()];
        onPressInterface=(OnPress)context;

   }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_stock_details_child, parent, false);
        return new StockDetailsAdapter.Holder(v);    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        Log.e("TAG", "Stock detail: "+position );

        if(holder instanceof StockDetailsAdapter.Holder) {
            if(position < list.size()) {

                HandlePositionOnSet(holder,position);

                ((StockDetailsAdapter.Holder)holder).Detail_Soh.setText(""+Math.round(list.get(position).getStkOnhandQty()));
                ((StockDetailsAdapter.Holder)holder).Detail_optionLevel.setText(list.get(position).getLevel());
                ((StockDetailsAdapter.Holder)holder).Detail_reqQty.setText(""+Math.round(list.get(position).getStkOnhandQtyRequested()));
                ((StockDetailsAdapter.Holder)holder).Detail_Git.setText(""+Math.round(list.get(position).getStkGitQty()));
                ((StockDetailsAdapter.Holder)holder).Detail_AviQty.setText(""+Math.round(list.get(position).getStkQtyAvl()));
                ((Holder)holder).Detail_headerCheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                            if(((CheckBox)view).isChecked())
                            {
                                //Header check is enable when view is open
                                if(Toggle[position]){
                                    HeadercheckList[position]=true;
                                    SetChangeInChild(position);

                                }else{
                                    HeadercheckList[position]=false;

                                }
                                notifyItemChanged(position);
                            }else
                            {
                                HeadercheckList[position]=false;
                                SetUncheckInChild(position);
                               // notifyItemChanged(position);
                            }
                        }


                });
                ((StockDetailsAdapter.Holder)holder).Detail_optionLevel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("TAG", "onClick:>>>> "+position );
                        if(Toggle[position]==true)
                        {
                            Toggle[position]=false;
                            notifyDataSetChanged();

                        }else
                        {
                            Toggle[position]=true;
                            if(Details.HashmapList.get(position).isEmpty())
                            {
                                onPressInterface.OnPress(position);

                            }else
                            {
                                notifyDataSetChanged();

                            }

                        }


                    }
                });
                DetailsHeaderChildAdapter detailsHeaderChildAdapter=new DetailsHeaderChildAdapter(Details.HashmapList,context,position,StockDetailsAdapter.this);
                ((StockDetailsAdapter.Holder)holder).detailsLinear.setAdapter(detailsHeaderChildAdapter);

            }
        }
    }

    private void SetUncheckInChild(int position) {

        for (int i = 0; i <Details.HashmapList.get(position).size(); i++)
        {
            Pair<Integer, Integer> Tag = new Pair<Integer, Integer>(position,i);
            if(DetailsHeaderChildAdapter.CheckedItems.contains(Tag))
            {
                DetailsHeaderChildAdapter.CheckedItems.remove(Tag);
            }

        }
        notifyItemChanged(position);

    }

    private void SetChangeInChild(int position) {
        for (int i = 0; i <Details.HashmapList.get(position).size(); i++)
        {
            Pair<Integer, Integer> Tag = new Pair<Integer, Integer>(position,i);
            if(!DetailsHeaderChildAdapter.CheckedItems.contains(Tag))
            {
                DetailsHeaderChildAdapter.CheckedItems.add(Tag);
            }

        }
        notifyItemChanged(position);
    }

    private void HandlePositionOnSet(RecyclerView.ViewHolder holder, int position)
    {
        if(Toggle[position])
        {
            ((StockDetailsAdapter.Holder)holder).Sizeslayout.setVisibility(View.VISIBLE);

        }else
        {
            ((StockDetailsAdapter.Holder)holder).Sizeslayout.setVisibility(View.GONE);

        }
        if(HeadercheckList[position])
        {
            ((StockDetailsAdapter.Holder)holder).Detail_headerCheck.setChecked(true);

        }else
        {
            ((StockDetailsAdapter.Holder)holder).Detail_headerCheck.setChecked(false);

        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        private final TextView Detail_Soh,Detail_reqQty,Detail_Git,Detail_AviQty;
        private TextView Detail_optionLevel;
        private LinearLayout Sizeslayout;
        private CheckBox Detail_headerCheck;
        protected RecyclerView detailsLinear;
        public Holder(View itemView) {
            super(itemView);
            Detail_optionLevel=(TextView)itemView.findViewById(R.id.detail_optionLevel);
            Detail_reqQty=(TextView)itemView.findViewById(R.id.detail_reqQty);
            Detail_Soh=(TextView)itemView.findViewById(R.id.detail_Soh);
            Detail_Git=(TextView)itemView.findViewById(R.id.detail_Git);
            Detail_AviQty=(TextView)itemView.findViewById(R.id.detail_AviQty);
            Sizeslayout=(LinearLayout)itemView.findViewById(R.id.detail_size);
            detailsLinear=(RecyclerView)itemView.findViewById(R.id.details_headerChild);
            Detail_headerCheck=(CheckBox) itemView.findViewById(R.id.detail_headerCheck);






        }

    }


}
