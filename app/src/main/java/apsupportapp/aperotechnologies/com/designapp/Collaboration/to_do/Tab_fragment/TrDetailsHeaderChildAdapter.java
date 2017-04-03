package apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    private final HashMap<Integer, ArrayList<Transfer_Request_Model>> list;
    private final HashMap<Integer, ArrayList<Integer>> subchildCount;
    TrDetailsHeaderChildAdapter trDetailsHeaderChildAdapter;

    private  TransferDetailsAdapter transferDetailsAdapter;
    public OnScanBarcode onBarcodeScan;
    String barcode,checkChildStr;
    private static final String ACTION_SOFTSCANTRIGGER = "com.motorolasolutions.emdk.datawedge.api.ACTION_SOFTSCANTRIGGER";
    private static final String EXTRA_PARAM = "com.motorolasolutions.emdk.datawedge.api.EXTRA_PARAMETER";
    private static final String DWAPI_TOGGLE_SCANNING = "TOGGLE_SCANNING";
    private ArrayList<Integer> countList;

    public TrDetailsHeaderChildAdapter(Context context, int position, TransferDetailsAdapter transferDetailsAdapter, HashMap<Integer, ArrayList<Transfer_Request_Model>> subchildScanqty, HashMap<Integer, ArrayList<Integer>> subchildCount) {
        this.list=subchildScanqty;
        this.subchildCount=subchildCount;
        this.context=context;//
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
        ((TrDetailsHeaderChildAdapter.Holder)holder).tr_DetailChild_scanqty.setCursorVisible(false);
        ((TrDetailsHeaderChildAdapter.Holder) holder).tr_DetailChild_scanqty.setText(""+subchildCount.get(PrePosition).get(position));   //set count of sub child
        String scanQty = String.valueOf(((TrDetailsHeaderChildAdapter.Holder) holder).tr_DetailChild_scanqty.getText());

        ((TrDetailsHeaderChildAdapter.Holder)holder).tr_DetailChild_scanqty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        if(Integer.parseInt(scanQty) < Math.round(list.get(PrePosition).get(position).getStkOnhandQtyRequested()))
        {
            String updatedScanQty = String.valueOf(((TrDetailsHeaderChildAdapter.Holder) holder).tr_DetailChild_scanqty.getText());
            Log.e("Editted ScanQty :",""+updatedScanQty);

        }
        else
        {
            Toast.makeText(context,"ScanQty should be less than Req.Qty.",Toast.LENGTH_SHORT).show();
            ((TrDetailsHeaderChildAdapter.Holder)holder).tr_DetailChild_scanqty.setFocusable(false);

        }
    }


    @Override
    public int getItemCount() {
        return list.get(PrePosition).size();
    }

    private static class Holder extends RecyclerView.ViewHolder
    {
        private final TextView tr_DetailChild_size,tr_DetailChild_requiredQty;
        public EditText tr_DetailChild_scanqty;
        //  private CheckBox cb_trDetailChild;
        ImageView imgbtn_detailchild_scan;
        EditText et_trcdetailchildBarcode;
        LinearLayout lin_childimgbtnScan;

        public Holder(View itemView)
        {
            super(itemView);
            tr_DetailChild_size=(TextView)itemView.findViewById(R.id.txt_trdetailChild_size);
            tr_DetailChild_requiredQty=(TextView)itemView.findViewById(R.id.txt_trdetailchild_reqty);
            tr_DetailChild_scanqty=(EditText) itemView.findViewById(R.id.txt_trdetailchild_scanqty);
           // imgbtn_detailchild_scan = (ImageView)itemView.findViewById(R.id.btn_scan);
           // lin_childimgbtnScan = (LinearLayout)itemView.findViewById(R.id.lin_childimgbtnScan);
           // et_trcdetailchildBarcode = (EditText)itemView.findViewById(R.id.et_trcdetailchildBarcode);
        }
    }
}
