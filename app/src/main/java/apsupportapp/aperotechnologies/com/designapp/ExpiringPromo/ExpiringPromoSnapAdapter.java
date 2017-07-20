package apsupportapp.aperotechnologies.com.designapp.ExpiringPromo;


import android.content.Context;
        import android.content.Intent;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;

        import android.view.LayoutInflater;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

        import java.util.ArrayList;

        import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.RunningPromo.VM;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;

public class ExpiringPromoSnapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements GravitySnapHelper.SnapListener {

    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 2;
    private final Context context;
    private final ArrayList<RunningPromoListDisplay> expireList;

    // Disable touch detection for parent recyclerView if we use vertical nested recyclerViews
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        }
    };

    public ExpiringPromoSnapAdapter(ArrayList<RunningPromoListDisplay> expireList, Context context)
    {
        this.context=context;
        this.expireList=expireList;
    }


    @Override
    public int getItemViewType(int position) {

        if (isPositionItem(position)){
            return VIEW_ITEM;

        }
        else {
            return VIEW_PROG;
        }
    }
    private boolean isPositionItem(int position) {
        return position != expireList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_ITEM) {
            View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_expiring_promo_child, parent, false);
            return new ExpireHolder(v);
        } else if (viewType == VIEW_PROG){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_footer, parent, false);
            return new FooterView(v);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ExpireHolder) {
            if(position < expireList.size()) {

                RunningPromoListDisplay snap = expireList.get(position);
                ((ExpireHolder)holder).PromotionName.setText(snap.getPromoDesc());
                ((ExpireHolder)holder).StartDate.setText(snap.getPromoStartDate());
                ((ExpireHolder)holder).EndDate.setText(snap.getPromoEndDate());
                ((ExpireHolder)holder).Days.setText(String.valueOf(snap.getPromoDays()));
                ((ExpireHolder)holder).Vm.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context,VM.class);
                        intent.putExtra("VM",expireList.get(position).getPromoDesc());
                        intent.putExtra("FROM","RunningPromo");
                        context.startActivity(intent);
                    }
                });
            }
        }
    }




    @Override
    public int getItemCount() {
        return expireList.size()+1;
    }

    @Override
    public void onSnap(int position) {
        Log.d("Snapped: ", position + "");
    }

    public static class ExpireHolder extends RecyclerView.ViewHolder {



        TextView PromotionName,StartDate,EndDate,Days;
        ImageView Vm;

        public ExpireHolder(View itemView) {
            super(itemView);
            PromotionName = (TextView) itemView.findViewById(R.id.txtPromoExpName);
            StartDate = (TextView) itemView.findViewById(R.id.txtstartExpDate);
            EndDate = (TextView) itemView.findViewById(R.id.txtEndExpDate);
            Days = (TextView) itemView.findViewById(R.id.txtExpDays);
            Vm = (ImageView) itemView.findViewById(R.id.imgExpVm);
        }

    }

    public static class FooterView extends RecyclerView.ViewHolder {

        TextView txtView;

        public FooterView(View footerView){
            super(footerView);

            txtView = (TextView)footerView.findViewById(R.id.txtView);
        }
    }
}