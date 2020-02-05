package com.example.spacenova.fcm;

/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import android.os.PowerManager;
import android.util.Log;

import com.example.spacenova.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.example.spacenova.activity.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

//    private static final String TAG = "FirebaseMsgService";
//
//    // [START receive_message]
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//
//        sendPushNotification(remoteMessage.getData().get("message"));
//    }
//
//    private void sendPushNotification(String message) {
//        System.out.println("received message : " + message);
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher) )
//                .setContentTitle("Push Title ")
//                .setContentText(message)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri).setLights(000000255,500,2000)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
////        PowerManager.WakeLock wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
////        wakelock.acquire(5000);
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//    }

}
