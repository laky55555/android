package hr.math.android.signme;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import hr.math.android.signme.Database.DBStudents;

import static hr.math.android.signme.Dialogs.NewStudent.popUpNewStudents;

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
        return inflater.inflate(R.layout.fragment_select_name, parent, false);
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
            popUpNewStudents(getActivity(), lectureId, db);
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

    private void startSigningScreen(boolean learningNew, int studentId)
    {
        Toast.makeText(getContext(), "Starting fragment for fragment_drawing", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Starting fragment for fragment_drawing");
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, DrawingFragment.newInstance(learningNew, studentId, lectureId));
        ft.commit();
    }

}