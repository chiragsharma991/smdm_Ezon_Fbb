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
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.cipherlab.barcode.GeneralString;
import com.cipherlab.barcode.ReaderManager;
import com.cipherlab.barcode.decoder.BcReaderType;
import com.google.gson.Gson;
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
import apsupportapp.aperotechnologies.com.designapp.StoreAdapter;
import apsupportapp.aperotechnologies.com.designapp.model.SalesAnalysisListDisplay;


public class StyleActivity extends AppCompatActivity
{
    Button btnBarcode;
    RelativeLayout imageBtnBack;
    TextView collection, style;
    List<String> collectionList, list;
    ArrayList<String> storeList, collectionCode_list, articleOptionCode_list;
    public static ArrayList<StyleModel> arrayList,articleOptionList;
    static ArrayList<StyleModel> newcollectionList;
    String userId, bearertoken;
    View view;
    String collectionNM, optionName, store_name, collectionCode, articleOptionCode;
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
    EditText edtsearchCollection, edtsearchOption, edtsearchStore;
    public static String selcollectionName = null, seloptionName = null, selStoreName = null;
    LinearLayout stylemainlayout;
    LinearLayout collectionLayout, optionLayout;
    private ListView listCollection, listOption;
    ListAdapter collectionAdapter;
    StoreAdapter storeAdapter;
    ListAdapter1 optionAdapter;
    String collect_name = "",geoLevel2Code,lobId,store_nm;
    private ReaderManager mReaderManager;
    private IntentFilter filter;
    public static StyleActivity styleactivity;
    private StyleModel styleModel;
    String barcode;
    Gson gson;
    String storecode = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_product_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        context = this;
        styleactivity = this;
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
        gson = new Gson();
        storeList =new ArrayList<String>();
        collectionList = new ArrayList<String>();
        arrayList = new ArrayList<StyleModel>();
        collectionCode_list = new ArrayList<String>();
        articleOptionCode_list = new ArrayList<String>();
        list = new ArrayList<>();
        articleOptionList = new ArrayList<StyleModel>();
        stylemainlayout = (LinearLayout) findViewById(R.id.stylemainlayout);
        stylemainlayout.setVisibility(View.VISIBLE);
        collectionLayout = (LinearLayout) findViewById(R.id.collectionLayout);
        optionLayout = (LinearLayout) findViewById(R.id.optionLayout);
        edtsearchStore = (EditText)findViewById(R.id.searchStore);
        edtsearchCollection = (EditText) findViewById(R.id.searchCollection);
        edtsearchOption = (EditText) findViewById(R.id.searchOption);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        String submit = "Submit";
        btnSubmit.setText(submit);
        btnBarcode = (Button) findViewById(R.id.btnBarcode);
        imageBtnBack = (RelativeLayout) findViewById(R.id.imageBtnBack);

        if (getIntent().getExtras() != null)
        {
            if(getIntent().getExtras().getString("from").equals("dashBoard"))
            {
                storecode  = getIntent().getExtras().getString("storeCode");
            }
            else
            {
                storecode  = getIntent().getExtras().getString("selStoreName");
                optionName = getIntent().getExtras().getString("selOptionName");
                collectionNM = getIntent().getExtras().getString("selCollectionname");
                selcollectionName = getIntent().getExtras().getString("selCollectionname");
                seloptionName = getIntent().getExtras().getString("selOptionName");
            }
       }

        collection = (TextView) findViewById(R.id.searchablespinnerlibrary);
        collection.setText("Select Collection");
        listCollection = (ListView) findViewById(R.id.listCollection);

        style = (TextView) findViewById(R.id.searchablespinnerlibrary1);
        style.setText("Select Option");
        style.setEnabled(false);
        listOption = (ListView) findViewById(R.id.listOption);
        optionAdapter = new ListAdapter1(articleOptionList, StyleActivity.this);
        listOption.setAdapter(optionAdapter);
        listOption.setTextFilterEnabled(true);
        optionAdapter.notifyDataSetChanged();
        collectionCall(storecode);


        btnSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (collection.getText().toString().trim().equals("Select Collection")) {
                    Toast.makeText(StyleActivity.this, "Please select Collection", Toast.LENGTH_LONG).show();

                } else if (style.getText().toString().trim().equals("Select Option")) {
                    Toast.makeText(StyleActivity.this, "Please select Option", Toast.LENGTH_LONG).show();

                } else {
                    if (Reusable_Functions.chkStatus(context)) {
                        Reusable_Functions.hDialog();
//                        Log.e("store name "," "+store_name+" "+store_name.substring(0,4));
//                        Log.e("articleOptionCode "," "+articleOptionCode);

                        store_name = store_name.substring(0,4);
                        Reusable_Functions.sDialog(context, "Loading  data...");
                        requestStyleDetailsAPI(optionName, "optionname",store_name, articleOptionCode);

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

                Log.e("selcollectionName "," "+selcollectionName+" seloptionName "+seloptionName+" collectionNM "+collectionNM+" optionName "+optionName);

                if (isAMobileModel())
                {

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

        edtsearchStore.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                storeAdapter.getFilter().filter(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                storeAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                storeAdapter.getFilter().filter(s);
            }
        });

        edtsearchStore.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_NEXT) || (actionId == EditorInfo.IME_ACTION_GO)) {
                    InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputManager != null) {
                        inputManager.hideSoftInputFromWindow(edtsearchCollection.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                return false;
            }
        });

        edtsearchCollection.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                collectionAdapter.getFilter().filter(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchData = edtsearchCollection.getText().toString();
                collectionAdapter.getFilter().filter(searchData);
               // collectionAdapter.notifyDataSetChanged();
              }

            @Override
            public void afterTextChanged(Editable s) {

//                collectionAdapter.getFilter().filter(s);
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
                edtsearchCollection.setText("");
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
                collectionLayout.setVisibility(View.GONE);
                optionLayout.setVisibility(View.VISIBLE);
            }

        });

        listCollection.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //harshada
                offsetvalue = 0;
                count = 0;
                //
                collectionNM = arrayList.get(position).getCollectionName();
                collectionCode = arrayList.get(position).getCollectionCode();
                selcollectionName = collectionNM;
                collection.setText(selcollectionName);
                //harshada
                seloptionName = null;
                optionName = "";
                //
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
                        articleOptionList = new ArrayList<StyleModel>();
                        optionAdapter = new ListAdapter1(articleOptionList, StyleActivity.this);
                        listOption.setAdapter(optionAdapter);
                        requestArticleOptionsAPI(collectionCode,store_name);   //collectionNM
                    }
                    else
                    {
                        Toast.makeText(StyleActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        listOption.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                optionName = articleOptionList.get(position).getArticleOptions();
                articleOptionCode = articleOptionList.get(position).getArticleOptionCode();
                //seloptionName = optionName;
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
        if (Reusable_Functions.chkStatus(StyleActivity.this))
        {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(StyleActivity.this, "Loading data...");
            requestStyleDetailsAPI(barcode, "barcode", store_name, articleOptionCode);
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
                    requestStyleDetailsAPI(result.getContents(), "barcode", store_name, articleOptionCode);

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
        if (s == null || s.length() == 0)
        {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first))
        {
            return s;
        }
        else

        {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    private void requestStyleDetailsAPI(final String content, final String check, final String store_name, final String articleOptionCode)
    {

        String url = " ";
        if (check.equals("optionname"))
        {
            url = ConstsCore.web_url + "/v1/display/productdetailsNew/" + userId + "?articleOption=" + articleOptionCode.replaceAll(" ", "%20").replaceAll("&", "%26").replaceAll(",", "%2c")+"&geoLevel2Code="+geoLevel2Code + "&lobId="+lobId +"&storeCode="+store_name;

        }
        else if (check.equals("barcode"))
        {
            url = ConstsCore.web_url + "/v1/display/productdetailsNew/" + userId + "?eanNumber=" + content +"&geoLevel2Code="+geoLevel2Code + "&lobId="+lobId+"&storeCode="+store_name;
        }
        Log.e("", "requestStyleDetailsAPI: "+url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {
//                        Log.e("productdetailsNew res "," "+response.toString());
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
                                intent.putExtra("articleOptionCode", articleOptionCode);
                                intent.putExtra("styleDetailsBean", styleDetailsBean);
                                intent.putExtra("selStoreName", storecode);
                                intent.putExtra("selCollectionname", collectionNM);
                                intent.putExtra("selOptionName", optionName);
                                intent.putExtra("storeCode", store_name);
                                intent.putExtra("check", check);
                                intent.putExtra("content", content);



                                Log.e("selStoreName "," "+selStoreName+" selCollectionname "+collectionNM+" selOptionName"+optionName);

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
                        error.printStackTrace();

                        if (error.networkResponse == null) {
                            if (error.getClass().equals(TimeoutError.class)) {
                                // Show timeout error message
                                Toast.makeText(StyleActivity.this,
                                        "Oops. Timeout error!",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(StyleActivity.this, "Network connectivity fail", Toast.LENGTH_LONG).show();
                        }
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

        int socketTimeout = 90000;
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
//                        Log.e("collection response "," "+response.toString());

                        try {
                            if (response.equals("") || response == null || response.length() == 0 && collectioncount == 0)
                            {
                                Reusable_Functions.hDialog();
                                Toast.makeText(StyleActivity.this, "No collection data found", Toast.LENGTH_LONG).show();

                            }
                            else if (response.equals("") || response == null || response.length() == 0 && collectioncount > 0)
                            {

                                // harshada
//                                Collections.sort(arrayList);
                                styleModel = new StyleModel();
                                styleModel.setCollectionName("Select Collection");
                                arrayList.add(0, styleModel);
                                collectionAdapter.notifyDataSetChanged();
                                Reusable_Functions.hDialog();
                                newcollectionList = new ArrayList<StyleModel>();
                                newcollectionList.addAll(arrayList);

                                if (selcollectionName == null || selcollectionName.equals(""))
                                {
                                    collection.setText("Select Collection");
                                    style.setText("Select Option");
                                    Log.e("in if : first", " ");
                                }

                                else
                                {

                                    Log.e("here in else : ", " ");

                                    for(int i = 0; i < arrayList.size(); i++)
                                    {
                                        if(arrayList.get(i).getCollectionName().equals(selcollectionName))
                                        {
                                            collectionNM = arrayList.get(i).getCollectionName();
                                            collectionCode = arrayList.get(i).getCollectionCode();
                                            break;
                                        }
                                    }



                                    if (Reusable_Functions.chkStatus(context))
                                    {
                                        Reusable_Functions.sDialog(context, "Loading options data...");
                                        offsetvalue = 0;
                                        limit = 100;
                                        count = 0;
                                        articleOptionList = new ArrayList<StyleModel>();
                                        optionAdapter = new ListAdapter1(articleOptionList, StyleActivity.this);
                                        listOption.setAdapter(optionAdapter);
                                        requestArticleOptionsAPI(collectionCode,store_name);   //collectionNM
                                    }
                                    else
                                    {
                                        Toast.makeText(StyleActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
                                    }

                                }

                            }
                            else if (response.length() == collectionlimit)
                            {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject collectionName = response.getJSONObject(i);
                                    styleModel = gson.fromJson(response.get(i).toString(), StyleModel.class);
                                    arrayList.add(styleModel);
                                }
                                collectionoffset = (collectionlimit * collectioncount) + collectionlimit;
                                collectioncount++;
                                requestCollectionAPI(storeName);
                            }
                            else if (response.length() < collectionlimit)
                            {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject collectionName = response.getJSONObject(i);
                                    styleModel = gson.fromJson(response.get(i).toString(), StyleModel.class);
                                    arrayList.add(styleModel);
//
                                }

                                styleModel = new StyleModel();
                                styleModel.setCollectionName("Select Collection");
                                arrayList.add(0, styleModel);
                                collectionAdapter = new ListAdapter(arrayList, StyleActivity.this);
                                //attach the adapter to the list
                                listCollection.setAdapter(collectionAdapter);
                                collectionAdapter.notifyDataSetChanged();
                                Reusable_Functions.hDialog();
                                newcollectionList = new ArrayList<StyleModel>();
                                newcollectionList.addAll(arrayList);


                                if (selcollectionName == null || selcollectionName.equals(""))
                                {
                                    collection.setText("Select Collection");
                                    style.setText("Select Option");
                                    Log.e("in if : first", " ");
                                }

                                else
                                {

                                    Log.e("here in else : ", " ");

                                    for(int i = 0; i < arrayList.size(); i++)
                                    {
                                        if(arrayList.get(i).getCollectionName().equals(selcollectionName))
                                        {
                                            collectionNM = arrayList.get(i).getCollectionName();
                                            collectionCode = arrayList.get(i).getCollectionCode();
                                            break;
                                        }
                                    }



                                    if (Reusable_Functions.chkStatus(context))
                                    {
                                        Reusable_Functions.sDialog(context, "Loading options data...");
                                        offsetvalue = 0;
                                        limit = 100;
                                        count = 0;
                                        articleOptionList = new ArrayList<StyleModel>();
                                        optionAdapter = new ListAdapter1(articleOptionList, StyleActivity.this);
                                        listOption.setAdapter(optionAdapter);
                                        requestArticleOptionsAPI(collectionCode,store_name);   //collectionNM
                                    }
                                    else
                                    {
                                        Toast.makeText(StyleActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
                                    }

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

    private void requestArticleOptionsAPI(final String collectionCode, final String store_name)
    {
        String url;
        url = ConstsCore.web_url + "/v1/display/collectionoptionsNew/" + userId + "?prodLevel6Code=" + collectionCode.replaceAll(" ", "%20").replaceAll("&", "%26").replaceAll(",", "%2c") + "&offset=" + offsetvalue + "&limit=" + limit+ "&geoLevel2Code="+geoLevel2Code + "&lobId="+lobId +"&storeCode="+store_name;
        Log.e("requestArticleOptionsAPI url ", "" + url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response)
                    {

//                        Log.e("option response "," "+response.toString());

                        try
                        {
                            if (response.equals("") || response == null || response.length() == 0 && count == 0)
                            {
                                styleModel = new StyleModel();
                                styleModel.setArticleOptions("Select Option");
                                articleOptionList.add(0, styleModel);
                                style.setEnabled(false);
                                Reusable_Functions.hDialog();
                                Toast.makeText(StyleActivity.this, "No options data found", Toast.LENGTH_LONG).show();
                            }
                            if (response.equals("") || response == null || response.length() == 0 && count > 0)
                            {
//                                Collections.sort(articleOptionList);
                                styleModel = new StyleModel();
                                styleModel.setArticleOptions("Select Option");
                                articleOptionList.add(0, styleModel);
                                style.setEnabled(true);
                                Reusable_Functions.hDialog();
                                SnapDashboardActivity._collectionitems = new ArrayList();
                                SnapDashboardActivity._collectionitems.addAll(articleOptionList);

                                if (seloptionName == null || seloptionName.equals(""))
                                {
                                    style.setText("Select Option");
                                }
                                else
                                {
                                    style.setText(seloptionName);

                                }
                            }
                            else if (response.length() == limit)
                            {
                                for (int i = 0; i < response.length(); i++)
                                {
                                    JSONObject jsonResponse = response.getJSONObject(i);
                                    styleModel = gson.fromJson(response.get(i).toString(), StyleModel.class);
                                    articleOptionList.add(styleModel);

                                    if(seloptionName != null)
                                    {
                                        if(seloptionName.equals(styleModel.getArticleOptions()))
                                        {
                                            articleOptionCode = styleModel.getArticleOptionCode();
                                        }

                                    }
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestArticleOptionsAPI(collectionCode, store_name);
                            }
                            else if (response.length() < limit)
                            {
                                for (int i = 0; i < response.length(); i++)
                                {
                                    JSONObject jsonResponse = response.getJSONObject(i);
                                    styleModel = gson.fromJson(response.get(i).toString(), StyleModel.class);
                                    articleOptionList.add(styleModel);

                                    if(seloptionName != null)
                                    {
                                        if(seloptionName.equals(styleModel.getArticleOptions()))
                                        {
                                            articleOptionCode = styleModel.getArticleOptionCode();
                                        }

                                    }

                                }


                                styleModel = new StyleModel();
                                styleModel.setArticleOptions("Select Option");
                                articleOptionList.add(0, styleModel);
                                style.setEnabled(true);
                                Reusable_Functions.hDialog();
                                SnapDashboardActivity._collectionitems = new ArrayList();
                                SnapDashboardActivity._collectionitems.addAll(articleOptionList);


                                if (seloptionName == null || seloptionName.equals(""))
                                {
                                    style.setText("Select Option");
                                }
                                else
                                {
                                    style.setText(seloptionName);

                                }
                            }




                            optionAdapter.notifyDataSetChanged();

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
            newcollectionList= new ArrayList<StyleModel>();
            arrayList = new ArrayList<StyleModel>();
            articleOptionList = new ArrayList<StyleModel>();
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


    public void collectionCall(String storecode)
    {

        collectionoffset = 0;
        collectioncount = 0;
        store_name = storecode;
        selStoreName = store_name;

//        selcollectionName = null;
//        seloptionName = null;
//        collectionNM = "";
//        optionName = "";
        if(selcollectionName != null)
        {
            collection.setText(selcollectionName);
        }
        else
        {
            collection.setText("Select Collection");
        }

        if(seloptionName != null)
        {
            style.setText(seloptionName);
        }
        else
        {
            style.setText("Select Option");
        }


        collectionLayout.setVisibility(View.GONE);

        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(edtsearchStore.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

            if (Reusable_Functions.chkStatus(context))
            {
                Reusable_Functions.sDialog(context, "Loading collection data...");
                offsetvalue = 0;
                collectionlimit = 100;
                count = 0;
                collectionList.clear();
                store_name = store_name.substring(0,4);
                arrayList = new ArrayList<StyleModel>();
                collectionAdapter = new ListAdapter(arrayList, StyleActivity.this);
                listCollection.setAdapter(collectionAdapter);

                articleOptionList = new ArrayList<StyleModel>();
                optionAdapter = new ListAdapter1(articleOptionList, StyleActivity.this);
                listOption.setAdapter(optionAdapter);
                requestCollectionAPI(store_name);
            }
            else
            {
                Toast.makeText(StyleActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
            }


    }
}
