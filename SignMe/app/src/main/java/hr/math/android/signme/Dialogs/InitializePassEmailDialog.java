package hr.math.android.signme.Dialogs;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import hr.math.android.signme.PasswordEmailFragment;
import hr.math.android.signme.R;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        // If has intent dialog is called when user want to start signing,
        // else dialog is called when user want to send statistic over email.
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
