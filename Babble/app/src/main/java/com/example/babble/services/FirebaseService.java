package com.example.babble.services;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.babble.API.FirebaseAPI;
import com.example.babble.MyApplication;
import com.example.babble.R;

import com.example.babble.activities.ChatActivity;
import com.example.babble.entities.Message;
import com.example.babble.utilities.FirebaseMessagingCallback;
import com.example.babble.utilities.RequestCallBack;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;


public class FirebaseService extends FirebaseMessagingService {

    private final FirebaseAPI firebaseAPI;
    private final Context context;

    private static FirebaseMessagingCallback messagingCallback;

    public FirebaseService() {
        this.firebaseAPI = new FirebaseAPI(MyApplication.context);
        this.context = MyApplication.context;
    }

    public static void setMessagingCallback(FirebaseMessagingCallback callback) {
        messagingCallback = callback;
    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {

            // for ui update - received message.
            if (remoteMessage.getData().containsKey("message")) {
                String messageData = remoteMessage.getData().get("message");
                if (messageData != null) {
                    try {
                        JSONObject messageJson = new JSONObject(messageData);
                        String chatId = messageJson.getString("chatId");

                        createNotificationChannel();

                        // create an intent that moves to the chat activity
                        Intent intent = new Intent(context, ChatActivity.class);
                        intent.putExtra("chatId", chatId);
                        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                        int notificationId = (int) System.currentTimeMillis(); // Generate unique notification ID

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1")
                                .setSmallIcon(R.mipmap.babblelogo)
                                .setContentTitle(remoteMessage.getNotification().getTitle())
                                .setContentText(remoteMessage.getNotification().getBody())
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                            Log.d("Notification", "No permission.");
                        } else {
                            notificationManager.notify(notificationId, builder.build());
                        }
                        // update UI regardless of permission.
                        Message newMessage = buildMessage(messageJson);
                        messagingCallback.updateUI(newMessage, messageJson.getString("senderUsername"));

                    } catch(Exception e) {
                        Log.d("Notification", "error! " + e.getMessage());
                    }
                }
            }
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        firebaseAPI.setFirebaseToken(token, new RequestCallBack() {});
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", "Babble", importance);
            channel.setDescription("receiving messages.");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void createNewToken(Activity activity) {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(activity, instanceIdResult -> {
            String token = instanceIdResult.getToken();
            firebaseAPI.setFirebaseToken(token, new RequestCallBack() {
            });
        });
    }

    private Message buildMessage(JSONObject messageJson) {
        try {
            String chatId = messageJson.getString("chatId");
            String content = messageJson.getString("content");
            String timeSent = messageJson.getString("timeSent");
            String extendedTime = messageJson.getString("extendedTime");

            return new Message(content, chatId, timeSent, extendedTime, false);
        } catch (JSONException e) {
            e.printStackTrace();
            return null; // Return null or handle the error accordingly
        }
    }
}