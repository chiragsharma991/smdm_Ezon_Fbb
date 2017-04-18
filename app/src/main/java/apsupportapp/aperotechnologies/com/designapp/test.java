package apsupportapp.aperotechnologies.com.designapp;


        import android.animation.Animator;
        import android.animation.AnimatorListenerAdapter;
        import android.app.ProgressDialog;
        import android.content.ActivityNotFoundException;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.Color;
        import android.net.Uri;
        import android.os.Build;
        import android.os.Environment;
        import android.print.PrintAttributes;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.DisplayMetrics;
        import android.util.Log;
        import android.util.TypedValue;
        import android.view.Display;
        import android.view.KeyEvent;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.ViewAnimationUtils;
        import android.view.ViewTreeObserver;
        import android.view.animation.TranslateAnimation;
        import android.webkit.WebResourceRequest;
        import android.webkit.WebSettings;
        import android.webkit.WebView;
        import android.webkit.WebViewClient;
        import android.widget.LinearLayout;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.io.File;
        import java.util.Calendar;

public class test extends AppCompatActivity {

    private WebView webview_wrap,webview_full;
    private String doc;
    private String check;
    private long startClickTime;
    private long MAX_CLICK_DURATION=200;
    private String TAG="TAG";
    private String url;
    private RelativeLayout halfView,fullView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        halfView=(RelativeLayout)findViewById(R.id.halfView);
        fullView=(RelativeLayout)findViewById(R.id.fullView);
        fullView.setVisibility(View.GONE);
        halfView.setVisibility(View.VISIBLE);

        check="http://www.google.com";
        webview_wrap = (WebView)findViewById(R.id.webview_wrap);
        webview_full = (WebView)findViewById(R.id.webview_fill);

        webview_wrap.getSettings().setAppCacheMaxSize( 5 * 1024 * 1024 ); // 5MB  /data/user/0/com.project.nat.test123/cache
        webview_wrap.getSettings().setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
        Log.e(TAG, "setAppCachePath: "+getApplicationContext().getCacheDir().getAbsolutePath() );
        webview_wrap.getSettings().setAllowFileAccess( true );
        webview_wrap.getSettings().setBuiltInZoomControls(true);
        webview_wrap.getSettings().setDisplayZoomControls(false);
        webview_wrap.getSettings().setAppCacheEnabled( true );
        webview_wrap.getSettings().setJavaScriptEnabled( true );
        webview_wrap.getSettings().setCacheMode( WebSettings.LOAD_DEFAULT );
        webview_wrap.setWebViewClient(new Webview());


        webview_full.getSettings().setAppCacheMaxSize( 5 * 1024 * 1024 ); // 5MB  /data/user/0/com.project.nat.test123/cache
        webview_full.getSettings().setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
        Log.e(TAG, "setAppCachePath: "+getApplicationContext().getCacheDir().getAbsolutePath() );
        webview_full.getSettings().setAllowFileAccess( true );
        webview_full.getSettings().setBuiltInZoomControls(true);
        webview_full.getSettings().setDisplayZoomControls(false);
        webview_full.getSettings().setAppCacheEnabled( true );
        webview_full.getSettings().setJavaScriptEnabled( true );
        webview_full.getSettings().setCacheMode( WebSettings.LOAD_DEFAULT );
        webview_full.setWebViewClient(new Webviewfull());



        webview_wrap.loadUrl("http://docs.google.com/gview?embedded=true&url=https://sm-dm-prod.s3.amazonaws.com/MPM/MENS_WEAR_DJ%26C_LOW.pdf");
        webview_full.loadUrl("http://docs.google.com/gview?embedded=true&url=https://sm-dm-prod.s3.amazonaws.com/MPM/MENS_WEAR_DJ%26C_LOW.pdf");
        // wv.loadUrl("http://stackoverflow.com/questions/36859977/webview-to-display-pdf-file-in-offline-mode");
    }
    public void OnClick(View view)
    {
//"https://sm-dm-prod.s3.amazonaws.com/MPM/KIDS_WEAR.pdf"


    }

    public void secondClick(View view)
    {
        webview_wrap.loadUrl("http://docs.google.com/gview?embedded=true&url=https://sm-dm-prod.s3.amazonaws.com/MPM/MENS_WEAR_DJ%26C_LOW.pdf");


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview_wrap.canGoBack()) {
            Log.e(TAG, "onKeyDown: " );
            webview_wrap.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private class Webview extends WebViewClient
    {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            //  String x=  Uri.parse(String.valueOf(request)).getHost();

            Log.e(TAG, "shouldOverrideUrlLoading: Webview "+url );

            // fullView.setVisibility(View.VISIBLE);
            ViewVisible(fullView);
            ViewGone(halfView);


            // halfView.setVisibility(View.GONE);
            return true;

        }




    }



    private class Webviewfull extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            Log.e(TAG, "shouldOverrideUrlLoading: Webviewfull "+url );

            // fullView.setVisibility(View.GONE);
            // halfView.setVisibility(View.VISIBLE);
            ViewVisible(halfView);
            ViewGone(fullView);


            return true;

        }
    }

    @Override
    public void onBackPressed() {
        if(fullView.getVisibility()==View.VISIBLE)
        {
            //fullView.setVisibility(View.VISIBLE);
            // halfView.setVisibility(View.GONE);
            ViewVisible(halfView);
            ViewGone(fullView);
        }else
        {
            finish();
        }

    }

    public static void ViewVisible (View view)
    {
        // previously invisible view
        //View myView = findViewById(R.id.my_view);

// get the center for the clipping circle
        int cx = view.getWidth() / 2;
        int cy = view.getHeight() / 2;

// get the final radius for the clipping circle
        float finalRadius = (float) Math.hypot(cx, cy);

// create the animator for this view (the start radius is zero)
        Animator anim =
                null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
        }

// make the view visible and start the animation
        view.setVisibility(View.VISIBLE);
        anim.start();
    }

    public static void ViewGone (final View view)
    {
        // previously visible view
        //  final View myView = findViewById(R.id.my_view);

// get the center for the clipping circle
        int cx = view.getWidth() / 2;
        int cy = view.getHeight() / 2;

// get the initial radius for the clipping circle
        float initialRadius = (float) Math.hypot(cx, cy);

// create the animation (the final radius is zero)
        Animator anim =
                null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);
        }

// make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
            }
        });

// start the animation
        anim.start();


    }
}
