package hr.math.android.alltasks.test;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import hr.math.android.alltasks.R;

public class NoBack extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_back);
    }

    @Override
    protected void onPause() {
        super.onPause();

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            Toast.makeText(this, "Stistnut je back.", Toast.LENGTH_SHORT).show();
            return true;
        }
        Log.d("NESTO", "Stistnut je: " + keyCode);
        Toast.makeText(this, "Stistnut je: " + keyCode, Toast.LENGTH_SHORT).show();
        return super.onKeyDown(keyCode, event);
    }

    /*@Override
    public void onBackPressed() {
        Log.d("CDA", "onKeyDown Called");
        Toast.makeText(this, "Stistnut je back.", Toast.LENGTH_SHORT).show();
    }*/



    public void enterPassword(View view) {
        Intent intent = new Intent(this, EnterPassword.class);
        startActivity(intent);
    }

}
