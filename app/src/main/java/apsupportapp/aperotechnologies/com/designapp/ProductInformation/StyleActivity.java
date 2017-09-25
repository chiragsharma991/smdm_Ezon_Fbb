package apsupportapp.aperotechnologies.com.designapp.ProductInformation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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
import com.cipherlab.barcode.GeneralString;
import com.cipherlab.barcode.ReaderManager;
import com.cipherlab.barcode.decoder.BcReaderType;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.AnyOrientationCaptureActivity;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.DashboardSnap.SnapDashboardActivity;
import apsupportapp.aperotechnologies.com.designapp.ListAdapter;
import apsupportapp.aperotechnologies.com.designapp.ListAdapter1;
import apsupportapp.aperotechnologies.com.designapp.MySingleton;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;


public class StyleActivity extends AppCompatActivity
{
    Button btnBarcode;
    RelativeLayout imageBtnBack;
    TextView collection, style,txt_store;
    List<String> collectionList, list;
    ArrayList<String> arrayList, articleOptionList,storeList;
    static ArrayList<String> newcollectionList;
    String userId, bearertoken;
    View view;
    String collectionNM, optionName, from,store_name;
    RequestQueue queue;
    Context context;
    ArrayList<StyleDetailsBean> styleDetailsBeenList;
    StyleDetailsBean styleDetailsBean;
    MySingleton m_config;
    int offsetvalue = 0, limit = 100;
    int count = 0;
    int collectionoffset = 0, collectionlimit = 100, collectioncount = 0;
    SharedPreferences sharedPreferences;
    Button btnSubmit;
    EditText edtsearchCollection, edtsearchOption, edit_barcode,edtsearchStore;
    public static String selcollectionName = null, seloptionName = null, selStoreName = null;
    LinearLayout stylemainlayout;
    LinearLayout collectionLayout, optionLayout,storeLayout;
    private ListView listCollection, listOption,listStore;
    ListAdapter collectionAdapter,storeAdapter;
    ListAdapter1 optionAdapter;

    String collect_name = "",geoLevel2Code,lobId,store_nm;

    private ReaderManager mReaderManager;
    private IntentFilter filter;

    String barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_product_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        context = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLevel2Code = sharedPreferences.getString("concept", "");
        lobId = sharedPreferences.getString("lobid", "");
        context = this;
        m_config = MySingleton.getInstance(context);
        styleDetailsBeenList = new ArrayList<>();
        final Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        storeList =new ArrayList<String>();
        collectionList = new ArrayList<String>();
        arrayList = new ArrayList<String>();

