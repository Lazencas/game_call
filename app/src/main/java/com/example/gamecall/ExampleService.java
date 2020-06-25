package com.example.gamecall;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import static com.example.gamecall.App.CHANNEL_ID;




public class ExampleService extends Service {




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        String input = intent.getStringExtra("inputExtra");
        SharedPreferences sharedPref = getSharedPreferences("shared", Context.MODE_PRIVATE);
        String text = sharedPref.getString("lasttext", " ");

        Intent notiintent = new Intent(this, talk.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notiintent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("GameCall")
            .setContentText(text)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(2, notification);

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



}
