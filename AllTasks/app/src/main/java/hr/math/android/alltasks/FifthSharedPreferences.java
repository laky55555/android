package hr.math.android.alltasks;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class FifthSharedPreferences extends AppCompatActivity {

    private final String MYPREFS = "MyPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth_shared_preferences);

        savePreferences();
        loadPreferences();
    }

    protected void savePreferences(){
        //stvorimo shared preference
        int mode=MODE_PRIVATE;
        SharedPreferences mySharedPreferences=getSharedPreferences(MYPREFS,mode);

        //editor za modificiranje shared preference
        SharedPreferences.Editor editor=mySharedPreferences.edit();

        //spremamo vrijednosti u shared preference
        editor.putBoolean("isTrue", true);
        editor.putFloat("lastFloat",1f);
        editor.putInt("wholeNumber", 5);
        editor.putLong("aNumber", 35);
        editor.putString("textEntryValue", "Neki tekst");

        //commit promjene
        editor.commit();
    }

    public void loadPreferences(){
        // dohvatimo preference
        int mode=MODE_PRIVATE;
        SharedPreferences mySharedPreferences=getSharedPreferences(MYPREFS,mode);

        //dohvatimo vrijednosti
        boolean isTrue=mySharedPreferences.getBoolean("isTrue", false);
        float lastFloat=mySharedPreferences.getFloat("lastFloat", 0f);
        int wholeNumber=mySharedPreferences.getInt("WholeNumber", 7);
        long aNumber=mySharedPreferences.getLong("aNumber", 0);
        String stringPreference=mySharedPreferences.getString("textEntryValue", "nista");

        //ispisemo string da se nesto ispise...
        Toast.makeText(getBaseContext(),
                stringPreference,
                Toast.LENGTH_SHORT).show();

    }
}
