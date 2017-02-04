package hr.math.android.signme;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by ivan on 04.02.17..
 */

/**
 * Fragment for setting up password and email.
 * Fragment validate input and behave accordingly of
 * who called it (popup before signing, popup before sending mail,
 * plain text in settings).
 */
public class PasswordEmailFragment extends Fragment {

    private boolean isPopUp;
    private boolean signing;

    private EditText pass1;
    private EditText pass2;
    private TextView eye;

    private EditText email;
    private Button saveButton;

    private Preferences preferencesClass;

    private int lectureId;
    private String lectureName;
    private final String TAG = "PasswordEmailFragment";

    public static PasswordEmailFragment newInstance(int lectureId, String lectureName) {
        PasswordEmailFragment fragmentDemo = new PasswordEmailFragment();
        Bundle args = new Bundle();
        args.putInt("LECTURE_ID", lectureId);
        args.putString("LECTURE_NAME", lectureName);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isPopUp = false;
        if(getArguments() != null) {
            isPopUp = true;
            lectureId = getArguments().getInt("LECTURE_ID", -1);
            signing = lectureId > 0;
            lectureName = getArguments().getString("LECTURE_NAME");
            Log.d(TAG, "Password and Email fragment is in popup" + lectureId + " " + lectureName);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_password_email, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        initializeButtons(view);

        preferencesClass = new Preferences(getActivity());
        if(preferencesClass.isPasswordInitialized()) {
            pass1.setText(preferencesClass.getPassword());
            if(isPopUp)
                pass2.setText(preferencesClass.getPassword());
        }
        if(preferencesClass.isEmailInitialized())
            email.setText(preferencesClass.getEmail());

        // Checking if text in first and second edit text are the same.
        pass1.addTextChangedListener(checkText("PASS"));
        pass2.addTextChangedListener(checkText("PASS"));
        email.addTextChangedListener(checkText("EMAIL"));

        saveButtonListener();
        togglePasswordVisibility();
    }

    private void initializeButtons(View view) {
        pass1 = (EditText) view.findViewById(R.id.password1);
        pass1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pass2 = (EditText) view.findViewById(R.id.password2);
        eye = (TextView) view.findViewById(R.id.eye);
        email = (EditText) view.findViewById(R.id.email);
        saveButton = (Button) view.findViewById(R.id.save_pass_email);
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

    private boolean arePasswordsValid() {
        String pass1Text = pass1.getText().toString();
        return (pass1Text.equals(pass2.getText().toString()) && pass1Text.trim().length() > 0);
    }

    private boolean isEmailValid() {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches();
    }

    private void saveButtonListener() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                if(arePasswordsValid() && isEmailValid()) {
                    preferencesClass.saveNewPassword(pass1.getText().toString());
                    preferencesClass.saveNewEmail(email.getText().toString());
                    Snackbar.make(v, getContext().getString(R.string.saved_pass_email), Snackbar.LENGTH_LONG).show();
                    hideKeyboard();
                    if(signing) {
                        Intent intent = new Intent(getActivity(), Signing.class);
                        intent.putExtra("LECTURE_NAME", lectureName);
                        intent.putExtra("LECTURE_ID", lectureId);
                        startActivity(intent);
                        //getActivity().finish();
                    }
                    if(isPopUp) {
                        getActivity().finish();
                    }
                }
                else if(!arePasswordsValid())
                    Snackbar.make(v, getContext().getString(R.string.password_not_valid), Snackbar.LENGTH_LONG).show();
                else if (!isEmailValid())
                    Snackbar.make(v, getContext().getString(R.string.email_not_valid), Snackbar.LENGTH_LONG).show();
            }
        });

    }

    private TextWatcher checkText(final String text) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (text.equals("PASS")) {
                    if (arePasswordsValid())
                        pass2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tick, 0);
                    else
                        pass2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
                else if(text.equals("EMAIL")) {
                    if(isEmailValid())
                        email.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tick, 0);
                    else
                        email.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
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
