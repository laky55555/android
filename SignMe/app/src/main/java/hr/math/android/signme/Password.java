package hr.math.android.signme;

import android.app.Activity;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;


class Password {

    private static final String KEYWORD = "pass";
    private static final String PREFERENCES = "pref";
    private static final int prefMode = MODE_PRIVATE;

    private SharedPreferences mySharedPreferences;

    Password(Activity activity)
    {
        mySharedPreferences = activity.getSharedPreferences(PREFERENCES, prefMode);
    }

    String getPassword() {
        //TODO: tu dolazi dekriptiranje
        return mySharedPreferences.getString(KEYWORD, "iz supljeg u prazno");
    }

    boolean saveNewPassword(String newPassword){
        SharedPreferences.Editor editor=mySharedPreferences.edit();
        //TODO: tu treba jos doci kriptiranje
        editor.putString(KEYWORD, newPassword);
        editor.apply();
        return true;
    }

    boolean isPasswordInitialized() {
        return mySharedPreferences.contains(KEYWORD);
    }

}
