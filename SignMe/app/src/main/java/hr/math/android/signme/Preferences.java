package hr.math.android.signme;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;


public class Preferences {

    private static final String TAG = "Preferences";
    private static final String EMAIL_KEYWORD = "mailic";
    private static final String PASS_KEYWORD = "pass";
    private static final String INTRO_KEYWORD = "intro";
    private static final String PREFERENCES = "pref";
    private static final int prefMode = MODE_PRIVATE;

    private static final String defaultMessage = "iz supljeg u prazno";

    private SharedPreferences mySharedPreferences;

    Preferences(Activity activity)
    {
        mySharedPreferences = activity.getSharedPreferences(PREFERENCES, prefMode);
    }

    public Preferences(Context context)
    {
        mySharedPreferences = context.getSharedPreferences(PREFERENCES, prefMode);
    }

    public String getPassword() {
        //TODO: tu dolazi dekriptiranje
        return mySharedPreferences.getString(PASS_KEYWORD, defaultMessage);
    }

    boolean saveNewPassword(String newPassword){
        SharedPreferences.Editor editor=mySharedPreferences.edit();
        //TODO: tu treba jos doci kriptiranje
        editor.putString(PASS_KEYWORD, newPassword);
        editor.apply();
        return true;
    }

    boolean isPasswordInitialized() {
        return mySharedPreferences.contains(PASS_KEYWORD) && (getPassword().trim().length() > 0);
    }

    String getEmail() {
        //TODO: tu dolazi dekriptiranje
        return mySharedPreferences.getString(EMAIL_KEYWORD, defaultMessage);
    }

    boolean saveNewEmail(String newEmail){
        SharedPreferences.Editor editor=mySharedPreferences.edit();
        //TODO: tu treba jos doci kriptiranje
        editor.putString(EMAIL_KEYWORD, newEmail);
        editor.apply();
        return true;
    }

    boolean isEmailInitialized() {
        return mySharedPreferences.contains(EMAIL_KEYWORD) && (getEmail().trim().length() > 0);
    }

    boolean sawIntro() {
        if(mySharedPreferences.getBoolean(INTRO_KEYWORD, false)) {
            return true;
        }
        mySharedPreferences.edit().putBoolean(INTRO_KEYWORD, true).apply();
        return false;
    }
}
