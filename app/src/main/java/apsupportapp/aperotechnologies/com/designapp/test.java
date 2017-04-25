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

import apsupportapp.aperotechnologies.com.designapp.Httpcall.ApiRequest;
import apsupportapp.aperotechnologies.com.designapp.MPM.mpm_model;

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






    }

}
