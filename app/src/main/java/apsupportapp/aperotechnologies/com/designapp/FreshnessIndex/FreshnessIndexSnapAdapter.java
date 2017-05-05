package apsupportapp.aperotechnologies.com.designapp.FreshnessIndex;

/**
 * Created by csuthar on 19/01/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.google.gson.Gson;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisViewPagerValue;


public class FreshnessIndexSnapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements GravitySnapHelper.SnapListener {

    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;
    private final ArrayList<FreshnessIndexDetails> freshnessIndexDetailsArrayList;

    RecyclerView listViewFIndex;
    ArrayList<SalesAnalysisViewPagerValue> analysisArrayList;

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 2;
    Context context;
    String fromWhere;

    Gson gson;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        }
    };
    public FreshnessIndexSnapAdapter(ArrayList<FreshnessIndexDetails> freshnessIndexDetailsArrayList, Context context, String fromWhere, RecyclerView listViewFIndex) {

        this.context = context;
        this.freshnessIndexDetailsArrayList = freshnessIndexDetailsArrayList;
        this.fromWhere = fromWhere;
        this.listViewFIndex = listViewFIndex;
        gson = new Gson();
    }

    @Override
    public int getItemViewType(int position) {

        if (isPositionItem(position)) {
            return VIEW_ITEM;

        } else {
            return VIEW_PROG;
        }
    }

    private boolean isPositionItem(int position) {
        // return position == 0;
        return position != freshnessIndexDetailsArrayList.size();
    }

    @Override
    public int getItemCount() {
        return freshnessIndexDetailsArrayList.size() + 1;
    }

    @Override
    public void onSnap(int position) {
        Log.d("Snapped: ", position + "");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.findex_list_row, parent, false);
            return new FreshnessIndexSnapAdapter.FreshnessHolder(v);
        } else if (viewType == VIEW_PROG) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_footer, parent, false);
            return new ProgressViewHolder(v);
        }

        return null;

    }


    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof FreshnessHolder) {
            if (position < freshnessIndexDetailsArrayList.size()) {
                FreshnessIndexDetails freshnessIndexDetails = freshnessIndexDetailsArrayList.get(position);
                NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("", "in"));

                //  fromWhere = "Department";
                if (fromWhere.equals("Department")) {

                    ((FreshnessHolder) viewHolder).txtfindexClass.setText(freshnessIndexDetails.getPlanDept());
                    ((FreshnessHolder) viewHolder).txtfindexSOH.setText("" + formatter.format(freshnessIndexDetails.getStkOnhandQty()));
                    ((FreshnessHolder) viewHolder).txtfindexSOH_U.setText(" " + String.format("%.1f", freshnessIndexDetails.getStkOnhandQtyCount()));
                    ((FreshnessHolder) viewHolder).txtfindexGIT.setText("" + formatter.format(Math.round(freshnessIndexDetails.getStkGitQty())));


                } else if (fromWhere.equals("Subdept")) {

                    ((FreshnessHolder) viewHolder).txtfindexClass.setText(freshnessIndexDetails.getPlanCategory());
                    ((FreshnessHolder) viewHolder).txtfindexSOH.setText("" + formatter.format(freshnessIndexDetails.getStkOnhandQty()));
                    ((FreshnessHolder) viewHolder).txtfindexSOH_U.setText(" " + String.format("%.1f", freshnessIndexDetails.getStkOnhandQtyCount()));
                    ((FreshnessHolder) viewHolder).txtfindexGIT.setText("" + formatter.format(Math.round(freshnessIndexDetails.getStkGitQty())));

                } else if (fromWhere.equals("Class")) {

                    ((FreshnessHolder) viewHolder).txtfindexClass.setText(freshnessIndexDetails.getPlanClass());
                    ((FreshnessHolder) viewHolder).txtfindexSOH.setText("" + formatter.format(freshnessIndexDetails.getStkOnhandQty()));
                    ((FreshnessHolder) viewHolder).txtfindexSOH_U.setText(" " + String.format("%.1f", freshnessIndexDetails.getStkOnhandQtyCount()));
                    ((FreshnessHolder) viewHolder).txtfindexGIT.setText("" + formatter.format(Math.round(freshnessIndexDetails.getStkGitQty())));


                } else if (fromWhere.equals("Subclass")) {

                    ((FreshnessHolder) viewHolder).txtfindexClass.setText(freshnessIndexDetails.getBrandName());
                    ((FreshnessHolder) viewHolder).txtfindexSOH.setText("" + formatter.format(freshnessIndexDetails.getStkOnhandQty()));
                    ((FreshnessHolder) viewHolder).txtfindexSOH_U.setText(" " + String.format("%.1f", freshnessIndexDetails.getStkOnhandQtyCount()));
                    ((FreshnessHolder) viewHolder).txtfindexGIT.setText("" + formatter.format(Math.round(freshnessIndexDetails.getStkGitQty())));


                } else if (fromWhere.equals("MC")) {

                    ((FreshnessHolder) viewHolder).txtfindexClass.setText(freshnessIndexDetails.getBrandplanClass());
                    ((FreshnessHolder) viewHolder).txtfindexSOH.setText("" + formatter.format(freshnessIndexDetails.getStkOnhandQty()));
                    ((FreshnessHolder) viewHolder).txtfindexSOH_U.setText(" " + String.format("%.1f", freshnessIndexDetails.getStkOnhandQtyCount()));
                    ((FreshnessHolder) viewHolder).txtfindexGIT.setText("" + formatter.format(Math.round(freshnessIndexDetails.getStkGitQty())));
                }
            }
        } else {

        }

    }


    public static class FreshnessHolder extends RecyclerView.ViewHolder {


        TextView txtfindexClass, txtfindexSOH, txtfindexSOH_U, txtfindexGIT;


        public FreshnessHolder(View itemView) {
            super(itemView);

            txtfindexClass = (TextView) itemView.findViewById(R.id.txtfindexClass);
            txtfindexSOH = (TextView) itemView.findViewById(R.id.txtfindexSOH);
            txtfindexSOH_U = (TextView) itemView.findViewById(R.id.txtfindexSOH_U);
            txtfindexGIT = (TextView) itemView.findViewById(R.id.txtfindexGIT);

        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        TextView txtView;

        public ProgressViewHolder(View footerView) {
            super(footerView);
            txtView = (TextView) footerView.findViewById(R.id.txtView);
        }
    }


}