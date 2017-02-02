package hr.math.android.alltasks.tenth;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import hr.math.android.alltasks.R;

public class TenthClass extends AppCompatActivity {

    private int MY_PERM=1;
    private boolean start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenth_class);

        start = getIntent().hasExtra("START");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERM);
        else if(start) {
            start = false;
            startMaps();
        }
        else
            finish();

    }

    private void startMaps() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
