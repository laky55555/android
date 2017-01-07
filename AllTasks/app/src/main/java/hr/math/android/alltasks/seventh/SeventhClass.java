package hr.math.android.alltasks.seventh;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import hr.math.android.alltasks.R;

public class SeventhClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seventh_class);
    }

    public void startEmbeddedContentProvider(View view) {
        Intent i = new Intent(this, SeventhEmbeddedCP.class);
        startActivity(i);
    }

    public void startCustomContentProvider(View view) {
        Intent i = new Intent(this, SeventhTestingBookProvider.class);
        startActivity(i);
    }


}
