package com.example.baselineapp;
import android.app.NotificationChannel ;
import android.app.NotificationManager ;
import android.app.Service ;
import android.content.Intent ;
import android.content.pm.PackageManager;
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
public class NotificationService extends Service
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
        startTimer() ;
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
        stopTimerTask() ;
        super .onDestroy() ;
    }
    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler() ;
    public void startTimer ()
    {
        timer = new Timer() ;
        initializeTimerTask() ;
        timer .schedule( timerTask , 5000 , Your_X_SECS * 1000 ) ; //
    }
    public void stopTimerTask ()
    {
        if ( timer != null ) {
            timer .cancel() ;
            timer = null;
        }
    }
    public void initializeTimerTask ()
    {
        timerTask = new TimerTask()
        {
            public void run ()
            {
                handler .post( new Runnable() {
                    public void run ()
                    {
                        createNotification() ;
                    }
                }) ;
            }
        } ;
    }
    private void createNotification ()
    {
        // Check if the permission for POST_NOTIFICATION is provided or not
        if (ActivityCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED)
        {
            System.out.println("Permission denied");
            return;
        }

        NotificationManager mNotificationManager = (NotificationManager) getSystemService( NOTIFICATION_SERVICE ) ;

        NotificationCompat.Builder notif_babyWarning = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_dashboard_black_24dp)
                .setContentTitle("Baby Alert")
                .setContentText("Health warning detected")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("WARNING - An alert has been generated based on the health data of your baby."))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true);

        NotificationCompat.Builder notif_babyCaution = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_dashboard_black_24dp)
                .setContentTitle("Baby Caution")
                .setContentText("Health abnormality detected")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("CAUTION - An alert has been generated based on the health data of your baby."))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            notif_babyWarning.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel) ;
        }

        System.out.println("Notifying");
        assert mNotificationManager != null;
        mNotificationManager.notify(( int ) System. currentTimeMillis () , notif_babyWarning.build()) ;
        System.out.println("Notified");
    }
}




/*
import static android.icu.number.NumberFormatter.with;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.baselineapp.MainActivity;
import com.example.baselineapp.NotificationSettings;

import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {

    Timer timer;
    TimerTask timerTask;
    String TAG = "Timers";
    int Your_X_SECS = 5;


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        startTimer();

        return START_STICKY;
    }


    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");


    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        stoptimertask();
        super.onDestroy();


    }

    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();


    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 5000, Your_X_SECS * 1000); //
        //timer.schedule(timerTask, 5000,1000); //
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {

                        //TODO CALL NOTIFICATION FUNC
                        //YOURNOTIFICATIONFUNCTION();
                        NotificationFunction();

                        // finally notifying the notification

                        // Check if the permission for POST_NOTIFICATION is provided or not
                        if (ActivityCompat.checkSelfPermission(
                                getApplicationContext(),
                                Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED)
                        {
                            //ActivityCompat.requestPermissions();

                            return;
                        }
                        NotificationManagerCompat.from(getApplicationContext()).notify();
                    }
                });
            }
        };
    }

    void NotificationFunction()
    {

    }
}
*/