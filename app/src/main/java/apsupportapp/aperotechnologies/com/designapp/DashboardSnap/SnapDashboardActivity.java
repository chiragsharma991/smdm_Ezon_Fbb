package apsupportapp.aperotechnologies.com.designapp.DashboardSnap;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.UUID;

import apsupportapp.aperotechnologies.com.designapp.AboutUsActivity;
import apsupportapp.aperotechnologies.com.designapp.Constants;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;

import apsupportapp.aperotechnologies.com.designapp.DB_operation.DatabaseHandler;
import apsupportapp.aperotechnologies.com.designapp.FCM.ContCreateTokenService;
import apsupportapp.aperotechnologies.com.designapp.FCM.FetchNewRefreshToken;
import apsupportapp.aperotechnologies.com.designapp.FCM.TokenRefresh;
import apsupportapp.aperotechnologies.com.designapp.HorlyAnalysis.ProductNameBean;
import apsupportapp.aperotechnologies.com.designapp.HourlyPerformence.HourlyAdapter;
import apsupportapp.aperotechnologies.com.designapp.Login.LoginActivity;
import apsupportapp.aperotechnologies.com.designapp.LoginActivity1;
import apsupportapp.aperotechnologies.com.designapp.MySingleton;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.RecyclerItemClickListener;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesAnalysisActivity1;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesFilterActivity;
import apsupportapp.aperotechnologies.com.designapp.model.EtlStatus;
import apsupportapp.aperotechnologies.com.designapp.model.Login_StoreList;

import static apsupportapp.aperotechnologies.com.designapp.R.id.concept;
import static apsupportapp.aperotechnologies.com.designapp.R.id.listView;

public class SnapDashboardActivity extends SwitchingActivity implements onclickView {

    public static RecyclerView Recycler_verticalView;
    private String TAG = "SnapDashboardActivity";
    private Context context;
    public static NestedScrollView nestedScrollview;
    RequestQueue queue;
    String userId, bearertoken, geoLeveLDesc, pushtoken, lobName;
    SharedPreferences sharedPreferences;
    ArrayList<String> arrayList, eventUrlList;
    MySingleton m_config;
    ArrayList<ProductNameBean> productNameBeanArrayList;
    //Event ViewPager
    ViewPager pager;
    PagerAdapter adapter;
    ImageView imgdot;
    LinearLayout li;
    Timer timer;
    int page = 0;
    //variable for storing collection list in style activity in searchable spinner
    public static List _collectionitems;
    private Gson gson;
    private EtlStatus etlStatus;
    private ArrayList<EtlStatus> etlStatusList;
    private Snackbar snackbar;
    private TextView RefreshTime, concept_txt, lob_name_txt;
    public static SnapAdapter snapAdapter;
    public static boolean tokenProcess = false;
    private DatabaseHandler db;
    private AlertDialog dialog;
    private boolean[] lobchecked, conceptchecked;
    private View viewpart;
    private RecyclerView lobList;
    private ArrayList<String> lobData = null, conceptData = null ,conceptDesc=null;

  /*  001, 002, 003, 004, 005, 006, 007, 008, 009,010, 011, 012, 013, 014, 015, 016, 017, 018,
            020. 021,  022, 023, 026, 027, 028*/

