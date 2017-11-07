package apsupportapp.aperotechnologies.com.designapp.InfantApp.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.InfantApp.ProductDetails;
import apsupportapp.aperotechnologies.com.designapp.InfantApp.SubCategory;
import apsupportapp.aperotechnologies.com.designapp.R;

import static apsupportapp.aperotechnologies.com.designapp.InfantApp.BabyCerealActivity.gridView;
import static apsupportapp.aperotechnologies.com.designapp.R.id.gridview;
import static apsupportapp.aperotechnologies.com.designapp.R.id.linear_snap;

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
            final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linear_snap);
            viewHolder = new CategoriesHolder(view);

            ViewTreeObserver vto = gridView.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        gridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    int height = gridView.getMeasuredHeight();
                    Log.e("height "," "+height);
                    linearLayout.getLayoutParams().height = height/2;
                    linearLayout.requestLayout();//09033615500


                }
            });



        }
        else if(viewString.equals("listView"))
        {
            view = inflater.inflate(R.layout.custom_list, parent, false);
            final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.custom_list);
            viewHolder = new CategoriesHolder(view);

            ViewTreeObserver vto = gridView.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        gridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    int height = gridView.getMeasuredHeight();
                    Log.e("height "," "+height);
                    linearLayout.getLayoutParams().height = height;
                    linearLayout.requestLayout();


                }
            });
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof CategoriesHolder) {
            ((CategoriesHolder) holder).txt_desc.setText(R.string.grid_text);
            ((CategoriesHolder) holder).textView1.setText(R.string.grid_text1);
            ((CategoriesHolder) holder).textView2.setText(R.string.grid_text2);

//            ((CategoriesHolder) holder).img_categories.setImageResource(R.mipmap.placeholder);


            Glide.with(mContext)
                    .load("http://res.cloudinary.com/viintro/image/upload/v1509701631/v76ykp64hw3zwjckyzpp.jpg")
                    .into(((CategoriesHolder) holder).img_categories);

            ((CategoriesHolder) holder).bst_cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(mContext,ProductDetails.class);
                    CardView sharedView = (CardView) v;
                    String transitionName = "details";

                    ActivityOptions transitionActivityOptions = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, sharedView, transitionName);
                        mContext.startActivity(intent, transitionActivityOptions.toBundle());
                    }else{

                        mContext.startActivity(intent);
                    }
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
        public LinearLayout linear_snap;


        public CategoriesHolder(View v) {
            super(v);
            img_categories = (ImageView) v.findViewById(R.id.grid_image);
            txt_desc = (TextView) v.findViewById(R.id.grid_text);
            textView1 = (TextView) v.findViewById(R.id.textView1);
            textView2 = (TextView) v.findViewById(R.id.textView2);
            bst_cardView = (CardView) v.findViewById(R.id.bst_cardView);
            linear_snap = (LinearLayout) v.findViewById(R.id.linear_snap);


        }

    }


}
