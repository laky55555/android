package hr.math.android.alltasks.eight;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import hr.math.android.alltasks.R;

public class EighthClass extends AppCompatActivity {

    private int MY_PERM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eighth_class);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                                                            != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERM);

    }


    public void startSend(View view) {
        Intent intent = new Intent(this, EighthSendSmsMail.class);
        startActivity(intent);
    }

    public void startDownload(View view) {
        Intent intent = new Intent(this, EighthDownloadPicTxt.class);
        startActivity(intent);
    }

}
