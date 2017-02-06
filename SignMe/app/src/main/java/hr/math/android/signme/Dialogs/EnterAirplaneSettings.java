package hr.math.android.signme.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import hr.math.android.signme.R;

/**
 * Created by ivan on 04.02.17..
 */

public class EnterAirplaneSettings {

    private static void startAirplaneSettings(Activity activity) {
        //TODO: choose between new task and without
        // (with new task user goes back with multiple back button press or long home and choosing signMe,
        // or without and users go back with one back pressed.
        Intent intentAirplaneMode = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
        //intentAirplaneMode.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intentAirplaneMode);

    }


    public static void enterAirplaneSettingsDialog(final Activity activity)
    {
        //Toast.makeText(getContext(), "AIRPLANE pop up add", Toast.LENGTH_SHORT);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.enter_airplane_mode));

        final TextView info = new TextView(activity);
        info.setText(activity.getString(R.string.security_airplane_info));
        info.setPadding(30,10,10,10);
        builder.setView(info);


        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAirplaneSettings(activity);
                //dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


}
