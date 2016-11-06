package hr.math.android.alltasks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
     * @param view The View passed into the method is a reference to the widget that was clicked.
     */
    public void startSecondClass(View view) {
        Intent intent = new Intent(this, SecondClass.class);
        startActivity(intent);
    }

    /**
     * Function starts activity representing third class.
     * @param view The View passed into the method is a reference to the widget that was clicked.
     */
    public void startThirdClass(View view) {
        Intent intent = new Intent(this, ThirdClass.class);
        startActivity(intent);
    }

    /**
     * Function starts activity representing fourth class.
     * @param view The View passed into the method is a reference to the widget that was clicked.
     */
    public void startFourthClass(View view) {
        Intent intent = new Intent(this, FourthClass.class);
        startActivity(intent);
    }

}
