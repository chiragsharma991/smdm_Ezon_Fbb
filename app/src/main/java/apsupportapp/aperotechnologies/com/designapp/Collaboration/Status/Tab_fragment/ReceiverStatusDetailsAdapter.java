package apsupportapp.aperotechnologies.com.designapp.Collaboration.Status.Tab_fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
 * Created by pamrutkar on 15/03/17.
 */
public class ReceiverStatusDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ProgressBar receiverDetailProcess;
    private Context context;
    private ArrayList<StatusModel> rec_status_dtllist;
    private static boolean check=false;
    private String TAG="ReceiverStatus_Detail_Fragment";
    private static boolean[] Rec_Toggle;
    public OnPress onPressInterface;




    public ReceiverStatusDetailsAdapter(ArrayList<StatusModel> rec_status_List, Context context, ProgressBar receiverDetailProcess) {
        this.context = context;
        this.rec_status_dtllist = rec_status_List;
        Rec_Toggle = new boolean[rec_status_dtllist.size()];
        this.rec_status_dtllist = rec_status_List;
        onPressInterface=(OnPress)context;
        this.receiverDetailProcess=receiverDetailProcess;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_status_receiver_details_child1, parent, false);
        return new ReceiverStatusDetailsAdapter.Holder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ReceiverStatusDetailsAdapter.Holder) {
            if(position < rec_status_dtllist.size()) {

                Log.e(TAG,"position"+position);
                HandlePositionOnSet(holder,position);
               // HandlePositionOnSet(holder,position);
                ((ReceiverStatusDetailsAdapter.Holder)holder).OptionLevel.setText(rec_status_dtllist.get(position).getLevel());
                ((ReceiverStatusDetailsAdapter.Holder)holder).ShortQty.setText(""+Math.round(rec_status_dtllist.get(position).getStkOnhandQtyRequested()));
                ((ReceiverStatusDetailsAdapter.Holder)holder).AvlQty.setText(""+Math.round(rec_status_dtllist.get(position).getStkOnhandQtyAcpt()));
                ((ReceiverStatusDetailsAdapter.Holder)holder).OptionLevel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(Rec_Toggle[position]==true)
                        {
                            Rec_Toggle[position]=false;
                            notifyDataSetChanged();

                        }else
                        {
                            Rec_Toggle[position]=true;

                            if(ToBeReceiverDetails.Rec_StatusHashmapChildList.get(position).isEmpty())
                            {
                                if(receiverDetailProcess.getVisibility()==View.GONE)
                                {
                                    receiverDetailProcess.setVisibility(View.VISIBLE);
                                    onPressInterface.OnPress(position);
                                }

                            }
                            else
                            {
                                notifyDataSetChanged();

                            }
                        }
                    }
                });

               ReceiverStatusSubChildAdapter detailsHeaderChildAdapter=new ReceiverStatusSubChildAdapter(ToBeReceiverDetails.Rec_StatusHashmapChildList,context,position,ReceiverStatusDetailsAdapter.this);
                ((ReceiverStatusDetailsAdapter.Holder)holder).receiverdetails_SubChild.setAdapter(detailsHeaderChildAdapter);

            }
        }
    }
    private void HandlePositionOnSet(RecyclerView.ViewHolder holder, int position) {
        if(Rec_Toggle[position])
        {
            ((ReceiverStatusDetailsAdapter.Holder)holder).lin_receiver_dtlsubchild.setVisibility(View.VISIBLE);

        }else
        {
            ((ReceiverStatusDetailsAdapter.Holder)holder).lin_receiver_dtlsubchild.setVisibility(View.GONE);

        }

    }

    @Override
    public int getItemCount() {
        return rec_status_dtllist.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        private TextView OptionLevel,ShortQty,AvlQty  ;
        private LinearLayout lin_receiver_dtlsubchild;
        private RecyclerView receiverdetails_SubChild;
        public Holder(View itemView)
        {
            super(itemView);
            OptionLevel=(TextView)itemView.findViewById(R.id.status_receiver_detail_optionLevel);
            ShortQty=(TextView)itemView.findViewById(R.id.status_receiver_detail_shortQty);
            AvlQty=(TextView)itemView.findViewById(R.id.status_receiver_detail_avlQty);
            lin_receiver_dtlsubchild=(LinearLayout)itemView.findViewById(R.id.lin_receiver_dtlsubchild);
            receiverdetails_SubChild=(RecyclerView)itemView.findViewById(R.id.receiverdetails_SubChild);
        }

    }


}
