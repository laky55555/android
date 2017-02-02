package hr.math.android.alltasks.ninth;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import hr.math.android.alltasks.R;

public class NinthClass extends AppCompatActivity {

    int notificationID= 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ninth_class);
    }

    public void startService(View view) {
        startService(new Intent(getBaseContext(), MyService.class));
    }

    public void startCountdownService(View view) {
        startService(new Intent(getBaseContext(), ServiceCountdown.class));
    }

    public void stopService(View view) {
        stopService(new Intent(getBaseContext(), MyService.class));
    }

    public void sendNotification(View view) {
        displayNotification();
    }

    protected void displayNotification()
    {
        //---PendingIntent to launch activity if the user selects
        // this notification---
        Intent i = new Intent(this, NotificationView.class);

        i.putExtra("notificationID", notificationID);


        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, i, 0);

        long[] vibrate = new long[] { 100, 250, 2000, 500};

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager nm = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);


        Notification notif = new Notification.Builder(this)
                .setTicker("Reminder: meeting starts in 5 minutes")
                .setContentTitle("Meeting with customer at 3pm...")
                .setContentText("this is the second row")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setShowWhen(true)
                .setContentIntent(pendingIntent)
                .setVibrate(vibrate)
                .setSound(sound)
                .build();


        nm.notify(notificationID, notif);
    }

}
