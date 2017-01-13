package hr.math.android.signme;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DrawingFragment extends Fragment {

    private final static String TAG = "DrawingFragment";
    private int number_of_drawings;
    private int student_id;
    private int lecture_id;
    private boolean learning;
    private DrawingView drawing_view;
    private TextView text;

    public static DrawingFragment newInstance(int number_of_drawings, int student_id, int lecture_id) {
        DrawingFragment fragmentDemo = new DrawingFragment();
        Bundle args = new Bundle();
        args.putInt("number_of_drawings", number_of_drawings);
        args.putInt("student_id", student_id);
        args.putInt("lecture_id", lecture_id);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get back arguments
        number_of_drawings = getArguments().getInt("number_of_drawings", -2);
        student_id = getArguments().getInt("student_id", -1);
        lecture_id = getArguments().getInt("lecture_id", -1);
        if(number_of_drawings == -2) {
            Toast.makeText(getContext(), "Some error in number_of_drawings = " + number_of_drawings, Toast.LENGTH_LONG).show();
            Log.d(TAG, "Error in getting number_of_drawings = " + number_of_drawings);
            getActivity().getFragmentManager().popBackStack();
        }
        else if(number_of_drawings == -1) {
            learning = false;
        }
        else {
            learning = true;
        }
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
        if(number_of_drawings == 0) {
            //TODO: tu treba ubaciti studenta da je bio na predavanju i zahvaliti mu te izaci iz fragmenta
        }
        else if(number_of_drawings == -1)
           text.setText(R.string.please_sign);
        else
            text.setText(getResources().getString(R.string.please_sign)
                    + " " + Integer.toString(number_of_drawings) + " " + getResources().getString(R.string.more_time));

        drawing_view = (DrawingView) view.findViewById(R.id.drawing_view);

        view.findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Save current signature", Toast.LENGTH_LONG).show();
                if(!drawing_view.saveSignature(number_of_drawings, student_id, lecture_id)) {
                    Log.d(TAG, "Too many coordinates. Number of coordinates must be less than 1000");
                    Toast.makeText(getContext(), "Try to sign faster ;).\nYou are too slow.", Toast.LENGTH_LONG).show();
                    return;
                }
                number_of_drawings--;
                if(number_of_drawings == -2)
                    //TODO: pozvati provjeru da li je potpis moj i napraviti odgovarajuce
                if(number_of_drawings == 0)
                    //TODO: izbaciti iz ovog pogleda i upisti ga u posjecene
                text.setText(getResources().getString(R.string.please_sign)
                    + " " + Integer.toString(number_of_drawings) + " " + getResources().getString(R.string.more_time));
            }
        });

        view.findViewById(R.id.button_discard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Discard current signature", Toast.LENGTH_LONG).show();
                drawing_view.discardSignature();
            }
        });
    }

}