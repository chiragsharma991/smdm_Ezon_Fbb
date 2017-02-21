package apsupportapp.aperotechnologies.com.designapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;


public class StyleActivity extends AppCompatActivity
//implements IWrapperCallBack {
{
    Button btnBarcode;
    //  private Wrapper m_wrapper = null;
    RelativeLayout imageBtnBack;
    TextView collection, style;
    ArrayAdapter<String> adapter1, adapter2;
    List<String> collectionList, list;
    ArrayList<String> arrayList, articleOptionList;
    String userId, bearertoken;
    View view;
    String collectionNM, optionName;
    RequestQueue queue;
    Context context;
    boolean flag = true;
    ArrayList<StyleDetailsBean> styleDetailsBeenList;
    StyleDetailsBean styleDetailsBean;
    MySingleton m_config;
    int offsetvalue = 0, limit = 100;
    int count = 0;
    int collectionoffset = 0, collectionlimit = 100, collectioncount = 0;
    SharedPreferences sharedPreferences;
    Button btnSubmit;
    EditText edtsearchCollection, edtsearchOption, edit_barcode;
    public static String selcollectionName = null, seloptionName = null;

    RelativeLayout stylemainlayout;
    LinearLayout collectionLayout, optionLayout;
    private ListView listCollection, listOption;
    ListAdapter collectionAdapter;
    ListAdapter1 optionAdapter;
    private String TAG = "StyleActivity";
    private static final String SOURCE_TAG = "com.motorolasolutions.emdk.datawedge.source";
    private static final String LABEL_TYPE_TAG = "com.motorolasolutions.emdk.datawedge.label_type";
    private static final String DATA_STRING_TAG = "com.motorolasolutions.emdk.datawedge.data_string";
    private static final String ACTION_SOFTSCANTRIGGER = "com.motorolasolutions.emdk.datawedge.api.ACTION_SOFTSCANTRIGGER";
    private static final String EXTRA_PARAM = "com.motorolasolutions.emdk.datawedge.api.EXTRA_PARAMETER";
    private static final String DWAPI_TOGGLE_SCANNING = "TOGGLE_SCANNING";
    private static String ourIntentAction = "com.motorolasolutions.emdk.sample.dwdemosample.RECVR";
    String barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_style);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        context = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
