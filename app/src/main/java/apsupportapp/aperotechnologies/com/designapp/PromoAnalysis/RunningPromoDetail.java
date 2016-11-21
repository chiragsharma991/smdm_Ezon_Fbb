package apsupportapp.aperotechnologies.com.designapp.PromoAnalysis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.MySingleton;
import apsupportapp.aperotechnologies.com.designapp.OnRowPressListener;
import apsupportapp.aperotechnologies.com.designapp.ProductNameBean;
import apsupportapp.aperotechnologies.com.designapp.R;

/*public class RunningPromoDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_promo_detail);
    }
}*/

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
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
import com.google.gson.Gson;

import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by pamrutkar on 23/08/16.
 */
public class RunningPromoDetail extends AppCompatActivity {
    TableLayout tableAProd_Frag;
    TableLayout tableBProd_Frag;
    TableLayout tableCProd_Frag;
    TableLayout tableDProd_Frag;

    Button btnProdFilter;
    ViewGroup view;
    HorizontalScrollView horizontalScrollViewB;
    HorizontalScrollView horizontalScrollViewD;


    ScrollView scrollViewC;
    ScrollView scrollViewD;
    RequestQueue queue;
    //passData data;
    Context context;

    RelativeLayout relativeLayout;
    public static RelativeLayout relProd_Frag;
    // set the header titles
    String headers[] = {
            "                  MC               ",
            "     Promo Sales    ",
            "     Promo Sales(U)   ",
            "     SOH(U)   ",


    };

    int headerCellsWidth[] = new int[headers.length];
    TextView txtStoreCode, txtStoreDesc;
    String userId, bearertoken;
    MySingleton m_config;
    int offsetvalue = 0, limit = 100;
    int count = 0;

    SharedPreferences sharedPreferences;
    String TAG = "RunningPromoDetail";


    //

    RunningPromoListDisplay runningPromoListDisplay;
    ArrayList<RunningPromoListDisplay> promoList;
    Gson gson;
    Bundle data;
    RelativeLayout ImageBtnBack;
    private TextView txtHeader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_promo_detail);
        getSupportActionBar().hide();
       // getSupportActionBar().setTitle("Running Promo Details");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;


        //Intent i = getActivity().getIntent();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RunningPromoDetail.this.getBaseContext());
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        m_config = MySingleton.getInstance(RunningPromoDetail.this);
        gson = new Gson();
        promoList = new ArrayList<>();
        findview();

        Cache cache = new DiskBasedCache(RunningPromoDetail.this.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();


        relProd_Frag = (RelativeLayout) findViewById(R.id.rel);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
      //  relativeLayout.setBackgroundColor(Color.WHITE);
        relProd_Frag.setVisibility(View.VISIBLE);


        if (Reusable_Functions.chkStatus(context)) {

            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            offsetvalue = 0;
            limit = 100;
            count = 0;

            requestProductAPI(offsetvalue, limit);

        } else {
            Toast.makeText(RunningPromoDetail.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
        }


        initComponents();
        setComponentsId();
        setScrollViewAndHorizontalScrollViewTag();
        // no need to assemble component A, since it is just a table
        horizontalScrollViewB.addView(tableBProd_Frag);
        scrollViewC.addView(tableCProd_Frag);
        scrollViewD.addView(horizontalScrollViewD);
        horizontalScrollViewD.addView(tableDProd_Frag);

        addComponentToMainLayout();

    }






    private void findview()
    {
        txtStoreCode = (TextView) findViewById(R.id.txtStoreCode);
        txtStoreDesc = (TextView) findViewById(R.id.txtStoreName);
        txtHeader = (TextView) findViewById(R.id.tstoreCode);
        ImageBtnBack=(RelativeLayout)findViewById(R.id.rpd_ImageBtnBack);

        data=getIntent().getExtras();



        ImageBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RunningPromoDetail.this,RunningPromoActivity.class);
                startActivity(intent);
                finish();

            }
        });


    }




    // initalized components
    private void initComponents() {
        tableAProd_Frag = new TableLayout(this.context);
        tableBProd_Frag = new TableLayout(this.context);
        tableCProd_Frag = new TableLayout(this.context);
        tableDProd_Frag = new TableLayout(this.context);
        tableDProd_Frag = new TableLayout(this.context);
        horizontalScrollViewB = new MyHorizontalScrollView(this.context);
        horizontalScrollViewD = new MyHorizontalScrollView(this.context);
        scrollViewC = new MyScrollView(this.context);
        scrollViewD = new MyScrollView(this.context);
        tableAProd_Frag.setBackgroundColor(Color.GREEN);
        horizontalScrollViewB.setBackgroundColor(Color.LTGRAY);
    }

    // set essential component IDs
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
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, 70);
        params.setMargins(2, 0, 0, 0);
        TextView textView = this.headerTextView(headers[0]);
        textView.setBackgroundColor(Color.parseColor("#B73020"));
        textView.setTextColor(Color.parseColor("#ffffff"));
        componentATableRow.addView(textView);
        return componentATableRow;
    }

    // generate table row of table B
    TableRow componentBTableRow() {

        TableRow componentBTableRow = new TableRow(this.context);

        int headerFieldCount = headers.length;

        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, 70);
        params.setMargins(2, 0, 0, 0);

        for (int x = 0; x < (headerFieldCount - 1); x++) {
            TextView textView = this.headerTextView(this.headers[x + 1]);
            textView.setBackgroundColor(Color.parseColor("#B73020"));
            textView.setTextColor(Color.parseColor("#ffffff"));
            textView.setLayoutParams(params);
            componentBTableRow.addView(textView);
        }
        return componentBTableRow;
    }

    private void generateTableC_AndTable_B() {

        // just seeing some header cell width
        for (int x = 0; x < this.headerCellsWidth.length; x++) {
            Log.v("Product Data", this.headerCellsWidth[x] + "");
        }

        for (int k = 0; k < promoList.size(); k++) {
            final TableRow tableRowForTableCProd_Frag;
            tableRowForTableCProd_Frag = this.tableRowForTableCProd_Frag(promoList.get(k).getProdLevel6Desc());
            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT, 120);
            params.setMargins(2, 0, 0, 0);

            final TableRow tableRowForTableDProd_Frag = this.tableRowForTableDProd_Frag(promoList.get(k));
            tableRowForTableCProd_Frag.setBackgroundColor(Color.WHITE);
            tableRowForTableDProd_Frag.setBackgroundColor(Color.LTGRAY);
            final int i = k;

//            tableRowForTableCProd_Frag.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    KeyProductActivity.prodName = productNameBeanArrayList.get(i).getProductName();
//                    relProd_Frag.setVisibility(View.GONE);
//                    ViewPager viewPager = (ViewPager) view.getParent();
//                    LinearLayout layout = (LinearLayout) viewPager.getParent();
//                    TabLayout tab = (TabLayout) layout.getChildAt(one);
//                    tab.getTabAt(one).select();
//                    rowPressListener.communicateToFragment2(productNameBeanArrayList.get(i).getProductName());
//                }
//            });
            this.tableCProd_Frag.addView(tableRowForTableCProd_Frag);
            this.tableDProd_Frag.addView(tableRowForTableDProd_Frag);
        }
        Reusable_Functions.hDialog();
    }

    TableRow tableRowForTableCProd_Frag(String productNameDetails) {

        TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], TableRow.LayoutParams.MATCH_PARENT);
        params.setMargins(2, 2, 0, 0);


        TableRow tableRowForTableCProd_Frag = new TableRow(this.context);
