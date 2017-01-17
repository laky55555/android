package hr.math.android.signme;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


//TODO: double code here and in SettingsFragment (settings would have more options than this pop up). Think of something smart ;)
public class NewPassword extends AppCompatActivity {

    private EditText pass1;
    private EditText pass2;
    private Button save_button;
    private TextView eye;

    private Password passwordClass;

    private String lectureName;
    private int lectureId;

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        lectureId = -1;
        if(getIntent().hasExtra("LECTURE_ID")) {
            lectureName = getIntent().getStringExtra("LECTURE_NAME");
            lectureId = getIntent().getIntExtra("LECTURE_ID", -1);
            activity = this;
        }

        initializeButtons();

        if(passwordClass.isPasswordInitialized())
            pass1.setText(passwordClass.getPassword());

        // Checking if text in first and second edit text are the same.
        pass1.addTextChangedListener(checkTexts());
        pass2.addTextChangedListener(checkTexts());

        saveButtonListener();
        togglePasswordVisibility();
    }

    private void initializeButtons() {

        pass1 = (EditText) findViewById(R.id.password1);
        pass1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pass2 = (EditText) findViewById(R.id.password2);
        eye = (TextView) findViewById(R.id.eye);
        save_button = (Button) findViewById(R.id.save_password);
        passwordClass = new Password(this);

    }
    private void togglePasswordVisibility() {
        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pass1.getInputType() == InputType.TYPE_CLASS_TEXT)
                    pass1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                else
                    pass1.setInputType(InputType.TYPE_CLASS_TEXT);

            }
        });
    }

    private void saveButtonListener() {
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                String pass1Text = pass1.getText().toString();
                if(pass1Text.equals(pass2.getText().toString()) && pass1Text.trim().length() > 0) {
                    passwordClass.saveNewPassword(pass1Text);
                    Snackbar.make(v, "New password saved", Snackbar.LENGTH_LONG).show();
                    if(lectureId != -1) {
                        Intent intent = new Intent(activity, Signing.class);
                        intent.putExtra("LECTURE_NAME", lectureName);
                        intent.putExtra("LECTURE_ID", lectureId);
                        startActivity(intent);
                    }
                    finish();
                } else if (pass1Text.trim().length() == 0)
                    Snackbar.make(v, "Password must contain at least one character", Snackbar.LENGTH_LONG).show();
                else
                    Snackbar.make(v, "First and second passwords are different", Snackbar.LENGTH_LONG).show();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            }
        });

    }

    private TextWatcher checkTexts() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(pass1.getText().toString().equals(pass2.getText().toString()))
                    pass2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tick, 0);
                else
                    pass2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        };
    }

}
