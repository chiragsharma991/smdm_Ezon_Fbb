package apsupportapp.aperotechnologies.com.designapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Toast;


public class Reusable_Functions {

    static ProgressDialog progressDialog = null;

    public static boolean chkStatus(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //noinspection deprecation,deprecation
        final NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        //noinspection deprecation,deprecation
        final NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return wifi.isConnectedOrConnecting() || mobile.isConnectedOrConnecting();
    }

    public static void hDialog() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog.cancel();
                progressDialog = null;
                Log.e("progressDialog hDialog  ", " " + progressDialog);
            }
        }
    }

    public static void sDialog(Context cont, String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(cont);  //  R.style.AlertDialog_Theme);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            if (!progressDialog.isShowing()) {
                progressDialog.show();

            }
        }
    }

    public static void ViewVisible (View view)
    {
        if (Build.VERSION.SDK_INT >= 21) {

            int cx = view.getWidth() / 2;
            int cy = view.getHeight() / 2;

            float finalRadius = (float) Math.hypot(cx, cy);

            Animator anim =
                    ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);

            view.setVisibility(View.VISIBLE);
            anim.start();

        }else{
            view.setVisibility(View.VISIBLE);

        }
    }

    public static void ViewGone (final View view)
    {
        if (Build.VERSION.SDK_INT >= 21) {

            int cx = view.getWidth() / 2;
            int cy = view.getHeight() / 2;

            float initialRadius = (float) Math.hypot(cx, cy);

            Animator anim =
                    ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.GONE);
                }
            });

            anim.start();

        }else{
            view.setVisibility(View.GONE);

        }

    }

    public static void  MakeToast(Context context,String info){

        Toast.makeText(context,info, Toast.LENGTH_SHORT).show();

    }
}
