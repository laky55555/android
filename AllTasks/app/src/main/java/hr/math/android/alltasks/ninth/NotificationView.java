package hr.math.android.alltasks.ninth;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import hr.math.android.alltasks.MainActivity;
import hr.math.android.alltasks.R;
import hr.math.android.alltasks.tenth.TenthClass;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationView extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nineth_notification_view);

        //---look up the notification manager service---
        NotificationManager nm = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        //---cancel the notification that we started---
        nm.cancel(getIntent().getExtras().getInt("notificationID"));

        Button button = (Button) findViewById(R.id.button_restart_ninth);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NinthClass.class);
                startActivity(intent);
            }
        });
    }

    public void startMain(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
