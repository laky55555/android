package hr.math.android.signme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class LecturesList extends AppCompatActivity {
    ArrayList<String> lectures;
    ArrayList<String> lectures_ids;

    private DBAdapter db;
    //private CursorAdapter adapter;
    private ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectures_list);

        db = new DBAdapter(this);


        /*db.open();
        adapter = new CursorAdapter(this, db.getAllLectures()) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(context).inflate(R.layout.nova_lista, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView tvBody = (TextView) view.findViewById(R.id.lecture_id);
                TextView tvPriority = (TextView) view.findViewById(R.id.lecture_name);
                // Extract properties from cursor
                String body = cursor.getString(1);
                int priority = cursor.getInt(2);
                // Populate fields with extracted properties
                tvBody.setText(body);
                tvPriority.setText(String.valueOf(priority));
            }
        };*/

        ListView listView = (ListView)findViewById(R.id.list_of_lectures);
        initializeAllLectures();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, lectures);
        listView.setAdapter(adapter);

        initializeFloatingActionButton();

        Intent intent = getIntent();
        if(intent.hasExtra(MainActivity.PARAMETER_ADD))
            findViewById(R.id.add_new_lecture).performClick();
    }

    private void initializeFloatingActionButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_new_lecture);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.adding_new_lectures , Snackbar.LENGTH_LONG).show();
                popUpAdd();
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.delete_marked_lectures);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.deleting_marked_lectures, Snackbar.LENGTH_LONG).show();
                final ArrayList<String> forDelete = new ArrayList<>(lectures.size());
                ListView list = (ListView) findViewById(R.id.list_of_lectures);
                SparseBooleanArray checked = list.getCheckedItemPositions();
                for (int i = 0; i < list.getAdapter().getCount(); i++) {
                    if (checked.get(i))
                        forDelete.add(lectures.get(i));
                }
                if(forDelete.size() > 0)
                    popUpDelete(forDelete);
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.send_statistic);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.sending_stats + "To jos trevba napraviti", Snackbar.LENGTH_LONG).show();
                popUpSend();
            }
        });
    }

    private void addNewLecture(String name, String id) {

        Long id_number;
        try{
            id_number = Long.parseLong(id);
        }
        catch(NumberFormatException e){
            Log.d("Lecture ID", "Given lecture ID = " + id + ", assigning -1.");
            id_number = -1L;
        }

        db.open();
        if (!db.doesExist(name)) {
            db.newLecture(name, id_number);
            Log.d("ADD", "Added " + name + " with id " + id_number.toString() + " to database.");
            Toast.makeText(this, "Added new lecture", Toast.LENGTH_SHORT).show();
            lectures.add(name);
            lectures_ids.add(id_number.toString());
            adapter.notifyDataSetChanged();
        }
        else
            Toast.makeText(this, "Lecture with " + name + " already exist.", Toast.LENGTH_SHORT).show();
        db.close();

    }

    private void initializeAllLectures() {
        lectures = new ArrayList<String>();
        lectures_ids = new ArrayList<String>();

        db.open();
        Cursor c = db.getAllLectures();
        if (c.moveToFirst()) {
            do {
                lectures.add(c.getString(1));
                lectures_ids.add(c.getString(2));
            } while (c.moveToNext());
        }
        db.close();
    }

    private void popUpAdd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add_lecture);

        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText input_name = new EditText(this);
        input_name.setHint(R.string.add_lecture);
        input_name.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        layout.addView(input_name);

        final EditText input_id = new EditText(this);
        input_id.setHint(R.string.add_lecture_id);
        input_id.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
        layout.addView(input_id);

        builder.setView(layout);

        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addNewLecture(input_name.getText().toString(), input_id.getText().toString());
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void popUpDelete(final ArrayList<String> forDelete) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.deleting_marked_lectures);

        final TextView info = new TextView(this);
        info.setText(getResources().getString(R.string.delete_warning) + ":\n" + forDelete.toString());
        builder.setView(info);

        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.open();
                for(int i = 0; i<forDelete.size(); ++i) {
                    db.deleteLecture(forDelete.get(i));
                    lectures.remove(forDelete.get(i));
                }
                db.close();
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    //TODO: tu jos treba napraviti slanje
    private void popUpSend() {

    }

}