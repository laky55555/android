package hr.math.android.signme;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditLecturesFragment extends Fragment {

    private DBAdapter db;
    private CursorAdapter adapter;
    private Activity activity;
    private Cursor cursor;
    private boolean addNewLecture;
    private boolean editLectures;
    private View view;


    public static EditLecturesFragment newInstance(boolean editLectures, boolean addNewLecture) {
        EditLecturesFragment fragmentDemo = new EditLecturesFragment();
        Bundle args = new Bundle();
        args.putBoolean("edit_lectures", editLectures);
        args.putBoolean("add_lecture", addNewLecture);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addNewLecture = getArguments().getBoolean("add_lecture", false);
        editLectures = getArguments().getBoolean("edit_lectures", false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lectures_list, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d("EDIT", "TU SAMMMMMMMMM");
        activity = getActivity();
        this.view = view;
        db = new DBAdapter(getContext());
        db.open();
        cursor = db.getAllLectures();

        adapter = new CursorAdapter(getContext(), cursor) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                int listType;
                if(editLectures)
                    listType = android.R.layout.simple_list_item_checked;
                else
                    listType = android.R.layout.simple_selectable_list_item;
                return LayoutInflater.from(context).inflate(listType, parent, false);
            }
            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                ((CheckedTextView) view.findViewById(android.R.id.text1)).setText(cursor.getString(1));
            }
        };

        final ListView listView = (ListView) view.findViewById(R.id.list_of_lectures_fragment);
        listView.setAdapter(adapter);

        if(editLectures)
            initializeFloatingActionButton();
        else
            setOnListClickListener(listView);

        if(addNewLecture)
            popUpAdd();
    }

    private void setOnListClickListener(final ListView listView)
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor lecture = (Cursor)listView.getItemAtPosition(position);
                Password pass = new Password(getActivity());
                if(!pass.isPasswordInitialized()) {
                    //TODO: ne dopustiti korisniku da ide u potpisivanje ako pass nije postavio
                    //popUpInitializePass return da li je inicijaliziran
                    //ako vrati false return;
                }

                Toast.makeText(getContext(), getResources().getString(R.string.current_password)
                        + " " + pass.getPassword(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), Signing.class);
                intent.putExtra("LECTURE_NAME", lecture.getString(1));
                intent.putExtra("LECTURE_ID", lecture.getInt(0));
                startActivity(intent);
            }
        });
    }

    private void initializeFloatingActionButton() {

        Log.d("EDIT", "inicijaliziram");
        FloatingActionButton fab1 = (FloatingActionButton) getView().findViewById(R.id.add_new_lecture);
        Log.d("EDIT", "View.VISIBLE = " + View.VISIBLE + ", View.INVISIBLE = " + View.INVISIBLE);
        if(fab1.getVisibility() == View.INVISIBLE) {
            fab1.setVisibility(View.VISIBLE);
        }
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.adding_new_lectures, Snackbar.LENGTH_LONG).show();
                popUpAdd();
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) view.findViewById(R.id.delete_marked_lectures);
        fab2.setVisibility(View.VISIBLE);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.deleting_marked_lectures, Snackbar.LENGTH_LONG).show();
                popUpDelete(getCheckedLectures());
            }
        });

        FloatingActionButton fab3 = (FloatingActionButton) view.findViewById(R.id.send_statistic);
        fab3.setVisibility(View.VISIBLE);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.sending_stats + "To jos trevba napraviti", Snackbar.LENGTH_LONG).show();
                popUpSend();
            }
        });
        Log.d("EDIT", "Zavrsio inicijalizaciju");
    }

    private ArrayList<String> getCheckedLectures()
    {
        ListView list = (ListView) view.findViewById(R.id.list_of_lectures_fragment);
        final ArrayList<String> forDelete = new ArrayList<>(list.getAdapter().getCount());
        SparseBooleanArray checked = list.getCheckedItemPositions();
        for (int i = 0; i < list.getAdapter().getCount(); i++) {
            if (checked.get(i))
                forDelete.add(((Cursor) list.getItemAtPosition(i)).getString(1));
        }

        return forDelete;
    }

    private void popUpAdd()
    {
        Toast.makeText(getContext(), "POP up add", Toast.LENGTH_SHORT);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.add_lecture);

        final LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText input_name = new EditText(activity);
        input_name.setHint(R.string.add_lecture);
        input_name.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(input_name);

        builder.setView(layout);

        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addNewLecture(input_name.getText().toString());
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

    private void addNewLecture(String name) {

        if (!db.doesLectureExist(name)) {
            DBLectures dbl = new DBLectures(getContext());
            dbl.open();
            dbl.newLecture(name);
            Toast.makeText(activity, "Added new lecture", Toast.LENGTH_SHORT).show();
            cursor.requery();
            adapter.notifyDataSetChanged();
        } else
            Toast.makeText(activity, "Lecture with " + name + " already exist.", Toast.LENGTH_SHORT).show();
    }


    private void popUpDelete(final ArrayList<String> forDelete) {
        if(forDelete.size() < 1)
            return;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.deleting_marked_lectures);

        final TextView info = new TextView(activity);
        info.setText(getResources().getString(R.string.delete_warning) + ":\n" + forDelete.toString());
        builder.setView(info);

        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < forDelete.size(); ++i)
                    db.deleteLecture(forDelete.get(i));
                cursor.requery();
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

    private void popUpSend()
    {
        Toast.makeText(getContext(), "POP up send", Toast.LENGTH_SHORT);
    }

}