package hr.math.android.signme;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


public class Signing extends AppCompatActivity {

    private String lectureName;
    private int lectureId;
    private String TAG = "SIGNING";

    private WindowManager manager;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signing);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if(getIntent().hasExtra("LECTURE_ID")) {
            lectureName = getIntent().getStringExtra("LECTURE_NAME");
            lectureId = getIntent().getIntExtra("LECTURE_ID", -1);

            Log.d(TAG, "onCreate intent IMA extra lecture name = "
                    + lectureName + " lecture id = " + lectureId);
            Toast.makeText(this, "onCreate intent ima extra lecture name = " + lectureName
                    + " lecture id = " + lectureId, Toast.LENGTH_SHORT).show();

            setTitle(lectureName);

            disableStatusBar();
            startSelectNameFragment();
        }
        else {
            Log.d(TAG, "onCreate intent NEMA extra");
            Toast.makeText(this, "onCreate intent NEMA extra.", Toast.LENGTH_SHORT).show();
            exit(false);
        }

    }

    private void startSelectNameFragment() {
        Toast.makeText(this,"Starting fragment for finding student", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Starting fragment for finding student");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, FindStudentFragment.newInstance(lectureId));
        ft.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onKeyDown Called");
        Toast.makeText(this, "Stistnut je back.", Toast.LENGTH_SHORT).show();
    }


    private void popUpPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.password_for_exit);

        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText input_password = new EditText(this);
        input_password.setHint(R.string.enter_password);
        input_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        layout.addView(input_password);

        builder.setView(layout);

        builder.setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkPassword(input_password.getText().toString());
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

    private void checkPassword(String input_password) {
        String stored_pass = new Preferences(this).getPassword();
        if(stored_pass.equals(input_password)){
            exit(true);
        }
        else
            Toast.makeText(this, "Krivi pass, tocni pass = " + stored_pass, Toast.LENGTH_SHORT).show();
    }

    //TODO: tu mozda bolje ne napraviti removeView nego u exitu samo za treu i poslati intentu u limb
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("STOP", "POZVAN ON STOP");
        if(view.getWindowToken() != null)
            manager.removeView(view);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("STOP", "POZVAN ON RESTART");
        disableStatusBar();
    }

    //TODO: ako se nemoze u limbo doci iz potpisivanja inace druga
    private void exit(boolean userExit) {
        Log.d("STOP", "POZVAN exit");
        Intent intent;
        intent = new Intent(this, MainActivity.class);
        getPackageManager().clearPackagePreferredActivities(getPackageName());
        Log.d("STOP", "pokrenut novi activity");
        startActivity(intent);
        Log.d("STOP", "pozvan finish()");
        finish();
    }

//    private void exit(boolean userExit) {
//        Log.d("STOP", "POZVAN exit");
//        Intent intent;
//        if(userExit) {
//            intent = new Intent(this, MainActivity.class);
//            getPackageManager().clearPackagePreferredActivities(getPackageName());
//        }
//        else
//            intent = new Intent(this, Limbo.class);
//
//        Log.d("STOP", "pokrenut novi activity");
//        startActivity(intent);
//        Log.d("STOP", "pozvan finish()");
//        finish();
//    }


    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().hasExtra("LECTURE_ID"))
            Log.d(TAG, "onResume intent IMA extra");
        else {
            Log.d(TAG, "onResume intent NEMA extra");
            exit(false);
        }

        if(isMyAppLauncherDefault())
            Log.d(TAG, "onResume Provjera launchr, MOJ JE");
        else
            Log.d(TAG, "onResume Provjera launchr, Nije MOJ");
        if(!isMyAppLauncherDefault()) {
            resetPreferredLauncherAndOpenChooser(getApplicationContext());
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            i.putExtra("LECTURE_NAME", lectureName);
            i.putExtra("LECTURE_ID", lectureId);
            //i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
        }
    }


    public static void resetPreferredLauncherAndOpenChooser(Context context) {
        PackageManager packageManager = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, FakeLauncherActivity.class);
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        Intent selector = new Intent(Intent.ACTION_MAIN);
        selector.addCategory(Intent.CATEGORY_HOME);
        selector.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(selector);

        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);
    }

    private boolean isMyAppLauncherDefault() {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = getPackageManager().resolveActivity(intent, 0);
        if (res.activityInfo == null) {
            // should not happen. A home is always installed, isn't it?
            Log.d("TEST", "nema launchera");
        } else if ("android".equals(res.activityInfo.packageName)) {
            // No default selected
            Log.d("TEST", "nema defult launchera");
        } else {
            Log.d("TEST", "ima odabranog: " + res.activityInfo.packageName);
            Log.d("TEST", "ima odabranog: " + res.activityInfo.name);
            Log.d("TEST", "packageName = " + getPackageName());
            if(res.activityInfo.packageName.equals(getPackageName()))
                return true;
            // res.activityInfo.packageName and res.activityInfo.name gives you the default app
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.signing_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_exit) {
            Toast.makeText(this, "exit", Toast.LENGTH_LONG).show();
            popUpPassword();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void disableStatusBar() {

        manager = ((WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE));

        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|

        // this is to enable the notification to recieve touch events
        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |

        // Draws over status bar
        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        localLayoutParams.height = (int) (50 * getResources()
                .getDisplayMetrics().scaledDensity);
        localLayoutParams.format = PixelFormat.TRANSPARENT;

        view = new customViewGroup(this);

        manager.addView(view, localLayoutParams);
        //manager.removeView(view);
    }
}
