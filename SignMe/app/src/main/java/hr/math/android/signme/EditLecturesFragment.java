package hr.math.android.signme;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import hr.math.android.signme.Database.DBLectures;
import hr.math.android.signme.Dialogs.InitializePassEmailDialog;

import static hr.math.android.signme.Dialogs.AddLecture.popUpAdd;
import static hr.math.android.signme.Dialogs.DeleteLecture.popUpDelete;
import static hr.math.android.signme.Dialogs.EnterAirplaneSettings.enterAirplaneSettingsDialog;
import static hr.math.android.signme.Dialogs.SendEmailAttendance.popUpSend;

public class EditLecturesFragment extends Fragment {

    private DBLectures db;
    private CursorAdapter adapter;
    private Activity activity;
    private Cursor cursor;
    private boolean addNewLecture;
    private boolean editLectures;
    private View view;
    private Preferences pref;

    public static EditLecturesFragment newInstance(boolean editLectures, boolean addNewLecture) {
        EditLecturesFragment fragmentDemo = new EditLecturesFragment();
        Bundle args = new Bundle();
        args.putBoolean("editLectures", editLectures);
        args.putBoolean("addLecture", addNewLecture);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addNewLecture = getArguments().getBoolean("addLecture", false);
        editLectures = getArguments().getBoolean("editLectures", false);
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
        db = new DBLectures(getContext());
        db.open();
        cursor = db.getAllLectures();
        pref = new Preferences(getActivity());

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
        else {
            setOnListClickListener(listView);
            if(Settings.System.getInt(getContext().getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) == 0)
                enterAirplaneSettingsDialog(activity);
        }
        if(addNewLecture)
            popUpAdd(db, adapter, activity, cursor);
    }

    private void setOnListClickListener(final ListView listView)
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor lecture = (Cursor)listView.getItemAtPosition(position);

                if (!pref.isPasswordInitialized() || !pref.isEmailInitialized()) {
                    Intent intent = new Intent(getActivity(), InitializePassEmailDialog.class);
                    intent.putExtra("LECTURE_NAME", lecture.getString(1));
                    intent.putExtra("LECTURE_ID", lecture.getInt(0));
                    //intent.putExtra("PASSWORD", pref.isPasswordInitialized());
                    //intent.putExtra("MAIL", pref.isEmailInitialized());
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.current_password)
                            + " " + pref.getPassword(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity(), Signing.class);
                    intent.putExtra("LECTURE_NAME", lecture.getString(1));
                    intent.putExtra("LECTURE_ID", lecture.getInt(0));
                    startActivity(intent);
                }
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
                popUpAdd(db, adapter, activity, cursor);
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) view.findViewById(R.id.delete_marked_lectures);
        fab2.setVisibility(View.VISIBLE);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.deleting_marked_lectures, Snackbar.LENGTH_LONG).show();
                popUpDelete(getCheckedLectures(), db, adapter, activity, cursor);
                //popUpDelete(getCheckedLectures());
            }
        });

        FloatingActionButton fab3 = (FloatingActionButton) view.findViewById(R.id.send_statistic);
        fab3.setVisibility(View.VISIBLE);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.sending_stats, Snackbar.LENGTH_LONG).show();
                checkEmailInitializedSend();
            }
        });
        Log.d("EDIT", "Zavrsio inicijalizaciju");
    }

    private void checkEmailInitializedSend() {
        int result = 1;
        if (!pref.isEmailInitialized()) {
            Intent intent = new Intent(getActivity(), InitializePassEmailDialog.class);
            //startActivity(intent);
            startActivityForResult(intent, result);
        }
        else
            popUpSend(getCheckedLectures(), getContext(), view);
    }

    private ArrayList<String> getCheckedLectures()
    {
        ListView list = (ListView) view.findViewById(R.id.list_of_lectures_fragment);
        final ArrayList<String> checkedElements = new ArrayList<>(list.getAdapter().getCount());
        SparseBooleanArray checked = list.getCheckedItemPositions();
        for (int i = 0; i < list.getAdapter().getCount(); i++) {
            if (checked.get(i))
                checkedElements.add(((Cursor) list.getItemAtPosition(i)).getString(1));
        }

        return checkedElements;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (pref.isEmailInitialized())
            popUpSend(getCheckedLectures(), getContext(), view);
    }

}