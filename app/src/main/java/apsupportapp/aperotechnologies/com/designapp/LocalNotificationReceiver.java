package apsupportapp.aperotechnologies.com.designapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class LocalNotificationReceiver extends BroadcastReceiver {
    Context cont;

    @Override
    public void onReceive(final Context context, Intent intent) {
        cont = context;
        //Boolean isApplicationForeGround = BaseLifeCycleCallbacks.applicationStatus();
//        if (isApplicationForeGround == true)
//        {

        Intent i = new Intent(context, TransparentActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

//        }
//        else
//        {
//
//        }

    }

    public static void CreateNotification(Intent intent, String message, Context context) {
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Random myRandom = new Random();
//        int notId = myRandom.nextInt();
//
//        NotificationCompat.Builder mBuilder = null;
//        // application is in background or close
//        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);
//        mBuilder = new NotificationCompat.Builder(context)
//                .setSmallIcon(R.drawable.common_signin_btn_icon_dark)
//                .setContentTitle("AftrParties")
//                .setContentText(message);
//
//        mBuilder.setDeleteIntent(contentIntent);
//        mBuilder.setContentIntent(contentIntent);
//        mBuilder.setAutoCancel(true);
//
//        notificationManager.notify(notId, mBuilder.build());


    }
}
