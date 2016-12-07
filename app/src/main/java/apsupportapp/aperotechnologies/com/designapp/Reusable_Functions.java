package apsupportapp.aperotechnologies.com.designapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;



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
            progressDialog = new ProgressDialog(cont, R.style.AppCompatAlertDialogStyle);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
    }

}
