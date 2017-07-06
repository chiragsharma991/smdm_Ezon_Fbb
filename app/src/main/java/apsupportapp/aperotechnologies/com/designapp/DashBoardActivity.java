package apsupportapp.aperotechnologies.com.designapp;

import android.*;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import apsupportapp.aperotechnologies.com.designapp.BestPerformersInventory.BestPerformerInventory;
import apsupportapp.aperotechnologies.com.designapp.BestPerformersPromo.BestPerformerActivity;
import apsupportapp.aperotechnologies.com.designapp.Collaboration.Status.StatusActivity;
import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.To_Do;
import apsupportapp.aperotechnologies.com.designapp.CustomerLoyalty.CustomerLookupActivity;
import apsupportapp.aperotechnologies.com.designapp.ExpiringPromo.ExpiringPromoActivity;
import apsupportapp.aperotechnologies.com.designapp.Feedback.Feedback;
import apsupportapp.aperotechnologies.com.designapp.Feedback.FeedbackList;
import apsupportapp.aperotechnologies.com.designapp.FloorAvailability.FloorAvailabilityActivity;
import apsupportapp.aperotechnologies.com.designapp.FreshnessIndex.FreshnessIndexActivity;
import apsupportapp.aperotechnologies.com.designapp.HorlyAnalysis.KeyProductActivity;
import apsupportapp.aperotechnologies.com.designapp.HorlyAnalysis.ProductNameBean;
import apsupportapp.aperotechnologies.com.designapp.HourlyPerformence.HourlyPerformence;
import apsupportapp.aperotechnologies.com.designapp.KeyProductPlan.KeyProductPlanActivity;
import apsupportapp.aperotechnologies.com.designapp.MPM.mpm_activity;
import apsupportapp.aperotechnologies.com.designapp.OptionEfficiency.OptionEfficiencyActivity;
import apsupportapp.aperotechnologies.com.designapp.ProductInformation.StyleActivity;
import apsupportapp.aperotechnologies.com.designapp.PvaSalesAnalysis.SalesPvAActivity;
import apsupportapp.aperotechnologies.com.designapp.RunningPromo.RunningPromoActivity;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesAnalysisActivity1;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesFilterActivity;
import apsupportapp.aperotechnologies.com.designapp.SellThruExceptions.SaleThruInventory;
import apsupportapp.aperotechnologies.com.designapp.SkewedSize.SkewedSizesActivity;
import apsupportapp.aperotechnologies.com.designapp.StockAgeing.StockAgeingActivity;
import apsupportapp.aperotechnologies.com.designapp.StoreInspection.InspectionBeginActivity;
import apsupportapp.aperotechnologies.com.designapp.StoreInspection.InspectionHistoryActivity;
import apsupportapp.aperotechnologies.com.designapp.TargetStockExceptions.TargetStockExceptionActivity;
import apsupportapp.aperotechnologies.com.designapp.UpcomingPromo.UpcomingPromo;
import apsupportapp.aperotechnologies.com.designapp.VisualAssortmentSwipe.VisualAssortmentActivity;
import apsupportapp.aperotechnologies.com.designapp.VisualAssortmentSwipe.VisualReportActivity;
import apsupportapp.aperotechnologies.com.designapp.model.EtlStatus;


