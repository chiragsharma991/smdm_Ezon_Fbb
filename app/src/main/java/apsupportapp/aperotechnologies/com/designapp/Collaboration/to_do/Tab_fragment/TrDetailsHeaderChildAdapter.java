package apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Transfer_Request_Model;
import apsupportapp.aperotechnologies.com.designapp.R;


public class TrDetailsHeaderChildAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final Context context;
    private final int PrePosition;
    private final HashMap<Integer, ArrayList<Transfer_Request_Model>> list;
    public static Set<Pair<Integer, Integer>> CheckedItems = new HashSet<Pair<Integer, Integer>>();
    private  TransferDetailsAdapter transferDetailsAdapter;
    public OnScanBarcode onBarcodeScan;
    String barcode;
    private static final String ACTION_SOFTSCANTRIGGER = "com.motorolasolutions.emdk.datawedge.api.ACTION_SOFTSCANTRIGGER";
    private static final String EXTRA_PARAM = "com.motorolasolutions.emdk.datawedge.api.EXTRA_PARAMETER";
    private static final String DWAPI_TOGGLE_SCANNING = "TOGGLE_SCANNING";



    public TrDetailsHeaderChildAdapter(HashMap<Integer, ArrayList<Transfer_Request_Model>> list, Context context, int position, TransferDetailsAdapter transferDetailAdapter) {
        this.list=list;
        this.context=context;//
        PrePosition=position;
        this.transferDetailsAdapter=transferDetailAdapter;
        onBarcodeScan = (OnScanBarcode)context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.transferreq_details_header_child, parent, false);
        return new TrDetailsHeaderChildAdapter.Holder(v);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        Log.e("TAG", "On Detail child: "+position );

        final Pair<Integer, Integer> Childtag = new Pair<Integer, Integer>(PrePosition,position);

        ((TrDetailsHeaderChildAdapter.Holder)holder).tr_DetailChild_size.setText(list.get(PrePosition).get(position).getLevel());
        ((TrDetailsHeaderChildAdapter.Holder)holder).tr_DetailChild_requiredQty.setText(""+Math.round(list.get(PrePosition).get(position).getStkOnhandQtyRequested()));
     //   ((TrDetailsHeaderChildAdapter.Holder)holder).DetailChild_aviQty.setText(""+Math.round(list.get(PrePosition).get(position).getStkQtyAvl()));
        ((TrDetailsHeaderChildAdapter.Holder)holder).cb_trDetailChild.setTag(Childtag);

        ((TrDetailsHeaderChildAdapter.Holder)holder).cb_trDetailChild.setChecked(CheckedItems.contains(Childtag));
         ((TrDetailsHeaderChildAdapter.Holder) holder).imgbtn_detailchild_scan.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Log.e("TAG", "Detail Child Scan onClick:>>>> "+position );

                 if (isAMobileModel()) {

                     Intent intent_barcode = new Intent();
                     intent_barcode.setAction(ACTION_SOFTSCANTRIGGER);
                     intent_barcode.putExtra(EXTRA_PARAM, DWAPI_TOGGLE_SCANNING);
                     context.sendBroadcast(intent_barcode);
                   //  ((TransferDetailsAdapter.Holder)holder).et_trBarcode.setText(" ");
                     barcode = " ";


                     android.os.Handler h = new android.os.Handler();
                     h.postDelayed(new Runnable() {
                         public void run() {

                             Intent i1 =((Activity) context).getIntent();
                             Log.e("getIntent : ", "" + ((Activity) context).getIntent());
                             Log.e("barcode :", " " + i1 + "\ntxt :" +   ((TrDetailsHeaderChildAdapter.Holder)holder).et_trcdetailchildBarcode.getText().toString());
                             barcode =   ((TrDetailsHeaderChildAdapter.Holder)holder).et_trcdetailchildBarcode.getText().toString();
                             if(!barcode.equals(" ")) {
                                 Toast.makeText(context, "Barcode is : " + barcode, Toast.LENGTH_SHORT).show();
                                 //  TimeUP();
                             }
                             else{
                                 View view=((Activity)context).findViewById(android.R.id.content);
                                 Snackbar.make(view, "No barcode found. Please try again.", Snackbar.LENGTH_LONG).show();
                             }
                         }
                     }, 1500);

                 } else if (!isAMobileModel()) {
                     Log.e("regular device", "");
                     onBarcodeScan.onScan(v);

                 }
             }
         });
//        ((TrDetailsHeaderChildAdapter.Holder)holder).DetailChild_checkBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                final CheckBox cb = (CheckBox) view;
//                final Pair<Integer, Integer> tagFlag = (Pair<Integer, Integer>) view.getTag();
//
//                //this is check and uncheked method to remove and add flag ...
//
//                // before uncheck you have to calculate past things like :
//                // 1. child list are selected to all
//                // 2. one is not selected to all
//
//                if (cb.isChecked())
//                {
//                    CheckCondition(tagFlag);
//
//                } else
//                {
//                    UnCheckCondition(tagFlag);
//                }
//                Log.e("TAG", "data has been notified.... " );
//
//                transferDetailsAdapter.notifyDataSetChanged();
//
//            }
//        });

    }

//    private void CheckCondition(Pair<Integer, Integer> tagFlag)
//    {
//        CheckedItems.add(tagFlag);
//
//        boolean[]CheckChild=new boolean[Details.HashmapList.get(PrePosition).size()];
//        for (int i = 0; i <Details.HashmapList.get(PrePosition).size(); i++) {
//
//
//            Pair<Integer, Integer> Tag = new Pair<Integer, Integer>(PrePosition,i);
//            if(CheckedItems.contains(Tag))
//            {
//                CheckChild[i]=true;
//            }else
//            {
//                CheckChild[i]=false;
//            }
//        }
//        // if all list are true from all list child then header check will be enable.
//        if(containsTrue(CheckChild))
//        {
//            StockDetailsAdapter.HeadercheckList[PrePosition]=true;
//        }
//    }
//

    private boolean isAMobileModel() {
        Log.e("checking model", "");
        getDeviceInfo();
        Log.e("model is ", "" + Build.MODEL);
        return Build.MODEL.contains("TC75");
    }

    public String getDeviceInfo() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }
    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }


    @Override
    public int getItemCount() {
        return list.get(PrePosition).size();
    }

    private static class Holder extends RecyclerView.ViewHolder {


        private final TextView tr_DetailChild_size,tr_DetailChild_requiredQty,tr_DetailChild_scanqty;
        private CheckBox cb_trDetailChild;
        ImageButton imgbtn_detailchild_scan;
        EditText et_trcdetailchildBarcode;

        public Holder(View itemView) {
            super(itemView);
            tr_DetailChild_size=(TextView)itemView.findViewById(R.id.txt_trdetailChild_size);
            tr_DetailChild_requiredQty=(TextView)itemView.findViewById(R.id.txt_trdetailchild_reqty);
            tr_DetailChild_scanqty=(TextView)itemView.findViewById(R.id.txt_trdetailchild_scanqty);
            cb_trDetailChild=(CheckBox) itemView.findViewById(R.id.tr_detailChild_checkBox);
            imgbtn_detailchild_scan = (ImageButton)itemView.findViewById(R.id.imgbtn_detailchild_scan);
            et_trcdetailchildBarcode = (EditText)itemView.findViewById(R.id.et_trcdetailchildBarcode);
        }

    }


}
