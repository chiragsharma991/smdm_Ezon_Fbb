package apsupportapp.aperotechnologies.com.designapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import java.util.List;
import java.util.Map;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class Style_Fragment extends Fragment {
    TableLayout tableA;
    TableLayout tableB;
    TableLayout tableC;
    TableLayout tableD;

    TextView txtSales, txtGit, txtFwdWeekCover, txtSalesUnit, txtSalesThruUnit, txtRos, txtSOh, txtarticleOption;
    View view;
    String userId, bearertoken;
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
    String articleCode, articleOption;
    int offsetvalue = 0, limit = 100;
    int count = 0;
    String TA="StyleActivity";

    // set the header titles
    String headers[] = {
            "             Color            ",
            "   Size   ",
            "    TW Sales\n\t\t\t(U)    ",
            "  SOH\n\t\t\t(U)    ",
            "    FWC   "

    };

    List<SampleObject> sampleObjects = sampleObjects();
    int headerCellsWidth[] = new int[headers.length];
    private String TAG="StyleActivity";
    private LinearLayout LinearTable;
    private RelativeLayout Style_loadingBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Bundle bundle = getActivity().getIntent().getExtras();
        articleCode = bundle.getString("articleCode");
        articleOption = bundle.getString("articleOption");
        Log.e("Option in Style Fragment"," "+articleOption);
        context = getContext();
        styleColorBeanList = new ArrayList<>();
        m_config = MySingleton.getInstance(context);

        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();

        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
           // Reusable_Functions.sDialog(context, "Loading data...");
            requestStyleSizeDetailsAPI();
            requestStyleColorDetailsAPI(offsetvalue, limit);
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_LONG).show();
            Style_loadingBar.setVisibility(View.GONE);

        }
    }

    private void requestStyleColorDetailsAPI(int offsetvalue1, final int limit1) {

        String url = ConstsCore.web_url + "/v1/display/sizes/" + userId + "?articleOption=" + articleOption.replace(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e(TAG,"requestStyleColorDetailsAPI   "+url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG,"requestStyleColorDetailsAPI :   "+response.toString());
                        Log.e("Response lenght ", String.valueOf(response.length()));
                        try {
                            if (response.equals(null) || response == null || response.length() == 0) {
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
                                Log.e("styleColorBeanList", styleColorBeanList.size() + "");

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

                                    Log.e("style bean", color + size);

                                    styleColorBeanList.add(styleColorBean);

                                    Log.e("added to array", "value: " + i);

                                }

                                Log.e("array size", "value: " + styleColorBeanList.size());
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
                            Log.e("Exception e", e.toString() + "");
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

                        // Toast.makeText(LoginActivity.this,"Invalid User",Toast.LENGTH_LONG).show();
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

    private void createTable()
    {
        int sumTW=0,sumSOH=0;
        for (int i = 0; i < styleColorBeanList.size(); i++) {

            LayoutInflater layoutInflater = (LayoutInflater)getActivity()
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            view = (ViewGroup) layoutInflater.inflate(R.layout.activity_stylefragment_child, null);
            TextView StyleColorName = (TextView) view.findViewById(R.id.styleColorName);
            TextView StyleFwd = (TextView) view.findViewById(R.id.styleFwd);
            TextView StyleSize = (TextView) view.findViewById(R.id.styleSize);
            TextView StyleSOH = (TextView) view.findViewById(R.id.styleSOH);
            TextView StyleTW = (TextView) view.findViewById(R.id.styleTW);
            StyleColorName.setText(styleColorBeanList.get(i).getColor());
            StyleFwd.setText(String.format("%.1f",styleColorBeanList.get(i).getFwdWeekCover()));
            StyleSize.setText(styleColorBeanList.get(i).getSize());
            StyleSOH.setText(""+styleColorBeanList.get(i).getStkOnhandQty());
            StyleTW.setText(""+styleColorBeanList.get(i).getTwSaleTotQty());
            sumTW+=styleColorBeanList.get(i).getTwSaleTotQty();
            sumSOH+=styleColorBeanList.get(i).getStkOnhandQty();

            LinearTable.addView(view);

        }
        LayoutInflater layoutInflater = (LayoutInflater)getActivity()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        view = (ViewGroup) layoutInflater.inflate(R.layout.activity_style_totalchild, null);
        TextView StyleTW_total = (TextView) view.findViewById(R.id.styleTW_total);
        TextView StyleSOH_total = (TextView) view.findViewById(R.id.styleSOH_total);
        StyleTW_total.setText(""+sumTW);
        StyleSOH_total.setText(""+sumSOH);

        LinearTable.addView(view);
        Reusable_Functions.hDialog();



    }

    private void requestStyleSizeDetailsAPI() {

        String url = ConstsCore.web_url + "/v1/display/styles/" + userId + "?articleOption=" + articleOption.replaceAll(" ", "%20").replaceAll("&", "%26");
        Log.e(TAG,"requestStyleSizeDetailsAPI   "+url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG,"requestStyleSizeDetailsAPI :   "+response.toString());
                        try {
                            if (response.equals(null) || response == null) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(context, "No data Found", Toast.LENGTH_LONG).show();

                            } else {
                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject styleDetails = response.getJSONObject(i);
                                    int Sales = styleDetails.getInt("twSaleNetVal");
                                    int twSaleTotQty = styleDetails.getInt("twSaleTotQty");
                                    double fwdWeekCover = styleDetails.getDouble("fwdWeekCover");
                                    int stkGitQty = styleDetails.getInt("stkGitQty");

                                    double sellThruUnitsRcpt = styleDetails.getDouble("sellThruUnitsRcpt");
                                    double ros = styleDetails.getDouble("ros");

                                    int stkOnhandQty = styleDetails.getInt("stkOnhandQty");

                                    txtSales.setText(": "+"\u20B9" + Sales);
                                    txtSalesUnit.setText(": " + twSaleTotQty);
                                    txtFwdWeekCover.setText(": " + String.format("%.1f", fwdWeekCover));
                                    txtGit.setText(": " + stkGitQty);
                                    txtSalesThruUnit.setText(": " + String.format("%.1f", sellThruUnitsRcpt) + "%");
                                    txtRos.setText(": " + String.format("%.1f", ros));
                                    txtSOh.setText(": " + stkOnhandQty);
                                    txtarticleOption.setText("" + articleOption);
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Exception e", e.toString() + "");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        // Toast.makeText(LoginActivity.this,"Invalid User",Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " +bearertoken);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.style_fragment, container, false);
        Log.e(TA, "Style fragment onCreateView: ");
        context = view.getContext();
        relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);
        relativeLayout.setBackgroundColor(Color.parseColor("#f8f6f6"));  //dfdedf
        Style_loadingBar = (RelativeLayout) view.findViewById(R.id.style_loadingBar);
        txtarticleOption = (TextView) view.findViewById(R.id.txtarticleOption);
        //style size text
        txtSales = (TextView) view.findViewById(R.id.txtSales);
        txtSalesUnit = (TextView) view.findViewById(R.id.txtSalesUnit);
        LinearTable = (LinearLayout) view.findViewById(R.id.linearTable);
        txtFwdWeekCover = (TextView) view.findViewById(R.id.txtFwdCover);
        txtGit = (TextView) view.findViewById(R.id.txtGit);
        txtSalesThruUnit = (TextView) view.findViewById(R.id.txtSalesThruUnit);
        txtRos = (TextView) view.findViewById(R.id.txtRos);
        txtSOh = (TextView) view.findViewById(R.id.txtSOh);

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
       // relativeLayout.setBackgroundColor(Color.WHITE);

        int headerCellsWidth[] = new int[headers.length];

        return view;
    }

    List<SampleObject> sampleObjects() {

        List<SampleObject> sampleObjects = new ArrayList<SampleObject>();
        for (int x = 1; x <= 20; x++) {

            SampleObject sampleObject = new SampleObject(
                    "1 ", "2 ", "3 ", "4 ",
                    " 5"
            );


            sampleObjects.add(sampleObject);
        }

        return sampleObjects;

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
                TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 0, 0, 0);
        TextView textView = this.headerTextView(headers[0]);
        textView.setBackgroundColor(Color.parseColor("#2277b1"));
        textView.setTextColor(Color.parseColor("#ffffff"));
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        componentATableRow.addView(textView);

        return componentATableRow;
    }

    // generate table row of table B
    TableRow componentBTableRow() {

        TableRow componentBTableRow = new TableRow(this.context);
componentBTableRow.setBackgroundColor(Color.parseColor("#2277b1"));
        int headerFieldCount = headers.length;

        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
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


        //  int cnt=0;
        // just seeing some header cell width
        for (int x = 0; x < this.headerCellsWidth.length; x++) {
            Log.v("Product Data", this.headerCellsWidth[x] + "");
        }
        int size = styleColorBeanList.size() - 2;
        //  int size1=styleColorBeanList.size()-1;


        for (int k = 0; k < styleColorBeanList.size(); k++) {
            if (k == 0 && styleColorBeanList.size() == 1) {
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

                salesUnitTotal = salesUnitTotal + styleColorBeanList.get(k).getTwSaleTotQty();
                sohTotal = sohTotal + styleColorBeanList.get(k).getStkOnhandQty();
                fwdweekTotal = fwdweekTotal + styleColorBeanList.get(k).getFwdWeekCover();

                TableRow taleRowForTableD = this.taleRowForTableD(styleColorBeanList.get(k));
                taleRowForTableD.setBackgroundColor(Color.parseColor("#dfdedf"));
                this.tableD.addView(taleRowForTableD);

                TableRow tableRowForTableC1 = this.tableRowForTableCSpace();
                tableRowForTableC1.setBackgroundColor(Color.parseColor("#dfdedf"));
                this.tableC.addView(tableRowForTableC1);

                TableRow taleRowForTableD1 = this.taleRowForTableDtotal();
                taleRowForTableD1.setBackgroundColor(Color.parseColor("#dfdedf"));
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
                    Log.e("same color k=", k + "size" + styleColorBeanList.size());

                    TableRow tableRowForTableC = this.tableRowForTableC("");
                    tableRowForTableC.setBackgroundColor(Color.parseColor("#dfdedf"));
                    this.tableC.addView(tableRowForTableC);

                    TableRow taleRowForTableD = this.taleRowForTableD(styleColorBeanList.get(k));
                    taleRowForTableD.setBackgroundColor(Color.LTGRAY);
                    this.tableD.addView(taleRowForTableD);

                    salesUnitTotal = salesUnitTotal + styleColorBeanList.get(k).getTwSaleTotQty();
                    sohTotal = sohTotal + styleColorBeanList.get(k).getStkOnhandQty();
                    fwdweekTotal = fwdweekTotal + styleColorBeanList.get(k).getFwdWeekCover();


                    TableRow tableRowForTableC1 = this.tableRowForTableCSpace();
                    tableRowForTableC1.setBackgroundColor(Color.parseColor("#dfdedf"));
                    this.tableC.addView(tableRowForTableC1);

                    TableRow taleRowForTableD2 = this.taleRowForTableDtotal();
                    taleRowForTableD2.setBackgroundColor(Color.parseColor("#2277b1"));
                    this.tableD.addView(taleRowForTableD2);

                    salesUnitTotal = 0;
                    sohTotal = 0;
                    fwdweekTotal = 0;
                }
                //If current color not equal to previous color & last entry in array, then add total for previous, current row & total for current row
                else if (!styleColorBeanList.get(k).getColor().trim().equals(styleColorBeanList.get(k - 1).getColor().trim()) && k == styleColorBeanList.size() - 1) {
                    TableRow tableRowForTableC = this.tableRowForTableCSpace();
                    tableRowForTableC.setBackgroundColor(Color.parseColor("#dfdedf"));
                    this.tableC.addView(tableRowForTableC);

                    TableRow tableRowForTableD = this.taleRowForTableDtotal();
                    tableRowForTableD.setBackgroundColor(Color.parseColor("#2277b1"));
                    this.tableD.addView(tableRowForTableD);

                    salesUnitTotal = 0;
                    sohTotal = 0;
                    fwdweekTotal = 0;

                    TableRow tableRowForTableC1 = this.tableRowForTableC(styleColorBeanList.get(k).getColor());
                    //TableRow tableRowForTableC = this.tableRowForTableC("");
                    tableRowForTableC1.setBackgroundColor(Color.parseColor("#dfdedf"));
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
                    tableRowForTableC2.setBackgroundColor(Color.parseColor("#dfdedf"));
                    this.tableC.addView(tableRowForTableC2);

                    TableRow tableRowForTableD2 = this.taleRowForTableDtotal();
                    tableRowForTableD2.setBackgroundColor(Color.parseColor("#2277b1"));
                    this.tableD.addView(tableRowForTableD2);

                    salesUnitTotal = 0;
                    sohTotal = 0;
                    fwdweekTotal = 0;
                }
                //If current color equal to previous color & last entry in array, then add the row
                else if (styleColorBeanList.get(k).getColor().trim().equals(styleColorBeanList.get(k - 1).getColor().trim())) {
                    Log.e("same color k less than size k=", k + "size" + styleColorBeanList.size());
                    //TableRow tableRowForTableC = this.tableRowForTableC(styleColorBeanList.get(k).getColor());
                    TableRow tableRowForTableC = this.tableRowForTableC("");
                    tableRowForTableC.setBackgroundColor(Color.parseColor("#dfdedf"));
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
                    Log.e("else loop add to table k=", k + "size" + styleColorBeanList.size());

                    TableRow tableRowForTableC1 = this.tableRowForTableCSpace();
                    tableRowForTableC1.setBackgroundColor(Color.parseColor("#dfdedf"));
                    this.tableC.addView(tableRowForTableC1);


                    TableRow taleRowForTableD = this.taleRowForTableDtotal();
                    taleRowForTableD.setBackgroundColor(Color.parseColor("#2277b1"));
                    this.tableD.addView(taleRowForTableD);

                    salesUnitTotal = 0;
                    sohTotal = 0;
                    fwdweekTotal = 0;

                    TableRow tableRowForTableC = this.tableRowForTableC(styleColorBeanList.get(k).getColor());
                    //TableRow tableRowForTableC = this.tableRowForTableC("");
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

            //           if(styleColorBeanList.get(k).getColor().trim().equals(styleColorBeanList.get(k+1).getColor().trim()))
//            {
//                TableRow tableRowForTableC = this.tableRowForTableC(styleColorBeanList.get(k).getColor());
//                tableRowForTableC.setBackgroundColor(Color.WHITE);
//                this.tableC.addView(tableRowForTableC);
//
//                salesUnitTotal=salesUnitTotal+styleColorBeanList.get(k).getTwSaleTotQty();
//                sohTotal=sohTotal+styleColorBeanList.get(k).getStkOnhandQty();
//                fwdweekTotal=fwdweekTotal+styleColorBeanList.get(k).getFwdWeekCover();
//
//                TableRow taleRowForTableD = this.taleRowForTableD(styleColorBeanList.get(k));
//                taleRowForTableD.setBackgroundColor(Color.LTGRAY);
//                this.tableD.addView(taleRowForTableD);
//            }else
//            {
//                cnt++;
//                TableRow tableRowForTableC1 = this.tableRowForTableCSpace();
//                tableRowForTableC1.setBackgroundColor(Color.WHITE);
//                this.tableC.addView(tableRowForTableC1);
//
//                TableRow taleRowForTableD = this.taleRowForTableDtotal();
//                taleRowForTableD.setBackgroundColor(Color.parseColor("#B73020"));
//                this.tableD.addView(taleRowForTableD);
//
//           }
//
//
//            if (k==size)
//            {
//               if (cnt==1)
//                   break;
//
//                TableRow tableRowForTableC1 = this.tableRowForTableCSpace();
//                tableRowForTableC1.setBackgroundColor(Color.WHITE);
//                this.tableC.addView(tableRowForTableC1);
//
//                TableRow taleRowForTableD = this.taleRowForTableDtotal();
//                taleRowForTableD.setBackgroundColor(Color.parseColor("#B73020"));
//                this.tableD.addView(taleRowForTableD);
//
//
//            }


        }


    }

    private TableRow tableRowForTableCSpace() {
        TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], TableRow.LayoutParams.MATCH_PARENT);
        params.setMargins(0, 2, 0, 0);

        TableRow tableRowForTableC = new TableRow(this.context);
