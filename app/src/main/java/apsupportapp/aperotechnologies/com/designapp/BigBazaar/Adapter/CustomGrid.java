package apsupportapp.aperotechnologies.com.designapp.BigBazaar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.BigBazaar.ProductDetails;
import apsupportapp.aperotechnologies.com.designapp.PvaSalesAnalysis.PvASnapAdapter;
import apsupportapp.aperotechnologies.com.designapp.R;

import static apsupportapp.aperotechnologies.com.designapp.R.id.bst_cardView;

/**
 * Created by pamrutkar on 02/11/17.
 */
public class CustomGrid extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<String> listProducts;
    private String viewString;

    public CustomGrid(Context context, ArrayList<String> listProducts, String viewString) {
        this.mContext = context;
        this.listProducts =  listProducts;
        this.viewString = viewString;
        Log.e("CustomGrid: ",""+viewString);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if(viewString.equals("gridView"))
        {
            view = inflater.inflate(R.layout.custom_grid, parent, false);
            viewHolder = new CategoriesHolder(view);
        }
        else if(viewString.equals("listView"))
        {
            view = inflater.inflate(R.layout.custom_list, parent, false);
            viewHolder = new CategoriesHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof CategoriesHolder) {
            ((CategoriesHolder) holder).txt_desc.setText(R.string.grid_text);
            ((CategoriesHolder) holder).textView1.setText(R.string.grid_text1);
            ((CategoriesHolder) holder).textView2.setText(R.string.grid_text2);

            ((CategoriesHolder) holder).img_categories.setImageResource(R.mipmap.placeholder);
            ((CategoriesHolder) holder).bst_cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(mContext,ProductDetails.class);
                    mContext.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class CategoriesHolder extends RecyclerView.ViewHolder {

        private ImageView img_categories;
        private TextView txt_desc,textView1,textView2;
        CardView bst_cardView;


        public CategoriesHolder(View v) {
            super(v);
            img_categories = (ImageView) v.findViewById(R.id.grid_image);
            txt_desc = (TextView) v.findViewById(R.id.grid_text);
            textView1 = (TextView) v.findViewById(R.id.textView1);
            textView2 = (TextView) v.findViewById(R.id.textView2);
            bst_cardView = (CardView) v.findViewById(R.id.bst_cardView);

        }


    }

}
