package apsupportapp.aperotechnologies.com.designapp.SalesAnalysis;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.google.gson.Gson;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisListDisplay;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisViewPagerValue;

/**
 * Created by pamrutkar on 30/05/17.
 */
public class EzoneSalesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements GravitySnapHelper.SnapListener {

    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;
    ArrayList<SalesAnalysisViewPagerValue> ez_sales_header_array;
    EzoneSalesPagerAdapter ezone_pageradapter;
    SalesAnalysisListDisplay ezone_sales_details;
    int focusposition, selFirstPositionValue;
    RecyclerView rv_ez_sales;
    int currentIndex;
    int offsetvalue = 0, count = 0, limit = 100;
    private LayoutInflater mInflater;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 2;
    Context context;
    String fromwhere;
    int level;
    Gson gson;


    public ArrayList<SalesAnalysisListDisplay> mSnaps;
    // Disable touch detection for parent recyclerView if we use vertical nested recyclerViews


    public EzoneSalesAdapter(ArrayList<SalesAnalysisListDisplay> arrayList, Context context, int currentIndex, String fromwhere, RecyclerView listView_SalesAnalysis) {

        this.mSnaps = arrayList;
        this.context = context;
        this.fromwhere = fromwhere;
        this.rv_ez_sales = listView_SalesAnalysis;
        mInflater = LayoutInflater.from(context);
        this.currentIndex = currentIndex;
        level = 1;
        focusposition = 0;
        selFirstPositionValue = 0;
        ez_sales_header_array = new ArrayList<SalesAnalysisViewPagerValue>();
        gson = new Gson();
    }


    public EzoneSalesAdapter() {
        mSnaps = new ArrayList<>();
    }

    public void addSnap(SalesAnalysisListDisplay snap) {
        mSnaps.add(snap);
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
        return position != mSnaps.size();
    }

    @Override
    public int getItemCount() {
        return mSnaps.size() + 1;
    }

    @Override
    public void onSnap(int position) {
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_ezone_sales_recycleview, parent, false);
            return new EzoneSalesAdapter.EzoneSalesViewHolder(v);
        } else if (viewType == VIEW_PROG) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_footer, parent, false);
            return new EzoneSalesAdapter.ProgressViewHolder(v);
        }

        return null;

    }


    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {

        if (viewHolder instanceof EzoneSalesAdapter.EzoneSalesViewHolder) {
            if (position < mSnaps.size()) {

                SalesAnalysisListDisplay productNameBean = mSnaps.get(position);

                switch (fromwhere) {
                    case "Department":

                        ((EzoneSalesAdapter.EzoneSalesViewHolder) viewHolder).txt_ez_name.setText(productNameBean.getLevel());
                        ((EzoneSalesAdapter.EzoneSalesViewHolder) viewHolder).txt_ez_sPvAValue.setText(" " + Math.round(productNameBean.getPvaAchieved()) + "%");

                        break;
                    case "Subdept":

                        ((EzoneSalesAdapter.EzoneSalesViewHolder) viewHolder).txt_ez_name.setText(productNameBean.getLevel());
                        ((EzoneSalesAdapter.EzoneSalesViewHolder) viewHolder).txt_ez_sPvAValue.setText("" + Math.round(productNameBean.getPvaAchieved()) + "%");

                        break;
                    case "Class":

                        ((EzoneSalesAdapter.EzoneSalesViewHolder) viewHolder).txt_ez_name.setText(productNameBean.getLevel());
                        ((EzoneSalesAdapter.EzoneSalesViewHolder) viewHolder).txt_ez_sPvAValue.setText("" + Math.round(productNameBean.getPvaAchieved()) + "%");

                        break;
                    case "Subclass":

                        ((EzoneSalesAdapter.EzoneSalesViewHolder) viewHolder).txt_ez_name.setText(productNameBean.getLevel());
                        ((EzoneSalesAdapter.EzoneSalesViewHolder) viewHolder).txt_ez_sPvAValue.setText("" + Math.round(productNameBean.getPvaAchieved()) + "%");

                        break;
                    case "MC":

                        ((EzoneSalesAdapter.EzoneSalesViewHolder) viewHolder).txt_ez_name.setText(productNameBean.getLevel());
                        ((EzoneSalesAdapter.EzoneSalesViewHolder) viewHolder).txt_ez_sPvAValue.setText("" + Math.round(productNameBean.getPvaAchieved()) + "%");
                        break;
                }

                double singlePercVal = 0.5;//50/100;// width divide by 100 perc
                int planVal = 100; // planned value from API
                double achieveVal = productNameBean.getPvaAchieved();// Achieved value from API
                double calplanVal = planVal * singlePercVal; // planned value multiplied by single perc value
                double calachieveVal = achieveVal * singlePercVal; // Achieved value multiplied by single perc value


                float density = context.getResources().getDisplayMetrics().density;
                int finalCalplanVal = (int) (density * calplanVal); //converting value from px to dp
                int finalCalachieveVal = (int) (density * calachieveVal); //converting value from px to dp
                ((EzoneSalesAdapter.EzoneSalesViewHolder) viewHolder).txt_ez_sPlan.setWidth(finalCalachieveVal);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(3, 24);
                params.setMargins(finalCalplanVal, 0, 0, 0);
                ((EzoneSalesAdapter.EzoneSalesViewHolder) viewHolder).txt_ez_sAchieve.setLayoutParams(params);


                if (achieveVal < 70) {
                    ((EzoneSalesAdapter.EzoneSalesViewHolder) viewHolder).txt_ez_sPlan.setBackgroundColor(Color.RED);
                } else if (achieveVal > 90) {
                    ((EzoneSalesAdapter.EzoneSalesViewHolder) viewHolder).txt_ez_sPlan.setBackgroundColor(Color.GREEN);//yellow
                } else {
                    ((EzoneSalesAdapter.EzoneSalesViewHolder) viewHolder).txt_ez_sPlan.setBackgroundColor(Color.parseColor("#ff7e00"));
                }
            }
        }

    }

    public class EzoneSalesViewHolder extends RecyclerView.ViewHolder {


        TextView txt_ez_name,txt_ez_sPlan, txt_ez_sPvAValue,txt_ez_sAchieve;
        RelativeLayout rel_ez_outer,rel_ez_inner;

        public EzoneSalesViewHolder(View itemView) {
            super(itemView);

            txt_ez_name = (TextView) itemView.findViewById(R.id.txt_ez_sName);
            rel_ez_outer = (RelativeLayout) itemView.findViewById(R.id.rel_ez_outer);
            rel_ez_inner = (RelativeLayout) itemView.findViewById(R.id.rel_ez_inner);
            txt_ez_sPlan = (TextView) itemView.findViewById(R.id.txt_ez_sPlan);
            txt_ez_sAchieve = (TextView) itemView.findViewById(R.id.txt_ez_sAchieve);
            txt_ez_sPvAValue = (TextView) itemView.findViewById(R.id.txt_ez_sPvAValue);

        }
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {

        TextView txtView;

        public ProgressViewHolder(View footerView) {
            super(footerView);
        }
    }

}

