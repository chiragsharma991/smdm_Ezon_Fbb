package apsupportapp.aperotechnologies.com.designapp.StoreInspection;

import android.content.Context;
import android.content.Intent;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;

import apsupportapp.aperotechnologies.com.designapp.R;



/**
 * Created by pamrutkar on 18/04/17.
 */
public class Insp_History_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private ArrayList<InspectionBeanClass> list;
    String store_Code;


    public Insp_History_Adapter(ArrayList<InspectionBeanClass> list, Context context, String store_Code)
    {
        this.list = list;  //main adapter
        this.context = context;//
        this.store_Code = store_Code;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_insp_history_child, parent, false);
        return new Insp_History_Adapter.Holder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

         if (holder instanceof Insp_History_Adapter.Holder) {
            if (position < list.size())
            {
                ((Insp_History_Adapter.Holder) holder).txt_inspected_by.setText( list.get(position).getInspectorName());
                ((Insp_History_Adapter.Holder) holder).txt_inspection_id.setText(""+list.get(position).getInspectionId());
                ((Insp_History_Adapter.Holder) holder).txt_store_code.setText("" + list.get(position).getStoreCode()+" "+list.get(position).getStoreDesc());

                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
                String inputDateStr=list.get(position).getInspectionDate();
                Date date = null;
                try {
                    date = inputFormat.parse(inputDateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String outputDateStr = outputFormat.format(date);

                ((Insp_History_Adapter.Holder) holder).txt_date.setText("" + outputDateStr);
                Glide.with(context)
                        .load(list.get(position).getStoreImg())
                        .fitCenter()
                        .into(((Insp_History_Adapter.Holder) holder).image_insphistory_1);

                if(list.get(position).getRating().equals("Okay"))
                {
                    Glide.with(context)
                            .load(R.mipmap.okayemojiselected)
                            .fitCenter()
                            .into(((Insp_History_Adapter.Holder) holder).image_emoji);

                }
                else if(list.get(position).getRating().equals("Need Improvement"))
                {
                    Glide.with(context)
                            .load(R.mipmap.improvementemojiselected)
                            .fitCenter()
                            .into(((Insp_History_Adapter.Holder) holder).image_emoji);

                }
                else if(list.get(position).getRating().equals("Excellent"))
                {
                    Glide.with(context)
                            .load(R.mipmap.excellentemojiselected)
                            .fitCenter()
                            .into(((Insp_History_Adapter.Holder) holder).image_emoji);

                }
                else if(list.get(position).getRating().equals("Good"))
                {
                    Glide.with(context)
                            .load(R.mipmap.goodemojiselected)
                            .fitCenter()
                            .into(((Insp_History_Adapter.Holder) holder).image_emoji);

                }
                ((Insp_History_Adapter.Holder) holder).relative_insp_summary.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {

                        Intent intent = new Intent(context ,InspectionDetailsActivity.class);
                        intent.putExtra("inspectionId",list.get(position).getInspectionId());
                        intent.putExtra("store_Code", store_Code);
                        context.startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public static class Holder extends RecyclerView.ViewHolder
    {
        private TextView txt_store_code, txt_inspected_by, txt_inspection_id, txt_date;
        private ImageView image_insp_store, image_emoji,image_insphistory_1;
        RelativeLayout relative_insp_summary;
        public Holder(View itemView)
        {
            super(itemView);
            image_insphistory_1 =(ImageView)itemView.findViewById(R.id.image_insphistory_1);
            txt_store_code = (TextView) itemView.findViewById(R.id.txt_store_code);
            txt_inspected_by = (TextView) itemView.findViewById(R.id.txt_inspected_by_val);
            txt_inspection_id = (TextView) itemView.findViewById(R.id.txt_inspection_id_val);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            image_insp_store = (ImageView) itemView.findViewById(R.id.image_insphistory_1);
            image_emoji = (ImageView) itemView.findViewById(R.id.image_emoji);
            relative_insp_summary = (RelativeLayout)itemView.findViewById(R.id.relative_insp_summary);
        }
    }
}