    /* Product Info,
    Visual Asst,  Visual Assrt Report,
     Sales, Sales PVA, Freshness Index, Option Eff,
     Skewed Size, Best/Worst Per, Stock Ageing,
     Floor Avl, Target Stock Exc, Sell Thru Exc,
     Running Promo, Upcoming Promo, Expiring Promo, Best/Worst Promo, Key Product PVA, Stock Transfer, Stock Transfer Status, Best Worst Feedback, Best Worst Feedback List, Season Catalogue, Customer Eng, Hourly Performance
 */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate: SnapDashboardActivity");
        super.onCreate(savedInstanceState);
        context = this;
        statusbar();
        m_config = MySingleton.getInstance(context);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLeveLDesc = sharedPreferences.getString("conceptDesc", "");
        lobName = sharedPreferences.getString("lobname", "");
        pushtoken = sharedPreferences.getString("push_tokken", "");
        String[] kpiIdArray = getIntent().getStringArrayExtra("kpiId");
        Log.e(TAG, "onCreate: kpi id" + kpiIdArray.length);
        //  String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.e("SnapDashboard", "Refreshed token:------ " + refreshedToken);
        Log.e(TAG, "userId :--" + userId);
        Log.e(TAG, "pushtoken :--" + pushtoken.toString());
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();
        if (userId.equals("")) {
            Intent intent = new Intent(this, LoginActivity1.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_snap_dashboard);
        initalise();
        eventUrlList = new ArrayList<>();

        if (geoLeveLDesc.equals("E ZONE")) {
            Log.e("TAG", "Ezone login");
            loginFromFbb = false;
            if (Reusable_Functions.chkStatus(context)) {
                Reusable_Functions.hDialog();
                Reusable_Functions.sDialog(context, "Loading events...");
                RefreshTimeAPI();
            } else {
                Toast.makeText(SnapDashboardActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
            }
        }

        //-------------------------------------------------------------//

        else {
            Log.e("TAG", "FBB login");
            loginFromFbb = true;
            _collectionitems = new ArrayList();
            arrayList = new ArrayList<>();
            productNameBeanArrayList = new ArrayList<>();
            //  initialize_fbb_ui();
            //Marketing events API
            if (Reusable_Functions.chkStatus(context)) {
                Reusable_Functions.hDialog();
                Reusable_Functions.sDialog(context, "Loading events...");
                requestMarketingEventsAPI();
                RefreshTimeAPI();

            } else {
                Toast.makeText(SnapDashboardActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
            }
        }
        //  checkPermission();
        // Arrays.asList(kpiIdArray);


        setupAdapter(Arrays.asList(kpiIdArray));

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getString("from").equals("feedback")) {

                nestedScrollview.post(new Runnable() {
                    @Override
                    public void run() {
                        nestedScrollview.fullScroll(View.FOCUS_DOWN);
                    }
                });


            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e(TAG, "onResume: device id: " + device_id + "  and token: " + FirebaseInstanceId.getInstance().getToken());
        if (!tokenProcess) {
            if (TokenRefresh.pushToken != null && !device_id.equals("") && device_id != null)
                requestSubmitAPI(context, getObject());
            Log.e(TAG, "onResume: !tokenProcess");
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("device_id", device_id);
        editor.apply();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(TAG, "onNewIntent: ");
    }

    private void statusbar() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.ezfbb_status_bar));
        }
    }

    private void initalise() {
        db = new DatabaseHandler(context);
        viewpart = findViewById(android.R.id.content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Recycler_verticalView = (RecyclerView) findViewById(R.id.recycler_verticalView);
        RefreshTime = (TextView) findViewById(R.id.refreshTime);
        lob_name_txt = (TextView) findViewById(R.id.lob_name);
        concept_txt = (TextView) findViewById(R.id.concept);
        lob_name_txt.setText(lobName);
        concept_txt.setText(geoLeveLDesc);
        nestedScrollview = (NestedScrollView) findViewById(R.id.nestedScrollview);
        Recycler_verticalView.setNestedScrollingEnabled(false);
        Recycler_verticalView.setLayoutManager(new LinearLayoutManager(this));
        Recycler_verticalView.setHasFixedSize(true);


        //setupAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            // db.deleteAllData();
            SalesFilterActivity.level_filter = 1;
            //  SalesAnalysisActivity1.selectedsegValue = null;
            SalesAnalysisActivity1.level = 1;
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notifManager.cancelAll();
            return true;
        } else if (id == R.id.mapping) {

            selectConceptNLob();


        } else if (id == R.id.aboutus) {
            Intent intent = new Intent(this, AboutUsActivity.class);
            startActivity(intent);
//            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void selectConceptNLob() {
        lobData=null;conceptData=null;
        List<Login_StoreList> list = db.db_GetAllContacts();
        conceptData = new ArrayList<>();
        conceptDesc = new ArrayList<>();
        for (Login_StoreList data : list) {
            conceptData.add(data.getGeoLevel2Code());
            conceptDesc.add(data.getGeoLevel2Desc());
            // lobData.add(data.getLobName());
        }
        Set<String> set = new HashSet<>();
        set.addAll(conceptData);
        conceptData.clear();
        conceptData.addAll(set);  // remove dublicate values from list
       // customAlert(conceptData);

        set = new HashSet<>();
        set.addAll(conceptDesc);
        conceptDesc.clear();
        conceptDesc.addAll(set);  // remove dublicate values from list
        customAlert(conceptDesc);


    }

    private void customAlert(final ArrayList<String> conceptDesc) {

        final Dialog dialog = new Dialog(context, R.style.ThemeDialog);
        dialog.getWindow().getAttributes().windowAnimations = R.style.ThemeDialog;
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dashboard_dropdown);
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        lobList = (RecyclerView) dialog.findViewById(R.id.lobList);
        final RecyclerView conceptList = (RecyclerView) dialog.findViewById(R.id.conceptList);
        RelativeLayout qfDoneLayout = (RelativeLayout) dialog.findViewById(R.id.qfDoneLayout);
        final TextView txt_incorrect = (TextView) dialog.findViewById(R.id.txt_incorrect);
        txt_incorrect.setVisibility(View.GONE);
        conceptchecked = new boolean[conceptDesc.size()];
        for (int i = 0; i < conceptDesc.size(); i++) {
            conceptchecked[i] = false;
        }

        conceptList.setLayoutManager(new LinearLayoutManager(context));
        conceptList.setLayoutManager(new LinearLayoutManager(conceptList.getContext(), LinearLayoutManager.VERTICAL, false));
        final ConceptMappingAdapter conceptMappingAdapter = new ConceptMappingAdapter(conceptDesc, context, conceptchecked);
        conceptList.setAdapter(conceptMappingAdapter);

        conceptList.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public  void onItemClick(View view, int position) {

                for (int i = 0; i < conceptDesc.size(); i++) {
                    if (position == i) conceptchecked[i] = true;
                    else conceptchecked[i] = false;

                }

                List<Login_StoreList> list = db.db_GetListWhereClause(conceptData.get(position));
                lobData = new ArrayList<>();
                for (Login_StoreList data : list) {
                    lobData.add(data.getLobName());
                }
                Set<String> set = new HashSet<>();
                set.addAll(lobData);
                lobData.clear();
                lobData.addAll(set);  // remove dublicate values from list
                displaylobName(lobData);

                conceptMappingAdapter.notifyDataSetChanged();

            }
        }));

        qfDoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_incorrect.setVisibility(View.GONE);
                String selectconcept = null;
                String selectLob = null;

                if (lobData == null || conceptData == null) {
                    txt_incorrect.setText("Please select Concept first");
                    txt_incorrect.setVisibility(View.VISIBLE);
                    //Reusable_Functions.showSnackbarError(context,viewpart,"Please select both entries");
                    //dialog.dismiss();
                    return;
                }

                for (int i = 0; i < lobchecked.length; i++) {
                    if (lobchecked[i]) {
                        selectLob = lobData.get(i);
                    }
                }
                for (int i = 0; i < conceptchecked.length; i++) {
                    if (conceptchecked[i]) {
                        selectconcept = conceptData.get(i);
                    }
                }

                if (selectconcept == null || selectLob == null) {
                    txt_incorrect.setText("Please select Lob name");
                    txt_incorrect.setVisibility(View.VISIBLE);
                    //Reusable_Functions.showSnackbarError(context,viewpart,"Please select both entries");
                    //dialog.dismiss();
                    return;
                }

                Login_StoreList model = db.db_GetOneRowDetails(selectLob);
                Reusable_Functions.showSnackbar(viewpart, "Mapping success !");
                lob_name_txt.setText(selectLob);
                concept_txt.setText(model.getGeoLevel2Desc());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("concept", model.getGeoLevel2Code());
                editor.putString("conceptDesc", model.getGeoLevel2Desc());
                editor.putString("lobid", model.getLobId());
                editor.putString("lobname", model.getLobName());
                editor.putString("kpi_id", model.getKpiId());
                editor.apply();

                String kpi_id = model.getKpiId();
                String[] selectKpiID = kpi_id.split(",");
                setupAdapter(Arrays.asList(selectKpiID));
                if (Reusable_Functions.chkStatus(context)) {
                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading events...");
                    requestMarketingEventsAPI();
                } else {
                    Toast.makeText(SnapDashboardActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
                return;


            }
        });

        dialog.show();

    }

    private  void displaylobName(final ArrayList<String> lobData) {

        lobchecked = new boolean[lobData.size()];
        for (int i = 0; i < lobData.size(); i++) {
            lobchecked[i] = false;
        }
        lobList.setLayoutManager(new LinearLayoutManager(context));
        lobList.setLayoutManager(new LinearLayoutManager(lobList.getContext(), LinearLayoutManager.VERTICAL, false));
        final LobMappingAdapter lobMappingAdapter = new LobMappingAdapter(lobData, context, lobchecked);
        lobList.setAdapter(lobMappingAdapter);

        lobList.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                for (int i = 0; i < lobData.size(); i++) {
                    if (position == i) lobchecked[i] = true;
                    else lobchecked[i] = false;
                }
                lobMappingAdapter.notifyDataSetChanged();

            }
        }));
    }


    private void setupAdapter(List<String> kpiIdArray) {

       /* Mapping
        001 - Product Info
        002 - Visual Assortment
        003 - Visual Assortment Report
        004 - Sales
        005 - Sales PvA
        006 - Freshness Index
        007 - Option Efficiency
        008 - Skewed Sizes
        009 - Best/Worst performers
        010 - Stock Ageing
        011 - Floor availability
        012 - Target stock exception
        013 - Sell Thru exception
        014 - Running promo
        015 - Upcoming promo
        016 - Expiring promo
        017 - Best/worst promo
        018 - Key products PvA
        019 - Key products hourly
        020 - Collaboration to do
        021 - Collaboration status
        022 - Feedback
        023 - Feedback list
        024 - Store inspection new
        025 - Store inspection history
        026 - Season catalogue
        027 - Customer loyalty
        028 - Hourly performance
        029 - BORIS
        030 - Customer Feedback : Product Availability & Notify
        031 - Customer Feedback : Policy Exchange,Refund
        032 - Customer Feedback : Price & Promotion
        033 - Customer Feedback : Product Quality & Range
        034 - Customer Feedback : Our Store Services
        035 - Customer Feedback : Supervisor & Staff*/

        snapAdapter = new SnapAdapter(context, eventUrlList);

        if (geoLeveLDesc.equals("E ZONE")) {
    /*        for (int i = 0; i <kpiIdArray.size(); i++) {
                Log.i(TAG, "kpiIdArray:"+kpiIdArray.get(i).toString() );
                switch (kpiIdArray.get(i)){

                    case "004":
                        List<App> apps = getProduct(21);
                        snapAdapter.addSnap(new Snap(Gravity.START, "Sales", apps));
                        break;

                    case "006":
                        apps = getProduct(22);
                        snapAdapter.addSnap(new Snap(Gravity.START, "Inventory", apps));
                        break;

                   default:
                      // finish();
                       break;


                }

            }
            */

        } else {

            for (int i = 0; i < kpiIdArray.size(); i++) {
                Log.i(TAG, "kpiIdArray:" + kpiIdArray.get(i).toString());
                switch (kpiIdArray.get(i)) {

                    case "001":
                        List<App> apps = getProduct(0);
                        snapAdapter.addSnap(new Snap(Gravity.START, "Product Information", apps));
                        break;

                    case "002":
                        apps = getProduct(1);
                        snapAdapter.addSnap(new Snap(Gravity.START, "Visual Assortment", apps));
                        break;

                    case "004":
                        apps = getProduct(2);
                        snapAdapter.addSnap(new Snap(Gravity.START, "Sales", apps));
                        break;

                    case "006":
                        apps = getProduct(3);
                        snapAdapter.addSnap(new Snap(Gravity.START, "Inventory", apps));
                        break;

                    case "014":
                        apps = getProduct(4);
                        snapAdapter.addSnap(new Snap(Gravity.START, "Promo Analysis", apps));
                        break;

                    case "020":
                        apps = getProduct(5);
                        snapAdapter.addSnap(new Snap(Gravity.START, "Collaboration", apps));
                        break;

                    case "022":
                        apps = getProduct(6);
                        snapAdapter.addSnap(new Snap(Gravity.START, "Product Feedback", apps));
                        break;

                    case "026":
                        apps = getProduct(7);
                        snapAdapter.addSnap(new Snap(Gravity.START, "Season Catalogue", apps));
                        break;

                    case "027":
                        apps = getProduct(8);
                        snapAdapter.addSnap(new Snap(Gravity.START, "Customer Engagement", apps));
                        break;

                    // hourly has been shifted in sales module
                /*    case "028":
                        apps = getProduct(9);
                        snapAdapter.addSnap(new Snap(Gravity.START, "Hourly performance", apps));
                        break;*/

                    case "029":
                        apps = getProduct(10);
                        snapAdapter.addSnap(new Snap(Gravity.START, "BORIS", apps));
                        break;

                    case "030":
                        apps = getProduct(11);
                        snapAdapter.addSnap(new Snap(Gravity.START, "Customer Feedback", apps));
                        break;

                    default:
                        // finish();
                        break;


                }

            }
        }
        Recycler_verticalView.setAdapter(snapAdapter);
    }


    @Override
    public void onclickView(int group_position, int child_position, String kpiID) {
        Log.e(TAG, "group_position: " + group_position + "child_position" + child_position + " tag is" + kpiID);
        int value = Integer.parseInt("" + group_position + "" + child_position);
        moveTo(kpiID, context);
    }


    private void RefreshTimeAPI() {
        String url = ConstsCore.web_url + "/v1/display/etlstatus/" + userId;
        Log.e("Refreshtime Url :", "" + url);
        etlStatusList = new ArrayList<EtlStatus>();

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("refresh time response :", "" + response);
                        try {
                            if (response == null || response.equals("")) {

                                RefreshTime.setText("N/A");

                            } else {
                                for (int i = 0; i < response.length(); i++) {
                                    etlStatus = gson.fromJson(response.get(i).toString(), EtlStatus.class);
                                    etlStatusList.add(etlStatus);

                                }
                                RefreshTime.setText(etlStatusList.get(1).getLastETLDate());

                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();

                            RefreshTime.setText("N/A");

                            e.printStackTrace();
                        }
                        Reusable_Functions.hDialog();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        RefreshTime.setText("N/A");
                        error.printStackTrace();
                        Reusable_Functions.hDialog();

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermission() {
        boolean checkDeviceId = sharedPreferences.getString("device_id", "").equals("") ? true : false;   //true means you not get any device id.

        if (checkDeviceId) {

            if (Reusable_Functions.checkPermission(android.Manifest.permission.READ_PHONE_STATE, this)) {
                Log.e("TAG", ":check permission is okk");
                getDeviceId();
            } else {
                Log.e("TAG", ":check permission calling");
                requestPermissions(new String[]{android.Manifest.permission.READ_PHONE_STATE}, Constants.REQUEST_PERMISSION_WRITE_STORAGE);
            }
        }
    }

    private void getDeviceId() {

        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(this.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        Log.e("TAG", "tmDevice: " + tmDevice + "tm serial" + tmSerial + "android id" + androidId);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();
        Log.e("TAG", "deviceId: " + deviceId);

        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("device_id", deviceId);
        edit.apply();
        requestSubmitAPI(context, getObject());

    }

    public JSONObject getObject() {
        String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("deviceId", device_id);
            jsonObject.put("pushToken", TokenRefresh.pushToken);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "jsonobject: " + jsonObject.toString());

        return jsonObject;
    }


    private void requestSubmitAPI(final Context mcontext, JSONObject object)  // Sender Submit Api call
    {
        if (Reusable_Functions.chkStatus(mcontext)) {
            String url = ConstsCore.web_url + "/v1/submit/deviceID/" + userId;
            Log.e(TAG, "requestSubmitAPI: " + url);
            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, object.toString(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e(TAG, "onResponse: from token" + response);
                            try {
                                if (response == null || response.equals("")) {
                                    Log.e(TAG, "onResponse token: null");
                                } else {
                                    String result = response.getString("status");
                                    tokenProcess = true;
                                    Log.e(TAG, "onResponse token: success " + result);

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
                            Toast.makeText(context, "server not responding...", Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization", bearertoken);
                    return params;
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }

            };
            int socketTimeout = 60000;//5 seconds
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            postRequest.setRetryPolicy(policy);
            queue.add(postRequest);

        } else {
            Toast.makeText(context, "Please check network connection...", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.REQUEST_PERMISSION_WRITE_STORAGE) {
            Log.e("TAG", "onRequestPermissionsResult: " + grantResults[0]);
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Log.e("TAG", "onRequestPermissionsResult: Granted");
                getDeviceId();


            } else {

                Log.e("TAG", "onRequestPermissionsResult: Declined");
                boolean should = ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.READ_PHONE_STATE);
                Log.e(TAG, "oncheck permission.. " + should);
                if (should) {
                    showAlert();
                } else {
                    View view = findViewById(android.R.id.content);
                    snackbar = Snackbar.make(view, Constants.REQUEST_PERMISSION_SNACKALERT, Snackbar.LENGTH_SHORT).setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.setActionTextColor(getResources().getColor(R.color.smdm_actionbar));
                    snackbar.show();
                }
            }
        }
    }

    private void showAlert() {
        //user denied without Never ask again, just show rationale explanation
        new android.app.AlertDialog.Builder(context)
                .setTitle("Permission Denied")
                .setMessage(Constants.REQUEST_PERMISSION_ALERT)
                .setPositiveButton("RE-TRY", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("Click of I m sure", ", permission request Retry");
                        requestPermissions(new String[]{android.Manifest.permission.READ_PHONE_STATE}, Constants.REQUEST_PERMISSION_WRITE_STORAGE);

                    }
                })
                .setNegativeButton("I'M SURE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        Log.e("Click of I m sure", ", permission request is denied");

                    }
                })
                .show();
    }

    private void requestMarketingEventsAPI() {

        geoLeveLDesc = sharedPreferences.getString("concept", "");
        String url = ConstsCore.web_url + "/v1/display/dashboardNew/" + userId + "?geoLevel2Code=" + geoLeveLDesc;
        Log.e(TAG, "requestMarketingEventsAPI: " + url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, "Event Response: " + response);

                        try {
                            if (response.equals("") || response == null || response.length() == 0) {
                                Log.e(TAG, "Event Response: null");
                                Reusable_Functions.hDialog();
                                // Toast.makeText(SnapDashboardActivity.this, "No data found", Toast.LENGTH_LONG).show();
                            } else {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonOject = response.getJSONObject(i);
                                    String imageURL = jsonOject.getString("imageName");
                                    eventUrlList.add(imageURL);
                                    // setupAdapter();
                                    snapAdapter.notifyDataSetChanged();
                                    Reusable_Functions.hDialog();
                                }
                            }

                        } catch (Exception e) {
                            Reusable_Functions.hDialog();
                            e.printStackTrace();
                            Log.e(TAG, "Event catch: " + e.getMessage());
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

    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }


}
