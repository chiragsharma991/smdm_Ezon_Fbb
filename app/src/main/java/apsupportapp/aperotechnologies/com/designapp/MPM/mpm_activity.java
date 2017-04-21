package apsupportapp.aperotechnologies.com.designapp.MPM;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

import org.json.JSONArray;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.Httpcall.ApiRequest;
import apsupportapp.aperotechnologies.com.designapp.Httpcall.HttpResponse;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesFilterActivity;

public class mpm_activity extends AppCompatActivity implements HttpResponse, View.OnClickListener {

    private SharedPreferences sharedPreferences;
    private String userId;
    private String bearertoken;
    private RequestQueue queue;
    private Context context;
    private int limit = 100;
    private int offsetvalue = 0;
    private String TAG = "mpm_activity";
    private mpm_model model_mpm;
    private String url;
    public static int clickPosition=0;
    private Cache cache;
    private Network network;
    private ListView listView;
    private ArrayList<mpm_model> list;
    private RelativeLayout WebViewProcess;
    private WebView WebViewWrap,WebViewMatch;
    private RelativeLayout mpm_imageBtnBack;
    private mpm_adapter mpmAdapter;
    public mpm_activity pre_activity;
    private LinearLayout WebView_match_layout,WebView_wrap_layout;
    private TextView Toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpm_activity);
        context = this;
        checkCollapsing();
        intialise();


        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            ApiRequest api_request = new ApiRequest(context, bearertoken, url, TAG, cache, network, queue, model_mpm);
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }


    }




    private void intialise() {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Log.e(TAG, "userID and token" + userId + "and this is" + bearertoken);
        cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();

        Toolbar_title=(TextView)findViewById(R.id.toolbar_title);
        WebView_match_layout = (LinearLayout) findViewById(R.id.webView_match_layout);
        WebView_wrap_layout = (LinearLayout) findViewById(R.id.webView_wrap_layout);
        WebView_wrap_layout.setVisibility(View.GONE);
        WebView_match_layout.setVisibility(View.GONE);

        WebViewProcess = (RelativeLayout) findViewById(R.id.webview_process);
        mpm_imageBtnBack = (RelativeLayout) findViewById(R.id.mpm_imageBtnBack);
        WebViewProcess.setVisibility(View.GONE);
        WebViewProcess.setOnClickListener(this);

        WebViewWrap = (WebView) findViewById(R.id.webview_wrap);
        WebViewMatch = (WebView) findViewById(R.id.webview_match);

        url = ConstsCore.web_url + "/v1/display/mpmproducts/" + userId + "?offset=" + offsetvalue + "&limit=" + limit;
        Log.e(TAG, "web_url: " + url);
        listView = (ListView) findViewById(R.id.department_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                if (WebViewProcess.getVisibility() == View.VISIBLE) {
                    Toast.makeText(context, "Please wait file is working above...", Toast.LENGTH_SHORT).show();
                } else {
                    setWebView(position);
                    clickPosition=position;
                    mpmAdapter.notifyDataSetChanged();
                    Log.e(TAG, "clickPosition: in Activity "+clickPosition );

                }
            }
        });


        mpm_imageBtnBack.setOnClickListener(this);
    }

    private void checkCollapsing() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

    }


    @Override
    public void response(ArrayList<mpm_model> list) {

        Log.e(TAG, "response: " + list.size());
        WebView_wrap_layout.setVisibility(View.VISIBLE);
        this.list = list;
        mpmAdapter = new mpm_adapter(context, list);
        listView.setAdapter(mpmAdapter);
        // set web view for read pdf...
        WebViewProcess.setVisibility(View.VISIBLE);

        WebViewWrap.getSettings().setAppCacheMaxSize(5 * 1024 * 1024); // 5MB  /data/user/0/com.project.nat.test123/cache
        WebViewWrap.getSettings().setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
        Log.e(TAG, "setAppCachePath: Wrap " + getApplicationContext().getCacheDir().getAbsolutePath());
        WebViewWrap.getSettings().setAllowFileAccess(true);
        WebViewWrap.getSettings().setBuiltInZoomControls(true);
        WebViewWrap.getSettings().setDisplayZoomControls(false);
        WebViewWrap.getSettings().setAppCacheEnabled(true);
        WebViewWrap.getSettings().setJavaScriptEnabled(true);
        WebViewWrap.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        WebViewWrap.setWebViewClient(new Webview_wrap());


        WebViewMatch.getSettings().setAppCacheMaxSize(5 * 1024 * 1024); // 5MB  /data/user/0/com.project.nat.test123/cache
        WebViewMatch.getSettings().setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
        Log.e(TAG, "setAppCachePath: Match " + getApplicationContext().getCacheDir().getAbsolutePath());
        WebViewMatch.getSettings().setAllowFileAccess(true);
        WebViewMatch.getSettings().setBuiltInZoomControls(true);
        WebViewMatch.getSettings().setDisplayZoomControls(false);
        WebViewMatch.getSettings().setAppCacheEnabled(true);
        WebViewMatch.getSettings().setJavaScriptEnabled(true);
        WebViewMatch.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        WebViewMatch.setWebViewClient(new Webview_Match());


        setWebView(0);


    }

    private void setWebView(int position) {


        Log.e(TAG, "setWebView: " + "http://docs.google.com/gview?embedded=true&url=" + list.get(position).getMpmPath());
        WebViewWrap.loadUrl("http://docs.google.com/gview?embedded=true&url=" + list.get(position).getMpmPath());
        WebViewMatch.loadUrl("http://docs.google.com/gview?embedded=true&url=" + list.get(position).getMpmPath());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mpm_imageBtnBack:
                onBackPressed();
                break;
            case R.id.webview_process:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(WebView_match_layout.getVisibility()==View.VISIBLE)
        {
            Log.e(TAG, "onBackPressed: IN" );
            WebView_wrap_layout.setVisibility(View.VISIBLE);
            WebView_match_layout.setVisibility(View.GONE);
            Toolbar_title.setText("MPM");

        }else
        {
            Log.e(TAG, "onBackPressed: OUT" );
            clickPosition=0;
            finish();
        }
    }

    private class Webview_wrap extends WebViewClient

    {


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            Log.e(TAG, "shouldOverrideUrlLoading: Webview Wrap" + url);


            Reusable_Functions.ViewVisible(WebView_match_layout);
            Reusable_Functions.ViewGone(WebView_wrap_layout);
            Toolbar_title.setText("Mens Party Wear Shirt");

            return true;

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            WebViewProcess.setVisibility(View.VISIBLE);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            WebViewProcess.setVisibility(View.GONE);

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(context, description, Toast.LENGTH_SHORT).show();
        }
    }


    private class Webview_Match extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            Log.e(TAG, "shouldOverrideUrlLoading: Webview Match " + url);
            Reusable_Functions.ViewVisible(WebView_wrap_layout);
            Reusable_Functions.ViewGone(WebView_match_layout);
            Toolbar_title.setText("MPM");

            return true;

        }
    }
}
