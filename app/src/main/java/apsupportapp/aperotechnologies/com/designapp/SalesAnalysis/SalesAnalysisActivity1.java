package apsupportapp.aperotechnologies.com.designapp.SalesAnalysis;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.ProductNameBean;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by hasai on 19/09/16.
 */
public class SalesAnalysisActivity1 extends AppCompatActivity {//implements RadioGroup.OnCheckedChangeListener {

//    SegmentedGroup segmentedGroupSales;
//    ViewPager vwpagersales;
//    LinearLayout llayoutSalesAnalysis;
//    RelativeLayout relLayoutSales;
//    Context context;
//    SalesPagerAdapter adapter;
//
//    SharedPreferences sharedPreferences;
//    String userId,bearertoken;
//    ArrayList<ProductNameBean> arrayList;
//    public static String selectedsegValue = "Day";
//
//    int offsetvalue=0,limit=100;
//    int count=0;
//    RequestQueue queue;
//
//    TableLayout tableA_SalesAnalysis;
//    TableLayout tableB_SalesAnalysis;
//    TableLayout tableC_SalesAnalysis;
//    TableLayout tableD_SalesAnalysis;
//
//    HorizontalScrollView horScrB_SalesAnalysis;
//    HorizontalScrollView horScrD_SalesAnalysis;
//    ScrollView scrC_SalesAnalysis;
//    ScrollView scrD_SalesAnalysis;
//    String headers[] = {
//        "              Product Name            ",
//                "    L2H Sls    ",
//                "    Day Sls    ",
//                "    WTD Sls    ",
//                "    Day PvA %    ",
//                "    WTD PvA %    ",
//                "    SOH    ",
//                "    GIT    ",
//
//    };
//    int headerCellsWidth[] = new int[headers.length];
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_salesanalysis);
//
//
//
//        context = this;
//        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//        userId = sharedPreferences.getString("userId","");
//        bearertoken = sharedPreferences.getString("bearerToken","");
//
//        segmentedGroupSales= (SegmentedGroup) findViewById(R.id.segmentedGrp);
//        segmentedGroupSales.setOnCheckedChangeListener(this);
//        llayoutSalesAnalysis = (LinearLayout) findViewById(R.id.llayoutSalesAnalysis);
//
//
//        initialiseUIElements();
//
//
//        int focusposition = 0;
//        LinearLayout lldots = null;
//        adapter = new SalesPagerAdapter(SalesAnalysisActivity1.this, arrayList, focusposition, vwpagersales, lldots);
//        // Binds the Adapter to the ViewPager
//        vwpagersales.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//
//        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
//        Network network = new BasicNetwork(new HurlStack());
//        queue = new RequestQueue(cache, network);
//        queue.start();
//
//
//        if (Reusable_Functions.chkStatus(context)) {
//
//            Reusable_Functions.hDialog();
//            Reusable_Functions.sDialog(context, "Loading data...");
//            offsetvalue = 0;
//            limit = 100;
//            count = 0;
//
//
//            arrayList = new ArrayList<ProductNameBean>();
//            requestProductAPI();
//
//        } else {
//            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
//        }
//
//
//    }
//
//    private void initialiseUIElements() {
//        vwpagersales = (ViewPager) findViewById(R.id.viewpager);
//        relLayoutSales = (RelativeLayout) findViewById(R.id.relTablelayout);
//
//        tableA_SalesAnalysis = new TableLayout(context);
//        tableB_SalesAnalysis = new TableLayout(context);
//        tableC_SalesAnalysis = new TableLayout(context);
//        tableD_SalesAnalysis = new TableLayout(context);
//        horScrB_SalesAnalysis = new MyHorizontalScrollView(context);
//        horScrD_SalesAnalysis = new MyHorizontalScrollView(context);
//        scrC_SalesAnalysis = new MyScrollView(context);
//        scrD_SalesAnalysis = new MyScrollView(context);
//        tableA_SalesAnalysis.setBackgroundColor(Color.GREEN);
//        horScrB_SalesAnalysis.setBackgroundColor(Color.LTGRAY);
//
//
//        setComponentsId();
//        setScrollViewAndHorizontalScrollViewTag();
//        // no need to assemble component A, since it is just a table
//        horScrB_SalesAnalysis.addView(tableB_SalesAnalysis);
//        scrC_SalesAnalysis.addView(tableC_SalesAnalysis);
//        scrD_SalesAnalysis.addView(horScrD_SalesAnalysis);
//        horScrD_SalesAnalysis.addView(tableD_SalesAnalysis);
//
//        addComponentToMainLayout();
//
//
//    }
//
//
//    // set essential component IDs
//    private void setComponentsId() {
//        tableA_SalesAnalysis.setId(1);
//        horScrB_SalesAnalysis.setId(2);
//        scrC_SalesAnalysis.setId(3);
//        scrD_SalesAnalysis.setId(4);
//    }
//
//    // set tags for some horizontal and vertical scroll view
//    private void setScrollViewAndHorizontalScrollViewTag() {
//        horScrB_SalesAnalysis.setTag("horizontal scroll view b");
//        horScrD_SalesAnalysis.setTag("horizontal scroll view d");
//        scrC_SalesAnalysis.setTag("scroll view c");
//        scrD_SalesAnalysis.setTag("scroll view d");
//    }
//
//    // we add the components here in our TableMainLayout
//    private void addComponentToMainLayout() {
//        // RelativeLayout params were very useful here
//        // the addRule method is the key to arrange the components properly
//        RelativeLayout.LayoutParams componentB_Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        componentB_Params.addRule(RelativeLayout.RIGHT_OF, this.tableA_SalesAnalysis.getId());
//        RelativeLayout.LayoutParams componentC_Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        componentC_Params.addRule(RelativeLayout.BELOW, this.tableA_SalesAnalysis.getId());
//        RelativeLayout.LayoutParams componentD_Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        componentD_Params.addRule(RelativeLayout.RIGHT_OF, scrC_SalesAnalysis.getId());
//        componentD_Params.addRule(RelativeLayout.BELOW, horScrB_SalesAnalysis.getId());
//
//        // 'this' is a relative layout,
//        // we extend this table layout as relative layout as seen during the creation of this class
//        relLayoutSales.addView(tableA_SalesAnalysis);
//        relLayoutSales.addView(horScrB_SalesAnalysis, componentB_Params);
//        relLayoutSales.addView(scrC_SalesAnalysis, componentC_Params);
//        relLayoutSales.addView(scrD_SalesAnalysis, componentD_Params);
//    }
//
//    @Override
//    public void onCheckedChanged(RadioGroup group, int checkedId) {
//        switch (checkedId) {
//
//            case R.id.btnDay:
//                selectedsegValue = "Day";
//
//                Log.e("---1---"," ");
//
//                break;
//
//            case R.id.btnWTD:
//                selectedsegValue = "WTD";
//
//                Log.e("---2---"," ");
//
//                break;
//
//            case R.id.btnMTD:
//                selectedsegValue = "MTD";
//
//                Log.e("---3---"," ");
//
//                break;
//
//            case R.id.btnYTD:
//                selectedsegValue = "YTD";
//
//                Log.e("---4---"," ");
//
//                break;
//            default:
//
//        }
//    }
//
//
//
//    private void requestProductAPI()
//    {
//        String url = ConstsCore.web_url + "/v1/display/hourlytransproducts/"+userId+"?offset="+offsetvalue+"&limit="+ limit;
//        Log.i("URL   ", url + " "+bearertoken);
//
//        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//
//                        try {
//                            if (response.equals(null) || response == null|| response.length()==0 && count==0)
//                            {
//                                Reusable_Functions.hDialog();
//                                Toast.makeText(context, "no product data found", Toast.LENGTH_LONG).show();
//                            }
//                            else if(response.length()==limit)
//                            {
//                                for (int i = 0; i < response.length(); i++)
//                                {
//                                    JSONObject productName1 = response.getJSONObject(i);
//                                    String ProductName = productName1.getString("productName");
//                                    //Log.e("Product Name:", ProductName);
//                                    int L2Hrs_Net_Sales = productName1.getInt("last2HourSaleTotQty");
//                                    int Day_Net_Sales = productName1.getInt("fordaySaleTotQty");
//                                    int WTD_Net_Sales = productName1.getInt("wtdSaleTotQty");
//                                    double Day_Net_Sales_Percent = productName1.getDouble("fordayPvaSalesUnitsPercent");
//                                    double WTD_Net_Sales_Percent = productName1.getDouble("wtdPvaSalesUnitsPercent");
//                                    int SOH = productName1.getInt("stkOnhandQty");
//                                    int GIT = productName1.getInt("stkGitQty");
//                                    String Storecode = productName1.getString("storeCode");
//                                    String storeDesc = productName1.getString("storeDesc");
//
//                                    ProductNameBean productNameBean = new ProductNameBean();
//                                    productNameBean.setProductName(ProductName);
//                                    productNameBean.setL2hrsNetSales(L2Hrs_Net_Sales);
//                                    productNameBean.setDayNetSales(Day_Net_Sales);
//                                    productNameBean.setWtdNetSales(WTD_Net_Sales);
//                                    productNameBean.setDayNetSalesPercent(Day_Net_Sales_Percent);
//                                    productNameBean.setWtdNetSalesPercent(WTD_Net_Sales_Percent);
//                                    productNameBean.setSoh(SOH);
//                                    productNameBean.setGit(GIT);
//                                    productNameBean.setStoreCode(Storecode);
//                                    productNameBean.setStoreDesc(storeDesc);
//                                    arrayList.add(productNameBean);
//                                }
//                                offsetvalue = (limit * count) + limit;
//                                count++;
//                                requestProductAPI();
//                            }
//                            else if(response.length()< limit)
//                            {
//                                for (int i = 0; i < response.length(); i++)
//                                {
//                                    JSONObject productName1 = response.getJSONObject(i);
//                                    String ProductName = productName1.getString("productName");
//                                    //Log.e("Product Name:", ProductName);
//                                    int L2Hrs_Net_Sales = productName1.getInt("last2HourSaleTotQty");
//                                    int Day_Net_Sales = productName1.getInt("fordaySaleTotQty");
//                                    int WTD_Net_Sales = productName1.getInt("wtdSaleTotQty");
//                                    double Day_Net_Sales_Percent = productName1.getDouble("fordayPvaSalesUnitsPercent");
//                                    double WTD_Net_Sales_Percent = productName1.getDouble("wtdPvaSalesUnitsPercent");
//                                    int SOH = productName1.getInt("stkOnhandQty");
//                                    int GIT = productName1.getInt("stkGitQty");
//                                    String Storecode = productName1.getString("storeCode");
//                                    String storeDesc = productName1.getString("storeDesc");
//                                    ProductNameBean productNameBean = new ProductNameBean();
//                                    productNameBean.setProductName(ProductName);
//                                    productNameBean.setL2hrsNetSales(L2Hrs_Net_Sales);
//                                    productNameBean.setDayNetSales(Day_Net_Sales);
//                                    productNameBean.setWtdNetSales(WTD_Net_Sales);
//                                    productNameBean.setDayNetSalesPercent(Day_Net_Sales_Percent);
//                                    productNameBean.setWtdNetSalesPercent(WTD_Net_Sales_Percent);
//                                    productNameBean.setSoh(SOH);
//                                    productNameBean.setGit(GIT);
//                                    productNameBean.setStoreCode(Storecode);
//                                    productNameBean.setStoreDesc(storeDesc);
//                                    arrayList.add(productNameBean);
//
//                                }
//
//                                Collections.sort(arrayList, new Comparator<ProductNameBean>() {
//                                    public int compare(ProductNameBean one, ProductNameBean other) {
//                                        return  new Integer(one.getWtdNetSales()).compareTo(new Integer(other.getWtdNetSales()));
//                                    }
//                                });
//                                Collections.reverse(arrayList);
//
//
//                                addTableRowToTableA();
//                                addTableRowToTableB();
//                                resizeHeaderHeight();
//                                getTableRowHeaderCellWidth();
//                                generateTableC_AndTable_B();
//                                resizeBodyTableRowHeight();
//
//                            }
//
//                        }
//                        catch (Exception e) {
//                            //Log.e("Exception e", e.toString() + "");
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Reusable_Functions.hDialog();
//                        // Toast.makeText(LoginActivity.this,"Invalid User",Toast.LENGTH_LONG).show();
//                        error.printStackTrace();
//                    }
//                }
//
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("Content-Type", "application/json");
//                params.put("Authorization", "Bearer "+bearertoken);
//
//                Log.e("params "," "+params);
//                return params;
//            }
//        };
//        int socketTimeout = 60000;//5 seconds
//
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        postRequest.setRetryPolicy(policy);
//        queue.add(postRequest);
//    }
//
//
//    private void addTableRowToTableA() {
//        tableA_SalesAnalysis.addView(this.componentATableRow());
//    }
//    private void addTableRowToTableB() {
//        tableB_SalesAnalysis.addView(this.componentBTableRow());
//    }
//
//
//    // generate table row of table A
//    TableRow componentATableRow() {
//        TableRow componentATableRow = new TableRow(this.context);
//        TableRow.LayoutParams params = new TableRow.LayoutParams(
//                TableRow.LayoutParams.WRAP_CONTENT, 80);
//        params.setMargins(2, 0, 0, 0);
//        TextView textView = this.headerTextView(headers[0]);
//        textView.setBackgroundColor(Color.parseColor("#B73020"));
//        textView.setTextColor(Color.parseColor("#ffffff"));
//        componentATableRow.addView(textView);
//        return componentATableRow;
//    }
//
//    // generate table row of table B
//    TableRow componentBTableRow() {
//
//        TableRow componentBTableRow = new TableRow(this.context);
//
//        int headerFieldCount = headers.length;
//
//        TableRow.LayoutParams params = new TableRow.LayoutParams(
//                TableRow.LayoutParams.WRAP_CONTENT, 80);
//        params.setMargins(2, 0, 0, 0);
//
//        for (int x = 0; x < (headerFieldCount - 1); x++) {
//            TextView textView = this.headerTextView(this.headers[x + 1]);
//            textView.setBackgroundColor(Color.parseColor("#B73020"));
//            textView.setTextColor(Color.parseColor("#ffffff"));
//            textView.setLayoutParams(params);
//            componentBTableRow.addView(textView);
//        }
//        return componentBTableRow;
//    }
//
//    private void generateTableC_AndTable_B() {
//
//        // just seeing some header cell width
//        for (int x = 0; x < this.headerCellsWidth.length; x++) {
//            Log.v("Product Data", this.headerCellsWidth[x] + "");
//        }
//
//        for (int k = 0; k < arrayList.size(); k++) {
//            final TableRow tableRowForTableC_SalesAnalysis;
//            tableRowForTableC_SalesAnalysis = this.tableRowForTableC_SalesAnalysis(arrayList.get(k).getProductName());
//            TableRow.LayoutParams params = new TableRow.LayoutParams(
//                    TableRow.LayoutParams.WRAP_CONTENT, 120);
//            params.setMargins(2, 0, 0, 0);
//
//            final TableRow tableRowForTableD_SalesAnalysis = this.tableRowForTableD_SalesAnalysis(arrayList.get(k));
//            tableRowForTableC_SalesAnalysis.setBackgroundColor(Color.WHITE);
//            tableRowForTableD_SalesAnalysis.setBackgroundColor(Color.LTGRAY);
//            final int i=k;
//
//            tableRowForTableC_SalesAnalysis.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//
//                    llayoutSalesAnalysis.removeAllViews();
//                    initialiseUIElements();
//                    arrayList = new ArrayList<ProductNameBean>();
//                    if (Reusable_Functions.chkStatus(context)) {
//
//                        Reusable_Functions.hDialog();
//                        Reusable_Functions.sDialog(context, "Loading data...");
//                        offsetvalue = 0;
//                        limit = 100;
//                        count = 0;
//                        requestProductAPI();
//
//                    } else
//                    {
//                        Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
//                    }
//
//
//
//                }
//            });
//            this.tableC_SalesAnalysis.addView(tableRowForTableC_SalesAnalysis);
//            this.tableD_SalesAnalysis.addView(tableRowForTableD_SalesAnalysis);
//        }
//        Reusable_Functions.hDialog();
//    }
//
//    TableRow tableRowForTableC_SalesAnalysis(String productNameDetails) {
//
//        TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], TableRow.LayoutParams.MATCH_PARENT);
//        params.setMargins(2, 2, 0, 0);
//
//
//        TableRow tableRowForTableC_SalesAnalysis = new TableRow(this.context);
////        TextView textView = this.bodyTextView(sampleObject.header1);
//        TextView textView = this.bodyTextView(productNameDetails);
//        textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
//        Log.e("Value",textView.getText().toString());
//        tableRowForTableC_SalesAnalysis.addView(textView, params);
//        return tableRowForTableC_SalesAnalysis;
//    }
//
//    TableRow tableRowForTableD_SalesAnalysis(ProductNameBean productDetails) {
//        TableRow tableRowForTableD_SalesAnalysis = new TableRow(this.context);
//        int loopCount = ((TableRow) this.tableB_SalesAnalysis.getChildAt(0)).getChildCount();
//
//        String info[] = {
//                String.valueOf(productDetails.getL2hrsNetSales()),
//                String.valueOf(productDetails.getDayNetSales()),
//                String.valueOf(productDetails.getWtdNetSales()),
//                String.valueOf(String.format("%.1f",productDetails.getDayNetSalesPercent())).concat("%"),
//                String.valueOf(String.format("%.1f",productDetails.getWtdNetSalesPercent())).concat("%"),
//                String.valueOf(productDetails.getSoh()),
//                String.valueOf(productDetails.getGit())
//                //productDetails.getArticleOption()
//
//        };
//
//        for (int x = 0; x < loopCount; x++) {
//            TableRow.LayoutParams params = new TableRow.LayoutParams(headerCellsWidth[x + 1], TableRow.LayoutParams.MATCH_PARENT);
//            params.setMargins(2, 2, 0, 0);
//            TextView textViewB = this.bodyTextView(String.valueOf(info[x]));
//
//
//            if(tableRowForTableD_SalesAnalysis.getChildAt(3) != null)
//            {
//                TextView txtDayNetSalesPercent = (TextView) tableRowForTableD_SalesAnalysis.getChildAt(3);
//
//                if(productDetails.getDayNetSalesPercent() >= Double.parseDouble("70") && productDetails.getDayNetSalesPercent() <= Double.parseDouble("80"))
//                {
//                    txtDayNetSalesPercent.setTextColor(Color.parseColor("#FFBF00"));
//
//                }
//                else if(productDetails.getDayNetSalesPercent() > Double.parseDouble("80"))
//                {
//                    txtDayNetSalesPercent.setTextColor(Color.GREEN);
//
//                }
//                else if(productDetails.getDayNetSalesPercent() < Double.parseDouble("70"))
//                {
//                    txtDayNetSalesPercent.setTextColor(Color.RED);
//
//                }
//
//            }
//
//
//            if(tableRowForTableD_SalesAnalysis.getChildAt(4) != null)
//            {
//                TextView txtWtdNetSalesPercent = (TextView) tableRowForTableD_SalesAnalysis.getChildAt(4);
//
//                if(productDetails.getWtdNetSalesPercent() >= Double.parseDouble("70") && productDetails.getWtdNetSalesPercent() <= Double.parseDouble("80"))
//                {
//                    txtWtdNetSalesPercent.setTextColor(Color.parseColor("#FFBF00"));
//
//                }
//                else if(productDetails.getWtdNetSalesPercent() > Double.parseDouble("80"))
//                {
//                    txtWtdNetSalesPercent.setTextColor(Color.GREEN);
//
//                }
//                else if(productDetails.getWtdNetSalesPercent() < Double.parseDouble("70"))
//                {
//                    txtWtdNetSalesPercent.setTextColor(Color.RED);
//
//                }
//
//            }
//
//
//            tableRowForTableD_SalesAnalysis.addView(textViewB, params);
//        }
//        return tableRowForTableD_SalesAnalysis;
//
//    }
//
//    // table cell standard TextView
//    TextView bodyTextView(String label) {
//
//        TextView bodyTextView = new TextView(this.context);
//        bodyTextView.setBackgroundColor(Color.WHITE);
//        bodyTextView.setText(label);
//        bodyTextView.setGravity(Gravity.CENTER);
//        bodyTextView.setPadding(5, 5, 5, 5);
//        return bodyTextView;
//    }
//
//    // header standard TextView
//    TextView headerTextView(String label) {
//
//        TextView headerTextView = new TextView(this.context);
//        headerTextView.setBackgroundColor(Color.WHITE);
//        headerTextView.setText(label);
//        headerTextView.setGravity(Gravity.CENTER);
//        headerTextView.setPadding(5, 5, 5, 5);
//
//        return headerTextView;
//    }
//
//
//    void resizeHeaderHeight() {
//        TableRow productNameHeaderTableRow = (TableRow) this.tableA_SalesAnalysis.getChildAt(0);
//        TableRow productInfoTableRow = (TableRow) this.tableB_SalesAnalysis.getChildAt(0);
//        int rowAHeight = this.viewHeight(productNameHeaderTableRow);
//        int rowBHeight = this.viewHeight(productInfoTableRow);
//        TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
//        int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;
//        this.matchLayoutHeight(tableRow, finalHeight);
//    }
//
//    void getTableRowHeaderCellWidth() {
//        int tableAChildCount = ((TableRow) this.tableA_SalesAnalysis.getChildAt(0)).getChildCount();
//        int tableBChildCount = ((TableRow) this.tableB_SalesAnalysis.getChildAt(0)).getChildCount();
//        for (int x = 0; x < (tableAChildCount + tableBChildCount); x++)
//        {
//
//            if (x == 0) {
//                this.headerCellsWidth[x] = this.viewWidth(((TableRow) this.tableA_SalesAnalysis.getChildAt(0)).getChildAt(x));
//            } else {
//                this.headerCellsWidth[x] = this.viewWidth(((TableRow) this.tableB_SalesAnalysis.getChildAt(0)).getChildAt(x - 1));
//            }
//
//        }
//    }
//
//    // resize body table row height
//    void resizeBodyTableRowHeight() {
//
//        int tableC_ChildCount = this.tableC_SalesAnalysis.getChildCount();
//
//        for (int x = 0; x < tableC_ChildCount; x++) {
//
//            TableRow productNameHeaderTableRow = (TableRow) this.tableC_SalesAnalysis.getChildAt(x);
//            TableRow productInfoTableRow = (TableRow) this.tableD_SalesAnalysis.getChildAt(x);
//
//            int rowAHeight = this.viewHeight(productNameHeaderTableRow);
//            int rowBHeight = this.viewHeight(productInfoTableRow);
//
//            TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
//            int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;
//
//            this.matchLayoutHeight(tableRow, finalHeight);
//        }
//
//    }
//
//    private void matchLayoutHeight(TableRow tableRow, int height) {
//
//        int tableRowChildCount = tableRow.getChildCount();
//
//        // if a TableRow has only 1 child
//        if (tableRow.getChildCount() == 1) {
//
//            View view = tableRow.getChildAt(0);
//            TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();
//            params.height = height - (params.bottomMargin + params.topMargin);
//
//            return;
//        }
//
//        // if a TableRow has more than 1 child
//        for (int x = 0; x < tableRowChildCount; x++) {
//
//            View view = tableRow.getChildAt(x);
//
//            TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();
//
//            if (!isTheHeighestLayout(tableRow, x)) {
//                params.height = height - (params.bottomMargin + params.topMargin);
//                return;
//            }
//        }
//
//    }
//
//    // check if the view has the highest height in a TableRow
//    private boolean isTheHeighestLayout(TableRow tableRow, int layoutPosition) {
//
//        int tableRowChildCount = tableRow.getChildCount();
//        int heighestViewPosition = -1;
//        int viewHeight = 0;
//
//        for (int x = 0; x < tableRowChildCount; x++) {
//            View view = tableRow.getChildAt(x);
//            int height = this.viewHeight(view);
//
//            if (viewHeight < height) {
//                heighestViewPosition = x;
//                viewHeight = height;
//            }
//        }
//
//        return heighestViewPosition == layoutPosition;
//    }
//
//    // read a view's height
//    private int viewHeight(View view) {
//        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//        return view.getMeasuredHeight();
//    }
//
//    // read a view's width
//    private int viewWidth(View view) {
//        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//        return view.getMeasuredWidth();
//    }
//
//    // horizontal scroll view custom class
//    class MyHorizontalScrollView extends HorizontalScrollView {
//
//        public MyHorizontalScrollView(Context context) {
//            super(context);
//        }
//
//        @Override
//        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//            String tag = (String) this.getTag();
//
//            if (tag.equalsIgnoreCase("horizontal scroll view b")) {
//                horScrD_SalesAnalysis.scrollTo(l, 0);
//            } else {
//                horScrB_SalesAnalysis.scrollTo(l, 0);
//            }
//        }
//    }
//
//    // scroll view custom class
//    class MyScrollView extends ScrollView {
//
//        public MyScrollView(Context context) {
//            super(context);
//        }
//
//        @Override
//        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//
//            String tag = (String) this.getTag();
//            if (tag.equalsIgnoreCase("scroll view c")) {
//                scrD_SalesAnalysis.scrollTo(0, t);
//            } else {
//                scrC_SalesAnalysis.scrollTo(0, t);
//            }
//        }
//    }
}
