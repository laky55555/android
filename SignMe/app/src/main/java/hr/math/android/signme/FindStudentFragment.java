package hr.math.android.signme;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.LinearLayout;
import android.widget.ResourceCursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class FindStudentFragment extends Fragment {

    DBAdapter db;
    private int lecture_id;
    private final String TAG = "FindStudentFragment";
    private AutoCompleteTextView textView;

    public static FindStudentFragment newInstance(int id) {
        FindStudentFragment fragmentDemo = new FindStudentFragment();
        Bundle args = new Bundle();
        args.putInt("lecture_id", id);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get back arguments
        lecture_id = getArguments().getInt("lecture_id", -1);
        if(lecture_id == -1) {
            Toast.makeText(getContext(), "Some error in lecture_id = " + lecture_id, Toast.LENGTH_LONG).show();
            Log.d(TAG, "Error in getting lecture id for activity, lecture_id = " + lecture_id);
            getActivity().getFragmentManager().popBackStack();
        }
    }

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

        Button check_attendance = (Button) view.findViewById(R.id.check_attendance);
        Button add_new_student = (Button) view.findViewById(R.id.add_new_student);

        check_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Toast.makeText(getActivity(), "Starting signature for student", Toast.LENGTH_LONG).show();
            checkExistingStudent();
            }
        });

        add_new_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Toast.makeText(getActivity(), "Creating new student: ", Toast.LENGTH_LONG).show();
            popUpNewStudents();
            }
        });

        // Setup any handles to view objects here
        textView = (AutoCompleteTextView) view.findViewById(R.id.list_of_names);
        // Get the string array
        //String[] countries = getResources().getStringArray(R.array.countries_array);
        // Create the adapter and set it to the AutoCompleteTextView
        db = new DBAdapter(getContext());
        db.open();
        //CursorAdapter adapter = new ClientCursorAdapter(getContext(), R.layout.fragmetn_select_name, db.getAllStudentsOfLecture(0), 0);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getContext(), android.R.layout.two_line_list_item,
                null, new String[]{"name", "JMBAG"}, new int[]{android.R.id.text1, android.R.id.text2});


        adapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor cursor) {
                final int colIndex = cursor.getColumnIndexOrThrow("name");
                return cursor.getString(colIndex) + " " + cursor.getString(2) + " " + cursor.getString(3);
            }
        });

        // This will run a query to find the descriptions for a given vehicle.
        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence description) {
                Cursor managedCursor = db.getAllStudentsOfLecture(lecture_id, description);
                Log.d("TESSST", "Query has " + managedCursor.getCount());
                Log.d("TESSST", "Number of students in lecture = " + db.numberOfStudents(lecture_id));
                return managedCursor;
            }
        });


        textView.setAdapter(adapter);
        //db.close();
    }

    private void noStudentMessage(String student)
    {
        Snackbar.make(getView(), "There is no student " + student,
                Snackbar.LENGTH_LONG).show();
    }

    private void checkExistingStudent() {
        String wholeText = textView.getText().toString();
        Log.d(TAG, wholeText);
        String[] text = wholeText.split(" ");
        if(text.length != 3)
            noStudentMessage(wholeText);
        else {
            int jmbag;
            try {
                jmbag = Integer.parseInt(text[2]);
            }
            catch (NumberFormatException e){
                noStudentMessage(wholeText);
                return;
            }
            int student_id = db.getStudentID(lecture_id, jmbag, text[0], text[1]);
            if(student_id == -1)
                noStudentMessage(wholeText);
            else
                startSigningScreen(false, student_id);
        }
    }

    private void popUpNewStudents() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.new_student);

        final LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText input_name = new EditText(getActivity());
        input_name.setHint(R.string.add_name);
        input_name.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(input_name);

        final EditText input_surname = new EditText(getActivity());
        input_surname.setHint(R.string.add_surname);
        input_surname.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(input_surname);

        final EditText input_jmbag = new EditText(getActivity());
        input_jmbag.setHint(R.string.add_jmbag);
        input_jmbag.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(input_jmbag);

        builder.setView(layout);

        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int new_student_id = addNewStudent(input_name.getText().toString(), input_surname.getText().toString(),
                        input_jmbag.getText().toString());
                if(new_student_id != -1)
                    startSigningScreen(true, new_student_id);
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

    private int addNewStudent(String name, String surname, String JMBAG)
    {
        int jmbag;
        try{
            jmbag = Integer.parseInt(JMBAG);
        }
        catch(NumberFormatException e){
            Log.d(TAG, "Given new student JMBAG = " + JMBAG + ", necessary true number");
            Toast.makeText(getContext(), "JMBAG number is mandatory", Toast.LENGTH_LONG).show();
            return -1;
        }

        db.open();
        if (!db.doesStudentExist(jmbag, lecture_id)) {
            db.newStudent(name, surname, jmbag, lecture_id);
            Toast.makeText(getContext(), "Added new student " + name, Toast.LENGTH_SHORT).show();
            return db.getStudentID(lecture_id, jmbag);

        }
        else
            Toast.makeText(getContext(), "Student with JMBAG " + JMBAG + " already exist.", Toast.LENGTH_SHORT).show();
        //db.close();
        return -1;
    }

    private void startSigningScreen(boolean learning_new, int student_id)
    {
        Toast.makeText(getContext(), "Starting fragment for drawing", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Starting fragment for drawing");
        // Begin the transaction
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.fragment, DrawingFragment.newInstance(learning_new, student_id, lecture_id));
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
    }

}