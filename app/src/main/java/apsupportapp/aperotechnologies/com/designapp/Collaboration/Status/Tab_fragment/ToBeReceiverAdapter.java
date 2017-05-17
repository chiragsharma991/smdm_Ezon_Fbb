package apsupportapp.aperotechnologies.com.designapp.Collaboration.Status.Tab_fragment;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.ArrayList;
import java.util.HashMap;

import apsupportapp.aperotechnologies.com.designapp.Collaboration.Status.StatusActivity;
import apsupportapp.aperotechnologies.com.designapp.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by pamrutkar on 15/03/17.
 */
public class ToBeReceiverAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements GravitySnapHelper.SnapListener, View.OnClickListener {
    Context context;
    private HashMap<Integer, ArrayList<StatusModel>> receiver_senderAcpStatusList;
    private HashMap<Integer, ArrayList<StatusModel>> receiver_stoStatusList;
    private HashMap<Integer, ArrayList<StatusModel>> receiver_grnStatusList;
    private HashMap<Integer, ArrayList<StatusModel>> receiver_initiatedStatusList;

    private ArrayList<StatusModel> receiver_list;
    private HashMap<Integer, ArrayList<StatusModel>> receiverada_statusList;
    private boolean[] Receiver_Toggle;
    private int[] receiver_trackId;
    private OnclickStatus onclickStatus;