        list = new ArrayList<>();
//        seloptionName = null;
//        selStoreName = null;
//        selcollectionName = null;
        articleOptionList = new ArrayList<>();
        stylemainlayout = (LinearLayout) findViewById(R.id.stylemainlayout);
        stylemainlayout.setVisibility(View.VISIBLE);
        collectionLayout = (LinearLayout) findViewById(R.id.collectionLayout);
        optionLayout = (LinearLayout) findViewById(R.id.optionLayout);
        edtsearchStore = (EditText)findViewById(R.id.searchStore);
        edtsearchCollection = (EditText) findViewById(R.id.searchCollection);
        edtsearchOption = (EditText) findViewById(R.id.searchOption);

//        edit_barcode = (EditText) findViewById(R.id.editBarcode);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        String submit = "Submit";
        btnSubmit.setText(submit);
        btnBarcode = (Button) findViewById(R.id.btnBarcode);
        imageBtnBack = (RelativeLayout) findViewById(R.id.imageBtnBack);
        if (getIntent().getExtras() != null)
        {
           // selStoreName = getIntent().getExtras().getString("selStoreName");
            selcollectionName = getIntent().getExtras().getString("selCollectionname");
            seloptionName = getIntent().getExtras().getString("selOptionName");
       }
        txt_store = (TextView)findViewById(R.id.searchablespinnerlibraryStore);
        txt_store.setText("Select Store");
        storeLayout = (LinearLayout)findViewById(R.id.storeLayout);
        listStore = (ListView)findViewById(R.id.listStore);
        collection = (TextView) findViewById(R.id.searchablespinnerlibrary);
        collection.setText("Select Collection");
        listCollection = (ListView) findViewById(R.id.listCollection);
        storeAdapter = new ListAdapter(storeList,StyleActivity.this);
        listStore.setAdapter(storeAdapter);
        collectionAdapter = new ListAdapter(arrayList, StyleActivity.this);
        //attach the adapter to the list
        listCollection.setAdapter(collectionAdapter);
        listCollection.setTextFilterEnabled(true);
        collectionAdapter.notifyDataSetChanged();
        style = (TextView) findViewById(R.id.searchablespinnerlibrary1);
        style.setText("Select Option");
        style.setEnabled(false);
        if (Reusable_Functions.chkStatus(context))
        {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading store data...");
            collectionoffset = 0;
            collectionlimit = 100;
           requestProductStoreSelection();

        }
        else
        {
            Toast.makeText(StyleActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
        }
        listOption = (ListView) findViewById(R.id.listOption);
        optionAdapter = new ListAdapter1(articleOptionList, StyleActivity.this);
        listOption.setAdapter(optionAdapter);
        listOption.setTextFilterEnabled(true);
        optionAdapter.notifyDataSetChanged();
        txt_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                edtsearchStore.setText("");
                storeLayout.setVisibility(View.VISIBLE);
                collectionLayout.setVisibility(View.GONE);
                optionLayout.setVisibility(View.GONE);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (txt_store.getText().toString().trim().equals("Select Store"))
                {
                    Toast.makeText(StyleActivity.this, "Please select Store", Toast.LENGTH_LONG).show();

                }
                else if (collection.getText().toString().trim().equals("Select Collection")) {
                    Toast.makeText(StyleActivity.this, "Please select Collection", Toast.LENGTH_LONG).show();

                } else if (style.getText().toString().trim().equals("Select Option")) {
                    Toast.makeText(StyleActivity.this, "Please select Option", Toast.LENGTH_LONG).show();

                } else {
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.hDialog();
                        store_name = store_name.substring(0,4);
                        Reusable_Functions.sDialog(context, "Loading  data...");
                        requestStyleDetailsAPI(optionName, "optionname",store_name);
                    } else {
                        Toast.makeText(StyleActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btnBarcode.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (isAMobileModel()) {

                    ExeSampleCode();
                    mReaderManager = ReaderManager.InitInstance(StyleActivity.this);
                    filter = new IntentFilter();
                    filter.addAction(GeneralString.Intent_SOFTTRIGGER_DATA);
                    filter.addAction(GeneralString.Intent_PASS_TO_APP);
                    filter.addAction(GeneralString.Intent_READERSERVICE_CONNECTED);
                    registerReceiver(myDataReceiver, filter);

                } else if (!isAMobileModel()) {
                    scanBarcode(view);
                }
            }
        });
        imageBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        edtsearchCollection.addTextChangedListener(new TextWatcher()
        {
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

        edtsearchCollection.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
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
        collection.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Log.e("", "onClick: ");
                edtsearchCollection.setText("");
                storeLayout.setVisibility(View.GONE);
                collectionLayout.setVisibility(View.VISIBLE);
                optionLayout.setVisibility(View.GONE);
            }
        });
        style.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                edtsearchOption.setText("");
                storeLayout.setVisibility(View.GONE);
                collectionLayout.setVisibility(View.GONE);
                optionLayout.setVisibility(View.VISIBLE);
            }

        });
        listOption.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                optionName = (String) optionAdapter.getItem(position);
                //seloptionName = optionName;
                Log.e("", "onItemClick: "+seloptionName);
                style.setText(optionName);
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


    private void ExeSampleCode()
    {
        if (mReaderManager != null)
        {
            Log.e("onClick: ", "------");
            BcReaderType myReaderType = mReaderManager.GetReaderType();
          //  edit_barcode.setText(myReaderType.toString());
        }
        if(mReaderManager != null)
        {
            // Enable/Disable barcode reader service
            com.cipherlab.barcode.decoder.ClResult clRet = mReaderManager.SetActive(false);
            boolean bRet = mReaderManager.GetActive();
            clRet = mReaderManager.SetActive(true);
            bRet = mReaderManager.GetActive();

        }
        if(mReaderManager != null)
        {
        //software trigger
            Thread sThread = new Thread(new Runnable()
            {
                @Override
                public void run() {
                    mReaderManager.SoftScanTrigger();
                }
            });
            sThread.setPriority(Thread.MAX_PRIORITY);
            sThread.start();
        }
    }

    private void TimeUP()
    {
        if (Reusable_Functions.chkStatus(StyleActivity.this)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(StyleActivity.this, "Loading data...");
            requestStyleDetailsAPI(barcode, "barcode", store_name);
        }
        else
        {
            Toast.makeText(StyleActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isAMobileModel()
    {
        getDeviceInfo();
        return Build.MODEL.contains("RS31");
    }

    public void scanBarcode(View view)
    {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Place a barcode inside the viewfinder rectangle to scan it");
        integrator.setOrientationLocked(true);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null)
        {
            if (result.getContents() == null) {
                Toast.makeText(this, "Barcode not scanned", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this, "Barcode Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading  data...");
                    requestStyleDetailsAPI(result.getContents(), "barcode", store_name);

                }
                else
                {
                    Toast.makeText(StyleActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
                }
            }
        }
        else
        {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /// create a BroadcastReceiver for receiving intents from barcode reader service
    private final BroadcastReceiver myDataReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Software trigger must receive this intent message
            if (intent.getAction().equals(GeneralString.Intent_SOFTTRIGGER_DATA)) {


                    barcode = intent.getStringExtra(GeneralString.BcReaderData);
                    Log.e("onReceive: ", " " + barcode);
                    android.os.Handler h = new android.os.Handler();
                    h.postDelayed(new Runnable() {
                        public void run() {
                            Log.e("run: ", "" + barcode);
                            if (!barcode.equals(" "))
                            {
                                Toast.makeText(StyleActivity.this, "Barcode scanned : " + barcode, Toast.LENGTH_SHORT).show();
                                TimeUP();
                            } else {
                                Log.e("come", "here");
                                View view = findViewById(android.R.id.content);
                                Snackbar.make(view, "No barcode found. Please try again.", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    }, 1500);
            }
       }
    };


    public String getDeviceInfo()
    {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer))
        {
            return capitalize(model);
        }
        else
        {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s)
    {
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

    private void requestStyleDetailsAPI(String content, String check, final String store_name)
    {
        String url = " ";
        if (check.equals("optionname"))
        {
            url = ConstsCore.web_url + "/v1/display/productdetailsNew/" + userId + "?articleOption=" + content.replaceAll(" ", "%20").replaceAll("&", "%26")+"&geoLevel2Code="+geoLevel2Code + "&lobId="+lobId +"&storeCode="+store_name;
            Log.e("", "requestCollectionAPI: "+url);
        }
        else if (check.equals("barcode"))
        {
            url = ConstsCore.web_url + "/v1/display/productdetails/" + userId + "?eanNumber=" + content;
        }

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.equals("") || response == null || response.length() == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(StyleActivity.this, "No data found", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
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
                                styleDetailsBean.setPromoFlg(promoFlg);
                                styleDetailsBean.setKeyProductFlg(keyProductFlg);
                                styleDetailsBean.setProductImageURL(productImageURL);
                                Intent intent = new Intent(StyleActivity.this, SwitchingTabActivity.class);
                                intent.putExtra("checkFrom", "styleActivity");
                                intent.putExtra("articleCode", articleCode);
                                intent.putExtra("articleOption", articleOption);
                                intent.putExtra("styleDetailsBean", styleDetailsBean);
                                intent.putExtra("selCollectionname", collectionNM);
                                intent.putExtra("selOptionName", optionName);
                              //  intent.putExtra("selStoreName",store_name);
                                startActivity(intent);
                                finish();
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
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


    private void requestProductStoreSelection()
    {
        String url = ConstsCore.web_url + "/v1/display/storeselection/" + userId + "?offset=" + collectionoffset + "&limit=" + collectionlimit+"&geoLevel2Code="+geoLevel2Code + "&lobId="+lobId;
        Log.e("", "requestProductStoreSelection: "+url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && collectioncount == 0)
                            {
                                Reusable_Functions.hDialog();
                                Toast.makeText(StyleActivity.this, "No store data found", Toast.LENGTH_LONG).show();
                            }
                            else if (response.length() == collectionlimit)
                            {
                                for (int i = 0; i < response.length(); i++)
                                {
                                    JSONObject storeName = response.getJSONObject(i);
                                    store_name = storeName.getString("storeCode");
                                    storeList.add(store_name);

                                }
                                collectionoffset = (collectionlimit * collectioncount) + collectionlimit;
                                collectioncount++;
                                requestProductStoreSelection();
                            }
                            else if (response.length() < collectionlimit)
                            {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject storeName = response.getJSONObject(i);
                                    store_name = storeName.getString("storeCode");
                                    storeList.add(store_name);
                                }
                            }
//                            Collections.sort(storeList);
                            storeList.add(0, "Select Store");
                            storeAdapter.notifyDataSetChanged();
                            Reusable_Functions.hDialog();

                            listStore.setOnItemClickListener(new AdapterView.OnItemClickListener()
                            {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                                {
                                    store_name = (String) storeAdapter.getItem(position);
                                    selStoreName = store_name;
                                    txt_store.setText(store_name);
                                    Log.e("store_name ", " "+store_name);


                                        if (!store_name.equals(store_nm))
                                        {
                                            collection.setText("Select Collection");
                                        }
                                        else
                                        {
                                            collection.setText("Select Collection");
                                            style.setText("Select Option");
                                            collection.setEnabled(true);
                                            style.setEnabled(true);
                                        }

                                    storeLayout.setVisibility(View.GONE);
                                    collectionLayout.setVisibility(View.GONE);
                                    InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    if (inputManager != null) {
                                        inputManager.hideSoftInputFromWindow(edtsearchStore.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                    }
                                    if (store_name.equalsIgnoreCase("Select Store"))
                                    {
                                        Log.e(" come ", "here");
                                        //  collectionNM = selcollectionName;
                                    }
                                    else
                                    {
                                        if (Reusable_Functions.chkStatus(context)) {
                                            Reusable_Functions.sDialog(context, "Loading collection data...");
                                            offsetvalue = 0;
                                            collectionlimit = 100;
                                            count = 0;
                                            collectionList.clear();
                                            store_name = store_name.substring(0,4);
                                            requestCollectionAPI(store_name);
                                        }
                                        else
                                        {
                                            Toast.makeText(StyleActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            });
                            if (selStoreName == null || selStoreName.equals(""))
                            {
                                txt_store.setText("Select Store");

                            }
                            else
                            {

                                if (storeList.contains(selStoreName))
                                {
                                    Log.e("come", "onResponse: " );
                                    store_name = selStoreName;
                                    store_nm = txt_store.getText().toString();
                                    txt_store.setText(selStoreName);
                                    collection.setText(selcollectionName);
                                    style.setText(seloptionName);
                                    arrayList.addAll(newcollectionList);
                                    articleOptionList.addAll(SnapDashboardActivity._collectionitems);
                                    style.setEnabled(true);
                                    collection.setEnabled(true);

                                }
                                else
                                {
                                    txt_store.setText("Select Store");
                                    Log.e("Collection Text in else of else: ", " ");
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            Reusable_Functions.hDialog();


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


    private void requestCollectionAPI(final String storeName)
    {
        String url = ConstsCore.web_url + "/v1/display/collectionsNew/" + userId + "?offset=" + collectionoffset + "&limit=" + collectionlimit+"&geoLevel2Code="+geoLevel2Code + "&lobId="+lobId +"&storeCode="+storeName;
        Log.e("", "requestCollectionAPI: "+url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.equals("") || response == null || response.length() == 0 && collectioncount == 0)
                            {
                                Reusable_Functions.hDialog();
                                Toast.makeText(StyleActivity.this, "No collection data found", Toast.LENGTH_LONG).show();
                            }
                            else if (response.length() == collectionlimit)
                            {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject collectionName = response.getJSONObject(i);
                                    collectionNM = collectionName.getString("collectionName");
                                    arrayList.add(collectionNM);
                                }
                                collectionoffset = (collectionlimit * collectioncount) + collectionlimit;
                                collectioncount++;
                                requestCollectionAPI(storeName);
                            }
                            else if (response.length() < collectionlimit)
                            {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject collectionName = response.getJSONObject(i);
                                    collectionNM = collectionName.getString("collectionName");
                                    arrayList.add(collectionNM);
                                }
                            }
                            Collections.sort(arrayList);
                            arrayList.add(0, "Select Collection");
                            collectionAdapter.notifyDataSetChanged();
                            Reusable_Functions.hDialog();
                            newcollectionList = new ArrayList();
                           newcollectionList.addAll(arrayList);
                            listCollection.setOnItemClickListener(new AdapterView.OnItemClickListener()
                            {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                                {
                                    collectionNM = (String) collectionAdapter.getItem(position);
                                    selcollectionName = collectionNM;
                                    collection.setText(selcollectionName);
                                    Log.e("collect_name ", " "+collect_name);

                                    if(!collect_name.equals(""))
                                    {
                                        if (!collectionNM.equals(collect_name))
                                        {
                                            style.setText("Select Option");
                                        }
                                        else
                                        {
                                            style.setText(seloptionName);
                                            style.setEnabled(true);
                                        }
                                    }
                                    collectionLayout.setVisibility(View.GONE);
                                    optionLayout.setVisibility(View.GONE);
                                    InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    if (inputManager != null)
                                    {
                                        inputManager.hideSoftInputFromWindow(edtsearchCollection.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                    }
                                    if (collectionNM.equalsIgnoreCase("Select Collection"))
                                    {
                                        Log.e(" come ", "here");
                                      //  collectionNM = selcollectionName;
                                    }
                                    else
                                    {
                                        if (Reusable_Functions.chkStatus(context))
                                        {
                                            Reusable_Functions.sDialog(context, "Loading options data...");
                                            offsetvalue = 0;
                                            limit = 100;
                                            count = 0;
                                            store_name = store_name.substring(0,4);
                                            articleOptionList.clear();
                                            requestArticleOptionsAPI(collectionNM,store_name);
                                        }
                                        else
                                        {
                                            Toast.makeText(StyleActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            });
                            Log.e("selcollectionName ", " "+selcollectionName);
                            if (selcollectionName == null || selcollectionName.equals(""))
                            {
                                collection.setText("Select Collection");
                                Log.e("Collection Text in if : first", " ");
                            }
                            else  if (storeList.contains(selStoreName))
                            {
                                Log.e("come", "onResponse:----1 " );
                                store_name = selStoreName;
                                store_nm = txt_store.getText().toString();
                                txt_store.setText(selStoreName);
                                collection.setText("Select Collection");
                                style.setText("Select Option");
                                style.setEnabled(true);

                            }
                            else
                            {
                                Log.e("selcollectionNm: ", "" + selcollectionName);
                                Log.e("here in else : ", " ");

                                if (arrayList.contains(selcollectionName))
                                {
                                    Log.e("Collection Text in else : ", " ");
                                    store_name = selStoreName;
                                    collectionNM = selcollectionName;
                                    collect_name = collection.getText().toString();
                                    optionName = seloptionName;
                                    collection.setText(selcollectionName);
                                    style.setText(seloptionName);
                                    style.setEnabled(true);
                                    articleOptionList.addAll(SnapDashboardActivity._collectionitems);
                                }
                                else
                                {
                                    collection.setText("Select Collection");
                                    Log.e("Collection Text in else of else: ", " ");
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            Reusable_Functions.hDialog();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Reusable_Functions.hDialog();
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
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

    private void requestArticleOptionsAPI(final String collectionNM, final String store_name)
    {
        String url;
        url = ConstsCore.web_url + "/v1/display/collectionoptionsNew/" + userId + "?collectionName=" + collectionNM.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset=" + offsetvalue + "&limit=" + limit+ "&geoLevel2Code="+geoLevel2Code + "&lobId="+lobId +"&storeCode="+store_name;

        Log.e("url ", "" + url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try
                        {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0) {
                                articleOptionList.add(0, "Select Option");
                                style.setEnabled(false);
                                Reusable_Functions.hDialog();
                                Toast.makeText(StyleActivity.this, "No options data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit)
                            {
                                for (int i = 0; i < response.length(); i++)
                                {
                                    JSONObject jsonResponse = response.getJSONObject(i);
                                    String collectionNames = jsonResponse.getString("collectionNames");
                                    String articleOptions = jsonResponse.getString("articleOptions");
                                    articleOptionList.add(articleOptions);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestArticleOptionsAPI(collectionNM, store_name);
                            }
                            else if (response.length() < limit)
                            {
                                for (int i = 0; i < response.length(); i++)
                                {
                                    JSONObject jsonResponse = response.getJSONObject(i);
                                    String collectionNames = jsonResponse.getString("collectionNames");
                                    String articleOptions = jsonResponse.getString("articleOptions");
                                    articleOptionList.add(articleOptions);
                                }
                            }
                            Collections.sort(articleOptionList);
                            articleOptionList.add(0, "Select Option");
                            style.setEnabled(true);
                            SnapDashboardActivity._collectionitems = new ArrayList();
                            SnapDashboardActivity._collectionitems.addAll(articleOptionList);
                            Log.e("seloptionName ", " "+seloptionName);

                            if (seloptionName == null || seloptionName.equals(""))
                            {
                                style.setText("Select Option");
                            }
                            else
                            {
                                Log.e("seloptionName :", "" + seloptionName);
                                style.setText(seloptionName);
                            }
//                            if(!collect_name.equals(""))
//                            {
//                                if (!collectionNM.equals(collect_name)) {
//                                    style.setText("Select Option");
//                                } else {
//                                    style.setText(seloptionName);
//                                    style.setEnabled(true);
//                                }
//                            }
                            optionAdapter.notifyDataSetChanged();
                            Reusable_Functions.hDialog();

                        }
                        catch (Exception e)
                        {
                            Reusable_Functions.hDialog();
                            Log.e("catch log", "" + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
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
    public void onBackPressed()
    {
        if (optionLayout.getVisibility() == View.VISIBLE)
        {
            storeLayout.setVisibility(View.GONE);
            optionLayout.setVisibility(View.GONE);
            collectionLayout.setVisibility(View.GONE);
            stylemainlayout.setVisibility(View.VISIBLE);
            InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null)
            {
                inputManager.hideSoftInputFromWindow(edtsearchOption.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        else if (collectionLayout.getVisibility() == View.VISIBLE)
        {
            storeLayout.setVisibility(View.GONE);
            optionLayout.setVisibility(View.GONE);
            collectionLayout.setVisibility(View.GONE);
            stylemainlayout.setVisibility(View.VISIBLE);
            InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null)
            {
                inputManager.hideSoftInputFromWindow(edtsearchCollection.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        else if(storeLayout.getVisibility() == View.VISIBLE)
        {
            storeLayout.setVisibility(View.GONE);
            optionLayout.setVisibility(View.GONE);
            collectionLayout.setVisibility(View.GONE);
            stylemainlayout.setVisibility(View.VISIBLE);
            InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null)
            {
                inputManager.hideSoftInputFromWindow(edtsearchCollection.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        else {
            selStoreName = null;
            selcollectionName = null;
            seloptionName = null;
            newcollectionList= new ArrayList();
            SnapDashboardActivity._collectionitems = new ArrayList();
            finish();
        }

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
      //  unregisterReceiver(myDataReceiver);
//        if (mReaderManager != null) {
//            mReaderManager.Release();
//        }
    }
}
