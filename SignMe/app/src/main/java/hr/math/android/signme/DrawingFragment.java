package hr.math.android.signme;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DrawingFragment extends Fragment {

    private final static String TAG = "DrawingFragment";
    private int number_of_signatures = 1;
    private int number_of_drawings = 3;
    private int student_id;
    private int lecture_id;
    private boolean learning_new_signature;
    private DrawingView drawing_view;
    private TextView text;

    public static DrawingFragment newInstance(boolean learning_new, int student_id, int lecture_id) {
        DrawingFragment fragmentDemo = new DrawingFragment();
        Bundle args = new Bundle();
        args.putBoolean("learning_new", learning_new);
        args.putInt("student_id", student_id);
        args.putInt("lecture_id", lecture_id);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get back arguments
        learning_new_signature = getArguments().getBoolean("learning_new", false);
        student_id = getArguments().getInt("student_id", -1);
        lecture_id = getArguments().getInt("lecture_id", -1);
    }


    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.drawing, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        text = (TextView) view.findViewById(R.id.number_of_drawings);
        if(learning_new_signature) {
            //TODO: za testirati, mislim da se prvi slucaj nece nikad dogoditi
            if(number_of_drawings == 0)
                signedOnLesson(student_id, lecture_id, 0);
            else
                text.setText(getResources().getString(R.string.please_sign)
                        + " " + Integer.toString(number_of_drawings) + " " + getResources().getString(R.string.more_time));
        }
        else
            text.setText(R.string.please_sign);

        drawing_view = (DrawingView) view.findViewById(R.id.drawing_view);

        saveButtonListener(view);

        view.findViewById(R.id.button_discard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Discard current signature", Toast.LENGTH_LONG).show();
                drawing_view.discardSignature();
            }
        });
    }

    private void signedOnLesson(int student_id, int lecture_id, float signature_distance)
    {
        DBAttendance db = new DBAttendance(getContext());
        db.open();
        db.newAttendance(student_id, lecture_id, signature_distance);
        db.close();
        Toast.makeText(getContext(), "Starting fragment for finding student", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Starting fragment for finding student");
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.fragment, FindStudentFragment.newInstance(lecture_id));
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
    }


    private void saveButtonListener(View view) {
        view.findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Save current signature", Toast.LENGTH_LONG).show();

                // If we are learning new signature
                if(learning_new_signature) {
                    int result = drawing_view.saveSignature(number_of_signatures, student_id, lecture_id);

                    if (result == -1)
                        signatureTooLong();
                    else if (result == -2)
                        signatureTooShort();
                    else
                        updateNumberOfSignatures();
                }
                // If we are checking signature against already known signature in database
                else {
                    Toast.makeText(getActivity(), "Checking current signature", Toast.LENGTH_LONG).show();
                    float signature_result = drawing_view.checkSignature(student_id, lecture_id);

                    if (signature_result == -1)
                        signatureTooLong();
                    else if (signature_result == -2)
                        signatureTooShort();
                    else
                        signedOnLesson(student_id, lecture_id, signature_result);
                }
            }
        });
    }

    private void updateNumberOfSignatures() {
        number_of_signatures++;
        number_of_drawings--;
        text.setText(getResources().getString(R.string.please_sign)
                + " " + Integer.toString(number_of_drawings) + " " + getResources().getString(R.string.more_time));
        if (number_of_drawings == 0)
            signedOnLesson(student_id, lecture_id, 0);
    }

    private void signatureTooShort() {
        Log.d(TAG, "Too few coordinates. Number of coordinates must be over 200");
        Toast.makeText(getContext(), "Try to sign slower or choose longer signature ;).\nYou are too fast.", Toast.LENGTH_LONG).show();
    }

    private void signatureTooLong() {
        Log.d(TAG, "Too many coordinates. Number of coordinates must be less than " + DBAdapter.number_of_coords);
        Toast.makeText(getContext(), "Try to sign faster ;).\nYou are too slow.", Toast.LENGTH_LONG).show();
    }
}