//        TextView textView = this.bodyTextView(sampleObject.header1);
        TextView textView = this.bodyTextView(productNameDetails);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        Log.e("Value", textView.getText().toString());
        tableRowForTableCProd_Frag.addView(textView, params);
        return tableRowForTableCProd_Frag;
    }

    TableRow tableRowForTableDProd_Frag(RunningPromoListDisplay runningdetail) {
        TableRow tableRowForTableDProd_Frag = new TableRow(this.context);
        int loopCount = ((TableRow) this.tableBProd_Frag.getChildAt(0)).getChildCount();
        //NetPercent=String.valueOf(runningdetail.getDayNetSalesPercent()).concat("%");
        String info[] = {
                String.valueOf("\u20B9"+(int)runningdetail.getDurSaleNetVal()),
                String.valueOf(""+(int)runningdetail.getDurSaleTotQty()),
                String.valueOf(""+(int)runningdetail.getStkOnhandQty()),


        };

        for (int x = 0; x < loopCount; x++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(headerCellsWidth[x + 1], TableRow.LayoutParams.MATCH_PARENT);
            params.setMargins(2, 2, 0, 0);
            TextView textViewB = this.bodyTextView(String.valueOf(info[x]));

            tableRowForTableDProd_Frag.addView(textViewB, params);
        }
        return tableRowForTableDProd_Frag;

    }

    // table cell standard TextView
    TextView bodyTextView(String label) {

        TextView bodyTextView = new TextView(this.context);
        bodyTextView.setBackgroundColor(Color.WHITE);
        bodyTextView.setText(label);
        bodyTextView.setGravity(Gravity.CENTER);
        bodyTextView.setPadding(5, 5, 5, 5);
        return bodyTextView;
    }

    // header standard TextView
    TextView headerTextView(String label) {

        TextView headerTextView = new TextView(this.context);
        headerTextView.setBackgroundColor(Color.WHITE);
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

        // if a TableRow has only one child
        if (tableRow.getChildCount() == 1) {

            View view = tableRow.getChildAt(0);
            TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();
            params.height = height - (params.bottomMargin + params.topMargin);

            return;
        }

        // if a TableRow has more than one child
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

    private void requestProductAPI(int offsetvalue1, int limit1) {


        String url = ConstsCore.web_url + "/v1/display/runningpromodetails/" + userId + "?offset=" + offsetvalue + "&limit=" + limit;
        Log.i(TAG,"URL   "+ url + " " + bearertoken);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, "Running promo : " + " " + response);
                        Log.i(TAG, "Sales View Pager response" + "" + response.length());

                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(RunningPromoDetail.this, "no product data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {
                                //Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    runningPromoListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                    promoList.add(runningPromoListDisplay);
                                    Log.e(TAG,"Promolist in limit");

                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestProductAPI(offsetvalue, limit);
                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    runningPromoListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                    promoList.add(runningPromoListDisplay);
                                    Log.e(TAG,"Promolist size"+promoList.size());

                                }

                                txtHeader.setText(data.getString("VM"));

                               addTableRowToTableA();
                                addTableRowToTableB();
                                resizeHeaderHeight();
                                getTableRowHeaderCellWidth();
                                generateTableC_AndTable_B();
                                resizeBodyTableRowHeight();
                                Reusable_Functions.hDialog();

                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "no data found in catch"+e.toString(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            Log.e(TAG, "catch...Error" +e.toString());
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RunningPromoDetail.this,RunningPromoActivity.class);
        startActivity(intent);
        finish();
    }
}

