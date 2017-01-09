package hr.math.android.alltasks;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hr.math.android.alltasks.eight.EighthClass;
import hr.math.android.alltasks.fifth.FifthClass;
import hr.math.android.alltasks.fourth.FourthClass;
import hr.math.android.alltasks.second.SecondClass;
import hr.math.android.alltasks.seventh.SeventhClass;
import hr.math.android.alltasks.sixth.SixthClass;
import hr.math.android.alltasks.test.FakeLauncherActivity;
import hr.math.android.alltasks.test.NoBack;
import hr.math.android.alltasks.third.ThirdClass;

/**
 * Main activity that starts every time application starts.
 * Main activity represents menu with buttons that leads to
 * each class in lecture.
 */
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Function starts activity representing second class.
     *
     * @param view The View passed into the method is a reference to the widget that was clicked.
     */
    public void startSecondClass(View view) {
        Intent intent = new Intent(this, SecondClass.class);
        startActivity(intent);
    }

    /**
     * Function starts activity representing third class.
     *
     * @param view The View passed into the method is a reference to the widget that was clicked.
     */
    public void startThirdClass(View view) {
        Intent intent = new Intent(this, ThirdClass.class);
        startActivity(intent);
    }

    /**
     * Function starts activity representing fourth class.
     *
     * @param view The View passed into the method is a reference to the widget that was clicked.
     */
    public void startFourthClass(View view) {
        Intent intent = new Intent(this, FourthClass.class);
        startActivity(intent);
    }


    /**
     * Function starts activity representing fifth class.
     *
     * @param view The View passed into the method is a reference to the widget that was clicked.
     */
    public void startFifthClass(View view) {
        Intent intent = new Intent(this, FifthClass.class);
        startActivity(intent);
    }

    /**
     * Function starts activity representing sixth class.
     *
     * @param view The View passed into the method is a reference to the widget that was clicked.
     */
    public void startSixthClass(View view) {
        Intent intent = new Intent(this, SixthClass.class);
        startActivity(intent);
    }

    /**
     * Function starts activity representing seventh class.
     * @param view The View passed into the method is a reference to the widget that was clicked.
     */
    public void startSeventhClass(View view) {
        Intent intent = new Intent(this, SeventhClass.class);
        startActivity(intent);
    }

    /**
     * Function starts activity representing eighth class.
     * @param view The View passed into the method is a reference to the widget that was clicked.
     */
    public void startEighthClass(View view) {
        Intent intent = new Intent(this, EighthClass.class);
        startActivity(intent);
    }

    public void startTestNoBack(View view) {
        Intent intent = new Intent(this, NoBack.class);
        //intent.addCategory(Intent.CATEGORY_HOME);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void exit(View view) {
        Toast.makeText(this, "Kliknut Exit", Toast.LENGTH_SHORT).show();
    }

}
