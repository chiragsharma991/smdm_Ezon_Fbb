package apsupportapp.aperotechnologies.com.designapp.DashboardSnap;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import apsupportapp.aperotechnologies.com.designapp.R;

import static java.security.AccessController.getContext;

public class SnapChildAdapter extends RecyclerView.Adapter<SnapChildAdapter.ViewHolder> {

    private final Context context;
    private final int preposition;
    private List<App> mApps;
    private boolean mHorizontal;
    private boolean mPager;
    private onclickView clickView;
    public String TAG = "SnapChildAdapter";

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
        LinearLayout linear_snap;

        public ViewHolder(View itemView)
        {
            super(itemView);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity)context).getWindowManager()
                    .getDefaultDisplay()
                    .getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;

//            Log.e( "ViewHolder: ",""+width );
            Snap_child_imageView = (ImageView) itemView.findViewById(R.id.snap_child_imageView);
            Snap_child_subtitle = (TextView) itemView.findViewById(R.id.snap_child_subtitle);
            linear_snap = (LinearLayout)itemView.findViewById(R.id.linear_snap);
            if(mApps.size() > 3)
            {
                double update_width = width / 3.5;
//                Log.e(TAG, "ViewHolderin if: "+update_width);
                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams((int) update_width,LinearLayout.LayoutParams.WRAP_CONTENT);
                linear_snap.setLayoutParams(parms);
            }

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            Log.e("TAG","click"+getAdapterPosition());
            clickView.onclickView(preposition,getAdapterPosition());
        }
    }

}

