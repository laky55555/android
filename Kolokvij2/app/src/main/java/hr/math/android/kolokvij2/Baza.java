package hr.math.android.kolokvij2;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class Baza extends AppCompatActivity {


    private DBAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baza);

        db = new DBAdapter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_baza, menu);
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

    public void fillTables(View view) {
        db.open();

        db.insertEmployee("Fran", "fran@mail.com", 12345);
        db.insertEmployee("Bran", "bran@mail.com", 2345);
        db.insertEmployee("Franina", "franina@mail.com", 3345);
        db.insertEmployee("Jurina", "jurina@mail.com", 4345);
        db.insertEmployee("Đurđa", "durda@mail.com", 5345);
        db.insertEmployee("Vanja", "vanja@mail.com", 12345);

        db.insertWorkspace("Gazda", 5, 12345);
        db.insertWorkspace("Minesweeper zaposlenik", 4, 11111);
        db.insertWorkspace("Pasijans zaposlenik", 4, 11110);
        db.insertWorkspace("Mail Gazda", 3, 9999);
        db.insertWorkspace("Zamjenik malog Gazde", 3, 9998);
        db.insertWorkspace("Direktor odjelenja", 2, 7777);
        db.insertWorkspace("Direktor po vezi", 2, 7776);
        db.insertWorkspace("Radnik", 1, 4500);
        db.insertWorkspace("Radnik na minimalcu", 1, 4499);

        db.close();
    }

    public void printEmployees(View view) {
        Intent intent = new Intent(this, TableList.class);
        intent.putExtra("TABLE", "employees");
        startActivity(intent);
    }

    public void printWorkspace(View view) {
        Intent intent = new Intent(this, TableList.class);
        intent.putExtra("TABLE", "workspace");
        startActivity(intent);
    }

    public void findMaxSalary(View view) {
        db.open();

        String names = db.getHighestPaying();

        EditText editText = (EditText) findViewById(R.id.highest_paying);
        editText.setText(names);

        db.close();
    }
}
