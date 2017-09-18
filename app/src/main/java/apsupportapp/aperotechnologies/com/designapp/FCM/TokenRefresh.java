package apsupportapp.aperotechnologies.com.designapp.FCM;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import apsupportapp.aperotechnologies.com.designapp.DashboardSnap.SnapDashboardActivity;

/**
 * Created by csuthar on 21/08/17.
 */

public class TokenRefresh extends FirebaseInstanceIdService {

    private SharedPreferences sharedPreferences;
    public static String pushToken=null;

    @Override
    public void onTokenRefresh()
    {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("TAG", "Refreshed token:------ " + refreshedToken);
        // TODO: Implement this method to send any registration to your app's servers.
         sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        pushToken=refreshedToken;
        SnapDashboardActivity.tokenProcess=false;

    }
}
