package apsupportapp.aperotechnologies.com.designapp.BigBazaar;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by csuthar on 02/11/17.
 */

public class SubCategory_childAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


// VIEW_LIST=1 , VIEW_LIST=2

    private final String[] listdata;
    private final int VIEW_LIST;
    private Context context;
    Gson gson;
    private String TAG=this.getClass().getSimpleName();
    private onclick listner;


    public SubCategory_childAdapter(Context context, String[] listdata, int VIEW_LIST) {
        this.context = context;
        this.listdata = listdata;
        this.VIEW_LIST = VIEW_LIST;
        this.listner=(onclick) context;
        gson = new Gson();

    }




    @Override
    public int getItemCount() {
        return listdata.length;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.e(TAG, "viewType: "+viewType );

        if (VIEW_LIST == 1) {
            View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_root_bb_list, parent, false);
            final ViewHolder_List holderList=new ViewHolder_List(v);
            holderList.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e(TAG, "onClick: "+holderList.getAdapterPosition()+" " );
                    listner.onclickPostion(VIEW_LIST,holderList.getAdapterPosition(),view);
                }
            });

            return  holderList;
        } else if (VIEW_LIST == 2){
            View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_root_bb_gridlist, parent, false);
            final ViewHolder_Grid holderGrid=new ViewHolder_Grid(v);
            holderGrid.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e(TAG, "onClick: "+holderGrid.getAdapterPosition()+" " );
                    listner.onclickPostion(VIEW_LIST,holderGrid.getAdapterPosition(),view);

                }
            });
            return  holderGrid;
        }
        return null;

    }


    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        if (holder instanceof ViewHolder_List) {

                ((ViewHolder_List) holder).title.setText(listdata[position]);


        }
        else if (holder instanceof ViewHolder_Grid){

            ((ViewHolder_Grid) holder).title.setText(listdata[position]);
            ((ViewHolder_Grid) holder).price.setText(listdata[position]);
            ((ViewHolder_Grid) holder).cutofprice.setText(listdata[position]);

        }

    }

    public class ViewHolder_List extends RecyclerView.ViewHolder {

        TextView title;
        ImageView image;

        public ViewHolder_List(View itemView) {
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.subcategory_root_title);
            image = (ImageView)itemView.findViewById(R.id.subcategory_root_image);

        }
    }
    public class ViewHolder_Grid extends RecyclerView.ViewHolder {

        TextView title,price,cutofprice;
        ImageView image;


        public ViewHolder_Grid(View itemView) {
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.subcategory_root_title);
            price = (TextView)itemView.findViewById(R.id.subcategory_root_price);
            cutofprice = (TextView)itemView.findViewById(R.id.subcategory_root_cutofprice);
            image = (ImageView)itemView.findViewById(R.id.subcategory_root_image);


        }
    }

    protected interface onclick {
        void onclickPostion (int parent , int child,View view);
    }

}
