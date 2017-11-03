package apsupportapp.aperotechnologies.com.designapp.BigBazaar;

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

import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by rkanawade on 02/11/17.
 */

public class FragmentCategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private RecyclerView listCategories;


    public FragmentCategoriesAdapter(Context context, RecyclerView listCategories) {
        this.context = context;
        this.listCategories = listCategories;


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v0 = inflater.inflate(R.layout.adapter_categories_fragment, parent, false);
        viewHolder = new CategoriesHolder(v0);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        CategoriesHolder categoriesHolder = (CategoriesHolder) holder;
        categoriesHolder.cf_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(context, "position  "+position,Toast.LENGTH_SHORT).show();
//                Intent prod_details = new Intent(context, ProductDetailsActivity.class);
//                context.startActivity(prod_details);
            }
        });
        if(position == 0){
            categoriesHolder.txt_desc.setText("Nutritional Needs");
            categoriesHolder.img_categories.setImageResource(R.mipmap.mi_max2_buy_now_app);
        }
        else if(position == 1){
            categoriesHolder.txt_desc.setText("Diapering Needs");
            categoriesHolder.img_categories.setImageResource(R.mipmap.moto_e4_app);
        }
        else if(position == 2){
            categoriesHolder.txt_desc.setText("Baby Travel");
            categoriesHolder.img_categories.setImageResource(R.mipmap.koryo_speaker_21_mb_app);
        }
        else if(position == 3){
            categoriesHolder.txt_desc.setText("Baby Bathtime");
            categoriesHolder.img_categories.setImageResource(R.mipmap.samsung_j7_max_original);
        }
        else if(position == 4){
            categoriesHolder.txt_desc.setText("Baby Playtime");
            categoriesHolder.img_categories.setImageResource(R.mipmap.mi_max2_buy_now_app);
        }

    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class CategoriesHolder extends RecyclerView.ViewHolder {

        ImageView img_categories;
        TextView txt_desc;
        CardView cf_cardView;


        public CategoriesHolder(View v) {
            super(v);
            img_categories = (ImageView) v.findViewById(R.id.img_categories);
            txt_desc = (TextView) v.findViewById(R.id.txt_desc);
            cf_cardView = (CardView) v.findViewById(R.id.cf_cardView);

        }


    }

}

/*
public class FragmentCategoriesAdapter extends BaseAdapter{

    private Context context;
    LayoutInflater inflater;
    private RecyclerView listCategories;


    public FragmentCategoriesAdapter(Context context, RecyclerView listCategories) {
        this.context = context;
        this.listCategories = listCategories;

        inflater = LayoutInflater.from(this.context);

    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CategoriesHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_categories_fragment, parent, false);
            mViewHolder = new CategoriesHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (CategoriesHolder) convertView.getTag();
        }


        return convertView;
    }


    private class CategoriesHolder {

        ImageView img_categories;
        TextView txt_desc;


        public CategoriesHolder(View v) {

            img_categories = (ImageView) v.findViewById(R.id.img_categories);
         //   txt_desc = (TextView) v.findViewById(R.id.txt_desc);

        }


    }

}



 */

