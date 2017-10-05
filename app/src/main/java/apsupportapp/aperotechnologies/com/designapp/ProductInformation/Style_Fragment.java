
package apsupportapp.aperotechnologies.com.designapp.ProductInformation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import android.util.Log;
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
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.MySingleton;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;


import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class Style_Fragment extends Fragment {
    TableLayout tableA;
    TableLayout tableB;
    TableLayout tableC;
    TableLayout tableD;

    TextView txtarticleOption, txtStoreCode, txtStoreName, txttwSalesUnit, txtlwSales, txtytdSales, txtSoh, txtGit, txtSales, txtFwdWeekCover,
            txtsalesThruUnit, txtROS;
    View view;
    String userId, bearertoken,storeDescription,geoLevel2Code,lobId;
    HorizontalScrollView horizontalScrollViewB;
    HorizontalScrollView horizontalScrollViewD;
    ArrayList<StyleDetailsBean> styleDetailsBeanList;
    ScrollView scrollViewC;
    ScrollView scrollViewD;
    RequestQueue queue;
    SharedPreferences sharedPreferences;
    static int salesUnitTotal = 0, sohTotal = 0;
    double fwdweekTotal = 0;

    ArrayList<StyleColorBean> styleColorBeanList;
    StyleDetailsBean styleDetailsBean;
    StyleColorBean styleColorBean;
    Context context;
    MySingleton m_config;
    static int cnt = 0;
    RelativeLayout relativeLayout;
    String articleCode, articleOption,storeCode;
    int offsetvalue = 0, limit = 100;
    int count = 0;
    String TA = "StyleActivity";

    // set the header titles
    String headers[] =
            {
                    "Color               " ,
                    "    Size    ",
                    "    TW Sales   ",
                    "   SOH   ",
                    "     FWC   "
            };

    int headerCellsWidth[] = new int[headers.length];
    private String TAG = "StyleActivity";
    private LinearLayout LinearTable;
    private RelativeLayout Style_loadingBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLevel2Code = sharedPreferences.getString("concept", "");
        lobId = sharedPreferences.getString("lobid", "");
//        storeDescription = sharedPreferences.getString("storeDescription","");

        Bundle bundle = getActivity().getIntent().getExtras();
        articleCode = bundle.getString("articleCode");
        articleOption = bundle.getString("articleOption");
        storeCode = bundle.getString("storeCode");
        Log.e(TAG, "onCreate: "+storeCode);
        context = getContext();
        styleColorBeanList = new ArrayList<>();
        m_config = MySingleton.getInstance(context);

        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        Log.e(TAG, "onCreate: ");
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            requestStyleSizeDetailsAPI();

        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
            Style_loadingBar.setVisibility(View.GONE);
        }
    }

    private void requestStyleColorDetailsAPI(int offsetvalue1, final int limit1) {

        String url = ConstsCore.web_url + "/v1/display/sizesNew/" + userId + "?articleOption=" + articleOption.replace(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" +"&geoLevel2Code="+geoLevel2Code + "&lobId="+lobId +"&storeCode="+storeCode;
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.equals("") || response == null || response.length() == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "No size data found", Toast.LENGTH_LONG).show();
                                Style_loadingBar.setVisibility(View.GONE);

                            } else if (response.length() == limit) {

                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject styleDetails = response.getJSONObject(i);
                                    String color = styleDetails.getString("color");
                                    String size = styleDetails.getString("size");
                                    String articleCode = styleDetails.getString("articleCode");
                                    String articleOption = styleDetails.getString("articleOption");
                                    int twSaleTotQty = styleDetails.getInt("twSaleTotQty");
                                    int stkOnhandQty = styleDetails.getInt("stkOnhandQty");
                                    double fwdWeekCover = styleDetails.getDouble("fwdWeekCover");
                                    styleColorBean = new StyleColorBean();
                                    styleColorBean.setColor(color);
                                    styleColorBean.setSize(size);
                                    styleColorBean.setTwSaleTotQty(twSaleTotQty);
                                    styleColorBean.setFwdWeekCover(fwdWeekCover);
                                    styleColorBean.setStkOnhandQty(stkOnhandQty);
                                    styleColorBean.setArticleCode(articleCode);
                                    styleColorBean.setArticleOption(articleOption);
                                    styleColorBeanList.add(styleColorBean);
                                }

                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestStyleColorDetailsAPI(offsetvalue, limit);

                            } else if (response.length() < limit) {

                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject styleDetails = response.getJSONObject(i);
                                    String color = styleDetails.getString("color");
                                    String size = styleDetails.getString("size");
                                    String articleCode = styleDetails.getString("articleCode");
                                    String articleOption = styleDetails.getString("articleOption");
                                    int twSaleTotQty = styleDetails.getInt("twSaleTotQty");
                                    int stkOnhandQty = styleDetails.getInt("stkOnhandQty");
                                    double fwdWeekCover = styleDetails.getDouble("fwdWeekCover");
                                    styleColorBean = new StyleColorBean();
                                    styleColorBean.setColor(color);
                                    styleColorBean.setSize(size);
                                    styleColorBean.setTwSaleTotQty(twSaleTotQty);
                                    styleColorBean.setFwdWeekCover(fwdWeekCover);
                                    styleColorBean.setStkOnhandQty(stkOnhandQty);
                                    styleColorBean.setArticleCode(articleCode);
                                    styleColorBean.setArticleOption(articleOption);
                                    styleColorBeanList.add(styleColorBean);
                                }
                                addTableRowToTableA();
                                addTableRowToTableB();
                                resizeHeaderHeight();
                                getTableRowHeaderCellWidth();
                                generateTableC_AndTable_B();
                                resizeBodyTableRowHeight();
                                // createTable();
                                Style_loadingBar.setVisibility(View.GONE);

                                // Reusable_Functions.hDialog();
                            }
                        } catch (Exception e) {
                            Style_loadingBar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Style_loadingBar.setVisibility(View.GONE);
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

    private void createTable() {
        int sumTW = 0, sumSOH = 0;
        for (int i = 0; i < styleColorBeanList.size(); i++) {

            LayoutInflater layoutInflater = (LayoutInflater) getActivity()
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.activity_stylefragment_child, null);
            TextView StyleColorName = (TextView) view.findViewById(R.id.styleColorName);
            TextView StyleFwd = (TextView) view.findViewById(R.id.styleFwd);
            TextView StyleSize = (TextView) view.findViewById(R.id.styleSize);
            TextView StyleSOH = (TextView) view.findViewById(R.id.styleSOH);
            TextView StyleTW = (TextView) view.findViewById(R.id.styleTW);
            StyleColorName.setText(styleColorBeanList.get(i).getColor());
            StyleFwd.setText(String.format("%.1f", styleColorBeanList.get(i).getFwdWeekCover()));
            StyleSize.setText(styleColorBeanList.get(i).getSize());
            StyleSOH.setText("" + styleColorBeanList.get(i).getStkOnhandQty());
            StyleTW.setText("" + styleColorBeanList.get(i).getTwSaleTotQty());
            sumTW += styleColorBeanList.get(i).getTwSaleTotQty();
            sumSOH += styleColorBeanList.get(i).getStkOnhandQty();
            LinearTable.addView(view);
        }
        LayoutInflater layoutInflater = (LayoutInflater) getActivity()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.activity_style_totalchild, null);
        TextView StyleTW_total = (TextView) view.findViewById(R.id.styleTW_total);
        TextView StyleSOH_total = (TextView) view.findViewById(R.id.styleSOH_total);
        StyleTW_total.setText("" + sumTW);
        StyleSOH_total.setText("" + sumSOH);

        LinearTable.addView(view);
        Reusable_Functions.hDialog();
    }

    private void requestStyleSizeDetailsAPI() {

        String url = ConstsCore.web_url + "/v1/display/stylesNew/" + userId + "?articleOption=" + articleOption.replaceAll(" ", "%20").replaceAll("&", "%26")+"&geoLevel2Code="+geoLevel2Code + "&lobId="+lobId +"&storeCode="+storeCode;
        Log.e(TAG, "requestStyleSizeDetailsAPI: "+url );
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, "onResponse: " + response);
                        try {
                            if (response.equals("") || response == null) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "No data Found", Toast.LENGTH_LONG).show();

                            } else {
                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject styleDetails = response.getJSONObject(i);
                                    String storeCode = styleDetails.getString("storeCode");
                                    String storeDesc = styleDetails.getString("storeDesc");
                                    double twSaleTotQty = styleDetails.getDouble("twSaleTotQty");
                                    double twSaleNetVal = styleDetails.getDouble("twSaleNetVal");
                                    double lwSaleTotQty = styleDetails.getDouble("lwSaleTotQty");
                                    double lwSaleNetVal = styleDetails.getDouble("lwSaleNetVal");
                                    double ytdSaleNetVal = styleDetails.getDouble("ytdSaleNetVal");
                                    double ytdSaleTotQty = styleDetails.getDouble("ytdSaleTotQty");
                                    double fwdWeekCover = styleDetails.getDouble("fwdWeekCover");
                                    double stkGitQty = styleDetails.getDouble("stkGitQty");
                                    double sellThruUnitsRcpt = styleDetails.getDouble("sellThruUnitsRcpt");
                                    double ros = styleDetails.getDouble("ros");
                                    double stkOnhandQty = styleDetails.getDouble("stkOnhandQty");
                                     txtStoreCode.setText(storeCode);
                                    txtStoreName.setText(storeDesc);
                                    //  txtSales.setText(": "+"\u20B9" + Sales);
                                    txtarticleOption.setText("" + articleOption);
                                    txttwSalesUnit.setText(" " + Math.round(twSaleTotQty));
                                    txtlwSales.setText("" + Math.round(lwSaleTotQty));
                                    txtytdSales.setText("" + Math.round(ytdSaleTotQty));
                                    txtSoh.setText("" + Math.round(stkOnhandQty));
                                    txtGit.setText("" + Math.round(stkGitQty));
                                    txtSales.setText("" + Math.round(twSaleNetVal));
                                    txtFwdWeekCover.setText("" + String.format("%.1f",fwdWeekCover));
                                    txtsalesThruUnit.setText("" +  String.format("%.1f",sellThruUnitsRcpt)+"%");
                                    txtROS.setText("" + String.format("%.1f",ros));
                                }
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_style, container, false);
        context = view.getContext();
        relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);
        relativeLayout.setBackgroundColor(Color.parseColor("#f8f6f6"));  //dfdedf
        Style_loadingBar = (RelativeLayout) view.findViewById(R.id.style_loadingBar);
        txtarticleOption = (TextView) view.findViewById(R.id.txtarticleOption);
        //style size text
        LinearTable = (LinearLayout) view.findViewById(R.id.linearTable);
        txtStoreCode = (TextView) view.findViewById(R.id.txtStoreCode);
        txtStoreName = (TextView) view.findViewById(R.id.txtStoreName);
