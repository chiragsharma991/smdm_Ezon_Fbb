package apsupportapp.aperotechnologies.com.designapp.KeyProductPlan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.security.Key;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;

import apsupportapp.aperotechnologies.com.designapp.MySingleton;
import apsupportapp.aperotechnologies.com.designapp.OnRowPressListener;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by pamrutkar on 28/02/17.
 */

public class Plan_Product extends Fragment {
   TableLayout tableAProd_Frag;
   TableLayout tableBProd_Frag;
   TableLayout tableCProd_Frag;
   TableLayout tableDProd_Frag;
    ViewGroup view;
    HorizontalScrollView horizontalScrollViewB;
    HorizontalScrollView horizontalScrollViewD;
    ArrayList<KeyPlanProductBean> productNameBeanArrayList;
    ScrollView scrollViewC;
    ScrollView scrollViewD;
    static String prodsegClick ="WTD" ;
    SegmentedGroup segmentedGroupProduct;
    RadioButton plan_btnWTD, plan_btnLW;
    RequestQueue queue;
    Context context;
    RelativeLayout relativeLayout;
    public static RelativeLayout relPlanProd_Frag;
    int planlevel;

    Button txtProdGreen,txtProdRed,txtProdAmber;
    private  boolean plantoggleClick=true;
    // set the header titles
    String headers[] = {
           "    Product Name   ",
           " PvA\n\t\tSales% ",
           "  PvA\n\t\tStk%   ",
           "  Plan\n\t\tSales   ",
           "    Sales\n\t\t\t(U)    ",
           "    Plan\n\t\t\tStk    ",
           "    Stk\n\t\t\t(U)    "

    };

    int headerCellsWidth[] = new int[headers.length];
    KeyPlanProductBean productNameBean;
    TextView txtStoreCode, txtStoreDesc;
    String userId, bearertoken;
    MySingleton m_config;
    int offsetvalue = 0, limit = 100;
    int count = 0;
    OnRowPressListener1 rowPressListener;
    SharedPreferences sharedPreferences;
    String filterProductValues = "",achColor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        m_config = MySingleton.getInstance(context);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.e("TAG", "onCreateView: Productview>>>>>" );

