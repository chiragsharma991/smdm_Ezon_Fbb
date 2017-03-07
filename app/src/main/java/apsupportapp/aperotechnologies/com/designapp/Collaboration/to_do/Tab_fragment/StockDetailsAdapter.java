package apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.ToDo_Modal;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.RecyclerItemClickListener;

/**
 * Created by csuthar on 06/03/17.
 */

public class StockDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final Context context;
    private final ArrayList<ToDo_Modal> list;
    private static boolean check=false;
    public  boolean[] Toggle;



    public StockDetailsAdapter(ArrayList<ToDo_Modal> list, Context context) {
        this.list=list;
        this.context=context;//
        Toggle= new boolean[list.size()];

 }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_stock_details_child, parent, false);
        return new StockDetailsAdapter.Holder(v);    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof StockDetailsAdapter.Holder) {
            if(position < list.size()) {

                // holder.snapTextView.setText(snap.getText());


                ((StockDetailsAdapter.Holder)holder).Detail_Soh.setText(""+Math.round(list.get(position).getStkOnhandQty()));
                ((StockDetailsAdapter.Holder)holder).Detail_optionLevel.setText(list.get(position).getLevel());
                ((StockDetailsAdapter.Holder)holder).Detail_reqQty.setText(""+list.get(position).getStkOnhandQtyRequested());
                ((StockDetailsAdapter.Holder)holder).Detail_Git.setText(""+Math.round(list.get(position).getStkGitQty()));
                ((StockDetailsAdapter.Holder)holder).Detail_AviQty.setText(""+list.get(position).getStkQtyAvl());
                ((StockDetailsAdapter.Holder)holder).Detail_optionLevel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("TAG", "onClick:>>>> "+position );

                        if(Toggle[position])
                        {
                            ((StockDetailsAdapter.Holder)holder).Sizeslayout.setVisibility(View.VISIBLE);



                        }else
                        {
                            ((StockDetailsAdapter.Holder)holder).Sizeslayout.setVisibility(View.GONE);

                        }


//                        if(Toggle[position]==true)
//                        {
//                            Toggle[position]=false;
//                            notifyDataSetChanged();
//                        }else
//                        {
//                            Toggle[position]=true;
//
//                            LayoutInflater layoutInflater = (LayoutInflater)context.getApplicationContext()
//                                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
//                            ViewGroup layout = (ViewGroup) layoutInflater.inflate(R.layout.details_header_child, null);
//                            TextView txt = new TextView(context);
//                            txt.setId(position);
//                            Log.e("clicked Position",""+txt.getId() + "option click position :"+((StockDetailsAdapter.Holder)holder).Detail_optionLevel.getId());
//                            txt.setText("Welcome");
//                            layout.addView(txt);
//                            ((StockDetailsAdapter.Holder)holder).detailsLinear.addView(layout);
//                            notifyDataSetChanged();
//
//                        }
                    }
                });
            }
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    private static class Holder extends RecyclerView.ViewHolder {


        private final TextView Detail_Soh,Detail_optionLevel,Detail_reqQty,Detail_Git,Detail_AviQty;
        private LinearLayout Sizeslayout,detailsLinear;
        public Holder(View itemView)
        {
            super(itemView);
            Detail_optionLevel=(TextView)itemView.findViewById(R.id.detail_optionLevel);
            Detail_reqQty=(TextView)itemView.findViewById(R.id.detail_reqQty);
            Detail_Soh=(TextView)itemView.findViewById(R.id.detail_Soh);
            Detail_Git=(TextView)itemView.findViewById(R.id.detail_Git);
            Detail_AviQty=(TextView)itemView.findViewById(R.id.detail_AviQty);
            Sizeslayout=(LinearLayout)itemView.findViewById(R.id.detail_size);
            detailsLinear=(LinearLayout)itemView.findViewById(R.id.details_headerChild);

        }

    }


}
