package hr.math.android.signme;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsFragment extends Fragment {

    private EditText pass1;
    private EditText pass2;
    private Button saveButton;
    private TextView eye;

    private Password passwordClass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        initializeButtons(view);

        if(passwordClass.isPasswordInitialized())
            pass1.setText(passwordClass.getPassword());

        // Checking if text in first and second edit text are the same.
        pass1.addTextChangedListener(checkTexts());
        pass2.addTextChangedListener(checkTexts());

        saveButtonListener();
        togglePasswordVisibility();
    }

    private void initializeButtons(View view) {
        pass1 = (EditText) view.findViewById(R.id.password1);
        pass1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pass2 = (EditText) view.findViewById(R.id.password2);
        eye = (TextView) view.findViewById(R.id.eye);
        saveButton = (Button) view.findViewById(R.id.save_password);

        passwordClass = new Password(getActivity());

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
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                String pass1Text = pass1.getText().toString();
                if(pass1Text.equals(pass2.getText().toString()) && pass1Text.trim().length() > 0) {
                    passwordClass.saveNewPassword(pass1Text);
                    Snackbar.make(v, "New password saved", Snackbar.LENGTH_LONG).show();
                    hideKeyboard();
                } else if (pass1Text.trim().length() == 0)
                    Snackbar.make(v, "Password must contain at least one character", Snackbar.LENGTH_LONG).show();
                else
                    Snackbar.make(v, "First and second passwords are different", Snackbar.LENGTH_LONG).show();

                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            }
        });

    }

    private TextWatcher checkTexts() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password1 = pass1.getText().toString();
                if(password1.trim().length() != 0 && password1.equals(pass2.getText().toString()))
                    pass2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tick, 0);
                else
                    pass2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        };
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getActivity().getCurrentFocus();
        if (view == null)
            view = new View(getActivity());
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
