package apsupportapp.aperotechnologies.com.designapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.Random;


public class LocalNotificationReceiver extends BroadcastReceiver {
    public static boolean logoutAlarm=false;
    public static int notId;
    Context cont;

    @Override
    public void onReceive(final Context context, Intent intent) {
        cont = context;
        Boolean isApplicationForeGround = BaseLifeCycleCallbacks.applicationStatus();
        if (isApplicationForeGround)
        {
        Intent i = new Intent(context, TransparentActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        }
        else
        {
            Intent logoutIntent=new Intent(context,LoginActivity1.class);
            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            CreateNotification(logoutIntent, "Your session is about to expire. Please logout.", context);
        }
    }

    public static void CreateNotification(Intent intent, String message, Context context) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Random myRandom = new Random();
        notId = myRandom.nextInt();
        logoutAlarm = true;

        NotificationCompat.Builder mBuilder;
        // application is in background or close
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);
        mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.notification_logo)
                .setContentTitle("Engage 24x7")
                .setContentText(message);

        mBuilder.setDeleteIntent(contentIntent);
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setAutoCancel(true);
        notificationManager.notify(notId, mBuilder.build());
    }

}
