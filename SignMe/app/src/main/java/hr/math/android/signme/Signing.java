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
import android.provider.Settings;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import static hr.math.android.signme.Dialogs.CheckPassword.popUpPassword;


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
    public void exit(boolean userExit) {
        Log.d("STOP", "POZVAN exit");
        Intent intent;
        intent = new Intent(this, MainActivity.class);
        getPackageManager().clearPackagePreferredActivities(getPackageName());
        Log.d("STOP", "pokrenut novi activity");
        startActivity(intent);
        Log.d("STOP", "pozvan finish()");

        startAirplaneSettings();
        finish();

    }

    private void startAirplaneSettings() {
        //TODO: Here is if because maybe we will allow users to enter signing without airplane mode.
        if(Settings.System.getInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0) {
            Intent intentAirplaneMode = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
            //intentAirplaneMode.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentAirplaneMode);
        }
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
//        startAirplaneSettings();
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
            popUpPassword(getBaseContext(), this);
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
