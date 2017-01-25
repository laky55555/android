package hr.math.android.kolokvij2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ilakovi on 1/25/17.
 */

public class MyFibonacciService extends Service {

    //za timer:
    int fib0 = 0;
    int fib1 = 1;

    static final int UPDATE_INTERVAL = 1500;
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

        Toast.makeText(this, "Fibonacci Service Started", Toast.LENGTH_LONG).show();

        //za timer:
        doSomethingRepeatedly();


        return START_STICKY;
    }

    //za timer
    private void doSomethingRepeatedly() {
        timer.scheduleAtFixedRate( new TimerTask() {
            public void run() {
                int temp = fib0 + fib1;
                fib0 = fib1;
                fib1 = temp;
                Log.d("MyFibonacciService", String.valueOf(fib1));
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

        Toast.makeText(this, "Fibonacci Service Destroyed", Toast.LENGTH_LONG).show();
    }
}