    public ToBeReceiverAdapter(HashMap<Integer, ArrayList<StatusModel>> rec_initiatedStatusList,
                               HashMap<Integer, ArrayList<StatusModel>> rec_senderAcpStatusList,
                               HashMap<Integer, ArrayList<StatusModel>> rec_stoStatusList,
                               HashMap<Integer, ArrayList<StatusModel>> rec_grnStatusList,
                               ArrayList<StatusModel> rec_list,
                               Context context,
                               OnclickStatus listner) {
        this.receiver_initiatedStatusList = rec_initiatedStatusList;
        this.receiver_senderAcpStatusList = rec_senderAcpStatusList;
        this.receiver_stoStatusList = rec_stoStatusList;
        this.receiver_grnStatusList = rec_grnStatusList;
        this.receiver_list = rec_list;
        this.context = context;
        onclickStatus = listner;
        Receiver_Toggle = new boolean[receiver_list.size()];
        receiver_trackId = new int[receiver_list.size()];
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_to_be_receiver_adapter, parent, false);
        return new ToBeReceiverAdapter.Holder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ToBeReceiverAdapter.Holder) {
            if (position < receiver_list.size()) {
                HandlePositionOnSet(holder, position);
                ((ToBeReceiverAdapter.Holder) holder).Rec_Status_caseNumber.setText("" + "Case#" + receiver_list.get(position).getCaseNo());
                ((ToBeReceiverAdapter.Holder) holder).Rec_Status_storeCode.setText(receiver_list.get(position).getSenderStoreCode());
                ((ToBeReceiverAdapter.Holder) holder).Rec_Status_storedesc.setText(receiver_list.get(position).getSenderStoreDesc());
                String Rec_StatusInitiated = receiver_list.get(position).getStatusInitiated();
                String Rec_StatusAccept = receiver_list.get(position).getStatusAccept();
                String Rec_StatusSto = receiver_list.get(position).getStatusSto();
                String Rec_StatusGrn = receiver_list.get(position).getStatusGrn();
                ((Holder) holder).Lin_ProcessStatus.removeAllViewsInLayout();
                ((ToBeReceiverAdapter.Holder) holder).Rec_Lin_trCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new ToBeReceiverDetails().StartActivity(context, receiver_list.get(position).getCaseNo(), receiver_list.get(position).getSenderStoreCode(), receiver_list.get(position).getSenderStoreDesc());

                    }
                });

                if (Rec_StatusInitiated.equals("Yes") && Rec_StatusAccept.equals("No") && Rec_StatusSto.equals("No") && Rec_StatusGrn.equals("No")) {
                    Rec_Case0(position, holder);
                    ((ToBeReceiverAdapter.Holder) holder).Rec_Status_case.setText("Pending");
                    ((ToBeReceiverAdapter.Holder) holder).Rec_Status_case.setTextColor(Color.parseColor("#ff7e00"));
                } else if (Rec_StatusInitiated.equals("Yes") && Rec_StatusAccept.equals("Yes") && Rec_StatusSto.equals("No") && Rec_StatusGrn.equals("No")) {
                    Rec_Case1(position, holder);
                    ((ToBeReceiverAdapter.Holder) holder).Rec_Status_case.setText("Pending");
                    ((ToBeReceiverAdapter.Holder) holder).Rec_Status_case.setTextColor(Color.parseColor("#ff7e00"));
                } else if (Rec_StatusInitiated.equals("Yes") && Rec_StatusAccept.equals("Yes") && Rec_StatusSto.equals("Yes") && Rec_StatusGrn.equals("No")) {
                    Rec_Case2(position, holder);
                    ((ToBeReceiverAdapter.Holder) holder).Rec_Status_case.setText("Pending");
                    ((ToBeReceiverAdapter.Holder) holder).Rec_Status_case.setTextColor(Color.parseColor("#ff7e00"));

                } else if (Rec_StatusInitiated.equals("Yes") && Rec_StatusAccept.equals("Yes") && Rec_StatusSto.equals("Yes") && Rec_StatusGrn.equals("Yes")) {
                    Rec_Case3(position, holder);
                    ((ToBeReceiverAdapter.Holder) holder).Rec_Status_case.setText("Completed");
                    ((ToBeReceiverAdapter.Holder) holder).Rec_Status_case.setTextColor(Color.parseColor("#70e503"));
                }
            }
        }
    }

    private void Rec_Case0(final int position, RecyclerView.ViewHolder holder) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup view = (ViewGroup) layoutInflater.inflate(R.layout.activity_receiver_status_track_one, null);
        LinearLayout Initiated = (LinearLayout) view.findViewById(R.id.rec_status_track_position_Initiated);
        Initiated.setTag(position); //we are using set tag for list view's position
        LinearLayout SenderAcpt = (LinearLayout) view.findViewById(R.id.rec_status_track_position_SenderAcpt);
        SenderAcpt.setTag(position);
        LinearLayout STO = (LinearLayout) view.findViewById(R.id.rec_status_track_position_STO);
        STO.setTag(position);
        LinearLayout GRN = (LinearLayout) view.findViewById(R.id.rec_status_track_position_GRN);
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
        LinearLayout Initiated = (LinearLayout) view.findViewById(R.id.rec_status_track_position_Initiated);
        Initiated.setTag(position); //we are using set tag for list view's position
        LinearLayout SenderAcpt = (LinearLayout) view.findViewById(R.id.rec_status_track_position_SenderAcpt);
        SenderAcpt.setTag(position);
        LinearLayout STO = (LinearLayout) view.findViewById(R.id.rec_status_track_position_STO);
        STO.setTag(position);
        LinearLayout GRN = (LinearLayout) view.findViewById(R.id.rec_status_track_position_GRN);
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
        LinearLayout Initiated = (LinearLayout) view.findViewById(R.id.rec_status_track_position_Initiated);
        Initiated.setTag(position); //we are using set tag for list view's position
        LinearLayout SenderAcpt = (LinearLayout) view.findViewById(R.id.rec_status_track_position_SenderAcpt);
        SenderAcpt.setTag(position);
        LinearLayout STO = (LinearLayout) view.findViewById(R.id.rec_status_track_position_STO);
        STO.setTag(position);
        LinearLayout GRN = (LinearLayout) view.findViewById(R.id.rec_status_track_position_GRN);
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
        LinearLayout Initiated = (LinearLayout) view.findViewById(R.id.rec_status_track_position_Initiated);
        Initiated.setTag(position); //we are using set tag for list view's position
        LinearLayout SenderAcpt = (LinearLayout) view.findViewById(R.id.rec_status_track_position_SenderAcpt);
        SenderAcpt.setTag(position);
        LinearLayout STO = (LinearLayout) view.findViewById(R.id.rec_status_track_position_STO);
        STO.setTag(position);
        LinearLayout GRN = (LinearLayout) view.findViewById(R.id.rec_status_track_position_GRN);
        GRN.setTag(position);
        Initiated.setOnClickListener(this);
        SenderAcpt.setOnClickListener(this);
        STO.setOnClickListener(this);
        GRN.setOnClickListener(this);
        ((ToBeReceiverAdapter.Holder) holder).Lin_ProcessStatus.addView(view);
    }

    private void HandlePositionOnSet(RecyclerView.ViewHolder holder, int position) {

        if (Receiver_Toggle[position]) {
            ((ToBeReceiverAdapter.Holder) holder).Rec_Status_layout.removeAllViewsInLayout();
            LayoutInflater layoutInflater1 = (LayoutInflater) context.getApplicationContext()
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup view = (ViewGroup) layoutInflater1.inflate(R.layout.activity_rec_status_initiate, null);
            TextView Rec_Status_docNumber = (TextView) view.findViewById(R.id.rec_status_docNumber);
            TextView Rec_Status_qty = (TextView) view.findViewById(R.id.rec_status_qty);
            TextView Rec_Status_date = (TextView) view.findViewById(R.id.rec_status_date);
            if (receiver_trackId[position] == 1) {
                if (receiver_initiatedStatusList.get(position).isEmpty()) {
                    Rec_Status_docNumber.setText("N/A");
                    Rec_Status_qty.setText("N/A");
                    Rec_Status_date.setText("N/A");
                } else {
                    Rec_Status_docNumber.setText("" + receiver_initiatedStatusList.get(position).get(0).getDocNo());
                    Rec_Status_qty.setText("" + Math.round(receiver_initiatedStatusList.get(position).get(0).getStkOnhandQtyRequested()));
                    Rec_Status_date.setText(receiver_initiatedStatusList.get(position).get(0).getReceiver_requested_date());
                }

            } else if (receiver_trackId[position] == 2) {
                if (receiver_senderAcpStatusList.get(position).isEmpty()) {
                    Rec_Status_docNumber.setText("N/A");
                    Rec_Status_qty.setText("N/A");
                    Rec_Status_date.setText("N/A");
                } else {
                    Rec_Status_docNumber.setText("" + receiver_senderAcpStatusList.get(position).get(0).getDocNo());
                    Rec_Status_qty.setText("" + Math.round(receiver_senderAcpStatusList.get(position).get(0).getStkOnhandQtyRequested()));
                    Rec_Status_date.setText(receiver_senderAcpStatusList.get(position).get(0).getReceiver_requested_date());
                }

            } else if (receiver_trackId[position] == 3) {
                if (receiver_stoStatusList.get(position).isEmpty()) {
                    Rec_Status_docNumber.setText("N/A");
                    Rec_Status_qty.setText("N/A");
                    Rec_Status_date.setText("N/A");
                } else {
                    Rec_Status_docNumber.setText("" + receiver_stoStatusList.get(position).get(0).getDocNo());
                    Rec_Status_qty.setText("" + Math.round(receiver_stoStatusList.get(position).get(0).getStoQty()));
                    Rec_Status_date.setText(receiver_stoStatusList.get(position).get(0).getStoDate());
                }

            } else if (receiver_trackId[position] == 4) {
                if (receiver_grnStatusList.get(position).isEmpty()) {
                    Rec_Status_docNumber.setText("N/A");
                    Rec_Status_qty.setText("N/A");
                    Rec_Status_date.setText("N/A");
                } else {
                    Rec_Status_docNumber.setText("" + receiver_grnStatusList.get(position).get(0).getDocNo());
                    Rec_Status_qty.setText("" + Math.round(receiver_grnStatusList.get(position).get(0).getGrnQty()));
                    Rec_Status_date.setText(receiver_grnStatusList.get(position).get(0).getGrnDate());
                }

            }


            ((ToBeReceiverAdapter.Holder) holder).Rec_Status_layout.addView(view);
            ((ToBeReceiverAdapter.Holder) holder).Rec_Status_layout.setVisibility(View.VISIBLE);
        } else {
            ((ToBeReceiverAdapter.Holder) holder).Rec_Status_layout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return receiver_list.size();
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.rec_status_track_position_Initiated:
                int dublicatePosition = (int) v.getTag();
                if (receiver_list.get(dublicatePosition).getStatusInitiated().equals("Yes")) {
                    receiver_trackId[dublicatePosition] = 1;
                    int caseNo = receiver_list.get(dublicatePosition).getCaseNo();
                    String senderStoreCode = receiver_list.get(dublicatePosition).getSenderStoreCode();
                    String actionStatus = "RECVR_REQ";
                    if (receiver_initiatedStatusList.get(dublicatePosition).isEmpty()) {
                        if (StatusActivity.StatusProcess.getVisibility() == View.GONE) {
                            StatusActivity.StatusProcess.setVisibility(View.VISIBLE);
                            onclickStatus.Onclick(caseNo, actionStatus, dublicatePosition, 1, senderStoreCode);
                            Receiver_Toggle[dublicatePosition] = true;
                        }

                    } else {
                        Receiver_Toggle[dublicatePosition] = true;
                        notifyDataSetChanged();
                    }
                }
                break;

            case R.id.rec_status_track_position_SenderAcpt:
                dublicatePosition = (int) v.getTag();
                if (receiver_list.get(dublicatePosition).getStatusAccept().equals("Yes")) {
                    receiver_trackId[dublicatePosition] = 2;
                    int caseNo = receiver_list.get(dublicatePosition).getCaseNo();
                    String senderStoreCode = receiver_list.get(dublicatePosition).getSenderStoreCode();

                    String actionStatus = "SENDER_ACPT";
                    if (receiver_senderAcpStatusList.get(dublicatePosition).isEmpty()) {
                        if (StatusActivity.StatusProcess.getVisibility() == View.GONE) {
                            StatusActivity.StatusProcess.setVisibility(View.VISIBLE);
                            onclickStatus.Onclick(caseNo, actionStatus, dublicatePosition, 2, senderStoreCode);
                            Receiver_Toggle[dublicatePosition] = true;
                        }

                    } else {
                        Receiver_Toggle[dublicatePosition] = true;
                        notifyDataSetChanged();
                    }
                }
                break;
            case R.id.rec_status_track_position_STO:
                dublicatePosition = (int) v.getTag();
                if (receiver_list.get(dublicatePosition).getStatusSto().equals("Yes")) {
                    receiver_trackId[dublicatePosition] = 3;
                    int caseNo1 = receiver_list.get(dublicatePosition).getCaseNo();
                    String senderStoreCode = receiver_list.get(dublicatePosition).getSenderStoreCode();

                    if (receiver_stoStatusList.get(dublicatePosition).isEmpty()) {
                        if (StatusActivity.StatusProcess.getVisibility() == View.GONE) {
                            StatusActivity.StatusProcess.setVisibility(View.VISIBLE);
                            onclickStatus.Onclick(caseNo1, "", dublicatePosition, 3, senderStoreCode);
                            Receiver_Toggle[dublicatePosition] = true;
                        }

                    } else {
                        Receiver_Toggle[dublicatePosition] = true;
                        notifyDataSetChanged();
                    }
                }
                break;
            case R.id.rec_status_track_position_GRN:
                dublicatePosition = (int) v.getTag();
                if (receiver_list.get(dublicatePosition).getStatusGrn().equals("Yes")) {
                    receiver_trackId[dublicatePosition] = 4;
                    int caseNo2 = receiver_list.get(dublicatePosition).getCaseNo();
                    String senderStoreCode = receiver_list.get(dublicatePosition).getSenderStoreCode();

                    if (receiver_grnStatusList.get(dublicatePosition).isEmpty()) {
                        if (StatusActivity.StatusProcess.getVisibility() == View.GONE) {
                            StatusActivity.StatusProcess.setVisibility(View.VISIBLE);
                            onclickStatus.Onclick(caseNo2, "", dublicatePosition, 4, senderStoreCode);
                            Receiver_Toggle[dublicatePosition] = true;
                        }

                    } else {
                        Receiver_Toggle[dublicatePosition] = true;
                        notifyDataSetChanged();
                    }
                }
                break;
        }
    }

    @Override
    public void onSnap(int position) {

    }

    private static class Holder extends RecyclerView.ViewHolder {

        LinearLayout Lin_ProcessStatus, Rec_Lin_trCard;
        RelativeLayout Rec_Status_layout;
        TextView Rec_Status_caseNumber, Rec_Status_case, Rec_Status_storeCode, Rec_Status_storedesc, Rec_Status_docNumber, Rec_Status_qty, Rec_Status_date;

        public Holder(View itemView) {
            super(itemView);
            Lin_ProcessStatus = (LinearLayout) itemView.findViewById(R.id.lin_progressView);
            Rec_Status_caseNumber = (TextView) itemView.findViewById(R.id.receiver_status_caseNumber);
            Rec_Status_case = (TextView) itemView.findViewById(R.id.receiver_status_case);
            Rec_Status_storeCode = (TextView) itemView.findViewById(R.id.receiver_status_storeCode);
            Rec_Status_storedesc = (TextView) itemView.findViewById(R.id.receiver_status_storedesc);
            Rec_Status_docNumber = (TextView) itemView.findViewById(R.id.rec_status_docNumber);
            Rec_Status_qty = (TextView) itemView.findViewById(R.id.rec_status_qty);
            Rec_Status_date = (TextView) itemView.findViewById(R.id.rec_status_date);
            Rec_Lin_trCard = (LinearLayout) itemView.findViewById(R.id.linear_receiver);
            Rec_Status_layout = (RelativeLayout) itemView.findViewById(R.id.rel_status_layout);
        }

    }

}
