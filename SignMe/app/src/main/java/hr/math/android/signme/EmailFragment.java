//package hr.math.android.signme;
//
//import android.app.Activity;
//import android.support.v4.app.Fragment;
//import android.os.Bundle;
//import android.support.design.widget.Snackbar;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.EditText;
//
///**
// * Created by ivan on 04.02.17..
// */
//
//public class EmailFragment extends Fragment {
//
//    private EditText email;
//    private Button saveButton;
//
//    private Preferences preferencesClass;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.email_input, parent, false);
//    }
//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//
//        initializeButtons(view);
//
//        if(preferencesClass.isMailInitialized())
//            email.setText(preferencesClass.getMail());
//
//        saveButtonListener();
//    }
//
//    private void initializeButtons(View view) {
//        email = (EditText) view.findViewById(R.id.email);
//        saveButton = (Button) view.findViewById(R.id.save_email);
//
//        preferencesClass = new Preferences(getActivity());
//
//    }
//
//
//    private void saveButtonListener() {
//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String emailText = email.getText().toString();
//                if(android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
//                    preferencesClass.saveNewMail(emailText);
//                    Snackbar.make(v, getContext().getString(R.string.new_email_saved), Snackbar.LENGTH_LONG).show();
//                    hideKeyboard();
//                }
//                else
//                    Snackbar.make(v, getContext().getString(R.string.email_not_valid), Snackbar.LENGTH_LONG).show();
//            }
//        });
//
//    }
//
//    private void hideKeyboard() {
//        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
//        View view = getActivity().getCurrentFocus();
//        if (view == null)
//            view = new View(getActivity());
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//    }
//
//}
