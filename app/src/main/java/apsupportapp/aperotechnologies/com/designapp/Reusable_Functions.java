package apsupportapp.aperotechnologies.com.designapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by ifattehkhan on 23/08/16.
 */
public class Reusable_Functions {

    static ProgressDialog progressDialog = null;


    public static  boolean chkStatus(Context context)
    {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return wifi.isConnectedOrConnecting() || mobile.isConnectedOrConnecting();

    }
    public static void hDialog(){
     //   Log.e("progressDialog in hDialog "+progressDialog," ");
        if(progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog.cancel();
                progressDialog = null;
                Log.e("progressDialog  "+progressDialog," ");
            }
        }
    }



    public static void sDialog(Context cont, String message){
      //  Log.e("progressDialog in sDialog "+progressDialog," ");
        if(progressDialog == null) {
            progressDialog = new ProgressDialog(cont);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }    }
}