        view = (ViewGroup) inflater.inflate(R.layout.planactual_product_fragment, container, false);
        context = view.getContext();
        txtStoreCode = (TextView) view.findViewById(R.id.txtStoreCode);
        txtStoreDesc = (TextView) view.findViewById(R.id.txtStoreName);
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        planlevel = 1;
        filterProductValues = getActivity().getIntent().getStringExtra("productfilterValue");
        Log.e("filterProductValues :",""+filterProductValues);
        relPlanProd_Frag = (RelativeLayout) view.findViewById(R.id.planactual_rel);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.planactual_productrelLayout);
        relativeLayout.setBackgroundColor(Color.parseColor("#f8f6f6"));  //dfdedf
        relPlanProd_Frag.setVisibility(View.VISIBLE);

        LinearLayout layout = (LinearLayout) KeyProductPlanActivity.plan_pager.getParent();
        TabLayout tab = (TabLayout) layout.getChildAt(1);
        if (tab.getTabCount() == 3) {
            tab.removeTabAt(2);
        }
        if(tab.getTabCount() == 2)
        {
            tab.removeTabAt(1);
        }

        segmentedGroupProduct = (SegmentedGroup) view.findViewById(R.id.segmentedGrpProduct);
        plan_btnWTD = (RadioButton) view.findViewById(R.id.planactual_prodbtnWTD);
       // plan_btnWTD.toggle();
        plan_btnLW = (RadioButton) view.findViewById(R.id.planactual_prodbtnLW);
        Log.e( "onCreateView: ","" +prodsegClick );
        txtProdGreen = (Button)view.findViewById(R.id.txtProdGreen);
        txtProdRed = (Button) view.findViewById(R.id.txtProdRed);
        txtProdAmber = (Button) view.findViewById(R.id.txtProdAmber);

        Log.e("TAG", "onCreateView: Productview>>>>>" );

        if (Reusable_Functions.chkStatus(context)) {

            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            offsetvalue = 0;
            limit = 100;
            count = 0;
            planlevel = 1;
            productNameBeanArrayList = new ArrayList<KeyPlanProductBean>();

            Log.e("Segment Val in Api ",""+prodsegClick);

            if(filterProductValues == null || filterProductValues == "") {
                plantoggleClick = false;
                RetainSegVal();
                requestPlanProductAPI(offsetvalue, limit);
            }
            else
            {
                plantoggleClick = true;
                RetainSegVal();
                requestFilterProductAPI(offsetvalue,limit);
            }
        } else {
            Toast.makeText(getContext(), "Check your network connectivity", Toast.LENGTH_LONG).show();
            plantoggleClick=false;

        }

         segmentedGroupProduct.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(RadioGroup group, int checkedId) {
                 if(!plantoggleClick) {

                    switch (checkedId) {
                        case R.id.planactual_prodbtnWTD:
                            if (prodsegClick.equals("WTD")) {
                                break;
                            }
                            prodsegClick = "WTD";

                            relPlanProd_Frag.setVisibility(View.VISIBLE);
                            tableAProd_Frag.removeAllViews();
                            tableBProd_Frag.removeAllViews();
                            tableCProd_Frag.removeAllViews();
                            tableDProd_Frag.removeAllViews();
                            if (Reusable_Functions.chkStatus(context)) {
                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                productNameBeanArrayList = new ArrayList<KeyPlanProductBean>();
                                planlevel = 1;
                                if(filterProductValues == null || filterProductValues == "") {
                                    requestPlanProductAPI(offsetvalue, limit);
                                }
                                else
                                {
                                    requestFilterProductAPI(offsetvalue,limit);
                                }
                            } else {
                                Toast.makeText(getContext(), "Check your network connectivity", Toast.LENGTH_LONG).show();
                            }

                            break;
                        case R.id.planactual_prodbtnLW:
                            if (prodsegClick.equals("LW")) {
                                break;
                            }
                            prodsegClick = "LW";

                            relPlanProd_Frag.setVisibility(View.VISIBLE);
                            tableAProd_Frag.removeAllViews();
                            tableBProd_Frag.removeAllViews();
                            tableCProd_Frag.removeAllViews();
                            tableDProd_Frag.removeAllViews();
                            if (Reusable_Functions.chkStatus(context)) {

                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(context, "Loading data...");
                                offsetvalue = 0;
                                limit = 100;
                                count = 0;
                                productNameBeanArrayList = new ArrayList<KeyPlanProductBean>();
                                planlevel = 1;
                                if(filterProductValues == null || filterProductValues == "") {
                                    requestPlanProductAPI(offsetvalue, limit);
                                }
                                else
                                {
                                    requestFilterProductAPI(offsetvalue,limit);
                                }
                            } else {
                                Toast.makeText(getContext(), "Check your network connectivity", Toast.LENGTH_LONG).show();
                            }

                            break;
                    }
                }
                 else
                 {
                     plantoggleClick = false;
                 }
             }
         });

        txtProdGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                relPlanProd_Frag.setVisibility(View.VISIBLE);
                tableAProd_Frag.removeAllViews();
                tableBProd_Frag.removeAllViews();
                tableCProd_Frag.removeAllViews();
                tableDProd_Frag.removeAllViews();
                if (Reusable_Functions.chkStatus(context)) {

                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading data...");
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    planlevel = 1;
                    achColor = "Green";
                    productNameBeanArrayList = new ArrayList<KeyPlanProductBean>();
                    plantoggleClick = false;
                    RetainSegVal();
                    requestPlanProductAchColorAPI(offsetvalue, limit);

            }
            }

        });
        txtProdRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                relPlanProd_Frag.setVisibility(View.VISIBLE);
                tableAProd_Frag.removeAllViews();
                tableBProd_Frag.removeAllViews();
                tableCProd_Frag.removeAllViews();
                tableDProd_Frag.removeAllViews();
                if (Reusable_Functions.chkStatus(context)) {

                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading data...");
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    planlevel = 1;
                    achColor = "Red";
                    productNameBeanArrayList = new ArrayList<KeyPlanProductBean>();
                    plantoggleClick = false;
                    RetainSegVal();
                    requestPlanProductAchColorAPI(offsetvalue, limit);

                }
            }

        });
        txtProdAmber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                relPlanProd_Frag.setVisibility(View.VISIBLE);
                tableAProd_Frag.removeAllViews();
                tableBProd_Frag.removeAllViews();
                tableCProd_Frag.removeAllViews();
                tableDProd_Frag.removeAllViews();
                if (Reusable_Functions.chkStatus(context)) {

                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading data...");
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    planlevel = 1;
                    achColor = "Amber";
                    productNameBeanArrayList = new ArrayList<KeyPlanProductBean>();
                    plantoggleClick = false;
                    RetainSegVal();
                    requestPlanProductAchColorAPI(offsetvalue, limit);

                }
            }

        });

        initComponents();
        setComponentsId();
        setScrollViewAndHorizontalScrollViewTag();
        // no need to assemble component A, since it is just a table
        horizontalScrollViewB.addView(tableBProd_Frag);
        scrollViewC.addView(tableCProd_Frag);
        scrollViewD.addView(horizontalScrollViewD);
        horizontalScrollViewD.addView(tableDProd_Frag);

        addComponentToMainLayout();

        return view;
    }

    private void RetainSegVal() {
        plantoggleClick = true;

        if(prodsegClick.equals("WTD"))
        {
            plan_btnWTD.toggle();


        }else if(prodsegClick.equals("LW"))
        {
            plan_btnLW.toggle();

        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            rowPressListener = (OnRowPressListener1) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getContext().toString() + " must implement onButtonPressed");
        }
    }

    // initalized components
    private void initComponents() {
        tableAProd_Frag = new TableLayout(this.context);
        tableBProd_Frag = new TableLayout(this.context);
        tableCProd_Frag = new TableLayout(this.context);
        tableDProd_Frag = new TableLayout(this.context);
        horizontalScrollViewB = new MyHorizontalScrollView(this.context);
        horizontalScrollViewD = new MyHorizontalScrollView(this.context);
        scrollViewC = new MyScrollView(this.context);
        scrollViewD = new MyScrollView(this.context);
        tableAProd_Frag.setBackgroundColor(Color.parseColor("#000000"));
        horizontalScrollViewB.setBackgroundColor(Color.parseColor("#dfdedf"));
    }

    // set essential component IDs
    @SuppressWarnings("ResourceType")
    private void setComponentsId() {
        tableAProd_Frag.setId(1);
        horizontalScrollViewB.setId(2);
        scrollViewC.setId(3);
        scrollViewD.setId(4);
    }

    // set tags for some horizontal and vertical scroll view
    private void setScrollViewAndHorizontalScrollViewTag() {
        horizontalScrollViewB.setTag("horizontal scroll view b");
        horizontalScrollViewD.setTag("horizontal scroll view d");
        scrollViewC.setTag("scroll view c");
        scrollViewD.setTag("scroll view d");
    }

    // we add the components here in our TableMainLayout
    private void addComponentToMainLayout() {
        // RelativeLayout params were very useful here
        // the addRule method is the key to arrange the components properly
        RelativeLayout.LayoutParams componentB_Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        componentB_Params.addRule(RelativeLayout.RIGHT_OF, this.tableAProd_Frag.getId());
        RelativeLayout.LayoutParams componentC_Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        componentC_Params.addRule(RelativeLayout.BELOW, this.tableAProd_Frag.getId());
        RelativeLayout.LayoutParams componentD_Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        componentD_Params.addRule(RelativeLayout.RIGHT_OF, scrollViewC.getId());
        componentD_Params.addRule(RelativeLayout.BELOW, horizontalScrollViewB.getId());

        // 'this' is a relative layout,
        // we extend this table layout as relative layout as seen during the creation of this class
        relativeLayout.addView(tableAProd_Frag);
        relativeLayout.addView(horizontalScrollViewB, componentB_Params);
        relativeLayout.addView(scrollViewC, componentC_Params);
        relativeLayout.addView(scrollViewD, componentD_Params);
    }


    private void addTableRowToTableA() {
        tableAProd_Frag.addView(this.componentATableRow());
    }

    private void addTableRowToTableB() {
        tableBProd_Frag.addView(this.componentBTableRow());
    }

    // generate table row of table A
    TableRow componentATableRow() {
        TableRow componentATableRow = new TableRow(this.context);
        componentATableRow.setBackgroundColor(Color.parseColor("#2277b1"));
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 0, 0, 0);
        TextView textView = this.headerTextView(headers[0]);
        textView.setBackgroundColor(Color.parseColor("#2277b1"));
        textView.setTextColor(Color.parseColor("#ffffff"));
        textView.setGravity(Gravity.CENTER);
        componentATableRow.addView(textView);
        return componentATableRow;
    }

    // generate table row of table B
    TableRow componentBTableRow() {

        TableRow componentBTableRow = new TableRow(this.context);
        componentBTableRow.setBackgroundColor(Color.parseColor("#2277b1"));
        int headerFieldCount = headers.length;

        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 0, 0, 0);

        for (int x = 0; x < (headerFieldCount - 1); x++) {
            TextView textView = this.headerTextView(this.headers[x + 1]);
           textView.setBackgroundColor(Color.parseColor("#2277b1"));
            textView.setTextColor(Color.parseColor("#ffffff"));
            textView.setLayoutParams(params);
            componentBTableRow.addView(textView);
        }
        return componentBTableRow;
    }

    private void generateTableC_AndTable_B() {

        // just seeing some header cell width
        for (int x = 0; x < this.headerCellsWidth.length; x++) {
         //   Log.v("Product Data", this.headerCellsWidth[x] + "");
        }

        for (int k = 0; k < productNameBeanArrayList.size(); k++) {
            final TableRow tableRowForTableCProd_Frag;
            tableRowForTableCProd_Frag = this.tableRowForTableCProd_Frag(productNameBeanArrayList.get(k).getLevel());
            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT, 120);
            params.setMargins(2, 0, 0, 0);

            final TableRow tableRowForTableDProd_Frag = this.tableRowForTableDProd_Frag(productNameBeanArrayList.get(k));
            tableRowForTableCProd_Frag.setBackgroundColor(Color.parseColor("#dfdedf"));
            tableRowForTableDProd_Frag.setBackgroundColor(Color.parseColor("#dfdedf"));
            final int i = k;

            tableRowForTableCProd_Frag.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    KeyProductPlanActivity.productName = productNameBeanArrayList.get(i).getLevel();
                   // KeyProductPlanActivity.segClick = prodsegClick;
                    Log.e("segmt click",""+prodsegClick);
                    relPlanProd_Frag.setVisibility(View.GONE);
                    ViewPager viewPager = (ViewPager) view.getParent();
                    LinearLayout layout = (LinearLayout) viewPager.getParent();
                    TabLayout tab = (TabLayout) layout.getChildAt(1);
                    if (tab.getTabCount() == 3) {
                        tab.removeTabAt(2);
                    }
                    if(tab.getTabCount() == 2) {
                       tab.removeTabAt(1);
                    }
                    tab.addTab(tab.newTab().setText("OPTION"));
                    tab.getTabAt(1).select();
                    rowPressListener.communicateToFragment2(productNameBeanArrayList.get(i).getLevel(),prodsegClick);
                }
            });
            this.tableCProd_Frag.addView(tableRowForTableCProd_Frag);
            this.tableDProd_Frag.addView(tableRowForTableDProd_Frag);
        }
        Reusable_Functions.hDialog();
    }

    TableRow tableRowForTableCProd_Frag(String productNameDetails) {

        TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], TableRow.LayoutParams.MATCH_PARENT);
        params.setMargins(2, 2, 0, 0);
        TableRow tableRowForTableCProd_Frag = new TableRow(this.context);
        TextView textView = this.bodyTextView(productNameDetails);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
     //   Log.e("Value", textView.getText().toString());
        tableRowForTableCProd_Frag.addView(textView, params);
        return tableRowForTableCProd_Frag;
    }

    TableRow tableRowForTableDProd_Frag(KeyPlanProductBean productDetails) {


        TableRow tableRowForTableDProd_Frag = new TableRow(this.context);
        int loopCount = ((TableRow) this.tableBProd_Frag.getChildAt(0)).getChildCount();
        NumberFormat format = NumberFormat.getNumberInstance(new Locale("", "in"));
        String info[] = {
                String.valueOf(Math.round(productDetails.getPvaSales())).concat("%"),
                String.valueOf(Math.round(productDetails.getPvaStock())).concat("%"),
                String.valueOf(format.format(Math.round(productDetails.getPlanSaleTotQty()))),
                String.valueOf(format.format(Math.round(productDetails.getSaleTotQty()))),
                String.valueOf(format.format(Math.round(productDetails.getPlanTargetStockQty()))),
                String.valueOf(format.format(Math.round(productDetails.getInvClosingQty()))),
            };

        for (int x = 0; x < loopCount; x++) {

            TableRow.LayoutParams params = new TableRow.LayoutParams(headerCellsWidth[x + 1], TableRow.LayoutParams.MATCH_PARENT);
            params.setMargins(2, 2, 0, 0);
            TextView textViewB = this.bodyTextView(info[x]);
            textViewB.setTextColor(Color.WHITE);
            if(productDetails.getPvaSales() >= Double.parseDouble("100"))
            {
                textViewB.setBackgroundColor(Color.parseColor("#70e503"));
            }
            else if(productDetails.getPvaSales() > Double.parseDouble("80") && productDetails.getPvaSales() < Double.parseDouble("100"))
            {
                textViewB.setBackgroundColor(Color.parseColor("#ff7e00"));

            }else if(productDetails.getPvaSales() < Double.parseDouble("80"))
            {
                textViewB.setBackgroundColor(Color.parseColor("#fe0000"));
            }

            tableRowForTableDProd_Frag.addView(textViewB, params);
        }

        return tableRowForTableDProd_Frag;

    }

    // table cell standard TextView
    TextView bodyTextView(String label) {

        TextView bodyTextView = new TextView(context);
        bodyTextView.setBackgroundColor(Color.parseColor("#f8f6f6"));
        bodyTextView.setText(label);
        bodyTextView.setTextSize(12f);
        bodyTextView.setGravity(Gravity.CENTER);
        bodyTextView.setPadding(5, 5, 5, 5);
        return bodyTextView;
    }

    // header standard TextView
    TextView headerTextView(String label) {

        TextView headerTextView = new TextView(this.context);
        headerTextView.setBackgroundColor(Color.parseColor("#f8f6f6"));
        headerTextView.setText(label);
        headerTextView.setGravity(Gravity.CENTER);
        headerTextView.setPadding(5, 5, 5, 5);
        return headerTextView;
    }

    // resizing TableRow height starts here
    void resizeHeaderHeight() {
        TableRow productNameHeaderTableRow = (TableRow) this.tableAProd_Frag.getChildAt(0);
        TableRow productInfoTableRow = (TableRow) this.tableBProd_Frag.getChildAt(0);
        int rowAHeight = this.viewHeight(productNameHeaderTableRow);
        int rowBHeight = this.viewHeight(productInfoTableRow);
        TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
        int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;
        this.matchLayoutHeight(tableRow, finalHeight);
    }

    void getTableRowHeaderCellWidth() {
        int tableAChildCount = ((TableRow) this.tableAProd_Frag.getChildAt(0)).getChildCount();
        int tableBChildCount = ((TableRow) this.tableBProd_Frag.getChildAt(0)).getChildCount();
        for (int x = 0; x < (tableAChildCount + tableBChildCount); x++) {
            if (x == 0) {
                this.headerCellsWidth[x] = this.viewWidth(((TableRow) this.tableAProd_Frag.getChildAt(0)).getChildAt(x));
            } else {
                this.headerCellsWidth[x] = this.viewWidth(((TableRow) this.tableBProd_Frag.getChildAt(0)).getChildAt(x - 1));
            }

        }
    }

    // resize body table row height
    void resizeBodyTableRowHeight() {

        int tableC_ChildCount = this.tableCProd_Frag.getChildCount();

        for (int x = 0; x < tableC_ChildCount; x++) {

            TableRow productNameHeaderTableRow = (TableRow) this.tableCProd_Frag.getChildAt(x);
            TableRow productInfoTableRow = (TableRow) this.tableDProd_Frag.getChildAt(x);

            int rowAHeight = this.viewHeight(productNameHeaderTableRow);
            int rowBHeight = this.viewHeight(productInfoTableRow);

            TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
            int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;

            this.matchLayoutHeight(tableRow, finalHeight);
        }
    }

    // match all height in a table row
    // to make a standard TableRow height
    private void matchLayoutHeight(TableRow tableRow, int height) {

        int tableRowChildCount = tableRow.getChildCount();

        // if a TableRow has only 1 child
        if (tableRow.getChildCount() == 1) {
            View view = tableRow.getChildAt(0);
            TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();
            params.height = height - (params.bottomMargin + params.topMargin);
            return;
        }

        // if a TableRow has more than 1 child
        for (int x = 0; x < tableRowChildCount; x++) {
            View view = tableRow.getChildAt(x);
            TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();
            if (!isTheHeighestLayout(tableRow, x)) {
                params.height = height - (params.bottomMargin + params.topMargin);
                return;
            }
        }
    }

    // check if the view has the highest height in a TableRow
    private boolean isTheHeighestLayout(TableRow tableRow, int layoutPosition) {

        int tableRowChildCount = tableRow.getChildCount();
        int heighestViewPosition = -1;
        int viewHeight = 0;

        for (int x = 0; x < tableRowChildCount; x++) {
            View view = tableRow.getChildAt(x);
            int height = this.viewHeight(view);

            if (viewHeight < height) {
                heighestViewPosition = x;
                viewHeight = height;
            }
        }

        return heighestViewPosition == layoutPosition;
    }

    // read a view's height
    private int viewHeight(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return view.getMeasuredHeight();
    }

    // read a view's width
    private int viewWidth(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return view.getMeasuredWidth();
    }



    // horizontal scroll view custom class
    class MyHorizontalScrollView extends HorizontalScrollView {

        public MyHorizontalScrollView(Context context) {
            super(context);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            String tag = (String) this.getTag();

            if (tag.equalsIgnoreCase("horizontal scroll view b")) {
                horizontalScrollViewD.scrollTo(l, 0);
            } else {
                horizontalScrollViewB.scrollTo(l, 0);
            }
        }
    }

    // scroll view custom class
    class MyScrollView extends ScrollView {

        public MyScrollView(Context context) {
            super(context);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {

            String tag = (String) this.getTag();
            if (tag.equalsIgnoreCase("scroll view c")) {
                scrollViewD.scrollTo(0, t);
            } else {
                scrollViewC.scrollTo(0, t);
            }
        }
    }


    private void requestFilterProductAPI(final int offset, int limit1) {
        String url = ConstsCore.web_url + "/v1/display/keyproductsplan/" + userId + "?view=" + prodsegClick + "&productName="+filterProductValues.replace(" ","%20").replaceAll("&", "%26") +"&level=" + planlevel + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("filtre product Url   ", url + " " + bearertoken);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("PlanProductFilter Response", " " + response.toString() + "\nlength: " + response.length());
                        try {
                            int i;
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no product data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {
                                for (i = 0; i < response.length(); i++) {

                                    JSONObject productName1 = response.getJSONObject(i);
                                    String level = productName1.getString("level");
                                    double planSaleNetVal = productName1.getDouble("planSaleNetVal");
                                    double planSaleTotQty = productName1.getDouble("planSaleTotQty");
                                    double planTargetStockQty = productName1.getDouble("planTargetStockQty");
                                    double saleNetVal = productName1.getDouble("saleNetVal");
                                    double saleTotQty = productName1.getDouble("saleTotQty");
                                    double invClosingQty = productName1.getDouble("invClosingQty");
                                    double pvaSales = productName1.getDouble("pvaSales");
                                    double pvaStock = productName1.getDouble("pvaStock");
                                    String storeCode = productName1.getString("storeCode");
                                    String storeDesc = productName1.getString("storeDesc");
                                    String achColor = productName1.getString("achColor");
                                    productNameBean = new KeyPlanProductBean();
                                    productNameBean.setLevel(level);
                                    productNameBean.setPlanSaleNetVal(planSaleNetVal);
                                    productNameBean.setPlanSaleTotQty(planSaleTotQty);
                                    productNameBean.setInvClosingQty(invClosingQty);
                                    productNameBean.setPlanTargetStockQty(planTargetStockQty);
                                    productNameBean.setSaleNetVal(saleNetVal);
                                    productNameBean.setSaleTotQty(saleTotQty);
                                    productNameBean.setPvaSales(pvaSales);
                                    productNameBean.setPvaStock(pvaStock);
                                    productNameBean.setStoreCode(storeCode);
                                    productNameBean.setStoreDesc(storeDesc);
                                    productNameBeanArrayList.add(productNameBean);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestFilterProductAPI(offsetvalue, limit);
                            } else if (response.length() < limit) {
                                for (i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String level = productName1.getString("level");
                                    double planSaleNetVal = productName1.getDouble("planSaleNetVal");
                                    double planSaleTotQty = productName1.getDouble("planSaleTotQty");
                                    double planTargetStockQty = productName1.getDouble("planTargetStockQty");
                                    double saleNetVal = productName1.getDouble("saleNetVal");
                                    double saleTotQty = productName1.getDouble("saleTotQty");
                                    double invClosingQty = productName1.getDouble("invClosingQty");
                                    double pvaSales = productName1.getDouble("pvaSales");
                                    double pvaStock = productName1.getDouble("pvaStock");
                                    String storeCode = productName1.getString("storeCode");
                                    String storeDesc = productName1.getString("storeDesc");
                                    String achColor = productName1.getString("achColor");
                                    productNameBean = new KeyPlanProductBean();
                                    productNameBean.setLevel(level);
                                    productNameBean.setPlanSaleNetVal(planSaleNetVal);
                                    productNameBean.setPlanSaleTotQty(planSaleTotQty);
                                    productNameBean.setInvClosingQty(invClosingQty);
                                    productNameBean.setPlanTargetStockQty(planTargetStockQty);
                                    productNameBean.setSaleNetVal(saleNetVal);
                                    productNameBean.setSaleTotQty(saleTotQty);
                                    productNameBean.setPvaSales(pvaSales);
                                    productNameBean.setPvaStock(pvaStock);
                                    productNameBean.setStoreCode(storeCode);
                                    productNameBean.setStoreDesc(storeDesc);
                                    productNameBeanArrayList.add(productNameBean);
                                }


                                txtStoreCode.setText(productNameBeanArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(productNameBeanArrayList.get(0).getStoreDesc());
                                addTableRowToTableA();
                                addTableRowToTableB();
                                resizeHeaderHeight();
                                getTableRowHeaderCellWidth();
                                generateTableC_AndTable_B();
                                resizeBodyTableRowHeight();
                                plantoggleClick=false;
                            }
                        } catch (Exception e) {

                            e.printStackTrace();
                            plantoggleClick=false;

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        error.printStackTrace();
                        plantoggleClick=false;

                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + bearertoken);

                Log.e("params ", " " + params);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void requestPlanProductAPI(final int offset, int limit1) {
        String url = ConstsCore.web_url + "/v1/display/keyproductsplan/" + userId + "?view=" + prodsegClick + "&level=" + planlevel + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("URL   ", url + " " + bearertoken);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("PlanProductName Response", " " + response.toString() + "\nlength: " + response.length());
                        try {
                            int i;
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no product data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {
                                for (i = 0; i < response.length(); i++) {

                                    JSONObject productName1 = response.getJSONObject(i);
                                    String level = productName1.getString("level");
                                    double planSaleNetVal = productName1.getDouble("planSaleNetVal");
                                    double planSaleTotQty = productName1.getDouble("planSaleTotQty");
                                    double planTargetStockQty = productName1.getDouble("planTargetStockQty");
                                    double saleNetVal = productName1.getDouble("saleNetVal");
                                    double saleTotQty = productName1.getDouble("saleTotQty");
                                    double invClosingQty = productName1.getDouble("invClosingQty");
                                    double pvaSales = productName1.getDouble("pvaSales");
                                    double pvaStock = productName1.getDouble("pvaStock");
                                    String storeCode = productName1.getString("storeCode");
                                    String storeDesc = productName1.getString("storeDesc");
                                    String achColor = productName1.getString("achColor");
                                    productNameBean = new KeyPlanProductBean();
                                    productNameBean.setLevel(level);
                                    productNameBean.setPlanSaleNetVal(planSaleNetVal);
                                    productNameBean.setPlanSaleTotQty(planSaleTotQty);
                                    productNameBean.setInvClosingQty(invClosingQty);
                                    productNameBean.setPlanTargetStockQty(planTargetStockQty);
                                    productNameBean.setSaleNetVal(saleNetVal);
                                    productNameBean.setSaleTotQty(saleTotQty);
                                    productNameBean.setPvaSales(pvaSales);
                                    productNameBean.setPvaStock(pvaStock);
                                    productNameBean.setStoreCode(storeCode);
                                    productNameBean.setStoreDesc(storeDesc);
                                    productNameBeanArrayList.add(productNameBean);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestPlanProductAPI(offsetvalue, limit);
                            } else if (response.length() < limit) {
                                for (i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String level = productName1.getString("level");
                                    double planSaleNetVal = productName1.getDouble("planSaleNetVal");
                                    double planSaleTotQty = productName1.getDouble("planSaleTotQty");
                                    double planTargetStockQty = productName1.getDouble("planTargetStockQty");
                                    double saleNetVal = productName1.getDouble("saleNetVal");
                                    double saleTotQty = productName1.getDouble("saleTotQty");
                                    double invClosingQty = productName1.getDouble("invClosingQty");
                                    double pvaSales = productName1.getDouble("pvaSales");
                                    double pvaStock = productName1.getDouble("pvaStock");
                                    String storeCode = productName1.getString("storeCode");
                                    String storeDesc = productName1.getString("storeDesc");
                                    String achColor = productName1.getString("achColor");
                                    productNameBean = new KeyPlanProductBean();
                                    productNameBean.setLevel(level);
                                    productNameBean.setPlanSaleNetVal(planSaleNetVal);
                                    productNameBean.setPlanSaleTotQty(planSaleTotQty);
                                    productNameBean.setInvClosingQty(invClosingQty);
                                    productNameBean.setPlanTargetStockQty(planTargetStockQty);
                                    productNameBean.setSaleNetVal(saleNetVal);
                                    productNameBean.setSaleTotQty(saleTotQty);
                                    productNameBean.setPvaSales(pvaSales);
                                    productNameBean.setPvaStock(pvaStock);
                                    productNameBean.setStoreCode(storeCode);
                                    productNameBean.setStoreDesc(storeDesc);
                                    productNameBeanArrayList.add(productNameBean);
                                }

                                txtStoreCode.setText(productNameBeanArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(productNameBeanArrayList.get(0).getStoreDesc());
                                addTableRowToTableA();
                                addTableRowToTableB();
                                resizeHeaderHeight();
                                getTableRowHeaderCellWidth();
                                generateTableC_AndTable_B();
                                resizeBodyTableRowHeight();
                                plantoggleClick=false;
                            }
                        } catch (Exception e) {

                            e.printStackTrace();
                            plantoggleClick=false;

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        error.printStackTrace();
                        plantoggleClick=false;

                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + bearertoken);

                Log.e("params ", " " + params);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void requestPlanProductAchColorAPI(final int offset, int limit1) {
        String url = ConstsCore.web_url + "/v1/display/keyproductsplan/" + userId + "?view=" + prodsegClick + "&level=" + planlevel + "&achColor=" + achColor +"&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("URL   ", url + " " + bearertoken);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("PlanProduct Ach Color Response", " " + response.toString() + "\nlength: " + response.length());
                        try {
                            int i;
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "no product data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {
                                for (i = 0; i < response.length(); i++) {

                                    JSONObject productName1 = response.getJSONObject(i);
                                    String level = productName1.getString("level");
                                    double planSaleNetVal = productName1.getDouble("planSaleNetVal");
                                    double planSaleTotQty = productName1.getDouble("planSaleTotQty");
                                    double planTargetStockQty = productName1.getDouble("planTargetStockQty");
                                    double saleNetVal = productName1.getDouble("saleNetVal");
                                    double saleTotQty = productName1.getDouble("saleTotQty");
                                    double invClosingQty = productName1.getDouble("invClosingQty");
                                    double pvaSales = productName1.getDouble("pvaSales");
                                    double pvaStock = productName1.getDouble("pvaStock");
                                    String storeCode = productName1.getString("storeCode");
                                    String storeDesc = productName1.getString("storeDesc");
                                    String achColor = productName1.getString("achColor");

                                    productNameBean = new KeyPlanProductBean();
                                    productNameBean.setLevel(level);
                                    productNameBean.setPlanSaleNetVal(planSaleNetVal);
                                    productNameBean.setPlanSaleTotQty(planSaleTotQty);
                                    productNameBean.setInvClosingQty(invClosingQty);
                                    productNameBean.setPlanTargetStockQty(planTargetStockQty);
                                    productNameBean.setSaleNetVal(saleNetVal);
                                    productNameBean.setSaleTotQty(saleTotQty);
                                    productNameBean.setPvaSales(pvaSales);
                                    productNameBean.setPvaStock(pvaStock);
                                    productNameBean.setStoreCode(storeCode);
                                    productNameBean.setStoreDesc(storeDesc);
                                    productNameBeanArrayList.add(productNameBean);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestPlanProductAchColorAPI(offsetvalue, limit);
                            } else if (response.length() < limit) {
                                for (i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String level = productName1.getString("level");
                                    double planSaleNetVal = productName1.getDouble("planSaleNetVal");
                                    double planSaleTotQty = productName1.getDouble("planSaleTotQty");
                                    double planTargetStockQty = productName1.getDouble("planTargetStockQty");
                                    double saleNetVal = productName1.getDouble("saleNetVal");
                                    double saleTotQty = productName1.getDouble("saleTotQty");
                                    double invClosingQty = productName1.getDouble("invClosingQty");
                                    double pvaSales = productName1.getDouble("pvaSales");
                                    double pvaStock = productName1.getDouble("pvaStock");
                                    String storeCode = productName1.getString("storeCode");
                                    String storeDesc = productName1.getString("storeDesc");
                                    String achColor = productName1.getString("achColor");

                                    productNameBean = new KeyPlanProductBean();
                                    productNameBean.setLevel(level);
                                    productNameBean.setPlanSaleNetVal(planSaleNetVal);
                                    productNameBean.setPlanSaleTotQty(planSaleTotQty);
                                    productNameBean.setInvClosingQty(invClosingQty);
                                    productNameBean.setPlanTargetStockQty(planTargetStockQty);
                                    productNameBean.setSaleNetVal(saleNetVal);
                                    productNameBean.setSaleTotQty(saleTotQty);
                                    productNameBean.setPvaSales(pvaSales);
                                    productNameBean.setPvaStock(pvaStock);
                                    productNameBean.setStoreCode(storeCode);
                                    productNameBean.setStoreDesc(storeDesc);
                                    productNameBeanArrayList.add(productNameBean);
                                }

                                txtStoreCode.setText(productNameBeanArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(productNameBeanArrayList.get(0).getStoreDesc());
                                addTableRowToTableA();
                                addTableRowToTableB();
                                resizeHeaderHeight();
                                getTableRowHeaderCellWidth();
                                generateTableC_AndTable_B();
                                resizeBodyTableRowHeight();
                                plantoggleClick=false;
                            }
                        } catch (Exception e) {

                            e.printStackTrace();
                            plantoggleClick=false;

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        error.printStackTrace();
                        plantoggleClick=false;

                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + bearertoken);

                Log.e("params ", " " + params);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

}
