package apsupportapp.aperotechnologies.com.designapp;

/**
 * Created by hasai on 27/09/16.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by hasai on 01/07/16.
 */
public class TransparentActivity extends Activity {


    Context context;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder
                .setTitle("Exit")
                .setMessage("Your session is about to expire. Please logout.")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.commit();
                                finish();
                                Intent i = new Intent(TransparentActivity.this, LoginActivity.class);
                                // set the new task and clear flags
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);

                            }
                        });

        alertDialogBuilder.show();


    }


    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        return;
    }


    @Override
    protected void onStop() {

        super.onStop();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        finish();
        Intent i = new Intent(TransparentActivity.this, LoginActivity.class);
        // set the new task and clear flags
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);




    }


}