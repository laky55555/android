package hr.math.android.signme;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment for all settings.
 * Include fragment for setting password and email address.
 * Implements all other settings.
 */
public class SettingsFragment extends Fragment {

    private void initializePassMail() {
        Log.d("TAG", "Starting fragment for initializing password and mail");
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.pass_email_settings_fragment, new PasswordEmailFragment());
        ft.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initializePassMail();
    }

}