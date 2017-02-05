package hr.math.android.signme.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import hr.math.android.signme.Limbo;
import hr.math.android.signme.Preferences;
import hr.math.android.signme.R;
import hr.math.android.signme.Signing;

/**
 * Created by ivan on 04.02.17..
 */

public class CheckPassword {

    public static void popUpPassword(final Context context, final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.password_for_exit);

        final LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText input_password = new EditText(activity);
        input_password.setHint(R.string.enter_password);
        input_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        layout.addView(input_password);

        builder.setView(layout);

        builder.setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkPassword(input_password.getText().toString(), context, activity);
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

    private static void checkPassword(String input_password, Context context, Activity activity) {
        String stored_pass = new Preferences(context).getPassword();
        if(stored_pass.equals(input_password)){
            String activityName = activity.getLocalClassName();
            if(activityName.equals("Signing"))
                ((Signing)activity).exit(true);
            else if(activityName.equals("Limbo"))
                ((Limbo)activity).exitLimbo();
        }
        else
            Toast.makeText(context, "Krivi pass, tocni pass = " + stored_pass, Toast.LENGTH_SHORT).show();
    }




}
