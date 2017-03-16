package apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
    TrDetailsHeaderChildAdapter trDetailsHeaderChildAdapter;

    private final HashMap<Integer, ArrayList<Transfer_Request_Model>> list;
    private final HashMap<Integer, ArrayList<Integer>> childScanCount;
    private  TransferDetailsAdapter transferDetailsAdapter;
    public OnScanBarcode onBarcodeScan;
    String barcode,checkChildStr;
    private static final String ACTION_SOFTSCANTRIGGER = "com.motorolasolutions.emdk.datawedge.api.ACTION_SOFTSCANTRIGGER";
    private static final String EXTRA_PARAM = "com.motorolasolutions.emdk.datawedge.api.EXTRA_PARAMETER";
    private static final String DWAPI_TOGGLE_SCANNING = "TOGGLE_SCANNING";
    private ArrayList<Integer> countList;





    public TrDetailsHeaderChildAdapter(HashMap<Integer, ArrayList<Transfer_Request_Model>> transferReqHashmapList, Context context, int position, TransferDetailsAdapter transferDetailsAdapter, HashMap<Integer, ArrayList<Integer>> childScanCount) {

        this.list=transferReqHashmapList;
        this.context=context;//
        this.childScanCount=childScanCount;//
        PrePosition=position;
        this.transferDetailsAdapter=transferDetailsAdapter;
        onBarcodeScan = (OnScanBarcode)context;
        checkChildStr = "";
        trDetailsHeaderChildAdapter = this;
        countList = new ArrayList<Integer>();


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
                            if(!barcode.equals(" "))
                            {
                                Toast.makeText(context, "Barcode is : " + barcode, Toast.LENGTH_SHORT).show();
                                //  TimeUP();
                            }
                            else
                            {
                                View view=((Activity)context).findViewById(android.R.id.content);
                                Snackbar.make(view, "No barcode found. Please try again.", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    }, 1500);

                } else if (!isAMobileModel()) {

                    checkChildStr = "ChildAdapter";
                    onBarcodeScan.onScan(v,position,checkChildStr,transferDetailsAdapter);
//                     for(int i = 0;i )
//                     countList.add(1);
                }
                notifyDataSetChanged();
            }
        });
        ((TrDetailsHeaderChildAdapter.Holder) holder).tr_DetailChild_scanqty.setText(""+childScanCount.get(PrePosition).get(position));

    }

    private boolean isAMobileModel() {
        Log.e("checking model", "");
        getDeviceInfo();
        Log.e("cmodel is ", "" + Build.MODEL);
        return Build.MODEL.contains("TC75");
    }

    public String getDeviceInfo()
    {
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


        private final TextView tr_DetailChild_size,tr_DetailChild_requiredQty;
        public TextView tr_DetailChild_scanqty;
        //  private CheckBox cb_trDetailChild;
        ImageButton imgbtn_detailchild_scan;
        EditText et_trcdetailchildBarcode;
        LinearLayout lin_childimgbtnScan;

        public Holder(View itemView) {
            super(itemView);
            tr_DetailChild_size=(TextView)itemView.findViewById(R.id.txt_trdetailChild_size);
            tr_DetailChild_requiredQty=(TextView)itemView.findViewById(R.id.txt_trdetailchild_reqty);
            tr_DetailChild_scanqty=(TextView)itemView.findViewById(R.id.txt_trdetailchild_scanqty);
            imgbtn_detailchild_scan = (ImageButton)itemView.findViewById(R.id.imgbtn_detailchild_scan);
            lin_childimgbtnScan = (LinearLayout)itemView.findViewById(R.id.lin_childimgbtnScan);
            et_trcdetailchildBarcode = (EditText)itemView.findViewById(R.id.et_trcdetailchildBarcode);
        }
    }
}
