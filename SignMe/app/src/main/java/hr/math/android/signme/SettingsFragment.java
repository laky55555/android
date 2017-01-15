package hr.math.android.signme;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends Fragment {

    public static final String MYPREFS = "pref";
    private int prefMode = MODE_PRIVATE;
    private String old_pass;

    private EditText pass1;
    private EditText pass2;
    private Button save_button;
    private TextView eye;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_settings, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        pass1 = (EditText) view.findViewById(R.id.password1);
        pass1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pass2 = (EditText) view.findViewById(R.id.password2);
        eye = (TextView) view.findViewById(R.id.eye);
        save_button = (Button) view.findViewById(R.id.save_password);

        if(loadPreferences())
            pass1.setText(old_pass);

        pass2.addTextChangedListener(new TextWatcher() {
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
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pass1.getText().toString().equals(pass2.getText().toString())) {
                    savePreferences(pass1.getText().toString());
                    Snackbar.make(v, "New password saved",
                            Snackbar.LENGTH_LONG).show();
                }
                else
                    Snackbar.make(v, "First and second passwords are different",
                            Snackbar.LENGTH_LONG).show();
            }
        });

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

    protected void savePreferences(String pass){
        SharedPreferences mySharedPreferences = getActivity().getSharedPreferences(MYPREFS, prefMode);
        SharedPreferences.Editor editor=mySharedPreferences.edit();
        //TODO: tu treba jos doci kriptiranje
        editor.putString("pass", pass);
        editor.commit();
    }

    public boolean loadPreferences()
    {
        SharedPreferences mySharedPreferences = getActivity().getSharedPreferences(MYPREFS, prefMode);
        String stringPreference = mySharedPreferences.getString("pass", "iz supljeg u prazno");

        if(stringPreference.equals("iz supljeg u prazno"))
            return false;

        old_pass = stringPreference;
        return true;
    }
}
