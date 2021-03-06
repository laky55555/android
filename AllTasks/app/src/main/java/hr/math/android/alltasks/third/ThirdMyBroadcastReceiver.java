package hr.math.android.alltasks.third;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class ThirdMyBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = ThirdMyBroadcastReceiver.class.getSimpleName();

    public ThirdMyBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String typeOfAlien = intent.getStringExtra("lifeformName");

        Log.d(TAG, typeOfAlien);

        //Toast.makeText(context, typeOfAlien, Toast.LENGTH_LONG);

        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com/#q=" + typeOfAlien));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
