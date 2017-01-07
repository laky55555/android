package hr.math.android.alltasks.second;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import hr.math.android.alltasks.R;


/**
 * In second class we are playing with sending and receiving information from
 * one activity to another. This class have solved problems 4 trough 7, together
 * with bonus problem (https://web.math.pmf.unizg.hr/~karaga/android/materijali01.html).
 */
public class SecondClass extends AppCompatActivity {

    public static final String TEXT_TO_SEND = "EXTRA_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_class);
    }

    /**
     * Function starts activity SecondWindowLikeDialog that looks like dialog window.
     * @param view The View passed into the method is a reference to the widget that was clicked.
     */
    public void startWindowLikeDialog(View view) {
        Intent intent = new Intent(this, SecondWindowLikeDialog.class);
        startActivity(intent);
    }

    /**
     * Function starts new activity and sends text give in edit text window.
     * @param view The View passed into the method is a reference to the widget that was clicked.
     */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, SecondReceiveText.class);
        EditText editText = (EditText) findViewById(R.id.second_edit_text1);
        String message = editText.getText().toString();
        intent.putExtra(TEXT_TO_SEND, message);
        startActivity(intent);
    }

    /**
     * Function starts activity SecondReceiveTexts and sends hardcoded text on 3 different ways.
     * @param view The View passed into the method is a reference to the widget that was clicked.
     */
    public void sendMessageOnThreeWays(View view) {
        Intent intent = new Intent(this, SecondReceiveTexts.class);

        // First way, appending string/int directly to intent.
        intent.putExtra("string1", "This is first string.");
        intent.putExtra("number1", 25);

        // Second way, putting string/int into bundle and then appending bundle to intent.
        Bundle extras=new Bundle();
        extras.putString("string2","This is second string.");
        extras.putInt("number2",27);
        intent.putExtras(extras);

        // Third way, setting data given intent is operating on on given string.
        intent.setData(Uri.parse("This is third string."));


        startActivity(intent);
    }


}
