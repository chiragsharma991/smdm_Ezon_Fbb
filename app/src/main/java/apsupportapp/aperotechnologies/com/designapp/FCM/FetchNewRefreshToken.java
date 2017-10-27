package apsupportapp.aperotechnologies.com.designapp.FCM;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by pamrutkar on 14/09/17.
 */

public class FetchNewRefreshToken extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    public static final String TAG = FetchNewRefreshToken.class.getSimpleName();


    public FetchNewRefreshToken() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {

            // Resets Instance ID and revokes all tokens.
            FirebaseInstanceId.getInstance().deleteInstanceId();


            // Now manually call onTokenRefresh()

            FirebaseInstanceId.getInstance().getToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
