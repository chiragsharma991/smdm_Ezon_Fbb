package apsupportapp.aperotechnologies.com.designapp.DashboardSnap;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.UUID;

import apsupportapp.aperotechnologies.com.designapp.AboutUsActivity;
import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment.TransferRequest_Details;
import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.To_Do;
import apsupportapp.aperotechnologies.com.designapp.Constants;
import apsupportapp.aperotechnologies.com.designapp.ConstsCore;

import apsupportapp.aperotechnologies.com.designapp.FCM.TokenRefresh;
import apsupportapp.aperotechnologies.com.designapp.HorlyAnalysis.ProductNameBean;
import apsupportapp.aperotechnologies.com.designapp.LoginActivity1;
import apsupportapp.aperotechnologies.com.designapp.MySingleton;
import apsupportapp.aperotechnologies.com.designapp.ProductInformation.StyleActivity;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesAnalysisActivity1;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesFilterActivity;
import apsupportapp.aperotechnologies.com.designapp.model.EtlStatus;

public class SnapDashboardActivity extends SwitchingActivity implements onclickView {

    public static RecyclerView Recycler_verticalView;
    private String TAG = "SnapDashboardActivity";
    private Context context;
    public static NestedScrollView nestedScrollview;
    RequestQueue queue;
    String userId, bearertoken, geoLeveLDesc,pushtoken;
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
    private TextView RefreshTime;
    public static SnapAdapter snapAdapter;
    private static boolean tokenProcess=false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate: SnapDashboardActivity" );
        super.onCreate(savedInstanceState);
        context = this;
        statusbar();
        m_config = MySingleton.getInstance(context);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        geoLeveLDesc = sharedPreferences.getString("geoLeveLDesc", "");
        pushtoken = sharedPreferences.getString("push_tokken", "");
        Log.e(TAG,"userId :--"+ userId);
        Log.e(TAG,"pushtoken :--"+ pushtoken.toString());
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();
        setContentView(R.layout.activity_snap_dashboard);
        initalise();
        eventUrlList = new ArrayList<>();

