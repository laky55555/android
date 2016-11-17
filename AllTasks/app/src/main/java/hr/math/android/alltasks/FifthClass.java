package hr.math.android.alltasks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FifthClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth_class);
    }

    public void startLogDebug(View view) {
        Intent intent = new Intent(this, FifthLogDebug.class);
        startActivity(intent);
    }

    public void startOptionsContextMenu(View view) {
        Intent intent = new Intent(this, FifthOptionsContextMenu.class);
        startActivity(intent);
    }

    public void startBonusOptionsMenu(View view) {
        Intent intent = new Intent(this, FifthBonusOptionsMenu.class);
        startActivity(intent);
    }

    public void startSharedPreferences(View view) {
        Intent intent = new Intent(this, FifthSharedPreferences.class);
        startActivity(intent);
    }

    public void startFileSaving(View view) {
        Intent intent = new Intent(this, FifthFileSaving.class);
        startActivity(intent);
    }

    public void startReadFile(View view) {
        Intent intent = new Intent(this, FifthReadFile.class);
        startActivity(intent);
    }




}
