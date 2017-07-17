package apsupportapp.aperotechnologies.com.designapp.DashboardSnap;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import apsupportapp.aperotechnologies.com.designapp.R;

public class SnapChildAdapter extends RecyclerView.Adapter<SnapChildAdapter.ViewHolder> {

    private final Context context;
    private final int preposition;
    private List<App> mApps;
    private boolean mHorizontal;
    private boolean mPager;
    private onclickView clickView;

    public SnapChildAdapter(boolean horizontal, boolean pager, List<App> apps, Context context, int preposition) {
        mHorizontal = horizontal;
        mApps = apps;
        mPager = pager;
        this.context= context;
        this.preposition= preposition;
        clickView=(onclickView)this.context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_snap_child_hrl, parent, false)) ;


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        App app = mApps.get(position);
        holder.Snap_child_imageView.setImageResource(app.getDrawable());
        holder.Snap_child_subtitle.setText(app.getName());

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mApps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView Snap_child_imageView;
        public TextView Snap_child_subtitle;

        public ViewHolder(View itemView) {
            super(itemView);
            Snap_child_imageView = (ImageView) itemView.findViewById(R.id.snap_child_imageView);
            Snap_child_subtitle = (TextView) itemView.findViewById(R.id.snap_child_subtitle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Log.e("TAG","click"+getAdapterPosition());
            clickView.onclickView(preposition,getAdapterPosition());
        }
    }

}
