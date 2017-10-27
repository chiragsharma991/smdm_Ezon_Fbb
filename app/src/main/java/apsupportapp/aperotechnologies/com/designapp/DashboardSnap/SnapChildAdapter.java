package apsupportapp.aperotechnologies.com.designapp.DashboardSnap;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    public String TAG = "SnapChildAdapter";
    private String sutitle="";

    public SnapChildAdapter(boolean horizontal, boolean pager, List<App> apps, Context context, int preposition) {
        mHorizontal = horizontal;
        mApps = apps;
        mPager = pager;
        this.context= context;
        this.preposition= preposition;
        clickView=(onclickView)this.context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

            return new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_snap_child_hrl, parent, false)) ;


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        App app = mApps.get(position);
        sutitle=app.getName();

        holder.Snap_child_imageView.setImageResource(app.getDrawable());
        holder.Snap_child_subtitle.setText(app.getName());
        holder.Snap_child_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickView.onclickView(preposition,position,mApps.get(position).getKpiId());

            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mApps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        public ImageView Snap_child_imageView;
        public TextView Snap_child_subtitle;
        LinearLayout linear_snap;

        public ViewHolder(final View itemView)
        {
            super(itemView);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity)context).getWindowManager()
                    .getDefaultDisplay()
                    .getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;

            Snap_child_imageView = (ImageView) itemView.findViewById(R.id.snap_child_imageView);
            Snap_child_subtitle = (TextView) itemView.findViewById(R.id.snap_child_subtitle);
            linear_snap = (LinearLayout)itemView.findViewById(R.id.linear_snap);
            if(mApps.size() > 3)
            {
                double update_width = width / 3.5;
                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams((int) update_width,LinearLayout.LayoutParams.WRAP_CONTENT);
                linear_snap.setLayoutParams(parms);
            }
        }

    }

}

