package apsupportapp.aperotechnologies.com.designapp.OptionEfficiency;

/**
 * Created by csuthar on 24/01/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import apsupportapp.aperotechnologies.com.designapp.model.OptionEfficiencyDetails;


public class OptionIndexSnapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements GravitySnapHelper.SnapListener {

    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;
    private final ArrayList<OptionEfficiencyDetails> OptionIndexDetailsArrayList;
    private final String[] hierarchyList;
    RecyclerView listViewFIndex;
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

    public OptionIndexSnapAdapter(ArrayList<OptionEfficiencyDetails> OptionIndexDetailsArrayList, Context context, String fromWhere, RecyclerView listViewFIndex,String[] hierarchyList) {

        this.context = context;
        this.OptionIndexDetailsArrayList = OptionIndexDetailsArrayList;
        this.fromWhere = fromWhere;
        this.listViewFIndex = listViewFIndex;
        this.hierarchyList = hierarchyList;

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
        return position != OptionIndexDetailsArrayList.size();
    }

    @Override
    public int getItemCount() {
        return OptionIndexDetailsArrayList.size() + 1;
    }

    @Override
    public void onSnap(int position) {
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.oefficiency_list_row, parent, false);
            return new OptionHolder(v);
        } else if (viewType == VIEW_PROG) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_footer, parent, false);
            return new ProgressViewHolder(v);
        }

        return null;

    }


    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof OptionHolder) {
            if (position < OptionIndexDetailsArrayList.size()) {
                OptionEfficiencyDetails optionEfficiencyDetails = OptionIndexDetailsArrayList.get(position);
                NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("", "in"));

                if(fromWhere.toString().equals(hierarchyList[0])){

                    ((OptionHolder) viewHolder).oe_txtPlanClass.setText(optionEfficiencyDetails.getPlanDept());
                    ((OptionHolder) viewHolder).oe_txtOption.setText("" + formatter.format(optionEfficiencyDetails.getOptionCount()));
                    ((OptionHolder) viewHolder).oe_txtOption_Perc.setText("" + String.format("%.1f", optionEfficiencyDetails.getFullSizeCount()));
                    ((OptionHolder) viewHolder).oe_txtSOH_U.setText("" + formatter.format(Math.round(optionEfficiencyDetails.getStkOnhandQty())));
                    ((OptionHolder) viewHolder).oe_txtSOH_Prec.setText(" " + String.format("%.1f", optionEfficiencyDetails.getSohCountFullSize()));

                }
                else if(fromWhere.toString().equals(hierarchyList[1])){

                    ((OptionHolder) viewHolder).oe_txtPlanClass.setText(optionEfficiencyDetails.getPlanCategory());
                    ((OptionHolder) viewHolder).oe_txtOption.setText("" + formatter.format(optionEfficiencyDetails.getOptionCount()));
                    ((OptionHolder) viewHolder).oe_txtOption_Perc.setText("" + String.format("%.1f", optionEfficiencyDetails.getFullSizeCount()));
                    ((OptionHolder) viewHolder).oe_txtSOH_U.setText("" + formatter.format(Math.round(optionEfficiencyDetails.getStkOnhandQty())));
                    ((OptionHolder) viewHolder).oe_txtSOH_Prec.setText(" " + String.format("%.1f", optionEfficiencyDetails.getSohCountFullSize()));

                }
                else if(fromWhere.toString().equals(hierarchyList[2])){

                    ((OptionHolder) viewHolder).oe_txtPlanClass.setText(optionEfficiencyDetails.getPlanClass());
                    ((OptionHolder) viewHolder).oe_txtOption.setText("" + formatter.format(optionEfficiencyDetails.getOptionCount()));
                    ((OptionHolder) viewHolder).oe_txtOption_Perc.setText("" + String.format("%.1f", optionEfficiencyDetails.getFullSizeCount()));
                    ((OptionHolder) viewHolder).oe_txtSOH_U.setText("" + formatter.format(Math.round(optionEfficiencyDetails.getStkOnhandQty())));
                    ((OptionHolder) viewHolder).oe_txtSOH_Prec.setText(" " + String.format("%.1f", optionEfficiencyDetails.getSohCountFullSize()));

                }
                else if(fromWhere.toString().equals(hierarchyList[3])){

                    ((OptionHolder) viewHolder).oe_txtPlanClass.setText(optionEfficiencyDetails.getBrandName());
                    ((OptionHolder) viewHolder).oe_txtOption.setText("" + formatter.format(optionEfficiencyDetails.getOptionCount()));
                    ((OptionHolder) viewHolder).oe_txtOption_Perc.setText("" + String.format("%.1f", optionEfficiencyDetails.getFullSizeCount()));
                    ((OptionHolder) viewHolder).oe_txtSOH_U.setText("" + formatter.format(Math.round(optionEfficiencyDetails.getStkOnhandQty())));
                    ((OptionHolder) viewHolder).oe_txtSOH_Prec.setText(" " + String.format("%.1f", optionEfficiencyDetails.getSohCountFullSize()));

                }
                else if(fromWhere.toString().equals(hierarchyList[4])){

                    ((OptionHolder) viewHolder).oe_txtPlanClass.setText(optionEfficiencyDetails.getBrandplanClass());
                    ((OptionHolder) viewHolder).oe_txtOption.setText("" + formatter.format(optionEfficiencyDetails.getOptionCount()));
                    ((OptionHolder) viewHolder).oe_txtOption_Perc.setText("" + String.format("%.1f", optionEfficiencyDetails.getFullSizeCount()));
                    ((OptionHolder) viewHolder).oe_txtSOH_U.setText("" + formatter.format(Math.round(optionEfficiencyDetails.getStkOnhandQty())));
                    ((OptionHolder) viewHolder).oe_txtSOH_Prec.setText(" " + String.format("%.1f", optionEfficiencyDetails.getSohCountFullSize()));

                }



              /*  switch (fromWhere) {
                    case "Department":

                        ((OptionHolder) viewHolder).oe_txtPlanClass.setText(optionEfficiencyDetails.getPlanDept());
                        ((OptionHolder) viewHolder).oe_txtOption.setText("" + formatter.format(optionEfficiencyDetails.getOptionCount()));
                        ((OptionHolder) viewHolder).oe_txtOption_Perc.setText("" + String.format("%.1f", optionEfficiencyDetails.getFullSizeCount()));
                        ((OptionHolder) viewHolder).oe_txtSOH_U.setText("" + formatter.format(Math.round(optionEfficiencyDetails.getStkOnhandQty())));
                        ((OptionHolder) viewHolder).oe_txtSOH_Prec.setText(" " + String.format("%.1f", optionEfficiencyDetails.getSohCountFullSize()));


                        break;
                    case "Category":

                        ((OptionHolder) viewHolder).oe_txtPlanClass.setText(optionEfficiencyDetails.getPlanCategory());
                        ((OptionHolder) viewHolder).oe_txtOption.setText("" + formatter.format(optionEfficiencyDetails.getOptionCount()));
                        ((OptionHolder) viewHolder).oe_txtOption_Perc.setText("" + String.format("%.1f", optionEfficiencyDetails.getFullSizeCount()));
                        ((OptionHolder) viewHolder).oe_txtSOH_U.setText("" + formatter.format(Math.round(optionEfficiencyDetails.getStkOnhandQty())));
                        ((OptionHolder) viewHolder).oe_txtSOH_Prec.setText(" " + String.format("%.1f", optionEfficiencyDetails.getSohCountFullSize()));

                        break;
                    case "Class":

                        ((OptionHolder) viewHolder).oe_txtPlanClass.setText(optionEfficiencyDetails.getPlanClass());
                        ((OptionHolder) viewHolder).oe_txtOption.setText("" + formatter.format(optionEfficiencyDetails.getOptionCount()));
                        ((OptionHolder) viewHolder).oe_txtOption_Perc.setText("" + String.format("%.1f", optionEfficiencyDetails.getFullSizeCount()));
                        ((OptionHolder) viewHolder).oe_txtSOH_U.setText("" + formatter.format(Math.round(optionEfficiencyDetails.getStkOnhandQty())));
                        ((OptionHolder) viewHolder).oe_txtSOH_Prec.setText(" " + String.format("%.1f", optionEfficiencyDetails.getSohCountFullSize()));


                        break;
                    case "Brand":

                        ((OptionHolder) viewHolder).oe_txtPlanClass.setText(optionEfficiencyDetails.getBrandName());
                        ((OptionHolder) viewHolder).oe_txtOption.setText("" + formatter.format(optionEfficiencyDetails.getOptionCount()));
                        ((OptionHolder) viewHolder).oe_txtOption_Perc.setText("" + String.format("%.1f", optionEfficiencyDetails.getFullSizeCount()));
                        ((OptionHolder) viewHolder).oe_txtSOH_U.setText("" + formatter.format(Math.round(optionEfficiencyDetails.getStkOnhandQty())));
                        ((OptionHolder) viewHolder).oe_txtSOH_Prec.setText(" " + String.format("%.1f", optionEfficiencyDetails.getSohCountFullSize()));


                        break;
                    case "Brand Class":

                        ((OptionHolder) viewHolder).oe_txtPlanClass.setText(optionEfficiencyDetails.getBrandplanClass());
                        ((OptionHolder) viewHolder).oe_txtOption.setText("" + formatter.format(optionEfficiencyDetails.getOptionCount()));
                        ((OptionHolder) viewHolder).oe_txtOption_Perc.setText("" + String.format("%.1f", optionEfficiencyDetails.getFullSizeCount()));
                        ((OptionHolder) viewHolder).oe_txtSOH_U.setText("" + formatter.format(Math.round(optionEfficiencyDetails.getStkOnhandQty())));
                        ((OptionHolder) viewHolder).oe_txtSOH_Prec.setText(" " + String.format("%.1f", optionEfficiencyDetails.getSohCountFullSize()));
                        break;
                }*/
            }
        }

    }


    public static class OptionHolder extends RecyclerView.ViewHolder {


        TextView oe_txtPlanClass, oe_txtSOH_U, oe_txtSOH_Prec, oe_txtOption, oe_txtOption_Perc;


        public OptionHolder(View itemView) {
            super(itemView);

            oe_txtPlanClass = (TextView) itemView.findViewById(R.id.oe_txtPlanClass);
            oe_txtSOH_U = (TextView) itemView.findViewById(R.id.oe_txtSOH_U);
            oe_txtSOH_Prec = (TextView) itemView.findViewById(R.id.oe_txtSOH_Prec);
            oe_txtOption = (TextView) itemView.findViewById(R.id.oe_txtOption);
            oe_txtOption_Perc = (TextView) itemView.findViewById(R.id.oe_txtOption_Perc);

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