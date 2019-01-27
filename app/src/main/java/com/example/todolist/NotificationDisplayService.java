package com.example.todolist;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class NotificationDisplayService extends Service {

    final int NOTIFICATION_ID = 16;
    public NotificationDisplayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        displayNotification("You just added a new activity","Don't forget to finish it :)");
        return super.onStartCommand(intent,flags,startId);
    }

    private void displayNotification(String title, String text){

        Intent notificationIntent = new Intent(this,Home.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.very)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.large))
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setVibrate(new long[]{0,300,300,300})
                .setLights(Color.WHITE,1000,5000)
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text));

        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID,notification.build());
    }
}
