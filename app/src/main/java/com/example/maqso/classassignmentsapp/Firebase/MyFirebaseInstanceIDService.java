package com.example.maqso.classassignmentsapp.Firebase;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by maqso on 12/25/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        SharedPreferences sp = getSharedPreferences("USER_CREDIENTIAL", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Log.d("MYTAG", "sendRegistrationToServer: " + refreshedToken);
        editor.putString("DEVICE_TOKEN", refreshedToken);
        editor.apply();
        //   EventBus.getDefault().postSticky(new MyEvents.DeviceToken(refreshedToken));
    }
}
