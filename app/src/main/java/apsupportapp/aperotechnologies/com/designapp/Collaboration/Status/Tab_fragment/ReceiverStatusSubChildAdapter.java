package apsupportapp.aperotechnologies.com.designapp.Collaboration.Status.Tab_fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by pamrutkar on 16/03/17.
 */
public class ReceiverStatusSubChildAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private  Context context;
    private  int Rec_PrePosition;
    private HashMap<Integer, ArrayList<StatusModel>> subchild_list;
    private  ReceiverStatusDetailsAdapter receiverStatusDetailsAdapter;



    public ReceiverStatusSubChildAdapter(HashMap<Integer, ArrayList<StatusModel>> statusHashmapChildList, Context context, int position, ReceiverStatusDetailsAdapter receiverStatusDetailsAdapter) {

        this.subchild_list=statusHashmapChildList;
        this.context=context;//
        Rec_PrePosition=position;
        this.receiverStatusDetailsAdapter=receiverStatusDetailsAdapter;

    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
