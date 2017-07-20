package apsupportapp.aperotechnologies.com.designapp.BestPerformersPromo;

/**
 * Created by csuthar on 18/11/16.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;

public class BestPromoAdapter extends BaseAdapter {

    private ArrayList<RunningPromoListDisplay> arrayList;

    private LayoutInflater mInflater;
    private Context context;
    private int Position;



    public BestPromoAdapter(ArrayList<RunningPromoListDisplay> arrayList, Context context) {

        this.arrayList = arrayList;
        this.context = context;
        mInflater = LayoutInflater.from(context);

    }

    //How many items are in the data set represented by this Adapter.
    @Override
    public int getCount() {
        return arrayList.size();
    }

    //Get the data item associated with the specified position in the data set.
    @Override
    public Object getItem(int position) {

        return arrayList.get(position);
    }

    //Get the row id associated with the specified position in the list.
    @Override
    public long getItemId(int position) {

        return position;
    }

    //Get a View that displays the data at the specified position in the data set.
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Position=position;
        final Holder holder;
        if (convertView == null) {
            holder=new Holder();
            convertView = mInflater.inflate(R.layout.activity_best_performer_child, null);
            holder.PromotionName = (TextView) convertView.findViewById(R.id.bst_head_child);
            holder.Bst_PromoValues_child = (TextView) convertView.findViewById(R.id.bst_PromoValues_child);
            holder.Bst_PromoValuesU_child = (TextView) convertView.findViewById(R.id.bst_PromoValuesU_child);
            holder.ProgressPicaso = (ProgressBar) convertView.findViewById(R.id.progressPicaso);
            holder.Bst_image_child = (ImageView) convertView.findViewById(R.id.bst_image_child);
            convertView.setTag(holder);

        } else {
            holder=(Holder)convertView.getTag();
        }
        holder.PromotionName.setText(arrayList.get(position).getPromoDesc());

        NumberFormat format  = NumberFormat.getNumberInstance(new Locale("", "in"));

        holder.Bst_PromoValues_child.setText("₹\t"+format.format(Math.round(arrayList.get(position).getDurSaleNetVal())));
        holder.Bst_PromoValuesU_child.setText(""+arrayList.get(position).getDurSaleTotQty());
        return convertView;
    }

    private class Holder {

        TextView PromotionName,Bst_PromoValues_child,Bst_PromoValuesU_child;
        ImageView Bst_image_child;
        public ProgressBar ProgressPicaso;
    }
}
