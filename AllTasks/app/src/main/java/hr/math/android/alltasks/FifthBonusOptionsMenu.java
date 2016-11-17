package hr.math.android.alltasks;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class FifthBonusOptionsMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth_bonus_options_menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fifth_bonus_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.black:
                changeBackground(Color.BLACK);
                return true;
            case R.id.blue:
                changeBackground(Color.BLUE);
                return true;
            case R.id.white:
                changeBackground(Color.WHITE);
                return true;
            case R.id.red:
                changeBackground(Color.RED);
                return true;
            case R.id.start_google:
                startWebBrowser(this.getWindow().getDecorView());
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startWebBrowser(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
        startActivity(i);
    }


    private void changeBackground(int color) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(color);
    }


}
