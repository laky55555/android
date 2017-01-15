package hr.math.android.signme;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Signing extends AppCompatActivity {

    private String lecture_name;
    private int lecture_id;
    private int column_for_change;
    private DBAdapter db;
    private String TAG = "SIGNING";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signing);

        if(getIntent().hasExtra("LECTURE_ID")) {
            lecture_name = getIntent().getStringExtra("LECTURE_NAME");
            lecture_id = getIntent().getIntExtra("LECTURE_ID", -1);
            Log.d(TAG, "onCreate intent IMA extra lecture name = "
                    + lecture_name + " lecture id = " + lecture_id);
            Toast.makeText(this, "onCreate intent ima extra lecture name = " + lecture_name
                    + " lecture id = " + lecture_id, Toast.LENGTH_SHORT).show();

            db = new DBAdapter(this);
            //TODO: tu se sad pokrece fragment s potpisivanjem
            startSelectNameFragment();
            //startDrawingFragment();
        }
        else {
            Log.d(TAG, "onCreate intent NEMA extra");
            Toast.makeText(this, "onCreate intent NEMA extra.", Toast.LENGTH_SHORT).show();
            //TODO: tu ne ide u exit(main) nego u neki limbo activity koji ce pitati pass za izlaz!
            exit();
        }
    }

    private void startSelectNameFragment() {
        Toast.makeText(this,"Starting fragment for finding student", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Starting fragment for finding student");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.fragment, FindStudentFragment.newInstance(lecture_id));
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
    }

    private void startDrawingFragment() {
        Toast.makeText(this,"Starting fragment for drawing", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Starting fragment for drawing");
        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.fragment, new DrawingFragment());
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
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

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d(TAG, "onKeyDown Called");
            Toast.makeText(this, "Stistnut je back.", Toast.LENGTH_SHORT).show();
            return true;
        }
        Log.d(TAG, "Stistnut je: " + keyCode);
        Toast.makeText(this, "Stistnut je: " + keyCode, Toast.LENGTH_SHORT).show();
        return super.onKeyDown(keyCode, event);
    }*/



    public void checkPassword(View view) {
        popUpPassword();
    }

    private void popUpPassword() {
        //TODO: tu dolazi neki pop up fragment ili tako nesto za unijeti password
        exit();
    }

    private void exit() {
        Intent intent = new Intent(this, MainActivity.class);
        //TODO: to treba vratiti ili negdje drugdje stvaiti jer inace se nemoze izaci iz aplikacije
        //getPackageManager().clearPackagePreferredActivities(getPackageName());
        startActivity(intent);
        finish();
    }

    /*public void checkPassword(View view) {
        String pass = ((EditText) findViewById(R.id.password)).getText().toString();
        if(pass.equals(password)){
            Intent intent = new Intent(this, MainActivity.class);
            getPackageManager().clearPackagePreferredActivities(getPackageName());
            startActivity(intent);
        }
        else
            Toast.makeText(this, "Krivi pass", Toast.LENGTH_SHORT).show();
        finish();
    }*/


    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().hasExtra("LECTURE_ID"))
            Log.d(TAG, "onResume intent IMA extra");
        else {
            Log.d(TAG, "onResume intent NEMA extra");
            exit();
        }

        if(isMyAppLauncherDefault())
            Log.d(TAG, "onResume Provjera launchr, MOJ JE");
        else
            Log.d(TAG, "onResume Provjera launchr, Nije MOJ");
        if(!isMyAppLauncherDefault())
            resetPreferredLauncherAndOpenChooser(getApplicationContext());
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
        final IntentFilter filter = new IntentFilter(Intent.ACTION_MAIN);
        filter.addCategory(Intent.CATEGORY_HOME);

        List<IntentFilter> filters = new ArrayList<IntentFilter>();
        filters.add(filter);

        final String myPackageName = getPackageName();
        List<ComponentName> activities = new ArrayList<ComponentName>();
        final PackageManager packageManager = (PackageManager) getPackageManager();

        packageManager.getPreferredActivities(filters, activities, null);

        for (ComponentName activity : activities) {
            if (myPackageName.equals(activity.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.signing_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_new_student) {
//            addNewStudent();
//            return true;
//        } else
        if (id == R.id.action_exit) {
            Toast.makeText(this,"exit", Toast.LENGTH_LONG).show();
            popUpPassword();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    private void addNewStudent() {
//        //TODO: dolazi fragment neki pop up za upis novog studenta
//        Toast.makeText(this,"new_student", Toast.LENGTH_LONG).show();
//    }

}
