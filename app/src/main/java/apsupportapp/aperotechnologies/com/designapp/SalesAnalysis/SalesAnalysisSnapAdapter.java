package apsupportapp.aperotechnologies.com.designapp.SalesAnalysis;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.google.gson.Gson;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.RunningPromo.RunningPromoSnapAdapter;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisListDisplay;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisViewPagerValue;

public class SalesAnalysisSnapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements GravitySnapHelper.SnapListener {

    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;
    ArrayList<SalesAnalysisViewPagerValue> analysisArrayList;
    SalesPagerAdapter pageradapter;
    SalesAnalysisListDisplay salesAnalysisListDisplay;
    int focusposition,selFirstPositionValue;
    RecyclerView listView_SalesAnalysis;
    int currentIndex;
    int offsetvalue = 0, count = 0, limit = 100;
    private LayoutInflater mInflater;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 2;
    Context context;
    String fromwhere;
    int level ;
    Gson gson;
    private ArrayList<SalesAnalysisListDisplay> mSnaps;
    // Disable touch detection for parent recyclerView if we use vertical nested recyclerViews
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        }
    };

    public SalesAnalysisSnapAdapter(ArrayList<SalesAnalysisListDisplay> arrayList, Context context, int currentIndex, String fromwhere, RecyclerView listView_SalesAnalysis) {

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


    public SalesAnalysisSnapAdapter() {
        mSnaps = new ArrayList<>();
    }

    public void addSnap(SalesAnalysisListDisplay snap) {
        mSnaps.add(snap);
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
       // return position == 0;
        return position != getItemCount()-1;
    }

    @Override
    public int getItemCount() {
        return mSnaps.size();
    }

    @Override
    public void onSnap(int position) {
        Log.d("Snapped: ", position + "");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.child_sales_listview, parent, false);
//
//
////        if (viewType == VERTICAL) {
////            view.findViewById(R.id.recyclerView).setOnTouchListener(mTouchListener);
////        }
//
//        return new SalesAnalysisSnapAdapter.SalesViewHolder(view);



        if (viewType == VIEW_ITEM) {
            View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.child_sales_listview, parent, false);
            return new SalesAnalysisSnapAdapter.SalesViewHolder(v);
        } else if (viewType == VIEW_PROG){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_footer, parent, false);
            return new ProgressViewHolder(v);
        }

        return null;

    }


    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof SalesViewHolder) {
            SalesAnalysisListDisplay productNameBean = mSnaps.get(position);
            if (fromwhere.equals("Department")) {

                ((SalesViewHolder) viewHolder).nameTv.setText(productNameBean.getPlanDept());
                ((SalesViewHolder) viewHolder).txtPvAValue.setText(" " + Math.round(productNameBean.getPvaAchieved()) + "%");


            } else if (fromwhere.equals("Category")) {

                ((SalesViewHolder) viewHolder).nameTv.setText(productNameBean.getPlanCategory());
                ((SalesViewHolder) viewHolder).txtPvAValue.setText("" + Math.round(productNameBean.getPvaAchieved()) + "%");


            } else if (fromwhere.equals("Plan Class")) {

                ((SalesViewHolder) viewHolder).nameTv.setText(productNameBean.getPlanClass());
                ((SalesViewHolder) viewHolder).txtPvAValue.setText("" + Math.round(productNameBean.getPvaAchieved()) + "%");

            } else if (fromwhere.equals("Brand")) {

                ((SalesViewHolder) viewHolder).nameTv.setText(productNameBean.getBrandName());
                ((SalesViewHolder) viewHolder).txtPvAValue.setText("" + Math.round(productNameBean.getPvaAchieved()) + "%");

            } else if (fromwhere.equals("Brand Plan Class")) {

                ((SalesViewHolder) viewHolder).nameTv.setText(productNameBean.getBrandplanClass());
                ((SalesViewHolder) viewHolder).txtPvAValue.setText("" + Math.round(productNameBean.getPvaAchieved()) + "%");

            }

            double singlePercVal = 0.5;//50/100;// width divide by 100 perc

            int planVal = 100; // planned value from API
            double achieveVal = productNameBean.getPvaAchieved();// Achieved value from API

            double calplanVal = planVal * singlePercVal; // planned value multiplied by single perc value
            double calachieveVal = achieveVal * singlePercVal; // Achieved value multiplied by single perc value

//        int planvalueinpx = convertSpToPixels(calplanVal, context); //converting value from sp to px
//        int achievevalueinpx = convertSpToPixels(calachieveVal, context); //converting value from sp to px

            float density = context.getResources().getDisplayMetrics().density;

            int finalCalplanVal = (int) (density * calplanVal); //converting value from px to dp
            //Log.e("", "==finalCalplanVal= " + finalCalplanVal);
            int finalCalachieveVal = (int) (density * calachieveVal); //converting value from px to dp
            // Log.e("", "==finalCalachieveVal= " + finalCalachieveVal);


            ((SalesViewHolder) viewHolder).txtPlan.setWidth(finalCalachieveVal);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(3, 24);
            params.setMargins(finalCalplanVal, 0, 0, 0);
            ((SalesViewHolder) viewHolder).txtAchieve.setLayoutParams(params);


            if (achieveVal < 70) {
                ((SalesViewHolder) viewHolder).txtPlan.setBackgroundColor(Color.RED);
            } else if (achieveVal > 90) {
                ((SalesViewHolder) viewHolder).txtPlan.setBackgroundColor(Color.GREEN);//yellow
            } else {
                ((SalesViewHolder) viewHolder).txtPlan.setBackgroundColor(Color.parseColor("#ff7e00"));
            }
        }
//        else
//        {
//           // ((ProgressViewHolder) viewHolder).txtView.setText(" ");
//        }

  }


    public static class SalesViewHolder extends RecyclerView.ViewHolder {


        TextView nameTv;
        RelativeLayout rel;
        RelativeLayout innerrel, relValue;
        TextView txtPlan, txtValue,txtPvAValue;
        TextView txtAchieve;

        public SalesViewHolder(View itemView) {
            super(itemView);

           nameTv = (TextView) itemView.findViewById(R.id.txtVal);
           rel = (RelativeLayout) itemView.findViewById(R.id.rel);
            innerrel = (RelativeLayout) itemView.findViewById(R.id.innerrellay);
            relValue = (RelativeLayout) itemView.findViewById(R.id.relValue);
           txtPlan = (TextView) itemView.findViewById(R.id.txtPlan);
           txtValue = (TextView) itemView.findViewById(R.id.txtValue);
            txtAchieve = (TextView) itemView.findViewById(R.id.txtAchieve);
            txtPvAValue =(TextView) itemView.findViewById(R.id.txtPvAValue);

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
          //  txtView = (TextView)footerView.findViewById(R.id.txtView);
        }
    }

}