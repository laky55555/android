package hr.math.android.alltasks.second;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import hr.math.android.alltasks.R;

public class SecondReceiveTexts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_receive_texts);

        // Get data from first way of sending data (appended to intent).
        Toast.makeText(this,getIntent().getStringExtra("string1"),Toast.LENGTH_SHORT).show();
        Toast.makeText(this,Integer.toString(getIntent().getIntExtra("number1",0)),Toast.LENGTH_SHORT).show();

        // Get data from second way of sending data (added to bundle and appended to intent).
        Bundle bundle=getIntent().getExtras();
        Toast.makeText(this,bundle.getString("string2"),Toast.LENGTH_SHORT).show();
        Toast.makeText(this,Integer.toString(bundle.getInt("number2")),Toast.LENGTH_SHORT).show();


        // Get data from third way of sending data (added to intent over setData).
        Toast.makeText(this,getIntent().getData().toString(), Toast.LENGTH_SHORT).show();

        // Modifying current layout. We are adding received string to current layout.
        View layout = findViewById(R.id.activity_second_receive_texts);
        TextView valueTV = new TextView(this);
        valueTV.setText(bundle.getString("string2"));
        ((RelativeLayout) layout).addView(valueTV);


    }
}