//        if (isAMobileModel()) {
//            Log.e("amobile device", "");
//          //  m_wrapper = new Wrapper(StyleActivity.this);
//
//            IntentFilter filter = new IntentFilter();
//            registerReceiver(m_brc, filter);
//        }

        context = this;
        m_config = MySingleton.getInstance(context);

        styleDetailsBeenList = new ArrayList<>();
        final Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();

        collectionList = new ArrayList<String>();
        arrayList = new ArrayList<String>();
        list = new ArrayList<>();
        articleOptionList = new ArrayList<>();

        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading collection data...");
            requestCollectionAPI(collectionoffset, collectionlimit);
        } else {

            Toast.makeText(StyleActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
        }

        stylemainlayout = (RelativeLayout) findViewById(R.id.stylemainlayout);
        stylemainlayout.setVisibility(View.VISIBLE);
        collectionLayout = (LinearLayout) findViewById(R.id.collectionLayout);
        optionLayout = (LinearLayout) findViewById(R.id.optionLayout);
        edtsearchCollection = (EditText) findViewById(R.id.searchCollection);
        edtsearchOption = (EditText) findViewById(R.id.searchOption);
        edit_barcode = (EditText) findViewById(R.id.editBarcode);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnBarcode = (Button) findViewById(R.id.btnBarcode);
        imageBtnBack = (RelativeLayout) findViewById(R.id.imageBtnBack);
        if (getIntent().getExtras() != null) {
            selcollectionName = getIntent().getExtras().getString("selCollectionname");
            seloptionName = getIntent().getExtras().getString("selOptionName");
        }
        Log.e("selcollectionName", " " + selcollectionName + " " + seloptionName);
        collection = (TextView) findViewById(R.id.searchablespinnerlibrary);
        collection.setText("Select Collection");
        listCollection = (ListView) findViewById(R.id.listCollection);
        collectionAdapter = new ListAdapter(arrayList, StyleActivity.this);
        //attach the adapter to the list
        listCollection.setAdapter(collectionAdapter);
        listCollection.setTextFilterEnabled(true);
        collectionAdapter.notifyDataSetChanged();
        style = (TextView) findViewById(R.id.searchablespinnerlibrary1);
        style.setText("Select Option");
        style.setEnabled(false);
        listOption = (ListView) findViewById(R.id.listOption);
        optionAdapter = new ListAdapter1(articleOptionList, StyleActivity.this);
        listOption.setAdapter(optionAdapter);
        listOption.setTextFilterEnabled(true);
        optionAdapter.notifyDataSetChanged();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (collection.getText().toString().trim().equals("Select Collection")) {
                    Toast.makeText(StyleActivity.this, "Please select Collection", Toast.LENGTH_LONG).show();

                } else if (style.getText().toString().trim().equals("Select Option")) {
                    Toast.makeText(StyleActivity.this, "Please select Option", Toast.LENGTH_LONG).show();

                } else {
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.hDialog();
                        Reusable_Functions.sDialog(context, "Loading  data...");
                        Log.e("select item", optionName);
                        requestStyleDetailsAPI(optionName, "optionname");
                    } else {
                        Toast.makeText(StyleActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        btnBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("scan clicked", "");
                if (isAMobileModel()) {

                    Intent intent_barcode = new Intent();
                    intent_barcode.setAction(ACTION_SOFTSCANTRIGGER);
                    intent_barcode.putExtra(EXTRA_PARAM, DWAPI_TOGGLE_SCANNING);
                    StyleActivity.this.sendBroadcast(intent_barcode);
                    edit_barcode.setText(" ");
                    barcode = " ";


                    android.os.Handler h = new android.os.Handler();
                    h.postDelayed(new Runnable() {
                        public void run() {

                            Intent i1 = getIntent();
                            Log.e("getIntent : ", "" + getIntent());
                            Log.e("barcode :", " " + i1 + "\ntxt :" + edit_barcode.getText().toString());
                            barcode = edit_barcode.getText().toString();
                            Toast.makeText(StyleActivity.this, "Barcode is : " + barcode, Toast.LENGTH_SHORT).show();
                            if (Reusable_Functions.chkStatus(StyleActivity.this)) {
                                Reusable_Functions.hDialog();
                                Reusable_Functions.sDialog(StyleActivity.this, "Loading  data...");
                                requestStyleDetailsAPI(barcode, "barcode");
                            } else {
                                Toast.makeText(StyleActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, 1500);
                    // tet commit



                    //   handleDecodeData(intent);

//                    if (m_wrapper != null && m_wrapper.IsOpen()) {
//                        m_wrapper.Scan();
//                    }
                } else if (!isAMobileModel()) {
                    Log.e("regular device", "");
                    scanBarcode(view);

                }
            }
        });

        imageBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selcollectionName = null;
                seloptionName = null;
                DashBoardActivity._collectionitems = new ArrayList();
                finish();

            }
        });

        edtsearchCollection.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                collectionAdapter.getFilter().filter(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                collectionAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                collectionAdapter.getFilter().filter(s);
            }
        });

        edtsearchCollection.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_NEXT) || (actionId == EditorInfo.IME_ACTION_GO)) {
                    InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputManager != null) {
                        inputManager.hideSoftInputFromWindow(edtsearchOption.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                return false;
            }
        });

        edtsearchOption.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                optionAdapter.getFilter().filter(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                optionAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                optionAdapter.getFilter().filter(s);
            }
        });


        edtsearchOption.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_NEXT) || (actionId == EditorInfo.IME_ACTION_GO)) {
                    InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputManager != null) {
                        inputManager.hideSoftInputFromWindow(edtsearchOption.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                return false;
            }
        });

        style.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtsearchOption.setText("");
                collectionLayout.setVisibility(View.GONE);
                optionLayout.setVisibility(View.VISIBLE);
            }

        });

        listOption.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(" ", " " + parent + " " + position + " " + view);

                optionName = (String) optionAdapter.getItem(position);
                style.setText(optionName.trim());
                Log.e("optionName ", " " + optionName);
                stylemainlayout.setVisibility(View.VISIBLE);
                collectionLayout.setVisibility(View.GONE);
                optionLayout.setVisibility(View.GONE);

                InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputManager != null) {
                    inputManager.hideSoftInputFromWindow(edtsearchOption.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        EditText et = (EditText) findViewById(R.id.editBarcode);


//        handleDecodeData(intent);
        Log.e("=======", "-------");
    }

    private void handleDecodeData(Intent i) {


    }


    @Override
    protected void onStart() {
        super.onStart();

//        if (m_wrapper != null) {
//            if (m_wrapper.Open()) {
//                m_wrapper.SetDispathBarCode(false);
//                m_wrapper.SetLightMode2D(Wrapper.LightMode2D.mix);
//                m_wrapper.SetTimeOut(30);
//            } else {
//                m_wrapper = null;
//            }
//        }
    }

    @Override
    protected void onDestroy() {
        if (isAMobileModel()) {
            //    unregisterReceiver(m_brc);
        }
//        if (m_wrapper != null) {
//            m_wrapper.Close();
//            m_wrapper = null;
//        }
        super.onDestroy();
    }

//    public void onNewIntent(Intent i) {
//
//        handleDecodeData(i);
//
//    }


//    @Override
//    public void onDataReady(String strData) {
//        byte[] bytes = strData.getBytes();
//        if (strData == null) {
//            Toast.makeText(this, "Barcode not scanned", Toast.LENGTH_LONG).show();
//        } else if (strData.equalsIgnoreCase("Unknown command : setLightMode2D")) {
//            Log.e("Do nothing", "");
//        } else if (strData.equalsIgnoreCase("Time Out")) {
//
//        } else {
//            Toast.makeText(this, "Barcode Scanned: " + strData, Toast.LENGTH_LONG).show();
//            if (Reusable_Functions.chkStatus(context)) {
//                Reusable_Functions.hDialog();
//                Reusable_Functions.sDialog(context, "Loading  data...");
//                requestStyleDetailsAPI(strData, "barcode");
//            } else {
//                Toast.makeText(StyleActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
//            }
//        }
//    }

    private BroadcastReceiver m_brc = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String s = intent.getStringExtra(ACTION_SOFTSCANTRIGGER);
                if (s != null) {
                    // m_text.setText(s);
                    Log.e("printing string s=", s);
                }
            }
        }
    };

