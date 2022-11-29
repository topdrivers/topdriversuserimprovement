package com.topdrivers.userv2.FCM;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.topdrivers.userv2.Activities.MainActivity;
import com.topdrivers.userv2.Helper.SharedHelper;
import com.topdrivers.userv2.R;
import com.topdrivers.userv2.Utils.Utilities;

public class MyFirebaseMessagingServiceS extends FirebaseMessagingService {


    private static final String TAG = "MyFirebaseMsgService";
    Utilities utils = new Utilities();

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        try {
            if (remoteMessage.getData() != null) {
                Log.d("TAG", remoteMessage.getData() + "-----");

                utils.print(TAG, "From: " + remoteMessage.getFrom());
                utils.print(TAG, "Notification Message Body: " + remoteMessage.getData());
                utils.print(TAG, "Notification Message Body: " + remoteMessage.getData());
                //Calling method to generate notification
                // String noti = remoteMessage.getNotification().getBody();
                // String data = remoteMessage.getData().get("message");

                sendNotificationSound(remoteMessage.getData().get("message"),
                        remoteMessage.getData().get("is_cancelled"));

            } else {
                utils.print(TAG, "FCM Notification failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String messageBody, String isCancelled) {

        String channelId = "123456";
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Notification", messageBody);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri;
        if (isCancelled.equalsIgnoreCase("true")) {
            soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName()
                    + "/raw/ride_cancelled");
        } else
            soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            notificationBuilder = new NotificationCompat.Builder(this);
        } else {
            notificationBuilder = new NotificationCompat.Builder(this, channelId);
        }

        notificationBuilder.setContentTitle(getString(R.string.app_name))
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setContentIntent(pendingIntent);
//.setPriority(NotificationManager.IMPORTANCE_HIGH)
        notificationBuilder.setSmallIcon(getNotificationIcon(notificationBuilder), 1);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            NotificationChannel notificationChannel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setShowBadge(false);
            notificationChannel.setSound(soundUri, attributes);
            notificationManager.createNotificationChannel(notificationChannel);
            notificationBuilder.setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL);
            notificationBuilder.setChannelId(channelId);
//            notificationBuilder.setSound(soundUri);

            notificationBuilder.build();
                    /*  NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);*/

        }
        notificationManager.notify(123, notificationBuilder.build());

    }

    private void sendNotificationSound(String messageBody, String isCancelled) {
        String CHANNEL_ID = "1234";
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Notification", messageBody);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri;
//        Désolé pour l'attente, nos partenaires sont occupés. merci d'essayer plus tard

        if (messageBody.equals("Votre trajet accepté par un chauffeur")) {
            soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.user_ride_accepted);
        } else if (messageBody.equals("Chauffeur arrivé à votre position")) {
            soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.user_ride_arrived);
        } else if (messageBody.contains("Course réalisée avec succès.")) {
            soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.user_completed_ride);
        } else {
            soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.user_ride_cancelled);
            SharedHelper.putBoolean(getApplicationContext(), "IS_CANCEL", true);
        }
        playNotificationSound(getApplicationContext(), soundUri);

//
//        if (isCancelled.equalsIgnoreCase("true")) {
//            soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.ride_cancelled);/*Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName()
//                    + "/raw/ride_cancelled");*/
//            playNotificationSound(getApplicationContext());
//        } else
//            soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        //For API 26+ you need to put some additional code like below:
        NotificationChannel mChannel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, "Top", NotificationManager.IMPORTANCE_HIGH);
            mChannel.setLightColor(Color.GRAY);
            mChannel.enableLights(true);
            mChannel.setDescription(messageBody);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
//            mChannel.setSound(soundUri, audioAttributes);

            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(mChannel);
            }
        }


        //General code:
        NotificationCompat.Builder status = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        // status.setAutoCancel(true)
        status.setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.logo)
                //.setOnlyAlertOnce(true)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody)
                .setVibrate(new long[]{0, 500, 1000})
                .setDefaults(Notification.DEFAULT_LIGHTS)
//                .setSound(soundUri)
                .setContentIntent(pendingIntent);
        //.setContent(views);

        mNotificationManager.notify(123, status.build());
    }

    private int getNotificationIcon(NotificationCompat.Builder notificationBuilder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(ContextCompat.getColor(getApplicationContext(),
                    R.color.colorPrimary));
            return R.drawable.notification_white;
        } else {
            return R.mipmap.ic_launcher;
        }
    }

    public void playNotificationSound(Context context, Uri notification) {
        try {
//            Uri notification = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.ride_cancelled);/*RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);*/
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
