package hr.math.android.alltasks.second;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import hr.math.android.alltasks.R;

public class SecondWindowLikeDialog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_window_like_dialog);
    }

    /**
     * Function that kills current activity and starts last one active.
     * @param view The View passed into the method is a reference to the widget that was clicked.
     */
    public void finishActivity(View view) {
        finish();
    }
}
