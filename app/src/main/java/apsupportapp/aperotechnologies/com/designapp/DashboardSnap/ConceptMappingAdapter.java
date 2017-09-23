package apsupportapp.aperotechnologies.com.designapp.DashboardSnap;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import java.util.ArrayList;
import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by csuthar on 22/09/17.
 */



public class ConceptMappingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>   {


    private final boolean[] conceptchecked;
    private ArrayList<String> conceptList;
    private LayoutInflater mInflater;
    private final int VIEW_ITEM = 1;
    Context context;




    public ConceptMappingAdapter(ArrayList<String> conceptList, Context context, boolean[] conceptchecked) {

        this.context = context;
        this.conceptList = conceptList;
        this.conceptchecked = conceptchecked;
        mInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getItemViewType(int position) {

        return VIEW_ITEM;

    }



    @Override
    public int getItemCount() {
        return conceptList.size();
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.root_concept_mappingchild, parent, false);
            return new MappingViewHolder(v) ;
        }

        return null;

    }


    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {

        if (viewHolder instanceof MappingViewHolder ) {
            if (position < conceptList.size()) {

                ((MappingViewHolder) viewHolder).radio_chk.setChecked(conceptchecked[position] ? true : false );
                ((MappingViewHolder) viewHolder).title.setText(conceptList.get(position));

            }
        }
    }



    private class MappingViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        RadioButton radio_chk;

        public MappingViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            radio_chk = (RadioButton) itemView.findViewById(R.id.radio_chk);
        }
    }



}
