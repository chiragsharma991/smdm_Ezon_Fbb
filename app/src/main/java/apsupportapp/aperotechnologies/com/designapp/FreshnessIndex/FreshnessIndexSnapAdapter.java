package apsupportapp.aperotechnologies.com.designapp.FreshnessIndex;

/**
 * Created by csuthar on 19/01/17.
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.google.gson.Gson;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesAnalysisSnapAdapter;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesPagerAdapter;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisListDisplay;
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
    // Disable touch detection for parent recyclerView if we use vertical nested recyclerViews
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        }
    };

  /*  public FreshnessIndexSnapAdapter(ArrayList<SalesAnalysisListDisplay> arrayList, Context context, int currentIndex, String fromwhere, RecyclerView listView_SalesAnalysis) {

       // mSnaps = new ArrayList<>();
        Log.e("in sales analysis adapter"," ");
        this.mSnaps = arrayList;
        this.context = context;
        this.fromwhere = fromwhere;
        this.listView_SalesAnalysis = listView_SalesAnalysis;
        mInflater = LayoutInflater.from(context);
        this.currentIndex = currentIndex;
        level = 1;
        focusposition = 0;
        selFirstPositionValue = 0;
        analysisArrayList = new ArrayList<SalesAnalysisViewPagerValue>();
        gson = new Gson();

        //getFilter();
    }
*/
    public FreshnessIndexSnapAdapter(ArrayList<FreshnessIndexDetails> freshnessIndexDetailsArrayList, Context context, String fromWhere, RecyclerView listViewFIndex) {

        this.context = context;
        this.freshnessIndexDetailsArrayList = freshnessIndexDetailsArrayList;
        this.fromWhere = fromWhere;
        this.listViewFIndex = listViewFIndex;

        gson = new Gson();
    }




//    public void addSnap(SalesAnalysisListDisplay snap) {
//        mSnaps.add(snap);
//    }

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
        // return position == 0;
        return position != freshnessIndexDetailsArrayList.size();
    }

    @Override
    public int getItemCount() {
        return freshnessIndexDetailsArrayList.size()+1;
    }

    @Override
    public void onSnap(int position) {
        Log.d("Snapped: ", position + "");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_ITEM) {
            View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.findex_list_row, parent, false);
            return new FreshnessIndexSnapAdapter.FreshnessHolder(v);
        } else if (viewType == VIEW_PROG){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_footer, parent, false);
            return new ProgressViewHolder(v);
        }

        return null;

    }


    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof FreshnessHolder) {
            if(position <= freshnessIndexDetailsArrayList.size()) {
                FreshnessIndexDetails freshnessIndexDetails = freshnessIndexDetailsArrayList.get(position);
                //  fromWhere = "Department";
                if (fromWhere.equals("Department")) {

                    ((FreshnessHolder) viewHolder).txtfindexClass.setText(freshnessIndexDetails.getPlanDept());
                    ((FreshnessHolder) viewHolder).txtfindexSOH.setText("" + freshnessIndexDetails.getStkOnhandQty());
                    ((FreshnessHolder) viewHolder).txtfindexSOH_U.setText(" " + String.format("%.1f", freshnessIndexDetails.getStkOnhandQtyCount()));
                    ((FreshnessHolder) viewHolder).txtfindexGIT.setText("" + Math.round(freshnessIndexDetails.getStkGitQty()));


                } else if (fromWhere.equals("Category")) {

                    ((FreshnessHolder) viewHolder).txtfindexClass.setText(freshnessIndexDetails.getPlanCategory());
                    ((FreshnessHolder) viewHolder).txtfindexSOH.setText("" + freshnessIndexDetails.getStkOnhandQty());
                    ((FreshnessHolder) viewHolder).txtfindexSOH_U.setText(" " + String.format("%.1f", freshnessIndexDetails.getStkOnhandQtyCount()));
                    ((FreshnessHolder) viewHolder).txtfindexGIT.setText("" + Math.round(freshnessIndexDetails.getStkGitQty()));

                } else if (fromWhere.equals("Plan Class")) {

                    ((FreshnessHolder) viewHolder).txtfindexClass.setText(freshnessIndexDetails.getPlanClass());
                    ((FreshnessHolder) viewHolder).txtfindexSOH.setText("" + freshnessIndexDetails.getStkOnhandQty());
                    ((FreshnessHolder) viewHolder).txtfindexSOH_U.setText(" " + String.format("%.1f", freshnessIndexDetails.getStkOnhandQtyCount()));
                    ((FreshnessHolder) viewHolder).txtfindexGIT.setText("" + Math.round(freshnessIndexDetails.getStkGitQty()));


                } else if (fromWhere.equals("Brand")) {

                    ((FreshnessHolder) viewHolder).txtfindexClass.setText(freshnessIndexDetails.getBrandName());
                    ((FreshnessHolder) viewHolder).txtfindexSOH.setText("" + (int) freshnessIndexDetails.getStkOnhandQty());
                    ((FreshnessHolder) viewHolder).txtfindexSOH_U.setText(" " + String.format("%.1f", freshnessIndexDetails.getStkOnhandQtyCount()));
                    ((FreshnessHolder) viewHolder).txtfindexGIT.setText("" + Math.round(freshnessIndexDetails.getStkGitQty()));


                } else if (fromWhere.equals("Brand Plan Class")) {

                    ((FreshnessHolder) viewHolder).txtfindexClass.setText(freshnessIndexDetails.getBrandplanClass());
                    ((FreshnessHolder) viewHolder).txtfindexSOH.setText("" + (int) freshnessIndexDetails.getStkOnhandQty());
                    ((FreshnessHolder) viewHolder).txtfindexSOH_U.setText(" " + String.format("%.1f", freshnessIndexDetails.getStkOnhandQtyCount()));
                    ((FreshnessHolder) viewHolder).txtfindexGIT.setText("" + Math.round(freshnessIndexDetails.getStkGitQty()));
                }
            }
        }
        else
        {

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
        //        Button loadButton;
//        ProgressBar progressBar;
        TextView txtView;

        public ProgressViewHolder(View footerView){
            super(footerView);
//            loadButton = (Button) footerView.findViewById(R.id.reload_button);
//            progressBar = (ProgressBar) footerView.findViewById(R.id.progress_load);
              txtView = (TextView)footerView.findViewById(R.id.txtView);
        }
    }


}