//        TextView textView = this.bodyTextView(sampleObject.header1);
        TextView textView = this.bodyTextView("     ");
        textView.setBackgroundColor(Color.parseColor("#ffffff"));
        tableRowForTableC.addView(textView, params);

        return tableRowForTableC;
    }

    TableRow tableRowForTableC(String styleDetails) {
        TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], TableRow.LayoutParams.MATCH_PARENT);
        params.setMargins(0, 2, 0, 0);

        Log.e("style color ", styleDetails);
        TableRow tableRowForTableC = new TableRow(this.context);
//        TextView textView = this.bodyTextView(sampleObject.header1);
        TextView textView = this.bodyTextView(styleDetails);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        tableRowForTableC.addView(textView, params);

        return tableRowForTableC;

    }


//    TableRow tableRowForTableC(SampleObject sampleObject) {
//
//        TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], TableRow.LayoutParams.MATCH_PARENT);
//        params.setMargins(0, 2, 0, 0);
//
//
//             TableRow tableRowForTableC = new TableRow(this.context);
////        TextView textView = this.bodyTextView(sampleObject.header1);
//             TextView textView = this.bodyTextView(styleDetailsBean.getProductName());
//             tableRowForTableC.addView(textView, params);
//
//             return tableRowForTableC;
//
//    }


