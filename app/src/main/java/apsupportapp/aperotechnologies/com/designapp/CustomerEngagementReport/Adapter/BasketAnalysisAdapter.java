package apsupportapp.aperotechnologies.com.designapp.CustomerEngagementReport.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import apsupportapp.aperotechnologies.com.designapp.CustomerEngagementReport.CustDetailsActivity;
import apsupportapp.aperotechnologies.com.designapp.InfantApp.Adapter.FragmentCategoriesAdapter;
import apsupportapp.aperotechnologies.com.designapp.InfantApp.SubCategory;
import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by pamrutkar on 08/11/17.
 */

public class BasketAnalysisAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private RecyclerView listCategories;


    public BasketAnalysisAdapter(Context context, RecyclerView listCategories) {
        this.context = context;
        this.listCategories = listCategories;


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v0 = inflater.inflate(R.layout.adapter_basket_analysis, parent, false);
        viewHolder =new CategoriesHolder(v0);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

       CategoriesHolder categoriesHolder = (BasketAnalysisAdapter.CategoriesHolder) holder;
        categoriesHolder.cf_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "position  "+position,Toast.LENGTH_SHORT).show();
                Intent cust_details = new Intent(context, CustDetailsActivity.class);
                context.startActivity(cust_details);
            }
        });

    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class CategoriesHolder extends RecyclerView.ViewHolder {

        TextView txt_bandVal,txt_band_percentage,txt_band_range;
        CardView cf_cardView;


        public CategoriesHolder(View v) {
            super(v);
            txt_bandVal = (TextView) v.findViewById(R.id.txt_bandVal);
            txt_band_percentage = (TextView) v.findViewById(R.id.txt_band_percentage);
            txt_band_range = (TextView) v.findViewById(R.id.txt_band_range);
            cf_cardView = (CardView) v.findViewById(R.id.cer_cardView);

        }


    }

}