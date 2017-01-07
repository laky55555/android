package hr.math.android.alltasks.second;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class SecondReceiveText extends AppCompatActivity {

    private int lengthOfText;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_second_receive_text);

        // Getting message from intent
        Intent intent = getIntent();
        String message = intent.getStringExtra(SecondClass.TEXT_TO_SEND);
        lengthOfText = message.length();
        // Creating new textView
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);

        // Putting new textView as layout of this activity.
        setContentView(textView);

    }

    /**
     * After activity stops to be visible we want to show how many letters received string got.
     */
    @Override
    protected void onStop(){
        super.onStop();
        Toast.makeText(this, Integer.toString(lengthOfText), Toast.LENGTH_SHORT).show();
    }
}
