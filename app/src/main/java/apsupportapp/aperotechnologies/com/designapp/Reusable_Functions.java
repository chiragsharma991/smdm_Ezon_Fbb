package apsupportapp.aperotechnologies.com.designapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.Calendar;

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
                Log.e("progressDialog hDialog  "," "+progressDialog);
            }
        }
    }



    public static void sDialog(Context cont, String message){
        if(progressDialog == null) {
            progressDialog = new ProgressDialog(cont);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
    }


    public static long getCurrentTime()
    {
        final Calendar calendar = Calendar.getInstance();
        int startDate = calendar.get(Calendar.DAY_OF_MONTH);
        int startMon = calendar.get(Calendar.MONTH);
        int startYear = calendar.get(Calendar.YEAR);
        int startHour = calendar.get(Calendar.HOUR_OF_DAY);
        int startMin = calendar.get(Calendar.MINUTE);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.DAY_OF_MONTH, startDate);
        calendar1.set(Calendar.MONTH, startMon);
        calendar1.set(Calendar.YEAR, startYear);
        calendar1.set(Calendar.HOUR_OF_DAY, startHour);
        calendar1.set(Calendar.MINUTE, startMin);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);

        long TimeinMs = calendar1.getTimeInMillis();
        Log.e("getTime " + TimeinMs, "");
        return TimeinMs;
    }

}