//    @Override
//    public void onServiceConnected() {
//        // do something after connecting
//        int i = 0;
//    }
//
//    @Override
//    public void onServiceDisConnected() {
//        // do something after disconnecting
//        int i = 0;
//    }
//

    private boolean isAMobileModel() {
        Log.e("checking model", "");
        getDeviceInfo();
        Log.e("model is ", "" + Build.MODEL);
        return Build.MODEL.contains("TC75");
    }

    public void scanBarcode(View view) {

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Place a barcode inside the viewfinder rectangle to scan it");
        integrator.setOrientationLocked(true);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Barcode not scanned", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Barcode Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading  data...");
                    requestStyleDetailsAPI(result.getContents(), "barcode");

                } else {
                    Toast.makeText(StyleActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if(result != null) {
//            if(result.getContents() == null)
//            {
//                Toast.makeText(this, "Barcode not scanned", Toast.LENGTH_LONG).show();
//            } else
//            {
//                Toast.makeText(this, "Barcode Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
//                if (Reusable_Functions.chkStatus(context)) {
//                    Reusable_Functions.sDialog(context, "Loading  data...");
//                    requestStyleDetailsBarcodeAPI(result.getContents());
//
//                } else
//                {
//                    // Reusable_Functions.hDialog();
//                    Toast.makeText(StyleActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
//                }
//
//
//            }
//        } else {
//            // This is important, otherwise the result will not be passed to the fragment
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }

//    private void requestStyleDetailsBarcodeAPI(String contents)
//    {
//
//
//            String url=ConstsCore.web_url + "/v1/display/productdetails/"+userId+"?eanNumber="+contents;
//
//            Log.i("URL barcode  style  ", url);
//
//            final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
//                    new Response.Listener<JSONArray>()
//                    {
//                        @Override
//                        public void onResponse(JSONArray response)
//                        {
//                            Log.i("Style details :   ", response.toString());
//                            try
//                            {
//
//                                if (response.equals(null) || response ==null||response.length()==0)
//                                {
//                                    Log.e("requestDetailBarcodeAPI","hiiiiii");
//                                    Reusable_Functions.hDialog();
//                                    Toast.makeText(StyleActivity.this,"No data found",Toast.LENGTH_LONG).show();
//
//                                }else {
//
//                                    Reusable_Functions.hDialog();
//
//                                    // JSONObject mainObject = new JSONObject(response);
//                                    for (int i = 0; i < response.length(); i++) {
//                                        JSONObject styleDetails = response.getJSONObject(i);
//                                        String productName = styleDetails.getString("productName");
//                                        String collectionName = styleDetails.getString("collectionName");
//                                        String productFabricDesc = styleDetails.getString("productFabricDesc");
//                                        String productFitDesc = styleDetails.getString("productFitDesc");
//                                        String productFinishDesc = styleDetails.getString("productFinishDesc");
//                                        String seasonName = styleDetails.getString("seasonName");
//                                        int fwdWeekCover = styleDetails.getInt("fwdWeekCover");
//
//                                        int twSaleTotQty = styleDetails.getInt("twSaleTotQty");
//                                        int lwSaleTotQty = styleDetails.getInt("lwSaleTotQty");
//                                        int ytdSaleTotQty = styleDetails.getInt("ytdSaleTotQty");
//
//                                        int stkOnhandQty = styleDetails.getInt("stkOnhandQty");
//                                        int stkGitQty = styleDetails.getInt("stkGitQty");
//                                        int targetStock = styleDetails.getInt("targetStock");
//
//                                        int unitGrossPrice = styleDetails.getInt("unitGrossPrice");
//                                        double sellThruUnitsRcpt = styleDetails.getDouble("sellThruUnitsRcpt");
//                                        int ros = styleDetails.getInt("ros");
//
//                                        //   int usp = styleDetails.getInt("usp");
//
//                                        styleDetailsBean = new StyleDetailsBean();
//
//                                        styleDetailsBean.setProductName(productName);
//                                        styleDetailsBean.setCollectionName(collectionName);
//                                        styleDetailsBean.setProductFabricDesc(productFabricDesc);
//                                        styleDetailsBean.setProductFitDesc(productFitDesc);
//                                        styleDetailsBean.setProductFinishDesc(productFinishDesc);
//                                        styleDetailsBean.setSeasonName(seasonName);
//
//                                        styleDetailsBean.setFirstReceiptDate(firstReceiptDate);
//                                        styleDetailsBean.setLastReceiptDate(lastReceiptDate);
//                                        styleDetailsBean.setFwdWeekCover(fwdWeekCover);
//
//                                        styleDetailsBean.setTwSaleTotQty(twSaleTotQty);
//                                        styleDetailsBean.setLwSaleTotQty(lwSaleTotQty);
//                                        styleDetailsBean.setYtdSaleTotQty(ytdSaleTotQty);
//
//                                        styleDetailsBean.setUnitGrossPrice(unitGrossPrice);
//                                        styleDetailsBean.setSellThruUnitsRcpt(sellThruUnitsRcpt);
//                                        styleDetailsBean.setRos(ros);
//
//                                        //  styleDetailsBean.setUsp(usp);
//                                        Intent intent = new Intent(StyleActivity.this, SwitchingTabActivity.class);
//                                        intent.putExtra("styleDetailsBean", styleDetailsBean);
//                                        //intent.putExtra("userId",m_config.userId);
//                                        startActivity(intent);
//
//                                        Log.e("Style details:", productName + "   " + collectionName + "  " + productFabricDesc + "  " + productFitDesc + "  " + productFinishDesc + "  " + seasonName);
//                                        // Log.e("row 1:", firstReceiptDate + "   " + lastReceiptDate + "  " + fwdWeekCover);
//                                        Log.e("row 2:", twSaleTotQty + "   " + lwSaleTotQty + "  " + ytdSaleTotQty);
//                                        Log.e("row 3:", stkOnhandQty + "   " + stkGitQty + "  " + targetStock);
//                                        Log.e("row4:", unitGrossPrice + "   " + sellThruUnitsRcpt + "  " + ros);
//                                        //  Log.e("benefit :", "" + usp);
//
//                                    }
//
//
////                                Intent intent = new Intent(StyleActivity.this, SwitchingTabActivity.class);
////                                intent.putExtra("styleDetailsBean", styleDetailsBean);
////                                //intent.putExtra("userId",m_config.userId);
////                                startActivity(intent);
//
//                                }
//                            }
//                            catch(Exception e)
//                            {
//                                Log.e("Exception e",e.toString() +"");
//                                e.printStackTrace();
//                            }
//                        }
//                    },
//                    new Response.ErrorListener()
//                    {
//                        @Override
//                        public void onErrorResponse(VolleyError error)
//                        {
//                            Reusable_Functions.hDialog();
//                            // Toast.makeText(LoginActivity.this,"Invalid User",Toast.LENGTH_LONG).show();
//                            error.printStackTrace();
//                        }
//                    }
//
//            ){
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError
//                {
//                    Map<String, String> params = new HashMap<>();
//                    params.put("Content-Type", "application/json");
//                    params.put("Authorization", "Bearer "+bearertoken);
//                    return params;
//                }
//            };
//            int socketTimeout = 60000;//5 seconds
//            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//            postRequest.setRetryPolicy(policy);
//            queue.add(postRequest);
//
//    }


    public String getDeviceInfo() {
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

    private void requestStyleDetailsAPI(String content, String check) {
        String url = " ";
        if (check.equals("optionname")) {
            url = ConstsCore.web_url + "/v1/display/productdetails/" + userId + "?articleOption=" + content.replaceAll(" ", "%20").replaceAll("&", "%26");
        } else if (check.equals("barcode")) {
            url = ConstsCore.web_url + "/v1/display/productdetails/" + userId + "?eanNumber=" + content;
        }
        Log.e(TAG, "requestStyleDetailsAPI  " + url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, " requestStyleDetailsAPI :   " + response.toString());
                        try {

                            if (response.equals(null) || response == null || response.length() == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(StyleActivity.this, "No data found", Toast.LENGTH_LONG).show();
                            } else {
                                Reusable_Functions.hDialog();
                                JSONObject styleDetails = response.getJSONObject(0);
                                String storeCode = styleDetails.getString("storeCode");
                                String storeDesc = styleDetails.getString("storeDesc");
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
                                styleDetailsBean = new StyleDetailsBean();
                                styleDetailsBean.setStoreCode(storeCode);
                                styleDetailsBean.setStoreDesc(storeDesc);
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
                                styleDetailsBean.setPromoFlag(promoFlg);
                                styleDetailsBean.setKeyProductFlg(keyProductFlg);
                                styleDetailsBean.setProductImageURL(productImageURL);
                                Log.e(TAG, "intent calling: ");
                                Intent intent = new Intent(StyleActivity.this, SwitchingTabActivity.class);
                                intent.putExtra("articleCode", articleCode);
                                intent.putExtra("articleOption", articleOption);
                                intent.putExtra("styleDetailsBean", styleDetailsBean);
                                intent.putExtra("selCollectionname", collectionNM);
                                intent.putExtra("selOptionName", optionName);
                                startActivity(intent);
                                finish();
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
                        Toast.makeText(StyleActivity.this, "Network connectivity fail", Toast.LENGTH_LONG).show();
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

    private void requestCollectionAPI(int offsetvalue1, final int limit1) {
        String url = ConstsCore.web_url + "/v1/display/collections/" + userId + "?offset=" + collectionoffset + "&limit=" + collectionlimit;
        Log.e(TAG, "requestCollectionAPI   " + url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, "requestCollectionAPI   " + response.toString());
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && collectioncount == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(StyleActivity.this, "No collection data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == collectionlimit) {
                                Log.i("limit eq resp length", "" + response.length());
                                Log.e("offsetvalue", "" + collectionoffset);
                                Log.e("limit", "" + collectionlimit);
                                Log.e("count", "" + collectioncount);
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject collectionName = response.getJSONObject(i);
                                    collectionNM = collectionName.getString("collectionName");
                                    Log.e("collectionName  :", collectionName.getString("collectionName"));
                                    arrayList.add(collectionName.getString("collectionName"));
                                    Log.e("size in limit :", "" + arrayList.size());
                                }
                                collectionoffset = (collectionlimit * collectioncount) + collectionlimit;
                                collectioncount++;
                                requestCollectionAPI(collectionoffset, collectionlimit);
                            } else if (response.length() < collectionlimit) {
                                Reusable_Functions.hDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject collectionName = response.getJSONObject(i);
                                    collectionNM = collectionName.getString("collectionName");
                                    arrayList.add(collectionName.getString("collectionName"));
                                    Log.e("size  :", "" + arrayList.size());
                                }
                                Collections.sort(arrayList);
                                arrayList.add(0, "Select Collection");
                                Log.e("selcollectionName", "==== " + selcollectionName);
                                if (selcollectionName == null || selcollectionName.equals(null)) {
                                    collection.setText("Select Collection");
                                } else {
                                    if (arrayList.contains(selcollectionName)) {
                                        collectionNM = selcollectionName;
                                        optionName = seloptionName;
                                        collection.setText(selcollectionName);
                                        style.setText(seloptionName);
                                        style.setEnabled(true);
                                        articleOptionList.addAll(DashBoardActivity._collectionitems);
                                    } else {
                                        collection.setText("Select Collection");
                                    }
                                }
                                collection.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        edtsearchCollection.setText("");
                                        collectionLayout.setVisibility(View.VISIBLE);
                                        optionLayout.setVisibility(View.GONE);
                                    }
                                });
                                listCollection.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        //Log.e(" ------- "," "+parent +" "+position+" "+view);
                                        collectionNM = (String) collectionAdapter.getItem(position);
                                        collection.setText(collectionNM.trim());

                                        if (selcollectionName == null || selcollectionName.equals(null)) {

                                        } else {
                                            selcollectionName = null;
                                            seloptionName = null;
                                        }

                                        Log.e("collectionNM", " " + collectionNM);
                                        collectionLayout.setVisibility(View.GONE);
                                        optionLayout.setVisibility(View.GONE);

                                        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                        if (inputManager != null) {
                                            inputManager.hideSoftInputFromWindow(edtsearchCollection.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                        }
                                        if (collectionNM.equalsIgnoreCase("Select Collection")) {
                                            //Toast.makeText(StyleActivity.this,"Please select Collection",Toast.LENGTH_LONG).show();
                                        } else {

                                            if (Reusable_Functions.chkStatus(context)) {
                                                Reusable_Functions.sDialog(context, "Loading options data...");
                                                Log.e("select item", collectionNM);
                                                offsetvalue = 0;
                                                limit = 100;
                                                count = 0;
                                                articleOptionList.clear();
                                                Log.e("articleOptionList---", " " + articleOptionList.size());
                                                requestArticleOptionsAPI(collectionNM, offsetvalue, limit);

                                            } else {

                                                Toast.makeText(StyleActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }
                                });
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

    private void requestArticleOptionsAPI(final String collectionNM, int offsetvalue1, final int limit1) {
        String url;
        url = ConstsCore.web_url + "/v1/display/collectionoptions/" + userId + "?collectionName=" + collectionNM.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e(TAG, "requestArticleOptionsAPI   " + url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, "requestArticleOptionsAPI Response " + response.toString() + " " + articleOptionList.size());
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                articleOptionList.add(0, "Select Option");
                                style.setEnabled(false);
                                Reusable_Functions.hDialog();
                                Toast.makeText(StyleActivity.this, "No options data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonResponse = response.getJSONObject(i);
                                    String articleOptions = jsonResponse.getString("articleOptions");
                                    articleOptionList.add(articleOptions);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestArticleOptionsAPI(collectionNM, offsetvalue, limit);
                            } else if (response.length() < limit) {
                                Log.i(" Response length", "" + response.length());
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonResponse = response.getJSONObject(i);
                                    String articleOptions = jsonResponse.getString("articleOptions");
                                    //   Log.e("articleOptions  :", "   " + articleOptions);
                                    articleOptionList.add(articleOptions);
                                }
                                Reusable_Functions.hDialog();
                                Log.e("articleOptionList", articleOptionList.size() + "");
                                Collections.sort(articleOptionList);
                                articleOptionList.add(0, "Select Option");
                                style.setEnabled(true);
                                DashBoardActivity._collectionitems = new ArrayList();
                                DashBoardActivity._collectionitems.addAll(articleOptionList);
                                if (seloptionName == null || seloptionName.equals(null)) {
                                    style.setText("Select Option");
                                } else {
                                }
                                optionAdapter.notifyDataSetChanged();
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

    @Override
    public void onBackPressed() {
        if (optionLayout.getVisibility() == View.VISIBLE) {
            optionLayout.setVisibility(View.GONE);
            collectionLayout.setVisibility(View.GONE);
            stylemainlayout.setVisibility(View.VISIBLE);


            InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(edtsearchOption.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } else if (collectionLayout.getVisibility() == View.VISIBLE) {
            optionLayout.setVisibility(View.GONE);
            collectionLayout.setVisibility(View.GONE);
            stylemainlayout.setVisibility(View.VISIBLE);

            InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(edtsearchCollection.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } else {
            selcollectionName = null;
            seloptionName = null;
            DashBoardActivity._collectionitems = new ArrayList();
         /* Intent intent = new Intent(StyleActivity.this,DashBoardActivity.class);
            startActivity(intent);*/
            finish();
        }
    }
}
