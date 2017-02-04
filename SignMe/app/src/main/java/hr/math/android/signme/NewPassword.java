package hr.math.android.signme;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


//TODO: double code here and in SettingsFragment (settings would have more options than this pop up). Think of something smart ;)
public class NewPassword extends AppCompatActivity {

    /*private EditText pass1;
    private EditText pass2;
    private Button saveButton;
    private TextView eye;

    private Preferences preferencesClass;
*/
    private String TAG = "NEW_PASSWORD";
    private String lectureName;
    private int lectureId;


    private void initializePassMail()
    {
        Log.d(TAG, "Starting fragment for initializing password and mail");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.your_placeholder, PasswordEmailFragment.newInstance(lectureId, lectureName));
        ft.commit();
    }

    private void startPasswordFragment()
    {
        Log.d(TAG, "Starting fragment for initializing password");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.your_placeholder, PasswordFragment.newInstance(lectureId, lectureName));
        ft.commit();
    }

    private void startEmailFragment()
    {
        Log.d(TAG, "Starting fragment for initializing email");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.your_placeholder, new PasswordFragment());
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        if (getIntent().hasExtra("LECTURE_ID")) {
            lectureName = getIntent().getStringExtra("LECTURE_NAME");
            lectureId = getIntent().getIntExtra("LECTURE_ID", -1);
            boolean isPassInitialized = getIntent().getBooleanExtra("PASSWORD", false);
            boolean isMailInitialized = getIntent().getBooleanExtra("MAIL", false);
            initializePassMail();
        }
    }
}
