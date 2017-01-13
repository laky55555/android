package hr.math.android.signme;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.FilterQueryProvider;
import android.widget.ResourceCursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class FindStudentFragment extends Fragment {

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragmetn_select_name, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.list_of_names);
        // Get the string array
        //String[] countries = getResources().getStringArray(R.array.countries_array);
        // Create the adapter and set it to the AutoCompleteTextView
        final DBAdapter db = new DBAdapter(getContext());
        db.open();
        //CursorAdapter adapter = new ClientCursorAdapter(getContext(), R.layout.fragmetn_select_name, db.getAllStudentsOfLecture(0), 0);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getContext(), android.R.layout.simple_dropdown_item_1line,
                null, new String[] {"name"}, new int[] { android.R.id.text1 });


        adapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor cursor) {
                final int colIndex = cursor.getColumnIndexOrThrow("name");
                return cursor.getString(colIndex);
            }
        });

        // This will run a query to find the descriptions for a given vehicle.
        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence description) {
                Cursor managedCursor = db.getAllStudentsOfLecture(1, description);
                Log.d("TESSST", "Query has " + managedCursor.getCount());
                Log.d("TESSST", "Number of students in lecture = " + db.numberOfStudents(1));
                return managedCursor;
            }
        });


        textView.setAdapter(adapter);
        //db.close();
    }

}