package com.example.maqso.classassignmentsapp.Firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.maqso.classassignmentsapp.R;
import com.example.maqso.classassignmentsapp.StudentHomeActivity;
import com.example.maqso.classassignmentsapp.TeacherHomeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FireBaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MYTAG";
    SharedPreferences sp;

    public FireBaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived: " + remoteMessage.getNotification().getBody());
        onNotify(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
    }

    public void onNotify(String title, String message) {
        Intent intent;
        sp = getSharedPreferences("USER_CREDIENTIAL", MODE_PRIVATE);
        if (sp.getString("USER_ROLE", "").equals("teacher")) {
            intent = new Intent(this, TeacherHomeActivity.class);
        } else {
            intent = new Intent(this, StudentHomeActivity.class);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setContentText(message);

        notificationManager.notify(1, notificationBuilder.build());
    }
}
