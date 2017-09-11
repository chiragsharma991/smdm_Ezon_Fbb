package apsupportapp.aperotechnologies.com.designapp.FCM;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

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
    private String title,message;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.




        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e("TAG", "Notification Body:-- " + remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e("TAG", "Data Payload: --" + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e("TAG", "Exception: " + e.getMessage());
            }
        }
    }

    private void handleDataMessage(JSONObject json) {
        try {

            String title = json.getString("title");
            String message = json.getString("message");
            sendNotification(title,message);


        } catch (JSONException e) {
            Log.e("TAG", "JSONException: "+e.getMessage() );
            e.printStackTrace();
        }

    }

    private void sendNotification(final String title, String message) {

        this.title=title;
        this.message=message;
        UiTask task=new UiTask(this);
        task.execute();


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
            new NotificationBuild(title,message,context);

        }
    }
}
