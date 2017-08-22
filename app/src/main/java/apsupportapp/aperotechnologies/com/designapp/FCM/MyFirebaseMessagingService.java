package apsupportapp.aperotechnologies.com.designapp.FCM;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.net.URL;

import apsupportapp.aperotechnologies.com.designapp.DashboardSnap.SnapDashboardActivity;
import apsupportapp.aperotechnologies.com.designapp.LoginActivity1;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Utils.NotificationBuild;
import io.fabric.sdk.android.services.concurrency.AsyncTask;

/**
 * Created by csuthar on 21/08/17.
 */


public class MyFirebaseMessagingService extends FirebaseMessagingService
{


    private SharedPreferences sharedPreferences;
    private Intent intent;
    private  Handler handler;
    private String messageBody;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Log.e("TAG", "From:-------- " + remoteMessage.getFrom());
        Log.e("TAG", "Notification Message Body:------- " + remoteMessage.getNotification().getBody());
        sendNotification(remoteMessage.getNotification().getBody().toString());
    }

    private void sendNotification(final String messageBody) {

        this.messageBody=messageBody;
        UiTask task=new UiTask(this);
        task.execute();


       /* sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userId = sharedPreferences.getString("userId", "");
        if(userId !=null && !userId.equals("")){
            intent = new Intent(this, SnapDashboardActivity.class);
        } else{
            intent = new Intent(this, LoginActivity1.class);
        }

        //Intent intent = new Intent(this, LoginActivity1.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Firebase Push Notification")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());*/


    }

    public  class UiTask extends AsyncTask<URL, Integer, Long>{


        private final Context context;

        public UiTask(Context context) {
            this.context=context;
        }

        @Override
        protected Long doInBackground(URL... urls) {
            return null;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            new NotificationBuild(messageBody,context);

        }
    }
}
