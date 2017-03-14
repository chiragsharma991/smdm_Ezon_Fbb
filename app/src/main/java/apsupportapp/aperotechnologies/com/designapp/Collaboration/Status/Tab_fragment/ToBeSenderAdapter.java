package apsupportapp.aperotechnologies.com.designapp.Collaboration.Status.Tab_fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by csuthar on 02/03/17.
 */
public class ToBeSenderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>implements GravitySnapHelper.SnapListener,View.OnClickListener
{

    private final Context context;
    private final ArrayList<StatusModel> list;
    private static boolean check=false;


    public ToBeSenderAdapter(ArrayList<StatusModel> list, Context context) {
        this.list=list;
        this.context=context;



    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_be_received_child, parent, false);
        return new ToBeSenderAdapter.Holder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof ToBeSenderAdapter.Holder) {
            if(position < list.size())
            {
                ((ToBeSenderAdapter.Holder)holder).Status_caseNumber.setText(""+(int)list.get(position).getCaseNo());
                ((ToBeSenderAdapter.Holder)holder).Status_storeCode.setText(list.get(position).getReqStoreCode());
//                ((ToBeSenderAdapter.Holder)holder).Status_storedesc.setText(list.get(position).getCaseNo());
//                ((ToBeSenderAdapter.Holder)holder).Status_docNumber.setText(list.get(position).getCaseNo());
//                ((ToBeSenderAdapter.Holder)holder).Status_qty.setText(list.get(position).getCaseNo());
//                ((ToBeSenderAdapter.Holder)holder).Status_date.setText(list.get(position).getCaseNo());
                String StatusInitiated=list.get(position).getStatusInitiated();
                String StatusAccept=list.get(position).getStatusAccept();
                String StatusSto=list.get(position).getStatusSto();
                String StatusGrn=list.get(position).getStatusGrn();
//                ((Holder) holder).ProcessStatus.removeAllViewsInLayout();
                ((Holder) holder).Lin_trCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("TAG", "onClick: on Linear layout >>>>>>>> " );
                        new ToBeSenderDetails().StartActivity(context,list.get(position).getCaseNo(),list.get(position).getReqStoreCode());

                    }
                });


                if(StatusInitiated.equals("Yes")&&StatusAccept.equals("No")&&StatusSto.equals("No")&&StatusGrn.equals("No"))
                {
                    Case0(position,holder);

                }else if(StatusInitiated.equals("Yes")&&StatusAccept.equals("Yes")&&StatusSto.equals("No")&&StatusGrn.equals("No"))
                {
                    Case1(position,holder);

                }else if(StatusInitiated.equals("Yes")&&StatusAccept.equals("Yes")&&StatusSto.equals("Yes")&&StatusGrn.equals("No"))
                {
                    Case2(position,holder);

                }else if(StatusInitiated.equals("Yes")&&StatusAccept.equals("Yes")&&StatusSto.equals("Yes")&&StatusGrn.equals("Yes"))
                {
                    Case3(position,holder);

                }




            }




        }







    }

    private void Case1(final int position, RecyclerView.ViewHolder holder) {

        LayoutInflater layoutInflater1 = (LayoutInflater) context.getApplicationContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup view = (ViewGroup) layoutInflater1.inflate(R.layout.activity_status_track_two, null);
        LinearLayout Initiated=(LinearLayout)view.findViewById(R.id.status_track_position_Initiated);
        Initiated.setTag(position); //we are using set tag for list view's position
        LinearLayout SenderAcpt=(LinearLayout)view.findViewById(R.id.status_track_position_SenderAcpt);
        SenderAcpt.setTag(position);
        LinearLayout STO=(LinearLayout)view.findViewById(R.id.status_track_position_STO);
        STO.setTag(position);
        LinearLayout GRN=(LinearLayout)view.findViewById(R.id.status_track_position_GRN);
        GRN.setTag(position);
        Initiated.setOnClickListener(this);
        SenderAcpt.setOnClickListener(this);
        STO.setOnClickListener(this);
        GRN.setOnClickListener(this);
        ((ToBeSenderAdapter.Holder) holder).ProcessStatus.addView(view);
    }

    private void Case2(final int position, RecyclerView.ViewHolder holder) {

        LayoutInflater layoutInflater1 = (LayoutInflater) context.getApplicationContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup view = (ViewGroup) layoutInflater1.inflate(R.layout.activity_status_track_three, null);
        LinearLayout Initiated=(LinearLayout)view.findViewById(R.id.status_track_position_Initiated);
        Initiated.setTag(position); //we are using set tag for list view's position
        LinearLayout SenderAcpt=(LinearLayout)view.findViewById(R.id.status_track_position_SenderAcpt);
        SenderAcpt.setTag(position);
        LinearLayout STO=(LinearLayout)view.findViewById(R.id.status_track_position_STO);
        STO.setTag(position);
        LinearLayout GRN=(LinearLayout)view.findViewById(R.id.status_track_position_GRN);
        GRN.setTag(position);
        Initiated.setOnClickListener(this);
        SenderAcpt.setOnClickListener(this);
        STO.setOnClickListener(this);
        GRN.setOnClickListener(this);
        ((ToBeSenderAdapter.Holder) holder).ProcessStatus.addView(view);
    }

    private void Case3(final int position, RecyclerView.ViewHolder holder) {

        LayoutInflater layoutInflater1 = (LayoutInflater) context.getApplicationContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup view = (ViewGroup) layoutInflater1.inflate(R.layout.activity_status_track_four, null);
        LinearLayout Initiated=(LinearLayout)view.findViewById(R.id.status_track_position_Initiated);
        Initiated.setTag(position); //we are using set tag for list view's position
        LinearLayout SenderAcpt=(LinearLayout)view.findViewById(R.id.status_track_position_SenderAcpt);
        SenderAcpt.setTag(position);
        LinearLayout STO=(LinearLayout)view.findViewById(R.id.status_track_position_STO);
        STO.setTag(position);
        LinearLayout GRN=(LinearLayout)view.findViewById(R.id.status_track_position_GRN);
        GRN.setTag(position);
        Initiated.setOnClickListener(this);
        SenderAcpt.setOnClickListener(this);
        STO.setOnClickListener(this);
        GRN.setOnClickListener(this);
        ((ToBeSenderAdapter.Holder) holder).ProcessStatus.addView(view);
    }

    private void Case0(final int position, RecyclerView.ViewHolder holder) {

        LayoutInflater layoutInflater1 = (LayoutInflater) context.getApplicationContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup view = (ViewGroup) layoutInflater1.inflate(R.layout.activity_status_track_one, null);
        LinearLayout Initiated=(LinearLayout)view.findViewById(R.id.status_track_position_Initiated);
        Initiated.setTag(position); //we are using set tag for list view's position
        LinearLayout SenderAcpt=(LinearLayout)view.findViewById(R.id.status_track_position_SenderAcpt);
        SenderAcpt.setTag(position);
        LinearLayout STO=(LinearLayout)view.findViewById(R.id.status_track_position_STO);
        STO.setTag(position);
        LinearLayout GRN=(LinearLayout)view.findViewById(R.id.status_track_position_GRN);
        GRN.setTag(position);
        Initiated.setOnClickListener(this);
        SenderAcpt.setOnClickListener(this);
        STO.setOnClickListener(this);
        GRN.setOnClickListener(this);
        ((ToBeSenderAdapter.Holder) holder).ProcessStatus.addView(view);

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onSnap(int position) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.status_track_position_Initiated:
                int dublicatePosition=(int)view.getTag();

                Log.e("TAG", "onClick: Initiated one ------- "+dublicatePosition );
                break;
            case R.id.status_track_position_SenderAcpt:
                dublicatePosition = (int) view.getTag();

                Log.e("TAG", "onClick: SenderAcpt two ------- "+dublicatePosition );
                break;
            case R.id.status_track_position_STO:
                dublicatePosition=(int)view.getTag();

                Log.e("TAG", "onClick: STO three ------- "+dublicatePosition );
                break;
            case R.id.status_track_position_GRN:
                dublicatePosition=(int)view.getTag();
                Log.e("TAG", "onClick: GRN  four ------- "+dublicatePosition );
                break;

        }

    }


    private static class Holder extends RecyclerView.ViewHolder {

        LinearLayout ProcessStatus,Lin_trCard;
        TextView Status_caseNumber,Status_case,Status_storeCode,Status_storedesc,Status_docNumber,Status_qty,Status_date;
        public Holder(View itemView) {
            super(itemView);
            ProcessStatus=(LinearLayout)itemView.findViewById(R.id.progressView);

            Status_caseNumber=(TextView)itemView.findViewById(R.id.status_caseNumber);
            Status_case=(TextView)itemView.findViewById(R.id.status_case);
            Status_storeCode=(TextView)itemView.findViewById(R.id.status_storeCode);
            Status_storedesc=(TextView)itemView.findViewById(R.id.status_storedesc);
            Status_docNumber=(TextView)itemView.findViewById(R.id.status_docNumber);
            Status_qty=(TextView)itemView.findViewById(R.id.status_qty);
            Status_date=(TextView)itemView.findViewById(R.id.status_date);
            Lin_trCard=(LinearLayout)itemView.findViewById(R.id.linearClick);



        }

    }
}