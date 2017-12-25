package com.example.maqso.classassignmentsapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MYTAG";
    TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        message = findViewById(R.id.txt_messageView);
//        PusherAndroid pusher = new PusherAndroid("4af87f1577b6ec7bbbae");
//        PushNotificationRegistration nativePusher = pusher.nativePusher();
//        try {
//            nativePusher.registerFCM(this, new PushNotificationRegistrationListener() {
//                @Override
//                public void onSuccessfulRegistration() {
//                    Log.d(TAG, "onSuccessfulRegistration: ");
//                }
//
//                @Override
//                public void onFailedRegistration(int statusCode, String response) {
//                    Log.d(TAG, "onFailedRegistration: " + response);
//                }
//            });
//
//
//        } catch (ManifestValidator.InvalidManifestException e) {
//            e.printStackTrace();
//        }
//        nativePusher.subscribe("banana");
//        nativePusher.setFCMListener(new FCMPushNotificationReceivedListener() {
//            @Override
//            public void onMessageReceived(RemoteMessage remoteMessage) {
//                Log.d(TAG, "onMessageReceived: ");
//            }
//        });
//
//
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
