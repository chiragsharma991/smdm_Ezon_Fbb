package apsupportapp.aperotechnologies.com.designapp.FCM;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by pamrutkar on 14/09/17.
 */

public class ContCreateTokenService  extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent serviceIntent = new Intent(this, MyFirebaseMessagingService.class);
        startService(serviceIntent);
        return START_REDELIVER_INTENT;
    }
}
