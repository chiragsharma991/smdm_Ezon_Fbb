package apsupportapp.aperotechnologies.com.designapp.Collaboration.Status.Tab_fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
public class ToBeReceivedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>implements GravitySnapHelper.SnapListener
{

    private final Context context;
    private final ArrayList<StatusModel> list;
    private static boolean check=false;


    public ToBeReceivedAdapter(ArrayList<StatusModel> list, Context context) {
        this.list=list;
        this.context=context;



    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_be_received_child, parent, false);
        return new ToBeReceivedAdapter.Holder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ToBeReceivedAdapter.Holder) {
            if(position < list.size())
            {
                ((ToBeReceivedAdapter.Holder)holder).Status_caseNumber.setText(""+(int)list.get(position).getCaseNo());
                ((ToBeReceivedAdapter.Holder)holder).Status_storeCode.setText(list.get(position).getReqStoreCode());
//                ((ToBeReceivedAdapter.Holder)holder).Status_storedesc.setText(list.get(position).getCaseNo());
//                ((ToBeReceivedAdapter.Holder)holder).Status_docNumber.setText(list.get(position).getCaseNo());
//                ((ToBeReceivedAdapter.Holder)holder).Status_qty.setText(list.get(position).getCaseNo());
//                ((ToBeReceivedAdapter.Holder)holder).Status_date.setText(list.get(position).getCaseNo());
                String StatusInitiated=list.get(position).getStatusInitiated();
                String StatusAccept=list.get(position).getStatusAccept();
                String StatusSto=list.get(position).getStatusSto();
                String StatusGrn=list.get(position).getStatusGrn();
                ((Holder) holder).ProcessStatus.removeAllViewsInLayout();

                for (int j = 0; j <4; j++) {

                    switch (j)
                    {
                        case 0:
                            String text0="Initiated";
                            if(StatusInitiated.equals("Yes")){ StatusCompleted(holder,text0); }else { StatusPending(holder,text0); }
                            break;
                        case 1:
                            String text1="Acpt";
                            if(StatusAccept.equals("Yes")){ StatusCompleted(holder,text1); }else { StatusPending(holder,text1); }
                            break;
                        case 2:
                            String text2="STO";
                            if(StatusSto.equals("Yes")){ StatusCompleted(holder,text2); }else { StatusPending(holder,text2); }
                            break;
                        case 3:
                            String text3="GRN";
                            if(StatusGrn.equals("Yes")){ StatusCompleted(holder,text3); }else { StatusPending(holder,text3); }
                            break;

                    }




                }


            }




        }







    }

    private void StatusPending(RecyclerView.ViewHolder holder,String Text) {

        LayoutInflater layoutInflater1 = (LayoutInflater) context.getApplicationContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup view = (ViewGroup) layoutInflater1.inflate(R.layout.status_pending, null);
        TextView title=(TextView)view.findViewById(R.id.title_PendingChild);
        title.setText(Text);
        ((ToBeReceivedAdapter.Holder) holder).ProcessStatus.addView(view);
    }

    private void StatusCompleted(RecyclerView.ViewHolder holder,String Text) {

        LayoutInflater layoutInflater1 = (LayoutInflater) context.getApplicationContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup view = (ViewGroup) layoutInflater1.inflate(R.layout.status_completed, null);
        TextView title=(TextView)view.findViewById(R.id.title_ComppletedChild);
        title.setText(Text);
        ((ToBeReceivedAdapter.Holder) holder).ProcessStatus.addView(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onSnap(int position) {

    }



    private static class Holder extends RecyclerView.ViewHolder {

        LinearLayout ProcessStatus;
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



        }

    }
}