        if (geoLeveLDesc.equals("E ZONE"))
        {
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
        setupAdapter();

        if( getIntent().getExtras() != null)
        {
            if(getIntent().getExtras().getString("from").equals("feedback")) {

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
        Log.e(TAG, "onResume: "+device_id+"and token "+TokenRefresh.pushToken);
        if (!tokenProcess){
            if(TokenRefresh.pushToken!=null && !device_id.equals("") && device_id !=null)
                requestSubmitAPI(context,getObject());
            Log.e(TAG, "onResume: !tokenProcess" );
        }


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(TAG, "onNewIntent: " );

    }

    private void statusbar()
    {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.ezfbb_status_bar));
        }
    }

    private void initalise()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Recycler_verticalView = (RecyclerView) findViewById(R.id.recycler_verticalView);
        RefreshTime = (TextView) findViewById(R.id.refreshTime);
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
    public boolean onOptionsItemSelected(MenuItem item)
    {

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
            Intent intent = new Intent(this, LoginActivity1.class);
            startActivity(intent);
            finish();
            NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notifManager.cancelAll();
            return true;
        } else if (id == R.id.aboutus) {
            Intent intent = new Intent(this, AboutUsActivity.class);
            startActivity(intent);
//            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setupAdapter() {
        Log.e(TAG, "setupAdapter: " );

        snapAdapter = new SnapAdapter(context, eventUrlList);

        if (geoLeveLDesc.equals("E ZONE")) {
            List<App> apps = getProduct(21);
            snapAdapter.addSnap(new Snap(Gravity.START, "Sales", apps));
            apps = getProduct(22);
            snapAdapter.addSnap(new Snap(Gravity.START, "Inventory", apps));
            //apps = getProduct(23);
            //snapAdapter.addSnap(new Snap(Gravity.START, "Customer Engagement", apps));
            //apps = getProduct(24);
            //snapAdapter.addSnap(new Snap(Gravity.START, "Hourly Performance", apps));
            // apps = getProduct(25);
            // snapAdapter.addSnap(new Snap(Gravity.START,"Feedback of Customer",apps));


        } else
        {
            // In release build when required hide promo analysis and collabration as per requirement and customer engagement will start from 4 onwards
            // In debug build unhide promo analysis and collabration and numbering will start from 4 onwards
            List<App> apps = getProduct(0);
            snapAdapter.addSnap(new Snap(Gravity.START, "Product Information", apps));
            apps = getProduct(1);
            snapAdapter.addSnap(new Snap(Gravity.START, "Visual Assortment", apps));
            apps = getProduct(2);
            snapAdapter.addSnap(new Snap(Gravity.START, "Sales", apps));
            apps = getProduct(3);
            snapAdapter.addSnap(new Snap(Gravity.START, "Inventory", apps));
            apps = getProduct(4);
            snapAdapter.addSnap(new Snap(Gravity.START, "Promo Analysis", apps));
            apps = getProduct(5);
            snapAdapter.addSnap(new Snap(Gravity.START, "Collaboration", apps));
            apps = getProduct(6);
            snapAdapter.addSnap(new Snap(Gravity.START,"Customer Feedback",apps));
            apps = getProduct(7);
            snapAdapter.addSnap(new Snap(Gravity.START, "Product Feedback", apps));
            apps = getProduct(8);
            snapAdapter.addSnap(new Snap(Gravity.START, "Season Catalogue", apps));
            apps = getProduct(9);
            snapAdapter.addSnap(new Snap(Gravity.START, "Customer Engagement", apps));
            apps = getProduct(10);
            snapAdapter.addSnap(new Snap(Gravity.START,"Boris",apps));

        }
        Recycler_verticalView.setAdapter(snapAdapter);
    }


    @Override
    public void onclickView(int group_position, int child_position) {
        Log.e(TAG, "group_position: " + group_position + "child_position" + child_position);
        int value = Integer.parseInt("" + group_position + "" + child_position);
        moveTo(value, context);


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

                            }
                            else
                            {
                                for (int i = 0; i < response.length(); i++)
                                {
                                    etlStatus = gson.fromJson(response.get(i).toString(), EtlStatus.class);
                                    etlStatusList.add(etlStatus);

                                }
                                RefreshTime.setText(etlStatusList.get(1).getLastETLDate());

                            }

                        }
                        catch (Exception e)
                        {
                            Reusable_Functions.hDialog();

                            RefreshTime.setText("N/A");

                            e.printStackTrace();
                        }
                        Reusable_Functions.hDialog();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

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
    private void checkPermission()
    {
        boolean checkDeviceId = sharedPreferences.getString("device_id", "").equals("") ? true : false;   //true means you not get any device id.

        if (checkDeviceId) {

            if (Reusable_Functions.checkPermission(android.Manifest.permission.READ_PHONE_STATE, this)) {
                Log.e("TAG", ":check permission is okk");
                getDeviceId();
            } else {
                Log.e("TAG", ":check permission calling");
                requestPermissions (new String[]{android.Manifest.permission.READ_PHONE_STATE}, Constants.REQUEST_PERMISSION_WRITE_STORAGE);
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

        requestSubmitAPI(context,getObject());




    }

    public JSONObject getObject() {
        String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("deviceId", device_id);
            jsonObject.put("pushToken",TokenRefresh.pushToken);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "jsonobject: "+jsonObject.toString());

        return jsonObject;
    }


    private void requestSubmitAPI(final Context mcontext, JSONObject object)  // Sender Submit Api call
    {

        if (Reusable_Functions.chkStatus(mcontext)) {
            String url = ConstsCore.web_url + "/v1/submit/deviceID/" + userId ;
            Log.e(TAG, "requestSubmitAPI: "+url );
            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, object.toString(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response == null || response.equals("")) {
                                    Log.e(TAG, "onResponse: null" );
                                } else
                                {
                                    String result=response.getString("status");
                                    tokenProcess=true;
                                    Log.e(TAG, "onResponse: sucess"+result );

                                }
                            } catch (Exception e)
                            {
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

        } else
        {
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
                if (should)
                {
                    showAlert();
                }
                else
                {
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
                .setPositiveButton("RE-TRY", new DialogInterface.OnClickListener()
                {
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

        String url = ConstsCore.web_url + "/v1/display/dashboard/" + userId;
        Log.e(TAG, "requestMarketingEventsAPI: " + url);
        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, "Event Response: " + response.length());

                        try {
                            if (response.equals("") || response == null || response.length() == 0) {
                                Log.e(TAG, "Event Response: null");
                                Reusable_Functions.hDialog();
                                // Toast.makeText(SnapDashboardActivity.this, "No data found", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
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
