package apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cipherlab.barcode.GeneralString;
import com.cipherlab.barcode.ReaderManager;
import com.cipherlab.barcode.decoder.BcReaderType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Transfer_Request_Model;
import apsupportapp.aperotechnologies.com.designapp.ProductInformation.StyleActivity;
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
    public  boolean[] TransferDetails_HeadercheckList;  //list for check header
    public boolean[] visibleItems;

    private Set<Pair<Integer, Integer>> CheckedItems ;
    String barcode,checkStr;
    private ReaderManager mReaderManager;
    private IntentFilter filter;
    TransferRequest_Details transferRequestDetails;


    public TransferDetailsAdapter(ArrayList<Transfer_Request_Model> sender_detailsList, Context context, HashMap<Integer, ArrayList<Transfer_Request_Model>> subchildqty, HashMap<Integer, ArrayList<Integer>> subchildCount, ProgressBar transferDetailProcess, HashMap<Integer, ArrayList<Integer>> headerScancount, TransferRequest_Details transferRequest_detailsClass, HashSet<Pair<Integer, Integer>> checkedItems)
    {
        this.list=sender_detailsList;  //main option adapter
        this.context=context;//
        Tr_HeaderToggle= new boolean[list.size()];
        onPressInterface=(OnPress)context;
        onBarcodeScan = (OnScanBarcode)context;
        TransferDetails_HeadercheckList = new boolean[list.size()];
        this.subchildqty=subchildqty;  //total sub child details
        this.subchildCount=subchildCount;  //only sub child scan qty
        this.transferDetailProcess=transferDetailProcess;
        this.headerScancount=headerScancount;
        CheckedItems=checkedItems;
        visibleItems=new boolean[list.size()];
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
                ((TransferDetailsAdapter.Holder)holder).transferdetail_checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        if(((CheckBox)view).isChecked())
                        {
                            //Header check is enable when view is open
                            TransferDetails_HeadercheckList[position]=true;
                            visibleItems[position]=true;
                            notifyItemChanged(position);
                        }else
                        {
                            TransferDetails_HeadercheckList[position]=false;
                            visibleItems[position]=true;

                            notifyItemChanged(position);
                        }
                    }
                });


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
                                ExeSampleCode();
                                mReaderManager = ReaderManager.InitInstance(context);
                                filter = new IntentFilter();
                                filter.addAction(GeneralString.Intent_SOFTTRIGGER_DATA);
                                filter.addAction(GeneralString.Intent_PASS_TO_APP);
                                filter.addAction(GeneralString.Intent_READERSERVICE_CONNECTED);
                                ((Activity)context).registerReceiver(myDataReceiver, filter);



                        } else if (!isAMobileModel()) {

                            onBarcodeScan.onScan(view, position, TransferDetailsAdapter.this);
                        }

                    }
                });

                ((TransferDetailsAdapter.Holder) holder).txt_scanqtyVal.setText(""+headerScancount.get(position).get(0));  // header total scan qty
                TrDetailsHeaderChildAdapter detailsHeaderChildAdapter=new TrDetailsHeaderChildAdapter(context,position,TransferDetailsAdapter.this,subchildqty,subchildCount,transferRequest_detailsClass,CheckedItems,headerScancount,visibleItems,TransferDetails_HeadercheckList);
                ((TransferDetailsAdapter.Holder)holder).recycleview_transferreq_detailChild.setAdapter(detailsHeaderChildAdapter);
                detailsHeaderChildAdapter.notifyDataSetChanged();
            }
        }
    }


    private void ExeSampleCode()
    {

        if (mReaderManager != null) {
            Log.e("onClick: ", "------");
            BcReaderType myReaderType = mReaderManager.GetReaderType();
            //  edit_barcode.setText(myReaderType.toString());
        }
        if(mReaderManager != null) {
            // Enable/Disable barcode reader service
            com.cipherlab.barcode.decoder.ClResult clRet = mReaderManager.SetActive(false);
            boolean bRet = mReaderManager.GetActive();
            clRet = mReaderManager.SetActive(true);
            bRet = mReaderManager.GetActive();

        }
        if(mReaderManager != null)
        {
            //software trigger
            Thread sThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    mReaderManager.SoftScanTrigger();
                }
            });
            sThread.setPriority(Thread.MAX_PRIORITY);
            sThread.start();

        }

    }

    /// create a BroadcastReceiver for receiving intents from barcode reader service
    private final BroadcastReceiver myDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            // Software trigger must receive this intent message
            if (intent.getAction().equals(GeneralString.Intent_SOFTTRIGGER_DATA)) {


                barcode = intent.getStringExtra(GeneralString.BcReaderData);
                Log.e("onReceive: ", " " + barcode);
                android.os.Handler h = new android.os.Handler();
                h.postDelayed(new Runnable() {
                    public void run() {
                        Log.e("run: ", "" + barcode);
                        if (!barcode.equals(" ")) {
                            Toast.makeText(context, "Barcode scanned : " + barcode, Toast.LENGTH_SHORT).show();
                            transferRequestDetails.requestScanDetailsAPI(barcode);
                        }
                        else
                        {
                            Log.e("come", "here");
                            View view =((Activity)context).findViewById(android.R.id.content);
                            Snackbar.make(view, "No barcode found. Please try again.", Snackbar.LENGTH_LONG).show();
                        }
                    }
                }, 1500);


            }


        }
    };



    private void HandlePositionOnSet(RecyclerView.ViewHolder holder, int position) {
        if (Tr_HeaderToggle[position]) {
            ((TransferDetailsAdapter.Holder) holder).SizesLinLayout.setVisibility(View.VISIBLE);

        } else {
            ((TransferDetailsAdapter.Holder) holder).SizesLinLayout.setVisibility(View.GONE);
        }

        if(TransferDetails_HeadercheckList[position])
        {
            ((TransferDetailsAdapter.Holder)holder).transferdetail_checkBox.setChecked(true);

        }
        else
        {
            ((TransferDetailsAdapter.Holder)holder).transferdetail_checkBox.setChecked(false);

        }
    }

    private boolean isAMobileModel() {
        getDeviceInfo();
        return Build.MODEL.contains("RS31");
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
        private CheckBox transferdetail_checkBox;

        public Holder(View itemView)


        {
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
            transferdetail_checkBox = (CheckBox)itemView.findViewById(R.id.transferdetail_headerCheck);

        }

    }
}
