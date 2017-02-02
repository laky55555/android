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

public class MyService extends Service {

    //za timer:
    int counter = 0;

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

        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

        //za timer:
        doSomethingRepeatedly();


        return START_STICKY;
    }

    //za timer
    private void doSomethingRepeatedly() {
        timer.scheduleAtFixedRate( new TimerTask() {
            public void run() {
                Log.d("MyService", String.valueOf(++counter));
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

        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

}


