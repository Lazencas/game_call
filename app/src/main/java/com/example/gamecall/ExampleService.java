package com.example.gamecall;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


public class ExampleService extends Service {
    private static final String TAG = "ExampleService";
    String text;
    int nos, stat;
    private Thread mThread;
private int mCount=0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        
//        String input = intent.getStringExtra("inputExtra");
        SharedPreferences sharedPref = getSharedPreferences("shared", Context.MODE_PRIVATE);

        text = sharedPref.getString("lasttext", " ");
        nos = sharedPref.getInt("mesnotify",0);

            //처음 알림설정에서는 안울리게

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle("GameCall");
            builder.setContentText("메세지 알림 설정이 되었습니다.");

            builder.setSound(null);
        builder.setAutoCancel(true);

//            Intent intentt = new Intent(this, talk.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this,
//                    0,
//                    intentt,
//                    PendingIntent.FLAG_UPDATE_CURRENT);
//
//            builder.setContentIntent(pendingIntent);
//            builder.setColor(Color.RED);
//            Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this,
//                    RingtoneManager.TYPE_NOTIFICATION);
//            builder.setSound(ringtoneUri);
//            builder.setAutoCancel(true);


        if("message".equals(intent.getAction())) {
            NotificationCompat.Builder builder2 = new NotificationCompat.Builder(this, "default");
            builder2.setSmallIcon(R.mipmap.ic_launcher);
            builder2.setContentTitle("GameCall");
            builder2.setContentText(text);

            Intent intentt = new Intent(this, talk.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    0,
                    intentt,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            builder2.setContentIntent(pendingIntent);
            builder2.setColor(Color.RED);
            Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this,
                    RingtoneManager.TYPE_NOTIFICATION);
            builder2.setSound(ringtoneUri);
            builder2.setAutoCancel(true);


            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                manager.createNotificationChannel(new NotificationChannel("default", "기본채널",
                        NotificationManager.IMPORTANCE_DEFAULT));
            }


            manager.notify(2, builder2.build());
        }

            startForeground(1, builder.build());







        return START_NOT_STICKY;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //알림로직
    public void show(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("GameCall");
        builder.setContentText(text);

        Intent intent =  new Intent(this, talk.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        builder.setColor(Color.RED);
        Uri ringtoneUri  = RingtoneManager.getActualDefaultRingtoneUri(this,
                RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(ringtoneUri);
        builder.setAutoCancel(true);

        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("default","기본채널",
                     NotificationManager.IMPORTANCE_DEFAULT));
        }

        manager.notify(2, builder.build());



    }

    private void startForegroundService(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default2");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("포그라운드서비스");
        builder.setContentText("포그라운드 서비스 실행 중");
        builder.setAutoCancel(true);

        Intent notification = new Intent(this, talk.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notification, 0);
        builder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("default2","기본채널2",
                    NotificationManager.IMPORTANCE_DEFAULT));
        }

        startForeground(1, builder.build());

    }



}
