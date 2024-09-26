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

import android.util.Log ;

import java.util.Calendar;
import java.util.Timer ;
import java.util.TimerTask ;
public class NotificationService extends Service {
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    private static boolean bool_connectionErrorNotifiedFlag = false;
    Timer shortTimer;
    Timer longTimer;
    TimerTask shortTimerTask ;
    TimerTask dailyTimerTask ;
    String TAG = "Timers" ;
    int timer_20_s = 20 ;
    int timer_10_s = 10 ;
    int timer_5_s = 5 ;
    int timer_day_s = 86400;

    boolean warning_hasBeenActive5s = false;
    boolean caution_hasBeenActive5s = false;

    NotificationManager mNotificationManager;


    NotificationCompat.Builder notif_babyWarning = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_dashboard_black_24dp)
            .setContentTitle("Baby Alert")
            .setContentText("Health warning detected")
            .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText("WARNING - An alert has been generated based on the health data of  " + Globals.getBabyFirstName() + "."))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true);

    NotificationCompat.Builder notif_babyCaution = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_dashboard_black_24dp)
            .setContentTitle("Baby Caution")
            .setContentText("Health abnormality detected")
            .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText("CAUTION - An alert has been generated based on the health data of " + Globals.getBabyFirstName() + "."))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true);

    NotificationCompat.Builder notif_notificationsActive = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_dashboard_black_24dp)
            .setContentTitle("Notifications Enabled")
            .setContentText("Your baby is safe with us")
            .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText("Notifications will be sent if anything seems off."))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true);

    NotificationCompat.Builder notif_nannyNotConnected = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_dashboard_black_24dp)
            .setContentTitle("Not receiving baby data")
            .setContentText("Having trouble connecting to the Nanny.")
            .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText("Try making sure you're wi-fi is working."))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true);

    @Override
    public IBinder onBind (Intent arg0)
    {
        return null;
    }
    @Override
    public int onStartCommand (Intent intent , int flags , int startId) {
        Log. e ( TAG , "onStartCommand" ) ;
        super .onStartCommand(intent , flags , startId) ;

        startForeground(1, notif_notificationsActive.build());
        //Start two timer tasks
        //One handles notifications
        startTimer() ;
        //The other handles daily threshold updates
        //startDailyTimer();
        return START_NOT_STICKY ;
    }
    @Override
    public void onCreate ()
    {

        Log. e ( TAG , "onCreate" ) ;
        mNotificationManager = (NotificationManager) getSystemService( NOTIFICATION_SERVICE ) ;

        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            notif_babyWarning.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel) ;
        }
    }
    @Override
    public void onDestroy ()
    {
        try
        {
            Log.e(TAG, "onDestroy");

            stopForeground(true);

            stopTimerTask();
        } catch (Exception e) {
            Log.e(TAG, "Error in onDestroy: " + e.getMessage());
            e.printStackTrace(); // Print the stack trace to understand where the issue is
        } finally {
            super.onDestroy(); // Always call super.onDestroy() in the end
        }
    }
    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler() ;
    public void startTimer () {
        shortTimer = new Timer() ;
        longTimer = new Timer();
        initializeTimerTasks() ;

        shortTimer.schedule( shortTimerTask , 5000 , timer_10_s * 1000 ) ; //
        longTimer.schedule( dailyTimerTask , 5000 , timer_day_s * 1000 ) ; //
    }
    public void stopTimerTask () {
        if ( shortTimer != null ) {
            shortTimer.cancel(); // Stop the timer
            shortTimer.purge();  // Clean up canceled tasks
            shortTimer = null;
        }

        if ( longTimer != null ) {
            longTimer.cancel(); // Stop the timer
            longTimer.purge();  // Clean up canceled tasks
            longTimer = null;
        }
    }
    public void initializeTimerTasks () {
        shortTimerTask = new TimerTask()
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

        dailyTimerTask = new TimerTask()
        {
            public void run ()
            {
                handler .post( new Runnable() {
                    public void run ()
                    {
                        handleThresholds() ;
                    }
                }) ;
            }
        } ;
    }
    private void createNotification () {
        double pulse = Globals.getPulseVal();
        double temp = Globals.getTempVal();
        double bloodOx = Globals.getBloodOxVal();

        //Debug only
        //Globals.debugOnlySetVitals(++bloodOx, ++pulse, ++temp);
        // Check if the permission for POST_NOTIFICATION is provided or not
        if (ActivityCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED)
        {
            System.out.println("Permission denied");
            return;
        }

        //If client is connected and we've sent the connection error flag,
        //we can reset it now for future disconnects.
        if(Globals.getClientIsConnected() && bool_connectionErrorNotifiedFlag)
        {
            bool_connectionErrorNotifiedFlag = false;
        }

        if(Globals.getClientIsConnected())
            sendBabyVitalsNotifications();
        else if(!bool_connectionErrorNotifiedFlag) //If we haven't already sent the warning
        {
            //Notify
            assert mNotificationManager != null;
            mNotificationManager.notify(( int ) System. currentTimeMillis () , notif_nannyNotConnected.build()) ;
            bool_connectionErrorNotifiedFlag = true;
        }



    }

    void sendBabyVitalsNotifications()
    {
        //Read lots of data from Globals for readability sake
        boolean pulseWarning    = Globals.getPulseVal() >= Globals.getPulseHighWarningThreshold() ||
                Globals.getPulseVal() <= Globals.getPulseLowWarningThreshold();
        boolean tempWarning     = Globals.getTempVal() >= Globals.getTempHighWarningThreshold() ||
                Globals.getTempVal() <= Globals.getTempLowWarningThreshold();
        boolean bloodOxWarning  = Globals.getBloodOxVal() <= Globals.getBloodOxLowWarningThreshold();

        boolean pulseCaution    = Globals.getPulseVal() >= Globals.getPulseHighCautionThreshold() ||
                Globals.getPulseVal() <= Globals.getPulseLowCautionThreshold();
        boolean tempCaution     = Globals.getTempVal() >= Globals.getTempHighCautionThreshold() ||
                Globals.getTempVal() <= Globals.getTempLowCautionThreshold();
        boolean bloodOxCaution  = Globals.getBloodOxVal() <= Globals.getBloodOxLowCautionThreshold();
        boolean isWarningActive = Globals.isWarningActive();
        boolean isCautionActive = Globals.isCautionActive();

        //If there is a warning active (priority over cautions!)
        if(pulseWarning || tempWarning || bloodOxWarning)
        {
            //If the warning has already been notified, don't notify again
            if(!isWarningActive)
            {
                //Warning has been active for one full tick
                if(warning_hasBeenActive5s)
                {
                    //Notify
                    assert mNotificationManager != null;
                    mNotificationManager.notify(( int ) System. currentTimeMillis () , notif_babyWarning.build()) ;
                    Globals.setWarningActive(true);
                    Globals.setCautionActive(false);
                    warning_hasBeenActive5s = false;

                    //Handle notification logging
                    String msg = "";

                    if(pulseWarning) msg += ("Pulse out of range - " +  String.valueOf(Globals.getPulseVal())) + "\n";
                    if(tempWarning) msg += ("Temperature out of range - " +  String.valueOf(Globals.getTempVal())) + "\n";
                    if(bloodOxWarning) msg += ("Blood Oxygen out of range - " +  String.valueOf(Globals.getBloodOxVal())) + "\n";

                    //Globals.addNotification("WARNING", msg, getBaseContext().getFilesDir().getPath() + "/AccountData");
                    Globals.addNotification("WARNING", msg, this);
                }
                //If this is still active during next tick, run notification
                else warning_hasBeenActive5s = true;
            }
            //Warning was disabled during that tick
            else if (warning_hasBeenActive5s) warning_hasBeenActive5s = false;
        }
        //If a caution is active
        else if(pulseCaution || tempCaution || bloodOxCaution)
        {
            //If the caution has already been notified, don't do it again
            if(!isCautionActive)
            {
                //Caution has been active for one full tick
                if(caution_hasBeenActive5s)
                {
                    //Notify
                    assert mNotificationManager != null;
                    mNotificationManager.notify(( int ) System. currentTimeMillis () , notif_babyCaution.build()) ;
                    Globals.setCautionActive(true);
                    Globals.setWarningActive(false);
                    caution_hasBeenActive5s = false;

                    //Handle notification logging
                    String msg = "";
                    if(pulseCaution) msg += ("Pulse out of range -  " +  String.valueOf(Globals.getPulseVal())) + "\n";
                    if(tempCaution) msg += ("Temperature out of range -  " +  String.valueOf(Globals.getTempVal())) + "\n";
                    if(bloodOxCaution) msg += ("Blood Oxygen out of range -  " +  String.valueOf(Globals.getBloodOxVal())) + "\n";

                    //Globals.addNotification("CAUTION", msg, getBaseContext().getFilesDir().getPath() + "/AccountData");
                    Globals.addNotification("CAUTION", msg, this);

                }
                //If this is still active during next tick, run notification
                else caution_hasBeenActive5s = true;
            }
            //Caution was disabled during that tick
            else if (caution_hasBeenActive5s) caution_hasBeenActive5s = false;
        }
        //If the warning or caution were active before
        //but aren't anymore (otherwise it wouldn't have made it here)
        //go ahead and disable them.
        else if(isCautionActive || isWarningActive)
        {
            Globals.setCautionActive(false);
            Globals.setWarningActive(false);
        }
    }


    void handleThresholds()
    {
        if(Globals.getBirthdate() == null) return;

        int year = Calendar.getInstance().get(Calendar.YEAR) -
                Globals.getBirthdate().getYear();
        int months = year * 12 + (Calendar.getInstance().get(Calendar.MONTH) -
                Globals.getBirthdate().getMonth());

        //Temperature thresholds don't change
        Globals.setTempLowCautionThreshold(98);
        Globals.setTempLowWarningThreshold(97);

        Globals.setTempHighCautionThreshold(99);
        Globals.setTempHighWarningThreshold(100);

        //Handle blood oxygen thresholds
        if(months <= 1)
        {
            //Bigger caution range here, but very young babies can have a lower blood ox (>90%)
            Globals.setBloodOxLowCautionThreshold(94);
            Globals.setBloodOxLowWarningThreshold(91);
        }
        else
        {
            //Blood ox should stabilize between 95-100%
            Globals.setBloodOxLowCautionThreshold(95);
            Globals.setBloodOxLowWarningThreshold(94);
        }


        //Handle pulse thresholds
        if(months <= 1)
        {
            Globals.setPulseLowCautionThreshold(70);
            Globals.setPulseLowWarningThreshold(65);

            Globals.setPulseHighCautionThreshold(190);
            Globals.setPulseHighWarningThreshold(195);
        }
        else if(months <= 11)
        {
            Globals.setPulseLowCautionThreshold(80);
            Globals.setPulseLowWarningThreshold(75);

            Globals.setPulseHighCautionThreshold(160);
            Globals.setPulseHighWarningThreshold(165);
        }
        if(months <= 24)
        {
            Globals.setPulseLowCautionThreshold(80);
            Globals.setPulseLowWarningThreshold(75);

            Globals.setPulseHighCautionThreshold(130);
            Globals.setPulseHighWarningThreshold(135);
        }
    }
}