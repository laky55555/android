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
import android.view.WindowManager;
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

    DBStudents db;
    private int lectureId;
    private final String TAG = "FindStudentFragment";
    private AutoCompleteTextView textView;

    public static FindStudentFragment newInstance(int id) {
        FindStudentFragment fragmentDemo = new FindStudentFragment();
        Bundle args = new Bundle();
        args.putInt("lectureId", id);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get back arguments
        lectureId = getArguments().getInt("lectureId", -1);
        if(lectureId == -1) {
            Toast.makeText(getContext(), "Some error in lectureId = " + lectureId, Toast.LENGTH_LONG).show();
            Log.d(TAG, "Error in getting lecture id for activity, lectureId = " + lectureId);
            getActivity().getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmetn_select_name, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        Button check_attendance = (Button) view.findViewById(R.id.check_attendance);
        Button add_new_student = (Button) view.findViewById(R.id.add_new_student);

        check_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            checkExistingStudent();
            }
        });

        add_new_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            popUpNewStudents();
            }
        });

        db = new DBStudents(getContext());
        db.open();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getContext(), android.R.layout.two_line_list_item,
                null, new String[]{"name", "JMBAG"}, new int[]{android.R.id.text1, android.R.id.text2});


        adapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor cursor) {
                return cursor.getString(1) + " " + cursor.getString(2) + " " + cursor.getString(3);
            }
        });

        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence description) {
                Cursor managedCursor = db.getAllStudentsOfLecture(lectureId, description);
                Log.d("AutoComplete", "Query has " + managedCursor.getCount());
                Log.d("AutoComplete", "Number of students in lecture = " + db.numberOfStudents(lectureId));
                return managedCursor;
            }
        });

        textView = (AutoCompleteTextView) view.findViewById(R.id.list_of_names);
        textView.setAdapter(adapter);
    }

    private void noStudentMessage(String student)
    {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        Snackbar.make(getView(), "There is no student " + student,
                Snackbar.LENGTH_LONG).show();
    }

    private void badStudentInfo(String name, String surname, String jmbag)
    {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        Snackbar.make(getView(), "Given student info is not valid.\nName = " + name + "; Surname = "
                + surname + "; JMBAG = " + jmbag, Snackbar.LENGTH_LONG).show();
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
            int studentId = db.getStudentID(lectureId, jmbag, text[0], text[1]);
            if(studentId == -1)
                noStudentMessage(wholeText);
            else
                startSigningScreen(false, studentId);
        }
    }

    private void popUpNewStudents() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.new_student);

        final LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText inputName = new EditText(getActivity());
        inputName.setHint(R.string.add_name);
        inputName.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(inputName);

        final EditText inputSurname = new EditText(getActivity());
        inputSurname.setHint(R.string.add_surname);
        inputSurname.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(inputSurname);

        final EditText inputJmbag = new EditText(getActivity());
        inputJmbag.setHint(R.string.add_jmbag);
        inputJmbag.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(inputJmbag);

        builder.setView(layout);

        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int new_studentId = addNewStudent(inputName.getText().toString(), inputSurname.getText().toString(),
                        inputJmbag.getText().toString());
                if(new_studentId != -1)
                    startSigningScreen(true, new_studentId);
                else
                    badStudentInfo(inputName.getText().toString(), inputSurname.getText().toString(),
                            inputJmbag.getText().toString());
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
        if(name.trim().length() == 0) {
            Log.d(TAG, "Given new student name = " + name + ", necessary student name");
            Toast.makeText(getContext(), "Student name is mandatory", Toast.LENGTH_LONG).show();
            return -1;
        }
        if(surname.trim().length() == 0) {
            Log.d(TAG, "Given new student surname = " + surname + ", necessary student surname");
            Toast.makeText(getContext(), "Student surname is mandatory", Toast.LENGTH_LONG).show();
            return -1;
        }
        try{
            jmbag = Integer.parseInt(JMBAG);
        }
        catch(NumberFormatException e){
            Log.d(TAG, "Given new student JMBAG = " + JMBAG + ", necessary true number");
            Toast.makeText(getContext(), "JMBAG number is mandatory", Toast.LENGTH_LONG).show();
            return -1;
        }

        if (!db.doesStudentExist(lectureId, jmbag)) {
            db.newStudent(name, surname, jmbag, lectureId);
            Toast.makeText(getContext(), "Added new student " + name, Toast.LENGTH_SHORT).show();
            return db.getStudentID(lectureId, jmbag);

        }
        else
            Toast.makeText(getContext(), "Student with JMBAG " + JMBAG + " already exist.", Toast.LENGTH_SHORT).show();
        return -1;
    }

    private void startSigningScreen(boolean learningNew, int studentId)
    {
        Toast.makeText(getContext(), "Starting fragment for drawing", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Starting fragment for drawing");
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, DrawingFragment.newInstance(learningNew, studentId, lectureId));
        ft.commit();
    }

}