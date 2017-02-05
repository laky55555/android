package hr.math.android.signme;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import hr.math.android.signme.Dialogs.InitializePassEmailDialog;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v("MAIN", "onCreate; Starting main activity.");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        startSettingsFragment();
//        checkPermission();
        if(!new Preferences(this).sawIntro()) {
            startIntro();
            Log.v("MAIN", "nije vidio intro");
            //startSettingsFragment();
            //checkPermission();
        }
        else {
            startSettingsFragment();
            checkPermission();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("MAIN", "onResume; Starting main activity.");
    }

    public static int DRAW_OVERLAY = 66;
    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                //startActivity(intent);
                startActivityForResult(intent, DRAW_OVERLAY);
                Toast.makeText(this, "NEKI GLUPI TEK", Toast.LENGTH_LONG).show();
                //Snackbar.make(findViewById(R.id.email), getString(R.string.mail_text), Snackbar.LENGTH_INDEFINITE).show();
                //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW},
                  //      ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            }
        }

//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                            Manifest.permission.READ_EXTERNAL_STORAGE}, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
//        }

        /*if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SYSTEM_ALERT_WINDOW)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW},ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
        }*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startSettingsFragment();
            return true;
        } else if (id == R.id.action_change_password) {
            Intent intent = new Intent(this, InitializePassEmailDialog.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_lectures_list) {
            startLectures(true, false);
        } else if (id == R.id.nav_add_lecture) {
            startLectures(true, true);
        } else if (id == R.id.nav_settings) {
            startSettingsFragment();
        } else if (id == R.id.nav_check_attendance) {
            startLectures(false, false);
        } else if (id == R.id.nav_share_one) {
            //TODO: see what to do with share buttons, maybe delete them??
        } else if (id == R.id.nav_send_all) {

        } else if (id == R.id.nav_send_one) {

        } else if (id == R.id.nav_intro) {
            startIntro();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void startSettingsFragment()
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.your_placeholder, new SettingsFragment());
        ft.commit();
    }

    private void startLectures(boolean editLectures, boolean addNewLecture)
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        EditLecturesFragment fragmentDemo = EditLecturesFragment.newInstance(editLectures, addNewLecture);
        ft.replace(R.id.your_placeholder, fragmentDemo);
        ft.commit();
    }

    //TODO: make listener so if user open navigation bar hideKeyboard function is called
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null)
            view = new View(this);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    static final int INTRO_RESULT = 68;  // The request code
    private void startIntro() {
        Intent intent = new Intent(this, MainIntroActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(intent, INTRO_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == INTRO_RESULT) {
            startSettingsFragment();
            checkPermission();
        }
        else if (requestCode == DRAW_OVERLAY) {
            checkPermission();
        }
    }

}
