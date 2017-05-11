package apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.ToDo_Modal;
import apsupportapp.aperotechnologies.com.designapp.R;


public class DetailsHeaderChildAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final Context context;
    private final int PrePosition;
    private final boolean[] visibleItems;
    private HashMap<Integer, ArrayList<ToDo_Modal>> list;
    private Set<Pair<Integer, Integer>> CheckedItems;
    private final StockDetailsAdapter stockDetailsAdapter;
    private final boolean[] headercheckList;



    public DetailsHeaderChildAdapter(boolean[] visibleItems, HashMap<Integer, ArrayList<ToDo_Modal>> list, boolean[] headercheckList, Set<Pair<Integer, Integer>> checkedItems, Context context, int position, StockDetailsAdapter stockDetailsAdapter)
    {
        this.list = list;
        this.context = context;//
        this.visibleItems = visibleItems;//
        PrePosition = position;
        this.stockDetailsAdapter = stockDetailsAdapter;
        this.headercheckList = headercheckList;
        CheckedItems = checkedItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_header_child, parent, false);
        return new DetailsHeaderChildAdapter.Holder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position)
    {

        final Pair<Integer, Integer> Childtag = new Pair<Integer, Integer>(PrePosition, position);
        ((DetailsHeaderChildAdapter.Holder) holder).DetailChild_size.setText(list.get(PrePosition).get(position).getLevel());
        ((DetailsHeaderChildAdapter.Holder) holder).DetailChild_requiredQty.setText("" + Math.round(list.get(PrePosition).get(position).getStkOnhandQtyRequested()));
        ((DetailsHeaderChildAdapter.Holder) holder).DetailChild_aviQty.setText("" + Math.round(list.get(PrePosition).get(position).getStkQtyAvl()));
        ((DetailsHeaderChildAdapter.Holder) holder).DetailChild_git.setText("" + Math.round(list.get(PrePosition).get(position).getStkGitQty()));
        ((DetailsHeaderChildAdapter.Holder) holder).DetailChild_soh.setText("" + Math.round(list.get(PrePosition).get(position).getStkOnhandQty()));
        ((DetailsHeaderChildAdapter.Holder) holder).DetailChild_checkBox.setTag(Childtag);

        // if header is checked then all sub values will be checked.
        if(visibleItems[PrePosition])
        {
            if (headercheckList[PrePosition])
            {
                CheckedItems.add(Childtag);
            }
            else
            {
                CheckedItems.remove(Childtag);
            }
        }
        ((DetailsHeaderChildAdapter.Holder) holder).DetailChild_checkBox.setChecked(CheckedItems.contains(Childtag));

        ((DetailsHeaderChildAdapter.Holder) holder).DetailChild_checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                visibleItems[PrePosition]=false;
                final CheckBox cb = (CheckBox) view;
                final Pair<Integer, Integer> tagFlag = (Pair<Integer, Integer>) view.getTag();

                //this is check and uncheked method to remove and add flag ...
                // before uncheck you have to calculate past things like :
                // 1. child list are selected to all
                // 2. one is not selected to all
                if (cb.isChecked())
                {
                    CheckCondition(tagFlag);
                }
                else
                {
                    UnCheckCondition(tagFlag);
                }
                stockDetailsAdapter.notifyDataSetChanged();
            }
        });
    }

    private void CheckCondition(Pair<Integer, Integer> tagFlag)
    {
        CheckedItems.add(tagFlag);
        boolean[] CheckChild = new boolean[list.get(PrePosition).size()];
        for (int i = 0; i < list.get(PrePosition).size(); i++)
        {
            Pair<Integer, Integer> Tag = new Pair<Integer, Integer>(PrePosition, i);
            if (CheckedItems.contains(Tag)) {
                CheckChild[i] = true;
            } else
            {
                CheckChild[i] = false;
            }
        }
        //if all list are true from all list child then header check will be enable.
        if (containsTrue(CheckChild)) {
            headercheckList[PrePosition] = true;
        }
    }

    private void UnCheckCondition(Pair<Integer, Integer> tagFlag) {
        boolean[] CheckChild = new boolean[list.get(PrePosition).size()];
        for (int i = 0; i < list.get(PrePosition).size(); i++)
        {
            Pair<Integer, Integer> Tag = new Pair<Integer, Integer>(PrePosition, i);
            if (CheckedItems.contains(Tag))
            {
                CheckChild[i] = true;
            } else {
                CheckChild[i] = false;
            }
        }
        // if one list is false from all list child then header check will be disable.
        if (containsTrue(CheckChild))
        {
            headercheckList[PrePosition] = false;
        }
        CheckedItems.remove(tagFlag);
    }

    public boolean containsTrue(boolean[] array) {

        boolean AllItems = false;
        for (boolean anArray : array) {
            if (anArray) {
                AllItems = true;
            } else {
                AllItems = false;
                break;
            }
        }
        return AllItems;
    }

    @Override
    public int getItemCount() {
        return list.get(PrePosition).size();
    }

    private static class Holder extends RecyclerView.ViewHolder {

        private final TextView DetailChild_size, DetailChild_requiredQty, DetailChild_aviQty, DetailChild_git, DetailChild_soh;
        private CheckBox DetailChild_checkBox;
        public Holder(View itemView) {
            super(itemView);
            DetailChild_size = (TextView) itemView.findViewById(R.id.detailChild_size);
            DetailChild_requiredQty = (TextView) itemView.findViewById(R.id.detailChild_requiredQty);
            DetailChild_aviQty = (TextView) itemView.findViewById(R.id.detailChild_aviQty);
            DetailChild_git = (TextView) itemView.findViewById(R.id.detailChild_git);
            DetailChild_soh = (TextView) itemView.findViewById(R.id.detailChild_soh);
            DetailChild_checkBox = (CheckBox) itemView.findViewById(R.id.detailChild_checkBox);

        }
    }
}
