package hr.math.android.alltasks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.TextView;

public class ChargingChanged extends BroadcastReceiver {
    public ChargingChanged() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        //String message = "Battery charging started/stopped";

        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com/#q=chargingBattery"));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
