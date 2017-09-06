package apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Transfer_Request_Model;
import apsupportapp.aperotechnologies.com.designapp.R;


public class TrDetailsHeaderChildAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final Context context;
    private final int PrePosition;
    private final HashMap<Integer, ArrayList<Transfer_Request_Model>> list;
    private final HashMap<Integer, ArrayList<Integer>> subchildCount;
    private final Set<Pair<Integer, Integer>> checkedItems;
    private final HashMap<Integer, ArrayList<Integer>> headerScancount;
    private final TransferRequest_Details transferRequest_detailsClass;
    TrDetailsHeaderChildAdapter trDetailsHeaderChildAdapter;
    int maxScanQty;
    String scanQty;
    private TransferDetailsAdapter transferDetailsAdapter;
    private  boolean[] visibleItems,headerCheckList;
    public OnScanBarcode onBarcodeScan;
    String checkChildStr;
    private static final String ACTION_SOFTSCANTRIGGER = "com.motorolasolutions.emdk.datawedge.api.ACTION_SOFTSCANTRIGGER";
    private static final String EXTRA_PARAM = "com.motorolasolutions.emdk.datawedge.api.EXTRA_PARAMETER";
    private static final String DWAPI_TOGGLE_SCANNING = "TOGGLE_SCANNING";
    private ArrayList<Integer> countList;

    public TrDetailsHeaderChildAdapter(Context context, int position, TransferDetailsAdapter transferDetailsAdapter, HashMap<Integer, ArrayList<Transfer_Request_Model>> subchildScanqty, HashMap<Integer, ArrayList<Integer>> subchildCount, TransferRequest_Details transferRequest_detailsClass, Set<Pair<Integer, Integer>> checkedItems, HashMap<Integer, ArrayList<Integer>> headerScancount,boolean[] visibleItems,boolean[] headerCheckList)
    {
        this.list = subchildScanqty;         // sub child sizes list
        this.subchildCount = subchildCount;  //add sub scan qty.
        this.context = context;//
        PrePosition = position;
        this.checkedItems = checkedItems;
        this.transferDetailsAdapter = transferDetailsAdapter;
        onBarcodeScan = (OnScanBarcode) context;
        checkChildStr = "";
        this.transferRequest_detailsClass = transferRequest_detailsClass;
        trDetailsHeaderChildAdapter = this;
        this.headerScancount = headerScancount;  //header scan qty.
        countList = new ArrayList<Integer>();
        this.visibleItems = visibleItems;// to check if header is check then its all sizes are also checked
        this.headerCheckList = headerCheckList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transferreq_details_header_child, parent, false);
        return new TrDetailsHeaderChildAdapter.Holder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final Pair<Integer, Integer> Childtag = new Pair<Integer, Integer>(PrePosition, position);
        ((TrDetailsHeaderChildAdapter.Holder) holder).tr_DetailChild_size.setText(list.get(PrePosition).get(position).getLevel());
        ((TrDetailsHeaderChildAdapter.Holder) holder).tr_DetailChild_requiredQty.setText("" + Math.round(list.get(PrePosition).get(position).getStkOnhandQtyRequested()));
        ((TrDetailsHeaderChildAdapter.Holder) holder).tr_DetailChild_scanqty.setCursorVisible(false);
        ((TrDetailsHeaderChildAdapter.Holder) holder).tr_DetailChild_scanqty.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });

        ((TrDetailsHeaderChildAdapter.Holder) holder).tr_DetailChild_scanqty.setText("" + subchildCount.get(PrePosition).get(position));   //set count of sub child
        ((TrDetailsHeaderChildAdapter.Holder) holder).Tr_detailChild_checkBox.setTag(Childtag);
        // if header is checked then all sub values will be checked.
        if(visibleItems[PrePosition])
        {
            if (headerCheckList[PrePosition])
            {
                checkedItems.add(Childtag);
            }
            else
            {
                checkedItems.remove(Childtag);
            }
        }
        ((TrDetailsHeaderChildAdapter.Holder) holder).Tr_detailChild_checkBox.setChecked(checkedItems.contains(Childtag));
        ((TrDetailsHeaderChildAdapter.Holder) holder).Tr_detailChild_checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                visibleItems[PrePosition]=false;
                final CheckBox cb = (CheckBox) view;
                final Pair<Integer, Integer> tagFlag = (Pair<Integer, Integer>) view.getTag();
                //this is check and uncheked method to remove and add flag ...
                if (cb.isChecked()) {
                    CheckCondition(tagFlag);
                } else {
                    UnCheckCondition(tagFlag);
                }
                transferDetailsAdapter.notifyDataSetChanged();

            }
        });

        maxScanQty = (int) Math.round(list.get(PrePosition).get(position).getStkOnhandQtyRequested());
        scanQty = ((TrDetailsHeaderChildAdapter.Holder) holder).tr_DetailChild_scanqty.getText().toString();

        if (scanQty.equals("0")) // when scan qty is 0 block virual keyboard
        {
            ((TrDetailsHeaderChildAdapter.Holder) holder).tr_DetailChild_scanqty.setCursorVisible(false);
            ((TrDetailsHeaderChildAdapter.Holder) holder).tr_DetailChild_scanqty.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int inType = ((TrDetailsHeaderChildAdapter.Holder) holder).tr_DetailChild_scanqty.getInputType(); // backup the input type
                    ((TrDetailsHeaderChildAdapter.Holder) holder).tr_DetailChild_scanqty.setInputType(InputType.TYPE_NULL); // disable soft input
                    ((TrDetailsHeaderChildAdapter.Holder) holder).tr_DetailChild_scanqty.onTouchEvent(event); // call native handler
                    ((TrDetailsHeaderChildAdapter.Holder) holder).tr_DetailChild_scanqty.setInputType(inType); // restore input type
                    return true; // consume touch event
                }
            });
        } else  // open dialog box to add scan Qty
        {
            ((TrDetailsHeaderChildAdapter.Holder) holder).tr_DetailChild_scanqty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(v.getContext(), R.style.AppCompatAlertDialogStyle);
                    View dialogView = LayoutInflater.from(context).inflate(R.layout.sender_alert_dialog, null);
                    dialogBuilder.setView(dialogView);
                    final EditText edt = (EditText) dialogView.findViewById(R.id.edit_scanQty);
                    dialogBuilder.setTitle("");

                    dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //do something with edt.getText().toString();
                            if (edt.getText().toString().equals("")) {
                                InputMethodManager imm = (InputMethodManager) edt.getContext()
                                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
                                return;
                            } else if (Integer.parseInt(edt.getText().toString()) <= maxScanQty)
                            {
                                if (Integer.parseInt(edt.getText().toString()) == 0) {
                                    Toast.makeText(context, "Scan Qty cannot be 0", Toast.LENGTH_LONG).show();

                                } else {
                                    ArrayList<Integer> total = new ArrayList<Integer>();
                                    int positionOfAdd = position;
                                    for (int i = 0; i < list.size(); i++)
                                    {
                                        if (positionOfAdd == i) {
                                            int totalCount = Integer.parseInt(edt.getText().toString());  //pre count + next count
                                            total.add(totalCount);

                                        } else {
                                            int totalCount = subchildCount.get(PrePosition).get(positionOfAdd);  //pre count + next count
                                            total.add(totalCount);
                                        }
                                    }
                                    //total sub count
                                    subchildCount.put(PrePosition, total);
                                    transferRequest_detailsClass.addtotalIn_headerScanqty(PrePosition);  //0 is pre position
                                    transferDetailsAdapter.notifyDataSetChanged();
                                }
                            }
                            else
                            {
                                Toast.makeText(context, "Scan Qty should be less than Req.Qty", Toast.LENGTH_LONG).show();
                            }

                            InputMethodManager imm = (InputMethodManager) edt.getContext()
                                    .getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);

                        }
                    });
                    dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            InputMethodManager imm = (InputMethodManager) context
                                    .getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);

                        }
                    });

                    dialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            InputMethodManager imm = (InputMethodManager) context
                                    .getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
                        }
                    });

                    final AlertDialog b = dialogBuilder.create();
                    b.show();
                    edt.setOnFocusChangeListener(new View.OnFocusChangeListener()
                    {
                        @Override
                        public void onFocusChange(View view, boolean hasFocus) {
                            if (hasFocus) {
                                b.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            }
                        }
                    });
                }
            });
        }
    }

    private void CheckCondition(Pair<Integer, Integer> tagFlag) {

        checkedItems.add(tagFlag);
        boolean[] CheckChild = new boolean[list.get(PrePosition).size()];
        for (int i = 0; i < list.get(PrePosition).size(); i++)
        {
            Pair<Integer, Integer> Tag = new Pair<Integer, Integer>(PrePosition, i);
            CheckChild[i] = checkedItems.contains(Tag) ? true : false;
        }
        //if all list are true from all list child then header check will be enable.
        if (containsTrue(CheckChild)) {
            headerCheckList[PrePosition] = true;
        }
    }

    private void UnCheckCondition(Pair<Integer, Integer> tagFlag) {
        boolean[] CheckChild = new boolean[list.get(PrePosition).size()];
        for (int i = 0; i < list.get(PrePosition).size(); i++)
        {
            Pair<Integer, Integer> Tag = new Pair<Integer, Integer>(PrePosition, i);
            if (checkedItems.contains(Tag))
            {
                CheckChild[i] = true;
            } else {
                CheckChild[i] = false;
            }
        }
        // if one list is false from all list child then header check will be disable.
        if (containsTrue(CheckChild))
        {
            headerCheckList[PrePosition] = false;
        }
        checkedItems.remove(tagFlag);
    }

    public boolean containsTrue(boolean[] array) {

        boolean AllItems = false;
        for (boolean anArray : array) {
            if (anArray) {
                AllItems = true;
            } else {
                AllItems = false;
                break;
            }
        }
        return AllItems;
    }
    @Override
    public int getItemCount() {
        return list.get(PrePosition).size();
    }

    private static class Holder extends RecyclerView.ViewHolder {
        private final TextView tr_DetailChild_size, tr_DetailChild_requiredQty;
        public EditText tr_DetailChild_scanqty;
        //  private CheckBox cb_trDetailChild;
        ImageView imgbtn_detailchild_scan;
        EditText et_trcdetailchildBarcode;
        LinearLayout lin_childimgbtnScan;
        CheckBox Tr_detailChild_checkBox;

        public Holder(View itemView) {
            super(itemView);
            tr_DetailChild_size = (TextView) itemView.findViewById(R.id.txt_trdetailChild_size);
            tr_DetailChild_requiredQty = (TextView) itemView.findViewById(R.id.txt_trdetailchild_reqty);
            tr_DetailChild_scanqty = (EditText) itemView.findViewById(R.id.txt_trdetailchild_scanqty);
            Tr_detailChild_checkBox = (CheckBox) itemView.findViewById(R.id.tr_detailChild_checkBox);

        }
    }
}
