package apsupportapp.aperotechnologies.com.designapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;


import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


public class Reusable_Functions  {

    public static ProgressDialog progressDialog = null;

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
            }
        }
    }

    public static void sDialog(Context cont, String message)
    {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(cont);//R.style.AlertDialog_Theme);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            if (!progressDialog.isShowing())
            {
               progressDialog.show();

            }
        }
    }

    public static void displayToast(final Context context, final String msg)
    {

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) ((Activity)context).findViewById(R.id.custom_container));

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(msg);

        Toast toast = Toast.makeText(context, ""+msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.setView(layout);
        toast.show();

    }

    public static void showSnackbar(View view, String msg){
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }

    public static void showSnackbarError(Context context,View view, String msg){
        Snackbar snack = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
        view = snack.getView();

        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.parseColor("#ffffff"));
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.ezfbb_red));
        snack.show();    }

    public static void showSnackbarSuccess(Context context,View view, String msg){
        Snackbar snack = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
        view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.parseColor("#ffffff"));
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.smdm_actionbar));


        snack.show();    }

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

    public static void animateScaleOut(final View view){
        if(!view.isShown()) {
            view.setScaleX(0.2f);
            view.setScaleY(0.2f);
            view.animate()
                    .setStartDelay(200)
                    .alpha(1)
                    .scaleX(1)
                    .scaleY(1).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }


    public static void animateScaleIn(final View view){
        view.setScaleX(1);
        view.setScaleY(1);
        view.animate()
                .setStartDelay(100)
                .alpha(1)
                .scaleX(0.1f)
                .scaleY(0.1f).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public static boolean checkPermission(String strPermission, Context context){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int result = ContextCompat.checkSelfPermission(context, strPermission);
            if (result == PackageManager.PERMISSION_GRANTED){
                return true;
            } else {
                return false;
            }
        }
        return true;
    }











}