//        txtStoreCode.setText(storeDescription.trim().substring(0,4));
//        txtStoreName.setText(storeDescription.substring(5));
        txttwSalesUnit = (TextView) view.findViewById(R.id.txttwSalesUnit);
        txtlwSales = (TextView) view.findViewById(R.id.txtlwSales);
        txtytdSales = (TextView) view.findViewById(R.id.txtytdSales);
        txtSoh = (TextView) view.findViewById(R.id.txtSoh);
        txtGit = (TextView) view.findViewById(R.id.txtGit);
        txtSales = (TextView) view.findViewById(R.id.txtSales);
        txtFwdWeekCover = (TextView) view.findViewById(R.id.txtFwdWeekCover);
        txtsalesThruUnit = (TextView) view.findViewById(R.id.txtsalesThruUnit);
        txtROS = (TextView) view.findViewById(R.id.txtROS);


        initComponents();
        setComponentsId();
        setScrollViewAndHorizontalScrollViewTag();


        // no need to assemble component A, since it is just a table
        horizontalScrollViewB.addView(tableB);

        scrollViewC.addView(tableC);

        scrollViewD.addView(horizontalScrollViewD);
        horizontalScrollViewD.addView(tableD);

        // add the components to be part of the main layout
        addComponentToMainLayout();
        return view;
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
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 0, 0, 0);
        TextView textView = this.headerTextView(headers[0]);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        textView.setBackgroundColor(Color.parseColor("#ffffff"));
        textView.setTextColor(Color.parseColor("#000000"));
        componentATableRow.addView(textView);

        return componentATableRow;
    }

    // generate table row of table B
    TableRow componentBTableRow() {

        TableRow componentBTableRow = new TableRow(this.context);
        int headerFieldCount = headers.length;

        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 0, 0, 0);

        for (int x = 0; x < (headerFieldCount - 1); x++) {
            TextView textView = this.headerTextView(this.headers[x + 1]);
            textView.setBackgroundColor(Color.parseColor("#ffffff"));
            textView.setTextColor(Color.parseColor("#000000"));
            textView.setLayoutParams(params);
            componentBTableRow.addView(textView);
        }

        return componentBTableRow;
    }


    private void generateTableC_AndTable_B() {


        for (int k = 0; k < styleColorBeanList.size(); k++) {
            if (k == 0 && styleColorBeanList.size() == 1) {
                TableRow tableRowForTableC = this.tableRowForTableC(styleColorBeanList.get(k).getColor());
                this.tableC.addView(tableRowForTableC);

                final int finalK = k;
                tableRowForTableC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String articleOption1 = styleColorBeanList.get(finalK).getArticleOption();

                        if (articleOption1.equals(articleOption)) {
                            SwitchingTabActivity.viewPager.setCurrentItem(0);
                            SwitchingTabActivity.tabLayout.getTabAt(0).select();

                        } else {
                            Reusable_Functions.sDialog(context, "Loading data...");
                            requestStyleDetailsAPI(context, articleOption1);
                        }


                    }
                });

                salesUnitTotal = salesUnitTotal + styleColorBeanList.get(k).getTwSaleTotQty();
                sohTotal = sohTotal + styleColorBeanList.get(k).getStkOnhandQty();
                fwdweekTotal = fwdweekTotal + styleColorBeanList.get(k).getFwdWeekCover();

                TableRow taleRowForTableD = this.taleRowForTableD(styleColorBeanList.get(k));
                this.tableD.addView(taleRowForTableD);

                TableRow tableRowForTableC1 = this.tableRowForTableCSpace();
                this.tableC.addView(tableRowForTableC1);

                TableRow taleRowForTableD1 = this.taleRowForTableDtotal();
                this.tableD.addView(taleRowForTableD1);

                salesUnitTotal = 0;
                sohTotal = 0;
                fwdweekTotal = 0;


            } else if (k == 0 && styleColorBeanList.size() > 1) {
                TableRow tableRowForTableC = this.tableRowForTableC(styleColorBeanList.get(k).getColor());
                tableRowForTableC.setBackgroundColor(Color.WHITE);
                this.tableC.addView(tableRowForTableC);


                final int finalK = k;
                tableRowForTableC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String articleOption1 = styleColorBeanList.get(finalK).getArticleOption();

                        if (articleOption1.equals(articleOption)) {
                            SwitchingTabActivity.viewPager.setCurrentItem(0);
                            SwitchingTabActivity.tabLayout.getTabAt(0).select();

                        } else {
                            Reusable_Functions.sDialog(context, "Loading data...");
                            requestStyleDetailsAPI(context, articleOption1);
                        }

                    }
                });

                TableRow taleRowForTableD = this.taleRowForTableD(styleColorBeanList.get(k));
                taleRowForTableD.setBackgroundColor(Color.LTGRAY);
                this.tableD.addView(taleRowForTableD);

                salesUnitTotal = salesUnitTotal + styleColorBeanList.get(k).getTwSaleTotQty();
                sohTotal = sohTotal + styleColorBeanList.get(k).getStkOnhandQty();
                fwdweekTotal = fwdweekTotal + styleColorBeanList.get(k).getFwdWeekCover();

            } else if (k >= 1) {    //If current color equal to previous color & last entry in array, then add the row & total row
                if (styleColorBeanList.get(k).getColor().trim().equals(styleColorBeanList.get(k - 1).getColor().trim()) && k == styleColorBeanList.size() - 1) {

                    TableRow tableRowForTableC = this.tableRowForTableC("");
                    this.tableC.addView(tableRowForTableC);

                    TableRow taleRowForTableD = this.taleRowForTableD(styleColorBeanList.get(k));
                    taleRowForTableD.setBackgroundColor(Color.LTGRAY);
                    this.tableD.addView(taleRowForTableD);

                    salesUnitTotal = salesUnitTotal + styleColorBeanList.get(k).getTwSaleTotQty();
                    sohTotal = sohTotal + styleColorBeanList.get(k).getStkOnhandQty();
                    fwdweekTotal = fwdweekTotal + styleColorBeanList.get(k).getFwdWeekCover();


                    TableRow tableRowForTableC1 = this.tableRowForTableCSpace();
                    this.tableC.addView(tableRowForTableC1);

                    TableRow taleRowForTableD2 = this.taleRowForTableDtotal();
                    this.tableD.addView(taleRowForTableD2);

                    salesUnitTotal = 0;
                    sohTotal = 0;
                    fwdweekTotal = 0;
                }
                //If current color not equal to previous color & last entry in array, then add total for previous, current row & total for current row
                else if (!styleColorBeanList.get(k).getColor().trim().equals(styleColorBeanList.get(k - 1).getColor().trim()) && k == styleColorBeanList.size() - 1) {
                    TableRow tableRowForTableC = this.tableRowForTableCSpace();
                    this.tableC.addView(tableRowForTableC);

                    TableRow tableRowForTableD = this.taleRowForTableDtotal();
                    this.tableD.addView(tableRowForTableD);

                    salesUnitTotal = 0;
                    sohTotal = 0;
                    fwdweekTotal = 0;

                    TableRow tableRowForTableC1 = this.tableRowForTableC(styleColorBeanList.get(k).getColor());
                    this.tableC.addView(tableRowForTableC1);

                    final int finalK = k;
                    tableRowForTableC1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String articleOption1 = styleColorBeanList.get(finalK).getArticleOption();

                            if (articleOption1.equals(articleOption)) {
                                SwitchingTabActivity.viewPager.setCurrentItem(0);
                                SwitchingTabActivity.tabLayout.getTabAt(0).select();

                            } else {
                                Reusable_Functions.sDialog(context, "Loading data...");
                                requestStyleDetailsAPI(context, articleOption1);
                            }


                        }
                    });

                    TableRow tableRowForTableD1 = this.taleRowForTableD(styleColorBeanList.get(k));
                    tableRowForTableD1.setBackgroundColor(Color.LTGRAY);
                    this.tableD.addView(tableRowForTableD1);

                    salesUnitTotal = salesUnitTotal + styleColorBeanList.get(k).getTwSaleTotQty();
                    sohTotal = sohTotal + styleColorBeanList.get(k).getStkOnhandQty();
                    fwdweekTotal = fwdweekTotal + styleColorBeanList.get(k).getFwdWeekCover();


                    TableRow tableRowForTableC2 = this.tableRowForTableCSpace();
                    tableRowForTableC2.setBackgroundColor(Color.parseColor("#ffffff"));
                    this.tableC.addView(tableRowForTableC2);

                    TableRow tableRowForTableD2 = this.taleRowForTableDtotal();
                    tableRowForTableD2.setBackgroundColor(Color.parseColor("#ffffff"));
                    this.tableD.addView(tableRowForTableD2);

                    salesUnitTotal = 0;
                    sohTotal = 0;
                    fwdweekTotal = 0;
                }
                //If current color equal to previous color & last entry in array, then add the row
                else if (styleColorBeanList.get(k).getColor().trim().equals(styleColorBeanList.get(k - 1).getColor().trim())) {
                    TableRow tableRowForTableC = this.tableRowForTableC("");
                    tableRowForTableC.setBackgroundColor(Color.parseColor("#ffffff"));
                    this.tableC.addView(tableRowForTableC);

                    TableRow taleRowForTableD = this.taleRowForTableD(styleColorBeanList.get(k));
                    taleRowForTableD.setBackgroundColor(Color.LTGRAY);
                    this.tableD.addView(taleRowForTableD);

                    salesUnitTotal = salesUnitTotal + styleColorBeanList.get(k).getTwSaleTotQty();
                    sohTotal = sohTotal + styleColorBeanList.get(k).getStkOnhandQty();
                    fwdweekTotal = fwdweekTotal + styleColorBeanList.get(k).getFwdWeekCover();


                }
                //If current color not equal to previous color, then add total row & current row
                else if (!styleColorBeanList.get(k).getColor().trim().equals(styleColorBeanList.get(k - 1).getColor().trim())) {

                    TableRow tableRowForTableC1 = this.tableRowForTableCSpace();
                    this.tableC.addView(tableRowForTableC1);


                    TableRow taleRowForTableD = this.taleRowForTableDtotal();
                    this.tableD.addView(taleRowForTableD);

                    salesUnitTotal = 0;
                    sohTotal = 0;
                    fwdweekTotal = 0;

                    TableRow tableRowForTableC = this.tableRowForTableC(styleColorBeanList.get(k).getColor());
                    tableRowForTableC.setBackgroundColor(Color.parseColor("#dfdedf"));
                    this.tableC.addView(tableRowForTableC);


                    final int finalK = k;
                    tableRowForTableC.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String articleOption1 = styleColorBeanList.get(finalK).getArticleOption();

                            if (articleOption1.equals(articleOption)) {
                                SwitchingTabActivity.viewPager.setCurrentItem(0);
                                SwitchingTabActivity.tabLayout.getTabAt(0).select();

                            } else {
                                Reusable_Functions.sDialog(context, "Loading data...");
                                requestStyleDetailsAPI(context, articleOption1);
                            }
                        }
                    });

                    TableRow tableRowForTableD = this.taleRowForTableD(styleColorBeanList.get(k));
                    tableRowForTableD.setBackgroundColor(Color.LTGRAY);
                    this.tableD.addView(tableRowForTableD);

                    salesUnitTotal = salesUnitTotal + styleColorBeanList.get(k).getTwSaleTotQty();
                    sohTotal = sohTotal + styleColorBeanList.get(k).getStkOnhandQty();
                    fwdweekTotal = fwdweekTotal + styleColorBeanList.get(k).getFwdWeekCover();
                }
            }
        }
    }

    private TableRow tableRowForTableCSpace()
    {
        TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], TableRow.LayoutParams.MATCH_PARENT);
        params.setMargins(0, 2, 0, 0);

        TableRow tableRowForTableC = new TableRow(this.context);
        TextView textView = this.bodyTextView("     ");
        textView.setBackgroundColor(Color.parseColor("#f8f6f6"));
        tableRowForTableC.addView(textView, params);

        return tableRowForTableC;
    }

    TableRow tableRowForTableC(String styleDetails) {
        TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], TableRow.LayoutParams.MATCH_PARENT);
        params.setMargins(0, 2, 0, 0);

        TableRow tableRowForTableC = new TableRow(this.context);

        TextView textView = this.bodyTextView(styleDetails);
        textView.setBackgroundColor(Color.parseColor("#ffffff"));
        textView.setTextColor(Color.parseColor("#000000"));
        textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        tableRowForTableC.addView(textView, params);
        return tableRowForTableC;

    }

    TableRow taleRowForTableDtotal() {

        TableRow taleRowForTableD = new TableRow(this.context);

        int loopCount = ((TableRow) this.tableB.getChildAt(0)).getChildCount();
        String info[] = {"   Total   ", String.valueOf(salesUnitTotal), String.valueOf(sohTotal), "-"};
        for (int x = 0; x < loopCount; x++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(headerCellsWidth[x + 1], TableRow.LayoutParams.MATCH_PARENT);
            params.setMargins(2, 2, 0, 0);

            TextView textViewB = this.bodyTextView(info[x]);
            textViewB.setBackgroundColor(Color.parseColor("#f8f6f6"));
            taleRowForTableD.addView(textViewB, params);
            textViewB.setTextColor(Color.parseColor("#000000"));
            textViewB.setTypeface(Typeface.DEFAULT_BOLD);
            salesUnitTotal = 0;
            sohTotal = 0;
            fwdweekTotal = 0;

        }

        return taleRowForTableD;

    }


    TableRow taleRowForTableD(StyleColorBean styleColor) {

        TableRow taleRowForTableD = new TableRow(this.context);

        int loopCount = ((TableRow) this.tableB.getChildAt(0)).getChildCount();
        String info[] = {
                styleColor.getSize(),
                String.valueOf(styleColor.getTwSaleTotQty()),
                String.valueOf(styleColor.getStkOnhandQty()),
                String.valueOf(String.format("%.1f", styleColor.getFwdWeekCover()))

        };

        for (int x = 0; x < loopCount; x++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(headerCellsWidth[x + 1], TableRow.LayoutParams.MATCH_PARENT);
            params.setMargins(2, 2, 0, 0);

            TextView textViewB = this.bodyTextView(info[x]);
            textViewB.setBackgroundColor(Color.parseColor("#ffffff"));
            textViewB.setTextColor(Color.parseColor("#000000"));
            textViewB.setTypeface(Typeface.DEFAULT_BOLD);
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
        //headerTextView.getLayoutParams().height = 20;

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


    private void requestStyleDetailsAPI(final Context context, String articleOption) {
        String url = ConstsCore.web_url + "/v1/display/productdetails/" + userId + "?articleOption=" + articleOption.replaceAll(" ", "%20").replaceAll("&", "%26");

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            if (response.equals("") || response == null || response.length() == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "No data found", Toast.LENGTH_LONG).show();

                            } else {

                                Reusable_Functions.hDialog();
                                JSONObject styleDetails = response.getJSONObject(0);

                                String articleOption = styleDetails.getString("articleOption");
                                String productName = styleDetails.getString("productName");
                                String collectionName = styleDetails.getString("collectionName");
                                String productFabricDesc = styleDetails.getString("productFabricDesc");
                                String productFitDesc = styleDetails.getString("productFitDesc");
                                String productFinishDesc = styleDetails.getString("productFinishDesc");
                                String seasonName = styleDetails.getString("seasonName");

                                String promoFlg = styleDetails.getString("promoFlg");
                                String keyProductFlg = styleDetails.getString("keyProductFlg");


                                String firstReceiptDate = styleDetails.getString("firstReceiptDate");
                                String lastReceiptDate = styleDetails.getString("lastReceiptDate");
                                double fwdWeekCover = styleDetails.getDouble("fwdWeekCover");

                                int twSaleTotQty = styleDetails.getInt("twSaleTotQty");
                                int lwSaleTotQty = styleDetails.getInt("lwSaleTotQty");
                                int ytdSaleTotQty = styleDetails.getInt("ytdSaleTotQty");

                                int stkOnhandQty = styleDetails.getInt("stkOnhandQty");
                                int stkGitQty = styleDetails.getInt("stkGitQty");
                                int targetStock = styleDetails.getInt("targetStock");

                                int unitGrossPrice = styleDetails.getInt("unitGrossPrice");
                                double sellThruUnitsRcpt = styleDetails.getDouble("sellThruUnitsRcpt");
                                double ros = styleDetails.getDouble("ros");

                                String articleCode = styleDetails.getString("articleCode");
                                String productImageURL = styleDetails.getString("productImageURL");
                                styleDetailsBean = new StyleDetailsBean();
                                styleDetailsBean.setProductName(productName);
                                styleDetailsBean.setCollectionName(collectionName);
                                styleDetailsBean.setProductFabricDesc(productFabricDesc);
                                styleDetailsBean.setProductFitDesc(productFitDesc);
                                styleDetailsBean.setProductFinishDesc(productFinishDesc);
                                styleDetailsBean.setSeasonName(seasonName);

                                styleDetailsBean.setFirstReceiptDate(firstReceiptDate);
                                styleDetailsBean.setLastReceiptDate(lastReceiptDate);
                                styleDetailsBean.setFwdWeekCover(fwdWeekCover);

                                styleDetailsBean.setTwSaleTotQty(twSaleTotQty);
                                styleDetailsBean.setLwSaleTotQty(lwSaleTotQty);
                                styleDetailsBean.setYtdSaleTotQty(ytdSaleTotQty);

                                styleDetailsBean.setStkOnhandQty(stkOnhandQty);
                                styleDetailsBean.setStkGitQty(stkGitQty);
                                styleDetailsBean.setTargetStock(targetStock);

                                styleDetailsBean.setUnitGrossPrice(unitGrossPrice);
                                styleDetailsBean.setSellThruUnitsRcpt(sellThruUnitsRcpt);
                                styleDetailsBean.setRos(ros);

                                styleDetailsBean.setPromoFlg(promoFlg);
                                styleDetailsBean.setKeyProductFlg(keyProductFlg);
                                styleDetailsBean.setProductImageURL(productImageURL);

                                Intent i = getActivity().getIntent();
                                Intent intent = new Intent(context, SwitchingTabActivity.class);
                                intent.putExtra("articleCode", articleCode);
                                intent.putExtra("articleOption", articleOption);
                                intent.putExtra("styleDetailsBean", styleDetailsBean);
                                intent.putExtra("selCollectionname", i.getExtras().getString("selCollectionname"));
                                intent.putExtra("selOptionName", i.getExtras().getString("selOptionName"));
                                startActivity(intent);
                                SwitchingTabActivity.switchingTabActivity.finish();
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
                        Toast.makeText(context, "Network connectivity fail", Toast.LENGTH_LONG).show();
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



