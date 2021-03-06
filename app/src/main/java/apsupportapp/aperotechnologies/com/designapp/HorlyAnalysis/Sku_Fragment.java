package apsupportapp.aperotechnologies.com.designapp.HorlyAnalysis;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
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


public class Sku_Fragment extends Fragment {


    TableLayout tableA;
    TableLayout tableB;
    TableLayout tableC;
    TableLayout tableD;
    ViewGroup view;
    HorizontalScrollView horizontalScrollViewB;
    HorizontalScrollView horizontalScrollViewD;
    ArrayList<ProductNameBean> productNameBeanArrayList;
    OnRowPressListener rowPressListener;

    ScrollView scrollViewC;
    ScrollView scrollViewD;
    RequestQueue queue;

    Context context;
    RelativeLayout rel, relativeLayout;
    // set the header titles
    String headers[] = {
            "        Size        ",
            "    L2H\n\t\t\tSls    ",
            "    Day\n\t\t\tSls    ",
            "    WTD\n\t\tSls    ",
            "     SOH     ",
            "     GIT     ",

    };

    int headerCellsWidth[] = new int[headers.length];
    ProductNameBean productNameBean;
    TextView txtStoreCode, txtStoreDesc;
    MySingleton m_config;
    String articleOption, productName1;
    int offsetvalue = 0, limit = 100;
    int count = 0;
    String userId, bearertoken;
    SharedPreferences sharedPreferences;
    TextView txt_articleOption;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_config = MySingleton.getInstance(getActivity());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = (ViewGroup) inflater.inflate(R.layout.sku_fragment, container, false);
        txtStoreCode = (TextView) view.findViewById(R.id.txtStoreCode);
        txtStoreDesc = (TextView) view.findViewById(R.id.txtStoreName);
        context = view.getContext();
        relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);
        rel = (RelativeLayout) view.findViewById(R.id.rel);
        relativeLayout.setBackgroundColor(Color.parseColor("#f8f6f6"));
        txt_articleOption = (TextView) view.findViewById(R.id.txt_Sku_article_Option_Name);
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        productNameBeanArrayList = new ArrayList<ProductNameBean>();
        initComponents();
        setComponentsId();
        setScrollViewAndHorizontalScrollViewTag();
        horizontalScrollViewB.addView(tableB);
        scrollViewC.addView(tableC);
        scrollViewD.addView(horizontalScrollViewD);
        horizontalScrollViewD.addView(tableD);
        addComponentToMainLayout();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            rowPressListener = (OnRowPressListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement IFragmentToActivity");
        }
    }

    public void fragmentCommunication1(String productName, String articlOption) {

        productName1 = productName;
        articleOption = articlOption;
        if (Reusable_Functions.chkStatus(context)) {
            LinearLayout layout = (LinearLayout) KeyProductActivity.viewPager.getParent();
            TabLayout tab = (TabLayout) layout.getChildAt(1);
            // Remove option tab when seen product and sku tab
            if (tab.getTabCount() == 3) {
                tab.removeTabAt(1);
            }

            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            offsetvalue = 0;
            limit = 100;
            count = 0;

            txt_articleOption.setText(articleOption);
            requestProductArticleSkuAPI(offsetvalue, limit);
        } else {
            Toast.makeText(getContext(), "Check your network connectivity", Toast.LENGTH_LONG).show();
        }


    }


    public void requestProductArticleSkuAPI(int offsetvalue1, int limit1) {
        String url = ConstsCore.web_url + "/v1/display/hourlytransproducts/" + userId + "?view=productDesc&articleOption=" + articleOption.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit + "&productName=" + productName1.replaceAll(" ", "%20").replaceAll("&", "%26");

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(getContext(), "no sku data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String ProductName = productName1.getString("productName");

                                    String articleOption = productName1.getString("articleOption");
                                    String articleCode = productName1.getString("articleCode");
                                    String productDesc = productName1.getString("productDesc");
                                    String Size = productName1.getString("size");
                                    int L2Hrs_Net_Sales = productName1.getInt("last2HourSaleTotQty");
                                    int Day_Net_Sales = productName1.getInt("fordaySaleTotQty");
                                    int WTD_Net_Sales = productName1.getInt("wtdSaleTotQty");
                                    double Day_Net_Sales_Percent = productName1.getDouble("fordayPvaSalesUnitsPercent");
                                    double WTD_Net_Sales_Percent = productName1.getDouble("wtdPvaSalesUnitsPercent");
//                                    int Forday_Plan_Sale_Tot_Qty = productName1.getInt("fordayPlanSaleTotQty");
//                                    int Wtd_Plan_Sale_Tot_Qty = productName1.getInt("wtdPlanSaleTotQty");
                                    int SOH = productName1.getInt("stkOnhandQty");
                                    int GIT = productName1.getInt("stkGitQty");
                                    String Storecode = productName1.getString("storeCode");
                                    String storeDesc = productName1.getString("storeDesc");

                                    productNameBean = new ProductNameBean();
                                    productNameBean.setProductName(ProductName);

                                    productNameBean.setArticleOption(articleOption);
                                    productNameBean.setArtileCode(articleCode);
                                    productNameBean.setProdDesc(productDesc);
                                    productNameBean.setSize(Size);
                                    productNameBean.setL2hrsNetSales(L2Hrs_Net_Sales);
                                    productNameBean.setDayNetSales(Day_Net_Sales);
                                    productNameBean.setWtdNetSales(WTD_Net_Sales);
                                    productNameBean.setDayNetSalesPercent(Day_Net_Sales_Percent);
                                    productNameBean.setWtdNetSalesPercent(WTD_Net_Sales_Percent);
                                    productNameBean.setSoh(SOH);
                                    productNameBean.setGit(GIT);
                                    productNameBean.setStoreCode(Storecode);


                                    productNameBean.setStoreDesc(storeDesc);
                                    productNameBeanArrayList.add(productNameBean);
                                    txtStoreCode.setText(productNameBeanArrayList.get(i).getStoreCode());
                                    txtStoreDesc.setText(productNameBeanArrayList.get(i).getStoreDesc());

                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestProductArticleSkuAPI(offsetvalue, limit);
                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String ProductName = productName1.getString("productName");

                                    String articleOption = productName1.getString("articleOption");
                                    String articleCode = productName1.getString("articleCode");
                                    String productDesc = productName1.getString("productDesc");
                                    String Size = productName1.getString("size");
                                    int L2Hrs_Net_Sales = productName1.getInt("last2HourSaleTotQty");
                                    int Day_Net_Sales = productName1.getInt("fordaySaleTotQty");
                                    int WTD_Net_Sales = productName1.getInt("wtdSaleTotQty");
                                    double Day_Net_Sales_Percent = productName1.getDouble("fordayPvaSalesUnitsPercent");
                                    double WTD_Net_Sales_Percent = productName1.getDouble("wtdPvaSalesUnitsPercent");
//                                    int Forday_Plan_Sale_Tot_Qty = productName1.getInt("fordayPlanSaleTotQty");
//                                    int Wtd_Plan_Sale_Tot_Qty = productName1.getInt("wtdPlanSaleTotQty");
                                    int SOH = productName1.getInt("stkOnhandQty");
                                    int GIT = productName1.getInt("stkGitQty");
                                    String Storecode = productName1.getString("storeCode");
                                    String storeDesc = productName1.getString("storeDesc");

                                    productNameBean = new ProductNameBean();
                                    productNameBean.setProductName(ProductName);

                                    productNameBean.setArticleOption(articleOption);
                                    productNameBean.setArtileCode(articleCode);
                                    productNameBean.setProdDesc(productDesc);
                                    productNameBean.setSize(Size);
                                    productNameBean.setL2hrsNetSales(L2Hrs_Net_Sales);
                                    productNameBean.setDayNetSales(Day_Net_Sales);
                                    productNameBean.setWtdNetSales(WTD_Net_Sales);
                                    productNameBean.setDayNetSalesPercent(Day_Net_Sales_Percent);
                                    productNameBean.setWtdNetSalesPercent(WTD_Net_Sales_Percent);
                                    productNameBean.setSoh(SOH);
                                    productNameBean.setGit(GIT);
                                    productNameBean.setStoreCode(Storecode);

                                    productNameBean.setStoreDesc(storeDesc);

                                    productNameBeanArrayList.add(productNameBean);
                                    txtStoreCode.setText(productNameBeanArrayList.get(i).getStoreCode());
                                    txtStoreDesc.setText(productNameBeanArrayList.get(i).getStoreDesc());
                                }

                                Collections.sort(productNameBeanArrayList, new Comparator<ProductNameBean>() {
                                    public int compare(ProductNameBean one, ProductNameBean other) {
                                        return new Integer(one.getWtdNetSales()).compareTo(other.getWtdNetSales());
                                    }
                                });
                                Collections.reverse(productNameBeanArrayList);

                                addTableRowToTableA();
                                addTableRowToTableB();
                                resizeHeaderHeight();
                                getTableRowHeaderCellWidth();
                                generateTableC_AndTable_B();
                                resizeBodyTableRowHeight();

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

    // initalized components
    private void initComponents() {
        tableA = new TableLayout(this.context);
        tableB = new TableLayout(this.context);
        tableC = new TableLayout(this.context);
        tableD = new TableLayout(this.context);
        horizontalScrollViewB = new MyHorizontalScrollView(this.context);
        horizontalScrollViewD = new MyHorizontalScrollView(this.context);
        scrollViewC = new MyScrollView(this.context);
        scrollViewD = new MyScrollView(this.context);
        tableA.setBackgroundColor(Color.GREEN);
        horizontalScrollViewB.setBackgroundColor(Color.LTGRAY);
    }

    // set essential component IDs
    @SuppressWarnings("ResourceType")
    private void setComponentsId() {
        tableA.setId(1);
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
        componentB_Params.addRule(RelativeLayout.RIGHT_OF, this.tableA.getId());

        RelativeLayout.LayoutParams componentC_Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        componentC_Params.addRule(RelativeLayout.BELOW, this.tableA.getId());

        RelativeLayout.LayoutParams componentD_Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        componentD_Params.addRule(RelativeLayout.RIGHT_OF, scrollViewC.getId());
        componentD_Params.addRule(RelativeLayout.BELOW, horizontalScrollViewB.getId());

        // 'this' is a relative layout,
        // we extend this table layout as relative layout as seen during the creation of this class
        relativeLayout.addView(tableA);
        relativeLayout.addView(horizontalScrollViewB, componentB_Params);
        relativeLayout.addView(scrollViewC, componentC_Params);
        relativeLayout.addView(scrollViewD, componentD_Params);

    }

    private void addTableRowToTableA() {
        tableA.addView(this.componentATableRow());
    }

    private void addTableRowToTableB() {
        tableB.addView(this.componentBTableRow());
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
            textView.setGravity(Gravity.CENTER);
            componentBTableRow.addView(textView);
        }

        return componentBTableRow;
    }

    private void generateTableC_AndTable_B() {

        // just seeing some header cell width

        for (int k = 0; k < productNameBeanArrayList.size(); k++) {
            TableRow tableRowForTableC = this.tableRowForTableC(productNameBeanArrayList.get(k).getSize());

            TableRow taleRowForTableD = this.taleRowForTableD(productNameBeanArrayList.get(k));
            tableRowForTableC.setBackgroundColor(Color.parseColor("#dfdedf"));
            taleRowForTableD.setBackgroundColor(Color.parseColor("#dfdedf"));
            this.tableC.addView(tableRowForTableC);
            this.tableD.addView(taleRowForTableD);
        }
        Reusable_Functions.hDialog();
    }

    TableRow tableRowForTableC(String productNameDetails) {
        TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], TableRow.LayoutParams.MATCH_PARENT);
        params.setMargins(0, 2, 0, 0);
        TableRow tableRowForTableC = new TableRow(this.context);

        TextView textView = this.bodyTextView(productNameDetails);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        tableRowForTableC.addView(textView, params);
        return tableRowForTableC;
    }

    TableRow taleRowForTableD(ProductNameBean productDetails) {

        TableRow taleRowForTableD = new TableRow(this.context);
        int loopCount = ((TableRow) this.tableB.getChildAt(0)).getChildCount();
        NumberFormat format = NumberFormat.getNumberInstance(new Locale("","in"));
        String[] info = {
                String.valueOf(productDetails.getL2hrsNetSales()),
                String.valueOf(productDetails.getDayNetSales()),
                String.valueOf(format.format(productDetails.getWtdNetSales())),
                String.valueOf(format.format(productDetails.getSoh())),
                String.valueOf(format.format(productDetails.getGit()))


        };

        for (int x = 0; x < loopCount; x++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(headerCellsWidth[x + 1], TableRow.LayoutParams.MATCH_PARENT);
            params.setMargins(2, 2, 0, 0);
            TextView textViewB = this.bodyTextView(info[x]);
            taleRowForTableD.addView(textViewB, params);
        }
        return taleRowForTableD;

    }

    // table cell standard TextView
    TextView bodyTextView(String label) {
        TextView bodyTextView = new TextView(this.context);
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

        TableRow productNameHeaderTableRow = (TableRow) this.tableA.getChildAt(0);
        TableRow productInfoTableRow = (TableRow) this.tableB.getChildAt(0);

        int rowAHeight = this.viewHeight(productNameHeaderTableRow);
        int rowBHeight = this.viewHeight(productInfoTableRow);

        TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
        int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;

        this.matchLayoutHeight(tableRow, finalHeight);
    }

    void getTableRowHeaderCellWidth() {

        int tableAChildCount = ((TableRow) this.tableA.getChildAt(0)).getChildCount();
        int tableBChildCount = ((TableRow) this.tableB.getChildAt(0)).getChildCount();

        for (int x = 0; x < (tableAChildCount + tableBChildCount); x++) {

            if (x == 0) {
                this.headerCellsWidth[x] = this.viewWidth(((TableRow) this.tableA.getChildAt(0)).getChildAt(x));
            } else {
                this.headerCellsWidth[x] = this.viewWidth(((TableRow) this.tableB.getChildAt(0)).getChildAt(x - 1));
            }

        }
    }

    // resize body table row height
    void resizeBodyTableRowHeight() {

        int tableC_ChildCount = this.tableC.getChildCount();

        for (int x = 0; x < tableC_ChildCount; x++) {

            TableRow productNameHeaderTableRow = (TableRow) this.tableC.getChildAt(x);
            TableRow productInfoTableRow = (TableRow) this.tableD.getChildAt(x);

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

    public void incrementData() {

        txtStoreCode.setText("Hello");
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
}







