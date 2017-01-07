package hr.math.android.alltasks.third;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import hr.math.android.alltasks.R;


/**
 * Playing with intents and fragments.
 * In this class we made 3 buttons that do some Android native actions,
 * and we created some static and dynamic fragments.
 */
public class ThirdClass extends AppCompatActivity {

    public static final String NEW_LIFEFORM_DETECTED = "hr.math.android.alltasks.NEW_LIFEFORM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_class);
    }

    public void onClickWebBrowser(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
        startActivity(i);
    }

    public void onClickMakeCall(View view) {

        EditText editText = (EditText) findViewById(R.id.third_edit_text1);
        String tel_number = "tel:" + editText.getText().toString();

        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse(tel_number));
        startActivity(i);
    }

    public void onClickShowMap(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:37.827500,-122.481670"));
        startActivity(i);
    }

    public void sendBroadcast(View view) {

        Intent intent = new Intent(NEW_LIFEFORM_DETECTED);
        intent.putExtra("lifeformName", "Predator");

        sendBroadcast(intent);
    }

    public void startBothFragments(View view) {
        Intent intent = new Intent(this, ThirdBothFragments.class);
        startActivity(intent);
    }

    public void startAlternatingFragments(View view) {
        Intent intent = new Intent(this, ThirdAlternatingFragments.class);
        startActivity(intent);
    }

    public void startMixFragments(View view) {
        Intent intent = new Intent(this, ThirdMixFragments.class);
        startActivity(intent);
    }


}