public class DashBoardActivity extends AppCompatActivity
        implements View.OnClickListener {

    ImageButton imageBtnStyle, imageBtnKeyProducts, imgBtnSales, imgBtnVisualAssortment, ActualKeyProduct;
    ImageButton btnFloorAvailability, btnTargetStockExcep, btnSellThruExcep, btnVisualReport;
    ImageButton imgBtnPvaAnalysis, imgBtnRunningPromo, BtnUpcomingpromo, BtnExpiringpromo, BtnBestWorstpromo, btnBestPerformersInv;
    ImageButton btnFeshnessindex, BtnOnlyWorstpromo, btnOptionEfficiency, To_do_image_button, Status_image_button,
            btnSkewedSize, btnCutSize, btnStockAgeing, BtnWorstPerformers, FeedbackList_btn, Feedback_btn, btn_inspection_begin, btn_inspection_history, btn_mpm, btn_cust_loyalty, Btn_hourly_performer;


    LinearLayout hourlyFlash, productInfo, visualAssort, hourly_performance_view, sales, promoAnalysis, inventory, linplanactual, Collaboration_subView, Feedback_linear, inspection_linear, Mpm_linear, linear_cust_loyalty;
    TextView hourlyFlashTxt, productInfoTxt, visualAssortTxt, salesTxt, hourly_performer_Title, promoAnalysisTxt, inventoryTxt, RefreshTime, planvsActualtxt, Collaboration, Feedback,
            txt_store_Inspection, txt_mpm, txt_cust_loyalty;
    // Ezone Ui Declaration
    TextView txt_ezone_sales, txt_ezone_inventory, txt_ezone_refresh_time, txt_ez_cust_loyalty;
    ImageButton btn_ezone_sales, btn_ezone_AssortmentAnalysis, btn_ezone_best_worst, btn_ez_cust_loyalty;
    LinearLayout linear_ezone_sales, linear_ezone_inventory, linear_ez_cust_loyalty;

    EventAdapter eventAdapter;
    String hrflash = "NO", pdInfo = "NO", vsAssort = "NO", sAles = "NO", pmAnalysis = "NO", inVENtory = "NO", planActual = "NO", Collab = "NO",
            feedback_flag = "NO", store_inspection = "NO", mpm = "NO", cust_loyalty = "NO", hourly_performance = "NO";
    String TAG = "DashBoardActivity";


    private String str_ezone_sales = "NO", str_ezone_inv = "NO", str_ezone_cust_loyalty = "NO";
    RequestQueue queue;
    String userId, bearertoken, geoLeveLDesc;
    SharedPreferences sharedPreferences;
    ArrayList<String> arrayList, eventUrlList;
    Context context;
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
    private boolean ezone_sales = false, ezone_cust_loyalty = false;
    private boolean ezone_inventory = false;
    private boolean Promo = false;
    private boolean HourlyFlash = false;
    private boolean ProductInfo = false;
    private boolean hourly_performer_flag = false;
    private boolean VisualAssort = false;
    private boolean Sales = false;
    private boolean Inventory = false;
    private boolean PlanActual = false;
    private boolean Collab_bool = false;
    private boolean feedback_bool = false;
    private boolean inspection_flag = false;
    private boolean mpm_flag = false, custloylty_flag = false, hourlyperform_flag = false;
    private Gson gson;
    private EtlStatus etlStatus;
    private ArrayList<EtlStatus> etlStatusList;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Log.e(TAG, "Oncreate: Dashboard..");
        m_config = MySingleton.getInstance(context);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLeveLDesc = sharedPreferences.getString("geoLeveLDesc", "");
        Log.e("geoLeveLDesc :", "" + geoLeveLDesc);
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();

        if (geoLeveLDesc.equals("E ZONE")) {
            Log.e("ezone layout", "");
            setContentView(R.layout.activity_ezone_dashboard);
            initialize_ezone_ui();
        } else {
            Log.e("fbb layout", "");
            setContentView(R.layout.activity_dashboard);
            _collectionitems = new ArrayList();
            arrayList = new ArrayList<>();
            eventUrlList = new ArrayList<>();
            productNameBeanArrayList = new ArrayList<>();
            initialize_fbb_ui();
            //Marketing events API
            if (Reusable_Functions.chkStatus(context)) {
                Reusable_Functions.hDialog();
                Reusable_Functions.sDialog(context, "Loading events...");
                requestMarketingEventsAPI();
            } else {
                Toast.makeText(DashBoardActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
            }
        }
        RefreshTimeAPI();
        checkPermission();

    }

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

    private void initialize_ezone_ui() {
        txt_ezone_refresh_time = (TextView) findViewById(R.id.txt_ezone_refresh_time);
        txt_ezone_sales = (TextView) findViewById(R.id.ezone_headersales);
        hourly_performer_Title = (TextView) findViewById(R.id.txt_hourly_ezperformer_);
        txt_ez_cust_loyalty = (TextView) findViewById(R.id.txt_ez_cust_loyalty);
        txt_ezone_inventory = (TextView) findViewById(R.id.ezone_header_inventory);
        btn_ezone_sales = (ImageButton) findViewById(R.id.btn_ezone_Sales);
        btn_ezone_AssortmentAnalysis = (ImageButton) findViewById(R.id.btn_ezone_AssortmentAnalysis);
        btn_ezone_best_worst = (ImageButton) findViewById(R.id.btn_ezone_best_worst);
        btn_ez_cust_loyalty = (ImageButton) findViewById(R.id.btn_ez_cust_loyalty);
        Btn_hourly_performer = (ImageButton) findViewById(R.id.btn_hourly_ezperformer);
        linear_ezone_sales = (LinearLayout) findViewById(R.id.linear_ezone_sales);
        linear_ezone_inventory = (LinearLayout) findViewById(R.id.linear_ezone_inventory);
        linear_ez_cust_loyalty = (LinearLayout) findViewById(R.id.linear_ez_cust_loyalty);
        hourly_performance_view = (LinearLayout) findViewById(R.id.linear_hourly_ezperformer_view_);
        txt_ezone_sales.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
        txt_ezone_inventory.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
        txt_ez_cust_loyalty.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
        hourly_performer_Title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
        txt_ezone_sales.setOnClickListener(this);
        txt_ezone_inventory.setOnClickListener(this);
        txt_ez_cust_loyalty.setOnClickListener(this);
        hourly_performer_Title.setOnClickListener(this);
        btn_ezone_best_worst.setOnClickListener(this);
        btn_ezone_AssortmentAnalysis.setOnClickListener(this);
        btn_ezone_sales.setOnClickListener(this);
        Btn_hourly_performer.setOnClickListener(this);
        btn_ez_cust_loyalty.setOnClickListener(this);
    }

    private void initialize_fbb_ui() {

        hourlyFlashTxt = (TextView) findViewById(R.id.headersmdm);
        productInfoTxt = (TextView) findViewById(R.id.productinfo);
        visualAssortTxt = (TextView) findViewById(R.id.visualAssort);
        salesTxt = (TextView) findViewById(R.id.headersales);
        hourly_performer_Title = (TextView) findViewById(R.id.txt_hourly_performer_);

        promoAnalysisTxt = (TextView) findViewById(R.id.headerpromo);
        inventoryTxt = (TextView) findViewById(R.id.headerinvent);
        RefreshTime = (TextView) findViewById(R.id.refreshTime);
        planvsActualtxt = (TextView) findViewById(R.id.headerplanactual);
        Collaboration = (TextView) findViewById(R.id.collaboration);
        Feedback = (TextView) findViewById(R.id.feedback);
        txt_store_Inspection = (TextView) findViewById(R.id.storeInspection);
        txt_mpm = (TextView) findViewById(R.id.mpm);
        txt_cust_loyalty = (TextView) findViewById(R.id.txt_cust_loyalty);

        hourlyFlashTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
        productInfoTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
        visualAssortTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
        salesTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
        hourly_performer_Title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
        promoAnalysisTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
        inventoryTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
        planvsActualtxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
        inventoryTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
        inventoryTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
        Collaboration.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
        Feedback.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
        txt_store_Inspection.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
        txt_mpm.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
        txt_cust_loyalty.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);

        hourlyFlash = (LinearLayout) findViewById(R.id.lin1);
        productInfo = (LinearLayout) findViewById(R.id.lineartwo);
        visualAssort = (LinearLayout) findViewById(R.id.linearthree);
        hourly_performance_view = (LinearLayout) findViewById(R.id.linear_hourly_performer_view_);
        sales = (LinearLayout) findViewById(R.id.lin2);
        promoAnalysis = (LinearLayout) findViewById(R.id.lin3);
        inventory = (LinearLayout) findViewById(R.id.lin4);
        Collaboration_subView = (LinearLayout) findViewById(R.id.collaboration_subView);
        Feedback_linear = (LinearLayout) findViewById(R.id.feedback_linear);
        linplanactual = (LinearLayout) findViewById(R.id.linplanactual);
        inspection_linear = (LinearLayout) findViewById(R.id.storeInspection_linear);
        Mpm_linear = (LinearLayout) findViewById(R.id.mpm_linear);
        linear_cust_loyalty = (LinearLayout) findViewById(R.id.linear_cust_loyalty);

        imageBtnStyle = (ImageButton) findViewById(R.id.imageBtnStyle);
        imageBtnKeyProducts = (ImageButton) findViewById(R.id.imageBtnKeyProducts);
        imgBtnSales = (ImageButton) findViewById(R.id.btnSales);
        imgBtnVisualAssortment = (ImageButton) findViewById(R.id.btnVisualAssortment);
        ActualKeyProduct = (ImageButton) findViewById(R.id.actualKeyProduct);
        imgBtnPvaAnalysis = (ImageButton) findViewById(R.id.btnPVA);
        imgBtnRunningPromo = (ImageButton) findViewById(R.id.btnRunningpromo);
        BtnUpcomingpromo = (ImageButton) findViewById(R.id.btnUpcomingpromo);
        BtnExpiringpromo = (ImageButton) findViewById(R.id.btnExpiringpromo);
        BtnBestWorstpromo = (ImageButton) findViewById(R.id.btnBestWorstpromo);
        BtnWorstPerformers = (ImageButton) findViewById(R.id.btnWorstPerformers);
        btnFeshnessindex = (ImageButton) findViewById(R.id.btnFeshnessindex);
        btnOptionEfficiency = (ImageButton) findViewById(R.id.btnOptionEfficiency);
        btnSkewedSize = (ImageButton) findViewById(R.id.btnSkewedSize);
        btnStockAgeing = (ImageButton) findViewById(R.id.btnStockAgeing);
        btnBestPerformersInv = (ImageButton) findViewById(R.id.btnBestPerformers);
        btnFloorAvailability = (ImageButton) findViewById(R.id.btnFloorAvailability);
        btnTargetStockExcep = (ImageButton) findViewById(R.id.btnTargetStockExcep);
        btnSellThruExcep = (ImageButton) findViewById(R.id.btnSellThruExcep);
        btnVisualReport = (ImageButton) findViewById(R.id.btnVisualReport);
        To_do_image_button = (ImageButton) findViewById(R.id.to_do);
        Status_image_button = (ImageButton) findViewById(R.id.status);
        Feedback_btn = (ImageButton) findViewById(R.id.feedback_btn);
        FeedbackList_btn = (ImageButton) findViewById(R.id.feedbackList_btn);
        btn_inspection_history = (ImageButton) findViewById(R.id.btn_inspection_history);
        btn_inspection_begin = (ImageButton) findViewById(R.id.btn_inspection_begin);
        btn_mpm = (ImageButton) findViewById(R.id.btn_mpm);
        btn_cust_loyalty = (ImageButton) findViewById(R.id.btn_cust_loyalty);
        Btn_hourly_performer = (ImageButton) findViewById(R.id.btn_hourly_performer);
        pager = (ViewPager) findViewById(R.id.viewpager);

        TabLayout tab = (TabLayout) findViewById(R.id.dotTab_dashboard);
        tab.setupWithViewPager(pager, true);
        li = (LinearLayout) findViewById(R.id.lill);
        li.setOrientation(LinearLayout.HORIZONTAL);
        final ScrollView scrollview = (ScrollView) findViewById(R.id.scrollView);
        scrollview.post(new Runnable() {

            public void run() {
                scrollview.fullScroll(View.FOCUS_UP);
            }
        });

        hourlyFlashTxt.setOnClickListener(this);
        productInfoTxt.setOnClickListener(this);
        visualAssortTxt.setOnClickListener(this);
        salesTxt.setOnClickListener(this);
        hourly_performer_Title.setOnClickListener(this);
        promoAnalysisTxt.setOnClickListener(this);
        inventoryTxt.setOnClickListener(this);
        Collaboration.setOnClickListener(this);
        Feedback.setOnClickListener(this);
        txt_store_Inspection.setOnClickListener(this);
        txt_mpm.setOnClickListener(this);
        txt_cust_loyalty.setOnClickListener(this);
        planvsActualtxt.setOnClickListener(this);
        btnSkewedSize.setOnClickListener(this);
        BtnBestWorstpromo.setOnClickListener(this);
        btn_mpm.setOnClickListener(this);
        btn_cust_loyalty.setOnClickListener(this);
        Btn_hourly_performer.setOnClickListener(this);
        BtnExpiringpromo.setOnClickListener(this);
        To_do_image_button.setOnClickListener(this);
        Status_image_button.setOnClickListener(this);
        Feedback_btn.setOnClickListener(this);
        FeedbackList_btn.setOnClickListener(this);
        BtnUpcomingpromo.setOnClickListener(this);
        imgBtnRunningPromo.setOnClickListener(this);
        imageBtnStyle.setOnClickListener(this);
        imageBtnKeyProducts.setOnClickListener(this);
        imgBtnSales.setOnClickListener(this);
        imgBtnPvaAnalysis.setOnClickListener(this);
        imgBtnVisualAssortment.setOnClickListener(this);
        btnFeshnessindex.setOnClickListener(this);
        btnOptionEfficiency.setOnClickListener(this);
        btnStockAgeing.setOnClickListener(this);
        btnBestPerformersInv.setOnClickListener(this);
        btnFloorAvailability.setOnClickListener(this);
        btnTargetStockExcep.setOnClickListener(this);
        btnSellThruExcep.setOnClickListener(this);
        btnVisualReport.setOnClickListener(this);
        ActualKeyProduct.setOnClickListener(this);
        btn_inspection_begin.setOnClickListener(this);
        btn_inspection_history.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ");
        if (geoLeveLDesc.equals("E ZONE")) {
            Log.e("---------Ezone--------", "----------");
            if (ezone_sales) {
                linear_ezone_sales.setVisibility(View.VISIBLE);
            } else {
                linear_ezone_sales.setVisibility(View.GONE);
            }
            if (hourly_performer_flag) {
                hourly_performance_view.setVisibility(View.VISIBLE);
            } else {
                hourly_performance_view.setVisibility(View.GONE);
            }
            if (ezone_inventory) {
                linear_ezone_inventory.setVisibility(View.VISIBLE);
            } else {
                linear_ezone_inventory.setVisibility(View.GONE);
            }
            if (ezone_cust_loyalty) {
                linear_ez_cust_loyalty.setVisibility(View.VISIBLE);
            } else {
                linear_ez_cust_loyalty.setVisibility(View.GONE);
            }

        } else {
            Log.e("----FBB----", "--------");
            if (Promo) {
                promoAnalysis.setVisibility(View.VISIBLE);
            } else {
                promoAnalysis.setVisibility(View.GONE);
            }
            if (hourly_performer_flag) {
                hourly_performance_view.setVisibility(View.VISIBLE);
            } else {
                hourly_performance_view.setVisibility(View.GONE);
            }
            if (HourlyFlash) {
                hourlyFlash.setVisibility(View.VISIBLE);
            } else {
                hourlyFlash.setVisibility(View.GONE);
            }
            if (ProductInfo) {
                productInfo.setVisibility(View.VISIBLE);

            } else {
                productInfo.setVisibility(View.GONE);
            }
            if (VisualAssort) {
                visualAssort.setVisibility(View.VISIBLE);
            } else {
                visualAssort.setVisibility(View.GONE);
            }
            if (Sales) {
                sales.setVisibility(View.VISIBLE);
            } else {
                sales.setVisibility(View.GONE);
            }
            if (Inventory) {
                inventory.setVisibility(View.VISIBLE);
            } else {
                inventory.setVisibility(View.GONE);
            }
            if (Collab_bool) {
                Collaboration_subView.setVisibility(View.VISIBLE);
            } else {
                Collaboration_subView.setVisibility(View.GONE);
            }
            if (PlanActual) {
                linplanactual.setVisibility(View.VISIBLE);
            } else {
                linplanactual.setVisibility(View.GONE);
            }

            if (feedback_bool) {
                Feedback_linear.setVisibility(View.VISIBLE);

            } else {
                Feedback_linear.setVisibility(View.GONE);

            }
            if (inspection_flag) {
                inspection_linear.setVisibility(View.VISIBLE);
            } else {
                inspection_linear.setVisibility(View.GONE);
            }
            if (mpm_flag) {
                Mpm_linear.setVisibility(View.VISIBLE);
            } else {
                Mpm_linear.setVisibility(View.GONE);
            }
            if (custloylty_flag) {
                linear_cust_loyalty.setVisibility(View.VISIBLE);
            } else {
                linear_cust_loyalty.setVisibility(View.GONE);
            }
        }

// check  permission..




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
            SalesFilterActivity.level_filter = 1;
            //  SalesAnalysisActivity1.selectedsegValue = null;
            SalesAnalysisActivity1.level = 1;
            Intent intent = new Intent(DashBoardActivity.this, LoginActivity1.class);
            startActivity(intent);
            finish();
            NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notifManager.cancelAll();
            return true;
        } else if (id == R.id.aboutus) {
            Intent intent = new Intent(DashBoardActivity.this, AboutUsActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                                if (geoLeveLDesc.equals("E ZONE")) {
                                    txt_ezone_refresh_time.setText("N/A");
                                } else {
                                    RefreshTime.setText("N/A");
                                }
                            } else {
                                for (int i = 0; i < response.length(); i++) {
                                    etlStatus = gson.fromJson(response.get(i).toString(), EtlStatus.class);
                                    etlStatusList.add(etlStatus);

                                }
                                if (geoLeveLDesc.equals("E ZONE")) {
                                    txt_ezone_refresh_time.setText(etlStatusList.get(0).getLastETLDate());
                                } else {
                                    RefreshTime.setText(etlStatusList.get(0).getLastETLDate());
                                }
                            }

                        } catch (Exception e) {
                            if (geoLeveLDesc.equals("E ZONE")) {
                                txt_ezone_refresh_time.setText("N/A");
                            } else {
                                RefreshTime.setText("N/A");
                            }
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)

                    {
                        if (geoLeveLDesc.equals("E ZONE")) {
                            txt_ezone_refresh_time.setText("N/A");
                        } else {
                            RefreshTime.setText("N/A");
                        }
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

    private void requestMarketingEventsAPI() {

        String url = ConstsCore.web_url + "/v1/display/dashboard/" + userId;
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.equals("") || response == null) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(DashBoardActivity.this, "No data found", Toast.LENGTH_LONG).show();
                            } else {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonOject = response.getJSONObject(i);
                                    String imageURL = jsonOject.getString("imageName");
                                    eventUrlList.add(imageURL);
                                }

                                EventScroller();
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

    private void EventScroller() {

        for (int i = 0; i < eventUrlList.size(); i++) {

            imgdot = new ImageView(this);//new View(DashBoardActivity.this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
            layoutParams.setMargins(3, 3, 3, 3);
            imgdot.setLayoutParams(layoutParams);
            imgdot.setImageResource(R.mipmap.dots_unselected);
            li.addView(imgdot);

        }

        // Pass results to ToDoViewPagerAdapter Class
        adapter = new EventPagerAdapter(DashBoardActivity.this, eventUrlList, li, pager);
        // Binds the Adapter to the ViewPager
        pager.setAdapter(adapter);
        Reusable_Functions.hDialog();
        pageSwitcher(10);
    }

    public void pageSwitcher(int seconds) {
        timer = new Timer(); // At this line a new Thread will be created
        timer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 1000); // delay

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            // Ezone part
            case R.id.ezone_headersales:
                if (str_ezone_sales.equals("NO")) {
                    linear_ezone_sales.setVisibility(View.VISIBLE);
                    linear_ezone_inventory.setVisibility(View.GONE);
                    linear_ez_cust_loyalty.setVisibility(View.GONE);
                    hourly_performance_view.setVisibility(View.GONE);
                    txt_ezone_sales.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.uplist, 0);
                    txt_ezone_inventory.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_ez_cust_loyalty.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    hourly_performer_Title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    str_ezone_sales = "YES";
                    str_ezone_inv = "NO";
                    str_ezone_cust_loyalty = "NO";
                    hourly_performance = "NO";
                    ezone_sales = true;
                    ezone_inventory = false;
                    ezone_cust_loyalty = false;
                    hourlyperform_flag = false;
                } else {
                    linear_ezone_sales.setVisibility(View.GONE);
                    txt_ezone_sales.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    str_ezone_sales = "NO";
                    ezone_sales = false;
                }
                break;

            case R.id.txt_hourly_ezperformer_:
                if (hourly_performance.equals("NO")) {
                    linear_ezone_sales.setVisibility(View.GONE);
                    linear_ezone_inventory.setVisibility(View.GONE);
                    linear_ez_cust_loyalty.setVisibility(View.GONE);
                    hourly_performance_view.setVisibility(View.VISIBLE);
                    txt_ezone_sales.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    hourly_performer_Title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.uplist, 0);
                    txt_ezone_inventory.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_ez_cust_loyalty.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    str_ezone_sales = "NO";
                    hourly_performance = "YES";
                    str_ezone_inv = "NO";
                    str_ezone_cust_loyalty = "NO";
                    ezone_sales = false;
                    hourlyperform_flag = true;
                    ezone_inventory = false;
                    ezone_cust_loyalty = false;
                } else {
                    hourly_performance_view.setVisibility(View.GONE);
                    hourly_performer_Title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    hourly_performance = "NO";
                    hourlyperform_flag = false;
                }
                break;
            case R.id.ezone_header_inventory:
                if (str_ezone_inv.equals("NO")) {
                    linear_ezone_sales.setVisibility(View.GONE);
                    linear_ezone_inventory.setVisibility(View.VISIBLE);
                    linear_ez_cust_loyalty.setVisibility(View.GONE);
                    hourly_performance_view.setVisibility(View.GONE);

                    txt_ezone_sales.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_ezone_inventory.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.uplist, 0);
                    txt_ez_cust_loyalty.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    hourly_performer_Title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);

                    str_ezone_sales = "NO";
                    hourly_performance = "NO";
                    str_ezone_inv = "YES";
                    str_ezone_cust_loyalty = "NO";
                    ezone_sales = false;
                    hourlyperform_flag = false;
                    ezone_inventory = true;
                    ezone_cust_loyalty = false;
                } else {
                    linear_ezone_inventory.setVisibility(View.GONE);
                    txt_ezone_inventory.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    str_ezone_inv = "NO";
                    ezone_inventory = false;
                }
                break;
            case R.id.txt_ez_cust_loyalty:
                if (str_ezone_cust_loyalty.equals("NO")) {
                    linear_ezone_sales.setVisibility(View.GONE);
                    linear_ezone_inventory.setVisibility(View.GONE);
                    hourly_performance_view.setVisibility(View.GONE);
                    linear_ez_cust_loyalty.setVisibility(View.VISIBLE);

                    txt_ezone_sales.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_ezone_inventory.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    hourly_performer_Title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_ez_cust_loyalty.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.uplist, 0);

                    str_ezone_sales = "NO";
                    str_ezone_inv = "NO";
                    hourly_performance = "NO";
                    str_ezone_cust_loyalty = "YES";
                    ezone_sales = false;
                    ezone_inventory = false;
                    hourlyperform_flag = false;
                    ezone_cust_loyalty = true;
                } else {
                    linear_ez_cust_loyalty.setVisibility(View.GONE);
                    txt_ez_cust_loyalty.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    str_ezone_cust_loyalty = "NO";
                    ezone_cust_loyalty = false;
                }
                break;
            case R.id.btn_ezone_Sales:
                Intent int_sales = new Intent(DashBoardActivity.this, SalesAnalysisActivity1.class);
                startActivity(int_sales);
                if (timer != null) {
                    timer.cancel();
                }
                break;
            case R.id.btn_ezone_AssortmentAnalysis:
                Intent int_assort = new Intent(DashBoardActivity.this, FreshnessIndexActivity.class);
                startActivity(int_assort);
                if (timer != null) {
                    timer.cancel();
                }
                break;
            case R.id.btn_ezone_best_worst:
                Intent int_best = new Intent(DashBoardActivity.this, BestPerformerInventory.class);
                startActivity(int_best);
                if (timer != null) {
                    timer.cancel();
                }
                break;
            case R.id.btn_ez_cust_loyalty:
                Intent int_cust = new Intent(DashBoardActivity.this, CustomerLookupActivity.class);
                startActivity(int_cust);
                if (timer != null) {
                    timer.cancel();
                }
                break;

            case R.id.btn_hourly_ezperformer:
                Intent hourly = new Intent(DashBoardActivity.this, HourlyPerformence.class);
                startActivity(hourly);
                if (timer != null) {
                    timer.cancel();
                }
                break;


            // Fbb part
            case R.id.headersmdm:
                if (hrflash.equals("NO")) {
                    hourlyFlash.setVisibility(View.VISIBLE);
                    productInfo.setVisibility(View.GONE);
                    visualAssort.setVisibility(View.GONE);
                    sales.setVisibility(View.GONE);
                    promoAnalysis.setVisibility(View.GONE);
                    inventory.setVisibility(View.GONE);
                    linplanactual.setVisibility(View.GONE);
                    Mpm_linear.setVisibility(View.GONE);
                    Collaboration_subView.setVisibility(View.GONE);
                    Feedback_linear.setVisibility(View.GONE);
                    inspection_linear.setVisibility(View.GONE);
                    linear_cust_loyalty.setVisibility(View.GONE);
                    hourly_performance_view.setVisibility(View.GONE);
                    hourlyFlashTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.uplist, 0);
                    productInfoTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    visualAssortTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    salesTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    hourly_performer_Title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    promoAnalysisTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    inventoryTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    Collaboration.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_mpm.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    planvsActualtxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    Feedback.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_store_Inspection.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_cust_loyalty.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);

                    hrflash = "YES";
                    pdInfo = "NO";
                    vsAssort = "NO";
                    sAles = "NO";
                    pmAnalysis = "NO";
                    inVENtory = "NO";
                    mpm = "NO";
                    planActual = "NO";
                    feedback_flag = "NO";
                    store_inspection = "NO";
                    Collab = "NO";
                    cust_loyalty = "NO";
                    hourly_performance = "NO";

                    HourlyFlash = true;
                    Promo = false;
                    ProductInfo = false;
                    VisualAssort = false;
                    hourly_performer_flag = false;
                    Sales = false;
                    Inventory = false;
                    Collab_bool = false;
                    mpm_flag = false;
                    feedback_bool = false;
                    PlanActual = false;
                    inspection_flag = false;
                    custloylty_flag = false;

                } else {
                    hourlyFlash.setVisibility(View.GONE);
                    hourlyFlashTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    hrflash = "NO";
                    HourlyFlash = false;

                }
                break;


            case R.id.txt_hourly_performer_:
                if (hourly_performance.equals("NO")) {
                    hourlyFlash.setVisibility(View.GONE);
                    productInfo.setVisibility(View.GONE);
                    visualAssort.setVisibility(View.GONE);
                    sales.setVisibility(View.GONE);
                    promoAnalysis.setVisibility(View.GONE);
                    inventory.setVisibility(View.GONE);
                    linplanactual.setVisibility(View.GONE);
                    Mpm_linear.setVisibility(View.GONE);
                    Collaboration_subView.setVisibility(View.GONE);
                    Feedback_linear.setVisibility(View.GONE);
                    inspection_linear.setVisibility(View.GONE);
                    linear_cust_loyalty.setVisibility(View.GONE);
                    hourly_performance_view.setVisibility(View.VISIBLE);
                    hourlyFlashTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    productInfoTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    visualAssortTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    salesTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    hourly_performer_Title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.uplist, 0);
                    promoAnalysisTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    inventoryTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    Collaboration.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_mpm.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    planvsActualtxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    Feedback.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_store_Inspection.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_cust_loyalty.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);

                    hrflash = "NO";
                    pdInfo = "NO";
                    vsAssort = "NO";
                    sAles = "NO";
                    pmAnalysis = "NO";
                    inVENtory = "NO";
                    mpm = "NO";
                    planActual = "NO";
                    feedback_flag = "NO";
                    store_inspection = "NO";
                    Collab = "NO";
                    cust_loyalty = "NO";
                    hourly_performance = "YES";

                    HourlyFlash = false;
                    Promo = false;
                    ProductInfo = false;
                    VisualAssort = false;
                    hourly_performer_flag = true;
                    Sales = false;
                    Inventory = false;
                    Collab_bool = false;
                    mpm_flag = false;
                    feedback_bool = false;
                    PlanActual = false;
                    inspection_flag = false;
                    custloylty_flag = false;

                } else {
                    hourly_performance_view.setVisibility(View.GONE);
                    hourly_performer_Title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    hourly_performance = "NO";
                    hourly_performer_flag = false;

                }
                break;

            case R.id.productinfo:
                if (pdInfo.equals("NO")) {
                    hourlyFlash.setVisibility(View.GONE);
                    productInfo.setVisibility(View.VISIBLE);
                    visualAssort.setVisibility(View.GONE);
                    sales.setVisibility(View.GONE);
                    promoAnalysis.setVisibility(View.GONE);
                    inventory.setVisibility(View.GONE);
                    linplanactual.setVisibility(View.GONE);
                    Mpm_linear.setVisibility(View.GONE);
                    Feedback_linear.setVisibility(View.GONE);
                    Collaboration_subView.setVisibility(View.GONE);
                    inspection_linear.setVisibility(View.GONE);
                    linear_cust_loyalty.setVisibility(View.GONE);
                    hourly_performance_view.setVisibility(View.GONE);

                    hourlyFlashTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    productInfoTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.uplist, 0);
                    visualAssortTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    salesTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    hourly_performer_Title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    promoAnalysisTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    inventoryTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    Collaboration.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    planvsActualtxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_mpm.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    Feedback.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_store_Inspection.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_cust_loyalty.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);

                    pdInfo = "YES";
                    hrflash = "NO";
                    vsAssort = "NO";
                    sAles = "NO";
                    pmAnalysis = "NO";
                    inVENtory = "NO";
                    hourly_performance = "NO";
                    mpm = "NO";
                    planActual = "NO";
                    feedback_flag = "NO";
                    store_inspection = "NO";
                    cust_loyalty = "NO";

                    ProductInfo = true;
                    HourlyFlash = false;
                    Promo = false;
                    VisualAssort = false;
                    Sales = false;
                    Inventory = false;
                    PlanActual = false;
                    feedback_bool = false;
                    mpm_flag = false;
                    Collab_bool = false;
                    inspection_flag = false;
                    hourly_performer_flag = false;
                    Collab = "NO";
                    custloylty_flag = false;

                } else {
                    productInfo.setVisibility(View.GONE);
                    productInfoTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    pdInfo = "NO";
                    ProductInfo = false;
                }
                break;

            case R.id.visualAssort:
                if (vsAssort.equals("NO")) {
                    hourlyFlash.setVisibility(View.GONE);
                    productInfo.setVisibility(View.GONE);
                    visualAssort.setVisibility(View.VISIBLE);
                    sales.setVisibility(View.GONE);
                    promoAnalysis.setVisibility(View.GONE);
                    inventory.setVisibility(View.GONE);
                    linplanactual.setVisibility(View.GONE);
                    Collaboration_subView.setVisibility(View.GONE);
                    Mpm_linear.setVisibility(View.GONE);
                    Feedback_linear.setVisibility(View.GONE);
                    inspection_linear.setVisibility(View.GONE);
                    linear_cust_loyalty.setVisibility(View.GONE);
                    hourly_performance_view.setVisibility(View.GONE);

                    hourlyFlashTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    productInfoTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    visualAssortTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.uplist, 0);
                    salesTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    hourly_performer_Title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    promoAnalysisTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    inventoryTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    Collaboration.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    planvsActualtxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_mpm.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    Feedback.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_store_Inspection.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_cust_loyalty.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);

                    vsAssort = "YES";
                    hrflash = "NO";
                    pdInfo = "NO";
                    sAles = "No";
                    pmAnalysis = "NO";
                    inVENtory = "NO";
                    mpm = "NO";
                    planActual = "NO";
                    feedback_flag = "NO";
                    store_inspection = "NO";
                    cust_loyalty = "NO";
                    hourly_performance = "NO";


                    VisualAssort = true;
                    HourlyFlash = false;
                    Promo = false;
                    ProductInfo = false;
                    Sales = false;
                    Inventory = false;
                    Collab_bool = false;
                    mpm_flag = false;
                    feedback_bool = false;
                    Collab = "NO";
                    inspection_flag = false;
                    PlanActual = false;
                    hourly_performer_flag = false;
                    custloylty_flag = false;
                } else {
                    visualAssort.setVisibility(View.GONE);
                    visualAssortTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    vsAssort = "NO";
                    VisualAssort = false;

                }
                break;

            case R.id.headersales:
                if (sAles.equals("NO")) {
                    hourlyFlash.setVisibility(View.GONE);
                    productInfo.setVisibility(View.GONE);
                    visualAssort.setVisibility(View.GONE);
                    sales.setVisibility(View.VISIBLE);
                    promoAnalysis.setVisibility(View.GONE);
                    inventory.setVisibility(View.GONE);
                    linplanactual.setVisibility(View.GONE);
                    Mpm_linear.setVisibility(View.GONE);
                    Feedback_linear.setVisibility(View.GONE);
                    inspection_linear.setVisibility(View.GONE);
                    linear_cust_loyalty.setVisibility(View.GONE);
                    hourly_performance_view.setVisibility(View.GONE);

                    Collaboration_subView.setVisibility(View.GONE);
                    hourlyFlashTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    productInfoTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    visualAssortTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    salesTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.uplist, 0);
                    promoAnalysisTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    inventoryTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    hourly_performer_Title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    Collaboration.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_mpm.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    planvsActualtxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    Feedback.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_store_Inspection.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_cust_loyalty.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);

                    sAles = "YES";
                    hrflash = "NO";
                    pdInfo = "NO";
                    vsAssort = "NO";
                    pmAnalysis = "NO";
                    inVENtory = "NO";
                    mpm = "NO";
                    planActual = "NO";
                    feedback_flag = "NO";
                    store_inspection = "NO";
                    cust_loyalty = "NO";
                    hourly_performance = "NO";


                    Sales = true;
                    HourlyFlash = false;
                    Promo = false;
                    ProductInfo = false;
                    VisualAssort = false;
                    Inventory = false;
                    Collab_bool = false;
                    feedback_bool = false;
                    mpm_flag = false;
                    Collab = "NO";
                    PlanActual = false;
                    inspection_flag = false;
                    custloylty_flag = false;
                    hourly_performer_flag = false;
                } else {
                    sales.setVisibility(View.GONE);
                    salesTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    sAles = "NO";
                    Sales = false;

                }
                break;

            case R.id.headerpromo:
                if (pmAnalysis.equals("NO")) {
                    hourlyFlash.setVisibility(View.GONE);
                    productInfo.setVisibility(View.GONE);
                    visualAssort.setVisibility(View.GONE);
                    sales.setVisibility(View.GONE);
                    promoAnalysis.setVisibility(View.VISIBLE);
                    inventory.setVisibility(View.GONE);
                    Mpm_linear.setVisibility(View.GONE);
                    Collaboration_subView.setVisibility(View.GONE);
                    linplanactual.setVisibility(View.GONE);
                    Feedback_linear.setVisibility(View.GONE);
                    inspection_linear.setVisibility(View.GONE);
                    linear_cust_loyalty.setVisibility(View.GONE);
                    hourly_performance_view.setVisibility(View.GONE);

                    hourlyFlashTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    productInfoTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    visualAssortTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    salesTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    promoAnalysisTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.uplist, 0);
                    inventoryTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    hourly_performer_Title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    Collaboration.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_mpm.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    planvsActualtxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    Feedback.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_store_Inspection.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_cust_loyalty.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);

                    pmAnalysis = "YES";
                    hrflash = "NO";
                    pdInfo = "NO";
                    vsAssort = "NO";
                    sAles = "NO";
                    mpm = "NO";
                    inVENtory = "NO";
                    feedback_flag = "NO";
                    planActual = "NO";
                    store_inspection = "NO";
                    cust_loyalty = "NO";
                    hourly_performance = "NO";

                    HourlyFlash = false;
                    Promo = true;
                    ProductInfo = false;
                    VisualAssort = false;
                    Sales = false;
                    Inventory = false;
                    feedback_bool = false;
                    mpm_flag = false;
                    Collab_bool = false;
                    Collab = "NO";
                    inspection_flag = false;
                    PlanActual = false;
                    custloylty_flag = false;
                    hourly_performer_flag = false;

                } else {
                    promoAnalysis.setVisibility(View.GONE);
                    promoAnalysisTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    pmAnalysis = "NO";
                    Promo = false;
                }
                break;

            case R.id.headerinvent:
                if (inVENtory.equals("NO")) {
                    hourlyFlash.setVisibility(View.GONE);
                    productInfo.setVisibility(View.GONE);
                    visualAssort.setVisibility(View.GONE);
                    sales.setVisibility(View.GONE);
                    promoAnalysis.setVisibility(View.GONE);
                    inventory.setVisibility(View.VISIBLE);
                    linplanactual.setVisibility(View.GONE);
                    Feedback_linear.setVisibility(View.GONE);
                    Mpm_linear.setVisibility(View.GONE);
                    Collaboration_subView.setVisibility(View.GONE);
                    inspection_linear.setVisibility(View.GONE);
                    linear_cust_loyalty.setVisibility(View.GONE);
                    hourly_performance_view.setVisibility(View.GONE);

                    hourlyFlashTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    productInfoTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    visualAssortTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    salesTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    promoAnalysisTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    inventoryTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.uplist, 0);
                    planvsActualtxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_mpm.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    hourly_performer_Title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    Feedback.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    Collaboration.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_store_Inspection.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_cust_loyalty.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);

                    inVENtory = "YES";
                    hrflash = "NO";
                    pdInfo = "NO";
                    vsAssort = "NO";
                    Collab = "NO";
                    sAles = "NO";
                    mpm = "NO";
                    feedback_flag = "NO";
                    pmAnalysis = "NO";
                    planActual = "NO";
                    store_inspection = "NO";
                    cust_loyalty = "NO";
                    hourly_performance = "NO";

                    HourlyFlash = false;
                    Promo = false;
                    ProductInfo = false;
                    VisualAssort = false;
                    mpm_flag = false;
                    Sales = false;
                    Inventory = true;
                    Collab_bool = false;
                    feedback_bool = false;
                    PlanActual = false;
                    inspection_flag = false;
                    custloylty_flag = false;
                    hourly_performer_flag = false;

                } else {
                    inventory.setVisibility(View.GONE);
                    inventoryTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    inVENtory = "NO";
                    Inventory = false;
                }
                break;


            case R.id.collaboration:
                if (Collab.equals("NO")) {
                    hourlyFlash.setVisibility(View.GONE);
                    productInfo.setVisibility(View.GONE);
                    visualAssort.setVisibility(View.GONE);
                    sales.setVisibility(View.GONE);
                    promoAnalysis.setVisibility(View.GONE);
                    inventory.setVisibility(View.GONE);
                    linplanactual.setVisibility(View.GONE);
                    Feedback_linear.setVisibility(View.GONE);
                    Mpm_linear.setVisibility(View.GONE);
                    Collaboration_subView.setVisibility(View.VISIBLE);
                    inspection_linear.setVisibility(View.GONE);
                    linear_cust_loyalty.setVisibility(View.GONE);
                    hourly_performance_view.setVisibility(View.GONE);

                    hourlyFlashTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    productInfoTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    visualAssortTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    salesTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_mpm.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    promoAnalysisTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    Feedback.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    hourly_performer_Title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    inventoryTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    Collaboration.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.uplist, 0);
                    planvsActualtxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_store_Inspection.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_cust_loyalty.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);

                    inVENtory = "No";
                    hrflash = "NO";
                    pdInfo = "NO";
                    vsAssort = "NO";
                    sAles = "NO";
                    feedback_flag = "NO";
                    pmAnalysis = "NO";
                    mpm = "NO";
                    Collab = "YES";
                    planActual = "NO";
                    store_inspection = "NO";
                    cust_loyalty = "NO";
                    hourly_performance = "NO";


                    HourlyFlash = false;
                    Promo = false;
                    ProductInfo = false;
                    VisualAssort = false;
                    mpm_flag = false;
                    Sales = false;
                    Inventory = false;
                    feedback_bool = false;
                    PlanActual = false;
                    Collab_bool = true;
                    inspection_flag = false;
                    custloylty_flag = false;
                    hourly_performer_flag = false;

                } else {
                    Collaboration_subView.setVisibility(View.GONE);
                    Collaboration.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    Collab = "NO";
                    Collab_bool = false;
                }
                break;

            case R.id.headerplanactual:
                if (planActual.equals("NO")) {
                    hourlyFlash.setVisibility(View.GONE);
                    productInfo.setVisibility(View.GONE);
                    visualAssort.setVisibility(View.GONE);
                    sales.setVisibility(View.GONE);
                    promoAnalysis.setVisibility(View.GONE);
                    inventory.setVisibility(View.GONE);
                    Mpm_linear.setVisibility(View.GONE);
                    Feedback_linear.setVisibility(View.GONE);
                    linplanactual.setVisibility(View.VISIBLE);
                    Collaboration_subView.setVisibility(View.GONE);
                    inspection_linear.setVisibility(View.GONE);
                    linear_cust_loyalty.setVisibility(View.GONE);
                    hourly_performance_view.setVisibility(View.GONE);

                    hourlyFlashTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    productInfoTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    visualAssortTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    salesTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    hourly_performer_Title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    Feedback.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    promoAnalysisTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_mpm.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    inventoryTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    planvsActualtxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.uplist, 0);
                    Collaboration.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_store_Inspection.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_cust_loyalty.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);

                    planActual = "YES";
                    inVENtory = "NO";
                    hrflash = "NO";
                    pdInfo = "NO";
                    vsAssort = "NO";
                    feedback_flag = "NO";
                    sAles = "NO";
                    mpm = "NO";
                    pmAnalysis = "NO";
                    Collab = "NO";
                    store_inspection = "NO";
                    cust_loyalty = "NO";
                    hourly_performance = "NO";


                    Collab_bool = false;
                    HourlyFlash = false;
                    Promo = false;
                    ProductInfo = false;
                    VisualAssort = false;
                    mpm_flag = false;
                    feedback_bool = false;
                    Sales = false;
                    Inventory = false;
                    PlanActual = true;
                    inspection_flag = false;
                    custloylty_flag = false;
                    hourly_performer_flag = false;

                } else {
                    linplanactual.setVisibility(View.GONE);
                    planvsActualtxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    planActual = "NO";
                    PlanActual = false;
                }
                break;

            case R.id.feedback:
                if (feedback_flag.equals("NO")) {
                    hourlyFlash.setVisibility(View.GONE);
                    productInfo.setVisibility(View.GONE);
                    visualAssort.setVisibility(View.GONE);
                    sales.setVisibility(View.GONE);
                    promoAnalysis.setVisibility(View.GONE);
                    inventory.setVisibility(View.GONE);
                    Feedback_linear.setVisibility(View.VISIBLE);
                    linplanactual.setVisibility(View.GONE);
                    Collaboration_subView.setVisibility(View.GONE);
                    Mpm_linear.setVisibility(View.GONE);
                    inspection_linear.setVisibility(View.GONE);
                    linear_cust_loyalty.setVisibility(View.GONE);
                    hourly_performance_view.setVisibility(View.GONE);


                    hourlyFlashTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    productInfoTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    visualAssortTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    salesTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    Feedback.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.uplist, 0);
                    hourly_performer_Title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    promoAnalysisTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    inventoryTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    planvsActualtxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_mpm.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    Collaboration.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_store_Inspection.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_cust_loyalty.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);

                    planActual = "NO";
                    inVENtory = "NO";
                    hrflash = "NO";
                    pdInfo = "NO";
                    mpm = "NO";
                    vsAssort = "NO";
                    feedback_flag = "YES";
                    sAles = "NO";
                    pmAnalysis = "NO";
                    Collab = "NO";
                    store_inspection = "NO";
                    cust_loyalty = "NO";
                    hourly_performance = "NO";


                    Collab_bool = false;
                    HourlyFlash = false;
                    Promo = false;
                    ProductInfo = false;
                    mpm_flag = false;
                    VisualAssort = false;
                    feedback_bool = true;
                    Sales = false;
                    Inventory = false;
                    PlanActual = false;
                    inspection_flag = false;
                    custloylty_flag = false;
                    hourly_performer_flag = false;

                } else {
                    Feedback_linear.setVisibility(View.GONE);
                    Feedback.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    feedback_flag = "NO";
                    feedback_bool = false;
                }
                break;
            case R.id.storeInspection:
                if (store_inspection.equals("NO")) {
                    hourlyFlash.setVisibility(View.GONE);
                    productInfo.setVisibility(View.GONE);
                    visualAssort.setVisibility(View.GONE);
                    sales.setVisibility(View.GONE);
                    promoAnalysis.setVisibility(View.GONE);
                    inventory.setVisibility(View.GONE);
                    Feedback_linear.setVisibility(View.GONE);
                    linplanactual.setVisibility(View.GONE);
                    Collaboration_subView.setVisibility(View.GONE);
                    Mpm_linear.setVisibility(View.GONE);
                    inspection_linear.setVisibility(View.VISIBLE);
                    linear_cust_loyalty.setVisibility(View.GONE);
                    hourly_performance_view.setVisibility(View.GONE);


                    hourlyFlashTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    productInfoTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    visualAssortTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    salesTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    Feedback.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    hourly_performer_Title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    promoAnalysisTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    inventoryTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    planvsActualtxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    Collaboration.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_mpm.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_store_Inspection.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.uplist, 0);
                    txt_cust_loyalty.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.uplist, 0);

                    planActual = "NO";
                    inVENtory = "NO";
                    hrflash = "NO";
                    pdInfo = "NO";
                    vsAssort = "NO";
                    mpm = "NO";
                    feedback_flag = "NO";
                    sAles = "NO";
                    pmAnalysis = "NO";
                    Collab = "NO";
                    store_inspection = "YES";
                    cust_loyalty = "NO";
                    hourly_performance = "NO";


                    Collab_bool = false;
                    HourlyFlash = false;
                    mpm_flag = false;
                    Promo = false;
                    ProductInfo = false;
                    VisualAssort = false;
                    feedback_bool = false;
                    inspection_flag = true;
                    Sales = false;
                    Inventory = false;
                    PlanActual = false;
                    custloylty_flag = false;
                    hourly_performer_flag = false;

                } else {
                    inspection_linear.setVisibility(View.GONE);
                    txt_store_Inspection.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    store_inspection = "NO";
                    inspection_flag = false;
                }
                break;
            case R.id.mpm:
                if (mpm.equals("NO")) {
                    hourlyFlash.setVisibility(View.GONE);
                    productInfo.setVisibility(View.GONE);
                    visualAssort.setVisibility(View.GONE);
                    sales.setVisibility(View.GONE);
                    promoAnalysis.setVisibility(View.GONE);
                    inventory.setVisibility(View.GONE);
                    Feedback_linear.setVisibility(View.GONE);
                    linplanactual.setVisibility(View.GONE);
                    Collaboration_subView.setVisibility(View.GONE);
                    inspection_linear.setVisibility(View.GONE);
                    Mpm_linear.setVisibility(View.VISIBLE);
                    linear_cust_loyalty.setVisibility(View.GONE);
                    hourly_performance_view.setVisibility(View.GONE);


                    hourlyFlashTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    productInfoTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    visualAssortTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    salesTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    Feedback.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    hourly_performer_Title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    promoAnalysisTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    inventoryTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    planvsActualtxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    Collaboration.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_store_Inspection.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_mpm.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.uplist, 0);
                    txt_cust_loyalty.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);

                    planActual = "NO";
                    inVENtory = "NO";
                    hrflash = "NO";
                    pdInfo = "NO";
                    vsAssort = "NO";
                    feedback_flag = "NO";
                    sAles = "NO";
                    pmAnalysis = "NO";
                    Collab = "NO";
                    store_inspection = "NO";
                    mpm = "YES";
                    cust_loyalty = "NO";
                    hourly_performance = "NO";


                    Collab_bool = false;
                    HourlyFlash = false;
                    Promo = false;
                    ProductInfo = false;
                    VisualAssort = false;
                    feedback_bool = false;
                    inspection_flag = false;
                    mpm_flag = true;
                    Sales = false;
                    Inventory = false;
                    PlanActual = false;
                    custloylty_flag = false;
                    hourly_performer_flag = false;

                } else {
                    Mpm_linear.setVisibility(View.GONE);
                    txt_mpm.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    mpm = "NO";
                    mpm_flag = false;
                }
                break;
            case R.id.txt_cust_loyalty:
                if (cust_loyalty.equals("NO")) {
                    hourlyFlash.setVisibility(View.GONE);
                    productInfo.setVisibility(View.GONE);
                    visualAssort.setVisibility(View.GONE);
                    sales.setVisibility(View.GONE);
                    promoAnalysis.setVisibility(View.GONE);
                    inventory.setVisibility(View.GONE);
                    Feedback_linear.setVisibility(View.GONE);
                    linplanactual.setVisibility(View.GONE);
                    Collaboration_subView.setVisibility(View.GONE);
                    inspection_linear.setVisibility(View.GONE);
                    Mpm_linear.setVisibility(View.GONE);
                    linear_cust_loyalty.setVisibility(View.VISIBLE);
                    hourly_performance_view.setVisibility(View.GONE);


                    hourlyFlashTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    productInfoTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    visualAssortTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    salesTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    Feedback.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    promoAnalysisTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    inventoryTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    planvsActualtxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    hourly_performer_Title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    Collaboration.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_store_Inspection.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_mpm.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    txt_cust_loyalty.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.uplist, 0);

                    planActual = "NO";
                    inVENtory = "NO";
                    hrflash = "NO";
                    pdInfo = "NO";
                    vsAssort = "NO";
                    feedback_flag = "NO";
                    sAles = "NO";
                    pmAnalysis = "NO";
                    Collab = "NO";
                    store_inspection = "NO";
                    mpm = "NO";
                    cust_loyalty = "YES";
                    hourly_performance = "NO";

                    Collab_bool = false;
                    HourlyFlash = false;
                    Promo = false;
                    ProductInfo = false;
                    VisualAssort = false;
                    feedback_bool = false;
                    inspection_flag = false;
                    mpm_flag = false;
                    Sales = false;
                    Inventory = false;
                    PlanActual = false;
                    custloylty_flag = true;
                    hourly_performer_flag = false;

                } else {
                    linear_cust_loyalty.setVisibility(View.GONE);
                    txt_cust_loyalty.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downlist, 0);
                    cust_loyalty = "NO";
                    custloylty_flag = false;
                }
                break;

            case R.id.btn_cust_loyalty:
                Intent intent_cust_loyalty = new Intent(DashBoardActivity.this, CustomerLookupActivity.class);
                startActivity(intent_cust_loyalty);
                if (timer != null) {
                    timer.cancel();
                }
                break;

            case R.id.btn_hourly_performer:
                Intent intent_hourly = new Intent(DashBoardActivity.this, HourlyPerformence.class);
                startActivity(intent_hourly);
                if (timer != null) {
                    timer.cancel();
                }
                break;

            case R.id.btn_mpm:
                Intent intent_mpm = new Intent(DashBoardActivity.this, mpm_activity.class);
                startActivity(intent_mpm);
                if (timer != null) {
                    timer.cancel();
                }
                break;

            case R.id.status:
                new StatusActivity().StartIntent(DashBoardActivity.this);
                if (timer != null) {
                    timer.cancel();

                }
                break;
            case R.id.to_do:
                new To_Do().StartIntent(DashBoardActivity.this);
                if (timer != null) {
                    timer.cancel();

                }
                break;
            case R.id.btnBestPerformers:
                Intent intent_best = new Intent(DashBoardActivity.this, BestPerformerInventory.class);
                startActivity(intent_best);
                if (timer != null) {
                    timer.cancel();

                }
                break;
            case R.id.btnFloorAvailability:
                Intent intent_floor = new Intent(DashBoardActivity.this, FloorAvailabilityActivity.class);
                startActivity(intent_floor);
                if (timer != null) {
                    timer.cancel();

                }
                break;
            case R.id.btnTargetStockExcep:
                Intent intent_target = new Intent(DashBoardActivity.this, TargetStockExceptionActivity.class);
                startActivity(intent_target);
                if (timer != null) {
                    timer.cancel();

                }
            case R.id.btnSellThruExcep:
                Intent intent_sale = new Intent(DashBoardActivity.this, SaleThruInventory.class);
                startActivity(intent_sale);
                if (timer != null) {
                    timer.cancel();

                }
                break;

            case R.id.btnStockAgeing:
                Intent intent_stock = new Intent(DashBoardActivity.this, StockAgeingActivity.class);
                startActivity(intent_stock);
                if (timer != null) {
                    timer.cancel();

                }
                break;
            case R.id.btnSkewedSize:
                Intent intent_skew = new Intent(DashBoardActivity.this, SkewedSizesActivity.class);
                startActivity(intent_skew);
                if (timer != null) {
                    timer.cancel();

                }
                break;

            case R.id.btnFeshnessindex:
                Intent intent_freshness = new Intent(DashBoardActivity.this, FreshnessIndexActivity.class);
                startActivity(intent_freshness);
                if (timer != null) {
                    timer.cancel();

                }
                break;
            case R.id.btnOptionEfficiency:
                Intent intent_option = new Intent(DashBoardActivity.this, OptionEfficiencyActivity.class);
                startActivity(intent_option);
                if (timer != null) {
                    timer.cancel();

                }
                break;
            case R.id.btnBestWorstpromo:
                Intent intent_best_worse = new Intent(DashBoardActivity.this, BestPerformerActivity.class);
                startActivity(intent_best_worse);
                if (timer != null) {
                    timer.cancel();

                }
                break;
            case R.id.feedbackList_btn:
                new FeedbackList().StartIntent(DashBoardActivity.this);
                if (timer != null) {
                    timer.cancel();
                }
                break;

            case R.id.feedback_btn:
                new Feedback().StartIntent(DashBoardActivity.this);
                if (timer != null) {
                    timer.cancel();
                }
                break;
            case R.id.btnUpcomingpromo:
                Intent intent_upcoming = new Intent(DashBoardActivity.this, UpcomingPromo.class);
                startActivity(intent_upcoming);
                if (timer != null) {
                    timer.cancel();
                }
                break;
            case R.id.btnExpiringpromo:
                Intent intent_exprinig = new Intent(DashBoardActivity.this, ExpiringPromoActivity.class);
                startActivity(intent_exprinig);

                if (timer != null) {
                    timer.cancel();
                }
                break;

            case R.id.btnPVA:
                Intent intent = new Intent(DashBoardActivity.this, SalesPvAActivity.class);
                startActivity(intent);
                if (timer != null) {
                    timer.cancel();
                }
                break;
            case R.id.btnSales:
                Intent intent1 = new Intent(DashBoardActivity.this, SalesAnalysisActivity1.class);
                startActivity(intent1);
                if (timer != null) {
                    timer.cancel();
                }
                break;
            case R.id.btn_inspection_begin:
                Intent intent_insp = new Intent(DashBoardActivity.this, InspectionBeginActivity.class);
                startActivity(intent_insp);
                if (timer != null) {
                    timer.cancel();
                }
                break;
            case R.id.btn_inspection_history:
                Intent intent_insp_history = new Intent(DashBoardActivity.this, InspectionHistoryActivity.class);
                startActivity(intent_insp_history);
                if (timer != null) {
                    timer.cancel();
                }
                break;
            case R.id.imageBtnKeyProducts:
                Intent intent_key_product = new Intent(DashBoardActivity.this, KeyProductActivity.class);
                startActivity(intent_key_product);
                if (timer != null) {
                    timer.cancel();
                }
                break;
            case R.id.actualKeyProduct:
                Intent intent_plan_actual = new Intent(DashBoardActivity.this, KeyProductPlanActivity.class);
                startActivity(intent_plan_actual);
                if (timer != null) {
                    timer.cancel();
                }
                break;
            case R.id.imageBtnStyle:
                Intent intent_style = new Intent(DashBoardActivity.this, StyleActivity.class);
                startActivity(intent_style);
                if (timer != null) {
                    timer.cancel();
                }
                break;
            case R.id.btnVisualAssortment:
                Intent intent_visual_assort = new Intent(DashBoardActivity.this, VisualAssortmentActivity.class);
                startActivity(intent_visual_assort);
                if (timer != null) {
                    timer.cancel();
                }
                break;
            case R.id.btnVisualReport:
                Intent intent_visual_report = new Intent(DashBoardActivity.this, VisualReportActivity.class);
                startActivity(intent_visual_report);
                if (timer != null) {
                    timer.cancel();
                }
                break;
            case R.id.btnRunningpromo:
                Intent intent_running_promo = new Intent(DashBoardActivity.this, RunningPromoActivity.class);
                startActivity(intent_running_promo);
                if (timer != null) {
                    timer.cancel();
                }
                break;

        }

    }


  /*  @Override
    protected void onResume() {
        super.onResume();


        if (!onRestart_toggle) {

            checkPermission();
        }
    }*/


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
                    snackbar = Snackbar.make(view, Constants.REQUEST_PERMISSION_SNACKALERT,Snackbar.LENGTH_SHORT).setAction("OK", new View.OnClickListener() {
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


    }

    // this is an inner class...
    class RemindTask extends TimerTask {
        @Override
        public void run() {
            // As the TimerTask run on a seprate thread from UI thread we have
            // to call runOnUiThread to do work on UI thread.
            runOnUiThread(new Runnable() {
                public void run() {

                    if (page == eventUrlList.size() - 1) { // In my case the number of pages are 5
                        page = 0;
                        pager.setCurrentItem(0);
                    } else {
                        page = page + 1;
                        pager.setCurrentItem(page);
                    }
                }
            });

        }
    }


}
