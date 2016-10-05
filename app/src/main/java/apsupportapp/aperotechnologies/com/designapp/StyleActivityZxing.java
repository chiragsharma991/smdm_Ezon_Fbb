//package apsupportapp.aperotechnologies.com.designapp;
//
//import android.Manifest;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.preference.PreferenceManager;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Cache;
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.Network;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.RetryPolicy;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.BasicNetwork;
//import com.android.volley.toolbox.DiskBasedCache;
//import com.android.volley.toolbox.HurlStack;
//import com.android.volley.toolbox.JsonArrayRequest;
//import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import amobile.android.barcodesdk.api.IWrapperCallBack;
//import amobile.android.barcodesdk.api.Wrapper;
//import me.sudar.zxingorient.ZxingOrient;
//import me.sudar.zxingorient.ZxingOrientResult;
//
////import com.google.zxing.integration.android.IntentIntegrator;
////import com.google.zxing.integration.android.IntentResult;
//
//public class StyleActivityZxing extends AppCompatActivity implements IWrapperCallBack //implements IWrapperCallBack {
//{   Button  btnSearch,btnBarcode;
//    private Wrapper m_wrapper = null;
//    Button imageBtnBack;
//   // Spinner style;
//    SearchableSpinner collection,style;
//    EditText edtSearch;
//    ArrayAdapter<String> adapter1,adapter2;
//    List<String> collectionList,StyleList,list;
//    ArrayList<String> arrayList,articleOptionList;
//    String userId, bearertoken;
//    View view;
//    String collectionNM,optionName;
//    RequestQueue queue;
//    Context context;
//    boolean flag=true;
//    ArrayList<StyleDetailsBean> styleDetailsBeenList;
//    StyleDetailsBean styleDetailsBean;
//    MySingleton m_config;
//    int offsetvalue=0,limit=100;
//    int count=0;
//    int collectionoffset=0, collectionlimit=100,collectioncount=0;
//    SharedPreferences sharedPreferences;
//    Button btnSubmit;
//    private static final int REQUEST_CAMERA = 0x00000011;
//
//
//    public static String selcollectionName = null , seloptionName = null;
//    String tempselcollectionName = "";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
//        getSupportActionBar().hide();
//        setContentView(R.layout.activity_style);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        context = this;
//        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//        userId = sharedPreferences.getString("userId", "");
//        bearertoken = sharedPreferences.getString("bearerToken", "");
//
//
//        if (isAMobileModel()) {
//            Log.e("amobile device", "");
//            m_wrapper = new Wrapper(StyleActivityZxing.this);
//
//            IntentFilter filter = new IntentFilter("BROADCAST_BARCODE");
//            registerReceiver(m_brc, filter);
//        }
//
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//                != PackageManager.PERMISSION_GRANTED) {
//            requestCameraPermission();
//        }
//
//
//        context = this;
//        m_config = MySingleton.getInstance(context);
//
//        styleDetailsBeenList = new ArrayList<>();
//        final Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
//        Network network = new BasicNetwork(new HurlStack());
//        queue = new RequestQueue(cache, network);
//        queue.start();
//
//
//        collectionList = new ArrayList<String>();
//        arrayList = new ArrayList<String>();
//        list = new ArrayList<>();
//
//        articleOptionList = new ArrayList<>();
//
//        if (Reusable_Functions.chkStatus(context)) {
//            Reusable_Functions.hDialog();
//            Reusable_Functions.sDialog(context, "Loading collection data...");
//
//            requestCollectionAPI(collectionoffset, collectionlimit);
//
//        } else {
//            // Reusable_Functions.hDialog();
//            Toast.makeText(StyleActivityZxing.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
//        }
//
//        btnSubmit = (Button) findViewById(R.id.btnSubmit);
//        btnBarcode = (Button) findViewById(R.id.btnBarcode);
//        imageBtnBack = (Button) findViewById(R.id.imageBtnBack);
//
//        if(getIntent().getExtras() != null)
//        {
//            selcollectionName =  getIntent().getExtras().getString("selCollectionname");
//            seloptionName =  getIntent().getExtras().getString("selOptionName");
//        }
//
//        Log.e("selcollectionName"," "+selcollectionName+" "+seloptionName);
//
//        collection = (SearchableSpinner)findViewById(R.id.searchablespinnerlibrary);
//        collection.setTitle("Select Collection");
//
//
//        style = (SearchableSpinner) findViewById(R.id.searchablespinnerlibrary1);
//        list.add("Select Option");
//        style.setEnabled(false);
//
//
//        style.setTitle("Select Option");
//        adapter2 = new ArrayAdapter<String>(StyleActivityZxing.this, android.R.layout.simple_spinner_item, list);
//        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        style.setAdapter(adapter2);
//
//        style.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                optionName = parent.getSelectedItem().toString().trim();
//                Log.e("optionName "," "+optionName);
//
//                InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                if(inputManager != null){
//                    inputManager.hideSoftInputFromWindow(style.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                }
//
//
//            } // to close the onItemSelected
//
//            public void onNothingSelected(AdapterView<?> parent) {
//
//                View view = getCurrentFocus();
//                if (view != null) {
//                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                }
//            }
//        });
//
//        btnSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(collection.getSelectedItem().toString().trim().equals("Select Collection"))
//                {
//                    Toast.makeText(StyleActivityZxing.this,"Please select Collection",Toast.LENGTH_LONG).show();
//                }
//                else if(style.getSelectedItem().toString().trim().equals("Select Option"))
//                {
//                    Toast.makeText(StyleActivityZxing.this,"Please select Option",Toast.LENGTH_LONG).show();
//                }
//                else
//                {
//                    if (Reusable_Functions.chkStatus(context)) {
//                        Reusable_Functions.hDialog();
//                        Reusable_Functions.sDialog(context, "Loading  data...");
//                        Log.e("select item", optionName);
//                        requestStyleDetailsAPI(optionName, "optionname");
//
//                    } else {
//                        // Reusable_Functions.hDialog();
//                        Toast.makeText(StyleActivityZxing.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//        });
//
//
//        btnBarcode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                Log.e("scan clicked","");
//                if(isAMobileModel())
//
//                {
//                    if (m_wrapper != null && m_wrapper.IsOpen())
//                    {
//                        m_wrapper.Scan();
//                    }
////                    Intent intent = new Intent(StyleActivity.this,ScannerActivity.class);
////                    startActivity(intent);
//                }
//                else if(!isAMobileModel())
//                {
//                    Log.e("regular device","");
//                    scanBarcode(view);
//
//                }
//            }
//        });
//        imageBtnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selcollectionName = null ; seloptionName = null;
//                Intent intent=new Intent(StyleActivityZxing.this,DashBoardActivity.class);
//                startActivity(intent);
//                finish();
//
//            }
//        });
//
//
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        if (m_wrapper != null) {
//            if (m_wrapper.Open()) {
//                m_wrapper.SetDispathBarCode(false);
//                m_wrapper.SetLightMode2D(Wrapper.LightMode2D.mix);
//                m_wrapper.SetTimeOut(15);
//            } else {
//                m_wrapper = null;
//            }
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        if(isAMobileModel()) {
//            unregisterReceiver(m_brc);
//        }
//
//        if (m_wrapper != null) {
//            m_wrapper.Close();
//            m_wrapper = null;
//        }
//
//        super.onDestroy();
//    }
//
//    @Override
//    public void onDataReady(String strData) {
////        if (m_wrapper != null && m_wrapper.Open()) {
////            m_wrapper.Stop();
////            m_wrapper.Scan();
////        }
////        m_wrapper.Close();
//        byte[] bytes = strData.getBytes();
//        if(strData == null)
//        {
//            Toast.makeText(this, "Barcode not scanned", Toast.LENGTH_LONG).show();
//        }
//        else if (strData.equalsIgnoreCase("Unknown command : setLightMode2D"))
//        {
//            Log.e("Do nothing","");
//        }
//        else
//        {
//            Toast.makeText(this, "Barcode Scanned: " + strData, Toast.LENGTH_LONG).show();
//            if (Reusable_Functions.chkStatus(context)) {
//                Reusable_Functions.hDialog();
//                Reusable_Functions.sDialog(context, "Loading  data...");
//                //requestStyleDetailsBarcodeAPI( strData);
//                requestStyleDetailsAPI(strData, "barcode");
//
//            } else
//            {
//                // Reusable_Functions.hDialog();
//                Toast.makeText(StyleActivityZxing.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
//            }
//
//
//        }
//        //m_text.setText(strData);
//    }
//
//    private BroadcastReceiver m_brc = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent != null) {
//                String s = intent.getStringExtra("BROADCAST_BARCODE_STRING");
//                if (s != null) {
//                    // m_text.setText(s);
//                    Log.e("printing string s=",s);
//                }
//            }
//        }
//    };
//
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
//
//    private boolean isAMobileModel()  {
//        Log.e("checking model","");
//        getDeviceInfo();
//        Log.e("model is ",""+Build.MODEL);
//
//        return Build.MODEL.contains("G0550");
//    }
//
//    public void scanBarcode(View view) {
//
//        //new IntentIntegrator(this).initiateScan();
//        new ZxingOrient(StyleActivityZxing.this).initiateScan();
//
////        Toast.makeText(StyleActivity.this,"Barcode :")
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
////
////        if(result != null) {
////            if(result.getContents() == null)
////            {
////                Toast.makeText(this, "Barcode not scanned", Toast.LENGTH_LONG).show();
////            } else
////            {
////                Toast.makeText(this, "Barcode Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
////                if (Reusable_Functions.chkStatus(context)) {
////                    Reusable_Functions.hDialog();
////                    Reusable_Functions.sDialog(context, "Loading  data...");
////                    //requestStyleDetailsBarcodeAPI( result.getContents());
////                    requestStyleDetailsAPI(result.getContents(), "barcode");
////
////                } else
////                {
////                    // Reusable_Functions.hDialog();
////                    Toast.makeText(StyleActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
////                }
////
////
////            }
////        } else {
////            // This is important, otherwise the result will not be passed to the fragment
////            super.onActivityResult(requestCode, resultCode, data);
////        }
//
//
//        ZxingOrientResult scanResult = ZxingOrient.parseActivityResult(requestCode, resultCode, data);
//        if (scanResult != null) {
//            if(scanResult.getContents() == null)
//            {
//                Toast.makeText(this, "Barcode not scanned", Toast.LENGTH_LONG).show();
//            } else
//            {
//                Toast.makeText(this, "Barcode Scanned: " + scanResult.getContents(), Toast.LENGTH_LONG).show();
//                if (Reusable_Functions.chkStatus(context)) {
//                    Reusable_Functions.hDialog();
//                    Reusable_Functions.sDialog(context, "Loading  data...");
//                    //requestStyleDetailsBarcodeAPI( result.getContents());
//                    requestStyleDetailsAPI(scanResult.getContents(), "barcode");
//
//                } else
//                {
//                    // Reusable_Functions.hDialog();
//                    Toast.makeText(StyleActivityZxing.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
//                }
//
//
//            }
//        } else {
//            // This is important, otherwise the result will not be passed to the fragment
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }
//
//
//
//
////
////    @Override
////    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
////        if(result != null) {
////            if(result.getContents() == null)
////            {
////                Toast.makeText(this, "Barcode not scanned", Toast.LENGTH_LONG).show();
////            } else
////            {
////                Toast.makeText(this, "Barcode Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
////                if (Reusable_Functions.chkStatus(context)) {
////                    Reusable_Functions.sDialog(context, "Loading  data...");
////                    requestStyleDetailsBarcodeAPI(result.getContents());
////
////                } else
////                {
////                    // Reusable_Functions.hDialog();
////                    Toast.makeText(StyleActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
////                }
////
////
////            }
////        } else {
////            // This is important, otherwise the result will not be passed to the fragment
////            super.onActivityResult(requestCode, resultCode, data);
////        }
////    }
//
////    private void requestStyleDetailsBarcodeAPI(String contents)
////    {
////
////
////            String url=ConstsCore.web_url + "/v1/display/productdetails/"+userId+"?eanNumber="+contents;
////
////            Log.i("URL barcode  style  ", url);
////
////            final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
////                    new Response.Listener<JSONArray>()
////                    {
////                        @Override
////                        public void onResponse(JSONArray response)
////                        {
////                            Log.i("Style details :   ", response.toString());
////                            try
////                            {
////
////                                if (response.equals(null) || response ==null||response.length()==0)
////                                {
////                                    Log.e("requestDetailBarcodeAPI","hiiiiii");
////                                    Reusable_Functions.hDialog();
////                                    Toast.makeText(StyleActivity.this,"No data found",Toast.LENGTH_LONG).show();
////
////                                }else {
////
////                                    Reusable_Functions.hDialog();
////
////                                    // JSONObject mainObject = new JSONObject(response);
////                                    for (int i = 0; i < response.length(); i++) {
////                                        JSONObject styleDetails = response.getJSONObject(i);
////                                        String productName = styleDetails.getString("productName");
////                                        String collectionName = styleDetails.getString("collectionName");
////                                        String productFabricDesc = styleDetails.getString("productFabricDesc");
////                                        String productFitDesc = styleDetails.getString("productFitDesc");
////                                        String productFinishDesc = styleDetails.getString("productFinishDesc");
////                                        String seasonName = styleDetails.getString("seasonName");
////                                        int fwdWeekCover = styleDetails.getInt("fwdWeekCover");
////
////                                        int twSaleTotQty = styleDetails.getInt("twSaleTotQty");
////                                        int lwSaleTotQty = styleDetails.getInt("lwSaleTotQty");
////                                        int ytdSaleTotQty = styleDetails.getInt("ytdSaleTotQty");
////
////                                        int stkOnhandQty = styleDetails.getInt("stkOnhandQty");
////                                        int stkGitQty = styleDetails.getInt("stkGitQty");
////                                        int targetStock = styleDetails.getInt("targetStock");
////
////                                        int unitGrossPrice = styleDetails.getInt("unitGrossPrice");
////                                        double sellThruUnitsRcpt = styleDetails.getDouble("sellThruUnitsRcpt");
////                                        int ros = styleDetails.getInt("ros");
////
////                                        //   int usp = styleDetails.getInt("usp");
////
////                                        styleDetailsBean = new StyleDetailsBean();
////
////                                        styleDetailsBean.setProductName(productName);
////                                        styleDetailsBean.setCollectionName(collectionName);
////                                        styleDetailsBean.setProductFabricDesc(productFabricDesc);
////                                        styleDetailsBean.setProductFitDesc(productFitDesc);
////                                        styleDetailsBean.setProductFinishDesc(productFinishDesc);
////                                        styleDetailsBean.setSeasonName(seasonName);
////
//////                                    styleDetailsBean.setFirstReceiptDate(firstReceiptDate);
//////                                    styleDetailsBean.setLastReceiptDate(lastReceiptDate);
////                                        styleDetailsBean.setFwdWeekCover(fwdWeekCover);
////
////                                        styleDetailsBean.setTwSaleTotQty(twSaleTotQty);
////                                        styleDetailsBean.setLwSaleTotQty(lwSaleTotQty);
////                                        styleDetailsBean.setYtdSaleTotQty(ytdSaleTotQty);
////
////                                        styleDetailsBean.setUnitGrossPrice(unitGrossPrice);
////                                        styleDetailsBean.setSellThruUnitsRcpt(sellThruUnitsRcpt);
////                                        styleDetailsBean.setRos(ros);
////
////                                        //  styleDetailsBean.setUsp(usp);
////                                        Intent intent = new Intent(StyleActivity.this, SwitchingTabActivity.class);
////                                        intent.putExtra("styleDetailsBean", styleDetailsBean);
////                                        //intent.putExtra("userId",m_config.userId);
////                                        startActivity(intent);
////
////                                        Log.e("Style details:", productName + "   " + collectionName + "  " + productFabricDesc + "  " + productFitDesc + "  " + productFinishDesc + "  " + seasonName);
////                                        // Log.e("row 1:", firstReceiptDate + "   " + lastReceiptDate + "  " + fwdWeekCover);
////                                        Log.e("row 2:", twSaleTotQty + "   " + lwSaleTotQty + "  " + ytdSaleTotQty);
////                                        Log.e("row 3:", stkOnhandQty + "   " + stkGitQty + "  " + targetStock);
////                                        Log.e("row4:", unitGrossPrice + "   " + sellThruUnitsRcpt + "  " + ros);
////                                        //  Log.e("benefit :", "" + usp);
////
////                                    }
////
////
//////                                Intent intent = new Intent(StyleActivity.this, SwitchingTabActivity.class);
//////                                intent.putExtra("styleDetailsBean", styleDetailsBean);
//////                                //intent.putExtra("userId",m_config.userId);
//////                                startActivity(intent);
////
////                                }
////                            }
////                            catch(Exception e)
////                            {
////                                Log.e("Exception e",e.toString() +"");
////                                e.printStackTrace();
////                            }
////                        }
////                    },
////                    new Response.ErrorListener()
////                    {
////                        @Override
////                        public void onErrorResponse(VolleyError error)
////                        {
////                            Reusable_Functions.hDialog();
////                            // Toast.makeText(LoginActivity.this,"Invalid User",Toast.LENGTH_LONG).show();
////                            error.printStackTrace();
////                        }
////                    }
////
////            ){
////                @Override
////                public Map<String, String> getHeaders() throws AuthFailureError
////                {
////                    Map<String, String> params = new HashMap<>();
////                    params.put("Content-Type", "application/json");
////                    params.put("Authorization", "Bearer "+bearertoken);
////                    return params;
////                }
////            };
////            int socketTimeout = 60000;//5 seconds
////            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
////            postRequest.setRetryPolicy(policy);
////            queue.add(postRequest);
////
////    }
//
//
//    public String getDeviceInfo() {
//        String manufacturer = Build.MANUFACTURER;
//        String model = Build.MODEL;
//        if (model.startsWith(manufacturer)) {
//            return capitalize(model);
//        } else {
//            return capitalize(manufacturer) + " " + model;
//        }
//    }
//    private String capitalize(String s) {
//        if (s == null || s.length() == 0) {
//            return "";
//        }
//        char first = s.charAt(0);
//        if (Character.isUpperCase(first)) {
//            return s;
//        } else {
//            return Character.toUpperCase(first) + s.substring(1);
//        }
//    }
//
//
//    private void requestStyleDetailsAPI(String content, String check)
//    {
//        String url = "";
//        if(check.equals("optionname"))
//        {
//            url = ConstsCore.web_url + "/v1/display/productdetails/"+userId+"?articleOption="+content.replaceAll(" ", "%20").replaceAll("&","%26");
//        }
//        else if(check.equals("barcode"))
//        {
//            url = ConstsCore.web_url + "/v1/display/productdetails/"+userId+"?eanNumber="+content;
//        }
//
//        Log.i("URL style  ", ""+url);
//
//        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
//                new Response.Listener<JSONArray>()
//                {
//                    @Override
//                    public void onResponse(JSONArray response)
//                    {
//                        Log.i("Style details :   ", response.toString());
//                        try
//                        {
//
//                            if (response.equals(null) || response ==null||response.length()==0)
//                            {
//                                Reusable_Functions.hDialog();
//                                Toast.makeText(StyleActivityZxing.this,"No data found",Toast.LENGTH_LONG).show();
//
//                            }else {
//
//                                    Reusable_Functions.hDialog();
//
//                                // JSONObject mainObject = new JSONObject(response);
//
//                                    JSONObject styleDetails = response.getJSONObject(0);
//
//                                    String articleOption=styleDetails.getString("articleOption");
//                                    String productName = styleDetails.getString("productName");
//                                    String collectionName = styleDetails.getString("collectionName");
//                                    String productFabricDesc = styleDetails.getString("productFabricDesc");
//                                    String productFitDesc = styleDetails.getString("productFitDesc");
//                                    String productFinishDesc = styleDetails.getString("productFinishDesc");
//                                    String seasonName = styleDetails.getString("seasonName");
//
//                                    String promoFlg = styleDetails.getString("promoFlg");
//                                    String keyProductFlg = styleDetails.getString("keyProductFlg");
//
//
//                                    String firstReceiptDate = styleDetails.getString("firstReceiptDate");
//                                    String lastReceiptDate = styleDetails.getString("lastReceiptDate");
//                                    double fwdWeekCover = styleDetails.getDouble("fwdWeekCover");
//
//                                    int twSaleTotQty = styleDetails.getInt("twSaleTotQty");
//                                    int lwSaleTotQty = styleDetails.getInt("lwSaleTotQty");
//                                    int ytdSaleTotQty = styleDetails.getInt("ytdSaleTotQty");
//
//                                    int stkOnhandQty = styleDetails.getInt("stkOnhandQty");
//                                    int stkGitQty = styleDetails.getInt("stkGitQty");
//                                    int targetStock = styleDetails.getInt("targetStock");
//
//                                    int unitGrossPrice = styleDetails.getInt("unitGrossPrice");
//                                    double sellThruUnitsRcpt = styleDetails.getDouble("sellThruUnitsRcpt");
//                                    double ros = styleDetails.getDouble("ros");
//
//                                    String articleCode=styleDetails.getString("articleCode");
//                                    String productImageURL=styleDetails.getString("productImageURL");
//                                    Log.e("row4:===", productImageURL);
//                                 //   int usp = styleDetails.getInt("usp");
//
//                                    styleDetailsBean = new StyleDetailsBean();
//                                    styleDetailsBean.setProductName(productName);
//                                    styleDetailsBean.setCollectionName(collectionName);
//                                    styleDetailsBean.setProductFabricDesc(productFabricDesc);
//                                    styleDetailsBean.setProductFitDesc(productFitDesc);
//                                    styleDetailsBean.setProductFinishDesc(productFinishDesc);
//                                    styleDetailsBean.setSeasonName(seasonName);
//
//                                    styleDetailsBean.setFirstReceiptDate(firstReceiptDate);
//                                    styleDetailsBean.setLastReceiptDate(lastReceiptDate);
//                                    styleDetailsBean.setFwdWeekCover(fwdWeekCover);
//
//                                    styleDetailsBean.setTwSaleTotQty(twSaleTotQty);
//                                    styleDetailsBean.setLwSaleTotQty(lwSaleTotQty);
//                                    styleDetailsBean.setYtdSaleTotQty(ytdSaleTotQty);
//
//                                    styleDetailsBean.setStkOnhandQty(stkOnhandQty);
//                                    styleDetailsBean.setStkGitQty(stkGitQty);
//                                    styleDetailsBean.setTargetStock(targetStock);
//
//                                    styleDetailsBean.setUnitGrossPrice(unitGrossPrice);
//                                    styleDetailsBean.setSellThruUnitsRcpt(sellThruUnitsRcpt);
//                                    styleDetailsBean.setRos(ros);
//
//                                    styleDetailsBean.setPromoFlag(promoFlg);
//                                    styleDetailsBean.setKeyProductFlg(keyProductFlg);
//                                    styleDetailsBean.setProductImageURL(productImageURL);
//
//
//                                Intent intent = new Intent(StyleActivityZxing.this, SwitchingTabActivity.class);
//                                intent.putExtra("articleCode",articleCode);
//                                intent.putExtra("articleOption",articleOption);
//                                intent.putExtra("styleDetailsBean", styleDetailsBean);
//                                intent.putExtra("selCollectionname", collectionNM);
//                                intent.putExtra("selOptionName", optionName);
//                                //intent.putExtra("userId",m_config.userId);
//                                startActivity(intent);
//                                finish();
//
//
//
//                            }
//                        }
//                        catch(Exception e)
//                        {
//                            Log.e("Exception e",e.toString() +"");
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error)
//                    {
//                        Reusable_Functions.hDialog();
//                        Log.e("",""+error.networkResponse+"");
//                        Toast.makeText(StyleActivityZxing.this,"Network connectivity fail",Toast.LENGTH_LONG).show();
//                        error.printStackTrace();
//                    }
//                }
//
//        ){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError
//            {
//                Map<String, String> params = new HashMap<>();
//                params.put("Content-Type", "application/json");
//                params.put("Authorization", "Bearer "+bearertoken);
//                return params;
//            }
//        };
//        int socketTimeout = 60000;//5 seconds
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        postRequest.setRetryPolicy(policy);
//        queue.add(postRequest);
//
//
//
//
//    }
//
//
//    private void requestCollectionAPI(int offsetvalue1, final int limit1)
//    {
//        String url= ConstsCore.web_url + "/v1/display/collections/"+userId+"?offset="+collectionoffset +"&limit="+ collectionlimit;
//       // String url="https://ra.manthan.com/v1/display/collections/270389";
//        Log.i("URL   ", url);
//      //   arrayList.clear();
//        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
//                new Response.Listener<JSONArray>()
//                {
//                    @Override
//                    public void onResponse(JSONArray response)
//                    {
//                        Log.i("Collection Response   ", response.toString());
//                        try
//                        {
//                            if (response.equals(null) || response == null|| response.length()==0 && collectioncount==0)
//                            {
//                                Reusable_Functions.hDialog();
//                                Toast.makeText(StyleActivityZxing.this, "No collection data found", Toast.LENGTH_LONG).show();
//                            } else if(response.length()==collectionlimit)
//                            {
//
//                                Log.i("limit eq resp length", ""+response.length());
//                              Log.e("offsetvalue", "" + collectionoffset);
//                                Log.e("limit", "" + collectionlimit);
//                                Log.e("count", "" + collectioncount);
//
//                                for (int i = 0; i < response.length(); i++) {
//                                    JSONObject collectionName = response.getJSONObject(i);
//                                    collectionNM = collectionName.getString("collectionName");
//                                    Log.e("collectionName  :", collectionName.getString("collectionName"));
//                                    arrayList.add(collectionName.getString("collectionName"));
//                                    Log.e("size in limit :", "" + arrayList.size());
//
//                                }
//
//                                collectionoffset = (collectionlimit * collectioncount) + collectionlimit ;
//                                collectioncount++;
//                                requestCollectionAPI(collectionoffset, collectionlimit);
//
//                            }
//                            else if(response.length()< collectionlimit)
//                            {
//                                Reusable_Functions.hDialog();
//
//                                for (int i = 0; i < response.length(); i++)
//                                {
//                                    JSONObject collectionName = response.getJSONObject(i);
//                                    collectionNM=collectionName.getString("collectionName");
//                                    //Log.e("collectionName  :", collectionName.getString("collectionName"));
//                                    arrayList.add(collectionName.getString("collectionName"));
//                                    Log.e("size  :", "" + arrayList.size());
//
//                                }
//
//                                Collections.sort(arrayList);
//                                arrayList.add(0,"Select Collection");
//                                adapter1 = new ArrayAdapter<String>(StyleActivityZxing.this, android.R.layout.simple_spinner_item, arrayList);
//                                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                                collection.setAdapter(adapter1);
//
//                                if(selcollectionName == null || selcollectionName.equals(null))
//                                {
//                                    collection.setSelection(0);
//                                }
//                                else
//                                {
//                                    if(arrayList.contains(selcollectionName))
//                                    {
//                                        collection.setSelection(arrayList.indexOf(selcollectionName));
//                                    }
//                                    else
//                                    {
//                                        collection.setSelection(0);
//                                    }
//
//                                }
//
//
//                                collection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
//                                {
//                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
//                                    {
//
//                                        collectionNM = parent.getSelectedItem().toString().trim();
//
//                                        //Log.e("---- "," "+collectionNM+" "+collectionNM.equals(tempselcollectionName));
//
//                                        if(collectionNM.equals(tempselcollectionName))
//                                        {
//                                            selcollectionName = null;
//                                            seloptionName = null;
//                                        }
//                                        if(selcollectionName == null || selcollectionName.equals(null))
//                                        {
//
//                                        }
//                                        else
//                                        {
//                                            tempselcollectionName = selcollectionName;
//                                        }
//
//
//
//
//                                        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                                        if(inputManager != null){
//                                            inputManager.hideSoftInputFromWindow(collection.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                                        }
//
//                                        if(collectionNM.equalsIgnoreCase("Select Collection"))
//                                        {
//                                            //Toast.makeText(StyleActivity.this,"Please select Collection",Toast.LENGTH_LONG).show();
//                                        }else
//                                        {
//
//                                            Log.e("---999999---"," ");
//                                            if (Reusable_Functions.chkStatus(context))
//                                            {
//                                               // int i=1,j=20;
//                                                //Reusable_Functions.hDialog();
//                                                Reusable_Functions.sDialog(context, "Loading options data...");
//                                                collectionNM= (String) parent.getItemAtPosition(position);
//                                                Log.e("select item",collectionNM);
//                                                offsetvalue=0;
//                                                limit=100;
//                                                 count=0;
//                                                articleOptionList.clear();
//                                                requestArticleOptionsAPI(collectionNM,offsetvalue,limit);
//
//                                            } else
//                                            {
//                                                // Reusable_Functions.hDialog();
//                                                Toast.makeText(StyleActivityZxing.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
//                                            }
//
//                                        }
//                                    } // to close the onItemSelected
//                                    public void onNothingSelected(AdapterView<?> parent)
//                                    {
//
//                                    }
//                                });
//                            }
//                        }
//                        catch(Exception e)
//                        {
//                            Log.e("Exception e",e.toString() +"");
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error)
//                    {
//                        Reusable_Functions.hDialog();
//                        // Toast.makeText(LoginActivity.this,"Invalid User",Toast.LENGTH_LONG).show();
//                        error.printStackTrace();
//                    }
//                }
//
//        ){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError
//            {
//                Map<String, String> params = new HashMap<>();
//                params.put("Content-Type", "application/json");
//                params.put("Authorization", "Bearer "+bearertoken);
//                return params;
//            }
//        };
//        int socketTimeout = 60000;//5 seconds
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        postRequest.setRetryPolicy(policy);
//        queue.add(postRequest);
//
//
//
//
//
//    }
//
//
//
//
//    private void requestArticleOptionsAPI(final String collectionNM,int offsetvalue1, final int limit1)
//    {
//        String url;
//
//       // url = "https://ra.manthan.com/v1/display/collectionoptions/270389?collectionName=" + collectionNM.replace(" ", "%20")+"&offset="+offsetvalue +"&limit="+ limit;
//        url = ConstsCore.web_url + "/v1/display/collectionoptions/" +userId + "?collectionName=" + collectionNM.replaceAll(" ", "%20").replaceAll("&","%26")+"&offset="+offsetvalue +"&limit="+ limit;
//       // url = "https://ra.manthan.com/v1/display/collectionoptions/" + m_config.userId + "?collectionName=" + collectionNM.replace(" ", "%20")+"&offset=1&limit=20";
//        Log.i("URL article   ", url);
//        final int[] optPos = {0};
//
//
//        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
//                new Response.Listener<JSONArray>()
//                {
//
//
//                    @Override
//                    public void onResponse(JSONArray response)
//                    {
//                        Log.i("ArticleOption Response ", response.toString());
//                        try
//                        {
//                            if (response.equals(null) || response == null|| response.length()==0 && count==0)
//                            {
//
//                                ArrayList list = new ArrayList();
//                                list.add("Select Option");
//                                style.setEnabled(false);
//                                adapter2 = new ArrayAdapter<String>(StyleActivityZxing.this, android.R.layout.simple_spinner_item, list);
//                                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                                style.setAdapter(adapter2);
//                                Reusable_Functions.hDialog();
//                                Toast.makeText(StyleActivityZxing.this, "No options data found", Toast.LENGTH_LONG).show();
//                            }
//                            else if(response.length()==limit)
//                            {
//                                Log.i(" Response length", ""+response.length());
//                                Log.e("offsetvalue", "" + offsetvalue);
//                                Log.e("limit", "" + limit);
//                                Log.e("count", "" + count);
//
//                                for (int i = 0; i < response.length(); i++) {
//                                    JSONObject jsonResponse = response.getJSONObject(i);
//                                    String articleOptions = jsonResponse.getString("articleOptions");
//                                    articleOptionList.add(articleOptions);
//                                }
//
//                                offsetvalue = (limit * count) + limit ;
//                                count++;
//                                requestArticleOptionsAPI(collectionNM, offsetvalue, limit);
//
//                            }else if(response.length()<limit)
//                            {
//                                Log.i(" Response length", ""+response.length());
//                                for (int i = 0; i < response.length(); i++) {
//                                    JSONObject jsonResponse = response.getJSONObject(i);
//                                    String articleOptions = jsonResponse.getString("articleOptions");
//                                    //   Log.e("articleOptions  :", "   " + articleOptions);
//                                    articleOptionList.add(articleOptions);
//                                }
//
//                                Reusable_Functions.hDialog();
//
//                                Log.e("articleOptionList", articleOptionList.size() + "");
//
//                                Collections.sort(articleOptionList);
//                                articleOptionList.add(0,"Select Option");
//                                style.setEnabled(true);
//                                adapter2 = new ArrayAdapter<String>(StyleActivityZxing.this, android.R.layout.simple_spinner_item, articleOptionList);
//                                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                                style.setAdapter(adapter2);
//
//                                if(seloptionName == null || seloptionName.equals(null))
//                                {
//                                 style.setSelection(0);
//                                }
//                                else
//                                {
//                                    if(articleOptionList.contains(seloptionName))
//                                    {
//                                        style.setSelection(articleOptionList.indexOf(seloptionName));
//                                        return;
//                                    }
//                                    else
//                                    {
//                                        style.setSelection(0);
//
//                                    }
//
//                                }
//
//
////                                style.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
////                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                                        optionName = parent.getSelectedItem().toString().trim();
////                                        Log.e("optionName "," "+optionName);
////
////                                        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
////                                        if(inputManager != null){
////                                            inputManager.hideSoftInputFromWindow(style.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
////                                        }
////
////
////                                        if (optionName.equalsIgnoreCase("Select option")) {
////                                            //Toast.makeText(StyleActivity.this,"Please select Collection",Toast.LENGTH_LONG).show();
////                                        } else {
////                                            if (Reusable_Functions.chkStatus(context)) {
////                                                Reusable_Functions.hDialog();
////                                                Reusable_Functions.sDialog(context, "Loading  data...");
////                                                optionName = (String) parent.getItemAtPosition(position);
////                                                Log.e("select item", optionName);
////
////                                                requestStyleDetailsAPI(optionName, "optionname");
////
////                                            } else {
////                                                // Reusable_Functions.hDialog();
////                                                Toast.makeText(StyleActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
////                                            }
////
////                                        }
////                                    } // to close the onItemSelected
////
////                                    public void onNothingSelected(AdapterView<?> parent) {
////
////                                        View view = getCurrentFocus();
////                                        if (view != null) {
////                                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
////                                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
////                                        }
////                                    }
////                                });
//
//                            }
//
//
//
//
//                           // }
//
//                        }
//                        catch(Exception e)
//                        {
//                            Log.e("Exception e",e.toString() +"");
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error)
//                    {
//                        Reusable_Functions.hDialog();
//                        // Toast.makeText(LoginActivity.this,"Invalid User",Toast.LENGTH_LONG).show();
//                        error.printStackTrace();
//                    }
//                }
//
//        ){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError
//            {
//                // String auth_code = "Basic " + Base64.encodeToString((uname+":"+password).getBytes(), Base64.NO_WRAP); //Base64.NO_WRAP flag
//                // Log.i("Auth Code", auth_code);
//                Map<String, String> params = new HashMap<>();
//                params.put("Content-Type", "application/json");
//                params.put("Authorization", "Bearer "+bearertoken);
//                return params;
//
//
//            }
//        };
//        int socketTimeout = 60000;//5 seconds
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        postRequest.setRetryPolicy(policy);
//        queue.add(postRequest);
//
//
//    }
//
//    private void requestCameraPermission() {
//        Log.i("", "CAMERA permission has NOT been granted. Requesting permission.");
//
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                Manifest.permission.CAMERA)) {
//            Log.i("",
//                    "Displaying camera permission rationale to provide additional context.");
////            Snackbar.make(findViewById(R.id.coordinator_layout), R.string.permission_camera_rationale,
////                    Snackbar.LENGTH_INDEFINITE)
////                    .setAction("OK", new View.OnClickListener() {
////                        @Override
////                        public void onClick(View view) {
//                            ActivityCompat.requestPermissions(StyleActivityZxing.this,
//                                    new String[]{Manifest.permission.CAMERA},
//                                    REQUEST_CAMERA);
////                        }
////                    })
////                    .show();
//        } else {
//
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
//                    REQUEST_CAMERA);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           @NonNull String permissions[],
//                                           @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_CAMERA: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    new ZxingOrient(StyleActivityZxing.this).initiateScan();
//
//                } else {
//
//                    finish();
//                }
//            }
//            break;
//        }
//    }
//
//
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        selcollectionName = null ; seloptionName = null;
//        Intent intent=new Intent(StyleActivityZxing.this,DashBoardActivity.class);
//        startActivity(intent);
//        finish();
//
//    }
//}
