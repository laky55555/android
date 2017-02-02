package hr.math.android.alltasks.ninth;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import hr.math.android.alltasks.R;

/**
 * Created by ivan on 02.02.17..
 */

public class ServiceCountdown extends Service {

    //za timer:
    int counter = 5;
    int notificationID = 1;

    static final int UPDATE_INTERVAL = 1000;
    private Timer timer = new Timer();
    // do tuda za timer

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // ovaj servis radi dok ga se ne zaustavi eksplicitno
        // dakle vraca sticky

        Toast.makeText(this, "Service Countdown Started", Toast.LENGTH_LONG).show();

        //za timer:
        doSomethingRepeatedly();


        return START_STICKY;
    }

    //za timer
    private void doSomethingRepeatedly() {
        timer.scheduleAtFixedRate( new TimerTask() {
            public void run() {
                Log.d("ServiceCountdown", String.valueOf(--counter));
                if(counter == 0)
                    onDestroy();
            }
        }, 0, UPDATE_INTERVAL);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //za timer
        if (timer != null){
            timer.cancel();

        }

        //Toast.makeText(getBaseContext(), "ServiceCountdown Destroyed", Toast.LENGTH_LONG).show();
        Log.d("ServiceCountdown", "ServiceCountdown Destroyed");
        sendNotification();

    }

    private void sendNotification() {
        Intent i = new Intent(this, NotificationView.class);

        i.putExtra("notificationID", notificationID);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, i, 0);

        long[] vibrate = new long[] { 100, 250, 100, 500};

        NotificationManager nm = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);


        Notification notif = new Notification.Builder(this)
                .setTicker("Reminder: meeting starts in 5 minutes")
                .setContentTitle("Countdown counted 5 seconds")
                .setContentText("Here comes the boooom")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setShowWhen(true)
                .setContentIntent(pendingIntent)
                .setVibrate(vibrate)
                .build();


        nm.notify(notificationID, notif);
    }

}


