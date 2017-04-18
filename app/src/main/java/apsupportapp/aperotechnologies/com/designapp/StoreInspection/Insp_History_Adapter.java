package apsupportapp.aperotechnologies.com.designapp.StoreInspection;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.Collaboration.Status.Tab_fragment.ToBeReceiverAdapter;
import apsupportapp.aperotechnologies.com.designapp.Collaboration.Status.Tab_fragment.ToBeReceiverDetails;
import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment.TransferDetailsAdapter;
import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by pamrutkar on 18/04/17.
 */
public class Insp_History_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private ArrayList<InspectionBeanClass> list;


    public Insp_History_Adapter(ArrayList<InspectionBeanClass> list, Context context) {
        this.list = list;  //main adapter
        this.context = context;//

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_insp_history_child, parent, false);
        return new Insp_History_Adapter.Holder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        //  Log.e("TAG", "Stock detail: "+position );

        if (holder instanceof Insp_History_Adapter.Holder) {
//            if (position < list.size())
//            {
                ((Insp_History_Adapter.Holder) holder).txt_inspected_by.setText(""+"Inspected By : " + "1");
                ((Insp_History_Adapter.Holder) holder).txt_inspection_id.setText(""+"Inspection Id : " + "Rohit");
                ((Insp_History_Adapter.Holder) holder).txt_store_code.setText("" + "4813");
                ((Insp_History_Adapter.Holder) holder).txt_date.setText("" + "10 April 2017");
                ((Insp_History_Adapter.Holder) holder).relative_insp_summary.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view) {
                        Log.e( "OnClick Rel Layout " ,"");
                        Intent intent = new Intent(context ,InspectionDetailsActivity.class);
                        context.startActivity(intent);
                       // new InspectionHistoryActivity().StartActivity(context);
                    }
                });
          //  }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        private TextView txt_store_code, txt_inspected_by, txt_inspection_id, txt_date;
        private ImageView image_insp_store, image_emoji;
        RelativeLayout relative_insp_summary;

        public Holder(View itemView)
        {
            super(itemView);
            txt_store_code = (TextView) itemView.findViewById(R.id.txt_store_code);
            txt_inspected_by = (TextView) itemView.findViewById(R.id.txt_inspected_name);
            txt_inspection_id = (TextView) itemView.findViewById(R.id.txt_inspection_id);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            image_insp_store = (ImageView) itemView.findViewById(R.id.image_insphistory_1);
            image_emoji = (ImageView) itemView.findViewById(R.id.image_emoji);
            relative_insp_summary = (RelativeLayout)itemView.findViewById(R.id.relative_insp_summary);
        }

    }
}