//    TableRow taleRowForTableD(SampleObject sampleObject) {
//
//        TableRow taleRowForTableD = new TableRow(this.context);
//
//        int loopCount = ((TableRow) this.tableB.getChildAt(0)).getChildCount();
//        String info[] = {
//                styleDetailsBeanList.get(0).getCollectionName(),
//                styleDetailsBeanList.get(0).getProductFabricDesc(),
//                styleDetailsBeanList.get(0).getProductFitDesc(),
//                styleDetailsBeanList.get(0).getProductFinishDesc()
//
//        };
//
//        for (int x = 0; x < loopCount; x++) {
//            TableRow.LayoutParams params = new TableRow.LayoutParams(headerCellsWidth[x + 1], TableRow.LayoutParams.MATCH_PARENT);
//            params.setMargins(2, 2, 0, 0);
//
//            TextView textViewB = this.bodyTextView(info[x]);
//            taleRowForTableD.addView(textViewB, params);
//        }
//
//        return taleRowForTableD;
//
//    }


    TableRow taleRowForTableDtotal() {

        TableRow taleRowForTableD = new TableRow(this.context);

        int loopCount = ((TableRow) this.tableB.getChildAt(0)).getChildCount();
        // String info[] = { "   Total   ", String.valueOf(salesUnitTotal), String.valueOf(sohTotal), String.valueOf(fwdweekTotal)};
        String info[] = {"   Total   ", String.valueOf(salesUnitTotal), String.valueOf(sohTotal), "-"};
        for (int x = 0; x < loopCount; x++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(headerCellsWidth[x + 1], TableRow.LayoutParams.MATCH_PARENT);
            params.setMargins(2, 2, 0, 0);

            TextView textViewB = this.bodyTextView(info[x]);
            textViewB.setBackgroundColor(Color.parseColor("#2277b1"));
            taleRowForTableD.addView(textViewB, params);
            textViewB.setTextColor(Color.parseColor("#ffffff"));
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


        Log.i("URL style  ", "" + url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Style details :   ", response.toString());
                        try {

                            if (response.equals(null) || response == null || response.length() == 0) {
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
                                Log.e("row4:===", productImageURL);
                                //   int usp = styleDetails.getInt("usp");

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
                            Log.e("Exception e", e.toString() + "");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        Log.e("", "" + error.networkResponse + "");
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



