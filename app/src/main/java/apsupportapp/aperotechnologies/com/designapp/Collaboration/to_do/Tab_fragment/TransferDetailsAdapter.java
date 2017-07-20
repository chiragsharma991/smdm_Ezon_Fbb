package apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Transfer_Request_Model;
import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by pamrutkar on 09/03/17.
 */
public class TransferDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final HashMap<Integer, ArrayList<Transfer_Request_Model>> subchildqty;   //sub child list
    private final ProgressBar transferDetailProcess;
    private final HashMap<Integer, ArrayList<Integer>> subchildCount;
    private final HashMap<Integer, ArrayList<Integer>> headerScancount;
    private final TransferRequest_Details transferRequest_detailsClass;
    private Context context;
    private ArrayList<Transfer_Request_Model> list;
    public  boolean[] Tr_HeaderToggle;
    public OnPress onPressInterface;
    public OnScanBarcode onBarcodeScan;
    private Set<Pair<Integer, Integer>> CheckedItems ;
    String barcode,checkStr;
    private static final String ACTION_SOFTSCANTRIGGER = "com.motorolasolutions.emdk.datawedge.api.ACTION_SOFTSCANTRIGGER";
    private static final String EXTRA_PARAM = "com.motorolasolutions.emdk.datawedge.api.EXTRA_PARAMETER";
    private static final String DWAPI_TOGGLE_SCANNING = "TOGGLE_SCANNING";

    public TransferDetailsAdapter(ArrayList<Transfer_Request_Model> sender_detailsList, Context context, HashMap<Integer, ArrayList<Transfer_Request_Model>> subchildqty, HashMap<Integer, ArrayList<Integer>> subchildCount, ProgressBar transferDetailProcess, HashMap<Integer, ArrayList<Integer>> headerScancount, TransferRequest_Details transferRequest_detailsClass, HashSet<Pair<Integer, Integer>> checkedItems)
    {
        this.list=sender_detailsList;  //main option adapter
        this.context=context;//
        Tr_HeaderToggle= new boolean[list.size()];
        onPressInterface=(OnPress)context;
        onBarcodeScan = (OnScanBarcode)context;
        this.subchildqty=subchildqty;  //total sub child details
        this.subchildCount=subchildCount;  //only sub child scan qty
        this.transferDetailProcess=transferDetailProcess;
        this.headerScancount=headerScancount;
        CheckedItems=checkedItems;
        this.transferRequest_detailsClass=transferRequest_detailsClass;
        checkStr = "";
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_transferreq_details_child, parent, false);
        return new TransferDetailsAdapter.Holder(v);    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof TransferDetailsAdapter.Holder) {
            if(position < list.size())
            {
                HandlePositionOnSet(holder,position);
                ((TransferDetailsAdapter.Holder)holder).txt_optionval.setText(list.get(position).getLevel());
                ((TransferDetailsAdapter.Holder)holder).txt_reqtyval.setText(""+Math.round(list.get(position).getStkOnhandQtyRequested()));
                ((TransferDetailsAdapter.Holder)holder).txt_avlqtyval.setText(""+Math.round(list.get(position).getStkQtyAvl()));
                ((TransferDetailsAdapter.Holder)holder).txt_sohval.setText(""+Math.round(list.get(position).getStkOnhandQty()));
                ((TransferDetailsAdapter.Holder)holder).txt_optionval.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(transferDetailProcess.getVisibility()==View.GONE)
                        {
                            if(Tr_HeaderToggle[position]==true)
                            {
                                Tr_HeaderToggle[position]=false;
                                notifyDataSetChanged();
                            }
                            else
                            {
                                Tr_HeaderToggle[position]=true;
                               if(subchildqty.get(position).isEmpty())
                                {
                                    onPressInterface.onPress(position);
                                }
                                else
                                {
                                    notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });
                ((TransferDetailsAdapter.Holder)holder).btn_scan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                            if (isAMobileModel())
                            {

                            Intent intent_barcode = new Intent();
                            intent_barcode.setAction(ACTION_SOFTSCANTRIGGER);
                            intent_barcode.putExtra(EXTRA_PARAM, DWAPI_TOGGLE_SCANNING);
                            context.sendBroadcast(intent_barcode);
                            ((TransferDetailsAdapter.Holder)holder).et_trBarcode.setText(" ");
                            barcode = " ";
                            android.os.Handler h = new android.os.Handler();
                            h.postDelayed(new Runnable() {
                                public void run() {

                                    Intent i1 =((Activity) context).getIntent();
                                    barcode =   ((TransferDetailsAdapter.Holder)holder).et_trBarcode.getText().toString();
                                    if(!barcode.equals(" "))
                                    {
                                        Toast.makeText(context, "Barcode is : " + barcode, Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        View view=((Activity)context).findViewById(android.R.id.content);
                                        Snackbar.make(view, "No barcode found. Please try again.", Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            }, 1500);

                        } else if (!isAMobileModel()) {

                            onBarcodeScan.onScan(view, position, TransferDetailsAdapter.this);
                        }

                        if (isAMobileModel()) {

                            Intent intent_barcode = new Intent();
                            intent_barcode.setAction(ACTION_SOFTSCANTRIGGER);
                            intent_barcode.putExtra(EXTRA_PARAM, DWAPI_TOGGLE_SCANNING);
                            context.sendBroadcast(intent_barcode);
                            ((TransferDetailsAdapter.Holder)holder).et_trBarcode.setText(" ");
                            barcode = " ";
                            android.os.Handler h = new android.os.Handler();
                            h.postDelayed(new Runnable() {
                                public void run()
                                {
                                    Intent i1 =((Activity) context).getIntent();
                                    barcode =   ((TransferDetailsAdapter.Holder)holder).et_trBarcode.getText().toString();
                                    if(!barcode.equals(" "))
                                    {
                                        Toast.makeText(context, "Barcode is : " + barcode, Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        View view=((Activity)context).findViewById(android.R.id.content);
                                        Snackbar.make(view, "No barcode found. Please try again.", Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            }, 1500);

                        } else if (!isAMobileModel())
                        {
                           onBarcodeScan.onScan(view,position, TransferDetailsAdapter.this);
                        }
                    }
                });

                ((TransferDetailsAdapter.Holder) holder).txt_scanqtyVal.setText(""+headerScancount.get(position).get(0));  // header total scan qty
                TrDetailsHeaderChildAdapter detailsHeaderChildAdapter=new TrDetailsHeaderChildAdapter(context,position,TransferDetailsAdapter.this,subchildqty,subchildCount,transferRequest_detailsClass,CheckedItems,headerScancount);
                ((TransferDetailsAdapter.Holder)holder).recycleview_transferreq_detailChild.setAdapter(detailsHeaderChildAdapter);
                detailsHeaderChildAdapter.notifyDataSetChanged();
            }
        }
    }

    private void HandlePositionOnSet(RecyclerView.ViewHolder holder, int position) {
        if (Tr_HeaderToggle[position]) {
            ((TransferDetailsAdapter.Holder) holder).SizesLinLayout.setVisibility(View.VISIBLE);

        } else {
            ((TransferDetailsAdapter.Holder) holder).SizesLinLayout.setVisibility(View.GONE);
        }
    }

    private boolean isAMobileModel() {
        getDeviceInfo();
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
        return list.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        private TextView txt_caseNo,txt_optionval,txt_reqtyval,txt_avlqtyval,txt_sohval,txt_gitval,txt_scanqtyVal;
        private EditText et_trBarcode;
        private RelativeLayout SizesLinLayout;
        private ImageView btn_scan;
        protected RecyclerView recycleview_transferreq_detailChild;

        public Holder(View itemView) {
            super(itemView);
            txt_caseNo = (TextView)itemView.findViewById(R.id.txt_caseNo);
            txt_optionval = (TextView)itemView.findViewById(R.id.detail_optionLevel);
            txt_reqtyval = (TextView)itemView.findViewById(R.id.txt_reqtyVal);
            txt_avlqtyval = (TextView)itemView.findViewById(R.id.txt_avlqtyVal);
            txt_sohval = (TextView)itemView.findViewById(R.id.txt_sohVal);
            txt_scanqtyVal= (TextView)itemView.findViewById(R.id.txt_scanqtyVal);
            btn_scan = (ImageView) itemView.findViewById(R.id.imageView_scan);
            recycleview_transferreq_detailChild = (RecyclerView)itemView.findViewById(R.id.details_headerChild);
            SizesLinLayout = (RelativeLayout)itemView.findViewById(R.id.detail_size);

        }

    }
}
