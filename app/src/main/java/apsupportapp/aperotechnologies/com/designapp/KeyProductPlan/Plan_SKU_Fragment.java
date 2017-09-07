package apsupportapp.aperotechnologies.com.designapp.KeyProductPlan;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.MySingleton;

import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by pamrutkar on 27/02/17.
 */
public class Plan_SKU_Fragment extends Fragment implements TabLayout.OnTabSelectedListener{

    public  TableLayout tableAPlanSku;
    public  TableLayout tableBPlanSku;
    public  TableLayout tableCPlanSku;
    public TableLayout tableDPlanSku;
    SegmentedGroup segmentedGroupSku;
    RadioButton sku_btnWTD,sku_btnLW;
    ViewGroup skuview;
    HorizontalScrollView horizontalScrollViewB;
    HorizontalScrollView horizontalScrollViewD;
    ArrayList<KeyPlanProductBean> productNameBeanArrayList;
    KeyPlanProductBean keyPlanProductBean;
    Gson gson;
    String sku_seg_clk = "WTD";
    int planlevel = 3;
    String userId, bearertoken,storeDescription;
    ScrollView scrollViewC;
    ScrollView scrollViewD;
    RequestQueue queue;
    OnRowPressListener1 rowPressListener;
    Context context;
    RelativeLayout sku_relativeLayout;
    public static RelativeLayout skurel;
    Button txtSkuGreen, txtSkuRed,txtSkuAmber;
    String prodnm;
    private CardView table_area;
    // set the header titles
    private boolean skutoggleClick=false;
    String headers[] = {
            "Size    ",
            " PvA\n\t\tSales%  ",
            "   PvA\n\t\tStk%  ",
            "   Plan\n\tSales  ",
            "  Sales\n\t(U)  ",
            "   Plan\n\tStk  ",
            "    Stk\n\t\t(U)   "


    };

    int headerCellsWidth[] = new int[headers.length];

    TextView txtStoreCode, txtStoreDesc;
    MySingleton m_config;

    int offsetvalue = 0, limit = 100;
    int count = 0;
    SharedPreferences sharedPreferences;
    TextView txtOptionName;
     String optionName,achColor;
    private TabLayout Tabview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_config = MySingleton.getInstance(getActivity());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        storeDescription = sharedPreferences.getString("storeDescription","");

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        skuview = (ViewGroup) inflater.inflate(R.layout.planactual_sku_fragment, container, false);
        context = skuview.getContext();
        sku_relativeLayout = (RelativeLayout) skuview.findViewById(R.id.planactual_skurelLayout);
        sku_relativeLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        txtOptionName = (TextView) skuview.findViewById(R.id.opt_name_val);
        optionName = "";
        txtStoreCode = (TextView) skuview.findViewById(R.id.txtStoreCode);
        txtStoreDesc = (TextView) skuview.findViewById(R.id.txtStoreName);
        txtStoreCode.setText(storeDescription.trim().substring(0,4));
        txtStoreDesc.setText(storeDescription.substring(5));
        txtSkuGreen =(Button)skuview.findViewById(R.id.txtSkuGreen);
        txtSkuRed =(Button)skuview.findViewById(R.id.txtSkuRed);
        txtSkuAmber =(Button)skuview.findViewById(R.id.txtSkuAmber);
        Tabview = (TabLayout)skuview.findViewById(R.id.tabview);
        table_area = (CardView) skuview.findViewById(R.id.table_area);
        table_area.setVisibility(View.GONE);
        Tabview.addTab(Tabview.newTab().setText("WTD"));
        Tabview.addTab(Tabview.newTab().setText("LW"));
        Tabview.setOnTabSelectedListener(this);

        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        prodnm = KeyProductPlanActivity.productName;
        skurel = (RelativeLayout) skuview.findViewById(R.id.planskuactual_rel);
        //segmentedGroupSku = (SegmentedGroup) skuview.findViewById(R.id.segmentedGrpSku);
        //sku_btnWTD = (RadioButton) skuview.findViewById(R.id.planactual_skubtnWTD);

