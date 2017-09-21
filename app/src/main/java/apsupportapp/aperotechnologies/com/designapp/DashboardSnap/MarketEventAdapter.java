package apsupportapp.aperotechnologies.com.designapp.DashboardSnap;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;


import apsupportapp.aperotechnologies.com.designapp.R;

public class MarketEventAdapter extends RecyclerView.Adapter<MarketEventAdapter.ViewHolder> {

    private final Context context;
    private final int preposition;
    private final ArrayList<String> eventUrlList;
    private onclickView clickView;

    public MarketEventAdapter(ArrayList<String> eventUrlList, Context context, int preposition)
    {
        this.context = context;
        this.preposition = preposition;
        this.eventUrlList = eventUrlList;

        clickView = (onclickView) this.context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_snap_child_footer_hrl, parent, false));

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {

        Picasso.with(context).load(eventUrlList.get(position)).fit().centerInside()
                .placeholder(R.color.grey)
                .error(R.color.grey)
                .into(holder.Snap_child_imageView);

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount()
    {
        return eventUrlList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        public ImageView Snap_child_imageView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            Snap_child_imageView = (ImageView) itemView.findViewById(R.id.snap_child_imageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Log.e("TAG", "click" + getAdapterPosition());
            clickView.onclickView(preposition, getAdapterPosition());
        }
    }

}

