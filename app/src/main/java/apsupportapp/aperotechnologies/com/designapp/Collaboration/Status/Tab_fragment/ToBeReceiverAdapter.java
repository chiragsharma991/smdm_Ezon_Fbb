package apsupportapp.aperotechnologies.com.designapp.Collaboration.Status.Tab_fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.ArrayList;
import java.util.HashMap;

import apsupportapp.aperotechnologies.com.designapp.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static apsupportapp.aperotechnologies.com.designapp.RunningPromo.VM.list;

/**
 * Created by pamrutkar on 15/03/17.
 */
public class ToBeReceiverAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>implements GravitySnapHelper.SnapListener,View.OnClickListener
{
    Context context;
    private  ArrayList<StatusModel> receiver_list;
    private static boolean check=false;
    private HashMap<Integer, ArrayList<StatusModel>> receiverada_statusList;
    private boolean[] Receiver_Toggle;



    public ToBeReceiverAdapter(HashMap<Integer, ArrayList<StatusModel>> receiver_statusList, ArrayList<StatusModel> receiverSummaryList, Context context) {
    this.receiverada_statusList = receiver_statusList;
        this.receiver_list = receiverSummaryList;
        this.context = context;
        Receiver_Toggle= new boolean[receiver_list.size()];

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_to_be_receiver_adapter, parent, false);
        return new ToBeReceiverAdapter.Holder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ToBeReceiverAdapter.Holder) {
            if(position < receiver_list.size())
            {

                HandlePositionOnSet(holder,position);
                ((ToBeReceiverAdapter.Holder)holder).Rec_Status_caseNumber.setText(""+(int)receiver_list.get(position).getCaseNo());
                ((ToBeReceiverAdapter.Holder)holder).Rec_Status_storeCode.setText(receiver_list.get(position).getReqStoreCode());

                String Rec_StatusInitiated=receiver_list.get(position).getStatusInitiated();
                String Rec_StatusAccept=receiver_list.get(position).getStatusAccept();
                String Rec_StatusSto=receiver_list.get(position).getStatusSto();
                String Rec_StatusGrn=receiver_list.get(position).getStatusGrn();

                ((ToBeReceiverAdapter.Holder) holder).Rec_Lin_trCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("TAG", "onClick: on Linear layout >>>>>>>> " );
                        new ToBeReceiverDetails().StartActivity(context,receiver_list.get(position).getCaseNo(),receiver_list.get(position).getReqStoreCode());

                    }
                });


                if(Rec_StatusInitiated.equals("Yes") && Rec_StatusAccept.equals("No") && Rec_StatusSto.equals("No") && Rec_StatusGrn.equals("No"))
                {
                    Rec_Case0(position,holder);

                }else if(Rec_StatusInitiated.equals("Yes") && Rec_StatusAccept.equals("Yes") && Rec_StatusSto.equals("No") && Rec_StatusGrn.equals("No"))
                {
                    Rec_Case1(position,holder);

                }else if(Rec_StatusInitiated.equals("Yes") && Rec_StatusAccept.equals("Yes") && Rec_StatusSto.equals("Yes") && Rec_StatusGrn.equals("No"))
                {
                    Rec_Case2(position,holder);

                }else if(Rec_StatusInitiated.equals("Yes") && Rec_StatusAccept.equals("Yes") && Rec_StatusSto.equals("Yes") && Rec_StatusGrn.equals("Yes"))
                {
                    Rec_Case3(position,holder);

                }
            }
        }
    }

    private void Rec_Case0(final int position, RecyclerView.ViewHolder holder) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup view = (ViewGroup) layoutInflater.inflate(R.layout.activity_receiver_status_track_one, null);
        LinearLayout Initiated=(LinearLayout)view.findViewById(R.id.rec_status_track_position_Initiated);
        Initiated.setTag(position); //we are using set tag for list view's position
        LinearLayout SenderAcpt=(LinearLayout)view.findViewById(R.id.rec_status_track_position_SenderAcpt);
        SenderAcpt.setTag(position);
        LinearLayout STO=(LinearLayout)view.findViewById(R.id.rec_status_track_position_STO);
        STO.setTag(position);
        LinearLayout GRN=(LinearLayout)view.findViewById(R.id.rec_status_track_position_GRN);
        GRN.setTag(position);
        Initiated.setOnClickListener(this);
        SenderAcpt.setOnClickListener(this);
        STO.setOnClickListener(this);
        GRN.setOnClickListener(this);
        ((ToBeReceiverAdapter.Holder) holder).Lin_ProcessStatus.addView(view);

    }
    private void Rec_Case1(final int position, RecyclerView.ViewHolder holder) {

        LayoutInflater layoutInflater1 = (LayoutInflater) context.getApplicationContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup view = (ViewGroup) layoutInflater1.inflate(R.layout.activity_receiver_status_track_two, null);
        LinearLayout Initiated=(LinearLayout)view.findViewById(R.id.rec_status_track_position_Initiated);
        Initiated.setTag(position); //we are using set tag for list view's position
        LinearLayout SenderAcpt=(LinearLayout)view.findViewById(R.id.rec_status_track_position_SenderAcpt);
        SenderAcpt.setTag(position);
        LinearLayout STO=(LinearLayout)view.findViewById(R.id.rec_status_track_position_STO);
        STO.setTag(position);
        LinearLayout GRN=(LinearLayout)view.findViewById(R.id.rec_status_track_position_GRN);
        GRN.setTag(position);
        Initiated.setOnClickListener(this);
        SenderAcpt.setOnClickListener(this);
        STO.setOnClickListener(this);
        GRN.setOnClickListener(this);
        ((ToBeReceiverAdapter.Holder) holder).Lin_ProcessStatus.addView(view);
    }

    private void Rec_Case2(final int position, RecyclerView.ViewHolder holder) {

        LayoutInflater layoutInflater2 = (LayoutInflater) context.getApplicationContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup view = (ViewGroup) layoutInflater2.inflate(R.layout.activity_receiver_status_track_three, null);
        LinearLayout Initiated=(LinearLayout)view.findViewById(R.id.rec_status_track_position_Initiated);
        Initiated.setTag(position); //we are using set tag for list view's position
        LinearLayout SenderAcpt=(LinearLayout)view.findViewById(R.id.rec_status_track_position_SenderAcpt);
        SenderAcpt.setTag(position);
        LinearLayout STO=(LinearLayout)view.findViewById(R.id.rec_status_track_position_STO);
        STO.setTag(position);
        LinearLayout GRN=(LinearLayout)view.findViewById(R.id.rec_status_track_position_GRN);
        GRN.setTag(position);
        Initiated.setOnClickListener(this);
        SenderAcpt.setOnClickListener(this);
        STO.setOnClickListener(this);
        GRN.setOnClickListener(this);
        ((ToBeReceiverAdapter.Holder) holder).Lin_ProcessStatus.addView(view);
    }

    private void Rec_Case3(final int position, RecyclerView.ViewHolder holder) {

        LayoutInflater layoutInflater3 = (LayoutInflater) context.getApplicationContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup view = (ViewGroup) layoutInflater3.inflate(R.layout.activity_receiver_status_track_four, null);
        LinearLayout Initiated=(LinearLayout)view.findViewById(R.id.rec_status_track_position_Initiated);
        Initiated.setTag(position); //we are using set tag for list view's position
        LinearLayout SenderAcpt=(LinearLayout)view.findViewById(R.id.rec_status_track_position_SenderAcpt);
        SenderAcpt.setTag(position);
        LinearLayout STO=(LinearLayout)view.findViewById(R.id.status_track_position_STO);
        STO.setTag(position);
        LinearLayout GRN=(LinearLayout)view.findViewById(R.id.rec_status_track_position_GRN);
        GRN.setTag(position);
        Initiated.setOnClickListener(this);
        SenderAcpt.setOnClickListener(this);
        STO.setOnClickListener(this);
        GRN.setOnClickListener(this);
        ((ToBeReceiverAdapter.Holder) holder).Lin_ProcessStatus.addView(view);
    }

    private void HandlePositionOnSet(RecyclerView.ViewHolder holder, int position) {

        if(Receiver_Toggle[position])
        {
            LayoutInflater layoutInflater1 = (LayoutInflater) context.getApplicationContext()
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup view = (ViewGroup) layoutInflater1.inflate(R.layout.activity_rec_status_initiate, null);
            TextView Rec_Status_docNumber=(TextView)view.findViewById(R.id.rec_status_docNumber);
            TextView Rec_Status_qty=(TextView)view.findViewById(R.id.rec_status_qty);
            TextView Rec_Status_date=(TextView)view.findViewById(R.id.rec_status_date);
            if(receiverada_statusList.get(position).isEmpty()){
                Rec_Status_docNumber.setText("N/A");
                Rec_Status_qty.setText("N/A");
                Rec_Status_date.setText("N/A");
            }else
            {
                Rec_Status_docNumber.setText(""+receiverada_statusList.get(position).get(0).getDocNo());
                Rec_Status_qty.setText(""+Math.round(receiverada_statusList.get(position).get(0).getStkOnhandQtyRequested()));
                Rec_Status_date.setText(receiverada_statusList.get(position).get(0).getReceiver_requested_date());
            }
            ((ToBeReceiverAdapter.Holder) holder).Rec_Status_layout.addView(view);
            ((ToBeReceiverAdapter.Holder)holder).Rec_Status_layout.setVisibility(View.VISIBLE);
        }else
        {
            ((ToBeReceiverAdapter.Holder)holder).Rec_Status_layout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return receiver_list.size();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSnap(int position) {

    }

    private static class Holder extends RecyclerView.ViewHolder {

        LinearLayout Lin_ProcessStatus,Rec_Lin_trCard;
        RelativeLayout Rec_Status_layout;
        TextView Rec_Status_caseNumber,Rec_Status_case,Rec_Status_storeCode,Rec_Status_storedesc,Rec_Status_docNumber,Rec_Status_qty,Rec_Status_date;
        public Holder(View itemView)
        {
            super(itemView);
            Lin_ProcessStatus=(LinearLayout)itemView.findViewById(R.id.lin_progressView);
            Rec_Status_caseNumber=(TextView)itemView.findViewById(R.id.receiver_status_caseNumber);
            Rec_Status_case=(TextView)itemView.findViewById(R.id.receiver_status_case);
            Rec_Status_storeCode=(TextView)itemView.findViewById(R.id.receiver_status_storeCode);
            Rec_Status_storedesc=(TextView)itemView.findViewById(R.id.receiver_status_storedesc);
            Rec_Status_docNumber=(TextView)itemView.findViewById(R.id.rec_status_docNumber);
            Rec_Status_qty=(TextView)itemView.findViewById(R.id.rec_status_qty);
            Rec_Status_date=(TextView)itemView.findViewById(R.id.rec_status_date);
            Rec_Lin_trCard=(LinearLayout)itemView.findViewById(R.id.linear_receiver);
            Rec_Status_layout=(RelativeLayout) itemView.findViewById(R.id.rel_status_layout);

        }

    }

}