       // sku_btnLW = (RadioButton) skuview.findViewById(R.id.planactual_skubtnLW);
     /*   segmentedGroupSku.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


            }
        });*/

        txtSkuGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                skurel.setVisibility(View.VISIBLE);
                tableAPlanSku.removeAllViews();
                tableBPlanSku.removeAllViews();
                tableCPlanSku.removeAllViews();
                tableDPlanSku.removeAllViews();
                if (Reusable_Functions.chkStatus(context)) {

                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading data...");
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    planlevel = 3;
                    achColor = "Green";
                    productNameBeanArrayList = new ArrayList<KeyPlanProductBean>();
                    retainSegmntVal();
                    requestPlanSkuAchColorAPI(offsetvalue, limit);

                }
            }

        });
        txtSkuRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skurel.setVisibility(View.VISIBLE);
                tableAPlanSku.removeAllViews();
                tableBPlanSku.removeAllViews();
                tableCPlanSku.removeAllViews();
                tableDPlanSku.removeAllViews();
                if (Reusable_Functions.chkStatus(context)) {

                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading data...");
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    planlevel = 3;
                    achColor = "Red";
                    productNameBeanArrayList = new ArrayList<KeyPlanProductBean>();
                    retainSegmntVal();
                    requestPlanSkuAchColorAPI(offsetvalue, limit);
                }
            }

        });
        txtSkuAmber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skurel.setVisibility(View.VISIBLE);
                tableAPlanSku.removeAllViews();
                tableBPlanSku.removeAllViews();
                tableCPlanSku.removeAllViews();
                tableDPlanSku.removeAllViews();
                if (Reusable_Functions.chkStatus(context)) {

                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading data...");
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    planlevel = 3;
                    achColor = "Amber";
                    productNameBeanArrayList = new ArrayList<KeyPlanProductBean>();
                    retainSegmntVal();
                    requestPlanSkuAchColorAPI(offsetvalue, limit);


                }
            }

        });



        initComponents();
        setComponentsId();
        setScrollViewAndHorizontalScrollViewTag();
        gson = new Gson();
        horizontalScrollViewB.addView(tableBPlanSku);
        scrollViewC.addView(tableCPlanSku);
        scrollViewD.addView(horizontalScrollViewD);
        horizontalScrollViewD.addView(tableDPlanSku);
        // add the components to be part of the main layout
        addComponentToMainLayout();
        //int headerCellsWidth[] = new int[headers.length];
        return skuview;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            rowPressListener = (OnRowPressListener1) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement IFragmentToActivity");
        }
    }

    public void fragment_Communication(String level,String optionsegmentclick) {

        optionName = level;
        sku_seg_clk = optionsegmentclick;
        txtOptionName.setText(level);
        if (Reusable_Functions.chkStatus(context)) {
            LinearLayout layout = (LinearLayout) KeyProductPlanActivity.plan_pager.getParent();
            TabLayout tab = (TabLayout) layout.getChildAt(1);
            if (tab.getTabCount() == 3) {
                tab.removeTabAt(1);
            }
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            offsetvalue = 0;
            limit = 100;
            count = 0;
            productNameBeanArrayList = new ArrayList<KeyPlanProductBean>();
            planlevel = 3;
            retainSegmntVal();
            requestPlanSkuAPI(offsetvalue, limit);
        } else {
            Toast.makeText(getContext(), "Check your network connectivity", Toast.LENGTH_LONG).show();
        }
    }

    private void retainSegmntVal() {
       if(sku_seg_clk.equals("WTD"))
        {
         //  sku_btnWTD.toggle();


        }else
        {
           // sku_btnLW.toggle();

        }

    }
    // initalized components
    private void initComponents() {

        tableAPlanSku = new TableLayout(this.context);
        tableBPlanSku = new TableLayout(this.context);
        tableCPlanSku = new TableLayout(this.context);
        tableDPlanSku = new TableLayout(this.context);

        horizontalScrollViewB = new MyHorizontalScrollView(this.context);
        horizontalScrollViewD = new MyHorizontalScrollView(this.context);

        scrollViewC = new MyScrollView(this.context);
        scrollViewD = new MyScrollView(this.context);

        tableAPlanSku.setBackgroundColor(Color.parseColor("#ffffff"));
        horizontalScrollViewB.setBackgroundColor(Color.parseColor("#ffffff"));
    }

    // set essential component IDs
    @SuppressWarnings("ResourceType")
    private void setComponentsId() {
        tableAPlanSku.setId(1);
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
        componentB_Params.addRule(RelativeLayout.RIGHT_OF, tableAPlanSku.getId());

        RelativeLayout.LayoutParams componentC_Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        componentC_Params.addRule(RelativeLayout.BELOW, tableAPlanSku.getId());

        RelativeLayout.LayoutParams componentD_Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        componentD_Params.addRule(RelativeLayout.RIGHT_OF, scrollViewC.getId());
        componentD_Params.addRule(RelativeLayout.BELOW, horizontalScrollViewB.getId());


        sku_relativeLayout.addView(tableAPlanSku);
       sku_relativeLayout.addView(horizontalScrollViewB, componentB_Params);
        sku_relativeLayout.addView(scrollViewC, componentC_Params);
       sku_relativeLayout.addView(scrollViewD, componentD_Params);

    }

    private void addTableRowToTableA() {
        tableAPlanSku.addView(this.componentATableRow());
    }

    private void addTableRowToTableB() {
        tableBPlanSku.addView(this.componentBTableRow());
    }

    // generate table row of table A
    TableRow componentATableRow() {

        TableRow componentATableRow = new TableRow(this.context);
        componentATableRow.setBackgroundColor(Color.parseColor("#dfdedf"));
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 0, 0, 0);

        TextView textView = this.headerTextView(headers[0]);
        textView.setBackgroundColor(Color.parseColor("#f8f6f6"));
        textView.setTextColor(Color.parseColor("#000000"));
        textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        componentATableRow.addView(textView);

        return componentATableRow;
    }

    // generate table row of table B
    TableRow componentBTableRow() {

        TableRow componentBTableRow = new TableRow(this.context);
        componentBTableRow.setBackgroundColor(Color.parseColor("#dfdedf"));
        int headerFieldCount = headers.length;

        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 0, 0, 0);

        for (int x = 0; x < (headerFieldCount - 1); x++) {
            TextView textView = this.headerTextView(this.headers[x + 1]);
            textView.setBackgroundColor(Color.parseColor("#f8f6f6"));
            textView.setTextColor(Color.parseColor("#000000"));
            textView.setLayoutParams(params);
            componentBTableRow.addView(textView);
        }

        return componentBTableRow;
    }

    private void generateTableC_AndTable_B() {

        // just seeing some header cell width


        for (KeyPlanProductBean aProductNameBeanArrayList : productNameBeanArrayList) {
            final TableRow tableRowForTableC = this.tableRowForTableC(aProductNameBeanArrayList.getLevel());
            final TableRow taleRowForTableD = this.taleRowForTableD(aProductNameBeanArrayList);
            tableRowForTableC.setBackgroundColor(Color.parseColor("#ffffff"));
            taleRowForTableD.setBackgroundColor(Color.parseColor("#dfdedf"));
            this.tableCPlanSku.addView(tableRowForTableC);
            this.tableDPlanSku.addView(taleRowForTableD);
        }
        Reusable_Functions.hDialog();
    }


    TableRow tableRowForTableC(String productNameDetails) {

        TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], TableRow.LayoutParams.MATCH_PARENT);
        params.setMargins(0, 2, 0, 0);
        TableRow tableRowForTableC = new TableRow(this.context);
        TextView textView = this.bodyTextView(productNameDetails);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        textView.setTextColor(Color.parseColor("#000000"));
        tableRowForTableC.addView(textView, params);
        return tableRowForTableC;

    }

    TableRow taleRowForTableD(KeyPlanProductBean productDetails) {
        TableRow taleRowForTableD = new TableRow(this.context);
        int loopCount = ((TableRow) tableBPlanSku.getChildAt(0)).getChildCount();
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
            textViewB.setTextColor(Color.parseColor("#000000"));
            textViewB.setTypeface(Typeface.DEFAULT);
            if (taleRowForTableD.getChildAt(0) != null)
            {
                TextView txtPvaSales = (TextView) taleRowForTableD.getChildAt(0);
                if (productDetails.getPvaSales() >= Double.parseDouble("100")) {
                    txtPvaSales.setBackgroundColor(Color.parseColor("#70e503"));
                    txtPvaSales.setTextColor(Color.parseColor("#ffffff"));
                } else if (productDetails.getPvaSales() > Double.parseDouble("80") && productDetails.getPvaSales() < Double.parseDouble("100")) {
                    txtPvaSales.setBackgroundColor(Color.parseColor("#ff7e00"));
                    txtPvaSales.setTextColor(Color.parseColor("#ffffff"));
                } else if (productDetails.getPvaSales() < Double.parseDouble("80")) {
                    txtPvaSales.setBackgroundColor(Color.parseColor("#fe0000"));
                    txtPvaSales.setTextColor(Color.parseColor("#ffffff"));
                }

            }
            if (taleRowForTableD.getChildAt(1) != null) {
                TextView txtPvaStock = (TextView) taleRowForTableD.getChildAt(1);
                if (productDetails.getPvaStock() >= Double.parseDouble("100")) {
                    txtPvaStock.setBackgroundColor(Color.parseColor("#70e503"));
                    txtPvaStock.setTextColor(Color.parseColor("#ffffff"));
                } else if (productDetails.getPvaStock() > Double.parseDouble("80") && productDetails.getPvaStock() < Double.parseDouble("100")) {
                    txtPvaStock.setBackgroundColor(Color.parseColor("#ff7e00"));
                    txtPvaStock.setTextColor(Color.parseColor("#ffffff"));
                } else if (productDetails.getPvaStock() < Double.parseDouble("80")) {
                    txtPvaStock.setBackgroundColor(Color.parseColor("#fe0000"));
                    txtPvaStock.setTextColor(Color.parseColor("#ffffff"));
                }

            }
            taleRowForTableD.addView(textViewB, params);
        }

        return taleRowForTableD;

    }

    // table cell standard TextView
    TextView bodyTextView(String label) {

        TextView bodyTextView = new TextView(this.context);
        bodyTextView.setBackgroundColor(Color.parseColor("#ffffff"));
        bodyTextView.setText(label);
        bodyTextView.setTextSize(12f);
        bodyTextView.setGravity(Gravity.CENTER);
        bodyTextView.setPadding(5, 5, 5, 5);

        return bodyTextView;
    }

    // header standard TextView
    TextView headerTextView(String label) {

        TextView headerTextView = new TextView(this.context);
        headerTextView.setBackgroundColor(Color.parseColor("#000000"));
        headerTextView.setText(label);
        headerTextView.setTextSize(12f);
        headerTextView.setGravity(Gravity.CENTER);
        headerTextView.setPadding(5, 5, 5, 5);

        return headerTextView;
    }

    // resizing TableRow height starts here
    void resizeHeaderHeight() {

        TableRow productNameHeaderTableRow = (TableRow) tableAPlanSku.getChildAt(0);
        TableRow productInfoTableRow = (TableRow) tableBPlanSku.getChildAt(0);

        int rowAHeight = this.viewHeight(productNameHeaderTableRow);
        int rowBHeight = this.viewHeight(productInfoTableRow);

        TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
        int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;

        this.matchLayoutHeight(tableRow, finalHeight);
    }

    void getTableRowHeaderCellWidth() {

        int tableAChildCount = ((TableRow) tableAPlanSku.getChildAt(0)).getChildCount();
        int tableBChildCount = ((TableRow) tableBPlanSku.getChildAt(0)).getChildCount();

        for (int x = 0; x < (tableAChildCount + tableBChildCount); x++) {

            if (x == 0) {
                this.headerCellsWidth[x] = this.viewWidth(((TableRow) tableAPlanSku.getChildAt(0)).getChildAt(x));
            } else {
                this.headerCellsWidth[x] = this.viewWidth(((TableRow) tableBPlanSku.getChildAt(0)).getChildAt(x - 1));
            }

        }
    }

    // resize body table row height
    void resizeBodyTableRowHeight() {

        int tableC_ChildCount = tableCPlanSku.getChildCount();

        for (int x = 0; x < tableC_ChildCount; x++) {

            TableRow productNameHeaderTableRow = (TableRow) tableCPlanSku.getChildAt(x);
            TableRow productInfoTableRow = (TableRow) tableDPlanSku.getChildAt(x);

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

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int checkedId= Tabview.getSelectedTabPosition();
        Log.e("TAB", "onTabSelected: " );

        switch (checkedId) {
            case 0 :   //WTD selection
                if (sku_seg_clk.equals("WTD"))
                    break;
                sku_seg_clk = "WTD";

                skurel.setVisibility(View.VISIBLE);
                tableAPlanSku.removeAllViews();
                tableBPlanSku.removeAllViews();
                tableCPlanSku.removeAllViews();
                tableDPlanSku.removeAllViews();
                if (Reusable_Functions.chkStatus(context)) {

                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading data...");
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    productNameBeanArrayList = new ArrayList<KeyPlanProductBean>();
                    planlevel = 3;
                    requestPlanSkuAPI(offsetvalue, limit);
                } else {
                    Toast.makeText(getContext(), "Check your network connectivity", Toast.LENGTH_LONG).show();
                }
                break;
            case 1 :  // LW selection
                if (sku_seg_clk.equals("LW"))
                    break;
                sku_seg_clk = "LW";

                skurel.setVisibility(View.VISIBLE);
                tableAPlanSku.removeAllViews();
                tableBPlanSku.removeAllViews();
                tableCPlanSku.removeAllViews();
                tableDPlanSku.removeAllViews();
                if (Reusable_Functions.chkStatus(context)) {

                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading data...");
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    productNameBeanArrayList = new ArrayList<KeyPlanProductBean>();
                    planlevel = 3;
                    requestPlanSkuAPI(offsetvalue, limit);
                } else {
                    Toast.makeText(getContext(), "Check your network connectivity", Toast.LENGTH_LONG).show();
                }
                break;
        }

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

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

    private void requestPlanSkuAPI(final int offset, int limit1) {
        String url = ConstsCore.web_url + "/v1/display/keyproductsplan/" + userId + "?view=" + sku_seg_clk + "&level=" + planlevel +"&option=" + optionName.replaceAll(" ", "%20").replaceAll("&", "%26") +"&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("TAG", "requestPlan_SKU: "+url );
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("TAG", "responsePlan_SKU: "+response );

                        try {
                            int i;
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(getActivity(), "no data found", Toast.LENGTH_LONG).show();
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
                                    keyPlanProductBean = new KeyPlanProductBean();
                                    keyPlanProductBean.setLevel(level);
                                    keyPlanProductBean.setPlanSaleNetVal(planSaleNetVal);
                                    keyPlanProductBean.setPlanSaleTotQty(planSaleTotQty);
                                    keyPlanProductBean.setInvClosingQty(invClosingQty);
                                    keyPlanProductBean.setPlanTargetStockQty(planTargetStockQty);
                                    keyPlanProductBean.setSaleNetVal(saleNetVal);
                                    keyPlanProductBean.setSaleTotQty(saleTotQty);
                                    keyPlanProductBean.setPvaSales(pvaSales);
                                    keyPlanProductBean.setPvaStock(pvaStock);
                                    keyPlanProductBean.setStoreCode(storeCode);
                                    keyPlanProductBean.setStoreDesc(storeDesc);
                                    productNameBeanArrayList.add(keyPlanProductBean);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestPlanSkuAPI(offsetvalue, limit);
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

                                    keyPlanProductBean = new KeyPlanProductBean();
                                    keyPlanProductBean.setLevel(level);
                                    keyPlanProductBean.setPlanSaleNetVal(planSaleNetVal);
                                    keyPlanProductBean.setPlanSaleTotQty(planSaleTotQty);
                                    keyPlanProductBean.setInvClosingQty(invClosingQty);
                                    keyPlanProductBean.setPlanTargetStockQty(planTargetStockQty);
                                    keyPlanProductBean.setSaleNetVal(saleNetVal);
                                    keyPlanProductBean.setSaleTotQty(saleTotQty);
                                    keyPlanProductBean.setPvaSales(pvaSales);
                                    keyPlanProductBean.setPvaStock(pvaStock);
                                    keyPlanProductBean.setStoreCode(storeCode);
                                    keyPlanProductBean.setStoreDesc(storeDesc);
                                    productNameBeanArrayList.add(keyPlanProductBean);

                                }


                                table_area.setVisibility(View.VISIBLE);
                                addTableRowToTableA();
                                addTableRowToTableB();
                                resizeHeaderHeight();
                                getTableRowHeaderCellWidth();
                                generateTableC_AndTable_B();
                                resizeBodyTableRowHeight();
                                Plan_Product.relPlanProd_Frag.removeAllViews();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Reusable_Functions.hDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        error.printStackTrace();
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + bearertoken);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void requestPlanSkuAchColorAPI(final int offset, int limit1) {
        String url = ConstsCore.web_url + "/v1/display/keyproductsplan/" + userId + "?view=" + sku_seg_clk + "&level=" + planlevel +"&option=" + optionName.replaceAll(" ", "%20").replaceAll("&", "%26")+ "&achColor="+ achColor +"&offset=" + offsetvalue + "&limit=" + limit;

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            int i;
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(getActivity(), "no data found", Toast.LENGTH_LONG).show();
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

                                    keyPlanProductBean = new KeyPlanProductBean();
                                    keyPlanProductBean.setLevel(level);
                                    keyPlanProductBean.setPlanSaleNetVal(planSaleNetVal);
                                    keyPlanProductBean.setPlanSaleTotQty(planSaleTotQty);
                                    keyPlanProductBean.setInvClosingQty(invClosingQty);
                                    keyPlanProductBean.setPlanTargetStockQty(planTargetStockQty);
                                    keyPlanProductBean.setSaleNetVal(saleNetVal);
                                    keyPlanProductBean.setSaleTotQty(saleTotQty);
                                    keyPlanProductBean.setPvaSales(pvaSales);
                                    keyPlanProductBean.setPvaStock(pvaStock);
                                    keyPlanProductBean.setStoreCode(storeCode);
                                    keyPlanProductBean.setStoreDesc(storeDesc);
                                    productNameBeanArrayList.add(keyPlanProductBean);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestPlanSkuAchColorAPI(offsetvalue, limit);
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

                                    keyPlanProductBean = new KeyPlanProductBean();
                                    keyPlanProductBean.setLevel(level);
                                    keyPlanProductBean.setPlanSaleNetVal(planSaleNetVal);
                                    keyPlanProductBean.setPlanSaleTotQty(planSaleTotQty);
                                    keyPlanProductBean.setInvClosingQty(invClosingQty);
                                    keyPlanProductBean.setPlanTargetStockQty(planTargetStockQty);
                                    keyPlanProductBean.setSaleNetVal(saleNetVal);
                                    keyPlanProductBean.setSaleTotQty(saleTotQty);
                                    keyPlanProductBean.setPvaSales(pvaSales);
                                    keyPlanProductBean.setPvaStock(pvaStock);
                                    keyPlanProductBean.setStoreCode(storeCode);
                                    keyPlanProductBean.setStoreDesc(storeDesc);
                                    productNameBeanArrayList.add(keyPlanProductBean);

                                }


                                table_area.setVisibility(View.VISIBLE);
                                addTableRowToTableA();
                                addTableRowToTableB();
                                resizeHeaderHeight();
                                getTableRowHeaderCellWidth();
                                generateTableC_AndTable_B();
                                resizeBodyTableRowHeight();
                                Plan_Product.relPlanProd_Frag.removeAllViews();

                            }
                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        error.printStackTrace();
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + bearertoken);

               return params;
            }
        };
        int socketTimeout = 60000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

}
