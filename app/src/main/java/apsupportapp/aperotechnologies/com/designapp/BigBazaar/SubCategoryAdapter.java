package apsupportapp.aperotechnologies.com.designapp.BigBazaar;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.gson.Gson;
import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by csuthar on 02/11/17.
 */


public class SubCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private final int VIEW_LIST = 1;
    private final int VIEW_GRID = 2;
    private final String[] listdata;
    private Context context;
    Gson gson;


    public SubCategoryAdapter(Context context, String[] listdata) {
        this.context = context;
        this.listdata = listdata;
        gson = new Gson();
    }


    @Override
    public int getItemViewType(int position) {

        if (isPositionItem(position)){
            return VIEW_LIST;
        }
        else {
            return VIEW_GRID;
        }
    }

    private boolean isPositionItem(int position) {

        if(position==0) {
            return true;
        }else{
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return listdata.length;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //if (viewType == VIEW_LIST) {
            View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_bb_verticallist, parent, false);
            return new ViewHolder(v);
     /*   } else if (viewType == VIEW_GRID){
            View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_root_bb_gridlist, parent, false);
            return new ViewHolder(v);
        }
        return null;*/

    }


    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolder) {
            if(position <= listdata.length) {
                ((ViewHolder) holder).title.setText(listdata[position]);

                switch (holder.getItemViewType()){

                    case VIEW_LIST:
                        ((ViewHolder) holder).recycler_childView.setLayoutManager(new LinearLayoutManager(((ViewHolder) holder)
                                .recycler_childView.getContext(), LinearLayoutManager.VERTICAL, false));
                        ((ViewHolder) holder).recycler_childView.setAdapter(new SubCategory_childAdapter(context,listdata,VIEW_LIST));
                        break;

                    case VIEW_GRID:
                        ((ViewHolder) holder).recycler_childView.setLayoutManager(new GridLayoutManager(((ViewHolder) holder)
                                .recycler_childView.getContext(),2));
                        ((ViewHolder) holder).recycler_childView.setAdapter(new SubCategory_childAdapter(context,listdata,VIEW_GRID));
                        break;

                }





            }

        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        RecyclerView recycler_childView;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.title);
            recycler_childView = (RecyclerView) itemView.findViewById(R.id.list);

        }
    }

}