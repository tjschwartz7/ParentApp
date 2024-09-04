package com.example.baselineapp;
import static com.example.baselineapp.NotificationService.NOTIFICATION_CHANNEL_ID;

import android.app.NotificationChannel ;
import android.app.NotificationManager ;
import android.app.Service ;
import android.content.Intent ;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler ;
import android.os.IBinder ;
import android.Manifest;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.util.Log ;
import java.util.Timer ;
import java.util.TimerTask ;
public class NotificationSetup extends Service
{
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    Timer timer ;
    TimerTask timerTask ;
    String TAG = "Timers" ;
    int Your_X_SECS = 5 ;
    @Override
    public IBinder onBind (Intent arg0)
    {
        return null;
    }
    @Override
    public int onStartCommand (Intent intent , int flags , int startId) {
        Log. e ( TAG , "onStartCommand" ) ;
        super .onStartCommand(intent , flags , startId) ;
        setupNotification(); ;
        return START_STICKY ;
    }
    @Override
    public void onCreate ()
    {
        Log. e ( TAG , "onCreate" ) ;
    }
    @Override
    public void onDestroy ()
    {
        Log. e ( TAG , "onDestroy" ) ;
        super .onDestroy() ;
    }
    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler() ;

    private void setupNotification ()
    {
        //Code goes here

        System.out.println("Creating notification service.");
        createNotificationChannel();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService( new Intent( this, NotificationService. class )) ;
        }
        else {
            startService( new Intent( this, NotificationService. class )) ;
        }
    }

    private void createNotificationChannel() {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.str_channelName);
            String description = getString(R.string.str_channelDescription);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}



