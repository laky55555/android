package hr.math.android.kolokvij2;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class TableList extends AppCompatActivity {

    DBAdapter db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_list);
        db = new DBAdapter(this);

        if(getIntent().hasExtra("TABLE")) {

            if(getIntent().getStringExtra("TABLE").equals("workspace"))
                printTableWorkspace();
            else
                printTableEmployees();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_table_list, menu);
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

    private void printTableEmployees() {

        db.open();
        Cursor cursor = db.getAllEmployees();

        SimpleCursorAdapter adapter;

        String[] columns = new String[] {
                DBAdapter.NAME,
                DBAdapter.EMAIL,
                DBAdapter.SALARY};

        int[] views = new int[] {R.id.first, R.id.second, R.id.third};

        adapter = new SimpleCursorAdapter(
                this, R.layout.activity_table_list, cursor, columns, views,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        final ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        db.close();
    }

    private void printTableWorkspace() {

        db.open();
        Cursor cursor = db.getAllWorkplaces();

        SimpleCursorAdapter adapter;

        String[] columns = new String[] {
                DBAdapter.POSITION_NAME,
                DBAdapter.RESPONSIBILITY,
                DBAdapter.MIN_SALARY};

        int[] views = new int[] {R.id.first, R.id.second, R.id.third};

        adapter = new SimpleCursorAdapter(
                this, R.layout.activity_table_list, cursor, columns, views,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        final ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        db.close();
    }

}
