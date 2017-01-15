package hr.math.android.signme;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LecturesListFragment extends Fragment {

    private DBAdapter db;
    //private CheckedTextView ctv;

        // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment

        return inflater.inflate(R.layout.fragment_lectures_list, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here

        final ListView listView = (ListView) view.findViewById(R.id.list_of_lectures_fragment);

        //listView.setChoiceMode();
        //listView.setOnItemClickListener();
        db = new DBAdapter(getContext());
        db.open();

        final CursorAdapter adapter = new CursorAdapter(getContext(), db.getAllLectures()) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                //return LayoutInflater.from(context).inflate(R.layout.nova_lista, parent, false);
                return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_checked, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                //CheckedTextView novi = (CheckedTextView) view.findViewById(R.id.neka_lista);
                CheckedTextView novi = (CheckedTextView) view.findViewById(android.R.id.text1);
                novi.setText(cursor.getString(1));
                //TextView tvBody = (TextView) view.findViewById(R.id.lecture_id);
                //TextView tvPriority = (TextView) view.findViewById(R.id.lecture_name);
                // Extract properties from cursor
                //String body = cursor.getString(2);
                //int priority = cursor.getInt(2);
                // Populate fields with extracted properties
                //tvBody.setText(body);
                //tvPriority.setText(String.valueOf(priority));
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor lecture = (Cursor)listView.getItemAtPosition(position);

//                if(isMyAppLauncherDefault())
//                    Log.d("Provjera launchr", "MOJ JE");
//                else
//                    Log.d("Provjera launchr", "Nije MOJ");

                /*if(!isMyAppLauncherDefault()) {
                    Toast.makeText(getActivity(), "NIJE LAUNCHER", Toast.LENGTH_SHORT).show();
                    Log.d("RESET", "ajmo launcher");
                    resetPreferredLauncherAndOpenChooser(getContext());
                }*/
                //else {
                    //getActivity().finishActivity(Signing.class);
                    Intent intent = new Intent(getActivity(), Signing.class);
                    intent.putExtra("LECTURE_NAME", lecture.getString(1));
                    intent.putExtra("LECTURE_ID", lecture.getInt(0));
                    startActivity(intent);
                //}
            }
        });
        db.close();
    }

}