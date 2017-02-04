package hr.math.android.signme;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

/**
 * Dialog for initializing password and email.
 */
public class InitializePassEmailDialog extends AppCompatActivity {

    private String TAG = "Initialize Dialog";
    private String lectureName;
    private int lectureId;

    private void initializePassMail()
    {
        Log.d(TAG, "Starting fragment for initializing password and mail");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.your_placeholder, PasswordEmailFragment.newInstance(lectureId, lectureName));
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        if (getIntent().hasExtra("LECTURE_ID")) {
            lectureName = getIntent().getStringExtra("LECTURE_NAME");
            lectureId = getIntent().getIntExtra("LECTURE_ID", -1);
        }
        else {
            lectureId = -2;
            lectureName = "Vise srece drugi put.";
        }
        initializePassMail();
    }
}
