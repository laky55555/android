package hr.pmf.math.prva.prva;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "hr.pmf.math.Prva.MESSAGE";
    //public static final String NEW_LIFEFORM_DETECTED = "hr.pmf.math.prva.NEW_LIFEFORM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }


    public void sendData(View view) {
        Intent i = new Intent(this, zad7.class);

        i.putExtra("str1","Ovo je prvi string");
        i.putExtra("br1",25);

        Bundle extras=new Bundle();
        extras.putString("str2","Ovo je drugi string");
        extras.putInt("br2",27);
        i.putExtras(extras);

        i.setData(Uri.parse("neki tekst treci"));

        startActivity(i);
    }


    public void onClickWebBrowser(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
        startActivity(i);
    }

    public void onClickMakeCalls(View view){
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+385922472406"));
        startActivity(i);
    }

    public void onClickShowMap(View view){
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:37.827500,-122.481670"));
        startActivity(i);
    }

    public  void startFragmentActivityStatic(View view){
        Intent intent = new Intent(this, ActivityForFragmentStatic.class);
        startActivity(intent);
    }

    public  void startFragmentActivityDynamic(View view){
        Intent intent = new Intent(this, ActivityForFragmentDynamic.class);
        startActivity(intent);
    }


}
