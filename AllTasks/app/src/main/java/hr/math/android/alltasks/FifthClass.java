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
