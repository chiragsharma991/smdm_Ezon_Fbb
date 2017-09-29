package apsupportapp.aperotechnologies.com.designapp.DashboardSnap;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import java.util.ArrayList;
import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by csuthar on 22/09/17.
 */


public class LobMappingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>   {

    private boolean[] lobchecked;
    private ArrayList<String> lobList;
    private LayoutInflater mInflater;
    private final int VIEW_ITEM = 1;
    Context context;




    public LobMappingAdapter(ArrayList<String> lobList, Context context, boolean[] lobchecked) {

        this.context = context;
        this.lobList = lobList;
        this.lobchecked = lobchecked;
        mInflater = LayoutInflater.from(this.context);

    }

    @Override
    public int getItemViewType(int position) {


        return VIEW_ITEM;

    }



    @Override
    public int getItemCount() {

            return lobList.size();

    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.root_lob_mappingchild, parent, false);
            return new MappingViewHolder(v) ;
        }

        return null;

    }


    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        if (viewHolder instanceof MappingViewHolder) {
            if (position < lobList.size()) {

                Log.e("TAG", "onBindViewHolder: "+lobchecked[position]);
                ((MappingViewHolder) viewHolder).radio_chk.setChecked(lobchecked[position] ? true : false );
                ((MappingViewHolder) viewHolder).title.setText(lobList.get(position));
                ((MappingViewHolder) viewHolder).parent_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("TAG", "onClick: "+position );
                        for (int i = 0; i < lobchecked.length; i++) {
                            if (position == i) lobchecked[i] = true;
                            else lobchecked[i] = false;
                        }
                        notifyDataSetChanged();
                    }
                });
            }
        }
    }



    private class MappingViewHolder extends RecyclerView.ViewHolder {


        TextView title;
        RadioButton radio_chk;
        LinearLayout parent_view;

        public MappingViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            radio_chk = (RadioButton) itemView.findViewById(R.id.radio_chk);
            parent_view = (LinearLayout) itemView.findViewById(R.id.parent_view);
        }
    }



}