package apsupportapp.aperotechnologies.com.designapp.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;

import apsupportapp.aperotechnologies.com.designapp.DashboardSnap.SnapDashboardActivity;
import apsupportapp.aperotechnologies.com.designapp.LoginActivity1;
import apsupportapp.aperotechnologies.com.designapp.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by csuthar on 21/08/17.
 */

public class NotificationBuild extends AppCompatActivity {


    private SharedPreferences sharedPreferences;
    private Intent intent;

    public NotificationBuild(String title,String message, Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String userId = sharedPreferences.getString("userId", "");
        if (userId != null && !userId.equals("")) {
            intent = new Intent(context, SnapDashboardActivity.class);
        } else {
            intent = new Intent(context, SnapDashboardActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //FLAG_ACTIVITY_SINGLE_TOP for unrefresh

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
       /* PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_NO_CREATE);*/

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setOngoing(true)
                .setSmallIcon(R.mipmap.notification_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
                //.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);


        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
