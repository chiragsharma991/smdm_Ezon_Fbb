package apsupportapp.aperotechnologies.com.designapp.Collaboration.Status.Tab_fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.RunningPromo.RunningPromoDetails;
import apsupportapp.aperotechnologies.com.designapp.RunningPromo.RunningPromoSnapAdapter;
import apsupportapp.aperotechnologies.com.designapp.RunningPromo.VM;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static apsupportapp.aperotechnologies.com.designapp.HorlyAnalysis.Option_Fragment.view;

/**
 * Created by csuthar on 02/03/17.
 */
public class ToBeReceivedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>implements GravitySnapHelper.SnapListener
{

    private final Context context;
    private final ArrayList<String> list;
    private static boolean check=false;


    public ToBeReceivedAdapter(ArrayList<String> list, Context context) {
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

                for (int j = 0; j <5; j++) {
                    Log.e("TAG", "Onclick: ");
                    LayoutInflater layoutInflater1 = (LayoutInflater) context.getApplicationContext()
                            .getSystemService(LAYOUT_INFLATER_SERVICE);
                    ViewGroup view = (ViewGroup) layoutInflater1.inflate(R.layout.grey_child, null);
                    ((Holder) holder).ProcessStatus.addView(view);
                }
        }







    }

    @Override
    public int getItemCount() {
        return 8;
    }

    @Override
    public void onSnap(int position) {

    }



    private static class Holder extends RecyclerView.ViewHolder {

        LinearLayout ProcessStatus;
        public Holder(View itemView) {
            super(itemView);
            ProcessStatus=(LinearLayout)itemView.findViewById(R.id.progressView);



        }

    }
}