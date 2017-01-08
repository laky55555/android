package hr.math.android.alltasks.eight;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import hr.math.android.alltasks.R;

public class EighthSendSmsMail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eighth_send_sms_mail);
    }

    public void sendSmsSneaky(View v) {
        SmsManager sms = SmsManager.getDefault();

        String text = ((EditText) findViewById(R.id.input_txt)).getText().toString();
        String number = ((EditText) findViewById(R.id.input_number)).getText().toString();

        if (text.length() > 0 && number.length() > 0) {
            sms.sendTextMessage(number, null, text, null, null);
            Toast.makeText(this, "SMS sent, text: " + text + ", number: " + number, Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(this, "You need to enter number and text.", Toast.LENGTH_LONG).show();
    }

    public void sendSmsApp(View v) {

        String text = ((EditText)findViewById(R.id.input_txt)).getText().toString();
        String number = ((EditText)findViewById(R.id.input_number)).getText().toString();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + number));
            i.putExtra("sms_body", text);
            startActivity(i);
        }
        else {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setType("vnd.android-dir/mms-sms");
            i.putExtra("address", number);
            i.putExtra("sms_body", text);
            startActivity(i);
        }

    }

    public void sendEmail(View view) {

        String subject = "Subject: ";
        String text = ((EditText) findViewById(R.id.input_txt)).getText().toString();
        String email = ((EditText) findViewById(R.id.input_email)).getText().toString();

        String[] cc = {"ivan@mutiny.codes"};

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
        emailIntent.putExtra(Intent.EXTRA_CC, cc);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, text);
        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent, "Email"));

    }

}
